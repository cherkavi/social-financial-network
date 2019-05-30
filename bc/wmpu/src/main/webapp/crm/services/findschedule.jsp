<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_LOYALITY_SCHEDULE";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_schedule = Bean.getDecodeParamPrepare(parameters.get("id_schedule"));
String name_schedule = Bean.isEmpty(id_schedule)?"":Bean.getLoyScheduleName(id_schedule);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));
field = Bean.isEmpty(field)?"shedule":field;

%>
<head>
	<%=Bean.getMetaContent() %>
	<script src="../../dwr/interface/responseUtility.js" language="javascript" type="text/javascript"></script>
	<script src="../../dwr/interface/ReporterUtility.js" language="javascript" type="text/javascript"></script>
	<script src="../../dwr/engine.js" language="javascript" type="text/javascript"></script>

	<link href="../../CSS/div.css" rel="stylesheet" type="text/css">
	<link href="../../CSS/tablesorter.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/main/services.js"></script>
		
	<script>
		function changeOpener(id, Name){
			window.opener.document.getElementById('id_<%=field%>').value = id;
			window.opener.document.getElementById('name_<%=field%>').value = Name;
			window.opener.document.getElementById('name_<%=field%>').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_SCHEDULE', 
					'', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_SHEDULE", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_SHEDULE", false)) %>
	
	<%=Bean.getServiceFindTable(name_schedule) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.sheduleXML.getfieldTransl("ID_SHEDULE", false)%></th>
		<th><%= Bean.sheduleXML.getfieldTransl("NAME_SHEDULE", false)%></th>
		<th><%= Bean.sheduleXML.getfieldTransl("DESC_SHEDULE", false)%></th>
		<th><%= Bean.sheduleXML.getfieldTransl("NAME_LOYALITY_SCHEME_DEFAULT", false)%></th>
		<th><%= Bean.sheduleXML.getfieldTransl("DATE_BEG_FRMT", false)%></th>
		<th><%= Bean.sheduleXML.getfieldTransl("DATE_END_FRMT", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>