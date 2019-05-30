<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.reports.bcReports"%>
<%@page import="bc.objects.bcSysLogObject"%><html>
<%

String pageFormName = "FINANCE_BANKSTATEMENT";

String tagType = "_TYPE";
String tagFindHeader = "_FIND_HEADER";
String tagFindLine = "_FIND_LINE";
String tagPageHeader = "_PAGE_HEADER";
String tagPageLine = "_PAGE_LINE";
String tagHeaderImportState = "_HEADER_IMPORT_STATE";
String tagLineImportState = "_LINE_IMPORT_STATE";
String tagLineReconcileState = "_LINE_RECONCILE_STATE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 				= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_header			= Bean.getDecodeParam(parameters.get("find_header"));
String find_line			= Bean.getDecodeParam(parameters.get("find_line"));
String l_page 				= Bean.getDecodeParam(parameters.get("page"));
String l_page_header 		= Bean.getDecodeParam(parameters.get("page_header"));
String l_page_line	 		= Bean.getDecodeParam(parameters.get("page_line"));
String id_type 				= Bean.getDecodeParam(parameters.get("id_type"));
String header_import_state	= Bean.getDecodeParam(parameters.get("header_import_state"));
String line_import_state	= Bean.getDecodeParam(parameters.get("line_import_state"));
String line_reconcile_state	= Bean.getDecodeParam(parameters.get("line_reconcile_state"));

find_header 			= Bean.checkFindString(pageFormName + tagFindHeader, find_header, l_page_header);
find_line	 			= Bean.checkFindString(pageFormName + tagFindLine, find_line, l_page_line);
id_type 				= Bean.checkFindString(pageFormName + tagType, id_type, "");
header_import_state 	= Bean.checkFindString(pageFormName + tagHeaderImportState, header_import_state, l_page_header);
line_import_state 		= Bean.checkFindString(pageFormName + tagLineImportState, line_import_state, l_page_line);
line_reconcile_state 	= Bean.checkFindString(pageFormName + tagLineReconcileState, line_reconcile_state, l_page_line);

if (id_type==null || "".equalsIgnoreCase(id_type)) {
	id_type = "HEADERS";
}

Bean.pageCheck(pageFormName + tagPageHeader, l_page_header);
String l_page_header_beg = Bean.getFirstRowNumber(pageFormName + tagPageHeader);
String l_page_header_end = Bean.getLastRowNumber(pageFormName + tagPageHeader);

Bean.pageCheck(pageFormName + tagPageLine, l_page_line);
String l_page_line_beg = Bean.getFirstRowNumber(pageFormName + tagPageLine);
String l_page_line_end = Bean.getLastRowNumber(pageFormName + tagPageLine);


String tagFindReport = "_GENERAL_FIND_REPORT";
String tagFindLog = "_GENERAL_FIND_LOG";
String tagPageReport = "_GENERAL_PAGE_REPORT";
String tagPageReportDet = "_GENERAL_PAGE_REPORT_DET";
String tagPageReportLog = "_GENERAL_PAGE_REPORT_LOG";
String tagLogRowType = "_GENERAL_LOG_ROW_TYPE";
String tagReportType = "_GENERAL_REPORT_TYPE";

String report_type			= Bean.getDecodeParam(parameters.get("rep_type"));
if (report_type==null || "".equalsIgnoreCase(report_type)) {
	report_type = "report";
}

String find_report	 			= Bean.getDecodeParam(parameters.get("find_report"));
String find_log		 			= Bean.getDecodeParam(parameters.get("find_log"));
String l_page_report			= Bean.getDecodeParam(parameters.get("page_report"));
String id_report				= Bean.getDecodeParam(parameters.get("id_report"));
String l_log_row_type 			= Bean.getDecodeParam(parameters.get("log_row_type"));
String l_page_report_det		= Bean.getDecodeParam(parameters.get("page_report_det"));
String l_page_log_det			= Bean.getDecodeParam(parameters.get("page_log_det"));

find_report 	= Bean.checkFindString(pageFormName + tagFindReport, find_report, "");
find_log	 	= Bean.checkFindString(pageFormName + tagFindLog, find_log, "");
l_log_row_type 	= Bean.checkFindString(pageFormName + tagLogRowType, l_log_row_type, "");

Bean.pageCheck(pageFormName + tagPageReport, l_page_report);
String l_page_report_beg = Bean.getFirstRowNumber(pageFormName + tagPageReport);
String l_page_report_end = Bean.getLastRowNumber(pageFormName + tagPageReport);

Bean.pageCheck(pageFormName + tagPageReportDet, l_page_report_det);
String l_page_report_det_beg = Bean.getFirstRowNumber(pageFormName + tagPageReportDet);
String l_page_report_det_end = Bean.getLastRowNumber(pageFormName + tagPageReportDet);

Bean.pageCheck(pageFormName + tagPageReportLog, l_page_log_det);
String l_page_log_det_beg = Bean.getFirstRowNumber(pageFormName + tagPageReportLog);
String l_page_log_det_end = Bean.getLastRowNumber(pageFormName + tagPageReportLog);

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

			<% if ("HEADERS".equalsIgnoreCase(id_type)) { %>
				<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
			    	<%= Bean.getMenuButton("ADD", "../crm/finance/bankstatementupdate.jsp?type=general&action=add&process=no", "", "") %>
					<%= Bean.getMenuButton("LOAD_FROM_FILE", "../crm/finance/bankstatementimport.jsp?type=general&action=load_from_file&process=no", "", "", Bean.bank_statementXML.getfieldTransl("h_load_from_file", false)) %>
					<%= Bean.getMenuButton("IMPORT_ALL", "../crm/finance/bankstatementimport.jsp?type=general&action=import_all&process=yes", Bean.bank_statementXML.getfieldTransl("l_import_all", false), "", Bean.bank_statementXML.getfieldTransl("l_import_all", false)) %>
				<% } %>
			<% } else if ("REPORTS".equalsIgnoreCase(id_type)) { %>
					<%= Bean.getMenuButton("LOAD_FROM_FILE", "../crm/finance/bankstatementimport.jsp?type=general&action=load_from_file&process=no", "", "", Bean.bank_statementXML.getfieldTransl("h_load_from_file", false)) %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/finance/bankstatement.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<% if ("HEADERS".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getPagesHTML(pageFormName + tagPageHeader, "../crm/finance/bankstatement.jsp?", "page_header") %>
			<% } else if ("LINES".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getPagesHTML(pageFormName + tagPageLine, "../crm/finance/bankstatement.jsp?", "page_line") %>
			<% } else if ("REPORTS".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getPagesHTML(pageFormName + tagPageReport, "../crm/finance/bankstatement.jsp?", "page_report") %>
			<% } %>
		</tr>
	</table>
	
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<% if ("HEADERS".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getFindHTML("find_clearing", find_header, "../crm/finance/bankstatement.jsp?page_header=1&") %>
			<% } else if ("LINES".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getFindHTML("find_line", find_line, "../crm/finance/bankstatement.jsp?page_line=1&") %>
			<% } else if ("REPORTS".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getFindHTML("find_report", find_report, "../crm/finance/bankstatement.jsp?page_report=1&") %>
			<% } %>
	
			<%=Bean.getSelectOnChangeBeginHTML("id_type", "../crm/finance/bankstatement.jsp?page_header=1&page_report=1") %>
				<%=Bean.getSelectOptionHTML(id_type, "HEADERS", Bean.bank_statementXML.getfieldTransl("tab_headers", false)) %>
				<%=Bean.getSelectOptionHTML(id_type, "LINES", Bean.bank_statementXML.getfieldTransl("tab_lines", false)) %>
				<%=Bean.getSelectOptionHTML(id_type, "REPORTS", Bean.bank_statementXML.getfieldTransl("tab_reports", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<% if ("HEADERS".equalsIgnoreCase(id_type)) { %>
				<%=Bean.getSelectOnChangeBeginHTML("header_import_state", "../crm/finance/bankstatement.jsp?page_header=1&page_report=1", Bean.bank_statementXML.getfieldTransl("h_import_state", false)) %>
					<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("BS_HEADER_IMPORT_STATE", header_import_state, true)%>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<% } else if ("LINES".equalsIgnoreCase(id_type)) { %>
				<%=Bean.getSelectOnChangeBeginHTML("line_import_state", "../crm/finance/bankstatement.jsp?page_header=1&page_report=1", Bean.bank_statementXML.getfieldTransl("h_import_state", false)) %>
					<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("BS_LINE_IMPORT_STATE", line_import_state, true)%>
				<%=Bean.getSelectOnChangeEndHTML() %>
				<%=Bean.getSelectOnChangeBeginHTML("line_reconcile_state", "../crm/finance/bankstatement.jsp?page_header=1&page_report=1", Bean.bank_statementXML.getfieldTransl("reconcile_state", false)) %>
					<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("BS_LINE_STATE", line_reconcile_state, true)%>
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
<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
	<% Bean.header.setDeleteHyperLink("../crm/finance/bankstatementupdate.jsp?type=general&action=remove&process=yes&id=",Bean.bank_statementXML.getfieldTransl("h_delete_header", false),"ID_BANK_STATEMENT"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>

	<% if ("HEADERS".equalsIgnoreCase(id_type)) { %>
		<%= Bean.header.getBankStatementHeadHTML(header_import_state, find_header, l_page_header_beg, l_page_header_end, print) %> 
	<% } else if ("LINES".equalsIgnoreCase(id_type)) { %>
		<%= Bean.header.getBankStatementLinesHTML(line_import_state, line_reconcile_state, find_line, l_page_line_beg, l_page_line_end, print) %>
	<% } else if ("REPORTS".equalsIgnoreCase(id_type)) {  

   		bcReports report = new bcReports(Bean.getReportFormat());
		
	  %>
		<%= report.getReportHeaderHTML("'BANK_STATEMENT_LOAD_FROM_FILE'", "", find_report, id_report, "../crm/finance/bankstatement.jsp?rep_type=report&page_report_det=1", "../crm/finance/bankstatement.jsp?rep_type=log&page_log_det=1", l_page_report_beg, l_page_report_end, print) %>

		<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
			<% if ("report".equalsIgnoreCase(report_type)) { %>
				<br>
				<table  <%=Bean.getTableDetailParam() %>>
				<tr>
				<td align="right" width=20>
				  <select onchange="ajaxpage('../crm/finance/bankstatement.jsp?rep_type=report&id_report=<%=id_report %>&report_page=1&log_row_type='+this.value, 'div_main')" name="log_row_type" id="log_row_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", l_log_row_type, true) %></select>
				</td>
				<td align="right">
				<%= Bean.getPagesHTML(pageFormName + tagPageReportDet, "../crm/finance/bankstatement.jsp?rep_type=report&id_report="+id_report+"&", "page_report_det") %>
				</td>
				</tr>
				</table>
	
				<%= report.getBankStatementReportDetailHTML(id_report, "", l_log_row_type, l_page_report_det_beg, l_page_report_det_end, print) %>
			<% } else if ("log".equalsIgnoreCase(report_type)) { %>
				<br>
				<table  <%=Bean.getTableDetailParam() %>>
				<tr>
				<td align="right" width=20>
				  <select onchange="ajaxpage('../crm/finance/bankstatement.jsp?rep_type=log&id_report=<%=id_report %>&page_log_det=1&log_row_type='+this.value, 'div_main')" name="log_row_type" id="log_row_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", l_log_row_type, true) %></select>
				</td>
				<td align="right">
				<%= Bean.getPagesHTML(pageFormName + tagPageReportLog, "../crm/finance/bankstatement.jsp?rep_type=log&id_report="+id_report+"&", "page_log_det") %>
				</td>
				</tr>
				</table>
	
				<% bcSysLogObject log = new bcSysLogObject(); %>
	
				<%= log.getSysLogReportHTML(id_report, "", l_log_row_type, l_page_log_det_beg, l_page_log_det_end) %>
			<% } %>
		<% } %>


	<% } %>
<%} %>
</div></div>
</body>
</html>