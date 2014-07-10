/**
 * 
 */
package com.crm.product.cache;

import java.util.Date;

import org.apache.log4j.Logger;

import com.crm.product.cache.ProductCache;
import com.crm.util.DateUtil;

/**
 * @author ThangPV
 * 
 */
public class ProductFactory
{
	private static ProductCache	cache		= null;
	private static Date			cacheDate	= null;

	public ProductFactory()
	{
		super();
	}

	public synchronized static void clear() throws Exception
	{
		if (cache != null)
		{
			cache.clear();
		}
		
		cacheDate	= null;
	}
	
	public synchronized static ProductCache loadCache(Date date) throws Exception
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
				cache = new ProductCache();
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

	public synchronized static ProductCache getCache(Date date) throws Exception
	{
		boolean reload = true;

		try
		{
			date = DateUtil.trunc(date);

			if (cache == null)
			{
				cache = new ProductCache();
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

	public static ProductCache getCache() throws Exception
	{
		return getCache(new Date());
	}

	private static Logger	log	= Logger.getLogger(ProductFactory.class);

}
