/**
 * 
 */
package com.crm.kernel.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.crm.kernel.index.BaseMessage;
import com.crm.util.AppProperties;

/**
 * @author ThangPV
 * 
 */
public class AlarmMessage extends BaseMessage implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private Date				requestTime			= new Date();
	private Date				responseTime		= null;

	private long				companyId			= 0;
	private long				groupId				= 0;
	private long				userId				= 0;
	private String				userName			= "";

	private String				sourceType			= "ISDN";
	private String				sourceDescriber		= "";
	private String				content				= "";

	private long				alarmId				= 0;
	private long				notifyId			= 0;
	private int					priority			= 0;

	private AppProperties		parameters			= new AppProperties();

	private int					status				= Constants.ORDER_STATUS_PENDING;
	private String				cause				= "";
	private String				description			= "";
	private long				provisioningId		= 0L;
	private String				provisioningClass	= "";
	private boolean				immediately			= false;

	public AlarmMessage clone()
	{
		AlarmMessage result = new AlarmMessage();

		result.setCompanyId(companyId);
		result.setGroupId(groupId);
		result.setUserId(userId);
		result.setUserName(userName);

		result.setParameters(parameters);

		result.setStatus(status);
		result.setCause(cause);

		result.setDescription(description);
		result.setProvisioningId(provisioningId);
		result.setProvisioningClass(provisioningClass);
		result.setImmediately(immediately);

		return result;
	}

	public String toString()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		StringBuffer result = new StringBuffer();

		result.append(" | requestTime = ");
		result.append(dateFormat.format(requestTime));
		result.append(" | responseTime = ");
		result.append(dateFormat.format(responseTime));

		result.append(" | companyId = ");
		result.append(companyId);
		result.append(" | groupId = ");
		result.append(groupId);
		result.append(" | userId = ");
		result.append(userName);

		result.append(" | sourceType = ");
		result.append(sourceType);
		result.append(" | sourceDescriber = ");
		result.append(sourceDescriber);
		result.append(" | content = ");
		result.append(content);

		result.append(" | alarmId = ");
		result.append(alarmId);
		result.append(" | notifyId = ");
		result.append(notifyId);
		result.append(" | priority = ");
		result.append(priority);

		result.append(" | provisioningId = ");
		result.append(provisioningId);

		result.append(" | provisioningClass = ");
		result.append(provisioningClass);

		result.append(" | parameters = ");
		result.append(parameters);

		result.append(" | status = ");
		result.append(status);
		result.append(" | cause = ");
		result.append(cause);
		result.append(" | description = ");
		result.append(description);

		return result.toString();

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

	public String getSourceType()
	{
		return sourceType;
	}

	public void setSourceType(String sourceType)
	{
		this.sourceType = sourceType;
	}

	public String getSourceDescriber()
	{
		return sourceDescriber;
	}

	public void setSourceDescriber(String sourceDescriber)
	{
		this.sourceDescriber = sourceDescriber;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public long getAlarmId()
	{
		return alarmId;
	}

	public void setAlarmId(long alarmId)
	{
		this.alarmId = alarmId;
	}

	public long getNotifyId()
	{
		return notifyId;
	}

	public void setNotifyId(long notifyId)
	{
		this.notifyId = notifyId;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
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

	public void setProvisioningId(long provisioningId)
	{
		this.provisioningId = provisioningId;
	}

	public long getProvisioningId()
	{
		return provisioningId;
	}

	public void setProvisioningClass(String provisioningClass)
	{
		this.provisioningClass = provisioningClass;
	}

	public String getProvisioningClass()
	{
		return provisioningClass;
	}

	public void setImmediately(boolean immediately)
	{
		this.immediately = immediately;
	}

	public boolean isImmediately()
	{
		return immediately;
	}

}
