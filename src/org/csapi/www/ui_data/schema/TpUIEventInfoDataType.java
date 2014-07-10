/**
 * TpUIEventInfoDataType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIEventInfoDataType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpUIEventInfoDataType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_UI_EVENT_DATA_TYPE_UNDEFINED = "P_UI_EVENT_DATA_TYPE_UNDEFINED";
    public static final java.lang.String _P_UI_EVENT_DATA_TYPE_UNSPECIFIED = "P_UI_EVENT_DATA_TYPE_UNSPECIFIED";
    public static final java.lang.String _P_UI_EVENT_DATA_TYPE_TEXT = "P_UI_EVENT_DATA_TYPE_TEXT";
    public static final java.lang.String _P_UI_EVENT_DATA_TYPE_USSD_DATA = "P_UI_EVENT_DATA_TYPE_USSD_DATA";
    public static final TpUIEventInfoDataType P_UI_EVENT_DATA_TYPE_UNDEFINED = new TpUIEventInfoDataType(_P_UI_EVENT_DATA_TYPE_UNDEFINED);
    public static final TpUIEventInfoDataType P_UI_EVENT_DATA_TYPE_UNSPECIFIED = new TpUIEventInfoDataType(_P_UI_EVENT_DATA_TYPE_UNSPECIFIED);
    public static final TpUIEventInfoDataType P_UI_EVENT_DATA_TYPE_TEXT = new TpUIEventInfoDataType(_P_UI_EVENT_DATA_TYPE_TEXT);
    public static final TpUIEventInfoDataType P_UI_EVENT_DATA_TYPE_USSD_DATA = new TpUIEventInfoDataType(_P_UI_EVENT_DATA_TYPE_USSD_DATA);
    public java.lang.String getValue() { return _value_;}
    public static TpUIEventInfoDataType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpUIEventInfoDataType enumeration = (TpUIEventInfoDataType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpUIEventInfoDataType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpUIEventInfoDataType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIEventInfoDataType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
