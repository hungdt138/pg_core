/**
 * 
 */
package com.crm.subscriber.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ThangPV
 * 
 */
public class SubscriberAge implements Serializable
{
	// PK fields -->

	private Date	sumDate	= null;
	private long	age		= 0;

	// Audit fields -->

	private long	days	= 0;
	private long	weeks	= 0;
	private long	months	= 0;
	private long	years	= 0;

	public Date getSumDate()
	{
		return sumDate;
	}

	public void setSumDate(Date sumDate)
	{
		this.sumDate = sumDate;
	}

	public long getAge()
	{
		return age;
	}

	public void setAge(long age)
	{
		this.age = age;
	}

	public long getDays()
	{
		return days;
	}

	public void setDays(long days)
	{
		this.days = days;
	}

	public long getWeeks()
	{
		return weeks;
	}

	public void setWeeks(long weeks)
	{
		this.weeks = weeks;
	}

	public long getMonths()
	{
		return months;
	}

	public void setMonths(long months)
	{
		this.months = months;
	}

	public long getYears()
	{
		return years;
	}

	public void setYears(long years)
	{
		this.years = years;
	}
}
