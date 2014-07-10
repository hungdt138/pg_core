/**
 * 
 */
package com.crm.merchant.cache;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.index.IndexNode;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.fss.util.AppException;

/**
 * @author hungdt
 *
 */
public class MerchantCache {
	//cache object
	private BinaryIndex merchants = new BinaryIndex();
	
	private Date cacheDate = null;
	
	public synchronized void loadCache() throws Exception {
		Connection connection = null;

		try {
			

			log.debug("Caching merchant ...");

			connection = Database.getConnection();
			loadMerchant(connection);
			setCacheDate(new Date());
			log.debug("Merchant is cached");
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(connection);
		}
	}
	
	public void clear()
	{
		merchants.clear();
	}
	
	protected void loadMerchant(Connection connection) throws Exception {
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;
		
		try {
			String sql = "select * from merchantentry order by merchantid desc";
			stmtConfig = connection.prepareStatement(sql);
			rsConfig = stmtConfig.executeQuery();
			while(rsConfig.next()){
				log.debug(String.format("Caching merchant rule for %s ...", rsConfig.getString("code")));
				MerchantEntry merchant = new MerchantEntry(rsConfig.getLong("merchantId"), Database.getString(rsConfig, "code"));
				merchant.setMerchantId(rsConfig.getLong("merchantId"));
				merchant.setAlias(rsConfig.getString("code"));
				merchant.setUsername(rsConfig.getString("connectbyusername"));
				merchant.setPassword(rsConfig.getString("connectbypassword"));
				merchant.setHost(rsConfig.getString("connectionHost"));
				merchant.setPort(rsConfig.getInt("connectionPort"));
				merchant.setStatus(rsConfig.getInt("status"));
				merchant.setParameters(rsConfig.getString("properties"));
				merchant.setServiceAddress(rsConfig.getString("serviceAddress"));
				merchants.add(merchant.getMerchantId(), merchant.getIndexKey(), merchant);
				
				log.debug(String.format("Merchant rule %s is cached", rsConfig.getString("code")));
				
			}
		} catch (Exception e) {
			throw e;
		} finally {
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

	
	public BinaryIndex getMerchants() {
		return merchants;
	}

	public void setMerchants(BinaryIndex merchants) {
		this.merchants = merchants;
	}

	public MerchantEntry getMerchant(long merchantId) throws Exception {
		if (merchantId == Constants.DEFAULT_ID)
		{
			return null;
		}
		
		IndexNode result = merchants.getById(merchantId);

		if (result == null)
		{
			throw new AppException(Constants.ERROR_MERCHANT_NOT_FOUND);
		}

		return (MerchantEntry) result;
	}
	
	public MerchantEntry getMerchant(String alias) throws Exception
	{
		if (alias.equals(""))
		{
			return null;
		}
		
		IndexNode result = merchants.getByKey(alias);

		if (result == null)
		{
			throw new AppException(Constants.ERROR_MERCHANT_NOT_FOUND);
		}

		return (MerchantEntry) result;
	}


	private static Logger log = Logger.getLogger(MerchantCache.class);
}
