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
import java.util.Date;

import com.crm.kernel.index.ExecuteImpl;
import com.crm.kernel.message.Constants;
import com.crm.marketing.cache.CampaignEntry;
import com.crm.marketing.message.CampaignMessage;
import com.crm.util.DateUtil;

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
public class CampaignImpl extends ExecuteImpl
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

	public Date getNextDate(Connection connection, CampaignEntry campaign, CampaignMessage message) throws Exception
	{
		Date nextDate = null;

		if (campaign.isScheduleEnable())
		{
			if (message.getStatus() == Constants.ORDER_STATUS_PENDING)
			{
				nextDate = DateUtil.addDate(new Date(), "daily", 1);
			}
			else if (message.getStatus() != Constants.ORDER_STATUS_APPROVED)
			{
				nextDate = DateUtil.addDate(new Date(), campaign.getScheduleUnit(), campaign.getSchedulePeriod());
			}
		}

		return nextDate;
	}

	public double getAmount(Connection connection, CampaignMessage message) throws Exception
	{
		return 0;
	}

	public double getScore(Connection connection, CampaignMessage message) throws Exception
	{
		return 0;
	}

	public int processMessage(Connection connection, CampaignEntry campaign, CampaignMessage request) throws Exception
	{
		try
		{
			request.setStatus(Constants.ORDER_STATUS_DENIED);
		}
		catch (Exception e)
		{
			throw e;
		}

		return Constants.BIND_ACTION_SUCCESS;
	}
}