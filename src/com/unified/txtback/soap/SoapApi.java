/**
 * SoapApi.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unified.txtback.soap;

public interface SoapApi extends java.rmi.Remote {
    public com.unified.txtback.soap.ResponseHeader subscribe(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String imsi, int subscriberType, int packageType, java.lang.String statusCode, java.lang.String errorCode, java.lang.String remarks) throws java.rmi.RemoteException;
    public com.unified.txtback.soap.ResponseHeader terminate(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String remarks) throws java.rmi.RemoteException;
    public com.unified.txtback.soap.ResponseHeader renewal(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String statusCode, java.lang.String errorCode, java.lang.String chargingType, int renewalDays, java.lang.String remarks) throws java.rmi.RemoteException;
    public com.unified.txtback.soap.ResponseHeader clearSchedule(com.unified.txtback.soap.RequestHeader requestHeader, int dayPart) throws java.rmi.RemoteException;
    public com.unified.txtback.soap.ResponseHeader setPrePackage(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String prePackageCode) throws java.rmi.RemoteException;
    public com.unified.txtback.soap.ResponseHeader showProfileMessage(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String profileCode) throws java.rmi.RemoteException;
    public com.unified.txtback.soap.ResponseHeader setProfileMessage(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String profileCode, java.lang.String profileMessage) throws java.rmi.RemoteException;
    public com.unified.txtback.soap.ResponseHeader setProfileSchedule(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String profileCode, int dayPart) throws java.rmi.RemoteException;
    public com.unified.txtback.soap.ResponseHeader setProfile(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String profileCode) throws java.rmi.RemoteException;
    public com.unified.txtback.soap.ResponseHeader getSchedule(com.unified.txtback.soap.RequestHeader requestHeader, int dayPart) throws java.rmi.RemoteException;
}
