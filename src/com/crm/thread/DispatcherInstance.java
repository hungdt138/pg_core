package com.crm.thread;

import java.io.Serializable;
import java.sql.Connection;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageFormatException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ResourceAllocationException;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.crm.kernel.message.AlarmMessage;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.cache.MQConnectionPool;
import com.crm.provisioning.message.CommandMessage;
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
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Phan Viet Thang<br>
 *         Edited by NamTA Edited Date 21/08/2012 Edited Description: Change
 *         logMonitor to debugMonitor in run method
 * @version 1.0
 */

public class DispatcherInstance implements Runnable
{
	// //////////////////////////////////////////////////////
	// Member variables
	// //////////////////////////////////////////////////////
	private Thread			thread			= null;
	private boolean			running			= false;
	private boolean			stopRequested	= true;

	public DispatcherThread	dispatcher		= null;

	// //////////////////////////////////////////////////////
	// Queue variables
	// //////////////////////////////////////////////////////
	public Queue			queueWorking	= null;

	// //////////////////////////////////////////////////////
	// connection variables
	// //////////////////////////////////////////////////////
	public Connection		mcnMain			= null;

	// //////////////////////////////////////////////////////
	// error variables
	// //////////////////////////////////////////////////////
	public Exception		error			= null;

	public DispatcherInstance() throws Exception
	{
	}

	public void setDispatcher(DispatcherThread dispatcher) throws Exception
	{
		if (dispatcher == null)
		{
			throw new AppException("dispatcher-can-not-be-null");
		}

		this.dispatcher = dispatcher;
	}

	public DispatcherThread getDispatcher()
	{
		return dispatcher;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}

	public Logger getLog()
	{
		return (dispatcher == null) ? null : dispatcher.getLog();
	}

	// ////////////////////////////////////////////////////////////////////////
	// load directory parameters
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public void logMonitor(Object message)
	{
		getDispatcher().logMonitor(message);
	}

	// ////////////////////////////////////////////////////////////////////////
	// load directory parameters
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public void debugMonitor(Object message)
	{
		getDispatcher().debugMonitor(message);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Start thread
	 * 
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void start()
	{
		// Destroy previous if it's constructed
		destroy();

		// Start new thread
		thread = new Thread(this);
		stopRequested = false;

		thread.start();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Start thread
	 * 
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void stop()
	{
		destroy();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Destroy thread
	 * 
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void destroy()
	{
		try
		{
			stopRequested = true;

			Thread.sleep(100);

			//dispatcher.destroyQueuePool();

			if ((thread != null) && !thread.isInterrupted())
			{
				Thread tmpThread = thread;
				thread = null;

				if (tmpThread != null)
				{
					tmpThread.interrupt();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			Database.closeObject(mcnMain);
		}
	}

	public boolean isAvailable()
	{
		Thread thisThread = Thread.currentThread();

		return (!stopRequested && (thread == thisThread) && getDispatcher().isAvailable());
	}

	public boolean isNeverExpire()
	{
		return dispatcher.isAutoLoop();
	}

	public void sendMessage(Queue queue, Serializable object, String jmsCorrelationID, long timeout) throws Exception
	{
		MQConnection connection = null;
		try
		{
			connection = getMQConnection();
			
			connection.sendMessage(object, jmsCorrelationID, queue, timeout, dispatcher.queuePersistent);

		}
		catch (ResourceAllocationException e)
		{
			logMonitor(e);

			throw new AppException(Constants.ERROR_RESOURCE_BUSY);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			returnMQConnection(connection);
		}
	}

	public void sendMessage(Queue queue, Serializable object, long timeout) throws Exception
	{
		sendMessage(queue, object, "", timeout);
	}

	public void sendMessage(String queueName, Serializable object, String jmsCorrelationID, long timeout) throws Exception
	{
		try
		{
			Queue queue = QueueFactory.getQueue(queueName);

			if (queue != null)
			{
				sendMessage(queue, object, jmsCorrelationID, timeout);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void sendMessage(String queueName, Serializable object, long timeout) throws Exception
	{
		sendMessage(queueName, object, "", timeout);
	}

	public void sendAlarm(CommandMessage request)
	{
		dispatcher.processAlarm(request);
	}

	public void sendInstanceAlarm(Serializable alarm)
	{
		if (getDispatcher().alarmEnable)
		{
			MQConnection connection = null;
			try
			{
				connection = getMQConnection();
				
				connection.sendMessage(alarm, QueueFactory.ALARM_QUEUE, 60000, dispatcher.queuePersistent);
			}
			catch (Exception e)
			{
				logMonitor(e);
			}
			finally
			{
				returnMQConnection(connection);
			}
		}
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public Connection getConnection() throws Exception
	{
		if ((mcnMain == null) || mcnMain.isClosed())
		{
			mcnMain = Database.getConnection();
		}

		return mcnMain;
	}

	public MQConnection getMQConnection() throws Exception
	{
		return dispatcher.getMQConnection();
	}

	public void returnMQConnection(MQConnection connection)
	{
		dispatcher.returnMQConnection(connection);
	}

	public void initQueue() throws Exception
	{
		try
		{
			dispatcher.InitQueuePool();

			if (!dispatcher.queueName.equals(""))
			{
				if (dispatcher.temporaryQueue)
				{
					MQConnection connection = null;
					try
					{
						connection = getMQConnection();
						queueWorking = connection.createTempQueue(dispatcher.queueName);
					}
					finally
					{
						returnMQConnection(connection);
					}
				}
				else
				{
					queueWorking = QueueFactory.getQueue(dispatcher.queueName);
				}
			}
		}
		catch (NamingException e)
		{
			QueueFactory.resetContext();

			throw e;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		try
		{
			if (dispatcher.queueInstanceEnable)
			{
				initQueue();
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void afterProcessSession() throws Exception
	{
		try
		{
			// dispatcher.destroyQueuePool();
			// getDispatcher().resetQueueConnection();
			Database.closeObject(mcnMain);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int exportData(Message message) throws Exception
	{
		return Constants.BIND_ACTION_SUCCESS;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int exportError(Message message, String error) throws Exception
	{
		return Constants.BIND_ACTION_SUCCESS;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int exportError(Message message, Exception error) throws Exception
	{
		return Constants.BIND_ACTION_SUCCESS;
	}

	public int processMessage(Message message) throws Exception
	{
		return Constants.BIND_ACTION_NONE;
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public Message detachMessage() throws Exception
	{
		Message message = null;

		try
		{
			if (!dispatcher.queueInstanceEnable || dispatcher.temporaryQueue)
			{
				return null;
			}

			if (dispatcher.queueMode == Constants.QUEUE_MODE_CONSUMER)
			{
				MQConnection connection = null;
				try
				{
					connection = getMQConnection();
					message = connection.detachMessage();
				}
				finally
				{
					returnMQConnection(connection);
				}
			}

			return message;
		}
		catch (Exception e)
		{
			// getDispatcher().resetQueueConnection();
			throw e;
		}
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
			Message message = detachMessage();

			while (isAvailable() && (message != null))
			{
				int action = Constants.BIND_ACTION_SUCCESS;

				try
				{
					action = processMessage(message);
				}
				catch (MessageFormatException e)
				{
					logMonitor(e);

					action = Constants.BIND_ACTION_ERROR;
				}
				catch (Exception e)
				{
					throw e;
				}

				if (action == Constants.BIND_ACTION_SUCCESS)
				{
					dispatcher.successCount++;
				}
				else if (action == Constants.BIND_ACTION_EXPORT)
				{
					exportData(message);

					dispatcher.exportCount++;
				}
				else if (action == Constants.BIND_ACTION_ERROR)
				{
					exportError(message, error);

					dispatcher.errorCount++;
				}
				else if (action == Constants.BIND_ACTION_BYPASS)
				{
					dispatcher.bypassCount++;
				}
				
				message = null;

				message = detachMessage();
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public void run()
	{
		setRunning(true);

		try
		{
			beforeProcessSession();

			while (isAvailable())
			{
				doProcessSession();

				if (!isNeverExpire() && !isRunning())
				{
					break;
				}
				else
				{
					ThreadUtil.sleep(dispatcher);
				}
			}
		}
		catch (com.sun.messaging.jms.IllegalStateException e)
		{
			// debugMonitor("Consumer was closed, reconnect.");
		}
		catch (JMSException e)
		{
			debugMonitor(e);
			debugMonitor("Consumer was closed, reconnect.");
		}
		catch (Exception e)
		{
			debugMonitor(e);
		}
		finally
		{
			try
			{
				afterProcessSession();
			}
			catch (Exception e)
			{
				logMonitor(e);
			}

			setRunning(false);
		}
	}
}
