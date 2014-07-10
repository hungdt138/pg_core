/**
 * TpCallServiceCodeSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.common_cc_data.schema;

public class TpCallServiceCodeSet  implements java.io.Serializable {
    private org.csapi.www.common_cc_data.schema.TpCallServiceCode[] tpCallServiceCodeSet;

    public TpCallServiceCodeSet() {
    }

    public TpCallServiceCodeSet(
           org.csapi.www.common_cc_data.schema.TpCallServiceCode[] tpCallServiceCodeSet) {
           this.tpCallServiceCodeSet = tpCallServiceCodeSet;
    }


    /**
     * Gets the tpCallServiceCodeSet value for this TpCallServiceCodeSet.
     * 
     * @return tpCallServiceCodeSet
     */
    public org.csapi.www.common_cc_data.schema.TpCallServiceCode[] getTpCallServiceCodeSet() {
        return tpCallServiceCodeSet;
    }


    /**
     * Sets the tpCallServiceCodeSet value for this TpCallServiceCodeSet.
     * 
     * @param tpCallServiceCodeSet
     */
    public void setTpCallServiceCodeSet(org.csapi.www.common_cc_data.schema.TpCallServiceCode[] tpCallServiceCodeSet) {
        this.tpCallServiceCodeSet = tpCallServiceCodeSet;
    }

    public org.csapi.www.common_cc_data.schema.TpCallServiceCode getTpCallServiceCodeSet(int i) {
        return this.tpCallServiceCodeSet[i];
    }

    public void setTpCallServiceCodeSet(int i, org.csapi.www.common_cc_data.schema.TpCallServiceCode _value) {
        this.tpCallServiceCodeSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCallServiceCodeSet)) return false;
        TpCallServiceCodeSet other = (TpCallServiceCodeSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpCallServiceCodeSet==null && other.getTpCallServiceCodeSet()==null) || 
             (this.tpCallServiceCodeSet!=null &&
              java.util.Arrays.equals(this.tpCallServiceCodeSet, other.getTpCallServiceCodeSet())));
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
        if (getTpCallServiceCodeSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpCallServiceCodeSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpCallServiceCodeSet(), i);
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
