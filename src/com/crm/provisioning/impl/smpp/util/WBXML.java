package com.crm.provisioning.impl.smpp.util;

/**
 * 
 * @author Nam
 *
 */
public class WBXML
{
	public static final byte	NULL						= 0x00;

	public static final byte	VERSION_1_1					= 0x01;
	public static final byte	VERSION_1_2					= 0x02;

	public static final byte	CHARSET_UTF_8				= 0x6A;

	public static final byte	TAGTOKEN_END				= 0x01;
	public static final byte	TOKEN_INLINE_STRING_FOLLOWS	= 0x03;
	public static final byte	TOKEN_OPAQUEDATA_FOLLOWS	= (byte)0xC3;

	public static byte SetTagTokenIndications(byte token, boolean hasAttributes, boolean hasContent)
	{
		if (hasAttributes)
			token |= 0xC0;
		if (hasContent)
			token |= 0x40;

		return token;
	}
}
