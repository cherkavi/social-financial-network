<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>
<%

String pageFormName = "DISPATCH_SETTINGS_SMS_PROFILE";
String tagFind = "_FIND";
String tagProfileState = "_PROFILE_STATE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String cd_state 	= Bean.getDecodeParam(parameters.get("cd_state"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
cd_state 		= Bean.checkFindString(pageFormName + tagProfileState, cd_state, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/dispatch/settings/sms_profileupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/dispatch/settings/sms_profiles.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/dispatch/settings/sms_profiles.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/dispatch/settings/sms_profiles.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("cd_state", "../crm/dispatch/settings/sms_profiles.jsp?page=1", Bean.smsXML.getfieldTransl("name_profile_state", false)) %>
			 	<%= Bean.getDSPforileStateListOptions(cd_state, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/dispatch/settings/sms_profileupdate.jsp?type=general&action=remove&process=yes&id=",Bean.buttonXML.getfieldTransl("delete", false),"ID_SMS_PROFILE"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getSMSProfileHeadHTML(cd_state, find_string, l_beg, l_end, print) %> 
<%} %>
</div></div>
</body></html>