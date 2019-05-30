
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
 *         &lt;element name="GetRatesResult" type="{http://merchant.roboxchange.com/WebService/}RatesList" minOccurs="0"/>
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
    "getRatesResult"
})
@XmlRootElement(name = "GetRatesResponse")
public class GetRatesResponse {

    @XmlElement(name = "GetRatesResult")
    protected RatesList getRatesResult;

    /**
     * Gets the value of the getRatesResult property.
     * 
     * @return
     *     possible object is
     *     {@link RatesList }
     *     
     */
    public RatesList getGetRatesResult() {
        return getRatesResult;
    }

    /**
     * Sets the value of the getRatesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatesList }
     *     
     */
    public void setGetRatesResult(RatesList value) {
        this.getRatesResult = value;
    }

}
