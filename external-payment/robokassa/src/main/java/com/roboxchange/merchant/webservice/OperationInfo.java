
package com.roboxchange.merchant.webservice;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperationInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OperationInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IncCurrLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IncSum" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="IncAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PaymentMethod" type="{http://merchant.roboxchange.com/WebService/}OperationPaymentMethod" minOccurs="0"/>
 *         &lt;element name="OutCurrLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OutSum" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationInfo", propOrder = {
    "incCurrLabel",
    "incSum",
    "incAccount",
    "paymentMethod",
    "outCurrLabel",
    "outSum"
})
@XmlSeeAlso({
    OperationInfoExt.class
})
public class OperationInfo {

    @XmlElement(name = "IncCurrLabel")
    protected String incCurrLabel;
    @XmlElement(name = "IncSum", required = true)
    protected BigDecimal incSum;
    @XmlElement(name = "IncAccount")
    protected String incAccount;
    @XmlElement(name = "PaymentMethod")
    protected OperationPaymentMethod paymentMethod;
    @XmlElement(name = "OutCurrLabel")
    protected String outCurrLabel;
    @XmlElement(name = "OutSum", required = true)
    protected BigDecimal outSum;

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
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIncSum() {
        return incSum;
    }

    /**
     * Sets the value of the incSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIncSum(BigDecimal value) {
        this.incSum = value;
    }

    /**
     * Gets the value of the incAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncAccount() {
        return incAccount;
    }

    /**
     * Sets the value of the incAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncAccount(String value) {
        this.incAccount = value;
    }

    /**
     * Gets the value of the paymentMethod property.
     * 
     * @return
     *     possible object is
     *     {@link OperationPaymentMethod }
     *     
     */
    public OperationPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets the value of the paymentMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationPaymentMethod }
     *     
     */
    public void setPaymentMethod(OperationPaymentMethod value) {
        this.paymentMethod = value;
    }

    /**
     * Gets the value of the outCurrLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutCurrLabel() {
        return outCurrLabel;
    }

    /**
     * Sets the value of the outCurrLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutCurrLabel(String value) {
        this.outCurrLabel = value;
    }

    /**
     * Gets the value of the outSum property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOutSum() {
        return outSum;
    }

    /**
     * Sets the value of the outSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOutSum(BigDecimal value) {
        this.outSum = value;
    }

}
