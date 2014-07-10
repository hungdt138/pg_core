package com.crm.ascs.net;

public interface INetAnalyzer
{
	public void createObject(Object data, INetDataCollection collection);

	public void setHandler(INetHandler handler);

	public INetHandler getHandler();
}
