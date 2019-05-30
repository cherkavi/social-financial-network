<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_BK_ACCOUNTS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_bk_account = Bean.getDecodeParamPrepare(parameters.get("id_bk_account"));
String name_bk_account = Bean.isEmpty(id_bk_account)?"":Bean.getBKAccountCd(id_bk_account);

String field = Bean.getDecodeParamPrepare(parameters.get("field"));

String is_group = Bean.getDecodeParam(parameters.get("is_group"));
is_group = (Bean.isEmpty(is_group) || !"Y".equalsIgnoreCase(is_group) || !"N".equalsIgnoreCase(is_group))?"N":is_group;

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
			window.opener.document.getElementById('<%=field%>_id_bk_account').value = id;
			window.opener.document.getElementById('<%=field%>_name_bk_account').value = Name;
			window.opener.document.getElementById('<%=field%>_name_bk_account').className = "inputfield_modified";
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_BK_ACCOUNTS', 
					'<%=is_group%>', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_BK_ACCOUNTS", false))%>');
		 	
	</script>

</head>
<body onload="focusInput(); checkExpr(); <% if (!"".equalsIgnoreCase(name_bk_account)) {%>send_request(); return false;<%} %>">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_BK_ACCOUNTS", false)) %>
	
	<%=Bean.getServiceFindTable(name_bk_account) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.bk_accountXML.getfieldTransl("ID_BK_ACCOUNT", false)%></th>
		<th><%= Bean.bk_accountXML.getfieldTransl("CD_BK_ACCOUNT", false)%></th>
		<th><%= Bean.bk_accountXML.getfieldTransl("NAME_BK_ACCOUNT", false)%></th>
		<th><%= Bean.bk_accountXML.getfieldTransl("CD_BK_ACCOUNT_PARENT", false)%></th>
		<th><%= Bean.bk_accountXML.getfieldTransl("IS_GROUP_TSL", false)%></th>
		<th><%= Bean.bk_accountXML.getfieldTransl("EXIST_FLAG", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>