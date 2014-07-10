/**
 * 
 */
package com.crm.marketing.cache;

import java.util.Date;

import com.crm.kernel.index.IndexNode;
import com.crm.marketing.impl.CampaignImpl;

/**
 * @author ThangPV
 * 
 */
public class CampaignEntry extends IndexNode
{
	private long	campaignId		= 0;
	private String	alias			= "";
	private String	title			= "";
	private String	campaignType	= "";

	private boolean	scheduleEnable	= false;
	private String	scheduleUnit	= "daily";
	private int		schedulePeriod	= 1;

	private boolean	bonusEnable		= false;
	private String	bonusUnit		= "daily";
	private int		bonusPeriod		= 1;

	private long	productId		= 0;
	private long	segmentId		= 0;
	
	private int		status			= 0;

	public CampaignEntry(long campaignId, String alias)
	{
		super(alias);
		setAlias(alias);
		setCampaignId(campaignId);
	}

	public long getCampaignId()
	{
		return campaignId;
	}

	public void setCampaignId(long campaignId)
	{
		this.campaignId = campaignId;
	}
	
	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getCampaignType()
	{
		return campaignType;
	}

	public void setCampaignType(String campaignType)
	{
		this.campaignType = campaignType;
	}

	public CampaignImpl getExecuteImpl()
	{
		return (CampaignImpl) super.getExecuteImpl();
	}

	public boolean isScheduleEnable()
	{
		return scheduleEnable;
	}

	public void setScheduleEnable(boolean scheduleEnable)
	{
		this.scheduleEnable = scheduleEnable;
	}

	public String getScheduleUnit()
	{
		return scheduleUnit;
	}

	public void setScheduleUnit(String scheduleUnit)
	{
		this.scheduleUnit = scheduleUnit;
	}

	public int getSchedulePeriod()
	{
		return schedulePeriod;
	}

	public void setSchedulePeriod(int schedulePeriod)
	{
		this.schedulePeriod = schedulePeriod;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public boolean isBonusEnable()
	{
		return bonusEnable;
	}

	public void setBonusEnable(boolean bonusEnable)
	{
		this.bonusEnable = bonusEnable;
	}

	public String getBonusUnit()
	{
		return bonusUnit;
	}

	public void setBonusUnit(String bonusUnit)
	{
		this.bonusUnit = bonusUnit;
	}

	public int getBonusPeriod()
	{
		return bonusPeriod;
	}

	public void setBonusPeriod(int bonusPeriod)
	{
		this.bonusPeriod = bonusPeriod;
	}

	public long getSegmentId()
	{
		return segmentId;
	}

	public void setSegmentId(long segmentId)
	{
		this.segmentId = segmentId;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	public boolean equals(String alias, String channel)
	{
		boolean result = getAlias().equals(alias) && getCampaignType().equals(channel);

		return result;
	}
}
