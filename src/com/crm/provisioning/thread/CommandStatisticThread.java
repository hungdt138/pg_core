package com.crm.provisioning.thread;

import java.util.Vector;

import com.crm.thread.DispatcherThread;
import com.crm.thread.util.ThreadUtil;
import com.fss.util.AppException;

public class CommandStatisticThread extends DispatcherThread
{
	public int updateDatabaseInterval = 0; 
	public String hostToStatistic = "";
	public int warningCount = 0;

	@Override
	@SuppressWarnings(value = { "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();
		vtReturn.add(ThreadUtil.createIntegerParameter("updateInterval", "Update database interval in seconds."));
		vtReturn.add(ThreadUtil.createTextParameter("host", 100, "Host to log statistic."));
		vtReturn.add(ThreadUtil.createIntegerParameter("warningCount", "Total failure command to send alarm."));
		vtReturn.addAll(super.getDispatcherDefinition());
		
		return vtReturn;
	}
	
	@Override
	public void fillParameter() throws AppException
	{
		try
		{
			super.fillParameter();
			updateDatabaseInterval = ThreadUtil.getInt(this, "updateInterval", 3600);
			hostToStatistic = ThreadUtil.getString(this, "host",false , "");
			warningCount = ThreadUtil.getInt(this, "warningCount", 1000);
		}
		catch (AppException e)
		{
			logMonitor(e);

			throw e;
		}
		catch (Exception e)
		{
			logMonitor(e);

			throw new AppException(e.getMessage());
		}
	}
}
