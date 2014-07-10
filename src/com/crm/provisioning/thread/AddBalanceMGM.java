package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.jms.Queue;
import org.apache.log4j.Logger;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.MailThread;
import com.fss.thread.ParameterType;
import com.fss.util.AppException;

public class AddBalanceMGM extends MailThread
{
	protected PreparedStatement	_stmtQueue			= null;
	protected PreparedStatement	_stmtTotal			= null;
	protected String			_sqlCommand			= "";
	protected String			_sqlCount			= "";
	protected String			_lastRunDate		= "";
	protected int				_waittingTime		= 10;
	protected int				_minFreeSize		= 29985;
	protected String			orderQueueName		= "";
	protected String			mstrStartContent	= "";
	protected String			mstrRunContent		= "";
	protected Connection 		connection 			= null;

	{

	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(createParameterDefinition("SQLCommand", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("SQLCount", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("LastRunDate", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("WaittingTime", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("MinFreeSize", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("OrderQueueName", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("MailStartContent", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("MailRunContent", "",
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

			// Fill parameter
			setSQLCommand(loadMandatory("SQLCommand"));
			_sqlCount = loadMandatory("SQLCount");
			setLastRunDate(loadMandatory("LastRunDate"));
			setWaittingTime(Integer.parseInt(loadMandatory("WaittingTime")));
			setMinFreeSize(Integer.parseInt(loadMandatory("MinFreeSize")));
			orderQueueName = loadMandatory("OrderQueueName");
			mstrStartContent = loadMandatory("MailStartContent");
			mstrRunContent = loadMandatory("MailRunContent");
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
			_stmtTotal = connection.prepareStatement(_sqlCount);

			String strSQL = getSQLCommand();
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
			Database.closeObject(_stmtTotal);
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

	public void doProcessSession() throws Exception
	{
		ResultSet result = null;
		try
		{
			int totalExecute = 0;
			int totalAdd = 0;
			int sizeOrder = 0;
			int count = 0;
			int total = 0;
			String introducer = "";
			String strMailContent = "";

			Queue checkQueue = QueueFactory.getQueue(queueName);

			result = _stmtTotal.executeQuery();
			if (result.next())
			{
				totalAdd = result.getInt("TOTAL");
				strMailContent = mstrStartContent.replaceAll("<%TOTAL%>", ""
						+ totalAdd);
				sendEmail(getSubject(), strMailContent, null);
			}
			result.close();

			result = _stmtQueue.executeQuery();

			MQConnection connection = null;

			try
			{
				connection = getMQConnection();
				while (result.next() && isAvailable())
				{
					sizeOrder = connection.getQueueSize(checkQueue);
					//sizeOrder = QueueFactory.getQueueSize(queueSession, checkQueue);

					if (sizeOrder >= _minFreeSize)
					{
						debugMonitor("Too many order in queue: " + sizeOrder);
						Thread.sleep(_waittingTime * 1000);
					}
					introducer = result.getString("introducer");
					total = result.getInt("TOTAL");

					CommandMessage order = pushOrder(introducer, total);
					connection.sendMessage(order, orderQueueName, 0, queuePersistent);

					logMonitor("isdn: " + introducer + ", member: " + total);

					count++;

					if (count == 60000)
					{
						strMailContent = mstrRunContent.replaceAll("<%TOTAL%>", ""
								+ count + "/" + totalAdd);
						totalAdd = totalAdd - count;
						sendEmail(getSubject(), strMailContent, null);
						count = 0;
					}

					totalExecute++;
				}

			}
			finally
			{
				returnMQConnection(connection);
			}

			logMonitor("Total: " + totalExecute);

			if (count > 0)
			{
				strMailContent = mstrRunContent.replaceAll("<%TOTAL%>", ""
						+ count + "/" + totalAdd);

				sendEmail(getSubject(), strMailContent, null);
			}

			setLastRunDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));

			mprtParam.setProperty("LastRunDate", getLastRunDate());
			storeConfig();
		}
		catch (Exception ex)
		{
			logMonitor("Error: " + ex.getMessage());
			_logger.error("AddMGMPromotion: " + ex.getMessage());

		}
		finally
		{
			result.close();
			connection.commit();
		}
	}

	public CommandMessage pushOrder(String isdn, int total) throws Exception
	{
		CommandMessage order = new CommandMessage();

		try
		{
			order.setChannel("web");
			order.setUserId(0);
			order.setUserName("system");

			order.setServiceAddress("MGM");
			order.setIsdn(isdn);
			order.setKeyword("ADD_BALANCE_MGM");
			order.getParameters().setProperty("TotalMember",
					String.valueOf(total));
		}
		catch (Exception e)
		{
			throw e;
		}
		return order;
	}

	private static Logger	_logger	= Logger.getLogger(AddBalanceMGM.class);

	public void setSQLCommand(String _sqlCommand)
	{
		this._sqlCommand = _sqlCommand;
	}

	public String getSQLCommand()
	{
		return _sqlCommand;
	}

	public void setLastRunDate(String _lastRunDate)
	{
		this._lastRunDate = _lastRunDate;
	}

	public String getLastRunDate()
	{
		return _lastRunDate;
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
