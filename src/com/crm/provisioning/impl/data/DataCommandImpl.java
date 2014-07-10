/**
 * 
 */
package com.crm.provisioning.impl.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductMessage;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.impl.ccws.CCWSConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.StringUtil;
import com.fss.util.AppException;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.constructorParameterOrderType;

/**
 * @author hungdt
 * 
 */
public class DataCommandImpl extends CommandImpl
{
	private static long DEFAULT_ID = 0;
	
	public CommandMessage registerServiceByPassExisted(
			CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request) throws Exception
	{
		CommandMessage result = request;
		
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
	
				boolean includeCurrentDay = result.getParameters().getBoolean(
						"includeCurrentDay");
	
				if (result.getActionType().equals(Constants.ACTION_UPGRADE))
					includeCurrentDay = false;
	
				long sessionId = setRequest(instance, request,
						getLogRequest("com.crm.provisioning.impl.DataCommandImpl.registerServiceByPassExisted", request.getIsdn() + ", " + request.getProductId()));
				
				int subProductStatus = Constants.SUBSCRIBER_REGISTER_STATUS;
				if (result.getCampaignId() != Constants.DEFAULT_ID)
				{
					if (result.getParameters().getBoolean("FreeWithReactive"))
					{
						if (result.getParameters().getBoolean("FreeOneDay", false))
						{
							subProductStatus = Constants.SUBSCRIBER_ALERT_EXPIRE_STATUS;
						}
						else
						{
							subProductStatus = Constants.SUBSCRIBER_FREE_WITH_REACTIVE_STATUS;
						}
					}
					else
					{
						subProductStatus = Constants.SUBSCRIBER_FREE_NOT_REACTIVE_STATUS;
					}
				}
				
				SubscriberProduct subProduct = SubscriberProductImpl
						.registerProductBypassExisted(result.getUserId(),
								result.getUserName(), result.getSubscriberId(),
								result.getIsdn(), result.getSubscriberType(),
								result.getProductId(), result.getCampaignId(),
								result.getLanguageId(), includeCurrentDay, subProductStatus, getActivationDate(result));
	
				setResponse(instance, request, "success", sessionId);
				
				if (subProduct.getSubProductId() != DEFAULT_ID)
				{
					result.setSubProductId(subProduct.getSubProductId());
				}
				/**
				 * Add response value
				 */
				if (subProduct.getExpirationDate() != null)
				{
					result.setResponseValue(ResponseUtil.SERVICE_EXPIRE_DATE,
						StringUtil.format(subProduct.getExpirationDate(),
								"dd/MM/yyyy"));
				}
				
				if (result.getCampaignId() != DEFAULT_ID)
				{
					String content = result.getParameters().getProperty("FreeSMSTemp", "");
					content = content.replaceAll("~SERVICE_EXPIRE_DATE~", StringUtil.format(subProduct.getExpirationDate(),
							"dd/MM/yyyy"));
					
					Calendar chargeDate = Calendar.getInstance();
					chargeDate.setTime(subProduct.getExpirationDate());
					chargeDate.add(Calendar.DATE, 1);
					content = content.replaceAll("~SERVICE_START_DATE~", StringUtil.format(chargeDate.getTime(),
							"dd/MM/yyyy"));
					
					result.setResponseValue(ResponseUtil.SMS_TEXT,content);
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, request, error);
			}
		}
		return result;
	}
	
	public CommandMessage registerServiceWhenInvite(
			CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request) throws Exception
	{
		CommandMessage result = request;
		
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
	
				boolean includeCurrentDay = result.getParameters().getBoolean(
						"includeCurrentDay");
	
				if (result.getActionType().equals(Constants.ACTION_UPGRADE))
					includeCurrentDay = false;
	
				long sessionId = setRequest(instance, request,
						getLogRequest("com.crm.provisioning.impl.DataCommandImpl.registerServiceWhenInvite", request.getIsdn() + ", " + request.getProductId()));
				
				int subProductStatus = Constants.SUBSCRIBER_REGISTER_STATUS;
				if (result.getCampaignId() != Constants.DEFAULT_ID)
				{
					if (result.getParameters().getBoolean("FreeWithReactive"))
					{
						if (result.getParameters().getBoolean("FreeOneDay", false))
						{
							subProductStatus = Constants.SUBSCRIBER_ALERT_EXPIRE_STATUS;
						}
						else
						{
							subProductStatus = Constants.SUBSCRIBER_FREE_WITH_REACTIVE_STATUS;
						}
					}
					else
					{
						subProductStatus = Constants.SUBSCRIBER_FREE_NOT_REACTIVE_STATUS;
					}
				}
				else if (result.getActionType().equals(Constants.ACTION_INVITE))
				{
					subProductStatus = Constants.SUBSCRIBER_INVITE_STATUS;
				}
				
				SubscriberProduct subProduct = SubscriberProductImpl
						.registerProductInvite(result.getUserId(),
								result.getUserName(), result.getSubscriberId(),
								result.getIsdn(), result.getSubscriberType(),
								result.getProductId(), result.getCampaignId(),
								result.getLanguageId(), includeCurrentDay, subProductStatus, getActivationDate(result));
	
				setResponse(instance, request, "success", sessionId);
				
				if (subProduct.getSubProductId() != DEFAULT_ID)
				{
					result.setSubProductId(subProduct.getSubProductId());
				}
				/**
				 * Add response value
				 */
				if (subProduct.getExpirationDate() != null)
				{
					result.setResponseValue(ResponseUtil.SERVICE_EXPIRE_DATE,
						StringUtil.format(subProduct.getExpirationDate(),
								"dd/MM/yyyy"));
				}
				
				if (result.getCampaignId() != DEFAULT_ID)
				{
					String content = result.getParameters().getProperty("FreeSMSTemp", "");
					content = content.replaceAll("~SERVICE_EXPIRE_DATE~", StringUtil.format(subProduct.getExpirationDate(),
							"dd/MM/yyyy"));
					
					Calendar chargeDate = Calendar.getInstance();
					chargeDate.setTime(subProduct.getExpirationDate());
					chargeDate.add(Calendar.DATE, 1);
					content = content.replaceAll("~SERVICE_START_DATE~", StringUtil.format(chargeDate.getTime(),
							"dd/MM/yyyy"));
					
					result.setResponseValue(ResponseUtil.SMS_TEXT,content);
				}
				
				if (result.getActionType().equals(Constants.ACTION_INVITE))
				{
					String inviter = request.getParameters().getString("INVITER_ISDN");
					
					result.setShipTo(result.getIsdn());
					result.setResponseValue(ResponseUtil.LEADER, inviter);
					result.setResponseValue(ResponseUtil.REFERAL, result.getIsdn());
//					result.setIsdn(inviter);
				}
			}
			catch (Exception error)
			{
//				result.setIsdn(request.getParameters().getString("INVITER_ISDN"));
				processError(instance, provisioningCommand, result, error);
			}
		}
		return result;
	}

	public CommandMessage unregister(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CommandMessage result = request;

		try
		{
			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl.DataCommandImpl.unregister", request.getIsdn() + ", " + request.getProductId()));
			
			SubscriberProductImpl.unregister(result.getUserId(),
					result.getUserName(), result.getSubProductId(),
					result.getProductId());
			
			setResponse(instance, request, "success", sessionId);
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	public CommandMessage subscription(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		try
		{
			boolean includeCurrentDay = request.getParameters().getBoolean("includeCurrentDay");
			
			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl.DataCommandImpl.subscription", request.getIsdn() + ", " + request.getProductId()));
			
			SubscriberProduct subProduct = SubscriberProductImpl.subscription(request.getUserId(),
					request.getUserName(), request.getSubProductId(),
					request.isFullOfCharge(), request.getQuantity(), includeCurrentDay);
			
			setResponse(instance, request, "success", sessionId);
			
			ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());
			ProductMessage productMessage = product.getProductMessage(request.getActionType(), 
					request.getCampaignId(), request.getLanguageId(), request.getChannel(), Constants.SUCCESS);
			if (productMessage != null)
			{
				String content = productMessage.getContent();
				if (subProduct.getExpirationDate() != null)
				{
					content = content.replaceAll("~SERVICE_EXPIRE_DATE~",
							StringUtil.format(subProduct.getExpirationDate(),
									"dd/MM/yyyy"));

					SubscriberEntity subscriberEntity = ((VNMMessage) request).getSubscriberEntity();
					BalanceEntity balance = CCWSConnection.getBalance(subscriberEntity, "GPRS");
					
					double convertRatio = Double.parseDouble(product.getParameter("ConvertRatio", "0.00000095367431640625"));
					double amount = product.getParameters().getDouble(
							"balance." + request.getActionType() + ".GPRS.amount");
					content = content.replaceAll("~SERVICE_BALANCE~", StringUtil.format(
							(balance.getAvailableBalance() + amount) * convertRatio, "#,##0"));
				}
				SubscriberProductImpl.insertSendSMS(product.getParameter("ProductShotCode", ""), request.getIsdn(), content);
			}
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return request;
	}

	public CommandMessage registerFlexi(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CommandMessage result = request;
		try
		{
			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl.DataCommandImpl.registerFlexi", request.getIsdn() + ", " + request.getProductId()));
			
			SubscriberProductImpl.registerFlexi(result.getIsdn(),
					result.getProductId(), result.getSubProductId());
			
			setResponse(instance, request, "success", sessionId);
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}
}
