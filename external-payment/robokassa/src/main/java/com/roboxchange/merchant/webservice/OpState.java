
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
 *         &lt;element name="InvoiceID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Signature" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "invoiceID",
    "signature"
})
@XmlRootElement(name = "OpState")
public class OpState {

    @XmlElement(name = "MerchantLogin")
    protected String merchantLogin;
    @XmlElement(name = "InvoiceID")
    protected int invoiceID;
    @XmlElement(name = "Signature")
    protected String signature;

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
     * Gets the value of the invoiceID property.
     * 
     */
    public int getInvoiceID() {
        return invoiceID;
    }

    /**
     * Sets the value of the invoiceID property.
     * 
     */
    public void setInvoiceID(int value) {
        this.invoiceID = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignature(String value) {
        this.signature = value;
    }

}
