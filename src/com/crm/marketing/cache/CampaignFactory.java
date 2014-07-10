/**
 * 
 */
package com.crm.marketing.cache;

import java.util.Date;

import com.crm.marketing.cache.CampaignCache;
import com.crm.util.DateUtil;

/**
 * @author ThangPV
 * 
 */
public class CampaignFactory
{
	private static CampaignCache	cache		= new CampaignCache();
	private static Date				cacheDate	= null;

	public CampaignFactory()
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

	public synchronized static CampaignCache loadCache(Date date) throws Exception
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
				cache = new CampaignCache();
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

	public synchronized static CampaignCache getCache(Date date) throws Exception
	{
		boolean reload = true;

		try
		{
			date = DateUtil.trunc(date);

			if (cache == null)
			{
				cache = new CampaignCache();
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

	public static CampaignCache getCache() throws Exception
	{
		return getCache(new Date());
	}

	public static Date getCacheDate()
	{
		return cacheDate;
	}

	public static void setCacheDate(Date cacheDate)
	{
		CampaignFactory.cacheDate = cacheDate;
	}
}
