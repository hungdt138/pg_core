package com.crm.thread;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.jms.Message;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.util.ThreadUtil;
import com.crm.thread.validator.DSValidator;
import com.crm.util.DateUtil;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;

import com.fss.thread.ParameterType;
import com.fss.util.AppException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Phan Viet Thang
 * @version 1.0
 */

public class DatasourceThread extends DispatcherThread
{
	// //////////////////////////////////////////////////////
	// Configuration
	// //////////////////////////////////////////////////////
	protected String		fieldList			= "";
	protected String		indicatorField		= "";
	protected String		stampField			= "";

	protected String		validateClass		= "";
	protected DSValidator	dsValidator			= null;

	// //////////////////////////////////////////////////////
	// Member variables
	// //////////////////////////////////////////////////////
	protected List<Object>	datasources			= null;
	protected Object		datasource			= null;

	protected int[]			datasourceColumns	= new int[0];
	protected int			indicatorColumn		= -1;
	protected int			stampColumn			= -1;

	protected boolean		datasourceResult	= false;
	protected boolean		isExecutingBatch	= false;

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		boolean display = false;
		
		try
		{
			display = ThreadUtil.getBoolean(this, "display", false);
		}
		catch (Exception e)
		{
			
		}
		
		if (display)
		{
			Vector vtYesNo = new Vector();
			vtYesNo.addElement("Y");
			vtYesNo.addElement("N");

			vtReturn.addElement(createParameterDefinition("validateClass", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
			vtReturn.addElement(createParameterDefinition("fieldList", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
			vtReturn.addElement(createParameterDefinition("indicatorField", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
			vtReturn.addElement(createParameterDefinition("stampField", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
			vtReturn.addElement(createParameterDefinition("batchSize", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		}

		vtReturn.addAll(super.getParameterDefinition());

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

			// date format
			fieldList = ThreadUtil.getString(this, "fieldList", false, "");
			indicatorField = ThreadUtil.getString(this, "indicatorField", false, "");
			stampField = ThreadUtil.getString(this, "stampField", false, "");

			// validate class
			validateClass = ThreadUtil.getString(this, "validateClass", false, "");

			if (!validateClass.equals(""))
			{
				dsValidator = (DSValidator) Class.forName(validateClass).newInstance();
				dsValidator.setDispatcher(this);
			}

			// batch size
			batchSize = ThreadUtil.getInt(this, "batchSize", 5000);
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
		finally
		{
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	public int getColumnCount() throws Exception
	{
		return datasourceColumns.length;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: ThangPV
	// Modify DateTime: 05-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int[] findColumns(String columnNames, String delimiter, boolean exception) throws Exception
	{
		// build error columns
		ArrayList<String> fields = StringUtil.toList(columnNames, delimiter);

		int[] columns = new int[fields.size()];

		for (int j = 0; j < fields.size(); j++)
		{
			columns[j] = findColumn(fields.get(j), exception);
		}

		if (getLog().isDebugEnabled())
		{
			StringBuffer buffer = new StringBuffer();

			for (int j = 0; j < columns.length; j++)
			{
				buffer.append(columns[j]);
				buffer.append(delimiter);
			}
		}

		return columns;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: ThangPV
	// Modify DateTime: 05-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int[] findColumns(String columnNames, String delimiter) throws Exception
	{
		return findColumns(columnNames, delimiter, true);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	public int findRawColumn(String columnName) throws Exception
	{
		return -1;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	public int findColumn(String columnName, boolean exception) throws Exception
	{
		if ((columnName.length() > 1) && columnName.startsWith("\"") && columnName.endsWith("\""))
		{
			columnName = columnName.substring(1, columnName.length() - 1);
		}

		if (columnName.equals(""))
		{
			return -1;
		}

		int index = -1;

		if (columnName.equalsIgnoreCase(Constants.PRIMARY_ID_FIELD))
		{
			index = Constants.PRIMARY_ID_COLUMN;
		}
		else if (columnName.equalsIgnoreCase(Constants.COMPANY_ID_FIELD))
		{
			index = Constants.COMPANY_ID_COLUMN;
		}
		else if (columnName.equalsIgnoreCase(Constants.GROUP_ID_FIELD))
		{
			index = Constants.GROUP_ID_COLUMN;
		}
		else if (columnName.equalsIgnoreCase(Constants.USER_ID_FIELD))
		{
			index = Constants.USER_ID_COLUMN;
		}
		else if (columnName.equalsIgnoreCase(Constants.USER_NAME_FIELD))
		{
			index = Constants.USER_NAME_COLUMN;
		}
		else if (columnName.equalsIgnoreCase(Constants.CREATE_DATE_FIELD))
		{
			index = Constants.CREATE_DATE_COLUMN;
		}
		else if (columnName.equalsIgnoreCase(Constants.MODIFIED_DATE_FIELD))
		{
			index = Constants.MODIFIED_DATE_COLUMN;
		}
		else if (columnName.equalsIgnoreCase(Constants.ERROR_FIELD))
		{
			index = Constants.ERROR_COLUMN;
		}
		else
		{
			index = findRawColumn(columnName);
		}

		if ((index == -1) && exception)
		{
			throw new AppException("invalid column name " + columnName);
		}
		else
		{
			return index;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	public int findColumn(String columnName) throws Exception
	{
		return findColumn(columnName, true);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	public long getSequenceValue() throws Exception
	{
		return sequenceValue++;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected boolean isExecutingBatch() throws Exception
	{
		return isExecutingBatch;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected void setExecutingBatch(boolean executing) throws Exception
	{
		this.isExecutingBatch = executing;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected String getRawValue(int index) throws Exception
	{
		throw new AppException("unimplemented");
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected String getRawBatchValue(int index) throws Exception
	{
		throw new AppException("unimplemented");
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected String getValue(int index) throws Exception
	{
		String value = "";

		try
		{
			if (index == Constants.PRIMARY_ID_COLUMN)
			{
				value = String.valueOf(getSequenceValue());
			}
			else if (index == Constants.COMPANY_ID_COLUMN)
			{
				value = "0";
			}
			else if (index == Constants.GROUP_ID_COLUMN)
			{
				value = "0";
			}
			else if (index == Constants.USER_ID_COLUMN)
			{
				value = "0";
			}
			else if (index == Constants.USER_NAME_COLUMN)
			{
				value = "system";
			}
			else if (index == Constants.CREATE_DATE_COLUMN)
			{
				value = dateFormat.format(new Date());
			}
			else if (index == Constants.MODIFIED_DATE_COLUMN)
			{
				value = dateFormat.format(new Date());
			}
			else if (index == Constants.ERROR_COLUMN)
			{
				value = lastError;
			}
			else if (isExecutingBatch())
			{
				value = getRawBatchValue(index);
			}
			else
			{
				value = getRawValue(index);
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		if ((value.length() > 1) && value.startsWith("\"") && value.endsWith("\""))
		{
			value = value.substring(1, value.length() - 1);
		}

		return value;
	}

	public Date getDate(int index) throws Exception
	{
		Date date = null;

		try
		{
			String value = getValue(index);

			if (!value.equals(""))
			{
				date = (dateFormat != null) ? dateFormat.parse(value) : null;
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		return date;
	}

	public String getValues(int[] columns, String delimiter) throws Exception
	{
		StringBuffer buffer = new StringBuffer();

		for (int j = 0; j < columns.length; j++)
		{
			buffer.append(getValue(columns[j]));

			if (j != (columns.length - 1))
			{
				buffer.append(delimiter);
			}
		}

		return buffer.toString();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int bindData() throws Exception
	{
		return Constants.BIND_ACTION_SUCCESS;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int exportData() throws Exception
	{
		return Constants.BIND_ACTION_SUCCESS;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public int exportError(String error) throws Exception
	{
		return Constants.BIND_ACTION_SUCCESS;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public void runBatch() throws Exception
	{
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: ThangPV
	// Modify DateTime: 07-Jan-2012
	// /////////////////////////////////////////////////////////////////////////
	public Serializable createLocalMessage() throws Exception
	{
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data target
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean next() throws Exception
	{
		return false;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data target
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	protected void buildDatasourceList() throws Exception
	{
		if (datasources == null)
		{
			datasources = new ArrayList<Object>();
		}
		else
		{
			datasources.clear();
		}

		datasources.add(DateUtil.trunc());
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data target
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	protected String getDatasourceName() throws Exception
	{
		return (datasource == null) ? "" : datasource.toString();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Event raised when session prepare to run
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	protected void prepareDatasource() throws Exception
	{
		datasourceColumns = findColumns(fieldList, StringPool.SEMICOLON);

		indicatorColumn = findColumn(indicatorField, false);

		stampColumn = findColumn(stampField, false);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Event raised when session prepare to run
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	protected void closeDatasource() throws Exception
	{

	}

	protected void openDatasourceQueue() throws Exception
	{
	}

	// //////////////////////////////////////////////////////
	/**
	 * Event raised when session prepare to run
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	protected void beforeProcessDatasource() throws Exception
	{
		totalCount = 0;
		successCount = 0;
		errorCount = 0;
		bypassCount = 0;
		insertCount = 0;
		updateCount = 0;

		minStamp = "";
		maxStamp = "";

		prepareDatasource();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Event raised when session finish
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	protected void afterProcessDatasource() throws Exception
	{
		try
		{
			if (totalCount > 0)
			{
				String msg = "";

				msg += "\n      Total of record : " + totalCount;
				msg += "\n      Inserted record : " + insertCount;
				msg += "\n      Updated record  : " + updateCount;
				msg += "\n      Success record  : " + successCount;
				msg += "\n      Bypass record   : " + bypassCount;
				msg += "\n      Error record    : " + errorCount;

				logMonitor(msg);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	protected void processDatasource() throws Exception
	{
		totalCount = 0;

		try
		{
			batchCount = 0;

			if (instanceEnable)
			{
				openDatasourceQueue();
			}

			while (next())
			{
				totalCount++;

				int action = bindData();

				if (action == Constants.BIND_ACTION_SUCCESS)
				{
					if (instanceEnable)
					{
						Serializable message = createLocalMessage();

						if (message != null)
						{
							while (isAvailable() && QueueFactory.getFreeSize(queueWorking) < 1000)
							{
								checkInstance(true);

								Thread.sleep(10);
							}
							
							MQConnection connection = null;
							try
							{
								connection = getMQConnection();
								connection.sendMessage(message, queueWorking, 0, queuePersistent);
							}
							finally
							{
								returnMQConnection(connection);
							}
						}
					}
					else
					{
						successCount++;
					}
				}
				else if (action == Constants.BIND_ACTION_ERROR)
				{
					exportError(lastError);

					errorCount++;
				}
				else if (action == Constants.BIND_ACTION_BYPASS)
				{
					bypassCount++;
				}
				else if (action == Constants.BIND_ACTION_EXPORT)
				{
					exportData();

					exportCount++;
				}

				// check data stamp
				if (stampColumn != -1)
				{
					String strCurrentStamp = getValue(stampColumn);

					if (totalCount == 1)
					{
						maxStamp = strCurrentStamp;
						minStamp = strCurrentStamp;
					}
					else
					{
						if (strCurrentStamp.compareTo(maxStamp) > 0)
						{
							maxStamp = strCurrentStamp;
						}
						if (strCurrentStamp.compareTo(minStamp) < 0)
						{
							minStamp = strCurrentStamp;
						}
					}
				}

				// check batch size
				batchCount++;

				if (batchCount >= batchSize)
				{
					runBatch();

					batchCount = 0;
				}

				checkInstance(true);
			}
		}
		catch (Exception e)
		{
			logMonitor("An error occur at record #" + totalCount);

			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	/**
	 * Session process
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	public void doProcessSession() throws Exception
	{
		try
		{
			buildDatasourceList();

			if ((datasources == null) || (datasources.size() == 0))
			{
				return;
			}

			for (int j = 0; j < datasources.size(); j++)
			{
				datasourceResult = false;

				datasource = datasources.get(j);

				if ((dsValidator == null) || dsValidator.validate(datasource))
				{
					try
					{
						beforeProcessDatasource();

						processDatasource();

						datasourceResult = true;
					}
					catch (Exception e)
					{
						throw e;
					}
					finally
					{
						try
						{
							closeDatasource();
						}
						catch (Exception e)
						{
							logMonitor(e);
						}

						afterProcessDatasource();
					}
				}
				else if ((dsValidator != null) && !dsValidator.validate(datasource))
				{
					break;
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

}
