/**
 * TpUIEventNotificationInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIEventNotificationInfo  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpAddress originatingAddress;
    private org.csapi.www.osa.schema.TpAddress destinationAddress;
    private java.lang.String serviceCode;
    private org.csapi.www.ui_data.schema.TpUIEventInfoDataType dataTypeIndication;
    private org.csapi.www.osa.schema.TpOctetSet UIEventData;

    public TpUIEventNotificationInfo() {
    }

    public TpUIEventNotificationInfo(
           org.csapi.www.osa.schema.TpAddress originatingAddress,
           org.csapi.www.osa.schema.TpAddress destinationAddress,
           java.lang.String serviceCode,
           org.csapi.www.ui_data.schema.TpUIEventInfoDataType dataTypeIndication,
           org.csapi.www.osa.schema.TpOctetSet UIEventData) {
           this.originatingAddress = originatingAddress;
           this.destinationAddress = destinationAddress;
           this.serviceCode = serviceCode;
           this.dataTypeIndication = dataTypeIndication;
           this.UIEventData = UIEventData;
    }


    /**
     * Gets the originatingAddress value for this TpUIEventNotificationInfo.
     * 
     * @return originatingAddress
     */
    public org.csapi.www.osa.schema.TpAddress getOriginatingAddress() {
        return originatingAddress;
    }


    /**
     * Sets the originatingAddress value for this TpUIEventNotificationInfo.
     * 
     * @param originatingAddress
     */
    public void setOriginatingAddress(org.csapi.www.osa.schema.TpAddress originatingAddress) {
        this.originatingAddress = originatingAddress;
    }


    /**
     * Gets the destinationAddress value for this TpUIEventNotificationInfo.
     * 
     * @return destinationAddress
     */
    public org.csapi.www.osa.schema.TpAddress getDestinationAddress() {
        return destinationAddress;
    }


    /**
     * Sets the destinationAddress value for this TpUIEventNotificationInfo.
     * 
     * @param destinationAddress
     */
    public void setDestinationAddress(org.csapi.www.osa.schema.TpAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
    }


    /**
     * Gets the serviceCode value for this TpUIEventNotificationInfo.
     * 
     * @return serviceCode
     */
    public java.lang.String getServiceCode() {
        return serviceCode;
    }


    /**
     * Sets the serviceCode value for this TpUIEventNotificationInfo.
     * 
     * @param serviceCode
     */
    public void setServiceCode(java.lang.String serviceCode) {
        this.serviceCode = serviceCode;
    }


    /**
     * Gets the dataTypeIndication value for this TpUIEventNotificationInfo.
     * 
     * @return dataTypeIndication
     */
    public org.csapi.www.ui_data.schema.TpUIEventInfoDataType getDataTypeIndication() {
        return dataTypeIndication;
    }


    /**
     * Sets the dataTypeIndication value for this TpUIEventNotificationInfo.
     * 
     * @param dataTypeIndication
     */
    public void setDataTypeIndication(org.csapi.www.ui_data.schema.TpUIEventInfoDataType dataTypeIndication) {
        this.dataTypeIndication = dataTypeIndication;
    }


    /**
     * Gets the UIEventData value for this TpUIEventNotificationInfo.
     * 
     * @return UIEventData
     */
    public org.csapi.www.osa.schema.TpOctetSet getUIEventData() {
        return UIEventData;
    }


    /**
     * Sets the UIEventData value for this TpUIEventNotificationInfo.
     * 
     * @param UIEventData
     */
    public void setUIEventData(org.csapi.www.osa.schema.TpOctetSet UIEventData) {
        this.UIEventData = UIEventData;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUIEventNotificationInfo)) return false;
        TpUIEventNotificationInfo other = (TpUIEventNotificationInfo) obj;
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
            ((this.UIEventData==null && other.getUIEventData()==null) || 
             (this.UIEventData!=null &&
              this.UIEventData.equals(other.getUIEventData())));
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
        if (getUIEventData() != null) {
            _hashCode += getUIEventData().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
