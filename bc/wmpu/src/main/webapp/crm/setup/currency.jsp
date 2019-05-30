<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>

<% 
String pageFormName = "SETUP_CURRENCY";
String tagFind = "_FIND";
String tagIsUsed = "_IS_USED";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String is_used	 	= Bean.getDecodeParam(parameters.get("is_used"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));

find_string 		= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
is_used 			= Bean.checkFindString(pageFormName + tagIsUsed, is_used, l_page);

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
<% StringBuilder html = new StringBuilder();
   html.append(Bean.header.getCurrenciesHTML(find_string, is_used, true, print));
%>

<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

		   <%= Bean.getMenuButton("PRINT", "../crm/setup/currency.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/setup/currency.jsp?", "page", Bean.header.getLastResultSetRowCount()) %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/setup/currency.jsp?page=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("is_used", "../crm/setup/currency.jsp?page=1&", Bean.currencyXML.getfieldTransl("is_used", false)) %>
				<%= Bean.getMeaningFromLookupNumberOptions("YES_NO", is_used, true) %>
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
	<%= html.toString() %>
<%} %>
</div></div>
</body>
</html>