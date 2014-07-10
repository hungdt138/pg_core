package com.crm.util;

import com.crm.kernel.index.IndexNode;

public class CompareUtil 
{

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static int compare(long source, long dest, boolean wildcard)
	{
		int result = 0;

		if ((source == 0) && wildcard)
		{
			return result;
		}

		if (source > dest)
		{
			result = 1;
		}
		else if (source < dest)
		{
			result = -1;
		}

		return result;
	}

	// //////////////////////////////////////////////////////
	// return 0 if acceptable, less than return -1, more than return 1
	// Author : ThangPV
	// Created Date : 06/01/2006
	// //////////////////////////////////////////////////////
	public static int compare(long source, long dest)
	{
		return compare(source, dest, false);
	}

	public static int compareString(String source, String dest, boolean equalIfNull, boolean wildcard)
	{
		int result = 0;

		if (source.equals(""))
		{
			if (equalIfNull)
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
		else if (dest.equals(""))
		{
			return 1;
		}

		result = source.compareTo(dest);

		if (result != 0)
		{
			if (dest.startsWith(source) && wildcard)
			{
				result = 0;
			}
		}

		return result;
	}

	public static int compareString(String source, String dest, boolean wildcard)
	{
		return compareString(source, dest, false, wildcard);
	}

	public static int compareByIndexKey(IndexNode index, IndexNode lookup)
	{
		if (lookup == null)
		{
			return 1;
		}

		int result = 0;

		if (lookup.getIndexKey().startsWith(index.getIndexKey()))
		{
			if (index.isWildcard() || index.getIndexKey().equals(lookup.getIndexKey()))
			{
				result = 0;
			}
			else
			{
				result = index.getIndexKey().compareTo(lookup.getIndexKey());
			}
		}
		else if (index.isWildcard())
		{
			result = 0;
		}
		else
		{
			result = index.getIndexKey().compareTo(lookup.getIndexKey());
		}

		return result;
	}

	public static boolean equalsKey(IndexNode index, IndexNode lookup)
	{
		if ((index == null) || (lookup == null))
		{
			return false;
		}

		return lookup.getIndexKey().equals(index.getIndexKey());
	}

	public static boolean beforeKey(IndexNode index, IndexNode lookup)
	{
		if (index.getIndexKey().equals(lookup.getIndexKey()))
		{
			return index.isWildcard();
		}
		else
		{
			return lookup.getIndexKey().compareTo(index.getIndexKey()) < 0;
		}
	}

	public static boolean before(long long1, long long2)
	{
		return long1 < long2;
	}

	public static boolean before(int int1, int int2)
	{
		return int1 < int2;
	}

	public static boolean before(String string1, String string2)
	{
		return string1.compareTo(string2) < 0;
	}
}
