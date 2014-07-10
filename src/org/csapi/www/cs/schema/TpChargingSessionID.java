/**
 * TpChargingSessionID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpChargingSessionID  implements java.io.Serializable {
    private int chargingSessionID;
    private int requestNumberFirstRequest;
    private java.lang.String chargingSessionReference;

    public TpChargingSessionID() {
    }

    public TpChargingSessionID(
           int chargingSessionID,
           int requestNumberFirstRequest,
           java.lang.String chargingSessionReference) {
           this.chargingSessionID = chargingSessionID;
           this.requestNumberFirstRequest = requestNumberFirstRequest;
           this.chargingSessionReference = chargingSessionReference;
    }


    /**
     * Gets the chargingSessionID value for this TpChargingSessionID.
     * 
     * @return chargingSessionID
     */
    public int getChargingSessionID() {
        return chargingSessionID;
    }


    /**
     * Sets the chargingSessionID value for this TpChargingSessionID.
     * 
     * @param chargingSessionID
     */
    public void setChargingSessionID(int chargingSessionID) {
        this.chargingSessionID = chargingSessionID;
    }


    /**
     * Gets the requestNumberFirstRequest value for this TpChargingSessionID.
     * 
     * @return requestNumberFirstRequest
     */
    public int getRequestNumberFirstRequest() {
        return requestNumberFirstRequest;
    }


    /**
     * Sets the requestNumberFirstRequest value for this TpChargingSessionID.
     * 
     * @param requestNumberFirstRequest
     */
    public void setRequestNumberFirstRequest(int requestNumberFirstRequest) {
        this.requestNumberFirstRequest = requestNumberFirstRequest;
    }


    /**
     * Gets the chargingSessionReference value for this TpChargingSessionID.
     * 
     * @return chargingSessionReference
     */
    public java.lang.String getChargingSessionReference() {
        return chargingSessionReference;
    }


    /**
     * Sets the chargingSessionReference value for this TpChargingSessionID.
     * 
     * @param chargingSessionReference
     */
    public void setChargingSessionReference(java.lang.String chargingSessionReference) {
        this.chargingSessionReference = chargingSessionReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpChargingSessionID)) return false;
        TpChargingSessionID other = (TpChargingSessionID) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.chargingSessionID == other.getChargingSessionID() &&
            this.requestNumberFirstRequest == other.getRequestNumberFirstRequest() &&
            ((this.chargingSessionReference==null && other.getChargingSessionReference()==null) || 
             (this.chargingSessionReference!=null &&
              this.chargingSessionReference.equals(other.getChargingSessionReference())));
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
        _hashCode += getChargingSessionID();
        _hashCode += getRequestNumberFirstRequest();
        if (getChargingSessionReference() != null) {
            _hashCode += getChargingSessionReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
