/**
 * 
 */
package com.crm.subscriber.bean;

import java.io.Serializable;

import com.crm.kernel.message.Constants;

/**
 * @author ThangPV
 * 
 */
public class SubscriberProfile implements Serializable
{
	// PK fields -->

	private long	subscriberId	= Constants.DEFAULT_ID;

	// Other fields -->
	private String	screenName		= "";
	private String	password		= "";
	private String	openTime		= "";
	private String	closedTime		= "";
	private String	languageId		= "";
	private String	description		= "";

	public long getSubscriberId()
	{
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId)
	{
		this.subscriberId = subscriberId;
	}

	public String getScreenName()
	{
		return screenName;
	}

	public void setScreenName(String screenName)
	{
		this.screenName = screenName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getOpenTime()
	{
		return openTime;
	}

	public void setOpenTime(String openTime)
	{
		this.openTime = openTime;
	}

	public String getClosedTime()
	{
		return closedTime;
	}

	public void setClosedTime(String closedTime)
	{
		this.closedTime = closedTime;
	}

	public String getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(String languageId)
	{
		this.languageId = languageId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
