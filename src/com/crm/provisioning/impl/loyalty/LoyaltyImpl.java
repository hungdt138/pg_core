package com.crm.provisioning.impl.loyalty;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.message.VNMMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.CommandUtil;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.SubscriberBalanceImpl;
import com.crm.subscriber.impl.SubscriberProductImpl;

import com.fss.util.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;


public class LoyaltyImpl extends CommandImpl {
	
	public static String NOT_REGISTERED = "not-registered";
	
	@SuppressWarnings("unused")
	public CommandMessage getBalanceAmount(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		
//		try
//		{
//			new SubscriberBalanceImpl();
//			
//			SubscriberBalance subscriberBalance = SubscriberBalanceImpl.getBalance(request.getIsdn());
//			
//			SubscriberProduct subscriberProduct = SubscriberProductImpl.getProduct(request.getProductId());
//			
//			boolean isCheck = (subscriberProduct == null);
//
//			try
//			{
//				Date nowDate = new Date();
//
//				Date expireDate = subscriberBalance.getExpirationDate();
//					
//				boolean compDate =( (long) expireDate.getTime() < (long) nowDate.getTime());
//				// is not member but have points in the balance
//						
//				if(subscriberBalance == null)
//				{
//					request.setCause(Constants.ERROR_BALANCE_NOT_FOUND);
//					return request;
//				}
//	
//				else 
//				{
//					if(compDate)
//					{
//						request.setCause(Constants.ERROR_EXPIRED);
//						return request;
//					}
//				}
//			}
//				catch(Throwable e)
//				{
//					request.setCause(Constants.ERROR_MEMBER_NOT_FOUND);
//					return request;
//				}
//				try
//				{
//					if (isCheck)
//					{
//						request.setCause(Constants.ERROR_MEMBER_NOT_FOUND);
//						int balanceAmount = subscriberBalance.getBalanceAmount();
//						request.setResponseValue(ResponseUtil.SERVICE_AMOUNT_REMAIN, balanceAmount);
//						return request;
//					}
//				
//					else
//					{
//						request.setCause("success");
//						request.setResponseValue(ResponseUtil.SERVICE_START_DATE, subscriberProduct.getRegisterDate());
//						request.setResponseValue(ResponseUtil.SERVICE_AMOUNT_REMAIN, subscriberBalance.getBalanceAmount());
//					}
//				}
//				catch(Throwable e)
//				{
//					
//				}
//		}
//		catch(Throwable e)
//		{
//			request.setCause(Constants.ERROR_EXPIRED);
//
//		}
		return request;
	}
	
	public CommandMessage validateE(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request) throws Throwable
	{
//		try
//		{
////			check Properties in ProductRout
////			ProductRoute pobj = ProductFactory.getCache().getProductRoute(request.getRouteId());
////			pobj.getParameters().getBoolean("true";)
//			
////			request.setCause("success");
//			
//			ProductEntry productEntry = ProductFactory.getCache().getProduct(request.getProductId());
//			
//			SubscriberProduct subscriberProduct = SubscriberProductImpl.getProduct(request.getSubProductId());
//			
//			SubscriberBalance subscriberBalance = SubscriberBalanceImpl.getBalance(request.getIsdn());
//			
//			//check expireDate
//			try
//			{
//				Date nowDate = new Date();
//				Date expireDate = subscriberBalance.getExpirationDate();
//				
//				if(expireDate.getTime() < nowDate.getTime())
//				{
//					request.setCause(Constants.ERROR_EXPIRED);
//					return request;
//				}
//			}	
//			catch(Throwable e)
//			{
//				request.setCause(Constants.ERROR_BALANCE_NOT_FOUND);
//				return request;
//			}
//			
//			
//			int validDays = productEntry.getParameters().getInteger("validDays", 90);
//			
//			//is not registered then return "unknow-member"
//			String isCheckMember = productEntry.getParameters().getString(request.getActionType()+".isCheckMember", "true");
//			
//			try
//			{
//				boolean isCheck = (subscriberProduct == null);
//
//				if(isCheck)
//				{	
//					request.setCause(Constants.ERROR_MEMBER_NOT_FOUND);
//									
//					return request;
//				}
//			}
//			catch(Throwable e)
//			{
////				throw new AppException(Constants.ERROR_MEMBER_NOT_FOUND);
//			}
//			
//			// DOIDIEM need to check validMember
//			if(isCheckMember == "true")
//			{
//				// is not enough registerDate
//				Calendar timeCompare = Calendar.getInstance();
//			    Date registerDate = subscriberProduct.getRegisterDate();
//			    Calendar calRegisterDate = Calendar.getInstance();
//			    calRegisterDate.setTime(registerDate);
//			    
//			    double now = timeCompare.getTimeInMillis() / (1000*60);
//			    double registerDay = calRegisterDate.getTimeInMillis() / (1000*60) + validDays*24*60;
//			    
//			    if((registerDay - now)  > 0)
//			    {
//			    	request.setCause(Constants.ERROR_INVALID_ACTIVE_DATE);
//			    	
//			    	return request;
////					 throw new AppException(Constants.ERROR_INVALID_ACTIVE_DATE);
//			    }
//				
//				String strPoints = request.getParameters().getString("sms.params["+ 0 +"]");
//				//check points is number
//				try
//				{
//					Integer.parseInt(strPoints);
//				}
//				
//				catch(NumberFormatException e)
//				{
//					request.setCause(Constants.ERROR_INVALID_SYNTAX);
//				}
//			
//			int factor = productEntry.getParameters().getInteger("factor", 10);
//			int redeemPoints = Integer.parseInt(strPoints);
//			request.getParameters().setDouble("Amount", redeemPoints/factor);
//			
//			request.getParameters().setString("modifyBalance", "PROMOTION_60");
//			
//			// check isEnoughPoint to redeem
//			double balanceAmount = subscriberBalance.getBalanceAmount();
//
//			if(redeemPoints > (int) balanceAmount )
//				
//				request.setCause(Constants.ERROR_NOT_ENOUGH_MONEY);
//		}
//			
//		}
//		catch(Exception e)
//		{
//			throw e;
//		}
		 
		return request;
	}	

	public VNMMessage redeem(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		VNMMessage result = CommandUtil.createVNMMessage(request);
		
//		Connection connection = Database.getConnection();
//
//		PreparedStatement stmtBalance = null;
//
//		String strPoints = request.getParameters().getString("sms.params["+ 0 +"]");
//
//		try
//		{
//			
//			result.setCause("success");
//			
//			int redeemPoints = Integer.parseInt(strPoints);
//			
//			request.setResponseValue(ResponseUtil.SERVICE_AMOUNT, redeemPoints);
//
//			String uSQL = "update subscriberbalance set balanceAmount = balanceAmount - ? where isdn = ? and balancetype = 'LOYALTY' ";
//
//			stmtBalance = connection.prepareStatement(uSQL);
//			
//			stmtBalance.setDouble(1, Integer.parseInt(strPoints));
//			
//			stmtBalance.setString(2, result.getIsdn());
//
//			stmtBalance.execute();
//
//		}
//		catch(Exception e)
//		{
//			Database.rollback(connection);
//
//			throw e;
//			
//		}
//		finally
//		{
//			Database.closeObject(stmtBalance);
//			Database.closeObject(connection);
//		}
		
		return result;
	}
	
	public VNMMessage unRedeem(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		VNMMessage result = CommandUtil.createVNMMessage(request);
		return result;
	}
	
	public VNMMessage unModifyBalance(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		VNMMessage result = CommandUtil.createVNMMessage(request);
		return result;
	}
}