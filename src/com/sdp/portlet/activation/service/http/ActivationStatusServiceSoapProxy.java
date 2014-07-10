package com.sdp.portlet.activation.service.http;

public class ActivationStatusServiceSoapProxy implements com.sdp.portlet.activation.service.http.ActivationStatusServiceSoap {
  private String _endpoint = null;
  private com.sdp.portlet.activation.service.http.ActivationStatusServiceSoap activationStatusServiceSoap = null;
  
  public ActivationStatusServiceSoapProxy() {
    _initActivationStatusServiceSoapProxy();
  }
  
  public ActivationStatusServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initActivationStatusServiceSoapProxy();
  }
  
  private void _initActivationStatusServiceSoapProxy() {
    try {
      activationStatusServiceSoap = (new com.sdp.portlet.activation.service.http.ActivationStatusServiceSoapServiceLocator()).getPortlet_Activation_ActivationStatusService();
      if (activationStatusServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)activationStatusServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)activationStatusServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (activationStatusServiceSoap != null)
      ((javax.xml.rpc.Stub)activationStatusServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.sdp.portlet.activation.service.http.ActivationStatusServiceSoap getActivationStatusServiceSoap() {
    if (activationStatusServiceSoap == null)
      _initActivationStatusServiceSoapProxy();
    return activationStatusServiceSoap;
  }
  
  public com.sdp.portlet.activation.model.ActivationStatusSoap[] checkAllStatus(java.lang.String sourceAddress) throws java.rmi.RemoteException{
    if (activationStatusServiceSoap == null)
      _initActivationStatusServiceSoapProxy();
    return activationStatusServiceSoap.checkAllStatus(sourceAddress);
  }
  
  public com.sdp.portlet.activation.model.ActivationStatusSoap checkStatus(java.lang.String sourceAddress, long sku) throws java.rmi.RemoteException{
    if (activationStatusServiceSoap == null)
      _initActivationStatusServiceSoapProxy();
    return activationStatusServiceSoap.checkStatus(sourceAddress, sku);
  }
  
  public com.sdp.portlet.activation.model.ActivationStatusSoap provisioning(java.lang.String sourceAddress, java.lang.String sku, int commandId) throws java.rmi.RemoteException{
    if (activationStatusServiceSoap == null)
      _initActivationStatusServiceSoapProxy();
    return activationStatusServiceSoap.provisioning(sourceAddress, sku, commandId);
  }
  
  public com.sdp.portlet.activation.model.ActivationStatusSoap provisioning(java.lang.String sourceAddress, java.lang.String destinationAddress, java.lang.String productId, java.lang.String keyword, java.lang.String parameters) throws java.rmi.RemoteException{
    if (activationStatusServiceSoap == null)
      _initActivationStatusServiceSoapProxy();
    return activationStatusServiceSoap.provisioning(sourceAddress, destinationAddress, productId, keyword, parameters);
  }
  
  
}