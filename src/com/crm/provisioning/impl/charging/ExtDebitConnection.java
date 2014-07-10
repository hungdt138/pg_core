package com.crm.provisioning.impl.charging;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.crm.provisioning.cache.ProvisioningConnection;

public class ExtDebitConnection extends ProvisioningConnection
{
	/**
	 * SEPARATE_CHARS: end of each request
	 */
	public static final String		SEPARATE_CHARS								= "\r\n";			// CRLF
	public static final String		SEPARATOR_CMD								= " ";
	public static final String		SEPARATOR_FIELD								= "&";
	public static final String		SEPARATOR_REQUEST_CMD_FIELD					= "?";
	public static final String		SEPARATOR_RESPONSE_CMD_FIELD				= ":";

	public static final String		REQUEST										= "REQ";
	public static final String		RESPONSE									= "RESP";
	public static final String		INFO										= "INFO";

	public static final String		CMD_LOGON									= "Logon";
	public static final String		CMD_CREATE_SESSION							= "CreateSession";
	public static final String		CMD_HEART_BEAT								= "HeartBeat";
	public static final String		CMD_DEBIT									= "Debit";
	public static final String		CMD_DETROY_SESSION							= "DestroySession";

	public static final String		FIELD_SESSION_ID							= "SessionId=";
	public static final String		FIELD_MSG									= "Msg=";
	public static final String		FIELD_USER									= "UserName=";
	public static final String		FIELD_PASSWORD								= "Password=";
	public static final String		FIELD_ERROR_CODE							= "ErrCode=";
	public static final String		FIELD_ERROR_DETAIL							= "ErrDetail=";
	public static final String		FIELD_TRANSACTION_ID						= "TransId=";
	public static final String		FIELD_TRANSACTION_DATETIME					= "TransDateTime=";
	public static final String		FIELD_CP_ID									= "CpId=";
	public static final String		FIELD_CP_NAME								= "CpName=";
	public static final String		FIELD_NUMBER_A								= "ANumber=";
	public static final String		FIELD_NUMBER_B								= "BNumber=";
	public static final String		FIELD_SUBMIT_TIME							= "SubmitTime=";
	public static final String		FIELD_SENT_TIME								= "SentTime=";
	public static final String		FIELD_SERVICE_STATE							= "ServiceState=";
	public static final String		FIELD_CONTENT_CODE							= "ContCode=";
	public static final String		FIELD_CONTENT_TYPE							= "ContType=";
	public static final String		FIELD_DESCRIPTION							= "Description=";

	public static final String		VALUE_SERVICE_STATE_SUCCESS					= "D";
	public static final String		VALUE_SERVICE_STATE_FAILED					= "U";
	public static final String		VALUE_CONTENT_CODE_VOICE_MO					= "100";
	public static final String		VALUE_CONTENT_CODE_SMS_MO					= "102";
	public static final String		VALUE_CONTENT_CODE_SMS_MT					= "103";
	public static final String		VALUE_CONTENT_CODE_CALL_FORWARD_OR_DIVERT	= "104";
	public static final String		VALUE_CONTENT_CODE_MMS_MO					= "120";
	public static final String		VALUE_CONTENT_CODE_MMS_MT					= "121";
	public static final String		VALUE_CONTENT_CODE_MMS_FORWARDED			= "125";
	public static final String		VALUE_CONTENT_CODE_DATA						= "122";
	public static final String		VALUE_CONTENT_TYPE_DEFAULT					= "1";

	protected TCPClient				client										= null;
	protected ExtDebitDataListener	listener									= null;

	private String					sessionId									= "";

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public ExtDebitConnection()
	{
		super();
	}

	@Override
	public boolean openConnection() throws Exception
	{
		client = new TCPClient(getHost(), getPort());
		client.connect();
		listener = new ExtDebitDataListener();
		listener.setDispatcher(getDispatcher());
		client.setListener(listener);
		client.connect();
		client.start();
		try
		{
			createSession();
			return super.openConnection();
		}
		catch (Exception e)
		{
			client.close();
			throw e;
		}
	}

	@Override
	public boolean closeConnection() throws Exception
	{
		try
		{
			detroySession();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			client.close();
			super.closeConnection();
		}

		return true;
	}
	
	@Override
	public boolean validate() throws Exception
	{
		try
		{
			enquireLink();
			return true;
		}
		catch (Exception e)
		{
			logMonitor(e);
			return false;
		}
	}

	public void enquireLink() throws Exception
	{
		String request = REQUEST + SEPARATOR_CMD + CMD_HEART_BEAT
				+ SEPARATOR_REQUEST_CMD_FIELD + FIELD_SESSION_ID
				+ getSessionId() + SEPARATE_CHARS;

		try
		{
			client.send(request.getBytes());
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void createSession() throws Exception
	{
		String request = REQUEST + SEPARATOR_CMD + CMD_CREATE_SESSION
				+ SEPARATOR_REQUEST_CMD_FIELD + FIELD_USER + getUserName()
				+ SEPARATOR_FIELD + FIELD_PASSWORD + getPassword() + SEPARATE_CHARS;
		try
		{
			String result = (String) sendAndReceive(CMD_CREATE_SESSION, request);

			String errorCode = getFieldValue(FIELD_ERROR_CODE, result);
			if (errorCode.equals("0x0000"))
			{
				setSessionId(getFieldValue(FIELD_SESSION_ID, result));
			}
			else
			{
				throw new Exception(getFieldValue(FIELD_ERROR_DETAIL, result));
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public void detroySession() throws Exception
	{
		String request = REQUEST + SEPARATOR_CMD + CMD_DETROY_SESSION
				+ SEPARATOR_REQUEST_CMD_FIELD + FIELD_SESSION_ID + getSessionId() + SEPARATE_CHARS;

		try
		{
			client.send(request.getBytes());
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public String debit(String request) throws Exception
	{
		String sequence = getFieldValue(FIELD_TRANSACTION_ID, request);
		request = REQUEST + SEPARATOR_CMD + CMD_DEBIT + SEPARATOR_REQUEST_CMD_FIELD
				+ FIELD_SESSION_ID + getSessionId() + SEPARATOR_FIELD + request + SEPARATE_CHARS;

		try
		{
			String result = (String) sendAndReceive(sequence, request);
			result = result.substring(result.indexOf(FIELD_SESSION_ID));
			result = result.substring(result.indexOf(SEPARATOR_FIELD) + SEPARATOR_FIELD.length());

			return result;
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	private Object sendAndReceive(String sequence, String req) throws Exception
	{
		Object notifyObject = new Object();
		listener.setNotifiedObject(sequence, notifyObject);
		synchronized (notifyObject)
		{
			client.send(req.getBytes());
			notifyObject.wait(getTimeout());
			Object result = client.receive(sequence);
			
			if (result == null)
				throw new Exception("timeout");
			return result;
		}
	}

	public static String getFieldValue(String name, String source)
	{
		String value = "";
		int fieldIndex = source.indexOf(name);
		if (fieldIndex >= 0)
		{
			value = source.substring(source.indexOf(name) + name.length());
			int endIndex = value.indexOf(SEPARATOR_FIELD);
			if (endIndex >= 0)
			{
				value = value.substring(0, endIndex);
			}
		}
		return value;
	}

	public static String getCommand(String result)
	{
		int separatorCmdIndex = result.indexOf(SEPARATOR_CMD);
		if (separatorCmdIndex >= 0)
		{
			String command = result.substring(separatorCmdIndex + SEPARATOR_CMD.length());
			int endIndex = command.indexOf(SEPARATOR_REQUEST_CMD_FIELD);
			if (endIndex < 0)
			{
				endIndex = command.indexOf(SEPARATOR_RESPONSE_CMD_FIELD);
			}
			if (endIndex >= 0)
			{
				command = command.substring(0, endIndex);
			}
			return command;
		}
		else
		{
			return "";
		}
	}
	
	public static void main(String[] param)
	{
		ExtDebitConnection conn = new ExtDebitConnection();
		conn.setHost("127.0.0.1");
		conn.setPort(8000);
		conn.setTimeout(5000);

		try
		{
			conn.openConnection();

			String response = conn.debit("TransId=0x000000A3&TransDateTime=20060613153023&CpId=ELC&CpName=ELCOM&ANumber=19001570&BNumber=84923527227&SubmitTime=20060419091135&ServiceState=D&SentTime=20060419091135&ContCode=100&ContType=1&Description=ss");

			System.out.println("response: " + response);

			conn.closeConnection();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
