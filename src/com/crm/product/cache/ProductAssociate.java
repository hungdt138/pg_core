package com.crm.product.cache;

import com.crm.kernel.index.IndexNode;

public class ProductAssociate extends IndexNode
{
	private String	associateType	= "";
	private long	productId		= 0;
	private long	segmentId		= 0;

	public ProductAssociate()
	{
		super();
	}

	public boolean equals(String associateType, long productId)
	{
		return getAssociateType().equals(associateType) && (getProductId() == productId);
	}

	public String getAssociateType()
	{
		return associateType;
	}

	public void setAssociateType(String associateType)
	{
		this.associateType = associateType;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public long getSegmentId()
	{
		return segmentId;
	}

	public void setSegmentId(long segmentId)
	{
		this.segmentId = segmentId;
	}
}
