<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "DISPATCH_MESSAGES_PATTERN";
String tagFind = "_FIND";
String tagType = "_TYPE";
String tagStatus = "_STATUS";
String tagPath = "_PATH";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String cd_type	 	= Bean.getDecodeParam(parameters.get("cd_type"));
String cd_status 	= Bean.getDecodeParam(parameters.get("cd_status"));
String path		 	= Bean.getDecodeParam(parameters.get("path"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
cd_type 		= Bean.checkFindString(pageFormName + tagType, cd_type, l_page);
cd_status 		= Bean.checkFindString3(pageFormName + tagStatus, cd_status, "ACTIVE", l_page);
path 			= Bean.checkFindString(pageFormName + tagPath, path, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/dispatch/messages/patternupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/dispatch/messages/pattern.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/dispatch/messages/pattern.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/dispatch/messages/pattern.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("cd_type", "../crm/dispatch/messages/pattern.jsp?page=1&path="+path, Bean.messageXML.getfieldTransl("name_pattern_type", false)) %>
				<%= Bean.getDSPatternTypeOptions(cd_type, "50", true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("cd_status", "../crm/dispatch/messages/pattern.jsp?page=1&path="+path, Bean.messageXML.getfieldTransl("name_pattern_status", false)) %>
				<%= Bean.getDsPatternStatusOptions(cd_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("path", "../crm/dispatch/messages/pattern.jsp?page=1&cd_type="+cd_type, Bean.messageXML.getfieldTransl("can_send", false)) %>
				<%=Bean.getSelectOptionHTML(path, "", "") %>
				<%=Bean.getSelectOptionHTML(path, "SMS", Bean.messageXML.getfieldTransl("can_send_sms", false)) %>
				<%=Bean.getSelectOptionHTML(path, "EMAIL", Bean.messageXML.getfieldTransl("can_send_email", false)) %>
				<%=Bean.getSelectOptionHTML(path, "OFFICE", Bean.messageXML.getfieldTransl("can_send_office", false)) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/dispatch/messages/patternupdate.jsp?type=general&action=remove&process=yes&id=",Bean.buttonXML.getfieldTransl("delete", false),"ID_CL_PATTERN"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getClientsPatternsHeaderHTML(path, find_string, cd_type, cd_status, l_beg, l_end, print) %>
<%  
} %>
</div></div>
</body>
</html>