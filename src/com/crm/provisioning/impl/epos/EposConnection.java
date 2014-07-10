package com.crm.provisioning.impl.epos; 

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;

import org.apache.axis.AxisFault;

import com.crm.kernel.message.Constants;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.thread.EPOSCommandThread;
import com.crm.provisioning.thread.ProvisioningThread;
import com.fss.SMSUtility.BasicInput;
import com.fss.SMSUtility.LoginOutput;
import com.fss.SMSUtility.services.APISoapBindingStub;

public class EposConnection extends ProvisioningConnection 
{
	private APISoapBindingStub _connection = null;
	private LoginOutput login = null;
	
	public boolean isLogin = false;
	
	public EposConnection()
	{
		super();
	}
	
	public EposConnection(String host, int port, String user, String pass)
	{
		setHost(host);
		setPort(port);
		setUserName(user);
		setPassword(pass);
	}
	
	private URL getURL(String host, int port) throws Exception
	{
		String strUrl = "http://" + host + ":" + port + "/eload/services/API";

		URL url = new URL(strUrl);
		return url;
	}
	
	public boolean openConnection() throws Exception
	{
		loadService();
		return super.openConnection();
	}
	public void loadService() throws Exception
	{
		try
		{
			_connection = new APISoapBindingStub(getURL(getHost(), getPort()), null);
			
			synchronized(getDispatcher().syncLogin)
			{
				isLogin = getDispatcher().isLogin;
				if (!isLogin)
				{
					getToken();
				}
			}
		}
		catch (AxisFault ex)
		{
			getDispatcher().logMonitor(ex);
			throw ex;
		}
		catch (RemoteException e)
		{
			getDispatcher().logMonitor(e);
			throw e;
		}
	}
	
	public void getToken() throws Exception
	{
		BasicInput input = new BasicInput();
		input.setUsername(getUserName());
		input.setPassword(getPassword());
		
		this.login = _connection.getTokenNow(input);
		if (this.login.getStatus().equals("0"))
		{
			getDispatcher().isLogin = true;
			
			input.setTokenKey(this.login.getSessionid());
			getDispatcher().setInput(input);
			
			getDispatcher().hasError = false;
		}
	}
	
	/**
	 * Purpose: Change COS for 3G register/cancel.
	 * @param _instance
	 * @param command
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String changeCOS (String isdn, String Option, String packageType,boolean isPostpaid) throws Exception
	{
		boolean  success = false;
		String responseCode = "";
		
		try
		{						
			if (isPostpaid)
			{
				success = _connection.changeCOS4POS(isdn, Option, packageType, getDispatcher().getInput());
			}
			else
			{
				success = _connection.changeCos4Pre(isdn, Option, getDispatcher().getInput());
			}
			if (success)
			{
				responseCode = Constants.SUCCESS;
			}
			
			getDispatcher().setLastRun(new Date());
		}
		catch (Exception ex)
		{
			responseCode = ePosErrorHandle(ex.getMessage());
		}
		
		return responseCode;
	}
	
	public String convertToDataSim (String isdn, String Option) throws Exception
	{
		boolean  success = false;
		String responseCode = "";

		try
		{
			success = _connection.changeCos4PreCustom(isdn, Option, getDispatcher().getInput());
			if (success)
			{
				responseCode = Constants.SUCCESS;
			}
			
			getDispatcher().setLastRun(new Date());
		}
		catch (Exception ex)
		{
			responseCode = ePosErrorHandle(ex.getMessage());
		}
		
		return responseCode;
	}
	
	@Override
	public boolean validate() throws Exception
	{
		boolean check = false;
			
		try
		{
			Date now = new Date();
			if ((now.getTime() - getDispatcher().getLastRun().getTime()) < (getDispatcher().enquireInterval * 1000)
					&& !getDispatcher().hasError)
			{
				return true;
			}
			
			closeConnection();
			
			synchronized (getDispatcher().syncLogin)
			{
				getDispatcher().logMonitor("GET-TOKEN-EPOS-START");
				getToken();
				getDispatcher().logMonitor("GET-TOKEN-EPOS-FINISH");
			}
			
			check = true;
   
			getDispatcher().setLastRun(new Date());
		}
		catch (Exception e)
		{
			getDispatcher().debugMonitor(e.getMessage());
			throw e;
		}
		return check;
	}
	
	public boolean closeConnection()
	{
		try
		{
			getDispatcher().isLogin = false;
		}
		catch (Exception e)
		{
			getDispatcher().logMonitor(e);
		}
		
		return true;
	}
	/**
	 * Purpose: Handle error from EPOS.
	 * @param errorDetail
	 * @return
	 */
	public String ePosErrorHandle(String errorDetail)
	{
		String response = "";
		
		if (errorDetail == null)
		{
			return response;
		}
		
		if (errorDetail.contains("API"))
		{
			response = errorDetail.substring(errorDetail.indexOf("API"), errorDetail.indexOf(":"));
		}
		else if (errorDetail.contains("IERR"))
		{
			response = errorDetail.substring(errorDetail.indexOf("IERR"));
		}
		else if (errorDetail.contains("OOM3"))
		{
			response = errorDetail.substring(errorDetail.indexOf("OOM3"), errorDetail.indexOf(":"));
		}
		else
		{
			response = errorDetail;
		}
		
		if (response.equals("API-00003")
				|| response.equals("API-00008")
				|| response.equals("API-00011"))
		{
			getDispatcher().hasError = true;
		}
			
		return response;
	}
	
	public LoginOutput getLogin()
	{
		return login;
	}
	public void setLogin(LoginOutput login)
	{
		this.login = login;
	}	
	
	public static void main (String args[])
	{
		try
		{
//			String strUrl = "http://" + "10.8.13.61" + ":" + "7865" + "/eload/services/API";
//
//			URL url = new URL(strUrl);
//			
//			APISoapBindingStub _connection = new APISoapBindingStub(url, null);
//			
//			BasicInput input = new BasicInput();
//			input.setUsername("APITEST2");
//			input.setPassword("fCIvspJ9goryL1khNOiTJIBjfA0=");
//			input.setUsername("TRINHCTT2");
//			input.setPassword("12");
//			LoginOutput login = _connection.getTokenNow(input);
//			if (login.getStatus().equals("0"))
//			{
//				input.setTokenKey(login.getSessionid());
//			}
			
//			boolean response = _connection.changeCos4Pre("0922000515", "UNREGISTER", input);
//			boolean response = _connection.changeCos4PreCustom("0922000515", "VMAX", input);
//			System.out.print(response);
		}
		catch (Exception e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void setDispatcher(ProvisioningThread dispatcher)
	{
		super.setDispatcher((EPOSCommandThread)dispatcher);
	}
	
	public EPOSCommandThread getDispatcher()
	{
		return (EPOSCommandThread)super.getDispatcher();
	}
}
