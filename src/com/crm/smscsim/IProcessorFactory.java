package com.crm.smscsim;

import com.logica.smpp.pdu.BindRequest;

public interface IProcessorFactory
{
	public void setReceiveTimeout(long timeout);
	
	public long getReceiveTimeout();

	public IProcessor createProcessor(ISession session);
	
	public IProcessor stopProcessor(IProcessor processor);
	
	public void stopAllProcessor();

	public int authenticate(IProcessor processor, BindRequest request);

	public void debugMonitor(Object message);
}
