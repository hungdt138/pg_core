package com.crm.provisioning.thread.osa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.jms.Message;
import javax.jms.QueueSession;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.message.OSACallbackMessage;

public class OSACallbackHandlerForTest implements Runnable
{
	public static final String	DEBIT_METHOD		= "directDebitAmountRes";
	public static final String	CREDIT_METHOD		= "directCreditAmountRes";

	public static final String	TAG_NEXT_REQUEST	= "<requestNumberNextRequest>";

	private static int			MAX_HEADER_LENGTH	= 512;

	private Socket				socket;
	private OSACallbackThread	dispatcher;

	public OSACallbackHandlerForTest(Socket socket, OSACallbackThread dispatcher)
	{
		this.socket = socket;
		this.dispatcher = dispatcher;

		Thread t = new Thread(this);
		t.start();
	}

	private String readContent(int length, InputStream input) throws IOException
	{
		int i;
		int ch;

		byte[] buf = new byte[length];

		for (i = 0; i < length; i++)
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

	private String readHTTPHeader(InputStream input) throws IOException
	{
		int ch;
		int length = 0;

		byte[] buf = new byte[MAX_HEADER_LENGTH];

		while (MAX_HEADER_LENGTH > length)
		{
			ch = input.read();

			switch (ch)
			{
			case -1:
				throw new IOException("Connection reset by peer!");
			case '\r':
				break;
			case '\n':
				if (0 < length)
					if ('\n' == buf[length - 1])
						return new String(buf, 0, length);
			default:
				buf[length++] = (byte) ch;
			}
		}

		throw new IOException("HTTP message header too long!\n" + new String(buf, 0, length));
	}

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

	private void response(String actionType, OutputStream output) throws Exception
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
						"<SOAP-ENV:Body" +
						" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
						"<cs:" + actionType + "Response></cs:" + actionType + "Response>" +
						"</SOAP-ENV:Body>" +
						"</SOAP-ENV:Envelope>";

		TimeZone tz = TimeZone.getTimeZone("GMT:00");
		DateFormat dfGMT = DateFormat.getTimeInstance(DateFormat.LONG);
		dfGMT.setTimeZone(tz);

		String http =
				"HTTP/1.0 200 OK\r\n" +
						"Date: " + dfGMT.format(new Date()) + "\r\n" +
						"Content-Type: text/htlm\r\n" +
						"Connection: close\r\n" +
						"Content-Length: " + content.length() + "\r\n" +
						"\r\n"
						+ content;

		output.write(http.getBytes());

		output.flush();
	}

	public void run()
	{
		String actionType = "";

		InputStream is = null;
		OutputStream os = null;

		// QueueSession queueSession = null;

		try
		{
			//
			// Read a message sent by client application
			//

			is = socket.getInputStream();

			if (is == null)
			{
				return;
			}

			os = socket.getOutputStream();

			String message = "";

			try
			{
				String header = readHTTPHeader(is);

				dispatcher.debugMonitor(header);

				int length = getContentLength(header);

				message = readContent(length, is);
			}
			finally
			{
			}

			long receiveTime = System.currentTimeMillis();
			
			String sessionId = "";
			String cause = "";

			int indexStart = message.indexOf("<sessionID>");

			if (indexStart > 0)
			{
				sessionId =
						message.substring(
								indexStart + "<sessionID>".length(), message.indexOf("</sessionID>"));
			}

			if (message.indexOf("directDebitAmountErr") >= 0)
			{
				// process directDebitAmountErr
				indexStart = message.indexOf("<error>");
				if (indexStart > 0)
				{
					cause =
							message.substring(
									indexStart + "<error>".length(), message.indexOf("</error>"));
				}

				actionType = "directDebitAmountErr";
			}
			else if (message.indexOf("directDebitAmountRes") >= 0)
			{
				// process directDebitAmountErr
				indexStart = message.indexOf("<Number>");
				if (indexStart > 0)
				{
					cause =
							message.substring(
									indexStart + "<Number>".length(), message.indexOf("</Number>"));
				}

				actionType = "directDebitAmountRes";

				cause = Constants.SUCCESS;
			}
			else if (message.indexOf("directCreditAmountErr") >= 0)
			{
				// process directDebitAmountErr
				indexStart = message.indexOf("<error>");
				if (indexStart > 0)
				{
					cause =
							message.substring(
									indexStart + "<error>".length(), message.indexOf("</error>"));
				}

				actionType = "directCreditAmountErr";
			}
			else if (message.indexOf("directCreditAmountRes") >= 0)
			{
				// process directDebitAmountErr
				indexStart = message.indexOf("<Number>");
				if (indexStart > 0)
				{
					cause =
							message.substring(
									indexStart + "<Number>".length(), message.indexOf("</Number>"));
				}

				actionType = "directCreditAmountErr";

				cause = Constants.SUCCESS;
			}
			// process directDebitAmountErr - DuyMB Add 12/07/2011 sua loi OSA.

			String nextChargingSequence = "";

			indexStart = message.indexOf("<requestNumberNextRequest>");

			if (indexStart > 0)
			{
				nextChargingSequence =
						message.substring(
								indexStart + "<requestNumberNextRequest>".length(),
								message.indexOf("</requestNumberNextRequest>"));
			}
			// Add end 12/07/2011 sua loi OSA.

			long sentTime = Long.parseLong(nextChargingSequence);
			long costTime = receiveTime - sentTime;
			
			if (costTime > 120000)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				dispatcher.debugMonitor("SENT: " + sdf.format(new Date(sentTime))
						+ " - RECEIVED: " +sdf.format(new Date(receiveTime)));
			}
		}
		catch (Exception e)
		{
			dispatcher.logMonitor(e);
		}
		finally
		{
			// QueueFactory.closeQueue(queueSession);
			
			if ((os != null) && !actionType.equals(""))
			{
				try
				{
					response(actionType, os);

					dispatcher.debugMonitor("Return http_resp for actionType = " + actionType + " is success ");
				}
				catch (Exception e)
				{
					dispatcher.debugMonitor("error when return http_resp for actionType = " + actionType + ": " + e.getMessage());

					e.printStackTrace();
				}
			}

			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			if (socket != null)
			{
				try
				{
					socket.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
