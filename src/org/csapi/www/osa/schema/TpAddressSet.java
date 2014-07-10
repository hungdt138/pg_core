/**
 * TpAddressSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpAddressSet  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpAddress[] tpAddressSet;

    public TpAddressSet() {
    }

    public TpAddressSet(
           org.csapi.www.osa.schema.TpAddress[] tpAddressSet) {
           this.tpAddressSet = tpAddressSet;
    }


    /**
     * Gets the tpAddressSet value for this TpAddressSet.
     * 
     * @return tpAddressSet
     */
    public org.csapi.www.osa.schema.TpAddress[] getTpAddressSet() {
        return tpAddressSet;
    }


    /**
     * Sets the tpAddressSet value for this TpAddressSet.
     * 
     * @param tpAddressSet
     */
    public void setTpAddressSet(org.csapi.www.osa.schema.TpAddress[] tpAddressSet) {
        this.tpAddressSet = tpAddressSet;
    }

    public org.csapi.www.osa.schema.TpAddress getTpAddressSet(int i) {
        return this.tpAddressSet[i];
    }

    public void setTpAddressSet(int i, org.csapi.www.osa.schema.TpAddress _value) {
        this.tpAddressSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpAddressSet)) return false;
        TpAddressSet other = (TpAddressSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpAddressSet==null && other.getTpAddressSet()==null) || 
             (this.tpAddressSet!=null &&
              java.util.Arrays.equals(this.tpAddressSet, other.getTpAddressSet())));
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
        if (getTpAddressSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpAddressSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpAddressSet(), i);
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
