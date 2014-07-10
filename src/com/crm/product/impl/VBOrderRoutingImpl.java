package com.crm.product.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.crm.subscriber.impl.IDDServiceImpl;
import com.crm.subscriber.impl.SubscriberEntryImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.DateUtil;
import com.crm.util.GeneratorSeq;
import com.crm.util.StringUtil;
import com.fss.util.AppException;

public class VBOrderRoutingImpl extends VASOrderRoutingImpl
{
	private final static String ERROR_CONFIRM_NOT_EXISTS = "confirm-not-exists";
	private final static String ERROR_NOT_CONFIRM = "not-confirm";
	private final static String ERROR_REGISTER_IN_PAST = "registered-in-past";
	private final static String ERROR_MAX_REGISTER = "max-register";
	private final static String ERROR_FIRST_TIME_REGISTER = "first-time-register";
	private final static String ERROR_EXTEND_INVALID = "extend-invalid";
	
	public CommandMessage register(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		String responseCode = "";
		try
		{
			SubscriberProduct subProduct = IDDServiceImpl.isConfirmRegister(
					order.getIsdn(), order.getProductId());
			if (subProduct == null)
			{
				responseCode = ERROR_CONFIRM_NOT_EXISTS;
				order.setCause(responseCode);
				order.setStatus(Constants.ORDER_STATUS_DENIED);

				return order;
			}
			order.setSubProductId(subProduct.getSubProductId());
			
			IDDServiceImpl.removeConfirm(subProduct.getSubProductId());
			order = parser(instance, orderRoute, order);
		}
		catch (Exception e)
		{
			order.setStatus(Constants.ORDER_STATUS_DENIED);
			order.setCause(Constants.ERROR);
			order.setDescription(e.getMessage());
		}

		return order;
	}

	public CommandMessage cancelConfirm(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		try
		{
			SubscriberProduct subProduct = IDDServiceImpl.isConfirmRegister(
					order.getIsdn(), order.getProductId());
			if (subProduct == null)
			{
				order.setCause(ERROR_CONFIRM_NOT_EXISTS);
				order.setStatus(Constants.ORDER_STATUS_DENIED);
				
				return order;
			}
			order.setSubProductId(subProduct.getSubProductId());
			
			order = parser(instance, orderRoute, order);
		}
		catch (Exception e)
		{
			order.setStatus(Constants.ORDER_STATUS_DENIED);
			order.setCause(Constants.ERROR);
			order.setDescription(e.getMessage());
		}

		return order;
	}

	public CommandMessage confirmRegisterService(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		String responseCode = "";

		try
		{
			order = parser(instance, orderRoute, order);
			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
				ProductEntry product = ProductFactory.getCache().getProduct(order.getProductId());
				try
				{
					checkBlacklist(instance, product, order);
				}
				catch(AppException e)
				{
					if (e.getMessage().equals(Constants.ERROR_BLACKLIST_PRODUCT))
					{
						order.setCause(Constants.ERROR_BLACKLIST_PRODUCT);
						order.setStatus(Constants.ORDER_STATUS_DENIED);
		
						return order;
					}
					else
					{
						order.setCause(e.getMessage());
						order.setStatus(Constants.ORDER_STATUS_DENIED);
		
						return order;
					}
				}
				
//				SubscriberProduct subProduct = SubscriberProductImpl.getUnterminated(order.getIsdn(), order.getProductId());
//				if (subProduct != null && subProduct.isActive())
//				{
//					responseCode = Constants.ERROR_REGISTERED
//							+ (order.getSubscriberType() == Constants.PREPAID_SUB_TYPE ? ".prepaid"
//									: ".postpaid");
//					order.setCause(responseCode);
//					order.setStatus(Constants.ORDER_STATUS_DENIED);
//	
//					return order;
//				}
				
				if (IDDServiceImpl.isRegistedBefore(order.getIsdn(),
						order.getProductId()))
				{
					responseCode = ERROR_REGISTER_IN_PAST;
					order.setCause(responseCode);
					order.setStatus(Constants.ORDER_STATUS_DENIED);

					return order;
				}

				SubscriberProduct subProductConfirm = IDDServiceImpl.isConfirmRegister(
						order.getIsdn(), order.getProductId());
				if (subProductConfirm != null)
				{
					responseCode = ERROR_NOT_CONFIRM;
					order.setCause(responseCode);
					order.setStatus(Constants.ORDER_STATUS_DENIED);

					return order;
				}
			}
			else if (order.getCause().equals(Constants.ERROR_REGISTERED))
			{
				responseCode = Constants.ERROR_REGISTERED
						+ (order.getSubscriberType() == Constants.PREPAID_SUB_TYPE ? ".prepaid"
								: ".postpaid");
				order.setCause(responseCode);
				order.setStatus(Constants.ORDER_STATUS_DENIED);
			}
		}
		catch (Exception e)
		{
			order.setStatus(Constants.ORDER_STATUS_DENIED);
			order.setCause(Constants.ERROR);
			order.setDescription(e.getMessage());
		}

		return order;
	}

	public CommandMessage renewService(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		ProductEntry product = ProductFactory.getCache().getProduct(
				order.getProductId());

		String responseCode = "";
		try
		{
			if (!IDDServiceImpl.isRegistedBefore(order.getIsdn(),
					order.getProductId()))
			{
				order.setCause(ERROR_FIRST_TIME_REGISTER);
				order.setStatus(Constants.ORDER_STATUS_DENIED);

				return order;
			}
			
			order = parser(instance, orderRoute, order);
			
			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
//				SubscriberProduct subProduct = SubscriberProductImpl.getUnterminated(order.getIsdn(), order.getProductId());
//				if (subProduct != null && subProduct.isActive())
//				{
//					responseCode = Constants.ERROR_REGISTERED
//							+ (order.getSubscriberType() == Constants.PREPAID_SUB_TYPE ? ".prepaid"
//									: ".postpaid");
//					order.setCause(responseCode);
//					order.setStatus(Constants.ORDER_STATUS_DENIED);
//	
//					return order;
//				}
	
				if (order.isPostpaid())
				{
					int maxRegister = Integer.parseInt(product.getParameter(
							"MaxRegister", "1"));
	
					if (IDDServiceImpl.checkMaxRegister(order.getProductId(),
							order.getIsdn(), maxRegister))
					{
						order.setResponseValue(ResponseUtil.SERVICE_AMOUNT,
								Integer.valueOf(maxRegister));
						order.setCause(ERROR_MAX_REGISTER);
						order.setStatus(Constants.ORDER_STATUS_DENIED);
	
						return order;
					}
				}
				
				order.getParameters().setProperty("PropertiesRenew", "true");
			}
			else if (order.getCause().equals(Constants.ERROR_REGISTERED))
			{
				responseCode = Constants.ERROR_REGISTERED
						+ (order.getSubscriberType() == Constants.PREPAID_SUB_TYPE ? ".prepaid"
								: ".postpaid");
				order.setCause(responseCode);
				order.setStatus(Constants.ORDER_STATUS_DENIED);
			}
		}
		catch (Exception e)
		{
			order.setCause(Constants.ERROR);
			order.setDescription(e.getMessage());
			order.setStatus(Constants.ORDER_STATUS_DENIED);
		}

		return order;
	}
	
	public CommandMessage registerByPassConfirm(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		ProductEntry product = ProductFactory.getCache().getProduct(
				order.getProductId());

		String responseCode = "";
		try
		{
			order = parser(instance, orderRoute, order);
			
			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
				if (order.isPostpaid())
				{
					int maxRegister = Integer.parseInt(product.getParameter(
							"MaxRegister", "1"));
	
					if (IDDServiceImpl.checkMaxRegister(order.getProductId(),
							order.getIsdn(), maxRegister))
					{
						order.setResponseValue(ResponseUtil.SERVICE_AMOUNT,
								Integer.valueOf(maxRegister));
						order.setCause(ERROR_MAX_REGISTER);
						order.setStatus(Constants.ORDER_STATUS_DENIED);
	
						return order;
					}
				}
				
				if (IDDServiceImpl.isRegistedBefore(order.getIsdn(),
						order.getProductId()))
				{
					order.getParameters().setProperty("PropertiesRenew", "true");
				}
			}
			else if (order.getCause().equals(Constants.ERROR_REGISTERED))
			{
				responseCode = Constants.ERROR_REGISTERED
						+ (order.getSubscriberType() == Constants.PREPAID_SUB_TYPE ? ".prepaid"
								: ".postpaid");
				order.setCause(responseCode);
				order.setStatus(Constants.ORDER_STATUS_DENIED);
			}
		}
		catch (Exception e)
		{
			order.setCause(Constants.ERROR);
			order.setDescription(e.getMessage());
			order.setStatus(Constants.ORDER_STATUS_DENIED);
		}

		return order;
	}

	public CommandMessage confirmExtend(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		SubscriberProduct subProduct = SubscriberProductImpl.getUnterminated(order.getIsdn(), order.getProductId());
		if (subProduct == null)
		{
			order.setCause(Constants.ERROR_UNREGISTERED);
			order.setStatus(Constants.ORDER_STATUS_DENIED);

			return order;
		}

		if (subProduct.getStatus() != Constants.SUBSCRIBER_ALERT_BALANCE_STATUS)
		{
			order.setCause(ERROR_EXTEND_INVALID);
			order.setStatus(Constants.ORDER_STATUS_DENIED);

			return order;
		}

		try
		{
			orderRoute.setCheckBalance(true);
			order = parser(instance, orderRoute, order);
			if (!order.getCause().equals(Constants.ERROR_NOT_ENOUGH_MONEY) && order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
				order.setActionType(Constants.ACTION_SUPPLIER_REACTIVE);
			}
			else if (order.getCause().equals(Constants.ERROR_NOT_ENOUGH_MONEY))
			{
				order.setActionType(Constants.ACTION_SUPPLIER_DEACTIVE);
				order.setCause("");
				order.setStatus(Constants.ORDER_STATUS_PENDING);
			}
		}
		catch (Exception e)
		{
			order.setCause(Constants.ERROR);
			order.setDescription(e.getMessage());
			order.setStatus(Constants.ORDER_STATUS_DENIED);
		}

		return order;
	}

	public CommandMessage cancelExtendIDD(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		order = parser(instance, orderRoute, order);
		
		if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
		{
			SubscriberProduct subProduct = SubscriberProductImpl.getUnterminated(order.getIsdn(), order.getProductId());
			if (subProduct == null)
			{
				order.setCause(Constants.ERROR_UNREGISTERED);
				order.setStatus(Constants.ORDER_STATUS_DENIED);
	
				return order;
			}
	
			if (subProduct.getStatus() != Constants.SUBSCRIBER_ALERT_BALANCE_STATUS
					&& subProduct.getStatus() != Constants.SUBSCRIBER_ALERT_EXPIRE_STATUS)
			{
				order.setCause(ERROR_EXTEND_INVALID);
				order.setStatus(Constants.ORDER_STATUS_DENIED);
	
				return order;
			}
	
			try
			{
				if (subProduct.getStatus() == Constants.SUBSCRIBER_ALERT_EXPIRE_STATUS)
				{
					IDDServiceImpl.updateIDDStatus(Constants.SUBSCRIBER_NOT_EXTEND_STATUS, subProduct.getSubProductId());
				}
				else if (subProduct.getStatus() == Constants.SUBSCRIBER_ALERT_BALANCE_STATUS)
				{
					order.setCause("");
					order.setActionType(Constants.ACTION_CANCEL);
				}
			}
			catch (Exception e)
			{
				order.setCause(Constants.ERROR);
				order.setDescription(e.getMessage());
				order.setStatus(Constants.ORDER_STATUS_DENIED);
			}
		}

		return order;
	}

	public CommandMessage getIDDInstruction(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		order = parser(instance, orderRoute, order);
		if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
		{
			order.setCause("get-intruction.success");
		}

		return order;
	}

	public CommandMessage getIDDDestination(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		order = parser(instance, orderRoute, order);
		if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
		{
			order.setCause("get-destination.success");
		}

		return order;
	}

	public CommandMessage searchVB220(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		SubscriberProduct subProduct = SubscriberProductImpl.getActive(
				order.getIsdn(), order.getProductId());

		String responseCode = "";
		try
		{
			if (subProduct != null && subProduct.isActive())
			{
				order = parser(instance, orderRoute, order);
				if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
				{
					if (order.isPostpaid())
					{
						if (order.getKeyword().toUpperCase().equals("TH VB220"))
						{
							responseCode = "get-expired-success";
							order.setCause(responseCode);
							order.setResponseValue(
									ResponseUtil.SERVICE_EXPIRE_DATE,
									new SimpleDateFormat("dd/MM/yyyy")
											.format(subProduct.getExpirationDate()));
							order.setStatus(Constants.ORDER_STATUS_DENIED);
	
							return order;
						}
					}
					else
					{
						responseCode = "prepaid-query";
						order.setCause(responseCode);
						order.setStatus(Constants.ORDER_STATUS_DENIED);
	
						return order;
					}
				}
			}
			else
			{
				responseCode = Constants.ERROR_SUBSCRIPTION_NOT_FOUND;
				order.setCause(responseCode);
				order.setStatus(Constants.ORDER_STATUS_DENIED);

				return order;
			}
		}
		catch (Exception e)
		{
			order.setCause(Constants.ERROR);
			order.setStatus(Constants.ORDER_STATUS_DENIED);
		}

		return order;
	}
	
//	public CommandMessage renewPrepaid(OrderRoutingInstance instance,
//			ProductRoute orderRoute, CommandMessage order) throws Exception
//	{
//		ProductEntry product = ProductFactory.getCache().getProduct(
//				order.getProductId());
//
//		SubscriberProduct subProduct = SubscriberProductImpl.getUnterminated(
//				order.getIsdn(), order.getProductId());
//
//		if (subProduct == null)
//		{
//			order.setCause(Constants.ERROR_UNREGISTERED);
//			order.setStatus(Constants.ORDER_STATUS_DENIED);
//
//			return order;
//		}
//		
//		CCWSConnection connection = null;
//
//		String responseCode = "";
//		try
//		{
//			order = parser(instance, orderRoute, order);
//			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
//			{
//				Date expirationDate = subProduct.getExpirationDate();
//				Date currentDate = new Date();
//				double dayBetween = (double) ((expirationDate.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24));
//	
//				connection = (CCWSConnection) instance.getProvisioningConnection();
//	
//				int sessionId = 0;
//				try
//				{
//					sessionId = GeneratorSeq.getNextSeq();
//				}
//				catch (Exception e)
//				{
//				}
//				
//				order.getParameters().setProperty("IsQueryRTBS", "true");
//				String strRequest = (new CCWSCommandImpl())
//						.getLogRequest("com.comverse_in.prepaid.ccws.ServiceSoapStub.retrieveSubscriberWithIdentityNoHistory", order.getIsdn());
//				instance.logMonitor("SEND: " + strRequest + ". ID=" + sessionId);
//				order.setRequest("SEND: " + strRequest + ". ID=" + sessionId);
//				
//				Date startTime = new Date();
//				order.setRequestTime(new Date());
//				
//				SubscriberRetrieve subscriberRetrieve = connection.getSubscriber(order.getIsdn(), 1);
//				Date endTime = new Date();
//				
//				long costTime = CommandUtil.calculateCostTime(startTime, endTime);
//				
//				SubscriberEntity subEntity = null;
//				VNMMessage vnmMessage = CommandUtil.createVNMMessage(order);
//				
//				if (subscriberRetrieve != null)
//				{
//					subEntity = subscriberRetrieve.getSubscriberData();
//					String strResponse = (new CCWSCommandImpl()).getLogResponse(subEntity, order.getIsdn());
//					
//					order.setSubscriberType(Constants.PREPAID_SUB_TYPE);
//					
//					order.setResponseTime(new Date());
//					instance.logMonitor("RECEIVE:" + strResponse + ". ID=" + sessionId + ". costTime=" + costTime);
//					order.setResponse("RECEIVE:" + strResponse + ". ID=" + sessionId + ". costTime=" + costTime);
//					
//					
//					vnmMessage.setSubscriberRetrieve(subscriberRetrieve);
//				}
//				
//				long balance = (long) CCWSConnection.getBalance(subEntity, "VB600_M").getAvailableBalance();
//	
//				long minBalance = Long.parseLong(product.getParameter("MinBalance", "1000"));
//	
//				boolean notEnough = false;
//				if (balance <= minBalance)
//				{
//					notEnough = true;
//				}
//				
//				double maxTopUp = Double.parseDouble(product.getParameter(
//						"MaxTopUp", "3"));
//	
//				int sendNotify = IDDServiceImpl.checkSendNotify(
//						order.getIsdn(), dayBetween,
//						order.getProductId(), notEnough, maxTopUp);
//	
//				if (sendNotify == 0)
//				{
//					responseCode = "";
//					int dayNotify = Integer.parseInt(product.getParameter(
//							"DayNotify", "5"));
//					if (notEnough)
//					{
//							IDDServiceImpl.notifyOverdue(order.getIsdn(),
//									-1, 0, order.getProductId());
//	
//							responseCode = Constants.ERROR_NOTIFY_OVER_BALANCE;
//							order.setCause(responseCode);
//							order.setStatus(Constants.ORDER_STATUS_DENIED);
//					}
//					else if (dayBetween <= dayNotify)
//					{
//						IDDServiceImpl.notifyOverdue(order.getIsdn(), 0,
//								0, order.getProductId());
//	
//						responseCode = Constants.ERROR_NOTIFY_OVER_EXPIRED;
//						order.setCause(responseCode);
//						order.setStatus(Constants.ORDER_STATUS_DENIED);
//					}
//				}
//				else if (sendNotify == -1)
//				{
//					order.setActionType(Constants.ACTION_UNREGISTER);
//				}
//				else if ((sendNotify == 1 && dayBetween <= 0)
//						|| (sendNotify == 1 && notEnough))
//				{
//					boolean isNotEnoughMoney = false;
//					
//					try
//					{
//						validateBalance(instance, orderRoute, product, vnmMessage);
//					}
//					catch (AppException e) {
//						if (e.getMessage().equals(
//								Constants.ERROR_NOT_ENOUGH_MONEY))
//						{
//							isNotEnoughMoney = true;
//						}
//						else
//						{
//							throw e;
//						}
//					}
//					
//					if (isNotEnoughMoney)
//					{
//						if (subProduct.getStatus() != Constants.SUBSCRIBER_ALERT_BALANCE_STATUS)
//						{
//							IDDServiceImpl.updateIDDStatus(Constants.SUBSCRIBER_EXTEND_NOT_ENOUGHT_STATUS,
//									subProduct.getGraceDate(), subProduct.getSubProductId());
//	
//							order.setCause(Constants.ERROR_NOT_ENOUGH_MONEY);
//							order.setStatus(Constants.ORDER_STATUS_DENIED);
//						}
//						else
//						{
//							order.setCause(Constants.ACTION_SUPPLIER_REACTIVE
//											+ "." + Constants.ERROR_NOT_ENOUGH_MONEY);
//							order.setStatus(Constants.ORDER_STATUS_DENIED);
//						}
//					}
//					else
//					{
//						order.setActionType(Constants.ACTION_SUPPLIER_REACTIVE);
//					}
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			order.setCause(Constants.ERROR);
//			order.setDescription(e.getMessage());
//			order.setStatus(Constants.ORDER_STATUS_DENIED);
//		}
//		finally
//		{
//			instance.closeProvisioningConnection(connection);
//		}
//		
//		return order;
//	}
	
	public CommandMessage parser(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		Exception error = null;

		ProductEntry product = null;

		SubscriberProduct subscriberProduct = null;

		try
		{
			// check SMS syntax
			Date startTime = new Date();
			Date endTime = new Date();

			/**
			 * Should check for both SMS & web.
			 */
			smsParser(instance, orderRoute, order);

			// check duplicate request
			if (orderRoute.getDuplicateScan() > 0)
			{
				startTime = new Date();
				checkDuplicate(instance, orderRoute, order);

				endTime = new Date();
				instance.debugMonitor("Check duplicate(" + order.getIsdn() + ") cost time: "
						+ (endTime.getTime() - startTime.getTime()) + "ms");
			}

			if (orderRoute.getMaxRegisterDaily() > 0)
			{
				startTime = new Date();
				checkMaxRegister(instance, orderRoute, order);

				endTime = new Date();
				instance.debugMonitor("Check maxregisterdaily(" + order.getIsdn() + ")  cost time: "
						+ (endTime.getTime() - startTime.getTime()) + "ms");
			}
			
			// check product in available list
			product = ProductFactory.getCache().getProduct(order.getProductId());
						
			// check promotion
			if (orderRoute.isCheckPromotion())
			{
				checkPromotion(instance, orderRoute, order, product.getAlias());
			}
			
			// get current subscriber product
			if (order.getSubProductId() == Constants.DEFAULT_ID)
			{
				/**
				 * Edited: replaced getActive by getUnterminated (for barring
				 * subscription case)
				 */
				subscriberProduct = SubscriberProductImpl.getUnterminated(
						order.getIsdn(), order.getProductId());
			}
			else
			{
				subscriberProduct = SubscriberProductImpl.getProduct(order
						.getSubProductId());
			}
			
			// Set subscriber type
			if (order.getSubscriberType() == Constants.UNKNOW_SUB_TYPE)
			{
				order.setSubscriberType(SubscriberEntryImpl
						.getSubscriberType(order.getIsdn()));
			}
			
			// check action type
			checkActionType(instance, orderRoute, product, order,
					subscriberProduct);
			
			if (order.getActionType().equals(Constants.ACTION_TOPUP))
			{
				if (order.getRequestValue("first-action-type", "").equals(Constants.ACTION_SUBSCRIPTION))
				{
					Date currentDate = new Date();
					
					if (subscriberProduct.getGraceDate() != null
									&& subscriberProduct.getGraceDate().before(currentDate)
									&& subscriberProduct.isBarring())
					{
						order.setActionType(Constants.ACTION_CANCEL);
					}
					else
					{
						order.setActionType(Constants.ACTION_SUBSCRIPTION);
					}
				}
			}
			
			if (orderRoute.isCheckBalance() && !order.getActionType().equals(Constants.ACTION_UNREGISTER) 
					&& order.getSubscriberType() == Constants.PREPAID_SUB_TYPE)
			{
				order = checkBalance(instance, orderRoute, order);
				order.getParameters().setProperty("IsQueryRTBS", "true");
			}
			else
			{
				order.setAmount(order.getQuantity() * order.getPrice());
			}
			
			if (order.getSubscriberType() == Constants.POSTPAID_SUB_TYPE
					&& order.getCampaignId() != Constants.DEFAULT_ID)
			{
				order.setCampaignId(Constants.DEFAULT_ID);
			}

			Date currentDate = new Date();
			
			if ((order.getStatus() == Constants.ORDER_STATUS_DENIED)
					&& order.getCause()
							.equals(Constants.ERROR_NOT_ENOUGH_MONEY))
			{
				if (order.getActionType().equals(Constants.ACTION_SUBSCRIPTION)
						|| order.getActionType().equals(Constants.ACTION_SUPPLIER_REACTIVE))
				{
					if (subscriberProduct.getSupplierStatus() != Constants.SUPPLIER_BARRING_STATUS
							&& !order.getChannel().equals(Constants.CHANNEL_SMS))
					{
						ProductMessage productMessage = product.getProductMessage(order.getActionType(), 
								order.getCampaignId(), order.getLanguageId(), order.getChannel(), order.getCause());
						if (productMessage != null)
						{
							String content = productMessage.getContent();
							if (subscriberProduct.getExpirationDate() != null)
							{
								content = content.replaceAll("~SERVICE_EXPIRE_DATE~",
										StringUtil.format(subscriberProduct.getExpirationDate(),
												"dd/MM/yyyy"));
								SubscriberEntity subscriberEntity = ((VNMMessage) order).getSubscriberEntity();
								BalanceEntity balance = CCWSConnection.getBalance(subscriberEntity, "VB600_M");
								
								double convertRatio = Double.parseDouble(product.getParameter("ConvertRatio", "1"));
								content = content.replaceAll("~SERVICE_BALANCE~",  StringUtil.format(
										balance.getAvailableBalance() * convertRatio, "#,##0"));
							}
							SubscriberProductImpl.insertSendSMS(product.getParameter("ProductShotCode", ""), order.getIsdn(), content);
						}
					}
					
					if (subscriberProduct.getSupplierStatus() == Constants.SUPPLIER_ACTIVE_STATUS)
					{
						order.setActionType(Constants.ACTION_SUPPLIER_DEACTIVE);

						order.setCause("");

						order.setStatus(Constants.ORDER_STATUS_PENDING);
					}
					else if (subscriberProduct.getGraceDate() != null
									&& subscriberProduct.getGraceDate().before(currentDate))
					{
						order.setActionType(Constants.ACTION_CANCEL);
						order.setCause("");
						order.setStatus(Constants.ORDER_STATUS_PENDING);
					}
				}
				else if (order.getActionType().equals(Constants.ACTION_CANCEL))
				{
					order.setCause("");
					order.setStatus(Constants.ORDER_STATUS_PENDING);
				}
				else
				{
					if (orderRoute.isCheckPromotion()
							&& order.getActionType().equals(Constants.ACTION_REGISTER))
					{
						long lastCampaignId = order.getCampaignId();
						checkPromotion(instance, orderRoute, order, product.getAlias() + "." + Constants.ERROR_NOT_ENOUGH_MONEY);
						if (order.getCampaignId() != lastCampaignId)
						{
							order.getParameters().setBoolean("FreeOneDay", true);
							order.setCause("");
							order.setStatus(Constants.ORDER_STATUS_PENDING);
						}
					}
				}
			}
			else
			{
				if (order.getActionType().equals(Constants.ACTION_CANCEL))
				{
					order.setActionType(Constants.ACTION_SUPPLIER_REACTIVE);
					order.setCause("");
					order.setStatus(Constants.ORDER_STATUS_PENDING);
				}
				
				checkSubscriberType(instance, product, order);
			}
			
			if (order.getActionType().equals(Constants.ACTION_SUBSCRIPTION)
					|| order.getActionType().equals(Constants.ACTION_SUPPLIER_DEACTIVE)
//					|| order.getActionType().equals(Constants.ACTION_TOPUP)
					|| order.getActionType().equals(Constants.ACTION_UNREGISTER))
			{
				if (subscriberProduct == null)
					throw new AppException(Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
				if ((subscriberProduct.getGraceDate() == null
						&& order.getActionType().equals(Constants.ACTION_SUPPLIER_DEACTIVE))
						|| (subscriberProduct.getGraceDate() != null
								&& subscriberProduct.getGraceDate().before(currentDate)
								&& (order.getActionType().equals(Constants.ACTION_SUPPLIER_DEACTIVE)
										|| subscriberProduct.isBarring())
										))
				{
					order.setActionType(Constants.ACTION_CANCEL);

					order.setCause("");

					order.setStatus(Constants.ORDER_STATUS_PENDING);
				}
				else if (subscriberProduct.isBarring() && !orderRoute.isTopupEnable()
						&& order.getActionType().equals(Constants.ACTION_SUBSCRIPTION))
				{
					order.setCause(Constants.ERROR_REGISTERED);

					order.setStatus(Constants.ORDER_STATUS_DENIED);
				}
			}

			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
				validate(instance, orderRoute, order);
			}
			
			order.getParameters().setProperty("IsVBService", "true");
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
		
		String currentState = "";
		try
		{
			currentState = getSubscriberState(instance, orderRoute, product, order);
		}
		catch (Exception e)
		{

		}
		if (!currentState.equals(Constants.BALANCE_STATE_ACTIVE)
				&& order.getParameters().getProperty("IsQueryRTBS","false").equals("true")
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

		if ((error != null) && !(error instanceof AppException))
		{
			throw error;
		}

		order.getParameters().setBoolean("includeCurrentDay", true);
		
		return order;
	}
	
	public void checkActionType(OrderRoutingInstance instance,
			ProductRoute orderRoute, ProductEntry product,
			CommandMessage order, SubscriberProduct subscriberProduct)
			throws Exception
	{
		Date now = new Date();

		order.setRequestValue("first-action-type", order.getActionType());
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
				if (subscriberProduct.getExpirationDate() != null)
				{
					order.setResponseValue("service.activeDate",
							subscriberProduct.getExpirationDate());
				}
			}

			if (subscriberProduct != null)
			{
				order.setSubProductId(subscriberProduct.getSubProductId());
			}

			String actionType = order.getActionType();

			if ((actionType.equals(Constants.ACTION_REGISTER)
					|| actionType.equals(Constants.ACTION_CONFIRM))
					&& (subscriberProduct != null 
						&& (subscriberProduct.isActive() || subscriberProduct.isBarring())))
			{
				throw new AppException(Constants.ERROR_REGISTERED);
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
								Constants.ERROR_UNREGISTERED);
					}
				}
				else if (subscriberProduct != null)
				{
					if (orderRoute.isTopupEnable()
							&& subscriberProduct.isBarring())
					{
						actionType = Constants.ACTION_TOPUP;
					}
					else if (actionType
								.equals(Constants.ACTION_SUBSCRIPTION))
					{
						if (subscriberProduct.getExpirationDate().after(now))
						{
							throw new AppException(
										Constants.ERROR_REGISTERED);
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
}
