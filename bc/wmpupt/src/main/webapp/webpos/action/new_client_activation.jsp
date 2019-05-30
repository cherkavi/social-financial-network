<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpNatPrsObject"%>
<%@page import="webpos.wpNatPrsRoleObject"%>
<%@page import="webpos.wpUserObject"%>
<%@page import="webpos.wpTransactionObject"%>
<%@page import="webpos.wpTelegramObject"%><html>
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

String id_user		= Bean.getDecodeParam(parameters.get("id_user"));

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
			card_activation_code	= Bean.getDecodeParam(parameters.get("card_activation_code")),

			id_trans_given			= Bean.getDecodeParam(parameters.get("id_trans_given")),
			//id_trans_activation		= Bean.getDecodeParam(parameters.get("id_trans_activation")),
			id_telgr_activation		= Bean.getDecodeParam(parameters.get("id_telgr_activation"));
		

		String resultInt 				= Bean.C_SUCCESS_RESULT;
 		String resultMessage 			= "";
 		
		if (cd_card1 != null) {
			cd_card1 = cd_card1.replace(" ", "");
		}

		if (action.equalsIgnoreCase("check_card")) {
			
	 		ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_term);
			pParam.add(cd_card1);
			pParam.add(id_trans_given);
			
			String[] results = new String[3];
			
			results 				= Bean.executeFunction("PACK$WEBPOS_UI.activation_card_check", pParam, results.length);
			resultInt 				= results[0];
			id_trans_given	    	= results[1];
	 		resultMessage 			= results[2];
	 		
		} else if (action.equalsIgnoreCase("put_card")) {
			ArrayList<String> pParam = new ArrayList<String>();
			
			String[] results = new String[5];

			pParam.add(id_term);
			pParam.add(cd_card1);
			pParam.add(card_activation_code);
			pParam.add(id_trans_given);

			results 	= Bean.executeFunction("PACK$WEBPOS_UI.activation_nat_prs_card", pParam, results.length);
				
			resultInt 						= results[0];
			id_telgr_activation				= results[1];
			String phone_mobile_confirm		= results[2];
	 		String can_send_pin_in_sms		= results[3];
	 		resultMessage 					= results[4];
	 		
		} else if (action.equalsIgnoreCase("sms_confirm")) {
			String
				confirm_type		= Bean.getDecodeParam(parameters.get("confirm_type")),
				confirm_code		= Bean.getDecodeParam(parameters.get("confirm_code"));
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_telgr_activation);
			pParam.add(confirm_type);
			pParam.add(confirm_code);
			pParam.add("");
			
			String[] results = new String[4];
			
			results 					= Bean.executeFunction("PACK$WEBPOS_UI.oper_confirm", pParam, results.length);
			resultInt 					= results[0];
			String phone_mobile_confirm	= results[1];
			String can_send_pin_in_sms  = results[2];
	 		resultMessage 				= results[3];
		}
	    	
		
		
		if (action.equalsIgnoreCase("check_card") && !(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) {	%>
		<script>
			function validatePutCard(){
				var formParam = new Array (
					new Array ('cd_card1', 'card', 1)
				);
				return validateFormForID(formParam, 'updateForm1');
			}
			card_mask2("cd_card1");
		</script>
		<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_activation", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("activation", "div_action_big") %></h1>
		<%=Bean.getCardActivationTabSheets("3") %>
		<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="client">
			        <input type="hidden" name="action" value="check_card">
			        <input type="hidden" name="process" value="yes">
					<input type="hidden" name="id_user" value="<%=id_user %>">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<table class="action_table">
					<% 	if (!Bean.isEmpty(id_user)) {
							wpUserObject user = new wpUserObject(id_user);
					%>
					<tr>
						<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" id="name_user" size="30" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green"> </td>
					</tr>
					<tr>
						<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" id="sname_jur_prs" size="30" value="<%= user.getValue("SNAME_JUR_PRS") %>" readonly class="inputfield_finish_blue"> </td>
					</tr>
					<tr>
						<td><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3"><input type="text" name="fio_nat_prs" id="fio_nat_prs" size="30" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green"> </td>
					</tr>
					<tr>
						<td><%= Bean.webposXML.getfieldTransl("user_phone_mobile", false) %></td> <td colspan="3"><input type="text" name="phone_mobile" id="phone_mobile" size="30" value="<%= user.getValue("PHONE_MOBILE_HIDE") %>" readonly class="inputfield_finish_green"></td>
					</tr>
					<% } %>
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="30" value="<%=cd_card1 %>"  class="inputfield"></td></tr>
			  		<tr><td colspan="2" class="left">&nbsp;</td></tr>
					<tr><td colspan="2" class="center"><%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "button_further", "updateForm", "div_action_big", "validatePutCard") %> </td></tr>
					<tr><td colspan="2" class="left"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
					<tr><td colspan="2" class="left"><span id="error_description"><%=resultMessage %></span></td></tr>
					<tr><td colspan="2" class="left">
					<div id=div_hints>
					<%=Bean.getWEBPosOnlyTestCards() %>
					</div>
					</td></tr>
				</table>
			</form>
	    <%
			
		} else if ( (action.equalsIgnoreCase("check_card") && (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) ||
				    (action.equalsIgnoreCase("put_card") && !(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt) || Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)))) {
				wpTransactionObject trans_given = new wpTransactionObject(id_trans_given);

				wpNatPrsRoleObject role = new wpNatPrsRoleObject(trans_given.getValue("ID_NAT_PRS_ROLE"));
				
	    %>
		<script>
			function validatePutCard(){
				var returnValue = false;
				var formAll = new Array (
					new Array ('card_activation_code', 'varchar2', 1)
				);
				
				returnValue = validateFormForID(formAll, 'updateForm');
				return returnValue;
			}
		</script>
			<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
			<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_activation", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("activation", "div_action_big") %></h1>
			<% } else { %>
			<h1><%=Bean.webposXML.getfieldTransl("title_activation", false) %><%=Bean.getHelpButton("activation", "div_action_big") %></h1>
			<% } %>
			<%=Bean.getCardActivationTabSheets("3") %>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="client">
				<input type="hidden" name="action" value="put_card">
    			<input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_user" value="<%=id_user %>">
				<input type="hidden" name="id_term" value="<%=id_term %>">
				<input type="hidden" name="id_trans_given" value="<%=id_trans_given %>">
				<input type="hidden" name="id_telgr_activation" value="<%=id_telgr_activation %>">
				<input type="hidden" name="cd_card1" value="<%=trans_given.getValue("CD_CARD1") %>">
				<table class="action_table">
				<tbody>
					<% 	if (!Bean.isEmpty(id_user)) {
							wpUserObject user = new wpUserObject(id_user);
					%>
					<tr>
						<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" id="name_user" size="30" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green"> </td>
					</tr>
					<tr>
						<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" id="sname_jur_prs" size="30" value="<%= user.getValue("SNAME_JUR_PRS") %>" readonly class="inputfield_finish_blue"> </td>
					</tr>
					<tr>
						<td><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3"><input type="text" name="fio_nat_prs" id="fio_nat_prs" size="30" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green"> </td>
					</tr>
					<% } %>

		  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="30" value="<%=role.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish"></td></tr>
					<tr><td><%= Bean.webposXML.getfieldTransl("client_country", false) %></td><td><input type="text" name="client_country_name" id="client_country_name" size="30" value="<%=Bean.getCountryName(role.getValue("CODE_COUNTRY")) %>" readonly class="inputfield_finish"></td></tr>
		  			<tr><td><%=Bean.webposXML.getfieldTransl("club_date_beg", false) %></td><td><input type="text" name="club_date_beg" id="club_date_beg" size="10" value="<%=role.getValue("DATE_CARD_SALE_DF") %>" readonly class="inputfield_finish_blue"></td></tr>

					<tr><td><%=Bean.webposXML.getfieldTransl("client_fio_nat_prs", false) %></td><td><input type="text" name="client_fio_nat_prs" id="client_fio_nat_prs" size="30" value="<%=role.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green"></td></tr>
		  			<tr><td><%=Bean.webposXML.getfieldTransl("client_phone_mobile", false) %></td><td><input type="text" name="client_phone_mobile" id="client_phone_mobile" size="30" value="<%=role.getValue("PHONE_MOBILE_HIDE") %>" readonly class="inputfield_finish_green"></td></tr>
		  			<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td><%=Bean.webposXML.getfieldTransl("card_activation_code", true) %></td><td><input type="password" name="card_activation_code" id="card_activation_code" size="20" value="" class="inputfield" placeholder="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER %>" autofocus="" required="" value="" maxlength="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER.length() %>"></td></tr>
		  			<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td colspan="2" class="center">
					        <%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "activate", "updateForm", "div_action_big", "validatePutCard") %>
							<% if (Bean.isEmpty(id_user)) { %>
					        <%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateFormBack", "div_main", "") %>
							<% } else { %>
					        <%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateFormBack", "div_main", "") %>
							<% } %>
						</td>
					</tr>
					<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
					<tr><td colspan="2" class="left"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
					<tr><td colspan="2" class="left"><span id="error_description"><%=resultMessage %></span></td></tr>
					<% } %>
              			
				</tbody>
				</table>
			</form>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="questionnaire">
    			<input type="hidden" name="action" value="edit">
    			<input type="hidden" name="process" value="no">
				<input type="hidden" name="id_user" value="<%=id_user %>">
    			<input type="hidden" name="id_telgr_activation" value="<%=id_telgr_activation %>">
    			<input type="hidden" name="id_role" value="<%=role.getValue("ID_NAT_PRS_ROLE") %>">
			</form>
			<% if (Bean.isEmpty(id_user)) { %>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="action" value="show">
			</form>
			<% } else { %>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
		        <input type="hidden" name="id_user" value="<%=id_user %>">
		        <input type="hidden" name="tab" value="3">
		        <input type="hidden" name="type" value="edit">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="no">
			</form>
			<% } %>
			<% 
    		
		} else if ((action.equalsIgnoreCase("put_card") && (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt) || Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt))) ||
				(action.equalsIgnoreCase("sms_confirm") && !(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)))) { 
				
				
			wpTransactionObject trans_given = new wpTransactionObject(id_trans_given);

			wpNatPrsRoleObject role = new wpNatPrsRoleObject(trans_given.getValue("ID_NAT_PRS_ROLE"));
		%>
		
		<script>
			function validateData(){
				var formParam = new Array (
					<% if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
					new Array ('confirm_code', 'varchar2', 1)
					<% } %>
				);
				return validateFormForID(formParam, 'updateForm');
			}
		</script>
		<% if ((action.equalsIgnoreCase("sms_confirm") && !(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)))) { %>
			<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_activation", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("activation", "div_action_big") %></h1>
		<% } else { %>
			<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_activation", false) %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
		<% } %>
		<%=Bean.getCardActivationTabSheets("3") %>
		<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="id_trans_given" value="<%=id_trans_given %>">
			<input type="hidden" name="id_telgr_activation" value="<%=id_telgr_activation %>">
    		<input type="hidden" name="type" value="client">
    		<input type="hidden" name="action" value="sms_confirm">
    		<input type="hidden" name="process" value="yes">
			<input type="hidden" name="id_user" value="<%=id_user %>">
			<input type="hidden" name="id_term" value="<%=id_term %>">
			<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">

			<% if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
			<input type="hidden" name="confirm_type" value="SMS">
			<% } %>
			<table class="action_table">
			<tbody>
			  	<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="30" value="<%=role.getValue("CD_CARD1_HIDE") %>" readonly class="inputfield_finish"></td></tr>
				<tr><td><%= Bean.webposXML.getfieldTransl("client_country", false) %></td><td><input type="text" name="client_country_name" id="client_country_name" size="30" value="<%=Bean.getCountryName(role.getValue("CODE_COUNTRY")) %>" readonly class="inputfield_finish"></td></tr>
			  	<tr><td><%=Bean.webposXML.getfieldTransl("club_date_beg", false) %></td><td><input type="text" name="club_date_beg" id="club_date_beg" size="10" value="<%=role.getValue("DATE_CARD_SALE_DF") %>" readonly class="inputfield_finish_blue"></td></tr>
			  	<tr><td><%=Bean.webposXML.getfieldTransl("client_fio_nat_prs", false) %></td><td><input type="text" name="client_fio_nat_prs" id="client_fio_nat_prs" size="30" value="<%=role.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green"></td></tr>
			  	<tr><td><%=Bean.webposXML.getfieldTransl("client_phone_mobile", false) %></td><td><input type="text" name="client_phone_mobile" id="client_phone_mobile" size="30" value="<%=role.getValue("PHONE_MOBILE_HIDE") %>" readonly class="inputfield_finish_green"></td></tr>
			  	<tr><td colspan="2">&nbsp;</td></tr>
				<% if (Bean.C_NEED_SMS_CONFIRMATION.equalsIgnoreCase(resultInt)) { %>
					<% if (!Bean.isEmpty(resultMessage)) { %>
						<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
						<tr><td colspan="2">&nbsp;</td></tr>
					<% } %>
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("sms_password", true) %></td><td><input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER.length() %>"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
				        <%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "button_back", "updateFormBack", "div_action_big", "") %>
					</td>
				</tr>
				<% } %>
				<% if ((action.equalsIgnoreCase("sms_confirm") && !(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)))) { %>
				<tr>
					<td colspan="2" align="center">
				        <%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "button_back", "updateFormBack", "div_action_big", "") %>
					</td>
				</tr>				
				<tr><td colspan="2" class="left"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2" class="left"><span id="error_description"><%=resultMessage %></span></td></tr>

				<% } %>
			</tbody>
			</table>
		</form>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="client">
    			<input type="hidden" name="action" value="check_card">
    			<input type="hidden" name="process" value="yes">
    			<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
    			<input type="hidden" name="back_type" value="user">
		        <input type="hidden" name="id_user" value="<%=id_user %>">
			</form>
    <%
			} else  if (action.equalsIgnoreCase("sms_confirm") && (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) {
			
				wpChequeObject cheque = new wpChequeObject(id_telgr_activation, Bean.getChequeSaveFormat(), Bean.loginTerm);
				
			%>
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_activation", false) %>: <%=Bean.webposXML.getfieldTransl("operation_confirm", false) %><%=Bean.getHelpButton("activation", "div_action_big") %></h1>
				<%=Bean.getCardActivationTabSheets("3") %>
					<table class="table_cheque"><tbody>
						<tr><td class="centerb">
						<%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></td><td>
						<% if (Bean.isEmpty(id_user)) { %>
						<%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateFormBack", "div_main", "") %>
						<% } else { %>
						<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateFormBack", "div_main", "") %>
						<% } %>
						</td></tr>
						<%=cheque.getChequeHTML(true) %>
					</tbody></table>
			<% if (Bean.isEmpty(id_user)) { %>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="action" value="show">
			</form>
			<% } else { %>
			<form name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
		        <input type="hidden" name="id_user" value="<%=id_user %>">
		        <input type="hidden" name="tab" value="3">
		        <input type="hidden" name="type" value="edit">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="no">
			</form>
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
