/**
 * 
 */
package com.crm.subscriber.bean;

import java.io.Serializable;
import java.util.Date;

import com.crm.kernel.message.Constants;

/**
 * @author ThangPV
 * 
 */
public class SubscriberOrder implements Serializable
{
	// PK fields -->

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4947692391004700878L;

	private long				orderId				= Constants.DEFAULT_ID;

	// Audit fields -->

	private long				userId				= Constants.DEFAULT_ID;
	private String				userName			= "";
	private Date				createDate			= null;
	private Date				modifiedDate		= null;

	// Other fields -->

	private long				merchantId			= Constants.DEFAULT_ID;
	private String				orderType			= "";
	private Date				orderDate			= null;
	private String				orderNo				= "";
	private Date				cycleDate			= null;

	private long				subscriberId		= Constants.DEFAULT_ID;
	private long				subProductId		= Constants.DEFAULT_ID;
	private int					subscriberType		= Constants.PREPAID_SUB_TYPE;
	private String				isdn				= "";
	private String				shipTo				= "";
	private long				productId			= Constants.DEFAULT_ID;

	private double				offerPrice			= 0;
	private double				price				= 0;
	private int					quantity			= 1;
	private double				discount			= 0;
	private double				amount				= 0;
	private double				score				= 0;
	private String				currency			= "";

	private int					status				= Constants.ORDER_STATUS_PENDING;
	private String				cause				= "";
	private String				description			= "";

	public long getOrderId()
	{
		return orderId;
	}

	public void setOrderId(long orderId)
	{
		this.orderId = orderId;
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

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public Date getModifiedDate()
	{
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate)
	{
		this.modifiedDate = modifiedDate;
	}

	public long getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
	}

	public String getOrderType()
	{
		return orderType;
	}

	public void setOrderType(String orderType)
	{
		this.orderType = orderType;
	}

	public Date getOrderDate()
	{
		return orderDate;
	}

	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public Date getCycleDate()
	{
		return cycleDate;
	}

	public void setCycleDate(Date cycleDate)
	{
		this.cycleDate = cycleDate;
	}

	public long getSubscriberId()
	{
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId)
	{
		this.subscriberId = subscriberId;
	}

	public long getSubProductId()
	{
		return subProductId;
	}

	public void setSubProductId(long subProductId)
	{
		this.subProductId = subProductId;
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

	public String getShipTo()
	{
		return shipTo;
	}

	public void setShipTo(String destAddress)
	{
		this.shipTo = destAddress;
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

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
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

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
