/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.crm.thread.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.crm.kernel.sql.Database;
import com.crm.util.StringUtil;

public class ServerConfiguration
{
	private HashMap<String, ThreadConfiguration>	configs	= new HashMap<String, ThreadConfiguration>();

	public ThreadConfiguration getThreadConfig(String alias)
	{
		return configs.get(alias);
	}

	public synchronized void storeThreadConfig(ThreadConfiguration config)
	{
	}

	public synchronized void clear() throws Exception
	{
		configs.clear();
	}

	public synchronized void loadThreadConfig() throws Exception
	{
		Connection connection = null;
		PreparedStatement stmtThread = null;
		ResultSet rsThread = null;

		try
		{
			log.debug("Loading service configuration ... ");
			clear();

			String SQL = "Select A.alias_, A.title, B.* From ThreadEntry A, ThreadRule B Where A.threadId = B.threadId";

			connection = Database.getConnection();
			stmtThread = connection.prepareStatement(SQL);
			rsThread = stmtThread.executeQuery();

			while (rsThread.next())
			{
				configs.put(rsThread.getString("alias_"), loadThreadConfig(rsThread));
			}

			log.debug("Finish load service configuration");
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsThread);
			Database.closeObject(stmtThread);
			Database.closeObject(connection);
		}
	}

	public ThreadConfiguration loadThreadConfig(ResultSet rsThread) throws Exception
	{
		ThreadConfiguration config = new ThreadConfiguration();

		try
		{
			config.setAlias(rsThread.getString("alias_"));

			config.setThreadId(rsThread.getLong("threadId"));
			config.setName(rsThread.getString("title"));
			config.setInstanceClassName(rsThread.getString("instanceClass"));
			config.setStartupType(rsThread.getString("startupType"));

			config.setStartTime(StringUtil.nvl(rsThread.getString("startTime"), "00:00:00"));
			config.setEndTime(StringUtil.nvl(rsThread.getString("endTime"), "23:59:59"));
			config.setDayAvailables("");
			config.setMonthAvailables("");
			config.setDelayTime(rsThread.getInt("interval_"));

			// parameters
			// config.getParameters().load(StringUtil.nvl(rsThread.getString("properties"), ""));

			log.debug("Configuration for thread " + config.getAlias() + " are loaded");
		}
		catch (Exception e)
		{
			throw e;
		}

		return config;
	}

	public void setConfigs(HashMap<String, ThreadConfiguration> configs)
	{
		this.configs = configs;
	}

	public HashMap<String, ThreadConfiguration> getConfigs()
	{
		return configs;
	}

	private static Logger	log	= Logger.getLogger(ServerConfiguration.class);
}