package com.crm.smscsim;

import com.crm.thread.DispatcherThread;
import com.logica.smpp.Connection;
import com.logica.smpp.ServerPDUEventListener;
import com.logica.smpp.Transmitter;
import com.logica.smpp.pdu.PDU;

public class SMSCSession implements ISession
{
	private long				receiveTimeout	= 1000;

	private Connection			connection		= null;
	private Transmitter			transmitter		= null;
	private SMSCReceiver		receiver		= null;

	private DispatcherThread	dispatcher		= null;

	public void setDispatcher(DispatcherThread dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public DispatcherThread getDispatcher()
	{
		return dispatcher;
	}

	public SMSCSession(Connection connection)
	{
		this.connection = connection;
		receiveTimeout = connection.getReceiveTimeout();
		transmitter = new Transmitter(connection);
		receiver = new SMSCReceiver(transmitter, connection);
		receiver.setQueueWaitTimeout(receiveTimeout);
		receiver.setReceiveTimeout(receiveTimeout);
	}

	@Override
	public PDU receive(long timeout) throws Exception
	{
		return receiver.receive(timeout);
	}

	@Override
	public synchronized void send(PDU pdu) throws Exception
	{
		transmitter.send(pdu);
	}

	public void startSession()
	{
		receiver.start();
	}

	public void endSession()
	{
		try
		{
			try
			{
				connection.close();
				debugMonitor("Connection closed.");
			}
			finally
			{
				connection = null;
				debugMonitor("Receiver is stopping.");
				receiver.stop();
				debugMonitor("Receiver stopped.");
			}
		}
		catch (Exception e)
		{
			debugMonitor(e);
		}
		finally
		{
			receiver = null;
			transmitter = null;
		}
	}

	public void debugMonitor(Object message)
	{
		if (dispatcher != null)
			dispatcher.debugMonitor(message);
	}

	@Override
	public void setPDUListener(ServerPDUEventListener listener)
	{
		receiver.setServerPDUEventListener(listener);
	}

}
