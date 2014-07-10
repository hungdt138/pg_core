package com.crm.provisioning.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;

public class LoyaltyCommandImpl extends CommandImpl{

	@Override
	public CommandMessage register(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		// TODO Auto-generated method stub
		CommandMessage result = request;
		try
		{
			Connection connection = Database.getConnection();
			PreparedStatement stmtBalance = null;
			
			String SQL = "UPDATE SubscriberBalance SET status = ? WHERE isdn = ? and Status = ?" ;
			
			stmtBalance = connection.prepareStatement(SQL);
			stmtBalance.setInt(1, 0);
			stmtBalance.setString(2, request.getIsdn());
			stmtBalance.setInt(3, 1);
			
			stmtBalance.executeUpdate();
			
			super.register(instance, provisioningCommand, request);
		}
		catch(Throwable e)
		{
			result.setCause("error");
			return result;
		}
		
		return result;
	}
	
	

}
