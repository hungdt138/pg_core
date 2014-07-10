/**
 * ActivationStatusSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sdp.portlet.activation.model;

public class ActivationStatusSoap  implements java.io.Serializable {
    private long activationId;

    private java.lang.String barringStatus;

    private long primaryKey;

    private java.lang.String productId;

    private java.lang.String responseCode;

    private java.lang.String responseDetail;

    private int returnCode;

    private java.lang.String sku;

    private java.lang.String sourceAddress;

    private java.lang.String supplierStatus;

    public ActivationStatusSoap() {
    }

    public ActivationStatusSoap(
           long activationId,
           java.lang.String barringStatus,
           long primaryKey,
           java.lang.String productId,
           java.lang.String responseCode,
           java.lang.String responseDetail,
           int returnCode,
           java.lang.String sku,
           java.lang.String sourceAddress,
           java.lang.String supplierStatus) {
           this.activationId = activationId;
           this.barringStatus = barringStatus;
           this.primaryKey = primaryKey;
           this.productId = productId;
           this.responseCode = responseCode;
           this.responseDetail = responseDetail;
           this.returnCode = returnCode;
           this.sku = sku;
           this.sourceAddress = sourceAddress;
           this.supplierStatus = supplierStatus;
    }


    /**
     * Gets the activationId value for this ActivationStatusSoap.
     * 
     * @return activationId
     */
    public long getActivationId() {
        return activationId;
    }


    /**
     * Sets the activationId value for this ActivationStatusSoap.
     * 
     * @param activationId
     */
    public void setActivationId(long activationId) {
        this.activationId = activationId;
    }


    /**
     * Gets the barringStatus value for this ActivationStatusSoap.
     * 
     * @return barringStatus
     */
    public java.lang.String getBarringStatus() {
        return barringStatus;
    }


    /**
     * Sets the barringStatus value for this ActivationStatusSoap.
     * 
     * @param barringStatus
     */
    public void setBarringStatus(java.lang.String barringStatus) {
        this.barringStatus = barringStatus;
    }


    /**
     * Gets the primaryKey value for this ActivationStatusSoap.
     * 
     * @return primaryKey
     */
    public long getPrimaryKey() {
        return primaryKey;
    }


    /**
     * Sets the primaryKey value for this ActivationStatusSoap.
     * 
     * @param primaryKey
     */
    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }


    /**
     * Gets the productId value for this ActivationStatusSoap.
     * 
     * @return productId
     */
    public java.lang.String getProductId() {
        return productId;
    }


    /**
     * Sets the productId value for this ActivationStatusSoap.
     * 
     * @param productId
     */
    public void setProductId(java.lang.String productId) {
        this.productId = productId;
    }


    /**
     * Gets the responseCode value for this ActivationStatusSoap.
     * 
     * @return responseCode
     */
    public java.lang.String getResponseCode() {
        return responseCode;
    }


    /**
     * Sets the responseCode value for this ActivationStatusSoap.
     * 
     * @param responseCode
     */
    public void setResponseCode(java.lang.String responseCode) {
        this.responseCode = responseCode;
    }


    /**
     * Gets the responseDetail value for this ActivationStatusSoap.
     * 
     * @return responseDetail
     */
    public java.lang.String getResponseDetail() {
        return responseDetail;
    }


    /**
     * Sets the responseDetail value for this ActivationStatusSoap.
     * 
     * @param responseDetail
     */
    public void setResponseDetail(java.lang.String responseDetail) {
        this.responseDetail = responseDetail;
    }


    /**
     * Gets the returnCode value for this ActivationStatusSoap.
     * 
     * @return returnCode
     */
    public int getReturnCode() {
        return returnCode;
    }


    /**
     * Sets the returnCode value for this ActivationStatusSoap.
     * 
     * @param returnCode
     */
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }


    /**
     * Gets the sku value for this ActivationStatusSoap.
     * 
     * @return sku
     */
    public java.lang.String getSku() {
        return sku;
    }


    /**
     * Sets the sku value for this ActivationStatusSoap.
     * 
     * @param sku
     */
    public void setSku(java.lang.String sku) {
        this.sku = sku;
    }


    /**
     * Gets the sourceAddress value for this ActivationStatusSoap.
     * 
     * @return sourceAddress
     */
    public java.lang.String getSourceAddress() {
        return sourceAddress;
    }


    /**
     * Sets the sourceAddress value for this ActivationStatusSoap.
     * 
     * @param sourceAddress
     */
    public void setSourceAddress(java.lang.String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }


    /**
     * Gets the supplierStatus value for this ActivationStatusSoap.
     * 
     * @return supplierStatus
     */
    public java.lang.String getSupplierStatus() {
        return supplierStatus;
    }


    /**
     * Sets the supplierStatus value for this ActivationStatusSoap.
     * 
     * @param supplierStatus
     */
    public void setSupplierStatus(java.lang.String supplierStatus) {
        this.supplierStatus = supplierStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActivationStatusSoap)) return false;
        ActivationStatusSoap other = (ActivationStatusSoap) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.activationId == other.getActivationId() &&
            ((this.barringStatus==null && other.getBarringStatus()==null) || 
             (this.barringStatus!=null &&
              this.barringStatus.equals(other.getBarringStatus()))) &&
            this.primaryKey == other.getPrimaryKey() &&
            ((this.productId==null && other.getProductId()==null) || 
             (this.productId!=null &&
              this.productId.equals(other.getProductId()))) &&
            ((this.responseCode==null && other.getResponseCode()==null) || 
             (this.responseCode!=null &&
              this.responseCode.equals(other.getResponseCode()))) &&
            ((this.responseDetail==null && other.getResponseDetail()==null) || 
             (this.responseDetail!=null &&
              this.responseDetail.equals(other.getResponseDetail()))) &&
            this.returnCode == other.getReturnCode() &&
            ((this.sku==null && other.getSku()==null) || 
             (this.sku!=null &&
              this.sku.equals(other.getSku()))) &&
            ((this.sourceAddress==null && other.getSourceAddress()==null) || 
             (this.sourceAddress!=null &&
              this.sourceAddress.equals(other.getSourceAddress()))) &&
            ((this.supplierStatus==null && other.getSupplierStatus()==null) || 
             (this.supplierStatus!=null &&
              this.supplierStatus.equals(other.getSupplierStatus())));
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
        _hashCode += new Long(getActivationId()).hashCode();
        if (getBarringStatus() != null) {
            _hashCode += getBarringStatus().hashCode();
        }
        _hashCode += new Long(getPrimaryKey()).hashCode();
        if (getProductId() != null) {
            _hashCode += getProductId().hashCode();
        }
        if (getResponseCode() != null) {
            _hashCode += getResponseCode().hashCode();
        }
        if (getResponseDetail() != null) {
            _hashCode += getResponseDetail().hashCode();
        }
        _hashCode += getReturnCode();
        if (getSku() != null) {
            _hashCode += getSku().hashCode();
        }
        if (getSourceAddress() != null) {
            _hashCode += getSourceAddress().hashCode();
        }
        if (getSupplierStatus() != null) {
            _hashCode += getSupplierStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ActivationStatusSoap.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.activation.portlet.sdp.com", "ActivationStatusSoap"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activationId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "activationId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("barringStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "barringStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("primaryKey");
        elemField.setXmlName(new javax.xml.namespace.QName("", "primaryKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "productId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "responseCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("", "responseDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "returnCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sku");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sku"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supplierStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "supplierStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
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
