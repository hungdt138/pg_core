/**
 * TpReleaseCause.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpReleaseCause implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpReleaseCause(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_UNDEFINED = "P_UNDEFINED";
    public static final java.lang.String _P_USER_NOT_AVAILABLE = "P_USER_NOT_AVAILABLE";
    public static final java.lang.String _P_BUSY = "P_BUSY";
    public static final java.lang.String _P_NO_ANSWER = "P_NO_ANSWER";
    public static final java.lang.String _P_NOT_REACHABLE = "P_NOT_REACHABLE";
    public static final java.lang.String _P_ROUTING_FAILURE = "P_ROUTING_FAILURE";
    public static final java.lang.String _P_PREMATURE_DISCONNECT = "P_PREMATURE_DISCONNECT";
    public static final java.lang.String _P_DISCONNECTED = "P_DISCONNECTED";
    public static final java.lang.String _P_CALL_RESTRICTED = "P_CALL_RESTRICTED";
    public static final java.lang.String _P_UNAVAILABLE_RESOURCE = "P_UNAVAILABLE_RESOURCE";
    public static final java.lang.String _P_GENERAL_FAILURE = "P_GENERAL_FAILURE";
    public static final java.lang.String _P_TIMER_EXPIRY = "P_TIMER_EXPIRY";
    public static final java.lang.String _P_UNSUPPORTED_MEDIA = "P_UNSUPPORTED_MEDIA";
    public static final TpReleaseCause P_UNDEFINED = new TpReleaseCause(_P_UNDEFINED);
    public static final TpReleaseCause P_USER_NOT_AVAILABLE = new TpReleaseCause(_P_USER_NOT_AVAILABLE);
    public static final TpReleaseCause P_BUSY = new TpReleaseCause(_P_BUSY);
    public static final TpReleaseCause P_NO_ANSWER = new TpReleaseCause(_P_NO_ANSWER);
    public static final TpReleaseCause P_NOT_REACHABLE = new TpReleaseCause(_P_NOT_REACHABLE);
    public static final TpReleaseCause P_ROUTING_FAILURE = new TpReleaseCause(_P_ROUTING_FAILURE);
    public static final TpReleaseCause P_PREMATURE_DISCONNECT = new TpReleaseCause(_P_PREMATURE_DISCONNECT);
    public static final TpReleaseCause P_DISCONNECTED = new TpReleaseCause(_P_DISCONNECTED);
    public static final TpReleaseCause P_CALL_RESTRICTED = new TpReleaseCause(_P_CALL_RESTRICTED);
    public static final TpReleaseCause P_UNAVAILABLE_RESOURCE = new TpReleaseCause(_P_UNAVAILABLE_RESOURCE);
    public static final TpReleaseCause P_GENERAL_FAILURE = new TpReleaseCause(_P_GENERAL_FAILURE);
    public static final TpReleaseCause P_TIMER_EXPIRY = new TpReleaseCause(_P_TIMER_EXPIRY);
    public static final TpReleaseCause P_UNSUPPORTED_MEDIA = new TpReleaseCause(_P_UNSUPPORTED_MEDIA);
    public java.lang.String getValue() { return _value_;}
    public static TpReleaseCause fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpReleaseCause enumeration = (TpReleaseCause)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpReleaseCause fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpReleaseCause.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpReleaseCause"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
