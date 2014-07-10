package com.crm.product.cache;

import java.util.Date;

import com.crm.kernel.index.IndexNode;

public class ProductScore extends IndexNode
{
	private long	productId		= 0;
	private long	segmentId		= 0;

	private String	actionType		= "";
	private int		minQuantity		= 0;
	private int		maxQuantity		= 999999999;
	private int		minAmount		= 0;
	private int		maxAmount		= 999999999;

	private String	balanceType		= "charge";
	private boolean	flatCharge		= true;
	private double	scoreRate		= 0;
	private double	scoreUnit		= 0;
	private long	campaignId		= 0;

	public ProductScore()
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

	public int getMinAmount()
	{
		return minAmount;
	}

	public void setMinAmount(int minAmount)
	{
		this.minAmount = minAmount;
	}

	public int getMaxAmount()
	{
		return maxAmount;
	}

	public void setMaxAmount(int maxAmount)
	{
		this.maxAmount = maxAmount;
	}

	public String getBalanceType()
	{
		return balanceType;
	}

	public void setBalanceType(String balanceType)
	{
		this.balanceType = balanceType;
	}

	public boolean isFlatCharge()
	{
		return flatCharge;
	}

	public void setFlatCharge(boolean flatCharge)
	{
		this.flatCharge = flatCharge;
	}

	public double getScoreRate()
	{
		return scoreRate;
	}

	public void setScoreRate(double scoreRate)
	{
		this.scoreRate = scoreRate;
	}

	public double getScoreUnit()
	{
		return scoreUnit;
	}

	public void setScoreUnit(double scoreUnit)
	{
		this.scoreUnit = scoreUnit;
	}

	public long getCampaignId()
	{
		return campaignId;
	}

	public void setCampaignId(long campaignId)
	{
		this.campaignId = campaignId;
	}

	public boolean equals(long productId, long segmentId, int quantity, double amount, Date date)
	{
		boolean result = (getProductId() == productId)
				&& (getSegmentId() == segmentId)
				&& (getMinQuantity() <= quantity)
				&& (getMaxQuantity() >= quantity)
				&& (getMinAmount() <= amount)
				&& (getMaxAmount() >= amount)
				&& isRange(date);;

		return result;
	}
}
