package com.crm.smscsim.util;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import com.crm.smscsim.IProcessor;
import com.crm.smscsim.SMSCException;
import com.logica.smpp.Data;
import com.logica.smpp.pdu.Request;

public class SMSCProcessorCollection
{
	private ConcurrentHashMap<String, SMSCProcessorGroup>	nameMap			= new ConcurrentHashMap<String, SMSCProcessorGroup>();
	private SMSCProcessorGroup								processorGroup	= new SMSCProcessorGroup();
	private Object											lockObject		= new Object();
	private int												transmitterSize	= 0;
	private int												receiverSize	= 0;

	public int size()
	{
		synchronized (lockObject)
		{
			return processorGroup.size();
		}
	}

	public int receiverCount()
	{
		synchronized (lockObject)
		{
			return receiverSize;
		}
	}

	public int transmitterCount()
	{
		synchronized (lockObject)
		{
			return transmitterSize;
		}
	}

	public void addProcessor(IProcessor processor)
	{
		synchronized (lockObject)
		{
			SMSCProcessorGroup processors = getProcessors(processor.getSystemId());
			if (processors == null)
				processors = new SMSCProcessorGroup();
			processors.add(processor);
			nameMap.put(processor.getSystemId(), processors);
			processorGroup.add(processor);

			if (processor.getBindType() == Data.BIND_TRANSCEIVER)
			{
				transmitterSize++;
				receiverSize++;
			}
			else if (processor.getBindType() == Data.BIND_RECEIVER)
			{
				receiverSize++;
			}
			else if (processor.getBindType() == Data.BIND_TRANSMITTER)
			{
				transmitterSize++;
			}

		}
	}

	public IProcessor removeProcessor(IProcessor processor)
	{
		synchronized (lockObject)
		{
			SMSCProcessorGroup processors = getProcessors(processor.getSystemId());
			if (processors != null)
				processors.remove(processor);
			if (processorGroup.remove(processor))
			{
				if (processor.getBindType() == Data.BIND_TRANSCEIVER)
				{
					transmitterSize--;
					receiverSize--;
				}
				else if (processor.getBindType() == Data.BIND_RECEIVER)
				{
					receiverSize--;
				}
				else if (processor.getBindType() == Data.BIND_TRANSMITTER)
				{
					transmitterSize--;
				}

				return processor;
			}
			return null;
		}
	}

	public SMSCProcessorGroup removeProcessors(String systemId)
	{
		synchronized (lockObject)
		{
			SMSCProcessorGroup processors = nameMap.remove(systemId);
			if (processors != null)
			{
				for (int i = 0; i < processors.size(); i++)
				{

					IProcessor processor = processors.get(i);
					if (processorGroup.remove(processor))
					{
						if (processor.getBindType() == Data.BIND_TRANSCEIVER)
						{
							transmitterSize--;
							receiverSize--;
						}
						else if (processor.getBindType() == Data.BIND_RECEIVER)
						{
							receiverSize--;
						}
						else if (processor.getBindType() == Data.BIND_TRANSMITTER)
						{
							transmitterSize--;
						}
					}
				}
			}
			return processors;
		}
	}

	public String[] getSystemIds()
	{
		synchronized (lockObject)
		{
			return nameMap.keySet().toArray(new String[] {});
		}
	}

	private SMSCProcessorGroup getProcessors(String systemId)
	{
		return nameMap.get(systemId);
	}

	public int count(String systemId)
	{
		synchronized (lockObject)
		{
			SMSCProcessorGroup processors = getProcessors(systemId);
			if (processors != null)
				return processors.size();
			return 0;
		}
	}

	public IProcessor get(int index)
	{
		synchronized (lockObject)
		{
			return processorGroup.get(index);
		}
	}
	
	public void send(Request request, String systemId) throws SMSCException
	{
		if (request == null)
			return;
		SMSCProcessorGroup processors = getProcessors(systemId);
		if (processors != null)
		{
			IProcessor processor = processors.borrowReceiverProcessor();

			try
			{
				if (processor == null)
					throw new SMSCException("[" + systemId + "] does not have any receiver.");
				sendToProcessor(processor, request);
			}
			finally
			{
				processors.returnProcessor(processor);
			}
		}
	}

	public void sendBroadcast(Request[] requests)
	{
		if (requests == null)
			return;
		Iterator<String> t = nameMap.keySet().iterator();
		while (t.hasNext())
		{
			SMSCProcessorGroup processors = getProcessors(t.next());
			int count = 0;
			while (count < requests.length && processors != null)
			{
				IProcessor processor = processors.borrowReceiverProcessor();

				try
				{
					sendToProcessor(processor, requests[count++]);
				}
				finally
				{
					processors.returnProcessor(processor);
				}
			}
		}

	}

	public void sendBroadcast(Request request) throws Exception
	{
		sendBroadcast(new Request[] { request });
	}

	private void sendToProcessor(IProcessor processor, Request request)
	{
		try
		{
			synchronized (processor)
			{
				processor.serverRequest(request);
			}
		}
		catch (Exception e)
		{
			processor.disconnect();
		}
	}

	public IProcessor stopProcessor(IProcessor processor)
	{
		IProcessor returnProcessor = removeProcessor(processor);

		processor.stop();

		return returnProcessor;
	}

	public void stopAllProcessors()
	{
		for (int i = 0; i < processorGroup.size(); i++)
		{
			IProcessor processor = processorGroup.get(i);
			if (processor != null)
				removeProcessor(processor);
			processor.stop();
		}
	}

	public void revalidate()
	{
		for (int i = 0; i < processorGroup.size(); i++)
		{
			IProcessor processor = processorGroup.get(i);
			if (processor != null)
				if (!processor.validate())
				{
					stopProcessor(processor);
				}
		}
	}
}
