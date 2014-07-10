package com.crm.smscsim.thread;

import javax.jms.Message;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.DispatcherInstance;
import com.logica.smpp.pdu.DeliverSM;
import com.logica.smpp.pdu.WrongLengthOfStringException;

public class SMSCServerInstance extends DispatcherInstance
{

	public SMSCServerInstance() throws Exception
	{
		super();
	}

	@Override
	public Message detachMessage() throws Exception
	{
		if (((SMSCServerThread) getDispatcher()).factory == null)
			return null;
		if (((SMSCServerThread) getDispatcher()).factory.receiverCount() == 0)
			return null;

		return super.detachMessage();
	}

	@Override
	public void afterProcessSession() throws Exception
	{
		super.afterProcessSession();
	}

	@Override
	public int processMessage(Message message) throws Exception
	{
		CommandMessage request = (CommandMessage) QueueFactory.getContentMessage(message);

		DeliverSM deliverSM = createDeliverSM(request);

		if (deliverSM != null)
		{
			((SMSCServerThread) getDispatcher()).factory.enqueue(deliverSM, request.getUserName());
		}

		return Constants.BIND_ACTION_NONE;
	}

	protected DeliverSM createDeliverSM(CommandMessage request)
	{
		DeliverSM deliverSM = null;

		try
		{
			deliverSM = new DeliverSM();
			deliverSM.setSourceAddr(request.getIsdn());
			deliverSM.setShortMessage(request.getKeyword());
			deliverSM.setDestAddr(request.getServiceAddress());
//			debugMonitor("Prepare to send from [" + deliverSM.getSourceAddr().getAddress()
//					+ "] to [" + request.getUserName()
//					+ "] content [" + deliverSM.getDestAddr().getAddress() + ": "
//					+ deliverSM.getShortMessage() + "]");
		}
		catch (WrongLengthOfStringException e)
		{
			debugMonitor(e);
		}
		finally
		{
		}

		return deliverSM;
	}
}
