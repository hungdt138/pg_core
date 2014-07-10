package com.crm.provisioning.impl.ccws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ParseValue
{
	private static String	dateFormat	= "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ";
	
	private boolean	hasResult	= false;
	private String	value		= "";

	public ParseValue()
	{
	}

	public ParseValue(boolean hasResult, String value)
	{
		this.hasResult = hasResult;
		this.value = value;
	}

	public boolean hasValue()
	{
		return hasResult;
	}

	public String getValue()
	{
		return value;
	}

	public int getIntValue()
	{
		return Integer.parseInt(value);
	}

	public double getDoubleValue()
	{
		return Double.parseDouble(value);
	}

	public boolean getBooleanValue()
	{
		return value.toUpperCase().equals("TRUE");
	}

	public Calendar getCalendarValue() throws ParseException
	{
		Date time = dateFromString(value);
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		return cal;
	}

	public static String stringFromDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	public static Date dateFromString(String date) throws ParseException
	{
		date = date.substring(0, date.length() - 3) + "00";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		return sdf.parse(date);
	}
}
