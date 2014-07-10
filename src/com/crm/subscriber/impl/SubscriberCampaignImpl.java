/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.crm.subscriber.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.util.DateUtil;

/**
 * The implementation of the subscriber entry local service.
 * 
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link com.sdp.subscriber.service.SubscriberEntryLocalService} interface.
 * </p>
 * 
 * <p>
 * Never reference this interface directly. Always use
 * {@link com.sdp.subscriber.service.SubscriberEntryLocalServiceUtil} to access
 * the subscriber entry local service.
 * </p>
 * 
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 * 
 * @author Phan Viet Thang
 * @see com.sdp.subscriber.service.base.SubscriberEntryLocalServiceBaseImpl
 * @see com.sdp.subscriber.service.SubscriberEntryLocalServiceUtil
 */
public class SubscriberCampaignImpl
{
	public static String getCampaignCode(Connection connection, String isdn) throws Exception
	{
		return "PROMOTION";
	}

	public static String getCampaignCode(String isdn) throws Exception
	{
		Connection connection = null;

		try
		{
			return getCampaignCode(connection, isdn);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(connection);
		}
	}

	public static void createCampaignEvent(
			Connection connection, long userId, String userName, long orderId, Date orderDate
			, long subscriberId, long subProductId, String isdn, int subscriberType, long productId
			, String serviceAddress, String keyword, long campaignId, long segmentId
			, Date nextRunDate, long reasonId, String cause, int status)
			throws Exception
	{
		PreparedStatement stmtOrder = null;

		try
		{
			long subCampaignId= Database.getSequence(connection, "sub_campaign_seq");

			String SQL = "Insert into SubscriberCampaign "
					+ "		(subCampaignId, userId, userName, createDate, modifiedDate, orderId, orderDate "
					+ "		, subscriberId, subProductId, productId, isdn, subscriberType "
					+ "		, serviceAddress, keyword, campaignId, segmentId, nextRunDate, reasonId, cause, status) "
					+ " Values "
					+ "		(?, ?, ?, sysDate, sysDate, ?, ? "
					+ "		, ?, ?, ?, ?, ? "
					+ "		, ?, ?, ?, ?, ?, ?, ?, ?) ";

			stmtOrder = connection.prepareStatement(SQL);

			stmtOrder.setLong(1, subCampaignId);
			stmtOrder.setLong(2, userId);
			stmtOrder.setString(3, userName);

			stmtOrder.setLong(4, orderId);
			stmtOrder.setTime(5, DateUtil.getTimeSQL(orderDate));

			stmtOrder.setLong(6, subscriberId);
			stmtOrder.setLong(7, subProductId);
			stmtOrder.setLong(8, productId);
			stmtOrder.setString(9, isdn);
			stmtOrder.setInt(10, subscriberType);

			stmtOrder.setString(11, serviceAddress);
			stmtOrder.setString(12, keyword);
			stmtOrder.setLong(13, campaignId);
			stmtOrder.setLong(14, segmentId);
			stmtOrder.setDate(15, DateUtil.getDateSQL(nextRunDate));
			
			stmtOrder.setLong(16, reasonId);
			stmtOrder.setString(17, cause);
			
			if (status == Constants.ORDER_STATUS_ANY)
			{
				status = Constants.ORDER_STATUS_PENDING;
			}
			
			stmtOrder.setInt(18, status);

			stmtOrder.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtOrder);
		}
	}
	public static void createCampaignEvent(
			long userId, String userName, long orderId, Date orderDate
			, long subscriberId, long subProductId, String isdn, int subscriberType, long productId
			, String serviceAddress, String keyword, long campaignId, long segmentId
			, Date nextRunDate, long reasonId, String cause, int status)
			throws Exception
	{
		Connection connection = null;
		
		try
		{
			connection = Database.getConnection();
			
			createCampaignEvent(
					connection, userId, userName, orderId, orderDate, subscriberId, subProductId
					, isdn, subscriberType, productId, serviceAddress, keyword, campaignId, segmentId
					, nextRunDate, reasonId, cause, status);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(connection);
		}
	}

}