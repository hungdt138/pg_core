package com.crm.provisioning.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.jms.Queue;

import org.apache.log4j.Logger;

import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.DispatcherThread;
import com.fss.thread.ParameterType;
import com.fss.util.AppException;

public class NotifyOverPrepaidVB extends DispatcherThread
{
	protected PreparedStatement _stmtQueue = null;
	protected String _sqlCommand = "";
	protected int expiredTime = 10;
	protected int waittingTime = 10;
	protected int _maxRequest = 10;
	protected int _minFreeSize = 29985;
	protected String _productVB600 = "";
	protected String _productVB220 = "";
	protected Connection connection = null;
	{

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getParameterDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addElement(createParameterDefinition("SQLCommand", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("ExpiredTime", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("WaittingTime", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("MaxRequest", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("MinFreeSize", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("VB600Alias", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));
		vtReturn.addElement(createParameterDefinition("VB220Alias", "",
				ParameterType.PARAM_TEXTBOX_MAX, "100"));

		vtReturn.addAll(super.getParameterDefinition());

		return vtReturn;
	}

	public void fillParameter() throws AppException
	{
		try
		{
			setSQLCommand(loadMandatory("SQLCommand"));
			setExpiredTime(Integer.parseInt(loadMandatory("ExpiredTime")));
			setWaittingTime(Integer.parseInt(loadMandatory("WaittingTime")));
			setMaxRequest(Integer.parseInt(loadMandatory("MaxRequest")));
			setMinFreeSize(Integer.parseInt(loadMandatory("MinFreeSize")));
			setProductVB600(loadMandatory("VB600Alias"));
			setProductVB220(loadMandatory("VB220Alias"));

			super.fillParameter();
		}
		catch (AppException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void beforeProcessSession() throws Exception
	{
		super.beforeProcessSession();

		try
		{
			connection = Database.getConnection();
			String strSQL = getSQLCommand();
			debugMonitor(strSQL);
			_stmtQueue = connection.prepareStatement(strSQL);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void afterProcessSession() throws Exception
	{
		try
		{
			Database.closeObject(_stmtQueue);
			Database.closeObject(connection);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			super.afterProcessSession();
		}
	}

	public void doProcessSession() throws Exception
	{
		ResultSet result = null;
		MQConnection conn = null;
		try
		{
			conn = getMQConnection();
			Queue checkQueue = QueueFactory.getQueue(queueName);

			result = _stmtQueue.executeQuery();

			int count = 0;
			int sizeOrder = 0;

			ProductEntry productVB600 = ProductFactory.getCache().getProduct(
					getProductVB600());
			ProductEntry productVB220 = ProductFactory.getCache().getProduct(
					getProductVB220());

			String serviceAddress = "";
			String product = "";
			while (result.next() && isAvailable())
			{
				sizeOrder = conn.getQueueSize(checkQueue);

				if (sizeOrder >= _minFreeSize)
				{
					debugMonitor("Too many order in queue: " + sizeOrder);
					Thread.sleep(getWaittingTime() * 1000);
				}

				if (result.getLong("productid") == productVB600.getProductId())
				{
					serviceAddress = "1720";
					product = productVB600.getAlias();
				}
				else
				{
					serviceAddress = "1721";
					product = productVB220.getAlias();
				}

				CommandMessage order = pushOrder(result.getString("isdn"),
						serviceAddress, product);

				conn.sendMessage(order, QueueFactory.ORDER_REQUEST_QUEUE, 0, queuePersistent);


				logMonitor("isdn: " + result.getString("isdn") + ", Product: "
						+ product);

				count++;

				if (count == getMaxRequest())
				{
					count = 0;
					debugMonitor("Waitting...");
					Thread.sleep(getWaittingTime() * 1000);
				}
			}
			result.close();
			Thread.sleep(getExpiredTime() * 1000);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			returnMQConnection(conn);
			connection.commit();
		}
	}

	public CommandMessage pushOrder(String isdn, String serviceAddress, String product)
			throws Exception
	{
		CommandMessage order = new CommandMessage();

		try
		{
			order.setChannel("core");
			order.setUserId(0);
			order.setUserName("system");

			order.setServiceAddress(serviceAddress);
			order.setIsdn(isdn);
			order.setKeyword("CHECKPRE_" + product);
		}
		catch (Exception e)
		{
			throw e;
		}
		return order;
	}

	private static Logger _logger = Logger.getLogger(NotifyOverPrepaidVB.class);

	public void setSQLCommand(String _sqlCommand)
	{
		this._sqlCommand = _sqlCommand;
	}

	public String getSQLCommand()
	{
		return _sqlCommand;
	}

	public void setExpiredTime(int expiredTime)
	{
		this.expiredTime = expiredTime;
	}

	public int getExpiredTime()
	{
		return expiredTime;
	}

	public void setWaittingTime(int waittingTime)
	{
		this.waittingTime = waittingTime;
	}

	public int getWaittingTime()
	{
		return waittingTime;
	}

	public void setMaxRequest(int _maxRequest)
	{
		this._maxRequest = _maxRequest;
	}

	public int getMaxRequest()
	{
		return _maxRequest;
	}

	public void setMinFreeSize(int _minFreeSize)
	{
		this._minFreeSize = _minFreeSize;
	}

	public int getMinFreeSize()
	{
		return _minFreeSize;
	}

	public String getProductVB600()
	{
		return _productVB600;
	}

	public void setProductVB600(String _productVB600)
	{
		this._productVB600 = _productVB600;
	}

	public String getProductVB220()
	{
		return _productVB220;
	}

	public void setProductVB220(String _productVB220)
	{
		this._productVB220 = _productVB220;
	}
}
