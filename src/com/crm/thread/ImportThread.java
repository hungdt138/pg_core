package com.crm.thread;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.sql.*;
import java.util.*;

import com.crm.kernel.sql.Database;
import com.crm.thread.util.ThreadUtil;

import com.fss.thread.ParameterType;
import com.fss.util.AppException;

public class ImportThread extends FileThread
{
	public final static String				AUTO_MODE			= "auto";

	public final static String				INSERT_MODE			= "insert";

	public final static String				UPDATE_MODE			= "update";

	protected Connection					connection			= null;
	protected String						batchMode			= INSERT_MODE;
	protected String						insertStatement		= "";
	protected String						insertFields		= "";
	protected String						updateStatement		= "";
	protected String						updateFields		= "";

	protected int[]							insertColumns		= new int[0];
	protected int[]							updateColumns		= new int[0];

	protected boolean						autoTruncate		= false;
	protected String						tableName			= "";
	protected String						partitionPrefix		= "";
	protected String						partitionPostfix	= "";

	// Target data
	protected PreparedStatement				mstmtInsert;
	protected PreparedStatement				mstmtUpdate;

	protected ArrayList<String>				rawData				= new ArrayList<String>();
	protected ArrayList<ArrayList<String>>	batchData			= new ArrayList<ArrayList<String>>();

	public ImportThread()
	{
		super();
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(createParameterDefinition("insertStatement", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("insertFields", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("updateStatement", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("updateFields", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));

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

			insertStatement = ThreadUtil.getString(this, "insertStatement", false, "");
			insertFields = ThreadUtil.getString(this, "insertFields", false, "");
			updateStatement = ThreadUtil.getString(this, "updateStatement", false, "");
			updateFields = ThreadUtil.getString(this, "updateFields", false, "");
			
			batchMode = ThreadUtil.getString(this, "batchMode", false, INSERT_MODE);
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

	public void beforeSession() throws Exception
	{
		super.beforeSession();

		try
		{
			autoTruncate = ThreadUtil.getBoolean(this, "truncatePartition", false);

			if (autoTruncate)
			{
				tableName = ThreadUtil.getString(this, "tableName", true, "");
				partitionPrefix = ThreadUtil.getString(this, "partitionPrefix", false, "");
				partitionPostfix = ThreadUtil.getString(this, "partitionPostifx", false, "");
			}

			if (isInsertMode() && insertStatement.equals(""))
			{
				throw new AppException("invalid-insert-SQL");
			}
			else if (isInsertMode() && insertFields.equals(""))
			{
				throw new AppException("invalid-insert-fields");
			}
			else if (isInsertMode() && updateStatement.equals("") && !updateFields.equals(""))
			{
				throw new AppException("invalid-update-SQL");
			}
			else if (isInsertMode() && !updateStatement.equals("") && updateFields.equals(""))
			{
				throw new AppException("invalid-update-fields");
			}
			else if (isUpdateMode() && updateStatement.equals(""))
			{
				throw new AppException("invalid-update-SQL");
			}
			else if (isUpdateMode() && updateFields.equals(""))
			{
				throw new AppException("invalid-update-fields");
			}
		}
		catch (Exception e)
		{
			throw new AppException(e.getMessage());
		}
	}

	public boolean isInsertMode() throws Exception
	{
		return batchMode.equals(AUTO_MODE) || batchMode.equals(INSERT_MODE);
	}

	public boolean isUpdateMode() throws Exception
	{
		return batchMode.equals(AUTO_MODE) || batchMode.equals(UPDATE_MODE);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected void prepareInsertStatement() throws Exception
	{
		if (!insertStatement.equals(""))
		{
			mstmtInsert = connection.prepareStatement(insertStatement);
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected void prepareUpdateStatement() throws Exception
	{
		if (!updateStatement.equals(""))
		{
			mstmtInsert = connection.prepareStatement(updateStatement);
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected void prepareDatasource() throws Exception
	{
		try
		{
			super.prepareDatasource();

			connection = Database.getConnection();

			if (isInsertMode())
			{
				prepareInsertStatement();

				insertColumns = findColumns(insertFields, ThreadUtil.getDelimiter(this));
			}

			if (isUpdateMode() || (isInsertMode() && !updateStatement.equals("") && !updateFields.equals("")))
			{
				prepareUpdateStatement();

				updateColumns = findColumns(updateFields, ThreadUtil.getDelimiter(this));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected String getRawBatchValue(int index) throws Exception
	{
		return rawData.get(index);
	}

	// ////////////////////////////////////////////////////////////////////////
	// after insert into vector
	// Author: ThangPV
	// Modify: HungHM
	// Modify DateTime: 22/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public void runInsertBatch() throws Exception
	{
		String error = null;

		int index = 0;
		int executedCount = 0;

		int intDataSize = batchData.size();

		while (index < intDataSize)
		{
			// add to batch
			mstmtInsert.clearBatch();

			for (int j = index; j < intDataSize; j++)
			{
				rawData = batchData.get(j);

				bindInsert();
			}

			try
			{
				mstmtInsert.executeBatch();
				
				batchSize = ThreadUtil.getBatchSize(this);
			}
			catch (BatchUpdateException e)
			{
				error = e.getMessage().trim();
			}
			catch (SQLException e)
			{
				error = e.getMessage().trim();
			}

			if (error != null)
			{
				if (error.startsWith("ORA-0168"))
				{
					// Out of TableSpace
					throw new Exception(error);
				}
				else if (error.startsWith("ORA-01653"))
				{
					// Out of TableSpace
					throw new Exception(error);
				}
			}

			executedCount = mstmtInsert.getUpdateCount();

			if (executedCount < 0)
			{
				throw new AppException("invalid-update-count");
			}

			insertCount += executedCount;

			index += executedCount;

			if (error != null)
			{
				rawData = batchData.get(index);

				onError(error);

				error = null;
			}

			index++;
		}

		batchData.clear();
	}

	// ////////////////////////////////////////////////////////////////////////
	// after insert into vector
	// Author: ThangPV
	// Modify: HungHM
	// Modify DateTime: 22/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public void runUpdateBatch() throws Exception
	{
		String error = null;

		int index = 0;
		int executedCount = 0;

		int intDataSize = batchData.size();

		while (index < intDataSize)
		{
			// add to batch
			mstmtUpdate.clearBatch();

			for (int j = index; j < intDataSize; j++)
			{
				rawData = batchData.get(j);

				bindUpdate();
			}

			try
			{
				mstmtUpdate.executeBatch();
			}
			catch (BatchUpdateException e)
			{
				error = e.getMessage().trim();
			}
			catch (SQLException e)
			{
				error = e.getMessage().trim();
			}

			executedCount = mstmtUpdate.getUpdateCount();
			updateCount += executedCount;

			index += (executedCount + 1);

			if (error != null)
			{
				onError(error);

				error = null;
			}
		}

		batchData.clear();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: HiepTH
	// Modify DateTime: 08/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public void runBatch() throws Exception
	{
		try
		{
			setExecutingBatch(true);

			if (isInsertMode())
			{
				runInsertBatch();
			}
			else
			{
				runUpdateBatch();
			}

			connection.commit();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			batchData.clear();

			setExecutingBatch(false);
		}

		batchCount = 0;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: HiepTH
	// Modify DateTime: 08/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public void bindInsert() throws Exception
	{
		for (int k = 0; k < insertColumns.length; k++)
		{
			try
			{
				mstmtInsert.setString(k + 1, getValue(insertColumns[k]));
			}
			catch (Exception e)
			{
				debugMonitor("bind variable at index " + (k + 1) + " is error: " + e.getMessage());

				throw e;
			}
		}

		mstmtInsert.addBatch();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: HiepTH
	// Modify DateTime: 08/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public void bindUpdate() throws Exception
	{
		for (int k = 0; k < updateColumns.length; k++)
		{
			try
			{
				mstmtUpdate.setString(k + 1, getValue(updateColumns[k]));
			}
			catch (Exception e)
			{
				debugMonitor("bind variable at index " + (k + 1) + " is error: " + e.getMessage());

				throw e;
			}
		}

		mstmtUpdate.addBatch();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Process error data
	// Author: HiepTH
	// Modify DateTime: 08/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public void onError(String message) throws Exception
	{
		// Process duplicate
		if (message.startsWith("ORA-00001") && isInsertMode() && (mstmtUpdate != null))
		{
			try
			{
				onDuplicate();

				return;
			}
			catch (SQLException e)
			{
				exportError(e.getMessage().trim());
			}
			catch (AppException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				logMonitor(e);

				exportError(e.getMessage().trim());
			}
		}
		else
		{
			// Log error
			exportError(message);
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Process duplicate data
	// Author: HungHM
	// Modify DateTime: 08/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public void onDuplicate() throws Exception
	{
		String strValue = "";

		try
		{
			for (int iIndex = 0; iIndex < updateColumns.length; iIndex++)
			{
				strValue = getValue(updateColumns[iIndex]);
				mstmtUpdate.setString(iIndex + 1, strValue);
			}
			mstmtUpdate.executeUpdate();
			updateCount++;

			batchSize = 1;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	/**
	 * Event raised when session finish
	 * 
	 * @throws java.lang.Exception
	 * @author Phan Viet Thang
	 */
	// //////////////////////////////////////////////////////
	protected void beforeProcessDatasource() throws Exception
	{
		super.beforeProcessDatasource();

		connection.setAutoCommit(false);
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
			if (datasourceResult)
			{
				connection.commit();
			}
			else
			{
				connection.rollback();
			}
		}
		catch (Exception e)
		{
			logMonitor(e);
		}
		finally
		{
			super.afterProcessDatasource();
		}
	}
}
