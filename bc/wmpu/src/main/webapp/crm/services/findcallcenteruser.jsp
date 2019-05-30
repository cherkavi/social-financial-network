<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_CALL_CENTER_USERS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_user = Bean.getDecodeParamPrepare(parameters.get("id_user"));
String name_user = Bean.isEmpty(id_user)?"":Bean.getBCUserName(id_user);

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
			window.opener.document.getElementById('id_user').value = id;
			window.opener.document.getElementById('name_user').value = Name;
			window.opener.document.getElementById('name_user').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_CALL_CENTER_USERS', 
					'', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_USERS", false))%>');
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(name_user)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_USERS", false)) %>
	
	<%=Bean.getServiceFindTable(name_user) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.userXML.getfieldTransl("ID_USER", false)%></th>
		<th><%= Bean.userXML.getfieldTransl("NAME_USER", false)%></th>
		<th><%= Bean.userXML.getfieldTransl("DESC_USER", false)%></th>
		<th><%= Bean.userXML.getfieldTransl("NAME_MODULE_TYPE", false)%></th>
		<th><%= Bean.userXML.getfieldTransl("CD_USER_STATUS", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>