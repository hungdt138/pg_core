package com.crm.provisioning.impl.ema;

import java.io.IOException;
import org.apache.commons.net.telnet.*;

public class TelnetWrapper extends TelnetClient
{
	private int	miInterval	= 100;

	/**
	 * 
	 * @param iInterval
	 *            int
	 * @throws IOException
	 */
	public void setInterval(int iInterval) throws IOException
	{
		miInterval = iInterval;
	}

	/**
	 * Returns bytes available to be read. Since they haven't been negotiated
	 * over, this could be misleading...
	 * 
	 * @return int
	 * @throws IOException
	 */
	public int available() throws IOException
	{
		return getInputStream().available();
	}

	/**
	 * Returns a String from the telnet connection. Blocks until one is
	 * available. No guarantees that the string is in any way complete. NOTE:
	 * uses Java 1.0.2 style String-bytes conversion.
	 * 
	 * @return String
	 * @throws IOException
	 */
	public String receive() throws IOException
	{
		byte[] bt = new byte[available()];
		int iByteRead = getInputStream().read(bt);
		return new String(bt, 0, iByteRead);
	}

	/**
	 * Returns all data received up until a certain token.
	 * 
	 * @param token
	 *            String to wait for
	 * @exception IOException
	 *                on problems with the socket connection
	 * @see #wait
	 * @return String
	 * @throws IOException
	 */
	public String receiveUntil(String token) throws IOException
	{
		return new String(StreamUtil.getDataTerminatedBySymbol(getInputStream(), token.getBytes()));
	}

	/**
	 * Returns all data received up until a certain token.
	 * 
	 * @param token
	 *            String to wait for
	 * @param timeout
	 *            time in milliseconds to wait (negative means wait forever)
	 * @exception IOException
	 *                on problems with the socket connection
	 * @exception TimedOutException
	 *                if time runs out before token received
	 * @see #wait(String, long)
	 * @return String
	 */
	/**
	 * DEPQ Dung ham nay bi loi trong truong hop nhu sau Neu Server khong tra ve
	 * token ma lien tuc tra ve du lieu thi vong while(available() <= 0){} khong
	 * bao gio vao trong duoc(da tung xay ra tai VNM) khi do vong
	 * while(buf.indexOf(token) == -1){} lien tuc chay cho den khi bat duoc
	 * token mac du da qua thoi gian timeout --> Co the dan den vong nay chay
	 * mai khong co dieu kien thoat khi Server lien tuc tra ve du lieu nhung
	 * khong co Token Khac phuc: Dung ham receiveUntilEx(token,timeout)
	 */
	public String receiveUntil(String token, long timeout) throws IOException
	{
		StringBuffer buf = new StringBuffer();
		long deadline = 0;
		if (timeout >= 0)
		{
			deadline = System.currentTimeMillis() + timeout;
		}
		do
		{
			if (timeout >= 0)
			{
				while (available() <= 0)
				{
					if (System.currentTimeMillis() > deadline)
					{
						throw new IOException("Wait '" + token + "' for " + timeout + " miliseconds was timeout\r\n" + buf.toString());
					}
					try
					{
						Thread.sleep(miInterval);
					}
					catch (InterruptedException ignored)
					{
					}
				}
			}
			buf.append(receive());
		}
		while (buf.indexOf(token) == -1);
		return buf.toString();
	}

	/**
	 * depq 2009: Nhan tra ve khi gui lenh telnet + Doi trong thoi gian timeout
	 * cho den khi tra ve token + Neu qua thoi gian timeout ma van khong tra ve
	 * token thi throw exception
	 * 
	 * @param token
	 *            String
	 * @param timeout
	 *            long
	 * @return String
	 * @throws IOException
	 */
	public String receiveUntilEx(String token, long timeout) throws IOException
	{
		long deadline = 0;
		if (timeout >= 0)
		{
			deadline = System.currentTimeMillis() + timeout;
		}
		else
		{
			throw new IOException("Must set timeout > 0");
		}
		// chua het thoi gian timeout ma van con du lieu trong inputstream thi
		// tiep tu lay ve
		String total = "";
		while (System.currentTimeMillis() < deadline)
		{
			int j = available();
			// tiep tuc doi neu khong co du lieu va chua het gio timeout
			while (j <= 0 && System.currentTimeMillis() < deadline)
			{
				try
				{
					Thread.sleep(miInterval);
				}
				catch (InterruptedException ex)
				{
				}
				j = available();
			}
			// lay du lieu neu stream ton tai du lieu va ngung lay khi het
			// timeout hoac tim thay token
			while (j > 0 && System.currentTimeMillis() < deadline)
			{
				total += receive(j);
				if (total.contains(token))
				{
					break;
				}
				j = available();
			}
			if (total.contains(token))
			{
				break;
			}
			if (j <= 0)
			{
				continue;
			}
		}
		if (!total.contains(token))
		{
			throw new IOException("Wait for " + timeout + " miliseconds was timeout\r\n" + total);
		}
		return total;
	}

	/**
	 * Sends a String to the remote host. NOTE: uses Java 1.0.2 style
	 * String-bytes conversion.
	 * 
	 * @exception IOException
	 *                on problems with the socket connection
	 * @param s
	 *            String
	 * @param bAutoFlush
	 *            boolean
	 * @throws IOException
	 */
	public void send(String s, boolean bAutoFlush) throws IOException
	{
		byte[] buf = s.getBytes();
		send(buf, bAutoFlush);
	}

	public void send(String s) throws IOException
	{
		send(s, true);
	}

	/**
	 * Sends bytes over the telnet connection.
	 * 
	 * @param buf
	 *            byte[]
	 * @param bAutoFlush
	 *            boolean
	 * @throws IOException
	 */
	public void send(byte[] buf, boolean bAutoFlush) throws IOException
	{
		getOutputStream().write(buf);
		if (bAutoFlush)
		{
			getOutputStream().flush();
		}
	}

	/**
	 * Connects to the default telnet port on the given host. If the
	 * defaultLogin and defaultPassword are non-null, attempts login.
	 * 
	 * @param host
	 *            String
	 * @throws IOException
	 */
	public TelnetWrapper(String host) throws IOException
	{
		connect(host);
		setSoLinger(true, 0);
	}

	/**
	 * Connects to a specific telnet port on the given host. If the defaultLogin
	 * and defaultPassword are non-null, attempts login.
	 * 
	 * @param host
	 *            String
	 * @param port
	 *            int
	 * @throws IOException
	 */
	public TelnetWrapper(String host, int port) throws IOException
	{
		connect(host, port);
		setSoLinger(true, 0);
	}

	/**
	 * Ends the telnet connection.
	 * 
	 * @throws Throwable
	 */
	public void finalize() throws Throwable
	{
		try
		{
			disconnect();
		}
		catch (IOException e)
		{
		}
		super.finalize();
	}

	public boolean isOpen()
	{
		return isConnected();
	}

	/**
	 * depq 2009 receive(int count)
	 * 
	 * @return String
	 * @throws IOException
	 */
	public String receive(int count) throws IOException
	{
		byte[] bt = new byte[count];
		int iByteRead = getInputStream().read(bt);
		return new String(bt, 0, iByteRead);
	}

	/**
	 * depq 2009 receiveAll
	 * 
	 * @return String
	 * @throws IOException
	 */
	public String receiveAll() throws IOException
	{
		String total = null;
		int j = available();
		while (j > 0)
		{
			if (total == null)
			{
				total = receive(j);
			}
			else
			{
				total += receive(j);
			}
			j = available();
		}
		if (total == null)
		{
			throw new IOException("Not yet receiver data  from Input stream\r\n");
		}
		return total;
	}

	/**
	 * depq 2009: neu input stream available lien tuc thi doc lien tuc trong
	 * thoi gian timeout neu khong available thi doi de het thoi gian time out
	 * 
	 * @param timeout
	 *            long
	 * @return String
	 * @throws IOException
	 */
	public String receiveAllUntil(long timeout) throws IOException
	{
		long deadline = 0;
		if (timeout >= 0)
		{
			deadline = System.currentTimeMillis() + timeout;
		}
		else
		{
			throw new IOException("Must set timeout > 0");
		}
		// chua het thoi gian timeout ma van con du lieu trong inputstream thi
		// tiep tu lay ve
		String total = "";
		while (System.currentTimeMillis() < deadline)
		{
			int j = available();
			while (j <= 0 && System.currentTimeMillis() < deadline)
			{
				try
				{
					Thread.sleep(miInterval);
				}
				catch (InterruptedException ex)
				{
				}
				j = available();
			}
			while (j > 0)
			{
				total += receive(j);
				j = available();
			}
			if (j <= 0)
			{
				break;
			}
		}
		if (total.equals(""))
		{
			throw new IOException("Wait for " + timeout + " miliseconds was timeout\r\n");
		}
		return total;
	}

	/**
	 * depq 2009: Nhan tra ve khi gui lenh telnet + Doi trong thoi gian timeout
	 * cho den khi tra ve token + Neu qua thoi gian timeout ma van khong tra ve
	 * token thi: - Kiem tra khong co du lieu tra ve thi throw exception - Co du
	 * lieu tra ve thi tra ra du lieu tra ve do
	 * 
	 * @param token
	 *            String
	 * @param timeout
	 *            long
	 * @return String
	 * @throws IOException
	 */
	public String receiveAllUntil(String token, long timeout) throws IOException
	{
		long deadline = 0;
		if (timeout >= 0)
		{
			deadline = System.currentTimeMillis() + timeout;
		}
		else
		{
			throw new IOException("Must set timeout > 0");
		}
		// chua het thoi gian timeout ma van con du lieu trong inputstream thi
		// tiep tu lay ve
		String total = "";
		while (System.currentTimeMillis() < deadline)
		{
			int j = available();
			while (j <= 0 && System.currentTimeMillis() < deadline)
			{
				try
				{
					Thread.sleep(miInterval);
				}
				catch (InterruptedException ex)
				{
				}
				j = available();
			}
			while (j > 0)
			{
				total += receive(j);
				if (total.contains(token))
				{
					break;
				}
				j = available();
			}
			if (total.contains(token))
			{
				break;
			}
			if (j <= 0)
			{
				continue;
			}
		}
		if (total.equals(""))
		{
			throw new IOException("Wait for " + timeout + " miliseconds was timeout\r\n");
		}
		return total;
	}

	/**
	 * depq : giong ham receiveAllUntil(String token,long timeout) nhung cho
	 * phep truyen vao mot chuoi token
	 * 
	 * @param token
	 *            String
	 * @param timeout
	 *            long
	 * @return String
	 * @throws IOException
	 */
	public String receiveAllUntil(String token[], long timeout) throws IOException
	{
		long deadline = 0;
		if (timeout >= 0)
		{
			deadline = System.currentTimeMillis() + timeout;
		}
		else
		{
			throw new IOException("Must set timeout > 0");
		}
		// chua het thoi gian timeout ma van con du lieu trong inputstream thi
		// tiep tu lay ve
		String total = "";
		while (System.currentTimeMillis() < deadline)
		{
			int j = available();
			while (j <= 0 && System.currentTimeMillis() < deadline)
			{
				try
				{
					Thread.sleep(miInterval);
				}
				catch (InterruptedException ex)
				{
				}
				j = available();
			}
			while (j > 0)
			{
				total += receive(j);
				if (checkToken(total, token))
				{
					break;
				}
				j = available();
			}
			if (checkToken(total, token))
			{
				break;
			}
			if (j <= 0)
			{
				continue;
			}
		}
		if (total.equals(""))
		{
			throw new IOException("Wait for " + timeout + " miliseconds was timeout\r\n");
		}
		return total;
	}

	/**
	 * depq
	 * 
	 * @param strCheck
	 *            String
	 * @param token
	 *            String[]
	 * @return boolean
	 */
	public boolean checkToken(String strCheck, String token[])
	{
		for (int i = 0; i < token.length; i++)
		{
			if (strCheck.contains(token[i]))
			{
				return true;
			}
		}
		return false;
	}
}
