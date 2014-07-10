package com.crm.subscriber.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.util.DateUtil;

public class SubscriberRenewDailyImpl 
{
	public static int insertRenewChargeReq(String isdn, long productId, Date orderDate) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return insertRenewChargeReq(connection, isdn, productId, orderDate);
		}
		finally
		{
			Database.closeObject(connection);
		}
	}
	public static int insertRenewChargeReq(Connection connection, String isdn, 
											long productId, Date orderDate) throws SQLException
	{
		int result = Constants.SERVICE_STATUS_APPROVED;
		String insertSQL = "INSERT INTO DAILYRENEWSERVICES VALUES (?,?,?,?)";
		
		PreparedStatement stmt = connection.prepareStatement(insertSQL);
		
		stmt.setString(0, isdn);
		stmt.setLong(1, productId);
		stmt.setTimestamp(2, DateUtil.getTimestampSQL(orderDate));
		stmt.setInt(3, 0);

		try
		{
			stmt.execute();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			result = Constants.SERVICE_STATUS_DENIED;
		}
		return result;
	}
	public static int disableRenew(String isdn, long productId) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return disableRenew(connection, isdn, productId);
		}
		finally
		{
			Database.closeObject(connection);
		}		
	}
	public static int disableRenew(Connection connection, String isdn, long productId) throws SQLException
	{
		int result = Constants.SERVICE_STATUS_APPROVED;
		String insertSQL = " UPDATE SUBSCRIBEORDER " +
						   " SET DESCRIPTION = 'disableRenew'" + 
						   " WHERE ISDN = ? and PRODUCTID = ? and STATUS = ? and ORDERDATE >= trunc(sysdate)";
		
		PreparedStatement stmt = connection.prepareStatement(insertSQL);
		
		stmt.setString(1, isdn);
		stmt.setLong(2, productId);
		stmt.setInt(3, Constants.SERVICE_STATUS_APPROVED);

		try
		{
			stmt.executeUpdate();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			result = Constants.SERVICE_STATUS_DENIED;
		}
		return result;		
	}	
}
