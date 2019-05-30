
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
 *         &lt;element name="IncSum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "incSum"
})
@XmlRootElement(name = "CalcOutSumm")
public class CalcOutSumm {

    @XmlElement(name = "MerchantLogin")
    protected String merchantLogin;
    @XmlElement(name = "IncCurrLabel")
    protected String incCurrLabel;
    @XmlElement(name = "IncSum")
    protected String incSum;

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
     * Gets the value of the incSum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncSum() {
        return incSum;
    }

    /**
     * Sets the value of the incSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncSum(String value) {
        this.incSum = value;
    }

}
