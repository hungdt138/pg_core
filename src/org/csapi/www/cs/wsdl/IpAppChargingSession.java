/**
 * IpAppChargingSession.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.wsdl;

public interface IpAppChargingSession extends java.rmi.Remote {
    public void creditAmountErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void creditAmountRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingPrice creditedAmount, org.csapi.www.cs.schema.TpChargingPrice reservedAmountLeft, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void creditUnitErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void creditUnitRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpVolumeSet creditedVolumes, org.csapi.www.cs.schema.TpVolumeSet reservedUnitsLeft, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void debitAmountErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void debitAmountRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingPrice debitedAmount, org.csapi.www.cs.schema.TpChargingPrice reservedAmountLeft, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void debitUnitErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void debitUnitRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpVolumeSet debitedVolumes, org.csapi.www.cs.schema.TpVolumeSet reservedUnitsLeft, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void directCreditAmountErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void directCreditAmountRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingPrice creditedAmount, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void directCreditUnitErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void directCreditUnitRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpVolumeSet creditedVolumes, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void directDebitAmountErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void directDebitAmountRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingPrice debitedAmount, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void directDebitUnitErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void directDebitUnitRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpVolumeSet debitedVolumes, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void extendLifeTimeErr(int sessionID, org.csapi.www.cs.schema.TpChargingError error) throws java.rmi.RemoteException;
    public void extendLifeTimeRes(int sessionID, int sessionTimeLeft) throws java.rmi.RemoteException;
    public void rateErr(int sessionID, org.csapi.www.cs.schema.TpChargingError error) throws java.rmi.RemoteException;
    public void rateRes(int sessionID, org.csapi.www.cs.schema.TpPriceVolumeSet rates, int validityTimeLeft) throws java.rmi.RemoteException;
    public void reserveAmountErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void reserveAmountRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingPrice reservedAmount, int sessionTimeLeft, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void reserveUnitErr(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpChargingError error, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void reserveUnitRes(int sessionID, int requestNumber, org.csapi.www.cs.schema.TpVolumeSet reservedUnits, int sessionTimeLeft, int requestNumberNextRequest) throws java.rmi.RemoteException;
    public void sessionEnded(int sessionID, org.csapi.www.cs.schema.TpSessionEndedCause report) throws java.rmi.RemoteException;
}
