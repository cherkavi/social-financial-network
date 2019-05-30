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

String id_cc_faq = Bean.getDecodeParamPrepare(parameters.get("id_cc_faq"));
String title_cc_faq = Bean.isEmpty(id_cc_faq)?"":Bean.getFAQTitle(id_cc_faq);

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
		function changeOpener(id, title, question, answer){
			window.opener.document.getElementById('id_cc_faq').value = id;
			window.opener.document.getElementById('name_cc_faq').value = title;
			window.opener.document.getElementById('name_cc_faq').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_FAQ', 
					'', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_FAQ", false))%>');
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(title_cc_faq)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_FAQ", false)) %>
	
	<%=Bean.getServiceFindTable(title_cc_faq) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.call_centerXML.getfieldTransl("CD_CC_FAQ", false)%></th>
		<th><%= Bean.call_centerXML.getfieldTransl("NAME_CC_FAQ_CATEGORY", false)%></th>
		<th><%= Bean.call_centerXML.getfieldTransl("TITLE_CC_FAQ", false)%></th>
		<th><%= Bean.call_centerXML.getfieldTransl("QUESTION_CC_FAQ", false)%></th>
		<th><%= Bean.call_centerXML.getfieldTransl("ANSWER_CC_FAQ", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>