/**
 * TpCallLoadControlMechanism.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallLoadControlMechanism  implements java.io.Serializable {
    private org.csapi.www.common_cc_data.schema.TpCallLoadControlMechanismType switchName;
    private java.lang.Integer callLoadControlPerInterval;

    public TpCallLoadControlMechanism() {
    }

    public TpCallLoadControlMechanism(
           org.csapi.www.common_cc_data.schema.TpCallLoadControlMechanismType switchName,
           java.lang.Integer callLoadControlPerInterval) {
           this.switchName = switchName;
           this.callLoadControlPerInterval = callLoadControlPerInterval;
    }


    /**
     * Gets the switchName value for this TpCallLoadControlMechanism.
     * 
     * @return switchName
     */
    public org.csapi.www.common_cc_data.schema.TpCallLoadControlMechanismType getSwitchName() {
        return switchName;
    }


    /**
     * Sets the switchName value for this TpCallLoadControlMechanism.
     * 
     * @param switchName
     */
    public void setSwitchName(org.csapi.www.common_cc_data.schema.TpCallLoadControlMechanismType switchName) {
        this.switchName = switchName;
    }


    /**
     * Gets the callLoadControlPerInterval value for this TpCallLoadControlMechanism.
     * 
     * @return callLoadControlPerInterval
     */
    public java.lang.Integer getCallLoadControlPerInterval() {
        return callLoadControlPerInterval;
    }


    /**
     * Sets the callLoadControlPerInterval value for this TpCallLoadControlMechanism.
     * 
     * @param callLoadControlPerInterval
     */
    public void setCallLoadControlPerInterval(java.lang.Integer callLoadControlPerInterval) {
        this.callLoadControlPerInterval = callLoadControlPerInterval;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallLoadControlMechanism)) return false;
        TpCallLoadControlMechanism other = (TpCallLoadControlMechanism) obj;
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
            ((this.callLoadControlPerInterval==null && other.getCallLoadControlPerInterval()==null) || 
             (this.callLoadControlPerInterval!=null &&
              this.callLoadControlPerInterval.equals(other.getCallLoadControlPerInterval())));
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
        if (getCallLoadControlPerInterval() != null) {
            _hashCode += getCallLoadControlPerInterval().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
