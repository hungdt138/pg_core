/**
 * TpUIReport.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIReport implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpUIReport(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_UI_REPORT_UNDEFINED = "P_UI_REPORT_UNDEFINED";
    public static final java.lang.String _P_UI_REPORT_INFO_SENT = "P_UI_REPORT_INFO_SENT";
    public static final java.lang.String _P_UI_REPORT_INFO_COLLECTED = "P_UI_REPORT_INFO_COLLECTED";
    public static final java.lang.String _P_UI_REPORT_NO_INPUT = "P_UI_REPORT_NO_INPUT";
    public static final java.lang.String _P_UI_REPORT_TIMEOUT = "P_UI_REPORT_TIMEOUT";
    public static final java.lang.String _P_UI_REPORT_MESSAGE_STORED = "P_UI_REPORT_MESSAGE_STORED";
    public static final java.lang.String _P_UI_REPORT_MESSAGE_NOT_STORED = "P_UI_REPORT_MESSAGE_NOT_STORED";
    public static final java.lang.String _P_UI_REPORT_MESSAGE_DELETED = "P_UI_REPORT_MESSAGE_DELETED";
    public static final java.lang.String _P_UI_REPORT_MESSAGE_NOT_DELETED = "P_UI_REPORT_MESSAGE_NOT_DELETED";
    public static final TpUIReport P_UI_REPORT_UNDEFINED = new TpUIReport(_P_UI_REPORT_UNDEFINED);
    public static final TpUIReport P_UI_REPORT_INFO_SENT = new TpUIReport(_P_UI_REPORT_INFO_SENT);
    public static final TpUIReport P_UI_REPORT_INFO_COLLECTED = new TpUIReport(_P_UI_REPORT_INFO_COLLECTED);
    public static final TpUIReport P_UI_REPORT_NO_INPUT = new TpUIReport(_P_UI_REPORT_NO_INPUT);
    public static final TpUIReport P_UI_REPORT_TIMEOUT = new TpUIReport(_P_UI_REPORT_TIMEOUT);
    public static final TpUIReport P_UI_REPORT_MESSAGE_STORED = new TpUIReport(_P_UI_REPORT_MESSAGE_STORED);
    public static final TpUIReport P_UI_REPORT_MESSAGE_NOT_STORED = new TpUIReport(_P_UI_REPORT_MESSAGE_NOT_STORED);
    public static final TpUIReport P_UI_REPORT_MESSAGE_DELETED = new TpUIReport(_P_UI_REPORT_MESSAGE_DELETED);
    public static final TpUIReport P_UI_REPORT_MESSAGE_NOT_DELETED = new TpUIReport(_P_UI_REPORT_MESSAGE_NOT_DELETED);
    public java.lang.String getValue() { return _value_;}
    public static TpUIReport fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpUIReport enumeration = (TpUIReport)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpUIReport fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpUIReport.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIReport"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
