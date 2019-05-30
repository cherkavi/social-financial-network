<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_DOC";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_doc = Bean.getDecodeParamPrepare(parameters.get("id_doc"));
String name_doc = Bean.isEmpty(id_doc)?"":Bean.getDocName(id_doc);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));

String id_jur_prs = Bean.getDecodeParamPrepare(parameters.get("id_jur_prs"));
String name_jur_prs = Bean.isEmpty(id_jur_prs)?"":Bean.getJurPersonShortName(id_jur_prs);

String title_caption = Bean.commonXML.getfieldTransl("FIND_DOC", false);
if (!Bean.isEmpty(name_jur_prs)) {
	title_caption = title_caption + " (" + name_jur_prs + ")";
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
					'GET_DOC', 
					'<%=id_jur_prs%>', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(title_caption)%>');
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(id_doc)) {%>send_request(); <% } %>return false;">

	<%=Bean.getServiceHeaderTable(title_caption) %>
	
	<%=Bean.getServiceFindTable(name_doc) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.documentXML.getfieldTransl("FULL_DOC", false)%></th>
		<th><%= Bean.documentXML.getfieldTransl("PARTIES_DOC", false)%></th>
		<th><%= Bean.documentXML.getfieldTransl("NAME_DOC_TYPE", false)%></th>
		<th><%= Bean.documentXML.getfieldTransl("CLUB_REL_TYPE", false)%></th>
		<th><%= Bean.documentXML.getfieldTransl("NAME_DOC_STATE", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>