package com.crm.provisioning.cache;

import com.crm.kernel.index.IndexNode;
import com.crm.util.CompareUtil;

public class ProvisioningRoute extends IndexNode
{
	private String	provisioningType	= "";
	private String	routeType			= "";
	private String	routeKey			= "";

	private long	provisioningId		= 0;

	public ProvisioningRoute()
	{
		super();
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		try
		{
			buffer.append("provisioningType = " + provisioningType + "\t");
			buffer.append("routeType = " + routeType + "\t");
			buffer.append("routeKey = " + getRouteKey() + "\t");
			buffer.append("wildcard = " + isWildcard() + "\t");
			buffer.append("provisioningId = " + provisioningId + "\t");
		}
		catch (Exception e)
		{
		}

		return buffer.toString();
	}

	@Override
	public int compareTo(IndexNode obj)
	{
		if (obj == null)
		{
			return 1;
		}

		ProvisioningRoute route = (ProvisioningRoute) obj;

		// compare provisioningType
		int result = provisioningType.compareTo(route.getProvisioningType());

		if (result != 0)
		{
			return result;
		}

		// compare routeType
		result = routeType.compareTo(route.getRouteType());

		if (result != 0)
		{
			return result;
		}

		// compare routeKey
		return CompareUtil.compareByIndexKey(this, route);
	}

	public String getProvisioningType()
	{
		return provisioningType;
	}

	public void setProvisioningType(String provisioningType)
	{
		this.provisioningType = provisioningType;
	}

	public String getRouteType()
	{
		return routeType;
	}

	public void setRouteType(String routeType)
	{
		this.routeType = routeType;
	}

	public String getRouteKey()
	{
		return routeKey;
	}

	public void setRouteKey(String routeKey)
	{
		this.routeKey = routeKey;
		
		// this.setIndexKey(routeKey);
	}

	public long getProvisioningId()
	{
		return provisioningId;
	}

	public void setProvisioningId(long provisioningId)
	{
		this.provisioningId = provisioningId;
	}

	public boolean equals(String provisioningType, String routeType, String routeKey)
	{
		if (getProvisioningType().equals(provisioningType)
				&& getRouteType().equals(routeType)
				&& routeKey.startsWith(getRouteKey()))
		{
			if (routeKey.equals(getRouteKey()) || isWildcard())
			{
				return true;
			}
		}
		
		return false;
	}
}
