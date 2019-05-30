<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>

<%

String pageFormName = "LOGISTIC_CLIENTS_PAYMENTS";
String tagFind = "_FIND";
String tagPayKind = "_PAY_KIND";
String tagPayState = "_PAY_STATE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);

String cd_pay_kind			= Bean.getDecodeParam(parameters.get("cd_pay_kind"));
cd_pay_kind 				= Bean.checkFindString(pageFormName + tagPayKind, cd_pay_kind, l_page);

String cd_pay_state			= Bean.getDecodeParam(parameters.get("cd_pay_state"));
cd_pay_state 				= Bean.checkFindString(pageFormName + tagPayState, cd_pay_state, l_page);

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
		    <%= Bean.getMenuButton("RUN", "../crm/logistic/clients/paymentupdate.jsp?type=general&action=run&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/logistic/clients/payment.jsp?print=Y", "", "") %>
			
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/logistic/clients/payment.jsp?", "page") %>
		</tr>
	</table>
	
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/logistic/clients/payment.jsp?page=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("cd_pay_state", "../crm/logistic/clients/payment.jsp?page=1", Bean.logisticXML.getfieldTransl("cd_lg_promoter_pay_state", false)) %>
				<%= Bean.getLogisticPromoterPayStateOptions(cd_pay_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("cd_pay_kind", "../crm/logistic/clients/payment.jsp?page=1", Bean.logisticXML.getfieldTransl("cd_lg_promoter_pay_kind", false)) %>
				<%= Bean.getLogisticPromoterPayKindOptions(cd_pay_kind, true) %>
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
	<%= Bean.header.getLogisticPromoterPaymentsHTML(find_string, cd_pay_kind, cd_pay_state, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>