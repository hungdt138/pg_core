/**
 * TpCallPartyCategory.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallPartyCategory implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected TpCallPartyCategory(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _P_CALL_PARTY_CATEGORY_UNKNOWN = "P_CALL_PARTY_CATEGORY_UNKNOWN";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_OPERATOR_F = "P_CALL_PARTY_CATEGORY_OPERATOR_F";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_OPERATOR_E = "P_CALL_PARTY_CATEGORY_OPERATOR_E";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_OPERATOR_G = "P_CALL_PARTY_CATEGORY_OPERATOR_G";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_OPERATOR_R = "P_CALL_PARTY_CATEGORY_OPERATOR_R";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_OPERATOR_S = "P_CALL_PARTY_CATEGORY_OPERATOR_S";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_ORDINARY_SUB = "P_CALL_PARTY_CATEGORY_ORDINARY_SUB";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_PRIORITY_SUB = "P_CALL_PARTY_CATEGORY_PRIORITY_SUB";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_DATA_CALL = "P_CALL_PARTY_CATEGORY_DATA_CALL";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_TEST_CALL = "P_CALL_PARTY_CATEGORY_TEST_CALL";
    public static final java.lang.String _P_CALL_PARTY_CATEGORY_PAYPHONE = "P_CALL_PARTY_CATEGORY_PAYPHONE";
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_UNKNOWN = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_UNKNOWN);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_F = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_F);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_E = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_E);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_G = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_G);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_R = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_R);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_OPERATOR_S = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_OPERATOR_S);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_ORDINARY_SUB = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_ORDINARY_SUB);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_PRIORITY_SUB = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_PRIORITY_SUB);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_DATA_CALL = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_DATA_CALL);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_TEST_CALL = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_TEST_CALL);
    public static final TpCallPartyCategory P_CALL_PARTY_CATEGORY_PAYPHONE = new TpCallPartyCategory(_P_CALL_PARTY_CATEGORY_PAYPHONE);
    public java.lang.String getValue() { return _value_;}
    public static TpCallPartyCategory fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        TpCallPartyCategory enumeration = (TpCallPartyCategory)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static TpCallPartyCategory fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(TpCallPartyCategory.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallPartyCategory"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
