/**
 * TpCallNetworkAccessType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallNetworkAccessType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpCallNetworkAccessType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN = "P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN";
    public static final java.lang.String _P_CALL_NETWORK_ACCESS_TYPE_POT = "P_CALL_NETWORK_ACCESS_TYPE_POT";
    public static final java.lang.String _P_CALL_NETWORK_ACCESS_TYPE_ISDN = "P_CALL_NETWORK_ACCESS_TYPE_ISDN";
    public static final java.lang.String _P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET = "P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET";
    public static final java.lang.String _P_CALL_NETWORK_ACCESS_TYPE_XDSL = "P_CALL_NETWORK_ACCESS_TYPE_XDSL";
    public static final java.lang.String _P_CALL_NETWORK_ACCESS_TYPE_WIRELESS = "P_CALL_NETWORK_ACCESS_TYPE_WIRELESS";
    public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_UNKNOWN);
    public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_POT = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_POT);
    public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_ISDN = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_ISDN);
    public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_DIALUPINTERNET);
    public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_XDSL = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_XDSL);
    public static final TpCallNetworkAccessType P_CALL_NETWORK_ACCESS_TYPE_WIRELESS = new TpCallNetworkAccessType(_P_CALL_NETWORK_ACCESS_TYPE_WIRELESS);
    public java.lang.String getValue() { return _value_;}
    public static TpCallNetworkAccessType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpCallNetworkAccessType enumeration = (TpCallNetworkAccessType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpCallNetworkAccessType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpCallNetworkAccessType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallNetworkAccessType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
