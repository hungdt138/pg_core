package com.crm.provisioning.cache;

import com.crm.kernel.index.IndexNode;
import com.crm.kernel.message.Constants;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;

public class CommandAction extends IndexNode
{
	private long	commandId		= 0;
	private long	productId		= 0;
	private String	actionType		= "";
	private String	actionName		= "";
	private int		subscriberType	= Constants.PREPAID_SUB_TYPE;
	private long	campaignId		= 0;

	private long	nextCommandId	= 0;
	private String	nextAction		= "";

	public CommandAction()
	{
		super();
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append(commandId + "\t");
		buffer.append(actionType + "\t");
		buffer.append(actionName + "\t");
		buffer.append(subscriberType + "\t");
		buffer.append(nextCommandId + "\t");

		return buffer.toString();
	}

	public CommandImpl getExecuteImpl()
	{
		return (CommandImpl) super.getExecuteImpl();
	}

	public long getCommandId()
	{
		return commandId;
	}

	public void setCommandId(long commandId)
	{
		this.commandId = commandId;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public String getActionType()
	{
		return actionType;
	}

	public void setActionType(String actionType)
	{
		this.actionType = actionType;
	}

	public String getActionName()
	{
		return actionName;
	}

	public void setActionName(String actionName)
	{
		this.actionName = actionName;
	}

	public int getSubscriberType()
	{
		return subscriberType;
	}

	public void setSubscriberType(int subscriberType)
	{
		this.subscriberType = subscriberType;
	}

	public long getCampaignId()
	{
		return campaignId;
	}

	public void setCampaignId(long campaignId)
	{
		this.campaignId = campaignId;
	}

	public long getNextCommandId()
	{
		return nextCommandId;
	}

	public void setNextCommandId(long nextCommandId)
	{
		this.nextCommandId = nextCommandId;
	}

	public synchronized void setProcessMethod(String processMethod) throws Exception
	{
		try
		{
			String processClass = executeImpl.getClass().getName();
			
			executeMethod = Class.forName(processClass).getMethod(processMethod, CommandAction.class, CommandMessage.class);
		}
		catch (Exception e)
		{
			executeImpl = null;
			executeMethod = null;

			throw e;
		}
	}

	public String getNextAction()
	{
		return nextAction;
	}

	public void setNextAction(String nextAction)
	{
		this.nextAction = nextAction;
	}

	public boolean equals(long productId, String actionType, int subscriberType, String cause)
	{
		return ((getProductId() == productId) && getActionType().equals(actionType)
				&& (getSubscriberType() == subscriberType) && (getActionName().equals(cause)));
	}
}
