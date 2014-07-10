package com.crm.provisioning.thread;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;

import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.CommandEntry;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.cache.ProvisioningEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.CommandUtil;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.StringUtil;
import com.crm.alarm.cache.AlarmEntry;
import com.crm.alarm.cache.AlarmFactory;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;

import com.fss.util.AppException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright:
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author: HungPQ
 * @version 1.0
 */

public class CommandInstance extends ProvisioningInstance
{
	public CommandInstance() throws Exception
	{
		super();
	}

	public int processMessage(Message message) throws Exception
	{
		long start = System.currentTimeMillis();
		StringBuilder log = new StringBuilder();
		
		CommandMessage request = (CommandMessage) QueueFactory.getContentMessage(message);

		CommandMessage result = request;

		CommandEntry command = null;

		ProductRoute productRoute = null;

		ProvisioningCommand action = null;

		Exception error = null;
		
		boolean isRollback = false;

		try
		{
			request.setRequest("");
			request.setResponse("");
			
			result.setRequestTime(new Date());
			result.setResponseTime(null);

			command = ProvisioningFactory.getCache().getCommand(request.getCommandId());
			
			productRoute = ProductFactory.getCache().getProductRoute(result.getRouteId());

			if (!request.getActionType().equals(Constants.ACTION_ROLLBACK))
			{
				if ((productRoute != null) && CommandUtil.isTimeout(result, productRoute.getTimeout()))
				{
					throw new AppException(Constants.ERROR_TIMEOUT);
				}
			}

			ProvisioningEntry provisioning = ProvisioningFactory.getCache().getProvisioning(request.getProvisioningId());

			if (provisioning.getStatus() == Constants.SERVICE_STATUS_DENIED)
			{
				throw new AppException(Constants.UPGRADING);
			}

			action = provisioning.getAction(command.getCommandId());

			//debug
			log.append("get info: " + (System.currentTimeMillis() - start) + " - ");
			start = System.currentTimeMillis();
			
			if (action.getStatus() == Constants.SERVICE_STATUS_DENIED)
			{
				throw new AppException(Constants.UPGRADING);
			}
			else
			{
				result = (CommandMessage) action.getExecuteMethod().invoke(action.getExecuteImpl(), this, action, request);

				if (result == null)
				{
					result = request;
				}

				if (result.getResponseTime() == null)
					result.setResponseTime(new Date());
				
				//debug
				log.append("execute: " + (System.currentTimeMillis() - start) + " - ");
				start = System.currentTimeMillis();
			}
		}
		catch (AppException e)
		{
			result.setCause(e.getMessage());
			result.setDescription(e.getContext());

			error = e;
		}
		catch (Exception e)
		{
			result.setCause(Constants.ERROR);
			result.setDescription(e.getMessage());

			error = e;
		}
		finally
		{
			if ((action != null && action.isLogEnable())
					&& (!result.getParameters().getProperty("ByPassCommandLog", "false").equals("true")))
			{
				sendCommandLog(result);
			}
			result.getParameters().setProperty("ByPassCommandLog", "false");
		}

		// get next command if available
		try
		{
			String actionCause = result.getCause();

			if (error != null)
			{
				result.setStatus(Constants.ORDER_STATUS_DENIED);
			}

			if (result.getStatus() == Constants.ORDER_STATUS_DENIED)
			{
				if (actionCause.equals(""))
					actionCause = Constants.ERROR;
			}
			else if (actionCause.equals(""))
			{
				actionCause = Constants.SUCCESS;
			}

			if (!result.getActionType().equals(Constants.ACTION_ROLLBACK)
					&& actionCause.equals(Constants.SUCCESS)
					&& command != null)
				result.getCompletedCommands().add(command.getAlias());

			int nextCounter = 0;
			try
			{
				if (!request.getActionType().equals(Constants.ACTION_ROLLBACK))
				{
					if (isTimeout(request))
						actionCause = Constants.ERROR_TIMEOUT;
				}

				result.setCause(actionCause);
				if (!result.getActionType().equals(Constants.ACTION_ROLLBACK)
//						&& !result.getActionType().equals(Constants.ACTION_CANCEL)
						&& !actionCause.contains(Constants.SUCCESS)
						&& command != null)
				{
					result.setStatus(Constants.ORDER_STATUS_DENIED);
					isRollback = rollback(result);
				}
				
//				if ((result.getActionType().equals(Constants.ACTION_CANCEL)
//						|| result.getActionType().equals(Constants.ACTION_UNREGISTER))
//						&& !result.getCause().equals(Constants.SUCCESS)
//						&& !result.getCause().equals(Constants.ERROR_RESOURCE_BUSY)
//						&& !result.getCause().equals(Constants.ERROR_UNREGISTERED)
//						&& !result.getCause().equals(Constants.ERROR_SUBSCRIPTION_NOT_FOUND)
//						&& !result.getCause().equals(Constants.UPGRADING)
//						&& !result.getCause().equals(Constants.ERROR_TIMEOUT))
//				{
//					result.setDescription(result.getCause());
//					result.setCause(Constants.SUCCESS);
//					result.setStatus(Constants.ORDER_STATUS_PENDING);
//					actionCause = Constants.SUCCESS;
//				}

				nextCounter = sendNextCommand(productRoute, result, command, result.getActionType(), actionCause);
				
				//debug
				log.append("next command: " + (System.currentTimeMillis() - start) + " - ");
				start = System.currentTimeMillis();
			}
			catch (JMSException jme)
			{
				result.setCause(Constants.ERROR_RESOURCE_BUSY);
				result.setStatus(Constants.ORDER_STATUS_DENIED);
				if (!isRollback)
				{
					isRollback = rollback(result);
				}
			}

			// reply to sender
			if (!result.getActionType().equals(Constants.ACTION_ROLLBACK) && ((nextCounter == 0) || (nextCounter > 1)))
			{
				if (result.getStatus() == Constants.ORDER_STATUS_PENDING)
				{
					result.setStatus(Constants.ORDER_STATUS_APPROVED);
				}

				try
				{
					sendOrderResponse(productRoute, result);
				}
				catch (Exception e)
				{
					logMonitor(e);
					result.setCause(Constants.ERROR_RESOURCE_BUSY);
					result.setStatus(Constants.ORDER_STATUS_DENIED);
					if (!isRollback)
					{
						isRollback = rollback(result);
					}
				}
				
				if (result.getStatus() == Constants.ORDER_STATUS_DENIED)
				{
					if (result.getCause() != null && result.getCause().equals(Constants.ERROR)
							&& result.getDescription() != null && !result.getDescription().equals(""))
					{
						getDispatcher().sendInstanceAlarm(result.getDescription(), result.getProvisioningId());
					}
					else
					{
						getDispatcher().sendInstanceAlarm(result.getCause(), result.getProvisioningId());
					}
				}
				
				//debug
				log.append("response: " + (System.currentTimeMillis() - start) + " - ");
				start = System.currentTimeMillis();
			}

			if ((error != null) && !(error instanceof AppException))
			{
				throw error;
			}
			
			
			logMonitor(log.toString());
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			// debugMonitor(result.toString());
			debugMonitor(result.toLogString());
		}

		return Constants.BIND_ACTION_SUCCESS;
	}

	public boolean rollback(CommandMessage request) throws Exception
	{
		CommandMessage rollback = null;
		for (int i = 0; i < request.getCompletedCommands().size(); i++)
		{
			String commandAlias = request.getCompletedCommands().get(i);

			try
			{
				CommandEntry completedCommand = null;
				try
				{
					completedCommand = ProvisioningFactory.getCache().getCommand(commandAlias);
				}
				catch (Exception e)
				{
					debugMonitor("Error: rollback command [" + completedCommand + "]not found.");
				}

				String rollbackCommandAlias = completedCommand.getParameter("rollback", "");

				if (!"".equals(rollbackCommandAlias))
				{
					CommandEntry commandEntry = null;
					try
					{
						commandEntry = ProvisioningFactory.getCache().getCommand(rollbackCommandAlias);
					}
					catch (Exception e)
					{

					}
					if (commandEntry != null)
					{
						rollback = request.clone();
						rollback.setProvisioningType(commandEntry.getProvisioningType());
						rollback.setCommandId(commandEntry.getCommandId());

						rollback.setActionType(Constants.ACTION_ROLLBACK);
						// rollback.setParameter("ignoreDBUpdate", "true");
						rollback.getParameters().setProperty("ignoreError", "true");
						rollback.setStatus(Constants.SERVICE_STATUS_DENIED);

						rollback.setRequest("");
						rollback.setResponse("");

						// Send to command routing queue.
						try
						{
							sendCommandRouting(rollback);
						}
						catch (Exception e)
						{
							debugMonitor("Error: rollback error [" + rollbackCommandAlias + "].");
						}
					}
					else
					{
						// Rollback command not found.
						debugMonitor("Error: rollback command [" + rollbackCommandAlias + "]not found.");
					}
				}
			}
			catch (Exception e)
			{
				debugMonitor(e);
			}
		}
		
		return true;
	}
}
