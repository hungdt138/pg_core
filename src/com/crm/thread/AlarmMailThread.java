package com.crm.thread;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Message;

import com.crm.kernel.message.AlarmMessage;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.cache.CommandEntry;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.cache.ProvisioningEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.thread.util.ThreadUtil;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;
import com.fss.util.AppException;

public class AlarmMailThread extends MailThread
{
	protected ConcurrentHashMap<Long, ProvisioningAlarm>	chmProvisioningAlarm	= null;
	private Calendar										startTime				= null;
	private int												sendInterval			= 10;
	private String											isdnList				= "";

	public void setIsdnList(String isdnList)
	{
		this.isdnList = isdnList;
	}

	public String getIsdnList()
	{
		return isdnList;
	}

	public void setSendInterval(int sendInterval)
	{
		this.sendInterval = sendInterval;
	}

	public int getSendInterval()
	{
		return sendInterval;
	}

	@Override
	@SuppressWarnings(value = { "unchecked", "rawtypes" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();
		vtReturn.add(ThreadUtil.createIntegerParameter("sendInterval", "Send email after each interval in second."));
		vtReturn.add(ThreadUtil.createTextParameter("isdnList", 400, "List of destination isdn."));
		vtReturn.addAll(super.getDispatcherDefinition());
		return vtReturn;
	}

	@Override
	public void fillParameter() throws AppException
	{
		super.fillParameter();
		setSendInterval(ThreadUtil.getInt(this, "sendInterval", 0));
		setIsdnList(ThreadUtil.getString(this, "isdnList", false, ""));
	}

	@Override
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		chmProvisioningAlarm = new ConcurrentHashMap<Long, AlarmMailThread.ProvisioningAlarm>();
		startTime = Calendar.getInstance();
	}

	@Override
	public void afterProcessSession() throws Exception
	{
		try
		{
			sendProvisioningAlarm();
		}
		finally
		{
			chmProvisioningAlarm = null;
			startTime = null;
		}
		super.afterProcessSession();
	}

	@Override
	public Message detachMessage() throws Exception
	{
		Calendar now = Calendar.getInstance();
		if (now.getTimeInMillis() - startTime.getTimeInMillis() > 1000 * getSendInterval())
		{
			sendProvisioningAlarm();
		}

		return super.detachMessage();
	}

	public void sendProvisioningAlarm() throws Exception
	{
		Set<Long> keys = chmProvisioningAlarm.keySet();

		Iterator<Long> iterator = keys.iterator();
		Calendar now = Calendar.getInstance();
		while (iterator.hasNext())
		{
			Long provisioningId = iterator.next();
			ProvisioningAlarm provisioningAlarm = chmProvisioningAlarm.get(provisioningId);

			String strContent = formatContent(provisioningAlarm.getAlarm());
			String strSubject = formatSubject(provisioningAlarm.getAlarm());

			try
			{
				strContent = "StartTime: " + StringUtil.format(provisioningAlarm.getStartTime().getTime(), "dd/MM/yyyy HH:mm:ss")
						+ "\r\n"
						+ "EndTime: " + StringUtil.format(now.getTime(), "dd/MM/yyyy HH:mm:ss") + "\r\n"
						+ "Count: " + provisioningAlarm.count + "\r\n"
						+ strContent;

				sendEmail(strSubject, strContent, "");

				chmProvisioningAlarm.remove(provisioningId);
				startTime = Calendar.getInstance();
			}
			catch (Exception e)
			{
				throw e;
			}
		}
	}

	@Override
	public String formatContent(Object request)
	{
		if (request instanceof AlarmMessage)
		{
			AlarmMessage alarm = (AlarmMessage) request;
			String message = "Description:\r\n" + alarm.getDescription() + "\r\n"
					+ "Detail:\r\n" + alarm.getContent() + "\r\n";

			if (alarm.getProvisioningId() != 0)
			{
				try
				{
					ProvisioningEntry provisioningEntry = ProvisioningFactory.getCache().getProvisioning(
							alarm.getProvisioningId());
					if (provisioningEntry != null)
						message = "Provisioning: " + provisioningEntry.getAlias() + "-" + alarm.getProvisioningClass() + "\r\n"
								+ message;
				}
				catch (Exception e)
				{
				}
			}
			return message;
		}

		return request.toString();
	}

	@Override
	public String formatSubject(Object request)
	{
		if (request instanceof AlarmMessage)
		{
			AlarmMessage alarm = (AlarmMessage) request;
			String subject = super.formatSubject(request) + " - " + alarm.getCause();

			if (alarm.getProvisioningId() != 0)
			{
				try
				{
					ProvisioningEntry provisioningEntry = ProvisioningFactory.getCache().getProvisioning(
							alarm.getProvisioningId());
					if (provisioningEntry != null)
						subject += " - " + provisioningEntry.getAlias() + "\r\n";
				}
				catch (Exception e)
				{
				}
			}

			return subject;
		}
		return super.formatSubject(request);
	}

	@Override
	public void processMessage(Object request) throws Exception
	{
		if (request instanceof AlarmMessage)
		{
			AlarmMessage alarm = (AlarmMessage) request;

			if (alarm.isImmediately())
			{
				String strSubject = formatSubject(alarm);
				String strContent = formatContent(alarm);
				strContent = "Time: " + StringUtil.format(alarm.getRequestTime(), "dd/MM/yyyy HH:mm:ss") + "\r\n" + strContent;

				sendEmail(strSubject, strContent, "");
			}
			else
			{
				ProvisioningAlarm prAlarm = chmProvisioningAlarm.get(alarm.getProvisioningId());
				if (prAlarm == null)
				{
					prAlarm = new ProvisioningAlarm();
					prAlarm.setAlarm(alarm);
					prAlarm.setProvisioningId(alarm.getProvisioningId());
				}
				else
				{
					prAlarm.setCount(prAlarm.getCount() + 1);
				}

				chmProvisioningAlarm.put(alarm.getProvisioningId(), prAlarm);
			}
		}
		else
			super.processMessage(request);
	}

	@Override
	public void sendEmail(String strSubject, String strContent, String strFileName) throws Exception
	{
		try
		{
			super.sendEmail(strSubject, strContent, strFileName);
		}
		finally
		{
			sendAlarmSMS(strContent);
		}
	}

	protected void sendAlarmSMS(String content) throws Exception
	{
		String[] isdns = getIsdnList().split(StringPool.COMMA);
		String sentAlarmIsdn = "";
		try
		{
			CommandEntry command = ProvisioningFactory.getCache().getCommand(Constants.COMMAND_SEND_SMS);

			for (String isdn : isdns)
			{
				if (isdn.equals(""))
				{
					continue;
				}
				sentAlarmIsdn += isdn + ",";
				CommandMessage request = new CommandMessage();
				request.setChannel(Constants.CHANNEL_SMS);
				request.setUserId(0);
				request.setUserName("system");

				request.setServiceAddress("123");
				request.setShipTo(isdn);
				request.setIsdn(isdn);
				request.setRequest(content);

				request.setKeyword("ALARM");

				request.setProvisioningType(Constants.PROVISIONING_SMSC);
				request.setCommandId(command.getCommandId());
				request.setRequestValue(ResponseUtil.SMS_CMD_CHECK, "false");

				//QueueFactory.sendObjectMessage(this.queueSession, QueueFactory.COMMAND_ROUTE_QUEUE, request);
				
//				MQConnection connection = null;
//				try
//				{
//					connection = getMQConnection();
//					connection.sendMessage(request, QueueFactory.COMMAND_ROUTE_QUEUE, 0);
//				}
//				finally
//				{
//					returnMQConnection(connection);
//				}
				
				QueueFactory.attachCommandRouting(request);
			}
			
			if (!sentAlarmIsdn.equals(""))
				debugMonitor("Sent alarm SMS to: " + sentAlarmIsdn); 
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	protected class ProvisioningAlarm
	{
		private long			provisioningId	= 0;
		private AlarmMessage	alarm			= null;
		private Calendar		startTime		= Calendar.getInstance();
		private int				count			= 1;

		public long getProvisioningId()
		{
			return provisioningId;
		}

		public void setProvisioningId(long provisioningId)
		{
			this.provisioningId = provisioningId;
		}

		public AlarmMessage getAlarm()
		{
			return alarm;
		}

		public void setAlarm(AlarmMessage alarm)
		{
			this.alarm = alarm;
		}

		public Calendar getStartTime()
		{
			return startTime;
		}

		public void setStartTime(Calendar startTime)
		{
			this.startTime = startTime;
		}

		public void setCount(int count)
		{
			this.count = count;
		}

		public int getCount()
		{
			return count;
		}
	}
}
