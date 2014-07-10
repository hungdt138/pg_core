package com.crm.provisioning.impl.ccws;

import com.comverse_in.prepaid.ccws.ArrayOfBalanceEntity;

import com.comverse_in.prepaid.ccws.BalanceEntity;

import com.comverse_in.prepaid.ccws.ServiceSoapStub;

import com.comverse_in.prepaid.ccws.SubscriberCreate;

import com.comverse_in.prepaid.ccws.SubscriberEntity;

import com.comverse_in.prepaid.ccws.SubscriberMainBase;

import com.comverse_in.prepaid.ccws.SubscriberModify;

import com.comverse_in.prepaid.ccws.SubscriberPPS;

import com.comverse_in.prepaid.ccws.SubscriberRetrieve;

import java.net.URL;

import java.util.Calendar;

import java.util.LinkedHashMap;

import java.util.Map;

import javax.xml.rpc.Service;

import org.apache.axis.AxisFault;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.WSConstants;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import com.comverse_in.prepaid.ccws.ServiceLocator;

public class CCWSService extends ServiceSoapStub

{

	private String	url			= "http://10.8.13.140/ccws/ccws.asmx";
	private String	user		= "";
	private String	password	= "";

	public CCWSService() throws AxisFault
	{

	}

	public CCWSService(URL endpointURL, Service service) throws AxisFault
	{
		super(endpointURL, service);
	}

	public CCWSService(String strUrl, String strPassword, String strUser) throws AxisFault
	{
		url = strUrl;
		password = strPassword;
		user = strUser;

		loadService();
	}

	private void loadService()
	{
		ServiceSoapStub binding = null;
		if (url == null || url.equals(""))
			throw new NullPointerException("CCWS URL is not valid");

		try
		{
			try
			{
				java.net.URL endpoint = new java.net.URL(url);
				EngineConfiguration configuration = new FileProvider(
							"client-config.wsdd");
				ServiceLocator locator = new ServiceLocator(configuration);
				binding = (CCWSService) locator.getServiceSoap(endpoint);
				// this=binding;
				this._setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
				this._setProperty(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
				this._setProperty(WSHandlerConstants.USER, user);
				PasswordCallback pwCallback = new PasswordCallback(user, password);
				this._setProperty(WSHandlerConstants.PW_CALLBACK_REF, pwCallback);
			}
			catch (javax.xml.rpc.ServiceException jre)
			{
				if (jre.getLinkedCause() != null)
				{
					jre.getLinkedCause().printStackTrace();
				}
				throw new Exception("JAX-RPC ServiceException caught: " + jre);
			}
			if (binding == null)
			{
				throw new Exception("Binding is null");
			}
		}
		catch (Exception ex)
		{
			binding = null;
			ex.printStackTrace();
		}
		// return binding;
	}

	public Object retrieveSubsInfo(String strIsdn)
	{
		Map mpResponse;

		try
		{

			mpResponse = new LinkedHashMap();

			SubscriberRetrieve retrieve = super.
					retrieveSubscriberWithIdentityNoHistory(strIsdn, "", 23);

			mpResponse.put("cosname", retrieve.getSubscriberData().getCOSName());

			mpResponse.put("state", retrieve.getSubscriberData().getCurrentState());

			mpResponse.put("active_date", retrieve.getSubscriberData().getDateEnterActive().getTime());

			ArrayOfBalanceEntity LBalance = retrieve.getSubscriberData().getBalances();

			for (int i = 0; i < LBalance.getBalance().length; ++i)
			{
				BalanceEntity balance = LBalance.getBalance(i);

				mpResponse.put(balance.getBalanceName().toLowerCase(), String.valueOf(balance.getBalance()));
			}

			return mpResponse;
		}

		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;

	}

	public Object changeCOS(String strIsdn, String newCos)
	{

		SubscriberModify subMod = new SubscriberModify();

		subMod.setSubscriberID(strIsdn);

		SubscriberPPS pss = new SubscriberPPS();

		pss.setCOSName(newCos);

		subMod.setSubscriber(pss);

		try
		{

			boolean bln = super.modifySubscriber(subMod);

			if (!(bln))
			{

				return new Exception("Fail to change COS");
			}

		}

		catch (Exception ex)
		{

			return ex;

		}

		return Boolean.valueOf(true);

	}

	public Object changeStatus(String strIsdn, String newStatus)
	{

		SubscriberModify subMod = new SubscriberModify();

		subMod.setSubscriberID(strIsdn);

		SubscriberPPS pss = new SubscriberPPS();

		pss.setCurrentState(newStatus);

		subMod.setSubscriber(pss);

		try
		{

			boolean bln = super.modifySubscriber(subMod);

			if (!(bln))
			{

				return new Exception("Fail to change status");
			}

		}

		catch (Exception ex)
		{

			return ex;

		}

		return Boolean.valueOf(true);

	}

	public Object createSubscriber(String strIsdn, String cos)

	{

		SubscriberCreate subCreate = new SubscriberCreate();

		subCreate.setSubscriberID(strIsdn);

		subCreate.setSPName("HTC_HAN");

		SubscriberMainBase base = new SubscriberMainBase();

		base.setCOSName(cos);

		base.setCurrentState("Idle");

		base.setLanguageName("Vietnamese");

		base.setMarketSegment("ANY");

		subCreate.setSubscriber(base);

		try

		{

			boolean bln = super.createSubscriber(subCreate);

			if (!(bln))
			{

				return new Exception("Fail to create subscriber");
			}

		}

		catch (Exception ex)
		{

			return ex;

		}

		return Boolean.valueOf(true);
	}

	public Object deleteSubscriber(String strMDN)
	{

		boolean bln;

		try
		{

			bln = super.deleteSubscriber(strMDN, null);

			if (!(bln))
			{

				return new Exception("Fail to delete subscriber");
			}

		}

		catch (Exception ex)
		{

			return ex;

		}

		return Boolean.valueOf(true);

	}

}
