package com.crm.ascs.collector;

import java.util.ArrayList;

public class TriggerClientHandlerCollection extends ArrayList<TriggerClientHandler>
{
	private static final long	serialVersionUID	= 1L;

	@Override
	public boolean add(TriggerClientHandler e)
	{
		e.setTriggerClientHandlerCollection(this);
		return super.add(e);
	}
}
