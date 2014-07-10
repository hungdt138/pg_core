package com.crm.provisioning.impl.cbs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crm.kernel.message.Constants;
import com.crm.product.cache.ProductEntry;
import com.crm.product.cache.ProductFactory;
import com.crm.provisioning.cache.ProvisioningCommand;
import com.crm.provisioning.impl.CommandImpl;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.thread.CommandInstance;
import com.crm.provisioning.util.ResponseUtil;
import com.crm.subscriber.bean.SubscriberProduct;
import com.crm.subscriber.impl.SubscriberProductImpl;
import com.crm.util.DateUtil;
import com.crm.util.StringUtil;

public class CBSCommandImpl extends CommandImpl
{

	/*
	 * Constants Keyword
	 */
	private final static String ACTION_CHAN_ON = "CHAN ON";
	private final static String ACTION_CHAN_OFF = "CHAN OFF";
	private final static String ACTION_NHAN_ON = "NHAN ON";
	private final static String ACTION_NHAN_OFF = "NHAN OFF";
	private final static String ACTION_CHAN_XOA_ALL = "CHAN XOA ALL";
	private final static String ACTION_NHAN_XOA_ALL = "NHAN XOA ALL";
	private final static String ACTION_CHAN_XOA = "CHAN XOA ";
	private final static String ACTION_NHAN_XOA = "NHAN XOA ";
	private final static String ACTION_CHAN_ADD = "CHAN ";
	private final static String ACTION_NHAN_ADD = "NHAN ";
	
	private final static int CHAN_OFF_PARAM = 0;
	private final static int CHAN_ON_PARAM = 1;
	private final static int NHAN_ON_PARAM = 2;
	private final static int NHAN_OFF_PARAM = 3;
	
	private final static String REPS_CHAN_ON = "chan-on.";
	private final static String REPS_CHAN_OFF = "chan-off.";
	private final static String REPS_NHAN_ON = "nhan-on.";
	private final static String REPS_NHAN_OFF = "nhan-off.";
	private final static String REPS_CHAN_XOA_ALL = "chan-xoa-all.";
	private final static String REPS_NHAN_XOA_ALL = "nhan-xoa-all.";
	private final static String REPS_CHAN_XOA = "chan-xoa.";
	private final static String REPS_NHAN_XOA = "nhan-xoa.";
	private final static String REPS_CHAN_ADD = "chan-add.";
	private final static String REPS_NHAN_ADD = "nhan-add.";
	
	private final static String ERROR_WRONG_FORMAT = "wrong-format";
	private final static String ERROR_LIST_EMPTY = "empty";
	/*
	 * Function registerService
	 */
	public CommandMessage registerService(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			CBSConnection connection = null;
			
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());
			try
			{
				String strPackageType = product.getParameter("PackageType",	"BASIC");
				int iType = Integer.parseInt(product.getParameter("TypeCharge", "0"));
				if (request.getCampaignId() != Constants.DEFAULT_ID)
				{
					iType = Integer.parseInt(product.getParameter("TypeFree", "1"));;
				}
				
				try
				{
					connection = (CBSConnection) instance.getProvisioningConnection();
					int sessionId = setRequestLog(instance, request, "addAccount(" + request.getIsdn() + ", " + strPackageType + ", " + iType + ")");
					int errorCode = connection.register(request.getIsdn(),
							strPackageType, iType);
					setResponse(instance, request, "CBS." + errorCode, sessionId);
					responseCode = connection.getResponse(errorCode);
				}
				finally
				{
					instance.closeProvisioningConnection(connection);
				}
				
				String[] expectedResult = StringUtil.toStringArray(provisioningCommand.getParameter("expectedResult", ""), ";");
				boolean found = false;
				for (int i = 0; i < expectedResult.length; i++)
				{
					if (responseCode.equals(expectedResult[i]))
					{
						found = true;
						request.setCause(Constants.SUCCESS);
						break;
					}
				}
				
				if (!found)
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return request;
	}

	/*
	 * Function unregisterService
	 */
	public CommandMessage unregisterService(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			CBSConnection connection = null;
			try
			{
				connection = (CBSConnection) instance.getProvisioningConnection();

				int sessionId = setRequestLog(instance, request, "removeAccount(" + request.getIsdn() + ")");
				int errorCode = connection.unregister(request.getIsdn());
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = connection.getResponse(errorCode);

				String[] expectedResult = StringUtil.toStringArray(provisioningCommand.getParameter("expectedResult", ""), ";");
				boolean found = false;
				for (int i = 0; i < expectedResult.length; i++)
				{
					if (responseCode.equals(expectedResult[i]))
					{
						found = true;
						request.setCause(Constants.SUCCESS);
						break;
					}
				}
				
				if (!found)
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return request;
	}

	/*
	 * Function reactiveService
	 */
	public CommandMessage reactiveService(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			CBSConnection connection = null;
			try
			{
				connection = (CBSConnection) instance.getProvisioningConnection();
				int sessionId = setRequestLog(instance, request, "enableAccount(" + request.getIsdn() + ")");
				int errorCode = connection.reactive(request.getIsdn());
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = connection.getResponse(errorCode);

				String[] expectedResult = StringUtil.toStringArray(provisioningCommand.getParameter("expectedResult", ""), ";");
				boolean found = false;
				for (int i = 0; i < expectedResult.length; i++)
				{
					if (responseCode.equals(expectedResult[i]))
					{
						found = true;
						request.setCause(Constants.SUCCESS);
						break;
					}
				}
				
				if (!found)
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return request;
	}

	/*
	 * Function deactiveService
	 */
	public CommandMessage deactiveService(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			CBSConnection connection = null;
			try
			{
				connection = (CBSConnection) instance.getProvisioningConnection();
				int sessionId = setRequestLog(instance, request, "disableAccount(" + request.getIsdn() + ")");
				int errorCode = connection.deactive(request.getIsdn());
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = connection.getResponse(errorCode);

				String[] expectedResult = StringUtil.toStringArray(provisioningCommand.getParameter("expectedResult", ""), ";");
				boolean found = false;
				for (int i = 0; i < expectedResult.length; i++)
				{
					if (responseCode.equals(expectedResult[i]))
					{
						found = true;
						request.setCause(Constants.SUCCESS);
						break;
					}
				}
				
				if (!found)
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return request;
	}

	/*
	 * Function updateService
	 */
	public CommandMessage updateService(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		if (instance.getDebugMode().equals("depend"))
		{
			simulation(instance, provisioningCommand, request);
		}
		else
		{
			ProductEntry product = ProductFactory.getCache().getProduct(
					request.getProductId());
			
			CBSConnection connection = null;
			try
			{
				String strPackageType = product.getParameter("PackageType",	"BASIC");
				
				connection = (CBSConnection) instance.getProvisioningConnection();
				int sessionId = setRequestLog(instance, request, "updatePackage(" + request.getIsdn() + ")");
				int errorCode = connection.updatePackage(request.getIsdn(), strPackageType);
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = connection.getResponse(errorCode);

				String[] expectedResult = StringUtil.toStringArray(provisioningCommand.getParameter("expectedResult", ""), ";");
				boolean found = false;
				for (int i = 0; i < expectedResult.length; i++)
				{
					if (responseCode.equals(expectedResult[i]))
					{
						found = true;
						request.setCause(Constants.SUCCESS);
						break;
					}
				}
				
				if (!found)
				{
					request.setCause(Constants.ERROR);
				}
			}
			catch (Exception e)
			{
				processError(instance, provisioningCommand, request, e);
			}
			finally
			{
				instance.closeProvisioningConnection(connection);
			}
		}

		return request;
	}

	/*
	 * Function changeBlackList
	 */
	public CommandMessage changeBlackList(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		CBSConnection connection = null;
		try
		{
			connection = (CBSConnection) instance
					.getProvisioningConnection();
			
			String strRequest = request.getKeyword().trim().toUpperCase();
			
			if (ACTION_CHAN_ON.equals(strRequest))
			{
				int sessionId = setRequestLog(instance, request, "switchBlacklistWhitelist(" + request.getIsdn() + ", " + CHAN_ON_PARAM + ")");
				int errorCode = connection.switchList(request.getIsdn(),	CHAN_ON_PARAM);
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = REPS_CHAN_ON + connection.getResponse(errorCode);
			}
			else if (ACTION_CHAN_OFF.equals(strRequest))
			{
				int sessionId = setRequestLog(instance, request, "switchBlacklistWhitelist(" + request.getIsdn() + ", " + CHAN_OFF_PARAM + ")");
				int errorCode = connection.switchList(request.getIsdn(),	CHAN_OFF_PARAM);
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = REPS_CHAN_OFF + connection.getResponse(errorCode);
			}
			else if (ACTION_CHAN_XOA_ALL.equals(strRequest))
			{
				int sessionId = setRequestLog(instance, request, "removeAllBlackListMember(" + request.getIsdn() + ")");
				int errorCode = connection.removeAllBlackList(request.getIsdn());
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = REPS_CHAN_XOA_ALL + connection.getResponse(errorCode);
			}
			else if (strRequest.startsWith(ACTION_CHAN_XOA))
			{
				String strList = strRequest.trim().substring(ACTION_CHAN_XOA.length(),
								strRequest.length()).trim();
				if (verifyNumber(convertFormatNumber(strList)))
				{
					int sessionId = setRequestLog(instance, request, "removeBlackListMember(" + request.getIsdn() + ", List: " + strList + ")");
					int errorCode = connection.removeBlackList(request.getIsdn(), strList);
					setResponse(instance, request, "CBS." + errorCode, sessionId);
					responseCode = REPS_CHAN_XOA + connection.getResponse(errorCode);
					request.setResponseValue(ResponseUtil.BLACK_LIST, strList);
				}
				else
				{
					responseCode = REPS_CHAN_XOA + ERROR_WRONG_FORMAT;
				}
			}
			else
			{
				String strList = strRequest.trim().substring(ACTION_CHAN_ADD.length(),
						strRequest.length()).trim();
				if (verifyNumber(convertFormatNumber(strList)))
				{
					int sessionId = setRequestLog(instance, request, "addBlackListMember(" + request.getIsdn() + ", List: " + strList + ")");
					int errorCode = connection.addBlackList(request.getIsdn(), strList);
					setResponse(instance, request, "CBS." + errorCode, sessionId);
					responseCode = REPS_CHAN_ADD + connection.getResponse(errorCode);
					request.setResponseValue(ResponseUtil.BLACK_LIST, strList);
				}
				else
				{
					responseCode = REPS_CHAN_ADD + ERROR_WRONG_FORMAT;
				}
			}
			
			request.setCause(responseCode);
			if (responseCode.contains(Constants.SUCCESS))
			{
				request.setStatus(Constants.ORDER_STATUS_APPROVED);
			}
		}
		catch (Exception e)
		{
			processError(instance, provisioningCommand, request, e);
			request.setCause(Constants.ERROR);
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}

		return request;
	}

	/*
	 * Function changeWhiteList
	 */
	public CommandMessage changeWhiteList(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		CBSConnection connection = null; 
		try
		{
			connection = (CBSConnection) instance
					.getProvisioningConnection();
			
			String strRequest = request.getKeyword().trim().toUpperCase();
			
			if (ACTION_NHAN_ON.equals(strRequest))
			{
				int sessionId = setRequestLog(instance, request, "switchBlacklistWhitelist(" + request.getIsdn() + ", " + NHAN_ON_PARAM + ")");
				int errorCode = connection.switchList(request.getIsdn(), NHAN_ON_PARAM);
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = REPS_NHAN_ON + connection.getResponse(errorCode);
			}
			else if (ACTION_NHAN_OFF.equals(strRequest))
			{
				int sessionId = setRequestLog(instance, request, "switchBlacklistWhitelist(" + request.getIsdn() + ", " + NHAN_OFF_PARAM + ")");
				int errorCode = connection.switchList(request.getIsdn(),	NHAN_OFF_PARAM);
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = REPS_NHAN_OFF + connection.getResponse(errorCode);
			}
			else if (ACTION_NHAN_XOA_ALL.equals(strRequest))
			{
				int sessionId = setRequestLog(instance, request, "removeAllWhiteListMember(" + request.getIsdn() + ")");
				int errorCode = connection.removeAllWhiteList(request.getIsdn());
				setResponse(instance, request, "CBS." + errorCode, sessionId);
				responseCode = REPS_NHAN_XOA_ALL + connection.getResponse(errorCode);
			}
			else if (strRequest.startsWith(ACTION_NHAN_XOA))
			{
				String strList = strRequest.trim().substring(ACTION_NHAN_XOA.length(),
								strRequest.length()).trim();
				if (verifyNumber(convertFormatNumber(strList)))
				{
					int sessionId = setRequestLog(instance, request, "removeWhiteListMember(" + request.getIsdn() + ", List: " + strList + ")");
					int errorCode = connection.removeWhiteList(request.getIsdn(), strList);
					setResponse(instance, request, "CBS." + errorCode, sessionId);
					responseCode = REPS_NHAN_XOA + connection.getResponse(errorCode);
					request.setResponseValue(ResponseUtil.WHITE_LIST, strList);
				}
				else
				{
					responseCode = REPS_NHAN_XOA + ERROR_WRONG_FORMAT;
				}
			}
			else
			{
				String strList = strRequest.trim().substring(ACTION_NHAN_ADD.length(),
						strRequest.length()).replaceAll(" ", "");
				if (verifyNumber(convertFormatNumber(strList)))
				{
					int sessionId = setRequestLog(instance, request, "addWhiteListMember(" + request.getIsdn() + ", List: " + strList + ")");
					int errorCode = connection.addWhiteList(request.getIsdn(), strList);
					setResponse(instance, request, "CBS." + errorCode, sessionId);
					responseCode = REPS_NHAN_ADD + connection.getResponse(errorCode);
					request.setResponseValue(ResponseUtil.WHITE_LIST, strList);
				}
				else
				{
					responseCode = REPS_NHAN_ADD + ERROR_WRONG_FORMAT;
				}
			}
			request.setCause(responseCode);
			if (responseCode.contains(Constants.SUCCESS))
			{
				request.setStatus(Constants.ORDER_STATUS_APPROVED);
			}
		}
		catch (Exception e)
		{
			processError(instance, provisioningCommand, request, e);
			request.setCause(Constants.ERROR);
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}
		
		return request;
	}

	/*
	 * Function logCharge to synchronize charging from Vas-man
	 */
	public CommandMessage logCharge(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		String responseCode = "";

		CBSConnection connection = null;
		try
		{
			String amount = String.valueOf(request.getAmount());
			
			SubscriberProduct subscriberProduct = SubscriberProductImpl.getProduct(request.getSubProductId());
			ProductEntry product = ProductFactory.getCache().getProduct(subscriberProduct.getProductId());
			String subscriptionUnit = request.isFullOfCharge() ? product.getSubscriptionUnit() : "daily";
			int subscriptionPeriod = request.isFullOfCharge() ? product.getSubscriptionPeriod() : request.getQuantity();

			// extend subscription date
//			Date expirationDate = DateUtil.addDate(subscriberProduct.getExpirationDate(), subscriptionUnit, subscriptionPeriod);
			boolean includeCurrentDay = request.getParameters().getBoolean("includeCurrentDay");
			Date expirationDate = subscriberProduct.getExpirationDate();
			Date now = new Date();
			if (expirationDate.getTime() < now.getTime())
			{
				expirationDate = now;
			}
			
			boolean truncExpire = Boolean.parseBoolean(product
					.getParameter("TruncExpireDate", "true"));
			expirationDate = calculateExpirationDate(expirationDate, subscriptionUnit,
					subscriptionPeriod, 1, truncExpire);
			
			if (includeCurrentDay)
			{
				Calendar expiration = Calendar.getInstance();
				expiration.setTime(expirationDate);
				expiration.add(Calendar.DATE, -1);

				expirationDate = expiration.getTime();
			}
			
			connection = (CBSConnection) instance.getProvisioningConnection();
			int sessionId = setRequestLog(instance, request, "insertCharging(" + request.getIsdn() + ", " + amount + ", "
							+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expirationDate) +")");
			int errorCode = connection.logCharge(request.getIsdn(), amount, 2,
					expirationDate);
			setResponse(instance, request, "CBS." + errorCode, sessionId);
			responseCode = connection.getResponse(errorCode);

			String[] expectedResult = StringUtil.toStringArray(provisioningCommand.getParameter("expectedResult", ""), ";");
			boolean found = false;
			for (int i = 0; i < expectedResult.length; i++)
			{
				if (responseCode.equals(expectedResult[i]))
				{
					found = true;
					request.setCause(Constants.SUCCESS);
					break;
				}
			}
			
			if (!found)
			{
				request.setCause(Constants.ERROR);
			}
		}
		catch (Exception e)
		{
			processError(instance, provisioningCommand, request, e);
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}

		return request;
	}

	/*
	 * Function getAllListMember to get all members from list
	 */
	public CommandMessage getAllListMember(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		CBSConnection connection = null;
		try
		{
			String listBlack = "";
			String listWhite = "";
			
			connection = (CBSConnection) instance.getProvisioningConnection();
			int sessionId = setRequestLog(instance, request, "getBlackList(" + request.getIsdn() + "), getWhiteList(" + request.getIsdn() + ")");
			listBlack = connection.getBlackList(request.getIsdn());
			listWhite = connection.getWhiteList(request.getIsdn());
			setResponse(instance, request, "ListBlack: " + listBlack + ", ListWhite: " + listWhite, sessionId);
			
			if (listBlack.equals("") && listWhite.equals(""))
			{
				request.setCause(ERROR_LIST_EMPTY);
			}
			else
			{
				request.setResponseValue(ResponseUtil.BLACK_LIST, listBlack == "" ? "khong co" : listBlack);
				request.setResponseValue(ResponseUtil.WHITE_LIST, listWhite == "" ? "khong co" : listWhite);
			}
		}
		catch (Exception e)
		{
			processError(instance, provisioningCommand, request, e);
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}

		return request;
	}

	/*
	 * Function removeAllList to delete all subs from list
	 */
	public CommandMessage removeAllList(CommandInstance instance,
			ProvisioningCommand provisioningCommand, CommandMessage request)
			throws Exception
	{
		String responseCode = "";
		
		CBSConnection connection = null;
		try
		{
			connection = (CBSConnection) instance.getProvisioningConnection();
			int sessionId = setRequestLog(instance, request, "removeAllBlackWhiteListMember(" + request.getIsdn() + ")");
			int errorCode = connection.removeAllList(request.getIsdn());
			setResponse(instance, request, "CBS." + errorCode, sessionId);
			responseCode = connection.getResponse(errorCode);
	
			request.setCause(responseCode);
			if (responseCode.equals(Constants.SUCCESS))
			{
				request.setStatus(Constants.ORDER_STATUS_APPROVED);
			}
		}
		catch (Exception e)
		{
			processError(instance, provisioningCommand, request, e);
		}
		finally
		{
			instance.closeProvisioningConnection(connection);
		}

		return request;
	}

	/*
	 * Function setRequestLog
	 */
	public int setRequestLog(CommandInstance instance, CommandMessage request,
			String requestString) throws Exception
	{

		request.setRequestTime(new Date());
		long sessionId = setRequest(instance, request, requestString);
		if (sessionId > (long) Integer.MAX_VALUE)
			return (int) (sessionId % (long) Integer.MAX_VALUE);
		else
			return (int) sessionId;
	}

	/*
	 * Function verifyNumber
	 */
	public boolean verifyNumber(String[] number) throws Exception
	{
		boolean valid = true;

		for (int i = 0; i < number.length; i++)
		{
			Pattern pattern = Pattern.compile("\\d{11}");
			Matcher matcher = pattern.matcher(number[i]);

			if (!matcher.matches())
			{
				pattern = Pattern.compile("\\d{12}");
				matcher = pattern.matcher(number[i]);
				if (!matcher.matches())
				{
					valid = false;
				}
			}
		}

		return valid;
	}

	/*
	 * Function convertFormatNumber
	 */
	public String[] convertFormatNumber(String listNumber) throws Exception
	{
		String[] number = listNumber.split(",");
		
		for (int i = 0; i < number.length; i++)
		{
			number[i] = number[i].trim();
			if (!number[i].equals("")
					&& number[i].startsWith(Constants.DOMESTIC_CODE))
			{
				number[i] = number[i].substring(Constants.DOMESTIC_CODE
						.length());
				number[i] = Constants.COUNTRY_CODE + number[i];
			}
			listNumber = number[i] + ",";
		}
		
		listNumber.substring(0, listNumber.length()-1);

		return number;
	}
	
	public static Date calculateExpirationDate(Date startDate,
			String subscriptionType, int period, int quantity,
			boolean truncExpire)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		if (period <= 0)
		{
			return null;
		}

		if (subscriptionType.equalsIgnoreCase("monthly")
				|| subscriptionType.equalsIgnoreCase("month"))
		{
			calendar.add(Calendar.DATE, 30 * period * quantity);
		}
		else if (subscriptionType.equalsIgnoreCase("weekly")
				|| subscriptionType.equalsIgnoreCase("week"))
		{
			calendar.add(Calendar.DATE, 7 * period * quantity);
		}
		else if (subscriptionType.equalsIgnoreCase("daily")
				|| subscriptionType.equalsIgnoreCase("day"))
		{
			calendar.add(Calendar.DATE, 1 * period * quantity);
		}

		if (truncExpire)
		{
			// calendar.add(Calendar.DATE, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
		}

		return calendar.getTime();
	}
}
