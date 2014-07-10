package com.crm.smscsim;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.crm.ascs.collector.TriggerAnalyzer;
import com.crm.ascs.collector.TriggerClientHandler;
import com.crm.ascs.net.NetThread;
import com.crm.smscsim.thread.SMSCServerThread;
import com.crm.thread.DispatcherThread;
import com.logica.smpp.Connection;
import com.logica.smpp.TCPIPConnection;

public class SMSCServer extends NetThread
{
	private Connection			serverConnection	= null;
	private int					port				= 0;
	private int					receiveTimeout		= 1000;
	private int					maxConnection		= 10;
	private IProcessorFactory	processorFactory	= null;

	private DispatcherThread	dispatcher			= null;

	public void setDispatcher(DispatcherThread dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public DispatcherThread getDispatcher()
	{
		return dispatcher;
	}

	public int getPort()
	{
		return port;
	}

	public int getReceiveTimeout()
	{
		return receiveTimeout;
	}

	public void setReceiveTimeout(int receiveTimeout)
	{
		this.receiveTimeout = receiveTimeout;
	}

	public void setMaxConnection(int maxConnection)
	{
		this.maxConnection = maxConnection;
	}

	public int getMaxConnection()
	{
		return maxConnection;
	}

	public SMSCServer(int port)
	{
		this.port = port;
	}

	@Override
	public void start()
	{
		if (isRunning())
			destroy();
		try
		{
			openConnection();
		}
		catch (Exception e)
		{

		}
		super.start();
	}

	private void openConnection() throws Exception
	{
		if (serverConnection != null)
			return;
		serverConnection = new TCPIPConnection(port);
		serverConnection.setReceiveTimeout(receiveTimeout);
		serverConnection.open();
	}

	private void closeConnection() throws IOException
	{
		try
		{
			if (serverConnection == null)
				return;
			try
			{
				serverConnection.close();
			}
			catch (Exception e)
			{
				debugMonitor(e);
			}
		}
		finally
		{
			serverConnection = null;
		}
	}

	@Override
	public void stop()
	{
		destroy();
	}

	@Override
	public void destroy()
	{
		if (!isRunning())
			return;

		try
		{
			super.destroy();

			processorFactory.stopAllProcessor();
			try
			{
				closeConnection();
			}
			catch (IOException e)
			{
				debugMonitor(e);
			}
		}
		catch (Exception e)
		{
			debugMonitor(e);
		}
		finally
		{
			// super.destroy();
		}
	}

	@Override
	public void process() throws Exception
	{
		try
		{
			if (!isRunning())
			{
				stop();
				return;
			}

			if (((SMSCProcessorFactory) processorFactory).processorCount() >= maxConnection)
			{
				closeConnection();
			}
			else
			{
				openConnection();
				Connection connection = serverConnection.accept();
				if (connection != null)
				{
					connection.setReceiveTimeout(receiveTimeout);
					connection.setCommsTimeout(receiveTimeout);
					SMSCSession handler = new SMSCSession(connection);
					handler.setDispatcher(dispatcher);
					IProcessor processor = processorFactory.createProcessor(handler);
					handler.setPDUListener(processor);
					debugMonitor("Client #" + processor.getProcessorId() + " connected.");
					handler.startSession();
				}
			}
		}
		catch (SocketTimeoutException ste)
		{
		}
		catch (SocketException se)
		{
		}
		catch (Exception e)
		{
			closeConnection();
			throw e;
		}
	}

	@Override
	public void debugMonitor(Object message)
	{
		if (dispatcher != null)
			dispatcher.debugMonitor(message);
	}

	public void setProcessorFactory(IProcessorFactory processorFactory)
	{
		this.processorFactory = processorFactory;
	}

	public IProcessorFactory getProcessorFactory()
	{
		return processorFactory;
	}

}
