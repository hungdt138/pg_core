package com.crm.provisioning.impl.osa;

import java.net.URL;
import java.rmi.RemoteException;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueSession;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.csapi.www.cs.schema.TpAppInformationSet;
import org.csapi.www.cs.schema.TpChargingParameterSet;
import org.csapi.www.cs.schema.TpChargingSessionID;
import org.csapi.www.cs.schema.TpCorrelationID;
import org.csapi.www.cs.schema.TpMerchantAccountID;
import org.csapi.www.cs.wsdl.CsLocator;
import org.csapi.www.cs.wsdl.IpChargingManager;
import org.csapi.www.cs.wsdl.P_INVALID_ACCOUNT;
import org.csapi.www.cs.wsdl.P_INVALID_USER;
import org.csapi.www.osa.schema.TpAddress;
import org.csapi.www.osa.schema.TpAddressPlan;
import org.csapi.www.osa.schema.TpAddressPresentation;
import org.csapi.www.osa.schema.TpAddressScreening;
import org.csapi.www.osa.wsdl.TpCommonExceptions;
import org.csapi.www.cs.wsdl.IpChargingSession;
import org.csapi.www.cs.schema.TpChargingPrice;
import org.csapi.www.cs.schema.TpAmount;
import org.csapi.www.cs.schema.TpApplicationDescription;
// import org.csapi.www.cs.schema.TpChargingParameterSet;
// import org.csapi.www.cs.schema.TpAppInformationSet;
import org.csapi.www.cs.schema.TpChargingParameterValue;
import org.csapi.www.cs.schema.TpChargingParameterValueType;
import org.csapi.www.cs.schema.TpChargingParameter;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.OSACallbackMessage;
import com.crm.provisioning.thread.osa.OSACommandInstance;
import com.crm.provisioning.thread.osa.OSAThread;
import com.crm.provisioning.util.CommandUtil;
import com.crm.util.AppProperties;
import com.fss.util.AppException;

public class OSAConnection extends ProvisioningConnection
{
	public CsLocator			csLocator;

	public IpChargingManager	chargingManager;
	public IpChargingSession	_IpChargingSession;

	public TpMerchantAccountID	merchant;

	public String				applicationName		= "NMS";
	public String				serviceDescription	= "VASMAN";
	public String				currency			= "VND";
	public String				merchantAccount		= "MCA";
	public int					merchantId			= 4;

	public String				callbackHost		= "localhost";
	public int					callbackPort		= OSAThread.DEFAULT_CALLBACK_PORT;

	public OSAConnection()
	{
		super();
	}

	// public OSAConnection(
	// String applicationName, String host, String account, int merchantId,
	// String callbackHost,String callbackPort)
	// throws ServiceException
	// {
	// super();
	//
	// openCommandConnection(applicationName, host, account, merchantId,
	// callbackHost,callbackPort);
	// }
	//
	// public void openCommandConnection(
	// String applicationName, String host, String account, int merchantId,
	// String callbackHost, String callbackPort)
	// throws ServiceException
	// {
	// this.applicationName = applicationName;
	// this.callbackHost = callbackHost;
	//
	// EngineConfiguration configuration = new
	// FileProvider("client-config-osa.wsdd");
	//
	// csLocator = new CsLocator(configuration);
	//
	// csLocator.setIpChargingSessionEndpointAddress(host);
	// csLocator.setIpAppChargingSessionEndpointAddress(host);
	// csLocator.setIpChargingManagerEndpointAddress(host);
	//
	// chargingManager = csLocator.getIpChargingManager();
	// _IpChargingSession = csLocator.getIpChargingSession();
	//
	// merchant = new TpMerchantAccountID(account, merchantId);
	// }
	public void setParameters(AppProperties parameters) throws Exception
	{
		super.setParameters(parameters);

		applicationName = getParameters().getString("applicationName", "NMS_ChargingGateway");
		serviceDescription = getParameters().getString("serviceDescription", "MCA");
		currency = getParameters().getString("currency", "VND");

		merchantAccount = getParameters().getString("merchantAccount", "MCA");
		merchantId = getParameters().getInteger("merchantId", 4);

		callbackHost = getParameters().getString("callbackHost", "");
		callbackPort = getParameters().getInteger("callbackPort", 5000);
	}

	public boolean openConnection() throws Exception
	{
		EngineConfiguration configuration = new FileProvider("client-config-osa.wsdd");

		csLocator = new CsLocator(configuration);

		csLocator.setIpChargingSessionEndpointAddress(host);
		csLocator.setIpAppChargingSessionEndpointAddress(host);
		csLocator.setIpChargingManagerEndpointAddress(host);

		chargingManager = csLocator.getIpChargingManager();
		_IpChargingSession = csLocator.getIpChargingSession();

		merchant = new TpMerchantAccountID(merchantAccount, merchantId);

		return super.openConnection();
	}

	public TpAddress createTpAddress(String isdn)
	{
		TpAddress user =
				new TpAddress(
						TpAddressPlan.P_ADDRESS_PLAN_E164, isdn, ""
						, TpAddressPresentation.P_ADDRESS_PRESENTATION_UNDEFINED
						, TpAddressScreening.P_ADDRESS_SCREENING_UNDEFINED, "");

		return user;
	}

	public TpChargingSessionID createChargingSession(String isdn, String sessionDescription)
			throws P_INVALID_ACCOUNT, P_INVALID_USER, TpCommonExceptions, RemoteException
	{
		TpAddress user = createTpAddress(isdn);
		TpCorrelationID correlationID = new TpCorrelationID(0, 0);

		TpChargingSessionID chargingSession =
				chargingManager.createChargingSession(
						callbackHost + ":" + callbackPort, sessionDescription, merchant, user, correlationID);

		return chargingSession;
	}

	public TpChargingParameterSet createChargingParameters()
	{
		TpChargingParameter paramValue =
				new TpChargingParameter(
						1,
						new TpChargingParameterValue(
								TpChargingParameterValueType.P_CHS_PARAMETER_STRING, null, null, "AMOUNT", null, null));

		// TpChargingParameter[] tpChargingParameterSet = new
		// TpChargingParameter[1];
		// tpChargingParameterSet[0] = paramValue;

		TpChargingParameter paramValue1 =
				new TpChargingParameter(
						2,
						new TpChargingParameterValue(
								TpChargingParameterValueType.P_CHS_PARAMETER_STRING,
								null, null, serviceDescription, null, null));

		TpChargingParameter[] tpChargingParameterSet = new TpChargingParameter[2];
		tpChargingParameterSet[0] = paramValue;
		tpChargingParameterSet[1] = paramValue1;
		TpChargingParameterSet chargingParameters = new TpChargingParameterSet(tpChargingParameterSet);

		return chargingParameters;
	}

	public OSACallbackMessage waitResponse(IpChargingSession chargingSession, int sessionId, 
			TpApplicationDescription appDescription, TpChargingParameterSet chargingParameters, 
			TpChargingPrice _amount, int requestNumber, OSACommandInstance instance, 
			CommandMessage request, long timeout, boolean isDebit)
			throws Exception
	{
		MessageConsumer responseConsumer = null;
		
		QueueSession session = null;

		try
		{
			String callbackSelector = "JMSCorrelationID = '" + sessionId + "'";

			MQConnection connection = null;
			
			try
			{
				connection = instance.getMQConnection();
				// responseConsumer = connection.createTempConsumer(instance.queueCallback, callbackSelector);
				
				session = connection.createSession();
				
				//responseConsumer = instance.queueSession.createConsumer(instance.queueCallback, callbackSelector);
			}
			finally
			{
				instance.returnMQConnection(connection);
			}

			responseConsumer = session.createConsumer(instance.queueCallback, callbackSelector);
			
			if (isDebit)
			{
				chargingSession.directDebitAmountReq(sessionId, appDescription, chargingParameters, _amount, requestNumber);
			}
			else
				chargingSession.directCreditAmountReq(sessionId, appDescription, chargingParameters, _amount, requestNumber);
			
			Message response = responseConsumer.receive(timeout);
			if (response == null)
				throw new AppException(Constants.ERROR_TIMEOUT);

			OSACallbackMessage callbackContent = (OSACallbackMessage) QueueFactory.getContentMessage(response);

			if (callbackContent == null)
			{
//				/**
//				 * Force to be success in case of timeout.
//				 */
//				callbackContent = new OSACallbackMessage();
//				
//				callbackContent.setSessionId(sessionId + "");
//				callbackContent.setNextChargingSequence((requestNumber + 1) + "");
//				callbackContent.setActionType("SentSuccessWithoutResponse");
//					
//				callbackContent.setCause(Constants.ERROR);
				
				throw new AppException(Constants.ERROR_TIMEOUT);
				// request.setCause(Constants.SUCCESS);
			}
			else
			{
				request.getParameters().setInteger("sessionId", sessionId);

				request.getParameters().setString("nextChargingSequence", callbackContent.getNextChargingSequence());
				request.getParameters().setString("nextChargingSequence", callbackContent.getNextChargingSequence());
			}

			return callbackContent;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			QueueFactory.closeQueue(responseConsumer);
			QueueFactory.closeQueue(session);
		}
	}

	public OSACallbackMessage directDebit(OSACommandInstance instance, CommandMessage request,
			TpChargingSessionID chargingSession, String description) throws Exception
	{
		//String isdn = CommandUtil.addCountryCode(request.getIsdn());

		int amount = CommandUtil.getAmount(request.getAmount());

		TpChargingParameterSet chargingParameters = createChargingParameters();
		TpChargingPrice _amount = new TpChargingPrice(currency, new TpAmount(amount, 0));

		int sessionId = chargingSession.getChargingSessionID();
		int requestNumber = chargingSession.getRequestNumberFirstRequest();
		String chargingSessionReference = chargingSession.getChargingSessionReference();

		request.getParameters().setInteger("sessionId", sessionId);
		request.getParameters().setInteger("requestNumber", requestNumber);
		request.getParameters().setInteger("chargingSessionReference", requestNumber);

		if (chargingSessionReference != null && !chargingSessionReference.equals(""))
		{
			_IpChargingSession = csLocator.getIpChargingSession(new URL(chargingSessionReference));
		}
		else
		{
			_IpChargingSession = csLocator.getIpChargingSession();
		}

		TpAppInformationSet appInformation = new TpAppInformationSet();
		TpApplicationDescription appDescription = new TpApplicationDescription(appInformation, applicationName);

		// start wait before send request to OSA
		// send debt request

		int nextRequest = request.getParameters().getInteger("nextChargingSequence", requestNumber + 1);
		try
		{
			//_IpChargingSession.directDebitAmountReq(sessionId, appDescription, chargingParameters, _amount, requestNumber);

			OSACallbackMessage response = waitResponse(_IpChargingSession, sessionId, appDescription, chargingParameters,
					_amount, requestNumber, instance, request, timeout, true);

			return response;
		}
		finally
		{
			try
			{
				_IpChargingSession.release(sessionId, nextRequest);
			}
			catch (Exception e)
			{	
				instance.debugMonitor(e);
			}
		}
	}

	public OSACallbackMessage directCredit(OSACommandInstance instance, CommandMessage request,
			TpChargingSessionID chargingSession, String description) throws Exception
	{
		//String isdn = CommandUtil.addCountryCode(request.getIsdn());

		int amount = CommandUtil.getAmount(request.getAmount());

		TpChargingParameterSet chargingParameters = createChargingParameters();
		TpChargingPrice _amount = new TpChargingPrice(currency, new TpAmount(amount, 0));

		int sessionId = chargingSession.getChargingSessionID();
		int requestNumber = chargingSession.getRequestNumberFirstRequest();
		String chargingSessionReference = chargingSession.getChargingSessionReference();

		request.getParameters().setInteger("sessionId", sessionId);
		request.getParameters().setInteger("requestNumber", requestNumber);
		request.getParameters().setInteger("chargingSessionReference", requestNumber);

		TpAppInformationSet appInformation = new TpAppInformationSet();
		TpApplicationDescription appDescription = new TpApplicationDescription(appInformation, applicationName);

		if (chargingSessionReference != null && !chargingSessionReference.equals(""))
		{
			_IpChargingSession = csLocator.getIpChargingSession(new URL(chargingSessionReference));
		}
		else
		{
			_IpChargingSession = csLocator.getIpChargingSession();
		}

		int nextRequest = request.getParameters().getInteger("nextChargingSequence", requestNumber + 1);

		// start wait before send request to OSA
		// send credit request

		try
		{
			// _IpChargingSession.directCreditAmountReq(sessionId, appDescription, chargingParameters, _amount, requestNumber);

			// wait response
			OSACallbackMessage response = waitResponse(_IpChargingSession, sessionId, appDescription, chargingParameters,
					_amount, requestNumber, instance, request, timeout, false);

			return response;
		}
		finally
		{
			try
			{
				_IpChargingSession.release(sessionId, nextRequest);
			}
			catch (Exception e)
			{	
				instance.debugMonitor(e);
			}
		}
	}

	public static void main(String args[])
	{
		String applicationName, host, account, callback;
		applicationName = "MCA";
		host = "http://rtbstest.htmobile.com.vn/osa/mobility";
		account = "MCA";
		int merchantId = 4;
		callback = "10.32.62.32:5000";
		String strCallbackHost = "10.32.62.32";
		String strCallbackPort = "5000";
		String isdn = "84922000512";
		try
		{
			// OSACommandInstance instance = new OSACommandInstance();
			// CommandMessage request = new CommandMessage();
			// request.setIsdn(isdn);
			// OSAConnection k = new OSAConnection(applicationName, host,
			// account, merchantId, strCallbackHost, strCallbackPort);
			// //k.directDebit(null, null, null, "84922000512", 4610000, "VND",
			// new Properties(), "test");
			// k.directDebit(instance, request, "test");
		}
		catch (Exception ex)
		{

		}
	}
}
