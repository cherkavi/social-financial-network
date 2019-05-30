package bc.payment.sberbank.resource;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import bc.payment.citypay.resource.AbstractEmbeddedJetty;
import bc.payment.sberbank.domain.Response;
import bc.payment.sberbank.service.MarshallerSberbank;
import bc.utils.RestUtils;

public class SberbankResourceTest extends AbstractEmbeddedJetty{

	@Test
	public void testWrongActNumber() throws Exception{
		// given
		String requestXml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+System.getProperty("line.separator")+ 
				"<request><params>"+System.getProperty("line.separator")+
				"   <act>101</act>"+System.getProperty("line.separator")+
				"   <agent_code>1001</agent_code>"+System.getProperty("line.separator")+
				"   <agent_date>2015-12-21T19:33:54</agent_date>"+System.getProperty("line.separator")+
				"   <serv_code>501</serv_code>"+System.getProperty("line.separator")+
				"   <account>502</account>"+System.getProperty("line.separator")+
				"   <pay_amount>2000</pay_amount>"+System.getProperty("line.separator")+
				"</params><sign>8b53063435efbeff274c33a305547850</sign></request>";

		// when
		String data=RestUtils.getStringPostRequest(this.getBaseUrl()+"process", "params", requestXml);

		// then
		// non positive response
		Assert.assertNull(data);
	}	

	
	@Test
	public void testCheckPossibilityToPay() throws Exception{
		// given
		String requestXml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><params>"+System.getProperty("line.separator")+
				"   <act>1</act>"+System.getProperty("line.separator")+
				"   <agent_code>1001</agent_code>"+System.getProperty("line.separator")+
				"   <agent_date>2015-12-21T19:33:54</agent_date>"+System.getProperty("line.separator")+
				"   <serv_code>501</serv_code>"+System.getProperty("line.separator")+
				"   <account>9900990010125</account>"+System.getProperty("line.separator")+
				"   <pay_amount>2000</pay_amount>"+System.getProperty("line.separator")+
				"</params><sign>3d1035e2b66d2e9b9243863abbae606a</sign></request>";

		// Parameters parameters=Request.fromXmlAndCheck(requestXml);
		// when
		String data=RestUtils.getStringPostRequest(this.getBaseUrl()+"process", "params", requestXml);

		// then
		data=StringUtils.substringAfter(data, "<params>");
		data=StringUtils.substringBeforeLast(data, "</params>");
		data="<params>"+data+"</params>";
		Response.Parameters responseParameters=MarshallerSberbank.getFromXmlString(data, Response.Parameters.class);

		Assert.assertNotNull(responseParameters);
		Assert.assertEquals(0, responseParameters.getErrorCode());
		Assert.assertNull(responseParameters.getErrorText());
		Assert.assertEquals("9900990010125", responseParameters.getAccount());
		Assert.assertEquals(new Integer(2000), responseParameters.getDesiredAmount());
		// Assert.assertEquals("name-1", responseParameters.getClientName());
		Assert.assertEquals("0", responseParameters.getBalance());
	}
	
	@Test
	public void testCheckPayment() throws Exception{
		// given
		StringBuilder requestXml=new StringBuilder()
		.append(System.getProperty("line.separator"))
		.append("   <act>2</act>")
		.append(System.getProperty("line.separator"))
		.append("   <agent_code>1001</agent_code>")
		.append(System.getProperty("line.separator"))
		.append("   <agent_date>"+getCurrentDate()+"</agent_date>")
		.append(System.getProperty("line.separator"))
		.append("   <serv_code>501</serv_code>")
		.append(System.getProperty("line.separator"))
		.append("   <account>9900990010125</account>")
		.append(System.getProperty("line.separator"))
		.append("   <pay_amount>2000</pay_amount>")
		.append(System.getProperty("line.separator"))
		.append("   <pay_id>")
		.append(Long.toString(System.currentTimeMillis()))
		.append("</pay_id>")
		.append(System.getProperty("line.separator"));
		
		String signature=MarshallerSberbank.calculateMd5(requestXml.toString());
		requestXml.insert(0,"<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><params>")
		.append("</params>")
		.append("<sign>")
		.append(signature)
		.append("</sign>")
		.append("</request>")
		;

		// Parameters parameters=Request.fromXmlAndCheck(requestXml);
		// when
		String data=RestUtils.getStringPostRequest(this.getBaseUrl()+"process", "params", requestXml.toString());

		// then
		data=StringUtils.substringAfter(data, "<params>");
		data=StringUtils.substringBeforeLast(data, "</params>");
		data="<params>"+data+"</params>";
		Response.Parameters responseParameters=MarshallerSberbank.getFromXmlString(data, Response.Parameters.class);

		Assert.assertNotNull(responseParameters);
		Assert.assertEquals(0, responseParameters.getErrorCode());
		Assert.assertEquals("OK", responseParameters.getErrorText());
		Assert.assertEquals("9900990010125", responseParameters.getAccount());
		Assert.assertNull(responseParameters.getDesiredAmount());
		// Assert.assertEquals("name-1", responseParameters.getClientName());
		Assert.assertNull(responseParameters.getBalance());
	}



	private String getBaseUrl(){
		return "http://localhost:"+AbstractEmbeddedJetty.DEFAULT_PORT+"/sberbank/";
	}

	private final static SimpleDateFormat SDF_DATE=new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat SDF_TIME=new SimpleDateFormat("HH:mm:ss");
	private final static String DELIMITER="T";
	
	private String getCurrentDate(){
		Date currentDate=new Date();
		return SDF_DATE.format(currentDate)+DELIMITER+SDF_TIME.format(currentDate);
	}

}
