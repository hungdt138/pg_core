package com.crm.subscriber.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.marketing.cache.CampaignEntry;
import com.crm.marketing.cache.CampaignFactory;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.util.DateUtil;
import com.fss.util.AppException;

public class IDDServiceImpl
{
	private static long DEFAULT_ID = 0;
	
	public static boolean confirmRegister(long userId, String userName,
			long subscriberId, String isdn, int subscriberType, long productId,
			String languageId, int status, String activationDate)
			throws Exception
	{
		boolean success = false;

		Connection connection = null;
		PreparedStatement stmtProduct = null;

		try
		{
			String sql = "Insert into SubscriberProduct "
					+ "     (subProductId, userId, userName, createDate, modifiedDate "
					+ "     , subscriberId, isdn, subscriberType, productId, languageId "
					+ "     , supplierStatus, Status, ActivationDate) "
					+ "Values "
					+ "     (?, ?, ?, sysDate, sysDate "
					+ "     , ?, ?, ?, ?, ? "
					+ "     , ?, ?, to_date(?,'YYYY-MM-DD HH24:MI:SS'))";

			connection = Database.getConnection();
			stmtProduct = connection.prepareStatement(sql);

			long subProductId = Database.getSequence(connection,
					"sub_product_seq");
			
			int supplierStatus = Constants.SUPPLIER_UNKNOW_STATUS;

			stmtProduct.setLong(1, subProductId);
			stmtProduct.setLong(2, userId);
			stmtProduct.setString(3, userName);

			stmtProduct.setLong(4, subscriberId);
			stmtProduct.setString(5, isdn);
			stmtProduct.setInt(6, subscriberType);
			stmtProduct.setLong(7, productId);
			stmtProduct.setString(8, languageId);

			stmtProduct.setInt(9, supplierStatus);
			stmtProduct.setInt(10, status);
			stmtProduct.setString(11, activationDate);

			stmtProduct.setQueryTimeout(10);

			stmtProduct.execute();

			success = true;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}

		return success;
	}

	public static void removeConfirm(long subProductId)
			throws Exception
	{
		String strSQL = "Update SubscriberProduct Set supplierstatus = ?, reservation = null where subProductId = ?";

		Connection connection = null;
		PreparedStatement stmtProduct = null;

		try
		{
			connection = Database.getConnection();
			stmtProduct = connection.prepareStatement(strSQL);
			
			stmtProduct.setInt(1, Constants.SUPPLIER_CANCEL_CONFIRM_STATUS);
			stmtProduct.setLong(2, subProductId);
			stmtProduct.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
	}

	public static int checkSendNotify(String isdn, double dayExpired,
			long productid, boolean notEnough, double maxTopUp)
			throws Exception
	{
		int existed = 0;

		String strSQLCheck = "select confirmstatus, trunc(confirmdate) as confirmdate, messagetype from NOTIFYIDDBUFFET where isdn = ? and productid = ?";

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		try
		{
			Date confirmDate;
			int confirmstatus = 0;
			Date currentDate = new Date();
			double dayExtend;
			int messagetype;

			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQLCheck);
			stmtProduct.setString(1, isdn);
			stmtProduct.setLong(2, productid);
			stmtProduct.setQueryTimeout(10);
			rsProduct = stmtProduct.executeQuery();

			int count = 0;
			while (rsProduct.next())
			{
				confirmDate = rsProduct.getDate("confirmdate");
				confirmstatus = rsProduct.getInt("confirmstatus");
				messagetype = rsProduct.getInt("messagetype");
				dayExtend = (double) ((currentDate.getTime() - confirmDate
						.getTime()) / (1000 * 60 * 60 * 24));
				if ((confirmstatus == 1 && messagetype == 4 && dayExtend <= maxTopUp)
						|| (confirmstatus == 1 && messagetype != 4 && (dayExpired <= 0 && dayExpired >= -3)))
				{
					existed = 1;
				}
				else if ((dayExpired <= 0)
						|| (confirmstatus == 2 && notEnough)
						|| (confirmstatus == 1 && messagetype == 4 && dayExtend > maxTopUp))
				{
					existed = -1;
				}
				else
				{
					existed = 2;
				}
				count++;
			}
			if (count == 0 && dayExpired <= 0)
			{
				existed = -1;
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
			Database.closeObject(connection);
		}

		return existed;
	}

	public static void notifyOverdue(String isdn, int status, int mesageType,
			long productid) throws Exception
	{
		String strSQL = "insert into NOTIFYIDDBUFFET values( ?, sysdate, 1, ?, ?, sysdate, ?, 0)";

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setLong(2, productid);
			stmtProduct.setInt(3, status);
			stmtProduct.setInt(4, mesageType);
			stmtProduct.setQueryTimeout(10);
			stmtProduct.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(rsProduct);
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
	}

	public static boolean isRegistedBefore(String isdn, long productid)
			throws Exception
	{
		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		int count = 0;
		String strSQL = "Select Count(*) as Total from subscriberproduct "
				+ "where productid = ? and isdn = ? and "
				+ " supplierstatus in (1,2,3) ";
		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setLong(1, productid);
			stmtProduct.setString(2, isdn);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				count = rsProduct.getInt("Total");
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
			Database.closeObject(connection);
		}

		return (count > 0);
	}

	public static boolean checkMaxRegister(long product, String isdn,
			int maxRegister) throws Exception
	{
		boolean success = false;

		Calendar cal = Calendar.getInstance();
		cal.add(cal.DAY_OF_MONTH, -29);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String strSQL = "select count(*) as total from subscriberproduct where productid = ? and isdn = ? "
				+ "and trunc(createdate) >= to_date( ?, 'SYYYY-MM-DD')";

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setLong(1, product);
			stmtProduct.setString(2, isdn);
			stmtProduct.setString(3, sdf.format(cal.getTime()));
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				int total = rsProduct.getInt("total");
				if (total >= maxRegister)
					success = true;
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
			Database.closeObject(connection);
		}

		return success;
	}

	public static SubscriberProduct isConfirmRegister(String isdn, long productID)
			throws Exception
	{
		SubscriberProduct subProduct = null;

		String strSQL = "Select * from SubscriberProduct where isdn = ? and productid = ? and supplierstatus = ?";

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setLong(2, productID);
			stmtProduct.setInt(3, Constants.SUPPLIER_UNKNOW_STATUS);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				subProduct = getProduct(rsProduct);
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
			Database.closeObject(connection);
		}

		return subProduct;
	}

//	public static boolean isConfirmExtend(long subProductId)
//			throws Exception
//	{
//		boolean bExtend = false;
//
//		String strSQL = "select * from subscriberproduct where subProductId = ?  and status in (?,?)";
//
//		Connection connection = null;
//		PreparedStatement stmtProduct = null;
//		ResultSet rsProduct = null;
//
//		try
//		{
//			connection = Database.getConnection();
//
//			stmtProduct = connection.prepareStatement(strSQL);
//			stmtProduct.setLong(1, subProductId);
//			stmtProduct.setInt(2, Constants.SUBSCRIBER_ALERT_EXPIRE_STATUS);
//			stmtProduct.setInt(2, Constants.SUBSCRIBER_ALERT_BALANCE_STATUS);
//			stmtProduct.setQueryTimeout(10);
//
//			rsProduct = stmtProduct.executeQuery();
//			if (rsProduct.next())
//			{
//				bExtend = true;
//			}
//		}
//		catch (Exception e)
//		{
//			throw e;
//		}
//		finally
//		{
//			Database.closeObject(rsProduct);
//			Database.closeObject(stmtProduct);
//			Database.closeObject(connection);
//		}
//
//		return bExtend;
//	}

	public static boolean isRenewNow(String isdn, long productId)
			throws Exception
	{
		boolean isRenew = false;

		String strSQL = "select * from NOTIFYIDDBUFFET where isdn = ? and confirmstatus = -1 and productId = ?";

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setLong(2, productId);
			stmtProduct.setQueryTimeout(10);

			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				isRenew = true;
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
			Database.closeObject(connection);
		}

		return isRenew;
	}

	public static void pendingNotify(String isdn, int confirmStatus,
			long productid) throws Exception
	{
		String strSQL = "update NOTIFYIDDBUFFET set confirmstatus = ?, confirmdate=sysdate, messagetype=-1, sendflg=0 where isdn = ? and productid = ?";

		Connection connection = null;
		PreparedStatement stmtProduct = null;

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setInt(1, confirmStatus);
			stmtProduct.setString(2, isdn);
			stmtProduct.setLong(3, productid);
			stmtProduct.setQueryTimeout(10);

			stmtProduct.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
	}

	public static void updateProperties(String isdn, long productid,
			String properties) throws Exception
	{
		String strSQL = "update subscriberproduct set properties = ? where isdn = ? and PRODUCTID = ? and SUPPLIERSTATUS = 1";

		Connection connection = null;
		PreparedStatement stmtProduct = null;

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, properties);
			stmtProduct.setString(2, isdn);
			stmtProduct.setLong(3, productid);
			stmtProduct.setQueryTimeout(10);

			stmtProduct.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
	}

	public static void updateIDDStatus(int status, long subProductId) throws Exception
	{
		String strSQL = "Update SubscriberProduct Set Status = ? where subProductId = ?";

		Connection connection = null;
		PreparedStatement stmtProduct = null;

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setInt(1, status);
			
			stmtProduct.setLong(2, subProductId);
			stmtProduct.setQueryTimeout(10);

			stmtProduct.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
	}

	public static void removeExtendIDDBuffet(String isdn, long productID)
			throws Exception
	{
		String strSQL = "delete NOTIFYIDDBUFFET where isdn = ? and productid = ?";

		Connection connection = null;
		PreparedStatement stmtProduct = null;

		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setLong(2, productID);
			stmtProduct.setQueryTimeout(10);

			stmtProduct.execute();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			Database.closeObject(stmtProduct);
			Database.closeObject(connection);
		}
	}

	public static boolean checkNotifyNotEnough(String isdn) throws Exception
	{
		String strSQL = "select * from NOTIFYIDDBUFFET where isdn = ? and messagetype=4 and sendflg=1";

		Connection connection = null;
		PreparedStatement stmtProduct = null;
		ResultSet rsProduct = null;

		boolean send = false;
		try
		{
			connection = Database.getConnection();

			stmtProduct = connection.prepareStatement(strSQL);
			stmtProduct.setString(1, isdn);
			stmtProduct.setQueryTimeout(10);
			rsProduct = stmtProduct.executeQuery();
			if (rsProduct.next())
			{
				send = true;
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
			Database.closeObject(connection);
		}

		return send;
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

			result.setSupplierStatus(rsProduct.getInt("supplierStatus"));
		}
		catch (Exception e)
		{
			throw e;
		}

		return result;
	}
	
	public static SubscriberProduct registerIDD(Connection connection,
			long userId, String userName, long subscriberId, String isdn,
			int subscriberType, long productId, long campaignId,
			String languageId, boolean includeCurrentDay, boolean isPrePaid,
			int status, String activationDate, long subProductId) throws Exception
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
				expirationDate = SubscriberProductImpl.calculateExpirationDate(now, expirationUnit,
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
				graceDate = SubscriberProductImpl.calculateGraceDate(expirationDate,
						product.getGraceUnit(), product.getGracePeriod());
			}

			// check product are registered or not
			subscriberProduct = SubscriberProductImpl.getActive(connection, subscriberId, isdn,
					productId);

			if (subscriberProduct != null)
			{
				throw new AppException(Constants.ERROR_REGISTERED);
			}

			// register product for subscriber
			String sql = "Update SubscriberProduct Set "
					+ "userId = ?, userName = ?, createDate = sysdate, modifiedDate = sysdate, "
					+ "subscriberId = ?, registerDate = ?, termDate = ?, expirationDate = ?, "
					+ "graceDate = ?, barringStatus = ?, supplierStatus = ?, CampaignId = ?, Status = ?, Reservation = null "
					+ "Where subProductId = ?";

			stmtRegister = connection.prepareStatement(sql);

			int barringStatus = Constants.USER_ACTIVE_STATUS;
			int supplierStatus = Constants.SUPPLIER_ACTIVE_STATUS;

			stmtRegister.setLong(1, userId);
			stmtRegister.setString(2, userName);
			stmtRegister.setLong(3, subscriberId);
			stmtRegister.setTimestamp(4, DateUtil.getTimestampSQL(now));
			stmtRegister.setTimestamp(5, DateUtil.getTimestampSQL(termDate));
			stmtRegister.setTimestamp(6,
					DateUtil.getTimestampSQL(expirationDate));
			stmtRegister.setTimestamp(7, DateUtil.getTimestampSQL(graceDate));

			stmtRegister.setInt(8, barringStatus);
			stmtRegister.setInt(9, supplierStatus);
			stmtRegister.setLong(10, campaignId);
			stmtRegister.setInt(11, status);
			stmtRegister.setLong(12, subProductId);

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
	
	public static SubscriberProduct registerIDD(long userId, String userName,
			long subscriberId, String isdn, int subscriberType, long productId,
			long campaignId, String languageId, boolean includeCurrentDay,
			boolean isPrePaid, int status, String activationDate, long subProductId)
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

			return registerIDD(connection, userId, userName, subscriberId, isdn,
					subscriberType, productId, campaignId, languageId,
					includeCurrentDay, isPrePaid, status, activationDate, subProductId);
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
	
	public static void barringVBService(long userId, String userName,
			long subProductId) throws Exception
	{
		Connection connection = null;

		try
		{
			connection = Database.getConnection();

			barringVBService(connection, userId, userName, subProductId);
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
	
	public static void barringVBService(Connection connection, long userId,
			String userName, long subProductId) throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtSubscription = null;

		try
		{
			subscriberProduct = SubscriberProductImpl.getProduct(connection, subProductId);

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
			
			int gracePeriod = product.getGracePeriod();
			String graceUnit = product.getGraceUnit();
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			
			Date graceDate = DateUtil.addDate(calendar.getTime(), graceUnit,
					gracePeriod);

			String sql = "Update SubscriberProduct "
					+ "     Set    userId = ?, userName = ?, modifiedDate = sysDate, supplierStatus = ?, graceDate = ?, lastrundate = sysdate, status = 1, reservation = null "
					+ "     Where  subProductId = ? and unregisterDate is null ";

			stmtSubscription = connection.prepareStatement(sql);

			stmtSubscription.setLong(1, userId);
			stmtSubscription.setString(2, userName);
			stmtSubscription.setInt(3, Constants.SUPPLIER_BARRING_STATUS);
			stmtSubscription.setTimestamp(4,
					DateUtil.getTimestampSQL(graceDate));
			stmtSubscription.setLong(5, subProductId);

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
	
	public static SubscriberProduct subscription(Connection connection,
			long userId, String userName, long subProductId,
			boolean fullOfCharge, int quantity, boolean includeCurrentDay) throws Exception
	{
		SubscriberProduct subscriberProduct = null;

		PreparedStatement stmtSubscription = null;

		try
		{
			subscriberProduct = SubscriberProductImpl.getProduct(connection, subProductId);

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
			if (expirationDate.getTime() < now.getTime()
					|| subscriberProduct.getStatus() == Constants.SUBSCRIBER_ALERT_BALANCE_STATUS
					|| subscriberProduct.getSupplierStatus() == Constants.SUPPLIER_BARRING_STATUS)
			{
				expirationDate = now;
			}
			
			boolean truncExpire = Boolean.parseBoolean(product
					.getParameter("TruncExpireDate", "true"));
			expirationDate = SubscriberProductImpl.calculateExpirationDate(expirationDate, subscriptionUnit,
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
					+ "       , supplierStatus = ?, expirationDate = ?, graceDate = ?, lastrundate = sysdate, status = 0, reservation = null "
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
}
