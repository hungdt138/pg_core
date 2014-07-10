package com.crm.provisioning.impl.charging;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Calendar;
import java.util.Date;

import javax.activity.InvalidActivityException;

public class TCPClient
{
	private String			host			= "";
	private int				port			= 0;
	private long			timeout			= 1000;
	private boolean			useListener		= false;

	private Socket			socket			= null;
	private DataReceiver	receiver		= null;
	private TCPDataListener	dataListener	= null;

	private Object			mutex			= new Object();

	public void setListener(TCPDataListener listener)
	{
		dataListener = listener;
		dataListener.setTCPClient(this);
		useListener = true;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getHost()
	{
		return host;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public int getPort()
	{
		return port;
	}

	public boolean isConnected()
	{
		if (socket != null)
			return socket.isConnected();
		else
			return false;
	}

	public TCPClient()
	{
	}

	public TCPClient(String host, int port)
	{
		setHost(host);
		setPort(port);
	}

	public void connect(String host, int port) throws Exception
	{
		setHost(host);
		setPort(port);
		connect();
	}

	public void connect() throws Exception
	{
		InetAddress inetAddress = InetAddress.getByName(getHost());
		SocketAddress socketAddress = new InetSocketAddress(inetAddress, getPort());
		socket = new Socket();
		socket.connect(socketAddress);
	}

	public void send(byte[] data) throws Exception
	{
		synchronized (mutex)
		{
			socket.getOutputStream().write(data);
			socket.getOutputStream().flush();
		}
	}

	public Object receive(String seq)
	{
		return dataListener.getResponse(seq);
	}

	public void start() throws Exception
	{
		if (!useListener)
		{
			return;
		}

		if (receiver != null)
			receiver.shutdown();
		if (!isConnected())
			connect();
		receiver = new DataReceiver();
		receiver.start();
	}

	public void close()
	{
		if (useListener & receiver != null)
			receiver.shutdown();

		if (socket != null)
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				socket = null;
			}
		}
	}

	private class DataReceiver extends Thread
	{
		private boolean	running	= false;

		public DataReceiver()
		{
		}

		@Override
		public void start()
		{
			running = true;
			super.start();
		}

		@Override
		public void run()
		{
			while (running)
			{
				synchronized (mutex)
				{
					try
					{
						if (socket.getInputStream().available() > 0)
						{
							byte[] buffer = new byte[0];
							byte[] receivedData = new byte[8];
							// System.out.println("waiting data...");
							int byteCount = 0;
							while (socket.getInputStream().available() > 0)
							{
								byteCount = socket.getInputStream().read(receivedData);
								byte[] newBuffer = new byte[buffer.length + byteCount];
								System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
								System.arraycopy(receivedData, 0, newBuffer, buffer.length, byteCount);
								buffer = newBuffer;
								byteCount = 0;
							}
							dataListener.onReceive(buffer);
						}
					}
					catch (Exception e)
					{
					}
				}
			}
		}

		public void shutdown()
		{
			running = false;
			try
			{
				join();
			}
			catch (InterruptedException ie)
			{
				interrupt();
			}
		}
	}
}
