/**
 * TpCallErrorType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallErrorType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpCallErrorType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_CALL_ERROR_UNDEFINED = "P_CALL_ERROR_UNDEFINED";
    public static final java.lang.String _P_CALL_ERROR_INVALID_ADDRESS = "P_CALL_ERROR_INVALID_ADDRESS";
    public static final java.lang.String _P_CALL_ERROR_INVALID_STATE = "P_CALL_ERROR_INVALID_STATE";
    public static final java.lang.String _P_CALL_ERROR_RESOURCE_UNAVAILABLE = "P_CALL_ERROR_RESOURCE_UNAVAILABLE";
    public static final TpCallErrorType P_CALL_ERROR_UNDEFINED = new TpCallErrorType(_P_CALL_ERROR_UNDEFINED);
    public static final TpCallErrorType P_CALL_ERROR_INVALID_ADDRESS = new TpCallErrorType(_P_CALL_ERROR_INVALID_ADDRESS);
    public static final TpCallErrorType P_CALL_ERROR_INVALID_STATE = new TpCallErrorType(_P_CALL_ERROR_INVALID_STATE);
    public static final TpCallErrorType P_CALL_ERROR_RESOURCE_UNAVAILABLE = new TpCallErrorType(_P_CALL_ERROR_RESOURCE_UNAVAILABLE);
    public java.lang.String getValue() { return _value_;}
    public static TpCallErrorType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpCallErrorType enumeration = (TpCallErrorType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpCallErrorType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpCallErrorType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallErrorType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
