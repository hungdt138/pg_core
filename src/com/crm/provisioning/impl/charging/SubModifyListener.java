package com.crm.provisioning.impl.charging;

import java.util.HashMap;

import com.crm.thread.DispatcherThread;
import com.crm.util.StringPool;

public class SubModifyListener implements TCPDataListener
{
	protected HashMap<String, Object>	responseMap	= new HashMap<String, Object>();
	protected HashMap<String, Object>	notifyMap	= new HashMap<String, Object>();
	protected String					lastData	= "";
	protected DispatcherThread			dispatcher	= null;
	protected TCPClient					tcpClient	= null;

	@Override
	public void onReceive(byte[] data) throws Exception
	{
		String receiveData = new String(data);

		synchronized (lastData)
		{
			receiveData = lastData + receiveData;

			int endIndex = receiveData.indexOf(SubModifyConnection.SEPARATE_CHARS);
			int startIndex = 0;
			while (endIndex > 0)
			{
				String receive = receiveData.substring(startIndex, endIndex);
				try
				{
					//System.out.println("received: " + receive);

					String seq = receive.split(StringPool.COMMA)[0];
					Object notifiedObject = notifyMap.remove(seq);
					if (seq.length() < receive.length())
					{
						logMonitor(receive);
						responseMap.put(seq, receive);
						if (notifiedObject != null)
							synchronized (notifiedObject)
							{
								notifiedObject.notify();
							}
					}
				}
				catch (Exception e)
				{
				}

				startIndex = endIndex + SubModifyConnection.SEPARATE_CHARS.length();

				endIndex = receiveData.indexOf(SubModifyConnection.SEPARATE_CHARS, startIndex);
			}

			lastData = receiveData.substring(startIndex);
		}
	}

	public void setNotifiedObject(String seq, Object notifyObj)
	{
		// Object notifyObj = new Object();
		notifyMap.put(seq, notifyObj);
		// return notifyObj;
	}

	@Override
	public Object getResponse(String seq)
	{
		return responseMap.remove(seq);
	}

	@Override
	public void setDispatcher(DispatcherThread dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	@Override
	public DispatcherThread getDispatcher()
	{
		return dispatcher;
	}

	public void debugMonitor(String message)
	{
		if (dispatcher != null)
			dispatcher.debugMonitor(message);
	}

	public void logMonitor(String message)
	{
		if (dispatcher != null)
			dispatcher.logMonitor(message);
	}

	@Override
	public void setTCPClient(TCPClient client)
	{
		tcpClient = client;
	}
}
