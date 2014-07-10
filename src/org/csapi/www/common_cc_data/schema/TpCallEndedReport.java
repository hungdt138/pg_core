/**
 * TpCallEndedReport.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallEndedReport  implements java.io.Serializable {
    private int callLegSessionID;
    private org.csapi.www.common_cc_data.schema.TpReleaseCause cause;

    public TpCallEndedReport() {
    }

    public TpCallEndedReport(
           int callLegSessionID,
           org.csapi.www.common_cc_data.schema.TpReleaseCause cause) {
           this.callLegSessionID = callLegSessionID;
           this.cause = cause;
    }


    /**
     * Gets the callLegSessionID value for this TpCallEndedReport.
     * 
     * @return callLegSessionID
     */
    public int getCallLegSessionID() {
        return callLegSessionID;
    }


    /**
     * Sets the callLegSessionID value for this TpCallEndedReport.
     * 
     * @param callLegSessionID
     */
    public void setCallLegSessionID(int callLegSessionID) {
        this.callLegSessionID = callLegSessionID;
    }


    /**
     * Gets the cause value for this TpCallEndedReport.
     * 
     * @return cause
     */
    public org.csapi.www.common_cc_data.schema.TpReleaseCause getCause() {
        return cause;
    }


    /**
     * Sets the cause value for this TpCallEndedReport.
     * 
     * @param cause
     */
    public void setCause(org.csapi.www.common_cc_data.schema.TpReleaseCause cause) {
        this.cause = cause;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallEndedReport)) return false;
        TpCallEndedReport other = (TpCallEndedReport) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.callLegSessionID == other.getCallLegSessionID() &&
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
        _hashCode += getCallLegSessionID();
        if (getCause() != null) {
            _hashCode += getCause().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
