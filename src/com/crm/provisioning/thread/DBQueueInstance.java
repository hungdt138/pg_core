package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;

import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.message.CommandMessage;

public class DBQueueInstance extends ProvisioningInstance
{
	protected PreparedStatement _stmtRemove = null;
	protected Connection connection = null;
	
	public DBQueueInstance() throws Exception
	{
		super();
	}

	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			connection = Database.getConnection();
			
			String strSQL = "Delete CommandRequest Where requestId = ?";
			_stmtRemove = connection.prepareStatement(strSQL);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void afterProcessSession() throws Exception
	{
		try
		{
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
		MQConnection mqConnection = null;
		QueueSession queueSession = null;
		MessageProducer producer = null;
		Queue sendQueue = null;

		long start = 0;
		long end = 0;
		StringBuilder log = null;

		try
		{
			try
			{
				sendQueue = QueueFactory.getQueue(getDispatcher().queueName);

				mqConnection = getMQConnection();
				queueSession = mqConnection.createSession();
				producer = QueueFactory.createQueueProducer(queueSession,
						sendQueue, getDispatcher().timeout,
						getDispatcher().queuePersistent);
			}
			finally
			{
				returnMQConnection(mqConnection);
			}

			CommandMessage order = QueueFactory.detachDBQueue();

			while (order != null && isAvailable())
			{
				log = new StringBuilder();
				
				String requestId = String.valueOf(((CommandMessage) order).getRequestId());
				
				log.append(requestId);
				log.append(" - " + order.getIsdn());
				log.append(" - " + order.getServiceAddress());
				log.append(" - " + order.getKeyword());

				// debug
				start = System.currentTimeMillis();

				Message message = QueueFactory.createObjectMessage(
						queueSession, order);
				producer.send(message);

				// debug
				end = System.currentTimeMillis() - start;
				start = System.currentTimeMillis();

				_stmtRemove.setLong(1, ((CommandMessage) order).getRequestId());
				_stmtRemove.execute();
				connection.commit();

				if (getDispatcher().displayDebug)
				{
					log.append(" - Send message: " + end);
					log.append(" - Remove command message: " + (System.currentTimeMillis() - start));
				}

				getDispatcher().indexes.remove(requestId);
				
				logMonitor(log.toString());

				Thread.sleep(5);
				
				order = QueueFactory.detachDBQueue();
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
			if (e instanceof JMSException)
			{
				mqConnection.markError();
			}

			logMonitor(e);
		}
		finally
		{
			QueueFactory.closeQueue(queueSession);
		}
	}

	public DBQueueThread getDispatcher()
	{
		return (DBQueueThread) super.getDispatcher();
	}
}
