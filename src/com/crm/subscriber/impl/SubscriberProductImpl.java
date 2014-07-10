package com.crm.subscriber.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.crm.marketing.cache.CampaignEntry;
import com.crm.marketing.cache.CampaignFactory;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.kernel.message.Constants;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.kernel.sql.Database;
import com.crm.util.DateUtil;

import com.fss.util.AppException;

/**
 * 
 * @author ?? <br>
 *         Modified by NamTA Modified Date: 07/06/2012
 * 
 */
public class SubscriberProductImpl
{
	public final static String CONDITION_ACTIVE = " (supplierStatus = "
			+ Constants.SUPPLIER_ACTIVE_STATUS + ") ";

	public final static String CONDITION_BARRING = " (supplierStatus = "
			+ Constants.SUPPLIER_BARRING_STATUS + ") ";

	public final static String CONDITION_TERMINATED = " (supplierStatus = "
			+ Constants.SUPPLIER_CANCEL_STATUS + ") ";

	public final static String CONDITION_UNTERMINATED = " (supplierStatus in ("
			+ Constants.SUPPLIER_ACTIVE_STATUS + "," + Constants.SUPPLIER_BARRING_STATUS + ")) ";

	private static long DEFAULT_ID = 0;
	private static String ERROR_REGISTER_FLEXI = "error-register-flexi";
	private static String ERROR_UPDATE_FLEXI = "error-update-flexi";

	/**
	 * TODO: Performance test
	 */
	// private static long sleepTime = 1000L;

	public static Date calculateExpirationDate(Date startDate,
			String subscriptionType, int period, int quantity,
			boolean truncExpire)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		if (period <= 0)
		{
			return null;
		}

		if (subscriptionType.equalsIgnoreCase("monthly")
				|| subscriptionType.equalsIgnoreCase("month"))
		{
			calendar.add(Calendar.DATE, 30 * period * quantity);
		}
		else if (subscriptionType.equalsIgnoreCase("weekly")
				|| subscriptionType.equalsIgnoreCase("week"))
		{
			calendar.add(Calendar.DATE, 7 * period * quantity);
		}
		else if (subscriptionType.equalsIgnoreCase("daily")
				|| subscriptionType.equalsIgnoreCase("day"))
		{
			calendar.add(Calendar.DATE, 1 * period * quantity);
		}

		if (truncExpire)
		{
			// calendar.add(Calendar.DATE, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
		}

		return calendar.getTime();
	}

	public static Date calculateGraceDate(Date startDate, String graceDateUnit,
			int graceDatePeriod) throws Exception
	{
		Date graceDate = null;
		if (graceDatePeriod > 0)
		{
			graceDate = DateUtil.addDate(startDate, graceDateUnit,
					graceDatePeriod);
		}

		return graceDate;
	}

	public static SubscriberProduct getProduct(ResultSet rsProduct)
			throws Exception
	{
		SubscriberProduct result = new SubscriberProduct();

		try
		{
			result.setUserId(rsProduct.getLong("userId"));
			result.setUserName(Database.getString(rsProduct, "userName"));

			result.setSubscriberId(rsProduct.getLong("subscriberId"));
			result.setSubProductId(rsProduct.getLong("subProductId"));
			result.setProductId(rsProduct.getLong("productId"));

			result.setSubscriberType(rsProduct.getInt("subscriberType"));
			result.setIsdn(Database.getString(rsProduct, "isdn"));
			result.setLanguageId(Database.getString(rsProduct, "languageId"));

			result.setRegisterDate(rsProduct.getTimestamp("registerDate"));
			result.setUnregisterDate(rsProduct.getTimestamp("unregisterDate"));
			result.setTermDate(rsProduct.getTimestamp("termDate"));
			result.setExpirationDate(rsProduct.getTimestamp("expirationDate"));
			result.setGraceDate(rsProduct.getTimestamp("graceDate"));

			result.setBarringStatus(rsProduct.getInt("barringStatus"));
			result.setSupplierStatus(rsProduct.getInt("supplierStatus"));
			result.setStatus(rsProduct.getInt("status"));
		}
		catch (Exception e)
		{
			throw e;
		}

		return result;
	}

	/**
	 * Only use for searching on table subscriberorder to get information about
	 * service.
	 * 
	 * @param rsProduct
	 * @param temp
	 * @return
	 * @throws Exception
	 */
	public static SubscriberProduct getProduct(ResultSet rsProduct, long temp)
			throws Exception
	{
		SubscriberProduct result = new SubscriberProduct();

		try
		{
			result.setUserId(rsProduct.getLong("userId"));
			result.setUserName(Database.getString(rsProduct, "userName"));

			result.setSubscriberId(rsProduct.getLong("subscriberId"));
			result.setSubProductId(rsProduct.getLong("subProductId"));
			result.setProductId(rsProduct.getLong("productId"));

			result.setSubscriberType(rsProduct.getInt("subscriberType"));
			result.setIsdn(Database.getString(rsProduct, "isdn"));

			result.setRegisterDate(rsProduct.getTimestamp("createdate"));

		}
		catch (Exception e)
		{
			throw e;
		}

		return result;
	}

	public static SubscriberProduct getProduct(Connection connection,
			long subProductId) throws Exception
	{
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		SubscriberProduct result = null;

		try
		{
			String SQL = "Select * From SubscriberProduct Where subProductId = ?";

			stmtProduct = connection.prepareStatement(SQL);
			stmtProduct.setLong(1, subProductId);

			rsProduct = stmtProduct.executeQuery();

			if (rsProduct.next())
			{
				result = getProduct(rsProduct);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsProduct);
			Database.closeObject(stmtProduct);
		}

		return result;
	}

	public static SubscriberProduct getProduct(long subProductId)
			throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return getProduct(connection, subProductId);
		}
		finally
		{
			Database.closeObject(connection);
		}
	}

	/**
	 * Created by NamTA<br>
	 * Created Date: 07/06/2012
	 * 
	 * @param isdn
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public static SubscriberProduct getUnterminated(String isdn, long productId)
			throws Exception
	{
		Connection connection = null;

		try
		{
			/**
			 * TODO: performanceTest
			 */

			// Thread.sleep(sleepTime);
			// return null;

			connection = Database.getConnection();

			return getUnterminated(connection, isdn, productId);
		}
		finally
		{
			Database.closeObject(connection);
		}
	}

	/**
	 * Created by NamTA<br>
	 * Created Date: 07/06/2012
	 * 
	 * @param connection
	 * @param isdn
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public static SubscriberProduct getUnterminated(Connection connection,
			String isdn, long productId) throws Exception
	{
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;

		SubscriberProduct result = null;

		try
		{
			String SQL = "Select * " + "From SubscriberProduct "
					+ "Where isdn = ? and productId = ? and "
					+ CONDITION_UNTERMINATED + "Order by registerDate desc";

			stmtActive = connection.prepareStatement(SQL);
			stmtActive.setString(1, isdn);
			stmtActive.setLong(2, productId);

			rsActive = stmtActive.executeQuery();

			if (rsActive.next())
			{
				result = getProduct(rsActive);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActive);
			Database.closeObject(stmtActive);
		}

		return result;
	}

	/**
	 * Created by NamTA<br>
	 * Created Date: 07/06/2012
	 * 
	 * @param connection
	 * @param isdn
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public static SubscriberProduct getBarring(Connection connection,
			String isdn, long productId) throws Exception
	{
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;

		SubscriberProduct result = null;

		try
		{
			String SQL = "Select * " + "From SubscriberProduct "
					+ "Where isdn = ? and productId = ? and "
					+ CONDITION_BARRING + "Order by registerDate desc";

			stmtActive = connection.prepareStatement(SQL);
			stmtActive.setString(1, isdn);
			stmtActive.setLong(2, productId);

			rsActive = stmtActive.executeQuery();

			if (rsActive.next())
			{
				result = getProduct(rsActive);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActive);
			Database.closeObject(stmtActive);
		}

		return result;
	}

	/**
	 * Created by NamTA<br>
	 * Created Date: 07/06/2012
	 * 
	 * @param isdn
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public static SubscriberProduct getBarring(String isdn, long productId)
			throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return getBarring(connection, isdn, productId);
		}
		finally
		{
			Database.closeObject(connection);
		}
	}

	public static SubscriberProduct getActive(Connection connection,
			String isdn, long productId) throws Exception
	{
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;

		SubscriberProduct result = null;

		try
		{
			String SQL = "Select * " + "From SubscriberProduct "
					+ "Where isdn = ? and productId = ? and "
					+ CONDITION_ACTIVE + "Order by registerDate desc";

			stmtActive = connection.prepareStatement(SQL);
			stmtActive.setString(1, isdn);
			stmtActive.setLong(2, productId);

			rsActive = stmtActive.executeQuery();

			if (rsActive.next())
			{
				result = getProduct(rsActive);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActive);
			Database.closeObject(stmtActive);
		}

		return result;
	}

	public static SubscriberProduct getActive(String isdn, long productId)
			throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return getActive(connection, isdn, productId);
		}
		finally
		{
			Database.closeObject(connection);
		}
	}
	
	public static ArrayList<SubscriberProduct> getActive(String isdn, String listProductId)
			throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return getActive(connection, isdn, listProductId);
		}
		finally
		{
			Database.closeObject(connection);
		}
	}
	
	public static ArrayList<SubscriberProduct> getActive(Connection connection,
			String isdn, String listProductId) throws Exception
	{
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;

		ArrayList<SubscriberProduct> result = new ArrayList<SubscriberProduct>();

		try
		{
			String SQL = "Select * " + "From SubscriberProduct "
					+ "Where isdn = ? and productId in (" + listProductId + ") and "
					+ CONDITION_ACTIVE + "Order by registerDate desc";

			stmtActive = connection.prepareStatement(SQL);
			stmtActive.setString(1, isdn);

			rsActive = stmtActive.executeQuery();

			while (rsActive.next())
			{
				result.add(getProduct(rsActive));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActive);
			Database.closeObject(stmtActive);
		}

		return result;
	}

	public static SubscriberProduct getActive(Connection connection,
			long subscriberId, long productId) throws Exception
	{
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;

		SubscriberProduct result = null;

		try
		{
			String SQL = "Select * " + "From SubscriberProduct "
					+ "Where subscriberId = ? and productId = ? and "
					+ CONDITION_ACTIVE + "Order by registerDate desc";

			stmtActive = connection.prepareStatement(SQL);
			stmtActive.setLong(1, subscriberId);
			stmtActive.setLong(2, productId);

			rsActive = stmtActive.executeQuery();

			if (rsActive.next())
			{
				result = getProduct(rsActive);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActive);
			Database.closeObject(stmtActive);
		}

		return result;
	}

	public static SubscriberProduct getActive(long subscriberId, long productId)
			throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return getActive(connection, subscriberId, productId);
		}
		finally
		{
			Database.closeObject(connection);
		}
	}

	public static SubscriberProduct getActive(Connection connection,
			long subscriberId, String isdn, long productId) throws Exception
	{
		if (subscriberId != DEFAULT_ID)
		{
			return getActive(connection, subscriberId, productId);
		}
		else
		{
			return getActive(connection, isdn, productId);
		}
	}

	public static SubscriberProduct getActiveX(String isdn, long productId,
			Date orderDate) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return getActiveX(connection, isdn, productId, orderDate);
		}
		finally
		{
			Database.closeObject(connection);
		}
	}

	public static SubscriberProduct getActiveX(Connection connection,
			String isdn, long productId, Date orderDate) throws Exception
	{
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;

		SubscriberProduct result = null;

		try
		{
			String SQL = "Select * "
					+ "From SubscriberOrder "
					+ "Where isdn = ? and productId = ? and status = ? "
					+ "and orderDate >= trunc(sysdate) and orderDate < (trunc(sysdate) + 1)"
					+ "Order by createdate desc";

			stmtActive = connection.prepareStatement(SQL);
			stmtActive.setString(1, isdn);
			stmtActive.setLong(2, productId);
			stmtActive.setLong(3, Constants.ORDER_STATUS_APPROVED);

			rsActive = stmtActive.executeQuery();

			if (rsActive.next())
			{
				result = getProduct(rsActive, 1);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActive);
			Database.closeObject(stmtActive);
		}

		return result;

	}

	public static List<SubscriberProduct> getActive(Connection connection,
			long subscriberId, String isdn) throws Exception
	{
		PreparedStatement stmtActive = null;
		ResultSet rsActive = null;

		ArrayList<SubscriberProduct> result = new ArrayList<SubscriberProduct>();

		try
		{
			if (subscriberId != Constants.DEFAULT_ID)
			{
				String SQL = "Select * From SubscriberProduct Where isdn = ? and "
						+ CONDITION_ACTIVE;

				stmtActive = connection.prepareStatement(SQL);
				stmtActive.setString(1, isdn);
			}
			else
			{
				String SQL = "Select * From SubscriberProduct Where subscriberId = ? and "
						+ CONDITION_ACTIVE;

				stmtActive = connection.prepareStatement(SQL);
				stmtActive.setLong(1, subscriberId);
			}

			rsActive = stmtActive.executeQuery();

			if (rsActive.next())
			{
				result.add(getProduct(rsActive));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsActive);
			Database.closeObject(stmtActive);
		}

		return result;
	}

	/**
	 * Edited by NamTA<br>
	 * Modified Date: 17/05/2012
	 * 
	 * @param connection
	 * @param userId
	 * @param userName
	 * @param subscriberId
	 * @param isdn
	 * @param subscriberType
	 * @param productId
	 * @param campaignId
	 * @param languageId
	 * @param includeCurrentDay
	 * @return
	 * @throws Exception
	 */
	public static SubscriberProduct register(Connection connection,
			long userId, String userName, long subscriberId, String isdn,
			int subscriberType, long productId, long campaignId,
			String languageId, boolean includeCurrentDay, boolean isPrePaid, int status, String activationDate) throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtRegister = null;

		try
		{
			Date now = new Date();

			ProductEntry product = ProductFactory.getCache().getProduct(
					productId);

			// calculate term of use date
			Date termDate = null;

			if (product.getTermPeriod() > 0)
			{
				termDate = DateUtil.addDate(now, product.getTermUnit(),
						product.getTermPeriod());
			}

			// calculate expire date
			Date expirationDate = null;
			Date graceDate = null;

			if (product.isSubscription() && isPrePaid)
			{
				int quantity = 1;

				int expirationPeriod = product.getSubscriptionPeriod();

				String expirationUnit = product.getSubscriptionUnit();
				
				if (product.getParameter("FreeRegisterSMS", "false").equals("true"))
				{
					expirationPeriod = Integer.parseInt(product.getParameter("FreeRegisterPeriod","13"));
					expirationUnit = product.getParameter("FreeRegisterUnit","day");
				}

				if (campaignId != DEFAULT_ID)
				{
					CampaignEntry campaign = CampaignFactory.getCache()
							.getCampaign(campaignId);

					if ((campaign != null))
					{
						expirationPeriod = campaign.getSchedulePeriod();
						expirationUnit = campaign.getScheduleUnit();
					}
				}

				boolean truncExpire = Boolean.parseBoolean(product
						.getParameter("TruncExpireDate", "true"));
				expirationDate = calculateExpirationDate(now, expirationUnit,
						expirationPeriod, quantity, truncExpire);

				/**
				 * remove 1 day if expiration time includes current day
				 */
				if (includeCurrentDay)
				{
					Calendar expiration = Calendar.getInstance();
					expiration.setTime(expirationDate);
					expiration.add(Calendar.DATE, -1);

					expirationDate = expiration.getTime();
				}
				graceDate = calculateGraceDate(expirationDate,
						product.getGraceUnit(), product.getGracePeriod());
			}

			// check product are registered or not
			subscriberProduct = getActive(connection, subscriberId, isdn,
					productId);

			if (subscriberProduct != null)
			{
				throw new AppException(Constants.ERROR_REGISTERED);
			}

			// register product for subscriber
			String sql = "Insert into SubscriberProduct "
					+ "     (subProductId, userId, userName, createDate, modifiedDate "
					+ "     , subscriberId, isdn, subscriberType, productId, languageId "
					+ "     , registerDate, termDate, expirationDate, graceDate, barringStatus, supplierStatus, CampaignId, Status, ActivationDate) "
					+ "Values " + "     (?, ?, ?, sysDate, sysDate "
					+ "     , ?, ?, ?, ?, ? " + "     , ?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'YYYY-MM-DD HH24:MI:SS'))";

			stmtRegister = connection.prepareStatement(sql);

			long subProductId = Database.getSequence(connection,
					"sub_product_seq");

			int barringStatus = Constants.USER_ACTIVE_STATUS;
			int supplierStatus = Constants.SUPPLIER_ACTIVE_STATUS;

			stmtRegister.setLong(1, subProductId);
			stmtRegister.setLong(2, userId);
			stmtRegister.setString(3, userName);

			stmtRegister.setLong(4, subscriberId);
			stmtRegister.setString(5, isdn);
			stmtRegister.setInt(6, subscriberType);
			stmtRegister.setLong(7, productId);
			stmtRegister.setString(8, languageId);

			stmtRegister.setTimestamp(9, DateUtil.getTimestampSQL(now));
			stmtRegister.setTimestamp(10, DateUtil.getTimestampSQL(termDate));
			stmtRegister.setTimestamp(11,
					DateUtil.getTimestampSQL(expirationDate));
			stmtRegister.setTimestamp(12, DateUtil.getTimestampSQL(graceDate));

			stmtRegister.setInt(13, barringStatus);
			stmtRegister.setInt(14, supplierStatus);
			stmtRegister.setLong(15, campaignId);
			stmtRegister.setInt(16, status);
			stmtRegister.setString(17, activationDate);

			stmtRegister.execute();

			// bind return
			// bind order
			subscriberProduct = new SubscriberProduct();

			subscriberProduct.setUserId(userId);
			subscriberProduct.setUserName(userName);

			subscriberProduct.setSubscriberId(subscriberId);
			subscriberProduct.setSubProductId(subProductId);
			subscriberProduct.setProductId(productId);

			subscriberProduct.setSubscriberType(subscriberType);
			subscriberProduct.setIsdn(isdn);

			subscriberProduct.setRegisterDate(now);
			subscriberProduct.setTermDate(termDate);
			subscriberProduct.setExpirationDate(expirationDate);
			subscriberProduct.setGraceDate(graceDate);

			subscriberProduct.setBarringStatus(barringStatus);
			subscriberProduct.setSupplierStatus(supplierStatus);

			if (product.isAuditEnable())
			{
				SubscriberActivateImpl.addActivate(connection, userId,
						userName, subscriberId, isdn, subProductId,
						subscriberProduct.getRegisterDate(),
						subscriberProduct.getBarringStatus(),
						subscriberProduct.getSupplierStatus(), "");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtRegister);
		}

		return subscriberProduct;
	}
	
	

	/**
	 * Edited by NamTA<br>
	 * Modified Date: 17/05/2012
	 * 
	 * @param userId
	 * @param userName
	 * @param subscriberId
	 * @param isdn
	 * @param subscriberType
	 * @param productId
	 * @param campaignId
	 * @param languageId
	 * @param includeCurrentDay
	 * @return
	 * @throws Exception
	 */
	public static SubscriberProduct register(long userId, String userName,
			long subscriberId, String isdn, int subscriberType, long productId,
			long campaignId, String languageId, boolean includeCurrentDay,
			boolean isPrePaid, int status, String activationDate)
			throws Exception
	{
		Connection connection = null;

		try
		{
			/**
			 * TODO; PerformanceTest
			 */

			// Thread.sleep(sleepTime);
			// SubscriberProduct subPro = new SubscriberProduct();
			// subPro.setExpirationDate(new Date());
			//
			// return subPro;

			connection = Database.getConnection();

			return register(connection, userId, userName, subscriberId, isdn,
					subscriberType, productId, campaignId, languageId,
					includeCurrentDay, isPrePaid, status, activationDate);
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

	public static void unregister(Connection connection, long userId,
			String userName, long subProductId, long productId)
			throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtSubscription = null;

		try
		{
			String sql = "Update SubscriberProduct "
					+ "   Set 	modifiedDate = sysDate "
					+ "   		, unregisterDate = sysDate, barringStatus = ?, supplierStatus = ?, lastrundate = sysdate, status = 0 "
					+ "	  Where subProductId = ? and " + CONDITION_UNTERMINATED;

			stmtSubscription = connection.prepareStatement(sql);

			stmtSubscription.setInt(1, Constants.USER_CANCEL_STATUS);
			stmtSubscription.setInt(2, Constants.SUPPLIER_CANCEL_STATUS);
			stmtSubscription.setLong(3, subProductId);

			stmtSubscription.execute();

			if (stmtSubscription.getUpdateCount() == 0)
			{
				throw new AppException(Constants.ERROR_UNREGISTERED);
			}

			ProductEntry product = ProductFactory.getCache().getProduct(
					productId);

			if (product.isAuditEnable())
			{
				subscriberProduct = getProduct(connection, subProductId);

				SubscriberActivateImpl.unregister(connection, userId, userName,
						subscriberProduct.getSubscriberId(),
						subscriberProduct.getIsdn(),
						subscriberProduct.getProductId(),
						subscriberProduct.getUnregisterDate(), "");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscription);
		}
	}

	public static void unregister(long userId, String userName,
			long subProductId, long productId) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			unregister(connection, userId, userName, subProductId, productId);
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

	public static void barringBySupplier(Connection connection, long userId,
			String userName, long subProductId) throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtSubscription = null;

		try
		{
			subscriberProduct = getProduct(connection, subProductId);

			if (subscriberProduct == null)
			{
				throw new AppException(Constants.ERROR_UNREGISTERED);
			}
			else if (subscriberProduct.getSupplierStatus() == Constants.SUPPLIER_BARRING_STATUS)
			{
				return;
			}

			ProductEntry product = ProductFactory.getCache().getProduct(
					subscriberProduct.getProductId());

			String sql = "Update SubscriberProduct "
					+ "     Set    userId = ?, userName = ?, modifiedDate = sysDate, supplierStatus = ?, lastrundate = sysdate "
					+ "     Where  subProductId = ? and unregisterDate is null ";

			stmtSubscription = connection.prepareStatement(sql);

			stmtSubscription.setLong(1, userId);
			stmtSubscription.setString(2, userName);
			stmtSubscription.setInt(3, Constants.SUPPLIER_BARRING_STATUS);
			stmtSubscription.setLong(4, subProductId);

			stmtSubscription.execute();

			subscriberProduct
					.setSupplierStatus(Constants.SUPPLIER_BARRING_STATUS);

			if (product.isAuditEnable())
			{
				SubscriberActivateImpl.updateActivate(connection, userId,
						userName, subscriberProduct.getSubscriberId(),
						subscriberProduct.getIsdn(),
						subscriberProduct.getProductId(), new Date(),
						subscriberProduct.getBarringStatus(),
						subscriberProduct.getSupplierStatus(), "");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscription);
		}
	}

	public static void barringBySupplier(long userId, String userName,
			long subProductId) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			barringBySupplier(connection, userId, userName, subProductId);
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

	public static SubscriberProduct subscription(Connection connection,
			long userId, String userName, long subProductId,
			boolean fullOfCharge, int quantity, boolean includeCurrentDay) throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtSubscription = null;

		try
		{
			subscriberProduct = getProduct(connection, subProductId);

			if (subscriberProduct == null)
			{
				throw new AppException(Constants.ERROR_UNREGISTERED);
			}

			ProductEntry product = ProductFactory.getCache().getProduct(
					subscriberProduct.getProductId());

			// if (!product.isSubscription())
			// {
			// throw new AppException(Constants.ERROR_SUBSCRIPTION_NOT_FOUND);
			// }

			String subscriptionUnit = fullOfCharge ? product
					.getSubscriptionUnit() : "daily";
			int subscriptionPeriod = fullOfCharge ? product
					.getSubscriptionPeriod() : quantity;

			int gracePeriod = product.getGracePeriod();
			String graceUnit = product.getGraceUnit();

			// extend subscription date
			Date expirationDate = subscriberProduct.getExpirationDate();
			Date now = new Date();
			if (expirationDate.getTime() < now.getTime())
			{
				expirationDate = now;
			}
			
			boolean truncExpire = Boolean.parseBoolean(product
					.getParameter("TruncExpireDate", "true"));
			expirationDate = calculateExpirationDate(expirationDate, subscriptionUnit,
					subscriptionPeriod, 1, truncExpire);
			
			if (includeCurrentDay)
			{
				Calendar expiration = Calendar.getInstance();
				expiration.setTime(expirationDate);
				expiration.add(Calendar.DATE, -1);

				expirationDate = expiration.getTime();
			}
			
			Date graceDate = DateUtil.addDate(expirationDate, graceUnit,
					gracePeriod);

			// prepare SQL
			String SQL = "Update SubscriberProduct "
					+ "   Set userId = ?, userName = ?, modifiedDate = sysDate "
					+ "       , supplierStatus = ?, expirationDate = ?, graceDate = ?, lastrundate = sysdate"
					+ "   Where subProductId = ? and unregisterDate is null ";

			stmtSubscription = connection.prepareStatement(SQL);

			stmtSubscription.setLong(1, userId);
			stmtSubscription.setString(2, userName);
			stmtSubscription.setInt(3, Constants.SUPPLIER_ACTIVE_STATUS);
			stmtSubscription.setTimestamp(4,
					DateUtil.getTimestampSQL(expirationDate));
			stmtSubscription.setTimestamp(5,
					DateUtil.getTimestampSQL(graceDate));
			stmtSubscription.setLong(6, subProductId);

			stmtSubscription.execute();

			if (stmtSubscription.getUpdateCount() == 0)
			{
				throw new AppException(Constants.ERROR_UNREGISTERED);
			}

			if (product.isAuditEnable()
					&& (subscriberProduct.getSupplierStatus() != Constants.SUPPLIER_ACTIVE_STATUS))
			{
				SubscriberActivateImpl.updateActivate(connection, userId,
						userName, subscriberProduct.getSubscriberId(),
						subscriberProduct.getIsdn(),
						subscriberProduct.getProductId(), new Date(),
						subscriberProduct.getBarringStatus(),
						subscriberProduct.getSupplierStatus(), "");
			}

			subscriberProduct.setExpirationDate(expirationDate);
			subscriberProduct.setGraceDate(graceDate);
			subscriberProduct
					.setSupplierStatus(Constants.SUPPLIER_ACTIVE_STATUS);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscription);
		}

		return subscriberProduct;
	}

	public static SubscriberProduct subscription(long userId, String userName,
			long subProductId, boolean fullOfCharge, int quantity, boolean includeCurrentDay)
			throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return subscription(connection, userId, userName, subProductId,
					fullOfCharge, quantity, includeCurrentDay);
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

	public static void changeLanguage(Connection connection, long userId,
			String userName, long subProductId, String languageId)
			throws Exception
	{
		PreparedStatement stmtProduct = null;

		try
		{
			// prepare SQL
			String SQL = "Update 	SubscriberProduct "
					+ "   Set 		userId = ?, userName = ?, modifiedDate = sysDate, languageId = ? "
					+ "   Where 	subProductId = ? and unregisterDate is null ";

			stmtProduct = connection.prepareStatement(SQL);

			// update
			stmtProduct.setLong(1, userId);
			stmtProduct.setString(2, userName);
			stmtProduct.setString(3, languageId);
			stmtProduct.setLong(4, subProductId);

			if (stmtProduct.executeUpdate() == 0)
			{
				throw new AppException("unregistered");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
		}
	}

	public static void changeLanguage(long userId, String userName,
			long subProductId, String languageId) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			changeLanguage(connection, userId, userName, subProductId,
					languageId);
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

	public static void unbarringBySupplier(Connection connection, long userId,
			String userName, long subProductId) throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtSubscription = null;

		try
		{
			subscriberProduct = getProduct(connection, subProductId);

			if (subscriberProduct == null)
			{
				throw new AppException(Constants.ERROR_UNREGISTERED);
			}
			else if (subscriberProduct.getSupplierStatus() == Constants.SUPPLIER_ACTIVE_STATUS)
			{
				return;
			}

			ProductEntry product = ProductFactory.getCache().getProduct(
					subscriberProduct.getProductId());

			String sql = "Update SubscriberProduct "
					+ "     Set    userId = ?, userName = ?, modifiedDate = sysDate, supplierStatus = ?, lastrundate = sysdate "
					+ "     Where  subProductId = ? and unregisterDate is null ";

			stmtSubscription = connection.prepareStatement(sql);

			stmtSubscription.setLong(1, userId);
			stmtSubscription.setString(2, userName);
			stmtSubscription.setInt(3, Constants.SUPPLIER_ACTIVE_STATUS);
			stmtSubscription.setLong(4, subProductId);

			stmtSubscription.execute();

			subscriberProduct
					.setSupplierStatus(Constants.SUPPLIER_ACTIVE_STATUS);

			if (product.isAuditEnable())
			{
				SubscriberActivateImpl.updateActivate(connection, userId,
						userName, subscriberProduct.getSubscriberId(),
						subscriberProduct.getIsdn(),
						subscriberProduct.getProductId(), new Date(),
						subscriberProduct.getBarringStatus(),
						subscriberProduct.getSupplierStatus(), "");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscription);
		}
	}

	public static void unbarringBySupplier(long userId, String userName,
			long subProductId) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			unbarringBySupplier(connection, userId, userName, subProductId);
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

	/**
	 * 
	 * Created by NamTA <br>
	 * Created Date: 16/05/2012
	 * 
	 * @param connection
	 * @param userId
	 * @param userName
	 * @param subProductId
	 * @return
	 * @throws Exception
	 */
	public static SubscriberProduct extendExpirationDate(Connection connection,
			long userId, String userName, long subProductId, long campaignId,
			boolean includeCurrentDay, boolean fullOfCharge, int quantity) throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtSubscription = null;

		try
		{
			subscriberProduct = getProduct(connection, subProductId);

			if (subscriberProduct == null)
			{
				throw new AppException(Constants.ERROR_UNREGISTERED);
			}

			ProductEntry product = ProductFactory.getCache().getProduct(
					subscriberProduct.getProductId());

			Date now = new Date();
			Date expirationDate = subscriberProduct.getExpirationDate();
			Date graceDate = null;
			if (expirationDate.before(now))
				expirationDate = now;

			if (product.isSubscription())
			{
//				int quantity = 1;

//				int expirationPeriod = product.getSubscriptionPeriod();
//				String expirationUnit = product.getSubscriptionUnit();
				
				String expirationUnit = fullOfCharge ? product
						.getSubscriptionUnit() : "daily";
				int expirationPeriod = fullOfCharge ? product
						.getSubscriptionPeriod() : quantity;

				if (campaignId != DEFAULT_ID)
				{
					CampaignEntry campaign = CampaignFactory.getCache()
							.getCampaign(campaignId);

					if ((campaign != null))
					{
						expirationPeriod = campaign.getSchedulePeriod();
						expirationUnit = campaign.getScheduleUnit();
					}
				}

				boolean truncExpire = Boolean.parseBoolean(product
						.getParameter("TruncExpireDate", "true"));
				expirationDate = calculateExpirationDate(expirationDate,
						expirationUnit, expirationPeriod, 1, truncExpire);

				if (includeCurrentDay)
				{
					/**
					 * remove 1 day if expiration time includes current day
					 */
					if (includeCurrentDay)
					{
						Calendar expiration = Calendar.getInstance();
						expiration.setTime(expirationDate);
						expiration.add(Calendar.DATE, -1);

						expirationDate = expiration.getTime();
					}
				}

				graceDate = DateUtil.addDate(expirationDate,
						product.getGraceUnit(), product.getGracePeriod());
			}

			String sql = "Update SubscriberProduct "
					+ "     Set    userId = ?, userName = ?, modifiedDate = sysDate, supplierStatus = ?, "
					+ "     expirationDate = ?, graceDate = ?, lastrundate = sysdate, status = ? "
					+ "     Where  subProductId = ? and unregisterDate is null ";

			stmtSubscription = connection.prepareStatement(sql);

			stmtSubscription.setLong(1, userId);
			stmtSubscription.setString(2, userName);
			stmtSubscription.setInt(3, Constants.SUPPLIER_ACTIVE_STATUS);
			stmtSubscription.setTimestamp(4,
					DateUtil.getTimestampSQL(expirationDate));
			stmtSubscription.setTimestamp(5,
					DateUtil.getTimestampSQL(graceDate));
			stmtSubscription.setInt(6, Constants.SUBSCRIBER_REGISTER_STATUS);
			stmtSubscription.setLong(7, subProductId);

			stmtSubscription.execute();

			subscriberProduct
					.setSupplierStatus(Constants.SUPPLIER_ACTIVE_STATUS);
			subscriberProduct.setExpirationDate(expirationDate);
			subscriberProduct.setGraceDate(graceDate);

			if (product.isAuditEnable())
			{
				SubscriberActivateImpl.updateActivate(connection, userId,
						userName, subscriberProduct.getSubscriberId(),
						subscriberProduct.getIsdn(),
						subscriberProduct.getProductId(), new Date(),
						subscriberProduct.getBarringStatus(),
						subscriberProduct.getSupplierStatus(), "");
			}

			return subscriberProduct;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscription);
		}
	}
	
	public static SubscriberProduct extendExpirationFree(Connection connection,
			long userId, String userName, long subProductId, long campaignId,
			boolean includeCurrentDay, boolean fullOfCharge, int quantity) throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtSubscription = null;

		try
		{
			subscriberProduct = getProduct(connection, subProductId);

			if (subscriberProduct == null)
			{
				throw new AppException(Constants.ERROR_UNREGISTERED);
			}

			ProductEntry product = ProductFactory.getCache().getProduct(
					subscriberProduct.getProductId());

			Date now = new Date();
			Date expirationDate = new Date();
			Date graceDate = null;
			if (expirationDate.before(now))
				expirationDate = now;

			if (product.isSubscription())
			{
//				int quantity = 1;

//				int expirationPeriod = product.getSubscriptionPeriod();
//				String expirationUnit = product.getSubscriptionUnit();
				
				String expirationUnit = fullOfCharge ? product
						.getSubscriptionUnit() : "daily";
				int expirationPeriod = fullOfCharge ? product
						.getSubscriptionPeriod() : quantity;

				if (campaignId != DEFAULT_ID)
				{
					CampaignEntry campaign = CampaignFactory.getCache()
							.getCampaign(campaignId);

					if ((campaign != null))
					{
						expirationPeriod = campaign.getSchedulePeriod();
						expirationUnit = campaign.getScheduleUnit();
					}
				}

				boolean truncExpire = Boolean.parseBoolean(product
						.getParameter("TruncExpireDate", "true"));
				expirationDate = calculateExpirationDate(expirationDate,
						expirationUnit, expirationPeriod, 1, truncExpire);

				if (includeCurrentDay)
				{
					/**
					 * remove 1 day if expiration time includes current day
					 */
					if (includeCurrentDay)
					{
						Calendar expiration = Calendar.getInstance();
						expiration.setTime(expirationDate);
						expiration.add(Calendar.DATE, -1);

						expirationDate = expiration.getTime();
					}
				}

				graceDate = DateUtil.addDate(expirationDate,
						product.getGraceUnit(), product.getGracePeriod());
			}

			String sql = "Update SubscriberProduct "
					+ "     Set    userId = ?, userName = ?, modifiedDate = sysDate, supplierStatus = ?, "
					+ "     registerDate = sysdate, expirationDate = ?, graceDate = ?, lastrundate = sysdate, status = ? "
					+ "     Where  subProductId = ? and unregisterDate is null ";

			stmtSubscription = connection.prepareStatement(sql);

			stmtSubscription.setLong(1, userId);
			stmtSubscription.setString(2, userName);
			stmtSubscription.setInt(3, Constants.SUPPLIER_ACTIVE_STATUS);
			stmtSubscription.setTimestamp(4,
					DateUtil.getTimestampSQL(expirationDate));
			stmtSubscription.setTimestamp(5,
					DateUtil.getTimestampSQL(graceDate));
			stmtSubscription.setInt(6, Constants.SUBSCRIBER_REGISTER_STATUS);
			stmtSubscription.setLong(7, subProductId);

			stmtSubscription.execute();

			subscriberProduct
					.setSupplierStatus(Constants.SUPPLIER_ACTIVE_STATUS);
			subscriberProduct.setExpirationDate(expirationDate);
			subscriberProduct.setGraceDate(graceDate);

			if (product.isAuditEnable())
			{
				SubscriberActivateImpl.updateActivate(connection, userId,
						userName, subscriberProduct.getSubscriberId(),
						subscriberProduct.getIsdn(),
						subscriberProduct.getProductId(), new Date(),
						subscriberProduct.getBarringStatus(),
						subscriberProduct.getSupplierStatus(), "");
			}

			return subscriberProduct;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscription);
		}
	}

	/**
	 * 
	 * Created by NamTA<br>
	 * Created Date: 16/05/2012
	 * 
	 * @param userId
	 * @param userName
	 * @param subProductId
	 * @return
	 * @throws Exception
	 */
	public static SubscriberProduct extendExpirationDate(long userId,
			String userName, long subProductId, long campaignId,
			boolean includeCurrentDay, boolean fullOfCharge, int quantity) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return extendExpirationDate(connection, userId, userName,
					subProductId, campaignId, includeCurrentDay, fullOfCharge, quantity);
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
	
	public static SubscriberProduct extendExpirationFree(long userId,
			String userName, long subProductId, long campaignId,
			boolean includeCurrentDay, boolean fullOfCharge, int quantity) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return extendExpirationFree(connection, userId, userName,
					subProductId, campaignId, includeCurrentDay, fullOfCharge, quantity);
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

	public static String getMemberList(Connection connection, String isdn,
			long productId, boolean includeSuspended) throws Exception
	{
		String phoneBookList = "";
		PreparedStatement stmtSubscription = null;

		ResultSet resultSet = null;

		try
		{
			String sql = "";
			if (includeSuspended)
			{
				sql = "Select * from subscribergroup "
						+ "Where  isdn = ? and productid = ? "
						+ " and unregisterdate is null order by createdate";
			}
			else
			{
				sql = "Select * from subscribergroup "
						+ "Where  isdn = ? and productid = ? and status = ? "
						+ " and unregisterdate is null order by createdate";
			}

			stmtSubscription = connection.prepareStatement(sql);

			stmtSubscription.setString(1, isdn);
			stmtSubscription.setLong(2, productId);
			if (!includeSuspended)
			{
				stmtSubscription.setInt(3, Constants.SUPPLIER_ACTIVE_STATUS);
			}
			resultSet = stmtSubscription.executeQuery();

			if (resultSet.next())
			{
				phoneBookList = com.fss.util.StringUtil.nvl(
						resultSet.getString("REFERALISDN"), "");
			}

			while (resultSet.next())
			{
				phoneBookList = phoneBookList
						+ ","
						+ com.fss.util.StringUtil.nvl(
								resultSet.getString("REFERALISDN"), "");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscription);
		}
		return phoneBookList;
	}

	public static String getMemberList(String isdn, String userName,
			long productId, boolean includeSuspended) throws Exception
	{
		Connection connection = null;
		String result;
		try
		{
			connection = Database.getConnection();

			result = getMemberList(connection, isdn, productId,
					includeSuspended);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(connection);
		}
		return result;
	}

	public static boolean withdraw(long userId, String userName,
			long subscriberId, String isdn, String balanceType, double amount)
			throws Exception
	{
		Connection connection = Database.getConnection();
		connection.setAutoCommit(false);

		PreparedStatement stmtBalance = null;

		try
		{
			String SQL = "Update SubscriberBalance "
					+ "Set userId = ?, userName = ?, modifiedDate = sysDate, balanceAmount = nvl(balanceAmount, 0) - ? "
					+ "Where subscriberId = ? and balanceType = ? and nvl(balanceAmount, 0) >= ? ";

			stmtBalance = connection.prepareStatement(SQL);

			stmtBalance.setLong(1, userId);
			stmtBalance.setString(2, userName);
			stmtBalance.setDouble(3, amount);
			stmtBalance.setString(4, isdn);
			stmtBalance.setString(5, balanceType);
			stmtBalance.setDouble(6, amount);

			stmtBalance.execute();

			if (stmtBalance.getUpdateCount() == 0)
			{
				throw new AppException("not-enough-money");
			}

			connection.commit();
		}
		catch (Exception e)
		{
			Database.rollback(connection);

			throw e;
		}
		finally
		{
			Database.closeObject(stmtBalance);
			Database.closeObject(connection);
		}

		return true;
	}

	/**
	 * Create by Do Tien Hung
	 * 
	 * @param connection
	 * @param userId
	 * @param userName
	 * @param subscriberId
	 * @param isdn
	 * @param subscriberType
	 * @param productId
	 * @param campaignId
	 * @param languageId
	 * @param includeCurrentDay
	 * @return
	 * @throws Exception
	 */

	public static SubscriberProduct registerProductBypassExisted(
			Connection connection, long userId, String userName,
			long subscriberId, String isdn, int subscriberType, long productId,
			long campaignId, String languageId, boolean includeCurrentDay, int status, String activationDate)
			throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtRegister = null;

		try
		{
			Date now = new Date();

			ProductEntry product = ProductFactory.getCache().getProduct(
					productId);

			// calculate term of use date
			Date termDate = null;

			if (product.getTermPeriod() > 0)
			{
				termDate = DateUtil.addDate(now, product.getTermUnit(),
						product.getTermPeriod());
			}

			// calculate expire date
			Date expirationDate = null;
			Date graceDate = null;

			if (product.isSubscription())
			{
				int quantity = 1;

				int expirationPeriod = product.getSubscriptionPeriod();

				String expirationUnit = product.getSubscriptionUnit();

				if (campaignId != DEFAULT_ID)
				{
					CampaignEntry campaign = CampaignFactory.getCache()
							.getCampaign(campaignId);

					if ((campaign != null))
					{
						expirationPeriod = campaign.getSchedulePeriod();
						expirationUnit = campaign.getScheduleUnit();
					}
				}

				boolean truncExpire = Boolean.parseBoolean(product
						.getParameter("TruncExpireDate", "true"));
				expirationDate = calculateExpirationDate(now, expirationUnit,
						expirationPeriod, quantity, truncExpire);

				/**
				 * remove 1 day if expiration time includes current day
				 */
				if (includeCurrentDay)
				{
					Calendar expiration = Calendar.getInstance();
					expiration.setTime(expirationDate);
					expiration.add(Calendar.DATE, -1);

					expirationDate = expiration.getTime();
				}

				graceDate = DateUtil.addDate(expirationDate,
						product.getGraceUnit(), product.getGracePeriod());
			}

			// check product are registered or not
			String[] listProductId = product.getParameter("listProductId", "")
					.split(",");
			for (int i = 0; i < listProductId.length; i++)
			{
				subscriberProduct = getActive(connection, subscriberId, isdn,
						Long.parseLong(listProductId[i]));
				if (subscriberProduct != null)
				{
					break;
				}
			}

			if (subscriberProduct != null)
			{
				long subProductId = subscriberProduct.getSubProductId();
				unregister(connection, userId, userName, subProductId,
						productId);
			}

			// register product for subscriber
			String sql = "Insert into SubscriberProduct "
					+ "     (subProductId, userId, userName, createDate, modifiedDate "
					+ "     , subscriberId, isdn, subscriberType, productId, languageId "
					+ "     , registerDate, termDate, expirationDate, graceDate, barringStatus, supplierStatus, CampaignId, Status, ActivationDate) "
					+ "Values " + "     (?, ?, ?, sysDate, sysDate "
					+ "     , ?, ?, ?, ?, ? " + "     , ?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'YYYY-MM-DD HH24:MI:SS'))";

			stmtRegister = connection.prepareStatement(sql);

			long subProductId = Database.getSequence(connection,
					"sub_product_seq");

			int barringStatus = Constants.USER_ACTIVE_STATUS;
			int supplierStatus = Constants.SUPPLIER_ACTIVE_STATUS;

			stmtRegister.setLong(1, subProductId);
			stmtRegister.setLong(2, userId);
			stmtRegister.setString(3, userName);

			stmtRegister.setLong(4, subscriberId);
			stmtRegister.setString(5, isdn);
			stmtRegister.setInt(6, subscriberType);
			stmtRegister.setLong(7, productId);
			stmtRegister.setString(8, languageId);

			stmtRegister.setTimestamp(9, DateUtil.getTimestampSQL(now));
			stmtRegister.setTimestamp(10, DateUtil.getTimestampSQL(termDate));
			stmtRegister.setTimestamp(11,
					DateUtil.getTimestampSQL(expirationDate));
			stmtRegister.setTimestamp(12, DateUtil.getTimestampSQL(graceDate));

			stmtRegister.setInt(13, barringStatus);
			stmtRegister.setInt(14, supplierStatus);
			stmtRegister.setLong(15, campaignId);
			stmtRegister.setInt(16, status);
			stmtRegister.setString(17, activationDate);

			stmtRegister.execute();

			// bind return
			// bind order
			subscriberProduct = new SubscriberProduct();

			subscriberProduct.setUserId(userId);
			subscriberProduct.setUserName(userName);

			subscriberProduct.setSubscriberId(subscriberId);
			subscriberProduct.setSubProductId(subProductId);
			subscriberProduct.setProductId(productId);

			subscriberProduct.setSubscriberType(subscriberType);
			subscriberProduct.setIsdn(isdn);

			subscriberProduct.setRegisterDate(now);
			subscriberProduct.setTermDate(termDate);
			subscriberProduct.setExpirationDate(expirationDate);
			subscriberProduct.setGraceDate(graceDate);

			subscriberProduct.setBarringStatus(barringStatus);
			subscriberProduct.setSupplierStatus(supplierStatus);

			if (product.isAuditEnable())
			{
				SubscriberActivateImpl.addActivate(connection, userId,
						userName, subscriberId, isdn, subProductId,
						subscriberProduct.getRegisterDate(),
						subscriberProduct.getBarringStatus(),
						subscriberProduct.getSupplierStatus(), "");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtRegister);
		}

		return subscriberProduct;
	}

	public static SubscriberProduct registerProductBypassExisted(long userId,
			String userName, long subscriberId, String isdn,
			int subscriberType, long productId, long campaignId,
			String languageId, boolean includeCurrentDay, int status, String activationDate) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return registerProductBypassExisted(connection, userId, userName,
					subscriberId, isdn, subscriberType, productId, campaignId,
					languageId, includeCurrentDay, status, activationDate);
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
	
	public static SubscriberProduct registerProductInvite(
			Connection connection, long userId, String userName,
			long subscriberId, String isdn, int subscriberType, long productId,
			long campaignId, String languageId, boolean includeCurrentDay, int status, String activationDate)
			throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtRegister = null;

		try
		{
			Date now = new Date();

			ProductEntry product = ProductFactory.getCache().getProduct(
					productId);

			// calculate term of use date
			Date termDate = null;

			if (product.getTermPeriod() > 0)
			{
				termDate = DateUtil.addDate(now, product.getTermUnit(),
						product.getTermPeriod());
			}

			// calculate expire date
			Date expirationDate = null;
			Date graceDate = null;

			if (product.isSubscription())
			{
				int quantity = 1;

				int expirationPeriod = product.getSubscriptionPeriod();

				String expirationUnit = product.getSubscriptionUnit();

				if (campaignId != DEFAULT_ID)
				{
					CampaignEntry campaign = CampaignFactory.getCache()
							.getCampaign(campaignId);

					if ((campaign != null))
					{
						expirationPeriod = campaign.getSchedulePeriod();
						expirationUnit = campaign.getScheduleUnit();
					}
				}

				boolean truncExpire = Boolean.parseBoolean(product
						.getParameter("TruncExpireDate", "true"));
				expirationDate = calculateExpirationDate(now, expirationUnit,
						expirationPeriod, quantity, truncExpire);

				/**
				 * remove 1 day if expiration time includes current day
				 */
				if (includeCurrentDay)
				{
					Calendar expiration = Calendar.getInstance();
					expiration.setTime(expirationDate);
					expiration.add(Calendar.DATE, -1);

					expirationDate = expiration.getTime();
				}

				graceDate = DateUtil.addDate(expirationDate,
						product.getGraceUnit(), product.getGracePeriod());
			}

			// check product are registered or not
			String[] listProductId = product.getParameter("listProductId", "")
					.split(",");
			for (int i = 0; i < listProductId.length; i++)
			{
				subscriberProduct = getActive(connection, subscriberId, isdn,
						Long.parseLong(listProductId[i]));
				if (subscriberProduct != null)
				{
					break;
				}
			}

			if (subscriberProduct != null)
			{
				long subProductId = subscriberProduct.getSubProductId();
				updateExpireWhenInvite(connection, expirationDate, subProductId);
			}

			// register product for subscriber
			String sql = "Insert into SubscriberProduct "
					+ "     (subProductId, userId, userName, createDate, modifiedDate "
					+ "     , subscriberId, isdn, subscriberType, productId, languageId "
					+ "     , registerDate, termDate, expirationDate, graceDate, barringStatus, supplierStatus, CampaignId, Status, ActivationDate) "
					+ "Values " + "     (?, ?, ?, sysDate, sysDate "
					+ "     , ?, ?, ?, ?, ? " + "     , ?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'YYYY-MM-DD HH24:MI:SS'))";

			stmtRegister = connection.prepareStatement(sql);

			long subProductId = Database.getSequence(connection,
					"sub_product_seq");

			int barringStatus = Constants.USER_ACTIVE_STATUS;
			int supplierStatus = Constants.SUPPLIER_ACTIVE_STATUS;

			stmtRegister.setLong(1, subProductId);
			stmtRegister.setLong(2, userId);
			stmtRegister.setString(3, userName);

			stmtRegister.setLong(4, subscriberId);
			stmtRegister.setString(5, isdn);
			stmtRegister.setInt(6, subscriberType);
			stmtRegister.setLong(7, productId);
			stmtRegister.setString(8, languageId);

			stmtRegister.setTimestamp(9, DateUtil.getTimestampSQL(now));
			stmtRegister.setTimestamp(10, DateUtil.getTimestampSQL(termDate));
			stmtRegister.setTimestamp(11,
					DateUtil.getTimestampSQL(expirationDate));
			stmtRegister.setTimestamp(12, DateUtil.getTimestampSQL(graceDate));

			stmtRegister.setInt(13, barringStatus);
			stmtRegister.setInt(14, supplierStatus);
			stmtRegister.setLong(15, campaignId);
			stmtRegister.setInt(16, status);
			stmtRegister.setString(17, activationDate);

			stmtRegister.execute();

			// bind return
			// bind order
			subscriberProduct = new SubscriberProduct();

			subscriberProduct.setUserId(userId);
			subscriberProduct.setUserName(userName);

			subscriberProduct.setSubscriberId(subscriberId);
			subscriberProduct.setSubProductId(subProductId);
			subscriberProduct.setProductId(productId);

			subscriberProduct.setSubscriberType(subscriberType);
			subscriberProduct.setIsdn(isdn);

			subscriberProduct.setRegisterDate(now);
			subscriberProduct.setTermDate(termDate);
			subscriberProduct.setExpirationDate(expirationDate);
			subscriberProduct.setGraceDate(graceDate);

			subscriberProduct.setBarringStatus(barringStatus);
			subscriberProduct.setSupplierStatus(supplierStatus);

			if (product.isAuditEnable())
			{
				SubscriberActivateImpl.addActivate(connection, userId,
						userName, subscriberId, isdn, subProductId,
						subscriberProduct.getRegisterDate(),
						subscriberProduct.getBarringStatus(),
						subscriberProduct.getSupplierStatus(), "");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtRegister);
		}

		return subscriberProduct;
	}

	public static SubscriberProduct registerProductInvite(long userId,
			String userName, long subscriberId, String isdn,
			int subscriberType, long productId, long campaignId,
			String languageId, boolean includeCurrentDay, int status, String activationDate) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			return registerProductInvite(connection, userId, userName,
					subscriberId, isdn, subscriberType, productId, campaignId,
					languageId, includeCurrentDay, status, activationDate);
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
	
	public static void updateExpireWhenInvite(Connection connection, Date expirationDate, long subProductId)
			throws Exception
	{
		PreparedStatement stmtSubscription = null;

		try
		{
			String sql = "Update SubscriberProduct "
					+ "   Set 	modifiedDate = sysDate, lastrundate = sysdate, expirationDate = ? "
					+ "	  Where subProductId = ? and " + CONDITION_UNTERMINATED;

			stmtSubscription = connection.prepareStatement(sql);

			stmtSubscription.setTimestamp(1, DateUtil.getTimestampSQL(expirationDate));
			stmtSubscription.setLong(2, subProductId);

			stmtSubscription.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtSubscription);
		}
	}

	public static void registerFlexi(String isdn, long productId,
			long subProductId) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			if (!registerFlexi(connection, isdn, productId, 0, subProductId, 0))
			{
				throw new Exception(ERROR_REGISTER_FLEXI);
			}
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

	public static boolean registerFlexi(Connection connection, String isdn,
			long productId, int bypasstime, long id, int sms_flg)
			throws Exception
	{
		boolean success = false;

		PreparedStatement stmtFlexi = null;
		ResultSet rs = null;
		try
		{
			String strSQLCheck = "Select Id From Flexi_Alert Where isdn = ? and supplierstatus = 1";
			stmtFlexi = connection.prepareStatement(strSQLCheck);
			stmtFlexi.setString(1, isdn);
			rs = stmtFlexi.executeQuery();

			String strSQL = "INSERT INTO FLEXI_ALERT "
					+ "(Id, isdn, supplierstatus, productid, bypasstime, sms_flg) "
					+ "VALUES ( ?, ?,1, ?, 0, 0)";

			boolean existed = false;
			long flexiId = 0;

			if (rs.next())
			{
				existed = true;
				flexiId = rs.getLong("ID");
			}
			if (existed)
			{
				strSQL = "UPDATE FLEXI_ALERT SET ID = ?, productid = ?, bypasstime = 0, supplierstatus = 1, sms_flg = 0, scheduletime = null "
						+ "WHERE ID = ?";
				stmtFlexi = connection.prepareStatement(strSQL);
				stmtFlexi.setLong(1, id);
				stmtFlexi.setLong(2, productId);
				stmtFlexi.setLong(3, flexiId);
			}
			else
			{
				stmtFlexi = connection.prepareStatement(strSQL);
				stmtFlexi.setLong(1, id);
				stmtFlexi.setString(2, isdn);
				stmtFlexi.setLong(3, productId);
			}

			stmtFlexi.execute();
			if (stmtFlexi.getUpdateCount() > 0)
			{
				success = true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rs);
			Database.closeObject(stmtFlexi);
			Database.closeObject(connection);
		}
		return success;
	}

	public static void updateScheduleFlexi(long subProductId, 
			double remainBalance, Calendar scandTime) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			if (!updateScheduleFlexi(connection, subProductId, remainBalance, scandTime))
			{
				throw new Exception(ERROR_UPDATE_FLEXI);
			}
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

	public static boolean updateScheduleFlexi(Connection connection, long id, 
			double remainBalance, Calendar scanTime) throws Exception
	{
		boolean success = false;
		PreparedStatement stmtFlexi = null;
		try
		{
			String strSQL = "UPDATE SubscriberProduct SET  scheduletime = ? WHERE subproductid = ? ";
			stmtFlexi = connection.prepareStatement(strSQL);			
			stmtFlexi.setTimestamp(1, (scanTime != null? DateUtil.getTimestampSQL(scanTime.getTime()) : null));
			stmtFlexi.setLong(2, id);
			
			stmtFlexi.execute();
			if (stmtFlexi.getUpdateCount() > 0)
			{
				success = true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtFlexi);
		}

		return success;
	}
	
	public static void updateFlexi(long subProductId, 
			double remainBalance, Calendar scandTime, int status) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			if (!updateFlexi(connection, subProductId, remainBalance, scandTime, status))
			{
				throw new Exception(ERROR_UPDATE_FLEXI);
			}
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

	public static boolean updateFlexi(Connection connection, long id, 
			double remainBalance, Calendar scanTime, int status) throws Exception
	{
		boolean success = false;
		PreparedStatement stmtFlexi = null;
		try
		{
			String strSQL = "UPDATE SubscriberProduct SET  scheduletime = ?, status = ? WHERE subproductid = ? ";
			stmtFlexi = connection.prepareStatement(strSQL);			
			stmtFlexi.setTimestamp(1, (scanTime != null? DateUtil.getTimestampSQL(scanTime.getTime()) : null));
			stmtFlexi.setInt(2, status);
			stmtFlexi.setLong(3, id);
			
			stmtFlexi.execute();
			if (stmtFlexi.getUpdateCount() > 0)
			{
				success = true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtFlexi);
		}

		return success;
	}
	

	
	public static void updateSubscription(int status, long subProductId) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			updateSubscription(connection, status, subProductId);
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
	
	public static void updateSubscription(
			Connection connection, int status, long subProductId)
			throws Exception
	{
		PreparedStatement stmtOrder = null;

		try
		{
			String sql = "Update SubscriberProduct Set status = ? Where subProductid = ? ";

			stmtOrder = connection.prepareStatement(sql);

			stmtOrder.setInt(1, status);
			stmtOrder.setLong(2, subProductId);

			stmtOrder.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtOrder);
		}
	}
	
	public static void insertSendSMS(String sourceAddress, String targetAddress, String content) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			insertSendSMS(connection, sourceAddress, targetAddress, content);
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
	
	public static void insertSendSMS(
			Connection connection, String sourceAddress, String targetAddress, String content)
			throws Exception
	{
		PreparedStatement stmtOrder = null;

		try
		{
			String sql = "Insert Into Send_SMS Values(Send_SMS_Seq.nextval, ?, ?, ?, sysdate)";

			stmtOrder = connection.prepareStatement(sql);

			stmtOrder.setString(1, sourceAddress);
			stmtOrder.setString(2, targetAddress);
			stmtOrder.setString(3, content);

			stmtOrder.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtOrder);
		}
	}
	
	public static ArrayList<SubscriberProduct> checkRegisterService(String isdn, String fromDate, 
			long productId, String activationDate) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			 return checkRegisterService(connection, isdn, fromDate, productId, activationDate);
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
	
	public static ArrayList<SubscriberProduct> checkRegisterService(
			Connection connection, String isdn, String fromDate, long productId, String activationDate)
			throws Exception
	{
		PreparedStatement stmtOrder = null;
		ResultSet rsActive = null;
		
		ArrayList<SubscriberProduct> result = new ArrayList<SubscriberProduct>();
		
		try
		{
			String sql = "Select * from SubscriberProduct " +
						 "where isdn = ? and productId = ? and " +
						 "registerdate >= to_date(?,'YYYY-MM-DD HH24:MI:SS') and " + 
						 "activationDate = to_date(?,'YYYY-MM-DD HH24:MI:SS')";

			stmtOrder = connection.prepareStatement(sql);

			stmtOrder.setString(1, isdn);
			stmtOrder.setLong(2, productId);
			stmtOrder.setString(3, fromDate);
			stmtOrder.setString(4, activationDate);

			rsActive = stmtOrder.executeQuery();

			while (rsActive.next())
			{
				result.add(getProduct(rsActive));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtOrder);
		}
		
		return result;
	}
	
	public static int checkUsage(String isdn, String alias) throws Exception
	{
		Connection connection = null;

		int result = 0;

		try
		{
			connection = Database.getConnection();

			result = checkUsage(connection, isdn, alias);
		}
		finally
		{
			Database.closeObject(connection);
		}

		return result;
	}
	
	public static int checkUsage(Connection connection, String isdn, String alias) throws Exception
	{
		isdn = CommandUtil.addCountryCode(isdn);

		int result = 0;

		CallableStatement cstmt = null;

		try
		{
			String call = "{call ? := check_usage(?,?) }";

			cstmt = connection.prepareCall(call);
			cstmt.setQueryTimeout(30);
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setString(2, isdn);
			cstmt.setString(3, alias);

			cstmt.execute();

			// Check condition MP services
			// 0 : success
			// 1 : fail
			// other: customer's number is not VNM's.
			result = cstmt.getInt(1);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(cstmt);
		}

		return result;

	}
}
