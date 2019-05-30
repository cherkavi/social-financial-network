<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "SECURITY_USERS";
String tagUserStatus = "_USER_STATUS";
String tagFind = "_FIND";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String cd_status 	= Bean.getDecodeParam(parameters.get("cd_status"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
cd_status 		= Bean.checkFindString3(pageFormName + tagUserStatus, cd_status, "OPENED", l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/security/usersupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/security/users.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/security/users.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/security/users.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("cd_status", "../crm/security/users.jsp?page=1", Bean.userXML.getfieldTransl("name_user_status", false)) %>
				<%= Bean.getUserStatusOptions(cd_status, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/security/usersupdate.jsp?type=general&action=remove&process=yes&id=",Bean.userXML.getfieldTransl("LAB_DELETE_USER", false),"ID_USER"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getUsersHTML(cd_status, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>
