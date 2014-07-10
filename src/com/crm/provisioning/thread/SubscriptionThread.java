package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.thread.DispatcherThread;
import com.crm.thread.util.ThreadUtil;
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

public class SubscriptionThread extends DispatcherThread
{
	private PreparedStatement	_stmtQueue			= null;
	private PreparedStatement	_stmtAddQueue		= null;
	private PreparedStatement	_stmtSubscribers	= null;
	private String				_sqlCommand			= "";
	private String				_channel			= "";
	private String				_lastRunDate		= "";
	private Connection			connection			= null;

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(ThreadUtil.createTextParameter("SQLCommand", 400, "SQL query to get subscription."));

		vtReturn.addElement(ThreadUtil.createTextParameter("LastRunDate", 400, "SQL query to get subscription."));
		
		vtReturn.addElement(ThreadUtil.createTextParameter("channel", 400, "Subscription channel \"web\" or \"SMS\"."));

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

			setSQLCommand(loadMandatory("SQLCommand"));
			setLastRunDate(loadMandatory("LastRunDate"));
			
			_channel = ThreadUtil.getString(this, "channel", false, "web");
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
			neverExpire = false;

			connection = Database.getConnection();
			
			String strSQL = getSQLCommand();
			_stmtQueue = connection.prepareStatement(strSQL);

			strSQL = "Update SubscriberProduct Set lastRunDate = SysDate Where subProductId = ?";
			_stmtSubscribers = connection.prepareStatement(strSQL);

			strSQL = "insert into CommandRequest "
					+ "		(requestId, userName, createDate, requestDate "
					+ "		, channel, serviceAddress, isdn, keyword) "
					+ " values "
					+ "		(command_seq.nextval, ?, SysDate, SysDate"
					+ "		, ?, ?, ?, ?) ";
			_stmtAddQueue = connection.prepareStatement(strSQL);
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
			Database.closeObject(_stmtSubscribers);
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
		long counter = 0;

		int batchSize = 5000;
		int batchCounter = 0;

		ResultSet rsQueue = null;

		try
		{
			rsQueue = _stmtQueue.executeQuery();
			while (rsQueue.next() && isAvailable())
			{
				String sourceAddress = StringUtil.nvl(rsQueue.getString("isdn"), "");

				int subsriberType = rsQueue.getInt("subscriberType");

				if (subsriberType == Constants.PREPAID_SUB_TYPE)
				{
					ProductEntry product = ProductFactory.getCache().getProduct(rsQueue.getLong("productid"));

					if ((product != null) && product.isSubscription())
					{
						try
						{
							_stmtAddQueue.setString(1, "system");
							_stmtAddQueue.setString(2, _channel);
							_stmtAddQueue.setString(3, product.getAlias());
							_stmtAddQueue.setString(4, sourceAddress);
							_stmtAddQueue.setString(5, "SUBSCRIPTION_" + product.getAlias());

							_stmtAddQueue.addBatch();

							_stmtSubscribers.setLong(1, rsQueue.getLong("subProductId"));
							_stmtSubscribers.addBatch();

							batchCounter++;

							if (batchCounter >= batchSize)
							{
								_stmtAddQueue.executeBatch();
								_stmtSubscribers.executeBatch();

								batchCounter = 0;
							}

							counter++;

							debugMonitor(sourceAddress + ": " + product.getAlias());
						}
						catch (Exception e)
						{
							logMonitor(sourceAddress + ": " + product.getAlias() + "(" + e.getMessage() + ")");
						}
					}
				}
			}

			if (batchCounter >= 0)
			{
				_stmtAddQueue.executeBatch();
				_stmtSubscribers.executeBatch();
			}

			logMonitor("Total record is browsed: " + counter);

			setLastRunDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));

			mprtParam.setProperty("LastRunDate", getLastRunDate());
			storeConfig();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			connection.commit();
			Database.closeObject(rsQueue);
		}
	}

	public void setLastRunDate(String _lastRunDate)
	{
		this._lastRunDate = _lastRunDate;
	}

	public String getLastRunDate()
	{
		return _lastRunDate;
	}

	public void setSQLCommand(String _sqlCommand)
	{
		this._sqlCommand = _sqlCommand;
	}

	public String getSQLCommand()
	{
		return _sqlCommand;
	}
}
