package com.crm.ascs.net;

public interface INetHandler extends Runnable
{
	public INetAnalyzer getAnalyzer();

	public void setAnalyzer(INetAnalyzer analyzer);

	public void handle(byte[] data);

	public INetConnection getConnection();
}
