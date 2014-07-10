/**
 * TpDataSessionQosClass.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpDataSessionQosClass implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpDataSessionQosClass(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL = "P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL";
    public static final java.lang.String _P_DATA_SESSION_QOS_CLASS_STREAMING = "P_DATA_SESSION_QOS_CLASS_STREAMING";
    public static final java.lang.String _P_DATA_SESSION_QOS_CLASS_INTERACTIVE = "P_DATA_SESSION_QOS_CLASS_INTERACTIVE";
    public static final java.lang.String _P_DATA_SESSION_QOS_CLASS_BACKGROUND = "P_DATA_SESSION_QOS_CLASS_BACKGROUND";
    public static final TpDataSessionQosClass P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL = new TpDataSessionQosClass(_P_DATA_SESSION_QOS_CLASS_CONVERSATIONAL);
    public static final TpDataSessionQosClass P_DATA_SESSION_QOS_CLASS_STREAMING = new TpDataSessionQosClass(_P_DATA_SESSION_QOS_CLASS_STREAMING);
    public static final TpDataSessionQosClass P_DATA_SESSION_QOS_CLASS_INTERACTIVE = new TpDataSessionQosClass(_P_DATA_SESSION_QOS_CLASS_INTERACTIVE);
    public static final TpDataSessionQosClass P_DATA_SESSION_QOS_CLASS_BACKGROUND = new TpDataSessionQosClass(_P_DATA_SESSION_QOS_CLASS_BACKGROUND);
    public java.lang.String getValue() { return _value_;}
    public static TpDataSessionQosClass fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpDataSessionQosClass enumeration = (TpDataSessionQosClass)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpDataSessionQosClass fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpDataSessionQosClass.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpDataSessionQosClass"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
