package com.crm.provisioning.impl.cb;

import java.util.ArrayList;
import java.util.Date;

import com.adc.eji.tre.TreChainedException;
import com.adc.eji.tre.TreVarDate;
import java.util.List;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class PostpaidManager
{
	public String					Host					= "";
	public String					Port					= "";
	public String					Charset					= "";
	public String					MessageIndexFile		= "";
	public String					User					= "";
	public String					Pass					= "";
	public String					Comment					= "";
	public static TreStaticCalls	ltc						= null;
	private Date					dtEffect				= null;
	private final int				MAX_ROW_NUMBER_SEARCH	= 100;

	public PostpaidManager(String strHost, String strPort, String strCharset, String strMessageIndexFile,
			String strUser, String strPass, String strComment)
	{
		Host = strHost;
		Port = strPort;
		Charset = strCharset;
		MessageIndexFile = strMessageIndexFile;
		User = strUser;
		Pass = strPass;
		Comment = strComment;
	}

	public PostpaidManager(String host, String port, String user, String password)
	{
		this(host, port, "UTF-8", "ejimsgIndex.config", user, password, "");
	}

	public static void disconnect()
	{
		try
		{
			ltc.treDisconnect();
		}
		catch (TreChainedException ex)
		{
			ex.printStackTrace();
		}
	}

	public static Long paymentInsert(String strMDN, String strAmount) throws
			Exception
	{
		Long lngAccountId = searchAccountId(strMDN);
		ArrayList listFieldValues = new ArrayList();
		ArrayList listFieldNames = new ArrayList();
		Long lReturn = new Long(0);

		listFieldNames.add("TO_ACCOUNT_ID");
		listFieldValues.add(lngAccountId);
		listFieldNames.add("PAYMENT_TYPE_ID");
		listFieldValues.add(new Long(500001));
		listFieldNames.add("CURRENCY_ID");
		listFieldValues.add(new Long(1));
		listFieldNames.add("PAYMENT_LOCATION_CODE");
		listFieldValues.add(new Long(1));
		listFieldNames.add("AUTO_ALLOCATE_IND_CODE");
		listFieldValues.add(new Long(1));

		listFieldNames.add("AMOUNT");
		listFieldValues.add(new Double(strAmount));

		lReturn = ltc.fEV_PaymentInsert(new TreVarDate(), listFieldNames,
				listFieldValues);
		return lReturn;
	}

	public static Long searchAccountId(String strMDN) throws
			TreChainedException
	{
		String strCommmand = "SELECT b.account_id " +
				"  FROM ops$htctst1b.service_history_v a, ops$htctst1b.account_v b " +
				" WHERE a.service_name = '" + strMDN + "' " +
				"   AND a.service_type_id = 1000040 " +
				"   AND b.account_type_id = 10000 " +
				"   AND SYSDATE BETWEEN a.effective_start_date and a.effective_end_date " +
				"   AND a.service_status_code not in (9, 10) " +
				"   AND a.customer_node_id = b.customer_node_id ";
		List listFieldNames = new ArrayList();
		List listFieldValues = new ArrayList();
		List listReturn = ltc.biSQLQuery(strCommmand, listFieldNames,
				listFieldValues);
		return (Long) ((List) listReturn.get(0)).get(0);
	}

	public static Double searchAccountBalance(String strMDN) throws
			TreChainedException
	{
		String strCommmand = "SELECT b.account_balance " +
				"  FROM ops$htctst1b.service_history_v a, ops$htctst1b.account_v b " +
				" WHERE a.service_name = '" + strMDN + "' " +
				"   AND a.service_type_id = 1000040 " +
				"   AND b.account_type_id = 10000 " +
				"   AND SYSDATE BETWEEN a.effective_start_date and a.effective_end_date " +
				"   AND a.service_status_code not in (9, 10) " +
				"   AND a.customer_node_id = b.customer_node_id ";
		List listFieldNames = new ArrayList();
		List listFieldValues = new ArrayList();
		List listReturn = ltc.biSQLQuery(strCommmand, listFieldNames,
				listFieldValues);
		return (Double) ((List) listReturn.get(0)).get(0);
	}
}
