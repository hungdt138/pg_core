package com.crm.provisioning.impl.ccws;

import java.util.Calendar;
import java.util.Date;

import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.BalanceEntityBase;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;

public class MGMCommandImpl extends CCWSCommandImpl
{
	public CommandMessage setBalanceMGM(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());
	
				SubscriberEntity subscriber = getSubscriber(instance, result);
	
				String addBalances = "";
	
				boolean addDaily = Boolean.parseBoolean(request.getParameters()
						.getProperty("AddDaily", "true"));
	
				if (addDaily)
				{
					addBalances = product.getParameter("BalanceVOICE", "");
				}
				else
				{
					addBalances = product.getParameter("BalanceCURRENCY", "");
				}
	
				Date now = new Date();
	
				balances = new BalanceEntityBase[1];
	
				Calendar activeDate = Calendar.getInstance();
	
				String balanceName = addBalances;
	
				String prefix = "balance." + result.getActionType() + "."
						+ balanceName;
	
				// get current balance
				BalanceEntity balance = CCWSConnection.getBalance(subscriber,
						balanceName);
	
				// calculate expiration date for this balance
				Calendar maxExpiredDate = Calendar.getInstance();
	
				maxExpiredDate.setTime(now);
				maxExpiredDate.set(Calendar.HOUR_OF_DAY, 23);
				maxExpiredDate.set(Calendar.MINUTE, 59);
				maxExpiredDate.set(Calendar.SECOND, 59);
	
				int maxDays = product.getParameters().getInteger(
						prefix + ".maxDays");
	
				maxExpiredDate.add(Calendar.DATE, maxDays);
	
				Calendar expiredDate = balance.getAccountExpiration();
				int expireTime = product.getParameters().getInteger(
						prefix + ".days");
	
				String setExpFlg = product.getParameters().getString(
						prefix + ".resetexpiredate", "false");
	
				if (expiredDate.getTimeInMillis() < now.getTime()
						|| setExpFlg.equals("true"))
				{
					expireTime--;
					expiredDate.setTime(now);
				}
	
				expiredDate.set(Calendar.HOUR_OF_DAY, 23);
				expiredDate.set(Calendar.MINUTE, 59);
				expiredDate.set(Calendar.SECOND, 59);
	
				if (expireTime > 0)
				{
					expiredDate.add(Calendar.DATE, expireTime);
				}
	
				if ((maxDays > 0) && expiredDate.after(maxExpiredDate))
				{
					expiredDate = maxExpiredDate;
				}
	
				if ((maxDays > 0) && expiredDate.after(activeDate))
				{
					activeDate = expiredDate;
				}
	
				double amount = product.getParameters().getDouble(
						prefix + ".amount");
	
				balances[0] = new BalanceEntityBase();
	
				balances[0].setBalanceName(balanceName);
	
				String accumulateFlg = product.getParameters().getString(
						prefix + ".accumulate", "false");
				if (accumulateFlg.equals("true"))
				{
					balances[0].setBalance(balance.getBalance() + amount);
				}
				else
				{
					balances[0].setBalance(amount);
				}
	
				balances[0].setAccountExpiration(expiredDate);
	
				String comment = product.getIndexKey();
	
				// modify balances
				if (balances.length > 0)
				{
					connection = (CCWSConnection) instance
							.getProvisioningConnection();
	
					connection.setBalance(isdn, balances, activeDate, comment);
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}

	public CommandMessage setBalanceDaily(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CCWSConnection connection = null;

		BalanceEntityBase[] balances = null;

		String isdn = CommandUtil.addCountryCode(request.getIsdn());

		VNMMessage result = CommandUtil.createVNMMessage(request);

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			try
			{
				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());
	
				SubscriberEntity subscriber = getSubscriber(instance, result);
	
				String addBalances = product.getParameter("BalanceVOICE", "");
	
				Date now = new Date();
	
				balances = new BalanceEntityBase[1];
	
				Calendar activeDate = Calendar.getInstance();
	
				String balanceName = addBalances;
	
				String prefix = "balance." + result.getActionType() + "."
						+ balanceName;
	
				// get current balance
				BalanceEntity balance = CCWSConnection.getBalance(subscriber,
						balanceName);
	
				// calculate expiration date for this balance
				Calendar maxExpiredDate = Calendar.getInstance();
	
				maxExpiredDate.setTime(now);
				maxExpiredDate.set(Calendar.HOUR_OF_DAY, 23);
				maxExpiredDate.set(Calendar.MINUTE, 59);
				maxExpiredDate.set(Calendar.SECOND, 59);
	
				int maxDays = product.getParameters().getInteger(
						prefix + ".maxDays");
	
				maxExpiredDate.add(Calendar.DATE, maxDays);
	
				Calendar expiredDate = balance.getAccountExpiration();
				int expireTime = product.getParameters().getInteger(
						prefix + ".days");
	
				String setExpFlg = product.getParameters().getString(
						prefix + ".resetexpiredate", "false");
	
				if (expiredDate.getTimeInMillis() < now.getTime()
						|| setExpFlg.equals("true"))
				{
					expireTime--;
					expiredDate.setTime(now);
				}
	
				expiredDate.set(Calendar.HOUR_OF_DAY, 23);
				expiredDate.set(Calendar.MINUTE, 59);
				expiredDate.set(Calendar.SECOND, 59);
	
				if (expireTime > 0)
				{
					expiredDate.add(Calendar.DATE, expireTime);
				}
	
				if ((maxDays > 0) && expiredDate.after(maxExpiredDate))
				{
					expiredDate = maxExpiredDate;
				}
	
				if ((maxDays > 0) && expiredDate.after(activeDate))
				{
					activeDate = expiredDate;
				}
	
				int totalMember = Integer.parseInt(request.getParameters()
						.getProperty("TotalMember", "1"));
				double amount = totalMember
						* product.getParameters().getDouble(prefix + ".amount");
	
				balances[0] = new BalanceEntityBase();
	
				balances[0].setBalanceName(balanceName);
	
				String accumulateFlg = product.getParameters().getString(
						prefix + ".accumulate", "false");
	
				String addFlag = request.getParameters().getProperty("AddFlag",
						"false");
				if (accumulateFlg.equals("true") || addFlag.equals("true"))
				{
					balances[0].setBalance(balance.getBalance() + amount);
				}
				else
				{
					balances[0].setBalance(amount);
				}
	
				balances[0].setAccountExpiration(expiredDate);
	
				String comment = product.getIndexKey();
	
				// modify balances
				if (balances.length > 0)
				{
					connection = (CCWSConnection) instance
							.getProvisioningConnection();
	
					connection.setBalance(isdn, balances, activeDate, comment);
				}
			}
			catch (Exception error)
			{
				processError(instance, provisioningCommand, result, error);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}
}
