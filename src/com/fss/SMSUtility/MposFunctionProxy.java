package com.fss.SMSUtility;

public class MposFunctionProxy implements com.fss.SMSUtility.MposFunction {
  private String _endpoint = null;
  private com.fss.SMSUtility.MposFunction mposFunction = null;
  
  public MposFunctionProxy() {
    _initMposFunctionProxy();
  }
  
  public MposFunctionProxy(String endpoint) {
    _endpoint = endpoint;
    _initMposFunctionProxy();
  }
  
  private void _initMposFunctionProxy() {
    try {
      mposFunction = (new com.fss.SMSUtility.MposFunctionServiceLocator()).getMposFunction();
      if (mposFunction != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mposFunction)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mposFunction)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mposFunction != null)
      ((javax.xml.rpc.Stub)mposFunction)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.fss.SMSUtility.MposFunction getMposFunction() {
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction;
  }
  
  public com.fss.SMSUtility.LoginOutput login(com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.login(in);
  }
  
  public com.fss.SMSUtility.BasicOutput logout(java.lang.String sessionId) throws java.rmi.RemoteException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.logout(sessionId);
  }
  
  public com.fss.SMSUtility.BasicOutput verifySTK(com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.verifySTK(in);
  }
  
  public com.fss.SMSUtility.BasicOutput regSubInfo(com.fss.oom3.service.RegInfoRequest req, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.regSubInfo(req, in);
  }
  
  public com.fss.nms1.service.MsisdnStatus queryMsisdnStatus(java.lang.String msisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.queryMsisdnStatus(msisdn, in);
  }
  
  public com.fss.nms1.service.DspItem[] queryNnFromDSP(java.lang.String fromIsdn, java.lang.String toIsdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.queryNnFromDSP(fromIsdn, toIsdn, in);
  }
  
  public boolean reserveNnFromDSP(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.reserveNnFromDSP(msisdn, pid, in);
  }
  
  public com.fss.nms1.service.ReServationStatus[] queryReservationStatus(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.queryReservationStatus(msisdn, pid, in);
  }
  
  public boolean changeCos4Pre(java.lang.String msisdn, java.lang.String option, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.changeCos4Pre(msisdn, option, in);
  }
  
  public boolean changeCOS4POS(java.lang.String msisdn, java.lang.String option, java.lang.String package_type, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.changeCOS4POS(msisdn, option, package_type, in);
  }
  
  public com.fss.nms1.service.DspItem[] queryNiceNumber(java.lang.String toIsdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.queryNiceNumber(toIsdn, in);
  }
  
  public boolean reserveNumber(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.reserveNumber(msisdn, pid, in);
  }
  
  public com.fss.SMSUtility.PrepaidSubInfo querySubInfoPre(java.lang.String msisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException{
    if (mposFunction == null)
      _initMposFunctionProxy();
    return mposFunction.querySubInfoPre(msisdn, in);
  }
  
  
}