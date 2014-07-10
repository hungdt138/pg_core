package com.crm.subscriber.thread;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.jms.Message;

import com.crm.subscriber.message.SubscriberMessage;
import com.crm.thread.DatasourceThread;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.loyalty.cache.RankFactory;

import com.fss.util.AppException;

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

public class RankingThread extends DatasourceThread
{
	protected PreparedStatement	stmtRankingList	= null;
	protected ResultSet			rsList			= null;

	protected String			sqlStatement	= "";

	protected Date				cycleDate		= null;

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
		rsList = stmtRankingList.executeQuery();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data target
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean next() throws Exception
	{
		return rsList.next();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public Serializable createLocalMessage() throws Exception
	{
		try
		{
			if (rsList.getInt("supplierStatus") == Constants.SUPPLIER_CANCEL_STATUS)
			{
				return null;
			}

			SubscriberMessage message = new SubscriberMessage();

			message.setSubscriberId(rsList.getLong("subscriberId"));
			message.setCampaignId(rsList.getLong("campaignId"));
			message.setIsdn(rsList.getString("isdn"));
			message.setSubscriberType(rsList.getInt("subscriberType"));

			message.setCycleDate(cycleDate);

			message.setRankId(rsList.getLong("rankId"));
			message.setRankStartDate(rsList.getDate("rankStartDate"));
			message.setRankExpirationDate(rsList.getDate("rankExpirationDate"));

			//return QueueFactory.createObjectMessage(queueSession, message);
			return message;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws AppException
	{
		try
		{
			super.beforeProcessSession();

			// statement
			mcnMain = Database.getConnection();

			if (!sqlStatement.equals(""))
			{
				stmtRankingList = mcnMain.prepareStatement(sqlStatement);
			}
			else
			{
				stmtRankingList = mcnMain.prepareStatement("Select * From SubscriberEntry Where rownum < 1000");
			}

			RankFactory.loadCache(new Date());
		}
		catch (AppException e)
		{
			logMonitor(e);

			throw e;
		}
		catch (Exception e)
		{
			logMonitor(e);

			throw new AppException(e.getMessage());
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
			Database.closeObject(rsList);
			Database.closeObject(stmtRankingList);
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
