package bonclub.reports;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import database.ConnectWrap;
import database.StaticConnector;

/**
 * сервлет, который по запросам HTTP выдает файлы 
 */
public class ReportDownloader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(this.getClass());
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	
	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// INFO OfficePrivatePartnerReporter место загрузки
		Integer userId=getIntegerFromString(request.getParameter("user_id"));
		Integer reportId=getIntegerFromString(request.getParameter("report_id"));
		
		logger.debug("запрос на получение файла: UserId:"+userId+"   ReportId: "+reportId);
		
		if((reportId!=null)&&(userId!=null)){
			String fileName=this.getReportFileName(userId, reportId);
			if(fileName!=null){
				logger.debug("request report: "+reportId+"   fileName: "+fileName);
				String pathToFile=ReportExecuterParameters.pathToOutputReport+fileName;
				String fileExtension=this.getExtensionFromFileName(fileName);
            	File export_file=new File(pathToFile);

            	if(export_file.exists()){
            		logger.debug("файл существует:"+pathToFile);
    				response.setContentType("application/"+fileExtension);
                	response.addHeader("Content-Disposition", "attachment; filename=report."+fileExtension);
                    ServletOutputStream servlet_out=response.getOutputStream();
                    response.setContentLength((int)export_file.length());
                    BufferedInputStream buffer=new BufferedInputStream(new FileInputStream(export_file));    
                    logger.debug("вывести файл отчета");
                    this.bufferedInputStreamToStream(buffer, servlet_out);
                    logger.debug("export done");
                    servlet_out.close();
            	}else{
            		logger.debug("файл НЕ существует: "+pathToFile);
    				response.setContentType("application/"+fileExtension);
                	response.addHeader("Content-Disposition", "attachment; filename=erro_report.html");
                	String outputBody="<html><head><title>FILE NOT FOUND </title></head> <body><h1>file is not found, please notify administrator ("+reportId+")</h1></body></html>";
                	ServletOutputStream servlet_out=response.getOutputStream();
                	response.setContentLength(outputBody.getBytes().length);
                	servlet_out.write(outputBody.getBytes());
                	servlet_out.flush();
                	servlet_out.close();
            	}
				
			}else{
				logger.error("Request for report wich does not preapre: UserId:"+userId+"  reportId:"+reportId);
			}
		}else{
			logger.error("Parameter 'report_id' and/or 'user_id' does not recognized into request ");
		}
	}
	
	/** получить расширение файла из его полного имени 
	 * @param fileName - полное имя файла 
	 * @return имя после последнего символа "точка"
	 *  */
	private String getExtensionFromFileName(String fileName) {
		int dotPosition=fileName.lastIndexOf(".");
		if(dotPosition>0){
			return fileName.substring(dotPosition+1);
		}else{
			return "";
		}
	}

	private Integer getIntegerFromString(String value){
		try{
			return Integer.parseInt(value);
		}catch(Exception ex){
			return null;
		}
	}
	
	/** получить имя файла по номеру отчета  
	 * @param userId - уникальный идентификатор пользователя 
	 * @param reportId - уникальный идентификатор отчета 
	 * @return строку с именем файла, в котором содержится сформированный отчет 
	 */
	private String getReportFileName(Integer userId, Integer reportId){
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select * from "+ConnectWrap.schemePrefix+"sys_reports_task where id_report_task="+reportId+" and id_login_user="+userId);
			if(rs.next()){
				return rs.getString("REPORT_FILENAME");
			}else{
				logger.error("getReportUrl UserId:"+userId+"  reportId:"+reportId+"  does not found ");
				return null;
			}
		}catch(Exception ex){
			logger.error("getReportUrl Exception: "+ex.getMessage());
			return null;
		}finally{
			connector.close();
		}
		
	}

	/**
	 * Копирование из буфера ввода в вывод сервлета
	 * @param buffer
	 * @param servlet_out
	 */
	private void bufferedInputStreamToStream(BufferedInputStream buffer,
											 ServletOutputStream servlet_out) throws ServletException,IOException {
        byte[] read_bytes=new byte[1024];
        int byte_count=0;
        while( (byte_count=buffer.read(read_bytes))!=-1){
            servlet_out.write(read_bytes,0,byte_count);
        }
	}
	
}
