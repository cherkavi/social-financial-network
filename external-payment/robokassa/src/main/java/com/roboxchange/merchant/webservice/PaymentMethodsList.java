
package com.roboxchange.merchant.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentMethodsList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentMethodsList">
 *   &lt;complexContent>
 *     &lt;extension base="{http://merchant.roboxchange.com/WebService/}ResponseData">
 *       &lt;sequence>
 *         &lt;element name="Methods" type="{http://merchant.roboxchange.com/WebService/}ArrayOfMethod" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentMethodsList", propOrder = {
    "methods"
})
public class PaymentMethodsList
    extends ResponseData
{

    @XmlElement(name = "Methods")
    protected ArrayOfMethod methods;

    /**
     * Gets the value of the methods property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMethod }
     *     
     */
    public ArrayOfMethod getMethods() {
        return methods;
    }

    /**
     * Sets the value of the methods property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMethod }
     *     
     */
    public void setMethods(ArrayOfMethod value) {
        this.methods = value;
    }

}
