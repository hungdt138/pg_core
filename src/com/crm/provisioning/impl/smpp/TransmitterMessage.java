package com.crm.provisioning.impl.smpp;

import com.crm.provisioning.message.CommandMessage;

public class TransmitterMessage
{
	private int		sequenceNumber	= 0;
	private CommandMessage 	message = null;
	
	public int getSequenceNumber()
	{
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}
	public CommandMessage getMessage()
	{
		return message;
	}
	public void setMessage(CommandMessage message)
	{
		this.message = message;
	}
	
	@Override
	public boolean equals(Object paramObject)
	{
		if (paramObject == null)
			return false;
		
		if(paramObject instanceof TransmitterMessage)
		{
			return ((TransmitterMessage)paramObject).getSequenceNumber() == sequenceNumber;
		}
		else
		{
			return super.equals(paramObject);
		}
	}
}
