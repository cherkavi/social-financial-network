package bc.payments.sberbank.report.task.database;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import junit.framework.Assert;

public class ReportServiceTest extends DataSourceAware{
	private final static String FOLDER_FOR_OUTPUT="/tmp/output";
	private final static String REPORT_FILE="report-example.xml";
	
	@Test
	public void generateFileNumber() throws Exception{
		// given
		URL url=Thread.currentThread().getContextClassLoader().getResource(REPORT_FILE);
		File reportFile=new File(url.getFile());
		ReportService reportService=new ReportService(dataSource, FOLDER_FOR_OUTPUT);
		
		// when
		Integer fileNumber=reportService.saveFile("testProject", reportFile, "test description");
		
		// then
		Assert.assertNotNull(fileNumber);
		Assert.assertTrue(fileNumber>0);
		
	}
	
}
