package com.crm.provisioning.thread.osa;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright:
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author: HungPQ
 * @version 1.0
 */

public class CallbackServer implements Runnable
{
	protected OSACallbackThread	dispatcher;

	protected ServerSocket		server;

	protected int				port	= 5000;

	protected Thread			thread	= null;

	public CallbackServer(ServerSocket server, OSACallbackThread dispatcher)
	{
		this.dispatcher = dispatcher;
		this.server = server;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Start thread
	 * 
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void start()
	{
		// Destroy previous if it's constructed
		destroy();

		// Start new thread
		Thread thread = new Thread(this);

		thread.start();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Start thread
	 * 
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void stop()
	{
		destroy();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Destroy thread
	 * 
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void destroy()
	{
		try
		{
			Thread.sleep(100);

			if ((thread != null) && !thread.isInterrupted())
			{
				Thread tmpThread = thread;
				thread = null;

				if (tmpThread != null)
				{
					tmpThread.interrupt();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try
		{
			while (!server.isClosed() && ((dispatcher == null) || dispatcher.isAvailable()))
			{
				// The server do a loop here to accept all connection initiated
				// by the client application.

				try
				{
					Socket socket = server.accept();
					
					socket.setSoLinger(true, 1);
					socket.setTcpNoDelay(true);
					// socket.setReceiveBufferSize(1* 1024 * 1024);
					// socket.setSendBufferSize(1* 1024 * 1024);

					new OSACallbackHandler(socket, dispatcher);
				}
				catch (Exception e)
				{
					throw e;
				}
				finally
				{
					Thread.sleep(10);
				}
			}
		}
		catch (Exception e)
		{
			dispatcher.logMonitor(e);
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
}
