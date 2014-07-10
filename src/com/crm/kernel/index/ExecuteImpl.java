/**
 * 
 */
package com.crm.kernel.index;

import javax.jms.Message;

import com.crm.util.AppProperties;

/**
 * @author ThangPV
 * 
 */
public class ExecuteImpl
{
	private AppProperties	properties	= new AppProperties();

	public void setProperties(AppProperties properties)
	{
		this.properties = properties;
	}

	public AppProperties getProperties()
	{
		return properties;
	}
	
	public void onMessage(Message message) throws Exception
	{
	}
	
	public boolean validate(Message message) throws Exception
	{
		if (message == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
