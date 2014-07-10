package com.crm.provisioning.cache;

import com.crm.kernel.index.IndexNode;

public class ProvisioningMessage extends IndexNode
{
	private String	cause			= "";
	private String	content			= "";
	private int		responseCode	= 0;

	public ProvisioningMessage()
	{
		super();
	}

	public boolean equals(String cause)
	{
		return getCause().equals(cause);
	}

	public String getCause()
	{
		return cause;
	}

	public void setCause(String cause)
	{
		this.cause = cause;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getResponseCode()
	{
		return responseCode;
	}

	public void setResponseCode(int responseCode)
	{
		this.responseCode = responseCode;
	}
}
