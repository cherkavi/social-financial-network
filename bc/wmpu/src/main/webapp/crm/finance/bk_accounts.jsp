<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "FINANCE_BK_ACCOUNTS";
String tabScheme = "_SCHEME";
String tagFind = "_FIND";
String tagExist = "_EXIST";
String tagGroup = "_GROUP";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String is_group 	= Bean.getDecodeParam(parameters.get("is_group"));
String exist_flag 	= Bean.getDecodeParam(parameters.get("exist_flag"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String id_scheme	= Bean.getDecodeParam(parameters.get("id_scheme"));

find_string 		= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
id_scheme 			= Bean.checkFindString(pageFormName + tabScheme, id_scheme, l_page);
is_group 			= Bean.checkFindString(pageFormName + tagGroup, is_group, l_page);
exist_flag 			= Bean.checkFindString(pageFormName + tagExist, exist_flag, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/finance/bk_accountsupdate.jsp?type=general&action=add&process=no", "", "") %>
			<%= Bean.getMenuButton("DELETE_ALL", "../crm/finance/bk_accountsupdate.jsp?type=general&action=removeall&process=yes", Bean.bk_accountXML.getfieldTransl("h_delete_all", false), "", Bean.bk_accountXML.getfieldTransl("h_delete_all", false)) %>
			<%= Bean.getMenuButton("RUN", "../crm/finance/bk_accountsupdate.jsp?type=general&action=calc_rests&process=no", "", "", Bean.bk_accountXML.getfieldTransl("h_calc_rests", false)) %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/finance/bk_accounts.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/finance/bk_accounts.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/finance/bk_accounts.jsp?page=1&") %>
	
	        <%=Bean.getSelectOnChangeBeginHTML("id_scheme", "../crm/finance/bk_accounts.jsp?page=1") %>
				<%=Bean.getBKAccountSchemeOptions(id_scheme, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("is_group", "../crm/finance/bk_accounts.jsp?page=1&", Bean.bk_schemeXML.getfieldTransl("is_group", false)) %>
				<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("YES_NO", is_group, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("exist_flag", "../crm/finance/bk_accounts.jsp?page=1&", Bean.bk_schemeXML.getfieldTransl("exist_flag", false)) %>
				<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("YES_NO", exist_flag, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/finance/bk_accountsupdate.jsp?type=general&action=remove&process=yes&id=",Bean.buttonXML.getfieldTransl("delete", false),"ID_BK_ACCOUNT"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getBKAccountsHTML(id_scheme, is_group, exist_flag, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>