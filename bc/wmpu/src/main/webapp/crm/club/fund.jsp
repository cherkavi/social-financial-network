<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%
request.setCharacterEncoding("UTF-8");
	
String pageFormName = "CLUB_FUND";
String tagFind = "_FIND";
Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);

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
		    	<%= Bean.getMenuButton("ADD", "../crm/club/fundupdate.jsp?type=general&action=add&process=no", "", "") %>
				<%= Bean.getMenuButton("RUN", "../crm/club/fundupdate.jsp?type=rests&action=run&process=yes", Bean.clubfundXML.getfieldTransl("h_calc_rests", false), "", Bean.clubfundXML.getfieldTransl("h_calc_rests", false)) %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/club/fund.jsp?print=Y", "", "") %>

			<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/club/fund.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/club/fund.jsp?page=1&") %>
	
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
<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getClubsFundHeadHTML(find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>