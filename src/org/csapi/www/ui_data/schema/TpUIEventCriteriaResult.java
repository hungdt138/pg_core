/**
 * TpUIEventCriteriaResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIEventCriteriaResult  implements java.io.Serializable {
    private org.csapi.www.ui_data.schema.TpUIEventCriteria eventCriteria;
    private int assignmentID;

    public TpUIEventCriteriaResult() {
    }

    public TpUIEventCriteriaResult(
           org.csapi.www.ui_data.schema.TpUIEventCriteria eventCriteria,
           int assignmentID) {
           this.eventCriteria = eventCriteria;
           this.assignmentID = assignmentID;
    }


    /**
     * Gets the eventCriteria value for this TpUIEventCriteriaResult.
     * 
     * @return eventCriteria
     */
    public org.csapi.www.ui_data.schema.TpUIEventCriteria getEventCriteria() {
        return eventCriteria;
    }


    /**
     * Sets the eventCriteria value for this TpUIEventCriteriaResult.
     * 
     * @param eventCriteria
     */
    public void setEventCriteria(org.csapi.www.ui_data.schema.TpUIEventCriteria eventCriteria) {
        this.eventCriteria = eventCriteria;
    }


    /**
     * Gets the assignmentID value for this TpUIEventCriteriaResult.
     * 
     * @return assignmentID
     */
    public int getAssignmentID() {
        return assignmentID;
    }


    /**
     * Sets the assignmentID value for this TpUIEventCriteriaResult.
     * 
     * @param assignmentID
     */
    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUIEventCriteriaResult)) return false;
        TpUIEventCriteriaResult other = (TpUIEventCriteriaResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.eventCriteria==null && other.getEventCriteria()==null) || 
             (this.eventCriteria!=null &&
              this.eventCriteria.equals(other.getEventCriteria()))) &&
            this.assignmentID == other.getAssignmentID();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getEventCriteria() != null) {
            _hashCode += getEventCriteria().hashCode();
        }
        _hashCode += getAssignmentID();
        __hashCodeCalc = false;
        return _hashCode;
    }

}
