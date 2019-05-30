<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CLUB_COMISTYPE";
String tagFind = "_FIND";
String tagRelType = "_REL_TYPE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String rel_type 	= Bean.getDecodeParam(parameters.get("rel_type"));

find_string		= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
rel_type 		= Bean.checkFindString(pageFormName + tagRelType, rel_type, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/club/comistypeupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/club/comistype.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/club/comistype.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/club/comistype.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("rel_type", "../crm/club/comistype.jsp?page=1", Bean.comissionXML.getfieldTransl("name_club_rel_type", false)) %>
				<%= Bean.getClubRelTypeOptions(rel_type, true) %>
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
	<%= Bean.header.getClubComissionTypeHTML(rel_type, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>