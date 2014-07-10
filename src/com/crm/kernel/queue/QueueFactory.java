package com.crm.kernel.queue;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.crm.kernel.message.AlarmMessage;
import com.crm.kernel.message.Constants;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.OSACallbackMessage;
import com.crm.util.AppProperties;
import com.crm.util.StringUtil;
import com.fss.util.AppException;
import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.QueueConnectionFactory;
import com.sun.messaging.jms.QueueConnection;

public class QueueFactory
{
	public static String QUEUE_FACTORY = "jms/CCS";

	public static String ORDER_REQUEST_QUEUE = "queue/OrderRoute";
	public static String ORDER_RESPONSE_QUEUE = "queue/OrderResponse";
	public static String COMMAND_STATISTIC_QUEUE = "queue/CommandStatistic";

	public static String LOW_BALANCE_ALERT = "queue/LowBalanceAlert";

	public static String ALARM_QUEUE = "queue/Alarm";

	public static String COMMAND_ROUTE_QUEUE = "queue/CommandRoute";
	public static String COMMAND_LOG_QUEUE = "queue/CommandLog";
	public static String COMMAND_CALLBACK = "queue/CommandCallback";

	public static Context context = null;
	public static QueueConnectionFactory connectionFactory = null;
	public static QueueConnection queueConnection = null;
	public static int connectionMode = Constants.QUEUE_CONNECTION_DEDICATED;
	
	public static ConcurrentHashMap<String, Integer> appQueuesSize = new ConcurrentHashMap<String, Integer>();

	protected static ConcurrentHashMap<String, Queue> appQueues = new ConcurrentHashMap<String, Queue>();
	protected static ConcurrentLinkedQueue<CommandMessage> commandRoutingQueue = new ConcurrentLinkedQueue<CommandMessage>();
	protected static ConcurrentLinkedQueue<AlarmMessage> alarmQueue = new ConcurrentLinkedQueue<AlarmMessage>();
	protected static ConcurrentLinkedQueue<CommandMessage> dbQueue = new ConcurrentLinkedQueue<CommandMessage>();
	protected static ConcurrentLinkedQueue<CommandMessage> lbaQueue = new ConcurrentLinkedQueue<CommandMessage>();
	protected static ConcurrentHashMap<String, Long> orderList = new ConcurrentHashMap<String, Long>();

	private static Object mutex = "mutex";
	public static Logger log = Logger.getLogger(QueueFactory.class);

	// public static synchronized QueueSession
	// createQueueSession(DispatcherThread dispatcher) throws Exception
	// {
	// return dispatcher.queueConnection.createQueueSession(false,
	// Session.AUTO_ACKNOWLEDGE);
	// }
	//
	// public static synchronized QueueSession
	// createQueueSession(DispatcherInstance instance) throws Exception
	// {
	// return instance.queueConnection.createQueueSession(false,
	// Session.AUTO_ACKNOWLEDGE);
	// }

	public static synchronized Queue createQueue(QueueSession queueSession,
			String queueName) throws Exception
	{
		Queue queue = null;

		try
		{
			queue = getQueue(queueName);

			if (queue == null)
			{
				queue = queueSession.createTemporaryQueue();
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		return queue;
	}

	public static ConcurrentLinkedQueue<CommandMessage> getCommandRoutingQueue()
	{
		return commandRoutingQueue;
	}

	public static void setCommandRoutingQueue(
			ConcurrentLinkedQueue<CommandMessage> commandRoutingQueue)
	{
		QueueFactory.commandRoutingQueue = commandRoutingQueue;
	}

	public static int getCommandRoutingSize()
	{
		return commandRoutingQueue.size();
	}

	public static void attachCommandRouting(CommandMessage commandRouting)
	{
		commandRoutingQueue.offer(commandRouting);
	}

	public static CommandMessage detachCommandRouting()
	{
		return commandRoutingQueue.poll();
	}
	
	public static ConcurrentLinkedQueue<AlarmMessage> getAlarmQueue()
	{
		return alarmQueue;
	}

	public static void setAlarmQueue(
			ConcurrentLinkedQueue<AlarmMessage> alarmQueue)
	{
		QueueFactory.alarmQueue = alarmQueue;
	}

	public static int getAlarmSize()
	{
		return alarmQueue.size();
	}

	public static void attachAlarm(AlarmMessage alarmQueue)
	{
		QueueFactory.alarmQueue.offer(alarmQueue);
	}

	public static AlarmMessage detachAlarm()
	{
		return alarmQueue.poll();
	}
	
	public static ConcurrentLinkedQueue<CommandMessage> getDBQueue()
	{
		return dbQueue;
	}

	public static void setDBQueue(
			ConcurrentLinkedQueue<CommandMessage> dbQueue)
	{
		QueueFactory.dbQueue = dbQueue;
	}

	public static int getDBQueueSize()
	{
		return dbQueue.size();
	}

	public static void attachDBQueue(CommandMessage dbQueue)
	{
		QueueFactory.dbQueue.offer(dbQueue);
	}

	public static CommandMessage detachDBQueue()
	{
		return dbQueue.poll();
	}
	
	public static ConcurrentLinkedQueue<CommandMessage> getLBAQueue()
	{
		return lbaQueue;
	}

	public static void setLBAQueue(
			ConcurrentLinkedQueue<CommandMessage> lbaQueue)
	{
		QueueFactory.lbaQueue = lbaQueue;
	}

	public static int getLBAQueueSize()
	{
		return lbaQueue.size();
	}

	public static void attachLBAQueue(CommandMessage lbaQueue)
	{
		QueueFactory.lbaQueue.offer(lbaQueue);
	}

	public static CommandMessage detachLBAQueue()
	{
		return lbaQueue.poll();
	}

	public static void attachOrderList(String orderId, long orderDate)
	{
		QueueFactory.orderList.put(orderId, Long.valueOf(orderDate));
	}

	public static Long detachOrderList(String orderId)
	{
		return QueueFactory.orderList.get(orderId);
	}
	
	public static void removeOrderList(String orderId)
	{
		QueueFactory.orderList.remove(orderId);
	}
	
	public static void setQueueSize(String queueName, int queueSize)
	{
		QueueFactory.appQueuesSize.put(queueName, new Integer(queueSize));
	}
	
	public static int getQueueSize(String queueName)
	{
		return QueueFactory.appQueuesSize.get(queueName);
	}

	public static Queue getQueue(String queueName) throws Exception
	{
		Queue queue = null;

		queue = new com.sun.messaging.Queue(queueName.replace("/", "").replace("\\", ""));
		appQueues.put(queueName, queue);
		
//		try
//		{
//			queue = appQueues.get(queueName);
//
//			if (queue == null)
//			{
//				queue = (Queue) context.lookup(queueName);
//
//				appQueues.put(queueName, queue);
//			}
//		}
//		catch (Exception e)
//		{
//			queue = new com.sun.messaging.Queue(queueName.replace("/", "").replace("\\", ""));
//			appQueues.put(queueName, queue);
//		}

		return queue;
	}

	public static void closeQueue(QueueBrowser browser) throws Exception
	{
		try
		{
			if (browser != null)
			{
				browser.close();
			}
		}
		catch (Exception e)
		{
			log.info("closeQueueReceiver ", e);
		}
	}

	public static void closeQueue(MessageConsumer consumer) throws Exception
	{
		try
		{
			if (consumer != null)
			{
				consumer.close();
			}
		}
		catch (Exception e)
		{
			log.info("closeQueueConsumer ", e);
		}
	}

	public static void closeQueue(QueueReceiver queueReceiver) throws Exception
	{
		try
		{
			if (queueReceiver != null)
			{
				queueReceiver.close();
			}
		}
		catch (Exception e)
		{
			log.info("closeQueueReceiver ", e);
		}
	}

	public static void closeQueue(MessageProducer producer)
	{
		try
		{
			if (producer != null)
			{
				producer.close();
			}
		}
		catch (Exception e)
		{
			log.info("closeMessageProducer ", e);
		}
	}

	public static void closeQueue(QueueConnection connection)
	{
		try
		{
			if (connection != null)
			{
				connection.stop();
				connection.close();
			}
		}
		catch (Exception e)
		{
			log.info("closeQueueSession ", e);
		}
	}

	public static void closeQueue(QueueSession queueSession)
	{
		try
		{
			if (queueSession != null)
			{
				queueSession.close();
			}
		}
		catch (Exception e)
		{
			log.info("closeQueueSession ", e);
		}
	}

	public static Queue createQueue() throws Exception
	{
		throw new AppException("not-implemented");
	}

	public static void sendMessage(QueueSession queueSession, Queue queue,
			Message message, long timeout, boolean isPersistent)
			throws Exception
	{
		MessageProducer producer = null;

		try
		{
			producer = queueSession.createProducer(queue);
			if (!isPersistent)
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			if (timeout > 0)
				producer.setTimeToLive(timeout);
			producer.send(message);

			if (queueSession.getTransacted())
			{
				queueSession.commit();
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			closeQueue(producer);
		}
	}

	public static void sendMessage(QueueSession queueSession, Queue queue,
			Message message, boolean isPersistent) throws Exception
	{
		sendMessage(queueSession, queue, message, 0, isPersistent);
	}

	public static void sendMessage(QueueSession queueSession, String queueName,
			Message message, boolean isPersistent) throws Exception
	{
		sendMessage(queueSession, getQueue(queueName), message, isPersistent);
	}

	public static void sendObjectMessage(QueueSession queueSession,
			Queue queue, Serializable messageContent, boolean isPersistent)
			throws Exception
	{
		sendMessage(queueSession, queue,
				createObjectMessage(queueSession, messageContent), isPersistent);
	}

	public static void sendObjectMessage(QueueSession queueSession,
			String queueName, Serializable messageContent, boolean isPersistent)
			throws Exception
	{
		sendMessage(queueSession, getQueue(queueName),
				createObjectMessage(queueSession, messageContent), isPersistent);
	}

	public static Message createObjectMessage(QueueSession queueSession,
			Serializable content) throws Exception
	{
		try
		{
			ObjectMessage message = queueSession.createObjectMessage();
			message.setObject(content);
			if (content instanceof CommandMessage)
			{
				message.setJMSCorrelationID(((CommandMessage) content)
						.getCorrelationID());
			}
			if (content instanceof OSACallbackMessage)
			{
				message.setJMSCorrelationID(((OSACallbackMessage) content)
						.getSessionId());
			}
			return message;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public static Object getContentMessage(Message message) throws Exception
	{
		if (message == null)
		{
			return null;
		}
		else if (message instanceof ObjectMessage)
		{
			return ((ObjectMessage) message).getObject();
		}
		else
		{
			return message;
		}
	}

	public static void emptyQueue(QueueSession queueSession, String queueName)
			throws Exception
	{
		QueueReceiver receiver = null;

		try
		{
			receiver = queueSession.createReceiver(getQueue(queueName));

			while (receiver.receiveNoWait() != null)
			{

			}
		}
		finally
		{
			closeQueue(receiver);
		}
	}

	public static int getFreeSize(Queue queue) throws Exception
	{
		return 1000;
		// return queue == null ? 0 : queue.getMaxQueueSize() -
		// queue.getQueueSize();
	}

	public static int getFreeSize(QueueSession queueSession, String queueName)
			throws Exception
	{
		Queue queue = getQueue(queueName);

		return (queue == null) ? 0 : getFreeSize(queue);
	}

	public static int getFreeQueueSize() throws Exception
	{
		return 1000;
	}

	public static int getQueueSize(QueueSession queueSession, String queueName)
			throws Exception
	{
		Queue queue = getQueue(queueName);

		return (queue == null) ? 0 : getQueueSize(queueSession, queue);
	}

	public static int getQueueSize(QueueSession queueSession, Queue queue)
			throws Exception
	{
		int count = 0;

		QueueBrowser browser = null;

		try
		{
			browser = queueSession.createBrowser(queue);

			Enumeration<?> messagesOnQ = browser.getEnumeration();

			while (messagesOnQ.hasMoreElements())
			{
				messagesOnQ.nextElement();

				count++;
			}

			log.debug("number of messages on input Q= " + count);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			closeQueue(browser);
		}

		return count;
	}

	public static int getMaxQueueSize(Queue queue) throws Exception
	{
		return 10000;
	}

	public static int getMaxQueueSize(QueueSession queueSession,
			String queueName) throws Exception
	{
		Queue queue = getQueue(queueName);

		return (queue == null) ? 0 : getMaxQueueSize(queue);
	}

	public static QueueConnection createQueueConnection() throws Exception
	{
		initContext();

		QueueConnection connection = null;

		try
		{
			if (connectionMode == Constants.QUEUE_CONNECTION_DEDICATED)
			{
				connection = (QueueConnection) connectionFactory
						.createQueueConnection();

				connection.start();
			}
			else
			{
				connection = queueConnection;
			}
		}
		catch (Exception e)
		{
			closeQueue(connection);

			throw e;
		}

		return connection;
	}

	public static synchronized void initContext() throws Exception
	{
		if ((context != null) & (connectionFactory != null))
		{
			return;
		}

		synchronized (mutex)
		{
			appQueues.clear();

			try
			{
				if (context == null)
				{
					// get server host
					AppProperties configProvider = new AppProperties();

					configProvider.loadFromFile("ServerConfig.txt");
					QUEUE_FACTORY = configProvider.getString("queue.factory",
							"jms/CCS");
					ORDER_REQUEST_QUEUE = configProvider.getString(
							"queue.orderRoute", "queue/OrderRoute");
					ORDER_RESPONSE_QUEUE = configProvider.getString(
							"queue.orderResponse", "queue/OrderResponse");
					COMMAND_STATISTIC_QUEUE = configProvider.getString(
							"queue.commandStatistic", "queue/CommandStatistic");
					ALARM_QUEUE = configProvider.getString("queue.alarm",
							"queue/Alarm");

					COMMAND_ROUTE_QUEUE = configProvider.getString(
							"queue.commandRoute", "queue/CommandRoute");
					COMMAND_LOG_QUEUE = configProvider.getString(
							"queue.commandLog", "queue/CommandLog");
					COMMAND_CALLBACK = configProvider.getString(
							"queue.commandCallback", "queue/CommandCallback");
					LOW_BALANCE_ALERT = configProvider.getString(
							"queue.lowBalanceAlert", "queue/LowBalanceAlert");

					// connection mode
					String mode = configProvider.getString("queue.connection",
							"dedicated");

					if (mode.equalsIgnoreCase("dedicated"))
					{
						connectionMode = Constants.QUEUE_CONNECTION_DEDICATED;
					}
					else
					{
						connectionMode = Constants.QUEUE_CONNECTION_SHARING;
					}

					createConnectionFactory(configProvider);
					// get context properties
					Properties properties = new Properties();

					properties.load(new FileInputStream("jndi.properties"));

					System.setProperty(
							"org.omg.CORBA.ORBInitialPort",
							properties
									.getProperty("org.omg.CORBA.ORBInitialPort"));
					System.setProperty(
							"org.omg.CORBA.ORBInitialHost",
							properties
									.getProperty("org.omg.CORBA.ORBInitialHost"));

					context = new InitialContext(properties);
				}

				// lookup the queue connection factory
				// connectionFactory = (QueueConnectionFactory)
				// context.lookup(QUEUE_FACTORY);
				// cxnFactory.setProperty(ConnectionConfiguration.,
				// paramString2)

				if (connectionMode == Constants.QUEUE_CONNECTION_SHARING)
				{
					queueConnection = (QueueConnection) connectionFactory
							.createQueueConnection();

					queueConnection.start();
				}
			}
			catch (Exception e)
			{
				context = null;
				connectionFactory = null;

				throw e;
			}
		}
	}

	protected static void createConnectionFactory(AppProperties configProvider)
			throws JMSException
	{
		String prefix = "queue.factory.";
		String imqAddressList = configProvider.getString(prefix
				+ ConnectionConfiguration.imqAddressList, "localhost:7676");
		String imqAckTimeout = configProvider.getString(prefix
				+ ConnectionConfiguration.imqAckTimeout, "3000");
		String imqConsumerFlowLimit = configProvider.getString(prefix
				+ ConnectionConfiguration.imqConsumerFlowLimit, "3000");
		String imqSocketConnectionTimeout = configProvider.getString(prefix
				+ ConnectionConfiguration.imqSocketConnectTimeout, "3000");
		String imqOverrideJMSDeliveryMode = configProvider.getString(prefix
				+ ConnectionConfiguration.imqOverrideJMSDeliveryMode, "false");
		String imqJMSDeliveryMode = configProvider.getString(prefix
				+ ConnectionConfiguration.imqJMSDeliveryMode, "2");

		connectionFactory = new com.sun.messaging.QueueConnectionFactory();
		connectionFactory.setProperty(ConnectionConfiguration.imqAddressList,
				imqAddressList);
		connectionFactory.setProperty(ConnectionConfiguration.imqAckTimeout,
				imqAckTimeout);
		connectionFactory.setProperty(
				ConnectionConfiguration.imqConsumerFlowLimitPrefetch, "true");
		connectionFactory.setProperty(
				ConnectionConfiguration.imqConsumerFlowLimit,
				imqConsumerFlowLimit);
		connectionFactory.setProperty(
				ConnectionConfiguration.imqSocketConnectTimeout,
				imqSocketConnectionTimeout);
		connectionFactory.setProperty(
				ConnectionConfiguration.imqOverrideJMSDeliveryMode,
				imqOverrideJMSDeliveryMode);
		connectionFactory.setProperty(
				ConnectionConfiguration.imqJMSDeliveryMode, imqJMSDeliveryMode);
	}

	public static void resetContext() throws Exception
	{
		try
		{
			if (queueConnection != null)
			{
				queueConnection.stop();

				queueConnection.close();
			}
		}
		finally
		{
			context = null;

			connectionFactory = null;
		}
	}

	public static MessageProducer createQueueProducer(QueueSession session,
			Queue queue, long timeout, boolean isPersistent)
			throws JMSException
	{
		MessageProducer producer = session.createProducer(queue);

		if (!isPersistent)
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		if (timeout > 0)
			producer.setTimeToLive(timeout);
		return producer;
	}
}
