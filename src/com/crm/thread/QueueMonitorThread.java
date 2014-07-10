package com.crm.thread;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.jms.Queue;

import org.apache.log4j.Logger;

import com.crm.kernel.message.AlarmMessage;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.MQConnection;
import com.crm.thread.util.ThreadUtil;
import com.crm.util.StringPool;

import com.fss.thread.ParameterType;
import com.fss.thread.ParameterUtil;
import com.fss.util.AppException;
import com.fss.util.StringUtil;

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

public class QueueMonitorThread extends DispatcherThread
{
	protected String[] queueList = new String[0];
	protected int maxSize = 10000;
	protected int warningSize = 1000;
	protected String warningDiskPath = "";
	protected int warningDiskPercent = 10;

	protected long lastGarbage = System.currentTimeMillis();

	protected static Logger log = Logger.getLogger(QueueMonitorThread.class);

	protected StringBuilder alarmMessage = null;
	
	private SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();

		// data source connection
		vtReturn.addElement(createParameterDefinition("queueList", "",
				ParameterType.PARAM_TEXTBOX_MAX, "",
				"Indicates that default max queue size"));

		vtReturn.addElement(createParameterDefinition("warningSize", "",
				ParameterType.PARAM_TEXTBOX_MAX, "", "Warning size"));

		vtReturn.addElement(createParameterDefinition("maxSize", "",
				ParameterType.PARAM_TEXTBOX_MAX, "", "Max size"));
		vtReturn.add(ThreadUtil.createTextParameter("diskPath", 400,
				"Path of warning disk."));
		vtReturn.add(ThreadUtil.createIntegerParameter("warningDiskPercent",
				"Percent of in used disk space need to warning if reach."));

		Vector vtYesNo = new Vector();
		vtYesNo.addElement("Y");
		vtYesNo.addElement("N");

		vtReturn.addElement(ParameterUtil.createParameterDefinition(
				"alarmEnable", "", ParameterType.PARAM_COMBOBOX, vtYesNo,
				"Never expire session", "1"));

		vtReturn.addAll(ThreadUtil.createQueueParameter(this));
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

			maxSize = ThreadUtil.getInt(this, "maxSize", 10000);

			warningSize = ThreadUtil.getInt(this, "warningSize", 1000);

			queueList = StringUtil.toStringArray(
					ThreadUtil.getString(this, "queueList", false, ""),
					StringPool.SEMICOLON);

			warningDiskPath = ThreadUtil
					.getString(this, "diskPath", false, "/");

			warningDiskPercent = ThreadUtil.getInt(this, "warningDiskPercent",
					10);
		}
		catch (AppException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new AppException(e.getMessage());
		}

	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// Modified by NamTA
	// Modified Date 27/08/2012
	// //////////////////////////////////////////////////////
	protected String queueWarning(Queue checkQueue, String name, int size)
			throws Exception
	{
		if (size > warningSize)
		{
			String queueWarning = "";

			if (size >= maxSize)
			{
				queueWarning = "FATAL: queue " + name
						+ " is reach to limitation (" + size + "/" + maxSize
						+ ")\r\n";
			}
			else
			{
				queueWarning = "WARNING: queue " + name
						+ " may be reach to limitation (" + size + "/"
						+ maxSize + ")\r\n";
			}

			logMonitor(queueWarning);
			// alarmMessage += warningMessage;

			return queueWarning;
		}
		return "";
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// Modified by NamTA
	// Modified Date 27/08/2012
	// //////////////////////////////////////////////////////
	protected boolean memoryLogging() throws Exception
	{
		Runtime runtime = Runtime.getRuntime();

		/**
		 * Removed garbage function, changed to use in JVM options.
		 */
		// garbage
		// if ((lastGarbage + 10 * 60 * 1000) < System.currentTimeMillis())
		// {
		// log.info(sb.toString());
		// logMonitor(sb.toString());
		//
		// sb.append("Cleaning memory ...\n");
		//
		// runtime.gc();
		//
		// lastGarbage = System.currentTimeMillis();
		//
		// log.info("Cleaned memory ...");
		// logMonitor("Cleaned memory ...");
		// }

		// write log
		NumberFormat format = NumberFormat.getInstance();

		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		File file = new File(warningDiskPath);
		long totalDiskSize = file.getTotalSpace();
		long usableDiskSize = file.getUsableSpace();
		long usedDiskSize = totalDiskSize - usableDiskSize;
		long percentUsedDisk = 100 * (usedDiskSize) / totalDiskSize;

		alarmMessage.append(sdf.format(new Date()) +"\r\n");
		alarmMessage.append("Memory information:\r\n");
		alarmMessage.append("\t :: Free memory            : ");
		alarmMessage.append(format.format(freeMemory / 1024 / 1024));
		alarmMessage.append(" MB\r\n");
		alarmMessage.append("\t :: Allocated memory       : ");
		alarmMessage.append(format.format(allocatedMemory / 1024 / 1024));
		alarmMessage.append(" MB\r\n");
		alarmMessage.append("\t :: Max memory             : ");
		alarmMessage.append(format.format(maxMemory / 1024 / 1024));
		alarmMessage.append(" MB\r\n");
		alarmMessage.append("\t :: Total free memory      : ");
		alarmMessage.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024 / 1024));
		alarmMessage.append(" MB\r\n");
		alarmMessage.append("\t :: Total free memory      : ");
		alarmMessage.append(format.format((100 * (freeMemory + (maxMemory - allocatedMemory)) / maxMemory)));
		alarmMessage.append(" (%)\r\n");
		alarmMessage.append("\t :: Disk in used           : ");
		alarmMessage.append(format.format(usedDiskSize / 1024 / 1024));
		alarmMessage.append("/");
		alarmMessage.append(format.format(totalDiskSize / 1024 / 1024));
		alarmMessage.append(" MB, Used ");
		alarmMessage.append(format.format(percentUsedDisk));
		alarmMessage.append(" (%) (");
		alarmMessage.append(warningDiskPath);
		alarmMessage.append(")\r\n");
		alarmMessage.append("\t :: Total running thread   : ");
		alarmMessage.append(Thread.activeCount());
		alarmMessage.append("\r\n");
		
		boolean needAlarm = false;
		if (percentUsedDisk >= warningDiskPercent)
		{
			needAlarm = true;
		}

		return needAlarm;
	}
	
	protected void connectionLogging() throws Exception
	{
		if (Database.appDatasource != null)
		{
			alarmMessage.append("\r\n");
			alarmMessage.append("Database connection status: \r\n");
			alarmMessage.append("\t Number database connection\t\t\t: ");
			alarmMessage.append(Database.appDatasource.getNumConnections());
			alarmMessage.append("\r\n");
			alarmMessage.append("\t Number busy database connection\t: ");
			alarmMessage.append(Database.appDatasource.getNumBusyConnections());
			alarmMessage.append("\r\n");
			alarmMessage.append("\t Number idle database connection\t: ");
			alarmMessage.append(Database.appDatasource.getNumIdleConnections());
			alarmMessage.append("\r\n");
			alarmMessage.append("\r\n");
		}
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// Modified by NamTA
	// Modified Date 27/08/2012
	// //////////////////////////////////////////////////////
	public void doProcessSession() throws Exception
	{
		alarmMessage = new StringBuilder();
		
		boolean needAlarm = false;
		needAlarm = memoryLogging();
		
		connectionLogging();

		String queueWarningMessage = "";

		int commandRoutingSize = QueueFactory.getCommandRoutingSize();
		int alarmSize = QueueFactory.getAlarmSize();
//		int dbQueueSize = QueueFactory.getDBQueueSize();
		
		
		QueueFactory.setQueueSize("pg/CommandRoute", commandRoutingSize);
		QueueFactory.setQueueSize("pg/Alarm", alarmSize);
//		QueueFactory.setQueueSize("vas/DBQueue", dbQueueSize);
		
		alarmMessage.append(sdf.format(new Date()));
		alarmMessage.append("\r\n");
		
		alarmMessage.append("Local pg/CommandRoute queue: ");
		alarmMessage.append(commandRoutingSize);
		alarmMessage.append("\r\n");
		
		alarmMessage.append("Local pg/Alarm queue: ");
		alarmMessage.append(alarmSize);
		alarmMessage.append("\r\n");
		
//		alarmMessage.append("Local vas/DBQueue queue: ");
//		alarmMessage.append(dbQueueSize);
//		alarmMessage.append("\r\n");
//		
//		alarmMessage.append("Local vas/LBAQueue queue: ");
//		alarmMessage.append(lbaQueueSize);
//		alarmMessage.append("\r\n");

		if (queueDispatcherEnable)
		{
			MQConnection connection = null;

			try
			{
				connection = getMQConnection();
				for (int j = 0; j < queueList.length; j++)
				{
					if (!queueList[j].equals(""))
					{
						Queue checkQueue = QueueFactory.getQueue(queueList[j]);

						int size = 0;

						try
						{
							size = connection.getQueueSize(checkQueue);
							// size = QueueFactory.getQueueSize(queueSession,
							// checkQueue);
							QueueFactory.setQueueSize(queueList[j], size);
							
							alarmMessage.append("Total command request for ");
							alarmMessage.append(queueList[j]);
							alarmMessage.append(" : ");
							alarmMessage.append(size);
							alarmMessage.append("\r\n");
						}
						catch (Exception e)
						{
							alarmMessage.append("Total command request for ");
							alarmMessage.append(queueList[j]);
							alarmMessage.append(" : Can not count, browser is closed.\r\n");
						}

						queueWarningMessage += queueWarning(checkQueue,
								queueList[j], size);
					}
				}
			}
			finally
			{
				returnMQConnection(connection);
			}
		}

		if (needAlarm)
		{
			alarmMessage.append("WARNING: Disk space is running low.\r\n");
		}
		if (!queueWarningMessage.equals(""))
		{
			needAlarm = true;
			alarmMessage.append(queueWarningMessage);
		}
		
		log.info(alarmMessage.toString());

		logMonitor(alarmMessage.toString());

		if (needAlarm)
		{
			AlarmMessage message = new AlarmMessage();
			message.setContent(alarmMessage.toString());
			message.setDescription("System resource is running low.");
			message.setCause("system-resouce");
			message.setImmediately(true);
			sendAlarmMessage(message);
		}
	}
}
