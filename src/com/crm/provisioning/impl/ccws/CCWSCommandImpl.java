/**
 * 
 */
package com.crm.provisioning.impl.ccws;

import java.net.SocketException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.axis.AxisFault;

import com.comverse_in.prepaid.ccws.ArrayOfDeltaBalance;
import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.BalanceEntityBase;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.comverse_in.prepaid.ccws.SubscriberPB;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;

import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.message.CommandMessage;
import com.crm.kernel.message.Constants;
import com.crm.marketing.cache.CampaignEntry;
import com.crm.marketing.cache.CampaignFactory;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductPrice;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.IDDServiceImpl;
import com.crm.subscriber.impl.SubscriberBalanceImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.AppProperties;
import com.crm.util.GeneratorSeq;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;

import com.fss.util.AppException;

/**
 * @author ThangPV
 */
public class CCWSCommandImpl extends CommandImpl
{
	public String getErrorCode(CommandInstance instance,
			CommandMessage request, Exception error)
	{
		String errorCode = "";
		try
		{
			errorCode = error.getMessage();

			if (errorCode.startsWith("<ErrorCode>"))
			{
				errorCode = errorCode.substring("<ErrorCode>".length());
				errorCode = errorCode.substring(0,
						errorCode.indexOf("</ErrorCode>"));
			}
			else
			{
				errorCode = "";
			}
		}
		catch (Exception e)
		{
			errorCode = "CCWS unknown" + (error != null ? (": " + error.toString()) : "");
		}
		
		return errorCode;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public SubscriberEntity getSubscriber(CommandInstance instance,
			CommandMessage request) throws Exception
	{
		CCWSConnection connection = null;

		SubscriberEntity subscriber = null;

		try
		{
			if (request instanceof VNMMessage)
			{
				subscriber = ((VNMMessage) request).getSubscriberEntity();
			}

			if (subscriber == null)
			{
				String isdn = CommandUtil.addCountryCode(request.getIsdn());

				connection = (CCWSConnection) instance
						.getProvisioningConnection();
				long sessionId = setRequest(
						instance,
						request,
						getLogRequest(
								"com.comverse_in.prepaid.ccws.ServiceSoapStub.retrieveSubscriberWithIdentityNoHistory",
								request.getIsdn()));

				subscriber = connection.getSubscriberInfor(isdn);

				setResponse(instance, request,
						getLogResponse(subscriber, request.getIsdn()),
						sessionId);

				if (request instanceof VNMMessage)
				{
					((VNMMessage) request).setSubscriberEntity(subscriber);
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}

		return subscriber;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void validateStatus(CommandInstance instance,
			CommandMessage request, SubscriberEntity subscriber)
			throws Exception
	{
		try
		{
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());

			if ((subscriber == null) || (product == null))
			{
				return;
			}

			if (product.getAvailStatus().length > 0)
			{
				boolean found = false;

				for (int j = 0; !found && (j < product.getAvailStatus().length); j++)
				{
					String status = product.getAvailStatus()[j];

					found = status.equals(subscriber.getCurrentState());
				}

				if (!found)
				{
					throw new AppException(Constants.ERROR_DENIED_STATUS);
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void validateCOS(CommandInstance instance, CommandMessage request,
			SubscriberEntity subscriber) throws Exception
	{
		try
		{
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());

			if ((subscriber == null) || (product == null))
			{
				return;
			}
			if (product.getAvailCOS().length > 0)
			{
				boolean found = false;

				for (int j = 0; !found && (j < product.getAvailCOS().length); j++)
				{
					String cos = product.getAvailCOS()[j];

					found = cos.equals(subscriber.getCOSName());
				}

				if (!found)
				{
					throw new AppException(Constants.ERROR_DENIED_COS);
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void validateBalance(CommandInstance instance,
			ProductRoute orderRoute, ProductEntry product, VNMMessage vnmMessage)
			throws Exception
	{
		SubscriberEntity subscriberEntity = null;

		try
		{
			subscriberEntity = vnmMessage.getSubscriberEntity();

			BalanceEntity balance = CCWSConnection.getBalance(subscriberEntity,
					CCWSConnection.CORE_BALANCE);

			if (balance.getAvailableBalance() < product.getMinBalance())
			{
				throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
			}

			// set default price
			vnmMessage.setOfferPrice(product.getPrice());

			ProductPrice productPrice = product.getProductPrice(
					vnmMessage.getChannel(), vnmMessage.getActionType(),
					vnmMessage.getSegmentId(),
					vnmMessage.getAssociateProductId(),
					vnmMessage.getQuantity(), vnmMessage.getOrderDate());

			int quantity = 1;

			if (productPrice != null)
			{
				if (balance.getAvailableBalance() >= productPrice
						.getFullOfCharge())
				{
					vnmMessage.setPrice(productPrice.getFullOfCharge());
					vnmMessage.setFullOfCharge(true);
				}
				else if (balance.getAvailableBalance() < productPrice
						.getBaseOfCharge())
				{
					throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
				}
				else if (orderRoute.isBaseChargeEnable())
				{
					vnmMessage.setFullOfCharge(false);
					vnmMessage.setPrice(productPrice.getBaseOfCharge());

					quantity = (int) (balance.getAvailableBalance() / vnmMessage
							.getPrice());

					if (quantity == 0)
					{
						throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
					}
				}
				else
				{
					throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
				}
			}
			else
			{
				vnmMessage.setPrice(product.getPrice());
			}

			vnmMessage.setQuantity(quantity);
			vnmMessage.setAmount(vnmMessage.getPrice()
					* vnmMessage.getQuantity());

			if (balance.getAvailableBalance() < vnmMessage.getAmount())
			{
				throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// DuyMB add end check cosname khi huy 3G 2011/12/07
	public VNMMessage checkBalance(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		VNMMessage vnmMessage = CommandUtil.createVNMMessage(request);

		SubscriberEntity subscriber = null;

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, vnmMessage);
		}
		else
		{
			try
			{
				long productId = vnmMessage.getProductId();

				ProductEntry product = ProductFactory.getCache().getProduct(
						productId);

				ProductRoute orderRoute = ProductFactory.getCache()
						.getProductRoute(vnmMessage.getRouteId());

				subscriber = getSubscriber(instance, vnmMessage);

				boolean isNotEnoughMoney = false;

				if (subscriber != null)
				{
					try
					{
						validateStatus(instance, vnmMessage, subscriber);

						validateCOS(instance, vnmMessage, subscriber);

						validateBalance(instance, orderRoute, product,
								vnmMessage);
					}
					catch (AppException e)
					{
						if (e.getMessage().equals(
								Constants.ERROR_NOT_ENOUGH_MONEY))
						{
							isNotEnoughMoney = true;
						}
						else
						{
							throw e;
						}
					}
					catch (Exception e)
					{
						throw e;
					}
				}


				SubscriberProduct subscriberProduct = SubscriberProductImpl
						.getActive(vnmMessage.getIsdn(), productId);

				Date currentDate = new Date();
				if (isNotEnoughMoney)
				{
					String actionType = vnmMessage.getActionType();
					String cause = Constants.ERROR_NOT_ENOUGH_MONEY;
					
					if (actionType.equals(Constants.ACTION_TOPUP))
					{
						if (vnmMessage.getRequestValue("first-action-type", "").equals(Constants.ACTION_SUBSCRIPTION))
						{
							actionType = Constants.ACTION_SUBSCRIPTION;
						}
					}

					if (actionType.equals(Constants.ACTION_SUBSCRIPTION))
					{
						if (subscriberProduct == null)
						{
							throw new AppException(
									Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
						}
						if (subscriberProduct.getExpirationDate().before(currentDate))
						{
							if (subscriberProduct.getSupplierStatus() == Constants.SUPPLIER_ACTIVE_STATUS)
							{
								actionType = Constants.ACTION_SUPPLIER_DEACTIVE;

								cause = "";
							}
						}
					}
					else
					{
						vnmMessage.setStatus(Constants.ORDER_STATUS_DENIED);
					}

					vnmMessage.setActionType(actionType);

					if (!cause.equals(""))
					{
						vnmMessage.setCause(cause);
					}
				}
				
				if (vnmMessage.getActionType().equals(Constants.ACTION_SUBSCRIPTION)
						|| vnmMessage.getActionType().equals(Constants.ACTION_SUPPLIER_DEACTIVE))
				{
					if (subscriberProduct == null)
						throw new AppException(Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
					if ((subscriberProduct.getGraceDate() == null && vnmMessage.getActionType().equals(
							Constants.ACTION_SUPPLIER_DEACTIVE))
							|| (subscriberProduct.getGraceDate() != null
									&& subscriberProduct.getGraceDate().before(currentDate)))
					{
						vnmMessage.setActionType(Constants.ACTION_UNREGISTER);

						vnmMessage.setCause("");

						vnmMessage.setStatus(Constants.ORDER_STATUS_PENDING);
					}
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, vnmMessage, e);
			}
		}
		return vnmMessage;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public VNMMessage modifyBalance(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);
		
		String isdn = CommandUtil.addCountryCode(result.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				SubscriberEntity subscriber = getSubscriber(instance, result);

				String[] addBalances = StringUtil.toStringArray(
						product.getParameter("balances", ""), StringPool.COMMA);

				Date now = new Date();

				balances = new BalanceEntityBase[addBalances.length];

				Calendar activeDate = Calendar.getInstance();

				result.setRequestValue("balances.premodified.activeDate",
						subscriber.getDateEnterActive().getTime());
				for (int j = 0; j < addBalances.length; j++)
				{
					String balanceName = addBalances[j];

					String prefix = "balance." + result.getActionType() + "."
							+ balanceName;

					// get current balance
					BalanceEntity balance = CCWSConnection.getBalance(
							subscriber, balanceName);

					// save original balance status before add
					result.setRequestValue("balances." + balanceName
							+ ".premodified.expirationDate", balance
							.getAccountExpiration().getTime());
					result.setRequestValue("balances." + balanceName
							+ ".premodified.amount", balance.getBalance());

					// calculate expiration date for this balance
					Calendar maxExpiredDate = Calendar.getInstance();

					maxExpiredDate.setTime(now);
					
					maxExpiredDate.set(Calendar.HOUR_OF_DAY, 23);
					maxExpiredDate.set(Calendar.MINUTE, 59);
					maxExpiredDate.set(Calendar.SECOND, 59);

					int maxDays = product.getParameters().getInteger(
							prefix + ".maxDays");

					maxExpiredDate.add(Calendar.DATE, maxDays);

					
					Calendar expiredDate = balance.getAccountExpiration();
					
					if (product.isSubscription())
					{
						SubscriberProduct subProduct = SubscriberProductImpl.getUnterminated(
								request.getIsdn(), request.getProductId());
						if (subProduct != null)
						{
							expiredDate.setTime(subProduct.getExpirationDate());
						}
					}
					
//					int expireTime = product.getParameters().getInteger(
//							prefix + ".days");
					int expireTime = product.getSubscriptionPeriod();
					if (result.getCampaignId() != Constants.DEFAULT_ID)
					{
						CampaignEntry campaign = CampaignFactory.getCache()
								.getCampaign(result.getCampaignId());

						if ((campaign != null))
						{
							expireTime = campaign.getSchedulePeriod();
						}
					}
					
					String setExpFlg = product.getParameters().getString(
							prefix + ".resetexpiredate", "false");

					boolean includeCurrentDay = result.getParameters().getBoolean(
							"includeCurrentDay");
					
					if (expiredDate.getTimeInMillis() < now.getTime()
							|| setExpFlg.equals("true"))
					{
						expireTime--;
						expiredDate.setTime(now);
					}
					else if (expiredDate.getTimeInMillis() >= now.getTime()
							&& result.getActionType().equals(Constants.ACTION_SUBSCRIPTION))
					{
						expireTime--;
					}

					boolean truncExpire = Boolean.parseBoolean(product.getParameter("TruncExpireDate", "true"));
					if (truncExpire)
					{
						int expireHour = product.getParameters().getInteger(
								prefix + ".expire.hour", 23);
						int expireMinute = product.getParameters().getInteger(
								prefix + ".expire.minute", 59);
						int expireSecond = product.getParameters().getInteger(
								prefix + ".expire.second", 59);
	
						expiredDate.set(Calendar.HOUR_OF_DAY, expireHour);
						expiredDate.set(Calendar.MINUTE, expireMinute);
						expiredDate.set(Calendar.SECOND, expireSecond);
					}

					if (expireTime > 0)
					{
						expiredDate.add(Calendar.DATE, expireTime);
					}

					if ((maxDays > 0) && expiredDate.after(maxExpiredDate))
					{
						expiredDate = maxExpiredDate;
					}

					if ((maxDays > 0) && expiredDate.after(activeDate))
					{
						activeDate = expiredDate;
					}

					double amount = product.getParameters().getDouble(
							prefix + ".amount");

					balances[j] = new BalanceEntityBase();

					balances[j].setBalanceName(balanceName);

					// Need to check accumulate or reset account DuyMB.
					String accumulateFlg = product.getParameters().getString(
							prefix + ".accumulate", "false");
					if (accumulateFlg.equals("true"))
					{
						balances[j].setBalance(balance.getBalance() + amount);
					}
					else
					{
						balances[j].setBalance(amount);
					}
					
					balances[j].setAccountExpiration(expiredDate);

					// Add response value for writing log file
					result.setResponseValue(balances[j].getBalanceName()
							+ ".amount",
							StringUtil.format(balances[j].getBalance(), "#"));

					result.setResponseValue(balances[j].getBalanceName()
							+ ".expireDate", StringUtil.format(balances[j]
							.getAccountExpiration().getTime(),
							Constants.DATE_FORMAT));
				}

				// String comment = product.getIndexKey();

				String comment = product.getParameter(
						"mtrComment." + product.getAlias() + "."
								+ request.getActionType(), "Comment MTR"
								+ product.getAlias());

				// modify balances
				if (balances.length > 0)
				{
					connection = (CCWSConnection) instance
							.getProvisioningConnection();
					
					BalanceEntityBase[] balancesModify = new BalanceEntityBase[balances.length + 1];
					//Sync expire Core with expire GPRS of some COS
					boolean syncCOSList = product.getParameter("SyncCOSList", "FALSE").equals("TRUE");
					
					if (syncCOSList)
					{
						for (int j=0; j<balances.length; j++)
						{	
							balancesModify[j] = balances[j];
							
							BalanceEntity core = CCWSConnection.getBalance(connection.getSubscriberInfor(isdn),
									CCWSConnection.CORE_BALANCE);
							
							if (balances[j].getBalanceName().equals("GPRS")
									&& balances[j].getAccountExpiration().after(core.getAccountExpiration()))
							{
								balancesModify[balances.length] = new BalanceEntityBase();
								balancesModify[balances.length].setBalanceName("Core");
								balancesModify[balances.length].setBalance(core.getBalance());
								balancesModify[balances.length].setAccountExpiration(balances[j].getAccountExpiration());
							}
						}
					}

					long sessionId = setRequest(instance, result,
							"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifySubscriber:"
									+ getLogBalances(balancesModify, isdn));
					try
					{
						connection.setBalance(isdn, balancesModify, activeDate, comment);
						setResponse(instance, result, Constants.SUCCESS, sessionId);
					}
					catch (AxisFault error)
					{
						error.printStackTrace();
						String errorCode = getErrorCode(instance, request, error);
						if (!errorCode.equals(""))
						{
							setResponse(instance, result, "CCWS." + errorCode, sessionId);
							result.setStatus(Constants.ORDER_STATUS_DENIED);
							result.setCause(Constants.ERROR);
						}
						else
						{
							throw error;
						}
					}
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}
	
	public VNMMessage modifyBalanceWhenInvite(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);
		
		String isdn = CommandUtil.addCountryCode(request.getParameters().getString("INVITEE_ISDN"));
		result.setIsdn(isdn);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				SubscriberEntity subscriber = getSubscriber(instance, result);

				String[] addBalances = StringUtil.toStringArray(
						product.getParameter("balances", ""), StringPool.COMMA);

				Date now = new Date();

				balances = new BalanceEntityBase[addBalances.length];

				Calendar activeDate = Calendar.getInstance();

				result.setRequestValue("balances.premodified.activeDate",
						subscriber.getDateEnterActive().getTime());
				for (int j = 0; j < addBalances.length; j++)
				{
					String balanceName = addBalances[j];

					String prefix = "balance." + result.getActionType() + "."
							+ balanceName;

					// get current balance
					BalanceEntity balance = CCWSConnection.getBalance(
							subscriber, balanceName);

					// save original balance status before add
					result.setRequestValue("balances." + balanceName
							+ ".premodified.expirationDate", balance
							.getAccountExpiration().getTime());
					result.setRequestValue("balances." + balanceName
							+ ".premodified.amount", balance.getBalance());

					// calculate expiration date for this balance
					Calendar maxExpiredDate = Calendar.getInstance();

					maxExpiredDate.setTime(now);
					
					maxExpiredDate.set(Calendar.HOUR_OF_DAY, 23);
					maxExpiredDate.set(Calendar.MINUTE, 59);
					maxExpiredDate.set(Calendar.SECOND, 59);

					int maxDays = product.getParameters().getInteger(
							prefix + ".maxDays");

					maxExpiredDate.add(Calendar.DATE, maxDays);

					
					Calendar expiredDate = balance.getAccountExpiration();
					
					if (product.isSubscription())
					{
						SubscriberProduct subProduct = SubscriberProductImpl.getUnterminated(
								request.getIsdn(), request.getProductId());
						if (subProduct != null)
						{
							expiredDate.setTime(subProduct.getExpirationDate());
						}
					}
					
//					int expireTime = product.getParameters().getInteger(
//							prefix + ".days");
					int expireTime = product.getSubscriptionPeriod();
					if (result.getCampaignId() != Constants.DEFAULT_ID)
					{
						CampaignEntry campaign = CampaignFactory.getCache()
								.getCampaign(result.getCampaignId());

						if ((campaign != null))
						{
							expireTime = campaign.getSchedulePeriod();
						}
					}
					
					String setExpFlg = product.getParameters().getString(
							prefix + ".resetexpiredate", "false");

					boolean includeCurrentDay = result.getParameters().getBoolean(
							"includeCurrentDay");
					
					if (expiredDate.getTimeInMillis() < now.getTime()
							|| setExpFlg.equals("true"))
					{
						expireTime--;
						expiredDate.setTime(now);
					}
					else if (expiredDate.getTimeInMillis() >= now.getTime()
							&& result.getActionType().equals(Constants.ACTION_SUBSCRIPTION))
					{
						expireTime--;
					}

					boolean truncExpire = Boolean.parseBoolean(product.getParameter("TruncExpireDate", "true"));
					if (truncExpire)
					{
						int expireHour = product.getParameters().getInteger(
								prefix + ".expire.hour", 23);
						int expireMinute = product.getParameters().getInteger(
								prefix + ".expire.minute", 59);
						int expireSecond = product.getParameters().getInteger(
								prefix + ".expire.second", 59);
	
						expiredDate.set(Calendar.HOUR_OF_DAY, expireHour);
						expiredDate.set(Calendar.MINUTE, expireMinute);
						expiredDate.set(Calendar.SECOND, expireSecond);
					}

					if (expireTime > 0)
					{
						expiredDate.add(Calendar.DATE, expireTime);
					}

					if ((maxDays > 0) && expiredDate.after(maxExpiredDate))
					{
						expiredDate = maxExpiredDate;
					}

					if ((maxDays > 0) && expiredDate.after(activeDate))
					{
						activeDate = expiredDate;
					}

					double amount = product.getParameters().getDouble(
							prefix + ".amount");

					balances[j] = new BalanceEntityBase();

					balances[j].setBalanceName(balanceName);

					// Need to check accumulate or reset account DuyMB.
					String accumulateFlg = product.getParameters().getString(
							prefix + ".accumulate", "false");
					if (accumulateFlg.equals("true"))
					{
						balances[j].setBalance(balance.getBalance() + amount);
					}
					else
					{
						balances[j].setBalance(amount);
					}
					
					balances[j].setAccountExpiration(expiredDate);

					// Add response value for writing log file
					result.setResponseValue(balances[j].getBalanceName()
							+ ".amount",
							StringUtil.format(balances[j].getBalance(), "#"));

					result.setResponseValue(balances[j].getBalanceName()
							+ ".expireDate", StringUtil.format(balances[j]
							.getAccountExpiration().getTime(),
							Constants.DATE_FORMAT));
				}

				// String comment = product.getIndexKey();

				String comment = product.getParameter(
						"mtrComment." + product.getAlias() + "."
								+ request.getActionType(), "Comment MTR"
								+ product.getAlias());

				// modify balances
				if (balances.length > 0)
				{
					connection = (CCWSConnection) instance
							.getProvisioningConnection();
					
					BalanceEntityBase[] balancesModify = new BalanceEntityBase[balances.length + 1];
					//Sync expire Core with expire GPRS of some COS
					boolean syncCOSList = product.getParameter("SyncCOSList", "FALSE").equals("TRUE");
//					boolean found = false;
//					if (syncCOSList)
//					{
//						for (int j = 0; !found && (j < product.getAvailCOS().length); j++)
//						{
//							String cos = product.getAvailCOS()[j];
//
//							found = cos.equals(subscriber.getCOSName().trim());
//						}
//					}
					
					if (syncCOSList)
					{
						for (int j=0; j<balances.length; j++)
						{	
							balancesModify[j] = balances[j];
							
							BalanceEntity core = CCWSConnection.getBalance(connection.getSubscriberInfor(isdn),
									CCWSConnection.CORE_BALANCE);
							
							if (balances[j].getBalanceName().equals("GPRS")
									&& balances[j].getAccountExpiration().after(core.getAccountExpiration()))
							{
								balancesModify[balances.length] = new BalanceEntityBase();
								balancesModify[balances.length].setBalanceName(CCWSConnection.CORE_BALANCE);
								balancesModify[balances.length].setBalance(core.getBalance());
								balancesModify[balances.length].setAccountExpiration(balances[j].getAccountExpiration());
							}
						}
					}

					long sessionId = setRequest(instance, result,
							"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifySubscriber:"
									+ getLogBalances(balancesModify, isdn));
					try
					{
						connection.setBalance(isdn, balancesModify, activeDate, comment);
						setResponse(instance, result, Constants.SUCCESS, sessionId);
					}
					catch (AxisFault error)
					{
//						result.setIsdn(request.getParameters().getString("INVITER_ISDN"));
						
						error.printStackTrace();
						String errorCode = getErrorCode(instance, request, error);
						if (!errorCode.equals(""))
						{
							setResponse(instance, result, "CCWS." + errorCode, sessionId);
							result.setStatus(Constants.ORDER_STATUS_DENIED);
							result.setCause(Constants.ERROR);
						}
						else
						{
							throw error;
						}
					}
				}
			}
			catch (Exception error)
			{
//				result.setIsdn(request.getParameters().getString("INVITER_ISDN"));
				
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}
	
	//2013-08-01 MinhDT Add start for CR charge promotion
	public VNMMessage debitBalance(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else if (result.getCampaignId() != Constants.DEFAULT_ID && result.getAmount() == 0)
		{
			instance.debugMonitor("Amount = 0, Do not debit amount.");
			result.getParameters().setProperty("ByPassCommandLog", "true");
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());
				
				SubscriberEntity subscriber = getSubscriber(instance, result);
				
				balances = new BalanceEntityBase[1];
				
				Calendar activeDate = Calendar.getInstance();

				result.setRequestValue("balances.premodified.activeDate",
						subscriber.getDateEnterActive().getTime());
				
				// get current balance
				BalanceEntity balance = CCWSConnection.getBalance(
						subscriber, result.getBalanceType());

				// save original balance status before add
				result.setRequestValue("balances." + result.getBalanceType()
						+ ".premodified.expirationDate", balance
						.getAccountExpiration().getTime());
				result.setRequestValue("balances." + result.getBalanceType()
						+ ".premodified.amount", balance.getBalance());
				
				Calendar expiredDate = balance.getAccountExpiration();

				balances[0] = new BalanceEntityBase();
				balances[0].setBalanceName(result.getBalanceType());
				balances[0].setBalance(balance.getBalance() - result.getAmount());
				balances[0].setAccountExpiration(expiredDate);

				// Add response value for writing log file
				result.setResponseValue(balances[0].getBalanceName()
						+ ".amount",
						StringUtil.format(balances[0].getBalance(), "#"));

				result.setResponseValue(balances[0].getBalanceName()
						+ ".expireDate", StringUtil.format(balances[0]
						.getAccountExpiration().getTime(),
						Constants.DATE_FORMAT));

				// String comment = product.getIndexKey();

				String comment = product.getParameter(
						"mtrComment." + product.getAlias() + "."
								+ request.getActionType(), "Comment MTR"
								+ product.getAlias());

				// modify balances
				connection = (CCWSConnection) instance.getProvisioningConnection();
	
				long sessionId = setRequest(instance, result,
						"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifySubscriber:"
								+ getLogBalances(balances, isdn)
								+ " - Before modify:{" + balance.getBalance() + ";" 
								+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(balance.getAccountExpiration().getTime()) + "}");
				try
				{
					connection.setBalance(isdn, balances, activeDate, comment);
					setResponse(instance, result, Constants.SUCCESS, sessionId);
					
					result.setPaid(true);
				}
				catch (AxisFault error)
				{
					String errorCode = getErrorCode(instance, request, error);
					if (!errorCode.equals(""))
					{
						setResponse(instance, result, "CCWS." + errorCode, sessionId);
						result.setCause(Constants.ERROR);
						result.setStatus(Constants.ORDER_STATUS_DENIED);
					}
					else
					{
						throw error;
					}
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}
	
	public VNMMessage creditBalance(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		SubscriberEntity subscriber = null;
		SubscriberRetrieve subscriberRetrieve = null;
		BalanceEntityBase[] balances = null;

		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else if (result.isPaid())
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(result.getProductId());
				
				connection = (CCWSConnection) instance.getProvisioningConnection();
				
				subscriberRetrieve = connection.getSubscriber(request.getIsdn(), 1);
				if (subscriberRetrieve != null)
				{
					subscriber = subscriberRetrieve.getSubscriberData();
				}
				
				balances = new BalanceEntityBase[1];
				
				Calendar activeDate = Calendar.getInstance();

				result.setRequestValue("balances.premodified.activeDate",
						subscriber.getDateEnterActive().getTime());
				
				// get current balance
				BalanceEntity balance = CCWSConnection.getBalance(
						subscriber, result.getBalanceType());

				balances[0] = new BalanceEntityBase();
				balances[0].setBalanceName(result.getBalanceType());
				balances[0].setBalance(balance.getBalance() + result.getAmount());
				balances[0].setAccountExpiration(balance.getAccountExpiration());

				String comment = product.getParameter(
						"mtrComment." + product.getAlias() + "."
								+ request.getActionType(), "Comment MTR"
								+ product.getAlias());
	
				// modify balances
				if (balances.length > 0)
				{
					long sessionId = setRequest(instance, result,
							"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifySubscriber:"
									+ getLogBalances(balances, isdn) 
									+ " - Before modify:{" + balance.getBalance() + ";" 
									+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(balance.getAccountExpiration().getTime()) + "}");
					try
					{
						connection.setBalance(isdn, balances, activeDate, comment);
						setResponse(instance, result, Constants.SUCCESS, sessionId);
					}
					catch (AxisFault error)
					{
						String errorCode = getErrorCode(instance, request, error);
						if (!errorCode.equals(""))
						{
							setResponse(instance, result, "CCWS." + errorCode, sessionId);
							result.setStatus(Constants.ORDER_STATUS_DENIED);
							result.setCause(Constants.ERROR);
						}
						else
						{
							throw error;
						}
					}
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}
	//2013-08-01 MinhDT Add end for CR charge promotion

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public VNMMessage modifyBalanceForLuckySimProduct(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{

		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				SubscriberEntity subscriber = getSubscriber(instance, result);

				String[] addBalances = StringUtil.toStringArray(
						product.getParameter("balances", ""), StringPool.COMMA);

				Date now = new Date();

				balances = new BalanceEntityBase[addBalances.length];

				Calendar activeDate = Calendar.getInstance();

				result.setRequestValue("balances.premodified.activeDate",
						subscriber.getDateEnterActive().getTime());
				for (int j = 0; j < addBalances.length; j++)
				{
					String balanceName = addBalances[j];

					String prefix = "balance." + result.getActionType() + "."
							+ balanceName;

					// get current balance
					BalanceEntity balance = CCWSConnection.getBalance(
							subscriber, balanceName);

					// save original balance status before add
					result.setRequestValue("balances." + balanceName
							+ ".premodified.expirationDate", balance
							.getAccountExpiration().getTime());
					result.setRequestValue("balances." + balanceName
							+ ".premodified.amount", balance.getBalance());

					// calculate expiration date for this balance
					Calendar maxExpiredDate = Calendar.getInstance();

					maxExpiredDate.setTime(now);
					maxExpiredDate.set(Calendar.HOUR_OF_DAY, 23);
					maxExpiredDate.set(Calendar.MINUTE, 59);
					maxExpiredDate.set(Calendar.SECOND, 59);

					int maxDays = product.getParameters().getInteger(
							prefix + ".maxDays");

					maxExpiredDate.add(Calendar.DATE, maxDays);

					Calendar expiredDate = balance.getAccountExpiration();

					int expireTime = product.getParameters().getInteger(
							prefix + ".days");

					String setExpFlg = product.getParameters().getString(
							prefix + ".resetexpiredate", "false");

					if (expiredDate.getTimeInMillis() < now.getTime()
							|| setExpFlg.equals("true"))
					{
						expireTime--;
						expiredDate.setTime(now);
					}

					expiredDate.set(Calendar.HOUR_OF_DAY, 23);
					expiredDate.set(Calendar.MINUTE, 59);
					expiredDate.set(Calendar.SECOND, 59);

					if (expireTime > 0)
					{
						expiredDate.add(Calendar.DATE, expireTime);
					}

					if ((maxDays > 0) && expiredDate.after(maxExpiredDate))
					{
						expiredDate = maxExpiredDate;
					}

					if ((maxDays > 0) && expiredDate.after(activeDate))
					{
						activeDate = expiredDate;
					}

					double amount = 0;

					// Check balance core
					double dAmountMoreThanCondition = product.getParameters()
							.getDouble(prefix + ".amountMoreThanCondition");
					double dAmountLessThanCondition = product.getParameters()
							.getDouble(prefix + ".amountLessThanCondition");
					SubscriberEntity subscriberEntity = result
							.getSubscriberEntity();
					BalanceEntity balanceCore = CCWSConnection.getBalance(
							subscriberEntity, CCWSConnection.CORE_BALANCE);
					double dAvailableBalacen = balanceCore
							.getAvailableBalance();
					int iAmount = Integer.parseInt(product.getParameter(
							"amountCondition", "0"));
					if (dAvailableBalacen >= iAmount)
					{
						amount = dAmountMoreThanCondition;
					}
					else
					{
						amount = dAmountLessThanCondition;
					}

					balances[j] = new BalanceEntityBase();

					balances[j].setBalanceName(balanceName);

					// Need to check accumulate or reset account DuyMB.
					String accumulateFlg = product.getParameters().getString(
							prefix + ".accumulate", "false");
					if (accumulateFlg.equals("true"))
					{
						balances[j].setBalance(balance.getBalance() + amount);
					}
					else
					{
						balances[j].setBalance(amount);
					}

					balances[j].setAccountExpiration(expiredDate);

					// Add response value for writing log file
					result.setResponseValue(balances[j].getBalanceName()
							+ ".amount",
							StringUtil.format(balances[j].getBalance(), "#"));

					result.setResponseValue(balances[j].getBalanceName()
							+ ".expireDate", StringUtil.format(balances[j]
							.getAccountExpiration().getTime(),
							Constants.DATE_FORMAT));
				}

				// String comment = product.getIndexKey();

				String comment = product.getParameter(
						"mtrComment." + product.getAlias() + "."
								+ request.getActionType(), "Comment MTR"
								+ product.getAlias());

				// modify balances
				if (balances.length > 0)
				{
					connection = (CCWSConnection) instance
							.getProvisioningConnection();

					long sessionId = setRequest(instance, result,
							"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifySubscriber:"
									+ getLogBalances(balances, isdn));

					connection.setBalance(isdn, balances, activeDate, comment);
					setResponse(instance, result, Constants.SUCCESS, sessionId);
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}

	/**
	 * Roll back modified balance
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public VNMMessage rollbackModifyBalance(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				String[] addBalances = StringUtil.toStringArray(
						product.getParameter("balances", ""), StringPool.COMMA);

				Date now = new Date();

				balances = new BalanceEntityBase[addBalances.length];

				SubscriberEntity subEntity = result.getSubscriberEntity();
				
				if (subEntity == null)
					throw new AppException(Constants.ERROR_INVALID_REQUEST);
	
				Calendar activeDate = subEntity.getDateEnterActive();
//				DateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
//				Date activeTime = sdf.parse(result
//						.getRequestValue("balances.premodified.activeDate"));
//				Calendar activeDate = Calendar.getInstance();
//				activeDate.setTime(activeTime);
				for (int j = 0; j < addBalances.length; j++)
				{
					String balanceName = addBalances[j];

					BalanceEntity originalBalance = CCWSConnection.getBalance(subEntity, balanceName);
//					String prefix = "balance." + result.getActionType() + "."
//							+ balanceName;
//
//					double amount = Double.parseDouble(result
//							.getRequestValue("balances." + balanceName
//									+ ".premodified.amount"));
//					Date expirationTime = sdf.parse(result
//							.getRequestValue("balances." + balanceName
//									+ ".premodified.expirationDate"));
//					Calendar expiredDate = Calendar.getInstance();
//					expiredDate.setTime(expirationTime);

					balances[j] = new BalanceEntityBase();

					balances[j].setBalanceName(balanceName);

					balances[j].setBalance(originalBalance.getBalance());

					balances[j].setAccountExpiration(originalBalance.getAccountExpiration());

//					// Add response value for writing log file
//					result.setResponseValue(balances[j].getBalanceName()
//							+ ".amount",
//							StringUtil.format(balances[j].getBalance(), "#"));
//
//					result.setResponseValue(balances[j].getBalanceName()
//							+ ".expireDate", StringUtil.format(balances[j]
//							.getAccountExpiration().getTime(),
//							Constants.DATE_FORMAT));
				}

				// String comment = product.getIndexKey();

				String comment = product.getParameter(
						"mtrComment." + product.getAlias() + "."
								+ request.getActionType(), "Comment MTR"
								+ product.getAlias());

				// modify balances
				if (balances.length > 0)
				{
					connection = (CCWSConnection) instance
							.getProvisioningConnection();

					long sessionId = setRequest(instance, result,
							"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifySubscriber:"
									+ getLogBalances(balances, isdn));
					try
					{
						connection.setBalance(isdn, balances, activeDate, comment);
						setResponse(instance, result, Constants.SUCCESS, sessionId);
					}
					catch (AxisFault error)
					{
						String errorCode = getErrorCode(instance, request, error);
						if (!errorCode.equals(""))
						{
							setResponse(instance, result, "CCWS." + errorCode, sessionId);
							result.setStatus(Constants.ORDER_STATUS_DENIED);
							result.setCause(Constants.ERROR);
						}
						else
						{
							throw error;
						}
					}
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}
	
	public VNMMessage rollbackModifyWhenInvite(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);
		
		String isdn = CommandUtil.addCountryCode(request.getParameters().getString("INVITEE_ISDN"));
		result.setIsdn(isdn);
		
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				String[] addBalances = StringUtil.toStringArray(
						product.getParameter("balances", ""), StringPool.COMMA);

				Date now = new Date();

				balances = new BalanceEntityBase[addBalances.length];

				SubscriberEntity subEntity = result.getSubscriberEntity();
				
				if (subEntity == null)
					throw new AppException(Constants.ERROR_INVALID_REQUEST);
	
				Calendar activeDate = subEntity.getDateEnterActive();

				for (int j = 0; j < addBalances.length; j++)
				{
					String balanceName = addBalances[j];

					BalanceEntity originalBalance = CCWSConnection.getBalance(subEntity, balanceName);

					balances[j] = new BalanceEntityBase();

					balances[j].setBalanceName(balanceName);

					balances[j].setBalance(originalBalance.getBalance());

					balances[j].setAccountExpiration(originalBalance.getAccountExpiration());
				}

				String comment = product.getParameter(
						"mtrComment." + product.getAlias() + "."
								+ request.getActionType(), "Comment MTR"
								+ product.getAlias());

				// modify balances
				if (balances.length > 0)
				{
					connection = (CCWSConnection) instance
							.getProvisioningConnection();

					long sessionId = setRequest(instance, result,
							"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifySubscriber:"
									+ getLogBalances(balances, isdn));
					try
					{
						connection.setBalance(isdn, balances, activeDate, comment);
						setResponse(instance, result, Constants.SUCCESS, sessionId);
					}
					catch (AxisFault error)
					{
//						result.setIsdn(request.getParameters().getString("INVITER_ISDN"));
						
						String errorCode = getErrorCode(instance, request, error);
						if (!errorCode.equals(""))
						{
							setResponse(instance, result, "CCWS." + errorCode, sessionId);
							result.setStatus(Constants.ORDER_STATUS_DENIED);
							result.setCause(Constants.ERROR);
						}
						else
						{
							throw error;
						}
					}
				}
			}
			catch (Exception error)
			{
//				result.setIsdn(request.getParameters().getString("INVITER_ISDN"));
				
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}

	/**
	 * Sets subscriber state to suspend s1
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public VNMMessage suspendSubscriberS1(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;
		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				connection = (CCWSConnection) instance
						.getProvisioningConnection();
				connection.setSubscriberState(isdn,
						Constants.BALANCE_STATE_SUSPEND_S1);
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, result, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	/**
	 * Sets subscriber state to suspend s2
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public VNMMessage suspendSubscriberS2(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;
		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				connection = (CCWSConnection) instance
						.getProvisioningConnection();
				connection.setSubscriberState(isdn,
						Constants.BALANCE_STATE_SUSPEND_S2);
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, result, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	/**
	 * Sets subscriber state to active.
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public VNMMessage activeSubscriber(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;
		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				connection = (CCWSConnection) instance
						.getProvisioningConnection();
				connection.setSubscriberState(isdn,
						Constants.BALANCE_STATE_ACTIVE);
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, result, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	public VNMMessage rollbackSetSubscriberState(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;
		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			if (result.getSubscriberEntity() != null)
			{
				try
				{

					connection = (CCWSConnection) instance
							.getProvisioningConnection();
					connection.setSubscriberState(isdn, result
							.getSubscriberEntity().getCurrentState());
				}
				catch (Exception e)
				{
					processError(instance, provisioningCommand, result, e);
				}
				finally
				{
					instance.closeProvisioningConnection(connection);
				}
			}
		}
		return result;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public VNMMessage modifySubscriber(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				SubscriberEntity subscriber = getSubscriber(instance, result);

				String[] addBalances = StringUtil.toStringArray(
						product.getParameter("balances", ""), StringPool.COMMA);

				Date now = new Date();

				balances = new BalanceEntityBase[addBalances.length];

				Calendar activeDate = Calendar.getInstance();

				for (int j = 0; j < addBalances.length; j++)
				{
					String balanceName = addBalances[j];

					String prefix = "balance." + result.getActionType() + "."
							+ balanceName;

					// get current balance
					BalanceEntity balance = CCWSConnection.getBalance(
							subscriber, balanceName);

					// calculate expiration date for this balance
					Calendar maxExpiredDate = Calendar.getInstance();

					maxExpiredDate.setTime(now);
					maxExpiredDate.set(Calendar.HOUR, 23);
					maxExpiredDate.set(Calendar.MINUTE, 59);
					maxExpiredDate.set(Calendar.SECOND, 59);

					int maxDays = product.getParameters().getInteger(
							prefix + ".maxDays");

					maxExpiredDate.add(Calendar.DATE, maxDays);

					Calendar expiredDate = balance.getAccountExpiration();

					if (expiredDate.getTime().before(now))
					{
						expiredDate.setTime(now);
					}

					expiredDate.set(Calendar.HOUR, 23);
					expiredDate.set(Calendar.MINUTE, 59);
					expiredDate.set(Calendar.SECOND, 59);

					int expireTime = product.getParameters().getInteger(
							prefix + ".days");

					expireTime--;

					if (expireTime > 0)
					{
						expiredDate.add(Calendar.DATE, expireTime);
					}

					if ((maxDays > 0) && expiredDate.after(maxExpiredDate))
					{
						expiredDate = maxExpiredDate;
					}

					if ((maxDays > 0) && expiredDate.after(activeDate))
					{
						activeDate = expiredDate;
					}

					int amount = product.getParameters().getInteger(
							prefix + ".amount");

					balances[j] = new BalanceEntityBase();

					balances[j].setBalanceName(balanceName);
					balances[j].setBalance(balance.getBalance() + amount);
					balances[j].setAccountExpiration(expiredDate);

					// Add response value
					result.setResponseValue(balances[j].getBalanceName()
							+ ".amount",
							StringUtil.format(balances[j].getBalance(), "#"));
					result.setResponseValue(balances[j].getBalanceName()
							+ ".expireDate", StringUtil.format(balances[j]
							.getAccountExpiration().getTime(),
							Constants.DATE_FORMAT));
				}

				String comment = product.getIndexKey();

				// modify balances
				if (balances.length > 0)
				{
					connection = (CCWSConnection) instance
							.getProvisioningConnection();

					connection.setBalance(isdn, balances, activeDate, comment);
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public VNMMessage resetBalance(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);

		String isdn = CommandUtil.addCountryCode(result.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				SubscriberEntity subscriber = getSubscriber(instance, result);

				String[] addBalances = StringUtil.toStringArray(
						product.getParameter("balances", ""), StringPool.COMMA);

				Date now = new Date();

				balances = new BalanceEntityBase[addBalances.length];

				Calendar activeDate = Calendar.getInstance();

				for (int j = 0; j < addBalances.length; j++)
				{
					String balanceName = addBalances[j];

					String prefix = "balance." + result.getActionType() + "."
							+ balanceName;

					// get current balance
					BalanceEntity balance = CCWSConnection.getBalance(
							subscriber, balanceName);

					// calculate expiration date for this balance
					Calendar expiredDate = balance.getAccountExpiration();

					if (expiredDate.getTime().after(now))
					{
						expiredDate.setTime(now);
					}

					boolean clearBalance = product.getParameters().getBoolean(
							prefix + ".clear", false);

					balances[j] = new BalanceEntityBase();
					balances[j].setBalanceName(balanceName);
					balances[j].setBalance(clearBalance ? 0 : balance
							.getBalance());
					balances[j].setAccountExpiration(expiredDate);

					result.setResponseValue(balances[j].getBalanceName()
							+ ".amount",
							StringUtil.format(balances[j].getBalance(), "#"));
					result.setResponseValue(balances[j].getBalanceName()
							+ ".expireDate", StringUtil.format(balances[j]
							.getAccountExpiration().getTime(),
							Constants.DATE_FORMAT));
				}

//				String comment = product.getIndexKey();
				String comment = product.getParameter(
						"mtrComment." + product.getAlias() + "." + request.getActionType(), 
						"Comment MTR" + product.getAlias());

				// modify balances
				if (balances.length > 0)
				{
					connection = (CCWSConnection) instance
							.getProvisioningConnection();
					
					long sessionId = setRequest(instance, result,
							"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifySubscriber:"
									+ getLogBalances(balances, isdn));
					connection.setBalance(isdn, balances, activeDate, comment);
					setResponse(instance, result, Constants.SUCCESS, sessionId);
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public VNMMessage createCircle(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);

		String isdn = CommandUtil.addCountryCode(result.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				// Calling circle
				String prefix = "circle." + request.getActionType() + ".";

				String serviceProvider = product.getParameters()
						.getString(prefix + "serviceProvider",
								product.getCircleProvider());

				String circleGroup = product.getParameters().getString(
						prefix + "group", product.getCircleGroup());

				String circleName = product.getParameters().getString(
						prefix + "name", product.getCircleName());

				if (product.getParameters().getString(prefix + "align", "left")
						.equalsIgnoreCase("left"))
				{
					circleName = circleName + "_" + isdn;
				}
				else
				{
					circleName = isdn + "_" + circleName;
				}

				// modify balances
				connection = (CCWSConnection) instance
						.getProvisioningConnection();

				long sessionId = setRequest(
						instance,
						result,
						getLogRequest(
								"com.comverse_in.prepaid.ccws.ServiceSoapStub.changeCallingCircle",
								result.getIsdn()
										+ "," + circleGroup
										+ "," + circleName
										+ "," + serviceProvider
										+ "," + product.getMaxMembers()));

				if (connection.createCallingCircle(circleName, circleGroup,
						serviceProvider, product.getMaxMembers()))
					setResponse(instance, result, Constants.SUCCESS, sessionId);
				else
					setResponse(instance, result, Constants.ERROR, sessionId);
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);

				if (result.getCause().equals("7003"))
				{
					result.setCause(Constants.SUCCESS);
					result.setStatus(Constants.ORDER_STATUS_APPROVED);
				}
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public VNMMessage removeCircle(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);

		String isdn = CommandUtil.addCountryCode(result.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				// Calling circle
				String prefix = "circle." + request.getActionType() + ".";

				String circleName = product.getParameters().getString(
						prefix + "name", product.getCircleName());

				if (product.getParameters().getString(prefix + "align", "left")
						.equalsIgnoreCase("left"))
				{
					circleName = circleName + "_" + isdn;
				}
				else
				{
					circleName = isdn + "_" + circleName;
				}

				// modify balances
				connection = (CCWSConnection) instance
						.getProvisioningConnection();

				long sessionId = setRequest(
						instance,
						result,
						getLogRequest(
								"com.comverse_in.prepaid.ccws.ServiceSoapStub.deleteCallingCircle",
								result.getIsdn()
										+ "," + circleName));

				if (connection.deleteCC(circleName))
					setResponse(instance, result, Constants.SUCCESS, sessionId);
				else
					setResponse(instance, result, Constants.ERROR, sessionId);
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public VNMMessage modifyCircle(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);

		String isdn = CommandUtil.addCountryCode(result.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						request.getProductId());

				String circleName = isdn + "_"
						+ product.getParameter("circle.name", "");

				// modify balances
				connection = (CCWSConnection) instance
						.getProvisioningConnection();

				long sessionId = setRequest(
						instance,
						result,
						getLogRequest(
								"com.comverse_in.prepaid.ccws.ServiceSoapStub.deleteCallingCircle",
								result.getIsdn()
										+ "," + circleName));

				if (connection.deleteCC(circleName))
					setResponse(instance, result, Constants.SUCCESS, sessionId);
				else
					setResponse(instance, result, Constants.ERROR, sessionId);
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public VNMMessage addOffer(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);
		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						request.getProductId());

				Date now = new Date();
				
				String offerName = product.getParameter(
						Constants.PARM_OFFER_NAME, product.getOfferName());
				
				String prefix = "offer." + result.getActionType() + "." + offerName;

//				int alcoDuration = Integer.parseInt(product.getParameter(
//						Constants.PARM_OFFER_DURATION,
//						Constants.PARM_OFFER_DURATION_VALUE));

				Calendar serviceStart = Calendar.getInstance();
				Calendar serviceEnd = Calendar.getInstance();
				
				if (product.isSubscription())
				{
					SubscriberProduct subProduct = SubscriberProductImpl.getUnterminated(
							request.getIsdn(), request.getProductId());
					if (subProduct != null)
					{
						serviceEnd.setTime(subProduct.getExpirationDate());
					}
				}
				
				int alcoDuration = product.getSubscriptionPeriod();
				if (result.getCampaignId() != Constants.DEFAULT_ID)
				{
					CampaignEntry campaign = CampaignFactory.getCache()
							.getCampaign(result.getCampaignId());

					if ((campaign != null))
					{
						alcoDuration = campaign.getSchedulePeriod();
					}
				}
				
				String setExpFlg = product.getParameters().getString(
						prefix + ".resetexpiredate", "false");

				if (serviceEnd.getTimeInMillis() < now.getTime()
						|| setExpFlg.equals("true"))
				{
					alcoDuration--;
					serviceEnd.setTime(now);
				}
				else if (serviceEnd.getTimeInMillis() >= now.getTime()
						&& result.getActionType().equals(Constants.ACTION_SUBSCRIPTION))
				{
					alcoDuration--;
				}

				boolean truncExpire = Boolean.parseBoolean(product.getParameter("TruncExpireDate", "true"));
				if (truncExpire)
				{
					int expireHour = product.getParameters().getInteger(
							prefix + ".expire.hour", 23);
					int expireMinute = product.getParameters().getInteger(
							prefix + ".expire.minute", 59);
					int expireSecond = product.getParameters().getInteger(
							prefix + ".expire.second", 59);

					serviceEnd.set(Calendar.HOUR_OF_DAY, expireHour);
					serviceEnd.set(Calendar.MINUTE, expireMinute);
					serviceEnd.set(Calendar.SECOND, expireSecond);
				}

				if (alcoDuration > 0)
				{
					serviceEnd.add(Calendar.DATE, alcoDuration);
				}
				
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

				// modify balances
				connection = (CCWSConnection) instance
						.getProvisioningConnection();

				long sessionId = setRequest(
						instance,
						result,
						getLogRequest(
								"com.comverse_in.prepaid.ccws.ServiceSoapStub.subscribeOffer",
								result.getIsdn()
										+ "," + offerName
										+ "," + df.format(serviceStart.getTime())
										+ "," + df.format(serviceEnd.getTime())));
				try
				{
					connection.createAlco(offerName, isdn, serviceEnd, serviceStart);
					setResponse(instance, result, Constants.SUCCESS, sessionId);
				}
				catch (AxisFault error)
				{
					String errorCode = getErrorCode(instance, request, error);
					if (!errorCode.equals(""))
					{
						setResponse(instance, result, "CCWS." + errorCode, sessionId);
						result.setCause(Constants.ERROR);
						result.setStatus(Constants.ORDER_STATUS_DENIED);
					}
					else
					{
						throw error;
					}
				}
			}
			catch (Exception error)
			{
				result.setCause(Constants.ERROR);
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public VNMMessage removeOffer(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);
		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		try
		{
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());

			String offerName = product.getParameter(Constants.PARM_OFFER_NAME,
					product.getOfferName());

			// modify balances

			connection = (CCWSConnection) instance.getProvisioningConnection();

			long sessionId = setRequest(
					instance,
					result,
					getLogRequest(
							"com.comverse_in.prepaid.ccws.ServiceSoapStub.unsubscribeOffer",
							result.getIsdn()
									+ "," + offerName));
			try
			{
				connection.deleteAlco(offerName, isdn);	
				setResponse(instance, result, Constants.SUCCESS, sessionId);
			}
			catch (AxisFault error)
			{
				String errorCode = getErrorCode(instance, request, error);
				if (!errorCode.equals(""))
				{
					setResponse(instance, result, "CCWS." + errorCode, sessionId);
					result.setStatus(Constants.ORDER_STATUS_DENIED);
					result.setCause(Constants.ERROR);
				}
				else
				{
					throw error;
				}
			}
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, result, error);
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}
		return result;
	}

	public VNMMessage addPhoneBook(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);
		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						request.getProductId());

				ProductRoute productRoute = ProductFactory.getCache()
						.getProductRoute(request.getRouteId());

				int intMaxMember = Integer.parseInt(product.getParameter(
						Constants.PARM_MAX_MEMBER, "5"));

				String[] phoneBookList = request.getRequestValue("f5-member", "").split(",");
				
				// Modify phone book before update.
				SubscriberPB subPhoneBookList = new SubscriberPB();
				for (int i = 0; i < intMaxMember; i++)
				{
					switch (i)
					{
					case 0:
						subPhoneBookList
								.setDestNumber1(i < phoneBookList.length ? CommandUtil
										.addCountryCode(phoneBookList[i]) : "");
						break;
					case 1:
						subPhoneBookList
								.setDestNumber2(i < phoneBookList.length ? CommandUtil
										.addCountryCode(phoneBookList[i]) : "");
						break;
					case 2:
						subPhoneBookList
								.setDestNumber3(i < phoneBookList.length ? CommandUtil
										.addCountryCode(phoneBookList[i]) : "");
						break;
					case 3:
						subPhoneBookList
								.setDestNumber4(i < phoneBookList.length ? CommandUtil
										.addCountryCode(phoneBookList[i]) : "");
						break;
					case 4:
						subPhoneBookList
								.setDestNumber5(i < phoneBookList.length ? CommandUtil
										.addCountryCode(phoneBookList[i]) : "");
						break;
					case 5:
						subPhoneBookList
								.setDestNumber6(i < phoneBookList.length ? CommandUtil
										.addCountryCode(phoneBookList[i]) : "");
						break;
					case 6:
						subPhoneBookList
								.setDestNumber7(i < phoneBookList.length ? CommandUtil
										.addCountryCode(phoneBookList[i]) : "");
						break;
					case 7:
						subPhoneBookList
								.setDestNumber8(i < phoneBookList.length ? CommandUtil
										.addCountryCode(phoneBookList[i]) : "");
						break;
					case 8:
						subPhoneBookList
								.setDestNumber9(i < phoneBookList.length ? CommandUtil
										.addCountryCode(phoneBookList[i]) : "");
						break;
					default:
					}
				}
				connection = (CCWSConnection) instance
						.getProvisioningConnection();
				connection.setPhoneBook(isdn, subPhoneBookList);
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	public VNMMessage removePhoneBook(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);
		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						request.getProductId());

				ProductRoute productRoute = ProductFactory.getCache()
						.getProductRoute(request.getRouteId());
				String removeMemList = request.getKeyword().replaceFirst(
						productRoute.getKeyword(), "");
				removeMemList = removeMemList.trim();
				String[] arrayRemoveMemList = removeMemList.split(",");
				// Validate member
				if (!isValidateSub(arrayRemoveMemList))
				{
					AppException error = new AppException(
							Constants.ERROR_SUBSCRIBER_NOT_FOUND);
					processError(instance, provisioningCommand, result, error);
					return result;
				}
				// Get member
				String currentPhoneBookList = SubscriberProductImpl
						.getMemberList(isdn, "", request.getProductId(), false);

				for (int i = 0; i < arrayRemoveMemList.length; i++)
				{
					if (currentPhoneBookList.contains(arrayRemoveMemList[i]))
					{
						currentPhoneBookList = currentPhoneBookList.replaceAll(
								arrayRemoveMemList[i], "");
					}
				}

				int intMaxMember = Integer.parseInt(product.getParameter(
						Constants.PARM_MAX_MEMBER, "5"));
				String[] PhoneBookList = currentPhoneBookList.split(",");
				SubscriberPB subPhoneBookList = new SubscriberPB();
				for (int i = 0; i < intMaxMember; i++)
				{
					switch (i)
					{
					case 0:
						subPhoneBookList
								.setDestNumber1(i < PhoneBookList.length ? CommandUtil
										.addCountryCode(PhoneBookList[i]) : "");
						break;
					case 1:
						subPhoneBookList
								.setDestNumber2(i < PhoneBookList.length ? CommandUtil
										.addCountryCode(PhoneBookList[i]) : "");
						break;
					case 2:
						subPhoneBookList
								.setDestNumber3(i < PhoneBookList.length ? CommandUtil
										.addCountryCode(PhoneBookList[i]) : "");
						break;
					case 3:
						subPhoneBookList
								.setDestNumber4(i < PhoneBookList.length ? CommandUtil
										.addCountryCode(PhoneBookList[i]) : "");
						break;
					case 4:
						subPhoneBookList
								.setDestNumber5(i < PhoneBookList.length ? CommandUtil
										.addCountryCode(PhoneBookList[i]) : "");
						break;
					case 5:
						subPhoneBookList
								.setDestNumber6(i < PhoneBookList.length ? CommandUtil
										.addCountryCode(PhoneBookList[i]) : "");
						break;
					case 6:
						subPhoneBookList
								.setDestNumber7(i < PhoneBookList.length ? CommandUtil
										.addCountryCode(PhoneBookList[i]) : "");
						break;
					case 7:
						subPhoneBookList
								.setDestNumber8(i < PhoneBookList.length ? CommandUtil
										.addCountryCode(PhoneBookList[i]) : "");
						break;
					case 8:
						subPhoneBookList
								.setDestNumber9(i < PhoneBookList.length ? CommandUtil
										.addCountryCode(PhoneBookList[i]) : "");
						break;
					default:
					}
				}
				// modify balances
				connection = (CCWSConnection) instance
						.getProvisioningConnection();
				connection.setPhoneBook(isdn, subPhoneBookList);
				result.setRequestValue("f5-remove-member", removeMemList);
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	public VNMMessage deletePhoneBook(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);
		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						request.getProductId());

				// Get member
				String currentPhoneBookList = SubscriberProductImpl
						.getMemberList(isdn, "", request.getProductId(), false);

				int intMaxMember = Integer.parseInt(product.getParameter(
						Constants.PARM_MAX_MEMBER, "5"));
				SubscriberPB subPhoneBookList = new SubscriberPB();
				for (int i = 0; i < intMaxMember; i++)
				{
					switch (i)
					{
					case 0:
						subPhoneBookList.setDestNumber1("");
					case 1:
						subPhoneBookList.setDestNumber2("");
					case 2:
						subPhoneBookList.setDestNumber3("");
					case 3:
						subPhoneBookList.setDestNumber4("");
					case 4:
						subPhoneBookList.setDestNumber5("");
					case 5:
						subPhoneBookList.setDestNumber6("");
					case 6:
						subPhoneBookList.setDestNumber7("");
					case 7:
						subPhoneBookList.setDestNumber8("");
					case 8:
						subPhoneBookList.setDestNumber9("");

					default:
					}
				}
				// modify balances
				connection = (CCWSConnection) instance
						.getProvisioningConnection();
				connection.setPhoneBook(isdn, subPhoneBookList);
				AppProperties properties = result.getParameters();
				properties.setString("f5-remove-member", currentPhoneBookList);
				result.setParameters(properties);
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	public static boolean isValidateSub(String[] array)
	{
		for (int i = 0; i < array.length; i++)
		{

			if (!array[i].startsWith("092") 
					&& !array[i].startsWith("0188")
					&& !array[i].startsWith("0186")
					&& !array[i].startsWith("8492") 
					&& !array[i].startsWith("84188")
					&& !array[i].startsWith("84186"))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Add member into Calling circle.
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public VNMMessage addToCallingCircle(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);

		String[] arrayOfMem = { request.getIsdn(), request.getShipTo() };
		String isdn = request.getIsdn();

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				// Calling circle
				String prefix = "circle." + request.getActionType() + ".";

				String serviceProvider = product.getParameters()
						.getString(prefix + "serviceProvider",
								product.getCircleProvider());

				String circleGroup = product.getParameters().getString(
						prefix + "group", product.getCircleGroup());

				String circleName = product.getParameters().getString(
						prefix + "name", product.getCircleName());

				if (product.getParameters().getString(prefix + "align", "left")
						.equalsIgnoreCase("left"))
				{
					circleName = circleName + "_" + isdn;
				}
				else
				{
					circleName = isdn + "_" + circleName;
				}
				// add member to Calling Circle

				long sessionId = setRequest(
						instance,
						result,
						getLogRequest(
								"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifyCallingCircleMembers",
								result.getIsdn()
										+ "," + circleGroup
										+ "," + circleName
										+ "," + request.getIsdn() + " - " + request.getShipTo()
										+ ",JOIN"));
				connection = (CCWSConnection) instance
						.getProvisioningConnection();
				if (connection.addMemberToCC(arrayOfMem, circleName, "JOIN"))
					setResponse(instance, result, Constants.SUCCESS, sessionId);
				else
					setResponse(instance, result, Constants.ERROR, sessionId);
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	/**
	 * Add member into Calling circle.
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public VNMMessage removeFromCallingCircle(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);

		String isdn = request.getIsdn();

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				// Calling circle
				String prefix = "circle." + request.getActionType() + ".";

				String circleName = product.getParameters().getString(
						prefix + "name", product.getCircleName());

				if (product.getParameters().getString(prefix + "align", "left")
						.equalsIgnoreCase("left"))
				{
					circleName = circleName + "_" + isdn;
				}
				else
				{
					circleName = isdn + "_" + circleName;
				}
				// Remove member From Calling Circle
				String memberList = SubscriberProductImpl.getMemberList(
						request.getIsdn(), request.getUserName(),
						request.getProductId(), true);
				memberList = request.getIsdn() + "," + memberList;
				String[] arrayOfMem = memberList.split(",");

				long sessionId = setRequest(
						instance,
						result,
						getLogRequest(
								"com.comverse_in.prepaid.ccws.ServiceSoapStub.modifyCallingCircleMembers",
								result.getIsdn()
										+ "," + circleName
										+ "," + memberList
										+ ",LEAVE"));

				connection = (CCWSConnection) instance
						.getProvisioningConnection();
				if (connection.addMemberToCC(arrayOfMem, circleName, "LEAVE"))
					setResponse(instance, result, Constants.SUCCESS, sessionId);
				else
					setResponse(instance, result, Constants.ERROR, sessionId);
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		return result;
	}

	public boolean voucherTopup(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		boolean success = false;

		String topupISDN = request.getShipTo(); // get top up isdn.
		String responseCode = "";
		String secretCode = "";

		double amount = -1;

		String isdn = CommandUtil.addCountryCode(StringUtil.nvl(
				request.getIsdn(), ""));

		if (isdn.equals(""))
		{
			isdn = request.getIsdn();
		}

		CCWSConnection connection = null;

		try
		{
			connection = (CCWSConnection) instance.getProvisioningConnection();

			secretCode = request.getParameters().getProperty("secretCode", "");

			String description = "Postpaid payment for " + request.getIsdn()
					+ " - " + request.getShipTo();

			ArrayOfDeltaBalance balances = connection
					.rechargeAccountBySubscriber(topupISDN, secretCode,
							description);

			for (int j = 0; j < balances.getDeltaBalance().length; j++)
			{
				if (balances.getDeltaBalance(j).getBalanceName().toUpperCase()
						.equals("CORE"))
				{
					amount = balances.getDeltaBalance(j).getDelta();

					break;
				}
			}

			if (amount > 0)
			{
				Properties response = (Properties) request.getParameters();
				response.setProperty("topup", topupISDN);
				response.setProperty("amount", String.valueOf(amount));

				responseCode = "postpaid-payment";

				request.getParameters().setProperty("topup", topupISDN);
				request.getParameters().setProperty("amount",
						String.valueOf(amount));
			}
			else
			{
				responseCode = Constants.ERROR;
			}

			success = true;
		}
		catch (SocketException e)
		{
			throw e;
		}
		catch (RemoteException e)
		{
			processError(instance, provisioningCommand, request, e);
			success = true;
		}
		catch (Exception e)
		{
			processError(instance, provisioningCommand, request, e);
			throw e;
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}

		request.setCause(responseCode);

		return success;
	}

	public boolean setAccountBalance(CommandInstance instance,
			CommandMessage request) throws Exception
	{
		CCWSConnection connection = null;

		String cause = "";

		String isdn = CommandUtil.addCountryCode(StringUtil.nvl(
				request.getIsdn(), ""));
		try
		{
			double amount = 0;
			String balanceType = "";
			BalanceEntityBase[] balances = new BalanceEntityBase[1];

			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());

			if (product == null)
			{
				throw new AppException(Constants.ERROR_PRODUCT_NOT_FOUND);
			}
			else
			{
				balanceType = product.getParameter("loyalty.balance.prepaid",
						"");
				if ("".equals(balanceType))
				{
					throw new AppException(Constants.ERROR_BALANCE_NOT_FOUND);
				}

				request.getParameters().setProperty("balanceType", balanceType);
			}

			String comment = product.getTitle();

			connection = (CCWSConnection) instance.getProvisioningConnection();

			BalanceEntity balance = null;

			double remainingBalance = 0;

			if (!product.getParameter("resetBalance", "false").equals("false"))
			{
				balance = connection.getBalance(request.getIsdn(), balanceType);

				if (balance == null)
				{
					throw new AppException(Constants.ERROR_BALANCE_NOT_FOUND);
				}

				balanceType = balance.getBalanceName();

				remainingBalance = balance.getBalance();

				double adjustAmount = request.getAmount();

				if (request.getActionType().equals(Constants.ACTION_ROLLBACK))
				{
					amount = remainingBalance - adjustAmount;
				}
				else
				{
					amount = remainingBalance + adjustAmount;
				}
			}
			else
			{
				amount = 0;
			}

			// expire time calculate
			int expireTime = 0;

			Calendar expiredDate = Calendar.getInstance();
			if (balance != null)
			{
				expiredDate = balance.getAccountExpiration();

				if (expiredDate.getTime().before(new Date()))
				{
					expiredDate.setTime(new Date());
				}
			}
			else
			{
				expiredDate.setTime(new Date());
			}
			expiredDate.set(Calendar.HOUR, 23);
			expiredDate.set(Calendar.MINUTE, 59);
			expiredDate.set(Calendar.SECOND, 59);

			if (product.isSubscription())
			{
				if (product.getSubscriptionUnit().equals("daily"))
				{
					expireTime = -1;
				}
				else if (product.getSubscriptionUnit().equals("weekly"))
				{
					expireTime = 5;
				}
				else if (product.getSubscriptionUnit().equals("monthly"))
				{
					expireTime = 28;
				}
				else
				{
					expireTime = 28;
				}

				if (expireTime > 0)
				{
					expireTime = expireTime * product.getSubscriptionPeriod();
				}
			}
			else
			{
				expireTime = Integer.valueOf(product.getParameter(
						"loyalty.balance.days", "0"));
			}

			expiredDate.add(Calendar.DATE, expireTime);

			balances[0].setBalanceName(balanceType);
			balances[0].setAccountExpiration(expiredDate);
			balances[0].setBalance(amount);

			Calendar activeDate = Calendar.getInstance();

			connection.setBalance(isdn, balances, activeDate, comment);

			cause = Constants.SUCCESS;
		}
		catch (AppException e)
		{
			request.setStatus(Constants.ORDER_STATUS_DENIED);
			cause = e.getMessage();
		}
		catch (RemoteException e)
		{
			request.setStatus(Constants.ORDER_STATUS_DENIED);
			request.setDescription(e.getMessage());
			throw e;
		}
		catch (Exception e)
		{
			request.setStatus(Constants.ORDER_STATUS_DENIED);
			throw e;
		}
		finally
		{
			request.setCause(cause);

			instance.closeProvisioningConnection(connection);
		}
		return true;
	}

	public boolean redeem(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		try
		{
			if (!setAccountBalance(instance, request))
			{
				SubscriberBalanceImpl.adjustment(request.getUserId(),
						request.getUserName(), request.getSubscriberId(),
						request.getIsdn(), "LOYALTY", request.getQuantity());

				return false;
			}

			if (request.getStatus() == Constants.ORDER_STATUS_DENIED)
			{
				// SubscriberBalanceImpl.adjustment(request);
			}
			else if (request.getCause().equals(Constants.SUCCESS))
			{
				request.setCause("prepaid-success");
			}

			String smsContent = ProductFactory
					.getCache()
					.getProduct(request.getProductId())
					.getMessage(request.getActionType(),
							request.getCampaignId(),
							Constants.DEFAULT_LANGUAGE, request.getChannel(),
							request.getCause());

			if (smsContent.equals(""))
			{
				return true;
			}

			smsContent = smsContent.replaceAll("<score>",
					String.valueOf(request.getQuantity()));

			CommandUtil.sendSMS(instance, request, smsContent);
		}
		catch (Exception e)
		{
			throw e;
		}

		return true;
	}

	public String getLogBalances(BalanceEntityBase[] balances, String isdn)
	{
		String log = isdn + "";
		if (balances.length > 0)
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			log += ":";
			for (BalanceEntityBase balance : balances)
			{
				if (balance != null)
				{
					log += balance.getBalanceName() + "{" + balance.getBalance()
							+ ";"
							+ df.format(balance.getAccountExpiration().getTime())
							+ "},";
				}
			}
		}
		return log;
	}

	public String getLogRequest(String functionName, String isdn)
	{
		String log = functionName + "{" + isdn + "}";

		return log;
	}

	public String getLogResponse(SubscriberEntity infor, String isdn)
	{
		String balance = "{" + isdn + "}";
		BalanceEntity[] data = infor.getBalances().getBalance();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		for (int i = 0; i < data.length; i++)
		{
			if (data[i].getBalance() > 0)
			{
				balance = balance + data[i].getBalanceName() + "{"
						+ data[i].getAvailableBalance() + ";"
						+ df.format(data[i].getAccountExpiration().getTime())
						+ "},";
			}
		}

		String log = "cosname=" + infor.getCOSName() + "," + "status="
				+ infor.getCurrentState() + "," + "active_date="
				+ df.format(infor.getDateEnterActive().getTime()) + ","
				+ balance;

		return log;
	}
	
	// 2012-09-25 MinhDT ADD_END: add for product VB600 & VB220
	public VNMMessage checkOverdueIDDBuffet(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		VNMMessage vnmMessage = CommandUtil.createVNMMessage(request);

		ProductEntry product = ProductFactory.getCache().getProduct(
				vnmMessage.getProductId());

		SubscriberProduct subProduct = SubscriberProductImpl.getActive(
				vnmMessage.getIsdn(), vnmMessage.getProductId());

		CCWSConnection connection = null;

		String responseCode = "";
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, vnmMessage);
		}
		else
		{
			try
			{
				Date registerDate = subProduct.getRegisterDate();
				Date currentDate = new Date();
				double dayBetween = (double) ((currentDate.getTime() - registerDate
						.getTime()) / (1000 * 60 * 60 * 24));

				connection = (CCWSConnection) instance.getProvisioningConnection();

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
				instance.logMonitor("SEND: " + strRequest + ". ID=" + sessionId);
				vnmMessage.setRequest("SEND: " + strRequest + ". ID=" + sessionId);
				
				Date startTime = new Date();
				vnmMessage.setRequestTime(new Date());
				
				SubscriberRetrieve subscriberRetrieve = connection.getSubscriber(vnmMessage.getIsdn(), 1);
				Date endTime = new Date();
				
				SubscriberEntity subEntity = null;
				long costTime = CommandUtil.calculateCostTime(startTime, endTime);
				if (subscriberRetrieve != null)
				{
					subEntity = subscriberRetrieve.getSubscriberData();
					String strResponse = (new CCWSCommandImpl()).getLogResponse(subEntity, vnmMessage.getIsdn());
					
					vnmMessage.setSubscriberType(Constants.PREPAID_SUB_TYPE);
					
					vnmMessage.setResponseTime(new Date());
					instance.logMonitor("RECEIVE:" + strResponse + ". ID=" + sessionId + ". costTime=" + costTime);
					vnmMessage.setResponse("RECEIVE:" + strResponse + ". ID=" + sessionId + ". costTime=" + costTime);
				}
				
				long balance = (long) CCWSConnection.getBalance(subEntity, "VB600_M").getAvailableBalance();

				long minBalance = Long.parseLong(product.getParameter("MinBalance", "1000"));

				boolean notEnough = false;
				if (balance <= minBalance)
				{
					notEnough = true;
				}

				double maxExpired = Double.parseDouble(product.getParameter(
						"MaxExpired", "30"));
				double maxTopUp = Double.parseDouble(product.getParameter(
						"MaxTopUp", "3"));

				int sendNotify = IDDServiceImpl.checkSendNotify(
						vnmMessage.getIsdn(), dayBetween,
						vnmMessage.getProductId(), notEnough, maxTopUp);

				if (sendNotify == 0)
				{
					responseCode = "";
					int dayNotify = Integer.parseInt(product.getParameter(
							"DayNotify", "25"));
					if (dayBetween < dayNotify)
					{
						if (notEnough && request.getStatus() == 1)
						{
							IDDServiceImpl.notifyOverdue(vnmMessage.getIsdn(),
									-1, 0, vnmMessage.getProductId());

							responseCode = Constants.ERROR_NOTIFY_OVER_BALANCE;
							vnmMessage.setCause(responseCode);
							vnmMessage.setStatus(Constants.ORDER_STATUS_DENIED);
						}
					}
					else
					{
						IDDServiceImpl.notifyOverdue(vnmMessage.getIsdn(), 0,
								0, request.getProductId());

						responseCode = Constants.ERROR_NOTIFY_OVER_EXPIRED;
						vnmMessage.setCause(responseCode);
						vnmMessage.setStatus(Constants.ORDER_STATUS_DENIED);
					}
				}
				else if (sendNotify == -1)
				{
					vnmMessage.setCause(Constants.ACTION_UNREGISTER);
				}
				else if ((sendNotify == 1 && dayBetween == product
						.getSubscriptionPeriod())
						|| (sendNotify == 1 && notEnough))
				{
					ProductRoute orderRoute = ProductFactory.getCache().getProductRoute(vnmMessage.getRouteId());
					
					boolean isNotEnoughMoney = false;
					
					try
					{
						validateBalance(instance, orderRoute, product, vnmMessage);
					}
					catch (AppException e) {
						if (e.getMessage().equals(
								Constants.ERROR_NOT_ENOUGH_MONEY))
						{
							isNotEnoughMoney = true;
						}
						else
						{
							throw e;
						}
					}
					
					if (isNotEnoughMoney)
					{
						if (!IDDServiceImpl.checkNotifyNotEnough(request
								.getIsdn()))
						{
//								IDDServiceImpl.updateMessageNotify(
//										request.getIsdn(), 4,
//										request.getProductId(), 1);
	
							vnmMessage.setCause(Constants.ERROR_NOT_ENOUGH_MONEY);
							vnmMessage.setStatus(Constants.ORDER_STATUS_DENIED);
						}
						else
						{
							vnmMessage.setCause(Constants.ACTION_SUPPLIER_REACTIVE
											+ "." + Constants.ERROR_NOT_ENOUGH_MONEY);
							vnmMessage.setStatus(Constants.ORDER_STATUS_DENIED);
						}
					}
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, vnmMessage, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return vnmMessage;
	}

	public VNMMessage checkBalanceVB(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		VNMMessage vnmMessage = CommandUtil.createVNMMessage(request);

		SubscriberEntity subscriber = null;

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, vnmMessage);
		}
		else
		{
			try
			{
				long productId = vnmMessage.getProductId();

				ProductEntry product = ProductFactory.getCache().getProduct(
						productId);

				ProductRoute orderRoute = ProductFactory.getCache()
						.getProductRoute(vnmMessage.getRouteId());

				subscriber = getSubscriber(instance, vnmMessage);

				boolean isNotEnoughMoney = false;

				if (subscriber != null)
				{
					try
					{
						validateStatus(instance, vnmMessage, subscriber);

						validateCOS(instance, vnmMessage, subscriber);

						validateBalance(instance, orderRoute, product,
								vnmMessage);
					}
					catch (AppException e)
					{
						if (e.getMessage().equals(
								Constants.ERROR_NOT_ENOUGH_MONEY))
						{
							isNotEnoughMoney = true;
						}
						else
						{
							throw e;
						}
					}
					catch (Exception e)
					{
						throw e;
					}
				}

				if (isNotEnoughMoney)
				{
					String actionType = vnmMessage.getActionType();
					String cause = Constants.ERROR_NOT_ENOUGH_MONEY;

					vnmMessage.setActionType(actionType);

					if (request.getActionType().toLowerCase()
							.equals(Constants.ACTION_SUPPLIER_REACTIVE))
					{
						if (!IDDServiceImpl.checkNotifyNotEnough(request
								.getIsdn()))
						{
//								IDDServiceImpl.updateMessageNotify(
//										request.getIsdn(), 4,
//										request.getProductId(), 1);

							vnmMessage.setCause(cause);
						}
						else
						{
							vnmMessage
									.setCause(Constants.ACTION_SUPPLIER_REACTIVE
											+ "."
											+ Constants.ERROR_NOT_ENOUGH_MONEY);

							// clone one object for sending sms.
							CommandMessage object = vnmMessage.clone();
							object.setCause(Constants.ACTION_SUPPLIER_REACTIVE
									+ "." + Constants.ERROR_NOT_ENOUGH_MONEY);
							object.setChannel(Constants.CHANNEL_SMS);

							ResponseUtil.notifyOwner(instance, orderRoute,
									object);
						}
					}
					else
					{
						vnmMessage.setCause(cause);
					}
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, vnmMessage, e);
			}
		}

		return vnmMessage;
	}
	// 2012-09-25 MinhDT ADD_END: add for product VB600 & VB220
}
