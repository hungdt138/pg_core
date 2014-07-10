/**
 * P_INVALID_INTERFACE_TYPE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.wsdl;

public class P_INVALID_INTERFACE_TYPE extends org.apache.axis.AxisFault {
    public java.lang.String extraInformation;
    public java.lang.String getExtraInformation() {
        return this.extraInformation;
    }

    public P_INVALID_INTERFACE_TYPE() {
    }

    public P_INVALID_INTERFACE_TYPE(java.lang.Exception target) {
        super(target);
    }

    public P_INVALID_INTERFACE_TYPE(java.lang.String message, java.lang.Throwable t) {
        super(message, t);
    }

      public P_INVALID_INTERFACE_TYPE(java.lang.String extraInformation) {
        this.extraInformation = extraInformation;
    }

    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, extraInformation);
    }
}
