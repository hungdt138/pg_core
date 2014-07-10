package com.crm.subscriber.thread;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.jms.Message;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.product.cache.ProductFactory;
import com.crm.subscriber.message.SubscriberMessage;
import com.crm.thread.FileThread;
import com.crm.thread.util.ThreadUtil;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: FPT
 * </p>
 * 
 * @author Vu Anh Dung
 * @version 1.0 Purpose : Base class for other threads
 */

public class MigrateThread extends FileThread
{
	public int					ISDN_COLUMN				= -1;
	public int					SUBSCRIBER_TYPE_COLUMN	= -1;
	public int					PRODUCT_COLUMN			= -1;
	public int					AMOUNT_COLUMN			= -1;
	public int					ORDER_DATE_COLUMN		= -1;
	public int					ACTIVE_DATE_COLUMN		= -1;
	public int					NETWORK_STATUS_COLUMN	= -1;

	private PreparedStatement	stmtSubscribers			= null;
	private ResultSet			rsSubscribers			= null;

	private Connection			cnDatasource			= null;
	private SimpleDateFormat	orderDateFormat			= new SimpleDateFormat("dd-MMM-yyyy");
	private SimpleDateFormat	activeDateFormat		= new SimpleDateFormat("dd-MMM-yy");

	private long				subscriberId			= 0;

	public MigrateThread()
	{
		super();
	}

	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		// caching product
		ProductFactory.loadCache(new Date());
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected void prepareDatasource() throws Exception
	{
		try
		{
			// load the driver into memory
			Class.forName("org.relique.jdbc.csv.CsvDriver");

			// create a connection. The first command line parameter is assumed
			// to be the directory in which the .csv files are held
			Properties props = new Properties();

			// Define column names and column data types here.
			props.put("suppressHeaders", "true");
			props.put("headerline", "orderDate;product;isdn;subscriberType;usageAmount;networkStatus;registerDate");
			// props.put("columnTypes",
			// "Date,String,String,Int,Double,Int,Date");
			props.put("separator", ";");

			cnDatasource = DriverManager.getConnection("jdbc:relique:csv:" + importDir);
			//cnDatasource = DriverManager.getConnection("jdbc:relique:csv:d:/Workspaces/CRM/crm-service/", props);

			// create a Statement object to execute the query with
			stmtSubscribers = cnDatasource.prepareStatement("Select * From migrate");

			// Select the ID and NAME columns from sample.csv
			rsSubscribers = stmtSubscribers.executeQuery();

			PRODUCT_COLUMN = findColumn(ThreadUtil.getString(this, "column.sku", true, "product"));

			ACTIVE_DATE_COLUMN = findColumn(ThreadUtil.getString(this, "column.orderDate", true, "registerDate"));

			ORDER_DATE_COLUMN = findColumn(ThreadUtil.getString(this, "column.orderDate", true, "orderDate"));

			ISDN_COLUMN = findColumn(ThreadUtil.getString(this, "column.isdn", true, "isdn"));

			SUBSCRIBER_TYPE_COLUMN = findColumn(ThreadUtil.getString(this, "column.subscriberType", true, "subscriberType"));

			AMOUNT_COLUMN = findColumn(ThreadUtil.getString(this, "column.amount", true, "usageAmount"));

			NETWORK_STATUS_COLUMN = findColumn(ThreadUtil.getString(this, "column.status", true, "networkStatus"));

			super.prepareDatasource();
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected void closeDatasource() throws Exception
	{
		Database.closeObject(rsSubscribers);
		Database.closeObject(stmtSubscribers);
		Database.closeObject(cnDatasource);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	public int findRawColumn(String columnName) throws Exception
	{
		return rsSubscribers.findColumn(columnName);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data source
	// Author: HungHM
	// Modify DateTime: 03/10/2004
	// /////////////////////////////////////////////////////////////////////////
	protected String getRawValue(int index) throws Exception
	{
		return rsSubscribers.getString(index);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: HiepTH
	// Modify DateTime: 08/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public int bindData() throws Exception
	{
		return Constants.BIND_ACTION_SUCCESS;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Bind data into batch statement
	// Author: HiepTH
	// Modify DateTime: 08/09/2004
	// /////////////////////////////////////////////////////////////////////////
	public Serializable createLocalMessage() throws Exception
	{
		SubscriberMessage message = new SubscriberMessage();

		try
		{
			subscriberId++;

			message.setSubscriberId(subscriberId);
			message.setIsdn(rsSubscribers.getString(ISDN_COLUMN));
			message.setSubscriberType(rsSubscribers.getInt(SUBSCRIBER_TYPE_COLUMN));

			// order date
			String orderDate = rsSubscribers.getString(ORDER_DATE_COLUMN);

			if (orderDate != null)
			{
				message.setOrderDate(orderDateFormat.parse(orderDate));
			}

			// register date
			String registerDate = rsSubscribers.getString(ACTIVE_DATE_COLUMN);

			if ((registerDate != null) && !registerDate.equals(""))
			{
				message.setRegisterDate(activeDateFormat.parse(registerDate));
			}

			message.setNetworkStatus(rsSubscribers.getInt(NETWORK_STATUS_COLUMN));
			
			// amount
			message.setAmount(rsSubscribers.getDouble(AMOUNT_COLUMN));
		}
		catch (Exception e)
		{
			throw e;
		}

		return message;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data target
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean next() throws Exception
	{
		return rsSubscribers.next() && isAvailable();
	}
}
