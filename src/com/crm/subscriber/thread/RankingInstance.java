package com.crm.subscriber.thread;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.jms.Message;

import com.crm.loyalty.cache.RankEntry;
import com.crm.loyalty.cache.RankFactory;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.subscriber.message.SubscriberMessage;
import com.crm.thread.DatasourceInstance;
import com.crm.thread.util.ThreadUtil;
import com.crm.util.DateUtil;

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

public class RankingInstance extends DatasourceInstance
{
	protected PreparedStatement	stmtUsage			= null;
	protected PreparedStatement	stmtInsertRank		= null;

	protected PreparedStatement	stmtBalance			= null;
	protected PreparedStatement	stmtClearBalance	= null;
	protected PreparedStatement	stmtCloseCycle		= null;
	protected PreparedStatement	stmtSubscriber		= null;
	protected PreparedStatement	stmtAudit			= null;

	protected PreparedStatement	stmtCommunication	= null;

	protected ResultSet			rsUsage				= null;

	protected String			channel				= "SMS";
	protected String			serviceAddress		= "";
	protected String			keyword				= "";
	protected String			startTime			= "";
	protected String			endTime				= "";
	protected String			informTemplate		= "";
	protected String			greetingTemplate	= "";

	protected Date				cycleDate			= null;
	protected Date				nextCycleDate		= null;
	protected boolean			closingCycle		= false;

	protected String			balanceType			= "LOYALTY";

	public RankingInstance() throws Exception
	{
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	protected void applyNextCycle(SubscriberMessage request) throws Exception
	{
		stmtInsertRank.setDate(1, DateUtil.getDateSQL(request.getNextCycleDate()));
		stmtInsertRank.setString(2, request.getIsdn());
		stmtInsertRank.setString(3, balanceType);

		stmtInsertRank.setDouble(4, request.getAmount());
		stmtInsertRank.setDouble(5, request.getAmount());

		stmtInsertRank.setDouble(6, request.getRankId());
		stmtInsertRank.setDate(7, DateUtil.getDateSQL(request.getRankStartDate()));
		stmtInsertRank.setDate(8, DateUtil.getDateSQL(request.getRankExpirationDate()));

		stmtInsertRank.setDate(9, DateUtil.getDateSQL(request.getNextCycleDate()));
		stmtInsertRank.setLong(10, request.getSubscriberId());
		stmtInsertRank.setString(11, request.getIsdn());
		stmtInsertRank.setInt(12, request.getSubscriberType());

		stmtInsertRank.setString(13, balanceType);
		stmtInsertRank.setDouble(14, request.getAmount());
		stmtInsertRank.setDouble(15, 0);
		stmtInsertRank.setDouble(16, request.getAmount());

		stmtInsertRank.setDouble(17, request.getRankId());
		stmtInsertRank.setDate(18, DateUtil.getDateSQL(request.getRankStartDate()));
		stmtInsertRank.setDate(19, DateUtil.getDateSQL(request.getRankExpirationDate()));
		stmtInsertRank.setInt(20, Constants.ORDER_STATUS_PENDING);

		stmtInsertRank.execute();
	}

	protected String getLongValue(double number)
	{
		return String.valueOf(new Double(number).longValue());
	}

	// //////////////////////////////////////////////////////
	// process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public int processMessage(Message message) throws Exception
	{
		SubscriberMessage request = (SubscriberMessage) message;

		ResultSet rsBalance = null;

		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

		boolean hasChange = true;

		try
		{
			Date now = DateUtil.trunc(new Date());

			long subscriberId = request.getSubscriberId();
			long rankId = request.getRankId();
			Date rankStartDate = request.getRankStartDate();
			Date rankExpirationDate = request.getRankExpirationDate();

			RankEntry oldRank = RankFactory.getCache().getRank(request.getRankId());

			if ((oldRank == null) && (rankExpirationDate != null))
			{
				throw new AppException("rank-not-found");
			}

			// get total score of current cycle
			stmtUsage.setLong(1, subscriberId);
			stmtUsage.setString(2, request.getIsdn());
			stmtUsage.setDate(3, DateUtil.getDateSQL(cycleDate));

			rsUsage = stmtUsage.executeQuery();

			if (!rsUsage.next())
			{
				request.setCause("usage-not-found");
				
				return Constants.BIND_ACTION_BYPASS;
			}
			else if (rsUsage.getInt("status") == Constants.ORDER_STATUS_EXPIRED)
			{
				return Constants.BIND_ACTION_BYPASS;
			}
			else
			{
				request.setAmount(rsUsage.getDouble("totalAmount"));
			}

			// get cycle date

			String informContent = informTemplate;

			RankEntry newRank = RankFactory.getCache().ranking(request.getAmount());

			if (newRank == null)
			{
				throw new AppException("rank-not-found");
			}

			boolean upgrade = ((oldRank != null) && (oldRank.getPriority() < newRank.getPriority()));

			if ((rankExpirationDate == null) || rankExpirationDate.before(now) || upgrade)
			{
				rankId = newRank.getRankId();

				rankStartDate = DateUtil.trunc();
				rankExpirationDate = DateUtil.addDate(rankStartDate, newRank.getRankUnit(), newRank.getRankPeriod());

				request.setRankId(rankId);
				request.setRankStartDate(rankStartDate);
				request.setRankExpirationDate(rankExpirationDate);

				informContent = upgrade ? greetingTemplate : informTemplate;
			}
			else
			{
				hasChange = false;
			}

			// clear balance if expire
			stmtBalance.setLong(1, subscriberId);
			stmtBalance.setString(2, request.getIsdn());
			stmtBalance.setString(3, balanceType);

			rsBalance = stmtBalance.executeQuery();

			if (rsBalance.next())
			{
				request.setBalanceAmount(rsBalance.getDouble("balanceAmount"));

				Date balanceExpirationDate = rsBalance.getDate("expirationDate");

				if ((balanceExpirationDate != null) && balanceExpirationDate.before(now))
				{
					// clear balance
					stmtAudit.setLong(1, subscriberId);
					stmtAudit.setString(2, request.getIsdn());
					stmtAudit.setString(3, "reset-balance");
					stmtAudit.setString(4, "reset balance from " + request.getBalanceAmount() + " to 0");
					stmtAudit.execute();
				}
			}

			if (closingCycle)
			{
				applyNextCycle(request);

				stmtCloseCycle.setInt(1, Constants.ORDER_STATUS_EXPIRED);
				stmtCloseCycle.setLong(2, rankId);
				stmtCloseCycle.setDate(3, DateUtil.getDateSQL(rankStartDate));
				stmtCloseCycle.setDate(4, DateUtil.getDateSQL(rankExpirationDate));
				stmtCloseCycle.setDate(5, DateUtil.getDateSQL(cycleDate));
				stmtCloseCycle.setString(6, request.getIsdn());
				stmtCloseCycle.setString(7, balanceType);
				stmtCloseCycle.execute();
			}

			if (hasChange)
			{
				stmtSubscriber.setLong(1, rankId);
				stmtSubscriber.setDate(2, DateUtil.getDateSQL(rankStartDate));
				stmtSubscriber.setDate(3, DateUtil.getDateSQL(rankExpirationDate));
				stmtSubscriber.setLong(4, subscriberId);
				stmtSubscriber.execute();

				stmtAudit.setLong(1, subscriberId);
				stmtAudit.setString(2, request.getIsdn());
				stmtAudit.setString(3, "change-rank");
				
				if (oldRank != null)
				{
					stmtAudit.setString(4, "change rank from " + oldRank.getTitle() + " to " + newRank.getTitle());
				}
				else
				{
					stmtAudit.setString(4, "change rank to " + newRank.getTitle());
				}

				stmtAudit.execute();
			}

			// build notification content
			if (closingCycle || upgrade)
			{
				informContent = informContent.replaceAll("<currentAmount>", getLongValue(request.getAmount()));
				informContent = informContent.replaceAll("<balanceAmount>", getLongValue(request.getBalanceAmount()));
				informContent = informContent.replaceAll("<nextCycle>", formatDate.format(cycleDate).substring(3));
				informContent = informContent.replaceAll("<rank>", newRank.getTitle());
				informContent = informContent.replaceAll("<fromDate>", formatDate.format(rankStartDate));
				informContent = informContent.replaceAll("<toDate>", formatDate.format(rankExpirationDate));

				stmtCommunication.setString(1, channel);
				stmtCommunication.setLong(2, subscriberId);
				stmtCommunication.setString(3, request.getIsdn());
				stmtCommunication.setString(4, serviceAddress);
				stmtCommunication.setString(5, keyword);
				stmtCommunication.setString(6, informContent);
				stmtCommunication.setString(7, startTime);
				stmtCommunication.setString(8, endTime);
				stmtCommunication.setInt(9, Constants.ORDER_STATUS_PENDING);

				stmtCommunication.execute();
			}
		}
		catch (AppException e)
		{
			request.setStatus(Constants.ORDER_STATUS_PENDING);
			request.setCause("rank-not-found");

			return Constants.BIND_ACTION_ERROR;
		}
		catch (Exception e)
		{
			request.setStatus(Constants.ORDER_STATUS_PENDING);
			
			throw e;
		}
		finally
		{
			debugMonitor("End of ranking process for subscriber " + request.getIsdn() + " : " + request.getCause());
		}

		return Constants.BIND_ACTION_SUCCESS;
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void beforeProcessSession() throws Exception
	{
		try
		{
			super.beforeProcessSession();

			if ((mcnMain == null) || mcnMain.isClosed())
			{
				mcnMain = Database.getConnection();
			}

			// insert next date for this campaign
			String SQL = "";

			SQL = "Merge into SubscriberRank using dual on (cycleDate = ? and isdn = ? and balanceType = ? ) "
					+ "	When Matched then Update Set  "
					+ "		modifiedDate = sysDate, priorAmount = ?, totalAmount = nvl(currentAmount, 0) + ? "
					+ "		, rankId = ?, startDate = ?, expirationDate = ? "
					+ "When Not Matched then Insert "
					+ "		(subRankId, userId, userName, createDate, modifiedDate, cycleDate "
					+ "		, subscriberId, isdn, subscriberType "
					+ "		, balanceType, priorAmount, currentAmount, adjustAmount, totalAmount "
					+ "		, rankId, startDate, expirationDate, status ) "
					+ "Values "
					+ "		(sub_rank_seq.nextval, 0, 'system', sysDate, sysDate, ? "
					+ "		, ?, ?, ? "
					+ "		, ?, ?, ?, 0, ? "
					+ "		, ?, ?, ?, ? ) ";

			stmtInsertRank = mcnMain.prepareStatement(SQL);

			// Balance
			SQL = "Select * From SubscriberBalance Where subscriberId = ? and isdn = ? and balanceType = ?";

			stmtBalance = mcnMain.prepareStatement(SQL);

			SQL = "Update SubscriberRank "
					+ "Set modifiedDate = sysDate, status = ?, rankId = ?, startDate = ?, expirationDate = ? "
					+ "Where cycleDate = ? and isdn = ? and balanceType = ? ";

			stmtCloseCycle = mcnMain.prepareStatement(SQL);

			SQL = "Update SubscriberBalance "
					+ "Set modifiedDate = sysDate, status = ?, balanceAmount = ?, startDate = ?, expirationDate = ? "
					+ "Where balanceId = ? ";

			stmtClearBalance = mcnMain.prepareStatement(SQL);

			SQL = "Select * From SubscriberRank Where subscriberId = ? and isdn = ? and cycleDate = ?";

			stmtUsage = mcnMain.prepareStatement(SQL);

			SQL = "Update SubscriberEntry "
					+ "Set modifiedDate = sysDate, rankId = ?, rankStartDate = ?, rankExpirationDate = ? "
					+ "Where subscriberId = ? ";

			stmtSubscriber = mcnMain.prepareStatement(SQL);

			SQL = "Insert into SubscriberAudit "
					+ "		(auditId, userId, userName, createDate, modifiedDate "
					+ "		, subscriberId, isdn, actionType, description) "
					+ "Values "
					+ "		(batch_seq.nextval, 0, 'system', sysDate, sysDate "
					+ "		, ?, ?, ?, ?) ";

			stmtAudit = mcnMain.prepareStatement(SQL);

			SQL = "Insert into BatchCommand "
					+ "		(batchId, userId, userName, createDate, modifiedDate "
					+ "		, channel, subscriberId, isdn, serviceAddress, keyword "
					+ "		, objRequest, startTime, endTime, status) "
					+ "Values "
					+ "		(batch_seq.nextval, 0, 'system', sysDate, sysDate "
					+ "		, ?, ?, ?, ?, ? "
					+ "		, ?, ?, ?, ?) ";

			stmtCommunication = mcnMain.prepareStatement(SQL);

			channel = ThreadUtil.getString(getDispatcher(), "communication.channel", true, "SMS");
			serviceAddress = ThreadUtil.getString(getDispatcher(), "communication.serviceAddress", true, "123");
			keyword = ThreadUtil.getString(getDispatcher(), "communication.keyword", true, "SEND_SMS");
			startTime = ThreadUtil.getString(getDispatcher(), "communication.startTime", true, "07:00:00");
			endTime = ThreadUtil.getString(getDispatcher(), "communication.endTime", true, "20:00:00");
			informTemplate = ThreadUtil.getString(getDispatcher(), "communication.informTemplate", true, "");
			greetingTemplate = ThreadUtil.getString(getDispatcher(), "communication.greetingTemplate", true, "");

			// get current & next cycle
			// cycleDate = getThreadConfig().getParseDate().parse("2011/12/01");

			Calendar calendar = Calendar.getInstance();

			calendar.setTime(cycleDate);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.MONTH, 1);

			nextCycleDate = calendar.getTime();
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void afterProcessSession() throws Exception
	{
		try
		{
			Database.closeObject(rsUsage);
			Database.closeObject(stmtUsage);
			Database.closeObject(stmtInsertRank);
			Database.closeObject(stmtCommunication);
			Database.closeObject(stmtCloseCycle);
			Database.closeObject(stmtSubscriber);
		}
		finally
		{
			super.afterProcessSession();
		}
	}
}
