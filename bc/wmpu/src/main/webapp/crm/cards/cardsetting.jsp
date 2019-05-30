<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>
<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CARDS_CARDSETTING";
String tagFind = "_FIND";
String tagStatus = "_STATUS";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specid"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String id_status	= Bean.getDecodeParam(parameters.get("id_status"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
id_status 		= Bean.checkFindString(pageFormName + tagStatus, id_status, l_page);

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

			<%= Bean.getMenuButton("PRINT", "../crm/cards/cardsetting.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/cards/cardsetting.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/cards/cardsetting.jsp?page=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("id_status", "../crm/cards/cardsetting.jsp?page=1", Bean.cardsettingXML.getfieldTransl("card_status", false)) %>
				<%= Bean.getClubCardStatusOptions(id_status, true) %>
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
	<%= Bean.header.getCardStatusHeadHTML(find_string, id_status, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>

</html>