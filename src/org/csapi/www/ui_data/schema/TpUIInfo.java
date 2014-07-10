/**
 * TpUIInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.ui_data.schema;

public class TpUIInfo  implements java.io.Serializable {
    private org.csapi.www.ui_data.schema.TpUIInfoType switchName;
    private java.lang.Integer infoID;
    private java.lang.String infoData;
    private java.lang.String infoAddress;
    private org.csapi.www.osa.schema.TpOctetSet infoBinData;
    private java.lang.String infoUUEncData;
    private org.csapi.www.osa.schema.TpOctetSet infoMimeData;
    private org.csapi.www.osa.schema.TpOctetSet infoWaveData;
    private org.csapi.www.osa.schema.TpOctetSet infoAuData;

    public TpUIInfo() {
    }

    public TpUIInfo(
           org.csapi.www.ui_data.schema.TpUIInfoType switchName,
           java.lang.Integer infoID,
           java.lang.String infoData,
           java.lang.String infoAddress,
           org.csapi.www.osa.schema.TpOctetSet infoBinData,
           java.lang.String infoUUEncData,
           org.csapi.www.osa.schema.TpOctetSet infoMimeData,
           org.csapi.www.osa.schema.TpOctetSet infoWaveData,
           org.csapi.www.osa.schema.TpOctetSet infoAuData) {
           this.switchName = switchName;
           this.infoID = infoID;
           this.infoData = infoData;
           this.infoAddress = infoAddress;
           this.infoBinData = infoBinData;
           this.infoUUEncData = infoUUEncData;
           this.infoMimeData = infoMimeData;
           this.infoWaveData = infoWaveData;
           this.infoAuData = infoAuData;
    }


    /**
     * Gets the switchName value for this TpUIInfo.
     * 
     * @return switchName
     */
    public org.csapi.www.ui_data.schema.TpUIInfoType getSwitchName() {
        return switchName;
    }


    /**
     * Sets the switchName value for this TpUIInfo.
     * 
     * @param switchName
     */
    public void setSwitchName(org.csapi.www.ui_data.schema.TpUIInfoType switchName) {
        this.switchName = switchName;
    }


    /**
     * Gets the infoID value for this TpUIInfo.
     * 
     * @return infoID
     */
    public java.lang.Integer getInfoID() {
        return infoID;
    }


    /**
     * Sets the infoID value for this TpUIInfo.
     * 
     * @param infoID
     */
    public void setInfoID(java.lang.Integer infoID) {
        this.infoID = infoID;
    }


    /**
     * Gets the infoData value for this TpUIInfo.
     * 
     * @return infoData
     */
    public java.lang.String getInfoData() {
        return infoData;
    }


    /**
     * Sets the infoData value for this TpUIInfo.
     * 
     * @param infoData
     */
    public void setInfoData(java.lang.String infoData) {
        this.infoData = infoData;
    }


    /**
     * Gets the infoAddress value for this TpUIInfo.
     * 
     * @return infoAddress
     */
    public java.lang.String getInfoAddress() {
        return infoAddress;
    }


    /**
     * Sets the infoAddress value for this TpUIInfo.
     * 
     * @param infoAddress
     */
    public void setInfoAddress(java.lang.String infoAddress) {
        this.infoAddress = infoAddress;
    }


    /**
     * Gets the infoBinData value for this TpUIInfo.
     * 
     * @return infoBinData
     */
    public org.csapi.www.osa.schema.TpOctetSet getInfoBinData() {
        return infoBinData;
    }


    /**
     * Sets the infoBinData value for this TpUIInfo.
     * 
     * @param infoBinData
     */
    public void setInfoBinData(org.csapi.www.osa.schema.TpOctetSet infoBinData) {
        this.infoBinData = infoBinData;
    }


    /**
     * Gets the infoUUEncData value for this TpUIInfo.
     * 
     * @return infoUUEncData
     */
    public java.lang.String getInfoUUEncData() {
        return infoUUEncData;
    }


    /**
     * Sets the infoUUEncData value for this TpUIInfo.
     * 
     * @param infoUUEncData
     */
    public void setInfoUUEncData(java.lang.String infoUUEncData) {
        this.infoUUEncData = infoUUEncData;
    }


    /**
     * Gets the infoMimeData value for this TpUIInfo.
     * 
     * @return infoMimeData
     */
    public org.csapi.www.osa.schema.TpOctetSet getInfoMimeData() {
        return infoMimeData;
    }


    /**
     * Sets the infoMimeData value for this TpUIInfo.
     * 
     * @param infoMimeData
     */
    public void setInfoMimeData(org.csapi.www.osa.schema.TpOctetSet infoMimeData) {
        this.infoMimeData = infoMimeData;
    }


    /**
     * Gets the infoWaveData value for this TpUIInfo.
     * 
     * @return infoWaveData
     */
    public org.csapi.www.osa.schema.TpOctetSet getInfoWaveData() {
        return infoWaveData;
    }


    /**
     * Sets the infoWaveData value for this TpUIInfo.
     * 
     * @param infoWaveData
     */
    public void setInfoWaveData(org.csapi.www.osa.schema.TpOctetSet infoWaveData) {
        this.infoWaveData = infoWaveData;
    }


    /**
     * Gets the infoAuData value for this TpUIInfo.
     * 
     * @return infoAuData
     */
    public org.csapi.www.osa.schema.TpOctetSet getInfoAuData() {
        return infoAuData;
    }


    /**
     * Sets the infoAuData value for this TpUIInfo.
     * 
     * @param infoAuData
     */
    public void setInfoAuData(org.csapi.www.osa.schema.TpOctetSet infoAuData) {
        this.infoAuData = infoAuData;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpUIInfo)) return false;
        TpUIInfo other = (TpUIInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.switchName==null && other.getSwitchName()==null) || 
             (this.switchName!=null &&
              this.switchName.equals(other.getSwitchName()))) &&
            ((this.infoID==null && other.getInfoID()==null) || 
             (this.infoID!=null &&
              this.infoID.equals(other.getInfoID()))) &&
            ((this.infoData==null && other.getInfoData()==null) || 
             (this.infoData!=null &&
              this.infoData.equals(other.getInfoData()))) &&
            ((this.infoAddress==null && other.getInfoAddress()==null) || 
             (this.infoAddress!=null &&
              this.infoAddress.equals(other.getInfoAddress()))) &&
            ((this.infoBinData==null && other.getInfoBinData()==null) || 
             (this.infoBinData!=null &&
              this.infoBinData.equals(other.getInfoBinData()))) &&
            ((this.infoUUEncData==null && other.getInfoUUEncData()==null) || 
             (this.infoUUEncData!=null &&
              this.infoUUEncData.equals(other.getInfoUUEncData()))) &&
            ((this.infoMimeData==null && other.getInfoMimeData()==null) || 
             (this.infoMimeData!=null &&
              this.infoMimeData.equals(other.getInfoMimeData()))) &&
            ((this.infoWaveData==null && other.getInfoWaveData()==null) || 
             (this.infoWaveData!=null &&
              this.infoWaveData.equals(other.getInfoWaveData()))) &&
            ((this.infoAuData==null && other.getInfoAuData()==null) || 
             (this.infoAuData!=null &&
              this.infoAuData.equals(other.getInfoAuData())));
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
        if (getSwitchName() != null) {
            _hashCode += getSwitchName().hashCode();
        }
        if (getInfoID() != null) {
            _hashCode += getInfoID().hashCode();
        }
        if (getInfoData() != null) {
            _hashCode += getInfoData().hashCode();
        }
        if (getInfoAddress() != null) {
            _hashCode += getInfoAddress().hashCode();
        }
        if (getInfoBinData() != null) {
            _hashCode += getInfoBinData().hashCode();
        }
        if (getInfoUUEncData() != null) {
            _hashCode += getInfoUUEncData().hashCode();
        }
        if (getInfoMimeData() != null) {
            _hashCode += getInfoMimeData().hashCode();
        }
        if (getInfoWaveData() != null) {
            _hashCode += getInfoWaveData().hashCode();
        }
        if (getInfoAuData() != null) {
            _hashCode += getInfoAuData().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

}
