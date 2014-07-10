/**
 * TpChargingParameter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpChargingParameter  implements java.io.Serializable {
    private int parameterID;
    private org.csapi.www.cs.schema.TpChargingParameterValue parameterValue;

    public TpChargingParameter() {
    }

    public TpChargingParameter(
           int parameterID,
           org.csapi.www.cs.schema.TpChargingParameterValue parameterValue) {
           this.parameterID = parameterID;
           this.parameterValue = parameterValue;
    }


    /**
     * Gets the parameterID value for this TpChargingParameter.
     * 
     * @return parameterID
     */
    public int getParameterID() {
        return parameterID;
    }


    /**
     * Sets the parameterID value for this TpChargingParameter.
     * 
     * @param parameterID
     */
    public void setParameterID(int parameterID) {
        this.parameterID = parameterID;
    }


    /**
     * Gets the parameterValue value for this TpChargingParameter.
     * 
     * @return parameterValue
     */
    public org.csapi.www.cs.schema.TpChargingParameterValue getParameterValue() {
        return parameterValue;
    }


    /**
     * Sets the parameterValue value for this TpChargingParameter.
     * 
     * @param parameterValue
     */
    public void setParameterValue(org.csapi.www.cs.schema.TpChargingParameterValue parameterValue) {
        this.parameterValue = parameterValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpChargingParameter)) return false;
        TpChargingParameter other = (TpChargingParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.parameterID == other.getParameterID() &&
            ((this.parameterValue==null && other.getParameterValue()==null) || 
             (this.parameterValue!=null &&
              this.parameterValue.equals(other.getParameterValue())));
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
        _hashCode += getParameterID();
        if (getParameterValue() != null) {
            _hashCode += getParameterValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
