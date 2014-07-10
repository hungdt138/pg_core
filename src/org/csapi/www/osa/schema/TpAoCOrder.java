/**
 * TpAoCOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpAoCOrder  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpCallAoCOrderCategory switchName;
    private org.csapi.www.osa.schema.TpChargeAdviceInfo chargeAdviceInfo;
    private org.csapi.www.osa.schema.TpChargePerTime chargePerTime;
    private java.lang.String networkCharge;

    public TpAoCOrder() {
    }

    public TpAoCOrder(
           org.csapi.www.osa.schema.TpCallAoCOrderCategory switchName,
           org.csapi.www.osa.schema.TpChargeAdviceInfo chargeAdviceInfo,
           org.csapi.www.osa.schema.TpChargePerTime chargePerTime,
           java.lang.String networkCharge) {
           this.switchName = switchName;
           this.chargeAdviceInfo = chargeAdviceInfo;
           this.chargePerTime = chargePerTime;
           this.networkCharge = networkCharge;
    }


    /**
     * Gets the switchName value for this TpAoCOrder.
     * 
     * @return switchName
     */
    public org.csapi.www.osa.schema.TpCallAoCOrderCategory getSwitchName() {
        return switchName;
    }


    /**
     * Sets the switchName value for this TpAoCOrder.
     * 
     * @param switchName
     */
    public void setSwitchName(org.csapi.www.osa.schema.TpCallAoCOrderCategory switchName) {
        this.switchName = switchName;
    }


    /**
     * Gets the chargeAdviceInfo value for this TpAoCOrder.
     * 
     * @return chargeAdviceInfo
     */
    public org.csapi.www.osa.schema.TpChargeAdviceInfo getChargeAdviceInfo() {
        return chargeAdviceInfo;
    }


    /**
     * Sets the chargeAdviceInfo value for this TpAoCOrder.
     * 
     * @param chargeAdviceInfo
     */
    public void setChargeAdviceInfo(org.csapi.www.osa.schema.TpChargeAdviceInfo chargeAdviceInfo) {
        this.chargeAdviceInfo = chargeAdviceInfo;
    }


    /**
     * Gets the chargePerTime value for this TpAoCOrder.
     * 
     * @return chargePerTime
     */
    public org.csapi.www.osa.schema.TpChargePerTime getChargePerTime() {
        return chargePerTime;
    }


    /**
     * Sets the chargePerTime value for this TpAoCOrder.
     * 
     * @param chargePerTime
     */
    public void setChargePerTime(org.csapi.www.osa.schema.TpChargePerTime chargePerTime) {
        this.chargePerTime = chargePerTime;
    }


    /**
     * Gets the networkCharge value for this TpAoCOrder.
     * 
     * @return networkCharge
     */
    public java.lang.String getNetworkCharge() {
        return networkCharge;
    }


    /**
     * Sets the networkCharge value for this TpAoCOrder.
     * 
     * @param networkCharge
     */
    public void setNetworkCharge(java.lang.String networkCharge) {
        this.networkCharge = networkCharge;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpAoCOrder)) return false;
        TpAoCOrder other = (TpAoCOrder) obj;
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
            ((this.chargeAdviceInfo==null && other.getChargeAdviceInfo()==null) || 
             (this.chargeAdviceInfo!=null &&
              this.chargeAdviceInfo.equals(other.getChargeAdviceInfo()))) &&
            ((this.chargePerTime==null && other.getChargePerTime()==null) || 
             (this.chargePerTime!=null &&
              this.chargePerTime.equals(other.getChargePerTime()))) &&
            ((this.networkCharge==null && other.getNetworkCharge()==null) || 
             (this.networkCharge!=null &&
              this.networkCharge.equals(other.getNetworkCharge())));
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
        if (getChargeAdviceInfo() != null) {
            _hashCode += getChargeAdviceInfo().hashCode();
        }
        if (getChargePerTime() != null) {
            _hashCode += getChargePerTime().hashCode();
        }
        if (getNetworkCharge() != null) {
            _hashCode += getNetworkCharge().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
