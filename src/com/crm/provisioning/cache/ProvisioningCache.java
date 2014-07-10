/**
 * 
 */
package com.crm.provisioning.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class ProvisioningCache
{
	// index cache
	private BinaryIndex	provisionings	= new BinaryIndex();

	private BinaryIndex	commands		= new BinaryIndex();

	public synchronized void loadCache() throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			loadProvisioning(connection);
			loadCommand(connection);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(connection);
		}
	}

	public void clear()
	{
		provisionings.clear();
		commands.clear();
	}

	/**
	 * Load Provisioning entries to cache<br>
	 * 
	 * @param connection
	 * @throws Exception
	 */
	protected void loadProvisioning(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			provisionings.clear();

			// //////////////////////////////////////////////////////
			// Connection parameters
			// //////////////////////////////////////////////////////
			stmtConfig = connection.prepareStatement("Select * From	ProvisioningEntry Order by alias_");
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProvisioningEntry provisioning =
						new ProvisioningEntry(rsConfig.getLong("provisioningId"), rsConfig.getString("alias_"));

				provisioning.setProvisioningType(Database.getString(rsConfig, "provisioningType"));
				provisioning.setTitle(Database.getString(rsConfig, "title"));

				provisioning.setParameters(rsConfig.getString("properties"));
				provisioning.setStatus(rsConfig.getInt("status"));

				provisionings.add(provisioning.getProvisioningId(), provisioning.getIndexKey(), provisioning);
			}

			Database.closeObject(rsConfig);
			Database.closeObject(stmtConfig);

			// //////////////////////////////////////////////////////
			// Provisioning Action Mapping by command
			// //////////////////////////////////////////////////////
			stmtConfig = connection.prepareStatement("Select * From ProvisioningAction");
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProvisioningCommand action = new ProvisioningCommand();

				action.setProvisioningId(rsConfig.getLong("provisioningId"));
				action.setCommandId(rsConfig.getLong("commandId"));
				action.setParameters(rsConfig.getString("properties"));

				action.setRetryEnable(rsConfig.getBoolean("retryEnable"));
				action.setMaxRetry(rsConfig.getInt("maxRetry"));
				action.setPriority(rsConfig.getInt("priority"));
				action.setTimeout(rsConfig.getInt("timeout"));
				action.setLogEnable(rsConfig.getBoolean("logEnable"));
				action.setStatus(rsConfig.getInt("status"));

				action.setExecuteMethod(rsConfig.getString("processClass"), rsConfig.getString("processMethod"));

				ProvisioningEntry entry = (ProvisioningEntry) provisionings.getById(action.getProvisioningId());

				if (entry != null)
				{
					entry.getCommands().put(action.getCommandId(), action);
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsConfig);
			Database.closeObject(stmtConfig);
		}
	}

	/**
	 * Load command entries to cache<br>
	 * Edited by NamTA<br>
	 * Edited Date 21/08/2012<br>
	 * Edited Description: get alias field.
	 * 
	 * @param connection
	 * @throws Exception
	 */
	protected synchronized void loadCommand(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			commands.clear();

			// //////////////////////////////////////////////////////
			// List of Command
			// //////////////////////////////////////////////////////
			stmtConfig = connection.prepareStatement("Select * From CommandEntry Order by alias_");
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				CommandEntry command = new CommandEntry(rsConfig.getLong("commandId"), rsConfig.getString("alias_"));
				command.setAlias(rsConfig.getString("alias_"));
				command.setProvisioningType(rsConfig.getString("provisioningType"));
				command.setRetryEnable(rsConfig.getBoolean("retryEnable"));
				command.setMaxRetry(rsConfig.getInt("maxRetry"));
				command.setPriority(rsConfig.getInt("priority"));
				command.setTimeout(rsConfig.getInt("timeout"));
				command.setLogEnable(rsConfig.getBoolean("logenable"));
				command.setParameters(rsConfig.getString("properties"));

				commands.add(command.getCommandId(), command.getIndexKey(), command);
			}

			// //////////////////////////////////////////////////////
			// Provisioning Action Mapping by command
			// //////////////////////////////////////////////////////
			stmtConfig = connection.prepareStatement(
					"Select * From CommandAction Order by productId, actionType, subscriberType, actionName");
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				CommandAction action = new CommandAction();

				action.setCommandId(rsConfig.getLong("commandId"));
				action.setProductId(rsConfig.getLong("productId"));
				action.setSubscriberType(rsConfig.getInt("subscriberType"));
				action.setActionType(rsConfig.getString("actionType"));
				action.setActionName(rsConfig.getString("actionName"));

				action.setNextAction(rsConfig.getString("nextActionType"));
				action.setNextCommandId(rsConfig.getLong("nextCommandId"));

				action.setExecuteMethod(rsConfig.getString("processClass"), rsConfig.getString("processMethod"));

				CommandEntry entry = getCommand(action.getCommandId());

				if (entry != null)
				{
					entry.getActions().add(action);
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsConfig);
			Database.closeObject(stmtConfig);
		}
	}

	public ProvisioningEntry getProvisioning(long provisioningId) throws Exception
	{
		ProvisioningEntry provisioning = (ProvisioningEntry) provisionings.getById(provisioningId);

		if (provisioning == null)
		{
			throw new AppException(Constants.ERROR_PROVISIONING_NOT_FOUND);
		}

		return provisioning;
	}

	public ProvisioningEntry getProvisioning(String alias) throws Exception
	{
		ProvisioningEntry provisioning = (ProvisioningEntry) provisionings.getByKey(alias);

		if (provisioning == null)
		{
			throw new AppException(Constants.ERROR_PROVISIONING_NOT_FOUND);
		}

		return provisioning;
	}

	public CommandEntry getCommand(long commandId) throws Exception
	{
		CommandEntry command = (CommandEntry) commands.getById(commandId);

		if (command == null)
		{
			throw new AppException(Constants.ERROR_COMMAND_NOT_FOUND);
		}

		return command;
	}

	public CommandEntry getCommand(String alias) throws Exception
	{
		CommandEntry command = (CommandEntry) commands.getByKey(alias);

		if (command == null)
		{
			throw new AppException(Constants.ERROR_COMMAND_NOT_FOUND);
		}

		return command;
	}
}
