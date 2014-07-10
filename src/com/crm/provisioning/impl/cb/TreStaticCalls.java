package com.crm.provisioning.impl.cb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;

import com.adc.eji.tre.*;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author HungHM
 * @version 1.0
 */

public class TreStaticCalls extends TreConnection
{
	private static SimpleDateFormat	spDf	= new SimpleDateFormat("dd/MM/yyyy");

	public TreStaticCalls() throws TreChainedException
	{
		super();
	}

	public TreStaticCalls(Properties l_Properties) throws TreChainedException
	{
		super(l_Properties);
	}

	public static Date getCurrentDate() throws ParseException
	{
		return spDf.parse(spDf.format(new Date()));
	}

	public Long biServiceFetchByIdLong(Long lngServiceId,
										Date dtEffectiveDate,
										List arrFieldNames,
										List arrFieldValues) throws
			TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[4];
			parms[0] = lngServiceId;
			parms[1] = dtEffectiveDate;
			parms[2] = arrFieldNames;
			parms[3] = arrFieldValues;

			lretval = treVariantCall("biServiceFetchById&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public List biServiceFetchByName(String strServiceName,
										Date dtEffectiveDate, List lngParamNames) throws
			TreChainedException
	{
		Object lretval = null;
		try
		{
			Object[] parms = new Object[3];
			parms[0] = strServiceName;
			parms[1] = dtEffectiveDate;
			parms[2] = lngParamNames;

			lretval = treVariantCall("biServiceFetchByName?[]", new java.util.Date(), parms);
		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (List) lretval;
	}

	public List biServiceProducts(Long lngServiceId,
									Date dtEffectiveDate) throws
			TreChainedException
	{
		Object lretval = null;
		try
		{
			Object[] parms = new Object[2];
			parms[0] = lngServiceId;
			parms[1] = dtEffectiveDate;
			lretval = treVariantCall("biServiceProducts$[]",
										new java.util.Date(),
										parms);
		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (List) lretval;
	}

	public Long biProductInstanceSearch(
			Date dtEffectiveDate, String strWhereClause, String strOrderByClause
			, List productInstanceId, List lastModified)
			throws TreChainedException
	{
		Object lretval = null;
		try
		{
			Object[] parms = new Object[5];
			parms[0] = dtEffectiveDate;
			parms[1] = strWhereClause;
			parms[2] = strOrderByClause;
			parms[3] = productInstanceId;
			parms[4] = lastModified;

			lretval = treVariantCall("biProductInstanceSearch&", new java.util.Date(), parms);
		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biServiceSearch(Date dtEffectiveDate,
								String strWhereClause,
								String strOrderByClause,
								List arrFieldNames,
								List arrFieldValues) throws
			TreChainedException
	{
		Object lretval = null;
		try
		{
			Object[] parms = new Object[5];
			parms[0] = dtEffectiveDate;
			parms[1] = strWhereClause;
			parms[2] = strOrderByClause;
			parms[3] = arrFieldNames;
			parms[4] = arrFieldValues;

			lretval = treVariantCall("biServiceSearch&",
										new java.util.Date(),
										parms);
		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biCustomerNodeFetchByIdLong(Long lngCustomerNodeId,
											Date dtEffectiveDate,
											List arrFieldNames,
											List arrFieldValues) throws
			TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[4];
			parms[0] = lngCustomerNodeId;
			parms[1] = dtEffectiveDate;
			parms[2] = arrFieldNames;
			parms[3] = arrFieldValues;

			lretval = treVariantCall("biCustomerNodeFetchById&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biCustomerHierarchySearchLong(Date dtEffectiveDate,
												String strWhereClause,
												String strOrderByClause,
												Long lngHierarchySearch,
												List arrFieldNames,
												List arrFieldValues) throws
			TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[6];
			parms[0] = dtEffectiveDate;
			parms[1] = strWhereClause;
			parms[2] = strOrderByClause;
			parms[3] = lngHierarchySearch;
			parms[4] = arrFieldNames;
			parms[5] = arrFieldValues;

			lretval = treVariantCall("biCustomerHierarchySearch&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biAccountSearch(String strWhereClause,
								String strOrderByClause,
								List arrAccountId,
								List arrLastModified) throws
			TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[4];
			parms[0] = strWhereClause;
			parms[1] = strOrderByClause;
			parms[2] = arrAccountId;
			parms[3] = arrLastModified;

			lretval = treVariantCall("biAccountSearch&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biAccountFetchByIdLong(Long lngAccountId,
										List arrFieldNames,
										List arrFieldValues) throws
			TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[3];
			parms[0] = lngAccountId;
			parms[1] = arrFieldNames;
			parms[2] = arrFieldValues;

			lretval = treVariantCall("biAccountFetchById&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biCustomerNodeSearch(Date dtEffectiveDate,
										String strWhereClause,
										String strOrderByClause,
										List arrCustomerNodeId,
										List arrLastModified) throws
			TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[5];
			parms[0] = dtEffectiveDate;
			parms[1] = strWhereClause;
			parms[2] = strOrderByClause;
			parms[3] = arrCustomerNodeId;
			parms[4] = arrLastModified;

			lretval = treVariantCall("biCustomerNodeSearch&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biInvoiceSearch(String strWhereClause,
								String strOrderByClause,
								List arrInvoiceId,
								List arrLastModified) throws
			TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[4];
			parms[0] = strWhereClause;
			parms[1] = strOrderByClause;
			parms[2] = arrInvoiceId;
			parms[3] = arrLastModified;

			lretval = treVariantCall("biInvoiceSearch&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biInvoiceFetchByIdx(Long lngInvoiceId,
									List arrFieldNames,
									List arrFieldValues) throws
			TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[3];
			parms[0] = lngInvoiceId;
			parms[1] = arrFieldNames;
			parms[2] = arrFieldValues;

			lretval = treVariantCall("biInvoiceFetchByIdx&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biPaymentSearch(String strWhereClause,
								String strOrderByClause,
								List arrPaymentId,
								List arrLastModified) throws
			TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[4];
			parms[0] = strWhereClause;
			parms[1] = strOrderByClause;
			parms[2] = arrPaymentId;
			parms[3] = arrLastModified;

			lretval = treVariantCall("biPaymentSearch&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long biPaymentFetchById(Long lngPaymentId, List arrFieldNames, List arrFieldValues)
			throws TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[3];
			parms[0] = lngPaymentId;
			parms[1] = arrFieldNames;
			parms[2] = arrFieldValues;

			lretval = treVariantCall("biPaymentFetchById&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long fEV_PaymentInsert(TreVarDate treLastModified, List arrFieldNames, List arrFieldValues)
			throws TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[3];
			parms[0] = treLastModified.getValue();
			parms[1] = arrFieldNames;
			parms[2] = arrFieldValues;

			lretval = treVariantCall("fEV_PaymentInsert&",
										new java.util.Date(),
										parms);
			treLastModified.setValue((Date) parms[0]);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}
	
	public Long fEV_PaymentAdjust(TreVarDate treLastModified, List arrFieldNames, List arrFieldValues)
	    throws TreChainedException
	{
	    Object lretval = null;
	    try
	    {
	    	Object[] parms = new Object[3];
	    	parms[0] = treLastModified.getValue();
	    	parms[1] = arrFieldNames;
	    	parms[2] = arrFieldValues;

	    	lretval = treVariantCall("fEV_AdjustmentInsert&", 
	    								new Date(), 
	    								parms);
	    	treLastModified.setValue((Date)parms[0]);
	    }
	    catch (Throwable lth)
	    {
	    	handleThrowable(lth);
	    }
	    return (Long)lretval;
	}
	
	public Long fEV_PaymentDelete(Long paymentID) throws TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[1];
			parms[0] = paymentID;

			lretval = treVariantCall("fEV_PaymentDelete&",
										new java.util.Date(),
										parms);

		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public List biSQLQuery(String sqlCommand, List lngParamNames, List lngParamValue) throws TreChainedException
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[3];
			parms[0] = sqlCommand;
			parms[1] = lngParamNames;
			parms[2] = lngParamValue;

			lretval = treVariantCall("biSQLQuery?[]", new java.util.Date(), parms);
		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (List) lretval;
	}

	public Long biSQLQuery(
			String sqlCommand, List lngParamNames, List lngParamValue
			, Long lngFromRow, Long lngToRow, List lngOut)
			throws TreChainedException
	{
		Object lretval = null;
		try
		{
			Object[] parms = new Object[6];
			parms[0] = sqlCommand;
			parms[1] = lngParamNames;
			parms[2] = lngParamValue;
			parms[3] = lngFromRow;
			parms[4] = lngToRow;
			parms[5] = lngOut;
			lretval = treVariantCall("biSQLQuery&", new java.util.Date(), parms);
		}
		catch (Throwable lth)
		{
			handleThrowable(lth);
		}
		return (Long) lretval;
	}

	public Long fEV_ProdInstInsert(
			TreVarDate treStartDate, TreVarDate treLastModified, List arrFieldNames, List arrFieldValues)
			throws Exception
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[4];
			parms[0] = treStartDate.getValue();
			parms[1] = treLastModified.getValue();
			parms[2] = arrFieldNames;
			parms[3] = arrFieldValues;

			if (_logger.isDebugEnabled())
			{
				_logger.debug("ProductInstanceInsert");

				_logger.debug("arrFieldNames" + arrFieldNames.toString());
				_logger.debug("arrFieldValue" + arrFieldValues.toString());
			}

			lretval = treVariantCall("fEV_ProdInstInsert&", new java.util.Date(), parms);
			
			treStartDate.setValue((Date) parms[0]);
			treLastModified.setValue((Date) parms[1]);

		}
		catch (Throwable lth)
		{
			throw new Exception(lth);
		}
		return (Long) lretval;
	}

	public Long fEV_ProdInstComplete(
			TreVarDate treStartDate, Long lngProductInstanceId, TreVarDate treLastModified
			, List arrFromAccountTypeId, List arrFromAccountId)
			throws Exception
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[5];
			parms[0] = treStartDate.getValue();
			parms[1] = lngProductInstanceId;
			parms[2] = treLastModified.getValue();
			parms[3] = arrFromAccountTypeId;
			parms[4] = arrFromAccountId;

			lretval = treVariantCall("fEV_ProdInstComplete&",
										new java.util.Date(),
										parms);
			treStartDate.setValue((Date) parms[0]);
			treLastModified.setValue((Date) parms[2]);

		}
		catch (Throwable lth)
		{
			throw new Exception(lth);
		}
		return (Long) lretval;
	}

	public Long fEV_ProdInstUpdate(
			Long lProdInstId, TreVarDate effectiveDate, TreVarDate lastModified
			, List arrFieldNames, List arrFieldValues)
			throws TreChainedException, Exception
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[5];
			parms[0] = lProdInstId;
			parms[1] = effectiveDate.getValue();
			parms[2] = lastModified.getValue();
			parms[3] = arrFieldNames;
			parms[4] = arrFieldValues;

			lretval = treVariantCall("fEV_ProdInstUpdate&",
										new java.util.Date(),
										parms);
			lastModified.setValue((Date) parms[2]);
		}
		catch (Throwable lth)
		{
			throw new Exception(lth);
		}
		return (Long) lretval;
	}

	public Long fEV_PersonInsert(
			TreVarDate treStartDate, TreVarDate treLastModified, List arrFieldNames, List arrFieldValues)
			throws TreChainedException, Exception
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[4];
			parms[0] = treStartDate.getValue();
			parms[1] = treLastModified.getValue();
			parms[2] = arrFieldNames;
			parms[3] = arrFieldValues;

			lretval = treVariantCall("fEV_PersonInsert&", new java.util.Date(), parms);

			treStartDate.setValue((Date) parms[0]);
			treLastModified.setValue((Date) parms[1]);
		}
		catch (Throwable lth)
		{
			throw new Exception(lth);
		}

		return (Long) lretval;
	}

	public Long fEV_CustomerInsert(
			TreVarDate treStartDate, TreVarDate treLastModified, List arrFieldNames, List arrFieldValues)
			throws Exception
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[4];
			parms[0] = treStartDate.getValue();
			parms[1] = treLastModified.getValue();
			parms[2] = arrFieldNames;
			parms[3] = arrFieldValues;

			if (_logger.isDebugEnabled())
			{
				_logger.debug("CustomerInsert");

				_logger.debug("arrFieldNames" + arrFieldNames.toString());
				_logger.debug("arrFieldValue" + arrFieldValues.toString());
			}

			lretval = treVariantCall("fEV_CustomerInsert&", new java.util.Date(), parms);

			treStartDate.setValue((Date) parms[0]);
			treLastModified.setValue((Date) parms[1]);

		}
		catch (Throwable lth)
		{
			throw new Exception(lth);
		}
		return (Long) lretval;
	}

	public Long fEV_ServiceInsert(
			TreVarDate treStartDate, TreVarDate treLastModified, List arrFieldNames, List arrFieldValues)
			throws Exception
	{
		Object lretval = null;

		try
		{
			Object[] parms = new Object[4];
			parms[0] = treStartDate.getValue();
			parms[1] = treLastModified.getValue();
			parms[2] = arrFieldNames;
			parms[3] = arrFieldValues;

			if (_logger.isDebugEnabled())
			{
				_logger.debug("ServiceInsert");

				_logger.debug("arrFieldNames" + arrFieldNames.toString());
				_logger.debug("arrFieldValue" + arrFieldValues.toString());
			}

			lretval = treVariantCall("fEV_ServiceInsert&", new java.util.Date(), parms);

			treStartDate.setValue((Date) parms[0]);
			treLastModified.setValue((Date) parms[1]);
		}
		catch (Throwable lth)
		{
			throw new Exception(lth);
		}
		return (Long) lretval;
	}

	public long fEV_CustomerInsert(
			long personId, String firstName, String lastName, String province, String district) throws Exception
	{
		List arrFieldNames = new ArrayList(), arrFieldValue = new ArrayList();

		arrFieldNames.add("PERSON_ID"); // [0] =
		arrFieldNames.add("CUSTOMER_NODE_TYPE_ID"); // [1] =
		arrFieldNames.add("CUSTOMER_NODE_STATUS_CODE"); // [2] =
		arrFieldNames.add("REPORT_LEVEL_CODE"); // [3] =
		arrFieldNames.add("SITE_GENERAL_1"); // [4] =
		arrFieldNames.add("SITE_CITY"); // [5] =
		arrFieldNames.add("SITE_STATE"); // [6] =
		// arrFieldNames.add("PAYMENT_METHOD_CODE"); // [7] =
		// arrFieldNames.add("PAYMENT_LOCATION_CODE"); // [8] =
		arrFieldNames.add("NODE_NAME"); // [9] =
		arrFieldNames.add("GENERAL_5"); // [10] =
		arrFieldNames.add("POSTAL_GENERAL_4"); // [11] =
		arrFieldNames.add("POSTAL_GENERAL_3"); // [12] =
		arrFieldNames.add("POSTAL_LINE_2"); // [13] =
		arrFieldNames.add("POSTAL_SUBURB"); // [14] =
		arrFieldNames.add("POSTAL_STATE"); // [15] =
		arrFieldNames.add("POSTAL_GENERAL_1"); // [16] =
		arrFieldNames.add("POSTAL_CITY"); // [17] =
		arrFieldNames.add("SITE_GENERAL_4"); // [18] =
		arrFieldNames.add("SITE_GENERAL_3"); // [19] =
		arrFieldNames.add("SITE_LINE_1"); // [20] =
		arrFieldNames.add("SITE_LINE_2"); // [21] =
		arrFieldNames.add("SITE_SUBURB"); // [22] =
		// arrFieldNames.add("GENERAL_3"); // [23] =
		arrFieldNames.add("POSTAL_LINE_1"); // [24] =

		arrFieldValue.add(personId); // [0] =
		arrFieldValue.add(1000044L); // [1] =
		arrFieldValue.add(3L); // [2] =
		arrFieldValue.add(1L); // [3] =
		arrFieldValue.add("Northern"); // [4] =
		//arrFieldValue.add("QuÃƒÂ¢ÃŒÂ£n Ã„ï¿½ÃƒÂ´ÃŒï¿½ng Ã„ï¿½a"); // [5] =
		arrFieldValue.add(district); // [5] =
		// arrFieldValue.add("TP HÃƒÂ  NÃƒÂ´ÃŒÂ£i"); // [6] =
		arrFieldValue.add(province); // [6] =
		// arrFieldValue.add(3L); // [7] =
		// arrFieldValue.add(1L); // [8] =
		arrFieldValue.add(firstName + " " + lastName); // [9] =
		arrFieldValue.add(""); // [10] =
		arrFieldValue.add(""); // [11] =
		arrFieldValue.add(""); // [12] =
		arrFieldValue.add(""); // [13] =
		arrFieldValue.add("Trung Tu"); // [14] =
		arrFieldValue.add(province); // [15] =
		arrFieldValue.add("Northern"); // [16] =
		arrFieldValue.add(district); // [17] =
		arrFieldValue.add(""); // [18] =
		arrFieldValue.add(""); // [19] =
		arrFieldValue.add(""); // [20] =
		arrFieldValue.add(""); // [21] =
		arrFieldValue.add("Trung Tu"); // [22] =
		// arrFieldValue.add(""); // [23] =
		arrFieldValue.add(""); // [24] =

		TreVarDate treStartDate = new TreVarDate(getCurrentDate());
		TreVarDate treLastModified = new TreVarDate(new Date());

		return fEV_CustomerInsert(treStartDate, treLastModified, arrFieldNames, arrFieldValue);
	}

	public Long fEV_ProdInstInsert(Long customerNodeId) throws Exception
	{
		List arrFieldNames = new ArrayList(), arrFieldValue = new ArrayList();
		arrFieldNames.add("CUSTOMER_NODE_ID");
		arrFieldNames.add("PRODUCT_ID");

		arrFieldValue.add(customerNodeId);
		arrFieldValue.add(1000036L);

		TreVarDate treStartDate = new TreVarDate(getCurrentDate());
		TreVarDate treLastModified = new TreVarDate(new Date());

		return fEV_ProdInstInsert(treStartDate, treLastModified, arrFieldNames, arrFieldValue);
	}

	public Long fEV_ServiceInsert(
			Long personId, String msisdn, String imsi, String iccid, Long baseProductInstanceId) throws Exception
	{
		List arrFieldNames = new ArrayList(), arrFieldValue = new ArrayList();
		// arrFieldNames.add("CUSTOMER_NODE_ID");
		arrFieldNames.add("PERSON_ID");
		arrFieldNames.add("SERVICE_NAME");
		arrFieldNames.add("NETWORK_NAME");
		arrFieldNames.add("CREATION_ORDER");
		arrFieldNames.add("SERVICE_TYPE_ID");
		// arrFieldNames.add("PRODUCT_ID");
		// arrFieldNames.add("ACTIVE_DATE");
		arrFieldNames.add("BASE_PRODUCT_INSTANCE_ID");
		arrFieldNames.add("GENERAL_1");
		arrFieldNames.add("GENERAL_5");

		// arrFieldValue.add(70233L);
		arrFieldValue.add(personId);
		arrFieldValue.add(msisdn);
		arrFieldValue.add(imsi);
		arrFieldValue.add(1L);
		arrFieldValue.add(1000080L);
		// arrFieldValue.add(1000036L);
		// arrFieldValue.add(getCurrentDate());
		arrFieldValue.add(baseProductInstanceId);
		arrFieldValue.add(imsi);
		arrFieldValue.add(iccid);

		TreVarDate treStartDate = new TreVarDate(getCurrentDate());
		TreVarDate treLastModified = new TreVarDate(new Date());

		Long serviceid = fEV_ServiceInsert(treStartDate, treLastModified, arrFieldNames, arrFieldValue);

		Long completeid = fEV_ProdInstComplete(treStartDate, baseProductInstanceId, treLastModified, new ArrayList(), new ArrayList());

		return serviceid;
	}

	public Long fEV_PersonInsert(
			String msisdn, String firstName, String lastName, Date birthDate, long gender
			, String idCard, String province, String district) throws Exception
	{
		List arrFieldNames = new ArrayList(), arrFieldValue = new ArrayList();

		arrFieldNames.add("PERSON_TYPE_ID"); // [0] =
		arrFieldNames.add("HOME_STATE"); // [1] =
		arrFieldNames.add("HOME_CITY"); // [2] =
		arrFieldNames.add("HOME_SUBURB"); // [3] =
		arrFieldNames.add("POSTAL_STATE"); // [4] =
		arrFieldNames.add("POSTAL_CITY"); // [5] =
		arrFieldNames.add("POSTAL_SUBURB"); // [6] =
		arrFieldNames.add("FAMILY_NAME"); // [7] =
		arrFieldNames.add("GIVEN_NAMES"); // [8] =
		arrFieldNames.add("BIRTH_DATE"); // [9] =
		arrFieldNames.add("GENDER_CODE"); // [10] =
		arrFieldNames.add("OFFICIAL_NAME"); // [12] =
		arrFieldNames.add("MOBILE_PHONE_NR"); // [13] =
		arrFieldNames.add("SPOKEN_LANGUAGE_CODE"); // [14] =

		arrFieldValue.add(1000067L); // [0] =
		arrFieldValue.add(province); // [1] =
		arrFieldValue.add(district); // [2] =
		arrFieldValue.add("Trung Tu"); // [3] =
		arrFieldValue.add(province); // [4] =
		arrFieldValue.add(district); // [5] =
		arrFieldValue.add("Trung Tu"); // [6] =
		arrFieldValue.add(lastName); // [7] =
		arrFieldValue.add(firstName); // [8] =
		arrFieldValue.add(birthDate); // [9] =
		arrFieldValue.add(gender); // [10] =
		// arrFieldValue.add(0L); // [10] =
		arrFieldValue.add("Vietnamobile"); // [12] =
		arrFieldValue.add(msisdn); // [13] =
		arrFieldValue.add(2L); // [14] =

		if (idCard.startsWith("P"))
		{
			arrFieldNames.add("PASSPORT"); // [11] =
			arrFieldValue.add(idCard); // [11] =
		}
		else
		{
			arrFieldNames.add("SOCIAL_SECURITY_NUMBER"); // [11] =
			arrFieldValue.add(idCard); // [11] =
		}

		TreVarDate treStartDate = new TreVarDate(getCurrentDate()), treLastModified = new TreVarDate(new Date());

		return fEV_PersonInsert(treStartDate, treLastModified, arrFieldNames, arrFieldValue);
	}
	// DuyMB add loyalty 2012/01/19
	  public Long fEV_AdjustmentInsert(TreVarDate treLastModified, List arrFieldNames, List arrFieldValues) throws TreChainedException
	  {
	    Object lretval = null;
	    try
	    {
	      Object[] parms = new Object[3];
	      parms[0] = treLastModified.getValue();
	      parms[1] = arrFieldNames;
	      parms[2] = arrFieldValues;

	      lretval = treVariantCall("fEV_AdjustmentInsert&", 
	        new Date(), 
	        parms);
	      treLastModified.setValue((Date)parms[0]);
	    }
	    catch (Throwable lth)
	    {
	      handleThrowable(lth);
	    }
	    return (Long)lretval;
	  }
	// DuyMB add end loyalty 2012/01/19
	private static Logger	_logger	= Logger.getLogger(CBConnection.class);
}
