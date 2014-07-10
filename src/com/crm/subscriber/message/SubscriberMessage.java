/**
 * 
 */
package com.crm.subscriber.message;

import java.io.Serializable;
import java.util.Date;

import com.crm.kernel.index.BaseMessage;
import com.crm.kernel.message.Constants;

/**
 * @author ThangPV
 * 
 */
public class SubscriberMessage extends BaseMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private long				companyId			= 0;
	private long				groupId				= 0;
	private long				userId				= 0;
	private String				userName			= "system";

	private long				requestId			= 0;

	private long				subscriberId		= 0;
	private int					subscriberType		= 1;
	private String				isdn				= "";
	private long				productId			= 0;
	private int					barringStatus		= 0;
	private int					supplierStatus		= 0;
	private int					networkStatus		= 0;
	private Date				birthDate			= null;

	private String				orderType			= "";
	private Date				orderDate			= null;
	private String				SKU					= "";

	private Date				cycleDate			= null;
	private Date				nextCycleDate		= null;
	private Date				registerDate		= null;
	private Date				unregisterDate		= null;
	private Date				activeDate			= null;
	private Date				barringDate			= null;

	private long				campaignId			= 0;
	private long				rankId				= 0;
	private Date				rankStartDate		= null;
	private Date				rankExpirationDate	= null;

	private double				amount				= 0;
	private double				balanceAmount		= 0;
	private double				score				= 0;

	private int					status				= Constants.ORDER_STATUS_PENDING;
	private String				cause				= "";
	private String				description			= "";

	public long getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(long companyId)
	{
		this.companyId = companyId;
	}

	public long getGroupId()
	{
		return groupId;
	}

	public void setGroupId(long groupId)
	{
		this.groupId = groupId;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public long getSubscriberId()
	{
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId)
	{
		this.subscriberId = subscriberId;
	}

	public int getSubscriberType()
	{
		return subscriberType;
	}

	public void setSubscriberType(int subscriberType)
	{
		this.subscriberType = subscriberType;
	}

	public String getIsdn()
	{
		return isdn;
	}

	public void setIsdn(String isdn)
	{
		this.isdn = isdn;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public int getBarringStatus()
	{
		return barringStatus;
	}

	public void setBarringStatus(int barringStatus)
	{
		this.barringStatus = barringStatus;
	}

	public int getSupplierStatus()
	{
		return supplierStatus;
	}

	public void setSupplierStatus(int supplierStatus)
	{
		this.supplierStatus = supplierStatus;
	}

	public int getNetworkStatus()
	{
		return networkStatus;
	}

	public void setNetworkStatus(int networkStatus)
	{
		this.networkStatus = networkStatus;
	}

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate(Date birthDate)
	{
		this.birthDate = birthDate;
	}

	public Date getCycleDate()
	{
		return cycleDate;
	}

	public void setCycleDate(Date cycleDate)
	{
		this.cycleDate = cycleDate;
	}

	public Date getRegisterDate()
	{
		return registerDate;
	}

	public void setRegisterDate(Date registerDate)
	{
		this.registerDate = registerDate;
	}

	public Date getUnregisterDate()
	{
		return unregisterDate;
	}

	public void setUnregisterDate(Date unregisterDate)
	{
		this.unregisterDate = unregisterDate;
	}

	public Date getActiveDate()
	{
		return activeDate;
	}

	public void setActiveDate(Date activeDate)
	{
		this.activeDate = activeDate;
	}

	public Date getBarringDate()
	{
		return barringDate;
	}

	public void setBarringDate(Date barringDate)
	{
		this.barringDate = barringDate;
	}

	public long getCampaignId()
	{
		return campaignId;
	}

	public void setCampaignId(long campaignId)
	{
		this.campaignId = campaignId;
	}

	public long getRankId()
	{
		return rankId;
	}

	public void setRankId(long rankId)
	{
		this.rankId = rankId;
	}

	public Date getRankStartDate()
	{
		return rankStartDate;
	}

	public void setRankStartDate(Date rankStartDate)
	{
		this.rankStartDate = rankStartDate;
	}

	public Date getRankExpirationDate()
	{
		return rankExpirationDate;
	}

	public void setRankExpirationDate(Date rankExpirationDate)
	{
		this.rankExpirationDate = rankExpirationDate;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double usageAmount)
	{
		this.amount = usageAmount;
	}

	public double getScore()
	{
		return score;
	}

	public void setScore(double scoreUsage)
	{
		this.score = scoreUsage;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getCause()
	{
		return cause;
	}

	public void setCause(String cause)
	{
		this.cause = cause;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getNextCycleDate()
	{
		return nextCycleDate;
	}

	public void setNextCycleDate(Date nextCycleDate)
	{
		this.nextCycleDate = nextCycleDate;
	}

	public double getBalanceAmount()
	{
		return balanceAmount;
	}

	public void setBalanceAmount(double balanceAmount)
	{
		this.balanceAmount = balanceAmount;
	}

	public Date getOrderDate()
	{
		return orderDate;
	}

	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}

	public long getRequestId()
	{
		return requestId;
	}

	public void setRequestId(long requestId)
	{
		this.requestId = requestId;
	}

	public String getOrderType()
	{
		return orderType;
	}

	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}

	public String getSKU()
	{
		return SKU;
	}

	public void setSKU(String sKU)
	{
		SKU = sKU;
	}
}
