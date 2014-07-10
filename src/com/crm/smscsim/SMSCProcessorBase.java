package com.crm.smscsim;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.crm.smscsim.util.SMSCUser;
import com.logica.smpp.Data;
import com.logica.smpp.ServerPDUEvent;
import com.logica.smpp.pdu.BindRequest;
import com.logica.smpp.pdu.BindResponse;
import com.logica.smpp.pdu.DataSMResp;
import com.logica.smpp.pdu.DeliverSM;
import com.logica.smpp.pdu.DeliverSMResp;
import com.logica.smpp.pdu.PDU;
import com.logica.smpp.pdu.QuerySM;
import com.logica.smpp.pdu.QuerySMResp;
import com.logica.smpp.pdu.Request;
import com.logica.smpp.pdu.Response;
import com.logica.smpp.pdu.SubmitMultiSMResp;
import com.logica.smpp.pdu.SubmitSM;
import com.logica.smpp.pdu.SubmitSMResp;
import com.logica.smpp.pdu.WrongLengthOfStringException;

public abstract class SMSCProcessorBase implements IProcessor
{
	public static final int		DELIVERED					= 0;
	public static final int		EXPIRED						= 1;
	public static final int		DELETED						= 2;
	public static final int		UNDELIVERABLE				= 3;
	public static final int		ACCEPTED					= 4;
	public static final int		UNKNOWN						= 5;
	public static final int		REJECTED					= 6;

	private static String[]		states;

	static
	{
		states = new String[7];
		states[DELIVERED] = "DELIVRD";
		states[EXPIRED] = "EXPIRED";
		states[DELETED] = "DELETED";
		states[UNDELIVERABLE] = "UNDELIV";
		states[ACCEPTED] = "ACCEPTD";
		states[UNKNOWN] = "UNKNOWN";
		states[REJECTED] = "REJECTD";
	}

	private int					processorId					= 0;
	protected boolean			bound						= false;
	protected boolean			connected					= true;
	private int					messageId					= 1;
	private String				systemId					= "";
	private int					bindType					= Data.BIND_TRANSCEIVER;

	private static final String	DELIVERY_RCPT_DATE_FORMAT	= "yyMMddHHmm";

	private SimpleDateFormat	dateFormatter				= new SimpleDateFormat(DELIVERY_RCPT_DATE_FORMAT);

	private SMSCUser			user						= null;
	protected long				receiveTimeout				= 0;
	protected long				lastTimeReceived			= System.currentTimeMillis();

	@Override
	public int getProcessorId()
	{
		return processorId;
	}

	protected String getName()
	{
		return "[" + systemId + "][#" + processorId + "]";
	}

	public void setProcessorId(int processorId)
	{
		this.processorId = processorId;
	}

	public synchronized long getLastTimeReceived()
	{
		return lastTimeReceived;
	}

	public synchronized void setLastTimeReceived(long lastTimeReceived)
	{
		this.lastTimeReceived = lastTimeReceived;
	}

	public void setLastTimeReceived()
	{
		setLastTimeReceived(System.currentTimeMillis());
	}

	@Override
	public String getSystemId()
	{
		return systemId;
	}

	@Override
	public int getBindType()
	{
		return bindType;
	}

	public abstract ISession getSession();

	public abstract SubmitSMResp processSubmitSM(SubmitSM request);

	public void clientRequest(Request request) throws Exception
	{
		setLastTimeReceived();
		Response response = null;
		int commandId = request.getCommandId();
		int commandStatus = 0;
		if (!bound)
		{ // the first PDU must be bound request
			if (commandId == Data.BIND_TRANSMITTER ||
						commandId == Data.BIND_RECEIVER ||
						commandId == Data.BIND_TRANSCEIVER)
			{
				debugMonitor("Bind request: " + request.debugString());

				commandStatus = authenticate((BindRequest) request);
				if (commandStatus == 0)
				{ // authenticated
					// firstly generate proper bind response
					BindResponse bindResponse =
								(BindResponse) request.getResponse();
					bindResponse.setSystemId(systemId);
					// and send it to the client via serverResponse
					debugMonitor("Bind authenticate for " + getName() + " success.");

					serverResponse(bindResponse);
					// success => bound
					bindType = commandId;
					bound = true;
				}
				else
				{ // system id not authenticated
					// get the response
					response = request.getResponse();
					// set it the error command status
					response.setCommandStatus(commandStatus);
					// and send it to the client via serverResponse
					debugMonitor("Bind authenticate for " + getName() + " fail, status = " + commandStatus);
					serverResponse(response);
					// bind failed, stopping the session

					exit();
				}
			}
			else
			{
				debugMonitor("Receive request from " + getName() + ": " + request.debugString());

				// the request isn't a bound req and this is wrong: if not
				// bound, then the server expects bound PDU
				if (request.canResponse())
				{
					// get the response
					response = request.getResponse();
					response.setCommandStatus(Data.ESME_RINVBNDSTS);
					// and send it to the client via serverResponse
					serverResponse(response);
				}
				else
				{
					// cannot respond to a request which doesn't have
					// a response :-(
				}
				// bind failed, stopping the session
				exit();
			}
		}
		else
		{ // already bound, can receive other PDUs

			debugMonitor("Receive request from " + getName() + ": " + request.debugString());
			if (request.canResponse())
			{
				response = request.getResponse();
				switch (commandId)
				{ // for selected PDUs do extra steps
				case Data.SUBMIT_SM:
					long submitDate = System.currentTimeMillis();
					byte registeredDelivery =
							(byte) (((SubmitSM) request).getRegisteredDelivery() &
							Data.SM_SMSC_RECEIPT_MASK);

					SubmitSMResp submitResponse = processSubmitSM((SubmitSM) request);
					submitResponse.setMessageId(assignMessageId());

					if (registeredDelivery == Data.SM_SMSC_RECEIPT_REQUESTED)
					{
						SubmitSM submit = (SubmitSM) request;
						DeliverSM deliver = new DeliverSM();
						deliver.setSourceAddr(submit.getDestAddr());
						deliver.setDestAddr(submit.getDestAddr());
						String msg = "";
						msg += "id:" + submitResponse.getMessageId() + " ";
						msg += "sub:" + 1 + " ";
						msg += "dlvrd:" + 1 + " ";
						msg += "submit date:" + formatDate(submitDate) + " ";
						msg += "done date:" + formatDate(System.currentTimeMillis()) + " ";
						msg += "stat:" + DELIVERED + " ";
						msg += "err:" + 0 + " ";
						String shortMessage = submit.getShortMessage();
						int msgLen = shortMessage.length();
						msg += "text:" + shortMessage.substring(0, (msgLen > 20 ? 20 : msgLen));
						try
						{
							deliver.setShortMessage(msg);
							deliver.setServiceType(submit.getServiceType());
						}
						catch (WrongLengthOfStringException e)
						{
						}
						serverRequest(deliver);
					}
					break;

				case Data.SUBMIT_MULTI:
					SubmitMultiSMResp submitMultiResponse =
								(SubmitMultiSMResp) response;
					submitMultiResponse.setMessageId(assignMessageId());
					break;

				case Data.DELIVER_SM:
					DeliverSMResp deliverResponse = (DeliverSMResp) response;
					deliverResponse.setMessageId(assignMessageId());
					break;

				case Data.DATA_SM:
					DataSMResp dataResponse = (DataSMResp) response;
					dataResponse.setMessageId(assignMessageId());
					break;

				case Data.QUERY_SM:
					QuerySM queryRequest = (QuerySM) request;
					QuerySMResp queryResponse = (QuerySMResp) response;
					queryResponse.setMessageId(queryRequest.getMessageId());
					break;

				case Data.UNBIND:
					// do nothing, just respond and after sending
					// the response stop the session
					break;
				}
				// send the prepared response
				serverResponse(response);
				if (commandId == Data.UNBIND)
				{
					// unbind causes stopping of the session
					exit();
				}
			}
			else
			{
				// can't respond => nothing to do :-)
			}
		}
	}

	public void clientResponse(Response response)
	{
		setLastTimeReceived();
		debugMonitor("Receive response from " + getName() + ": " + response.debugString());
	}

	public void serverResponse(Response response) throws Exception
	{
		debugMonitor("Send response to " + getName() + ": " + response.debugString());
		getSession().send((PDU) response);
	}

	@Override
	public void serverRequest(Request request) throws Exception
	{
		debugMonitor("Send request to " + getName() + ": " + request.debugString());
		getSession().send((PDU) request);
	}

	private int authenticate(BindRequest request)
	{
		systemId = request.getSystemId();
		return getFactory().authenticate(this, request);
	}

	private String assignMessageId()
	{
		messageId++;
		return "SMSC" + messageId;
	}

	@Override
	public void disconnect()
	{
		try
		{
			if (!connected)
				return;
			debugMonitor("Client " + getName() + " was disconnected.");
		}
		finally
		{
			getFactory().stopProcessor(this);
		}
	}

	private void exit()
	{
		getFactory().stopProcessor(this);
	}

	private boolean isReceiveTimeout()
	{
		if (getLastTimeReceived() + receiveTimeout <= System.currentTimeMillis())
		{
			return true;
		}
		return false;
	}

	@Override
	public void debugMonitor(Object message)
	{
		getFactory().debugMonitor(message);
	}

	private String formatDate(long ms)
	{
		return dateFormatter.format(new Date(ms));
	}

	@Override
	public void setUser(SMSCUser user)
	{
		this.user = user;
		if (user.getTimeout() != 0)
			receiveTimeout = user.getTimeout();
	}

	@Override
	public SMSCUser getUser()
	{
		return user;
	}

	@Override
	public void stop()
	{
		try
		{
			if (!connected)
				return;
			debugMonitor("Session " + getName() + " end.");
			getSession().endSession();
		}
		catch (Exception e)
		{
			debugMonitor(e);
		}
		finally
		{
			bound = false;
			connected = false;
		}
	}

	@Override
	public void handleEvent(ServerPDUEvent event)
	{
		PDU pdu = event.getPDU();
		try
		{
			if (pdu.isRequest())
			{
				try
				{
					clientRequest((Request) pdu);
				}
				catch (Exception e)
				{
					debugMonitor(e);
				}
			}
			else if (pdu.isResponse())
			{
				clientResponse((Response) pdu);
			}
			else
			{
				// MSCSession not reqest nor response => not doing anything.
			}
		}
		catch (Exception e)
		{
			debugMonitor(e);
		}
	}

	@Override
	public boolean validate()
	{
		if (!connected)
			return false;
		if (isReceiveTimeout())
			return false;
		return true;
	}
}
