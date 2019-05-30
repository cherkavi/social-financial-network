<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_CALL_CENTER_INQUIRER";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_inquirere = Bean.getDecodeParamPrepare(parameters.get("id_cc_inquirere"));
String name_inquirer = Bean.isEmpty(id_inquirere)?"":Bean.getCallCenterInquirerName(id_inquirere);

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
			window.opener.document.getElementById('id_cc_inquirer').value = id;
			window.opener.document.getElementById('name_cc_inquirer').value = Name;
			window.opener.document.getElementById('name_cc_inquirer').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_CALL_CENTER_INQUIRER', 
					'', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_CALL_CENTER_INQUIRER", false))%>');
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(name_inquirer)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_CALL_CENTER_INQUIRER", false)) %>
	
	<%=Bean.getServiceFindTable(name_inquirer) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.call_centerXML.getfieldTransl("ID_CC_INQUIRER", false)%></th>
		<th><%= Bean.call_centerXML.getfieldTransl("NAME_CC_INQUIRER", false)%></th>
		<th><%= Bean.call_centerXML.getfieldTransl("DESC_CC_INQUIRER", false)%></th>
		<th><%= Bean.call_centerXML.getfieldTransl("NAME_CC_INQUIRER_STATE", false)%></th>
		<th><%= Bean.call_centerXML.getfieldTransl("DATE_CC_INQUIRER_FRMT", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>