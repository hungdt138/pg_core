/**
 * TpCorrelationID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpCorrelationID  implements java.io.Serializable {
    private int correlationID;
    private int correlationType;

    public TpCorrelationID() {
    }

    public TpCorrelationID(
           int correlationID,
           int correlationType) {
           this.correlationID = correlationID;
           this.correlationType = correlationType;
    }


    /**
     * Gets the correlationID value for this TpCorrelationID.
     *
     * @return correlationID
     */
    public int getCorrelationID() {
        return correlationID;
    }


    /**
     * Sets the correlationID value for this TpCorrelationID.
     *
     * @param correlationID
     */
    public void setCorrelationID(int correlationID) {
        this.correlationID = correlationID;
    }


    /**
     * Gets the correlationType value for this TpCorrelationID.
     *
     * @return correlationType
     */
    public int getCorrelationType() {
        return correlationType;
    }


    /**
     * Sets the correlationType value for this TpCorrelationID.
     *
     * @param correlationType
     */
    public void setCorrelationType(int correlationType) {
        this.correlationType = correlationType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCorrelationID)) return false;
        TpCorrelationID other = (TpCorrelationID) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            this.correlationID == other.getCorrelationID() &&
            this.correlationType == other.getCorrelationType();
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
        _hashCode += getCorrelationID();
        _hashCode += getCorrelationType();
        __hashCodeCalc = false;
        return _hashCode;
    }

}
