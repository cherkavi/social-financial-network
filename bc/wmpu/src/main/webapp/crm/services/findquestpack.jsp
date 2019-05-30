<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_FAQ";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_quest_pack = Bean.getDecodeParamPrepare(parameters.get("id_quest_pack"));
String name_quest_pack = Bean.isEmpty(id_quest_pack)?"":id_quest_pack;

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
		function changeOpener(id, number){
			window.opener.changePackParam(id, number);
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_QUESTIONAIRE_PACK', 
					'', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_QUESTIONNAIRE_PACK", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_QUESTIONNAIRE_PACK", false)) %>
	
	<%=Bean.getServiceFindTable(name_quest_pack) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.questionnaireXML.getfieldTransl("ID_QUEST_PACK", false)%></th>
		<th><%= Bean.questionnaireXML.getfieldTransl("DATE_RECEPTION_PACK", false)%></th>
		<th><%= Bean.questionnaireXML.getfieldTransl("SNAME_JUR_PR_WHO_HAS_SOLD_CARD", false)%></th>
		<th><%= Bean.questionnaireXML.getfieldTransl("NAME_SERV_PLCE_WHERE_CARD_SOLD", false)%></th>
		<th><%= Bean.questionnaireXML.getfieldTransl("STATE_PACK_TSL", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>