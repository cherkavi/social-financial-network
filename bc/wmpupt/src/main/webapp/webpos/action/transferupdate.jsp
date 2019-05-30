<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="webpos.wpNatPrsObject"%>
<%@page import="webpos.wpTelegramObject"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_TRANSFER";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

type 	= Bean.isEmpty(type)?"":type;
action 	= Bean.isEmpty(action)?"":action;
process = Bean.isEmpty(process)?"":process;

String	id_term		= Bean.getCurrentTerm();

Bean.loginTerm.getTermFeature();

Bean.readWebPosMenuHTML();

%>


<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
	<% type = "error_term"; %>
<% } %>
<%
if (type.equalsIgnoreCase("error_term")) {
} else 

if (type.equalsIgnoreCase("transfer")) {
	String	
		from_card					= Bean.getDecodeParam(parameters.get("from_card")).replace(" ", ""),
		to_card						= Bean.getDecodeParam(parameters.get("to_card")).replace(" ", ""),
		transfer_value				= Bean.getDecodeParam(parameters.get("transfer_value")),
		//transfer_currency			= Bean.getDecodeParam(parameters.get("transfer_currency")),
		can_use_share_account		= Bean.getDecodeParam(parameters.get("can_use_share_account"));
	
	if (process.equalsIgnoreCase("no")) {

		if (action.equalsIgnoreCase("put_to_another_card")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_term);
			pParam.add(from_card);
			pParam.add(to_card);
			pParam.add(transfer_value);
			//pParam.add(transfer_currency);
			pParam.add(can_use_share_account);
			
			String[] results = new String[6];
			
			results 						= Bean.executeFunction("PACK$WEBPOS_UI.transfer_from_to_card", pParam, results.length);
			String resultInt 				= results[0];
			String id_telgr					= results[1];
			String phone_mobile_confirm		= results[2];
			String can_send_pin_in_sms		= results[3];
			String card_type_error  		= results[4];
	 		String resultMessage 			= results[5];
	    	
	    	if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) {
	    		
	    		if (!(Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt) || 
	    				Bean.C_NEED_PIN.equalsIgnoreCase(resultInt) || 
	    				Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt) || 
	    				Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt) || 
	    				Bean.C_SMS_CONFIRM_CREATED.equalsIgnoreCase(resultInt))) {
	    %>
		
		
			<script>
		
			function validateTransfer(){
				var returnValue = null;
				var formParam = new Array (
					new Array ('from_card', 'card', 1),
					new Array ('to_card', 'card', 1),
					new Array ('transfer_value', 'oper_sum', 1)
				);
				returnValue = validateFormForID(formParam, 'updateForm5');
				return returnValue;
			}
			card_mask2("from_card");
			card_mask2("to_card");
			</script>
			<% 	//int checkBoxCount = 0; 
				String way1Disable = "";
				String way2Disable = "";
				int disableCount = 0;
			%>
		<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_transfer", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("transfer", "div_action_big") %></h1>
		<form name="updateForm5" id="updateForm5" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="transfer">
			<input type="hidden" name="action" value="put_to_another_card">
			<input type="hidden" name="process" value="no">
			<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
			<input type="hidden" name="id_term" value="<%=id_term %>">
			<table class="action_table">
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("transfer_from_card", true) %></td><td><input type="text" name="from_card" id="from_card" size="30" value="<%=from_card %>"  class="inputfield"></td></tr>
				<tr>
					<td><%=Bean.webposXML.getfieldTransl("transfer_amount", true) %></td>
					<td>
						<input type="text" name="transfer_value" id="transfer_value" size="20" value="<%=transfer_value %>" class="inputfield" maxlength="15"><input type="text" name="transfer_point_currency" id="transfer_point_currency" size="5" value="<%=Bean.webposXML.getfieldTransl("title_transfer_points_currency", false) %>" readonly class="inputfield-ro">
					</td>
				</tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("transfer_to_card", true) %></td><td><input type="text" name="to_card" id="to_card" size="30" value="<%=to_card %>"  class="inputfield"></td></tr>
				<% if (Bean.C_ISNT_ENOUGH_POINTS.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td><span class="can_use_share_account"><%= Bean.webposXML.getfieldTransl("can_use_share_account", false) %></span></td><td><select name="can_use_share_account" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", can_use_share_account, false) %></select></td>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"  align="center"><%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "transfer", "updateForm5", "div_action_big", "validateTransfer") %></td></tr>
				</tr>
				<% } else { %>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
					<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"  align="center"><%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "transfer", "updateForm5", "div_action_big", "validateTransfer") %></td></tr>
				<% } %>

				<% if (Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_REPLANISH_MEMBERSHIP_FEE")) { %>
					<tr><td colspan="2"  align="center"><%=Bean.getSubmitButtonAjax("action/replenis.jsp", "bring", "updateForm2", "div_main") %> </td></tr>
				<% } %>
				<% if (Bean.C_CARD_NOT_GIVEN.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_REGISTRATION")) { %>
					<tr><td colspan="2"  align="center"><%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "button_give", "updateForm2", "div_action_big") %> </td></tr>
				<% } %>
				<% if (Bean.C_CARD_NOT_QUESTIONED.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_QUESTIONNAIRE")) { %>
					<tr><td colspan="2"  align="center"><%=Bean.getSubmitButtonAjax("action/new_client_questionnaire.jsp", "button_questionnaire", "updateForm2", "div_action_big") %> </td></tr>
				<% } %>
				<% if (Bean.C_CARD_NOT_ACTIVATED.equalsIgnoreCase(resultInt) && Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_ACTIVATION")) { %>
					<tr><td colspan="2"  align="center"><%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "activate", "updateForm2", "div_action_big") %> </td></tr>
				<% } %>
				<tr><td colspan="2">
				<div id=div_hints>
					<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %>
					</b><br><%=Bean.webposXML.getfieldTransl("title_note_transfer", false) %></i>
					<%=Bean.getWEBPosOnlyTestCards(false) %>
				</div>
				</td></tr>
			</table>
		</form>		
		<% if (Bean.C_MEMBERSHIP_FEE_ERROR.equalsIgnoreCase(resultInt)) { %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
			</form>
		<% } %>
		<% if (Bean.C_CARD_NOT_GIVEN.equalsIgnoreCase(resultInt) || 
				Bean.C_CARD_NOT_ACTIVATED.equalsIgnoreCase(resultInt)) { %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="client">
				<input type="hidden" name="action" value="check_card">
				<input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="cd_card1" value="<%=from_card %>">
			</form>
		<% } %>
		<% if (Bean.C_CARD_NOT_QUESTIONED.equalsIgnoreCase(resultInt)) { %>
			<% wpClubCardObject card = new wpClubCardObject(from_card); %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="action" value="edit">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="cd_card1" value="<%=from_card %>">
				<input type="hidden" name="id_role" value="<%=card.getValue("ID_NAT_PRS_ROLE_CURRENT") %>">
			</form>
		<% } %>
			<% } else { 
			


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
			</script>
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_transfer", false) %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation", false) %><%=Bean.getHelpButton("transfer", "div_action_big") %></h1>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
					<input type="hidden" name="type" value="transfer">
	    			<input type="hidden" name="action" value="oper_confirm">
					<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
	    			<input type="hidden" name="confirm_type" value="PIN">
					<% } else if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
	    			<input type="hidden" name="confirm_type" value="SMS">
					<% } else if (Bean.C_SMS_CONFIRM_CREATED.equalsIgnoreCase(resultInt)) { %>
	    			<input type="hidden" name="confirm_type" value="SMS">
					<% } else if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) { %>
	    			<input type="hidden" name="confirm_type" value="ACTIVATION_CODE">
					<% } else if (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt)) { %>
	    			<input type="hidden" name="confirm_type" value="NONE">
					<% } %>
	    			<input type="hidden" name="process" value="yes">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="from_card" value="<%=from_card %>">
					<input type="hidden" name="to_card" value="<%=to_card %>">
					<input type="hidden" name="transfer_value" value="<%=transfer_value %>">
	    			<table class="action_table">
					<tbody>
				<!-- <tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><input type="text" name="doc_number" id="doc_number" size="30" value="<%=Bean.loginTerm.getNextDocumentNumber() %>" readonly class="inputfield_finish"></td></tr> -->
			  	<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("transfer_from_card", false) %></td><td><input type="text" name="from_cd_card1_hide" id="from_cd_card1_hide" size="30" value="<%=Bean.hideCdCard1(from_card) %>" readonly class="inputfield_finish_blue"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("transfer_amount", false) %></td><td><input type="text" name="transfer_value_frmt" id="transfer_value_frmt" size="20" value="<%=transfer_value %> <%=Bean.webposXML.getfieldTransl("title_transfer_points_currency", false) %>" readonly class="inputfield_finish_red"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("transfer_to_card", false) %></td><td><input type="text" name="to_cd_card1_hide" id="to_cd_card1_hide" size="30" value="<%=Bean.hideCdCard1(to_card) %>" readonly class="inputfield_finish_green"></td></tr>
				

				<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_pin", false) %></td></tr>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_pin"><%=Bean.webposXML.getfieldTransl("title_pin", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_PIN_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_PIN_PLACEHOLDER.length() %>"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %>
					</td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% if ("Y".equalsIgnoreCase(can_send_pin_in_sms)) { %>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_pin_remind", false) %> <%=phone_mobile_confirm %>)</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "remind_pin", "updateForm2", "div_action_big") %>
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
					        <%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %>
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
		    		<span>Не получили СМС? <span class="go_to" onclick="ajaxpage('service/get_sms_code.jsp?id_telgr=<%= id_telgr%>&action=get_sms_code&back_type=transfer', 'div_sms_confirmation')" title="<%=Bean.buttonXML.getfieldTransl("button_questionnaire", false) %>">Отправить еще раз</span></span>
		    		<br>
		    		<% } else { %>
		    		<br>
		    		<% } %>
		    		<br>
		    		<span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("sms_password", false) %></span>&nbsp;&nbsp;&nbsp;<input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER.length() %>"><br><br>
		    		<div style="width:100%; text-align: center;">
		    		    <%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
		    		    <%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %>
		    		</div>
					</div>
				</td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } else if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2">
					<div id="div_sms_confirmation">
						<%=(!Bean.isEmpty(resultMessage))?"<span id=\"error_description\">" + resultMessage + "</span><br><br>":"" %>
						<div style="width:100%; text-align: center;">
					        <%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
					        <%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %>
						</div>
					</div>
				</td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } else if (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt)) { %>
				<% if (!Bean.isEmpty(resultMessage)) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><span id="succes_description"><%=resultMessage %></span></td></tr>
				<% } %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("service/get_sms_code.jsp", "button_send_sms", "updateFormGetSMS", "div_sms_confirmation", "") %>
				        <%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %>
					</td>
				</tr>
				<% } %>
				</tbody>
				</table>
			</form>
			<% if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
			<form name="updateFormGetSMS" id="updateFormGetSMS" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
    			<input type="hidden" name="action" value="get_sms_code">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="back_type" value="transfer">
			</form>
			<% } %>
			<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
    			<input type="hidden" name="type" value="transfer">
    			<input type="hidden" name="action" value="oper_confirm">
    			<input type="hidden" name="confirm_type" value="REMIND_PIN">
    			<input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_term" value="<%=id_term %>">
    			<input type="hidden" name="from_card" value="<%=from_card %>">
    			<input type="hidden" name="to_card" value="<%=to_card %>">
    			<input type="hidden" name="transfer_value" value="<%=transfer_value %>">
			</form>
			<% } %>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_term" value="<%=id_term %>">
    			<input type="hidden" name="from_card" value="<%=from_card %>">
    			<input type="hidden" name="to_card" value="<%=to_card %>">
    			<input type="hidden" name="transfer_value" value="<%=transfer_value %>">
			</form>
			<% } %>		
	    <%
			} else {
				
				wpChequeObject cheque = new wpChequeObject(id_telgr, Bean.getChequeSaveFormat(), Bean.loginTerm);

			%>
			<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_replenish", false) %>: <%=Bean.webposXML.getfieldTransl("operation_confirm", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
				<table class="table_cheque"><tbody>
					<tr><td class="centerb">
						<%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></td><td>
						<%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "button_back", "updateForm", "div_main") %><br><br>
					</td></tr>
					<%=cheque.getChequeHTML(true) %>
				</tbody></table>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			</form>
			<%
			}
	    	
 		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
			}
	} else if (process.equalsIgnoreCase("yes")) {
			if (action.equalsIgnoreCase("oper_confirm")) {
				String
					id_telgr			= Bean.getDecodeParam(parameters.get("id_telgr")),
					confirm_type		= Bean.getDecodeParam(parameters.get("confirm_type")),
					confirm_code		= Bean.getDecodeParam(parameters.get("confirm_code"));
				
				ArrayList<String> pParam = new ArrayList<String>();
				
				pParam.add(id_telgr);
				pParam.add(confirm_type);
				pParam.add(confirm_code);
				pParam.add("");
				
				String[] results = new String[4];
				
				results 						= Bean.executeFunction("PACK$WEBPOS_UI.oper_confirm", pParam, results.length);
				String resultInt 				= results[0];
				String phone_mobile_confirm		= results[1];
				String can_send_pin_in_sms  	= results[2];
		 		String resultMessage 			= results[3];
		    	
		    	
		    %>
			<% 
			if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
				
				wpChequeObject cheque = new wpChequeObject(id_telgr, Bean.getChequeSaveFormat(), Bean.loginTerm);
			%>
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_replenish", false) %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation_success", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
	 					<table class="table_cheque"><tbody>
						<tr><td class="centerb">
						<%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></td><td>
						<%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %><br><br>
						</td></tr>
						<%=cheque.getChequeHTML(true) %>
						</tbody></table>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				</form>
			<% } else { %>
				<script>
				function validateData(){
					var formParam = new Array (
						new Array ('confirm_code', 'number', 1)
					);
					return validateFormForID(formParam, 'updateForm');
				}
				card_mask2("from_card");
				card_mask2("to_card");
			</script>
				<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_transfer", false) %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation_error", false) %><%=Bean.getHelpButton("transfer", "div_action_big") %></h1>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
					<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
					<input type="hidden" name="type" value="transfer">
	    			<input type="hidden" name="action" value="oper_confirm">
					<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
	    			<input type="hidden" name="confirm_type" value="PIN">
					<% } %>
	    			<% if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
	    			<input type="hidden" name="confirm_type" value="SMS">
					<% } %>
	    			<% if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) { %>
	    			<input type="hidden" name="confirm_type" value="ACTIVATION_CODE">
					<% } %>
	    			<input type="hidden" name="process" value="yes">
					<input type="hidden" name="id_term" value="<%=id_term %>">
	    			<input type="hidden" name="from_card" value="<%=from_card %>">
	    			<input type="hidden" name="to_card" value="<%=to_card %>">
	    			<input type="hidden" name="transfer_value" value="<%=transfer_value %>">
	    			<table class="action_table">
					<tbody>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cheque_number_full", false) %></td><td><input type="text" name="doc_number" id="doc_number" size="30" value="<%=Bean.loginTerm.getNextDocumentNumber() %>" readonly class="inputfield_finish"></td></tr>
			  	<tr><td><%=Bean.webposXML.getfieldTransl("transfer_from_card", false) %></td><td><input type="text" name="from_cd_card1_hide" id="from_cd_card1_hide" size="30" value="<%=Bean.hideCdCard1(from_card) %>" readonly class="inputfield_finish_blue"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("transfer_amount", false) %></td><td><input type="text" name="transfer_value_frmt" id="transfer_value_frmt" size="20" value="<%=transfer_value %> <%=Bean.webposXML.getfieldTransl("title_transfer_points_currency", false) %>" readonly class="inputfield_finish_red"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("transfer_to_card", false) %></td><td><input type="text" name="to_cd_card1_hide" id="to_cd_card1_hide" size="30" value="<%=Bean.hideCdCard1(to_card) %>" readonly class="inputfield_finish_green"></td></tr>

				<tr><td colspan="2">&nbsp;</td></tr>
				<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_pin", false) %></td></tr>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_pin"><%=Bean.webposXML.getfieldTransl("title_pin", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_PIN_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_PIN_PLACEHOLDER.length() %>"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %>
					</td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% if ("Y".equalsIgnoreCase(can_send_pin_in_sms)) { %>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_pin_remind", false) %> <%=phone_mobile_confirm %>)</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "remind_pin", "updateForm2", "div_action_big") %>
					</td>
				</tr>
				<% } %>
				<% } else if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
				
				<% if (!Bean.isEmpty(resultMessage)) { %>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } %>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("sms_password", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER.length() %>"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %>
					</td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } else if (Bean.C_NEED_ACTIVATION_CODE.equalsIgnoreCase(resultInt)) { %>
				<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_activation_code", false) %></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("card_activation_code", false) %></span></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER.length() %>"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/transferupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %>
					</td>
				</tr>
				<% } else { %>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/transfer.jsp", "button_back", "updateForm", "div_main") %>
					</td>
				</tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<% } %>
				</tbody>
				</table>
				</form>
			<% if (Bean.C_NEED_PIN.equalsIgnoreCase(resultInt)) { %>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="id_telgr" value="<%=id_telgr %>">
    			<input type="hidden" name="type" value="transfer">
    			<input type="hidden" name="action" value="oper_confirm">
    			<input type="hidden" name="confirm_type" value="REMIND_PIN">
    			<input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_term" value="<%=id_term %>">
    			<input type="hidden" name="from_card" value="<%=from_card %>">
    			<input type="hidden" name="to_card" value="<%=to_card %>">
    			<input type="hidden" name="transfer_value" value="<%=transfer_value %>">
			</form>
			<% } %>
			<% } 
	 			
		} else { %> 
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
