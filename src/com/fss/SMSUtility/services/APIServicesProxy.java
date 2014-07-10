package com.fss.SMSUtility.services;

public class APIServicesProxy implements com.fss.SMSUtility.services.APIServices {
  private String _endpoint = null;
  private com.fss.SMSUtility.services.APIServices aPIServices = null;
  
  public APIServicesProxy() {
    _initAPIServicesProxy();
  }
  
  public APIServicesProxy(String endpoint) {
    _endpoint = endpoint;
    _initAPIServicesProxy();
  }
  
  private void _initAPIServicesProxy() {
    try {
      aPIServices = (new com.fss.SMSUtility.services.APIServicesServiceLocator()).getAPI();
      if (aPIServices != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)aPIServices)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)aPIServices)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (aPIServices != null)
      ((javax.xml.rpc.Stub)aPIServices)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.fss.SMSUtility.services.APIServices getAPIServices() {
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices;
  }
  
  public void main(java.lang.String[] argvs) throws java.rmi.RemoteException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    aPIServices.main(argvs);
  }
  
  public com.fss.SMSUtility.LoginOutput getTokenNow(com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.getTokenNow(in);
  }
  
  public com.fss.SMSUtility.BasicOutput verifySTK(com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.verifySTK(in);
  }
  
  public com.fss.SMSUtility.BasicOutput regSubInfo(com.fss.oom3.service.RegInfoRequest req, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.regSubInfo(req, in);
  }
  
  public com.fss.nms1.service.MsisdnStatus queryMsisdnStatus(java.lang.String msisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.queryMsisdnStatus(msisdn, in);
  }
  
  public com.fss.nms1.service.DspItem[] queryNnFromDSP(java.lang.String fromIsdn, java.lang.String toIsdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.queryNnFromDSP(fromIsdn, toIsdn, in);
  }
  
  public boolean reserveNnFromDSP(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.reserveNnFromDSP(msisdn, pid, in);
  }
  
  public com.fss.nms1.service.ReServationStatus[] queryReservationStatus(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.queryReservationStatus(msisdn, pid, in);
  }
  
  public boolean changeCos4Pre(java.lang.String msisdn, java.lang.String option, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.changeCos4Pre(msisdn, option, in);
  }
  
  public boolean changeCos4PreCustom(java.lang.String msisdn, java.lang.String cos, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.changeCos4PreCustom(msisdn, cos, in);
  }
  
  public boolean changeCOS4POS(java.lang.String msisdn, java.lang.String option, java.lang.String package_type, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.changeCOS4POS(msisdn, option, package_type, in);
  }
  
  public com.fss.nms1.service.DspItem[] queryNiceNumber(java.lang.String toIsdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.queryNiceNumber(toIsdn, in);
  }
  
  public boolean reserveNumber(java.lang.String subMsisdn, java.lang.String reserveMsisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.reserveNumber(subMsisdn, reserveMsisdn, in);
  }
  
  public com.fss.SMSUtility.PrepaidSubInfo querySubInfoPre(java.lang.String msisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.querySubInfoPre(msisdn, in);
  }
  
  public com.fss.oom3.util.SQLObject.SQLObject reportCommission(java.lang.String keyword, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (aPIServices == null)
      _initAPIServicesProxy();
    return aPIServices.reportCommission(keyword, in);
  }
  
  
}