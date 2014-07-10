package com.crm.smscsim;

import com.logica.smpp.ServerPDUEventListener;
import com.logica.smpp.pdu.PDU;

public interface ISession
{	
	public void setPDUListener(ServerPDUEventListener listener);

	public PDU receive(long timeout) throws Exception;

	public void send(PDU pdu) throws Exception;

	public void endSession();
}
