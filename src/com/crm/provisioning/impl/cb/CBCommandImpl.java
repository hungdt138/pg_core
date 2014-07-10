/**
 * 
 */
package com.crm.provisioning.impl.cb;

import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.crm.kernel.sql.Database;

import org.apache.log4j.Logger;

import com.adc.eji.tre.TreChainedException;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.SubscriberEntryImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.thread.DispatcherThread;
import com.crm.util.AppProperties;
import com.crm.util.GeneratorSeq;
import com.fss.util.AppException;
import com.fss.util.StringUtil;
import com.crm.kernel.message.Constants;

/**
 * @author ThangPV
 * 
 */
public class CBCommandImpl extends CommandImpl {
	public String transformISDN(String isdn) throws Exception {
		isdn = CommandUtil.addCountryCode(isdn);
		if (isdn.startsWith(Constants.COUNTRY_CODE)) {
			isdn = isdn.replaceFirst(Constants.COUNTRY_CODE,
					Constants.DOMESTIC_CODE);
		}
		return isdn;
	}

	// public String getSchema() throws Exception
	// {
	// return getProperty("schema", "ops$htcprd1b");
	// }

	public String getBillingStatus(Long status) throws Exception {
		return String.valueOf(Constants.SUPPLIER_CANCEL_STATUS);
	}

	// public String getCancelMode() throws Exception
	// {
	// return getProperty("postpaid.cancelMode", "");
	// }

	public void buildParameters(CommandInstance instance, String productId,
			String prefix, List names, List values) throws Exception {
		ProductEntry product = ProductFactory.getCache().getProduct(productId);
		AppProperties properties = product.getParameters();

		Enumeration<?> propNames = properties.propertyNames();

		if (!prefix.endsWith(".")) {
			prefix = prefix + ".";
		}

		for (; propNames.hasMoreElements();) {
			String strKey = (String) propNames.nextElement();

			if (strKey.startsWith(prefix)) {
				names.add(strKey.substring(prefix.length()));

				String value = properties.getProperty(strKey);

				if (value.endsWith("L")) {
					values.add(Long.valueOf(value.substring(0,
							value.length() - 1)));
				} else {
					values.add(value);
				}
			}
		}

	}

	public String getErrorCode(TreChainedException e) throws Exception {
		String error = e.getMessage();

		if (error.startsWith("<")) {
			error = error.substring("<".length());
			error = error.substring(0, error.indexOf(">"));

			return error;
		} else
			throw new Exception(e.getMessage());
	}

	public long getProductId(CommandInstance instance, CommandMessage request)
			throws Exception {
		long productId = 0;

		try {
			ProductEntry productEntry = ProductFactory.getCache().getProduct(
					request.getProductId());

			AppProperties properties = productEntry.getParameters();

			productId = Long.valueOf(properties.getProperty(
					"postpaidProductId", "0"));
		} catch (Exception e) {
			throw new Exception("invalid-product-id");
		}

		return productId;
	}

	/**
	 * Check balance information Postpaid.
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 */
	public VNMMessage checkBalance(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);
		if (instance.getDebugMode().equals("depend")) {
			simulation(instance, provisioningCommand, result);
		} else {
			try {
				double[] balances = new double[3];
				CBConnection connection = (CBConnection) instance
						.getProvisioningConnection();

				/**
				 * Add log request/response NamTA 21/08/2012
				 */
				int sessionId = 0;
				try {
					sessionId = GeneratorSeq.getNextSeq();
				} catch (Exception e) {
				}

				request.setRequest(getLogRequest("IVR.fGetAccountDetails",
						request.getIsdn()));
				instance.debugMonitor("SEND:" + request.getRequest() + ". ID="
						+ sessionId);

				connection.checkBalancePostpaid(
						transformISDN(request.getIsdn()), balances);
				request.setResponse(getLogResponse(balances));

				instance.debugMonitor("RECEIVE:" + request.getResponse()
						+ ". ID=" + sessionId);

				if (request.getChannel().equals(Constants.CHANNEL_SMS)) {
					ProductRoute productRoute = ProductFactory.getCache()
							.getProductRoute(request.getRouteId());

					AppProperties config = productRoute.getParameters();

					String content = config
							.getString(
									"postpaid.message",
									"Cuoc trong thang <OUTSTANDING_AMOUNT>."
											+ " so no chua thanh toan thang truoc <UNBILLED_AMOUNT>,"
											+ " tong so no chua thanh toan <TOTAL_UNBILLED_AMOUNT>,"
											+ " khoan da thanh toan lan cuoi <LASTPAYMENT_AMOUNT> . Xin cam on!");

					request.setResponse(content);
					content = content.replaceAll("<OUTSTANDING_AMOUNT>",
							String.valueOf(balances[0]));
					content = content.replaceAll("<UNBILLED_AMOUNT>",
							String.valueOf(balances[1]));
					content = content.replaceAll("<LASTPAYMENT_AMOUNT>",
							String.valueOf(balances[2]));
					content = content.replaceAll("<TOTAL_UNBILLED_AMOUNT>",
							String.valueOf(balances[0] + balances[1]));
					// send SMS to subscriber
					if (!content.equals("")) {
						CommandUtil.sendSMS(instance, request, content);

						instance.debugMonitor(request.getIsdn() + ": "
								+ content);
					}
				} else if (request.getChannel().equals(Constants.CHANNEL_WEB)) {
					result.setResponseValue(ResponseUtil.AMOUNT_OUTSTANDING,
							balances[0]);
					result.setResponseValue(ResponseUtil.AMOUNT_UNBILL,
							balances[1]);
					result.setResponseValue(ResponseUtil.AMOUNT_LAST_PAYMENT,
							balances[2]);
				}
			} catch (Exception ex) {
				_logger.error("Error check balance postpaid: ", ex);
				processError(instance, provisioningCommand, request, ex);
			}
		}
		return result;
	}

	// DuyMB add loyalty 2012/01/19
	public boolean adjustment(CommandInstance instance, CommandMessage request)
			throws Exception {
		CBConnection _connection = null;

		String comment = ProductFactory.getCache()
				.getProduct(request.getProductId())
				.getParameter("loyalty.postpaid.comment", "chua co messsage");
		try {
			String schema = ProductFactory.getCache()
					.getProduct(request.getProductId())
					.getParameter("schema", "");

			String isdn = StringUtil.nvl(request.getShipTo(), "");

			if (isdn.equals("")) {
				isdn = request.getIsdn();
			}

			isdn = transformISDN(isdn);

			_connection = (CBConnection) instance.getProvisioningConnection();

			double amount = request.getAmount();

			_connection.adjustmentInsert(schema, isdn, String.valueOf(amount),
					comment);

			double balance = _connection.searchAccountBalance(schema, isdn)
					.doubleValue();

			request.setResponseValue("postpaid.balance",
					String.valueOf(balance));
			request.setCause(Constants.SUCCESS);
		} catch (SocketException e) {
			instance.getDispatcher().debugMonitor(
					"SocketException:" + e.getMessage());
			request.setStatus(Constants.ORDER_STATUS_DENIED);

			e.printStackTrace();

			throw e;
		} catch (TreChainedException e) {

			request.setStatus(4);

			this._logger.error("payment-error: " + request.toString());
			this._logger.error(e, e);

			request.setCause(Constants.ERROR);
			request.setResponse(e.getMessage());
		} finally {
			instance.closeProvisioningConnection(_connection);
		}

		return true;
	}

	// DuyMB add end 2012/01/19.
	public VNMMessage redeem(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);
		if (!adjustment(instance, request)) {
			result.setCause(Constants.ERROR);

			return result;
		}

		if (request.getStatus() == Constants.ORDER_STATUS_DENIED) {
			SubscriberProductImpl.withdraw(request.getUserId(),
					request.getUserName(), request.getSubscriberId(),
					request.getIsdn(), "LOYALTY", request.getQuantity());
		} else if (request.getCause().equals(Constants.SUCCESS)) {
			result.setCause(Constants.SUCCESS);
		}

		String smsContent = ProductFactory
				.getCache()
				.getProduct(request.getProductId())
				.getMessage(request.getActionType(), request.getCampaignId(),
						Constants.DEFAULT_LANGUAGE, request.getChannel(),
						request.getCause());

		smsContent = smsContent.replaceAll("<score>",
				String.valueOf(request.getQuantity()));
		smsContent = smsContent.replaceAll("<balance>",
				String.valueOf(request.getResponseValue("postpaid.balance")));

		CommandUtil.sendSMS(instance, request, smsContent);
		return result;
	}

	public VNMMessage registerService(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);
		if (instance.getDebugMode().equals("depend")) {
			simulation(instance, provisioningCommand, result);
		} else {
			try {
				String isdn = transformISDN(result.getIsdn());
				// check subscriber is existed or not
				Long productId = getProductId(instance, result);
				Long serviceTypeId = CBConnection.POSTPAID_SERVICE_TYPE_ID;
				if (result.isPrepaid())
					serviceTypeId = CBConnection.PREPAID_SERVICE_TYPE_ID;

				Properties respProperties = new Properties();
				long cbStatus = -1;

				CBConnection connection = null;
				try {
					connection = (CBConnection) instance
							.getProvisioningConnection();
					String schema = connection.getSchema(getSchema(result));
					cbStatus = connection.getServiceStatus(schema,
							serviceTypeId, productId, isdn, respProperties);

					instance.debugMonitor("isdn: " + isdn + " - cbstatus: "
							+ cbStatus);

					if (cbStatus == -1) {
						cbStatus = 3;
					} else {
						String strCBStatus = provisioningCommand.getParameter(
								"CB_STATUS." + cbStatus, cbStatus + "");
						cbStatus = Long.parseLong(strCBStatus);
					}
					if (cbStatus == 2 || cbStatus == 3) {
						// In case prepaid and service is cvqt
						if (!result.isPostpaid()
								&& createPrepaidInfor(result,
										"createSubscriber")) {
							Long baseProductInstanceId = connection
									.getBaseProductInstanceId(schema, isdn,
											serviceTypeId);

							if (baseProductInstanceId < 0) {
								SubscriberEntryImpl.getSubscriberInfo(result);
								connection.createPrepaidSubscriber(isdn,
										result, respProperties);
							}
						}
						ArrayList fields = new ArrayList();
						ArrayList values = new ArrayList();

						ProductEntry product = ProductFactory.getCache()
								.getProduct(request.getProductId());

						String parameterPrefix = "command."
								+ product.getAlias()
								+ "."
								+ ProvisioningFactory
										.getCache()
										.getCommand(
												provisioningCommand
														.getCommandId())
										.getAlias() + ".parameters";

						buildParameters(instance, product.getAlias(),
								parameterPrefix, fields, values);

						String strCommand = "REGISTER(" + isdn
								+ "), CB_STATUS=" + cbStatus + ", PRODUCTID="
								+ productId + ";";
						long sessionId = setRequest(instance, result,
								strCommand);

						String responseCode = connection.registerService(isdn,
								productId, fields, values);

						setResponse(instance, result, responseCode, sessionId);

						if (!responseCode.equals("success"))
							throw new Exception("Register service error");
						else
							result.setCause(Constants.SUCCESS);
					} else if (cbStatus == 1) {
						SubscriberProduct subscriberProduct = SubscriberProductImpl
								.getActive(result.getIsdn(),
										result.getProductId());
						if (subscriberProduct == null)
							result.setCause(Constants.SUCCESS);
						else
							result.setStatus(Constants.ORDER_STATUS_APPROVED);
					} else {
						throw new Exception("Unknown cbStatus.");
					}

				} finally {
					instance.closeProvisioningConnection(connection);
				}

			} catch (Exception e) {
				processError(instance, provisioningCommand, result, e);
			}
		}

		return result;
	}

	public VNMMessage renewalService(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);
		if (instance.getDebugMode().equals("depend")) {
			simulation(instance, provisioningCommand, result);
		} else {
			CBConnection connection = null;
			try {
				String isdn = transformISDN(result.getIsdn());
				// check subscriber is existed or not
				Long productId = getProductId(instance, result);
				Long serviceTypeId = CBConnection.POSTPAID_SERVICE_TYPE_ID;
				if (result.isPrepaid())
					serviceTypeId = CBConnection.PREPAID_SERVICE_TYPE_ID;

				Properties respProperties = new Properties();
				long cbStatus = -1;

				long cbStatusConvert = 3;
				int retryTime = 0;

				ProductEntry product = ProductFactory.getCache().getProduct(
						result.getProductId());

				boolean retryRenewal = product.getParameter("RetryRenewal",
						"false").equals("true");
				int maxRetryTime = Integer.parseInt(product.getParameter(
						"MaxRetryTime", "5"));
				long retryWaitingTime = Long.parseLong(product.getParameter(
						"RetryWaitingTime", "5"));

				connection = (CBConnection) instance
						.getProvisioningConnection();
				String schema = connection.getSchema(getSchema(result));
				do {
					cbStatus = connection.getServiceStatus(schema,
							serviceTypeId, productId, isdn, respProperties);
					instance.debugMonitor("isdn: " + isdn
							+ " - cbStatus before renewal: " + cbStatus);

					if (cbStatus == -1) {
						cbStatusConvert = 3;
					} else {
						String strCBStatus = provisioningCommand.getParameter(
								"CB_STATUS." + cbStatus, cbStatus + "");
						cbStatusConvert = Long.parseLong(strCBStatus);

						if (retryRenewal) {
							retryTime++;
							instance.debugMonitor("Retry " + retryTime
									+ " times isdn: " + isdn + " - cbstatus: "
									+ cbStatus);
							Thread.sleep(retryWaitingTime);
						}
					}
				} while (cbStatus != -1 && retryTime <= maxRetryTime);

				if (cbStatusConvert == 2 || cbStatusConvert == 3) {
					// In case prepaid and service is cvqt
					if (!result.isPostpaid()
							&& createPrepaidInfor(result, "createSubscriber")) {
						Long baseProductInstanceId = connection
								.getBaseProductInstanceId(schema, isdn,
										serviceTypeId);

						if (baseProductInstanceId < 0) {
							SubscriberEntryImpl.getSubscriberInfo(result);
							connection.createPrepaidSubscriber(isdn, result,
									respProperties);
						}
					}
					ArrayList fields = new ArrayList();
					ArrayList values = new ArrayList();

					String parameterPrefix = "command."
							+ product.getAlias()
							+ "."
							+ ProvisioningFactory
									.getCache()
									.getCommand(
											provisioningCommand.getCommandId())
									.getAlias() + ".parameters";

					buildParameters(instance, product.getAlias(),
							parameterPrefix, fields, values);

					String strCommand = "REGISTER(" + isdn + "), CB_STATUS="
							+ cbStatusConvert + ", PRODUCTID=" + productId
							+ ";";
					long sessionId = setRequest(instance, result, strCommand);

					String responseCode = connection.registerService(isdn,
							productId, fields, values);

					setResponse(instance, result, responseCode, sessionId);

					if (!responseCode.equals("success"))
						throw new Exception("Register service error");
					else
						result.setCause(Constants.SUCCESS);
				} else if (cbStatusConvert == 1) {
					SubscriberProduct subscriberProduct = SubscriberProductImpl
							.getActive(result.getIsdn(), result.getProductId());
					if (subscriberProduct == null)
						result.setCause(Constants.SUCCESS);
					else
						result.setStatus(Constants.ORDER_STATUS_APPROVED);
				} else {
					throw new Exception("Unknown cbStatus.");
				}
			} catch (Exception e) {
				processError(instance, provisioningCommand, result, e);
			} finally {
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}

	public VNMMessage unregisterService(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);
		if (instance.getDebugMode().equals("depend")) {
			simulation(instance, provisioningCommand, result);
		} else {
			try {

				String isdn = transformISDN(result.getIsdn());
				// check subscriber is existed or not
				Long productId = getProductId(instance, result);
				Long serviceTypeId = CBConnection.POSTPAID_SERVICE_TYPE_ID;
				if (result.isPrepaid())
					serviceTypeId = CBConnection.PREPAID_SERVICE_TYPE_ID;

				Properties respProperties = new Properties();
				long cbStatus = -1;
				CBConnection connection = null;
				try {
					connection = (CBConnection) instance
							.getProvisioningConnection();
					String schema = connection.getSchema(getSchema(result));
					cbStatus = connection.getServiceStatus(schema,
							serviceTypeId, productId, isdn, respProperties);

					if (cbStatus == -1)
						cbStatus = 3;
					else {
						String strCBStatus = provisioningCommand.getParameter(
								"CB_STATUS." + cbStatus, cbStatus + "");
						cbStatus = Long.parseLong(strCBStatus);
					}

					SubscriberProduct subscriberProduct = SubscriberProductImpl
							.getActive(result.getIsdn(), result.getProductId());

					if (cbStatus == 3) {
						if (subscriberProduct != null)
							result.setCause(Constants.SUCCESS);
					} else if (cbStatus == 2) {
						// if data does not exist in DB
						// setCause to transfer to register command in CRM
						// call barringService in CRM
					} else if (cbStatus == 1) {
						if (subscriberProduct != null) {
							String strCancelMode = getCancelMode(result);

							String strCommand = "UNREGISTER(" + isdn
									+ "), CB_STATUS=" + cbStatus
									+ ", PRODUCTID=" + productId;
							if (strCancelMode.equals(""))
								strCommand += ";";
							else
								strCommand += ", CANCEL_MODE=" + strCancelMode
										+ ";";

							String responseCode = "";
							long sessionId = setRequest(instance, result,
									strCommand);
							if (strCancelMode.equals(""))
								responseCode = connection.unregisterService(
										isdn, productId);
							else {
								long cancelMode = Long.parseLong(strCancelMode);
								responseCode = connection.unregisterService(
										isdn, productId, cancelMode);
							}

							setResponse(instance, result, responseCode,
									sessionId);
							result.setCause(Constants.SUCCESS);
						}
					} else {
						throw new Exception("Unknown cbStatus.");
					}

					cbStatus = connection.getServiceStatus(schema,
							serviceTypeId, productId, isdn, respProperties);
					instance.debugMonitor("isdn: " + isdn
							+ " - cbStatus after unregister: " + cbStatus);

					ProductEntry product = ProductFactory.getCache()
							.getProduct(result.getProductId());
					if (product.getParameter("PendingRegisterVB", "false")
							.equals("true")) {
						if (!result.getActionType().equals(
								Constants.ACTION_CANCEL)
								&& !result.getActionType().equals(
										Constants.ACTION_UNREGISTER)) {
							instance.logMonitor("Waiting...");
							Thread.sleep(Long.parseLong(product.getParameter(
									"TimePending", "10000")));
						}
					}
				} finally {
					instance.closeProvisioningConnection(connection);
				}
				result.setStatus(Constants.ORDER_STATUS_APPROVED);
			} catch (Exception e) {
				processError(instance, provisioningCommand, result, e);
			}
		}

		return result;
	}

	public VNMMessage unregisterOtherService(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);
		if (instance.getDebugMode().equals("depend")) {
			simulation(instance, provisioningCommand, result);
		} else {
			try {

				String isdn = transformISDN(result.getIsdn());
				// check subscriber is existed or not
				Long productId = Long.parseLong(provisioningCommand
						.getParameter("PostpaidProductId", ""));
				Long serviceTypeId = CBConnection.POSTPAID_SERVICE_TYPE_ID;
				if (result.isPrepaid())
					serviceTypeId = CBConnection.PREPAID_SERVICE_TYPE_ID;

				Properties respProperties = new Properties();
				long cbStatus = -1;
				CBConnection connection = null;
				try {
					connection = (CBConnection) instance
							.getProvisioningConnection();
					String schema = connection.getSchema(getSchema(result));
					cbStatus = connection.getServiceStatus(schema,
							serviceTypeId, productId, isdn, respProperties);

					if (cbStatus == -1)
						cbStatus = 3;
					else {
						String strCBStatus = provisioningCommand.getParameter(
								"CB_STATUS." + cbStatus, cbStatus + "");
						cbStatus = Long.parseLong(strCBStatus);
					}

					String productAlias = provisioningCommand.getParameter(
							"ProductAlias", "");
					ProductEntry product = ProductFactory.getCache()
							.getProduct(productAlias);
					SubscriberProduct subscriberProduct = SubscriberProductImpl
							.getActive(result.getIsdn(), product.getProductId());

					if (cbStatus == 3) {
						if (subscriberProduct != null)
							result.setCause(Constants.SUCCESS);
					} else if (cbStatus == 2) {
						// if data does not exist in DB
						// setCause to transfer to register command in CRM
						// call barringService in CRM
					} else if (cbStatus == 1) {
						if (subscriberProduct != null) {
							String strCancelMode = getCancelMode(result);

							String strCommand = "UNREGISTER(" + isdn
									+ "), CB_STATUS=" + cbStatus
									+ ", PRODUCTID=" + productId;
							if (strCancelMode.equals(""))
								strCommand += ";";
							else
								strCommand += ", CANCEL_MODE=" + strCancelMode
										+ ";";

							String responseCode = "";
							long sessionId = setRequest(instance, result,
									strCommand);
							if (strCancelMode.equals(""))
								responseCode = connection.unregisterService(
										isdn, productId);
							else {
								long cancelMode = Long.parseLong(strCancelMode);
								responseCode = connection.unregisterService(
										isdn, productId, cancelMode);
							}

							setResponse(instance, result, responseCode,
									sessionId);
							result.setCause(Constants.SUCCESS);
						}
					} else {
						throw new Exception("Unknown cbStatus.");
					}

					if (product.getParameter("PendingRegister", "false")
							.equals("true")) {
						if (!result.getActionType().equals(
								Constants.ACTION_CANCEL)
								&& !result.getActionType().equals(
										Constants.ACTION_UNREGISTER)) {
							instance.logMonitor("Waiting...");
							Thread.sleep(Long.parseLong(product.getParameter(
									"TimePending", "10000")));
						}
					}
				} finally {
					instance.closeProvisioningConnection(connection);
				}
				result.setStatus(Constants.ORDER_STATUS_APPROVED);
			} catch (Exception e) {
				processError(instance, provisioningCommand, result, e);
			}
		}

		return result;
	}

	public VNMMessage checkTimeUsed(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);
		CBConnection connection = null;

		SubscriberProduct subProduct = SubscriberProductImpl.getActive(
				result.getIsdn(), result.getProductId());

		String responseCode = "";
		if (instance.getDebugMode().equals("depend")) {
			simulation(instance, provisioningCommand, result);
		} else {
			try {
				connection = (CBConnection) instance
						.getProvisioningConnection();

				String schema = connection.getSchema(getSchema(result));

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");

				String strCommand = "checkTimeUsed(" + result.getIsdn()
						+ "), PRODUCTID=" + result.getProductId();
				long sessionId = setRequest(instance, request, strCommand);
				double timeUsed = connection.getTimeUsed(schema,
						result.getIsdn(),
						sdf.format(subProduct.getRegisterDate()));
				setResponse(instance, request, "success", sessionId);

				if (timeUsed == -1) {
					responseCode = "postpaid-not-used";
					result.setCause(responseCode);
				} else {
					responseCode = "postpaid-get-time-used";
					result.setCause(responseCode);
					result.setResponseValue(ResponseUtil.SERVICE_BALANCE,
							String.valueOf(timeUsed));
				}
			} catch (Exception e) {
				processError(instance, provisioningCommand, request, e);
			} finally {
				instance.closeProvisioningConnection(connection);
			}
		}

		return result;
	}

	public String getLogRequest(String functionName, String isdn) {
		String log = functionName + "{" + isdn + "}";

		return log;
	}

	public String getCancelMode(CommandMessage request) throws Exception {
		ProductEntry product = ProductFactory.getCache().getProduct(
				request.getProductId());
		return product.getParameter("cb.cancelMode", "");
	}

	public String getSchema(CommandMessage request) {
		String schema = "";

		try {
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());
			schema = product.getParameter("schema", "");
		} catch (Exception e) {

		}

		return schema;
	}

	public String getLogResponse(double[] balances) {
		String content = "";
		content = "OUTSTANDING_AMOUNT{" + String.valueOf(balances[0]) + "};";
		content += "UNBILLED_AMOUNT{" + String.valueOf(balances[1]) + "};";
		content += "LASTPAYMENT_AMOUNT{" + String.valueOf(balances[2]) + "}";
		return content;
	}

	// Ham top up cho postpaid
	public VNMMessage payment(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);
		ProductRoute productRoute = ProductFactory.getCache().getProductRoute(
				request.getRouteId());
		
		ProductEntry product = ProductFactory.getCache().getProduct(request.getProductId());
		
		String smsContent = "";
		String isdn = "";
		String sourceISDN = "";

		try {
			if (request.getShipTo() == null || request.getShipTo().equals("")) {
				isdn = StringUtil.nvl(request.getIsdn(), "");
			} else {
				isdn = StringUtil.nvl(request.getShipTo(), "");
				sourceISDN = transformISDN(request.getIsdn());
			}

			if (isdn.equals("")) {
				isdn = result.getShipTo();
			}

			isdn = transformISDN(isdn);

			CBConnection _connection = (CBConnection) instance
					.getProvisioningConnection();
			String schema = _connection.getSchema(product.getParameter(
					"schema", ""));
			String amount = String.valueOf(result.getAmount());

			amount = StringUtil.nvl(amount, "0");
			if (result.getAmount() < 0) {
				throw new AppException("invalid-amount");
			} else if (result.getAmount() == 0) {
				throw new AppException("invalid-amount");
			} else {
				long sessionId = setRequest(instance, result,
						"Postpaid Billing{isdn=" + isdn + ",amount= " + amount
								+ "}");
				_connection.paymentInsert(schema, isdn, amount);
				double balance = _connection.searchAccountBalance(schema, isdn);

				if (request.getChannel().equals(Constants.CHANNEL_SMS)) {

					if (sourceISDN != null && !sourceISDN.equals("")) {
						smsContent = productRoute
								.getParameter("message.sender",
										"Da thanh toan thanh cong cho so thue bao <isdn>, so tien <amount>");

						smsContent = smsContent.replaceAll("<isdn>", isdn);
						smsContent = smsContent.replaceAll("<amount>", amount);
						// send SMS to subscriber
						if (!smsContent.equals("")) {
							CommandUtil.sendSMS(instance, request, smsContent);

							instance.debugMonitor(request.getIsdn() + ": "
									+ smsContent);
						}

						smsContent = productRoute
								.getParameter(
										"message.receiver",
										"Ban da duoc thanh toan <amount> dong tu so thue bao <isdn>, tai khoan <balance> d");

						result.setResponse(smsContent);
						smsContent = smsContent.replaceAll("<isdn>", isdn);
						smsContent = smsContent.replaceAll("<amount>", amount);
						smsContent = smsContent.replaceAll("<balance>",
								String.valueOf(balance));

						if (!smsContent.equals("")) {
							CommandUtil.sendSMS(instance, request, smsContent);

							instance.debugMonitor(request.getIsdn() + ": "
									+ smsContent);
						}
					} else {
						smsContent = productRoute
								.getParameter("message.owner",
										"Thanh toan thanh con so tien <amount>, tai khoan <balance> d");
						result.setResponse(smsContent);
						smsContent = smsContent.replaceAll("<amount>", amount);
						smsContent = smsContent.replaceAll("<balance>",
								String.valueOf(balance));
						if (!smsContent.equals("")) {
							CommandUtil.sendSMS(instance, request, smsContent);

							instance.debugMonitor(request.getIsdn() + ": "
									+ smsContent);

						}
					}

					result.setResponse(Constants.SUCCESS);
					setResponse(instance, result, "{isdn=" + isdn
							+ ",balance= " + balance + ",cause="
							+ Constants.SUCCESS + "}", sessionId);
				} else if (request.getChannel().equals(Constants.CHANNEL_WEB)) {
					result.setResponse(Constants.SUCCESS);
					result.setDescription("Balance after payment: " + balance);
				}
			}

		} catch (Exception e) {
			_logger.error("Error payment balance postpaid: ", e);
			result.setResponse(Constants.ERROR);
			processError(instance, provisioningCommand, request, e);
			DispatcherThread dis = new DispatcherThread();
			dis.sendAlarmMessage(e, Constants.ERROR,
					result.getProvisioningId(), "");
		}
		return result;
	}

	public VNMMessage checkBlackList(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);
		boolean found = false;
		Connection connection = null;
		PreparedStatement smpt = null;
		ResultSet rs = null;

		if (!request.isPostpaid()) {
			result.setCause("idd-prepaid");
			return result;
		}

		if (instance.getDebugMode().equals("depend")) {
			simulation(instance, provisioningCommand, result);
		} else {
			try {
				long productid = request.getProductId();
				connection = Database.getConnection();

				String table = "subscriberblacklist";
				String SQL = " SELECT NVL(STATUS,0) as STATUS FROM " + table
						+ " WHERE MSISDN = '" + request.getIsdn() + "' AND "
						+ " PRODUCTID ='" + productid + "'";
				smpt = connection.prepareStatement(SQL);
				rs = smpt.executeQuery();
				while (rs.next()) {
					if (rs.getString("STATUS").equals("1")) {
						found = true;
						break;
					}
				}
				if (!found) {
					result.setCause(Constants.SUCCESS);
				} else {
					result.setCause(Constants.ERROR);
				}
			} catch (Exception ex) {
			} finally {
				Database.closeObject(rs);
				Database.closeObject(smpt);
				Database.closeObject(connection);
			}
		}

		return result;
	}

	private boolean createPrepaidInfor(CommandMessage request, String keyFlag) {
		ProductEntry product = null;
		try {
			product = ProductFactory.getCache().getProduct(
					request.getProductId());
		} catch (Exception ex) {

		}
		return product.getParameter(keyFlag, "false").equals("true");
	}

	private Logger _logger = Logger.getLogger(CBCommandImpl.class);
}
