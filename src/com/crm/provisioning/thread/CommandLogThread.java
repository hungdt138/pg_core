package com.crm.provisioning.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.crm.thread.DispatcherThread;
import com.crm.thread.util.ThreadUtil;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;
import com.fss.util.AppException;

public class CommandLogThread extends DispatcherThread
{
	public String	byPassCommandIds		= "";
	public int		updateDatabaseInterval	= 1;
	public int		maxBatchSize			= 1;
	public boolean	useShortLog				= false;

	@Override
	@SuppressWarnings(value = { "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();
		vtReturn.add(ThreadUtil.createTextParameter("byPassCommandIds", 500,
				"Thread does not write log of commands had commandId in this list."));
		vtReturn.add(ThreadUtil.createIntegerParameter("updateDatabaseInterval",
				"Update database interval in second."));
		vtReturn.add(ThreadUtil.createIntegerParameter("maxStatementBatchSize",
				"Maxium size of SQL statement batch."));
		vtReturn.add(ThreadUtil.createBooleanParameter("useShortLog",
		"Use short or long log(long log includes request and response value), default value is false."));
		vtReturn.addAll(super.getDispatcherDefinition());

		return vtReturn;
	}

	@Override
	public void fillParameter() throws AppException
	{
		try
		{
			super.fillParameter();
			byPassCommandIds = ThreadUtil.getString(this, "byPassCommandIds", false, "");
			updateDatabaseInterval = ThreadUtil.getInt(this, "updateDatabaseInterval", 1);
			maxBatchSize = ThreadUtil.getInt(this, "maxStatementBatchSize", 1);
			useShortLog = ThreadUtil.getBoolean(this, "useShortLog", false);
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
