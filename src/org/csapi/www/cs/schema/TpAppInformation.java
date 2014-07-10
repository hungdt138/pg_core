/**
 * TpAppInformation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpAppInformation  implements java.io.Serializable {
    private org.csapi.www.cs.schema.TpAppInformationType switchName;
    private java.lang.String timestamp;

    public TpAppInformation() {
    }

    public TpAppInformation(
           org.csapi.www.cs.schema.TpAppInformationType switchName,
           java.lang.String timestamp) {
           this.switchName = switchName;
           this.timestamp = timestamp;
    }


    /**
     * Gets the switchName value for this TpAppInformation.
     * 
     * @return switchName
     */
    public org.csapi.www.cs.schema.TpAppInformationType getSwitchName() {
        return switchName;
    }


    /**
     * Sets the switchName value for this TpAppInformation.
     * 
     * @param switchName
     */
    public void setSwitchName(org.csapi.www.cs.schema.TpAppInformationType switchName) {
        this.switchName = switchName;
    }


    /**
     * Gets the timestamp value for this TpAppInformation.
     * 
     * @return timestamp
     */
    public java.lang.String getTimestamp() {
        return timestamp;
    }


    /**
     * Sets the timestamp value for this TpAppInformation.
     * 
     * @param timestamp
     */
    public void setTimestamp(java.lang.String timestamp) {
        this.timestamp = timestamp;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpAppInformation)) return false;
        TpAppInformation other = (TpAppInformation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.switchName==null && other.getSwitchName()==null) || 
             (this.switchName!=null &&
              this.switchName.equals(other.getSwitchName()))) &&
            ((this.timestamp==null && other.getTimestamp()==null) || 
             (this.timestamp!=null &&
              this.timestamp.equals(other.getTimestamp())));
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
        if (getSwitchName() != null) {
            _hashCode += getSwitchName().hashCode();
        }
        if (getTimestamp() != null) {
            _hashCode += getTimestamp().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
