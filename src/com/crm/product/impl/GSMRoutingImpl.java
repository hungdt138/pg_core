package com.crm.product.impl;

import com.crm.kernel.message.Constants;
import com.crm.merchant.cache.MerchantAgent;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.subscriber.impl.GSMServiceImpl;
import com.crm.subscriber.impl.SubscriberEntryImpl;
import com.fss.util.AppException;

public class GSMRoutingImpl extends OrderRoutingImpl
{
	@Override
	public CommandMessage parser(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		Exception error = null;
		ProductEntry product = null;
		try
		{
			smsParser(instance, orderRoute, order);
			
			// check product in available list
			product = ProductFactory.getCache().getProduct(order.getProductId());
			
			// Set subscriber type
			if (order.getSubscriberType() == Constants.UNKNOW_SUB_TYPE)
			{
				order.setSubscriberType(SubscriberEntryImpl.getSubscriberType(order.getIsdn()));
			}
			
			checkAgent(instance, orderRoute, order);
			
			String parserParams = order.getParameters().getString("sms.params[0]");
			if (parserParams == null || parserParams.equals(""))
			{
				throw new AppException(Constants.ERROR_INVALID_REQUEST);
			}
			
			String[] params = order.getParameters().getString("sms.params[0]").split("||");
			if (params.length != 14)
			{
				throw new AppException(Constants.ERROR_INVALID_REQUEST);
			}
		}
		catch (Exception e)
		{
			error = e;
		}
		
		if (error != null)
		{
			order.setStatus(Constants.ORDER_STATUS_DENIED);

			if (error instanceof AppException)
			{
				order.setCause(error.getMessage());
			}
			else
			{
				order.setDescription(error.getMessage());
			}
		}
		
		return order;
	}

	public void checkAgent(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		MerchantAgent agent = GSMServiceImpl.getAgent(order.getIsdn());
		
		if (agent == null)
		{
			throw new AppException(Constants.ERROR_AGENT_NOT_FOUND);
		}
	}
}
