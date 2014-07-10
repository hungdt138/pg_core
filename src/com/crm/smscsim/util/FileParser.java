package com.crm.smscsim.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class FileParser
{
	/*
	 * End of line
	 */
	static final char					CR					= '\r';
	static final char					LF					= '\n';
	static final String					LINE_END			= "\r\n";

	/**
	 * The characters which can be used as delimiters between name of the
	 * attribute and it's value.
	 */
	static final String					ATTR_DELIMS			= "=:";

	static final String					COMMENT_CHARS		= "#;";

	static final String					USER_FIELD			= "userid";
	static final String					PWD_FIELD			= "password";
	static final String					CONNECTION_FIELD	= "connection";
	static final String					BIND_FIELD			= "bindmode";
	static final String					TIMEOUT_FIELD		= "timeout";
	static final String					TRANCEIVER			= "tr";
	static final String					TRANSMITTER			= "t";
	static final String					RECEIVER			= "r";

	InputStreamReader					in;
	OutputStreamWriter					out;
	char								c, pending;
	boolean								pendingChar			= false;
	String								line				= "";
	SMSCUser							user				= null;
	boolean								pendingRecord		= false;

	ConcurrentHashMap<String, SMSCUser>	users				= null;
	
	public FileParser(ConcurrentHashMap<String, SMSCUser>	users)
	{
		this.users = users;
	}

	/**
	 * Parses the input stream and fills the table with data from the stream.
	 * 
	 * @param is
	 *            the input stream to read the data from
	 * @see #compose(OutputStream)
	 */
	public void parse(InputStream is) throws IOException
	{
		in = new InputStreamReader(is);
		prepareRecord();
		while (!eof())
		{
			getLine();
			if (isEmpty())
			{
				// got empty line
				finaliseRecord(true);
			}
			else if (!isComment())
			{
				parseAttribute(line);
			}
			else
			{
				// got comment line
			}
		}
		finaliseRecord(false);
	}

	/**
	 * Writes to the output stream formatted content of the table.
	 * 
	 * @param os
	 *            the output stream to write to
	 * @see #parse(InputStream)
	 */
	public void compose(OutputStream os)
			throws IOException
	{
		out = new OutputStreamWriter(os);
		SMSCUser user;
		synchronized (users)
		{
			Iterator<String> itr = users.keySet().iterator();

			while (itr.hasNext())
			{
				user = users.get(itr.next());
				synchronized (user)
				{
					line = USER_FIELD + "=" + user.getUserId() + LINE_END;
					line += PWD_FIELD + "=" + user.getPassword() + LINE_END;
					line += CONNECTION_FIELD + "=" + user.getConnectionLimit() + LINE_END;
					if (user.isTranceiverEnabled())
					{
						line += BIND_FIELD + TRANCEIVER + LINE_END;
					}
					else if (user.isTransmitterEnabled())
					{
						line += BIND_FIELD + TRANSMITTER + LINE_END;
					}
					else if (user.isReceiverEnabled())
					{
						line += BIND_FIELD + RECEIVER + LINE_END;
					}

					line = TIMEOUT_FIELD + "=" + user.getTimeout() + LINE_END;
					out.write(line);
				}

				if (itr.hasNext())
				{
					// if this wasn't last record, write empty line
					// as delimiter between users
					out.write(LINE_END);
				}
			}
		}
		out.flush();
	}

	/**
	 * Called whenever end of record was reached. If there is a record which
	 * hasn't been inserted to the table yet, it's inserted by this method.
	 */
	void finaliseRecord(boolean prepareNext)
	{
		if (pendingRecord)
		{
			users.put(user.getUserId().toUpperCase(), user);
			pendingRecord = false;
			if (prepareNext)
			{
				prepareRecord();
			}
		}
	}

	/**
	 * Creates new record to add the newly read attributes to.
	 */
	void prepareRecord()
	{
		user = new SMSCUser();
	}

	/**
	 * Parses attribute and inserts it into the record.
	 */
	void parseAttribute(String attr)
	{
		int attrLen = attr.length();
		int currPos = 0;
		while ((currPos < attrLen) &&
				(ATTR_DELIMS.indexOf(attr.charAt(currPos)) == -1))
		{
			currPos++;
		}
		String name = attr.substring(0, currPos);
		String value = attr.substring(currPos + 1, attrLen);
		setUser(name, value);
		pendingRecord = true;
	}

	/**
	 * Returns if end of the stream was already reached.
	 */
	boolean eof() throws IOException
	{
		return !in.ready();
	}

	/**
	 * Returns if on the current position in the stream there is end of line
	 * character.
	 */
	boolean eol()
	{
		return (c == CR) || (c == LF);
	}

	/**
	 * Returns if the current line is empty, i.e. doesn't contain any character
	 * including whitespace.
	 */
	boolean isEmpty()
	{
		return line.length() == 0;
	}

	/**
	 * Returns if the current line contains a comment text.
	 */
	boolean isComment()
	{
		return isEmpty() ?
				false :
				COMMENT_CHARS.indexOf(line.charAt(0)) != -1;
	}

	/**
	 * Reads one line from the input stream and stores it into <code>line</code>
	 * variable.
	 * 
	 * @see #get()
	 * @see #unget()
	 */
	void getLine() throws IOException
	{
		line = "";
		get();
		while(true)
		{
			if (!eol())
			{
				line += c;
			}
			get();
			if (eol())
				break;
			if (eof())
			{
				line += c;
				break;
			}
		}
		
		if (!eof())
		{
			// then it must have been eol => we are trying
			// to skip another potential line delim
			if (c == CR)
			{
				// then we could have CRLF
				get();
				if (c != LF)
				{
					// no CRLF
					unget();
				}
			}
			else
			{
				// nothing as LF is ok
			}
		}
	}

	/**
	 * Reads one character from input stream or gets a pending character read
	 * before.
	 * 
	 * @see #getLine()
	 * @see #unget()
	 */
	void get() throws IOException
	{
		if (pendingChar)
		{
			c = pending;
			pendingChar = false;
		}
		else
		{
			c = (char) in.read();
		}
	}

	/**
	 * If necessary, one and only one character can be 'unget' by this method.
	 * This character becomes pending character and will be get by next call to
	 * method <code>get</code>.
	 * 
	 * @see #get()
	 * @see #getLine()
	 */
	void unget()
	{
		pending = c;
		pendingChar = true;
	}

	/**
	 * Set User attribute
	 * @param name
	 * @param value
	 * 
	 * @see SMSCUser#setUserId(String)
	 * @see SMSCUser#setPassword(String)
	 * @see SMSCUser#setConnectionLimit(int)
	 * @see SMSCUser#setTimeout(long)
	 */
	void setUser(String name, String value)
	{
		if (name.equals(USER_FIELD))
			user.setUserId(value);
		else if (name.equals(PWD_FIELD))
			user.setPassword(value);
		else if (name.equals(CONNECTION_FIELD))
			user.setConnectionLimit(Integer.parseInt(value));
		else if (name.equals(BIND_FIELD))
		{
			if (value.toUpperCase().contains("T"))
				user.enableTransmitter();
			else
				user.disableTransmitter();

			if (value.toUpperCase().contains("R"))
				user.enableReceiver();
			else
				user.disableReceiver();
		}
		else if (name.equals(TIMEOUT_FIELD))
			user.setTimeout(Long.parseLong(value));
	}
}
