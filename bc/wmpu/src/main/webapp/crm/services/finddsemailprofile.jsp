<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_EMAIL_PROFILE";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_profile = Bean.getDecodeParamPrepare(parameters.get("id_profile"));
String name_profile = Bean.isEmpty(id_profile)?"":Bean.getDSEmailProfileName(id_profile);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));

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
		function changeOpener(id, Name, cdCur, nameCur, idBank, nameBank){
			window.opener.document.getElementById('id_email_profile').value = id;
			window.opener.document.getElementById('name_email_profile').value = Name;
			window.opener.document.getElementById('name_email_profile').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_DS_EMAIL_PROFILE', 
					'<%=id_profile%>', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_EMAIL_PROFILE", false))%>');
		
	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_EMAIL_PROFILE", false)) %>
	
	<%=Bean.getServiceFindTable(name_profile) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.emailXML.getfieldTransl("ID_EMAIL_PROFILE", false)%></th>
		<th><%= Bean.emailXML.getfieldTransl("NAME_EMAIL_PROFILE", false)%></th>
		<th><%= Bean.emailXML.getfieldTransl("SMTP_SERVER", false)%></th>
		<th><%= Bean.emailXML.getfieldTransl("SMTP_PORT", false)%></th>
		<th><%= Bean.emailXML.getfieldTransl("SMTP_SSL", false)%></th>
		<th><%= Bean.emailXML.getfieldTransl("NEED_AUTORIZATION_TSL", false)%></th>
		<th><%= Bean.emailXML.getfieldTransl("SMTP_USER", false)%></th>
		<th><%= Bean.emailXML.getfieldTransl("DELAY_NEXT_LETTER", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>