/**
 * 
 */
package com.crm.provisioning.cache;

import java.util.HashMap;

import com.crm.kernel.index.IndexNode;
import com.fss.util.AppException;

/**
 * @author ThangPV<br>
 *         Edited by NamTA<br>
 *         Edited Date 21/08/2012<br>
 *         Edited Description: Add alias field.
 * 
 */
public class ProvisioningEntry extends IndexNode
{
	// //////////////////////////////////////////////////////
	// Common Configuration
	// //////////////////////////////////////////////////////
	private long									provisioningId		= 0;
	private String									alias				= "";
	private String									provisioningType	= "";

	private String									title				= "";

	private String									host				= "";
	private int										port				= 0;
	private String									userName			= "";
	private String									password			= "";

	private String									queueName			= "";

	private int										acquireIncrement	= 1;
	private int										idleTestPeriod		= 30;
	private int										maxWaitTime			= 300;
	private int										minIdleTime			= 300;
	private int										maxIdleTime			= 300;
	private int										maxPoolSize			= 100;
	private int										minPoolSize			= 10;
	private int										numHelperThreads	= 3;
	private String									testCommand			= "";
	private int										timeout				= 15000;

	private int										status				= 0;

	// //////////////////////////////////////////////////////
	// Command Configuration
	// //////////////////////////////////////////////////////
	private HashMap<Long, ProvisioningCommand>		actions				= new HashMap<Long, ProvisioningCommand>();

	private HashMap<String, ProvisioningMessage>	messages			= new HashMap<String, ProvisioningMessage>();

	public ProvisioningEntry(long provisioningId, String indexKey)
	{
		super(indexKey);
		setAlias(indexKey);
		setProvisioningId(provisioningId);
	}

	public long getProvisioningId()
	{
		return provisioningId;
	}

	public void setProvisioningId(long provisioningId)
	{
		this.provisioningId = provisioningId;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setProvisioningType(String provisioningType)
	{
		this.provisioningType = provisioningType;
	}

	public String getProvisioningType()
	{
		return provisioningType;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public HashMap<Long, ProvisioningCommand> getCommands()
	{
		return actions;
	}

	public void setCommands(HashMap<Long, ProvisioningCommand> commands)
	{
		this.actions = commands;
	}

	public HashMap<String, ProvisioningMessage> getMessages()
	{
		return messages;
	}

	public void setMessages(HashMap<String, ProvisioningMessage> messages)
	{
		this.messages = messages;
	}

	public ProvisioningCommand getAction(long commandId) throws AppException
	{
		ProvisioningCommand action = actions.get(commandId);

		if (action == null)
		{
			throw new AppException("action-not-found");
		}

		return action;
	}

	public ProvisioningMessage getMessage(String code)
	{
		return messages.get(code);
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

	public int getAcquireIncrement()
	{
		return acquireIncrement;
	}

	public void setAcquireIncrement(int acquireIncrement)
	{
		this.acquireIncrement = acquireIncrement;
	}

	public int getIdleTestPeriod()
	{
		return idleTestPeriod;
	}

	public void setIdleTestPeriod(int idleTestPeriod)
	{
		this.idleTestPeriod = idleTestPeriod;
	}

	public int getMaxWaitTime()
	{
		return maxWaitTime;
	}

	public void setMaxWaitTime(int maxWaitTime)
	{
		this.maxWaitTime = maxWaitTime;
	}

	public int getMinIdleTime()
	{
		return minIdleTime;
	}

	public void setMinIdleTime(int minIdleTime)
	{
		this.minIdleTime = minIdleTime;
	}

	public int getMaxIdleTime()
	{
		return maxIdleTime;
	}

	public void setMaxIdleTime(int maxIdleTime)
	{
		this.maxIdleTime = maxIdleTime;
	}

	public int getMaxPoolSize()
	{
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize)
	{
		this.maxPoolSize = maxPoolSize;
	}

	public int getMinPoolSize()
	{
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize)
	{
		this.minPoolSize = minPoolSize;
	}

	public int getNumHelperThreads()
	{
		return numHelperThreads;
	}

	public void setNumHelperThreads(int numHelperThreads)
	{
		this.numHelperThreads = numHelperThreads;
	}

	public String getTestCommand()
	{
		return testCommand;
	}

	public void setTestCommand(String testCommand)
	{
		this.testCommand = testCommand;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public String getQueueName()
	{
		return queueName;
	}

	public void setQueueName(String queueName)
	{
		this.queueName = queueName;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
