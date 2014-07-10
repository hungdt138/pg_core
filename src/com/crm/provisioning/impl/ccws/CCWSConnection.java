package com.crm.provisioning.impl.ccws;

import com.comverse_in.prepaid.ccws.ServiceSoapStub;

import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.WSConstants;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;

import com.comverse_in.prepaid.ccws.AccumulatorEntity;
import com.comverse_in.prepaid.ccws.ArrayOfBalanceEntityBase;
import com.comverse_in.prepaid.ccws.ArrayOfCircleMemberOperation;
import com.comverse_in.prepaid.ccws.ArrayOfCircleMembership;
import com.comverse_in.prepaid.ccws.ArrayOfCircleOperationResponse;
import com.comverse_in.prepaid.ccws.ArrayOfOSAHistory;
import com.comverse_in.prepaid.ccws.ArrayOfRechargeHistory;
import com.comverse_in.prepaid.ccws.BalanceEntityBase;
import com.comverse_in.prepaid.ccws.CallingCircle;
import com.comverse_in.prepaid.ccws.CallingCircleOperation;
import com.comverse_in.prepaid.ccws.ChangeCallingCircleRequest;
import com.comverse_in.prepaid.ccws.ChangeCallingCircleResponse;
import com.comverse_in.prepaid.ccws.CircleMember;
import com.comverse_in.prepaid.ccws.CircleMemberOperation;
import com.comverse_in.prepaid.ccws.CircleMembership;
import com.comverse_in.prepaid.ccws.CircleOperation;
import com.comverse_in.prepaid.ccws.CircleOperationResponse;
import com.comverse_in.prepaid.ccws.DeleteCallingCircleRequest;
import com.comverse_in.prepaid.ccws.DeleteCallingCircleResponse;
import com.comverse_in.prepaid.ccws.OfferSubscribeRequest;
import com.comverse_in.prepaid.ccws.OfferUnsubscribeRequest;
import com.comverse_in.prepaid.ccws.RechargeHistory;
import com.comverse_in.prepaid.ccws.RetrieveCallingCirclesRequest;
import com.comverse_in.prepaid.ccws.RetrieveCallingCirclesResponse;
import com.comverse_in.prepaid.ccws.RetrieveCircleMembersRequest;
import com.comverse_in.prepaid.ccws.RetrieveCirclesMembersResponse;
import com.comverse_in.prepaid.ccws.ServiceLocator;
import com.comverse_in.prepaid.ccws.SubscriberBasic;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.SubscriberModify;
import com.comverse_in.prepaid.ccws.SubscriberPB;
import com.comverse_in.prepaid.ccws.SubscriberPPS;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import com.comverse_in.prepaid.ccws.ArrayOfDeltaBalance;
import com.crm.kernel.message.Constants;
import com.crm.provisioning.cache.ProvisioningConnection;
import com.crm.provisioning.message.VNMMessage;
import com.fss.util.AppException;

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
public class CCWSConnection extends ProvisioningConnection
{
	public final static String	DEFAULT_COS		= "MAIN_COS";
	public final static String	CORE_BALANCE	= "Core";

	// private ServiceSoapStub binding = null;
	// private static String url = "http://10.230.1.45/ccws/ccws.asmx";
	public ServiceSoapStub		binding			= null;

	public CCWSConnection()
	{
		super();
	}

	public boolean openConnection() throws Exception
	{
		loadService();

		return super.openConnection();
	}

	// Tai khaon chinh name =""
	public BalanceEntity getBalance(String isdn, String balanceName) throws RemoteException
	{
		BalanceEntity result = null;

		SubscriberRetrieve subs = binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, 7);

		BalanceEntity[] data = subs.getSubscriberData().getBalances().getBalance();
		for (int i = 0; i <= data.length - 1; i++)
		{
			if (data[i].getBalanceName().equals(balanceName))
			{
				result = data[i];
				break;
			}
		}

		//System.out.println("in 2" + String.valueOf(System.currentTimeMillis() - start));

		return result;
	}

	// //////////////////////////////////////////////////////
	// check class of service and compare with list of denied COS
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public static BalanceEntity getBalance(SubscriberEntity subscriber, String balanceName) throws Exception
	{
		try
		{
			if (subscriber == null)
			{
				throw new AppException(Constants.ERROR_SUBSCRIBER_NOT_FOUND);
			}
			else if (balanceName.equals(""))
			{
				throw new AppException(Constants.ERROR_BALANCE_NOT_FOUND);
			}

			BalanceEntity[] balances = subscriber.getBalances().getBalance();

			for (int j = 0; j <= balances.length - 1; j++)
			{
				if (balances[j].getBalanceName().equals(balanceName))
				{
					return balances[j];
				}
			}

			throw new AppException(Constants.ERROR_BALANCE_NOT_FOUND);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public SubscriberRetrieve getSubscriber(String isdn, int queryLevel) throws Exception
	{
		return CCWSHttpQuery.getSubscriber(isdn, getHost(), getUserName(), getPassword(), (int)getTimeout(), queryLevel, getDispatcher());
		//return binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, queryLevel);
	}

	public SubscriberEntity getSubscriberInfor(String isdn, int queryLevel) throws Exception
	{
		return getSubscriber(isdn, queryLevel).getSubscriberData();
	}

	public SubscriberEntity getSubscriberInfor(String isdn) throws Exception
	{
		return getSubscriberInfor(isdn, 1);
	}

	public ArrayOfDeltaBalance rechargeAccountBySubscriber(String strIsdn, String secretCode, String rechargeComment)
			throws Exception
	{
		ArrayOfDeltaBalance LBalance = binding.rechargeAccountBySubscriber(strIsdn, null, secretCode, rechargeComment);

		return LBalance;
	}

	public ServiceSoapStub loadService() throws Exception
	{
		if (getHost().equals(""))
		{
			throw new NullPointerException("CCWS URL is not valid");
		}

		try
		{
			try
			{
				java.net.URL endpoint = new java.net.URL(getHost());

				EngineConfiguration configuration = new FileProvider("client-config-ccws.wsdd");
				ServiceLocator locator = new ServiceLocator(configuration);

				binding = (ServiceSoapStub) locator.getServiceSoap(endpoint);
				binding._setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
				binding._setProperty(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
				binding._setProperty(WSHandlerConstants.USER, getUserName());
				PasswordCallback pwCallback = new PasswordCallback(getUserName(), getPassword());

				binding._setProperty(WSHandlerConstants.PW_CALLBACK_REF, pwCallback);
				binding.setTimeout((int)timeout);
			}
			catch (javax.xml.rpc.ServiceException jre)
			{
				if (jre.getLinkedCause() != null)
				{
					jre.getLinkedCause().printStackTrace();
				}
				throw new Exception("JAX-RPC ServiceException caught: " + jre);
			}

			if (binding == null)
			{
				throw new Exception("Binding is null");
			}
		}
		catch (Exception ex)
		{
			binding = null;

			throw ex;
		}

		return binding;
	}

	/**
	 * Purpose: set value into IDD buffet account.
	 * 
	 * @param isdn
	 *            : subscriber
	 * @param balanceAmount
	 *            : Number of minutes.
	 * @param balanceName
	 *            : Account name.
	 * @param expiredDate
	 *            : Expired Date
	 * @param comment
	 *            : Comment.
	 * @return true: success processing false: failure processing
	 * @throws RemoteException
	 * @date 25/05/2011
	 * @author DuyMB
	 */
	public void setBalance(String isdn, BalanceEntityBase[] balances, Calendar activeDate, String comment) throws RemoteException
	{
		try
		{
			// Set information.
			SubscriberModify objSubModify = new SubscriberModify();
			objSubModify.setSubscriberID(isdn);

			SubscriberPPS subscriber = new SubscriberPPS();
			subscriber.setBalanceChangeComment(comment);

			// Balances
			ArrayOfBalanceEntityBase balanceEntities = new ArrayOfBalanceEntityBase();

			balanceEntities.setBalance(balances);

			subscriber.setBalances(balanceEntities);

			subscriber.setSubscriberDateEnterActive(activeDate);

			objSubModify.setSubscriber(subscriber);

			binding.modifySubscriber(objSubModify);
		}
		catch (RemoteException ex)
		{
			throw ex;
		}
	}

	/**
	 * Purpose: set value into IDD buffet account.
	 * 
	 * @param strIsdn
	 *            : subscriber
	 * @param dBalance
	 *            : Number of minutes.
	 * @param strBalanceName
	 *            : Account name.
	 * @param expiredDate
	 *            : Expired Date
	 * @param strNMSComment
	 *            : Comment.
	 * @return true: success processing false: failure processing
	 * @throws RemoteException
	 * @date 25/05/2011
	 * @author DuyMB
	 */
	public boolean setIDDBuffetAccount(
			String strIsdn, double dBalance, String strBalanceName, Calendar expiredDate, String strNMSComment)
			throws RemoteException
	{
		boolean success = true;

		try
		{
			// Set information.
			SubscriberModify objSubModify = new SubscriberModify();
			objSubModify.setSubscriberID(strIsdn);

			SubscriberPPS subscriber = new SubscriberPPS();
			subscriber.setBalanceChangeComment(strNMSComment);

			// Balances
			ArrayOfBalanceEntityBase balances = new ArrayOfBalanceEntityBase();

			BalanceEntityBase[] ArrayOfbalance = new BalanceEntityBase[1];
			ArrayOfbalance[0] = new BalanceEntityBase();

			ArrayOfbalance[0].setBalance(dBalance);
			ArrayOfbalance[0].setAccountExpiration(expiredDate);
			ArrayOfbalance[0].setBalanceName(strBalanceName);

			balances.setBalance(ArrayOfbalance);

			subscriber.setBalances(balances);

			subscriber.setSubscriberDateEnterActive(expiredDate);

			objSubModify.setSubscriber(subscriber);
			// DuyMB add
			// MTRItem[] MTRItem = new MTRItem[1];
			// MTRItem[0].setName("MTRComment");
			// MTRItem[0].setValue(strNMSComment);
			// ArrayOfMTRItem MTRItems = new ArrayOfMTRItem(MTRItem);
			// MTRDataArray obj = new MTRDataArray(MTRItems);
			// objSubModify.set

			binding.modifySubscriber(objSubModify);
		}
		catch (RemoteException ex)
		{
			success = false;

			throw ex;
		}

		return success;
	}

	/**
	 * Purpose: set phone book list for master subscriber.
	 * 
	 * @param strMasterIsdn
	 * @param subPhoneBookList
	 * @return true: success. false: failure.
	 * @throws RemoteException
	 * @author DuyMB Project: Friend & Family.
	 */
	public boolean setPhoneBook(String strMasterIsdn, SubscriberPB subPhoneBookList) throws RemoteException
	{
		boolean success = true;

		try
		{
			SubscriberModify modyfySub = new SubscriberModify();

			modyfySub.setSubscriberPhoneBook(subPhoneBookList);

			// Set master subscriber.
			modyfySub.setSubscriberID(strMasterIsdn);
			// modyfySub.getSubscriberPhoneBook();
			binding.modifySubscriber(modyfySub);

		}
		catch (RemoteException ex)
		{
			success = false;

			throw ex;
		}

		return success;
	}

	/**
	 * purpose: get activate date.
	 * 
	 * @param strIsdn
	 * @param informationToRetrieve
	 *            (1: get detail information, 8 get phone book)
	 */
	public Calendar getSubActivateDate(String strIsdn, int informationToRetrieve) throws Exception
	{
		Calendar result = Calendar.getInstance();

		try
		{
			SubscriberRetrieve objSubRetri =
					binding.retrieveSubscriberWithIdentityNoHistory(strIsdn, null, informationToRetrieve);

			result = objSubRetri.getSubscriberData().getDateEnterActive();
		}
		catch (Exception ex)
		{
			throw ex;
		}

		return result;
	}

	public String getPBList(String strMasterIsdn) throws Exception
	{
		String result = "";

		try
		{
			SubscriberRetrieve objSubRetrieve =
					binding.retrieveSubscriberWithIdentityNoHistory(strMasterIsdn, null, 8);

			SubscriberPB subscriberPB = objSubRetrieve.getSubscriberPhoneBook();

			result = subscriberPB.getDestNumber1() + "," +
					subscriberPB.getDestNumber2() + "," +
					subscriberPB.getDestNumber3() + "," +
					subscriberPB.getDestNumber4() + "," +
					subscriberPB.getDestNumber5() + "," +
					subscriberPB.getDestNumber6() + "," +
					subscriberPB.getDestNumber7() + "," +
					subscriberPB.getDestNumber8() + "," +
					subscriberPB.getDestNumber9() + "," +
					subscriberPB.getDestNumber10();
		}
		catch (Exception ex)
		{
			throw ex;
		}

		return result;
	}

	/**
	 * Purpose: get Calling circle name.
	 * 
	 * @param isdn
	 * @return Array of Calling circle name.
	 * @throws RemoteException
	 */
	public String[] getCCName(String isdn) throws Exception
	{
		// checkCCNameOfIntroduce
		String[] result = null;

		try
		{
			SubscriberRetrieve objSubRetrieve =
					binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, 8192);

			ArrayOfCircleMembership arrCircleMembership = objSubRetrieve.getCircles();

			CircleMembership[] arrayOfMS = arrCircleMembership.getCircleMembership();

			if (arrayOfMS == null)
			{
				return null;
			}

			result = new String[arrayOfMS.length];

			for (int i = 0; i < arrayOfMS.length; i++)
			{
				result[i] = arrayOfMS[i].getCircleName();
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		return result;
	}

	/**
	 * Purpose: Check calling circle name.
	 * 
	 * @param invalidCCName
	 * @param arrayOfCCName
	 * @return true: is invalid name. false: is valid name.
	 */
	public boolean checkCCNameOfIntroduce(String invalidCCName, String[] arrayOfCCName)
	{
		boolean success = false;
		if (arrayOfCCName == null)
		{
			return false;
		}
		for (int i = 0; i < arrayOfCCName.length; i++)
		{
			if (arrayOfCCName[i].toUpperCase().contains(invalidCCName.toUpperCase()))
			{
				success = true;
				break;
			}
		}
		return success;
	}

	/**
	 * 
	 * @return
	 */
	public String getStudentGroupMember(String circle, String isdn) throws Exception
	{
		String result = "";

		try
		{
			RetrieveCircleMembersRequest request = new RetrieveCircleMembersRequest(circle, null, 50);

			RetrieveCirclesMembersResponse response = binding.retrieveCircleMembers(request);

			CircleMember circleMember[] = response.getMembers();

			for (int i = 0; i < circleMember.length; i++)
			{
				if (!isdn.equals(circleMember[i].getSubscriber().getSubscriberID()))
				{
					result = circleMember[i].getSubscriber().getSubscriberID();
				}
			}
		}
		catch (Exception ex)
		{
			throw ex;
		}

		return result;
	}

	/**
	 * Purpose: Check CC group name
	 * 
	 * @param isdn
	 * @param invalidGroupName
	 * @return true: is invalid CC group name false: is valid CC group name
	 */
	public boolean checkCCGroupName(String[] arrayOfCCName, String invalidGroupName) throws Exception
	{
		boolean success = false;

		RetrieveCallingCirclesRequest request = null;

		if (arrayOfCCName == null)
		{
			return false;
		}
		try
		{
			for (int i = 0; i < arrayOfCCName.length; i++)
			{
				request = new RetrieveCallingCirclesRequest(arrayOfCCName[i], null, 50);
				RetrieveCallingCirclesResponse response = binding.retrieveCallingCircles(request);
				CallingCircle[] arrayOfCC = response.getCircles();

				if (arrayOfCC == null)
				{
					continue;
				}
				for (int j = 0; j < arrayOfCC.length; j++)
				{
					if (arrayOfCC[j].getCallingCircleGroup().toUpperCase().contains(invalidGroupName.toUpperCase()))
					{
						success = true;
						break;
					}
				}
				if (success)
				{
					break;
				}
			}
		}
		catch (Exception e)
		{
			throw e;
		}

		return success;
	}

	/**
	 * Purpose: Check current status of subscriber.
	 * 
	 * @param strAvaiableStatus
	 * @param isdn
	 * @param strParser
	 * @return true: valid false: invalid
	 * @author DuyMB - 21/06/2011
	 */
	public String checkSubStatus(String strAvaiableStatus, String isdn, String strParser) throws Exception
	{
		boolean regular = false;

		String strCurrentSubStatus = "";

		try
		{
			if ("".equals(strAvaiableStatus))
			{
				return "";
			}

			if ("".equals(strParser))
			{
				strParser = ";";
			}

			SubscriberEntity subEntity = getSubscriberInfor(isdn);
			strCurrentSubStatus = subEntity.getCurrentState();
			String array[] = strAvaiableStatus.split(strParser);

			for (int i = 0; i < array.length; i++)
			{
				if (array[i].equals(strCurrentSubStatus))
				{
					regular = true;
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (regular)
		{
			// return validstatus;
		}

		return strCurrentSubStatus;
	}

	/**
	 * Purpose: Create Alco account.
	 * 
	 * @param strAlcoName
	 * @param isdn
	 * @param serviceEnd
	 * @param serviceStart
	 * @return true: success fail: failure.
	 * @throws Exception
	 * @author hoang duc cuong - 15/07/2011 MGM project.
	 */
	public boolean createAlco(String strAlcoName, String isdn, Calendar serviceEnd, Calendar serviceStart) throws Exception
	{
		boolean success = false;

		OfferSubscribeRequest request = new OfferSubscribeRequest(isdn, null, 0);

		request.setName(strAlcoName);
		request.setServiceEnd(serviceEnd);
		request.setServiceStart(serviceStart);

		try
		{
			success = binding.subscribeOffer(request);
		}
		catch (RemoteException e)
		{
			throw e;
		}

		return success;
	}

	/**
	 * Purpose: delete Alco account.
	 * 
	 * @param strAlcoName
	 * @param isdn
	 * @return true: success fail:failure.
	 * @throws Exception
	 * @author hoang duc cuong - 15/07/2011 MGM project.
	 */
	public boolean deleteAlco(String strAlcoName, String isdn) throws Exception
	{
		boolean success = false;

		OfferUnsubscribeRequest request = new OfferUnsubscribeRequest(isdn, null, strAlcoName);

		try
		{
			success = binding.unsubscribeOffer(request);
		}
		catch (RemoteException e)
		{
			throw e;
		}

		return success;
	}

	/**
	 * Purpose: Create Calling Circle.
	 * 
	 * @param isdn
	 * @param circleGroup
	 * @param serviceProvider
	 * @param maxMember
	 * @return true: success fail: Failure.
	 * @author hoang duc cuong - 15/07/2011 MGM project.
	 */
	public boolean createCallingCircle(String circleName, String circleGroup, String serviceProvider, int maxMember)
			throws Exception
	{
		boolean success = false;

		ChangeCallingCircleResponse response = null;

		try
		{
			CallingCircle callingCircle =
					new CallingCircle(circleName, circleGroup, serviceProvider, String.valueOf(maxMember), "", "");

			CallingCircleOperation callingCircleOperation = CallingCircleOperation.CREATE;

			ChangeCallingCircleRequest request = new ChangeCallingCircleRequest(callingCircle, callingCircleOperation);

			response = binding.changeCallingCircle(request);

			success = response.isSuccess();
		}
		catch (Exception e)
		{
			throw e;
		}

		return success;
	}

	/**
	 * Purpose: Add member to Calling circle.
	 * 
	 * @param arrayOfMem
	 * @param isdn
	 * @param circleName
	 * @return true: success false: failure.
	 * @author hoang duc cuong - 15/07/2011 - MGM project.
	 */
	public boolean addMemberToCC(String[] arrayOfMem, String circleName, String operation) throws Exception
	{
		boolean success = false;
		try
		{
			CircleMemberOperation circleMemberOperation[] = new CircleMemberOperation[arrayOfMem.length];

			ArrayOfCircleMemberOperation arrayOfMember = new ArrayOfCircleMemberOperation(circleMemberOperation);
			for (int i = 0; i < arrayOfMem.length; i++)
			{
				CircleMember circleMember = new CircleMember(i, new SubscriberBasic(arrayOfMem[i], ""), false);
				CircleOperation circleOperation = CircleOperation.JOIN;
				if (operation.equalsIgnoreCase("LEAVE"))
				{
					circleOperation = CircleOperation.LEAVE;
				}
				arrayOfMember.setCircleMemberOperation(i, new CircleMemberOperation(circleName, circleMember, circleOperation));
			}
			ArrayOfCircleOperationResponse response = binding.modifyCallingCircleMembers(arrayOfMember);

			for (int i = 0; i < arrayOfMem.length; i++)
			{
				CircleOperationResponse obj = response.getCircleOperationResponse(i);
				if (obj != null)
				{
					success = obj.getCircleName().equals(circleName) &&
							obj.getSubscriber().getSubscriberID().equals(arrayOfMem[i]);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return success;
	}

	public String getInfor(String isdn)
	{
		String result = "";
		try
		{
			SubscriberRetrieve objSubRetrieve =
					binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, 8192);

			ArrayOfCircleMembership arrCircleMembership = objSubRetrieve.getCircles();

			CircleMembership[] arrayOfMS = arrCircleMembership.getCircleMembership();
			if (arrayOfMS == null)
			{
				return "";
			}
			for (int i = 0; i < arrayOfMS.length; i++)
			{
				result = result + "Circle name: " + arrayOfMS[i].getCircleName() + " Position:" + arrayOfMS[i].getPosition()
						+ "\n";
				// System.out.println("Circle name: " +
				// arrayOfMS[i].getCircleName() +" Position:" +
				// arrayOfMS[i].getPosition());
			}

		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Purpose: Delete circle.
	 * 
	 * @param circleName
	 * @return true: success false: failure.
	 * @author hoang duc cuong - MGM Project 20/07/2011.
	 */
	public boolean deleteCC(String circleName)
	{
		boolean success = false;
		try
		{
			DeleteCallingCircleRequest arg0 = new DeleteCallingCircleRequest();
			arg0.setCircleName(circleName);

			DeleteCallingCircleResponse res = binding.deleteCallingCircle(arg0);
			success = res.isSuccess();
		}
		catch (RemoteException e)
		{
			success = false;
			e.printStackTrace();
		}
		return success;
	}

	public String getAccumulator(String isdn, int MaxAccumulator, int information, String AccumulatorPrefix)
	{
		String result = "";

		try
		{
			SubscriberRetrieve objSubscriberRetrieve = binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, information);
			SubscriberEntity objSubscriberEntiy = objSubscriberRetrieve.getSubscriberData();

			AccumulatorEntity[] arrayOfAcc = objSubscriberEntiy.getAccumulator();
			for (int i = 0; i < arrayOfAcc.length; i++)
			{
				if (arrayOfAcc[i].getAccumulatorName().startsWith(AccumulatorPrefix))
				{
					// result[j] = "Thue bao " + isdn +
					// " gioi thieu <referral>: " +
					// arrayOfAcc[i].getAccumulatorName() + " da goi " +
					// arrayOfAcc[i].getAmount() + " giay, con lai " + (1200 -
					// Integer.valueOf(arrayOfAcc[i].getAmount())) + " giay.";
					// j++;
					result = result + "Thue bao " + isdn + " gioi thieu <referral>: " + arrayOfAcc[i].getAccumulatorName()
							+ " da goi " +
							arrayOfAcc[i].getAmount() + " giay, con lai " + (1200 - Integer.valueOf(arrayOfAcc[i].getAmount()))
							+ " giay." + "\n";
				}
			}
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public void setSubscriberState(String isdn, String state) throws Exception
	{
		try
		{
			// Set information.
			SubscriberModify objSubModify = new SubscriberModify();
			objSubModify.setSubscriberID(isdn);

			SubscriberPPS subscriber = new SubscriberPPS();
			subscriber.setCurrentState(state);		
			
			Calendar activeDate = Calendar.getInstance();
			activeDate.setTime(new Date());
			subscriber.setSubscriberDateEnterActive(activeDate);
			
			objSubModify.setSubscriber(subscriber);

			binding.modifySubscriber(objSubModify);
		}
		catch (RemoteException ex)
		{
			throw ex;
		}
	}

	public ArrayOfOSAHistory getOSAHistory(String isdn, int InformationToRetrieve, Calendar startDate, Calendar endDate)
	{
		ArrayOfOSAHistory arrayOfOSAHistory = null;

		try
		{
			SubscriberRetrieve subRetrieve =
					binding.retrieveSubscriberWithIdentityWithHistoryForMultipleIdentities(isdn, null, InformationToRetrieve,
							startDate, endDate, true);

			arrayOfOSAHistory = subRetrieve.getOSAHistories();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return arrayOfOSAHistory;
	}
	
	public boolean nonVoucherRechage(String isdn, double amount, int activeDuration, String comment)
	{
		boolean success = false;
		try {
			success = binding.nonVoucherRecharge(isdn, "", amount, activeDuration, comment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}

	public static void main(String[] arr)
	{
		String strUrl, strUser, strPassword, isdn;
		isdn = "84922000512";
		strUser = "NMS";
		strUrl = "http:///ccws/ccws.asmx";
		// strPassword="Abcd1234%";
		strPassword = "nms!23";
		// Test
	}
}
