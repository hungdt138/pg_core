/**
 * TpChargingParameterValue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpChargingParameterValue  implements java.io.Serializable {
    private org.csapi.www.cs.schema.TpChargingParameterValueType switchName;
    private java.lang.Integer intValue;
    private java.lang.Float floatValue;
    private java.lang.String stringValue;
    private java.lang.Boolean booleanValue;
    private org.csapi.www.osa.schema.TpOctetSet octetValue;

    public TpChargingParameterValue() {
    }

    public TpChargingParameterValue(
           org.csapi.www.cs.schema.TpChargingParameterValueType switchName,
           java.lang.Integer intValue,
           java.lang.Float floatValue,
           java.lang.String stringValue,
           java.lang.Boolean booleanValue,
           org.csapi.www.osa.schema.TpOctetSet octetValue) {
           this.switchName = switchName;
           this.intValue = intValue;
           this.floatValue = floatValue;
           this.stringValue = stringValue;
           this.booleanValue = booleanValue;
           this.octetValue = octetValue;
    }


    /**
     * Gets the switchName value for this TpChargingParameterValue.
     * 
     * @return switchName
     */
    public org.csapi.www.cs.schema.TpChargingParameterValueType getSwitchName() {
        return switchName;
    }


    /**
     * Sets the switchName value for this TpChargingParameterValue.
     * 
     * @param switchName
     */
    public void setSwitchName(org.csapi.www.cs.schema.TpChargingParameterValueType switchName) {
        this.switchName = switchName;
    }


    /**
     * Gets the intValue value for this TpChargingParameterValue.
     * 
     * @return intValue
     */
    public java.lang.Integer getIntValue() {
        return intValue;
    }


    /**
     * Sets the intValue value for this TpChargingParameterValue.
     * 
     * @param intValue
     */
    public void setIntValue(java.lang.Integer intValue) {
        this.intValue = intValue;
    }


    /**
     * Gets the floatValue value for this TpChargingParameterValue.
     * 
     * @return floatValue
     */
    public java.lang.Float getFloatValue() {
        return floatValue;
    }


    /**
     * Sets the floatValue value for this TpChargingParameterValue.
     * 
     * @param floatValue
     */
    public void setFloatValue(java.lang.Float floatValue) {
        this.floatValue = floatValue;
    }


    /**
     * Gets the stringValue value for this TpChargingParameterValue.
     * 
     * @return stringValue
     */
    public java.lang.String getStringValue() {
        return stringValue;
    }


    /**
     * Sets the stringValue value for this TpChargingParameterValue.
     * 
     * @param stringValue
     */
    public void setStringValue(java.lang.String stringValue) {
        this.stringValue = stringValue;
    }


    /**
     * Gets the booleanValue value for this TpChargingParameterValue.
     * 
     * @return booleanValue
     */
    public java.lang.Boolean getBooleanValue() {
        return booleanValue;
    }


    /**
     * Sets the booleanValue value for this TpChargingParameterValue.
     * 
     * @param booleanValue
     */
    public void setBooleanValue(java.lang.Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }


    /**
     * Gets the octetValue value for this TpChargingParameterValue.
     * 
     * @return octetValue
     */
    public org.csapi.www.osa.schema.TpOctetSet getOctetValue() {
        return octetValue;
    }


    /**
     * Sets the octetValue value for this TpChargingParameterValue.
     * 
     * @param octetValue
     */
    public void setOctetValue(org.csapi.www.osa.schema.TpOctetSet octetValue) {
        this.octetValue = octetValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpChargingParameterValue)) return false;
        TpChargingParameterValue other = (TpChargingParameterValue) obj;
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
            ((this.intValue==null && other.getIntValue()==null) || 
             (this.intValue!=null &&
              this.intValue.equals(other.getIntValue()))) &&
            ((this.floatValue==null && other.getFloatValue()==null) || 
             (this.floatValue!=null &&
              this.floatValue.equals(other.getFloatValue()))) &&
            ((this.stringValue==null && other.getStringValue()==null) || 
             (this.stringValue!=null &&
              this.stringValue.equals(other.getStringValue()))) &&
            ((this.booleanValue==null && other.getBooleanValue()==null) || 
             (this.booleanValue!=null &&
              this.booleanValue.equals(other.getBooleanValue()))) &&
            ((this.octetValue==null && other.getOctetValue()==null) || 
             (this.octetValue!=null &&
              this.octetValue.equals(other.getOctetValue())));
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
        if (getIntValue() != null) {
            _hashCode += getIntValue().hashCode();
        }
        if (getFloatValue() != null) {
            _hashCode += getFloatValue().hashCode();
        }
        if (getStringValue() != null) {
            _hashCode += getStringValue().hashCode();
        }
        if (getBooleanValue() != null) {
            _hashCode += getBooleanValue().hashCode();
        }
        if (getOctetValue() != null) {
            _hashCode += getOctetValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
