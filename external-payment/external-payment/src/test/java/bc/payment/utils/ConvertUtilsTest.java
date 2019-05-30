package bc.payment.utils;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import bc.utils.ConvertUtils;


public class ConvertUtilsTest {
	
	@Test
	public void checkConversationFromCents(){
		// given
		int amount=435;
		
		// when
		BigDecimal realValue=ConvertUtils.divideByHundred(new BigDecimal(amount));
		
		// then
		Assert.assertEquals("4.35", realValue.toString());
	}

	@Test
	public void checkConversationFromCents2(){
		// given
		int amount=5510;

		// when
		BigDecimal realValue=ConvertUtils.divideByHundred(new BigDecimal(amount));

		// then
		Assert.assertEquals("55.10", realValue.toString());
	}


	@Test
	public void checkConversationFromCentsForLongValue(){
		// given
		long amount=4334380715L;
		
		// when
		BigDecimal realValue=ConvertUtils.divideByHundred(new BigDecimal(amount));
		
		// then
		Assert.assertEquals("43343807.15", realValue.toString());
	}
	
	@Test
	public void checkConversationFromCentsForShortValue(){
		// given
		long amount=15L;
		
		// when
		BigDecimal realValue=ConvertUtils.divideByHundred(new BigDecimal(amount));
		
		// then
		Assert.assertEquals("0.15", realValue.toString());
	}
	
	
	@Test
	public void checkAmountToCentConversation(){
		// given
		BigDecimal amount=new BigDecimal("12.34");
		
		// when
		int cents=ConvertUtils.getCents(amount);
		
		// then
		Assert.assertEquals(1234, cents);
	}
	
	
}
