<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_SAM";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_sam = Bean.getDecodeParamPrepare(parameters.get("id_sam"));

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
			window.opener.document.getElementById('id_sam').value = id;
			window.opener.document.getElementById('id_sam').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_SAM', 
					'', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_SAM", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(id_sam)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_SAM", false)) %>
	
	<%=Bean.getServiceFindTable(id_sam) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.samXML.getfieldTransl("ID_SAM", false)%></th>
		<th><%= Bean.samXML.getfieldTransl("SAM_SERIAL_NUMBER", false)%></th>
		<th><%= Bean.samXML.getfieldTransl("NAME_CARD_TYPE", false)%></th>
		<th><%= Bean.samXML.getfieldTransl("NAME_CARD_STATUS", false)%></th>
		<th><%= Bean.samXML.getfieldTransl("DATE_BEG_FRMT", false)%></th>
		<th><%= Bean.samXML.getfieldTransl("EXPIRY_DATE_FRMT", false)%></th>
		<th><%= Bean.samXML.getfieldTransl("DATE_END_FRMT", false)%></th>
		<th><%= Bean.samXML.getfieldTransl("ID_TERM", false)%></th>
		<th><%= Bean.samXML.getfieldTransl("NAME_USER", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>