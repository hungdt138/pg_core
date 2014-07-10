/**
 * TpCallAdditionalTreatmentInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallAdditionalTreatmentInfo  implements java.io.Serializable {
    private org.csapi.www.common_cc_data.schema.TpCallTreatmentType switchName;
    private org.csapi.www.ui_data.schema.TpUIInfo informationToSend;

    public TpCallAdditionalTreatmentInfo() {
    }

    public TpCallAdditionalTreatmentInfo(
           org.csapi.www.common_cc_data.schema.TpCallTreatmentType switchName,
           org.csapi.www.ui_data.schema.TpUIInfo informationToSend) {
           this.switchName = switchName;
           this.informationToSend = informationToSend;
    }


    /**
     * Gets the switchName value for this TpCallAdditionalTreatmentInfo.
     * 
     * @return switchName
     */
    public org.csapi.www.common_cc_data.schema.TpCallTreatmentType getSwitchName() {
        return switchName;
    }


    /**
     * Sets the switchName value for this TpCallAdditionalTreatmentInfo.
     * 
     * @param switchName
     */
    public void setSwitchName(org.csapi.www.common_cc_data.schema.TpCallTreatmentType switchName) {
        this.switchName = switchName;
    }


    /**
     * Gets the informationToSend value for this TpCallAdditionalTreatmentInfo.
     * 
     * @return informationToSend
     */
    public org.csapi.www.ui_data.schema.TpUIInfo getInformationToSend() {
        return informationToSend;
    }


    /**
     * Sets the informationToSend value for this TpCallAdditionalTreatmentInfo.
     * 
     * @param informationToSend
     */
    public void setInformationToSend(org.csapi.www.ui_data.schema.TpUIInfo informationToSend) {
        this.informationToSend = informationToSend;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallAdditionalTreatmentInfo)) return false;
        TpCallAdditionalTreatmentInfo other = (TpCallAdditionalTreatmentInfo) obj;
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
            ((this.informationToSend==null && other.getInformationToSend()==null) || 
             (this.informationToSend!=null &&
              this.informationToSend.equals(other.getInformationToSend())));
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
        if (getInformationToSend() != null) {
            _hashCode += getInformationToSend().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
