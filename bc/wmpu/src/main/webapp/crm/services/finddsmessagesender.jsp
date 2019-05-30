<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_DISPATCH_MESSAGE_SENDER";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String message_type = Bean.getDecodeParamPrepare(parameters.get("message_type"));
String message_contact = Bean.getDecodeParamPrepare(parameters.get("message_contact"));
String sender_kind = Bean.getDecodeParamPrepare(parameters.get("sender_kind"));
String field_name = Bean.getDecodeParamPrepare(parameters.get("field_name"));
String id_sender = Bean.getDecodeParamPrepare(parameters.get("id_sender"));
String name_sender = Bean.getDecodeParamPrepare(parameters.get("name_sender"));
String function_name = "";

if ("PHONE_MOBILE".equalsIgnoreCase(message_type)) {
	function_name = "GET_MESSAGE_SENDER_FOR_PHONE";
} else if ("EMAIL".equalsIgnoreCase(message_type)) {
	function_name = "GET_MESSAGE_SENDER_FOR_EMAIL";
}

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
		function changeOpener(id_value, name_value, dispatch_kind){
			window.opener.document.getElementById('id_<%=field_name%>').value = id_value;
			window.opener.document.getElementById('name_<%=field_name%>').value = name_value;
			window.opener.document.getElementById('name_<%=field_name%>').className = "inputfield_modified";
			window.opener.document.getElementById('cd_dispatch_kind').value = dispatch_kind;
			window.close();
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		function send_request() {
			send_request_to_server(
					'<%=function_name%>', 
					'<%=sender_kind%>', 
					'<%=message_contact%>');
		}
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_DISPATCH_MESSAGE_SENDER", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_DISPATCH_MESSAGE_SENDER", false)) %>
	
	<%=Bean.getServiceFindTable(name_sender) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.messageXML.getfieldTransl("ID_SENDER", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("NAME_SENDER", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("DISPATCH_KIND", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("DESC_SENDER", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("PHONE_MOBILE", false)%></th>
		<th><%= Bean.messageXML.getfieldTransl("EMAIL", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>