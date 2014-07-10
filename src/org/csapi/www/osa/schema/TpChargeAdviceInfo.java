/**
 * TpChargeAdviceInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpChargeAdviceInfo  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpCAIElements nextCAI;
    private org.csapi.www.osa.schema.TpCAIElements currentCAI;

    public TpChargeAdviceInfo() {
    }

    public TpChargeAdviceInfo(
           org.csapi.www.osa.schema.TpCAIElements nextCAI,
           org.csapi.www.osa.schema.TpCAIElements currentCAI) {
           this.nextCAI = nextCAI;
           this.currentCAI = currentCAI;
    }


    /**
     * Gets the nextCAI value for this TpChargeAdviceInfo.
     * 
     * @return nextCAI
     */
    public org.csapi.www.osa.schema.TpCAIElements getNextCAI() {
        return nextCAI;
    }


    /**
     * Sets the nextCAI value for this TpChargeAdviceInfo.
     * 
     * @param nextCAI
     */
    public void setNextCAI(org.csapi.www.osa.schema.TpCAIElements nextCAI) {
        this.nextCAI = nextCAI;
    }


    /**
     * Gets the currentCAI value for this TpChargeAdviceInfo.
     * 
     * @return currentCAI
     */
    public org.csapi.www.osa.schema.TpCAIElements getCurrentCAI() {
        return currentCAI;
    }


    /**
     * Sets the currentCAI value for this TpChargeAdviceInfo.
     * 
     * @param currentCAI
     */
    public void setCurrentCAI(org.csapi.www.osa.schema.TpCAIElements currentCAI) {
        this.currentCAI = currentCAI;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpChargeAdviceInfo)) return false;
        TpChargeAdviceInfo other = (TpChargeAdviceInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nextCAI==null && other.getNextCAI()==null) || 
             (this.nextCAI!=null &&
              this.nextCAI.equals(other.getNextCAI()))) &&
            ((this.currentCAI==null && other.getCurrentCAI()==null) || 
             (this.currentCAI!=null &&
              this.currentCAI.equals(other.getCurrentCAI())));
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
        if (getNextCAI() != null) {
            _hashCode += getNextCAI().hashCode();
        }
        if (getCurrentCAI() != null) {
            _hashCode += getCurrentCAI().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
