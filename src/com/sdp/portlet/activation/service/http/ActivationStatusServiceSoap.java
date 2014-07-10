/**
 * ActivationStatusServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sdp.portlet.activation.service.http;

public interface ActivationStatusServiceSoap extends java.rmi.Remote {
    public com.sdp.portlet.activation.model.ActivationStatusSoap[] checkAllStatus(java.lang.String sourceAddress) throws java.rmi.RemoteException;
    public com.sdp.portlet.activation.model.ActivationStatusSoap checkStatus(java.lang.String sourceAddress, long sku) throws java.rmi.RemoteException;
    public com.sdp.portlet.activation.model.ActivationStatusSoap provisioning(java.lang.String sourceAddress, java.lang.String sku, int commandId) throws java.rmi.RemoteException;
    public com.sdp.portlet.activation.model.ActivationStatusSoap provisioning(java.lang.String sourceAddress, java.lang.String destinationAddress, java.lang.String productId, java.lang.String keyword, java.lang.String parameters) throws java.rmi.RemoteException;
}
