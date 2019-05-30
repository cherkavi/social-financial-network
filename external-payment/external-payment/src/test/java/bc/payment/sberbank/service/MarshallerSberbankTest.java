package bc.payment.sberbank.service;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import bc.payment.exception.MarshallingException;
import bc.payment.exception.SecurityCheckException;
import bc.payment.sberbank.domain.Request;
import bc.payment.sberbank.domain.Response;
import junit.framework.Assert;

public class MarshallerSberbankTest {

	@Test
	public void checkMarshallingRequest(){
		// given
		@SuppressWarnings("deprecation")
		Date date=new Date(2015-1900,11,21,19,33,54);
		Request request=new Request(new Request.Parameters(101, 1001, date, "501", "502", 2000, 12234));

		// when
		String xmlValue=Request.toXml(request);

		// then
		Assert.assertNotNull(xmlValue);
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<act>101</act>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<agent_code>1001</agent_code>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<agent_date>2015-12-21T19:33:54</agent_date>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<serv_code>501</serv_code>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<account>502</account>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<pay_amount>2000</pay_amount>"));
		// System.out.println(xmlValue);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void checkUnmarshallingRequest() throws SecurityCheckException, MarshallingException{
		// given
		String xmlStringValue="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><params>"+System.getProperty("line.separator")+
				"   <act>101</act>"+System.getProperty("line.separator")+
				"   <agent_code>1001</agent_code>"+System.getProperty("line.separator")+
				"   <agent_date>2015-12-21T19:33:54</agent_date>"+System.getProperty("line.separator")+
				"   <serv_code>501</serv_code>"+System.getProperty("line.separator")+
				"   <account>502</account>"+System.getProperty("line.separator")+
				"   <pay_amount>2000</pay_amount>"+System.getProperty("line.separator")+
				"</params><sign>a593df58a0a6060debf0bea29bfaff7c</sign></request>";

		// when
		Request.Parameters requestParameters=Request.fromXmlAndCheck(xmlStringValue);

		// then
		Assert.assertNotNull(requestParameters);
		Assert.assertEquals(101, requestParameters.getAct());
		Assert.assertEquals(1001, requestParameters.getAgentCode());
		Assert.assertEquals(new Date(2015-1900,11,21,19,33,54), requestParameters.getAgentDate());
		Assert.assertEquals("501", requestParameters.getServCode());
		Assert.assertEquals("502", requestParameters.getAccount());
		Assert.assertEquals(2000, requestParameters.getPayAmount());

		// System.out.println(xmlValue);
	}

	@Test
	public void checkMarshallingResponse(){
		// given
		Response.Parameters responseParameters=new Response.Parameters(0,"OK","101",2000, "test client", new BigDecimal("10.01").toString().replace(',', '.'));

		// when
		String xmlValue=Response.toXml(responseParameters);

		// then
		// System.out.println(xmlValue);
		Assert.assertNotNull(xmlValue);
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<err_code>0</err_code>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<err_text>OK</err_text>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<account>101</account>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<desired_amount>2000</desired_amount>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<client_name>testclient</client_name>"));
		Assert.assertTrue(xmlValue.replaceAll(" ", "").contains("<balance>10.01</balance>"));
	}
}
