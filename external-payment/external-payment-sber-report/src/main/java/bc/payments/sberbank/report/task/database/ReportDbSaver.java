package bc.payments.sberbank.report.task.database;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.core.Persister;
import org.springframework.batch.item.ItemWriter;

import bc.payments.sberbank.report.task.domain.SberbankReport;


public class ReportDbSaver implements ItemWriter<List<File>>{
	private final ReportService reportService;
	
	public ReportDbSaver(ReportService service){
		this.reportService=service;
	}
		

	@Override
	public void write(List<? extends List<File>> filesFromStep) throws Exception {
		Persister parser=new Persister();
		for(List<File> files:filesFromStep){
			for(File eachFile:files){
				SberbankReport report=parser.read(SberbankReport.class, eachFile);
				// Integer fileNumber=
				reportService.saveFile(eachFile.getName(), eachFile, generateDescription(report));
			}
		}
	}

	private final static String DELIMITER=",";
	
	private String generateDescription(SberbankReport report) {
		StringBuilder returnValue=new StringBuilder();
		returnValue.append("Format:");
		returnValue.append(report.getFormat());
		returnValue.append(DELIMITER);
		returnValue.append("FormDate:");
		returnValue.append(report.getFormatDate());
		returnValue.append(DELIMITER);
		returnValue.append("RegDate:");
		returnValue.append(report.getRegDate());
		returnValue.append(DELIMITER);
		returnValue.append("AgentName:");
		returnValue.append(report.getAgentName());
		returnValue.append(DELIMITER);
		returnValue.append("ProvName:");
		returnValue.append(report.getProvName());
		return returnValue.toString();
	}

}
