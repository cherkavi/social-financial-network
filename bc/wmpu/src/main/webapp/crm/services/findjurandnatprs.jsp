<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_JUR_AND_NAT_PRS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));

String findType = Bean.getDecodeParamPrepare(parameters.get("findtype"));
findType = (Bean.isEmpty(findType))?"ALL":findType;

String existType = Bean.getDecodeParamPrepare(parameters.get("existtype"));

String id_entry = Bean.getDecodeParamPrepare(parameters.get("id"));
String name_entry = "";
if (Bean.isEmpty(id_entry)) {
	name_entry = "";
} else if ("NAT_PRS".equalsIgnoreCase(existType)) {
	name_entry = Bean.getNatPrsName(id_entry);
} else {
	name_entry = Bean.getJurPersonShortName(id_entry);
}
String typeId = Bean.getDecodeParamPrepare(parameters.get("typeid"));

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
		function changeOpener(idNatPrs, idJurPrs, nameEntry, typeEntry){
			window.opener.document.getElementById('id_nat_prs').value = idNatPrs;
			window.opener.document.getElementById('id_jur_prs').value = idJurPrs;
			if (typeEntry == 'NAT_PRS') {
				window.opener.document.getElementById('id_entry').value = idNatPrs;
			} else {
				window.opener.document.getElementById('id_entry').value = idJurPrs;
			}
			window.opener.document.getElementById('type_entry').value = typeEntry;
			window.opener.document.getElementById('name_entry').value = nameEntry;
			window.opener.document.getElementById('name_entry').className = "inputfield_modified";
			window.close();
		}
	
		function send_request() {
			send_request_to_server(
					'GET_JUR_AND_NAT_PRS', 
					'<%=findType%>', 
					'<%=typeId%>');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_JUR_AND_NAT_PRS", false))%>');
	</script>
</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">
	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_JUR_AND_NAT_PRS", false)) %>
	
	<%=Bean.getServiceFindTable(name_entry) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.commonXML.getfieldTransl("entry_type", false)%></th> 
		<th><%= Bean.commonXML.getfieldTransl("entry_name", false)%></th>
		<th><%= Bean.commonXML.getfieldTransl("entry_address", false)%></th>
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