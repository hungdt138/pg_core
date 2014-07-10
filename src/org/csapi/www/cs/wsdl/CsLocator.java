/**
 * CsLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.wsdl;

public class CsLocator extends org.apache.axis.client.Service implements org.csapi.www.cs.wsdl.Cs {

    public CsLocator() {
    }


    public CsLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CsLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IpAppChargingSession
    private java.lang.String IpAppChargingSession_address = "http://www.somecompany.com/someservice/";

    public java.lang.String getIpAppChargingSessionAddress() {
        return IpAppChargingSession_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IpAppChargingSessionWSDDServiceName = "IpAppChargingSession";

    public java.lang.String getIpAppChargingSessionWSDDServiceName() {
        return IpAppChargingSessionWSDDServiceName;
    }

    public void setIpAppChargingSessionWSDDServiceName(java.lang.String name) {
        IpAppChargingSessionWSDDServiceName = name;
    }

    public org.csapi.www.cs.wsdl.IpAppChargingSession getIpAppChargingSession() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IpAppChargingSession_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIpAppChargingSession(endpoint);
    }

    public org.csapi.www.cs.wsdl.IpAppChargingSession getIpAppChargingSession(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.csapi.www.cs.wsdl.IpAppChargingSessionBindingStub _stub = new org.csapi.www.cs.wsdl.IpAppChargingSessionBindingStub(portAddress, this);
            _stub.setPortName(getIpAppChargingSessionWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIpAppChargingSessionEndpointAddress(java.lang.String address) {
        IpAppChargingSession_address = address;
    }


    // Use to get a proxy class for IpChargingManager
    private java.lang.String IpChargingManager_address = "http://uw8b.comverse-in.com/osa/";

    public java.lang.String getIpChargingManagerAddress() {
        return IpChargingManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IpChargingManagerWSDDServiceName = "IpChargingManager";

    public java.lang.String getIpChargingManagerWSDDServiceName() {
        return IpChargingManagerWSDDServiceName;
    }

    public void setIpChargingManagerWSDDServiceName(java.lang.String name) {
        IpChargingManagerWSDDServiceName = name;
    }

    public org.csapi.www.cs.wsdl.IpChargingManager getIpChargingManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IpChargingManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIpChargingManager(endpoint);
    }

    public org.csapi.www.cs.wsdl.IpChargingManager getIpChargingManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.csapi.www.cs.wsdl.IpChargingManagerBindingStub _stub = new org.csapi.www.cs.wsdl.IpChargingManagerBindingStub(portAddress, this);
            _stub.setPortName(getIpChargingManagerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIpChargingManagerEndpointAddress(java.lang.String address) {
        IpChargingManager_address = address;
    }


    // Use to get a proxy class for IpChargingSession
    private java.lang.String IpChargingSession_address = "http://uw8b.comverse-in.com/osa/";

    public java.lang.String getIpChargingSessionAddress() {
        return IpChargingSession_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IpChargingSessionWSDDServiceName = "IpChargingSession";

    public java.lang.String getIpChargingSessionWSDDServiceName() {
        return IpChargingSessionWSDDServiceName;
    }

    public void setIpChargingSessionWSDDServiceName(java.lang.String name) {
        IpChargingSessionWSDDServiceName = name;
    }

    public org.csapi.www.cs.wsdl.IpChargingSession getIpChargingSession() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IpChargingSession_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIpChargingSession(endpoint);
    }

    public org.csapi.www.cs.wsdl.IpChargingSession getIpChargingSession(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.csapi.www.cs.wsdl.IpChargingSessionBindingStub _stub = new org.csapi.www.cs.wsdl.IpChargingSessionBindingStub(portAddress, this);
            _stub.setPortName(getIpChargingSessionWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIpChargingSessionEndpointAddress(java.lang.String address) {
        IpChargingSession_address = address;
    }


    // Use to get a proxy class for IpAppChargingManager
    private java.lang.String IpAppChargingManager_address = "http://www.somecompany.com/someservice/";

    public java.lang.String getIpAppChargingManagerAddress() {
        return IpAppChargingManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IpAppChargingManagerWSDDServiceName = "IpAppChargingManager";

    public java.lang.String getIpAppChargingManagerWSDDServiceName() {
        return IpAppChargingManagerWSDDServiceName;
    }

    public void setIpAppChargingManagerWSDDServiceName(java.lang.String name) {
        IpAppChargingManagerWSDDServiceName = name;
    }

    public org.csapi.www.cs.wsdl.IpAppChargingManager getIpAppChargingManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IpAppChargingManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIpAppChargingManager(endpoint);
    }

    public org.csapi.www.cs.wsdl.IpAppChargingManager getIpAppChargingManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.csapi.www.cs.wsdl.IpAppChargingManagerBindingStub _stub = new org.csapi.www.cs.wsdl.IpAppChargingManagerBindingStub(portAddress, this);
            _stub.setPortName(getIpAppChargingManagerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIpAppChargingManagerEndpointAddress(java.lang.String address) {
        IpAppChargingManager_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.csapi.www.cs.wsdl.IpAppChargingSession.class.isAssignableFrom(serviceEndpointInterface)) {
                org.csapi.www.cs.wsdl.IpAppChargingSessionBindingStub _stub = new org.csapi.www.cs.wsdl.IpAppChargingSessionBindingStub(new java.net.URL(IpAppChargingSession_address), this);
                _stub.setPortName(getIpAppChargingSessionWSDDServiceName());
                return _stub;
            }
            if (org.csapi.www.cs.wsdl.IpChargingManager.class.isAssignableFrom(serviceEndpointInterface)) {
                org.csapi.www.cs.wsdl.IpChargingManagerBindingStub _stub = new org.csapi.www.cs.wsdl.IpChargingManagerBindingStub(new java.net.URL(IpChargingManager_address), this);
                _stub.setPortName(getIpChargingManagerWSDDServiceName());
                return _stub;
            }
            if (org.csapi.www.cs.wsdl.IpChargingSession.class.isAssignableFrom(serviceEndpointInterface)) {
                org.csapi.www.cs.wsdl.IpChargingSessionBindingStub _stub = new org.csapi.www.cs.wsdl.IpChargingSessionBindingStub(new java.net.URL(IpChargingSession_address), this);
                _stub.setPortName(getIpChargingSessionWSDDServiceName());
                return _stub;
            }
            if (org.csapi.www.cs.wsdl.IpAppChargingManager.class.isAssignableFrom(serviceEndpointInterface)) {
                org.csapi.www.cs.wsdl.IpAppChargingManagerBindingStub _stub = new org.csapi.www.cs.wsdl.IpAppChargingManagerBindingStub(new java.net.URL(IpAppChargingManager_address), this);
                _stub.setPortName(getIpAppChargingManagerWSDDServiceName());
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
        if ("IpAppChargingSession".equals(inputPortName)) {
            return getIpAppChargingSession();
        }
        else if ("IpChargingManager".equals(inputPortName)) {
            return getIpChargingManager();
        }
        else if ("IpChargingSession".equals(inputPortName)) {
            return getIpChargingSession();
        }
        else if ("IpAppChargingManager".equals(inputPortName)) {
            return getIpAppChargingManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "cs");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "IpAppChargingSession"));
            ports.add(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "IpChargingManager"));
            ports.add(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "IpChargingSession"));
            ports.add(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "IpAppChargingManager"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

if ("IpAppChargingSession".equals(portName)) {
            setIpAppChargingSessionEndpointAddress(address);
        }
        else
if ("IpChargingManager".equals(portName)) {
            setIpChargingManagerEndpointAddress(address);
        }
        else
if ("IpChargingSession".equals(portName)) {
            setIpChargingSessionEndpointAddress(address);
        }
        else
if ("IpAppChargingManager".equals(portName)) {
            setIpAppChargingManagerEndpointAddress(address);
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
