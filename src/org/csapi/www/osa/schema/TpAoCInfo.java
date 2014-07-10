/**
 * TpAoCInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpAoCInfo  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpAoCOrder chargeOrder;
    private java.lang.String currency;

    public TpAoCInfo() {
    }

    public TpAoCInfo(
           org.csapi.www.osa.schema.TpAoCOrder chargeOrder,
           java.lang.String currency) {
           this.chargeOrder = chargeOrder;
           this.currency = currency;
    }


    /**
     * Gets the chargeOrder value for this TpAoCInfo.
     * 
     * @return chargeOrder
     */
    public org.csapi.www.osa.schema.TpAoCOrder getChargeOrder() {
        return chargeOrder;
    }


    /**
     * Sets the chargeOrder value for this TpAoCInfo.
     * 
     * @param chargeOrder
     */
    public void setChargeOrder(org.csapi.www.osa.schema.TpAoCOrder chargeOrder) {
        this.chargeOrder = chargeOrder;
    }


    /**
     * Gets the currency value for this TpAoCInfo.
     * 
     * @return currency
     */
    public java.lang.String getCurrency() {
        return currency;
    }


    /**
     * Sets the currency value for this TpAoCInfo.
     * 
     * @param currency
     */
    public void setCurrency(java.lang.String currency) {
        this.currency = currency;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpAoCInfo)) return false;
        TpAoCInfo other = (TpAoCInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.chargeOrder==null && other.getChargeOrder()==null) || 
             (this.chargeOrder!=null &&
              this.chargeOrder.equals(other.getChargeOrder()))) &&
            ((this.currency==null && other.getCurrency()==null) || 
             (this.currency!=null &&
              this.currency.equals(other.getCurrency())));
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
        if (getChargeOrder() != null) {
            _hashCode += getChargeOrder().hashCode();
        }
        if (getCurrency() != null) {
            _hashCode += getCurrency().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
