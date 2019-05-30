package bc.payments.sberbank.report.task.domain;

import java.io.File;

import org.junit.Test;
import org.simpleframework.xml.core.Persister;

import junit.framework.Assert;

public class SberbankReportTest {

	@Test
	public void checkSerializing() throws Exception{
		// given
		File testReportFile=new File(Thread.currentThread().getContextClassLoader().getResource("report-example.xml").getFile());
		
		
		// when
		Persister parser=new Persister();
		SberbankReport report=parser.read(SberbankReport.class, testReportFile);
		
		// then
		Assert.assertNotNull(report.getFormat());
		Assert.assertEquals("P03", report.getFormat());
		
		Assert.assertNotNull(report.getFormatDate());
		Assert.assertEquals("2011-05-13 12:00:00", report.getFormatDate());

		Assert.assertNotNull(report.getRegDate());
		Assert.assertEquals("2011-05-12", report.getRegDate());

		Assert.assertNotNull(report.getAgentName());
		Assert.assertEquals("ООО Агент", report.getAgentName());
		
		Assert.assertNotNull(report.getProvName());
		Assert.assertEquals("ООО Оператор", report.getProvName());
		
		
		Assert.assertNotNull(report.getListOfPays());
		Assert.assertTrue(report.getListOfPays().size()>0);
		Assert.assertTrue(report.getListOfPays().size()==2);

		ReportPayRecord pay1 = report.getListOfPays().get(0);
		Assert.assertNotNull(pay1.getAgentDate());
		Assert.assertEquals("2011-05-12 11:22:33", pay1.getAgentDate());
		Assert.assertNotNull(pay1.getPayId());
		Assert.assertEquals(new Integer(2345), pay1.getPayId());
		Assert.assertNotNull(pay1.getPayDate());
		Assert.assertEquals("2011-05-12 11:00:12", pay1.getPayDate());
		Assert.assertNotNull(pay1.getAccount());
		Assert.assertEquals("54321", pay1.getAccount());
		Assert.assertNotNull(pay1.getPayAmount());
		Assert.assertEquals(new Integer(10000), pay1.getPayAmount());
		Assert.assertNotNull(pay1.getRegId());
		Assert.assertEquals(new Integer(98765), pay1.getRegId());
		Assert.assertNotNull(pay1.getErrCode());
		Assert.assertEquals(new Integer(0), pay1.getErrCode());
		Assert.assertNotNull(pay1.getNote());
		Assert.assertEquals("", pay1.getNote());
		
		ReportPayRecord pay2 = report.getListOfPays().get(1);
		Assert.assertNotNull(pay2.getAgentDate());
		Assert.assertEquals("2011-05-12 11:22:35", pay2.getAgentDate());
		Assert.assertNotNull(pay2.getPayId());
		Assert.assertEquals(new Integer(2346), pay2.getPayId());
		Assert.assertNotNull(pay2.getPayDate());
		Assert.assertEquals("2011-05-12 11:00:17", pay2.getPayDate());
		Assert.assertNotNull(pay2.getAccount());
		Assert.assertEquals("65432", pay2.getAccount());
		Assert.assertNotNull(pay2.getPayAmount());
		Assert.assertEquals(new Integer(20000), pay2.getPayAmount());
		Assert.assertNotNull(pay2.getRegId());
		Assert.assertEquals(new Integer(98767), pay2.getRegId());
		Assert.assertNotNull(pay2.getErrCode());
		Assert.assertEquals(new Integer(99), pay2.getErrCode());
		Assert.assertNotNull(pay2.getNote());
		Assert.assertEquals("Ошибка подключения к серверу оператора", pay2.getNote());
	}
	
}
