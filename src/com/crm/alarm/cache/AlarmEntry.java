package com.crm.alarm.cache;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.index.IndexNode;

public class AlarmEntry extends IndexNode
{
	private long	alarmId					= 0;
	private String	title					= "";
	private int		waitDuration			= 0;
	private BinaryIndex	actions				= new BinaryIndex();
	
	public AlarmEntry(long alarmId, String alias)
	{
		super(alias);

		setAlarmId(alarmId);
	}
	
	public long getAlarmId()
	{
		return alarmId;
	}

	public void setAlarmId(long alarmId)
	{
		this.alarmId = alarmId;
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public int getWaitDuration()
	{
		return waitDuration;
	}
	
	public void setWaitDuration(int waitDuration)
	{
		this.waitDuration = waitDuration;
	}
	
	public BinaryIndex getActions()
	{
		return actions;
	}

	public void setActions(BinaryIndex actions)
	{
		this.actions = actions;
	}
	
	public AlarmAction getAlarmAction(long alarmId, String channel) throws Exception
	{
		AlarmAction lookup = new AlarmAction();

		lookup.setAlarmId(alarmId);
		lookup.setDescription(channel);

		return (AlarmAction) actions.get(lookup, false);
	}
}
