
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
 *         &lt;element name="OpStateExtResult" type="{http://merchant.roboxchange.com/WebService/}OperationStateResponse" minOccurs="0"/>
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
    "opStateExtResult"
})
@XmlRootElement(name = "OpStateExtResponse")
public class OpStateExtResponse {

    @XmlElement(name = "OpStateExtResult")
    protected OperationStateResponse opStateExtResult;

    /**
     * Gets the value of the opStateExtResult property.
     * 
     * @return
     *     possible object is
     *     {@link OperationStateResponse }
     *     
     */
    public OperationStateResponse getOpStateExtResult() {
        return opStateExtResult;
    }

    /**
     * Sets the value of the opStateExtResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationStateResponse }
     *     
     */
    public void setOpStateExtResult(OperationStateResponse value) {
        this.opStateExtResult = value;
    }

}
