<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "FINANCE_BK_SCHEME";
String tagFind = "_FIND";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String reportId 	= Bean.getDecodeParam(parameters.get("id_report"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);

if (reportId==null || "".equalsIgnoreCase(reportId)) {
	reportId = "";
}

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
		    <%= Bean.getMenuButton("ADD", "../crm/finance/bk_schemeupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/finance/bk_scheme.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/finance/bk_scheme.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/finance/bk_scheme.jsp?page=1&") %>
	
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
	<% Bean.header.setDeleteHyperLink("../crm/finance/bk_schemeupdate.jsp?type=general&action=remove&process=yes&id=",Bean.buttonXML.getfieldTransl("delete", false),"ID_BK_ACCOUNT_SCHEME"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>


<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getPostingSettingsHTML(find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>