/**
 * TpUIEventCriteriaResultSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIEventCriteriaResultSet  implements java.io.Serializable {
    private org.csapi.www.ui_data.schema.TpUIEventCriteriaResult[] tpUIEventCriteriaResultSet;

    public TpUIEventCriteriaResultSet() {
    }

    public TpUIEventCriteriaResultSet(
           org.csapi.www.ui_data.schema.TpUIEventCriteriaResult[] tpUIEventCriteriaResultSet) {
           this.tpUIEventCriteriaResultSet = tpUIEventCriteriaResultSet;
    }


    /**
     * Gets the tpUIEventCriteriaResultSet value for this TpUIEventCriteriaResultSet.
     * 
     * @return tpUIEventCriteriaResultSet
     */
    public org.csapi.www.ui_data.schema.TpUIEventCriteriaResult[] getTpUIEventCriteriaResultSet() {
        return tpUIEventCriteriaResultSet;
    }


    /**
     * Sets the tpUIEventCriteriaResultSet value for this TpUIEventCriteriaResultSet.
     * 
     * @param tpUIEventCriteriaResultSet
     */
    public void setTpUIEventCriteriaResultSet(org.csapi.www.ui_data.schema.TpUIEventCriteriaResult[] tpUIEventCriteriaResultSet) {
        this.tpUIEventCriteriaResultSet = tpUIEventCriteriaResultSet;
    }

    public org.csapi.www.ui_data.schema.TpUIEventCriteriaResult getTpUIEventCriteriaResultSet(int i) {
        return this.tpUIEventCriteriaResultSet[i];
    }

    public void setTpUIEventCriteriaResultSet(int i, org.csapi.www.ui_data.schema.TpUIEventCriteriaResult _value) {
        this.tpUIEventCriteriaResultSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUIEventCriteriaResultSet)) return false;
        TpUIEventCriteriaResultSet other = (TpUIEventCriteriaResultSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpUIEventCriteriaResultSet==null && other.getTpUIEventCriteriaResultSet()==null) || 
             (this.tpUIEventCriteriaResultSet!=null &&
              java.util.Arrays.equals(this.tpUIEventCriteriaResultSet, other.getTpUIEventCriteriaResultSet())));
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
        if (getTpUIEventCriteriaResultSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpUIEventCriteriaResultSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpUIEventCriteriaResultSet(), i);
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
