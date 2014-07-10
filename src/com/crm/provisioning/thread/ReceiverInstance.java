package com.crm.provisioning.thread;

import java.util.NoSuchElementException;

import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.Queue;

import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.impl.smpp.SMPPConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.CommandUtil;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;

import javax.jms.JMSException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: FPT
 * </p>
 * 
 * @author DatPX
 * @version 1.0 Purpose : Compute file : R4
 */

public class ReceiverInstance extends ProvisioningInstance
{
	// public Queue queueIncomeMessage = null;

	public ReceiverInstance() throws Exception
	{
		super();
	}

	@Override
	public void initQueue() throws Exception
	{

		super.initQueue();
	}

	/**
	 * process message
	 */
	public int processMessage(Message message) throws Exception
	{
		//QueueFactory.sendMessage(queueSession, queueWorking, message, message.getJMSExpiration());

		return Constants.BIND_ACTION_NONE;
	}

	@Override
	public Message detachMessage() throws Exception
	{
		Message message = null;
		SMPPConnection receiveConnection = null;
		CommandMessage request = null;
		try
		{
			// debugMonitor("Try to get smpp connection object.");
			receiveConnection = (SMPPConnection) getProvisioningConnection();
			request = receiveConnection.receive();
		}
		catch (InterruptedException e)
		{
			InterruptedException ie = new InterruptedException("Stop thread while thread is waiting for borrowing object.");
			ie.setStackTrace(e.getStackTrace());
			throw ie;
		}
		catch (NoSuchElementException e)
		{
			debugMonitor("Can not get idle object in pool due to instanceSize > maxActive in pool or can not make connection to server.");
		}
		catch (Exception e)
		{
			debugMonitor(e);
			throw e;
		}
		finally
		{
			if (receiveConnection != null)
				closeProvisioningConnection(receiveConnection);
		}

		try
		{
			if (request != null)
			{
				request.setTimeout(((SMPPThread) getDispatcher()).orderTimeout * 1000);
				
				MQConnection connection = null;
				
				try
				{
					connection = getMQConnection();
					message = connection.sendMessage(request, request.getCorrelationID(), queueWorking, request.getTimeout(), dispatcher.queuePersistent);
					
					debugMonitor("Sent to OrderRoute: " + request.toShortString());
				}
				catch (Exception ex)
				{
					logMonitor("Can not send to route, notify error: " + request.toShortString());
					CommandUtil.sendSMS(this, request, "He thong dang ban, quy khach vui long thu lai sau.");
				}
				finally
				{
					returnMQConnection(connection);
				}
			}
		}
		catch (Exception e)
		{
			debugMonitor(e);
			throw e;
		}

		request  = null;
		
		return message;
	}

}
