/**
 * TpCallServiceCodeType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallServiceCodeType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpCallServiceCodeType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_CALL_SERVICE_CODE_UNDEFINED = "P_CALL_SERVICE_CODE_UNDEFINED";
    public static final java.lang.String _P_CALL_SERVICE_CODE_DIGITS = "P_CALL_SERVICE_CODE_DIGITS";
    public static final java.lang.String _P_CALL_SERVICE_CODE_FACILITY = "P_CALL_SERVICE_CODE_FACILITY";
    public static final java.lang.String _P_CALL_SERVICE_CODE_U2U = "P_CALL_SERVICE_CODE_U2U";
    public static final java.lang.String _P_CALL_SERVICE_CODE_HOOKFLASH = "P_CALL_SERVICE_CODE_HOOKFLASH";
    public static final java.lang.String _P_CALL_SERVICE_CODE_RECALL = "P_CALL_SERVICE_CODE_RECALL";
    public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_UNDEFINED = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_UNDEFINED);
    public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_DIGITS = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_DIGITS);
    public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_FACILITY = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_FACILITY);
    public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_U2U = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_U2U);
    public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_HOOKFLASH = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_HOOKFLASH);
    public static final TpCallServiceCodeType P_CALL_SERVICE_CODE_RECALL = new TpCallServiceCodeType(_P_CALL_SERVICE_CODE_RECALL);
    public java.lang.String getValue() { return _value_;}
    public static TpCallServiceCodeType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpCallServiceCodeType enumeration = (TpCallServiceCodeType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpCallServiceCodeType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpCallServiceCodeType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallServiceCodeType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
