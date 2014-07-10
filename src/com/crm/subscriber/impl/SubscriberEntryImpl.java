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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.crm.kernel.message.Constants;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.CommandUtil;
import com.crm.subscriber.bean.SubscriberEntry;
import com.crm.kernel.sql.Database;
import com.crm.util.AppProperties;
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
public class SubscriberEntryImpl
{
	public static int getSubscriberType(String isdn) throws Exception
	{
		Connection connection = null;

		int subscriberType = Constants.UNKNOW_SUB_TYPE;

		try
		{
			connection = Database.getConnection();

			subscriberType = getSubscriberType(connection, isdn);
		}
		finally
		{
			Database.closeObject(connection);
		}

		return subscriberType;
	}

	public static Date getCycleDate(Date registerDate) throws Exception
	{
		Date cycleDate = null;

		try
		{
			if (registerDate != null)
			{
				Calendar now = Calendar.getInstance();

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(registerDate);
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);

				if (calendar.get(Calendar.DATE) > 28)
				{
					calendar.set(Calendar.DATE, 28);
				}

				calendar.set(Calendar.YEAR, now.get(Calendar.YEAR));
				calendar.set(Calendar.MONTH, now.get(Calendar.MONTH));

				if (calendar.after(now))
				{
					calendar.add(Calendar.MONTH, -1);
				}

				cycleDate = calendar.getTime();
			}
			else
			{
				throw new AppException("unknow-cycle");
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		return cycleDate;
	}

	public static int getSubscriberType(Connection connection, String isdn) throws Exception
	{
		isdn = CommandUtil.addCountryCode(isdn);

		int result = Constants.PREPAID_SUB_TYPE;

		CallableStatement cstmt = null;

		try
		{
			String call = "{call ? := get_subs_type(?) }";

			cstmt = connection.prepareCall(call);
			cstmt.setQueryTimeout(30);
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setString(2, isdn);

			cstmt.execute();

			// Check type of subscriber
			// 1 : pre-paid
			// 2 : post-paid
			// other: customer's number is not VNM's.
			result = cstmt.getInt(1);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(cstmt);
		}

		return result;

	}
	public static void getSubscriberInfo(CommandMessage request) throws Exception
	{
		String isdn = request.getIsdn();

		if (!isdn.equals("") && isdn.startsWith(Constants.DOMESTIC_CODE))
		{
			isdn = isdn.substring(Constants.DOMESTIC_CODE.length());
		}
		if (!isdn.equals("") && isdn.startsWith(Constants.COUNTRY_CODE))
		{
			isdn = isdn.substring(Constants.COUNTRY_CODE.length());
		}
		
		Connection connection = Database.getConnection();
		CallableStatement cstmt = null;
		try
		{
			String call = "{call get_sub_info(?, ?, ?, ?, ?, ?, ?, ?) }";

			cstmt = connection.prepareCall(call);

			cstmt.setQueryTimeout(1800);
			cstmt.registerOutParameter(2, Types.VARCHAR);
			cstmt.registerOutParameter(3, Types.VARCHAR);
			cstmt.registerOutParameter(4, Types.VARCHAR);
			cstmt.registerOutParameter(5, Types.VARCHAR);
			cstmt.registerOutParameter(6, Types.DATE);
			cstmt.registerOutParameter(7, Types.VARCHAR);
			cstmt.registerOutParameter(8, Types.VARCHAR);

			cstmt.setString(1, isdn);

			cstmt.execute();
			AppProperties parameters = new AppProperties();			

			parameters.setString("iccid", String.valueOf(cstmt.getString(2)));
			parameters.setString("imsi", String.valueOf(cstmt.getString(3)));
			parameters.setString("firstName", String.valueOf(cstmt.getString(4)));
			parameters.setString("lastName", String.valueOf(cstmt.getString(5)));
			parameters.setString("idCard", String.valueOf(cstmt.getString(7)));
			parameters.setString("gender", String.valueOf(cstmt.getString(8)));

			java.sql.Date birthDate = cstmt.getDate(6);
			if (birthDate != null)
			{
				parameters.setString("birthdate", String.valueOf(birthDate.getTime()));
			}
			
			request.setParameters(parameters);			
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			Database.closeObject(cstmt);
			Database.closeObject(connection);			
		}
	}
	public static SubscriberEntry addSubscriber(
			Connection connection, long userId, String userName, long merchantId
			, String isdn, int subscriberType, long productId, String contractNo
			, long campaignId, long segmentId, Date termDate, Date registerDate, int barringStatus, int supplierStatus
			, String description)
			throws Exception
	{
		PreparedStatement stmtSubscriber = null;

		SubscriberEntry subscriber = getActiveSubscriber(connection, isdn);

		if (subscriber != null)
		{
			throw new AppException("subscriber-is-existed");
		}

		try
		{
			String SQL = "Insert into SubscriberEntry "
					+ " (subscriberId, userId, userName, createDate, modifiedDate, merchantId, isdn, subscriberType "
					+ " , productId, contractNo, campaignId, segmentId, termDate, registerDate "
					+ " , barringStatus, supplierStatus, description)"
					+ " Values "
					+ " (?, ?, ?, sysDate, sysDate, ?, ?, ? "
					+ " , ?, ?, ?, ?, ?, ? "
					+ " , ?, ?, ?)";

			long subscriberId = Database.getSequence("Subscriber_Seq");

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setLong(1, subscriberId);
			stmtSubscriber.setLong(2, userId);
			stmtSubscriber.setString(3, userName);
			stmtSubscriber.setString(4, isdn);
			stmtSubscriber.setInt(5, subscriberType);

			stmtSubscriber.setLong(6, productId);
			stmtSubscriber.setString(7, contractNo);
			stmtSubscriber.setLong(8, campaignId);
			stmtSubscriber.setLong(9, segmentId);

			stmtSubscriber.setTime(10, DateUtil.getTimeSQL(termDate));
			stmtSubscriber.setTime(11, DateUtil.getTimeSQL(registerDate));

			stmtSubscriber.setInt(12, barringStatus);
			stmtSubscriber.setInt(13, supplierStatus);
			stmtSubscriber.setString(14, description);

			stmtSubscriber.execute();

			SubscriberAuditImpl.addSubscriberAudit(
					connection, userId, userName, Constants.ACTION_REGISTER, subscriberId, isdn, productId, description);

			subscriber = getActiveSubscriber(connection, isdn, subscriberId);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscriber);
		}

		return subscriber;
	}

	public static SubscriberEntry addSubscriber(
			Connection connection, long userId, String userName, String isdn, int subscriberType, long productId)
			throws Exception
	{
		return addSubscriber(
				connection, userId, userName, Constants.DEFAULT_ID, isdn, subscriberType, productId, ""
				, Constants.DEFAULT_ID, Constants.DEFAULT_ID, null, new Date()
				, Constants.USER_ACTIVE_STATUS, Constants.SUPPLIER_ACTIVE_STATUS, "");
	}

	public static void updateSegment(
			Connection connection, long userId, String userName, long subscriberId, long segmentId, String description)
			throws Exception
	{
		PreparedStatement stmtSubscriber = null;

		SubscriberEntry subscriber = null;

		try
		{
			subscriber = getActiveSubscriber(connection, "", subscriberId);

			String SQL = "Update SubscriberEntry Set segmentId = ? Where subscriberId = ? ";

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setLong(1, segmentId);
			stmtSubscriber.setLong(2, subscriberId);

			stmtSubscriber.execute();

			SubscriberAuditImpl.addSubscriberAudit(
					connection, userId, userName, Constants.ACTION_CHANGE_PROFILE
					, subscriberId, subscriber.getIsdn(), subscriber.getProductId(), description);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscriber);
		}
	}

	public static void changeIsdn(
			Connection connection, long userId, String userName, long subscriberId, String isdn, String description)
			throws Exception
	{
		PreparedStatement stmtSubscriber = null;

		SubscriberEntry subscriber = getActiveSubscriber(connection, subscriberId);

		try
		{
			String SQL = "Update SubscriberEntry Set isdn = ? Where subscriberId = ? ";

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setString(1, isdn);
			stmtSubscriber.setLong(2, subscriberId);

			stmtSubscriber.execute();

			subscriber.setIsdn(isdn);

			SubscriberAuditImpl.addSubscriberAudit(
					connection, userId, userName, Constants.ACTION_CHANGE_ISDN
					, subscriberId, subscriber.getIsdn(), subscriber.getProductId(), description);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscriber);
		}
	}

	public static void changeSupplierStatus(
			Connection connection, long userId, String userName, long subscriberId, int supplierStatus, String description)
			throws Exception
	{
		PreparedStatement stmtSubscriber = null;

		SubscriberEntry subscriber = getActiveSubscriber(connection, subscriberId);

		if (subscriber.getSupplierStatus() == supplierStatus)
		{
			return;
		}

		try
		{
			String SQL = "Update SubscriberEntry Set supplierStatus = ? Where subscriberId = ? ";

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setInt(1, supplierStatus);
			stmtSubscriber.setLong(2, subscriber.getSubscriberId());

			stmtSubscriber.execute();

			SubscriberActivateImpl.updateActivate(
					connection, userId, userName, subscriberId, subscriber.getIsdn(), subscriber.getProductId()
					, new Date(), subscriber.getBarringStatus(), supplierStatus, description);

			SubscriberAuditImpl.addSubscriberAudit(
					connection, userId, userName, Constants.ACTION_SUPPLIER_BARRING
					, subscriberId, subscriber.getIsdn(), subscriber.getProductId(), description);

			subscriber.setSupplierStatus(supplierStatus);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscriber);
		}
	}

	public static void updateRank(
			Connection connection, long userId, String userName, long subscriberId
			, long rankId, Date rankStartDate, Date rankExpirationDate, String description)
			throws Exception
	{
		PreparedStatement stmtSubscriber = null;

		SubscriberEntry subscriber = null;

		try
		{
			subscriber = getActiveSubscriber(connection, subscriberId);

			String SQL = "Update SubscriberEntry Set rankId = ?, rankStartDate = ?, rankExpirationDate = ? Where subscriberId = ? ";

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setLong(1, rankId);
			stmtSubscriber.setTimestamp(2, DateUtil.getTimestampSQL(rankStartDate));
			stmtSubscriber.setTimestamp(3, DateUtil.getTimestampSQL(rankExpirationDate));

			stmtSubscriber.setLong(4, subscriberId);

			stmtSubscriber.execute();

			subscriber.setRankId(rankId);
			subscriber.setRankStartDate(rankStartDate);
			subscriber.setRankExpirationDate(rankExpirationDate);

			SubscriberAuditImpl.addSubscriberAudit(
					connection, userId, userName, Constants.ACTION_RANK
					, subscriberId, subscriber.getIsdn(), subscriber.getProductId(), description);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscriber);
		}
	}

	public static void unregister(Connection connection, long userId, String userName, long subscriberId, String description)
			throws Exception
	{
		PreparedStatement stmtSubscriber = null;

		SubscriberEntry subscriber = getActiveSubscriber(connection, subscriberId);

		try
		{
			String SQL = "Update SubscriberEntry Set unregisterDate = ?, supplierStatus = ? Where subscriberId = ? ";

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setTime(1, DateUtil.getTimeSQL());
			stmtSubscriber.setInt(2, Constants.SUPPLIER_CANCEL_STATUS);

			stmtSubscriber.setLong(3, subscriberId);

			stmtSubscriber.execute();

			SubscriberAuditImpl.addSubscriberAudit(
					connection, userId, userName, Constants.ACTION_UNREGISTER
					, subscriberId, subscriber.getIsdn(), subscriber.getProductId(), description);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscriber);
		}
	}

	public SubscriberEntry deleteSubscriber(Connection connection, long subscriberId) throws Exception
	{
		SubscriberEntry subscriber = null;

		PreparedStatement stmtSubscriber = null;

		try
		{
			String SQL = "Delete SubscriberEntry Where subscriberId = ? ";

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setLong(1, subscriberId);

			stmtSubscriber.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscriber);
		}

		return subscriber;
	}

	public static SubscriberEntry getSubscriber(ResultSet rsSubscriber) throws Exception
	{
		SubscriberEntry subscriber = new SubscriberEntry();

		try
		{
			if (rsSubscriber.next())
			{
				subscriber.setSubscriberId(rsSubscriber.getLong("subscriberId"));

				subscriber.setIMEI(rsSubscriber.getString("IMEI"));
				subscriber.setIMSI(rsSubscriber.getString("IMSI"));
				subscriber.setIsdn(rsSubscriber.getString("isdn"));
				subscriber.setSubscriberType(rsSubscriber.getInt("subscriberType"));

				subscriber.setIdType(rsSubscriber.getString("idType"));
				subscriber.setIdNo(rsSubscriber.getString("idNo"));
				subscriber.setIdIssuer(rsSubscriber.getString("idIssuer"));
				subscriber.setIdIssueDate(rsSubscriber.getTimestamp("idIssueDate"));
				subscriber.setIdExpirationDate(rsSubscriber.getTimestamp("idExpirationDate"));
				subscriber.setTaxId(rsSubscriber.getString("taxId"));

				subscriber.setMerchantId(rsSubscriber.getLong("merchantId"));
				subscriber.setProductId(rsSubscriber.getLong("productId"));
				subscriber.setContractNo(rsSubscriber.getString("contractNo"));

				subscriber.setSegmentId(rsSubscriber.getLong("segmentId"));
				subscriber.setRankId(rsSubscriber.getLong("rankId"));
				subscriber.setRankStartDate(rsSubscriber.getTimestamp("rankStartDate"));
				subscriber.setRankExpirationDate(rsSubscriber.getTimestamp("rankExpirationDate"));

				subscriber.setTermDate(rsSubscriber.getTimestamp("termDate"));
				subscriber.setRegisterDate(rsSubscriber.getTimestamp("registerDate"));
				subscriber.setUnregisterDate(rsSubscriber.getTimestamp("unregisterDate"));
				subscriber.setBarringStatus(rsSubscriber.getInt("barringStatus"));
				subscriber.setSupplierStatus(rsSubscriber.getInt("supplierStatus"));
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		return subscriber;
	}

	public static SubscriberEntry getSubscriber(Connection connection, long subscriberId) throws Exception
	{
		SubscriberEntry subscriber = null;

		PreparedStatement stmtSubscriber = null;

		ResultSet rsSubscriber = null;

		try
		{
			String SQL = "Select * From SubscriberEntry Where (subscriberId = ?)";

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setLong(1, subscriberId);

			rsSubscriber = stmtSubscriber.executeQuery();

			subscriber = getSubscriber(rsSubscriber);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsSubscriber);
			Database.closeObject(stmtSubscriber);
		}

		return subscriber;
	}

	public static SubscriberEntry getActiveSubscriber(Connection connection, String isdn, long subscriberId) throws Exception
	{
		SubscriberEntry subscriber = null;

		PreparedStatement stmtSubscriber = null;

		ResultSet rsSubscriber = null;

		try
		{
			if (subscriberId != Constants.DEFAULT_ID)
			{
				subscriber = getSubscriber(connection, subscriberId);
			}
			else
			{
				String SQL = "Select * From SubscriberEntry Where isdn = ? and unregisterDate is null "
						+ "Order by registerDate desc";

				stmtSubscriber = connection.prepareStatement(SQL);

				stmtSubscriber.setString(1, isdn);

				rsSubscriber = stmtSubscriber.executeQuery();

				subscriber = getSubscriber(rsSubscriber);
			}

			if (subscriber.isActive())
			{
				throw new AppException("subscriber-is-canceled");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsSubscriber);
			Database.closeObject(stmtSubscriber);
			Database.closeObject(connection);
		}

		return subscriber;
	}

	public static List<SubscriberEntry> getSubscriber(String isdn) throws Exception
	{
		ArrayList<SubscriberEntry> subscribers = new ArrayList<SubscriberEntry>();

		Connection connection = null;
		PreparedStatement stmtSubscriber = null;
		ResultSet rsSubscriber = null;

		try
		{
			String SQL = "Select * From SubscriberEntry Where isdn = ? ";

			connection = Database.getConnection();

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setString(1, isdn);

			rsSubscriber = stmtSubscriber.executeQuery();

			while (rsSubscriber.next())
			{
				subscribers.add(getSubscriber(rsSubscriber));

				rsSubscriber.next();
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsSubscriber);
			Database.closeObject(stmtSubscriber);
			Database.closeObject(connection);
		}

		return subscribers;
	}

	public static SubscriberEntry getActiveSubscriber(Connection connection, long subscriberId) throws Exception
	{
		return getActiveSubscriber(connection, "", subscriberId);
	}

	public static SubscriberEntry getActiveSubscriber(Connection connection, String isdn) throws Exception
	{
		return getActiveSubscriber(connection, isdn, Constants.DEFAULT_ID);
	}

	public static List<SubscriberEntry> getActive(String isdn) throws Exception
	{
		ArrayList<SubscriberEntry> subscribers = new ArrayList<SubscriberEntry>();

		Connection connection = null;

		PreparedStatement stmtSubscriber = null;

		ResultSet rsSubscriber = null;

		try
		{
			String SQL = "Select * "
					+ "From SubscriberEntry "
					+ "Where isdn = ? and unregisterDate is null "
					+ "Order by registerDate desc";

			connection = Database.getConnection();

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setString(1, isdn);

			rsSubscriber = stmtSubscriber.executeQuery();

			while (rsSubscriber.next())
			{
				subscribers.add(getSubscriber(rsSubscriber));

				rsSubscriber.next();
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsSubscriber);
			Database.closeObject(stmtSubscriber);
			Database.closeObject(connection);
		}

		return subscribers;
	}

	public static SubscriberEntry getActive(String isdn, long productId) throws Exception
	{
		SubscriberEntry subscriber = new SubscriberEntry();

		Connection connection = null;

		PreparedStatement stmtSubscriber = null;

		ResultSet rsSubscriber = null;

		try
		{
			String SQL = "Select * "
					+ "From SubscriberEntry "
					+ "Where isdn = ? and productId = ? and unregisterDate != null ";

			connection = Database.getConnection();

			stmtSubscriber = connection.prepareStatement(SQL);

			stmtSubscriber.setString(1, isdn);
			stmtSubscriber.setLong(2, productId);

			rsSubscriber = stmtSubscriber.executeQuery();

			if (rsSubscriber.next())
			{
				subscriber = getSubscriber(rsSubscriber);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsSubscriber);
			Database.closeObject(stmtSubscriber);
			Database.closeObject(connection);
		}

		return subscriber;
	}
}