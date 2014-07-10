/**
 * TpChargePerTime.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpChargePerTime  implements java.io.Serializable {
    private int currentChargePerMinute;
    private int initialCharge;
    private int nextChargePerMinute;

    public TpChargePerTime() {
    }

    public TpChargePerTime(
           int currentChargePerMinute,
           int initialCharge,
           int nextChargePerMinute) {
           this.currentChargePerMinute = currentChargePerMinute;
           this.initialCharge = initialCharge;
           this.nextChargePerMinute = nextChargePerMinute;
    }


    /**
     * Gets the currentChargePerMinute value for this TpChargePerTime.
     * 
     * @return currentChargePerMinute
     */
    public int getCurrentChargePerMinute() {
        return currentChargePerMinute;
    }


    /**
     * Sets the currentChargePerMinute value for this TpChargePerTime.
     * 
     * @param currentChargePerMinute
     */
    public void setCurrentChargePerMinute(int currentChargePerMinute) {
        this.currentChargePerMinute = currentChargePerMinute;
    }


    /**
     * Gets the initialCharge value for this TpChargePerTime.
     * 
     * @return initialCharge
     */
    public int getInitialCharge() {
        return initialCharge;
    }


    /**
     * Sets the initialCharge value for this TpChargePerTime.
     * 
     * @param initialCharge
     */
    public void setInitialCharge(int initialCharge) {
        this.initialCharge = initialCharge;
    }


    /**
     * Gets the nextChargePerMinute value for this TpChargePerTime.
     * 
     * @return nextChargePerMinute
     */
    public int getNextChargePerMinute() {
        return nextChargePerMinute;
    }


    /**
     * Sets the nextChargePerMinute value for this TpChargePerTime.
     * 
     * @param nextChargePerMinute
     */
    public void setNextChargePerMinute(int nextChargePerMinute) {
        this.nextChargePerMinute = nextChargePerMinute;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpChargePerTime)) return false;
        TpChargePerTime other = (TpChargePerTime) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.currentChargePerMinute == other.getCurrentChargePerMinute() &&
            this.initialCharge == other.getInitialCharge() &&
            this.nextChargePerMinute == other.getNextChargePerMinute();
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
        _hashCode += getCurrentChargePerMinute();
        _hashCode += getInitialCharge();
        _hashCode += getNextChargePerMinute();
        __hashCodeCalc = false;
        return _hashCode;
    }

}
