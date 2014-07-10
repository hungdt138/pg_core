/**
 * 
 */
package com.crm.loyalty.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.log4j.Logger;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.sql.Database;
import com.crm.util.DateUtil;

/**
 * @author ThangPV
 * 
 */
public class RankCache
{
	// cache object
	private BinaryIndex	ranks		= new BinaryIndex();

	private Date		cacheDate	= null;

	public synchronized void loadCache() throws Exception
	{
		Connection connection = null;

		try
		{
			clear();

			log.debug("Caching product ...");

			connection = Database.getConnection();

			loadRank(connection);

			setCacheDate(DateUtil.trunc());

			log.debug("Product is cached");
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
		ranks.clear();
	}

	protected void loadRank(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			log.debug("Loading product rule ...");

			String sql = "Select * From RankEntry Order by alias_ desc";

			stmtConfig = connection.prepareStatement(sql);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				RankEntry rank = new RankEntry(rsConfig.getLong("rankId"), rsConfig.getString("alias_"));

				rank.setRankId(rsConfig.getLong("rankId"));
				rank.setTitle(Database.getString(rsConfig, "title"));
				rank.setPriority(rsConfig.getInt("priority"));
				rank.setSegmentId(rsConfig.getLong("segmentId"));

				rank.setMinScore(rsConfig.getLong("minScore"));
				rank.setMaxScore(rsConfig.getLong("maxScore"));

				rank.setRankPeriod(rsConfig.getInt("rankPeriod"));
				rank.setRankUnit(rsConfig.getString("rankUnit"));
				rank.setScorePeriod(rsConfig.getInt("scorePeriod"));
				rank.setScoreUnit(rsConfig.getString("scoreUnit"));

				ranks.add(rank.getRankId(), rank.getIndexKey(), rank);
			}

			log.debug("Product rule are loaded");
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

	public void setCacheDate(Date cacheDate)
	{
		this.cacheDate = DateUtil.trunc(cacheDate);
	}

	public Date getCacheDate()
	{
		return cacheDate;
	}

	public BinaryIndex getRankRule()
	{
		return ranks;
	}

	public void setRankRule(BinaryIndex rankRule)
	{
		this.ranks = rankRule;
	}

	public RankEntry getRank(long rankId) throws Exception
	{
		return (RankEntry) ranks.getById(rankId);
	}

	public RankEntry getRank(String alias) throws Exception
	{
		return (RankEntry) ranks.getByKey(alias);
	}

	public RankEntry ranking(double score) throws Exception
	{
		for (int j = 0; j < ranks.size(); j++)
		{
			RankEntry lookup = (RankEntry) ranks.get(j);

			if (lookup.equals(score))
			{
				return lookup;
			}
		}

		return null;
	}

	private static Logger	log	= Logger.getLogger(RankCache.class);

}
