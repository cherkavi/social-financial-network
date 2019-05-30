package bc.reports;

import java.io.BufferedReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

import bonclub.reports.ReportExecuterParameters;
import bonclub.reports.ReportExecuterQueryParameters;
import bonclub.reports.web_service.interf.IReporter;
import bonclub.reports.web_service.interf.ServiceFactory;



/** создает отчеты */
public class ReportsCreator {
	private final static Logger LOGGER=Logger.getLogger(ReportsCreator.class);
	// private String pathToReportPattern;
	// private String pathToReportOutput;
	private static String delimeter=":";
	
	/** создает отчеты, используя удаленный WebService 
	 */
	public ReportsCreator(){
		//  * @param pathToReportPattern - путь к отчетам
		//  * @param pathToReportOutput - путь к каталогу, в который нужно помещать отчеты 
		// this.pathToReportPattern=pathToReportPattern;
		// this.pathToReportOutput=pathToReportOutput;
	}
	
	/** создать отчет ( на основании введенных значений в поля )
	 * @param connection - соединение с базой данных
	 * @param userId - уникальный идентификатор пользователя, для которого создается данный отчет 
	 * @param report - отчет, который должен быть создан и сохранен в файле на диске    
	 * @param outputFormatStringRepresent - строковое представление формата выходных данных
	 * @param fullPathToServer - полный путь к WebService (http://localhost:8080/OfficePrivateServerWebService/services) 
	 * @return - полный путь к файлу отчета или null в случае ошибки 
	 * 	<ul>
	 * 		<li><b>true</b> - задача успешно установлена </li>
	 * 		<li><b>false</b> - ошибка установки задачи в очередь </li>
	 * 	</ul>
	 * */
	public boolean createReport(ConnectWrap connector,
							   int userId, 
							   Reports report, 
							   String outputFormatStringRepresent,
							   String fullPathToServer)  throws Exception {
		/** получить формат вывода даты в запрос для заполнения данными */
		fillDateFormat(connector.getConnection());
		
		LOGGER.debug("Получить все параметры запроса: ($P)");
		HashMap<String,Object> parameters=getAllParameters(connector, report);
		LOGGER.debug("Получить все запросы по отчету: ");
		ArrayList<ReportExecuterQueryParameters> queryOfReports=getSqlQuery(connector.getConnection(),report.getReportId());
		LOGGER.debug("наполнить запросы данными из параметров ");
		queryOfReports=fillQueryOfParameters(queryOfReports, parameters);
        /** получить все переменные для передачи их в Jasper Report  */
        HashMap<String, String> jasperParameters=getAllParametersForReport(parameters, connector, report);
        
		// место рассоединения отчета
		/*
		String pathToOutputFile=this.pathToReportOutput+fileName;
		ReportExecuter reportExecuter=new ReportExecuter(this.pathToReportPattern);
		reportExecuter.executeReport(connector,
									pathToOutputFile,
				 	  
									report.getReportId(), 
									queryOfReports, 
									outputFormatStringRepresent, 
									parameters, 
									jasperParameters);
		return pathToOutputFile;*/		 
        ReportExecuterParameters paramForSend=new ReportExecuterParameters();
        paramForSend.setJasperParameters(jasperParameters);
        paramForSend.setOutputFormatStringRepresent(outputFormatStringRepresent);
        paramForSend.setParameters(parameters);
        paramForSend.setQueryOfReports(queryOfReports);
        paramForSend.setReportId(report.getReportId());
        paramForSend.setUserId(userId);
		ServiceFactory<IReporter> serviceFactory=new ServiceFactory<IReporter>(){
			@Override
			public Class<IReporter> getType() {
				return IReporter.class;
			}
		};
		return serviceFactory.getRemoteService(fullPathToServer+"/add_report").addReportForProcess(paramForSend.saveToByteArray());
	}

	
	
	/** все параметры заменить на значения для отчета  
	 * @param parameters - параметры 
	 * @param connector - соединение с базой данных 
	 * @param report - отчет 
	 * @return параметры с замененными значениями 
	 */
	private HashMap<String, String> getAllParametersForReport(HashMap<String,Object> parameters, ConnectWrap connector, Reports report) {
		for(int counter=0;counter<report.getVariables().size();counter++){
			Variable variable=report.getVariables().get(counter);
			parameters.put(variable.getHtmlId(),
						   variable.getValueForOutput(connector, report.getVariables()));
		}
		return convertObjectToJasperRepresent(parameters);
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, String> convertObjectToJasperRepresent(HashMap<String, Object> parameters) {
		HashMap<String,String> returnParameters=new HashMap<String,String>();
		Iterator<String> iterator=parameters.keySet().iterator();
		String nextValue=null;
		while(iterator.hasNext()){
			nextValue=iterator.next();
			Object object=parameters.get(nextValue);
			if(object instanceof String){
				returnParameters.put(nextValue, (String)object);
			}else if(object instanceof ArrayList){
				StringBuffer returnValue=new StringBuffer();
				ArrayList list=(ArrayList)object;
				for(int counter=0;counter<list.size();counter++){
					if(returnValue.length()>0){
						returnValue.append(",");
					}
					if(list.get(counter)!=null){
						returnValue.append("'"+list.get(counter).toString()+"'");
					}
				}
				returnParameters.put(nextValue, returnValue.toString());
			}else if(object instanceof Date){
				returnParameters.put(nextValue, "'"+sqlDateFormat.format((Date)object)+"'");
			}else if(object instanceof Integer){
				returnParameters.put(nextValue, object.toString());
			}else if(object instanceof Float){
				returnParameters.put(nextValue, object.toString());
			}else if(object instanceof Double){
				returnParameters.put(nextValue, object.toString());
			}else{
				// без изменений 
				if(object!=null){
					returnParameters.put(nextValue, object.toString());
				}else{
					returnParameters.put(nextValue, null);
				}
			}
		}
		return returnParameters;
	}

	
	/** наполнить тексты запросов параметрами */
	private ArrayList<ReportExecuterQueryParameters> fillQueryOfParameters(ArrayList<ReportExecuterQueryParameters> queryOfReports,
															 HashMap<String, Object> parameters) {
		for(int counter=0;counter<queryOfReports.size();counter++){
			Iterator<String> keyIterator=parameters.keySet().iterator();
			String nextKey=null;
			while(keyIterator.hasNext()){
				nextKey=keyIterator.next();
				try{
					System.out.println("ReportsCreator#fillQueryOfParameters Query:"+queryOfReports.get(counter).getQuery());
					if(queryOfReports.get(counter).getQuery().indexOf(delimeter+nextKey+delimeter)>=0){
						System.out.println("ReportsCreator#fillQueryOfParameters имеет зависимость ");
						queryOfReports.get(counter).setQuery( queryOfReports.get(counter).getQuery().replaceAll(delimeter+nextKey+delimeter, 
															 													convertObjectToOracleRepresent(parameters.get(nextKey))
															 													) 
															 );
					}
				}catch(Exception ex){
					System.err.println("ReportsCreator#fillQueryOfParameters Key:"+nextKey+"   Value:"+parameters.get(nextKey)+"  Exception:"+ex.getMessage());
				}
				
			}
		}
		return queryOfReports;
	}


	/** преобразовать объект, который был получен от {@link Variable#getValue} на представление в Oracle запросе */
	@SuppressWarnings("unchecked")
	private String convertObjectToOracleRepresent(Object object) {
		if(object instanceof String){
			return "'"+((String)object).replaceAll("'", "''")+"'";
		}else if(object instanceof ArrayList){
			StringBuffer returnValue=new StringBuffer();
			ArrayList list=(ArrayList)object;
			for(int counter=0;counter<list.size();counter++){
				if(returnValue.length()>0){
					returnValue.append(",");
				}
				if(list.get(counter)!=null){
					returnValue.append("'"+list.get(counter).toString()+"'");
				}
			}
			return returnValue.toString();
		}else if(object instanceof Date){
			return "'"+sqlDateFormat.format((Date)object)+"'";
		}else if(object!=null){
			return object.toString();
		}else{
			return "null";
		}
	}

	/** получить из отчета значения всех переменных  
	 * @param connector - соединение с базой данных 
	 * @param report - объект, по которому нужно создать файл данных 
	 * @return получить список параметров 
	 */
	private HashMap<String, Object> getAllParameters(ConnectWrap connector,
													 Reports report) {
		HashMap<String, Object> returnValue=new HashMap<String, Object>();
		for(int counter=0;counter<report.getVariables().size();counter++){
			returnValue.put(report.getVariables().get(counter).getHtmlId(), 
							report.getVariables().get(counter).getValue(connector, report.getVariables())
							);
		}
		return returnValue;
	}



	private String getStringFromClob(Clob field){
		StringBuffer returnValue=new StringBuffer();
		try{
			BufferedReader br=new BufferedReader(field.getCharacterStream());
			String currentString=null;
			while((currentString=br.readLine())!=null){
				returnValue.append(currentString);
			}
		}catch(Exception ex){
			System.err.println("ReportsCreater#getStringFromClob Exception: "+ex.getMessage());
		}
		return returnValue.toString();
	}

	
	/**
	 * Получить все запросы, которые касаются только тела данных 
	 * @param connection текущее соединение с базой данных
	 * @param report_id номер отчета, по которому нужно получить запрос
	 * @return возвращает запрос, или null
	 */
	private ArrayList<ReportExecuterQueryParameters> getSqlQuery(Connection connection,int report_id){
		ArrayList<ReportExecuterQueryParameters> return_value=new ArrayList<ReportExecuterQueryParameters>();
		try{
			// INFO получение всех запросов по отчету 
			// select * from bc_admin.VC_CONTACT_PRS_JOF_REP_SQL_ALL
			String query="select * from "+ConnectWrap.schemePrefix+"VC_CONTACT_PRS_JOF_REP_SQL_ALL where (trim(is_header)='N' or trim(is_header)='n') and id_report="+report_id+" order by order_number";
			LOGGER.debug(query);
			ResultSet resultset=connection.createStatement().executeQuery(query);
			// берем текст запроса из первой записи
			while(resultset.next()){
				return_value.add(new ReportExecuterQueryParameters(this.getStringFromClob(resultset.getClob("CLOB_SQL")),resultset.getString("EXEC_FILE")));
				// LOGGER.debug("SQL:"+resultset.getString("TEXT_SQL"));
				// LOGGER.debug("Exec_file:"+resultset.getString("EXEC_FILE"));
			}
			resultset.getStatement().close();
		}catch(SQLException ex){
			LOGGER.error("Error in get Query report"+ex.getMessage());
			// System.err.println("getSqlQuery ERROR: "+ex.getMessage());
		}
		/* 
		LOGGER.debug("Всего запросов по телу данных: "+return_value.size());
		for(int counter=0;counter<return_value.size();counter++){
			LOGGER.debug(counter+" : "+return_value.get(counter).getQuery());
		}
		 */
		return return_value;
	}


	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd_HH_mm_sss");
	private Random random=new Random();
	/** сгенерировать уникальное имя файла, на основании номера отчета и штампа времени  
	 * @param clientId - номер клиента ( может быть любой )
	 * @param reportId - номер отчета ( может быть любой )
	 * @param format - расширение файла 
	 * */
	public String generateUniqueFileName(int clientId, int reportId, String formatString){
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
	
	private static String[] dateOracleFormat=new String[]{"MM","DD","YYYY","RRRR","YY","RR","HH24","HH","MI","SS"};
	private static String[] dateJavaFormat=new String[]  {"MM","dd","yyyy","yyyy","yy","yy","HH",  "hh","mm","ss"};
	private String convertDateOracleFormatToJavaFormat(String oracleFormat) {
		String returnValue=oracleFormat;
		for(int counter=0;counter<dateOracleFormat.length;counter++){
			returnValue=returnValue.replaceAll(dateOracleFormat[counter], dateJavaFormat[counter]);
		}
		return returnValue;
	}
	
	private SimpleDateFormat sqlDateFormat=new SimpleDateFormat("dd.MM.yyyy");
	
	/** получить формат даты из базы данных  */
	private void fillDateFormat(Connection connection){
		Statement statement=null;
		try{
			statement=connection.createStatement();
			// FIXME OfficePrivatePartner формат даты из базы данных 
			ResultSet rs=statement.executeQuery("SELECT dateformat FROM "+ConnectWrap.schemePrefix+"v_user_param_ln");
			rs.next();
			String oracleFormat=rs.getString(1);
			this.sqlDateFormat=new SimpleDateFormat(convertDateOracleFormatToJavaFormat(oracleFormat));
		}catch(Exception ex){
			System.out.println("Ошибка получения формата даты из View: v_user_param_ln "+ex.getMessage());
			this.sqlDateFormat=new SimpleDateFormat("dd.MM.yyyy");
		}finally{
			try{
				statement.close();
			}catch(Exception ex){};
		}
	}
	
}
