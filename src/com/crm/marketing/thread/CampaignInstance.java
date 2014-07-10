package com.crm.marketing.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.crm.thread.DatasourceInstance;
import com.crm.util.DateUtil;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.marketing.cache.CampaignEntry;
import com.crm.marketing.cache.CampaignFactory;
import com.crm.marketing.message.CampaignMessage;

import com.fss.queue.Message;
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

public class CampaignInstance extends DatasourceInstance
{
	public PreparedStatement	stmtCampaignSchedule	= null;
	public PreparedStatement	stmtSubscriber			= null;
	public PreparedStatement	stmtOrder				= null;

	public CampaignInstance() throws Exception
	{
		super();
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void updateCampaignSchedule(CampaignMessage request) throws Exception
	{
		try
		{
			stmtCampaignSchedule.setDate(1, DateUtil.getDateSQL(request.getNextRunDate()));
			stmtCampaignSchedule.setLong(2, request.getSubCampaignId());

			stmtCampaignSchedule.execute();
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
	public void createOrder(CampaignMessage request) throws Exception
	{
		try
		{
			stmtOrder.setLong(1, request.getSubscriberId());
			stmtOrder.setLong(2, request.getSubProductId());
			stmtOrder.setLong(3, request.getProductId());
			stmtOrder.setString(4, "");

			stmtOrder.setString(5, "promotion");
			stmtOrder.setLong(6, request.getOrderId());
			stmtOrder.setDate(7, DateUtil.getDateSQL(request.getOrderDate()));
			stmtOrder.setDate(8, DateUtil.getDateSQL(request.getCycleDate()));

			stmtOrder.setString(9, request.getIsdn());
			stmtOrder.setInt(10, request.getSubscriberType());

			stmtOrder.setDouble(11, request.getAmount());
			stmtOrder.setDouble(12, request.getScore());
			stmtOrder.setInt(13, request.getStatus());

			stmtOrder.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public int processCampaign(Connection connection, CampaignEntry campaign, CampaignMessage request) throws Exception
	{
		try
		{
			if (getSubscriberInfo(request) != Constants.BIND_ACTION_SUCCESS)
			{
				return Constants.BIND_ACTION_ERROR;
			}

			int action = Constants.BIND_ACTION_NONE;

			if (campaign.getExecuteImpl() != null)
			{
				action = campaign.getExecuteImpl().processMessage(connection, campaign, request);
			}

			if ((action == Constants.BIND_ACTION_SUCCESS) && (request.getStatus() == Constants.ORDER_STATUS_APPROVED))
			{
				createOrder(request);
			}

			// checking next run date
			if ((action != Constants.BIND_ACTION_ERROR) && (campaign.getExecuteImpl() != null))
			{
				request.setNextRunDate(campaign.getExecuteImpl().getNextDate(connection, campaign, request));
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		return Constants.BIND_ACTION_SUCCESS;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int getSubscriberInfo(CampaignMessage request) throws Exception
	{
		ResultSet rsSubscriber = null;

		try
		{
			if (stmtSubscriber != null)
			{
				stmtSubscriber.setLong(1, request.getSubscriberId());

				rsSubscriber = stmtSubscriber.executeQuery();

				if (rsSubscriber.next())
				{
					request.setRegisterDate(rsSubscriber.getDate("registerDate"));
					request.setActiveDate(rsSubscriber.getDate("activeDate"));
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsSubscriber);
		}

		// return Constants.BIND_ACTION_SUCCESS;
		return Constants.BIND_ACTION_SUCCESS;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int processMessage(Message message) throws Exception
	{
		int action = Constants.BIND_ACTION_SUCCESS;

		CampaignEntry campaign = null;
		CampaignMessage request = (CampaignMessage) message;

		try
		{
			if (request == null)
			{
				return Constants.BIND_ACTION_NONE;
			}
			else if ((request.getNextRunDate() != null) && request.getNextRunDate().after(new Date()))
			{
				return Constants.BIND_ACTION_BYPASS;
			}

			campaign = CampaignFactory.getCache().getCampaign(request.getCampaignId());

			if (campaign == null)
			{
				throw new AppException("campaign-not-found");
			}

			request.setProductId(campaign.getProductId());

			action = processCampaign(mcnMain, campaign, request);

			if (action != Constants.BIND_ACTION_NONE)
			{
				updateCampaignSchedule(request);
			}

			if ((mcnMain != null) && !mcnMain.isClosed())
			{
				mcnMain.commit();
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (campaign != null)
			{
				debugMonitor(request.getIsdn() + " : " + request.getNextRunDate() + " for campaign " + campaign.getIndexKey());
			}
		}

		return action;
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			// cut off date
			if ((mcnMain == null) || mcnMain.isClosed())
			{
				mcnMain = Database.getConnection();
				mcnMain.setAutoCommit(false);
			}

			String SQL = "";

			SQL = "Update SubscriberCampaign Set modifiedDate = sysDate, nextRunDate = ? Where subCampaignId = ? ";

			stmtCampaignSchedule = mcnMain.prepareStatement(SQL);

			SQL = "Insert into SubscriberOrder "
					+ "		(orderId, userId, userName, createDate, modifiedDate, subscriberId, subProductId, productId, SKU "
					+ "		, orderType, orderNo, orderDate, cycleDate, isdn, subscriberType "
					+ "		, offerPrice, price, quantity, discount, amount, score, status) "
					+ " Values "
					+ "		(order_seq.nextval, 0, 'system', sysDate, sysDate, ?, ?, ?, ? "
					+ "		, ?, ?, ?, ?, ?, ? "
					+ "		, 0, 0, 0, 0, ?, ?, ?) ";

			stmtOrder = mcnMain.prepareStatement(SQL);

			stmtSubscriber = mcnMain.prepareStatement("Select * From SubscriberEntry Where subscriberId = ?");
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
			Database.closeObject(stmtCampaignSchedule);
			Database.closeObject(stmtOrder);
			Database.closeObject(stmtSubscriber);
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
