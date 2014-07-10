package com.crm.kernel.io;

import java.io.*;

import com.crm.thread.DispatcherThread;
import com.fss.util.AppException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company: FPT
 * </p>
 * 
 * @author ThangPV
 * @version 1.0 Modified Date : 05/01/2012
 */

public class OutputFile
{
	protected DispatcherThread		dispatcher	= null;

	private String					fileType	= "";
	private String					workingDir	= "";
	private String					exportDir	= "";
	private String					fileName	= "";
	private String					header		= "";

	private FileOutputStream		outFile		= null;
	private BufferedOutputStream	buffer		= null;
	private int						bufferSize	= 5 * 1024 * 1024;

	private boolean					isOpen		= false;
	private boolean					allowEmpty	= false;
	private String					fields		= "";
	private String					delimiter	= ";";
	private int[]					columns		= new int[0];

	private int						rowCount	= 0;

	public OutputFile()
	{
	}

	public void setDispatcher(DispatcherThread dispatcher) throws AppException
	{
		this.dispatcher = dispatcher;
	}

	public boolean isOpen()
	{
		return isOpen;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public String getFileType()
	{
		return fileType;
	}

	public void setWorkingDir(String workingDir)
	{
		this.workingDir = workingDir;
	}

	public String getWorkingDir()
	{
		return workingDir;
	}

	public void setExportDir(String exportDir)
	{
		this.exportDir = exportDir;
	}

	public String getExportDir()
	{
		return exportDir;
	}

	public void setHeader(String header)
	{
		this.header = header;
	}

	public String getHeader()
	{
		return header;
	}

	public void setBufferSize(int bufferSize)
	{
		this.bufferSize = bufferSize;
	}

	public int getBufferSize()
	{
		return bufferSize;
	}

	public boolean isAllowEmpty()
	{
		return allowEmpty;
	}

	public void setAllowEmpty(boolean allowEmpty)
	{
		this.allowEmpty = allowEmpty;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileName()
	{
		return fileName;
	}

	public String getFilePath()
	{
		return workingDir + fileName;
	}

	public void openFile(String fileName, String header, int bufferSize) throws Exception
	{
		setFileName(fileName);
		setHeader(header);

		try
		{
			FileUtil.forceFolderExist(workingDir);
			FileUtil.forceFolderExist(exportDir);

			outFile = new FileOutputStream(workingDir + fileName);
			buffer = new BufferedOutputStream(outFile, bufferSize); // 5M

			isOpen = true;

			if (!getHeader().equals(""))
			{
				addText(getHeader());
			}
		}
		catch (Exception e)
		{
			closeFile(false);

			throw e;
		}
	}

	public void openFile(String fileName, String header) throws Exception
	{
		openFile(fileName, header, 5 * 1024 * 1024);
	}

	public void open(String fileName) throws Exception
	{
		openFile(fileName, getHeader(), getBufferSize());
	}

	public void closeFile(boolean move) throws Exception
	{
		if (!isOpen())
		{
			return;
		}

		try
		{
			FileUtil.safeClose(buffer);
			FileUtil.safeClose(outFile);

			if ((rowCount == 0) && !allowEmpty)
			{
				FileUtil.deleteFile(getFilePath());
			}
			else if (move && isOpen && !workingDir.equals(exportDir))
			{
				if (!FileUtil.renameFile(getFilePath(), exportDir + getFileName(), true))
				{
					String alarm = "Can not move file " + getFileName() + " to directory " + exportDir;

					if (dispatcher != null)
					{
						dispatcher.logMonitor(alarm);
					}
					else
					{
						throw new AppException(alarm);
					}
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			isOpen = false;
			rowCount = 0;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// clear vector
	// Author: ThangPV
	// Modify DateTime: 17/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public void clear() throws Exception
	{
		if (!isOpen())
		{
			return;
		}

		try
		{
			if (buffer != null)
			{
				buffer.flush();
				buffer.close();
			}

			if (outFile != null)
			{
				outFile.close();
			}

			FileUtil.deleteFile(getFilePath());
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			isOpen = false;
			rowCount = 0;

			FileUtil.safeClose(buffer);
			FileUtil.safeClose(outFile);
		}
	}

	public void addText(String text) throws Exception
	{
		if (!isOpen())
		{
			throw new AppException("file-is-not-open");
		}
		else if (text.equals(""))
		{
			return;
		}

		text += '\n';

		try
		{
			buffer.write(text.getBytes());

			rowCount++;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public String getFields()
	{
		return fields;
	}

	public void setFields(String fields)
	{
		this.fields = fields;
	}

	public int[] getColumns()
	{
		return columns;
	}

	public void setColumns(int[] columns)
	{
		this.columns = columns;
	}

	public String getDelimiter()
	{
		return delimiter;
	}

	public void setDelimiter(String delimiter)
	{
		this.delimiter = delimiter;
	}
}
