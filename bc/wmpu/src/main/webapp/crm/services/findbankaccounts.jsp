<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScriptShort(request) %>

<%@page import="java.util.HashMap"%><html>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");
String pageFormName = "FIND_BANK_ACCOUNTS";

//Обрабатываем номера страниц
String l_page = Bean.getDecodeParam(parameters.get("page"));
Bean.pageCheck(pageFormName, l_page);
String l_curr_page = Bean.getCurrentPage(pageFormName);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String id_bank_account = Bean.getDecodeParam(parameters.get("id_bank_account"));
String name_bank_account = Bean.isEmpty(id_bank_account)?"":Bean.getBankAccountNumber(id_bank_account);

String suf = Bean.getDecodeParamPrepare(parameters.get("suf"));
String short_result = Bean.getDecodeParam(parameters.get("short_result"));
short_result = Bean.isEmpty(short_result)?"N":short_result;

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
		function changeOpener(id, Name, cdCur, nameCur, idBank, nameBank, idJurPrs, nameJurPrs){
			window.opener.document.getElementById('id_bank_account<%=suf%>').value = id;
			window.opener.document.getElementById('name_bank_account<%=suf%>').value = Name;
			<% if ("N".equalsIgnoreCase(short_result)) {%>
				window.opener.document.getElementById('cd_currency').value = cdCur;
				window.opener.document.getElementById('name_currency').value = nameCur;
				window.opener.document.getElementById('id_bank').value = idBank;
				window.opener.document.getElementById('name_bank').value = nameBank;
				window.opener.document.getElementById('name_bank_account<%=suf%>').className = "inputfield_modified";
				window.opener.document.getElementById('id_jur_prs<%=suf%>').value = idJurPrs;
				window.opener.document.getElementById('name_jur_prs<%=suf%>').value = nameJurPrs;
			<% } %>
			window.close();
		}
		function send_request() {
			send_request_to_server(
					'GET_BANK_ACCOUNTS', 
					'<%=id_jur_prs%>', 
					'');
		}
		setParam('<%=Bean.getLanguage()%>', 
				'<%= Bean.getSessionId()%>', 
				'<%= Bean.getRowsPerPage()%>');
		setTitle('<%= Bean.getPageTitle(Bean.commonXML.getfieldTransl("FIND_BANK_ACCOUNTS", false))%>');

	</script>

</head>
<body onload="focusInput(); checkExpr(); send_request(); return false;">

	<%=Bean.getServiceHeaderTable(Bean.commonXML.getfieldTransl("FIND_BANK_ACCOUNTS", false)) %>
	
	<%=Bean.getServiceFindTable(name_bank_account) %>

<table width="100%" class="tablesorter" id="id_table">
	<thead>
	<tr>
		<th><%= Bean.accountXML.getfieldTransl("ID_BANK_ACCOUNT", false)%></th>
		<th><%= Bean.accountXML.getfieldTransl("NAME_BANK_ACCOUNT_TYPE", false)%></th>
		<th><%= Bean.accountXML.getfieldTransl("NUMBER_BANK_ACCOUNT", false)%></th>
		<th><%= Bean.accountXML.getfieldTransl("NAME_BANK_ALT", false)%></th>
		<th><%= Bean.accountXML.getfieldTransl("NAME_OWNER_BANK_ACCOUNT", false)%></th>
		<th><%= Bean.accountXML.getfieldTransl("NAME_CURRENCY", false)%></th>
	</tr>
	</thead>
	<tbody id="rows">
		
	</tbody>
</table>
</body>
</html>