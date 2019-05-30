
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
 *         &lt;element name="GetPaymentMethodsResult" type="{http://merchant.roboxchange.com/WebService/}PaymentMethodsList" minOccurs="0"/>
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
    "getPaymentMethodsResult"
})
@XmlRootElement(name = "GetPaymentMethodsResponse")
public class GetPaymentMethodsResponse {

    @XmlElement(name = "GetPaymentMethodsResult")
    protected PaymentMethodsList getPaymentMethodsResult;

    /**
     * Gets the value of the getPaymentMethodsResult property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentMethodsList }
     *     
     */
    public PaymentMethodsList getGetPaymentMethodsResult() {
        return getPaymentMethodsResult;
    }

    /**
     * Sets the value of the getPaymentMethodsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentMethodsList }
     *     
     */
    public void setGetPaymentMethodsResult(PaymentMethodsList value) {
        this.getPaymentMethodsResult = value;
    }

}
