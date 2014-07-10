package com.crm.subscriber.thread;

import java.sql.PreparedStatement;
import java.util.Date;

import com.fss.queue.Message;
import com.fss.util.AppException;

import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.marketing.cache.CampaignEntry;
import com.crm.marketing.cache.CampaignFactory;
import com.crm.subscriber.message.SubscriberMessage;
import com.crm.thread.DatasourceInstance;
import com.crm.util.DateUtil;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: FPT
 * </p>
 * 
 * @author Vu Anh Dung
 * @version 1.0 Purpose : Base class for other threads
 */

public class MigrateInstance extends DatasourceInstance
{
	private PreparedStatement	stmtSubscriber	= null;
	private PreparedStatement	stmtOrder		= null;
	private PreparedStatement	stmtRanking		= null;
	private PreparedStatement	stmtCampaign	= null;

	private ProductEntry		usage			= null;
	private CampaignEntry		ageActive		= null;
	private CampaignEntry		ageNetwork		= null;

	private int					batchSize		= 1000;
	private int					batchCounter	= 0;

	public MigrateInstance() throws Exception
	{
		super();
	}

	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		// cut off date
		if ((mcnMain == null) || mcnMain.isClosed())
		{
			mcnMain = Database.getConnection();
			mcnMain.setAutoCommit(false);
		}

		String SQL = "Insert into SubscriberEntry "
				+ " (subscriberId, userId, userName, createDate, modifiedDate "
				+ " , isdn, subscriberType, registerDate, activeDate, barringStatus, supplierStatus) "
				+ "Values "
				+ " (?, 0, 'system', sysDate, sysDate "
				+ " , ?, ?, ?, sysDate, ?, ?) ";

		stmtSubscriber = mcnMain.prepareStatement(SQL);

		SQL = "Insert into SubscriberOrder "
				+ " (orderId, userId, userName, createDate, modifiedDate, orderType, orderDate, cycleDate "
				+ " , subscriberId, isdn, subscriberType, productId, amount, score, status) "
				+ "Values "
				+ " (order_seq.nextval, 0, 'system', sysDate, sysDate, ?, ?, ? "
				+ " , ?, ?, ?, ?, ?, ?, 0) ";

		stmtOrder = mcnMain.prepareStatement(SQL);

		SQL = "Insert into SubscriberRank "
				+ " (subRankId, userId, userName, createDate, modifiedDate, cycleDate, balanceType "
				+ " , subscriberId, isdn, subscriberType, currentAmount, totalAmount, status) "
				+ "Values "
				+ " (sub_rank_seq.nextval, 0, 'system', sysDate, sysDate, ?, 'LOYALTY' "
				+ " , ?, ?, ?, ?, ?, 0) ";

		stmtRanking = mcnMain.prepareStatement(SQL);

		SQL = "Insert into SubscriberCampaign "
				+ " (subCampaignId, userId, userName, createDate, modifiedDate "
				+ " , subscriberId, isdn, subscriberType, campaignId, nextRunDate, status) "
				+ "Values "
				+ " (sub_campaign_seq.nextval, 0, 'system', sysDate, sysDate "
				+ " , ?, ?, ?, ?, sysDate, 0) ";

		stmtCampaign = mcnMain.prepareStatement(SQL);

		// caching product
		ProductFactory.loadCache(new Date());

		usage = ProductFactory.getCache().getProduct("USAGE");
		ageNetwork = CampaignFactory.getCache().getCampaign("LONG_AGE");
		ageActive = CampaignFactory.getCache().getCampaign("LONG_ACTIVE");
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	public void afterProcessSession() throws Exception
	{
		try
		{
			if (batchCounter > 0)
			{
				runBatch();
			}

			Database.closeObject(stmtSubscriber);
			Database.closeObject(stmtOrder);
			Database.closeObject(stmtRanking);
			Database.closeObject(stmtCampaign);

			Database.closeObject(mcnMain);
		}
		finally
		{
			super.afterProcessSession();
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: HiepTH
	// Modify DateTime: 08/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public int processMessage(Message message) throws Exception
	{
		SubscriberMessage request = (SubscriberMessage) message;

		try
		{
			long subscriberId = request.getSubscriberId();
			
			if (request.getCycleDate() == null)
			{
				request.setCycleDate(DateUtil.truncMonth(request.getOrderDate()));
			}
			
			// subscriber status
			int supplierStatus = Constants.SUPPLIER_ACTIVE_STATUS;

			if (request.getNetworkStatus() == 2)
			{
				supplierStatus = Constants.SUPPLIER_ACTIVE_STATUS;
			}
			else if (request.getNetworkStatus() == 7)
			{
				supplierStatus = Constants.SUPPLIER_ACTIVE_STATUS;
			}
			else if (request.getNetworkStatus() == 50)
			{
				supplierStatus = Constants.SUPPLIER_BARRING_STATUS;
			}
			else if (request.getNetworkStatus() == 51)
			{
				supplierStatus = Constants.SUPPLIER_BARRING_STATUS;
			}
			else if (request.getNetworkStatus() == 52)
			{
				supplierStatus = Constants.SUPPLIER_CANCEL_STATUS;
			}

			request.setBarringStatus(request.getNetworkStatus());
			request.setSupplierStatus(supplierStatus);

			if ((request.getRegisterDate() != null) && (supplierStatus == Constants.SUPPLIER_ACTIVE_STATUS))
			{
				request.setActiveDate(request.getRegisterDate());
			}

			// usage score
			double scoreUsage = usage.getScore(request.getAmount(), request.getOrderDate());

			// database import
			stmtSubscriber.setLong(1, subscriberId);
			stmtSubscriber.setString(2, request.getIsdn());
			stmtSubscriber.setInt(3, request.getSubscriberType());
			stmtSubscriber.setDate(4, DateUtil.getDateSQL(request.getRegisterDate()));
			stmtSubscriber.setInt(5, request.getBarringStatus());
			stmtSubscriber.setInt(6, request.getSupplierStatus());
			stmtSubscriber.addBatch();

			// usage, active, network score
			if (request.getAmount() > 0)
			{
				stmtOrder.setString(1, "usage");
				stmtOrder.setDate(2, DateUtil.getDateSQL(request.getOrderDate()));
				stmtOrder.setDate(3, DateUtil.getDateSQL(request.getCycleDate()));
				stmtOrder.setLong(4, subscriberId);
				stmtOrder.setString(5, request.getIsdn());
				stmtOrder.setInt(6, request.getSubscriberType());
				stmtOrder.setLong(7, usage.getProductId());
				stmtOrder.setDouble(8, request.getAmount());
				stmtOrder.setDouble(9, scoreUsage);
				stmtOrder.addBatch();
			}

			// campaign schedule
			stmtCampaign.setLong(1, subscriberId);
			stmtCampaign.setString(2, request.getIsdn());
			stmtCampaign.setInt(3, request.getSubscriberType());
			stmtCampaign.setLong(4, ageNetwork.getCampaignId());
			stmtCampaign.addBatch();

			stmtCampaign.setLong(1, subscriberId);
			stmtCampaign.setString(2, request.getIsdn());
			stmtCampaign.setInt(3, request.getSubscriberType());
			stmtCampaign.setLong(4, ageActive.getCampaignId());
			stmtCampaign.addBatch();

			batchCounter++;
			if (batchCounter >= batchSize)
			{
				runBatch();

				batchCounter = 0;
			}
		}
		catch (AppException e)
		{
			request.setStatus(Constants.ORDER_STATUS_DENIED);
			request.setDescription(e.getMessage());

			error = e;

			return Constants.BIND_ACTION_ERROR;
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
	public void runBatch() throws Exception
	{
		stmtSubscriber.executeBatch();
		stmtOrder.executeBatch();
		stmtRanking.executeBatch();
		stmtCampaign.executeBatch();

		mcnMain.commit();

		stmtSubscriber.clearBatch();
		stmtOrder.clearBatch();
		stmtRanking.clearBatch();
		stmtCampaign.clearBatch();
	}

}
