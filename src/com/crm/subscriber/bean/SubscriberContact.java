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
public class SubscriberContact implements Serializable
{
	// PK fields -->

	private long	contactId		= Constants.DEFAULT_ID;

	// Other fields -->

	private long	subscriberId	= Constants.DEFAULT_ID;

	private String	contactType		= "";
	private String	fullName		= "";
	private long	countryId		= Constants.DEFAULT_ID;
	private long	regionId		= Constants.DEFAULT_ID;
	private String	address			= "";
	private String	phone			= "";
	private String	mobile			= "";
	private String	fax				= "";
	private String	email			= "";
	private String	taxId			= "";
	private Date	birthDate		= null;

	private boolean	active			= false;

	public long getContactId()
	{
		return contactId;
	}

	public void setContactId(long contactId)
	{
		this.contactId = contactId;
	}

	public long getSubscriberId()
	{
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId)
	{
		this.subscriberId = subscriberId;
	}

	public String getContactType()
	{
		return contactType;
	}

	public void setContactType(String contactType)
	{
		this.contactType = contactType;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public long getCountryId()
	{
		return countryId;
	}

	public void setCountryId(long countryId)
	{
		this.countryId = countryId;
	}

	public long getRegionId()
	{
		return regionId;
	}

	public void setRegionId(long regionId)
	{
		this.regionId = regionId;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getFax()
	{
		return fax;
	}

	public void setFax(String fax)
	{
		this.fax = fax;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getTaxId()
	{
		return taxId;
	}

	public void setTaxId(String taxId)
	{
		this.taxId = taxId;
	}

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate(Date birthDate)
	{
		this.birthDate = birthDate;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}
}
