<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_SERVICEPLACE";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_service_place = Bean.getDecodeParam(parameters.get("id_service_place"));
String name_service_place = Bean.isEmpty(id_service_place)?"":Bean.getServicePlaceShortName(id_service_place);

String id_jur_prs = Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String id_term = Bean.getDecodeParamPrepare(parameters.get("id_term"));
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
		function changeOpener(id, Name){
			window.opener.document.getElementById('id_<%=field%>').value = id;
			window.opener.document.getElementById('name_<%=field%>').value = Name;
			window.opener.document.getElementById('name_<%=field%>').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_SERVICE_PLACES', 
					'<%=id_jur_prs%>', 
					'<%=id_term%>');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_SERVICE_PLACES", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_SERVICE_PLACES", false)) %>
	
	<%=Bean.getServiceFindTable(name_service_place) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.jurpersonXML.getfieldTransl("NAME_SERVICE_PLACE", false)%></th>
		<th><%= Bean.jurpersonXML.getfieldTransl("JUR_PRS", false)%></th>
		<th><%= Bean.jurpersonXML.getfieldTransl("ADR_FULL", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>