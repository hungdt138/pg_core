package com.crm.smscsim.thread;

import java.io.FileInputStream;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.crm.smscsim.ISession;
import com.crm.smscsim.SMSCException;
import com.crm.smscsim.SMSCServer;
import com.crm.smscsim.util.FileParser;
import com.crm.smscsim.util.SMSCUser;
import com.crm.thread.DispatcherInstance;
import com.crm.thread.DispatcherThread;
import com.crm.thread.util.ThreadUtil;
import com.fss.util.AppException;
import com.logica.smpp.Data;
import com.logica.smpp.pdu.BindRequest;

public class SMSCThread extends DispatcherThread
{
	public int									listenPort			= 3000;
	public int									networkTimeout		= 3000;
	public long									receiveTimeout		= 10000;
	public String								userFilePath		= "";
	public SMSCServer							server				= null;
	public int									connectionCount		= 0;
	private ConcurrentHashMap<String, SMSCUser>	users				= new ConcurrentHashMap<String, SMSCUser>();
	private ConcurrentHashMap<String, Integer>	usersCount			= new ConcurrentHashMap<String, Integer>();

	private Object								instanceLockObject	= new Object();

	@Override
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.add(ThreadUtil.createIntegerParameter("listenPort", "Listen port."));
		vtReturn.add(ThreadUtil.createIntegerParameter("maxConnection", "Max connection that server can handle."));
		vtReturn.add(ThreadUtil.createIntegerParameter("networkTimeout",
				"Time out to wait for accept connection or read from client."));
		vtReturn.add(ThreadUtil
				.createLongParameter("receiveTimeout",
						"Max time (millisecond) between 2 times when client send request before detect that client disconnected."));
		vtReturn.add(ThreadUtil.createTextParameter("userFilePath", 400,
				"User file path, if file does not exist, use default user: nms/nms."));
		vtReturn.addAll(super.getParameterDefinition());
		return vtReturn;
	}

	@Override
	public void fillDispatcherParameter() throws AppException
	{
		listenPort = ThreadUtil.getInt(this, "listenPort", 3000);
		networkTimeout = ThreadUtil.getInt(this, "networkTimeout", 1000);
		receiveTimeout = ThreadUtil.getLong(this, "receiveTimeout", 10000);
		userFilePath = ThreadUtil.getString(this, "userFilePath", false, "");

		super.fillDispatcherParameter();
	}

	@Override
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();
		startServer();
	}

	private void startServer()
	{
		loadUsers(userFilePath);
		server = new SMSCServer(listenPort);
		server.setReceiveTimeout(networkTimeout);
		server.setSleepTime(getDelayTime());
		server.setDispatcher(this);
		server.setMaxConnection(this.instanceSize);
		// server.setProcessorFactory(factory);
		server.start();
	}

	@Override
	public void afterProcessSession() throws Exception
	{
		try
		{
			super.afterProcessSession();
		}
		finally
		{
			try
			{
				closeAllInstanceSession();
				server.stop();
			}
			catch (Exception e)
			{
				debugMonitor(e);
			}
			finally
			{
				server = null;
			}
		}
	}

	/**
	 * Set default 1 user: nms/nms
	 */
	public void loadUsers()
	{
		SMSCUser user = new SMSCUser("nms", "nms", 0);
		users.put(user.getUserId().toUpperCase(), user);
	}

	public void loadUsers(String filePath)
	{
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(filePath);
			FileParser parser = new FileParser(users);
			parser.parse(is);
		}
		catch (Exception e)
		{
			debugMonitor(e);
			loadUsers();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (Exception e)
			{

			}
		}
	}

	public void increaseUserConnectionCount(String user)
	{
		synchronized (usersCount)
		{
			Integer count = usersCount.get(user);
			if (count == null)
				count = new Integer(0);
			count++;
			connectionCount++;
			usersCount.put(user, count);
		}
	}

	public int getUserConnectionCount(String user)
	{
		synchronized (usersCount)
		{
			Integer count = usersCount.get(user);
			if (count != null)
				return 0;
			return count;
		}
	}

	public void decreaseUserConnectionCount(String user)
	{
		synchronized (usersCount)
		{
			Integer count = usersCount.get(user);
			if (count != null)
			{
				if (count > 0)
				{
					count--;
					connectionCount--;
					usersCount.put(user, count);
				}
			}
		}
	}

	public int authenticate(SMSCInstance instance, BindRequest request)
	{
		String systemId = request.getSystemId();
		String password = request.getPassword();
		int commandId = request.getCommandId();

		int commandStatus = Data.ESME_ROK;
		SMSCUser user = users.get(systemId.toUpperCase());
		if (user != null)
		{
			if (!user.getPassword().equals(password))
			{
				commandStatus = Data.ESME_RINVPASWD;
				debugMonitor("system id " + systemId +
								" not authenticated. Invalid password.");
			}
			else
			{
				if (user.getConnectionLimit() > 0 && getUserConnectionCount(user.getUserId()) >= user.getConnectionLimit())
				{
					commandStatus = Data.ESME_RBINDFAIL;
					debugMonitor("system id " + systemId + " not authenticated, has max connection.");
				}
				else if (commandId == Data.BIND_TRANSCEIVER && !user.isTranceiverEnabled())
				{
					commandStatus = Data.ESME_RBINDFAIL;
					debugMonitor("system id " + systemId + " not authenticated, tranceiver is not allowed to bind.");
				}
				else if (commandId == Data.BIND_TRANSMITTER && !user.isTransmitterEnabled())
				{
					commandStatus = Data.ESME_RBINDFAIL;
					debugMonitor("system id " + systemId + " not authenticated, transmitter is not allowed to bind.");
				}
				else if (commandId == Data.BIND_RECEIVER && !user.isReceiverEnabled())
				{
					commandStatus = Data.ESME_RBINDFAIL;
					debugMonitor("system id " + systemId + " not authenticated, receiver is not allow to bind.");
				}
				else
				{
					debugMonitor("system id " + systemId + " authenticated");
					increaseUserConnectionCount(systemId);
					instance.setUser(user);
				}
			}
		}
		else
		{
			commandStatus = Data.ESME_RINVSYSID;
			debugMonitor("system id " + systemId +
						" not authenticated -- not found");
		}

		return commandStatus;
	}

	public void putToFreeInstance(ISession session, int id) throws SMSCException
	{
		synchronized (instanceLockObject)
		{

			SMSCInstance smscInstance = null;
			for (DispatcherInstance instance : getInstances())
			{
				if (((SMSCInstance) instance).getUser() == null)
				{
					smscInstance = (SMSCInstance) instance;
					break;
				}
			}
			if (smscInstance == null)
			{
				session.endSession();
				throw new SMSCException("there is not any free space for connection.");
			}
			session.setPDUListener(smscInstance);
			smscInstance.setReceiveTimeout(receiveTimeout);
			smscInstance.setSession(session, id);
			debugMonitor("Client #" + smscInstance.getProcessorId() + " connected.");
		}
	}

	public void closeAllInstanceSession()
	{
		synchronized (instanceLockObject)
		{
			for (DispatcherInstance instance : getInstances())
			{
				if (((SMSCInstance) instance).getUser() != null)
				{
					try
					{
						((SMSCInstance) instance).close();
					}
					catch (Exception e)
					{
						debugMonitor(e);
					}
				}
			}
		}
	}
}
