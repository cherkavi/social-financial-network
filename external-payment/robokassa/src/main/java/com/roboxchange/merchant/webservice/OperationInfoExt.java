
package com.roboxchange.merchant.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperationInfoExt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OperationInfoExt">
 *   &lt;complexContent>
 *     &lt;extension base="{http://merchant.roboxchange.com/WebService/}OperationInfo">
 *       &lt;sequence>
 *         &lt;element name="OpKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationInfoExt", propOrder = {
    "opKey"
})
public class OperationInfoExt
    extends OperationInfo
{

    @XmlElement(name = "OpKey")
    protected String opKey;

    /**
     * Gets the value of the opKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpKey() {
        return opKey;
    }

    /**
     * Sets the value of the opKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpKey(String value) {
        this.opKey = value;
    }

}
