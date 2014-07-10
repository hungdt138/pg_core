package com.crm.smscsim;

import com.logica.smpp.Connection;
import com.logica.smpp.Receiver;
import com.logica.smpp.Transmitter;

public class SMSCReceiver extends Receiver
{

	public SMSCReceiver(Connection connection)
	{
		super(connection);
	}
	
	public SMSCReceiver(Transmitter transmitter, Connection connection)
	{
		super(transmitter, connection);
	}
	
	@Override
	public void stop()
	{
		debug.write(DRXTX,"Receiver stoping");
        if (isReceiver()) {
            stopProcessing(null);
        }
        debug.write(DRXTX,"Receiver stoped");
	}

}
