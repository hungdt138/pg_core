/**
 * 
 */
package com.crm.spam.bean;

import java.util.Calendar;
import java.util.Date;


/**
 * @author hungdt
 * 
 */
public class SpamEntry {
	private java.lang.String commandId;

	private long companyId;

	private Date createDate;

	private String description;

	private long entryId;

	private long groupId;

	private Date modifiedDate;

	private long primaryKey;

	private String processClass;

	private String processMethod;

	private String productId;

	private String properties;

	private long resourcePrimKey;

	private String spamId;

	private int spamInterval;

	private int spamTimes;

	private int suspendTime;

	private String title;

	private long userId;

	private String userName;

	private String uuid;

	private double version;

	public SpamEntry() {

	}

	public java.lang.String getCommandId() {
		return commandId;
	}

	public void setCommandId(java.lang.String commandId) {
		this.commandId = commandId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getEntryId() {
		return entryId;
	}

	public void setEntryId(long entryId) {
		this.entryId = entryId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getProcessClass() {
		return processClass;
	}

	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}

	public String getProcessMethod() {
		return processMethod;
	}

	public void setProcessMethod(String processMethod) {
		this.processMethod = processMethod;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public long getResourcePrimKey() {
		return resourcePrimKey;
	}

	public void setResourcePrimKey(long resourcePrimKey) {
		this.resourcePrimKey = resourcePrimKey;
	}

	public String getSpamId() {
		return spamId;
	}

	public void setSpamId(String spamId) {
		this.spamId = spamId;
	}

	public int getSpamInterval() {
		return spamInterval;
	}

	public void setSpamInterval(int spamInterval) {
		this.spamInterval = spamInterval;
	}

	public int getSpamTimes() {
		return spamTimes;
	}

	public void setSpamTimes(int spamTimes) {
		this.spamTimes = spamTimes;
	}

	public int getSuspendTime() {
		return suspendTime;
	}

	public void setSuspendTime(int suspendTime) {
		this.suspendTime = suspendTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	private java.lang.Object __equalsCalc = null;

	@SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SpamEntry))
			return false;
		SpamEntry other = (SpamEntry) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& ((this.commandId == null && other.getCommandId() == null) || (this.commandId != null && this.commandId
						.equals(other.getCommandId())))
				&& this.companyId == other.getCompanyId()
				&& ((this.createDate == null && other.getCreateDate() == null) || (this.createDate != null && this.createDate
						.equals(other.getCreateDate())))
				&& ((this.description == null && other.getDescription() == null) || (this.description != null && this.description
						.equals(other.getDescription())))
				&& this.entryId == other.getEntryId()
				&& this.groupId == other.getGroupId()
				&& ((this.modifiedDate == null && other.getModifiedDate() == null) || (this.modifiedDate != null && this.modifiedDate
						.equals(other.getModifiedDate())))
				&& this.primaryKey == other.getPrimaryKey()
				&& ((this.processClass == null && other.getProcessClass() == null) || (this.processClass != null && this.processClass
						.equals(other.getProcessClass())))
				&& ((this.processMethod == null && other.getProcessMethod() == null) || (this.processMethod != null && this.processMethod
						.equals(other.getProcessMethod())))
				&& ((this.productId == null && other.getProductId() == null) || (this.productId != null && this.productId
						.equals(other.getProductId())))
				&& ((this.properties == null && other.getProperties() == null) || (this.properties != null && this.properties
						.equals(other.getProperties())))
				&& this.resourcePrimKey == other.getResourcePrimKey()
				&& ((this.spamId == null && other.getSpamId() == null) || (this.spamId != null && this.spamId
						.equals(other.getSpamId())))
				&& this.spamInterval == other.getSpamInterval()
				&& this.spamTimes == other.getSpamTimes()
				&& this.suspendTime == other.getSuspendTime()
				&& ((this.title == null && other.getTitle() == null) || (this.title != null && this.title
						.equals(other.getTitle())))
				&& this.userId == other.getUserId()
				&& ((this.userName == null && other.getUserName() == null) || (this.userName != null && this.userName
						.equals(other.getUserName())))
				&& ((this.uuid == null && other.getUuid() == null) || (this.uuid != null && this.uuid
						.equals(other.getUuid())))
				&& this.version == other.getVersion();
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getCommandId() != null) {
			_hashCode += getCommandId().hashCode();
		}
		_hashCode += new Long(getCompanyId()).hashCode();
		if (getCreateDate() != null) {
			_hashCode += getCreateDate().hashCode();
		}
		if (getDescription() != null) {
			_hashCode += getDescription().hashCode();
		}
		_hashCode += new Long(getEntryId()).hashCode();
		_hashCode += new Long(getGroupId()).hashCode();
		if (getModifiedDate() != null) {
			_hashCode += getModifiedDate().hashCode();
		}
		_hashCode += new Long(getPrimaryKey()).hashCode();
		if (getProcessClass() != null) {
			_hashCode += getProcessClass().hashCode();
		}
		if (getProcessMethod() != null) {
			_hashCode += getProcessMethod().hashCode();
		}
		if (getProductId() != null) {
			_hashCode += getProductId().hashCode();
		}
		if (getProperties() != null) {
			_hashCode += getProperties().hashCode();
		}
		_hashCode += new Long(getResourcePrimKey()).hashCode();
		if (getSpamId() != null) {
			_hashCode += getSpamId().hashCode();
		}
		_hashCode += getSpamInterval();
		_hashCode += getSpamTimes();
		_hashCode += getSuspendTime();
		if (getTitle() != null) {
			_hashCode += getTitle().hashCode();
		}
		_hashCode += new Long(getUserId()).hashCode();
		if (getUserName() != null) {
			_hashCode += getUserName().hashCode();
		}
		if (getUuid() != null) {
			_hashCode += getUuid().hashCode();
		}
		_hashCode += new Double(getVersion()).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

}
