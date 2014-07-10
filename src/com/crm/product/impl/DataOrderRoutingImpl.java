package com.crm.product.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;
import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductMessage;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.impl.ccws.CCWSCommandImpl;
import com.crm.provisioning.impl.ccws.CCWSConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.SubscriberEntryImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.DateUtil;
import com.crm.util.GeneratorSeq;
import com.crm.util.StringUtil;
import com.fss.util.AppException;

public class DataOrderRoutingImpl extends VNMOrderRoutingImpl
{
	public void checkActionType(OrderRoutingInstance instance,
			ProductRoute orderRoute, ProductEntry product,
			CommandMessage order, SubscriberProduct subscriberProduct)
			throws Exception
	{
		Date now = new Date();

		try
		{
			if (subscriberProduct != null)
			{
				int remainDays = 0;
				if (subscriberProduct.getExpirationDate() != null)
				{
					remainDays = DateUtil.getDateDiff(now,
							subscriberProduct.getExpirationDate());
				}

				if (remainDays < 0)
				{
					remainDays = 0;
				}

				order.setResponseValue("service.activeDays", remainDays);
				order.setResponseValue("service.activeDate",
						subscriberProduct.getExpirationDate());
			}

			if (subscriberProduct != null)
			{
				order.setSubProductId(subscriberProduct.getSubProductId());
			}

			String actionType = order.getActionType();

			if (actionType.equals(Constants.ACTION_REGISTER)
					&& (subscriberProduct != null))
			{
				if (orderRoute.isTopupEnable())
				{
					actionType = Constants.ACTION_TOPUP;
				}
				else
				{
					actionType = Constants.ACTION_REGISTER;
				}
			}

			if (product.isSubscription())
			{
				if ((subscriberProduct == null) || subscriberProduct.isCancel())
				{
					if (actionType.equals(Constants.ACTION_SUBSCRIPTION))
					{
						throw new AppException(
								Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
					}
					else if (actionType.equals(Constants.ACTION_UNREGISTER))
					{
						throw new AppException(
								Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
					}
					else if (actionType.equals(Constants.ACTION_CANCEL))
					{
						throw new AppException(
								Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
					}
					else if (actionType.equals(Constants.ACTION_TOPUP))
					{
						actionType = Constants.ACTION_REGISTER;
					}
				}
				else if (subscriberProduct != null)
				{
					if (orderRoute.isTopupEnable())
					{
						actionType = Constants.ACTION_TOPUP;
					}
					else if (actionType.equals(Constants.ACTION_SUBSCRIPTION))
					{
						if (DateUtil.compareDate(
								subscriberProduct.getExpirationDate(), now) >= 0)
						{
							actionType = Constants.ACTION_SUBSCRIPTION;
						}
					}
				}
			}

			// get associate product
			if (actionType.equals(Constants.ACTION_REGISTER)
					|| actionType.equals(Constants.ACTION_UPGRADE))
			{
				checkBlacklist(instance, product, order);

				checkUpgrade(instance, product, order);

				if (order.getAssociateProductId() != Constants.DEFAULT_ID)
				{
					actionType = Constants.ACTION_UPGRADE;
				}
			}

			order.setActionType(actionType);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public CommandMessage parser(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		Exception error = null;

		ProductEntry product = null;

		SubscriberProduct subscriberProduct = null;

		try
		{
			// check SMS syntax
			if (order.getChannel().equals("SMS"))
			{
				smsParser(instance, orderRoute, order);
			}

			// check duplicate request
			if (orderRoute.getDuplicateScan() > 0)
			{
				checkDuplicate(instance, orderRoute, order);
			}

			if (orderRoute.getMaxRegisterDaily() > 0)
			{
				checkMaxRegister(instance, orderRoute, order);
			}

			// check product in available list
			product = ProductFactory.getCache()
					.getProduct(order.getProductId());

			// check promotion
			if (orderRoute.isCheckPromotion())
			{
				checkPromotion(instance, orderRoute, order, product.getAlias());
			}

			// get current subscriber product
			if (order.getSubProductId() == Constants.DEFAULT_ID)
			{
				subscriberProduct = SubscriberProductImpl.getActive(
						order.getIsdn(), order.getProductId());
			}
			else
			{
				subscriberProduct = SubscriberProductImpl.getProduct(order
						.getSubProductId());
			}

			// check action type
			checkActionType(instance, orderRoute, product, order,
					subscriberProduct);

			// validate
			if (orderRoute.isCheckBalance())
			{
				order = checkBalance(instance, orderRoute, order);
				order.getParameters().setProperty("IsQueryRTBS", "true");
			}
			else
			{
				if (order.getSubscriberType() == Constants.UNKNOW_SUB_TYPE)
				{
					order.setSubscriberType(SubscriberEntryImpl
							.getSubscriberType(order.getIsdn()));
				}

				order.setAmount(order.getQuantity() * order.getPrice());
			}

			if ((order.getStatus() == Constants.ORDER_STATUS_DENIED)
					&& order.getCause()
							.equals(Constants.ERROR_NOT_ENOUGH_MONEY))
			{
				if (order.getActionType().equals(Constants.ACTION_SUBSCRIPTION))
				{
					ProductMessage productMessage = product.getProductMessage(
							order.getActionType(), order.getCampaignId(),
							order.getLanguageId(), order.getChannel(),
							order.getCause());
					if (productMessage != null)
					{
						String content = productMessage.getContent();
						if (subscriberProduct.getExpirationDate() != null)
						{
							content = content.replaceAll(
									"~SERVICE_EXPIRE_DATE~", StringUtil.format(
											subscriberProduct
													.getExpirationDate(),
											"dd/MM/yyyy"));
							SubscriberEntity subscriberEntity = ((VNMMessage) order)
									.getSubscriberEntity();
							BalanceEntity balance = CCWSConnection.getBalance(
									subscriberEntity, "GPRS");

							double convertRatio = Double.parseDouble(product
									.getParameter("ConvertRatio",
											"0.00000095367431640625"));
							content = content.replaceAll(
									"~SERVICE_BALANCE~",
									StringUtil.format(
											balance.getAvailableBalance()
													* convertRatio, "#,##0"));
						}
						SubscriberProductImpl.insertSendSMS(
								product.getParameter("ProductShotCode", ""),
								order.getIsdn(), content);
					}

					order.setActionType(Constants.ACTION_UNREGISTER);

					order.setCause("");

					order.setStatus(Constants.ORDER_STATUS_PENDING);
				}
				else
				{
					if (orderRoute.isCheckPromotion()
							&& order.getActionType().equals(
									Constants.ACTION_REGISTER))
					{
						long lastCampaignId = order.getCampaignId();
						checkPromotion(instance, orderRoute, order,
								product.getAlias() + "."
										+ Constants.ERROR_NOT_ENOUGH_MONEY);
						if (order.getCampaignId() != lastCampaignId)
						{
							order.getParameters()
									.setBoolean("FreeOneDay", true);
							order.setCause("");
							order.setStatus(Constants.ORDER_STATUS_PENDING);
						}
					}
				}
			}
			else
			{
				checkSubscriberType(instance, product, order);
			}

			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
				validate(instance, orderRoute, order);
			}
		}
		catch (Exception e)
		{
			error = e;
		}

		if (error != null)
		{
			order.setStatus(Constants.ORDER_STATUS_DENIED);

			if (error instanceof AppException)
			{
				order.setCause(error.getMessage());
			}
			else
			{
				order.setDescription(error.getMessage());
			}
		}

		if (order.getActionType().equals(Constants.ACTION_SUBSCRIPTION))
		{
			if (subscriberProduct == null)
			{
				order.setStatus(Constants.ORDER_STATUS_DENIED);
				order.setCause(Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
			}

			String currentState = "";
			try
			{
				currentState = getSubscriberState(instance, orderRoute,
						product, order);
			}
			catch (Exception e)
			{

			}

			if (!currentState.equals(Constants.BALANCE_STATE_ACTIVE)
					&& order.getStatus() == Constants.ORDER_STATUS_DENIED)
			{
				order.setActionType(Constants.ACTION_UNREGISTER);

				order.setDescription(order.getCause());
				order.setCause("");
				/**
				 * unregister for all subtype = prepaid subtype
				 */
				order.setSubscriberType(Constants.PREPAID_SUB_TYPE);
				order.setStatus(Constants.ORDER_STATUS_PENDING);
			}
		}

		if ((error != null) && !(error instanceof AppException))
		{
			throw error;
		}

		order.getParameters().setBoolean("includeCurrentDay", true);

		return order;
	}
	
	public CommandMessage inviteService(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		Exception error = null;

		ProductEntry product = null;

		SubscriberProduct subscriberProduct = null;

		try
		{
			// check SMS syntax
			if (order.getChannel().equals("SMS"))
			{
				smsParser(instance, orderRoute, order);
			}

			// check duplicate request
			if (orderRoute.getDuplicateScan() > 0)
			{
				checkDuplicate(instance, orderRoute, order);
			}

			// check product in available list
			product = ProductFactory.getCache()
					.getProduct(order.getProductId());

			order.getParameters().setString("INVITER_ISDN", order.getIsdn());
			
			order.setResponseValue(ResponseUtil.LEADER, order.getIsdn());
			order.setResponseValue(ResponseUtil.SERVICE_ALIAS, product.getAlias());
			
			// validate
			if (orderRoute.isCheckBalance())
			{
				order = checkBalanceInvite(instance, orderRoute, order, true);
			}
			else
			{
				if (order.getSubscriberType() == Constants.UNKNOW_SUB_TYPE)
				{
					order.setSubscriberType(SubscriberEntryImpl
							.getSubscriberType(order.getIsdn()));
				}

				order.setAmount(order.getQuantity() * order.getPrice());
			}
			
			order.getParameters().setInteger("INVITER_SUBSCRIBERTYPE", order.getSubscriberType());

			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
				checkSubscriberType(instance, product, order);
				order.setProvisioningType("ROUTE");
				instance.sendCommandLog(order);
				
				String inviteeIsdn = CommandUtil.addCountryCode(order.getParameters().getString("sms.params[0]"));
				verifyNumber(inviteeIsdn);
				
				order.setResponseValue(ResponseUtil.REFERAL, inviteeIsdn);
				
				order.getParameters().setString("INVITEE_ISDN", inviteeIsdn);
				order.setIsdn(inviteeIsdn);
				
				order = checkBalanceInvite(instance, orderRoute, order, false);
				order.getParameters().setProperty("IsQueryRTBS", "true");
				if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
				{
					try
					{
						checkSubscriberType(instance, product, order);
					}
					catch (AppException e)
					{
						order.setCause(e.getMessage() + ".deliver");
						order.setDescription(e.getContext());
						order.setStatus(Constants.ORDER_STATUS_DENIED);
					}
				}
				else
				{
					order.setIsdn(order.getParameters().getString("INVITER_ISDN"));
					throw new AppException(order.getCause() + ".deliver");
				}
			}

			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
				validate(instance, orderRoute, order);
			}
		}
		catch (Exception e)
		{
			error = e;
		}

		if (error != null)
		{
			order.setStatus(Constants.ORDER_STATUS_DENIED);

			if (error instanceof AppException)
			{
				order.setCause(error.getMessage());
			}
			else
			{
				order.setDescription(error.getMessage());
			}
		}
		
		if ((error != null) && !(error instanceof AppException))
		{
			throw error;
		}

		order.getParameters().setBoolean("includeCurrentDay", true);

		return order;
	}
	
	public CommandMessage notInviteService(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		return order;
	}

	public CommandMessage checkBalanceInvite(OrderRoutingInstance instance, ProductRoute orderRoute, CommandMessage order, boolean isInviter)
			throws Exception
	{
		ProductEntry product = null;

		CCWSConnection connection = null;

		SubscriberRetrieve subscriberRetrieve = null;

		SubscriberEntity subscriberEntity = null;

		VNMMessage vnmMessage = CommandUtil.createVNMMessage(order);

		if ((instance.getDebugMode().equals("depend")))
		{
			simulation(instance, orderRoute, vnmMessage);
		}
		else
		{
			try
			{
				long productId = vnmMessage.getProductId();

				product = ProductFactory.getCache().getProduct(productId);

				connection = (CCWSConnection) instance.getProvisioningConnection();

				// get subscriber information in CCWS
				int queryLevel = orderRoute.getParameters().getInteger("prepaid.queryLevel", 1);

				try
				{
					int sessionId = 0;
					try
					{
						sessionId = GeneratorSeq.getNextSeq();
					}
					catch (Exception e)
					{
					}
					String strRequest = (new CCWSCommandImpl())
							.getLogRequest("com.comverse_in.prepaid.ccws.ServiceSoapStub.retrieveSubscriberWithIdentityNoHistory", vnmMessage.getIsdn());
					instance.logMonitor("SEND: " + strRequest + ". Product= " + product.getAlias() + ". ID=" + sessionId);
					vnmMessage.setRequest("SEND: " + strRequest + ". Product= " + product.getAlias() + ". ID=" + sessionId);
					
					Date startTime = new Date();
					vnmMessage.setRequestTime(new Date());
					
					subscriberRetrieve = connection.getSubscriber(vnmMessage.getIsdn(), queryLevel);
					Date endTime = new Date();
					long costTime = CommandUtil.calculateCostTime(startTime, endTime);
					if (subscriberRetrieve != null)
					{
						subscriberEntity = subscriberRetrieve.getSubscriberData();
						String strResponse = (new CCWSCommandImpl()).getLogResponse(subscriberEntity, vnmMessage.getIsdn());
						
						vnmMessage.setSubscriberType(Constants.PREPAID_SUB_TYPE); // DuyMB fixbug add 20130108
						
						vnmMessage.setResponseTime(new Date());
						instance.logMonitor("RECEIVE:" + strResponse + ". ID=" + sessionId + ". costTime=" + costTime);
						vnmMessage.setResponse("RECEIVE:" + strResponse + ". ID=" + sessionId + ". costTime=" + costTime);
					}					
				}
				catch (Exception e)
				{
					//vnmMessage.setSubscriberType(SubscriberEntryImpl.getSubscriberType(vnmMessage.getIsdn()));
					//vnmMessage.setSubscriberType(Constants.POSTPAID_SUB_TYPE);
					vnmMessage.setSubscriberType(Constants.UNKNOW_SUB_TYPE);
				}
				finally
				{
					instance.closeProvisioningConnection(connection);
				}

				if (subscriberEntity == null)
				{
					if (vnmMessage.getSubscriberType() == Constants.PREPAID_SUB_TYPE)
					{
						throw new AppException(Constants.ERROR);
					}
				}
				else
				{
					vnmMessage.setSubscriberRetrieve(subscriberRetrieve);
					vnmMessage.setSubscriberType(Constants.PREPAID_SUB_TYPE);

					// Add balance info in response
					BalanceEntity[] balances = subscriberRetrieve.getSubscriberData().getBalances().getBalance();

					for (BalanceEntity balance : balances)
					{
						vnmMessage.setResponseValue(balance.getBalanceName() + ".amount",
								StringUtil.format(balance.getBalance(), "#"));
						vnmMessage.setResponseValue(balance.getBalanceName() + ".expireDate",
								StringUtil.format(balance.getAccountExpiration().getTime(), "dd/MM/yyyy HH:mm:ss"));
					}

					vnmMessage.setResponseValue(ResponseUtil.SERVICE_PRICE, StringUtil.format(product.getPrice(), "#"));
					// End edited

					validateState(instance, orderRoute, product, vnmMessage);
					
					if (!isInviter)
					{
						validateCOS(instance, orderRoute, product, vnmMessage);
					}
					else
					{
						//2013-07-25 MinhDT Change start for CR charge promotion
	//					validateBalance(instance, orderRoute, product, vnmMessage);
						boolean notEnough = true;
						String error = "";
						boolean chargeMulti = product.getParameter("ChargeMulti." + order.getActionType(), "false").equals("true");
						if (chargeMulti && product.getAvailBalances().length > 0)
						{
							for (int i=0; i< product.getAvailBalances().length; i++)
							{
								try
								{
									validateBalance(instance, orderRoute, product, vnmMessage, product.getAvailBalances()[i], chargeMulti);
									vnmMessage.setBalanceType(product.getAvailBalances()[i]);
									notEnough = false;
								}
								catch (Exception e)
								{
									error = e.getMessage();
									if (i == product.getAvailBalances().length - 1 && !error.equals(""))
									{
										throw new AppException(error);
									}
									else if (error.equals(Constants.ERROR_NOT_ENOUGH_MONEY))
									{
										notEnough = true;
									}
								}
								
								if (!notEnough)
								{
									break;
								}
							}
						}
						else
						{
							validateBalance(instance, orderRoute, product, vnmMessage, CCWSConnection.CORE_BALANCE, chargeMulti);
						}
					}
				}
				//2013-07-25 MinhDT Change end for CR charge promotion
			}
			catch (AppException e)
			{
				vnmMessage.setCause(e.getMessage());
				vnmMessage.setDescription(e.getContext());
				vnmMessage.setStatus(Constants.ORDER_STATUS_DENIED);
			}
			catch (Exception e)
			{
				throw e;
			}
			finally
			{
				if (vnmMessage != null)
				{
					vnmMessage.setSubscriberRetrieve(subscriberRetrieve);
				}

				instance.closeProvisioningConnection(connection);
			}
		}

		return (vnmMessage == null) ? order : vnmMessage;
	}
	
	public void verifyNumber(String number) throws Exception
	{
		Pattern pattern = Pattern.compile("\\d{11}");
		Matcher matcher = pattern.matcher(number);

		if (!matcher.matches())
		{
			pattern = Pattern.compile("\\d{12}");
			matcher = pattern.matcher(number);
			if (!matcher.matches())
			{
				throw new AppException(Constants.ERROR_INVALID_DELIVER);
			}
		}
		
		if (!number.startsWith(Constants.SHORT_CODE_VNM_8492)
				&& !number.startsWith(Constants.SHORT_CODE_VNM_84186)
				&& !number.startsWith(Constants.SHORT_CODE_VNM_84188))
		{
			throw new AppException(Constants.ERROR_INVALID_DELIVER);
		}
	}
}
