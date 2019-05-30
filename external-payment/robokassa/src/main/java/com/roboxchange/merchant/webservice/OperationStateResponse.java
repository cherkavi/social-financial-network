
package com.roboxchange.merchant.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperationStateResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OperationStateResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://merchant.roboxchange.com/WebService/}ResponseData">
 *       &lt;sequence>
 *         &lt;element name="State" type="{http://merchant.roboxchange.com/WebService/}OperationState" minOccurs="0"/>
 *         &lt;element name="Info" type="{http://merchant.roboxchange.com/WebService/}OperationInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationStateResponse", propOrder = {
    "state",
    "info"
})
public class OperationStateResponse
    extends ResponseData
{

    @XmlElement(name = "State")
    protected OperationState state;
    @XmlElement(name = "Info")
    protected OperationInfo info;

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link OperationState }
     *     
     */
    public OperationState getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationState }
     *     
     */
    public void setState(OperationState value) {
        this.state = value;
    }

    /**
     * Gets the value of the info property.
     * 
     * @return
     *     possible object is
     *     {@link OperationInfo }
     *     
     */
    public OperationInfo getInfo() {
        return info;
    }

    /**
     * Sets the value of the info property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationInfo }
     *     
     */
    public void setInfo(OperationInfo value) {
        this.info = value;
    }

}
