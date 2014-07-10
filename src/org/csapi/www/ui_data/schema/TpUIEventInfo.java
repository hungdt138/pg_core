/**
 * TpUIEventInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIEventInfo  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpAddress originatingAddress;
    private org.csapi.www.osa.schema.TpAddress destinationAddress;
    private java.lang.String serviceCode;
    private org.csapi.www.ui_data.schema.TpUIEventInfoDataType dataTypeIndication;
    private java.lang.String dataString;

    public TpUIEventInfo() {
    }

    public TpUIEventInfo(
           org.csapi.www.osa.schema.TpAddress originatingAddress,
           org.csapi.www.osa.schema.TpAddress destinationAddress,
           java.lang.String serviceCode,
           org.csapi.www.ui_data.schema.TpUIEventInfoDataType dataTypeIndication,
           java.lang.String dataString) {
           this.originatingAddress = originatingAddress;
           this.destinationAddress = destinationAddress;
           this.serviceCode = serviceCode;
           this.dataTypeIndication = dataTypeIndication;
           this.dataString = dataString;
    }


    /**
     * Gets the originatingAddress value for this TpUIEventInfo.
     * 
     * @return originatingAddress
     */
    public org.csapi.www.osa.schema.TpAddress getOriginatingAddress() {
        return originatingAddress;
    }


    /**
     * Sets the originatingAddress value for this TpUIEventInfo.
     * 
     * @param originatingAddress
     */
    public void setOriginatingAddress(org.csapi.www.osa.schema.TpAddress originatingAddress) {
        this.originatingAddress = originatingAddress;
    }


    /**
     * Gets the destinationAddress value for this TpUIEventInfo.
     * 
     * @return destinationAddress
     */
    public org.csapi.www.osa.schema.TpAddress getDestinationAddress() {
        return destinationAddress;
    }


    /**
     * Sets the destinationAddress value for this TpUIEventInfo.
     * 
     * @param destinationAddress
     */
    public void setDestinationAddress(org.csapi.www.osa.schema.TpAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
    }


    /**
     * Gets the serviceCode value for this TpUIEventInfo.
     * 
     * @return serviceCode
     */
    public java.lang.String getServiceCode() {
        return serviceCode;
    }


    /**
     * Sets the serviceCode value for this TpUIEventInfo.
     * 
     * @param serviceCode
     */
    public void setServiceCode(java.lang.String serviceCode) {
        this.serviceCode = serviceCode;
    }


    /**
     * Gets the dataTypeIndication value for this TpUIEventInfo.
     * 
     * @return dataTypeIndication
     */
    public org.csapi.www.ui_data.schema.TpUIEventInfoDataType getDataTypeIndication() {
        return dataTypeIndication;
    }


    /**
     * Sets the dataTypeIndication value for this TpUIEventInfo.
     * 
     * @param dataTypeIndication
     */
    public void setDataTypeIndication(org.csapi.www.ui_data.schema.TpUIEventInfoDataType dataTypeIndication) {
        this.dataTypeIndication = dataTypeIndication;
    }


    /**
     * Gets the dataString value for this TpUIEventInfo.
     * 
     * @return dataString
     */
    public java.lang.String getDataString() {
        return dataString;
    }


    /**
     * Sets the dataString value for this TpUIEventInfo.
     * 
     * @param dataString
     */
    public void setDataString(java.lang.String dataString) {
        this.dataString = dataString;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUIEventInfo)) return false;
        TpUIEventInfo other = (TpUIEventInfo) obj;
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
              this.serviceCode.equals(other.getServiceCode()))) &&
            ((this.dataTypeIndication==null && other.getDataTypeIndication()==null) || 
             (this.dataTypeIndication!=null &&
              this.dataTypeIndication.equals(other.getDataTypeIndication()))) &&
            ((this.dataString==null && other.getDataString()==null) || 
             (this.dataString!=null &&
              this.dataString.equals(other.getDataString())));
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
        if (getDataTypeIndication() != null) {
            _hashCode += getDataTypeIndication().hashCode();
        }
        if (getDataString() != null) {
            _hashCode += getDataString().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
