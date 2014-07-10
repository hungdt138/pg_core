package com.crm.ascs.net;

public interface INetConnection
{	
	public void setHandler(INetHandler handler);
	public INetHandler getHandler();
}
