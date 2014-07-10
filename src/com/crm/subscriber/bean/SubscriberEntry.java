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
public class SubscriberEntry implements Serializable
{
	// PK fields -->

	private long	subscriberId		= Constants.DEFAULT_ID;

	// Other fields -->

	private String	IMEI				= "";
	private String	IMSI				= "";
	private String	isdn				= "";
	private int		subscriberType		= Constants.PREPAID_SUB_TYPE;

	private String	idType				= "";
	private String	idNo				= "";
	private String	idIssuer			= "";
	private Date	idIssueDate			= null;
	private Date	idExpirationDate	= null;
	private String	taxId				= "";

	private long	merchantId			= Constants.DEFAULT_ID;
	private long	productId			= Constants.DEFAULT_ID;
	private String	contractNo			= "";

	private long	segmentId			= Constants.DEFAULT_ID;
	private long	rankId				= Constants.DEFAULT_ID;
	private Date	rankStartDate		= null;
	private Date	rankExpirationDate	= null;

	private int		barringStatus		= Constants.USER_CANCEL_STATUS;
	private int		supplierStatus		= Constants.SUPPLIER_CANCEL_STATUS;

	private Date	termDate			= null;
	private Date	registerDate		= null;
	private Date	unregisterDate		= null;

	public boolean isActive()
	{
		return (getUnregisterDate() != null);
	}

	public long getSubscriberId()
	{
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId)
	{
		this.subscriberId = subscriberId;
	}

	public String getIMEI()
	{
		return IMEI;
	}

	public void setIMEI(String iMEI)
	{
		IMEI = iMEI;
	}

	public String getIMSI()
	{
		return IMSI;
	}

	public void setIMSI(String iMSI)
	{
		IMSI = iMSI;
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

	public String getIdType()
	{
		return idType;
	}

	public void setIdType(String idType)
	{
		this.idType = idType;
	}

	public String getIdNo()
	{
		return idNo;
	}

	public void setIdNo(String idNo)
	{
		this.idNo = idNo;
	}

	public String getIdIssuer()
	{
		return idIssuer;
	}

	public void setIdIssuer(String idIssuer)
	{
		this.idIssuer = idIssuer;
	}

	public Date getIdIssueDate()
	{
		return idIssueDate;
	}

	public void setIdIssueDate(Date idIssueDate)
	{
		this.idIssueDate = idIssueDate;
	}

	public Date getIdExpirationDate()
	{
		return idExpirationDate;
	}

	public void setIdExpirationDate(Date idExpirationDate)
	{
		this.idExpirationDate = idExpirationDate;
	}

	public String getTaxId()
	{
		return taxId;
	}

	public void setTaxId(String taxId)
	{
		this.taxId = taxId;
	}

	public long getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(long merchantId)
	{
		this.merchantId = merchantId;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public String getContractNo()
	{
		return contractNo;
	}

	public void setContractNo(String contractNo)
	{
		this.contractNo = contractNo;
	}

	public long getSegmentId()
	{
		return segmentId;
	}

	public void setSegmentId(long segmentId)
	{
		this.segmentId = segmentId;
	}

	public long getRankId()
	{
		return rankId;
	}

	public void setRankId(long rankId)
	{
		this.rankId = rankId;
	}

	public Date getRankStartDate()
	{
		return rankStartDate;
	}

	public void setRankStartDate(Date rankStartDate)
	{
		this.rankStartDate = rankStartDate;
	}

	public Date getRankExpirationDate()
	{
		return rankExpirationDate;
	}

	public void setRankExpirationDate(Date rankExpirationDate)
	{
		this.rankExpirationDate = rankExpirationDate;
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
}
