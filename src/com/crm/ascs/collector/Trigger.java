package com.crm.ascs.collector;

import java.util.Date;

import com.crm.ascs.net.INetData;

public class Trigger implements INetData
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private String				triggerContent		= "";

	private Date				receiveTime			= new Date();

	private String				remoteHost			= "0.0.0.0";
	private int					remotePort				= 0;

	public void setReceiveTime(Date receiveTime)
	{
		this.receiveTime = receiveTime;
	}

	public Date getReceiveTime()
	{
		return receiveTime;
	}

	public void setContent(String triggerContent)
	{
		this.triggerContent = triggerContent;
	}

	@Override
	public String getContent()
	{
		return triggerContent;
	}

	@Override
	public byte[] getData()
	{
		return triggerContent.getBytes();
	}

	@Override
	public void setRemoteHost(String remoteHost)
	{
		this.remoteHost = remoteHost;
	}

	@Override
	public String getRemoteHost()
	{
		return remoteHost;
	}

	@Override
	public void setRemotePort(int remotePort)
	{
		this.remotePort = remotePort;
	}

	@Override
	public int getRemotePort()
	{
		return remotePort;
	}
}
