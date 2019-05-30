<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_CLUB_EVENT_GIFTS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_club_event = Bean.getDecodeParamPrepare(parameters.get("id_club_event"));
String id_gift = Bean.getDecodeParamPrepare(parameters.get("id_gift"));
String name_gift = Bean.getDecodeParamPrepare(parameters.get("name_gift"));

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
		function changeOpener(id_club_event_gift, name_club_event_gift){
			window.opener.document.getElementById('id_club_event_gift').value = id_club_event_gift;
			window.opener.document.getElementById('name_club_event_gift').value = name_club_event_gift;
			window.opener.document.getElementById('name_club_event_gift').className = "inputfield_modified";
			window.close();
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		function send_request() {
			send_request_to_server(
					'GET_CLUB_EVENT_GIFTS2', 
					'<%=id_club_event%>', 
					'');
		}
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_CLUB_EVENT_GIFTS", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_CLUB_EVENT_GIFTS", false)) %>
	
	<%=Bean.getServiceFindTable(name_gift) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.club_actionXML.getfieldTransl("NAME_CLUB_EVENT", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("CD_GIFT", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("NAME_GIFT", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("COUNT_GIFT_ALL", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("COUNT_GIFT_RESERVED", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("COUNT_GIFT_GIVEN", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("COUNT_GIFT_REMAIN", false)%></th>
		<th><%= Bean.club_actionXML.getfieldTransl("IS_ACTIVE_TSL", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>