package com.crm.kernel.io;

import java.io.*;

import com.crm.util.StringUtil;

/**
 * <p>
 * Title: FileUtil
 * </p>
 * <p>
 * Description: Utility for file processing
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Thai Hoang Hiep
 * @version 1.0
 */

public class FileUtil
{
	// //////////////////////////////////////////////////////
	// Constant
	// //////////////////////////////////////////////////////
	public static final int	BUFFER_SIZE			= 65536;	// 64K
	public static final int	MAX_SMALL_FILE_SIZE	= 16777216; // 16M

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param strCurrenDir
	 *            String
	 * @param strFileName
	 *            String
	 * @return String
	 */
	// //////////////////////////////////////////////////////
	public static String getAbsolutePath(String strCurrenDir, String strFileName)
	{
		if (!strFileName.startsWith("/") && !strFileName.startsWith("\\"))
		{
			if (!strCurrenDir.endsWith("/") && !strCurrenDir.endsWith("\\"))
				return strCurrenDir + "/" + strFileName;
			return strCurrenDir + strFileName;
		}
		return strFileName;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Create folder if it's not exist
	 * 
	 * @param folder
	 *            folder to create if it does not exist
	 * @throws IOException
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static void forceFolderExist(String folder) throws IOException
	{
		File flTemp = new File(folder);
		if (!flTemp.exists())
		{
			if (!flTemp.mkdirs())
				throw new IOException("Could not create folder " + folder);
		}
		else if (!flTemp.isDirectory())
			throw new IOException("A file with name" + folder + " already exist");
	}

	// //////////////////////////////////////////////////////
	/**
	 * Rename file from source to destination
	 * 
	 * @param strSrc
	 *            String
	 * @param strDest
	 *            String
	 * @param deleteIfExist
	 *            boolean
	 * @return boolean
	 * @throws IOException
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static boolean renameFile(String strSrc, String strDest, boolean deleteIfExist) throws IOException
	{
		File flSrc = new File(strSrc);
		File flDest = new File(strDest);
		if (flSrc.getAbsolutePath().equals(flDest.getAbsolutePath()))
			return false;
		if (flDest.exists())
		{
			if (deleteIfExist)
				flDest.delete();
			else
				throw new IOException("File '" + strDest + "' already exist");
		}
		return flSrc.renameTo(flDest);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Rename file from src to des, override if des exist
	 * 
	 * @param strSrc
	 *            source file
	 * @param strDest
	 *            destination file
	 * @return true if succees, otherswise false
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static boolean renameFile(String strSrc, String strDest)
	{
		File flSrc = new File(strSrc);
		File flDest = new File(strDest);
		if (flSrc.getAbsolutePath().equals(flDest.getAbsolutePath()))
			return true;
		if (flDest.exists())
			flDest.delete();
		return flSrc.renameTo(flDest);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Copy file from src to des, override if des exist
	 * 
	 * @param strSrc
	 *            source file
	 * @param strDest
	 *            destination file
	 * @return true if succees, otherswise false
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static boolean copyFile(String strSrc, String strDest)
	{
		FileInputStream isSrc = null;
		FileOutputStream osDest = null;
		try
		{
			File flDest = new File(strDest);
			if (flDest.exists())
				flDest.delete();

			File flSrc = new File(strSrc);
			if (!flSrc.exists())
				return false;

			isSrc = new FileInputStream(flSrc);
			osDest = new FileOutputStream(flDest);

			byte btData[] = new byte[BUFFER_SIZE];
			int iLength;
			while ((iLength = isSrc.read(btData)) != -1)
				osDest.write(btData, 0, iLength);

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			safeClose(isSrc);
			safeClose(osDest);
		}
	}

	// //////////////////////////////////////////////////////
	/**
	 * Delete file
	 * 
	 * @param strSrc
	 *            file to delete
	 * @return true if succees, otherswise false
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static boolean deleteFile(String strSrc)
	{
		File flSrc = new File(strSrc);
		return flSrc.delete();
	}

	// //////////////////////////////////////////////////////
	/**
	 * Copy resource to file
	 * 
	 * @param cls
	 *            Class with valid priviledge
	 * @param strResSource
	 *            resource path
	 * @param strFile
	 *            file to copy to
	 * @return true if succees, otherswise false
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static boolean copyResource(Class<?> cls, String strResSource, String strFile)
	{
		InputStream isSrc = null;
		FileOutputStream osDest = null;
		try
		{
			isSrc = cls.getResourceAsStream(strResSource);
			if (isSrc == null)
				throw new IOException("Resource " + strResSource + " not found");
			osDest = new FileOutputStream(strFile);

			byte btData[] = new byte[BUFFER_SIZE];
			int iLength;
			while ((iLength = isSrc.read(btData)) != -1)
				osDest.write(btData, 0, iLength);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			safeClose(isSrc);
			safeClose(osDest);
		}
		return true;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Delete unused file
	 * 
	 * @param strPath
	 *            Path to scan
	 * @param strWildcard
	 *            scan wildcard
	 * @param iOffset
	 *            miliseconds to determinate old file
	 * @author Thai Hoang Hiep
	 */
	// //////////////////////////////////////////////////////
	public static void deleteOldFile(String strPath, String strWildcard, int iOffset)
	{
		if (!strPath.endsWith("/"))
			strPath += "/";
		File flFolder = new File(strPath);
		if (!flFolder.exists())
			return;
		String strFileList[] = flFolder.list(new WildcardFilter(strWildcard));
		if (strFileList != null && strFileList.length > 0)
		{
			long lCurrentTime = (new java.util.Date()).getTime();
			for (int iFileIndex = 0; iFileIndex < strFileList.length; iFileIndex++)
			{
				File fl = new File(strPath + strFileList[iFileIndex]);
				if (lCurrentTime - fl.lastModified() >= iOffset)
					fl.delete();
			}
		}
	}

	// //////////////////////////////////////////////////////
	/**
	 * @param strFileName
	 *            String File to check
	 * @param iMaxSize
	 *            max size
	 * @param iRemainSize
	 *            remain size
	 * @throws Exception
	 */
	// //////////////////////////////////////////////////////
	public static void backup(String strFileName, int iMaxSize, int iRemainSize) throws Exception
	{
		if (iMaxSize <= iRemainSize)
			throw new IllegalArgumentException();
		final java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		File flSource = new File(strFileName);
		if (flSource.length() > iMaxSize)
		{
			String strNewName = strFileName + "." + fmt.format(new java.util.Date());
			renameFile(strFileName, strNewName);
			RandomAccessFile fl = null;
			FileOutputStream os = null;
			try
			{
				os = new FileOutputStream(strFileName);
				fl = new RandomAccessFile(strNewName, "rw");
				fl.seek(fl.length() - iRemainSize);
				byte bt[] = new byte[iRemainSize];
				int iByteRead = fl.read(bt);
				if (iByteRead != iRemainSize)
					throw new IOException();
				os.write(bt, 0, iByteRead);
				fl.setLength(fl.length() - iRemainSize);
			}
			finally
			{
				FileUtil.safeClose(fl);
				FileUtil.safeClose(os);
			}
		}
	}

	// //////////////////////////////////////////////////////
	public static void backup(String strFileName, int iMaxSize)
	{
		final java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		File flSource = new File(strFileName);
		if (flSource.length() > iMaxSize)
		{
			String strNewName = "";
			if (strFileName.indexOf(".") >= 0)
				strNewName = strFileName.substring(0, strFileName.lastIndexOf(".")) +
								fmt.format(new java.util.Date()) +
								strFileName.substring(strFileName.lastIndexOf("."));
			else
				strNewName = strFileName + fmt.format(new java.util.Date());
			renameFile(strFileName, strNewName);
		}
	}

	// //////////////////////////////////////////////////////
	/**
	 * Backup used for file relation thread
	 * 
	 * @param strSourcePath
	 *            String (Must have '/' at last string)
	 * @param strBackupPath
	 *            String (Must have '/' at last string)
	 * @param strSourceFile
	 *            String
	 * @param strBackupFile
	 *            String
	 * @param strBackupStyle
	 *            String
	 * @throws Exception
	 * @return String
	 */
	// //////////////////////////////////////////////////////
	public static String backup(String strSourcePath, String strBackupPath, String strSourceFile, String strBackupFile,
			String strBackupStyle) throws Exception
	{
		return backup(strSourcePath, strBackupPath, strSourceFile, strBackupFile, strBackupStyle, true);
	}

	// //////////////////////////////////////////////////////
	public static String backup(String strSourcePath, String strBackupPath, String strSourceFile, String strBackupFile,
			String strBackupStyle, boolean bReplace) throws Exception
	{
		return backup(strSourcePath, strBackupPath, strSourceFile, strBackupFile, strBackupStyle, "", bReplace);
	}

	// //////////////////////////////////////////////////////
	public static String backup(String strSourcePath, String strBackupPath, String strSourceFile, String strBackupFile,
			String strBackupStyle, String strAdditionPath) throws Exception
	{
		return backup(strSourcePath, strBackupPath, strSourceFile, strBackupFile, strBackupStyle, strAdditionPath, true);
	}

	// //////////////////////////////////////////////////////
	public static String backup(String strSourcePath, String strBackupPath, String strSourceFile, String strBackupFile,
			String strBackupStyle, String strAdditionPath, boolean bReplace) throws Exception
	{
		// Backup file
		if (strBackupStyle.equals("Delete file"))
		{
			if (!FileUtil.deleteFile(strSourcePath + strSourceFile))
				throw new Exception("Cannot delete file " + strSourcePath + strSourceFile);
		}
		else if (strBackupPath.length() > 0)
		{
			// Backup source file
			String strCurrentDate = "";
			if (strBackupStyle.equals("Daily"))
				strCurrentDate = StringUtil.format(new java.util.Date(), "yyyyMMdd") + "/";
			else if (strBackupStyle.equals("Monthly"))
				strCurrentDate = StringUtil.format(new java.util.Date(), "yyyyMM") + "/";
			else if (strBackupStyle.equals("Yearly"))
				strCurrentDate = StringUtil.format(new java.util.Date(), "yyyy") + "/";
			FileUtil.forceFolderExist(strBackupPath + strCurrentDate + strAdditionPath);
			if (!FileUtil.renameFile(strSourcePath + strSourceFile, strBackupPath + strCurrentDate + strAdditionPath
					+ strBackupFile, bReplace))
				throw new Exception("Cannot rename file " + strSourcePath + strSourceFile + " to " + strBackupPath
						+ strCurrentDate + strBackupFile);
			return strBackupPath + strCurrentDate + strBackupFile;
		}
		return "";
	}

	// //////////////////////////////////////////////////////
	/**
	 * Close object safely
	 * 
	 * @param is
	 *            InputStream
	 */
	// //////////////////////////////////////////////////////
	public static void safeClose(InputStream is)
	{
		try
		{
			if (is != null)
				is.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////////////////
	/**
	 * Close object safely
	 * 
	 * @param os
	 *            OutputStream
	 */
	// //////////////////////////////////////////////////////
	public static void safeClose(OutputStream os)
	{
		try
		{
			if (os != null)
				os.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////////////////
	/**
	 * Close object safely
	 * 
	 * @param fl
	 *            RandomAccessFile
	 */
	// //////////////////////////////////////////////////////
	public static void safeClose(RandomAccessFile fl)
	{
		try
		{
			if (fl != null)
				fl.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////////////////
	/**
	 * Return resource from local file, if false then search from classpath
	 * 
	 * @param strName
	 *            String
	 * @return InputStream
	 * @throws Exception
	 */
	// //////////////////////////////////////////////////////
	public static java.net.URL getResource(String strName)
	{
		try
		{
			File fl = new File(strName);
			if (fl.exists() && fl.isFile())
				return fl.toURI().toURL();
		}
		catch (Exception e)
		{
		}
		if (!strName.startsWith("/"))
			return FileUtil.class.getResource("/" + strName);
		else
			return FileUtil.class.getResource(strName);
	}
}
