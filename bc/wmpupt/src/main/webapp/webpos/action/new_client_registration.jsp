<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpUserObject"%>
<%@page import="webpos.wpTelegramObject"%>
<%@page import="bc.objects.bcTelegramObject"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_CARD_ACTIVATION";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

type 	= Bean.isEmpty(type)?"":type;
action 	= Bean.isEmpty(action)?"":action;
process = Bean.isEmpty(process)?"":process;

String id_term	= Bean.getCurrentTerm();

Bean.loginTerm.getTermAdditionFeature();
String termCurrency = Bean.loginTerm.getValue("SNAME_TERM_CURRENCY");

String idClub = Bean.loginTerm.getValue("ID_CLUB");
String idDealer = Bean.loginTerm.getValue("ID_DEALER");

String marginText = Bean.getMarginChangeToShareAccountDescription(idClub, idDealer);

String id_user = Bean.getDecodeParam(parameters.get("id_user")); 

Bean.readWebPosMenuHTML();

%>


<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
	<% type = "error_term"; %>
<% } %>
<%
if (type.equalsIgnoreCase("error_term")) {
} else 

if (type.equalsIgnoreCase("client")) {
	
	if (process.equalsIgnoreCase("yes")) {
		String
			cd_card1				= Bean.getDecodeParam(parameters.get("cd_card1")),
			new_client_package		= Bean.getDecodeParam(parameters.get("new_client_package")),
			pay_type				= Bean.getDecodeParam(parameters.get("pay_type")),
			club_date_beg			= Bean.getDecodeParam(parameters.get("club_date_beg")),
			//card_serial_number		= Bean.getDecodeParam(parameters.get("card_serial_number")),
			//id_issuer				= Bean.getDecodeParam(parameters.get("id_issuer")),
			//id_payment_system		= Bean.getDecodeParam(parameters.get("id_payment_system")),
			id_card_status          = Bean.getDecodeParam(parameters.get("id_card_status")),
			entered_sum				= Bean.getDecodeParamPrepare(parameters.get("entered_sum")),
			sum_change				= Bean.getDecodeParamPrepare(parameters.get("sum_change")),
			change_to_share_account	= Bean.getDecodeParamPrepare(parameters.get("change_to_share_account")),
			bank_trn				= Bean.getDecodeParam(parameters.get("bank_trn")),
			client_country			= Bean.getDecodeParam(parameters.get("client_country")),
			data_type				= Bean.getDecodeParam(parameters.get("data_type"));

		String id_entrance_fee_telgr = Bean.getDecodeParam(parameters.get("id_entrance_fee_telgr"));
		
		cd_card1				= !Bean.isEmpty(cd_card1)?cd_card1.replace(" ", ""):"";
		bank_trn = Bean.isEmpty(bank_trn)?"":bank_trn;
		if (Bean.isEmpty(client_country)) {
			client_country = Bean.loginTerm.getValue("ADR_CODE_COUNTRY_SERVICE_PLACE", "TERM");
		}
		
		String resultInt 				= Bean.C_SUCCESS_RESULT;
		String resultMessage 			= "";
		boolean isSuccessResult         = false;
		
		String phone_mobile 			= Bean.getDecodeParam(parameters.get("phone_mobile"));
		String id_telgr                 = Bean.getDecodeParam(parameters.get("id_telgr"));
		
		String dealer_margin_from_change   			= Bean.getDecodeParam(parameters.get("dealer_margin_from_change"));
		String sum_put_share_account_from_change 	= Bean.getDecodeParam(parameters.get("sum_put_share_account_from_change"));
		String can_send_pin_in_sms					= "";
		

		wpUserObject user = null;
		if (!Bean.isEmpty(id_user)) {
			user = new wpUserObject(id_user);
		}
		
		bcTelegramObject entrance_telgr = null;
		if (!Bean.isEmpty(id_entrance_fee_telgr)) {
			entrance_telgr = new bcTelegramObject(id_entrance_fee_telgr);
			entrance_telgr.getFeature();
		}
		
			
		if (action.equalsIgnoreCase("check_card")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_term);
			pParam.add(cd_card1);
			pParam.add("PUT_CARD");
			pParam.add(Bean.getDateFormat());
			
			String[] results = new String[10];
			
			results 						= Bean.executeFunction("PACK$WEBPOS_UI.oper_check_card", pParam, results.length);
			resultInt 						= results[0];
			id_card_status           		= results[1];
			// Следующие 7 параметров не используются
			String membership_month_sum  	= results[2];
			String membership_last_date		= results[3];
			String membership_nopay_month   = results[4];
			String membership_fee			= results[5];
			String membership_max_pay_month	= results[6];
			String membership_cd_currency   = results[7];
			String membership_fee_margin	= results[8];
			
	 		resultMessage 					= results[9];
	 		
	 		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {	
	 			isSuccessResult = true;
	 		}
	 		
		} else if (action.equalsIgnoreCase("check_phone_mobile")) {
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_term);
			pParam.add(phone_mobile);
			
			String[] results = new String[2];
			
			results 		= Bean.executeFunction("PACK$WEBPOS_UI.check_paid_entrance_fee", pParam, results.length);
			resultInt 		= results[0];
			resultMessage 	= results[1];
	 		
	 		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {	
	 			isSuccessResult = true;
	 		}

		} else if (action.equalsIgnoreCase("put_card")) {

	 		ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_term);
			pParam.add(id_user);
			pParam.add(cd_card1);
			pParam.add("N");
			pParam.add(client_country);
			pParam.add("NAT_PRS");
			pParam.add(new_client_package);
			if (!Bean.isEmpty(id_entrance_fee_telgr)) {
				pParam.add("NONE");
			} else {
				pParam.add(pay_type);
			}
			pParam.add(entered_sum);
			pParam.add(sum_change);
			pParam.add(change_to_share_account);
			pParam.add(bank_trn);
			pParam.add(club_date_beg);
			pParam.add(id_entrance_fee_telgr);
			pParam.add(Bean.getDateFormat());
			
			String[] results = new String[7];
			
			results 	= Bean.executeFunction("PACK$WEBPOS_UI.put_nat_prs_card", pParam, results.length);
				
			resultInt 							= results[0];
			id_telgr		 					= results[1];
			dealer_margin_from_change   		= results[2];
			sum_put_share_account_from_change 	= results[3];
			phone_mobile 						= results[4];
			can_send_pin_in_sms					= results[5];
	 		resultMessage 						= results[6];
	 		
	 		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt) || 
	 				Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt) ||
					Bean.C_NEED_PIN.equalsIgnoreCase(resultInt) ||
					Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt) ||
					Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) {	
	 			isSuccessResult = true;
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
			
			results 			= Bean.executeFunction("PACK$WEBPOS_UI.oper_confirm", pParam, results.length);
			resultInt 			= results[0];
			phone_mobile	 	= results[1];
			can_send_pin_in_sms = results[2];
	 		resultMessage 		= results[3];
	 		
	 		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {	
	 			isSuccessResult = true;
	 		}
		} 
	    		
		
		
		if ((action.equalsIgnoreCase("check_card") && !isSuccessResult)) {
			System.out.println("1");
	    %>
		<script>
		function validatePutCard(){
			var formParam = new Array (
				new Array ('cd_card1', 'card', 1),
				new Array ('client_country', 'varchar2', 1)	
			);
			return validateFormForID(formParam, 'updateForm1');
		}
		card_mask2("cd_card1");
		<% if ("PAID".equalsIgnoreCase(data_type)) { %>
		phone_mask_empty("phone_mobile","<%=client_country%>");
		<% } %>
		</script>
		<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("put_card", "div_action_big") %></h1>
		<%=Bean.getCardActivationTabSheets("1") %>
		
		<input type="radio" id="data_type_nopay" name="data_type" value="NOPAY" <%if ("NOPAY".equalsIgnoreCase(data_type)) { %>checked<% } %> onclick="ajaxpage('action/new_client.jsp?tab=1&data_type=NOPAY', 'div_main')"><label class="checbox_label" for="data_type_nopay">Вступительный взнос <font color="red">не оплачен</font></label><br>
		<input type="radio" id="data_type_paid" name="data_type" value="PAID" <%if ("PAID".equalsIgnoreCase(data_type)) { %>checked<% } %> onclick="ajaxpage('action/new_client.jsp?tab=1&data_type=PAID', 'div_main')"><label class="checbox_label" for="data_type_paid">Вступительный взнос <font color="green">оплачен</font> внешними платежными системами</label><br><br>
		
		<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			    <input type="hidden" name="type" value="client">
				<% if ("NOPAY".equalsIgnoreCase(data_type)) { %>
			       <input type="hidden" name="action" value="check_card">
				<% } else { %>
			       <input type="hidden" name="action" value="check_phone_mobile">
				<% } %>
			    <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_user" value="<%=id_user %>">
				<input type="hidden" name="data_type" value="<%=data_type %>">
				<% if (!(client_country == null || "".equalsIgnoreCase(client_country))) { %>
				<input type="hidden" name="client_country" value="<%=client_country %>">
				<% } %>
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<table class="action_table">
						<% if (!Bean.isEmpty(id_user)) { %>
							<tr>
								<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" id="name_user" size="30" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" id="sname_jur_prs" size="30" value="<%= user.getValue("SNAME_SERVICE_PLACE_WORK") %>" readonly class="inputfield_finish_blue"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3"><input type="text" name="fio_nat_prs" id="fio_nat_prs" size="30" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green"> </td>
							</tr>
							<tr>
								<td class="bottom_line_gray line_dashed"><%= Bean.webposXML.getfieldTransl("user_phone_mobile", false) %></td> <td colspan="3" class="bottom_line_gray line_dashed"><input type="text" name="phone_mobile" id="phone_mobile" size="30" value="<%= user.getValue("PHONE_MOBILE_HIDE") %>" readonly class="inputfield_finish_green"></td>
							</tr>
						<% } %>
			  			<tr><tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="30" value="<%=cd_card1 %>"  class="inputfield"></td></tr>
						<% if ((client_country == null || "".equalsIgnoreCase(client_country))) { %>
							<tr><td><%= Bean.webposXML.getfieldTransl("client_country", true) %></td><td><select name="client_country" id="client_country" class="inputfield"><%= Bean.getCountryOptions(client_country, true) %></select></td></tr>
						<% } %>
						<tr>
							<td colspan="4"><%=Bean.webposXML.getfieldTransl("goods_pay_way", true) %></td>
						</tr>
						<tr>
							<td colspan="4">
								<table border="0">
									<tr>
										<%=Bean.getPayTypeImage("CASH", "WEBPOS_SERVICE_CARD_ACTIVATION_PAY_CASH", "action/new_client_registration.jsp?pay_type=CASH", "div_action_big", "updateForm", "validatePutCard") %>
										<%=Bean.getPayTypeImage("BANK_CARD", "WEBPOS_SERVICE_CARD_ACTIVATION_PAY_BANK_CARD", "action/new_client_registration.jsp?pay_type=BANK_CARD", "div_action_big", "updateForm", "validatePutCard") %>
										<%=Bean.getPayTypeImage("INVOICE", "WEBPOS_SERVICE_CARD_ACTIVATION_MAKE_INVOICE", "action/new_client_registration.jsp?pay_type=INVOICE", "div_action_big", "updateForm", "validatePutCard") %>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td colspan="2"  align="center">
							<% if (!Bean.isEmpty(id_user)) { %>
								<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateFormBack", "div_main") %>
							<% } %> 
						</td></tr>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<tr><td colspan="2">
						<%=Bean.getWEBPosOnlyTestCards() %>
						</td></tr>
					</table>
				</form>
				<% if (!Bean.isEmpty(id_user)) { %>
					<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="tab" value="3">
				        <input type="hidden" name="type" value="edit">
				        <input type="hidden" name="action" value="edit">
				        <input type="hidden" name="id_user" value="<%=id_user %>">
					</form>
				<% } %> 
	    <%
	    
	    
		} else if ((action.equalsIgnoreCase("check_card") && isSuccessResult) ||
					("NOPAY".equalsIgnoreCase(data_type) && action.equalsIgnoreCase("put_card") && !isSuccessResult)) {
			System.out.println("2");
	    %>

		<script>
	function validatePutCard(){
		var returnValue = false;
		var formAll = new Array (
			new Array ('cd_card1', 'card', 1),
			new Array ('client_country', 'varchar2', 1),
			new Array ('club_date_beg', 'varchar2', 1),
			new Array ('new_client_package', 'varchar2', 1)
		);

		<% if ("BANK_CARD".equalsIgnoreCase(pay_type)) { %>
		var formPayBankCard = new Array (
				new Array ('bank_trn', 'varchar2', 1)
			);
		formAll = formAll.concat(formPayBankCard);
		<% } %>
		returnValue = validateFormForID(formAll, 'updateForm');
		<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
		if (returnValue) {
			returnValue = validateChange();
		}
		<% } %>
		return returnValue;
	}
	<%= Bean.getWEBPosCardPackagesCostScript(Bean.loginTerm.getValue("ID_DEALER", "TERM"), Bean.loginTerm.getValue("ID_CLUB", "TERM"), id_card_status) %>
	changeCardPackPayValue(document.getElementById('new_client_package'));
	<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
	calcChange();
	<% } %>
	</script>
			<h1><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %><%=Bean.getHelpButton("put_card", "div_action_big") %></h1>
			<%=Bean.getCardActivationTabSheets("1") %>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="client">
    			<input type="hidden" name="action" value="put_card">
    			<input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_user" value="<%=id_user %>">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="pay_type" value="<%=pay_type %>">
				<input type="hidden" name="id_card_status" value="<%=id_card_status %>">
				<input type="hidden" name="client_country" value="<%=client_country %>">
				<table class="action_table">
				<tbody>
						<% 	if (!Bean.isEmpty(id_user)) {
						%>
						<tr>
								<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" id="name_user" size="30" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" id="sname_jur_prs" size="30" value="<%= user.getValue("SNAME_SERVICE_PLACE_WORK") %>" readonly class="inputfield_finish_blue"> </td>
							</tr>
							<tr>
								<td class="bottom_line_gray line_dashed"><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3" class="bottom_line_gray line_dashed"><input type="text" name="fio_nat_prs" id="fio_nat_prs" size="30" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green"> </td>
							</tr>
						<% } %>
			  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="30" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly class="inputfield_finish_blue"></td></tr>
			  			<tr><td><%= Bean.webposXML.getfieldTransl("client_country", false) %></td><td><input type="text" name="client_country_name" id="client_country_name" size="30" value="<%=Bean.getCountryName(client_country) %>" readonly class="inputfield_finish_green"></td></tr>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("club_date_beg", true) %></td><td><%=Bean.getCalendarInputField("club_date_beg", Bean.isEmpty(club_date_beg)?"":club_date_beg, "10") %></td></tr>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("new_client_package", true) %></td>
							<td>
								<%=Bean.getSelectBeginHTML("new_client_package", Bean.webposXML.getfieldTransl("new_client_package", false), "changeCardPackPayValue(this)") %>
									<%= Bean.getWEBPosCardPackagesOptions(new_client_package, Bean.loginTerm.getValue("ID_DEALER", "TERM"), Bean.loginTerm.getValue("ID_CLUB", "TERM"), id_card_status, true) %>
								<%=Bean.getSelectEndHTML() %>
								<br><input type="hidden" name="pay_value" id="pay_value" value="">
								<% if (!"CASH".equalsIgnoreCase(pay_type)) { %>
								<input type="hidden" name="entered_sum" id="entered_sum" value="">
								<input type="hidden" name="sum_change" id="sum_change" value="">
								<input type="hidden" name="change_calc_error" id="change_calc_error" value="">
								<input type="hidden" name="change_to_share_account" id="change_to_share_account" value="">
								<% } %>
							</td>
						</tr>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
								<td>
									<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="15" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
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
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="15" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
							</td>
						</tr>
			  			<tr>
							<td><%=Bean.webposXML.getfieldTransl("bank_trn", true) %></td>
							<td>
								<input type="text" name="bank_trn" id="bank_trn" size="30" value="<%=bank_trn %>" class="inputfield">
								<% } else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="15" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
								<% } else if ("INVOICE".equalsIgnoreCase(pay_type)) {  %>
								<input type="text" name="pay_type_txt" id="pay_type_txt" size="15" value="<%=Bean.webposXML.getfieldTransl("goods_pay_invoices", false) %>" readonly class="inputfield_finish_green">
								<% } %>
							</td>
						</tr>
						<tr><td colspan="2" class="left">&nbsp;</td></tr>
						<tr>
							<td colspan="2" align="center">
						        <%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "put", "updateForm", "div_action_big", "validatePutCard") %>
						        <%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateForm", "div_main") %>
							</td>
						</tr>
						<% if (!isSuccessResult) { %>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<tr><td colspan="2">
						<% } %>
				<% if (!Bean.isEmpty(marginText)) { %>
					<tr><td colspan="2">
					<div id=div_hints>
						<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %></b><br>
							<%=marginText %><br>
						</i>
					</div>
					</td></tr>
				<% } %>
				</tbody>
				</table>
			</form>
		<%= Bean.getCalendarScript("club_date_beg", false) %>
			<% 
    		
		} else if (action.equalsIgnoreCase("check_phone_mobile") && !isSuccessResult) {
			System.out.println("3");
	    %>
		<script>
		function validatePutCard(){
			var formParam = new Array (
				new Array ('cd_card1', 'card', 1),
				new Array ('client_country', 'varchar2', 1)	
			);
			return validateFormForID(formParam, 'updateForm1');
		}
		card_mask2("cd_card1");
		<% if ("PAID".equalsIgnoreCase(data_type)) { %>
		function mask_form() {
			phone_mask_empty("phone_mobile","<%=client_country%>");
		}
		mask_form();
		function onAjaxDone(){
			mask_form();
		}
		<% } %>
		</script>
		<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("put_card", "div_action_big") %></h1>
		<%=Bean.getCardActivationTabSheets("1") %>
		
		<input type="radio" id="data_type_nopay" name="data_type" value="NOPAY" <%if ("NOPAY".equalsIgnoreCase(data_type)) { %>checked<% } %> onclick="ajaxpage('action/new_client.jsp?tab=1&data_type=NOPAY', 'div_main')"><label class="checbox_label" for="data_type_nopay">Вступительный взнос <font color="red">не оплачен</font></label><br>
		<input type="radio" id="data_type_paid" name="data_type" value="PAID" <%if ("PAID".equalsIgnoreCase(data_type)) { %>checked<% } %> onclick="ajaxpage('action/new_client.jsp?tab=1&data_type=PAID', 'div_main')"><label class="checbox_label" for="data_type_paid">Вступительный взнос <font color="green">оплачен</font> внешними платежными системами</label><br><br>
		
		<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			    <input type="hidden" name="type" value="client">
				<% if ("NOPAY".equalsIgnoreCase(data_type)) { %>
			       <input type="hidden" name="action" value="check_card">
				<% } else { %>
			       <input type="hidden" name="action" value="check_phone_mobile">
				<% } %>
			    <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_user" value="<%=id_user %>">
				<input type="hidden" name="data_type" value="<%=data_type %>">
				<% if (!(client_country == null || "".equalsIgnoreCase(client_country))) { %>
				<input type="hidden" name="client_country" value="<%=client_country %>">
				<% } %>
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<table class="action_table">
						<% 	if (!Bean.isEmpty(id_user)) {
						%>
						<tr>
								<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" id="name_user" size="30" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" id="sname_jur_prs" size="30" value="<%= user.getValue("SNAME_SERVICE_PLACE_WORK") %>" readonly class="inputfield_finish_blue"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3"><input type="text" name="fio_nat_prs" id="fio_nat_prs" size="30" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green"> </td>
							</tr>
							<% if ("NOPAY".equalsIgnoreCase(data_type)) { %>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_phone_mobile", false) %></td> <td colspan="3"><input type="text" name="user_phone_mobile" id="user_phone_mobile" size="30" value="<%= user.getValue("PHONE_MOBILE_HIDE") %>" readonly class="inputfield_finish_green"></td>
							</tr>
							<% } %>
						<% } %>
						<% if ("NOPAY".equalsIgnoreCase(data_type)) { %>
				  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %> class="top_line_gray line_dashed"><%=Bean.webposXML.getfieldTransl("cd_card1", true) %></td><td class="top_line_gray line_dashed"><input type="text" name="cd_card1" id="cd_card1" size="30" value="<%=cd_card1 %>"  class="inputfield"></td></tr>
							<% if ((client_country == null || "".equalsIgnoreCase(client_country))) { %>
							<tr><td><%= Bean.webposXML.getfieldTransl("client_country", true) %></td><td><select name="client_country" id="client_country" class="inputfield"><%= Bean.getCountryOptions(client_country, true) %></select></td></tr>
							<% } %>
							<tr>
								<td colspan="4"><%=Bean.webposXML.getfieldTransl("goods_pay_way", true) %></td>
							</tr>
							<tr>
								<td colspan="4">
									<table border="0">
										<tr>
											<%=Bean.getPayTypeImage("CASH", "WEBPOS_SERVICE_CARD_ACTIVATION_PAY_CASH", "action/new_client_registration.jsp?pay_type=CASH", "div_action_big", "updateForm", "validatePutCard") %>
											<%=Bean.getPayTypeImage("BANK_CARD", "WEBPOS_SERVICE_CARD_ACTIVATION_PAY_BANK_CARD", "action/new_client_registration.jsp?pay_type=BANK_CARD", "div_action_big", "updateForm", "validatePutCard") %>
											<%=Bean.getPayTypeImage("INVOICE", "WEBPOS_SERVICE_CARD_ACTIVATION_MAKE_INVOICE", "action/new_client_registration.jsp?pay_type=INVOICE", "div_action_big", "updateForm", "validatePutCard") %>
										</tr>
									</table>
								</td>
							</tr>
							<% if (!Bean.isEmpty(id_user)) { %>
								<tr><td colspan="2" class="center">
									<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateFormBack", "div_main") %>
								</td></tr>
							<% } %> 
						<% } else { %>
				  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %> class="top_line_gray line_dashed"><%=Bean.webposXML.getfieldTransl("client_phone_mobile", true) %></td><td class="top_line_gray line_dashed"><input type="text" name="phone_mobile" id="phone_mobile" size="30" value="<%=phone_mobile %>"  class="inputfield"></td></tr>
							<tr><td colspan="2">&nbsp;</td></tr>
							<tr><td colspan="2" class="center">
								<%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "button_further", "updateForm", "div_action_big", "validatePutCard") %> 
							<% if (!Bean.isEmpty(id_user)) { %>
									<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateFormBack", "div_main") %>
							<% } %>
 							</td></tr>
						<% } %>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<% if ("NOPAY".equalsIgnoreCase(data_type)) { %>
						<tr><td colspan="2">
						<%=Bean.getWEBPosOnlyTestCards() %>
						</td></tr>
						<% } %>
					</table>
				</form>
				<% if (!Bean.isEmpty(id_user)) { %>
					<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				        <input type="hidden" name="tab" value="3">
				        <input type="hidden" name="type" value="edit">
				        <input type="hidden" name="action" value="edit">
				        <input type="hidden" name="id_term_user" value="<%=id_user %>">
					</form>
				<% } %> 
	    <%
	    
		} else if ((action.equalsIgnoreCase("check_phone_mobile") && isSuccessResult)) {
			
			System.out.println("4");
				String tagEntranceFee = "_ENTRANCE_FEE";
				String tagEntranceFeeFind = "_ENTRANCE_FEE_FIND";

				String l_entrance_page = Bean.getDecodeParam(parameters.get("entrance_page"));
				Bean.pageCheck(pageFormName + tagEntranceFee, l_entrance_page);
				String l_entrance_page_beg = Bean.getFirstRowNumber(pageFormName + tagEntranceFee);
				String l_entrance_page_end = Bean.getLastRowNumber(pageFormName + tagEntranceFee);

				String entrance_find 	= Bean.getDecodeParam(parameters.get("entrance_find"));
				entrance_find 			= Bean.checkFindString(pageFormName + tagEntranceFeeFind, entrance_find, l_entrance_page);
				
				StringBuilder html = new StringBuilder();
				html.append(Bean.loginTerm.getExternalEntranceFeesHTML(id_user, phone_mobile, entrance_find, l_entrance_page_beg, l_entrance_page_end));
				%>
				<h1><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %><%=Bean.getHelpButton("put_card", "div_action_big") %></h1>
				<%=Bean.getCardActivationTabSheets("1") %>
				<table <%=Bean.getTableBottomFilter() %>>
				  	<tr>
					<%= Bean.getFindHTML("entrance_find", entrance_find, "action/new_client_registration.jsp?tab=1&type=client&action=check_phone_mobile&process=yes&data_type=PAID&phone_mobile="+phone_mobile, "div_action_big") %>
					
				    <!-- Вывод страниц -->
					<%= Bean.getPagesHTML(pageFormName + tagEntranceFee, "action/new_client_registration.jsp?tab=1&type=client&action=check_phone_mobile&process=yes&data_type=PAID&phone_mobile="+phone_mobile+"&", "transaction_page", Bean.loginTerm.getCardSalesCount(), "div_action_big") %>
				  	</tr>
				</table>
			<%= html.toString() %><br>
			<table class="action_table">
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateFormBack", "div_main") %>
					</td>
				</tr>
			</table>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="client">
	    			<input type="hidden" name="action" value="check_phone_mobile">
	    			<input type="hidden" name="process" value="yes">
	    			<input type="hidden" name="tab" value="1">
	    			<input type="hidden" name="data_type" value="PAID">
					<input type="hidden" name="id_user" value="<%=id_user %>">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="phone_mobile" value="<%=phone_mobile %>">
					<input type="hidden" name="client_country" value="<%=client_country %>">
			</form>
		<% 
    		
		} else if (action.equalsIgnoreCase("put_card_phone") ||
				("PAID".equalsIgnoreCase(data_type) && action.equalsIgnoreCase("put_card") && !isSuccessResult)) {
			System.out.println("5");
			
			isSuccessResult = action.equalsIgnoreCase("put_card_phone")?true:isSuccessResult;
			
		    %>
			<script>
			function validatePutCard(){
				var formAll = new Array (
					new Array ('cd_card1', 'card', 1),
					new Array ('club_date_beg', 'varchar2', 1)
					
				);
				return validateFormForID(formAll, 'updateForm');
			}
			card_mask2("cd_card1");
			</script>
				<h1><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %><%=Bean.getHelpButton("put_card", "div_action_big") %></h1>
				<%=Bean.getCardActivationTabSheets("1") %>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="client">
	    			<input type="hidden" name="action" value="put_card">
	    			<input type="hidden" name="process" value="yes">
					<input type="hidden" name="data_type" value="<%=data_type %>">
					<input type="hidden" name="id_user" value="<%=id_user %>">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="id_entrance_fee_telgr" value="<%=id_entrance_fee_telgr %>">
					<input type="hidden" name="client_country" value="<%=client_country %>">
					<table class="action_table">
					<tbody>
							<% 	if (!Bean.isEmpty(id_user)) {
							%>
							<tr>
									<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" id="name_user" size="30" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green"> </td>
								</tr>
								<tr>
									<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" id="sname_jur_prs" size="30" value="<%= user.getValue("SNAME_SERVICE_PLACE_WORK") %>" readonly class="inputfield_finish_blue"> </td>
								</tr>
								<tr>
									<td class="bottom_line_gray line_dashed"><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3" class="bottom_line_gray line_dashed"><input type="text" name="fio_nat_prs" id="fio_nat_prs" size="30" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green"> </td>
								</tr>
							<% } %>
				  			<tr><td><%= Bean.webposXML.getfieldTransl("cheque_entrance_fee_date", false) %></td><td><input type="text" name="cheque_entrance_fee_date" id="cheque_entrance_fee_date" size="30" value="<%=entrance_telgr.getValue("DATE_TELGR_FRMT") %>" readonly class="inputfield_finish_green"></td></tr>
							<tr><td><%= Bean.webposXML.getfieldTransl("pay_type", false) %></td><td><input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=entrance_telgr.getValue("NAME_TRANS_PAY_TYPE") %>" readonly class="inputfield_finish_green"></td></tr>
				  			<tr><td><%= Bean.webposXML.getfieldTransl("cheque_fee", false) %></td><td><input type="text" name="cheque_fee_sum" id="cheque_fee_sum" size="30" value="<%=entrance_telgr.getValue("OPR_SUM_FRMT") %> <%=entrance_telgr.getValue("SNAME_CURRENCY") %>" readonly class="inputfield_finish_blue"></td></tr>
				  			
				  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %> class="top_line_gray line_dashed"><%=Bean.webposXML.getfieldTransl("cd_card1", true) %></td><td class="top_line_gray line_dashed"><input type="text" name="cd_card1" id="cd_card1" size="30" value="<%=cd_card1 %>"  class="inputfield"></td></tr>
			  				<tr><td><%=Bean.webposXML.getfieldTransl("club_date_beg", true) %></td><td><%=Bean.getCalendarInputField("club_date_beg", Bean.isEmpty(club_date_beg)?entrance_telgr.getValue("DATE_TELGR_DF"):club_date_beg, "10") %></td></tr>
							<tr><td colspan="2" class="left">&nbsp;</td></tr>
							<tr>
								<td colspan="2" align="center">
							        <%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "button_further", "updateForm", "div_action_big", "validatePutCard") %>
							        <%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "button_back", "updateFormBack", "div_action_big") %>
								</td>
							</tr>
						<% if (!isSuccessResult) { %>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<tr><td colspan="2">
						<% } %>
							<tr><td colspan="2" class="left">
							<div id=div_hints>
							<%=Bean.getWEBPosOnlyTestCards() %>
							</div>
					</tbody>
					</table>
				</form>
		<%= Bean.getCalendarScript("club_date_beg", false) %>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="client">
	    			<input type="hidden" name="action" value="check_phone_mobile">
	    			<input type="hidden" name="process" value="yes">
	    			<input type="hidden" name="data_type" value="PAID">
					<input type="hidden" name="id_user" value="<%=id_user %>">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="phone_mobile" value="<%=entrance_telgr.getValue("phone_mobile_nat_prs") %>">
					<input type="hidden" name="client_country" value="<%=client_country %>">
			</form>
				<% 
    		
		} else if ((action.equalsIgnoreCase("put_card") && isSuccessResult) ||
				(action.equalsIgnoreCase("oper_confirm") && !isSuccessResult)) {
			System.out.println("7");
				
				if ((action.equalsIgnoreCase("put_card") && isSuccessResult) && (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt) ||
						Bean.C_NEED_PIN.equalsIgnoreCase(resultInt) ||
						Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt) ||
						Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) ||
						(action.equalsIgnoreCase("oper_confirm") && !isSuccessResult)) {
					
					wpTelegramObject oper = new wpTelegramObject(id_telgr);;
					%>
					<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %>: <%=Bean.webposXML.getfieldTransl("operation_confirmation", false) %><%=Bean.getHelpButton("put_card", "div_action_big") %></h1>
					<%=Bean.getCardActivationTabSheets("1") %>
					<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
					<% if (Bean.isEmpty(id_entrance_fee_telgr)) { %>
						<% if ("INVOICE".equalsIgnoreCase(pay_type)) { %>
							<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
			    			<input type="hidden" name="type" value="trans">
			    			<input type="hidden" name="process" value="yes">
			    			<input type="hidden" name="action" value="oper_confirm">
						<% } else { %>
							<input type="hidden" name="type" value="client">
			    			<input type="hidden" name="action" value="oper_confirm">
			    			<input type="hidden" name="process" value="yes">
							<input type="hidden" name="id_term" value="<%=id_term %>">
							<input type="hidden" name="id_user" value="<%=id_user %>">
							<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
							<input type="hidden" name="pay_type" value="<%=pay_type %>">
							<input type="hidden" name="id_card_status" value="<%=id_card_status %>">
							<input type="hidden" name="client_country" value="<%=client_country %>">
							<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
							<input type="hidden" name="new_client_package" value="<%=new_client_package %>">
							<input type="hidden" name="entered_sum" value="<%=entered_sum %>">
							<input type="hidden" name="sum_change" value="<%=sum_change %>">
							<input type="hidden" name="change_to_share_account" value="<%=change_to_share_account %>">
	    					<input type="hidden" name="confirm_type" value="NONE">
							<input type="hidden" name="sum_put_share_account_from_change" value="<%=sum_put_share_account_from_change %>">
							<input type="hidden" name="dealer_margin_from_change" value="<%=dealer_margin_from_change %>">
						<% } %>
					<% } else { %>
						<input type="hidden" name="type" value="client">
			    		<input type="hidden" name="action" value="oper_confirm">
			    		<input type="hidden" name="process" value="yes">
						<input type="hidden" name="id_term" value="<%=id_term %>">
						<input type="hidden" name="id_user" value="<%=id_user %>">
						<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
						<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
						<input type="hidden" name="id_card_status" value="<%=id_card_status %>">
						<input type="hidden" name="client_country" value="<%=client_country %>">
						<input type="hidden" name="id_entrance_fee_telgr" value="<%=id_entrance_fee_telgr %>">
	    				<input type="hidden" name="confirm_type" value="NONE">
					<% } %>
					<table class="action_table">
					<tbody>
						<% if (!Bean.isEmpty(id_user)) { %>
						<tr>
								<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" id="name_user" size="30" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green"> </td>
							</tr>
							<tr>
								<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" id="sname_jur_prs" size="30" value="<%= user.getValue("SNAME_SERVICE_PLACE_WORK") %>" readonly class="inputfield_finish_blue"> </td>
							</tr>
							<tr>
								<td class="bottom_line_gray line_dashed"><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3" class="bottom_line_gray line_dashed"><input type="text" name="fio_nat_prs" id="fio_nat_prs" size="30" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green"> </td>
							</tr>
						<% } %>
						<% if (Bean.isEmpty(id_entrance_fee_telgr)) { %>
				  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="30" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly class="inputfield_finish_blue"></td></tr>
							<tr><td><%= Bean.webposXML.getfieldTransl("client_country", false) %></td><td><input type="text" name="client_country_name" id="client_country_name" size="30" value="<%=Bean.getCountryName(client_country) %>" readonly class="inputfield_finish_green"></td></tr>
				  			<tr><td><%=Bean.webposXML.getfieldTransl("club_date_beg", false) %></td><td><input type="text" name="club_date_beg" id="club_date_beg" size="30" value="<%=club_date_beg %>" readonly class="inputfield_finish_blue"></td></tr>
							<tr><td class="top_line_gray line_dashed result_desc"><%=Bean.webposXML.getfieldTransl("new_client_package", false) %></td><td class="top_line_gray line_dashed"><input type="text" name="new_client_package_name" id="new_client_package_name" size="30" value="<%=Bean.getWEBPosCardPackagesTotalAmount(new_client_package) %> <%=termCurrency %>" readonly class="inputfield_finish_red"></td></tr>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
								<td>
									<% if ("CASH".equalsIgnoreCase(pay_type)) { %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %>" readonly class="inputfield_finish_green">
						<% if (!Bean.isEmpty(sum_change)) {  %>
							</td>
						</tr>
			  			<tr>
							<td class="pay_cash_change"><%=Bean.webposXML.getfieldTransl("entered_sum", false) %></td>
							<td class="pay_cash_change">
								<input type="text" name="entered_sum_title" id="entered_sum_title" size="30" value="<%=entered_sum %> <%=termCurrency %>" readonly class="inputfield_finish_blue">
							</td>
						</tr>
			  			<tr>
							<% if ("Y".equalsIgnoreCase(change_to_share_account)) { %>
								<td class="pay_cash_change result_desc"><%=Bean.webposXML.getfieldTransl("add_share_account_from_change", false) %></td><td class="pay_cash_change"><input type="text" name="sum_put_share_account" id="sum_put_share_account" size="30" value="<%=sum_put_share_account_from_change %> <%=termCurrency %>" readonly class="inputfield_finish_blue"></td>
							<% } else { %>
								<td class="pay_cash_change"><%=Bean.webposXML.getfieldTransl("sum_change", false) %></td><td class="pay_cash_change"><input type="text" name="sum_change_title" id="sum_change_title" size="30" value="<%=sum_change %> <%=termCurrency %>" readonly class="inputfield_finish_blue"></td>
							<% } %>

						<% } %>
									<% } else if ("BANK_CARD".equalsIgnoreCase(pay_type)) {  %>
				  					<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %>" readonly class="inputfield_finish_green">
								</td>
							</tr>
				  			<tr>
								<td><%=Bean.webposXML.getfieldTransl("bank_trn", false) %></td>
								<td>
									<input type="text" name="bank_trn" id="bank_trn" size="30" value="<%=bank_trn %>" readonly class="inputfield_finish_green">
									<% } else if ("SMPU_CARD".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_points", false) %>" readonly class="inputfield_finish_green">
									<% } else if ("INVOICE".equalsIgnoreCase(pay_type)) {  %>
									<input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=Bean.webposXML.getfieldTransl("goods_pay_invoices", false) %>" readonly class="inputfield_finish_green">
									<% } %>
								</td>
							</tr>
						<% if (!Bean.isEmpty(dealer_margin_from_change)) { %>
							<tr><td class="result_desc"><%=Bean.webposXML.getfieldTransl("cheque_dealer_margin", false) %></td><td colspan="2"><input type="text" name="dealer_margin_txt" id="dealer_margin_txt" size="20" value="<%=dealer_margin_from_change %> <%=termCurrency %>" class="inputfield_finish_red" maxlength="15"></td></tr>	  			<tr>
						<% } %>
					<% } else { %>
				
				  			<tr><td><%= Bean.webposXML.getfieldTransl("cheque_entrance_fee_date", false) %></td><td><input type="text" name="cheque_entrance_fee_date" id="cheque_entrance_fee_date" size="30" value="<%=entrance_telgr.getValue("DATE_TELGR_FRMT") %>" readonly class="inputfield_finish_green"></td></tr>
				  			<tr><td><%= Bean.webposXML.getfieldTransl("pay_type", false) %></td><td><input type="text" name="pay_type_txt" id="pay_type_txt" size="30" value="<%=entrance_telgr.getValue("NAME_TRANS_PAY_TYPE") %>" readonly class="inputfield_finish_green"></td></tr>
				  			<tr><td><%= Bean.webposXML.getfieldTransl("cheque_fee", false) %></td><td><input type="text" name="cheque_fee_sum" id="cheque_fee_sum" size="30" value="<%=entrance_telgr.getValue("OPR_SUM_FRMT") %> <%=entrance_telgr.getValue("SNAME_CURRENCY") %>" readonly class="inputfield_finish_blue"></td></tr>
				  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %> class="top_line_gray line_dashed"><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td class="top_line_gray line_dashed"><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="30" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly class="inputfield_finish_green"></td></tr>
				  			<tr><td><%=Bean.webposXML.getfieldTransl("club_date_beg", false) %></td><td><input type="text" name="club_date_beg" id="club_date_beg" size="30" value="<%=club_date_beg %>" readonly class="inputfield_finish_blue"></td></tr>
					<% } %>
							<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
							<tr><td colspan="2">&nbsp;</td></tr>
							<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_pin", false) %></td></tr>
							<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
							<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_pin"><%=Bean.webposXML.getfieldTransl("title_pin", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_PIN_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_PIN_PLACEHOLDER.length() %>"></td></tr>
								<% if ("Y".equalsIgnoreCase(can_send_pin_in_sms)) { %>
									<tr><td colspan="2">&nbsp;</td></tr>
									<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_pin_remind", false) %> <%=phone_mobile %>)</td></tr>
									<tr>
										<td colspan="2" align="center">
									        <%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "remind_pin", "updateForm2", "div_action_big") %>
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
							<% } else if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) { %>
							<tr><td colspan="2">&nbsp;</td></tr>
							<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_activation_code", false) %></td></tr>
							<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("card_activation_code", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER.length() %>"></td></tr>
							<% } else if (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt)) { %>
								<% if (!Bean.isEmpty(resultMessage)) { %>
									<tr><td colspan="2">&nbsp;</td></tr>
									<tr><td colspan="2"><span id="succes_description"><%=resultMessage %></span></td></tr>
									<tr><td colspan="2">&nbsp;</td></tr>
								<% } %>
							<% } %>
							<tr><td colspan="2">&nbsp;</td></tr>
							<% if ("INVOICE".equalsIgnoreCase(pay_type)) { %>
							<tr>
								<td><%=Bean.webposXML.getfieldTransl("pay_description", false) %></td><td><textarea name="pay_description" cols="27" rows="3" class="inputfield"><%=Bean.getTelegramPaymendDescription(oper) %></textarea></td>
							</tr>
							<tr><td colspan="2">&nbsp;</td></tr>
							<% } %>
							<tr>
								<td colspan="2" align="center">
				        			<% if ("INVOICE".equalsIgnoreCase(pay_type)) { %>
							        	<%=Bean.getSubmitButtonAjax("report/operationupdate.jsp", "confirm", "updateForm", "div_action_big", "") %>
									<% } else { %>
								        <%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
									<% } %>
							        <%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "button_back", "backForm", "div_action_big") %>
								</td>
							</tr>
							<% if (!isSuccessResult) { %>
							<tr><td colspan="2">&nbsp;</td></tr>
							<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
							<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
							<% } %>
						</tbody>
						</table>
					</form>
					<form id="backForm" name="backForm" method="POST" accept-charset="UTF-8">
						<input type="hidden" value="client" name="type">
						<input type="hidden" value="yes" name="process">
						<input type="hidden" name="id_term" value="<%=id_term %>">
						<input type="hidden" name="id_user" value="<%=id_user %>">
						<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
						<input type="hidden" name="pay_type" value="<%=pay_type %>">
						<input type="hidden" name="club_date_beg" value="<%=club_date_beg %>">
						<% if (Bean.isEmpty(id_entrance_fee_telgr)) { %>
						<input type="hidden" value="check_card" name="action">
						<input type="hidden" name="id_card_status" value="<%=id_card_status %>">
						<input type="hidden" name="client_country" value="<%=client_country %>">
						<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
						<input type="hidden" name="new_client_package" value="<%=new_client_package %>">
						<input type="hidden" name="pay_type" value="<%=pay_type %>">
						<input type="hidden" name="entered_sum" value="<%=entered_sum %>">
						<input type="hidden" name="sum_change" value="<%=sum_change %>">
						<input type="hidden" name="bank_trn" value="<%=bank_trn %>">
						<input type="hidden" name="change_to_share_account" value="<%=change_to_share_account %>">
						<% } else { %>
						<input type="hidden" name="action" value="put_card_phone" >
						<input type="hidden" name="data_type" value="<%=data_type %>">
						<input type="hidden" name="id_entrance_fee_telgr" value="<%=id_entrance_fee_telgr %>">
						<% } %>
					</form>
			<%
					
				} else {
			
				wpChequeObject cheque = new wpChequeObject(id_telgr, Bean.getChequeSaveFormat(), Bean.loginTerm);
			%>
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %>: <%=Bean.webposXML.getfieldTransl("operation_confirm", false) %><%=Bean.getHelpButton("put_card", "div_action_big") %></h1>
				<%=Bean.getCardActivationTabSheets("1") %>
		 			<table class="table_cheque"><tbody>
						<tr><td class="centerb"  colspan="2">	
						<div style="display: table-cell;"><%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></div>
						<div style="display: table-cell; width: 100%; text-align: right;">
							<%=Bean.getSubmitButtonAjax("action/new_client_questionnaire.jsp", "button_questionnaire", "updateForm12", "div_action_big", "validatePutCard") %>
						    <%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateForm12", "div_main") %>
						</div>
						</td></tr>
						<%=cheque.getChequeHTML(true) %>
					</tbody></table>
				<form name="updateForm12" id="updateForm12" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="questionnaire">
	    			<input type="hidden" name="action" value="edit">
	    			<input type="hidden" name="process" value="no">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
					<input type="hidden" name="id_role" value="<%=cheque.getValue("ID_NAT_PRS_ROLE") %>">
				</form>
		<%
				}
		
		} else if (action.equalsIgnoreCase("oper_confirm") && isSuccessResult) {
			System.out.println("8");
			
			wpChequeObject cheque = new wpChequeObject(id_telgr, Bean.getChequeSaveFormat(), Bean.loginTerm);
		%>
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %>: <%=Bean.webposXML.getfieldTransl("operation_confirm", false) %><%=Bean.getHelpButton("put_card", "div_action_big") %></h1>
				<%=Bean.getCardActivationTabSheets("1") %>
		 			<table class="table_cheque"><tbody>
						<tr><td class="centerb"  colspan="2">	
						<div style="display: table-cell;"><%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></div>
						<div style="display: table-cell; width: 100%; text-align: right;">
							<%=Bean.getSubmitButtonAjax("action/new_client_questionnaire.jsp", "button_questionnaire", "updateForm12", "div_action_big", "validatePutCard") %>
						    <%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateForm12", "div_main") %>
						</div>
						</td></tr>
						<%=cheque.getChequeHTML(true) %>
					</tbody></table>
				<form name="updateForm12" id="updateForm12" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="type" value="questionnaire">
	    			<input type="hidden" name="action" value="edit">
	    			<input type="hidden" name="process" value="no">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
					<input type="hidden" name="id_role" value="<%=cheque.getValue("ID_NAT_PRS_ROLE") %>">
				</form>

   	<% } else { %> 
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
