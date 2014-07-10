package com.crm.provisioning.impl.smpp.util;

import java.io.UnsupportedEncodingException;

import com.logica.smpp.util.ByteBuffer;

/**
 * 
 * @author Nam
 * 
 */
public class PushMessage
{
	/**
	 * Ports for the WDP information element, instructing the hand-set which
	 * application to load on receiving the message
	 */
	protected static byte[]	WDP_DESTINATIONPORT	= new byte[] { 0x0b, (byte) 0x84 };
	protected static byte[]	WDP_SOURCEPORT		= new byte[] { 0x23, (byte) 0xf0 };

	ServiceIndication		serviceIndication;

	public PushMessage(String href, String text)
	{
		this.serviceIndication = new ServiceIndication(href, text, ServiceIndicationAction.Signal_high);
	}

	public PushMessage(String href, String text, String encoding)
	{
		this.serviceIndication = new ServiceIndication(href, text, encoding, ServiceIndicationAction.Signal_high);
	}

	/**
	 * Generates the body of the SMS message
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public byte[] getSMSBytes() throws UnsupportedEncodingException
	{
		ByteBuffer buffer = new ByteBuffer();
		// byte[] wdpHeader = GetWDPHeaderBytes();
		// stream.Write(wdpHeader, 0, wdpHeader.Length);

		byte[] pdu = getPDUBytes();
		buffer.appendBytes(pdu);

		return buffer.getBuffer();
	}

	/**
	 * Generates the PDU (Protocol Data Unit) comprising the encoded Service
	 * Indication and the WSP (Wireless Session Protocol) headers
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public byte[] getPDUBytes() throws UnsupportedEncodingException
	{
		byte[] body = serviceIndication.getWBXMLBytes();

		byte[] headerBuffer = getWSPHeaderBytes((byte) body.length);

		ByteBuffer buffer = new ByteBuffer();
		buffer.appendBytes(headerBuffer);
		buffer.appendBytes(body);

		return buffer.getBuffer();
	}

	/**
	 * Generates the WSP (Wireless Session Protocol) headers with the well known
	 * byte values specific to a Service Indication
	 * 
	 * @param contentLength
	 * @return
	 */
	public byte[] getWSPHeaderBytes(byte contentLength)
	{
		ByteBuffer buffer = new ByteBuffer();

		buffer.appendByte(WSP.TRANSACTIONID_CONNECTIONLESSWSP);
		buffer.appendByte(WSP.PDUTYPE_PUSH);

		ByteBuffer headersBuffer = new ByteBuffer();
		headersBuffer.appendBytes(WSP.HEADER_CONTENTTYPE_application_vnd_wap_sic_utf_8);

		headersBuffer.appendByte(WSP.HEADER_APPLICATIONTYPE);
		headersBuffer.appendByte(WSP.HEADER_APPLICATIONTYPE_x_wap_application_id_w2);
		headersBuffer.appendBytes(WSP.HEADER_PUSHFLAG);

		buffer.appendByte((byte) headersBuffer.length());

		buffer.appendBytes(headersBuffer.getBuffer());

		return buffer.getBuffer();
	}

	/**
	 * 
	 * Generates the WDP (Wireless Datagram Protocol) or UDH (User Data Header)
	 * for the SMS message. In the case comprising the Application Port
	 * information element indicating to the handset which application to start
	 * on receipt of the message
	 * 
	 * @return
	 */
	public byte[] getWDPHeaderBytes()
	{
		ByteBuffer buffer = new ByteBuffer();
		buffer.appendByte(WDP.INFORMATIONELEMENT_IDENTIFIER_APPLICATIONPORT);
		buffer.appendByte((byte) (WDP_DESTINATIONPORT.length + WDP_SOURCEPORT.length));
		buffer.appendBytes(WDP_DESTINATIONPORT);
		buffer.appendBytes(WDP_SOURCEPORT);

		ByteBuffer headerBuffer = new ByteBuffer();

		// write length of header
		headerBuffer.appendByte((byte) buffer.length());

		headerBuffer.appendBuffer(buffer);
		return headerBuffer.getBuffer();
	}
}
