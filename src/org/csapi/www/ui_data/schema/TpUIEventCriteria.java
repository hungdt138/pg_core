/**
 * TpUIEventCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIEventCriteria  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpAddressRange originatingAddress;
    private org.csapi.www.osa.schema.TpAddressRange destinationAddress;
    private java.lang.String serviceCode;

    public TpUIEventCriteria() {
    }

    public TpUIEventCriteria(
           org.csapi.www.osa.schema.TpAddressRange originatingAddress,
           org.csapi.www.osa.schema.TpAddressRange destinationAddress,
           java.lang.String serviceCode) {
           this.originatingAddress = originatingAddress;
           this.destinationAddress = destinationAddress;
           this.serviceCode = serviceCode;
    }


    /**
     * Gets the originatingAddress value for this TpUIEventCriteria.
     * 
     * @return originatingAddress
     */
    public org.csapi.www.osa.schema.TpAddressRange getOriginatingAddress() {
        return originatingAddress;
    }


    /**
     * Sets the originatingAddress value for this TpUIEventCriteria.
     * 
     * @param originatingAddress
     */
    public void setOriginatingAddress(org.csapi.www.osa.schema.TpAddressRange originatingAddress) {
        this.originatingAddress = originatingAddress;
    }


    /**
     * Gets the destinationAddress value for this TpUIEventCriteria.
     * 
     * @return destinationAddress
     */
    public org.csapi.www.osa.schema.TpAddressRange getDestinationAddress() {
        return destinationAddress;
    }


    /**
     * Sets the destinationAddress value for this TpUIEventCriteria.
     * 
     * @param destinationAddress
     */
    public void setDestinationAddress(org.csapi.www.osa.schema.TpAddressRange destinationAddress) {
        this.destinationAddress = destinationAddress;
    }


    /**
     * Gets the serviceCode value for this TpUIEventCriteria.
     * 
     * @return serviceCode
     */
    public java.lang.String getServiceCode() {
        return serviceCode;
    }


    /**
     * Sets the serviceCode value for this TpUIEventCriteria.
     * 
     * @param serviceCode
     */
    public void setServiceCode(java.lang.String serviceCode) {
        this.serviceCode = serviceCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUIEventCriteria)) return false;
        TpUIEventCriteria other = (TpUIEventCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.originatingAddress==null && other.getOriginatingAddress()==null) || 
             (this.originatingAddress!=null &&
              this.originatingAddress.equals(other.getOriginatingAddress()))) &&
            ((this.destinationAddress==null && other.getDestinationAddress()==null) || 
             (this.destinationAddress!=null &&
              this.destinationAddress.equals(other.getDestinationAddress()))) &&
            ((this.serviceCode==null && other.getServiceCode()==null) || 
             (this.serviceCode!=null &&
              this.serviceCode.equals(other.getServiceCode())));
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
        if (getOriginatingAddress() != null) {
            _hashCode += getOriginatingAddress().hashCode();
        }
        if (getDestinationAddress() != null) {
            _hashCode += getDestinationAddress().hashCode();
        }
        if (getServiceCode() != null) {
            _hashCode += getServiceCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
