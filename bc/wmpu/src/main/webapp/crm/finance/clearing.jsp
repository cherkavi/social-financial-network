<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.reports.bcReports"%><html>

<%

String pageFormName = "FINANCE_CLEARING";
String tagFindClering = "_FIND_CLEARING";
String tagFindLine = "_FIND_LINE";
String tagFindReport = "_FIND_REPORT";
String tagType = "_TYPE";
String tagPageClearing = "_PAGE_CLEARING";
String tagPageLine = "_PAGE_LINE";
String tagPageReport = "_PAGE_REPORT";
String tagPageReportDet = "_PAGE_REPORT_DET";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 				= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_clearing		= Bean.getDecodeParam(parameters.get("find_clearing"));
String find_line			= Bean.getDecodeParam(parameters.get("find_line"));
String find_report 			= Bean.getDecodeParam(parameters.get("find_report"));
String l_page_clearing 		= Bean.getDecodeParam(parameters.get("page_clearing"));
String l_page_line	 		= Bean.getDecodeParam(parameters.get("page_line"));
String l_page_report 		= Bean.getDecodeParam(parameters.get("page_report"));
String l_page_report_det	= Bean.getDecodeParam(parameters.get("page_report_det"));
String id_type 				= Bean.getDecodeParam(parameters.get("id_type"));
String id_report			= Bean.getDecodeParam(parameters.get("id_report"));

find_clearing 	= Bean.checkFindString(pageFormName + tagFindClering, find_clearing, l_page_clearing);
find_line	 	= Bean.checkFindString(pageFormName + tagFindLine, find_line, l_page_line);
find_report 	= Bean.checkFindString(pageFormName + tagFindReport, find_report, l_page_report);
id_type 		= Bean.checkFindString(pageFormName + tagType, id_type, "");

if (id_type==null || "".equalsIgnoreCase(id_type)) {
	id_type = "CLEARING";
}
if (id_report==null) {
	id_report = "";
}

Bean.pageCheck(pageFormName + tagPageClearing, l_page_clearing);
String l_page_clearing_beg = Bean.getFirstRowNumber(pageFormName + tagPageClearing);
String l_page_clearing_end = Bean.getLastRowNumber(pageFormName + tagPageClearing);

Bean.pageCheck(pageFormName + tagPageLine, l_page_line);
String l_page_line_beg = Bean.getFirstRowNumber(pageFormName + tagPageLine);
String l_page_line_end = Bean.getLastRowNumber(pageFormName + tagPageLine);

Bean.pageCheck(pageFormName + tagPageReport, l_page_report);
String l_page_report_beg = Bean.getFirstRowNumber(pageFormName + tagPageReport);
String l_page_report_end = Bean.getLastRowNumber(pageFormName + tagPageReport);

Bean.pageCheck(pageFormName + tagPageReportDet, l_page_report_det);
String l_page_report_det_beg = Bean.getFirstRowNumber(pageFormName + tagPageReportDet);
String l_page_report_det_end = Bean.getLastRowNumber(pageFormName + tagPageReportDet);

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
		    <%= Bean.getMenuButton("RUN", "../crm/finance/clearingupdate.jsp?type=create&action=run&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/finance/clearing.jsp?print=Y&id_report=" + id_report, "", "") %>
	
		    <!-- Вывод страниц -->
			<% if ("CLEARING".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getPagesHTML(pageFormName + tagPageClearing, "../crm/finance/clearing.jsp?", "page_clearing") %>
			<% } else if ("LINES".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getPagesHTML(pageFormName + tagPageLine, "../crm/finance/clearing.jsp?", "page_line") %>
			<% } else { %>
				<%= Bean.getPagesHTML(pageFormName + tagPageReport, "../crm/finance/clearing.jsp?", "page_report") %>
			<% } %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<% if ("CLEARING".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getFindHTML("find_clearing", find_clearing, "../crm/finance/clearing.jsp?page_clearing=1&") %>
			<% } else if ("LINES".equalsIgnoreCase(id_type)) { %>
				<%= Bean.getFindHTML("find_line", find_line, "../crm/finance/clearing.jsp?page_line=1&") %>
			<% } else { %>
				<%= Bean.getFindHTML("find_report", find_report, "../crm/finance/clearing.jsp?page_report=1&") %>
			<% } %>
	
			<%=Bean.getSelectOnChangeBeginHTML("id_type", "../crm/finance/clearing.jsp?page_clearing=1&page_report=1") %>
				<%=Bean.getSelectOptionHTML(id_type, "CLEARING", Bean.clearingXML.getfieldTransl("tab_header_clearing", false)) %>
				<%=Bean.getSelectOptionHTML(id_type, "LINES", Bean.clearingXML.getfieldTransl("tab_header_lines", false)) %>
				<%=Bean.getSelectOptionHTML(id_type, "REPORTS", Bean.clearingXML.getfieldTransl("tab_header_reports", false)) %>
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
<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
	<% Bean.header.setDeleteHyperLink("../crm/finance/clearingupdate.jsp?type=header&action=remove&process=yes&id_clearing=",Bean.clearingXML.getfieldTransl("h_clearing_remove", false),"ID_CLEARING"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<% if ("CLEARING".equalsIgnoreCase(id_type)) { %>
		<%= Bean.header.getClearingHeadHTML(find_clearing, l_page_clearing_beg, l_page_clearing_end, print) %>
	<% } else if ("LINES".equalsIgnoreCase(id_type)) { %>
		<%= Bean.header.getClearingLinesHeadHTML(find_line, l_page_line_beg, l_page_line_end, print) %>
	<% } else { 
	    
		bcReports report = new bcReports(Bean.getReportFormat());
	%>
		<%= report.getClearingReportHTML(id_report, find_report, l_page_report_beg, l_page_report_end, print) %>

		<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
			<br>
			<br>
			<table  <%=Bean.getTableDetailParam() %> style="border: 0px;">
			<tr>
			<td align="right">
			<%= Bean.getPagesHTML(pageFormName + tagPageReportDet, "../crm/finance/postings.jsp?id_report="+id_report+"&", "page_report_det") %>
			</td>
			</tr>
			</table>
	

			<%= report.getClearingReportDetailHTML(id_report, l_page_report_det_beg, l_page_report_det_end) %>
		<% } %>
	<% } %>
<%} %>
</div></div>
</body>
</html>