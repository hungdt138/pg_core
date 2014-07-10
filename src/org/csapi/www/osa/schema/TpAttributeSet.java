/**
 * TpAttributeSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpAttributeSet  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpAttribute[] tpAttributeSet;

    public TpAttributeSet() {
    }

    public TpAttributeSet(
           org.csapi.www.osa.schema.TpAttribute[] tpAttributeSet) {
           this.tpAttributeSet = tpAttributeSet;
    }


    /**
     * Gets the tpAttributeSet value for this TpAttributeSet.
     * 
     * @return tpAttributeSet
     */
    public org.csapi.www.osa.schema.TpAttribute[] getTpAttributeSet() {
        return tpAttributeSet;
    }


    /**
     * Sets the tpAttributeSet value for this TpAttributeSet.
     * 
     * @param tpAttributeSet
     */
    public void setTpAttributeSet(org.csapi.www.osa.schema.TpAttribute[] tpAttributeSet) {
        this.tpAttributeSet = tpAttributeSet;
    }

    public org.csapi.www.osa.schema.TpAttribute getTpAttributeSet(int i) {
        return this.tpAttributeSet[i];
    }

    public void setTpAttributeSet(int i, org.csapi.www.osa.schema.TpAttribute _value) {
        this.tpAttributeSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpAttributeSet)) return false;
        TpAttributeSet other = (TpAttributeSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpAttributeSet==null && other.getTpAttributeSet()==null) || 
             (this.tpAttributeSet!=null &&
              java.util.Arrays.equals(this.tpAttributeSet, other.getTpAttributeSet())));
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
        if (getTpAttributeSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpAttributeSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpAttributeSet(), i);
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
