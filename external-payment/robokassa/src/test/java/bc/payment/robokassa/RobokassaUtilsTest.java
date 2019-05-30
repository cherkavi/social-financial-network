package bc.payment.robokassa;

import org.junit.Test;

import junit.framework.Assert;

public class RobokassaUtilsTest {
	
	@Test
	public void clearInvalidSymbols(){
		// given
		String value="это тестовый комментарий, this is test comment #1";
		String expectedValue="это тестовый комментарий, this is test comment 1";
		
		// when
		String result=RobokassaUtils.correctDescription(value);
		
		// then
		Assert.assertEquals(expectedValue, result);
	}
	
	@Test
	public void checkSuccessPageRedirect(){
		// given
		String invId="101";
		String outSum="10.00";
		String signatureValue="70f04f2ab88c6f32042f8e7a9e35c2f5";
		String password1="smpysmpy1";
		
		// when 
		boolean result=RobokassaUtils.isCrcValid(signatureValue, outSum, invId, password1);
		
		// then 
		Assert.assertTrue(result);
	}
	
}
