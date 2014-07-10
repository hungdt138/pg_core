package com.crm.provisioning.thread;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.crm.alarm.cache.AlarmTemplate;
import com.crm.kernel.index.BinaryIndex;
import com.crm.kernel.index.IndexNode;
import com.crm.kernel.message.AlarmMessage;
import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.impl.smpp.TransmitterMessage;
import com.crm.provisioning.message.CommandMessage;
import com.crm.thread.util.ThreadUtil;
import com.crm.util.AppProperties;

import com.fss.util.*;
import com.logica.smpp.util.Queue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: FPT
 * </p>
 * 
 * @author DatPX
 * @version 1.0 Purpose : Compute file : R4
 */

public class SMPPThread extends ProvisioningThread
{
	/**
	 * How you want to bind to the SMSC: transmitter (t), receiver (r) or
	 * transciever (tr). Transciever can both send messages and receive
	 * messages. Note, that if you bind as receiver you can still receive
	 * responses to you requests (submissions).
	 */
	public String bindOption = "t";

	/**
	 * Indicates that the Session has to be asynchronous. Asynchronous Session
	 * means that when submitting a Request to the SMSC the Session does not
	 * wait for a response. Instead the Session is provided with an instance of
	 * implementation of ServerPDUListener from the smpp library which receives
	 * all PDUs received from the SMSC. It's application responsibility to match
	 * the received Response with sended Requests.
	 */
	public boolean asynchronous = false;

	/**
	 * The range of addresses the smpp session will serve.
	 */
	public byte addressTON = 1;
	public byte addressNPI = 1;
	public String addressRange = "9242";

	/**
	 * The range of addresses the smpp session will serve.
	 */
	public byte sourceTON = 1;
	public byte sourceNPI = 1;
	public String sourceAddress = "84983589789";

	/**
	 * The range of addresses the smpp session will serve.
	 */
	public byte destTON = 1;
	public byte destNPI = 1;
	public String destAddress = "9242";

	/*
	 * for information about these variables have a look in SMPP 3.4
	 * specification
	 */
	public String systemType = "SMPP";
	public String serviceType = "";

	public long nextEnquireLink = 0;
	public long enquireInterval = 10;
	public byte registeredDelivery = 0;

	public boolean useConcatenated = false;
	public int orderTimeout = 60000;

	// protected LinkedList<TransmitterMessage> transmitterQueue = new
	// LinkedList<TransmitterMessage>();
	protected Queue transmitterQueue = new Queue();
	private Object mutex = "mutex";

	// ////////////////////////////////////////////////////////////////////////
	// get directory parameters definition
	// Author: ThangPV
	// Modify DateTime: 19/02/2003
	// /////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDispatcherDefinition()
	{
		Vector vtReturn = new Vector();

		vtReturn.addAll(ThreadUtil.createSMPPParameter(this));
		vtReturn.add(ThreadUtil.createIntegerParameter("orderTimeout",
				"Time to live of order (s)."));
		vtReturn.addAll(ThreadUtil.createProvisioningParameter(this));
		vtReturn.addAll(ThreadUtil.createQueueParameter(this));
		vtReturn.addAll(ThreadUtil.createInstanceParameter(this));
		vtReturn.addAll(ThreadUtil.createLogParameter(this));

		return vtReturn;
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillSMPPParameter() throws AppException
	{
		asynchronous = ThreadUtil.getBoolean(this, "asynchronous", false);
		useConcatenated = ThreadUtil
				.getBoolean(this, "use-concatenated", false);
		bindOption = ThreadUtil.getString(this, "bindMode", true, "");

		addressRange = ThreadUtil.getString(this, "address-range", true, "");
		addressTON = Byte.parseByte(ThreadUtil.getString(this, "addr-ton",
				true, "1"));
		addressNPI = Byte.parseByte(ThreadUtil.getString(this, "addr-npi",
				true, "1"));

		sourceAddress = ThreadUtil.getString(this, "source-address", true, "");
		sourceTON = Byte.parseByte(ThreadUtil.getString(this, "source-ton",
				true, "1"));
		sourceNPI = Byte.parseByte(ThreadUtil.getString(this, "source-npi",
				true, "1"));

		destAddress = ThreadUtil.getString(this, "destination-address", true,
				"");
		destTON = Byte.parseByte(ThreadUtil.getString(this, "destination-ton",
				true, "1"));
		destNPI = Byte.parseByte(ThreadUtil.getString(this, "destination-npi",
				true, "1"));

		enquireInterval = ThreadUtil.getLong(this, "enquireInterval", 3000);

		registeredDelivery = Byte.parseByte(ThreadUtil.getString(this,
				"registeredDelivery", true, "0"));

		orderTimeout = ThreadUtil.getInt(this, "orderTimeout", 60000);
	}

	// //////////////////////////////////////////////////////
	// Override
	// //////////////////////////////////////////////////////
	public void fillDispatcherParameter() throws AppException
	{
		super.fillDispatcherParameter();

		fillSMPPParameter();
	}

	// //////////////////////////////////////////////////////
	// after process session
	// Author : ThangPV
	// Created Date : 16/09/2004
	// //////////////////////////////////////////////////////
	public void initProvisioningParameters() throws Exception
	{
		try
		{
			super.initProvisioningParameters();

			AppProperties parameters = new AppProperties();

			parameters.setBoolean("asynchronous", asynchronous);
			parameters.setBoolean("useConcatenated", useConcatenated);
			parameters.setProperty("bindOption", bindOption);

			parameters.setProperty("addressRange", addressRange);
			parameters.setByte("addressTON", addressTON);
			parameters.setByte("addressNPI", addressNPI);

			parameters.setProperty("sourceAddress", sourceAddress);
			parameters.setByte("sourceTON", sourceTON);
			parameters.setByte("sourceNPI", sourceNPI);

			parameters.setProperty("destAddress", destAddress);
			parameters.setByte("destTON", destTON);
			parameters.setByte("destNPI", destNPI);

			parameters.setLong("enquireInterval", enquireInterval);
			parameters.setByte("registeredDelivery", registeredDelivery);

			provisioningPool.setParameters(parameters);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	// public LinkedList<TransmitterMessage> getTransmitterQueue()
	// {
	// return transmitterQueue;
	// }
	//
	public void attachTransmitter(TransmitterMessage transmitterMessage)
	{
		transmitterQueue.enqueue(transmitterMessage);
	}

	public TransmitterMessage detachTransmitter(TransmitterMessage transmitter)
	{
		return (TransmitterMessage)transmitterQueue.dequeue(transmitter);
	}

	public void sendTransmitterLog(CommandMessage transmitter)
	{
		if (transmitter != null)
		{
			MQConnection queueConnection = null;
			try
			{
				queueConnection = getMQConnection();

				queueConnection.sendMessage(transmitter, 60000,
						QueueFactory.COMMAND_LOG_QUEUE, 60000, queuePersistent);
			}
			catch (Exception e)
			{
				logMonitor(e);
			}
			finally
			{
				returnMQConnection(queueConnection);
			}
		}
	}
}
