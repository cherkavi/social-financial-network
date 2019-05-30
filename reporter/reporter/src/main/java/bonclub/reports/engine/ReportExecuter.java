package bonclub.reports.engine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import bonclub.reports.ReportExecuterQueryParameters;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import database.ConnectWrap;

/** создатель отчетов  */
public class ReportExecuter {
	private Logger logger=Logger.getLogger(this.getClass());
	private String pathToReportPattern;
	
	/** создатель отчетов  
	 * @param pathToReportPattern - полный путь к файлам для складирования отчетов 
	 * */
	public ReportExecuter(String pathToReportPattern){
		this.pathToReportPattern=pathToReportPattern;
	}
	
	/** выполнение запроса, согласно переданным параметрам  */
	public void executeReport(ConnectWrap connector,
							   String pathToFile,
							   
							   int reportId,  
							   ArrayList<ReportExecuterQueryParameters> queryOfReports, 
							   String outputFormatStringRepresent,
							   HashMap<String, Object> parameters,
							   HashMap<String, String> jasperParameters) throws Exception{
		logger.debug("executeReport begin");
		ReportOutputFormat outputFormat=ReportOutputFormat.valueOf(outputFormatStringRepresent);
		
		logger.debug("executeReport outputFormat:"+outputFormat.name());
		ResultSet resultset=executeQueryAndAddParameter(connector.getConnection(),
														queryOfReports,
														parameters,
														this.pathToReportPattern);
		
		
		String patternFile=getFileName(connector.getConnection(),reportId);
		logger.debug("Получить имя файла JasperReport: "+patternFile);
		
        logger.debug("compile report:"+this.pathToReportPattern+patternFile);
        JasperReport jasper_report=JasperCompileManager.compileReport(this.pathToReportPattern+patternFile);
		
		if(outputFormat.equals(ReportOutputFormat.HTML)){
        	jasperParameters.put("DEFAULT_SPACE", " ");
        }else{
        	jasperParameters.put("DEFAULT_SPACE", "");
        }

        logger.debug("create print");
        JasperPrint jasperPrint=null;
        //  JasperPrint jasperPrint=JasperFillManager.fillReport(jasper_report,parameters, new JRResultSetDataSource(resultset));
        if(resultset!=null){
        	// обычный отчет 
            jasperPrint=JasperFillManager.fillReport(jasper_report,
                    jasperParameters,
                    new JRResultSetDataSource(resultset));
        }else{
        	// мультиотчет
            jasperPrint=JasperFillManager.fillReport(jasper_report,
                    jasperParameters,
                    new JREmptyDataSource());
        }

		exportToFile(outputFormat, jasperPrint, pathToFile);
        
		// закрытие всех возможно откртых запросов
		if(resultset!=null){
			resultset.getStatement().close();
		}
		for(int counter=0;counter<queryOfReports.size();counter++){
			if(jasperParameters.get("DATASOURCE_"+(counter+1))!=null){
				try{
					if(queryOfReports.get(counter).getResultSet()!=null){
						queryOfReports.get(counter).getResultSet().getStatement().close();
					}
				}catch(Exception ex){}
			}
		}
	}
	
	/** экспортировать данные в файл 
	 * @param outputFormat - выходной формат файла
	 * @param jasperPrint - отчет в предпечатном состоянии 
	 * @param pathToFile - полный путь к файлу, который нужно сохранить 
	 * @throws JRException - ошибка при выполнении экспорта отчета
	 * @return 
	 * <ul>
	 * 	<li><b>true</b> - экспортирован </li>
	 * 	<li><b>false</b> - ошибка экспорта </li>
	 * </ul>  
	 */
	private boolean exportToFile(ReportOutputFormat outputFormat, JasperPrint jasperPrint, String pathToFile) throws JRException {
		boolean returnValue=false; 
        if(outputFormat.equals(ReportOutputFormat.PDF)){
            //JasperExportManager.exportReportToPdfFile(jasper_print,path_to_export_file);
        	JRPdfExporter exporter=new JRPdfExporter();
            exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME, pathToFile);
            exporter.setParameter(JRPdfExporterParameter.IS_128_BIT_KEY, true);
            exporter.setParameter(JRPdfExporterParameter.IS_ENCRYPTED, false);
            exporter.setParameter(JRPdfExporterParameter.METADATA_AUTHOR, "NMT Group");
            exporter.setParameter(JRPdfExporterParameter.METADATA_CREATOR, "NMT Group");
            exporter.setParameter(JRPdfExporterParameter.METADATA_KEYWORDS, "Report");
            //exporter.setParameter(JRPdfExporterParameter.METADATA_SUBJECT, "меcто для темы");
            exporter.setParameter(JRPdfExporterParameter.METADATA_TITLE, "NMT Group Report");
            exporter.exportReport();
            returnValue=true;
        }else
        if(outputFormat.equals(ReportOutputFormat.XLS)){
            JExcelApiExporter exporter=new JExcelApiExporter();
            exporter.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JExcelApiExporterParameter.OUTPUT_FILE_NAME, pathToFile);
            exporter.exportReport();
            returnValue=true;
        }else 
        if(outputFormat.equals(ReportOutputFormat.RTF)){
            JRRtfExporter exporter=new JRRtfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, pathToFile);
            exporter.exportReport();
            returnValue=true;
        }else
        if(outputFormat.equals(ReportOutputFormat.HTML)){
        	//JasperExportManager.exportReportToHtmlFile(jasper_print,path_to_export_file);
        	JRHtmlExporter exporter=new JRHtmlExporter();
        	exporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRHtmlExporterParameter.OUTPUT_FILE_NAME, pathToFile);
            exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
            exporter.exportReport();
        }
        return returnValue;
	}

	/**
	 * получить имя файла (jrxml), на основании номера отчета 
	 * @param connection текущее соединение с базой данных
	 * @param report_id номер отчета, по которому нужно получить запрос
	 * @return имя файла, который нужно запустить
	 */
	private String getFileName(Connection connection,
									  int report_id){
		String return_value=null;
		try{
			// FIXME OfficePrivatePartnerReporter query: получить имя report файла
			String query="select * from "+ConnectWrap.schemePrefix+"VC_CONTACT_PRS_JOF_REPORTS_ALL where id_report="+report_id;
			// String query="select * from "+ConnectWrap.schemePrefix+"VC_USER_REPORTS_ALL where id_report="+report_id;
			// field_logger.debug(query);
			ResultSet resultset=connection.createStatement().executeQuery(query);
			if(resultset.next()){
				return_value=resultset.getString("EXEC_FILE");
			}
			resultset.getStatement().close();
		}catch(SQLException ex){
			// field_logger.error("Error in get Query report"+ex.getMessage());
			logger.error("get FileName Exception: "+ex.getMessage());
		}
		return return_value;
	}

	/** 
	 * отработать запрос и вернуть ResultSet - для простого отчета<br>
	 * отработать все запросы, поместить их в параметры и вернуть null - для мультизапроса
	 * @param connection - текущее соединение с базой данных
	 * @param query - массив который содержит все запросы к базе данных
	 * @param parameters - HashMap, который содержит все параметры для отчета <br>
	 * <i> в параметры кладем отчеты и именами</i>
	 * @return null - обычный отчет <br>
	 *         ResultSet - мультиотчет
	 */
	private ResultSet executeQueryAndAddParameter(Connection connection,
														 ArrayList<ReportExecuterQueryParameters> query, 
														 HashMap<String,Object> parameters,
														 String path_to_reports) throws SQLException{
		ResultSet return_value=null;
		if(query.size()==1){
			// обычный отчет
			if(query.get(0).getQuery()==null){
				logger.debug("у данного отчета нет ни одного запроса"+query.get(0).getQuery());
				return_value=null;
			}else{
				logger.debug("Попытка выполнения единственного запроса: "+query.get(0).getQuery());
				System.out.println("Try Execute Query: "+query.get(0).getQuery());
				return_value=connection.createStatement().executeQuery(query.get(0).getQuery());
			}
		}else{
			logger.debug("Мультиотчет:");
			parameters.put("SUBREPORT_DIR", path_to_reports);
			// отработать все отчеты
			for(int counter=0;counter<query.size();counter++){
				logger.debug("Execute query: "+counter+"/"+query.size()+"   "+query.get(counter).getQuery());
				query.get(counter).setResultSet(connection.createStatement().executeQuery(query.get(counter).getQuery()));
				parameters.put("SHOW_REPORT_"+(counter+1), new Boolean(true));
				logger.debug("SHOW_REPORT_"+(counter+1));
				parameters.put("DATASOURCE_"+(counter+1), new JRResultSetDataSource(query.get(counter).getResultSet()));
				// logger.debug("DATASOURCE_"+(counter+1)+"   "+resultset);
			}
			// положить ссылки на ResultSEt в параметры отчета
			
		}
		return return_value;
	}
	
}
