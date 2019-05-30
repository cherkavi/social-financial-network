<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcTelegramObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcSysLogObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body>
<div id="div_tabsheet">

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_TERMSES";

String tagTransaction = "_TELEGRAM_TRANSACTION";
String tagTransactionFind = "_TELEGRAM_FIND_TRANS";
String tagTransactionType = "_TELEGRAM_TYPE_TRANS";
String tagTransactionState = "_TELEGRAM_TRANS_STATE";
String tagTransactionPayType = "_TELEGRAM_TRANS_PAY_TYPE";
String tagSrcType = "_TELEGRAM_SRC_TYPE";

String tagPostings = "_TELEGRAM_POSTINGS";
String tagPostingsFind = "_TELEGRAM_POSTINGS_FIND";
String tagPostingsBKOper = "_TELEGRAM_POSTINGS_BK_OPER";

String tagLog = "_TELEGRAM_LOG";
String tagLogFind = "_TELEGRAM_LOG_FIND";
String tagLogRowType = "_TELEGRAM_ID_TELGR_LOG_ROWTYPE";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcTelegramObject telegram = new bcTelegramObject(id);
	telegram.getFeature();
	if (telegram.getValue("ID_TELGR") == null || "".equalsIgnoreCase(telegram.getValue("ID_TELGR"))) {%>
		<%=Bean.getIDNotFoundMessage() %>
	<%} else {
		
	String src_type = Bean.getDecodeParam(parameters.get("src_type"));
	Bean.filtersHmSetValue(pageFormName + tagSrcType, src_type);
	
	Bean.currentMenu.setExistFlag("CLIENTS_TERMSES_TELGR", false);
	if (Bean.currentMenu.isCurrentTab("CLIENTS_TERMSES_TELGR")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}

	//Обрабатываем номера страниц
	String l_transaction_page = Bean.getDecodeParam(parameters.get("transaction_page"));
	Bean.pageCheck(pageFormName + tagTransaction, l_transaction_page);
	String l_transaction_page_beg = Bean.getFirstRowNumber(pageFormName + tagTransaction);
	String l_transaction_page_end = Bean.getLastRowNumber(pageFormName + tagTransaction);
	
	String transaction_find 	= Bean.getDecodeParam(parameters.get("transaction_find"));
	transaction_find 	= Bean.checkFindString(pageFormName + tagTransactionFind, transaction_find, l_transaction_page);
	
	String transaction_type 	= Bean.getDecodeParam(parameters.get("transaction_type"));
	transaction_type 	= Bean.checkFindString(pageFormName + tagTransactionType, transaction_type, l_transaction_page);

	String transaction_state 	= Bean.getDecodeParam(parameters.get("transaction_state"));
	transaction_state 	= Bean.checkFindString(pageFormName + tagTransactionState, transaction_state, l_transaction_page);

	String transaction_pay_type 	= Bean.getDecodeParam(parameters.get("transaction_pay_type"));
	transaction_pay_type 	= Bean.checkFindString(pageFormName + tagTransactionPayType, transaction_pay_type, l_transaction_page);
	
	String l_postings_page = Bean.getDecodeParam(parameters.get("postings_page"));
	Bean.pageCheck(pageFormName + tagPostings, l_postings_page);
	String l_postings_beg = Bean.getFirstRowNumber(pageFormName + tagPostings);
	String l_postings_end = Bean.getLastRowNumber(pageFormName + tagPostings);

	String posting_find 	= Bean.getDecodeParam(parameters.get("posting_find"));
	posting_find 	= Bean.checkFindString(pageFormName + tagPostingsFind, posting_find, l_postings_page);

	String posting_bk_oper 	= Bean.getDecodeParam(parameters.get("posting_bk_oper"));
	posting_bk_oper 	= Bean.checkFindString(pageFormName + tagPostingsBKOper, posting_bk_oper, l_postings_page);
	
	String l_log_page = Bean.getDecodeParam(parameters.get("log_page"));
	Bean.pageCheck(pageFormName + tagLog, l_log_page);
	String l_log_beg = Bean.getFirstRowNumber(pageFormName + tagLog);
	String l_log_end = Bean.getLastRowNumber(pageFormName + tagLog);
	
	String log_find 	= Bean.getDecodeParam(parameters.get("log_find"));
	log_find 	= Bean.checkFindString(pageFormName + tagLogFind, log_find, l_log_page);

	String l_log_row_type = Bean.getDecodeParam(parameters.get("log_row_type"));
	if ("0".equalsIgnoreCase(l_log_row_type)) {
		l_log_row_type = "";
	}
	l_log_row_type 	= Bean.checkFindString(pageFormName + tagLogRowType, l_log_row_type, l_log_page);

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader(Bean.telegramXML.getfieldTransl("general", false), "../crm/clients/telegrams.jsp") %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_TRANSACTIONS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTransaction, "../crm/clients/telegramspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMSES_TRANSACTIONS")+"&", "transaction_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_POSTINGS_TRANS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMSES_POSTINGS_TRANS")) { %>
				    <%= Bean.getMenuButton("DELETE_ALL", "../crm/clients/telegramupdate.jsp?id=" + id + "&type=posting&action=removeall&process=yes", Bean.postingXML.getfieldTransl("LAB_DELETE_ALL_POSTINGS", false), "") %>
				    <%= Bean.getMenuButton("POSTING", "../crm/clients/telegramupdate.jsp?id=" + id + "&type=posting&action=run&process=yes", Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS", false), "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPostings, "../crm/clients/telegramspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMSES_POSTINGS_TRANS")+"&", "postings_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_SYS_LOG")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLog, "../crm/clients/telegramspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMSES_SYS_LOG")+"&", "log_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(Bean.telegramXML.getfieldTransl("id_telgr", false) + " - " + id) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/telegramspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_INFO")) { %>
 	<form>
	<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.telegramXML.getfieldTransl("id_term_ses", false) %>
				<%= Bean.getGoToTermSessionLink(telegram.getValue("ID_TERM_SES")) %>
			</td> <td><input type="text" name="id_term_ses" size="20" value="<%=telegram.getValue("ID_TERM_SES") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.telegramXML.getfieldTransl("id_term", false) %>
				<%= Bean.getGoToTerminalLink(telegram.getValue("ID_TERM")) %>
			</td> 
			<td><input type="text" name="id_term" size="20" value="<%= telegram.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro" title="ID_TERM"></td>
		</tr>

		<tr>
			<td><%= Bean.telegramXML.getfieldTransl("id_telgr", false) %></td> <td><input type="text" name="id_telgr" size="20" value="<%=telegram.getValue("ID_TELGR") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.telegramXML.getfieldTransl("ID_SAM", false) %>
				<%= Bean.getGoToSAMLink(telegram.getValue("ID_SAM")) %>
			</td> <td><input type="text" name="id_sam" size="20" value="<%=telegram.getValue("ID_SAM") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.telegramXML.getfieldTransl("cd_telgr_type", false) %></td> <td><input type="text" name="cd_telgr_type" size="20" value="<%=telegram.getValue("CD_TELGR_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.telegramXML.getfieldTransl("nt_sam", false) %></td> <td><input type="text" name="nt_sam" size="20" value="<%=telegram.getValue("NT_SAM") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.telegramXML.getfieldTransl("date_telgr", false) %></td> <td><input type="text" name="date_telgr" size="20" value="<%=telegram.getValue("DATE_TELGR_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.telegramXML.getfieldTransl("mac_pda", false) %></td> <td><input type="text" name="mac_pda" size="20" value="<%=telegram.getValue("MAC_PDA") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		<%
			String wayTelgr = "RECEIVED".equalsIgnoreCase(telegram.getValue("WAY_TELGR"))
							?Bean.telegramXML.getfieldTransl("way_telgr_received", false)
							:Bean.telegramXML.getfieldTransl("way_telgr_sent", false);
		
		%>
			<td><%= Bean.telegramXML.getfieldTransl("tel_identifier", false) %></td> <td><input type="text" name="tel_identifier" size="20" value="<%=telegram.getValue("TEL_IDENTIFIER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.telegramXML.getfieldTransl("nt_msg_b", false) %></td> <td><input type="text" name="nt_msg_b" size="20" value="<%=telegram.getValue("NT_MSG_B") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.telegramXML.getfieldTransl("tel_length", false) %></td> <td><input type="text" name="tel_length" size="20" value="<%=telegram.getValue("TEL_LENGTH") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.telegramXML.getfieldTransl("tel_version", false) %></td> <td><input type="text" name="tel_version" size="20" value="<%=telegram.getValue("TEL_VERSION") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.telegramXML.getfieldTransl("way_telgr", false) %></td> <td><input type="text" name="way_telgr" size="20" value="<%=wayTelgr %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.telegramXML.getfieldTransl("vk_enc", false) %></td> <td><input type="text" name="vk_enc" size="20" value="<%=telegram.getValue("VK_ENC") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.telegramXML.getfieldTransl("outher_sid", false) %></td> <td><input type="text" name="outher_sid" size="20" value="<%=telegram.getValue("OUTHER_SID") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.telegramXML.getfieldTransl("cd_telgr_state", false) %></td> <td><input type="text" name="cd_telgr_state" size="20" value="<%=telegram.getValue("NAME_TELGR_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>


		<tr>
			<td class="top_line"><b><%= Bean.telegramXML.getfieldTransl("nc", false) %></b></td> <td class="top_line"><input type="text" name="nc" size="20" value="<%=telegram.getValue("NC") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line"><b><%= Bean.telegramXML.getfieldTransl("ni", false) %></b></td> <td class="top_line"><input type="text" name="ni" size="20" value="<%=telegram.getValue("NI") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_payment", false),telegram.getValue("N_PAYMENT")) %></td> <td><input type="text" name="n_payment" size="20" value="<%=telegram.getValue("N_PAYMENT") %>" readonly="readonly" class="inputfield-ro" title="TC_P='0001'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_mtf", false),telegram.getValue("N_MTF")) %></td> <td><input type="text" name="n_mtf" size="20" value="<%=telegram.getValue("N_MTF") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00012'"></td>
		</tr>
		<tr>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_mov_bon", false),telegram.getValue("N_MOV_BON")) %></td> <td><input type="text" name="n_mov_bon" size="20" value="<%=telegram.getValue("N_MOV_BON") %>" readonly="readonly" class="inputfield-ro" title="TC_P='0002'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_point_fee", false),telegram.getValue("N_POINT_FEE")) %></td> <td><input type="text" name="n_point_fee" size="20" value="<%=telegram.getValue("N_POINT_FEE") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00013'"></td>
		</tr>
		<tr>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_chk_card", false),telegram.getValue("N_CHK_CARD")) %></td> <td><input type="text" name="n_chk_card" size="20" value="<%=telegram.getValue("N_CHK_CARD") %>" readonly="readonly" class="inputfield-ro" title="TC_P='0003'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_put_card", false),telegram.getValue("N_PUT_CARD")) %></td> <td><input type="text" name="n_put_card" size="20" value="<%=telegram.getValue("N_PUT_CARD") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00014'"></td>
		</tr>
		<tr>
			<td><%=Bean.makeValue( Bean.telegramXML.getfieldTransl("n_inval_card", false),telegram.getValue("N_INVAL_CARD")) %></td> <td><input type="text" name="n_inval_card" size="20" value="<%=telegram.getValue("N_INVAL_CARD") %>" readonly="readonly" class="inputfield-ro" title="TC_P='0004'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_questioning", false),telegram.getValue("N_QUESTIONING")) %></td> <td><input type="text" name="n_questioning" size="20" value="<%=telegram.getValue("N_QUESTIONING") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00015'"></td>
		</tr>
		<tr>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_storno_bon", false),telegram.getValue("N_STORNO_BON")) %></td> <td><input type="text" name="n_storno_bon" size="20" value="<%=telegram.getValue("N_STORNO_BON") %>" readonly="readonly" class="inputfield-ro" title="TC_P='0005'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_share_fee", false),telegram.getValue("N_SHARE_FEE")) %></td> <td><input type="text" name="n_share_fee" size="20" value="<%=telegram.getValue("N_SHARE_FEE") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00017'"></td>
		</tr>
		<tr>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_payment_im", false),telegram.getValue("N_PAYMENT_IM")) %></td> <td><input type="text" name="n_payment_im" size="20" value="<%=telegram.getValue("N_PAYMENT_IM") %>" readonly="readonly" class="inputfield-ro" title="TC_P='0006'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_transfer_put_point", false),telegram.getValue("N_TRANSFER_PUT_POINT")) %></td> <td><input type="text" name="n_transfer_put_point" size="20" value="<%=telegram.getValue("N_TRANSFER_PUT_POINT") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00018'"></td>
		</tr>
		<tr>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_payment_ext", false),telegram.getValue("N_PAYMENT_EXT")) %></td> <td><input type="text" name="n_payment_ext" size="20" value="<%=telegram.getValue("N_PAYMENT_EXT") %>" readonly="readonly" class="inputfield-ro" title="TC_P='0007'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_payment_invoice", false),telegram.getValue("N_PAYMENT_INVOICE")) %></td> <td><input type="text" name="n_payment_invoice" size="20" value="<%=telegram.getValue("N_PAYMENT_INVOICE") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00019'"></td>
		</tr>
		<tr>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_actvation", false),telegram.getValue("N_ACTVATION")) %></td> <td><input type="text" name="n_actvation" size="20" value="<%=telegram.getValue("N_ACTVATION") %>" readonly="readonly" class="inputfield-ro" title="TC_P='0008'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_share_fee_change", false),telegram.getValue("N_SHARE_FEE_CHANGE")) %></td> <td><input type="text" name="n_share_fee_change" size="20" value="<%=telegram.getValue("N_SHARE_FEE_CHANGE") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00020'"></td>
		</tr>
		<tr>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_coupon", false),telegram.getValue("N_COUPON")) %></td> <td><input type="text" name="n_coupon" size="20" value="<%=telegram.getValue("N_COUPON") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00010'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_transform_from_share", false),telegram.getValue("N_TRANSFORM_FROM_SHARE")) %></td> <td><input type="text" name="n_transform_from_share" size="20" value="<%=telegram.getValue("N_TRANSFORM_FROM_SHARE") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00021'"></td>
		</tr>
		<tr>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_membership_fee", false),telegram.getValue("N_MEMBERSHIP_FEE")) %></td> <td><input type="text" name="n_membership_fee" size="20" value="<%=telegram.getValue("N_MEMBERSHIP_FEE") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00011'"></td>
			<td><%= Bean.makeValue(Bean.telegramXML.getfieldTransl("n_transfer_get_point", false),telegram.getValue("N_TRANSFER_GET_POINT")) %></td> <td><input type="text" name="n_transfer_get_point" size="20" value="<%=telegram.getValue("N_TRANSFER_GET_POINT") %>" readonly="readonly" class="inputfield-ro" title="TC_P='00022'"></td>
		</tr>
		<tr>								
			<td valign="top"><%= Bean.telegramXML.getfieldTransl("src_msg", false) %></td><td  colspan="3"><textarea name="src_telgr" cols="120" rows="6" readonly="readonly" class="inputfield-ro"><%= telegram.getTelegramSourceHTML("INPUT", telegram.getValue("ID_TERM_SES")) %></textarea></td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/clients/telegrams.jsp") %>
			</td>
		</tr>

		</table>

	</form>

 <% } 
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_TELGR")) { %>
	<%= telegram.getTelegramParamHTML() %> 
<%
  if (!(src_type==null || "".equalsIgnoreCase(src_type))) {
      %> <%= telegram.getTelegramSourceHTML(src_type, telegram.getValue("ID_TERM_SES")) %> 
<%  }
  }


if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_TRANSACTIONS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("transaction_find", transaction_find, "../crm/clients/telegramspecs.jsp?id=" + id + "&transaction_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("transaction_type", "../crm/clients/telegramspecs.jsp?id=" + id + "&transaction_page=1", Bean.transactionXML.getfieldTransl("type_trans", false)) %>
			<%= Bean.getTransTypeOptions(transaction_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("transaction_pay_type", "../crm/clients/telegramspecs.jsp?id=" + id + "&transaction_page=1", Bean.transactionXML.getfieldTransl("pay_type", false)) %>
			<%= Bean.getTransPayTypeOptions(transaction_pay_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("transaction_state", "../crm/clients/telegramspecs.jsp?id=" + id + "&transaction_page=1", Bean.transactionXML.getfieldTransl("state_trans", false)) %>
			<%= Bean.getTransStateOptions(transaction_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= telegram.getTransactionsHTML(transaction_find, transaction_type, transaction_pay_type, transaction_state, l_transaction_page_beg, l_transaction_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_POSTINGS_TRANS")) {%>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("posting_find", posting_find, "../crm/clients/telegramspecs.jsp?id=" + id + "&postings_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("posting_bk_oper", "../crm/clients/telegramspecs.jsp?id=" + id + "&postings_page=1", "") %>
			<%= Bean.getBKOperationTypeShortOptions(posting_bk_oper, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table>
	<%= telegram.getPostingsHTML(posting_find, posting_bk_oper, l_postings_beg, l_postings_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_SYS_LOG")) {%>
	<% bcSysLogObject log = new bcSysLogObject(); %>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("log_find", log_find, "../crm/clients/telegramspecs.jsp?id=" + id + "&log_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("log_row_type", "../crm/clients/telegramspecs.jsp?id=" + id + "&log_page=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
				<%= Bean.getSelectOptionHTML("", "0", "") %>
				<%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", l_log_row_type, false) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>
	
	<%= log.getSysLogTermInterchangeHTML(log_find, "", "", id, "", l_log_row_type, l_log_beg, l_log_end) %>
<%}

%>
 <%} } %>
</div></div>
</body>
</html>
