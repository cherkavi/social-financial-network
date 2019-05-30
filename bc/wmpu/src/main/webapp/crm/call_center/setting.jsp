<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CALL_CENTER_SETTINGS";
String tagState = "_STATE";
String tagPage = "_PAGE";
String tagFind = "_FIND";

Bean.setJspPageForTabName(pageFormName);
	
request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specid"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String state		= Bean.getDecodeParam(parameters.get("state"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
state			= Bean.checkFindString(pageFormName + tagState, state, l_page);

//Обрабатываем номера страниц
Bean.pageCheck(pageFormName + tagPage, l_page);
String l_page_beg = Bean.getFirstRowNumber(pageFormName + tagPage);
String l_page_end = Bean.getLastRowNumber(pageFormName + tagPage);

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
		    <%= Bean.getMenuButton("ADD", "../crm/call_center/settingupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/call_center/setting.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagPage, "../crm/call_center/setting.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/call_center/setting.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("state", "../crm/call_center/setting.jsp?page=1", Bean.call_centerXML.getfieldTransl("name_cc_setting_state", false)) %>
				<%=Bean.getCallCenterSettingStateOptions(state, true) %>
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
<% Bean.header.setDeleteHyperLink("","",""); %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getCallCenterSettingsHeadHTML(state, find_string, l_page_beg, l_page_end, print) %>
<%
} %>
</div></div>
</body>
</html>