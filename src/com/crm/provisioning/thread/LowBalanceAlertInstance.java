package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.impl.ccws.CCWSConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.CommandUtil;
import com.crm.util.DateUtil;
import com.crm.util.StringUtil;

public class LowBalanceAlertInstance extends ProvisioningInstance
{
	protected Connection connection = null;
	protected PreparedStatement _stmtFlexi = null;
	protected PreparedStatement _stmtScheduleFlexi = null;

	public LowBalanceAlertThread getDispatcher()
	{
		return (LowBalanceAlertThread) super.getDispatcher();
	}
	
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			connection = Database.getConnection();
			
			String strSQLUpdateFlexi = "UPDATE SubscriberProduct SET  scheduletime = ?, status = ? WHERE subproductid = ? ";
			_stmtFlexi = connection.prepareStatement(strSQLUpdateFlexi);
			
			String strSQLUpdateScheduleFlexi = "UPDATE SubscriberProduct SET  scheduletime = ? WHERE subproductid = ? ";
			_stmtScheduleFlexi = connection.prepareStatement(strSQLUpdateScheduleFlexi);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	public void afterProcessSession() throws Exception
	{
		try
		{
			Database.closeObject(_stmtFlexi);
			Database.closeObject(_stmtScheduleFlexi);
			Database.closeObject(connection);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			super.afterProcessSession();
		}
	}
	
	public LowBalanceAlertInstance() throws Exception
	{
		super();
	}

	public int processMessage(CommandMessage request) throws Exception
	{
		CCWSConnection ccwsConnection = (CCWSConnection) getProvisioningConnection();
		
		double dataLimitation = ((LowBalanceAlertThread) dispatcher).getDataLimitation(request.getProductId());
		String balanceName = ((LowBalanceAlertThread) dispatcher).getBalanceName(request.getProductId());
		String serviceAddress = ((LowBalanceAlertThread) dispatcher).getServiceAddress(request.getProductId());
		request.setServiceAddress(serviceAddress);
		
		try
		{
			SubscriberRetrieve subscriberRetrieve = ccwsConnection.getSubscriber(request.getIsdn(), 1);
			BalanceEntity balance = CCWSConnection.getBalance(subscriberRetrieve.getSubscriberData(), balanceName);
			
			double dataAmount = balance.getAvailableBalance();
			if (balanceName.toUpperCase().equals("GPRS"))
			{
				dataAmount = dataAmount * 0.00000095367431640625;
			}
			
			String logCheckBalance = request.getIsdn() + ": Balance: " + balanceName + " Remain amount: " + dataAmount;
			
			int status = Integer.parseInt(request.getParameters().getProperty("SubscriberStatus", "1"));
			if (dataAmount < dataLimitation)
			{
				status = Constants.SUBSCRIBER_ALERT_BALANCE_STATUS;
				// Send Sms alert
				CommandUtil.sendSMS(this, request, request.getServiceAddress(),
						request.getShipTo(),
						createContent(request.getIsdn(), StringUtil.format(dataAmount, "#,##0")
								, "",request.getProductId()));
			}
			
			int timePerData = ((LowBalanceAlertThread) dispatcher).getTimePerData(request.getProductId());
			Calendar scanTime = null;
			boolean byPassUpdate = false;
			if (timePerData > 0)
			{
				int nextTime = (int) (dataAmount/dataLimitation) * timePerData;
				scanTime = Calendar.getInstance();
				scanTime.add(Calendar.SECOND, nextTime);
				if (status == Constants.SUBSCRIBER_ALERT_BALANCE_STATUS)
				{
					updateFlexi(request.getSubProductId(), scanTime, status);
				}
				else
				{
					updateScheduleFlexi(request.getSubProductId(), scanTime);
				}
				byPassUpdate = true;
			}
			
			if (!byPassUpdate && status == Constants.SUBSCRIBER_ALERT_BALANCE_STATUS)
			{
//					SubscriberProductImpl.updateFlexi(request.getSubProductId(),dataAmount, scandTime, status);
				updateFlexi(request.getSubProductId(), scanTime, status);
			}
			
			getDispatcher().indexes.remove(request.getParameters().getProperty("SubProductId"));
			
			logMonitor(logCheckBalance);
		}
		catch (SQLException e)
		{
			logMonitor(e);
			
			try
			{
				Database.closeObject(connection);
			}
			catch (Exception ex)
			{
				logMonitor(ex);
			}
			
			try
			{
				connection = Database.getConnection();
			}
			catch (Exception ex)
			{
				logMonitor(ex);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			this.closeProvisioningConnection(ccwsConnection);
		}
		
		return Constants.BIND_ACTION_SUCCESS;
	}
	
	public void doProcessSession() throws Exception
	{
		try
		{
			while (isAvailable())
			{
				CommandMessage request = QueueFactory.detachLBAQueue();

				if (request != null)
				{
					int action = processMessage(request);
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	private String createContent(String isdn, String amount,
			String expirationDate, long productid)
	{
		String template = ((LowBalanceAlertThread) dispatcher).getSMSContent(productid);

		template = template.replaceAll("<isdn>", isdn);
		template = template.replaceAll("<amount>", String.valueOf(amount));

		return template;
	}
	
	private void updateFlexi(long id, Calendar scanTime, int status) throws Exception
	{
		_stmtFlexi.setTimestamp(1, (scanTime != null? DateUtil.getTimestampSQL(scanTime.getTime()) : null));
		_stmtFlexi.setInt(2, status);
		_stmtFlexi.setLong(3, id);
		
		_stmtFlexi.executeUpdate();
		connection.commit();
	}
	
	private void updateScheduleFlexi(long id, Calendar scanTime) throws Exception
	{
		_stmtScheduleFlexi.setTimestamp(1, (scanTime != null? DateUtil.getTimestampSQL(scanTime.getTime()) : null));
		_stmtScheduleFlexi.setLong(2, id);
		
		_stmtScheduleFlexi.executeUpdate();
		connection.commit();
	}
}
