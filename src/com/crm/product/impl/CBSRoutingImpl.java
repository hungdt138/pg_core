package com.crm.product.impl;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.SubscriberProductImpl;

public class CBSRoutingImpl extends VASOrderRoutingImpl
{
	private String CALL_BLOCKING_VIP = "CBSVIP";
	private String CALL_BLOCKING = "CBS";
	
	public CommandMessage unregisterCallBlock(OrderRoutingInstance _instance,
			ProductRoute route, CommandMessage request) throws Exception
	{
		ProductEntry product = ProductFactory.getCache().getProduct(
				CALL_BLOCKING);
		SubscriberProduct subscriber = SubscriberProductImpl.getUnterminated(
				request.getIsdn(), product.getProductId());

		if (subscriber == null)
		{
			product = ProductFactory.getCache().getProduct(
					CALL_BLOCKING_VIP);
			subscriber = SubscriberProductImpl.getUnterminated(request.getIsdn(),
					product.getProductId());
			if (subscriber == null)
			{
				request.setCause(Constants.ERROR_UNREGISTERED);
				request.setStatus(Constants.ORDER_STATUS_DENIED);

				return request;
			}
		}
		
		if (subscriber != null)
		{
			request.setProductId(product.getProductId());
			route.setProductId(product.getProductId());
		}

		request = parser(_instance, route, request);

		return request;
	}

	public CommandMessage changeBlackList(OrderRoutingInstance _instance,
			ProductRoute route, CommandMessage request) throws Exception
	{
		ProductEntry product = ProductFactory.getCache().getProduct(
				CALL_BLOCKING);
		SubscriberProduct subscriber = SubscriberProductImpl.getUnterminated(
				request.getIsdn(), product.getProductId());

		if (subscriber == null)
		{
			product = ProductFactory.getCache().getProduct(
					CALL_BLOCKING_VIP);
			subscriber = SubscriberProductImpl.getUnterminated(request.getIsdn(),
					product.getProductId());
			if (subscriber == null)
			{
				request.setCause(Constants.ERROR_UNREGISTERED);
				request.setStatus(Constants.ORDER_STATUS_DENIED);

				return request;
			}
		}
		
		if (subscriber != null && subscriber.isActive())
		{
			request.setProductId(product.getProductId());
			route.setProductId(product.getProductId());
		}
		
		request = parser(_instance, route, request);

		return request;
	}

	public CommandMessage changeWhiteList(OrderRoutingInstance _instance,
			ProductRoute route, CommandMessage request) throws Exception
	{
		ProductEntry product = ProductFactory.getCache().getProduct(
				CALL_BLOCKING);
		SubscriberProduct subscriber = SubscriberProductImpl.getUnterminated(
				request.getIsdn(), product.getProductId());

		if (subscriber == null)
		{
			product = ProductFactory.getCache().getProduct(
					CALL_BLOCKING_VIP);
			subscriber = SubscriberProductImpl.getUnterminated(request.getIsdn(),
					product.getProductId());
			if (subscriber == null)
			{
				request.setCause(Constants.ERROR_UNREGISTERED);
				request.setStatus(Constants.ORDER_STATUS_DENIED);

				return request;
			}
		}
		
		if (subscriber != null && subscriber.isActive())
		{
			request.setProductId(product.getProductId());
			route.setProductId(product.getProductId());
		}

		request = parser(_instance, route, request);

		return request;
	}

	public CommandMessage callBlocking(OrderRoutingInstance _instance,
			ProductRoute route, CommandMessage request) throws Exception
	{
		String responseCode = "";
		
		ProductEntry product = ProductFactory.getCache().getProduct(
				CALL_BLOCKING);
		SubscriberProduct subscriber = SubscriberProductImpl.getUnterminated(
				request.getIsdn(), product.getProductId());

		if (subscriber == null)
		{
			product = ProductFactory.getCache().getProduct(
					CALL_BLOCKING_VIP);
			subscriber = SubscriberProductImpl.getUnterminated(request.getIsdn(),
					product.getProductId());
			if (subscriber == null)
			{
				request.setCause(Constants.ERROR_UNREGISTERED);
				request.setStatus(Constants.ORDER_STATUS_DENIED);

				return request;
			}
		}
		
		if (subscriber != null && subscriber.isActive())
		{
			request.setProductId(product.getProductId());
			route.setProductId(product.getProductId());
		}
		
		String strRequest = request.getKeyword().toUpperCase();
		if (strRequest.equals("HD"))
		{
			responseCode = Constants.SUCCESS;
			request.setCause(responseCode);
			request.setStatus(Constants.ORDER_STATUS_DENIED);

			return request;
		}

		request = parser(_instance, route, request);

		return request;
	}
}
