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

import com.crm.kernel.sql.Database;

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
public class SubscriberBalanceImpl
{
	public static boolean withdraw(
			long userId, String userName, long subscriberId, String isdn, String balanceType, double amount)
			throws Exception
	{
		Connection connection = Database.getConnection();
		connection.setAutoCommit(false);

		PreparedStatement stmtBalance = null;

		try
		{
			String SQL = "Update SubscriberBalance "
					+ "Set userId = ?, userName = ?, modifiedDate = sysDate, balanceAmount = nvl(balanceAmount, 0) - ? "
					+ "Where subscriberId = ? and balanceType = ? and nvl(balanceAmount, 0) >= ? ";

			stmtBalance = connection.prepareStatement(SQL);

			stmtBalance.setLong(1, userId);
			stmtBalance.setString(2, userName);
			stmtBalance.setDouble(3, amount);
			stmtBalance.setString(4, isdn);
			stmtBalance.setString(5, balanceType);
			stmtBalance.setDouble(6, amount);

			stmtBalance.execute();

			if (stmtBalance.getUpdateCount() == 0)
			{
				throw new AppException("not-enough-money");
			}

			connection.commit();
		}
		catch (Exception e)
		{
			Database.rollback(connection);

			throw e;
		}
		finally
		{
			Database.closeObject(stmtBalance);
			Database.closeObject(connection);
		}

		return true;
	}

	public static boolean adjustment(
			long userId, String userName, long subscriberId, String isdn, String balanceType, double amount)
			throws Exception
	{
		return withdraw(userId, userName, subscriberId, isdn, balanceType, - amount);
	}
}