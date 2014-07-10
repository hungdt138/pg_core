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
public class SubscriberGroup implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -948469931842606312L;

	// PK fields -->

	private long				subGroupId			= Constants.DEFAULT_ID;

	// Audit fields -->

	private long				userId				= Constants.DEFAULT_ID;
	private String				userName			= "";
	private Date				createDate			= null;
	private Date				modifiedDate		= null;

	// Other fields -->

	private String				groupType			= "";
	private long				productId			= Constants.DEFAULT_ID;
	private String				isdn				= "";
	private String				referalIsdn			= "";
	private String				verifyCode			= "";

	private Date				registerDate		= null;
	private Date				unregisterDate		= null;
	private Date				expirationDate		= null;

	private int					status				= Constants.ORDER_STATUS_PENDING;

	public long getSubGroupId()
	{
		return subGroupId;
	}

	public void setSubGroupId(long subGroupId)
	{
		this.subGroupId = subGroupId;
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

	public String getGroupType()
	{
		return groupType;
	}

	public void setGroupType(String groupType)
	{
		this.groupType = groupType;
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

	public String getReferalIsdn()
	{
		return referalIsdn;
	}

	public void setReferalIsdn(String referalIsdn)
	{
		this.referalIsdn = referalIsdn;
	}

	public String getVerifyCode()
	{
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode)
	{
		this.verifyCode = verifyCode;
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

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
