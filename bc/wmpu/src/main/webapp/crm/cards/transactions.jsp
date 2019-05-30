<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%
request.setCharacterEncoding("UTF-8");

String pageFormName = "CARDS_TRANSACTIONS";
String tagTransKind = "_TRANS_KIND";
String tagTransType = "_TRANS_TYPE";
String tagTransState = "_TRANS_STATE";
String tagTransPayType = "_TRANS_PAY_TYPE";
String tagFind = "_FIND";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 		= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 			= Bean.getDecodeParam(parameters.get("page"));
String kind_trans 		= Bean.getDecodeParam(parameters.get("kind_trans"));
String type_trans 		= Bean.getDecodeParam(parameters.get("type_trans"));
String state_trans 		= Bean.getDecodeParam(parameters.get("state_trans"));
String pay_type 		= Bean.getDecodeParam(parameters.get("pay_type"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
kind_trans 	= Bean.checkFindString(pageFormName + tagTransKind, kind_trans, l_page);
if (kind_trans==null || "".equalsIgnoreCase(kind_trans)) {
	kind_trans = "SUCCESSFUL";
}

type_trans = Bean.checkFindString(pageFormName + tagTransType, type_trans, l_page);
state_trans = Bean.checkFindString(pageFormName + tagTransState, state_trans, l_page);
pay_type = Bean.checkFindString(pageFormName + tagTransPayType, pay_type, l_page);

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
			    <%=Bean.getMenuButton("POSTING", "../crm/cards/transactionsupdate.jsp?type=posting&action=run_all&process=yes&id_club="+Bean.getCurrentClubID(), Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_TRANS_ALL", false), "", Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_TRANS_ALL", false))%>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/cards/transactions.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/cards/transactions.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/cards/transactions.jsp?page=1&") %>
	
		 	<%=Bean.getSelectOnChangeBeginHTML("kind_trans", "../crm/cards/transactions.jsp?page=1", Bean.transactionXML.getfieldTransl("type_trans", false)) %>
				<%= Bean.getSelectOptionHTML(kind_trans, "SUCCESSFUL", "Успешные", "style=\"color:green\"") %>
				<%= Bean.getSelectOptionHTML(kind_trans, "UNCHECKED", "Непроверенные", "style=\"color:red\"") %>
		 	<%=Bean.getSelectOnChangeEndHTML() %>

		 	<%=Bean.getSelectOnChangeBeginHTML("type_trans", "../crm/cards/transactions.jsp?page=1", Bean.transactionXML.getfieldTransl("type_trans", false)) %>
		 		<%= Bean.getTransTypeOptions(type_trans, true) %>
		 	<%=Bean.getSelectOnChangeEndHTML() %>
	
		 	<%=Bean.getSelectOnChangeBeginHTML("state_trans", "../crm/cards/transactions.jsp?page=1", Bean.transactionXML.getfieldTransl("state_trans", false)) %>
		 		<%= Bean.getTransStateOptions(state_trans, true) %>
		 	<%=Bean.getSelectOnChangeEndHTML() %>
	
			<% if ("SUCCESSFUL".equalsIgnoreCase(kind_trans)) { %>		
			 	<%=Bean.getSelectOnChangeBeginHTML("pay_type", "../crm/cards/transactions.jsp?page=1", Bean.transactionXML.getfieldTransl("pay_type", false)) %>
			 		<%= Bean.getTransPayTypeOptions(pay_type, true) %>
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

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<% if ("SUCCESSFUL".equalsIgnoreCase(kind_trans)) { %>	
	<%= Bean.header.getTransactionsHeadHTML(type_trans, pay_type, state_trans, "", find_string, l_beg, l_end, print) %>
	<% } else { %>
	<%= Bean.header.getAllErrorTransHTML(type_trans, state_trans, find_string, l_beg, l_end, print) %>
	<% } %>
<%} %>
</div></div>
</body>
</html>