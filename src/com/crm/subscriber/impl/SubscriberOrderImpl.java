package com.crm.subscriber.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.crm.kernel.domain.DomainFactory;
import com.crm.kernel.message.Constants;
import com.crm.subscriber.bean.SubscriberOrder;
import com.crm.kernel.sql.Database;
import com.crm.util.DateUtil;

import com.fss.util.AppException;

public class SubscriberOrderImpl {
	/**
	 * TODO performance Test
	 * 
	 */
	// private static long sleepTime = 1000L;

	static Logger log = Logger.getLogger(SubscriberOrderImpl.class);

	public static SubscriberOrder getOrder(ResultSet rsOrder) throws Exception {
		SubscriberOrder result = new SubscriberOrder();

		try {
			result.setUserId(rsOrder.getLong("userId"));
			result.setUserName(Database.getString(rsOrder, "userName"));

			result.setSubscriberId(rsOrder.getLong("subscriberId"));
			result.setSubProductId(rsOrder.getLong("subProductId"));
			result.setProductId(rsOrder.getLong("productId"));

			result.setSubscriberType(rsOrder.getInt("subscriberType"));
			result.setIsdn(Database.getString(rsOrder, "isdn"));
			result.setShipTo(Database.getString(rsOrder, "shippingTo"));

			result.setOrderDate(rsOrder.getTime("orderDate"));
			result.setOrderId(rsOrder.getLong("orderId"));
			result.setOrderNo(rsOrder.getString("orderNo"));
			result.setCycleDate(rsOrder.getTimestamp("cycleDate"));

			result.setStatus(rsOrder.getInt("status"));
		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	public static SubscriberOrder getOrder(Connection connection, long orderId,
			Date orderDate) throws Exception {
		PreparedStatement stmtOrder = null;
		ResultSet rsOrder = null;

		SubscriberOrder result = null;

		try {
			String SQL = "Select * From SubscriberOrder Where orderId = ? and orderDate >= trunc(?) and orderDate < (trunc(?) + 1) ";

			stmtOrder = connection.prepareStatement(SQL);
			stmtOrder.setLong(1, orderId);
			stmtOrder.setTimestamp(2, DateUtil.getTimestampSQL(orderDate));
			stmtOrder.setTimestamp(3, DateUtil.getTimestampSQL(orderDate));

			rsOrder = stmtOrder.executeQuery();

			if (rsOrder.next()) {
				result = getOrder(rsOrder);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(rsOrder);
			Database.closeObject(stmtOrder);
		}

		return result;
	}

	public static int getPendingOrder(Connection connection, String isdn,
			long productId, Date orderDate, int duplicateScan) throws Exception {
		// SubscriberOrder order = null;

		PreparedStatement stmtOrder = null;
		ResultSet rsOrder = null;

		if (duplicateScan <= 0) {
			return 0;
		}
		int count = 0;
		try {
			/**
			 * Change to select count(*)
			 */
			String SQL = "Select count(*) total "
					+ "   From 	SubscriberOrder "
					+ "   Where	isdn = ? and productId = ? and status = ? "
					+ "      	and orderDate >= (sysdate - ? / 86400) and orderDate <= sysdate ";

			stmtOrder = connection.prepareStatement(SQL);

			stmtOrder.setString(1, isdn);
			stmtOrder.setLong(2, productId);
			stmtOrder.setInt(3, Constants.ORDER_STATUS_PENDING);

			// stmtOrder.setInt(4, delta);
			stmtOrder.setInt(4, duplicateScan);

			rsOrder = stmtOrder.executeQuery();

			if (rsOrder.next()) {
				// order = getOrder(rsOrder);
				count = rsOrder.getInt("total");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(rsOrder);
			Database.closeObject(stmtOrder);
		}

		return count;
	}

	public static boolean isDuplicatedOrder(Connection connection, String isdn,
			long productId, Date orderDate, int duplicateScan) throws Exception {
		if (duplicateScan <= 0) {
			return false;
		} else {
			int count = getPendingOrder(connection, isdn, productId, orderDate,
					duplicateScan);

			return (count > 1);
		}
	}

	public static boolean isDuplicatedOrder(String isdn, long productId,
			Date orderDate, int duplicateScan) throws Exception {
		Connection connection = null;

		try {
			/**
			 * TODO: performance test
			 */
			// Thread.sleep(sleepTime);
			// return false;

			if (duplicateScan <= 0) {
				return false;
			}

			connection = Database.getConnection();

			return isDuplicatedOrder(connection, isdn, productId, orderDate,
					duplicateScan);
		} finally {
			Database.closeObject(connection);
		}
	}

	public static int getRegisteredOrder(Connection connection, String isdn,
			long productId, Date orderDate) throws Exception {
		PreparedStatement stmtOrder = null;
		ResultSet rsOrder = null;

		int total = 0;

		try {
			String SQL = "Select count(*) total "
					+ "   From 	SubscriberOrder "
					+ "   Where	isdn = ? and productId = ? and status = ? and orderType in (?, ?, ?, ?) "
					+ "      	and orderDate >= trunc(?) and orderDate < (trunc(?) + 1) ";

			// String SQL = "Select count(*) total "
			// + "   From 	SubscriberOrder "
			// +
			// "   Where	isdn = ? and productId = ? and status = ? and orderType in (?, ?, ?) "
			// +
			// "      	and orderDate >= trunc(to_date(?, 'DD/MM/SYYYY HH24:MI:SS')) "
			// +
			// " 		and orderDate < (trunc(to_date(?, 'DD/MM/SYYYY HH24:MI:SS')) + 1) ";

			stmtOrder = connection.prepareStatement(SQL);

			// SimpleDateFormat sdf = new
			// SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			stmtOrder.setString(1, isdn);
			stmtOrder.setLong(2, productId);
			stmtOrder.setInt(3, Constants.ORDER_STATUS_PENDING);
			stmtOrder.setString(4, Constants.ACTION_REGISTER);
			stmtOrder.setString(5, Constants.ACTION_TOPUP);
			// stmtOrder.setString(6, Constants.ACTION_UPGRADE);
			stmtOrder.setString(6, Constants.ACTION_FREE);
			stmtOrder.setString(7, Constants.ACTION_SUPPLIER_REACTIVE);
			// stmtOrder.setString(7, sdf.format(orderDate));
			// stmtOrder.setString(8, sdf.format(orderDate));
			stmtOrder.setTimestamp(8, DateUtil.getTimestampSQL(orderDate));
			stmtOrder.setTimestamp(9, DateUtil.getTimestampSQL(orderDate));

			rsOrder = stmtOrder.executeQuery();

			if (rsOrder.next()) {
				total = rsOrder.getInt("total");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(rsOrder);
			Database.closeObject(stmtOrder);
		}

		return total;
	}

	public static int getRegisteredOrder(String isdn, long productId,
			Date orderDate) throws Exception {
		Connection connection = null;

		try {
			/**
			 * TODO: For performance test
			 */
			// Thread.sleep(sleepTime);
			// return 0;
			connection = Database.getConnection();

			return getRegisteredOrder(connection, isdn, productId, orderDate);
		} finally {
			Database.closeObject(connection);
		}
	}

	public static SubscriberOrder createOrder(Connection connection,
			long userId, String userName, Date orderDate, String orderType,
			long subscriberId, String isdn, int subscriberType,
			long subProductId, long productId, double price, int quantity,
			double discount, double amount, double score, String cause,
			int status, String channel, long orderNo, long merchantId,
			long agentId) throws Exception {
		SubscriberOrder order = null;

		PreparedStatement stmtOrder = null;

		try {
			Date now = new Date();
			Date cycleDate = SubscriberEntryImpl.getCycleDate(now);

			long orderId = Database.getSequence(connection, "order_seq");

			String SQL = "Insert into SubscriberOrder "
					+ "		(orderId, userId, userName, createDate, modifiedDate, orderType, orderDate, cycleDate "
					+ "		, subscriberId, subProductId, productId, isdn, subscriberType "
					+ "		, offerPrice, price, quantity, discount, amount, score, status, cause, channel, orderno, merchantId, agentId) "
					+ " Values " + "		(?, ?, ?, sysDate, sysDate, ?, ?, ? "
					+ "		, ?, ?, ?, ?, ? "
					+ "		, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

			stmtOrder = connection.prepareStatement(SQL);

			stmtOrder.setLong(1, orderId);
			stmtOrder.setLong(2, userId);
			stmtOrder.setString(3, userName);

			stmtOrder.setString(4, orderType);
			stmtOrder.setTime(5, DateUtil.getTimeSQL(orderDate));
			stmtOrder.setTimestamp(6, DateUtil.getTimestampSQL(cycleDate));

			stmtOrder.setLong(7, subscriberId);
			stmtOrder.setLong(8, subProductId);
			stmtOrder.setLong(9, productId);
			stmtOrder.setString(10, isdn);
			stmtOrder.setInt(11, subscriberType);

			stmtOrder.setDouble(12, 0);
			stmtOrder.setDouble(13, price);
			stmtOrder.setDouble(14, quantity);
			stmtOrder.setDouble(15, discount);
			stmtOrder.setDouble(16, amount);
			stmtOrder.setDouble(17, score);

			stmtOrder.setInt(18, status);
			stmtOrder.setString(19, cause);
			stmtOrder.setString(20, channel);
			stmtOrder.setLong(21, orderNo);
			stmtOrder.setLong(22, merchantId);
			stmtOrder.setLong(23, agentId);

			stmtOrder.execute();

			// bind order
			order = new SubscriberOrder();

			order.setUserId(userId);
			order.setUserName(userName);

			order.setSubscriberId(subscriberId);
			order.setSubProductId(subProductId);
			order.setProductId(productId);

			order.setSubscriberType(subscriberType);
			order.setIsdn(isdn);
			// order.setShipTo(shipTo);

			order.setOrderDate(now);
			order.setOrderId(orderId);
			// order.setOrderNo(orderNo);
			order.setCycleDate(cycleDate);

			order.setStatus(status);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			Database.closeObject(stmtOrder);
		}

		return order;
	}

	public static SubscriberOrder createOrder(long userId, String userName,
			Date orderDate, String orderType, long subscriberId, String isdn,
			int subscriberType, long subProductId, long productId,
			double price, int quantity, double discount, double amount,
			double score, String cause, int status, long orderNo,
			long merchantId, long agentId) throws Exception {
		return createOrder(userId, userName, orderDate, orderType,
				subscriberId, isdn, subscriberType, subProductId, productId,
				price, quantity, discount, amount, score, cause, status, "",
				orderNo, merchantId, agentId);

	}

	public static SubscriberOrder createOrder(long userId, String userName,
			Date orderDate, String orderType, long subscriberId, String isdn,
			int subscriberType, long subProductId, long productId,
			double price, int quantity, double discount, double amount,
			double score, String cause, int status, String channel,
			long orderNo, long merchantId, long agentId) throws Exception {
		Connection connection = null;

		SubscriberOrder order = null;

		long start = 0;
		long costGet = 0;
		long costExec = 0;
		int numConn = 0;
		int numBusyConn = 0;
		int numIdleConn = 0;

		try {
			/**
			 * TODO: performance test
			 */

			// Thread.sleep(sleepTime);
			// SubscriberOrder order = new SubscriberOrder();
			// order.setOrderId(Constants.DEFAULT_ID);
			// order.setOrderDate(new Date());
			// return order;

			start = System.currentTimeMillis();

			numConn = Database.getNumberConnection();
			numBusyConn = Database.getNumberBusyConnection();
			numIdleConn = Database.getNumberIdleConnection();

			connection = Database.getConnection();

			costGet = System.currentTimeMillis() - start;

			start = System.currentTimeMillis();

			order = createOrder(connection, userId, userName, orderDate,
					orderType, subscriberId, isdn, subscriberType,
					subProductId, productId, price, quantity, discount, amount,
					score, cause, status, channel, orderNo, merchantId, agentId);

			costExec = System.currentTimeMillis() - start;
		} finally {
			Database.closeObject(connection);
		}

		// log.setLevel(Level.DEBUG);
		// log.debug("Isdn-" + isdn + " Create get connection pool: " + costGet
		// + (costGet > 200? " high":" low")
		// + " - conn: " + numConn
		// + " - busy: " + numBusyConn
		// + " - idle: " + numIdleConn
		// + "   Execute: " + costExec + (costExec > 200? " high.":" low."));

		return order;
	}

	public static void updateOrder(long orderId, Date orderDate,
			String orderType, long subscriberId, int subscriberType,
			long subProductId, long productId, double price, int quantity,
			double discount, double amount, double score, String cause,
			int status, String channel) throws Exception {
		Connection connection = null;

		long start = 0;

		try {
			start = System.currentTimeMillis();

			connection = Database.getConnection();

			start = System.currentTimeMillis();

			updateOrder(connection, orderId, orderDate, orderType,
					subscriberId, subscriberType, subProductId, productId,
					price, quantity, discount, amount, score, cause, status,
					channel);
		} finally {
			Database.closeObject(connection);
		}

		// log.setLevel(Level.DEBUG);
		// log.debug("Isdn-" + isdn + " Update get connection pool: " + costGet
		// + (costGet > 200? " high":" low")
		// + " - conn: " + numConn
		// + " - busy: " + numBusyConn
		// + " - idle: " + numIdleConn
		// + "   Execute: " + costExec + (costExec > 200? " high.":" low."));
	}

	public static void updateOrder(Connection connection, long orderId,
			Date orderDate, String orderType, long subscriberId,
			int subscriberType, long subProductId, long productId,
			double price, int quantity, double discount, double amount,
			double score, String cause, int status, String channel)
			throws Exception {
		PreparedStatement stmtOrder = null;

		try {
			Date now = new Date();

			String SQL = "Update SubscriberOrder Set modifiedDate = sysdate, "
					+ "orderType = ?, subscriberId = ?, subProductId = ?, productId = ?, "
					+ "subscriberType = ?, price = ?, quantity = ?, "
					+ "discount = ?, amount = ?, score = ?, status = ?, "
					+ "cause = ?, channel = ? "
					+ "Where orderDate >= trunc(?) and orderDate < (trunc(?) + 1) and orderId = ? ";

			stmtOrder = connection.prepareStatement(SQL);

			stmtOrder.setString(1, orderType);
			stmtOrder.setLong(2, subscriberId);
			stmtOrder.setLong(3, subProductId);
			stmtOrder.setLong(4, productId);
			stmtOrder.setInt(5, subscriberType);
			stmtOrder.setDouble(6, price);
			stmtOrder.setDouble(7, quantity);
			stmtOrder.setDouble(8, discount);
			stmtOrder.setDouble(9, amount);
			stmtOrder.setDouble(10, score);
			stmtOrder.setInt(11, status);
			stmtOrder.setString(12, cause);
			stmtOrder.setString(13, channel);
			stmtOrder.setTimestamp(14, DateUtil.getTimestampSQL(orderDate));
			stmtOrder.setTimestamp(15, DateUtil.getTimestampSQL(orderDate));
			stmtOrder.setLong(16, orderId);

			stmtOrder.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(stmtOrder);
		}
	}

	public static void updateOrder(Connection connection, long orderId,
			Date orderDate, long subscriberId, long subProductId, double price,
			int quantity, double discount, double amount, double score,
			int status, String cause, String description) throws Exception {
		PreparedStatement stmtOrder = null;

		try {
			String sql = "Update SubscriberOrder "
					+ "   Set 	modifiedDate = sysDate, subscriberId = ?, subProductId = ? "
					+ "   		, price = ?, quantity = ?, discount = ?, amount = ?, score = ? "
					+ "   		, status = ?, cause = ?, description = ? "
					+ "Where orderDate >= trunc(?) and orderDate < (trunc(?) + 1) and orderId = ? ";

			stmtOrder = connection.prepareStatement(sql);

			stmtOrder.setLong(1, subscriberId);
			stmtOrder.setLong(2, subProductId);

			stmtOrder.setDouble(3, price);
			stmtOrder.setInt(4, quantity);
			stmtOrder.setDouble(5, discount);
			stmtOrder.setDouble(6, amount);
			stmtOrder.setDouble(7, score);

			stmtOrder.setInt(8, status);
			stmtOrder.setString(9, cause);
			stmtOrder.setString(10, description);

			stmtOrder.setTimestamp(11, DateUtil.getTimestampSQL(orderDate));
			stmtOrder.setTimestamp(12, DateUtil.getTimestampSQL(orderDate));
			stmtOrder.setLong(13, orderId);

			stmtOrder.execute();

			if (stmtOrder.getUpdateCount() == 0) {
				throw new AppException(Constants.ERROR_ORDER_NOT_FOUND);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(stmtOrder);
		}
	}

	public static void updateStatus(Connection connection, long orderId,
			Date orderDate, int status, String cause, String description,
			long subProductId, long campaignId, String orderType,
			long subscriberId, int subscriberType, long productId,
			double price, int quantity, double discount, double amount,
			double score, String channel) throws Exception {
		PreparedStatement stmtOrder = null;

		try {
			String sql = "Update SubscriberOrder "
					+ "Set modifiedDate = sysDate, status = ?, cause = ?, "
					+ "description = ?, subproductid = ?, campaignId = ?, "
					+ "orderType = ?, subscriberId = ?, productId = ?, "
					+ "subscriberType = ?, price = ?, quantity = ?, "
					+ "discount = ?, amount = ?, score = ?, channel = ? "
					+ "Where orderDate >= trunc(?) and orderDate < (trunc(?) + 1) and orderId = ? ";

			stmtOrder = connection.prepareStatement(sql);

			stmtOrder.setInt(1, status);
			stmtOrder.setString(2, cause);
			stmtOrder.setString(3, description);
			stmtOrder.setLong(4, subProductId);
			stmtOrder.setLong(5, campaignId);
			stmtOrder.setString(6, orderType);
			stmtOrder.setLong(7, subscriberId);
			stmtOrder.setLong(8, productId);
			stmtOrder.setInt(9, subscriberType);
			stmtOrder.setDouble(10, price);
			stmtOrder.setDouble(11, quantity);
			stmtOrder.setDouble(12, discount);
			stmtOrder.setDouble(13, amount);
			stmtOrder.setDouble(14, score);
			stmtOrder.setString(15, channel);
			stmtOrder.setTimestamp(16, DateUtil.getTimestampSQL(orderDate));
			stmtOrder.setTimestamp(17, DateUtil.getTimestampSQL(orderDate));
			stmtOrder.setLong(18, orderId);

			stmtOrder.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(stmtOrder);
		}
	}

	public static void updateStatus(long orderId, Date orderDate, int status,
			String cause, String description, long subProductId,
			long campaignId, String orderType, long subscriberId,
			int subscriberType, long productId, double price, int quantity,
			double discount, double amount, double score, String channel)
			throws Exception {
		Connection connection = null;

		try {
			/**
			 * TODO: performance test
			 */

			// Thread.sleep(sleepTime);

			connection = Database.getConnection();

			String respCode = "";
			String errorCode = "";
			if (cause != null && description != null
					&& cause.equals(Constants.ERROR) && !description.equals("")) {
				respCode = "RESP." + description;
				errorCode = DomainFactory.getCache().getDomain("RESPONSE_CODE",
						respCode);
			}

			if (errorCode.equals("")) {
				respCode = "RESP." + cause;
				errorCode = DomainFactory.getCache().getDomain("RESPONSE_CODE",
						respCode);
			} else {
				description = "";
			}

			if (errorCode.equals("")) {
				errorCode = cause;
			}

			updateStatus(connection, orderId, orderDate, status, errorCode,
					description, subProductId, campaignId, orderType,
					subscriberId, subscriberType, productId, price, quantity,
					discount, amount, score, channel);
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(connection);
		}
	}
}
