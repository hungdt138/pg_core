package com.unified.txtback.soap;

public class SoapApiProxy implements com.unified.txtback.soap.SoapApi {
  private String _endpoint = null;
  private com.unified.txtback.soap.SoapApi soapApi = null;
  
  public SoapApiProxy() {
    _initSoapApiProxy();
  }
  
  public SoapApiProxy(String endpoint) {
    _endpoint = endpoint;
    _initSoapApiProxy();
  }
  
  private void _initSoapApiProxy() {
    try {
      soapApi = (new com.unified.txtback.soap.SoapApiImplServiceLocator()).getSoapApiImplPort();
      if (soapApi != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)soapApi)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)soapApi)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (soapApi != null)
      ((javax.xml.rpc.Stub)soapApi)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.unified.txtback.soap.SoapApi getSoapApi() {
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi;
  }
  
  public com.unified.txtback.soap.ResponseHeader subscribe(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String imsi, int subscriberType, int packageType, java.lang.String statusCode, java.lang.String errorCode, java.lang.String remarks) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.subscribe(requestHeader, imsi, subscriberType, packageType, statusCode, errorCode, remarks);
  }
  
  public com.unified.txtback.soap.ResponseHeader terminate(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String remarks) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.terminate(requestHeader, remarks);
  }
  
  public com.unified.txtback.soap.ResponseHeader renewal(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String statusCode, java.lang.String errorCode, java.lang.String chargingType, int renewalDays, java.lang.String remarks) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.renewal(requestHeader, statusCode, errorCode, chargingType, renewalDays, remarks);
  }
  
  public com.unified.txtback.soap.ResponseHeader clearSchedule(com.unified.txtback.soap.RequestHeader requestHeader, int dayPart) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.clearSchedule(requestHeader, dayPart);
  }
  
  public com.unified.txtback.soap.ResponseHeader setPrePackage(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String prePackageCode) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.setPrePackage(requestHeader, prePackageCode);
  }
  
  public com.unified.txtback.soap.ResponseHeader showProfileMessage(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String profileCode) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.showProfileMessage(requestHeader, profileCode);
  }
  
  public com.unified.txtback.soap.ResponseHeader setProfileMessage(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String profileCode, java.lang.String profileMessage) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.setProfileMessage(requestHeader, profileCode, profileMessage);
  }
  
  public com.unified.txtback.soap.ResponseHeader setProfileSchedule(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String profileCode, int dayPart) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.setProfileSchedule(requestHeader, profileCode, dayPart);
  }
  
  public com.unified.txtback.soap.ResponseHeader setProfile(com.unified.txtback.soap.RequestHeader requestHeader, java.lang.String profileCode) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.setProfile(requestHeader, profileCode);
  }
  
  public com.unified.txtback.soap.ResponseHeader getSchedule(com.unified.txtback.soap.RequestHeader requestHeader, int dayPart) throws java.rmi.RemoteException{
    if (soapApi == null)
      _initSoapApiProxy();
    return soapApi.getSchedule(requestHeader, dayPart);
  }
  
  
}