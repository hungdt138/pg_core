/**
 * TpMerchantAccountID.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpMerchantAccountID  implements java.io.Serializable {
    private java.lang.String merchantID;
    private int accountID;

    public TpMerchantAccountID() {
    }

    public TpMerchantAccountID(
           java.lang.String merchantID,
           int accountID) {
           this.merchantID = merchantID;
           this.accountID = accountID;
    }


    /**
     * Gets the merchantID value for this TpMerchantAccountID.
     * 
     * @return merchantID
     */
    public java.lang.String getMerchantID() {
        return merchantID;
    }


    /**
     * Sets the merchantID value for this TpMerchantAccountID.
     * 
     * @param merchantID
     */
    public void setMerchantID(java.lang.String merchantID) {
        this.merchantID = merchantID;
    }


    /**
     * Gets the accountID value for this TpMerchantAccountID.
     * 
     * @return accountID
     */
    public int getAccountID() {
        return accountID;
    }


    /**
     * Sets the accountID value for this TpMerchantAccountID.
     * 
     * @param accountID
     */
    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpMerchantAccountID)) return false;
        TpMerchantAccountID other = (TpMerchantAccountID) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.merchantID==null && other.getMerchantID()==null) || 
             (this.merchantID!=null &&
              this.merchantID.equals(other.getMerchantID()))) &&
            this.accountID == other.getAccountID();
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
        if (getMerchantID() != null) {
            _hashCode += getMerchantID().hashCode();
        }
        _hashCode += getAccountID();
        __hashCodeCalc = false;
        return _hashCode;
    }

}
