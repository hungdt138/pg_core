/**
 * TpUICollectCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUICollectCriteria  implements java.io.Serializable {
    private int minLength;
    private int maxLength;
    private java.lang.String endSequence;
    private int startTimeout;
    private int interCharTimeout;

    public TpUICollectCriteria() {
    }

    public TpUICollectCriteria(
           int minLength,
           int maxLength,
           java.lang.String endSequence,
           int startTimeout,
           int interCharTimeout) {
           this.minLength = minLength;
           this.maxLength = maxLength;
           this.endSequence = endSequence;
           this.startTimeout = startTimeout;
           this.interCharTimeout = interCharTimeout;
    }


    /**
     * Gets the minLength value for this TpUICollectCriteria.
     * 
     * @return minLength
     */
    public int getMinLength() {
        return minLength;
    }


    /**
     * Sets the minLength value for this TpUICollectCriteria.
     * 
     * @param minLength
     */
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }


    /**
     * Gets the maxLength value for this TpUICollectCriteria.
     * 
     * @return maxLength
     */
    public int getMaxLength() {
        return maxLength;
    }


    /**
     * Sets the maxLength value for this TpUICollectCriteria.
     * 
     * @param maxLength
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }


    /**
     * Gets the endSequence value for this TpUICollectCriteria.
     * 
     * @return endSequence
     */
    public java.lang.String getEndSequence() {
        return endSequence;
    }


    /**
     * Sets the endSequence value for this TpUICollectCriteria.
     * 
     * @param endSequence
     */
    public void setEndSequence(java.lang.String endSequence) {
        this.endSequence = endSequence;
    }


    /**
     * Gets the startTimeout value for this TpUICollectCriteria.
     * 
     * @return startTimeout
     */
    public int getStartTimeout() {
        return startTimeout;
    }


    /**
     * Sets the startTimeout value for this TpUICollectCriteria.
     * 
     * @param startTimeout
     */
    public void setStartTimeout(int startTimeout) {
        this.startTimeout = startTimeout;
    }


    /**
     * Gets the interCharTimeout value for this TpUICollectCriteria.
     * 
     * @return interCharTimeout
     */
    public int getInterCharTimeout() {
        return interCharTimeout;
    }


    /**
     * Sets the interCharTimeout value for this TpUICollectCriteria.
     * 
     * @param interCharTimeout
     */
    public void setInterCharTimeout(int interCharTimeout) {
        this.interCharTimeout = interCharTimeout;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUICollectCriteria)) return false;
        TpUICollectCriteria other = (TpUICollectCriteria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.minLength == other.getMinLength() &&
            this.maxLength == other.getMaxLength() &&
            ((this.endSequence==null && other.getEndSequence()==null) || 
             (this.endSequence!=null &&
              this.endSequence.equals(other.getEndSequence()))) &&
            this.startTimeout == other.getStartTimeout() &&
            this.interCharTimeout == other.getInterCharTimeout();
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
        _hashCode += getMinLength();
        _hashCode += getMaxLength();
        if (getEndSequence() != null) {
            _hashCode += getEndSequence().hashCode();
        }
        _hashCode += getStartTimeout();
        _hashCode += getInterCharTimeout();
        __hashCodeCalc = false;
        return _hashCode;
    }

}
