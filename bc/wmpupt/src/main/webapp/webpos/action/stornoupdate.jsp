<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpTelegramObject"%>
<%@page import="webpos.wpNatPrsTargetPrgObject"%>
<%@page import="webpos.wpClubCardObject"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());
System.out.println("parameters="+parameters.toString());

String pageFormName = "WEBPOS_SERVICE_STORNO";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

type 	= Bean.isEmpty(type)?"":type;
action 	= Bean.isEmpty(action)?"":action;

String id_term	= Bean.getCurrentTerm();

Bean.loginTerm.getTermFeature();

Bean.readWebPosMenuHTML();

String
	storno_type				= Bean.getDecodeParam(parameters.get("storno_type")),
	storno_rrn				= Bean.getDecodeParam(parameters.get("storno_rrn")),
	storno_id				= Bean.getDecodeParam(parameters.get("id_telgr")),
	back_type				= Bean.getDecodeParam(parameters.get("back_type")),
	can_use_share_account	= Bean.getDecodeParam(parameters.get("can_use_share_account"));

back_type 	= Bean.isEmpty(back_type)?"":back_type;

String storno_type_name = "";
if ("CANCELLATION".equalsIgnoreCase(storno_type)) {
	storno_type_name = Bean.webposXML.getfieldTransl("storno_type_cancellation", false);
} else if ("RETURN".equalsIgnoreCase(storno_type)) {
	storno_type_name = Bean.webposXML.getfieldTransl("storno_type_return", false);
} else {
	storno_type 	= "CHECK";
	storno_type_name = Bean.webposXML.getfieldTransl("title_storno", false);
}

boolean disableStorno 	= false;
boolean disableReturn 	= false;
String resultInt 		= Bean.C_SUCCESS_RESULT;
String resultMessage  	= "";
boolean successResult	= false;

String 
	storno_confirm_id		= Bean.getDecodeParam(parameters.get("storno_confirm_id")),
	storno_return_value		= Bean.getDecodeParam(parameters.get("storno_return_value")),
	confirm_return			= Bean.getDecodeParam(parameters.get("confirm_return")),
	cancellation_disable 	= "Y",
	return_disable       	= "Y";

String cancel_oper_id       = "";
String phone_mobile         = "";
String can_send_pin_in_sms  = "";
%>


<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
	<% type = "error_term"; %>
<% } %>
<%
if (type.equalsIgnoreCase("error_term")) {
} else if (type.equalsIgnoreCase("storno")) {
	if (action.equalsIgnoreCase("check")) {
		
		ArrayList<String> pParam = new ArrayList<String>();
		
		pParam.add(id_term);
		pParam.add(storno_rrn);
		pParam.add(storno_type);
		pParam.add(storno_id);
		
		String[] results = new String[5];
		
		results 				= Bean.executeFunction("PACK$WEBPOS_UI.storno_check", pParam, results.length);
		resultInt 				= results[0];
		storno_id 		        = results[1];
		cancellation_disable    = results[2];
		return_disable          = results[3];
		resultMessage 			= results[4];
		
		cancellation_disable	= Bean.isEmpty(cancellation_disable)?"Y":cancellation_disable;
		return_disable			= Bean.isEmpty(return_disable)?"Y":return_disable;
		
		disableStorno 			= ("Y".equalsIgnoreCase(cancellation_disable))?true:false;
		disableReturn 			= ("Y".equalsIgnoreCase(return_disable))?true:false;
		
		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
			successResult = true;
		}
		
	} else if (action.equalsIgnoreCase("go")) {	
		ArrayList<String> pParam = new ArrayList<String>();
		
		pParam.add(id_term);
		pParam.add(storno_confirm_id);
		if ("RETURN".equalsIgnoreCase(storno_type)) {
			pParam.add(storno_return_value);
		}
		pParam.add(can_use_share_account);
		if ("RETURN".equalsIgnoreCase(storno_type)) {
			pParam.add(confirm_return);
		}
		
		String[] results = new String[5];
		
		if ("CANCELLATION".equalsIgnoreCase(storno_type)) {
			results 						= Bean.executeFunction("PACK$WEBPOS_UI.storno_cancellation", pParam, results.length);
		} else if ("RETURN".equalsIgnoreCase(storno_type)) {
			results 						= Bean.executeFunction("PACK$WEBPOS_UI.storno_return", pParam, results.length);
		}
		resultInt 						= results[0];
		cancel_oper_id           		= results[1];
		phone_mobile             		= results[2];
		can_send_pin_in_sms      		= results[3];
 		resultMessage 					= results[4];
		
		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
			successResult = true;
		}
		
	} else if (action.equalsIgnoreCase("oper_confirm")) {
		String
			id_telgr			= Bean.getDecodeParam(parameters.get("cancel_oper_id")),
			confirm_type		= Bean.getDecodeParam(parameters.get("confirm_type")),
			confirm_code		= Bean.getDecodeParam(parameters.get("confirm_code"));
		
		ArrayList<String> pParam = new ArrayList<String>();
		
		pParam.add(id_telgr);
		pParam.add(confirm_type);
		pParam.add(confirm_code);
		pParam.add("");
		
		String[] results = new String[4];
		
		results 					= Bean.executeFunction("PACK$WEBPOS_UI.oper_confirm", pParam, results.length);
		resultInt 					= results[0];
		phone_mobile	 			= results[1];
		can_send_pin_in_sms  		= results[2];
 		resultMessage 				= results[3];
		
		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
			successResult = true;
		}
	}
	
	if (action.equalsIgnoreCase("check")) { %>
		<script>
			function validateCheckStorno(){
				var returnValue = null;
				var formParam = new Array (
					new Array ('storno_rrn', 'varchar2', 1)
				);
				returnValue = validateForm(formParam, 'updateForm3');
				return returnValue;
			}
			function showCheckCardButton(card){
				//checkBox = document.getElementById(elem);
				element = document.getElementById('check_card');
				if (card.value == '' || card.value == null) {
					element.className = 'img_check_card_inactive';
				} else {
					element.className = 'img_check_card';
				}
			}
			card_mask2("cd_card1");
		</script>
		<% if (successResult) { %>
			<h1><%=Bean.webposXML.getfieldTransl("title_storno", false) %><%=Bean.getHelpButton("storno", "div_action_big") %></h1>
		<% } else { %>
			<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_storno", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %>&nbsp;<%=Bean.getHelpButton("storno", "div_action_big") %></h1>
		<% } %>
		<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="storno">
			<input type="hidden" name="action" value="prepare">
			<input type="hidden" name="process" value="no">
			<input type="hidden" name="id_term" value="<%=id_term %>">
			<input type="hidden" name="back_type" value="<%=back_type %>">
			<input type="hidden" name="storno_confirm_id" value="<%=storno_id %>">
		    <table class="action_table">
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("storno_rrn", false) %></td><td><input type="text" name="storno_rrn" id="storno_rrn" size="30" value="<%=storno_rrn %>" readonly="readonly" class="inputfield-ro"></td></tr>
				<tr>
					<td><%=Bean.webposXML.getfieldTransl("storno_type", true) %></td>
					<td>
						<table border="0">
							<tr>
								<td><%=Bean.getReturnCancelImageDisable("CANCEL", "WEBPOS_SERVICE_STORNO_CANCELLATION", disableStorno, "action/stornoupdate.jsp?storno_type=CANCELLATION", "div_action_big", "updateForm3", "validateCheckStorno") %></td>
								<td><%=Bean.getReturnCancelImageDisable("RETURN", "WEBPOS_SERVICE_STORNO_RETURN", disableReturn, "action/stornoupdate.jsp?storno_type=RETURN", "div_action_big", "updateForm3", "validateCheckStorno") %></td>
							</tr>
						</table>
					</td>
				</tr>
				<% if (!successResult) { %>
				<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<% } %>
             	<tr><td colspan="2"  align="center">
					<% if ("operations".equalsIgnoreCase(back_type) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION")) { %>
					    <%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateForm2", "div_main") %>
					<% } else { %>
					    <%=Bean.getSubmitButtonAjax("action/storno.jsp", "button_back", "updateForm1", "div_main") %>
					<% } %>
				</td></tr>
			</table>
		</form>
		<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="operation">
		     <input type="hidden" name="action" value="show">
		     <input type="hidden" name="process" value="yes">
		</form>
		<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="storno_rrn" value="<%=storno_rrn %>">
		</form>
		<%
	}
	if ((action.equalsIgnoreCase("prepare")) ||
			(action.equalsIgnoreCase("go") && !successResult)) {
				wpTelegramObject oper = new wpTelegramObject(storno_confirm_id);
				
				boolean operForbidden = true;
				String telgr_type = oper.getValue("CD_TELGR_TYPE");
				String telgr_type_name = "";
				
				if ("PAYMENT".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("payment_for_goods", false);
				} else if ("PUT_CARD".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_card_registration", false);
				} else if ("QUESTIONING".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_questioning", false);
				} else if ("TRANSFER_POINT".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_transfer", false);
				} else if ("CANCEL".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_cancel", false);
				} else if ("RETURN".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_return", false);
				} else if ("POINT_FEE".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_point_fee", false);
				} else if ("SHARE_FEE".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_share_fee", false);
				} else if ("MEMBERSHIP_FEE".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_membership_fee", false);
				} else if ("MTF".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_mtf", false);
				} else if ("ACTIVATION".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_activation", false);
				} else if ("PAYMENT_INVOICE".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_payment_invoice", false);
				} else if ("COUPON".equalsIgnoreCase(telgr_type)) {
					telgr_type_name = Bean.webposXML.getfieldTransl("title_coupon", false);
				} else {
					telgr_type_name = "UNKNOWN";
				}
				
				wpNatPrsTargetPrgObject target_prg = null;
				if ("REC_MTF".equalsIgnoreCase(telgr_type)) {
					target_prg = new wpNatPrsTargetPrgObject(oper.getValue("ID_TARGET_PRG"), oper.getValue("ID_NAT_PRS"));
				}
					%>

				
				<script>
					function validateCheckStorno(){
						var returnValue = null;
						var formAll = new Array (
						);
						var formParam = new Array (
							new Array ('storno_rrn', 'varchar2', 1)
						);
						var formReturnValue = new Array (
							new Array ('storno_return_value', 'oper_sum', 1)
						);
						formAll = formParam;
						<% if ("RETURN".equalsIgnoreCase(storno_type)) { %>
							formAll = formAll.concat(formReturnValue);
						<% } %>
						returnValue = validateForm(formAll, 'updateForm');
						return returnValue;
					}
				</script>
				<h1><%=storno_type_name %><%=Bean.getHelpButton("storno", "div_action_big") %></h1>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="storno">
			        <input type="hidden" name="action" value="go">
			        <input type="hidden" name="process" value="yes">
			        <input type="hidden" name="id_term" value="<%=id_term %>">
			        <input type="hidden" name="storno_confirm_id" value="<%=storno_confirm_id %>">
					<input type="hidden" name="back_type" value="<%=back_type %>">
					<input type="hidden" name="storno_type" value="<%=storno_type %>">
					<table class="action_table"><tbody>
			  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("storno_rrn", false) %></td><td><input type="text" name="storno_rrn" id="storno_rrn" size="30" value="<%=oper.getValue("RRN") %>" readonly class="inputfield_finish"></td></tr>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("title_oper_type", false) %></td><td><input type="text" name="oper_type" id="oper_type" size="30" value="<%=telgr_type_name %>" readonly class="inputfield_finish_blue"></td></tr>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("id_term", false) %></td><td><input type="text" name="id_term_oper" id="id_term_oper" size="30" value="<%=oper.getValue("ID_TERM") %>" readonly class="inputfield_finish"></td></tr>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("cheque_cashier", false) %></td><td><input type="text" name="cashier" id="cashier" size="30" value="<%=oper.getValue("CASHIER_NAME") %>" readonly class="inputfield_finish"></td></tr>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_operation_date", false) %></td><td><input type="text" name="operation_date" id="operation_date" size="30" value="<%=oper.getValue("DATE_TELGR_DHMF") %>" readonly class="inputfield_finish"></td></tr>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><input type="text" name="doc_number" id="doc_number" size="30" value="<%=oper.getValue("NC_TERM") %>" readonly class="inputfield_finish"></td></tr>
			  			
						<% if ("PAYMENT".equalsIgnoreCase(telgr_type)) { %>
							<% operForbidden = false; %>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("card", false) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="30" value="<%=oper.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish_blue"></td></tr>
						<tr><td><%=Bean.webposXML.getfieldTransl("operation_sum", false) %></td><td><input type="text" name="pay_value" id="pay_value" size="20" value="<%=oper.getValue("OPR_SUM_FRMT") %> <%=oper.getValue("SNAME_CURRENCY") %>" readonly class="inputfield_finish_red"></td></tr>
						
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
							<td>
								<% if ("CASH".equalsIgnoreCase(oper.getValue("PAY_TYPE"))) { %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
								<% } else if ("BANK_CARD".equalsIgnoreCase(oper.getValue("PAY_TYPE"))) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
							</td>
						</tr>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("bank_trn", false) %></td>
							<td><input type="text" name="bank_trn" id="bank_trn" size="30" value="<%=oper.getValue("bank_trn") %>" readonly class="inputfield_finish_green">

								<% } else if ("SMPU_CARD".equalsIgnoreCase(oper.getValue("PAY_TYPE"))) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
								<% } %>
							</td>
						</tr>

						<% if ("C_RETURNED_PART_TRANS".equalsIgnoreCase(oper.getValue("FCD_TRANS_STATE"))) { %>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("storno_already_returned_value", false) %></td>
							<td>
								<input type="text" name="storno_already_returned_value" id="storno_already_returned_value" size="20" value="<%=oper.getValue("STORNED_INPUT_SUM_FRMT") %>" readonly class="inputfield-ro"><input type="text" name="sname_term_currency2" size="5" value="<%=oper.getValue("SNAME_CURRENCY") %>" readonly class="inputfield-ro">
							</td>
						</tr>
						<% } %>

						<% if ("RETURN".equalsIgnoreCase(storno_type)) { %>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("storno_return_value", true) %></td>
							<td>
								<input type="text" name="storno_return_value" id="storno_return_value" size="20" value="<%=oper.getValue("REMAINED_OPR_SUM_FRMT") %>" class="inputfield"><input type="text" name="sname_term_currency2" size="5" value="<%=oper.getValue("SNAME_CURRENCY") %>" readonly class="inputfield-ro">
							</td>
						</tr>
						<% } %>
						<% } else if ("SHARE_FEE".equalsIgnoreCase(telgr_type) ||
								"MEMBERSHIP_FEE".equalsIgnoreCase(telgr_type) ||
								"POINT_FEE".equalsIgnoreCase(telgr_type) ||
								"MTF".equalsIgnoreCase(telgr_type)) { %>
							<%
								operForbidden = false;
							%>
						<tr><td><%=Bean.webposXML.getfieldTransl("fee_kind", false) %></td><td colspan="2"><input type="text" name="fee_kind" id="fee_kind" size="30" value="<%=telgr_type_name %>" readonly="readonly" class="inputfield_finish_green"></td></tr>
						<tr><td ><%=Bean.webposXML.getfieldTransl("card", false) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="30" value="<%=oper.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish_blue"></td></tr>
						
						<% if ("MTF".equalsIgnoreCase(telgr_type)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("target_prg", false) %></td><td colspan="2"><input type="text" name="name_target_prg" id="name_target_prg" size="30" value="<%=target_prg.getValue("NAME_TARGET_PRG") %>" readonly="readonly" class="inputfield_finish_green"></td></tr>
							<% if (!"IRREGULAR".equalsIgnoreCase(target_prg.getValue("CD_TARGET_PRG_PAY_PERIOD"))) { %>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("target_prg_pay_frequency", false) %></td>
								<td colspan="2">
									<input type="text" name="pay_period_frmt" id="pay_period_frmt" size="30" value="<%=target_prg.getValue("PAY_AMOUNT_FULL_FRMT") %>" readonly="readonly" class="inputfield_finish_blue">
									<input type="hidden" name="pay_period" id="pay_period" value="<%=target_prg.getValue("PAY_AMOUNT_FRMT") %>">
								</td>
							</tr>
							<% } else { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("target_prg_pay_period", false) %></td><td colspan="2"><input type="text" name="target_prg_pay_period" id="target_prg_pay_period" size="30" value="<%=target_prg.getValue("NAME_TARGET_PRG_PAY_PERIOD") %>" readonly="readonly" class="inputfield_finish_blue"></td></tr>
							<% } %>
						<% } %>
						<% if ("POINT_FEE".equalsIgnoreCase(telgr_type)) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("point_fee_sum", false) %></td><td><input type="text" name="replenish_value" id="replenish_value" size="20" value="<%=oper.getValue("OPR_SUM_FRMT") %> <%=Bean.webposXML.getfieldTransl("point_currency_name", false) %>" readonly="readonly" class="inputfield_finish_red"></td></tr>
						<% } else { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("fee_sum", false) %></td><td><input type="text" name="replenish_value" id="replenish_value" size="20" value="<%=oper.getValue("OPR_SUM_FRMT") %> <%=oper.getValue("SNAME_CURRENCY") %>" readonly="readonly" class="inputfield_finish_red"></td></tr>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
								<td>
									<% if ("CASH".equalsIgnoreCase(oper.getValue("PAY_TYPE"))) { %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
									<% } else if ("BANK_CARD".equalsIgnoreCase(oper.getValue("PAY_TYPE"))) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("bank_trn", false) %></td>
								<td><input type="text" name="bank_trn" id="bank_trn" size="30" value="<%=oper.getValue("bank_trn") %>" readonly class="inputfield_finish_green">
									<% } else if ("SMPU_CARD".equalsIgnoreCase(oper.getValue("PAY_TYPE"))) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
									<% } %>
								</td>
							</tr>
							<% if ("MTF".equalsIgnoreCase(telgr_type)) { %>
	 							<% if (!(oper.getValue("PAYMENT_DESCRIPTION") == null || "".equalsIgnoreCase(oper.getValue("PAYMENT_DESCRIPTION")))) { %>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("pay_description", false) %></td><td><span class="inputfield_finish_green" id="pay_description"><%=oper.getValue("PAYMENT_DESCRIPTION") %></span></td>
							</tr>
							<% } %>
						<% } %>
						<% } %>
						<% } else if ("TRANSFER_POINT".equalsIgnoreCase(telgr_type)) { %>
							<% operForbidden = false; %>
						<tr><td><%=Bean.webposXML.getfieldTransl("transfer_from_card", false) %></td><td><input type="text" name="from_card" id="from_card" size="30" value="<%=oper.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish_blue"></td></tr>
						<tr><td><%=Bean.webposXML.getfieldTransl("transfer_amount", false) %></td><td><input type="text" name="transfer_value" id="transfer_value" size="20" value="<%=oper.getValue("OPR_SUM_FRMT") %> <%=oper.getValue("SNAME_CURRENCY") %>" readonly class="inputfield_finish_red"></td></tr>
						<tr><td><%=Bean.webposXML.getfieldTransl("transfer_to_card", false) %></td><td><input type="text" name="to_card" id="to_card" size="30" value="<%=oper.getValue("TO_CD_CARD1_HIDE") %>" readonly class="inputfield_finish_green"></td></tr>
						
						<% } else if ("PUT_CARD".equalsIgnoreCase(telgr_type)) { %>
							<% operForbidden = false; %>
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="30" value="<%=oper.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish_blue"></td></tr>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("club_date_beg", false) %></td><td><input type="text" name="club_date_beg" id="club_date_beg" size="10" value="<%=oper.getValue("CLUB_DATE_BEG_DF") %>" readonly class="inputfield_finish"></td></tr>
						<tr><td><%=Bean.webposXML.getfieldTransl("new_client_package", false) %></td><td><input type="text" name="new_client_package" id="new_client_package" size="30" value="<%=Bean.getWEBPosCardPackagesName(oper.getValue("ID_JUR_PRS_CARD_PACK")) %>" readonly class="inputfield_finish_red"></td></tr>
						
							<% if ("CASH".equalsIgnoreCase(oper.getValue("PAY_TYPE"))) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td><td><input type="text" name="pay_type" id="pay_type" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green"></td></tr>
							<% } else if ("BANK_CARD".equalsIgnoreCase(oper.getValue("PAY_TYPE"))) { %>
							<tr><td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td><td><input type="text" name="pay_type" id="pay_type" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green"></td></tr>
							<tr><td><%=Bean.webposXML.getfieldTransl("bank_trn", false) %></td><td><input type="text" name="bank_trn" id="bank_trn" size="30" value="<%=oper.getValue("BANK_TRN") %>" readonly class="inputfield_finish_green"></td></tr>
							<% } %>
				
						<% } else if ("ACTIVATION".equalsIgnoreCase(telgr_type)) { %>
						<tr><td colspan="2" class="forbidden"><%=Bean.webposXML.getfieldTransl("title_cancelation_operation_forbidden", false) %></td></tr>
						<% } else if ("COUPON".equalsIgnoreCase(telgr_type)) { %>
						<tr><td colspan="2" class="forbidden"><%=Bean.webposXML.getfieldTransl("title_cancelation_operation_forbidden", false) %></td></tr>
						<% } else { %>
						<tr><td colspan="2" class="forbidden"><%=Bean.webposXML.getfieldTransl("title_unknown_operation_type", false) %></td></tr>
						<% } %>
						
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr>
							<td colspan="2" align="center">
								<% if (!operForbidden) { %>
									<% if ("RETURN".equalsIgnoreCase(storno_type)) { %>
							       		<%=Bean.getSubmitButtonAjax("action/stornoupdate.jsp", "return", "updateForm", "div_action_big", "validateCheckStorno") %>
									<% } else { %>
							        	<%=Bean.getSubmitButtonAjax("action/stornoupdate.jsp", "cancel", "updateForm", "div_action_big", "validateCheckStorno") %>
									<% } %>
								<% } %>
								<% if ("operations".equalsIgnoreCase(back_type) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION")) { %>
						        	<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateForm2", "div_main") %>
								<% } else { %>
						        	<%=Bean.getSubmitButtonAjax("action/storno.jsp", "button_back", "updateForm", "div_main") %>
								<% } %>
							</td>
						</tr>

					
					<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_pin", false) %></td></tr>
					<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_pin"><%=Bean.webposXML.getfieldTransl("title_pin", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_PIN_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_PIN_PLACEHOLDER.length() %>"></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td colspan="2" align="center">
					        <%=Bean.getSubmitButtonAjax("action/stornoupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
						<% if ("operations".equalsIgnoreCase(back_type) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION")) { %>
							<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateForm2", "div_main") %>
						<% } else { %>
							<%=Bean.getSubmitButtonAjax("action/storno.jsp", "button_back", "updateForm", "div_main") %>
						<% } %>
						</td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<% if ("Y".equalsIgnoreCase(can_send_pin_in_sms)) { %>
					<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_pin_remind", false) %> <%=phone_mobile %>)</td></tr>
					<tr>
						<td colspan="2" align="center">
					        <%=Bean.getSubmitButtonAjax("action/stornoupdate.jsp", "remind_pin", "updateForm3", "div_action_big") %>
						</td>
					</tr>
					<% } %>
					<% } else if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
					<tr><td colspan="2">&nbsp;</td></tr>
					<% if (!Bean.isEmpty(resultMessage)) { %>
					<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<% } %>
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("sms_password", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER.length() %>"></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td colspan="2" align="center">
					        <%=Bean.getSubmitButtonAjax("action/stornoupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
						<% if ("operations".equalsIgnoreCase(back_type) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION")) { %>
							<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateForm2", "div_main") %>
						<% } else { %>
							<%=Bean.getSubmitButtonAjax("action/storno.jsp", "button_back", "updateForm", "div_main") %>
						<% } %>
						</td>
					</tr>
					<% } else if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) { %>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_activation_code", false) %></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("card_activation_code", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER.length() %>"></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td colspan="2" align="center">
					        <%=Bean.getSubmitButtonAjax("action/stornoupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
						<% if ("operations".equalsIgnoreCase(back_type) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION")) { %>
							<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateForm2", "div_main") %>
						<% } else { %>
							<%=Bean.getSubmitButtonAjax("action/storno.jsp", "button_back", "updateForm", "div_main") %>
						<% } %>
						</td>
					</tr>
					<% } else if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {%>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
					<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
					
					<% if (Bean.C_ISNT_ENOUGH_POINTS.equalsIgnoreCase(resultInt)) { %>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td><span class="can_use_share_account"><%= Bean.webposXML.getfieldTransl("can_use_share_account", false) %></span></td><td><select name="can_use_share_account" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", can_use_share_account, false) %></select></td>
					</tr>
					<% } else if (Bean.C_CONFIRM_RETURN.equalsIgnoreCase(resultInt)) { %>
					<tr>
						<td><span class="confirm_return"><%= Bean.webposXML.getfieldTransl("confirm_return", false) %></span></td><td><select name="confirm_return" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", confirm_return, false) %></select></td>
					</tr>
					<% } %>

					<tr><td colspan="2">&nbsp;</td></tr>
					<% if (!operForbidden) { %>
						<tr>
							<td colspan="2" align="center">
						        <%=Bean.getSubmitButtonAjax("action/stornoupdate.jsp", "storno", "updateForm", "div_action_big", "validateCheckStorno") %>
								<% if ("operations".equalsIgnoreCase(back_type) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION")) { %>
						        <%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateForm2", "div_main") %>
								<% } else { %>
						        <%=Bean.getSubmitButtonAjax("action/storno.jsp", "button_back", "updateForm", "div_main") %>
								<% } %>
							</td>
						</tr>
					<% } %>
					<% } %>
					</tbody></table>
				</form>
				
				<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="operation">
			        <input type="hidden" name="action" value="show">
			        <input type="hidden" name="process" value="yes">
			    </form>
			<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
			<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="storno">
    			<input type="hidden" name="action" value="oper_confirm">
    			<input type="hidden" name="confirm_type" value="REMIND_PIN">
    			<input type="hidden" name="process" value="no">
			    <input type="hidden" name="id_term" value="<%=id_term %>">
			    <input type="hidden" name="storno_confirm_id" value="<%=storno_confirm_id %>">
				<input type="hidden" name="back_type" value="<%=back_type %>">
			</form>
			<% } %>
					<%
	}

	if ((action.equalsIgnoreCase("go") && successResult) ||
			action.equalsIgnoreCase("oper_confirm") && successResult) {	
			wpChequeObject cheque = new wpChequeObject(cancel_oper_id, Bean.getChequeSaveFormat(), Bean.loginTerm);
		%>
			<h1 class="confirm"><%=storno_type_name %>: <%=Bean.webposXML.getfieldTransl("operation_success", false) %><%=Bean.getHelpButton("storno", "div_action_big") %></h1>
 					<table class="table_cheque"><tbody>
					<tr><td class="centerb">
						<%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></td><td>
						<% if ("operations".equalsIgnoreCase(back_type) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION")) { %>
							<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateForm2", "div_main") %>
						<% } else { %>
							<%=Bean.getSubmitButtonAjax("action/storno.jsp", "button_back", "updateForm", "div_main") %>
						<% } %>
					</td></tr>
					<%=cheque.getChequeHTML(true) %>
					</tbody></table>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			</form>
				
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="operation">
		        <input type="hidden" name="action" value="show">
		        <input type="hidden" name="process" value="yes">
		    </form>
			<% 
	} 
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
	System.out.println("type2="+type);
}

%>


</body>
</html>
