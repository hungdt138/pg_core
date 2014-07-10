package com.crm.product.impl;

import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.SubscriberOrderImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.DateUtil;
import com.fss.util.AppException;

public class VASOrderRoutingImpl extends VNMOrderRoutingImpl
{
	@Override
	public void validate(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		if (order.getActionType()
				.equals(Constants.ACTION_UNREGISTER))
		{
			Date now = new Date();
			
			SubscriberProduct subscriberProduct = SubscriberProductImpl.getProduct(order
					.getSubProductId());
			if (subscriberProduct != null)
			{
				if (order.getSubscriberType() == Constants.POSTPAID_SUB_TYPE
						&& DateUtil.compareDate(subscriberProduct.getRegisterDate(), now) == 0)
				{
					throw new AppException(
								Constants.ERROR_UNREGISTER_INDAY);
				}
			}
		}
	}
	
	@Override
	public void checkUpgrade(OrderRoutingInstance instance,
			ProductEntry product, CommandMessage order) throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		if (product.getUpgradeProducts().length > 0)
		{
			for (int j = 0; (subscriberProduct == null)
					&& (j < product.getUpgradeProducts().length); j++)
			{
				long productId = product.getUpgradeProducts()[j];

				subscriberProduct = SubscriberProductImpl.getUnterminated(
						order.getIsdn(), productId);

				if (subscriberProduct != null)
				{
					order.setAssociateProductId(subscriberProduct
							.getProductId());
				}
			}
		}
	}
	
//	@Override
//	public void checkBlacklist(OrderRoutingInstance instance,
//			ProductEntry product, ProductRoute orderRoute, CommandMessage order) throws Exception
//	{
//		SubscriberProduct subscriberProduct = null;
//
//		for (int j = 0; (subscriberProduct == null)
//					&& (j < product.getBlacklistProducts().length); j++)
//		{
//			int successOrder = SubscriberOrderImpl.getRegisteredOrder(
//					order.getIsdn(), product.getBlacklistProducts()[j], order.getOrderDate(), orderRoute.getDuplicateScan());
//			if (successOrder > 0)
//			{
//				throw new AppException(Constants.ERROR_BLACKLIST_PRODUCT);
//			}
//			else
//			{
//				subscriberProduct = SubscriberProductImpl.getUnterminated(
//						order.getIsdn(), product.getBlacklistProducts()[j]);
//			}
//		}
//		
//		if (subscriberProduct != null)
//		{
//			throw new AppException(Constants.ERROR_BLACKLIST_PRODUCT);
//		}
//	}
	
	@Override
	public CommandMessage parser(OrderRoutingInstance instance, ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		// TODO Auto-generated method stub
		order = super.parser(instance, orderRoute, order);
		order.getParameters().setBoolean("includeCurrentDay", true);
		return order;
	}
}
