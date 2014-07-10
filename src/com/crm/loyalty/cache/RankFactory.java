/**
 * 
 */
package com.crm.loyalty.cache;

import java.util.Date;

import org.apache.log4j.Logger;

import com.crm.loyalty.cache.RankCache;
import com.crm.util.DateUtil;

/**
 * @author ThangPV
 * 
 */
public class RankFactory
{
	private static RankCache	cache		= null;
	private static Date			cacheDate	= null;

	public RankFactory()
	{
		super();
	}

	public synchronized static RankCache loadCache(Date date) throws Exception
	{
		try
		{
			date = DateUtil.trunc(date);

			log.debug("Caching product information for date: " + date);

			if (cache != null)
			{
				cache.clear();
			}
			else
			{
				cache = new RankCache();
			}
			cache.loadCache();

			cacheDate = date;

			log.debug("Cached product information for date: " + cacheDate);
		}
		catch (Exception e)
		{
			cache = null;
			cacheDate = null;

			throw e;
		}

		return cache;
	}

	public synchronized static RankCache getCache(Date date) throws Exception
	{
		boolean reload = true;

		try
		{
			date = DateUtil.trunc(date);

			if (cache == null)
			{
				cache = new RankCache();
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
				log.debug("Caching product information for date: " + date);

				cache.loadCache();

				cacheDate = date;

				log.debug("Cached product information for date: " + cacheDate);
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

	public static RankCache getCache() throws Exception
	{
		return getCache(new Date());
	}

	private static Logger	log	= Logger.getLogger(RankFactory.class);

}
