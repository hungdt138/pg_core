package com.crm.provisioning.impl.cb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import com.adc.eji.tre.TreChainedException;
import com.adc.eji.tre.TreVarDouble;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.adc.eji.tre.TreVarDate;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.CommandUtil;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class CBConnection extends ProvisioningConnection {
	public String charset = "";
	public String messageIndexFile = "";
	public String comment = "";

	public TreStaticCalls ltc = null;

	private static SimpleDateFormat spDf = new SimpleDateFormat("dd/MM/yyyy");

	public static final Long ACTIVE = 3L;
	public static final Long CANCELLATION_REQUIRED = 8L;
	public static final Long CANCELLED = 9L;
	public static final Long TEMPORARY_DISCONNECT_REQUIRED = 5L;
	public static final Long REACTIVATION_REQUIRED = 7L;

	public static final Long PREPAID_SERVICE_TYPE_ID = 1000080L;
	public static final Long POSTPAID_SERVICE_TYPE_ID = 1000040L;

	public CBConnection() throws Exception {
		super();
	}

	@Override
	public boolean openConnection() throws Exception {
		this.ltc = getConnection();
		return super.openConnection();
	}

	@Override
	public boolean closeConnection() throws Exception {
		try {
			disconnect();
		} catch (Exception ex) {
			throw ex;
		} finally {
			super.closeConnection();
		}
		
		return true;
	}

	public TreStaticCalls getConnection() throws Exception {
		TreStaticCalls ltc = null;

		try {
			Properties prop = new java.util.Properties();
			prop.setProperty("ThpHost", host);
			prop.setProperty("ThpPort", port + "");
			prop.setProperty("Charset", "UTF-8");
			prop.setProperty("MessageIndexFile", "ejimsgIndex.config");

			ltc = new TreStaticCalls(prop);
			ltc.treConnect(userName, password, "");

			return ltc;
		} catch (Exception e) {
			throw e;
		}
	}

	public void disconnect() {
		try {
			ltc.treDisconnect();
		} catch (TreChainedException ex) {
			ex.printStackTrace();
		}
	}

	public String getSchema(String schema) {
		if (schema.equals(""))
			return "ops$htcprd1b";
		else
			return schema;
	}

	public Long paymentInsert(String schema, String strMDN, String strAmount)
			throws Exception {
		Long lngAccountId = searchAccountId(schema, strMDN);

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

	// DuyMB add loyalty
	public Long paymentAdjust(String schema, String strMDN, String strAmount)
			throws Exception {
		Long lngAccountId = searchAccountId(schema, strMDN);

		ArrayList listFieldValues = new ArrayList();
		ArrayList listFieldNames = new ArrayList();
		Long lReturn = new Long(0L);

		listFieldNames.add("TO_ACCOUNT_ID");
		listFieldValues.add(lngAccountId);
		listFieldNames.add("ADJUSTMENT_TYPE_ID");
		listFieldValues.add(new Long(10000L));
		listFieldNames.add("CURRENCY_ID");
		listFieldValues.add(new Long(1L));

		listFieldNames.add("AUTO_ALLOCATE_IND_CODE");
		listFieldValues.add(new Long(1L));
		listFieldNames.add("AMOUNT");
		listFieldValues.add(new Double(strAmount));

		listFieldNames.add("DESCRIPTION");
		listFieldValues.add(new String(
				"Redeem points from Loyalty system for Postpaid Payment."));

		lReturn = this.ltc.fEV_PaymentAdjust(new TreVarDate(), listFieldNames,
				listFieldValues);

		return lReturn;
	}

	// DuyMB add end loyalty
	public Long searchAccountId(String schema, String strMDN) throws Exception {
		schema = getSchema(schema);

		String strCommmand = "SELECT b.account_id "
				+ "  FROM "
				+ schema
				+ ".service_history_v a, "
				+ schema
				+ ".account_v b "
				+ " WHERE a.service_name = '"
				+ strMDN
				+ "' "
				+ "   AND a.service_type_id = 1000040 "
				+ "   AND b.account_type_id = 10000 "
				+ "   AND SYSDATE BETWEEN a.effective_start_date and a.effective_end_date "
				+ "   AND a.service_status_code not in (9, 10) "
				+ "   AND a.customer_node_id = b.customer_node_id ";

		List listFieldNames = new ArrayList();
		List listFieldValues = new ArrayList();

		List listReturn = ltc.biSQLQuery(strCommmand, listFieldNames,
				listFieldValues);

		if ((listReturn != null) && (listReturn.size() > 0)) {
			return (Long) ((List) listReturn.get(0)).get(0);
		} else {
			return (long) -1;
		}
	}

	public Double searchAccountBalance(String schema, String strMDN)
			throws TreChainedException {
		schema = getSchema(schema);

		String strCommmand = "SELECT b.account_balance "
				+ "  FROM "
				+ schema
				+ ".service_history_v a, "
				+ schema
				+ ".account_v b "
				+ " WHERE a.service_name = '"
				+ strMDN
				+ "' "
				+ "   AND a.service_type_id = 1000040 "
				+ "   AND b.account_type_id = 10000 "
				+ "   AND SYSDATE BETWEEN a.effective_start_date and a.effective_end_date "
				+ "   AND a.service_status_code not in (9, 10) "
				+ "   AND a.customer_node_id = b.customer_node_id ";

		List listFieldNames = new ArrayList();
		List listFieldValues = new ArrayList();

		List listReturn = ltc.biSQLQuery(strCommmand, listFieldNames,
				listFieldValues);

		if ((listReturn != null) && (listReturn.size() > 0)) {
			return (Double) ((List) listReturn.get(0)).get(0);
		} else {
			return (double) -1;
		}
	}

	public long getServiceStatus(String schema, Long serviceTypeId,
			Long productId, String msisdn, Properties prtResponse)
			throws TreChainedException {
		schema = getSchema(schema);

		String strCommmand = "Select 	service_name, b.customer_node_id, b.product_instance_id, "
				+ "				b.product_instance_status_code, b.base_product_instance_id "
				+ "	From "
				+ schema
				+ ".service_history_v a, "
				+ schema
				+ ".product_instance_cm_v b "
				+
				// " WHERE a.customer_node_id = b.customer_node_id " + // DuyMB
				// del fix bug 2011/11/24
				" WHERE a.PRODUCT_INSTANCE_ID = b.base_product_instance_id "
				+ // DuyMB
					// add
					// fix
					// bug
					// 2011/11/24
				"   AND sysdate BETWEEN a.effective_start_date AND a.effective_end_date "
				+ "   AND sysdate BETWEEN b.effective_start_date AND b.effective_end_date "
				+ "   AND a.service_type_id = "
				+ serviceTypeId
				+ "   AND a.service_status_code not in (9, 10,101,100,0) "
				+ "   AND b.product_instance_status_code not in (9, 10,101,0,100) "
				+ "   AND a.service_name = '"
				+ msisdn
				+ "' "
				+ "   AND b.product_id =  " + productId;

		List listFieldNames = new ArrayList();
		List listFieldValues = new ArrayList();

		List listReturn = ltc.biSQLQuery(strCommmand, listFieldNames,
				listFieldValues);

		if ((listReturn != null) && (listReturn.size() > 0)) {
			prtResponse.setProperty("customerNodeId",
					String.valueOf(((List) listReturn.get(0)).get(1)));
			prtResponse.setProperty("productInstanceId",
					String.valueOf(((List) listReturn.get(0)).get(2)));
			prtResponse.setProperty("productStatus",
					String.valueOf(((List) listReturn.get(0)).get(3)));
			prtResponse.setProperty("baseProductInstanceId",
					String.valueOf(((List) listReturn.get(0)).get(4)));

			return (Long) ((List) listReturn.get(0)).get(3);
		} else {
			return (long) -1;
		}
	}

	public long getPostpaidServiceStatus(String schema, Long productId,
			String msisdn, Properties properties) throws TreChainedException {
		return getServiceStatus(schema, 1000040L, productId, msisdn, properties);
	}

	public long getPrepaidServiceStatus(String schema, Long productId,
			String msisdn, Properties properties) throws TreChainedException {
		return getServiceStatus(schema, 1000080L, productId, msisdn, properties);
	}

	public long getPostpaidStatus(String isdn, Properties prtResponse) throws TreChainedException

	{
		if(!isdn.equals("") && isdn.startsWith(Constants.COUNTRY_CODE))
		{
			isdn = isdn.substring(Constants.COUNTRY_CODE.length());
			isdn = Constants.DOMESTIC_CODE + isdn;
		}
		String strCommmand = "Select to_char(sh.active_date,'YYYY-MM-DD HH24:MI:SS')  from service_history sh where sh.service_name = "+ isdn+ " "
								+ " and sysdate between sh.effective_start_date and sh.effective_end_date " 
								+ " and sh.service_status_code = 3" 
								+ " and sh.service_type_id = 1000040";

		List listFieldNames = new ArrayList();

		List listFieldValues = new ArrayList();

		List listReturn = ltc.biSQLQuery(strCommmand, listFieldNames,
				listFieldValues);

		if ((listReturn != null) && (listReturn.size() > 0))
		{
			prtResponse.setProperty("active_date",
					String.valueOf(((List) listReturn.get(0)).get(0)));
			return (long) 3;
		}
		else
		{
			return (long) -1;
		}

	}

	public Long getBaseProductInstanceId(String schema, String msisdn,
			Long serviceTypeId) throws Exception {
		schema = getSchema(schema);

		String strCommmand = "Select 	service_name, b.customer_node_id, b.product_instance_id, "
				+ "				b.product_instance_status_code, b.base_product_instance_id "
				+ "	From	 "
				+ schema
				+ ".service_history_v a, "
				+ schema
				+ ".product_instance_cm_v b "
				+ " WHERE a.customer_node_id = b.customer_node_id "
				+ "   AND sysdate BETWEEN a.effective_start_date AND a.effective_end_date "
				+ "   AND sysdate BETWEEN b.effective_start_date AND b.effective_end_date "
				+ "   AND a.service_type_id = "
				+ serviceTypeId
				+ "   AND a.service_status_code not in (9, 10, 101, 100) "
				+ "   AND b.product_instance_status_code not in (9, 10, 101, 100) "
				+ "   AND a.service_name = '"
				+ msisdn
				+ "' "
				+ "   AND b.base_product_instance_id is null ";

		List listFieldNames = new ArrayList(), listFieldValues = new ArrayList();

		List listReturn = ltc.biSQLQuery(strCommmand, listFieldNames,
				listFieldValues);

		if ((listReturn != null) && (listReturn.size() > 0)) {
			return (Long) ((List) listReturn.get(0)).get(2);
		} else {
			return (long) -1;
		}
	}

	@SuppressWarnings("finally")
	public Long getBaseProductInstanceId(String isdn, long productId)
			throws Exception {
		Long baseProductInstanceId = -1L;

		try {
			List arrFieldNames = new ArrayList(), arrFieldValue = new ArrayList();

			arrFieldNames.add("CUSTOMER_NODE_ID");
			arrFieldNames.add("PRODUCT_INSTANCE_ID");
			Date effectiveDate = new Date();

			List listService = ltc.biServiceFetchByName(isdn, effectiveDate,
					arrFieldNames);
			List resultService = (List) listService.get(0);

			Long lCustomerNodeId = (Long) resultService.get(0);
			Long lBaseProdInstId = (Long) resultService.get(1);

			List lProductInstanceId = new ArrayList<Object>();
			List lLastModified = new ArrayList();

			ltc.biProductInstanceSearch(new Date(), "CUSTOMER_NODE_ID = "
					+ lCustomerNodeId + " AND PRODUCT_ID = " + productId
					+ " AND BASE_PRODUCT_INSTANCE_ID = " + lBaseProdInstId
					+ " AND PRODUCT_INSTANCE_STATUS_CODE = 3",
					"CUSTOMER_NODE_ID", lProductInstanceId, lLastModified);

			arrFieldNames = new ArrayList();

			if ((lProductInstanceId != null) && (lProductInstanceId.size() > 0)) {
				baseProductInstanceId = (Long) lProductInstanceId.get(0);
			}
		} catch (Exception e) {
			throw e;
		}

		return baseProductInstanceId;
	}

	public Long createPrepaidSubscriber(String msisdn, CommandMessage request,
			Properties prtResponse) throws Exception {
		String firstName = request.getParameters().getProperty("firstName",
				"noname");
		String lastName = request.getParameters().getProperty("lastName",
				"nolastname");
		String idCard = request.getParameters().getProperty("idCard",
				"12345678");
		String gender = request.getParameters().getProperty("gender", "0");

		String birthdate = request.getParameters().getProperty("birthdate", "");

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -20);

		if (!birthdate.equals("")) {
			calendar.setTimeInMillis(Long.valueOf(birthdate));
		}

		String imsi = request.getParameters().getProperty("imsi", "");
		String iccid = request.getParameters().getProperty("iccid", "");

		// get province and district
		String province = "";
		String district = "";

		String strCommmand = "select a.abbreviation from reference_code a where a.reference_type_id = 1000060 and a.reference_code = 59";
		List listFieldNames = new ArrayList(), listFieldValues = new ArrayList();
		List listReturn = ltc.biSQLQuery(strCommmand, listFieldNames,
				listFieldValues);

		if ((listReturn != null) && (listReturn.size() > 0)) {
			province = (String) ((List) listReturn.get(0)).get(0);
		}

		strCommmand = "select a.abbreviation from reference_code a where a.reference_type_id = 1000210 and a.reference_code = 20003";
		listReturn = ltc.biSQLQuery(strCommmand, listFieldNames,
				listFieldValues);

		if ((listReturn != null) && (listReturn.size() > 0)) {
			district = (String) ((List) listReturn.get(0)).get(0);
		}

		if (log.isDebugEnabled()) {
			log.debug("Insert new person for prepaid subscriber " + msisdn);
			log.debug("firstName = " + firstName + " | lastName = " + lastName
					+ " | birthDate = " + calendar.getTime() + " | idCard = "
					+ idCard + " | province = " + province + " | district = "
					+ district);
		}

		Long personId;
		try {
			personId = ltc.fEV_PersonInsert(msisdn, firstName, lastName,
					calendar.getTime(), Long.valueOf(gender), idCard, province,
					district);
		} catch (Exception ex) {
			log.debug("fEV_PersonInsert:" + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
		if (log.isDebugEnabled()) {
			log.debug("Person is created with personId = " + personId);
		}

		prtResponse.setProperty("personId", String.valueOf(personId));
		Long customerNodeId;
		try {
			customerNodeId = ltc.fEV_CustomerInsert(personId, firstName,
					lastName, province, district);
		} catch (Exception ex2) {
			log.debug("fEV_CustomerInsert: " + ex2.getMessage());
			throw ex2;
		}
		if (log.isDebugEnabled()) {
			log.debug("CustomerNode is created with customerNodeId = "
					+ customerNodeId);
		}

		prtResponse.setProperty("customerNodeId",
				String.valueOf(customerNodeId));

		Long baseProductInstanceId;
		try {
			baseProductInstanceId = ltc.fEV_ProdInstInsert(customerNodeId);
		} catch (Exception ex3) {
			ex3.printStackTrace();
			throw ex3;
		}

		prtResponse.setProperty("baseProductInstanceId",
				String.valueOf(baseProductInstanceId));

		if (log.isDebugEnabled()) {
			log.debug("Base product instance is created with baseProductInstanceId = "
					+ baseProductInstanceId);
		}
		Long serviceId;
		try {
			serviceId = ltc.fEV_ServiceInsert(personId, msisdn, imsi, iccid,
					baseProductInstanceId);
		} catch (Exception ex1) {
			log.debug("fEV_ServiceInsert: " + ex1.getMessage());
			ex1.printStackTrace();
			throw ex1;
		}

		if (log.isDebugEnabled()) {
			log.debug("Service is created with serviceId = " + serviceId);
		}

		return (Long) serviceId;
	}

	@SuppressWarnings({ "finally", "unchecked", "rawtypes" })
	public String registerService(String isdn, long productId,
			List advancedFields, ArrayList advancedValues) throws Exception {
		String strSuccess = "";

		try {
			List arrFieldNames = new ArrayList(), arrFieldValue = new ArrayList();

			Date effectiveDate = new Date();
			arrFieldNames.add("CUSTOMER_NODE_ID");
			arrFieldNames.add("PRODUCT_INSTANCE_ID");
			List listService = ltc.biServiceFetchByName(isdn, effectiveDate,
					arrFieldNames);

			List resultService = (List) listService.get(0);
			Long lCustomerNodeId = (Long) resultService.get(0);
			Long lProdInstId = (Long) resultService.get(1);

			arrFieldNames = new ArrayList();
			arrFieldNames.add("PRODUCT_ID");
			arrFieldValue.add(productId);
			arrFieldNames.add("CUSTOMER_NODE_ID");
			arrFieldValue.add(lCustomerNodeId);
			arrFieldNames.add("BASE_PRODUCT_INSTANCE_ID");
			arrFieldValue.add(lProdInstId);

			if ((advancedFields != null) && (advancedValues != null)) {
				arrFieldNames.addAll(advancedFields);
				arrFieldValue.addAll(advancedValues);
			}

			// arrFieldNames.add("GENERAL_2");
			// arrFieldValue.add("1");
			// arrFieldNames.add("PRODUCT_INSTANCE_STATUS_CODE");
			// arrFieldValue.add(ACTIVE);
			TreVarDate treStartDate = new TreVarDate(getCurrentDate());
			TreVarDate treLastModified = new TreVarDate(new Date());

			ltc.fEV_ProdInstInsert(treStartDate, treLastModified,
					arrFieldNames, arrFieldValue);

			strSuccess = "success";
		} catch (TreChainedException ex) {
			throw ex;
		} catch (Exception ex) {
			throw ex;
		}

		return strSuccess;
	}

	public String registerService(String isdn, long productId) throws Exception {
		return registerService(isdn, productId, null, null);
	}

	public static Date getCurrentDate() throws ParseException {
		return spDf.parse(spDf.format(new Date()));
	}

	@SuppressWarnings("finally")
	public String unregisterService(String isdn, long productId, long cancelMode)
			throws Exception {
		String strSuccess = "";

		try {
			List arrFieldNames = new ArrayList();
			List arrFieldValue = new ArrayList();

			arrFieldNames.add("CUSTOMER_NODE_ID");
			arrFieldNames.add("PRODUCT_INSTANCE_ID");
			Date effectiveDate = new Date();

			List listService = ltc.biServiceFetchByName(isdn, effectiveDate,
					arrFieldNames);
			List resultService = (List) listService.get(0);

			Long lCustomerNodeId = (Long) resultService.get(0);
			Long lBaseProdInstId = (Long) resultService.get(1);

			List lProductInstanceId = new ArrayList<Object>();
			List lLastModified = new ArrayList();

			// 6: TEMPORARAY_DISCONNECT, 3: ACTIVE
			ltc.biProductInstanceSearch(new Date(), "CUSTOMER_NODE_ID = "
					+ lCustomerNodeId + " AND PRODUCT_ID = " + productId
					+ " AND BASE_PRODUCT_INSTANCE_ID = " + lBaseProdInstId
					+ " AND PRODUCT_INSTANCE_STATUS_CODE IN (3,6,2002)",
					"CUSTOMER_NODE_ID", lProductInstanceId, lLastModified);

			arrFieldNames = new ArrayList();

			TreVarDate tEffectiveDate = new TreVarDate(getCurrentDate());

			if (lProductInstanceId.size() > 0) {
				TreVarDate lastModified = new TreVarDate(
						(Date) lLastModified.get(0));
				Long lProdInstId = (Long) lProductInstanceId.get(0);

				arrFieldNames.add("PRODUCT_INSTANCE_STATUS_CODE");
				// arrFieldValue.add(CANCELLED);
				arrFieldValue.add(cancelMode);
				arrFieldNames.add("EFFECTIVE_START_DATE");
				arrFieldValue.add(getCurrentDate());

				ltc.fEV_ProdInstUpdate(lProdInstId, tEffectiveDate,
						lastModified, arrFieldNames, arrFieldValue);

				strSuccess = "success";
			} else {
				strSuccess = "instance-not-found";
			}
		} catch (TreChainedException ex) {
			strSuccess = "fail";
			throw ex;
		} catch (Exception ex) {
			strSuccess = "fail";
			throw ex;
		} finally {
			return strSuccess;
		}

	}

	public String unregisterService(String isdn, long productId)
			throws Exception {
		return unregisterService(isdn, productId, CANCELLATION_REQUIRED);
	}

	// test.
	public static int getSubscriberInfo(String isdn, CommandMessage request)
			throws Exception {

		CallableStatement cstmt = null;
		Connection connection = null;

		try {
			connection = Database.getConnection();

			// isdn = request.getSourceAddress();

			if (!isdn.equals("") && isdn.startsWith("0")) {
				isdn = isdn.substring(1);
			}
			if (!isdn.equals("") && isdn.startsWith("84")) {
				isdn = isdn.substring(2);
			}

			String call = "{call get_sub_info(?, ?, ?, ?, ?, ?, ?, ?) }";

			// cstmt = _instance.getConnection().prepareCall(call);
			cstmt = connection.prepareCall(call);

			cstmt.setQueryTimeout(1800);
			cstmt.registerOutParameter(2, Types.VARCHAR);
			cstmt.registerOutParameter(3, Types.VARCHAR);
			cstmt.registerOutParameter(4, Types.VARCHAR);
			cstmt.registerOutParameter(5, Types.VARCHAR);
			cstmt.registerOutParameter(6, Types.DATE);
			cstmt.registerOutParameter(7, Types.VARCHAR);
			cstmt.registerOutParameter(8, Types.VARCHAR);

			cstmt.setString(1, isdn);

			cstmt.execute();

			request.getParameters().setProperty("iccid",
					String.valueOf(cstmt.getString(2)));
			request.getParameters().setProperty("imsi",
					String.valueOf(cstmt.getString(3)));
			request.getParameters().setProperty("firstName",
					String.valueOf(cstmt.getString(4)));
			request.getParameters().setProperty("lastName",
					String.valueOf(cstmt.getString(5)));
			request.getParameters().setProperty("idCard",
					String.valueOf(cstmt.getString(7)));
			request.getParameters().setProperty("gender",
					String.valueOf(cstmt.getString(8)));

			java.sql.Date birthDate = cstmt.getDate(6);
			if (birthDate != null) {
				request.getParameters().setProperty("birthdate",
						String.valueOf(birthDate.getTime()));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.closeObject(cstmt);
			Database.closeObject(connection);
		}

		return 1;
	}

	// DuyMB add loyalty 2012/01/19
	public Long adjustmentInsert(String schema, String strMDN,
			String strAmount, String comment) throws Exception {
		Long lngAccountId = searchAccountId(schema, strMDN);

		ArrayList listFieldValues = new ArrayList();
		ArrayList listFieldNames = new ArrayList();
		Long lReturn = new Long(0L);

		listFieldNames.add("TO_ACCOUNT_ID");
		listFieldValues.add(lngAccountId);
		listFieldNames.add("PAYMENT_TYPE_ID");
		listFieldValues.add(new Long(500001L));
		listFieldNames.add("CURRENCY_ID");
		listFieldValues.add(new Long(1L));
		listFieldNames.add("PAYMENT_LOCATION_CODE");
		listFieldValues.add(new Long(1L));
		listFieldNames.add("AUTO_ALLOCATE_IND_CODE");
		listFieldValues.add(new Long(1L));
		listFieldNames.add("AMOUNT");
		listFieldValues.add(new Double(strAmount));

		listFieldNames.add("ADJUSTMENT_TYPE_ID");
		listFieldValues.add(new Long(10000L));
		listFieldNames.add("DESCRIPTION");
		listFieldValues.add(comment);

		lReturn = this.ltc.fEV_AdjustmentInsert(new TreVarDate(),
				listFieldNames, listFieldValues);

		return lReturn;
	}

	// DuyMB add end 2012/01/19.
	// NamTA edited 13/07/2012
	public void checkBalancePostpaid(String isdn, double[] balances)
			throws Exception {

		TreVarDouble l_outstandingAmount = new TreVarDouble();
		TreVarDouble l_currentUnbilledAmount = new TreVarDouble();
		TreVarDouble l_lastPaymentAmount = new TreVarDouble();

		try {
			boolean success = IVR_fGetAccountDetails(isdn, l_outstandingAmount,
					l_currentUnbilledAmount, l_lastPaymentAmount);
			if (!success) {
				log.warn("Can not find OutstandingAmount for :" + isdn);
				return;
			}
		} catch (Exception ex) {
			log.error("Error when query Postpaid infor: ", ex);
		}

		balances[0] = l_outstandingAmount.doubleValue();
		balances[1] = l_currentUnbilledAmount.doubleValue();
		balances[2] = l_lastPaymentAmount.doubleValue();
	}

	public boolean IVR_fGetAccountDetails(String isdn,
			TreVarDouble l_outstandingAmount,
			TreVarDouble l_currentUnbilledAmount,
			TreVarDouble l_lastPaymentAmount) throws TreChainedException {
		Object[] p;
		p = new Object[6];
		p[0] = isdn;
		p[1] = new Double(0);
		p[2] = new Date(0);
		p[3] = new Double(0);
		p[4] = new Date(0);
		p[5] = new Double(0);
		Long x;

		x = (Long) this.ltc.treVariantCall("IVR.fGetAccountDetails&",
				new Date(), p);
		l_outstandingAmount.setValue((Double) p[1]);
		l_currentUnbilledAmount.setValue((Double) p[3]);
		l_lastPaymentAmount.setValue((Double) p[5]);

		return x.longValue() != 0;
	}

	private String transformISDN(String isdn) throws Exception {
		isdn = CommandUtil.addCountryCode(isdn);
		if (isdn.startsWith(Constants.COUNTRY_CODE)) {
			isdn = isdn.replaceFirst(Constants.COUNTRY_CODE,
					Constants.DOMESTIC_CODE);
		}
		return isdn;
	}

	private List<Object> doBiSQLQueryRWx(String query, Long startIndex,
			Long count) throws TreChainedException {
		Object[] param = new Object[6];
		param[0] = query;
		param[1] = null;
		param[2] = null;
		param[3] = startIndex;
		param[4] = count;
		param[5] = null;

		ltc.treVariantCall("biSQLQueryRWx&", new java.util.Date(), param);

		return (List<Object>) param[5];
	}

	public List<List<Object>> getSubscriberWithHistory(String schema,
			String isdn, Date startTime, Date endTime) throws Exception {
		schema = getSchema(schema);
		String cbIsdn = transformISDN(isdn);
		String sStartTime = (new SimpleDateFormat("yyyyMMddHHmmss"))
				.format(startTime);
		String sEndTime = (new SimpleDateFormat("yyyyMMddHHmmss"))
				.format(endTime);
		String query = "Select count(a.A_PARTY_ID) "
				+ " from "
				+ schema
				+ ".call_detail_v a, "
				+ schema
				+ ".service_tre_v b where "
				+ " a.service_id = b.service_id"
				+ " b.service_name = "
				+ cbIsdn
				+ "  and to_char(a.CHARGE_START_DATE,'yyyymmddhh24miss') between "
				+ sStartTime + " and " + sEndTime;
		List<Object> counts = doBiSQLQueryRWx(query, 1L, 2L);
		String sCount = "0";
		if (counts != null)
			for (int i = 0; i < counts.size(); i++) {
				sCount = (Double) counts.get(0) + "";
				break;
			}

		if (sCount.indexOf(".") != -1)
			sCount = sCount.substring(0, sCount.indexOf("."));
		Long count = Long.parseLong(sCount);

		query = "Select "
				+ " a.A_PARTY_ID , a.B_PARTY_ID ,"
				+ " a.C_PARTY_ID ,"
				+ " a.CHARGE_START_DATE ,"
				+ " a.DURATION  , a.INVOICE_TXT "
				+ " from "
				+ schema
				+ ".call_detail_v a, "
				+ schema
				+ ".service_tre_v b where "
				+ " a.service_id = b.service_id"
				+ " b.service_name = "
				+ cbIsdn
				+ "  and to_char(a.CHARGE_START_DATE,'yyyymmddhh24miss') between "
				+ sStartTime + " and " + sEndTime;

		List<Object> records = doBiSQLQueryRWx(query, 1L, count);
		List<List<Object>> results = new ArrayList<List<Object>>();
		if (records != null) {
			// INTEC stored each call to 3 records: record 1, record 1, record
			// 1,
			// record 2, record 2, record 2, ...
			for (int i = 0; i < records.size(); i++) {
				if (i % 3 == 0) {
					ArrayList<Object> record = (ArrayList<Object>) records
							.get(i);

					List<Object> result = (ArrayList<Object>) record.clone();
					Date chargeStartDate = (Date) result.get(3);
					result.set(3, (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"))
							.format(chargeStartDate));
					results.add(result);
				}
			}
		}
		return results;
	}

	// 2012-09-24 MinhDT ADD END: Add product VB220
	public long getTimeUsed(String schema, String isdn, String registerDate)
			throws TreChainedException {
		try {
			schema = getSchema(schema);

			String query = "select sum(ceil(nv.duration/60)) duration_in_minute "
					+ "from "
					+ schema
					+ ".normalised_event nv "
					+ "where nv.event_type_code = 1013 "
					+ "and nv.event_sub_type_code = 2601 "
					+ "and nv.c_party_id = '"
					+ isdn
					+ "' "
					+ "and nv.charge_start_date >= TO_DATE('"
					+ registerDate
					+ "', 'SYYYY-MM-DD HH24:MI:SS', 'NLS_CALENDAR=GREGORIAN')";

			List<Object> counts = doBiSQLQueryRWx(query, 1L, 2L);
			String sCount = "0";
			if (counts != null)
				for (int i = 0; i < counts.size(); i++) {
					sCount = (Double) counts.get(0) + "";
					break;
				}

			return Long.parseLong(sCount);
		} catch (TreChainedException e) {
			return (long) -1;
		} catch (Exception e) {
			return (long) -1;
		}
	}

	// 2012-09-24 MinhDT ADD END: Add product VB220

	private static Logger log = Logger.getLogger(CBConnection.class);

	public static void main(String args[]) {
		String strHost = "10.8.8.10";
		int intPort = 30315;
		String strUser = "POS";
		String strPass = "abcd1234";
		List advancedFields = new ArrayList();
		ArrayList advancedValues = new ArrayList();

		// // Voice mail
		// advancedFields.add("PRODUCT_INSTANCE_STATUS_CODE");
		// advancedValues.add("3L");
		// try
		// {
		// CBConnection obj = new CBConnection(strHost, strPort, strUser,
		// strPass);
		// // System.out.println(obj.unregisterService("0922000511",
		// 20000161,CANCELLED));
		// System.out.println(obj.registerService("0922000511",
		// 20000161,advancedFields,advancedValues));
		// //System.out.println(obj.registerService("0923361020"));
		// }
		// catch (Exception ex)
		// {
		// ex.printStackTrace();
		// }
		// Voice mail
		// advancedFields.add("GENERAL_2");
		// advancedValues.add("1");
		// advancedFields.add("PRODUCT_INSTANCE_STATUS_CODE");
		// advancedValues.add("3");
		try {
			CBConnection obj = new CBConnection();
			obj.setHost(strHost);
			obj.setPort(intPort);
			obj.setUserName(strUser);
			obj.setPassword(strPass);
			obj.openConnection();
			// obj.getBaseProductInstanceId("ops$htctst1b", "0922000511",
			// POSTPAID_SERVICE_TYPE_ID);
			// List<List<Object>> results =
			// obj.getSubscriberWithHistory("ops$htctstd1b", "0922000511",
			// spDf.parse("25/06/2012"),
			// spDf.parse("10/07/2012"));
			// System.out.println(obj.unregisterService("0922000511", 20000161,
			// CBConnection.CANCELLED));
			//
			// System.out.println(Long.valueOf("2L".substring(0,
			// "2L".length()-1)));
			// System.out.println(obj.registerService("0922000511",
			// 20000161,advancedFields,advancedValues));
			// System.out.println(obj.registerService("0923361020"));

			//String result = obj.unregisterService("0922000511", 20000221L, 9L);
			Properties prtResponse = new Properties();
			long result = obj.getPostpaidStatus("84922000511", prtResponse);
//			System.out.println(result);
			obj.closeConnection();
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:MM:ss");
			String currentDate = df.format(date);
//			System.out.println(prtResponse.getProperty("active_date"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
