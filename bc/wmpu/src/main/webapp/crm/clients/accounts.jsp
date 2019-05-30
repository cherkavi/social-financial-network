<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%><html>
<%= Bean.getLogOutScript(request) %>

<%

String pageFormName = "CLIENTS_BANK_ACCOUNTS";
String tagFind = "_FIND";
String tagType = "_TYPE";
String tagStatus = "_STATUS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specid"));
String find_string 		= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 			= Bean.getDecodeParam(parameters.get("page"));
String id_type_filtr 	= Bean.getDecodeParam(parameters.get("id_type_filtr"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
id_type_filtr 	= Bean.checkFindString(pageFormName + tagType, id_type_filtr, l_page);

//Обрабатываем номера страниц
Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>
<head>
	<%=Bean.getJSPHeadHTML(print) %>
</head>
<body>
<div id="div_tabsheet">
<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
		    <%= Bean.getMenuButton("ADD", "../crm/clients/accountsupdate.jsp?type=general&action=add&process=no", "", "") %>
			<%= Bean.getMenuButton("RUN", "../crm/clients/accountsupdate.jsp?type=general&action=calc_rests_all&process=no", "", "", Bean.accountXML.getfieldTransl("h_calc_rests", false)) %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/clients/accounts.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/clients/accounts.jsp?id_type_filtr=" + id_type_filtr + "&", "page") %>
		</tr>
	</table>
	
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/clients/accounts.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("id_type_filtr", "../crm/clients/accounts.jsp?page=1", Bean.accountXML.getfieldTransl("name_bank_account_type", false)) %>
				<%= Bean.getBankAccountTypeOptions(id_type_filtr, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
<% } else { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeaderPrint() %>
		</tr>
	</table>
<% } %>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
	<% Bean.header.setDeleteHyperLink("../crm/clients/accountsupdate.jsp?type=general&action=remove&process=yes&id=",Bean.accountXML.getfieldTransl("h_delete_bank_account", false),"ID_BANK_ACCOUNT", "NUMBER_BANK_ACCOUNT"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getAccountsHTML(id_type_filtr, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>

</html>