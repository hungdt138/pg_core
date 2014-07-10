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
public class SubscriberActivate implements Serializable
{
	// PK fields -->

	private long	activateId		= Constants.DEFAULT_ID;

	// Audit fields -->

	private long	companyId		= Constants.DEFAULT_ID;
	private long	userId			= Constants.DEFAULT_ID;
	private String	userName		= "";
	private Date	createDate		= null;
	private Date	modifiedDate	= null;

	// Other fields -->

	private long	subscriberId	= Constants.DEFAULT_ID;
	private long	productId		= Constants.DEFAULT_ID;
	private String	sourceAddress	= "";

	private int		barringStatus	= Constants.USER_CANCEL_STATUS;
	private int		supplierStatus	= Constants.SUPPLIER_CANCEL_STATUS;

	private Date	startDate		= null;
	private Date	endDate			= null;

	private String	description		= "";

	public void setActivateId(long activateId)
	{
		this.activateId = activateId;
	}

	public long getActivateId()
	{
		return activateId;
	}

	public long getSubscriberId()
	{
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId)
	{
		this.subscriberId = subscriberId;
	}

	public long getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(long companyId)
	{
		this.companyId = companyId;
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

	public long getProductId()
	{
		return productId;
	}

	public void setSourceAddress(String sourceAddress)
	{
		this.sourceAddress = sourceAddress;
	}

	public String getSourceAddress()
	{
		return sourceAddress;
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

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
}
