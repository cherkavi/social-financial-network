<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.io.files"%>
<%@page import="bc.AppConst"%>
<%@page import="java.util.HashMap"%><html>

<%

String pageFormName = "FORM_WARNINGS";
String tagStatus = "_STATUS";
String tagType = "_TYPE";
String tagFind = "_FIND";
String tagProfile = "_PROFILE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specid"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String id_type 		= Bean.getDecodeParam(parameters.get("id_type"));
String id_status 	= Bean.getDecodeParam(parameters.get("id_status"));
String id_profile 	= Bean.getDecodeParam(parameters.get("id_profile"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
id_type 		= Bean.checkFindString(pageFormName + tagType, id_type, l_page);

id_status	 	= Bean.checkFindString3(pageFormName + tagStatus, id_status, "NC", l_page);
id_profile 	= Bean.checkFindString(pageFormName + tagProfile, id_profile, l_page);
if ("".equalsIgnoreCase(id_profile)) {
	id_profile = "ALL";
}
//Обрабатываем номера страниц
Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>

<head>
	<%=Bean.getJSPHeadHTML(Bean.warningXML.getfieldTransl("general", false), print) %>
</head>
<body>

<% StringBuilder html = new StringBuilder();
   html.append(Bean.header.getWarningsHeadHTML(id_profile, id_type, id_status, find_string, l_beg, l_end, print));
%>
<div id="div_tabsheet">
<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader(Bean.warningXML.getfieldTransl("general", false), "../admin/warnings.jsp") %>

			<%= Bean.getMenuButton("ADD", "../admin/warningsupdate.jsp?type=general&action=add&process=no", "", "") %>
		    <%= Bean.getMenuButton("PRINT", "../admin/warnings.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../admin/warnings.jsp?id_type=" + id_type + "&", "page", Bean.header.getLastResultSetRowCount()) %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
	
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../admin/warnings.jsp?page=1&") %>
			<% String userId = Bean.loginUser.getValue("ID_USER");
			%>
			<%=Bean.getSelectOnChangeBeginHTML("id_profile", "../admin/warnings.jsp?page=1", Bean.jurpersonXML.getfieldTransl("id_profile", false)) %>
				<%=Bean.getSelectOptionHTML(id_profile, "ALL", Bean.warningXML.getfieldTransl("h_all", false)) %>
				<%=Bean.getSelectOptionHTML(id_profile, userId, Bean.warningXML.getfieldTransl("h_current_user", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("id_type", "../admin/warnings.jsp?page=1", Bean.warningXML.getfieldTransl("name_menu_element", false)) %>
			  	<%= Bean.getMeaningFromLookupNameOptions("SYS_WARNING_TYPE", id_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("id_status", "../admin/warnings.jsp?page=1", Bean.warningXML.getfieldTransl("status_warning", false)) %>
				<%=Bean.getSelectOptionHTML(id_status, "", "") %>
				<%=Bean.getSelectOptionHTML(id_status, "NC", Bean.warningXML.getfieldTransl("h_not_closed", false)) %>
			  	<%= Bean.getWarningStatusOptions(id_status, false) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
<% } else { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeaderPrint(Bean.warningXML.getfieldTransl("general", false)) %>
		</tr>
	</table>
<% } %>
</div>
<div id="div_data">
<div id="div_data_detail">

	<%= html.toString() %>
</div></div>
</body>
</html>