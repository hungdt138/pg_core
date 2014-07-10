package com.crm.product.cache;

import java.util.Date;

import com.crm.kernel.index.IndexNode;
import com.crm.kernel.queue.QueueFactory;
import com.crm.product.impl.OrderRoutingImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.util.StringUtil;

public class ProductRoute extends IndexNode
{
	private long	routeId				= 0;

	private String	subscriberType		= "";
	private String	channel				= "";
	private String	serviceAddress		= "";
	private String	keyword				= "";
	private String	openTime			= "";
	private String	closedTime			= "";

	private long	productId			= 0;
	private String	actionType			= "";
	private long	segmentId			= 0;
	private long	campaignId			= 0;

	private boolean	synchronous			= true;
	private boolean	createOrder			= true;
	private boolean	autoReconcile		= false;
	private int		timeout				= 0;
	private int		duplicateScan		= 0;
	private int		maxRegisterDaily	= 0;

	private boolean	checkSpam			= false;
	private int		fraudTimes			= 0;
	private int		fraudPeriod			= 0;
	private String	fraudUnit			= "";
	private String	rejectUnit			= "";
	private int		rejectPeriod		= 0;

	private boolean	notifyOwner			= false;
	private boolean	notifyDeliver		= false;
	private boolean	sendAdvertising		= false;
	private boolean	checkBalance		= false;
	private boolean	checkPromotion		= false;
	private boolean	topupEnable			= false;
	private boolean	baseChargeEnable	= false;

	private double	chargingAmount		= 0;
	private int		smsMinParams		= 0;
	private int		smsMaxParams		= 0;

	private int		status				= 0;

	private String	queueName			= QueueFactory.ORDER_REQUEST_QUEUE;

	public ProductRoute()
	{
		super();
	}

	public synchronized void setExecuteMethod(String processClass, String processMethod) throws Exception
	{
		try
		{
			processClass = StringUtil.nvl(processClass, "com.crm.product.impl.OrderRoutingImpl");
			processMethod = StringUtil.nvl(processMethod, "parser");

			if (processClass.equals("") && processMethod.equals(""))
			{

			}
			else
			{
				setProcessClass(processClass);
				setProcessMethod(processMethod);
			}
		}
		catch (Exception e)
		{
			executeImpl = null;

			throw e;
		}
	}

	public synchronized void setProcessMethod(String processMethod) throws Exception
	{
		try
		{
			if (processMethod.equals(""))
			{
				processMethod = "parser";
			}

			executeMethod =
					Class.forName(executeImpl.getClass().getName()).getMethod(
							processMethod, OrderRoutingInstance.class, ProductRoute.class, CommandMessage.class);
		}
		catch (Exception e)
		{
			executeMethod = null;

			throw e;
		}
	}

	public OrderRoutingImpl getExecuteImpl()
	{
		return (OrderRoutingImpl) super.getExecuteImpl();
	}

	public boolean equals(String channel, String service, String keyword, String time, Date date)
	{
		boolean result = getChannel().equals(channel) && getServiceAddress().equals(service) && isRange(date);

		if (result && keyword.startsWith(getKeyword()))
		{
			if (keyword.equals(getKeyword()) || isWildcard())
			{
				return ((getOpenTime().compareTo(time) <= 0) && (getClosedTime().compareTo(time) >= 0));
			}
		}

		return false;
	}

	public String getSubscriberType()
	{
		return subscriberType;
	}

	public void setSubscriberType(String subscriberType)
	{
		this.subscriberType = subscriberType;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getServiceAddress()
	{
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress)
	{
		this.serviceAddress = serviceAddress;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public String getOpenTime()
	{
		return openTime;
	}

	public void setOpenTime(String openTime)
	{
		this.openTime = openTime;
	}

	public String getClosedTime()
	{
		return closedTime;
	}

	public void setClosedTime(String closedTime)
	{
		this.closedTime = closedTime;
	}

	public long getProductId()
	{
		return productId;
	}

	public void setProductId(long productId)
	{
		this.productId = productId;
	}

	public String getActionType()
	{
		return actionType;
	}

	public void setActionType(String actionType)
	{
		this.actionType = actionType;
	}

	public long getSegmentId()
	{
		return segmentId;
	}

	public void setSegmentId(long segmentId)
	{
		this.segmentId = segmentId;
	}

	public long getCampaignId()
	{
		return campaignId;
	}

	public void setCampaignId(long campaignId)
	{
		this.campaignId = campaignId;
	}

	public boolean isSynchronous()
	{
		return synchronous;
	}

	public void setSynchronous(boolean synchronous)
	{
		this.synchronous = synchronous;
	}

	public boolean isCreateOrder()
	{
		return createOrder;
	}

	public void setCreateOrder(boolean createOrder)
	{
		this.createOrder = createOrder;
	}

	public boolean isAutoReconcile()
	{
		return autoReconcile;
	}

	public void setAutoReconcile(boolean autoReconcile)
	{
		this.autoReconcile = autoReconcile;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public int getDuplicateScan()
	{
		return duplicateScan;
	}

	public void setDuplicateScan(int duplicateScan)
	{
		this.duplicateScan = duplicateScan;
	}

	public int getMaxRegisterDaily()
	{
		return maxRegisterDaily;
	}

	public void setMaxRegisterDaily(int maxRegisterDaily)
	{
		this.maxRegisterDaily = maxRegisterDaily;
	}

	public boolean isCheckSpam()
	{
		return checkSpam;
	}

	public void setCheckSpam(boolean checkSpam)
	{
		this.checkSpam = checkSpam;
	}

	public int getFraudTimes()
	{
		return fraudTimes;
	}

	public void setFraudTimes(int fraudTimes)
	{
		this.fraudTimes = fraudTimes;
	}

	public int getFraudPeriod()
	{
		return fraudPeriod;
	}

	public void setFraudPeriod(int fraudPeriod)
	{
		this.fraudPeriod = fraudPeriod;
	}

	public String getFraudUnit()
	{
		return fraudUnit;
	}

	public void setFraudUnit(String fraudUnit)
	{
		this.fraudUnit = fraudUnit;
	}

	public String getRejectUnit()
	{
		return rejectUnit;
	}

	public void setRejectUnit(String rejectUnit)
	{
		this.rejectUnit = rejectUnit;
	}

	public int getRejectPeriod()
	{
		return rejectPeriod;
	}

	public void setRejectPeriod(int rejectPeriod)
	{
		this.rejectPeriod = rejectPeriod;
	}

	public String getQueueName()
	{
		return queueName;
	}

	public void setQueueName(String queueName)
	{
		this.queueName = queueName;
	}

	public boolean isNotifyOwner()
	{
		return notifyOwner;
	}

	public void setNotifyOwner(boolean notifyOwner)
	{
		this.notifyOwner = notifyOwner;
	}

	public boolean isNotifyDeliver()
	{
		return notifyDeliver;
	}

	public void setNotifyDeliver(boolean notifyDeliver)
	{
		this.notifyDeliver = notifyDeliver;
	}

	public boolean isSendAdvertising()
	{
		return sendAdvertising;
	}

	public void setSendAdvertising(boolean sendAdvertising)
	{
		this.sendAdvertising = sendAdvertising;
	}

	public boolean isCheckBalance()
	{
		return checkBalance;
	}

	public void setCheckBalance(boolean checkBalance)
	{
		this.checkBalance = checkBalance;
	}

	public boolean isCheckPromotion()
	{
		return checkPromotion;
	}

	public void setCheckPromotion(boolean checkPromotion)
	{
		this.checkPromotion = checkPromotion;
	}

	public boolean isBaseChargeEnable()
	{
		return baseChargeEnable;
	}

	public void setBaseChargeEnable(boolean baseChargeEnable)
	{
		this.baseChargeEnable = baseChargeEnable;
	}

	public double getChargingAmount()
	{
		return chargingAmount;
	}

	public void setChargingAmount(double chargingAmount)
	{
		this.chargingAmount = chargingAmount;
	}

	public int getSmsMinParams()
	{
		return smsMinParams;
	}

	public void setSmsMinParams(int smsMinParams)
	{
		this.smsMinParams = smsMinParams;
	}

	public int getSmsMaxParams()
	{
		return smsMaxParams;
	}

	public void setSmsMaxParams(int smsMaxParams)
	{
		this.smsMaxParams = smsMaxParams;
	}

	public boolean isTopupEnable()
	{
		return topupEnable;
	}

	public void setTopupEnable(boolean topupEnable)
	{
		this.topupEnable = topupEnable;
	}

	public long getRouteId()
	{
		return routeId;
	}

	public void setRouteId(long routeId)
	{
		this.routeId = routeId;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
