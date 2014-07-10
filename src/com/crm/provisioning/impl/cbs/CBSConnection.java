package com.crm.provisioning.impl.cbs;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.provisioning.cache.ProvisioningConnection;
import cbapiws.CBWS;
import cbapiws.accountResponse;
import cbapiws.genResponse;

public class CBSConnection extends ProvisioningConnection
{
	private CBWS service;
	private genResponse response;

	public CBSConnection() throws Exception
	{
		setHost("192.168.96.30");
		setPort(80);
		setUserName("vnmtest");
		setPassword("vnmpwdtest");
		setTimeout(15000);
	}

	private String getURL(String host, int port) throws Exception
	{
		String strUrl = "http://" + host + ":" + port
				+ "/cbwebservice/cbwebservice.php";

		return strUrl;
	}

	public boolean openConnection() throws Exception
	{
		String url = getURL(getHost(), getPort());

		try
		{
			service = new CBWS(url, getUserName(), getPassword());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return super.openConnection();
	}

	public int register(String isdn, String packageType, int type)
			throws Exception
	{
		response = service.addAccount(isdn, packageType, type);
		return response.getResCode();
	}

	public int unregister(String isdn) throws Exception
	{
		response = service.removeAccount(isdn);
		return response.getResCode();
	}

	public int reactive(String isdn) throws Exception
	{
		response = service.enableAccount(isdn);
		return response.getResCode();
	}

	public int deactive(String isdn) throws Exception
	{
		response = service.disableAccount(isdn);
		return response.getResCode();
	}

	public boolean getAccount(String isdn) throws Exception
	{
		accountResponse account = service.getAccount(isdn);
		if (account != null && account.getStatus() == 0)
			return true;
		else
			return false;
	}

	public int addBlackList(String isdn, String blackListMember)
			throws Exception
	{
		response = service.addBlackListMember(isdn, blackListMember);
		return response.getResCode();
	}

	public int removeBlackList(String isdn, String blackListMember)
			throws Exception
	{
		response = service.removeBlackListMember(isdn, blackListMember);
		return response.getResCode();
	}

	public String getBlackList(String isdn) throws Exception
	{
		String list = "";
		response = service.getBlackList(isdn);
		if (response.getResCode() == 0)
		{
			list = response.getDescript();
		}
		return list;
	}

	public int addWhiteList(String isdn, String whiteListMember)
			throws Exception
	{
		response = service.addWhiteListMember(isdn, whiteListMember);
		return response.getResCode();
	}

	public int removeWhiteList(String isdn, String whiteListMember)
			throws Exception
	{
		response = service.removeWhiteListMember(isdn, whiteListMember);
		return response.getResCode();
	}

	public int updatePackage(String isdn, String packageType)
			throws Exception
	{
		response = service.updatePackage(isdn, packageType);
		return response.getResCode();
	}

	public int switchList(String isdn, int listType) throws Exception
	{
		response = service.switchBlacklistWhitelist(isdn, listType);
		return response.getResCode();
	}

	public int removeAllList(String isdn) throws Exception
	{
		response = service.removeAllBlackWhiteListMember(isdn);
		return response.getResCode();
	}

	public int removeAllBlackList(String isdn) throws Exception
	{
		response = service.removeAllBlackListMember(isdn);
		return response.getResCode();
	}

	public int removeAllWhiteList(String isdn) throws Exception
	{
		response = service.removeAllWhiteListMember(isdn);
		return response.getResCode();
	}

	public int logCharge(String isdn, String amount, int action, Date expire)
			throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		response = service.insertCharging(isdn, amount, action,
				sdf.format(expire));
		return response.getResCode();
	}

	public String getWhiteList(String isdn) throws Exception
	{
		String list = "";
		response = service.getWhiteList(isdn);
		if (response.getResCode() == 0)
		{
			list = response.getDescript();
		}
		return list;
	}

	public String getResponse(int responseCode) throws Exception
	{
		String response = "";
		
		switch (responseCode)
		{
			case 0:
				response = Constants.SUCCESS;
				break;
			case -1:
				response = "system-error";
				break;
			case 101:
				response = "registered";
				break;
			case 102:
				response = "register-error";
				break;
			case 103:
				response = "not-registered";
				break;
			case 105:
				response = "unregister-error";
				break;
			case 107:
				response = "deactive-error";
				break;
			case 108:
				response = "reactive-error";
				break;
			case 109:
				response = "deactived";
				break;
			case 111:
				response = "actived";
				break;
			case 304:
				response = "error-turnoff";
				break;
			case 401:
				response = "member-exist";
				break;
			case 402:
				response = "add-member-error";
				break;
			case 403:
				response = "member-not-exist";
				break;
			case 405:
				response = "remove-member-error";
				break;
			case 703:
				response = "list-not-exist";
				break;
			case 904:
				response = "package-exist";
				break;
			case 905:
				response = "invalid-list-type";
				break;
			case 907:
				response = "max-member";
				break;
			case 908:
				response = "type-already";
				break;
			case 920:
				response = "log-charge-fail";
				break;
			case 930:
				response = "wrong-format";
				break;
			case 940:
				response = "success-switch";
				break;
		}
		
		return response;
	}

	public genResponse getResponse()
	{
		return response;
	}

	public void genResponse(genResponse response)
	{
		this.response = response;
	}

	public static void main(String args[])
	{
		try
		{
			CBSConnection connection = new CBSConnection();
			connection.setHost("192.168.96.30");
			connection.setPort(80);
			connection.setUserName("vnmtest");
			connection.setPassword("vnmpwdtest");
			connection.openConnection();
			String response = "";
//			response = connection.register("84922000511", "BASIC", 0);
//			 response = connection.unregister("84922000511");
			// response = connection.updatePackage("84922000512","BASIC");
			// response = connection.reactive(request);
			// response = connection.renewal(request, 601);
			// response = connection.switchList("84922000512", 2);
			// response = connection.addWhiteList("84922000512","84922000514");
			// response = connection.getWhiteList("84922000512");
//			System.out.print(response);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
