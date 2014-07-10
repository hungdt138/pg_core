/**
 * TpCallMonitorMode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallMonitorMode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpCallMonitorMode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_CALL_MONITOR_MODE_INTERRUPT = "P_CALL_MONITOR_MODE_INTERRUPT";
    public static final java.lang.String _P_CALL_MONITOR_MODE_NOTIFY = "P_CALL_MONITOR_MODE_NOTIFY";
    public static final java.lang.String _P_CALL_MONITOR_MODE_DO_NOT_MONITOR = "P_CALL_MONITOR_MODE_DO_NOT_MONITOR";
    public static final TpCallMonitorMode P_CALL_MONITOR_MODE_INTERRUPT = new TpCallMonitorMode(_P_CALL_MONITOR_MODE_INTERRUPT);
    public static final TpCallMonitorMode P_CALL_MONITOR_MODE_NOTIFY = new TpCallMonitorMode(_P_CALL_MONITOR_MODE_NOTIFY);
    public static final TpCallMonitorMode P_CALL_MONITOR_MODE_DO_NOT_MONITOR = new TpCallMonitorMode(_P_CALL_MONITOR_MODE_DO_NOT_MONITOR);
    public java.lang.String getValue() { return _value_;}
    public static TpCallMonitorMode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpCallMonitorMode enumeration = (TpCallMonitorMode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpCallMonitorMode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpCallMonitorMode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallMonitorMode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
