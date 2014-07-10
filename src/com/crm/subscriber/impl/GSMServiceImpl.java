package com.crm.subscriber.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.merchant.cache.MerchantAgent;
import com.crm.util.DateUtil;

public class GSMServiceImpl
{
	public static void report(Connection connection, long userId, String userName,
			String isdn, Date reportTime, long mcc, long mnc, String type,
			long lac, long cellId, long rnc, long psc, String signal,
			String latitude, String longitude, int voiceStatus, int smsStatus,
			int speedStatus, String description, int status) throws Exception
	{
		PreparedStatement stmtRegister = null;

		try
		{
			StringBuilder sql = new StringBuilder();
			sql.append("Insert into cellularreport ");
			sql.append("(reportId, userId, userName, isdn, createDate, reportDate ");
			sql.append(", mcc, mnc, type_, lac, cellId, rnc, psc, signal ");
			sql.append(", latitude, longitude, voiceStatus, smsStatus, speedStatus ");
			sql.append(", description_, status) ");
			sql.append("Values ");
			sql.append("(cellularsequence.nextval, ?, ?, ?, sysDate, ? ");
			sql.append(", ?, ?, ?, ?, ?, ?, ?, ? ");
			sql.append(", ?, ?, ?, ?, ? ");
			sql.append(", ?, ?)");

			stmtRegister = connection.prepareStatement(sql.toString());

			stmtRegister.setLong(1, userId);
			stmtRegister.setString(2, userName);
			stmtRegister.setString(3, isdn);
			stmtRegister.setTimestamp(4, DateUtil.getTimestampSQL(reportTime));
			stmtRegister.setLong(5, mcc);
			stmtRegister.setLong(6, mnc);
			stmtRegister.setString(7, type);
			stmtRegister.setLong(8, lac);
			stmtRegister.setLong(9, cellId);
			stmtRegister.setLong(10, rnc);
			stmtRegister.setLong(11, psc);
			stmtRegister.setString(12, signal);
			stmtRegister.setString(13, latitude);
			stmtRegister.setString(14, longitude);
			stmtRegister.setInt(15, voiceStatus);
			stmtRegister.setInt(16, smsStatus);
			stmtRegister.setInt(17, speedStatus);
			stmtRegister.setString(18, description);
			stmtRegister.setInt(19, status);

			stmtRegister.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtRegister);
		}
	}
	
	

	/**
	 * Edited by NamTA<br>
	 * Modified Date: 17/05/2012
	 * 
	 * @param userId
	 * @param userName
	 * @param subscriberId
	 * @param isdn
	 * @param subscriberType
	 * @param productId
	 * @param campaignId
	 * @param languageId
	 * @param includeCurrentDay
	 * @return
	 * @throws Exception
	 */
	public static void report(long userId, String userName,
			String isdn, Date reportTime, long mcc, long mnc, String type,
			long lac, long cellId, long rnc, long psc, String signal,
			String latitude, String longitude, int voiceStatus, int smsStatus,
			int speedStatus, String description, int status)
			throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			report(connection, userId, userName, isdn, reportTime,
					mcc, mnc, type, lac, cellId, rnc, psc, signal,
					latitude, longitude, voiceStatus, smsStatus,
					speedStatus, description, status);
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
	
	public static MerchantAgent getAgent(Connection connection, String isdn) throws Exception
	{
		MerchantAgent agent = null;
		
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;
		try
		{
			String sql = "select * from merchantAgent where isdn = ? and status = ? and startdate <= trunc(sysdate) and enddate > trunc(sysdate)";
			stmtConfig = connection.prepareStatement(sql);
			stmtConfig.setString(1, isdn);
			stmtConfig.setInt(2, Constants.USER_ACTIVE_STATUS);
			
			rsConfig = stmtConfig.executeQuery();
			
			if (rsConfig.next())
			{
				agent = new MerchantAgent(rsConfig.getLong("agentId"), Database.getString(rsConfig, "alias"));
				agent.setMerchantId(rsConfig.getLong("merchantId"));
				agent.setAgentId(rsConfig.getLong("agentId"));
				agent.setAlias(rsConfig.getString("code"));
				agent.setName(rsConfig.getString("name"));
				agent.setJobTitle(rsConfig.getString("jobTitle"));
				agent.setIsdn(rsConfig.getString("isdn"));
				agent.setStartDate(rsConfig.getDate("startDate"));
				agent.setEndDate(rsConfig.getDate("endDate"));
				agent.setStatus(rsConfig.getInt("status"));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsConfig);
			Database.closeObject(stmtConfig);
		}
		
		return agent;
	}
	
	public static MerchantAgent getAgent(String isdn)
			throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return getAgent(connection, isdn);
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
