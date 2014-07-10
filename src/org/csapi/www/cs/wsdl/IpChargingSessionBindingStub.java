/**
 * IpChargingSessionBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.cs.wsdl;

public class IpChargingSessionBindingStub extends org.apache.axis.client.Stub implements org.csapi.www.cs.wsdl.IpChargingSession {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[19];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setCallback");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "appInterface"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "IpInterfaceRef"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setCallbackWithSessionID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "appInterface"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "IpInterfaceRef"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("creditAmountReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "amount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingPrice"), org.csapi.www.cs.schema.TpChargingPrice.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "closeReservation"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpBoolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_AMOUNT",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_CURRENCY",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("creditUnitReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "volumes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpVolumeSet"), org.csapi.www.cs.schema.TpVolumeSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "closeReservation"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpBoolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_VOLUME",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("debitAmountReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "amount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingPrice"), org.csapi.www.cs.schema.TpChargingPrice.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "closeReservation"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpBoolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_AMOUNT",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_CURRENCY",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("debitUnitReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "volumes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpVolumeSet"), org.csapi.www.cs.schema.TpVolumeSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "closeReservation"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpBoolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_VOLUME",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("directCreditAmountReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "chargingParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterSet"), org.csapi.www.cs.schema.TpChargingParameterSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "amount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingPrice"), org.csapi.www.cs.schema.TpChargingPrice.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_AMOUNT",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_CURRENCY",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("directCreditUnitReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "chargingParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterSet"), org.csapi.www.cs.schema.TpChargingParameterSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "volumes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpVolumeSet"), org.csapi.www.cs.schema.TpVolumeSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_VOLUME",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("directDebitAmountReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "chargingParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterSet"), org.csapi.www.cs.schema.TpChargingParameterSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "amount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingPrice"), org.csapi.www.cs.schema.TpChargingPrice.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_AMOUNT",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_CURRENCY",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("directDebitUnitReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "chargingParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterSet"), org.csapi.www.cs.schema.TpChargingParameterSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "volumes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpVolumeSet"), org.csapi.www.cs.schema.TpVolumeSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_VOLUME",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("extendLifeTimeReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAmountLeft");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingPrice"));
        oper.setReturnClass(org.csapi.www.cs.schema.TpChargingPrice.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getLifeTimeLeft");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getUnitLeft");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpVolumeSet"));
        oper.setReturnClass(org.csapi.www.cs.schema.TpVolumeSet.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("rateReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "chargingParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterSet"), org.csapi.www.cs.schema.TpChargingParameterSet.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("release");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("reserveAmountReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "chargingParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterSet"), org.csapi.www.cs.schema.TpChargingParameterSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "preferredAmount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingPrice"), org.csapi.www.cs.schema.TpChargingPrice.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "minimumAmount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingPrice"), org.csapi.www.cs.schema.TpChargingPrice.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_AMOUNT",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_CURRENCY",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("reserveUnitReq");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationDescription"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription"), org.csapi.www.cs.schema.TpApplicationDescription.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "chargingParameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterSet"), org.csapi.www.cs.schema.TpChargingParameterSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "volumes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpVolumeSet"), org.csapi.www.cs.schema.TpVolumeSet.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "requestNumber"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_VOLUME",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "ExtraInformation"),
                      "org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("cmvtGetClassOfService");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExceptionType"),
                      "org.csapi.www.osa.wsdl.TpCommonExceptions",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32"),
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "ExtraInformation"),
                      "org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID",
                      new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString"),
                      false
                     ));
        _operations[18] = oper;

    }

    public IpChargingSessionBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public IpChargingSessionBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public IpChargingSessionBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.1");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        addBindings0();
        addBindings1();
    }

    private void addBindings0() {
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_AUDIO");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_INFO_RELEASE_CAUSE");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_INFO_TIMES");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_INFO_UNDEFINED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_LOAD_CONTROL_ADMIT_NO_CALLS");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_SUPERVISE_APPLY_TONE");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_SUPERVISE_CALL_ENDED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_SUPERVISE_RELEASE");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_SUPERVISE_RESPOND");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_SUPERVISE_TIMEOUT");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_SUPERVISE_TONE_APPLIED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_CALL_SUPERVISE_UI_FINISHED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_DATA");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "P_VIDEO");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallAdditionalErrorInfo");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallAdditionalErrorInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallAdditionalTreatmentInfo");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallAdditionalTreatmentInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallAlertingMechanism");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallBearerService");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallBearerService.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallChargeOrderCategory");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallChargeOrderCategory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallChargePlan");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallChargePlan.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallEndedReport");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallEndedReport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallError");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallError.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallErrorType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallErrorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallInfoReport");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallInfoReport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallInfoType");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallLoadControlIntervalRate");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallLoadControlMechanism");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallLoadControlMechanism.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallLoadControlMechanismType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallLoadControlMechanismType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallMonitorMode");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallMonitorMode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallNetworkAccessType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallNetworkAccessType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallPartyCategory");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallPartyCategory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallPartyToChargeAdditionalInfo");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallPartyToChargeAdditionalInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallPartyToChargeType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallPartyToChargeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallServiceCode");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallServiceCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallServiceCodeSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallServiceCodeSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallServiceCodeType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallServiceCodeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallSuperviseReport");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallSuperviseTreatment");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallTeleService");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallTeleService.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallTreatment");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallTreatment.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpCallTreatmentType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpCallTreatmentType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpMediaType");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/common_cc_data/schema", "TpReleaseCause");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.common_cc_data.schema.TpReleaseCause.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "IpAppChargingManagerRef");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "IpAppChargingSessionRef");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "IpChargingManagerRef");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "IpChargingSessionRef");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_CORRELATION_DATA");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_CORRELATION_MM");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_CORRELATION_UNDEFINED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_CORRELATION_VOICE");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_PARAM_CONFIRMATION_ID");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_PARAM_CONTRACT");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_PARAM_ITEM");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_PARAM_SUBTYPE");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_PARAM_UNDEFINED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_UNIT_DAYS");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_UNIT_HOURS");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_UNIT_MINUTES");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_UNIT_NUMBER");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_UNIT_OCTETS");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_UNIT_SECONDS");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "P_CHS_UNIT_UNDEFINED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpAmount");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpAmount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpAppInformation");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpAppInformation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpAppInformationSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpAppInformationSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpAppInformationType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpAppInformationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpApplicationDescription");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpApplicationDescription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingError");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpChargingError.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameter");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpChargingParameter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterID");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpChargingParameterSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterValue");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpChargingParameterValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingParameterValueType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpChargingParameterValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingPrice");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpChargingPrice.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpChargingSessionID");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpChargingSessionID.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpCorrelationID");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpCorrelationID.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpCorrelationType");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpMerchantAccountID");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpMerchantAccountID.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpPriceVolume");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpPriceVolume.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpPriceVolumeSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpPriceVolumeSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpSessionEndedCause");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpSessionEndedCause.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpUnitID");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpVolume");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpVolume.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/cs/schema", "TpVolumeSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.cs.schema.TpVolumeSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "IpInterfaceRef");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "IpServiceRef");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "ObjectRef");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "P_INVALID_STATE");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "P_METHOD_NOT_SUPPORTED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "P_NO_CALLBACK_ADDRESS_SET");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "P_RESOURCES_UNAVAILABLE");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "P_TASK_CANCELLED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "P_TASK_REFUSED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAddress");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAddress.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAddressError");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAddressError.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAddressPlan");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAddressPlan.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAddressPresentation");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAddressPresentation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAddressRange");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAddressRange.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAddressScreening");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAddressScreening.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

    }
    private void addBindings1() {
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAddressSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAddressSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAny");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAoCInfo");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAoCInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAoCOrder");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAoCOrder.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAssignmentID");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAttribute");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAttribute.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAttributeList");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAttributeList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAttributeSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpAttributeSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpAttributeType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpBoolean");
            cachedSerQNames.add(qName);
            cls = boolean.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpCAIElements");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpCAIElements.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpCallAoCOrderCategory");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpCallAoCOrderCategory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpChargeAdviceInfo");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpChargeAdviceInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpChargePerTime");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpChargePerTime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpDataSessionQosClass");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpDataSessionQosClass.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpDate");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpDateAndTime");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpDuration");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpFloat");
            cachedSerQNames.add(qName);
            cls = float.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt32");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpInt64");
            cachedSerQNames.add(qName);
            cls = double.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpLanguage");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpLongString");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpOctet");
            cachedSerQNames.add(qName);
            cls = byte[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpOctetSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpOctetSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpPrice");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionID");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpSessionIDSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpSessionIDSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpString");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpStringList");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpStringList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpStringSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpStringSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpTime");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpTimeInterval");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.osa.schema.TpTimeInterval.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpURL");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/osa/schema", "TpVersion");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "P_UI_FINAL_REQUEST");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "P_UI_LAST_ANNOUNCEMENT_IN_A_ROW");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "P_UI_RESPONSE_REQUIRED");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUICollectCriteria");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUICollectCriteria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIError");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIError.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIEventCriteria");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIEventCriteria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIEventCriteriaResult");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIEventCriteriaResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIEventCriteriaResultSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIEventCriteriaResultSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIEventInfo");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIEventInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIEventInfoDataType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIEventInfoDataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIEventNotificationInfo");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIEventNotificationInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIFault");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIInfo");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIInfoType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIInfoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIMessageCriteria");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIMessageCriteria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIReport");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIReport.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIResponseRequest");
            cachedSerQNames.add(qName);
            cls = int.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIVariableInfo");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIVariableInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIVariableInfoSet");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIVariableInfoSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.csapi.org/ui_data/schema", "TpUIVariablePartType");
            cachedSerQNames.add(qName);
            cls = org.csapi.www.ui_data.schema.TpUIVariablePartType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public void setCallback(java.lang.String appInterface) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE, org.csapi.www.osa.wsdl.TpCommonExceptions {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/osa/IpService#setCallback");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "setCallback"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {appInterface});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void setCallbackWithSessionID(java.lang.String appInterface, int sessionID) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/osa/IpService#setCallbackWithSessionID");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/osa/wsdl", "setCallbackWithSessionID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {appInterface, new java.lang.Integer(sessionID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_INTERFACE_TYPE) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void creditAmountReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpChargingPrice amount, boolean closeReservation, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.P_INVALID_AMOUNT, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID, org.csapi.www.osa.wsdl.P_INVALID_CURRENCY {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#creditAmountReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "creditAmountReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, amount, new java.lang.Boolean(closeReservation), new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void creditUnitReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpVolumeSet volumes, boolean closeReservation, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.cs.wsdl.P_INVALID_VOLUME, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#creditUnitReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "creditUnitReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, volumes, new java.lang.Boolean(closeReservation), new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_VOLUME) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_VOLUME) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void debitAmountReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpChargingPrice amount, boolean closeReservation, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.P_INVALID_AMOUNT, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID, org.csapi.www.osa.wsdl.P_INVALID_CURRENCY {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#debitAmountReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "debitAmountReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, amount, new java.lang.Boolean(closeReservation), new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void debitUnitReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpVolumeSet volumes, boolean closeReservation, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.cs.wsdl.P_INVALID_VOLUME, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#debitUnitReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "debitUnitReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, volumes, new java.lang.Boolean(closeReservation), new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_VOLUME) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_VOLUME) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void directCreditAmountReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpChargingParameterSet chargingParameters, org.csapi.www.cs.schema.TpChargingPrice amount, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.P_INVALID_AMOUNT, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID, org.csapi.www.osa.wsdl.P_INVALID_CURRENCY {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#directCreditAmountReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "directCreditAmountReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, chargingParameters, amount, new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void directCreditUnitReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpChargingParameterSet chargingParameters, org.csapi.www.cs.schema.TpVolumeSet volumes, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.cs.wsdl.P_INVALID_VOLUME, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#directCreditUnitReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "directCreditUnitReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, chargingParameters, volumes, new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_VOLUME) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_VOLUME) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void directDebitAmountReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpChargingParameterSet chargingParameters, org.csapi.www.cs.schema.TpChargingPrice amount, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.P_INVALID_AMOUNT, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID, org.csapi.www.osa.wsdl.P_INVALID_CURRENCY {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#directDebitAmountReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "directDebitAmountReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, chargingParameters, amount, new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void directDebitUnitReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpChargingParameterSet chargingParameters, org.csapi.www.cs.schema.TpVolumeSet volumes, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.cs.wsdl.P_INVALID_VOLUME, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#directDebitUnitReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "directDebitUnitReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, chargingParameters, volumes, new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_VOLUME) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_VOLUME) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void extendLifeTimeReq(int sessionID) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#extendLifeTimeReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "extendLifeTimeReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public org.csapi.www.cs.schema.TpChargingPrice getAmountLeft(int sessionID) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#getAmountLeft");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "getAmountLeft"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.csapi.www.cs.schema.TpChargingPrice) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.csapi.www.cs.schema.TpChargingPrice) org.apache.axis.utils.JavaUtils.convert(_resp, org.csapi.www.cs.schema.TpChargingPrice.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public int getLifeTimeLeft(int sessionID) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#getLifeTimeLeft");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "getLifeTimeLeft"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public org.csapi.www.cs.schema.TpVolumeSet getUnitLeft(int sessionID) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#getUnitLeft");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "getUnitLeft"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.csapi.www.cs.schema.TpVolumeSet) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.csapi.www.cs.schema.TpVolumeSet) org.apache.axis.utils.JavaUtils.convert(_resp, org.csapi.www.cs.schema.TpVolumeSet.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void rateReq(int sessionID, org.csapi.www.cs.schema.TpChargingParameterSet chargingParameters) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#rateReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "rateReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), chargingParameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void release(int sessionID, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#release");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "release"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void reserveAmountReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpChargingParameterSet chargingParameters, org.csapi.www.cs.schema.TpChargingPrice preferredAmount, org.csapi.www.cs.schema.TpChargingPrice minimumAmount, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.P_INVALID_AMOUNT, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID, org.csapi.www.osa.wsdl.P_INVALID_CURRENCY {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#reserveAmountReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "reserveAmountReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, chargingParameters, preferredAmount, minimumAmount, new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_AMOUNT) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_CURRENCY) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void reserveUnitReq(int sessionID, org.csapi.www.cs.schema.TpApplicationDescription applicationDescription, org.csapi.www.cs.schema.TpChargingParameterSet chargingParameters, org.csapi.www.cs.schema.TpVolumeSet volumes, int requestNumber) throws java.rmi.RemoteException, org.csapi.www.cs.wsdl.P_INVALID_VOLUME, org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#reserveUnitReq");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "reserveUnitReq"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID), applicationDescription, chargingParameters, volumes, new java.lang.Integer(requestNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_VOLUME) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_VOLUME) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) {
              throw (org.csapi.www.cs.wsdl.P_INVALID_REQUEST_NUMBER) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public int cmvtGetClassOfService(int sessionID) throws java.rmi.RemoteException, org.csapi.www.osa.wsdl.TpCommonExceptions, org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.csapi.org/cs/IpChargingSession#cmvtGetClassOfService");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.csapi.org/cs/wsdl", "cmvtGetClassOfService"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(sessionID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.TpCommonExceptions) {
              throw (org.csapi.www.osa.wsdl.TpCommonExceptions) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) {
              throw (org.csapi.www.osa.wsdl.P_INVALID_SESSION_ID) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
