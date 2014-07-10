package com.crm.provisioning.impl.ccws;

import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;

public class loyaltyCCWSCommandImpl extends CommandImpl
{
	public CommandMessage modifyBalance(CommandInstance instance, ProvisioningCommand command, CommandMessage request)
	{
		return request;
	}
	
	public CommandMessage unModifyBalance(CommandInstance instance, ProvisioningCommand command, CommandMessage request)
	{
		return request;
	}
}
