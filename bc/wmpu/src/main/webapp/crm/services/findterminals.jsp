<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_TERMINALS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_term = Bean.getDecodeParamPrepare(parameters.get("id_term"));
String id_device_type = Bean.getDecodeParamPrepare(parameters.get("id_device_type"));
String id_service_place = Bean.getDecodeParamPrepare(parameters.get("id_service_place"));
String name_service_place = Bean.isEmpty(id_service_place)?"":Bean.getJurPersonShortName(id_service_place);
String field = Bean.getDecodeParamPrepare(parameters.get("field"));
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
		function changeOpener(id){
			window.opener.document.getElementById('id_<%=field%>').value = id;
			window.opener.document.getElementById('id_<%=field%>').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_TERMINALS', 
					'<%=id_device_type%>', 
					'<%=id_service_place%>');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_TERMINALS", false))%><%=!Bean.isEmpty(name_service_place)?" ("+name_service_place+")":""%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(id_term)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_TERMINALS", false) + (!Bean.isEmpty(name_service_place)?" ("+name_service_place+")":"")) %>
	
	<%=Bean.getServiceFindTable(id_term) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.terminalXML.getfieldTransl("ID_TERM", false)%></th>
		<th><%= Bean.terminalXML.getfieldTransl("ID_TERM_HEX", false)%></th>
		<th><%= Bean.terminalXML.getfieldTransl("NAME_TERM_TYPE", false)%></th>
		<th><%= Bean.terminalXML.getfieldTransl("NAME_SERVICE_PLACE", false)%></th>
		<th><%= Bean.terminalXML.getfieldTransl("ADR_SERVICE_PLACE", false)%></th>
		<th><%= Bean.terminalXML.getfieldTransl("NAME_TERM_STATUS", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>