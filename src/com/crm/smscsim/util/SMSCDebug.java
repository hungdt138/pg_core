package com.crm.smscsim.util;

import com.crm.thread.DispatcherThread;
import com.logica.smpp.debug.Debug;

public class SMSCDebug implements Debug
{
	private int					indent		= 0;
	private boolean				active		= false;
	private DispatcherThread	dispatcher	= null;

	public SMSCDebug(DispatcherThread dispatcher)
	{
		this.dispatcher = dispatcher;
		this.active = true;
	}

	@Override
	public void enter(int group, Object from, String name)
	{
		enter(from, name);
	}

	@Override
	public void enter(Object from, String name)
	{
		if (active)
		{
			debugMonitor(getDelimiter(true, from, name));
			indent++;
		}
	}

	@Override
	public void write(int group, String msg)
	{
		write(msg);
	}

	@Override
	public void write(String msg)
	{

		if (active)
		{
			debugMonitor(getIndent() + " " + msg);
		}
	}

	@Override
	public void exit(int group, Object from)
	{
		exit(from);
	}

	@Override
	public void exit(Object from)
	{
		if (active)
		{
			indent--;
			if (indent < 0)
			{
				// it's your fault :-)
				indent = 0;
			}
			debugMonitor(getDelimiter(false, from, ""));
		}
	}

	@Override
	public void activate()
	{
		active = true;
	}

	@Override
	public void activate(int group)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate()
	{
		active = false;
	}

	@Override
	public void deactivate(int group)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean active(int group)
	{
		// TODO Auto-generated method stub
		return false;
	}

	private String getDelimiter(boolean start, Object from, String name)
	{
		String indentStr = getIndent();
		if (start)
		{
			indentStr += "-> ";
		}
		else
		{
			indentStr += "<- ";
		}
		return indentStr + from.toString() + (name == "" ? "" : " " + name);
	}

	private String getIndent()
	{
		String result = new String("");
		for (int i = 0; i < indent; i++)
		{
			result += "  ";
		}
		return result;
	}

	private void debugMonitor(Object message)
	{
		dispatcher.debugMonitor(message);
	}
}
