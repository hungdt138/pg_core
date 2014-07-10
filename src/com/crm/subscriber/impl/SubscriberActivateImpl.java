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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.subscriber.bean.SubscriberActivate;
import com.crm.util.DateUtil;

/**
 * The implementation of the subscriber entry local service.
 * 
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link com.sdp.subscriber.service.SubscriberActivateLocalService} interface.
 * </p>
 * 
 * <p>
 * Never reference this interface directly. Always use
 * {@link com.sdp.subscriber.service.SubscriberActivateLocalServiceUtil} to
 * access the subscriber entry local service.
 * </p>
 * 
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 * 
 * @author Phan Viet Thang
 * @see com.sdp.subscriber.service.base.SubscriberActivateLocalServiceBaseImpl
 * @see com.sdp.subscriber.service.SubscriberActivateLocalServiceUtil
 */
public class SubscriberActivateImpl
{
	public static void addActivate(
			Connection connection, long userId, String userName, long subscriberId, String isdn
			, long productId, Date startDate, int barringStatus, int supplierStatus, String description)
			throws Exception
	{
		PreparedStatement stmtActivate = null;

		try
		{
			String SQL = "Insert into SubscriberActivate "
					+ " 	(activateId, userId, userName, createDate, modifiedDate, subscriberId, isdn "
					+ " 	, productId, barringStatus, supplierStatus, startDate, description)"
					+ " Values "
					+ " 	(activate_seq.nextVal, ?, ?, sysDate, sysDate, ?, ? "
					+ " 	, ?, ?, ?, ?, ?)";

			stmtActivate = connection.prepareStatement(SQL);

			stmtActivate.setLong(1, userId);
			stmtActivate.setString(2, userName);
			stmtActivate.setLong(3, subscriberId);
			stmtActivate.setString(4, isdn);

			stmtActivate.setLong(5, productId);
			stmtActivate.setInt(6, barringStatus);
			stmtActivate.setInt(7, supplierStatus);
			stmtActivate.setTime(8, DateUtil.getTimeSQL(startDate));
			stmtActivate.setString(9, description);

			stmtActivate.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtActivate);
		}
	}

	public static void updateActivate(
			Connection connection, long userId, String userName, long subscriberId, String isdn
			, long productId, Date modifiedDate, int barringStatus, int supplierStatus, String description)
			throws Exception
	{
		PreparedStatement stmtActivate = null;

		try
		{
			SubscriberActivate activate = null;

			activate = getActivate(connection, isdn, subscriberId, productId);

			if (activate != null)
			{
				String SQL = "Update SubscriberActivate Set endDate = ?, description = ? Where activateId = ? ";

				stmtActivate = connection.prepareStatement(SQL);

				stmtActivate.setTime(1, DateUtil.getTimeSQL(modifiedDate));
				stmtActivate.setString(2, description);

				stmtActivate.setLong(3, activate.getActivateId());

				stmtActivate.execute();
			}

			if ((barringStatus != Constants.USER_CANCEL_STATUS)
					&& (supplierStatus != Constants.SUPPLIER_CANCEL_STATUS))
			{
				addActivate(
						connection, userId, userName, subscriberId, isdn, productId
						, modifiedDate, barringStatus, supplierStatus, "");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtActivate);
		}
	}

	public static void unregister(
			Connection connection, long userId, String userName, long subscriberId, String isdn, long productId
			, Date endDate, String description)
			throws Exception
	{
		updateActivate(
				connection, userId, userName, subscriberId, isdn, productId
				, endDate, Constants.USER_CANCEL_STATUS, Constants.SUPPLIER_CANCEL_STATUS, description);
	}

	public void deleteActivate(Connection connection, long activateId) throws Exception
	{
		PreparedStatement stmtActivate = null;

		try
		{
			String SQL = "Delete SubscriberActivate Where subscriberId = ? ";

			stmtActivate = connection.prepareStatement(SQL);

			stmtActivate.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtActivate);
		}
	}

	public void deleteActivateByIsdn(Connection connection, String isdn, long subscriberId) throws Exception
	{
		PreparedStatement stmtActivate = null;

		try
		{
			String SQL = "Delete SubscriberActivate Where isdn = ? and (subscriberId = ? or ? = 0)";

			stmtActivate = connection.prepareStatement(SQL);

			stmtActivate.setString(1, isdn);
			stmtActivate.setLong(2, subscriberId);
			stmtActivate.setLong(3, subscriberId);

			stmtActivate.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtActivate);
		}
	}

	public static SubscriberActivate getActivate(ResultSet rsActivate) throws Exception
	{
		SubscriberActivate activate = new SubscriberActivate();

		try
		{
			if (rsActivate.next())
			{
				activate.setActivateId(rsActivate.getLong("subscriberId"));
				activate.setUserId(rsActivate.getLong("userId"));
				activate.setUserName(rsActivate.getString("userName"));
				activate.setCreateDate(rsActivate.getTimestamp("createDate"));
				activate.setModifiedDate(rsActivate.getTimestamp("modifiedDate"));

				activate.setBarringStatus(rsActivate.getInt("barringStatus"));
				activate.setSupplierStatus(rsActivate.getInt("supplierStatus"));
				activate.setStartDate(rsActivate.getTimestamp("startDate"));
				activate.setEndDate(rsActivate.getTimestamp("endDate"));

				activate.setDescription(rsActivate.getString("description"));
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		return activate;
	}

	public static SubscriberActivate getActivate(long activateId) throws Exception
	{
		SubscriberActivate subscriber = null;

		Connection connection = null;

		PreparedStatement stmtActivate = null;

		ResultSet rsActivate = null;

		try
		{
			String SQL = "Select * From SubscriberActivate Where activateId = ? ";

			connection = Database.getConnection();

			stmtActivate = connection.prepareStatement(SQL);

			stmtActivate.setLong(1, activateId);

			rsActivate = stmtActivate.executeQuery();

			subscriber = getActivate(rsActivate);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActivate);
			Database.closeObject(stmtActivate);
			Database.closeObject(connection);
		}

		return subscriber;
	}

	public static SubscriberActivate getActivate(Connection connection, String isdn, long subscriberId, long productId)
			throws Exception
	{
		SubscriberActivate activate = null;

		PreparedStatement stmtActivate = null;

		ResultSet rsActivate = null;

		try
		{
			if (subscriberId != Constants.DEFAULT_ID)
			{
				String SQL = "Select * "
						+ "From SubscriberActivate "
						+ "Where (subscriberId = ?) and productId = ? and endDate is null "
						+ "Order by startDate desc";

				stmtActivate = connection.prepareStatement(SQL);

				stmtActivate.setLong(1, subscriberId);
				stmtActivate.setLong(2, productId);
			}
			else
			{
				String SQL = "Select * "
						+ "From SubscriberActivate "
						+ "Where isdn = ? and productId = ? and endDate is null "
						+ "Order by startDate desc";

				stmtActivate = connection.prepareStatement(SQL);

				stmtActivate.setString(1, isdn);
				stmtActivate.setLong(2, productId);
			}

			rsActivate = stmtActivate.executeQuery();

			if (rsActivate.next())
			{
				activate = getActivate(rsActivate);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActivate);
			Database.closeObject(stmtActivate);
		}

		return activate;
	}

	public static List<SubscriberActivate> getAll(Connection connection, long subscriberId, long productId) throws Exception
	{
		ArrayList<SubscriberActivate> result = new ArrayList<SubscriberActivate>();

		PreparedStatement stmtActivate = null;

		ResultSet rsActivate = null;

		try
		{
			String SQL = "Select * "
					+ "From SubscriberActivate "
					+ "Where subscriberId = ? and productId = ? "
					+ "Order by startDate";

			stmtActivate = connection.prepareStatement(SQL);

			stmtActivate.setLong(1, subscriberId);
			stmtActivate.setLong(2, productId);

			rsActivate = stmtActivate.executeQuery();

			if (rsActivate.next())
			{
				result.add(getActivate(rsActivate));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActivate);
			Database.closeObject(stmtActivate);
		}

		return result;
	}
}