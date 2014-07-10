package com.crm.provisioning.impl.charging;

import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.ResponseUtil;

public class SubModifyCommandImpl extends CommandImpl
{
	public CommandMessage requestSubModify(
			CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		SubModifyConnection connection = null;
		try
		{
			connection = (SubModifyConnection) instance.getProvisioningConnection();
			String seq = request.getRequestValue(ResponseUtil.SESSION_ID, "");
			String req = request.getRequestValue(ResponseUtil.VALUE, "");

			String response = connection.request(seq, req);

			request.setResponseValue(ResponseUtil.VALUE, response);
		}
		catch (Exception e)
		{
			processError(instance, provisioningCommand, request, e);
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}
		return request;
	}

	public CommandMessage requestExtDebit(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		ExtDebitConnection connection = null;
		try
		{
			connection = (ExtDebitConnection) instance.getProvisioningConnection();

			String req = request.getRequestValue(ResponseUtil.VALUE, "");

			String response = connection.debit(req);

			request.setResponseValue(ResponseUtil.VALUE, response);
		}
		catch (Exception e)
		{
			processError(instance, provisioningCommand, request, e);
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}
		return request;
	}
}
