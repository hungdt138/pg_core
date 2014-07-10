package com.crm.ascs.collector;

import java.util.ArrayList;

import com.crm.ascs.net.INetData;
import com.crm.ascs.net.INetDataCollection;

public class TriggerCollection extends ArrayList<INetData> implements INetDataCollection
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Override
	public void put(INetData data)
	{
		synchronized (this)
		{
			add(data);
		}
	}

	@Override
	public INetData get()
	{
		synchronized (this)
		{
			if (size() > 0)
				return remove(0);
			else
				return null;
		}
	}

}
