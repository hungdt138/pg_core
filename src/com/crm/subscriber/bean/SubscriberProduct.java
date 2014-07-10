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
public class SubscriberProduct implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -948469931842606312L;

	// PK fields -->

	private long				subProductId		= Constants.DEFAULT_ID;

	// Audit fields -->

	private long				userId				= Constants.DEFAULT_ID;
	private String				userName			= "";
	private Date				createDate			= null;
	private Date				modifiedDate		= null;

	// Other fields -->

	private long				merchantId			= Constants.DEFAULT_ID;
	private long				subscriberId		= Constants.DEFAULT_ID;
	private long				productId			= Constants.DEFAULT_ID;
	private String				isdn				= "";
	private int					subscriberType		= Constants.USER_CANCEL_STATUS;
	private int					barringStatus		= Constants.USER_CANCEL_STATUS;
	private int					supplierStatus		= Constants.SUPPLIER_CANCEL_STATUS;
	private int					status				= Constants.SUBSCRIBER_PENDING_STATUS;

	private Date				termDate			= null;
	private Date				registerDate		= null;
	private Date				unregisterDate		= null;
	private Date				expirationDate		= null;
	private Date				graceDate			= null;

	private String				languageId			= "";

	public boolean isActive()
	{
		return (barringStatus == Constants.USER_ACTIVE_STATUS)
				&& (supplierStatus == Constants.SUPPLIER_ACTIVE_STATUS);
	}

	public boolean isBarring()
	{
		return (barringStatus == Constants.USER_BARRING_STATUS)
				|| (supplierStatus == Constants.SUPPLIER_BARRING_STATUS);
	}

	public boolean isCancel()
	{
		return (barringStatus == Constants.USER_CANCEL_STATUS)
				&& (supplierStatus == Constants.SUPPLIER_CANCEL_STATUS);
	}

	public boolean isPrepaid()
	{
		return subscriberType == Constants.PREPAID_SUB_TYPE;
	}

	public boolean isExpired()
	{
		if (getExpirationDate() != null)
		{
			return getExpirationDate().before(new Date());
		}
		else
		{
			return false;
		}
	}

	public boolean isOver()
	{
		if (getGraceDate() != null)
		{
			return getGraceDate().before(new Date());
		}
		else
		{
			return false;
		}
	}

	public long getSubProductId()
	{
		return subProductId;
	}

	public void setSubProductId(long subProductId)
	{
		this.subProductId = subProductId;
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

	public long getSubscriberId()
	{
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId)
	{
		this.subscriberId = subscriberId;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public String getIsdn()
	{
		return isdn;
	}

	public void setIsdn(String isdn)
	{
		this.isdn = isdn;
	}

	public int getSubscriberType()
	{
		return subscriberType;
	}

	public void setSubscriberType(int subscriberType)
	{
		this.subscriberType = subscriberType;
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

	public Date getTermDate()
	{
		return termDate;
	}

	public void setTermDate(Date termDate)
	{
		this.termDate = termDate;
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

	public Date getExpirationDate()
	{
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate)
	{
		this.expirationDate = expirationDate;
	}

	public Date getGraceDate()
	{
		return graceDate;
	}

	public void setGraceDate(Date graceDate)
	{
		this.graceDate = graceDate;
	}

	public String getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(String languageId)
	{
		this.languageId = languageId;
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
