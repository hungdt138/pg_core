/**
 * 
 */
package com.crm.provisioning.message;

import java.util.Date;

import com.crm.kernel.index.BaseMessage;
import com.crm.kernel.message.Constants;
import com.crm.util.AppProperties;

/**
 * @author ThangPV
 * 
 */
public class OSACallbackMessage extends BaseMessage
{
	/**
	 * 
	 */
	private static final long	serialVersionUID		= 1L;

	private long				requestId				= 0;
	private Date				requestTime				= new Date();
	private Date				responseTime			= null;

	private long				companyId				= 0;
	private long				groupId					= 0;
	private long				userId					= 0;
	private String				userName				= "";

	private String				merchantAccount			= "";
	private int					merchantId				= 0;
	private int					callbackPort			= 0;

	private String				actionType				= "";
	private String				sessionId				= "";
	private String				requestNumber			= "";
	private String				nextChargingSequence	= "";

	private String				isdn					= "";
	private double				amount					= 0;

	private AppProperties		parameters				= new AppProperties();

	private int					status					= Constants.ORDER_STATUS_PENDING;
	private int					responseCode			= Constants.ORDER_STATUS_PENDING;
	private String				cause					= "";

	private String				description				= "";

	public long getRequestId()
	{
		return requestId;
	}

	public void setRequestId(long requestId)
	{
		this.requestId = requestId;
	}

	public Date getRequestTime()
	{
		return requestTime;
	}

	public void setRequestTime(Date requestTime)
	{
		this.requestTime = requestTime;
	}

	public Date getResponseTime()
	{
		return responseTime;
	}

	public void setResponseTime(Date responseTime)
	{
		this.responseTime = responseTime;
	}

	public long getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(long companyId)
	{
		this.companyId = companyId;
	}

	public long getGroupId()
	{
		return groupId;
	}

	public void setGroupId(long groupId)
	{
		this.groupId = groupId;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getMerchantAccount()
	{
		return merchantAccount;
	}

	public void setMerchantAccount(String merchantAccount)
	{
		this.merchantAccount = merchantAccount;
	}

	public int getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(int merchantId)
	{
		this.merchantId = merchantId;
	}

	public int getCallbackPort()
	{
		return callbackPort;
	}

	public void setCallbackPort(int callbackPort)
	{
		this.callbackPort = callbackPort;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getRequestNumber()
	{
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber)
	{
		this.requestNumber = requestNumber;
	}

	public String getNextChargingSequence()
	{
		return nextChargingSequence;
	}

	public void setNextChargingSequence(String nextChargingSequence)
	{
		this.nextChargingSequence = nextChargingSequence;
	}

	public String getIsdn()
	{
		return isdn;
	}

	public void setIsdn(String isdn)
	{
		this.isdn = isdn;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public AppProperties getParameters()
	{
		return parameters;
	}

	public void setParameters(AppProperties parameters)
	{
		this.parameters = parameters;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getResponseCode()
	{
		return responseCode;
	}

	public void setResponseCode(int responseCode)
	{
		this.responseCode = responseCode;
	}

	public String getCause()
	{
		return cause;
	}

	public void setCause(String cause)
	{
		this.cause = cause;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getActionType()
	{
		return actionType;
	}

	public void setActionType(String actionType)
	{
		this.actionType = actionType;
	}
}
