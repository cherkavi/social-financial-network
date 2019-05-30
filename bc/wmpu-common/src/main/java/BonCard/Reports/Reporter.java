package BonCard.Reports;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import bc.objects.bcUserObject;
import bc.servlet.JndiParamHttpServlet;

/**
 *  Данный класс служит для вывода отчетов
 */
public class Reporter extends JndiParamHttpServlet{
	private final static Logger LOGGER=Logger.getLogger(Reporter.class);
	
	private static final long serialVersionUID = 1L;
	/** имя параметра, который содержит номер отчета */
	private String field_request_report_id="REPORT_ID";
	/** тип файла для вывода */
	private String field_request_report_format="REPORT_FORMAT";
	/** объект из всех переменных по данному отчету */
	private ReportVariables field_variables;
	/** путь к файлам с Jasper/JRXML файлами */
	private String field_path_to_report=null;
	/** путь к файлам-отчетам, которые были предоставлены пользователю*/
	private String field_path_to_out=null;
	
    /**
     * Default constructor. 
     */
    public Reporter() {
    	// Constructor
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//super.doGet(request, response);
		LOGGER.debug("Reporter:GET");
		doProcess(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//super.doPost(request, response);
		LOGGER.debug("Reporter:POST");
		doProcess(request,response);
		
	}
	/** метод по обработке запросов */
	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.setLevel(Level.DEBUG);
		LOGGER.debug("Начало обработки запроса");
		//this.field_connection=this.getConnection(request, request.getSession().getId());
		if(isValidParameters(request)){
			LOGGER.debug("request parameter's is valid ");
			this.outReport(request, response,this.field_variables);
		}else{
			LOGGER.debug("mistake in request parameter's");
			this.outErrorPage(request, response);
		}
		//this.closeConnection();
		LOGGER.debug("Окончание обработки запроса");
	}
	

	/** метод по обработке запросов */
	public boolean generateReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("Начало генерации запроса");
		//this.field_connection=this.getConnection(request, request.getSession().getId());
		if(isValidParameters(request)){
			LOGGER.debug("request parameter's is valid ");
			this.outReport(request, response, this.field_variables);
			return true;
		}else{
			return false;
		}
	}
	/** 
	 * метод по проверке валидности всех передаваемых значений
	 * 
	 */
	private boolean isValidParameters(HttpServletRequest request){
		boolean return_value=true;
		LOGGER.debug("попытка получения параметров запроса");
		/*
		 Enumeration parameter=request.getParameterNames();
		String current_key=null;
		String value=null;
		while(parameter.hasMoreElements()){
			try{
				current_key=(String)parameter.nextElement();
				value=request.getParameter(current_key);
			}catch(Exception ex){
				LOGGER.warn("Ошибка при получении значения по параметру: "+current_key);
			}
		}*/
		LOGGER.debug(this.field_request_report_id+":"+request.getParameter(field_request_report_id));
		LOGGER.debug(this.field_request_report_format+":"+request.getParameter(field_request_report_format));
		if(   (request.getParameter(field_request_report_id)!=null)
			&&(request.getParameter(field_request_report_format)!=null)
			//&&(this.field_connection!=null)
			){
			LOGGER.debug("получить все переменные по данному запросу");
			field_variables=new ReportVariables(request.getParameter(field_request_report_id),
												request.getSession().getId(),
												null);
			LOGGER.debug("Всего переменных по отчету №"+request.getParameter(field_request_report_id)+" = "+field_variables.getVariablesCount());
			if(requiredVariablesPresent(request,field_variables)==true){
				LOGGER.debug("All required variables present");
				return_value=true;
			}else{
				LOGGER.debug("required variables NOT FOUND");
				return_value=false;
			}
		}else{
			LOGGER.debug("Report_id or Report_format is not defined");
			return_value=false;
		}
		//this.closeConnection();
		return return_value;
	}
	
	/** проверить HttpServletRequest на наличие всех обязательных параметров */
	private boolean requiredVariablesPresent(HttpServletRequest request, ReportVariables variables){
		boolean return_value=true;
		/*
		for(int counter=0;counter<variables.getVariablesCount();counter++){
			if(  variables.getVariable(counter).isRequired()){
				// required field
				if(request.getParameter(variables.getVariable(counter).getUniqueHTMLName())!=null){
					// required field present
					LOGGER.debug("Required Variable is present:"+variables.getVariable(counter).getUniqueHTMLName()+"   "+request.getParameter(variables.getVariable(counter).getUniqueHTMLName()));
				}else{
					// required field absent
					LOGGER.error("Required Variable not present:"+variables.getVariable(counter).getUniqueHTMLName());
					return_value=false;
				}
			}else{
				// field not required
			}
		}
		*/
		return return_value;
	}
	/**
	 * получить путь к каталогу с Jasper файлами
	 * @return
	 */
	private String getPathToReport(){
		if(this.field_path_to_report==null){
			this.field_path_to_report=this.getInitParameter("path_to_report");
		}
		return this.field_path_to_report;
	}
	
	/**
	 * получить путь к готовым файлам отчетам
	 */
	private String getPathToOut(){
		if(this.field_path_to_out==null){
			this.field_path_to_out=this.getInitParameter("path_to_out");
			System.out.println("path_to_out="+this.field_path_to_out);
		}
		return this.field_path_to_out;
	}
	
	/**
	 * создание уникального имени файла
	 */
	private String getUniqueFileName(HttpServletRequest request){
		return this.getPathToOut()+request.getSession().getId()+System.currentTimeMillis()+".";
	}
	/**
	 * Отобразить отчет для пользователя:
	 * @param request - ServletHttpRequest для данного запроса
	 * @param response - ServletHttpResponse для данного запроса
	 * @param connection - соединение с базой данных
	 * @param field_variables - переменные по данному отчету
	 */
	private void outReport(HttpServletRequest request, 
						   HttpServletResponse response, 
						   ReportVariables field_variables) throws ServletException, IOException {
		bcUserObject loginUser = new bcUserObject();
		loginUser.getCurrentUserFeature();
		LOGGER.debug("Show report BEGIN");
        // сгенерировать уникальное имя файла для сохранения отчета
		String path_to_file=this.getUniqueFileName(request)+request.getParameter("REPORT_FORMAT");
        LOGGER.debug("создать файл-отчет для вывода данных");
        if(Jasper.CreateReport(request.getParameter(field_request_report_id),
        					   request, 
        					   response, 
        					   field_variables,
        					   this.getPathToReport(),
        					   path_to_file,
        					   loginUser)==true){
            LOGGER.debug("установить тип файла для вывода данных");
            
            String attached_filename = "report_" + request.getParameter(field_request_report_id) + "_" + System.currentTimeMillis() + "." + request.getParameter("REPORT_FORMAT");
    		// задать тип значения 
            if(request.getParameter("REPORT_FORMAT").equals("HTML")){
            	//response.setContentType("text/html");//+request.getParameter("REPORT_FORMAT"));
            	//response.addHeader("Content-Disposition", "attachment; filename=report."+request.getParameter("REPORT_FORMAT"));
            }else{
            	response.setContentType("application/"+request.getParameter("REPORT_FORMAT"));
            	//response.addHeader("Content-Disposition", "attachment; filename=report."+request.getParameter("REPORT_FORMAT"));
            	response.addHeader("Content-Disposition", "attachment; filename="+attached_filename);
            }
        	LOGGER.debug("получить контекст для вывода данных");
            ServletOutputStream servlet_out=response.getOutputStream();
        	File export_file=new File(path_to_file);
            response.setContentLength((int)export_file.length());
            BufferedInputStream buffer=new BufferedInputStream(new FileInputStream(export_file));    
            servlet_out=response.getOutputStream();
            LOGGER.debug("вывести файл отчета");
            this.BufferedInputStreamToStream(buffer, servlet_out);
            LOGGER.debug("export done");
            servlet_out.flush();
            servlet_out.close();
    		LOGGER.debug("Show report END");
        }else{
        	LOGGER.error("файл-отчет не создан");
        	this.outErrorPage(request, response);
        }
	}
	
	/**
	 * Копирование из буфера ввода в вывод сервлета
	 * @param buffer
	 * @param servlet_out
	 */
	private void BufferedInputStreamToStream(BufferedInputStream buffer,
											 ServletOutputStream servlet_out) throws ServletException,IOException {
        byte[] read_bytes=new byte[1000];
        int byte_count=0;
        while( (byte_count=buffer.read(read_bytes))!=-1){
            servlet_out.write(read_bytes,0,byte_count);
        }
	}

	/**
	 * Вывести пользователю страницу ошибки  
	 */
	private void outErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		
			response.setContentType("text/html");
	 
	      	String title = "Error/Exception Information";
	      	String docType =
	      		"<!doctype html public \"-//w3c//dtd html 4.0 " +
	      		"transitional//en\">\n";
	      	out.println(docType +
	      			"<html>\n" +
	      			"<head><title>" + title + "</title></head>\n" +
	      		"<body bgcolor=\"#f0f0f0\">\n");

	      	out.println("<h2>Error information</h2>");
	      	out.println(Jasper.getErrorMessage());
	      	out.println("</body>");
	      	out.println("</html>");
	}
}