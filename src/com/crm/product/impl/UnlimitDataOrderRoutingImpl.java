package com.crm.product.impl;

import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.IDDServiceImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.DateUtil;
import com.fss.util.AppException;

public class UnlimitDataOrderRoutingImpl extends DataOrderRoutingImpl
{
	@Override
	public void checkActionType(
			OrderRoutingInstance instance, ProductRoute orderRoute, ProductEntry product
			, CommandMessage order, SubscriberProduct subscriberProduct)
			throws Exception
	{
		Date now = new Date();

		try
		{
			if (subscriberProduct != null)
			{
				int remainDays = 0;
				if (subscriberProduct.getExpirationDate() != null)
				{
					remainDays = DateUtil.getDateDiff(now, subscriberProduct.getExpirationDate());
				}

				if (remainDays < 0)
				{
					remainDays = 0;
				}

				order.setResponseValue("service.activeDays", remainDays);
				order.setResponseValue("service.activeDate", subscriberProduct.getExpirationDate());
			}

			if (subscriberProduct != null)
			{
				order.setSubProductId(subscriberProduct.getSubProductId());
			}

			String actionType = order.getActionType();

			if ((actionType.equals(Constants.ACTION_REGISTER)
					|| actionType.equals(Constants.ACTION_FREE))
					&& (subscriberProduct != null))
			{

				/**
				 * Check if isTopupEnable() and (subscriberproduct.isBarring or
				 * subscriberProduct.expirationDate < sysDate)
				 */
				if (orderRoute.isTopupEnable() &&
						(subscriberProduct.isBarring() || 
								(subscriberProduct.isPrepaid() && subscriberProduct.getExpirationDate().before(new Date()))))
				{
					actionType = Constants.ACTION_TOPUP;
				}
				else
				{
					throw new AppException(Constants.ERROR_REGISTERED);
				}
			}

			if (product.isSubscription())
			{
				if ((subscriberProduct == null) || subscriberProduct.isCancel())
				{
					if (actionType.equals(Constants.ACTION_SUBSCRIPTION))
					{
						throw new AppException(Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
					}
					else if (actionType.equals(Constants.ACTION_UNREGISTER))
					{
						throw new AppException(Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
					}
					else if (actionType.equals(Constants.ACTION_CANCEL))
					{
						throw new AppException(Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
					}
					else if (actionType.equals(Constants.ACTION_TOPUP))
					{
						actionType = Constants.ACTION_REGISTER;
					}
				}
				else if (subscriberProduct != null)
				{
					if (orderRoute.isTopupEnable())
					{
						actionType = Constants.ACTION_TOPUP;
					}
					else if (actionType.equals(Constants.ACTION_SUBSCRIPTION))
					{
						if (DateUtil.compareDate(subscriberProduct.getExpirationDate(), now) >= 0)
						{
							actionType = Constants.ACTION_SUBSCRIPTION;
						}
					}
				}
			}

			// get associate product
			if (actionType.equals(Constants.ACTION_REGISTER) || actionType.equals(Constants.ACTION_UPGRADE))
			{
				checkBlacklist(instance, product, order);

				checkUpgrade(instance, product, order);

				if (order.getAssociateProductId() != Constants.DEFAULT_ID)
				{
					actionType = Constants.ACTION_UPGRADE;
				}
			}

			order.setActionType(actionType);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	public CommandMessage registerMPServices(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		try
		{
			order = super.parser(instance, orderRoute, order);
			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
				ProductEntry product = ProductFactory.getCache().getProduct(order.getProductId());
				int checkUsage = SubscriberProductImpl.checkUsage(order.getIsdn(), product.getAlias());
				if (checkUsage == Constants.VERIFY_CONDITION_FAIL)
				{
					order.setCause(Constants.ERROR_NOT_ENOUGH_CONDITION);
					order.setStatus(Constants.ORDER_STATUS_DENIED);
				}
				else if (checkUsage != Constants.VERIFY_CONDITION_SUCCESS)
				{
					order.setCause(Constants.ERROR);
					order.setStatus(Constants.ORDER_STATUS_DENIED);
				}
			}
		}
		catch (Exception e)
		{
			order.setStatus(Constants.ORDER_STATUS_DENIED);
			order.setCause(Constants.ERROR);
			order.setDescription(e.getMessage());
		}

		return order;
	}
}
