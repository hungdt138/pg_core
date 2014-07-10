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
public class SubscriberAudit implements Serializable
{
	// PK fields -->

	private long	auditId			= Constants.DEFAULT_ID;

	// Audit fields -->

	private long	companyId		= Constants.DEFAULT_ID;
	private long	userId			= Constants.DEFAULT_ID;
	private String	userName		= "";
	private Date	createDate		= null;
	private Date	modifiedDate	= null;

	// Other fields -->

	private String	auditType		= "";
	private Date	auditDate		= null;

	private long	classPK			= Constants.DEFAULT_ID;
	private String	isdn			= "";
	private String	actionType		= "";

	private String	description		= "";

	public long getAuditId()
	{
		return auditId;
	}

	public void setAuditId(long auditId)
	{
		this.auditId = auditId;
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

	public String getAuditType()
	{
		return auditType;
	}

	public void setAuditType(String auditType)
	{
		this.auditType = auditType;
	}

	public Date getAuditDate()
	{
		return auditDate;
	}

	public void setAuditDate(Date auditDate)
	{
		this.auditDate = auditDate;
	}

	public long getClassPK()
	{
		return classPK;
	}

	public void setClassPK(long classPK)
	{
		this.classPK = classPK;
	}

	public String getIsdn()
	{
		return isdn;
	}

	public void setIsdn(String isdn)
	{
		this.isdn = isdn;
	}

	public String getActionType()
	{
		return actionType;
	}

	public void setActionType(String actionType)
	{
		this.actionType = actionType;
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
