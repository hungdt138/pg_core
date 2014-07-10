/**
 * TpAddress.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpAddress  implements java.io.Serializable {
    private org.csapi.www.osa.schema.TpAddressPlan plan;
    private java.lang.String addrString;
    private java.lang.String name;
    private org.csapi.www.osa.schema.TpAddressPresentation presentation;
    private org.csapi.www.osa.schema.TpAddressScreening screening;
    private java.lang.String subAddressString;

    public TpAddress() {
    }

    public TpAddress(
           org.csapi.www.osa.schema.TpAddressPlan plan,
           java.lang.String addrString,
           java.lang.String name,
           org.csapi.www.osa.schema.TpAddressPresentation presentation,
           org.csapi.www.osa.schema.TpAddressScreening screening,
           java.lang.String subAddressString) {
           this.plan = plan;
           this.addrString = addrString;
           this.name = name;
           this.presentation = presentation;
           this.screening = screening;
           this.subAddressString = subAddressString;
    }


    /**
     * Gets the plan value for this TpAddress.
     *
     * @return plan
     */
    public org.csapi.www.osa.schema.TpAddressPlan getPlan() {
        return plan;
    }


    /**
     * Sets the plan value for this TpAddress.
     *
     * @param plan
     */
    public void setPlan(org.csapi.www.osa.schema.TpAddressPlan plan) {
        this.plan = plan;
    }


    /**
     * Gets the addrString value for this TpAddress.
     *
     * @return addrString
     */
    public java.lang.String getAddrString() {
        return addrString;
    }


    /**
     * Sets the addrString value for this TpAddress.
     *
     * @param addrString
     */
    public void setAddrString(java.lang.String addrString) {
        this.addrString = addrString;
    }


    /**
     * Gets the name value for this TpAddress.
     *
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this TpAddress.
     *
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the presentation value for this TpAddress.
     *
     * @return presentation
     */
    public org.csapi.www.osa.schema.TpAddressPresentation getPresentation() {
        return presentation;
    }


    /**
     * Sets the presentation value for this TpAddress.
     *
     * @param presentation
     */
    public void setPresentation(org.csapi.www.osa.schema.TpAddressPresentation presentation) {
        this.presentation = presentation;
    }


    /**
     * Gets the screening value for this TpAddress.
     *
     * @return screening
     */
    public org.csapi.www.osa.schema.TpAddressScreening getScreening() {
        return screening;
    }


    /**
     * Sets the screening value for this TpAddress.
     *
     * @param screening
     */
    public void setScreening(org.csapi.www.osa.schema.TpAddressScreening screening) {
        this.screening = screening;
    }


    /**
     * Gets the subAddressString value for this TpAddress.
     *
     * @return subAddressString
     */
    public java.lang.String getSubAddressString() {
        return subAddressString;
    }


    /**
     * Sets the subAddressString value for this TpAddress.
     *
     * @param subAddressString
     */
    public void setSubAddressString(java.lang.String subAddressString) {
        this.subAddressString = subAddressString;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpAddress)) return false;
        TpAddress other = (TpAddress) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.plan==null && other.getPlan()==null) ||
             (this.plan!=null &&
              this.plan.equals(other.getPlan()))) &&
            ((this.addrString==null && other.getAddrString()==null) ||
             (this.addrString!=null &&
              this.addrString.equals(other.getAddrString()))) &&
            ((this.name==null && other.getName()==null) ||
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.presentation==null && other.getPresentation()==null) ||
             (this.presentation!=null &&
              this.presentation.equals(other.getPresentation()))) &&
            ((this.screening==null && other.getScreening()==null) ||
             (this.screening!=null &&
              this.screening.equals(other.getScreening()))) &&
            ((this.subAddressString==null && other.getSubAddressString()==null) ||
             (this.subAddressString!=null &&
              this.subAddressString.equals(other.getSubAddressString())));
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
        if (getPlan() != null) {
            _hashCode += getPlan().hashCode();
        }
        if (getAddrString() != null) {
            _hashCode += getAddrString().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getPresentation() != null) {
            _hashCode += getPresentation().hashCode();
        }
        if (getScreening() != null) {
            _hashCode += getScreening().hashCode();
        }
        if (getSubAddressString() != null) {
            _hashCode += getSubAddressString().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
