/**
 * TpCommonExceptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.wsdl;

public class TpCommonExceptions extends org.apache.axis.AxisFault {
    public int exceptionType;
    public int getExceptionType() {
        return this.exceptionType;
    }
    public java.lang.String extraInformation;
    public java.lang.String getExtraInformation() {
        return this.extraInformation;
    }

    public TpCommonExceptions() {
    }

    public TpCommonExceptions(java.lang.Exception target) {
        super(target);
    }

    public TpCommonExceptions(java.lang.String message, java.lang.Throwable t) {
        super(message, t);
    }

      public TpCommonExceptions(int exceptionType, java.lang.String extraInformation) {
        this.exceptionType = exceptionType;
        this.extraInformation = extraInformation;
    }

    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, new java.lang.Integer(exceptionType));
        context.serialize(qname, null, extraInformation);
    }
}
