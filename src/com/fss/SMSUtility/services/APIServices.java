/**
 * APIServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.SMSUtility.services;

public interface APIServices extends java.rmi.Remote {
    public void main(java.lang.String[] argvs) throws java.rmi.RemoteException;
    public com.fss.SMSUtility.LoginOutput getTokenNow(com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException;
    public com.fss.SMSUtility.BasicOutput verifySTK(com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.SMSUtility.BasicOutput regSubInfo(com.fss.oom3.service.RegInfoRequest req, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.nms1.service.MsisdnStatus queryMsisdnStatus(java.lang.String msisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.nms1.service.DspItem[] queryNnFromDSP(java.lang.String fromIsdn, java.lang.String toIsdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public boolean reserveNnFromDSP(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.nms1.service.ReServationStatus[] queryReservationStatus(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public boolean changeCos4Pre(java.lang.String msisdn, java.lang.String option, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public boolean changeCos4PreCustom(java.lang.String msisdn, java.lang.String cos, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public boolean changeCOS4POS(java.lang.String msisdn, java.lang.String option, java.lang.String package_type, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.nms1.service.DspItem[] queryNiceNumber(java.lang.String toIsdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public boolean reserveNumber(java.lang.String subMsisdn, java.lang.String reserveMsisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.SMSUtility.PrepaidSubInfo querySubInfoPre(java.lang.String msisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.oom3.util.SQLObject.SQLObject reportCommission(java.lang.String keyword, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
}
