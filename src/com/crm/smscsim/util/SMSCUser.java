package com.crm.smscsim.util;

public class SMSCUser
{

	private String	userId				= "";
	private String	password			= "";
	private int		connectionLimit		= 0;
	private boolean	enableTransmitter	= true;
	private boolean	enableReceiver		= true;
	private long	timeout				= 0;

	public boolean isTransmitterEnabled()
	{
		return enableTransmitter;
	}

	public boolean isReceiverEnabled()
	{
		return enableReceiver;
	}

	public boolean isTranceiverEnabled()
	{
		return enableReceiver && enableTransmitter;
	}

	public void disableReceiver()
	{
		enableReceiver = false;
	}

	public void disableTransmitter()
	{
		enableTransmitter = false;
	}

	public void enableReceiver()
	{
		enableReceiver = true;
	}

	public void enableTransmitter()
	{
		enableTransmitter = true;
	}

	public void enableTransceiver()
	{
		enableTransmitter = true;
		enableReceiver = true;
	}
	
	public long getTimeout()
	{
		return timeout;
	}
	
	public void setTimeout(long timeout)
	{
		this.timeout= timeout; 
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public int getConnectionLimit()
	{
		return connectionLimit;
	}

	public void setConnectionLimit(int connectionLimit)
	{
		this.connectionLimit = connectionLimit;
	}

	public SMSCUser()
	{
	}

	public SMSCUser(String userId, String password, int connectionLimit)
	{
		setUserId(userId);
		setPassword(password);
		setConnectionLimit(connectionLimit);
	}
}
