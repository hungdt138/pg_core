/**
 * 
 */
package com.crm.provisioning.message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crm.kernel.index.BaseMessage;
import com.crm.kernel.message.Constants;
import com.crm.util.AppProperties;

/**
 * @author ThangPV
 * 
 */
public class CommandMessage extends BaseMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String correlationID = "";
	private long requestId = 0;
	private Date requestTime = new Date();
	private Date responseTime = null;
	private int timeout = 60000;
	private int retryCounter = 0;

	private long routeId = 0;
	private long orderId = 0;
	private Date orderDate = new Date();
	private Date cycleDate = new Date();

	private long companyId = 0;
	private long groupId = 0;
	private long userId = 0;
	private String userName = "";
	private long merchantId = 0;
	private long agentId = 0;

	private long subscriberId = 0;
	private long subPackageId = 0;
	private long subProductId = 0;

	private long segmentId = 0;
	private long campaignId = 0;
	private long rankId = 0;

	private String channel = "";
	private String serviceAddress = "";
	private String keyword = "";
	private String isdn = "";
	private int subscriberType = Constants.UNKNOW_SUB_TYPE;
	private String shipTo = "";
	private String languageId = Constants.DEFAULT_LANGUAGE;
	private int numSMS = 0;

	private String actionType = "";
	private long productId = 0;
	private long associateProductId = 0;
	private int contentCode = 0;
	private int contentType = 0;

	private double offerPrice = 0;
	private double price = 0;
	private int quantity = 1;
	private double discount = 0;
	private double amount = 0;
	private double score = 0;
	private boolean fullOfCharge = true;

	private String provisioningType = "";
	private long provisioningId = 0;
	private long commandId = 0;

	private boolean paid = false;

	private AppProperties parameters = new AppProperties();
	private String request = "";
	private String response = "";

	private int status = Constants.ORDER_STATUS_PENDING;
	private int causeValue = Constants.ORDER_STATUS_PENDING;
	private String cause = "";

	private String description = "";
	private String content = "";
	private int chargeMode = 0;
	private String deliveryWapHref = "";
	private String deliveryWapTitle = "";
	// The cao
	private String secretCode = "";
	private String serial = "";

	private String balanceType = "";

	private List<String> completedCommands = new ArrayList<String>();

	public void copyTo(CommandMessage destination) {
		if (destination == null) {
			return;
		}

		destination.setCorrelationID(correlationID);
		destination.setRequestTime(requestTime);
		destination.setResponseTime(responseTime);
		destination.setOrderId(orderId);
		destination.setOrderDate(orderDate);
		destination.setCycleDate(cycleDate);

		destination.setCompanyId(companyId);
		destination.setGroupId(groupId);
		destination.setUserId(userId);
		destination.setUserName(userName);
		destination.setMerchantId(merchantId);
		destination.setAgentId(agentId);

		destination.setSubscriberId(subscriberId);
		destination.setSubPackageId(subPackageId);
		destination.setSubProductId(subProductId);

		destination.setSegmentId(segmentId);
		destination.setCampaignId(campaignId);
		destination.setRankId(rankId);

		destination.setChannel(channel);
		destination.setServiceAddress(serviceAddress);
		destination.setKeyword(keyword);
		destination.setIsdn(isdn);
		destination.setSubscriberType(subscriberType);
		destination.setShipTo(shipTo);
		destination.setNumSMS(numSMS);

		destination.setActionType(actionType);
		destination.setProductId(productId);
		destination.setAssociateProductId(associateProductId);
		destination.setContentCode(contentCode);
		destination.setContentType(contentType);

		destination.setOfferPrice(offerPrice);
		destination.setPrice(price);
		destination.setQuantity(quantity);
		destination.setDiscount(discount);
		destination.setAmount(amount);
		destination.setScore(score);
		destination.setFullOfCharge(fullOfCharge);

		destination.setProvisioningId(provisioningId);
		destination.setProvisioningType(provisioningType);
		destination.setCommandId(commandId);

		destination.setPaid(paid);

		destination.setFullOfCharge(fullOfCharge);
		destination.setRouteId(routeId);
		destination.setCompletedCommands(completedCommands);
		destination.setParameters(parameters);

		destination.setTimeout(timeout);
		destination.setContent(content);
		destination.setChargeMode(chargeMode);
		destination.setDeliveryWapHref(deliveryWapHref);
		destination.setDeliveryWapTitle(deliveryWapTitle);
		destination.setSecretCode(secretCode);
		destination.setSerial(serial);
		destination.setBalanceType(balanceType);
	}

	public CommandMessage clone() {
		CommandMessage result = new CommandMessage();

		copyTo(result);

		return result;
	}

	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");

		StringBuffer result = new StringBuffer();

		result.append("orderId = ");
		result.append(orderId);
		result.append(" | orderDate = ");
		result.append(dateFormat.format(orderDate));
		result.append(" | cycleDate = ");
		result.append(dateFormat.format(cycleDate));

		result.append(" | companyId = ");
		result.append(companyId);
		result.append(" | groupId = ");
		result.append(groupId);
		result.append(" | userId = ");
		result.append(userName);
		result.append(" | merchantId = ");
		result.append(merchantId);
		result.append(" | agentId = ");
		result.append(agentId);

		result.append(" | subscriberId = ");
		result.append(subscriberId);
		result.append(" | subPackageId = ");
		result.append(subPackageId);
		result.append(" | subProductId = ");
		result.append(subProductId);

		result.append(" | segmentId = ");
		result.append(segmentId);
		result.append(" | campaignId = ");
		result.append(campaignId);

		result.append(" | channel = ");
		result.append(channel);
		result.append(" | serviceAddress = ");
		result.append(serviceAddress);
		result.append(" | keyword = ");
		result.append(keyword);
		result.append(" | isdn = ");
		result.append(isdn);
		result.append(" | subscriberType = ");
		result.append(subscriberType);
		result.append(" | shipTo = ");
		result.append(shipTo);
		result.append(" | numSMS = ");
		result.append(numSMS);

		result.append(" | actionType = ");
		result.append(actionType);
		result.append(" | productId = ");
		result.append(productId);
		result.append(" | associateProductId = ");
		result.append(associateProductId);
		result.append(" | contentCode = ");
		result.append(contentCode);
		result.append(" | contentType = ");
		result.append(contentType);

		result.append(" | offerPrice = ");
		result.append(offerPrice);
		result.append(" | price = ");
		result.append(price);
		result.append(" | quantity = ");
		result.append(quantity);
		result.append(" | discount = ");
		result.append(discount);
		result.append(" | amount = ");
		result.append(amount);
		result.append(" | loyalty = ");
		result.append(score);

		result.append(" | provisioningId = ");
		result.append(provisioningId);
		result.append(" | commandId = ");
		result.append(commandId);

		result.append(" | request = ");
		result.append(request.toString());
		result.append(" | response = ");
		result.append(response.toString());

		result.append(" | status = ");
		result.append(status);
		result.append(" | cause = ");
		result.append(cause);
		result.append(" | description = ");
		result.append(description);
		result.append(" | balance = ");
		result.append(balanceType);

		return result.toString();

	}

	public String toLogString() {
		StringBuffer result = new StringBuffer();

		result.append(" | userId = ");
		result.append(userName);

		result.append(" | channel = ");
		result.append(channel);
		result.append(" | serviceAddress = ");
		result.append(serviceAddress);
		result.append(" | keyword = ");
		result.append(keyword);
		result.append(" | isdn = ");
		result.append(isdn);
		result.append(" | subscriberType = ");
		result.append(subscriberType);
		result.append(" | shipTo = ");
		result.append(shipTo);
		result.append(" | merchantId = ");
		result.append(merchantId);
		result.append(" | agentId = ");
		result.append(agentId);

		result.append(" | actionType = ");
		result.append(actionType);
		result.append(" | productId = ");
		result.append(productId);
		result.append(" | contentCode = ");
		result.append(contentCode);
		result.append(" | contentType = ");
		result.append(contentType);

		result.append(" | commandId = ");
		result.append(commandId);

		result.append(" | request = ");
		result.append(request.toString());
		result.append(" | response = ");
		result.append(response.toString());

		result.append(" | status = ");
		result.append(status);
		result.append(" | cause = ");
		result.append(cause);
		result.append(" | description = ");
		result.append(description);

		return result.toString();

	}

	public String toShortString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss:SSS");

		StringBuffer result = new StringBuffer();

		result.append("orderId = ");
		result.append(orderId);
		result.append(" | orderDate = ");
		result.append(dateFormat.format(orderDate));

		result.append(" | userName = ");
		result.append(userName);

		result.append(" | isdn = ");
		result.append(isdn);

		result.append(" | actionType = ");
		result.append(actionType);
		result.append(" | productId = ");
		result.append(productId);
		result.append(" | merchantId = ");
		result.append(merchantId);
		result.append(" | agentId = ");
		result.append(agentId);
		result.append(" | contentCode = ");
		result.append(contentCode);
		result.append(" | contentType = ");
		result.append(contentType);

		result.append(" | amount = ");
		result.append(amount);

		result.append(" | commandId = ");
		result.append(commandId);

		result.append(" | cause = ");
		result.append(cause);
		result.append(" | status = ");
		result.append(status);

		return result.toString();

	}

	public String toOrderString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");

		StringBuffer result = new StringBuffer();

		result.append("orderId = ");
		result.append(orderId);
		result.append(" | orderDate = ");
		result.append(dateFormat.format(orderDate));
		result.append(" | merchantId = ");
		result.append(merchantId);
		result.append(" | agentId = ");
		result.append(agentId);

		result.append(" | userName = ");
		result.append(userName);

		result.append(" | subProductId = ");
		result.append(subProductId);

		result.append(" | channel = ");
		result.append(channel);
		result.append(" | serviceAddress = ");
		result.append(serviceAddress);
		result.append(" | keyword = ");
		result.append(keyword);
		result.append(" | isdn = ");
		result.append(isdn);
		result.append(" | subscriberType = ");
		result.append(subscriberType);
		result.append(" | shipTo = ");
		result.append(shipTo);
		result.append(" | contentCode = ");
		result.append(contentCode);
		result.append(" | contentType = ");
		result.append(contentType);

		result.append(" | actionType = ");
		result.append(actionType);
		result.append(" | productId = ");
		result.append(productId);

		result.append(" | amount = ");
		result.append(amount);

		result.append(" | provisioningType = ");
		result.append(provisioningType);
		result.append(" | commandId = ");
		result.append(commandId);

		result.append(" | cause = ");
		result.append(cause);
		result.append(" | status = ");
		result.append(status);
		result.append(" | description = ");
		result.append(description);

		return result.toString();

	}

	public String toFeedBackLog() {
		DateFormat df = new SimpleDateFormat("DD/MM/yyyy HH:mm:ss");
		StringBuffer result = new StringBuffer();
		result.append("TIME SENT: ");
		result.append(df.format(requestTime));
		result.append("SEND: ");
		result.append(request + "\n");
		result.append("TIME RECEIVE: ");
		result.append(df.format(responseTime));
		result.append("RECEIVE: ");
		result.append(df.format(responseTime));
		return result.toString();
	}
	
	

	public String getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getDeliveryWapHref() {
		return deliveryWapHref;
	}

	public void setDeliveryWapHref(String deliveryWapHref) {
		this.deliveryWapHref = deliveryWapHref;
	}

	public String getDeliveryWapTitle() {
		return deliveryWapTitle;
	}

	public void setDeliveryWapTitle(String deliveryWapTitle) {
		this.deliveryWapTitle = deliveryWapTitle;
	}

	public int getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(int chargeMode) {
		this.chargeMode = chargeMode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getNumSMS() {
		return numSMS;
	}

	public void setNumSMS(int numSMS) {
		this.numSMS = numSMS;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public int getContentCode() {
		return contentCode;
	}

	public void setContentCode(int contentCode) {
		this.contentCode = contentCode;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public boolean isExisted() {
		return subProductId != 0;
	}

	public boolean isPrepaid() {
		return subscriberType == Constants.PREPAID_SUB_TYPE;
	}

	public boolean isPostpaid() {
		return subscriberType == Constants.POSTPAID_SUB_TYPE;
	}

	public String getSubscriberLabel() {
		return isPrepaid() ? "prepaid" : "postpaid";
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getCycleDate() {
		return cycleDate;
	}

	public void setCycleDate(Date cycleDate) {
		this.cycleDate = cycleDate;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(long subscriberId) {
		this.subscriberId = subscriberId;
	}

	public long getSubPackageId() {
		return subPackageId;
	}

	public void setSubPackageId(long subPackageId) {
		this.subPackageId = subPackageId;
	}

	public long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(long subProductId) {
		this.subProductId = subProductId;
	}

	public long getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(long segmentId) {
		this.segmentId = segmentId;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public long getRankId() {
		return rankId;
	}

	public void setRankId(long rankId) {
		this.rankId = rankId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getIsdn() {
		return isdn;
	}

	public void setIsdn(String isdn) {
		this.isdn = isdn;
	}

	public int getSubscriberType() {
		return subscriberType;
	}

	public void setSubscriberType(int subscriberType) {
		this.subscriberType = subscriberType;
	}

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getAssociateProductId() {
		return associateProductId;
	}

	public void setAssociateProductId(long associateProductId) {
		this.associateProductId = associateProductId;
	}

	public double getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(double offerPrice) {
		this.offerPrice = offerPrice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getProvisioningType() {
		return provisioningType;
	}

	public void setProvisioningType(String provisioningType) {
		this.provisioningType = provisioningType;
	}

	public long getProvisioningId() {
		return provisioningId;
	}

	public void setProvisioningId(long provisioningId) {
		this.provisioningId = provisioningId;
	}

	public long getCommandId() {
		return commandId;
	}

	public void setCommandId(long commandId) {
		this.commandId = commandId;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCauseValue() {
		return causeValue;
	}

	public void setCauseValue(int causeValue) {
		this.causeValue = causeValue;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public boolean isPaid() {
		return paid;
	}

	public AppProperties getParameters() {
		return parameters;
	}

	public void setParameters(AppProperties parameters) {
		this.parameters = parameters;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public boolean isFullOfCharge() {
		return fullOfCharge;
	}

	public void setFullOfCharge(boolean fullOfCharge) {
		this.fullOfCharge = fullOfCharge;
	}

	public String getCorrelationID() {
		return correlationID;
	}

	public void setCorrelationID(String correlationID) {
		this.correlationID = correlationID;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

	/**
	 * Sets list of completed command alias
	 * 
	 * @param completedCommands
	 */
	public void setCompletedCommands(List<String> completedCommands) {
		this.completedCommands = completedCommands;
	}

	/**
	 * Gets list of completed command alias
	 * 
	 * @return
	 */
	public List<String> getCompletedCommands() {
		return this.completedCommands;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * 
	 * @author Nam
	 */
	public void setRequestValue(String key, String value) {
		parameters.setString(Constants.REQUEST_PREFIX + key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * 
	 * @author Nam
	 */
	public void setRequestValue(String key, int value) {
		parameters.setInteger(Constants.REQUEST_PREFIX + key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * 
	 * @author Nam
	 */
	public void setRequestValue(String key, long value) {
		parameters.setLong(Constants.REQUEST_PREFIX + key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * 
	 * @author Nam
	 */
	public void setRequestValue(String key, double value) {
		parameters.setDouble(Constants.REQUEST_PREFIX + key, value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * 
	 * @author Nam
	 */
	public void setRequestValue(String key, Date value) throws Exception {
		parameters.setDate(Constants.REQUEST_PREFIX + key, value,
				Constants.DATE_FORMAT);
	}

	public void setResponseValue(String key, String value) {
		parameters.setString(Constants.RESPONSE_PREFIX + key, value);
	}

	public void setResponseValue(String key, int value) {
		parameters.setInteger(Constants.RESPONSE_PREFIX + key, value);
	}

	public void setResponseValue(String key, long value) {
		parameters.setLong(Constants.RESPONSE_PREFIX + key, value);
	}

	public void setResponseValue(String key, double value) {
		parameters.setDouble(Constants.RESPONSE_PREFIX + key, value);
	}

	public void setResponseValue(String key, Date value) throws Exception {
		parameters.setDate(Constants.RESPONSE_PREFIX + key, value,
				Constants.DATE_FORMAT);
	}

	/**
	 * 
	 * @param key
	 * @param defaultvalue
	 * @return
	 * 
	 * @author Nam
	 */
	public String getRequestValue(String key, String defaultvalue) {
		return parameters.getProperty(Constants.REQUEST_PREFIX + key,
				defaultvalue);
	}

	/**
	 * 
	 * @param key
	 * @return
	 * 
	 * @author Nam
	 */
	public String getRequestValue(String key) {
		return parameters.getProperty(Constants.REQUEST_PREFIX + key, "");
	}

	public String getResponseValue(String key, String defaultvalue) {
		return parameters.getProperty(Constants.RESPONSE_PREFIX + key,
				defaultvalue);
	}

	public String getResponseValue(String key) {
		return parameters.getProperty(Constants.RESPONSE_PREFIX + key, "");
	}

	public void setRetryCounter(int retryCounter) {
		this.retryCounter = retryCounter;
	}

	public int getRetryCounter() {
		return retryCounter;
	}

}
