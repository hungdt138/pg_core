/**
 * 
 */
package com.crm.product.cache;

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
import com.crm.util.DateUtil;
import com.crm.util.StringUtil;

import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class ProductCache
{
	// cache object
	private BinaryIndex	products	= new BinaryIndex();

	private BinaryIndex	routes		= new BinaryIndex();

	private Date		cacheDate	= null;

	public synchronized void loadCache() throws Exception
	{
		Connection connection = null;

		try
		{
			clear();

			log.debug("Caching product ...");

			connection = Database.getConnection();

			loadProducts(connection);
			loadActions(connection);
			loadMessages(connection);
			loadRoutes(connection);

			loadProductPrice(connection);
			loadProductScore(connection);
			loadProductConfig(connection);

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
		products.clear();
		routes.clear();
	}

	/**
	 * Edited by NamTA<br>
	 * Edited Date 21/08/2012<br>
	 * Edited Description: Get alias field
	 * 
	 * @param connection
	 * @throws Exception
	 */
	protected void loadProducts(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			log.debug("Loading product rule ...");

			stmtConfig = connection.prepareStatement("Select * From ProductEntry Order by productId desc");
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProductEntry product = new ProductEntry(rsConfig.getLong("productId"), rsConfig.getString("alias_"));

				product.setProductType(Database.getString(rsConfig, "productType"));
				product.setTitle(Database.getString(rsConfig, "title"));

				product.setAlias(Database.getString(rsConfig, "alias_"));
				
				product.setSKU(Database.getString(rsConfig, "sku"));
				product.setPrice(rsConfig.getDouble("price"));
				
				product.setTermOfUse(rsConfig.getBoolean("termOfUse"));
				product.setTermPeriod(rsConfig.getInt("termPeriod"));
				product.setTermUnit(Database.getString(rsConfig, "termUnit", "day"));

				product.setSubscription(rsConfig.getBoolean("subscription"));
				product.setSubscriptionPeriod(rsConfig.getInt("subscriptionPeriod"));
				product.setSubscriptionUnit(Database.getString(rsConfig, "subscriptionUnit", "day"));
				product.setGracePeriod(rsConfig.getInt("gracePeriod"));
				product.setGraceUnit(Database.getString(rsConfig, "graceUnit", "day"));

				product.setSubscriberTypes(rsConfig.getString("subscriberTypes"));
				
				product.setStatus(rsConfig.getInt("status"));
				
				//2013-07-25 MinhDT Add start for CR charge promotion
				product.setAvailBalances(StringUtil.toStringArray(rsConfig.getString("availBalances"), ","));
				//2013-07-25 MinhDT Add end for CR charge promotion

				products.add(product.getProductId(), product.getIndexKey(), product);
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

	protected synchronized void loadProductConfig(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			log.debug("Loading product action ...");

			String SQL = "Select * From ProductConfig Order by productId desc";

			stmtConfig = connection.prepareStatement(SQL);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProductEntry product = getProduct(rsConfig.getLong("productId"));

				product.setAvailCOS(StringUtil.toStringArray(rsConfig.getString("availCOS"), ","));
				product.setAvailStatus(StringUtil.toStringArray(rsConfig.getString("availStatus"), ","));
				product.setUpgradeProducts(StringUtil.toStringArray(rsConfig.getString("upgradeProducts"), ","));
				product.setBlacklistProducts(StringUtil.toStringArray(rsConfig.getString("blacklistProducts"), ","));
				product.setPostpaidSKU(rsConfig.getString("postpaidSKU"));

				product.setMinBalance(rsConfig.getDouble("minBalance"));
				product.setMinExpirationDays(rsConfig.getInt("minExpirationDays"));
				product.setMaxBalance(rsConfig.getDouble("maxBalance"));
				product.setMaxExpirationDays(rsConfig.getInt("maxExpirationDays"));

				// product.setSingle
				product.setAccumulator(StringUtil.nvl(rsConfig.getString("accumulator"), ""));
				
				product.setOfferName(StringUtil.nvl(rsConfig.getString("offerName"), ""));
				product.setOfferGroup(StringUtil.nvl(rsConfig.getString("offerGroup"), ""));
				
				product.setCircleName(StringUtil.nvl(rsConfig.getString("circleName"), ""));
				product.setCircleGroup(StringUtil.nvl(rsConfig.getString("circleGroup"), ""));
				
				product.setCircleProvider(StringUtil.nvl(rsConfig.getString("circleProvider"), ""));
				
				if (product.getCircleProvider().equals(""))
				{
					product.setCircleProvider("HTC_HAN");
				}
				
				product.setMaxMembers(rsConfig.getInt("maxMembers"));
				product.setMemberType(StringUtil.nvl(rsConfig.getString("memberType"), ""));

				product.setParameters(rsConfig.getString("properties"));
			}

			log.debug("Product action are loaded");
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

	protected synchronized void loadActions(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			log.debug("Loading product action ...");

			String SQL = "Select * From ProductAction Order by productId desc, actionType desc, subscriberType desc, balanceType desc";

			stmtConfig = connection.prepareStatement(SQL);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProductAction action = new ProductAction();

				action.setProductId(rsConfig.getLong("productId"));
				action.setSubscriberType(rsConfig.getInt("subscriberType"));
				action.setActionType(Database.getString(rsConfig, "actionType"));
				action.setCommandId(rsConfig.getLong("commandId"));
				
				//2013-07-25 MinhDT Add start for CR charge promotion
				action.setBalanceType(Database.getString(rsConfig, "balanceType"));
				//2013-07-25 MinhDT Add end for CR charge promotion

				ProductEntry product = getProduct(rsConfig.getLong("productId"));

				product.getActions().add(action);
			}

			log.debug("Product action are loaded");
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

	protected synchronized void loadProductPrice(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			log.debug("Loading product price ...");

			String SQL = "Select * From ProductPrice "
					+ "Order by productId desc, priceType desc, actionType desc, channel desc, segmentId desc, minQuantity desc, balanceType desc";

			stmtConfig = connection.prepareStatement(SQL);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProductPrice price = new ProductPrice();

				price.setProductId(rsConfig.getLong("productId"));
				price.setPriceType(rsConfig.getString("priceType"));
				price.setSegmentId(rsConfig.getLong("segmentId"));
				price.setActionType(rsConfig.getString("actionType"));
				price.setChannel(rsConfig.getString("channel"));
				price.setMinQuantity(rsConfig.getInt("minQuantity"));
				price.setMaxQuantity(rsConfig.getInt("maxQuantity"));

				price.setRelateProductId(rsConfig.getLong("relateProductId"));

				// price.setCurrency(rsConfig.getString("currency"));
				price.setCurrency("VND");
				price.setFlatCharge(rsConfig.getBoolean("flatCharge"));

				price.setBaseOfCharge(rsConfig.getDouble("baseOfCharge"));
				price.setFullOfCharge(rsConfig.getDouble("fullOfCharge"));

				price.setStartDate(rsConfig.getDate("startDate"));
				price.setExpirationDate(rsConfig.getDate("expirationDate"));
				
				//2013-07-25 MinhDT Add start for CR charge promotion
				price.setBalanceType(rsConfig.getString("balanceType"));
				//2013-07-25 MinhDT Add end for CR charge promotion

				ProductEntry product = (ProductEntry) products.getById(rsConfig.getLong("productId"));

				product.getPrices().add(price);
			}

			log.debug("Product price are loaded");
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

	protected synchronized void loadProductScore(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			log.debug("Loading product route ...");

			String SQL = "Select * "
					+ "From	ProductScore "
					+ "Order by productId desc, minQuantity desc, minAmount desc, startDate desc";

			stmtConfig = connection.prepareStatement(SQL);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProductScore score = new ProductScore();

				score.setProductId(rsConfig.getLong("productId"));
				score.setSegmentId(rsConfig.getLong("segmentId"));

				score.setActionType(rsConfig.getString("actionType"));
				score.setMinQuantity(rsConfig.getInt("minQuantity"));
				score.setMaxQuantity(rsConfig.getInt("maxQuantity"));
				score.setMinAmount(rsConfig.getInt("minAmount"));
				score.setMaxAmount(rsConfig.getInt("maxAmount"));

				score.setBalanceType(rsConfig.getString("balanceType"));
				score.setFlatCharge(rsConfig.getBoolean("flatCharge"));
				score.setScoreRate(rsConfig.getDouble("scoreRate"));
				score.setScoreUnit(rsConfig.getDouble("scoreUnit"));
				score.setCampaignId(rsConfig.getLong("campaignId"));

				score.setStartDate(rsConfig.getDate("startDate"));
				score.setExpirationDate(rsConfig.getDate("expirationDate"));

				ProductEntry product = getProduct(rsConfig.getLong("productId"));

				if (product != null)
				{
					product.getScores().add(score);
				}
				else
				{
//					System.out.println(rsConfig.getLong("productId"));
				}
			}

			log.debug("Product route are loaded");
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

	protected synchronized void loadMessages(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			log.debug("Loading product message ...");

			String SQL =
					"Select * From ProductMessage Order by productId desc, channel desc, actionType desc, campaignId desc, languageId desc, cause desc";

			stmtConfig = connection.prepareStatement(SQL);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProductMessage message = new ProductMessage();

				message.setProductId(rsConfig.getLong("productId"));
				message.setActionType(rsConfig.getString("actionType"));
				message.setChannel(rsConfig.getString("channel"));
				message.setLanguageId(rsConfig.getString("languageId"));
				message.setCause(rsConfig.getString("cause"));
				message.setCauseValue(rsConfig.getInt("causeValue"));
				message.setContent(rsConfig.getString("content"));

				ProductEntry product = (ProductEntry) products.getById(rsConfig.getLong("productId"));

				product.getMessages().add(message);
			}

			log.debug("Product message are loaded");
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

	protected synchronized void loadRoutes(Connection connection) throws Exception
	{
		PreparedStatement stmtConfig = null;
		ResultSet rsConfig = null;

		try
		{
			log.debug("Loading product route ...");

			String SQL = "Select * "
					+ "From	ProductRoute "
					+ "Order by "
					+ "		channel desc, serviceAddress desc "
					+ "		, decode(substr(keyword,length(keyword)),'%',0,1) desc "
					+ "		, keyword desc, openTime desc, startDate desc";

			stmtConfig = connection.prepareStatement(SQL);
			rsConfig = stmtConfig.executeQuery();

			while (rsConfig.next())
			{
				ProductRoute route = new ProductRoute();

				route.setRouteId(rsConfig.getLong("routeId"));

				route.setChannel(rsConfig.getString("channel"));
				route.setServiceAddress(rsConfig.getString("serviceAddress"));

				String keyword = StringUtil.nvl(rsConfig.getString("keyword"), "%");
				if (keyword.endsWith("%"))
				{
					route.setWildcard(true);
					route.setKeyword(keyword.substring(0, keyword.length() - 1));
				}
				else
				{
					route.setWildcard(false);
					route.setKeyword(keyword);
				}

				route.setOpenTime(rsConfig.getString("openTime"));
				route.setClosedTime(rsConfig.getString("closedTime"));

				route.setProductId(rsConfig.getLong("productId"));
				route.setActionType(rsConfig.getString("actionType"));
				route.setSegmentId(rsConfig.getLong("segmentId"));
				route.setCampaignId(rsConfig.getLong("campaignId"));

				route.setSynchronous(rsConfig.getBoolean("synchronous"));
				route.setCreateOrder(rsConfig.getBoolean("createOrder"));
				route.setTimeout(rsConfig.getInt("timeout"));
				route.setDuplicateScan(rsConfig.getInt("duplicateScan"));
				route.setMaxRegisterDaily(rsConfig.getInt("maxRegisterDaily"));

				route.setCheckSpam(rsConfig.getBoolean("checkSpam"));
				route.setFraudTimes(rsConfig.getInt("fraudTimes"));
				route.setFraudPeriod(rsConfig.getInt("fraudPeriod"));
				route.setFraudUnit(rsConfig.getString("fraudUnit"));
				route.setRejectPeriod(rsConfig.getInt("rejectPeriod"));
				route.setRejectUnit(StringUtil.nvl(rsConfig.getString("rejectUnit"), ""));

				route.setCheckBalance(rsConfig.getBoolean("checkBalance"));
				route.setCheckPromotion(rsConfig.getBoolean("checkPromotion"));
				route.setTopupEnable(rsConfig.getBoolean("topupEnable"));
				route.setBaseChargeEnable(rsConfig.getBoolean("baseChargeEnable"));
				route.setNotifyOwner(rsConfig.getBoolean("notifyOwner"));
				route.setNotifyDeliver(rsConfig.getBoolean("notifyDeliver"));
				route.setSendAdvertising(rsConfig.getBoolean("sendAdvertising"));

				route.setSmsMinParams(rsConfig.getInt("smsMinParams"));
				route.setSmsMaxParams(rsConfig.getInt("smsMaxParams"));

				route.setQueueName(StringUtil.nvl(rsConfig.getString("queueName"), ""));
				route.setParameters(rsConfig.getString("properties"));
				route.setExecuteMethod(rsConfig.getString("processClass"), rsConfig.getString("processMethod"));

				route.setStartDate(rsConfig.getDate("startDate"));
				route.setExpirationDate(rsConfig.getDate("expirationDate"));

				route.setStatus(rsConfig.getInt("status"));

				routes.add(rsConfig.getLong("routeId"), "", route);
			}

			log.debug("Product route are loaded");
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

	public BinaryIndex getProducts()
	{
		return products;
	}

	public BinaryIndex getRoutes()
	{
		return routes;
	}

	public ProductEntry getProduct(long productId) throws Exception
	{
		if (productId == Constants.DEFAULT_ID)
		{
			return null;
		}

		IndexNode node = products.getById(productId);

		if (node == null)
		{
			throw new AppException(Constants.ERROR_PRODUCT_NOT_FOUND);
		}

		return (ProductEntry) node;
	}

	public ProductEntry getProduct(String alias) throws Exception
	{
		return (ProductEntry) products.getByKey(alias);
	}

	public ProductRoute getProductRoute(long routeId) throws Exception
	{
		ProductRoute productRoute = (ProductRoute) routes.getById(routeId);

		if ((productRoute == null) && (routeId != Constants.DEFAULT_ID))
		{
			throw new AppException(Constants.ERROR_ROUTE_NOT_FOUND);
		}

		return productRoute;
	}

	public ProductRoute getProductRoute(String channel, String service, String keyword, Date date) throws Exception
	{
		keyword = keyword.toUpperCase();

		// check time
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		String time = timeFormat.format(date);

		for (int j = 0; j < routes.size(); j++)
		{
			ProductRoute lookup = (ProductRoute) routes.get(j);

			if (lookup.equals(channel, service, keyword, time, DateUtil.trunc(date)))
			{
				return lookup;
			}
		}

		return null;
	}

	private static Logger	log	= Logger.getLogger(ProductCache.class);

}
