/**
 * 
 */
package com.crm.marketing.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.index.IndexNode;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.product.cache.ProductRoute;
import com.crm.util.DateUtil;
import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class CampaignCache
{
	// cache object
	private BinaryIndex	campaigns	= new BinaryIndex();

	private Date		cacheDate	= null;

	public void clear()
	{
		campaigns.clear();
	}

	public synchronized void loadCache() throws Exception
	{
		Connection connection = null;

		try
		{
			log.debug("Caching campaign ...");

			connection = Database.getConnection();

			loadCampaign(connection);

			setCacheDate(new Date());

			log.debug("Campaign is cached");
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

	protected void loadCampaign(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			String sql = "Select * From CampaignEntry Where Status = 1 Order by alias_ desc";

			stmtConfig = connection.prepareStatement(sql);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				log.debug(String.format("Caching campaign rule for %s ...", rsConfig.getString("alias_")));

				CampaignEntry campaign = new CampaignEntry(rsConfig.getLong("campaignId"), Database.getString(rsConfig, "alias_"));

				campaign.setCampaignType(Database.getString(rsConfig, "campaignType"));
				campaign.setTitle(Database.getString(rsConfig, "title"));

				campaign.setScheduleEnable(rsConfig.getBoolean("scheduleEnable"));
				campaign.setSchedulePeriod(rsConfig.getInt("schedulePeriod"));
				campaign.setScheduleUnit(rsConfig.getString("scheduleUnit"));
				campaign.setStartDate(rsConfig.getDate("startDate"));
				campaign.setExpirationDate(rsConfig.getDate("expirationDate"));
				
				campaign.setProductId(rsConfig.getLong("productId"));
				campaign.setSegmentId(rsConfig.getLong("segmentId"));

				campaign.setParameters(Database.getString(rsConfig, "properties"));

//				campaign.setProcessClass(Database.getString(rsConfig,"processClass"));
				
				campaign.setStatus(rsConfig.getInt("status"));

				campaigns.add(campaign.getCampaignId(), campaign.getIndexKey(), campaign);

				log.debug(String.format("Campaign rule %s is cached", rsConfig.getString("alias_")));
			}
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
		this.cacheDate = cacheDate;
	}

	public Date getCacheDate()
	{
		return cacheDate;
	}

	public BinaryIndex getCampaigns()
	{
		return campaigns;
	}

	public void setCampaigns(BinaryIndex campaignRule)
	{
		this.campaigns = campaignRule;
	}

	public CampaignEntry getCampaign(long campaignId) throws Exception
	{
		if (campaignId == Constants.DEFAULT_ID)
		{
			return null;
		}
		
		IndexNode result = campaigns.getById(campaignId);

		if (result == null)
		{
			throw new AppException(Constants.ERROR_CAMPAIGN_NOT_FOUND);
		}

		return (CampaignEntry) result;
	}

	public CampaignEntry getCampaign(String alias) throws Exception
	{
		if (alias.equals(""))
		{
			return null;
		}
		
		IndexNode result = campaigns.getByKey(alias);

//		if (result == null)
//		{
//			throw new AppException(Constants.ERROR_CAMPAIGN_NOT_FOUND);
//		}

		return (CampaignEntry) result;
	}
	
	public CampaignEntry getCampaign(String alias, String channel) throws Exception
	{
		for (int j = 0; j < campaigns.size(); j++)
		{
			CampaignEntry lookup = (CampaignEntry) campaigns.get(j);

			if (lookup.equals(alias, channel))
			{
				return lookup;
			}
		}

		return null;
	}

	private static Logger	log	= Logger.getLogger(CampaignCache.class);

}
