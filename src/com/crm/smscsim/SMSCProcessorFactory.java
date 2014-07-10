package com.crm.smscsim;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.crm.ascs.net.NetThread;
import com.crm.smscsim.util.FileParser;
import com.crm.smscsim.util.SMSCProcessorCollection;
import com.crm.smscsim.util.SMSCUser;
import com.crm.thread.DispatcherThread;
import com.logica.smpp.Data;
import com.logica.smpp.pdu.BindRequest;
import com.logica.smpp.pdu.DeliverSM;
import com.logica.smpp.pdu.PDU;
import com.logica.smpp.pdu.SubmitSM;
import com.logica.smpp.util.Queue;

public class SMSCProcessorFactory extends NetThread implements IProcessorFactory
{
	private int									currentId		= 1;
	private SMSCProcessorCollection				processors		= new SMSCProcessorCollection();
	private ConcurrentHashMap<String, SMSCUser>	users			= new ConcurrentHashMap<String, SMSCUser>();
	private DispatcherThread					dispatcher		= null;
	private long								receiveTimeout	= 10000;
	private ConcurrentHashMap<String, Queue>	batchQueueMap	= new ConcurrentHashMap<String, Queue>();

	public SMSCProcessorFactory()
	{

	}
	
	public int getQueueSize(String systemId)
	{
		synchronized (batchQueueMap)
		{
			Queue queue = batchQueueMap.get(systemId);
			if (queue == null)
			{
				return 0;
			}
			return queue.size();
		}
	}

	public PDU dequeue(String systemId)
	{
		synchronized (batchQueueMap)
		{
			Queue queue = batchQueueMap.get(systemId);
			if (queue == null)
			{
				return null;
			}

			return (PDU) queue.dequeue();
		}
	}

	public void enqueue(PDU pdu, String systemId)
	{
		synchronized (batchQueueMap)
		{
			Queue queue = batchQueueMap.get(systemId);
			if (queue == null)
			{
				queue = new Queue();
			}
			queue.enqueue(pdu);
			batchQueueMap.put(systemId, queue);
		}
	}

	public int processorCount()
	{
		return processors.size();
	}

	public int receiverCount()
	{
		return processors.receiverCount();
	}

	public int transmitterCount()
	{
		return processors.transmitterCount();
	}

	public void setDispatcher(DispatcherThread dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	@Override
	public IProcessor createProcessor(ISession session)
	{
		SMSCProcessor processor = new SMSCProcessor(this, session);
		processor.setProcessorId(currentId++);
		return processor;
	}

	@Override
	public void debugMonitor(Object message)
	{
		if (dispatcher != null)
			dispatcher.debugMonitor(message);
	}

	@Override
	public int authenticate(IProcessor processor, BindRequest request)
	{
		String systemId = request.getSystemId();
		String password = request.getPassword();
		int commandId = request.getCommandId();

		int commandStatus = Data.ESME_ROK;
		SMSCUser user = users.get(systemId.toUpperCase());
		if (user != null)
		{
			if (!user.getPassword().equals(password))
			{
				commandStatus = Data.ESME_RINVPASWD;
				debugMonitor("system id " + systemId +
								" not authenticated. Invalid password.");
			}
			else
			{
				if (user.getConnectionLimit() > 0 && processors.count(user.getUserId()) >= user.getConnectionLimit())
				{
					commandStatus = Data.ESME_RBINDFAIL;
					debugMonitor("system id " + systemId + " not authenticated, has max connection.");
				}
				else if (commandId == Data.BIND_TRANSCEIVER && !user.isTranceiverEnabled())
				{
					commandStatus = Data.ESME_RBINDFAIL;
					debugMonitor("system id " + systemId + " not authenticated, tranceiver is not allowed to bind.");
				}
				else if (commandId == Data.BIND_TRANSMITTER && !user.isTransmitterEnabled())
				{
					commandStatus = Data.ESME_RBINDFAIL;
					debugMonitor("system id " + systemId + " not authenticated, transmitter is not allowed to bind.");
				}
				else if (commandId == Data.BIND_RECEIVER && !user.isReceiverEnabled())
				{
					commandStatus = Data.ESME_RBINDFAIL;
					debugMonitor("system id " + systemId + " not authenticated, receiver is not allow to bind.");
				}
				else
				{
					debugMonitor("system id " + systemId + " authenticated");
					processor.setUser(user);
					processors.addProcessor(processor);
					((SMSCProcessor)processor).start();
				}
			}
		}
		else
		{
			commandStatus = Data.ESME_RINVSYSID;
			debugMonitor("system id " + systemId +
						" not authenticated -- not found");
		}

		return commandStatus;
	}

	/**
	 * Set default 1 user: nms/nms
	 */
	public void loadUsers()
	{
		SMSCUser user = new SMSCUser("nms", "nms", 0);
		users.put(user.getUserId().toUpperCase(), user);
	}

	public void loadUsers(String filePath)
	{
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(filePath);
			FileParser parser = new FileParser(users);
			parser.parse(is);
		}
		catch (Exception e)
		{
			debugMonitor(e);
			loadUsers();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (Exception e)
			{

			}
		}
	}

	public void sendMessage(String systemId, DeliverSM deliverSM) throws SMSCException
	{
		processors.send(deliverSM, systemId);
	}

	public void sendMessage(DeliverSM[] deliverSMs)
	{
		processors.sendBroadcast(deliverSMs);
	}

	@Override
	public void setReceiveTimeout(long timeout)
	{
		receiveTimeout = timeout;
		setSleepTime(timeout);
	}

	@Override
	public long getReceiveTimeout()
	{
		return receiveTimeout;
	}

	@Override
	public void process() throws Exception
	{
		processors.revalidate();
	}

	@Override
	public IProcessor stopProcessor(IProcessor processor)
	{
		return processors.stopProcessor(processor);
	}

	@Override
	public void stopAllProcessor()
	{
		processors.stopAllProcessors();
	}

}
