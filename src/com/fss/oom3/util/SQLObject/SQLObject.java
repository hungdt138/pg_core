/**
 * SQLObject.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.oom3.util.SQLObject;

public class SQLObject  implements java.io.Serializable {
    private java.lang.String[][] column;

    private int columnSize;

    private java.lang.String[][] data;

    private int rowSize;

    public SQLObject() {
    }

    public SQLObject(
           java.lang.String[][] column,
           int columnSize,
           java.lang.String[][] data,
           int rowSize) {
           this.column = column;
           this.columnSize = columnSize;
           this.data = data;
           this.rowSize = rowSize;
    }


    /**
     * Gets the column value for this SQLObject.
     * 
     * @return column
     */
    public java.lang.String[][] getColumn() {
        return column;
    }


    /**
     * Sets the column value for this SQLObject.
     * 
     * @param column
     */
    public void setColumn(java.lang.String[][] column) {
        this.column = column;
    }


    /**
     * Gets the columnSize value for this SQLObject.
     * 
     * @return columnSize
     */
    public int getColumnSize() {
        return columnSize;
    }


    /**
     * Sets the columnSize value for this SQLObject.
     * 
     * @param columnSize
     */
    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }


    /**
     * Gets the data value for this SQLObject.
     * 
     * @return data
     */
    public java.lang.String[][] getData() {
        return data;
    }


    /**
     * Sets the data value for this SQLObject.
     * 
     * @param data
     */
    public void setData(java.lang.String[][] data) {
        this.data = data;
    }


    /**
     * Gets the rowSize value for this SQLObject.
     * 
     * @return rowSize
     */
    public int getRowSize() {
        return rowSize;
    }


    /**
     * Sets the rowSize value for this SQLObject.
     * 
     * @param rowSize
     */
    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SQLObject)) return false;
        SQLObject other = (SQLObject) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.column==null && other.getColumn()==null) || 
             (this.column!=null &&
              java.util.Arrays.equals(this.column, other.getColumn()))) &&
            this.columnSize == other.getColumnSize() &&
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              java.util.Arrays.equals(this.data, other.getData()))) &&
            this.rowSize == other.getRowSize();
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
        if (getColumn() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getColumn());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getColumn(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getColumnSize();
        if (getData() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getData(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getRowSize();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SQLObject.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://SQLObject.util.oom3.fss.com", "SQLObject"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("column");
        elemField.setXmlName(new javax.xml.namespace.QName("", "column"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columnSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "columnSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rowSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rowSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
