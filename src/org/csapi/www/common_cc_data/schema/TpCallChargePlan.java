/**
 * TpCallChargePlan.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallChargePlan  implements java.io.Serializable {
    private org.csapi.www.common_cc_data.schema.TpCallChargeOrderCategory chargeOrderType;
    private org.csapi.www.osa.schema.TpOctetSet transparentCharge;
    private int chargePlan;
    private org.csapi.www.osa.schema.TpOctetSet additionalInfo;
    private org.csapi.www.common_cc_data.schema.TpCallPartyToChargeType partyToCharge;
    private org.csapi.www.common_cc_data.schema.TpCallPartyToChargeAdditionalInfo partyToChargeAdditionalInfo;

    public TpCallChargePlan() {
    }

    public TpCallChargePlan(
           org.csapi.www.common_cc_data.schema.TpCallChargeOrderCategory chargeOrderType,
           org.csapi.www.osa.schema.TpOctetSet transparentCharge,
           int chargePlan,
           org.csapi.www.osa.schema.TpOctetSet additionalInfo,
           org.csapi.www.common_cc_data.schema.TpCallPartyToChargeType partyToCharge,
           org.csapi.www.common_cc_data.schema.TpCallPartyToChargeAdditionalInfo partyToChargeAdditionalInfo) {
           this.chargeOrderType = chargeOrderType;
           this.transparentCharge = transparentCharge;
           this.chargePlan = chargePlan;
           this.additionalInfo = additionalInfo;
           this.partyToCharge = partyToCharge;
           this.partyToChargeAdditionalInfo = partyToChargeAdditionalInfo;
    }


    /**
     * Gets the chargeOrderType value for this TpCallChargePlan.
     * 
     * @return chargeOrderType
     */
    public org.csapi.www.common_cc_data.schema.TpCallChargeOrderCategory getChargeOrderType() {
        return chargeOrderType;
    }


    /**
     * Sets the chargeOrderType value for this TpCallChargePlan.
     * 
     * @param chargeOrderType
     */
    public void setChargeOrderType(org.csapi.www.common_cc_data.schema.TpCallChargeOrderCategory chargeOrderType) {
        this.chargeOrderType = chargeOrderType;
    }


    /**
     * Gets the transparentCharge value for this TpCallChargePlan.
     * 
     * @return transparentCharge
     */
    public org.csapi.www.osa.schema.TpOctetSet getTransparentCharge() {
        return transparentCharge;
    }


    /**
     * Sets the transparentCharge value for this TpCallChargePlan.
     * 
     * @param transparentCharge
     */
    public void setTransparentCharge(org.csapi.www.osa.schema.TpOctetSet transparentCharge) {
        this.transparentCharge = transparentCharge;
    }


    /**
     * Gets the chargePlan value for this TpCallChargePlan.
     * 
     * @return chargePlan
     */
    public int getChargePlan() {
        return chargePlan;
    }


    /**
     * Sets the chargePlan value for this TpCallChargePlan.
     * 
     * @param chargePlan
     */
    public void setChargePlan(int chargePlan) {
        this.chargePlan = chargePlan;
    }


    /**
     * Gets the additionalInfo value for this TpCallChargePlan.
     * 
     * @return additionalInfo
     */
    public org.csapi.www.osa.schema.TpOctetSet getAdditionalInfo() {
        return additionalInfo;
    }


    /**
     * Sets the additionalInfo value for this TpCallChargePlan.
     * 
     * @param additionalInfo
     */
    public void setAdditionalInfo(org.csapi.www.osa.schema.TpOctetSet additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


    /**
     * Gets the partyToCharge value for this TpCallChargePlan.
     * 
     * @return partyToCharge
     */
    public org.csapi.www.common_cc_data.schema.TpCallPartyToChargeType getPartyToCharge() {
        return partyToCharge;
    }


    /**
     * Sets the partyToCharge value for this TpCallChargePlan.
     * 
     * @param partyToCharge
     */
    public void setPartyToCharge(org.csapi.www.common_cc_data.schema.TpCallPartyToChargeType partyToCharge) {
        this.partyToCharge = partyToCharge;
    }


    /**
     * Gets the partyToChargeAdditionalInfo value for this TpCallChargePlan.
     * 
     * @return partyToChargeAdditionalInfo
     */
    public org.csapi.www.common_cc_data.schema.TpCallPartyToChargeAdditionalInfo getPartyToChargeAdditionalInfo() {
        return partyToChargeAdditionalInfo;
    }


    /**
     * Sets the partyToChargeAdditionalInfo value for this TpCallChargePlan.
     * 
     * @param partyToChargeAdditionalInfo
     */
    public void setPartyToChargeAdditionalInfo(org.csapi.www.common_cc_data.schema.TpCallPartyToChargeAdditionalInfo partyToChargeAdditionalInfo) {
        this.partyToChargeAdditionalInfo = partyToChargeAdditionalInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallChargePlan)) return false;
        TpCallChargePlan other = (TpCallChargePlan) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.chargeOrderType==null && other.getChargeOrderType()==null) || 
             (this.chargeOrderType!=null &&
              this.chargeOrderType.equals(other.getChargeOrderType()))) &&
            ((this.transparentCharge==null && other.getTransparentCharge()==null) || 
             (this.transparentCharge!=null &&
              this.transparentCharge.equals(other.getTransparentCharge()))) &&
            this.chargePlan == other.getChargePlan() &&
            ((this.additionalInfo==null && other.getAdditionalInfo()==null) || 
             (this.additionalInfo!=null &&
              this.additionalInfo.equals(other.getAdditionalInfo()))) &&
            ((this.partyToCharge==null && other.getPartyToCharge()==null) || 
             (this.partyToCharge!=null &&
              this.partyToCharge.equals(other.getPartyToCharge()))) &&
            ((this.partyToChargeAdditionalInfo==null && other.getPartyToChargeAdditionalInfo()==null) || 
             (this.partyToChargeAdditionalInfo!=null &&
              this.partyToChargeAdditionalInfo.equals(other.getPartyToChargeAdditionalInfo())));
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
        if (getChargeOrderType() != null) {
            _hashCode += getChargeOrderType().hashCode();
        }
        if (getTransparentCharge() != null) {
            _hashCode += getTransparentCharge().hashCode();
        }
        _hashCode += getChargePlan();
        if (getAdditionalInfo() != null) {
            _hashCode += getAdditionalInfo().hashCode();
        }
        if (getPartyToCharge() != null) {
            _hashCode += getPartyToCharge().hashCode();
        }
        if (getPartyToChargeAdditionalInfo() != null) {
            _hashCode += getPartyToChargeAdditionalInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
