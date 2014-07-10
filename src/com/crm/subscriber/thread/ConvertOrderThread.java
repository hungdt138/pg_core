package com.crm.subscriber.thread;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.jms.Message;

import com.crm.thread.DatasourceThread;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.message.CommandMessage;
import com.crm.subscriber.message.SubscriberMessage;

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

public class ConvertOrderThread extends DatasourceThread
{
	protected PreparedStatement	stmtQueue			= null;
	protected ResultSet			rsQueue				= null;
	
	protected String			queueTable			= "";
	protected String			indicatorField		= "";

	protected int				minFreeSize			= 15000;
	protected int				delayTimeWhenBusy	= 15000;

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public Serializable createLocalMessage() throws Exception
	{
		SubscriberMessage content = new SubscriberMessage();

		if (indicatorField.equals(""))
		{
			content.setRequestId(rsQueue.getLong("subscriberId"));
		}
		else
		{
			content.setRequestId(rsQueue.getLong(indicatorField));
		}
		
		content.setSubscriberId(rsQueue.getLong("subscriberId"));
		content.setIsdn(rsQueue.getString("isdn"));
		content.setSubscriberType(rsQueue.getInt("subscriberType"));
		
		content.setNetworkStatus(rsQueue.getInt("networkStatus"));
		content.setRegisterDate(rsQueue.getDate("registerDate"));
		
		content.setOrderType(rsQueue.getString("orderType"));
		content.setOrderDate(rsQueue.getDate("orderDate"));
		content.setSKU(rsQueue.getString("SKU"));
		
		content.setAmount(rsQueue.getDouble("amount"));
		
		return content;

		//return QueueFactory.createObjectMessage(queueSession, content);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Event raised when session prepare to run
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	protected void prepareDatasource() throws Exception
	{
		rsQueue = stmtQueue.executeQuery();

		super.prepareDatasource();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data target
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean next() throws Exception
	{
		return rsQueue.next() && isAvailable();
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		try
		{
			super.beforeProcessSession();

			if ((mcnMain == null) || mcnMain.isClosed())
			{
				mcnMain = Database.getConnection();
				mcnMain.setAutoCommit(false);
			}

			stmtQueue = mcnMain.prepareStatement("Select * From " + queueTable);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void afterProcessSession() throws Exception
	{
		try
		{
			Database.closeObject(rsQueue);
			Database.closeObject(stmtQueue);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			super.afterProcessSession();
		}
	}
}
