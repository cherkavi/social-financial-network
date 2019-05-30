<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>
<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CLUB_ONLINE_PAY_TYPE";
String tagFind = "_FIND";
String tagExist = "_EXIST_FLAG";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specid"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String exist_flag	= Bean.getDecodeParam(parameters.get("exist_flag"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
exist_flag 		= Bean.checkFindString(pageFormName + tagExist, exist_flag, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/club/online_pay_typeupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %> 
		    <%= Bean.getMenuButton("PRINT", "../crm/club/online_pay_type.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/club/online_pay_type.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/club/online_pay_type.jsp?page=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("exist_flag", "../crm/club/online_pay_type.jsp?page=1", Bean.clubXML.getfieldTransl("exist_club_online_pay_type", false)) %>
				<%= Bean.getMeaningFromLookupNameOptions("YES_NO", exist_flag, true) %>
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
	<%= Bean.header.getCardOnlinePayTypeHeadHTML(find_string, exist_flag, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>

</html>