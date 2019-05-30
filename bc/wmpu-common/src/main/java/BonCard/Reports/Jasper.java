package BonCard.Reports;

import java.io.BufferedReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.*;


import bc.connection.Connector;
import bc.objects.bcObject;
import bc.objects.bcUserObject;

/**
 * Генератор отчетов с помощью JasperReport 
 * @author cherkashinv
 */
public class Jasper {
    public static int TYPE_PDF=0;
    public static int TYPE_XLS=1;
    public static int TYPE_RTF=2;
    public static int TYPE_HTML=3;
    // public static String field_path_to_report="D:\\work_nmtg\\Reports\\";
    
	private static bcObject object = new bcObject();

	private final static Logger LOGGER=Logger.getLogger(Jasper.class);
	
	private static StringBuffer variableList = new StringBuffer();
	
	private static StringBuffer errorMessage = new StringBuffer();
	
	public static StringBuffer getErrorMessage () {
		return errorMessage;
	}
	
	/** 
	 * генератор отчетов
	 * Singleton
	 * @param reporter_id - уникальный идентификатор отчета
	 * @param request - ServletHttpRequest для данного запроса
	 * @param response - ServletHttpResponse для данного запроса
	 * @param connection - соединение с базой данных
	 * @param field_variables - переменные по данному отчету
	 * @param path_to_jasper_reports_catalog - путь к каталогу, в котором лежат файлы JRXML (JasperReports file)
	 * @param path_to_export_file путь к файлу, в который необходимо экспортировать полученный отчет
	 * @return true если отчет успешно создан
	 */
	
	public static synchronized boolean CreateReport(String reporter_id,
													HttpServletRequest request, 
													HttpServletResponse response, 
													ReportVariables field_variables,
													String path_to_jasper_reports_catalog,
													String path_to_export_file,
													bcUserObject loginUser){
		boolean return_value=false;
		Connection connection=null;
		String sessionId = request.getSession().getId();
		errorMessage.setLength(0);
		LOGGER.setLevel(Level.DEBUG);
		try{
			object.setSessionId(sessionId);
			String generalDBScheme = object.getGeneralDBScheme();
			LOGGER.debug("Jasper.CreateReport.generalDBScheme=" + generalDBScheme);
			
			connection = Connector.getConnection(sessionId);
			
			int type=getType(request.getParameter("REPORT_FORMAT"));

			loginUser.getCurrentUserFeature();
			
			LOGGER.debug("type="+type);
			LOGGER.debug("Получить имя файла JasperReport ");
			String report_file=getFileName(connection, generalDBScheme, reporter_id);
			
			LOGGER.debug("Получить все параметры запроса: ($P)");
			Map<String, Object> parameters=getAllParameters(connection,
												generalDBScheme,
												reporter_id,
												field_variables,
												request);
			LOGGER.debug("Получить все запросы по отчету: ");
			ArrayList<QueryParameters> report_query=getSqlQuery(connection, generalDBScheme, reporter_id);
			
			//LOGGER.debug("сформировать список параметров");
			System.out.println("сформировать список параметров");
			getVariableList(request, field_variables);

            if(type==TYPE_HTML){
            	parameters.put("DEFAULT_SPACE", " ");
            }else{
            	parameters.put("DEFAULT_SPACE", "");
            }
        	parameters.put("CASHIER_NAME", loginUser.prepareFromHTML(loginUser.getValue("FIO_NAT_PRS_INITIAL")));
        	parameters.put("CASHIER_CD_CARD1", loginUser.prepareFromHTML(loginUser.getValue("CD_CARD1_HIDE")));
        	parameters.put("CASHIER_ID_DEALER", loginUser.prepareFromHTML(loginUser.getValue("ID_PARTNER_WORK")));
        	parameters.put("CASHIER_ID_SERVICE_PLACE", loginUser.prepareFromHTML(loginUser.getValue("ID_SERVICE_PLACE_WORK")));
        	parameters.put("CASHIER_CD_DEALER_IN_CLUB", loginUser.prepareFromHTML(loginUser.getValue("CD_CLUB_JUR_PRS")));
        	parameters.put("CASHIER_SNAME_SERVICE_PLACE", loginUser.prepareFromHTML(loginUser.getValue("SNAME_SERVICE_PLACE_WORK")));
        	parameters.put("CASHIER_ADR_SERVICE_PLACE", loginUser.prepareFromHTML(loginUser.getValue("ADR_JUR_PRS_SHORT")));
        	parameters.put("CASHIER_ID_CLUB", loginUser.prepareFromHTML(loginUser.getValue("ID_CLUB")));
        	parameters.put("CASHIER_SNAME_CLUB", loginUser.prepareFromHTML(loginUser.getValue("SNAME_CLUB")));
        	parameters.put("CASHIER_ID_TERM", loginUser.prepareFromHTML(loginUser.getValue("ID_TERM")));
			
        	field_variables.addStringVariable(connection, "CASHIER_ID_DEALER", "9999");
			LOGGER.debug("наполнить отчет параметрами из HTTP запроса ");
			report_query=fillQueryOfParameters(report_query,
											   request, 
											   field_variables);
			
			LOGGER.debug("Отработать запросы: ($F):"+report_query.size());
			ResultSet resultset=executeQueryAndAddParameter(connection,report_query,parameters,path_to_jasper_reports_catalog);
			//ResultSet resultset=connection.createStatement().executeQuery(report_query);

			LOGGER.debug("compile report");
            JasperReport jasper_report=JasperCompileManager.compileReport(path_to_jasper_reports_catalog+report_file);
            
            LOGGER.debug("create print");
            JasperPrint jasper_print;
            if(resultset!=null){
            	// обычный отчет 
                jasper_print=JasperFillManager.fillReport(jasper_report,
                        parameters,
                        new JRResultSetDataSource(resultset));
            }else{
            	// мультиотчет
                jasper_print=JasperFillManager.fillReport(jasper_report,
                        parameters,
                        new JREmptyDataSource());
            }
            LOGGER.debug("create report (exporter): path_to_export_file="+path_to_export_file);
            LOGGER.setLevel(Level.OFF);
            if(type==TYPE_PDF){
                //JasperExportManager.exportReportToPdfFile(jasper_print,path_to_export_file);
            	JRPdfExporter exporter=new JRPdfExporter();
                exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasper_print);
                exporter.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME, path_to_export_file);
                exporter.setParameter(JRPdfExporterParameter.IS_128_BIT_KEY, true);
                exporter.setParameter(JRPdfExporterParameter.IS_ENCRYPTED, false);
                exporter.setParameter(JRPdfExporterParameter.METADATA_AUTHOR, "NMT Group");
                exporter.setParameter(JRPdfExporterParameter.METADATA_CREATOR, "NMT Group");
                exporter.setParameter(JRPdfExporterParameter.METADATA_KEYWORDS, "Report");
                //exporter.setParameter(JRPdfExporterParameter.METADATA_SUBJECT, "меcто для темы");
                exporter.setParameter(JRPdfExporterParameter.METADATA_TITLE, "NMT Group Report");
//                Map<FontKey, PdfFont> defaultFonts=new HashMap<FontKey, PdfFont>();
//                defaultFonts.put(new FontKey("tahomabd.ttf", true, false), new PdfFont("/tmp/tahomabd.ttf", BaseFont.IDENTITY_H, true));
//                exporter.setParameter(JRPdfExporterParameter.FONT_MAP, defaultFonts);
                exporter.exportReport();
            }
            if(type==TYPE_XLS){
                JExcelApiExporter exporter=new JExcelApiExporter();
                exporter.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jasper_print);
                exporter.setParameter(JExcelApiExporterParameter.OUTPUT_FILE_NAME, path_to_export_file);
                exporter.exportReport();
            }
            if(type==TYPE_RTF){
                JRRtfExporter exporter=new JRRtfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasper_print);
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path_to_export_file);
                exporter.exportReport();
            }
            if(type==TYPE_HTML){
            	//JasperExportManager.exportReportToHtmlFile(jasper_print,path_to_export_file);
            	JRHtmlExporter exporter=new JRHtmlExporter();
            	exporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT, jasper_print);
                exporter.setParameter(JRHtmlExporterParameter.OUTPUT_FILE_NAME, path_to_export_file);
                exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
                exporter.exportReport();
            }
            LOGGER.setLevel(Level.DEBUG);
            
			LOGGER.debug("Создание отчета");
			LOGGER.debug("Сохранение отчета в файл");
			
			if(resultset!=null){
				resultset.getStatement().close();
			}
			// закрытие всех возможно откртых запросов
			for(int counter=0;counter<report_query.size();counter++){
				if(parameters.get("DATASOURCE_"+(counter+1))!=null){
					try{
						if(report_query.get(counter).getResultSet()!=null){
							report_query.get(counter).getResultSet().getStatement().close();
							LOGGER.debug("ResultSet #"+counter+" closed");
						}
					}catch(Exception ex){
						
					}
				}
			}
			writeReportResult(sessionId, reporter_id, path_to_export_file, "CREATED", "");
			return_value=true;
		}catch(Exception ex){
			LOGGER.error("CreateReport Error:"+ex.getMessage());
			writeReportResult(sessionId, reporter_id, "", "ERROR", "");
			errorMessage.append("ID report : "+reporter_id+"<br>Error message : "+ex.getMessage());
			return_value=false;
		}finally{
			Connector.closeConnection(connection);
		}
		return return_value;
	}
	
	private static void writeReportResult (String pSessionId, String pReportId, String pResultFileName, String pState, String pErrorMessage) {
		Connection con = null;
		CallableStatement cs = null;
		try {
			con = Connector.getConnection(pSessionId);
			
			cs = con.prepareCall("{call PACK$REPORT.write_report_task(?,?,?,?,?,?)}");
			cs.setString(1, pReportId);
			cs.setString(2, variableList.toString());
			cs.setString(3, "");
			cs.setString(4, pResultFileName);
			cs.setString(5, pState);
			cs.setString(6, pErrorMessage);
			cs.execute();
			con.commit();
		} catch (SQLException e) {
			LOGGER.error("writeReportResult() SQLException: " + e.toString());
		} catch (Exception el) {
			LOGGER.error("writeReportResult() Exception: " + el.toString());
		} finally {
			try {
				if (cs != null)
					cs.close();
			} catch (SQLException w) {
			}
			Connector.closeConnection(con);
		}
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
	private static ResultSet executeQueryAndAddParameter(Connection connection,
														 ArrayList<QueryParameters> query, 
														 Map<String, Object> parameters,
														 String path_to_reports) throws SQLException{
		ResultSet return_value=null;
		if(query.size()==1){
			// обычный отчет
			if(query.get(0).getQuery()==null){
				LOGGER.debug("у данного отчета нет ни одного запроса"+query.get(0).getQuery());
				return_value=null;
			}else{
				LOGGER.debug("Попытка выполнения единственного запроса: "+query.get(0).getQuery());
				System.out.println("Jasper: " + query.get(0).getQuery());
				parameters.put("SUBREPORT_DIR", path_to_reports);
				query.get(0).setResultSet(connection.createStatement().executeQuery(query.get(0).getQuery()));
				parameters.put("SHOW_REPORT_1", new Boolean(true));
				//LOGGER.debug("SHOW_REPORT_"+(counter+1));
				parameters.put("DATASOURCE_1", new JRResultSetDataSource(query.get(0).getResultSet()));
				return_value=connection.createStatement().executeQuery(query.get(0).getQuery());
			}
		}else{
			LOGGER.debug("Мультиотчет:");
			parameters.put("SUBREPORT_DIR", path_to_reports);
			// отработать все отчеты
			for(int counter=0;counter<query.size();counter++){
				LOGGER.debug("QUERY"+counter+"="+query.get(counter).getQuery());
				query.get(counter).setResultSet(connection.createStatement().executeQuery(query.get(counter).getQuery()));
				parameters.put("SHOW_REPORT_"+(counter+1), new Boolean(true));
				//LOGGER.debug("SHOW_REPORT_"+(counter+1));
				parameters.put("DATASOURCE_"+(counter+1), new JRResultSetDataSource(query.get(counter).getResultSet()));
				//LOGGER.debug("DATASOURCE_"+(counter+1)+"   "+resultset);
			}
			// положить ссылки на ResultSEt в параметры отчета
			
		}
		return return_value;
	}
	
	/** вернуть HashMap со всеми заполненными параметрами 
	 * @param connection соединение с базой данных
	 * @param field_variables все переменные для этого отчета
	 * @param request Http запрос
	 */
	private static Map<String, Object> getAllParameters(Connection connection,
													    String generalDBScheme,
														   String report_id,
														   ReportVariables field_variables, 
														   HttpServletRequest request){
		/** объект, который возвращает все значения по отчету */
		Map<String, Object> return_value=new HashMap<String, Object>();
		if (field_variables.getVariablesCount() == 0) {
			return return_value;
		}

		// блок получения параметров из HTML запроса ( те параметры, которые были переданы вместе с запросом HTML) 
		for(int counter=0;counter<field_variables.getVariablesCount();counter++){
			LOGGER.debug(field_variables.getVariable(counter).getUniqueHTMLName()+" & "+request.getParameter(field_variables.getVariable(counter).getUniqueHTMLName()));
			
			if(field_variables.getVariable(counter).isTextValue()){
				if(request.getParameter(field_variables.getVariable(counter).getUniqueHTMLName())==null){
					LOGGER.debug("нет данных в HTTP запросе");
					// нет данных в запросе 
					// вывести в отчет значение по умолчанию
					return_value.put(field_variables.getVariable(counter).getUniqueHTMLName(), 
					         		 field_variables.getVariable(counter).getDefaultValue()
					         );
				}else{
					// вывести значение из запроса
					// вывести в отчет параметр из запроса
					if (request.getParameter(field_variables.getVariable(counter).getUniqueHTMLName())==null ||
						"".equalsIgnoreCase(request.getParameter(field_variables.getVariable(counter).getUniqueHTMLName()))) {
						return_value.put(field_variables.getVariable(counter).getUniqueHTMLName(), 
								field_variables.getVariable(counter).getDefaultValue()
						         );
					} else {
					return_value.put(field_variables.getVariable(counter).getUniqueHTMLName(), 
							         request.getParameter(field_variables.getVariable(counter).getUniqueHTMLName())
							         );
					}
				}
			}else{
				// вывести в отчет параметр по ключу из SELECT
				if(request.getParameter(field_variables.getVariable(counter).getUniqueHTMLName())==null){
					// вывести значение по умолчанию
					LOGGER.debug("нет данных в HTTP запросе");
					return_value.put(field_variables.getVariable(counter).getUniqueHTMLName(),
									 ""
									 );
				}else{
					// проверить, является ли запрос MultiChoice
					if(field_variables.getVariable(counter).isMultiChoice()==true){
						// вывести значение из запроса 
						return_value.put(field_variables.getVariable(counter).getUniqueHTMLName(),
								 		 field_variables.getVariable(counter).getValueByKey(connection, field_variables.getVariable(counter).getUniqueHTMLName(), request.getParameterValues(field_variables.getVariable(counter).getUniqueHTMLName()))
								 		 );
					}else{
						// вывести значение из запроса 
						return_value.put(field_variables.getVariable(counter).getUniqueHTMLName(),
								 field_variables.getVariable(counter).getValueByKey(connection, field_variables.getVariable(counter).getUniqueHTMLName(), request.getParameter(field_variables.getVariable(counter).getUniqueHTMLName()))
								 );
					}
				}
			}
		}
		// блок получения параметров из SQL запросов к базе данных для этого отчета ( параметры из шапки )
		// Table: V_USER_REPORTS_SQL_ALL       Field: IS_HEADER='Y'
			// получить все запросы по отчету
		ArrayList<String> query_list=getAllQueryByReportHeader(connection, generalDBScheme, report_id, request, field_variables);
		    // отработать все запросы, и полученные значения положить в параметры запроса
		ResultSet resultset;
		for(int counter=0;counter<query_list.size();counter++){
			try{
				LOGGER.debug("getAllParameters:"+query_list.get(counter));
				resultset=connection.createStatement().executeQuery(query_list.get(counter));
				if(resultset.next()){
					// добавить все полученные значения (первая строка) из запроса в параметры отчета  
					addResultSetToHashMap(resultset, return_value);
				}
				resultset.getStatement().close();
			}catch(SQLException ex){
				LOGGER.error(" getAllParameters, запросы по параметрам из шапки SQLException:"+ex.getMessage());
			}catch(Exception ex){
				LOGGER.error(" getAllParameters, запросы по параметрам из шапки Exception:"+ex.getMessage());
			}
		}
		// output all parameters to LOG
		/*
		LOGGER.debug("$P{}:");
		Set entry_set=return_value.keySet();
		Iterator iterator=entry_set.iterator();
		String key;
		while(iterator.hasNext()){
			key=(String)iterator.next();
			LOGGER.debug(key+" : "+(String)return_value.get(key));
		}*/
		
		//Connector.closeConnection(connection);
		return return_value;
	}
	
	/** добавить все поля ResultSet в HashMap*/
	private static void addResultSetToHashMap(ResultSet resultset, Map<String,Object> parameters){
		try{
			ResultSetMetaData metaData=resultset.getMetaData();
			for(int counter=0;counter<metaData.getColumnCount();counter++){
				parameters.put(metaData.getColumnLabel(counter+1), resultset.getString(counter+1));
			}
		}catch(SQLException ex){
			LOGGER.error("addResultSetToHashMap SQLException:"+ex.getMessage());
		}catch(Exception ex){
			LOGGER.error("addResultSetToHashMap Exception:"+ex.getMessage());
		}
	}
	
	
	/** получить все запросы, которые отображают данные в шапке,  
	 *  по отчету и наполнить их данными
	 */
	private static ArrayList<String> getAllQueryByReportHeader(Connection connection,
															   String generalDBScheme,
															   String report_id,
															   HttpServletRequest request, 
															   ReportVariables field_variables){
		ArrayList<String> return_value=new ArrayList<String>();
		try{
			ResultSet resultset=connection.createStatement().executeQuery("select * from " + generalDBScheme + ".V_USER_REPORTS_SQL_ALL where (trim(is_header)='Y' or trim(is_header)='y') and id_report="+report_id);
			while(resultset.next()){
				return_value.add(fillQuery(getStringFromClob(resultset.getClob("CLOB_SQL")),request, field_variables));
			}
			resultset.getStatement().close();
		}catch(SQLException ex){
			LOGGER.error("getAllQueryByReportHeader SQLException:"+ex.getMessage());
		}catch(Exception ex){
			LOGGER.error("getAllQueryByReportHeader Exception:"+ex.getMessage());
		}
		LOGGER.debug("Query count:"+return_value.size());
		return return_value;
	}
	
	private static String getStringFromClob(Clob field){
		StringBuilder returnValue=new StringBuilder();
		try{
			BufferedReader br=new BufferedReader(field.getCharacterStream());
			String currentString=null;
			while((currentString=br.readLine())!=null){
				returnValue.append(currentString);
			}
		}catch(Exception ex){
			LOGGER.error("Jasper#getStringFromClob Exception: "+ex.getMessage());
		}
		return returnValue.toString();
	}
	
	/** получить тип запроса (int) на основании текстового значения из Http запроса */
	// TODO change to enum 
	private static int getType(String value){
		int return_value=TYPE_PDF;
		
		if(value==null){
			return return_value;
		}
		
		String temp_value=StringUtils.trim(value).toUpperCase();
		if(temp_value.equals("PDF")){
			return_value=TYPE_PDF;
		};
		if(temp_value.equals("RTF")){
			return_value=TYPE_RTF;
		};
		if(temp_value.equals("XLS")){
			return_value=TYPE_XLS;
		};
		if(temp_value.equals("HTML")){
			return_value=TYPE_HTML;
		};
		
		return return_value;
	}
	
	private static void getVariableList(HttpServletRequest request, ReportVariables variables){
		Variable current_variable;
		variableList.setLength(0);
		for(int counter=0;counter<variables.getVariablesCount();counter++){
			current_variable=variables.getVariable(counter);
			String variableName = current_variable.getUniqueHTMLName();
			if (counter!=0) {
				variableList.append(";");
			}
			variableList.append(variableName+"=");
			
			String[] values=request.getParameterValues(variableName);
			if(values!=null){
				for(int counter2=0;counter2<values.length;counter2++){
					variableList.append("'"+values[counter2].replaceAll("'","''")+"'");
					if(counter2!=(values.length-1)){
						variableList.append(",");
					}
				}
			}else{
				LOGGER.debug("getVariableList: Parameter name:"+variableName+"  is empty");
				variableList.append("''");
			}
			System.out.println("variableList="+variableList.toString());
		}
	}
	
	/**
	 * наполнить запросы параметрами из HTTP запроса
	 * @return
	 */
	private static ArrayList<QueryParameters> fillQueryOfParameters(ArrayList<QueryParameters> query,
												 HttpServletRequest request,
												 ReportVariables variables){
		String current_query_text;
		LOGGER.debug("Всего параметров:"+query.size());
		// перебрать все SQL запросы по данному отчету
		for(int query_counter=0;query_counter<query.size();query_counter++){
			// получить текст запроса
			current_query_text=query.get(query_counter).getQuery();
			LOGGER.debug("fill parameters:"+current_query_text);
			query.get(query_counter).setQuery(fillQuery(current_query_text,request,variables));
		}
		return query;
	}
	
	/** 
	 * Данный метод служит для преобразования множественного параметра, который находится в HttpServletRequest
	 * в вид, который являлся бы приемлемым для Query IN, а именно  "('','','')" 
	 */
	private static String getAllParametersForQuery(HttpServletRequest request,String name){
		StringBuilder return_value=new StringBuilder();
		String[] values=request.getParameterValues(name);
		if(values!=null){
			for(int counter=0;counter<values.length;counter++){
				return_value.append("'"+values[counter].replaceAll("'","''")+"'");
				if(counter!=(values.length-1)){
					return_value.append(",");
				}
			}
		}else{
			LOGGER.debug("getAllParametersForQuery: Parameter name:"+name+"  is empty");
			return_value.append("''");
		}
		if (return_value==null || "".equalsIgnoreCase(return_value.toString())) {
			return_value.append(name);
		}
		LOGGER.debug("getAllParametersForQuery: return="+return_value.toString());
		return return_value.toString();
	}
	/**
	 * метод, который заполняет переданный текст запроса всеми параметрами из HTTP запроса
	 * @param query текст запроса, который необходимо наполнить данными
	 * @param request Http запрос
	 * @param variables обертка для имен переменных
	 * @return возвращает уже наполненный данными запрос
	 */
	private static String fillQuery(String query, 
									HttpServletRequest request,
									ReportVariables variables){
		String return_value=query;
		Variable current_variable;
		if(return_value!=null){
			// перебрать все возможные параметры для переменных Http запроса
			for(int counter=0;counter<variables.getVariablesCount();counter++){
				current_variable=variables.getVariable(counter);
				if(request.getParameter(current_variable.getUniqueHTMLName())!=null){
					// параметр существует - вставляем
					if(current_variable.isMultiChoice()==true){
						LOGGER.debug("Переменная имеет множественное значение ");
						return_value=return_value.replaceAll(":"+current_variable.getUniqueHTMLName()+":", getAllParametersForQuery(request,current_variable.getUniqueHTMLName()));
					}else{
						LOGGER.debug("Переменная имеет единичное значение");
						return_value=return_value.replaceAll(":"+current_variable.getUniqueHTMLName()+":", getAllParametersForQuery(request,current_variable.getUniqueHTMLName()));
						//return_value=return_value.replaceAll(":"+current_variable.getUniqueHTMLName()+":", request.getParameter(current_variable.getUniqueHTMLName()).replaceAll("'","''"));
					}
				}else{
					// параметр не существует - пытаемся взять значение по умолчанию
					return_value=return_value.replaceAll(":"+current_variable.getUniqueHTMLName()+":", current_variable.getDefaultValue().replaceAll("'","''"));
				}
			}
		}
		return return_value;
	}
	
	/**
	 * Получить все запросы, которые касаются только тела данных 
	 * @param connection текущее соединение с базой данных
	 * @param report_id номер отчета, по которому нужно получить запрос
	 * @return возвращает запрос, или null
	 */
	private static ArrayList<QueryParameters> getSqlQuery(Connection connection,String generalDBScheme, String report_id){
		ArrayList<QueryParameters> return_value=new ArrayList<QueryParameters>();
		try{
			String query="select * from " + generalDBScheme + ".V_USER_REPORTS_SQL_ALL where (trim(is_header)='N' or trim(is_header)='n') and id_report="+report_id+" order by order_number";
			LOGGER.debug(query);
			ResultSet resultset=connection.createStatement().executeQuery(query);
			// берем текст запроса из первой записи
			while(resultset.next()){
				return_value.add(new QueryParameters(getStringFromClob(resultset.getClob("CLOB_SQL")),resultset.getString("EXEC_FILE")));
				//LOGGER.debug("SQL:"+resultset.getString("TEXT_SQL"));
				LOGGER.debug("Exec_file:"+resultset.getString("EXEC_FILE"));
			}
			resultset.getStatement().close();
		}catch(SQLException ex){
			LOGGER.error("Error in get Query report"+ex.getMessage());
		}
		LOGGER.debug("Всего запросов по телу данных: "+return_value.size());
		for(int counter=0;counter<return_value.size();counter++){
			LOGGER.debug(counter+" : "+return_value.get(counter).getQuery());
		}
		return return_value;
	}
	
	/**
	 * @param connection текущее соединение с базой данных
	 * @param report_id номер отчета, по которому нужно получить запрос
	 * @return возвращает запрос, или null
	 */
	private static String getFileName(Connection connection, String generalDBScheme, String report_id){
		String return_value=null;
		try{
			
			String query="select * from " + generalDBScheme + ".VC_USER_REPORTS_ALL where id_report="+report_id;
			LOGGER.debug("Jasper.getFileName = "+query);
			ResultSet resultset=connection.createStatement().executeQuery(query);
			if(resultset.next()){
				LOGGER.debug("Jasper.EXEC_FILE = "+resultset.getString("EXEC_FILE"));
				return_value=resultset.getString("EXEC_FILE");
			}
			LOGGER.error("Jasper.getFileName(): "+return_value);
			resultset.getStatement().close();
		}catch(SQLException ex){
			LOGGER.debug("Jasper.getFileName SQLException "+ex.toString());
			LOGGER.error("Error in get Query report"+ex.getMessage());
		}catch(Exception e){
				LOGGER.debug("Jasper.getFileName SQLException "+e.toString());
				LOGGER.error("Error in get Query report"+e.getMessage());
		}
		return return_value;
	}
}

/** 
 * класс-обертка для параметров запроса (хранит текст запроса и хранит путь к файлу, для мультиотчетов)
 */
class QueryParameters{
	private String field_query;
	private String field_path_to_file;
	private ResultSet field_resultset;
	public QueryParameters(String query,String path_to_file){
		//this.field_query=query;
		//this.field_path_to_file=path_to_file;
		this.setQuery(query);
		this.setPathToFile(path_to_file);
	}
	/** получить значение для ResultSet*/
	public ResultSet getResultSet(){
		return this.field_resultset;
	}
	/** установить значение для ResultSet*/
	public void setResultSet(ResultSet value){
		this.field_resultset=value;
	}
	/** установить текст запроса */
	public void setQuery(String value){
		this.field_query=value;
	}
	/** получить текст запроса */
	public String getQuery(){
		return this.field_query;
	}
	/** устновить путь к файлу-отчета */
	public void setPathToFile(String value){
		this.field_path_to_file=value;
	}
	/** получить путь к файлу-отчета */
	public String getPathToFile(){
		return this.field_path_to_file;
	}
}
