package com.crm.provisioning.thread;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.jms.Queue;

import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.DispatcherThread;
import com.fss.thread.ParameterType;
import com.fss.util.AppException;

public class UnregisterS3Sub extends DispatcherThread
{
	private String _filePath = "";
	private String _sql = "";
	private int _waittingTime = 10;
	private int _minFreeSize = 29985;
	private PreparedStatement _stmtQueue = null;
	private Connection connection = null;

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(createParameterDefinition("FilePath", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("SQL", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("WaittingTime", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("MinFreeSize", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));

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

			setSql(loadMandatory("SQL"));
			setFilePath(loadMandatory("FilePath"));
			setWaittingTime(loadInteger("WaittingTime"));
			setMinFreeSize(loadInteger("MinFreeSize"));
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
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			neverExpire = false;

			connection = Database.getConnection();
			
			String strSQL = getSql();
			debugMonitor(strSQL);
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
			Database.closeObject(_stmtQueue);
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

	public void doProcessSession() throws Exception
	{
		ResultSet result = null;

		MQConnection conn = null;

		FileReader mTextFile = null;
		BufferedReader mTextBuffer = null;

		int sizeOrder = 0;

		try
		{
			Queue checkQueue = QueueFactory.getQueue(queueName);

			mTextFile = new FileReader(getFilePath());

			mTextBuffer = new BufferedReader(mTextFile, 1024 * 1024);

			String mstrLine = mTextBuffer.readLine().trim();

			conn = getMQConnection();
			while ((mstrLine != null) && isAvailable())
			{
				_stmtQueue.setString(1, mstrLine);
				result = _stmtQueue.executeQuery();

				while (result.next())
				{
					sizeOrder = conn.getQueueSize(checkQueue);

					if (sizeOrder >= _minFreeSize)
					{
						debugMonitor("Too many order in queue: " + sizeOrder);
						Thread.sleep(_waittingTime * 1000);
					}

					String product = result.getString("ALIAS_");

					CommandMessage order = pushOrder(mstrLine, product);
					conn.sendMessage(order,
							QueueFactory.ORDER_REQUEST_QUEUE, 0, queuePersistent);
					
					debugMonitor("Isdn: " + mstrLine + ", Product: " + product);
				}
				result.close();

				mstrLine = mTextBuffer.readLine();
			}
		}
		catch (Exception ex)
		{
			logMonitor("Error: " + ex.getMessage());
		}
		finally
		{
			returnMQConnection(conn);
			connection.commit();
		}
	}

	public CommandMessage pushOrder(String isdn, String serviceAddress)
			throws Exception
	{
		CommandMessage order = new CommandMessage();

		try
		{
			order.setChannel("web");
			order.setUserId(0);
			order.setUserName("system");

			order.setServiceAddress(serviceAddress);
			order.setIsdn(isdn);
			order.setKeyword("UNREGISTER_" + serviceAddress);
		}
		catch (Exception e)
		{
			throw e;
		}
		return order;
	}

	public void setFilePath(String _filePath)
	{
		this._filePath = _filePath;
	}

	public String getFilePath()
	{
		return _filePath;
	}

	public void setSql(String _sql)
	{
		this._sql = _sql;
	}

	public String getSql()
	{
		return _sql;
	}

	public void setWaittingTime(int waittingTime)
	{
		this._waittingTime = waittingTime;
	}

	public int getWaittingTime()
	{
		return _waittingTime;
	}

	public void setMinFreeSize(int _minFreeSize)
	{
		this._minFreeSize = _minFreeSize;
	}

	public int getMinFreeSize()
	{
		return _minFreeSize;
	}
}
