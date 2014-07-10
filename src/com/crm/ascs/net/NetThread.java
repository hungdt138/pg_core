package com.crm.ascs.net;

public abstract class NetThread implements Runnable
{
	private boolean	stopped		= true;
	private Thread	thread		= null;
	private long	sleepTime	= 1000L;

	public long getSleepTime()
	{
		return sleepTime;
	}

	public void setSleepTime(long sleepTime)
	{
		this.sleepTime = sleepTime;
	}

	public boolean isRunning()
	{
		return !stopped;
	}

	public void start()
	{
		thread = new Thread(this);
		stopped = false;
		thread.start();
		debugMonitor("NetThread #" + thread.getId() + "[" + thread.getName() + "] started.");
	}

	public void stop()
	{
		stopped = true;
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			thread.interrupt();
		}
		debugMonitor("NetThread #" + thread.getId() + "[" + thread.getName() + "] stopped.");
	}
	
	public void stop(long timeout)
	{
		stopped = true;
		try
		{
			thread.join(timeout);
		}
		catch (InterruptedException e)
		{
			thread.interrupt();
		}
		debugMonitor("NetThread #" + thread.getId() + "[" + thread.getName() + "] stopped.");
	}

	public void destroy()
	{
		stopped = true;
		thread.interrupt();
		debugMonitor("NetThread #" + thread.getId() + "[" + thread.getName() + "] destroyed.");
	}

	@Override
	public void run()
	{
		while (isRunning())
		{
			try
			{
				process();
			}
			catch (Exception e)
			{
				debugMonitor(e);
			}
			finally
			{
				sleep(sleepTime);
			}
		}

	}

	public void sleep(long millis)
	{
		try
		{
			thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			thread.interrupt();
		}
	}

	public abstract void process() throws Exception;

	public abstract void debugMonitor(Object message);

}
