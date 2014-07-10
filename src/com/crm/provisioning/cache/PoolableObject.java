package com.crm.provisioning.cache;

public interface PoolableObject
{
	public void activate();
	
	public void destroy();
	
	public void passivate();
	
	public boolean validate();
}
