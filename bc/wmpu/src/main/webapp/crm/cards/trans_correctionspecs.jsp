<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcErrorTransactionObject"%>

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcSysLogObject"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body topmargin="0">
<div id="div_tabsheet">
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_TRANSACTIONS";

Bean.setJspPageForTabName(pageFormName);

String tagLog = "_UNCHECKED_LOG";
String tagLogRowType = "_UNCHECKED_LOG_ROW_TYPE";
String tagLogFind = "_UNCHECKED_LOG_FIND";

String transid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null) { tab = Bean.tabsHmGetValue(pageFormName); }

if (transid==null) { transid=""; }
if ("".equalsIgnoreCase(transid)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	
	if (tab==null) {
		tab = Bean.currentMenu.getCurrentTab();
	}
	Bean.tabsHmSetValue(pageFormName, tab);
	bcErrorTransactionObject errtrans = new bcErrorTransactionObject(transid);
	
	//Обрабатываем номера страниц
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
	
	Bean.currentMenu.setExistFlag("CARDS_TRANSACTIONS_ADDITIONAL", false);
	Bean.currentMenu.setExistFlag("CARDS_TRANSACTIONS_TASKS", false);
	Bean.currentMenu.setExistFlag("CARDS_TRANSACTIONS_FUND_OPER", false);
	Bean.currentMenu.setExistFlag("CARDS_TRANSACTIONS_POSTINS", false);
	Bean.currentMenu.setExistFlag("CARDS_TRANSACTIONS_BROKEN", false);
	if (Bean.currentMenu.isCurrentTab("CARDS_TRANSACTIONS_ADDITIONAL") ||
			Bean.currentMenu.isCurrentTab("CARDS_TRANSACTIONS_TASKS") ||
			Bean.currentMenu.isCurrentTab("CARDS_TRANSACTIONS_FUND_OPER") ||
			Bean.currentMenu.isCurrentTab("CARDS_TRANSACTIONS_POSTINS") ||
			Bean.currentMenu.isCurrentTab("CARDS_TRANSACTIONS_BROKEN")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}


%>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
	
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_TRANSACTIONS_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/cards/trans_correctionupdate.jsp?id_trans=" + transid + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/cards/trans_correctionupdate.jsp?id_trans=" + transid + "&err_type="+errtrans.getValue("ERR_TYPE")+"&type=general&action=remove&process=yes", Bean.transactionXML.getfieldTransl("h_delete_trans", false), errtrans.getValue("ID_TRANS") + " - " +  errtrans.getValue("TYPE_TRANS_TXT")) %>
			    <%= Bean.getMenuButton("CHECK", "../crm/cards/trans_correctionupdate.jsp?id_trans=" + transid + "&err_type="+errtrans.getValue("ERR_TYPE")+"&type=general&action=check&process=yes", "", "") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_LOG")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagLog, "../crm/cards/trans_correctionspecs.jsp?id=" + transid + "&tab="+Bean.currentMenu.getTabID("CARDS_TRANSACTIONS_LOG")+"&", "log_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(errtrans.getValue("ID_TRANS") + " - " +  errtrans.getValue("TYPE_TRANS_TXT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/cards/trans_correctionspecs.jsp?id=" + transid + "&idtelgr=" + errtrans.getValue("ID_TELGR")) %>
			</td>

	</tr>
</table>
</div>
<div id="div_data">
<div id="div_data_detail">


<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("cards_transactions_info")) {

  if (Bean.currentMenu.isCurrentTabAndEditPermitted("cards_transactions_info")) { %>
	<script>
		var formAll = new Array (
		);
		var formTransData = new Array (
			new Array ('card_nr', 'varchar2', 1),
			new Array ('type_trans', 'varchar2', 1),
			new Array ('id_term', 'varchar2', 1),
			new Array ('cd_issuer', 'varchar2', 1),
			new Array ('cd_paym_sys', 'varchar2', 1),
			new Array ('sys_date', 'varchar2', 1),
			new Array ('state_trans', 'varchar2', 1),
			new Array ('id_sam', 'varchar2', 1),
			new Array ('sys_time', 'varchar2', 1),
			new Array ('ver_trans', 'varchar2', 1),
			new Array ('nt_sam', 'varchar2', 1),
			new Array ('curr_pda', 'varchar2', 1),
			new Array ('vk_enc', 'varchar2', 1),
			new Array ('nt_icc', 'varchar2', 1),
			new Array ('err_tx', 'varchar2', 1),
			new Array ('mac_icc', 'varchar2', 1),
			new Array ('mac_pda', 'varchar2', 1),
			new Array ('bases_for_changes', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1)
		);
		var formRecPayment_v0 = new Array (
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1)
		);
		var formRecPayment_v1_2 = new Array (
			new Array ('clubcard', 'varchar2', 1),
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('sum_pay_bon', 'varchar2', 1),
			new Array ('fl_ext_loyl', 'varchar2', 1),
			new Array ('club_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('cash_card_nr', 'varchar2', 1),
			new Array ('c_nr', 'varchar2', 1),
			new Array ('c_check_nr', 'varchar2', 1)
		);
		var formRecPayment_prv = new Array (
			new Array ('bal_acc_prv', 'varchar2', 1),
			new Array ('bal_cur_prv', 'varchar2', 1),
			new Array ('bal_bon_per_prv', 'varchar2', 1),
			new Array ('bal_disc_per_prv', 'varchar2', 1)
		);
		var formRecMovBon = new Array (
			new Array ('bal_acc', 'varchar2', 1),
			new Array ('bal_cur', 'varchar2', 1),
			new Array ('bal_bon_per', 'varchar2', 1),
			new Array ('bal_disc_per', 'varchar2', 1)
		);
		var formRecMovBon_prv = new Array (
			new Array ('club_disc_prv', 'varchar2', 1),
			new Array ('club_bon_prv', 'varchar2', 1),
			new Array ('date_acc_prv', 'varchar2', 1),
			new Array ('date_calc_prv', 'varchar2', 1),
			new Array ('date_mov_prv', 'varchar2', 1),
			new Array ('bal_acc_prv', 'varchar2', 1),
			new Array ('bal_cur_prv', 'varchar2', 1),
			new Array ('bal_bon_per_prv', 'varchar2', 1),
			new Array ('bal_disc_per_prv', 'varchar2', 1)
		);
		var formRecChkCard = new Array (
			new Array ('p_action_chk', 'varchar2', 1),
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
		var formRecChkCard_prv = new Array (
			new Array ('club_st_prv', 'varchar2', 1),
			new Array ('club_disc_prv', 'varchar2', 1),
			new Array ('club_bon_prv', 'varchar2', 1),
			new Array ('date_acc_prv', 'varchar2', 1),
			new Array ('date_calc_prv', 'varchar2', 1),
			new Array ('date_mov_prv', 'varchar2', 1),
			new Array ('bal_acc_prv', 'varchar2', 1),
			new Array ('bal_cur_prv', 'varchar2', 1),
			new Array ('bal_bon_per_prv', 'varchar2', 1),
			new Array ('bal_disc_per_prv', 'varchar2', 1)
		);
		var formRecInvalCard = new Array (
			new Array ('p_action_inval', 'varchar2', 1),
			new Array ('club_st', 'varchar2', 1)
		);
		var formRecInvalCard_prv = new Array (
			new Array ('club_st_prv', 'varchar2', 1)
		);
		var formRecStornoBon_v0 = new Array (
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('bal_acc', 'varchar2', 1),
			new Array ('bal_cur', 'varchar2', 1),
			new Array ('bal_bon_per', 'varchar2', 1),
			new Array ('bal_disc_per', 'varchar2', 1)
		);
		var formRecStornoBon_v1_2 = new Array (
			new Array ('nt_ext', 'varchar2', 1),
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
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('sum_pay_bon', 'varchar2', 1),
			new Array ('club_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('imid', 'varchar2', 1),
			new Array ('specid', 'varchar2', 1),
			new Array ('clubcard', 'varchar2', 1),
			new Array ('cardid', 'varchar2', 1),
			new Array ('fl_ext_loyl', 'varchar2', 1),
			new Array ('cash_card_nr', 'varchar2', 1),
			new Array ('c_nr', 'varchar2', 1),
			new Array ('c_check_nr', 'varchar2', 1)
		);
		var formRecPaymentIm_prv = new Array (
			new Array ('bal_acc_prv', 'varchar2', 1),
			new Array ('bal_cur_prv', 'varchar2', 1),
			new Array ('bal_bon_per_prv', 'varchar2', 1),
			new Array ('bal_disc_per_prv', 'varchar2', 1)
		);
		var formRecPaymentExt = new Array (
			new Array ('clubcard', 'varchar2', 1),
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('sum_pay_bon', 'varchar2', 1),
			new Array ('fl_ext_loyl', 'varchar2', 1),
			new Array ('club_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('cash_card_nr', 'varchar2', 1),
			new Array ('c_nr', 'varchar2', 1),
			new Array ('c_check_nr', 'varchar2', 1)
		);
		
		function myValidateForm(){
			var transType = document.getElementById('type_trans').value;
			var verTrans = document.getElementById('ver_trans').value;
			var enteredManually = document.getElementById('entered_manually').value;

			formAll = formTransData;
			
			if (transType == '1') {
				if (verTrans == '0') {
					formAll = formAll.concat(formRecPayment_v0);
				} else {
					formAll = formAll.concat(formRecPayment_v1_2);
				}
				if (enteredManually == 'N') {
					formAll = formAll.concat(formRecPayment_prv);
				}
			}
			if (transType == '2') {
				formAll = formAll.concat(formRecMovBon);
				if (enteredManually == 'N') {
					formAll = formAll.concat(formRecMovBon_prv);
				}
			}
			if (transType == '3') {
				formAll = formAll.concat(formRecChkCard);
				if (enteredManually == 'N') {
					formAll = formAll.concat(formRecChkCard_prv);
				}
			}
			if (transType == '4') {
				formAll = formAll.concat(formRecInvalCard);
				if (enteredManually == 'N') {
					formAll = formAll.concat(formRecInvalCard_prv);
				}
			}
			if (transType == '5') {
				if (verTrans == '0') {
					formAll = formAll.concat(formRecStornoBon_v0);
				} else {
					formAll = formAll.concat(formRecStornoBon_v1_2);
				}
			}
			if (transType == '6') {
				if (verTrans == '0') {
					return false;
				} else {
					formAll = formAll.concat(formRecPaymentIm);
					if (enteredManually == 'N') {
						formAll = formAll.concat(formRecPaymentIm_prv);
					}
				}
			}
			if (transType == '7') {
				if (verTrans == '0' || verTrans == '1') {
					return false;
				} else {
					formAll = formAll.concat(formRecPaymentExt);
					if (enteredManually == 'N') {
						formAll = formAll.concat(formRecPayment_prv);
					}
				}
			}
			//alert(formAll);
			return validateForm(formAll);
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

		//var rec_chk_card_action = '<select name="p_action" id="p_action" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("REC_CHK_CARD_ACTION", errtrans.getValue("ACTION"), false) %></select>';
		//var rec_inval_card_action = '<select name="p_action" id="p_action" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("REC_INVAL_CARD_ACTION", errtrans.getValue("ACTION"), false) %></select>';

		function setRecPayment(ver_trans){
			if (ver_trans==0) {
				disable('clubcard');
			} else {
				enable('clubcard');
			}
			enable('nt_ext');
			enable('opr_sum');
			if (ver_trans==0) {
				disable('sum_pay_cash');
				disable('sum_pay_card');
				disable('sum_pay_bon');
				disable('club_sum');
			} else {
				enable('sum_pay_cash');
				enable('sum_pay_card');
				enable('sum_pay_bon');
				enable('club_sum');
			}
			enable('sum_bon');
			enable('sum_disc');
			enable('bal_acc_prv');
			enable('bal_cur_prv');
			enable('bal_bon_per_prv');
			enable('bal_disc_per_prv');
			disable('club_st_prv');
			disable('club_disc_prv');
			disable('club_bon_prv');
			disable('date_acc_prv');
			disable('date_mov_prv');
			disable('date_calc_prv');
			disable('club_st');
			disable('club_disc');
			disable('club_bon');
			disable('bal_acc');
			disable('bal_bon_per');
			disable('bal_disc_per');
			disable('bal_cur');
			disable('date_acc');
			disable('date_mov');
			if (ver_trans==0) {
				disable('fl_ext_loyl');
				disable('cash_card_nr');
				disable('c_nr');
				disable('c_check_nr');
			} else {
				enable('fl_ext_loyl');
				enable('cash_card_nr');
				enable('c_nr');
				enable('c_check_nr');
			}
			disable('imid');
			disable('specid');
			disable('cardid');
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function setRecMovBon(ver_trans){
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
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function setRecChkCard(ver_trans){
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
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "visible";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "visible";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').outerHTML=rec_chk_card_action;
		}
		function setRecInvalCard(ver_trans){
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
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "visible";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "visible";
			//document.getElementById('p_action').outerHTML=rec_inval_card_action;
		}
		function setRecStornoBon(ver_trans){
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
			if (ver_trans==1) {
				enable('sum_pay_cash');
			} else {
				disable('sum_pay_cash');
			}
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
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function setRecPaymentIm(ver_trans){
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
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function setRecPaymentExt(ver_trans){
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
			enable('clubcard');
			disable('cardid');
			enable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function disablePrvParam(){
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
		}
		function checkTransType(){
			var transType = document.getElementById('type_trans').value;
			var verTrans = document.getElementById('ver_trans').value;
			var enteredManually = document.getElementById('entered_manually').value;
			var idTelgr = document.getElementById('id_telgr').value;

			if (!(verTrans=="0" || verTrans=="1" || verTrans=="2" || verTrans=="3" || verTrans=="4")) {
				alertText = '<%= Bean.transactionXML.getfieldTransl("h_this_trans_ver_not_found", false) %>';
				alertText = alertText.replace("%VER_TRANS%", verTrans);
				window.alert(alertText);
				return false;
			}
			
			if (transType == '1') setRecPayment(verTrans);
			if (transType == '2') setRecMovBon(verTrans);
			if (transType == '3') setRecChkCard(verTrans);
			if (transType == '4') setRecInvalCard(verTrans);
			if (transType == '5') setRecStornoBon(verTrans);
			if (transType == '6') {
				if (verTrans=="0") {
					alertText = '<%= Bean.transactionXML.getfieldTransl("h_this_trans_ver_not_found", false) %>';
					alertText = alertText.replace("%VER_TRANS%", verTrans);
					window.alert(alertText);
					return false;
				} else {
					setRecPaymentIm(verTrans);
				}
			}
			if (transType == '7') {
				if (verTrans=="0" || verTrans=="1") {
					alertText = '<%= Bean.transactionXML.getfieldTransl("h_this_trans_ver_not_found", false) %>';
					alertText = alertText.replace("%VER_TRANS%", verTrans);
					window.alert(alertText);
					return false;
				} else {
					setRecPaymentExt(verTrans);
				}
			}
			
			if (enteredManually == 'Y' && !(idTelgr==null || "".equalsIgnoreCase(idTelgr))) {
				disablePrvParam();
			}
			return true;
		}	
		function checkEnteredManually(element){
			if (element.value == 'Y') {
				disablePrvParam();
			} else {
				checkTransType();
			}
			return true;
		}	
		checkTransType();
		
	</script>

	<form action="../crm/cards/trans_correctionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	    <input type="hidden" name="action" value="edit">
	    <input type="hidden" name="process" value="yes">
		<input type="hidden" name="err_type" value="<%=errtrans.getValue("ERR_TYPE") %>">
	<table <%=Bean.getTableDetailParam() %>> 
		<% if ("C".equalsIgnoreCase(errtrans.getValue("ERR_TYPE"))) { %>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("id_trans", false) %>
			<% if ("Y".equalsIgnoreCase(errtrans.getValue("ENTERED_MANUALLY"))) { %>
				<b><font color="red">(<%= Bean.transactionXML.getfieldTransl("h_entered_manually", false) %>)</font></b>
			<% } %>
            </td> <td><input type="text" name="id_trans" id="id_trans" size="20" value="<%= transid %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.clubXML.getfieldTransl("club", false)%>
					<%=Bean.getGoToClubLink(errtrans.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%=Bean.getClubShortName(errtrans.getValue("ID_CLUB"))%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("id_telgr", false) %>
				<%= Bean.getGoToTelegramLink(errtrans.getValue("ID_TELGR")) %>
			</td> <td><input type="text" name="id_telgr" id="id_telgr" size="20" value="<%= errtrans.getValue("ID_TELGR") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("type_trans_txt", true) %></td> <td><select name="type_trans" id="type_trans" class="inputfield" onchange="checkTransType();"><%= Bean.getTransTypeOptions(errtrans.getValue("TYPE_TRANS"), true) %></select></td>
			<td><%= Bean.transactionXML.getfieldTransl("id_term", true) %>
				<%= Bean.getGoToTerminalLink(errtrans.getValue("ID_TERM")) %>
			</td>
			<td><input type="text" name="id_term" id="id_term" size="20" value="<%= errtrans.getValue("ID_TERM") %>" class="inputfield" title="ID_TERM"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("state_trans_tsl", true) %></td> <td><select name="state_trans" id="state_trans" class="inputfield"><%= Bean.getTransStateOptions(errtrans.getValue("STATE_TRANS"), true) %></select></td>
			<td><%= Bean.transactionXML.getfieldTransl("id_sam", true) %>
				<%= Bean.getGoToSAMLink(errtrans.getValue("ID_SAM")) %>
			</td>
			<td><input type="text" name="id_sam" id="id_sam" size="20" value="<%= errtrans.getValue("ID_SAM") %>" class="inputfield" title="ID_SAM"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("sys_date", true) %></td> <td><input type="text" name="sys_date" id="sys_date" size="20" value="<%= errtrans.getValue("SYS_DATE") %>" class="inputfield" title="SYS_DATE"> </td>
			<td><%= Bean.transactionXML.getfieldTransl("nt_sam", true) %></td> <td><input type="text" name="nt_sam" id="nt_sam" size="20" value="<%= errtrans.getValue("NT_SAM") %>" class="inputfield" title="NT_SAM"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("sys_time", true) %></td> <td><input type="text" name="sys_time" id="sys_time" size="20" value="<%= errtrans.getValue("SYS_TIME") %>" class="inputfield" title="SYS_TIME"> </td>
			<td><span id="span_nt_ext"><%= Bean.transactionXML.getfieldTransl("nt_ext", false) %></span></td><td><input type="text" name="nt_ext" id="nt_ext" size="20" value="<%= errtrans.getValue("NT_EXT") %>" class="inputfield" title="NT_EXT"></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("ver_trans", true) %></td> <td><input type="text" name="ver_trans" id="ver_trans" size="20" value="<%= errtrans.getValue("VER_TRANS") %>" class="inputfield" onblur="checkTransType();"></td>
			<td valign="top"><span id="span_clubcard"><%= Bean.transactionXML.getfieldTransl("clubcard", false) %></span></td> <td><input type="text" name="clubcard" id="clubcard" size="20" value="<%= errtrans.getValue("CLUBCARD") %>" class="inputfield" onblur="checkTransType();"></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("vk_enc", true) %></td><td><input type="text" name="vk_enc" id="vk_enc" size="20" value="<%= errtrans.getValue("VK_ENC") %>" class="inputfield" title="VK_ENC"></td>
			<td><%= Bean.transactionXML.getfieldTransl("card_serial_number", true) %>
				<% if (!(errtrans.getValue("ID_ISSUER")==null || "".equalsIgnoreCase(errtrans.getValue("ID_ISSUER")))) { %>
				<%= Bean.getGoToClubCardLink(
						errtrans.getValue("CARD_NR"),
						errtrans.getValue("ID_ISSUER"),
						errtrans.getValue("ID_PAYMENT_SYSTEM")
					) %>
				<% } %>
			</td> 
			<td> <input type="text" name="card_nr" id="card_nr" size="20" value="<%= errtrans.getValue("CARD_NR") %>" class="inputfield" title="CARD_NR"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("err_tx", true) %></td> <td><input type="text" name="err_tx" id="err_tx" size="20" value="<%= errtrans.getValue("ERR_TX") %>" class="inputfield" title="ERR_TX: <%= errtrans.getValue("ERR_TX") %> - <%= Bean.getSysERRTXDescription(errtrans.getValue("ERR_TX")) %>"> </td>
			<td><span id="span_cd_issuer"><%= Bean.transactionXML.getfieldTransl("cd_issuer", true) %></span></td> <td> <input type="text" name="cd_issuer" id="cd_issuer" size="20" value="<%= errtrans.getValue("CD_ISSUER") %>" class="inputfield" title="ID_ISSUER"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("mac_icc", true) %></td> <td><input type="text" name="mac_icc" id="mac_icc" size="20" value="<%= errtrans.getValue("MAC_ICC") %>" class="inputfield" title="MAC_ICC"> </td>
			<td><span id="span_cd_paym_sys"><%= Bean.transactionXML.getfieldTransl("cd_paym_sys", true) %></span></td> <td> <input type="text" name="cd_paym_sys" id="cd_paym_sys" size="20" value="<%= errtrans.getValue("CD_PAYM_SYS") %>" class="inputfield" title="ID_PAYM_SYS"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("mac_pda", true) %></td> <td><input type="text" name="mac_pda" id="mac_pda" size="20" value="<%= errtrans.getValue("MAC_PDA") %>" class="inputfield" title="MAC_PDA"> </td>
			<td><%= Bean.transactionXML.getfieldTransl("nt_icc", true) %></td> <td> <input type="text" name="nt_icc" id="nt_icc" size="20" value="<%= errtrans.getValue("NT_ICC") %>" class="inputfield" title="NT_ICC"> </td>
		</tr>
		<tr>
			<td><span id="p_action_title_chk"><%= Bean.transactionXML.getfieldTransl("action", true) %></span><br>
				<span id="p_action_title_inval"><%= Bean.transactionXML.getfieldTransl("action", true) %></span>
            </td>
			<td>
			    <select name="p_action_chk" id="p_action_chk" class="inputfield">
					<%= Bean.getMeaningFromLookupNameOptions("REC_CHK_CARD_ACTION", errtrans.getValue("ACTION"), true) %>
				</select><br>
				<select name="p_action_inval" id="p_action_inval" class="inputfield">
					<%= Bean.getMeaningFromLookupNameOptions("REC_INVAL_CARD_ACTION", errtrans.getValue("ACTION"), true) %>
				</select>
		    </td>
			<td><%= Bean.transactionXML.getfieldTransl("cd_currency", true) %></td><td><input type="text" name="curr_pda" id="curr_pda" size="20" value="<%= errtrans.getValue("CURR_PDA") %>" class="inputfield" title="CURR_PDA"></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_before", false) %></b></td>
		</tr>
		<tr>
			<td><span id="span_club_st_prv"><%= Bean.transactionXML.getfieldTransl("club_st_prv", false) %></span></td> <td><input type="text" name="club_st_prv" id="club_st_prv" size="20" value="<%= errtrans.getValue("CLUB_ST_PRV") %>" class="inputfield" title="CLUB_ST_PRV"></td>
			<td><span id="span_club_bon_prv"><%= Bean.transactionXML.getfieldTransl("club_bon_prv", false) %></span></td><td><input type="text" name="club_bon_prv" id="club_bon_prv" size="20" value="<%= errtrans.getValue("CLUB_BON_PRV") %>" class="inputfield" title="CLUB_BON_PRV"></td>
		</tr>
		<tr>
			<td><span id="span_bal_acc_prv"><%= Bean.transactionXML.getfieldTransl("bal_acc_prv", false) %></span></td><td><input type="text" name="bal_acc_prv" id="bal_acc_prv" size="20" value="<%= errtrans.getValue("BAL_ACC_PRV") %>" class="inputfield" title="BAL_ACC_PRV"></td>
			<td><span id="span_club_disc_prv"><%= Bean.transactionXML.getfieldTransl("club_disc_prv", false) %></span></td> <td><input type="text" name="club_disc_prv" id="club_disc_prv" size="20" value="<%= errtrans.getValue("CLUB_DISC_PRV") %>" class="inputfield" title="CLUB_DISC_PRV"> </td>
		</tr>
		<tr>
			<td><span id="span_bal_cur_prv"><%= Bean.transactionXML.getfieldTransl("bal_cur_prv", false) %></span></td><td><input type="text" name="bal_cur_prv" id="bal_cur_prv" size="20" value="<%= errtrans.getValue("BAL_CUR_PRV") %>" class="inputfield" title="BAL_CUR_PRV"></td>
			<td><span id="span_date_acc_prv"><%= Bean.transactionXML.getfieldTransl("date_acc_prv", false) %></span></td><td><input type="text" name="date_acc_prv" id="date_acc_prv" size="20" value="<%= errtrans.getValue("DATE_ACC_PRV") %>" class="inputfield" title="DATE_ACC_PRV"></td>
		</tr>
		<tr>
			<td><span id="span_bal_bon_per_prv"><%= Bean.transactionXML.getfieldTransl("bal_bon_per_prv", false) %></span></td> <td><input type="text" name="bal_bon_per_prv" id="bal_bon_per_prv" size="20" value="<%= errtrans.getValue("BAL_BON_PER_PRV") %>" class="inputfield" title="BAL_BON_PER_PRV"> </td>
			<td><span id="span_date_mov_prv"><%= Bean.transactionXML.getfieldTransl("date_mov_prv", false) %></span></td><td><input type="text" name="date_mov_prv" id="date_mov_prv" size="20" value="<%= errtrans.getValue("DATE_MOV_PRV") %>" class="inputfield" title="DATE_MOV_PRV"></td>
		</tr>
		<tr>
			<td><span id="span_bal_disc_per_prv"><%= Bean.transactionXML.getfieldTransl("bal_disc_per_prv", false) %></span></td> <td><input type="text" name="bal_disc_per_prv" id="bal_disc_per_prv" size="20" value="<%= errtrans.getValue("BAL_DISC_PER_PRV") %>" class="inputfield" title="BAL_DISC_PER_PRV"> </td>
			<td><span id="span_date_calc_prv"><%= Bean.transactionXML.getfieldTransl("date_calc_prv", false) %></span></td><td><input type="text" name="date_calc_prv" id="date_calc_prv" size="20" value="<%= errtrans.getValue("DATE_CALC_PRV") %>" class="inputfield" title="DATE_CALC_PRV"></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_operation", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_opr_sum"><%= Bean.transactionXML.getfieldTransl("opr_sum", false) %></span></td><td><input type="text" name="opr_sum" id="opr_sum" size="20" value="<%= errtrans.getValue("OPR_SUM") %>" class="inputfield" title="OPR_SUM"></td>
			<td valign="top"><span id="span_sum_bon"><%= Bean.transactionXML.getfieldTransl("sum_bon", false) %></span></td><td><input type="text" name="sum_bon" id="sum_bon" size="20" value="<%= errtrans.getValue("SUM_BON") %>" class="inputfield" title="SUM_BON"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_sum_pay_cash"><%= Bean.transactionXML.getfieldTransl("sum_pay_cash", false) %></span></td><td><input type="text" name="sum_pay_cash" id="sum_pay_cash" size="20" value="<%= errtrans.getValue("SUM_PAY_CASH") %>" class="inputfield" title="SUM_PAY_CASH"></td>
			<td valign="top"><span id="span_sum_bon_cash"><%= Bean.transactionXML.getfieldTransl("sum_bon_cash", false) %></span></td><td><input type="text" name="sum_bon_cash" id="sum_bon_cash" size="20" value="<%= errtrans.getValue("SUM_BON_CASH") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_sum_pay_card"><%= Bean.transactionXML.getfieldTransl("sum_pay_card", false) %></span></td><td><input type="text" name="sum_pay_card" id="sum_pay_card" size="20" value="<%= errtrans.getValue("SUM_PAY_CARD") %>" class="inputfield" title="SUM_PAY_CARD"> </td>
			<td valign="top"><span id="span_sum_bon_card"><%= Bean.transactionXML.getfieldTransl("sum_bon_card", false) %></span></td><td><input type="text" name="sum_bon_card" id="sum_bon_card" size="20" value="<%= errtrans.getValue("SUM_BON_CARD") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_sum_pay_bon"><%= Bean.transactionXML.getfieldTransl("sum_pay_bon", false) %></span></td><td><input type="text" name="sum_pay_bon" id="sum_pay_bon" size="20" value="<%= errtrans.getValue("SUM_PAY_BON") %>" class="inputfield" title="SUM_PAY_BON"> </td>
			<td valign="top"><span id="span_sum_bon_bon"><%= Bean.transactionXML.getfieldTransl("sum_bon_bon", false) %></span></td><td><input type="text" name="sum_bon_bon" id="sum_bon_bon" size="20" value="<%= errtrans.getValue("SUM_BON_BON") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_sum"><%= Bean.transactionXML.getfieldTransl("club_sum", false) %></span></td> <td><input type="text" name="club_sum" id="club_sum" size="20" value="<%= errtrans.getValue("CLUB_SUM") %>" class="inputfield" title="CLUB_SUM"></td>
			<td valign="top"><span id="span_sum_bon_disc"><%= Bean.transactionXML.getfieldTransl("sum_bon_disc", false) %></span></td><td><input type="text" name="sum_bon_disc" id="sum_bon_disc" size="20" value="<%= errtrans.getValue("SUM_BON_DISC") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top" colspan="2">&nbsp;</td>
			<td valign="top"><span id="span_sum_disc"><%= Bean.transactionXML.getfieldTransl("sum_disc", false) %></span></td><td><input type="text" name="sum_disc" id="sum_disc" size="20" value="<%= errtrans.getValue("SUM_DISC") %>" class="inputfield" title="SUM_DISC"></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_after", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_st"><%= Bean.transactionXML.getfieldTransl("club_st", false) %></span></td> <td><input type="text" name="club_st" id="club_st" size="20" value="<%= errtrans.getValue("CLUB_ST") %>" class="inputfield" title="CLUB_ST"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_bon"><%= Bean.transactionXML.getfieldTransl("club_bon", false) %></span></td><td><input type="text" name="club_bon" id="club_bon" size="20" value="<%= errtrans.getValue("CLUB_BON") %>" class="inputfield" title="CLUB_BON"></td>
			<td valign="top"><span id="span_bal_acc"><%= Bean.transactionXML.getfieldTransl("bal_acc", false) %></span></td> <td><input type="text" name="bal_acc" id="bal_acc" size="20" value="<%= errtrans.getValue("BAL_ACC") %>" class="inputfield" title="BAL_ACC"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_disc"><%= Bean.transactionXML.getfieldTransl("club_disc", false) %></span></td><td><input type="text" name="club_disc" id="club_disc" size="20" value="<%= errtrans.getValue("CLUB_DISC") %>" class="inputfield" title="CLUB_DISC"> </td>
			<td valign="top"><span id="span_bal_cur"><%= Bean.transactionXML.getfieldTransl("bal_cur", false) %></span></td><td><input type="text" name="bal_cur" id="bal_cur" size="20" value="<%= errtrans.getValue("BAL_CUR") %>" class="inputfield" title="BAL_CUR"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_date_acc"><%= Bean.transactionXML.getfieldTransl("date_acc", false) %></span></td><td><input type="text" name="date_acc" id="date_acc" size="20" value="<%= errtrans.getValue("DATE_ACC") %>" class="inputfield" title="DATE_ACC"></td>
			<td valign="top"><span id="span_bal_bon_per"><%= Bean.transactionXML.getfieldTransl("bal_bon_per", false) %></span></td><td><input type="text" name="bal_bon_per" id="bal_bon_per" size="20" value="<%= errtrans.getValue("BAL_BON_PER") %>" class="inputfield" title="BAL_BON_PER"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_date_mov"><%= Bean.transactionXML.getfieldTransl("date_mov", false) %></span></td><td><input type="text" name="date_mov" id="date_mov" size="20" value="<%= errtrans.getValue("DATE_MOV") %>" class="inputfield" title="DATE_MOV"></td>
			<td valign="top"><span id="span_bal_disc_per"><%= Bean.transactionXML.getfieldTransl("bal_disc_per", false) %></span></td><td><input type="text" name="bal_disc_per" id="bal_disc_per" size="20" value="<%= errtrans.getValue("BAL_DISC_PER") %>" class="inputfield" title="BAL_DISC_PER"> </td>
		</tr>
		
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_im", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_imid"><%= Bean.transactionXML.getfieldTransl("imid", false) %></span></td><td><input type="text" name="imid" id="imid" size="20" value="<%= errtrans.getValue("IMID") %>" class="inputfield" title="IMID"></td>
			<td valign="top"><span id="span_specid"><%= Bean.transactionXML.getfieldTransl("specid", false) %></span></td><td><input type="text" name="specid" id="specid" size="20" value="<%= errtrans.getValue("SPECID") %>" class="inputfield" title="SPECID"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_cardid"><%= Bean.transactionXML.getfieldTransl("cardid", false) %></span></td><td><input type="text" name="cardid" id="cardid" size="20" value="<%= errtrans.getValue("CARDID") %>" class="inputfield" title="CARDID"></td>
		</tr>
		
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_check", false) %></b></td>
		</tr>
		<tr>
			<td><span id="span_fl_ext_loyl"><%= Bean.transactionXML.getfieldTransl("fl_ext_loyl", false) %></span></td> <td><select name="fl_ext_loyl" id="fl_ext_loyl" class="inputfield" title="FL_EXT_LOYL"><option value=""></option><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", errtrans.getValue("FL_EXT_LOYL"), false) %></select></td>
			<td><span id="span_c_nr"><%= Bean.transactionXML.getfieldTransl("c_nr", false) %></span></td><td><input type="text" name="c_nr" id="c_nr" size="20" value="<%= errtrans.getValue("C_NR") %>" class="inputfield" title="C_NR"></td>
		</tr>
		<tr>
			<td><span id="span_cash_card_nr"><%= Bean.transactionXML.getfieldTransl("cash_card_nr", false) %></span></td><td><input type="text" name="cash_card_nr" id="cash_card_nr" size="20" value="<%= errtrans.getValue("CASH_CARD_NR") %>" class="inputfield" title="CASH_CARD_NR"></td>
			<td><span id="span_c_check_nr"><%= Bean.transactionXML.getfieldTransl("c_check_nr", false) %></span></td><td><input type="text" name="c_check_nr" id="c_check_nr" size="20" value="<%= errtrans.getValue("C_CHECK_NR") %>" class="inputfield" title="C_CHECK_NR"></td>
		</tr>
		
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_rec_payment_ext_param", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_c_nomenkl"><%= Bean.transactionXML.getfieldTransl("c_nomenkl", false) %></span></td><td  colspan="3"><input type="text" name="c_nomenkl" id="c_nomenkl" size="74" value="<%= errtrans.getValue("C_NOMENKL") %>" class="inputfield" title="C_NOMENKL"></td>
		</tr>
		<tr>
			<td valign="top" class="top_line"><%= Bean.transactionXML.getfieldTransl("src_trans", false) %></td> 
			<td colspan="3" class="top_line"><textarea name="src_trans" id="src_trans" cols="120" rows="3" class="inputfield"><%= errtrans.getValue("SRC_TRANS") %></textarea></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_result", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("id_trans_reversed", false) %>
				<%= Bean.getGoToTransactionLink(errtrans.getValue("ID_TRANS_REVERSED")) %>
			</td> 
				<td><input type="text" name="id_trans_reversed" id="id_trans_reversed" size="20" value="<%= errtrans.getValue("ID_TRANS_REVERSED") %>" class="inputfield"> </td>
			<td><%= Bean.transactionXML.getfieldTransl("id_trans_double", false) %>
				<%= Bean.getGoToTransactionLink(errtrans.getValue("ID_TRANS_DOUBLE")) %>
			</td>
				<td><input type="text" name="id_trans_double" id="id_trans_double" size="20" value="<%= errtrans.getValue("ID_TRANS_DOUBLE") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_user_changing", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("changed_by_user", false) %></td> <td><input type="text" name="changed_by_user" id="changed_by_user" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", errtrans.getValue("CHANGED_BY_USER")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.transactionXML.getfieldTransl("date_last_users_changes", false) %></td><td><input type="text" name="date_last_users_changes" id="date_last_users_changes" size="20" value="<%= errtrans.getValue("DATE_LAST_USERS_CHANGES_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("user_who_has_changed", false) %></td> <td>
				<input type="hidden" name="user_who_has_changed" id="user_who_has_changed" value="<%= Bean.loginUser.getValue("ID_USER") %>">
				<input type="text" name="user_who_has_changed_name" id="user_who_has_changed_name" size="20" value="<%= Bean.getUserName(errtrans.getValue("USER_WHO_HAS_CHANGED")) %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td><%= Bean.transactionXML.getfieldTransl("entered_manually", false) %></td> <td><select name="entered_manually" id="entered_manually" class="inputfield" onchange="checkEnteredManually(this)"><%= Bean.getYesNoLookupOptions(errtrans.getValue("ENTERED_MANUALLY"), false) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("bases_for_changes", true) %></td><td  colspan="3"><textarea name="bases_for_changes" id="bases_for_changes" cols="100" rows="3" class="inputfield"><%= errtrans.getValue("BASES_FOR_CHANGES") %></textarea></td>
		</tr>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.commonXML.getfieldTransl("h_record_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.commonXML.getfieldTransl("creation_date", false)%></td> <td><input type="text" name="creation_date" size="20" value="<%=errtrans.getValue(Bean.getCreationDateFieldName())%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.commonXML.getfieldTransl("created_by", false)%></td> <td><input type="text" name="created_by" size="20" value="<%=Bean.getSystemUserName(errtrans.getValue("CREATED_BY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/trans_correctionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/cards/trans_correction.jsp") %>
			</td>
		</tr>

		<% } else if ("P".equalsIgnoreCase(errtrans.getValue("ERR_TYPE"))) { %>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("id_trans", false) %>
			<% if ("Y".equalsIgnoreCase(errtrans.getValue("ENTERED_MANUALLY"))) { %>
				<b><font color="red">(<%= Bean.transactionXML.getfieldTransl("h_entered_manually", false) %>)</font></b>
			<% } %>
            </td> <td><input type="text" name="id_trans" size="20" value="<%= transid %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.transactionXML.getfieldTransl("id_telgr", false) %>
				<%= Bean.getGoToTelegramLink(errtrans.getValue("ID_TELGR")) %>
			</td> <td><input type="text" name="id_telgr" size="20" value="<%= errtrans.getValue("ID_TELGR") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("type_trans_txt", false) %></td> <td><select name="type_trans" id="type_trans" class="inputfield"><%= Bean.getTransTypeOptions(errtrans.getValue("TYPE_TRANS"), true) %></select></td>
			<td><%= Bean.transactionXML.getfieldTransl("id_term", false) %>
				<%= Bean.getGoToTerminalLink(errtrans.getValue("ID_TERM")) %>
			</td> 
			<td><input type="text" name="id_term" size="20" value="<%= errtrans.getValue("ID_TERM") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("state_trans_tsl", false) %></td> <td><select name="state_trans" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("SYS_MONITOR_CHECK_RESULT", errtrans.getValue("STATE_TRANS"), false) %></select></td>
			<td><%= Bean.transactionXML.getfieldTransl("is_crypted", false) %></td> <td><select name="is_crypted" class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", errtrans.getValue("IS_CRYPTED"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("ver_trans", false) %></td> <td><input type="text" name="ver_trans" id="ver_trans" size="20" value="<%= errtrans.getValue("VER_TRANS") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("fl_has_format_error", false) %></td> <td><select name="fl_has_format_error" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("FORMAT_ERROR_FLAG", errtrans.getValue("FL_HAS_FORMAT_ERROR"), false) %></select></td>
			<td><%= Bean.transactionXML.getfieldTransl("entered_manually", false) %></td> <td><select name="entered_manually" id="entered_manually" class="inputfield"><%= Bean.getYesNoLookupOptions(errtrans.getValue("ENTERED_MANUALLY"), false) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("src_trans", false) %></td> 
				<td colspan="5"><textarea name="src_trans" cols="150" rows="3" class="inputfield"><%= errtrans.getValue("SRC_TRANS") %></textarea></td>
		</tr>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.commonXML.getfieldTransl("h_record_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.commonXML.getfieldTransl("creation_date", false)%></td> <td><input type="text" name="creation_date" size="20" value="<%=errtrans.getValue(Bean.getCreationDateFieldName())%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.commonXML.getfieldTransl("created_by", false)%></td> <td><input type="text" name="created_by" size="20" value="<%=Bean.getSystemUserName(errtrans.getValue("CREATED_BY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/trans_correctionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/cards/trans_correction.jsp") %>
			</td>
		</tr>
		<% } %>
	</table>
	</form>
 

 <%
  } else {
	  %>
		<form>
		<table <%=Bean.getTableDetailParam() %>> 
			<% if ("C".equalsIgnoreCase(errtrans.getValue("ERR_TYPE"))) { %>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("id_trans", false) %>
				<% if ("Y".equalsIgnoreCase(errtrans.getValue("ENTERED_MANUALLY"))) { %>
					<b><font color="red">(<%= Bean.transactionXML.getfieldTransl("h_entered_manually", false) %>)</font></b>
				<% } %>
            	</td> <td><input type="text" name="id_trans" size="20" value="<%= transid %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_telgr", false) %>
					<%= Bean.getGoToTelegramLink(errtrans.getValue("ID_TELGR")) %>
				</td> <td><input type="text" name="id_telgr" size="20" value="<%= errtrans.getValue("ID_TELGR") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("type_trans_txt", false) %></td> <td><input type="text" name="type_trans_txt" size="30" value="<%= errtrans.getValue("TYPE_TRANS_TXT") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_term", false) %>
					<%= Bean.getGoToTerminalLink(errtrans.getValue("ID_TERM")) %>
				</td><td><input type="text" name="id_term" size="20" value="<%= errtrans.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro" title="ID_TERM"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("state_trans_tsl", false) %></td> <td><input type="text" name="state_trans_tsl" size="30" value="<%= errtrans.getValue("STATE_TRANS_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_sam", false) %>
					<%= Bean.getGoToSAMLink(errtrans.getValue("ID_SAM")) %>
				</td><td><input type="text" name="id_sam" size="20" value="<%= errtrans.getValue("ID_SAM") %>" readonly="readonly" class="inputfield-ro" title="ID_SAM"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sys_date", false) %></td> <td><input type="text" name="sys_date" size="20" value="<%= errtrans.getValue("SYS_DATE") %>" readonly="readonly" class="inputfield-ro" title="SYS_DATE"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("nt_sam", false) %></td> <td><input type="text" name="nt_sam" size="20" value="<%= errtrans.getValue("NT_SAM") %>" readonly="readonly" class="inputfield-ro" title="NT_SAM"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sys_time", false) %></td> <td><input type="text" name="sys_time" size="20" value="<%= errtrans.getValue("SYS_TIME") %>" readonly="readonly" class="inputfield-ro" title="SYS_TIME"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("nt_ext", false) %></td><td><input type="text" name="nt_ext" size="20" value="<%= errtrans.getValue("NT_EXT") %>" readonly="readonly" class="inputfield-ro" title="NT_EXT"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("ver_trans", false) %></td> <td><input type="text" name="ver_trans" size="20" value="<%= errtrans.getValue("VER_TRANS") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("card_serial_number", false) %></td><td><input type="text" name="cd_card" size="20" value="<%= errtrans.getValue("CARD_NR") %>" readonly="readonly" class="inputfield-ro" title="CARD_NR"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("vk_enc", false) %></td><td><input type="text" name="vk_b" size="20" value="<%= errtrans.getValue("VK_ENC") %>" readonly="readonly" class="inputfield-ro" title="VK_ENC"></td>
				<td><%= Bean.transactionXML.getfieldTransl("cd_issuer", false) %> </td> <td> <input type="text" name="cd_issuer" size="20" value="<%= errtrans.getValue("CD_ISSUER") %>" readonly="readonly" class="inputfield-ro" title="ID_ISSUER"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("err_tx", false) %></td> <td><input type="text" name="err_tx" size="20" value="<%= errtrans.getValue("ERR_TX") %>" readonly="readonly" class="inputfield-ro" title="ERR_TX: <%= errtrans.getValue("ERR_TX") %> - <%= Bean.getSysERRTXDescription(errtrans.getValue("ERR_TX")) %>"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("cd_paym_sys", false) %> </td> <td> <input type="text" name="cd_paym_sys" size="20" value="<%= errtrans.getValue("CD_PAYM_SYS") %>" readonly="readonly" class="inputfield-ro" title="ID_PAYM_SYS"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("mac_icc", false) %></td> <td><input type="text" name="mac_icc" size="20" value="<%= errtrans.getValue("MAC_ICC") %>" readonly="readonly" class="inputfield-ro" title="MAC_ICC"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("nt_icc", false) %> </td> <td> <input type="text" name="nt_icc" size="20" value="<%= errtrans.getValue("NT_ICC") %>" readonly="readonly" class="inputfield-ro" title="NT_ICC"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("mac_pda", false) %></td> <td><input type="text" name="mac_pda" size="20" value="<%= errtrans.getValue("MAC_PDA") %>" readonly="readonly" class="inputfield-ro" title="MAC_PDA"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("cd_currency", false) %>
					<%= Bean.getGoToCurrencyLink(errtrans.getValue("CURR_PDA")) %>
				</td><td><input type="text" name="cd_currency" size="20" value="<%= errtrans.getValue("CURR_PDA") %>" readonly="readonly" class="inputfield-ro" title="CURR_PDA"> </td>
			</tr>
			<tr>
				<% if ("3".equalsIgnoreCase(errtrans.getValue("TYPE_TRANS"))) { %>
					<td><%= Bean.transactionXML.getfieldTransl("action", false) %></td><td><input type="text" name="p_action" id="p_action" size="35" value="<%= Bean.getMeaningFoCodeValue("REC_CHK_CARD_ACTION", errtrans.getValue("ACTIOIN")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else if ("4".equalsIgnoreCase(errtrans.getValue("TYPE_TRANS"))) { %>
					<td><%= Bean.transactionXML.getfieldTransl("action", false) %></td><td><input type="text" name="p_action" id="p_action" size="35" value="<%= Bean.getMeaningFoCodeValue("REC_CHK_INVAL_ACTION", errtrans.getValue("ACTION")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
					<td><input type="hidden" id="p_action" name="p_action" value="">&nbsp;</td> <td>&nbsp;</td>
					<td>&nbsp;</td><td>&nbsp;</td>
				<% } %>
			</tr>
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_before", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_st_prv", false) %></td> <td><input type="text" name="club_st_prv" size="20" value="<%= errtrans.getValue("CLUB_ST_PRV") %>" readonly="readonly" class="inputfield-ro" title="CLUB_ST_PRV"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("club_bon_prv", false) %></td><td><input type="text" name="club_bon_prv" size="20" value="<%= errtrans.getValue("CLUB_BON_PRV") %>" readonly="readonly" class="inputfield-ro" title="CLUB_BON_PRV"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("bal_acc_prv", false) %></td><td><input type="text" name="bal_acc_prv" size="20" value="<%= errtrans.getValue("BAL_ACC_PRV") %>" readonly="readonly" class="inputfield-ro" title="BAL_ACC_PRV"></td>
				<td><%= Bean.transactionXML.getfieldTransl("club_disc_prv", false) %></td> <td><input type="text" name="club_disc_prv" size="20" value="<%= errtrans.getValue("CLUB_DISC_PRV") %>" readonly="readonly" class="inputfield-ro" title="CLUB_DISC_PRV"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("bal_cur_prv", false) %></td><td><input type="text" name="bal_cur_prv" size="20" value="<%= errtrans.getValue("BAL_CUR_PRV") %>" readonly="readonly" class="inputfield-ro" title="BAL_CUR_PRV"></td>
				<td><%= Bean.transactionXML.getfieldTransl("date_acc_prv", false) %></td><td ><input type="text" name="date_acc_prv" size="20" value="<%= errtrans.getValue("DATE_ACC_PRV") %>" readonly="readonly" class="inputfield-ro" title="DATE_ACC_PRV"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("bal_bon_per_prv", false) %></td> <td><input type="text" name="bal_per_prv" size="20" value="<%= errtrans.getValue("BAL_BON_PER_PRV") %>" readonly="readonly" class="inputfield-ro" title="BAL_BON_PER_PRV"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("date_mov_prv", false) %></td> <td><input type="text" name="date_mov_prv" size="20" value="<%= errtrans.getValue("DATE_MOV_PRV") %>" readonly="readonly" class="inputfield-ro" title="DATE_MOV_PRV"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("bal_disc_per_prv", false) %></td> <td><input type="text" name="bal_disc_prv" size="20" value="<%= errtrans.getValue("BAL_DISC_PER_PRV") %>" readonly="readonly" class="inputfield-ro" title="BAL_DISC_PER_PRV"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("date_calc_prv", false) %></td> <td><input type="text" name="date_calc_prv" size="20" value="<%= errtrans.getValue("DATE_CALC_PRV") %>" readonly="readonly" class="inputfield-ro" title="DATE_CALC_PRV"> </td>
			</tr>
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_operation", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("opr_sum", false) %></td><td><input type="text" name="opr_sum" size="20" value="<%= errtrans.getValue("OPR_SUM") %>" readonly="readonly" class="inputfield-ro" title="OPR_SUM"></td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon", false) %></td><td><input type="text" name="sum_bon" size="20" value="<%= errtrans.getValue("SUM_BON") %>" readonly="readonly" class="inputfield-ro" title="SUM_BON"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sum_pay_cash", false) %></td><td><input type="text" name="sum_pay_cash" size="20" value="<%= errtrans.getValue("SUM_PAY_CASH") %>" readonly="readonly" class="inputfield-ro" title="SUM_PAY_CASH"></td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon_cash", false) %></td> <td><input type="text" name="sum_bon_cash" size="20" value="<%= errtrans.getValue("SUM_BON_CASH") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sum_pay_card", false) %></td> <td><input type="text" name="sum_pay_card" size="20" value="<%= errtrans.getValue("SUM_PAY_CARD") %>" readonly="readonly" class="inputfield-ro" title="SUM_PAY_CARD"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon_card", false) %></td> <td><input type="text" name="sum_bon_card" size="20" value="<%= errtrans.getValue("SUM_BON_CARD") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("sum_pay_bon", false) %></td> <td><input type="text" name="sum_pay_bon" size="20" value="<%= errtrans.getValue("SUM_PAY_BON") %>" readonly="readonly" class="inputfield-ro" title="SUM_PAY_BON"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon_bon", false) %></td> <td><input type="text" name="sum_bon_bon" size="20" value="<%= errtrans.getValue("SUM_BON_BON") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_sum", false) %></td> <td><input type="text" name="club_sum" size="20" value="<%= errtrans.getValue("CLUB_SUM") %>" readonly="readonly" class="inputfield-ro" title="CLUB_SUM"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_disc", false) %></td><td><input type="text" name="sum_disc" size="20" value="<%= errtrans.getValue("SUM_DISC") %>" readonly="readonly" class="inputfield-ro" title="SUM_DISC"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.transactionXML.getfieldTransl("sum_bon_disc", false) %></td> <td><input type="text" name="sum_bon_disc" size="20" value="<%= errtrans.getValue("SUM_BON_DISC") %>" readonly="readonly" class="inputfield-ro"> </td>
			</tr>
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_after", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_st_prv", false) %></td> <td><input type="text" name="club_st_prv" size="20" value="<%= errtrans.getValue("CLUB_ST") %>" readonly="readonly" class="inputfield-ro" title="CLUB_ST"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_bon", false) %></td><td><input type="text" name="club_bon" size="20" value="<%= errtrans.getValue("CLUB_BON") %>" readonly="readonly" class="inputfield-ro" title="CLUB_BON"></td>
				<td><%= Bean.transactionXML.getfieldTransl("bal_acc", false) %></td> <td><input type="text" name="bal_acc" size="20" value="<%= errtrans.getValue("BAL_ACC") %>" readonly="readonly" class="inputfield-ro" title="BAL_ACC"> </td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("club_disc", false) %></td> <td><input type="text" name="club_disc" size="20" value="<%= errtrans.getValue("CLUB_DISC") %>" readonly="readonly" class="inputfield-ro" title="CLUB_DISC"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("bal_cur", false) %></td><td><input type="text" name="bal_cur" size="20" value="<%= errtrans.getValue("BAL_CUR") %>" readonly="readonly" class="inputfield-ro" title="BAL_CUR"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("date_acc", false) %></td><td><input type="text" name="date_acc" size="20" value="<%= errtrans.getValue("DATE_ACC") %>" readonly="readonly" class="inputfield-ro" title="DATE_ACC"></td>
				<td><%= Bean.transactionXML.getfieldTransl("bal_bon_per", false) %></td><td><input type="text" name="bal_per" size="20" value="<%= errtrans.getValue("BAL_BON_PER") %>" readonly="readonly" class="inputfield-ro" title="BAL_BON_PER"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("date_mov", false) %></td> <td><input type="text" name="date_mov" size="20" value="<%= errtrans.getValue("DATE_MOV") %>" readonly="readonly" class="inputfield-ro" title="DATE_MOV"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("bal_disc_per", false) %></td> <td><input type="text" name="bal_disc" size="20" value="<%= errtrans.getValue("BAL_DISC_PER") %>" readonly="readonly" class="inputfield-ro" title="BAL_DISC_PER"> </td>
			</tr>
		
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_im", false) %></b></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("imid", false) %></td><td><input type="text" name="imid" size="20" value="<%= errtrans.getValue("IMID") %>" readonly="readonly" class="inputfield-ro" title="IMID"></td>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("specid", false) %></td><td><input type="text" name="specid" size="20" value="<%= errtrans.getValue("SPECID") %>" readonly="readonly" class="inputfield-ro" title="SPECID"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("cardid", false) %></td><td><input type="text" name="cardid" size="20" value="<%= errtrans.getValue("CARDID") %>" readonly="readonly" class="inputfield-ro" title="CARDID"></td>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("clubcard", false) %></td> <td><input type="text" name="clubcard" size="20" value="<%= Bean.getMeaningForNumValue("YES_NO", errtrans.getValue("CLUBCARD")) %>" readonly="readonly" class="inputfield-ro" title="CLUBCARD"></td>
			</tr>
		
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_check", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("fl_ext_loyl", false) %></td> <td><input type="text" name="fl_ext_loyl" size="20" value="<%= errtrans.getValue("FL_EXT_LOYL") %>" readonly="readonly" class="inputfield-ro" title="FL_EXT_LOYL"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("c_nr", false) %></td><td><input type="text" name="c_nr" size="20" value="<%= errtrans.getValue("C_NR") %>" readonly="readonly" class="inputfield-ro" title="C_NR"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("cash_card_nr", false) %></td><td><input type="text" name="cash_card_nr" size="20" value="<%= errtrans.getValue("CASH_CARD_NR") %>" readonly="readonly" class="inputfield-ro" title="CASH_CARD_NR"></td>
				<td><%= Bean.transactionXML.getfieldTransl("c_check_nr", false) %></td><td><input type="text" name="c_check_nr" size="20" value="<%= errtrans.getValue("C_CHECK_NR") %>" readonly="readonly" class="inputfield-ro" title="C_CHECK_NR"></td>
			</tr>
		
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_rec_payment_ext_param", false) %></b></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("c_nomenkl", false) %></td><td  colspan="3"><input type="text" name="c_nomenkl" id="c_nomenkl" size="74" value="<%= errtrans.getValue("C_NOMENKL") %>" readonly="readonly" class="inputfield-ro" title="C_NOMENKL"></td>
			</tr>
			<tr>
				<td valign="top" class="top_line"><%= Bean.transactionXML.getfieldTransl("src_trans", false) %></td> 
					<td colspan="3" class="top_line"><textarea name="src_trans" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= errtrans.getValue("SRC_TRANS") %></textarea></td>
			</tr>
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_result", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("id_trans_reversed", false) %>
					<%= Bean.getGoToTransactionLink(errtrans.getValue("ID_TRANS_REVERSED")) %>
				</td> 
					<td><input type="text" name="id_trans_reversed" size="20" value="<%= errtrans.getValue("ID_TRANS_REVERSED") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("id_trans_double", false) %>
					<%= Bean.getGoToTransactionLink(errtrans.getValue("ID_TRANS_DOUBLE")) %>
				</td>
				<td><input type="text" name="id_trans_double" size="20" value="<%= errtrans.getValue("ID_TRANS_DOUBLE") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_user_changing", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("changed_by_user", false) %></td> <td><input type="text" name="changed_by_user" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", errtrans.getValue("CHANGED_BY_USER")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("date_last_users_changes", false) %></td><td><input type="text" name="date_last_users_changes" size="20" value="<%= errtrans.getValue("DATE_LAST_USERS_CHANGES") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.transactionXML.getfieldTransl("user_who_has_changed", false) %></td> <td><input type="text" name="user_who_has_changed" size="20" value="<%= Bean.getUserName(errtrans.getValue("USER_WHO_HAS_CHANGED")) %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.transactionXML.getfieldTransl("entered_manually", false) %></td><td><input type="text" name="entered_manually" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", errtrans.getValue("ENTERED_MANUALLY")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.transactionXML.getfieldTransl("bases_for_changes", false) %></td><td colspan=7><textarea name="bases_for_changes" cols="100" rows="3" readonly="readonly" class="inputfield-ro"><%= errtrans.getValue("BASES_FOR_CHANGES") %></textarea></td>
			</tr>

		<% } else if ("P".equalsIgnoreCase(errtrans.getValue("ERR_TYPE"))) { %>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("id_trans", false) %>
			<% if ("Y".equalsIgnoreCase(errtrans.getValue("ENTERED_MANUALLY"))) { %>
				<b><font color="red">(<%= Bean.transactionXML.getfieldTransl("h_entered_manually", false) %>)</font></b>
			<% } %>
            </td> <td><input type="text" name="id_trans" size="20" value="<%= transid %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.transactionXML.getfieldTransl("id_telgr", false) %>
				<%= Bean.getGoToTelegramLink(errtrans.getValue("ID_TELGR")) %>
			</td> <td><input type="text" name="id_telgr" size="20" value="<%= errtrans.getValue("ID_TELGR") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("type_trans_txt", false) %></td> <td><input type="text" name="type_trans_txt" size="20" value="<%= errtrans.getValue("TYPE_TRANS_TXT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.transactionXML.getfieldTransl("id_term", false) %>
				<%= Bean.getGoToTerminalLink(errtrans.getValue("ID_TERM")) %>
			</td><td><input type="text" name="id_term" size="20" value="<%= errtrans.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("state_trans_tsl", false) %></td> <td><input type="text" name="state_trans" size="20" value="<%= errtrans.getValue("STATE_TRANS_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.transactionXML.getfieldTransl("is_crypted", false) %></td> <td><input type="text" name="is_crypted" size="20" value="<%= errtrans.getValue("IS_CRYPTED_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("ver_trans", false) %></td> <td><input type="text" name="ver_trans" size="20" value="<%= errtrans.getValue("VER_TRANS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("fl_has_format_error", false) %></td> <td><input type="text" name="fl_has_format_error" size="20" value="<%= errtrans.getValue("FL_HAS_FORMAT_ERROR_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.transactionXML.getfieldTransl("entered_manually", false) %></td><td><input type="text" name="entered_manually" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", errtrans.getValue("ENTERED_MANUALLY")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("src_trans", false) %></td> 
				<td colspan="3"><textarea name="src_trans" cols="150" rows="3" readonly="readonly" class="inputfield-ro"><%= errtrans.getValue("SRC_TRANS") %></textarea></td>
		</tr>
			
    	<%}%>
		
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.commonXML.getfieldTransl("h_record_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.commonXML.getfieldTransl("creation_date", false)%></td> <td><input type="text" name="creation_date" size="20" value="<%=errtrans.getValue(Bean.getCreationDateFieldName())%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.commonXML.getfieldTransl("created_by", false)%></td> <td><input type="text" name="created_by" size="20" value="<%=Bean.getSystemUserName(errtrans.getValue("CREATED_BY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getGoBackButton("../crm/cards/trans_correction.jsp") %>
			</td>
		</tr>
		</table>
		</form>
	 

	 <% 
  }
  }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_TELGR")) {%>
	<%= errtrans.getTransactionsTelegramsHTML() %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_TRANSACTIONS_LOG")) {%>
    <% bcSysLogObject log = new bcSysLogObject(); %>

		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("log_find", log_find, "../crm/cards/trans_correctionspecs.jsp?id="+transid + "&log_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("log_row_type", "../crm/cards/trans_correctionspecs.jsp?id=" + transid + "&log_page=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
				<%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", log_row_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>			

	<%= log.getSysLogTermInterchangeHTML(log_find, "", "", "", transid, log_row_type, l_log_beg, l_log_end) %>
<%}

%>

<%   } %>
</div></div>
</body>
</html>
