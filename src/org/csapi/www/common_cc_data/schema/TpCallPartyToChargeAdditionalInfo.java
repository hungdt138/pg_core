/**
 * TpCallPartyToChargeAdditionalInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallPartyToChargeAdditionalInfo  implements java.io.Serializable {
    private org.csapi.www.common_cc_data.schema.TpCallPartyToChargeType switchName;
    private org.csapi.www.osa.schema.TpAddress callPartySpecial;

    public TpCallPartyToChargeAdditionalInfo() {
    }

    public TpCallPartyToChargeAdditionalInfo(
           org.csapi.www.common_cc_data.schema.TpCallPartyToChargeType switchName,
           org.csapi.www.osa.schema.TpAddress callPartySpecial) {
           this.switchName = switchName;
           this.callPartySpecial = callPartySpecial;
    }


    /**
     * Gets the switchName value for this TpCallPartyToChargeAdditionalInfo.
     * 
     * @return switchName
     */
    public org.csapi.www.common_cc_data.schema.TpCallPartyToChargeType getSwitchName() {
        return switchName;
    }


    /**
     * Sets the switchName value for this TpCallPartyToChargeAdditionalInfo.
     * 
     * @param switchName
     */
    public void setSwitchName(org.csapi.www.common_cc_data.schema.TpCallPartyToChargeType switchName) {
        this.switchName = switchName;
    }


    /**
     * Gets the callPartySpecial value for this TpCallPartyToChargeAdditionalInfo.
     * 
     * @return callPartySpecial
     */
    public org.csapi.www.osa.schema.TpAddress getCallPartySpecial() {
        return callPartySpecial;
    }


    /**
     * Sets the callPartySpecial value for this TpCallPartyToChargeAdditionalInfo.
     * 
     * @param callPartySpecial
     */
    public void setCallPartySpecial(org.csapi.www.osa.schema.TpAddress callPartySpecial) {
        this.callPartySpecial = callPartySpecial;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallPartyToChargeAdditionalInfo)) return false;
        TpCallPartyToChargeAdditionalInfo other = (TpCallPartyToChargeAdditionalInfo) obj;
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
            ((this.callPartySpecial==null && other.getCallPartySpecial()==null) || 
             (this.callPartySpecial!=null &&
              this.callPartySpecial.equals(other.getCallPartySpecial())));
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
        if (getCallPartySpecial() != null) {
            _hashCode += getCallPartySpecial().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
