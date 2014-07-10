package com.crm.provisioning.thread;

import java.util.Vector;

import com.crm.thread.util.ThreadUtil;
import com.fss.thread.ParameterType;
import com.fss.util.AppException;

public class EMACommandThread extends CommandThread
{
	public long		enquireInterval		= 10;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(createParameterDefinition("enquireInterval"
				, "", ParameterType.PARAM_TEXTBOX_MASK, "999999", ""));
		vtReturn.addAll(super.getDispatcherDefinition());
		
		return vtReturn;
	}
	
	public void fillDispatcherParameter() throws AppException
	{
		try
		{
			super.fillDispatcherParameter();
			enquireInterval = ThreadUtil.getLong(this, "enquireInterval", 3000);
		}
		catch (AppException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
