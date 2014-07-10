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
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.subscriber.bean.SubscriberGroup;
import com.crm.util.DateUtil;
import com.fss.util.AppException;

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
public class SubscriberGroupImpl
{
	public final static String	CONDITION_ACTIVE		= " (status = " + Constants.SUPPLIER_ACTIVE_STATUS + ") ";

	public final static String	CONDITION_BARRING		= " (status = " + Constants.SUPPLIER_BARRING_STATUS + ") ";

	public final static String	CONDITION_TERMINATED	= " (status = " + Constants.SUPPLIER_CANCEL_STATUS + ") ";

	public final static String	CONDITION_UNTERMINATED	= " (status != " + Constants.SUPPLIER_CANCEL_STATUS + ") ";

	public static int countMember(Connection connection, String isdn, long productId, String groupType, boolean includeSuspended)
			throws Exception
	{
		PreparedStatement stmtMember = null;

		ResultSet rsMember = null;

		int count = 0;

		try
		{
			String SQL = "";

			if (includeSuspended)
			{
				SQL = "Select count(*) total From SubscriberGroup Where isdn = ? and productId = ? and groupType = ? "
					+ " and unregisterdate is null and " + CONDITION_UNTERMINATED;
			}
			else
			{
				SQL = "Select count(*) total From SubscriberGroup Where isdn = ? and productId = ? and groupType = ? "
						+ " and unregisterdate is null and " + CONDITION_ACTIVE;
			}

			stmtMember = connection.prepareStatement(SQL);

			stmtMember.setString(1, isdn);
			stmtMember.setLong(2, productId);
			stmtMember.setString(3, groupType);

			rsMember = stmtMember.executeQuery();

			if (rsMember.next())
			{
				count = rsMember.getInt("total");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsMember);
			Database.closeObject(stmtMember);
		}

		return count;
	}

	public static int countMember(String isdn, long productId, String groupType, boolean includeSuspended) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return countMember(connection, isdn, productId, groupType, includeSuspended);
		}
		finally
		{
			Database.closeObject(connection);
		}
	}

	public static SubscriberGroup getGroup(ResultSet rsGroup) throws Exception
	{
		SubscriberGroup result = new SubscriberGroup();

		try
		{
			result.setUserId(rsGroup.getLong("userId"));
			result.setUserName(Database.getString(rsGroup, "userName"));

			result.setSubGroupId(rsGroup.getLong("subGroupId"));
			result.setGroupType(rsGroup.getString("groupType"));

			result.setProductId(rsGroup.getLong("productId"));

			result.setIsdn(Database.getString(rsGroup, "isdn"));
			result.setReferalIsdn(Database.getString(rsGroup, "referalIsdn"));
			result.setVerifyCode(Database.getString(rsGroup, "verifyCode"));

			result.setRegisterDate(rsGroup.getTimestamp("registerDate"));

			result.setStatus(rsGroup.getInt("status"));
		}
		catch (Exception e)
		{
			throw e;
		}

		return result;
	}

	public static void addMember(
			Connection connection, long userId, String userName
			, String isdn, String referalIsdn, long productId, String groupType, String verifyCode, Date registerDate, int status)
			throws Exception
	{
		PreparedStatement stmtGroup = null;

		try
		{
			long subGroupId = Database.getSequence(connection, "sub_group_seq");

			String SQL = "Insert into SubscriberGroup "
					+ "		(subGroupId, userId, userName, createDate, modifiedDate "
					+ "		, isdn, referalIsdn, productId, groupType, verifyCode, registerDate, status) "
					+ " Values "
					+ "		(?, ?, ?, sysDate, sysDate "
					+ "		, ?, ?, ?, ?, ?, ?, ?) ";

			stmtGroup = connection.prepareStatement(SQL);

			stmtGroup.setLong(1, subGroupId);
			stmtGroup.setLong(2, userId);
			stmtGroup.setString(3, userName);

			stmtGroup.setString(4, isdn);
			stmtGroup.setString(5, referalIsdn);
			stmtGroup.setLong(6, productId);
			stmtGroup.setString(7, groupType);
			stmtGroup.setString(8, verifyCode);

			stmtGroup.setTime(9, DateUtil.getTimeSQL(registerDate));

			if (status == Constants.ORDER_STATUS_ANY)
			{
				status = Constants.ORDER_STATUS_PENDING;
			}

			stmtGroup.setInt(10, status);

			stmtGroup.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtGroup);
		}
	}

	public static boolean isOwner(Connection connection, String isdn, long productId, String groupType) throws Exception
	{
		PreparedStatement stmtGroup = null;

		ResultSet rsGroup = null;

		try
		{
			String SQL = "Select * From SubscriberGroup Where isdn = ? and productId = ? and groupType = ? "
					+ " and unregisterdate is null and " + CONDITION_UNTERMINATED;

			stmtGroup = connection.prepareStatement(SQL);

			stmtGroup.setString(1, isdn);
			stmtGroup.setLong(2, productId);
			stmtGroup.setString(3, groupType);

			rsGroup = stmtGroup.executeQuery();

			if (rsGroup.next())
			{
				return true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsGroup);
			Database.closeObject(stmtGroup);
		}

		return false;
	}

	public static boolean isOwner(String isdn, long productId, String groupType) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return isOwner(connection, isdn, productId, groupType);
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

	public static void addMember(
			long userId, String userName, String groupType
			, String isdn, long productId, String referalIsdn, String verifyCode, Date registerDate, int status)
			throws Exception
	{
		Connection connection = null;

		try
		{
			addMember(connection, userId, userName, groupType, isdn, productId, referalIsdn, verifyCode, registerDate, status);
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

	public static void removeMember(Connection connection, String isdn, String referalIsdn, long productId, String groupType,
			int removeStatus)
			throws Exception
	{
		PreparedStatement stmtGroup = null;

		try
		{
			String SQL = "Update SubscriberGroup "
					+ " Set status = ? "
					+ " Where isdn = ? and productId = ? and referalIsdn = ? and groupType = ? and unregisterdate is null ";

			stmtGroup = connection.prepareStatement(SQL);

			stmtGroup.setInt(1, removeStatus);
			stmtGroup.setString(2, isdn);
			stmtGroup.setLong(3, productId);
			stmtGroup.setString(4, referalIsdn);
			stmtGroup.setString(5, groupType);

			stmtGroup.execute();

			if (stmtGroup.getUpdateCount() == 0)
			{
				throw new AppException(Constants.ERROR_MEMBER_NOT_FOUND);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtGroup);
		}
	}

	/**
	 * 
	 * @param isdn
	 * @param referalIsdn
	 * @param productId
	 * @param groupType
	 * @param removeStatus
	 *            status to set after remove
	 * @throws Exception
	 */
	public static void removeMember(String isdn, String referalIsdn, long productId, String groupType, int removeStatus)
			throws Exception
	{
		Connection connection = null;

		try
		{
			removeMember(connection, isdn, referalIsdn, productId, groupType, removeStatus);
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

	public static void removeGroup(Connection connection, String isdn, long productId, String groupType)
			throws Exception
	{
		PreparedStatement stmtGroup = null;

		try
		{
			String SQL = "Update SubscriberGroup "
					+ " Set status = ?, unregisterdate = ? "
					+ " Where isdn = ? and productId = ? and groupType = ? and unregisterdate is null ";

			stmtGroup = connection.prepareStatement(SQL);

			stmtGroup.setInt(1, Constants.SUPPLIER_CANCEL_STATUS);
			stmtGroup.setTime(2, DateUtil.getTimeSQL(new Date()));
			stmtGroup.setString(3, isdn);
			stmtGroup.setLong(4, productId);
			stmtGroup.setString(5, groupType);

			stmtGroup.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtGroup);
		}
	}

	public static void removeGroup(String isdn, long productId, String groupType) throws Exception
	{
		Connection connection = null;

		try
		{
			removeGroup(connection, isdn, productId, groupType);
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