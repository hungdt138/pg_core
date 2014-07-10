/**
 * TpCallInfoReport.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallInfoReport  implements java.io.Serializable {
    private java.lang.String callConnectedToDestinationTime;
    private java.lang.String callConnectedToResourceTime;
    private java.lang.String callEndTime;
    private int callInfoType;
    private java.lang.String callInitiationStartTime;
    private org.csapi.www.common_cc_data.schema.TpReleaseCause cause;

    public TpCallInfoReport() {
    }

    public TpCallInfoReport(
           java.lang.String callConnectedToDestinationTime,
           java.lang.String callConnectedToResourceTime,
           java.lang.String callEndTime,
           int callInfoType,
           java.lang.String callInitiationStartTime,
           org.csapi.www.common_cc_data.schema.TpReleaseCause cause) {
           this.callConnectedToDestinationTime = callConnectedToDestinationTime;
           this.callConnectedToResourceTime = callConnectedToResourceTime;
           this.callEndTime = callEndTime;
           this.callInfoType = callInfoType;
           this.callInitiationStartTime = callInitiationStartTime;
           this.cause = cause;
    }


    /**
     * Gets the callConnectedToDestinationTime value for this TpCallInfoReport.
     * 
     * @return callConnectedToDestinationTime
     */
    public java.lang.String getCallConnectedToDestinationTime() {
        return callConnectedToDestinationTime;
    }


    /**
     * Sets the callConnectedToDestinationTime value for this TpCallInfoReport.
     * 
     * @param callConnectedToDestinationTime
     */
    public void setCallConnectedToDestinationTime(java.lang.String callConnectedToDestinationTime) {
        this.callConnectedToDestinationTime = callConnectedToDestinationTime;
    }


    /**
     * Gets the callConnectedToResourceTime value for this TpCallInfoReport.
     * 
     * @return callConnectedToResourceTime
     */
    public java.lang.String getCallConnectedToResourceTime() {
        return callConnectedToResourceTime;
    }


    /**
     * Sets the callConnectedToResourceTime value for this TpCallInfoReport.
     * 
     * @param callConnectedToResourceTime
     */
    public void setCallConnectedToResourceTime(java.lang.String callConnectedToResourceTime) {
        this.callConnectedToResourceTime = callConnectedToResourceTime;
    }


    /**
     * Gets the callEndTime value for this TpCallInfoReport.
     * 
     * @return callEndTime
     */
    public java.lang.String getCallEndTime() {
        return callEndTime;
    }


    /**
     * Sets the callEndTime value for this TpCallInfoReport.
     * 
     * @param callEndTime
     */
    public void setCallEndTime(java.lang.String callEndTime) {
        this.callEndTime = callEndTime;
    }


    /**
     * Gets the callInfoType value for this TpCallInfoReport.
     * 
     * @return callInfoType
     */
    public int getCallInfoType() {
        return callInfoType;
    }


    /**
     * Sets the callInfoType value for this TpCallInfoReport.
     * 
     * @param callInfoType
     */
    public void setCallInfoType(int callInfoType) {
        this.callInfoType = callInfoType;
    }


    /**
     * Gets the callInitiationStartTime value for this TpCallInfoReport.
     * 
     * @return callInitiationStartTime
     */
    public java.lang.String getCallInitiationStartTime() {
        return callInitiationStartTime;
    }


    /**
     * Sets the callInitiationStartTime value for this TpCallInfoReport.
     * 
     * @param callInitiationStartTime
     */
    public void setCallInitiationStartTime(java.lang.String callInitiationStartTime) {
        this.callInitiationStartTime = callInitiationStartTime;
    }


    /**
     * Gets the cause value for this TpCallInfoReport.
     * 
     * @return cause
     */
    public org.csapi.www.common_cc_data.schema.TpReleaseCause getCause() {
        return cause;
    }


    /**
     * Sets the cause value for this TpCallInfoReport.
     * 
     * @param cause
     */
    public void setCause(org.csapi.www.common_cc_data.schema.TpReleaseCause cause) {
        this.cause = cause;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallInfoReport)) return false;
        TpCallInfoReport other = (TpCallInfoReport) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.callConnectedToDestinationTime==null && other.getCallConnectedToDestinationTime()==null) || 
             (this.callConnectedToDestinationTime!=null &&
              this.callConnectedToDestinationTime.equals(other.getCallConnectedToDestinationTime()))) &&
            ((this.callConnectedToResourceTime==null && other.getCallConnectedToResourceTime()==null) || 
             (this.callConnectedToResourceTime!=null &&
              this.callConnectedToResourceTime.equals(other.getCallConnectedToResourceTime()))) &&
            ((this.callEndTime==null && other.getCallEndTime()==null) || 
             (this.callEndTime!=null &&
              this.callEndTime.equals(other.getCallEndTime()))) &&
            this.callInfoType == other.getCallInfoType() &&
            ((this.callInitiationStartTime==null && other.getCallInitiationStartTime()==null) || 
             (this.callInitiationStartTime!=null &&
              this.callInitiationStartTime.equals(other.getCallInitiationStartTime()))) &&
            ((this.cause==null && other.getCause()==null) || 
             (this.cause!=null &&
              this.cause.equals(other.getCause())));
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
        if (getCallConnectedToDestinationTime() != null) {
            _hashCode += getCallConnectedToDestinationTime().hashCode();
        }
        if (getCallConnectedToResourceTime() != null) {
            _hashCode += getCallConnectedToResourceTime().hashCode();
        }
        if (getCallEndTime() != null) {
            _hashCode += getCallEndTime().hashCode();
        }
        _hashCode += getCallInfoType();
        if (getCallInitiationStartTime() != null) {
            _hashCode += getCallInitiationStartTime().hashCode();
        }
        if (getCause() != null) {
            _hashCode += getCause().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
