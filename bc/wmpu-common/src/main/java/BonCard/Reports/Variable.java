	package BonCard.Reports;
import java.sql.*;
import java.util.ArrayList;

import org.apache.log4j.*;

import BonCard.DataBase.UtilityConnector;

/** 
 * Класс для описания самой переменной, которую нужно отображать 
 * на HTML странице в качестве критерия поиска для отчета 
 * @author cherkashinv
 */
public class Variable {
	private final static Logger LOGGER=Logger.getLogger(Variable.class);
	
	/** функция JavaScript, которая будет помещена в блок, который, в свою очередь, должен быть помещен в раздел страницы HTML windowl.onload()*/
	private static String field_javascript_function_name="fill_data";
	/** функция JavaScript, которая будет помещать в объект данные о необходимости установки ширины объетка */
	private static String field_javascript_function_width="add_object_width";
	/** функция JavaScript, которая будет вызываться при изменении объекта SELECT */
	private static String field_javascript_function_onchange="on_change_select";
	
	/** строка, которая является разделителем в запросах, для параметров*/
	private static String field_marker=":";
	/** имя и уникальный ID на HTML странице*/
	private String field_html_name="";
	
	public static int TYPE_STRING=0;
	public static int TYPE_NUMBER=1;
	public static int TYPE_DATE=2;
	public static int TYPE_DATE_TIME=3;
	private int field_current_parameter_type=0;
	
	private String sessionId;
	
	/** тип параметра из базы данных - строковое представление самого типа <br>
	 * возможные значения: <br>
	 * <li> DATE</li>
	 * <li> STRING</li>
	 * <li> NUMBER</li>
	 * <br>
	 * <strong> внимание на регистр - его нужно игнорировать</strong>
	 * */
	private String field_parameter_type="";
	/** 
	 * значение для параметра по умолчанию
	 */
	private String field_default_value="";
	/** 
	 * поле-флаг, которое показывает, является ли поле (SELECT multichoice) <br>
	 * возможные значения для поля: <br>
	 * <li> Y</li> 
	 * <li> N</li>
	 * <br>
	 * <strong> внимание на регистр - его нужно игнорировать</strong>
	 */
	private String field_multi_choice="";
	/**
	 * Заголовок для параметра, который необходимо поставить на HTML странице
	 */
	private String field_caption="";
	/**
	 * тип отображения перменной:<br>
	 * 	<li>INPUT</li> - простое значение, отображение в виде ввода строки
	 *  <li>SQL</li> - значение нужно выбирать из списка значений
	 */
	private String field_enter_type="";
	
	/** объект, в котором хранится код HTML для данной переменной */
	private StringBuilder field_html;
	/** строка, которая содерит запрос к базе для выбора значений из словарей */
	private String field_sql_text_for_select="";
	
	/** признак обязательности поля*/
	private String field_required="";
	
	/** строка, которая содержит запрос к базе данных, для получения на основании ключа наименования поля, в случаях выбора значения из списка */
	private String field_sql_text_get_value="";
	/** строка, которая является преамбулой для получения запроса, используя ключевое значение */
	private String field_sql_text_get_value_preambule="";
	/** строка, которая является постамбулой для получения запроса, используя ключевое значение */
	private String field_sql_text_get_value_postambule="";
	/** флаг, который говорит о необходимости принудительного выбора при загрузке страницы */
	private String field_on_load="";
	/** строка, которая содержит порядковый номер в запросе*/
	private String field_order_number="";
	/** объект, который хранит все необходимые параметры для запроса*/
	private ArrayList<String> field_sql_text_for_select_params;
	/** объект, который хранит текст с необходимыми функциями, которые нужно добавить в HTML.onLoad*/
	private StringBuilder field_onload_javascript=new StringBuilder();
	/** идентификатор отчета */
	private String field_report_id="";
	/** полный путь к контексту Web приложения, для которого генерируется данный HTML код - для полного пути к изображениям */
	//private String field_path_to_web_context="";
	/** ширина элемента, которая должна быть установлена после загрузки страницы */
	private String field_width="";

	private String field_date_format="";

	/** объект, который содержит дополнительный код на JavaScript, который должен быть помещен на страницу*/
	private StringBuilder field_addition_java_script=new StringBuilder();
	/**
	 * @param report_id - уникальный идентификатор отчета
	 * @param html_name - уникальное имя для переменной на HTML странице
	 * @param parameter_type - тип параметра из базы данных - строковое представление самого типа <br>
	 * возможные значения: <br>
	 * <li> DATE</li>
	 * <li> STRING</li>
	 * <li> NUMBER</li>
	 * <br>
	 * <strong> внимание на регистр - его нужно игнорировать</strong>
	 * 
	 * @param default_value - значение по умолчанию
	 * @param text_in_report - Заголовок для параметра, который необходимо поставить на HTML странице
	 * ключ вставляется вместо :value: (между двумя двоеточиями, то вставляем ключ, убираю между двоеточиями включительно, если есть ' - задваиваем)
	 * @param enter_type - тип отображения перменной:<br>
	 * 	<li>INPUT</li> - простое значение, отображение в виде ввода строки
	 *  <li>SQL</li> - значение нужно выбирать из списка значений
	 * @param sql_text_for_select - строка, которая содержит запрос к базе для выбора значений из словарей
	 * @param sql_text_get_value -  строка, которая содежит запрос к базе данных для получения Наименования поля на основании значения,<br>
	 * @param multi_choice поле-флаг, которое показывает, является ли поле (SELECT multichoice) <br>
	 * возможные значения для поля: <br>
	 * <li> Y</li> 
	 * <li> N</li>
	 * <br>
	 * <strong> внимание на регистр - его нужно игнорировать</strong>
	 * @param required - является ли данный параметр обязательным <br>
	 *  <li> Y </li>
	 *  <li> N </li>
	 *  @param order_number - порядковый номер переменной в запросе для данного отчета
	 *  @param on_load  - возможные варианты, имеет смысл только для SELECT:
	 *  <li> Y </li> данная переменная должна быть загружена на старте страницы 
	 *  <li> N </li>
	 *  @param width - ширина элемента, которая должна быть установлена после загрузки страницы
	 *  @param path_to_web_context - путь к контексту данного Web ресурса (для добавления полного пути к изображениям)
	 */
	public Variable(Connection connection,
					String report_id,
					String html_name,
					String parameter_type,
					String default_value,
					String text_in_report,
					String enter_type,
					String sql_text_for_select,
					String sql_text_get_value,
					String multi_choice,
					String required,
					String order_number,
					String on_load,
					String width,
					//String path_to_web_context,
					String pDateFormat,
					String pSessionId
					){
		LOGGER.debug("begin constructor, set variable's");
		this.field_html=new StringBuilder();
		// задать значения для переменных
		this.field_report_id=report_id;
		this.field_html_name=html_name;
		this.field_parameter_type=parameter_type;
		this.setParameterType(this.field_parameter_type);
		if(default_value!=null){
			this.field_default_value=default_value;
		}
		this.field_enter_type=enter_type;
		this.field_multi_choice=multi_choice;
		this.field_caption=text_in_report;
		this.field_sql_text_for_select=sql_text_for_select;
		this.field_sql_text_get_value=sql_text_get_value;
		this.field_required=required;
		this.field_on_load=on_load;
		this.field_order_number=order_number;
		//this.field_path_to_web_context=path_to_web_context;
		this.field_width=width;
		this.field_date_format = pDateFormat;
		this.sessionId = pSessionId;
		LOGGER.debug("отработать данные, для получения кода в виде HTML текста");
		// отработать данные, для получения кода в виде HTML текста
		LOGGER.debug("generateHTML()");
		generateHTML(connection);
		if(this.isTextValue()==false){
			LOGGER.debug("generateSQL_get_value");
			generateSQL_get_value();
			LOGGER.debug("generateSQL_params");
			generateSQL_params();
		}
	}

	/** установить значение текущего параметра в виде static int*/
	private int setParameterType(String value){
		String prepare_value=value.trim().toUpperCase();
		if(prepare_value.equals("NUMBER")){
			this.field_current_parameter_type=TYPE_NUMBER;
		}
		if(prepare_value.equals("DATE")){
			this.field_current_parameter_type=TYPE_DATE;
		}
		if(prepare_value.equals("DATETIME")){
			this.field_current_parameter_type=TYPE_DATE_TIME;
		}
		if(prepare_value.equals("STRING")){
			this.field_current_parameter_type=TYPE_STRING;
		}
		return this.field_current_parameter_type;
	}
	/** получить значение параметра в виде static int*/
	public int getParameterType(){
		return this.field_current_parameter_type;
	}
	
	/** метод для генерации HTML текста */
	private boolean generateHTML(Connection connection){
		boolean return_value=false;
		this.field_html.append("<span>");
		if(this.field_enter_type.trim().equalsIgnoreCase("INPUT")){
			// распознать какой именно тип переменной нужно отобразить на HTML странице
				// текстовый тип 
			if(this.getParameterType()==TYPE_DATE){
				// тип дата
				LOGGER.debug("переменная типа DATE");
				this.field_html.append("<input  class=\"inputfield\" type=\"text\" ");
				this.field_html.append("name=\""+this.getUniqueHTMLName()+"\" ");
				this.field_html.append("size=\"8\" ");
				this.field_html.append("value=\""+this.getDefaultValue()+"\" ");
				this.field_html.append("class=\"inputfield\" ");
				this.field_html.append("id=\"id_"+this.getUniqueHTMLName()+"\"> ");
				this.field_html.append("<img 	src=\""+/*this.field_path_to_web_context+*/"../images/calendar.gif\" "); 
				this.field_html.append("	id=\"btn_"+this.getUniqueHTMLName()+"\" "); 
				this.field_html.append("	style=\"cursor: pointer; border: 1px solid grey\" ");
				// TODO добавить формат 
				this.field_html.append("	title=\"\" "); 
				this.field_html.append("	onmouseover=\"this.style.background='gray'; \" "); 
				this.field_html.append("	onmouseout=\"this.style.background='' \" /> ");				
				
				this.field_addition_java_script.append("<script type=\"text/javascript\"> \n");
				this.field_addition_java_script.append("Calendar.setup({ \n");
				this.field_addition_java_script.append("			   inputField  : \"id_"+this.getUniqueHTMLName()+"\", \n");
				// TODO добавить формат даты для вывода пользователю 
				this.field_addition_java_script.append("			   ifFormat    : \""+this.field_date_format+"\", \n");    
				this.field_addition_java_script.append("			   button      : \"btn_"+this.getUniqueHTMLName()+"\" \n");       
				this.field_addition_java_script.append("			     }); \n");  
				this.field_addition_java_script.append("			</script> \n");
				
			} else if(this.getParameterType()==TYPE_DATE_TIME){
				// тип дата
				LOGGER.debug("переменная типа DATE");
				this.field_html.append("<input  class=\"inputfield\" type=\"text\" ");
				this.field_html.append("name=\""+this.getUniqueHTMLName()+"\" ");
				this.field_html.append("size=\"8\" ");
				this.field_html.append("value=\""+this.getDefaultValue()+"\" ");
				this.field_html.append("class=\"inputfield\" ");
				this.field_html.append("id=\"id_"+this.getUniqueHTMLName()+"\"> ");
				this.field_html.append("<img 	src=\""+/*this.field_path_to_web_context+*/"../images/calendar.gif\" "); 
				this.field_html.append("	id=\"btn_"+this.getUniqueHTMLName()+"\" "); 
				this.field_html.append("	style=\"cursor: pointer; border: 1px solid grey\" ");
				// TODO добавить формат 
				this.field_html.append("	title=\"\" "); 
				this.field_html.append("	onmouseover=\"this.style.background='gray'; \" "); 
				this.field_html.append("	onmouseout=\"this.style.background='' \" /> ");				
				
				this.field_addition_java_script.append("<script type=\"text/javascript\"> \n");
				this.field_addition_java_script.append("Calendar.setup({ \n");
				this.field_addition_java_script.append("			   inputField  : \"id_"+this.getUniqueHTMLName()+"\", \n");
				// TODO добавить формат даты для вывода пользователю 
				this.field_addition_java_script.append("			   ifFormat    : \""+this.field_date_format+" %H:%M"+"\", \n");    
				this.field_addition_java_script.append("			   button      : \"btn_"+this.getUniqueHTMLName()+"\", \n");       
				this.field_addition_java_script.append("			   showsTime   : true \n");       
				this.field_addition_java_script.append("			     }); \n");  
				this.field_addition_java_script.append("			</script> \n");
				
			} else {
				// другой текстовый тип
				// TODO добавить распознавание переменных NUMBER, STRING
				LOGGER.debug("переменная NUMBER или STRING ");
				this.field_html.append("<input class=\"inputfield\" type=\"text\" name=\""+this.field_html_name+"\" id=\""+this.field_html_name+"\" value=\""+this.field_default_value+"\">");
			}
		} else if (this.field_enter_type.trim().equalsIgnoreCase("SQL")){
			// тип SELECT
			this.field_html.append(this.generateHtmlSelect(connection, field_sql_text_for_select, field_default_value, isMultiChoice()));
		} else if (this.field_enter_type.trim().equalsIgnoreCase("AUTOCOMPLETE")){
			this.generateHtmlAutoComplete(connection, field_sql_text_for_select, field_default_value /*, isMultiChoice()*/);
		}
		this.field_html.append("</span>");
		// проверить на необходимость добавления данного поля в задачу по заданию ширины
		if(this.isWidthHtmlNeedSet()){
			this.field_onload_javascript.append(Variable.field_javascript_function_width+"('"+this.getUniqueHTMLName()+"','"+this.field_width+"');\n");
		}
		return return_value;
	}
	/** метод для получения из текста запроса всех параметров, если они есть*/
	private ArrayList<String> getParametersFromQuery(String query,String marker){
        ArrayList<String> return_value=new ArrayList<String>();
        int position_begin=query.indexOf(marker);
        int position_end=0;
        while(position_begin>=0){
            position_end=query.indexOf(marker,position_begin+marker.length());
            if(position_end>0){
                return_value.add(query.substring(position_begin+marker.length(),position_end));
            }else{
                break;
            }
            position_begin=query.indexOf(marker,position_end+marker.length());
        }
        return return_value;
	}
	/** 
	 * Метод, который заполняет объект field_sql_text_for_select_params,
	 * в котором хранятся возможные параметры для получения значений
	 */
	private void generateSQL_params(){
		this.field_sql_text_for_select_params=this.getParametersFromQuery(this.field_sql_text_for_select,Variable.field_marker);
		if(this.field_sql_text_for_select_params.size()>0){
			// нужно добавить скрипт в onLoad страницы HTML
			/*	function (param_name,  - имя параметра из запроса
		  				  this_html_name, - имя HTML от которого зависит данных запрос
	  					  report_id, - номер отчета
	  					  order_number - номер переменной в запросе
	  					   )
			 * 
			 */
			for(int counter=0;counter<this.field_sql_text_for_select_params.size();counter++){
				
				this.field_onload_javascript.append( field_javascript_function_name+"(" 
												    +"'"+ this.field_sql_text_for_select_params.get(counter)+"',"
												    +"'"+ this.field_html_name+"',"
												    +"'"+this.field_report_id+"',"
												    +"'"+this.field_order_number+"'"
						                            +");\n");
			}
		}
	}
	/**
	 * метод, который наполняет переменные преамбулы и постамбулы для получения отчета
	 */
	private void generateSQL_get_value(){
		if(   (this.field_sql_text_get_value!=null)
			&&(!this.field_sql_text_get_value.equals(""))
			&&(this.field_sql_text_get_value.indexOf(field_marker)>0)
		){
			/** позиция первого вхождения маркера*/
			int marker_position=this.field_sql_text_get_value.indexOf(field_marker);
			this.field_sql_text_get_value_preambule=this.field_sql_text_get_value.substring(0, marker_position);
			this.field_sql_text_get_value_postambule=this.field_sql_text_get_value.substring(
														this.field_sql_text_get_value.indexOf(field_marker,marker_position+field_marker.length())+field_marker.length(),
														this.field_sql_text_get_value.length()
													 );
			LOGGER.debug("Original["+this.field_sql_text_get_value+"]");
			LOGGER.debug("Preambule["+this.field_sql_text_get_value_preambule+"]");
			LOGGER.debug("Postambule["+this.field_sql_text_get_value_postambule+"]");
		}else{
			LOGGER.warn("в запросе для получения ключа нет маркеров:"+this.field_sql_text_get_value);
		}
	}
	/** 
	 * генерация блока HTML кода для выбора SELECT из запроса
	 * @param SQL_query - запрос для получения значений, отображаемых в списке
	 * @param selected_value - значение по умолчанию, если оно будет
	 * @param is_multi_choice 
	 */
	private String generateHtmlSelect(Connection connection, String SQL_query, String selected_value, boolean is_multi_choice){
		StringBuilder html_text=new StringBuilder();
		// начальная фаза создания SELECT
		if(is_multi_choice==true){
			html_text.append("<table class=\"tablereport\" style=\"border: 0px;\"><tr><td>");
			html_text.append("<select class=\"inputfield\" name=\""+this.field_html_name+"\" id=\""+this.field_html_name+"\" multiple onchange=\""+field_javascript_function_onchange+"(this,'"+sessionId+"');\" >");
		}else{
			html_text.append("<select class=\"inputfield\" name=\""+this.field_html_name+"\" id=\""+this.field_html_name+"\" onchange=\""+field_javascript_function_onchange+"(this);\" >");
		}
		// создание тела SELECT
		ResultSet resultset=null;
		try{
			LOGGER.debug("попытка получения данных из словаря "+SQL_query);
			resultset=connection.createStatement().executeQuery(SQL_query);
			String current_value=null;
			if(this.isRequired()==false){
				html_text.append("	<option value=\"null\" selected style=\"height: 11px; padding:2px 0;\"> </option>\n");
			}
			if((selected_value==null)||(selected_value.trim().equals(""))){
				// нет значения по умолчанию - установить первый из списка, если он есть
				int counter=0;
				String selectdParam = "";
				if(this.isRequired()==true){
					selectdParam = " selected ";
				}
				while(resultset.next()){
					++counter;
					current_value=resultset.getString(1).trim();
					if(counter==1){
						html_text.append("	<option value=\""+resultset.getString(1)+"\" " + selectdParam + "> "+resultset.getString(2)+"</option>\n");
					}else{
						html_text.append("	<option value=\""+resultset.getString(1)+"\">"+resultset.getString(2)+"</option>\n");
					}
				}
			}else{
				// нужно установить значение по умолчанию
				while(resultset.next()){
					current_value=resultset.getString(1).trim();
					if(current_value.equalsIgnoreCase(selected_value)){
						html_text.append("	<option value=\""+resultset.getString(1)+"\" selected> "+resultset.getString(2)+"</option>\n");
					}else{
						html_text.append("	<option value=\""+resultset.getString(1)+"\">"+resultset.getString(2)+"</option>\n");					
					}
				}
			}
			LOGGER.debug("законечно получение данных по запросу");
		}catch(SQLException ex){
			LOGGER.error("Variable SQLException:"+ex.getMessage());
			SQLException current_exception=null;
			while((current_exception=ex.getNextException())!=null){
				LOGGER.error("Variable SQLException:"+current_exception.getMessage());
			}
		}catch(Exception e){
			LOGGER.error("Variable Exception:"+e.getMessage());
		}finally{
			if(resultset!=null){
				UtilityConnector.closeQuietly(resultset);
			}
		}
		// последняя стадия создания SELECT
		html_text.append("</select>");
		if(is_multi_choice==true){
			// добавить кнопку и JavaScript для обработки текущего элемента
			html_text.append("</td><td align=\"left\" valign=\"top\">");
			html_text.append("<input type=\"button\" class=\"inputfield\" value=\"+\" onclick=\"select_all('"+this.field_html_name+"')\"><br>");
			html_text.append("<input type=\"button\" class=\"inputfield\" value=\"-\" onclick=\"deselect_all('"+this.field_html_name+"')\"");
			html_text.append("</td></tr></table>");
		}
		
		return html_text.toString();
	}
	
	private void generateHtmlAutoComplete(Connection connection, String SQL_query, String selected_value /*, boolean is_multi_choice*/){
		StringBuilder htmlFunction=new StringBuilder();
		
		String selectedId = "";
		String selectedName = "";
		
		ResultSet resultset=null;
		try{
			LOGGER.debug("попытка получения данных из словаря для AUTOCOMPLETE: "+SQL_query);
			resultset=connection.createStatement().executeQuery(SQL_query);
			String current_value=null;
			//if(this.isRequired()==false){
			//	html_text.append("	<option value=\"null\" selected style=\"height: 11px; padding:2px 0;\"> </option>\n");
			//}

			this.field_addition_java_script.append("<script type=\"text/javascript\"> \n");
			this.field_addition_java_script.append("jQuery.fn.select"+this.field_html_name+" = function(field){\n");
			this.field_addition_java_script.append("return this.each(function(){\n");

			this.field_addition_java_script.append("var values_"+this.field_html_name+" = [\n");
			int counter=0;
			while(resultset.next()){
				counter++;
				current_value=resultset.getString(1).trim();
				if (!(selected_value==null || "".equalsIgnoreCase(selected_value))) {
					if(current_value.equalsIgnoreCase(selected_value)) {
						selectedId = current_value;
						selectedName = resultset.getString(2).trim();
					}
				}
				if (counter==1) {
					htmlFunction.append("$(\"#\"+field+\"_name\")\n");
					htmlFunction.append(".autocomplete({\n");
					htmlFunction.append("source: values_"+this.field_html_name+",\n");
					htmlFunction.append("lookupLimit: 10,\n");
					htmlFunction.append("minLength:0,\n");
					htmlFunction.append("select: function( event , ui ) {\n");
					htmlFunction.append(" document.getElementById(field).value =ui.item.data; \n");
					htmlFunction.append("}\n");
					htmlFunction.append("})\n");
					htmlFunction.append(".focus(function(){\n");
					htmlFunction.append(" if (this.value == \"\") {  $(this).autocomplete(\"search\"); }\n");
					htmlFunction.append("});\n");
				}
				
				this.field_addition_java_script.append(" { value: '" + resultset.getString(2).trim() + "', data: '" + resultset.getString(1).trim() + "' },\n");
			}
			this.field_addition_java_script.delete(this.field_addition_java_script.length()-2,this.field_addition_java_script.length());

			this.field_addition_java_script.append("];\n");
			this.field_addition_java_script.append(htmlFunction.toString()+"\n");
			this.field_addition_java_script.append("});\n");
			this.field_addition_java_script.append("}\n");
			this.field_addition_java_script.append("function get"+this.field_html_name+"(field){\n");
			this.field_addition_java_script.append("jQuery(document).ready(function(){\n");
			this.field_addition_java_script.append("jQuery(\"#\"+field+\"_name\").select"+this.field_html_name+"(field);\n");
			this.field_addition_java_script.append("});\n");
			this.field_addition_java_script.append("}\n");
			this.field_addition_java_script.append("get"+this.field_html_name+"(\""+this.field_html_name+"\");\n");
			this.field_addition_java_script.append("			</script> \n");

			this.field_html.append("<input type=\"hidden\" name=\""+this.field_html_name+"\" id=\""+this.field_html_name+"\" value=\"" + selectedId + "\">");
			this.field_html.append("<input type=\"text\" name=\""+this.field_html_name+"_name\" id=\""+this.field_html_name+"_name\" size=\"20\" value=\"" + selectedName + "\" class=\"inputfield\" >");
			
			LOGGER.debug("законечно получение данных по запросу для AUTOCOMPLETE");
		}catch(SQLException ex){
			LOGGER.error("Variable SQLException:"+ex.getMessage());
			SQLException current_exception=null;
			while((current_exception=ex.getNextException())!=null){
				LOGGER.error("Variable SQLException:"+current_exception.getMessage());
			}
		}catch(Exception e){
			LOGGER.error("Variable Exception:"+e.getMessage());
		}finally{
			if(resultset!=null){
				UtilityConnector.closeQuietly(resultset);
			}
		}
		//if(is_multi_choice==true){
			// добавить кнопку и JavaScript для обработки текущего элемента
		//	html_text.append("</td><td align=\"left\" valign=\"top\">");
		//	html_text.append("<input type=\"button\" class=\"inputfield\" value=\"+\" onclick=\"select_all('"+this.field_html_name+"')\"><br>");
		//	html_text.append("<input type=\"button\" class=\"inputfield\" value=\"-\" onclick=\"deselect_all('"+this.field_html_name+"')\"");
		//	html_text.append("</td></tr></table>");
		//}
	}
	/** получить уникальное имя для переменной */
	public String getUniqueHTMLName(){
		return this.field_html_name;
	}
	/** получить наименование параметра */
	public String getCaption(){
		return this.field_caption;
	}
	
	/** получить вид компонента в виде HTML */
	public String getHTML(){
		return this.field_html.toString();
	}
	/** получить текст JavaScript, который будет добавлен в функцию, которая будет добавлена в onLoad страницы HTML */
	public String getOnLoadJavaScript(){
		return this.field_onload_javascript.toString();
	}
	/** получить текст JavaScript, который необходимо добавить в качестве скрипта на страницу HTML
	 * в любую его часть - данный код уже обернут в тэги <script type="text/javascript"> 
	 */
	public String getAdditionJavaScript(){
		return this.field_addition_java_script.toString();
	}
	
	/**
	 * @return является ли данная переменная текстовым значением <br>
	 * <li> true - переменная должна быть отображена в виде текста - JTextField</li>
	 * <li> false - переменная должна быть отображена в виде блока выбора SELECT </li>
	 */
	public boolean isTextValue(){
		boolean return_value=true;
		if(this.field_enter_type.trim().equalsIgnoreCase("INPUT")){
			return_value=true;
		};
		if(this.field_enter_type.trim().equalsIgnoreCase("SQL")){
			return_value=false;
		}
		if(this.field_enter_type.trim().equalsIgnoreCase("AUTOCOMPLETE")){
			return_value=false;
		}
		return return_value;
	}
	/**
	 * @return является ли данная переменная, которая есть вариантом выбора (SELECT)<br>
	 * <li> true - нужно ставить атрибут multiply для выбора - тип данных List</li>
	 * <li> false - не нужно ставить атрибут - тип данных combobox</li>
	 */
	public boolean isMultiChoice(){
		boolean return_value=false;
		if(this.field_multi_choice.trim().equalsIgnoreCase("Y")){
			return_value=true;
		};
		if(this.field_multi_choice.trim().equalsIgnoreCase("N")){
			return_value=false;
		}
		return return_value;
	}
	/** 
	 * @return является ли число автоматически загружаемым 
	 */
	public boolean isOnLoad(){
		boolean return_value=false;
		if(this.field_on_load.trim().equalsIgnoreCase("Y")){
			return_value=true;
		};
		if(this.field_on_load.trim().equalsIgnoreCase("N")){
			return_value=false;
		}
		return return_value;
	}
	/**
	 * @return является ли поле обязательным
	 * <li> true - обязательное поле </li>
	 * <li> false - не обязательное поле </li>
	 */
	public boolean isRequired(){
		boolean return_value=false;
		if(this.field_required.trim().equalsIgnoreCase("Y")){
			return_value=true;
		};
		if(this.field_required.trim().equalsIgnoreCase("N")){
			return_value=false;
		}
		return return_value;
	}

	/**
	 * получить значение параметра на основании ключа
	 * @param key - ключ из словаря
	 */
	public String getValueByKey(Connection connection, String pNameParam, String key){
		String return_value="";
		ResultSet resultSet=null;
		try{
			LOGGER.debug("Отработать запрос: "+field_sql_text_get_value_preambule+key.replaceAll("'","''")+field_sql_text_get_value_postambule);
			LOGGER.debug("getValueByKey:"+key);
			resultSet=connection.createStatement().executeQuery(field_sql_text_get_value_preambule+key.replaceAll("'","''")+field_sql_text_get_value_postambule);
			if(resultSet.next()){
				
				return_value=resultSet.getString(1);
				LOGGER.debug("getValueByKey:"+key+"  Value:"+return_value);
			}
		}catch(SQLException ex){
			LOGGER.error("SQLException при получении значения на основании ключа: "+ex.getMessage());
		}catch(Exception e){
			LOGGER.error("Exception при получении значения на основании ключа: "+e.getMessage());
		}finally{
			UtilityConnector.closeQuietly(resultSet);
		}
		return return_value;
	}
	/**
	 * получить значение параметра на основании ключей - MULTIChoice
	 */
	public String getValueByKey(Connection connection, String pNameParam, String[] keys){
		StringBuilder return_value=new StringBuilder();
		for(int counter=0;counter<keys.length;counter++){
			return_value.append(this.getValueByKey(connection, pNameParam, keys[counter]));
			if(counter!=(keys.length-1)){
				return_value.append(", ");
			}
		}
		if (return_value == null || "".equalsIgnoreCase(return_value.toString())) {
			return_value.append(pNameParam);
		}
		LOGGER.debug("Variable.getValueByKey(): return_value="+return_value.toString());
		return return_value.toString();
	}
	
	/** @return true если нужно задать для данного поля на странице HTML ширину объекта */
	private boolean isWidthHtmlNeedSet(){
		boolean return_value=true;
		String prepare_value=this.field_width.trim();
		if(prepare_value.trim().equals("")){
			return_value=false;
		};
		if(prepare_value.equalsIgnoreCase("NULL")){
			return_value=false;
		}
		if(prepare_value.trim().equals("0")){
			return_value=false;
		}
		return return_value;
	}
	
	/**
	 * получить значение по умолчанию
	 */
	public String getDefaultValue(){
		LOGGER.debug("Default value = "+this.field_default_value);
		return this.field_default_value;
	}
	
	/** получить имя функции, которая будет вызываться при изменении значения в блоке SELECT - для наполнения зависимых значений */
	public String getSelectChangeFunctionName(){
		return field_javascript_function_onchange;		
	}
}