package com.crm.subscriber.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.crm.kernel.sql.Database;

public class FunStoryServiceImpl
{
	public static String getFunStory(Connection connection) throws Exception
	{
		String funStory = "";
		
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;
		try
		{
			String sql = "select * from funstory where trunc(createdate) = trunc(sysdate)";
			stmtConfig = connection.prepareStatement(sql);
			
			rsConfig = stmtConfig.executeQuery();
			
			if (rsConfig.next())
			{
				funStory = rsConfig.getString("Content");
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
		
		return funStory;
	}
	
	public static String getFunStory() throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return getFunStory(connection);
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
