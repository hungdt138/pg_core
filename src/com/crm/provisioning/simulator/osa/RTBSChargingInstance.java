/*
 * @(#)RTBSChargingService.java
 * Copyright 2006 ELCOM Software Solution, Corp. All rights reserved.
 */

package com.crm.provisioning.simulator.osa;

/**
 * Charging service
 *
 * @author     <a href=mailto:tantn@elcom.com.vn>Tran Nhat Tan</a>
 * @version    1.0
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.apache.axis.Message;
import org.apache.axis.message.*;
import javax.xml.namespace.QName;

import java.net.InetAddress;

import java.util.HashMap;

import org.csapi.www.cs.schema.TpChargingError;
import org.csapi.www.cs.schema.TpChargingPrice;
import org.csapi.www.cs.schema.TpAmount;

import com.crm.provisioning.thread.ProvisioningThread;
import com.crm.thread.DispatcherInstance;

public class RTBSChargingInstance extends DispatcherInstance
{
	/**
	 * End point of this service
	 */
	public static final String			endPoint	= "/osa/mobility";
	private HashMap<Integer, String>	map			= new HashMap<Integer, String>(100);
	public static boolean				bSuccess	= true;

	public RTBSChargingInstance() throws Exception
	{
		super();
	}

	public ProvisioningThread getDispatcher()
	{
		return (ProvisioningThread) dispatcher;
	}

	public void accept(String endPoint, String header, InputStream input, OutputStream output) throws Exception
	{
		if (0 != endPoint.compareTo(endPoint))
		{
			throw new Exception("invalid-endpoint");
		}

		invoke(header, input, output);
	}

	/**
	 * Get content length from message header
	 * 
	 * @param header
	 *            String
	 * @return int
	 */
	private int getContentLength(String header)
	{
		int i;
		int k;

		i = header.indexOf("Content-Length:");

		if (-1 == i)
		{
			return -1;
		}

		k = header.indexOf('\n', i);

		if (-1 == k)
		{
			return -1;
		}

		String x = header.substring(i + 15, k).trim();

		try
		{
			return Integer.parseInt(x);
		}
		catch (NumberFormatException e)
		{
		}
		return -1;
	}

	/**
	 * Read message content
	 * 
	 * @param input
	 *            InputStream
	 * @param contentLength
	 *            int
	 * @throws IOException
	 * @return String
	 */
	private String readContent(int length, InputStream input) throws IOException
	{
		int i = 0;

		try
		{
			int ch;

			byte[] buf = new byte[length];

			for (; i < length; i++)
			{
				ch = input.read();

				if (-1 == ch)
				{
					return null;
				}

				buf[i] = (byte) ch;
			}

			return new String(buf);
		}
		catch (java.net.SocketTimeoutException e)
		{
			throw e;
		}
		finally
		{
			getLog().debug("Bytes read -- " + i + "/" + length);
		}
	}

	private void invoke(String header, InputStream input, OutputStream output) throws Exception
	{
		int length = getContentLength(header);

		if (-1 == length)
		{
			throw new Exception("Content-Length not found!\n" + header);
		}

		String content = readContent(length, input);

		if (null == content)
		{
			throw new Exception("Connection reset by peer!\n");
		}

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Request:\n" + header + content);
		}
		Message message = new Message(content);
		SOAPEnvelope envelope = message.getSOAPEnvelope();
		SOAPBody body = (SOAPBody) envelope.getBody();
		SOAPBodyElement operation = ((SOAPBodyElement) body.getFirstChild());
		
		if (operation.getQName().equals(new QName("http://www.csapi.org/cs/wsdl", "createChargingSession")))
		{
			createChargingSession(operation, output);
		}
		else if (operation.getQName().equals(new QName("http://www.csapi.org/cs/wsdl", "directDebitAmountReq")))
		{
			directDebitAmountReq(operation, output);
		}

		else if (operation.getQName().equals(new QName("http://www.csapi.org/cs/wsdl", "directCreditAmountReq")))
		{
			directCreditAmountReq(operation, output);
		}
		else if (operation.getQName().equals(new QName("http://www.csapi.org/cs/wsdl", "reserveAmountReq")))
		{
			reserveAmountReq(operation, output);
		}
		else if (operation.getQName().equals(new QName("http://www.csapi.org/cs/wsdl", "debitAmountReq")))
		{
			debitAmountReq(operation, output);
		}
		else if (operation.getQName().equals(new QName("http://www.csapi.org/cs/wsdl", "release")))
		{
			release(operation, output);
		}
		else
		{
			throw new Exception("Operation not supported -- " + operation.getLocalName() + "\n" + header + content);
		}
	}

	private static int		sessionID	= 1;
	private static Object	sync		= new Object();

	private static int getSessionID()
	{
		synchronized (sync)
		{
			return sessionID++;
		}
	}

	/**
	 * 
	 * @param operation
	 *            SOAPBodyElement
	 * @param output
	 *            OutputStream
	 */
	private void createChargingSession(SOAPBodyElement operation, OutputStream output) throws Exception
	{
		MessageElement e = (MessageElement) operation.getChildElement(new QName("", "appChargingSession"));

		if (null == e)
		{
			throw new Exception("Parameter not found -- appChargingSession\n" + operation.toString());
		}

		MessageElement e1 = (MessageElement) operation.getChildElement(new QName("", "user"));

		if (null == e1)
		{
			throw new Exception("Parameter not found -- user\n" + operation.toString());
		}

		MessageElement e2 = (MessageElement) e1.getChildElement(new QName("", "AddrString"));

		String msisdn = e2.getValue();

		if (msisdn.startsWith("92") || msisdn.startsWith("8492"))
		{

			int sessionID = getSessionID();
			String ref = e.getValue();
			synchronized (map)
			{
				map.put(new Integer(sessionID), ref);
			}

			String callback = "http://" + InetAddress.getLocalHost().getHostAddress() + ":5000" + "/osa/mobility";

			String content =
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
							+
							"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:osaxsd=\"http://www.csapi.org/osa/schema\" xmlns:osa=\"http://www.csapi.org/osa/wsdl\" xmlns:csxsd=\"http://www.csapi.org/cs/schema\" xmlns:cs=\"http://www.csapi.org/cs/wsdl\">\n"
							+
							"<SOAP-ENV:Body SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
							"<cs:createChargingSessionResponse>\n" +
							"<return xsi:type=\"csxsd:TpChargingSessionID\">\n" +
							"<ChargingSessionID xsi:type=\"osaxsd:TpSessionID\">" + sessionID +
							"</ChargingSessionID>\n" +
							"<RequestNumberFirstRequest xsi:type=\"osaxsd:TpInt32\">1</RequestNumberFirstRequest>\n" +
							"<ChargingSessionReference xsi:type=\"csxsd:IpChargingSessionRef\">" +
							callback + "</ChargingSessionReference>\n" +
							"</return>\n" +
							"</cs:createChargingSessionResponse>\n" +
							"</SOAP-ENV:Body>\n" +
							"</SOAP-ENV:Envelope>";

			String http =
					"HTTP/1.1 200 OK\r\n" +
							"Date: Mon, 03 Apr 2006 14:19:59 GMT\r\n" +
							"Server: Apache/2.0.48 (Unix)\r\n" +
							"Content-Length: " + content.length() + "\r\n" +
							"Connection: close\r\n" +
							"Content-Type: text/xml\r\n" +
							"\r\n";

			String x = http + content;
			output.write(x.getBytes());
			output.flush();

			if (getLog().isDebugEnabled())
			{
				getLog().debug("Response:\n" + x);
			}
		}
		else
		{
			String content =
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
							"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
							"<SOAP-ENV:Body>" +
							"<SOAP-ENV:Fault>" +
							"<faultcode>SOAP-ENV:Server</faultcode>" +
							"<faultstring>Server Error</faultstring>" +
							"<detail>" +
							"<cs:P_INVALID_USER xmlns:cs=\"http://www.csapi.org/cs/wsdl\">" +
							"<ExtraInformation>Invalid User account specified</ExtraInformation>" +
							"</cs:P_INVALID_USER>" +
							"</detail>" +
							"</SOAP-ENV:Fault>" +
							"</SOAP-ENV:Body>" +
							"</SOAP-ENV:Envelope>";

			String http =
					"HTTP/1.1 500 Internal Server Error\r\n" +
							"Date: Tue, 11 Apr 2006 10:44:30 GMT\r\n" +
							"Server: Apache/2.0.48 (Unix)\r\n" +
							"Content-Length: " + content.length() + "\r\n" +
							"Connection: close\r\n" +
							"Content-Type: text/html\r\n" +
							"\r\n";

			String x = http + content;
			output.write(x.getBytes());
			output.flush();

			if (getLog().isDebugEnabled())
			{
				getLog().debug("Response:\n" + x);
			}
		}

	}

	private void directDebitAmountReq(SOAPBodyElement operation, OutputStream output) throws Exception
	{

		String content =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<SOAP-ENV:Envelope" +
						" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
						" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
						" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
						" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
						" xmlns:osaxsd=\"http://www.csapi.org/osa/schema\"" +
						" xmlns:osa=\"http://www.csapi.org/osa/wsdl\"" +
						" xmlns:csxsd=\"http://www.csapi.org/cs/schema\"" +
						" xmlns:cs=\"http://www.csapi.org/cs/wsdl\">" +
						"<SOAP-ENV:Body SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
						"<cs:directDebitAmountReqResponse></cs:directDebitAmountReqResponse>" +
						"</SOAP-ENV:Body>" +
						"</SOAP-ENV:Envelope>";

		String http =
				"HTTP/1.1 200 OK\r\n" +
						"Date: Wed, 05 Apr 2006 10:49:04 GMT\r\n" +
						"Server: Apache/2.0.48 (Unix)\r\n" +
						"Content-Length: " + content.length() + "\r\n" +
						"Connection: close\r\n" +
						"Content-Type: text/xml\r\n" +
						"\r\n";

		String x = http + content;
		output.write(x.getBytes());
		output.flush();
		if (getLog().isDebugEnabled())
		{
			getLog().debug("Response:\n" + x);
		}

		MessageElement e = (MessageElement) operation.getChildElement(new QName("", "sessionID"));

		if (null == e)
		{
			throw new Exception("Session ID not found!\n" + operation.toString());
		}

		int sessionID = ((Integer) e.getValueAsType(null, Integer.class)).intValue();

		String ref;
		synchronized (map)
		{
			ref = (String) map.remove(new Integer(sessionID));
			if (null == ref)
				throw new Exception("Session ID not found!\n" +
						operation.toString());
		}

		SOAPEnvelope envelop = new SOAPEnvelope();
		SOAPBody body = (SOAPBody) envelop.getBody();

		bSuccess = !bSuccess;

		if (bSuccess)
		{

			SOAPBodyElement op = (SOAPBodyElement) body.addChildElement(
					"directDebitAmountRes", "cs", "http://www.csapi.org/cs/wsdl");
			((MessageElement) op.addChildElement("sessionID", "")).setObjectValue(new
					Integer(sessionID));
			((MessageElement) op.addChildElement("requestNumber", "")).setObjectValue(new Integer(0));
			op.addChildElement("debitedAmount", "");
			((MessageElement) op.addChildElement("requestNumberNextRequest", "")).setObjectValue(new Integer(0));
		}
		else
		{
			SOAPBodyElement op = (SOAPBodyElement) body.addChildElement(
					"directDebitAmountErr", "cs", "http://www.csapi.org/cs/wsdl");
			((MessageElement) op.addChildElement("sessionID", "")).setObjectValue(new Integer(sessionID));
			((MessageElement) op.addChildElement("requestNumber", "")).setObjectValue(new Integer(0));

			((MessageElement) op.addChildElement("error", "")).setObjectValue(TpChargingError.P_CHS_ERR_VOLUMES);

			((MessageElement) op.addChildElement("requestNumberNextRequest", "")).setObjectValue(new Integer(0));
		}

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Indicate request:\n" + envelop.toString());
		}

		SOAPEnvelope re = new RTBSMessageDispatcher(ref).invoke(null, envelop);

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Indicate response:\n" + re.toString());
		}
	}

	/**
	 * 
	 * @param operation
	 *            SOAPBodyElement
	 * @param output
	 *            OutputStream
	 * @throws Exception
	 */
	private void directCreditAmountReq(SOAPBodyElement operation, OutputStream output) throws Exception
	{

		String content =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<SOAP-ENV:Envelope" +
						" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
						" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
						" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
						" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
						" xmlns:osaxsd=\"http://www.csapi.org/osa/schema\"" +
						" xmlns:osa=\"http://www.csapi.org/osa/wsdl\"" +
						" xmlns:csxsd=\"http://www.csapi.org/cs/schema\"" +
						" xmlns:cs=\"http://www.csapi.org/cs/wsdl\">" +
						"<SOAP-ENV:Body SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
						"<cs:directCreditAmountReqResponse></cs:directCreditAmountReqResponse>" +
						"</SOAP-ENV:Body>" +
						"</SOAP-ENV:Envelope>";

		String http =
				"HTTP/1.1 200 OK\r\n" +
						"Date: Mon, 22 Dec 2008 08:06:56 GMT\r\n" +
						"Server: Apache/2.0.48 (Unix)\r\n" +
						"Content-Length: " + content.length() + "\r\n" +
						"Connection: close\r\n" +
						"Content-Type: text/xml\r\n" +
						"\r\n";

		String x = http + content;
		output.write(x.getBytes());
		output.flush();
		if (getLog().isDebugEnabled())
		{
			getLog().debug("Response:\n" + x);
		}

		MessageElement e = (MessageElement) operation.getChildElement(new QName("", "sessionID"));

		if (null == e)
		{
			throw new Exception("Session ID not found!\n" + operation.toString());
		}

		int sessionID = ((Integer) e.getValueAsType(null, Integer.class)).intValue();

		String ref;
		synchronized (map)
		{
			ref = (String) map.remove(new Integer(sessionID));
			if (null == ref)
				throw new Exception("Session ID not found!\n" + operation.toString());
		}

		SOAPEnvelope envelop = new SOAPEnvelope();
		SOAPBody body = (SOAPBody) envelop.getBody();

		bSuccess = !bSuccess;
		if (bSuccess)
		{
			SOAPBodyElement op = (SOAPBodyElement) body.addChildElement("directCreditAmountRes", "cs", "http://www.csapi.org/cs/wsdl");
			//
			((MessageElement) op.addChildElement("sessionID", "")).setObjectValue(new Integer(sessionID));
			//
			((MessageElement) op.addChildElement("requestNumber", "")).setObjectValue(new Integer(0));
			//
			op.addChildElement("creditedAmount", "");
			//
			((MessageElement) op.addChildElement("requestNumberNextRequest", "")).setObjectValue(new Integer(0));
		}
		else
		{
			SOAPBodyElement op = (SOAPBodyElement) body.addChildElement("directCreditAmountErr", "cs", "http://www.csapi.org/cs/wsdl");
			//
			((MessageElement) op.addChildElement("sessionID", "")).setObjectValue(new Integer(sessionID));
			//
			((MessageElement) op.addChildElement("requestNumber", "")).setObjectValue(new Integer(0));
			//
			((MessageElement) op.addChildElement("error", "")).setObjectValue(TpChargingError.P_CHS_ERR_VOLUMES);
			//
			((MessageElement) op.addChildElement("requestNumberNextRequest", "")).setObjectValue(new Integer(0));
		}

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Indicate request:\n" + envelop.toString());
		}

		SOAPEnvelope re = new RTBSMessageDispatcher(ref).invoke(null, envelop);

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Indicate response:\n" + re.toString());
		}

	}

	/**
	 * 
	 * @param operation
	 *            SOAPBodyElement
	 * @param output
	 *            OutputStream
	 * @throws Exception
	 */
	private void reserveAmountReq(SOAPBodyElement operation, OutputStream output) throws Exception
	{

		String content =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<SOAP-ENV:Envelope" +
						" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
						" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
						" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
						" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
						" xmlns:osaxsd=\"http://www.csapi.org/osa/schema\"" +
						" xmlns:osa=\"http://www.csapi.org/osa/wsdl\"" +
						" xmlns:csxsd=\"http://www.csapi.org/cs/schema\"" +
						" xmlns:cs=\"http://www.csapi.org/cs/wsdl\">" +
						"<SOAP-ENV:Body SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
						"<cs:reserveAmountReqResponse></cs:reserveAmountReqResponse>" +
						"</SOAP-ENV:Body>" +
						"</SOAP-ENV:Envelope>";

		String http =
				"HTTP/1.1 200 OK\r\n" +
						"Date: Mon, 22 Dec 2008 08:06:56 GMT\r\n" +
						"Server: Apache/2.0.48 (Unix)\r\n" +
						"Content-Length: " + content.length() + "\r\n" +
						"Connection: close\r\n" +
						"Content-Type: text/xml\r\n" +
						"\r\n";

		String x = http + content;
		output.write(x.getBytes());
		output.flush();
		if (getLog().isDebugEnabled())
		{
			getLog().debug("Response:\n" + x);
		}

		MessageElement e = (MessageElement) operation.getChildElement(new QName("", "sessionID"));

		if (null == e)
		{
			throw new Exception("Session ID not found!\n" + operation.toString());
		}
		int sessionID = ((Integer) e.getValueAsType(null, Integer.class)).intValue();

		MessageElement preferredAmount = operation.getChildElement(new QName("", "preferredAmount"));
		TpChargingPrice price = (TpChargingPrice) preferredAmount.getObjectValue(TpChargingPrice.class);
		double amount = price.getAmount().getNumber() * Math.pow(10, price.getAmount().getExponent());
		String currency = price.getCurrency();

		String ref;
		synchronized (map)
		{
			ref = (String) map.get(new Integer(sessionID));
			if (null == ref)
				throw new Exception("Session ID not found!\n" + operation.toString());
		}

		SOAPEnvelope envelop = new SOAPEnvelope();
		SOAPBody body = (SOAPBody) envelop.getBody();

		bSuccess = !bSuccess;
		if (bSuccess)
		{
			//
			SOAPBodyElement op = (SOAPBodyElement) body.addChildElement("reserveAmountRes", "cs", "http://www.csapi.org/cs/wsdl");
			// sessionID
			((MessageElement) op.addChildElement("sessionID", "")).setObjectValue(new Integer(sessionID));
			// requestNumber
			((MessageElement) op.addChildElement("requestNumber", "")).setObjectValue(new Integer(0));
			// reservedAmount
			((MessageElement) op.addChildElement("reservedAmount", "")).setObjectValue(new TpChargingPrice(currency, new TpAmount((int) amount, 0)));
			// sessionTimeLeft
			((MessageElement) op.addChildElement("sessionTimeLeft", "")).setObjectValue(new Integer(3600));
			// RTBS that cung la 3600s
			//
			((MessageElement) op.addChildElement("requestNumberNextRequest", "")).setObjectValue(new Integer(0));
		}
		else
		{
			SOAPBodyElement op = (SOAPBodyElement) body.addChildElement("reserveAmountErr", "cs", "http://www.csapi.org/cs/wsdl");
			//
			((MessageElement) op.addChildElement("sessionID", "")).setObjectValue(new Integer(sessionID));
			//
			((MessageElement) op.addChildElement("requestNumber", "")).setObjectValue(new Integer(0));
			//
			((MessageElement) op.addChildElement("error", "")).setObjectValue(TpChargingError.P_CHS_ERR_VOLUMES);
			//
			((MessageElement) op.addChildElement("requestNumberNextRequest", "")).setObjectValue(new Integer(0));
		}

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Indicate request:\n" + envelop.toString());
		}

		SOAPEnvelope re = new RTBSMessageDispatcher(ref).invoke(null, envelop);

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Indicate response:\n" + re.toString());
		}

	}

	/**
	 * 
	 * @param operation
	 *            SOAPBodyElement
	 * @param output
	 *            OutputStream
	 * @throws Exception
	 */
	private void debitAmountReq(SOAPBodyElement operation, OutputStream output) throws Exception
	{

		String content =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<SOAP-ENV:Envelope" +
						" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
						" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
						" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
						" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
						" xmlns:osaxsd=\"http://www.csapi.org/osa/schema\"" +
						" xmlns:osa=\"http://www.csapi.org/osa/wsdl\"" +
						" xmlns:csxsd=\"http://www.csapi.org/cs/schema\"" +
						" xmlns:cs=\"http://www.csapi.org/cs/wsdl\">" +
						"<SOAP-ENV:Body SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
						"<cs:debitAmountReqResponse></cs:debitAmountReqResponse>" +
						"</SOAP-ENV:Body>" +
						"</SOAP-ENV:Envelope>";

		String http =
				"HTTP/1.1 200 OK\r\n" +
						"Date: Mon, 22 Dec 2008 08:06:56 GMT\r\n" +
						"Server: Apache/2.0.48 (Unix)\r\n" +
						"Content-Length: " + content.length() + "\r\n" +
						"Connection: close\r\n" +
						"Content-Type: text/xml\r\n" +
						"\r\n";

		String x = http + content;
		output.write(x.getBytes());
		output.flush();
		if (getLog().isDebugEnabled())
		{
			getLog().debug("Response:\n" + x);
		}

		MessageElement e = (MessageElement) operation.getChildElement(new QName("", "sessionID"));
		if (null == e)
		{
			throw new Exception("Session ID not found!\n" + operation.toString());
		}
		int sessionID = ((Integer) e.getValueAsType(null, Integer.class)).intValue();

		String ref;
		synchronized (map)
		{
			ref = (String) map.remove(new Integer(sessionID));

			if (null == ref)
			{
				throw new Exception("Session ID not found!\n" + operation.toString());
			}
		}

		SOAPEnvelope envelop = new SOAPEnvelope();
		SOAPBody body = (SOAPBody) envelop.getBody();

		bSuccess = !bSuccess;

		if (bSuccess)
		{
			//
			SOAPBodyElement op = (SOAPBodyElement) body.addChildElement("debitAmountRes", "cs", "http://www.csapi.org/cs/wsdl");
			// sessionID
			((MessageElement) op.addChildElement("sessionID", "")).setObjectValue(new Integer(sessionID));
			// requestNumber
			((MessageElement) op.addChildElement("requestNumber", "")).setObjectValue(new Integer(0));
			// debitedAmount
			op.addChildElement("debitedAmount", "");
			// reservedAmountLeft
			op.addChildElement("reservedAmountLeft", "");
			//
			((MessageElement) op.addChildElement("requestNumberNextRequest", "")).setObjectValue(new Integer(0));
		}
		else
		{
			SOAPBodyElement op = (SOAPBodyElement) body.addChildElement("debitAmountErr", "cs", "http://www.csapi.org/cs/wsdl");
			//
			((MessageElement) op.addChildElement("sessionID", "")).setObjectValue(new Integer(sessionID));
			//
			((MessageElement) op.addChildElement("requestNumber", "")).setObjectValue(new Integer(0));
			//
			((MessageElement) op.addChildElement("error", "")).setObjectValue(TpChargingError.P_CHS_ERR_VOLUMES);
			//
			((MessageElement) op.addChildElement("requestNumberNextRequest", "")).setObjectValue(new Integer(0));
		}

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Indicate request:\n" + envelop.toString());
		}

		SOAPEnvelope re = new RTBSMessageDispatcher(ref).invoke(null, envelop);

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Indicate response:\n" + re.toString());
		}

	}

	private void release(SOAPBodyElement operation,
			OutputStream output) throws Exception
	{
		String content =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+
						"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:osaxsd=\"http://www.csapi.org/osa/schema\" xmlns:osa=\"http://www.csapi.org/osa/wsdl\" xmlns:csxsd=\"http://www.csapi.org/cs/schema\" xmlns:cs=\"http://www.csapi.org/cs/wsdl\">\n"
						+
						"<SOAP-ENV:Body SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
						"<cs:releaseResponse></cs:releaseResponse>\n" +
						"</SOAP-ENV:Body>\n" +
						"</SOAP-ENV:Envelope>";

		String http =
				"HTTP/1.1 200 OK\r\n" +
						"Date: Mon, 03 Apr 2006 14:19:59 GMT\r\n" +
						"Server: Apache/2.0.48 (Unix)\r\n" +
						"Content-Length: " + content.length() + "\r\n" +
						"Connection: close\r\n" +
						"Content-Type: text/xml\r\n" +
						"\r\n";

		String x = http + content;
		output.write(x.getBytes());
		output.flush();

		if (getLog().isDebugEnabled())
		{
			getLog().debug("Response:\n" + x);
		}
	}
}
