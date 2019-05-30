<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcBankStatementHeaderObject"%>
<%@page import="com.lowagie.text.Table"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcBankStatementFileObject"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="bc.reports.bcReports"%>
<%@page import="bc.objects.bcSysLogObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_BANKSTATEMENT";
String tagPosting = "_POSTING";
String tagPostingFind = "_POSTING_FIND";
String tagLines = "_LINES";
String tagLineFind = "_LINE_FIND";
String tagReconcile = "_RECONCILE";
String tagReconcileFind = "_RECONCILE_FIND";
String tagFileLines = "_FILE_LINES";
String tagFileLineFind = "_FILE_LINE_FIND";

String tagReportType = "_REPORT_TYPE";

String tagReport = "_REPORT";
String tagReportFind = "_REPORT_FIND";
String tagReportDet = "_REPORT_DET";
String tagReportDetFind = "_REPORT_DET_FIND";
String tagReportDetRowType = "_REPORT_DET_ROW_TYPE";
String tagReportLog = "_REPORT_LOG";
String tagReportLogFind = "_REPORT_LOG_FIND";
String tagReportLogRowType = "_REPORT_LOG_ROW_TYPE";

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
	bcBankStatementHeaderObject header = new bcBankStatementHeaderObject("IMPORTED", id);
	
	Bean.currentMenu.setExistFlag("FINANCE_BANKSTATEMENT_LINES", true);
	Bean.currentMenu.setExistFlag("FINANCE_BANKSTATEMENT_EXECUTE", true);
	
	boolean lHeaderImported = true;
	
	if ("NOT_IMPORTED".equalsIgnoreCase(header.getValue("IMPORT_STATE_BS_HEADER")) || 
			"ERROR".equalsIgnoreCase(header.getValue("IMPORT_STATE_BS_HEADER"))) {
		lHeaderImported = false;
	}
	
	//Обрабатываем номера страниц
	String l_lines_page = Bean.getDecodeParam(parameters.get("lines_page"));
	Bean.pageCheck(pageFormName + tagLines, l_lines_page);
	String l_lines_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
	String l_lines_page_end = Bean.getLastRowNumber(pageFormName + tagLines);

	String line_find 	= Bean.getDecodeParam(parameters.get("line_find"));
	line_find 	= Bean.checkFindString(pageFormName + tagLineFind, line_find, l_lines_page);

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

	String l_page_report			= Bean.getDecodeParam(parameters.get("page_report"));
	Bean.pageCheck(pageFormName + tagReport, l_page_report);
	String l_page_report_beg = Bean.getFirstRowNumber(pageFormName + tagReport);
	String l_page_report_end = Bean.getLastRowNumber(pageFormName + tagReport);

	String report_find 	= Bean.getDecodeParam(parameters.get("report_find"));
	report_find 	= Bean.checkFindString(pageFormName + tagReportFind, report_find, l_page_report);

	String l_page_report_det		= Bean.getDecodeParam(parameters.get("page_report_det"));
	Bean.pageCheck(pageFormName + tagReportDet, l_page_report_det);
	String l_page_report_det_beg = Bean.getFirstRowNumber(pageFormName + tagReportDet);
	String l_page_report_det_end = Bean.getLastRowNumber(pageFormName + tagReportDet);

	String report_det_find 	= Bean.getDecodeParam(parameters.get("report_det_find"));
	report_det_find 	= Bean.checkFindString(pageFormName + tagReportDetFind, report_det_find, l_page_report_det);

	String report_det_row_type 	= Bean.getDecodeParam(parameters.get("report_det_row_type"));
	if ("0".equalsIgnoreCase(report_det_row_type)) {
		report_det_row_type = "";
	}
	report_det_row_type 	= Bean.checkFindString(pageFormName + tagReportDetRowType, report_det_row_type, l_page_report_det);

	String l_page_log_det			= Bean.getDecodeParam(parameters.get("page_log_det"));
	Bean.pageCheck(pageFormName + tagReportLog, l_page_log_det);
	String l_page_log_det_beg = Bean.getFirstRowNumber(pageFormName + tagReportLog);
	String l_page_log_det_end = Bean.getLastRowNumber(pageFormName + tagReportLog);

	String report_log_find 	= Bean.getDecodeParam(parameters.get("report_log_find"));
	report_log_find 	= Bean.checkFindString(pageFormName + tagReportLogFind, report_log_find, l_page_log_det);

	String report_log_row_type 	= Bean.getDecodeParam(parameters.get("report_log_row_type"));
	if ("0".equalsIgnoreCase(report_log_row_type)) {
		report_log_row_type = "";
	}
	report_log_row_type 	= Bean.checkFindString(pageFormName + tagReportLogRowType, report_log_row_type, l_page_report_det);

	String id_report				= Bean.getDecodeParam(parameters.get("id_report"));

	String report_type			= Bean.getDecodeParam(parameters.get("rep_type"));
	report_type 	= Bean.checkFindString(pageFormName + tagReportType, report_type, l_page_report);
	if (report_type==null || "".equalsIgnoreCase(report_type)) {
		report_type = "report";
		report_type 	= Bean.checkFindString(pageFormName + tagReportType, report_type, l_page_report);
	}

	if (header.getValue("ID_BANK_STATEMENT_FILE")==null || "".equalsIgnoreCase(header.getValue("ID_BANK_STATEMENT_FILE"))) {
		Bean.currentMenu.setExistFlag("FINANCE_BANKSTATEMENT_FILE_LINES",false);
		if (Bean.currentMenu.isCurrentTab("FINANCE_BANKSTATEMENT_FILE_LINES")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	} else {
		Bean.currentMenu.setExistFlag("FINANCE_BANKSTATEMENT_FILE_LINES",true);
	}
%>

<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BANKSTATEMENT_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/finance/bankstatementupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/bankstatementupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.bank_statementXML.getfieldTransl("h_delete_header", false), header.getValue("NUMBER_BANK_STATEMENT") + " - " + header.getValue("DATE_BANK_STATEMENT_FRMT")) %>
				<%= Bean.getMenuButton("IMPORT", "../crm/finance/bankstatementimport.jsp?id=" + id + "&type=general&action=import&process=no", Bean.bank_statementXML.getfieldTransl("l_import_one_statement", false), header.getValue("ID_BANK_STATEMENT") + " - " + header.getValue("BANK_STATEMENT_DATE_FRMT"), Bean.bank_statementXML.getfieldTransl("l_import_one_statement", false)) %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_LINES")) {%>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BANKSTATEMENT_LINES") &&
						lHeaderImported) { %>
					<%= Bean.getMenuButton("ADD", "../crm/finance/bankstatementupdate.jsp?id=" + id + "&type=line&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLines, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BANKSTATEMENT_LINES")+"&", "lines_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_RECONCILATION")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReconcile, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BANKSTATEMENT_RECONCILATION")+"&", "reconcile_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_POSTINGS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BANKSTATEMENT_POSTINGS")) { %>
				    <%= Bean.getMenuButton("DELETE_ALL", "../crm/finance/bankstatementupdate.jsp?id=" + id + "&type=posting&action=deleteall&process=yes", Bean.postingXML.getfieldTransl("LAB_DELETE_ALL_POSTINGS", false), "") %>
				    <%= Bean.getMenuButton("POSTING", "../crm/finance/bankstatementupdate.jsp?id=" + id + "&id_club=" + header.getValue("ID_CLUB") + "&type=posting&action=run&process=yes", Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS", false), "") %>
				<% } %>
		
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPosting, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BANKSTATEMENT_POSTINGS")+"&", "posting_page") %>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_FILE_LINES")) {%>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagFileLines, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BANKSTATEMENT_FILE_LINES")+"&", "file_lines_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_EXECUTE")) {%>
				<% if (id_report==null || "".equalsIgnoreCase(id_report)) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReport, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_BANKSTATEMENT_EXECUTE")+"&", "page_report") %>
				<% } else { %>
				<td>&nbsp;</td>
				<% } %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(header.getValue("NUMBER_BANK_STATEMENT") + " - " + header.getValue("DATE_BANK_STATEMENT_FRMT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/bankstatementspecs.jsp?id=" + id) %>
			</td>


	</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
	
<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BANKSTATEMENT_INFO")) { %>

		<% if (lHeaderImported) { %>
			<script>
				var formData = new Array (
					new Array ('date_bank_statement', 'varchar2', 1),
					new Array ('name_bank_account_client', 'varchar2', 1)
				);
			</script>
		<% } else { %>
			<script>
				var formData = new Array (
					new Array ('date_bank_statement', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('src_client_number_bank_account', 'varchar2', 1),
					new Array ('src_currency_code', 'varchar2', 1)
				);
			</script>
		<% } %>		

		<form action="../crm/finance/bankstatementupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%= id %>">
			<% if (lHeaderImported) { %>
	        <input type="hidden" name="imported" value="yes">
			<% } else { %>
			<input type="hidden" name="imported" value="no">
			<% } %>
			
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("id_bank_statement", false) %> </td><td><input type="text" name="id_bank_statement" size="15" value="<%= header.getValue("ID_BANK_STATEMENT") %>" readonly="readonly" class="inputfield-ro"></td>
				<% if (lHeaderImported) { %>
					<td><%= Bean.clubXML.getfieldTransl("club", false) %>
						<%= Bean.getGoToClubLink(header.getValue("ID_CLUB")) %>
					</td>
					<td><input type="text" name="name_club" size="70" value="<%= Bean.getClubShortName(header.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
					<td><%= Bean.clubXML.getfieldTransl("club", true) %>
						<%= Bean.getGoToClubLink(header.getValue("ID_CLUB")) %>
					</td>
					<%=Bean.getWindowFindClub("club", header.getValue("ID_CLUB"), "", "70") %>
				<% } %>		
				
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("number_bank_statement", false) %></td> <td><input type="text" name="number_bank_statement" size="15" value="<%= header.getValue("NUMBER_BANK_STATEMENT") %>" class="inputfield"> </td>
				<% if (lHeaderImported) { %>
					<td><%= Bean.bank_statementXML.getfieldTransl("number_bank_account_client", true) %>
						<%= Bean.getGoToBankAccountLink(header.getValue("ID_BANK_ACCOUNT")) %>
					</td>
					<td>
						<%=Bean.getWindowFindBankAccount("bank_account_client", header.getValue("ID_BANK_ACCOUNT"), "N", "_client", "58") %>
					</td>	
				<% } else { %>
					<td><%= Bean.bank_statementXML.getfieldTransl("number_bank_account_client", true) %></td> <td><input type="text" name="src_client_number_bank_account" size="70" value="<%= header.getValue("SRC_CLIENT_NUMBER_BANK_ACCOUNT") %>" class="inputfield"> </td>
				<% } %>		
			</tr>
			<tr>
				<td><%=Bean.bank_statementXML.getfieldTransl("date_bank_statement", true)%></td> <td><%=Bean.getCalendarInputField("date_bank_statement", header.getValue("DATE_BANK_STATEMENT_FRMT"), "10") %></td>
				<% if (lHeaderImported) { %>
					<td><%= Bean.bank_statementXML.getfieldTransl("name_jur_prs_client", false) %>
					<% if ("JUR_PRS".equalsIgnoreCase(header.getValue("TYPE_OWNER_BANK_ACCOUNT"))) { %>
						<%=Bean.getGoToJurPrsHyperLink(header.getValue("ID_OWNER_BANK_ACCOUNT")) %>
					</td> 
					<% } else { %>
						<%= Bean.getGoToNatPrsLink(header.getValue("ID_OWNER_BANK_ACCOUNT")) %>
					<% } %>
					<td>
						<input type="hidden" name="id_jur_prs_client" id="id_jur_prs_client" size="70" value="<%= header.getValue("ID_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
						<input type="text" name="name_jur_prs_client" id="name_jur_prs_client" size="70" value="<%= header.getValue("NAME_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> 
					</td>
				<% } else { %>
					<td><%= Bean.bank_statementXML.getfieldTransl("inn_number_jur_prs_client", false) %><td><input type="text" name="src_client_inn_number" size="70" value="<%= header.getValue("SRC_CLIENT_INN_NUMBER") %>" class="inputfield"> </td>
				<% } %>		
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("h_import_state", false) %></td> <td><input type="text" name="import_state" size="30" value="<%= Bean.getMeaningFoCodeValue("BS_HEADER_IMPORT_STATE", header.getValue("IMPORT_STATE_BS_HEADER")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<% if (lHeaderImported) { %>
					<td><%= Bean.bank_statementXML.getfieldTransl("name_bank_branch_client", false) %>
						<%=Bean.getGoToJurPrsHyperLink(header.getValue("ID_BANK")) %>
					</td>
					<td>
						<input type="hidden" id="id_bank" name="id_bank" value="<%= header.getValue("ID_BANK") %>" readonly="readonly" class="inputfield">
						<input type="text" id="name_bank" name="name_bank" size="70" value="<%= Bean.getJurPersonShortName(header.getValue("ID_BANK")) %>" readonly="readonly" class="inputfield-ro">
					</td>			
				<% } else { %>
					<td><%= Bean.bank_statementXML.getfieldTransl("name_jur_prs_client", false) %></td><td><input type="text" name="src_client_name" size="70" value="<%= header.getValue("SRC_CLIENT_NAME") %>" class="inputfield"> </td>
				<% } %>		
			</tr>
			<% if (!lHeaderImported) { %>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("mfo_bank_client", false) %></td><td><input type="text" name="src_client_mfo_bank" size="70" value="<%= header.getValue("SRC_CLIENT_MFO_BANK") %>" class="inputfield"> </td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_bank_branch_client", false) %></td><td><input type="text" name="src_client_name_bank" size="70" value="<%= header.getValue("SRC_CLIENT_NAME_BANK") %>" class="inputfield"> </td>
			</tr>
			<% } %>		
			<tr>
				<td colspan="2">&nbsp;</td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
			<% if (header.getValue("ID_BANK_STATEMENT_FILE")==null || "".equalsIgnoreCase(header.getValue("ID_BANK_STATEMENT_FILE"))) { %>
				<td>&nbsp;</td><td>&nbsp;</td>
			<% } else { 
				bcBankStatementFileObject file = new bcBankStatementFileObject(header.getValue("ID_BANK_STATEMENT_FILE"));
			%>
				<td><%= Bean.bank_statementXML.getfieldTransl("source_file", false) %></td>
				<td>
					<a href="../FileSender?FILENAME=<%=URLEncoder.encode(file.getValue("STORED_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
						<%=file.getValue("SRC_FILE_NAME") %>
					</a> 
				</td>
			<% } %>
			<% if (lHeaderImported) { %>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_currency_client", false) %></td>
					<td><input type="hidden" id="cd_currency" name="cd_currency" value="<%= header.getValue("CD_CURRENCY") %>">
					<input type="text" id="name_currency" name="name_currency" size="20" value="<%= header.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_currency_client", true) %></td> <td><input type="text" name="src_currency_code" size="15" value="<%= header.getValue("SRC_CURRENCY_CODE") %>" class="inputfield"> </td>
			<% } %>		
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("line_count", false) %></td><td><input type="text" name="line_count" size="15" value="<%= header.getValue("LINE_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("begin_balance", false) %></td><td><input type="text" name="begin_balance" size="15" value="<%= header.getValue("BEGIN_BALANCE_FRMT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("debet_line_count", false) %></td><td><input type="text" name="debet_line_count" size="15" value="<%= header.getValue("DEBET_LINE_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("debet_total", false) %></td><td><input type="text" name="debet_total" size="15" value="<%= header.getValue("DEBET_TOTAL_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("credit_line_count", false) %></td><td><input type="text" name="credit_line_count" size="15" value="<%= header.getValue("CREDIT_LINE_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("credit_total", false) %></td><td><input type="text" name="credit_total" size="15" value="<%= header.getValue("CREDIT_TOTAL_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("end_balance", false) %></td><td><input type="text" name="end_balance" size="15" value="<%= header.getValue("END_BALANCE_FRMT") %>" class="inputfield"></td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					header.getValue(Bean.getCreationDateFieldName()),
					header.getValue("CREATED_BY"),
					header.getValue(Bean.getLastUpdateDateFieldName()),
					header.getValue("LAST_UPDATE_BY")
				) %>
 			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/bankstatementupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/bankstatement.jsp") %>
				</td>
			</tr>
		</table>
		</form>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_bank_statement", false) %>

	<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_BANKSTATEMENT_INFO")) {%>
		<form>
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("id_bank_statement", false) %> </td><td><input type="text" name="id_bank_statement" size="15" value="<%= header.getValue("ID_BANK_STATEMENT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(header.getValue("ID_CLUB")) %>
				</td><td><input type="text" name="name_club" size="70" value="<%= Bean.getClubShortName(header.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("number_bank_statement", false) %></td> <td><input type="text" name="number_bank_statement" size="15" value="<%= header.getValue("NUMBER_BANK_STATEMENT") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.bank_statementXML.getfieldTransl("number_bank_account_client", false) %>
					<%= Bean.getGoToBankAccountLink(header.getValue("ID_BANK_ACCOUNT")) %>
				</td> <td><input type="text" name="number_bank_account" size="70" value="<%= header.getValue("NUMBER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("date_bank_statement", false) %></td><td><input type="text" name="date_bank_statement" size="15" value="<%= header.getValue("DATE_BANK_STATEMENT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("inn_number_jur_prs_client", false) %><td><input type="text" name="src_client_inn_number" size="70" value="<%= header.getValue("SRC_CLIENT_INN_NUMBER") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("h_import_state", false) %></td> <td><input type="text" name="import_state" size="30" value="<%= Bean.getMeaningFoCodeValue("BS_HEADER_IMPORT_STATE", header.getValue("IMPORT_STATE_BS_HEADER")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_jur_prs_client", false) %>
				<% if ("JUR_PRS".equalsIgnoreCase(header.getValue("TYPE_OWNER_BANK_ACCOUNT"))) { %>
					<%=Bean.getGoToJurPrsHyperLink(header.getValue("ID_OWNER_BANK_ACCOUNT")) %>
				<% } else { %>
					<%= Bean.getGoToNatPrsLink(header.getValue("ID_OWNER_BANK_ACCOUNT")) %>
				<% } %>
				</td> <td><input type="text" name="sname_jur_prs" size="70" value="<%= header.getValue("NAME_OWNER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("mfo_bank_client", false) %></td><td><input type="text" name="src_client_mfo_bank" size="70" value="<%= header.getValue("SRC_CLIENT_MFO_BANK") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("name_bank_branch_client", false) %>
					<%=Bean.getGoToJurPrsHyperLink(header.getValue("ID_BANK")) %>
				</td> <td><input type="text" name="name_bank_alt" size="70" value="<%= header.getValue("SNAME_BANK") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
			<% if (header.getValue("ID_BANK_STATEMENT_FILE")==null || "".equalsIgnoreCase(header.getValue("ID_BANK_STATEMENT_FILE"))) { %>
				<td>&nbsp;</td><td>&nbsp;</td>
			<% } else { 
				bcBankStatementFileObject file = new bcBankStatementFileObject(header.getValue("ID_BANK_STATEMENT_FILE"));
			%>
				<td><%= Bean.bank_statementXML.getfieldTransl("source_file", false) %></td>
				<td>
					<a href="../FileSender?FILENAME=<%=URLEncoder.encode(file.getValue("STORED_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
						<%=file.getValue("SRC_FILE_NAME") %>
					</a> 
				</td>
			<% } %>
				<td><%= Bean.bank_statementXML.getfieldTransl("cd_currency_client", false) %></td><td><input type="text" name="name_currency" size="20" value="<%= header.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("line_count", false) %></td><td><input type="text" name="line_count" size="15" value="<%= header.getValue("LINE_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("begin_balance", false) %></td><td><input type="text" name="begin_balance" size="15" value="<%= header.getValue("BEGIN_BALANCE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("debet_line_count", false) %></td><td><input type="text" name="debet_line_count" size="15" value="<%= header.getValue("DEBET_LINE_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("debet_total", false) %></td><td><input type="text" name="debet_total" size="15" value="<%= header.getValue("DEBET_TOTAL_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.bank_statementXML.getfieldTransl("credit_line_count", false) %></td><td><input type="text" name="credit_line_count" size="15" value="<%= header.getValue("CREDIT_LINE_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.bank_statementXML.getfieldTransl("credit_total", false) %></td><td><input type="text" name="credit_total" size="15" value="<%= header.getValue("CREDIT_TOTAL_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
 			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.bank_statementXML.getfieldTransl("end_balance", false) %></td><td><input type="text" name="end_balance" size="15" value="<%= header.getValue("END_BALANCE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					header.getValue(Bean.getCreationDateFieldName()),
					header.getValue("CREATED_BY"),
					header.getValue(Bean.getLastUpdateDateFieldName()),
					header.getValue("LAST_UPDATE_BY")
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
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_LINES")) { %> 
	<script type="text/javascript">
	 	function CheckCB(Element) {
			myCheck = true;
	
			thisCheckBoxes = document.getElementsByTagName('input');
			for (i = 1; i < thisCheckBoxes.length; i++) { 
				myName = thisCheckBoxes[i].name;
				if (myName.substr(0,6) == 'chb_id'){
					myCheck = myCheck && thisCheckBoxes[i].checked;
				}
			}
			if (document.getElementById('mainCheck')) {
				document.getElementById('mainCheck').checked = myCheck;
			}
		}
		function CheckAll(Element,Name) {
			thisCheckBoxes = document.getElementsByTagName('input');
			for (i = 1; i < thisCheckBoxes.length; i++) { 
				myName = thisCheckBoxes[i].name;
				
				if (myName.substr(0,6) == Name){
						thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
				}
			}
		}
		CheckCB();
	</script>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("line_find", line_find, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&lines_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= header.getBankStatementLinesHTML(line_find, l_lines_page_beg, l_lines_page_end) %> 
<%} %>
	
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_RECONCILATION")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("reconcile_find", reconcile_find, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&reconcile_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table> 
	<%= header.getClearingReconcileLinesHTML(reconcile_find, l_reconcile_page_beg, l_reconcile_page_end) %> 
<%} %>
	
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_POSTINGS")) { %> 
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("posting_find", posting_find, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&posting_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= header.getPostingsHTML(posting_find, l_posting_page_beg, l_posting_page_end) %> 
<%} %>
	
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_BANKSTATEMENT_FILE_LINES")) { %> 
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("file_line_find", file_line_find, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&file_lines_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= header.getBankStatementFileLinesHTML(file_line_find, l_file_lines_page_beg, l_file_lines_page_end) %> 
<%}

if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_BANKSTATEMENT_EXECUTE")) {  
	bcReports report = new bcReports(Bean.getReportFormat());		
	  %> 
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<% if (id_report==null || "".equalsIgnoreCase(id_report)) { %>
		<%= Bean.getFindHTML("report_find", report_find, "../crm/finance/bankstatementspecs.jsp?id=" + id + "&page_report=1") %>
		<% } else { %>
		<td>&nbsp;</td>
		<% } %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
		<%= report.getReportHeaderHTML("'BANK_STATEMENT_LOAD_FROM_FILE', 'BANK_STATEMENT_IMPORT'", header.getValue("ID_BANK_STATEMENT"), report_find, id_report, "../crm/finance/bankstatementspecs.jsp?rep_type=report&page_report_det=1&id=" + id, "../crm/finance/bankstatementspecs.jsp?rep_type=log&page_log_det=1&id=" + id, l_page_report_beg, l_page_report_end, "N") %>

		<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
			<% if ("report".equalsIgnoreCase(report_type)) { %>
				<br> 
				<table <%=Bean.getTableBottomFilter() %>>
				  	<tr>
					<%= Bean.getFindHTML("report_det_find", report_det_find, "../crm/finance/bankstatementspecs.jsp?rep_type=report&id=" + id + "&id_report=" + id_report + "&page_report_det=1") %>
			
					<%=Bean.getSelectOnChangeBeginHTML("report_det_row_type", "../crm/finance/bankstatementspecs.jsp?rep_type=log&id=" + id + "&id_report="+id_report + "&page_report_det=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
						<%= Bean.getSelectOptionHTML("", "0", "") %>
						<%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", report_det_row_type, false) %>
					<%=Bean.getSelectOnChangeEndHTML() %>

					<%= Bean.getPagesHTML(pageFormName + tagReportDet, "../crm/finance/bankstatementspecs.jsp?rep_type=report&id=" + id + "&id_report="+id_report+"&", "page_report_det") %>
				  	</tr>
				</table>
	
				<%= report.getBankStatementReportDetailHTML(id_report, report_det_find, report_det_row_type, l_page_report_det_beg, l_page_report_det_end, "N") %>
			<% } else if ("log".equalsIgnoreCase(report_type)) { %>
				<br>
				<table <%=Bean.getTableBottomFilter() %>>
				  	<tr>
					<%= Bean.getFindHTML("report_log_find", report_log_find, "../crm/finance/bankstatementspecs.jsp?rep_type=log&id=" + id + "&id_report=" + id_report + "&page_log_det=1") %>
			
					<%=Bean.getSelectOnChangeBeginHTML("report_log_row_type", "../crm/finance/bankstatementspecs.jsp?rep_type=log&id=" + id + "&id_report="+id_report + "&page_log_det=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
						<%= Bean.getSelectOptionHTML("", "0", "") %>
						<%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", report_log_row_type, false) %>
					<%=Bean.getSelectOnChangeEndHTML() %>
	
					<%= Bean.getPagesHTML(pageFormName + tagReportLog, "../crm/finance/bankstatementspecs.jsp?rep_type=log&id=" + id + "&id_report="+id_report+"&", "page_log_det") %>
				  	</tr>
				</table>
	
				<% bcSysLogObject log = new bcSysLogObject(); %>
	
				<%= log.getSysLogReportHTML(id_report, report_log_find, report_log_row_type, l_page_log_det_beg, l_page_log_det_end) %>
			<% } %>
		<% } %>

	
<%} else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_BANKSTATEMENT_EXECUTE")) { %>
 <br><b><%= Bean.form_messageXML.getfieldTransl("operation_denied", false) %></b>
<% } 

%>

<%} %>
</div></div>
</body>
</html>
