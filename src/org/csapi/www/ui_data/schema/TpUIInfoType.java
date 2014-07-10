/**
 * TpUIInfoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIInfoType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpUIInfoType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_UI_INFO_ID = "P_UI_INFO_ID";
    public static final java.lang.String _P_UI_INFO_DATA = "P_UI_INFO_DATA";
    public static final java.lang.String _P_UI_INFO_ADDRESS = "P_UI_INFO_ADDRESS";
    public static final java.lang.String _P_UI_INFO_BIN_DATA = "P_UI_INFO_BIN_DATA";
    public static final java.lang.String _P_UI_INFO_UUENCODED = "P_UI_INFO_UUENCODED";
    public static final java.lang.String _P_UI_INFO_MIME = "P_UI_INFO_MIME";
    public static final java.lang.String _P_UI_INFO_WAVE = "P_UI_INFO_WAVE";
    public static final java.lang.String _P_UI_INFO_AU = "P_UI_INFO_AU";
    public static final TpUIInfoType P_UI_INFO_ID = new TpUIInfoType(_P_UI_INFO_ID);
    public static final TpUIInfoType P_UI_INFO_DATA = new TpUIInfoType(_P_UI_INFO_DATA);
    public static final TpUIInfoType P_UI_INFO_ADDRESS = new TpUIInfoType(_P_UI_INFO_ADDRESS);
    public static final TpUIInfoType P_UI_INFO_BIN_DATA = new TpUIInfoType(_P_UI_INFO_BIN_DATA);
    public static final TpUIInfoType P_UI_INFO_UUENCODED = new TpUIInfoType(_P_UI_INFO_UUENCODED);
    public static final TpUIInfoType P_UI_INFO_MIME = new TpUIInfoType(_P_UI_INFO_MIME);
    public static final TpUIInfoType P_UI_INFO_WAVE = new TpUIInfoType(_P_UI_INFO_WAVE);
    public static final TpUIInfoType P_UI_INFO_AU = new TpUIInfoType(_P_UI_INFO_AU);
    public java.lang.String getValue() { return _value_;}
    public static TpUIInfoType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpUIInfoType enumeration = (TpUIInfoType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpUIInfoType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpUIInfoType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIInfoType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
