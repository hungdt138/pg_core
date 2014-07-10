/**
 * 
 */
package com.crm.provisioning.cache;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.crm.alarm.cache.AlarmEntry;
import com.crm.alarm.cache.AlarmFactory;
import com.crm.kernel.message.Constants;
import com.crm.provisioning.cache.ProvisioningEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.thread.ProvisioningThread;
import com.crm.util.AppProperties;
import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class ProvisioningPool implements PoolableObjectFactory
{
	// //////////////////////////////////////////////////////
	// Member variables
	// //////////////////////////////////////////////////////
	private ProvisioningThread	dispatcher			= null;

	private long				provisioningId		= 0;

	private String				provisioningClass	= "";

	private String				host				= "";
	private int					port				= 0;
	private String				userName			= "";
	private String				password			= "";

	private int					timeout				= 0;
	private String				testCommand			= "";

	private AppProperties		parameters			= new AppProperties();

	private GenericObjectPool	connectionPool		= null;

	public ProvisioningPool()
	{
		super();
	}

	public ProvisioningPool(ProvisioningThread dispatcher)
	{
		super();

		setDispatcher(dispatcher);
	}

	// //////////////////////////////////////////////////////
	// process file
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void logMonitor(Object message) throws Exception
	{
		if (dispatcher != null)
		{
			dispatcher.logMonitor(message);
		}
	}

	// //////////////////////////////////////////////////////
	// process file
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void debugMonitor(Object message) throws Exception
	{
		if (dispatcher != null)
		{
			dispatcher.debugMonitor(message);
		}
	}

	public ProvisioningThread getDispatcher()
	{
		return dispatcher;
	}

	public void setDispatcher(ProvisioningThread dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public GenericObjectPool getProvisioningPool()
	{
		return connectionPool;
	}

	public void setProvisioningPool(GenericObjectPool provisioningPool)
	{
		this.connectionPool = provisioningPool;
	}

	public void setProvisioningId(long provisioningId)
	{
		this.provisioningId = provisioningId;
	}

	public long getProvisioningId()
	{
		return provisioningId;
	}

	public ProvisioningEntry getProvisioning() throws Exception
	{
		return ProvisioningFactory.getCache().getProvisioning(provisioningId);
	}

	public void open(
			int maxActive, long maxWait, int maxIdle, int minIdle, boolean testOnBorrow, boolean testOnReturn
			, long timeBetweenEvictionRunsMillis, int numTestsPerEvictionRun, long minEvictableIdleTimeMillis
			, boolean testWhileIdle, long softMinEvictableIdleTimeMillis)
			throws Exception
	{
		try
		{
			close();

			connectionPool =
					new GenericObjectPool(
							this, maxActive, GenericObjectPool.WHEN_EXHAUSTED_BLOCK, maxWait, maxIdle, minIdle
							, testOnBorrow, testOnReturn
							, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, minEvictableIdleTimeMillis, testWhileIdle
							, softMinEvictableIdleTimeMillis, true);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void close() throws Exception
	{
		if (connectionPool != null)
		{
			debugMonitor("Close pool");
			connectionPool.close();
		}
	}

	public ProvisioningConnection getConnection() throws Exception
	{
		ProvisioningConnection connection = (ProvisioningConnection) connectionPool.borrowObject();
		
		connection.setTimeout(timeout);
		
		return connection;
	}

	public void closeConnection(ProvisioningConnection connection) throws Exception
	{
		if (connection == null)
		{
			return;
		}

		if (connectionPool != null)
		{
			connectionPool.returnObject(connection);
		}
	}

	@Override
	public void activateObject(Object connection) throws Exception
	{
		if (connection instanceof ProvisioningConnection)
		{
			((ProvisioningConnection) connection).activate();
		}
	}

	@Override
	public void destroyObject(Object connection) throws Exception
	{
		if (connection == null)
		{
			return;
		}

		((ProvisioningConnection) connection).destroyConnection();
	}

	@Override
	public Object makeObject() throws Exception
	{
		ProvisioningConnection connection = null;
		ProvisioningEntry provisioning = ProvisioningFactory.getCache().getProvisioning(provisioningId);

		if (provisioningClass.equals(""))
		{
			throw new AppException(Constants.ERROR_PROCESS_CLASS);
		}
		else
		{
			connection = (ProvisioningConnection) Class.forName(provisioningClass).newInstance();
		}

		connection.setDispatcher(dispatcher);
		connection.setProvisioningId(provisioningId);

		connection.setHost(host);
		connection.setPort(port);
		connection.setUserName(userName);
		connection.setPassword(password);
		connection.setTimeout(timeout);
		connection.setTestCommand(testCommand);

		connection.setParameters(parameters);

		connection.setConnectionId(Calendar.getInstance().getTimeInMillis());
		connection.setLastRun(new Date());
		
		connection.openConnection();
		debugMonitor("Created provisioning instance for " + provisioning.getIndexKey());

		return connection;
	}

	@Override
	public void passivateObject(Object connection) throws Exception
	{
		if (connection instanceof ProvisioningConnection)
		{
			((ProvisioningConnection) connection).passivate();
		}
	}

	@Override
	public boolean validateObject(Object connection)
	{
		if (connection instanceof ProvisioningConnection)
		{
			try
			{
				return ((ProvisioningConnection) connection).validate();
			}
			catch (Exception e)
			{
				dispatcher.logMonitor(e);
				try
				{
					debugMonitor("Sending alarm connection");
					
					dispatcher.sendInstanceAlarm(e, Constants.ERROR_CONNECTION, provisioningId,
							((ProvisioningThread) dispatcher).provisioningClass);
				}
				catch(Exception ex)
				{
				}
			}
		}

		return false;
	}

	public String getProvisioningClass()
	{
		return provisioningClass;
	}

	public void setProvisioningClass(String provisioningClass)
	{
		this.provisioningClass = provisioningClass;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public GenericObjectPool getConnectionPool()
	{
		return connectionPool;
	}

	public void setConnectionPool(GenericObjectPool connectionPool)
	{
		this.connectionPool = connectionPool;
	}

	public AppProperties getParameters()
	{
		return parameters;
	}

	public void setParameters(AppProperties parameters)
	{
		this.parameters = parameters;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public String getTestCommand()
	{
		return testCommand;
	}

	public void setTestCommand(String testCommand)
	{
		this.testCommand = testCommand;
	}
}
