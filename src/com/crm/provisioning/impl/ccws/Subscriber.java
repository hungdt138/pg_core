package com.crm.provisioning.impl.ccws;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Subscriber
{
	private String notificationLanguage;
	private String roamingCreditLimitAsString;
	private String currentState;
	private String COSName;
	private String languageName;
	private String numberFreeAnCallsString;
	private String billCycleDay;
	private String notificationLevel;
	private String limitType;
	private String SPName;
	private Calendar creationDate;
	private Calendar dateEnterActive;
	
	private ArrayList<SubscriberBalance> balances = new ArrayList<SubscriberBalance>();
	
	public void setData(Element elementData) throws Exception
	{
		this.setNotificationLanguage(this.getTextValue(elementData, "NotificationLanguage"));
		this.setRoamingCreditLimitAsString(this.getTextValue(elementData, "RoamingCreditLimitAsString"));
		this.setCurrentState(this.getTextValue(elementData, "CurrentState"));
		this.setCOSName(this.getTextValue(elementData, "COSName"));
		this.setLanguageName(this.getTextValue(elementData, "LanguageName"));
		this.setNumberFreeAnCallsString(this.getTextValue(elementData, "NumberFreeAnCallsString"));
		this.setBillCycleDay(this.getTextValue(elementData, "BillCycleDay"));
		this.setNotificationLevel(this.getTextValue(elementData, "NotificationLevel"));
		this.setLimitType(this.getTextValue(elementData, "LimitType"));
		
		NodeList nl = elementData.getElementsByTagName("Balances");
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			NodeList balanceList = el.getElementsByTagName("Balance");
			if(balanceList != null && balanceList.getLength() > 0) {
				for (int i = 0; i < balanceList.getLength(); i++)
				{
					Element balance = (Element)balanceList.item(i);
					
					SubscriberBalance subBalance = new SubscriberBalance();
					subBalance.setBalance(this.getDoubleValue(balance, "Balance"));
					subBalance.setAccountExpiration(this.getCalendarValue(balance, "AccountExpiration"));
					subBalance.setBalanceName(this.getTextValue(balance, "BalanceName"));
					subBalance.setNextMaximumSpendingLimit(this.getDoubleValue(balance, "NextMaximumSpendingLimit"));
					subBalance.setTotalSpendingLimit(this.getDoubleValue(balance, "TotalSpendingLimit"));
					subBalance.setAvailableBalance(this.getDoubleValue(balance, "AvailableBalance"));
					subBalance.setMaximumSpendingLimit(this.getDoubleValue(balance, "MaximumSpendingLimit"));
					subBalance.setAvailableSpendingLimit(this.getDoubleValue(balance, "AvailableSpendingLimit"));
					subBalance.setPrecisionPoint(this.getIntValue(balance, "PrecisionPoint"));
					subBalance.setExclusiveBalance(Boolean.parseBoolean(this.getTextValue(balance, "ExclusiveBalance")));
					
					this.addBalance(subBalance);
				}	
			}
		}
		
		this.setSPName(this.getTextValue(elementData, "SPName"));
		this.setCreationDate(this.getCalendarValue(elementData, "CreationDate"));
		this.setDateEnterActive(this.getCalendarValue(elementData, "DateEnterActive"));
	}
	
	private String getTextValue(Element ele, String tagName)
	{
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0)
		{
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}
	
	private double getDoubleValue(Element ele, String tagName)
	{
		String textVal = "0";
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0)
		{
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return Double.parseDouble(textVal);
	}
	
	private int getIntValue(Element ele, String tagName)
	{
		String textVal = "0";
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0)
		{
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return Integer.parseInt(textVal);
	}
	
	private Calendar getCalendarValue(Element ele, String tagName) throws Exception
	{
		Calendar cal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0)
		{
			Element el = (Element)nl.item(0);
			
			cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			cal.setTime(sdf.parse(el.getFirstChild().getNodeValue().substring(0, 19)));
		}
		
		return cal;
	}
	
	public void addBalance(SubscriberBalance balance)
	{
		this.balances.add(balance);		
	}
	
	public String getNotificationLanguage()
	{
		return this.notificationLanguage;
	}

	public void setNotificationLanguage(String notificationLanguage)
	{
		this.notificationLanguage = notificationLanguage;
	}
	
	public String getCurrentState()
	{
		return this.currentState;
	}

	public void setCurrentState(String currentState)
	{
		this.currentState = currentState;
	}
	
	public String getRoamingCreditLimitAsString()
	{
		return this.roamingCreditLimitAsString;
	}

	public void setRoamingCreditLimitAsString(String roamingCreditLimitAsString)
	{
		this.roamingCreditLimitAsString = roamingCreditLimitAsString;
	}
	
	public String getCOSName()
	{
		return this.COSName;
	}

	public void setCOSName(String COSName)
	{
		this.COSName = COSName;
	}
	
	public String getLanguageName()
	{
		return this.languageName;
	}

	public void setLanguageName(String languageName)
	{
		this.languageName = languageName;
	}
	
	public String getNumberFreeAnCallsString()
	{
		return this.numberFreeAnCallsString;
	}

	public void setNumberFreeAnCallsString(String numberFreeAnCallsString)
	{
		this.numberFreeAnCallsString = numberFreeAnCallsString;
	}
	
	public String getBillCycleDay()
	{
		return this.billCycleDay;
	}

	public void setBillCycleDay(String billCycleDay)
	{
		this.billCycleDay = billCycleDay;
	}
	
	public String getNotificationLevel()
	{
		return this.notificationLevel;
	}

	public void setNotificationLevel(String notificationLevel)
	{
		this.notificationLevel = notificationLevel;
	}
	
	public String getLimitType()
	{
		return this.limitType;
	}

	public void setLimitType(String limitType)
	{
		this.limitType = limitType;
	}
	
	public String getSPName()
	{
		return this.SPName;
	}

	public void setSPName(String SPName)
	{
		this.SPName = SPName;
	}

	public Calendar getCreationDate()
	{
		return this.creationDate;
	}

	public void setCreationDate(Calendar creationDate)
	{
		this.creationDate = creationDate;
	}
	
	public Calendar getDateEnterActive()
	{
		return this.dateEnterActive;
	}

	public void setDateEnterActive(Calendar dateEnterActive)
	{
		this.dateEnterActive = dateEnterActive;
	}
}
