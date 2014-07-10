package com.crm.provisioning.impl.vim;

import java.net.URL;

import javax.xml.namespace.QName;

import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.message.CommandMessage;
import com.unified.provisioning.ws.ProvisioningService;
import com.unified.provisioning.ws.ProvisioningServiceImplService;
import com.unified.provisioning.ws.RequestHeader;
import com.unified.provisioning.ws.Response;

public class VIMConnection extends ProvisioningConnection
{
	private static final QName SERVICE_NAME = new QName("http://ws.provisioning.unified.com/", "ProvisioningServiceImplService");
	
	public final static String	SUCCESS		= "success";
	public final static String	FAILURE		= "fail";

	private ProvisioningService	service;
	private Response			response;
	
	public VIMConnection()
	{
		setHost("192.168.194.4");
		setPort(3300);
		setTimeout(15000);
	}

	public String getProductAlias(long productId) throws Exception
	{
		ProductEntry product = ProductFactory.getCache().getProduct(productId);
		return product.getAlias();
	}

	private URL getURL(String host, int port) throws Exception
	{
		String strUrl = "http://" + host + ":" + port + "/soap_prov/ProvisioningService";

		URL url = new URL(strUrl);
		return url;
	}

	@Override
	public boolean openConnection() throws Exception
	{
		URL url = getURL(getHost(), getPort());

		service = (new ProvisioningServiceImplService(url, SERVICE_NAME)).getProvisioningServiceImplPort();
		return super.openConnection();
	}

	public String register(CommandMessage request, int subscriberType, int packageType, int sessionid) throws Exception
	{
		String responseCode = "";
		try
		{
			RequestHeader header = new RequestHeader();
			header.setAccessMedia(request.getChannel());
			header.setExternalId("" + sessionid);
			header.setMsisdn(request.getIsdn());

			response = service.subscribe(header, "", subscriberType, packageType, getProductAlias(request.getProductId()));
			responseCode = response.getResponseName();
		}
		catch (Exception e)
		{
			throw e;
		}
		return responseCode;
	}

	public String unregister(CommandMessage request, int sessionid) throws Exception
	{
		RequestHeader header = new RequestHeader();
		header.setAccessMedia(request.getChannel());
		header.setExternalId("" + sessionid);
		header.setMsisdn(request.getIsdn());

		response = service.recycle(header, getProductAlias(request.getProductId()));

		return response.getResponseName();
	}

	public String reactive(CommandMessage request, int sessionid) throws Exception
	{
		RequestHeader header = new RequestHeader();
		header.setAccessMedia(request.getChannel());
		header.setExternalId("" + sessionid);
		header.setMsisdn(request.getIsdn());

		response = service.activate(header, getProductAlias(request.getProductId()));

		return response.getResponseName();
	}

	public String renewal(CommandMessage request, int renewalStatus, int sessionid) throws Exception
	{
		RequestHeader header = new RequestHeader();
		header.setAccessMedia(request.getChannel());
		header.setExternalId("" + sessionid);
		header.setMsisdn(request.getIsdn());

		response = service.renewalFailure(header, renewalStatus, getProductAlias(request.getProductId()));

		return response.getResponseName();
	}

	public String isSuccessCommand(String result)
	{
		result = result.toUpperCase();

		if ("".equals(result))
			return FAILURE;
		if (result.contains("COMMAND_OK"))
		{
			return SUCCESS;
		}

		return FAILURE;
	}

	public Response getResponse()
	{
		return response;
	}

	public void setResponse(Response response)
	{
		this.response = response;
	}

	public static void main(String args[])
	{
		CommandMessage request = new CommandMessage();
		request.setIsdn("84929999699");
		request.setChannel("SMS");
		// request.setProductId("Voiceim");
		try
		{
			VIMConnection connection = new VIMConnection();
			connection.setHost("10.8.37.13");
			connection.setPort(8080);
			connection.openConnection();
			String response = "";
			response = connection.register(request, 500, 101, 0);
			// response = connection.unregister(request);
			// response = connection.reactive(request);
			// response = connection.renewal(request, 601);
//			System.out.print(response);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
