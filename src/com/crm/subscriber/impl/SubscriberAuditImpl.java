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
 * {@link com.sdp.subscriber.service.SubscriberAuditLocalService} interface.
 * </p>
 * 
 * <p>
 * Never reference this interface directly. Always use
 * {@link com.sdp.subscriber.service.SubscriberAuditLocalServiceUtil} to
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
 * @see com.sdp.subscriber.service.base.SubscriberAuditLocalServiceBaseImpl
 * @see com.sdp.subscriber.service.SubscriberAuditLocalServiceUtil
 */
public class SubscriberAuditImpl
{
	public static void addAudit(
			Connection connection, long userId, String userName, Date auditDate, String auditType
			, String actionType, long classPK, String isdn, long productId, String description)
			throws Exception
	{
		PreparedStatement stmtAudit = null;

		try
		{
			String SQL = "Insert into SubscriberAudit "
					+ " (auditId, userId, userName, auditType, auditDate "
					+ " , actionType, classPK, isdn, productId, description)"
					+ " Values "
					+ " (audit_seq.nextval, ?, ?, ?, ? "
					+ " , ?, ?, ?, ?, ?)";

			stmtAudit = connection.prepareStatement(SQL);

			stmtAudit.setLong(1, userId);
			stmtAudit.setString(2, userName);
			stmtAudit.setString(3, auditType);
			stmtAudit.setTime(4, DateUtil.getTimeSQL(auditDate));
			
			stmtAudit.setString(5, actionType);
			stmtAudit.setLong(5, classPK);
			stmtAudit.setString(7, isdn);
			stmtAudit.setString(8, description);

			stmtAudit.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtAudit);
		}
	}

	public static void addSubscriberAudit(
			Connection connection, long userId, String userName
			, String actionType, long subscriberId, String isdn, long productId, String description)
			throws Exception
	{
		addAudit(connection, userId, userName, new Date(), Constants.AUDIT_SUBSCRIBER
				, actionType, subscriberId, isdn, productId, description);
	}

	public static void addProductAudit(
			Connection connection, long userId, String userName
			, String actionType, long classPK, String isdn, long productId, String description)
			throws Exception
	{
		addAudit(connection, userId, userName, new Date(), Constants.AUDIT_SUB_PRODUCT
				, actionType, classPK, isdn, productId, description);
	}
}