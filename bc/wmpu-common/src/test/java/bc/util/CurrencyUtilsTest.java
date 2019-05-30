package bc.util;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

public class CurrencyUtilsTest {
	
	@Test
	public void checkAmountWithoutCents(){
		// given
		String value="25";
		String expectedValue="25.00";
		// when
		String result=CurrencyUtils.amountToString(new BigDecimal(value));
		// then
		Assert.assertEquals(expectedValue, result);
	}
	
	@Test
	public void checkAmountWithoutCents2(){
		// given
		String value="25.2";
		String expectedValue="25.20";
		// when
		String result=CurrencyUtils.amountToString(new BigDecimal(value));
		// then
		Assert.assertEquals(expectedValue, result);
	}
	
	@Test
	public void checkAmountWithRoundUp(){
		// given
		String value="25.1999999";
		String expectedValue="25.20";
		// when
		String result=CurrencyUtils.amountToString(new BigDecimal(value));
		// then
		Assert.assertEquals(expectedValue, result);
	}
	
	@Test
	public void checkAmountWithRoundUp2(){
		// given
		String value="25.1955555";
		String expectedValue="25.20";
		// when
		String result=CurrencyUtils.amountToString(new BigDecimal(value));
		// then
		Assert.assertEquals(expectedValue, result);
	}

	@Test
	public void checkAmountWithRoundDown(){
		// given
		String value="25.191111";
		String expectedValue="25.19";
		// when
		String result=CurrencyUtils.amountToString(new BigDecimal(value));
		// then
		Assert.assertEquals(expectedValue, result);
	}
	
	@Test
	public void checkAmountWithRoundDown2(){
		// given
		String value="25.1944444";
		String expectedValue="25.19";
		// when
		String result=CurrencyUtils.amountToString(new BigDecimal(value));
		// then
		Assert.assertEquals(expectedValue, result);
	}
	
}
