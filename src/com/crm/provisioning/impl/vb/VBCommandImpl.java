package com.crm.provisioning.impl.vb;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductMessage;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.IDDServiceImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.StringUtil;

public class VBCommandImpl extends CommandImpl
{
	public CommandMessage confirmRegister(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		try
		{
			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl.vb.VBCommandImpl.confirmRegister", request.getIsdn() + ", " + request.getProductId()));
			
			IDDServiceImpl.confirmRegister(request.getUserId(), request.getUserName(), request.getSubscriberId()
					, request.getIsdn(), request.getSubscriberType(), request.getProductId(), request.getLanguageId(),
					Constants.SUBSCRIBER_PENDING_STATUS, getActivationDate(request));

			setResponse(instance, request, "success", sessionId);
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return request;
	}
	
	public CommandMessage cancelConfirmRegister(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		try
		{
			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl..vb.VBCommandImpl.cancelConfirmRegister", request.getIsdn() + ", " + request.getProductId()));
			
			IDDServiceImpl.removeConfirm(request.getSubProductId());
			
			setResponse(instance, request, "success", sessionId);
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return request;
	}

	public CommandMessage registerIDD(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request) throws Exception
	{
		try
		{
			boolean includeCurrentDay = request.getParameters().getBoolean(
					"includeCurrentDay");

//			if (request.getActionType().equals(Constants.ACTION_REGISTER)
//					|| request.getActionType().equals(Constants.ACTION_UPGRADE))
//				includeCurrentDay = false;

			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl.vb.VBCommandImpl.registerIDD", request.getIsdn() + ", " + request.getProductId()));
			
			SubscriberProduct subProduct = null;
			
			if (!request.getParameters()
					.getProperty("PropertiesRenew", "false").equals("true")
					&& request.getChannel().equals(Constants.CHANNEL_SMS))
			{
				subProduct = IDDServiceImpl.registerIDD(
						request.getUserId(), request.getUserName(),
						request.getSubscriberId(), request.getIsdn(),
						request.getSubscriberType(), request.getProductId(),
						request.getCampaignId(), request.getLanguageId(),
						includeCurrentDay, true, Constants.SUBSCRIBER_PENDING_STATUS,
						getActivationDate(request), request.getSubProductId());
			}
			else
			{
				subProduct = SubscriberProductImpl.register(
						request.getUserId(), request.getUserName(),
						request.getSubscriberId(), request.getIsdn(),
						request.getSubscriberType(), request.getProductId(),
						request.getCampaignId(), request.getLanguageId(),
						includeCurrentDay, true, Constants.SUBSCRIBER_PENDING_STATUS,
						getActivationDate(request));
			}

			setResponse(instance, request, "success", sessionId);
			
			if (request.getParameters()
							.getProperty("PropertiesRenew", "false")
							.equals("true"))
			{
				IDDServiceImpl.updateProperties(request.getIsdn(),
						request.getProductId(), "renew");
//				IDDServiceImpl.removeExtendIDDBuffet(request.getIsdn(),
//						request.getProductId());

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
				Calendar cal = Calendar.getInstance();
				cal.add(cal.HOUR_OF_DAY, 14);

				request.setCause(Constants.VB_RENEW_SUCCESS);
				request.setResponseValue(ResponseUtil.SERVICE_START_DATE, sdf1.format(cal
						.getTime()));
				request.setResponseValue(ResponseUtil.SERVICE_ACTIVE_DATE,
						sdf.format(cal.getTime()));
			}
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return request;
	}
	
	public CommandMessage registerByPassComfirmIDD(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		try
		{
			boolean includeCurrentDay = request.getParameters().getBoolean(
					"includeCurrentDay");

//			if (request.getActionType().equals(Constants.ACTION_REGISTER)
//					|| request.getActionType().equals(Constants.ACTION_UPGRADE))
//				includeCurrentDay = false;

			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl.vb.VBCommandImpl.registerIDD", request.getIsdn() + ", " + request.getProductId()));
			
			SubscriberProduct subProduct = null;
			
			subProduct = SubscriberProductImpl.register(
					request.getUserId(), request.getUserName(),
					request.getSubscriberId(), request.getIsdn(),
					request.getSubscriberType(), request.getProductId(),
					request.getCampaignId(), request.getLanguageId(),
					includeCurrentDay, true, Constants.SUBSCRIBER_PENDING_STATUS,
					getActivationDate(request));

			setResponse(instance, request, "success", sessionId);
			
			if (request.getParameters()
							.getProperty("PropertiesRenew", "false")
							.equals("true"))
			{
				IDDServiceImpl.updateProperties(request.getIsdn(),
						request.getProductId(), "renew");

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
				Calendar cal = Calendar.getInstance();
				cal.add(cal.HOUR_OF_DAY, 14);

				request.setCause(Constants.VB_RENEW_SUCCESS);
				request.setResponseValue(ResponseUtil.SERVICE_START_DATE, 
						sdf1.format(cal.getTime()));
				request.setResponseValue(ResponseUtil.SERVICE_ACTIVE_DATE,
						sdf.format(cal.getTime()));
			}
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return request;
	}

	public CommandMessage unregisterIDD(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		try
		{
			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl.vb.VBCommandImpl.unregisterIDD", request.getIsdn() + ", " + request.getProductId()));
			
			SubscriberProductImpl.unregister(request.getUserId(),
					request.getUserName(), request.getSubProductId(),
					request.getProductId());
			
			setResponse(instance, request, "success", sessionId);
			
			if (request.getChannel().equals(Constants.CHANNEL_CORE))
			{
				ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());
				ProductMessage productMessage = product.getProductMessage(request.getActionType(), 
						request.getCampaignId(), request.getLanguageId(), request.getChannel(), Constants.SUCCESS);
				if (productMessage != null)
				{
					String content = productMessage.getContent();
					SubscriberProductImpl.insertSendSMS(product.getParameter("ProductShotCode", ""), request.getIsdn(), content);
				}
			}
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return request;
	}
	
	public CommandMessage subscriptionIDD(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CommandMessage result = request;

		try
		{
			boolean includeCurrentDay = result.getParameters().getBoolean(
					"includeCurrentDay");
			
			long sessionId = setRequest(instance, result,
					getLogRequest("com.crm.provisioning.impl.vb.VBCommandImpl.subscriptionIDD", result.getIsdn() + ", " + request.getProductId()));
			
			SubscriberProduct subProduct = IDDServiceImpl.subscription(result.getUserId(),
					result.getUserName(), result.getSubProductId(),
					result.isFullOfCharge(), result.getQuantity(), includeCurrentDay);
			
			setResponse(instance, result, "success", sessionId);
			
			IDDServiceImpl.updateProperties(result.getIsdn(),
					result.getProductId(), "renew");
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
			Calendar cal = Calendar.getInstance();
			cal.add(cal.HOUR_OF_DAY, 14);
			
			result.setResponseValue(ResponseUtil.SERVICE_START_DATE, sdf1.format(cal
					.getTime()));
			result.setResponseValue(ResponseUtil.SERVICE_ACTIVE_DATE,
					sdf.format(cal.getTime()));

			if (!result.getChannel().equals(Constants.CHANNEL_SMS))
			{
				ProductEntry product = ProductFactory.getCache().getProduct(result.getProductId());
				ProductMessage productMessage = product.getProductMessage(result.getActionType(), 
						result.getCampaignId(), result.getLanguageId(), result.getChannel(), Constants.SUCCESS);
				if (productMessage != null)
				{
					String content = productMessage.getContent();
					if (subProduct.getExpirationDate() != null)
					{
						content = content.replaceAll("~SERVICE_START_DATE~", sdf1.format(cal.getTime()));
						content = content.replaceAll("~SERVICE_ACTIVE_DATE~", sdf.format(cal.getTime()));
					}
					SubscriberProductImpl.insertSendSMS(product.getParameter("ProductShotCode", ""), result.getIsdn(), content);
				}
			}
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, result, error);
		}

		return result;
	}
	
	public CommandMessage barringIDD(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CommandMessage result = request;

		try
		{
			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl.vb.VBCommandImpl.barringIDD", request.getIsdn() + ", " + request.getProductId()));
			
			IDDServiceImpl.barringVBService(result.getUserId(),
					result.getUserName(), result.getSubProductId());
			
			setResponse(instance, request, "success", sessionId);
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}
}
