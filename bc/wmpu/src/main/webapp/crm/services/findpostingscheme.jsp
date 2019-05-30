<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_OPER_SCHEME";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));

String id_oper_scheme = Bean.getDecodeParamPrepare(parameters.get("id_bk_operation_scheme_line"));
String name_oper_scheme = Bean.isEmpty(id_oper_scheme)?"":Bean.getBKOperationSchemeLineName(id_oper_scheme);

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
					'GET_BK_OPERATION_SCHEME_LINES', 
					'', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_POSTING_SETTINGS", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(name_oper_scheme)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_POSTING_SETTINGS", false)) %>
	
	<%=Bean.getServiceFindTable(name_oper_scheme) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.posting_schemeXML.getfieldTransl("ID_BK_OPERATION_SCHEME_LINE", false)%></th>
		<th><%= Bean.posting_schemeXML.getfieldTransl("NAME_CLUB_REL_TYPE", false)%></th>
		<th><%= Bean.posting_schemeXML.getfieldTransl("NAME_BK_OPERATION_TYPE", false)%></th>
		<th><%= Bean.posting_schemeXML.getfieldTransl("NAME_BK_PHASE", false)%></th>
		<th><%= Bean.posting_schemeXML.getfieldTransl("OPER_NUMBER", false)%></th>
		<th><%= Bean.posting_schemeXML.getfieldTransl("DEBET_CD_BK_ACCOUNT", false)%></th>
		<th><%= Bean.posting_schemeXML.getfieldTransl("CREDIT_CD_BK_ACCOUNT", false)%></th>
		<th><%= Bean.posting_schemeXML.getfieldTransl("OPER_CONTENT", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>