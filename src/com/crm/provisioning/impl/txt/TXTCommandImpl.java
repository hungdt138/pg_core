package com.crm.provisioning.impl.txt;

import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;

public class TXTCommandImpl extends CommandImpl
{
	private static String ACTIVE_STATUS_CODE = "ACTIVE";
	private static String SUSPENDED_STATUS_CODE = "SUSPENDED";
	private static String CHARGE_NORMAL = "NORMAL";
	private static String CHARGE_PARTIAL = "PARTIAL";
	
	public CommandMessage register(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			TXTConnection connection = null;
			try
			{
				int subscriberType;
				if (request.isPostpaid())
				{
					subscriberType = 2;
				}
				else
				{
					subscriberType = 1;
				}
				
				ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());
				int packageType = Integer.parseInt(product.getParameter("PackageTypeCharge", "1"));
				if (request.getCampaignId() != Constants.DEFAULT_ID)
				{
					packageType = Integer.parseInt(product.getParameter("PackageTypeFree", "2"));;
				}

				connection = (TXTConnection) instance.getProvisioningConnection();
				int sessionId = setRequestLog(instance, request, "REGISTER(" + request.getIsdn() + ", " + packageType + ")");
				int responseCode = connection.register(request, subscriberType, sessionId, packageType);
				setResponse(instance, request, "TXT." + responseCode, sessionId);
				
				if (responseCode != 0)
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		
		return request;
	}
	
	public CommandMessage unregister(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			TXTConnection connection = null;
			try
			{
				connection = (TXTConnection) instance.getProvisioningConnection();
				int sessionId = setRequestLog(instance, request, "UNREGISTER(" + request.getIsdn() + ")");
				int responseCode = connection.unregister(request, sessionId);
				setResponse(instance, request, "TXT." + responseCode, sessionId);
				
				if (responseCode != 0)
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return request;
	}
	
	public CommandMessage reactive(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			TXTConnection connection = null;
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());
				
				String chargeType = "";
				int renewalDays = -1;
				if (request.getAmount() == product.getPrice())
				{
					chargeType = CHARGE_NORMAL;
					renewalDays = 0;
				}
				else
				{
					chargeType = CHARGE_PARTIAL;
					renewalDays = request.getQuantity();
				}
				connection = (TXTConnection) instance.getProvisioningConnection();
				
				int sessionId = setRequestLog(instance, request, "REACTIVE(" + request.getIsdn() + ", " + chargeType + ", " + renewalDays + ")");
				int responseCode = connection.renewal(request, sessionId, ACTIVE_STATUS_CODE, chargeType, renewalDays);
				setResponse(instance, request, "TXT." + responseCode, sessionId);
					
				if (responseCode != 0)
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return request;
	}
	
	public CommandMessage deactive(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			TXTConnection connection = null;
			try
			{
				connection = (TXTConnection) instance.getProvisioningConnection();
				int sessionId = setRequestLog(instance, request, "DEACTIVE(" + request.getIsdn() + ")");
				int responseCode = connection.renewal(request, sessionId, SUSPENDED_STATUS_CODE, "", 0);
				setResponse(instance, request, "TXT." + responseCode, sessionId);
				
				if (responseCode != 0)
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return request;
	}
	
	public int setRequestLog(CommandInstance instance, CommandMessage request, String requestString) throws Exception
	{
		request.setRequestTime(new Date());
		long sessionId = setRequest(instance, request, requestString);
		if (sessionId > (long)Integer.MAX_VALUE)
			return (int)(sessionId % (long)Integer.MAX_VALUE);
		else
			return (int) sessionId;
	}
}
