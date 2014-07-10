package com.crm.thread;

import java.util.Vector;

import com.crm.kernel.io.FileUtil;
import com.crm.kernel.io.SmartZip;
import com.crm.kernel.sql.Database;
import com.crm.thread.util.ThreadUtil;
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
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: FPT
 * </p>
 * 
 * @author ThangPV
 * @version 1.0 Purpose : Base class for other threads
 */

public class FileThread extends DatasourceThread
{
	protected String	importDir		= "";
	protected String	exportDir		= "";
	protected String	tempDir			= "";
	protected String	rejectDir		= "";
	protected String	errorDir		= "";
	protected String	backupDir		= "";
	protected String	wildcard		= "";

	protected boolean	autoExtract		= false;
	protected String	extractFileName	= "";

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(createParameterDefinition("importDir", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("exportDir", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("tempDir", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("errorDir", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("rejectDir", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("backupDir", "", ParameterType.PARAM_TEXTBOX_MAX, "100"));

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

			importDir = ThreadUtil.getImportDir(this);
			exportDir = ThreadUtil.getImportDir(this);
			tempDir = ThreadUtil.getImportDir(this);
			errorDir = ThreadUtil.getImportDir(this);
			rejectDir = ThreadUtil.getImportDir(this);
			backupDir = ThreadUtil.getImportDir(this);
			wildcard = ThreadUtil.getImportDir(this);
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
	// Get Call_ID
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public long getFileID() throws Exception
	{
		return Database.getSequence("FileSeq");
	}

	public boolean backupFile(String fileName) throws Exception
	{
		if (backupDir.equals(""))
		{
			logMonitor("backupDir is null. File will be delete !");

			return FileUtil.deleteFile(importDir + fileName);
		}
		else
		{
			return FileUtil.renameFile(importDir + fileName, backupDir + fileName, true);
		}
	}

	public boolean rejectFile(String fileName) throws Exception
	{
		if (rejectDir.equals(""))
		{
			logMonitor("rejectDir is null. File will be delete !");

			return false;
		}

		return FileUtil.renameFile(importDir + fileName, rejectDir + fileName, true);
	}

	// //////////////////////////////////////////////////////
	// before process file
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	protected void beforeProcessDatasource() throws Exception
	{
		String fileName = getDatasourceName();

		logMonitor("Start of processing file " + fileName);

		extractFileName = "";

		if (autoExtract)
		{
			if (fileName.toUpperCase().endsWith(".ZIP"))
			{
				// SmartZip.GUnZip(mstrImportDir+fileName,mstrImportDir+fileName.substring(0,fileName.length()-4));
				SmartZip.UnZip(importDir + fileName, importDir);

				extractFileName = fileName.substring(0, fileName.length() - 4);
			}
		}

		super.beforeProcessDatasource();
	}

	// //////////////////////////////////////////////////////
	// after process file
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	protected void afterProcessDatasource() throws Exception
	{
		String fileName = getDatasourceName();

		try
		{
			boolean moved = false;

			if (!extractFileName.equals(""))
			{
				FileUtil.deleteFile(importDir + extractFileName);
			}

			if (datasourceResult)
			{
				moved = backupFile(fileName);
			}
			else
			{
				moved = rejectFile(fileName);
			}

			if (!moved)
			{
				logMonitor("Can not move file to relate folder. File will be delete");
			}

			if (dsValidator != null)
			{
				dsValidator.storeConfig(fileName, datasourceResult);
			}
		}
		finally
		{
			super.afterProcessDatasource();

			logMonitor("End of processing file " + fileName);
		}
	}
}
