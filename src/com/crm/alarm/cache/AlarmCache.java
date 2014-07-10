package com.crm.alarm.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.log4j.Logger;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.index.IndexNode;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;

public class AlarmCache
{
	// cache object
	private BinaryIndex	alarms		= new BinaryIndex();
	private BinaryIndex	templates	= new BinaryIndex();

	private Date		cacheDate	= null;

	public void clear()
	{
		alarms.clear();
	}

	public synchronized void loadCache() throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			loadAlarm(connection);
			loadActions(connection);
			loadTemplate(connection);
			
			setCacheDate(new Date());
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(connection);
		}
	}
	
	protected void loadAlarm(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			String sql = "Select * From AlarmEntry Order by alias_ desc";

			stmtConfig = connection.prepareStatement(sql);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				AlarmEntry alarm = new AlarmEntry(rsConfig.getLong("alarmId"), Database.getString(rsConfig, "alias_"));

				alarm.setTitle(Database.getString(rsConfig, "title"));
				alarm.setWaitDuration(rsConfig.getInt("waitDuration"));

				alarms.add(alarm.getAlarmId(), alarm.getIndexKey(), alarm);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsConfig);
			Database.closeObject(stmtConfig);
		}
	}
	
	protected void loadActions(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			String SQL = "Select * From AlarmAction Order by alarmId desc, description desc";

			stmtConfig = connection.prepareStatement(SQL);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				AlarmAction action = new AlarmAction();

				action.setAlarmId(rsConfig.getLong("alarmId"));
				action.setTemplateId(rsConfig.getLong("templateId"));
				action.setDescription(Database.getString(rsConfig,"description"));
				action.setMinTimes(rsConfig.getInt("minTimes"));
				action.setMaxTimes(rsConfig.getInt("maxTimes"));

				AlarmEntry alarm = getAlarm(rsConfig.getLong("alarmId"));

				alarm.getActions().add(action);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsConfig);
			Database.closeObject(stmtConfig);
		}
	}
	
	protected void loadTemplate(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			String sql = "Select * From ALARMTEMPLATE Order by templateId desc";

			stmtConfig = connection.prepareStatement(sql);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				AlarmTemplate template = new AlarmTemplate();

				template.setTemplateId(rsConfig.getLong("templateId"));
				template.setSendType(rsConfig.getString("sendType"));
				template.setSender(rsConfig.getString("sender"));
				template.setSendTo(rsConfig.getString("sendTo"));
				template.setSubject(rsConfig.getString("subject"));
				template.setContent(rsConfig.getString("content"));

				templates.add(template.getTemplateId(), template.getIndexKey(), template);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsConfig);
			Database.closeObject(stmtConfig);
		}
	}
	
	public void setCacheDate(Date cacheDate)
	{
		this.cacheDate = cacheDate;
	}

	public Date getCacheDate()
	{
		return cacheDate;
	}

	public BinaryIndex getCampaigns()
	{
		return alarms;
	}

	public void setCampaigns(BinaryIndex alarmRule)
	{
		this.alarms = alarmRule;
	}

	public AlarmEntry getAlarm(String alias) throws Exception
	{
		return (AlarmEntry) alarms.getByKey(alias.toUpperCase());
	}
	
	public AlarmEntry getAlarm(long alarmId) throws Exception
	{
		if (alarmId == Constants.DEFAULT_ID)
		{
			return null;
		}

		IndexNode node = alarms.getById(alarmId);
		
		return (AlarmEntry) node;
	}

	public AlarmTemplate getTemplate(long templateId) throws Exception
	{
		if (templateId == Constants.DEFAULT_ID)
		{
			return null;
		}
		
		IndexNode result = templates.getById(templateId);

		return (AlarmTemplate) result;
	}
	
	private static Logger	log	= Logger.getLogger(AlarmCache.class);
}
