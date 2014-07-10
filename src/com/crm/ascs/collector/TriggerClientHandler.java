package com.crm.ascs.collector;

import java.io.IOException;
import java.net.Socket;

import com.crm.ascs.collector.thread.CollectorThread;
import com.crm.ascs.net.INetAnalyzer;
import com.crm.ascs.net.INetConnection;
import com.crm.ascs.net.INetData;
import com.crm.ascs.net.INetHandler;
import com.crm.ascs.net.NetThread;

public class TriggerClientHandler extends NetThread implements INetHandler
{

	private int								handlerId			= 0;
	private TriggerCollection				triggerCollection	= new TriggerCollection();
	private INetAnalyzer					analyzer			= null;
	private TriggerConnection				connection			= null;
	private TriggerClientHandlerCollection	handlerCollection	= null;
	private CollectorThread					dispatcher			= null;

	public INetAnalyzer getAnalyzer()
	{
		return analyzer;
	}

	public void setAnalyzer(INetAnalyzer analyzer)
	{
		this.analyzer = analyzer;
		this.analyzer.setHandler(this);
	}

	public void setHandlerId(int handlerId)
	{
		this.handlerId = handlerId;
		if (connection != null)
			connection.setConnectionId(handlerId);
	}

	public int getHandlerId()
	{
		return handlerId;
	}

	public TriggerClientHandler(Socket socket, CollectorThread dispatcher) throws IOException
	{
		this.dispatcher = dispatcher;
		this.setSleepTime(dispatcher.getDelayTime());
		this.connection = new TriggerConnection(socket, this.dispatcher);
		this.connection.setHandler(this);
		this.connection.setConnectionId(getHandlerId());
	}

	public void setTriggerClientHandlerCollection(TriggerClientHandlerCollection handlerCollection)
	{
		this.handlerCollection = handlerCollection;
	}

	@Override
	public INetConnection getConnection()
	{
		return connection;
	}

	public void start()
	{
		try
		{
			if (connection == null)
				throw new Exception("Need to set connection for handler.");

			debugMonitor("Client #" + getHandlerId() + " (" +
					connection.getAddress().getHostAddress() + ":" + connection.getPort() +
					")" + " connected.");
			connection.start();
			super.start();
		}
		catch (Exception e)
		{
			debugMonitor(e);
			stop();
		}
	}

	@Override
	public void stop()
	{
		String strLog = "";
		try
		{
			if (connection == null)
				throw new Exception("Need to set connection for handler.");
			try
			{
				if (handlerCollection != null)
				{
					if (handlerCollection.contains(this))
						handlerCollection.remove(this);
				}

				strLog = "Client #" + getHandlerId() + " (" +
						connection.getAddress().getHostAddress() + ":" + connection.getPort() +
						")" + " was disconnected.";

				connection.stop();
			}
			finally
			{
				debugMonitor(strLog);
			}
		}
		catch (Exception e)
		{

		}
		finally
		{
			super.stop(dispatcher.networkTimeout);
		}
	}

	public void debugMonitor(Object message)
	{
		if (dispatcher != null)
		{
			dispatcher.debugMonitor(message);
		}
	}

	@Override
	public void handle(byte[] data)
	{
		if (data != null)
			analyzer.createObject(data, triggerCollection);
		else
			stop();
	}

	@Override
	public void process() throws Exception
	{
		Trigger trigger = (Trigger) triggerCollection.get();
		while (null != trigger)
		{
			trigger.setRemoteHost(connection.getAddress().getHostAddress());
			trigger.setRemotePort(connection.getPort());
			dispatcher.addWork(trigger);
			trigger = (Trigger) triggerCollection.get();
		}

	}
}
