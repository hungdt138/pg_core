/**
 * TpUIError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIError implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpUIError(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_UI_ERROR_UNDEFINED = "P_UI_ERROR_UNDEFINED";
    public static final java.lang.String _P_UI_ERROR_ILLEGAL_INFO = "P_UI_ERROR_ILLEGAL_INFO";
    public static final java.lang.String _P_UI_ERROR_ID_NOT_FOUND = "P_UI_ERROR_ID_NOT_FOUND";
    public static final java.lang.String _P_UI_ERROR_RESOURCE_UNAVAILABLE = "P_UI_ERROR_RESOURCE_UNAVAILABLE";
    public static final java.lang.String _P_UI_ERROR_ILLEGAL_RANGE = "P_UI_ERROR_ILLEGAL_RANGE";
    public static final java.lang.String _P_UI_ERROR_IMPROPER_USER_RESPONSE = "P_UI_ERROR_IMPROPER_USER_RESPONSE";
    public static final java.lang.String _P_UI_ERROR_ABANDON = "P_UI_ERROR_ABANDON";
    public static final java.lang.String _P_UI_ERROR_NO_OPERATION_ACTIVE = "P_UI_ERROR_NO_OPERATION_ACTIVE";
    public static final java.lang.String _P_UI_ERROR_NO_SPACE_AVAILABLE = "P_UI_ERROR_NO_SPACE_AVAILABLE";
    public static final java.lang.String _P_UI_ERROR_RESOURCE_TIMEOUT = "P_UI_ERROR_RESOURCE_TIMEOUT";
    public static final TpUIError P_UI_ERROR_UNDEFINED = new TpUIError(_P_UI_ERROR_UNDEFINED);
    public static final TpUIError P_UI_ERROR_ILLEGAL_INFO = new TpUIError(_P_UI_ERROR_ILLEGAL_INFO);
    public static final TpUIError P_UI_ERROR_ID_NOT_FOUND = new TpUIError(_P_UI_ERROR_ID_NOT_FOUND);
    public static final TpUIError P_UI_ERROR_RESOURCE_UNAVAILABLE = new TpUIError(_P_UI_ERROR_RESOURCE_UNAVAILABLE);
    public static final TpUIError P_UI_ERROR_ILLEGAL_RANGE = new TpUIError(_P_UI_ERROR_ILLEGAL_RANGE);
    public static final TpUIError P_UI_ERROR_IMPROPER_USER_RESPONSE = new TpUIError(_P_UI_ERROR_IMPROPER_USER_RESPONSE);
    public static final TpUIError P_UI_ERROR_ABANDON = new TpUIError(_P_UI_ERROR_ABANDON);
    public static final TpUIError P_UI_ERROR_NO_OPERATION_ACTIVE = new TpUIError(_P_UI_ERROR_NO_OPERATION_ACTIVE);
    public static final TpUIError P_UI_ERROR_NO_SPACE_AVAILABLE = new TpUIError(_P_UI_ERROR_NO_SPACE_AVAILABLE);
    public static final TpUIError P_UI_ERROR_RESOURCE_TIMEOUT = new TpUIError(_P_UI_ERROR_RESOURCE_TIMEOUT);
    public java.lang.String getValue() { return _value_;}
    public static TpUIError fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpUIError enumeration = (TpUIError)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpUIError fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpUIError.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIError"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
