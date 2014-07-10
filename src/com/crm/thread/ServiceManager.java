package com.crm.thread;

import java.io.*;
import java.sql.*;

import com.crm.kernel.sql.Database;
import com.crm.util.ParameterMap;

import com.fss.thread.ProcessorListener;
import com.fss.thread.ThreadConstant;
import com.fss.thread.ThreadManager;
import com.fss.thread.ThreadProcessor;
import com.fss.util.DBTableAuthenticator;
import com.fss.util.Global;
import com.fss.util.LogOutputStream;

public class ServiceManager implements ProcessorListener
{
	// //////////////////////////////////////////////////////
	// Variables
	// //////////////////////////////////////////////////////
	public static ParameterMap	configuration	= new ParameterMap();

	// //////////////////////////////////////////////////////
	// Constructor
	// //////////////////////////////////////////////////////
	public ServiceManager(String strConfigPath) throws Exception
	{
		try
		{
			loadServerConfig(strConfigPath);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// Purpose: Load parameter from resource
	// Date: 29/04/2003
	// //////////////////////////////////////////////////////
	public void loadServerConfig(String fileName) throws Exception
	{
		// mprtConfig =
		// Global.loadProperties(ServiceManager.class.getResourceAsStream(strFileName));
		configuration.loadFromFile(fileName);
	}

	// //////////////////////////////////////////////////////
	// Purpose: Get parameter from memory
	// Date: 29/04/2003
	// //////////////////////////////////////////////////////
	public String getParameter(String strKey)
	{
		return configuration.getString(strKey, "");
	}

	public void onCreate(ThreadProcessor processor) throws Exception
	{
		if (getParameter("DatabaseExist").equals("N"))
		{
			return;
		}
		// processor.log = new DBTableLogUtil();
		processor.authenticator = new com.fss.util.DBTableAuthenticator();
	}

	public void onOpen(ThreadProcessor processor) throws Exception
	{
		if (getParameter("DatabaseExist").equals("N"))
		{
			return;
		}
		processor.mcnMain = getConnection();
		// ((DBTableLogUtil)processor.log).setConnection(processor.mcnMain);
		// if (processor.channel != null && processor.channel.msckMain != null)
		// {
		// ((DBTableLogUtil)processor.log).setIPAddress(processor.channel.msckMain.getInetAddress().getHostAddress());
		// }
		((DBTableAuthenticator) processor.authenticator).setConnection(processor.mcnMain);
	}

	public void runService()
	{
		try
		{
			// Change system output to file
			String strLogFile = getParameter("LogFile");
			if (strLogFile == null || strLogFile.length() == 0)
			{
				strLogFile = "error.log";
			}
			String strWorkingDir = System.getProperty("user.dir");
			if (!strWorkingDir.endsWith("/") || !strWorkingDir.endsWith("\\"))
			{
				strWorkingDir += "/";
			}
			PrintStream ps = new PrintStream(new LogOutputStream(strWorkingDir + strLogFile));
			System.setOut(ps);
			System.setErr(ps);

			// Application name
			Global.APP_NAME = ThreadConstant.APP_NAME;
			Global.APP_VERSION = ThreadConstant.APP_VERSION;

			// Port id
			int iPortID = 8338;
			try
			{
				iPortID = Integer.parseInt(getParameter("PortID"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			ThreadManager cs = new ThreadManager(iPortID, this);

			// Set action log file
			strLogFile = getParameter("ActionLogFile");
			if (strLogFile != null && strLogFile.length() > 0)
			{
				cs.setActionLogFile(strLogFile);
			}

			// Set max logfile size
			try
			{
				if (getParameter("MaxLogFileSize").length() > 0)
				{
					int iMaxLogFileSize = Integer.parseInt(getParameter("MaxLogFileSize"));
					if (iMaxLogFileSize > 0)
					{
						cs.setMaxLogFileSize(iMaxLogFileSize);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			// Set MaxLogContentSize
			try
			{
				if (getParameter("MaxLogContentSize").length() > 0)
				{
					int iMaxLogContentSize = Integer.parseInt(getParameter("MaxLogContentSize"));
					if (iMaxLogContentSize > 0)
					{
						cs.setMaxLogContentSize(iMaxLogContentSize);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			// Set max connection
			try
			{
				int iMaxConnectionAllowed = Integer.parseInt(getParameter("MaxConnectionAllowed"));
				
				if (iMaxConnectionAllowed > 0)
				{
					cs.setMaxConnectionAllowed(iMaxConnectionAllowed);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			// Set loading method
			try
			{
				int iLoadingMethod = Integer.parseInt(getParameter("LoadingMethod"));
				if (iLoadingMethod == ThreadManager.LOAD_FROM_FILE ||
						iLoadingMethod == ThreadManager.LOAD_FROM_DB ||
						iLoadingMethod == ThreadManager.LOAD_FROM_FILE_AND_DB)
				{
					cs.setLoadingMethod(iLoadingMethod);
				}
			}
			catch (Exception e)
			{
				String strLoadingMethod = getParameter("LoadingMethod");
				if (strLoadingMethod.equals("Database"))
				{
					cs.setLoadingMethod(ThreadManager.LOAD_FROM_DB);
				}
				else if (strLoadingMethod.equals("File"))
				{
					cs.setLoadingMethod(ThreadManager.LOAD_FROM_FILE);
				}
				else
				{
					cs.setLoadingMethod(ThreadManager.LOAD_FROM_FILE_AND_DB);
				}
			}

			// Start manager
			cs.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Connection getConnection() throws Exception
	{
		return Database.getConnection();
	}

	// /////////////////////////////////////////////////////////
	// Main entry
	// /////////////////////////////////////////////////////////
	public static void main(String argvs[])
	{
		// BasicConfigurator.configure();

		// String strConfigPath = "/com/sdp/thread/ServerConfig.txt";
		String strConfigPath = "ServerConfig.txt";
		try
		{
			ServiceManager service = new ServiceManager(strConfigPath);
			service.runService();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
