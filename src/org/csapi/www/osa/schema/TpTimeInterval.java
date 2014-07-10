/**
 * TpTimeInterval.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpTimeInterval  implements java.io.Serializable {
    private java.lang.String startTime;
    private java.lang.String stopTime;

    public TpTimeInterval() {
    }

    public TpTimeInterval(
           java.lang.String startTime,
           java.lang.String stopTime) {
           this.startTime = startTime;
           this.stopTime = stopTime;
    }


    /**
     * Gets the startTime value for this TpTimeInterval.
     * 
     * @return startTime
     */
    public java.lang.String getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this TpTimeInterval.
     * 
     * @param startTime
     */
    public void setStartTime(java.lang.String startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the stopTime value for this TpTimeInterval.
     * 
     * @return stopTime
     */
    public java.lang.String getStopTime() {
        return stopTime;
    }


    /**
     * Sets the stopTime value for this TpTimeInterval.
     * 
     * @param stopTime
     */
    public void setStopTime(java.lang.String stopTime) {
        this.stopTime = stopTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpTimeInterval)) return false;
        TpTimeInterval other = (TpTimeInterval) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime()))) &&
            ((this.stopTime==null && other.getStopTime()==null) || 
             (this.stopTime!=null &&
              this.stopTime.equals(other.getStopTime())));
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
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
        }
        if (getStopTime() != null) {
            _hashCode += getStopTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
