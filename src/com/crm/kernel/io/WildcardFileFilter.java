package com.crm.kernel.io;

import java.io.*;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
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

public class WildcardFileFilter implements FileFilter
{
	// //////////////////////////////////////////////////////
	// Member variables
	// //////////////////////////////////////////////////////
	private String	mstrWildcard;
	private boolean	mbIncludeDirectory;

	// //////////////////////////////////////////////////////
	// Constructor
	// //////////////////////////////////////////////////////
	public WildcardFileFilter(String strWildcard, boolean bIncludeDirectory)
	{
		mstrWildcard = strWildcard;
		mbIncludeDirectory = bIncludeDirectory;
	}

	// //////////////////////////////////////////////////////
	// override function
	// //////////////////////////////////////////////////////
	public boolean accept(File file)
	{
		if (file.isDirectory())
		{
			if (mbIncludeDirectory)
				return true;
			else
				return false;
		}
		return WildcardFilter.match(mstrWildcard, file.getName());
	}
}
