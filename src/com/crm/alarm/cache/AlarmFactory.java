package com.crm.alarm.cache;

import java.util.Date;
import com.crm.util.DateUtil;

public class AlarmFactory
{
	private static AlarmCache	cache		= new AlarmCache();
	private static Date			cacheDate	= null;

	public AlarmFactory()
	{
		super();
	}

	public synchronized static void clear() throws Exception
	{
		if (cache != null)
		{
			cache.clear();
		}

		setCacheDate(null);
	}

	public synchronized static AlarmCache loadCache(Date date) throws Exception
	{
		try
		{
			date = DateUtil.trunc(date);

			if (cache != null)
			{
				cache.clear();
			}
			else
			{
				cache = new AlarmCache();
			}
			cache.loadCache();

			cacheDate = date;
		}
		catch (Exception e)
		{
			cache = null;
			cacheDate = null;

			throw e;
		}

		return cache;
	}

	public synchronized static AlarmCache getCache(Date date) throws Exception
	{
		boolean reload = true;

		try
		{
			date = DateUtil.trunc(date);

			if (cache == null)
			{
				cache = new AlarmCache();
			}
			else if ((cacheDate == null) || !cacheDate.equals(date))
			{
				cache.clear();
			}
			else
			{
				reload = false;
			}

			if (reload)
			{
				cache.loadCache();

				cacheDate = date;
			}
		}
		catch (Exception e)
		{
			cache = null;
			cacheDate = null;

			throw e;
		}

		return cache;
	}

	public static AlarmCache getCache() throws Exception
	{
		return getCache(new Date());
	}

	public static Date getCacheDate()
	{
		return cacheDate;
	}

	public static void setCacheDate(Date cacheDate)
	{
		AlarmFactory.cacheDate = cacheDate;
	}
}
