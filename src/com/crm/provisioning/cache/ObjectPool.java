package com.crm.provisioning.cache;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public abstract class ObjectPool implements PoolableObjectFactory
{
	private GenericObjectPool	objectPool	= null;

	private boolean				closed		= true;

	public abstract void debugMonitor(Object message);

	protected abstract GenericObjectPool initPool() throws Exception;

	protected abstract Object createObject() throws Exception;

	public void open() throws Exception
	{
		objectPool = initPool();
		closed = false;
	}

	protected GenericObjectPool getPool() throws Exception
	{
		return objectPool;
	}

	public boolean isClosed()
	{
		return closed;
	}

	public PoolableObject getObject() throws Exception
	{
		return (PoolableObject) getPool().borrowObject();
	}

	public void returnObject(PoolableObject object)
	{
		if (object != null && objectPool != null)
		{
			try
			{
				objectPool.returnObject(object);
			}
			catch (Exception e)
			{
				debugMonitor(e);
				object.destroy();
			}
		}
	}

	@Override
	public void activateObject(Object poolableObject) throws Exception
	{
		if (poolableObject instanceof PoolableObject)
		{
			((PoolableObject) poolableObject).activate();
		}

	}

	@Override
	public void destroyObject(Object poolableObject) throws Exception
	{
		if (poolableObject != null)
		{
			// debugMonitor("Close queue connection");
			((PoolableObject) poolableObject).destroy();
		}
	}

	@Override
	public Object makeObject() throws Exception
	{
		return createObject();
	}

	@Override
	public void passivateObject(Object poolableObject) throws Exception
	{
		if (poolableObject instanceof PoolableObject)
		{
			((PoolableObject) poolableObject).passivate();
		}
	}

	@Override
	public boolean validateObject(Object poolableObject)
	{
		if (poolableObject instanceof PoolableObject)
		{
			try
			{
				return ((PoolableObject) poolableObject).validate();
			}
			catch (Exception e)
			{
				debugMonitor(e);
			}
		}

		return false;
	}

	public void destroyPool() throws Exception
	{
		try
		{
			if (objectPool != null)
			{
				debugMonitor("Close queue connection pool.");
				objectPool.clear();
				objectPool.close();
			}
		}
		finally
		{
			closed = true;
		}
	}

}
