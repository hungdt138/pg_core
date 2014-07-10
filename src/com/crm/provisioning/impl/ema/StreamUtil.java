package com.crm.provisioning.impl.ema;

import java.io.*;

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
 * Company: ABC-ABC
 * </p>
 * 
 * @author Thai Hoang Hiep
 * @version 1.0
 */

public class StreamUtil
{
	private static final int	miDelayTime	= 100;

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param bt
	 *            byte[]
	 * @return byte[]
	 */
	// //////////////////////////////////////////////////////
	public static byte[] trim(byte[] bt)
	{
		int iFirstIndex = 0;
		while (iFirstIndex < bt.length && bt[iFirstIndex] <= 32)
			iFirstIndex++;
		if (iFirstIndex >= bt.length)
			return new byte[0];
		int iLastIndex = bt.length - 1;
		while (iLastIndex > iFirstIndex && bt[iLastIndex] <= 32)
			iLastIndex--;
		byte btReturn[] = new byte[iLastIndex - iFirstIndex + 1];
		System.arraycopy(bt, iFirstIndex, btReturn, 0, btReturn.length);
		return btReturn;
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param bt
	 *            byte[]
	 * @return byte[]
	 */
	// //////////////////////////////////////////////////////
	public static byte[] trimLeft(byte[] bt)
	{
		int iFirstIndex = 0;
		while (iFirstIndex < bt.length && bt[iFirstIndex] <= 32)
			iFirstIndex++;
		if (iFirstIndex >= bt.length)
			return new byte[0];
		byte btReturn[] = new byte[bt.length - iFirstIndex];
		System.arraycopy(bt, iFirstIndex, btReturn, 0, btReturn.length);
		return btReturn;
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param bt
	 *            byte[]
	 * @return byte[]
	 */
	// //////////////////////////////////////////////////////
	public static byte[] trimRight(byte[] bt)
	{
		int iLastIndex = bt.length - 1;
		while (iLastIndex >= 0 && bt[iLastIndex] <= 32)
			iLastIndex--;
		if (iLastIndex < 0)
			return new byte[0];
		byte btReturn[] = new byte[iLastIndex + 1];
		System.arraycopy(bt, 0, btReturn, 0, btReturn.length);
		return btReturn;
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getData(InputStream is) throws IOException
	{
		int iLength = is.available();
		if (iLength < 0)
			throw new IOException("No data to read");
		byte[] btReturn = new byte[iLength];
		if (is.read(btReturn) < iLength)
			throw new IOException("No data to read");
		return btReturn;
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param strSymbol
	 *            String
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbol(InputStream is, String strSymbol) throws IOException
	{
		return getDataTerminatedBySymbol(is, strSymbol.getBytes());
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param btSymbol
	 *            byte[]
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbol(InputStream is, byte[] btSymbol) throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int iByteRead = 0;
		int iSymbolIndex = 0;
		while ((iByteRead = is.read()) >= 0)
		{
			os.write(iByteRead);
			if (iByteRead != btSymbol[iSymbolIndex])
				iSymbolIndex = 0;
			else
				iSymbolIndex++;
			if (iSymbolIndex >= btSymbol.length)
				return os.toByteArray();
		}
		throw new IOException("No data to read");
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param btSymbol
	 *            byte[]
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbol(InputStream is, byte btSymbol) throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int iByteRead = 0;
		while ((iByteRead = is.read()) >= 0)
		{
			os.write(iByteRead);
			if (iByteRead == btSymbol)
				return os.toByteArray();
		}
		throw new IOException("No data to read");
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param strSymbol
	 *            String
	 * @param bIgnoreCase
	 *            boolean
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbol(InputStream is, String strSymbol, boolean bIgnoreCase) throws IOException
	{
		if (bIgnoreCase)
		{
			byte[] btLower = strSymbol.toLowerCase().getBytes();
			byte[] btUpper = strSymbol.toUpperCase().getBytes();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int iByteRead = 0;
			int iSymbolIndex = 0;
			while ((iByteRead = is.read()) >= 0)
			{
				os.write(iByteRead);
				if (iByteRead != btLower[iSymbolIndex] && iByteRead != btUpper[iSymbolIndex])
					iSymbolIndex = 0;
				else
					iSymbolIndex++;
				if (iSymbolIndex >= btLower.length)
					return os.toByteArray();
			}
		}
		else
			return getDataTerminatedBySymbol(is, strSymbol);
		throw new IOException("No data to read");
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param strSymbol
	 *            String
	 * @param lTimeOut
	 *            long
	 * @return byte[]
	 * @throws Exception
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbol(InputStream is, String strSymbol, long lTimeOut) throws Exception
	{
		return getDataTerminatedBySymbol(is, strSymbol.getBytes(), lTimeOut);
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param btSymbol
	 *            byte[]
	 * @param lTimeOut
	 *            long
	 * @return byte[]
	 * @throws Exception
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbol(InputStream is, byte[] btSymbol, long lTimeOut) throws Exception
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int iByteRead = 0;
		int iSymbolIndex = 0;
		long lExpire = System.currentTimeMillis() + lTimeOut;
		do
		{
			while (is.available() > 0 && (iByteRead = is.read()) >= 0)
			{
				os.write(iByteRead);
				if (iByteRead != btSymbol[iSymbolIndex])
					iSymbolIndex = 0;
				else
					iSymbolIndex++;
				if (iSymbolIndex >= btSymbol.length)
					return os.toByteArray();
			}
			if (iByteRead < 0)
				throw new IOException("No data to read");
			Thread.sleep(miDelayTime);
		}
		while (System.currentTimeMillis() < lExpire);
		throw new IOException("Wait '" + new String(btSymbol) + "' for " + lTimeOut + " miliseconds was timed out");
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param btSymbol
	 *            byte
	 * @param lTimeOut
	 *            long
	 * @return byte[]
	 * @throws Exception
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbol(InputStream is, byte btSymbol, long lTimeOut) throws Exception
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int iByteRead = 0;
		long lExpire = System.currentTimeMillis() + lTimeOut;
		do
		{
			while (is.available() > 0 && (iByteRead = is.read()) >= 0)
			{
				os.write(iByteRead);
				if (iByteRead == btSymbol)
					return os.toByteArray();
			}
			if (iByteRead < 0)
				throw new IOException("No data to read");
			Thread.sleep(miDelayTime);
		}
		while (System.currentTimeMillis() < lExpire);
		throw new IOException("Wait '" + btSymbol + "' for " + lTimeOut + " miliseconds was timed out");
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param strSymbol
	 *            String
	 * @param lTimeOut
	 *            long
	 * @param bIgnoreCase
	 *            boolean
	 * @return byte[]
	 * @throws Exception
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbol(InputStream is, String strSymbol, long lTimeOut, boolean bIgnoreCase) throws Exception
	{
		if (bIgnoreCase)
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] btLower = strSymbol.toLowerCase().getBytes();
			byte[] btUpper = strSymbol.toUpperCase().getBytes();
			byte[] btSymbol = strSymbol.getBytes();
			int iByteRead = 0;
			int iSymbolIndex = 0;
			long lExpire = System.currentTimeMillis() + lTimeOut;
			do
			{
				while (is.available() > 0 && (iByteRead = is.read()) >= 0)
				{
					os.write(iByteRead);
					if (iByteRead != btLower[iSymbolIndex] && iByteRead != btUpper[iSymbolIndex])
						iSymbolIndex = 0;
					else
						iSymbolIndex++;
					if (iSymbolIndex >= btSymbol.length)
						return os.toByteArray();
				}
				if (iByteRead < 0)
					throw new IOException("No data to read");
				Thread.sleep(miDelayTime);
			}
			while (System.currentTimeMillis() < lExpire);
			throw new IOException("Wait '" + strSymbol + "' for " + lTimeOut + " miliseconds was timed out");
		}
		else
			return getDataTerminatedBySymbol(is, strSymbol, lTimeOut);
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param strSymbol
	 *            String
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbolNoWait(InputStream is, String strSymbol) throws IOException
	{
		return getDataTerminatedBySymbolNoWait(is, strSymbol.getBytes());
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param btSymbol
	 *            byte[]
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbolNoWait(InputStream is, byte[] btSymbol) throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int iByteRead = 0;
		int iSymbolIndex = 0;
		if (is.available() <= 0)
			throw new IOException("No data to read");
		while (is.available() > 0 && (iByteRead = is.read()) >= 0)
		{
			os.write(iByteRead);
			if (iByteRead != btSymbol[iSymbolIndex])
				iSymbolIndex = 0;
			else
				iSymbolIndex++;
			if (iSymbolIndex >= btSymbol.length)
				return os.toByteArray();
		}
		throw new IOException("No data to read");
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param btSymbol
	 *            byte[]
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbolNoWait(InputStream is, byte btSymbol) throws IOException
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int iByteRead = 0;
		if (is.available() <= 0)
			throw new IOException("No data to read");
		while (is.available() > 0 && (iByteRead = is.read()) >= 0)
		{
			os.write(iByteRead);
			if (iByteRead == btSymbol)
				return os.toByteArray();
		}
		throw new IOException("No data to read");
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param strSymbol
	 *            String
	 * @param bIgnoreCase
	 *            boolean
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getDataTerminatedBySymbolNoWait(InputStream is, String strSymbol, boolean bIgnoreCase) throws IOException
	{
		if (is.available() <= 0)
			throw new IOException("No data to read");
		if (bIgnoreCase)
		{
			byte[] btLower = strSymbol.toLowerCase().getBytes();
			byte[] btUpper = strSymbol.toUpperCase().getBytes();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int iByteRead = 0;
			int iSymbolIndex = 0;
			while (is.available() > 0 && (iByteRead = is.read()) >= 0)
			{
				os.write(iByteRead);
				if (iByteRead != btLower[iSymbolIndex] && iByteRead != btUpper[iSymbolIndex])
					iSymbolIndex = 0;
				else
					iSymbolIndex++;
				if (iSymbolIndex >= btLower.length)
					return os.toByteArray();
			}
			throw new IOException("No data to read");
		}
		else
			return getDataTerminatedBySymbolNoWait(is, strSymbol);
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @param iSize
	 *            int
	 * @return byte[]
	 * @throws IOException
	 */
	// //////////////////////////////////////////////////////
	public static byte[] getFixedSizeBuffer(InputStream is, int iSize) throws IOException
	{
		byte[] btBuffer = new byte[iSize];
		int iRemainSize = iSize;
		int iByteRead = 0;
		while (iRemainSize > 0 &&
				((iByteRead = is.read(btBuffer, iSize - iRemainSize, iRemainSize)) >= 0))
			iRemainSize -= iByteRead;
		if (iByteRead < 0)
			throw new IOException("No data to read");
		return btBuffer;
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param is
	 *            InputStream
	 * @return String
	 * @throws Exception
	 */
	// //////////////////////////////////////////////////////
	public static String readStream(InputStream is) throws Exception
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream(is.available());
		while (is.available() > 0)
		{
			byte[] bt = new byte[is.available()];
			int iByteRead = is.read(bt);
			os.write(bt, 0, iByteRead);
		}
		os.close();
		return new String(os.toByteArray());
	}
}
