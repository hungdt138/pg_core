/**
 * ReServationStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.nms1.service;

public class ReServationStatus  implements java.io.Serializable {
    private java.util.Calendar expiredDate;

    private java.lang.String msisdn;

    private java.lang.String numberClass;

    private java.lang.String numberStatus;

    private double price;

    public ReServationStatus() {
    }

    public ReServationStatus(
           java.util.Calendar expiredDate,
           java.lang.String msisdn,
           java.lang.String numberClass,
           java.lang.String numberStatus,
           double price) {
           this.expiredDate = expiredDate;
           this.msisdn = msisdn;
           this.numberClass = numberClass;
           this.numberStatus = numberStatus;
           this.price = price;
    }


    /**
     * Gets the expiredDate value for this ReServationStatus.
     * 
     * @return expiredDate
     */
    public java.util.Calendar getExpiredDate() {
        return expiredDate;
    }


    /**
     * Sets the expiredDate value for this ReServationStatus.
     * 
     * @param expiredDate
     */
    public void setExpiredDate(java.util.Calendar expiredDate) {
        this.expiredDate = expiredDate;
    }


    /**
     * Gets the msisdn value for this ReServationStatus.
     * 
     * @return msisdn
     */
    public java.lang.String getMsisdn() {
        return msisdn;
    }


    /**
     * Sets the msisdn value for this ReServationStatus.
     * 
     * @param msisdn
     */
    public void setMsisdn(java.lang.String msisdn) {
        this.msisdn = msisdn;
    }


    /**
     * Gets the numberClass value for this ReServationStatus.
     * 
     * @return numberClass
     */
    public java.lang.String getNumberClass() {
        return numberClass;
    }


    /**
     * Sets the numberClass value for this ReServationStatus.
     * 
     * @param numberClass
     */
    public void setNumberClass(java.lang.String numberClass) {
        this.numberClass = numberClass;
    }


    /**
     * Gets the numberStatus value for this ReServationStatus.
     * 
     * @return numberStatus
     */
    public java.lang.String getNumberStatus() {
        return numberStatus;
    }


    /**
     * Sets the numberStatus value for this ReServationStatus.
     * 
     * @param numberStatus
     */
    public void setNumberStatus(java.lang.String numberStatus) {
        this.numberStatus = numberStatus;
    }


    /**
     * Gets the price value for this ReServationStatus.
     * 
     * @return price
     */
    public double getPrice() {
        return price;
    }


    /**
     * Sets the price value for this ReServationStatus.
     * 
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReServationStatus)) return false;
        ReServationStatus other = (ReServationStatus) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.expiredDate==null && other.getExpiredDate()==null) || 
             (this.expiredDate!=null &&
              this.expiredDate.equals(other.getExpiredDate()))) &&
            ((this.msisdn==null && other.getMsisdn()==null) || 
             (this.msisdn!=null &&
              this.msisdn.equals(other.getMsisdn()))) &&
            ((this.numberClass==null && other.getNumberClass()==null) || 
             (this.numberClass!=null &&
              this.numberClass.equals(other.getNumberClass()))) &&
            ((this.numberStatus==null && other.getNumberStatus()==null) || 
             (this.numberStatus!=null &&
              this.numberStatus.equals(other.getNumberStatus()))) &&
            this.price == other.getPrice();
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
        if (getExpiredDate() != null) {
            _hashCode += getExpiredDate().hashCode();
        }
        if (getMsisdn() != null) {
            _hashCode += getMsisdn().hashCode();
        }
        if (getNumberClass() != null) {
            _hashCode += getNumberClass().hashCode();
        }
        if (getNumberStatus() != null) {
            _hashCode += getNumberStatus().hashCode();
        }
        _hashCode += new Double(getPrice()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReServationStatus.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.nms1.fss.com", "ReServationStatus"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expiredDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "expiredDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msisdn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "msisdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberClass");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numberClass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numberStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("price");
        elemField.setXmlName(new javax.xml.namespace.QName("", "price"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
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
