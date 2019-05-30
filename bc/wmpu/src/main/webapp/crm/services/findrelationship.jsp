<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_RELATIONSHIP";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_club_rel = Bean.getDecodeParamPrepare(parameters.get("id_club_rel"));
String name_club_rel = Bean.isEmpty(id_club_rel)?"":Bean.getClubRelationshipName(id_club_rel);

String id_participant = Bean.getDecodeParamPrepare(parameters.get("id_participant"));

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
		function changeOpener(id, name){
			window.opener.changeRelationShipParam(id, name);
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_CLUB_RELATIONSHIPS', 
					'<%=id_club_rel%>', 
					'<%=id_participant%>');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_RELATIONSHIP", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_RELATIONSHIP", false)) %>
	
	<%=Bean.getServiceFindTable(name_club_rel) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.relationshipXML.getfieldTransl("ID_CLUB_REL", false)%></th>
		<th><%= Bean.relationshipXML.getfieldTransl("NAME_CLUB_REL", false)%></th>
		<th><%= Bean.relationshipXML.getfieldTransl("NAME_CLUB_REL_TYPE", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>