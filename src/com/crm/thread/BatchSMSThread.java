package com.crm.thread;

import java.sql.ResultSet;

import javax.jms.Message;

import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.DBQueueThread;
import com.crm.util.StringUtil;

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

public class BatchSMSThread extends DBQueueThread
{
	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	protected CommandMessage createMessage(ResultSet rsMessage) throws Exception
	{
		CommandMessage command = new CommandMessage();

		command.setChannel("SMS");
		command.setIsdn(StringUtil.nvl(rsMessage.getString("isdn"),""));
		command.setServiceAddress(StringUtil.nvl(rsMessage.getString("serviceAddress"),""));
		
		command.setShipTo(StringUtil.nvl(rsMessage.getString("shipTo"),""));
		
		if (command.getShipTo().equals(""))
		{
			command.setShipTo(command.getIsdn());
		}
		
		command.setKeyword(rsMessage.getString("keyword"));
		command.setRequest(rsMessage.getString("objRequest"));
		command.setUserId(rsMessage.getLong("userId"));
		command.setUserName(rsMessage.getString("userName"));
		
		return command;
		//return QueueFactory.createObjectMessage(queueSession, command);
	}
}
