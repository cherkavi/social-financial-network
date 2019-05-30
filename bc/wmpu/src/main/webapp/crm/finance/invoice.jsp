<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "CRM_FINANCE_INVOICE";
String tagFind = "_FIND";
String tagState = "_STATE";
String tagPriority = "_PRIORITY";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String state	 	= Bean.getDecodeParam(parameters.get("state"));
String priority 	= Bean.getDecodeParam(parameters.get("priority"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));

find_string 		= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
state 				= Bean.checkFindString(pageFormName + tagState, state, l_page);
priority 			= Bean.checkFindString(pageFormName + tagPriority, priority, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/finance/invoiceupdate.jsp?type=general&action=add&process=no", "", "", Bean.clearingXML.getfieldTransl("h_add_invoice", false)) %>
			<%= Bean.getMenuButton("RUN", "../crm/finance/invoiceupdate.jsp?type=general&action=create&process=no", "", "", Bean.clearingXML.getfieldTransl("h_create_invoice", false)) %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/finance/invoice.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/finance/invoice.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/finance/invoice.jsp?page=1&") %>
	
	        <%=Bean.getSelectOnChangeBeginHTML("state", "../crm/finance/invoice.jsp?page=1", Bean.clearingXML.getfieldTransl("name_fn_invoice_state", false)) %>
				<%=Bean.getFinanceInvoiceStateOptions(state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("priority", "../crm/finance/invoice.jsp?page=1&", Bean.clearingXML.getfieldTransl("name_fn_priority", false)) %>
				<%= Bean.getFinancePriorityOptions(priority, true) %>
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
	<%= Bean.header.getInvoicesHTML(find_string, state, priority, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>