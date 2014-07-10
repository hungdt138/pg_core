package com.crm.smscsim;

import com.crm.smscsim.util.SMSCUser;
import com.logica.smpp.ServerPDUEventListener;
import com.logica.smpp.pdu.Request;

public interface IProcessor extends ServerPDUEventListener
{
	public int getProcessorId();
	
	public IProcessorFactory getFactory();

	public int getBindType();

	public String getSystemId();

	public void setUser(SMSCUser user);
	
	public SMSCUser getUser();
	
	public void serverRequest(Request request) throws Exception;

	public void disconnect();
	
	public void stop();

	public void debugMonitor(Object message);
	
	public boolean validate();
}
