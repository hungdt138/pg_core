/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.crm.marketing.impl;

import java.sql.Connection;

import com.crm.kernel.message.Constants;
import com.crm.marketing.cache.CampaignEntry;
import com.crm.marketing.message.CampaignMessage;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.util.DateUtil;

import com.fss.util.AppException;

/**
 * The implementation of the subscriber entry local service.
 * 
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link com.sdp.subscriber.service.SubscriberEntryLocalService} interface.
 * </p>
 * 
 * <p>
 * Never reference this interface directly. Always use
 * {@link com.sdp.subscriber.service.SubscriberEntryLocalServiceUtil} to access
 * the subscriber entry local service.
 * </p>
 * 
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 * 
 * @author Phan Viet Thang
 * @see com.sdp.subscriber.service.base.SubscriberEntryLocalServiceBaseImpl
 * @see com.sdp.subscriber.service.SubscriberEntryLocalServiceUtil
 */
public class AgeActiveCampaignImpl extends CampaignImpl
{
	public boolean validate(Connection connection, CampaignMessage message) throws Exception
	{
		if (message == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public int processMessage(Connection connection, CampaignEntry campaign, CampaignMessage request) throws Exception
	{
		int status = Constants.ORDER_STATUS_PENDING;
		String cause = "";

		try
		{
			if (request.getActiveDate() != null)
			{
				String unit = campaign.getParameters().getString("campaign.unit", "year");
				int period = campaign.getParameters().getInteger("campaign.period", 1);

				request.setQuantity(DateUtil.getDateDiff(request.getActiveDate(), request.getOrderDate(), unit, period));

				if (request.getQuantity() >= 1)
				{
					ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());

					if (product == null)
					{
						throw new AppException("product-not-found");
					}
					
					request.setScore(product.getScore(request.getQuantity(), request.getOrderDate()));
				}

				if (request.getScore() > 0)
				{
					status = Constants.ORDER_STATUS_APPROVED;
				}
			}
		}
		catch (AppException e)
		{
			status = Constants.ORDER_STATUS_DENIED;
			cause = e.getMessage();

			return Constants.BIND_ACTION_ERROR;
		}
		catch (Exception e)
		{
			status = Constants.ORDER_STATUS_DENIED;
			
			throw e;
		}
		finally
		{
			request.setStatus(status);
			request.setCause(cause);
		}

		return Constants.BIND_ACTION_SUCCESS;
	}
}