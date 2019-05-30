package bc.payment.sberbank.service;

import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bc.payment.common.DbAware;
import bc.payment.common.service.CommonService;
import bc.payment.exception.GeneralPaymentException;
import bc.payment.sberbank.domain.Response.Parameters;

public class SberbankServiceTest extends DbAware{

	private SberbankService service;
	
	@Before
	public void init() throws IllegalAccessException{
		this.service=new SberbankService();
		CommonService commonService=new CommonService();
		FieldUtils.writeDeclaredField(commonService, "dataSource", this.dataSource, true);
		FieldUtils.writeDeclaredField(this.service, "commonService", commonService, true);
	}
	
	
	@Test
	public void checkPossibilityToPayCardWasNotFound() throws GeneralPaymentException{
		// given 
		// карты нет
		String account="9900010011111";
		int amountInCents=100;
		
		// when
		Parameters parameters=this.service.check(account, amountInCents);
		
		// then
		Assert.assertTrue(parameters.getErrorCode()!=0);
		Assert.assertNotNull(StringUtils.trimToNull(parameters.getErrorText()));
	}

	
	@Test
	public void checkPossibilityToPayOk() throws GeneralPaymentException{
		// given 
		// Кравчук Валерий, 7585732.43 баллов
		String account="9900990010014";
		int amountInCents=100;
		
		// when
		Parameters parameters=this.service.check(account, amountInCents);
		
		// then
		Assert.assertTrue(parameters.getErrorCode()==0);
		Assert.assertEquals(amountInCents, parameters.getDesiredAmount().intValue());
	}
	
	@Test
	public void checkPossibilityToPayBlockedCard() throws GeneralPaymentException{
		// given 
		//  - карта блокирована
		String account="9900010011526";
		int amountInCents=100;
		
		// when
		Parameters parameters=this.service.check(account, amountInCents);
		
		// then
		Assert.assertTrue(parameters.getErrorCode()!=0);
		Assert.assertNotNull(StringUtils.trimToNull(parameters.getErrorText()));
		Assert.assertEquals(0, parameters.getDesiredAmount().intValue());
	}

	// TODO need to clarify (skype: 2016-03-09:1956)
	@Test
	public void checkPossibilityToPayNotProcessedCard() throws GeneralPaymentException{
		// given 
		//  Черкашин В., не оплачен членский взнос
		String account="9900990010083";
		int amountInCents=100;
		
		// when
		Parameters parameters=this.service.check(account, amountInCents);
		
		// then
		Assert.assertTrue(parameters.getErrorCode()==0);
		Assert.assertNull(StringUtils.trimToNull(parameters.getErrorText()));
	}

	@Test
	public void payOk() throws GeneralPaymentException{
		// given 
		// Кравчук Валерий, 7585732.43 баллов
		String account="9900990010014";
		int amountInCents=100;
		
		// when
		Parameters parameters=this.service.payment(new Random().nextInt(200000000),account, amountInCents, new Date());
		
		// then
		Assert.assertTrue(parameters.getErrorCode()==0);
		// select count(*) from vc_sberbank_external_oper_all
	}
	
}

