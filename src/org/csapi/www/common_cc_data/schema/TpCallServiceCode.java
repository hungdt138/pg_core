/**
 * TpCallServiceCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallServiceCode  implements java.io.Serializable {
    private org.csapi.www.common_cc_data.schema.TpCallServiceCodeType callServiceCodeType;
    private java.lang.String serviceCodeValue;

    public TpCallServiceCode() {
    }

    public TpCallServiceCode(
           org.csapi.www.common_cc_data.schema.TpCallServiceCodeType callServiceCodeType,
           java.lang.String serviceCodeValue) {
           this.callServiceCodeType = callServiceCodeType;
           this.serviceCodeValue = serviceCodeValue;
    }


    /**
     * Gets the callServiceCodeType value for this TpCallServiceCode.
     * 
     * @return callServiceCodeType
     */
    public org.csapi.www.common_cc_data.schema.TpCallServiceCodeType getCallServiceCodeType() {
        return callServiceCodeType;
    }


    /**
     * Sets the callServiceCodeType value for this TpCallServiceCode.
     * 
     * @param callServiceCodeType
     */
    public void setCallServiceCodeType(org.csapi.www.common_cc_data.schema.TpCallServiceCodeType callServiceCodeType) {
        this.callServiceCodeType = callServiceCodeType;
    }


    /**
     * Gets the serviceCodeValue value for this TpCallServiceCode.
     * 
     * @return serviceCodeValue
     */
    public java.lang.String getServiceCodeValue() {
        return serviceCodeValue;
    }


    /**
     * Sets the serviceCodeValue value for this TpCallServiceCode.
     * 
     * @param serviceCodeValue
     */
    public void setServiceCodeValue(java.lang.String serviceCodeValue) {
        this.serviceCodeValue = serviceCodeValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallServiceCode)) return false;
        TpCallServiceCode other = (TpCallServiceCode) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.callServiceCodeType==null && other.getCallServiceCodeType()==null) || 
             (this.callServiceCodeType!=null &&
              this.callServiceCodeType.equals(other.getCallServiceCodeType()))) &&
            ((this.serviceCodeValue==null && other.getServiceCodeValue()==null) || 
             (this.serviceCodeValue!=null &&
              this.serviceCodeValue.equals(other.getServiceCodeValue())));
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
        if (getCallServiceCodeType() != null) {
            _hashCode += getCallServiceCodeType().hashCode();
        }
        if (getServiceCodeValue() != null) {
            _hashCode += getServiceCodeValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
