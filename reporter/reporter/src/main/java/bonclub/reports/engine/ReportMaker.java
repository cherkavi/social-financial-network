package bonclub.reports.engine;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import bonclub.reports.ReportExecuterParameters;
import database.ConnectWrap;
import database.StaticConnector;

/** объект, который выполняет отчеты и сохраняет эти отчеты во внешний файл  */
public class ReportMaker extends Thread {
	private Logger logger=Logger.getLogger(this.getClass().getName());
	private volatile boolean flagRun=true;
	private ReportExecuter reportExecuter=new ReportExecuter(ReportExecuterParameters.pathToPattern);
	
	/** остановить выполнение  */
	public void stopThread(){
		this.flagRun=false;
		this.interrupt();
	}
	
	/** оповестить о наличии новой задачи - нового отчета для обработки */
	public void notifyAboutNewReport(){
		logger.debug("оповещение о наличии новых отчетов ");
		synchronized(this){
			this.notify();
		}
	}
	
	@Override
	public void run(){
		// ReportExecuterParameters.pathToOutputReport;
		// ReportExecuterParameters.pathToParameters
		// ReportExecuterParameters.pathToPattern
		main_cycle:while(flagRun){
			if(isTaskExists()){
				processTask();
			}else{
				synchronized(this){
					if(isTaskExists()){
						logger.debug("задача только что была поставлена в очередь ");
						continue main_cycle;
					}else{
						logger.info("нет задач - засыпание");
						try{
							this.wait();
						}catch(InterruptedException ie){};
					}
				}
			}
		}
	}
	
	/** место выполнения всех входящих отчетов  */
	private void processTask() {
		logger.debug("получить номера отчетов, которые нужно выполнить ");
		ArrayList<Integer> listOfReport=new ArrayList<Integer>();
		ConnectWrap connector=StaticConnector.getConnectWrap();
		ResultSet rs=null;
		try{
			// INFO OfficePrivatePartnerReporter query: получить отчеты для выполнения
			rs=connector.getConnection().createStatement().executeQuery("select ID_REPORT_TASK from "+ConnectWrap.schemePrefix+"SYS$REPORT_TASK where CD_REPORT_TASK_STATE='NEW'");
			while(rs.next()){
				listOfReport.add(rs.getInt(1));
			}
		}catch(Exception ex){
			logger.error("Error in get Task From Database:"+ex.getMessage());
		}finally{
			ConnectWrap.close(rs);
			connector.close();
		}

		logger.debug("создать отчеты, согласно списку уникальных идентификаторов ");
		for(int counter=0;counter<listOfReport.size();counter++){
			createReport(listOfReport.get(counter));
		}
	}

	/** создать отчет на основании номера отчета из таблицы  */
	private void createReport(Integer idReport){
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from "+ConnectWrap.schemePrefix+"SYS$REPORT_TASK where bc_admin.sys_reports_task.id_report_task="+idReport+" and CD_REPORT_TASK_STATE='NEW'");
			if(rs.next()){
				updateReportAsInProcess(connector, idReport);
				String parameterFilename=rs.getString("PARAMETERS_FILENAME");
				logger.debug(">>>прочесть входные параметры для отчета из файла: "+ReportExecuterParameters.pathToParameters+parameterFilename);
				ReportExecuterParameters parameters=new ReportExecuterParameters();
				if(parameters.loadFromByteArray(new FileInputStream(ReportExecuterParameters.pathToParameters+parameterFilename))){
					logger.debug("Запросы отчета:");
					for(int counter=0;counter<parameters.getQueryOfReports().size();counter++){
						logger.debug("Number:"+counter+"  Query:"+parameters.getQueryOfReports().get(counter).getQuery());
					}
					logger.debug("выполнить отчет");
					String fileName=this.generateUniqueFileName(parameters.getUserId(), 
																parameters.getReportId(), 
																parameters.getOutputFormatStringRepresent());
					reportExecuter.executeReport(connector, 
												ReportExecuterParameters.pathToOutputReport+fileName,
												
												parameters.getReportId(), 
												parameters.getQueryOfReports(), 
												parameters.getOutputFormatStringRepresent(), 
												parameters.getParameters(), 
												parameters.getJasperParameters());
					logger.debug("записать отчет как сделанный"); 
					updateReportAsCreated(connector, idReport, fileName);
				}else{
					logger.error("Ошибка при выполнении отчета :"+ReportExecuterParameters.pathToParameters+parameterFilename);
				}
			}
		}catch(Exception ex){
			logger.error("ошибка выполнения отчета "+idReport+"   Exception:"+ex.getMessage() );
			try{
				updateReportAsError(connector,idReport, ex.getMessage());
			}catch(Exception exInner){
				logger.error("попытка пометить отчет как ошибочно-выполненный завершена неудачно: "+exInner.getMessage());
			}
		}finally{
			connector.close();
		}
	}

	/** пометить отчет как "ошибочный" 
	 * @param connector - соединение с базой данных
	 * @param idReport - уникальный номер отчета
	 * @param errorMessage - текст ошибки, который получен при попытке создании отчета
	 * @throws SQLException
	 */
	private void updateReportAsError(ConnectWrap connector, Integer idReport, String errorMessage) throws SQLException{
		Connection connection=connector.getConnection();
		Statement statement=connection.createStatement();
		if(errorMessage!=null){
			String delimeterMessage=errorMessage;
			if(delimeterMessage.length()>1000){
				delimeterMessage=delimeterMessage.substring(0,1000);
			}
			statement.executeUpdate("update "+ConnectWrap.schemePrefix+"SYS$REPORT_TASK set CD_REPORT_TASK_STATE='ERROR', ERROR_MESSAGE='"+delimeterMessage.replaceAll("'","''")+"' where ID_REPORT_TASK="+idReport);
		}else{
			statement.executeUpdate("update "+ConnectWrap.schemePrefix+"SYS$REPORT_TASK set CD_REPORT_TASK_STATE='ERROR' where ID_REPORT_TASK="+idReport);
		}
		ConnectWrap.close(statement);
		connection.commit();
	}
	
	
	/** пометить отчет как "выполнен" 
	 * @param connector - соединение с базой данных
	 * @param idReport - уникальный номер отчета
	 * @param fileName - имя файла, в котором данный отчет сохранен
	 * @throws SQLException
	 */
	private void updateReportAsCreated(ConnectWrap connector, Integer idReport,
			String fileName) throws SQLException{
		Connection connection=connector.getConnection();
		Statement statement=connection.createStatement();
		try{
			statement.executeUpdate("update "+ConnectWrap.schemePrefix+"SYS$REPORT_TASK set CD_REPORT_TASK_STATE='CREATED', REPORT_FILENAME='"+fileName+"' where ID_REPORT_TASK="+idReport);
			connection.commit();
		}finally{
			ConnectWrap.close(statement);
		}
	}

	/** пометить отчет как "взят для обработки " 
	 * @param connector - соединение с базой данных 
	 * @param reportId - уникальный номер отчета 
	 * @throws SQLException
	 */
	private void updateReportAsInProcess(ConnectWrap connector, Integer reportId) throws SQLException{
		Connection connection=connector.getConnection();
		Statement statement=connection.createStatement();
		try{
			statement.executeUpdate("update "+ConnectWrap.schemePrefix+"SYS$REPORT_TASK set CD_REPORT_TASK_STATE='IN_PROCESS' where ID_REPORT_TASK="+reportId);
			connection.commit();
		}finally{
			ConnectWrap.close(statement);
		}
	}
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_sss");
	private Random random=new Random();
	private String generateUniqueFileName(int clientId, int reportId, String formatString){
		ReportOutputFormat format=ReportOutputFormat.valueOf(formatString);
		String ext=null;
		switch(format){
			case PDF:{ext=".pdf";} break;
			case RTF:{ext=".rtf";} break;
			case HTML:{ext=".html";} break;
			case XLS:{ext=".xls";} break;
			default: {ext=".out";} break;
		}
		return "r_"+clientId+"_"+reportId+"_"+sdf.format(new Date())+"_"+Integer.toHexString(random.nextInt(1000))+ext;
	}
	
	
	/** проверка на появление заданий в таблице bc_admin.sys_reports_task.CD_REPORT_TASK_STATE
	 * @return
	 * <ul>
	 * 	<li><b>true</b> - есть отчеты, который нужно создать </li>
	 * 	<li><b>false</b> - нет задач для выполнения </li>
	 * </ul>
	 * */
	private boolean isTaskExists(){
		logger.debug("проверка на наличие задач в таблице ");
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			//logger.debug("Scheme Name: "+ConnectWrap.schemePrefix);
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select count(*) from "+ConnectWrap.schemePrefix+"SYS$REPORT_TASK where CD_REPORT_TASK_STATE='NEW'");
			rs.next();
			return rs.getInt(1)>0;
		}catch(SQLException ex){
			logger.error("Ошибка получения задач из таблицы ");
			return false;
		}finally{
			connector.close();
		}
	}
}
