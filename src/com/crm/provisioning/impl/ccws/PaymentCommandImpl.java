package com.crm.provisioning.impl.ccws;

import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.axis.AxisFault;

import com.comverse_in.prepaid.ccws.ArrayOfDeltaBalance;
import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.pgw.impl.PaymentServiceImpl;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.util.GeneratorSeq;
import com.crm.util.StringUtil;
import com.fss.util.AppException;

public class PaymentCommandImpl extends CCWSCommandImpl {

	private String[] topupISDNs = new String[0];
	private static int currentISDN = 0;

	public synchronized String getTopupISDN() throws Exception {
		if (topupISDNs.length == 0) {
			throw new Exception("Unknow simulator ISDN !");
		}

		if (currentISDN == (topupISDNs.length - 1)) {
			currentISDN = 0;
		} else {
			currentISDN++;
		}

		return topupISDNs[currentISDN];
	}

	public VNMMessage voucherPostpaidTopup(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		VNMMessage result = CommandUtil.createVNMMessage(request);

		ProductEntry product = ProductFactory.getCache().getProduct(
				result.getProductId());

		ProductRoute productRoute = ProductFactory.getCache().getProductRoute(
				request.getRouteId());
		String list = product.getParameter("topupISDN", "");
		if (!list.equals("")) {
			topupISDNs = StringUtil.toStringArray(list, ",");
		}
		String topupISDN = getTopupISDN();
		String responseCode = "";
		double amount = -1;

		String isdn = CommandUtil.addCountryCode(StringUtil.nvl(
				result.getIsdn(), ""));
		if (isdn.equals("")) {
			isdn = result.getIsdn();
		}

		CCWSConnection connection = null;
		String comment = product.getParameter("comment." + product.getAlias()
				+ "." + request.getActionType(),
				"Comment MTR" + product.getAlias());

		long sessionId = setRequest(
				instance,
				result,
				"com.comverse_in.prepaid.ccws.ServiceSoapStub.rechargeAccountBySubscriber{isdn="
						+ isdn
						+ ",secretCode= "
						+ result.getSecretCode()
						+ ",serial="
						+ result.getSerial()
						+ ",comment="
						+ comment + "}");
		try {
			connection = (CCWSConnection) instance.getProvisioningConnection();

			ArrayOfDeltaBalance balances = connection
					.rechargeAccountBySubscriber(topupISDN,
							result.getSecretCode(), comment);

			for (int j = 0; j < balances.getDeltaBalance().length; j++) {
				if (balances.getDeltaBalance(j).getBalanceName().toUpperCase()
						.equals("CORE")) {
					amount = balances.getDeltaBalance(j).getDelta();
					break;
				}
			}

			if (amount > 0) {
				Properties response = (Properties) request.getParameters();
				response.setProperty("topup", topupISDN);
				response.setProperty("amount", String.valueOf(amount));

				responseCode = "postpaid-payment";

				result.getParameters().setProperty("topup", topupISDN);
				result.setCause(Constants.SUCCESS);

				result.setAmount(amount);
				setResponse(instance, result, Constants.SUCCESS, sessionId);
			} else {
				responseCode = "error";
				result.setCause(Constants.ERROR_CARD);
			}

		} catch (AxisFault error) {
			error.printStackTrace();
			String errorCode = getErrorCode(instance, request, error);
			if (!errorCode.equals("")) {
				setResponse(instance, result, "CCWS-" + errorCode, sessionId);
				result.setStatus(Constants.ORDER_STATUS_DENIED);
				result.setCause("CCWS-"+errorCode);
			} else {
				throw error;
			}
		} catch (Exception e) {
			processError(instance, provisioningCommand, result, e);
		} finally {
			instance.closeProvisioningConnection(connection);
		}

//		if (!responseCode.equals("postpaid-payment")) {
//			boolean checkSpam = productRoute.getParameter("enableSpam", "true")
//					.equals("true");
//			if (checkSpam) {
//				try {
//					int resultSpam = checkSpam(result);
//					if (resultSpam == Constants.EXPIRE_SPAM_ACTION) {
//						throw new AppException(Constants.ERROR_SPAM);
//					}
//				} catch (Exception e) {
//
//					throw e;
//				}
//			}
//		}

		//result.setResponse(responseCode);

		return result;
	}

	public VNMMessage cardCharging(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {

		VNMMessage result = CommandUtil.createVNMMessage(request);

		ProductEntry product = ProductFactory.getCache().getProduct(
				result.getProductId());
		ProductRoute productRoute = ProductFactory.getCache().getProductRoute(
				request.getRouteId());
		String list = product.getParameter("topupISDN", "");
		if (!list.equals("")) {
			topupISDNs = StringUtil.toStringArray(list, ",");
		}
		String topupISDN = getTopupISDN();
		String responseCode = "";
		double amount = -1;

		String isdn = CommandUtil.addCountryCode(StringUtil.nvl(
				result.getIsdn(), ""));
		if (isdn.equals("")) {
			isdn = result.getIsdn();
		}

		CCWSConnection connection = null;

		long sessionId = setRequest(
				instance,
				result,
				"com.comverse_in.prepaid.ccws.ServiceSoapStub.rechargeAccountBySubscriber{secretCode= "
						+ result.getSecretCode()
						+ ",serial="
						+ result.getSerial() + "}");
		try {
			connection = (CCWSConnection) instance.getProvisioningConnection();
			String comment = product.getParameter(
					"comment." + product.getAlias() + "."
							+ request.getActionType(),
					"Comment MTR" + product.getAlias());

			ArrayOfDeltaBalance balances = connection
					.rechargeAccountBySubscriber(topupISDN,
							result.getSecretCode(), comment);

			for (int j = 0; j < balances.getDeltaBalance().length; j++) {
				if (balances.getDeltaBalance(j).getBalanceName().toUpperCase()
						.equals("CORE")) {
					amount = balances.getDeltaBalance(j).getDelta();
					break;
				}
			}

			if (amount > 0) {
				Properties response = (Properties) request.getParameters();
				response.setProperty("topup", topupISDN);
				response.setProperty("amount", String.valueOf(amount));

				responseCode = "card-charging";

				result.getParameters().setProperty("topup", topupISDN);
				result.getParameters().setProperty("amount",
						String.valueOf(amount));
				result.setDescription(comment);
				result.setAmount(amount);
				result.setCause(Constants.SUCCESS);
				setResponse(instance, result, Constants.SUCCESS, sessionId);
			} else {
				responseCode = "error";
				result.setCause(Constants.ERROR_CARD);
			}

		} catch (AxisFault error) {
			error.printStackTrace();
			String errorCode = getErrorCode(instance, request, error);
			if (!errorCode.equals("")) {
				setResponse(instance, result, "CCWS-" + errorCode, sessionId);
				result.setStatus(Constants.ORDER_STATUS_DENIED);
				result.setCause("CCWS-"+errorCode);
			//	result.setDescription("CCWS."+errorCode);
			} else {
				throw error;
			}
		} catch (Exception e) {
			processError(instance, provisioningCommand, result, e);
		} finally {
			instance.closeProvisioningConnection(connection);
		}

//		if (!responseCode.equals("card-charging")) {
//			boolean checkSpam = productRoute.getParameter("enableSpam", "true")
//					.equals("true");
//			if (checkSpam) {
//				try {
//					int resultSpam = checkSpam(result);
//					if (resultSpam == Constants.EXPIRE_SPAM_ACTION) {
//						throw new AppException(Constants.ERROR_SPAM);
//					}
//				} catch (Exception e) {
//
//					throw e;
//				}
//			}
//		}

		//result.setResponse(responseCode);

		return result;
	}

	public VNMMessage nonVoucherRecharge(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception {
		CCWSConnection connection = null;

		VNMMessage result = CommandUtil.createVNMMessage(request);

		ProductEntry product = ProductFactory.getCache().getProduct(
				result.getProductId());

		String isdn = CommandUtil.addCountryCode(StringUtil.nvl(
				result.getIsdn(), ""));
		if (isdn.equals("")) {
			isdn = result.getIsdn();
		}

		String comment = product.getParameter("comment." + product.getAlias()
				+ "." + request.getActionType(),
				"Comment MTR" + product.getAlias());

		String responseCode = "";
		double amount = result.getAmount();
		int activeDuration = PaymentServiceImpl
				.getActiveDuration(amount);
		long sessionId = setRequest(instance, result,
				"com.comverse_in.prepaid.ccws.ServiceSoapStub.nonVoucherRecharge{isdn="
						+ isdn + ",amount= " + amount + ",duration="
						+ activeDuration + ",comment=" + comment + "}");
		try {
			if (CommandUtil.validateAmount(amount)) {
				connection = (CCWSConnection) instance
						.getProvisioningConnection();

				boolean success = connection.nonVoucherRechage(isdn, amount,
						activeDuration, comment);

				if (!success) {
					responseCode = "prepaid-topup error";
					throw new AppException(Constants.ERROR);
				} else {
					responseCode = "prepaid-topup success";
					result.setCause(Constants.SUCCESS);
					setResponse(instance, result, Constants.SUCCESS, sessionId);
				}

				//result.setResponse(responseCode);
				result.setAmount(amount);
			} else {
				result.setStatus(Constants.ORDER_STATUS_DENIED);
				result.setCause(Constants.ERROR_INVALID_AMOUNT);
			}

		} catch (AxisFault error) {
			error.printStackTrace();
			String errorCode = getErrorCode(instance, request, error);
			if (!errorCode.equals("")) {
				setResponse(instance, result, "CCWS-" + errorCode, sessionId);
				result.setStatus(Constants.ORDER_STATUS_DENIED);
			//	result.setCause(Constants.ERROR_CARD);
				result.setCause("CCWS-"+errorCode);
			//	result.setDescription("CCWS."+errorCode);
			} else {
				throw error;
			}
		} catch (Exception e) {
			processError(instance, provisioningCommand, result, e);
		} finally {
			instance.closeProvisioningConnection(connection);
		}

		return result;
	}
}
