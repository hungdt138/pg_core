/**
 * TpAmount.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpAmount  implements java.io.Serializable {
    private int number;
    private int exponent;

    public TpAmount() {
    }

    public TpAmount(
           int number,
           int exponent) {
           this.number = number;
           this.exponent = exponent;
    }


    /**
     * Gets the number value for this TpAmount.
     * 
     * @return number
     */
    public int getNumber() {
        return number;
    }


    /**
     * Sets the number value for this TpAmount.
     * 
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }


    /**
     * Gets the exponent value for this TpAmount.
     * 
     * @return exponent
     */
    public int getExponent() {
        return exponent;
    }


    /**
     * Sets the exponent value for this TpAmount.
     * 
     * @param exponent
     */
    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpAmount)) return false;
        TpAmount other = (TpAmount) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.number == other.getNumber() &&
            this.exponent == other.getExponent();
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
        _hashCode += getNumber();
        _hashCode += getExponent();
        __hashCodeCalc = false;
        return _hashCode;
    }

}
