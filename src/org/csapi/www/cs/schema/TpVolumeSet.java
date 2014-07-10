/**
 * TpVolumeSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpVolumeSet  implements java.io.Serializable {
    private org.csapi.www.cs.schema.TpVolume[] tpVolumeSet;

    public TpVolumeSet() {
    }

    public TpVolumeSet(
           org.csapi.www.cs.schema.TpVolume[] tpVolumeSet) {
           this.tpVolumeSet = tpVolumeSet;
    }


    /**
     * Gets the tpVolumeSet value for this TpVolumeSet.
     * 
     * @return tpVolumeSet
     */
    public org.csapi.www.cs.schema.TpVolume[] getTpVolumeSet() {
        return tpVolumeSet;
    }


    /**
     * Sets the tpVolumeSet value for this TpVolumeSet.
     * 
     * @param tpVolumeSet
     */
    public void setTpVolumeSet(org.csapi.www.cs.schema.TpVolume[] tpVolumeSet) {
        this.tpVolumeSet = tpVolumeSet;
    }

    public org.csapi.www.cs.schema.TpVolume getTpVolumeSet(int i) {
        return this.tpVolumeSet[i];
    }

    public void setTpVolumeSet(int i, org.csapi.www.cs.schema.TpVolume _value) {
        this.tpVolumeSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpVolumeSet)) return false;
        TpVolumeSet other = (TpVolumeSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpVolumeSet==null && other.getTpVolumeSet()==null) || 
             (this.tpVolumeSet!=null &&
              java.util.Arrays.equals(this.tpVolumeSet, other.getTpVolumeSet())));
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
        if (getTpVolumeSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpVolumeSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpVolumeSet(), i);
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
