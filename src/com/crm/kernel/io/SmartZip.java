package com.crm.kernel.io;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class SmartZip
{
	// //////////////////////////////////////////////////////
	// Constant
	// //////////////////////////////////////////////////////
	public static final int	BUFFER_SIZE	= 131072;	// 128K

	// /////////////////////////////////////////////////////////////////////////
	// ZIP section
	// /////////////////////////////////////////////////////////////////////////
	public static void Zip(String[] inFileNames, String outFileName, boolean includePath) throws IOException
	{
		// Create a buffer for reading the files
		byte[] buf = new byte[BUFFER_SIZE];

		try
		{
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFileName));

			// Compress the files
			for (int i = 0; i < inFileNames.length; i++)
			{
				FileInputStream in = new FileInputStream(inFileNames[i]);

				// Add ZIP entry to output stream.
				if (includePath)
				{
					String strFileName = inFileNames[i].substring(inFileNames[i].lastIndexOf(":/") + 2);
					out.putNextEntry(new ZipEntry(strFileName));
				}
				else
				{
					String strFileName = inFileNames[i].substring(inFileNames[i].lastIndexOf("/") + 1);
					out.putNextEntry(new ZipEntry(strFileName));
				}

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0)
				{
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
		}
		catch (IOException e)
		{
			throw e;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	public static void Zip(String inListOfFileNames, String outFileName, boolean includePath) throws IOException
	{
		String strTemp = inListOfFileNames + ",";
		int intNumOfItem = 0, i = 0;
		while (i < strTemp.length())
		{
			if (strTemp.charAt(i) == ',')
			{
				intNumOfItem++;
			}
			i++;
		}
		String arrInFile[] = new String[intNumOfItem];
		i = 0;
		while (strTemp.length() > 1)
		{
			arrInFile[i] = strTemp.substring(0, strTemp.indexOf(","));
			strTemp = strTemp.substring(strTemp.indexOf(",") + 1);
			i++;
		}
		try
		{
			Zip(arrInFile, outFileName, includePath);
		}
		catch (IOException e)
		{
			throw e;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector UnZip(String inZipFile, String outFolder) throws IOException, ClassCastException
	{
		if (!outFolder.substring(outFolder.length() - 1).equals("/"))
			outFolder += "/";

		Vector vtReturn = new Vector();
		ZipInputStream in = null;
		ZipFile zf = null;
		try
		{
			in = new ZipInputStream(new FileInputStream(inZipFile));
			zf = new ZipFile(inZipFile);

			// Get the first entry
			Enumeration entries = zf.entries();

			while (entries.hasMoreElements())
			{
				// Get the entry name
				ZipEntry ze = (ZipEntry) entries.nextElement();
				if (ze.isDirectory())
				{
					in.getNextEntry();
					File dirCreate = new File(outFolder + ze.getName());
					if (!dirCreate.exists())
						dirCreate.mkdir();
				}
				else
				{
					// Get file name
					String outFilename = (ze.getName());
					vtReturn.addElement(outFilename);

					// Open the output file
					outFilename = outFolder + outFilename;
					OutputStream out = new FileOutputStream(outFilename);
					in.getNextEntry();

					// Transfer bytes from the ZIP file to the output file
					byte[] buf = new byte[BUFFER_SIZE];
					int len;
					while ((len = in.read(buf)) > 0)
						out.write(buf, 0, len);

					// Close the streams
					out.close();
				}
			}
		}
		catch (ClassCastException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			if (in != null)
				in.close();
			if (zf != null)
				zf.close();
		}
		return vtReturn;
	}

	// /////////////////////////////////////////////////////////////////////////
	// GZIP section
	// /////////////////////////////////////////////////////////////////////////
	public static void GZip(String strSource, String strDestination) throws IOException
	{
		// Create a buffer for reading the files
		byte[] buf = new byte[BUFFER_SIZE];

		try
		{
			FileInputStream in = new FileInputStream(strSource);
			GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(strDestination));

			// Compress the files
			int iReadCount = 0;
			while ((iReadCount = in.read(buf)) >= 0)
				out.write(buf, 0, iReadCount);

			// Complete the ZIP file
			in.close();
			out.close();
		}
		catch (IOException e)
		{
			throw e;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	public static void GUnZip(String strSource, String strDestination) throws IOException
	{
		// Create a buffer for reading the files
		byte[] buf = new byte[BUFFER_SIZE];

		try
		{
			GZIPInputStream in = new GZIPInputStream(new FileInputStream(strSource));
			FileOutputStream out = new FileOutputStream(strDestination);

			// DeCompress the files
			int iReadCount = 0;
			while ((iReadCount = in.read(buf)) >= 0)
				out.write(buf, 0, iReadCount);

			in.close();
			out.close();
		}
		catch (IOException e)
		{
			throw e;
		}
	}
}
