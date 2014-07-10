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

package com.crm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.crm.kernel.io.FileUtil;

import com.fss.util.AppException;

public class ParameterMap implements Serializable
{
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 141621021372499135L;

	protected Map<String, Object>	parameters			= new HashMap<String, Object>();

	public Map<String, Object> getParameters()
	{
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters)
	{
		this.parameters = parameters;
	}

	public void loadFromFile(String fileName) throws Exception
	{
		parameters.clear();

		FileInputStream is = null;

		Properties properties = new Properties();

		try
		{
			is = new FileInputStream(fileName);

			properties.load(is);
		}
		finally
		{
			FileUtil.safeClose(is);
		}

		loadParameters(properties);
	}

	public void loadParameters(Properties properties)
	{
		parameters.clear();

		for (Enumeration<?> en = properties.propertyNames(); en.hasMoreElements();)
		{
			String key = (String) en.nextElement();

			parameters.put(key, properties.getProperty(key));
		}
	}

	public void loadParameters(String properties) throws IOException
	{
		Properties props = new Properties();

		props.load(new StringReader(StringUtil.nvl(properties, "")));

		loadParameters(props);
	}

	public Object getParameter(String key, Object defaultValue)
	{
		Object value = parameters.get(key);

		return (value == null) ? defaultValue : value;
	}

	public Object getParameter(String key) throws Exception
	{
		return parameters.get(key);
	}

	public void setParameter(String key, Object value)
	{
		parameters.put(key, value);
	}

	public Object getMandatoryParameter(String key) throws Exception
	{
		Object value = parameters.get(key);

		if (value == null)
		{
			throw new AppException("null-parameter");
		}

		return value;
	}

	public Date getDate(String key) throws Exception
	{
		Object value = parameters.get(key);

		if (value == null)
		{
			return null;
		}
		else if (value instanceof Date)
		{
			return (Date) value;
		}
		else
		{
			throw new AppException("invalid-parameter-type");
		}
	}

	public boolean getBoolean(String key, boolean defaultValue)
	{
		Object value = parameters.get(key);

		if (value == null)
		{
			return defaultValue;
		}
		else if (value instanceof Boolean)
		{
			return (Boolean) value;
		}
		else
		{
			return Boolean.valueOf(value.toString());
		}
	}

	public boolean getBoolean(String key)
	{
		return getBoolean(key, false);
	}

	public int getInteger(String key, int defaultValue)
	{
		Object value = parameters.get(key);

		if (value == null)
		{
			return defaultValue;
		}
		else if (value instanceof Integer)
		{
			return (Integer) value;
		}
		else
		{
			return Integer.valueOf(value.toString());
		}
	}

	public byte getByte(String key, byte defaultValue)
	{
		Object value = parameters.get(key);

		if (value == null)
		{
			return defaultValue;
		}
		else
		{
			return Byte.valueOf(value.toString());
		}
	}

	public int getInteger(String key)
	{
		return getInteger(key, 0);
	}

	public long getLong(String key, long defaultValue)
	{
		Object value = parameters.get(key);

		if (value == null)
		{
			return defaultValue;
		}
		else if (value instanceof Long)
		{
			return (Long) value;
		}
		else
		{
			log.error("invalid-parameter-type" + value);

			return defaultValue;
		}
	}

	public long getLong(String key)
	{
		return getLong(key, 0);
	}

	public double getDouble(String key, double defaultValue)
	{
		Object value = parameters.get(key);

		if (value == null)
		{
			return defaultValue;
		}
		else if (value instanceof Double)
		{
			return (Double) value;
		}
		else
		{
			log.error("invalid-parameter-type" + value);

			return defaultValue;
		}
	}

	public double getDouble(String key)
	{
		return getDouble(key, 0);
	}

	public String getString(String key, String defaultValue)
	{
		Object value = parameters.get(key);

		try
		{
			if (value == null)
			{
				return defaultValue;
			}
			else if (value instanceof String)
			{
				return (String) value;
			}
			else
			{
				return value.toString();
			}
		}
		catch (Exception e)
		{
			log.error("invalid-parameter-type" + value);

			return defaultValue;
		}
	}

	public String getString(String key)
	{
		return getString(key, "");
	}

	public String getDirectory(String key, boolean mandatory, boolean autoCreate) throws Exception
	{
		String directory = mandatory ? (String) getMandatoryParameter(key) : getString(key);

		if (!directory.endsWith("/") && !directory.endsWith("\\"))
		{
			directory += "/";
		}

		if (!directory.equals("") && !directory.equals("/") && autoCreate)
		{
			FileUtil.forceFolderExist(directory);
		}

		return directory;
	}

	public String getDirectory(String key, boolean autoCreate) throws Exception
	{
		return getDirectory(key, false, autoCreate);
	}

	public String getDirectory(String key) throws Exception
	{
		return getDirectory(key, false, true);
	}

	public SimpleDateFormat getDateFormat(String key, String pattern)
	{
		Object format = parameters.get(key);

		if (format == null)
		{
			if (pattern.equals(""))
			{
				pattern = "yyyy/MM/dd";
			}

			format = new SimpleDateFormat(pattern);
		}
		else if (format instanceof String)
		{
			format = new SimpleDateFormat((String) format);
		}

		return (SimpleDateFormat) format;
	}

	public SimpleDateFormat getDateFormat(String key)
	{
		return getDateFormat(key, "");
	}

	public void setDateFormat(String key, SimpleDateFormat format)
	{
		parameters.put(key, format);
	}

	public void setDateFormat(String key, String pattern)
	{
		parameters.put(key, new SimpleDateFormat(pattern));
	}

	public AppProperties getProperties(String key) throws Exception
	{
		Object value = parameters.get(key);

		if (value == null)
		{
			return new AppProperties();
		}
		else if (value instanceof String)
		{
			return new AppProperties((String) value);
		}
		else
		{
			throw new AppException("invalid-parameter-type");
		}
	}

	private Logger	log	= Logger.getLogger(ParameterMap.class);
}