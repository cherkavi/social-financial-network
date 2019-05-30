<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcTelegramObject"%>
<%@page import="bc.objects.bcTerminalSessionObject"%>
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

String tagTelegram = "_TERM_SES_TELEGRAM";
String tagTelegramFind = "_TERM_SES_TELEGRAM_FIND";
String tagSrcType = "_TERM_SES_SRC_TYPE";

String tagTransaction = "_TERM_SES_TRANSACTION";
String tagTransactionFind = "_TERM_SES_FIND_TRANS";
String tagTransactionType = "_TERM_SES_TYPE_TRANS";
String tagTransactionState = "_TERM_SES_TRANS_STATE";
String tagTransactionPayType = "_TERM_SES_TRANS_PAY_TYPE";

String tagPostings = "_TERM_SES_POSTINGS";
String tagPostingsFind = "_TERM_SES_POSTINGS_FIND";
String tagPostingsBKOper = "_TERM_SES_POSTINGS_BK_OPER";

String tagLog = "_TERM_SES_LOG";
String tagLogFind = "_TERM_SES_LOG_FIND";
String tagIdTelgrLog = "_TERM_SES_ID_TELGR_LOG";
String tagLogRowType = "_TERM_SES_ID_TELGR_LOG_ROWTYPE";

Bean.setJspPageForTabName(pageFormName);

String termsesid = Bean.getDecodeParam(parameters.get("id"));
String id_telgr = Bean.getDecodeParam(parameters.get("id_telgr"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (termsesid==null || "".equalsIgnoreCase(termsesid)) {
	Bean.filtersHmSetValue(pageFormName + tagIdTelgrLog, "");
	
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcTerminalSessionObject termSession = new bcTerminalSessionObject(termsesid);
	
	Bean.currentMenu.setExistFlag("CLIENTS_TERMSES_TELGR", true);
	
	//Обрабатываем номера страниц
	String l_telegram_page = Bean.getDecodeParam(parameters.get("telegram_page"));
	Bean.pageCheck(pageFormName + tagTelegram, l_telegram_page);
	String l_telegram_page_beg = Bean.getFirstRowNumber(pageFormName + tagTelegram);
	String l_telegram_page_end = Bean.getLastRowNumber(pageFormName + tagTelegram);

	String src_type = Bean.getDecodeParam(parameters.get("src_type"));
	if (src_type==null || "".equalsIgnoreCase(src_type)) {
		if (Bean.filtersHmGetValue(pageFormName + tagSrcType) == null || "".equalsIgnoreCase(Bean.filtersHmGetValue(pageFormName + tagSrcType))) {
			Bean.filtersHmSetValue(pageFormName + tagSrcType, "FORMATTED");
		}
		src_type = Bean.filtersHmGetValue(pageFormName + tagSrcType);
	} else {
		Bean.filtersHmSetValue(pageFormName + tagSrcType, src_type);
	}
	
	String telegram_find 	= Bean.getDecodeParam(parameters.get("telegram_find"));
	telegram_find 	= Bean.checkFindString(pageFormName + tagTelegramFind, telegram_find, l_telegram_page);

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

	String id_telgr_log = Bean.getDecodeParam(parameters.get("id_telgr_log"));
	if ("0".equalsIgnoreCase(id_telgr_log)) {
		id_telgr_log = "";
	}
	id_telgr_log 	= Bean.checkFindString(pageFormName + tagIdTelgrLog, id_telgr_log, l_log_page);
	
	String l_log_row_type = Bean.getDecodeParam(parameters.get("log_row_type"));
	if ("0".equalsIgnoreCase(l_log_row_type)) {
		l_log_row_type = "";
	}
	l_log_row_type 	= Bean.checkFindString(pageFormName + tagLogRowType, l_log_row_type, l_log_page);
	
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader(Bean.term_sesXML.getfieldTransl("general", false), "../crm/clients/termses.jsp") %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_TELGR")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTelegram, "../crm/clients/termsespecs.jsp?id=" + termSession.getValue("ID_TERM_SES") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMSES_TELGR")+"&", "telegram_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_TRANSACTIONS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTransaction, "../crm/clients/termsespecs.jsp?id=" + termSession.getValue("ID_TERM_SES") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMSES_TRANSACTIONS")+"&", "transaction_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_POSTINGS_TRANS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_TERMSES_POSTINGS_TRANS")) { %>
				    <%= Bean.getMenuButton("DELETE_ALL", "../crm/clients/termsesupdate.jsp?id=" + termSession.getValue("ID_TERM_SES") + "&type=posting&action=removeall&process=yes", Bean.postingXML.getfieldTransl("LAB_DELETE_ALL_POSTINGS", false), "") %>
				    <%= Bean.getMenuButton("POSTING", "../crm/clients/termsesupdate.jsp?id=" + termSession.getValue("ID_TERM_SES") + "&type=posting&action=run&process=yes", Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS", false), "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPostings, "../crm/clients/termsespecs.jsp?id=" + termSession.getValue("ID_TERM_SES") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMSES_POSTINGS_TRANS")+"&", "postings_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_SYS_LOG")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLog, "../crm/clients/termsespecs.jsp?id=" + termSession.getValue("ID_TERM_SES") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_TERMSES_SYS_LOG")+"&", "log_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(Bean.term_sesXML.getfieldTransl("id_term_ses", false) + " - " + termSession.getValue("ID_TERM_SES")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/termsespecs.jsp?id=" + termSession.getValue("ID_TERM_SES")) %>
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
			<td><%= Bean.term_sesXML.getfieldTransl("id_term_ses", false) %></td> <td><input type="text" name="id_term_ses" size="25" value="<%=termSession.getValue("ID_TERM_SES") %>" readonly="readonly" class="inputfield-ro"> </td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(termSession.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="40" value="<%= Bean.getClubShortName(termSession.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.term_sesXML.getfieldTransl("date_beg_frmt", false) %></td> <td><input type="text" name="date_beg_frmt" size="25" value="<%=termSession.getValue("DATE_BEG_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.term_sesXML.getfieldTransl("id_term", false) %>
				<%= Bean.getGoToTerminalLink(termSession.getValue("ID_TERM")) %>
			</td> 
			<td><input type="text" name="id_term" size="20" value="<%= termSession.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro" title="ID_TERM"></td>
		</tr>
		<tr>
			<td><%= Bean.term_sesXML.getfieldTransl("date_end_frmt", false) %></td> <td><input type="text" name="date_end_frmt" size="25" value="<%=termSession.getValue("DATE_END_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.term_sesXML.getfieldTransl("sname_service_place", false) %>
				<%=Bean.getGoToServicePlaceLink(termSession.getValue("ID_SERVICE_PLACE")) %>
			</td> <td><input type="text" name="name_service_place" size="40" value="<%=termSession.getValue("SNAME_SERVICE_PLACE") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.term_sesXML.getfieldTransl("need_card_req", false) %></td> <td><input type="text" name="need_card_req" size="25" value="<%=Bean.getTermSesCardReqState(termSession.getValue("NEED_CARD_REQ")) %>" readonly="readonly" class="inputfield-ro" title = "<%=termSession.getValue("NEED_CARD_REQ") %>"></td>
			<td><%= Bean.term_sesXML.getfieldTransl("ID_SAM", false) %>
				<%= Bean.getGoToSAMLink(termSession.getValue("ID_SAM")) %>
			</td> <td><input type="text" name="id_sam" size="20" value="<%=termSession.getValue("ID_SAM") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.term_sesXML.getfieldTransl("need_col_data", false) %></td> <td><input type="text" name="need_col_data" size="25" value="<%=Bean.getTermSesDataState(termSession.getValue("NEED_COL_DATA")) %>" readonly="readonly" class="inputfield-ro" title = "<%=termSession.getValue("NEED_COL_DATA") %>"></td>
			<td><%= Bean.term_sesXML.getfieldTransl("nt_sam_begin", false) %></td> <td><input type="text" name="nt_sam_begin" size="20" value="<%=termSession.getValue("NT_SAM_BEGIN") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.term_sesXML.getfieldTransl("need_tpar_data", false) %></td> <td><input type="text" name="need_tpar_data" size="25" value="<%=Bean.getTermSesParamState(termSession.getValue("NEED_TPAR_DATA")) %>" readonly="readonly" class="inputfield-ro" title = "<%=termSession.getValue("NEED_TPAR_DATA") %>"></td>
			<td><%= Bean.term_sesXML.getfieldTransl("nt_sam_end", false) %></td> <td><input type="text" name="nt_sam_end" size="20" value="<%=termSession.getValue("NT_SAM_END") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.term_sesXML.getfieldTransl("need_term_mon", false) %></td> <td><input type="text" name="need_term_mon" size="25" value="<%=Bean.getTermSesMonState(termSession.getValue("NEED_TERM_MON")) %>" readonly="readonly" class="inputfield-ro" title = "<%=termSession.getValue("NEED_TERM_MON") %>"></td>
			<td><%= Bean.term_sesXML.getfieldTransl("id_last_input_telgr", false) %>
				<%= Bean.getGoToTabMenuHyperLink(
						"CLIENTS_TERMSES_TELGR",
						termSession.getValue("ID_LAST_INPUT_TELGR"),
						"../crm/clients/termsespecs.jsp?id=" + termsesid + "&id_telgr=" + termSession.getValue("ID_LAST_INPUT_TELGR") + "&tab=" + Bean.currentMenu.getTabID("CLIENTS_TERMSES_TELGR")
					) %>
			</td> <td><input type="text" name="id_last_input_telgr" size="40" value="<%=termSession.getValue("NAME_LAST_INPUT_TELGR") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.term_sesXML.getfieldTransl("outher_sid", false) %></td> <td><input type="text" name="outher_sid" size="25" value="<%=termSession.getValue("OUTHER_SID") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.term_sesXML.getfieldTransl("id_last_output_telgr", false) %>
				<%= Bean.getGoToTabMenuHyperLink(
						"CLIENTS_TERMSES_TELGR",
						termSession.getValue("ID_LAST_OUTPUT_TELGR"),
						"../crm/clients/termsespecs.jsp?id=" + termsesid + "&id_telgr=" + termSession.getValue("ID_LAST_OUTPUT_TELGR") + "&tab=" + Bean.currentMenu.getTabID("CLIENTS_TERMSES_TELGR")
					) %>
			</td> <td><input type="text" name="id_last_output_telgr" size="40" value="<%=termSession.getValue("NAME_LAST_OUTPUT_TELGR") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>								
			<td valign="top"><%= Bean.term_sesXML.getfieldTransl("desc_term_ses", false) %></td><td  colspan="3"><textarea name="desc_term_ses" cols="70" rows="3" readonly="readonly" class="inputfield-ro"><%= termSession.getValue("DESC_TERM_SES") %></textarea></td>
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
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("telegram_find", telegram_find, "../crm/clients/termsespecs.jsp?id=" + termsesid + "&telegram_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
	<%= termSession.getSessionTelegramsHTML(telegram_find, l_telegram_page_beg, l_telegram_page_end) %> 
<%
  if (!(id_telgr==null || "".equalsIgnoreCase(id_telgr))) {
	  bcTelegramObject telegram = new bcTelegramObject(id_telgr);
      %> <%= telegram.getTelegramSourceHTML(src_type, termSession.getValue("ID_TERM_SES")) %> 
<%  }
  }


if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_TRANSACTIONS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("transaction_find", transaction_find, "../crm/clients/termsespecs.jsp?id=" + termsesid + "&transaction_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("transaction_type", "../crm/clients/termsespecs.jsp?id=" + termsesid + "&transaction_page=1", Bean.getClubCardXMLFieldTransl("id_profile", false)) %>
			<%= Bean.getTransTypeOptions(transaction_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("transaction_pay_type", "../crm/clients/termsespecs.jsp?id=" + termsesid + "&transaction_page=1", Bean.transactionXML.getfieldTransl("pay_type", false)) %>
			<%= Bean.getTransPayTypeOptions(transaction_pay_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("transaction_state", "../crm/clients/termsespecs.jsp?id=" + termsesid + "&transaction_page=1", Bean.transactionXML.getfieldTransl("state_trans", false)) %>
			<%= Bean.getTransStateOptions(transaction_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= termSession.getTransactionsHTML(transaction_find, transaction_type, transaction_pay_type, transaction_state, l_transaction_page_beg, l_transaction_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_POSTINGS_TRANS")) {%>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("posting_find", posting_find, "../crm/clients/termsespecs.jsp?id=" + termsesid + "&postings_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("posting_bk_oper", "../crm/clients/termsespecs.jsp?id=" + termsesid + "&postings_page=1", "") %>
			<%= Bean.getBKOperationTypeShortOptions(posting_bk_oper, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table>
	<%= termSession.getTermSesPostingsHTML(posting_find, posting_bk_oper, l_postings_beg, l_postings_end) %>


<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_TERMSES_SYS_LOG")) {%>
	<% bcSysLogObject log = new bcSysLogObject(); %>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("log_find", log_find, "../crm/clients/termsespecs.jsp?id=" + termsesid + "&log_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("id_telgr_log", "../crm/clients/termsespecs.jsp?id=" + termsesid + "&log_page=1", "") %>
				<%= Bean.getSelectOptionHTML(id_telgr_log, "0", "") %>
				<%= Bean.getTermSessionTelgrListOptions(termSession.getValue("ID_TERM_SES"), id_telgr_log) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		
			<%=Bean.getSelectOnChangeBeginHTML("log_row_type", "../crm/clients/termsespecs.jsp?id=" + termsesid + "&log_page=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
				<%= Bean.getSelectOptionHTML(l_log_row_type, "0", "") %>
				<%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", l_log_row_type, false) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>
	
	<%= log.getSysLogTermInterchangeHTML(log_find, "", termsesid, id_telgr_log, "", l_log_row_type, l_log_beg, l_log_end) %>
<%}

%>
 <%} %>
</div></div>
</body>
</html>
