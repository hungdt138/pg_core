/**
 * IpChargingManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.wsdl;

public interface IpChargingManager extends java.rmi.Remote {
    public void setCallback(java.lang.String appInterface) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE, org.csapi.www.osa.wsdl.TpCommonExceptions;
    public void setCallbackWithSessionID(java.lang.String appInterface, int sessionID) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID;
    public org.csapi.www.cs.schema.TpChargingSessionID createChargingSession(java.lang.String appChargingSession, java.lang.String sessionDescription, org.csapi.www.cs.schema.TpMerchantAccountID merchantAccount, org.csapi.www.osa.schema.TpAddress user, org.csapi.www.cs.schema.TpCorrelationID correlationID) throws java.rmi.RemoteException, org.csapi.www.cs.wsdl.P_INVALID_ACCOUNT, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.cs.wsdl.P_INVALID_USER;
    public org.csapi.www.cs.schema.TpChargingSessionID createSplitChargingSession(java.lang.String appChargingSession, java.lang.String sessionDescription, org.csapi.www.cs.schema.TpMerchantAccountID merchantAccount, org.csapi.www.osa.schema.TpAddressSet users, org.csapi.www.cs.schema.TpCorrelationID correlationID) throws java.rmi.RemoteException, org.csapi.www.cs.wsdl.P_INVALID_ACCOUNT, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.cs.wsdl.P_INVALID_USER;
}
