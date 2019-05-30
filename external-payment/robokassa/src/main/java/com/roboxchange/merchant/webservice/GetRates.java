
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
 *         &lt;element name="MerchantLogin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IncCurrLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OutSum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "merchantLogin",
    "incCurrLabel",
    "outSum",
    "language"
})
@XmlRootElement(name = "GetRates")
public class GetRates {

    @XmlElement(name = "MerchantLogin")
    protected String merchantLogin;
    @XmlElement(name = "IncCurrLabel")
    protected String incCurrLabel;
    @XmlElement(name = "OutSum")
    protected String outSum;
    @XmlElement(name = "Language")
    protected String language;

    /**
     * Gets the value of the merchantLogin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerchantLogin() {
        return merchantLogin;
    }

    /**
     * Sets the value of the merchantLogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerchantLogin(String value) {
        this.merchantLogin = value;
    }

    /**
     * Gets the value of the incCurrLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncCurrLabel() {
        return incCurrLabel;
    }

    /**
     * Sets the value of the incCurrLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncCurrLabel(String value) {
        this.incCurrLabel = value;
    }

    /**
     * Gets the value of the outSum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutSum() {
        return outSum;
    }

    /**
     * Sets the value of the outSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutSum(String value) {
        this.outSum = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

}
