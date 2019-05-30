<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_JUR_PRS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_jur_prs = Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String name_jur_prs = Bean.isEmpty(id_jur_prs)?"":Bean.getJurPersonShortName(id_jur_prs);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));

String type = Bean.getDecodeParamPrepare(parameters.get("type"));
String typeName = "";
String typeFiltr = "";
if (Bean.isEmpty(type)) {
	type = "ALL";
	typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS", false);
} else {
	if ("ALL".equalsIgnoreCase(type)) {
		typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS", false);
	} else if ("DEALER".equalsIgnoreCase(type)) {
		typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS_DEALER", false);
	} else if ("BANK".equalsIgnoreCase(type)) {
		typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS_BANKS", false);
	} else if ("ISSUER".equalsIgnoreCase(type)) {
		type = "ALL";
		typeFiltr = "ISSUER";
		typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS_ISSUER", false);
	} else if ("FIN_ACQUIRER".equalsIgnoreCase(type)) {
		type = "ALL";
		typeFiltr = "FIN_ACQUIRER";
		typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS_FIN_ACQUIRER", false);
	} else if ("TECH_ACQUIRER".equalsIgnoreCase(type)) {
		type = "ALL";
		typeFiltr = "TECH_ACQUIRER";
		typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS_TECH_ACQUIRER", false);
	} else if ("TERMINAL_MANUFACTURER".equalsIgnoreCase(type)) {
		typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS_TERMINAL_MANUFACTURER", false);
	} else if ("PARTNER".equalsIgnoreCase(type)) {
		typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS_PARTNER", false);
	} else if ("CARD_SELLER".equalsIgnoreCase(type)) {
		typeName = Bean.commonXML.getfieldTransl("FIND_JUR_PRS_CARD_SELLER", false);
	}
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
		function changeOpener(id, Name){
			window.opener.document.getElementById('id_<%=field%>').value = id;
			window.opener.document.getElementById('name_<%=field%>').value = Name;
			window.opener.document.getElementById('name_<%=field%>').className = "inputfield_modified";
			window.close();
		}
	
		function send_request() {
			send_request_to_server(
					'GET_JUR_PRS', 
					'<%=type%>', 
					'<%=typeFiltr%>');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(typeName)%>');
	</script>
</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">
	<%=Bean.getServiceHeaderTable(typeName) %>
	
	<%=Bean.getServiceFindTable(name_jur_prs) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<!-- <th><%= Bean.jurpersonXML.getfieldTransl("ID_JUR_PRS", false)%>1</th>
		<th><%= Bean.jurpersonXML.getfieldTransl("SNAME_JUR_PRS", false)%></th> -->
		<th><%= Bean.jurpersonXML.getfieldTransl("NAME_JUR_PRS", false)%></th>
		<th><%= Bean.jurpersonXML.getfieldTransl("INN_NUMBER", false)%></th>
		<th><%= Bean.jurpersonXML.getfieldTransl("JUR_ADR_FULL", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		<tr>
		<td></td>
		</tr>
	</tbody>
</table>

</body>
</html>