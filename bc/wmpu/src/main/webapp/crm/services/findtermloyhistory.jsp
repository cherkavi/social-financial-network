<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_LOYALITY";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_term = Bean.getDecodeParamPrepare(parameters.get("id_term"));
String id_service_place = Bean.getDecodeParamPrepare(parameters.get("id_service_place"));
String findString = Bean.getDecodeParamPrepare(parameters.get("find"));

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
			window.opener.changeLoyHistoryParam(id);
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_TERM_LOY_HISTORY', 
					'<%=id_term%>', 
					'<%=id_service_place%>');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_LOY_SCHEME", false))%>');
		var myTitle = '<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_LOY_SCHEME", false))%>';
		document.title=myTitle;
		//alert (myTitle);
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_LOY_SCHEME", false)) %>

	<%=Bean.getServiceFindTable(findString) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.commonXML.getfieldTransl("RN", false)%></th>
		<th><%= Bean.loyXML.getfieldTransl("ID_LOYALITY_HISTORY", false)%></th>
		<th><%= Bean.terminalXML.getfieldTransl("ID_TERM", false)%></th>
		<th><%= Bean.terminalXML.getfieldTransl("NAME_LOYALITY_SCHEME", false)%></th>
		<th><%= Bean.loyXML.getfieldTransl("CD_KIND_LOYALITY", false)%></th>
		<th><%= Bean.loyXML.getfieldTransl("DATE_BEG_FRMT", false)%></th>
		<th><%= Bean.loyXML.getfieldTransl("DATE_END_FRMT", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>

</body>
</html>