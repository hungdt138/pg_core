/**
 * 
 */
package com.crm.provisioning.impl.ema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.CommandEntry;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.GeneratorSeq;
import com.crm.util.StringUtil;

import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class EMACommandImpl extends CommandImpl
{

	public String executeCommand(
			CommandInstance instance,
			CommandMessage request, String isdn, boolean ignoreError,
			String template, String expectedResults, String expectedCode,
			String errorCode, String bypassCode, String expectedByPassCode)
			throws Exception
	{
		String responseMessage = "";
		String responseCode = "";

		if (isdn.equals(""))
			isdn = StringUtil.nvl(request.getIsdn(), "");

		try
		{
			if (request.isPostpaid())
			{
				template = template.replaceAll("<SUBTYPE>", "POSTPAID");
			}
			else
				template = template.replaceAll("<SUBTYPE>", "PREPAID");

			EMAConnection connection = null;
			try
			{
				connection = (EMAConnection) instance
						.getProvisioningConnection();
				String command = template.replaceAll("<MSISDN>", isdn);

				/**
				 * Add log request/response NamTA 18/09/2012
				 */
				
				//debug
				instance.logMonitor("ConnectionID request: " + connection.getConnectionId());
				
				long sessionId = setRequest(instance, request, command);
				responseMessage = connection.executeCommand(command);
				String emaResponse = "EMA.";
				if (responseMessage != null)
				{
					if (responseMessage.contains("RESP:"))
					{
						if (responseMessage.endsWith(";"))
							emaResponse = emaResponse + responseMessage.substring(responseMessage.indexOf("RESP:") + 5, responseMessage.length()-1).trim();
						else
							emaResponse = emaResponse + responseMessage.substring(responseMessage.indexOf("RESP:") + 5, responseMessage.length()).trim();
					}
					else
					{
						emaResponse = emaResponse + responseMessage.trim();
					}
				}
				
				connection.setLastRun(new Date());
				
				setResponse(instance, request, emaResponse, sessionId);
			}
			catch (Exception e)
			{
				throw e;
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}

			boolean found = false;
			String[] arrExpecteds = StringUtil.toStringArray(expectedResults,
					";");

			for (int j = 0; !found && (j < arrExpecteds.length); j++)
			{
				if (!arrExpecteds[j].equals("")
						&& responseMessage.contains(arrExpecteds[j]))
				{
					found = true;
					responseCode = expectedCode;
				}
			}
			// DuyMB add 09/08/2011 fix bug start.
			if (!"".equals(bypassCode) && !found)
			{
				String[] arrByPassCode = StringUtil.toStringArray(bypassCode,
						";");
				for (int j = 0; !found && (j < arrByPassCode.length); j++)
				{
					if (!arrByPassCode[j].equals("")
							&& responseMessage.contains(arrByPassCode[j]))
					{
						found = true;
						responseCode = expectedByPassCode;
						expectedCode = expectedByPassCode;// DuyMB Add
															// 2011/08/11
					}
				}
			}
			// DuyMB add 09/08/2011 fix bug end.
			if (!found && ignoreError)
			{
				responseCode = expectedCode;
			}

			if (!responseCode.equals(expectedCode))
			{
				responseCode = errorCode;
				request.setCause(Constants.ERROR);
			}

//			request.setResponse(responseMessage);

		}
		catch (IOException e)
		{
			throw e;
		}

		return responseCode;
	}

	public String processCommand(CommandInstance _instance,
			CommandMessage request, boolean ignoreError, String template,
			String expectedResult, String expectedCode, String errorCode,
			String bypassCode, String expectedByPassCode) throws Exception
	{
		return executeCommand(_instance, request, request.getIsdn(),
				ignoreError, template, expectedResult, expectedCode, errorCode,
				bypassCode, expectedByPassCode);
	}

	public void executeMultiCommand(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		boolean isRecursive = false;

		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			try
			{
				boolean replacecommand = false;
				
				ProductEntry product = ProductFactory.getCache().getProduct(
						request.getProductId());
				ArrayList<SubscriberProduct> subProduct = null;
				String command = "";
				if (product.getParameter("ResetEMAFlag", "false").equals("true"))
				{
					String productCheckActive = product.getParameter("BlackProduct", "");
					subProduct = SubscriberProductImpl.getActive(request.getIsdn(), productCheckActive);
					Thread.sleep(Long.parseLong(product.getParameter("DelayTime", "2000")));
					command = product.getParameter("EMACommand." + request.getActionType(), "")
							.replaceAll("<%MSISDN%>", request.getIsdn());
				}
				
				if ((request.getActionType().equals(Constants.ACTION_UNREGISTER)
						|| request.getActionType().equals(Constants.ACTION_SUPPLIER_DEACTIVE))
						&& subProduct != null && subProduct.size() > 0)
				{
					if (!command.equals(""))
					{
						replacecommand = Boolean.parseBoolean(product.getParameter(
								"EMACommand." + request.getActionType() + ".ReplaceCommand", "false"));
					}
				}
				
				if (!replacecommand)
				{
					String[] subCommands = StringUtil.toStringArray(
							provisioningCommand.getParameter("subCommands",
									"default"), ";");
	
					if ((subCommands.length == 0) || subCommands[0].equals(""))
					{
						return;
					}
	
					String node = subCommands[0];
					String prefix = "command." + node + ".";
	
					while (true)
					{
						boolean cfnrcFlag = false;
	
						prefix = "command." + node + ".";
	
						String template = provisioningCommand.getParameter(prefix
								+ "template", "");
	
						String expectedResult = provisioningCommand.getParameter(
								prefix + "expectedResult", "");
						String expectedCode = provisioningCommand.getParameter(
								prefix + "expectedCode", "");
						String errorCode = provisioningCommand.getParameter(prefix
								+ "errorCode", "");
						// DuyMB add 09/08/2011 Fix bug start
						String bypassCode = provisioningCommand.getParameter(prefix
								+ "bypassCode", "");
						String expectedByPassCode = provisioningCommand
								.getParameter(prefix + "expectedByPassCode", "");
						// DuyMB add 09/08/2011 Fix bug end.
						// DuyMB add 01/06/2011 Fix bug timeout.
						boolean ignoreError = provisioningCommand.getParameters()
								.getBoolean(prefix + "ignoreError", false);
	
						request.setRequestTime(new Date());
						responseCode = processCommand(instance, request,
								ignoreError, template, expectedResult,
								expectedCode, errorCode, bypassCode,
								expectedByPassCode);
	
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
									CommandEntry commandEntry = ProvisioningFactory
											.getCache().getCommand(
													request.getCommandId());
									if (commandEntry.isLogEnable())
									{
										instance.sendCommandLog(request);
									}
									node = subCommands[j];
								}
							}
						}
	
						if (isRecursive || !found)
						{
							break;
						}
					}
					if (isRecursive)
					{
						request.setCause("recursive-command");
					}
				}
				
				if (!command.equals("")	&& subProduct != null && subProduct.size() > 0)
				{
					EMAConnection connection = null;
					try
					{
						connection = (EMAConnection) instance.getProvisioningConnection();
						
						//debug
						instance.logMonitor("ConnectionID request: " + connection.getConnectionId());
						
						CommandEntry commandEntry = ProvisioningFactory
								.getCache().getCommand(
										request.getCommandId());
						if (commandEntry.isLogEnable())
						{
							instance.sendCommandLog(request);
						}
						long sessionId = setRequest(instance, request, command);
						String responseMessage = connection.executeCommand(command);
						String emaResponse = "EMA.";
						if (responseMessage != null)
						{
							if (responseMessage.contains("RESP:"))
							{
								if (responseMessage.endsWith(";"))
									emaResponse = emaResponse + responseMessage.substring(responseMessage.indexOf("RESP:") + 5, responseMessage.length()-1).trim();
								else
									emaResponse = emaResponse + responseMessage.substring(responseMessage.indexOf("RESP:") + 5, responseMessage.length()).trim();
							}
							else
							{
								emaResponse = emaResponse + responseMessage.trim();
							}
						}
						
						connection.setLastRun(new Date());
						
						setResponse(instance, request, emaResponse, sessionId);
					}
					finally
					{
						instance.closeProvisioningConnection(connection);
					}
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
		}
	}
}
