package com.roboxchange.merchant.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 3.0.4
 * 2016-05-04T10:13:39.425+04:00
 * Generated source version: 3.0.4
 * 
 */
@WebServiceClient(name = "Service", 
                  wsdlLocation = "https://auth.robokassa.ru/Merchant/WebService/Service.asmx?WSDL",
                  targetNamespace = "http://merchant.roboxchange.com/WebService/") 
public class Service extends javax.xml.ws.Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://merchant.roboxchange.com/WebService/", "Service");
    public final static QName ServiceSoap = new QName("http://merchant.roboxchange.com/WebService/", "ServiceSoap");
    public final static QName ServiceSoap12 = new QName("http://merchant.roboxchange.com/WebService/", "ServiceSoap12");
    public final static QName ServiceHttpPost = new QName("http://merchant.roboxchange.com/WebService/", "ServiceHttpPost");
    public final static QName ServiceHttpGet = new QName("http://merchant.roboxchange.com/WebService/", "ServiceHttpGet");
    static {
        URL url = null;
        try {
            url = new URL("https://auth.robokassa.ru/Merchant/WebService/Service.asmx?WSDL");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(Service.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "https://auth.robokassa.ru/Merchant/WebService/Service.asmx?WSDL");
        }
        WSDL_LOCATION = url;
    }

    public Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns ServiceSoap
     */
    @WebEndpoint(name = "ServiceSoap")
    public ServiceSoap getServiceSoap() {
        return super.getPort(ServiceSoap, ServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServiceSoap
     */
    @WebEndpoint(name = "ServiceSoap")
    public ServiceSoap getServiceSoap(WebServiceFeature... features) {
        return super.getPort(ServiceSoap, ServiceSoap.class, features);
    }
    /**
     *
     * @return
     *     returns ServiceSoap
     */
    @WebEndpoint(name = "ServiceSoap12")
    public ServiceSoap getServiceSoap12() {
        return super.getPort(ServiceSoap12, ServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServiceSoap
     */
    @WebEndpoint(name = "ServiceSoap12")
    public ServiceSoap getServiceSoap12(WebServiceFeature... features) {
        return super.getPort(ServiceSoap12, ServiceSoap.class, features);
    }
    /**
     *
     * @return
     *     returns ServiceHttpPost
     */
    @WebEndpoint(name = "ServiceHttpPost")
    public ServiceHttpPost getServiceHttpPost() {
        return super.getPort(ServiceHttpPost, ServiceHttpPost.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServiceHttpPost
     */
    @WebEndpoint(name = "ServiceHttpPost")
    public ServiceHttpPost getServiceHttpPost(WebServiceFeature... features) {
        return super.getPort(ServiceHttpPost, ServiceHttpPost.class, features);
    }
    /**
     *
     * @return
     *     returns ServiceHttpGet
     */
    @WebEndpoint(name = "ServiceHttpGet")
    public ServiceHttpGet getServiceHttpGet() {
        return super.getPort(ServiceHttpGet, ServiceHttpGet.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServiceHttpGet
     */
    @WebEndpoint(name = "ServiceHttpGet")
    public ServiceHttpGet getServiceHttpGet(WebServiceFeature... features) {
        return super.getPort(ServiceHttpGet, ServiceHttpGet.class, features);
    }

}
