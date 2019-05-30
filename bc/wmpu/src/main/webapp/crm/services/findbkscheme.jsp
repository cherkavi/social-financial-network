<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_POSTING_SETTINGS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String type_bk_account_scheme_line = Bean.getDecodeParamPrepare(parameters.get("type_bk_account_scheme_line"));
if (Bean.isEmpty(type_bk_account_scheme_line)) {
	type_bk_account_scheme_line = "debet_";
} else {
	if ("none".equalsIgnoreCase(type_bk_account_scheme_line)) {
		type_bk_account_scheme_line = "";
	}
}

String id_bk_account_scheme_line = Bean.getDecodeParamPrepare(parameters.get("id_bk_account_scheme_line"));
String name_bk_account_scheme_line = Bean.isEmpty(id_bk_account_scheme_line)?"":Bean.getBKAccountSchemeLineCode(id_bk_account_scheme_line);

String id_bk_account_scheme = Bean.getDecodeParamPrepare(parameters.get("id_bk_account_scheme"));

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
			window.opener.document.getElementById('<%=type_bk_account_scheme_line%>id_bk_account_scheme_line').value = id;
			window.opener.document.getElementById('<%=type_bk_account_scheme_line%>name_bk_account_scheme_line').value = Name;
			window.opener.document.getElementById('<%=type_bk_account_scheme_line%>name_bk_account_scheme_line').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_BK_ACCOUNT_SCHEME', 
					'<%=id_bk_account_scheme%>', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_POSTING_SETTINGS", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(name_bk_account_scheme_line)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_POSTING_SETTINGS", false)) %>
	
	<%=Bean.getServiceFindTable(name_bk_account_scheme_line) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.bk_schemeXML.getfieldTransl("DESC_BK_ACCOUNT_SCHEME", false)%></th>
		<th><%= Bean.bk_schemeXML.getfieldTransl("ID_BK_ACCOUNT_SCHEME_LINE", false)%></th>
		<th><%= Bean.bk_schemeXML.getfieldTransl("CD_BK_ACCOUNT_SCHEME_LINE", false)%></th>
		<th><%= Bean.bk_schemeXML.getfieldTransl("NAME_BK_ACCOUNT_SCHEME_LINE", false)%></th>
		<th><%= Bean.bk_schemeXML.getfieldTransl("IS_GROUP_TSL", false)%></th>
		<th><%= Bean.bk_schemeXML.getfieldTransl("EXIST_FLAG_TSL", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>