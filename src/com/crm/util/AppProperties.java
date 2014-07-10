package com.crm.util;

import java.io.FileInputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

import com.crm.kernel.io.FileUtil;
import com.fss.util.AppException;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class AppProperties extends Properties
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public AppProperties()
	{
		super();
	}

	public AppProperties(String properties) throws Exception
	{
		super();

		load(properties);
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void close() throws AppException
	{
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void load(String properties) throws Exception
	{
		clear();

		if (properties != null)
		{
			this.load(new StringReader(properties));
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void loadFromFile(String file) throws Exception
	{
		clear();

		FileInputStream inputStream = null;

		try
		{
			inputStream = new FileInputStream(file);

			this.load(inputStream);
		}
		finally
		{
			if (inputStream != null)
			{
				inputStream.close();
			}
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void init(String[] properties, String value, boolean isOveride) throws AppException
	{
		for (int j = 0; j < properties.length; j++)
		{
			if (!properties[j].equals(""))
			{
				String current = getProperty(value);

				if (isOveride || (current == null))
				{
					setProperty(properties[j], value);
				}
			}
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public boolean existProperty(String key) throws AppException
	{
		return (getProperty(key) != null);
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public String getMandatory(String key) throws AppException
	{
		String value = getProperty(key);

		if (value == null)
		{
			throw new AppException("Property " + key + " can not be null");
		}
		else
		{
			return value;
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public String getPropertyIgnoreCase(String key, String defaultValue) throws AppException
	{
		boolean found = false;

		String result = defaultValue;

		Enumeration<?> propNames = propertyNames();

		for (; !found && propNames.hasMoreElements();)
		{
			String current = (String) propNames.nextElement();

			if (key.equalsIgnoreCase(current))
			{
				found = true;

				result = getProperty(current);
			}
		}

		return result;
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public AppProperties copy(String prefix, boolean isRemovePrefix) throws AppException
	{
		AppProperties result = new AppProperties();

		Enumeration<?> propNames = propertyNames();

		if (!prefix.endsWith("."))
		{
			prefix = prefix + ".";
		}

		for (; propNames.hasMoreElements();)
		{
			String key = (String) propNames.nextElement();

			if (key.startsWith(prefix))
			{
				if (isRemovePrefix)
				{
					String newKey = key.substring(prefix.length());

					result.setProperty(newKey, getProperty(key));
				}
				else
				{
					result.setProperty(key, getProperty(key));
				}
			}
		}

		return result;
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void setBoolean(String key, boolean blnValue)
	{
		if (blnValue)
		{
			setProperty(key, "1");
		}
		else
		{
			setProperty(key, "0");
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void setString(String key, String value)
	{
		setProperty(key, value);
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public String getString(String key, String defaultValue)
	{
		return getProperty(key, defaultValue);
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public String getString(String key)
	{
		return getString(key, "");
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public boolean getBoolean(String key) throws AppException
	{
		String value = getProperty(key, "false");

		return value.equals("1") || value.equals("true");
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public boolean getBoolean(String key, boolean blnDefault)
	{
		String strValue = getProperty(key, "");

		if (strValue.equals(""))
		{
			return blnDefault;
		}
		else
		{
			return strValue.equals("1") || strValue.equals("true");
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void setInteger(String key, int intValue) 
	{
		setProperty(key, String.valueOf(intValue));
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public int getUnsignInt(String key, int intDefault) throws AppException
	{
		int value = getInteger(key, intDefault);

		if (value < 0)
		{
			throw new AppException(key + " must greater than or equal 0");
		}
		else
		{
			return value;
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public int getInteger(String key, int intDefault) throws AppException
	{
		if (!existProperty(key) || getProperty(key).equals(""))
		{
			return intDefault;
		}
		else
		{
			return getInt(key);
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public int getInteger(String key) throws AppException
	{
		return getInteger(key, 0);
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void setByte(String key, byte value) throws AppException
	{
		setProperty(key, String.valueOf(value));
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public byte getByte(String key, byte defaultValue) throws AppException
	{
		String strValue = "";

		try
		{
			strValue = getProperty(key, String.valueOf(defaultValue));

			return Byte.valueOf(strValue);
		}
		catch (Exception e)
		{
			throw new AppException(key + " is not an byte (" + e.getMessage() + ")");
		}

	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void setLong(String key, long value) 
	{
		setProperty(key, String.valueOf(value));
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public long getLong(String key, long defaultValue) throws AppException
	{
		String strValue = "";

		try
		{
			strValue = getProperty(key);

			if ((strValue != null) && (!strValue.equals("")))
			{
				return Long.valueOf(strValue);
			}
			else
			{
				return defaultValue;
			}
		}
		catch (Exception e)
		{
			return defaultValue;
		}

	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public long getLong(String key) throws AppException
	{
		return getLong(key, 0);
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public int getInt(String key) throws AppException
	{
		String strValue = "";
		try
		{
			strValue = getProperty(key, "0");

			if (strValue.endsWith(".0"))
			{
				return Integer.parseInt(strValue.substring(0, strValue.length() - 2));
			}
			else
			{
				return Integer.parseInt(strValue);
			}
		}
		catch (Exception e)
		{
			throw new AppException(key + " is not an integer (" + e.getMessage() + ")");
		}

	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void setDouble(String key, double dblValue) 
	{
		setProperty(key, String.valueOf(dblValue));
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public double getDouble(String key) throws AppException
	{
		return Double.parseDouble(getProperty(key));
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public double getDouble(String key, double defaultValue) throws AppException
	{
		if (!existProperty(key) || getProperty(key).equals(""))
		{
			return defaultValue;
		}
		else
		{
			return getDouble(key);
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public Date getDate(String key, String format) throws Exception
	{
		String value = getProperty(key);

		if (value == null)
		{
			return null;
		}

		SimpleDateFormat formatDate = new SimpleDateFormat(format);

		return formatDate.parse(value);
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void setDate(String key, Date date, String format) throws Exception
	{
		SimpleDateFormat formatDate = new SimpleDateFormat(format);

		if (date == null)
		{
			setProperty(key, "");
		}
		else
		{
			setString(key, formatDate.format(date));
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void setDate(String key, Date date) throws Exception
	{
		SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");

		if (date == null)
		{
			setProperty(key, "");
		}
		else
		{
			setString(key, formatDate.format(date));
		}
	}

	// //////////////////////////////////////////////////////
	// do insert
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public Date getDate(String key, String format, Date defaultValue) throws Exception
	{
		Date value = getDate(key, format);

		if (value == null)
		{
			return defaultValue;
		}
		else
		{
			return value;
		}
	}

	public String getFolder(String key, String defaultFolder, boolean autoCreate, boolean mandatory)
			throws AppException
	{
		String folder = getProperty(key, defaultFolder);

		if (folder.length() > 0)
		{
			if (!folder.endsWith("/") && !folder.endsWith("\\"))
			{
				folder += "/";
			}

			if (autoCreate)
			{
				try
				{
					FileUtil.forceFolderExist(folder);
				}
				catch (Exception e)
				{
					throw new AppException(e.getMessage(), "ManageableThread.loadDirectory", key);
				}
			}
		}
		else if (mandatory)
		{
			throw new AppException("Value of '" + key + "' can not be null");
		}

		return folder;
	}

	public String getFolder(String key, boolean autoCreate, boolean mandatory) throws AppException
	{
		return getFolder(key, "", autoCreate, mandatory);
	}
}
