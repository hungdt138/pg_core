/**
 * TpCallBearerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallBearerService implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpCallBearerService(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_CALL_BEARER_SERVICE_UNKNOWN = "P_CALL_BEARER_SERVICE_UNKNOWN";
    public static final java.lang.String _P_CALL_BEARER_SERVICE_SPEECH = "P_CALL_BEARER_SERVICE_SPEECH";
    public static final java.lang.String _P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED = "P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED";
    public static final java.lang.String _P_CALL_BEARER_SERVICE_DIGITALRESTRICTED = "P_CALL_BEARER_SERVICE_DIGITALRESTRICTED";
    public static final java.lang.String _P_CALL_BEARER_SERVICE_AUDIO = "P_CALL_BEARER_SERVICE_AUDIO";
    public static final java.lang.String _P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES = "P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES";
    public static final java.lang.String _P_CALL_BEARER_SERVICE_VIDEO = "P_CALL_BEARER_SERVICE_VIDEO";
    public static final TpCallBearerService P_CALL_BEARER_SERVICE_UNKNOWN = new TpCallBearerService(_P_CALL_BEARER_SERVICE_UNKNOWN);
    public static final TpCallBearerService P_CALL_BEARER_SERVICE_SPEECH = new TpCallBearerService(_P_CALL_BEARER_SERVICE_SPEECH);
    public static final TpCallBearerService P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED = new TpCallBearerService(_P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED);
    public static final TpCallBearerService P_CALL_BEARER_SERVICE_DIGITALRESTRICTED = new TpCallBearerService(_P_CALL_BEARER_SERVICE_DIGITALRESTRICTED);
    public static final TpCallBearerService P_CALL_BEARER_SERVICE_AUDIO = new TpCallBearerService(_P_CALL_BEARER_SERVICE_AUDIO);
    public static final TpCallBearerService P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES = new TpCallBearerService(_P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES);
    public static final TpCallBearerService P_CALL_BEARER_SERVICE_VIDEO = new TpCallBearerService(_P_CALL_BEARER_SERVICE_VIDEO);
    public java.lang.String getValue() { return _value_;}
    public static TpCallBearerService fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpCallBearerService enumeration = (TpCallBearerService)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpCallBearerService fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpCallBearerService.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallBearerService"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
