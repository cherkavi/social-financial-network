<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcTransactionObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="bc.objects.bcTerminalLoyalityObject"%>
<%@page import="bc.objects.bcTerminalLoyLineHistoryObject"%>
<%@page import="bc.objects.bcSysLogObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_TRANSACTIONS";

Bean.setJspPageForTabName(pageFormName);

String tagPosting = "_POSTING";
String tagPostingsFind = "_POSTINGS_FIND";
String tagCardTask = "_CARD_TASK";
String tagCardTaskFind = "_CARD_TASK_FIND";
String tagFundOper = "_FUND_OPER";
String tagFundOperFind = "_FUND_OPER_FIND";
String tagPostingsBKOper = "_POSTINGS_BK_OPER";
String tagLogRowType = "_LOG_ROWTYPE";
String tagLogType = "_LOG_TYPE";
String tagLog = "_LOG";
String tagLogFind = "_LOG_FIND";
	
String transid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab == null) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}
if (transid==null || "".equalsIgnoreCase(transid)) {%>

	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcTransactionObject trans = new bcTransactionObject(transid);
	
	//Обрабатываем номера страниц
	String l_posting_page = Bean.getDecodeParam(parameters.get("posting_page"));
	Bean.pageCheck(pageFormName + tagPosting, l_posting_page);
	String l_posting_page_beg = Bean.getFirstRowNumber(pageFormName + tagPosting);
	String l_posting_page_end = Bean.getLastRowNumber(pageFormName + tagPosting);

	String posting_find 	= Bean.getDecodeParam(parameters.get("posting_find"));
	posting_find 	= Bean.checkFindString(pageFormName + tagPostingsFind, posting_find, l_posting_page);

	String posting_bk_oper 	= Bean.getDecodeParam(parameters.get("posting_bk_oper"));
	posting_bk_oper 	= Bean.checkFindString(pageFormName + tagPostingsBKOper, posting_bk_oper, l_posting_page);

	String l_card_task_page = Bean.getDecodeParam(parameters.get("card_task_page"));
	Bean.pageCheck(pageFormName + tagCardTask, l_card_task_page);
	String l_card_task_page_beg = Bean.getFirstRowNumber(pageFormName + tagCardTask);
	String l_card_task_page_end = Bean.getLastRowNumber(pageFormName + tagCardTask);

	String card_task_find 	= Bean.getDecodeParam(parameters.get("card_task_find"));
	card_task_find 	= Bean.checkFindString(pageFormName + tagCardTaskFind, card_task_find, l_card_task_page);

	String l_fund_oper_page = Bean.getDecodeParam(parameters.get("fund_oper_page"));
	Bean.pageCheck(pageFormName + tagFundOper, l_fund_oper_page);
	String l_fund_oper_page_beg = Bean.getFirstRowNumber(pageFormName + tagFundOper);
	String l_fund_oper_page_end = Bean.getLastRowNumber(pageFormName + tagFundOper);

	String fund_oper_find 	= Bean.getDecodeParam(parameters.get("fund_oper_find"));
	fund_oper_find 	= Bean.checkFindString(pageFormName + tagFundOperFind, fund_oper_find, l_fund_oper_page);
	
	String l_log_page = Bean.getDecodeParam(parameters.get("log_page"));
	Bean.pageCheck(pageFormName + tagLog, l_log_page);
	String l_log_beg = Bean.getFirstRowNumber(pageFormName + tagLog);
	String l_log_end = Bean.getLastRowNumber(pageFormName + tagLog);
	
	String log_find 	= Bean.getDecodeParam(parameters.get("log_find"));
	log_find 	= Bean.checkFindString(pageFormName + tagLogFind, log_find, l_log_page);
	
	String log_row_type 	= Bean.getDecodeParam(parameters.get("log_row_type"));
	if ("0".equalsIgnoreCase(log_row_type)) {
		log_row_type = "";
	}
	log_row_type 	= Bean.checkFindString(pageFormName + tagLogRowType, log_row_type, l_log_page);
	
	String log_type 	= Bean.getDecodeParam(parameters.get("log_type"));
	if ("0".equalsIgnoreCase(log_type)) {
		log_type = "";
	}
	log_type 	= Bean.checkFindString(pageFormName + tagLogType, log_type, l_log_page);
	
%>

<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_POSTINS")) { %>
				<%= Bean.getReportHyperLink("CARD_TRANS_REP1", "ID_TRANS=" + transid) %>
	
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_TRANSACTIONS_POSTINS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/cards/transactionsupdate.jsp?id=" + transid + "&type=posting&action=add&process=no&id_club=" + trans.getValue("ID_CLUB"), "", "") %>
				    <%= Bean.getMenuButton("POSTING", "../crm/cards/transactionsupdate.jsp?id=" + transid + "&type=posting&action=run&process=yes&id_club=" + trans.getValue("ID_CLUB"), Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_TRANS", false), transid, Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_TRANS", false)) %>
				    <%= Bean.getMenuButton("DELETE_ALL", "../crm/cards/transactionsupdate.jsp?id=" + transid + "&type=posting&action=deleteall&process=yes", Bean.postingXML.getfieldTransl("LAB_DELETE_ALL_POSTINGS", false), "") %>
                    <% if ("0".equalsIgnoreCase(trans.getValue("IS_POSTING"))) { %>
				    	<%= Bean.getMenuButton("CANCEL", "../crm/cards/transactionsupdate.jsp?id=" + transid + "&type=posting&action=cancel&process=yes", Bean.transactionXML.getfieldTransl("LAB_CANCEL_POSTING", false), "", Bean.transactionXML.getfieldTransl("LAB_CANCEL_POSTING", false)) %>
					<% } %>
				<% } %>

			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPosting, "../crm/cards/transactionspecs.jsp?id=" + transid + "&tab="+Bean.currentMenu.getTabID("CARDS_TRANSACTIONS_POSTINS")+"&", "posting_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_TASKS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagCardTask, "../crm/cards/transactionspecs.jsp?id=" + transid + "&tab="+Bean.currentMenu.getTabID("CARDS_TRANSACTIONS_TASKS")+"&", "card_task_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_FUND_OPER")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagFundOper, "../crm/cards/transactionspecs.jsp?id=" + transid + "&tab="+Bean.currentMenu.getTabID("CARDS_TRANSACTIONS_FUND_OPER")+"&", "fund_oper_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_LOG")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLog, "../crm/cards/transactionspecs.jsp?id=" + transid + "&tab="+Bean.currentMenu.getTabID("CARDS_TRANSACTIONS_LOG")+"&", "log_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(trans.getValue("ID_TRANS") + " - " +  trans.getValue("TYPE_TRANS_TXT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/cards/transactionspecs.jsp?id=" + transid) %>
			</td>
	</tr>
</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_INFO")) {
	
  String typeTrans = trans.getValue("TYPE_TRANS");

  // Редактирование транзакции запрещено
  if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_TRANSACTIONS_INFO") && 1==0) { 
  	
  	%>
	<script>
		var formTransData = new Array (
			new Array ('card_serial_number', 'varchar2', 1),
			new Array ('type_trans', 'varchar2', 1),
			new Array ('id_term', 'varchar2', 1),
			new Array ('cd_currency', 'varchar2', 1),
			new Array ('sys_date', 'varchar2', 1),
			new Array ('ver_trans', 'varchar2', 1),
			new Array ('id_sam', 'varchar2', 1),
			new Array ('nt_icc', 'varchar2', 1),
			new Array ('sys_time', 'varchar2', 1),
			new Array ('vk_enc', 'varchar2', 1),
			new Array ('nt_sam', 'varchar2', 1),
			new Array ('state_trans', 'varchar2', 1),
			new Array ('err_tx', 'varchar2', 1)
		);
		var formRecPayment = new Array (
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('club_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('bal_acc_prv', 'varchar2', 1),
			new Array ('bal_disc_per_prv', 'varchar2', 1),
			new Array ('bal_cur_prv', 'varchar2', 1),
			new Array ('bal_bon_per_prv', 'varchar2', 1)
		);
		var formRecMovBon = new Array (
			new Array ('club_disc_prv', 'varchar2', 1),
			new Array ('club_bon_prv', 'varchar2', 1),
			new Array ('date_acc_prv', 'varchar2', 1),
			new Array ('date_calc_prv', 'varchar2', 1),
			new Array ('date_mov_prv', 'varchar2', 1),
			new Array ('bal_acc_prv', 'varchar2', 1),
			new Array ('bal_disc_per_prv', 'varchar2', 1),
			new Array ('bal_cur_prv', 'varchar2', 1),
			new Array ('bal_bon_per_prv', 'varchar2', 1),
			new Array ('bal_acc', 'varchar2', 1),
			new Array ('bal_cur', 'varchar2', 1),
			new Array ('bal_bon_per', 'varchar2', 1),
			new Array ('bal_disc_per', 'varchar2', 1)
		);
		var formRecChkCard = new Array (
			new Array ('club_st_prv', 'varchar2', 1),
			new Array ('club_disc_prv', 'varchar2', 1),
			new Array ('club_bon_prv', 'varchar2', 1),
			new Array ('date_acc_prv', 'varchar2', 1),
			new Array ('date_calc_prv', 'varchar2', 1),
			new Array ('date_mov_prv', 'varchar2', 1),
			new Array ('bal_acc_prv', 'varchar2', 1),
			new Array ('bal_disc_per_prv', 'varchar2', 1),
			new Array ('bal_cur_prv', 'varchar2', 1),
			new Array ('bal_bon_per_prv', 'varchar2', 1),
			new Array ('club_st', 'varchar2', 1),
			new Array ('club_disc', 'varchar2', 1),
			new Array ('club_bon', 'varchar2', 1),
			new Array ('date_acc', 'varchar2', 1),
			new Array ('date_mov', 'varchar2', 1),
			new Array ('bal_acc', 'varchar2', 1),
			new Array ('bal_cur', 'varchar2', 1),
			new Array ('bal_bon_per', 'varchar2', 1),
			new Array ('bal_disc_per', 'varchar2', 1)
		);
		var formRecInvalCard = new Array (
			new Array ('club_st_prv', 'varchar2', 1),
			new Array ('club_st', 'varchar2', 1)
		);
		var formRecStornoBon = new Array (
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('sum_pay_bon', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('bal_acc', 'varchar2', 1),
			new Array ('bal_cur', 'varchar2', 1),
			new Array ('bal_bon_per', 'varchar2', 1),
			new Array ('bal_disc_per', 'varchar2', 1)
		);
		var formRecPaymentIm = new Array (
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('club_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('bal_acc_prv', 'varchar2', 1),
			new Array ('bal_disc_per_prv', 'varchar2', 1),
			new Array ('bal_cur_prv', 'varchar2', 1),
			new Array ('bal_bon_per_prv', 'varchar2', 1),
			new Array ('imid', 'varchar2', 1),
			new Array ('specid', 'varchar2', 1),
			new Array ('clubcard', 'varchar2', 1),
			new Array ('cardid', 'varchar2', 1)
		);

		var formChangeUser = new Array (
			new Array ('changed_by_user', 'varchar2', 1),
			new Array ('user_who_has_changed', 'varchar2', 1),
			new Array ('date_last_users_changes', 'varchar2', 1),
			new Array ('bases_for_changes', 'varchar2', 1)
		);
		function myValidateForm(){
			var transType = document.getElementById('type_trans').value;
			if (transType == '1') return validateForm(formRecPayment);
			if (transType == '2') return validateForm(formRecMovBon);
			if (transType == '3') return validateForm(formRecChkCard);
			if (transType == '4') return validateForm(formRecInvalCard);
			if (transType == '5') return validateForm(formRecStornoBon);
			if (transType == '6') return validateForm(formRecPaymentIm);
		}	


		var elements = new Array();
		var titles = new Array();
		var curI = 0;
		var firstText = '<%=Bean.transactionXML.getFirstObligatoryText()%>';
		var lastText = '<%=Bean.transactionXML.getLastObligatoryText()%>';
		
		function disable(element_name){
			//window.alert('disable: '+element_name);
			var element = document.getElementById(element_name);
			var span_elem = document.getElementById('span_'+element_name);
			
			var found = 0;
			
			for (i in elements) {
				if (elements[i] == element.name) {
					found = 1;
					span_elem.innerHTML = titles[i];
				}
			}
			if (found == 0) {
				curI = curI + 1;
				elements[curI] = element.name;
				titles[curI] = span_elem.innerHTML;
				span_elem.innerHTML = titles[curI];
			}
	        
			element.value = '';
			element.className = 'inputfield-ro';
			element.readOnly = 1;
			element.disabled = 1;
			
		}
		function enable(element_name){
			//window.alert('enable: '+element_name);
			var element = document.getElementById(element_name);
			var span_elem = document.getElementById('span_'+element_name);
			
			var found = 0;
			
			for (i in elements) {
				if (elements[i] == element.name) {
					found = 1;
					span_elem.innerHTML = firstText + titles[i] + lastText;
				}
			}
			if (found == 0) {
				curI = curI + 1;
				elements[curI] = element.name;
				titles[curI] = span_elem.innerHTML;
				span_elem.innerHTML = firstText + titles[curI] + lastText;
			}
			element.className = 'inputfield';
			element.readOnly = 0;
			element.disabled = 0;
		}

		function disable_old(element){
			var found = 0;
			
			for (i in elements) {
				if (elements[i] == element.name) {
					found = 1;
					document.getElementById(elements[i]).innerHTML = titles[i];
				}
				curI = 1 + i*1 ;
	        }
			if (found == 0) {
				elements[curI] = element.name;
				titles[curI] = document.getElementById(element.name).innerHTML;
				document.getElementById(elements[curI]).innerHTML = titles[curI];
			}
	        
			element.value = '';
			element.className = 'inputfield-ro';
			element.readOnly = 1;
			element.disabled = 1;
			
		}
		function enable_old(element){
			var found = 0;
			
			for (i in elements) {
				if (elements[i] == element.name) {
					found = 1;
					document.getElementById(elements[i]).innerHTML = firstText + titles[i] + lastText;
				}
				curI = 1 + i*1 ;
	        }
			if (found == 0) {
				elements[curI] = element.name;
				titles[curI] = document.getElementById(element.name).innerHTML;
				document.getElementById(elements[curI]).innerHTML = firstText + titles[curI] + lastText;
			}
			element.className = 'inputfield';
			element.readOnly = 0;
			element.disabled = 0;
		}
		function setRecPayment(){
			enable('nt_ext');
			disable('club_st_prv');
			disable('date_acc_prv');
			enable('bal_acc_prv');
			enable('bal_disc_per_prv');
			disable('club_disc_prv');
			disable('date_calc_prv');
			enable('bal_cur_prv');
			enable('bal_bon_per_prv');
			disable('club_bon_prv');
			disable('date_mov_prv');
			enable('opr_sum');
			enable('club_sum');
			enable('sum_bon');
			disable('bal_acc');
			enable('sum_pay_cash');
			disable('club_st');
			enable('sum_disc');
			disable('bal_cur');
			enable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			disable('bal_bon_per');
			enable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			disable('bal_disc_per');
			enable('fl_ext_loyl');
			enable('cash_card_nr');
			enable('c_nr');
			enable('c_check_nr');
			disable('imid');
			disable('specid');
			disable('clubcard');
			disable('cardid');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_chk_title').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_inval_title').style.visibility = "hidden";
		}
		function setRecMovBon(){
			disable('nt_ext');
			disable('club_st_prv');
			enable('date_acc_prv');
			enable('bal_acc_prv');
			enable('bal_disc_per_prv');
			enable('club_disc_prv');
			enable('date_calc_prv');
			enable('bal_cur_prv');
			enable('bal_bon_per_prv');
			enable('club_bon_prv');
			enable('date_mov_prv');
			disable('opr_sum');
			disable('club_sum');
			disable('sum_bon');
			enable('bal_acc');
			disable('sum_pay_cash');
			disable('club_st');
			disable('sum_disc');
			enable('bal_cur');
			disable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			enable('bal_bon_per');
			disable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			enable('bal_disc_per');
			disable('fl_ext_loyl');
			disable('cash_card_nr');
			disable('c_nr');
			disable('c_check_nr');
			disable('imid');
			disable('specid');
			disable('clubcard');
			disable('cardid');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_chk_title').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_inval_title').style.visibility = "hidden";
		}
		function setRecChkCard(){
			disable('nt_ext');
			enable('club_st_prv');
			enable('date_acc_prv');
			enable('bal_acc_prv');
			enable('bal_disc_per_prv');
			enable('club_disc_prv');
			enable('date_calc_prv');
			enable('bal_cur_prv');
			enable('bal_bon_per_prv');
			enable('club_bon_prv');
			enable('date_mov_prv');
			disable('opr_sum');
			disable('club_sum');
			disable('sum_bon');
			enable('bal_acc');
			disable('sum_pay_cash');
			enable('club_st');
			disable('sum_disc');
			enable('bal_cur');
			disable('sum_pay_card');
			enable('club_disc');
			enable('date_acc');
			enable('bal_bon_per');
			disable('sum_pay_bon');
			enable('club_bon');
			enable('date_mov');
			enable('bal_disc_per');
			disable('fl_ext_loyl');
			disable('cash_card_nr');
			disable('c_nr');
			disable('c_check_nr');
			disable('imid');
			disable('specid');
			disable('clubcard');
			disable('cardid');
			document.getElementById('p_action_chk').style.visibility = "visible";
			document.getElementById('p_action_chk_title').style.visibility = "visible";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_inval_title').style.visibility = "hidden";
		}
		function setRecInvalCard(){
			disable('nt_ext');
			enable('club_st_prv');
			disable('date_acc_prv');
			disable('bal_acc_prv');
			disable('bal_disc_per_prv');
			disable('club_disc_prv');
			disable('date_calc_prv');
			disable('bal_cur_prv');
			disable('bal_bon_per_prv');
			disable('club_bon_prv');
			disable('date_mov_prv');
			disable('opr_sum');
			disable('club_sum');
			disable('sum_bon');
			disable('bal_acc');
			disable('sum_pay_cash');
			enable('club_st');
			disable('sum_disc');
			disable('bal_cur');
			disable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			disable('bal_bon_per');
			disable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			disable('bal_disc_per');
			disable('fl_ext_loyl');
			disable('cash_card_nr');
			disable('c_nr');
			disable('c_check_nr');
			disable('imid');
			disable('specid');
			disable('clubcard');
			disable('cardid');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_chk_title').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "visible";
			document.getElementById('p_action_inval_title').style.visibility = "visible";
		}
		function setRecStornoBon(){
			enable('nt_ext');
			disable('club_st_prv');
			disable('date_acc_prv');
			disable('bal_acc_prv');
			disable('bal_disc_per_prv');
			disable('club_disc_prv');
			disable('date_calc_prv');
			disable('bal_cur_prv');
			disable('bal_bon_per_prv');
			disable('club_bon_prv');
			disable('date_mov_prv');
			enable('opr_sum');
			disable('club_sum');
			enable('sum_bon');
			enable('bal_acc');
			enable('sum_pay_cash');
			disable('club_st');
			enable('sum_disc');
			enable('bal_cur');
			enable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			enable('bal_bon_per');
			enable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			enable('bal_disc_per');
			disable('fl_ext_loyl');
			disable('cash_card_nr');
			disable('c_nr');
			disable('c_check_nr');
			disable('imid');
			disable('specid');
			disable('clubcard');
			disable('cardid');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_chk_title').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_inval_title').style.visibility = "hidden";
		}
		function setRecPaymentIm(){
			enable('nt_ext');
			disable('club_st_prv');
			disable('date_acc_prv');
			enable('bal_acc_prv');
			enable('bal_disc_per_prv');
			disable('club_disc_prv');
			disable('date_calc_prv');
			enable('bal_cur_prv');
			enable('bal_bon_per_prv');
			disable('club_bon_prv');
			disable('date_mov_prv');
			enable('opr_sum');
			enable('club_sum');
			enable('sum_bon');
			disable('bal_acc');
			enable('sum_pay_cash');
			disable('club_st');
			enable('sum_disc');
			disable('bal_cur');
			enable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			disable('bal_bon_per');
			enable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			disable('bal_disc_per');
			enable('fl_ext_loyl');
			enable('cash_card_nr');
			enable('c_nr');
			enable('c_check_nr');
			enable('imid');
			enable('specid');
			enable('clubcard');
			enable('cardid');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_chk_title').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_inval_title').style.visibility = "hidden";
		}
		function checkTransType(){
			var transType = document.getElementById('type_trans').value;
			
			if (transType == '1') setRecPayment();
			if (transType == '2') setRecMovBon();
			if (transType == '3') setRecChkCard();
			if (transType == '4') setRecInvalCard();
			if (transType == '5') setRecStornoBon();
			if (transType == '6') setRecPaymentIm();
		}
		checkTransType();
	</script>

	<form action="../crm/cards/transactionsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	    <input type="hidden" name="action" value="edit">
	    <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>> 
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("id_trans", false) %></td> <td><input type="text" name="id_trans" id="id_trans" size="20" value="<%= transid %>" readonly="readonly" class="inputfield-ro"> </td>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("id_telgr", false) %>
				<%= Bean.getGoToTelegramLink(trans.getValue("ID_TELGR")) %>
			</td> <td><input type="text" name="id_telgr" id="id_telgr" size="20" value="<%= trans.getValue("ID_TELGR") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("type_trans_txt", true) %></td> <td><select name="type_trans" id="type_trans" class="inputfield" onchange="checkTransType()"><%= Bean.getTransTypeName(typeTrans) %></select></td>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("id_term", true) %>
				<%= Bean.getGoToTerminalLink(trans.getValue("ID_TERM")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindTerm("term", trans.getValue("ID_TERM"), "8") %>
			</td>			
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("state_trans_tsl", true) %></td> <td><select name="state_trans" id="state_trans" class="inputfield"><%= Bean.getTransStateOptions(trans.getValue("STATE_TRANS"), true) %></select></td>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("id_sam", true) %>
				<%= Bean.getGoToSAMLink(trans.getValue("ID_SAM")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindSAM("sam", trans.getValue("ID_SAM"), "8") %>
			</td>			
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("sys_date", true) %></td> <td><input type="text" name="sys_date" id="sys_date" size="20" value="<%= trans.getValue("SYS_DATE_FRMT") %>" class="inputfield" title="SUS_DATE + SYS_TIME"> </td>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("nt_sam", true) %></td> <td><input type="text" name="nt_sam" id="nt_sam" size="20" value="<%= trans.getValue("NT_SAM") %>" class="inputfield" title="NT_SAM"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("sys_time", true) %></td> <td><input type="text" name="sys_time" id="sys_time" size="20" value="<%= trans.getValue("SYS_TIME_FRMT") %>" class="inputfield" title="SUS_DATE + SYS_TIME"> </td>
			<td valign="top"><span id="span_nt_ext"><%= Bean.transactionXML.getfieldTransl("nt_ext", false) %></span></td><td><input type="text" name="nt_ext" id="nt_ext" size="20" value="<%= trans.getValue("NT_EXT") %>" class="inputfield" title="NT_EXT"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("ver_trans", true) %></td> <td><select name="ver_trans" id="ver_trans" class="inputfield" title="TEL_VERSION"><%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", trans.getValue("VER_TRANS"), false) %></select></td>
			<td valign="top"><%= Bean.getClubCardXMLFieldTransl("cd_card1", true) %>
				<%= Bean.getGoToClubCardLink(
						trans.getValue("CARD_SERIAL_NUMBER"),
						trans.getValue("ID_ISSUER"),
						trans.getValue("ID_PAYMENT_SYSTEM")
					) %>
			</td> 
			<td>
				<% String cd_card1 = Bean.getClubCardCode(trans.getValue("CARD_SERIAL_NUMBER")+"_"+trans.getValue("ID_ISSUER")+"_"+trans.getValue("ID_PAYMENT_SYSTEM")); %>
				<%=Bean.getWindowFindClubCard(cd_card1, trans.getValue("CARD_SERIAL_NUMBER"), trans.getValue("ID_ISSUER"), trans.getValue("ID_PAYMENT_SYSTEM"), "30") %>
			</td>			
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("vk_enc", true) %></td><td><select name="vk_enc" id="vk_enc" class="inputfield" title="VK_ENC"><%= Bean.getMeaningFromLookupNameOptions("VER_KEY", trans.getValue("VK_ENC"), false) %></select></td>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("nt_icc", true) %></td> <td> <input type="text" name="nt_icc" id="nt_icc" size="20" value="<%= trans.getValue("NT_ICC") %>" class="inputfield" title="NT_ICC"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("err_tx", true) %></td> <td><input type="text" name="err_tx" id="err_tx" size="20" value="<%= trans.getValue("ERR_TX") %> - <%= Bean.getSysERRTXDescription(trans.getValue("ERR_TX")) %>" class="inputfield" title="ERR_TX: <%= trans.getValue("ERR_TX") %> - <%= Bean.getSysERRTXDescription(trans.getValue("ERR_TX")) %>"> </td>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("cd_currency", true) %>
				<%= Bean.getGoToCurrencyLink(trans.getValue("CD_CURRENCY")) %>
			</td> 
			<td><select name="cd_currency" id="cd_currency" class="inputfield" title="CURR_PDA"><%= Bean.getCurrencyOptions(trans.getValue("CD_CURRENCY"), false) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("mac_icc", false) %></td> <td><input type="text" name="mac_icc" id="mac_icc" size="20" value="<%= trans.getValue("MAC_ICC") %>" class="inputfield" title="MAC_ICC"> </td>
			<td><span id="p_action_chk_title"><%= Bean.transactionXML.getfieldTransl("action", true) %></span></td><td  colspan="3">
	            <select name="p_action" id="p_action_chk" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("REC_CHK_CARD_ACTION", trans.getValue("ACTION"), false) %></select>
		    </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("mac_pda", false) %></td> <td><input type="text" name="mac_pda" id="mac_pda" size="20" value="<%= trans.getValue("MAC_PDA") %>" class="inputfield" title="MAC_PDA"> </td>
			<td><span id="p_action_inval_title"><%= Bean.transactionXML.getfieldTransl("action", true) %></span></td><td  colspan="3">
				<select name="p_action" id="p_action_inval" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("REC_INVAL_CARD_ACTION", trans.getValue("ACTION"), false) %></select>
		    </td>
		</tr>
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_before", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_st_prv"><%= Bean.transactionXML.getfieldTransl("club_st_prv", false) %></span></td> <td><select name="club_st_prv" id="club_st_prv" class="inputfield" title="CLUB_ST_PRV"><%= Bean.getClubCardStatusOptions(trans.getValue("CLUB_ST_PRV"), false) %></select></td>
			<td valign="top"><span id="span_club_bon_prv"><%= Bean.transactionXML.getfieldTransl("club_bon_prv", false) %></span></td><td><input type="text" name="club_bon_prv" id="club_bon_prv" size="20" value="<%= trans.getValue("CLUB_BON_PRV") %>" class="inputfield" title="CLUB_BON_PRV"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_bal_cur_prv"><%= Bean.transactionXML.getfieldTransl("bal_cur_prv", false) %></span></td><td><input type="text" name="bal_cur_prv" id="bal_cur_prv" size="20" value="<%= trans.getValue("BAL_CUR_PRV_FRMT") %>" class="inputfield" title="BAL_CUR_PRV"></td>
			<td valign="top"><span id="span_club_disc_prv"><%= Bean.transactionXML.getfieldTransl("club_disc_prv", false) %></span></td> <td><input type="text" name="club_disc_prv" id="club_disc_prv" size="20" value="<%= trans.getValue("CLUB_DISC_PRV") %>" class="inputfield" title="CLUB_DISC_PRV"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_bal_acc_prv"><%= Bean.transactionXML.getfieldTransl("bal_acc_prv", false) %></span></td><td><input type="text" name="bal_acc_prv" id="bal_acc_prv" size="20" value="<%= trans.getValue("BAL_ACC_PRV_FRMT") %>" class="inputfield" title="BAL_ACC_PRV"></td>
			<td valign="top"><span id="span_date_acc_prv"><%= Bean.transactionXML.getfieldTransl("date_acc_prv", false) %></span></td><td><%=Bean.getCalendarInputField("date_acc_prv", trans.getValue("DATE_ACC_PRV_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_bal_bon_per_prv"><%= Bean.transactionXML.getfieldTransl("bal_bon_per_prv", false) %></span></td> <td><input type="text" name="bal_bon_per_prv" id="bal_bon_per_prv" size="20" value="<%= trans.getValue("BAL_BON_PER_PRV_FRMT") %>" class="inputfield" title="BAL_BON_PER_PRV"> </td>
			<td valign="top"><span id="span_date_mov_prv"><%= Bean.transactionXML.getfieldTransl("date_mov_prv", false) %></span></td><td><%=Bean.getCalendarInputField("date_mov_prv", trans.getValue("DATE_MOV_PRV_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_bal_disc_per_prv"><%= Bean.transactionXML.getfieldTransl("bal_disc_per_prv", false) %></span></td> <td><input type="text" name="bal_disc_per_prv" id="bal_disc_per_prv" size="20" value="<%= trans.getValue("BAL_DISC_PER_PRV_FRMT") %>" class="inputfield" title="BAL_DISC_PER_PRV"> </td>
			<td valign="top"><span id="span_date_calc_prv"><%= Bean.transactionXML.getfieldTransl("date_calc_prv", false) %></span></td><td><%=Bean.getCalendarInputField("date_calc_prv", trans.getValue("DATE_CALC_PRV_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_operation", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_opr_sum"><%= Bean.transactionXML.getfieldTransl("opr_sum", false) %></span></td><td><input type="text" name="opr_sum" id="opr_sum" size="20" value="<%= trans.getValue("OPR_SUM_FRMT") %>" class="inputfield" title="OPR_SUM"></td>
			<td valign="top"><span id="span_sum_bon"><%= Bean.transactionXML.getfieldTransl("sum_bon", false) %></span></td><td><input type="text" name="sum_bon" id="sum_bon" size="20" value="<%= trans.getValue("SUM_BON_FRMT") %>" class="inputfield" title="SUM_BON"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_sum_pay_cash"><%= Bean.transactionXML.getfieldTransl("sum_pay_cash", false) %></span></td><td><input type="text" name="sum_pay_cash" id="sum_pay_cash" size="20" value="<%= trans.getValue("SUM_PAY_CASH_FRMT") %>" class="inputfield" title="SUM_PAY_CASH"></td>
			<td valign="top"><span id="span_sum_bon_cash"><%= Bean.transactionXML.getfieldTransl("sum_bon_cash", false) %></span></td><td><input type="text" name="sum_bon_cash" id="sum_bon_cash" size="20" value="<%= trans.getValue("SUM_BON_CASH_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SUM_BON_CASH"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_sum_pay_card"><%= Bean.transactionXML.getfieldTransl("sum_pay_card", false) %></span></td><td><input type="text" name="sum_pay_card" id="sum_pay_card" size="20" value="<%= trans.getValue("SUM_PAY_CARD_FRMT") %>" class="inputfield" title="SUM_PAY_CARD"> </td>
			<td valign="top"><span id="span_sum_bon_card"><%= Bean.transactionXML.getfieldTransl("sum_bon_card", false) %></span></td><td><input type="text" name="sum_bon_card" id="sum_bon_card" size="20" value="<%= trans.getValue("SUM_BON_CARD_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SUM_BON_CARD"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_sum_pay_bon"><%= Bean.transactionXML.getfieldTransl("sum_pay_bon", false) %></span></td><td><input type="text" name="sum_pay_bon" id="sum_pay_bon" size="20" value="<%= trans.getValue("SUM_PAY_BON_FRMT") %>" class="inputfield" title="SUM_PAY_BON"> </td>
			<td valign="top"><span id="span_sum_bon_bon"><%= Bean.transactionXML.getfieldTransl("sum_bon_bon", false) %></span></td><td><input type="text" name="sum_bon_bon" id="sum_bon_bon" size="20" value="<%= trans.getValue("SUM_BON_BON_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SUM_BON_BON"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_sum"><%= Bean.transactionXML.getfieldTransl("club_sum", false) %></span></td> <td><input type="text" name="club_sum" id="club_sum" size="20" value="<%= trans.getValue("CLUB_SUM_FRMT") %>" class="inputfield" title="CLUB_SUM"></td>
			<td valign="top"><span id="span_sum_disc"><%= Bean.transactionXML.getfieldTransl("sum_disc", false) %></span></td><td><input type="text" name="sum_disc" id="sum_disc" size="20" value="<%= trans.getValue("SUM_DISC_FRMT") %>" class="inputfield" title="SUM_DISC"></td>
		</tr>
		<tr>
			<td valign="top" colspan="2">&nbsp;</td>
			<td valign="top"><span id="span_sum_bon_disc"><%= Bean.transactionXML.getfieldTransl("sum_bon_disc", false) %></span></td><td><input type="text" name="sum_bon_disc" id="sum_bon_disc" size="20" value="<%= trans.getValue("SUM_BON_DISC_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SUM_BON_DISC"> </td>
		</tr>
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_after", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_st"><%= Bean.transactionXML.getfieldTransl("club_st", false) %></span></td> <td><select name="club_st" id="club_st" class="inputfield" title="CLUB_ST">
						<OPTION value="255" <% if ("255".equalsIgnoreCase(trans.getValue("CLUB_ST"))) {%>SELECTED<% } %>><%= Bean.transactionXML.getfieldTransl("option_blocked", false) %></OPTION>
						<%= Bean.getClubCardStatusOptions(trans.getValue("CLUB_ST"), false) %></select>
			</td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_disc"><%= Bean.transactionXML.getfieldTransl("club_disc", false) %></span></td><td><input type="text" name="club_disc" id="club_disc" size="20" value="<%= trans.getValue("CLUB_DISC_FRMT") %>" class="inputfield" title="CLUB_DISC"> </td>
			<td valign="top"><span id="span_bal_acc"><%= Bean.transactionXML.getfieldTransl("bal_acc", false) %></span></td> <td><input type="text" name="bal_acc" id="bal_acc" size="20" value="<%= trans.getValue("BAL_ACC_FRMT") %>" class="inputfield" title="BAL_ACC"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_bon"><%= Bean.transactionXML.getfieldTransl("club_bon", false) %></span></td><td><input type="text" name="club_bon" id="club_bon" size="20" value="<%= trans.getValue("CLUB_BON_FRMT") %>" class="inputfield" title="CLUB_BON"></td>
			<td valign="top"><span id="span_bal_cur"><%= Bean.transactionXML.getfieldTransl("bal_cur", false) %></span></td><td><input type="text" name="bal_cur" id="bal_cur" size="20" value="<%= trans.getValue("BAL_CUR_FRMT") %>" class="inputfield" title="BAL_CUR"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_date_acc"><%= Bean.transactionXML.getfieldTransl("date_acc", false) %></span></td><td><%=Bean.getCalendarInputField("date_acc", trans.getValue("DATE_ACC_FRMT"), "10") %></td>
			<td valign="top"><span id="span_bal_bon_per"><%= Bean.transactionXML.getfieldTransl("bal_bon_per", false) %></span></td><td><input type="text" name="bal_bon_per" id="bal_bon_per" size="20" value="<%= trans.getValue("BAL_BON_PER_FRMT") %>" class="inputfield" title="BAL_BON_PER"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_date_mov"><%= Bean.transactionXML.getfieldTransl("date_mov", false) %></span></td><td><%=Bean.getCalendarInputField("date_mov", trans.getValue("DATE_MOV_FRMT"), "10") %></td>
			<td valign="top"><span id="span_bal_disc_per"><%= Bean.transactionXML.getfieldTransl("bal_disc_per", false) %></span></td><td><input type="text" name="bal_disc_per" id="bal_disc_per" size="20" value="<%= trans.getValue("BAL_DISC_PER_FRMT") %>" class="inputfield" title="BAL_DISC_PRV"> </td>
		</tr>
		
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_im", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_imid"><%= Bean.transactionXML.getfieldTransl("imid", false) %></span></td><td><input type="text" name="imid" id="imid" size="20" value="<%= trans.getValue("IMID") %>" class="inputfield" title="IMID"></td>
			<td valign="top"><span id="span_cardid"><%= Bean.transactionXML.getfieldTransl("cardid", false) %></span></td><td><input type="text" name="cardid" id="cardid" size="20" value="<%= trans.getValue("CARDID") %>" class="inputfield" title="CARDID"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_specid"><%= Bean.transactionXML.getfieldTransl("specid", false) %></span></td><td><input type="text" name="specid" id="specid" size="20" value="<%= trans.getValue("SPECID") %>" class="inputfield" title="SPECID"></td>
			<td valign="top"><span id="span_clubcard"><%= Bean.transactionXML.getfieldTransl("clubcard", false) %></span></td> <td><select name="clubcard" id="clubcard" class="inputfield" title="CLUBCARD"><option value=""></option><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", trans.getValue("CLUBCARD"), false) %></select></td>
		</tr>
		
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_check", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_fl_ext_loyl"><%= Bean.transactionXML.getfieldTransl("fl_ext_loyl", false) %></span></td> <td><select name="fl_ext_loyl" id="fl_ext_loyl" class="inputfield" title="FL_EXT_LOYL"><option value=""></option><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", trans.getValue("FL_EXT_LOYL"), false) %></select></td>
			<td valign="top"><span id="span_c_nr"><%= Bean.transactionXML.getfieldTransl("c_nr", false) %></span></td><td><input type="text" name="c_nr" id="c_nr" size="20" value="<%= trans.getValue("C_NR") %>" class="inputfield" title="C_NR"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_cash_card_nr"><%= Bean.transactionXML.getfieldTransl("cash_card_nr", false) %></span></td><td><input type="text" name="cash_card_nr" id="cash_card_nr" size="20" value="<%= trans.getValue("CASH_CARD_NR") %>" class="inputfield" title="CASH_CARD_NR"></td>
			<td valign="top"><span id="span_c_check_nr"><%= Bean.transactionXML.getfieldTransl("c_check_nr", false) %></span></td><td><input type="text" name="c_check_nr" id="c_check_nr" size="20" value="<%= trans.getValue("C_CHECK_NR") %>" class="inputfield" title="C_CHECK_NR"></td>
		</tr>
		
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_rec_payment_ext_param", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("c_nomenkl", false) %></td>
			<td  colspan="3">
				<input type="text" name="c_nomenkl" size="74" value="<%= trans.getValue("C_NOMENKL") %>" class="inputfield">
				<% if ("7".equalsIgnoreCase(trans.getValue("TYPE_TRANS"))) {%>
					<br><%= trans.getNomenklatureHTML() %>
				<% } %>
			</td>
		</tr>

		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_result", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("id_trans_reversed", false) %>
				<%= Bean.getGoToTransactionLink(trans.getValue("ID_TRANS_REVERSED")) %>
			</td> 
				<td><input type="text" name="id_trans_reversed" id="id_trans_reversed" size="20" value="<%= trans.getValue("ID_TRANS_REVERSED") %>" class="inputfield"> </td>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("id_trans_double", false) %>
				<%= Bean.getGoToTransactionLink(trans.getValue("ID_TRANS_DOUBLE")) %>
			</td>
				<td><input type="text" name="id_trans_double" id="id_trans_double" size="20" value="<%= trans.getValue("ID_TRANS_DOUBLE") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("is_posting", false) %></td> <td><select name="is_posting" id="is_posting" class="inputfield"><%= Bean.getMeaningForNumValue("ACCOUNTS_POSTING_TRANS_STATE", trans.getValue("IS_POSTING")) %></select></td>
		</tr>
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_user_changing", false) %></b></td>
		</tr>
		<tr>
			
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("user_who_has_changed", false) %>
				<input type="hidden" name="changed_by_user" id="changed_by_user" value="Y">
			</td>
			<td valign="top">
				<%=Bean.getWindowFindUser("user", trans.getValue("USER_WHO_HAS_CHANGED"), Bean.getSystemUserName(trans.getValue("USER_WHO_HAS_CHANGED")), "10") %>
			</td>

			<td valign="top"><%= Bean.transactionXML.getfieldTransl("date_last_users_changes", false) %></td><td valign="top"><%=Bean.getCalendarInputField("date_last_users_changes", trans.getValue("DATE_LAST_USERS_CHANGES_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("bases_for_changes", false) %></td><td  colspan="3"><textarea name="bases_for_changes" id="bases_for_changes" cols="70" rows="3" class="inputfield"><%= trans.getValue("BASES_FOR_CHANGES") %></textarea></td>
		</tr>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/transactionsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/cards/transactions.jsp") %>
			</td>
		</tr>
	</table>
	</form>

	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("date_acc_prv", false) %>
	<%= Bean.getCalendarScript("date_calc_prv", false) %>
	<%= Bean.getCalendarScript("date_mov_prv", false) %>
	<%= Bean.getCalendarScript("date_acc", false) %>
	<%= Bean.getCalendarScript("date_mov", false) %>
	<%= Bean.getCalendarScript("date_last_users_changes", false) %>

 <%
  } else if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_INFO")) {
	  %>
		<form>
		<table <%=Bean.getTableDetailParam() %>> 
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("id_trans", false) %></td> <td><input type="text" name="id_trans" id="id_trans" size="20" value="<%= transid %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(trans.getValue("ID_CLUB")) %>
				</td>
				<td>
					<input type="text" name="name_club" size="35" value="<%= Bean.getClubShortName(trans.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
				</td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("id_telgr", false) %>
					<%= Bean.getGoToTelegramLink(trans.getValue("ID_TELGR")) %>
				</td> <td><input type="text" name="id_telgr" id="id_telgr" size="20" value="<%= trans.getValue("ID_TELGR") %>" readonly="readonly" class="inputfield-ro" title="CARD_NR"> </td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("type_trans_txt", false) %></td> <td><input type="text" name="type_trans_txt" size="35" value="<%= trans.getValue("TYPE_TRANS_TXT") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_term", false) %>
					<%= Bean.getGoToTerminalLink(trans.getValue("ID_TERM")) %>
				</td> 
				<td><input type="text" name="id_term" size="20" value="<%= trans.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro" title="ID_TERM"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("state_trans_tsl", false) %></td> <td><input type="text" name="state_trans_tsl" size="35" value="<%= trans.getValue("STATE_TRANS_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_sam", false) %>
					<%= Bean.getGoToSAMLink(trans.getValue("ID_SAM")) %>
				</td> 
				<td><input type="text" name="id_sam" size="20" value="<%= trans.getValue("ID_SAM") %>" readonly="readonly" class="inputfield-ro" title="ID_SAM"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sys_date", false) %></td> <td><input type="text" name="sys_date" size="20" value="<%= trans.getValue("SYS_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SYS_DATE"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("nt_sam", false) %></td> <td><input type="text" name="nt_sam" size="20" value="<%= trans.getValue("NT_SAM") %>" readonly="readonly" class="inputfield-ro" title="NT_SAM"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sys_time", false) %></td> <td><input type="text" name="sys_time" size="20" value="<%= trans.getValue("SYS_TIME_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SYS_TIME"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("nt_ext", false) %></td><td><input type="text" name="nt_ext" size="20" value="<%= trans.getValue("NT_EXT") %>" readonly="readonly" class="inputfield-ro" title="NT_EXT"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("ver_trans", false) %></td> <td><input type="text" name="ver_trans" size="20" value="<%= trans.getValue("VER_TRANS") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("clubcard", false) %></td> <td><input type="text" name="clubcard" size="20" value="<%= Bean.getMeaningForNumValue("YES_NO", trans.getValue("CLUBCARD")) %>" readonly="readonly" class="inputfield-ro" title="CLUBCARD"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("vk_enc", false) %></td><td><input type="text" name="vk_b" size="20" value="<%= trans.getValue("VK_ENC") %>" readonly="readonly" class="inputfield-ro" title="VK_ENC"></td>
				<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						trans.getValue("CARD_SERIAL_NUMBER"),
						trans.getValue("ID_ISSUER"),
						trans.getValue("ID_PAYMENT_SYSTEM")
					) %>
				</td> 
				<td><input type="text" name="cd_card" id="cd_card" size="20" value="<%= trans.getValue("CARD_SERIAL_NUMBER") %>" readonly="readonly" class="inputfield-ro" title="CARD_NR"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("err_tx", false) %></td> <td><input type="text" name="err_tx" size="35" value="<%= trans.getValue("ERR_TX") %> - <%= Bean.getSysERRTXDescription(trans.getValue("ERR_TX")) %>" readonly="readonly" class="inputfield-ro" title="ERR_TX: <%= trans.getValue("ERR_TX") %> - <%= Bean.getSysERRTXDescription(trans.getValue("ERR_TX")) %>"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_issuer", false) %>
					<%= Bean.getGoToJurPrsHyperLink(trans.getValue("ID_ISSUER")) %>
				</td> <td> <input type="text" name="name_issuer" size="35" value="<%= Bean.getJurPersonShortName(trans.getValue("ID_ISSUER")) %>" readonly="readonly" class="inputfield-ro" title="ID_ISSUER"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("mac_icc", false) %></td> <td><input type="text" name="mac_icc" size="20" value="<%= trans.getValue("MAC_ICC") %>" readonly="readonly" class="inputfield-ro" title="MAC_ICC"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_payment_system", false) %> </td> <td> <input type="text" name="name_payment_system" size="35" value="<%= Bean.getPaymentSystemName(trans.getValue("ID_PAYMENT_SYSTEM")) %>" readonly="readonly" class="inputfield-ro" title="ID_PAYMENT_SYSTEM"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("mac_pda", false) %></td> <td><input type="text" name="mac_pda" size="20" value="<%= trans.getValue("MAC_PDA") %>" readonly="readonly" class="inputfield-ro" title="MAC_PDA"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("nt_icc", false) %> </td> <td> <input type="text" name="nt_icc" size="20" value="<%= trans.getValue("NT_ICC") %>" readonly="readonly" class="inputfield-ro" title="NT_ICC"> </td>
			</tr>
			<tr>
				<% if ("3".equalsIgnoreCase(typeTrans)) { %>
					<td><%= Bean.transactionXML.getfieldTransl("action", false) %></td><td><input type="text" name="p_action" id="p_action" size="20" value="<%= Bean.getMeaningFoCodeValue("REC_CHK_CARD_ACTION", trans.getValue("ACTION")) %>" readonly="readonly" class="inputfield-ro" title="ACTION"></td>
				<% } else if ("4".equalsIgnoreCase(typeTrans)) { %>
					<td><%= Bean.transactionXML.getfieldTransl("action", false) %></td><td><input type="text" name="p_action" id="p_action" size="20" value="<%= Bean.getMeaningFoCodeValue("REC_INVAL_CARD_ACTION", trans.getValue("ACTION")) %>" readonly="readonly" class="inputfield-ro" title="ACTION"></td>
				<% } else { %>
					<td><input type="hidden" id="p_action" name="p_action" value="">&nbsp;</td>
					<td>&nbsp;</td>
				<% } %>
				<td><%= Bean.transactionXML.getfieldTransl("cd_currency", false) %>
					<%= Bean.getGoToCurrencyLink(trans.getValue("CD_CURRENCY")) %>
				</td>
				<td><input type="text" name="cd_currency" size="20" value="<%= trans.getValue("CD_CURRENCY") %>" readonly="readonly" class="inputfield-ro" title="CURR_PDA"> </td>
			</tr>
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_before", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_st_prv", false) %></td> <td><input type="text" name="club_st_prv" size="20" value="<%= trans.getValue("CLUB_ST_PRV") %>" readonly="readonly" class="inputfield-ro" title="CLUB_ST_PRV"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("club_bon_prv", false) %></td><td><input type="text" name="club_bon_prv" size="20" value="<%= trans.getValue("CLUB_BON_PRV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="CLUB_BON_PRV"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("bal_acc_prv", false) %></td><td><input type="text" name="bal_acc_prv" size="20" value="<%= trans.getValue("BAL_ACC_PRV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BAL_ACC_PRV"></td>
				<td><%= Bean.transactionXML.getfieldTransl("club_disc_prv", false) %></td> <td><input type="text" name="club_disc_prv" size="20" value="<%= trans.getValue("CLUB_DISC_PRV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="CLUB_DISC_PRV"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("bal_cur_prv", false) %></td><td><input type="text" name="bal_cur_prv" size="20" value="<%= trans.getValue("BAL_CUR_PRV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BAL_CUR_PRV"></td>
				<td><%= Bean.transactionXML.getfieldTransl("date_acc_prv", false) %></td><td ><input type="text" name="date_acc_prv" size="20" value="<%= trans.getValue("DATE_ACC_PRV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="DATE_ACC_PRV"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("bal_bon_per_prv", false) %></td> <td><input type="text" name="bal_per_prv" size="20" value="<%= trans.getValue("BAL_BON_PER_PRV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BAL_BON_PER_PRV"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("date_mov_prv", false) %></td> <td><input type="text" name="date_mov_prv" size="20" value="<%= trans.getValue("DATE_MOV_PRV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="DATE_MOV_PRV"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("bal_disc_per_prv", false) %></td> <td><input type="text" name="bal_disc_prv" size="20" value="<%= trans.getValue("BAL_DISC_PER_PRV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BAL_DISC_PER_PRV"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("date_calc_prv", false) %></td> <td><input type="text" name="date_calc_prv" size="20" value="<%= trans.getValue("DATE_CALC_PRV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="DATE_CALC_PRV"> </td>
			</tr>
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_operation", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("opr_sum", false) %></td><td><input type="text" name="opr_sum" size="20" value="<%= trans.getValue("OPR_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro" title="OPR_SUM"></td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon", false) %></td><td><input type="text" name="sum_bon" size="20" value="<%= trans.getValue("SUM_BON_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SUM_BON"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sum_pay_cash", false) %></td><td><input type="text" name="sum_pay_cash" size="20" value="<%= trans.getValue("SUM_PAY_CASH_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SUM_PAY_CASH"></td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon_cash", false) %></td> <td><input type="text" name="sum_bon_cash" size="20" value="<%= trans.getValue("SUM_BON_CASH_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sum_pay_card", false) %></td> <td><input type="text" name="sum_pay_card" size="20" value="<%= trans.getValue("SUM_PAY_CARD_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SUM_PAY_CARD"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon_card", false) %></td> <td><input type="text" name="sum_bon_card" size="20" value="<%= trans.getValue("SUM_BON_CARD_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sum_pay_bon", false) %></td> <td><input type="text" name="sum_pay_bon" size="20" value="<%= trans.getValue("SUM_PAY_BON_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SUM_PAY_BON"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon_bon", false) %></td> <td><input type="text" name="sum_bon_bon" size="20" value="<%= trans.getValue("SUM_BON_BON_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_sum", false) %></td> <td><input type="text" name="club_sum" size="20" value="<%= trans.getValue("CLUB_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro" title="CLUB_SUM"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_disc", false) %></td><td><input type="text" name="sum_disc" size="20" value="<%= trans.getValue("SUM_DISC_FRMT") %>" readonly="readonly" class="inputfield-ro" title="SUM_DISC"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon_disc", false) %></td> <td><input type="text" name="sum_bon_disc" size="20" value="<%= trans.getValue("SUM_BON_DISC_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_after", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_st_prv", false) %></td> <td><input type="text" name="club_st_prv" size="20" value="<%= trans.getValue("CLUB_ST") %>" readonly="readonly" class="inputfield-ro" title="CLUB_ST"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_bon", false) %></td><td><input type="text" name="club_bon" size="20" value="<%= trans.getValue("CLUB_BON_FRMT") %>" readonly="readonly" class="inputfield-ro" title="CLUB_BON"></td>
				<td><%= Bean.transactionXML.getfieldTransl("bal_acc", false) %></td> <td><input type="text" name="bal_acc" size="20" value="<%= trans.getValue("BAL_ACC_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BAL_ACC"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_disc", false) %></td> <td><input type="text" name="club_disc" size="20" value="<%= trans.getValue("CLUB_DISC_FRMT") %>" readonly="readonly" class="inputfield-ro" title="CLUB_DISC"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("bal_cur", false) %></td><td><input type="text" name="bal_cur" size="20" value="<%= trans.getValue("BAL_CUR_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BAL_CUR"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("date_acc", false) %></td><td><input type="text" name="date_acc" size="20" value="<%= trans.getValue("DATE_ACC_FRMT") %>" readonly="readonly" class="inputfield-ro" title="DATE_ACC"></td>
				<td><%= Bean.transactionXML.getfieldTransl("bal_bon_per", false) %></td><td><input type="text" name="bal_per" size="20" value="<%= trans.getValue("BAL_BON_PER_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BAL_BON_PER"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("date_mov", false) %></td> <td><input type="text" name="date_mov" size="20" value="<%= trans.getValue("DATE_MOV_FRMT") %>" readonly="readonly" class="inputfield-ro" title="DATE_MOV"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("bal_disc_per", false) %></td> <td><input type="text" name="bal_disc" size="20" value="<%= trans.getValue("BAL_DISC_PER_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BAL_DISC_PER"> </td>
			</tr>
		
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_im", false) %></b></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("imid", false) %></td><td><input type="text" name="imid" size="20" value="<%= trans.getValue("IMID") %>" readonly="readonly" class="inputfield-ro" title="IMID"></td>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("specid", false) %></td><td><input type="text" name="specid" size="20" value="<%= trans.getValue("SPECID") %>" readonly="readonly" class="inputfield-ro" title="SPECID"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("cardid", false) %></td><td><input type="text" name="cardid" size="20" value="<%= trans.getValue("CARDID") %>" readonly="readonly" class="inputfield-ro" title="CARDID"></td>
				<td valign="top" colspan="2">&nbsp;</td>
			</tr>
		
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_check", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("fl_ext_loyl", false) %></td> <td><input type="text" name="fl_ext_loyl" size="20" value="<%= Bean.getMeaningForNumValue("YES_NO", trans.getValue("FL_EXT_LOYL")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("c_nr", false) %></td><td><input type="text" name="c_nr" size="20" value="<%= trans.getValue("C_NR") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("cash_card_nr", false) %></td><td><input type="text" name="cash_card_nr" size="20" value="<%= trans.getValue("CASH_CARD_NR") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.transactionXML.getfieldTransl("c_check_nr", false) %></td><td><input type="text" name="c_check_nr" size="20" value="<%= trans.getValue("C_CHECK_NR") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
		
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_rec_payment_ext_param", false) %></b></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("c_nomenkl", false) %></td>
				<td  colspan="3">
					<input type="text" name="c_nomenkl" size="74" value="<%= trans.getValue("C_NOMENKL") %>" readonly="readonly" class="inputfield-ro">
					<% if ("7".equalsIgnoreCase(trans.getValue("TYPE_TRANS"))) {%>
						<br><span title="Товар; значение; № чека"><%= trans.getNomenklatureHTML() %></span>
					<% } %>
				</td>
			</tr>

			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_result", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("id_trans_reversed", false) %>
					<%= Bean.getGoToTransactionLink(trans.getValue("ID_TRANS_REVERSED")) %>
				</td> 
					<td><input type="text" name="id_trans_reversed" size="20" value="<%= trans.getValue("ID_TRANS_REVERSED") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_trans_double", false) %>
					<%= Bean.getGoToTransactionLink(trans.getValue("ID_TRANS_DOUBLE")) %>
				</td>
				<td><input type="text" name="id_trans_double" size="20" value="<%= trans.getValue("ID_TRANS_DOUBLE") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("is_posting", false) %></td> <td><input type="text" name="is_posting" size="20" value="<%= Bean.getMeaningForNumValue("ACCOUNTS_POSTING_TRANS_STATE", trans.getValue("IS_POSTING")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td>&nbsp;</td> <td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_user_changing", false) %></b></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("user_who_has_changed", false) %></td> <td valign="top"><input type="text" name="user_who_has_changed" size="20" value="<%= Bean.getSystemUserName(trans.getValue("USER_WHO_HAS_CHANGED")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("date_last_users_changes", false) %></td><td valign="top"><input type="text" name="date_last_users_changes" size="20" value="<%= trans.getValue("DATE_LAST_USERS_CHANGES_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("bases_for_changes", false) %></td><td  colspan="3"><textarea name="bases_for_changes" cols="70" rows="3" readonly="readonly" class="inputfield-ro"><%= trans.getValue("BASES_FOR_CHANGES") %></textarea></td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					trans.getValue(Bean.getCreationDateFieldName()),
					trans.getValue("CREATED_BY"),
					trans.getValue(Bean.getLastUpdateDateFieldName()),
					trans.getValue("LAST_UPDATE_BY")
			) %>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getGoBackButton("../crm/cards/transactions.jsp") %>
				</td>
			</tr>
		</table>
		</form>
	 

	 <% 
  }
  }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_ADDITIONAL")) {
	%> 
		<table <%=Bean.getTableDetailParam() %>> 
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("terminal_manufacturer", false) %>
				<%= Bean.getGoToJurPrsHyperLink(trans.getValue("ID_TERMINAL_MANUFACTURER")) %>
				</td> <td><input type="text" name="id_terminal_manufacturer" id="id_terminal_manufacturer" size="50" value="<%= Bean.getJurPersonShortName(trans.getValue("ID_TERMINAL_MANUFACTURER")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						trans.getValue("CARD_SERIAL_NUMBER"),
						trans.getValue("ID_ISSUER"),
						trans.getValue("ID_PAYMENT_SYSTEM")
					) %>
				</td> 
				<td><input type="text" name="cd_card" id="cd_card" size="40" value="<%= trans.getValue("CARD_SERIAL_NUMBER") %>" readonly="readonly" class="inputfield-ro" title="CARD_NR"> </td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("term_owner", false) %>
				<%= Bean.getGoToJurPrsHyperLink(trans.getValue("ID_TERM_OWNER")) %>
				</td> <td><input type="text" name="id_term_owner" id="id_term_owner" size="50" value="<%= Bean.getJurPersonShortName(trans.getValue("ID_TERM_OWNER")) %>" readonly="readonly" class="inputfield-ro"> </td>

				<td><%= Bean.transactionXML.getfieldTransl("id_issuer", false) %>
				<%= Bean.getGoToJurPrsHyperLink(trans.getValue("ID_ISSUER")) %>
				</td> <td><input type="text" name="id_issuer" id="id_issuer" size="40" value="<%= Bean.getJurPersonShortName(trans.getValue("ID_ISSUER")) %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("name_finance_acquirer", false) %>
				<%= Bean.getGoToJurPrsHyperLink(trans.getValue("ID_FINANCE_ACQUIRER")) %>
				</td> <td><input type="text" name="id_finance_acquirer" id="id_finance_acquirer" size="50" value="<%= Bean.getJurPersonShortName(trans.getValue("ID_FINANCE_ACQUIRER")) %>" readonly="readonly" class="inputfield-ro"> </td>

				<td><%= Bean.transactionXML.getfieldTransl("id_card_status", false) %></td> <td><input type="text" name="id_card_status" size="40" value="<%= Bean.getCardStatusName(trans.getValue("ID_CARD_STATUS")) %>" readonly="readonly" class="inputfield-ro" title="SUM_PAY_BON"> </td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("id_technical_acquirer", false) %>
				<%= Bean.getGoToJurPrsHyperLink(trans.getValue("ID_TECHNICAL_ACQUIRER")) %>
				</td> <td><input type="text" name="id_technical_acquirer" id="id_technical_acquirer" size="50" value="<%= Bean.getJurPersonShortName(trans.getValue("ID_TECHNICAL_ACQUIRER")) %>" readonly="readonly" class="inputfield-ro"> </td>

				<td><%= Bean.transactionXML.getfieldTransl("id_bon_category", false) %></td> <td><input type="text" name="id_bon_category" size="40" value="<%= Bean.getCardCategoryName2(trans.getValue("ID_BON_CATEGORY")) %>" readonly="readonly" class="inputfield-ro" title="SUM_PAY_BON"> </td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("dealer", false) %>
					<%= Bean.getGoToJurPrsHyperLink(trans.getValue("ID_DEALER")) %>
				</td> <td><input type="text" name="id_dealer" id="id_dealer" size="50" value="<%= Bean.getJurPersonShortName(trans.getValue("ID_DEALER")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_disc_category", false) %></td> <td><input type="text" name="id_disc_category" size="40" value="<%= Bean.getCardCategoryName2(trans.getValue("ID_DISC_CATEGORY")) %>" readonly="readonly" class="inputfield-ro" title="SUM_PAY_BON"> </td>
			</tr>
		</table>
		<br><%= Bean.transactionXML.getfieldTransl("h_relationships", false) %><br>
		<%=trans.getClubRelationshipsHTML() %>

		<% if (!(trans.getValue("ID_LOYALITY_HISTORY")==null || "".equalsIgnoreCase(trans.getValue("ID_LOYALITY_HISTORY")))) {%>
			<% bcTerminalLoyalityObject loyHist = new bcTerminalLoyalityObject(trans.getValue("ID_LOYALITY_HISTORY")); %>
			
			<br><%= Bean.transactionXML.getfieldTransl("h_loyality_param", false) %><br>
			<table <%=Bean.getTableDetailParam() %>> 
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("TYPE_CALC_TSL", false) %></td><td><input type="text" name="type_calc_name" size="50" value="<%= loyHist.getValue("TYPE_CALC_NAME_F") %>" readonly="readonly" class="inputfield-ro" title="TYPE_CALC"></td>
					<td>&nbsp;</td> <td>&nbsp;</td>
				</tr>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("MAX_BONUS", false) %></td> <td><input type="text" name="max_bonus" size="20" value="<%= loyHist.getValue("MAX_BONUS_FRMT") %>" readonly="readonly" class="inputfield-ro" title="MAX_BONUS"> </td>
					<td><%= Bean.terminalXML.getfieldTransl("CASH_BON_TSL", false) %></td><td><input type="text" name="cash_bon_name" size="20" value="<%= loyHist.getValue("CASH_BON_NAME_F") %>" readonly="readonly" class="inputfield-ro" title="CASH_BON"></td>
				</tr>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("MIN_AMOUNT", false) %></td> <td><input type="text" name="min_amount" size="20" value="<%= loyHist.getValue("MIN_AMOUNT_FRMT") %>" readonly="readonly" class="inputfield-ro" title="MIN_AMOUNT"> </td>
					<td><%= Bean.terminalXML.getfieldTransl("BON_BON", false) %></td> <td><input type="text" name="bon_bon" size="20" value="<%= loyHist.getValue("BON_BON_TSL_F") %>" readonly="readonly" class="inputfield-ro" title="BON_BON"> </td>
				</tr>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("MAX_SUMPAYOFFLINE", false) %></td> <td><input type="text" name="max_sumpayoffline" size="20" value="<%= loyHist.getValue("MAX_SUMPAYOFFLINE_FRMT") %>" readonly="readonly" class="inputfield-ro" title="MAX_SUMPAYOFFLINE"> </td>
					<td><%= Bean.terminalXML.getfieldTransl("ROUNDING_RULE_TSL", false) %></td><td><input type="text" name="rounding_rule_name" size="20" value="<%= loyHist.getValue("ROUNDING_RULE_NAME_FULL") %>" readonly="readonly" class="inputfield-ro" title="ROUNDING_RULE"></td>
				</tr>
				<tr>
					<td><%= Bean.terminalXML.getfieldTransl("MAX_SUMPAYNOPIN", false) %></td> <td><input type="text" name="max_sumpaynopin" size="20" value="<%= loyHist.getValue("MAX_SUMPAYNOPIN_FRMT") %>" readonly="readonly" class="inputfield-ro" title="MAX_SUMPAYNOPIN"> </td>
					<td><%= Bean.loyXML.getfieldTransl("term_loyality_for_all_nsmep", false) %></td> <td> <input type="text" name="loyality_for_all_nsmep" size="20" value="<%= loyHist.getValue("LOYALITY_FOR_ALL_NSMEP_TSL_F") %>" readonly="readonly" class="inputfield-ro" title="MAX_DATE_ONL_TEMR"> </td>
				</tr>
				<tr>
					<td><%= Bean.loyXML.getfieldTransl("term_max_date_onl_term", false) %> </td> <td><input type="text" name="MAX_DATE_ONL_TERM" size="20" value="<%= loyHist.getValue("MAX_DATE_ONL_TERM") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.terminalXML.getfieldTransl("EXT_LOYL", false) %></td> <td><input type="text" name="ext_loyl" size="20" value="<%= loyHist.getValue("EXT_LOYL_TSL_F") %>" readonly="readonly" class="inputfield-ro" title="EXT_LOYL"> </td>
				</tr>
				<tr>
					<td><%= Bean.loyXML.getfieldTransl("term_limit_cash", false) %></td> <td><input type="text" name="limit_cash" size="20" value="<%= loyHist.getValue("LIMIT_CASH") %>" readonly="readonly" class="inputfield-ro" title="TR_LIMIT"> </td>
					<td>&nbsp;</td> <td>&nbsp;</td>
				</tr>
			</table>
		<% } %>

		<% if (!(trans.getValue("ID_LOYALITY_BON_LINE")==null || "".equalsIgnoreCase(trans.getValue("ID_LOYALITY_BON_LINE")))) {%>
			<% bcTerminalLoyLineHistoryObject loyBon = new bcTerminalLoyLineHistoryObject(trans.getValue("ID_LOYALITY_BON_LINE")); %>
			
			<br><%= Bean.transactionXML.getfieldTransl("h_loyality_bon", false) %><br>
			<table <%=Bean.getTableDetailParam() %>> 
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("NAME_CATEGORY", false) %></td><td><input type="text" name="name_category" size="30" value="<%= loyBon.getValue("NAME_CATEGORY") %>" readonly="readonly" class="inputfield-ro" title="TYPE_CALC"></td>
					<td>&nbsp;</td> <td>&nbsp;</td>
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("bon_percent_value", false) %></td><td><input type="text" name="bon_percent_value" size="20" value="<%= loyBon.getValue("BON_PERCENT_VALUE_PERCENT") %>" readonly="readonly" class="inputfield-ro" title="CASH_BON"></td>
					<td><%= Bean.loylineXML.getfieldTransl("max_bon_st", false) %></td> <td><input type="text" name="max_bon_st" size="20" value="<%= loyBon.getValue("MAX_BON_ST_FRMT") %>" readonly="readonly" class="inputfield-ro" title="MAX_BONUS"> </td>
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("bon_fixed_value", false) %></td> <td><input type="text" name="bon_fixed_value" size="20" value="<%= loyBon.getValue("BON_FIXED_VALUE_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BON_BON"> </td>
					<td><%= Bean.loylineXML.getfieldTransl("bonus_transfer_term", false) %></td> <td><input type="text" name="bonus_transfer_term" size="20" value="<%= loyBon.getValue("BONUS_TRANSFER_TERM") %>" readonly="readonly" class="inputfield-ro" title="MIN_AMOUNT"> </td>
				</tr>
				<tr>
					<td>&nbsp;</td> <td>&nbsp;</td>
					<td><%= Bean.loylineXML.getfieldTransl("bonus_calc_term", false) %></td> <td><input type="text" name="bonus_calc_term" size="20" value="<%= loyBon.getValue("BONUS_CALC_TERM") %>" readonly="readonly" class="inputfield-ro" title="MIN_AMOUNT"> </td>
				</tr>
			</table>
		<% } %>

		<% if (!(trans.getValue("ID_LOYALITY_DISC_LINE")==null || "".equalsIgnoreCase(trans.getValue("ID_LOYALITY_DISC_LINE")))) {%>
			<% bcTerminalLoyLineHistoryObject loyDisc = new bcTerminalLoyLineHistoryObject(trans.getValue("ID_LOYALITY_DISC_LINE")); %>
			
			<br><%= Bean.transactionXML.getfieldTransl("h_loyality_disc", false) %><br>
			<table <%=Bean.getTableDetailParam() %>> 
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("NAME_CATEGORY", false) %></td><td><input type="text" name="name_category" size="30" value="<%= loyDisc.getValue("NAME_CATEGORY") %>" readonly="readonly" class="inputfield-ro" title="TYPE_CALC"></td>
					<td>&nbsp;</td> <td>&nbsp;</td>
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("disc_percent_value", false) %></td><td><input type="text" name="disc_percent_value" size="20" value="<%= loyDisc.getValue("DISC_PERCENT_VALUE_PERCENT") %>" readonly="readonly" class="inputfield-ro" title="CASH_BON"></td>
					<td><%= Bean.loylineXML.getfieldTransl("max_disc_st", false) %></td> <td><input type="text" name="max_disc_st" size="20" value="<%= loyDisc.getValue("MAX_DISC_ST_FRMT") %>" readonly="readonly" class="inputfield-ro" title="MAX_BONUS"> </td>
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("disc_fixed_value", false) %></td> <td><input type="text" name="disc_fixed_value" size="20" value="<%= loyDisc.getValue("DISC_FIXED_VALUE_FRMT") %>" readonly="readonly" class="inputfield-ro" title="BON_BON"> </td>
					<td>&nbsp;</td> <td>&nbsp;</td>
				</tr>
			</table>
		<% } %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_TELGR")) {%> 
	<%= trans.getTransactionsTelegramsHTML() %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_POSTINS")) {%>
		<table <%=Bean.getTableBottomFilter() %>>
		  <tr>
			<%= Bean.getFindHTML("posting_find", posting_find, "../crm/cards/transactionspecs.jsp?id="+transid + "&posting_page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("posting_bk_oper", "../crm/cards/transactionspecs.jsp?id="+transid + "&posting_page=1", "") %>
				<%= Bean.getBKOperationTypeShortOptions(posting_bk_oper, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
		  </tr>
		</table>

	<%= trans.getTransactionsPostingsHTML(posting_find, posting_bk_oper, l_posting_page_beg, l_posting_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_TRANSACTIONS_BROKEN")) { 
	String rejection_state = trans.getValue("IS_REJECTED");
	%>
	<form name="updateForm" id="updateForm" action="../crm/cards/transactionsupdate.jsp" accept-charset="UTF-8" method="POST">
   		<input type="hidden" name="type" value="rejection">
   		<input type="hidden" name="process" value="yes">
	<% if ("0".equalsIgnoreCase(rejection_state)) {%>
   		<input type="hidden" name="action" value="start">
    <% } else { %>
    	<input type="hidden" name="action" value="revert">
    <% } %>
  <table <%=Bean.getTableDetailParam() %>>
    <tr>
       <td align="left" colspan="2"> 
		<% if ("0".equalsIgnoreCase(rejection_state)) {%>
          &nbsp;&nbsp;&nbsp;&nbsp;<font size=2><%= Bean.transactionXML.getfieldTransl("LAB_BROKEN1", false) %><br>&nbsp;
          <% if (trans.getValue("IS_POSTING").equalsIgnoreCase("1")) {%>
          	&nbsp;&nbsp;&nbsp;<i><%= Bean.transactionXML.getfieldTransl("LAB_BROKEN2", false) %></i>
          	<br>&nbsp;</font>
          <%} %>
		<% } else { %>
          &nbsp;&nbsp;&nbsp;&nbsp;<font size=2><%= Bean.transactionXML.getfieldTransl("LAB_BROKEN3", false) %><br>&nbsp;
		<% } %>
       </font></td>
    </tr>
    <tr>
       <td align="left"> 
          <font size=2><%= Bean.transactionXML.getfieldTransl("id_trans", false) %></font>
       </td>
       <td align="left"><input type="text" name="id" size="20" value="<%= transid %>" readonly="readonly" class="inputfield-ro"></td>
    </tr>
    <tr>
        <td align="center" colspan="4"> 
			<%=Bean.getSubmitButtonAjax("../crm/cards/transactionsupdate.jsp") %>
			<%=Bean.getResetButton() %>
			<%=Bean.getGoBackButton("../crm/cards/transactions.jsp") %>
        </td>
    </tr>
 </table>
 </form>


<%} else if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_BROKEN")) { %>
	<br><b><%=Bean.form_messageXML.getfieldTransl("operation_denied", false) %></b>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_TASKS")) {%>
	
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("card_task_find", card_task_find, "../crm/cards/transactionspecs.jsp?id="+transid + "&card_task_page=1") %>
		
		</tr>
		</tbody>
		</table>			

	<%= trans.getClubCardsTasksHTML(card_task_find, "", "", l_card_task_page_beg, l_card_task_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_FUND_OPER")) {%>

<table <%=Bean.getTableBottomFilter() %>><tbody>
<tr>
	<%=Bean.getFindHTML("fund_oper_find", fund_oper_find, "../crm/cards/transactionspecs.jsp?id="+transid + "&fund_oper_page=1") %>

</tr>
</tbody>
</table>			

<%= trans.getFundOperationsHTML(fund_oper_find, l_fund_oper_page_beg, l_fund_oper_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_LOG")) {%>
	<% bcSysLogObject log = new bcSysLogObject(); 
	 if (!(log_type==null || "".equalsIgnoreCase(log_type))) {
		 log_type = "'"+log_type+"'";
	 }
	%>

		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("log_find", log_find, "../crm/cards/transactionspecs.jsp?id="+transid + "&log_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("log_type", "../crm/cards/transactionspecs.jsp?id=" + transid + "&log_page=1", "") %>
				<%= Bean.getSelectOptionHTML(log_type, "0", "") %>
				<%= Bean.getSelectOptionHTML(log_type, "TELEGRAM", Bean.transactionXML.getfieldTransl("h_log_transaction", false)) %>
				<%= Bean.getSelectOptionHTML(log_type, "POSTING", Bean.transactionXML.getfieldTransl("h_log_posting", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("log_row_type", "../crm/cards/transactionspecs.jsp?id=" + transid + "&log_page=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
				<%= Bean.getSelectOptionHTML(log_row_type, "0", "") %>
				<%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", log_row_type, false) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>			

	<%= log.getSysLogTermInterchangeHTML(log_find, log_type, "", "", transid, log_row_type, l_log_beg, l_log_end) %>
<%}

} %>
</div></div>
</body>
</html>
