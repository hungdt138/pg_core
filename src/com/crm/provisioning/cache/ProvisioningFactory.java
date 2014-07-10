/**
 * 
 */
package com.crm.provisioning.cache;

import java.util.Date;

import com.crm.provisioning.cache.ProvisioningCache;
import com.crm.util.DateUtil;

/**
 * @author ThangPV
 * 
 */
public class ProvisioningFactory
{
	// Field descriptor #178 B
	public static final byte						WHEN_EXHAUSTED_FAIL					= 0;

	// Field descriptor #178 B
	public static final byte						WHEN_EXHAUSTED_BLOCK				= 1;

	// Field descriptor #178 B
	public static final byte						WHEN_EXHAUSTED_GROW					= 2;

	// Field descriptor #186 I
	public static final int							MAX_IDLE							= 8;

	// Field descriptor #186 I
	public static final int							MIN_IDLE							= 0;

	// Field descriptor #186 I
	public static final int							MAX_ACTIVE							= 8;

	// Field descriptor #178 B
	public static final byte						WHEN_EXHAUSTED_ACTION				= 1;

	// Field descriptor #192 Z
	public static final boolean						LIFO								= true;

	// Field descriptor #194 J
	public static final long						MAX_WAIT							= 1000;

	// Field descriptor #192 Z
	public static final boolean						TEST_ON_BORROW						= false;

	// Field descriptor #192 Z
	public static final boolean						TEST_ON_RETURN						= false;

	// Field descriptor #192 Z
	public static final boolean						TEST_WHILE_IDLE						= false;

	// Field descriptor #194 J
	public static final long						TIME_BETWEEN_EVICTION_RUNS_MILLIS	= 100;

	// Field descriptor #186 I
	public static final int							NUM_TESTS_PER_EVICTION_RUN			= 3;

	// Field descriptor #194 J
	public static final long						MIN_EVICTABLE_IDLE_TIME_MILLIS		= 60000L;

	// Field descriptor #194 J
	public static final long						SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS	= -1L;

	public final static int							MAX_RETRY_GET_INSTANCE				= 3;

	public final static int							SLEEP_TIME_GET_INSTANCE				= 100;

	private static ProvisioningCache				cache								= null;
	private static Date								cacheDate							= null;

	public ProvisioningFactory()
	{
		super();
	}

	protected synchronized static void clear() throws Exception
	{
		if (cache != null)
		{
			cache.clear();
		}

		cacheDate = null;
	}

	public synchronized static ProvisioningCache loadCache(Date date) throws Exception
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
				cache = new ProvisioningCache();
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

	public synchronized static ProvisioningCache getCache(Date date) throws Exception
	{
		boolean reload = true;

		try
		{
			date = DateUtil.trunc(date);

			if (cache == null)
			{
				cache = new ProvisioningCache();
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

	public static ProvisioningCache getCache() throws Exception
	{
		return getCache(new Date());
	}
}
