package com.crm.provisioning.impl.charging;

import com.crm.provisioning.cache.ProvisioningConnection;

public class SubModifyConnection extends ProvisioningConnection
{
	public static final String	SEPARATE_CHARS	= "EFEF";
	protected TCPClient			client			= null;
	protected SubModifyListener	listener		= null;

	public SubModifyConnection()
	{
	}

	@Override
	public boolean openConnection() throws Exception
	{
		client = new TCPClient(getHost(), getPort());
		client.setTimeout(getTimeout());
		listener = new SubModifyListener();
		listener.setDispatcher(getDispatcher());
		client.setListener(listener);
		client.connect();
		client.start();
		return super.openConnection();
	}

	@Override
	public boolean closeConnection() throws Exception
	{
		client.close();
		return super.closeConnection();
	}

	public String request(String seq, String req) throws Exception
	{
		byte[] core = req.getBytes();
		byte[] sufix = SEPARATE_CHARS.getBytes();
		byte[] data = new byte[core.length + sufix.length];

		System.arraycopy(core, 0, data, 0, core.length);
		System.arraycopy(sufix, 0, data, 0 + core.length, sufix.length);

		Object notifyObject = new Object();

		listener.setNotifiedObject(seq, notifyObject);
		synchronized (notifyObject)
		{
			client.send(data);
			notifyObject.wait(getTimeout());

			return (String) listener.getResponse(seq);
		}
	}

	public static void main(String[] param)
	{
		SubModifyConnection conn = new SubModifyConnection();
		conn.setHost("127.0.0.1");
		conn.setPort(8000);
		conn.setTimeout(50000);

		try
		{
			conn.openConnection();

			String response = conn.request("12345", "12345,this is request");

			System.out.println("response: " + response);

			conn.closeConnection();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
