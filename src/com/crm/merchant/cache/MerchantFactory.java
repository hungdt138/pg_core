/**
 * 
 */
package com.crm.merchant.cache;

import java.util.Date;

import com.crm.util.DateUtil;

/**
 * @author hungdt
 *
 */
public class MerchantFactory {
	private static MerchantCache	cache		= null;
	private static Date				cacheDate	= null;

	public MerchantFactory()
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

	public synchronized static MerchantCache loadCache(Date date) throws Exception
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
				cache = new MerchantCache();
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

	public synchronized static MerchantCache getCache(Date date) throws Exception
	{
		boolean reload = true;

		try
		{
			date = DateUtil.trunc(date);

			if (cache == null)
			{
				cache = new MerchantCache();
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

	public static MerchantCache getCache() throws Exception
	{
		return getCache(new Date());
	}

	public static Date getCacheDate()
	{
		return cacheDate;
	}

	public static void setCacheDate(Date cacheDate)
	{
		MerchantFactory.cacheDate = cacheDate;
	}
}
