/**
 * 
 */
package com.crm.provisioning.cache;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.index.IndexNode;

/**
 * @author ThangPV<br>
 *         Edited by NamTA<br>
 *         Edited Date 21/08/2012<br>
 *         Edited Description: Add alias field.
 * 
 */
public class CommandEntry extends IndexNode
{
	private long		commandId			= 0;
	private String		title				= "";
	private String		alias				= "";
	private String		provisioningType	= "";

	private boolean		retryEnable			= true;
	private int			maxRetry			= 1;
	private boolean		logEnable			= true;
	private int			priority			= 0;
	private int			timeout				= 0;

	private BinaryIndex	actions				= new BinaryIndex();

	public CommandEntry(long commandId, String alias)
	{
		super(alias);

		setCommandId(commandId);
	}

	public boolean isIgnoreError()
	{
		return parameters.getBoolean("ignoreError", false);
	}

	public long getCommandId()
	{
		return commandId;
	}

	public void setCommandId(long commandId)
	{
		this.commandId = commandId;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getAlias()
	{
		return alias;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getProvisioningType()
	{
		return provisioningType;
	}

	public void setProvisioningType(String provisioningType)
	{
		this.provisioningType = provisioningType;
	}

	public boolean isRetryEnable()
	{
		return retryEnable;
	}

	public void setRetryEnable(boolean retryEnable)
	{
		this.retryEnable = retryEnable;
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

	public BinaryIndex getActions()
	{
		return actions;
	}

	public void setActions(BinaryIndex actions)
	{
		this.actions = actions;
	}

	public CommandAction getAction(long productId, String actionType, String actionName, int subscriberType) throws Exception
	{
		for (IndexNode node : actions.getNodes())
		{
			CommandAction action = (CommandAction) node;

			if ((action.getProductId() == productId) && action.getActionType().equals(actionType)
					&& (action.getSubscriberType() == subscriberType) && (action.getActionName().equals(actionName)))
			{
				return action;
			}
		}

		return null;
	}

}
