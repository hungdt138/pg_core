package com.crm.ascs.collector.thread;

import javax.jms.Message;

import com.crm.ascs.collector.Trigger;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.thread.DispatcherInstance;

public class CollectorInstance extends DispatcherInstance
{

	public CollectorInstance() throws Exception
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Message detachMessage() throws Exception
	{
		Trigger trigger = ((CollectorThread) getDispatcher()).getWork();
		if (trigger != null)
		{
			MQConnection connection = null;
			try
			{
				int triggerTimeout = ((CollectorThread) getDispatcher()).triggerTimeout;
				connection = getMQConnection();
				Message message = connection.sendMessage(trigger, queueWorking, triggerTimeout, dispatcher.queuePersistent);
				return message;
			}
			finally
			{
				returnMQConnection(connection);
			}

		}

		return null;
	}

	@Override
	public int processMessage(Message message) throws Exception
	{
		return Constants.BIND_ACTION_NONE;
	}

}
