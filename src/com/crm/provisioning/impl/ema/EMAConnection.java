package com.crm.provisioning.impl.ema;

import java.util.Date;

import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.thread.EMACommandThread;
import com.crm.provisioning.thread.ProvisioningThread;

public class EMAConnection extends ProvisioningConnection
{
	private TelnetWrapper	telnet;

	private String			mstrPrompt				= "\nEnter command: ";
	private String			mstrAdditionalCommand	= ";\n";

	public boolean			isOpen					= false;
	public boolean			isLogin					= false;
	public boolean			isSending				= false;

	public EMAConnection()
	{
		super();
	}

	public boolean openConnection() throws Exception
	{
		try
		{
			// Open connection to telnet server
			isOpen = false;
			isLogin = false;
			telnet = new TelnetWrapper(host, port);
			
			telnet.setTcpNoDelay(true);
			
			isOpen = true;

			try
			{
				telnet.setSoTimeout((int)timeout);
				telnet.send("LOGIN:" + userName + ":" + password + ";\r\n");
				telnet.receiveUntil(";");
			}
			catch (Exception ex)
			{
				throw ex;
			}

			isLogin = true;
		}
		catch (Exception ex)
		{
			try
			{
				if (isOpen)
				{
					telnet.disconnect();

					isOpen = false;
				}
			}
			catch (Exception e1)
			{
				dispatcher.logMonitor(e1);
			}

			telnet = null;
		}

		setClosed(!isOpen);

		return (telnet != null);
	}

	public boolean closeConnection()
	{
		try
		{
			if (isLogin)
			{
				try
				{
//					executeCommand("LOGOUT");
					telnet.send("LOGOUT;\n");
					telnet.receiveUntil(";");
				}
				catch (Exception ex)
				{
					dispatcher.logMonitor(ex);
				}
			}
		}
		catch (Exception e)
		{
			dispatcher.logMonitor(e);
		}

		try
		{
			if (telnet != null)
			{
				telnet.disconnect();
			}
			isOpen = false;
		}
		catch (Exception e)
		{
			dispatcher.logMonitor(e);
		}
		finally
		{
			telnet = null;
		}

		return true;
	}
	
	@Override
	public boolean validate() throws Exception
	{
		boolean check = false;
		try
		{
			Date now = new Date();
			if (now.getTime() - getLastRun().getTime() < ((EMACommandThread)getDispatcher()).enquireInterval)
			{
				return true;
			}
			
			String command ="SET:HLRSUB:MSISDN,84922123123:NAM,0";
			   
		   logMonitor(getConnectionId() + ": SEND-KEEP-CONNECTION: SET:HLRSUB:MSISDN,84922123123:NAM,0");
		   
		   telnet.send(command + mstrAdditionalCommand);
		   String receive = telnet.receiveUntil(";");
		   
		   logMonitor(getConnectionId() + ": RECEIVE-KEEP-CONNECTION: " + receive.replaceAll("\nEnter command: ", ""));
		   check = true;
		   
		   setLastRun(new Date());
		}
		catch (Exception e)
		{
			dispatcher.debugMonitor(e.getMessage());
			throw e;
		}
		return check;
	}

	public String executeCommand(String strCommand) throws Exception
	{
		// Clear buffer
		if (telnet == null)
		{
			throw new Exception("Connection to server closed");
		}
		String str = telnet.receive();

		// Send command
		String strSend = strCommand + mstrAdditionalCommand;
		telnet.send(strSend);
		str = telnet.receiveUntilEx(mstrPrompt, timeout);

		// Remove prompt & correct response
		str = str.substring(0, str.length() - mstrPrompt.length());

		return str;
	}

	public String executeCommand(String template, String isdn) throws Exception
	{
		try
		{
			String command = template.replaceAll("<ISDN>", isdn);

			return executeCommand(command);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	public static void main(String[] params)
	{
		EMAConnection connection = new EMAConnection();
		connection.setHost("192.168.194.4");
		connection.setPort(3300);
		connection.setUserName("selfcare");
		connection.setPassword("ematest");
		connection.setTimeout(1000);
		
		try
		{
			connection.openConnection();
			
			for (int i = 0; i < 10; i++)
			{
//				System.out.println("Check connection =" + connection.validate());
				Thread.sleep(1000);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			connection.closeConnection();
		}
		
	}
	
	@Override
	public void setDispatcher(ProvisioningThread dispatcher)
	{
		// TODO Auto-generated method stub
		super.setDispatcher((EMACommandThread)dispatcher);
	}
}
