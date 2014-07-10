/**
 * TpChargingParameterSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpChargingParameterSet  implements java.io.Serializable {
    private org.csapi.www.cs.schema.TpChargingParameter[] tpChargingParameterSet;

    public TpChargingParameterSet() {
    }

    public TpChargingParameterSet(
           org.csapi.www.cs.schema.TpChargingParameter[] tpChargingParameterSet) {
           this.tpChargingParameterSet = tpChargingParameterSet;
    }


    /**
     * Gets the tpChargingParameterSet value for this TpChargingParameterSet.
     *
     * @return tpChargingParameterSet
     */
    public org.csapi.www.cs.schema.TpChargingParameter[] getTpChargingParameterSet() {
        return tpChargingParameterSet;
    }


    /**
     * Sets the tpChargingParameterSet value for this TpChargingParameterSet.
     *
     * @param tpChargingParameterSet
     */
    public void setTpChargingParameterSet(org.csapi.www.cs.schema.TpChargingParameter[] tpChargingParameterSet) {
        this.tpChargingParameterSet = tpChargingParameterSet;
    }

    public org.csapi.www.cs.schema.TpChargingParameter getTpChargingParameterSet(int i) {
        return this.tpChargingParameterSet[i];
    }

    public void setTpChargingParameterSet(int i, org.csapi.www.cs.schema.TpChargingParameter _value) {
        this.tpChargingParameterSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpChargingParameterSet)) return false;
        TpChargingParameterSet other = (TpChargingParameterSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.tpChargingParameterSet==null && other.getTpChargingParameterSet()==null) ||
             (this.tpChargingParameterSet!=null &&
              java.util.Arrays.equals(this.tpChargingParameterSet, other.getTpChargingParameterSet())));
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
        if (getTpChargingParameterSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpChargingParameterSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpChargingParameterSet(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
