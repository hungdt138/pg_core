package com.crm.alarm.cache;

import com.crm.kernel.index.IndexNode;

public class AlarmTemplate extends IndexNode
{
	private long	templateId	= 0;
	private String	sendType	= "";
	private String	sender		= "";
	private String	sendTo		= "";
	private String	subject		= "";
	private String	content		= "";

	public AlarmTemplate()
	{
		super();
	}

	public long getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(long templateId)
	{
		this.templateId = templateId;
	}

	public String getSendType()
	{
		return sendType;
	}

	public void setSendType(String sendType)
	{
		this.sendType = sendType;
	}

	public String getSender()
	{
		return sender;
	}

	public void setSender(String sender)
	{
		this.sender = sender;
	}

	public String getSendTo()
	{
		return sendTo;
	}

	public void setSendTo(String sendTo)
	{
		this.sendTo = sendTo;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
