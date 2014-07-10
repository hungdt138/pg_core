package com.crm.provisioning.impl.charging;

import com.crm.thread.DispatcherThread;

public interface TCPDataListener
{
	public void setTCPClient(TCPClient client);

	public void onReceive(byte data[]) throws Exception;

	public Object getResponse(String sequense);

	public void setDispatcher(DispatcherThread dispatcher);

	public DispatcherThread getDispatcher();
}
