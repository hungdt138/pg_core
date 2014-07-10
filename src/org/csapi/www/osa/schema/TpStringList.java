/**
 * TpStringList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpStringList  implements java.io.Serializable {
    private java.lang.String[] tpStringList;

    public TpStringList() {
    }

    public TpStringList(
           java.lang.String[] tpStringList) {
           this.tpStringList = tpStringList;
    }


    /**
     * Gets the tpStringList value for this TpStringList.
     * 
     * @return tpStringList
     */
    public java.lang.String[] getTpStringList() {
        return tpStringList;
    }


    /**
     * Sets the tpStringList value for this TpStringList.
     * 
     * @param tpStringList
     */
    public void setTpStringList(java.lang.String[] tpStringList) {
        this.tpStringList = tpStringList;
    }

    public java.lang.String getTpStringList(int i) {
        return this.tpStringList[i];
    }

    public void setTpStringList(int i, java.lang.String _value) {
        this.tpStringList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpStringList)) return false;
        TpStringList other = (TpStringList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpStringList==null && other.getTpStringList()==null) || 
             (this.tpStringList!=null &&
              java.util.Arrays.equals(this.tpStringList, other.getTpStringList())));
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
        if (getTpStringList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpStringList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpStringList(), i);
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
