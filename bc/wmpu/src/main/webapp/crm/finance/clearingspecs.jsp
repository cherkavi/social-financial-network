<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcClearingObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcSysLogObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<% 
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

request.setCharacterEncoding("UTF-8");

String pageFormName = "FINANCE_CLEARING";

Bean.setJspPageForTabName(pageFormName);

String tagLines = "_LINES";
String tagLineFind = "LINE_FIND";
String tagPostings = "_POSTINGS";
String tagPostingFind = "_POSTING_FIND";
String tagExpFiles = "_EXPORT_FILES";
String tagExpFileFind = "_EXPORT_FILES_FIND";
String tagExpFilesDetail = "_EXPORT_FILES_DETAIL";
String tagExpFileDetailFind = "_EXPORT_FILE_DETAIL_FIND";
String tagLog = "_LOG";
String tagLogFind = "_LOG_FIND";
String tagLogRowType = "_LOG_ROW_TYPE";
String tagDetailType = "_DETAIL_TYPE";
String tagReconcile = "_RECONCILE";
String tagReconcileFind = "_RECONCILE_FIND";

String id = Bean.getDecodeParam(parameters.get("id"));
if (id==null || "".equalsIgnoreCase(id.trim())) { id = ""; }

String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	if (tab==null || "".equalsIgnoreCase(tab)) { 
		tab = Bean.tabsHmGetValue(pageFormName);
	}
	Bean.tabsHmSetValue(pageFormName, tab);
	bcClearingObject clearing = new bcClearingObject(id);

	Bean.currentMenu.setExistFlag("FINANCE_CLEARING_INFO", true);
	Bean.currentMenu.setExistFlag("FINANCE_CLEARING_LINES", true);
	Bean.currentMenu.setExistFlag("FINANCE_CLEARING_POSTINGS", true);
	Bean.currentMenu.setExistFlag("FINANCE_CLEARING_EXPORT", true);
	Bean.currentMenu.setExistFlag("FINANCE_CLEARING_EXPFILES", true);

	//Обрабатываем номера страниц
	String l_lines_page = Bean.getDecodeParam(parameters.get("lines_page"));
	Bean.pageCheck(pageFormName + tagLines, l_lines_page);
	String l_lines_page_beg = Bean.getFirstRowNumber(pageFormName + tagLines);
	String l_lines_page_end = Bean.getLastRowNumber(pageFormName + tagLines);

	String line_find 	= Bean.getDecodeParam(parameters.get("line_find"));
	line_find 	= Bean.checkFindString(pageFormName + tagLineFind, line_find, l_lines_page);
	
	String id_owner_filtr = Bean.getDecodeParam(parameters.get("id_owner_filtr"));
	if (id_owner_filtr==null || "".equalsIgnoreCase(id_owner_filtr.trim())) { id_owner_filtr = ""; }
	
	String l_postings_page = Bean.getDecodeParam(parameters.get("postings_page"));
	Bean.pageCheck(pageFormName + tagPostings, l_postings_page);
	String l_postings_page_beg = Bean.getFirstRowNumber(pageFormName + tagPostings);
	String l_postings_page_end = Bean.getLastRowNumber(pageFormName + tagPostings);

	String posting_find 	= Bean.getDecodeParam(parameters.get("posting_find"));
	posting_find 	= Bean.checkFindString(pageFormName + tagPostingFind, posting_find, l_postings_page);
	
	String l_exp_files_page = Bean.getDecodeParam(parameters.get("exp_files_page"));
	Bean.pageCheck(pageFormName + tagExpFiles, l_exp_files_page);
	String l_exp_files_page_beg = Bean.getFirstRowNumber(pageFormName + tagExpFiles);
	String l_exp_files_page_end = Bean.getLastRowNumber(pageFormName + tagExpFiles);

	String exp_file_find 	= Bean.getDecodeParam(parameters.get("exp_file_find"));
	exp_file_find 	= Bean.checkFindString(pageFormName + tagExpFileFind, exp_file_find, l_exp_files_page);

	String l_reconcile_page = Bean.getDecodeParam(parameters.get("reconcile_page"));
	Bean.pageCheck(pageFormName + tagReconcile, l_reconcile_page);
	String l_reconcile_page_beg = Bean.getFirstRowNumber(pageFormName + tagReconcile);
	String l_reconcile_page_end = Bean.getLastRowNumber(pageFormName + tagReconcile);

	String reconcile_find 	= Bean.getDecodeParam(parameters.get("reconcile_find"));
	reconcile_find 	= Bean.checkFindString(pageFormName + tagReconcileFind, reconcile_find, l_reconcile_page);

	String detail_type = Bean.getDecodeParam(parameters.get("detail_type"));
	detail_type 	= Bean.checkFindString(pageFormName + tagDetailType, detail_type, "");
	if (detail_type==null || "".equalsIgnoreCase(detail_type)) {
		detail_type = "FILE";
		detail_type 	= Bean.checkFindString(pageFormName + tagDetailType, detail_type, "");
	}

	String l_log_page = Bean.getDecodeParam(parameters.get("log_page"));
	Bean.pageCheck(pageFormName + tagLog, l_log_page);
	String l_log_page_beg = Bean.getFirstRowNumber(pageFormName + tagLog);
	String l_log_page_end = Bean.getLastRowNumber(pageFormName + tagLog);

	String log_find 	= Bean.getDecodeParam(parameters.get("log_find"));
	log_find 	= Bean.checkFindString(pageFormName + tagLogFind, log_find, l_log_page);
	
	String log_row_type 	= Bean.getDecodeParam(parameters.get("log_row_type"));
	if ("0".equalsIgnoreCase(log_row_type)) {
		log_row_type = "";
	}
	log_row_type 	= Bean.checkFindString(pageFormName + tagLogRowType, log_row_type, l_log_page);

	String l_exp_file_det_page = Bean.getDecodeParam(parameters.get("exp_file_det_page"));

	String id_file = Bean.getDecodeParam(parameters.get("id_file"));

	if (id_file==null || "".equalsIgnoreCase(id_file)) {
		l_exp_file_det_page = "1";
	}
	Bean.pageCheck(pageFormName + tagExpFilesDetail, l_exp_file_det_page);
	String l_exp_file_det_page_beg = Bean.getFirstRowNumber(pageFormName + tagExpFilesDetail);
	String l_exp_file_det_page_end = Bean.getLastRowNumber(pageFormName + tagExpFilesDetail);

	String exp_file_detail_find 	= Bean.getDecodeParam(parameters.get("exp_file_detail_find"));
	exp_file_detail_find 	= Bean.checkFindString(pageFormName + tagExpFileDetailFind, exp_file_detail_find, l_exp_file_det_page);


%>

<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_CLEARING_INFO")) { %>
			    <%= Bean.getMenuButton("DELETE", "../crm/finance/clearingupdate.jsp?id_clearing=" + id + "&type=header&action=remove&process=yes", Bean.clearingXML.getfieldTransl("h_clearing_remove", false), clearing.getValue("ID_CLEARING") + " - " + clearing.getValue("NUMBER_CLEARING")) %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_LINES")){ %>
				<%= Bean.getReportHyperLink("CLEARING_REP1", "ID_CLEARING=" + id + "&ID_OWNER=" + id_owner_filtr) %>

			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLines, "../crm/finance/clearingspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_CLEARING_LINES")+"&", "lines_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_POSTINGS")){ %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPostings, "../crm/finance/clearingspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_CLEARING_POSTINGS")+"&", "postings_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_RECONCILATION")){ %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReconcile, "../crm/finance/clearingspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_CLEARING_RECONCILATION")+"&", "reconcile_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_EXPFILES")){ %>

				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_CLEARING_EXPFILES")) { %>
					<%= Bean.getMenuButton("EXPORT", "../crm/finance/clearingupdate.jsp?type=header&id_clearing=" + id + "&action=export&process=no", Bean.buttonXML.getfieldTransl("delete", false), clearing.getValue("ID_CLEARING") + " - " + clearing.getValue("NUMBER_CLEARING")) %>
				<% } %>	
			    <!-- Вывод страниц -->
				<% if ("FILE".equalsIgnoreCase(detail_type)) { %>
					<% if ((id_file==null || "".equalsIgnoreCase(id_file))) { %>
						<%= Bean.getPagesHTML(pageFormName + tagExpFiles, "../crm/finance/clearingspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_CLEARING_EXPFILES")+"&detail_type="+detail_type+"&", "exp_files_page") %>
					<% } else {%>
						<td>&nbsp;</td>
					<% } %>
				<% } else if ("LOG".equalsIgnoreCase(detail_type)) { %>
					<%= Bean.getPagesHTML(pageFormName + tagLog, "../crm/finance/clearingspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_CLEARING_EXPFILES")+"&detail_type="+detail_type+"&", "log_page") %>
				<% } %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(clearing.getValue("ID_CLEARING") + ": " + clearing.getValue("NUMBER_CLEARING") + " - " + clearing.getValue("DATE_CLEARING_FRMT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/clearingspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
	
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_INFO")) { 
   boolean hasEditPermission = 	Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_CLEARING_INFO");
 %>
	<script>
		var formData = new Array (
			new Array ('number_clearing', 'varchar2', 1),
			new Array ('time_clearing', 'varchar2', 1)
		);
	</script>
	<% if (hasEditPermission) { %>
		<form action="../crm/finance/clearingupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	    <input type="hidden" name="type" value="header">
	    <input type="hidden" name="action" value="edit">
	    <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id_clearing" value="<%= clearing.getValue("ID_CLEARING") %>">
	<% } %>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("id_clearing", false) %> </td><td><input type="text" name="id_clearing" size="16" value="<%= clearing.getValue("ID_CLEARING") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(clearing.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(clearing.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("date_clearing", false) %> </td><td><input type="text" name="date_clearing" size="16" value="<%= clearing.getValue("DATE_CLEARING_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clearingXML.getfieldTransl("begin_period", false) %> </td><td><input type="text" name="begin_period" size="16" value="<%= clearing.getValue("BEGIN_PERIOD_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<% if (hasEditPermission) { %>
			<td><%= Bean.clearingXML.getfieldTransl("number_clearing", true) %></td> <td><input type="text" name="number_clearing" size="16" value="<%= clearing.getValue("NUMBER_CLEARING") %>" class="inputfield"> </td>
			<% } else { %>
			<td><%= Bean.clearingXML.getfieldTransl("number_clearing", false) %></td> <td><input type="text" name="number_clearing" size="16" value="<%= clearing.getValue("NUMBER_CLEARING") %>" readonly="readonly" class="inputfield-ro"> </td>
			<% } %>
			<td><%= Bean.clearingXML.getfieldTransl("end_period", false) %> </td><td><input type="text" name="end_period" size="16" value="<%= clearing.getValue("END_PERIOD_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("state_clearing", true)%></td> <td><select name="state_clearing" class="inputfield"><%=Bean.getMeaningFromLookupNameOrderByNymberValueOptions("CLEARING_STATE", clearing.getValue("STATE_CLEARING"), true)%></select> </td>
			<td><%= Bean.clearingXML.getfieldTransl("line_count", false) %></td> <td><input type="text" name="line_count" size="25" value="<%= clearing.getValue("LINE_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("reconcile_state", false) %> </td><td><input type="text" name="reconcile_state" size="30" value="<%= clearing.getValue("RECONCILE_STATE_CLEARING_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clearingXML.getfieldTransl("posting_count", false) %></td> <td><input type="text" name="posting_count" size="25" value="<%= clearing.getValue("POSTING_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("reconciled_line_count", false) %></td> <td><input type="text" name="reconciled_line_count" size="16" value="<%= clearing.getValue("RECONCILED_LINE_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				clearing.getValue(Bean.getCreationDateFieldName()),
				clearing.getValue("CREATED_BY"),
				clearing.getValue(Bean.getLastUpdateDateFieldName()),
				clearing.getValue("LAST_UPDATE_BY")
			) %>
		<% if (hasEditPermission) { %>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/clearingupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/clearing.jsp") %>
			</td>
		</tr>
		<% } else {%>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getGoBackButton("../crm/finance/clearing.jsp") %>
			</td>
		</tr>
		<% } %>
	</table>
	</form>
	<br>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_LINES")) { %>
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
		<%= Bean.getFindHTML("line_find", line_find, "../crm/finance/clearingspecs.jsp?id=" + id + "&lines_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("id_owner_filtr", "../crm/finance/clearingspecs.jsp?id=" + id + "&lines_page=1", Bean.clearingXML.getfieldTransl("h_owner", false)) %>
			<%= Bean.getClearingBankAccountOwnerOptions(id, id_owner_filtr, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	
	<%= clearing.getClearingLinesHTML(line_find, id_owner_filtr, Bean.currentMenu.getTabID("FINANCE_CLEARING_POSTINGS"),  l_lines_page_beg, l_lines_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_POSTINGS")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("posting_find", posting_find, "../crm/finance/clearingspecs.jsp?id=" + id + "&postings_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= clearing.getClearingPostingsHTML("", posting_find, l_postings_page_beg, l_postings_page_end) %>
<%} %>
	
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_RECONCILATION")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("reconcile_find", reconcile_find, "../crm/finance/clearingspecs.jsp?id=" + id + "&reconcile_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table> 
	<%= clearing.getBankStatementReconcileLinesHTML(reconcile_find, l_reconcile_page_beg, l_reconcile_page_end) %> 
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_EXPFILES")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<% if ("FILE".equalsIgnoreCase(detail_type)) { %>
			<% if ((id_file==null || "".equalsIgnoreCase(id_file))) { %>
			<%= Bean.getFindHTML("exp_file_find", exp_file_find, "../crm/finance/clearingspecs.jsp?id=" + id + "&exp_files_page=1") %>
			<% } else { %>
			<td>&nbsp;</td>
			<% } %>
		<% } else if ("LOG".equalsIgnoreCase(detail_type)) { %>
			<%= Bean.getFindHTML("log_find", log_find, "../crm/finance/clearingspecs.jsp?id=" + id + "&log_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("log_row_type", "../crm/finance/clearingspecs.jsp?id=" + id + "&log_page=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
				<%= Bean.getSelectOptionHTML(log_row_type, "0", "") %>
				<%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", log_row_type, false) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		<% } %>

	<%=Bean.getSelectOnChangeBeginHTML("detail_type", "../crm/finance/clearingspecs.jsp?id=" + id + "&export_page=1", Bean.clearingXML.getfieldTransl("h_owner", false)) %>
		<%= Bean.getSelectOptionHTML(detail_type,"FILE",Bean.clearingXML.getfieldTransl("t_export_files", false)) %>
		<%= Bean.getSelectOptionHTML(detail_type,"LOG",Bean.clearingXML.getfieldTransl("t_log", false)) %>
	<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	
	<% if ("FILE".equalsIgnoreCase(detail_type)) { %>
		<%= clearing.getClearingFilesHTML(id_file, exp_file_find, l_exp_files_page_beg, l_exp_files_page_end) %>
		<% if (!(id_file==null || "".equalsIgnoreCase(id_file))) { %>
		
		<br>
		<br>
		<table <%=Bean.getTableBottomFilter() %>>
		  	<tr>
			<%= Bean.getFindHTML("exp_file_detail_find", exp_file_detail_find, "../crm/finance/clearingspecs.jsp?id=" + id + "&exp_file_det_page=1") %>
	
			<%= Bean.getPagesHTML(pageFormName + tagExpFilesDetail, "../crm/finance/clearingspecs.jsp?id=" + id + "&id_file=" + id_file + "&tab="+Bean.currentMenu.getTabID("FINANCE_CLEARING_EXPFILES")+"&detail_type="+detail_type+"&", "exp_file_det_page") %>
		  	</tr>
		</table>
			<%= clearing.getClearingFilesDetailHTML(id_file, exp_file_detail_find, l_exp_file_det_page_beg, l_exp_file_det_page_end) %>
		<% } %>
	<% } else if ("LOG".equalsIgnoreCase(detail_type)) { %>
		<% bcSysLogObject log = new bcSysLogObject(); %>
		<%= log.getSysLogHTML(log_find, "", "'CLEARING_EXPORT'", id, "", "", "", "", "", l_log_page_beg, l_log_page_end) %>
	<% } %>

<%} %>

 <% 
} %>
</div></div>
</body>
</html>
