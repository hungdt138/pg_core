/**
 * TpCallAdditionalErrorInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallAdditionalErrorInfo  implements java.io.Serializable {
    private org.csapi.www.common_cc_data.schema.TpCallErrorType switchName;
    private org.csapi.www.osa.schema.TpAddressError callErrorInvalidAddress;

    public TpCallAdditionalErrorInfo() {
    }

    public TpCallAdditionalErrorInfo(
           org.csapi.www.common_cc_data.schema.TpCallErrorType switchName,
           org.csapi.www.osa.schema.TpAddressError callErrorInvalidAddress) {
           this.switchName = switchName;
           this.callErrorInvalidAddress = callErrorInvalidAddress;
    }


    /**
     * Gets the switchName value for this TpCallAdditionalErrorInfo.
     * 
     * @return switchName
     */
    public org.csapi.www.common_cc_data.schema.TpCallErrorType getSwitchName() {
        return switchName;
    }


    /**
     * Sets the switchName value for this TpCallAdditionalErrorInfo.
     * 
     * @param switchName
     */
    public void setSwitchName(org.csapi.www.common_cc_data.schema.TpCallErrorType switchName) {
        this.switchName = switchName;
    }


    /**
     * Gets the callErrorInvalidAddress value for this TpCallAdditionalErrorInfo.
     * 
     * @return callErrorInvalidAddress
     */
    public org.csapi.www.osa.schema.TpAddressError getCallErrorInvalidAddress() {
        return callErrorInvalidAddress;
    }


    /**
     * Sets the callErrorInvalidAddress value for this TpCallAdditionalErrorInfo.
     * 
     * @param callErrorInvalidAddress
     */
    public void setCallErrorInvalidAddress(org.csapi.www.osa.schema.TpAddressError callErrorInvalidAddress) {
        this.callErrorInvalidAddress = callErrorInvalidAddress;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallAdditionalErrorInfo)) return false;
        TpCallAdditionalErrorInfo other = (TpCallAdditionalErrorInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.switchName==null && other.getSwitchName()==null) || 
             (this.switchName!=null &&
              this.switchName.equals(other.getSwitchName()))) &&
            ((this.callErrorInvalidAddress==null && other.getCallErrorInvalidAddress()==null) || 
             (this.callErrorInvalidAddress!=null &&
              this.callErrorInvalidAddress.equals(other.getCallErrorInvalidAddress())));
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
        if (getSwitchName() != null) {
            _hashCode += getSwitchName().hashCode();
        }
        if (getCallErrorInvalidAddress() != null) {
            _hashCode += getCallErrorInvalidAddress().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
