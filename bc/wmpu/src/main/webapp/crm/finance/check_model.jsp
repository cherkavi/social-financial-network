<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.reports.bcReports"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "FINANCE_CHECK_MODEL";
String tagFind = "_FIND";
String tagFindDet = "_FIND_DET";
String tagReportType = "_REPORT_TYPE";
String tagPageReport = "_PAGE_REPORT";
String tagPageReportDet = "_PAGE_REPORT_DET";
String tagStateLine = "_STATE_LINE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 				= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 			= Bean.getDecodeParam(parameters.get("find_string"));
String find_det 			= Bean.getDecodeParam(parameters.get("find_det"));
String report_type			= Bean.getDecodeParam(parameters.get("report_type"));
String l_page_report 		= Bean.getDecodeParam(parameters.get("page_report"));
String l_page_report_det	= Bean.getDecodeParam(parameters.get("page_report_det"));
String id_report			= Bean.getDecodeParam(parameters.get("id_report"));
String state_line			= Bean.getDecodeParam(parameters.get("state_line"));

if (id_report==null || "".equalsIgnoreCase(id_report) || "null".equalsIgnoreCase(id_report)) {
	id_report = "";
}
find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page_report);
find_det	 	= Bean.checkFindString(pageFormName + tagFindDet, find_det, l_page_report_det);
report_type		= Bean.checkFindString(pageFormName + tagReportType, report_type, l_page_report);
state_line		= Bean.checkFindString(pageFormName + tagStateLine, state_line, l_page_report_det);

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
		    <%= Bean.getMenuButton("RUN", "../crm/finance/check_modelspecs.jsp", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/finance/check_model.jsp?print=Y?id_report="+id_report, "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagPageReport, "../crm/finance/check_model.jsp?", "page_report") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/finance/check_model.jsp?page_report=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("report_type", "../crm/finance/check_model.jsp?page_report=1", Bean.posting_schemeXML.getfieldTransl("name_bk_operation_type", false)) %>
			<%
				String[] pOnlyValues = new String[3];
				pOnlyValues[0] = "ALL_FINANCE_MODEL_CHECK";
				pOnlyValues[1] = "CLUB_REL_CHECK";
				pOnlyValues[2] = "BK_OPERATION_CHECK";
				%>
			<%= Bean.getSelectBodyFromLookups("REPORT_TYPE", "CODE_MEANING", "CODE", null, pOnlyValues, report_type, null, true, false) %>
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

<% if (Bean.hasAccessMenuPermission(pageFormName)) {
	bcReports report = new bcReports(Bean.getReportFormat());
%>
	<%= report.getCheckFinModelHTML(report_type, "", id_report, find_string, l_page_report_beg, l_page_report_end, print) %>
	
	<% if (!"".equalsIgnoreCase(id_report)) { %>
	<br>
	<br>
	<table  <%=Bean.getTableBottomFilter() %> style="border: 0px;">
		<tr>
			<%= Bean.getFindHTML("find_det", find_det, "../crm/finance/check_model.jsp?id_report="+id_report+"&page_report_det=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("state_line", "../crm/finance/check_model.jsp?id_report="+id_report+"&page_report_det=1", Bean.reportXML.getfieldTransl("state_line", false)) %>
				<%= Bean.getMeaningFromLookupNameOptions("REPORT_LINE_STATE", state_line, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%= Bean.getPagesHTML(pageFormName + tagPageReportDet, "../crm/finance/check_model.jsp?id_report="+id_report+"&", "page_report_det") %>
		</tr>
	</table>

	<%= report.getCheckModelReportDetailHTML(id_report, find_det, state_line, l_page_report_det_beg, l_page_report_det_end) %>
	<% } %>
<%} %>
</div></div>
</body>
</html>