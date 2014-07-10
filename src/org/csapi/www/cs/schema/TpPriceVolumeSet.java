/**
 * TpPriceVolumeSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpPriceVolumeSet  implements java.io.Serializable {
    private org.csapi.www.cs.schema.TpPriceVolume[] tpPriceVolumeSet;

    public TpPriceVolumeSet() {
    }

    public TpPriceVolumeSet(
           org.csapi.www.cs.schema.TpPriceVolume[] tpPriceVolumeSet) {
           this.tpPriceVolumeSet = tpPriceVolumeSet;
    }


    /**
     * Gets the tpPriceVolumeSet value for this TpPriceVolumeSet.
     * 
     * @return tpPriceVolumeSet
     */
    public org.csapi.www.cs.schema.TpPriceVolume[] getTpPriceVolumeSet() {
        return tpPriceVolumeSet;
    }


    /**
     * Sets the tpPriceVolumeSet value for this TpPriceVolumeSet.
     * 
     * @param tpPriceVolumeSet
     */
    public void setTpPriceVolumeSet(org.csapi.www.cs.schema.TpPriceVolume[] tpPriceVolumeSet) {
        this.tpPriceVolumeSet = tpPriceVolumeSet;
    }

    public org.csapi.www.cs.schema.TpPriceVolume getTpPriceVolumeSet(int i) {
        return this.tpPriceVolumeSet[i];
    }

    public void setTpPriceVolumeSet(int i, org.csapi.www.cs.schema.TpPriceVolume _value) {
        this.tpPriceVolumeSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpPriceVolumeSet)) return false;
        TpPriceVolumeSet other = (TpPriceVolumeSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpPriceVolumeSet==null && other.getTpPriceVolumeSet()==null) || 
             (this.tpPriceVolumeSet!=null &&
              java.util.Arrays.equals(this.tpPriceVolumeSet, other.getTpPriceVolumeSet())));
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
        if (getTpPriceVolumeSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpPriceVolumeSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpPriceVolumeSet(), i);
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
