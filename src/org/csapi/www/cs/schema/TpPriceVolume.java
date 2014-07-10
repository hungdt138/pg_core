/**
 * TpPriceVolume.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpPriceVolume  implements java.io.Serializable {
    private org.csapi.www.cs.schema.TpChargingPrice price;
    private org.csapi.www.cs.schema.TpVolume volume;

    public TpPriceVolume() {
    }

    public TpPriceVolume(
           org.csapi.www.cs.schema.TpChargingPrice price,
           org.csapi.www.cs.schema.TpVolume volume) {
           this.price = price;
           this.volume = volume;
    }


    /**
     * Gets the price value for this TpPriceVolume.
     * 
     * @return price
     */
    public org.csapi.www.cs.schema.TpChargingPrice getPrice() {
        return price;
    }


    /**
     * Sets the price value for this TpPriceVolume.
     * 
     * @param price
     */
    public void setPrice(org.csapi.www.cs.schema.TpChargingPrice price) {
        this.price = price;
    }


    /**
     * Gets the volume value for this TpPriceVolume.
     * 
     * @return volume
     */
    public org.csapi.www.cs.schema.TpVolume getVolume() {
        return volume;
    }


    /**
     * Sets the volume value for this TpPriceVolume.
     * 
     * @param volume
     */
    public void setVolume(org.csapi.www.cs.schema.TpVolume volume) {
        this.volume = volume;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpPriceVolume)) return false;
        TpPriceVolume other = (TpPriceVolume) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.price==null && other.getPrice()==null) || 
             (this.price!=null &&
              this.price.equals(other.getPrice()))) &&
            ((this.volume==null && other.getVolume()==null) || 
             (this.volume!=null &&
              this.volume.equals(other.getVolume())));
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
        if (getPrice() != null) {
            _hashCode += getPrice().hashCode();
        }
        if (getVolume() != null) {
            _hashCode += getVolume().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
