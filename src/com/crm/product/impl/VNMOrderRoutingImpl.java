/**
 * 
 */
package com.crm.product.impl;

import java.util.Calendar;
import java.util.Date;

import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;

import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.product.cache.ProductPrice;
import com.crm.product.cache.ProductRoute;
import com.crm.kernel.message.Constants;
import com.crm.util.GeneratorSeq;
import com.crm.util.StringUtil;
import com.crm.provisioning.impl.ccws.CCWSCommandImpl;
import com.crm.provisioning.impl.ccws.CCWSConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.OrderRoutingInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.provisioning.util.ResponseUtil;

import com.fss.util.AppException;

/**
 * @author ThangPV
 * 
 */
public class VNMOrderRoutingImpl extends OrderRoutingImpl
{
	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	@Override
	public String getSubscriberState(OrderRoutingInstance instance, ProductRoute orderRoute, ProductEntry product,
			CommandMessage request) throws Exception
	{
		// TODO Auto-generated method stub
		if (request instanceof VNMMessage)
			return ((VNMMessage)request).getSubscriberEntity() == null ? "" : ((VNMMessage)request).getSubscriberEntity().getCurrentState();
		else
			return super.getSubscriberState(instance, orderRoute, product, request);
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	@Override
	public String getSubscriberCOS(OrderRoutingInstance instance, ProductRoute orderRoute, ProductEntry product,
			CommandMessage request) throws Exception
	{
		if (request instanceof VNMMessage)
			return ((VNMMessage)request).getSubscriberEntity() == null ? "" : ((VNMMessage)request).getSubscriberEntity().getCOSName();
		else
			return super.getSubscriberCOS(instance, orderRoute, product, request);
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
//	public void validateBalance(
//			OrderRoutingInstance instance, ProductRoute orderRoute, ProductEntry product, VNMMessage vnmMessage)
//			throws Exception
//	{
	public void validateBalance(
			OrderRoutingInstance instance, ProductRoute orderRoute, ProductEntry product, VNMMessage vnmMessage, String balanceType, boolean chargeMulti)
			throws Exception
	{
		SubscriberEntity subscriberEntity = null;
		
		try
		{
			subscriberEntity = vnmMessage.getSubscriberEntity();

			//2013-07-25 MinhDT Change for CR charge promotion
//			BalanceEntity balance = CCWSConnection.getBalance(subscriberEntity, CCWSConnection.CORE_BALANCE);
			try
			{
				BalanceEntity balance = CCWSConnection.getBalance(subscriberEntity, balanceType);

				Calendar calendar = Calendar.getInstance();
				if (balance.getAvailableBalance() < product.getMinBalance())
				{
					throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
				}
				else if ((product.getMaxBalance() > 0) && (balance.getAvailableBalance() > product.getMaxBalance()))
				{
					throw new AppException(Constants.ERROR_BALANCE_TOO_LARGE);
				}
				else
				{
					calendar.setTime(new Date());
	
					calendar.add(Calendar.DATE, product.getMaxExpirationDays());
					boolean checkAccountBalance;
					checkAccountBalance = product.getParameter("checkCoreExpireDate","false").equals("true");
					
					if (calendar.after(balance.getAccountExpiration()) && checkAccountBalance)
					{
						throw new AppException(Constants.ERROR_EXPIRE_TOO_LARGE);
					}
				}
	
				// set default price
				vnmMessage.setOfferPrice(product.getPrice());
				ProductPrice productPrice = null;
				if (chargeMulti)
				{
					productPrice =
						product.getProductPrice(
								vnmMessage.getChannel(), vnmMessage.getActionType(), vnmMessage.getSegmentId()
								, vnmMessage.getAssociateProductId(), vnmMessage.getQuantity(), vnmMessage.getOrderDate(), balanceType);
				}
				else
				{
					productPrice =
							product.getProductPrice(
									vnmMessage.getChannel(), vnmMessage.getActionType(), vnmMessage.getSegmentId()
									, vnmMessage.getAssociateProductId(), vnmMessage.getQuantity(), vnmMessage.getOrderDate());
				}
	
				int quantity = 1;
				double fullOfCharge = product.getPrice();
				double baseOfCharge = product.getPrice();
	
				if (vnmMessage.getCampaignId() != Constants.DEFAULT_ID
						&& vnmMessage.getActionType().equals(Constants.ACTION_REGISTER))
				{
					fullOfCharge = 0;
					baseOfCharge = 0;
				}
				else
				{
					if (productPrice != null)
					{
						fullOfCharge = productPrice.getFullOfCharge();
						baseOfCharge = productPrice.getBaseOfCharge();
					}
				}
	
				if (balance.getAvailableBalance() >= fullOfCharge)
				{
					vnmMessage.setPrice(fullOfCharge);
					vnmMessage.setFullOfCharge(true);
				}
				else if (balance.getAvailableBalance() < baseOfCharge)
				{
					throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
				}
				else if (orderRoute.isBaseChargeEnable())
				{
					vnmMessage.setFullOfCharge(false);
					vnmMessage.setPrice(baseOfCharge);
	
					quantity = (int) (balance.getAvailableBalance() / vnmMessage.getPrice());
	
					if (quantity == 0)
					{
						throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
					}
				}
				else
				{
					throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
				}
				
				if (!balanceType.equals(CCWSConnection.CORE_BALANCE) && calendar.after(balance.getAccountExpiration()))
				{
					throw new AppException(Constants.ERROR_OUT_OF_EXPIRE);
				}
	
				vnmMessage.setQuantity(quantity);
				vnmMessage.setAmount(vnmMessage.getPrice() * vnmMessage.getQuantity());
			}
			catch (AppException e)
			{
				if (e.getMessage().equals(Constants.ERROR_BALANCE_NOT_FOUND))
				{
					throw new AppException(Constants.ERROR_NOT_ENOUGH_MONEY);
				}
				else
				{
					throw e;
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public CommandMessage checkBalance(OrderRoutingInstance instance, ProductRoute orderRoute, CommandMessage order)
			throws Exception
	{
		ProductEntry product = null;

		CCWSConnection connection = null;

		SubscriberRetrieve subscriberRetrieve = null;

		SubscriberEntity subscriberEntity = null;

		VNMMessage vnmMessage = CommandUtil.createVNMMessage(order);

		if ((instance.getDebugMode().equals("depend")))
		{
			simulation(instance, orderRoute, vnmMessage);
		}
		else
		{
			try
			{
				long productId = vnmMessage.getProductId();

				product = ProductFactory.getCache().getProduct(productId);

				connection = (CCWSConnection) instance.getProvisioningConnection();

				// get subscriber information in CCWS
				int queryLevel = orderRoute.getParameters().getInteger("prepaid.queryLevel", 1);

				try
				{
					int sessionId = 0;
					try
					{
						sessionId = GeneratorSeq.getNextSeq();
					}
					catch (Exception e)
					{
					}
					String strRequest = (new CCWSCommandImpl())
							.getLogRequest("com.comverse_in.prepaid.ccws.ServiceSoapStub.retrieveSubscriberWithIdentityNoHistory", vnmMessage.getIsdn());
//					instance.logMonitor("SEND: " + strRequest + ". Product= " + product.getAlias() + ". ID=" + sessionId);
					vnmMessage.setRequest("SEND: " + strRequest + ". Product= " + product.getAlias() + ". ID=" + sessionId);
					
					Date startTime = new Date();
					vnmMessage.setRequestTime(new Date());
					
					subscriberRetrieve = connection.getSubscriber(vnmMessage.getIsdn(), queryLevel);
					Date endTime = new Date();
					long costTime = CommandUtil.calculateCostTime(startTime, endTime);
					if (subscriberRetrieve != null)
					{
						subscriberEntity = subscriberRetrieve.getSubscriberData();
						String strResponse = (new CCWSCommandImpl()).getLogResponse(subscriberEntity, vnmMessage.getIsdn());
						
						vnmMessage.setSubscriberType(Constants.PREPAID_SUB_TYPE); // DuyMB fixbug add 20130108
						
						vnmMessage.setResponseTime(new Date());
//						instance.logMonitor("RECEIVE:" + strResponse + ". ID=" + sessionId + ". costTime=" + costTime);
						vnmMessage.setResponse("RECEIVE:" + strResponse + ". ID=" + sessionId + ". costTime=" + costTime);
					}					
				}
				catch (Exception e)
				{
					//vnmMessage.setSubscriberType(SubscriberEntryImpl.getSubscriberType(vnmMessage.getIsdn()));
					//vnmMessage.setSubscriberType(Constants.POSTPAID_SUB_TYPE);
					vnmMessage.setSubscriberType(Constants.UNKNOW_SUB_TYPE);
				}
				finally
				{
					instance.closeProvisioningConnection(connection);
				}

				if (subscriberEntity == null)
				{
					if (vnmMessage.getSubscriberType() == Constants.PREPAID_SUB_TYPE)
					{
						throw new AppException(Constants.ERROR);
					}
				}
				else
				{
					vnmMessage.setSubscriberRetrieve(subscriberRetrieve);
					vnmMessage.setSubscriberType(Constants.PREPAID_SUB_TYPE);

					// Add balance info in response
					BalanceEntity[] balances = subscriberRetrieve.getSubscriberData().getBalances().getBalance();

					for (BalanceEntity balance : balances)
					{
						vnmMessage.setResponseValue(balance.getBalanceName() + ".amount",
								StringUtil.format(balance.getBalance(), "#"));
						vnmMessage.setResponseValue(balance.getBalanceName() + ".expireDate",
								StringUtil.format(balance.getAccountExpiration().getTime(), "dd/MM/yyyy HH:mm:ss"));
					}

					vnmMessage.setResponseValue(ResponseUtil.SERVICE_PRICE, StringUtil.format(product.getPrice(), "#"));
					// End edited

					validateCOS(instance, orderRoute, product, vnmMessage);

					validateState(instance, orderRoute, product, vnmMessage);

					//2013-07-25 MinhDT Change start for CR charge promotion
//					validateBalance(instance, orderRoute, product, vnmMessage);
					boolean notEnough = true;
					String error = "";
					boolean chargeMulti = product.getParameter("ChargeMulti." + order.getActionType(), "false").equals("true");
					if (chargeMulti && product.getAvailBalances().length > 0)
					{
						for (int i=0; i< product.getAvailBalances().length; i++)
						{
							try
							{
								validateBalance(instance, orderRoute, product, vnmMessage, product.getAvailBalances()[i], chargeMulti);
								vnmMessage.setBalanceType(product.getAvailBalances()[i]);
								notEnough = false;
							}
							catch (Exception e)
							{
								error = e.getMessage();
								if (i == product.getAvailBalances().length - 1 && !error.equals(""))
								{
									throw new AppException(error);
								}
								else if (error.equals(Constants.ERROR_NOT_ENOUGH_MONEY))
								{
									notEnough = true;
								}
							}
							
							if (!notEnough)
							{
								break;
							}
						}
					}
					else
					{
						validateBalance(instance, orderRoute, product, vnmMessage, CCWSConnection.CORE_BALANCE, chargeMulti);
					}
				}
				//2013-07-25 MinhDT Change end for CR charge promotion
			}
			catch (AppException e)
			{
				vnmMessage.setCause(e.getMessage());
				vnmMessage.setDescription(e.getContext());
				vnmMessage.setStatus(Constants.ORDER_STATUS_DENIED);
			}
			catch (Exception e)
			{
				throw e;
			}
			finally
			{
				if (vnmMessage != null)
				{
					vnmMessage.setSubscriberRetrieve(subscriberRetrieve);
				}

				instance.closeProvisioningConnection(connection);
			}
		}

		return (vnmMessage == null) ? order : vnmMessage;
	}
}
