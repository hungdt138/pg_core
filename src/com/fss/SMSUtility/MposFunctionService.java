/**
 * MposFunctionService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.SMSUtility;

public interface MposFunctionService extends javax.xml.rpc.Service {
    public java.lang.String getMposFunctionAddress();

    public com.fss.SMSUtility.MposFunction getMposFunction() throws javax.xml.rpc.ServiceException;

    public com.fss.SMSUtility.MposFunction getMposFunction(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
