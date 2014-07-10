/**
 * SoapApiImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unified.txtback.soap;

public class SoapApiImplServiceLocator extends org.apache.axis.client.Service implements com.unified.txtback.soap.SoapApiImplService {

    public SoapApiImplServiceLocator() {
    }


    public SoapApiImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SoapApiImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SoapApiImplPort
    private java.lang.String SoapApiImplPort_address = "http://10.8.39.48:8080/soap-api/service";

    public java.lang.String getSoapApiImplPortAddress() {
        return SoapApiImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SoapApiImplPortWSDDServiceName = "SoapApiImplPort";

    public java.lang.String getSoapApiImplPortWSDDServiceName() {
        return SoapApiImplPortWSDDServiceName;
    }

    public void setSoapApiImplPortWSDDServiceName(java.lang.String name) {
        SoapApiImplPortWSDDServiceName = name;
    }

    public com.unified.txtback.soap.SoapApi getSoapApiImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SoapApiImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSoapApiImplPort(endpoint);
    }

    public com.unified.txtback.soap.SoapApi getSoapApiImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.unified.txtback.soap.SoapApiImplServiceSoapBindingStub _stub = new com.unified.txtback.soap.SoapApiImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getSoapApiImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSoapApiImplPortEndpointAddress(java.lang.String address) {
        SoapApiImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.unified.txtback.soap.SoapApi.class.isAssignableFrom(serviceEndpointInterface)) {
                com.unified.txtback.soap.SoapApiImplServiceSoapBindingStub _stub = new com.unified.txtback.soap.SoapApiImplServiceSoapBindingStub(new java.net.URL(SoapApiImplPort_address), this);
                _stub.setPortName(getSoapApiImplPortWSDDServiceName());
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
        if ("SoapApiImplPort".equals(inputPortName)) {
            return getSoapApiImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://soap.txtback.unified.com/", "SoapApiImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://soap.txtback.unified.com/", "SoapApiImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SoapApiImplPort".equals(portName)) {
            setSoapApiImplPortEndpointAddress(address);
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
