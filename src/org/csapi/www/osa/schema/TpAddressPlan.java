/**
 * TpAddressPlan.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpAddressPlan implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpAddressPlan(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_ADDRESS_PLAN_NOT_PRESENT = "P_ADDRESS_PLAN_NOT_PRESENT";
    public static final java.lang.String _P_ADDRESS_PLAN_UNDEFINED = "P_ADDRESS_PLAN_UNDEFINED";
    public static final java.lang.String _P_ADDRESS_PLAN_IP = "P_ADDRESS_PLAN_IP";
    public static final java.lang.String _P_ADDRESS_PLAN_MULTICAST = "P_ADDRESS_PLAN_MULTICAST";
    public static final java.lang.String _P_ADDRESS_PLAN_UNICAST = "P_ADDRESS_PLAN_UNICAST";
    public static final java.lang.String _P_ADDRESS_PLAN_E164 = "P_ADDRESS_PLAN_E164";
    public static final java.lang.String _P_ADDRESS_PLAN_AESA = "P_ADDRESS_PLAN_AESA";
    public static final java.lang.String _P_ADDRESS_PLAN_URL = "P_ADDRESS_PLAN_URL";
    public static final java.lang.String _P_ADDRESS_PLAN_NSAP = "P_ADDRESS_PLAN_NSAP";
    public static final java.lang.String _P_ADDRESS_PLAN_SMTP = "P_ADDRESS_PLAN_SMTP";
    public static final java.lang.String _P_ADDRESS_PLAN_MSMAIL = "P_ADDRESS_PLAN_MSMAIL";
    public static final java.lang.String _P_ADDRESS_PLAN_X400 = "P_ADDRESS_PLAN_X400";
    public static final java.lang.String _P_ADDRESS_PLAN_ANY = "P_ADDRESS_PLAN_ANY";
    public static final java.lang.String _P_ADDRESS_PLAN_SIP = "P_ADDRESS_PLAN_SIP";
    public static final java.lang.String _P_ADDRESS_PLAN_NATIONAL = "P_ADDRESS_PLAN_NATIONAL";
    public static final TpAddressPlan P_ADDRESS_PLAN_NOT_PRESENT = new TpAddressPlan(_P_ADDRESS_PLAN_NOT_PRESENT);
    public static final TpAddressPlan P_ADDRESS_PLAN_UNDEFINED = new TpAddressPlan(_P_ADDRESS_PLAN_UNDEFINED);
    public static final TpAddressPlan P_ADDRESS_PLAN_IP = new TpAddressPlan(_P_ADDRESS_PLAN_IP);
    public static final TpAddressPlan P_ADDRESS_PLAN_MULTICAST = new TpAddressPlan(_P_ADDRESS_PLAN_MULTICAST);
    public static final TpAddressPlan P_ADDRESS_PLAN_UNICAST = new TpAddressPlan(_P_ADDRESS_PLAN_UNICAST);
    public static final TpAddressPlan P_ADDRESS_PLAN_E164 = new TpAddressPlan(_P_ADDRESS_PLAN_E164);
    public static final TpAddressPlan P_ADDRESS_PLAN_AESA = new TpAddressPlan(_P_ADDRESS_PLAN_AESA);
    public static final TpAddressPlan P_ADDRESS_PLAN_URL = new TpAddressPlan(_P_ADDRESS_PLAN_URL);
    public static final TpAddressPlan P_ADDRESS_PLAN_NSAP = new TpAddressPlan(_P_ADDRESS_PLAN_NSAP);
    public static final TpAddressPlan P_ADDRESS_PLAN_SMTP = new TpAddressPlan(_P_ADDRESS_PLAN_SMTP);
    public static final TpAddressPlan P_ADDRESS_PLAN_MSMAIL = new TpAddressPlan(_P_ADDRESS_PLAN_MSMAIL);
    public static final TpAddressPlan P_ADDRESS_PLAN_X400 = new TpAddressPlan(_P_ADDRESS_PLAN_X400);
    public static final TpAddressPlan P_ADDRESS_PLAN_ANY = new TpAddressPlan(_P_ADDRESS_PLAN_ANY);
    public static final TpAddressPlan P_ADDRESS_PLAN_SIP = new TpAddressPlan(_P_ADDRESS_PLAN_SIP);
    public static final TpAddressPlan P_ADDRESS_PLAN_NATIONAL = new TpAddressPlan(_P_ADDRESS_PLAN_NATIONAL);
    public java.lang.String getValue() { return _value_;}
    public static TpAddressPlan fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpAddressPlan enumeration = (TpAddressPlan)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpAddressPlan fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpAddressPlan.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAddressPlan"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
