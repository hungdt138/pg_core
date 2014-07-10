package com.crm.provisioning.impl.smpp.util;

/**
 * Well known values used when generating WSP (Wireless Session Protocol)
 * headers
 * 
 * @author Nam
 */
public class WSP
{
	public static final byte	TRANSACTIONID_CONNECTIONLESSWSP						= 0x25;

	public static final byte	PDUTYPE_PUSH										= 0x06;

	public static final byte	HEADER_CONTENTLENGTH								= (byte) 0x8D;

	public static byte[]		HEADER_CONTENTTYPE_application_vnd_wap_sic_utf_8	= new byte[] { 0x03, (byte) 0xAE,
			(byte) 0x81, (byte) 0xEA												};

	public static final byte	HEADER_APPLICATIONTYPE								= (byte) 0xaf;
	public static final byte	HEADER_APPLICATIONTYPE_x_wap_application_id_w2		= (byte) 0x82;

	public static byte[]		HEADER_PUSHFLAG										= new byte[] { (byte) 0xB4, (byte) 0x84 };
}
