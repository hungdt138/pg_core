/**
 * TpOctetSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpOctetSet  implements java.io.Serializable {
    private byte[][] tpOctetSet;

    public TpOctetSet() {
    }

    public TpOctetSet(
           byte[][] tpOctetSet) {
           this.tpOctetSet = tpOctetSet;
    }


    /**
     * Gets the tpOctetSet value for this TpOctetSet.
     * 
     * @return tpOctetSet
     */
    public byte[][] getTpOctetSet() {
        return tpOctetSet;
    }


    /**
     * Sets the tpOctetSet value for this TpOctetSet.
     * 
     * @param tpOctetSet
     */
    public void setTpOctetSet(byte[][] tpOctetSet) {
        this.tpOctetSet = tpOctetSet;
    }

    public byte[] getTpOctetSet(int i) {
        return this.tpOctetSet[i];
    }

    public void setTpOctetSet(int i, byte[] _value) {
        this.tpOctetSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpOctetSet)) return false;
        TpOctetSet other = (TpOctetSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpOctetSet==null && other.getTpOctetSet()==null) || 
             (this.tpOctetSet!=null &&
              java.util.Arrays.equals(this.tpOctetSet, other.getTpOctetSet())));
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
        if (getTpOctetSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpOctetSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpOctetSet(), i);
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
