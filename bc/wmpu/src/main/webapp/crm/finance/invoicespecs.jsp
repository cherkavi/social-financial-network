<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcFNInvoiceObject"%>
<%@page import="java.util.HashMap"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CRM_FINANCE_INVOICE";
String tagContent = "_CONTENT";
String tagContentFind = "_CONTENT_FIND";
String tagTrans = "_TRANS";
String tagTransFind = "_TRANS_FIND";
String tagTransType = "_TRANS_TYPE";
String tagTransState = "_TRANS_STATE";
String tagTransPayType = "_TRANS_PAY_TYPE";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
String sementFull = "";
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcFNInvoiceObject invoice = new bcFNInvoiceObject(id);
	
	//Обрабатываем номера страниц
	String l_content_page = Bean.getDecodeParam(parameters.get("content_page"));
	Bean.pageCheck(pageFormName + tagContent, l_content_page);
	String l_content_page_beg = Bean.getFirstRowNumber(pageFormName + tagContent);
	String l_content_page_end = Bean.getLastRowNumber(pageFormName + tagContent);

	String content_find	 	= Bean.getDecodeParam(parameters.get("content_find"));
	content_find 				= Bean.checkFindString(pageFormName + tagContentFind, content_find, l_content_page);

	
	String l_trans_page = Bean.getDecodeParam(parameters.get("trans_page"));
	Bean.pageCheck(pageFormName + tagTrans, l_trans_page);
	String l_trans_page_beg = Bean.getFirstRowNumber(pageFormName + tagTrans);
	String l_trans_page_end = Bean.getLastRowNumber(pageFormName + tagTrans);

	String trans_find 	= Bean.getDecodeParam(parameters.get("trans_find"));
	trans_find 	= Bean.checkFindString(pageFormName + tagTransFind, trans_find, l_trans_page);

	String trans_type 	= Bean.getDecodeParam(parameters.get("trans_type"));
	trans_type 	= Bean.checkFindString(pageFormName + tagTransType, trans_type, l_trans_page);

	String trans_state 	= Bean.getDecodeParam(parameters.get("trans_state"));
	trans_state 	= Bean.checkFindString(pageFormName + tagTransState, trans_state, l_trans_page);

	String trans_pay_type 	= Bean.getDecodeParam(parameters.get("trans_pay_type"));
	trans_pay_type 	= Bean.checkFindString(pageFormName + tagTransPayType, trans_pay_type, l_trans_page);
	
	String titleAccount = (Bean.isEmpty(invoice.getValue("NUMBER_INVOICE"))
			?Bean.clearingXML.getfieldTransl("title_invoice_from_wn", false)
			:invoice.getValue("NUMBER_INVOICE") + " " + Bean.clearingXML.getfieldTransl("title_invoice_from", false))
		 + " " + invoice.getValue("DATE_INVOICE_DF");

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CRM_FINANCE_INVOICE_INFO")) { %>
				<%= Bean.getMenuButtonBase("ADD", "../crm/finance/invoiceupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "", Bean.clearingXML.getfieldTransl("h_add_invoice", false), "div_main") %>
				<%= Bean.getReportHyperLink("CRMREP501", "ID_INVOICE="+id) %>
				<%= Bean.getMenuButtonBase("RUN", "../crm/finance/invoiceupdate.jsp?id=" + id + "&type=general&action=create2&process=no", "", "", Bean.clearingXML.getfieldTransl("h_invoice_actions", false), "div_main") %>
				<%= Bean.getMenuButton("DELETE", "../crm/finance/invoiceupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.clearingXML.getfieldTransl("h_delete_invoice", false), titleAccount, Bean.clearingXML.getfieldTransl("h_delete_invoice", false)) %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_FINANCE_INVOICE_CONTENT")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagContent, "../crm/finance/invoicespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CRM_FINANCE_INVOICE_CONTENT")+"&", "content_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_FINANCE_INVOICE_TRANSACTION")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTrans, "../crm/finance/invoicespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CRM_FINANCE_INVOICE_TRANSACTION")+"&", "trans_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(titleAccount) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/invoicespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndEditPermitted("CRM_FINANCE_INVOICE_INFO")) {
	%>
	<script>
		var formData = new Array (
			new Array ('date_invoice', 'varchar2', 1),
			new Array ('name_jur_prs_payer', 'varchar2', 1),
			new Array ('name_jur_prs_receiver', 'varchar2', 1),
			new Array ('cd_currency', 'varchar2', 1),
			new Array ('cd_fn_priority', 'varchar2', 1),
			new Array ('name_bank_account_receiver', 'varchar2', 1),
			new Array ('cd_fn_invoice_state', 'varchar2', 1)
		);
		function myValidateForm(){
			return validateForm(formData);
		}
			
	</script>

    <form action="../crm/finance/invoiceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%= id %>">
        <input type="hidden" name="LUD" value="<%= invoice.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("number_invoice", false) %> </td><td><input type="text" name="number_invoice" size="20" value="<%= invoice.getValue("NUMBER_INVOICE") %>" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(invoice.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(invoice.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("date_invoice", true) %></td> <td><%=Bean.getCalendarInputField("date_invoice", invoice.getValue("DATE_INVOICE_DF"), "10") %></td>
			<td><%= Bean.clearingXML.getfieldTransl("name_fn_priority", true) %></td> <td><select name="cd_fn_priority" class="inputfield"><%= Bean.getFinancePriorityOptions(invoice.getValue("CD_FN_PRIORITY"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("sname_jur_prs_receiver", true) %>
				<%=Bean.getGoToJurPrsHyperLink(invoice.getValue("ID_JUR_PRS_RECEIVER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs_receiver", invoice.getValue("ID_JUR_PRS_RECEIVER"), invoice.getValue("SNAME_JUR_PRS_RECEIVER"), "ALL", "50") %>
			</td>			
			<td><%= Bean.clearingXML.getfieldTransl("name_fn_invoice_module", false) %></td> <td><input type="text" name="name_fn_invoice_module" size="30" value="<%= invoice.getValue("NAME_FN_INVOICE_MODULE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("sname_jur_prs_payer", true) %>
				<%=Bean.getGoToJurPrsHyperLink(invoice.getValue("ID_JUR_PRS_PAYER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs_payer", invoice.getValue("ID_JUR_PRS_PAYER"), invoice.getValue("SNAME_JUR_PRS_PAYER"), "ALL", "50") %>
			</td>			
			<td><%= Bean.clearingXML.getfieldTransl("name_fn_invoice_creation", false) %></td> <td><input type="text" name="name_fn_invoice_creation" size="30" value="<%= invoice.getValue("NAME_FN_INVOICE_CREATION") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("invoice_doc", false) %>
				<%=Bean.getGoToDocLink(invoice.getValue("ID_DOC")) %>
			</td>
			<td>
				<%=Bean.getWindowDocuments("doc", invoice.getValue("ID_DOC"), "50") %>
			</td>			
			<td colspan="2"><b><i><%= Bean.clearingXML.getfieldTransl("title_invoice_period", false) %></i></b></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("cd_currency", true) %></td> <td><select name="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions(invoice.getValue("CD_CURRENCY"), true) %></select></td>
			<td><%= Bean.clearingXML.getfieldTransl("begin_period_date", false) %></td> <td><%=Bean.getCalendarInputField("begin_period_date", invoice.getValue("BEGIN_PERIOD_DATE_DF"), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.clearingXML.getfieldTransl("end_period_date", false) %></td> <td><%=Bean.getCalendarInputField("end_period_date", invoice.getValue("END_PERIOD_DATE_DF"), "10") %></td>
		</tr>
		<tr><td colspan="4" class="top_line_gray"><%= Bean.clearingXML.getfieldTransl("title_payment_details", false) %></td></tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("bank_account_receiver", true) %>
				<%=Bean.getGoToBankAccountLink(invoice.getValue("ID_BANK_ACCOUNT_RECEIVER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindBankAccountJurPrs("bank_account_receiver", invoice.getValue("ID_BANK_ACCOUNT_RECEIVER"), "'+document.getElementById('id_jur_prs_receiver').value+'", "50") %>
			</td>			
			<td><%= Bean.clearingXML.getfieldTransl("name_fn_invoice_state", true) %></td> <td><select name="cd_fn_invoice_state" class="inputfield"><%= Bean.getFinanceInvoiceStateOptions(invoice.getValue("CD_FN_INVOICE_STATE"), true) %></select></td>
		</tr>
		<tr>
			<td><b><%= Bean.clearingXML.getfieldTransl("total_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></b></td><td><input type="text" name="total_sum" size="20" value="<%= invoice.getValue("TOTAL_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clearingXML.getfieldTransl("paid_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="paid_sum" size="20" value="<%= invoice.getValue("PAID_SUM_FRMT") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="tax_sum" size="20" value="<%= invoice.getValue("TAX_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><b><%= Bean.clearingXML.getfieldTransl("debt_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></b></td><td><input type="text" name="debt_sum" size="20" value="<%= invoice.getValue("DEBT_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("total_without_tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="total_without_tax_sum" size="20" value="<%= invoice.getValue("TOTAL_WITHOUT_TAX_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clearingXML.getfieldTransl("overpayment_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="overpayment_sum" size="20" value="<%= invoice.getValue("OVERPAYMENT_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				invoice.getValue("ID_INVOICE"),
				invoice.getValue(Bean.getCreationDateFieldName()),
				invoice.getValue("CREATED_BY"),
				invoice.getValue(Bean.getLastUpdateDateFieldName()),
				invoice.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/invoiceupdate.jsp", "submit", "updateForm", "div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/invoice.jsp") %>
			</td>
		</tr>

	</table>

	</form>
	<%= Bean.getCalendarScript("date_invoice", false) %>
	<%= Bean.getCalendarScript("begin_period_date", false) %>
	<%= Bean.getCalendarScript("end_period_date", false) %>

<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CRM_FINANCE_INVOICE_INFO")) {

 %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("number_invoice", false) %> </td><td><input type="text" name="number_invoice" size="20" value="<%= invoice.getValue("NUMBER_INVOICE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(invoice.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(invoice.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("date_invoice", false) %></td> <td><input type="text" name="date_invoice" size="20" value="<%= invoice.getValue("DATE_INVOICE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clearingXML.getfieldTransl("name_fn_priority", false) %></td> <td><input type="text" name="name_fn_priority" size="30" value="<%= invoice.getValue("NAME_FN_PRIORITY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("sname_jur_prs_receiver", false) %>
				<%=Bean.getGoToJurPrsHyperLink(invoice.getValue("ID_JUR_PRS_RECEIVER")) %>
			</td>
			<td><input type="text" name="sname_jur_prs_receiver" size="50" value="<%= invoice.getValue("SNAME_JUR_PRS_RECEIVER") %>" title="<%= invoice.getValue("SNAME_JUR_PRS_RECEIVER") %>" readonly="readonly" class="inputfield-ro"></td>			
			<td><%= Bean.clearingXML.getfieldTransl("cd_currency", false) %></td> <td><input type="text" name="name_currency" size="30" value="<%= invoice.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("sname_jur_prs_payer", false) %>
				<%=Bean.getGoToJurPrsHyperLink(invoice.getValue("ID_JUR_PRS_PAYER")) %>
			</td>
			<td><input type="text" name="sname_jur_prs_payer" size="50" value="<%= invoice.getValue("SNAME_JUR_PRS_PAYER") %>" title="<%= invoice.getValue("SNAME_JUR_PRS_PAYER") %>" readonly="readonly" class="inputfield-ro"></td>			
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
		<tr><td colspan="4" class="top_line_gray"><%= Bean.clearingXML.getfieldTransl("title_payment_details", false) %></td></tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("bank_account_receiver", false) %>
				<%=Bean.getGoToBankAccountLink(invoice.getValue("ID_BANK_ACCOUNT_RECEIVER")) %>
			</td>
			<td><input type="text" name="id_bank_account_receiver" size="50" value="<%= invoice.getValue("BANK_ACCOUNT_RECEIVER") %>" title="<%= invoice.getValue("BANK_ACCOUNT_RECEIVER") %>" readonly="readonly" class="inputfield-ro"></td>			
			<td><%= Bean.clearingXML.getfieldTransl("name_fn_invoice_state", false) %></td> <td><input type="text" name="name_fn_invoice_state" size="30" value="<%= invoice.getValue("NAME_FN_INVOICE_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
		<tr><td colspan="4" class="top_line_gray"><%= Bean.clearingXML.getfieldTransl("title_total_amounts", false) %></td></tr>
		<tr>
			<td><b><%= Bean.clearingXML.getfieldTransl("total_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></b></td><td><input type="text" name="total_sum" size="20" value="<%= invoice.getValue("TOTAL_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clearingXML.getfieldTransl("paid_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="paid_sum" size="20" value="<%= invoice.getValue("PAID_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="tax_sum" size="20" value="<%= invoice.getValue("TAX_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><b><%= Bean.clearingXML.getfieldTransl("debt_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></b></td><td><input type="text" name="debt_sum" size="20" value="<%= invoice.getValue("DEBT_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("total_without_tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="total_without_tax_sum" size="20" value="<%= invoice.getValue("TOTAL_WITHOUT_TAX_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clearingXML.getfieldTransl("overpayment_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="overpayment_sum" size="20" value="<%= invoice.getValue("OVERPAYMENT_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				invoice.getValue("ID_INVOICE"),
				invoice.getValue(Bean.getCreationDateFieldName()),
				invoice.getValue("CREATED_BY"),
				invoice.getValue(Bean.getLastUpdateDateFieldName()),
				invoice.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/finance/invoice.jsp") %>
			</td>
		</tr>
	</table>
	</form>
    
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_FINANCE_INVOICE_INFO")) {

   %>
	<div id="div_invoice_lines">
	<form action="../crm/finance/invoiceupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableBottomFilter() %>>
		<tr><td colspan="10"><b><%= Bean.clearingXML.getfieldTransl("h_invoice_lines", false) %></b></td></tr>
		<tr>
		<%= Bean.getFindHTML("content_find", content_find, "../crm/finance/invoicespecs.jsp?id=" + id + "&content_page=1&") %>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CRM_FINANCE_INVOICE_INFO")) { %>
			<%= Bean.getMenuButtonBase("ADD", "../crm/finance/invoiceupdate.jsp?id=" + id + "&type=line&action=add&process=no", "", "", Bean.clearingXML.getfieldTransl("h_add_invoice_line", false), "div_data_detail") %>
		<% } %>
		<td style="width:10px;">&nbsp;</td>
		<!-- Вывод страниц -->
		<%= Bean.getPagesHTML(pageFormName + tagContent, "../crm/finance/invoicespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CRM_FINANCE_INVOICE_INFO")+"&", "content_page") %>

		</tr>
	</table>
	</form>
	</div>
	<%= invoice.getContentHTML(content_find, l_content_page_beg, l_content_page_end) %>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CRM_FINANCE_INVOICE_TRANSACTION")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("trans_find", trans_find, "../crm/finance/invoicespecs.jsp?id=" + id + "&trans_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("trans_type", "../crm/finance/invoicespecs.jsp?id=" + id + "&trans_page=1", Bean.transactionXML.getfieldTransl("type_trans", false)) %>
			<%= Bean.getTransTypeOptions(trans_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("trans_pay_type", "../crm/finance/invoicespecs.jsp?id=" + id + "&trans_page=1", Bean.transactionXML.getfieldTransl("pay_type", false)) %>
			<%= Bean.getTransPayTypeOptions(trans_pay_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("trans_state", "../crm/finance/invoicespecs.jsp?id=" + id + "&trans_page=1", Bean.transactionXML.getfieldTransl("state_trans", false)) %>
			<%= Bean.getTransStateOptions(trans_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= invoice.getTransactionsHTML(trans_find, trans_type, trans_pay_type, trans_state, l_trans_page_beg, l_trans_page_end) %>
<% } %>

<% } %>
</div></div>
</body></html>
