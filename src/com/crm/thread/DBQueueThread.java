package com.crm.thread;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.jms.Message;

import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.util.ThreadUtil;
import com.fss.thread.ParameterType;
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

public class DBQueueThread extends DispatcherThread
{
	protected PreparedStatement	stmtQueue			= null;
	protected PreparedStatement	stmtRemove			= null;
	protected ResultSet			rsQueue				= null;

	protected String			queueTable			= "";
	protected String			indicatorField		= "";
	protected String			sqlStatement		= "";

	protected int				minFreeSize			= 15000;
	protected int				delayTimeWhenBusy	= 15000;

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(createParameterDefinition("sqlStatement", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("queueTable", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("indicatorField", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("minFreeSize", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("delayTimeWhenBusy", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));

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

			sqlStatement = ThreadUtil.getString(this, "sqlStatement", true, "");
			queueTable = ThreadUtil.getString(this, "queueTable", true, "");
			indicatorField = ThreadUtil.getString(this, "indicatorField", true, "");
			minFreeSize = ThreadUtil.getInt(this, "minFreeSize", 1000);
			delayTimeWhenBusy = ThreadUtil.getInt(this, "delayTimeWhenBusy", 1000);
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
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	protected CommandMessage createMessage(ResultSet rsMessage) throws Exception
	{
		return null;
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	protected boolean sendMessage() throws Exception
	{
		CommandMessage message = createMessage(rsQueue);

		if ((message != null) && queueDispatcherEnable)
		{
			MQConnection connection = null;
			
			try
			{
				connection = getMQConnection();
				connection.sendMessage(message, queueName, 0, queuePersistent);
				debugMonitor(message);
			}
			catch (Exception e)
			{
				returnMQConnection(connection);
			}
			return true;
		}

		return false;
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	protected void removeBatch() throws Exception
	{
		if (!indicatorField.equals(""))
		{
			stmtRemove.setString(1, rsQueue.getString(indicatorField));
			stmtRemove.execute();

			mcnMain.commit();
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data target
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean next() throws Exception
	{
		if (!rsQueue.next() || !isAvailable() || queueDispatcherEnable)
		{
			return false;
		}
		
		
		int queueSize = 0;
		

		MQConnection connection = null;
		
		try
		{
			connection = getMQConnection();
			queueSize = connection.getQueueSize(queueWorking);
		}
		finally
		{
			returnMQConnection(connection);
		}

		int sizeOrder =  (QueueFactory.getMaxQueueSize(queueWorking) - queueSize);

		if (sizeOrder <= minFreeSize)
		{
			debugMonitor("Too many order in queue: " + sizeOrder);

			return false;
		}
		else
		{
			sizeOrder = QueueFactory.getFreeQueueSize();

			if (sizeOrder < minFreeSize)
			{
				debugMonitor("Too many request are processing: " + sizeOrder);

				return false;
			}
		}

		return true;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Event raised when session prepare to run
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			if (!queueTable.equals("") && !indicatorField.equals(""))
			{
				String SQL = "Delete " + queueTable + " Where " + indicatorField + " = ?";

				stmtRemove = mcnMain.prepareStatement(SQL);
			}

			if (sqlStatement.equals(""))
			{
				sqlStatement = "Select * From " + queueTable;
			}
			else
			{
				stmtQueue = mcnMain.prepareStatement(sqlStatement);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	/**
	 * Event raised when session prepare to run
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void afterProcessSession() throws Exception
	{
		Database.closeObject(stmtQueue);
		Database.closeObject(rsQueue);
		Database.closeObject(stmtRemove);

		super.afterProcessSession();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Session process
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void doProcessSession() throws Exception
	{
		rsQueue = stmtQueue.executeQuery();

		while (next())
		{
			if (sendMessage())
			{
				removeBatch();
			}
		}
	}

}
