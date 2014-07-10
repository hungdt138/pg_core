package com.crm.provisioning.thread.osa;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.message.OSACallbackMessage;
import com.crm.thread.DispatcherThread;
import com.crm.thread.util.ThreadUtil;

import com.fss.util.AppException;

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

public class OSACallbackThread extends DispatcherThread
{
	public ServerSocket									server				= null;

	public int											callbackPort		= 5000;
	public int											backLogSize			= 1000;
	public int											resultTimeout		= 60000;
	public int											logQueueInterval	= 10;

	// public ArrayList<OSACallbackMessage> responses = new
	// ArrayList<OSACallbackMessage>();
	public ConcurrentLinkedQueue<OSACallbackMessage>	responses			= new ConcurrentLinkedQueue<OSACallbackMessage>();

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(ThreadUtil.createIntegerParameter("callbackPort", ""));
		vtReturn.addElement(ThreadUtil.createIntegerParameter("backLogSize", "Backlog size."));
		vtReturn.addElement(ThreadUtil.createIntegerParameter("timeout", "time to live of result before expired (ms)."));
		vtReturn.addElement(ThreadUtil.createIntegerParameter("logQueueInterval",
				"Time interval to show local queue size, in second."));
		vtReturn.addAll(ThreadUtil.createQueueParameter(this));
		vtReturn.addAll(ThreadUtil.createInstanceParameter(this));
		vtReturn.addAll(ThreadUtil.createLogParameter(this));

		return vtReturn;
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillParameter() throws AppException
	{
		try
		{
			super.fillParameter();

			callbackPort = ThreadUtil.getInt(this, "callbackPort", 5000);
			backLogSize = ThreadUtil.getInt(this, "backLogSize", 1000);
			resultTimeout = ThreadUtil.getInt(this, "timeout", 60000);
			logQueueInterval = ThreadUtil.getInt(this, "logQueueInterval", 10);
		}
		catch (AppException e)
		{
			logMonitor(e);

			throw e;
		}
		catch (Exception e)
		{
			logMonitor(e);

			throw new AppException(e.getMessage());
		}
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void aferProcessSession() throws Exception
	{
		try
		{
			if ((server != null) && server.isClosed())
			{
				server.close();
			}
		}
		finally
		{
			super.afterProcessSession();
		}
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void attachMessage(OSACallbackMessage callbackContent) throws Exception
	{
		responses.offer(callbackContent);
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public OSACallbackMessage detachCallbackMessage() throws Exception
	{
		return responses.poll();
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void doProcessSession() throws Exception
	{
		try
		{
			// QueueFactory.closeQueue(queueConsumer);

			if ((server == null) || server.isClosed())
			{
				server = new ServerSocket(callbackPort, backLogSize);
			}

			CallbackServer callbackServer = new CallbackServer(server, this);

			callbackServer.start();

			long lastTimeCheck = System.currentTimeMillis();
			while (isAvailable())
			{
				if (lastTimeCheck + logQueueInterval * 1000 < System.currentTimeMillis())
				{
					lastTimeCheck = System.currentTimeMillis();
					logMonitor("Local queue size: " + responses.size());
				}
				Thread.sleep(100);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}
}
