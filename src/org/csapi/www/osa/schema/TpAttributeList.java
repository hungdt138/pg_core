/**
 * TpAttributeList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpAttributeList  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpAttribute[] tpAttributeList;

    public TpAttributeList() {
    }

    public TpAttributeList(
           org.csapi.www.osa.schema.TpAttribute[] tpAttributeList) {
           this.tpAttributeList = tpAttributeList;
    }


    /**
     * Gets the tpAttributeList value for this TpAttributeList.
     * 
     * @return tpAttributeList
     */
    public org.csapi.www.osa.schema.TpAttribute[] getTpAttributeList() {
        return tpAttributeList;
    }


    /**
     * Sets the tpAttributeList value for this TpAttributeList.
     * 
     * @param tpAttributeList
     */
    public void setTpAttributeList(org.csapi.www.osa.schema.TpAttribute[] tpAttributeList) {
        this.tpAttributeList = tpAttributeList;
    }

    public org.csapi.www.osa.schema.TpAttribute getTpAttributeList(int i) {
        return this.tpAttributeList[i];
    }

    public void setTpAttributeList(int i, org.csapi.www.osa.schema.TpAttribute _value) {
        this.tpAttributeList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpAttributeList)) return false;
        TpAttributeList other = (TpAttributeList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tpAttributeList==null && other.getTpAttributeList()==null) || 
             (this.tpAttributeList!=null &&
              java.util.Arrays.equals(this.tpAttributeList, other.getTpAttributeList())));
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
        if (getTpAttributeList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTpAttributeList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTpAttributeList(), i);
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
