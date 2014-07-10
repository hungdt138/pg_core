/**
 * BasicInput.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.SMSUtility;

public class BasicInput  implements java.io.Serializable {
    private java.lang.String username;

    private java.lang.String password;

    private java.lang.String agentMsIsdn;

    private java.lang.String mpin;

    private java.lang.String tokenKey;

    public BasicInput() {
    }

    public BasicInput(
           java.lang.String username,
           java.lang.String password,
           java.lang.String agentMsIsdn,
           java.lang.String mpin,
           java.lang.String tokenKey) {
           this.username = username;
           this.password = password;
           this.agentMsIsdn = agentMsIsdn;
           this.mpin = mpin;
           this.tokenKey = tokenKey;
    }


    /**
     * Gets the username value for this BasicInput.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this BasicInput.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the password value for this BasicInput.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this BasicInput.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the agentMsIsdn value for this BasicInput.
     * 
     * @return agentMsIsdn
     */
    public java.lang.String getAgentMsIsdn() {
        return agentMsIsdn;
    }


    /**
     * Sets the agentMsIsdn value for this BasicInput.
     * 
     * @param agentMsIsdn
     */
    public void setAgentMsIsdn(java.lang.String agentMsIsdn) {
        this.agentMsIsdn = agentMsIsdn;
    }


    /**
     * Gets the mpin value for this BasicInput.
     * 
     * @return mpin
     */
    public java.lang.String getMpin() {
        return mpin;
    }


    /**
     * Sets the mpin value for this BasicInput.
     * 
     * @param mpin
     */
    public void setMpin(java.lang.String mpin) {
        this.mpin = mpin;
    }


    /**
     * Gets the tokenKey value for this BasicInput.
     * 
     * @return tokenKey
     */
    public java.lang.String getTokenKey() {
        return tokenKey;
    }


    /**
     * Sets the tokenKey value for this BasicInput.
     * 
     * @param tokenKey
     */
    public void setTokenKey(java.lang.String tokenKey) {
        this.tokenKey = tokenKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BasicInput)) return false;
        BasicInput other = (BasicInput) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.agentMsIsdn==null && other.getAgentMsIsdn()==null) || 
             (this.agentMsIsdn!=null &&
              this.agentMsIsdn.equals(other.getAgentMsIsdn()))) &&
            ((this.mpin==null && other.getMpin()==null) || 
             (this.mpin!=null &&
              this.mpin.equals(other.getMpin()))) &&
            ((this.tokenKey==null && other.getTokenKey()==null) || 
             (this.tokenKey!=null &&
              this.tokenKey.equals(other.getTokenKey())));
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
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getAgentMsIsdn() != null) {
            _hashCode += getAgentMsIsdn().hashCode();
        }
        if (getMpin() != null) {
            _hashCode += getMpin().hashCode();
        }
        if (getTokenKey() != null) {
            _hashCode += getTokenKey().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BasicInput.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://SMSUtility.fss.com", "BasicInput"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("", "username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("agentMsIsdn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "agentMsIsdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mpin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mpin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tokenKey");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tokenKey"));
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
