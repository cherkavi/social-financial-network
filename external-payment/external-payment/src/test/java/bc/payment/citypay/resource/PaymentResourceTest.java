package bc.payment.citypay.resource;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Transform;

import bc.payment.citypay.domain.CheckingResponse;
import bc.payment.citypay.domain.OperationResponse;
import bc.payment.citypay.service.CityPayDateConverter;
import bc.utils.RestUtils;


/**
 * should be executed with JDK less than 1.8
 * @author technik
 *
 */
public class PaymentResourceTest extends AbstractEmbeddedJetty{
	private final static SimpleDateFormat SDF=new SimpleDateFormat("yyyyMMddHHmmss");
	
	private final static SimpleDateFormat SDF_DAY_BEGIN=new SimpleDateFormat("yyyyMMdd000000");
	private final static SimpleDateFormat SDF_DAY_END=new SimpleDateFormat("yyyyMMdd235959");
	
	@Test
	public void testCheck() throws Exception{
		// given
		// when
		String data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check&TransactionId=1234564&Account=9900990010014&PayElementId=1539&ProviderId=576&TerminalId=11&TerminalTransactionId=54321");// &field1=City-Pay

		// then
		Serializer serializer = new Persister(new AnnotationStrategy());
		OperationResponse response=serializer.read(OperationResponse.class, new StringReader(data));

		Assert.assertEquals("1234564", response.getTransactionId());
		Assert.assertEquals(0, response.getResultCode());
		Assert.assertEquals(null, response.getComment());
	}

	@Test
	public void testPay() throws Exception{
		// given
		String transactionId=Long.toString(System.currentTimeMillis());
		String smsPassword="12345";

		// 1/3 check
		// when
		String account="9900990010014";
		String payElementId="1539";
		String providerId="576";
		String terminalId="11";
		String terminalTransactionId="54321";
		String data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check"
				+"&TransactionId=" +transactionId
				+"&Account="+account
				+"&PayElementId="+payElementId
				+"&ProviderId="+providerId
				+"&TerminalId="+terminalId
				// +"&TerminalTransactionId="+terminalTransactionId
				);
		Serializer serializer = new Persister(new AnnotationStrategy());
		OperationResponse response=serializer.read(OperationResponse.class, new StringReader(data));

		// then
		Assert.assertEquals(0, response.getResultCode());

		// 2/3 check
		// when
		data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check"
				+"&TransactionId=" +transactionId
				+"&Account="+account
				+"&PayElementId="+payElementId
				+"&ProviderId="+providerId
				+"&TerminalId="+terminalId
				+"&TerminalTransactionId="+terminalTransactionId
				+"&field1="+smsPassword
				);
		serializer = new Persister(new AnnotationStrategy());
		response=serializer.read(OperationResponse.class, new StringReader(data));
		// then
		Assert.assertEquals(0, response.getResultCode());

		// 3/3 check
		// when
		data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check"
				+"&TransactionId=" +transactionId
				+"&Account="+account
				+"&PayElementId="+payElementId
				+"&ProviderId="+providerId
				+"&TerminalId="+terminalId
				+"&TerminalTransactionId="+terminalTransactionId
				+"&field1="+smsPassword
		);
		serializer = new Persister(new AnnotationStrategy());
		response=serializer.read(OperationResponse.class, new StringReader(data));
		// then
		Assert.assertEquals(0, response.getResultCode());

		// when
		String urlRequest=getBaseUrl()+"payment_app.cgi?QueryType=pay"
				+"&TransactionId="+transactionId
				+"&TransactionDate="+SDF.format(new Date())
				+"&Account="+account
				+"&Amount=17.40"
				+"&PayElementId="+payElementId
				+"&TerminalId="+terminalId
				+"&ProviderId="+providerId
				+"&field1="+smsPassword
				;
		data=RestUtils.getString(urlRequest);
		serializer = new Persister(new AnnotationStrategy());
		response=serializer.read(OperationResponse.class, new StringReader(data));

		// then
		Assert.assertEquals(new Long(transactionId).toString(), response.getTransactionId());
		Assert.assertNotNull(response.getTransactionExt());
		Assert.assertTrue(response.getTransactionExt()>0);
		Assert.assertEquals(null, response.getComment());
		Assert.assertEquals(0, response.getResultCode());
	}

	@Test
	public void testPay2() throws Exception{
		// given
		String transactionId=Long.toString(System.currentTimeMillis());
		String smsPassword="12345";

		// 1/3 check
		// when
		String account="9900990010014";
		String payElementId="1539";
		String providerId="576";
		String terminalId="11";
		String terminalTransactionId="54321";
		String data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check"
						+"&TransactionId=" +transactionId
						+"&Account="+account
						+"&PayElementId="+payElementId
						+"&ProviderId="+providerId
						+"&TerminalId="+terminalId
				// +"&TerminalTransactionId="+terminalTransactionId
		);
		Serializer serializer = new Persister(new AnnotationStrategy());
		OperationResponse response=serializer.read(OperationResponse.class, new StringReader(data));

		// then
		Assert.assertEquals(0, response.getResultCode());

		// 2/3 check
		// when
		data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check"
				+"&TransactionId=" +transactionId
				+"&Account="+account
				+"&PayElementId="+payElementId
				+"&ProviderId="+providerId
				+"&TerminalId="+terminalId
				+"&TerminalTransactionId="+terminalTransactionId
				+"&field1="+smsPassword
		);
		serializer = new Persister(new AnnotationStrategy());
		response=serializer.read(OperationResponse.class, new StringReader(data));
		// then
		Assert.assertEquals(0, response.getResultCode());

		// 3/3 check
		// when
		data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check"
				+"&TransactionId=" +transactionId
				+"&Account="+account
				+"&PayElementId="+payElementId
				+"&ProviderId="+providerId
				+"&TerminalId="+terminalId
				+"&TerminalTransactionId="+terminalTransactionId
				+"&field1="+smsPassword
		);
		serializer = new Persister(new AnnotationStrategy());
		response=serializer.read(OperationResponse.class, new StringReader(data));
		// then
		Assert.assertEquals(0, response.getResultCode());

		// when
		String urlRequest=getBaseUrl()+"payment_app.cgi?QueryType=pay"
				+"&TransactionId="+transactionId
				+"&TransactionDate="+SDF.format(new Date())
				+"&Account="+account
				+"&Amount=17.40"
				+"&PayElementId="+payElementId
				+"&TerminalId="+terminalId
				+"&ProviderId="+providerId
				+"&field1="+smsPassword
				+"&field2=anotherCityPay2"
				+"&field3=anotherCityPay3"
				+"&field4=anotherCityPay4"
				+"&field5=anotherCityPay5"
				+"&field6=anotherCityPay6"
				+"&field7=anotherCityPay7+"
				+"&field9=anotherCityPay9"
				;
		data=RestUtils.getString(urlRequest);
		serializer = new Persister(new AnnotationStrategy());
		response=serializer.read(OperationResponse.class, new StringReader(data));

		// then
		Assert.assertEquals(new Long(transactionId).toString(), response.getTransactionId());
		Assert.assertNotNull(response.getTransactionExt());
		Assert.assertTrue(response.getTransactionExt()>0);
		Assert.assertEquals(null, response.getComment());
		Assert.assertEquals(0, response.getResultCode());
	}

	@Test
	public void testCancelAfterPayment() throws Exception{
		// given
		String transactionId=Long.toString(System.currentTimeMillis());
		String smsPassword="12345";

		// 1/3 check
		// when
		String account="9900990010014";
		String payElementId="1539";
		String providerId="576";
		String terminalId="11";
		String terminalTransactionId="54321";
		String data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check"
						+"&TransactionId=" +transactionId
						+"&Account="+account
						+"&PayElementId="+payElementId
						+"&ProviderId="+providerId
						+"&TerminalId="+terminalId
				// +"&TerminalTransactionId="+terminalTransactionId
		);
		Serializer serializer = new Persister(new AnnotationStrategy());
		OperationResponse response=serializer.read(OperationResponse.class, new StringReader(data));

		// then
		Assert.assertEquals(0, response.getResultCode());

		// 2/3 check
		// when
		data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check"
				+"&TransactionId=" +transactionId
				+"&Account="+account
				+"&PayElementId="+payElementId
				+"&ProviderId="+providerId
				+"&TerminalId="+terminalId
				+"&TerminalTransactionId="+terminalTransactionId
				+"&field1="+smsPassword
		);
		serializer = new Persister(new AnnotationStrategy());
		response=serializer.read(OperationResponse.class, new StringReader(data));
		// then
		Assert.assertEquals(0, response.getResultCode());

		// 3/3 check
		// when
		data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=check"
				+"&TransactionId=" +transactionId
				+"&Account="+account
				+"&PayElementId="+payElementId
				+"&ProviderId="+providerId
				+"&TerminalId="+terminalId
				+"&TerminalTransactionId="+terminalTransactionId
				+"&field1="+smsPassword
		);
		serializer = new Persister(new AnnotationStrategy());
		response=serializer.read(OperationResponse.class, new StringReader(data));
		// then
		Assert.assertEquals(0, response.getResultCode());

		String amount="17.40";
		// when
		String urlRequest=getBaseUrl()+"payment_app.cgi?QueryType=pay"
				+"&TransactionId="+transactionId
				+"&TransactionDate="+SDF.format(new Date())
				+"&Account="+account
				+"&Amount="+amount
				+"&PayElementId="+payElementId
				+"&TerminalId="+terminalId
				+"&ProviderId="+providerId
				+"&field1="+smsPassword
				+"&field2=anotherCityPay2"
				+"&field3=anotherCityPay3"
				+"&field4=anotherCityPay4"
				+"&field5=anotherCityPay5"
				+"&field6=anotherCityPay6"
				+"&field7=anotherCityPay7+"
				+"&field9=anotherCityPay9"
				;
		data=RestUtils.getString(urlRequest);
		serializer = new Persister(new AnnotationStrategy());
		response=serializer.read(OperationResponse.class, new StringReader(data));

		// then
		Assert.assertEquals(new Long(transactionId).toString(), response.getTransactionId());
		Assert.assertNotNull(response.getTransactionExt());
		Assert.assertTrue(response.getTransactionExt()>0);
		Assert.assertEquals(null, response.getComment());
		Assert.assertEquals(0, response.getResultCode());
		
		
		// given
		String revertId=Long.toString(System.currentTimeMillis());
		// when
		data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=cancel"
				+"&TransactionId="+transactionId
				+"&RevertId="+revertId
				+"&RevertDate="+SDF.format(new Date())
				+"&Account="+account
				+"&Amount="+amount
		);
		// then
		serializer = new Persister();
		response=serializer.read(OperationResponse.class, new StringReader(data));
		
		Assert.assertEquals(transactionId, response.getTransactionId());
		Assert.assertEquals(1, response.getResultCode());
		Assert.assertTrue(response.getComment().length()>0);
	}
	
	@Test
	public void testCancel() throws Exception{
		// given
		// when
		String data=RestUtils.getString(getBaseUrl()+"payment_app.cgi?QueryType=cancel&TransactionId=1234567&RevertId=1234579&RevertDate="+SDF.format(new Date())+"&Account=2128506&Amount=17.40");
		// then
		Serializer serializer = new Persister(new AnnotationStrategy());
		OperationResponse response=serializer.read(OperationResponse.class, new StringReader(data));
		
		Assert.assertEquals("1234567", response.getTransactionId());
		Assert.assertEquals(1, response.getResultCode());
		Assert.assertTrue(response.getComment().length()>0);
	}

	private String getBaseUrl(){
		return "http://localhost:"+DEFAULT_PORT+"/citypay/";
	}

	@Test
	public void testPerDayReport() throws Exception{
		// given
		// when
		String data=RestUtils.getString(getBaseUrl()+"PayDayReport.html?CheckDateBegin="+SDF_DAY_BEGIN.format(new Date())+"&CheckDateEnd="+SDF_DAY_END.format(new Date()));

		// then
		CheckingResponse response=new Persister(new AnnotationStrategy()).read(CheckingResponse.class, new StringReader(data));
		Assert.assertNotNull(response);
	}
	
	
	@Test
	public void testPerDayReportWithPaymentId() throws Exception{
		// given
		// when
		String data=RestUtils.getString(getBaseUrl()+"PayDayReport.html?CheckDateBegin="+SDF_DAY_BEGIN.format(new Date())+"&CheckDateEnd="+SDF_DAY_END.format(new Date())+"&PayElementId=5");
		// then
		CheckingResponse response=new Persister(new AnnotationStrategy()).read(CheckingResponse.class, new StringReader(data));
		Assert.assertNotNull(response);
	}
	

	static class CityPayDateTransformer implements Transform<Date>{
		private SimpleDateFormat sdf=new SimpleDateFormat(CityPayDateConverter.DATE_FORMAT);
		
		@Override
		public Date read(String value) throws Exception {
			if(value==null){
				return null;
			}
			return sdf.parse(value);
		}

		@Override
		public String write(Date value) throws Exception {
			if(value==null){
				return null;
			}
			return sdf.format(value);
		}
		
	}
	
}
