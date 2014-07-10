/**
 * 
 */
package com.crm.spam.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import com.crm.kernel.sql.Database;
import com.crm.spam.bean.SpamEntry;
import com.crm.spam.bean.SpamLog;
import com.crm.util.DateUtil;

/**
 * @author Do Tien Hung
 * 
 */
public class SpamImpl {

	public static SpamEntry getSpamEntry(ResultSet rsSpam) throws Exception {
		SpamEntry spam = new SpamEntry();
		try {
			spam.setCommandId(rsSpam.getString("COMMANDID"));
			spam.setCompanyId(rsSpam.getLong("COMPANYID"));
			spam.setCreateDate(rsSpam.getDate("CREATEDATE"));
			spam.setDescription(rsSpam.getString("DESCRIPTION"));
			spam.setEntryId(rsSpam.getLong("ENTRYID"));
			spam.setGroupId(rsSpam.getLong("GroupId"));
			spam.setModifiedDate(rsSpam.getDate("ModifiedDate"));
			spam.setProductId(rsSpam.getString("ProductId"));
			spam.setProperties(rsSpam.getString("Properties"));
			spam.setResourcePrimKey(rsSpam.getLong("ResourcePrimKey"));
			spam.setSpamId(rsSpam.getString("SpamId"));
			spam.setSpamInterval(rsSpam.getInt("SpamInterval"));
			spam.setSpamTimes(rsSpam.getInt("SpamTimes"));
			spam.setSuspendTime(rsSpam.getInt("SuspendTime"));
			spam.setTitle(rsSpam.getString("Title"));
			spam.setUserId(rsSpam.getLong("UserId"));
			spam.setUserName(rsSpam.getString("UserName"));
		} catch (Exception e) {
			throw e;
		}
		return spam;
	}

	public static SpamLog getSpamLog(ResultSet rsSpam) throws Exception {
		SpamLog spam = new SpamLog();
		try {
			spam.setLogId(rsSpam.getLong("LogId"));
			spam.setCreateDate(rsSpam.getTimestamp("CreateDate"));
			spam.setExpireDate(rsSpam.getTimestamp("ExpireDate"));
			spam.setModifiedDate(rsSpam.getTimestamp("ModifiedDate"));
			spam.setSpamTimes(rsSpam.getInt("SpamTimes"));
			spam.setSpamAddress(rsSpam.getString("SpamAddress"));
			spam.setSpamDate(rsSpam.getTimestamp("SpamDate"));
			spam.setSpamType(rsSpam.getString("SpamType"));
		} catch (Exception e) {
			throw e;
		}
		return spam;
	}

	public static int addSpam(String productId, String commandId,
			String spamAddress, String spamType, String description,
			long userId, String username, long groupid, long merchantid,
			long companyid, Connection connection) throws Exception {
		int result = 0;
		try {
			SpamLog spamLog = getSpamLog(connection, merchantid, commandId,
					productId);
			SpamEntry spamEntry = getSpamEntry(connection, productId, commandId);
			if (spamLog == null) {

				createSpamLog(productId, commandId, spamAddress, spamType,
						description, userId, username, groupid, merchantid,
						companyid, spamEntry.getSpamInterval(), connection);
				result = 1;
			} else {
				Calendar now = Calendar.getInstance();
				int spamtimes = spamLog.getSpamTimes();
				if (spamtimes < spamEntry.getSpamTimes()) {
					if (now.getTimeInMillis() < spamLog.getExpireDate()
							.getTime()) {
						updateSpamLog(spamLog.getLogId(), spamtimes + 1,
								connection, productId, commandId);
					} else if (now.getTimeInMillis() > spamLog.getExpireDate()
							.getTime()) {
						updateSpamLog(spamLog.getLogId(), 1, connection,
								productId, commandId);
					}
					if (spamtimes + 1 < spamEntry.getSpamTimes()) {
						result = 2;
					} else if (spamtimes + 1 >= spamEntry.getSpamTimes()) {
						result = 3;
					}
				} else {
					result = 3;
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	public static boolean isSpam(String productId, String commandId,
			String spamAddress, String spamType, Connection connection)
			throws Exception {

		try {
			PreparedStatement stmtSpam = null;
			SpamLog spam = null;
			ResultSet rsSpam = null;
			String sql = "select * from spamlog where spamAddress = ? and productId = ? and commandId = ? and spamtype = ? and createdate > trunc(sysdate)";

			stmtSpam = connection.prepareStatement(sql);
			stmtSpam.setString(1, spamAddress);
			stmtSpam.setString(2, productId);
			stmtSpam.setString(3, commandId);
			stmtSpam.setString(4, spamType);

			rsSpam = stmtSpam.executeQuery();
			if (rsSpam.next()) {
				spam = getSpamLog(rsSpam);
			}
			SpamEntry spamEntry = getSpamEntry(connection, productId, commandId);
			if (spam != null) {
				if (spam.getSpamTimes() >= spamEntry.getSpamTimes()) {
					Calendar now = Calendar.getInstance();
					if (now.getTimeInMillis() > spam.getExpireDate().getTime()) {
						updateSpamLog(spam.getLogId(), 0, connection,
								productId, commandId);
					} else {
						return true;
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return false;
	}

	public static SpamLog deleteSpamLog(long logId, Date spamDate,
			Connection connection) {
		SpamLog spamlog = new SpamLog();
		// try {
		// String sql = "delete from spamlog where logid = ?";
		// } catch (Exception e) {
		//
		// }
		return spamlog;
	}

	/**
	 * @author Do Tien Hung
	 * @param java
	 *            .sql.Connection connection
	 * @param String
	 *            productId
	 * @param String
	 *            commandId
	 * @return info spamentry of product
	 * @throws Exception
	 */
	public static SpamEntry getSpamEntry(Connection connection,
			String productId, String commandId) throws Exception {
		SpamEntry spam = null;
		PreparedStatement stmtSpam = null;
		ResultSet rsSpam = null;
		try {
			String sql = "select * from spamentry where productid = ? and commandid = ?";

			stmtSpam = connection.prepareStatement(sql);
			stmtSpam.setString(1, productId);
			stmtSpam.setString(2, commandId);

			rsSpam = stmtSpam.executeQuery();
			if (rsSpam.next()) {
				spam = getSpamEntry(rsSpam);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(rsSpam);
			Database.closeObject(stmtSpam);
		}
		return spam;
	}

	/**
	 * @author Do Tien Hung
	 * @param connection
	 * @param spamAddress
	 * @return info spamlog of isdn
	 * @throws Exception
	 */
	public static SpamLog getSpamLog(Connection connection, long merchantId,
			String commandId, String productId) throws Exception {
		SpamLog spam = null;
		PreparedStatement stmtSpam = null;
		ResultSet rsSpam = null;
		try {
			String sql = "select * from spamlog where merchantId = ? and commandid = ? and productid = ? and createdate > trunc(sysdate)";

			stmtSpam = connection.prepareStatement(sql);
			stmtSpam.setLong(1, merchantId);
			stmtSpam.setString(2, commandId);
			stmtSpam.setString(3, productId);

			rsSpam = stmtSpam.executeQuery();
			if (rsSpam.next()) {
				spam = getSpamLog(rsSpam);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(rsSpam);
			Database.closeObject(stmtSpam);
		}
		return spam;
	}

	public static void createSpamLog(String productId, String commandId,
			String spamAddress, String spamType, String description,
			long userId, String username, long groupid, long merchantid,
			long companyid, int SpamInterval, Connection connection)
			throws Exception {

		PreparedStatement stmtSpam = null;
		try {

			String sql = "Insert into spamlog(logid, groupid, companyid, userid, username, createdate, modifieddate, "
					+ "spamdate, spamaddress, spamtype, productid, commandid, spamtimes, expiredate, description, status, merchantid) "
					+ "values (SPAM_LOG_SEQ.nextval,?,?,?,?,sysdate, sysdate, sysdate,?,?,?,?,?,?,?,?,?)";
			Calendar now = Calendar.getInstance();
			long expiredate = now.getTimeInMillis() + SpamInterval * 1000;
			stmtSpam = connection.prepareStatement(sql);

			stmtSpam.setLong(1, groupid);
			stmtSpam.setLong(2, companyid);
			stmtSpam.setLong(3, userId);
			stmtSpam.setString(4, username);
			stmtSpam.setString(5, spamAddress);
			stmtSpam.setString(6, spamType);
			stmtSpam.setString(7, productId);
			stmtSpam.setString(8, commandId);
			stmtSpam.setInt(9, 1);
			stmtSpam.setTimestamp(10,
					DateUtil.getTimestampSQL(new Date(expiredate)));
			stmtSpam.setString(11, description);
			stmtSpam.setInt(12, 0);
			stmtSpam.setLong(13, merchantid);

			stmtSpam.executeUpdate();

		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(stmtSpam);
		}

	}

	public static void updateSpamLog(long logid, int spamtimes,
			Connection connection, String productId, String commandId)
			throws Exception {
		PreparedStatement stmtSpam = null;
		SpamEntry spamEntry = getSpamEntry(connection, productId, commandId);
		try {
			String sql = "update spamlog set modifieddate = sysdate, spamdate = sysdate, spamtimes = ?, expiredate = ? where logid = ?";
			Calendar now = Calendar.getInstance();
			long expiredate = 0;
			if (spamtimes < spamEntry.getSpamTimes()) {
				expiredate = now.getTimeInMillis()
						+ spamEntry.getSpamInterval() * 1000;
			} else {
				expiredate = now.getTimeInMillis() + spamEntry.getSuspendTime()
						* 1000;
			}
			stmtSpam = connection.prepareStatement(sql);
			stmtSpam.setInt(1, spamtimes);
			stmtSpam.setTimestamp(2,
					DateUtil.getTimestampSQL(new java.util.Date(expiredate)));
			stmtSpam.setLong(3, logid);

			stmtSpam.execute();

		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(stmtSpam);
		}
	}

}
