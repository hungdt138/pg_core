package com.crm.merchant.cache;

import java.util.Date;

import com.crm.kernel.index.IndexNode;

public class MerchantAgent extends IndexNode
{
	private long 			merchantId 		= 0;
	private long 			agentId 		= 0;
	private String			alias			= "";
	private String 			type	 		= "";
	private String			name	 		= "";
	private String			jobTitle 		= "";
	
	private String			isdn 			= "";
	private Date			startDate		= null;
	private Date			endDate		 	= null;
	
	private int				status 			= 0;
	
	public MerchantAgent(long agentId, String alias){
		super(alias);
		
		setAgentId(agentId);
	}
	
	public long getMerchantId()
	{
		return merchantId;
	}


	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
	}


	public long getAgentId()
	{
		return agentId;
	}


	public void setAgentId(long agentId)
	{
		this.agentId = agentId;
	}


	public String getAlias()
	{
		return alias;
	}


	public void setAlias(String alias)
	{
		this.alias = alias;
	}


	public String getType()
	{
		return type;
	}


	public void setType(String type)
	{
		this.type = type;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public String getJobTitle()
	{
		return jobTitle;
	}


	public void setJobTitle(String jobTitle)
	{
		this.jobTitle = jobTitle;
	}


	public String getIsdn()
	{
		return isdn;
	}


	public void setIsdn(String isdn)
	{
		this.isdn = isdn;
	}


	public Date getStartDate()
	{
		return startDate;
	}


	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}


	public Date getEndDate()
	{
		return endDate;
	}


	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}


	public int getStatus()
	{
		return status;
	}


	public void setStatus(int status)
	{
		this.status = status;
	}
}
