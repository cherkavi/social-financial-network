package bc.payment.robokassa;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import bc.payment.exception.PaymentParameterException;

public class RobokassaUrlTest {

	@Test
	public void checkOutputUrl() throws PaymentParameterException, IllegalAccessException{
		// given
		Long orderId=50L;
		BigDecimal amount=new BigDecimal(100);

		
		String expectedResult="https://auth.robokassa.ru/Merchant/Index.aspx?IsTest=1&MerchantLogin=P4elka&InvId=50&OutSum=100.00&SignatureValue=7111b6b0fecb4da89d9932dc2d602f1c";
		
		// when
		String generatedUrl=generateRobokassaUrl().makeExternalLink(orderId, amount);
		
		// then
		Assert.assertEquals(expectedResult, generatedUrl);
	}
	
	@Test
	public void checkOutputUrlWithCents() throws PaymentParameterException, IllegalAccessException{
		// given
		Long orderId=50L;
		BigDecimal amount=new BigDecimal(100.99);

		String expectedResult="https://auth.robokassa.ru/Merchant/Index.aspx?IsTest=1&MerchantLogin=P4elka&InvId=50&OutSum=100.99&SignatureValue=887adc5894571e4f711e92141a8531c3";
		
		// when
		String generatedUrl=generateRobokassaUrl().makeExternalLink(orderId, amount);
		
		// then
		Assert.assertEquals(expectedResult, generatedUrl);
	}

	@Test
	public void checkOutputUrlWithDescription() throws PaymentParameterException, IllegalAccessException{
		// given
		Long orderId=50L;
		BigDecimal amount=new BigDecimal(100.99);

		
		String expectedResult="https://auth.robokassa.ru/Merchant/Index.aspx?IsTest=1&MerchantLogin=P4elka&InvId=50&OutSum=100.99&SignatureValue=887adc5894571e4f711e92141a8531c3";
		
		// when
		String generatedUrl=generateRobokassaUrl().makeExternalLink(orderId, amount);
		
		// then
		Assert.assertEquals(expectedResult, generatedUrl);
	}
	
	
	@Test
	public void checkRobokassaResponse(){
		// given
		String InvId="101";
		String OutSum="50.00";
		String SignatureValue="4f16079eb545aa8e79dd9b40de7338d9";

		// when
		boolean checkingResult=RobokassaUtils.isCrcResultFromRobokassaValid(InvId, OutSum, SignatureValue, "yV4ogviUw9B51NnMvb5U");
		
		// then
		Assert.assertTrue(checkingResult);
	}

	private RobokassaUrl generateRobokassaUrl() throws PaymentParameterException, IllegalAccessException{
		RobokassaUrl returnValue=new RobokassaUrl();
		RobokassaParameters parameters=new RobokassaParameters();
		
		FieldUtils.writeDeclaredField(parameters, "robokassaUrl", "https://auth.robokassa.ru/Merchant/Index.aspx", true);
		FieldUtils.writeDeclaredField(parameters, "robokassaLogin", "P4elka", true);
		FieldUtils.writeDeclaredField(parameters, "robokassaPassword", "VT2gn9mDM36YVxqxwRz1", true);
		FieldUtils.writeDeclaredField(parameters, "robokassaPassword2", "yV4ogviUw9B51NnMvb5U", true);
		FieldUtils.writeDeclaredField(parameters, "testEndpoint", Boolean.TRUE, true);

		FieldUtils.writeDeclaredField(returnValue, "parameters", parameters, true);
		return returnValue;
	}

}
