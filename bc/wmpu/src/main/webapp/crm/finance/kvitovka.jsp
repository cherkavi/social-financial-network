<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>
<%

String pageFormName = "FINANCE_KVITOVKA";
String tagFind = "_FIND";
String tagState = "_STATE";
String tagBSFind = "_BS_FIND";
String tagBSPage = "_BS_PAGE";
String tagBSState = "_BS_STATE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String l_bs_page 	= Bean.getDecodeParam(parameters.get("bs_page"));
String state_line 	= Bean.getDecodeParam(parameters.get("state_line"));
String find_bs	 	= Bean.getDecodeParam(parameters.get("find_bs"));
String state_bs	 	= Bean.getDecodeParam(parameters.get("state_bs"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
find_bs		 	= Bean.checkFindString(pageFormName + tagBSFind, find_bs, l_bs_page);
state_line 		= Bean.checkFindString(pageFormName + tagState, state_line, l_page);
if (state_line==null) {
	state_line = "UNRECONCILE";
}
state_bs 		= Bean.checkFindString(pageFormName + tagBSState, state_bs, l_bs_page);
if (state_bs==null || "".equalsIgnoreCase(state_bs)) {
	state_bs = "";
}

String id_bank_statement_line 	= Bean.getDecodeParam(parameters.get("id_bank_statement_line"));
if (id_bank_statement_line==null || "".equalsIgnoreCase(id_bank_statement_line)) {
	id_bank_statement_line = "";
}
Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);


Bean.pageCheck(pageFormName + tagBSPage, l_bs_page);
String l_bs_page_beg = Bean.getFirstRowNumber(pageFormName + tagBSPage);
String l_bs_page_end = Bean.getLastRowNumber(pageFormName + tagBSPage);

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

		    <%= Bean.getMenuButton("PRINT", "../crm/finance/kvitovka.jsp?print=Y&id_bank_statement_line="+id_bank_statement_line, "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/finance/kvitovka.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/finance/kvitovka.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("state_line", "../crm/finance/kvitovka.jsp?page=1", Bean.clearingXML.getfieldTransl("state_line", false)) %>
				<%= Bean.getMeaningFromLookupNameOptions("BS_LINE_STATE",state_line, true) %>
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
	<div class="div_caption"><%=Bean.bank_statementXML.getfieldTransl("general", false) %></div>
		<%= Bean.header.getKvitovkaHeadHTML(id_bank_statement_line, state_line, find_string, l_beg, l_end, print) %>

	<% if (!(id_bank_statement_line==null || "".equalsIgnoreCase(id_bank_statement_line))) {%>
		<br><div class="div_caption"><%= Bean.clearingXML.getfieldTransl("general", false) %></div>
		<script>
			function checkEditAmountPermission(id_elem) {
				
				var checkBoxId = document.getElementById('id_'+id_elem);
				var sumId = document.getElementById('sum_'+id_elem);
				sumId.readOnly = false;
				//alert(sumId.class);
				if (checkBoxId.checked) {
					sumId.className="inputfield";
					sumId.readOnly = false;
				} else {
					sumId.className="inputfield-ro";
					sumId.readOnly = true;
				}
				//sumId.value = "1";
				//alert(sumId.value);
				return true;
			}
		</script>

		<table <%= Bean.getTableMenuParam() %>>
			<tr>
				<%= Bean.getFindHTML("find_bs", find_bs, "../crm/finance/kvitovka.jsp?id_bank_statement_line="+id_bank_statement_line+"&bs_page=1&") %>
		
				<%=Bean.getSelectOnChangeBeginHTML("state_bs", "../crm/finance/kvitovka.jsp?id_bank_statement_line="+id_bank_statement_line+"&bs_page=1", Bean.clearingXML.getfieldTransl("state_line", false)) %>
					<%= Bean.getMeaningFromLookupNameOptions("BS_LINE_STATE",state_bs, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
		
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBSPage, "../crm/finance/kvitovka.jsp?id_bank_statement_line="+id_bank_statement_line+"&", "bs_page") %>
			</tr>
		</table>

		<%= Bean.header.getKvitovkaBankStatementHTML(id_bank_statement_line, state_bs, find_bs, l_bs_page_beg, l_bs_page_end, print) %>
	<% } %>
<%} %>
</div></div>
</body>
</html>