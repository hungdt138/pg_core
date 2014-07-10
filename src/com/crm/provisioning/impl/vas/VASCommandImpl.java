package com.crm.provisioning.impl.vas;

import java.net.URL;

import org.apache.axis.AxisFault;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.sdp.portlet.activation.model.ActivationStatusSoap;
import com.sdp.portlet.activation.service.http.ActivationStatusServiceSoap;
import com.sdp.portlet.activation.service.http.ActivationStatusServiceSoapServiceLocator;
//import com.sdp.portlet.activation.service.http.ActivationStatusServiceSoap;
//import com.sdp.portlet.activation.service.http.ActivationStatusServiceSoapServiceLocator;


public class VASCommandImpl extends CommandImpl
{

	/**
	 * Get provisioning from VASGATE <br>
	 * 
	 * Author: NamTA <br>
	 * Create Date: 08/05/2012
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CommandMessage getProvisioning(
			CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		VASConnection connection = null;
		ProductRoute productRoute = null;
		try
		{
			productRoute = ProductFactory.getCache().getProductRoute(request.getRouteId());
			connection = (VASConnection) instance.getProvisioningConnection();
			connection.provisioning(request);

			String messageResponseKey = request.getActionType() + "-" + request.getCause();
			String messageResponseValue = productRoute.getParameter(messageResponseKey,
					"He thong dang ban, xin vui long nhan tin lai sau");
			if (productRoute.getChannel().equals(Constants.CHANNEL_SMS))
			{
				CommandUtil.sendSMS(instance, request, messageResponseValue);
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

		return request;
	}

	public CommandMessage getActivationStatus(CommandInstance instance, ProvisioningCommand provisioningCommand,
			CommandMessage request)
			throws Exception
	{
		VASConnection connection = null;
		try
		{
			connection = (VASConnection) instance.getProvisioningConnection();
			connection.checkAllStatus(request);
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
