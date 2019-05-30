<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_GIFTS_LOGISTIC";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_lg_gift = Bean.getDecodeParamPrepare(parameters.get("id_lg_gift"));
String id_gift = Bean.getDecodeParamPrepare(parameters.get("id_gift"));
String desc_gift = Bean.getDecodeParamPrepare(parameters.get("desc_gift"));
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
			window.opener.document.getElementById('id_lg_gift').value = id;
			window.opener.document.getElementById('name_lg_gift').value = Name;
			window.opener.document.getElementById('name_lg_gift').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_GIFTS_LOGISTIC', 
					'<%=id_gift%>', 
					'FALSE_FALSE');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_GIFTS_LOGISTIC", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_GIFTS_LOGISTIC", false)) %>
	
	<%=Bean.getServiceFindTable(desc_gift) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.club_actionXML.getfieldTransl("LG_ACTION_DATE_FRMT", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("CD_GIFT", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("NAME_GIFT", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("DESC_GIFT", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("COST_ONE_GIFT", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("COUNT_GIFT_ALL", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("COUNT_GIFT_GIVEN", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("COUNT_GIFT_REMAIN", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>