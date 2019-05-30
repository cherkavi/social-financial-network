<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_DS_PARTNER_PATTERN";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_pt_pattern = Bean.getDecodeParamPrepare(parameters.get("id_pt_pattern"));
String name_pt_pattern = Bean.isEmpty(id_pt_pattern)?"":id_pt_pattern; //Bean.getDSPartnerPatternName(id_pt_pattern);

String type_message = Bean.getDecodeParamPrepare(parameters.get("type_message"));
String field_name = Bean.getDecodeParamPrepare(parameters.get("field_name"));
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
		function changeOpener(id, Name, cdCur, nameCur){
			window.opener.document.getElementById('id_<%=field_name%>').value = id;
			window.opener.document.getElementById('name_<%=field_name%>').value = Name;
			window.opener.document.getElementById('name_<%=field_name%>').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_DS_PT_PATTERN', 
					'', 
					'<%=type_message%>');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_GIFTS", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_GIFTS", false)) %>
	
	<%=Bean.getServiceFindTable(name_pt_pattern) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.messageXML.getfieldTransl("ID_PATTERN", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("TYPE_MESSAGE", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("NAME_PATTERN", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("TEXT_MESSAGE", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("BEGIN_ACTION_DATE", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("END_ACTION_DATE", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>