/**
 * 
 */
package com.crm.marketing.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.crm.kernel.index.BaseMessage;
import com.crm.kernel.message.Constants;

/**
 * @author ThangPV
 * 
 */
public class CampaignMessage extends BaseMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private Date				requestTime			= new Date();
	private Date				responseTime		= null;

	private long				orderId				= 0;
	private Date				orderDate			= new Date();
	private Date				cycleDate			= new Date();

	private long				companyId			= 0;
	private long				groupId				= 0;
	private long				userId				= 0;
	private String				userName			= "";

	private long				subscriberId		= 0;
	private long				subPackageId		= 0;
	private long				subProductId		= 0;
	private long				subCampaignId		= 0;
	private String				isdn				= "";
	private int					subscriberType		= 1;
	private Date				registerDate		= null;
	private Date				activeDate			= null;

	private long				segmentId			= 0;
	private long				campaignId			= 0;
	private long				rankId				= 0;

	private long				productId			= 0;
	private double				offerPrice			= 0;
	private double				price				= 0;
	private int					quantity			= 0;
	private double				discount			= 0;
	private double				amount				= 0;
	private double				score				= 0;

	private Date				nextRunDate			= null;
	private int					status				= Constants.ORDER_STATUS_PENDING;
	private String				cause				= "";
	private String				description			= "";

	public CampaignMessage clone()
	{
		CampaignMessage result = new CampaignMessage();

		result.setOrderId(orderId);
		result.setOrderDate(orderDate);
		result.setCycleDate(cycleDate);

		result.setCompanyId(companyId);
		result.setGroupId(groupId);
		result.setUserId(userId);
		result.setUserName(userName);

		result.setSubscriberId(subscriberId);
		result.setSubPackageId(subPackageId);
		result.setSubProductId(subProductId);

		result.setSegmentId(segmentId);
		result.setCampaignId(campaignId);
		result.setRankId(rankId);

		result.setIsdn(isdn);
		result.setSubscriberType(subscriberType);

		result.setProductId(productId);
		result.setOfferPrice(offerPrice);
		result.setPrice(price);
		result.setQuantity(quantity);
		result.setDiscount(discount);
		result.setAmount(amount);
		result.setScore(score);

		result.setStatus(status);
		result.setCause(cause);
		result.setDescription(description);

		return result;
	}

	public String toString()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		StringBuffer result = new StringBuffer();

		result.append("orderId = ");
		result.append(orderId);
		result.append(" | orderDate = ");
		result.append(dateFormat.format(orderDate));
		result.append(" | cycleDate = ");
		result.append(dateFormat.format(cycleDate));

		result.append(" | companyId = ");
		result.append(companyId);
		result.append(" | groupId = ");
		result.append(groupId);
		result.append(" | userId = ");
		result.append(userName);

		result.append(" | subscriberId = ");
		result.append(subscriberId);
		result.append(" | subPackageId = ");
		result.append(subPackageId);
		result.append(" | subProductId = ");
		result.append(subProductId);

		result.append(" | segmentId = ");
		result.append(segmentId);
		result.append(" | campaignId = ");
		result.append(campaignId);

		result.append(" | sourceAddress = ");
		result.append(isdn);
		result.append(" | subscriberType = ");
		result.append(subscriberType);

		result.append(" | productId = ");
		result.append(" | offerPrice = ");
		result.append(offerPrice);
		result.append(" | price = ");
		result.append(price);
		result.append(" | quantity = ");
		result.append(quantity);
		result.append(" | discount = ");
		result.append(discount);
		result.append(" | amount = ");
		result.append(amount);
		result.append(" | score = ");
		result.append(score);

		result.append(" | status = ");
		result.append(status);
		result.append(" | cause = ");
		result.append(cause);
		result.append(" | description = ");
		result.append(description);

		return result.toString();

	}

	public boolean isExisted()
	{
		return subProductId == 0;
	}

	public boolean isPrepaid()
	{
		return subscriberType == Constants.PREPAID_SUB_TYPE;
	}

	public boolean isPostpaid()
	{
		return subscriberType == Constants.POSTPAID_SUB_TYPE;
	}

	public void setCommandId(String alias) throws Exception
	{

	}

	public long getOrderId()
	{
		return orderId;
	}

	public void setOrderId(long orderId)
	{
		this.orderId = orderId;
	}

	public Date getOrderDate()
	{
		return orderDate;
	}

	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}

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

	public long getSubPackageId()
	{
		return subPackageId;
	}

	public void setSubPackageId(long subPackageId)
	{
		this.subPackageId = subPackageId;
	}

	public long getSubProductId()
	{
		return subProductId;
	}

	public void setSubProductId(long subProductId)
	{
		this.subProductId = subProductId;
	}

	public String getIsdn()
	{
		return isdn;
	}

	public void setIsdn(String isdn)
	{
		this.isdn = isdn;
	}

	public long getSegmentId()
	{
		return segmentId;
	}

	public void setSegmentId(long segmentId)
	{
		this.segmentId = segmentId;
	}

	public long getCampaignId()
	{
		return campaignId;
	}

	public void setCampaignId(long campaignId)
	{
		this.campaignId = campaignId;
	}

	public int getSubscriberType()
	{
		return subscriberType;
	}

	public void setSubscriberType(int subscriberType)
	{
		this.subscriberType = subscriberType;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public double getOfferPrice()
	{
		return offerPrice;
	}

	public void setOfferPrice(double offerPrice)
	{
		this.offerPrice = offerPrice;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	public double getDiscount()
	{
		return discount;
	}

	public void setDiscount(double discount)
	{
		this.discount = discount;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public double getScore()
	{
		return score;
	}

	public void setScore(double score)
	{
		this.score = score;
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

	public Date getRequestTime()
	{
		return requestTime;
	}

	public void setRequestTime(Date requestTime)
	{
		this.requestTime = requestTime;
	}

	public Date getResponseTime()
	{
		return responseTime;
	}

	public void setResponseTime(Date responseTime)
	{
		this.responseTime = responseTime;
	}

	public Date getCycleDate()
	{
		return cycleDate;
	}

	public void setCycleDate(Date cycleDate)
	{
		this.cycleDate = cycleDate;
	}

	public long getRankId()
	{
		return rankId;
	}

	public void setRankId(long rankId)
	{
		this.rankId = rankId;
	}

	public Date getNextRunDate()
	{
		return nextRunDate;
	}

	public void setNextRunDate(Date nextRunDate)
	{
		this.nextRunDate = nextRunDate;
	}

	public long getSubCampaignId()
	{
		return subCampaignId;
	}

	public void setSubCampaignId(long subCampaignId)
	{
		this.subCampaignId = subCampaignId;
	}

	public Date getActiveDate()
	{
		return activeDate;
	}

	public void setActiveDate(Date activeDate)
	{
		this.activeDate = activeDate;
	}

	public Date getRegisterDate()
	{
		return registerDate;
	}

	public void setRegisterDate(Date registerDate)
	{
		this.registerDate = registerDate;
	}
}
