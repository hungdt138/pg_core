package com.crm.product.impl;

import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.IDDServiceImpl;
import com.crm.util.DateUtil;
import com.fss.util.AppException;

public class DataSimRoutingImpl extends VASOrderRoutingImpl
{
	private final static String ERROR_CONFIRM_NOT_EXISTS = "confirm-not-exists";
	private final static String ERROR_NOT_CONFIRM = "not-confirm";
	
	public CommandMessage register(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		String responseCode = "";
		try
		{
			SubscriberProduct subProduct = IDDServiceImpl.isConfirmRegister(
					order.getIsdn(), order.getProductId());
			if (subProduct == null)
			{
				responseCode = ERROR_CONFIRM_NOT_EXISTS;
				order.setCause(responseCode);
				order.setStatus(Constants.ORDER_STATUS_DENIED);

				return order;
			}
			order.setSubProductId(subProduct.getSubProductId());
			
			IDDServiceImpl.removeConfirm(subProduct.getSubProductId());
			order = parser(instance, orderRoute, order);
		}
		catch (Exception e)
		{
			order.setStatus(Constants.ORDER_STATUS_DENIED);
			order.setCause(Constants.ERROR);
			order.setDescription(e.getMessage());
		}

		return order;
	}
	
	public CommandMessage confirmRegisterService(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage order) throws Exception
	{
		String responseCode = "";

		try
		{
			order = parser(instance, orderRoute, order);
			if (order.getStatus() != Constants.ORDER_STATUS_DENIED)
			{
				SubscriberProduct subProductConfirm = IDDServiceImpl.isConfirmRegister(
						order.getIsdn(), order.getProductId());
				if (subProductConfirm != null)
				{
					responseCode = ERROR_NOT_CONFIRM;
					order.setCause(responseCode);
					order.setStatus(Constants.ORDER_STATUS_DENIED);

					return order;
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
	
	@Override
	public void checkActionType(OrderRoutingInstance instance,
			ProductRoute orderRoute, ProductEntry product,
			CommandMessage order, SubscriberProduct subscriberProduct)
			throws Exception
	{
		Date now = new Date();

		order.setRequestValue("first-action-type", order.getActionType());
		try
		{
			if (subscriberProduct != null)
			{
				int remainDays = 0;
				if (subscriberProduct.getExpirationDate() != null)
				{
					remainDays = DateUtil.getDateDiff(now,
							subscriberProduct.getExpirationDate());
				}
				if (remainDays < 0)
				{
					remainDays = 0;
				}

				order.setResponseValue("service.activeDays", remainDays);
				if (subscriberProduct.getExpirationDate() != null)
				{
					order.setResponseValue("service.activeDate",
							subscriberProduct.getExpirationDate());
				}
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
				else if (subscriberProduct.isBarring() || subscriberProduct.isActive())
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
						throw new AppException(
								Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
					}
					else if (actionType.equals(Constants.ACTION_UNREGISTER))
					{
						throw new AppException(
								Constants.ERROR_UNREGISTERED);
					}
					else if (actionType.equals(Constants.ACTION_TOPUP))
					{
						actionType = Constants.ACTION_REGISTER;
					}
				}
				else if (subscriberProduct != null)
				{
					if (orderRoute.isTopupEnable()
							&& subscriberProduct.isBarring())
					{
						actionType = Constants.ACTION_TOPUP;
					}
					else if (actionType
								.equals(Constants.ACTION_SUBSCRIPTION))
					{
						if (subscriberProduct.getExpirationDate().after(now))
						{
							throw new AppException(
										Constants.ERROR_REGISTERED);
						}
					}
				}
			}

			// get associate product
			if (actionType.equals(Constants.ACTION_REGISTER)
					|| actionType.equals(Constants.ACTION_UPGRADE)
					|| actionType.equals(Constants.ACTION_FREE))
			{
				checkBlacklist(instance, product, order);

				if (!actionType.equals(Constants.ACTION_FREE))
				{
					checkUpgrade(instance, product, order);
	
					if (order.getAssociateProductId() != Constants.DEFAULT_ID)
					{
						actionType = Constants.ACTION_UPGRADE;
					}
				}
			}

			order.setActionType(actionType);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
}
