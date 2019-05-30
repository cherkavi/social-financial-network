package bc.payment.sberbank.report.processor;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import bc.payment.sberbank.report.parser.IncomeReport;

/**
 * read report from file, <br />
 * parse it, <br />
 * notify external listener about events
 */
public class ReportProcessor {
	/**
	 * read data from file - parse to POJO
	 * @param pathToReportFile
	 * @return
	 * @throws Exception
	 */
	public static IncomeReport readReport(String pathToReportFile) throws Exception{
		return ReportProcessor.readReport(new File(pathToReportFile));
	}

	/**
	 * read data from file - parse to POJO
	 * @param reportFile
	 * @return
	 * @throws Exception
	 */
	public static IncomeReport readReport(File reportFile) throws Exception{
		Serializer  persister=new Persister((new AnnotationStrategy()));
		return persister.read(IncomeReport.class, reportFile);

	}

	// TODO validate report
	/**
	 * walk through all element of the report, notify listener about changes
	 * @param incomeReport
	 * @param listener
	 */
	public static void validate(IncomeReport incomeReport, Object service, Object listener){
		// read all data from Database by Day

	}

	/**
	 * // TODO
	 * save all records
	 */
	public static void persist(IncomeReport incomeReport, Object service, Object listener){

	}
}
