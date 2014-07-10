package com.crm.thread;

import java.sql.ResultSet;

import javax.jms.Message;

import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.message.CommandMessage;

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

public class DBCommandAction extends DBQueueThread
{
	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	protected CommandMessage createMessage(ResultSet rsMessage) throws Exception
	{
		CommandMessage command = new CommandMessage();

		command.setChannel(rsMessage.getString("channel"));
		command.setIsdn(rsMessage.getString("isdn"));
		command.setServiceAddress(rsMessage.getString("serviceAddress"));
		command.setShipTo(rsMessage.getString("shipTo"));
		command.setKeyword(rsMessage.getString("keyword"));
		command.setRequest(rsMessage.getString("objRequest"));
		command.setUserId(rsMessage.getLong("userId"));
		command.setUserName(rsMessage.getString("userName"));

		return command;
	}
}
