<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="bc.objects.bcJurPrsObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcErrorTransactionObject"%>
<%@page import="bc.objects.bcNatPrsRoleObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
</head>

<% /* Получаем скрипты для проверки, изменены ли данные на форме*/ %>
<%= Bean.getCheckScripts() %>

<body topmargin="0"">
<div id="div_tabsheet">

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_CLUBCARDS";

Bean.setJspPageForTabName(pageFormName);

String tagTrans = "_ERROR_TRANS";
String tagRequest = "_REQUEST";
String tagRequestFind = "_REQUEST_FIND";
String tagHistory = "_HISTORY";
String tagHistoryFind = "_HISTORY_FIND";
String tagBKAccounts = "_BK_ACCOUNTS";
String tagBKAccountFind = "_BK_ACCOUNT_FIND";
String tagBKAccountExist = "_BK_ACCOUNT_EXIST";
String tagTaskType = "_TASK_TYPE";
String tagTaskState = "_TASK_STATE";
String tagTaskFind = "_TASK_FIND";
String tagTasks = "_TASKS";
String tagPostings = "_POSTINGS";
String tagPostingsFind = "_POSTINGS_FIND";
String tagPostingsBKOper = "_POSTINGS_BK_OPER";
String tagLogistic = "_LOGISTIC";
String tagLogisticFind = "_LOGISTIC_FIND";
String tagFindTrans = "_FIND_TRANS";
String tagPurses = "_PURSES";
String tagPurseFind = "_PURSE_FIND";
String tagPurseType = "_PURSE_TYPE";
String tagFinOper = "_FIN_OPER";
String tagFinOperState = "_FIN_OPER_STATE";
String tagFinOperFind = "_FIN_OPER_FIND";
String tagRests = "_RESTS";
String tagDiscount = "_DISCOUNT";
String tagDiscountFind = "_DISCOUNT_FIND";

String tagPrifile = "_PROFILE_CARD";
String tagTypeTrans = "_TYPE_TRANS";
String tagTransState = "_TRANS_STATE";
String tagTransPayType = "_TRANS_PAY_TYPE";

String cardid = Bean.getDecodeParam(parameters.get("id"));
String iss = Bean.getDecodeParam(parameters.get("iss"));
String paysys = Bean.getDecodeParam(parameters.get("paysys"));

String	tab = Bean.getDecodeParam(parameters.get("tab"));
tab = Bean.isEmpty(tab)?Bean.tabsHmGetValue(pageFormName):tab;
if (Bean.isEmpty(cardid)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcClubCardObject clubcard = new bcClubCardObject(cardid, iss, paysys);
	
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_BONAPPL", true);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_PURSES", true);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_RESTS", true);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_TRANS", true);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_REQUESTS", true);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_HISTORY", true);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_BK_ACCOUNTS", true);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_POSTINGS", true);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_LOGISTIC", true);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_DISCOUNT", true);
	
	String l_name_nat_prs = Bean.getNatPrsName(clubcard.getValue("ID_NAT_PRS"));
	
	//Обрабатываем номера страниц
	String l_purse_page = Bean.getDecodeParam(parameters.get("purse_page"));
	Bean.pageCheck(pageFormName + tagPurses, l_purse_page);
	String l_purse_page_beg = Bean.getFirstRowNumber(pageFormName + tagPurses);
	String l_purse_page_end = Bean.getLastRowNumber(pageFormName + tagPurses);
	
	String purse_find 	= Bean.getDecodeParam(parameters.get("purse_find"));
	purse_find 	= Bean.checkFindString(pageFormName + tagPurseFind, purse_find, l_purse_page);
	
	String purse_type 	= Bean.getDecodeParam(parameters.get("purse_type"));
	purse_type 	= Bean.checkFindString(pageFormName + tagPurseType, purse_type, l_purse_page);
	
	String l_trans_page = Bean.getDecodeParam(parameters.get("trans_page"));
	Bean.pageCheck(pageFormName + tagTrans, l_trans_page);
	String l_trans_page_beg = Bean.getFirstRowNumber(pageFormName + tagTrans);
	String l_trans_page_end = Bean.getLastRowNumber(pageFormName + tagTrans);
	
	String l_rests_page = Bean.getDecodeParam(parameters.get("rests_page"));
	Bean.pageCheck(pageFormName + tagRests, l_rests_page);
	String l_rests_page_beg = Bean.getFirstRowNumber(pageFormName + tagRests);
	String l_rests_page_end = Bean.getLastRowNumber(pageFormName + tagRests);

	String l_request_page = Bean.getDecodeParam(parameters.get("request_page"));
	Bean.pageCheck(pageFormName + tagRequest, l_request_page);
	String l_request_page_beg = Bean.getFirstRowNumber(pageFormName + tagRequest);
	String l_request_page_end = Bean.getLastRowNumber(pageFormName + tagRequest);
	
	String request_find 	= Bean.getDecodeParam(parameters.get("request_find"));
	request_find 	= Bean.checkFindString(pageFormName + tagRequestFind, request_find, l_request_page);
	
	String l_history_page = Bean.getDecodeParam(parameters.get("history_page"));
	Bean.pageCheck(pageFormName + tagHistory, l_history_page);
	String l_history_page_beg = Bean.getFirstRowNumber(pageFormName + tagHistory);
	String l_history_page_end = Bean.getLastRowNumber(pageFormName + tagHistory);
	
	String history_find 	= Bean.getDecodeParam(parameters.get("history_find"));
	history_find 	= Bean.checkFindString(pageFormName + tagHistoryFind, history_find, l_history_page);
	
	String l_discount_page = Bean.getDecodeParam(parameters.get("discount_page"));
	Bean.pageCheck(pageFormName + tagDiscount, l_discount_page);
	String l_discount_page_beg = Bean.getFirstRowNumber(pageFormName + tagDiscount);
	String l_discount_page_end = Bean.getLastRowNumber(pageFormName + tagDiscount);
	
	String discount_find 	= Bean.getDecodeParam(parameters.get("discount_find"));
	discount_find 	= Bean.checkFindString(pageFormName + tagDiscountFind, discount_find, l_discount_page);
	
	String l_bk_acc_page = Bean.getDecodeParam(parameters.get("bk_acc_page"));
	Bean.pageCheck(pageFormName + tagBKAccounts, l_bk_acc_page);
	String l_bk_acc_page_beg = Bean.getFirstRowNumber(pageFormName + tagBKAccounts);
	String l_bk_acc_page_end = Bean.getLastRowNumber(pageFormName + tagBKAccounts);
	
	String bk_acc_find 	= Bean.getDecodeParam(parameters.get("bk_acc_find"));
	bk_acc_find 	= Bean.checkFindString(pageFormName + tagBKAccountFind, bk_acc_find, l_bk_acc_page);
	
	String bk_acc_exist 	= Bean.getDecodeParam(parameters.get("bk_acc_exist"));
	bk_acc_exist 	= Bean.checkFindString(pageFormName + tagBKAccountExist, bk_acc_exist, l_bk_acc_page);
	
	String l_tasks_page = Bean.getDecodeParam(parameters.get("tasks_page"));
	Bean.pageCheck(pageFormName + tagTasks, l_tasks_page);
	String l_tasks_page_beg = Bean.getFirstRowNumber(pageFormName + tagTasks);
	String l_tasks_page_end = Bean.getLastRowNumber(pageFormName + tagTasks);

	String task_type 	= Bean.getDecodeParam(parameters.get("task_type"));
	task_type 	= Bean.checkFindString(pageFormName + tagTaskType, task_type, l_tasks_page);
	
	String task_state	= Bean.getDecodeParam(parameters.get("task_state"));
	task_state 		= Bean.checkFindString(pageFormName + tagTaskState, task_state, l_tasks_page);
	
	String task_find 	= Bean.getDecodeParam(parameters.get("task_find"));
	task_find 	= Bean.checkFindString(pageFormName + tagTaskFind, task_find, l_tasks_page);
	
	
	String l_posting_page = Bean.getDecodeParam(parameters.get("posting_page"));
	Bean.pageCheck(pageFormName + tagPostings, l_posting_page);
	String l_posting_page_beg = Bean.getFirstRowNumber(pageFormName + tagPostings);
	String l_posting_page_end = Bean.getLastRowNumber(pageFormName + tagPostings);

	String posting_find 	= Bean.getDecodeParam(parameters.get("posting_find"));
	posting_find 	= Bean.checkFindString(pageFormName + tagPostingsFind, posting_find, l_posting_page);

	String posting_bk_oper 	= Bean.getDecodeParam(parameters.get("posting_bk_oper"));
	posting_bk_oper 	= Bean.checkFindString(pageFormName + tagPostingsBKOper, posting_bk_oper, l_posting_page);

	String l_logistic_page = Bean.getDecodeParam(parameters.get("logistic_page"));
	Bean.pageCheck(pageFormName + tagLogistic, l_logistic_page);
	String l_logistic_page_beg = Bean.getFirstRowNumber(pageFormName + tagLogistic);
	String l_logistic_page_end = Bean.getLastRowNumber(pageFormName + tagLogistic);
	
	String logistic_find 	= Bean.getDecodeParam(parameters.get("logistic_find"));
	logistic_find 	= Bean.checkFindString(pageFormName + tagLogisticFind, logistic_find, l_logistic_page);
	
	String l_fin_oper_page = Bean.getDecodeParam(parameters.get("fin_oper_page"));
	Bean.pageCheck(pageFormName + tagFinOper, l_fin_oper_page);
	String l_fin_oper_page_beg = Bean.getFirstRowNumber(pageFormName + tagFinOper);
	String l_fin_oper_page_end = Bean.getLastRowNumber(pageFormName + tagFinOper);

	String fin_oper_state	= Bean.getDecodeParam(parameters.get("fin_oper_state"));
	fin_oper_state 		= Bean.checkFindString(pageFormName + tagFinOperState, fin_oper_state, l_fin_oper_page);
	
	String fin_oper_find 	= Bean.getDecodeParam(parameters.get("fin_oper_find"));
	fin_oper_find 	= Bean.checkFindString(pageFormName + tagFinOperFind, fin_oper_find, l_fin_oper_page);
	
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_INFO")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CLUBCARDS_INFO")) { %>
				    <%= Bean.getMenuButton("RUN", "../crm/cards/clubcardupdate.jsp?card_serial_number="+ cardid+"&id_issuer="+ iss+"&id_payment_system="+ paysys + "&cd_card1=" + clubcard.getValue("CD_CARD1") + "&type=actions&action=choise&process=no", "", "") %>
				<% } %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_PURSES")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPurses, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_PURSES")+"&", "purse_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_TASKS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CLUBCARDS_TASKS")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/cards/card_tasksupdate.jsp?back_type=CARD&card_serial_number="+ cardid+"&id_issuer="+ iss+"&id_payment_system="+ paysys + "&cd_card1=" + clubcard.getValue("CD_CARD1") + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTasks, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_TASKS")+"&", "tasks_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_TRANS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CLUBCARDS_TRANS")) { %>
				    <%= Bean.getMenuButton("CHECK", "../crm/cards/clubcardupdate.jsp?card_serial_number="+ cardid+"&id_issuer="+ iss+"&id_payment_system="+ paysys + "&type=errortrans&action=check&process=yes", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTrans, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_TRANS")+"&", "trans_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_REQUESTS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRequest, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_REQUESTS")+"&", "request_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_RESTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CLUBCARDS_RESTS")) { %>
					<%= Bean.getMenuButton("RUN", "../crm/cards/clubcardupdate.jsp?card_serial_number="+ cardid+"&id_issuer="+ iss+"&id_payment_system="+ paysys + "&cd_card1=" + clubcard.getValue("CD_CARD1") + "&type=rests&action=run&process=yes", Bean.bk_accountXML.getfieldTransl("h_calc_rests", false), "", Bean.bk_accountXML.getfieldTransl("h_calc_rests", false)) %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRests, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_RESTS")+"&", "rests_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_HISTORY")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagHistory, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_HISTORY")+"&", "history_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_DISCOUNT")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDiscount, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_DISCOUNT")+"&", "discount_page") %>
			<% }%>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_BK_ACCOUNTS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBKAccounts, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_BK_ACCOUNTS")+"&", "bk_acc_page") %>
			<% } %>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_POSTINGS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPostings, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_POSTINGS")+"&", "posting_page") %>
			<% } %>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_LOGISTIC")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLogistic, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_LOGISTIC")+"&", "logistic_page") %>
			<% } %>
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_FINANCE_OPER")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagFinOper, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_FINANCE_OPER")+"&", "fin_oper_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
        <% String l_caption = clubcard.getValue("CD_CARD1");
		   if (Bean.isEmpty(l_name_nat_prs)) {
			   l_caption = l_caption + " (<span style=\"color:red\"><b>" + Bean.natprsXML.getfieldTransl("h_client_not_found", false) + "</b></span>)";
		   } else {
			   l_caption = l_caption + " - " + l_name_nat_prs;
		   }
        %>
		<%= Bean.getDetailCaption(l_caption) %>
		<tr>
			<!-- Выводим перечень закладок -->
			<td>
			<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_INFO")) { 
	boolean hasEditPerm = Bean.currentMenu.isTabSheetEditPermitted("CARDS_CLUBCARDS_INFO");
%>

		<% if (hasEditPerm) { %>
	<script>
		var formData = new Array (
			new Array ('name_referral_nat_prs_role', 'varchar2', 1),
			new Array ('club_date_beg', 'varchar2', 1),
			new Array ('cd_club_member_status', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}
	</script>
		  <form action="../crm/cards/clubcardupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="id_nat_prs_role" value="<%= clubcard.getValue("ID_NAT_PRS_ROLE_CURRENT") %>">
	    	<input type="hidden" name="card_serial_number" value="<%= cardid %>">
	    	<input type="hidden" name="id_issuer" value="<%= iss %>">
	    	<input type="hidden" name="id_payment_system" value="<%= paysys %>">

		<%} %>
		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1" size="40" value="<%= clubcard.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_card_status", false) %></td><td><input type="text" name="name_card_status" size="40" value="<%= clubcard.getValue("NAME_CARD_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card2", false) %> </td><td><input type="text" name="cd_card2" size="10" value="<%= clubcard.getValue("CD_CARD2") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_card_material", false) %></td><td><input type="text" name="name_card_material" size="40" value="<%= clubcard.getValue("NAME_CARD_MATERIAL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("card_serial_number", false) %></td><td><input type="text" name="card_serial_number" size="40" value="<%= cardid %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_card_info_store", false) %></td><td><input type="text" name="name_card_info_store" size="40" value="<%= clubcard.getValue("NAME_CARD_INFO_STORE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		    <td><%= Bean.getClubCardXMLFieldTransl("name_bank", false) %>
				<%= Bean.getGoToJurPrsHyperLink(clubcard.getValue("ID_ISSUER")) %></td>
			<td><input type="text" name="name_bank" size="40" value="<%= Bean.getJurPersonShortName(iss) %>" readonly="readonly" class="inputfield-ro"></td>
		  	<td><%= Bean.getClubCardXMLFieldTransl("id_card_type", false) %></td><td><input type="text" name="id_card_type" size="40" value="<%= clubcard.getValue("NAME_CARD_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
 			<td><%= Bean.getClubCardXMLFieldTransl("name_payment_system", false) %></td><td><input type="text" name="name_payment_system" size="40" value="<%= clubcard.getValue("NAME_PAYMENT_SYSTEM")%>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("id_card_state", false) %></td><td><input type="text" name="id_card_state" size="40" value="<%= clubcard.getValue("NAME_CARD_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		    <td><%= Bean.getClubCardXMLFieldTransl("name_currency", false) %>
				<%= Bean.getGoToCurrencyLink(clubcard.getValue("CD_CURRENCY")) %>
			</td><td><input type="text" name="name_currency" size="40" value="<%= clubcard.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("expiry_date", false) %></td><td><input type="text" name="expiry_date" size="12" value="<%= clubcard.getValue("EXPIRY_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		
		<% if (Bean.isEmpty(clubcard.getValue("ID_NAT_PRS_ROLE_CURRENT"))) { %>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%=Bean.natprsXML.getfieldTransl("title_card_give", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.clubcardXML.getfieldTransl("title_card_not_issued", false)%></td>
			<td><%=Bean.getSubmitButtonAjax("../crm/cards/clubcardupdate.jsp", "button_give", "updateGiveCard", "div_main") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>

		<%
		} else {
    		bcNatPrsRoleObject role = new bcNatPrsRoleObject(clubcard.getValue("ID_NAT_PRS_ROLE_CURRENT"));
    		bcNatPrsRoleObject referral = null;
    		boolean referralExist = false;
    		if (!Bean.isEmpty(role.getValue("ID_REFERRAL_NAT_PRS_ROLE"))) {
    			referral = new bcNatPrsRoleObject(role.getValue("ID_REFERRAL_NAT_PRS_ROLE"));
    			if (referral.getResultSetRowCount()> 0) {
    				referralExist = true ;
    			}
    		}
		%>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%=Bean.natprsXML.getfieldTransl("title_card_sale", false)%></b></td>
		</tr>

		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("fio_nat_prs", false)%>
				<%= Bean.getGoToNatPrsLink(role.getValue("ID_NAT_PRS")) %>
			</td>
			<td>
				<input type="hidden" id="id_nat_prs" name="id_nat_prs" value="<%= role.getValue("ID_NAT_PRS") %>">
				<input type="text" id="name_nat_prs" name="name_nat_prs" size="40" value="<%=role.getValue("FIO_NAT_PRS") %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<td><%=Bean.clubXML.getfieldTransl("club", false)%>
				<%= Bean.getGoToClubLink(role.getValue("ID_CLUB")) %>
			</td> 
			<td><input type="text" name="name_club" size="40" value="<%= role.getValue("SNAME_CLUB")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><%=Bean.natprsXML.getfieldTransl("title_card", false)%>:&nbsp;
				<%=Bean.getCheckBox("is_corporate_card", role.getValue("IS_CORPORATE_CARD"), Bean.natprsXML.getfieldTransl("is_corporate_card", false)) %>
				<%=Bean.getCheckBox("is_temporary_card", role.getValue("IS_TEMPORARY_CARD"), Bean.natprsXML.getfieldTransl("is_temporary_card", false)) %>
			</td>			
			<% if (referralExist) { %>
				<td><%=Bean.natprsXML.getfieldTransl("cd_referral_card1", true)%>
					<%= Bean.getGoToClubCardLink(
							referral.getValue("CARD_SERIAL_NUMBER"),
							referral.getValue("CARD_ID_ISSUER"),
							referral.getValue("CARD_ID_PAYMENT_SYSTEM")
						) %>
				</td><td>
					<%=Bean.getWindowFindNatPrsRole("referral_nat_prs_role", role.getValue("ID_REFERRAL_NAT_PRS_ROLE"), "30") %>
				</td>
			<% } else { %>
				<td><%=Bean.natprsXML.getfieldTransl("cd_referral_card1", true)%> (<%= Bean.natprsXML.getfieldTransl("referral_isnot_specified_short", true).toLowerCase() %>)</td>
				<td>
					<%=Bean.getWindowFindNatPrsRole("referral_nat_prs_role", "", "30") %>
				</td>
			<% } %>
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("title_work_place", false)%>
				<%= Bean.getGoToServicePlaceLink(role.getValue("ID_SERVICE_PLACE_WORK")) %>
			</td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_work", role.getValue("ID_SERVICE_PLACE_WORK"), role.getValue("SNAME_SERVICE_PLACE_WORK"), "35") %>
			</td>			
			<td><%=Bean.natprsXML.getfieldTransl("club_date_beg", true)%></td> <td><%=Bean.getCalendarInputField("club_date_beg", role.getValue("CLUB_DATE_BEG_DF"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.contactXML.getfieldTransl("name_post", false) %></td> <td><select name="cd_post" class="inputfield" > <%= Bean.getContactPrsTypeOptions(role.getValue("CD_POST"), true) %></select></td>
			<td><%=Bean.natprsXML.getfieldTransl("club_date_end", false)%></td> <td><%=Bean.getCalendarInputField("club_date_end", role.getValue("CLUB_DATE_END_DF"), "10") %></td>
		</tr>
		<tr>
			<td>
				<%= Bean.natprsXML.getfieldTransl("phone_work", false) %>
				<% if ("N".equalsIgnoreCase(role.getValue("IS_PHONE_WORK_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="phone_work" size="30" value="<%= role.getValue("PHONE_WORK") %>" class="inputfield">
				<input type="checkbox" name="is_phone_work_valid" size="20" value="Y" style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(role.getValue("IS_PHONE_WORK_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", true) %></td> <td><select name="cd_club_member_status" class="inputfield" > <%= Bean.getClubMemberStatusOptions(role.getValue("CD_CLUB_MEMBER_STATUS"), true) %></select></td>
		</tr>
		<tr>
			<td>
				<%= Bean.natprsXML.getfieldTransl("email_work", false) %>
				<% if ("N".equalsIgnoreCase(role.getValue("IS_EMAIL_WORK_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="email_work" size="30" value="<%= role.getValue("EMAIL_WORK") %>" class="inputfield">
				<input type="checkbox" name="is_email_work_valid" size="20" value="Y" style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(role.getValue("IS_EMAIL_WORK_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
			<td>&nbsp;</td>
			<td>
				<%=Bean.getCheckBox("is_organizer", role.getValue("IS_ORGANIZER"), Bean.natprsXML.getfieldTransl("is_organizer", false)) %>
				<%=Bean.getCheckBox("is_investor", role.getValue("IS_INVESTOR"), Bean.natprsXML.getfieldTransl("is_investor", false)) %>
			</td>			
		</tr>

		<tr>
			<td class="top_line_gray"><%= Bean.natprsXML.getfieldTransl("cd_nat_prs_role_state", false) %></td> <td class="top_line_gray"><input type="text" name="name_nat_prs_role_state" size="40" value="<%= role.getValue("NAME_NAT_PRS_ROLE_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line_gray"><%=Bean.natprsXML.getfieldTransl("id_jur_prs_who_card_sold", false)%>
				<%= Bean.getGoToJurPrsHyperLink(role.getValue("ID_JUR_PRS_WHO_CARD_SOLD")) %>
			</td>
			<td class="top_line_gray"><input type="text" name="id_jur_prs_who_card_sold" size="40" value="<%= Bean.getJurPersonShortName(role.getValue("ID_JUR_PRS_WHO_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("code_country_give", false)%></td><td><input type="text" name="code_country_give" size="20" value="<%= Bean.getCountryName(role.getValue("CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.natprsXML.getfieldTransl("name_serv_plce_where_card_sold", false)%>
				<%= Bean.getGoToServicePlaceLink(role.getValue("id_serv_place_where_card_sold")) %>
			</td>
			<td><input type="text" name="id_serv_place_where_card_sold" size="40" value="<%= Bean.getServicePlaceShortName(role.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("date_card_sale", false)%></td><td><input type="text" name="date_card_sale" size="20" value="<%= role.getValue("DATE_CARD_SALE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.natprsXML.getfieldTransl("id_term_who_card_sold", false)%>
				<%= Bean.getGoToTerminalLink(role.getValue("ID_TERM_WHO_CARD_SOLD")) %>
			</td>
			<td><input type="text" name="id_term_who_card_sold" size="40" value="<%= role.getValue("ID_TERM_WHO_CARD_SOLD") %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("total_amount_card_sale", false)%></td><td><input type="text" name="total_amount_card_sale" size="20" value="<%= role.getValue("TOTAL_AMOUNT_CARD_SALE_FRMT") %> <%=Bean.getCurrencyShortNameById(role.getValue("CD_CURRENCY_CARD_SALE")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.natprsXML.getfieldTransl("id_user_who_card_sold", false)%>
				<%= Bean.getGoToSystemUserLink(role.getValue("ID_USER_WHO_CARD_SOLD")) %>
			</td>
			<td><input type="text" name="id_user_who_card_sold" size="40" value="<%= Bean.getUserNatPrsName2(role.getValue("ID_USER_WHO_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("id_jur_prs_card_pack", false)%>
				<%= Bean.getGoToCardPackageLink(role.getValue("ID_JUR_PRS_CARD_PACK")) %>
			</td>
			<td><input type="text" name="id_jur_prs_card_pack" size="40" value="<%= Bean.getClubJurPrsCardPackName(role.getValue("ID_JUR_PRS_CARD_PACK")) %>" readonly="readonly" class="inputfield-ro"></td>			
			<td><%=Bean.natprsXML.getfieldTransl("id_trans_card_given", false)%>
				<%= Bean.getGoToTransactionLink(role.getValue("ID_TRANS_CARD_GIVEN")) %>
			</td>
			<td><input type="text" name="id_trans_card_given" size="20" value="<%= role.getValue("ID_TRANS_CARD_GIVEN") %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("membership_month_sum", false)%> </td> 
			<td>
				<input type="text" name="membership_month_sum" size="15" value="<%= role.getValue("MEMBERSHIP_MONTH_SUM_FRMT") %>" class="inputfield">
				<select name="cd_currency" class="inputfield"><%=Bean.getCurrencyShortNameOptions(role.getValue("MEMBERSHIP_CD_CURRENCY"), true)%></select> 
			</td>
			<td><%=Bean.natprsXML.getfieldTransl("id_trans_card_activation", false)%>
				<%= Bean.getGoToTransactionLink(role.getValue("ID_TRANS_CARD_ACTIVATION")) %>
			</td>
			<td><input type="text" name="id_trans_card_activation" size="20" value="<%= role.getValue("id_trans_card_activation") %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td><%=Bean.natprsXML.getfieldTransl("membership_last_date", false)%></td> <td><%=Bean.getCalendarInputField("membership_last_date", role.getValue("MEMBERSHIP_LAST_DATE_DF"), "10") %></td>
			<td rowspan="2"><%=Bean.natprsXML.getfieldTransl("desc_nat_prs_role", false)%></td><td rowspan="2"><textarea name="desc_nat_prs_role" cols="37" rows="2" class="inputfield"><%=role.getValue("DESC_NAT_PRS_ROLE") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("is_questionnaire_checked", false) %></td> <td><select name="is_questionnaire_checked" class="inputfield" > <%= Bean.getYesNoLookupOptions(role.getValue("IS_QUESTIONNAIRE_CHECKED"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("can_use_share_account", false) %></td> <td><select name="can_use_share_account" class="inputfield" > <%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("YES_NO_UNKNOWN", role.getValue("CAN_USE_SHARE_ACCOUNT"), true) %></select></td>
		</tr>
		<% } %>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				clubcard.getValue(Bean.getCreationDateFieldName()),
				clubcard.getValue("CREATED_BY"),
				clubcard.getValue(Bean.getLastUpdateDateFieldName()),
				clubcard.getValue("LAST_UPDATE_BY")
			) %>
		<% if (hasEditPerm) {%>
			<tr>
				<td colspan="6" align="center">
					<% if (!Bean.isEmpty(clubcard.getValue("ID_NAT_PRS_ROLE_CURRENT"))) { %>
					<%=Bean.getSubmitButtonAjax("../crm/cards/clubcardupdate.jsp") %>
					<% } %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/cards/clubcards.jsp") %>
				</td>
			</tr>
		<% } else { %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getGoBackButton("../crm/cards/clubcards.jsp") %>
				</td>
			</tr>
		<% } %>
	</table>
	</form>

	<form action="../crm/cards/clubcardupdate.jsp" name="updateGiveCard" id="updateGiveCard" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="type" value="actions">
	    	<input type="hidden" name="process" value="no">
	    	<input type="hidden" name="action" value="choise">
	    	<input type="hidden" name="card_action" value="give">
	    	<input type="hidden" name="card_serial_number" value="<%= cardid %>">
	    	<input type="hidden" name="id_issuer" value="<%= iss %>">
	    	<input type="hidden" name="id_payment_system" value="<%= paysys %>">
	    	<input type="hidden" name="cd_card1" value="<%= clubcard.getValue("CD_CARD1") %>">
	</form>

<%
} if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_BONAPPL")) { 
	boolean hasEditPerm = Bean.currentMenu.isTabSheetEditPermitted("CARDS_CLUBCARDS_BONAPPL");

				
		%> 		
		<% if (hasEditPerm) { %>

		<script>
			var formData = new Array (
				new Array ('vk_sys_key_card', 'varchar2', 1),
				new Array ('nt_icc', 'varchar2', 1),
				new Array ('id_card_status_new', 'varchar2', 1),
				new Array ('id_bon_category', 'varchar2', 1),
				new Array ('id_disc_category', 'varchar2', 1)
			);
		</script>
		  <form action="../crm/cards/clubcardupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="action" value="bonappl">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="card_serial_number" value="<%= cardid %>">
	    	<input type="hidden" name="id_issuer" value="<%= iss %>">
	    	<input type="hidden" name="id_payment_system" value="<%= paysys %>">
	    	<input type="hidden" name="id_card_status" value="<%= clubcard.getValue("ID_CARD_STATUS") %>">
		<%} %>
		<table <%=Bean.getTableDetailParam() %>>
		<% if (hasEditPerm) { %>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("name_card_status", true) %></td><td><select name="id_card_status_new" id="id_card_status_new" class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%= Bean.getSessionId()%>');"><%= Bean.getClubCardStatusOptions(clubcard.getValue("ID_CARD_STATUS"), true) %></select></td>
			<td><%= Bean.getClubCardXMLFieldTransl("date_open", false) %>
			 	<%=Bean.getUWndHelp(Bean.getClubCardXMLFieldTransl("help_date_open", false)) %>
			</td><td><input type="text" name="date_open" size="12" value="<%= clubcard.getValue("DATE_OPEN_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td><%= Bean.getClubCardXMLFieldTransl("nt_icc", false) %></td><td><input type="text" name="nt_icc" size="16" value="<%= clubcard.getValue("NT_ICC") %>" class="inputfield"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("ver_key", true) %></td><td><select name="vk_sys_key_card" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("VK_SYS_KEY_CARD", clubcard.getValue("VER_KEY"), true) %></select></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_card_categories", false) %></b></td>
		</tr>
		<tr>			
			<td><%= Bean.getClubCardXMLFieldTransl("name_bon_category", true) %></td> <td><select name="id_bon_category" id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(clubcard.getValue("ID_CARD_STATUS"),clubcard.getValue("ID_BON_CATEGORY"),"BON",true) %></select></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_disc_category", true) %></td> <td><select name="id_disc_category" id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(clubcard.getValue("ID_CARD_STATUS"),clubcard.getValue("ID_DISC_CATEGORY"),"DISC",true) %></select></td>
		</tr>
		<tr>			
			<td><%= Bean.getClubCardXMLFieldTransl("CLUB_BON", false) %> </td> <td><input type="text" name="club_bon" size="15" value="<%= clubcard.getValue("CLUB_BON_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("CLUB_DISC", false) %></td> <td><input type="text" name="club_disc" size="15" value="<%= clubcard.getValue("CLUB_DISC_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_card_rests", false) %></b></td>
		</tr>
		<tr>			
		    <td><%= Bean.getClubCardXMLFieldTransl("BAL_ACC", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_acc" size="15" value="<%= clubcard.getValue("BAL_ACC_FRMT") %>" class="inputfield"> </td>
			<td><%= Bean.getClubCardXMLFieldTransl("BAL_BON_PER", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_bon_per" size="15" value="<%= clubcard.getValue("BAL_BON_PER_FRMT") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("BAL_CUR", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_cur" size="15" value="<%= clubcard.getValue("BAL_CUR_FRMT") %>" class="inputfield"> </td>
			<td><%= Bean.getClubCardXMLFieldTransl("BAL_DISC_PER", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_disc_per" size="15" value="<%= clubcard.getValue("BAL_DISC_PER_FRMT") %>" class="inputfield"></td>
		</tr>
		<tr>			
			<td><%= Bean.getClubCardXMLFieldTransl("BAL_FULL", false) %> </td> <td><input type="text" name="bal_full" size="15" value="<%= clubcard.getValue("BAL_FULL_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_card_dates", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("date_acc", false) %></td><td><%=Bean.getCalendarInputField("date_acc", clubcard.getValue("DATE_ACC_FRMT"), "10") %></td>
			<td><%= Bean.getClubCardXMLFieldTransl("date_onl", false) %></td><td><%=Bean.getCalendarInputField("date_onl", clubcard.getValue("DATE_ONL_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("date_calc", false) %></td><td><%=Bean.getCalendarInputField("date_calc", clubcard.getValue("DATE_CALC_FRMT"), "10") %></td>
			<td><%= Bean.getClubCardXMLFieldTransl("date_onl_next", false) %></td><td><%=Bean.getCalendarInputField("date_onl_next", clubcard.getValue("DATE_ONL_NEXT_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("date_mov", false) %></td><td><%=Bean.getCalendarInputField("date_mov", clubcard.getValue("DATE_MOV_FRMT"), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/clubcardupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/cards/clubcards.jsp") %>
			</td>
		</tr>
		<% } else { %>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("name_card_status", false) %></td><td><input type="text" name="name_card_status" size="20" value="<%= clubcard.getValue("NAME_CARD_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("date_open", false) %>
			 	<%=Bean.getUWndHelp(Bean.getClubCardXMLFieldTransl("help_date_open", false)) %>
			</td><td><input type="text" name="date_open" size="12" value="<%= clubcard.getValue("DATE_OPEN_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
	      	<td><%= Bean.getClubCardXMLFieldTransl("nt_icc", false) %></td><td><input type="text" name="nt_icc" size="16" value="<%= clubcard.getValue("NT_ICC") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("ver_key", false) %></td><td><input type="text" name="ver_key" size="10" value="<%= clubcard.getValue("VER_KEY")%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_card_categories", false) %></b></td>
		</tr>
		<tr>			
			<td><%= Bean.getClubCardXMLFieldTransl("name_bon_category", false) %> </td> <td><input type="text" name="name_bon_category" size="20" value="<%= clubcard.getValue("NAME_BON_CATEGORY") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_disc_category", false) %></td> <td><input type="text" name="name_disc_category" size="20" value="<%= clubcard.getValue("NAME_DISC_CATEGORY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>			
			<td><%= Bean.getClubCardXMLFieldTransl("CLUB_BON", false) %> </td> <td><input type="text" name="club_bon" size="10" value="<%= clubcard.getValue("CLUB_BON_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("CLUB_DISC", false) %></td> <td><input type="text" name="club_disc" size="10" value="<%= clubcard.getValue("CLUB_DISC_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_card_rests", false) %></b></td>
		</tr>
		<tr>
		    <td><%= Bean.getClubCardXMLFieldTransl("BAL_ACC", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_acc" size="12" value="<%= clubcard.getValue("BAL_ACC_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.getClubCardXMLFieldTransl("BAL_BON_PER", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_bon_per" size="10" value="<%= clubcard.getValue("BAL_BON_PER_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("BAL_CUR", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_cur" size="10" value="<%= clubcard.getValue("BAL_CUR_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.getClubCardXMLFieldTransl("BAL_DISC_PER", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td><input type="text" name="bal_disc_per" size="10" value="<%= clubcard.getValue("BAL_DISC_PER_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>			
			<td><%= Bean.getClubCardXMLFieldTransl("BAL_FULL", false) %> </td> <td><input type="text" name="bal_full" size="15" value="<%= clubcard.getValue("BAL_FULL_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.getClubCardXMLFieldTransl("h_card_dates", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("DATE_ACC", false) %></td> <td><input type="text" name="date_acc" size="12" value="<%= clubcard.getValue("DATE_ACC_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.getClubCardXMLFieldTransl("DATE_ONL", false) %></td> <td> <input type="text" name="date_onl" size="10" value="<%= clubcard.getValue("DATE_ONL_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("DATE_CALC", false) %></td> <td> <input type="text" name="date_calc" size="10" value="<%= clubcard.getValue("DATE_CALC_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.getClubCardXMLFieldTransl("DATE_ONL_NEXT", false) %></td> <td> <input type="text" name="date_onl_next" size="10" value="<%= clubcard.getValue("DATE_ONL_NEXT_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("DATE_MOV", false) %></td> <td><input type="text" name="date_mov" size="10" value="<%= clubcard.getValue("DATE_MOV_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/cards/clubcards.jsp") %>
			</td>
		</tr>
		<% } %>
	</table>
	</form>
	
	<% if (hasEditPerm) { %>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_acc", false) %>
		<%= Bean.getCalendarScript("date_mov", false) %>
		<%= Bean.getCalendarScript("date_calc", false) %>
		<%= Bean.getCalendarScript("date_onl", false) %>
		<%= Bean.getCalendarScript("date_onl_next", false) %>
	<%} %>
<%
} if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_TASKS")) { 

%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("task_find", task_find, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tasks_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("task_type", "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tasks_page=1", Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)) %>
			<%= Bean.getClubCardOperationTypeOptions(task_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("task_state", "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&tasks_page=1", Bean.card_taskXML.getfieldTransl("cd_card_oper_state", false)) %>
			<%= Bean.getClubCardOperationStateOptions(task_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>

	<%= clubcard.getClubCardsTasksHTML(task_find, task_type, task_state, l_tasks_page_beg, l_tasks_page_end) %>
					<%
} 
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_TRANS")) {

	String id_profile 		= Bean.getDecodeParam(parameters.get("id_profile"));
	String type_trans 		= Bean.getDecodeParam(parameters.get("type_trans"));
	String find_trans 		= Bean.getDecodeParam(parameters.get("find_trans"));

	find_trans 	= Bean.checkFindString(pageFormName + tagFindTrans, find_trans, l_trans_page);
	id_profile 	= Bean.checkFindString(pageFormName + tagPrifile, id_profile, l_trans_page);
	type_trans 	= Bean.checkFindString(pageFormName + tagTypeTrans, type_trans, l_trans_page);

	String trans_state 	= Bean.getDecodeParam(parameters.get("trans_state"));
	trans_state 	= Bean.checkFindString(pageFormName + tagTransState, trans_state, l_trans_page);

	String trans_pay_type 	= Bean.getDecodeParam(parameters.get("trans_pay_type"));
	trans_pay_type 	= Bean.checkFindString(pageFormName + tagTransPayType, trans_pay_type, l_trans_page);
	
	%> 
	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("find_trans", find_trans, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&trans_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("type_trans", "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&trans_page=1", Bean.getClubCardXMLFieldTransl("id_profile", false)) %>
			<%= Bean.getTransTypeOptions(type_trans, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("trans_pay_type", "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&trans_page=1", Bean.transactionXML.getfieldTransl("pay_type", false)) %>
			<%= Bean.getTransPayTypeOptions(trans_pay_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("trans_state", "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&trans_page=1", Bean.transactionXML.getfieldTransl("state_trans", false)) %>
			<%= Bean.getTransStateOptions(trans_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  </tr>
	</table>

	<%=clubcard.getCardTrans2HTML(find_trans, type_trans, trans_pay_type, trans_state, l_trans_page_beg, l_trans_page_end)%> 

<% }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_REQUESTS")) {%>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("request_find", request_find, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&request_page=1&") %>

		<td>&nbsp;</td>

	  </tr>
	</table> 
	<%=clubcard.getCardRequestsHTML(request_find, l_request_page_beg, l_request_page_end)%> 
<% }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_RESTS")) {

    String tagFindRest = "_FIND_REST";
    String tagFindRestBegin = "_FIND_REST_BEGIN";
    String tagFindRestEnd = "_FIND_REST_END";
    
	String find_rest	 	= Bean.getDecodeParam(parameters.get("find_rest"));
	find_rest 				= Bean.checkFindString(pageFormName + tagFindRest, find_rest, "");
	String begin_rest_period	 	= Bean.getDecodeParam(parameters.get("begin_rest_period"));
	begin_rest_period 			= Bean.checkFindString(pageFormName + tagFindRestBegin, begin_rest_period, "");
	String end_rest_period	 	= Bean.getDecodeParam(parameters.get("end_rest_period"));
	end_rest_period 				= Bean.checkFindString(pageFormName + tagFindRestEnd, end_rest_period, "");

%> 
	<form action="../crm/cards/clubcardpursespecs.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	    	<input type="hidden" name="card_serial_number" value="<%= cardid %>">
	    	<input type="hidden" name="id_issuer" value="<%= iss %>">
	    	<input type="hidden" name="id_payment_system" value="<%= paysys %>">
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<td valign="top"><%= Bean.bk_accountXML.getfieldTransl("h_begin_period", false) %>&nbsp;<%=Bean.getCalendarInputField("begin_rest_period", begin_rest_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_end_period", false) %>&nbsp;<%=Bean.getCalendarInputField("end_rest_period", end_rest_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_find_string", false) %>&nbsp;
			<input type="text" name="find_rest" id="find_rest" size="30" value="<%=find_rest %>" class="inputfield" title="<%= Bean.buttonXML.getfieldTransl("find_string", false) %>">&nbsp;
			<%=Bean.getSubmitButtonAjax("../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&rest_page=1&", "find", "updateForm") %>&nbsp;
			</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	</form>
	<%= Bean.getCalendarScript("begin_rest_period", false) %>
	<%= Bean.getCalendarScript("end_rest_period", false) %>

	<%=clubcard.getRestsFullHTML(begin_rest_period, end_rest_period, find_rest, l_rests_page_beg, l_rests_page_end)%> 
<% }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_HISTORY")) { %>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("history_find", history_find, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&history_page=1&") %>

		<td>&nbsp;</td>

	  </tr>
	</table>
	<%=clubcard.getClubCardsHistoryHTML(history_find, l_history_page_beg, l_history_page_end)%> 
<% }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_DISCOUNT")) { %>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("discount_find", discount_find, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&discount_page=1&") %>

		<td>&nbsp;</td>

	  </tr>
	</table>
	
	<%=clubcard.getClubCardsDiscountHTML(discount_find, clubcard.getValue("ID_CARD_STATUS"), clubcard.getValue("ID_BON_CATEGORY"), clubcard.getValue("ID_DISC_CATEGORY"), "", "", "", l_discount_page_beg, l_discount_page_end)%> 
<% }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_BK_ACCOUNTS")) {%>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("bk_acc_find", bk_acc_find, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&bk_acc_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("bk_acc_exist", "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&bk_acc_page=1", Bean.bk_accountXML.getfieldTransl("exist_flag", false)) %>
			<%= Bean.getYesNoLookupOptions(bk_acc_exist, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table>
 
	<%= clubcard.getBKAccountsHTML(bk_acc_find, bk_acc_exist, l_bk_acc_page_beg, l_bk_acc_page_end) %> 
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_POSTINGS")) { %>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("posting_find", posting_find, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&posting_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("posting_bk_oper", "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&posting_page=1", "") %>
			<%= Bean.getBKOperationTypeShortOptions(posting_bk_oper, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table>

	<%= clubcard.getCardPostingsHTML(posting_find, posting_bk_oper, l_posting_page_beg, l_posting_page_end)%> 
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_LOGISTIC")) {%>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("logistic_find", logistic_find, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&logistic_page=1&") %>

		<td>&nbsp;</td>

	  </tr>
	</table> 
	<%=clubcard.getLogisticHTML(logistic_find, l_logistic_page_beg, l_logistic_page_end)%> 
<% }
 
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_PURSES")) {%>

	<table <%=Bean.getTableBottomFilter() %>>
	  <tr>
		<%= Bean.getFindHTML("purse_find", purse_find, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&purse_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("purse_type", "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&purse_page=1", Bean.getClubCardXMLFieldTransl("cd_card_purse_type", false)) %>
			<%= Bean.getClubCardPurseTypeOptions(purse_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  </tr>
	</table> 
	<%=clubcard.getPursesHTML(purse_find, purse_type, l_purse_page_beg, l_purse_page_end)%> 
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_FINANCE_OPER")) { 

	%> 		
		<table <%=Bean.getTableBottomFilter() %>>
		  	<tr>
			<%= Bean.getFindHTML("fin_oper_find", fin_oper_find, "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&fin_oper_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("fin_oper_state", "../crm/cards/clubcardspecs.jsp?id=" + cardid + "&iss=" + iss + "&paysys=" + paysys + "&fin_oper_page=1", Bean.card_taskXML.getfieldTransl("cd_card_oper_state", false)) %>
				<%= Bean.getClubCardFinanceOperationStateOptions(fin_oper_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		  	</tr>
		</table>

		<%= clubcard.getFinanceOperationsHTML(fin_oper_state, fin_oper_find, l_fin_oper_page_beg, l_fin_oper_page_end) %>
<%
}
 

}
%>
</div></div>
</body>
</html>
