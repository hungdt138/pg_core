package com.crm.alarm.cache;

import com.crm.kernel.index.IndexNode;
import com.crm.util.CompareUtil;

public class AlarmAction extends IndexNode
{
	private long	alarmId		= 0;
	private long	templateId	= 0;
	private String 	description = "";
	private int		minTimes	= 500;
	private int		maxTimes	= 1000;

	public AlarmAction()
	{
		super();
	}

	public long getAlarmId()
	{
		return alarmId;
	}

	public void setAlarmId(long alarmId)
	{
		this.alarmId = alarmId;
	}

	public long getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(long templateId)
	{
		this.templateId = templateId;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public int getMinTimes()
	{
		return minTimes;
	}
	
	public void setMinTimes(int minTimes)
	{
		this.minTimes = minTimes;
	}
	
	public int getMaxTimes()
	{
		return maxTimes;
	}
	
	public void setMaxTimes(int maxTimes)
	{
		this.maxTimes = maxTimes;
	}
	
	@Override
	public int compareTo(IndexNode obj)
	{
		AlarmAction lookup = (AlarmAction)obj;
		
		int result = CompareUtil.compare(getAlarmId(), lookup.getAlarmId());
		
		if (result == 0)
		{
			result = CompareUtil.compareString(getDescription(), lookup.getDescription(), false);
		}
		
		return result;
	}
}
