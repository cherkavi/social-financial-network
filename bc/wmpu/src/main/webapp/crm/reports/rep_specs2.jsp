<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"
    import="java.sql.*,BonCard.*,BonCard.DataBase.*,BonCard.Reports.*,java.util.*"
    %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
	
<%= Bean.getLogOutScript(request) %>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="../CSS/main.css">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<%-- добавить обязательно для загрузки данных--%>

<%
String type = Bean.getDecodeParam(parameters.get("type"));
String id_entry = Bean.getDecodeParam(parameters.get("id_entry"));
if (type==null || "".equals(type)) {
	type = "all";
}
String goBackHyperLink = "";
String pageFormName = "";

if ("all".equalsIgnoreCase(type)) {
	goBackHyperLink = "../crm/reports/rep_system.jsp";
	pageFormName = "REPORTS_SYSTEM";
} else if ("selected".equalsIgnoreCase(type)) {
	goBackHyperLink = "../crm/reports/rep_selected.jsp";
	pageFormName = "REPORTS_SELECTED";
} else if ("user".equalsIgnoreCase(type)) {
	goBackHyperLink = "../crm/crm/security/userspecs.jsp?id=" + id_entry;
	pageFormName = "REPORTS_SYSTEM";
} else if ("role".equalsIgnoreCase(type)) {
	goBackHyperLink = "../crm/crm/security/rolespecs.jsp?id=" + id_entry;
	pageFormName = "REPORTS_SYSTEM";
}


Bean.setJspPageForTabName(pageFormName);
%>

<body id="body">

<div id="console">
</div>	
<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
		</tr>
	</table>
<script type="text/javascript" language="javascript">
<%-- Объект, который имитирует двухмерную таблицу, с возможностями добавления и поиска по ней --%>
util=new Object();
// constructor
util.table=function(dimension){
	this.field_columns=new Array();
	this.field_rows=new Array();
	for(var counter=0;counter<dimension;counter++){
		this.field_columns[counter]="";
	}
}
// установить текст для заголовков
util.table.prototype.setColumnText=function(column_number,column_name){
	this.field_columns[column_number]=column_name;
}
// установить значение для таблицы
util.table.prototype.setValue=function(row_number,column_number,value){
	if(this.field_rows.length<=row_number){
		this.field_rows[row_number]=new Array();
	}
	this.field_rows[row_number][column_number]=value;
}
// получить значение по ячейке
util.table.prototype.getValue=function(row_number,column_number){
	return this.field_rows[row_number][column_number];
}
// добавить еще одну строку в виде массива
util.table.prototype.addRow=function(array){
	this.field_rows[this.field_rows.length]=array;
}
// получить кол-во строк
util.table.prototype.getRowCount=function(){
	return this.field_rows.length;
}
// вернуть true если существует ключ
util.table.prototype.keyExists=function(column_number,value){
	var return_value=false;
	for(var counter=0;counter<this.field_rows.length;counter++){
		if(this.getValue(counter,column_number)==value){
			return_value=true;
			break;
		}
	}
	return return_value;
}
// вернуть массив из индексов ключей
util.table.prototype.getIndexArray=function(column_number,value){
	var return_value=new Array();
	for(var counter=0;counter<this.field_rows.length;counter++){
		if(this.getValue(counter,column_number)==value){
			return_value[return_value.length]=counter;
		}
	}
	return return_value;
}
// функция вывода на консоль
util.table.prototype.out=function(){
	for(var row_counter=0;row_counter<this.field_rows.length;row_counter++){
		var current_string="";
		for(var column_counter=0;column_counter<this.field_columns.length;column_counter++){
			current_string=current_string+"  |  "+column_counter+":"+this.getValue(row_counter,column_counter);
		}
	}
}
</script>
<script type="text/javascript">
function select_all(tag_id){
	var select_object=document.getElementById(tag_id);
	for(var counter=0;counter<select_object.options.length;counter++){
		//select_object.childNodes[counter].selected="selected";
		select_object.options[counter].selected=true;
	}
}
function deselect_all(tag_id){
	var select_object=document.getElementById(tag_id);
	for(var counter=0;counter<select_object.options.length;counter++){
		select_object.options[counter].selected=false;
	}
	if(select_object.childNodes.length>0){
		if(select_object.childNodes[0]!=null){
			select_object.options[0].selected=true;	
		}else{
			select_object.options[1].selected=true;
		}
	}
}
</script>

<script type="text/javascript" language="javascript" src="../dwr/interface/ReporterUtility.js"></script>
<script type="text/javascript" language="javascript" src="../dwr/engine.js"></script>

<script type="text/javascript" language="javascript">
	<%-- создание объекта, который содержит данные обо всех зависимых элементах --%>
	var field_data;
	field_data=new util.table(4);
	field_element_width=new util.table(2);
	
	<%-- служебная функция вывода данных на консоль  --%>
	function toConsole(data){
		var console=document.getElementById('console');
		if (console!=null){
			var newline=document.createElement("div");
		    console.appendChild(newline);
		    var txt=document.createTextNode(data);
		    newline.appendChild(txt);
		  }
	}

	
	<%-- функция, которая получает значение по выделенному элементу SELECT --%>
	function get_select_value(element_id){
		var return_value="";
		var element=document.getElementById(element_id);
		if(element!=null){
			for(var counter=0;counter<element.childNodes.length;counter++){
				var current_element=element.childNodes.item(counter);
				if(current_element.selected){
					if(return_value!=""){
						return_value=return_value+","+current_element.value;						
					}else{
						return_value=current_element.value;
					}
				}
			}				
		}
		return return_value;
	}
	var response_value;
	<%-- функция, которая получает управление во время возврата значения  --%>
	function get_server_responce(responce){
		<%-- ответ сервера состоит из объекта Fragment:
			String Function;
			String[] Parameters;
			//    Parameters[]:[0] ID element
			//	  Parameters[]:[1] Default_value
			//	  Values[][]:[0] - SELECT.OPTION.values
			//    Values[][]:[1] - SELECT.OPTION.text
		--%>
		try{
			if(responce.name!="error"){
				// check function
				if(responce.name=="FILL_ELEMENT"){
					load_data_to_select(responce.parameters[0],responce.parameters[1],responce.values[0],responce.values[1]);
				}
			}
		}catch(Exception){
			
		}
	}

	<%-- функция, которая заполняет нужный SELECT(id_select)   
		default_value - значение OPTION.VALUE по умолчанию - нужно поставить тэг select
		array_of_values - значения для OPTION.VALUE
		array_of_text - значения для OPTION.TEXT
	--%> 
	function load_data_to_select(id_select,default_value,array_of_values, array_of_text){
		var select_element=document.getElementById(id_select);
		if(select_element!=null){
			for(var counter=select_element.childNodes.length-1;counter>=0;counter--){
				select_element.removeChild(select_element.childNodes[counter]);
			}
			<%-- select_element.innerHTML=html_text; --%>
			for(counter=0;counter<array_of_values.length;counter++){
				var option_element=document.createElement("option");
				var text_element=document.createTextNode(array_of_text[counter]);
				option_element.value=array_of_values[counter];
				if(array_of_values[counter]==default_value){
					option_element.selected="selected";
				}
				option_element.appendChild(text_element);
				select_element.appendChild(option_element);
			}
			if(select_element.length>5)select_element.size=5;
		}
	}
	<%-- функция, которая обращается с запросом к серверу  
		id_function - идентификатор функции
		value_source - 	двухмерный массив возможных параметров для запроса ( сжимаем ID_DESTINATION,NUMBER,ORDER) получаем:
						<ID_SOURCE>	<value of ID_SOURCE>
						<id_source> - идентификатор HTML, который инициировал запрос, и является необходимым параметром для запроса на сервере для приемника (id_destination)
									<value of ID_SOURCE> - значение идентификатора, который инициировал запрос
		id_destination - идентификатор HTML, который будет принимать данные с сервера 
		report_number - необходимые данные для получения параметров на сервере (получения самого запроса)
		order_number - необходимые данные для получения параметров на сервере (получения самого запроса)
	--%>
	function send_data(id_function,value_source,id_destination,report_number,order_number){
		 var fragment=new Object();
		 fragment.name=id_function;
		 fragment.parameters=new Array(id_destination,report_number,order_number);
		 fragment.values=value_source;
		 fragment.sessionId = '<%=Bean.getSessionId()%>';
		 ReporterUtility.sendToServer(fragment,get_server_responce);
	}
	<%-- функция, которая анализирует объект SELECT, который вызвал событий onChange --%>
	function on_change_select(changed_object){
		<%-- поиск в объекте field_data уникального ID измененного элемента, и если он найден - заполнение объекта source --%>
		var array_of_element=field_data.getIndexArray(0,changed_object.id);
		for(var counter=0;counter<array_of_element.length;counter++){
			send_data('FILL_ELEMENT',get_values(array_of_element[counter]),field_data.getValue(array_of_element[counter],1),field_data.getValue(array_of_element[counter],2),field_data.getValue(array_of_element[counter],3));
		}
		
	} 
	<%-- функция, которая возвращает двухмерный массив из значений, которые получены из сжатия/GROUP_BY  <ID_DESTINATION><NUMBER><ORDER> 
		получаем <ID_SOURCE> <value of ID_SOURCE>
	--%>
	function get_values(control_counter){
		var return_value=new Array();
		var temp_value=field_data.getRowCount();
		<%-- пробежаться по всему списку и получить все значения --%>
		for(var index=0;index<field_data.getRowCount();index++){
			if(  (field_data.getValue(control_counter,1)==field_data.getValue(index,1))
			   &&(field_data.getValue(control_counter,2)==field_data.getValue(index,2))
			   &&(field_data.getValue(control_counter,3)==field_data.getValue(index,3))
				){
				<%-- add to return_value--%>
				return_value[return_value.length]=new Array(field_data.getValue(index,0),get_select_value(field_data.getValue(index,0)));
			}else{
				<%-- nothing adding--%>
			}
		}
		return return_value;
	}

	<%-- функция, которая присоединяет данные, полученные с текущей страницей к объекту field_data
	  param_name,  - имя параметра из запроса
	  this_html_name, - имя HTML от которого зависит данных запрос
	  report_id, - номер отчета
	  order_number - номер переменной в запросе --%>	
	function fill_data(id_destination,id_source,report_id,order_number){
		//window.alert(id_destination);
		field_data.addRow(new Array(id_destination,id_source,report_id,order_number));
	}

	<%-- функция, которая присоединяет данные о необходимой ширине объекта после загрузки страницы --%>
	function add_object_width(element_id, element_width){
		field_element_width.addRow(new Array(element_id,element_width));
	}

	<%-- функция, которая задает ширину для объекта --%>
	function set_width(element_name, value){
		try{
			document.getElementById(element_name).style.width = value+'px';
		}catch(exception){
		}
	}
	<%-- функция, которая перебирает все элементы, которым необходимо задать ширину --%>
	function set_width_for_all(){
		var element_name;
		var element_width;
		//window.alert("set_width_for_all()");
		for(var index=0;index<field_element_width.getRowCount();index++){
			element_name=field_element_width.getValue(index,0);
			element_width=field_element_width.getValue(index,1);
			//set_width(field_element_width.getValue(index,0),field_element_width.getValue(index,1));
			set_width(element_name,element_width);
		}
	} 	
</script>
<%
		//String url="index.jsp";
		String url= "../reports/Reporter";
		String report_number=Bean.getDecodeParam(parameters.get("id"));
		// получить соединение и отобразить по отчету все переменные
	        //out.println("connect OK:<br>");
	        // положить Connection в сессию
	        //pageContext.getSession().removeAttribute("CONNECTION");
	        //pageContext.getSession().setAttribute("CONNECTION",con);
	        Connection temp_connection=(Connection)pageContext.getSession().getAttribute("CONNECTION");
	        //out.println("connection:"+temp_connection+" <br>");
	        // получить объект, который служит для дополнительных манипуляций с данными
	        ReportVariables report_variables=new ReportVariables(report_number, Bean.getSessionId(), Bean.getJSPDateFormat());
	        //out.println("All variable:<b>"+report_variables.getVariablesCount()+"</b><br>");
	        // добавить JavaScript в функцию onLoad
	        
			out.println("<br><center><font size=\"+2\">" + report_variables.getReportName() + "</font></center>");
        	out.println(report_variables.getReportNotes());
	        %>
			<script type="text/javascript">
	        function server_onload(){	        	
	        	<%=report_variables.getOnLoadJavaScript()%>
	        }
	        server_onload();
			set_width_for_all();

</script>
	        <%
	        
	        if ("WORKS".equalsIgnoreCase(report_variables.getReportState())) {
		        // отобразить код всех переменных на форме
		        if(report_variables.getVariablesCount()>0){
		        	out.println("<center>");
		        	out.println("<form action=\""+url+"\" target=\"_report"+report_number+"\">");
		        	out.println("<table " + Bean.getTableReportParam() + ">");
	        		out.println("<tr>");
	        		out.println("<td>"+Bean.reportXML.getfieldTransl("ID_REPORT", false)+"</td>");
	        		out.println("<td valign=\"top\"><b>"+report_number+"</b></td>");
	        		out.println("</tr>");
		        	for(int counter=0;counter<report_variables.getVariablesCount();counter++){
		        		out.println("<tr>");
		        		out.println("<td>"+report_variables.getVariable(counter).getCaption()+"</td>");
		        		out.println("<td valign=\"top\">"+report_variables.getVariable(counter).getHTML()+"</td>");
		        		out.println("</tr>");
		        	}
		        	out.println("<tr>");
		        	out.println("<td>" + Bean.form_messageXML.getfieldTransl("REPORT_FORMAT", false) + "</td>");
		        	out.println("<td><select name=\"REPORT_FORMAT\" id=\"REPORT_FORMAT\" class=\"inputfield\">");
		        	out.println(Bean.getMeaningFromLookupNameOptions("REPORT_FORMAT", Bean.getReportFormat(), false));
		        	
		        	/*
		        	Utility utility=new Utility(con);
			        out.println(utility.getSelectBodyFromQuery("SELECT lookup_code, meaning FROM " + Bean.getGeneralDBScheme() + ". vc_lookup_values WHERE name_lookup_type = 'REPORT_FORMAT' AND enabled_flag = 'Y' ORDER BY meaning", Bean.loginUserParam.getValue("REPORT_FORMAT")));
			        Connector.closeConnection(con);
					*/
			        out.println("</select >");
		        	out.println("<input type=\"hidden\" name=\"REPORT_ID\" value=\""+report_number+"\">");	
		        	out.println("</td></tr>");
		        	out.println("<tr>");
		        	out.println("<td colspan=\"2\" align=center><input type=\"submit\" class=\"button\" value=\"" + Bean.buttonXML.getfieldTransl("show", false) + "\">");
		        	out.println("&nbsp;" + Bean.getGoBackButton(goBackHyperLink) + "</td>");
		        	out.println("</tr>");
		        	out.println("</table>");
		        	out.println("</form>");
		        	out.println("</center>");
		        }
		        // добавить дополнительный код JavaScript на страницу
		        for(int counter=0;counter<report_variables.getVariablesCount();counter++){
		        	out.println(report_variables.getVariable(counter).getAdditionJavaScript());
		        }
	        } else {
	        	out.println("<br><center><font>" + Bean.reportXML.getfieldTransl("title_report_in_development", false) + "</font></center>");
 	        }
%>
</body>

<%@page import="bc.connection.Connector"%></html>