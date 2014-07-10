package com.crm.provisioning.impl.vncs;

import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.provisioning.util.ResponseUtil;

public class VNCSCommandImpl extends CommandImpl
{
	private static String ACTION_REGISTER = "register";
	private static String ACTION_UNREGISTER = "unregister";
	private static String ACTION_SUSPEND = "suspend";
	private static String ACTION_CHANGE_HAND_SET = "changehandset";
	private static String ACTION_RENEW = "renew";
	private static String ACTION_REACTIVE = "reactive";
	private static String RESPONSE_SUCCESS = "0";
	
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
			VNCSConnection connection = null;
			try
			{
				connection = (VNCSConnection) instance.getProvisioningConnection();
				
				int sessionId = setRequestLog(instance, request, "REGISTER(" + request.getIsdn() + ")");
				String response = connection.sendGetRequest(ACTION_REGISTER, request.getIsdn());
				String[] responseDetail = response.split(";");
				
				if (responseDetail[0].equals(RESPONSE_SUCCESS))
				{
					setResponse(instance, request, "VNCS." + responseDetail[0], responseDetail[1] + ";" + responseDetail[2], sessionId);
					request.setCause(Constants.SUCCESS);
					request.setResponseValue(ResponseUtil.SERIAL, responseDetail[1]);
					request.setResponseValue(ResponseUtil.SMS_HREF, responseDetail[2]);
				}
				else
				{
					setResponse(instance, request, "VNCS." + responseDetail[0], sessionId);
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
			CommandMessage request) throws Exception
	{
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			VNCSConnection connection = null;
			
			try
			{
				connection = (VNCSConnection) instance.getProvisioningConnection();
				
				int sessionId = setRequestLog(instance, request, "UNREGISTER(" + request.getIsdn() + ")");
				String response = connection.sendGetRequest(ACTION_UNREGISTER, request.getIsdn());
				String[] responseDetail = response.split(";");
				setResponse(instance, request, "VNCS." + responseDetail[0], sessionId);
	
				if (responseDetail[0].equals(RESPONSE_SUCCESS))
				{
					request.setCause(Constants.SUCCESS);
				}
				else
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch(Exception e)
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

	public CommandMessage subscription(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request) throws Exception
	{
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			VNCSConnection connection = null;
			
			try
			{
				connection = (VNCSConnection) instance.getProvisioningConnection();
				
				int sessionId = setRequestLog(instance, request, "RENEW(" + request.getIsdn() + ")");
				String response = connection.sendGetRequest(ACTION_RENEW, request.getIsdn());
				String[] responseDetail = response.split(";");
				setResponse(instance, request, "VNCS." + responseDetail[0], sessionId);
	
				if (responseDetail[0].equals(RESPONSE_SUCCESS))
				{
					request.setCause(Constants.SUCCESS);
				}
				else
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch(Exception e)
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
			CommandMessage request) throws Exception
	{
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			VNCSConnection connection = null;
			
			try
			{
				connection = (VNCSConnection) instance.getProvisioningConnection();
				
				int sessionId = setRequestLog(instance, request, "SUSPEND(" + request.getIsdn() + ")");
				String response = connection.sendGetRequest(ACTION_SUSPEND, request.getIsdn());
				String[] responseDetail = response.split(";");
				setResponse(instance, request, "VNCS." + responseDetail[0], sessionId);
	
				if (responseDetail[0].equals(RESPONSE_SUCCESS))
				{
					request.setCause(Constants.SUCCESS);
				}
				else
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch(Exception e)
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
			CommandMessage request) throws Exception
	{
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			VNCSConnection connection = null;
			
			try
			{
				connection = (VNCSConnection) instance.getProvisioningConnection();
				
				int sessionId = setRequestLog(instance, request, "REACTIVE(" + request.getIsdn() + ")");
				String response = connection.sendGetRequest(ACTION_REACTIVE, request.getIsdn());
				String[] responseDetail = response.split(";");
				setResponse(instance, request, "VNCS." + responseDetail[0], sessionId);
	
				if (responseDetail[0].equals(RESPONSE_SUCCESS))
				{
					request.setCause(Constants.SUCCESS);
				}
				else
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch(Exception e)
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
	
	public CommandMessage changeHandSet(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			VNCSConnection connection = null;
			try
			{
				connection = (VNCSConnection) instance.getProvisioningConnection();
				
				int sessionId = setRequestLog(instance, request, "CHANGEHANDSET(" + request.getIsdn() + ")");
				String response = connection.sendGetRequest(ACTION_CHANGE_HAND_SET, request.getIsdn());
				String[] responseDetail = response.split(";");

				if (responseDetail[0].equals(RESPONSE_SUCCESS))
				{
					setResponse(instance, request, "VNCS." + responseDetail[0], responseDetail[1] + ";" + responseDetail[2], sessionId);
					request.setCause(Constants.SUCCESS);
					request.setResponseValue(ResponseUtil.SERIAL, responseDetail[1]);
					request.setResponseValue(ResponseUtil.SMS_HREF, responseDetail[2]);
				}
				else
				{
					setResponse(instance, request, "VNCS." + responseDetail[0], sessionId);
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
	
	public void setResponse(CommandInstance instance, CommandMessage request,
			String responseString, String responseDetail, long sessionId) throws Exception
	{
		request.setResponseTime(new Date());

		long costTime = CommandUtil.calculateCostTime(
				request.getRequestTime(), request.getResponseTime());

		request.setDescription(responseString);
		
		responseString = "ID=" + sessionId + ": " + responseString + ";" + responseDetail
				+ ": costTime=" + costTime;
		instance.logMonitor("RECEIVE: " + responseString);
		request.setResponse(responseString);
	}
}
