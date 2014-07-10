package com.crm.provisioning.impl.vim;

import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.util.GeneratorSeq;

public class VIMCommandImpl extends CommandImpl
{
	private static final String ERROR_SUB_NOT_EXISTS = "ERR_SUB_NOT_EXISTS";
	public CommandMessage registerService(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		// int result = UNKNOW_RESPONSE;
		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			try
			{
				int subscriberType;
				if (request.isPostpaid())
				{
					subscriberType = 501;
				}
				else
				{
					subscriberType = 500;
				}
	
				ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());
				int packageType = Integer.parseInt(product.getParameter("PackageTypeCharge", "101"));
				if (request.getCampaignId() != Constants.DEFAULT_ID)
				{
					packageType = Integer.parseInt(product.getParameter("PackageTypeFree", "110"));;
				}
	
				VIMConnection connection = null;
				try
				{
					connection = (VIMConnection) instance.getProvisioningConnection();
					int sessionId = setRequestLog(instance, request, "REGISTER(" + request.getIsdn() + ", " + packageType + ")");
					responseCode = connection.register(request, subscriberType,
							packageType, sessionId);
					setResponse(instance, request, "VIM." + responseCode, sessionId);
				}
				finally
				{
					instance.closeProvisioningConnection(connection);
				}
	
				if (responseCode.equals(provisioningCommand.getParameter("expectedResult", "")))
				{
					request.setCause(Constants.SUCCESS);
				}
				else
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
		}
		
		return request;
	}

	public CommandMessage unregisterService(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			try
			{
				VIMConnection connection = null;
				try
				{
					connection = (VIMConnection) instance.getProvisioningConnection();
					int sessionId = setRequestLog(instance, request, "UNREGISTER(" + request.getIsdn() + ")");
					responseCode = connection.unregister(request, sessionId);
	
					setResponse(instance, request, "VIM." + responseCode, sessionId);
				}
				finally
				{
					instance.closeProvisioningConnection(connection);
				}
	
				if (responseCode.contains(ERROR_SUB_NOT_EXISTS))
				{
					request.setCause(ERROR_SUB_NOT_EXISTS);
				}
				else
				{
					if (responseCode.equals(provisioningCommand.getParameter("expectedResult", "")))
					{
						request.setCause(Constants.SUCCESS);
					}
					else
					{
						request.setCause(Constants.ERROR);
					}
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
		}

		return request;
	}

	public CommandMessage reactiveService(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			try
			{
				VIMConnection connection = null;
				try
				{
					connection = (VIMConnection) instance.getProvisioningConnection();
					int sessionId = setRequestLog(instance, request, "REACTIVE(" + request.getIsdn() + ")");
					responseCode = connection.reactive(request, sessionId);
					
					if (responseCode.contains(ERROR_SUB_NOT_EXISTS))
					{
						int subscriberType;
						if (request.isPostpaid())
						{
							subscriberType = 501;
						}
						else
						{
							subscriberType = 500;
						}
						
						responseCode = connection.register(request, subscriberType, 101, GeneratorSeq.getNextSeq());
					}
	
					setResponse(instance, request, "VIM." + responseCode, sessionId);
				}
				finally
				{
					instance.closeProvisioningConnection(connection);
				}
	
				if (responseCode.equals(provisioningCommand.getParameter("expectedResult", "")))
				{
					request.setCause(Constants.SUCCESS);
				}
				else
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
		}

		return request;
	}

	public CommandMessage deactiveService(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			try
			{
				VIMConnection connection = null;
				try
				{
					connection = (VIMConnection) instance.getProvisioningConnection();
					int sessionId = setRequestLog(instance, request, "DEACTIVE(" + request.getIsdn() + ")");
					responseCode = connection.renewal(request, 600, sessionId);
					setResponse(instance, request, "VIM." + responseCode, sessionId);
				}
				finally
				{
					instance.closeProvisioningConnection(connection);
				}
				
				if (responseCode.contains(ERROR_SUB_NOT_EXISTS))
				{
					request.setCause(ERROR_SUB_NOT_EXISTS);
				}
				else
				{
					if (responseCode.equals(provisioningCommand.getParameter("expectedResult", "")))
					{
						request.setCause(Constants.SUCCESS);
					}
					else
					{
						request.setCause(Constants.ERROR);
					}
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
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
