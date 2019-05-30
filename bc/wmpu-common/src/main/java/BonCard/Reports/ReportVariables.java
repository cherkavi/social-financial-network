package BonCard.Reports;
import java.sql.*;

import org.apache.log4j.*;

import BonCard.DataBase.UtilityConnector;
import bc.connection.Connector;
import bc.objects.bcObject;




import java.util.*;
/**
 * Данный класс предназначен для получения всех переменных по заданному Отчету  
 * @author cherkashinv
 */
public class ReportVariables {
	private final static Logger LOGGER=Logger.getLogger(ReportVariables.class);
	private bcObject object = new bcObject();
	
	/** уникальный номер для отчета*/
	private String field_report_id="";
	/** наименование отчета в виде строки */
	private String field_report_name="";
	/** наименование отчета в виде строки */
	private String field_report_notes="";
	private String field_report_state="";
	/** путь к контексту данного Web ресурса (для добавления полного пути к изображениям)*/
	//private String field_path_to_web_context;

	private String field_date_format="";
	private String sessionId;
	
	/** все переменные по данному отчету*/
	private ArrayList<Variable> field_variables; 
	/**
	 * Получение всех переменных по отчету
	 * @param report_id - уникальный номер отчета из базы данных
	 * @param connection - текущее соединение с базой данных
	 * @param 
	 */
	
	private String generalDBScheme = ""; 
	
	public ReportVariables(String report_id,
						   //String path_to_web_context,
						   String pSessionId,
						   String pDateFormat){
		LOGGER.debug("Report_ID:"+report_id);
		this.field_report_id=new String(report_id);
		//this.field_path_to_web_context=path_to_web_context;
		this.field_date_format = pDateFormat;
		this.sessionId = pSessionId;
		object.setSessionId(pSessionId);
		this.generalDBScheme = object.getGeneralDBScheme();
		LOGGER.debug("ReportVariables.generalDBScheme=" + this.generalDBScheme);
		
		Connection connection=null;
		try{
			connection=Connector.getConnection(pSessionId);
			initVariables(connection);
		}finally{
			UtilityConnector.closeQuietly(connection);
		}
		
	}
	
	private void addFromResultSetToArrayList(Connection connection, ResultSet resultset, ArrayList<Variable> list){
		try{
			list.add(new Variable(connection,
					  this.field_report_id,
				  	  resultset.getString("CD_REPORT_PARAM"),
				  	  resultset.getString("TYPE_PARAM"),
				  	  resultset.getString("DEFAULT_VALUE"),
				  	  resultset.getString("TEXT_IN_REPORT"),
				  	  resultset.getString("ENTER_TYPE"),
				  	  resultset.getString("SQL_TEXT"),
				  	  resultset.getString("SQL_TEXT_GET_VALUE"),
				  	  resultset.getString("MYLTI_CHOISE"),
				  	  resultset.getString("REQUIRED"),
				  	  resultset.getString("ORDER_NUMBER"),
				  	  resultset.getString("ONLOAD"),
				  	  resultset.getString("LENGTH_IN_FORM"),
				  	  //this.field_path_to_web_context,
				  	  this.field_date_format,
				  	  this.sessionId
				  	  ));
		}catch(SQLException ex){
			LOGGER.error("Error in add Variable:"+ex.getMessage());
		}
	}
	
	public void addStringVariable(Connection connection, String pVariableFormName, String pOrderNumber){
		this.field_variables.add(new Variable(connection,
					  this.field_report_id,
					  pVariableFormName,
				  	  "STRING",
				  	  "",
				  	  "",
				  	  "INPUT",
				  	  "",
				  	  "",
				  	  "N",
				  	  "N",
				  	  pOrderNumber,
				  	  "N",
				  	  "",
				  	  //this.field_path_to_web_context,
				  	  this.field_date_format,
				  	  this.sessionId
				  	  ));
	}
	/**
	 * метод, который инициализирует все переменные по данному отчету
	 */
	private boolean initVariables(Connection connection){
		boolean return_value=false;
		String mySQL = "select * from " + this.generalDBScheme + ".VC_USER_REPORTS_PARAM_ALL where id_report="+this.field_report_id+" order by order_number";
		// получить имя отчета, по которому собираются/генерируются данные переменные
		getReportNameFromDataBase(connection, this.field_report_id);
		
		ResultSet resultset=null;
		// получить все переменные
		try{
			LOGGER.debug("get parameters of report #"+this.field_report_id);
			
			resultset=connection.createStatement().executeQuery(mySQL);
			LOGGER.debug("result set is get");
			if(resultset.next()){
				LOGGER.debug("parameters into Query exists");
				this.field_variables=new ArrayList<Variable>();
				this.addFromResultSetToArrayList(connection, resultset, this.field_variables);
				while(resultset.next()){
					this.addFromResultSetToArrayList(connection, resultset, this.field_variables);
				}
				LOGGER.debug("All parameters:"+this.field_variables.size());
			}else{
				LOGGER.debug("no parameter into Query #"+this.field_report_id);
			}
			return_value=true;
			
		}catch(SQLException ex){
			LOGGER.error("initVariables SQLException:"+ex.getMessage(), ex);
		}catch(Exception e){
			LOGGER.error("initVariables Exception:"+e.getMessage(), e );
		}finally{
			UtilityConnector.closeQuietly(resultset);
		}
		return return_value;
	}
	
	/** получить наименование отчета для 
	 * @param connection - текущее соединение с базой данных
	 * @param report_id - номер отчета
	 */
	private void getReportNameFromDataBase(Connection connection, String report_id) {
		//String return_value="";
		ResultSet resultset=null;
		try{
			resultset=connection.createStatement().executeQuery("SELECT name_report, notes_report, cd_report_state FROM " + this.generalDBScheme + ".vc_user_reports_all WHERE id_report ="+report_id);
			if(resultset.next()){
				this.field_report_name=resultset.getString("NAME_REPORT");
				this.field_report_notes=resultset.getString("NOTES_REPORT");
				this.field_report_state=resultset.getString("CD_REPORT_STATE");
			}
		}catch(SQLException ex){
			LOGGER.error("getReportNameFromDataBase: SQLException:"+ex.getMessage());
		}catch(Exception ex){
			LOGGER.error("getReportNameFromDataBase:    Exception:"+ex.getMessage());
		}finally{
			UtilityConnector.closeQuietly(resultset);
		}
	}
	
	/** возвращает кол-во переменных для заданного отчета*/
	public int getVariablesCount(){
		if(this.field_variables!=null){
			return this.field_variables.size();
		}else{
			return 0;
		}
	}
	/** возвращает переменную для заданого отчета по ее номеру
	 * @return null, если нет переменных, индекс вне диапазона, в случае ошибки 
	 */
	public Variable getVariable(int number){
		if(this.field_variables!=null){
			if((number>=0)&&(number<this.field_variables.size())){
				return this.field_variables.get(number);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	/** 
	 * @return возвращает JavaScript код, который необходимо добавить в функцию HTML.onLoad
	 * для всех переменных 
	 */
	public String getOnLoadJavaScript(){
		StringBuilder return_value=new StringBuilder();
		// получить данные для добавления в метод OnLoad
		for(int counter=0;counter<this.getVariablesCount();counter++){
			return_value.append(this.getVariable(counter).getOnLoadJavaScript());
		}
		// просмотреть на наличие в пакете переменной, которая бы нуждалась в инициализации при загрузке страницы
		for(int counter=0;counter<this.getVariablesCount();counter++){
			if(this.getVariable(counter).isOnLoad()){
				LOGGER.debug("Добавлена функция, которая автоматически должна загрузить выбранное значение из списка");
				return_value.append(this.getVariable(counter).getSelectChangeFunctionName()+"(document.getElementById('"+this.getVariable(counter).getUniqueHTMLName()+"'))");
				break;// такая переменная может быть только одна
			}
		}
		
		return return_value.toString();
	}
	/**
	 * получить текстовое название отчета, по которому сгенерированы все переменные 
	 */
	public String getReportName(){
		return this.field_report_name;
	}
	
	public String getReportNotes(){
		String return_value = "";
		if (!(this.field_report_notes==null || 
				"".equalsIgnoreCase(this.field_report_notes) || 
				"null".equalsIgnoreCase(this.field_report_notes))) {
			return_value = "<center><font size=\"0\">" + this.field_report_notes + "</font></center>";
		}
		return return_value;
	}
	
	public String getReportState(){
		return this.field_report_state;
	}
}
