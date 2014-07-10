package com.crm.product.cache;

import com.crm.kernel.index.IndexNode;
import com.crm.util.CompareUtil;

public class ProductMessage extends IndexNode
{
	private long	productId	= 0;
	private String	actionType	= "";
	private long	campaignId	= 0;
	private String	channel		= "";
	private String	cause		= "";
	private String	languageId	= "";

	private int		causeValue	= 0;
	private String	content		= "";

	public ProductMessage()
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

	public String getCause()
	{
		return cause;
	}

	public void setCause(String cause)
	{
		this.cause = cause;
	}

	public String getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(String languageId)
	{
		this.languageId = languageId;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public boolean equals(long productId, String actionType, String languageId, String channel, String cause)
	{
		return ((getProductId() == productId)
				&& getChannel().equals(channel)
				&& getActionType().equals(actionType)
				&& getCause().equals(cause)
				&& getLanguageId().equals(languageId));
	}

	public int getCauseValue()
	{
		return causeValue;
	}

	public void setCauseValue(int causeValue)
	{
		this.causeValue = causeValue;
	}

	public long getCampaignId()
	{
		return campaignId;
	}

	public void setCampaignId(long campaignId)
	{
		this.campaignId = campaignId;
	}

	@Override
	public int compareTo(IndexNode obj)
	{
		ProductMessage lookup = (ProductMessage) obj;

		int result = CompareUtil.compare(getProductId(), lookup.getProductId());

		if (result != 0)
		{
			return result;
		}

		result = CompareUtil.compareString(getChannel(), lookup.getChannel(), false);

		if (result != 0)
		{
			return result;
		}

		result = CompareUtil.compareString(getActionType(), lookup.getActionType(), false);

		if (result != 0)
		{
			return result;
		}

		result = CompareUtil.compare(getCampaignId(), lookup.getCampaignId());

		if (result != 0)
		{
			return result;
		}

		result = CompareUtil.compareString(getLanguageId(), lookup.getLanguageId(), false);

		if (result != 0)
		{
			return result;
		}

		return CompareUtil.compareString(getCause(), lookup.getCause(), false);
	}
}
