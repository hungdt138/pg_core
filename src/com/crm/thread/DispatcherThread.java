package com.crm.thread;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.crm.kernel.message.AlarmMessage;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.cache.MQConnectionPool;
import com.crm.thread.util.ThreadUtil;
import com.fss.util.AppException;

import com.fss.thread.ManageableThread;
import com.fss.thread.ThreadConstant;

public class DispatcherThread extends ManageableThread
{
	private Object									queueLockedObject		= new Object();

	// //////////////////////////////////////////////////////
	// Thread parameters
	// //////////////////////////////////////////////////////
	public boolean									loadCacheEnable			= false;
	public boolean									alarmEnable				= false;

	public boolean									instanceEnable			= false;
	public String									instanceClass			= "";
	public int										instanceSize			= 0;

	public boolean									neverExpire				= false;

	public boolean									queueDispatcherEnable	= false;
	public boolean									queueInstanceEnable		= false;
	public boolean									temporaryQueue			= false;
	public int										queueMode				= Constants.QUEUE_MODE_MANUAL;
	public String									queuePrefix				= "";
	public String									queueName				= "";
	public String									queueSelector			= "";

	public boolean									logEnable				= false;
	public boolean									displayDebug			= false;
	public String									logLevel				= "error";
	public String									logClass				= "";
	public Logger									log						= Logger.getLogger(DispatcherThread.class);

	public SimpleDateFormat							dateFormat				= new SimpleDateFormat("dd/MM/yyyy");

	public Vector<DispatcherInstance>				instances				= new Vector<DispatcherInstance>();

	public static ConcurrentHashMap<String, Long>	tpsMap					= new ConcurrentHashMap<String, Long>();

	// //////////////////////////////////////////////////////
	// Queue variables
	// //////////////////////////////////////////////////////

	public Queue									queueWorking			= null;
	public MQConnectionPool							queueConnectionPool		= null;
	public int										queueConnectionPoolSize	= 10;
	public int										queuePoolWaitTime		= 1000;
	public boolean 									queuePersistent 		= true;

	// //////////////////////////////////////////////////////
	// result of process variables
	// //////////////////////////////////////////////////////
	public int										totalCount				= 0;
	public int										successCount			= 0;
	public int										errorCount				= 0;
	public int										bypassCount				= 0;
	public int										insertCount				= 0;
	public int										updateCount				= 0;
	public int										exportCount				= 0;
	public String									minStamp				= "";
	public String									maxStamp				= "";

	// //////////////////////////////////////////////////////
	// batch variables
	// //////////////////////////////////////////////////////
	public int										batchSize				= 1;
	public int										batchCount				= 0;

	// //////////////////////////////////////////////////////
	// error variables
	// //////////////////////////////////////////////////////
	public Exception								error					= null;
	public String									lastError				= "";

	// //////////////////////////////////////////////////////
	// other variables
	// //////////////////////////////////////////////////////
	public long										sequenceValue			= 0;

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addAll(ThreadUtil.createInstanceParameter(this));
		vtReturn.addAll(ThreadUtil.createQueueParameter(this));
		vtReturn.addAll(ThreadUtil.createLogParameter(this));

		return vtReturn;
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = getDispatcherDefinition();

		vtReturn.addAll(super.getParameterDefinition());

		return vtReturn;
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillDispatcherParameter() throws AppException
	{
		try
		{
			loadCacheEnable = ThreadUtil.getBoolean(this, "loadCacheEnable", false);

			alarmEnable = ThreadUtil.getBoolean(this, "alarmEnable", false);

			neverExpire = ThreadUtil.getBoolean(this, "neverExpire", false);

			// get instance setting
			instanceEnable = ThreadUtil.getBoolean(this, "instanceEnable", false);

			if (instanceEnable)
			{
				instanceClass = ThreadUtil.getString(this, "instanceClass", true, "");

				instanceSize = ThreadUtil.getInt(this, "instanceSize", 1);
			}

			// get log setting
			logEnable = ThreadUtil.getBoolean(this, "logEnable", false);

			if (logEnable)
			{
				displayDebug = ThreadUtil.getBoolean(this, "displayDebug", false);

				logClass = ThreadUtil.getString(this, "logClass", true, "");

				log = Logger.getLogger(logClass);

				// setting log level
				logLevel = ThreadUtil.getString(this, "logLevel", true, "error");

				Level level = Level.ERROR;

				if (logLevel.equals(Constants.LOG_LEVEL_OFF))
				{
					level = Level.OFF;
				}
				else if (logLevel.equals(Constants.LOG_LEVEL_DEBUG))
				{
					level = Level.DEBUG;
				}
				else if (logLevel.equals(Constants.LOG_LEVEL_ERROR))
				{
					level = Level.ERROR;
				}
				else if (logLevel.equals(Constants.LOG_LEVEL_FATAL))
				{
					level = Level.FATAL;
				}
				else if (logLevel.equals(Constants.LOG_LEVEL_INFO))
				{
					level = Level.INFO;
				}
				else if (logLevel.equals(Constants.LOG_LEVEL_TRACE))
				{
					level = Level.TRACE;
				}
				else if (logLevel.equals(Constants.LOG_LEVEL_WARN))
				{
					level = Level.WARN;
				}

				log.setLevel(level);
			}

			// get queue setting
			queueDispatcherEnable = ThreadUtil.getBoolean(this, "queueDispatcherEnable", false);

			queueInstanceEnable = ThreadUtil.getBoolean(this, "queueInstanceEnable", false);

			if (queueDispatcherEnable || queueInstanceEnable)
			{
				temporaryQueue = ThreadUtil.getBoolean(this, "temporaryQueue", false);

				String mode = ThreadUtil.getString(this, "queueMode", true, "manual");

				if (mode.equalsIgnoreCase("manual"))
				{
					queueMode = Constants.QUEUE_MODE_MANUAL;
				}
				else if (mode.equalsIgnoreCase("consumer"))
				{
					queueMode = Constants.QUEUE_MODE_CONSUMER;
				}
				else if (mode.equalsIgnoreCase("producer"))
				{
					queueMode = Constants.QUEUE_MODE_PRODUCER;
				}
				else
				{
					throw new AppException("unknow-queue-mode");
				}

				queuePrefix = ThreadUtil.getString(this, "queuePrefix", false, "queue");

				queueName = ThreadUtil.getString(this, "queueName", false, "");

				queueSelector = ThreadUtil.getString(this, "queueSelector", false, "");

				queueConnectionPoolSize = ThreadUtil.getInt(this, "queuePoolSize", 5);
				queuePoolWaitTime = ThreadUtil.getInt(this, "queuePoolWaitTime", 1000);
				queuePersistent = ThreadUtil.getBoolean(this, "queuePersistent", true);
			}

			// date format
			String pattern = ThreadUtil.getString(this, "dateFormat", false, "dd/MM/yyyy");

			dateFormat = new SimpleDateFormat(pattern);
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
		finally
		{
		}
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillParameter() throws AppException
	{
		super.fillParameter();

		fillDispatcherParameter();
	}

	public int getDelayTime()
	{
		return miDelayTime;
	}

	public Logger getLog()
	{
		return log;
	}

	public boolean isAvailable()
	{
		return (miThreadCommand != ThreadConstant.THREAD_STOP && !mmgrMain.isServerLocked());
	}

	public void setInstances(Vector<DispatcherInstance> instances)
	{
		this.instances = instances;
	}

	public Vector<DispatcherInstance> getInstances()
	{
		return instances;
	}

	// ////////////////////////////////////////////////////////////////////////
	// load directory parameters
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public void logMonitor(String strLog, boolean bSendMail)
	{
		if (bSendMail)
		{
			alertByMail(strLog);
		}

		final java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("dd/MM HH:mm:ss:SSS");

		strLog = fmt.format(new java.util.Date()) + " " + strLog + "\r\n";

		log(strLog);
		
		strLog = null;
	}

	// ////////////////////////////////////////////////////////////////////////
	// load directory parameters
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public void logMonitor(Object message)
	{
		Exception error = null;

		try
		{
			if (message instanceof Exception)
			{
				error = (Exception) message;

				log.error(error);

				if (!(message instanceof AppException))
				{
					error.printStackTrace();
				}
			}

			if (error instanceof InvocationTargetException)
			{
				Throwable target = ((InvocationTargetException) message).getTargetException();

				logMonitor(target.getMessage());
			}
			else if (message instanceof AppException)
			{
				AppException appException = (AppException) message;

				if (appException.getContext() != null)
				{
					logMonitor(appException.getMessage() + " : " + appException.getContext());
				}
				else
				{
					logMonitor(appException.getMessage());
				}
			}
			else if (message instanceof Exception)
			{
				Exception e = (Exception) message;

				logMonitor(e.getClass().getName() + ": " + e.getMessage());
			}
			else
			{
				logMonitor(message.toString());
			}
		}
		catch (Exception e)
		{
			// logMonitor(e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// load directory parameters
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public void debugMonitor(Object message)
	{
		try
		{
			if (displayDebug || (message instanceof Exception))
			{
				logMonitor(message);
			}

			if (log.isDebugEnabled())
			{
				log.debug(message);
			}
		}
		catch (Exception e)
		{

		}
	}

	// //////////////////////////////////////////////////////
	// process file
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void processAlarm(Serializable alarm)
	{
		if (alarmEnable)
		{
			MQConnection queueConnection = null;
			try
			{
				queueConnection = getMQConnection();

				queueConnection.sendMessage(alarm, 60000, QueueFactory.ALARM_QUEUE, 60000, queuePersistent);
			}
			catch (Exception e)
			{
				logMonitor(e);
			}
			finally
			{
				returnMQConnection(queueConnection);
			}
		}
	}

	public void sendAlarmMessage(AlarmMessage alarm)
	{
		processAlarm(alarm);
	}

	public void sendAlarmMessage(Exception e, String cause, long provisioningId, String provisioningClass)
	{
		if (alarmEnable)
		{
			AlarmMessage alarm = new AlarmMessage();
			alarm.setCause(cause);
			alarm.setProvisioningId(provisioningId);
			alarm.setProvisioningClass(provisioningClass);

			StackTraceElement[] stackTraces = e.getStackTrace();
			String content = "";
			for (StackTraceElement stackTrace : stackTraces)
			{
				content += stackTrace.toString() + "\r\n";
			}

			alarm.setDescription(e.getMessage());
			alarm.setContent(content);

			sendAlarmMessage(alarm);
		}
	}

	// //////////////////////////////////////////////////////
	// reset counter
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void resetCounter() throws Exception
	{
		totalCount = 0;
		successCount = 0;
		errorCount = 0;
		bypassCount = 0;
		insertCount = 0;
		updateCount = 0;

		minStamp = "";
		maxStamp = "";
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
			if (!queueDispatcherEnable)
			{
				return null;
			}

			if ((queueMode == Constants.QUEUE_MODE_CONSUMER))
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
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void loadCache() throws Exception
	{

	}

	/**
	 * Init queue Author : ThangPV Created Date : 16/09/2004 Edited by NamTA
	 * 
	 * @throws Exception
	 */
	public void initQueue() throws Exception
	{
		try
		{
			InitQueuePool();
			if (!"".equals(queueName))
				queueWorking = QueueFactory.getQueue(queueName);
		}
		catch (NamingException ne)
		{
			QueueFactory.resetContext();

			throw ne;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public synchronized void InitQueuePool() throws Exception
	{

		if (queueConnectionPool == null)
			queueConnectionPool = new MQConnectionPool(this, queueConnectionPoolSize, queuePoolWaitTime,
					queueConnectionPoolSize);

		if (queueConnectionPool.isClosed())
		{
			queueConnectionPool.open();

			// Test and initial at least 1 MQConnection
			MQConnection connection = null;

			try
			{
				connection = getMQConnection();
			}
			finally
			{
				returnMQConnection(connection);
			}
		}
	}
	
	public Connection getConnection()
	{
		try
		{
			return Database.getConnection();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public MQConnection getMQConnection() throws Exception
	{
		return queueConnectionPool.getConnection();
	}

	public void returnMQConnection(MQConnection connection)
	{
		if (queueConnectionPool != null)
			queueConnectionPool.returnConnection(connection);
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		try
		{
			logMonitor("Starting process session");

			if (loadCacheEnable)
			{
				loadCache();
			}

			resetCounter();

			if (queueDispatcherEnable)
			{
				initQueue();
			}

			getInstances().clear();

			checkInstance(false);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void destroyQueuePool()
	{
		try
		{
			if (queueConnectionPool != null)
				queueConnectionPool.destroyPool();
		}
		catch (Exception e)
		{
			debugMonitor(e);
		}
		finally
		{
			// queueConnectionPool = null;
		}
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void afterProcessSession() throws Exception
	{
		try
		{
			for (int j = 0; j < getInstances().size(); j++)
			{
				getInstances().elementAt(j).stop();
			}
		}
		catch (Exception e)
		{
			logMonitor(e);
		}
		finally
		{
			destroyQueuePool();
			Database.closeObject(mcnMain);

			logMonitor("End of process session");
		}
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public boolean isRunning() throws Exception
	{
		for (int j = 0; j < getInstances().size(); j++)
		{
			if (getInstances().elementAt(j).isRunning())
			{
				return true;
			}
		}

		return false;
	}

	public boolean isAutoLoop()
	{
		return neverExpire;
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void checkInstance(boolean autoStart) throws Exception
	{
		if (instanceEnable)
		{
			int started = 0;
			int added = 0;

			if (instanceSize <= 0)
			{
				instanceSize = 1;
			}

			for (int j = 0; isAvailable() && (j < instanceSize); j++)
			{
				DispatcherInstance instance = null;

				if (j < getInstances().size())
				{
					instance = getInstances().elementAt(j);
				}

				if (instance == null)
				{
					instance = (DispatcherInstance) Class.forName(instanceClass).newInstance();

					instance.setDispatcher(this);

					if (j < getInstances().size())
					{
						getInstances().set(j, instance);
					}
					else
					{
						getInstances().add(instance);
					}
				}

				if (!instance.isRunning() && autoStart)
				{
					instance.start();

					added++;
					
					Thread.sleep(100);
				}
				else
				{
					started++;
				}
			}

			if (added > 0)
			{
				logMonitor("Total " + started + " instances are started");
				logMonitor("Total " + added + " instances are added");
			}
		}
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void doProcessSession() throws Exception
	{
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void processSession() throws Exception
	{
		try
		{
			beforeProcessSession();

			while (isAvailable())
			{
				checkInstance(true);

				doProcessSession();

				if (!isAutoLoop() && !isRunning())
				{
					break;
				}
				else if (isAvailable())
				{
					Thread.sleep(getDelayTime());
				}
			}
		}
		catch (Exception e)
		{
			// sendAlarmMessage(e);

			logMonitor(e);
		}
		finally
		{
			afterProcessSession();
		}
	}
}
