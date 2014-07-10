/**
 * TpCallTreatment.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallTreatment  implements java.io.Serializable {
    private org.csapi.www.common_cc_data.schema.TpCallAdditionalTreatmentInfo additionalTreatmentInfo;
    private org.csapi.www.common_cc_data.schema.TpCallTreatmentType callTreatmentType;
    private org.csapi.www.common_cc_data.schema.TpReleaseCause releaseCause;

    public TpCallTreatment() {
    }

    public TpCallTreatment(
           org.csapi.www.common_cc_data.schema.TpCallAdditionalTreatmentInfo additionalTreatmentInfo,
           org.csapi.www.common_cc_data.schema.TpCallTreatmentType callTreatmentType,
           org.csapi.www.common_cc_data.schema.TpReleaseCause releaseCause) {
           this.additionalTreatmentInfo = additionalTreatmentInfo;
           this.callTreatmentType = callTreatmentType;
           this.releaseCause = releaseCause;
    }


    /**
     * Gets the additionalTreatmentInfo value for this TpCallTreatment.
     * 
     * @return additionalTreatmentInfo
     */
    public org.csapi.www.common_cc_data.schema.TpCallAdditionalTreatmentInfo getAdditionalTreatmentInfo() {
        return additionalTreatmentInfo;
    }


    /**
     * Sets the additionalTreatmentInfo value for this TpCallTreatment.
     * 
     * @param additionalTreatmentInfo
     */
    public void setAdditionalTreatmentInfo(org.csapi.www.common_cc_data.schema.TpCallAdditionalTreatmentInfo additionalTreatmentInfo) {
        this.additionalTreatmentInfo = additionalTreatmentInfo;
    }


    /**
     * Gets the callTreatmentType value for this TpCallTreatment.
     * 
     * @return callTreatmentType
     */
    public org.csapi.www.common_cc_data.schema.TpCallTreatmentType getCallTreatmentType() {
        return callTreatmentType;
    }


    /**
     * Sets the callTreatmentType value for this TpCallTreatment.
     * 
     * @param callTreatmentType
     */
    public void setCallTreatmentType(org.csapi.www.common_cc_data.schema.TpCallTreatmentType callTreatmentType) {
        this.callTreatmentType = callTreatmentType;
    }


    /**
     * Gets the releaseCause value for this TpCallTreatment.
     * 
     * @return releaseCause
     */
    public org.csapi.www.common_cc_data.schema.TpReleaseCause getReleaseCause() {
        return releaseCause;
    }


    /**
     * Sets the releaseCause value for this TpCallTreatment.
     * 
     * @param releaseCause
     */
    public void setReleaseCause(org.csapi.www.common_cc_data.schema.TpReleaseCause releaseCause) {
        this.releaseCause = releaseCause;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallTreatment)) return false;
        TpCallTreatment other = (TpCallTreatment) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.additionalTreatmentInfo==null && other.getAdditionalTreatmentInfo()==null) || 
             (this.additionalTreatmentInfo!=null &&
              this.additionalTreatmentInfo.equals(other.getAdditionalTreatmentInfo()))) &&
            ((this.callTreatmentType==null && other.getCallTreatmentType()==null) || 
             (this.callTreatmentType!=null &&
              this.callTreatmentType.equals(other.getCallTreatmentType()))) &&
            ((this.releaseCause==null && other.getReleaseCause()==null) || 
             (this.releaseCause!=null &&
              this.releaseCause.equals(other.getReleaseCause())));
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
        if (getAdditionalTreatmentInfo() != null) {
            _hashCode += getAdditionalTreatmentInfo().hashCode();
        }
        if (getCallTreatmentType() != null) {
            _hashCode += getCallTreatmentType().hashCode();
        }
        if (getReleaseCause() != null) {
            _hashCode += getReleaseCause().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
