
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
 *         &lt;element name="GetLimitResult" type="{http://merchant.roboxchange.com/WebService/}LimitResponse" minOccurs="0"/>
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
    "getLimitResult"
})
@XmlRootElement(name = "GetLimitResponse")
public class GetLimitResponse {

    @XmlElement(name = "GetLimitResult")
    protected LimitResponse getLimitResult;

    /**
     * Gets the value of the getLimitResult property.
     * 
     * @return
     *     possible object is
     *     {@link LimitResponse }
     *     
     */
    public LimitResponse getGetLimitResult() {
        return getLimitResult;
    }

    /**
     * Sets the value of the getLimitResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link LimitResponse }
     *     
     */
    public void setGetLimitResult(LimitResponse value) {
        this.getLimitResult = value;
    }

}
