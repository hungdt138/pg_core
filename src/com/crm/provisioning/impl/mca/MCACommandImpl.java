package com.crm.provisioning.impl.mca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductMessage;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.fss.util.StringUtil;

public class MCACommandImpl extends CommandImpl {

	// "unknow";
	public static final int		UNKNOW_RESPONSE				= 1;
	// "success";
	public static final int		SUCCESS_RESPONSE			= 0;
	public final static String	COUNTRY_CODE				= "84";
	public final static String	DOMESTIC_CODE				= "0";
	public static final int		ERROR_RESPONSE				= 2;
	public final static int	MAX_SMS_CONTENT	= 160;
	public static final String	DEFAULT_ERROR_MESSAGE		= "Loi xay ra trong qua trinh xu ly. ";
	
	public String executeSingleCommand(CommandInstance instance, ProvisioningCommand provisioningCommand,
									CommandMessage order, Boolean ignoreError ,
									String command, String expectedResults ,
									String expectedCode, String errorCode) throws Exception
	{
		String responseMessage = "";
		String responseCode = "";
		String strLanguage ="";
		String isdn = order.getIsdn();
		if (isdn.equals(""))
			isdn = StringUtil.nvl(order.getIsdn(), "");
		
		MCAConnection _connection =null;
		try{

			String strCommand = command.replaceAll("<MSISDN>", isdn);
			
			_connection = (MCAConnection) instance.getProvisioningConnection();
			
			long sessionId = setRequest(instance, order, strCommand);
			responseMessage = _connection.processCommmand(strCommand);
			setResponse(instance, order, responseMessage, sessionId);
			
			strLanguage = getLanguage(isdn, _connection);
			
			boolean found = false;
			String[] arrExpecteds = StringUtil.toStringArray(expectedResults, ";");
			
			for (int j = 0; !found && (j < arrExpecteds.length); j++)
			{
				if (!arrExpecteds[j].equals("") && responseMessage.contains(arrExpecteds[j]))
				{
					found = true;
					responseCode = expectedCode;
				}
			}
			
			if (!found && ignoreError)
			{
				responseCode = expectedCode;
			}
			
			if (!responseCode.equals(expectedCode))
			{
				if(!errorCode.equals("by-pass"))
				{
					responseCode = errorCode;
	
					if (isAllowSendSMS(instance, order))
					{
						String smsContent ="";
						if ("".equals(strLanguage))
							smsContent = getProperty("message." + errorCode , DEFAULT_ERROR_MESSAGE);
						else 
							smsContent = getProperty("message." + errorCode + "." + strLanguage , DEFAULT_ERROR_MESSAGE);
	
						CommandUtil.sendSMS(instance, order, smsContent);
					}
				}
				else
					responseCode = getProperty("exptectedCode", "success");
			}
//			order.setResponse(responseCode);
		}catch (Exception ex)
		{
			processError(instance, provisioningCommand, order, ex);	
		}
		finally{			
			instance.closeProvisioningConnection(_connection);
		}
		return responseCode;
		
		
	}
	public int executeSingleCommand(CommandInstance instance,ProvisioningCommand provisioningCommand,
									CommandMessage order, String prefix ) throws Exception
	{
		int result = UNKNOW_RESPONSE;
		String strLanguage ="";
		String responseMessage = "";
		String responseCode = "";
		String isdn = order.getIsdn();
		MCAConnection _connection =null;
		
		if (isdn.equals(""))
			isdn = StringUtil.nvl(order.getIsdn(), "");
			
		isdn = transformISDN(isdn);
	
		if (prefix.equals(""))
			prefix = ".";
		else
			prefix = "." + prefix + ".";
		
		try{
			String template = provisioningCommand.getParameter("command" + prefix + "template", "");
			
			String expectedResult = provisioningCommand.getParameter("command" + prefix + "expectedResult", "");
			String expectedCode = provisioningCommand.getParameter("command" + prefix + "expectedCode", "");
			String errorCode = provisioningCommand.getParameter("command" + prefix + "errorCode", "");
			
			if (order.getQuantity() > 0 && (order.getOfferPrice() > order.getAmount()))
			{
				if(!provisioningCommand.getParameter("Parameter", "").equals(""))
				{
					template = template.replaceFirst("NORMAL,0", provisioningCommand.getParameter("Parameter","") + 
							order.getQuantity());
				}
			}
			
			String strCommand = template.replaceAll("<MSISDN>", isdn);
			
			_connection = (MCAConnection) instance.getProvisioningConnection();
			
//			order.setRequest(strCommand);
			
			if (strCommand.equals(""))
			{
				responseCode = "missing-template";			
			}
			
			long sessionId = setRequest(instance, order, strCommand);
			responseMessage = _connection.processCommmand(strCommand);
			setResponse(instance, order, responseMessage, sessionId);
			
			if("".equals(responseCode))
			{
				return ERROR_RESPONSE;
			}
			
			boolean found = false;
			String[] arrExpecteds = StringUtil.toStringArray(expectedResult, ";");
			
			for (int j = 0; !found && (j < arrExpecteds.length); j++)
			{
				if (!arrExpecteds[j].equals("") && responseMessage.contains(arrExpecteds[j]))
				{
					found = true;
					responseCode = expectedCode;
				}
			}
			
			if (!responseCode.equals(expectedCode))
			{
				responseCode = errorCode;
			}
			
//			order.setResponse(responseCode);
			result = SUCCESS_RESPONSE;
		}catch (Exception ex){
			processError(instance, provisioningCommand, order, ex);			
		}
		finally{
			instance.closeProvisioningConnection(_connection);
		}
		
		return result;
	}
	public int  executeMutilCommand(CommandInstance instance,ProvisioningCommand provisioningCommand,
										CommandMessage order, String productName ) throws Exception
	{
		int result = UNKNOW_RESPONSE;
		String responseCode = "";
		boolean isRecursive = false;
		
		try
		{
			if (provisioningCommand.getParameter("subCommands", "").equals(""))
			{
				result = executeSingleCommand(instance, provisioningCommand, order, productName);
				responseCode = order.getResponse();
			}
			else
			{
				String[] subCommands = StringUtil.toStringArray(provisioningCommand.getParameter("subCommands", ";"), ";");
				String prefix = "";
				String node = subCommands[0];
				while(true)
				{
					prefix = "command." + node + ".";	
					String template =  provisioningCommand.getParameter(prefix + productName + ".template", "");
					String expectedResult =  provisioningCommand.getParameter(prefix + productName + ".expectedResult", "");
					String expectedCode =  provisioningCommand.getParameter(prefix + productName + ".expectedCode", "");
					String errorCode =  provisioningCommand.getParameter(prefix + productName + ".errorCode", "");
					responseCode =
							executeSingleCommand(instance, provisioningCommand, order,
									 provisioningCommand.getParameter(prefix + "ignoreError", "false").equals("true"),
									  template, expectedResult, expectedCode, errorCode);
					// execute to next subCommand
					boolean found = false;
	
					for (int j = 0; !found && (j < subCommands.length); j++)
					{
						found = subCommands[j].equals(responseCode);
	
						// prevent recurrent declaration
						if (found)
						{
							if (responseCode.equals(node))
							{
								isRecursive = true;
							}
							else
							{
								node = subCommands[j];
							}
						}
					}
	
					if (isRecursive || !found)
					{
						break;
					}
				}
			}
			
//			order.setResponse(responseCode);
			result = SUCCESS_RESPONSE;
		}catch (Exception e)
		{
			processError(instance, provisioningCommand, order, e);	
		}
		
		return result;
	
	}
	public VNMMessage registerService(CommandInstance instance,ProvisioningCommand provisioningCommand, CommandMessage order ) throws Exception
	{
		VNMMessage result = CommandUtil.createVNMMessage(order);
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			ProductEntry product = ProductFactory.getCache().getProduct(result.getProductId());
			int resultRespone = executeMutilCommand(instance, provisioningCommand, result, product.getAlias());
			if(resultRespone == SUCCESS_RESPONSE)
			{
				result.setCause(Constants.SUCCESS);
			}
			else
			{
				result.setCause(Constants.ERROR);
			}
		}
		
		return result;
	
	}
	public VNMMessage unregisterService(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage order ) throws Exception
	{		
		VNMMessage result = CommandUtil.createVNMMessage(order);
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			ProductEntry product = ProductFactory.getCache().getProduct(result.getProductId());
			int resultRespone = executeMutilCommand(instance, provisioningCommand, result, product.getAlias());
			
			if(resultRespone == SUCCESS_RESPONSE )
			{
				result.setCause(Constants.SUCCESS);
			}		
			else
			{
				result.setCause(Constants.ERROR);
			}
		}
		return result;
	}
	
	public VNMMessage barring(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage order) throws Exception
	{

		VNMMessage result = CommandUtil.createVNMMessage(order);
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			ProductEntry product = ProductFactory.getCache().getProduct(result.getProductId());
			int resultResonse = executeMutilCommand(instance, provisioningCommand, result, product.getAlias());
			
			if(resultResonse == SUCCESS_RESPONSE)
			{
				result.setCause(Constants.SUCCESS);
			}
			else
			{
				// roll back
				result.setCause(Constants.ERROR);
			}
		}
		
		return result;
	}
	
	public VNMMessage unbarring(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage order) throws Exception
	{
		
		VNMMessage result = CommandUtil.createVNMMessage(order);
		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, result);
		}
		else
		{
			ProductEntry product = ProductFactory.getCache().getProduct(result.getProductId());
			int resultRespone = executeMutilCommand(instance, provisioningCommand, result, product.getAlias());
			
			if(resultRespone == SUCCESS_RESPONSE)
			{
				result.setCause(Constants.SUCCESS);
			}
			else
			{
				result.setCause(Constants.ERROR);
			}
		}
		
		return result;
	}
	
	public VNMMessage getHistory(CommandInstance instance, ProvisioningCommand provisioningCommand, CommandMessage order) throws Exception
	{
		VNMMessage result = CommandUtil.createVNMMessage(order);
		boolean isEmpty = true;
		String language="";
		String isdn = order.getIsdn();
		PreparedStatement preStatement = null;
		Connection _connection = null;
		MCAConnection _mcaConn = null;
		ResultSet resulSet = null;
		try
		{
			_connection = Database.getConnection();
			
			String sql = "SELECT CALLINGPARTY, SUM(NOOFTIMES) as NOOFTIMES, MAX(IAMTIMESTAMP) as IAMTIMESTAMP " +
						 "FROM MCA_ALL_HISTORY " +
						 "WHERE IAMTIMESTAMP > trunc(sysdate -3) AND " +
						 		"NOOFTIMES > 0 AND REPLACE <> 3 AND " +
						 		"MSISDN = ?  GROUP BY CALLINGPARTY ";
			preStatement = _connection.prepareStatement(sql);
			preStatement.setString(1, isdn);
			resulSet = preStatement.executeQuery();
			
			_mcaConn = (MCAConnection) instance.getProvisioningConnection();
			language = getLanguage(isdn, _mcaConn);
			
			String dateFormat = getProperty("dateFormat", "yyyy-MM-dd HH:mm:ss");

			String lineFormat = "";
			String history = "";
			ProductRoute route = ProductFactory.getCache().getProductRoute(result.getRouteId());
			
			instance.debugMonitor("routeid=" + route.getRouteId());
			
			if (route != null)
			{
				history = route.getParameter("header." + language, "Cac cuoc goi nho trong 3 ngay:");
				lineFormat = route.getParameter("content." + language, "<calling> - goi <number> lan. Lan goi cuoi luc :<time>");
			}
			else
			{
				history = "Cac cuoc goi nho trong 3 ngay:";
				lineFormat = "<calling> - goi <number> lan. Lan goi cuoi luc :<time>.";
			}
			
			instance.debugMonitor("history=" + history);
			instance.debugMonitor("lineFormat=" + lineFormat);
			
			while (resulSet.next())
			{
				String inline = lineFormat;
				
				Date strDate = resulSet.getTimestamp("IAMTIMESTAMP");
				SimpleDateFormat objDateFormat = new SimpleDateFormat(dateFormat);
				String strCallDate = objDateFormat.format(strDate);

				inline = inline.replaceAll("<time>", strCallDate);

				inline = inline.replaceAll("<calling>", "+" + resulSet.getString("CALLINGPARTY"));
				inline = inline.replaceAll("<number>", resulSet.getString("NOOFTIMES"));				
				
				history = history + " " + inline;

				isEmpty = false;
			}
			
			instance.debugMonitor("isEmpty=" + isEmpty);
			
			if (isEmpty)
			{
				history = route.getParameter("notfound." + language, "");
			}
			
			instance.debugMonitor("history=" + history);
			
			CommandUtil.sendSMS(instance, order, history);
		}
		catch (Exception ex)
		{
			throw ex;
		}
		finally
		{
			Database.closeObject(resulSet);
			Database.closeObject(_connection);
			instance.closeProvisioningConnection(_mcaConn);
		}


		return result;
	}
	
	/**
	 * purpose: get language setting of subscriber
	 * 
	 * @param isdn
	 * @param conn
	 * @return true: Vietnamese false: English.
	 * @throws Exception
	 */
	public String getLanguage(String isdn, MCAConnection conn) throws Exception
	{
		String language = "";
		try
		{
			String result = conn.processCommmand("READ " + isdn);
			if (!result.contains("FAILED"))
			{
				if (result.contains(":EN"))
					language = "en";
				else if (result.contains(":VN"))
					language = "vn";
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		
		return language.toLowerCase();
	}
	
	public String transformISDN(String isdn) throws Exception
	{
		isdn = StringUtil.nvl(isdn, "");

		if (!isdn.equals("") && isdn.startsWith(DOMESTIC_CODE))
		{
			isdn = isdn.substring(DOMESTIC_CODE.length());
			isdn = COUNTRY_CODE + isdn;
		}

		return isdn;
	}
	public String getProperty(String key, String defaultMessage) throws Exception
	{
		return StringUtil.nvl(getProperties().getProperty(key), defaultMessage);
	}
	
	// check allowe send sms or not
	public boolean isAllowSendSMS(CommandInstance _instance, CommandMessage order) throws Exception
	{
		return ((order != null) && (order.getChannel().equals("SMS") || getProperty("allowSendSMS", "false").equalsIgnoreCase("true")));
	}
}
