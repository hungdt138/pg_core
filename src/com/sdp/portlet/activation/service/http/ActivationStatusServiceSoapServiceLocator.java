/**
 * ActivationStatusServiceSoapServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sdp.portlet.activation.service.http;

public class ActivationStatusServiceSoapServiceLocator extends org.apache.axis.client.Service implements com.sdp.portlet.activation.service.http.ActivationStatusServiceSoapService {

    public ActivationStatusServiceSoapServiceLocator() {
    }


    public ActivationStatusServiceSoapServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ActivationStatusServiceSoapServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Portlet_Activation_ActivationStatusService
    private java.lang.String Portlet_Activation_ActivationStatusService_address = "http://localhost:8081/SDP-ext/axis/Portlet_Activation_ActivationStatusService";

    public java.lang.String getPortlet_Activation_ActivationStatusServiceAddress() {
        return Portlet_Activation_ActivationStatusService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String Portlet_Activation_ActivationStatusServiceWSDDServiceName = "Portlet_Activation_ActivationStatusService";

    public java.lang.String getPortlet_Activation_ActivationStatusServiceWSDDServiceName() {
        return Portlet_Activation_ActivationStatusServiceWSDDServiceName;
    }

    public void setPortlet_Activation_ActivationStatusServiceWSDDServiceName(java.lang.String name) {
        Portlet_Activation_ActivationStatusServiceWSDDServiceName = name;
    }

    public com.sdp.portlet.activation.service.http.ActivationStatusServiceSoap getPortlet_Activation_ActivationStatusService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Portlet_Activation_ActivationStatusService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPortlet_Activation_ActivationStatusService(endpoint);
    }

    public com.sdp.portlet.activation.service.http.ActivationStatusServiceSoap getPortlet_Activation_ActivationStatusService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sdp.portlet.activation.service.http.Portlet_Activation_ActivationStatusServiceSoapBindingStub _stub = new com.sdp.portlet.activation.service.http.Portlet_Activation_ActivationStatusServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getPortlet_Activation_ActivationStatusServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPortlet_Activation_ActivationStatusServiceEndpointAddress(java.lang.String address) {
        Portlet_Activation_ActivationStatusService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sdp.portlet.activation.service.http.ActivationStatusServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sdp.portlet.activation.service.http.Portlet_Activation_ActivationStatusServiceSoapBindingStub _stub = new com.sdp.portlet.activation.service.http.Portlet_Activation_ActivationStatusServiceSoapBindingStub(new java.net.URL(Portlet_Activation_ActivationStatusService_address), this);
                _stub.setPortName(getPortlet_Activation_ActivationStatusServiceWSDDServiceName());
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
        if ("Portlet_Activation_ActivationStatusService".equals(inputPortName)) {
            return getPortlet_Activation_ActivationStatusService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:http.service.activation.portlet.sdp.com", "ActivationStatusServiceSoapService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:http.service.activation.portlet.sdp.com", "Portlet_Activation_ActivationStatusService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Portlet_Activation_ActivationStatusService".equals(portName)) {
            setPortlet_Activation_ActivationStatusServiceEndpointAddress(address);
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
