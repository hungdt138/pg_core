package com.crm.kernel.io;

import java.io.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.crm.util.StringUtil;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author ThangPV
 * @version 1.0
 */

public class CSVFile
{
	private FileReader			fileReader		= null;
	private BufferedReader		textBuffer		= null;
	private int					bufferSize		= 5 * 1024 * 1024;

	// private property
	private boolean				suppressHeaders	= false;
	private boolean				autoFillValue	= false;
	private String				delimiterSymbol	= ";";
	private String				endOfFileSymbol	= "";

	private String				header			= "";

	private ArrayList<String>	columns			= new ArrayList<String>();
	private ArrayList<String>	values			= new ArrayList<String>();

	private String				currentLine		= null;
	private String				mstrLine		= null;

	public CSVFile()
	{
	}

	public CSVFile(int bufferSize)
	{
		setBufferSize(bufferSize);
	}

	public FileReader getFileReader()
	{
		return fileReader;
	}

	public void setFileReader(FileReader fileReader)
	{
		this.fileReader = fileReader;
	}

	public BufferedReader getTextBuffer()
	{
		return textBuffer;
	}

	public void setTextBuffer(BufferedReader textBuffer)
	{
		this.textBuffer = textBuffer;
	}

	public int getBufferSize()
	{
		return bufferSize;
	}

	public void setBufferSize(int bufferSize)
	{
		this.bufferSize = bufferSize;
	}

	public boolean isSuppressHeaders()
	{
		return suppressHeaders;
	}

	public void setSuppressHeaders(boolean suppressHeaders)
	{
		this.suppressHeaders = suppressHeaders;
	}

	public boolean isAutoFillValue()
	{
		return autoFillValue;
	}

	public void setAutoFillValue(boolean autoFillValue)
	{
		this.autoFillValue = autoFillValue;
	}

	public String getDelimiterSymbol()
	{
		return delimiterSymbol;
	}

	public void setDelimiterSymbol(String delimiterSymbol)
	{
		this.delimiterSymbol = delimiterSymbol;
	}

	public String getEndOfFileSymbol()
	{
		return endOfFileSymbol;
	}

	public void setEndOfFileSymbol(String endOfFileSymbol)
	{
		this.endOfFileSymbol = endOfFileSymbol;
	}

	public String getHeader()
	{
		return header;
	}

	public void setHeader(String header)
	{
		this.header = header;
	}

	public ArrayList<String> getColumns()
	{
		return columns;
	}

	public void setColumns(ArrayList<String> columns)
	{
		this.columns = columns;
	}

	public ArrayList<String> getValues()
	{
		return values;
	}

	public void setValues(ArrayList<String> values)
	{
		this.values = values;
	}

	public String getCurrentLine()
	{
		return currentLine;
	}

	public void setCurrentLine(String currentLine)
	{
		this.currentLine = currentLine;
	}

	public int getColumnCount()
	{
		return columns.size();
	}

	public int findColumn(String field)
	{
		if ((field.length() > 1) && field.startsWith("\"") && field.endsWith("\""))
		{
			field = field.substring(1, field.length() - 1);
		}

		if (field.equals(""))
		{
			return -1;
		}

		for (int j = 0; j < columns.size(); j++)
		{
			if (field.equalsIgnoreCase(columns.get(j)))
			{
				return j;
			}
		}

		log.debug("Field " + field + " is not found");

		return -1;
	}

	private void parseLine(String line) throws Exception
	{
		if (!delimiterSymbol.equals(""))
		{
			values = StringUtil.toList(line, delimiterSymbol);
		}
	}

	public void openFile(
			String filePath, String header, String delimiter, int ignoreRows, String endOfFile, long skipCharacters)
			throws Exception
	{
		try
		{
			fileReader = new FileReader(filePath);
			textBuffer = new BufferedReader(fileReader, getBufferSize());

			textBuffer.skip(skipCharacters);

			currentLine = null;

			delimiterSymbol = delimiter;
			endOfFileSymbol = endOfFile;

			if (ignoreRows > 0)
			{
				for (int j = 0; j < ignoreRows; j++)
				{
					mstrLine = textBuffer.readLine();
					if (mstrLine == null)
					{
						break;
					}
				}
			}

			if (header.equals(""))
			{
				parseHeader();
			}
			else
			{
				parseHeader(header);
			}

			mstrLine = textBuffer.readLine();
		}
		catch (Exception e)
		{
			safeClose();
			throw e;
		}
	}

	public void openFile(String filePath, String delimiter, int ignoreRows, String endOfFile) throws Exception
	{
		openFile(filePath, "", delimiter, ignoreRows, endOfFile, 0);
	}

	public void openFile(String filePath, String header) throws Exception
	{
		openFile(filePath, header, getDelimiterSymbol(), 0, "", 0);
	}

	public void openFile(String filePath) throws Exception
	{
		openFile(filePath, "");
	}

	public void closeFile() throws Exception
	{
		try
		{
			if (textBuffer != null)
			{
				textBuffer.close();
			}

			if (fileReader != null)
			{
				fileReader.close();
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			safeClose();
		}
	}

	public void safeClose()
	{
		safeClose(textBuffer);
		safeClose(fileReader);
	}

	public static void safeClose(Reader reader)
	{
		try
		{
			if (reader != null)
			{
				reader.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void parseHeader(String header) throws Exception
	{
		setHeader(header);

		columns = StringUtil.toList(header, delimiterSymbol);

		for (int j = 0; j < columns.size(); j++)
		{
			String value = columns.get(j);

			if ((value.length() > 1) && value.startsWith("\"") && value.endsWith("\""))
			{
				value = value.substring(1, value.length() - 1);

				columns.set(j, value);
			}

			log.debug("Column " + columns.get(j) + " at index " + j);
		}

		values.clear();

		for (int j = 0; j < columns.size(); j++)
		{
			values.add("");
		}
	}

	public void parseHeader() throws Exception
	{
		mstrLine = textBuffer.readLine();

		while (mstrLine != null && mstrLine.trim().equals(""))
		{
			mstrLine = textBuffer.readLine();
		}

		if ((mstrLine != null) && !delimiterSymbol.equals("")
				&& (endOfFileSymbol.equals("") || !mstrLine.startsWith(endOfFileSymbol)))
		{
			parseHeader(mstrLine);
		}
	}

	public void parseValues() throws Exception
	{
		parseLine(mstrLine);

		if ((columns != null) && (values != null))
		{
			if (!autoFillValue && (values.size() != columns.size()))
			{
				throw new Exception("Number of columns does not match header");
			}
		}
	}

	public boolean first() throws Exception
	{
		throw new Exception("This method does not supported");
	}

	public boolean last() throws Exception
	{
		throw new Exception("This method does not supported");
	}

	public boolean prev() throws Exception
	{
		throw new Exception("This method does not supported");
	}

	public boolean next() throws Exception
	{
		if ((mstrLine == null)
				|| (!endOfFileSymbol.equals("") && mstrLine.startsWith(endOfFileSymbol))
				|| mstrLine.equals(endOfFileSymbol))
		{
			return false;
		}
		else
		{
			if (!mstrLine.trim().equals(""))
			{
				parseValues();
				currentLine = mstrLine;
				mstrLine = textBuffer.readLine();

				return true;
			}
			else
			{
				mstrLine = textBuffer.readLine();

				return next();
			}
		}
	}

	public String getString(int intIndex)
	{
		return StringUtil.nvl(values.get(intIndex), "");
	}

	public String getString(String strField)
	{
		return getString(findColumn(strField));
	}

	public int getInt(int intIndex) throws Exception
	{
		String value = StringUtil.nvl(values.get(intIndex), "");

		if (!value.equals(""))
		{
			return Integer.valueOf(value);
		}
		else
		{
			return 0;
		}
	}

	public int getInt(String strField) throws Exception
	{
		return getInt(findColumn(strField));
	}

	public double getDouble(int intIndex) throws Exception
	{
		String value = StringUtil.nvl(values.get(intIndex), "");

		if (!value.equals(""))
		{
			return Double.valueOf(value);
		}
		else
		{
			return 0;
		}
	}

	public double getDouble(String strField) throws Exception
	{
		return getDouble(findColumn(strField));
	}

	public long getLong(int intIndex) throws Exception
	{
		String value = StringUtil.nvl(values.get(intIndex), "");

		if (!value.equals(""))
		{
			return Long.valueOf(value);
		}
		else
		{
			return 0;
		}
	}

	public long getLong(String strField) throws Exception
	{
		return getLong(findColumn(strField));
	}

	public String getLine()
	{
		return currentLine;
	}

	private static Logger	log	= Logger.getLogger(CSVFile.class);
}
