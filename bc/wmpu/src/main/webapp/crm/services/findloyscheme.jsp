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

String cd_scheme = Bean.getDecodeParamPrepare(parameters.get("cd_scheme"));
cd_scheme = Bean.isEmpty(cd_scheme)?"":cd_scheme;
String name_scheme = Bean.isEmpty(cd_scheme)?"":Bean.getLoySchemeCdAndName(cd_scheme);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));
String id_jur_prs = Bean.getDecodeParam(parameters.get("id_jur_prs"));
String name_jur_prs = Bean.isEmpty(id_jur_prs)?"":Bean.getJurPersonShortName(id_jur_prs);

String idField = "id_"+field;
String nameField = "name_"+field;

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
			window.opener.document.getElementById('<%=idField%>').value = id;
			window.opener.document.getElementById('<%=nameField%>').value = Name;
			window.opener.document.getElementById('<%=nameField%>').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_LOY_SCHEME', 
					'<%=id_jur_prs%>', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_LOY_SCHEME", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_LOY_SCHEME", false) + (!Bean.isEmpty(name_jur_prs)?" ("+name_jur_prs+")":"")) %>
	
	<%=Bean.getServiceFindTable(name_scheme) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.loyXML.getfieldTransl("NAME_PARTNER", false)%></th>
		<th><%= Bean.loyXML.getfieldTransl("NAME_LOYALITY_SCHEME", false)%></th>
		<th><%= Bean.loyXML.getfieldTransl("NAME_KIND_LOYALITY", false)%></th>
		<th><%= Bean.loyXML.getfieldTransl("DATE_BEG_FRMT", false)%></th>
		<th><%= Bean.loyXML.getfieldTransl("DATE_END_FRMT", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>