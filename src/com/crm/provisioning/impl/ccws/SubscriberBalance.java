package com.crm.provisioning.impl.ccws;

import java.util.Calendar;

public class SubscriberBalance
{
	private double balance;
	private Calendar accountExpiration;
	private String balanceName;
	private double nextMaximumSpendingLimit;
	private double totalSpendingLimit;
	private double availableBalance;
	private double maximumSpendingLimit;
	private double availableSpendingLimit;
	private int precisionPoint;
	private boolean exclusiveBalance;
	
	public double getAvailableBalance()
	{
		return this.availableBalance;
	}

	public void setAvailableBalance(double availableBalance)
	{
		this.availableBalance = availableBalance;
	}

	public double getMaximumSpendingLimit()
	{
		return this.maximumSpendingLimit;
	}

	public void setMaximumSpendingLimit(double maximumSpendingLimit)
	{
		this.maximumSpendingLimit = maximumSpendingLimit;
	}

	public double getAvailableSpendingLimit()
	{
		return this.availableSpendingLimit;
	}

	public void setAvailableSpendingLimit(double availableSpendingLimit)
	{
		this.availableSpendingLimit = availableSpendingLimit;
	}

	public int getPrecisionPoint()
	{
		return this.precisionPoint;
	}

	public void setPrecisionPoint(int precisionPoint)
	{
		this.precisionPoint = precisionPoint;
	}

	public boolean isExclusiveBalance()
	{
		return this.exclusiveBalance;
	}

	public void setExclusiveBalance(boolean exclusiveBalance)
	{
		this.exclusiveBalance = exclusiveBalance;
	}
	
	public double getBalance()
	{
		return this.balance;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

	public Calendar getAccountExpiration()
	{
		return this.accountExpiration;
	}

	public void setAccountExpiration(Calendar accountExpiration)
	{
		this.accountExpiration = accountExpiration;
	}

	public String getBalanceName()
	{
		return this.balanceName;
	}

	public void setBalanceName(String balanceName)
	{
		this.balanceName = balanceName;
	}

	public double getNextMaximumSpendingLimit()
	{
		return this.nextMaximumSpendingLimit;
	}

	public void setNextMaximumSpendingLimit(double nextMaximumSpendingLimit)
	{
		this.nextMaximumSpendingLimit = nextMaximumSpendingLimit;
	}

	public double getTotalSpendingLimit()
	{
		return this.totalSpendingLimit;
	}

	public void setTotalSpendingLimit(double totalSpendingLimit)
	{
		this.totalSpendingLimit = totalSpendingLimit;
	}
}
