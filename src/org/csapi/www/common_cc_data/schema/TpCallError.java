/**
 * TpCallError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallError  implements java.io.Serializable {
    private org.csapi.www.common_cc_data.schema.TpCallAdditionalErrorInfo additionalErorInfo;
    private java.lang.String errorTime;
    private org.csapi.www.common_cc_data.schema.TpCallErrorType errorType;

    public TpCallError() {
    }

    public TpCallError(
           org.csapi.www.common_cc_data.schema.TpCallAdditionalErrorInfo additionalErorInfo,
           java.lang.String errorTime,
           org.csapi.www.common_cc_data.schema.TpCallErrorType errorType) {
           this.additionalErorInfo = additionalErorInfo;
           this.errorTime = errorTime;
           this.errorType = errorType;
    }


    /**
     * Gets the additionalErorInfo value for this TpCallError.
     * 
     * @return additionalErorInfo
     */
    public org.csapi.www.common_cc_data.schema.TpCallAdditionalErrorInfo getAdditionalErorInfo() {
        return additionalErorInfo;
    }


    /**
     * Sets the additionalErorInfo value for this TpCallError.
     * 
     * @param additionalErorInfo
     */
    public void setAdditionalErorInfo(org.csapi.www.common_cc_data.schema.TpCallAdditionalErrorInfo additionalErorInfo) {
        this.additionalErorInfo = additionalErorInfo;
    }


    /**
     * Gets the errorTime value for this TpCallError.
     * 
     * @return errorTime
     */
    public java.lang.String getErrorTime() {
        return errorTime;
    }


    /**
     * Sets the errorTime value for this TpCallError.
     * 
     * @param errorTime
     */
    public void setErrorTime(java.lang.String errorTime) {
        this.errorTime = errorTime;
    }


    /**
     * Gets the errorType value for this TpCallError.
     * 
     * @return errorType
     */
    public org.csapi.www.common_cc_data.schema.TpCallErrorType getErrorType() {
        return errorType;
    }


    /**
     * Sets the errorType value for this TpCallError.
     * 
     * @param errorType
     */
    public void setErrorType(org.csapi.www.common_cc_data.schema.TpCallErrorType errorType) {
        this.errorType = errorType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallError)) return false;
        TpCallError other = (TpCallError) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.additionalErorInfo==null && other.getAdditionalErorInfo()==null) || 
             (this.additionalErorInfo!=null &&
              this.additionalErorInfo.equals(other.getAdditionalErorInfo()))) &&
            ((this.errorTime==null && other.getErrorTime()==null) || 
             (this.errorTime!=null &&
              this.errorTime.equals(other.getErrorTime()))) &&
            ((this.errorType==null && other.getErrorType()==null) || 
             (this.errorType!=null &&
              this.errorType.equals(other.getErrorType())));
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
        if (getAdditionalErorInfo() != null) {
            _hashCode += getAdditionalErorInfo().hashCode();
        }
        if (getErrorTime() != null) {
            _hashCode += getErrorTime().hashCode();
        }
        if (getErrorType() != null) {
            _hashCode += getErrorType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
