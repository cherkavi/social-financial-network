<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="webpos.wpNatPrsObject"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpTelegramObject"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_PAY";

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
cd_card1 = Bean.isEmpty(cd_card1)?"":cd_card1.replace(" ", "");

Bean.readWebPosMenuHTML();

Bean.loginTerm.getTermFeature();
String termCurrency = Bean.loginTerm.getValue("SNAME_TERM_CURRENCY");

String idClub = Bean.loginTerm.getValue("ID_CLUB");
String idDealer = Bean.loginTerm.getValue("ID_DEALER");

String
	pay_for_goods			= Bean.getDecodeParamPrepare(parameters.get("pay_for_goods")),
	pay_value				= Bean.getDecodeParamPrepare(parameters.get("pay_value")),
	pay_currency			= Bean.getDecodeParamPrepare(parameters.get("pay_currency")),
	pay_type				= Bean.getDecodeParamPrepare(parameters.get("pay_type")),
	can_use_share_account	= Bean.getDecodeParamPrepare(parameters.get("can_use_share_account")),
	share_fee				= Bean.getDecodeParamPrepare(parameters.get("share_fee")),
	share_fee_margin		= Bean.getDecodeParamPrepare(parameters.get("share_fee_margin")),
	entered_sum				= Bean.getDecodeParamPrepare(parameters.get("entered_sum")),
	sum_change				= Bean.getDecodeParamPrepare(parameters.get("sum_change")),
	change_to_share_account	= Bean.getDecodeParamPrepare(parameters.get("change_to_share_account")),
	bank_trn				= Bean.getDecodeParamPrepare(parameters.get("bank_trn")),
	sum_point				= Bean.getDecodeParamPrepare(parameters.get("sum_point")),
	percent_point			= Bean.getDecodeParamPrepare(parameters.get("percent_point")),
	calc_point				= Bean.getDecodeParamPrepare(parameters.get("calc_point")),
	membership_fee			= Bean.getDecodeParamPrepare(parameters.get("membership_fee")),
	membership_fee_margin	= Bean.getDecodeParamPrepare(parameters.get("membership_fee_margin")),
	replenish_kind			= Bean.getDecodeParamPrepare(parameters.get("replenish_kind")),
	promo_code				= Bean.getDecodeParamPrepare(parameters.get("promo_code"));

String snamePointCurrency = Bean.webposXML.getfieldTransl("point_currency_name", false);

boolean extLoyality = false;
if ("1".equalsIgnoreCase(Bean.loginTerm.getValue("EXT_LOYL","LOYALITY"))) { 
	extLoyality = true;
}



%>


<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessageShort(pageFormName, Bean.loginTerm) %>
	<% type = "error_term"; %>
<% } %>
<%
if (type.equalsIgnoreCase("error_term")) {
} else if (type.equalsIgnoreCase("term")) {
	
	if (process.equalsIgnoreCase("yes")) {
		String resultInt 					= Bean.C_SUCCESS_RESULT;
 		String resultMessage 				= "";
 		String id_telgr						= Bean.getDecodeParam(parameters.get("id_telgr"));
 		
 		String sum_share_fee_need			= "";
 		String change_margin 				= "";
 		String total_margin 				= "";
 		String calc_point_total				= "";
 		String calc_point_shareholder		= "";
 		String sum_put_share_account    	= "";
 		String sum_get_from_share_account 	= "";
		String phone_mobile	 				= "";
		String can_send_pin_in_sms			= "";
		

		boolean shareFeePermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE");
		
		if (action.equalsIgnoreCase("calc")) {
			boolean hasPermission = false;
			if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) {
				if ("CASH".equalsIgnoreCase(pay_type)) {
					if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_CASH")) {
						hasPermission 	= true;
					} else {
						resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_cash_forbidden", false);
					}
				} else if ("BANK_CARD".equalsIgnoreCase(pay_type)) { 
					if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_BANK_CARD")) {
						hasPermission 	= true;
					} else {
						resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_card_forbidden", false);
					}
				} else if ("INVOICE".equalsIgnoreCase(pay_type)) { 
					if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_REPLANISH_SHARE_FEE_MAKE_INVOICE")) {
						hasPermission 	= true;
					} else {
						resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_invoice_forbidden", false);
					}
				}
			} else {
				if ("CASH".equalsIgnoreCase(pay_type)) { 
					if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_PAY_CASH")) {
						hasPermission 	= true;
					} else {
						resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_cash_forbidden", false);
					}
				} else if ("BANK_CARD".equalsIgnoreCase(pay_type)) { 
					if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_PAY_BANK_CARD")) {
						hasPermission 	= true;
					} else {
						resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_card_forbidden", false);
					}
				} else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) { 
					if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_PAY_POINTS")) {
						hasPermission 	= true;
					} else {
						resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_points_forbidden", false);
					}
				} else if ("INVOICE".equalsIgnoreCase(pay_type)) { 
					if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_MAKE_INVOICE")) {
						hasPermission 	= true;
					} else {
						resultInt 		= Bean.C_FUNCTION_ACCESS_DENIED;
						resultMessage 	= Bean.webposXML.getfieldTransl("goods_pay_invoice_forbidden", false);
					}
				}
			}
			
			if (hasPermission) {
				ArrayList<String> pParam = new ArrayList<String>();	
				
				if (Bean.isEmpty(pay_value)) {
					pay_value = pay_for_goods;
				}
				
				pParam.add(id_term);
				pParam.add(cd_card1);
				pParam.add(promo_code);
				pParam.add(replenish_kind);
				pParam.add(pay_type);
				pParam.add(bank_trn);
				//pParam.add(pay_currency);
				pParam.add(pay_value);
				pParam.add(share_fee);
				pParam.add(share_fee_margin);
				pParam.add(pay_for_goods);
				pParam.add(percent_point);
				pParam.add(sum_point);
				pParam.add(entered_sum);
				pParam.add(sum_change);
				pParam.add(membership_fee);
				pParam.add(membership_fee_margin);				
				pParam.add(change_to_share_account);
				pParam.add(can_use_share_account);
				
				String[] results = new String[13];
				
				results 					= Bean.executeFunction("PACK$WEBPOS_UI.payment_for_goods", pParam, results.length);
				resultInt 					= results[0];
				id_telgr		 			= results[1];
				sum_share_fee_need			= results[2];
				share_fee_margin			= results[3];
				change_margin			    = results[4];
				total_margin	 			= results[5];
				calc_point_total			= results[6];
				calc_point_shareholder		= results[7];
				sum_put_share_account		= results[8];
				sum_get_from_share_account	= results[9];
				phone_mobile	 			= results[10];
				can_send_pin_in_sms			= results[11];
		 		resultMessage 				= results[12];
			}
			boolean needMembershipFee = false;
			if (!Bean.isEmpty(membership_fee)) {
				needMembershipFee = true;
			}
		} else if (action.equalsIgnoreCase("oper_confirm")) {
			String
				confirm_type		= Bean.getDecodeParam(parameters.get("confirm_type")),
				confirm_code		= Bean.getDecodeParam(parameters.get("confirm_code"));
				

			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_telgr);
			pParam.add(confirm_type);
			pParam.add(confirm_code);
			pParam.add("");
			
			String[] results = new String[4];
			
			results 				= Bean.executeFunction("PACK$WEBPOS_UI.oper_confirm", pParam, results.length);
			resultInt 				= results[0];
			phone_mobile	 		= results[1];
			can_send_pin_in_sms  	= results[2];
	 		resultMessage 			= results[3];
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
	 	
	 	String marginText = "";
	 	if (Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultInt)) {
	 		marginText = marginText + "<br>" + Bean.getMarginDescription(idClub, idDealer, "REC_SHARE_FEE");
	 	}

	    	
		if (action.equalsIgnoreCase("calc") || action.equalsIgnoreCase("oper_confirm")) { %>

		<% if (action.equalsIgnoreCase("oper_confirm") && Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { 
			wpChequeObject cheque = new wpChequeObject(id_telgr, Bean.getChequeSaveFormat(), Bean.loginTerm);
		%>
			<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("payment_for_goods", false) %>: <%=Bean.webposXML.getfieldTransl("operation_confirm", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
					<table class="table_cheque"><tbody>
					<tr><td class="centerb">
						<%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></td><td>
						<%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateForm", "div_main") %><br><br>
					</td></tr>
					<%=cheque.getChequeHTML(true) %>
					</tbody></table>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
    			<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
 			</form>
		<% 
		} else { 

				wpTelegramObject oper = null;
				if (!Bean.isEmpty(id_telgr)) {
					oper = new wpTelegramObject (id_telgr);
				}
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
			<h1 class="error"><%=Bean.webposXML.getfieldTransl("payment_for_goods", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
			<% } else { %>
			<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("payment_for_goods", false) %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
			<% } %>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
				<% if ("INVOICE".equalsIgnoreCase(pay_type)) { %>
    			<input type="hidden" name="type" value="trans">
    			<input type="hidden" name="process" value="yes">
				<% } else { %>
    			<input type="hidden" name="type" value="term">
    			<input type="hidden" name="process" value="yes">
				<% } %>
				<% if (Bean.C_ISNT_ENOUGH_POINTS.equalsIgnoreCase(resultInt)) { %>
		    	<input type="hidden" name="action" value="calc">
		    	<input type="hidden" name="pay_for_goods" value="<%=pay_for_goods %>">
		    	<input type="hidden" name="pay_value" value="<%=pay_value %>">
		    	<input type="hidden" name="bank_trn" value="<%=bank_trn %>">
		    	<input type="hidden" name="replenish_kind" value="GET_FROM_SHARE_ACCOUNT">
		    	<input type="hidden" name="share_fee" value="<%=sum_get_from_share_account %>">
				<% } else if (Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultInt)) { %>
		    	<input type="hidden" name="action" value="calc">
		    	<input type="hidden" name="pay_for_goods" value="<%=pay_for_goods %>">
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
    			<input type="hidden" name="pay_type" value="<%=pay_type %>">
    			<input type="hidden" name="pay_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
				<input type="hidden" name="calc_point" id="calc_point" value="<%=calc_point %>">
				<input type="hidden" name="sum_point" id="sum_point" value="<%=sum_point %>">
				<input type="hidden" name="percent_point" id="percent_point" value="<%=percent_point %>">
			    <input type="hidden" name="membership_fee" id="membership_fee" value="<%=membership_fee %>">
			    <input type="hidden" name="membership_fee_margin" id="membership_fee_margin" value="<%=membership_fee_margin %>">
				<input type="hidden" name="share_fee_margin" id="share_fee_margin" value="<%=share_fee_margin %>">
				<table class="action_table">
				<tbody>
				<% if (!Bean.isEmpty(id_telgr)) { %>
				<!-- <tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><input type="text" name="doc_number" id="doc_number" size="20" value="<%=oper.getValue("NC_TERM") %>" readonly class="inputfield_finish"></td></tr> -->
				<% } %>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly class="inputfield_finish_blue"></td></tr>
	  			<tr><td><b><%=Bean.webposXML.getfieldTransl("goods_pay_sum", false) %></b></td><td><input type="text" name="pay_for_goods_txt" id="pay_for_goods_txt" size="20" value="<%=pay_for_goods %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td></tr>
				
				<% if (extLoyality) { %>
					<tr><td><%=Bean.webposXML.getfieldTransl("point_calc_way", false) %></td><td ><input type="text" name="sum_point_txt" id="sum_point_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("point_calc_way_term", false) %>" readonly class="inputfield_finish_blue"></td></tr>
					<% if ("Y".equalsIgnoreCase(calc_point)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("percent_point", false) %></td><td><input type="text" name="percent_point_txt" id="percent_point_txt" size="20" value="<%=percent_point %> %" readonly class="inputfield_finish"></td></tr>
					<% } %>
					<tr><td><%=Bean.webposXML.getfieldTransl("cheque_add_point_all", false) %></td><td><input type="text" name="sum_point_txt" id="sum_point_txt" size="20" value="<%=sum_point %> <%=Bean.getWebPOSPointCurrencyName(sum_point) %>" readonly class="inputfield_finish_blue"></td></tr>
				<% } %>
				<% if (!Bean.isEmpty(calc_point_shareholder)) { %>
	  			<tr><td><b><%=Bean.webposXML.getfieldTransl("cheque_add_point", false) %></b></td><td><input type="text" name="sum_put_point_frmt" id="sum_put_point_frmt" size="20" value="<%=calc_point_shareholder %> <%=snamePointCurrency %>" readonly class="inputfield_finish_red"></td></tr>
				<% } %>
	
				<% if (!Bean.isEmpty(membership_fee)) { %>
		  			<tr><td class="another_fee"><%=Bean.webposXML.getfieldTransl("membership_fee_sum", false) %></td><td class="another_fee"><input type="text" name="membership_fee_txt" id="membership_fee_txt" size="20" value="<%=membership_fee %> <%=termCurrency %>" readonly class="inputfield_finish_green"></td></tr>
					<% if (!Bean.isEmpty(membership_fee_margin)) { %>
		  			<tr><td class="another_fee"><i><%=Bean.webposXML.getfieldTransl("membership_fee_dealer_margin", false) %></i></td><td class="another_fee"><input type="text" name="membership_fee_margin_txt" id="membership_fee_margin_txt" size="20" value="<%=membership_fee_margin %> <%=termCurrency %>" readonly class="inputfield_finish_gray"></td></tr>
					<% } %>
		  			<tr><td class="top_line_gray line_dashed"><b><%=Bean.webposXML.getfieldTransl("pay_total", false) %></b></td><td class="top_line_gray line_dashed"><input type="text" name="pay_value_txt" id="pay_value_txt" size="20" value="<%=pay_value %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td></tr>
				<% } else { %>
		  			<tr><td class="top_line_gray line_dashed"><b><%=Bean.webposXML.getfieldTransl("pay_total", false) %></b></td><td class="top_line_gray line_dashed"><input type="text" name="pay_value_txt" id="pay_value_txt" size="20" value="<%=pay_for_goods %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td></tr>
				<% } %>
				<% if ("SHARE_FEE".equalsIgnoreCase(replenish_kind)) { %>
					<tr><td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td><td><input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green"></td></tr>
					<tr><td colspan="2" class="through_share_fee" style="color:green"><%=Bean.webposXML.getfieldTransl("title_payment_through_share_fee", false) %></td></tr>
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
						<tr><td class="through_share_fee"><i><%=Bean.webposXML.getfieldTransl("share_fee_change_margin", false) %></i></td><td colspan="2" class="through_share_fee"><input type="text" name="change_margin_txt" id="change_margin_txt" size="20" value="<%=change_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
					<% } %>
		  			<tr>
						<% if ("Y".equalsIgnoreCase(change_to_share_account) &&
								!Bean.isEmpty(sum_put_share_account)) { %>
							<td class="through_share_fee"><b><%=Bean.webposXML.getfieldTransl("add_share_account_from_change", false) %></b></td><td class="through_share_fee"><input type="text" name="sum_put_share_account" id="sum_put_share_account" size="20" value="<%=sum_put_share_account %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td><td>
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
						<tr><td class="pay_cash_change"><i><%=Bean.webposXML.getfieldTransl("share_fee_change_margin", false) %></i></td><td colspan="2" class="pay_cash_change"><input type="text" name="change_margin_txt" id="change_margin_txt" size="20" value="<%=change_margin %> <%=termCurrency %>" class="inputfield_finish_gray" maxlength="15"></td></tr>
					<% } %>
		  			<tr>
						<% if ("Y".equalsIgnoreCase(change_to_share_account) &&
								!Bean.isEmpty(sum_put_share_account)) { %>
							<td class="pay_cash_change"><b><%=Bean.webposXML.getfieldTransl("add_share_account_from_change", false) %></b></td><td class="pay_cash_change"><input type="text" name="sum_put_share_account" id="sum_put_share_account" size="20" value="<%=sum_put_share_account %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td><td>
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
							<% } else if ("INVOICE".equalsIgnoreCase(pay_type)) {  %>
							<input type="text" name="pay_type_txt" id="pay_type_txt" size="20" value="<%=Bean.webposXML.getfieldTransl("goods_pay_invoices", false) %>" readonly class="inputfield_finish_green">
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
				        <%=Bean.getSubmitButtonAjax("action/payupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
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
				        <%=Bean.getSubmitButtonAjax("action/payupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
					</td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% if ("Y".equalsIgnoreCase(can_send_pin_in_sms)) { %>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_pin_remind", false) %> <%=phone_mobile %>)</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/payupdate.jsp", "remind_pin", "updateForm2", "div_action_big") %>
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
					        <%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
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
		    		<span>Не получили СМС? <span class="go_to" onclick="ajaxpage('service/get_sms_code.jsp?id_telgr=<%= id_telgr%>&action=get_sms_code&back_type=pay', 'div_sms_confirmation')" title="<%=Bean.buttonXML.getfieldTransl("button_questionnaire", false) %>">Отправить еще раз</span></span>
		    		<br>
		    		<% } else { %>
		    		<br>
		    		<% } %>
		    		<br>
		    		<span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("sms_password", false) %></span>&nbsp;&nbsp;&nbsp;<input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER.length() %>"><br><br>
		    		<div style="width:100%; text-align: center;">
		    		    <%=Bean.getSubmitButtonAjax("action/payupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
		    		    <%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
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
				        <%=Bean.getSubmitButtonAjax("action/payupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
					</td>
				</tr>
				<% } else if (Bean.C_ENOUGH_MEANS.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2" class="through_share_fee"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2" class="through_share_fee"><span id="error_description"><%=resultMessage %></span><% if (shareFeePermission) { %><br><b><%=Bean.webposXML.getfieldTransl("title_can_share_fee", false) %><% } %></b></td></tr>
				<% if (shareFeePermission) { %>
					<tr>
						<td colspan="4" class="through_share_fee">	
							<table border="0" class="payTypeTable">
								<tr>
									<%=Bean.getPayTypeImage("CASH", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_CASH", "action/pay.jsp?replenish_pay_type=CASH&replenish_kind=SHARE_FEE", "div_main", "updateForm", "") %>
									<%=Bean.getPayTypeImage("BANK_CARD", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_PAY_BANK_CARD", "action/pay.jsp?replenish_pay_type=BANK_CARD&replenish_kind=SHARE_FEE", "div_main", "updateForm", "") %>
									<%=Bean.getPayTypeImage("INVOICE", "WEBPOS_SERVICE_REPLANISH_SHARE_FEE_MAKE_INVOICE", "action/pay.jsp?replenish_pay_type=INVOICE&replenish_kind=SHARE_FEE", "div_main", "updateForm", "") %>
								</tr>
							</table>
						</td>
					</tr>
				<% } %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" class="center">
				        <%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
					</td>
				</tr>
				<% } else if (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt) || 
							(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt) && "INVOICE".equalsIgnoreCase(pay_type))) { %>
				<% if ("INVOICE".equalsIgnoreCase(pay_type)) { %>
				<% if (!Bean.isEmpty(id_telgr)) { %>
				<tr>
					<td><%=Bean.webposXML.getfieldTransl("pay_description", false) %></td><td><textarea name="pay_description" cols="27" rows="3" class="inputfield"><%=Bean.getTelegramPaymendDescription(oper) %></textarea></td>
				</tr>
				<% } %>
				<% } %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% if (!Bean.isEmpty(resultMessage)) { %>
				<tr><td colspan="2"><span id="succes_description"><%=resultMessage %></span></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } %>
				<tr>
					<td colspan="2" align="center">
						<% if ("INVOICE".equalsIgnoreCase(pay_type)) { %>
				        <%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "confirm", "updateForm", "div_action_big", "") %>
						<% } else { %>
				        <%=Bean.getSubmitButtonAjax("action/payupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
						<% } %>
				        <%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
					</td>
				</tr>
				<% } else { %>
				<tr><td colspan="2">&nbsp;</td></tr>
	 				<% if (isErrorResult) { %>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<tr>
							<td colspan="2" class="center">
				        <%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
							</td>
						</tr>
					<% } else { %>
						<% if (!Bean.isEmpty(resultMessage)) { %>
							<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
						<% } %>
						<tr>
							<td colspan="2" class="center">
						        <%=Bean.getSubmitButtonAjax("action/payupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("action/pay.jsp", "button_back", "updateFormBack", "div_main") %>
							</td>
						</tr>
					<% } %>
				<% } %>
						<tr><td colspan="2" class="left">
							<div id=div_hints>
								<% if (!Bean.isEmpty(marginText)) { %>
								<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %></b>
									<%=marginText %><br>
								</i>
								<% } %> 
							</div>
						</td></tr>
				</tbody>
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
    			<input type="hidden" name="pay_for_goods" value="<%=pay_for_goods %>">
    			<input type="hidden" name="pay_value" value="<%=pay_value %>">
			</form>
			<% } %>
			<form class="hiddenForm" name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_term" value="<%=id_term %>">
    			<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
    			<input type="hidden" name="pay_type" value="<%=pay_type %>">
    			<input type="hidden" name="pay_for_goods" value="<%=pay_for_goods %>">
    			<input type="hidden" name="pay_value" value="<%=pay_value %>">
    			<input type="hidden" name="action" value="check">
    			<input type="hidden" name="pay_currency" value="<%=Bean.loginTerm.getValue("CD_TERM_CURRENCY") %>">
				<input type="hidden" name="calc_point" id="calc_point" value="<%=calc_point %>">
				<input type="hidden" name="sum_point" id="sum_point" value="<%=sum_point %>">
				<input type="hidden" name="percent_point" id="percent_point" value="<%=percent_point %>">
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
