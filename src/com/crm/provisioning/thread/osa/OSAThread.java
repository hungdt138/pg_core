package com.crm.provisioning.thread.osa;

import java.util.Vector;

import com.crm.provisioning.thread.CommandThread;
import com.crm.thread.util.ThreadUtil;
import com.crm.util.AppProperties;

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

public class OSAThread extends CommandThread
{
	public static int	DEFAULT_CALLBACK_PORT	= 5000;

	public String		applicationName			= "NMS";
	public String		serviceDescription		= "VASMAN";
	public String		currency				= "VND";
	public String		merchantAccount			= "MCA";
	public int			merchantId				= 4;
	public String		callbackHost			= "localhost";
	public int			callbackPort			= DEFAULT_CALLBACK_PORT;

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(ThreadUtil.createTextParameter("applicationName", 100, ""));
		vtReturn.addElement(ThreadUtil.createTextParameter("serviceDescription", 100, ""));
		vtReturn.addElement(ThreadUtil.createTextParameter("currency", 100, ""));

		vtReturn.addElement(ThreadUtil.createTextParameter("merchantAccount", 100, ""));
		vtReturn.addElement(ThreadUtil.createIntegerParameter("merchantId", ""));
		vtReturn.addElement(ThreadUtil.createTextParameter("callbackHost", 100, ""));
		vtReturn.addElement(ThreadUtil.createIntegerParameter("callbackPort", ""));

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

			applicationName = ThreadUtil.getString(this, "applicationName", true, "");
			serviceDescription = ThreadUtil.getString(this, "serviceDescription", true, "");
			currency = ThreadUtil.getString(this, "currency", true, "");

			merchantAccount = ThreadUtil.getString(this, "merchantAccount", true, "");
			merchantId = ThreadUtil.getInt(this, "merchantId", 4);
			
			callbackHost = ThreadUtil.getString(this, "callbackHost", true, "");
			callbackPort = ThreadUtil.getInt(this, "callbackPort", 5000);
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

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void initProvisioningParameters() throws Exception
	{
		try
		{
			super.initProvisioningParameters();

			AppProperties parameters = new AppProperties();

			parameters.setProperty("applicationName", applicationName);
			parameters.setProperty("serviceDescription", serviceDescription);
			parameters.setProperty("currency", currency);

			parameters.setProperty("merchantAccount", merchantAccount);
			parameters.setInteger("merchantId", merchantId);
			
			parameters.setString("callbackHost", callbackHost);
			parameters.setInteger("callbackPort", callbackPort);

			provisioningPool.setParameters(parameters);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

}
