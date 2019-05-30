
package com.roboxchange.merchant.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RatesList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RatesList">
 *   &lt;complexContent>
 *     &lt;extension base="{http://merchant.roboxchange.com/WebService/}ResponseData">
 *       &lt;sequence>
 *         &lt;element name="Groups" type="{http://merchant.roboxchange.com/WebService/}ArrayOfPaymentMethodGroup" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RatesList", propOrder = {
    "groups"
})
public class RatesList
    extends ResponseData
{

    @XmlElement(name = "Groups")
    protected ArrayOfPaymentMethodGroup groups;

    /**
     * Gets the value of the groups property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPaymentMethodGroup }
     *     
     */
    public ArrayOfPaymentMethodGroup getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPaymentMethodGroup }
     *     
     */
    public void setGroups(ArrayOfPaymentMethodGroup value) {
        this.groups = value;
    }

}
