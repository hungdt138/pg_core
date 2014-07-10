/**
 * TpChargingParameterValueType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpChargingParameterValueType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpChargingParameterValueType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_CHS_PARAMETER_INT32 = "P_CHS_PARAMETER_INT32";
    public static final java.lang.String _P_CHS_PARAMETER_FLOAT = "P_CHS_PARAMETER_FLOAT";
    public static final java.lang.String _P_CHS_PARAMETER_STRING = "P_CHS_PARAMETER_STRING";
    public static final java.lang.String _P_CHS_PARAMETER_BOOLEAN = "P_CHS_PARAMETER_BOOLEAN";
    public static final java.lang.String _P_CHS_PARAMETER_OCTETSET = "P_CHS_PARAMETER_OCTETSET";
    public static final TpChargingParameterValueType P_CHS_PARAMETER_INT32 = new TpChargingParameterValueType(_P_CHS_PARAMETER_INT32);
    public static final TpChargingParameterValueType P_CHS_PARAMETER_FLOAT = new TpChargingParameterValueType(_P_CHS_PARAMETER_FLOAT);
    public static final TpChargingParameterValueType P_CHS_PARAMETER_STRING = new TpChargingParameterValueType(_P_CHS_PARAMETER_STRING);
    public static final TpChargingParameterValueType P_CHS_PARAMETER_BOOLEAN = new TpChargingParameterValueType(_P_CHS_PARAMETER_BOOLEAN);
    public static final TpChargingParameterValueType P_CHS_PARAMETER_OCTETSET = new TpChargingParameterValueType(_P_CHS_PARAMETER_OCTETSET);
    public java.lang.String getValue() { return _value_;}
    public static TpChargingParameterValueType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpChargingParameterValueType enumeration = (TpChargingParameterValueType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpChargingParameterValueType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpChargingParameterValueType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterValueType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
