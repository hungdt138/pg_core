/**
 * 
 */
package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.ProvisioningRoute;
import com.crm.thread.DatasourceThread;
import com.crm.thread.util.ThreadUtil;

import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class CommandRoutingThread extends DatasourceThread
{
	public BinaryIndex	routes			= new BinaryIndex();

	public int			maxRetryRouting	= 3;

	public CommandRoutingThread()
	{
		super();
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.add(ThreadUtil.createIntegerParameter("maxRetryRouting", "maxRetryRouting"));
		vtReturn.addAll(super.getDispatcherDefinition());

		return vtReturn;
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillParameter() throws AppException
	{
		try
		{
			super.fillParameter();

			maxRetryRouting = ThreadUtil.getInt(this, "maxRetryRouting", 3);
		}
		catch (AppException e)
		{
			logMonitor(e);

			throw e;
		}
		catch (Exception e)
		{
			logMonitor(e);

			throw new AppException(e.getMessage());
		}
	}

	public void loadCache() throws Exception
	{
		Connection connection = null;
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			getLog().debug("Loading route table ...");

			routes.clear();

			connection = Database.getConnection();

			// //////////////////////////////////////////////////////
			// Connection parameters
			// //////////////////////////////////////////////////////
			String SQL = "Select * From	ProvisioningRoute Order by provisioningType desc, routeType desc "
					+ ", decode(substr(routeKey,length(routeKey)), '%', 0, 1) desc, routeKey desc";

			stmtConfig = connection.prepareStatement(SQL);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProvisioningRoute entry = new ProvisioningRoute();

				entry.setProvisioningType(Database.getString(rsConfig, "provisioningType"));
				entry.setRouteType(Database.getString(rsConfig, "routeType"));
				entry.setIndexKey(Database.getString(rsConfig, "routeKey"));

				entry.setProvisioningId(rsConfig.getLong("provisioningId"));

				routes.add(entry);
			}

			Database.closeObject(rsConfig);
			Database.closeObject(stmtConfig);

			getLog().debug("Routing table are loaded");
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsConfig);
			Database.closeObject(stmtConfig);

			Database.closeObject(connection);
		}
	}

	public ProvisioningRoute getRoute(String provisioningType, String routeType, String routeKey) throws Exception
	{
		ProvisioningRoute lookup = new ProvisioningRoute();

		lookup.setProvisioningType(provisioningType);
		lookup.setRouteType(routeType);
		lookup.setRouteKey(routeKey);

		lookup = (ProvisioningRoute) routes.get(lookup);

		if (lookup == null)
		{
			throw new AppException(Constants.ERROR_ROUTE_NOT_FOUND);
		}

		return lookup;
	}
}
