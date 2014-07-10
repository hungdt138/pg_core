package com.crm.smscsim;

public class SMSCException extends Exception
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public SMSCException()
	{
		super("SMSC exception");
	}
	
	public SMSCException(String message)
	{
		super(message);
	}
	
	public SMSCException(Throwable cause)
	{
		super(cause);
	}
	
	public SMSCException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
