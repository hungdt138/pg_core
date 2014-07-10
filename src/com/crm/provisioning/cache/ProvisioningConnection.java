/**
 * 
 */
package com.crm.provisioning.cache;

import java.util.Date;

import com.crm.provisioning.cache.ProvisioningEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.thread.ProvisioningThread;
import com.crm.util.AppProperties;

/**
 * @author ThangPV
 * 
 */
public class ProvisioningConnection
{
	public long					provisioningId	= 0;

	public ProvisioningEntry	configuration	= null;

	public ProvisioningThread	dispatcher		= null;

	public String				host			= "";
	public int					port			= 0;
	public String				userName		= "";
	public String				password		= "";
	public String				testCommand		= "";
	public long					timeout			= 30000;

	public AppProperties		parameters		= new AppProperties();

	public boolean				closed			= true;
	
	public long					connectionId	= 0;
	
	public Date					lastRun			= new Date();

	public ProvisioningConnection()
	{
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

	public boolean openConnection() throws Exception
	{
		closed = false;

		return true;
	}

	public boolean closeConnection() throws Exception
	{
		closed = true;

		return true;
	}

	public boolean destroyConnection() throws Exception
	{
		try
		{
			closeConnection();
		}
		catch (Exception e)
		{
			dispatcher.logMonitor(e);
		}

		return true;
	}

	public void setProvisioningId(long provisioningId) throws Exception
	{
		this.provisioningId = provisioningId;

		setConfiguration(ProvisioningFactory.getCache().getProvisioning(provisioningId));
	}

	public long getProvisioningId()
	{
		return provisioningId;
	}

	public boolean isOpened()
	{
		return !closed;
	}

	public void activate() throws Exception
	{
		//dispatcher.debugMonitor("Activate connection: " + getConfiguration().getIndexKey());
	}

	public void passivate() throws Exception
	{
		//dispatcher.debugMonitor("Passivate connection: " + getConfiguration().getIndexKey());
	}

	public boolean validate() throws Exception
	{
		//dispatcher.debugMonitor("Validate connection: " + getConfiguration().getIndexKey());

		return true;
	}

	public ProvisioningThread getDispatcher()
	{
		return dispatcher;
	}

	public void setDispatcher(ProvisioningThread dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public boolean isClosed()
	{
		return closed;
	}

	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}

	public ProvisioningEntry getConfiguration()
	{
		return configuration;
	}

	public void setConfiguration(ProvisioningEntry configuration)
	{
		this.configuration = configuration;
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

	public long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	public AppProperties getParameters()
	{
		return parameters;
	}

	public void setParameters(AppProperties parameters) throws Exception
	{
		this.parameters = parameters;
	}

	public String getTestCommand()
	{
		return testCommand;
	}

	public void setTestCommand(String testCommand)
	{
		this.testCommand = testCommand;
	}
	
	public long getConnectionId()
	{
		return connectionId;
	}

	public void setConnectionId(long connectionId)
	{
		this.connectionId = connectionId;
	}
	
	public Date getLastRun()
	{
		return lastRun;
	}

	public void setLastRun(Date lastRun)
	{
		this.lastRun = lastRun;
	}
}
