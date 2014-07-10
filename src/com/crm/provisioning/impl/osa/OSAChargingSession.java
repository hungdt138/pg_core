package com.crm.provisioning.impl.osa;

public class OSAChargingSession
{
	protected String	sessionID					= "";
	protected int		requestNumberFirstRequest	= 0;
	protected String	chargingSessionReference	= "";

	public OSAChargingSession(String sessionID, int requestNumberFirstRequest, String chargingSessionReference)
	{
		this.sessionID = sessionID;
		this.requestNumberFirstRequest = requestNumberFirstRequest;
		this.chargingSessionReference = chargingSessionReference;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}

	public int getRequestNumberFirstRequest()
	{
		return requestNumberFirstRequest;
	}

	public void setRequestNumberFirstRequest(int requestNumberFirstRequest)
	{
		this.requestNumberFirstRequest = requestNumberFirstRequest;
	}

	public String getChargingSessionReference()
	{
		return chargingSessionReference;
	}

	public void setChargingSessionReference(String chargingSessionReference)
	{
		this.chargingSessionReference = chargingSessionReference;
	}

	public String getHost()
	{
		String host = "";
		return host;
	}

	public String getPath()
	{
		String path = "";
		return path;
	}

	public String getScheme()
	{
		String scheme = "http";

		return scheme;
	}
}
