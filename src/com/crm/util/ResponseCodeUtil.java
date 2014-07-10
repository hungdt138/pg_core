package com.crm.util;

import com.crm.kernel.message.Constants;

public class ResponseCodeUtil
{
	public static String getErrorCode(String cause) throws Exception
	{
		String errorCode = "0";
		
		if (cause.contains(Constants.SUCCESS))
		{
			errorCode = "0";
		}
		else if (cause.equals(Constants.ERROR))
		{
			errorCode = "1";
		}
		else if (cause.equals(Constants.EPOS_COS_CHANGED))
		{
			errorCode = "2";
		}
		else if (cause.equals(Constants.EPOS_COS_CANCELED))
		{
			errorCode = "3";
		}
		else if (cause.equals(Constants.ERROR_RESOURCE_BUSY))
		{
			errorCode = "4";
		}
		else if (cause.equals(Constants.ERROR_INVALID_REQUEST))
		{
			errorCode = "5";
		}
		else if (cause.equals(Constants.ERROR_INVALID_SYNTAX))
		{
			errorCode = "6";
		}
		else if (cause.equals(Constants.ERROR_OVER_TRANSACTION_LIMIT))
		{
			errorCode = "7";
		}
		else if (cause.equals(Constants.ERROR_CREATE_ORDER_FAIL))
		{
			errorCode = "8";
		}
		else if (cause.equals(Constants.ERROR_PRODUCT_NOT_FOUND))
		{
			errorCode = "9";
		}
		else if (cause.equals(Constants.ERROR_ROUTE_NOT_FOUND))
		{
			errorCode = "10";
		}
		else if (cause.equals(Constants.ERROR_CAMPAIGN_NOT_FOUND))
		{
			errorCode = "11";
		}
		else if (cause.equals(Constants.ERROR_SEGMENT_NOT_FOUND))
		{
			errorCode = "12";
		}
		else if (cause.equals(Constants.ERROR_RANK_NOT_FOUND))
		{
			errorCode = "13";
		}
		else if (cause.equals(Constants.ERROR_PROVISIONING_NOT_FOUND))
		{
			errorCode = "14";
		}
		else if (cause.equals(Constants.ERROR_COMMAND_NOT_FOUND))
		{
			errorCode = "15";
		}
		else if (cause.equals(Constants.ERROR_ORDER_NOT_FOUND))
		{
			errorCode = "16";
		}
		else if (cause.equals(Constants.ERROR_SUBSCRIBER_NOT_FOUND))
		{
			errorCode = "17";
		}
		else if (cause.equals(Constants.ERROR_SUBSCRIPTION_NOT_FOUND))
		{
			errorCode = "18";
		}
		else if (cause.equals(Constants.ERROR_MEMBER_NOT_FOUND))
		{
			errorCode = "19";
		}
		else if (cause.equals(Constants.ERROR_GROUP_NOT_FOUND))
		{
			errorCode = "20";
		}
		else if (cause.equals(Constants.ERROR_BALANCE_NOT_FOUND))
		{
			errorCode = "21";
		}
		else if (cause.equals(Constants.ERROR_PROCESS_CLASS))
		{
			errorCode = "22";
		}
		else if (cause.equals(Constants.ERROR_PROCESS_METHOD))
		{
			errorCode = "23";
		}
		else if (cause.equals(Constants.ERROR_UNSUPPORT))
		{
			errorCode = "24";
		}
		else if (cause.equals(Constants.ERROR_DUPLICATED))
		{
			errorCode = "25";
		}
		else if (cause.equals(Constants.ERROR_REGISTERED))
		{
			errorCode = "26";
		}
		else if (cause.equals(Constants.ERROR_UNREGISTERED))
		{
			errorCode = "27";
		}
		else if (cause.equals(Constants.ERROR_EXPIRED))
		{
			errorCode = "28";
		}
		else if (cause.equals(Constants.ERROR_BLACKLIST_PRODUCT))
		{
			errorCode = "29";
		}
		else if (cause.equals(Constants.ERROR_KEYWORD))
		{
			errorCode = "30";
		}
		else if (cause.equals(Constants.ERROR_OUT_OF_TIME))
		{
			errorCode = "31";
		}
		else if (cause.equals(Constants.ERROR_CONNECTION))
		{
			errorCode = "32";
		}
		else if (cause.equals(Constants.ERROR_TIMEOUT))
		{
			errorCode = "33";
		}
		else if (cause.equals(Constants.ERROR_RECURSIVE_COMMAND))
		{
			errorCode = "34";
		}
		else if (cause.equals(Constants.ERROR_DENIED_COS))
		{
			errorCode = "35";
		}
		else if (cause.equals(Constants.ERROR_DENIED_STATUS))
		{
			errorCode = "36";
		}
		else if (cause.equals(Constants.ERROR_DENIED_SUBSCRIBER_TYPE))
		{
			errorCode = "37";
		}
		else if (cause.equals(Constants.ERROR_NOT_ENOUGH_MONEY))
		{
			errorCode = "38";
		}
		else if (cause.equals(Constants.ERROR_BALANCE_TOO_LARGE))
		{
			errorCode = "39";
		}
		else if (cause.equals(Constants.ERROR_EXPIRE_TOO_LARGE))
		{
			errorCode = "40";
		}
		else if (cause.equals(Constants.ERROR_BALANCE_TOO_SMALL))
		{
			errorCode = "41";
		}
		else if (cause.equals(Constants.ERROR_INVALID_OWNER))
		{
			errorCode = "42";
		}
		else if (cause.equals(Constants.ERROR_INVALID_DELIVER))
		{
			errorCode = "43";
		}
		else if (cause.equals(Constants.ERROR_OVER_MEMBER_LIMITATION))
		{
			errorCode = "44";
		}
		else if (cause.equals(Constants.ERROR_EXSITED_MEMBER))
		{
			errorCode = "45";
		}
		else if (cause.equals(Constants.ERROR_INVALID_ACTIVE_DATE))
		{
			errorCode = "46";
		}
		else if (cause.equals(Constants.ERROR_UNREGISTER_INDAY))
		{
			errorCode = "47";
		}
		else if (cause.equals(Constants.ERROR_SUBSCRIPTION_DUPPLICATE))
		{
			errorCode = "48";
		}
		else if (cause.equals(Constants.ERROR_NOTIFY_OVER_BALANCE))
		{
			errorCode = "49";
		}
		else if (cause.equals(Constants.ERROR_NOTIFY_OVER_EXPIRED))
		{
			errorCode = "50";
		}
		
		return errorCode;
	}
}
