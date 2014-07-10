package com.crm.thread.validator;

import java.util.*;

import com.crm.thread.DatasourceThread;

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
 * Company: FPT
 * </p>
 * 
 * @author Vu Anh Dung
 * @version 1.0 Purpose : Base class for other threads
 */

public class DSValidator
{
	protected DatasourceThread	dispatcher	= null;

	public DSValidator()
	{
		super();
	}

	public DSValidator(DatasourceThread dispatcher)
	{
		super();
		
		setDispatcher(dispatcher);
	}

	public void setDispatcher(DatasourceThread dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public DatasourceThread getDispatcher()
	{
		return dispatcher;
	}

	public List<?> buildDatasourceList() throws Exception
	{
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////
	// Prepare data target
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	public String getDatasourceName(Object datasource) throws Exception
	{
		return (datasource == null) ? "" : datasource.toString();
	}

	// /////////////////////////////////////////////////////////////////////////
	// validate file before call convert
	// Author: HiepTH
	// Modify DateTime: 09/07/2003
	// /////////////////////////////////////////////////////////////////////////
	public boolean validate(Object datasource) throws Exception
	{
		return (datasource != null);
	}

	public boolean storeConfig(String fileName, boolean fileResult) throws Exception
	{
		return fileResult;
	}
}
