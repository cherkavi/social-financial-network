<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcPostingObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.reports.bcReports"%>
<%@page import="bc.objects.bcSysLogObject"%><html>

<%= Bean.getLogOutScript(request) %>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "FINANCE_POSTINGS";
String tagType = "_TYPE";
String tagFindPosting = "_FIND_POSTING";
String tagFindReport = "_FIND_REPORT";
String tagPagePosting = "_PAGE_POSTING";
String tagPageReport = "_PAGE_REPORT";
String tagPageReportDet = "_PAGE_REPORT_DET";
String tagReportDetFind = "_REPORT_DET_FIND";
String tagLogRowType = "_LOG_ROW_TYPE_GENERAL";
String tagGrouped = "_GROUPED";
String tagClearing = "_CLEARING";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 					= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_posting 			= Bean.getDecodeParam(parameters.get("find_posting"));
String find_report	 			= Bean.getDecodeParam(parameters.get("find_report"));
String l_page_posting			= Bean.getDecodeParam(parameters.get("page_posting"));
String l_page_report			= Bean.getDecodeParam(parameters.get("page_report"));
String l_page_report_det		= Bean.getDecodeParam(parameters.get("page_report_det"));
String id_type 					= Bean.getDecodeParam(parameters.get("id_type"));
String id_report				= Bean.getDecodeParam(parameters.get("id_report"));
String l_log_row_type 			= Bean.getDecodeParam(parameters.get("log_row_type"));
String is_grouped				= Bean.getDecodeParam(parameters.get("is_grouped"));
String is_clearing				= Bean.getDecodeParam(parameters.get("is_clearing"));

find_posting 	= Bean.checkFindString(pageFormName + tagFindPosting, find_posting, l_page_posting);
find_report 	= Bean.checkFindString(pageFormName + tagFindReport, find_report, l_page_report);
id_type 		= Bean.checkFindString(pageFormName + tagType, id_type, "");
is_grouped 		= Bean.checkFindString(pageFormName + tagGrouped, is_grouped, l_page_posting);
is_clearing 	= Bean.checkFindString(pageFormName + tagClearing, is_clearing, l_page_posting);

l_log_row_type 	= Bean.checkFindString(pageFormName + tagLogRowType, l_log_row_type, "");
if (id_type==null || "".equalsIgnoreCase(id_type)) {
	id_type = "POSTINGS";
}
if (id_report==null) {
	id_report = "";
}
if (l_log_row_type==null) {
	l_log_row_type = "";
}

String l_page_posting_beg = "";
String l_page_posting_end = "";
String l_page_report_beg = "";
String l_page_report_end = "";
String l_page_report_det_beg = "";
String l_page_report_det_end = "";
String l_page_report_posting_beg = "";
String l_page_report_posting_end = "";

String report_det_find = Bean.getDecodeParam(parameters.get("report_det_find"));
report_det_find 	= Bean.checkFindString(pageFormName + tagReportDetFind, report_det_find, l_page_report_det);

if ("POSTINGS".equalsIgnoreCase(id_type)) {
	Bean.pageCheck(pageFormName + tagPagePosting, l_page_posting);
	l_page_posting_beg = Bean.getFirstRowNumber(pageFormName + tagPagePosting);
	l_page_posting_end = Bean.getLastRowNumber(pageFormName + tagPagePosting);
} else {
	Bean.pageCheck(pageFormName + tagPageReport, l_page_report);
	l_page_report_beg = Bean.getFirstRowNumber(pageFormName + tagPageReport);
	l_page_report_end = Bean.getLastRowNumber(pageFormName + tagPageReport);
	
	Bean.pageCheck(pageFormName + tagPageReportDet, l_page_report_det);
	l_page_report_det_beg = Bean.getFirstRowNumber(pageFormName + tagPageReportDet);
	l_page_report_det_end = Bean.getLastRowNumber(pageFormName + tagPageReportDet);
	
}

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

			<% if (Bean.hasEditTabsheetPermission("FINANCE_POSTINGS_REPORTS")) { %>
		    	<%= Bean.getMenuButton("RUN", "../crm/finance/postingsupdate.jsp?type=general&action=create&process=no", "", "", Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS", false)) %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/finance/postings.jsp?print=Y&id_report=" + id_report + "&log_row_type=" + l_log_row_type, "", "") %>
	
		    <!-- Вывод страниц -->
			<% if ("POSTINGS".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getPagesHTML(pageFormName + tagPagePosting, "../crm/finance/postings.jsp?", "page_posting") %>
			<% } else { %>
				<%= Bean.getPagesHTML(pageFormName + tagPageReport, "../crm/finance/postings.jsp?", "page_report") %>
			<% } %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<% if ("POSTINGS".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getFindHTML("find_posting", find_posting, "../crm/finance/postings.jsp?page_posting=1&") %>
			<% } else { %>
				<%= Bean.getFindHTML("find_report", find_report, "../crm/finance/postings.jsp?page_report=1&") %>
			<% } %>
	
			<% if ("POSTINGS".equalsIgnoreCase(id_type)) { %>
				<%=Bean.getSelectOnChangeBeginHTML("is_grouped", "../crm/finance/postings.jsp?page_posting=1") %>
					<%=Bean.getSelectOptionHTML(is_grouped, "", "") %>
					<%=Bean.getSelectOptionHTML(is_grouped, "1", Bean.postingXML.getfieldTransl("h_grouped", false)) %>
					<%=Bean.getSelectOptionHTML(is_grouped, "0", Bean.postingXML.getfieldTransl("h_not_grouped", false)) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
	
				<%=Bean.getSelectOnChangeBeginHTML("is_clearing", "../crm/finance/postings.jsp?page_posting=1") %>
					<%=Bean.getSelectOptionHTML(is_clearing, "", "") %>
					<%=Bean.getSelectOptionHTML(is_clearing, "1", Bean.postingXML.getfieldTransl("h_clearing", false)) %>
					<%=Bean.getSelectOptionHTML(is_clearing, "0", Bean.postingXML.getfieldTransl("h_not_clearing", false)) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			<%} %>
	
			<%=Bean.getSelectOnChangeBeginHTML("id_type", "../crm/finance/postings.jsp?page_posting=1&page_report=1") %>
				<%=Bean.getSelectOptionHTML(id_type, "POSTINGS", Bean.postingXML.getfieldTransl("tab_header_postings", false)) %>
				<%=Bean.getSelectOptionHTML(id_type, "REPORTS", Bean.postingXML.getfieldTransl("tab_header_reports", false)) %>
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
	<% if ("POSTINGS".equalsIgnoreCase(id_type)) { %>
		<%= Bean.header.getPostingsHeadHTML("", is_grouped, is_clearing, find_posting, l_page_posting_beg, l_page_posting_end, print) %>
	<% } else { 	    
		bcReports report = new bcReports(Bean.getReportFormat());
		
	  %>
		<%= report.getPostingsReportHTML(id_report, "", find_report, l_page_report_beg, l_page_report_end, print) %>

		<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
			<br>
			<table  <%=Bean.getTableBottomFilter() %>>
			<tr>

				<%= Bean.getFindHTML("report_det_find", report_det_find, "../crm/finance/postings.jsp?id_report=" + id_report + "&page_report_det=1&") %>
			
				<%=Bean.getSelectOnChangeBeginHTML("log_row_type", "../crm/finance/postings.jsp?id_report=" + id_report + "&page_report_det=1&", Bean.syslogXML.getfieldTransl("row_type", false)) %>
					<%=Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", l_log_row_type, true)%>
				<%=Bean.getSelectOnChangeEndHTML() %>

				<%= Bean.getPagesHTML(pageFormName + tagPageReportDet, "../crm/finance/postings.jsp?id_report="+id_report+"&", "page_report_det") %>
			</tr>
			</table>
	
			
			<% bcSysLogObject log = new bcSysLogObject(); %>
	
			<%= log.getSysLogReportHTML(id_report, report_det_find, l_log_row_type, l_page_report_det_beg, l_page_report_det_end) %>
		<% } %>
	<% } %>
<%  
} %>
</div></div>
</body>
</html>