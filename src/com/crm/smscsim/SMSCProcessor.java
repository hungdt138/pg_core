package com.crm.smscsim;

import com.logica.smpp.Data;
import com.logica.smpp.pdu.PDU;
import com.logica.smpp.pdu.Request;
import com.logica.smpp.pdu.SubmitSM;
import com.logica.smpp.pdu.SubmitSMResp;
import com.logica.smpp.util.Queue;

public class SMSCProcessor extends SMSCProcessorBase implements Runnable
{

	private ISession			session			= null;
	private IProcessorFactory	factory			= null;

	private Thread				thread			= null;
	private boolean				isClientRequest	= false;
	private Queue				localQueue		= new Queue();

	public SMSCProcessor(IProcessorFactory factory, ISession session)
	{
		this.factory = factory;
		receiveTimeout = factory.getReceiveTimeout();
		this.session = session;
	}

	@Override
	public IProcessorFactory getFactory()
	{
		return factory;
	}

	@Override
	public ISession getSession()
	{
		return session;
	}

	@Override
	public SubmitSMResp processSubmitSM(SubmitSM request)
	{
		return (SubmitSMResp) request.getResponse();
	}

	public PDU detachMessage()
	{
		synchronized (localQueue)
		{
			if (localQueue.size() > 0)
			{
				isClientRequest = true;
				return (PDU) localQueue.dequeue();
			}
		}
		if (getBindType() == Data.BIND_TRANSMITTER)
		{
			return null;
		}

		isClientRequest = false;
		return ((SMSCProcessorFactory) factory).dequeue(getSystemId());
	}

	public void start()
	{
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void stop()
	{
		try
		{
			super.stop();
		}
		finally
		{
			if (thread != null)
				thread.interrupt();
		}
	}

	@Override
	public void run()
	{
		while (validate())
		{
			PDU pdu = detachMessage();

			while (pdu != null && validate())
			{
				try
				{
					if (isClientRequest)
					{
						serverResponse(((Request) pdu).getResponse());
					}
					else
					{
						serverRequest((Request) pdu);
					}
				}
				catch (Exception e)
				{
					debugMonitor(e);
					try
					{
						disconnect();
					}
					catch (Exception ex)
					{
						debugMonitor(ex);
					}
				}

				pdu = detachMessage();
			}

			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				debugMonitor(e);
			}
		}

	}
}
