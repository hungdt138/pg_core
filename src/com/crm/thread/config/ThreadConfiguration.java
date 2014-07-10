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

import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import org.apache.log4j.Level;

import com.crm.util.AppProperties;
import com.crm.util.ParameterMap;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;
import com.fss.util.AppException;

public class ThreadConfiguration
{
	protected long			threadId			= 0;
	protected String		alias				= StringPool.BLANK;
	protected String		name				= StringPool.BLANK;

	protected String		startTime			= StringPool.BLANK;
	protected String		endTime				= StringPool.BLANK;
	protected String		dayAvailables		= StringPool.BLANK;
	protected String		monthAvailables		= StringPool.BLANK;
	protected int			delayTime			= 1000;
	protected int			executedCount		= 0;

	protected String		dispatcherClassName	= StringPool.BLANK;
	protected String		instanceClassName	= StringPool.BLANK;
	protected String		startupType			= StringPool.BLANK;

	protected boolean		instanceable		= false;
	protected int			minInstances		= 1;
	protected int			maxInstances		= 1;

	protected String		logClassName		= "";
	protected int			logLevel			= Level.ALL_INT;
	protected String		logDir				= "";
	protected String		logWildcard			= "";
	protected boolean		debugEnable			= false;

	protected ParameterMap	parameters			= new ParameterMap();

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillDispatcherParameter(ResultSet rsConfig) throws AppException
	{
		try
		{
			setAlias(rsConfig.getString("alias_"));

			setThreadId(rsConfig.getLong("threadId"));
			setName(rsConfig.getString("title"));

			setDispatcherClassName(rsConfig.getString("dispatcherClass"));

			setInstanceClassName(rsConfig.getString("instanceClass"));
			setStartupType(rsConfig.getString("startupType"));

			setStartTime(StringUtil.nvl(rsConfig.getString("startTime"), "00:00:00"));
			setEndTime(StringUtil.nvl(rsConfig.getString("endTime"), "23:59:59"));
			setDayAvailables("");
			setMonthAvailables("");
			setDelayTime(rsConfig.getInt("delayTime"));

			if (rsConfig.getBoolean("instanceable"))
			{
				setMinInstances(rsConfig.getInt("minInstances"));
				setMaxInstances(rsConfig.getInt("maxInstances"));
			}
			else
			{
				setMinInstances(0);
				setMaxInstances(0);
			}

			// log
			setLogClassName(StringUtil.nvl(rsConfig.getString("logClassName"), ""));
			setLogLevel(rsConfig.getInt("logLevel"));
			setLogDir(StringUtil.nvl(rsConfig.getString("logDir"), ""));
			setLogWildcard(StringUtil.nvl(rsConfig.getString("logFile"), "*.*"));
			setDebugEnable(rsConfig.getBoolean("debugEnable"));
			setDebugEnable(true);
		}
		catch (Exception e)
		{
			throw new AppException(e.getMessage());
		}
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillParameter(ResultSet rsParameter) throws AppException
	{
		try
		{
			while (rsParameter.next())
			{

			}
		}
		catch (Exception e)
		{
			throw new AppException(e.getMessage());
		}
	}

	public boolean isAutoStart()
	{
		return startupType.equals(ThreadConstant.STARTUP_TYPE_AUTO);
	}

	public boolean isDisable()
	{
		return startupType.equals(ThreadConstant.STARTUP_TYPE_DISABLE);
	}

	public long getThreadId()
	{
		return threadId;
	}

	public void setThreadId(long threadId)
	{
		this.threadId = threadId;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public String getDayAvailables()
	{
		return dayAvailables;
	}

	public void setDayAvailables(String dayAvailables)
	{
		this.dayAvailables = dayAvailables;
	}

	public String getMonthAvailables()
	{
		return monthAvailables;
	}

	public void setMonthAvailables(String monthAvailables)
	{
		this.monthAvailables = monthAvailables;
	}

	public int getDelayTime()
	{
		return delayTime;
	}

	public void setDelayTime(int delayTime)
	{
		this.delayTime = delayTime;
	}

	public int getExecutedCount()
	{
		return executedCount;
	}

	public void setExecutedCount(int executedCount)
	{
		this.executedCount = executedCount;
	}

	public boolean isInstanceable()
	{
		return instanceable;
	}

	public void setInstanceable(boolean instanceable)
	{
		this.instanceable = instanceable;
	}

	public int getMinInstances()
	{
		return minInstances;
	}

	public void setMinInstances(int minInstances)
	{
		this.minInstances = minInstances;
	}

	public int getMaxInstances()
	{
		return maxInstances;
	}

	public void setMaxInstances(int maxInstances)
	{
		this.maxInstances = maxInstances;
	}

	public String getDispatcherClassName()
	{
		return dispatcherClassName;
	}

	public void setDispatcherClassName(String dispatcherClassName)
	{
		this.dispatcherClassName = dispatcherClassName;
	}

	public String getInstanceClassName()
	{
		return instanceClassName;
	}

	public void setInstanceClassName(String instanceClassName)
	{
		this.instanceClassName = instanceClassName;
	}

	public String getStartupType()
	{
		return startupType;
	}

	public void setStartupType(String startupType)
	{
		this.startupType = startupType;
	}

	// //////////////////////////////////////////////////////
	// Log4J
	// //////////////////////////////////////////////////////
	public String getLogClassName()
	{
		return logClassName;
	}

	public void setLogClassName(String logClassName)
	{
		this.logClassName = logClassName;
	}

	public int getLogLevel()
	{
		return logLevel;
	}

	public void setLogLevel(int logLevel)
	{
		this.logLevel = logLevel;
	}

	public String getLogDir()
	{
		return logDir;
	}

	public void setLogDir(String logDir)
	{
		this.logDir = logDir;
	}

	public String getLogWildcard()
	{
		return logWildcard;
	}

	public void setLogWildcard(String logWildcard)
	{
		this.logWildcard = logWildcard;
	}

	public boolean isDebugEnable()
	{
		return debugEnable;
	}

	public void setDebugEnable(boolean debugEnable)
	{
		this.debugEnable = debugEnable;
	}

	public ParameterMap getParameters()
	{
		return parameters;
	}

	// //////////////////////////////////////////////////////
	// Directory
	// //////////////////////////////////////////////////////
	public boolean isDirectoryEnable()
	{
		return parameters.getBoolean("directory.enable", false);
	}

	public void setDirectoryEnable(boolean directoryEnable)
	{
		parameters.setParameter("directory.enable", directoryEnable);
	}

	public String getDirectory(String directory) throws Exception
	{
		return parameters.getDirectory(directory);
	}

	public String getImportDir()
	{
		return (String) parameters.getParameter("directory.importDir", "");
	}

	public void setImportDir(String importDir) throws IOException
	{
		parameters.setParameter("directory.importDir", importDir);
	}

	public String getBackupDir()
	{
		return (String) parameters.getParameter("directory.backupDir", "");
	}

	public void setBackupDir(String backupDir) throws IOException
	{
		parameters.setParameter("directory.backupDir", backupDir);
	}

	public String getRejectDir()
	{
		return (String) parameters.getParameter("directory.rejectDir", "");
	}

	public void setRejectDir(String rejectDir) throws IOException
	{
		parameters.setParameter("directory.rejectDir", rejectDir);
	}

	public String getErrorDir()
	{
		return (String) parameters.getParameter("directory.errorDir", "");
	}

	public void setErrorDir(String errorDir) throws IOException
	{
		parameters.setParameter("directory.errorDir", errorDir);
	}

	public String getTempDir()
	{
		return (String) parameters.getParameter("directory.tempDir", "");
	}

	public void setTempDir(String tempDir) throws IOException
	{
		parameters.setParameter("directory.tempDir", tempDir);
	}

	public String getExportDir()
	{
		return (String) parameters.getParameter("directory.exportDir", "");
	}

	public void setExportDir(String exportDir) throws IOException
	{
		parameters.setParameter("directory.exportDir", exportDir);
	}

	public String getWildcard()
	{
		return (String) parameters.getParameter("directory.wildcard", "");
	}

	public void setWildcard(String wildcard)
	{
		parameters.setParameter("directory.wildcard", wildcard);
	}

	public String getValidateClass()
	{
		return (String) parameters.getParameter("directory.validate", "");
	}

	public void setValidateClass(String validateClass)
	{
		parameters.setParameter("directory.validate", validateClass);
	}

	// //////////////////////////////////////////////////////
	// Remote
	// //////////////////////////////////////////////////////
	public boolean isRemoteEnable()
	{
		return parameters.getBoolean("remote.enable", false);
	}

	public void setRemoteEnable(boolean remoteEnable)
	{
		parameters.setParameter("remote.enable", remoteEnable);
	}

	public String getRemoteHost()
	{
		return parameters.getString("remote.host");
	}

	public void setRemoteHost(String remoteHost)
	{
		parameters.setParameter("remote.host", remoteHost);
	}

	public String getRemotePort()
	{
		return parameters.getString("remote.port");
	}

	public void setRemotePort(String remotePort)
	{
		parameters.setParameter("remote.port", remotePort);
	}

	public String getRemoteUser()
	{
		return parameters.getString("remote.user");
	}

	public void setRemoteUser(String remoteUser)
	{
		parameters.setParameter("remote.user", remoteUser);
	}

	public String getRemotePassword()
	{
		return parameters.getString("remote.password");
	}

	public void setRemotePassword(String remotePassword)
	{
		parameters.setParameter("remote.password", remotePassword);
	}

	public AppProperties getRemoteContext() throws Exception
	{
		return parameters.getProperties("remote.context");
	}

	public void setRemoteContext(AppProperties context)
	{
		parameters.setParameter("remote.context", context);
	}

	public String getRemoteClass()
	{
		return parameters.getString("remote.class");
	}

	public void setRemoteClass(String remoteClass)
	{
		parameters.setParameter("remote.class", remoteClass);
	}

	// //////////////////////////////////////////////////////
	// Datasource
	// //////////////////////////////////////////////////////
	public boolean isDatasourceEnable()
	{
		return parameters.getBoolean("datasource.enable");
	}

	public void setDatasourceEnable(boolean datasourceEnable)
	{
		parameters.setParameter("datasource.enable", datasourceEnable);
	}

	public String getDatasourceHeader()
	{
		return parameters.getString("datasource.header");
	}

	public void setDatasourceHeader(String header)
	{
		parameters.setParameter("datasource.header", header);
	}

	public String getDatasourceDelimiter()
	{
		return parameters.getString("datasource.delimiter");
	}

	public void setDatasourceDelimiter(String delimiter)
	{
		parameters.setParameter("datasource.delimiter", delimiter);
	}

	public String getDatasourceFields()
	{
		return parameters.getString("datasource.fields");
	}

	public void setDatasourceFields(String fields)
	{
		parameters.setParameter("datasource.fields", fields);
	}

	public int getDatasourceIgnoreRows()
	{
		return parameters.getInteger("datasource.ignoreRows");
	}

	public void setDatasourceIgnoreRows(int ignoreRows)
	{
		parameters.setParameter("datasource.ignoreRows", ignoreRows);
	}

	public String getDatasourceStampField()
	{
		return parameters.getString("datasource.stampField");
	}

	public void setDatasourceStampField(String stampField)
	{
		parameters.setParameter("datasource.stampField", stampField);
	}

	public String getDatasourceIndicatorField()
	{
		return parameters.getString("datasource.indicatorField");
	}

	public void setDatasourceIndicatorField(String indicatorField)
	{
		parameters.setParameter("datasource.indicatorField", indicatorField);
	}

	public AppProperties getDatasourceContext() throws Exception
	{
		return parameters.getProperties("datasource.context");
	}

	public void setDatasourceContext(AppProperties context)
	{
		parameters.setParameter("datasource.context", context);
	}

	// //////////////////////////////////////////////////////
	// Export
	// //////////////////////////////////////////////////////
	public boolean isExportEnable()
	{
		return parameters.getBoolean("export.enable");
	}

	public void setExportEnable(boolean enable)
	{
		parameters.setParameter("export.enable", enable);
	}

	public String getExportClassName()
	{
		return parameters.getString("export.class");
	}

	public void setExportClassName(String className)
	{
		parameters.setParameter("export.className", className);
	}

	public String getExportHeader()
	{
		return parameters.getString("export.header");
	}

	public void setExportHeader(String header)
	{
		parameters.setParameter("export.header", header);
	}

	public String getExportFields()
	{
		return parameters.getString("export.fields");
	}

	public void setExportFields(String fields)
	{
		parameters.setParameter("export.fields", fields);
	}

	public String getExportDelimiter()
	{
		return parameters.getString("export.delimiter");
	}

	public void setExportDelimiter(String delimiter)
	{
		parameters.setParameter("export.delimiter", delimiter);
	}

	public AppProperties getExportContext() throws Exception
	{
		return parameters.getProperties("export.context");
	}

	public void setExportContext(AppProperties context)
	{
		parameters.setParameter("export.context", context);
	}

	// //////////////////////////////////////////////////////
	// Error
	// //////////////////////////////////////////////////////
	public boolean isErrorEnable()
	{
		return parameters.getBoolean("error.enable");
	}

	public void setErrorEnable(boolean enable)
	{
		parameters.setParameter("error.enable", enable);
	}

	public String getErrorClassName()
	{
		return parameters.getString("error.class");
	}

	public void setErrorClassName(String className)
	{
		parameters.setParameter("error.className", className);
	}

	public String getErrorHeader()
	{
		return parameters.getString("error.header");
	}

	public void setErrorHeader(String header)
	{
		parameters.setParameter("error.header", header);
	}

	public String getErrorFields()
	{
		return parameters.getString("error.fields");
	}

	public void setErrorFields(String fields)
	{
		parameters.setParameter("error.fields", fields);
	}

	public String getErrorDelimiter()
	{
		return parameters.getString("error.delimiter");
	}

	public void setErrorDelimiter(String delimiter)
	{
		parameters.setParameter("error.delimiter", delimiter);
	}

	public AppProperties getErrorContext() throws Exception
	{
		return parameters.getProperties("error.context");
	}

	public void setErrorContext(AppProperties context)
	{
		parameters.setParameter("error.context", context);
	}

	// //////////////////////////////////////////////////////
	// Queue
	// //////////////////////////////////////////////////////
	public boolean isQueueEnable()
	{
		return parameters.getBoolean("queue.enable");
	}

	public void setQueueEnable(boolean enable)
	{
		parameters.setParameter("queue.enable", enable);
	}

	public AppProperties getQueueContext() throws Exception
	{
		return parameters.getProperties("queue.context");
	}

	public void setQueueContext(AppProperties context)
	{
		parameters.setParameter("queue.context", context);
	}

	public String getQueueConnectionFactory()
	{
		return parameters.getString("queue.factory");
	}

	public void setQueueConnectionFactory(String factory)
	{
		parameters.setParameter("queue.factory", factory);
	}

	public String getQueueProducer()
	{
		return parameters.getString("queue.producer");
	}

	public void setQueueProducer(String producer)
	{
		parameters.setParameter("queue.producer", producer);
	}

	public String getQueueReceiver()
	{
		return parameters.getString("queue.receiver");
	}

	public void setQueueReceiver(String receiver)
	{
		parameters.setParameter("queue.receiver", receiver);
	}

	// //////////////////////////////////////////////////////
	// SQL
	// //////////////////////////////////////////////////////
	public boolean isSqlEnable()
	{
		return parameters.getBoolean("sql.enable");
	}

	public void setSqlEnable(boolean enable)
	{
		parameters.setParameter("sql.enable", enable);
	}

	public String getSqlStatement()
	{
		return parameters.getString("sql.statement");
	}

	public void setSqlStatement(String sqlStatement)
	{
		parameters.setParameter("sql.statement", sqlStatement);
	}

	// //////////////////////////////////////////////////////
	// Sequence
	// //////////////////////////////////////////////////////
	public String getSequenceName()
	{
		return parameters.getString("sequence.name");
	}

	public void setSequenceName(String sequence)
	{
		parameters.setParameter("sequence.name", sequence);
	}

	public int getSequenceIncremental()
	{
		return parameters.getInteger("sequence.incremental");
	}

	public void setSequenceIncremental(int incremental)
	{
		parameters.setParameter("sequence.incremental", incremental);
	}

	public long getSequenceValue()
	{
		return parameters.getLong("sequence.value");
	}

	public void setSequenceValue(long value)
	{
		parameters.setParameter("sequence.value", value);
	}

	// //////////////////////////////////////////////////////
	// Format
	// //////////////////////////////////////////////////////
	public SimpleDateFormat getParseDate()
	{
		return parameters.getDateFormat("format.parseDate");
	}

	public void setParseDate(SimpleDateFormat parseDate)
	{
		parameters.setDateFormat("format.parseDate", parseDate);
	}

	public SimpleDateFormat getFormatDate()
	{
		return parameters.getDateFormat("format.date");
	}

	public void setFormatDate(SimpleDateFormat format)
	{
		parameters.setParameter("format.date", format);
	}

	// //////////////////////////////////////////////////////
	// Batch
	// //////////////////////////////////////////////////////
	public boolean isBatchEnable()
	{
		return parameters.getBoolean("batch.enable");
	}

	public void setBatchEnable(boolean enable)
	{
		parameters.setParameter("batch.enable", enable);
	}

	public int getBatchSize()
	{
		return parameters.getInteger("batch.size");
	}

	public void setBatchSize(int batchSize)
	{
		parameters.setParameter("batch.size", batchSize);
	}

	public String getBatchMode()
	{
		return parameters.getString("batch.mode");
	}

	public void setBatchMode(String batchMode)
	{
		parameters.setParameter("batch.mode", batchMode);
	}

	public String getInsertStatement()
	{
		return parameters.getString("batch.insertStatement");
	}

	public void setInsertStatement(String insertStatement)
	{
		parameters.setParameter("batch.insertStatement", insertStatement);
	}

	public String getInsertFields()
	{
		return parameters.getString("batch.insertFields");
	}

	public void setInsertFields(String insertFields)
	{
		parameters.setParameter("batch.insertFields", insertFields);
	}

	public String getUpdateStatement()
	{
		return parameters.getString("batch.updateStatement");
	}

	public void setUpdateStatement(String updateStatement)
	{
		parameters.setParameter("batch.updateStatement", updateStatement);
	}

	public String getUpdateFields()
	{
		return parameters.getString("batch.updateFields");
	}

	public void setUpdateFields(String updateFields)
	{
		parameters.setParameter("batch.updateFields", updateFields);
	}

	public AppProperties getBatchContext() throws Exception
	{
		return parameters.getProperties("batch.context");
	}

	public void setBatchContext(AppProperties context)
	{
		parameters.setParameter("remote.context", context);
	}
}