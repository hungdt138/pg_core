/**
 * TpUIVariablePartType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIVariablePartType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpUIVariablePartType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_UI_VARIABLE_PART_INT = "P_UI_VARIABLE_PART_INT";
    public static final java.lang.String _P_UI_VARIABLE_PART_ADDRESS = "P_UI_VARIABLE_PART_ADDRESS";
    public static final java.lang.String _P_UI_VARIABLE_PART_TIME = "P_UI_VARIABLE_PART_TIME";
    public static final java.lang.String _P_UI_VARIABLE_PART_DATE = "P_UI_VARIABLE_PART_DATE";
    public static final java.lang.String _P_UI_VARIABLE_PART_PRICE = "P_UI_VARIABLE_PART_PRICE";
    public static final TpUIVariablePartType P_UI_VARIABLE_PART_INT = new TpUIVariablePartType(_P_UI_VARIABLE_PART_INT);
    public static final TpUIVariablePartType P_UI_VARIABLE_PART_ADDRESS = new TpUIVariablePartType(_P_UI_VARIABLE_PART_ADDRESS);
    public static final TpUIVariablePartType P_UI_VARIABLE_PART_TIME = new TpUIVariablePartType(_P_UI_VARIABLE_PART_TIME);
    public static final TpUIVariablePartType P_UI_VARIABLE_PART_DATE = new TpUIVariablePartType(_P_UI_VARIABLE_PART_DATE);
    public static final TpUIVariablePartType P_UI_VARIABLE_PART_PRICE = new TpUIVariablePartType(_P_UI_VARIABLE_PART_PRICE);
    public java.lang.String getValue() { return _value_;}
    public static TpUIVariablePartType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpUIVariablePartType enumeration = (TpUIVariablePartType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpUIVariablePartType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpUIVariablePartType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIVariablePartType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
