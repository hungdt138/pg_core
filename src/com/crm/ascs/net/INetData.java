package com.crm.ascs.net;

import java.io.Serializable;

public interface INetData extends Serializable
{
	public byte[] getData();
	
	public String getContent();
	
	public void setRemoteHost(String remoteHost);
	public String getRemoteHost();
	
	public void setRemotePort(int remotePort);
	public int getRemotePort();
}
