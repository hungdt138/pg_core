package com.crm.thread;

import java.util.Date;

import com.crm.provisioning.message.CommandMessage;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Phan Viet Thang
 * @version 1.0
 */

public class DatasourceInstance extends DispatcherInstance
{
	public DatasourceInstance() throws Exception
	{
		super();
	}
	
	public boolean isTimeout(CommandMessage request)
	{
		if (request.getOrderDate().getTime() + request.getTimeout() < (new Date()).getTime())
		{
			return true;
		}
		return false;
	}
	
}
