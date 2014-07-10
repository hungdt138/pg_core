package com.crm.thread.config;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: FSS-FPT
 * </p>
 * 
 * @author Thai Hoang Hiep
 * @version 1.0
 */

public class ThreadConstant
{
	// Thread const
	public static final String	ACTION_THREAD_NONE		= "";

	public static final String	ACTION_THREAD_START		= "start";

	public static final String	ACTION_THREAD_STOP		= "stop";

	public static final String	ACTION_THREAD_DISABLE	= "disable";

	public static final String	ACTION_CONFIG_LOAD		= "load";

	public static final String	ACTION_CONFIG_RELOAD	= "reload";

	public static final String	STARTUP_TYPE_AUTO		= "auto";

	public static final String	STARTUP_TYPE_MANUAL		= "manual";

	public static final String	STARTUP_TYPE_DISABLE	= "disable";

	public static final int		THREAD_NONE				= 0;
	public static final int		THREAD_START			= 1;
	public static final int		THREAD_STOP				= 2;
	public static final int		THREAD_RESTART			= 3;
	public static final int		THREAD_STARTED			= 1;
	public static final int		THREAD_STOPPED			= 2;
	public static final String	THREAD_CONFIG_FILE		= "ThreadManager.cfg";
}
