/**
 * TpAddressError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpAddressError implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpAddressError(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_ADDRESS_INVALID_UNDEFINED = "P_ADDRESS_INVALID_UNDEFINED";
    public static final java.lang.String _P_ADDRESS_INVALID_MISSING = "P_ADDRESS_INVALID_MISSING";
    public static final java.lang.String _P_ADDRESS_INVALID_MISSING_ELEMENT = "P_ADDRESS_INVALID_MISSING_ELEMENT";
    public static final java.lang.String _P_ADDRESS_INVALID_OUT_OF_RANGE = "P_ADDRESS_INVALID_OUT_OF_RANGE";
    public static final java.lang.String _P_ADDRESS_INVALID_INCOMPLETE = "P_ADDRESS_INVALID_INCOMPLETE";
    public static final java.lang.String _P_ADDRESS_INVALID_CANNOT_DECODE = "P_ADDRESS_INVALID_CANNOT_DECODE";
    public static final TpAddressError P_ADDRESS_INVALID_UNDEFINED = new TpAddressError(_P_ADDRESS_INVALID_UNDEFINED);
    public static final TpAddressError P_ADDRESS_INVALID_MISSING = new TpAddressError(_P_ADDRESS_INVALID_MISSING);
    public static final TpAddressError P_ADDRESS_INVALID_MISSING_ELEMENT = new TpAddressError(_P_ADDRESS_INVALID_MISSING_ELEMENT);
    public static final TpAddressError P_ADDRESS_INVALID_OUT_OF_RANGE = new TpAddressError(_P_ADDRESS_INVALID_OUT_OF_RANGE);
    public static final TpAddressError P_ADDRESS_INVALID_INCOMPLETE = new TpAddressError(_P_ADDRESS_INVALID_INCOMPLETE);
    public static final TpAddressError P_ADDRESS_INVALID_CANNOT_DECODE = new TpAddressError(_P_ADDRESS_INVALID_CANNOT_DECODE);
    public java.lang.String getValue() { return _value_;}
    public static TpAddressError fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpAddressError enumeration = (TpAddressError)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpAddressError fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpAddressError.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAddressError"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
