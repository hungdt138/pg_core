package com.crm.provisioning.impl.smpp.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import com.logica.smpp.Data;
import com.logica.smpp.util.ByteBuffer;

/**
 * 
 * @author Nam
 * 
 */
public class ServiceIndication
{
	/**
	 * Well known DTD token
	 */
	public static final byte				DOCUMENT_DTD_ServiceIndication				= 0x05;							// ServiceIndication
																															// 1.0
																															// Public
																															// Identifier

	/**
	 * Tag Tokens
	 */
	public static final byte				TAGTOKEN_si									= 0x5;
	public static final byte				TAGTOKEN_indication							= 0x6;
	public static final byte				TAGTOKEN_info								= 0x7;
	public static final byte				TAGTOKEN_item								= 0x8;

	// Attribute Tokens
	public static final byte				ATTRIBUTESTARTTOKEN_action_signal_none		= 0x5;
	public static final byte				ATTRIBUTESTARTTOKEN_action_signal_low		= 0x6;
	public static final byte				ATTRIBUTESTARTTOKEN_action_signal_medium	= 0x7;
	public static final byte				ATTRIBUTESTARTTOKEN_action_signal_high		= 0x8;
	public static final byte				ATTRIBUTESTARTTOKEN_action_signal_delete	= 0x9;
	public static final byte				ATTRIBUTESTARTTOKEN_created					= 0xA;
	public static final byte				ATTRIBUTESTARTTOKEN_href					= 0xB;
	public static final byte				ATTRIBUTESTARTTOKEN_href_http				= 0xC;								// http://
	public static final byte				ATTRIBUTESTARTTOKEN_href_http_www			= 0xD;								// http://www.
	public static final byte				ATTRIBUTESTARTTOKEN_href_https				= 0xE;								// https://
	public static final byte				ATTRIBUTESTARTTOKEN_href_https_www			= 0xF;								// https://www.
	public static final byte				ATTRIBUTESTARTTOKEN_si_expires				= 0x10;
	public static final byte				ATTRIBUTESTARTTOKEN_si_id					= 0x11;
	public static final byte				ATTRIBUTESTARTTOKEN_class					= 0x12;

	/**
	 * Attribute Value Tokens
	 */
	public static final byte				ATTRIBUTEVALUETOKEN_com						= (byte) 0x85;						// .com/
	public static final byte				ATTRIBUTEVALUETOKEN_edu						= (byte) 0x86;						// .edu/
	public static final byte				ATTRIBUTEVALUETOKEN_net						= (byte) 0x87;						// .net/
	public static final byte				ATTRIBUTEVALUETOKEN_org						= (byte) 0x88;						// .org/

	private static Hashtable<String, Byte>	hrefStartTokens;
	private static Hashtable<String, Byte>	attributeValueTokens;

	public String							href;
	public String							text;
	public Date								createdAt;
	public Date								expiresAt;
	public int								action										= ServiceIndicationAction.NotSet;

	String									encoding									= Data.ENC_ASCII;

	public ServiceIndication()
	{
		hrefStartTokens = new Hashtable<String, Byte>();
		hrefStartTokens.put("https://www.", ATTRIBUTESTARTTOKEN_href_https_www);
		hrefStartTokens.put("http://www.", ATTRIBUTESTARTTOKEN_href_http_www);
		hrefStartTokens.put("https://", ATTRIBUTESTARTTOKEN_href_https);
		hrefStartTokens.put("http://", ATTRIBUTESTARTTOKEN_href_http);

		attributeValueTokens = new Hashtable<String, Byte>();
		attributeValueTokens.put(".com/", ATTRIBUTEVALUETOKEN_com);
		attributeValueTokens.put(".edu/", ATTRIBUTEVALUETOKEN_edu);
		attributeValueTokens.put(".net/", ATTRIBUTEVALUETOKEN_net);
		attributeValueTokens.put(".org/", ATTRIBUTEVALUETOKEN_org);
	}

	public ServiceIndication(String href, String text, int action)
	{
		this();
		this.href = href;
		this.text = text;
		this.action = action;
	}

	public ServiceIndication(String href, String text, String encoding, int action)
	{
		this(href, text, action);
		this.encoding = encoding;
	}

	public ServiceIndication(String href, String text, Date createdAt, Date expiresAt)
	{
		this(href, text, ServiceIndicationAction.NotSet);
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}

	public ServiceIndication(String href, String text, String encoding, Date createdAt, Date expiresAt)
	{
		this(href, text, createdAt, expiresAt);
		this.encoding = encoding;
	}

	public ServiceIndication(String href, String text, Date createdAt, Date expiresAt, int action)
	{
		this(href, text, action);
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}

	public ServiceIndication(String href, String text, String encoding, Date createdAt, Date expiresAt, int action)
	{
		this(href, text, createdAt, expiresAt, action);
		this.encoding = encoding;
	}

	/**
	 * Generates a byte array comprising the encoded Service Indication
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public byte[] getWBXMLBytes() throws UnsupportedEncodingException
	{
		ByteBuffer buffer = new ByteBuffer();

		// wbxml headers
		buffer.appendByte(WBXML.VERSION_1_1);
		buffer.appendByte(DOCUMENT_DTD_ServiceIndication);
		buffer.appendByte(WBXML.CHARSET_UTF_8);
		buffer.appendByte(WBXML.NULL);

		// start xml doc
		buffer.appendByte(WBXML.SetTagTokenIndications(TAGTOKEN_si, false, true));
		buffer.appendByte(WBXML.SetTagTokenIndications(TAGTOKEN_indication, true, true));

		// href attribute
		// this attribute has some well known start tokens that
		// are contained within a static hashtable. Iterate through
		// the table and chose the token.
		int i = 0;
		byte hrefTagToken = ATTRIBUTESTARTTOKEN_href;
		Iterator<String> iterator = hrefStartTokens.keySet().iterator();

		while (iterator.hasNext())
		{
			String startString = iterator.next();
			if (this.href.startsWith(startString))
			{
				hrefTagToken = (byte) hrefStartTokens.get(startString);
				i = startString.length();
				break;
			}
		}

		buffer.appendByte(hrefTagToken);

		writeInlineString(buffer, this.href.substring(i));

		// action attibute
		if (this.action != ServiceIndicationAction.NotSet)
			buffer.appendByte(getActionToken(this.action));

		// close indication element attributes
		buffer.appendByte(WBXML.TAGTOKEN_END);

		// text of indication element
		writeInlineString(buffer, this.text);

		// close indication element
		buffer.appendByte(WBXML.TAGTOKEN_END);
		// close si element
		buffer.appendByte(WBXML.TAGTOKEN_END);

		return buffer.getBuffer();
	}

	/**
	 * Gets the token for the action attribute
	 * 
	 * @param action
	 * @return
	 */
	protected byte getActionToken(int action)
	{
		switch (action)
		{
		case ServiceIndicationAction.Delete:
			return ATTRIBUTESTARTTOKEN_action_signal_delete;
		case ServiceIndicationAction.Signal_high:
			return ATTRIBUTESTARTTOKEN_action_signal_high;
		case ServiceIndicationAction.Signal_low:
			return ATTRIBUTESTARTTOKEN_action_signal_low;
		case ServiceIndicationAction.Signal_medium:
			return ATTRIBUTESTARTTOKEN_action_signal_medium;
		default:
			return ATTRIBUTESTARTTOKEN_action_signal_none;
		}
	}

	/**
	 * Encodes an inline string into the buffer
	 * 
	 * @param buffer
	 * @param text
	 * @throws UnsupportedEncodingException
	 */
	public void writeInlineString(ByteBuffer buffer, String text) throws UnsupportedEncodingException
	{
		// indicate that the follow bytes comprise a string
		buffer.appendByte(WBXML.TOKEN_INLINE_STRING_FOLLOWS);

		// write character bytes
		byte[] bytes = text.getBytes(encoding);
		buffer.appendBytes(bytes);

		// end is indicated by a null byte
		buffer.appendByte(WBXML.NULL);
	}

	/**
	 * Encodes the Date to the stream. Dates are encoded as Opaque Data with
	 * each number in the string represented by its 4-bit binary value eg:
	 * 1999-04-30 06:40:00 is encoded as 199904300640. Trailing zero values are
	 * not included.
	 * 
	 * @param buffer
	 * @param date
	 */
	protected void writeDate(ByteBuffer buffer, Date date)
	{
		byte[] bytes = new byte[7];
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		bytes[0] = (byte) (cal.get(Calendar.YEAR) / 100);
		bytes[1] = (byte) (cal.get(Calendar.YEAR) % 100);
		bytes[2] = (byte) cal.get(Calendar.MONTH);
		bytes[3] = (byte) cal.get(Calendar.DAY_OF_MONTH);

		int dateLength = 4;

		if (cal.get(Calendar.HOUR_OF_DAY) > 0)
		{
			bytes[4] = (byte) cal.get(Calendar.HOUR_OF_DAY);
			dateLength = 5;
		}

		if (cal.get(Calendar.MINUTE) > 0)
		{
			bytes[5] = (byte) cal.get(Calendar.MINUTE);
			dateLength = 6;
		}

		if (cal.get(Calendar.SECOND) > 0)
		{
			bytes[6] = (byte) cal.get(Calendar.SECOND);
			dateLength = 7;
		}

		// write to buffer
		buffer.appendByte(WBXML.TOKEN_OPAQUEDATA_FOLLOWS);
		buffer.appendByte((byte) dateLength);
		buffer.appendBytes(bytes, dateLength);
	}
}
