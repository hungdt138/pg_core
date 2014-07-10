package com.crm.provisioning.impl.epos;

import java.util.Date;

import org.apache.log4j.Logger;

import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.crm.kernel.message.Constants;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;

public class EposCommandImpl extends CommandImpl {

	Logger logger = Logger.getLogger(EposCommandImpl.class);
	
	public VNMMessage changeCOS(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request) throws Exception
	{
		VNMMessage result = CommandUtil.createVNMMessage(request);
		EposConnection connection = null;
		
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			try
			{
				String strPackageType = provisioningCommand.getParameter("PackageType", "");
				String isdn = request.getIsdn();
				if (isdn.startsWith(Constants.COUNTRY_CODE))
				{
					isdn = Constants.DOMESTIC_CODE + isdn.substring(Constants.COUNTRY_CODE.length());
				}
				String strOption = provisioningCommand.getParameter("Option", "");
				connection = (EposConnection) instance.getProvisioningConnection();
				
				String response = "";
				
				int sessionId;
				if (request.isPostpaid())
				{
					sessionId = setRequestLog(instance, result, "changeCOS4POS(" + result.getIsdn() + ", " + strOption + ", " + strPackageType + ")");
					response = connection.changeCOS(isdn, strOption, strPackageType, true);
				}
				else
				{
					sessionId = setRequestLog(instance, result, "changeCos4Pre(" + result.getIsdn() + ", " + strOption + ", " + strPackageType + ")");
					response = connection.changeCOS(isdn, strOption, strPackageType, false);
				}
				setResponse(instance, result, "EPOS." + response, sessionId);
				
				if (response.equals(Constants.SUCCESS) || 
					response.equals(Constants.EPOS_COS_CHANGED) || 
					response.equals(Constants.EPOS_COS_CANCELED))
				{
					result.setCause(Constants.SUCCESS);
				}
				else
				{
					result.setCause(Constants.ERROR);
				}
			}
			catch(Exception ex)
			{
				processError(instance, provisioningCommand, request, ex);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		
		return result;
	}
	
	public VNMMessage convertDataSim(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request) throws Exception
	{
		VNMMessage result = CommandUtil.createVNMMessage(request);
		EposConnection connection = null;
		
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			try
			{
				String isdn = request.getIsdn();
				if (isdn.startsWith(Constants.COUNTRY_CODE))
				{
					isdn = Constants.DOMESTIC_CODE + isdn.substring(Constants.COUNTRY_CODE.length());
				}
				String strOption = provisioningCommand.getParameter("Option", "");
				
				SubscriberEntity subscriberEntity = result.getSubscriberEntity();
				String strCOS = subscriberEntity.getCOSName();
				result.getParameters().setProperty("SubscriberCOS", strCOS);
				
				connection = (EposConnection) instance.getProvisioningConnection();
				
				String response = "";
				
				int sessionId;
				sessionId = setRequestLog(instance, result, "changeCos4PreCustome(" + result.getIsdn() + ", " + strOption + ")");
				response = connection.convertToDataSim(isdn, strOption);
				setResponse(instance, result, "EPOS." + response, sessionId);
				
				if (response.equals(Constants.SUCCESS))
				{
					result.setCause(Constants.SUCCESS);
				}
				else
				{
					result.setCause(Constants.ERROR);
				}
			}
			catch(Exception ex)
			{
				processError(instance, provisioningCommand, request, ex);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		
		return result;
	}
	
	public VNMMessage cancelDataSim(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request) throws Exception
	{
		VNMMessage result = CommandUtil.createVNMMessage(request);
		EposConnection connection = null;
		
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			try
			{
				String isdn = request.getIsdn();
				if (isdn.startsWith(Constants.COUNTRY_CODE))
				{
					isdn = Constants.DOMESTIC_CODE + isdn.substring(Constants.COUNTRY_CODE.length());
				}
				
				String strCOS = result.getParameters().getProperty("SubscriberCOS");
				
				connection = (EposConnection) instance.getProvisioningConnection();
				
				String response = "";
				
				int sessionId;
				sessionId = setRequestLog(instance, result, "changeCos4PreCustome(" + result.getIsdn() + ", " + strCOS + ")");
				response = connection.convertToDataSim(isdn, strCOS);
				setResponse(instance, result, "EPOS." + response, sessionId);
				
				if (response.equals(Constants.SUCCESS))
				{
					result.setCause(Constants.SUCCESS);
				}
				else
				{
					result.setCause(Constants.ERROR);
				}
			}
			catch(Exception ex)
			{
				processError(instance, provisioningCommand, request, ex);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}
		
		return result;
	}
	
	public int setRequestLog(CommandInstance instance, CommandMessage request,
			String requestString) throws Exception
	{

		request.setRequestTime(new Date());
		long sessionId = setRequest(instance, request, requestString);
		if (sessionId > (long) Integer.MAX_VALUE)
			return (int) (sessionId % (long) Integer.MAX_VALUE);
		else
			return (int) sessionId;
	}
}
