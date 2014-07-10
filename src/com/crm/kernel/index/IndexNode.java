package com.crm.kernel.index;

import java.lang.reflect.Method;
import java.util.Date;

import javax.jms.Message;

import com.crm.kernel.message.Constants;
import com.crm.thread.DispatcherInstance;
import com.crm.util.AppProperties;
import com.crm.util.CompareUtil;
import com.crm.util.DateUtil;
import com.crm.util.StringUtil;

import com.fss.util.AppException;

public class IndexNode implements Comparable<IndexNode>
{
	private String			indexKey		= "";
	private boolean			wildcard		= false;

	private Date			startDate		= new Date();
	private Date			expirationDate	= null;

	protected Method		executeMethod	= null;
	protected ExecuteImpl	executeImpl		= null;

	// protected AppProperties parameters = new AppProperties();
	protected AppProperties	parameters		= new AppProperties();

	public IndexNode()
	{
	}

	public IndexNode(String indexKey)
	{
		setIndexKey(indexKey);
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append(indexKey + "\t");
		buffer.append(wildcard + "\t");

		return buffer.toString();
	}

	/**
	 * @param indexKey
	 *            the value to set
	 */
	public void setIndexKey(String indexKey)
	{
		if (indexKey.equals(""))
		{
			setWildcard(false);
			this.indexKey = "";
		}
		else
		{
			setWildcard(indexKey.endsWith("%"));

			if (isWildcard())
			{
				this.indexKey = indexKey.substring(0, indexKey.length() - 1);
			}
			else
			{
				this.indexKey = indexKey;
			}
		}
	}

	/**
	 * @return the value
	 */
	public String getIndexKey()
	{
		return indexKey;
	}

	public void setWildcard(boolean includeWildcard)
	{
		this.wildcard = includeWildcard;
	}

	public boolean isWildcard()
	{
		return wildcard;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getExpirationDate()
	{
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate)
	{
		this.expirationDate = expirationDate;
	}

	public ExecuteImpl getExecuteImpl()
	{
		return executeImpl;
	}

	public Method getExecuteMethod()
	{
		return executeMethod;
	}

	public synchronized void setProcessClass(String processClass) throws Exception
	{
		try
		{
			processClass = StringUtil.nvl(processClass, "");

			if (processClass.equals(""))
			{
				throw new AppException("invalid-execute-class");
			}

			executeImpl = (ExecuteImpl) Class.forName(processClass).newInstance();
		}
		catch (Exception e)
		{
			executeImpl = null;

			throw e;
		}
		finally
		{
			executeMethod = null;
		}
	}

	public synchronized void setProcessMethod(String processMethod) throws Exception
	{
		try
		{
			processMethod = StringUtil.nvl(processMethod, "");

			if (processMethod.equals(""))
			{
				throw new AppException(Constants.ERROR_PROCESS_METHOD);
			}
			else if (executeImpl == null)
			{
				throw new AppException(Constants.ERROR_PROCESS_CLASS);
			}

			String processClass = executeImpl.getClass().getName();

			executeMethod = Class.forName(processClass).getMethod(
					processMethod, DispatcherInstance.class, IndexNode.class, Message.class);

			if (executeMethod == null)
			{
				throw new AppException(Constants.ERROR_PROCESS_METHOD);
			}
		}
		catch (Exception e)
		{
			executeMethod = null;

			throw e;
		}
	}

	public synchronized void setExecuteMethod(String processClass, String processMethod) throws Exception
	{
		try
		{
			processClass = StringUtil.nvl(processClass, "");
			processMethod = StringUtil.nvl(processMethod, "");

			if (processClass.equals("") && processMethod.equals(""))
			{

			}
			else
			{
				setProcessClass(processClass);
				setProcessMethod(processMethod);
			}
		}
		catch (Exception e)
		{
			executeImpl = null;

			throw e;
		}
	}

	public AppProperties getParameters()
	{
		return parameters;
	}

	public void setParameters(AppProperties parameters)
	{
		this.parameters = parameters;
	}

	public void setParameters(String parameters) throws Exception
	{
		this.parameters.load(StringUtil.nvl(parameters, ""));
	}

	public String getParameter(String key, String defaultValue)
	{
		return parameters.getProperty(key, defaultValue);
	}

	public String getParameter(String parameter, String prefix, String postfix, boolean isPrepaid, String defaultValue)
	{
		if (!prefix.equals(""))
		{
			parameter = prefix + "." + parameter;
		}

		if (isPrepaid)
		{
			parameter = parameter + ".prepaid";
		}
		else
		{
			parameter = parameter + ".postpaid";
		}

		if (!postfix.equals(""))
		{
			parameter = parameter + "." + postfix;
		}

		return parameters.getString(parameter, defaultValue);
	}

	public boolean isRange(Date date)
	{
		return DateUtil.isRange(startDate, expirationDate, DateUtil.trunc(date));
	}

	public boolean isRange(double start, double end, double value)
	{
		return (start <= value) && (end >= value);
	}

	public boolean isRange(long start, long end, long value)
	{
		return (start <= value) && (end >= value);
	}

	public boolean isRange(int start, int end, int value)
	{
		return (start <= value) && (end >= value);
	}

	@Override
	public int compareTo(IndexNode obj)
	{
		return CompareUtil.compareString(getIndexKey(), obj.getIndexKey(), wildcard);
	}
}
