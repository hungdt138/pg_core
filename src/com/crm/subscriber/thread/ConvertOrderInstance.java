package com.crm.subscriber.thread;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.fss.queue.Message;
import com.fss.util.AppException;

import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.marketing.cache.CampaignEntry;
import com.crm.marketing.cache.CampaignFactory;
import com.crm.subscriber.message.SubscriberMessage;
import com.crm.thread.DatasourceInstance;
import com.crm.thread.util.ThreadUtil;
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

public class ConvertOrderInstance extends DatasourceInstance
{
	private PreparedStatement	stmtSubscriber			= null;
	private PreparedStatement	stmtInsertSubscriber	= null;
	private PreparedStatement	stmtOrder				= null;
	private PreparedStatement	stmtInsertCampaign		= null;
	private PreparedStatement	stmtCampaign			= null;
	private PreparedStatement	stmtRemove				= null;

	public ConvertOrderInstance() throws Exception
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

		String SQL = "Merge into SubscriberEntry using dual on (subscriberId = ?) "
				+ "	When Matched then Update Set "
				+ "		modifiedDate = sysDate, activeDate = ?, barringStatus = ?, supplierStatus = ? "
				+ "When Not Matched then Insert "
				+ " (subscriberId, userId, userName, createDate, modifiedDate "
				+ " , isdn, subscriberType, registerDate, activeDate, barringStatus, supplierStatus) "
				+ "Values "
				+ " (?, 0, 'system', sysDate, sysDate "
				+ " , ?, ?, ?, sysDate, ?, ?) ";

		stmtInsertSubscriber = mcnMain.prepareStatement(SQL);

		SQL = "Select * From SubscriberEntry Where isdn = ? ";

		stmtSubscriber = mcnMain.prepareStatement(SQL);

		SQL = "Merge into SubscriberOrder using dual on (orderDate = ? and isdn = ? and subscriberId = ?) "
				+ "	When Matched then Update Set "
				+ "		modifiedDate = sysDate, amount = ?, score = ?, status = ? "
				+ "When Not Matched then Insert "
				+ " (orderId, userId, userName, createDate, modifiedDate, orderType, orderDate, cycleDate "
				+ " , subscriberId, isdn, subscriberType, productId, amount, score, status) "
				+ "Values "
				+ " (order_seq.nextval, 0, 'system', sysDate, sysDate, ?, ?, ? "
				+ " , ?, ?, ?, ?, ?, ?, ?) ";

		stmtOrder = mcnMain.prepareStatement(SQL);

		SQL = "Insert into SubscriberCampaign "
				+ " (subCampaignId, userId, userName, createDate, modifiedDate "
				+ " , subscriberId, isdn, subscriberType, campaignId, nextRunDate, status) "
				+ "Values "
				+ " (sub_campaign_seq.nextval, 0, 'system', sysDate, sysDate "
				+ " , ?, ?, ?, ?, sysDate, 0) ";

		stmtInsertCampaign = mcnMain.prepareStatement(SQL);

		SQL = "Select * From SubscriberCampaign Where isdn = ? and campaignId = ? and status = ?";

		stmtCampaign = mcnMain.prepareStatement(SQL);
		
		String queueTable = ThreadUtil.getString(getDispatcher(), "tableName", true, "");

		String indicatorField = ThreadUtil.getString(getDispatcher(), "indicatorField", true, "");

		if (!queueTable.equals("") && !indicatorField.equals(""))
		{
			SQL = "Delete " + queueTable + " Where " + indicatorField + " = ?";

			stmtRemove = mcnMain.prepareStatement(SQL);
		}
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
			Database.closeObject(stmtSubscriber);
			Database.closeObject(stmtInsertSubscriber);
			Database.closeObject(stmtOrder);
			Database.closeObject(stmtInsertCampaign);
			Database.closeObject(stmtCampaign);
			Database.closeObject(stmtRemove);

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

		ResultSet rsSubscriber = null;

		boolean isNew = false;

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
			else
			{
				supplierStatus = Constants.SUPPLIER_UNKNOW_STATUS;
			}

			request.setBarringStatus(request.getNetworkStatus());
			request.setSupplierStatus(supplierStatus);

			if (supplierStatus == Constants.SUPPLIER_ACTIVE_STATUS)
			{
				request.setActiveDate(request.getOrderDate());
			}
			else
			{
				request.setActiveDate(null);
			}

			// database import
			stmtSubscriber.setString(1, request.getIsdn());

			rsSubscriber = stmtSubscriber.executeQuery();

			if (rsSubscriber.next())
			{
				request.setSubscriberId(rsSubscriber.getLong("subscriberId"));

				Date activeDate = rsSubscriber.getDate("activeDate");

				if (request.getSupplierStatus() == Constants.SUPPLIER_UNKNOW_STATUS)
				{
					request.setActiveDate(activeDate);
				}
				else if (request.getSupplierStatus() == rsSubscriber.getInt("supplierStatus"))
				{
					request.setActiveDate(activeDate);
				}
			}
			else
			{
				isNew = true;

				request.setSubscriberId(Database.getSequence("subscriber_seq"));
			}

			// update into subscriber
			stmtInsertSubscriber.setLong(1, subscriberId);
			stmtInsertSubscriber.setDate(2, DateUtil.getDateSQL(request.getActiveDate()));
			stmtInsertSubscriber.setInt(3, request.getBarringStatus());
			stmtInsertSubscriber.setInt(4, request.getSupplierStatus());
			stmtInsertSubscriber.setLong(5, subscriberId);
			stmtInsertSubscriber.setString(6, request.getIsdn());
			stmtInsertSubscriber.setInt(7, request.getSubscriberType());
			stmtInsertSubscriber.setDate(8, DateUtil.getDateSQL(request.getRegisterDate()));
			stmtInsertSubscriber.setDate(9, DateUtil.getDateSQL(request.getActiveDate()));
			stmtInsertSubscriber.setInt(10, request.getBarringStatus());
			stmtInsertSubscriber.setInt(11, request.getSupplierStatus());
			stmtInsertSubscriber.execute();

			// score rating
			ProductEntry product = ProductFactory.getCache().getProduct(request.getSKU());

			double score = 0;

			if (product == null)
			{
				request.setStatus(Constants.ORDER_STATUS_DENIED);
				request.setCause("product-not-found");
			}
			else
			{
				request.setProductId(product.getProductId());
				
				try
				{
					score = product.getScore(request.getAmount(), request.getOrderDate());
				}
				catch (AppException e)
				{
					request.setStatus(Constants.ORDER_STATUS_DENIED);
					request.setCause(e.getMessage());
				}
			}

			if (request.getAmount() > 0)
			{
				stmtOrder.setString(1, request.getOrderType());
				stmtOrder.setDate(2, DateUtil.getDateSQL(request.getOrderDate()));
				stmtOrder.setDate(3, DateUtil.getDateSQL(request.getCycleDate()));
				stmtOrder.setLong(4, subscriberId);
				stmtOrder.setString(5, request.getIsdn());
				stmtOrder.setInt(6, request.getSubscriberType());
				stmtOrder.setLong(7, request.getProductId());
				stmtOrder.setDouble(8, request.getAmount());
				stmtOrder.setDouble(9, score);
				stmtOrder.setInt(10, request.getStatus());
				stmtOrder.execute();
			}

			// campaign schedule
			if (isNew)
			{
				BinaryIndex campaigns = CampaignFactory.getCache().getCampaigns();

				for (int j = 0; j < campaigns.size(); j++)
				{
					CampaignEntry campaign = (CampaignEntry) campaigns.get(j);

					if (campaign.isScheduleEnable())
					{
						stmtInsertCampaign.setLong(1, subscriberId);
						stmtInsertCampaign.setString(2, request.getIsdn());
						stmtInsertCampaign.setInt(3, request.getSubscriberType());
						stmtInsertCampaign.setLong(4, campaign.getCampaignId());
						stmtInsertCampaign.execute();
					}
				}
			}

			if (stmtRemove != null)
			{
				stmtRemove.setLong(1, request.getRequestId());
				stmtRemove.execute();
			}

			if ((mcnMain != null) && !mcnMain.isClosed())
			{
				mcnMain.commit();
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
}
