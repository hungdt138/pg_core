package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.util.ThreadUtil;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;
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

public class DBQueueThread extends ProvisioningThread
{
	protected PreparedStatement					_stmtQueue		= null;

	protected ResultSet							_rsQueue		= null;

	protected String							_sqlCommand		= "";
	protected int								_minFreeSize	= 15000;
	protected int								_batchSize		= 100;
	protected int								_orderTimeOut	= 60000;
	protected String[]							queueList		= new String[0];
	protected boolean							pushFreeRequest	= false;
	protected Connection						connection		= null;
	
	protected ConcurrentHashMap<String, Boolean>	indexes		= new ConcurrentHashMap<String, Boolean>();

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(createParameterDefinition("SQLCommand", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("BatchSize", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("MinFreeSize", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("OrderTimeOut", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("queueList", "",
				ParameterType.PARAM_TEXTBOX_MAX, ""));
		vtReturn.addElement(ThreadUtil.createBooleanParameter(
				"PushFreeRequest", ""));

		vtReturn.addAll(super.getParameterDefinition());

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
			
			try
			{
				setSQLCommand(loadMandatory("SQLCommand"));
				setBatchSize(loadInteger("BatchSize"));
				setMinFreeSize(loadInteger("MinFreeSize"));
				setOrderTimeOut(loadInteger("OrderTimeOut"));
				queueList = StringUtil.toStringArray(
						ThreadUtil.getString(this, "queueList", false, ""),
						StringPool.SEMICOLON);
				setPushFreeRequest(ThreadUtil.getBoolean(this,
						"PushFreeRequest", false));
			}
			catch (Exception e)
			{
				setMinFreeSize(15000);
			}
		}
		catch (AppException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			connection = Database.getConnection();
			
			String strSQL = getSQLCommand();
			if (getBatchSize() > 0)
			{
				strSQL = strSQL + " and rownum <= "	+ getBatchSize();
			}
			
			if (!getPushFreeRequest())
			{
				strSQL = strSQL + " and keyword not like 'FREE_%'";
			}
			_stmtQueue = connection.prepareStatement(strSQL);
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
	public void afterProcessSession() throws Exception
	{
		try
		{
			QueueFactory.getDBQueue().clear();
			indexes.clear();

			Database.closeObject(_stmtQueue);
			Database.closeObject(connection);
		}
		catch (Exception e)
		{
			throw e;
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
	public void doProcessSession() throws Exception
	{
		try
		{
			_rsQueue = _stmtQueue.executeQuery();
			
			while (_rsQueue.next() && isAvailable())
			{
				updateSnapShot();
				
				while (isAvailable() && (QueueFactory.getDBQueueSize() > getMinFreeSize()))
				{
					Thread.sleep(getDelayTime());
				}
				
				String requestId = _rsQueue.getString("requestId");
				
				if (indexes.get(requestId) == null)
				{
					QueueFactory.attachDBQueue(pushOrder(_rsQueue.getString("isdn"),
							_rsQueue.getString("serviceaddress"),
							_rsQueue.getString("channel"),
							_rsQueue.getString("keyword"),
							_rsQueue.getLong("requestId")));
					
					indexes.put(requestId, Boolean.TRUE);
				}
				
			}
		}
		catch (SQLException e)
		{
			logMonitor(e);
			
			try
			{
				Database.closeObject(connection);
			}
			catch (Exception ex)
			{
				logMonitor(ex);
			}
			
			try
			{
				connection = Database.getConnection();
			}
			catch (Exception ex)
			{
				logMonitor(ex);
			}
		}
		catch (Exception e)
		{
			logMonitor(e);
		}
		finally
		{
			Database.closeObject(this._rsQueue);
		}
	}
	
	public void updateSnapShot() throws Exception
	{
		for (int j = 0; j < queueList.length; j++)
		{
			if (!queueList[j].equals(""))
			{
				int size = QueueFactory.getQueueSize(queueList[j]);
				if (size >= _minFreeSize)
				{
					logMonitor("Too many order in queue " + queueList[j]
							+ ": " + size);
					
					Thread.sleep(getDelayTime());
				}
			}
		}
	}

	public CommandMessage pushOrder(String isdn, String product,
			String channel, String keyword, long requestId)
			throws Exception
	{
		CommandMessage order = new CommandMessage();

		try
		{
			order.setChannel(channel);
			order.setUserId(0);
			order.setUserName("system");
			order.setRequestId(requestId);
			order.setServiceAddress(product);
			order.setIsdn(isdn);
			order.setKeyword(keyword);
			order.setTimeout(getOrderTimeOut());
		}
		catch (Exception e)
		{
			throw e;
		}
		return order;
	}

	public void setMinFreeSize(int _minFreeSize)
	{
		this._minFreeSize = _minFreeSize;
	}

	public int getMinFreeSize()
	{
		return _minFreeSize;
	}

	public void setSQLCommand(String _sqlCommand)
	{
		this._sqlCommand = _sqlCommand;
	}

	public String getSQLCommand()
	{
		return _sqlCommand;
	}

	public void setOrderTimeOut(int _orderTimeOut)
	{
		this._orderTimeOut = _orderTimeOut;
	}

	public int getOrderTimeOut()
	{
		return _orderTimeOut;
	}

	public void setPushFreeRequest(boolean pushFreeRequest)
	{
		this.pushFreeRequest = pushFreeRequest;
	}

	public boolean getPushFreeRequest()
	{
		return pushFreeRequest;
	}

	public Connection getConnection()
	{
		return connection;
	}

	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	public void setBatchSize(int _batchSize)
	{
		this._batchSize = _batchSize;
	}

	public int getBatchSize()
	{
		return _batchSize;
	}
}
