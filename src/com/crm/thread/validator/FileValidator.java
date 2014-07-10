package com.crm.thread.validator;

import java.io.*;
import java.util.*;

import com.crm.kernel.io.WildcardFilter;
import com.crm.thread.FileThread;
import com.crm.thread.util.ThreadUtil;

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
 * @author Vu Anh Dung
 * @version 1.0 Purpose : Base class for other threads
 */

public class FileValidator
{
	private FileThread		instance	= null;

	private ArrayList<String>	listFile	= new ArrayList<String>();
	private int					currentFile	= -1;

	public FileValidator()
	{
		super();
	}

	public void setInstance(FileThread instance)
	{
		this.instance = instance;
	}

	public FileThread getInstance()
	{
		return instance;
	}

	public ArrayList<String> getListFile()
	{
		return listFile;
	}

	public void setListFile(ArrayList<String> listFile)
	{
		this.listFile = listFile;
	}

	public void list() throws Exception
	{
		currentFile = -1;

		listFile.clear();

		try
		{
			File fl = new File(ThreadUtil.getString(instance, "importDir", true, ""));

			String[] fileNames = fl.list(new WildcardFilter(ThreadUtil.getString(instance, "wildcard", true, "")));

			if (fileNames == null)
			{
				return;
			}

			Arrays.sort(fileNames);
			
			for (int j = 0; j < fileNames.length; j++)
			{
				listFile.add(fileNames[j]);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// validate file before call convert
	// Author: HiepTH
	// Modify DateTime: 09/07/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean validateFile(String fileName) throws Exception
	{
		return true;
	}

	// /////////////////////////////////////////////////////////////////////////
	// validate file before call convert
	// Author: HiepTH
	// Modify DateTime: 09/07/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean validateFile(int index) throws Exception
	{
		if ((listFile == null) || (listFile.size() == 0) || (index >= listFile.size()))
		{
			return false;
		}
		else
		{
			return validateFile(listFile.get(index));
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// validate file before call convert
	// Author: HiepTH
	// Modify DateTime: 09/07/2003
	// /////////////////////////////////////////////////////////////////////////
	public String getFileName(int index) throws Exception
	{
		return listFile.get(index);
	}

	// /////////////////////////////////////////////////////////////////////////
	// validate file before call convert
	// Author: HiepTH
	// Modify DateTime: 09/07/2003
	// /////////////////////////////////////////////////////////////////////////
	public String getFileName() throws Exception
	{
		if ((currentFile < 0) || (currentFile >= listFile.size()))
		{
			return "";
		}

		return listFile.get(currentFile);
	}

	// /////////////////////////////////////////////////////////////////////////
	// validate file before call convert
	// Author: HiepTH
	// Modify DateTime: 09/07/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean next() throws Exception
	{
		currentFile++;

		while (instance.isAvailable() && (currentFile < listFile.size()))
		{
			if (validateFile(currentFile))
			{
				return true;
			}
			else
			{
				currentFile++;
			}
		}

		return false;
	}

	public boolean storeConfig(String fileName, boolean fileResult) throws Exception
	{
		return fileResult;
	}
}
