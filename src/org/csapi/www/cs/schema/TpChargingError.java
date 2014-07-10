/**
 * TpChargingError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpChargingError implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpChargingError(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_CHS_ERR_UNDEFINED = "P_CHS_ERR_UNDEFINED";
    public static final java.lang.String _P_CHS_ERR_ACCOUNT = "P_CHS_ERR_ACCOUNT";
    public static final java.lang.String _P_CHS_ERR_USER = "P_CHS_ERR_USER";
    public static final java.lang.String _P_CHS_ERR_PARAMETER = "P_CHS_ERR_PARAMETER";
    public static final java.lang.String _P_CHS_ERR_NO_DEBIT = "P_CHS_ERR_NO_DEBIT";
    public static final java.lang.String _P_CHS_ERR_NO_CREDIT = "P_CHS_ERR_NO_CREDIT";
    public static final java.lang.String _P_CHS_ERR_VOLUMES = "P_CHS_ERR_VOLUMES";
    public static final java.lang.String _P_CHS_ERR_CURRENCY = "P_CHS_ERR_CURRENCY";
    public static final java.lang.String _P_CHS_ERR_NO_EXTEND = "P_CHS_ERR_NO_EXTEND";
    public static final java.lang.String _P_CHS_ERR_RESERVATION_LIMIT = "P_CHS_ERR_RESERVATION_LIMIT";
    public static final java.lang.String _P_CHS_ERR_CONFIRMATION_REQUIRED = "P_CHS_ERR_CONFIRMATION_REQUIRED";
    public static final TpChargingError P_CHS_ERR_UNDEFINED = new TpChargingError(_P_CHS_ERR_UNDEFINED);
    public static final TpChargingError P_CHS_ERR_ACCOUNT = new TpChargingError(_P_CHS_ERR_ACCOUNT);
    public static final TpChargingError P_CHS_ERR_USER = new TpChargingError(_P_CHS_ERR_USER);
    public static final TpChargingError P_CHS_ERR_PARAMETER = new TpChargingError(_P_CHS_ERR_PARAMETER);
    public static final TpChargingError P_CHS_ERR_NO_DEBIT = new TpChargingError(_P_CHS_ERR_NO_DEBIT);
    public static final TpChargingError P_CHS_ERR_NO_CREDIT = new TpChargingError(_P_CHS_ERR_NO_CREDIT);
    public static final TpChargingError P_CHS_ERR_VOLUMES = new TpChargingError(_P_CHS_ERR_VOLUMES);
    public static final TpChargingError P_CHS_ERR_CURRENCY = new TpChargingError(_P_CHS_ERR_CURRENCY);
    public static final TpChargingError P_CHS_ERR_NO_EXTEND = new TpChargingError(_P_CHS_ERR_NO_EXTEND);
    public static final TpChargingError P_CHS_ERR_RESERVATION_LIMIT = new TpChargingError(_P_CHS_ERR_RESERVATION_LIMIT);
    public static final TpChargingError P_CHS_ERR_CONFIRMATION_REQUIRED = new TpChargingError(_P_CHS_ERR_CONFIRMATION_REQUIRED);
    public java.lang.String getValue() { return _value_;}
    public static TpChargingError fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpChargingError enumeration = (TpChargingError)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpChargingError fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpChargingError.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingError"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
