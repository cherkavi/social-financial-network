package bc.payment.robokassa.rest;

import org.junit.Assert;
import org.junit.Test;

public class RedirectPagesTest extends AbstractEmbeddedJetty {
	
	@Test
	public void checkSuccessPageRedirection(){
		// given
		Integer nInvId = Integer.MAX_VALUE;
		
		String innerRobokassaLink=getServerRoot()+"/pages/success?invId="+Integer.toString(nInvId);
		
		// when
		String robokassaReturnValue= bc.util.RestUtils.getString(innerRobokassaLink);
		
		// then
		Assert.assertNotNull(robokassaReturnValue);
		Assert.assertTrue(robokassaReturnValue.startsWith(PAGE_FAIL_HEADER));
	}

	private final static String PAGE_FAIL_HEADER="<!DOCTYPE html>";
	
	@Test
	public void checkFailPageRedirection(){
		// given
		String nInvId = "190557";
		
		String innerRobokassaLink=getServerRoot()+"/pages/fail?invId="+nInvId;
		
		// String expectedValue="ukr.net";

		// when
		String robokassaReturnValue= bc.util.RestUtils.getString(innerRobokassaLink);
		
		// then
		Assert.assertNotNull(robokassaReturnValue);
		Assert.assertTrue(robokassaReturnValue.startsWith(PAGE_FAIL_HEADER));
	}
}
