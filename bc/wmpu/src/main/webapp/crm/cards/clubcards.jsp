<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CARDS_CLUBCARDS";
String tagFind = "_FIND";
String tagProfile = "_PROFILE";
String tagIssuer = "_ISSUER";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specid"));
String iss 			= Bean.getDecodeParamPrepare(parameters.get("iss"));
String paysys 		= Bean.getDecodeParamPrepare(parameters.get("paysys"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String id_profile 	= Bean.getDecodeParam(parameters.get("id_profile"));
String id_issuer 	= Bean.getDecodeParam(parameters.get("id_issuer"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
id_profile 		= Bean.checkFindString(pageFormName + tagProfile, id_profile, l_page);
id_issuer 		= Bean.checkFindString(pageFormName + tagIssuer, id_issuer, l_page);

// Обрабатываем номера страниц
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
			    <%= Bean.getMenuButton("RUN", "../crm/cards/clubcardupdate.jsp?type=actions&action=choisegeneral&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/cards/clubcards.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/cards/clubcards.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/cards/clubcards.jsp?page=1&") %>
			
			<%=Bean.getSelectOnChangeBeginHTML("id_profile", "../crm/cards/clubcards.jsp?page=1", Bean.getClubCardXMLFieldTransl("id_profile", false)) %>
				<%= Bean.getMenuProfileList("105",id_profile) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("id_issuer", "../crm/cards/clubcards.jsp?page=1", Bean.getClubCardXMLFieldTransl("id_issuer", false)) %>
				<%= Bean.getIssuerNamesOptions(id_issuer, true) %>
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
<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
	<%= Bean.header.getClubCardHeadHTML(find_string, id_profile, id_issuer, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>