<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcBankStatementHeaderObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcBankStatementFileObject"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="bc.objects.bcBankStatementLineObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_BANKSTATEMENT";
String tagPosting = "_POSTING_LINE";
String tagPostingFind = "_POSTING_LINE_FIND";
String tagReconcile = "_RECONCILE_LINE";
String tagReconcileFind = "_RECONCILE_LINE_FIND";
String tagFileLines = "_ROW_FILE_LINES";
String tagFileLineFind = "_LINE_FILE_LINE_FIND";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}
if (id==null || "".equalsIgnoreCase(id)) { 
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcBankStatementLineObject line = new bcBankStatementLineObject(id);
	
	Bean.currentMenu.setExistFlag("FINANCE_BANKSTATEMENT_LINES", false);
	Bean.currentMenu.setExistFlag("FINANCE_BANKSTATEMENT_EXECUTE", false);
	
	boolean lLineImported = false;
	
	if ("IMPORTED".equalsIgnoreCase(line.getValue("IMPORT_STATE_BS_LINE"))) {
		lLineImported = true;
	}
	
	if (Bean.currentMenu.isCurrentTab("FINANCE_BANKSTATEMENT_LINES") ||
			Bean.currentMenu.isCurrentTab("FINANCE_BANKSTATEMENT_EXECUTE")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}
	
	String l_posting_page = Bean.getDecodeParam(parameters.get("posting_page"));
	Bean.pageCheck(pageFormName + tagPosting, l_posting_page);
	String l_posting_page_beg = Bean.getFirstRowNumber(pageFormName + tagPosting);
	String l_posting_page_end = Bean.getLastRowNumber(pageFormName + tagPosting);

	String posting_find 	= Bean.getDecodeParam(parameters.get("posting_find"));
	posting_find 	= Bean.checkFindString(pageFormName + tagPostingFind, posting_find, l_posting_page);

	String l_reconcile_page = Bean.getDecodeParam(parameters.get("reconcile_page"));
	Bean.pageCheck(pageFormName + tagReconcile, l_reconcile_page);
	String l_reconcile_page_beg = Bean.getFirstRowNumber(pageFormName + tagReconcile);
	String l_reconcile_page_end = Bean.getLastRowNumber(pageFormName + tagReconcile);

	String reconcile_find 	= Bean.getDecodeParam(parameters.get("reconcile_find"));
	reconcile_find 	= Bean.checkFindString(pageFormName + tagReconcileFind, reconcile_find, l_reconcile_page);

	String l_file_lines_page = Bean.getDecodeParam(parameters.get("file_lines_page"));
	Bean.pageCheck(pageFormName + tagFileLines, l_file_lines_page);
	String l_file_lines_page_beg = Bean.getFirstRowNumber(pageFormName + tagFileLines);
	String l_file_lines_page_end = Bean.getLastRowNumber(pageFormName + tagFileLines);

	String file_line_find 	= Bean.getDecodeParam(parameters.get("file_line_find"));
	file_line_find 	= Bean.checkFindString(pageFormName + tagFileLineFind, file_line_find, l_file_lines_page);
%>

<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BANKSTATEMENT_INFO")) { %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/bankstatement_lineupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.bank_statementXML.getfieldTransl("h_delete_line", false), line.getValue("LINE_NUMBER")) %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_POSTINGS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BANKSTATEMENT_POSTINGS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/finance/bankstatement_lineupdate.jsp?id=" + id + "&type=posting&action=add&process=no", "", "") %>
				    <%= Bean.getMenuButton("DELETE_ALL", "../crm/finance/bankstatement_lineupdate.jsp?id=" + id + "&type=posting&action=deleteall&process=yes", Bean.postingXML.getfieldTransl("LAB_DELETE_ALL_POSTINGS", false), "") %>
				    <%= Bean.getMenuButton("POSTING", "../crm/finance/bankstatement_lineupdate.jsp?id=" + id + "&id_club=" + line.getValue("ID_CLUB") + "&type=posting&action=run&process=yes", Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS", false), "") %>
				<% } %>
		
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPosting, "../crm/finance/bankstatement_linespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BANKSTATEMENT_POSTINGS")+"&", "posting_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_RECONCILATION")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReconcile, "../crm/finance/bankstatement_linespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BANKSTATEMENT_RECONCILATION")+"&", "reconcile_page") %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_FILE_LINES")) {%>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagFileLines, "../crm/finance/bankstatement_linespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BANKSTATEMENT_FILE_LINES")+"&", "file_lines_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(line.getValue("LINE_NUMBER")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/bankstatement_linespecs.jsp?id=" + id) %>
			</td>


	</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
	
<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BANKSTATEMENT_INFO")) { %>
	<script>
		var formData = new Array (
			new Array ('line_number', 'integer', 1),
			new Array ('reconcile_state', 'varchar2', 1),
			new Array ('operation_date', 'varchar2', 1),
			new Array ('need_bs_line_import', 'varchar2', 1),
			new Array ('name_bank_account_correspondent', 'varchar2', 1)
		);
	</script>

		<form action="../crm/finance/bankstatement_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%= id %>">
			<% if (lLineImported) { %>
	        <input type="hidden" name="imported" value="yes">
			<% } else { %>
			<input type="hidden" name="imported" value="no">
			<% } %>
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("id_bank_statement", false) %>
					<%= Bean.getGoToFinanceBankStatementLink(line.getValue("ID_BANK_STATEMENT")) %>
				</td><td><input type="text" name="id_bank_statement" size="15" value="<%= line.getValue("ID_BANK_STATEMENT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_operation", false) %></td><td><input type="text" name="operation_code" size="15" value="<%= line.getValue("OPERATION_CODE") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("id_bank_statement_line", false) %></td> <td><input type="text" name="id_bank_statement_line" size="15" value="<%= line.getValue("ID_BANK_STATEMENT_LINE") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%=Bean.bank_statementXML.getfieldTransl("date_operation", true)%></td> <td><%=Bean.getCalendarInputField("operation_date", line.getValue("OPERATION_DATE_FRMT"), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("line_number", true) %></td> <td><input type="text" name="line_number" size="15" value="<%= line.getValue("LINE_NUMBER") %>" class="inputfield"> </td>
				<% if (lLineImported) { %>
					<td><%= Bean.bank_statementXML.getfieldTransl("need_bs_line_import", false) %></td> <td><input type="text" name="need_bs_line_import_tsl" size="15" value="<%= line.getValue("NEED_BS_LINE_IMPORT_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
				<% } else { %>
					<td><%= Bean.bank_statementXML.getfieldTransl("need_bs_line_import", true) %></td><td><select name="need_bs_line_import" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("NEED_BS_LINE_IMPORT"), true) %></select></td>
				<% } %>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("reconcile_state", true) %></td><td><select name="reconcile_state" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("BS_LINE_STATE", line.getValue("RECONCILE_STATE"), true) %></select></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("h_import_state", false) %></td> <td><input type="text" name="import_state" size="50" value="<%= line.getValue("IMPORT_STATE_BS_LINE_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>

			<tr>
				<td colspan="4" class="top_line">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2"><b><%= Bean.bank_statementXML.getfieldTransl("h_client", false) %></b></td>
				<td colspan="2"><b><%= Bean.bank_statementXML.getfieldTransl("h_correspondent", false) %></b></td>
			</tr>

			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("bank_account", false) %>
					<%= Bean.getGoToBankAccountLink(line.getValue("CLIENT_ID_BANK_ACCOUNT")) %>
				</td>
				<td>
					<input type="text" name="client_bank_account" id="client_bank_account" size="50" value="<%= line.getValue("CLIENT_NUMBER_BANK_ACCOUNT") %> - <%= line.getValue("CLIENT_NAME_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
				</td>			
				<td><%= Bean.bank_statementXML.getfieldTransl("bank_account", true) %>
					<%= Bean.getGoToBankAccountLink(line.getValue("CORR_ID_BANK_ACCOUNT")) %>
				</td>
				<td>
					<% if (lLineImported) { %>
						<%=Bean.getWindowFindBankAccount("bank_account_correspondent", line.getValue("CORR_ID_BANK_ACCOUNT"), "N", "_correspondent", "50") %>
					<% } else { %>
						<input type="text" name="name_bank_account_correspondent" size="50" value="<%= line.getValue("SRC_CORR_NUMBER_BANK_ACCOUNT") %>" class="inputfield">
					<% } %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("inn_number", false) %></td>
				<td>
					<input type="text" id="client_inn_number" name="client_inn_number" size="50" value="<%= line.getValue("CLIENT_OWNER_INN_NUMBER") %>" readonly="readonly" class="inputfield-ro">
				</td>			
				<td><%= Bean.bank_statementXML.getfieldTransl("inn_number", false) %></td>
				<td>
					<% if (lLineImported) { %>
						<input type="text" id="corr_inn_number" name="corr_inn_number" size="50" value="<%= line.getValue("CORR_OWNER_INN_NUMBER") %>" readonly="readonly" class="inputfield-ro">
					<% } else { %>
						<input type="text" id="corr_inn_number" name="corr_inn_number" size="50" value="<%= line.getValue("SRC_CORR_INN_NUMBER") %>" class="inputfield">
					<% } %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_jur_prs", false) %>
				<% if ("JUR_PRS".equalsIgnoreCase(line.getValue("CLIENT_TYPE_OWNER_BANK_ACCOUNT"))) { %>
					<%=Bean.getGoToJurPrsHyperLink(line.getValue("CLIENT_ID_OWNER_BANK_ACCOUNT")) %>
				<% } else { %>
					<%= Bean.getGoToNatPrsLink(line.getValue("CLIENT_ID_OWNER_BANK_ACCOUNT")) %>
				<% } %>
				</td>
				<td>
					<input type="hidden" name="id_jur_prs_client" id="id_jur_prs_client" size="50" value="<%= line.getValue("CLIENT_ID_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
					<input type="text" name="name_jur_prs_client" id="name_jur_prs_client" size="50" value="<%= line.getValue("CLIENT_NAME_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
				</td>			

				<td><%= Bean.bank_statementXML.getfieldTransl("name_jur_prs", false) %>
				<% if (lLineImported) { %>
					<% if ("JUR_PRS".equalsIgnoreCase(line.getValue("CORR_TYPE_OWNER_BANK_ACCOUNT"))) { %>
						<%=Bean.getGoToJurPrsHyperLink(line.getValue("CORR_ID_OWNER_BANK_ACCOUNT")) %>
					<% } else { %>
						<%= Bean.getGoToNatPrsLink(line.getValue("CORR_ID_OWNER_BANK_ACCOUNT")) %>
					<% } %>
					</td>
					<td>
						<input type="hidden" name="id_jur_prs_correspondent" id="id_jur_prs_correspondent" size="50" value="<%= line.getValue("CORR_ID_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
						<input type="text" name="name_jur_prs_correspondent" id="name_jur_prs_correspondent" size="50" value="<%= line.getValue("CORR_NAME_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
					</td>
				<% } else { %>
					<td><input type="text" id="corr_name" name="corr_name" size="50" value="<%= line.getValue("SRC_CORR_NAME") %>" class="inputfield"></td>
				<% } %>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("mfo_bank", false) %></td>
				<td>
					<input type="text" id="mfo_bank_client" name="mfo_bank_client" size="50" value="<%= line.getValue("CLIENT_MFO_BANK") %>" readonly="readonly" class="inputfield-ro">
				</td>			
				<td><%= Bean.bank_statementXML.getfieldTransl("mfo_bank", false) %></td>
				<td>
					<% if (lLineImported) { %>
						<input type="text" id="mfo_bank_correspondent" name="mfo_bank_correspondent" size="50" value="<%= line.getValue("CORR_MFO_BANK") %>" readonly="readonly" class="inputfield-ro">
					<% } else { %>
						<input type="text" id="mfo_bank_correspondent" name="mfo_bank_correspondent" size="50" value="<%= line.getValue("SRC_CORR_MFO_BANK") %>" class="inputfield">
					<% } %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_bank", false) %>
					<%=Bean.getGoToJurPrsHyperLink(line.getValue("CLIENT_ID_BANK")) %>
				</td>
				<td>
					<input type="hidden" id="id_bank_client" name="id_bank_client" value="<%= line.getValue("CLIENT_ID_BANK") %>" readonly="readonly" class="inputfield">
					<input type="text" id="name_bank_client" name="name_bank_client" size="50" value="<%= line.getValue("CLIENT_SNAME_BANK") %>" readonly="readonly" class="inputfield-ro">
				</td>			
				<td><%= Bean.bank_statementXML.getfieldTransl("name_bank", false) %>
					<%=Bean.getGoToJurPrsHyperLink(line.getValue("CORR_ID_BANK")) %>
				</td>
				<td>
					<% if (lLineImported) { %>
						<input type="hidden" id="id_bank_correspondent" name="id_bank_correspondent" value="<%= line.getValue("CORR_ID_BANK") %>" readonly="readonly" class="inputfield">
						<input type="text" id="name_bank_correspondent" name="name_bank_correspondent" size="50" value="<%= line.getValue("CORR_SNAME_BANK") %>" readonly="readonly" class="inputfield-ro">
					<% } else { %>
						<input type="text" id="name_bank_correspondent" name="name_bank_correspondent" size="50" value="<%= line.getValue("SRC_CORR_NAME_BANK") %>" class="inputfield">
					<% } %>
				</td>			
			</tr>

			<tr>
				<td colspan="4" class="top_line">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("number_document", false) %></td><td><input type="text" name="doc_number" size="15" value="<%= line.getValue("DOC_NUMBER") %>" class="inputfield"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("debet_amount", false) %></td><td><input type="text" name="debet_amount" size="15" value="<%= line.getValue("DEBET_AMOUNT_FRMT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%=Bean.bank_statementXML.getfieldTransl("date_document", false)%></td> <td><%=Bean.getCalendarInputField("doc_date", line.getValue("DOC_DATE_FRMT"), "10") %></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("credit_amount", false) %></td><td><input type="text" name="credit_amount" size="15" value="<%= line.getValue("CREDIT_AMOUNT_FRMT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_currency_client", false) %></td>
					<td><input type="hidden" id="cd_currency" name="cd_currency" value="<%= line.getValue("CD_CURRENCY") %>" readonly="readonly" class="inputfield">
					<input type="text" id="name_currency" name="name_currency" size="15" value="<%= line.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("payment_assignment", false) %></td><td  colspan="3"><textarea name="assignment" cols="90" rows="2" class="inputfield"><%= line.getValue("ASSIGNMENT") %></textarea> </td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					line.getValue(Bean.getCreationDateFieldName()),
					line.getValue("CREATED_BY"),
					line.getValue(Bean.getLastUpdateDateFieldName()),
					line.getValue("LAST_UPDATE_BY")
				) %>
 			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/bankstatement_lineupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/bankstatement.jsp") %>
				</td>
			</tr>
		</table>
		</form>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("doc_date", false) %>
	<%= Bean.getCalendarScript("operation_date", false) %>

	<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_BANKSTATEMENT_INFO")) {%>
		<form>
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("id_bank_statement", false) %>
					<%= Bean.getGoToFinanceBankStatementLink(line.getValue("ID_BANK_STATEMENT")) %>
				</td><td><input type="text" name="id_bank_statement" size="15" value="<%= line.getValue("ID_BANK_STATEMENT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_operation", false) %></td><td><input type="text" name="operation_code" size="15" value="<%= line.getValue("OPERATION_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("id_bank_statement_line", false) %></td> <td><input type="text" name="id_bank_statement_line" size="15" value="<%= line.getValue("ID_BANK_STATEMENT_LINE") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.bank_statementXML.getfieldTransl("date_operation", false) %></td><td><input type="text" name="operation_date" size="15" value="<%= line.getValue("OPERATION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("line_number", false) %></td> <td><input type="text" name="line_number" size="15" value="<%= line.getValue("LINE_NUMBER") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.bank_statementXML.getfieldTransl("reconcile_state", false) %></td><td><input type="text" name="reconcile_state" size="30" value="<%= line.getValue("RECONCILE_STATE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("h_import_state", false) %></td> <td><input type="text" name="import_state" size="50" value="<%= line.getValue("IMPORT_STATE_BS_LINE_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td colspan="2">&nbsp;</td>
			</tr>

			<tr>
				<td colspan="4" class="top_line">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2"><b><%= Bean.bank_statementXML.getfieldTransl("h_client", false) %></b></td>
				<td colspan="2"><b><%= Bean.bank_statementXML.getfieldTransl("h_correspondent", false) %></b></td>
			</tr>

			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("bank_account", false) %>
					<%= Bean.getGoToBankAccountLink(line.getValue("CLIENT_ID_BANK_ACCOUNT")) %>
				</td>
				<td>
					<input type="text" name="client_bank_account" id="client_bank_account" size="50" value="<%= line.getValue("CLIENT_NUMBER_BANK_ACCOUNT") %> - <%= line.getValue("CLIENT_NAME_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
				</td>			
				<td><%= Bean.bank_statementXML.getfieldTransl("bank_account", false) %>
					<%= Bean.getGoToBankAccountLink(line.getValue("CORR_ID_BANK_ACCOUNT")) %>
				</td>
				<td>
					<input type="text" name="correspondent_bank_account" id="correspondent_bank_account" size="50" value="<%= line.getValue("CORR_NUMBER_BANK_ACCOUNT") %> - <%= line.getValue("CORR_NAME_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
				</td>			
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("inn_number", false) %></td>
				<td>
					<input type="text" id="inn_number_client" name="inn_number_client" size="50" value="<%= line.getValue("CLIENT_OWNER_INN_NUMBER") %>" readonly="readonly" class="inputfield-ro">
				</td>			
				<td><%= Bean.bank_statementXML.getfieldTransl("inn_number", false) %></td>
				<td>
					<input type="text" id="inn_number_correspondent" name="inn_number_correspondent" size="50" value="<%= line.getValue("CORR_OWNER_INN_NUMBER") %>" readonly="readonly" class="inputfield-ro">
				</td>			
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_jur_prs", false) %>
				<% if ("JUR_PRS".equalsIgnoreCase(line.getValue("CLIENT_TYPE_OWNER_BANK_ACCOUNT"))) { %>
					<%=Bean.getGoToJurPrsHyperLink(line.getValue("CLIENT_ID_OWNER_BANK_ACCOUNT")) %>
				<% } else { %>
					<%= Bean.getGoToNatPrsLink(line.getValue("CLIENT_ID_OWNER_BANK_ACCOUNT")) %>
				<% } %>
				</td>
				<td>
					<input type="hidden" name="id_jur_prs_client" id="id_jur_prs_client" size="50" value="<%= line.getValue("CLIENT_ID_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
					<input type="text" name="name_jur_prs_client" id="name_jur_prs_client" size="50" value="<%= line.getValue("CLIENT_NAME_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
				</td>			

				<td><%= Bean.bank_statementXML.getfieldTransl("name_jur_prs", false) %>
				<% if ("JUR_PRS".equalsIgnoreCase(line.getValue("CORR_TYPE_OWNER_BANK_ACCOUNT"))) { %>
					<%=Bean.getGoToJurPrsHyperLink(line.getValue("CORR_ID_OWNER_BANK_ACCOUNT")) %>
				<% } else { %>
					<%= Bean.getGoToNatPrsLink(line.getValue("CORR_ID_OWNER_BANK_ACCOUNT")) %>
				<% } %>
				</td>
				<td>
					<input type="hidden" name="id_jur_prs_correspondent" id="id_jur_prs_correspondent" size="50" value="<%= line.getValue("CORR_ID_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
					<input type="text" name="name_jur_prs_correspondent" id="name_jur_prs_correspondent" size="50" value="<%= line.getValue("CORR_NAME_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
				</td>			
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("mfo_bank", false) %></td>
				<td>
					<input type="text" id="mfo_bank_client" name="mfo_bank_client" size="50" value="<%= line.getValue("CLIENT_MFO_BANK") %>" readonly="readonly" class="inputfield-ro">
				</td>			
				<td><%= Bean.bank_statementXML.getfieldTransl("mfo_bank", false) %></td>
				<td>
					<input type="text" id="mfo_bank_correspondent" name="mfo_bank_correspondent" size="50" value="<%= line.getValue("CORR_MFO_BANK") %>" readonly="readonly" class="inputfield-ro">
				</td>			
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_bank", false) %>
					<%=Bean.getGoToJurPrsHyperLink(line.getValue("CLIENT_ID_BANK")) %>
				</td>
				<td>
					<input type="hidden" id="id_bank_client" name="id_bank_client" value="<%= line.getValue("CLIENT_ID_BANK") %>" readonly="readonly" class="inputfield">
					<input type="text" id="name_bank_client" name="name_bank_client" size="50" value="<%= line.getValue("CLIENT_SNAME_BANK") %>" readonly="readonly" class="inputfield-ro">
				</td>			
				<td><%= Bean.bank_statementXML.getfieldTransl("name_bank", false) %>
					<%=Bean.getGoToJurPrsHyperLink(line.getValue("CORR_ID_BANK")) %>
				</td>
				<td>
					<input type="hidden" id="id_bank_correspondent" name="id_bank_correspondent" value="<%= line.getValue("CORR_ID_BANK") %>" readonly="readonly" class="inputfield">
					<input type="text" id="name_bank_correspondent" name="name_bank_correspondent" size="50" value="<%= line.getValue("CORR_SNAME_BANK") %>" readonly="readonly" class="inputfield-ro">
				</td>			
			</tr>

			<tr>
				<td colspan="4" class="top_line">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("number_document", false) %></td><td><input type="text" name="doc_number" size="15" value="<%= line.getValue("DOC_NUMBER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("debet_amount", false) %></td><td><input type="text" name="debet_amount" size="15" value="<%= line.getValue("DEBET_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("date_document", false) %></td><td><input type="text" name="doc_date" size="15" value="<%= line.getValue("DOC_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("credit_amount", false) %></td><td><input type="text" name="credit_amount" size="15" value="<%= line.getValue("CREDIT_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_currency_client", false) %></td>
					<td><input type="hidden" id="cd_currency" name="cd_currency" value="<%= line.getValue("CD_CURRENCY") %>" readonly="readonly" class="inputfield">
					<input type="text" id="name_currency" name="name_currency" size="15" value="<%= line.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("payment_assignment", false) %></td><td  colspan="3"><textarea name="assignment" cols="90" rows="2" readonly="readonly" class="inputfield-ro"><%= line.getValue("ASSIGNMENT") %></textarea> </td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					line.getValue(Bean.getCreationDateFieldName()),
					line.getValue("CREATED_BY"),
					line.getValue(Bean.getLastUpdateDateFieldName()),
					line.getValue("LAST_UPDATE_BY")
				) %>
 			<tr>
				<td colspan="6" align="center">
					<%=Bean.getGoBackButton("../crm/finance/bankstatement.jsp") %>
				</td>
			</tr>
		</table>
		</form>
	<%  }

 %>
	
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_POSTINGS")) { %>  
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("posting_find", posting_find, "../crm/finance/bankstatement_linespecs.jsp?id=" + id + "&posting_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= line.getPostingsHTML(posting_find, l_posting_page_beg, l_posting_page_end) %> 
<%} %>
	
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_RECONCILATION")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("reconcile_find", reconcile_find, "../crm/finance/bankstatement_linespecs.jsp?id=" + id + "&reconcile_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table> 
	<%= line.getClearingReconcileLinesHTML(reconcile_find, l_reconcile_page_beg, l_reconcile_page_end) %> 
<%}
	
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_FILE_LINES")) { %>  
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("file_line_find", file_line_find, "../crm/finance/bankstatement_linespecs.jsp?id=" + id + "&file_lines_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= line.getFileLinesHTML(file_line_find, l_file_lines_page_beg, l_file_lines_page_end) %> 
<%} 

%>

<%} %>
</div></div>
</body>
</html>
