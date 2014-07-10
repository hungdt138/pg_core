package com.crm.provisioning.impl;

import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.subscriber.impl.GSMServiceImpl;

public class GSMCommandImpl extends CommandImpl
{
	public CommandMessage register(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CommandMessage result = request;

		try
		{
			String[] params = result.getParameters().getString("sms.params[0]").split("||");
			String reportDate = params[0];
			long mcc = Long.parseLong(params[1]);
			long mnc = Long.parseLong(params[2]);
			String type = params[3];
			long lac = Long.parseLong(params[4]);
			long cellId = Long.parseLong(params[5]);
			long rnc = Long.parseLong(params[6]);
			long psc = Long.parseLong(params[7]);
			String signal = params[8];
			String latitude = params[9];
			String longitude = params[10];
			int voiceStatus = Integer.parseInt(params[11]);
			int smsStatus = Integer.parseInt(params[12]);
			int speedStatus = Integer.parseInt(params[13]);
			
			long sessionId = setRequest(instance, request,
					getLogRequest("com.crm.provisioning.impl.GSMCommandImpl.report", request.getIsdn()));

			GSMServiceImpl.report(result.getUserId(), result.getUserName(), result.getIsdn(), new Date(),
					mcc, mnc, type, lac, cellId, rnc, psc, signal,
					latitude, longitude, voiceStatus, smsStatus,
					speedStatus, "", Constants.SERVICE_STATUS_APPROVED);
					
			
			setResponse(instance, request, "success", sessionId);
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}
}
