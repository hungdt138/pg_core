/**
 * TpVolume.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpVolume  implements java.io.Serializable {
    private int unit;
    private org.csapi.www.cs.schema.TpAmount amount;

    public TpVolume() {
    }

    public TpVolume(
           int unit,
           org.csapi.www.cs.schema.TpAmount amount) {
           this.unit = unit;
           this.amount = amount;
    }


    /**
     * Gets the unit value for this TpVolume.
     * 
     * @return unit
     */
    public int getUnit() {
        return unit;
    }


    /**
     * Sets the unit value for this TpVolume.
     * 
     * @param unit
     */
    public void setUnit(int unit) {
        this.unit = unit;
    }


    /**
     * Gets the amount value for this TpVolume.
     * 
     * @return amount
     */
    public org.csapi.www.cs.schema.TpAmount getAmount() {
        return amount;
    }


    /**
     * Sets the amount value for this TpVolume.
     * 
     * @param amount
     */
    public void setAmount(org.csapi.www.cs.schema.TpAmount amount) {
        this.amount = amount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpVolume)) return false;
        TpVolume other = (TpVolume) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.unit == other.getUnit() &&
            ((this.amount==null && other.getAmount()==null) || 
             (this.amount!=null &&
              this.amount.equals(other.getAmount())));
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
        _hashCode += getUnit();
        if (getAmount() != null) {
            _hashCode += getAmount().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
