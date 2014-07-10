package com.crm.ascs.collector.thread;

import java.net.ServerSocket;
import java.util.Vector;

import com.crm.ascs.collector.CollectorServer;
import com.crm.ascs.collector.Trigger;
import com.crm.ascs.collector.TriggerCollection;
import com.crm.thread.DispatcherThread;
import com.crm.thread.util.ThreadUtil;
import com.fss.util.AppException;

public class CollectorThread extends DispatcherThread
{
	public int					listenPort		= 3000;
	public int					maxConnection	= 10;
	public int					networkTimeout	= 3000;
	public int					triggerTimeout	= 60000;
	
	public CollectorServer		collectorServer	= null;

	public TriggerCollection	workQueue		= new TriggerCollection();

	public void addWork(Trigger trigger)
	{
		synchronized (workQueue)
		{
			workQueue.add(trigger);
		}
	}

	public Trigger getWork()
	{
		synchronized (workQueue)
		{
			if (workQueue.size() == 0)
				return null;
			else
				return (Trigger) workQueue.remove(0);
		}
	}

	@Override
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.add(ThreadUtil.createIntegerParameter("listenPort", "Listen port."));
		vtReturn.add(ThreadUtil.createIntegerParameter("maxConnection", "Max connection that server can handle."));
		vtReturn.add(ThreadUtil.createIntegerParameter("networkTimeout", "Time out to wait for accept connection or read from client."));
		vtReturn.add(ThreadUtil.createIntegerParameter("triggerTimeout", "Time to live of trigger."));
		vtReturn.addAll(super.getParameterDefinition());
		return vtReturn;
	}

	@Override
	public void fillDispatcherParameter() throws AppException
	{
		listenPort = ThreadUtil.getInt(this, "listenPort", 3000);
		maxConnection = ThreadUtil.getInt(this, "maxConnection", 10);
		networkTimeout = ThreadUtil.getInt(this, "networkTimeout", 1000);
		triggerTimeout = ThreadUtil.getInt(this, "triggerTimeout", 60000);

		super.fillDispatcherParameter();
	}

	@Override
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		collectorServer = new CollectorServer(this);

		collectorServer.start();
	}

	@Override
	public void afterProcessSession() throws Exception
	{
		try
		{
			collectorServer.stop();
		}
		catch (Exception e)
		{
			debugMonitor(e);
		}
		finally
		{
			collectorServer = null;
			super.afterProcessSession();
		}
	}
}
