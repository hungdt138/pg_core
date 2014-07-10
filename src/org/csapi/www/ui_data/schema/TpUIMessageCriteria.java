/**
 * TpUIMessageCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIMessageCriteria  implements java.io.Serializable {
    private java.lang.String endSequence;
    private int maxMessageSize;
    private int maxMessageTime;

    public TpUIMessageCriteria() {
    }

    public TpUIMessageCriteria(
           java.lang.String endSequence,
           int maxMessageSize,
           int maxMessageTime) {
           this.endSequence = endSequence;
           this.maxMessageSize = maxMessageSize;
           this.maxMessageTime = maxMessageTime;
    }


    /**
     * Gets the endSequence value for this TpUIMessageCriteria.
     * 
     * @return endSequence
     */
    public java.lang.String getEndSequence() {
        return endSequence;
    }


    /**
     * Sets the endSequence value for this TpUIMessageCriteria.
     * 
     * @param endSequence
     */
    public void setEndSequence(java.lang.String endSequence) {
        this.endSequence = endSequence;
    }


    /**
     * Gets the maxMessageSize value for this TpUIMessageCriteria.
     * 
     * @return maxMessageSize
     */
    public int getMaxMessageSize() {
        return maxMessageSize;
    }


    /**
     * Sets the maxMessageSize value for this TpUIMessageCriteria.
     * 
     * @param maxMessageSize
     */
    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }


    /**
     * Gets the maxMessageTime value for this TpUIMessageCriteria.
     * 
     * @return maxMessageTime
     */
    public int getMaxMessageTime() {
        return maxMessageTime;
    }


    /**
     * Sets the maxMessageTime value for this TpUIMessageCriteria.
     * 
     * @param maxMessageTime
     */
    public void setMaxMessageTime(int maxMessageTime) {
        this.maxMessageTime = maxMessageTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUIMessageCriteria)) return false;
        TpUIMessageCriteria other = (TpUIMessageCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.endSequence==null && other.getEndSequence()==null) || 
             (this.endSequence!=null &&
              this.endSequence.equals(other.getEndSequence()))) &&
            this.maxMessageSize == other.getMaxMessageSize() &&
            this.maxMessageTime == other.getMaxMessageTime();
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
        if (getEndSequence() != null) {
            _hashCode += getEndSequence().hashCode();
        }
        _hashCode += getMaxMessageSize();
        _hashCode += getMaxMessageTime();
        __hashCodeCalc = false;
        return _hashCode;
    }

}
