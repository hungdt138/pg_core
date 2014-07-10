/**
 * TpChargingPrice.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpChargingPrice  implements java.io.Serializable {
    private java.lang.String currency;
    private org.csapi.www.cs.schema.TpAmount amount;

    public TpChargingPrice() {
    }

    public TpChargingPrice(
           java.lang.String currency,
           org.csapi.www.cs.schema.TpAmount amount) {
           this.currency = currency;
           this.amount = amount;
    }


    /**
     * Gets the currency value for this TpChargingPrice.
     * 
     * @return currency
     */
    public java.lang.String getCurrency() {
        return currency;
    }


    /**
     * Sets the currency value for this TpChargingPrice.
     * 
     * @param currency
     */
    public void setCurrency(java.lang.String currency) {
        this.currency = currency;
    }


    /**
     * Gets the amount value for this TpChargingPrice.
     * 
     * @return amount
     */
    public org.csapi.www.cs.schema.TpAmount getAmount() {
        return amount;
    }


    /**
     * Sets the amount value for this TpChargingPrice.
     * 
     * @param amount
     */
    public void setAmount(org.csapi.www.cs.schema.TpAmount amount) {
        this.amount = amount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpChargingPrice)) return false;
        TpChargingPrice other = (TpChargingPrice) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.currency==null && other.getCurrency()==null) || 
             (this.currency!=null &&
              this.currency.equals(other.getCurrency()))) &&
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
        if (getCurrency() != null) {
            _hashCode += getCurrency().hashCode();
        }
        if (getAmount() != null) {
            _hashCode += getAmount().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
