package test;

import com.comverse_in.prepaid.ccws.ServiceSoapStub;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.WSConstants;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;

import com.comverse_in.prepaid.ccws.AccumulatorEntity;
import com.comverse_in.prepaid.ccws.ArrayOfBalanceEntity;
import com.comverse_in.prepaid.ccws.ArrayOfBalanceEntityBase;
import com.comverse_in.prepaid.ccws.ArrayOfCircleMemberOperation;
import com.comverse_in.prepaid.ccws.ArrayOfCircleMembership;
import com.comverse_in.prepaid.ccws.ArrayOfCircleOperationResponse;
import com.comverse_in.prepaid.ccws.ArrayOfIdentityStatus;
import com.comverse_in.prepaid.ccws.ArrayOfMTRItem;
import com.comverse_in.prepaid.ccws.ArrayOfOSAHistory;
import com.comverse_in.prepaid.ccws.ArrayOfOfferHistoryRecord;
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
import com.comverse_in.prepaid.ccws.IdentityStatus;
import com.comverse_in.prepaid.ccws.MTRDataArray;
import com.comverse_in.prepaid.ccws.MTRItem;
import com.comverse_in.prepaid.ccws.OSAHistory;
import com.comverse_in.prepaid.ccws.OfferHistoryRecord;
import com.comverse_in.prepaid.ccws.OfferSubscribeRequest;
import com.comverse_in.prepaid.ccws.OfferUnsubscribeRequest;
import com.comverse_in.prepaid.ccws.RetrieveAccumulatorValueRequest;
import com.comverse_in.prepaid.ccws.RetrieveAccumulatorValueResponse;
import com.comverse_in.prepaid.ccws.RetrieveCallingCirclesRequest;
import com.comverse_in.prepaid.ccws.RetrieveCallingCirclesResponse;
import com.comverse_in.prepaid.ccws.RetrieveCircleMembersRequest;
import com.comverse_in.prepaid.ccws.RetrieveCirclesMembersResponse;
import com.comverse_in.prepaid.ccws.ServiceLocator;
import com.comverse_in.prepaid.ccws.SubscriberBasic;
import com.comverse_in.prepaid.ccws.SubscriberEntity;
import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.SubscriberInfo;
import com.comverse_in.prepaid.ccws.SubscriberModify;
import com.comverse_in.prepaid.ccws.SubscriberPB;
import com.comverse_in.prepaid.ccws.SubscriberPPS;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;

import java.rmi.RemoteException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.comverse_in.prepaid.ccws.ArrayOfDeltaBalance;
import com.crm.provisioning.impl.ccws.PasswordCallback;

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
public class CCWSConnection
{
	// private ServiceSoapStub binding = null;
	// private static String url = "http://10.230.1.45/ccws/ccws.asmx";
	public  ServiceSoapStub	binding		= null;
	private String			url			= "http://10.8.13.140/ccws/ccws.asmx";
	private String			user		= "";
	private String			password	= "";
	private String 			validstatus = "valid";

	public CCWSConnection(String strUrl, String strUser, String strPassword)
	{
		url = strUrl;
		password = strPassword;
		user = strUser;
		loadService();

	}

	public ServiceSoapStub createNewSeccion()
	{
		return loadService();
	}

	// Tai khaon chinh name =""
	public BalanceEntity getBalance(String isdn, String balanceName) throws
			RemoteException
	{
		BalanceEntity result = null;
		
		long start = System.currentTimeMillis();
		
		SubscriberRetrieve subs = binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, 7);

		System.out.println("in " + String.valueOf(System.currentTimeMillis() - start));
		
		BalanceEntity[] data = subs.getSubscriberData().getBalances().getBalance();
		for (int i = 0; i <= data.length - 1; i++)
		{
			if (data[i].getBalanceName().equals(balanceName))
			{
				result = data[i];
				break;
			}
		}
		
		System.out.println("in 2" + String.valueOf(System.currentTimeMillis() - start));
		
		return result;
	}

	public SubscriberEntity getSubscriberInfor(String isdn) throws RemoteException
	{
		SubscriberRetrieve subs = binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, 1);
		return subs.getSubscriberData();
	}

	public ArrayOfDeltaBalance rechargeAccountBySubscriber(String strIsdn, String secretCode, String rechargeComment) throws Exception
	{
		ArrayOfDeltaBalance LBalance = binding.rechargeAccountBySubscriber(strIsdn, null, secretCode, rechargeComment);

		return LBalance;
	}

	private ServiceSoapStub loadService()
	{

		if (url == null || url.equals(""))
			throw new NullPointerException("CCWS URL is not valid");

		try
		{
			try
			{
				java.net.URL endpoint = new java.net.URL(url);
				EngineConfiguration configuration = new FileProvider("client-config-ccws.wsdd");
				ServiceLocator locator = new ServiceLocator(configuration);
				binding = (ServiceSoapStub) locator.getServiceSoap(endpoint);
				binding._setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
				binding._setProperty(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
				binding._setProperty(WSHandlerConstants.USER, user);

				PasswordCallback pwCallback = new PasswordCallback(user, password);
				binding._setProperty(WSHandlerConstants.PW_CALLBACK_REF, pwCallback);
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
			ex.printStackTrace();
		}
		return binding;
	}
	
	/**
	 * Purpose: set value into IDD buffet account. 
	 * @param strIsdn : subscriber
	 * @param dBalance : Number of minutes.
	 * @param strBalanceName : Account name.
	 * @param expiredDate : Expired Date
	 * @param strNMSComment : Comment.
	 * @return true: success processing
	 *         false: failure processing 
	 * @throws RemoteException
	 * @date  25/05/2011 
	 * @author DuyMB 
	 */
	public boolean setIDDBuffetAccount(	String strIsdn,
										double dBalance,
										String strBalanceName,
										Calendar expiredDate,
										String strNMSComment) throws RemoteException{
		boolean success = true;
		try
		{
			// Set information.
			SubscriberModify objSubModify = new SubscriberModify();
			objSubModify.setSubscriberID(strIsdn);
			
			SubscriberPPS subscriber = new SubscriberPPS();
			subscriber.setBalanceChangeComment(strNMSComment);

			// Balances
			ArrayOfBalanceEntityBase  balances = new ArrayOfBalanceEntityBase();

			BalanceEntityBase [] ArrayOfbalance = new BalanceEntityBase[1];
			ArrayOfbalance[0] = new BalanceEntityBase();
			
			ArrayOfbalance[0].setBalance(dBalance);
			ArrayOfbalance[0].setAccountExpiration(expiredDate);
			ArrayOfbalance[0].setBalanceName(strBalanceName);
			
			balances.setBalance(ArrayOfbalance);
			
			subscriber.setBalances(balances);
			
			subscriber.setSubscriberDateEnterActive(expiredDate);

			objSubModify.setSubscriber(subscriber);


			binding.modifySubscriber(objSubModify);
		}
		catch( RemoteException ex)
		{
			success = false;
			ex.printStackTrace();
			throw ex;
		}
		return success;
	}
	
	/**
	 * Purpose: set phone book list for master subscriber.
	 * @param strMasterIsdn
	 * @param subPhoneBookList
	 * @return true: success.
	 *         false: failure.
	 * @throws RemoteException
	 * @author DuyMB
	 * Project: Friend & Family.
	 */
	public boolean setPhoneBook(String strMasterIsdn, SubscriberPB subPhoneBookList) throws RemoteException
	{
		boolean success = true;
		try
		{
			SubscriberModify  modyfySub = new SubscriberModify();
			
			modyfySub.setSubscriberPhoneBook(subPhoneBookList);

			// Set master subscriber.
			modyfySub.setSubscriberID(strMasterIsdn);
			//modyfySub.getSubscriberPhoneBook();
			binding.modifySubscriber(modyfySub);
			
		}
		catch(RemoteException ex)
		{
			success = false;
			ex.printStackTrace();
			throw ex;
		}
		
		return success;
	}
	/**
	 * purpose: get activate date.
	 * @param strIsdn
	 * @param informationToRetrieve (1: get detail information, 8 get phone book)
	 */
	public Calendar getSubActivateDate(String strIsdn, int informationToRetrieve) throws Exception
	{
		Calendar  result = Calendar.getInstance();
		try
		{
			SubscriberRetrieve objSubRetri = 
						binding.retrieveSubscriberWithIdentityNoHistory(strIsdn, null, informationToRetrieve);
			result = objSubRetri.getSubscriberData().getDateEnterActive();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		return result;
	}
	public String getPBList(String strMasterIsdn)
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
			ex.printStackTrace();
		}
		return result;
	}
	/**
	 * Purpose: get Calling circle name. 
	 * @param isdn
	 * @return Array of Calling circle name.
	 */
	public String[]  getCCName(String isdn)
	{
		//checkCCNameOfIntroduce
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
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * Purpose: Check calling circle name.
	 * @param invalidCCName
	 * @param arrayOfCCName
	 * @return true: is invalid name.
	 * 		  false: is valid name.
	 */
	public boolean checkCCNameOfIntroduce(String invalidCCName, String[] arrayOfCCName)
	{
		boolean success = false;
		if (arrayOfCCName == null )
		{
			return false;
		}
		for (int i=0; i < arrayOfCCName.length; i++)
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
	public String getStudentGroupMember(String circle, String isdn)
	{
		String result = "";
		try
		{
			RetrieveCircleMembersRequest request = new RetrieveCircleMembersRequest(circle, null, 50);
			RetrieveCirclesMembersResponse response=  binding.retrieveCircleMembers(request);
			CircleMember circleMember[] =  response.getMembers();
			for (int i =0; i < circleMember.length;i++)
			{
				if (!isdn.equals(circleMember[i].getSubscriber().getSubscriberID()))
				{
					result = circleMember[i].getSubscriber().getSubscriberID();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return result;
	}
	/**
	 * Purpose: Check CC group name 
	 * @param isdn
	 * @param invalidGroupName
	 * @return true: is invalid CC group name
	 *        false: is valid CC group name
	 */
	public boolean checkCCGroupName(String[] arrayOfCCName, String invalidGroupName)
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
				RetrieveCallingCirclesResponse response =  binding.retrieveCallingCircles(request);
				CallingCircle[] arrayOfCC =  response.getCircles();
				
				if (arrayOfCC == null)
				{
					continue;
				}
				for (int j = 0; j < arrayOfCC.length; j ++)
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
			success = true;
			e.printStackTrace();
		}
		return success;
	}
	/**
	 * Purpose: Check current status of subscriber.
	 * @param strAvaiableStatus
	 * @param isdn
	 * @param strParser
	 * @return true: valid 
	 *         false: invalid
	 * @author DuyMB - 21/06/2011
	 */
	public String checkSubStatus(String strAvaiableStatus, String isdn , String strParser)
	{
		boolean regular = false;
		String strCurrentSubStatus = "";
		try
		{
			if ("".equals(strAvaiableStatus))
			{
				return "";
			}
			if ("".equals(strParser)) strParser = ";";
			
			SubscriberEntity subEntity = getSubscriberInfor(isdn);
			strCurrentSubStatus = subEntity.getCurrentState();
			String array[] = strAvaiableStatus.split(strParser);
			for (int i = 0; i < array.length; i++ )
			{
				if (array[i].equals(strCurrentSubStatus))
				{
					regular = true;
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if (regular)
		{
			return validstatus;
		}
		else
		{
			return strCurrentSubStatus;
		}
	}
	/**
	 * Purpose: Create Alco account. 
	 * @param strAlcoName
	 * @param isdn
	 * @param serviceEnd
	 * @param serviceStart
	 * @return true: success fail: failure.
	 * @throws Exception
	 * @author hoang duc cuong - 15/07/2011 MGM project.
	 */
	public boolean  createAlco (String strAlcoName, String isdn, Calendar serviceEnd, Calendar serviceStart) throws Exception
	{
		boolean  success = false;
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
			e.printStackTrace();
			throw e;
		}
		return success;
	}
	/**
	 * Purpose: delete Alco account.
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
			e.printStackTrace();
		}
		return success;
	}
	/**
	 * Purpose: Create Calling Circle.
	 * @param isdn
	 * @param strCCGroup
	 * @param strServiceProvider
	 * @param MaxMember
	 * @return true: success fail: Failure.
	 * @author hoang duc cuong - 15/07/2011 MGM project.
	 */
	public boolean createCallingCircle(String isdn, String strCCGroup, String strServiceProvider, String MaxMember) throws Exception
	{
		boolean success = false;
		ChangeCallingCircleResponse response = null;
		try
		{
			CallingCircle callingCircle = new CallingCircle(isdn + "_" + strCCGroup, strCCGroup, strServiceProvider, MaxMember,"","");
			
			CallingCircleOperation callingCircleOperation = CallingCircleOperation.CREATE;
			
			ChangeCallingCircleRequest request = new ChangeCallingCircleRequest(callingCircle, callingCircleOperation);
			
			response = binding.changeCallingCircle(request);
			
			success = response.isSuccess();
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
			throw e;
		}
		return success;
	}
	/**
	 * Purpose: Add member to Calling circle.
	 * @param arrayOfMem
	 * @param isdn
	 * @param circleName
	 * @return true: success false: failure.
	 * @author hoang duc cuong - 15/07/2011 - MGM project.
	 */
	public boolean addMemberToCC(String[] arrayOfMem,String circleName,String operation) throws Exception
	{
		boolean success = false;
		try
		{
			CircleMemberOperation circleMemberOperation[] = new CircleMemberOperation[arrayOfMem.length]  ;
			
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
			ArrayOfCircleOperationResponse response =  binding.modifyCallingCircleMembers(arrayOfMember);
			
			for (int i = 0; i< arrayOfMem.length; i++)
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
	public String getValidStatus()
	{
		return validstatus;
	}
	public String getInfor(String isdn)
	{
		String result  = "sdd";
		try
		{
			SubscriberRetrieve objSubRetrieve = 
				   binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, 8192);
			
			ArrayOfCircleMembership arrCircleMembership = objSubRetrieve.getCircles();
			
			CircleMembership[] arrayOfMS = arrCircleMembership.getCircleMembership();
			if (arrayOfMS == null)
			{
				return "ss";
			}
			for (int i = 0; i < arrayOfMS.length; i ++)
			{
				result = result + "Circle name: " + arrayOfMS[i].getCircleName() +" Position:" + arrayOfMS[i].getPosition() + "\n";

//				System.out.println("Circle name: " + arrayOfMS[i].getCircleName() +" Position:" + arrayOfMS[i].getPosition());
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
			
			DeleteCallingCircleResponse res =  binding.deleteCallingCircle(arg0);
			success =  res.isSuccess();
		}
		catch (RemoteException e)
		{
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	public String getAccumulator(String isdn, int  MaxAccumulator, int information, String AccumulatorPrefix)
	{
		String  result = "";
		
		try
		{
			SubscriberRetrieve objSubscriberRetrieve = binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, information);
			SubscriberEntity objSubscriberEntiy =  objSubscriberRetrieve.getSubscriberData();
			
			AccumulatorEntity[] arrayOfAcc =  objSubscriberEntiy.getAccumulator();
			int j  = 0;
			for (int i = 0; i < arrayOfAcc.length; i++)
			{
				if (arrayOfAcc[i].getAccumulatorName().startsWith(AccumulatorPrefix))
				{
//					result[j] = "Thue bao " + isdn + " gioi thieu <referral>: " + arrayOfAcc[i].getAccumulatorName() + " da goi " + 
//								arrayOfAcc[i].getAmount() + " giay, con lai " + (1200 - Integer.valueOf(arrayOfAcc[i].getAmount())) + " giay.";
//					j++;
					result = result + "Thue bao " + isdn + " gioi thieu <referral>: " + arrayOfAcc[i].getAccumulatorName() + " da goi " + 
							 arrayOfAcc[i].getAmount() + " giay, con lai " + (1200 - Integer.valueOf(arrayOfAcc[i].getAmount())) + " giay." + "\n";
				}
			}
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	public ArrayOfOSAHistory getOSAHistory(String isdn, int InformationToRetrieve, Calendar startDate, Calendar endDate)
	{
		ArrayOfOSAHistory arrayOfOSAHistory = null;
		try
		{
			SubscriberRetrieve subRetrieve = binding.retrieveSubscriberWithIdentityWithHistoryForMultipleIdentities(isdn, null, InformationToRetrieve, startDate, endDate, true);
			arrayOfOSAHistory =  subRetrieve.getOSAHistories();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return arrayOfOSAHistory;
	}
	
	public String getAcloName(String isdn, String AlcoName)
	{
		try
		{
			SubscriberRetrieve objSubRetrieve = 
				   binding.retrieveSubscriberWithIdentityNoHistory(isdn, null, 8192);
			
			ArrayOfOfferHistoryRecord  C1 = objSubRetrieve.getOfferHistories();
			OfferHistoryRecord[] arrayOfOffer =  C1.getOfferHistory();
			for (int i=0;i< arrayOfOffer.length;i++)
			{
				System.out.println("Alco " + i + ":" + arrayOfOffer[i].getName());
			}

		}

		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return "";
	}
	public void changeStatusofSub(String isdn, String status)
	{
		try
		{
			ArrayOfIdentityStatus arg1 = new ArrayOfIdentityStatus();
			//IdentityStatus is = new IdentityStatus(identity, status);
			
			binding.changeSubStatusAllIdentities(isdn, arg1);		
		}
		catch (Exception ex)
		{
			
		}
	}
	public static void main(String []arr)
	{
		
		String strUrl, strUser, strPassword,isdn;
		isdn="84922000512";
		strUser="NMS";
		strUrl="http://10.8.13.140/ccws/ccws.asmx";
		//strPassword="Abcd1234%";
		strPassword="nms!23";
		// Test
		CCWSConnection k = new CCWSConnection(strUrl, strUser, strPassword);
		try 		
		{
//			String arra[] = k.getCCName("84923332555");
//			for (int i = 0; i< arra.length; i++)
//			{
//				System.out.println(arra[i]);
//			}
//			System.out.println(k.getStudentGroupMember("STUDENT_84923332555", "84923332555"));
			//System.out.println(k.getCCName("841887702527"));
//			System.out.println("Status of subscriber: " + k.checkSubStatus("Active", "84922000512", ";"));
//			Calendar expiredDate = Calendar.getInstance();
//			k.setIDDBuffetAccount(isdn, 600, "GPRS", expiredDate, "D150");
//			System.out.println("suscess");3658700 3688700
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
//			BalanceEntity en =  k.getBalance(isdn, "1DAY_VOICE");
//			SubscriberEntity x = k.getSubscriberInfor("84922000512");
//			System.out.println("COS name: " + x.getCOSName());
//			
			
//			System.out.println("1DAY_VOICE balance:" + new DecimalFormat("#.##").format(en.getBalance()));//3154700 - 3122700
//			System.out.println("Ex 1DAY_VOICE: " + df.format(en.getAccountExpiration().getTime()));			
//			
			
			
			BalanceEntity en1 =  k.getBalance(isdn, "Core");
			System.out.println(k.getInfor("84922000514"));
			System.out.println("Core balance:" + new DecimalFormat("#.##").format(en1.getBalance()));//3154700 - 3122700
//			
//			System.out.println("Ex 1DAY_SMS: " + df.format(en1.getAccountExpiration().getTime()));


//			double DataFree = Double.valueOf("3072") * 1024 * 1024;
//			System.out.println("Max double:" + Double.MAX_VALUE);
//			System.out.println("double:" + DataFree );
//			System.out.println("Avaiable balance:" + en.getAvailableBalance());
//			System.out.println("Status:" + k.checkSubStatus("Active", "84922000512", ";"));
//			 Create CC
//			String strCCGroup = "CCG";
//			String strServiceProvider = "HTC_HAN";
//			String MaxMember = "5";
//			System.out.println("CC is " + k.createCallingCircle(isdn, strCCGroup, strServiceProvider, MaxMember));
//			
//			
//			// Create ALCO.
//			String strAlcoName = "ALCO_MONTHLY_MAXI18";
//			Calendar serviceStart = Calendar.getInstance();
//			Calendar serviceEnd = serviceStart;
//			serviceEnd.add(Calendar.DATE, 30);
//			
			//system.out.println("Result: " + k.createAlco(strAlcoName, isdn, serviceEnd, serviceStart));
//			k.getAcloName(isdn, strAlcoName);
//			String[] arrayOfMem = {"84925000512","84925000525"};
//			k.addMemberToCC(arrayOfMem,"84925000535_CG3","leave");
			
//			System.out.println(k.addMemberToCC(arrayOfMem, "84922000512_CG1","add"));
//			System.out.println(k.deleteAlco(strAlcoName, "84922000512"));
//			System.out.println(k.deleteCC("84922000512_CCG1"));
			
//			k.getInfor(isdn);
//			Calendar obj0106 = Calendar.getInstance();
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = df.parse("0000-03-02");
//			
//			obj0106.setTime(date);			
//			System.out.println(k.getSubActivateDate("84922000514", 1).after(obj0106));
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(k.getSubActivateDate("84922000512", 1).getTime()));
//			
//			SubscriberEntity subEntity = k.getSubscriberInfor("84925000988");
//			System.out.println("Current state: " + subEntity.getCurrentState());
//			String result = k.getAccumulator("84922000512", 5, 1,"ACC_VOICE_CG");
//			System.out.println(result);
//			System.out.println("Ket qua:" + k.checkCCOfIntroduce("2322", "LOVERS_84925000100"));
//			System.out.println("Ket qua:" + k.checkCCOfIntroduce("2322", "84922000512_CG1"));
//			System.out.println("Rss:" + k.getInfor("84922000512"));
//			System.out.println("Rss:" + k.getInfor("84925000535"));
//			System.out.println("Have invalid CC name? : " + k.checkCCOfIntroduce("84925000100", "LOVERS_" + isdn));
			
//			String[] array = k.getCCName("84922000512");
//			for (int i = 0; i < array.length; i++)
//			{
//				System.out.println("CC Name: " + array[i]);
//			}
//			System.out.println(k.checkCCGroupName(array, "CG1"));
//			Calendar startDate = Calendar.getInstance();
//			startDate.setTime(new Date());
//			startDate.add(Calendar.HOUR,0);
//			startDate.add(Calendar.MINUTE,0);
//			startDate.add(Calendar.SECOND,0);
//			
//			Calendar endDate = Calendar.getInstance();
//			endDate.setTime(new Date());
//			endDate.add(Calendar.HOUR,23);
//			endDate.add(Calendar.MINUTE,59);
//			endDate.add(Calendar.SECOND,59);
//			
//			ArrayOfOSAHistory arrayOfOSAHistory = k.getOSAHistory(isdn, 2048, startDate, endDate);
//			OSAHistory[] array = arrayOfOSAHistory.getOSAHistory();
//			for (int i =0; i < array.length; i ++)
//			{
//				System.out.println("Record [" + i +"]" 
//									+ "AccountID," + array[i].getAccountID() + ":" 
//									+ "ApplicationDescription," + array[i].getApplicationDescription() + ":" 
//									+ "ApplicationName," + array[i].getApplicationName() + ":" 
//									+ "Reason," + array[i].getReason());
//				System.out.println("------------------");
//			}
//			Calendar expiredDate = Calendar.getInstance();
//			Date date = new Date();
//			expiredDate.setTime(date);
//			expiredDate.add(Calendar.DATE, 28);
//			expiredDate.add(Calendar.HOUR,23);
//			expiredDate.add(Calendar.MINUTE,59);
//			expiredDate.add(Calendar.SECOND,59);
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			System.out.println("Date:" + df.format(expiredDate.getTime()));
//		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
