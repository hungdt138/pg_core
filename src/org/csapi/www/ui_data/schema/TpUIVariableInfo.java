/**
 * TpUIVariableInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIVariableInfo  implements java.io.Serializable {
    private org.csapi.www.ui_data.schema.TpUIVariablePartType switchName;
    private java.lang.Integer variablePartInteger;
    private java.lang.String variablePartAddress;
    private java.lang.String variablePartTime;
    private java.lang.String variablePartDate;
    private java.lang.String variablePartPrice;

    public TpUIVariableInfo() {
    }

    public TpUIVariableInfo(
           org.csapi.www.ui_data.schema.TpUIVariablePartType switchName,
           java.lang.Integer variablePartInteger,
           java.lang.String variablePartAddress,
           java.lang.String variablePartTime,
           java.lang.String variablePartDate,
           java.lang.String variablePartPrice) {
           this.switchName = switchName;
           this.variablePartInteger = variablePartInteger;
           this.variablePartAddress = variablePartAddress;
           this.variablePartTime = variablePartTime;
           this.variablePartDate = variablePartDate;
           this.variablePartPrice = variablePartPrice;
    }


    /**
     * Gets the switchName value for this TpUIVariableInfo.
     * 
     * @return switchName
     */
    public org.csapi.www.ui_data.schema.TpUIVariablePartType getSwitchName() {
        return switchName;
    }


    /**
     * Sets the switchName value for this TpUIVariableInfo.
     * 
     * @param switchName
     */
    public void setSwitchName(org.csapi.www.ui_data.schema.TpUIVariablePartType switchName) {
        this.switchName = switchName;
    }


    /**
     * Gets the variablePartInteger value for this TpUIVariableInfo.
     * 
     * @return variablePartInteger
     */
    public java.lang.Integer getVariablePartInteger() {
        return variablePartInteger;
    }


    /**
     * Sets the variablePartInteger value for this TpUIVariableInfo.
     * 
     * @param variablePartInteger
     */
    public void setVariablePartInteger(java.lang.Integer variablePartInteger) {
        this.variablePartInteger = variablePartInteger;
    }


    /**
     * Gets the variablePartAddress value for this TpUIVariableInfo.
     * 
     * @return variablePartAddress
     */
    public java.lang.String getVariablePartAddress() {
        return variablePartAddress;
    }


    /**
     * Sets the variablePartAddress value for this TpUIVariableInfo.
     * 
     * @param variablePartAddress
     */
    public void setVariablePartAddress(java.lang.String variablePartAddress) {
        this.variablePartAddress = variablePartAddress;
    }


    /**
     * Gets the variablePartTime value for this TpUIVariableInfo.
     * 
     * @return variablePartTime
     */
    public java.lang.String getVariablePartTime() {
        return variablePartTime;
    }


    /**
     * Sets the variablePartTime value for this TpUIVariableInfo.
     * 
     * @param variablePartTime
     */
    public void setVariablePartTime(java.lang.String variablePartTime) {
        this.variablePartTime = variablePartTime;
    }


    /**
     * Gets the variablePartDate value for this TpUIVariableInfo.
     * 
     * @return variablePartDate
     */
    public java.lang.String getVariablePartDate() {
        return variablePartDate;
    }


    /**
     * Sets the variablePartDate value for this TpUIVariableInfo.
     * 
     * @param variablePartDate
     */
    public void setVariablePartDate(java.lang.String variablePartDate) {
        this.variablePartDate = variablePartDate;
    }


    /**
     * Gets the variablePartPrice value for this TpUIVariableInfo.
     * 
     * @return variablePartPrice
     */
    public java.lang.String getVariablePartPrice() {
        return variablePartPrice;
    }


    /**
     * Sets the variablePartPrice value for this TpUIVariableInfo.
     * 
     * @param variablePartPrice
     */
    public void setVariablePartPrice(java.lang.String variablePartPrice) {
        this.variablePartPrice = variablePartPrice;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUIVariableInfo)) return false;
        TpUIVariableInfo other = (TpUIVariableInfo) obj;
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
            ((this.variablePartInteger==null && other.getVariablePartInteger()==null) || 
             (this.variablePartInteger!=null &&
              this.variablePartInteger.equals(other.getVariablePartInteger()))) &&
            ((this.variablePartAddress==null && other.getVariablePartAddress()==null) || 
             (this.variablePartAddress!=null &&
              this.variablePartAddress.equals(other.getVariablePartAddress()))) &&
            ((this.variablePartTime==null && other.getVariablePartTime()==null) || 
             (this.variablePartTime!=null &&
              this.variablePartTime.equals(other.getVariablePartTime()))) &&
            ((this.variablePartDate==null && other.getVariablePartDate()==null) || 
             (this.variablePartDate!=null &&
              this.variablePartDate.equals(other.getVariablePartDate()))) &&
            ((this.variablePartPrice==null && other.getVariablePartPrice()==null) || 
             (this.variablePartPrice!=null &&
              this.variablePartPrice.equals(other.getVariablePartPrice())));
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
        if (getVariablePartInteger() != null) {
            _hashCode += getVariablePartInteger().hashCode();
        }
        if (getVariablePartAddress() != null) {
            _hashCode += getVariablePartAddress().hashCode();
        }
        if (getVariablePartTime() != null) {
            _hashCode += getVariablePartTime().hashCode();
        }
        if (getVariablePartDate() != null) {
            _hashCode += getVariablePartDate().hashCode();
        }
        if (getVariablePartPrice() != null) {
            _hashCode += getVariablePartPrice().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
