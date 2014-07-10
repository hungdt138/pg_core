package com.crm.pgw.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.crm.kernel.sql.Database;

public class PaymentServiceImpl {
	
	public static int getActiveDuration(double amount) throws Exception{
		int duration = 0;
		PreparedStatement stmtDuration = null;
		ResultSet rsDuration = null;
		Connection connection = null;
		try {
			String sql = "select duration from activeduration where ? between min_value and max_value";
			connection = Database.getConnection();
			stmtDuration = connection.prepareStatement(sql);
			stmtDuration.setDouble(1, amount);
			rsDuration = stmtDuration.executeQuery();
			if(rsDuration.next()){
				duration = rsDuration.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(rsDuration);
			Database.closeObject(stmtDuration);
			Database.closeObject(connection);
		}
		return duration;
	}
}
