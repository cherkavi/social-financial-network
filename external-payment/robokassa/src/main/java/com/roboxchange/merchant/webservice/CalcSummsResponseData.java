
package com.roboxchange.merchant.webservice;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CalcSummsResponseData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CalcSummsResponseData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://merchant.roboxchange.com/WebService/}ResponseData">
 *       &lt;sequence>
 *         &lt;element name="OutSum" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CalcSummsResponseData", propOrder = {
    "outSum"
})
public class CalcSummsResponseData
    extends ResponseData
{

    @XmlElement(name = "OutSum", required = true)
    protected BigDecimal outSum;

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
