<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcPostingObject"%>
<%@page import="bc.objects.bcPostingDetailObject"%>

<%@page import="bc.reports.bcReports"%>
<%@page import="java.util.HashMap"%>

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcPostingEditObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<Head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_POSTINGS";

Bean.setJspPageForTabName(pageFormName);

String tagLines = "_LINES";
String tagReport = "_REPORT";
String tagReportFind = "_REPORT_FIND";
String tagReportDetail = "_REPORT_DETAIL";
String tagReportDetailFind = "_REPORT_DETAIL_FIND";
String tagLogRowType = "_LOG_ROWTYPE";

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));


if (tab==null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
}

if (id==null || "".equalsIgnoreCase(id)) { 
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	Bean.currentMenu.setExistFlag("FINANCE_POSTINGS_INFO", true);
	
	bcPostingDetailObject posting = new bcPostingDetailObject(id);	
	bcPostingEditObject posting_edit = new bcPostingEditObject();	

	//Обрабатываем номера страниц
	String l_line_page = Bean.getDecodeParam(parameters.get("line_page"));
	Bean.pageCheck(pageFormName + tagLines, l_line_page);
	String l_line_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
	String l_line_page_end = Bean.getLastRowNumber(pageFormName + tagLines);

	String id_report = posting.getValue("ID_REPORT");

	String l_report_page = Bean.getDecodeParam(parameters.get("report_page"));
	Bean.pageCheck(pageFormName + tagReport, l_report_page);
	String l_report_page_beg = Bean.getFirstRowNumber(pageFormName + tagReport);
	String l_report_page_end = Bean.getLastRowNumber(pageFormName + tagReport);
	
	String report_find 	= Bean.getDecodeParam(parameters.get("report_find"));
	report_find 	= Bean.checkFindString(pageFormName + tagReportFind, report_find, l_report_page);
	
	String l_report_det_page = Bean.getDecodeParam(parameters.get("report_det_page"));
	Bean.pageCheck(pageFormName + tagReportDetail, l_report_det_page);
	String l_report_det_page_beg = Bean.getFirstRowNumber(pageFormName + tagReportDetail);
	String l_report_det_page_end = Bean.getLastRowNumber(pageFormName + tagReportDetail);
	
	String report_det_find 	= Bean.getDecodeParam(parameters.get("report_det_find"));
	report_det_find 	= Bean.checkFindString(pageFormName + tagReportDetailFind, report_det_find, l_report_det_page);

	String l_log_row_type = Bean.getDecodeParam(parameters.get("log_row_type"));
	l_log_row_type 	= Bean.checkFindString(pageFormName + tagLogRowType, l_log_row_type, l_report_det_page);
	 

%>

</head>
<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% 
			int hasEditPermission = 0;
			if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_POSTINGS_INFO")) {
				if ((posting.getValue("ID_POSTING_LINE")== null || "".equalsIgnoreCase(posting.getValue("ID_POSTING_LINE"))) &&
					(posting.getValue("ID_CLEARING_LINE")== null || "".equalsIgnoreCase(posting.getValue("ID_CLEARING_LINE")))) {
					hasEditPermission = 2;
				} else {
					hasEditPermission = 1;
				}
			} else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_POSTINGS_INFO")) {
				hasEditPermission = 1;
			}
			
			if (hasEditPermission==2) { %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/postingsupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.postingXML.getfieldTransl("h_delete_posting", false), posting.getValue("DEBET_CD_BK_ACCOUNT") + " - " + posting.getValue("CREDIT_CD_BK_ACCOUNT")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTINGS_REPORTS")) { %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagReportDetail, "../crm/finance/postingspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_POSTINGS_REPORTS")+"&id_report=" + id_report + "&", "report_det_page") %>
	
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(posting.getValue("DEBET_CD_BK_ACCOUNT") + " - " + posting.getValue("CREDIT_CD_BK_ACCOUNT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/postingspecs.jsp?id=" + id) %>
			</td>
	</tr>
</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTINGS_INFO")) { 
	
	%>
		<% if (hasEditPermission == 2) { %>
		<script>
			var formAll = new Array();
			var formData = new Array (
				new Array ('operation_date', 'varchar2', 1),
				new Array ('debet_name_bk_account', 'varchar2', 1),
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('credit_name_bk_account', 'varchar2', 1),
				new Array ('entered_amount', 'varchar2', 1),
				new Array ('run_postings_export', 'varchar2', 1),
				new Array ('using_in_clearing', 'varchar2', 1)
			);
			var formDataClearing = new Array (
				new Array ('name_bank_account_debet', 'varchar2', 1),
				new Array ('name_bank_account_credit', 'varchar2', 1)
			);
			var formDataExport = new Array (
				new Array ('cd_bk_doc_type', 'varchar2', 1)
			);
		
			function myValidateForm() {
				var using = document.getElementById('using_in_clearing').value;
				var run_exp = document.getElementById('run_postings_export').value;
		
				formAll = formData;
				if (using == 'Y') {
					formAll = formAll.concat(formDataClearing);
				}
				if (run_exp == 'Y') {
					formAll = formAll.concat(formDataExport);
				}
				return validateForm(formAll);
			}
			
			function checkClearing(){
				var using = document.getElementById('using_in_clearing').value;
				if (using == 'Y') {
					document.getElementById('span_receiver_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("receiver_id_bank_account", true) %>';
					document.getElementById('span_payer_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("payer_id_bank_account", true) %>';
				} else {
					document.getElementById('span_receiver_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("receiver_id_bank_account", false) %>';
					document.getElementById('span_payer_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("payer_id_bank_account", false) %>';
				}
			}
			
			function checkBKDocType(){
				var run_exp = document.getElementById('run_postings_export').value;
				if (run_exp == 'Y') {
					document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", true) %>';
				} else {
					document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) %>';
				}
			}
			checkClearing();
			checkBKDocType();
			
			function changeRelationShipParam(id, name){
				document.getElementById('id_club_rel').value = id;
				document.getElementById('name_club_rel').value = name;
				document.getElementById('name_club_rel').className = "inputfield_modified";
			}
		</script>
		<form action="../crm/finance/postingsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
			<%=posting_edit.getPostingEditHTML(id, Bean.getDateFormatTitle()) %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/postingsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/postings.jsp") %>
				</td>
			</tr>
		</table>
		</form>	
		<script type="text/javascript">
	  		Calendar.setup({
				inputField  : "id_operation_date",         // ID поля вводу дати
	      		ifFormat    : "<%= Bean.getJSPDateFormat()%>",    // формат дати (23.03.2008)
	      		button      : "btn_operation_date"       // ID кнопки для меню вибору дати
	    	});
  		</script>
  		<script type="text/javascript">
	    	Calendar.setup({
				inputField  : "id_conversion_date",         // ID поля вводу дати
	      		ifFormat    : "<%= Bean.getJSPDateFormat()%>",    // формат дати (23.03.2008)
	      		button      : "btn_conversion_date"       // ID кнопки для меню вибору дати
	    	});
		</script>
	
	<% } else if (hasEditPermission == 1) { %>
		<form>
		<table <%=Bean.getTableDetailParam() %>>
			<%=posting_edit.getPostingPreviewHTML(id) %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getGoBackButton("../crm/finance/postings.jsp") %>
				</td>
			</tr>
		</table>
		</form>
	<% }
 %>
	<br>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTINGS_REPORTS")) { 
    bcReports report = new bcReports(Bean.getReportFormat());
%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("report_find", report_find, "../crm/finance/postingspecs.jsp?id=" + id + "&report_page=1") %>
	
			<td>&nbsp;</td>
		</tr>
	</table>
	<%= report.getPostingsReportHTML(id_report, id, "", l_report_page_beg, l_report_page_end, "N") %>
		<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>

		<br>
		<br>
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("report_det_find", report_det_find, "../crm/finance/postingspecs.jsp?id=" + id + "&report_page=1&") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("log_row_type", "../crm/finance/postingspecs.jsp?id=" + id + "&report_page=1&", Bean.syslogXML.getfieldTransl("row_type", false)) %>
				<%=Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", l_log_row_type, true)%>
			<%=Bean.getSelectOnChangeEndHTML() %>
			</tr>
		</table>	

		<% bcSysLogObject log = new bcSysLogObject(); %>
		<%= log.getSysLogReportHTML(id_report, report_det_find, l_log_row_type, l_report_det_page_beg, l_report_det_page_end) %>
	<% } %>

<% } %>

<% } %>

</div></div>
</body>

<%@page import="bc.objects.bcSysLogObject"%></html>
