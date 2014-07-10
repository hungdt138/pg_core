package com.crm.product.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.impl.ccws.CCWSConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.subscriber.impl.MGMServiceImpl;
import com.crm.subscriber.impl.SubscriberEntryImpl;

public class MGMOrderRoutingImpl extends OrderRoutingImpl
{
	private final String REFERRAL_EMPTY = "referral-empty";

	private final String REFERRAL_POSTPAID = "referral-postpaid";

	private final String INVALID_SUB = "invalid-sub";

	private final String INTRODUCER_POSTPAID = "introducer-postpaid";

	private final String INTRODUCER_INACTIVE = "introducer-inactive";

	private final String REFERRAL_INACTIVE = "referral-inactive";

	private final String NOT_TOP_UP_30 = "introducer-not-top-up-30";

	private final String NOT_TOP_UP = "introducer-not-top-up";

	private final String NOT_WHITE_LIST = "referral-not-white-list";

	private final String REFERRAL_INUSED = "referral-inused";

	private final String REFERRAL_CiRCLE = "referral-circle";

	private final String MAX_MEMBER = "max-member";

	public CommandMessage registerMGM(OrderRoutingInstance instance,
			ProductRoute orderRoute, CommandMessage request) throws Exception
	{
		super.parser(instance, orderRoute, request);

		ProductEntry product = ProductFactory.getCache().getProduct(
				request.getProductId());

		Calendar obj0106 = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse(product.getParameter("RegisterDay",
				"2011-06-01"));

		String introducer = request.getIsdn();
		obj0106.setTime(date);

		String responseCode = "";

		CCWSConnection connection = null;
		if ((instance.getDebugMode().equals("depend")))
		{
			simulation(instance, orderRoute, request);
		}
		else
		{
			try
			{
				String referral = request.getKeyword().toUpperCase();
	
				referral = referral.substring(referral.indexOf(" "),
						referral.length()).trim();
	
				// Check country code
				if (!referral.equals("")
						&& referral.startsWith(Constants.DOMESTIC_CODE))
				{
					referral = referral.substring(Constants.DOMESTIC_CODE.length());
					referral = Constants.COUNTRY_CODE + referral;
				}
	
				// Check empty referral
				if (referral.equals(""))
				{
					responseCode = REFERRAL_EMPTY;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
	
				// Check referral equal introducer
				if (introducer.equals(referral))
				{
					responseCode = INVALID_SUB;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
				
				// Check A is referral of B
				if (MGMServiceImpl.isIntroducer(introducer, referral))
				{
					responseCode = REFERRAL_CiRCLE;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
	
				int MaxMember = Integer.valueOf(product.getParameter(
						"MaxMember", "10"));
	
				// Check max referral
				int intNumOfCc = MGMServiceImpl.getNumOfCC(introducer);
				if (MaxMember > 0)
				{
					if (intNumOfCc >= MaxMember)
					{
						responseCode = MAX_MEMBER;
						request.setResponse(responseCode);
						request.setResponseValue(ResponseUtil.REFERAL, MaxMember);
						request.setCause(responseCode);
						request.setStatus(Constants.ORDER_STATUS_DENIED);
	
						return request;
					}
				}
	
				// Check referral is postpaid
				int subscriberType = SubscriberEntryImpl.getSubscriberType(
						Database.getConnection(), referral);
				if (subscriberType == Constants.POSTPAID_SUB_TYPE)
				{
					responseCode = REFERRAL_POSTPAID;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
	
				// Check introducer is postpaid
				subscriberType = request.getSubscriberType();
				if (subscriberType == Constants.POSTPAID_SUB_TYPE)
				{
					responseCode = INTRODUCER_POSTPAID;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
	
				// Check new Referral.
				if (!MGMServiceImpl.isNewReferral(referral))
				{
					responseCode = REFERRAL_INUSED;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
				
				connection = (CCWSConnection) instance.getProvisioningConnection();
	
				// Check activate date of referral
				if (!connection.getSubActivateDate(referral, 1).after(obj0106)
						&& !MGMServiceImpl.checkSubInWhiteList(referral))
				{
					responseCode = NOT_WHITE_LIST;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
				
				// Check status of introducer.
				if (!connection.checkSubStatus("Active", introducer, ";")
						.equalsIgnoreCase("Active"))
				{
					responseCode = INTRODUCER_INACTIVE;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
	
				// Check status of referral.
				if (!connection.checkSubStatus("Active", referral, ";")
						.equalsIgnoreCase("Active"))
				{
					responseCode = REFERRAL_INACTIVE;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
				
				// Check top-up introducer
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				int maxIntroduce = Integer.parseInt(product.getParameter(
						"MaxIntroduce", "10"));
				int minRecharge = Integer.parseInt(product.getParameter(
						"MinRecharge", "20000"));
				
				Date lastTopup = MGMServiceImpl.getLastTopup(introducer, minRecharge);
	
				Calendar cal = Calendar.getInstance();
				cal.add(cal.DAY_OF_MONTH, -30);
				
				if (lastTopup == null || !lastTopup.after(cal.getTime()))
				{
					responseCode = NOT_TOP_UP_30;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
				else if (!MGMServiceImpl.checkIntroducer(introducer,
						sdf.format(lastTopup), maxIntroduce))
				{
					responseCode = NOT_TOP_UP;
					request.setResponse(responseCode);
					request.setCause(responseCode);
					request.setStatus(Constants.ORDER_STATUS_DENIED);
	
					return request;
				}
	
				boolean checkAddOneTime = Boolean.parseBoolean(product.getParameter(
						"AddOneTime", "false"));
				if (checkAddOneTime)
				{
					if (MGMServiceImpl.checkSubAddOneTime(introducer))
					{
						request.getParameters().setProperty("AddDaily", "false");
					}
				}
			}
			catch (Exception e)
			{
				responseCode = Constants.ERROR;
				request.setResponse(responseCode);
				request.setCause(responseCode);
				request.setStatus(Constants.ORDER_STATUS_DENIED);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		request.setResponse(responseCode);

		return request;
	}
}
