package com.crm.subscriber.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.crm.kernel.sql.Database;

public class MGMServiceImpl
{
	public static Date getLastTopup(String isdn, int minRecharge)
			throws Exception
	{
		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		Date topupDate = null;

		String strSQL = "SELECT MAX(RECHARGE_DATE) as LAST_TOPUP FROM ASCS.RECHARGE_TRIGGER WHERE MDN = ? and FACE_VALUE >= ?";

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setInt(2, minRecharge);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				topupDate = rsProduct.getTimestamp("LAST_TOPUP");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsProduct);
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}

		return topupDate;
	}

	public static boolean checkIntroducer(String isdn, String lastTopup,
			int maxIntroduce) throws Exception
	{
		boolean success = false;

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		String strSQL = "SELECT COUNT(*) AS TOTAL FROM MGM_TABLE WHERE INTRODUCER = ? and startdate >= TO_DATE( ? , 'SYYYY-MM-DD HH24:MI:SS')";

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setString(2, lastTopup);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				int total = rsProduct.getInt("TOTAL");
				if (total < maxIntroduce)
					success = true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsProduct);
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}

		return success;
	}

	public static boolean checkSubInWhiteList(String isdn) throws Exception
	{
		boolean found = false;

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		String strSQL = "SELECT * FROM MGM_WHITE_LIST WHERE Referal = ?";
		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				found = true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsProduct);
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
		return found;
	}

	public static boolean isNewReferral(String strReferral) throws Exception
	{
		boolean success = false;

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		String strSQL = "SELECT count(ID) as total from MGM_TABLE where Referal= ?";

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, strReferral);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				int intTotal = rsProduct.getInt("total");
				if (intTotal == 0)
					success = true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsProduct);
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
		return success;
	}

	public static boolean isIntroducer(String isdn, String referral)
			throws Exception
	{
		boolean success = false;

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		String strSQL = "SELECT ID FROM MGM_TABLE WHERE INTRODUCER = ? and Referal = ?";

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, referral);
			stmtProduct.setString(2, isdn);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				success = true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsProduct);
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
		return success;
	}

	public static int getNumOfCC(String isdn) throws Exception
	{
		int number = 0;

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		String strSQL = "SELECT COUNT(INTRODUCER) as total from MGM_TABLE where INTRODUCER = ?";
		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				number = rsProduct.getInt("total");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsProduct);
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}

		return number;
	}

	public static void removeMGM(String isdn, String referral) throws Exception
	{
		Connection connection = null;
		PreparedStatement stmtProduct = null;

		String strSQL = "DELETE MGM_TABLE where INTRODUCER = ? and Referral = ?";

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setString(1, referral);
			stmtProduct.setQueryTimeout(10);

			stmtProduct.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
	}

	public static boolean insertNewMGM(String strIntroducer,
			String strReferral, String CircleName, Calendar startdate,
			Calendar enddate, int alcoTime, int addDaily) throws Exception
	{
		boolean success = false;

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String strSQL = "insert into MGM_TABLE (ID, INTRODUCER, REFERAL, CIRCLE_NAME, STARTDATE, ENDDATE, UNREGISTEDDATE, DESCRIPTION, ADDDAILY) "
				+ "values (MGM_TABLE_ID.NEXTVAL, ?, ?, ?, sysdate,sysdate + "
				+ alcoTime + ",null, ?, ?)";

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, strIntroducer);
			stmtProduct.setString(2, strReferral);
			stmtProduct.setString(3, CircleName);
			stmtProduct.setString(4, sdf.format(new Date()));
			stmtProduct.setInt(5, addDaily);
			stmtProduct.setQueryTimeout(10);

			stmtProduct.execute();
			success = true;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}

		return success;
	}

	public static boolean updateAddBalance(String strIntroducer, String lastRun)
			throws Exception
	{
		boolean success = false;
		Connection connection = null;
		PreparedStatement stmtProduct = null;

		String strSQL = "update mgm_table set description = ? where introducer = ? and trunc(enddate) >= trunc(sysdate)";

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, lastRun);
			stmtProduct.setString(2, strIntroducer);
			stmtProduct.setQueryTimeout(10);

			stmtProduct.execute();
			success = true;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
		
		return success;
	}

	public static boolean updateConvertBalance(String strIntroducer,
			String lastRun) throws Exception
	{
		boolean success = false;

		Connection connection = null;
		PreparedStatement stmtProduct = null;

		String strSQL = "update mgm_table set convertdate = ? where introducer = ? and trunc(enddate) >= trunc(sysdate)";

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, lastRun);
			stmtProduct.setString(2, strIntroducer);
			stmtProduct.setQueryTimeout(10);

			stmtProduct.execute();
			success = true;
		}
		catch (Exception ex)
		{
			throw ex;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
		return success;
	}

	public static boolean checkSubAddOneTime(String isdn)
			throws Exception
	{
		boolean found = false;

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		String strSQL = "SELECT * FROM MGM_ADD_ONE_TIME WHERE introducer = ?";

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				found = true;
			}
		}
		catch (Exception ex)
		{
			throw ex;
		}
		finally
		{
			Database.closeObject(rsProduct);
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
		return found;
	}
}
