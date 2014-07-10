package com.crm.provisioning.thread;

import java.util.Calendar;

import com.crm.util.StringUtil;

public class ProductStatistic
{
	private long		productId	= 0;
	private String		alias		= "";
	private int			failure		= 0;
	private int			success		= 0;
	private Calendar	startTime	= Calendar.getInstance();

	public int getFailure()
	{
		return failure;
	}

	public void setFailure(int failure)
	{
		this.failure = failure;
	}

	public int getSuccess()
	{
		return success;
	}

	public void setSuccess(int success)
	{
		this.success = success;
	}

	public void setStartTime(Calendar startTime)
	{
		this.startTime = startTime;
	}

	public Calendar getStartTime()
	{
		return startTime;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String toString()
	{
		return "ID=" + productId + ", ALIAS=" + alias + ", SUCCESS="
				+ success + ", FAILURE=" + failure + ", STARTTIME="
				+ StringUtil.format(startTime.getTime(), "dd/MM/yyyy HH:mm:ss") + ".";
	}
}
