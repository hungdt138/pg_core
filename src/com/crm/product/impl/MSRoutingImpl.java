package com.crm.product.impl;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.SubscriberProductImpl;

public class MSRoutingImpl extends VASOrderRoutingImpl
{
	public CommandMessage changeHandSet(OrderRoutingInstance _instance,
			ProductRoute route, CommandMessage request) throws Exception
	{
		SubscriberProduct subscriber = SubscriberProductImpl.getActive(
				request.getIsdn(), request.getProductId());

		if (subscriber == null)
		{
			request.setCause(Constants.ERROR_UNREGISTERED);
			request.setStatus(Constants.ORDER_STATUS_DENIED);

			return request;
		}		
		request = parser(_instance, route, request);

		return request;
	}
}
