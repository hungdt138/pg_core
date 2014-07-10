package com.crm.provisioning.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.thread.MailThread;
import com.crm.thread.util.ThreadUtil;
import com.fss.util.AppException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class TXTReportThread extends MailThread
{
	private PreparedStatement stmtQueue = null;
	private PreparedStatement stmtCheck = null;
	private Connection connection = null;

	protected double productPrice = 0;
	protected String mailContent = "";
	protected String fileName = "";
	protected String backupDir = "";
	protected String serverDir = "";
	protected String serverIP = "";
	protected String serverUsername = "";
	protected String serverPassword = "";
	protected String _strSQL = "";
	protected String _strSQLCheck = "";
	protected String timeRun = "";
	protected int timeDelay = 0;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(ThreadUtil.createTextParameter("SQL", 100, ""));
		vtReturn.addElement(ThreadUtil.createTextParameter("SQLCheck", 100, ""));
		vtReturn.addElement(ThreadUtil.createTextParameter("TimeRun", 100, ""));
		vtReturn.addElement(ThreadUtil
				.createTextParameter("TimeDelay", 100, ""));
		vtReturn.addElement(ThreadUtil.createTextParameter("productPrice", 100,
				""));
		vtReturn.addElement(ThreadUtil.createTextParameter("fileName", 100, ""));
		vtReturn.addElement(ThreadUtil
				.createTextParameter("backupDir", 100, ""));
		vtReturn.addElement(ThreadUtil.createTextParameter("fileServerDir",
				100, ""));
		vtReturn.addElement(ThreadUtil.createTextParameter("fileServerIP", 100,
				""));
		vtReturn.addElement(ThreadUtil.createTextParameter(
				"fileServerUsername", 100, ""));
		vtReturn.addElement(ThreadUtil.createPasswordParameter(
				"fileServerPassword", ""));
		vtReturn.addElement(ThreadUtil.createTextParameter("mailContent", 1000,
				""));

		vtReturn.addAll(super.getParameterDefinition());

		return vtReturn;
	}

	@Override
	public void fillParameter() throws AppException
	{
		super.fillParameter();

		_strSQL = ThreadUtil.getString(this, "SQL", false, "");
		_strSQLCheck = ThreadUtil.getString(this, "SQLCheck", false, "");
		timeRun = ThreadUtil.getString(this, "TimeRun", false, "");
		timeDelay = ThreadUtil.getInt(this, "TimeDelay", 0);
		productPrice = ThreadUtil.getDouble(this, "productPrice", 0);
		fileName = ThreadUtil.getString(this, "fileName", false, "");
		backupDir = ThreadUtil.getString(this, "backupDir", false, "");
		serverDir = ThreadUtil.getString(this, "fileServerDir", false, "");
		serverIP = ThreadUtil.getString(this, "fileServerIP", false, "");
		serverUsername = ThreadUtil.getString(this, "fileServerUsername",
				false, "");
		serverPassword = ThreadUtil.getString(this, "fileServerPassword",
				false, "");
		mailContent = ThreadUtil.getString(this, "mailContent", false, "");
	}

	@Override
	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			// setAutoLoop(false);

			String strSQL = _strSQLCheck;
			connection = Database.getConnection();
			stmtCheck = connection.prepareStatement(strSQL);

			logMonitor("SQL check: " + strSQL);

			strSQL = _strSQL.replaceAll("<%DATE%>", timeRun);
			stmtQueue = connection.prepareStatement(strSQL);

			logMonitor("SQL get data: " + strSQL);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public void afterProcessSession() throws Exception
	{
		try
		{
			Database.closeObject(stmtQueue);
			Database.closeObject(stmtCheck);
			Database.closeObject(connection);
		}
		finally
		{
			super.afterProcessSession();
		}
	}

	@Override
	public void doProcessSession() throws Exception
	{
		ResultSet result = null;
		try
		{
			boolean sendReportFLG = false;
			int total = 0;
			int count = 0;

			while (!sendReportFLG)
			{
				result = stmtCheck.executeQuery();
				while (result.next())
				{
					total = result.getInt("total");
				}

				logMonitor("Total records subscription in queue: " + total);

				if (total == 0)
				{
					sendReportFLG = true;
				}
				else
				{
					logMonitor("Delay Time: " + timeDelay);
					Thread.sleep(timeDelay * 1000);
				}
			}
			result.close();

			Thread.sleep(timeDelay * 1000);

			StringBuilder contents = new StringBuilder();

			result = stmtQueue.executeQuery();
			while (result.next())
			{
				String isdn = result.getString("isdn");
				int remainingDays = result.getInt("quantity");
				int price = result.getInt("amount");
				String subscriberType = result.getInt("subscriberType") == 1 ? "PREPAID" : "POSTPAID";
				String orderDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(result.getTimestamp("orderDate"));
				String orderType = result.getString("orderType");

				if (orderType.equals(Constants.ACTION_SUPPLIER_DEACTIVE))
				{
					contents.append(isdn + ",1,FAIL,INSUFFICIENT_BALANCE," + subscriberType + ",,,,"
							+ orderDate + "\n");
				}
				else if (orderType.equals(Constants.ACTION_SUBSCRIPTION))
				{
					if (remainingDays == 1 && price == productPrice)
					{
						contents.append(isdn
								+ ",1,SUCCESS,NONE," + subscriberType + "," + price + ",NORMAL,0,"
								+ orderDate + "\n");
					}
					else
					{
						contents.append(isdn
								+ ",1,SUCCESS,NONE," + subscriberType + "," + price + ",PARTIAL,"
								+ remainingDays + "," + orderDate + "\n");
					}
				}

				count++;
			}

			logMonitor("Total subscribers has been renewed: " + count);

			File file = new File(backupDir);
			if (!file.exists())
			{
				file.mkdirs();
			}

			String exportingFile = backupDir;
			if (!exportingFile.endsWith("\\") && !exportingFile.endsWith("/"))
				exportingFile = exportingFile + "/";

			exportingFile = exportingFile
					+ fileName.replaceAll("<%DATE%>", timeRun);

			logMonitor("File upload: " + exportingFile);

			FileWriter outFile = new FileWriter(exportingFile);
			BufferedWriter out = new BufferedWriter(outFile);
			out.write(contents.toString());
			out.close();

			logMonitor("Create file done.");

			file = new File(exportingFile);
			if (!file.exists())
			{
				logMonitor("File does'n exist!!!");
			}
			else
			{
				boolean check = sendFileToServer(exportingFile, serverDir,
						serverIP, serverUsername, serverPassword);

				if (check)
				{
					sendEmail(formatSubject(null), mailContent, exportingFile);

					Calendar timeToRun = Calendar.getInstance();
					timeToRun.setTime(new SimpleDateFormat("yyyyMMdd")
							.parse(timeRun));
					timeToRun.add(Calendar.DATE, 1);
					timeRun = new SimpleDateFormat("yyyyMMdd").format(timeToRun
							.getTime());
					mprtParam.setProperty("TimeRun", timeRun);
				}
			}

			logMonitor("Upload file done.");
			
			storeConfig();
		}
		catch (Exception e)
		{
			logMonitor(e.getMessage());
			throw e;
		}
		finally
		{
			Database.closeObject(result);
		}
	}

	public boolean sendFileToServer(String filePath, String destFolder,
			String host, String username, String pass) throws IOException
	{
		boolean vStatus = false;

		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;

		try
		{
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, 22);
			session.setPassword(pass);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(destFolder);
			File f = new File(filePath);
			channelSftp.put(new FileInputStream(f), f.getName());

			vStatus = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			channelSftp.exit();
			channel.disconnect();
			session.disconnect();
		}
		return vStatus;
	}
}
