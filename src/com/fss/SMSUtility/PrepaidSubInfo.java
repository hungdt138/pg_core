/**
 * PrepaidSubInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.SMSUtility;

public class PrepaidSubInfo  implements java.io.Serializable {
    private java.lang.String mdn;

    private java.lang.String fullName;

    private java.lang.String dob;

    private java.lang.String idCard;

    private java.lang.String dateOfIssue;

    private java.lang.String placeOfIssue;

    public PrepaidSubInfo() {
    }

    public PrepaidSubInfo(
           java.lang.String mdn,
           java.lang.String fullName,
           java.lang.String dob,
           java.lang.String idCard,
           java.lang.String dateOfIssue,
           java.lang.String placeOfIssue) {
           this.mdn = mdn;
           this.fullName = fullName;
           this.dob = dob;
           this.idCard = idCard;
           this.dateOfIssue = dateOfIssue;
           this.placeOfIssue = placeOfIssue;
    }


    /**
     * Gets the mdn value for this PrepaidSubInfo.
     * 
     * @return mdn
     */
    public java.lang.String getMdn() {
        return mdn;
    }


    /**
     * Sets the mdn value for this PrepaidSubInfo.
     * 
     * @param mdn
     */
    public void setMdn(java.lang.String mdn) {
        this.mdn = mdn;
    }


    /**
     * Gets the fullName value for this PrepaidSubInfo.
     * 
     * @return fullName
     */
    public java.lang.String getFullName() {
        return fullName;
    }


    /**
     * Sets the fullName value for this PrepaidSubInfo.
     * 
     * @param fullName
     */
    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
    }


    /**
     * Gets the dob value for this PrepaidSubInfo.
     * 
     * @return dob
     */
    public java.lang.String getDob() {
        return dob;
    }


    /**
     * Sets the dob value for this PrepaidSubInfo.
     * 
     * @param dob
     */
    public void setDob(java.lang.String dob) {
        this.dob = dob;
    }


    /**
     * Gets the idCard value for this PrepaidSubInfo.
     * 
     * @return idCard
     */
    public java.lang.String getIdCard() {
        return idCard;
    }


    /**
     * Sets the idCard value for this PrepaidSubInfo.
     * 
     * @param idCard
     */
    public void setIdCard(java.lang.String idCard) {
        this.idCard = idCard;
    }


    /**
     * Gets the dateOfIssue value for this PrepaidSubInfo.
     * 
     * @return dateOfIssue
     */
    public java.lang.String getDateOfIssue() {
        return dateOfIssue;
    }


    /**
     * Sets the dateOfIssue value for this PrepaidSubInfo.
     * 
     * @param dateOfIssue
     */
    public void setDateOfIssue(java.lang.String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }


    /**
     * Gets the placeOfIssue value for this PrepaidSubInfo.
     * 
     * @return placeOfIssue
     */
    public java.lang.String getPlaceOfIssue() {
        return placeOfIssue;
    }


    /**
     * Sets the placeOfIssue value for this PrepaidSubInfo.
     * 
     * @param placeOfIssue
     */
    public void setPlaceOfIssue(java.lang.String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PrepaidSubInfo)) return false;
        PrepaidSubInfo other = (PrepaidSubInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.mdn==null && other.getMdn()==null) || 
             (this.mdn!=null &&
              this.mdn.equals(other.getMdn()))) &&
            ((this.fullName==null && other.getFullName()==null) || 
             (this.fullName!=null &&
              this.fullName.equals(other.getFullName()))) &&
            ((this.dob==null && other.getDob()==null) || 
             (this.dob!=null &&
              this.dob.equals(other.getDob()))) &&
            ((this.idCard==null && other.getIdCard()==null) || 
             (this.idCard!=null &&
              this.idCard.equals(other.getIdCard()))) &&
            ((this.dateOfIssue==null && other.getDateOfIssue()==null) || 
             (this.dateOfIssue!=null &&
              this.dateOfIssue.equals(other.getDateOfIssue()))) &&
            ((this.placeOfIssue==null && other.getPlaceOfIssue()==null) || 
             (this.placeOfIssue!=null &&
              this.placeOfIssue.equals(other.getPlaceOfIssue())));
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
        if (getMdn() != null) {
            _hashCode += getMdn().hashCode();
        }
        if (getFullName() != null) {
            _hashCode += getFullName().hashCode();
        }
        if (getDob() != null) {
            _hashCode += getDob().hashCode();
        }
        if (getIdCard() != null) {
            _hashCode += getIdCard().hashCode();
        }
        if (getDateOfIssue() != null) {
            _hashCode += getDateOfIssue().hashCode();
        }
        if (getPlaceOfIssue() != null) {
            _hashCode += getPlaceOfIssue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PrepaidSubInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://SMSUtility.fss.com", "PrepaidSubInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mdn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mdn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fullName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fullName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dob");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dob"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCard");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateOfIssue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateOfIssue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placeOfIssue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "placeOfIssue"));
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
