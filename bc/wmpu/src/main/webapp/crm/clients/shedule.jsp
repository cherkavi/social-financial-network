<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "CLIENTS_SHEDULE";
String tagFind = "_FIND";
String tagTermType = "_TERMTYPE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String term_type	= Bean.getDecodeParam(parameters.get("term_type"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
term_type 		= Bean.checkFindString(pageFormName + tagTermType, term_type, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/clients/sheduleupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/clients/shedule.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/clients/shedule.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/clients/shedule.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("term_type", "../crm/clients/shedule.jsp?page=1", Bean.loyXML.getfieldTransl("term_type", false)) %>
				<%= Bean.getTermTypeOptions(term_type, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/clients/sheduleupdate.jsp?type=general&action=remove&process=yes&id=",Bean.sheduleXML.getfieldTransl("h_delete_schedule", false),"ID_SHEDULE", "NAME_SHEDULE"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getShedulesHTML(term_type, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>