/**
 * MposFunction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fss.SMSUtility;

public interface MposFunction extends java.rmi.Remote {
    public com.fss.SMSUtility.LoginOutput login(com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException;
    public com.fss.SMSUtility.BasicOutput logout(java.lang.String sessionId) throws java.rmi.RemoteException;
    public com.fss.SMSUtility.BasicOutput verifySTK(com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.SMSUtility.BasicOutput regSubInfo(com.fss.oom3.service.RegInfoRequest req, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.nms1.service.MsisdnStatus queryMsisdnStatus(java.lang.String msisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.nms1.service.DspItem[] queryNnFromDSP(java.lang.String fromIsdn, java.lang.String toIsdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public boolean reserveNnFromDSP(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.nms1.service.ReServationStatus[] queryReservationStatus(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public boolean changeCos4Pre(java.lang.String msisdn, java.lang.String option, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public boolean changeCOS4POS(java.lang.String msisdn, java.lang.String option, java.lang.String package_type, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.nms1.service.DspItem[] queryNiceNumber(java.lang.String toIsdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public boolean reserveNumber(java.lang.String msisdn, java.lang.String pid, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
    public com.fss.SMSUtility.PrepaidSubInfo querySubInfoPre(java.lang.String msisdn, com.fss.SMSUtility.BasicInput in) throws java.rmi.RemoteException, com.fss.util.AppException;
}
