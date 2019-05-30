<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>
<%

String pageFormName = "CLIENTS_TERMINALS";
String tagFind = "_FIND_TELEGRAM";
String tagType = "_TYPE_TELEGRAM";
String tagStatus = "_STATUS_TELEGRAM";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String cd_type 		= Bean.getDecodeParam(parameters.get("cd_type"));
String cd_status	= Bean.getDecodeParam(parameters.get("cd_status"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
cd_type 		= Bean.checkFindString(pageFormName + tagType, cd_type, l_page);
cd_status 		= Bean.checkFindString3(pageFormName + tagStatus, cd_status, "ACTIVE", l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/clients/terminalupdate.jsp?type=term&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/clients/terminals.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/clients/terminals.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/clients/terminals.jsp?page=1&") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("cd_type", "../crm/clients/terminals.jsp?page=1", Bean.terminalXML.getfieldTransl("name_term_type", false)) %>
				<%= Bean.getTermTypeOptions(cd_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		
			<%=Bean.getSelectOnChangeBeginHTML("cd_status", "../crm/clients/terminals.jsp?page=1", Bean.terminalXML.getfieldTransl("name_term_status", false)) %>
				<%= Bean.getTermStatusOptions(cd_status, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/clients/terminalupdate.jsp?type=term&action=remove&process=yes&id=",Bean.buttonXML.getfieldTransl("delete", false),"ID_TERM"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getTerminalsHeadHTML(cd_type, cd_status, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>