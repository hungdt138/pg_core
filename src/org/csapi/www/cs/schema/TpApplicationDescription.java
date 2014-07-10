/**
 * TpApplicationDescription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.schema;

public class TpApplicationDescription  implements java.io.Serializable {
    private org.csapi.www.cs.schema.TpAppInformationSet appInformation;
    private java.lang.String text;

    public TpApplicationDescription() {
    }

    public TpApplicationDescription(
           org.csapi.www.cs.schema.TpAppInformationSet appInformation,
           java.lang.String text) {
           this.appInformation = appInformation;
           this.text = text;
    }


    /**
     * Gets the appInformation value for this TpApplicationDescription.
     *
     * @return appInformation
     */
    public org.csapi.www.cs.schema.TpAppInformationSet getAppInformation() {
        return appInformation;
    }


    /**
     * Sets the appInformation value for this TpApplicationDescription.
     *
     * @param appInformation
     */
    public void setAppInformation(org.csapi.www.cs.schema.TpAppInformationSet appInformation) {
        this.appInformation = appInformation;
    }


    /**
     * Gets the text value for this TpApplicationDescription.
     *
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }


    /**
     * Sets the text value for this TpApplicationDescription.
     *
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpApplicationDescription)) return false;
        TpApplicationDescription other = (TpApplicationDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.appInformation==null && other.getAppInformation()==null) ||
             (this.appInformation!=null &&
              this.appInformation.equals(other.getAppInformation()))) &&
            ((this.text==null && other.getText()==null) ||
             (this.text!=null &&
              this.text.equals(other.getText())));
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
        if (getAppInformation() != null) {
            _hashCode += getAppInformation().hashCode();
        }
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
