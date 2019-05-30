<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.reports.bcReports"%>
<%@page import="bc.objects.bcFNPostingSchemeLineObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcFNPostingSchemeObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%=Bean.getMetaContent() %>
	<%=Bean.getBottomFrameCSS() %>

</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_POSTING_SCHEME";

Bean.setJspPageForTabName(pageFormName);

String tagClubRel = "_CLUB_REL";
String tagClubRelFind = "_CLUB_REL_FIND";
String tagReport = "_REPORTS";
String tagReportDetail = "_REPORT_DETAIL";
String tagFind = "_FIND_GENERAL";
String tagFindDet = "_FIND_DET";
String tagStateLine = "_STATE_LINE";

String id = Bean.getDecodeParam(parameters.get("id"));

String id_report = Bean.getDecodeParam(parameters.get("id_report"));

if (id==null || "".equalsIgnoreCase(id)) {
	id = "";
}

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }


if ("".equalsIgnoreCase(id)) { 
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else { 
	Bean.tabsHmSetValue(pageFormName, tab);
	bcFNPostingSchemeLineObject line = new bcFNPostingSchemeLineObject(id);
	
	bcFNPostingSchemeObject scheme = new bcFNPostingSchemeObject(line.getValue("ID_BK_OPERATION_SCHEME"));

	Bean.currentMenu.setExistFlag("FINANCE_POSTING_SCHEME_REPORTS",true);
	Bean.currentMenu.setExistFlag("FINANCE_POSTING_SCHEME_RELATIONSHIPS",true);
	Bean.currentMenu.setExistFlag("FINANCE_POSTING_SCHEME_LINES",false);
	if (Bean.currentMenu.isCurrentTab("FINANCE_POSTING_SCHEME_LINES")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}

	//Обрабатываем номера страниц
	String l_club_rel_page = Bean.getDecodeParam(parameters.get("club_rel_page"));
	Bean.pageCheck(pageFormName + tagClubRel, l_club_rel_page);
	String l_club_rel_page_beg = Bean.getFirstRowNumber(pageFormName + tagClubRel);
	String l_club_rel_page_end = Bean.getLastRowNumber(pageFormName + tagClubRel);

	String club_rel_find = Bean.getDecodeParam(parameters.get("club_rel_find"));
	club_rel_find	 	= Bean.checkFindString(pageFormName + tagClubRelFind, club_rel_find, l_club_rel_page);

	String rel_kind = Bean.getDecodeParam(parameters.get("rel_kind"));
	if (rel_kind==null || "".equalsIgnoreCase(rel_kind)) {
		rel_kind = "CREATED";
	}
	
	String l_report_page = Bean.getDecodeParam(parameters.get("report_page"));
	Bean.pageCheck(pageFormName + tagReport, l_report_page);
	String l_report_page_beg = Bean.getFirstRowNumber(pageFormName + tagReport);
	String l_report_page_end = Bean.getLastRowNumber(pageFormName + tagReport);

	String report_find = Bean.getDecodeParam(parameters.get("report_find"));
	report_find	 	= Bean.checkFindString(pageFormName + tagFind, report_find, l_report_page);

	String l_report_det_page = Bean.getDecodeParam(parameters.get("report_det_page"));

	if (id_report==null || "".equalsIgnoreCase(id_report)) {
		l_report_det_page = "1";
	}
	Bean.pageCheck(pageFormName + tagReportDetail, l_report_det_page);
	String l_report_det_page_beg = Bean.getFirstRowNumber(pageFormName + tagReportDetail);
	String l_report_det_page_end = Bean.getLastRowNumber(pageFormName + tagReportDetail);

	String find_det 			= Bean.getDecodeParam(parameters.get("find_det"));
	String state_line			= Bean.getDecodeParam(parameters.get("state_line"));

	find_det	 	= Bean.checkFindString(pageFormName + tagFindDet, find_det, l_report_det_page);
	state_line		= Bean.checkFindString(pageFormName + tagStateLine, state_line, l_report_det_page);

%>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTING_SCHEME_INFO")) { %>
				<%= Bean.getReportHyperLink("SOSR02", "ID_BK_OPERATION_SCHEME_LINE=" + id) %>
			<% } %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_POSTING_SCHEME_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/finance/posting_scheme_lineupdate.jsp?id=" + id + "&id_scheme=" + line.getValue("ID_BK_OPERATION_SCHEME") + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/posting_scheme_lineupdate.jsp?id=" + id + "&id_scheme=" + line.getValue("ID_BK_OPERATION_SCHEME") + "&type=general&action=remove&process=yes", Bean.posting_schemeXML.getfieldTransl("h_delete_line", false), line.getValue("OPER_NUMBER") + " - " + line.getValue("NAME_BK_OPERATION_TYPE")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTING_SCHEME_RELATIONSHIPS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagClubRel, "../crm/finance/posting_scheme_linespecs.jsp?id=" +id + "&tab="+Bean.currentMenu.getTabID("FINANCE_POSTING_SCHEME_RELATIONSHIPS")+"&", "club_rel_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTING_SCHEME_REPORTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_POSTING_SCHEME_REPORTS")) { %>
					<%= Bean.getMenuButton("CHECK", "../crm/finance/posting_scheme_lineupdate.jsp?id=" + id + "&type=general&action=check&process=yes", Bean.posting_schemeXML.getfieldTransl("h_check", false), id) %>
				<% } %>

			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReport, "../crm/finance/posting_scheme_linespecs.jsp?id=" +id + "&tab="+Bean.currentMenu.getTabID("FINANCE_POSTINGS_REPORTS")+"&", "report_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<td>
				<center>
					<div class="div_caption2" onclick="ajaxpage('../crm/finance/posting_schemespecs.jsp?id=<%=line.getValue("ID_BK_OPERATION_SCHEME") %>','div_main')"> 
					<%= scheme.getValue("ID_FN_POSTING_SCHEME") + ": " + scheme.getValue("DESC_FN_POSTING_SCHEME") %>
					</div>
				</center>
			</td>
		</tr>
		<%= Bean.getDetailCaption(line.getValue("OPER_NUMBER") + " - " + line.getValue("NAME_BK_OPERATION_TYPE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/posting_scheme_linespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("FINANCE_POSTING_SCHEME_INFO")) {%> 
    <script>
    	var formAll = new Array ();
		var formData = new Array (
			new Array ('debet_name_bk_account_scheme_line', 'varchar2', 1),
	   		new Array ('credit_name_bk_account_scheme_line', 'varchar2', 1),
			new Array ('oper_number', 'varchar2', 1),
			new Array ('amount', 'varchar2', 1)
		);
		var formDataAddit = new Array (
			new Array ('name_related_bk_oper_scheme_line', 'varchar2', 1)
		);
		var formDataClearing = new Array (
			new Array ('receiver_cd_bk_bank_accnt_type', 'varchar2', 1),
			new Array ('payer_cd_bk_bank_accnt_type', 'varchar2', 1)
		);
		var formExport = new Array (
			new Array ('cd_bk_doc_type', 'varchar2', 1)
		);
		function myValidateForm() {
			var phase = document.getElementById('cd_bk_phase').value;
			var using = document.getElementById('using_in_clearing').value;
			var run_exp = document.getElementById('run_postings_export').value;

			formAll = formAll.concat(formData);
			if (phase == 'AFTER_MONEY_TRANSFER') {
				formAll = formAll.concat(formDataAddit);
			}
			if (using == 'Y') {
				formAll = formAll.concat(formDataClearing);
			}
			if (run_exp == 'Y') {
				formAll = formAll.concat(formExport);
			}
			return validateForm(formAll);
		}

		function checkPhase(){
			var phase = document.getElementById('cd_bk_phase').value;
			if (phase == 'AFTER_MONEY_TRANSFER') {
				document.getElementById('rel_scheme').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("id_related_bk_oper_scheme_line", true) %>';
			} else {
				document.getElementById('rel_scheme').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("id_related_bk_oper_scheme_line", false) %>';
			}
		}

		function checkBKExport(){
			var run_exp = document.getElementById('run_postings_export').value;
			if (run_exp == 'Y') {
				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", true) %>';
			} else {
				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) %>';
			}
		}

		function checkClearing(){
			var using = document.getElementById('using_in_clearing').value;
			if (using == 'Y') {
				document.getElementById('span_receiver_cd_bk_bank_accnt_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("receiver_cd_bk_bank_accnt_type", true) %>';
				document.getElementById('span_payer_cd_bk_bank_accnt_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("payer_cd_bk_bank_accnt_type", true) %>';
			} else {
				document.getElementById('span_receiver_cd_bk_bank_accnt_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("receiver_cd_bk_bank_accnt_type", false) %>';
				document.getElementById('span_payer_cd_bk_bank_accnt_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("payer_cd_bk_bank_accnt_type", false) %>';
			}
		}

		var debetPart = '<select name=\"debet_participant_type\"  class=\"inputfield\"><%= Bean.getBKOperationTypeAccountParticipantOptions("REMITTANCE_CARD_CARD", line.getValue("DEBET_CD_BK_ACCOUNT_PART"), true) %></select>';
		var creditPart = '<select name=\"credit_participant_type\"  class=\"inputfield\"><%= Bean.getBKOperationTypeAccountParticipantOptions("REMITTANCE_CARD_CARD", line.getValue("CREDIT_CD_BK_ACCOUNT_PART"), true) %></select>';
		 
		function setParticipant(bkOperScheme) {
			var deb = document.getElementById('debet_part');
			var cred = document.getElementById('credit_part');
			if (bkOperScheme == 'REMITTANCE_CARD_CARD') {
				deb.innerHTML = debetPart;
				cred.innerHTML = creditPart;
			} else {
				deb.innerHTML = '';
				cred.innerHTML = '';
			}
		}
		<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTING_SCHEME_INFO")) {%> 
			checkPhase();
			checkClearing();
			checkBKExport();
			dwr_get_bk_bank_acc_type(document.getElementById('cd_club_rel_type'),'<%=Bean.getSessionId()%>'); 
		<% } %>
	</script>
	<form action="../crm/finance/posting_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%= id %>">
	
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.relationshipXML.getfieldTransl("general", false) %></td> <td  colspan="3"><select name="cd_club_rel_type" id="cd_club_rel_type" class="inputfield" onchange="dwr_get_bk_bank_acc_type(this,'<%=Bean.getSessionId()%>')"><%= Bean.getClubRelTypeOptions(line.getValue("CD_CLUB_REL_TYPE"), false) %></select></td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("exist_flag_tsl", false) %></td> <td valign=top><select name="exist_flag"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("EXIST_FLAG"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("name_bk_operation_type", false) %></td> 
			<td colspan="5">
				<select name="cd_bk_operation_type" id="cd_bk_operation_type" class="inputfield"><%= Bean.getBKOperationTypeShortOptions(line.getValue("CD_BK_OPERATION_TYPE"), false) %></select>
			</td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("cd_bk_phase", false) %></td> <td  colspan="3"><select name="cd_bk_phase" id="cd_bk_phase" onchange="checkPhase();" class="inputfield"><%= Bean.getBKPhaseListOptions(line.getValue("CD_BK_PHASE"), false) %></select></td>
			<td colspan="2" class="top_line"><b><%= Bean.posting_schemeXML.getfieldTransl("h_additional_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("oper_number", true) %></td> <td  colspan="3"><input type="text" name="oper_number" size="20" value="<%=line.getValue("OPER_NUMBER") %>" class="inputfield"> </td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("id_payment_system", false) %></td> <td  colspan="3"><select name="id_payment_system"  class="inputfield"><%= Bean.getPaymentSystemOptions(line.getValue("ID_PAYMENT_SYSTEM"), true) %></select></td>
		</tr>
		<tr>
			<td valign=top rowspan="4"><%= Bean.posting_schemeXML.getfieldTransl("oper_content", false) %></td><td valign=top  colspan="3" rowspan="4"><textarea name="oper_content" cols="58" rows="5" class="inputfield"><%= line.getValue("OPER_CONTENT") %></textarea></td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("card_owner", false) %></td> <td><select name="card_owner"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("BK_OPER_CARD_OWNER", line.getValue("CARD_OWNER"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("is_vat_payer", false) %></td> <td><select name="is_vat_payer"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("IS_VAT_PAYER"), true) %></select></td>
		</tr>
		<tr>
			<td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>
			<td valign=top class="top_line"><%= Bean.posting_schemeXML.getfieldTransl("run_postings_export", false) %></td> <td valign=top class="top_line"><select name="run_postings_export" id="run_postings_export" class="inputfield" onchange="checkBKExport()"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("RUN_POSTINGS_EXPORT"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("debet_name_bk_account", true) %>
				<%= Bean.getGoToFinanceBKSchemeLink(line.getValue("DEBET_ID_BK_ACCOUNT_SH_LINE")) %>
			</td> 
			<td  colspan="3">
				<%=Bean.getWindowBKAccountSchemeLine("debet_", line.getValue("DEBET_ID_BK_ACCOUNT_SH_LINE"), line.getValue("DEBET_CD_BK_ACCOUNT_SH_LINE") + " - " + line.getValue("DEBET_NAME_BK_ACCOUNT_SH_LINE"), scheme.getValue("ID_BK_ACCOUNT_SCHEME"), "40") %>
			<br><%= Bean.posting_schemeXML.getfieldTransl("h_participant_type", false) %>&nbsp;<span id="debet_part"></span>
			</td> 
			<td valign=top><span id="span_cd_bk_doc_type"><%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) %></span></td> <td valign=top><select name="cd_bk_doc_type"  class="inputfield"><%= Bean.getBKDocTypeOptions(line.getValue("CD_BK_DOC_TYPE"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("credit_name_bk_account", true) %>
				<%= Bean.getGoToFinanceBKSchemeLink(line.getValue("CREDIT_ID_BK_ACCOUNT_SH_LINE")) %>
			</td> 
			<td  colspan="3">
				<%=Bean.getWindowBKAccountSchemeLine("credit_", line.getValue("CREDIT_ID_BK_ACCOUNT_SH_LINE"), line.getValue("CREDIT_CD_BK_ACCOUNT_SH_LINE") + " - " + line.getValue("CREDIT_NAME_BK_ACCOUNT_SH_LINE"), scheme.getValue("ID_BK_ACCOUNT_SCHEME"), "40") %>
			<br><%= Bean.posting_schemeXML.getfieldTransl("h_participant_type", false) %>&nbsp;<span id="credit_part"></span>
			</td>
			<td class="top_line"><%= Bean.posting_schemeXML.getfieldTransl("zero_amounts_allowed", false) %> </td><td class="top_line"><select name="zero_amounts_allowed" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("ZERO_AMOUNTS_ALLOWED"), false) %></select></td>
		</tr>
		<tr>
			<td><span id="rel_scheme"></span>
				<%= Bean.getGoToFinancePostingSchemeLink(line.getValue("ID_RELATED_BK_OPER_SCHEME_LINE")) %>
			</td> 
			<td  colspan="3">
				<%=Bean.getWindowFindBKOperationScheme("related_bk_oper_scheme_line", line.getValue("ID_RELATED_BK_OPER_SCHEME_LINE"), "40") %>
			</td>
			<td valign=top>&nbsp;</td> <td valign=top>&nbsp;</td>
		</tr>
		<tr>
			<td valign=top rowspan="4"><%= Bean.posting_schemeXML.getfieldTransl("amount", true) %></td> 
			<td valign=top  colspan="3" rowspan="4">
				<%=Bean.getWindowCalculator("amount", id, id, line.getValue("AMOUNT"), "Y", "N", "50", "5") %>
			</td>
			<td colspan="2" class="top_line"><b><%= Bean.posting_schemeXML.getfieldTransl("h_clearing_param", false) %></b></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("using_in_clearing", false) %></td> <td valign=top><select name="using_in_clearing" id="using_in_clearing" onchange="checkClearing();" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("USING_IN_CLEARING"), false) %></select></td>
		</tr>
		<tr>
			<td valign=top><span id="span_receiver_cd_bk_bank_accnt_type"><%= Bean.posting_schemeXML.getfieldTransl("receiver_cd_bk_bank_accnt_type", false) %></span></td> <td valign=top><select name="receiver_cd_bk_bank_accnt_type" id="receiver_cd_bk_bank_accnt_type" class="inputfield"><option value="<%= line.getValue("RECEIVER_CD_BK_BANK_ACCNT_TYPE") %>"></select></td>
		</tr>
		<tr>
			<td valign=top><span id="span_payer_cd_bk_bank_accnt_type"><%= Bean.posting_schemeXML.getfieldTransl("payer_cd_bk_bank_accnt_type", false) %></span></td> <td valign=top><select name="payer_cd_bk_bank_accnt_type" id="payer_cd_bk_bank_accnt_type" class="inputfield"><option value="<%= line.getValue("PAYER_CD_BK_BANK_ACCNT_TYPE") %>"></select></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("assignment_posting", false) %></td> 
			   <td valign=top  colspan="3">
				<%=Bean.getWindowCalculator("assignment_posting", id, id, line.getValue("ASSIGNMENT_POSTING"), "N", "Y", "50", "3") %>
			</td>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("payment_function_default", false) %></td> 
			   <td valign=top>
				<%=Bean.getWindowCalculator("payment_function", id, id, line.getValue("PAYMENT_FUNCTION"), "N", "Y", "50", "3") %>
			</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				"8",
				line.getValue(Bean.getCreationDateFieldName()),
				"3",
				line.getValue("CREATED_BY"),
				"3",
				line.getValue(Bean.getLastUpdateDateFieldName()),
				"3",
				line.getValue("LAST_UPDATE_BY"),
				"3"
			) %>
		<tr>
			<td colspan="10" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/posting_scheme_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/posting_scheme_linespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</form>
<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("FINANCE_POSTING_SCHEME_INFO")) {%> 
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.relationshipXML.getfieldTransl("general", false) %></td> <td  colspan="3"><input type="text" name="name_club_rel_type" size="58" value="<%=line.getValue("NAME_CLUB_REL_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("exist_flag_tsl", false) %></td> <td  colspan="3"><input type="text" name="exist_flag_tsl" size="20" value="<%=line.getValue("EXIST_FLAG_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("name_bk_operation_type", false) %></td> <td  colspan="3"><input type="text" name="name_bk_operation_type" size="58" value="<%=line.getValue("NAME_BK_OPERATION_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("cd_bk_phase", false) %></td> <td  colspan="3"><input type="text" name="name_bk_phase" size="58" value="<%=line.getValue("NAME_BK_PHASE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2" class="top_line"><b><%= Bean.posting_schemeXML.getfieldTransl("h_additional_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("oper_number", false) %></td> <td  colspan="3"><input type="text" name="oper_number" size="20" value="<%=line.getValue("OPER_NUMBER") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("id_payment_system", false) %></td> <td  colspan="3"><input type="text" name="name_payment_system" size="40" value="<%=line.getValue("NAME_PAYMENT_SYSTEM") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign=top rowspan="4"><%= Bean.posting_schemeXML.getfieldTransl("oper_content", false) %></td><td valign=top  colspan="3" rowspan="4"><textarea name="oper_content" cols="54" rows="5" readonly="readonly" class="inputfield-ro"><%= line.getValue("OPER_CONTENT") %></textarea></td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("card_owner", false) %></td> <td  colspan="3"><input type="text" name="card_owner" size="40" value="<%= Bean.getMeaningFoCodeValue("BK_OPER_CARD_OWNER", line.getValue("CARD_OWNER")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("is_vat_payer", false) %></td> <td  colspan="3"><input type="text" name="is_vat_payer" size="40" value="<%= Bean.getMeaningFoCodeValue("YES_NO", line.getValue("IS_VAT_PAYER")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td> <td  colspan="3">&nbsp;</td>
		</tr>
		<tr>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("run_postings_export", false) %></td> <td valign=top><input type="text" name="run_postings_export" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", line.getValue("RUN_POSTINGS_EXPORT")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("debet_name_bk_account", false) %>
				<%= Bean.getGoToFinanceBKSchemeLink(line.getValue("DEBET_ID_BK_ACCOUNT_SH_LINE")) %>
			</td> 
			<td  colspan="3">
				<input type="text" name="debet_name_bk_account_sh_line" size="58" value="<%=line.getValue("DEBET_CD_BK_ACCOUNT_SH_LINE")%> - <%=line.getValue("DEBET_NAME_BK_ACCOUNT_SH_LINE") %>" readonly="readonly" class="inputfield-ro">
				<br><%= Bean.posting_schemeXML.getfieldTransl("h_participant_type", false) %>&nbsp;<input type="text" name="oper_number" size="35" value="<%=Bean.getBKOperationTypeAccountParticipantName(line.getValue("DEBET_CD_BK_ACCOUNT_PART")) %>" readonly="readonly" class="inputfield-ro">
			</td> 
			<td><%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) %></td> <td  colspan="3"><input type="text" name="name_bk_doc_type" size="20" value="<%= Bean.getBKDocTypeName(line.getValue("CD_BK_DOC_TYPE")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("credit_name_bk_account", false) %>
				<%= Bean.getGoToFinanceBKSchemeLink(line.getValue("CREDIT_ID_BK_ACCOUNT_SH_LINE")) %>
			</td> 
			<td  colspan="3">
				<input type="text" name="credit_name_bk_account_sh_line" size="58" value="<%=line.getValue("CREDIT_CD_BK_ACCOUNT_SH_LINE")%> - <%=line.getValue("CREDIT_NAME_BK_ACCOUNT_SH_LINE") %>" readonly="readonly" class="inputfield-ro">
				<br><%= Bean.posting_schemeXML.getfieldTransl("h_participant_type", false) %>&nbsp;<input type="text" name="oper_number" size="35" value="<%=Bean.getBKOperationTypeAccountParticipantName(line.getValue("CREDIT_CD_BK_ACCOUNT_PART")) %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("zero_amounts_allowed", false) %></td> <td  colspan="3"><input type="text" name="zero_amounts_allowed" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", line.getValue("ZERO_AMOUNTS_ALLOWED")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("id_related_bk_oper_scheme_line", false) %>
				<%= Bean.getGoToFinancePostingSchemeLink(line.getValue("ID_RELATED_BK_OPER_SCHEME_LINE")) %>
			</td> 
			<td  colspan="3">
				<input type="text" name="name_related_bk_oper_scheme_line" id="name_related_bk_oper_scheme_line" size="58" value="<%=Bean.getBKOperationSchemeLineAdditionalName(line.getValue("ID_RELATED_BK_OPER_SCHEME_LINE")) %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td valign=top>&nbsp;</td> <td valign=top>&nbsp;</td>
		</tr>
		<tr>
			<td valign=top rowspan="4"><%= Bean.posting_schemeXML.getfieldTransl("amount", false) %></td> 
			<td valign=top  colspan="3" rowspan="4">
				<textarea name="amount" cols="54" rows="4" readonly="readonly" class="inputfield-ro"><%=line.getValue("AMOUNT") %></textarea>
			</td>
			<td colspan="2" class="top_line"><b><%= Bean.posting_schemeXML.getfieldTransl("h_clearing_param", false) %></b></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("using_in_clearing", false) %></td> <td valign=top><input type="text" name="using_in_clearing" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", line.getValue("USING_IN_CLEARING")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("receiver_cd_bk_bank_accnt_type", false) %></td> <td valign=top><input type="text" name="receiver_cd_bk_bank_accnt_type" size="54" value="<%= Bean.getBKBankAccountTypeName(line.getValue("RECEIVER_CD_BK_BANK_ACCNT_TYPE")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("payer_cd_bk_bank_accnt_type", false) %></td> <td valign=top><input type="text" name="payer_cd_bk_bank_accnt_type" size="54" value="<%= Bean.getBKBankAccountTypeName(line.getValue("PAYER_CD_BK_BANK_ACCNT_TYPE")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("assignment_posting", false) %></td> 
			   <td valign=top  colspan="3">
				<textarea name="assignment_posting" cols="54" rows="3" readonly="readonly" class="inputfield-ro"><%=line.getValue("ASSIGNMENT_POSTING") %></textarea>
			</td>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("payment_function_default", false) %></td> 
			   <td valign=top  colspan="3">
				<textarea name="payment_function" cols="50" rows="3" readonly="readonly" class="inputfield-ro"><%=line.getValue("PAYMENT_FUNCTION") %></textarea>
			</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				"8",
				line.getValue(Bean.getCreationDateFieldName()),
				"3",
				line.getValue("CREATED_BY"),
				"3",
				line.getValue(Bean.getLastUpdateDateFieldName()),
				"3",
				line.getValue("LAST_UPDATE_BY"),
				"3"
			) %>

		<tr>
			<td colspan="10" align="center">
				<%=Bean.getGoBackButton("../crm/finance/posting_scheme_linespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>

<%   } 

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTING_SCHEME_REPORTS")) { 
    bcReports report = new bcReports(Bean.getReportFormat());
  %>
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("report_find", report_find, "../crm/finance/posting_schemespecs.jsp?id=" + id + "&report_page=1&") %>

 			<td>&nbsp;</td>
		</tr>
	</table>
	<%= report.getBKOperSchemeCheckReportHTML(id, id_report, report_find, l_report_page_beg, l_report_page_end, "N") %>
	<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
		<br>
		<br>
		<table  <%=Bean.getTableBottomFilter() %>>
			<tr>
				<%= Bean.getFindHTML("find_det", find_det, "../crm/finance/posting_scheme_linespecs.jsp?id=" + id + "&id_report="+id_report+"&report_det_page=1&") %>

				<%=Bean.getSelectOnChangeBeginHTML("state_line", "../crm/finance/posting_scheme_linespecs.jsp?id=" + id + "&id_report="+id_report+"&report_det_page=1", Bean.reportXML.getfieldTransl("state_line", false)) %>
					<%= Bean.getMeaningFromLookupNameOptions("REPORT_LINE_STATE", state_line, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>

				<%= Bean.getPagesHTML(pageFormName + tagReportDetail, "../crm/finance/posting_scheme_linespecs.jsp?id=" + id + "&id_report="+id_report+"&", "report_det_page") %>
			</tr>
		</table>
		<%= report.getCheckModelReportDetailHTML(id_report, find_det, state_line, l_report_det_page_beg, l_report_det_page_end) %>
	<% } %>
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_POSTING_SCHEME_RELATIONSHIPS")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("club_rel_find", club_rel_find, "../crm/finance/posting_schemespecs.jsp?id=" + id + "&club_rel_page=1&") %>

			<td align="right">
			<% String needComission = line.getRelationShipsNeededCount(line.getValue("CD_CLUB_REL_TYPE"));
			  
			  if (!(needComission.equalsIgnoreCase("0"))) {
				  %>
					<b><font color=red><%= Bean.relationshipXML.getfieldTransl("need_relationships_count", false) %> -  <%=needComission %></font></b><br>
				  <%
			  } else {
				  %>
					<b><font color=green><%= Bean.relationshipXML.getfieldTransl("all_relationships_was_created", false) %></font></b><br>
				  <%
			  }
			%>
			</td>
			<td align="right">
			<%=Bean.getSelectOnChangeBeginHTML("rel_kind", "../crm/finance/posting_scheme_linespecs.jsp?id=" + id + "&relation_page=1", "") %>
				<%= Bean.getSelectOptionHTML(rel_kind, "CREATED", Bean.relationshipXML.getfieldTransl("h_created", false)) %>
				<%= Bean.getSelectOptionHTML(rel_kind, "NEEDED", Bean.relationshipXML.getfieldTransl("h_needed", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>&nbsp;
		</td>
	</tr>
	</table>
	<% if ("CREATED".equalsIgnoreCase(rel_kind)) { %>
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
	function CheckAll(Element) {
		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			
			if (myName.substr(0,6) == 'chb_id'){
					thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
			}
		}
	}
	CheckCB();
</script>

		<%= line.getClubRelationshipsHTML(l_club_rel_page_beg, l_club_rel_page_end) %>
	<% } else { %>
		<%= line.getRelationShipsNeededHTML(line.getValue("CD_CLUB_REL_TYPE"), l_club_rel_page_beg, l_club_rel_page_end) %>
	<% } %>
<% }

}%>

</div></div>
</body>
</html>
