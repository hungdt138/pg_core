/**
 * TpAppInformationSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpAppInformationSet  implements java.io.Serializable {
    private org.csapi.www.cs.schema.TpAppInformation[] tpAppInformationSet;

    public TpAppInformationSet() {
    }

    public TpAppInformationSet(
           org.csapi.www.cs.schema.TpAppInformation[] tpAppInformationSet) {
           this.tpAppInformationSet = tpAppInformationSet;
    }


    /**
     * Gets the tpAppInformationSet value for this TpAppInformationSet.
     * 
     * @return tpAppInformationSet
     */
    public org.csapi.www.cs.schema.TpAppInformation[] getTpAppInformationSet() {
        return tpAppInformationSet;
    }


    /**
     * Sets the tpAppInformationSet value for this TpAppInformationSet.
     * 
     * @param tpAppInformationSet
     */
    public void setTpAppInformationSet(org.csapi.www.cs.schema.TpAppInformation[] tpAppInformationSet) {
        this.tpAppInformationSet = tpAppInformationSet;
    }

    public org.csapi.www.cs.schema.TpAppInformation getTpAppInformationSet(int i) {
        return this.tpAppInformationSet[i];
    }

    public void setTpAppInformationSet(int i, org.csapi.www.cs.schema.TpAppInformation _value) {
        this.tpAppInformationSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpAppInformationSet)) return false;
        TpAppInformationSet other = (TpAppInformationSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpAppInformationSet==null && other.getTpAppInformationSet()==null) || 
             (this.tpAppInformationSet!=null &&
              java.util.Arrays.equals(this.tpAppInformationSet, other.getTpAppInformationSet())));
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
        if (getTpAppInformationSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpAppInformationSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpAppInformationSet(), i);
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
