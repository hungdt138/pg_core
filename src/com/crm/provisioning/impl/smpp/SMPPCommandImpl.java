package com.crm.provisioning.impl.smpp;

import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.ResponseUtil;

public class SMPPCommandImpl extends CommandImpl
{

	public void sendSMS(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		SMPPConnection connection = null;
		try
		{
			setParameter(request);
			
			connection = (SMPPConnection) instance.getProvisioningConnection();

			connection.submit(request);
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

	public void sendTextSMS(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		SMPPConnection connection = null;
		try
		{
			setParameter(request);
			
			connection = (SMPPConnection) instance.getProvisioningConnection();

			connection.submitTextMessage(request);
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

	public void sendWSPSMS(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		SMPPConnection connection = null;
		try
		{
			setParameter(request);
			
			connection = (SMPPConnection) instance.getProvisioningConnection();

			connection.submitWSPMessage(request);
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
	
	private void setParameter(CommandMessage request) throws Exception
	{
		ProductRoute productRoute = ProductFactory.getCache().getProductRoute(request.getRouteId());
		if (productRoute != null)
		{
			String text = productRoute.getParameter("text", "");
			String href = productRoute.getParameter("href", "");
			
			request.setResponseValue(ResponseUtil.SMS_HREF, href);
			request.setResponseValue(ResponseUtil.SMS_TEXT, text);
			request.setRequest(text);
		}
	}
}
