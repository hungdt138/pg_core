/**
 * TpSessionIDSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpSessionIDSet  implements java.io.Serializable {
    private int[] tpSessionIDSet;

    public TpSessionIDSet() {
    }

    public TpSessionIDSet(
           int[] tpSessionIDSet) {
           this.tpSessionIDSet = tpSessionIDSet;
    }


    /**
     * Gets the tpSessionIDSet value for this TpSessionIDSet.
     * 
     * @return tpSessionIDSet
     */
    public int[] getTpSessionIDSet() {
        return tpSessionIDSet;
    }


    /**
     * Sets the tpSessionIDSet value for this TpSessionIDSet.
     * 
     * @param tpSessionIDSet
     */
    public void setTpSessionIDSet(int[] tpSessionIDSet) {
        this.tpSessionIDSet = tpSessionIDSet;
    }

    public int getTpSessionIDSet(int i) {
        return this.tpSessionIDSet[i];
    }

    public void setTpSessionIDSet(int i, int _value) {
        this.tpSessionIDSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpSessionIDSet)) return false;
        TpSessionIDSet other = (TpSessionIDSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpSessionIDSet==null && other.getTpSessionIDSet()==null) || 
             (this.tpSessionIDSet!=null &&
              java.util.Arrays.equals(this.tpSessionIDSet, other.getTpSessionIDSet())));
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
        if (getTpSessionIDSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpSessionIDSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpSessionIDSet(), i);
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
