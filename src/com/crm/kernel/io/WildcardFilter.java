package com.crm.kernel.io;

import java.io.*;

/**
 * <p>
 * Title: WildcardFilter
 * </p>
 * <p>
 * Description: Implements for wildcard filter
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company: FPT
 * </p>
 * 
 * @author Thai Hoang Hiep
 * @version 1.0
 */

public class WildcardFilter implements FilenameFilter
{
	// //////////////////////////////////////////////////////
	private String	mstrWildcard;

	// //////////////////////////////////////////////////////
	/**
	 * Create new instance of WildcardFilter
	 * 
	 * @param strWildcard
	 *            original wildcard of WildcardFilter
	 */
	// //////////////////////////////////////////////////////
	public WildcardFilter(String strWildcard)
	{
		mstrWildcard = strWildcard;
	}

	// //////////////////////////////////////////////////////
	/**
	 * Compare string value with wild card
	 * 
	 * @param strWild
	 *            wildcard to compare
	 * @param strVal
	 *            value to compare
	 * @return true if value match wildcard otherwise false
	 */
	// //////////////////////////////////////////////////////
	public static boolean match(String strWild, String strVal)
	{
		return match(strWild, strVal, true);
	}

	// //////////////////////////////////////////////////////
	/**
	 * Compare string value with wild card
	 * 
	 * @param strWild
	 *            wildcard to compare
	 * @param strVal
	 *            value to compare
	 * @param bIgnoreCase
	 *            true you dont want to compare case otherwise false
	 * @return true if value match wildcard otherwise false
	 */
	// //////////////////////////////////////////////////////
	public static boolean match(String strWild, String strVal, boolean bIgnoreCase)
	{
		if (bIgnoreCase)
		{
			strVal = strVal.toUpperCase();
			strWild = strWild.toUpperCase();
		}

		int iWild = 0;
		int iVal = 0;
		int iWildCount = strWild.length();
		int iValCount = strVal.length();

		while ((iVal < iValCount) && (iWild < iWildCount))
		{
			if ((strWild.charAt(iWild) == strVal.charAt(iVal)) || (strWild.charAt(iWild) == '?'))
			{
				iWild++;
				iVal++;
			}
			else if (strWild.charAt(iWild) == '*')
			{
				iWild++;
				if (iWild >= iWildCount)
					return true;
				while (true)
				{
					while ((iVal < iValCount) && strWild.charAt(iWild) != strVal.charAt(iVal))
						iVal++;
					if (iVal >= iValCount)
						return false;
					String strNewWild = strWild.substring(iWild, iWildCount);
					String strNewVal = strVal.substring(iVal, iValCount);
					if (match(strNewWild, strNewVal, bIgnoreCase))
						return true;
					else
						iVal++;
				}
			}
			else
				return false;
		}

		if (iVal >= iValCount && (iWild >= iWildCount || isOptional(strWild.substring(iWild, strWild.length()))))
			return true;
		else
			return false;
	}

	// //////////////////////////////////////////////////////
	/**
	 * 
	 * @param str
	 *            String
	 * @return boolean
	 */
	// //////////////////////////////////////////////////////
	public static boolean isOptional(String str)
	{
		for (int iIndex = 0; iIndex < str.length(); iIndex++)
		{
			if (str.charAt(iIndex) != '*')
				return false;
		}
		return true;
	}

	// //////////////////////////////////////////////////////
	// override function
	// //////////////////////////////////////////////////////
	public boolean accept(File dir, String name)
	{
		return match(mstrWildcard, name);
	}
}
