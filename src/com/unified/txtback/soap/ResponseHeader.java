/**
 * ResponseHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unified.txtback.soap;

public class ResponseHeader  implements java.io.Serializable {
    private java.lang.String additionalResponse;

    private int responseCode;

    private java.lang.String responseDescription;

    private java.lang.String responseName;

    public ResponseHeader() {
    }

    public ResponseHeader(
           java.lang.String additionalResponse,
           int responseCode,
           java.lang.String responseDescription,
           java.lang.String responseName) {
           this.additionalResponse = additionalResponse;
           this.responseCode = responseCode;
           this.responseDescription = responseDescription;
           this.responseName = responseName;
    }


    /**
     * Gets the additionalResponse value for this ResponseHeader.
     * 
     * @return additionalResponse
     */
    public java.lang.String getAdditionalResponse() {
        return additionalResponse;
    }


    /**
     * Sets the additionalResponse value for this ResponseHeader.
     * 
     * @param additionalResponse
     */
    public void setAdditionalResponse(java.lang.String additionalResponse) {
        this.additionalResponse = additionalResponse;
    }


    /**
     * Gets the responseCode value for this ResponseHeader.
     * 
     * @return responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }


    /**
     * Sets the responseCode value for this ResponseHeader.
     * 
     * @param responseCode
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }


    /**
     * Gets the responseDescription value for this ResponseHeader.
     * 
     * @return responseDescription
     */
    public java.lang.String getResponseDescription() {
        return responseDescription;
    }


    /**
     * Sets the responseDescription value for this ResponseHeader.
     * 
     * @param responseDescription
     */
    public void setResponseDescription(java.lang.String responseDescription) {
        this.responseDescription = responseDescription;
    }


    /**
     * Gets the responseName value for this ResponseHeader.
     * 
     * @return responseName
     */
    public java.lang.String getResponseName() {
        return responseName;
    }


    /**
     * Sets the responseName value for this ResponseHeader.
     * 
     * @param responseName
     */
    public void setResponseName(java.lang.String responseName) {
        this.responseName = responseName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResponseHeader)) return false;
        ResponseHeader other = (ResponseHeader) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.additionalResponse==null && other.getAdditionalResponse()==null) || 
             (this.additionalResponse!=null &&
              this.additionalResponse.equals(other.getAdditionalResponse()))) &&
            this.responseCode == other.getResponseCode() &&
            ((this.responseDescription==null && other.getResponseDescription()==null) || 
             (this.responseDescription!=null &&
              this.responseDescription.equals(other.getResponseDescription()))) &&
            ((this.responseName==null && other.getResponseName()==null) || 
             (this.responseName!=null &&
              this.responseName.equals(other.getResponseName())));
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
        if (getAdditionalResponse() != null) {
            _hashCode += getAdditionalResponse().hashCode();
        }
        _hashCode += getResponseCode();
        if (getResponseDescription() != null) {
            _hashCode += getResponseDescription().hashCode();
        }
        if (getResponseName() != null) {
            _hashCode += getResponseName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResponseHeader.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.txtback.unified.com/", "responseHeader"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalResponse");
        elemField.setXmlName(new javax.xml.namespace.QName("", "additionalResponse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "responseCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "responseDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "responseName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
