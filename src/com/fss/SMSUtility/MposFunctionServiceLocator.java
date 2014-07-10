/**
 * MposFunctionServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.SMSUtility;

public class MposFunctionServiceLocator extends org.apache.axis.client.Service implements com.fss.SMSUtility.MposFunctionService {

    public MposFunctionServiceLocator() {
    }


    public MposFunctionServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MposFunctionServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MposFunction
    private java.lang.String MposFunction_address = "http://10.8.13.61:7865/eload/services/MposFunction";

    public java.lang.String getMposFunctionAddress() {
        return MposFunction_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MposFunctionWSDDServiceName = "MposFunction";

    public java.lang.String getMposFunctionWSDDServiceName() {
        return MposFunctionWSDDServiceName;
    }

    public void setMposFunctionWSDDServiceName(java.lang.String name) {
        MposFunctionWSDDServiceName = name;
    }

    public com.fss.SMSUtility.MposFunction getMposFunction() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MposFunction_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMposFunction(endpoint);
    }

    public com.fss.SMSUtility.MposFunction getMposFunction(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.fss.SMSUtility.MposFunctionSoapBindingStub _stub = new com.fss.SMSUtility.MposFunctionSoapBindingStub(portAddress, this);
            _stub.setPortName(getMposFunctionWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMposFunctionEndpointAddress(java.lang.String address) {
        MposFunction_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.fss.SMSUtility.MposFunction.class.isAssignableFrom(serviceEndpointInterface)) {
                com.fss.SMSUtility.MposFunctionSoapBindingStub _stub = new com.fss.SMSUtility.MposFunctionSoapBindingStub(new java.net.URL(MposFunction_address), this);
                _stub.setPortName(getMposFunctionWSDDServiceName());
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
        if ("MposFunction".equals(inputPortName)) {
            return getMposFunction();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://SMSUtility.fss.com", "MposFunctionService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://SMSUtility.fss.com", "MposFunction"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MposFunction".equals(portName)) {
            setMposFunctionEndpointAddress(address);
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
