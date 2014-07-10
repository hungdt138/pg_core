/**
 * 
 */
package com.crm.provisioning.thread;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import javax.jms.Message;

import com.crm.product.cache.ProductAction;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductPrice;
import com.crm.product.cache.ProductRoute;
import com.crm.provisioning.cache.CommandEntry;
import com.crm.provisioning.cache.ProvisioningEntry;
import com.crm.provisioning.cache.ProvisioningFactory;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.CommandUtil;
import com.crm.alarm.cache.AlarmEntry;
import com.crm.alarm.cache.AlarmFactory;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.subscriber.bean.SubscriberOrder;
import com.crm.subscriber.impl.SubscriberEntryImpl;
import com.crm.subscriber.impl.SubscriberOrderImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;

import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class OrderRoutingInstance extends ProvisioningInstance {
	public OrderRoutingInstance() throws Exception {
		super();
	}

	public CommandMessage validateOrder(ProductRoute orderRoute,
			CommandMessage order) throws AppException, Exception {
		try {
			if ((orderRoute == null) || orderRoute.getExecuteMethod() == null) {
				return order;
			}

			Object result = orderRoute.getExecuteMethod().invoke(
					orderRoute.getExecuteImpl(), this, orderRoute, order);

			if (result instanceof CommandMessage) {
				return (CommandMessage) result;
			} else {
				throw new AppException("order-invalid");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public int processMessage(Message request) throws Exception {
		long start = System.currentTimeMillis();
		StringBuilder log = new StringBuilder();

		CommandMessage order = (CommandMessage) QueueFactory
				.getContentMessage(request);

		// order.setRequestValue("executionStartTime", (new
		// java.util.Date()).getTime());

		ProductRoute orderRoute = null;
		ProductEntry product = null;
		ProductAction action = null;
		CommandEntry command = null;

		Exception error = null;

		try {
			if (order.getQuantity() == 0) {
				order.setQuantity(1);
			}

			// source address
			String isdn = CommandUtil.addCountryCode(order.getIsdn());
			order.setIsdn(isdn);

			// destination address
			String shippingTo = CommandUtil.addCountryCode(order.getShipTo());
			order.setShipTo(shippingTo);

			// get command
			if (order.getKeyword().equals("")) {
				throw new AppException("unknow-keyword");
			}

			// get order routing
			if (order.getRouteId() != Constants.DEFAULT_ID) {
				orderRoute = ProductFactory.getCache().getProductRoute(
						order.getRouteId());
			} else {
				orderRoute = ProductFactory.getCache().getProductRoute(
						order.getChannel(), order.getServiceAddress(),
						order.getKeyword(), order.getOrderDate());
			}

			if (orderRoute == null) {
				throw new AppException(Constants.ERROR_INVALID_SYNTAX);
			} else {
				order.setProductId(orderRoute.getProductId());
				order.setActionType(orderRoute.getActionType());

				order.setRouteId(orderRoute.getRouteId());

				// check timeout
				if (orderRoute.getStatus() == Constants.SERVICE_STATUS_DENIED) {
					throw new AppException(Constants.UPGRADING);
				} else if (CommandUtil
						.isTimeout(order, orderRoute.getTimeout())) {
					throw new AppException(Constants.ERROR_TIMEOUT);
				}
			}

			/**
			 * Throws AppException(duplicate request) if the same request is in
			 * processing
			 */
			// ((OrderRoutingThread) dispatcher).checkProcessing(order);
			// checkProcessingPass = true;

			// product
			product = ProductFactory.getCache()
					.getProduct(order.getProductId());

			if (order.getIsdn().equals("")) {
				order.setIsdn(product.getParameter("topupISDN", ""));
			}

			if (product == null) {
				throw new AppException(Constants.ERROR_PRODUCT_NOT_FOUND);
			} else if (product.getStatus() == Constants.SERVICE_STATUS_DENIED) {
				throw new AppException(Constants.UPGRADING);
			}

			// debug
			log.append("get info: " + (System.currentTimeMillis() - start)
					+ " - ");
			start = System.currentTimeMillis();

			/**
			 * Create order
			 */
			SubscriberOrder subscriberOrder = null;
			if ((orderRoute != null) && orderRoute.isCreateOrder()) {
				try {
					subscriberOrder = SubscriberOrderImpl.createOrder(
							order.getUserId(), order.getUserName(),
							order.getOrderDate(), order.getActionType(),
							order.getSubscriberId(), order.getIsdn(),
							order.getSubscriberType(), order.getSubProductId(),
							order.getProductId(), order.getPrice(),
							order.getQuantity(), order.getDiscount(),
							order.getAmount(), order.getScore(),
							order.getCause(), order.getStatus(),
							order.getChannel(), order.getRequestId(), order.getMerchantId(), order.getAgentId());

					// debug
					log.append("create order: "
							+ (System.currentTimeMillis() - start) + " - ");
					start = System.currentTimeMillis();

					order.setOrderDate(subscriberOrder.getOrderDate());
					order.setOrderId(subscriberOrder.getOrderId());

					Long orderDate = QueueFactory.detachOrderList(order
							.getProductId() + "." + order.getIsdn());
					if (orderDate != null
							&& (System.currentTimeMillis() - orderDate
									.longValue()) <= (orderRoute
									.getDuplicateScan() * 1000)) {
						throw new AppException(Constants.ERROR_DUPLICATED);
					} else {
						QueueFactory.attachOrderList(order.getProductId() + "."
								+ order.getIsdn(), order.getOrderDate()
								.getTime());
					}

					// debug
					log.append("check duplicate: "
							+ (System.currentTimeMillis() - start) + " - ");
					start = System.currentTimeMillis();
				} catch (SQLException e) {
					order.setStatus(Constants.ORDER_STATUS_DENIED);

					if (e.getMessage().startsWith("ORA-00001")) {
						order.setCause(Constants.ERROR_DUPLICATED);
					} else {
						order.setCause(Constants.ERROR_CREATE_ORDER_FAIL);
					}
				} catch (AppException e) {
					order.setStatus(Constants.ORDER_STATUS_DENIED);
					order.setCause(e.getMessage());
				} catch (Exception e) {
					order.setStatus(Constants.ORDER_STATUS_DENIED);
					order.setCause(Constants.ERROR_CREATE_ORDER_FAIL);
				}
			}

			if (order.getStatus() != Constants.ORDER_STATUS_DENIED) {
				if (order.getChannel().equals(Constants.CHANNEL_SMS)) {
					CommandMessage logReceiver = new CommandMessage();
					logReceiver = order.clone();
					logReceiver.setRequest(order.getServiceAddress() + " - "
							+ order.getKeyword());
					logReceiver.setResponse(order.getResponse());
					sendCommandLog(logReceiver);
				}

				// debug
				log.append("commandlog sms: "
						+ (System.currentTimeMillis() - start) + " - ");
				start = System.currentTimeMillis();

				if (orderRoute.getExecuteMethod() != null) {
					order = validateOrder(orderRoute, order);
				} else {
					// get subscriber type
					order.setSubscriberType(SubscriberEntryImpl
							.getSubscriberType(order.getIsdn()));
				}

				// debug
				log.append("validate order: "
						+ (System.currentTimeMillis() - start) + " - ");
				start = System.currentTimeMillis();

				// hungdt: payment gw khong get price
				// get price
				// if (!orderRoute.isCheckBalance() && (order.getStatus() !=
				// Constants.ORDER_STATUS_DENIED))
				// {
				// // set default price
				// order.setOfferPrice(product.getPrice());
				//
				// // ProductPrice productPrice =
				// // product.getProductPrice(
				// // order.getChannel(), order.getActionType(),
				// order.getSegmentId()
				// // , order.getAssociateProductId(), order.getQuantity(),
				// order.getOrderDate());
				// //
				// // if (productPrice != null)
				// // {
				// // order.setPrice(productPrice.getFullOfCharge());
				// // }
				// // else
				// // {
				// order.setPrice(product.getPrice());
				// // }
				//
				// order.setAmount(order.getPrice() * order.getQuantity());
				// }

				/**
				 * Update order after validate
				 */
				// if (orderRoute.isCreateOrder())
				// {
				// try
				// {
				// Date startTime = new Date();
				//
				// int subscribertype = -1;
				// if (order.getActionType().equals(Constants.ACTION_INVITE))
				// {
				// subscribertype =
				// order.getParameters().getInteger("INVITER_SUBSCRIBERTYPE");
				// }
				//
				// SubscriberOrderImpl.updateOrder(order.getOrderId(),
				// order.getOrderDate(), order.getActionType(),
				// order.getSubscriberId(), subscribertype,
				// order.getSubProductId(),
				// order.getProductId(), order.getPrice(), order.getQuantity(),
				// order.getDiscount(),
				// order.getAmount(), order.getScore(), order.getCause(),
				// order.getStatus(), order.getChannel());
				//
				// Date endTime = new Date();
				// debugMonitor("Update order(" + order.getIsdn() +
				// ") cost time: "
				// + (endTime.getTime() - startTime.getTime())
				// + "ms");
				// }
				// catch (Exception e)
				// {
				// order.setStatus(Constants.ORDER_STATUS_DENIED);
				// order.setCause(Constants.ERROR_ORDER_NOT_FOUND);
				//
				// logMonitor(e);
				// }
				// }

				boolean isLogEnable = Boolean.parseBoolean(orderRoute
						.getParameter("LogEnable", "false"));
				if (isLogEnable
						&& order.getParameters()
								.getProperty("IsQueryRTBS", "false")
								.equals("true")) {
					order.setProvisioningType("ROUTE");
					sendCommandLog(order);
				}

				/**
				 * Get command
				 */
				if (order.getStatus() != Constants.ORDER_STATUS_DENIED) {
					// get destination queue
					String queueName = orderRoute.getParameter(
							order.getActionType(), "destination.queue", "",
							order.isPrepaid(), orderRoute.getQueueName());

					// get first provisioning command
					// 2013-07-25 MinhDT Change start for CR charge promotion
					// action = product.getProductAction(order.getActionType(),
					// order.getSubscriberType());
					boolean chargeMulti = Boolean.parseBoolean(product
							.getParameter(
									"ChargeMulti." + order.getActionType(),
									"false"));
					if (chargeMulti && order.getBalanceType() != null
							&& !order.getBalanceType().equals("")) {
						action = product.getProductAction(
								order.getActionType(),
								order.getSubscriberType(),
								order.getBalanceType());
					} else {
						action = product.getProductAction(
								order.getActionType(),
								order.getSubscriberType());
					}
					// 2013-07-25 MinhDT Change end for CR charge promotion

					if (action != null) {
						// first execute command for this order
						order.setCommandId(action.getCommandId());

						command = ProvisioningFactory.getCache().getCommand(
								order.getCommandId());

						if (command == null) {
							throw new AppException(
									Constants.ERROR_COMMAND_NOT_FOUND);
						} else {
							order.setProvisioningType(command
									.getProvisioningType());
						}

					} else {
						throw new AppException(
								Constants.ERROR_COMMAND_NOT_FOUND);
					}

					// send provisioning request to command routing queue
					if (queueName.equals("")) {
						sendCommandRouting(order);
					} else {
						String provisioningName = orderRoute.getParameter(
								order.getActionType(),
								"destination.provisioning", "",
								order.isPrepaid(), "");

						if (!provisioningName.equals("")) {
							ProvisioningEntry provisioning = ProvisioningFactory
									.getCache().getProvisioning(
											provisioningName);

							order.setProvisioningId(provisioning
									.getProvisioningId());
						}

						sendMessage(queueName, order, 0);
					}
				}
			}
		} catch (AppException e) {
			order.setCause(e.getMessage());
			order.setDescription(e.getContext());

			error = e;
		} catch (Exception e) {
			order.setCause(Constants.ERROR);
			order.setDescription(e.getMessage());

			error = e;
		} finally {
			// if (checkProcessingPass)
			// ((OrderRoutingThread) dispatcher).removeProcessing(order);
		}

		try {
			if (error != null) {
				order.setStatus(Constants.ORDER_STATUS_DENIED);

				logMonitor(error);
				logMonitor(order.toOrderString());
			} else {
				/**
				 * Add log ISDN: PRODUCT_ALIAS - COMMAND_ALIAS<br>
				 * NamTA<br>
				 * 21/08/2012
				 */
				if (product != null & command != null)
					debugMonitor(order.getIsdn() + ": " + product.getAlias()
							+ " - " + command.getAlias());
				debugMonitor(order.toOrderString());
			}

			if ((orderRoute == null) || !orderRoute.isSynchronous()
					|| (order.getStatus() == Constants.ORDER_STATUS_DENIED)
					|| (command == null)) {
				sendOrderResponse(orderRoute, order);
			}

			if ((error != null) && !(error instanceof AppException)) {
				throw error;
			}

			// debug
			log.append("finish: " + (System.currentTimeMillis() - start)
					+ " - ");
			start = System.currentTimeMillis();
			logMonitor(log.toString());
		} catch (Exception e) {
			getDispatcher().sendInstanceAlarm(error, error.getMessage(),
					order.getProvisioningId(), "");
			throw e;
		}

		return Constants.BIND_ACTION_SUCCESS;
	}
}
