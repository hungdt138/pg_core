package com.crm.provisioning.impl.txt;

import java.net.URL;
import com.crm.kernel.message.Constants;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.impl.vim.VIMConnection;
import com.crm.provisioning.message.CommandMessage;
import com.unified.txtback.soap.RequestHeader;
import com.unified.txtback.soap.ResponseHeader;
import com.unified.txtback.soap.SoapApiImplServiceLocator;
import com.unified.txtback.soap.SoapApiImplServiceSoapBindingStub;

public class TXTConnection extends ProvisioningConnection
{
	public final static String	SUCCESS			= "success";
	public final static String	FAILURE			= "fail";
	

	private SoapApiImplServiceSoapBindingStub		service;
	private ResponseHeader							response;
	
	public TXTConnection()
	{
		super();
	}
	
	private URL getURL(String host, int port) throws Exception
	{
		String strUrl = "http://" + host + ":" + port + "/soap-api/service";

		URL url = new URL(strUrl);
		return url;
	}
	
	@Override
	public boolean openConnection() throws Exception
	{
		URL url = getURL(getHost(), getPort());

		SoapApiImplServiceLocator locator = new SoapApiImplServiceLocator();
		service = new SoapApiImplServiceSoapBindingStub(url, locator);
		
		return super.openConnection();
	}
	
	public int register(CommandMessage request, int subscriberType, int sessionid, int packageType) throws Exception
	{
		RequestHeader header = new RequestHeader();
		header.setAccessMedia(getAccessMedia(request.getChannel(), request.getActionType()));
		header.setExternalId("" + sessionid);
		header.setMsisdn(request.getIsdn());

		response = service.subscribe(header, "", subscriberType, packageType, Constants.SUCCESS, "NONE", "");
		return response.getResponseCode();
	}
	
	public int unregister(CommandMessage request, int sessionid) throws Exception
	{
		RequestHeader header = new RequestHeader();
		header.setAccessMedia(getAccessMedia(request.getChannel(), request.getActionType()));
		header.setExternalId("" + sessionid);
		header.setMsisdn(request.getIsdn());

		response = service.terminate(header, "");
		return response.getResponseCode();
	}
	
	public int renewal(CommandMessage request, int sessionid, String statusCode, String chargeType, int renewalDays) throws Exception
	{
		RequestHeader header = new RequestHeader();
		header.setAccessMedia(getAccessMedia(request.getChannel(), request.getActionType()));
		header.setExternalId("" + sessionid);
		header.setMsisdn(request.getIsdn());

		response = service.renewal(header, statusCode, "NONE", chargeType, renewalDays, "");
		return response.getResponseCode();
	}
	
	public int getAccessMedia(String channel, String actionType)
	{
		if (channel.equals(Constants.CHANNEL_SMS))
		{
			return 1;
		}
		else if (channel.equals(Constants.CHANNEL_CORE))
		{
			if (actionType.equals(Constants.ACTION_FREE))
			{
				return 10;
			}
			else
			{
				return 5;
			}
		}
		else
		{
			return 12;
		}
	}
	
	public static void main(String args[])
	{
		CommandMessage request = new CommandMessage();
		request.setIsdn("84922000512");
		// request.setProductId("Voiceim");
		try
		{
			TXTConnection connection = new TXTConnection();
			connection.setHost("10.8.39.48");
			connection.setPort(8080);
			connection.openConnection();
			int response = 123;
//			request.setChannel("SMS");
//			response = connection.register(request, 1, 123456, 1);
//			request.setChannel("SMS");
//			response = connection.unregister(request, 123456);
//			request.setChannel("core");
//			response = connection.renewal(request, 123456, "ACTIVE", "NORMAL", 0);
//			request.setChannel("core");
//			response = connection.renewal(request, 123456, "ACTIVE", "PARTIAL", 10);
//			request.setChannel("core");
//			response = connection.renewal(request, 123456, "SUSPENDED", "", 0);
			System.out.print(response);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
