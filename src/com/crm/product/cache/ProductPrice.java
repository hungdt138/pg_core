package com.crm.product.cache;

import java.util.Date;

import com.crm.kernel.index.IndexNode;

public class ProductPrice extends IndexNode
{
	private long	productId		= 0;
	private long	relateProductId	= 0;
	private long	segmentId		= 0;

	private String	actionType		= "";
	private String	channel			= "";
	private int		minQuantity		= 1;
	private int		maxQuantity		= 1;

	private String	priceType		= "charge";
	private String	currency		= "";
	private boolean	flatCharge		= true;
	private boolean	allowBaseCharge	= true;
	private double	baseOfCharge	= 0;
	private double	fullOfCharge	= 0;

	private boolean	discountEnable	= false;
	private String	discountType	= "";
	private double	discountValue	= 0;
	
	//2013-07-25 MinhDT Add start for CR charge promotion
	private String	balanceType		= "";
	//2013-07-25 MinhDT Add end for CR charge promotion

	public ProductPrice()
	{
		super();
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public long getRelateProductId()
	{
		return relateProductId;
	}

	public void setRelateProductId(long relateProductId)
	{
		this.relateProductId = relateProductId;
	}

	public long getSegmentId()
	{
		return segmentId;
	}

	public void setSegmentId(long segmentId)
	{
		this.segmentId = segmentId;
	}

	public String getActionType()
	{
		return actionType;
	}

	public void setActionType(String actionType)
	{
		this.actionType = actionType;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public int getMinQuantity()
	{
		return minQuantity;
	}

	public void setMinQuantity(int minQuantity)
	{
		this.minQuantity = minQuantity;
	}

	public int getMaxQuantity()
	{
		return maxQuantity;
	}

	public void setMaxQuantity(int maxQuantity)
	{
		this.maxQuantity = maxQuantity;
	}

	public String getPriceType()
	{
		return priceType;
	}

	public void setPriceType(String priceType)
	{
		this.priceType = priceType;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public boolean isFlatCharge()
	{
		return flatCharge;
	}

	public void setFlatCharge(boolean flatCharge)
	{
		this.flatCharge = flatCharge;
	}

	public boolean isAllowBaseCharge()
	{
		return allowBaseCharge;
	}

	public void setAllowBaseCharge(boolean allowBaseCharge)
	{
		this.allowBaseCharge = allowBaseCharge;
	}

	public double getBaseOfCharge()
	{
		return baseOfCharge;
	}

	public void setBaseOfCharge(double baseOfCharge)
	{
		this.baseOfCharge = baseOfCharge;
	}

	public double getFullOfCharge()
	{
		return fullOfCharge;
	}

	public void setFullOfCharge(double fullOfCharge)
	{
		this.fullOfCharge = fullOfCharge;
	}

	public boolean equals(
			String channel, String actionType, long segmentId, long relateProductId, int quantity, Date date)
	{
		boolean result = (getChannel().equals(channel))
				&& (getActionType().equals(actionType))
				&& (getSegmentId() == segmentId)
				&& (getRelateProductId() == relateProductId)
				&& (getMinQuantity() <= quantity)
				&& (getMaxQuantity() >= quantity)
				&& isRange(date);

		return result;
	}
	
	//2013-07-25 MinhDT Add start for CR charge promotion
	public boolean equals(
			String channel, String actionType, long segmentId, long relateProductId, int quantity, Date date, String balanceType)
	{
		boolean result = (getChannel().equals(channel))
				&& (getActionType().equals(actionType))
				&& (getSegmentId() == segmentId)
				&& (getRelateProductId() == relateProductId)
				&& (getMinQuantity() <= quantity)
				&& (getMaxQuantity() >= quantity)
				&& isRange(date)
				&& (getBalanceType().equals(balanceType));

		return result;
	}
	//2013-07-25 MinhDT Add end for CR charge promotion

	public boolean isDiscountEnable()
	{
		return discountEnable;
	}

	public void setDiscountEnable(boolean discountEnable)
	{
		this.discountEnable = discountEnable;
	}

	public String getDiscountType()
	{
		return discountType;
	}

	public void setDiscountType(String discountType)
	{
		this.discountType = discountType;
	}

	public double getDiscountValue()
	{
		return discountValue;
	}

	public void setDiscountValue(double discountValue)
	{
		this.discountValue = discountValue;
	}
	
	//2013-07-25 MinhDT Add start for CR charge promotion
	public String getBalanceType()
	{
		return balanceType;
	}

	public void setBalanceType(String balanceType)
	{
		this.balanceType = balanceType;
	}
	//2013-07-25 MinhDT Add end for CR charge promotion
}
