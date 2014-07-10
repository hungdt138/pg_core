package com.crm.smscsim.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Message;
import javax.jms.Session;
import javax.naming.NamingException;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.message.CommandMessage;
import com.crm.smscsim.ISession;
import com.crm.smscsim.util.SMSCUser;
import com.crm.thread.DispatcherInstance;
import com.logica.smpp.Data;
import com.logica.smpp.ServerPDUEvent;
import com.logica.smpp.ServerPDUEventListener;
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

public class SMSCInstance extends DispatcherInstance implements ServerPDUEventListener
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
	protected boolean			connected					= false;
	private int					messageId					= 1;
	private String				systemId					= "";
	private int					bindType					= Data.BIND_TRANSCEIVER;
	private String				queueSelector				= "";

	private static final String	DELIVERY_RCPT_DATE_FORMAT	= "yyMMddHHmm";

	private SimpleDateFormat	dateFormatter				= new SimpleDateFormat(DELIVERY_RCPT_DATE_FORMAT);

	private ISession			session						= null;
	private SMSCUser			user						= null;
	protected long				receiveTimeout				= 0;
	protected long				lastTimeReceived			= System.currentTimeMillis();

	public String getSystemId()
	{
		return systemId;
	}

	public SMSCUser getUser()
	{
		return user;
	}

	public int getBindType()
	{
		return bindType;
	}
	
	public void setReceiveTimeout(long receiveTimeout)
	{
		this.receiveTimeout = receiveTimeout;
	}

	public void setUser(SMSCUser user)
	{
		this.user = user;
		systemId = user.getUserId();
		queueSelector = "SystemID = '" + systemId + "'";
		setReceiveTimeout(user.getTimeout());
		try
		{
			initQueue();
		}
		catch (Exception e)
		{
			debugMonitor(e);
		}
	}

	public int getProcessorId()
	{
		return processorId;
	}

	protected String getName()
	{
		return "[" + systemId + "][#" + processorId + "]";
	}

	public void setSession(ISession session, int processorId)
	{
		connected = true;
		this.session = session;
		this.processorId = processorId;
	}

	public SMSCInstance() throws Exception
	{
		super();
	}

	@Override
	public void handleEvent(ServerPDUEvent event)
	{
		setLastTimeReceived();
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

	private boolean isReceiveTimeout()
	{
		if (lastTimeReceived + receiveTimeout <= System.currentTimeMillis())
		{
			return true;
		}
		return false;
	}

	public boolean validate()
	{
		if (isReceiveTimeout())
			return false;
		return true;
	}

	private void setLastTimeReceived()
	{
		lastTimeReceived = System.currentTimeMillis();
	}

	private void clientRequest(Request request) throws Exception
	{
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

					close();
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
				close();
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
					close();
				}
			}
			else
			{
				// can't respond => nothing to do :-)
			}
		}
	}

	private void clientResponse(Response response)
	{
		debugMonitor("Receive response from " + getName() + ": " + response.debugString());
	}

	private void serverResponse(Response response) throws Exception
	{
		if (connected)
		{
			debugMonitor("Send response to " + getName() + ": " + response.debugString());
			session.send((PDU) response);
		}
	}

	public void serverRequest(Request request) throws Exception
	{
		if (connected)
		{
			debugMonitor("Send request to " + getName() + ": " + request.debugString());
			session.send((PDU) request);
		}
	}

	private SubmitSMResp processSubmitSM(SubmitSM request)
	{
		return (SubmitSMResp) request.getResponse();
	}

	private int authenticate(BindRequest request)
	{
		systemId = request.getSystemId();
		// return getFactory().authenticate(this, request);
		return 0;
	}

	private String formatDate(long ms)
	{
		return dateFormatter.format(new Date(ms));
	}

	private String assignMessageId()
	{
		messageId++;
		return "SMSC" + messageId;
	}

	public void disconnect()
	{
		if (!connected)
			return;
		debugMonitor("Client " + getName() + " disconnected.");
		close();
	}

	public void close()
	{
		try
		{
			if (!connected)
				return;
			debugMonitor("Session " + getName() + " end.");
			if (session != null)
				session.endSession();
		}
		catch (Exception e)
		{
			debugMonitor(e);
		}
		finally
		{
			((SMSCThread) dispatcher).decreaseUserConnectionCount(systemId);
			bound = false;
			connected = false;
			session = null;
			user = null;
			systemId = "";
		}
	}

	@Override
	public Message detachMessage() throws Exception
	{
		if (!connected)
			return null;

		if (!validate())
		{
			disconnect();
			return null;
		}

		if (getBindType() == Data.BIND_TRANSMITTER && getUser() == null)
		{
			return null;
		}

		return super.detachMessage();
	}

	@Override
	public int processMessage(Message message) throws Exception
	{
		CommandMessage request = (CommandMessage) QueueFactory.getContentMessage(message);

		DeliverSM deliverSM = createDeliverSM(request);

		if (deliverSM != null)
		{
			serverRequest(deliverSM);
		}

		return Constants.BIND_ACTION_NONE;
	}

	protected DeliverSM createDeliverSM(CommandMessage request)
	{
		DeliverSM deliverSM = null;

		try
		{
			deliverSM = new DeliverSM();
			deliverSM.setSourceAddr(request.getIsdn());
			deliverSM.setShortMessage(request.getKeyword());
			deliverSM.setDestAddr(request.getServiceAddress());
			debugMonitor("Prepare to send from [" + deliverSM.getSourceAddr().getAddress()
					+ "] to [" + request.getUserName()
					+ "] content [" + deliverSM.getDestAddr().getAddress() + ": "
					+ deliverSM.getShortMessage() + "]");
		}
		catch (WrongLengthOfStringException e)
		{
			debugMonitor(e);
		}
		finally
		{
		}

		return deliverSM;
	}

	public void initQueue() throws Exception
	{
//		try
//		{
//			if (queueConnection != null)
//				queueConnection.close();
//			// create a queue connection
//			queueConnection = QueueFactory.createQueueConnection();
//
//			// create a queue session
//			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//
//			if (!dispatcher.queueName.equals(""))
//			{
//				if (dispatcher.temporaryQueue)
//				{
//					queueWorking = QueueFactory.createQueue(queueSession, dispatcher.queueName);
//				}
//				else
//				{
//					queueWorking = QueueFactory.getQueue(dispatcher.queueName);
//				}
//			}
//
//			if ((queueWorking != null) && !dispatcher.temporaryQueue && (dispatcher.queueMode == Constants.QUEUE_MODE_CONSUMER))
//			{
//				queueConsumer = queueSession.createConsumer(queueWorking, queueSelector);
//			}
//		}
//		catch (NamingException e)
//		{
//			QueueFactory.resetContext();
//
//			throw e;
//		}
//		catch (Exception e)
//		{
//			throw e;
//		}
	}
}
