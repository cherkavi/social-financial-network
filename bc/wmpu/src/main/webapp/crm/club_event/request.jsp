<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CLUB_EVENT_REQUEST";
String tagFind = "_FIND";
String tagType = "_TYPE";
String tagState = "_STATE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String type 		= Bean.getDecodeParam(parameters.get("type"));
String state 		= Bean.getDecodeParam(parameters.get("state"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
type 			= Bean.checkFindString(pageFormName + tagType, type, l_page);
state 			= Bean.checkFindString(pageFormName + tagState, state, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/club_event/requestupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/club_event/request.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/club_event/request.jsp?", "page") %>
		</tr>
	</table>
	
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/club_event/request.jsp?page=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("type", "../crm/club_event/request.jsp?page=1", Bean.club_actionXML.getfieldTransl("name_nat_prs_gift_request_type", false)) %>
				<%= Bean.getNatPrsGiftRequestTypeOptions(type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("state", "../crm/club_event/request.jsp?page=1", Bean.club_actionXML.getfieldTransl("nm_nat_prs_gift_request_state", false)) %>
				<%= Bean.getNatPrsGiftRequestStateOptions(state, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/club_event/requestupdate.jsp?type=general&action=remove&process=yes&id=",Bean.club_actionXML.getfieldTransl("h_delete_nat_prs_gift_request", false),"ID_NAT_PRS_GIFT_REQUEST"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>
<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getNatPrsGiftRequestHeadHTML(type, state, find_string, l_beg,l_end, print) %>
<%} %>
</div></div>
</body>
</html>