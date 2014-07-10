/**
 * TpCallTeleService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallTeleService implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpCallTeleService(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_CALL_TELE_SERVICE_UNKNOWN = "P_CALL_TELE_SERVICE_UNKNOWN";
    public static final java.lang.String _P_CALL_TELE_SERVICE_TELEPHONY = "P_CALL_TELE_SERVICE_TELEPHONY";
    public static final java.lang.String _P_CALL_TELE_SERVICE_FAX_2_3 = "P_CALL_TELE_SERVICE_FAX_2_3";
    public static final java.lang.String _P_CALL_TELE_SERVICE_FAX_4_I = "P_CALL_TELE_SERVICE_FAX_4_I";
    public static final java.lang.String _P_CALL_TELE_SERVICE_FAX_4_II_III = "P_CALL_TELE_SERVICE_FAX_4_II_III";
    public static final java.lang.String _P_CALL_TELE_SERVICE_VIDEOTEX_SYN = "P_CALL_TELE_SERVICE_VIDEOTEX_SYN";
    public static final java.lang.String _P_CALL_TELE_SERVICE_VIDEOTEX_INT = "P_CALL_TELE_SERVICE_VIDEOTEX_INT";
    public static final java.lang.String _P_CALL_TELE_SERVICE_TELEX = "P_CALL_TELE_SERVICE_TELEX";
    public static final java.lang.String _P_CALL_TELE_SERVICE_MHS = "P_CALL_TELE_SERVICE_MHS";
    public static final java.lang.String _P_CALL_TELE_SERVICE_OSI = "P_CALL_TELE_SERVICE_OSI";
    public static final java.lang.String _P_CALL_TELE_SERVICE_FTAM = "P_CALL_TELE_SERVICE_FTAM";
    public static final java.lang.String _P_CALL_TELE_SERVICE_VIDEO = "P_CALL_TELE_SERVICE_VIDEO";
    public static final java.lang.String _P_CALL_TELE_SERVICE_VIDEO_CONF = "P_CALL_TELE_SERVICE_VIDEO_CONF";
    public static final java.lang.String _P_CALL_TELE_SERVICE_AUDIOGRAPH_CONF = "P_CALL_TELE_SERVICE_AUDIOGRAPH_CONF";
    public static final java.lang.String _P_CALL_TELE_SERVICE_MULTIMEDIA = "P_CALL_TELE_SERVICE_MULTIMEDIA";
    public static final java.lang.String _P_CALL_TELE_SERVICE_CS_INI_H221 = "P_CALL_TELE_SERVICE_CS_INI_H221";
    public static final java.lang.String _P_CALL_TELE_SERVICE_CS_SUB_H221 = "P_CALL_TELE_SERVICE_CS_SUB_H221";
    public static final java.lang.String _P_CALL_TELE_SERVICE_CS_INI_CALL = "P_CALL_TELE_SERVICE_CS_INI_CALL";
    public static final java.lang.String _P_CALL_TELE_SERVICE_DATATRAFFIC = "P_CALL_TELE_SERVICE_DATATRAFFIC";
    public static final java.lang.String _P_CALL_TELE_SERVICE_EMERGENCY_CALLS = "P_CALL_TELE_SERVICE_EMERGENCY_CALLS";
    public static final java.lang.String _P_CALL_TELE_SERVICE_SMS_MT_PP = "P_CALL_TELE_SERVICE_SMS_MT_PP";
    public static final java.lang.String _P_CALL_TELE_SERVICE_SMS_MO_PP = "P_CALL_TELE_SERVICE_SMS_MO_PP";
    public static final java.lang.String _P_CALL_TELE_SERVICE_CELL_BROADCAST = "P_CALL_TELE_SERVICE_CELL_BROADCAST";
    public static final java.lang.String _P_CALL_TELE_SERVICE_ALT_SPEECH_FAX_3 = "P_CALL_TELE_SERVICE_ALT_SPEECH_FAX_3";
    public static final java.lang.String _P_CALL_TELE_SERVICE_AUTOMATIC_FAX_3 = "P_CALL_TELE_SERVICE_AUTOMATIC_FAX_3";
    public static final java.lang.String _P_CALL_TELE_SERVICE_VOICE_GROUP_CALL = "P_CALL_TELE_SERVICE_VOICE_GROUP_CALL";
    public static final java.lang.String _P_CALL_TELE_SERVICE_VOICE_BROADCAST = "P_CALL_TELE_SERVICE_VOICE_BROADCAST";
    public static final TpCallTeleService P_CALL_TELE_SERVICE_UNKNOWN = new TpCallTeleService(_P_CALL_TELE_SERVICE_UNKNOWN);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_TELEPHONY = new TpCallTeleService(_P_CALL_TELE_SERVICE_TELEPHONY);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_FAX_2_3 = new TpCallTeleService(_P_CALL_TELE_SERVICE_FAX_2_3);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_FAX_4_I = new TpCallTeleService(_P_CALL_TELE_SERVICE_FAX_4_I);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_FAX_4_II_III = new TpCallTeleService(_P_CALL_TELE_SERVICE_FAX_4_II_III);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_VIDEOTEX_SYN = new TpCallTeleService(_P_CALL_TELE_SERVICE_VIDEOTEX_SYN);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_VIDEOTEX_INT = new TpCallTeleService(_P_CALL_TELE_SERVICE_VIDEOTEX_INT);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_TELEX = new TpCallTeleService(_P_CALL_TELE_SERVICE_TELEX);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_MHS = new TpCallTeleService(_P_CALL_TELE_SERVICE_MHS);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_OSI = new TpCallTeleService(_P_CALL_TELE_SERVICE_OSI);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_FTAM = new TpCallTeleService(_P_CALL_TELE_SERVICE_FTAM);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_VIDEO = new TpCallTeleService(_P_CALL_TELE_SERVICE_VIDEO);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_VIDEO_CONF = new TpCallTeleService(_P_CALL_TELE_SERVICE_VIDEO_CONF);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_AUDIOGRAPH_CONF = new TpCallTeleService(_P_CALL_TELE_SERVICE_AUDIOGRAPH_CONF);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_MULTIMEDIA = new TpCallTeleService(_P_CALL_TELE_SERVICE_MULTIMEDIA);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_CS_INI_H221 = new TpCallTeleService(_P_CALL_TELE_SERVICE_CS_INI_H221);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_CS_SUB_H221 = new TpCallTeleService(_P_CALL_TELE_SERVICE_CS_SUB_H221);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_CS_INI_CALL = new TpCallTeleService(_P_CALL_TELE_SERVICE_CS_INI_CALL);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_DATATRAFFIC = new TpCallTeleService(_P_CALL_TELE_SERVICE_DATATRAFFIC);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_EMERGENCY_CALLS = new TpCallTeleService(_P_CALL_TELE_SERVICE_EMERGENCY_CALLS);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_SMS_MT_PP = new TpCallTeleService(_P_CALL_TELE_SERVICE_SMS_MT_PP);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_SMS_MO_PP = new TpCallTeleService(_P_CALL_TELE_SERVICE_SMS_MO_PP);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_CELL_BROADCAST = new TpCallTeleService(_P_CALL_TELE_SERVICE_CELL_BROADCAST);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_ALT_SPEECH_FAX_3 = new TpCallTeleService(_P_CALL_TELE_SERVICE_ALT_SPEECH_FAX_3);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_AUTOMATIC_FAX_3 = new TpCallTeleService(_P_CALL_TELE_SERVICE_AUTOMATIC_FAX_3);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_VOICE_GROUP_CALL = new TpCallTeleService(_P_CALL_TELE_SERVICE_VOICE_GROUP_CALL);
    public static final TpCallTeleService P_CALL_TELE_SERVICE_VOICE_BROADCAST = new TpCallTeleService(_P_CALL_TELE_SERVICE_VOICE_BROADCAST);
    public java.lang.String getValue() { return _value_;}
    public static TpCallTeleService fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpCallTeleService enumeration = (TpCallTeleService)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpCallTeleService fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TpCallTeleService.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallTeleService"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
