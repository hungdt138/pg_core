/**
 * 
 */
package com.crm.kernel.domain;

import java.util.Date;

import com.crm.kernel.domain.DomainCache;
import com.crm.util.DateUtil;

/**
 * @author ThangPV
 * 
 */
public class DomainFactory
{
	private static DomainCache	cache		= new DomainCache();
	private static Date			cacheDate	= null;

	public DomainFactory()
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

	public synchronized static DomainCache loadCache(Date date) throws Exception
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
				cache = new DomainCache();
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

	public synchronized static DomainCache getCache(Date date) throws Exception
	{
		boolean reload = true;

		try
		{
			date = DateUtil.trunc(date);

			if (cache == null)
			{
				cache = new DomainCache();
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

	public static DomainCache getCache() throws Exception
	{
		return getCache(new Date());
	}

	public static Date getCacheDate()
	{
		return cacheDate;
	}

	public static void setCacheDate(Date cacheDate)
	{
		DomainFactory.cacheDate = cacheDate;
	}
}
