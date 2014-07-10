/**
 * TpStringSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpStringSet  implements java.io.Serializable {
    private java.lang.String[] tpStringSet;

    public TpStringSet() {
    }

    public TpStringSet(
           java.lang.String[] tpStringSet) {
           this.tpStringSet = tpStringSet;
    }


    /**
     * Gets the tpStringSet value for this TpStringSet.
     * 
     * @return tpStringSet
     */
    public java.lang.String[] getTpStringSet() {
        return tpStringSet;
    }


    /**
     * Sets the tpStringSet value for this TpStringSet.
     * 
     * @param tpStringSet
     */
    public void setTpStringSet(java.lang.String[] tpStringSet) {
        this.tpStringSet = tpStringSet;
    }

    public java.lang.String getTpStringSet(int i) {
        return this.tpStringSet[i];
    }

    public void setTpStringSet(int i, java.lang.String _value) {
        this.tpStringSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpStringSet)) return false;
        TpStringSet other = (TpStringSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpStringSet==null && other.getTpStringSet()==null) || 
             (this.tpStringSet!=null &&
              java.util.Arrays.equals(this.tpStringSet, other.getTpStringSet())));
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
        if (getTpStringSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpStringSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpStringSet(), i);
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
