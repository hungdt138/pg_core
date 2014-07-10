package com.crm.provisioning.thread;

import java.util.Date;

import javax.jms.Message;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.message.CommandMessage;
import com.crm.subscriber.impl.SubscriberOrderImpl;

public class TestDBInstance extends OrderRoutingInstance
{

	public TestDBInstance() throws Exception
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int processMessage(Message request) throws Exception
	{
		try
		{
			CommandMessage order = (CommandMessage) QueueFactory.getContentMessage(request);

			Date startTime = new Date();
			SubscriberOrderImpl.isDuplicatedOrder(order.getIsdn(),
					order.getProductId(), order.getOrderDate(),
					60);

			Date endTime = new Date();
			debugMonitor("Check duplicate(" + order.getIsdn() + ") cost time: " + (endTime.getTime() - startTime.getTime())
					+ "ms");

			startTime = new Date();
			SubscriberOrderImpl.getRegisteredOrder(
					order.getIsdn(), order.getProductId(), order.getOrderDate());

			endTime = new Date();
			debugMonitor("Check maxregisterdaily(" + order.getIsdn() + ")  cost time: "
					+ (endTime.getTime() - startTime.getTime()) + "ms");
		}
		catch (Exception e)
		{
		}

		return Constants.ORDER_STATUS_DENIED;
	}

}
