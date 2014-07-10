package com.crm.marketing.thread;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.jms.Message;

import com.crm.thread.DatasourceThread;
import com.crm.thread.util.ThreadUtil;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.marketing.cache.CampaignFactory;
import com.crm.marketing.message.CampaignMessage;

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

public class CampaignThread extends DatasourceThread
{
	public PreparedStatement	stmtCampaignList	= null;
	public ResultSet			rsCampaignList		= null;

	public int					totalCount			= 0;
	public int					orderCount			= 0;

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public Serializable createLocalMessage() throws Exception
	{
		CampaignMessage message = new CampaignMessage();

		message.setSubCampaignId(rsCampaignList.getLong("subCampaignId"));
		message.setSubscriberId(rsCampaignList.getLong("subscriberId"));
		message.setCampaignId(rsCampaignList.getLong("campaignId"));
		message.setIsdn(rsCampaignList.getString("isdn"));
		message.setSubscriberType(rsCampaignList.getInt("subscriberType"));

		//return QueueFactory.createObjectMessage(queueSession, message);
		
		return message;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Event raised when session prepare to run
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void prepareDatasource() throws Exception
	{
		rsCampaignList = stmtCampaignList.executeQuery();

		super.prepareDatasource();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data target
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean next() throws Exception
	{
		return rsCampaignList.next() && isAvailable();
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		try
		{
			super.beforeProcessSession();

			CampaignFactory.loadCache(new Date());

			mcnMain = Database.getConnection();

			String SQL = ThreadUtil.getString(this, "sqlStatement", false, "");

			if (SQL.equals(""))
			{
				SQL = "Select rowId, A.* From SubscriberCampaign A Where nextRunDate <= sysDate";
			}

			stmtCampaignList = mcnMain.prepareStatement(SQL);
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
			Database.closeObject(rsCampaignList);
			Database.closeObject(stmtCampaignList);
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
