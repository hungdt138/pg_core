package com.crm.util;

import java.util.Calendar;
import java.util.Date;

import com.fss.util.AppException;

public class DateUtil
{
	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static int compareDate(Date value1, Date value2)
	{
		return trunc(value1).compareTo(trunc(value2));
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static boolean isRange(Date startDate, Date endDate, Date compareDate)
	{
		if (compareDate == null)
		{
			return false;
		}
		else if (startDate == null)
		{
			return false;
		}
		else if ((startDate.compareTo(compareDate) <= 0) && ((endDate == null) || (endDate.compareTo(compareDate) >= 0)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static java.sql.Date getDateSQL(Date date)
	{
		if (date == null)
		{
			return null;
		}
		else
		{
			return new java.sql.Date(date.getTime());
		}
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static java.sql.Time getTimeSQL(Date date)
	{
		if (date == null)
		{
			return null;
		}
		else
		{
			return new java.sql.Time(date.getTime());
		}
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static java.sql.Time getTimeSQL()
	{
		return getTimeSQL(null);
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static java.sql.Timestamp getTimestampSQL(Date date)
	{
		if (date == null)
		{
			return null;
		}
		else
		{
			return new java.sql.Timestamp(date.getTime());
		}
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static java.sql.Timestamp getTimestampSQL()
	{
		return getTimestampSQL(null);
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static int getMonthDiff(Date fromDate, Date toDate)
	{
		int diff = 0;

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		fromCalendar.set(Calendar.DATE, 1);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(toDate);
		toCalendar.set(Calendar.DATE, 1);

		while (fromCalendar.before(toCalendar))
		{
			diff++;

			fromCalendar.add(Calendar.MONTH, 1);
		}

		return diff;
	}

	public static long daysBetween(final Calendar startDate, final Calendar endDate)
	{
		Calendar sDate = (Calendar) startDate.clone();
		long daysBetween = 0;

		int y1 = sDate.get(Calendar.YEAR);
		int y2 = endDate.get(Calendar.YEAR);
		int m1 = sDate.get(Calendar.MONTH);
		int m2 = endDate.get(Calendar.MONTH);

		int monthDiff = 0;
		int yearDiff = 0;

		// **year optimization**
		while (((y2 - y1) * 12 + (m2 - m1)) > 12)
		{

			// move to Jan 01
			if (sDate.get(Calendar.MONTH) == Calendar.JANUARY
					&& sDate.get(Calendar.DAY_OF_MONTH) == sDate.getActualMinimum(Calendar.DAY_OF_MONTH))
			{

				daysBetween += sDate.getActualMaximum(Calendar.DAY_OF_YEAR);
				monthDiff++;

				sDate.add(Calendar.YEAR, 1);
			}
			else
			{
				int diff = 1 + sDate.getActualMaximum(Calendar.DAY_OF_YEAR) - sDate.get(Calendar.DAY_OF_YEAR);
				sDate.add(Calendar.DAY_OF_YEAR, diff);

				daysBetween += diff;
				monthDiff = monthDiff + (diff * 12);
				yearDiff = yearDiff + diff;
			}
			y1 = sDate.get(Calendar.YEAR);
		}

		// ** optimize for month **
		// while the difference is more than a month, add a month to start month
		while ((m2 - m1) % 12 > 1)
		{
			daysBetween += sDate.getActualMaximum(Calendar.DAY_OF_MONTH);
			sDate.add(Calendar.MONTH, 1);
			m1 = sDate.get(Calendar.MONTH);
		}

		// process remainder date
		while (sDate.before(endDate))
		{
			sDate.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}

		return daysBetween;
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static int getDateDiff(Date fromDate, Date toDate, String unit, int period, boolean roundDate)
			throws AppException
	{
		long diff = 0;
		int cycleUnit = 1;

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);

		fromCalendar.set(Calendar.HOUR, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(toDate);
		toCalendar.set(Calendar.HOUR, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		if (roundDate)
		{
			fromCalendar.set(Calendar.DATE, 1);
			toCalendar.set(Calendar.DATE, 1);
		}

		int field = Calendar.DATE;

		if ((unit.equalsIgnoreCase("day") && unit.equalsIgnoreCase("daily")) && fromCalendar.equals(toCalendar))
		{
			return 1;
		}
		else
		{
			if (unit.equalsIgnoreCase("month") || unit.equalsIgnoreCase("monthly"))
			{
				field = Calendar.MONTH;
				cycleUnit = 30;
			}
			else if (unit.equalsIgnoreCase("year"))
			{
				field = Calendar.YEAR;
				cycleUnit = 365;
			}
			else if (unit.equalsIgnoreCase("week") || unit.equalsIgnoreCase("weekly"))
			{
				cycleUnit = 7;
			}

			diff = (toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / (1000 * 60 * 60 * 24);
			diff = diff / (cycleUnit * period);

			if ((field == Calendar.MONTH) || (field == Calendar.YEAR))
			{
				// fromCalendar.add(field,
				// Integer.parseInt(String.valueOf(diff)) *
				// period);
			}

		}

		return Integer.parseInt(String.valueOf(diff));
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static Date getLastDate(Date fromDate, Date toDate, String unit, int period)
			throws AppException
	{
		int diff = getDateDiff(fromDate, toDate, unit, period);

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);

		fromCalendar.set(Calendar.HOUR, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(toDate);
		toCalendar.set(Calendar.HOUR, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		int field = Calendar.DATE;

		if (unit.equalsIgnoreCase("month"))
		{
			field = Calendar.MONTH;

			fromCalendar.add(Calendar.MONTH, diff * period);
		}
		else if (unit.equalsIgnoreCase("year"))
		{
			field = Calendar.YEAR;

			fromCalendar.add(Calendar.YEAR, diff * period);
		}
		else if (unit.equalsIgnoreCase("week"))
		{
			fromCalendar.add(Calendar.DATE, diff * period * 7);
		}
		else
		{
			fromCalendar.add(Calendar.DATE, diff * period);
		}

		Date lastDate = fromCalendar.getTime();

		while (!fromCalendar.after(toCalendar))
		{
			diff++;
			lastDate = fromCalendar.getTime();

			fromCalendar.add(field, period);
		}

		if (fromCalendar.after(toCalendar))
		{
			diff--;
		}
//		System.out.println("diff: " + diff);

		return lastDate;
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static Date getCycleDate(Date registerDate, Date orderDate) throws AppException
	{
		Calendar cycleCalendar = Calendar.getInstance();
		cycleCalendar.setTime(registerDate);

		cycleCalendar.set(Calendar.HOUR, 0);
		cycleCalendar.set(Calendar.MINUTE, 0);
		cycleCalendar.set(Calendar.SECOND, 0);
		cycleCalendar.set(Calendar.MILLISECOND, 0);

		Calendar orderCalendar = Calendar.getInstance();
		orderCalendar.setTime(orderDate);
		orderCalendar.set(Calendar.HOUR, 0);
		orderCalendar.set(Calendar.MINUTE, 0);
		orderCalendar.set(Calendar.SECOND, 0);
		orderCalendar.set(Calendar.MILLISECOND, 0);

		if ((cycleCalendar.get(Calendar.MONTH) == 1) && (cycleCalendar.get(Calendar.DATE) == 29))
		{
			cycleCalendar.set(Calendar.DATE, 28);
		}
		cycleCalendar.set(Calendar.MONTH, orderCalendar.get(Calendar.MONTH));
		cycleCalendar.set(Calendar.YEAR, orderCalendar.get(Calendar.YEAR));

		if (cycleCalendar.after(orderCalendar))
		{
			cycleCalendar.add(Calendar.MONTH, -1);
		}

		return cycleCalendar.getTime();
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static int getDateDiff(Date fromDate, Date toDate, String unit, int period) throws AppException
	{
		return getDateDiff(fromDate, toDate, unit, period, false);
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static int getDateDiff(Date fromDate, Date toDate) throws AppException
	{
		return getDateDiff(fromDate, toDate, "day", 1, false);
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static Date endOfDay(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime((date == null) ? new Date() : date);

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static Date trunc(Date date)
	{
		Calendar cal = Calendar.getInstance();
		
		cal.setTime((date == null) ? new Date() : date);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static Date truncMonth(Date date)
	{
		Calendar cal = Calendar.getInstance();
		
		cal.setTime((date == null) ? new Date() : date);

		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static Calendar trunc(Calendar calendar)
	{
		if (calendar == null)
		{
			calendar = Calendar.getInstance();
		}

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static Date trunc(Date date, int truncType)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime((date == null) ? new Date() : date);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		cal.set(truncType, 1);

		return cal.getTime();
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static java.sql.Date getDateSQL()
	{
		return getDateSQL(new Date());
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static Date trunc()
	{
		return trunc(new Date());
	}

	public static Date addDate(Date startDate, String unit, int quantity) throws Exception
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);

		if (unit == null)
		{
			throw new AppException("invalid-date-unit");
		}
		else if (unit.equalsIgnoreCase("month") || unit.equalsIgnoreCase("monthly"))
		{
			calendar.add(Calendar.DATE, 30 * quantity);
		}
		else if (unit.equalsIgnoreCase("week") || unit.equalsIgnoreCase("weekly"))
		{
			calendar.add(Calendar.DATE, 7 * quantity);
		}
		else if (unit.equalsIgnoreCase("daily") || unit.equalsIgnoreCase("day"))
		{
			calendar.add(Calendar.DATE, quantity);
		}
		else if (unit.equalsIgnoreCase("year") || unit.equalsIgnoreCase("yearly"))
		{
			calendar.add(Calendar.YEAR, quantity);
		}
		else
		{
			throw new AppException("invalid-date-unit");
		}

		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		
		return calendar.getTime();
	}

	public static Date addSecond(Date date, int second) throws Exception
	{
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		calendar.add(Calendar.SECOND, second);

		return calendar.getTime();
	}

	// /////////////////////////////////////////////////////////
	// Main entry
	// /////////////////////////////////////////////////////////
	public static void main(String argvs[]) throws AppException
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 01, 29, 23, 59, 59);

		Date date = new Date();

//		System.out.println(date.toString());
//
//		System.out.println(DateUtil.getLastDate(calendar.getTime(), date, "day", 3));
//		System.out.println(DateUtil.getLastDate(calendar.getTime(), date, "week", 1));
//		System.out.println(DateUtil.getLastDate(calendar.getTime(), date, "month", 1));
//		System.out.println(DateUtil.getLastDate(calendar.getTime(), date, "year", 1));
//
//		System.out.println(DateUtil.getCycleDate(calendar.getTime(), date));
	}
}
