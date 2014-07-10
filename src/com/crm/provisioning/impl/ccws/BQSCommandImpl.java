/**
 * 
 */
package com.crm.provisioning.impl.ccws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.comverse_in.prepaid.ccws.SubscriberPB;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.util.AppProperties;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;
import com.fss.util.AppException;

/**
 * @author ThangPV
 */
public class BQSCommandImpl extends CCWSCommandImpl
{
	/**
	 * Modified by NamTA<br>
	 * Modified Date 12/09/2012
	 * 
	 * @param config
	 * @param cos
	 * @param balance
	 * @param keyword
	 * @param nullValue
	 * @return
	 * @throws Exception
	 */
	public String getProperty(
			AppProperties config, String cos, String balance, String keyword, String nullValue) throws Exception
	{
		try
		{
			String name = cos + (balance.equals("") ? "" : "." + balance)
						+ (keyword.equals("") ? "" : "." + keyword);

			while (name.startsWith("."))
				name = name.substring(1);

			if (name.equals(""))
				return nullValue;

			String value = config.getProperty(name);

			if (value == null && (!cos.equals("DEFAULT")) && (!cos.equals("")))
			{
				String cosGroupName = config.getProperty(cos + ".configGroup" + ".name", "");

				if (!cosGroupName.equals(""))
				{
					value = getProperty(config, cosGroupName,balance,keyword, null);
				}
			}

			if (value == null && (!cos.equals("DEFAULT")) && (!cos.equals("")))
			{
				value = getProperty(config, "DEFAULT", balance, keyword, null);
			}

			if (value == null && (!cos.equals("DEFAULT")) && (!cos.equals("")))
			{
				value = getProperty(config, "", balance, keyword, null);
			}

			if (value == null && (!balance.equals("")))
			{
				value = getProperty(config, "", "", keyword, null);
			}

			return StringUtil.nvl(value, nullValue);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	/**
	 * Edited by NamTA, convert balance amount.
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CommandMessage getBalanceQuery(
			CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		SubscriberEntity subscriber = null;

		try
		{
			subscriber = getSubscriber(instance, request);

			// format balance results
			if (subscriber == null)
			{
				throw new AppException(Constants.ERROR_SUBSCRIBER_NOT_FOUND);
			}
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		if (request.getStatus() == Constants.ORDER_STATUS_DENIED)
		{
			return request;
		}

		String content = "";

		try
		{
			ProductRoute productRoute = ProductFactory.getCache().getProductRoute(request.getRouteId());

			AppProperties config = productRoute.getParameters();

			content = config.getString("header", "");

			String expireLabel = config.getString("label.expire", "het han");

			// get list balance
			BalanceEntity[] balances = subscriber.getBalances().getBalance();
			ArrayList<String> accounts = new ArrayList<String>();
			// get SMS content
			//String defaultUnit = getProperty(config, "", "", "unit", "d");


			String cosName = subscriber.getCOSName();

			String accountNames = getProperty(config, cosName, "", "balances", "");

			if (accountNames.equals(""))
			{
				for (int j = 0; j <= balances.length - 1; j++)
				{
					accounts.add(balances[j].getBalanceName());
				}
			}
			else
			{
				accounts = StringUtil.toList(accountNames, StringPool.COMMA);
			}
			for (int j = 0; j < accounts.size(); j++)
			{
				BalanceEntity balance = null;

				try
				{
					balance = CCWSConnection.getBalance(subscriber, accounts.get(j));
				}
				catch (AppException e)
				{
					continue;
				}
				String balanceName = balance.getBalanceName();

				String displayName = getProperty(config, cosName, balanceName, "name", "");

				boolean display = getProperty(config, cosName, balanceName, "display", "true").equals("true");

				boolean alwayDisplay = getProperty(config, cosName, balanceName, "alwayDisplay", "false").equals("true");

				if (!displayName.equals("") && display && ((balance.getBalance() > 0) || alwayDisplay))
				{
					Date date = balance.getAccountExpiration().getTime();

					String unit = getProperty(config, "", balanceName, "unit", "d");
					boolean spaceBeforeUnit = getProperty(config, cosName, balanceName, "spaceBeforeUnit", "false")
							.toUpperCase()
							.equals("TRUE");

					// Convert between different values.
					double convertRatio = 1;
					String strConvertRatio = getProperty(config, cosName, balanceName, "convertRatio", "1");

					try
					{
						convertRatio = Double.parseDouble(strConvertRatio);
					}
					catch (Exception cve)
					{
						instance.getDispatcher().debugMonitor(
								"Can not convert convertRatio=" + strConvertRatio
										+ " from string to double, please reconfig product route properties, use default=1");
					}

					double balanceAmount = balance.getBalance();
					if (convertRatio != 1)
						balanceAmount = balanceAmount * convertRatio;

					String amount = StringUtil.format(balanceAmount, "#,##0")
								+ (spaceBeforeUnit ? " " : "")
								+ unit;

					content += (displayName + ": " + amount);

					if (getProperty(config, cosName, balanceName, "expireDate", "false").equals("true"))
					{
						content += (", " + expireLabel + " " + StringUtil.format(date, "dd-MM-yyyy") + ". ");
					}
					else
					{
						content += ". ";
					}
				}
			}

			if (content.equals(""))
			{
				content = config.getString("response.emptyBalance", "");
			}

			// debug
			instance.debugMonitor("SMS Content: " + content);

			// send SMS to subscriber
			if (!content.equals(""))
			{
				CommandUtil.sendSMS(instance, request, content);

				// instance.debugMonitor(request.getIsdn() + ": " + content);
			}
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}

		return request;
	}

	/**
	 * Retrieve subscriber: Balances, PhoneBook, add to CommandMessage response
	 * properties<br>
	 * Created Date: 27/06/2012<br>
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 * 
	 * @author Nam
	 */
	public CommandMessage retrieveSubscriber(
			CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		SubscriberRetrieve subscriber = null;

		try
		{
			connection = (CCWSConnection) instance.getProvisioningConnection();

			subscriber = getSubscriberInfo(instance, request);

			// format balance results
			if (subscriber == null)
			{
				throw new AppException(Constants.ERROR_SUBSCRIBER_NOT_FOUND);
			}
		}
		catch (Exception error)
		{
			processError(instance, provisioningCommand, request, error);
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}

		if (request.getStatus() == Constants.ORDER_STATUS_DENIED)
		{
			return request;
		}

		request.setResponseValue(ResponseUtil.COS_CURRENT, subscriber.getSubscriberData().getCOSName());
		request.setResponseValue(ResponseUtil.ACCOUNT_STATE, subscriber.getSubscriberData().getCurrentState());

		// get list balance
		BalanceEntity[] balances = subscriber.getSubscriberData().getBalances().getBalance();
		String balancesList = "";
		for (int i = 0; i < balances.length; i++)
		{
			String name = balances[i].getBalanceName();
			balancesList += name + StringPool.COMMA;
			double amount = balances[i].getBalance();
			Date expirationDate = balances[i].getAccountExpiration().getTime();
			request.setResponseValue(ResponseUtil.BALANCES + "." + name + ".amount", StringUtil.format(amount, "#"));
			request.setResponseValue(ResponseUtil.BALANCES + "." + name + ".expireDate",
					StringUtil.format(expirationDate, "dd/MM/yyyy HH:mm:ss"));
		}
		request.setResponseValue(ResponseUtil.BALANCES, balancesList);

		SubscriberPB subscriberPB = subscriber.getSubscriberPhoneBook();
		String phoneBookList = subscriberPB.getDestNumber1() + StringPool.COMMA +
					subscriberPB.getDestNumber2() + StringPool.COMMA +
					subscriberPB.getDestNumber3() + StringPool.COMMA +
					subscriberPB.getDestNumber4() + StringPool.COMMA +
					subscriberPB.getDestNumber5() + StringPool.COMMA +
					subscriberPB.getDestNumber6() + StringPool.COMMA +
					subscriberPB.getDestNumber7() + StringPool.COMMA +
					subscriberPB.getDestNumber8() + StringPool.COMMA +
					subscriberPB.getDestNumber9() + StringPool.COMMA +
					subscriberPB.getDestNumber10();

		request.setResponseValue(ResponseUtil.PHONEBOOK, phoneBookList);
		return request;
	}

	/**
	 * Get SubscriberInfo: Balances, PhoneBook<br>
	 * Created Date: 27/06/2012<br>
	 * 
	 * @param instance
	 * @param request
	 * @return
	 * @throws Exception
	 * 
	 * @author Nam
	 */
	public SubscriberRetrieve getSubscriberInfo(CommandInstance instance, CommandMessage request) throws Exception
	{
		CCWSConnection connection = null;

		SubscriberRetrieve subscriber = null;

		try
		{
			String isdn = CommandUtil.addCountryCode(request.getIsdn());

			connection = (CCWSConnection) instance.getProvisioningConnection();

			subscriber = connection.getSubscriber(isdn, 1 + 8);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}
		return subscriber;
	}
}
