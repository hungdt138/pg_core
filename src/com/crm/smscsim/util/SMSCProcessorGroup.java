package com.crm.smscsim.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.crm.smscsim.IProcessor;
import com.logica.smpp.Data;
import com.logica.smpp.pdu.Request;

public class SMSCProcessorGroup
{
	private Object				lockObject	= new Object();
	private List<IProcessor>	processors	= new ArrayList<IProcessor>();
	private List<IProcessor>	pool		= new ArrayList<IProcessor>();

	public int size()
	{
		synchronized (lockObject)
		{
			return processors.size();
		}
	}

	public void add(IProcessor processor)
	{
		synchronized (lockObject)
		{
			processors.add(processor);
			pool.add(processor);
		}
	}

	public boolean remove(IProcessor processor)
	{
		synchronized (lockObject)
		{
			pool.remove(processor);
			return processors.remove(processor);
		}
	}

	public IProcessor get(int index)
	{
		synchronized (lockObject)
		{
			try
			{
				return processors.get(index);
			}
			catch (IndexOutOfBoundsException e)
			{
				return null;
			}
		}
	}

	public IProcessor borrowProcessor()
	{
		synchronized (lockObject)
		{
			return pool.remove(0);
		}
	}

	public IProcessor borrowReceiverProcessor()
	{
		synchronized (lockObject)
		{
			IProcessor processor = null;
			for (int i = 0; i < pool.size(); i++)
			{
				processor = pool.get(i);
				if (processor.getBindType() == Data.BIND_RECEIVER
						|| processor.getBindType() == Data.BIND_TRANSCEIVER)
				{
					pool.remove(i);
					break;
				}
				else
					processor = null;
			}
			
			return processor;
		}
	}

	public void returnProcessor(IProcessor processor)
	{
		if (!processor.validate())
			return;
		synchronized (lockObject)
		{
			if (processor != null)
				pool.add(processor);
		}
	}
}
