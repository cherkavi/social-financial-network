<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CLIENTS_YURPERSONS";
String tagFind = "_FIND";
String tagStatus = "_STATUS";
String tagClubStatus = "_CLUB_STATUS";
String tagClubType = "_CLUB_TYPE";

Bean.setJspPageForTabName(pageFormName);
	
request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 		= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 			= Bean.getDecodeParam(parameters.get("page"));
String jur_prs_status 	= Bean.getDecodeParam(parameters.get("jur_prs_status"));
String club_status 		= Bean.getDecodeParam(parameters.get("club_status"));
String club_type 		= Bean.getDecodeParam(parameters.get("club_type"));

find_string 			= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
jur_prs_status 			= Bean.checkFindString(pageFormName + tagStatus, jur_prs_status, l_page);

club_status 	= Bean.checkFindString(pageFormName + tagClubStatus, club_status, l_page);
club_type 		= Bean.checkFindString(pageFormName + tagClubType, club_type, l_page);
jur_prs_status	= Bean.isEmpty(jur_prs_status)?"PARTNER":jur_prs_status;

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
		    <%= Bean.getMenuButton("ADD", "../crm/clients/yurpersonupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
			<%= Bean.getReportHyperLink("SR06", "") %>
		    <%= Bean.getMenuButton("PRINT", "../crm/clients/yurpersons.jsp?print=Y", "", "") %>
	
			<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/clients/yurpersons.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/clients/yurpersons.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("jur_prs_status", "../crm/clients/yurpersons.jsp?page=1", Bean.jurpersonXML.getfieldTransl("partner_or_service_place", false)) %>
			 	<%= Bean.getSelectOptionHTML(jur_prs_status, "ALL", "") %>
			 	<%= Bean.getSelectOptionHTML(jur_prs_status, "PARTNER", Bean.jurpersonXML.getfieldTransl("title_partner", false), "style=\"color:blue\"") %>
			 	<%= Bean.getSelectOptionHTML(jur_prs_status, "SERVICE_PLACE", Bean.jurpersonXML.getfieldTransl("title_service_place", false), "style=\"color:green\"") %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("club_status", "../crm/clients/yurpersons.jsp?page=1", Bean.clubXML.getfieldTransl("club_member_status", false)) %>
			 	<%= Bean.getClubMemberStatusOptions(club_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("club_type", "../crm/clients/yurpersons.jsp?page=1", Bean.clubXML.getfieldTransl("club_member_type", false)) %>
			 	<%= Bean.getClubMemberTypeOptions(club_type, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/clients/yurpersonupdate.jsp?type=general&action=remove&process=yes&id=",Bean.jurpersonXML.getfieldTransl("LAB_DELETE_FLD", false),"ID_JUR_PRS", "NAME_JUR_PRS_INITIAL"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getJurpersonsHeadHTML(jur_prs_status, club_status, club_type, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>