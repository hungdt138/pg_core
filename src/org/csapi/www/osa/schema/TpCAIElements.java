/**
 * TpCAIElements.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Aug 08, 2005 (11:49:10 PDT) WSDL2Java emitter.
 */

package org.csapi.www.osa.schema;

public class TpCAIElements  implements java.io.Serializable {
    private int initialSecsPerTimeInterval;
    private int secondsPerTimeInterval;
    private int segmentsPerDataInterval;
    private int scalingFactor;
    private int unitIncrement;
    private int unitsPerDataInterval;
    private int unitsPerInterval;

    public TpCAIElements() {
    }

    public TpCAIElements(
           int initialSecsPerTimeInterval,
           int secondsPerTimeInterval,
           int segmentsPerDataInterval,
           int scalingFactor,
           int unitIncrement,
           int unitsPerDataInterval,
           int unitsPerInterval) {
           this.initialSecsPerTimeInterval = initialSecsPerTimeInterval;
           this.secondsPerTimeInterval = secondsPerTimeInterval;
           this.segmentsPerDataInterval = segmentsPerDataInterval;
           this.scalingFactor = scalingFactor;
           this.unitIncrement = unitIncrement;
           this.unitsPerDataInterval = unitsPerDataInterval;
           this.unitsPerInterval = unitsPerInterval;
    }


    /**
     * Gets the initialSecsPerTimeInterval value for this TpCAIElements.
     * 
     * @return initialSecsPerTimeInterval
     */
    public int getInitialSecsPerTimeInterval() {
        return initialSecsPerTimeInterval;
    }


    /**
     * Sets the initialSecsPerTimeInterval value for this TpCAIElements.
     * 
     * @param initialSecsPerTimeInterval
     */
    public void setInitialSecsPerTimeInterval(int initialSecsPerTimeInterval) {
        this.initialSecsPerTimeInterval = initialSecsPerTimeInterval;
    }


    /**
     * Gets the secondsPerTimeInterval value for this TpCAIElements.
     * 
     * @return secondsPerTimeInterval
     */
    public int getSecondsPerTimeInterval() {
        return secondsPerTimeInterval;
    }


    /**
     * Sets the secondsPerTimeInterval value for this TpCAIElements.
     * 
     * @param secondsPerTimeInterval
     */
    public void setSecondsPerTimeInterval(int secondsPerTimeInterval) {
        this.secondsPerTimeInterval = secondsPerTimeInterval;
    }


    /**
     * Gets the segmentsPerDataInterval value for this TpCAIElements.
     * 
     * @return segmentsPerDataInterval
     */
    public int getSegmentsPerDataInterval() {
        return segmentsPerDataInterval;
    }


    /**
     * Sets the segmentsPerDataInterval value for this TpCAIElements.
     * 
     * @param segmentsPerDataInterval
     */
    public void setSegmentsPerDataInterval(int segmentsPerDataInterval) {
        this.segmentsPerDataInterval = segmentsPerDataInterval;
    }


    /**
     * Gets the scalingFactor value for this TpCAIElements.
     * 
     * @return scalingFactor
     */
    public int getScalingFactor() {
        return scalingFactor;
    }


    /**
     * Sets the scalingFactor value for this TpCAIElements.
     * 
     * @param scalingFactor
     */
    public void setScalingFactor(int scalingFactor) {
        this.scalingFactor = scalingFactor;
    }


    /**
     * Gets the unitIncrement value for this TpCAIElements.
     * 
     * @return unitIncrement
     */
    public int getUnitIncrement() {
        return unitIncrement;
    }


    /**
     * Sets the unitIncrement value for this TpCAIElements.
     * 
     * @param unitIncrement
     */
    public void setUnitIncrement(int unitIncrement) {
        this.unitIncrement = unitIncrement;
    }


    /**
     * Gets the unitsPerDataInterval value for this TpCAIElements.
     * 
     * @return unitsPerDataInterval
     */
    public int getUnitsPerDataInterval() {
        return unitsPerDataInterval;
    }


    /**
     * Sets the unitsPerDataInterval value for this TpCAIElements.
     * 
     * @param unitsPerDataInterval
     */
    public void setUnitsPerDataInterval(int unitsPerDataInterval) {
        this.unitsPerDataInterval = unitsPerDataInterval;
    }


    /**
     * Gets the unitsPerInterval value for this TpCAIElements.
     * 
     * @return unitsPerInterval
     */
    public int getUnitsPerInterval() {
        return unitsPerInterval;
    }


    /**
     * Sets the unitsPerInterval value for this TpCAIElements.
     * 
     * @param unitsPerInterval
     */
    public void setUnitsPerInterval(int unitsPerInterval) {
        this.unitsPerInterval = unitsPerInterval;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TpCAIElements)) return false;
        TpCAIElements other = (TpCAIElements) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.initialSecsPerTimeInterval == other.getInitialSecsPerTimeInterval() &&
            this.secondsPerTimeInterval == other.getSecondsPerTimeInterval() &&
            this.segmentsPerDataInterval == other.getSegmentsPerDataInterval() &&
            this.scalingFactor == other.getScalingFactor() &&
            this.unitIncrement == other.getUnitIncrement() &&
            this.unitsPerDataInterval == other.getUnitsPerDataInterval() &&
            this.unitsPerInterval == other.getUnitsPerInterval();
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
        _hashCode += getInitialSecsPerTimeInterval();
        _hashCode += getSecondsPerTimeInterval();
        _hashCode += getSegmentsPerDataInterval();
        _hashCode += getScalingFactor();
        _hashCode += getUnitIncrement();
        _hashCode += getUnitsPerDataInterval();
        _hashCode += getUnitsPerInterval();
        __hashCodeCalc = false;
        return _hashCode;
    }

}
