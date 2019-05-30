
package com.roboxchange.merchant.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CalcOutSummResult" type="{http://merchant.roboxchange.com/WebService/}CalcSummsResponseData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "calcOutSummResult"
})
@XmlRootElement(name = "CalcOutSummResponse")
public class CalcOutSummResponse {

    @XmlElement(name = "CalcOutSummResult")
    protected CalcSummsResponseData calcOutSummResult;

    /**
     * Gets the value of the calcOutSummResult property.
     * 
     * @return
     *     possible object is
     *     {@link CalcSummsResponseData }
     *     
     */
    public CalcSummsResponseData getCalcOutSummResult() {
        return calcOutSummResult;
    }

    /**
     * Sets the value of the calcOutSummResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CalcSummsResponseData }
     *     
     */
    public void setCalcOutSummResult(CalcSummsResponseData value) {
        this.calcOutSummResult = value;
    }

}
