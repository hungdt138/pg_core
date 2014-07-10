/**
 * DspItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.nms1.service;

public class DspItem  implements java.io.Serializable {
    private java.lang.String msisdn;

    private java.lang.String numberClass;

    private java.lang.String personId;

    private double price;

    private java.lang.String reserveStatus;

    private java.util.Calendar reserveTime;

    public DspItem() {
    }

    public DspItem(
           java.lang.String msisdn,
           java.lang.String numberClass,
           java.lang.String personId,
           double price,
           java.lang.String reserveStatus,
           java.util.Calendar reserveTime) {
           this.msisdn = msisdn;
           this.numberClass = numberClass;
           this.personId = personId;
           this.price = price;
           this.reserveStatus = reserveStatus;
           this.reserveTime = reserveTime;
    }


    /**
     * Gets the msisdn value for this DspItem.
     * 
     * @return msisdn
     */
    public java.lang.String getMsisdn() {
        return msisdn;
    }


    /**
     * Sets the msisdn value for this DspItem.
     * 
     * @param msisdn
     */
    public void setMsisdn(java.lang.String msisdn) {
        this.msisdn = msisdn;
    }


    /**
     * Gets the numberClass value for this DspItem.
     * 
     * @return numberClass
     */
    public java.lang.String getNumberClass() {
        return numberClass;
    }


    /**
     * Sets the numberClass value for this DspItem.
     * 
     * @param numberClass
     */
    public void setNumberClass(java.lang.String numberClass) {
        this.numberClass = numberClass;
    }


    /**
     * Gets the personId value for this DspItem.
     * 
     * @return personId
     */
    public java.lang.String getPersonId() {
        return personId;
    }


    /**
     * Sets the personId value for this DspItem.
     * 
     * @param personId
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }


    /**
     * Gets the price value for this DspItem.
     * 
     * @return price
     */
    public double getPrice() {
        return price;
    }


    /**
     * Sets the price value for this DspItem.
     * 
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }


    /**
     * Gets the reserveStatus value for this DspItem.
     * 
     * @return reserveStatus
     */
    public java.lang.String getReserveStatus() {
        return reserveStatus;
    }


    /**
     * Sets the reserveStatus value for this DspItem.
     * 
     * @param reserveStatus
     */
    public void setReserveStatus(java.lang.String reserveStatus) {
        this.reserveStatus = reserveStatus;
    }


    /**
     * Gets the reserveTime value for this DspItem.
     * 
     * @return reserveTime
     */
    public java.util.Calendar getReserveTime() {
        return reserveTime;
    }


    /**
     * Sets the reserveTime value for this DspItem.
     * 
     * @param reserveTime
     */
    public void setReserveTime(java.util.Calendar reserveTime) {
        this.reserveTime = reserveTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DspItem)) return false;
        DspItem other = (DspItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.msisdn==null && other.getMsisdn()==null) || 
             (this.msisdn!=null &&
              this.msisdn.equals(other.getMsisdn()))) &&
            ((this.numberClass==null && other.getNumberClass()==null) || 
             (this.numberClass!=null &&
              this.numberClass.equals(other.getNumberClass()))) &&
            ((this.personId==null && other.getPersonId()==null) || 
             (this.personId!=null &&
              this.personId.equals(other.getPersonId()))) &&
            this.price == other.getPrice() &&
            ((this.reserveStatus==null && other.getReserveStatus()==null) || 
             (this.reserveStatus!=null &&
              this.reserveStatus.equals(other.getReserveStatus()))) &&
            ((this.reserveTime==null && other.getReserveTime()==null) || 
             (this.reserveTime!=null &&
              this.reserveTime.equals(other.getReserveTime())));
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
        if (getMsisdn() != null) {
            _hashCode += getMsisdn().hashCode();
        }
        if (getNumberClass() != null) {
            _hashCode += getNumberClass().hashCode();
        }
        if (getPersonId() != null) {
            _hashCode += getPersonId().hashCode();
        }
        _hashCode += new Double(getPrice()).hashCode();
        if (getReserveStatus() != null) {
            _hashCode += getReserveStatus().hashCode();
        }
        if (getReserveTime() != null) {
            _hashCode += getReserveTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DspItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.nms1.fss.com", "DspItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("personId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "personId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("price");
        elemField.setXmlName(new javax.xml.namespace.QName("", "price"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reserveStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reserveStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reserveTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reserveTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
