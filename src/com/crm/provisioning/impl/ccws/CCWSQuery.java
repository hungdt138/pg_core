package com.crm.provisioning.impl.ccws;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.AxisFault;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.comverse_in.prepaid.ccws.ArrayOfBalanceEntity;
import com.comverse_in.prepaid.ccws.ArrayOfRechargeHistory;
import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.RechargeHistory;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;
import com.crm.util.AppProperties;

public class CCWSQuery
{
	private static String	dateFormat	= "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ";

	public static String stringFromDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	public static Date dateFromString(String date) throws ParseException
	{
		date = date.substring(0, date.length() - 3) + "00";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		return sdf.parse(date);
	}

	private static String doRequestWithHistory(String host, int port, String path, String userName, String password,
			String isdn, int queryLevel, Date startTime, Date endTime, int timeout) throws IOException
	{
		StringBuilder strXml = new StringBuilder();

		String strStartDate = stringFromDate(startTime);
		String strEndDate = stringFromDate(endTime);

		/**
		 * Append xmlData
		 */
		strXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strXml.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
		strXml.append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ");
		strXml.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		strXml.append("<soapenv:Header>");
		strXml
				.append("<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" soapenv:mustUnderstand=\"1\">");
		strXml
				.append("<wsse:UsernameToken xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"UsernameToken-19105669\">");
		strXml.append("<wsse:Username>");
		strXml.append(userName);
		strXml.append("</wsse:Username>");
		strXml
				.append("<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">");
		strXml.append(password);
		strXml.append("</wsse:Password>");
		strXml.append("</wsse:UsernameToken>");
		strXml.append("</wsse:Security>");
		strXml.append("</soapenv:Header>");
		strXml.append("<soapenv:Body>");
		strXml.append("<RetrieveSubscriberWithIdentityWithHistoryForMultipleIdentities xmlns=\"http://comverse-in.com/prepaid/ccws\">");
		strXml.append("<subscriberID>" + isdn + "</subscriberID>");
		strXml.append("<informationToRetrieve>" + queryLevel + "</informationToRetrieve>");
		strXml.append("<startDate>" + strStartDate + "</startDate>");
		strXml.append("<endDate>" + strEndDate + "</endDate>");
		strXml.append("<IsAllIdentity>false</IsAllIdentity>");
		strXml.append("</RetrieveSubscriberWithIdentityWithHistoryForMultipleIdentities>");
		strXml.append("</soapenv:Body>");
		strXml.append("</soapenv:Envelope>");

		int length = strXml.length();
		/**
		 * Add Header
		 */
		StringBuilder sbContent = new StringBuilder();
		sbContent.append("POST " + path + " HTTP/1.0\r\n");
		sbContent.append("Host: " + host + ":" + port + "\r\n");
		sbContent
				.append("SOAPAction: \"http://comverse-in.com/prepaid/ccws/RetrieveSubscriberWithIdentityWithHistoryForMultipleIdentities\"\r\n");
		sbContent.append("Content-Length: " + length + "\r\n");
		sbContent.append("Content-Type: text/xml; charset=\"utf-8\"\r\n");
		sbContent.append("\r\n");
		sbContent.append(strXml);

		return doRequest(host, port, sbContent.toString(), timeout);
	}

	private static String doRequestWithNoHistory(String host, int port, String path, String userName, String password,
			String isdn,
			int queryLevel, int timeout) throws IOException
	{
		StringBuilder strXml = new StringBuilder();

		/**
		 * Append xmlData
		 */
		strXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		strXml.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
		strXml.append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ");
		strXml.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		strXml.append("<soapenv:Header>");
		strXml
				.append("<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" soapenv:mustUnderstand=\"1\">");
		strXml
				.append("<wsse:UsernameToken xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" wsu:Id=\"UsernameToken-19105669\">");
		strXml.append("<wsse:Username>");
		strXml.append(userName);
		strXml.append("</wsse:Username>");
		strXml
				.append("<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">");
		strXml.append(password);
		strXml.append("</wsse:Password>");
		strXml.append("</wsse:UsernameToken>");
		strXml.append("</wsse:Security>");
		strXml.append("</soapenv:Header>");
		strXml.append("<soapenv:Body>");
		strXml.append("<RetrieveSubscriberWithIdentityNoHistory xmlns=\"http://comverse-in.com/prepaid/ccws\">");
		strXml.append("<subscriberID>" + isdn + "</subscriberID>");
		strXml.append("<identity xsi:nil=\"true\"></identity>");
		strXml.append("<informationToRetrieve>" + queryLevel + "</informationToRetrieve>");
		strXml.append("</RetrieveSubscriberWithIdentityNoHistory>");
		strXml.append("</soapenv:Body>");
		strXml.append("</soapenv:Envelope>");

		int length = strXml.length();
		/**
		 * Add Header
		 */
		StringBuilder sbContent = new StringBuilder();
		sbContent.append("POST " + path + " HTTP/1.0\r\n");
		sbContent.append("Host: " + host + ":" + port + "\r\n");
		sbContent.append("SOAPAction: \"http://comverse-in.com/prepaid/ccws/RetrieveSubscriberWithIdentityNoHistory\"\r\n");
		sbContent.append("Content-Length: " + length + "\r\n");
		sbContent.append("Content-Type: text/xml; charset=\"utf-8\"\r\n");
		sbContent.append("\r\n");
		sbContent.append(strXml);

		return doRequest(host, port, sbContent.toString(), timeout);
	}

	private static String doRequest(String host, int port, String content, int timeout) throws IOException
	{
		Socket sock = null;
		try
		{
			InetAddress addrIP = InetAddress.getByName(host);
			sock = new Socket(addrIP, port);
			if (timeout > 0)
			{
				sock.setSoTimeout(timeout);
			}

			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(sock.
					getOutputStream(), "UTF-8"));

			wr.write(content);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(sock.
					getInputStream()));
			String response = "";
			String line;
			while ((line = rd.readLine()) != null)
			{
				response += line + "\r\n";
			}

			/**
			 * remove last \r\n
			 */
			response = response.substring(0, response.length() - 2);
			return response;
		}
		finally
		{
			if (sock != null)
			{
				try
				{
					sock.close();
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	private static String getTextValue(Element ele, String tagName)
	{
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0)
		{
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	private static Document parseResponse(String response) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		String xmlContent = response;

		xmlContent = xmlContent.substring(xmlContent.indexOf("<soap:Body>"));
		xmlContent = xmlContent.replace("</soap:Envelope>", "");
		
		xmlContent = xmlContent.replace("<Balance><Balance>", "<Balance><TotalBalance>");
		xmlContent = xmlContent.replace("</Balance></Balance>", "</TotalBalance></Balance>");
		xmlContent = xmlContent.replace("</Balance><AccountExpiration>", "</TotalBalance><AccountExpiration>");
		
		xmlContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + xmlContent;

		// System.out.println(xmlContent);
		Document document = builder.parse(new InputSource(new StringReader(xmlContent)));

		NodeList nodeList = document.getElementsByTagName("soap:Fault");
		
		if (nodeList != null)
		{
			if (nodeList.getLength() > 0)
			{
				AxisFault fault = null;

				try
				{
					fault = new AxisFault();
					Element element = (Element) nodeList.item(0);
					// String faultCode =
					// element.getElementsByTagName("faultcode").item(0).getNodeValue();
					String faultString = getTextValue(element, "faultstring");
					String detail = element.getElementsByTagName("detail").item(0).getTextContent();

					// fault.setFaultCode(faultCode);
					fault.setFaultString(faultString);
					fault.setFaultDetailString(detail);

				}
				catch (Exception e)
				{
					fault = new AxisFault();
					fault.setFaultString("CCWS Internal Server Error");
					fault.setFaultDetailString(nodeList.item(0).getTextContent());
				}
				throw fault;
			}
		}

		return document;
	}

	public static SubscriberRetrieve getActivationDate(String isdn, String host, String userName, String password, int timeout)
			throws Exception
	{
		try
		{
			SubscriberRetrieve subRetrieve = new SubscriberRetrieve();
			subRetrieve.setSubscriberID(isdn);
			SubscriberEntity subData = new SubscriberEntity();
			subRetrieve.setSubscriberData(subData);

			URL url = new URL(host);
			String hostName = url.getHost();
			String path = url.getPath();
			int port = url.getPort();
			if (port <= 0)
				port = 80;

			int queryLevel = 1;
			String response = doRequestWithNoHistory(hostName, port, path, userName,
					password, isdn, queryLevel, timeout);

			Document doc = parseResponse(response);

			Element activeDateElement = (Element) doc.getElementsByTagName("DateEnterActive").item(0);

			Date dateEnterActive = dateFromString(activeDateElement.getTextContent());
			Calendar dateEnterActiveTime = Calendar.getInstance();
			dateEnterActiveTime.setTime(dateEnterActive);
			subData.setDateEnterActive(dateEnterActiveTime);

			return subRetrieve;
			// if (response != null)
			// {
			// trigger.setActivationDate(subRetrieve.getSubscriberData().getDateEnterActive().getTime());
			//
			// return Trigger.STATUS_APPROVED;
			// }
			//
			// return Trigger.STATUS_FAILURE;
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	public static SubscriberRetrieve getRechargeNoHistory(String isdn, String host, String userName, String password, int timeout)
			throws Exception
	{
		try
		{
			SubscriberRetrieve subRetrieve = new SubscriberRetrieve();
			subRetrieve.setSubscriberID(isdn);
			SubscriberEntity subData = new SubscriberEntity();
			subRetrieve.setSubscriberData(subData);

			URL url = new URL(host);
			String hostName = url.getHost();
			String path = url.getPath();
			int port = url.getPort();
			if (port <= 0)
				port = 80;

			int queryLevel = 1;
			String response = doRequestWithNoHistory(hostName, port, path, userName,
					password, isdn, queryLevel, timeout);

			Document doc = parseResponse(response);

			BalanceEntity[] balEntity = null;
			Element balancesElement = (Element) doc.getElementsByTagName("Balances").item(0);
			NodeList nodeListBalance = balancesElement.getElementsByTagName("Balance");
			if(nodeListBalance != null && nodeListBalance.getLength() > 0)
			{
				balEntity = new BalanceEntity[nodeListBalance.getLength()];
				for (int i = 0; i < nodeListBalance.getLength(); i++)
				{
					balEntity[i] = new BalanceEntity();
					Element balance = (Element)nodeListBalance.item(i);
					
					//set total balance
					Element totalBalanceElement = (Element) balance.getElementsByTagName("TotalBalance").item(0);
					double totalBalance = Double.parseDouble(totalBalanceElement.getTextContent());
					balEntity[i].setBalance(totalBalance);
					
					//set account expire
					Element accountExpireElement = (Element) balance.getElementsByTagName("AccountExpiration").item(0);
					Date expire = dateFromString(accountExpireElement.getTextContent());
					Calendar accountExpire = Calendar.getInstance();
					accountExpire.setTime(expire);
					balEntity[i].setAccountExpiration(accountExpire);
					
					//set balance name
					Element balanceNameElement = (Element) balance.getElementsByTagName("BalanceName").item(0);
					String balanceName = balanceNameElement.getTextContent();
					balEntity[i].setBalanceName(balanceName);
					
					//set available balance
					Element availableBalanceElement = (Element) balance.getElementsByTagName("AvailableBalance").item(0);
					double availableBalance = Double.parseDouble(availableBalanceElement.getTextContent());
					balEntity[i].setAvailableBalance(availableBalance);
				}
			}
			else
			{
				balEntity = new BalanceEntity[0]; 
			}
			
			subData.setBalances(new ArrayOfBalanceEntity(balEntity));

			return subRetrieve;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public static SubscriberRetrieve getRechargeHistory(String isdn, String host, String userName, String password,
			Calendar startTime, Calendar endTime, int timeout)
			throws Exception
	{
		try
		{
			SubscriberRetrieve subRetrieve = new SubscriberRetrieve();
			subRetrieve.setSubscriberID(isdn);
			SubscriberEntity subData = new SubscriberEntity();
			subRetrieve.setSubscriberData(subData);
			ArrayOfRechargeHistory aoRechargeHistory = new ArrayOfRechargeHistory();
			subRetrieve.setRechargeHistories(aoRechargeHistory);

			URL url = new URL(host);
			String hostName = url.getHost();
			String path = url.getPath();
			int port = url.getPort();
			if (port <= 0)
				port = 80;

			int queryLevel = 1 + 512;
			String response = doRequestWithHistory(hostName, port, path, userName,
					password, isdn, queryLevel, startTime.getTime(), endTime.getTime(), timeout);

			Document doc = parseResponse(response);

			/**
			 * Date enter active
			 */
			Element activeDateElement = (Element) doc.getElementsByTagName("DateEnterActive").item(0);

			Date dateEnterActive = dateFromString(activeDateElement.getTextContent());
			Calendar dateEnterActiveTime = Calendar.getInstance();
			dateEnterActiveTime.setTime(dateEnterActive);
			subData.setDateEnterActive(dateEnterActiveTime);

			/**
			 * Previous State
			 */
			Element previousStateElement = (Element) doc.getElementsByTagName("PreviousState").item(0);

			if (previousStateElement != null)
			{
				subData.setPreviousState(previousStateElement.getTextContent());
			}
			else
			{
				subData.setPreviousState("");
			}

			/**
			 * Recharge history list
			 */

			Element rechargeHistoryElement = (Element) doc.getElementsByTagName("RechargeHistories").item(0);

			if (rechargeHistoryElement != null)
			{

				RechargeHistory[] rechargeHistories = getRechargeHistoryFromElement(rechargeHistoryElement);

				aoRechargeHistory.setRechargeHistory(rechargeHistories);
			}

			return subRetrieve;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	private static RechargeHistory[] getRechargeHistoryFromElement(Element rechargeHistoryElement) throws DOMException,
			ParseException
	{
		NodeList nodeList = rechargeHistoryElement.getElementsByTagName("RechargeHistory");

		if (nodeList == null)
		{
			return new RechargeHistory[0];
		}

		RechargeHistory[] rechargeHistories = new RechargeHistory[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			/**
			 * Only get things that ASCS needs: RechargeDate SerialNumber
			 * BatchNumber FaceValue Balance(Core)
			 */

			Element element = (Element) nodeList.item(i);
			rechargeHistories[i] = new RechargeHistory();

			/**
			 * Recharge Date
			 */
			Element rechargeDateElement = (Element) element.getElementsByTagName("RechargeDate").item(0);
			Date rechargeDate = dateFromString(rechargeDateElement.getTextContent());
			Calendar rechargeTime = Calendar.getInstance();
			rechargeTime.setTime(rechargeDate);
			rechargeHistories[i].setRechargeDate(rechargeTime);

			/**
			 * Serial number
			 */
			Element serialNumberElement = (Element) element.getElementsByTagName("SerialNumber").item(0);
			long serialNumber = Long.parseLong(serialNumberElement.getTextContent());
			rechargeHistories[i].setSerialNumber(serialNumber);

			/**
			 * Batch number
			 */
			Element batchNumberElement = (Element) element.getElementsByTagName("BatchNumber").item(0);
			long batchNumber = Long.parseLong(batchNumberElement.getTextContent());
			rechargeHistories[i].setBatchNumber(batchNumber);

			/**
			 * Face Value
			 */
			Element faceValueElement = (Element) element.getElementsByTagName("FaceValue").item(0);
			double faceValue = Double.parseDouble(faceValueElement.getTextContent());
			rechargeHistories[i].setFaceValue(faceValue);

			/**
			 * Balance (Core only)
			 */
			NodeList balanceNodeList = element.getElementsByTagName("Balance");
			for (int j = 0; j < balanceNodeList.getLength(); j++)
			{
				Element balanceElement = (Element) balanceNodeList.item(j);
				Element balanceNameElement = (Element) balanceElement.getElementsByTagName("BalanceName").item(0);
				String balanceName = balanceNameElement.getTextContent();

				if (!balanceName.equals("Core"))
				{
					continue;
				}
				else
				{
					BalanceEntity balanceEntity = new BalanceEntity();
					balanceEntity.setBalanceName(balanceName);

					Element availBalanceElement = (Element) balanceElement.getElementsByTagName("AvailableBalance").item(0);
					Double availableBalance = Double.parseDouble(availBalanceElement.getTextContent());
					balanceEntity.setAvailableBalance(availableBalance);

					rechargeHistories[i].setBalances(new ArrayOfBalanceEntity(new BalanceEntity[] { balanceEntity }));
					break;
				}
			}

		}
		return rechargeHistories;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception
	{
		String strUserName = "NMS";
		String strPassword = "nms!23";
		String strHostName = "10.8.13.140";
		String strPath = "/ccws/ccws.asmx";
		int port = 80;

		URL url = new URL("http://10.8.13.140/ccws/ccws.asmx");
		strHostName = url.getHost();
		strPath = url.getPath();
		port = url.getPort();
		if (port <= 0)
			port = 80;

		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		startTime.add(Calendar.HOUR_OF_DAY, -36);
		endTime.add(Calendar.HOUR_OF_DAY, 1);

		long start = System.currentTimeMillis();

		// SubscriberRetrieve subRetrieve = getActivationDate("84922000514",
		// "http://10.8.13.140/ccws/ccws.asmx", strUserName,
		// strPassword);
		SubscriberRetrieve subRetrieve = getRechargeHistory("84922000514", "http://localhost:8000/ccws/ccws.asmx", strUserName,
				strPassword, startTime, endTime, 5000);

		long end = System.currentTimeMillis();

		System.out.println("Cost: " + (end - start));

		System.out.println("For debug.");
		// retrieveSubscriberWithIdentityWithHistory("84922000514", 1 + 512,
		// startTime.getTime(), endTime.getTime());
		// String date = "2014-12-31T23:59:59.0000000+07:00";
		// date = date.substring(0, date.length() - 3) + "00";
		// SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		// System.out.println(sdf.parse(date).toGMTString());
	}

}
