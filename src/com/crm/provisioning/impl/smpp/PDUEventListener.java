package com.crm.provisioning.impl.smpp;

import java.util.Date;

import com.crm.provisioning.thread.SMPPThread;
import com.crm.thread.DispatcherThread;

import com.logica.smpp.*;
import com.logica.smpp.pdu.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

/**
 * Implements simple PDU listener which handles PDUs received from SMSC. It puts
 * the received requests into a queue and discards all received responses.
 * Requests then can be fetched (should be) from the queue by calling to the
 * method <code>getRequestEvent</code>.
 * 
 * @see Queue
 * @see ServerPDUEvent
 * @see ServerPDUEventListener
 * @see SmppObject
 */
public class PDUEventListener extends SmppObject implements ServerPDUEventListener
{
	private Session						session;
	private com.logica.smpp.util.Queue	receivedQueue		= new com.logica.smpp.util.Queue();
	private com.logica.smpp.util.Queue	enquiredLinkQueue	= new com.logica.smpp.util.Queue();

	private DispatcherThread			dispatcher			= null;

	public PDUEventListener(Session session)
	{
		this.session = session;
	}

	// //////////////////////////////////////////////////////
	// process file
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void logMonitor(Object message)
	{
		if (dispatcher != null)
		{
			dispatcher.logMonitor(message);
		}
	}

	// //////////////////////////////////////////////////////
	// process file
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void debugMonitor(String message)
	{
		if (dispatcher != null)
			dispatcher.debugMonitor(message);
	}

	public void handleEvent(ServerPDUEvent event)
	{
		PDU pdu = event.getPDU();
		if (pdu == null)
		{
			return;
		}

		if (pdu.isRequest())
		{
			if (pdu instanceof DeliverSM)
			{
				synchronized (receivedQueue)
				{
					logMonitor("Has incoming message: " + pdu.debugString());
					receivedQueue.enqueue(pdu);
					receivedQueue.notify();
				}
			}
			else
			{
				try
				{
					logMonitor("Has incoming message: " + pdu.debugString());
				}
				catch (Exception e)
				{
					logMonitor(e);
				}
			}

			if (pdu.canResponse())
			{
				Response response = ((Request) pdu).getResponse();
				// respond with default response
				// logMonitor("Going to send default response to request "
				
				try
				{
					DispatcherThread dispatcher = getDispatcher();

					boolean displayDebug = ((dispatcher != null) && dispatcher.displayDebug);
					
					final java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("dd/MM HH:mm:ss:SSS");
					
					String strLog = fmt.format(new java.util.Date()) ;
					String smsLog = response.debugString();
					
					if (displayDebug)
					{
						dispatcher.logToFile(strLog + " Begin send response: " + smsLog + "\r\n");
					}
					// debugMonitor("Begin send response: " + response.debugString());
					
					session.respond(response);
					
					if (displayDebug)
					{
						dispatcher.logToFile(strLog + " End send response: " + smsLog + "\r\n");
					}
					// debugMonitor("End send response: " +
					// response.debugString());
				}
				catch (Exception ex)
				{
					logMonitor(ex);
				}
			}
		}
		else if (pdu.isResponse())
		{

			if (pdu instanceof EnquireLinkResp)
			{
				logMonitor("Enquirelink response: " + pdu.debugString());

				/**
				 * Need to get enquirelink response?
				 */
				// synchronized (enquiredLinkQueue)
				// {
				// //debugMonitor("Has incoming response");
				// enquiredLinkQueue.enqueue(pdu);
				// enquiredLinkQueue.notifyAll();
				// }
			}
			else
			{
				debugMonitor("Reponse: async response received " + pdu.debugString());

				try
				{
					TransmitterMessage message = new TransmitterMessage();
					message.setSequenceNumber(pdu.getSequenceNumber());
					message = ((SMPPThread)getDispatcher()).detachTransmitter(message);
					message.getMessage().setResponseTime(new Date());
					message.getMessage().setResponse(pdu.debugString());
					((SMPPThread)getDispatcher()).sendTransmitterLog(message.getMessage());
				}
				catch (Exception e)
				{
					logMonitor(e.getMessage());
				}
				
			}
		}
		else
		{
			debugMonitor("pdu of unknown class (not request nor response) received, discarding " + pdu.debugString());
		}
	}

	/**
	 * Returns received pdu from the queue. If the queue is empty, the method
	 * blocks for the specified timeout.
	 */
	public PDU getPDU(long timeout)
	{
		PDU pdu = null;

		synchronized (receivedQueue)
		{
			if (receivedQueue.isEmpty())
			{
				try
				{
					if (timeout < 0)
						timeout = 1000;

					// debugMonitor("Dont have message, wait " + timeout);
					receivedQueue.wait(timeout);
				}
				catch (InterruptedException e)
				{
					// ignoring, actually this is what we're waiting for
				}
			}
			if (!receivedQueue.isEmpty())
			{
				// debugMonitor("Had message");
				pdu = (PDU) receivedQueue.dequeue();
			}
			else
			{
				// debugMonitor("Dont have message");
			}
		}
		return pdu;
	}

	/**
	 * Receive expected enquire link pdu with timeout
	 * 
	 * @param expected
	 * @param timeout
	 * @return expected pdu if exists or null if not.
	 */
	public PDU getEnquireLinkRespPDU(EnquireLinkResp expected, long timeout)
	{
		PDU pdu = null;

		synchronized (enquiredLinkQueue)
		{
			pdu = (PDU) enquiredLinkQueue.dequeue(expected);
			if (pdu == null)
			{
				try
				{
					if (timeout < 0)
						timeout = 1000;

					// debugMonitor("Dont have message, wait " + timeout);
					enquiredLinkQueue.wait(timeout);
				}
				catch (InterruptedException e)
				{
					// ignoring, actually this is what we're waiting for
				}
			}

			pdu = (PDU) enquiredLinkQueue.dequeue(expected);
		}
		return pdu;
	}

	/**
	 * Receive pdu without waiting.
	 * 
	 * @return pdu if exists or null if not.
	 */
	public PDU getPDUNoWait()
	{
		PDU pdu = null;

		synchronized (receivedQueue)
		{
			if (!receivedQueue.isEmpty())
			{
				// debugMonitor("Had message");
				pdu = (PDU) receivedQueue.dequeue();
			}
			else
			{
				// debugMonitor("Dont have message");
			}
		}
		return pdu;
	}

	/**
	 * Receive expected pdu without waiting.
	 * 
	 * @param expectedPDU
	 * @return expected pdu if exists or null if not
	 */
	public PDU getPDUNoWait(PDU expectedPDU)
	{
		PDU pdu = null;

		synchronized (receivedQueue)
		{
			if (!receivedQueue.isEmpty())
			{
				// debugMonitor("Had message");
				pdu = (PDU) receivedQueue.dequeue(expectedPDU);
			}
			else
			{
				// debugMonitor("Dont have message");
			}
		}
		return pdu;
	}

	public void setDispatcher(DispatcherThread dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public SMPPThread getDispatcher()
	{
		return (SMPPThread) dispatcher;
	}

	public int getTempQueueSize()
	{
		synchronized (receivedQueue)
		{
			return receivedQueue.size();
		}
	}
}
