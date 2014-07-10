/**
 * OsaLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.wsdl;

public class OsaLocator extends org.apache.axis.client.Service implements org.csapi.www.osa.wsdl.Osa {

    public OsaLocator() {
    }


    public OsaLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OsaLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IpService
    private java.lang.String IpService_address = "http://www.somecompany.com/someservice/";

    public java.lang.String getIpServiceAddress() {
        return IpService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IpServiceWSDDServiceName = "IpService";

    public java.lang.String getIpServiceWSDDServiceName() {
        return IpServiceWSDDServiceName;
    }

    public void setIpServiceWSDDServiceName(java.lang.String name) {
        IpServiceWSDDServiceName = name;
    }

    public org.csapi.www.osa.wsdl.IpService getIpService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IpService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIpService(endpoint);
    }

    public org.csapi.www.osa.wsdl.IpService getIpService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.csapi.www.osa.wsdl.IpServiceBindingStub _stub = new org.csapi.www.osa.wsdl.IpServiceBindingStub(portAddress, this);
            _stub.setPortName(getIpServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIpServiceEndpointAddress(java.lang.String address) {
        IpService_address = address;
    }


    // Use to get a proxy class for IpInterface
    private java.lang.String IpInterface_address = "http://www.somecompany.com/someservice/";

    public java.lang.String getIpInterfaceAddress() {
        return IpInterface_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IpInterfaceWSDDServiceName = "IpInterface";

    public java.lang.String getIpInterfaceWSDDServiceName() {
        return IpInterfaceWSDDServiceName;
    }

    public void setIpInterfaceWSDDServiceName(java.lang.String name) {
        IpInterfaceWSDDServiceName = name;
    }

    public org.csapi.www.osa.wsdl.IpInterface getIpInterface() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IpInterface_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIpInterface(endpoint);
    }

    public org.csapi.www.osa.wsdl.IpInterface getIpInterface(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.csapi.www.osa.wsdl.IpInterfaceBindingStub _stub = new org.csapi.www.osa.wsdl.IpInterfaceBindingStub(portAddress, this);
            _stub.setPortName(getIpInterfaceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIpInterfaceEndpointAddress(java.lang.String address) {
        IpInterface_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.csapi.www.osa.wsdl.IpService.class.isAssignableFrom(serviceEndpointInterface)) {
                org.csapi.www.osa.wsdl.IpServiceBindingStub _stub = new org.csapi.www.osa.wsdl.IpServiceBindingStub(new java.net.URL(IpService_address), this);
                _stub.setPortName(getIpServiceWSDDServiceName());
                return _stub;
            }
            if (org.csapi.www.osa.wsdl.IpInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                org.csapi.www.osa.wsdl.IpInterfaceBindingStub _stub = new org.csapi.www.osa.wsdl.IpInterfaceBindingStub(new java.net.URL(IpInterface_address), this);
                _stub.setPortName(getIpInterfaceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("IpService".equals(inputPortName)) {
            return getIpService();
        }
        else if ("IpInterface".equals(inputPortName)) {
            return getIpInterface();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "osa");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "IpService"));
            ports.add(new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "IpInterface"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IpService".equals(portName)) {
            setIpServiceEndpointAddress(address);
        }
        else 
if ("IpInterface".equals(portName)) {
            setIpInterfaceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
