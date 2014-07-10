/**
 * 
 */
package com.crm.kernel.domain;

import com.crm.kernel.index.IndexNode;

/**
 * @author ThangPV
 * 
 */
public class DomainEntry extends IndexNode
{
	private long	domainId	= 0;
	private String	title		= "";
	private String	domainType	= "";
	private String	value		= "";

	public DomainEntry(long domainId, String alias)
	{
		super(alias);
		
		setDomainId(domainId);
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDomainType()
	{
		return domainType;
	}

	public void setDomainType(String domainType)
	{
		this.domainType = domainType;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public long getDomainId()
	{
		return domainId;
	}

	public void setDomainId(long domainId)
	{
		this.domainId = domainId;
	}
}
