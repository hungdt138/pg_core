package com.crm.provisioning.cache;

import com.crm.kernel.index.IndexNode;
import com.crm.kernel.message.Constants;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.util.StringUtil;

import com.fss.util.AppException;

public class ProvisioningCommand extends IndexNode
{
	private long	provisioningId	= 0;

	private long	commandId		= 0;

	private int		priority		= 0;

	private int		timeout			= 0;

	private int		maxRetry		= 1;

	private boolean	logEnable		= true;

	private boolean	retryEnable		= true;

	private int		status			= 0;

	public ProvisioningCommand()
	{
		super();
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append(provisioningId + "\t");
		buffer.append(commandId + "\t");
		buffer.append(isWildcard() + "\t");

		return buffer.toString();
	}

	public CommandImpl getExecuteImpl()
	{
		return (CommandImpl) super.getExecuteImpl();
	}

	public long getProvisioningId()
	{
		return provisioningId;
	}

	public void setProvisioningId(long provisioningId)
	{
		this.provisioningId = provisioningId;
	}

	public long getCommandId()
	{
		return commandId;
	}

	public void setCommandId(long commandId)
	{
		this.commandId = commandId;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public int getMaxRetry()
	{
		return maxRetry;
	}

	public void setMaxRetry(int maxRetry)
	{
		this.maxRetry = maxRetry;
	}

	public boolean isLogEnable()
	{
		return logEnable;
	}

	public void setLogEnable(boolean logEnable)
	{
		this.logEnable = logEnable;
	}

	public synchronized void setProcessMethod(String processMethod) throws Exception
	{
		try
		{
			processMethod = StringUtil.nvl(processMethod, "");

			if (processMethod.equals(""))
			{
				throw new AppException(Constants.ERROR_PROCESS_METHOD);
			}
			else if (executeImpl == null)
			{
				throw new AppException(Constants.ERROR_PROCESS_CLASS);
			}

			String processClass = executeImpl.getClass().getName();

			executeMethod = Class.forName(processClass).getMethod(
					processMethod, CommandInstance.class, ProvisioningCommand.class, CommandMessage.class);
		}
		catch (Exception e)
		{
			executeMethod = null;

			throw e;
		}
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public boolean isRetryEnable()
	{
		return retryEnable;
	}

	public void setRetryEnable(boolean retryEnable)
	{
		this.retryEnable = retryEnable;
	}
}
