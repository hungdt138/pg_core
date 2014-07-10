/**
 * 
 */
package com.crm.spam.bean;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


/**
 * @author hungdt
 * 
 */
public class SpamLog {
	private java.lang.String commandId;

	private long companyId;

	private Timestamp createDate;

	private String description;

	private Timestamp expireDate;

	private long groupId;

	private long logId;

	private Timestamp modifiedDate;

	private long primaryKey;

	private java.lang.String productId;

	private java.lang.String spamAddress;

	private Timestamp spamDate;

	private int spamTimes;

	private String spamType;

	private String status;

	private long userId;

	private String userName;

	public SpamLog() {
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

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Timestamp expireDate) {
		this.expireDate = expireDate;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getLogId() {
		return logId;
	}

	public void setLogId(long logId) {
		this.logId = logId;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	public java.lang.String getProductId() {
		return productId;
	}

	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}

	public java.lang.String getSpamAddress() {
		return spamAddress;
	}

	public void setSpamAddress(java.lang.String spamAddress) {
		this.spamAddress = spamAddress;
	}

	public Timestamp getSpamDate() {
		return spamDate;
	}

	public void setSpamDate(Timestamp spamDate) {
		this.spamDate = spamDate;
	}

	public int getSpamTimes() {
		return spamTimes;
	}

	public void setSpamTimes(int spamTimes) {
		this.spamTimes = spamTimes;
	}

	public String getSpamType() {
		return spamType;
	}

	public void setSpamType(String spamType) {
		this.spamType = spamType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SpamLog))
			return false;
		SpamLog other = (SpamLog) obj;
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
				&& ((this.expireDate == null && other.getExpireDate() == null) || (this.expireDate != null && this.expireDate
						.equals(other.getExpireDate())))
				&& this.groupId == other.getGroupId()
				&& this.logId == other.getLogId()
				&& ((this.modifiedDate == null && other.getModifiedDate() == null) || (this.modifiedDate != null && this.modifiedDate
						.equals(other.getModifiedDate())))
				&& this.primaryKey == other.getPrimaryKey()
				&& ((this.productId == null && other.getProductId() == null) || (this.productId != null && this.productId
						.equals(other.getProductId())))
				&& ((this.spamAddress == null && other.getSpamAddress() == null) || (this.spamAddress != null && this.spamAddress
						.equals(other.getSpamAddress())))
				&& ((this.spamDate == null && other.getSpamDate() == null) || (this.spamDate != null && this.spamDate
						.equals(other.getSpamDate())))
				&& this.spamTimes == other.getSpamTimes()
				&& ((this.spamType == null && other.getSpamType() == null) || (this.spamType != null && this.spamType
						.equals(other.getSpamType())))
				&& ((this.status == null && other.getStatus() == null) || (this.status != null && this.status
						.equals(other.getStatus())))
				&& this.userId == other.getUserId()
				&& ((this.userName == null && other.getUserName() == null) || (this.userName != null && this.userName
						.equals(other.getUserName())));
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
		if (getExpireDate() != null) {
			_hashCode += getExpireDate().hashCode();
		}
		_hashCode += new Long(getGroupId()).hashCode();
		_hashCode += new Long(getLogId()).hashCode();
		if (getModifiedDate() != null) {
			_hashCode += getModifiedDate().hashCode();
		}
		_hashCode += new Long(getPrimaryKey()).hashCode();
		if (getProductId() != null) {
			_hashCode += getProductId().hashCode();
		}
		if (getSpamAddress() != null) {
			_hashCode += getSpamAddress().hashCode();
		}
		if (getSpamDate() != null) {
			_hashCode += getSpamDate().hashCode();
		}
		_hashCode += getSpamTimes();
		if (getSpamType() != null) {
			_hashCode += getSpamType().hashCode();
		}
		if (getStatus() != null) {
			_hashCode += getStatus().hashCode();
		}
		_hashCode += new Long(getUserId()).hashCode();
		if (getUserName() != null) {
			_hashCode += getUserName().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}
}
