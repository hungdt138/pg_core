/**
 * Osa.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.wsdl;

public interface Osa extends javax.xml.rpc.Service {
    public java.lang.String getIpServiceAddress();

    public org.csapi.www.osa.wsdl.IpService getIpService() throws javax.xml.rpc.ServiceException;

    public org.csapi.www.osa.wsdl.IpService getIpService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getIpInterfaceAddress();

    public org.csapi.www.osa.wsdl.IpInterface getIpInterface() throws javax.xml.rpc.ServiceException;

    public org.csapi.www.osa.wsdl.IpInterface getIpInterface(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
