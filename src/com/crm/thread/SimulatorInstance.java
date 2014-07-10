package com.crm.thread;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.OSACallbackMessage;
import com.crm.provisioning.thread.CommandRoutingThread;
import com.crm.thread.DispatcherInstance;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright:
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author: HungPQ
 * @version 1.0
 */

public class SimulatorInstance extends DispatcherInstance
{
	// //////////////////////////////////////////////////////
	// Queue variables
	// //////////////////////////////////////////////////////
	public Queue queueCallback = null;

	public SimulatorInstance() throws Exception
	{
		super();
	}

	public SimulatorThread getDispatcher()
	{
		return (SimulatorThread) dispatcher;
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void doProcessSession() throws Exception
	{
		MQConnection connection = null;
		MessageProducer producer = null;
		try
		{
			int totalSent = 0;
			int count = 0;
			long currentIsdn = getDispatcher().getCurrentIsdn();

			logMonitor("Begin to send message.");

			connection = getMQConnection();

			while ((currentIsdn > 0) && isAvailable())
			{
				CommandMessage order = new CommandMessage();

				order.setChannel(getDispatcher().channel);

				if (getDispatcher().channel.equals(Constants.CHANNEL_SMS))
				{
					order.setProvisioningType("SMSC");
				}

				order.setUserId(0);

				if (getDispatcher().deliveryUser.equals(""))
					order.setUserName("system");
				else
					order.setUserName(getDispatcher().deliveryUser);

				order.setServiceAddress(getDispatcher().serviceAddress);
				order.setIsdn(String.valueOf(currentIsdn));
				order.setShipTo(getDispatcher().shipTo);
				order.setTimeout(getDispatcher().orderTimeout * 1000);

				order.setKeyword(getDispatcher().keyword);
				
				Message message= QueueFactory.createObjectMessage(connection.getSession(), order);
				message.setStringProperty("SystemID", order.getUserName());
				if (producer == null)
				{
					producer = QueueFactory.createQueueProducer(connection.getSession(),
							queueWorking, order.getTimeout(),
							dispatcher.queuePersistent);
				}

				producer.send(message);

				// if (displayDebug)
				logMonitor(order.toLogString());

				currentIsdn = getDispatcher().getCurrentIsdn();
				count++;

				if (count >= getDispatcher().batchSize)
				{
					totalSent += count;
					logMonitor("message count: " + totalSent);

					Thread.sleep(getDispatcher().timeBetweenLoop);

					count = 0;
				}
			}
		}
		catch (Exception e)
		{
			if(e instanceof JMSException)
			{
				connection.markError();
			}
			throw e;
		}
		finally
		{
			QueueFactory.closeQueue(producer);
			//QueueFactory.closeQueue(session);
			returnMQConnection(connection);
		}
	}
}
