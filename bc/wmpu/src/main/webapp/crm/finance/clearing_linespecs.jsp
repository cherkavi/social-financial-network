<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClearingLineObject"%><html>

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

String tagPostings = "_LINE_POSTINGS";
String tagPostingFind = "_LINE_POSTING_FIND";
String tagReconcile = "_LINE_RECONCILE";
String tagReconcileFind = "_LINE_RECONCILE_FIND";

String id = Bean.getDecodeParam(parameters.get("id"));
if (id==null || "".equalsIgnoreCase(id.trim())) { id = ""; }
String	tab = Bean.getDecodeParam(parameters.get("tab"));

bcClearingLineObject clearing_line = null;

if (!(id==null || "".equalsIgnoreCase(id))) {
	clearing_line = new bcClearingLineObject(id);
}

if ("".equalsIgnoreCase(clearing_line.getValue("ID_CLEARING"))) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	if (tab==null || "".equalsIgnoreCase(tab)) { 
		tab = Bean.tabsHmGetValue(pageFormName);
	}
	Bean.tabsHmSetValue(pageFormName, tab);

	Bean.currentMenu.setExistFlag("FINANCE_CLEARING_INFO", true);
	Bean.currentMenu.setExistFlag("FINANCE_CLEARING_LINES", false);
	Bean.currentMenu.setExistFlag("FINANCE_CLEARING_POSTINGS", true);
	Bean.currentMenu.setExistFlag("FINANCE_CLEARING_EXPFILES", false);
	
	if (Bean.currentMenu.isCurrentTab("FINANCE_CLEARING_LINES") ||
			Bean.currentMenu.isCurrentTab("FINANCE_CLEARING_EXPFILES")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}

	//Обрабатываем номера страниц
	String l_postings_page = Bean.getDecodeParam(parameters.get("postings_page"));
	Bean.pageCheck(pageFormName + tagPostings, l_postings_page);
	String l_postings_page_beg = Bean.getFirstRowNumber(pageFormName + tagPostings);
	String l_postings_page_end = Bean.getLastRowNumber(pageFormName + tagPostings);

	String posting_find 	= Bean.getDecodeParam(parameters.get("posting_find"));
	posting_find 	= Bean.checkFindString(pageFormName + tagPostingFind, posting_find, l_postings_page);

	String l_reconcile_page = Bean.getDecodeParam(parameters.get("reconcile_page"));
	Bean.pageCheck(pageFormName + tagReconcile, l_reconcile_page);
	String l_reconcile_page_beg = Bean.getFirstRowNumber(pageFormName + tagReconcile);
	String l_reconcile_page_end = Bean.getLastRowNumber(pageFormName + tagReconcile);

	String reconcile_find 	= Bean.getDecodeParam(parameters.get("reconcile_find"));
	reconcile_find 	= Bean.checkFindString(pageFormName + tagReconcileFind, reconcile_find, l_reconcile_page);
	
%>

<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_POSTINGS")){ %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPostings, "../crm/finance/clearing_linespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_CLEARING_POSTINGS")+"&", "postings_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_RECONCILATION")){ %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReconcile, "../crm/finance/clearing_linespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("FINANCE_CLEARING_RECONCILATION")+"&", "reconcile_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(clearing_line.getValue("ID_CLEARING_LINE") + " - " + clearing_line.getValue("NUMBER_DOC_CLEARING")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/finance/clearing_linespecs.jsp?id=" + id) %>
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
			new Array ('state_clearing_line', 'varchar2', 1)
		);
	</script>
	<% if (hasEditPermission) { %>
		<form action="../crm/finance/clearing_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	    <input type="hidden" name="type" value="set_state">
	    <input type="hidden" name="action" value="update">
	    <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= clearing_line.getValue("ID_CLEARING_LINE") %>">
		<input type="hidden" name="id_clearing" value="<%= clearing_line.getValue("ID_CLEARING") %>">
	<% } else { %>
	<% } %>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("id_clearing_line", false) %> </td><td><input type="text" name="id_clearing_line" size="16" value="<%= clearing_line.getValue("ID_CLEARING_LINE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clearingXML.getfieldTransl("name_currency", false)%></td> <td><input type="text" name="name_currency" size="16" value="<%= clearing_line.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("number_clearing", false) %>
				<%= Bean.getGoToFinanceClearingLink(clearing_line.getValue("ID_CLEARING")) %>
			</td> <td><input type="text" name="number_clearing" size="16" value="<%= clearing_line.getValue("NUMBER_CLEARING") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clearingXML.getfieldTransl("entered_amount", false)%></td> <td><input type="text" name="entered_amount" size="16" value="<%= clearing_line.getValue("ENTERED_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("number_doc_clearing", false)%></td> <td><input type="text" name="number_doc_clearing" size="16" value="<%= clearing_line.getValue("NUMBER_DOC_CLEARING") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" class="top_line"><b><%= Bean.clearingXML.getfieldTransl("RECEIVER_PARTICIPANT", false) %></b></td>
			<td colspan="2" class="top_line"><b><%= Bean.clearingXML.getfieldTransl("PAYER_PARTICIPANT", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("receiver_mfo_bank", false) %></td> <td><input type="text" name="receiver_mfo_bank" size="20" value="<%= clearing_line.getValue("RECEIVER_MFO_BANK") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clearingXML.getfieldTransl("payer_mfo_bank", false) %></td> <td><input type="text" name="payer_mfo_bank" size="20" value="<%= clearing_line.getValue("PAYER_MFO_BANK") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("receiver_name_bank_alt", false) %>
				<%= Bean.getGoToJurPrsHyperLink(clearing_line.getValue("RECEIVER_ID_BANK")) %>
			</td> <td><input type="text" name="receiver_name_bank_alt" size="40" value="<%= clearing_line.getValue("RECEIVER_SNAME_BANK") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clearingXML.getfieldTransl("payer_name_bank_alt", false) %>
				<%= Bean.getGoToJurPrsHyperLink(clearing_line.getValue("PAYER_ID_BANK")) %>
			</td> <td><input type="text" name="payer_name_bank_alt" size="40" value="<%= clearing_line.getValue("PAYER_SNAME_BANK") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("receiver_number_bank_account", false) %>
				<%= Bean.getGoToBankAccountLink(clearing_line.getValue("RECEIVER_ID_BANK_ACCOUNT")) %>
			</td> <td><input type="text" name="receiver_number_bank_account" size="40" value="<%= clearing_line.getValue("RECEIVER_NUMBER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clearingXML.getfieldTransl("payer_number_bank_account", false) %>
				<%= Bean.getGoToBankAccountLink(clearing_line.getValue("PAYER_ID_BANK_ACCOUNT")) %>
			</td> <td><input type="text" name="payer_number_bank_account" size="40" value="<%= clearing_line.getValue("PAYER_NUMBER_BANK_ACCOUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("receiver_sname_owner_ba", false) %>
			<% if ("JUR_PRS".equalsIgnoreCase(clearing_line.getValue("RECEIVER_TYPE_OWNER_BA"))) { %>
				<%= Bean.getGoToJurPrsHyperLink(clearing_line.getValue("RECEIVER_ID_OWNER_BA")) %>
			<% } else { %>
				<%= Bean.getGoToNatPrsLink(clearing_line.getValue("RECEIVER_ID_OWNER_BA")) %>
			<% } %>
			</td> <td><input type="text" name="receiver_sname_owner_ba" size="40" value="<%= clearing_line.getValue("RECEIVER_SNAME_OWNER_BA") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clearingXML.getfieldTransl("payer_sname_owner_ba", false) %>
			<% if ("JUR_PRS".equalsIgnoreCase(clearing_line.getValue("RECEIVER_TYPE_OWNER_BA"))) { %>
				<%= Bean.getGoToJurPrsHyperLink(clearing_line.getValue("PAYER_ID_OWNER_BA")) %>
			<% } else { %>
				<%= Bean.getGoToNatPrsLink(clearing_line.getValue("PAYER_ID_OWNER_BA")) %>
			<% } %>
			</td> <td><input type="text" name="payer_sname_owner_ba" size="40" value="<%= clearing_line.getValue("PAYER_SNAME_OWNER_BA") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.clearingXML.getfieldTransl("payment_function", false) %></td><td  colspan="3" class="top_line"><textarea name="payment_function" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= clearing_line.getValue("PAYMENT_FUNCTION") %></textarea></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.clearingXML.getfieldTransl("h_export", false) %> </b></td>
		</tr>
		<% if (hasEditPermission) { %>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("need_clearing_line_export", false)%></td> <td><select name="need_clearing_line_export" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("YES_NO", clearing_line.getValue("NEED_CLEARING_LINE_EXPORT"), true)%></select></td>
			<td><%= Bean.clearingXML.getfieldTransl("state_clearing_line_export", false) %></td> <td><input type="text" name="state_clearing_line_export" size="16" value="<%= clearing_line.getValue("STATE_CLEARING_LINE_EXPORT_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<% } else { %>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("need_clearing_line_export", false) %></td> <td><input type="text" name="need_clearing_line_export" size="25" value="<%= Bean.getMeaningFoCodeValue("YES_NO", clearing_line.getValue("NEED_CLEARING_LINE_EXPORT")) %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clearingXML.getfieldTransl("state_clearing_line_export", false) %></td> <td><input type="text" name="state_clearing_line_export" size="16" value="<%= clearing_line.getValue("STATE_CLEARING_LINE_EXPORT_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<% } %>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.clearingXML.getfieldTransl("h_reconcile", false) %> </b></td>
		</tr>
		<tr>
			<td><%= Bean.clearingXML.getfieldTransl("reconcile_state_tsl", false) %></td> <td><input type="text" name="reconcile_state" size="25" value="<%= clearing_line.getValue("RECONCILE_STATE_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clearingXML.getfieldTransl("unreconciled_amount", false) %></td> <td><input type="text" name="unreconciled_amount" size="16" value="<%= clearing_line.getValue("UNRECONCILED_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				clearing_line.getValue(Bean.getCreationDateFieldName()),
				clearing_line.getValue("CREATED_BY"),
				clearing_line.getValue(Bean.getLastUpdateDateFieldName()),
				clearing_line.getValue("LAST_UPDATE_BY")
			) %>
		<% if (hasEditPermission) { %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/clearing_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/clearing.jsp") %>
			</td>
		</tr>
		<% } else {%>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/finance/clearing.jsp") %>
			</td>
		</tr>
		<% } %>
	</table>
	</form>

	<br>
<% } %>


<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_POSTINGS")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("posting_find", posting_find, "../crm/finance/clearing_linespecs.jsp?id=" + id + "&postings_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= clearing_line.getPostingsHTML(posting_find, l_postings_page_beg, l_postings_page_end) %>
<%} %>
	
<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("FINANCE_CLEARING_RECONCILATION")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("reconcile_find", reconcile_find, "../crm/finance/clearing_linespecs.jsp?id=" + id + "&reconcile_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table> 
	<%= clearing_line.getBankStatementReconcileLinesHTML(reconcile_find, l_reconcile_page_beg, l_reconcile_page_end) %> 
<%} %>

 <% 
} %>
</div></div>
</body>
</html>
