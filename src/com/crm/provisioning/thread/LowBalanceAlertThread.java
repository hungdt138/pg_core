package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.message.CommandMessage;
import com.crm.util.StringUtil;
import com.fss.thread.ParameterType;
import com.fss.util.AppException;

public class LowBalanceAlertThread extends ProvisioningThread
{
	protected Vector vtAlert = new Vector();
	
	protected Connection connection = null;
	protected PreparedStatement _stmtQueue = null;
	protected ResultSet rsQueue = null;

	protected String _sqlCommand = "";
	protected int _restTime = 5;
	
	public ConcurrentHashMap<String, Boolean>	indexes				= new ConcurrentHashMap<String, Boolean>();

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		Vector vtValue = new Vector();
		vtValue.addElement(createParameterDefinition("ProductId", "",
				ParameterType.PARAM_TEXTBOX_MAX, "",
				"Context to mapping class", "0"));

		vtValue.addElement(createParameterDefinition("Limitation", "",
				ParameterType.PARAM_TEXTBOX_MAX, "",
				"Context to mapping class", "1"));

		vtValue.addElement(createParameterDefinition("Balance", "",
				ParameterType.PARAM_TEXTBOX_MAX, "",
				"Context to mapping class", "2"));

		vtValue.addElement(createParameterDefinition("ServiceAddress", "",
				ParameterType.PARAM_TEXTBOX_MAX, "",
				"Context to mapping class", "3"));

		vtValue.addElement(createParameterDefinition("TimePerData", "",
				ParameterType.PARAM_TEXTBOX_MAX, "",
				"Context to mapping class", "4"));

		vtValue.addElement(createParameterDefinition("SMSContent", "",
				ParameterType.PARAM_TEXTBOX_MAX, "",
				"Context to mapping class", "5"));

		vtReturn.addElement(createParameterDefinition("AlertConfig", "",
				ParameterType.PARAM_TABLE, vtValue, "Alert Config"));
		
		vtReturn.addElement(createParameterDefinition("SQLCommand", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("RestTime", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		
		vtReturn.addAll(super.getParameterDefinition());

		return vtReturn;
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillParameter() throws AppException
	{
		try
		{
			super.fillParameter();
			vtAlert = new Vector();
			Object obj = getParameter("AlertConfig");
			if (obj != null && (obj instanceof Vector))
			{
				vtAlert = (Vector) ((Vector) obj).clone();
			}
			
			setSQLCommand(loadMandatory("SQLCommand"));
			setRestTime(loadInteger("RestTime"));
		}
		catch (AppException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			connection = Database.getConnection();
			
			String strSQL = getSQLCommand();
			_stmtQueue = connection.prepareStatement(strSQL);
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
			QueueFactory.getLBAQueue().clear();
			indexes.clear();
			
			Database.closeObject(_stmtQueue);
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
	
	public CommandMessage pushMessage() throws Exception
	{
		CommandMessage request = new CommandMessage();

		request.setRequestTime(new Date());
		request.setUserName("system");
		request.setChannel("SMS");
		request.setSubProductId(rsQueue.getLong("subproductid"));
		request.setProductId(rsQueue.getLong("productid"));
		request.setServiceAddress("LBA");
		request.setIsdn(rsQueue.getString("isdn"));
		request.setKeyword("LowBalanceAlert");
		request.getParameters().setProperty("SubscriberStatus", StringUtil.valueOf(rsQueue.getInt("status")));		
		request.getParameters().setProperty("SubProductId", rsQueue.getString("subproductid"));

		return request;
	}
	
	public void doProcessSession() throws Exception
	{
		try
		{
			rsQueue = _stmtQueue.executeQuery();
	
			while (isAvailable() && rsQueue.next())
			{
				CommandMessage request = pushMessage();

				String requestId = rsQueue.getString("subproductid");

				if (indexes.get(requestId) == null)
				{
					QueueFactory.attachLBAQueue(request);

					indexes.put(requestId, Boolean.TRUE);
				}

				Thread.sleep(getRestTime());
			}
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
			logMonitor(e);
		}
	}

	public String getSMSContent(long productid)
	{
		String content = "";
		for (int i = 0; i < vtAlert.size(); i++)
		{
			Vector vt = (Vector) vtAlert.elementAt(i);

			if (Long.parseLong(vt.elementAt(0).toString()) == productid)
			{
				content = vt.elementAt(5).toString();
				break;
			}
		}
		return content;
	}

	public int getDataLimitation(long productid)
	{
		int dataLimitation = 0;
		for (int i = 0; i < vtAlert.size(); i++)
		{
			try
			{
				Vector vt = (Vector) vtAlert.elementAt(i);

				if (Long.parseLong(vt.elementAt(0).toString()) == productid)
				{
					dataLimitation = Integer.parseInt(vt.elementAt(1)
							.toString());
					break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return dataLimitation;
	}

	public int getTimePerData(long productid)
	{
		int TimePerData = 0;
		for (int i = 0; i < vtAlert.size(); i++)
		{
			Vector vt = (Vector) vtAlert.elementAt(i);

			if (Long.parseLong(vt.elementAt(0).toString()) == productid)
			{
				TimePerData = Integer.parseInt(vt.elementAt(4).toString());
				break;
			}
		}
		return TimePerData;
	}

	public String getBalanceName(long productid)
	{
		String balanceName = "";
		for (int i = 0; i < vtAlert.size(); i++)
		{
			Vector vt = (Vector) vtAlert.elementAt(i);

			if (Long.parseLong(vt.elementAt(0).toString()) == productid)
			{
				balanceName = vt.elementAt(2).toString();
				break;
			}
		}
		return balanceName;
	}

	public String getServiceAddress(long productid)
	{
		String serviceAddress = "";
		for (int i = 0; i < vtAlert.size(); i++)
		{
			Vector vt = (Vector) vtAlert.elementAt(i);

			if (Long.parseLong(vt.elementAt(0).toString()) == productid)
			{
				serviceAddress = vt.elementAt(3).toString();
				break;
			}
		}
		return serviceAddress;
	}
	
	public void setSQLCommand(String _sqlCommand)
	{
		this._sqlCommand = _sqlCommand;
	}

	public String getSQLCommand()
	{
		return _sqlCommand;
	}
	
	public int getRestTime() {
		return _restTime;
	}

	public void setRestTime(int _restTime) {
		this._restTime = _restTime;
	}
}
