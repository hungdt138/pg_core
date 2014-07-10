package com.crm.provisioning.impl.vas;

import java.net.URL;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;
import com.fss.util.AppException;
import com.sdp.portlet.activation.model.ActivationStatusSoap;
import com.sdp.portlet.activation.service.http.ActivationStatusServiceSoap;
import com.sdp.portlet.activation.service.http.ActivationStatusServiceSoapServiceLocator;

public class VASConnection extends ProvisioningConnection
{
	private static String				portlet		= "SDP-ext";
	private static String				serviceName	= "Portlet_Activation_ActivationStatusService";
	private ActivationStatusServiceSoap	serviceSoap	= null;

	public VASConnection()
	{
		super();
	}

	private URL getURL(String host, String port, String user, String password)
			throws Exception
	{
		// Unathenticated url

		String url = "http://" + host + "/" + portlet + "/axis/" + serviceName;

		// Authenticated url

		if (true)
		{
			url = "http://" + user + ":" + password
					+ "@" + host + ":" + port + "/" + portlet + "/secure/axis/" + serviceName;
		}

		return new URL(url);
	}

	@Override
	public boolean openConnection() throws Exception
	{
		ActivationStatusServiceSoapServiceLocator serviceLocator = new ActivationStatusServiceSoapServiceLocator();
		URL url = getURL(getHost(), getPort() + "", getUserName(), getPassword());

		serviceSoap = serviceLocator.getPortlet_Activation_ActivationStatusService(url);

		return super.openConnection();
	}

	public CommandMessage provisioning(CommandMessage request)
			throws Exception
	{
		try
		{
			String keyword = StringUtil.nvl(request.getKeyword(), "");
			String[] argParams = StringUtil.toStringArray(keyword, " ");
			if (argParams.length < 2)
			{
				throw new AppException(Constants.ERROR_INVALID_SYNTAX);
			}
			String productName = StringUtil.nvl(argParams[1], "");
			ProductEntry entry = ProductFactory.getCache().getProduct(request.getProductId());
			String sku = entry.getParameter(productName, "");
			if (sku.equals(""))
			{
				throw new AppException(Constants.ERROR_INVALID_SYNTAX);
			}
			String sourceAddress = StringUtil.nvl(request.getIsdn(), "");
			int commandId = 1;
			if (request.getActionType().equals(Constants.ACTION_REGISTER))
			{
				commandId = 1;
			}
			else if (request.getActionType().equals(Constants.ACTION_UNREGISTER))
			{
				commandId = 3;
			}
			ActivationStatusSoap res =
					serviceSoap.provisioning(sourceAddress, sku, commandId);
			request.setCause(res.getResponseCode());
		}
		catch (Exception e)
		{
			throw e;
		}
		return request;
	}

	public CommandMessage checkAllStatus(CommandMessage request) throws Exception
	{
		try
		{
			String isdn = request.getIsdn();

			ActivationStatusSoap[] activationStatusSoaps = serviceSoap.checkAllStatus(isdn);

			if (activationStatusSoaps == null)
			{

			}
			else
			{
				// SupplierStatus
				// 1 - active
				// 3 - deactive
				// 2 - suspend
				// vas name = productId
				// vas id = sku
				String vasList = "";
				for (ActivationStatusSoap activationStatusSoap : activationStatusSoaps)
				{
					String vasName = activationStatusSoap.getProductId();
					vasList += vasName + StringPool.COMMA;
					request.setResponseValue(ResponseUtil.VAS + "." + vasName + ".id", activationStatusSoap.getSku());
					request.setResponseValue(ResponseUtil.VAS + "." + vasName + ".status",
							activationStatusSoap.getSupplierStatus());
					request.setResponseValue(ResponseUtil.VAS + "." + vasName + ".description", "");
				}

				request.setResponseValue(ResponseUtil.VAS, vasList);
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		return request;
	}
}
