package com.crm.smscsim.thread;

import java.util.Vector;

import com.crm.smscsim.SMSCProcessorFactory;
import com.crm.smscsim.SMSCServer;
import com.crm.thread.DispatcherThread;
import com.crm.thread.util.ThreadUtil;
import com.fss.util.AppException;

public class SMSCServerThread extends DispatcherThread
{

	public int					listenPort		= 3000;
	public int					maxConnection	= 10;
	public int					networkTimeout	= 3000;
	public long					receiveTimeout	= 10000;
	public String				userFilePath	= "";
	public SMSCServer			server			= null;
	public SMSCProcessorFactory	factory			= null;

	@Override
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.add(ThreadUtil.createIntegerParameter("listenPort", "Listen port."));
		vtReturn.add(ThreadUtil.createIntegerParameter("maxConnection", "Max connection that server can handle."));
		vtReturn.add(ThreadUtil.createIntegerParameter("networkTimeout",
				"Time out to wait for accept connection or read from client."));
		vtReturn.add(ThreadUtil
				.createLongParameter("receiveTimeout",
						"Max time (millisecond) between 2 times when client send request before detect that client disconnected."));
		vtReturn.add(ThreadUtil.createTextParameter("userFilePath", 400,
				"User file path, if file does not exist, use default user: nms/nms."));
		vtReturn.addAll(super.getParameterDefinition());
		return vtReturn;
	}

	@Override
	public void fillDispatcherParameter() throws AppException
	{
		listenPort = ThreadUtil.getInt(this, "listenPort", 3000);
		maxConnection = ThreadUtil.getInt(this, "maxConnection", 10);
		networkTimeout = ThreadUtil.getInt(this, "networkTimeout", 1000);
		receiveTimeout = ThreadUtil.getLong(this, "receiveTimeout", 10000);
		userFilePath = ThreadUtil.getString(this, "userFilePath", false, "");

		super.fillDispatcherParameter();
	}

	@Override
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();
		startServer();
	}

	private void startServer()
	{
		factory = new SMSCProcessorFactory();
		factory.setDispatcher(this);
		factory.setReceiveTimeout(receiveTimeout);

		factory.loadUsers(userFilePath);
		factory.start();

		server = new SMSCServer(listenPort);
		server.setReceiveTimeout(networkTimeout);
		server.setSleepTime(getDelayTime());
		server.setDispatcher(this);
		server.setMaxConnection(maxConnection);
		server.setProcessorFactory(factory);
		server.start();
	}

	@Override
	public void afterProcessSession() throws Exception
	{
		try
		{
			super.afterProcessSession();
		}
		finally
		{
			try
			{
				try
				{
					factory.stopAllProcessor();
					factory.stop();
				}
				finally
				{
					factory = null;
					server.stop();
				}
			}
			catch (Exception e)
			{
				debugMonitor(e);
			}
			finally
			{
				server = null;
			}
		}
	}
}
