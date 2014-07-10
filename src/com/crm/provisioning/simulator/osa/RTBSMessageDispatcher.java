/*
 * @(#)RTBSMessageDispatcher.java
 * Copyright 2006 ELCOM Software Solution, Corp. All rights reserved.
 */

package com.crm.provisioning.simulator.osa;

/**
 * Dispatch a SOAP message
 *
 * @author     <a href=mailto:tantn@elcom.com.vn>Tran Nhat Tan</a>
 * @version    1.0
 */

import java.net.URL;
import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.ContentHandlerFactory;
import java.net.ContentHandler;
import org.apache.axis.message.*;
import org.apache.axis.Message;

public class RTBSMessageDispatcher
{

	/**
	 * URL to dispatch
	 */
	private String	url;

	/**
	 * Default constructor
	 * 
	 * @param url
	 *            String
	 */
	public RTBSMessageDispatcher(String url)
	{
		this.url = url;
	}

	/**
	 * Get content from error stream
	 * 
	 * @throws IOException
	 * @return String
	 */
	private String getErrorContent(HttpURLConnection conn) throws IOException
	{
		int k = conn.getHeaderFieldInt("Content-Length", -1);

		if (-1 == k)
		{
			throw new IOException("Content-Length not found!");
		}

		byte[] buf = new byte[k];
		int i = 0;

		while (i < k)
		{
			int x = conn.getErrorStream().read(buf, i, k - i);

			if (-1 == x)
			{
				throw new IOException("Connection reset by peer!\n");
			}

			i += x;
		}
		return new String(buf);
	}

	private static final int	MAX_RETRY_TIMES	= 5;

	/**
	 * Invoke to dispatch a SOAP message and wait for returned SOAP message
	 * 
	 * @param envelope
	 *            SOAPEnvelope
	 * @return SOAPEnvelope
	 */
	public SOAPEnvelope invoke(String soapAction, SOAPEnvelope envelope) throws Exception
	{
		String content = envelope.toString();

		URL url = new URL(this.url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.addRequestProperty("Content-Type", "text/xml");
		conn.addRequestProperty("User-Agent", "gSOAP/2.7");
		conn.addRequestProperty("Accept", "text");
		conn.addRequestProperty("Connection", "close");
		conn.addRequestProperty("Content-Length", String.valueOf(content.length()));

		if (null != soapAction)
		{
			conn.addRequestProperty("SOAPAction", soapAction);
		}

		OutputStream output = conn.getOutputStream();
		output.write(content.getBytes());
		output.flush();

		int retry = 0;

		while (true)
		{
			try
			{
				conn.connect();
				break;
			}
			catch (BindException e)
			{
				retry++;

				if (MAX_RETRY_TIMES <= retry)
				{
					throw e;
				}
			}
		}

		try
		{
			Message message = new Message(200 == conn.getResponseCode() ? (String) conn.getContent() : getErrorContent(conn));
			
			return (SOAPEnvelope) message.getSOAPPart().getEnvelope();
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			conn.disconnect();
		}
	}

	static
	{
		HttpURLConnection.setContentHandlerFactory(new ContentHanderFactoryImpl());
	}

	/**
	 * Implementation of content hander factory
	 * <p>
	 * Title: HTC charging gateway solution
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2005
	 * </p>
	 * <p>
	 * Company: Elcom - JSC
	 * </p>
	 * 
	 * @author Mobile ONE group
	 * @version 1.0
	 */
	private static class ContentHanderFactoryImpl extends ContentHandler implements ContentHandlerFactory
	{
		public Object getContent(URLConnection urlc) throws IOException
		{
			int length = urlc.getContentLength();
			byte[] buf = new byte[length];
			int i = 0;
			while (i < length)
			{
				int x = urlc.getInputStream().read(buf, i, length - i);
				if (-1 == x)
					throw new IOException("Connection reset by peer!\n");
				i += x;
			}
			return new String(buf);
		}

		public ContentHandler createContentHandler(String mimetype)
		{
			return this;
		}

	}

}
