
package com.roboxchange.merchant.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.roboxchange.merchant.webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LimitResponse_QNAME = new QName("http://merchant.roboxchange.com/WebService/", "LimitResponse");
    private final static QName _RatesList_QNAME = new QName("http://merchant.roboxchange.com/WebService/", "RatesList");
    private final static QName _PaymentMethodsList_QNAME = new QName("http://merchant.roboxchange.com/WebService/", "PaymentMethodsList");
    private final static QName _CurrenciesList_QNAME = new QName("http://merchant.roboxchange.com/WebService/", "CurrenciesList");
    private final static QName _OperationStateResponse_QNAME = new QName("http://merchant.roboxchange.com/WebService/", "OperationStateResponse");
    private final static QName _CalcSummsResponseData_QNAME = new QName("http://merchant.roboxchange.com/WebService/", "CalcSummsResponseData");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.roboxchange.merchant.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OpStateResponse }
     * 
     */
    public OpStateResponse createOpStateResponse() {
        return new OpStateResponse();
    }

    /**
     * Create an instance of {@link OperationStateResponse }
     * 
     */
    public OperationStateResponse createOperationStateResponse() {
        return new OperationStateResponse();
    }

    /**
     * Create an instance of {@link CalcSummsResponseData }
     * 
     */
    public CalcSummsResponseData createCalcSummsResponseData() {
        return new CalcSummsResponseData();
    }

    /**
     * Create an instance of {@link CalcOutSumm }
     * 
     */
    public CalcOutSumm createCalcOutSumm() {
        return new CalcOutSumm();
    }

    /**
     * Create an instance of {@link GetCurrenciesResponse }
     * 
     */
    public GetCurrenciesResponse createGetCurrenciesResponse() {
        return new GetCurrenciesResponse();
    }

    /**
     * Create an instance of {@link CurrenciesList }
     * 
     */
    public CurrenciesList createCurrenciesList() {
        return new CurrenciesList();
    }

    /**
     * Create an instance of {@link GetLimit }
     * 
     */
    public GetLimit createGetLimit() {
        return new GetLimit();
    }

    /**
     * Create an instance of {@link OpStateExt }
     * 
     */
    public OpStateExt createOpStateExt() {
        return new OpStateExt();
    }

    /**
     * Create an instance of {@link OpStateExtResponse }
     * 
     */
    public OpStateExtResponse createOpStateExtResponse() {
        return new OpStateExtResponse();
    }

    /**
     * Create an instance of {@link PaymentMethodsList }
     * 
     */
    public PaymentMethodsList createPaymentMethodsList() {
        return new PaymentMethodsList();
    }

    /**
     * Create an instance of {@link GetPaymentMethodsResponse }
     * 
     */
    public GetPaymentMethodsResponse createGetPaymentMethodsResponse() {
        return new GetPaymentMethodsResponse();
    }

    /**
     * Create an instance of {@link GetRatesResponse }
     * 
     */
    public GetRatesResponse createGetRatesResponse() {
        return new GetRatesResponse();
    }

    /**
     * Create an instance of {@link RatesList }
     * 
     */
    public RatesList createRatesList() {
        return new RatesList();
    }

    /**
     * Create an instance of {@link GetCurrencies }
     * 
     */
    public GetCurrencies createGetCurrencies() {
        return new GetCurrencies();
    }

    /**
     * Create an instance of {@link LimitResponse }
     * 
     */
    public LimitResponse createLimitResponse() {
        return new LimitResponse();
    }

    /**
     * Create an instance of {@link GetRates }
     * 
     */
    public GetRates createGetRates() {
        return new GetRates();
    }

    /**
     * Create an instance of {@link GetLimitResponse }
     * 
     */
    public GetLimitResponse createGetLimitResponse() {
        return new GetLimitResponse();
    }

    /**
     * Create an instance of {@link GetPaymentMethods }
     * 
     */
    public GetPaymentMethods createGetPaymentMethods() {
        return new GetPaymentMethods();
    }

    /**
     * Create an instance of {@link CalcOutSummResponse }
     * 
     */
    public CalcOutSummResponse createCalcOutSummResponse() {
        return new CalcOutSummResponse();
    }

    /**
     * Create an instance of {@link OpState }
     * 
     */
    public OpState createOpState() {
        return new OpState();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link PaymentMethodGroup }
     * 
     */
    public PaymentMethodGroup createPaymentMethodGroup() {
        return new PaymentMethodGroup();
    }

    /**
     * Create an instance of {@link Method }
     * 
     */
    public Method createMethod() {
        return new Method();
    }

    /**
     * Create an instance of {@link OperationState }
     * 
     */
    public OperationState createOperationState() {
        return new OperationState();
    }

    /**
     * Create an instance of {@link BaseData }
     * 
     */
    public BaseData createBaseData() {
        return new BaseData();
    }

    /**
     * Create an instance of {@link OperationPaymentMethod }
     * 
     */
    public OperationPaymentMethod createOperationPaymentMethod() {
        return new OperationPaymentMethod();
    }

    /**
     * Create an instance of {@link ArrayOfPaymentMethodGroup }
     * 
     */
    public ArrayOfPaymentMethodGroup createArrayOfPaymentMethodGroup() {
        return new ArrayOfPaymentMethodGroup();
    }

    /**
     * Create an instance of {@link ArrayOfCurrency }
     * 
     */
    public ArrayOfCurrency createArrayOfCurrency() {
        return new ArrayOfCurrency();
    }

    /**
     * Create an instance of {@link OperationInfoExt }
     * 
     */
    public OperationInfoExt createOperationInfoExt() {
        return new OperationInfoExt();
    }

    /**
     * Create an instance of {@link OperationInfo }
     * 
     */
    public OperationInfo createOperationInfo() {
        return new OperationInfo();
    }

    /**
     * Create an instance of {@link ArrayOfMethod }
     * 
     */
    public ArrayOfMethod createArrayOfMethod() {
        return new ArrayOfMethod();
    }

    /**
     * Create an instance of {@link Currency }
     * 
     */
    public Currency createCurrency() {
        return new Currency();
    }

    /**
     * Create an instance of {@link Rate }
     * 
     */
    public Rate createRate() {
        return new Rate();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LimitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://merchant.roboxchange.com/WebService/", name = "LimitResponse")
    public JAXBElement<LimitResponse> createLimitResponse(LimitResponse value) {
        return new JAXBElement<LimitResponse>(_LimitResponse_QNAME, LimitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RatesList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://merchant.roboxchange.com/WebService/", name = "RatesList")
    public JAXBElement<RatesList> createRatesList(RatesList value) {
        return new JAXBElement<RatesList>(_RatesList_QNAME, RatesList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentMethodsList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://merchant.roboxchange.com/WebService/", name = "PaymentMethodsList")
    public JAXBElement<PaymentMethodsList> createPaymentMethodsList(PaymentMethodsList value) {
        return new JAXBElement<PaymentMethodsList>(_PaymentMethodsList_QNAME, PaymentMethodsList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CurrenciesList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://merchant.roboxchange.com/WebService/", name = "CurrenciesList")
    public JAXBElement<CurrenciesList> createCurrenciesList(CurrenciesList value) {
        return new JAXBElement<CurrenciesList>(_CurrenciesList_QNAME, CurrenciesList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OperationStateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://merchant.roboxchange.com/WebService/", name = "OperationStateResponse")
    public JAXBElement<OperationStateResponse> createOperationStateResponse(OperationStateResponse value) {
        return new JAXBElement<OperationStateResponse>(_OperationStateResponse_QNAME, OperationStateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalcSummsResponseData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://merchant.roboxchange.com/WebService/", name = "CalcSummsResponseData")
    public JAXBElement<CalcSummsResponseData> createCalcSummsResponseData(CalcSummsResponseData value) {
        return new JAXBElement<CalcSummsResponseData>(_CalcSummsResponseData_QNAME, CalcSummsResponseData.class, null, value);
    }

}
