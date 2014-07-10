package com.crm.provisioning.impl.osa;

import java.io.ByteArrayInputStream;
import java.util.Date;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.OSACallbackMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.thread.osa.OSACommandInstance;
import com.crm.provisioning.thread.osa.OSAThread;
import com.crm.provisioning.util.CommandUtil;
import com.crm.util.AppProperties;
import com.fss.util.AppException;
import com.sun.enterprise.v3.admin.GetHabitatInfo;

public class OSAConnection extends ProvisioningConnection
{
	public final static int					DEFAULT_TIME_OUT		= 30000;

	public final static String				SESSION_TAG				= "ChargingSessionID";
	public final static String				REQUEST_NUMBER_TAG		= "RequestNumberFirstRequest";
	public final static String				SESSION_REFERENCE_TAG	= "ChargingSessionReference";

	public final static CloseableHttpClient	testClient				= HttpClients.createDefault();

	public static String					applicationName			= "NMS";
	public static String					serviceDescription		= "VASMAN";
	public static String					currency				= "VND";
	public static String					merchantAccount			= "NMS";
	public static int						merchantId				= 6;

	public static String					callbackHost			= "10.32.62.91";
	public static int						callbackPort			= OSAThread.DEFAULT_CALLBACK_PORT;

	public OSAConnection()
	{
		super();
	}

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

	public static String getValue(String content, String tag)
	{
		int startIndex = content.indexOf("<" + tag + ">");
		int endIndex = content.indexOf("</" + tag + ">");

		return content.substring(startIndex + tag.length() + 2, endIndex);
	}

	public static boolean checkError(HttpResponse response) throws AppException
	{
		if (response.getStatusLine().getStatusCode() == 200)
		{
			return false;
		}
		else
		{
			String errorContent = "";

			try
			{
				errorContent = EntityUtils.toString(response.getEntity());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			throw new AppException(errorContent);
		}
	}

	public static void safeClose(Object object) throws Exception
	{
		if (object != null)
		{
			try
			{
				if (object instanceof CloseableHttpResponse)
				{
					((CloseableHttpResponse) object).close();
				}
				else if (object instanceof CloseableHttpClient)
				{
					((CloseableHttpClient) object).close();
				}
				else if (object instanceof HttpPost)
				{
					((HttpPost) object).releaseConnection();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static String callSoap(
			CloseableHttpClient httpClient, HttpPost httpPost, String sessionId, String soapAction, StringBuilder request, boolean keepAlive)
			throws Exception
	{
		long startTime = System.currentTimeMillis();

		CloseableHttpResponse response = null;

		httpPost.setHeader("Content-type", "text/xml; charset=utf-8");
		httpPost.setHeader("Accept", "application/soap+xml, application/dime, multipart/related, text/*");
		httpPost.setHeader("Cache-Control", "no-cache");
		httpPost.setHeader("Pragma", "no-cache");
		httpPost.setHeader("SOAPAction", "http://www.csapi.org/cs/" + soapAction);

		if (keepAlive)
		{
			httpPost.setHeader("Connection", "Keep-alive");
			httpPost.setHeader("Keep-alive", "timeout=15, max=100");
		}
		else
		{
			httpPost.setHeader("Connection", "close");
		}
		StringBuilder content = new StringBuilder();

		content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		content.append("<soapenv:Envelope");
		content.append(" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"");
		content.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
		content.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		content.append("<soapenv:Body>");

		content.append(request);

		content.append("</soapenv:Body>");
		content.append("</soapenv:Envelope>");

		ByteArrayInputStream stream = new ByteArrayInputStream(content.toString().getBytes());

		startTime = System.currentTimeMillis();
		long responseCost = 0;
		long entityCost = 0;
		long parserCost = 0;

		httpPost.setEntity(new InputStreamEntity(stream, content.length()));

		try
		{
			response = httpClient.execute(httpPost);

			responseCost = (System.currentTimeMillis() - startTime);
			startTime = System.currentTimeMillis();

			HttpEntity entity = response.getEntity();

			entityCost = (System.currentTimeMillis() - startTime);
			startTime = System.currentTimeMillis();

			if (entity != null && !checkError(response))
			{
				return EntityUtils.toString(entity);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			safeClose(response);

			parserCost = (System.currentTimeMillis() - startTime);

//			if (sessionId.equals(""))
//			{
//				System.out.println(soapAction + ": " + responseCost + "," + entityCost + "," + parserCost);
//			}
//			else
//			{
//				System.out.println("sessionId = " + sessionId + " : " + soapAction + ", " + responseCost + "," + entityCost + "," + parserCost);
//			}
		}

		return "";
	}

	public static String callSoap(
			CloseableHttpClient httpClient, HttpPost httpPost, String soapAction, StringBuilder request, boolean keepAlive) throws Exception
	{
		return callSoap(httpClient, httpPost, "", soapAction, request, keepAlive);
	}

	public static OSAChargingSession createChargingSession(
			CloseableHttpClient httpClient, HttpPost httpPost, String merchantId, String accountId, String callback, String isdn) throws Exception
	{
		isdn = CommandUtil.addCountryCode(isdn);

		// Prepare the HTTP request.
		StringBuilder content = new StringBuilder();

		content.append("<createChargingSession xmlns=\"http://www.csapi.org/cs/wsdl\">");
		content.append("<appChargingSession xmlns=\"\">");
		content.append(callback);
		content.append("</appChargingSession>");
		content.append("<sessionDescription xmlns=\"\"></sessionDescription>");
		content.append("<merchantAccount xmlns=\"\">");
		content.append("<MerchantID>");
		content.append(merchantId);
		content.append("</MerchantID>");
		content.append("<AccountID>");
		content.append(accountId);
		content.append("</AccountID>");
		content.append("</merchantAccount>");
		content.append("<user xmlns=\"\">");
		content.append("<Plan>P_ADDRESS_PLAN_E164</Plan>");
		content.append("<AddrString>");
		content.append(isdn);
		content.append("</AddrString>");
		content.append("<Name></Name>");
		content.append("<Presentation>P_ADDRESS_PRESENTATION_UNDEFINED</Presentation>");
		content.append("<Screening>P_ADDRESS_SCREENING_UNDEFINED</Screening>");
		content.append("<SubAddressString></SubAddressString>");
		content.append("</user>");
		content.append("<correlationID xmlns=\"\">");
		content.append("<CorrelationID>0</CorrelationID>");
		content.append("<CorrelationType>0</CorrelationType>");
		content.append("</correlationID>");
		content.append("</createChargingSession>");

		String responseContent = callSoap(httpClient, httpPost, "IpChargingManager#createChargingSession", content, false);

		String sessionid = getValue(responseContent, SESSION_TAG);
		String numberFirstReq = getValue(responseContent, REQUEST_NUMBER_TAG);
		String sessionReference = getValue(responseContent, SESSION_REFERENCE_TAG);

		return new OSAChargingSession(sessionid, Integer.valueOf(numberFirstReq), sessionReference);
	}

	public static OSAChargingSession createChargingSession(
			CloseableHttpClient httpClient, HttpPost httpPost, OSACommandInstance instance, String isdn) throws Exception
	{
		return createChargingSession(
				httpClient, httpPost, merchantAccount, String.valueOf(merchantId)
				, callbackHost + ":" + callbackPort, isdn);
	}

	public static void changeCoreBalance(
			CloseableHttpClient httpClient, HttpPost httpPost, OSAChargingSession session, String soapAction, String currency, int amount)
			throws Exception
	{
		// Prepare the HTTP request.
		StringBuilder content = new StringBuilder();

		content.append("<");
		content.append(soapAction);
		content.append("Req xmlns=\"http://www.csapi.org/cs/wsdl\">");
		content.append("<sessionID xmlns=\"\">");
		content.append(session.getSessionID());
		content.append("</sessionID>");

		content.append("<applicationDescription xmlns=\"\">");
		content.append("<AppInformation/>");
		content.append("<Text>");
		content.append("NMS");
		content.append("</Text>");
		content.append("</applicationDescription>");

		content.append("<chargingParameters xmlns=\"\">");
		content.append("<TpChargingParameterSet>");
		content.append("<ParameterID>" + "1" + "</ParameterID>");
		content.append("<ParameterValue>");

		content.append("<SwitchName>");
		content.append("P_CHS_PARAMETER_STRING");
		content.append("</SwitchName>");

		content.append("<StringValue>");
		content.append("AMOUNT");
		content.append("</StringValue>");
		content.append("</ParameterValue>");
		content.append("</TpChargingParameterSet>");
		content.append("<TpChargingParameterSet>");
		content.append("<ParameterID>");
		content.append("2");
		content.append("</ParameterID>");
		content.append("<ParameterValue>");
		content.append("<SwitchName>");
		content.append("P_CHS_PARAMETER_STRING");
		content.append("</SwitchName>");
		content.append("<StringValue>");
		content.append("VASMAN");
		content.append("</StringValue>");
		content.append("</ParameterValue>");
		content.append("</TpChargingParameterSet>");
		content.append("</chargingParameters>");
		content.append("<amount xmlns=\"\">");
		content.append("<Currency>");
		content.append("VND");
		content.append("</Currency>");
		content.append("<Amount>");
		content.append("<Number>");
		content.append(amount);
		content.append("</Number>");
		content.append("<Exponent>");
		content.append("0");
		content.append("</Exponent>");
		content.append("</Amount>");
		content.append("</amount>");
		content.append("<requestNumber xmlns=\"\">");
		content.append(session.getRequestNumberFirstRequest());
		content.append("</requestNumber>");
		content.append("</");
		content.append(soapAction);
		content.append("Req>");

		callSoap(httpClient, httpPost, session.getSessionID(), "IpChargingSession#" + soapAction + "Req", content, true);
	}

	public static void releaseChargingSession(
			OSACommandInstance instance, CloseableHttpClient httpClient, HttpPost httpPost, OSAChargingSession session, int nextRequest)
	{
		if ((session == null) || session.getChargingSessionReference().equals(""))
		{
			return;
		}

		StringBuilder content = new StringBuilder();

		content.append("<release xmlns=\"http://www.csapi.org/cs/wsdl\">");
		content.append("<sessionID xmlns=\"\">");
		content.append(session.getSessionID());
		content.append("</sessionID>");
		content.append("<requestNumber xmlns=\"\">");
		content.append(nextRequest);
		content.append("</requestNumber>");
		content.append("</release>");

		try
		{
			callSoap(httpClient, httpPost, session.getSessionID(), "IpChargingSession#release", content, false);
		}
		catch (AppException e)
		{
			instance.debugMonitor("sessionId = " + session.getSessionID() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			instance.debugMonitor("sessionId = " + session.getSessionID() + ": " + e.getMessage());
			// e.printStackTrace();
		}
	}

	public static OSACallbackMessage waitResponse(
			OSACommandInstance instance, OSAChargingSession chargingSession, CommandMessage request, long timeout) throws Exception
	{
		MessageConsumer consumer = null;
		OSACallbackMessage callbackContent = null;
		
		QueueSession session = null;

		try
		{
			Exception error = null;
			String callbackSelector = "JMSCorrelationID = '" + chargingSession.getSessionID() + "'";

			MQConnection connection = null;
			
			try
			{
				connection = instance.getMQConnection();
				session = connection.createSession();
			}
			finally
			{
				instance.returnMQConnection(connection);
			}
			
			consumer = session.createConsumer(instance.queueCallback, callbackSelector);
			
			if (consumer == null)
			{
				throw new Exception("can not create consumer: " + (error == null ? "" : error.getMessage()));
			}

			Message response = consumer.receive(timeout);

			if (response == null)
			{
				throw new AppException(Constants.ERROR_TIMEOUT);
			}

			callbackContent = (OSACallbackMessage) QueueFactory.getContentMessage(response);

			if (callbackContent == null)
			{
				throw new AppException(Constants.ERROR_TIMEOUT);
			}
			else
			{
				request.getParameters().setString("callbackReceiveDate", callbackContent.getParameters().getString("callbackReceiveDate"));
				request.getParameters().setString("callbackParseDate", callbackContent.getParameters().getString("callbackParseDate"));
				request.getParameters().setString("callbackResult", callbackContent.getActionType());
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
			QueueFactory.closeQueue(consumer);
		}
	}

	public static CommandMessage charging(
			CommandInstance instance, CommandMessage request, String description, boolean debit) throws Exception
	{
		if (request == null)
		{
			return request;
		}
		request.setRequestTime(new Date());

		OSAChargingSession chargingSession = null;

		int requestNumber = -1;
		int nextRequest = -1;
		int amount = CommandUtil.getAmount(request.getAmount());

		String sessionId = "";
		String chargingURL = "";

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = null;

		try
		{
			httpPost = new HttpPost(instance.getDispatcher().host);

			chargingSession = createChargingSession(httpClient,httpPost, (OSACommandInstance) instance, request.getIsdn());

			//log request start
			String requestString = getRequestString(chargingSession.getRequestNumberFirstRequest(),
					request.getIsdn(),
					amount + "", currency, description, chargingSession.getChargingSessionReference());
			setRequest(instance, request, requestString, chargingSession.getSessionID());
			//log request end

			sessionId = chargingSession.getSessionID();
			chargingURL = chargingSession.getChargingSessionReference();

			requestNumber = chargingSession.getRequestNumberFirstRequest();
			nextRequest = requestNumber + 1;

			request.getParameters().setString("sessionId", sessionId);
			request.getParameters().setInteger("requestNumber", requestNumber);
			request.getParameters().setString("chargingSessionReference", chargingURL);

			if ((chargingURL != null) && !chargingURL.equals(""))
			{
				safeClose(httpPost);
				httpPost = new HttpPost(chargingURL);
			}

			OSACallbackMessage response = null;

			String correlationId = "osa.callback." + chargingSession.getSessionID();

			if (debit)
			{
				changeCoreBalance(httpClient, httpPost, chargingSession, "directDebitAmount", currency, amount);
			}
			else
			{
				changeCoreBalance(httpClient, httpPost, chargingSession, "directCreditAmount", currency, amount);
			}

			response = waitResponse((OSACommandInstance) instance, chargingSession, request, instance.getDispatcher().timeout);

			if (response == null)
			{
				throw new AppException(Constants.ERROR_TIMEOUT);
			}
			else if ((response != null) && !response.getCause().equals(Constants.SUCCESS))
			{
				request.setCause(Constants.ERROR);
				request.setDescription(response.getCause());
				request.setStatus(Constants.ORDER_STATUS_DENIED);
			}
			
			//log response start
			String responseString = getResponseString(response, request.getIsdn());
			setResponse(instance, request, responseString, chargingSession.getSessionID());
			//log response end
		}
		finally
		{
			try
			{
				if (chargingSession != null)
				{
					nextRequest = request.getParameters().getInteger("nextChargingSequence", requestNumber + 1);
					releaseChargingSession((OSACommandInstance) instance, httpClient, httpPost, chargingSession, nextRequest);
				}
			}
			finally
			{
				safeClose(httpPost);
				safeClose(httpClient);
			}
			
			request.setResponseTime(new Date());
		}

		return request;
	}
	
	private static String getRequestString(int requestNumber, String isdn, String amount, String currency, String description,
			String sessionReferenceUrl)
	{
		String requestString = "ISDN=" + isdn
				+ ", AMOUNT=" + amount + " " + currency + ", DESC=" + description + ", REF="
				+ (sessionReferenceUrl != null && !sessionReferenceUrl.equals("") ? sessionReferenceUrl : "OSA_HOST");

		return requestString;
	}
	
	private static void setRequest(CommandInstance instance, CommandMessage request, String requestString, String sessionId)
			throws Exception
	{
		requestString = "ID=" + sessionId + ": " + requestString;

//		instance.logMonitor("SEND: " + requestString);
		request.setRequest(requestString);
		request.setRequestTime(new Date());
		
		request.setRequestTime(new Date());
	}
	
	private static String getResponseString(OSACallbackMessage response, String isdn)
	{
		String responseString = "ISDN=" + isdn + ", ACTION=" + response.getActionType() + ", CAUSE="
				+ response.getCause();
		return responseString;
	}
	
	private static void setResponse(CommandInstance instance, CommandMessage request,
			String responseString, String sessionId) throws Exception
	{
		request.setResponseTime(new Date());

		long costTime = CommandUtil.calculateCostTime(
				request.getRequestTime(), request.getResponseTime());

		request.setDescription(responseString);
		
		responseString = "ID=" + sessionId + ": " + responseString
				+ ": costTime=" + costTime;
//		instance.logMonitor("RECEIVE: " + responseString);
		request.setResponse(responseString);
	}
}
