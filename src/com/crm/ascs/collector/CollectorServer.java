package com.crm.ascs.collector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.log4j.net.SocketAppender;

import com.crm.ascs.collector.thread.CollectorThread;
import com.crm.ascs.net.NetThread;

public class CollectorServer extends NetThread
{
	protected CollectorThread					dispatcher			= null;
	protected ServerSocket						server				= null;
	protected TriggerClientHandlerCollection	collectorHandlers	= new TriggerClientHandlerCollection();

	private int									currentId			= 1;

	public boolean isClosed()
	{
		if (server == null)
			return true;
		return server.isClosed();
	}

	public CollectorServer(CollectorThread dispatcher) throws IOException
	{
		this.dispatcher = dispatcher;
		setSleepTime(dispatcher.getDelayTime());
	}

	private void openSocket() throws IOException
	{
		if (server != null)
			return;
		server = new ServerSocket(dispatcher.listenPort, dispatcher.maxConnection);
		server.setSoTimeout(dispatcher.networkTimeout);
	}

	private void closeSocket() throws IOException
	{
		try
		{
			if (server == null)
				return;
			server.close();
		}
		finally
		{
			server = null;
		}
	}

	@Override
	public void start()
	{
		if (isRunning())
			destroy();
		super.start();
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
		{
			return;
		}

		try
		{

			while (collectorHandlers.size() > 0)
			{
				TriggerClientHandler handler = collectorHandlers.remove(0);
				try
				{
					handler.stop();
					handler = null;
				}
				catch (Exception e)
				{
					debugMonitor(e);
				}
			}
			try
			{
				closeSocket();
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
			super.destroy();
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

			if (collectorHandlers.size() >= dispatcher.maxConnection)
			{
				closeSocket();
			}
			else
			{
				openSocket();
				Socket socket = server.accept();
				socket.setSoTimeout(dispatcher.networkTimeout);
				TriggerClientHandler handler = new TriggerClientHandler(socket, dispatcher);
				handler.setAnalyzer(new TriggerAnalyzer());
				handler.setHandlerId(currentId++);
				collectorHandlers.add(handler);

				handler.start();
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
			throw e;
		}
		finally
		{
			if ((server != null) && server.isClosed())
			{
				try
				{
					server.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void debugMonitor(Object message)
	{
		if (dispatcher != null)
			dispatcher.debugMonitor(message);
	}

}
