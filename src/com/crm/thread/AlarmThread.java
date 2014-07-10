package com.crm.thread;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.crm.alarm.cache.AlarmAction;
import com.crm.alarm.cache.AlarmEntry;
import com.crm.alarm.cache.AlarmFactory;
import com.crm.alarm.cache.AlarmTemplate;
import com.crm.kernel.message.AlarmMessage;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.cache.CommandEntry;
import com.crm.provisioning.cache.ProvisioningEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.thread.util.ThreadUtil;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;
import com.fss.util.AppException;

public class AlarmThread extends MailThread
{
	protected ConcurrentHashMap<Long, ProvisioningAlarm>	chmProvisioningAlarm	= null;
	private Calendar										startTime				= null;
	private int												sendInterval			= 10;

	public void setSendInterval(int sendInterval)
	{
		this.sendInterval = sendInterval;
	}

	public int getSendInterval()
	{
		return sendInterval;
	}
	
	public Calendar getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Calendar startTime)
	{
		this.startTime = startTime;
	}

	@Override
	@SuppressWarnings(value = { "unchecked", "rawtypes" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();
		vtReturn.add(ThreadUtil.createIntegerParameter("sendInterval", "Send email after each interval in second."));
		vtReturn.addAll(super.getDispatcherDefinition());
		return vtReturn;
	}

	@Override
	public void fillParameter() throws AppException
	{
		super.fillParameter();
		setSendInterval(ThreadUtil.getInt(this, "sendInterval", 0));
	}

	@Override
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		chmProvisioningAlarm = new ConcurrentHashMap<Long, AlarmThread.ProvisioningAlarm>();
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

	public AlarmMessage detachAlarm() throws Exception
	{
		Calendar now = Calendar.getInstance();
		
		if (now.getTimeInMillis() - startTime.getTimeInMillis() > 1000 * getSendInterval())
		{
			debugMonitor("now: " + now.getTimeInMillis());
			debugMonitor("start: " + startTime.getTimeInMillis());
			
			sendProvisioningAlarm();
		}

		return (AlarmMessage) QueueFactory.detachAlarm();
	}
	
	@Override
	public void doProcessSession() throws Exception
	{
		try
		{
			while (isAvailable())
			{
				AlarmMessage request = detachAlarm();
				while (isAvailable() && request != null)
				{
					try
					{
						processMessage(request);
					}
					catch (Exception e)
					{
						debugMonitor(e);
						throw e;
					}

					request = detachAlarm();
				}

				ThreadUtil.sleep(this);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void sendProvisioningAlarm() throws Exception
	{
		Set<Long> keys = chmProvisioningAlarm.keySet();

		Iterator<Long> iterator = keys.iterator();
		while (iterator.hasNext())
		{
			long alarmId = iterator.next();
			ProvisioningAlarm provisioningAlarm = chmProvisioningAlarm.get(alarmId);

			try
			{
				AlarmEntry alarmEntry = AlarmFactory.getCache().getAlarm(provisioningAlarm.getAlarmId());
				
				debugMonitor("waitDuration: " + alarmEntry.getWaitDuration());
				debugMonitor("Alarm total: " + provisioningAlarm.getCount());
				
				if (alarmEntry.getWaitDuration() <= provisioningAlarm.getCount())
				{
					sendAlarmSMS(provisioningAlarm, alarmEntry);
					sendAlarmMail(provisioningAlarm, alarmEntry);
				}
				else
				{
					debugMonitor("Alarm total: " + provisioningAlarm.getCount());
				}

				chmProvisioningAlarm.remove(alarmId);
			}
			catch (Exception e)
			{
				throw e;
			}
		}
		setStartTime(Calendar.getInstance());
		storeConfig();
	}

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
				ProvisioningAlarm prAlarm = chmProvisioningAlarm.get(alarm.getAlarmId());
				if (prAlarm == null)
				{
					prAlarm = new ProvisioningAlarm();
					prAlarm.setStartTime(Calendar.getInstance());
					prAlarm.setAlarmId(alarm.getAlarmId());
					prAlarm.setAlarm(alarm);
				}
				else
				{
					prAlarm.setCount(prAlarm.getCount() + 1);
				}

				chmProvisioningAlarm.put(alarm.getAlarmId(), prAlarm);
			}
		}
		else
			super.processMessage(request);
	}
	
	protected void sendAlarmMail(ProvisioningAlarm alarm, AlarmEntry alarmEntry) throws Exception
	{
		AlarmAction alarmAction = null;
		AlarmTemplate template = null;
		try
		{
			alarmAction = alarmEntry.getAlarmAction(alarmEntry.getAlarmId(), "EMAIL");
			template = AlarmFactory.getCache().getTemplate(alarmAction.getTemplateId());
		}
		catch (Exception e)
		{
			debugMonitor(e.getMessage());
			e.printStackTrace();
		}
		
		if (template != null)
		{
			debugMonitor("Sending alarm mail");
			
			Calendar now = Calendar.getInstance();
			
			String strContent = template.getContent();
			
			strContent = strContent.replaceAll("~START_DATE~", StringUtil.format(alarm.getStartTime().getTime(), "dd/MM/yyyy HH:mm:ss"))
								   .replaceAll("~END_DATE~", StringUtil.format(now.getTime(), "dd/MM/yyyy HH:mm:ss"))
								   .replaceAll("~COUNT~", "" + alarm.count)
								   .replaceAll("~DESCRIPTION~", StringUtil.nvl(alarm.getAlarm().getCause(), ""))
								   .replaceAll("~DETAIL~", StringUtil.nvl(alarm.getAlarm().getContent(), ""));

			sendEmail(template.getSubject() + " - " + alarm.getAlarm().getCause(), template.getSender(), template.getSendTo(), strContent, null);

			debugMonitor("Finish send alarm mail");
		}
		else
		{
			debugMonitor("Dont have template alarm SMS for this alarm");
		}
	}

	protected void sendAlarmSMS(ProvisioningAlarm alarm, AlarmEntry alarmEntry) throws Exception
	{
		AlarmAction alarmAction = null;
		AlarmTemplate template = null;
		try
		{
			alarmAction = alarmEntry.getAlarmAction(alarmEntry.getAlarmId(), "SMS");
			template = AlarmFactory.getCache().getTemplate(alarmAction.getTemplateId());
		}
		catch (Exception e)
		{
			debugMonitor(e.getMessage());
			e.printStackTrace();
		}
		
		if (template != null)
		{
			debugMonitor("Sending alarm SMS");
			
			String[] isdns = template.getSendTo().split(StringPool.COMMA);
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
	
					request.setServiceAddress(template.getSender());
					request.setShipTo(isdn);
					request.setIsdn(isdn);
					
					Calendar now = Calendar.getInstance();
					String strContent = template.getContent();
					strContent = strContent.replaceAll("~START_DATE~", StringUtil.format(alarm.getStartTime().getTime(), "dd/MM/yyyy HH:mm:ss"))
										   .replaceAll("~END_DATE~", StringUtil.format(now.getTime(), "dd/MM/yyyy HH:mm:ss"))
										   .replaceAll("~COUNT~", "" + alarm.count)
										   .replaceAll("~DESCRIPTION~", StringUtil.nvl(alarm.getAlarm().getCause(), ""))
										   .replaceAll("~DETAIL~", StringUtil.nvl(alarm.getAlarm().getContent(), ""));
					request.setRequest(strContent);
	
					request.setKeyword("ALARM");
	
					request.setProvisioningType(Constants.PROVISIONING_SMSC);
					request.setCommandId(command.getCommandId());
					request.setRequestValue(ResponseUtil.SMS_CMD_CHECK, "false");
					
					QueueFactory.attachCommandRouting(request);
				}
				
				if (!sentAlarmIsdn.equals(""))
					debugMonitor("Sent alarm SMS to: " + sentAlarmIsdn); 
			}
			catch (Exception e)
			{
				throw e;
			}
			
			debugMonitor("Finish send alarm SMS");
		}
		else
		{
			debugMonitor("Dont have template alarm SMS for this alarm");
		}
	}

	protected class ProvisioningAlarm
	{
		private long			alarmId			= 0;
		private AlarmMessage	alarm			= null;
		private Calendar		startTime		= Calendar.getInstance();
		private int				count			= 1;

		public long getAlarmId()
		{
			return alarmId;
		}

		public void setAlarmId(long alarmId)
		{
			this.alarmId = alarmId;
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
