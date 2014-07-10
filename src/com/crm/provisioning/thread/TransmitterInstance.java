package com.crm.provisioning.thread;

import javax.jms.Message;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.provisioning.impl.smpp.SMPPConnection;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.ResponseUtil;

public class TransmitterInstance extends CommandInstance
{
	public TransmitterInstance() throws Exception
	{
		super(); 
	}

	public int processMessage(Message message) throws Exception
	{
		boolean checkCommand = false;

		CommandMessage sms = (CommandMessage) QueueFactory.getContentMessage(message);
		
		String strCmdCheck = sms.getRequestValue(ResponseUtil.SMS_CMD_CHECK, "true");
		
		if (strCmdCheck.toUpperCase().equals("TRUE"))
		{
			checkCommand = true;
		}

		if (checkCommand)
		{
			return super.processMessage(message);
		}
		else
		{
			SMPPConnection smppConnection = null;
			try
			{
				// debugMonitor(message.toString());

				smppConnection = (SMPPConnection) getProvisioningConnection();

				try
				{
					sms.setProvisioningType("SMSC");

					smppConnection.submit(sms);
					// debugMonitor(sms);
				}
				catch (Exception e)
				{
					sms.setStatus(Constants.ORDER_STATUS_DENIED);

					throw e;
				}
				finally
				{
					closeProvisioningConnection(smppConnection);
				}
			}
			catch (Exception e)
			{
				throw e;
			}

			return Constants.BIND_ACTION_SUCCESS;
		}
	}
}
