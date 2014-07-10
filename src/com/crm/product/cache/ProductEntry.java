/**
 * 
 */
package com.crm.product.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.index.IndexNode;
import com.crm.kernel.message.Constants;
import com.crm.util.DateUtil;
import com.crm.util.StringPool;
import com.crm.util.StringUtil;

import com.fss.util.AppException;

/**
 * @author ThangPV <br>
 * Edited by NamTA<br>
 * Edited Date 21/08/2012<br>
 * Edited Description: Add alias field
 */
public class ProductEntry extends IndexNode
{
	private long		productId			= 0;
	private String		alias				= "";
	private String		title				= "";
	private String		productType			= "";
	private String		SKU					= "";
	private String		partNumber			= "";
	private double		price				= 0;

	private boolean		termOfUse			= false;
	private int			termPeriod			= 0;
	private String		termUnit			= "";

	private boolean		subscription		= false;
	private String		subscriptionUnit	= "";
	private int			subscriptionPeriod	= 0;
	private int			gracePeriod			= 0;
	private String		graceUnit			= "";

	private int[]		subscriberTypes		= new int[0];
	private String[]	availCOS			= new String[0];
	private String[]	availStatus			= new String[0];
	private double		minBalance			= 0;
	private int			minExpirationDays	= 0;
	private double		maxBalance			= 0;
	private int			maxExpirationDays	= 0;

	private String		postpaidSKU			= "";

	private long[]		upgradeProducts		= new long[0];
	private long[]		blacklistProducts	= new long[0];

	private String		accumulator			= "";
	private String		offerName			= "";
	private String		offerGroup			= "";

	private String		circleName			= "";
	private String		circleGroup			= "";
	private String		circleProvider		= "";

	private int			maxMembers			= 0;
	private String		memberType			= "";

	private boolean		auditEnable			= false;

	private int			status				= 0;

	private BinaryIndex	actions				= new BinaryIndex();

	private BinaryIndex	messages			= new BinaryIndex();

	private BinaryIndex	prices				= new BinaryIndex();

	private BinaryIndex	scores				= new BinaryIndex();

	private BinaryIndex	associates			= new BinaryIndex();
	
	//2013-07-25 MinhDT Add start for CR charge promotion
	private String[]	availBalances		= new String[0];
	//2013-07-25 MinhDT Add end for CR charge promotion

	public ProductEntry(long productId, String alias)
	{
		super(alias);

		setProductId(productId);
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getAlias()
	{
		return alias;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getProductType()
	{
		return productType;
	}

	public void setProductType(String productType)
	{
		this.productType = productType;
	}

	public String getSKU()
	{
		return SKU;
	}

	public void setSKU(String sKU)
	{
		SKU = sKU;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public String getPartNumber()
	{
		return partNumber;
	}

	public void setPartNumber(String partNumber)
	{
		this.partNumber = partNumber;
	}

	public String getServiceAddress()
	{
		return getParameters().getString("serviceAddress");
	}

	public BinaryIndex getActions()
	{
		return actions;
	}

	public void setActions(BinaryIndex actions)
	{
		this.actions = actions;
	}

	public BinaryIndex getMessages()
	{
		return messages;
	}

	public void setMessages(BinaryIndex messages)
	{
		this.messages = messages;
	}

	public BinaryIndex getPrices()
	{
		return prices;
	}

	public void setPrices(BinaryIndex prices)
	{
		this.prices = prices;
	}

	public BinaryIndex getScores()
	{
		return scores;
	}

	public void setScores(BinaryIndex scores)
	{
		this.scores = scores;
	}

	public BinaryIndex getAssociates()
	{
		return associates;
	}

	public void setAssociates(BinaryIndex associates)
	{
		this.associates = associates;
	}

	public ProductAction getProductAction(String actionType, int subscriberType) throws Exception
	{
		ProductAction lookup = new ProductAction();

		lookup.setProductId(productId);
		lookup.setActionType(actionType);
		lookup.setSubscriberType(subscriberType);

		return (ProductAction) actions.get(lookup, false);
	}
	
	//2013-07-25 MinhDT Add start for CR charge promotion
	public ProductAction getProductAction(String actionType, int subscriberType, String balanceType) throws Exception
	{
		ProductAction lookup = new ProductAction();

		lookup.setProductId(productId);
		lookup.setActionType(actionType);
		lookup.setSubscriberType(subscriberType);
		lookup.setBalanceType(balanceType);

		return (ProductAction) actions.get(lookup, false);
	}
	//2013-07-25 MinhDT Add end for CR charge promotion

	public ProductPrice getProductPrice(
			String channel, String actionType, long segmentId, long relateProductId, int quantity, Date date)
			throws Exception
	{
		for (int j = 0; j < prices.size(); j++)
		{
			ProductPrice lookup = (ProductPrice) prices.get(j);

			if (lookup.equals(channel, actionType, segmentId, relateProductId, quantity, DateUtil.trunc(date)))
			{
				return lookup;
			}
		}

		return null;
	}
	
	//2013-07-25 MinhDT Add start for CR charge promotion
	public ProductPrice getProductPrice(
			String channel, String actionType, long segmentId, long relateProductId, int quantity, Date date, String balanceType)
			throws Exception
	{
		for (int j = 0; j < prices.size(); j++)
		{
			ProductPrice lookup = (ProductPrice) prices.get(j);

			if (lookup.equals(channel, actionType, segmentId, relateProductId, quantity, DateUtil.trunc(date), balanceType))
			{
				return lookup;
			}
		}

		return null;
	}
	//2013-07-25 MinhDT Add end for CR charge promotion

	public ProductScore getProductScore(long segmentId, int quantity, double amount, Date date) throws Exception
	{
		for (int j = 0; j < scores.size(); j++)
		{
			ProductScore lookup = (ProductScore) scores.get(j);

			if (lookup.equals(productId, segmentId, quantity, amount, DateUtil.trunc(date)))
			{
				return lookup;
			}
		}

		return null;
	}

	public ProductMessage getProductMessage(String actionType, long campaignId, String languageId, String channel, String cause)
			throws Exception
	{
		ProductMessage lookup = new ProductMessage();

		languageId = languageId.equals("") ? Constants.DEFAULT_LANGUAGE : languageId;

		lookup.setProductId(productId);
		lookup.setChannel(channel);
		lookup.setActionType(actionType);
		lookup.setLanguageId(languageId);
		lookup.setCampaignId(campaignId);
		lookup.setCause(cause);

		ProductMessage result = (ProductMessage) messages.get(lookup, false);

		if ((result == null) && (campaignId != Constants.DEFAULT_ID))
		{
			result = getProductMessage(actionType, 0, languageId, channel, cause);
		}

		return result;
	}

	public String getMessage(String actionType, long campaignId, String languageId, String channel, String cause)
			throws Exception
	{
		ProductMessage message = getProductMessage(actionType, campaignId, languageId, channel, cause);

		return (message == null) ? "" : message.getContent();
	}

	public double getScore(long segmentId, int quantity, double amount, Date date) throws Exception
	{
		double score = 0;

		ProductScore productScore = getProductScore(segmentId, quantity, amount, date);

		if (productScore == null)
		{
			throw new AppException("unknow-product-score");
		}

		if (productScore.isFlatCharge())
		{
			if (productScore.getScoreUnit() != 0)
			{
				score = (amount * productScore.getScoreRate()) / productScore.getScoreUnit();
				score = Math.round(score);
			}
			else
			{
				throw new AppException("invalid-product-score");
			}
		}
		else
		{
			score = productScore.getScoreRate();
		}

		return score;
	}

	public double getScore(double amount, Date scoreDate) throws Exception
	{
		return getScore(0, 1, amount, scoreDate);
	}

	public boolean isTermOfUse()
	{
		return termOfUse;
	}

	public void setTermOfUse(boolean termOfUse)
	{
		this.termOfUse = termOfUse;
	}

	public int getTermPeriod()
	{
		return termPeriod;
	}

	public void setTermPeriod(int termPeriod)
	{
		this.termPeriod = termPeriod;
	}

	public String getTermUnit()
	{
		return termUnit;
	}

	public void setTermUnit(String termUnit)
	{
		this.termUnit = termUnit;
	}

	public boolean isSubscription()
	{
		return subscription;
	}

	public void setSubscription(boolean subscription)
	{
		this.subscription = subscription;
	}

	public String getSubscriptionUnit()
	{
		return subscriptionUnit;
	}

	public void setSubscriptionUnit(String subscriptionUnit)
	{
		this.subscriptionUnit = subscriptionUnit;
	}

	public int getSubscriptionPeriod()
	{
		return subscriptionPeriod;
	}

	public void setSubscriptionPeriod(int subscriptionPeriod)
	{
		this.subscriptionPeriod = subscriptionPeriod;
	}

	public int getGracePeriod()
	{
		return gracePeriod;
	}

	public void setGracePeriod(int gracePeriod)
	{
		this.gracePeriod = gracePeriod;
	}

	public String getGraceUnit()
	{
		return graceUnit;
	}

	public void setGraceUnit(String graceUnit)
	{
		this.graceUnit = graceUnit;
	}

	public List<ProductAssociate> getAssociates(String associateType) throws Exception
	{
		ArrayList<ProductAssociate> result = new ArrayList<ProductAssociate>();

		for (int j = 0; j < associates.size(); j++)
		{
			ProductAssociate associate = (ProductAssociate) associates.get(j);

			if (associate.getAssociateType().equals(associateType))
			{
				result.add(associate);
			}
		}

		return result;
	}

	public int[] getSubscriberTypes()
	{
		return subscriberTypes;
	}

	public void setSubscriberTypes(int[] subscriberTypes)
	{
		this.subscriberTypes = subscriberTypes;
	}

	public void setSubscriberTypes(String list)
	{
		list = StringUtil.nvl(list, "");

		if (list.equals(""))
		{
			subscriberTypes = new int[0];
		}
		else
		{
			String[] values = list.split(StringPool.COMMA);

			subscriberTypes = new int[values.length];

			for (int j = 0; j < values.length; j++)
			{
				subscriberTypes[j] = Integer.valueOf(values[j]);
			}
		}
	}

	public String[] getAvailCOS()
	{
		return availCOS;
	}

	public void setAvailCOS(String[] availCOS)
	{
		this.availCOS = availCOS;
	}

	public long[] getUpgradeProducts()
	{
		return upgradeProducts;
	}

	public void setUpgradeProducts(long[] products)
	{
		this.upgradeProducts = products;
	}

	public void setUpgradeProducts(String[] products)
	{
		this.upgradeProducts = new long[products.length];

		for (int j = 0; j < products.length; j++)
		{
			if (!products[j].equals(""))
			{
				this.upgradeProducts[j] = Long.valueOf(products[j]);
			}
			else
			{
				this.upgradeProducts[j] = Constants.DEFAULT_ID;
			}
		}
	}

	public long[] getBlacklistProducts()
	{
		return blacklistProducts;
	}

	public void setBlacklistProducts(long[] blacklistProducts)
	{
		this.blacklistProducts = blacklistProducts;
	}

	public void setBlacklistProducts(String[] products)
	{
		this.blacklistProducts = new long[products.length];

		for (int j = 0; j < products.length; j++)
		{
			if (!products[j].equals(""))
			{
				this.blacklistProducts[j] = Long.valueOf(products[j]);
			}
			else
			{
				this.blacklistProducts[j] = Constants.DEFAULT_ID;
			}
		}
	}

	public String[] getAvailStatus()
	{
		return availStatus;
	}

	public void setAvailStatus(String[] availStatus)
	{
		this.availStatus = availStatus;
	}

	public double getMinBalance()
	{
		return minBalance;
	}

	public void setMinBalance(double minBalance)
	{
		this.minBalance = minBalance;
	}

	public int getMinExpirationDays()
	{
		return minExpirationDays;
	}

	public void setMinExpirationDays(int minExpirationDays)
	{
		this.minExpirationDays = minExpirationDays;
	}

	public String getPostpaidSKU()
	{
		return postpaidSKU;
	}

	public void setPostpaidSKU(String postpaidSKU)
	{
		this.postpaidSKU = postpaidSKU;
	}

	public String getAccumulator()
	{
		return accumulator;
	}

	public void setAccumulator(String accumulator)
	{
		this.accumulator = accumulator;
	}

	public String getOfferName()
	{
		return offerName;
	}

	public void setOfferName(String offerName)
	{
		this.offerName = offerName;
	}

	public String getCircleName()
	{
		return circleName;
	}

	public void setCircleName(String circleName)
	{
		this.circleName = circleName;
	}

	public int getMaxMembers()
	{
		return maxMembers;
	}

	public void setMaxMembers(int maxMembers)
	{
		this.maxMembers = maxMembers;
	}

	public String getMemberType()
	{
		return memberType;
	}

	public void setMemberType(String memberType)
	{
		this.memberType = memberType;
	}

	public boolean isAuditEnable()
	{
		return auditEnable;
	}

	public void setAuditEnable(boolean auditEnable)
	{
		this.auditEnable = auditEnable;
	}

	public String getOfferGroup()
	{
		return offerGroup;
	}

	public void setOfferGroup(String offerGroup)
	{
		this.offerGroup = offerGroup;
	}

	public String getCircleGroup()
	{
		return circleGroup;
	}

	public void setCircleGroup(String circleGroup)
	{
		this.circleGroup = circleGroup;
	}

	public double getMaxBalance()
	{
		return maxBalance;
	}

	public void setMaxBalance(double maxBalance)
	{
		this.maxBalance = maxBalance;
	}

	public int getMaxExpirationDays()
	{
		return maxExpirationDays;
	}

	public void setMaxExpirationDays(int maxExpirationDays)
	{
		this.maxExpirationDays = maxExpirationDays;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getCircleProvider()
	{
		return circleProvider;
	}

	public void setCircleProvider(String circleProvider)
	{
		this.circleProvider = circleProvider;
	}
	
	//2013-07-25 MinhDT Add start for CR charge promotion
	public String[] getAvailBalances()
	{
		return availBalances;
	}

	public void setAvailBalances(String[] availBalances)
	{
		this.availBalances = availBalances;
	}
	//2013-07-25 MinhDT Add end for CR charge promotion
}
