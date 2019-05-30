package bonclub.reports.web_service.implementation;


import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import bonclub.reports.ReportExecuterParameters;
import bonclub.reports.engine.ReportEngine;
import bonclub.reports.web_service.common.RemoteReportFileDescription;
import bonclub.reports.web_service.interf.IReporter;
import database.ConnectWrap;
import database.StaticConnector;

/** реализация установки в очередь очередного задания от удаленного клиента */
public class ReporterImplementation implements IReporter{
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	@Override
	public boolean addReportForProcess(byte[] byteArray) {
		boolean returnValue=false;
		logger.debug("распарсить параметры");
		ReportExecuterParameters parameters=new ReportExecuterParameters();
		ByteArrayInputStream inputStream=null;
		try{
			while(true){
				inputStream=new ByteArrayInputStream(byteArray);
				if(parameters.loadFromByteArray(inputStream)==false){
					logger.error("ошибка заргузки данных от удаленного клиента");
					returnValue=false;
					break;
				}
				logger.debug("данные успешно загружены от удаленного клиента и парсинг параметров прошел успешно ");
				
				String fileName=this.saveByteArrayToFile(byteArray);
				if(fileName==null){
					logger.error("ошибка сохранения данных в файле (ошибка маршалинга) ");
					returnValue=false;
					break;
				}
				logger.debug("параметры успешно сохранены во внешний файл: "+fileName);
				
				// сохранить отчет в базу данных
				if(saveToDatabase(parameters, fileName)==false){
					logger.error("ошибка получения данных ");
					returnValue=false;
					break;
				}
				logger.info("данные успешно сохранены ");
				// INFO OfficePrivatePartnerReporter оповещение для движка отчетов о наличии новых отчетов  
				ReportEngine.notifyAboutNewReport();
				returnValue=true;
				break;
			}
		}catch(Exception ex){
			logger.error("ошибка чтения/парсинга данных ");
		}finally{
			IOUtils.closeQuietly(inputStream);
		}
		return returnValue;
	}
	
	/** сохранить полученные данные в базу данных 
	 * @param parameters - параметры, которые получены от клиента 
	 * @param fileName - имя файла, в котором данные параметры сохранены 
	 * @return 
	 * <ul>
	 * 	<li><b>true</b> запись успешно сохранена в базе данных </li>
	 * 	<li><b>false</b> ошибка сохранения данных в базе  </li>
	 * </ul>
	 * 
	 * 
	 <table>
	 <tr> <td colspan="3">sys_reports_task ( </td></tr>
    	<tr><td>id_report_task                 </td><td>NUMBER NOT NULL,             </td><td>-- ІД завдання на створення звіту</td></tr>
    	<tr><td>id_report                      </td><td>NUMBER NOT NULL,             </td><td>-- ІД звіту</td></tr>
    	<tr><td>id_login_user                  </td><td>NUMBER NOT NULL,             </td><td>-- ІД контактного лица в масштабе партнера </td></tr>
    	<tr><td>parameters_filename            </td><td>VARCHAR2(250),               </td><td>-- Назва файлу параметрів звіту</td></tr>
    	<tr><td>report_filename                </td><td>VARCHAR2(250),               </td><td>-- Назва файлу звіту</td></tr>
    	<tr><td>cd_report_task_state           </td><td>VARCHAR2(100) NOT NULL,      </td><td>-- Код стану завдання на створення звіту</td></tr>
    	<tr><td>creation_date                  </td><td>DATE NOT NULL,               </td><td>-- Дата створення запису</td></tr>
    	<tr><td>created_by                     </td><td>NUMBER(10,0) NOT NULL,       </td><td>-- Ким створено запис</td></tr>
    	<tr><td>last_update_date               </td><td>DATE NOT NULL,               </td><td>-- Дата останньої модифікацї запису</td></tr>
    	<tr><td>last_update_by                 </td><td>NUMBER(10,0) NOT NULL        </td><td>-- Ким останній раз модифіковано запис</td></tr>
	<tr><td colspan="3">)</td></tr>
	</table>
	 */
	
	/** сохранить полученное задание от удаленного сервера в базу данных как новое задание для "создателя" отчетов 
	 * @param parameters - параметры, которые получаем из 
	 * @param fileName
	 * @return
	 */
	private boolean saveToDatabase(ReportExecuterParameters parameters,
								   String fileName) {
		boolean returnValue=false;
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			Connection connection=connector.getConnection();
			// создать очередное уникальное значение
			int id=getNextSequenceForTable(connection, ConnectWrap.schemePrefix+"seq_reports_task");
			// вставить данное значение в запрос
			/*
			 *      id_report_task                 NUMBER NOT NULL,             -- ІД завдання на створення звіту
    			    id_report                      NUMBER NOT NULL,             -- ІД звіту
    				id_login_user                  NUMBER NOT NULL,             -- ІД (контактного лица на партнере) пользователя не реализованного в системе  
    				parameters_filename            VARCHAR2(250),               -- Назва файлу параметрів звіту
    				report_filename                VARCHAR2(250),               -- Назва файлу звіту
    				cd_report_task_state           VARCHAR2(100) NOT NULL,      -- Код стану завдання на створення звіту
			 */
			PreparedStatement ps=connection.prepareStatement("insert into "+ConnectWrap.schemePrefix+"sys_reports_task(id_report_task, id_report, id_login_user,parameters_filename, report_filename, cd_report_task_state ) values(?,?,?,?,?,?)");
			ps.setInt(1, id);// id_report_task
			ps.setInt(2, parameters.getReportId());// id_report
			ps.setInt(3, parameters.getUserId());// id_login_user
			ps.setString(4, fileName); // pararmeters_filename
			ps.setNull(5, Types.VARCHAR ); // report_filename
			ps.setString(6, "NEW"); // cd_report_task_state - состояние 
			ps.executeUpdate();
			connection.commit();
			returnValue=true;
		}catch(Exception ex){
			logger.error("сохранение записи в базе данных не осуществлено "+ex.getMessage());
		}finally{
			connector.close();
		}
		return returnValue;
	}


	/** получить очередное значение из Sequence */
	private int getNextSequenceForTable(Connection connection, String fullSequenceName) throws SQLException{
		Statement statement=connection.createStatement();
		try{
			// SELECT bc_admin.seq_reports_task.NEXTVAL FROM dual;
			ResultSet rs=statement.executeQuery("SELECT "+fullSequenceName+".NEXTVAL FROM dual");
			if(rs.next()){
				return rs.getInt(1);
			}else{
				return 0;
			}
		}finally{
			try{
				statement.close();
			}catch(Exception ex){};
		}
	}
	
	/** сохранить массив байт в файл в операционной системе
	 * @param array - массив из байт, который содержит сериализованные данные
	 * @return 
	 * <ul>
	 * 	<li><b>String</b> - полный путь к файлу, в котором будет сохранен объект </li>
	 * 	<li><b>null</b>ошибка сохранения данных в файл </li>
	 * </ul>
	 */
	private String saveByteArrayToFile(byte[] array){
		String fileName=this.getFileName();
		try{
			FileOutputStream fos=new FileOutputStream(ReportExecuterParameters.pathToParameters+fileName);
			fos.write(array);
			fos.flush();
			fos.close();
			return fileName;
		}catch(Exception ex){
			logger.error("Сохранение массива байт в файл Exception: "+ex.getMessage());
			return null;
		}
	}
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_");
	private Random random=new Random();
	
	/** получить уникальное имя файла, случайно сгенерированное */
	private String getFileName(){
		return sdf.format(new Date())+Integer.toHexString(random.nextInt(1000))+".bin";
	}

	@Override
	public RemoteReportFileDescription getReportUrl(Integer userId, Integer reportId) {
		if((userId==null)||(reportId==null)){
			return null;
		}
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from "+ConnectWrap.schemePrefix+"sys_reports_task where id_report_task="+reportId+" and id_login_user="+userId);
			if(rs.next()){
				String reportFileName=rs.getString("REPORT_FILENAME");
				if(reportFileName!=null){
					RemoteReportFileDescription returnValue=new RemoteReportFileDescription();
					returnValue.setUrl(this.getFullUrlToFile(userId, reportId, reportFileName));
					returnValue.setFormat(this.getFileExtension(reportFileName));
					return returnValue;
				}else{
					logger.error("getReportUrl request a report that is not ready ");
				}
			}else{
				logger.error("getReportUrl UserId:"+userId+"  reportId:"+reportId+"  does not found ");
				return null;
			}
		}catch(Exception ex){
			logger.error("getReportUrl Exception: "+ex.getMessage());
		}finally{
			connector.close();
		}
		return null;
	}
	
	/** получить расширение файла, полагая что расширение файла начинается после последней точки */
	private String getFileExtension(String reportFileName) {
		int dotIndex=reportFileName.lastIndexOf(".");
		return reportFileName.substring(dotIndex+1);
	}

	/** получить полный путь к файлу на основании имени отчета  
	 * @param userId - уникальный номер клиента по данному отчету  
	 * @param reportId - уникальный номер отчета
	 * @param reportFileName - имя файла, сформированного отчета 
	 * @return полный URL для доступа
	 */
	private String getFullUrlToFile(Integer userId, Integer reportId, String reportFileName){
		return ReportExecuterParameters.urlToReportServlet+"?user_id="+userId+"&report_id="+reportId;
	}
}
