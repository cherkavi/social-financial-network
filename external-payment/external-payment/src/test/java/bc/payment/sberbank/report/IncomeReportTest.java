package bc.payment.sberbank.report;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;

import bc.payment.sberbank.report.parser.DateConverter;
import bc.payment.sberbank.report.parser.DateTimeConverter;
import bc.payment.sberbank.report.parser.IncomePayment;
import bc.payment.sberbank.report.parser.IncomeReport;
import junit.framework.Assert;

public class IncomeReportTest {

	@Test
	public void readDataFromFile() throws Exception{
		// given
		// Serializer  persister=new Persister(new AnnotationStrategy());
		Serializer  persister= new Persister(new RegistryStrategy(new Registry().bind(Date.class, DateConverter.class)));
		String pathToFile="sberbank/income-report.xml";
		URL urlToFile=Thread.currentThread().getContextClassLoader().getResource(pathToFile);
		Assert.assertNotNull("check your xml file with path: "+pathToFile, urlToFile);

		File xmlFile=new File(urlToFile.getFile());

		// when
		IncomeReport incomeReport=persister.read(IncomeReport.class, xmlFile);

		// then
		Assert.assertTrue(xmlFile.exists());
		Assert.assertNotNull(incomeReport);

		Assert.assertNotNull(incomeReport.getAgentName());
		Assert.assertEquals("ООО Агент", incomeReport.getAgentName());

		Assert.assertNotNull(incomeReport.getFormat());
		Assert.assertEquals("P03", incomeReport.getFormat());

		Assert.assertNotNull(incomeReport.getProvName());
		Assert.assertEquals("ООО Оператор", incomeReport.getProvName());

		Assert.assertNotNull(incomeReport.getFormDate());
		Assert.assertEquals("2011-05-13 12:00:00", new SimpleDateFormat(DateTimeConverter.DATETIME_FORMAT).format(incomeReport.getFormDate()));

		Assert.assertNotNull(incomeReport.getRegDate());
		Assert.assertEquals("2011-05-12", new SimpleDateFormat(DateConverter.DATE_FORMAT).format(incomeReport.getRegDate()));

		Assert.assertNotNull(incomeReport.getPayments());
		Assert.assertEquals(2, incomeReport.getPayments().size());

		IncomePayment payment=incomeReport.getPayments().get(0);
		Assert.assertNotNull(payment.getAgentDate());
		Assert.assertEquals("2011-05-12 11:22:33", new SimpleDateFormat(DateTimeConverter.DATETIME_FORMAT).format(payment.getAgentDate()));

		Assert.assertNotNull(payment.getPayId());
		Assert.assertEquals("2345",payment.getPayId());

		Assert.assertNotNull(payment.getPayDate());
		Assert.assertEquals("2011-05-12 11:00:12", new SimpleDateFormat(DateTimeConverter.DATETIME_FORMAT).format(payment.getPayDate()));

		Assert.assertNotNull(payment.getAccount());
		Assert.assertEquals("54321", payment.getAccount());

		Assert.assertNotNull(payment.getPayAmount());
		Assert.assertEquals(new BigDecimal(10000), payment.getPayAmount());

		Assert.assertNotNull(payment.getReqId());
		Assert.assertEquals("98765", payment.getReqId());

		Assert.assertNotNull(payment.getErrorCode());
		Assert.assertEquals(new Integer(0), payment.getErrorCode());

		Assert.assertNotNull(payment.getNote());
		Assert.assertEquals("", payment.getNote());

	}

}
