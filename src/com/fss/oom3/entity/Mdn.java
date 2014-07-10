/**
 * Mdn.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.oom3.entity;

public class Mdn  implements java.io.Serializable {
    private java.lang.String compactMdn;

    private java.lang.String fullMdn;

    private java.lang.String mdn;

    private java.lang.String normalMdn;

    public Mdn() {
    }

    public Mdn(
           java.lang.String compactMdn,
           java.lang.String fullMdn,
           java.lang.String mdn,
           java.lang.String normalMdn) {
           this.compactMdn = compactMdn;
           this.fullMdn = fullMdn;
           this.mdn = mdn;
           this.normalMdn = normalMdn;
    }


    /**
     * Gets the compactMdn value for this Mdn.
     * 
     * @return compactMdn
     */
    public java.lang.String getCompactMdn() {
        return compactMdn;
    }


    /**
     * Sets the compactMdn value for this Mdn.
     * 
     * @param compactMdn
     */
    public void setCompactMdn(java.lang.String compactMdn) {
        this.compactMdn = compactMdn;
    }


    /**
     * Gets the fullMdn value for this Mdn.
     * 
     * @return fullMdn
     */
    public java.lang.String getFullMdn() {
        return fullMdn;
    }


    /**
     * Sets the fullMdn value for this Mdn.
     * 
     * @param fullMdn
     */
    public void setFullMdn(java.lang.String fullMdn) {
        this.fullMdn = fullMdn;
    }


    /**
     * Gets the mdn value for this Mdn.
     * 
     * @return mdn
     */
    public java.lang.String getMdn() {
        return mdn;
    }


    /**
     * Sets the mdn value for this Mdn.
     * 
     * @param mdn
     */
    public void setMdn(java.lang.String mdn) {
        this.mdn = mdn;
    }


    /**
     * Gets the normalMdn value for this Mdn.
     * 
     * @return normalMdn
     */
    public java.lang.String getNormalMdn() {
        return normalMdn;
    }


    /**
     * Sets the normalMdn value for this Mdn.
     * 
     * @param normalMdn
     */
    public void setNormalMdn(java.lang.String normalMdn) {
        this.normalMdn = normalMdn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Mdn)) return false;
        Mdn other = (Mdn) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.compactMdn==null && other.getCompactMdn()==null) || 
             (this.compactMdn!=null &&
              this.compactMdn.equals(other.getCompactMdn()))) &&
            ((this.fullMdn==null && other.getFullMdn()==null) || 
             (this.fullMdn!=null &&
              this.fullMdn.equals(other.getFullMdn()))) &&
            ((this.mdn==null && other.getMdn()==null) || 
             (this.mdn!=null &&
              this.mdn.equals(other.getMdn()))) &&
            ((this.normalMdn==null && other.getNormalMdn()==null) || 
             (this.normalMdn!=null &&
              this.normalMdn.equals(other.getNormalMdn())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCompactMdn() != null) {
            _hashCode += getCompactMdn().hashCode();
        }
        if (getFullMdn() != null) {
            _hashCode += getFullMdn().hashCode();
        }
        if (getMdn() != null) {
            _hashCode += getMdn().hashCode();
        }
        if (getNormalMdn() != null) {
            _hashCode += getNormalMdn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Mdn.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://entity.oom3.fss.com", "Mdn"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compactMdn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "compactMdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fullMdn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fullMdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mdn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("normalMdn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "normalMdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
