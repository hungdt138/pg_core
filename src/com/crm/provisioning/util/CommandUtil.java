/**
 * 
 */
package com.crm.provisioning.util;

import java.util.Calendar;
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.CommandEntry;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.cache.ProvisioningPool;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.thread.DispatcherInstance;
import com.crm.util.StringUtil;

/**
 * @author ThangPV
 * 
 */
public class CommandUtil
{
	public static String addCountryCode(String isdn)
	{
		isdn = StringUtil.nvl(isdn, "");

		if (isdn.equals(""))
		{
			return isdn;
		}

		if (isdn.startsWith(Constants.DOMESTIC_CODE))
		{
			isdn = Constants.COUNTRY_CODE + isdn.substring(Constants.DOMESTIC_CODE.length());
		}
		else if (!isdn.startsWith(Constants.COUNTRY_CODE))
		{
			isdn = Constants.COUNTRY_CODE + isdn;
		}

		return isdn;
	}

	public static String addDomesticCode(String isdn)
	{
		isdn = StringUtil.nvl(isdn, "");

		if (isdn.equals(""))
		{
			return isdn;
		}

		if (!isdn.startsWith(Constants.DOMESTIC_CODE))
		{
			if (isdn.startsWith(Constants.COUNTRY_CODE))
			{
				isdn = Constants.DOMESTIC_CODE + isdn.substring(Constants.COUNTRY_CODE.length());
			}
			else
			{
				isdn = Constants.DOMESTIC_CODE + isdn;
			}

		}

		return isdn;
	}

	public static boolean isPostpaid(int subscriberType)
	{
		return subscriberType == Constants.POSTPAID_SUB_TYPE;
	}

	public static boolean isPrepaid(int subscriberType)
	{
		return subscriberType == Constants.PREPAID_SUB_TYPE;
	}

	public static String getCause(CommandMessage request)
	{
		if (request != null)
		{
			String cause = request.getCause();

			if (cause.equals(""))
			{
				cause = (request.getStatus() == Constants.ORDER_STATUS_DENIED) ? Constants.ERROR : Constants.SUCCESS;
			}

			return cause;
		}
		else
		{
			return Constants.SUCCESS;
		}
	}

	public static boolean isTimeout(Date requestTime, int timeout) throws Exception
	{
		Calendar calendar = Calendar.getInstance();

		try
		{
			calendar.setTime(requestTime);

			if (timeout > 0)
			{
				calendar.add(Calendar.SECOND, timeout);

				return calendar.getTime().before(new Date());
			}

			return false;
		}
		catch (Exception e)
		{
			return true;
		}
	}

	public static boolean isTimeout(CommandMessage request, int timeout) throws Exception
	{
		return isTimeout(request.getOrderDate(), timeout);
	}

	public static int getAmount(double amount)
	{
		return (int) amount;
	}

	public static VNMMessage createVNMMessage(CommandMessage request)
	{
		VNMMessage result = null;

		if (request instanceof VNMMessage)
		{
			result = (VNMMessage) request;
		}
		else
		{
			result = new VNMMessage();

			request.copyTo(result);
		}

		return result;
	}

	public void closeConnection(ProvisioningConnection connection)
	{
		try
		{
			if (connection != null)
			{
				connection.closeConnection();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void closeConnection(ProvisioningPool pool)
	{
		try
		{
			if (pool != null)
			{
				pool.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void processError(CommandInstance instance, CommandMessage request, String errorCode)
	{
		if (!errorCode.equals(""))
		{
			request.setStatus(Constants.ORDER_STATUS_DENIED);
			request.setCause(errorCode);
		}
	}

	public static long calculateCostTime(Date startTime, Date endTime)
	{
		long timeCost = (endTime != null ? endTime.getTime() : 0) - (startTime != null ? startTime.getTime() : 0);

//		if (timeCost < Constants.COST_LOW_VALUE)
//			return Constants.COST_LOW;
//		else if (timeCost > Constants.COST_HIGH_VALUE)
//			return Constants.COST_HIGH;
//		else
//			return Constants.COST_MEDIUM;
		return timeCost;
	}

	public static CommandMessage sendSMS(
			DispatcherInstance instance, CommandMessage request, String serviceAddress, String shippingTo, String content)
			throws Exception
	{
//		if (request.getChannel().equals(Constants.CHANNEL_WEB))
//		{
//			return null;
//		}

		if (content.equals(""))
		{
			instance.debugMonitor("ignore empty message ");

			return null;
		}

		CommandMessage requestMO = null;

		try
		{
			requestMO = request.clone();

			// provisioning & command
			CommandEntry command = ProvisioningFactory.getCache().getCommand(Constants.COMMAND_SEND_SMS);

			requestMO.setProvisioningType(Constants.PROVISIONING_SMSC);
			requestMO.setCommandId(command.getCommandId());

			// sender & receiver
			if (serviceAddress.equals(""))
			{
				ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());

				if (product != null)
				{
					serviceAddress = product.getParameter("serviceAddress", request.getServiceAddress());
				}
				else
				{
					serviceAddress = request.getServiceAddress();
				}
			}

			requestMO.setServiceAddress(serviceAddress);

			if (!shippingTo.equals(""))
			{
				requestMO.setIsdn(shippingTo);
			}

			requestMO.setRequest(content);

			requestMO.setRequestValue(ResponseUtil.SMS_CMD_CHECK, "false");
			
//			MQConnection connection = null;
//			try
//			{
//				connection = instance.getMQConnection();
//				connection.sendMessage(requestMO, QueueFactory.COMMAND_ROUTE_QUEUE, 0);
//			}
//			finally
//			{
//				instance.returnMQConnection(connection);
//			}
			
			QueueFactory.attachCommandRouting(requestMO);

		}
		catch (Exception e)
		{
			throw e;
		}

		return requestMO;
	}

	public static void sendSMS(DispatcherInstance instance, CommandMessage request, String content) throws Exception
	{
		sendSMS(instance, request, "", "", content);
	}
	
	public static boolean validateAmount(double amount)
	{
		boolean success = false;
		if(amount >= 5000 && amount % 1000 == 0 && amount <= 50000){
			success = true;
		}
		return success;
	}
}
