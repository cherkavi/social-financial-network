package bc.payment.robokassa.ws_test;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.roboxchange.merchant.webservice.CurrenciesList;
import com.roboxchange.merchant.webservice.Method;
import com.roboxchange.merchant.webservice.OperationStateResponse;
import com.roboxchange.merchant.webservice.PaymentMethodGroup;
import com.roboxchange.merchant.webservice.PaymentMethodsList;
import com.roboxchange.merchant.webservice.RatesList;
import com.roboxchange.merchant.webservice.Service;
import com.roboxchange.merchant.webservice.ServiceSoap;

import bc.payment.robokassa.RobokassaUtils;
import junit.framework.Assert;

public class RobokassaSoapClientTest {

    // private static final QName SERVICE_NAME = new QName("http://test.robokassa.ru/WebService/", "Service");
    private static final String MERCHANT_ID="P4elka";
    private static final String MERCHANT_PASSWORD2="smpysmpy2";

    static Service ss;
    static ServiceSoap port;

    @BeforeClass
    public static void init(){
        ss = new Service(Service.WSDL_LOCATION, Service.SERVICE);
        port = ss.getServiceSoap12();
    }
    
    /**
     * Payment Status
     * 
     * https://www.robokassa.ru/ru/Doc/Ru/Interface.aspx#interfeys
     */
    @Test
    public void getStatusOfThePayment(){
    	// given
    	int invoiceId=101;
    	// when
    	// OperationStateResponse result=port.opState(MERCHANT_ID, invoiceId, RobokassaUtils.crc(MERCHANT_ID, Integer.toString(invoiceId),MERCHANT_PASSWORD2), 1);
    	OperationStateResponse result=port.opState(MERCHANT_ID, invoiceId, RobokassaUtils.crc(MERCHANT_ID, Integer.toString(invoiceId),MERCHANT_PASSWORD2));
        
    	// then
    	System.out.println(result.getResult().getDescription());
        Assert.assertEquals(1000, result.getResult().getCode()); // 0 - successfully
        Assert.assertNotNull(result.getResult().getDescription());
        
        
//        System.out.println("Аккаунт клиента в платежной системе, через которую производилась оплата: "+result.getInfo().getIncAccount());
//        System.out.println("Валюта, которой платил клиент: "+result.getInfo().getIncCurrLabel());
//        System.out.println("Сумма, оплаченная клиентом, в единицах валюты IncCurrLabel: "+result.getInfo().getIncSum());
//        System.out.println("Валюта, в которой получает средства магазин: "+result.getInfo().getOutCurrLabel());
//        System.out.println("Сумма, зачисленная на счет магазина, в единицах валюты OutCurrLabel: "+result.getInfo().getOutSum());
//        System.out.println("Способ оплаты, выбранный клиентом: "+result.getInfo().getPaymentMethod().getDescription());
        
        /**
         * 5 - операция только инициализирована, деньги от покупателя не получены. <br />
         * 10 - операция отменена, деньги от покупателя не были получены. <br />
         * 50 - деньги от покупателя получены, производится зачисление денег на счет магазина <br />
         * 60 - деньги после получения были возвращены покупателю <br />
         * 80 - исполнение операции приостановлено. <br />
         * 100 - операция выполнена, завершена успешно. <br />
         */
//        System.out.println("State.Code:"+result.getState().getCode());
//        
//        System.out.println("State.Description:"+result.getState().getDescription());
//        System.out.println("State.RequestDate:"+result.getState().getRequestDate());
//        System.out.println("State.StateDate:"+result.getState().getStateDate());
    }
    
    
    /**
     * <b>Интерфейс расчёта суммы к оплате с учётом комиссии сервиса</b> <br />
     * <small>Описание:</small>
	 * Позволяет рассчитать сумму, которую должен будет заплатить покупатель, с учётом комиссий ROBOKASSA (согласно тарифам) и тех систем, через которые покупатель решил совершать оплату заказа
     */
    @Test
    public void getAmountWithFeeForCertainLabel(){
    	// given 
    	String paymentLabel="BANKOCEAN3R";
    	String amount="100.00";
    	// when 
    	RatesList result=port.getRates(MERCHANT_ID, paymentLabel, "100.00", "en");
    	
    	// then 
        Assert.assertEquals(0, result.getResult().getCode()); // 0 - successfully
        Assert.assertNull(result.getResult().getDescription());
        
        List<PaymentMethodGroup> listOfGroups=result.getGroups().getGroup();
        Assert.assertEquals(1, listOfGroups.size());
        Assert.assertEquals(1, listOfGroups.get(0).getItems().getCurrency().size());
        Assert.assertEquals(paymentLabel, listOfGroups.get(0).getItems().getCurrency().get(0).getLabel());
        Assert.assertTrue(paymentLabel, listOfGroups.get(0).getItems().getCurrency().get(0).getRate().getIncSum().doubleValue()>=Double.parseDouble(amount));
    }

    @Test
    public void getAmountWithFee(){
    	// given 
    	
    	// when 
    	RatesList result=port.getRates(MERCHANT_ID, null, "100.00", "en");
    	
    	// then 
        Assert.assertEquals(0, result.getResult().getCode()); // 0 - successfully
        Assert.assertNull(result.getResult().getDescription());
        
        List<PaymentMethodGroup> listOfGroups=result.getGroups().getGroup();
        Assert.assertTrue(listOfGroups.size()>0);
    }
    
	@Test
	public void getPaymentMethods() {
		// given 
		// when 
        PaymentMethodsList result = port.getPaymentMethods(MERCHANT_ID, "en");

        // then
        Assert.assertEquals(0, result.getResult().getCode()); // 0 - successfully
        Assert.assertNull(result.getResult().getDescription());
        
        List<Method> listOfMethods=result.getMethods().getMethod();
        Assert.assertTrue(listOfMethods.size()>0);
        /*
        System.out.println("methods size:"+listOfMethods.size());
        for(Method eachMethod:listOfMethods){
        	System.out.println("code:"+eachMethod.getCode());
        	System.out.println("description:"+eachMethod.getDescription());
        }*/
	}
	
	@Test
	public void getCurrencies(){
		// given
		// when
		// then
		CurrenciesList result = port.getCurrencies(MERCHANT_ID, "en");
        Assert.assertEquals(0, result.getResult().getCode()); // 0 - successfully
        Assert.assertNull(result.getResult().getDescription());
		
        List<PaymentMethodGroup> listOfGroups=result.getGroups().getGroup();
        Assert.assertTrue(listOfGroups.size()>0);
        /*
        for(PaymentMethodGroup eachGroup:listOfGroups){
        	System.out.println("code:"+eachGroup.getCode());
        	System.out.println("description:"+eachGroup.getDescription());
        	List<Currency> currencyList=eachGroup.getItems().getCurrency();
        	for(Currency eachCurrency:currencyList){
        		System.out.println("   label:"+eachCurrency.getLabel());
        		System.out.println("   name:"+eachCurrency.getName());
        		System.out.println("   rate:"+eachCurrency.getRate());
        	}
        }*/
	}
	
}
