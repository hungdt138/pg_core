/**
 * 
 */
package com.crm.kernel.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.log4j.Logger;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.sql.Database;

/**
 * @author ThangPV
 * 
 */
public class DomainCache
{
	// cache object
	private BinaryIndex	domains	= new BinaryIndex();

	private Date		cacheDate		= null;

	public void clear()
	{
		domains.clear();
	}

	public synchronized void loadCache() throws Exception
	{
		Connection connection = null;

		try
		{
			log.debug("Caching domain ...");

			connection = Database.getConnection();

			loadDomain(connection);
			
			setCacheDate(new Date());

			log.debug("Domain is cached");
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

	protected void loadDomain(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			String sql = "Select * From AppDomain A Order by alias_ desc";

			stmtConfig = connection.prepareStatement(sql);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				log.debug(String.format("Caching domain rule for %s ...", rsConfig.getString("alias_")));

				DomainEntry domain = new DomainEntry(rsConfig.getLong("domainId"),rsConfig.getString("alias_"));

				domain.setDomainType(Database.getString(rsConfig, "type_"));
				domain.setTitle(Database.getString(rsConfig, "title"));
				domain.setValue(rsConfig.getString("value"));

				domains.add(domain.getDomainId(), domain.getIndexKey(), domain);

				log.debug(String.format("Domain rule %s is cached", rsConfig.getString("alias_")));
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

	public BinaryIndex getDomains()
	{
		return domains;
	}

	public void setDomains(BinaryIndex domains)
	{
		this.domains = domains;
	}

	public String getDomain(String domainType, String alias) throws Exception
	{
		DomainEntry domain = (DomainEntry) domains.getByKey(alias);
		
		return domain == null ? "" : domain.getTitle();
	}

	private static Logger	log	= Logger.getLogger(DomainCache.class);

}
