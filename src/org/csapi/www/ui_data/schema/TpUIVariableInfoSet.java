/**
 * TpUIVariableInfoSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIVariableInfoSet  implements java.io.Serializable {
    private org.csapi.www.ui_data.schema.TpUIVariableInfo[] tpUIVariableInfoSet;

    public TpUIVariableInfoSet() {
    }

    public TpUIVariableInfoSet(
           org.csapi.www.ui_data.schema.TpUIVariableInfo[] tpUIVariableInfoSet) {
           this.tpUIVariableInfoSet = tpUIVariableInfoSet;
    }


    /**
     * Gets the tpUIVariableInfoSet value for this TpUIVariableInfoSet.
     * 
     * @return tpUIVariableInfoSet
     */
    public org.csapi.www.ui_data.schema.TpUIVariableInfo[] getTpUIVariableInfoSet() {
        return tpUIVariableInfoSet;
    }


    /**
     * Sets the tpUIVariableInfoSet value for this TpUIVariableInfoSet.
     * 
     * @param tpUIVariableInfoSet
     */
    public void setTpUIVariableInfoSet(org.csapi.www.ui_data.schema.TpUIVariableInfo[] tpUIVariableInfoSet) {
        this.tpUIVariableInfoSet = tpUIVariableInfoSet;
    }

    public org.csapi.www.ui_data.schema.TpUIVariableInfo getTpUIVariableInfoSet(int i) {
        return this.tpUIVariableInfoSet[i];
    }

    public void setTpUIVariableInfoSet(int i, org.csapi.www.ui_data.schema.TpUIVariableInfo _value) {
        this.tpUIVariableInfoSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUIVariableInfoSet)) return false;
        TpUIVariableInfoSet other = (TpUIVariableInfoSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpUIVariableInfoSet==null && other.getTpUIVariableInfoSet()==null) || 
             (this.tpUIVariableInfoSet!=null &&
              java.util.Arrays.equals(this.tpUIVariableInfoSet, other.getTpUIVariableInfoSet())));
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
        if (getTpUIVariableInfoSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpUIVariableInfoSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpUIVariableInfoSet(), i);
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
