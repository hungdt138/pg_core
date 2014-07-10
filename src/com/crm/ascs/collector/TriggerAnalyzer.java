package com.crm.ascs.collector;

import com.crm.ascs.net.INetAnalyzer;
import com.crm.ascs.net.INetDataCollection;
import com.crm.ascs.net.INetHandler;
import com.crm.util.StringUtil;

public class TriggerAnalyzer implements INetAnalyzer
{
	private static final String		SEPARATE_CHARS	= ";";

	private String					lastData		= "";
	private TriggerClientHandler	handler			= null;

	@Override
	public void createObject(Object data, INetDataCollection collection)
	{
		byte[] bytes = (byte[])data;
		String receiveData = new String(bytes);
		String hexData = StringUtil.toHexString(bytes, 0, bytes.length);
		
		debugMonitor("RECEIVE from #" + handler.getHandlerId() + ": " + hexData + "[ASCII:" + receiveData + "]");
		synchronized (lastData)
		{
			receiveData = lastData + receiveData;

			int endIndex = receiveData.indexOf(SEPARATE_CHARS);
			int startIndex = 0;
			while (endIndex > 0)
			{
				String receive = receiveData.substring(startIndex, endIndex);
				try
				{
					Trigger trigger = new Trigger();
					trigger.setContent(receive);
					collection.put(trigger);
				}
				catch (Exception e)
				{
				}

				startIndex = endIndex + SEPARATE_CHARS.length();

				endIndex = receiveData.indexOf(SEPARATE_CHARS, startIndex);
			}

			lastData = receiveData.substring(startIndex);
		}
	}

	public void debugMonitor(Object message)
	{
		if (handler != null)
			handler.debugMonitor(message);
	}

	@Override
	public void setHandler(INetHandler handler)
	{
		this.handler = (TriggerClientHandler)handler;
	}

	@Override
	public INetHandler getHandler()
	{
		return handler;
	}
}
