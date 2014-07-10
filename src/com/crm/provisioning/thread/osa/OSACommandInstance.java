package com.crm.provisioning.thread.osa;

import javax.jms.Queue;

import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.thread.CommandInstance;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright:
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author: HungPQ
 * @version 1.0
 */

public class OSACommandInstance extends CommandInstance
{
	// //////////////////////////////////////////////////////
	// Queue variables
	// //////////////////////////////////////////////////////
	public Queue	queueCallback		= null;

	public OSACommandInstance() throws Exception
	{
		super();
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void initQueue() throws Exception
	{
		super.initQueue();

		try
		{
			queueCallback = QueueFactory.getQueue(QueueFactory.COMMAND_CALLBACK);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

}
