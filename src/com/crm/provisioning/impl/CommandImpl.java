/**
 * 
 */
package com.crm.provisioning.impl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.axis.AxisFault;

import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductMessage;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.CommandAction;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.cache.ProvisioningEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.cache.ProvisioningMessage;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.thread.ProvisioningThread;
import com.crm.provisioning.util.CommandUtil;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.spam.impl.SpamImpl;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.IDDServiceImpl;
import com.crm.subscriber.impl.MGMServiceImpl;
import com.crm.subscriber.impl.SubscriberGroupImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.GeneratorSeq;
import com.crm.util.StringUtil;
import com.crm.kernel.index.ExecuteImpl;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class CommandImpl extends ExecuteImpl {
	private static long DEFAULT_ID = 0;

	public ProvisioningConnection getProvisioningConnection(
			CommandInstance instance) throws Exception {
		return instance.getProvisioningConnection();
	}

	public String getErrorCode(CommandInstance instance,
			CommandMessage request, Exception error) {
		String errorCode = Constants.ERROR;
		try {
			instance.logMonitor(error);

			if (error != null) {
				if (error instanceof AppException) {
					errorCode = ((AppException) error).getMessage();
				} else {
					ProvisioningEntry provisioning = ProvisioningFactory
							.getCache().getProvisioning(
									request.getProvisioningId());

					ProvisioningMessage message = provisioning.getMessage(error
							.getMessage());

					if (message != null) {
						errorCode = message.getCause();
					}
				}
			}
		} catch (Exception e) {
			instance.getLog().error(e);
		}

		return errorCode;
	}

	public void processError(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request,
			Exception error) throws Exception {
		String errorCode = "";

		try {
			if (error instanceof AppException) {
				errorCode = ((AppException) error).getMessage();
			} else if (error instanceof AxisFault) {
				AxisFault axisFault = (AxisFault) error;

				if (axisFault.detail instanceof ConnectException
						|| axisFault.detail instanceof NoRouteToHostException) {
					errorCode = Constants.ERROR_CONNECTION;
				} else {
					errorCode = getErrorCode(instance, request, error);
				}
			} else if (error instanceof IOException) {
				errorCode = Constants.ERROR_CONNECTION;
			} else {
				errorCode = getErrorCode(instance, request, error);
			}

			request.setStatus(Constants.ORDER_STATUS_DENIED);
			request.setCause(errorCode);
			request.setDescription(error.toString());

			CommandUtil.processError(instance, request, errorCode);
		} finally {
			// rollback(instance, provisioningCommand, request);
		}
	}

	public CommandMessage simulation(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		try {
			long simulationExecuteTime = ((ProvisioningThread) instance
					.getDispatcher()).simulationTime;
			String cause = ((ProvisioningThread) instance.getDispatcher()).simulationCause;
			instance.debugMonitor("Simulation execute time: "
					+ simulationExecuteTime + "ms");
			Thread.sleep(simulationExecuteTime);
			request.setCause(cause);
		} catch (Exception e) {
			throw e;
		}

		return request;
	}

	/**
	 * Edited by NamTA<br>
	 * Modified Date: 17/05/2012
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CommandMessage register(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			boolean includeCurrentDay = result.getParameters().getBoolean(
					"includeCurrentDay");

			// if (result.getActionType().equals(Constants.ACTION_UPGRADE))
			// includeCurrentDay = false;
			long sessionId = setRequest(
					instance,
					request,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.register",
							request.getIsdn() + ", " + request.getProductId()));

			int subProductStatus = Constants.SUBSCRIBER_REGISTER_STATUS;
			if (result.getCampaignId() != Constants.DEFAULT_ID) {
				if (result.getParameters().getBoolean("FreeWithReactive")) {
					if (result.getParameters().getBoolean("FreeOneDay", false)) {
						subProductStatus = Constants.SUBSCRIBER_ALERT_EXPIRE_STATUS;
					} else {
						subProductStatus = Constants.SUBSCRIBER_FREE_WITH_REACTIVE_STATUS;
					}
				} else {
					subProductStatus = Constants.SUBSCRIBER_FREE_NOT_REACTIVE_STATUS;
				}
			}
			SubscriberProduct subProduct = SubscriberProductImpl.register(
					result.getUserId(), result.getUserName(),
					result.getSubscriberId(), result.getIsdn(),
					result.getSubscriberType(), result.getProductId(),
					result.getCampaignId(), result.getLanguageId(),
					includeCurrentDay, request.isPrepaid(), subProductStatus,
					getActivationDate(result));

			setResponse(instance, request, "success", sessionId);
			if (subProduct.getSubProductId() != DEFAULT_ID) {
				result.setSubProductId(subProduct.getSubProductId());
			}

			if (subProduct.getExpirationDate() != null) {
				result.setResponseValue(ResponseUtil.SERVICE_EXPIRE_DATE,
						StringUtil.format(subProduct.getExpirationDate(),
								"dd/MM/yyyy"));
			}

			if (result.getCampaignId() != DEFAULT_ID) {
				String content = result.getParameters().getProperty(
						"FreeSMSTemp", "");
				content = content.replaceAll("~SERVICE_EXPIRE_DATE~",
						StringUtil.format(subProduct.getExpirationDate(),
								"dd/MM/yyyy"));

				Calendar chargeDate = Calendar.getInstance();
				chargeDate.setTime(subProduct.getExpirationDate());
				chargeDate.add(Calendar.DATE, 1);
				content = content.replaceAll("~SERVICE_START_DATE~",
						StringUtil.format(chargeDate.getTime(), "dd/MM/yyyy"));

				result.setResponseValue(ResponseUtil.SMS_TEXT, content);
			}
		} catch (Exception error) {
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	public CommandMessage registerWithConfirm(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			boolean includeCurrentDay = result.getParameters().getBoolean(
					"includeCurrentDay");

			// if (result.getActionType().equals(Constants.ACTION_UPGRADE))
			// includeCurrentDay = false;
			long sessionId = setRequest(
					instance,
					request,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.registerWithConfirm",
							request.getIsdn() + ", " + request.getProductId()));

			int subProductStatus = Constants.SUBSCRIBER_REGISTER_STATUS;
			if (result.getCampaignId() != Constants.DEFAULT_ID) {
				if (result.getParameters().getBoolean("FreeWithReactive")) {
					if (result.getParameters().getBoolean("FreeOneDay", false)) {
						subProductStatus = Constants.SUBSCRIBER_ALERT_EXPIRE_STATUS;
					} else {
						subProductStatus = Constants.SUBSCRIBER_FREE_WITH_REACTIVE_STATUS;
					}
				} else {
					subProductStatus = Constants.SUBSCRIBER_FREE_NOT_REACTIVE_STATUS;
				}
			}

			SubscriberProduct subProduct = IDDServiceImpl.registerIDD(
					request.getUserId(), request.getUserName(),
					request.getSubscriberId(), request.getIsdn(),
					request.getSubscriberType(), request.getProductId(),
					request.getCampaignId(), request.getLanguageId(),
					includeCurrentDay, true, subProductStatus,
					getActivationDate(request), request.getSubProductId());

			setResponse(instance, request, "success", sessionId);
			if (subProduct.getSubProductId() != DEFAULT_ID) {
				result.setSubProductId(subProduct.getSubProductId());
			}

			if (subProduct.getExpirationDate() != null) {
				result.setResponseValue(ResponseUtil.SERVICE_EXPIRE_DATE,
						StringUtil.format(subProduct.getExpirationDate(),
								"dd/MM/yyyy"));
			}

			if (result.getCampaignId() != DEFAULT_ID) {
				String content = result.getParameters().getProperty(
						"FreeSMSTemp", "");
				content = content.replaceAll("~SERVICE_EXPIRE_DATE~",
						StringUtil.format(subProduct.getExpirationDate(),
								"dd/MM/yyyy"));

				Calendar chargeDate = Calendar.getInstance();
				chargeDate.setTime(subProduct.getExpirationDate());
				chargeDate.add(Calendar.DATE, 1);
				content = content.replaceAll("~SERVICE_START_DATE~",
						StringUtil.format(chargeDate.getTime(), "dd/MM/yyyy"));

				result.setResponseValue(ResponseUtil.SMS_TEXT, content);
			}
		} catch (Exception error) {
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	public CommandMessage unregister(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			long sessionId = setRequest(
					instance,
					request,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.unregister",
							request.getIsdn() + ", " + request.getProductId()));

			SubscriberProductImpl.unregister(result.getUserId(),
					result.getUserName(), result.getSubProductId(),
					result.getProductId());

			setResponse(instance, request, "success", sessionId);
		} catch (Exception error) {
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	public CommandMessage unregisterOther(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			String productAlias = provisioningCommand.getParameter(
					"ProductAlias", "");
			ProductEntry product = ProductFactory.getCache().getProduct(
					productAlias);

			SubscriberProduct subscriberProduct = SubscriberProductImpl
					.getUnterminated(result.getIsdn(), product.getProductId());

			long sessionId = setRequest(
					instance,
					request,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.unregisterOther",
							request.getIsdn() + ", " + request.getProductId()));

			SubscriberProductImpl.unregister(subscriberProduct.getUserId(),
					subscriberProduct.getUserName(),
					subscriberProduct.getSubProductId(),
					subscriberProduct.getProductId());

			setResponse(instance, request, "success", sessionId);
		} catch (Exception error) {
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	public CommandMessage subscription(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			boolean includeCurrentDay = result.getParameters().getBoolean(
					"includeCurrentDay");

			long sessionId = setRequest(
					instance,
					result,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.subscription",
							result.getIsdn() + ", " + result.getProductId()));

			SubscriberProduct subProduct = SubscriberProductImpl.subscription(
					result.getUserId(), result.getUserName(),
					result.getSubProductId(), result.isFullOfCharge(),
					result.getQuantity(), includeCurrentDay);

			setResponse(instance, result, "success", sessionId);

			ProductEntry product = ProductFactory.getCache().getProduct(
					result.getProductId());
			ProductMessage productMessage = product.getProductMessage(
					result.getActionType(), result.getCampaignId(),
					result.getLanguageId(), result.getChannel(),
					Constants.SUCCESS);
			if (productMessage != null) {
				String content = productMessage.getContent();
				if (subProduct.getExpirationDate() != null) {
					content = content.replaceAll("~SERVICE_EXPIRE_DATE~",
							StringUtil.format(subProduct.getExpirationDate(),
									"dd/MM/yyyy"));
				}
				SubscriberProductImpl.insertSendSMS(
						product.getParameter("ProductShotCode", ""),
						result.getIsdn(), content);
			}
		} catch (Exception error) {
			processError(instance, provisioningCommand, result, error);
		}

		return result;
	}

	public CommandMessage barringBySupplier(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			long sessionId = setRequest(
					instance,
					request,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.barringBySupplier",
							request.getIsdn() + ", " + request.getProductId()));

			SubscriberProductImpl.barringBySupplier(result.getUserId(),
					result.getUserName(), result.getSubProductId());

			setResponse(instance, request, "success", sessionId);
		} catch (Exception error) {
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	/**
	 * Created by NamTA<br>
	 * Created Date: 16/05/2012
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CommandMessage extendExpirationDate(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			boolean includeCurrentDay = result.getParameters().getBoolean(
					"includeCurrentDay", false);

			long sessionId = setRequest(
					instance,
					request,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.extendExpirationDate",
							request.getIsdn() + ", " + request.getProductId()));

			SubscriberProduct subProduct = SubscriberProductImpl
					.extendExpirationDate(result.getUserId(),
							result.getUserName(), result.getSubProductId(),
							result.getCampaignId(), includeCurrentDay,
							result.isFullOfCharge(), result.getQuantity());

			setResponse(instance, request, "success", sessionId);

			/**
			 * Add response value
			 */
			result.setResponseValue(ResponseUtil.SERVICE_EXPIRE_DATE,
					StringUtil.format(subProduct.getExpirationDate(),
							"dd/MM/yyyy"));
		} catch (Exception error) {
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	public CommandMessage extendExpirationFree(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			boolean includeCurrentDay = result.getParameters().getBoolean(
					"includeCurrentDay", false);

			long sessionId = setRequest(
					instance,
					request,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.extendExpirationFree",
							request.getIsdn() + ", " + request.getProductId()));

			SubscriberProduct subProduct = SubscriberProductImpl
					.extendExpirationFree(result.getUserId(),
							result.getUserName(), result.getSubProductId(),
							result.getCampaignId(), includeCurrentDay,
							result.isFullOfCharge(), result.getQuantity());

			setResponse(instance, request, "success", sessionId);

			/**
			 * Add response value
			 */
			result.setResponseValue(ResponseUtil.SERVICE_EXPIRE_DATE,
					StringUtil.format(subProduct.getExpirationDate(),
							"dd/MM/yyyy"));
		} catch (Exception error) {
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	public CommandMessage unbarringBySupplier(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			long sessionId = setRequest(
					instance,
					request,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.unbarringBySupplier",
							request.getIsdn() + ", " + request.getProductId()));

			SubscriberProductImpl.unbarringBySupplier(result.getUserId(),
					result.getUserName(), result.getSubProductId());

			setResponse(instance, request, "success", sessionId);
		} catch (Exception error) {
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	public CommandMessage addMember(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		Connection connection = null;

		CommandMessage result = request;

		try {
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());

			connection = Database.getConnection();

			connection.setAutoCommit(false);

			// add member into group
			String verifyCode = result.getParameters().getString(
					"member.verifyCode", "");

			SubscriberGroupImpl.addMember(connection, result.getUserId(),
					result.getUserName(), result.getIsdn(), result.getShipTo(),
					result.getProductId(), product.getMemberType(), verifyCode,
					result.getOrderDate(), Constants.SUPPLIER_ACTIVE_STATUS);

			if (product.isSubscription()) {
				int totalMember = SubscriberGroupImpl.countMember(connection,
						result.getIsdn(), result.getProductId(),
						product.getMemberType(), true);

				if (totalMember == 1 && request.getProductId() != 12110) {
					boolean includeCurrentDay = result.getParameters()
							.getBoolean("includeCurrentDay", false);

					SubscriberProductImpl.register(connection,
							result.getUserId(), result.getUserName(),
							result.getSubscriberId(), result.getIsdn(),
							result.getSubscriberType(), result.getProductId(),
							result.getCampaignId(), result.getLanguageId(),
							includeCurrentDay, request.isPrepaid(), 0,
							getActivationDate(result));
				}
			}

			connection.commit();
		} catch (Exception error) {
			Database.rollback(connection);

			processError(instance, provisioningCommand, request, error);
		} finally {
			Database.closeObject(connection);
		}

		return result;
	}

	public CommandMessage addMemberF5(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		Connection connection = null;

		CommandMessage result = request;

		try {
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());

			connection = Database.getConnection();

			connection.setAutoCommit(false);

			// add member into group
			String verifyCode = result.getParameters().getString(
					"member.verifyCode", "");
			String[] phoneBookList = result
					.getRequestValue("f5-new-member", "").split(",");

			for (int i = 0; i < phoneBookList.length; i++) {
				SubscriberGroupImpl
						.addMember(connection, result.getUserId(),
								result.getUserName(), result.getIsdn(),
								phoneBookList[i], result.getProductId(),
								product.getMemberType(), verifyCode,
								result.getOrderDate(),
								Constants.SUPPLIER_ACTIVE_STATUS);
			}
			connection.commit();
		} catch (Exception error) {
			Database.rollback(connection);

			processError(instance, provisioningCommand, request, error);
		} finally {
			Database.closeObject(connection);
		}

		return result;
	}

	public CommandMessage removeMember(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		Connection connection = null;

		CommandMessage result = request;

		try {
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());

			connection = Database.getConnection();

			connection.setAutoCommit(false);

			// add member into group
			SubscriberGroupImpl.removeMember(connection, result.getIsdn(),
					result.getShipTo(), result.getProductId(),
					product.getMemberType(), Constants.SUPPLIER_CANCEL_STATUS);

			if (product.isSubscription()) {
				int totalMember = SubscriberGroupImpl.countMember(connection,
						result.getIsdn(), result.getProductId(),
						product.getMemberType(), true);

				if (totalMember == 0) {
					SubscriberProductImpl.unregister(connection,
							result.getUserId(), result.getUserName(),
							result.getSubProductId(), result.getProductId());
				}
			}

			connection.commit();
		} catch (Exception error) {
			Database.rollback(connection);

			processError(instance, provisioningCommand, request, error);
		} finally {
			Database.closeObject(connection);
		}

		return result;
	}

	public CommandMessage removeMemberF5(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		Connection connection = null;

		CommandMessage result = request;

		try {
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());

			connection = Database.getConnection();

			connection.setAutoCommit(false);

			// add member into group
			String[] phoneBookList = result.getRequestValue("f5-remove-member",
					"").split(",");

			for (int i = 0; i < phoneBookList.length; i++) {
				SubscriberGroupImpl.removeMember(connection, result.getIsdn(),
						phoneBookList[i], result.getProductId(),
						product.getMemberType(),
						Constants.SUPPLIER_BARRING_STATUS);
			}

			connection.commit();
		} catch (Exception error) {
			Database.rollback(connection);

			processError(instance, provisioningCommand, request, error);
		} finally {
			Database.closeObject(connection);
		}

		return result;
	}

	public CommandMessage removeGroup(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		Connection connection = null;

		CommandMessage result = request;

		try {
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());

			connection = Database.getConnection();

			connection.setAutoCommit(false);

			// add member into group
			SubscriberGroupImpl.removeGroup(connection, result.getIsdn(),
					result.getProductId(), product.getMemberType());

			if (product.isSubscription()) {
				SubscriberProductImpl.unregister(connection,
						result.getUserId(), result.getUserName(),
						result.getSubProductId(), result.getProductId());
			}

			connection.commit();
		} catch (Exception error) {
			Database.rollback(connection);

			processError(instance, provisioningCommand, request, error);
		} finally {
			Database.closeObject(connection);
		}

		return result;
	}

	public CommandMessage rollback(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		instance.rollback(request);
		return request;
	}

	public CommandMessage nextCommand(CommandAction action,
			CommandMessage message) throws Exception {
		CommandMessage transform = ((CommandMessage) message).clone();

		transform.setCommandId(action.getNextCommandId());

		// transform.setCommandRequest(transform.getCommandResponse());
		transform.setResponse("");

		return transform;
	}

	/**
	 * withdraw money for loyalty balance.
	 * 
	 * @param instance
	 * @param provisioningCommand
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public CommandMessage withDraw(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;
		try {

			SubscriberProductImpl.withdraw(request.getUserId(),
					request.getUserName(), request.getSubscriberId(),
					request.getIsdn(), "LOYALTY", request.getQuantity());

			result.setCause(Constants.SUCCESS);
		} catch (Exception e) {
			result.setCause("error-withdraw-loyalty");
			processError(instance, provisioningCommand, result, e);
		}
		return result;
	}

	public CommandMessage recover(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;
		try {
			// Change function to recover money when error
			SubscriberProductImpl.withdraw(request.getUserId(),
					request.getUserName(), request.getSubscriberId(),
					request.getIsdn(), "LOYALTY", -1 * request.getQuantity());
			result.setCause(Constants.SUCCESS);
		} catch (Exception e) {
			result.setCause("error-withdraw-loyalty");
			processError(instance, provisioningCommand, result, e);
		}
		return result;
	}

	public CommandMessage registerMGM(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		ProductEntry product = ProductFactory.getCache().getProduct(
				request.getProductId());

		String introducer = request.getIsdn();

		String responseCode = "";

		try {
			String referral = request.getKeyword().toUpperCase();

			referral = referral.substring(referral.indexOf(" "),
					referral.length()).trim();

			// Check country code
			if (!referral.equals("")
					&& referral.startsWith(Constants.DOMESTIC_CODE)) {
				referral = referral.substring(Constants.DOMESTIC_CODE.length());
				referral = Constants.COUNTRY_CODE + referral;
			}

			// Check max referral
			int intNumOfCc = MGMServiceImpl.getNumOfCC(introducer);
			String CCGroupName = product.getParameter("CCGroupName", "CG");
			CCGroupName = CCGroupName + (intNumOfCc + 1);
			int AlcoTime = Integer.valueOf(product.getParameter("AlcoTime",
					"34"));

			Calendar serviceStart = Calendar.getInstance();
			Calendar serviceEnd = Calendar.getInstance();
			serviceEnd.add(Calendar.DATE, AlcoTime);

			boolean checkAddDaily = Boolean.parseBoolean(request
					.getParameters().getProperty("AddDaily", "true"));
			int addDaily = 1;

			if (!checkAddDaily) {
				addDaily = 2;
				AlcoTime = Integer.valueOf(product.getParameter("AlcoTime_1M",
						"30"));
			}

			// Insert into table.
			if (MGMServiceImpl.insertNewMGM(introducer, referral, introducer
					+ "_" + CCGroupName, serviceStart, serviceEnd, AlcoTime,
					addDaily)) {
				if (!checkAddDaily) {
					responseCode = Constants.SUCCESS + "_1M";
				} else {
					responseCode = Constants.SUCCESS;
				}
				request.setCause(responseCode);
				request.setShipTo(referral);
				request.setResponseValue(ResponseUtil.REFERAL, referral);
				request.setResponseValue(ResponseUtil.SERVICE_EXPIRE_DATE,
						new SimpleDateFormat("dd/MM/yyyy").format(serviceEnd
								.getTime()));
			} else {
				throw new AppException("insert-fail");
			}
		} catch (Exception e) {
			processError(instance, provisioningCommand, request, e);
		}

		return request;
	}

	public CommandMessage updateDailyMGM(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		String responseCode = "";
		String isdn = request.getIsdn();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			if (MGMServiceImpl.updateAddBalance(isdn, sdf.format(new Date()))) {
				responseCode = Constants.SUCCESS;
				request.setCause(responseCode);
				request.setStatus(Constants.SERVICE_STATUS_APPROVED);
			} else {
				throw new AppException("update-fail");
			}
		} catch (Exception e) {
			processError(instance, provisioningCommand, request, e);
		}

		return request;
	}

	public CommandMessage doNothing(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		return request;
	}

	/**
	 * Sets request and log, auto-generate sessionId
	 * 
	 * @param instance
	 * @param request
	 * @param requestString
	 * @return
	 * @throws Exception
	 */
	public long setRequest(CommandInstance instance, CommandMessage request,
			String requestString) throws Exception {
		long sessionId = 0;
		try {
			if (request.getOrderId() != Constants.DEFAULT_ID)
				sessionId = request.getOrderId();
			sessionId = GeneratorSeq.getNextSeq();
		} catch (Exception e) {
		}

		setRequest(instance, request, requestString, sessionId);

		return sessionId;
	}

	/**
	 * Sets request and log, use existing sessionId
	 * 
	 * @param instance
	 * @param request
	 * @param requestString
	 * @param sessionId
	 * @throws Exception
	 */
	public void setRequest(CommandInstance instance, CommandMessage request,
			String requestString, long sessionId) throws Exception {

		requestString = "ID=" + sessionId + ": " + requestString;

		// instance.logMonitor("SEND: " + requestString);
		request.setRequest(requestString);
		request.setRequestTime(new Date());
	}

	/**
	 * Sets response and log
	 * 
	 * @param instance
	 * @param request
	 * @param responseString
	 * @param sessionId
	 * @throws Exception
	 */
	public void setResponse(CommandInstance instance, CommandMessage request,
			String responseString, long sessionId) throws Exception {
		request.setResponseTime(new Date());

		long costTime = CommandUtil.calculateCostTime(request.getRequestTime(),
				request.getResponseTime());

		request.setDescription(responseString);

		responseString = "ID=" + sessionId + ": " + responseString
				+ ": costTime=" + costTime;
		 //instance.logMonitor("RECEIVE: " + responseString);
		request.setResponse(responseString);
	}

	public String getLogRequest(String functionName, String isdn) {
		String log = functionName + "{" + isdn + "}";

		return log;
	}

	public CommandMessage register3G(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;

		try {
			boolean includeCurrentDay = result.getParameters().getBoolean(
					"includeCurrentDay");

			// if (result.getActionType().equals(Constants.ACTION_UPGRADE))
			// includeCurrentDay = false;
			long sessionId = setRequest(
					instance,
					request,
					getLogRequest(
							"com.crm.provisioning.impl.CommandImpl.register",
							request.getIsdn() + ", " + request.getProductId()));

			int subProductStatus = Constants.SUBSCRIBER_REGISTER_STATUS;
			if (result.getCampaignId() != Constants.DEFAULT_ID) {
				if (result.getParameters().getBoolean("FreeWithReactive")) {
					if (result.getParameters().getBoolean("FreeOneDay", false)) {
						subProductStatus = Constants.SUBSCRIBER_ALERT_EXPIRE_STATUS;
					} else {
						subProductStatus = Constants.SUBSCRIBER_FREE_WITH_REACTIVE_STATUS;
					}
				} else {
					subProductStatus = Constants.SUBSCRIBER_FREE_NOT_REACTIVE_STATUS;
				}
			}

			String activationDate = getActivationDate(result);
			SubscriberProduct subProduct = SubscriberProductImpl.register(
					result.getUserId(), result.getUserName(),
					result.getSubscriberId(), result.getIsdn(),
					result.getSubscriberType(), result.getProductId(),
					result.getCampaignId(), result.getLanguageId(),
					includeCurrentDay, request.isPrepaid(), subProductStatus,
					activationDate);

			setResponse(instance, request, "success", sessionId);
			if (subProduct.getSubProductId() != DEFAULT_ID) {
				result.setSubProductId(subProduct.getSubProductId());
			}

			if (subProduct.getExpirationDate() != null) {
				result.setResponseValue(ResponseUtil.SERVICE_EXPIRE_DATE,
						StringUtil.format(subProduct.getExpirationDate(),
								"dd/MM/yyyy"));
			}

			if (result.getCampaignId() != DEFAULT_ID) {
				String content = result.getParameters().getProperty(
						"FreeSMSTemp", "");
				content = content.replaceAll("~SERVICE_EXPIRE_DATE~",
						StringUtil.format(subProduct.getExpirationDate(),
								"dd/MM/yyyy"));

				Calendar chargeDate = Calendar.getInstance();
				chargeDate.setTime(subProduct.getExpirationDate());
				chargeDate.add(Calendar.DATE, 1);
				content = content.replaceAll("~SERVICE_START_DATE~",
						StringUtil.format(chargeDate.getTime(), "dd/MM/yyyy"));

				result.setResponseValue(ResponseUtil.SMS_TEXT, content);
			}

			try {
				if (result.getSubscriberType() == Constants.PREPAID_SUB_TYPE) {
					ProductEntry product = ProductFactory.getCache()
							.getProduct(result.getProductId());
					String promotionEnd = product.getParameter("PromotionEnd",
							"");
					String promotionStart = product.getParameter(
							"PromotionStart", "");

					Date dateStart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
							.parse(promotionStart);
					Date dateEnd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
							.parse(promotionEnd);
					Date now = new Date();
					if (dateEnd.after(now) && dateStart.before(now)) {
						ArrayList<SubscriberProduct> historyRegister = SubscriberProductImpl
								.checkRegisterService(result.getIsdn(),
										promotionStart, result.getProductId(),
										activationDate);
						if (historyRegister != null
								&& historyRegister.size() == 1) {
							String servicePromotion = product.getParameter(
									"ServiceAddress", "");
							String productPromotion = product.getParameter(
									"ProductPromotion", "");
							CommandMessage order = new CommandMessage();
							order.setChannel(Constants.CHANNEL_CORE);
							order.setUserId(0);
							order.setUserName("system");
							order.setServiceAddress(servicePromotion);
							order.setIsdn(result.getIsdn());
							order.setTimeout(60000);

							String dataPackageID = product.getParameter(
									"DataPackageID", "");
							ArrayList<SubscriberProduct> listSubProduct = SubscriberProductImpl
									.getActive(result.getIsdn(), dataPackageID);
							if (listSubProduct != null
									&& listSubProduct.size() > 0) {
								order.setKeyword("FREE_DATA_"
										+ productPromotion);
							} else {
								order.setKeyword("FREE_" + productPromotion);
							}

							instance.sendMessage(
									QueueFactory.ORDER_REQUEST_QUEUE, order,
									60000);
						}
					}
				}
			} catch (Exception e) {
			}
		} catch (Exception error) {
			processError(instance, provisioningCommand, request, error);
		}

		return result;
	}

	public String getActivationDate(CommandMessage request) throws Exception {
		String activationDate = "";
		try {
			if (request.getSubscriberType() == Constants.PREPAID_SUB_TYPE) {
				SubscriberEntity subscriberEntity = ((VNMMessage) request)
						.getSubscriberEntity();
				Calendar dataEnterActive = subscriberEntity
						.getDateEnterActive();
				activationDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.format(dataEnterActive.getTime());
			}
		} catch (Exception e) {
		}
		return activationDate;
	}

	// HungDT add check spam
	public CommandMessage spam(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CommandMessage result = request;
		try {
			int spamResult = SpamImpl.addSpam(
					String.valueOf(request.getProductId()),
					String.valueOf(request.getCommandId()),
					request.getIsdn(),
					"ISDN",
					"",
					request.getUserId(),
					request.getUserName(),
					request.getGroupId(),
					Integer.parseInt(request.getParameters().getProperty(
							"request.merchantid")), request.getCompanyId(),
					Database.getConnection());
			if (spamResult == Constants.EXPIRE_SPAM_ACTION) {
				instance.getDispatcher().debugMonitor(
						"Enable spam for isdn = " + request.getIsdn());
				ProductRoute productRoute = ProductFactory.getCache()
						.getProductRoute(request.getRouteId());
				result.setDescription(request.getDescription()
						+ " - Dich vu bi tam khoa do nhap sai qua 5 lan. Quy khach vui long thuc hien lai sau 1 gio.");
			}
		} catch (Exception e) {
			processError(instance, provisioningCommand, request, e);
		}
		return result;
	}

	public int checkSpam(CommandMessage request) throws Exception {
		int spamResult = 0;
		try {
			spamResult = SpamImpl.addSpam(
					String.valueOf(request.getProductId()),
					String.valueOf(request.getCommandId()), request.getIsdn(),
					"ISDN", "", request.getUserId(), request.getUserName(),
					request.getGroupId(), request.getMerchantId(),
					request.getCompanyId(), Database.getConnection());
		} catch (Exception e) {
			throw e;
		}
		return spamResult;
	}

	// registerID
	public CommandMessage registerIDD(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {

		return request;

	}

	public CommandMessage unregisterIDD(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {

		return request;

	}

	public CommandMessage confirmRegister(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {

		return request;

	}
}
