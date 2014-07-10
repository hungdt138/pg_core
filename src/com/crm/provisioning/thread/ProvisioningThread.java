/**
 * 
 */
package com.crm.provisioning.thread;

import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSession;

import com.crm.alarm.cache.AlarmEntry;
import com.crm.alarm.cache.AlarmFactory;
import com.crm.kernel.domain.DomainFactory;
import com.crm.kernel.message.AlarmMessage;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.marketing.cache.CampaignFactory;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.cache.ProvisioningEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.cache.ProvisioningPool;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.OSACallbackMessage;
import com.crm.provisioning.util.CommandUtil;
import com.crm.thread.DatasourceThread;

import com.crm.thread.util.ThreadUtil;
import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class ProvisioningThread extends DatasourceThread
{
	// constant
	public String									alias							= "";
	public ProvisioningEntry						provisioning					= null;

	public String									host							= "";
	public int										port							= 0;
	public String									userName						= "";
	public String									password						= "";

	public String									provisioningClass				= "";
	public int										maxActive						= 30;
	public long										maxWait							= 30000;
	public int										maxIdle							= 30000;
	public int										minIdle							= -1;
	public boolean									testOnBorrow					= true;
	public boolean									testOnReturn					= true;
	public long										timeBetweenEvictionRunsMillis	= 3000;
	public int										numTestsPerEvictionRun			= 3;
	public long										minEvictableIdleTimeMillis		= -1;
	public boolean									testWhileIdle					= true;
	public long										softMinEvictableIdleTimeMillis	= -1;
	public boolean									lifo							= true;
	public String									testCommand						= "";
	public int										timeout							= 30000;
	public boolean									useSimulation					= false;
	public long										simulationTime					= 1000;
	public String									simulationCause					= "";

	public ProvisioningPool							provisioningPool				= null;

	public Queue									queueCommandRouting				= null;

	public int										maxLocalQueueSize				= 0;

	public ProvisioningThread()
	{
		super();
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addAll(ThreadUtil.createProvisioningParameter(this));
		vtReturn.add(ThreadUtil.createBooleanParameter("simulationMode", "Use simulation or not"));
		vtReturn.add(ThreadUtil.createLongParameter("simulationExecuteTime", "Simulation time in millisecond."));
		vtReturn.add(ThreadUtil.createTextParameter("simulationCause", 400, "Response cause after using simulation."));
		vtReturn.addAll(ThreadUtil.createQueueParameter(this));
		vtReturn.addAll(ThreadUtil.createInstanceParameter(this));
		vtReturn.addAll(ThreadUtil.createLogParameter(this));

		return vtReturn;
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillParameter() throws AppException
	{
		try
		{
			super.fillParameter();

			alias = ThreadUtil.getString(this, "provisioning", true, "");

			host = ThreadUtil.getString(this, "host", true, "");
			port = ThreadUtil.getInt(this, "port", 0);
			userName = ThreadUtil.getString(this, "userName", true, "");
			password = ThreadUtil.getString(this, "password", true, "");

			provisioningClass = ThreadUtil.getString(this, "provisioningClass", true, "");

			maxLocalQueueSize = ThreadUtil.getInt(this, "maxLocalQueueSize", 0);
			
			maxActive = ThreadUtil.getInt(this, "maxActive", 30);
			maxWait = ThreadUtil.getInt(this, "maxWait", 30000);
			maxIdle = ThreadUtil.getInt(this, "maxIdle", 30000);
			minIdle = ThreadUtil.getInt(this, "minIdle", -1);
			testOnBorrow = ThreadUtil.getBoolean(this, "testOnBorrow", true);
			testOnReturn = ThreadUtil.getBoolean(this, "testOnReturn", true);
			timeBetweenEvictionRunsMillis = ThreadUtil.getInt(this, "timeBetweenEvictionRunsMillis", 3000);
			numTestsPerEvictionRun = ThreadUtil.getInt(this, "numTestsPerEvictionRun", 3);
			minEvictableIdleTimeMillis = ThreadUtil.getInt(this, "minEvictableIdleTimeMillis", -1);
			testWhileIdle = ThreadUtil.getBoolean(this, "testWhileIdle", true);
			softMinEvictableIdleTimeMillis = ThreadUtil.getInt(this, "softMinEvictableIdleTimeMillis", -1);
			lifo = ThreadUtil.getBoolean(this, "lifo", true);

			testCommand = ThreadUtil.getString(this, "testCommand", false, "");
			timeout = ThreadUtil.getInt(this, "timeout", 30000);
			useSimulation = ThreadUtil.getBoolean(this, "simulationMode", false);
			simulationTime = ThreadUtil.getLong(this, "simulationExecuteTime", 1000);
			simulationCause = ThreadUtil.getString(this, "simulationCause", false, "");
		}
		catch (AppException e)
		{
			logMonitor(e);

			throw e;
		}
		catch (Exception e)
		{
			logMonitor(e);

			throw new AppException(e.getMessage());
		}
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void initProvisioningParameters() throws Exception
	{
		try
		{
			provisioningPool.setHost(host);
			provisioningPool.setPort(port);
			provisioningPool.setUserName(userName);
			provisioningPool.setPassword(password);
			provisioningPool.setTimeout(timeout);
			provisioningPool.setTestCommand(testCommand);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void loadCache() throws Exception
	{
		try
		{
			Date now = new Date();

			DomainFactory.loadCache(now);
			ProductFactory.loadCache(now);
			ProvisioningFactory.loadCache(now);
			CampaignFactory.loadCache(now);
			AlarmFactory.loadCache(now);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		try
		{
			provisioning = ProvisioningFactory.getCache().getProvisioning(alias);

			CommandUtil.closeConnection(provisioningPool);

			if (provisioningPool == null)
			{
				provisioningPool = new ProvisioningPool();
			}

			provisioningPool.setDispatcher(this);
			provisioningPool.setProvisioningId(provisioning.getProvisioningId());
			provisioningPool.setProvisioningClass(provisioningClass);

			initProvisioningParameters();

			provisioningPool.open(maxActive, maxWait, maxIdle, minIdle, testOnBorrow, testOnReturn
					, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, minEvictableIdleTimeMillis
					, testWhileIdle, softMinEvictableIdleTimeMillis);
		}
		catch (Exception e)
		{
			throw e;
		}

		ProvisioningConnection testConnection = null;

		try
		{
			logMonitor("Testing connection ...");

			testConnection = provisioningPool.getConnection();

			logMonitor("Testing connection are success");
		}
		catch (Exception e)
		{
			logMonitor("open connection is fail");

			logMonitor(e);
		}
		finally
		{
			provisioningPool.closeConnection(testConnection);
		}

		super.beforeProcessSession();
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void afterProcessSession() throws Exception
	{
		try
		{
			if (provisioningPool != null)
			{
				provisioningPool.close();
			}
		}
		finally
		{
			super.afterProcessSession();
		}
	}

	public ProvisioningPool getProvisioningPool()
	{
		return provisioningPool;
	}

	public void setProvisioningPool(ProvisioningPool provisioningPool)
	{
		this.provisioningPool = provisioningPool;
	}
	
	public void sendInstanceAlarm(Exception e, String cause, long provisioningId, String provisioningClass) throws Exception
	{
		if (!alarmEnable)
		{
			return;
		}
		
		AlarmEntry alarmEntry = AlarmFactory.getCache().getAlarm(cause);
		if (alarmEntry == null)
		{
			return;
		}
		
		AlarmMessage alarm = new AlarmMessage();
		alarm.setAlarmId(alarmEntry.getAlarmId());
		alarm.setCause(cause);
		alarm.setProvisioningId(provisioningId);
		alarm.setProvisioningClass(provisioningClass);

		StackTraceElement[] stackTraces = e.getStackTrace();
		String content = "";
		for (StackTraceElement stackTrace : stackTraces)
		{
			content += stackTrace.toString() + "\r\n";
		}

		alarm.setDescription(e.getMessage());
		alarm.setContent(content);

		sendInstanceAlarm(alarm);
	}
	
	public void sendInstanceAlarm(String cause, long provisioningId) throws Exception
	{
		if (!alarmEnable)
		{
			return;
		}
		
		AlarmEntry alarmEntry = AlarmFactory.getCache().getAlarm(cause);
		if (alarmEntry == null)
		{
			return;
		}
		
		AlarmMessage alarm = new AlarmMessage();
		alarm.setAlarmId(alarmEntry.getAlarmId());
		alarm.setCause(cause);
		alarm.setProvisioningId(provisioningId);

		sendInstanceAlarm(alarm);
	}
	
	public void sendInstanceAlarm(AlarmMessage request) throws Exception
	{
		try
		{
			QueueFactory.attachAlarm(request);
			// sendMessage(queueCommandRouting, request, 0);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
}
