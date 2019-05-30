<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_NAT_PRS_ROLE";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));

String id_nat_prs_role = Bean.getDecodeParamPrepare(parameters.get("id_nat_prs_role"));
String full_name = Bean.isEmpty(id_nat_prs_role)?"":Bean.getNatPrsNameFromRole(id_nat_prs_role);

String id_jur_prs = Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));

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
			window.opener.document.getElementById('name_<%=field%>').value = Name;
			window.opener.document.getElementById('id_<%=field%>').value = id;
			window.opener.document.getElementById('name_<%=field%>').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_NAT_PRS_ROLE', 
					'<%=id_jur_prs%>', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_NAT_PRS_ROLE", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(full_name)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_NAT_PRS_ROLE", false)) %>
	
	<%=Bean.getServiceFindTable(full_name) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.natprsXML.getfieldTransl("FULL_NAME", false)%></th>
		<th><%= Bean.natprsXML.getfieldTransl("PHONE_MOBILE", false)%></th>
		<th><%= Bean.natprsXML.getfieldTransl("CD_CARD1", false)%></th>
		<th><%= Bean.natprsXML.getfieldTransl("DATE_CARD_SALE", false)%></th>
		<th><%= Bean.natprsXML.getfieldTransl("NAME_NAT_PRS_ROLE_STATE", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>