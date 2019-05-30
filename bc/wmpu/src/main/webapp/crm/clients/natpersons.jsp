<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%
String pageFormName = "CLIENTS_NATPERSONS";
String tagFind = "_FIND";
String tagType = "_TYPE";
String tagClubStatus = "_CLUB_STATUS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String club_status 		= Bean.getDecodeParam(parameters.get("club_status"));

club_status 	= Bean.checkFindString(pageFormName + tagClubStatus, club_status, l_page);

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
String shareholder_type 	= Bean.getDecodeParam(parameters.get("shareholder_type"));

shareholder_type 	= Bean.checkFindString(pageFormName + tagType, shareholder_type, l_page);
if ("".equalsIgnoreCase(shareholder_type)) {
	shareholder_type = "ALL";
}

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
		    <%= Bean.getMenuButton("ADD", "../crm/clients/natpersonupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/clients/natpersons.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/clients/natpersons.jsp?find=Y&", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/clients/natpersons.jsp?page=1&") %>
			<% if (1==0) { /*Заглушка, чтобы не выводить данные*/ %>
			<%=Bean.getSelectOnChangeBeginHTML("shareholder_type", "../crm/clients/natpersons.jsp?page=1", Bean.natprsXML.getfieldTransl("general", false)) %>
				<%=Bean.getSelectOptionHTML(shareholder_type, "ALL", Bean.commonXML.getfieldTransl("h_all", false)) %>
				<%=Bean.getSelectOptionHTML(shareholder_type, "NAT_PRS", Bean.commonXML.getfieldTransl("h_nat_prs", false)) %>
				<%=Bean.getSelectOptionHTML(shareholder_type, "JUR_PRS", Bean.commonXML.getfieldTransl("h_jur_prs", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("club_status", "../crm/clients/natpersons.jsp?page=1", Bean.clubXML.getfieldTransl("club_member_status", false)) %>
			 	<%= Bean.getClubMemberStatusOptions(club_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	 		<% } %>
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
	<% Bean.header.setDeleteHyperLink("../crm/clients/natpersonupdate.jsp?type=general&action=remove&process=yes&id=",Bean.natprsXML.getfieldTransl("LAB_DELETE_FLD", false),"ID_NAT_PRS", "FULL_NAME"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getShareholdersHeadHTML(/*shareholder_type,*/ find_string, /*club_status,*/ l_beg, l_end, print) %>
<%} %>

</div></div>
</body>
</html>