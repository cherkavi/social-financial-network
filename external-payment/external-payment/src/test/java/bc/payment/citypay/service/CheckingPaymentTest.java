package bc.payment.citypay.service;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import bc.payment.citypay.domain.CheckingPayment;
import junit.framework.Assert;

public class CheckingPaymentTest {

	@SuppressWarnings("deprecation")
	@Test
	public void checkSerialization() throws Exception{
		Date date=new Date(2015-1900,11,21,19,33,54);
		CheckingPayment payment=new CheckingPayment("101", 201L, date, "10");
		// when
// 		StringWriter output=new StringWriter();
		// matcher
		// RegistryMatcher matcher=new RegistryMatcher();
		// matcher.bind(Date.class, CityPayDateConverter.class);
		// new Persister(matcher).write(payment, output);

		// registry
//		Registry registry=new Registry();
//		registry.bind(Date.class, CityPayDateConverter.class);
//		new Persister(new RegistryStrategy(registry)).write(payment, output);
		String output=MarshallerCityPay.toXmlString(payment);
		// then
		Assert.assertTrue(output.indexOf("<TransactionDate>20151221193354</TransactionDate>")>0);
	}
}
