<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="webpos.wpNatPrsObject"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="java.util.ArrayList"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_INVOICE_PAY";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

type 	= Bean.isEmpty(type)?"":type;
action 	= Bean.isEmpty(action)?"":action;
process = Bean.isEmpty(process)?"":process;

String id_term	= Bean.getCurrentTerm();
String cd_card1	= Bean.getDecodeParam(parameters.get("cd_card1"));
cd_card1		= Bean.isEmpty(cd_card1)?"":cd_card1.replace(" ", "");

Bean.readWebPosMenuHTML();

Bean.loginTerm.getTermFeature();
String termCurrency = Bean.loginTerm.getValue("SNAME_TERM_CURRENCY");

String idClub = Bean.loginTerm.getValue("ID_CLUB");
String idDealer = Bean.loginTerm.getValue("ID_DEALER");

String marginChangeText = Bean.getMarginChangeToShareAccountDescription(idClub, idDealer);
String marginRobokassaText = Bean.getMarginRobokassaPaymentOnTerminalDescription(idClub, idDealer);

String
	id_telgr				= Bean.getDecodeParamPrepare(parameters.get("id_telgr")),
	id_telgr_invoice		= Bean.getDecodeParamPrepare(parameters.get("id_telgr_invoice")),
	pay_type				= Bean.getDecodeParamPrepare(parameters.get("pay_type")),
	can_use_share_account	= Bean.getDecodeParamPrepare(parameters.get("can_use_share_account")),
	share_fee				= Bean.getDecodeParamPrepare(parameters.get("share_fee")),
	share_fee_margin		= Bean.getDecodeParamPrepare(parameters.get("share_fee_margin")),
	entered_sum				= Bean.getDecodeParamPrepare(parameters.get("entered_sum")),
	sum_change				= Bean.getDecodeParamPrepare(parameters.get("sum_change")),
	change_to_share_account	= Bean.getDecodeParamPrepare(parameters.get("change_to_share_account")),
	bank_trn				= Bean.getDecodeParamPrepare(parameters.get("bank_trn")),
	replenish_kind			= Bean.getDecodeParamPrepare(parameters.get("replenish_kind")),
	payment_description		= Bean.getDecodeParamPrepare(parameters.get("replenish_kind"));

String snamePointCurrency = Bean.webposXML.getfieldTransl("point_currency_name", false);

wpChequeObject invoice = new wpChequeObject(id_telgr_invoice, "TXT", Bean.loginTerm);
String oper_types		= Bean.getTransactionShortNameList(invoice);
String pay_value		= invoice.getValue("OPR_SUM_FRMT");


%>


<% 
// Здсесь продумать на счет контроля доступа к оплате счетов
if ( !true /*!Bean.hasMenuPermission(pageFormName, Bean.loginTerm)*/) { %>
	<%=Bean.getErrorPermissionMessageShort(pageFormName, Bean.loginTerm) %>
	<% type = "error_term"; %>
<% } %>
<%
if (type.equalsIgnoreCase("error_term")) {
} else if (type.equalsIgnoreCase("term")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("calc")) {
			%>
			<script>
		function validateBonTerm(){
			var returnValue = null;
			try {
			var formAll = new Array (
				new Array ('cd_card1', 'card', 1)
			);
			var formPayBankCard = new Array (
				new Array ('bank_trn', 'varchar2', 1)
			);
	
			<% if ("BANK_CARD".equalsIgnoreCase(pay_type)) { %>
			formAll = formAll.concat(formPayBankCard);
			<% } %>
			returnValue = validateFormForID(formAll, 'updateForm');
			<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
			if (returnValue) {
				returnValue = validateChange();
			}
			<% } %>
			} catch(err2) {alert(err2);}
			return returnValue;
		}
		</script>
			<h1><%=Bean.webposXML.getfieldTransl("operation_cheque_payment", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="term">
			        <input type="hidden" name="action" value="calc">
			        <input type="hidden" name="process" value="yes">
			        <input type="hidden" name="id_telgr_invoice" value="<%=id_telgr_invoice %>">
			        <input type="hidden" name="pay_value" id="pay_value" value="<%=invoice.getValue("OPR_SUM_FRMT") %>">
			        <input type="hidden" name="pay_type" value="<%=pay_type %>">
			        <input type="hidden" name="id_term" value="<%=id_term %>">
					<input id="cd_card1" name="cd_card1" type="hidden" value="<%=cd_card1 %>">
					<input id="replenish_kind" name="replenish_kind" type="hidden" value="<%=replenish_kind %>">
					<% if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) { %>
			        <input type="hidden" name="share_fee" value="<%=invoice.getValue("OPR_SUM_FRMT") %>">
					<% } %>
					<input id="share_fee_margin" name="share_fee_margin" type="hidden" value="<%=share_fee_margin %>">
					<table class="action_table">
			  			<!-- <tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><input type="text" name="doc_number" id="doc_number" size="20" value="<%=invoice.getValue("NC") %>" readonly class="inputfield_finish"></td></tr> -->
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_invoice", false) %></td><td><input type="text" name="rrn_txt" id="rrn_txt" size="20" value="<%=invoice.getValue("RRN") %>" readonly class="inputfield_finish_blue"></td></tr>
			  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=invoice.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish_green"></td></tr>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("operation_date", false) %></td><td><input type="text" name="operation_date" id="operation_date" size="20" value="<%=invoice.getValue("DATE_TELGR_DHMF") %>" readonly class="inputfield_finish_green"></td><tr>
						<% if (!Bean.isEmpty(invoice.getValue("EXPIRATION_DATE"))) { %>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("invoice_expiration_date", false) %></td><td><input type="text" name="expiration_date" id="expiration_date" size="20" value="<%=invoice.getValue("EXPIRATION_DATE_DHMF") %>" readonly class="inputfield_finish_red"></td><tr>
						<% } %>
						<tr><td><%=Bean.webposXML.getfieldTransl("operation_type", false) %></td><td colspan="2"><span id="operation_type" class="inputfield_finish_red"><%=oper_types %></span></td></tr>
						<tr><td class="result_desc"><%=Bean.webposXML.getfieldTransl("operation_sum", false) %></td><td><input type="text" name="input_sum" id="input_sum" size="20" value="<%=invoice.getValue("OPR_SUM_FRMT") + " " + invoice.getValue("SNAME_CURRENCY") %>" readonly class="inputfield_finish_blue"></td></tr>
						<% if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td><td><input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green"></td></tr>
							<tr><td colspan="2" class="through_share_fee top_line_gray line_dashed" style="color:green"><%=Bean.webposXML.getfieldTransl("title_payment_through_share_fee", false) %></td></tr>
							<tr><td class="through_share_fee result_desc"><%=Bean.webposXML.getfieldTransl("cheque_share_fee", false) %></td><td colspan="2" class="through_share_fee"><input type="text" name="opr_sum_txt" id="opr_sum_txt" size="20" value="<%=pay_value %> <%=termCurrency %>" class="inputfield_finish_red" maxlength="15"></td></tr>
							
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
								<td>
									<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></td>
								<td>
									<input type="text" name="entered_sum" id="entered_sum" size="20" value="<%=entered_sum %>" class="inputfield" maxlength="20"  onchange="calcChange();" title="<%=Bean.webposXML.getfieldTransl("entered_sum_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
								</td>
							</tr>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td>
								<td><input type="text" name="sum_change" id="sum_change" size="20" value="<%=sum_change %>" class="inputfield-ro" readonly maxlength="20" title="<%=Bean.webposXML.getfieldTransl("sum_change_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
									<input type="hidden" name="change_calc_error" id="change_calc_error" value="N">
									<br>
									<input type="checkbox" name="change_to_share_account" id="change_to_share_account" <% if ("Y".equalsIgnoreCase(change_to_share_account)) { %> CHECKED <% } %> disabled="disabled" class="inputfield-ro" title="<%=Bean.webposXML.getfieldTransl("change_to_share_account_title", false) %>">
									<label class="checbox_label" for="change_to_share_account"><%=Bean.webposXML.getfieldTransl("change_to_share_account", false) %></label>
									<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("bank_trn", true) %></td>
								<td>
									<input type="text" name="bank_trn" id="bank_trn" size="20" value="<%=bank_trn %>" class="inputfield">
									<% } else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
									<% } else if ("ROBOKASSA".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_robokassa", false) %>" readonly class="inputfield_finish_green">
									<% } %>
								</td>
							</tr>
						<% } else { 
							String payValue = invoice.getValue("OPR_SUM_FRMT");
							String robokassaMargin = "";
							String robokassaMarginValue = "";
							if ("ROBOKASSA".equalsIgnoreCase(pay_type)) {
								payValue = invoice.getValue("ROBOKASSA_OPR_SUM_FRMT");
								robokassaMargin = invoice.getValue("ROBOKASSA_MARGIN");
								robokassaMarginValue = invoice.getValue("ROBOKASSA_MARGIN_FRMT");
							}
							%>
							<tr><td class="result_desc"  style="padding-top: 5px; border-top: 1px dashed gray;"><%=Bean.webposXML.getfieldTransl("pay_total", false) %></td><td style="padding-top: 5px; border-top: 1px dashed gray;"><input type="text" name="input_sum" id="input_sum" size="20" value="<%=payValue + " " + invoice.getValue("SNAME_CURRENCY") %>" readonly class="inputfield_finish_red"></td></tr>
		  					<tr>
								<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
								<td>
									<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></td>
								<td>
									<input type="text" name="entered_sum" id="entered_sum" size="20" value="<%=entered_sum %>" class="inputfield" maxlength="20"  onchange="calcChange();" title="<%=Bean.webposXML.getfieldTransl("entered_sum_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
								</td>
							</tr>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td>
								<td><input type="text" name="sum_change" id="sum_change" size="20" value="<%=sum_change %>" class="inputfield-ro" readonly maxlength="20" title="<%=Bean.webposXML.getfieldTransl("sum_change_title", false) %>"><input type="text" name="sname_term_currency" size="5" value="<%=termCurrency %>" readonly class="inputfield-ro">
									<input type="hidden" name="change_calc_error" id="change_calc_error" value="N">
									<br>
									<input type="checkbox" name="change_to_share_account" id="change_to_share_account" <% if ("Y".equalsIgnoreCase(change_to_share_account)) { %> CHECKED <% } %> disabled="disabled" class="inputfield-ro" title="<%=Bean.webposXML.getfieldTransl("change_to_share_account_title", false) %>">
									<label class="checbox_label" for="change_to_share_account"><%=Bean.webposXML.getfieldTransl("change_to_share_account", false) %></label>
									<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("bank_trn", true) %></td>
								<td>
									<input type="text" name="bank_trn" id="bank_trn" size="20" value="<%=bank_trn %>" class="inputfield">
									<% } else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
									<% } else if ("ROBOKASSA".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_robokassa", false) %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
							<% if (!(Bean.isEmpty(robokassaMargin) || "0".equalsIgnoreCase(robokassaMargin))) { %>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("robokassa_margin", false) %></td>
								<td>
									<input type="text" name="robokassa_margin" id="robokassa_margin" size="20" value="<%=robokassaMarginValue %> <%=termCurrency %>" readonly class="inputfield_finish_blue">
								</td>
							</tr>
							<% } %>
							<tr>
								<td class="result_desc"><%=Bean.webposXML.getfieldTransl("pay_description", false) %></td><td><span id="pay_description" class="inputfield_finish_green"><%=Bean.getInvoicePaymendDescription(invoice) %></span>
									<% } %>
								</td>
							</tr>
						<% } %>
				
						<tr><td colspan="2" class="left">&nbsp;</td></tr>
						<tr><td colspan="4" class="center">
							<% if ("ROBOKASSA".equalsIgnoreCase(pay_type)) {  
								String payRobokassaLink 	= Bean.getRobokassaPaymentLink(invoice);
								%>
								<button class="button" type="button" onclick="ajaxpage('report/operationupdate.jsp?' + mySubmitForm('updateFormBack'),'div_action_big'); window.open('<%=payRobokassaLink %>')">Оплатить</button>
							<% } else { %>
								<%=Bean.getSubmitButtonAjax("report/invoicepay.jsp", "pay", "updateForm", "div_action_big", "validateBonTerm", "pay_button", true) %>
							<% } %> 
							<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
						</td></tr>
						<tr><td colspan="2" class="left">
							<div id=div_hints>
								<% if ("CASH".equalsIgnoreCase(pay_type) && !Bean.isEmpty(marginChangeText)) { %>
								<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %></b><br>
									<%=marginChangeText %><br>
								</i>
								<% } else if ("ROBOKASSA".equalsIgnoreCase(pay_type) && !Bean.isEmpty(marginRobokassaText)) { %>
								<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %></b><br>
								<%=marginRobokassaText %><br>
								</i>
								<% } %> 
							</div>
						</td></tr>
					</table>
				</form>
	<form class="hiddenForm" name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
		<input type="hidden" name="id_telgr" value="<%=id_telgr_invoice %>">
		<input type="hidden" name="type" value="trans">
		<input type="hidden" name="process" value="no">
		<input type="hidden" name="action" value="pay">
 	</form>
			<% 

		} else { 
			%> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
		String resultInt 				= Bean.C_SUCCESS_RESULT;
 		String resultMessage 			= "";	    
 		
 		String sum_share_fee_need			= "";
 		String change_margin 				= "";
 		String total_margin 				= "";
 		String sum_put_to_share_account		= "";
 		String sum_get_from_share_account	= "";
		String phone_mobile	 				= "";
		String can_send_pin_in_sms			= "";	
		
		boolean shareFeePermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE");
		
		if (action.equalsIgnoreCase("calc")) {
			boolean hasPermission = true;		
			/*if ("CASH".equalsIgnoreCase(pay_type) && !(Bean.hasMenuElementPermission("WEBPOS_SERVICE_PAY_CASH"))) {
				resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
				resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_cash_forbidden", false);
				hasPermission 	= false;
			} else if ("BANK_CARD".equalsIgnoreCase(pay_type) && !(Bean.hasMenuElementPermission("WEBPOS_SERVICE_PAY_BANK_CARD"))) {
				resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
				resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_card_forbidden", false);
				hasPermission 	= false;
			} else if ("SMPU_CARD".equalsIgnoreCase(pay_type) && !(Bean.hasMenuElementPermission("WEBPOS_SERVICE_PAY_POINTS"))) {
				resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
				resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_points_forbidden", false);
				hasPermission 	= false;
			} else if ("INVOICE".equalsIgnoreCase(pay_type) && !(Bean.hasMenuElementPermission("WEBPOS_SERVICE_MAKE_INVOICE"))) {
				resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
				resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_invoice_forbidden", false);
				hasPermission 	= false;
			}*/
			
			if (hasPermission) {
				ArrayList<String> pParam = new ArrayList<String>();	
				
				pParam.add(id_term);
				pParam.add(id_telgr_invoice);
				pParam.add(replenish_kind);
				pParam.add(pay_type);
				pParam.add(bank_trn);
				pParam.add(pay_value);
				pParam.add(share_fee);
				pParam.add(share_fee_margin);
				pParam.add(entered_sum);
				pParam.add(sum_change);
				pParam.add(change_to_share_account);
				pParam.add(can_use_share_account);
				
				String[] results = new String[11];
				
				results 					= Bean.executeFunction("PACK$WEBPOS_UI.payment_invoice", pParam, results.length);
				resultInt 					= results[0];
				id_telgr 				    = results[1];
				sum_share_fee_need			= results[2];
				share_fee_margin			= results[3];
				change_margin	 			= results[4];
				total_margin				= results[5];
				sum_put_to_share_account	= results[6];
				sum_get_from_share_account	= results[7];
				phone_mobile	 			= results[8];
				can_send_pin_in_sms			= results[9];
		 		resultMessage 				= results[10];
			}
		} else if (action.equalsIgnoreCase("oper_confirm")) {
			String
				confirm_type		= Bean.getDecodeParam(parameters.get("confirm_type")),
				confirm_code		= Bean.getDecodeParam(parameters.get("confirm_code"));
				

			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_telgr);
			pParam.add(confirm_type);
			pParam.add(confirm_code);
			pParam.add(payment_description);
			
			String[] results = new String[4];
			
			results 				= Bean.executeFunction("PACK$WEBPOS_UI.oper_confirm", pParam, results.length);
			resultInt 				= results[0];
			phone_mobile	 		= results[1];
			can_send_pin_in_sms  	= results[2];
	 		resultMessage 			= results[3];
		} else {
			%> 
			<%= Bean.getUnknownActionText(action) %><%
		}
		
		
	   	boolean isErrorResult = true;
	 	if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt) || 
	 			Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt) || 
				Bean.C_NEED_PIN.equalsIgnoreCase(resultInt) || 
	 			Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt) || 
	 			Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt) || 
				Bean.C_ISNT_ENOUGH_POINTS.equalsIgnoreCase(resultInt) || 
				Bean.C_SMS_CONFIRM_CREATED.equalsIgnoreCase(resultInt)) {
	 		isErrorResult = false;
	 	}
			
		if (action.equalsIgnoreCase("calc") || action.equalsIgnoreCase("oper_confirm")) {
			
			if (action.equalsIgnoreCase("oper_confirm") && Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { 
				wpChequeObject cheque = new wpChequeObject(id_telgr, Bean.getChequeSaveFormat(), Bean.loginTerm);
			%>
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("operation_cheque_payment", false) %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation", false) %><%=Bean.getHelpButton("pay_invoice", "div_action_big") %></h1>
						<table class="table_cheque"><tbody>
						<tr><td class="centerb">
							<%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></td><td>
							<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateForm", "div_main") %><br><br>
						</td></tr>
						<%=cheque.getChequeHTML(true) %>
						</tbody></table>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	 			</form>
			<% 
			} else {
				
				wpChequeObject oper = new wpChequeObject(id_telgr, "TXT", Bean.loginTerm);
				
				
	    	%>
			
			<script>
				function validateData(){
					var formParam = new Array (
						new Array ('confirm_code', 'number', 1)
					);
					return validateFormForID(formParam, 'updateForm');
				}
				//card_mask2("cd_card1");
			</script>
				<%if (isErrorResult) { %>
				<h1 class="error"><%=Bean.webposXML.getfieldTransl("operation_cheque_payment", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("pay_invoice", "div_action_big") %></h1>
				<% } else { %>
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("operation_cheque_payment", false) %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation", false) %><%=Bean.getHelpButton("pay_invoice", "div_action_big") %></h1>
				<% } %>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
				<input type="hidden" name="id_telgr_invoice" value="<%=id_telgr_invoice %>">
				<input type="hidden" name="type" value="term">
				<% if (Bean.C_ISNT_ENOUGH_POINTS.equalsIgnoreCase(resultInt)) { %>
		    	<input type="hidden" name="action" value="calc">
		    	<input type="hidden" name="pay_value" value="<%=pay_value %>">
		    	<input type="hidden" name="bank_trn" value="<%=bank_trn %>">
		    	<input type="hidden" name="replenish_kind" value="GET_FROM_SHARE_ACCOUNT">
		    	<input type="hidden" name="share_fee" value="<%=sum_get_from_share_account %>">
				<% } else if (Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultInt)) { %>
		    	<input type="hidden" name="action" value="calc">
		    	<input type="hidden" name="pay_value" value="<%=sum_share_fee_need %>">
		    	<input type="hidden" name="bank_trn" value="<%=bank_trn %>">
				<% } else { %>
    			<input type="hidden" name="action" value="oper_confirm">
				<% } %>
				<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
    			<input type="hidden" name="confirm_type" value="PIN">
				<% } else if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
    			<input type="hidden" name="confirm_type" value="SMS">
				<% } else if (Bean.C_SMS_CONFIRM_CREATED.equalsIgnoreCase(resultInt)) { %>
    			<input type="hidden" name="confirm_type" value="SMS">
				<% } else if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) { %>
    			<input type="hidden" name="confirm_type" value="ACTIVATION_CODE">
				<% } else if (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt) ||
						Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>
    			<input type="hidden" name="confirm_type" value="NONE">
				<% } %>
				<input type="hidden" name="entered_sum" value="<%=entered_sum %>">
				<input type="hidden" name="sum_change" value="<%=sum_change %>">
				<input type="hidden" name="change_to_share_account" value="<%=change_to_share_account %>">
				<input type="hidden" name="id_term" value="<%=id_term %>">
    			<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
				<% if (!Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultInt)) { %>
    			<input type="hidden" name="pay_type" value="<%=pay_type %>">
				<input type="hidden" name="process" value="yes">
				<% } else { %>
				<input type="hidden" name="process" value="no">
				<% } %>
    			<input type="hidden" name="pay_value" value="<%=pay_value %>">
    			<input type="hidden" name="pay_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
					<input id="share_fee_margin" name="share_fee_margin" type="hidden" value="<%=share_fee_margin %>">
		    <table class="action_table">
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_invoice", false) %></td><td><input type="text" name="rrn_txt" id="rrn_txt" size="20" value="<%=invoice.getValue("RRN") %>" readonly class="inputfield_finish_blue"></td></tr>
			  	<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=oper.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish_green"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("operation_date", false) %></td><td><input type="text" name="operation_date" id="operation_date" size="20" value="<%=oper.getValue("DATE_TELGR_DHMF") %>" readonly class="inputfield_finish_green"></td><tr>
		
				<tr><td><%=Bean.webposXML.getfieldTransl("operation_type", false) %></td><td colspan="2"><span id="operation_type" class="inputfield_finish_red"><%=oper_types %></span></td></tr>
				<tr><td class="result_desc"><%=Bean.webposXML.getfieldTransl("operation_sum", false) %></td><td><input type="text" name="input_sum" id="input_sum" size="20" value="<%=oper.getValue("OPR_SUM_FRMT") + " " + oper.getValue("SNAME_CURRENCY") %>" readonly class="inputfield_finish_blue"></td></tr>
			
				<% if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) { %>
					<tr><td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td><td><input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green"></td></tr>
					<tr><td colspan="2" class="through_share_fee top_line_gray line_dashed" style="color:green"><%=Bean.webposXML.getfieldTransl("title_payment_through_share_fee", false) %></td></tr>
					<tr><td class="through_share_fee result_desc"><%=Bean.webposXML.getfieldTransl("cheque_share_fee", false) %></td><td colspan="2" class="through_share_fee"><input type="text" name="opr_sum_txt" id="opr_sum_txt" size="20" value="<%=pay_value %> <%=termCurrency %>" class="inputfield_finish_red" maxlength="15"></td></tr>
					<tr>
						<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
						<td class="through_share_fee">
							<% if ("CASH".equalsIgnoreCase(pay_type)) { 
								entered_sum = Bean.isEmpty(entered_sum)?pay_value:entered_sum;
								sum_change = Bean.isEmpty(sum_change)?"0":sum_change;
								
							%>
							<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
						</td>
					</tr>
		  			<tr>
						<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></td>
						<td class="through_share_fee">
							<input type="text" name="entered_sum_title" id="entered_sum_title" size="20" value="<%=entered_sum %> <%=termCurrency %>" readonly class="inputfield_finish_blue">
						</td>
					</tr>
		  			<tr>
					<% if ("Y".equalsIgnoreCase(change_to_share_account)) { %>
						<tr><td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("sum_change_to_share_account", false) %></td><td class="through_share_fee"><input type="text" name="sum_change_title" id="sum_change_title" size="20" value="<%=sum_change %> <%=termCurrency %>" readonly class="inputfield_finish_green"></td></tr>
					<% } else { %>
						<tr><td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td><td class="through_share_fee"><input type="text" name="sum_change_title" id="sum_change_title" size="20" value="<%=sum_change %> <%=termCurrency %>" readonly class="inputfield_finish_green"></td></tr>
					<% } %>
					<% if (!Bean.isEmpty(change_margin)) { %>
						<tr><td class="through_share_fee"><i><%=Bean.webposXML.getfieldTransl("share_fee_change_margin", false) %></i></td><td colspan="2" class="through_share_fee"><input type="text" name="change_margin_txt" id="change_margin_txt" size="20" value="<%=change_margin %> <%=termCurrency %>" class="inputfield_finish_green" maxlength="15"></td></tr>
					<% } %>
		  			<tr>
						<% if ("Y".equalsIgnoreCase(change_to_share_account)) { %>
							<td class="through_share_fee"><b><%=Bean.webposXML.getfieldTransl("add_share_account_from_change", false) %></b></td><td class="through_share_fee"><input type="text" name="sum_put_share_account" id="sum_put_share_account" size="20" value="<%=sum_put_to_share_account %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td><td>
						<% } else { %>
							<td class="through_share_fee">
						<% } %>
							<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
							<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
						</td>
					</tr>
		  			<tr>
						<td class="through_share_fee"><%=Bean.webposXML.getfieldTransl("bank_trn", false) %></td>
						<td class="through_share_fee">
							<input type="text" name="bank_trn" id="bank_trn" size="20" value="<%=bank_trn %>" readonly class="inputfield_finish_green">
							<% } else if ("INVOICE".equalsIgnoreCase(pay_type)) {  %>
							<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_invoices", false) %>" readonly class="inputfield_finish_green">
							<% } %>
						</td>
					</tr>
				<% } else { %>
					<tr><td class="result_desc"  style="padding-top: 5px; border-top: 1px dashed gray;"><%=Bean.webposXML.getfieldTransl("pay_total", false) %></td><td style="padding-top: 5px; border-top: 1px dashed gray;"><input type="text" name="input_sum" id="input_sum" size="20" value="<%=oper.getValue("OPR_SUM_FRMT") + " " + oper.getValue("SNAME_CURRENCY") %>" readonly class="inputfield_finish_red"></td></tr>
		  			<tr>
						<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
						<td>
							<% if ("CASH".equalsIgnoreCase(pay_type)) { 
								entered_sum = Bean.isEmpty(entered_sum)?pay_value:entered_sum;
								sum_change = Bean.isEmpty(sum_change)?"0":sum_change;
								
							%>
							<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
						</td>
					</tr>
					<tr>
						<td class="pay_cash_change"><b><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></b></td>
						<td class="pay_cash_change">
							<input type="text" name="entered_sum_title" id="entered_sum_title" size="20" value="<%=entered_sum %> <%=termCurrency %>" readonly class="inputfield_finish_blue">
						</td>
					</tr>
		  			<tr>
					<% if ("Y".equalsIgnoreCase(change_to_share_account)) { %>
						<tr><td class="pay_cash_change"><%=Bean.webposXML.getfieldTransl("sum_change_to_share_account", false) %></td><td class="pay_cash_change"><input type="text" name="sum_change_title" id="sum_change_title" size="20" value="<%=sum_change %> <%=termCurrency %>" readonly class="inputfield_finish_green"></td></tr>
					<% } else { %>
						<tr><td class="pay_cash_change"><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td><td class="pay_cash_change"><input type="text" name="sum_change_title" id="sum_change_title" size="20" value="<%=sum_change %> <%=termCurrency %>" readonly class="inputfield_finish_green"></td></tr>
					<% } %>
					<% if (!Bean.isEmpty(change_margin)) { %>
						<tr><td class="pay_cash_change"><i><%=Bean.webposXML.getfieldTransl("share_fee_change_margin", false) %></i></td><td colspan="2" class="pay_cash_change"><input type="text" name="change_margin_txt" id="change_margin_txt" size="20" value="<%=change_margin %> <%=termCurrency %>" class="inputfield_finish_green" maxlength="15"></td></tr>
					<% } %>
		  			<tr>
						<% if ("Y".equalsIgnoreCase(change_to_share_account)) { %>
							<td class="pay_cash_change"><b><%=Bean.webposXML.getfieldTransl("add_share_account_from_change", false) %></b></td><td class="pay_cash_change"><input type="text" name="sum_put_share_account" id="sum_put_share_account" size="20" value="<%=sum_put_to_share_account %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td><td>
						<% } else { %>
							<td>
						<% } %>
							<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
							<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
						</td>
					</tr>
		  			<tr>
						<td><%=Bean.webposXML.getfieldTransl("bank_trn", false) %></td>
						<td>
							<input type="text" name="bank_trn" id="bank_trn" size="20" value="<%=bank_trn %>" readonly class="inputfield_finish_green">
							<% } else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {  %>
							<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
							<% } else if ("ROBOKASSA".equalsIgnoreCase(pay_type)) {  %>
							<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_robokassa", false) %>" readonly class="inputfield_finish_green">
						</td>
					</tr>
					<tr>
					<tr>
						<td><%=Bean.webposXML.getfieldTransl("robokassa_margin", false) %></td>
						<td>
							<input type="text" name="robokassa_margin" id="robokassa_margin" size="20" value="<%=total_margin %> <%=termCurrency %>" readonly class="inputfield_finish_blue">
						</td>
					</tr>
					<tr>
					<tr>
						<td class="result_desc"><%=Bean.webposXML.getfieldTransl("pay_description", false) %></td><td><textarea name="pay_description" cols="27" rows="3" class="inputfield"><%=Bean.getInvoicePaymendDescription(oper) %></textarea>
							<% } %>
						</td>
					</tr>
				<% } %>
				<% if (Bean.C_ISNT_ENOUGH_POINTS.equalsIgnoreCase(resultInt)) { %>				
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td><span class="can_use_share_account"><%= Bean.webposXML.getfieldTransl("can_use_share_account", false) %></span></td><td><select name="can_use_share_account" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", can_use_share_account, false) %></select></td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("report/invoicepay.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
					</td>
				</tr>
				<% } else if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_pin", false) %></td></tr>
				<% if (!Bean.isEmpty(resultMessage)) { %>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<% } %>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_pin"><%=Bean.webposXML.getfieldTransl("title_pin", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_PIN_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_PIN_PLACEHOLDER.length() %>"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("report/invoicepay.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
					</td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% if ("Y".equalsIgnoreCase(can_send_pin_in_sms)) { %>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_pin_remind", false) %> <%=phone_mobile %>)</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("report/invoicepay.jsp", "remind_pin", "updateForm2", "div_action_big") %>
					</td>
				</tr>
				<% } %>
				<% } else if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2">
					<div id="div_sms_confirmation">
						<%=(!Bean.isEmpty(resultMessage))?"<span id=\"error_description\">" + resultMessage + "</span><br><br>":"" %>
						<div style="width:100%; text-align: center;">
					        <%=Bean.getSubmitButtonAjax("service/get_sms_code.jsp", "button_send_sms", "updateFormGetSMS", "div_sms_confirmation", "") %>
					        <%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
						</div>
					</div>
				</td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } else if (Bean.C_SMS_CONFIRM_CREATED.equalsIgnoreCase(resultInt)) {
		    		wpNatPrsObject nat_prs = new wpNatPrsObject(oper.getValue("ID_NAT_PRS"));
		    	    %>
				<tr><td colspan="2"><br>
					<div id="div_sms_confirmation">
		    		<% if (!(nat_prs.getValue("FULL_PHOTO_FILE_NAME") == null || "".equalsIgnoreCase(nat_prs.getValue("FULL_PHOTO_FILE_NAME")))) { %>
		    		<div class="photo_rect_small" id="div_photo" style="float:right;margin:7px 0 7px 7px;">
		    			<img src="../NatPrsPhoto?id_nat_prs=<%=oper.getValue("ID_NAT_PRS") %>&noCache=<%=Bean.getNoCasheValue() %>" class="photo_image_small">
		    		</div>
		    		<% } %>
		    		<span id="succes_description"><%=resultMessage %></span><br>
		    		<% 
		    			int signatureSendCount = 0; 
		    			if (!Bean.isEmpty(oper.getValue("SMS_SIGNATURE_SEND_COUNT"))) {
		    				signatureSendCount = Integer.parseInt(oper.getValue("SMS_SIGNATURE_SEND_COUNT"));
		    			}
		    			if (signatureSendCount < Bean.C_SIGNATURE_MAX_SEND_COUNT) {
		    		%>
		    		<span>Не получили СМС? <span class="go_to" onclick="ajaxpage('service/get_sms_code.jsp?id_telgr=<%= id_telgr%>&action=get_sms_code&back_type=invoice&back_div=div_action_big', 'div_sms_confirmation')" title="<%=Bean.buttonXML.getfieldTransl("button_questionnaire", false) %>">Отправить еще раз</span></span>
		    		<br>
		    		<% } else { %>
		    		<br>
		    		<% } %>
		    		<br>
		    		<span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("sms_password", false) %></span>&nbsp;&nbsp;&nbsp;<input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER.length() %>"><br><br>
		    		<div style="width:100%; text-align: center;">
		    		    <%=Bean.getSubmitButtonAjax("report/invoicepay.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
		    		</div>
					</div>
				</td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } else if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_activation_code", false) %></td></tr>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("card_activation_code", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER.length() %>"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("report/invoicepay.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
					</td>
				</tr>
				<% } else if (Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2" class="through_share_fee"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2" class="through_share_fee"><span id="error_description"><%=resultMessage %><% if (shareFeePermission) { %>&nbsp;<%=Bean.webposXML.getfieldTransl("title_can_share_fee", false) %><% } %></span></td></tr>
				<% if (shareFeePermission) { %>
					<tr>
						<td colspan="4" class="through_share_fee">
							<table border="0" class="payTypeTable">
								<tr>
									<%=Bean.getPayTypeImage("CASH", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_CASH", "report/invoicepay.jsp?pay_type=CASH&replenish_kind=SHARE_FEE", "div_action_big", "updateForm", "") %>
									<%=Bean.getPayTypeImage("BANK_CARD", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_BANK_CARD", "report/invoicepay.jsp?pay_type=BANK_CARD&replenish_kind=SHARE_FEE", "div_action_big", "updateForm", "") %>
								</tr>
							</table>
						</td>
					</tr>
				<% } %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" class="center">
				        <%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
					</td>
				</tr>
				<% } else if (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt) || 
							(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt) && "INVOICE".equalsIgnoreCase(pay_type))) { %>
				<% if ("INVOICE".equalsIgnoreCase(pay_type) &&
						!Bean.isEmpty(id_telgr)) { %>
				<tr>
					<td><%=Bean.webposXML.getfieldTransl("pay_description", false) %></td><td><textarea name="pay_description" cols="27" rows="3" class="inputfield"><%=Bean.getInvoicePaymendDescription(oper) %></textarea></td>
				</tr>
				<% } %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% if (!Bean.isEmpty(resultMessage)) { %>
				<tr><td colspan="2"><span id="succes_description"><%=resultMessage %></span></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } %>
				<tr>
				<tr><td colspan="4" class="center">
					<%=Bean.getSubmitButtonAjax("report/invoicepay.jsp", "pay", "updateForm", "div_action_big", "", "pay_button", true) %> 
					<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
				</td></tr>
				</tr>
				<% } else { %>
				<tr><td colspan="2">&nbsp;</td></tr>
	 				<% if (isErrorResult) { %>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<tr>
							<td colspan="2" class="center">
				        <%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
							</td>
						</tr>
					<% } else { %>
						<% if (!Bean.isEmpty(resultMessage)) { %>
							<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
						<% } %>
						<tr>
							<td colspan="2" class="center">
						        <%=Bean.getSubmitButtonAjax("report/invoicepay.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        		<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "button_back", "updateFormBack", "div_action_big") %>
							</td>
						</tr>
					<% } %>
				<% } %>
						<tr><td colspan="2" class="left">
							<div id=div_hints>
								<% if ("ROBOKASSA".equalsIgnoreCase(pay_type) && !Bean.isEmpty(marginRobokassaText)) { %>
								<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %></b><br>
								<%=marginRobokassaText %><br>
								</i>
								<% } %> 
							</div>
						</td></tr>
			</table>
		</form>
			<% if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
			<form class="hiddenForm" name="updateFormGetSMS" id="updateFormGetSMS" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
    			<input type="hidden" name="action" value="get_sms_code">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="back_type" value="pay">
			</form>
			<% } %>
			<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
			<form class="hiddenForm" name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
    			<input type="hidden" name="type" value="term">
    			<input type="hidden" name="action" value="oper_confirm">
    			<input type="hidden" name="confirm_type" value="REMIND_PIN">
    			<input type="hidden" name="process" value="no">
				<input type="hidden" name="id_term" value="<%=id_term %>">
    			<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
    			<input type="hidden" name="pay_type" value="<%=pay_type %>">
    			<input type="hidden" name="pay_value" value="<%=pay_value %>">
			</form>
			<% } %>
		<form class="hiddenForm" name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
			<input type="hidden" name="id_telgr" value="<%=id_telgr_invoice %>">
			<input type="hidden" name="type" value="trans">
			<input type="hidden" name="process" value="no">
			<input type="hidden" name="action" value="pay">
		</form>

		<% } 
		} else { 
			%> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
