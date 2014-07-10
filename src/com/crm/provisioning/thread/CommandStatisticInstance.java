package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Message;

import com.crm.kernel.message.AlarmMessage;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.DispatcherInstance;
import com.crm.util.DateUtil;

public class CommandStatisticInstance extends DispatcherInstance
{
	protected ConcurrentHashMap<Long, ProductStatistic>	chpProductStatistic	= null;
	protected Calendar									startTime			= null;

	public CommandStatisticInstance() throws Exception
	{
		super();
	}

	@Override
	public Message detachMessage() throws Exception
	{
		Calendar now = Calendar.getInstance();
		if (now.getTimeInMillis() - startTime.getTimeInMillis() > 1000 * ((CommandStatisticThread) getDispatcher()).updateDatabaseInterval)
		{
			updateStatistic();
		}
		Message message = null;

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
		return message;
	}

	public void beforeProcessSession() throws Exception
	{
		try
		{
			super.beforeProcessSession();

			startTime = Calendar.getInstance();

			chpProductStatistic = new ConcurrentHashMap<Long, ProductStatistic>();
		}
		catch (Exception e)
		{
			mcnMain.close();
			throw e;
		}
	}

	public void afterProcessSession() throws Exception
	{
		try
		{
			updateStatistic();
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

	@Override
	public void doProcessSession() throws Exception
	{
		super.doProcessSession();
	}

	public void updateStatistic() throws Exception
	{
		Connection connection = null;
		PreparedStatement stmtStatistic = null;
		
		int failureCount = 0;
		String failureContent = "";

		try
		{
			connection = Database.getConnection();

			String SQL = "Insert into ProductStatistic " +
					"	(startDate,statisticDate,ServerRun,productId,alias,totalReqFailure,totalReqSuccess,totalReq) " +
					"Values " +
					"	(?, ?, ?, ?, ?, ?, ?, ?) ";

			stmtStatistic = connection.prepareStatement(SQL);

			Set<Long> keys = chpProductStatistic.keySet();

			Iterator<Long> iterator = keys.iterator();
			Calendar now = Calendar.getInstance();
			while (iterator.hasNext())
			{
				Long productId = iterator.next();
				ProductStatistic productStatistic = chpProductStatistic.get(productId);
				if (productStatistic.getFailure() > 0)
				{
					failureCount += productStatistic.getFailure();
					failureContent += productStatistic.toString() + ";\r\n"; 
				}
				int total = productStatistic.getFailure() + productStatistic.getSuccess();

				if (total > 0)
				{
					stmtStatistic.setTimestamp(1, DateUtil.getTimestampSQL(productStatistic.getStartTime().getTime()));
					stmtStatistic.setTimestamp(2, DateUtil.getTimestampSQL(now.getTime()));
					stmtStatistic.setString(3, ((CommandStatisticThread) dispatcher).hostToStatistic);
					stmtStatistic.setLong(4, productId);

					stmtStatistic.setString(5, productStatistic.getAlias());
					stmtStatistic.setLong(6, productStatistic.getFailure());
					stmtStatistic.setLong(7, productStatistic.getSuccess());
					stmtStatistic.setLong(8, total);

					try
					{
						stmtStatistic.execute();
						debugMonitor("Update database " + productStatistic.toString());
						
						ProductEntry product = ProductFactory.getCache().getProduct(productId);
						
						productStatistic = new ProductStatistic();
						productStatistic.setAlias(product.getAlias());
						productStatistic.setProductId(productId);
						chpProductStatistic.put(productId, productStatistic);
					}
					catch (Exception e)
					{
						debugMonitor(e);
						debugMonitor("Fail to update database " + productStatistic.toString());
					}
				}
			}

			startTime = Calendar.getInstance();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtStatistic);
			Database.closeObject(connection);
			
			if (failureCount >= ((CommandStatisticThread)getDispatcher()).warningCount)
			{
				AlarmMessage alarmMessage = new AlarmMessage();
				alarmMessage.setRequestTime(new Date());
				alarmMessage.setContent(failureContent);
				alarmMessage.setCause("failure-command");
				alarmMessage.setDescription("Too many failed request.");
				alarmMessage.setImmediately(true);
				sendInstanceAlarm(alarmMessage);
			}
		}
	}

	public int processMessage(Message message) throws Exception
	{
		CommandMessage request = null;
		try
		{
			request = (CommandMessage) QueueFactory.getContentMessage(message);
		}
		catch (Exception e)
		{
//			debugMonitor(e);
			return Constants.BIND_ACTION_NONE;
		}
		
		if (request == null)
		{
			return Constants.BIND_ACTION_NONE;
		}
		try
		{
			ProductStatistic productStatistic = null;

			productStatistic = chpProductStatistic.get(request.getProductId());

			if (productStatistic == null)
			{
				ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());
				
				if (product == null)
					return Constants.BIND_ACTION_NONE;
				productStatistic = new ProductStatistic();
				productStatistic.setAlias(product.getAlias());
				productStatistic.setProductId(request.getProductId());
			}

			if (request.getStatus() == Constants.ORDER_STATUS_APPROVED)
			{
				productStatistic.setSuccess(productStatistic.getSuccess() + 1);
			}
			else
			{
				productStatistic.setFailure(productStatistic.getFailure() + 1);
			}

			chpProductStatistic.put(request.getProductId(), productStatistic);
		}
		catch (Exception e)
		{
			throw e;
		}
		return Constants.BIND_ACTION_NONE;
	}
}
