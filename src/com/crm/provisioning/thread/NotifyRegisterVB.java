package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;
import org.apache.log4j.Logger;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.CommandEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.thread.DispatcherThread;
import com.fss.thread.ParameterType;
import com.fss.util.AppException;

public class NotifyRegisterVB extends DispatcherThread
{
	protected PreparedStatement _stmtQueue = null;
	protected PreparedStatement _stmtRemove = null;
	protected String _sqlCommand = null;
	protected String _orderQueueName = "";
	protected String _contentVB600 = "";
	protected String _contentVB220 = "";
	protected int _ExpiredTime = 10;
	protected Connection connection = null;
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
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		vtReturn.addElement(createParameterDefinition("SMSContentVB600", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		vtReturn.addElement(createParameterDefinition("SMSContentVB220", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		vtReturn.addElement(createParameterDefinition("ExpiredTime", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		vtReturn.addElement(createParameterDefinition("OrderQueueName", "",
				ParameterType.PARAM_TEXTAREA_MAX, "100", ""));
		
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
			_sqlCommand = loadMandatory("SQLCommand");
			_contentVB220 = loadMandatory("SMSContentVB220");
			_contentVB600 = loadMandatory("SMSContentVB600");
			_ExpiredTime = Integer.valueOf(loadMandatory("ExpiredTime"));
			_orderQueueName = loadMandatory("OrderQueueName");

			super.fillParameter();
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
			connection = Database.getConnection();
			String strSQL = getSQLCommand();
			_stmtQueue = connection.prepareStatement(strSQL);

			strSQL = "delete REGISTERVB Where isdn = ? and productid = ?";
			_stmtRemove = connection.prepareStatement(strSQL);

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
			Database.closeObject(_stmtRemove);
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
		int batchCount = 0;
		try
		{
			CommandMessage order = null; 
					
			ResultSet result = _stmtQueue.executeQuery();

			Date currentDate = new Date();

			String isdn = "";
			String content = "";
			String serviceAddress = "";
			int productid;
			
			while (result.next())
			{
				Timestamp dRegisterDate = result.getTimestamp("orderdate");
				if (currentDate.getTime() - dRegisterDate.getTime() > _ExpiredTime * 60 * 1000)
				{

					isdn = result.getString("isdn");
					productid = result.getInt("productid");

					ProductEntry product = ProductFactory.getCache()
							.getProduct(productid);

					if ("VB600".equals(product.getAlias().toUpperCase()))
					{
						serviceAddress = "1720";
						content = _contentVB600;
					}
					else if ("VB220".equals(product.getAlias().toUpperCase()))
					{
						serviceAddress = "1721";
						content = _contentVB220;
					}

					order = pushOrder(isdn, serviceAddress,	content);

					QueueFactory.attachCommandRouting(order);

					_stmtRemove.setString(1, isdn);
					_stmtRemove.setLong(2, productid);
					_stmtRemove.addBatch();
					batchCount++;
					
					logMonitor(isdn + " - remove confirm register " + product.getAlias());
				}
			}
			result.close();
			if (batchCount > 0)
			{
				_stmtRemove.executeBatch();
			}
		}
		catch (Exception ex)
		{
			_logger.error("NotifyRegisterVB: " + ex.getMessage());
			logMonitor(ex.getMessage());
		}
		finally
		{
			connection.commit();
		}
	}

	public CommandMessage pushOrder(String isdn, String serviceAddress, String content) throws Exception
	{
		CommandMessage order = new CommandMessage();

		try
		{
			CommandEntry command = ProvisioningFactory.getCache().getCommand(Constants.COMMAND_SEND_SMS);
			
			order.setProvisioningType(Constants.PROVISIONING_SMSC);
			order.setCommandId(command.getCommandId());
			order.setServiceAddress(serviceAddress);
			order.setIsdn(isdn);
			
			order.setRequest(content);

			order.setRequestValue(ResponseUtil.SMS_CMD_CHECK, "false");
			
		}
		catch (Exception e)
		{
			throw e;
		}
		return order;
	}

	private static Logger _logger = Logger.getLogger(NotifyRegisterVB.class);

	public void setSQLCommand(String _sqlCommand)
	{
		this._sqlCommand = _sqlCommand;
	}

	public String getSQLCommand()
	{
		return _sqlCommand;
	}
}
