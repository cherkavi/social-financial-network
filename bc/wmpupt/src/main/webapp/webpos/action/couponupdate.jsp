<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="webpos.wpTelegramObject"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_COUPON";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

type 	= Bean.isEmpty(type)?"":type;
action 	= Bean.isEmpty(action)?"":action;
process = Bean.isEmpty(process)?"":process;

String
	cd_card1					= Bean.getDecodeParam(parameters.get("cd_card1")),
	coupon						= Bean.getDecodeParam(parameters.get("coupon")),
	coupon_control_code			= Bean.getDecodeParam(parameters.get("coupon_control_code"));

cd_card1	= Bean.isEmpty(cd_card1)?"":cd_card1.replace(" ", "");

String id_term					= Bean.getCurrentTerm();

Bean.loginTerm.getTermFeature();

Bean.readWebPosMenuHTML();

String event = "";

%>


<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
	<% type = "error_term"; %>
<% } %>
<%
if (type.equalsIgnoreCase("error_term")) {
} else if (type.equalsIgnoreCase("coupon")) {

	if (process.equalsIgnoreCase("yes")) {
		String resultInt 		= Bean.C_SUCCESS_RESULT;
		String id_telgr			= Bean.getDecodeParam(parameters.get("id_telgr"));
		String resultMessage 	= "";

		if (action.equalsIgnoreCase("put")) {
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_term);
			pParam.add(cd_card1);
			pParam.add(coupon);
			pParam.add(coupon_control_code);
			
			String[] results = new String[3];
			
			results 		= Bean.executeFunction("PACK$WEBPOS_UI.coupon_put", pParam, results.length);
			resultInt 		= results[0];
			id_telgr		= results[1];
			resultMessage 	= results[2];
			
			if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
				event = "GIVE";
			} else if (Bean.C_NEED_END_INFO.equalsIgnoreCase(resultInt)) {
				event = "CONFIRM";
			} else {
				event = "PUT_ERROR";
			}
		} else if (action.equalsIgnoreCase("confirm")) {
			String
				confirm_type		= Bean.getDecodeParam(parameters.get("confirm_type")),
				confirm_code		= Bean.getDecodeParam(parameters.get("confirm_code"));
				
			// Пока нет обработчика
			confirm_type = "NONE";
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_telgr);
			pParam.add(confirm_type);
			pParam.add(confirm_code);
			pParam.add("");
			
			String[] results = new String[4];
			
			results 					= Bean.executeFunction("PACK$WEBPOS_UI.oper_confirm", pParam, results.length);
			resultInt 					= results[0];
			String phone_mobile	 		= results[1];
			String can_send_pin_in_sms  = results[2];
	 		resultMessage 				= results[3];
			
			if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
				event = "GIVE";
			} else {
				event = "CONFIRM_ERROR";
			}
		} else {
			resultInt 		= Bean.C_ERROR_RESULT;
			resultMessage 	= Bean.form_messageXML.getfieldTransl("unknown_action", false) + " (action = " + action + ")";
			event 			= "PUT_ERROR";
		}
	    	
		if ("PUT_ERROR".equalsIgnoreCase(event)) {
	    %>
		<script>
		function validateCheckCoupon(){
			var returnValue = null;
			var formParam = new Array (
				new Array ('cd_card1', 'card', 1),
				new Array ('coupon', 'varchar2', 1),
				new Array ('coupon_control_code', 'varchar2', 1)
			);
			returnValue = validateForm(formParam, 'updateForm3');
			return returnValue;
		}
		card_mask2("cd_card1");
		</script>
		<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_coupon", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("coupon", "div_action_big") %></h1>
		<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="coupon">
			<input type="hidden" name="action" value="put">
			<input type="hidden" name="process" value="yes">
			<input type="hidden" name="id_term" value="<%=id_term %>">
			<table class="action_table">
				<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="20" value="<%=cd_card1 %>"  class="inputfield"></td></tr>
            	<tr><td><%=Bean.webposXML.getfieldTransl("coupon", true) %></td><td><input type="text" name="coupon" id="coupon" size="20" value="<%=coupon %>"  class="inputfield"></td></tr>
				<tr><td><%=Bean.webposXML.getfieldTransl("coupon_control_code", true) %></td><td><input type="text" name="coupon_control_code" id="coupon_control_code" size="20" value="<%=coupon_control_code %>"  class="inputfield"></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2" class="left"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2" class="left"><span id="error_description"><%=resultMessage %></span></td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2" class="center"><%=Bean.getSubmitButtonAjax("action/couponupdate.jsp", "check", "updateForm3", "div_action_big", "validateCheckCoupon") %></td></tr>
				<tr><td colspan="2" class="left">
				<div id=div_hints><%=Bean.getWEBPosOnlyTestCards() %>
				<br>
				<%=Bean.getWEBPosOnlyTestCoupon() %>
				</div>
				</td></tr>
			</table>
		</form>
	    <%
		} else if ("CONFIRM".equalsIgnoreCase(event) || 
				"CONFIRM_ERROR".equalsIgnoreCase(event)) {
			String message 				= "";
			String get_share_account 	= "";
			String put_share_account 	= "";
			String get_point 			= "";
			String put_point 			= "";
			String currency             = "";
		%> 
		<% if ("CONFIRM".equalsIgnoreCase(event)) { 
			wpTelegramObject telgr = new wpTelegramObject(id_telgr);
			message 			= telgr.getValue("MESSAGE_TELGR");
			get_share_account 	= telgr.getValue("SUM_GET_SHARE_TOTAL_FRMT");
			put_share_account 	= telgr.getValue("SUM_PUT_SHARE_TOTAL_FRMT");
			get_point 			= telgr.getValue("SUM_GET_POINT_TOTAL_FRMT");
			put_point 			= telgr.getValue("SUM_PUT_POINT_TOTAL_FRMT");
			currency 			= telgr.getValue("SCD_CURRENCY");
			%>
		<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_coupon", false) %>: <%=Bean.webposXML.getfieldTransl("operation_success", false) %><%=Bean.getHelpButton("coupon", "div_action_big") %></h1>
		<% } else { %>
		<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_coupon", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("coupon", "div_action_big") %></h1>
		<% } %>
		<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
			    <input type="hidden" name="type" value="coupon">
				<input type="hidden" name="action" value="confirm">
			    <input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
			    <input type="hidden" name="process" value="yes">
			    <input type="hidden" name="id_term" value="<%=id_term %>">
			    <input type="hidden" name="id_telgr" value="<%=id_telgr %>">
				<table class="action_table">
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("card", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly class="inputfield_finish_blue"></td></tr>
              			<tr><td><%=Bean.webposXML.getfieldTransl("coupon", false) %></td><td><input type="text" name="coupon" id="coupon" size="20" value="<%=coupon %>" readonly class="inputfield_finish_green"></td></tr>
			  			<tr><td colspan="2">&nbsp;</td></tr>
						<% if ("CONFIRM".equalsIgnoreCase(event)) { %>
						<% if (!Bean.isEmptyAmount(get_share_account)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_get_share_account", false) %></td><td><input type="text" name="get_share_account" id="get_share_account" size="20" value="<%=get_share_account %> <%=currency %>" readonly class="inputfield_finish_green"></td></tr>
						<% } %>
						<% if (!Bean.isEmptyAmount(put_share_account)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_add_share_account", false) %></td><td><input type="text" name="put_share_account" id="put_share_account" size="20" value="<%=put_share_account %> <%=currency %>" readonly class="inputfield_finish_green"></td></tr>
						<% } %>
						<% if (!Bean.isEmptyAmount(get_point)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_recepient_get_point", false) %></td><td><input type="text" name="get_point" id="get_point" size="20" value="<%=get_point %> <%=Bean.webposXML.getfieldTransl("point_currency_name", false) %>" readonly class="inputfield_finish_green"></td></tr>
						<% } %>
						<% if (!Bean.isEmptyAmount(put_point)) { %>
						<tr><td><%=Bean.webposXML.getfieldTransl("cheque_add_point", false) %></td><td><input type="text" name="put_point" id="put_point" size="20" value="<%=put_point %> <%=Bean.webposXML.getfieldTransl("point_currency_name", false) %>" readonly class="inputfield_finish_green"></td></tr>
						<% } %>
						<% if (!Bean.isEmpty(message)) { %>
			  			<tr><td colspan="2" class="left"><span id="succes_description"><%=message %></span></td></tr>
						<% } %>
			  			<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2" class="center">
			  				<%=Bean.getSubmitButtonAjax("action/couponupdate.jsp", "put", "updateForm3", "div_action_big", "validateCheckCoupon") %>
				        	<%=Bean.getSubmitButtonAjax("action/coupon.jsp", "button_back", "updateForm2", "div_main") %>
						</td></tr>
						<% } else { %>
						<tr><td colspan="2" class="left"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
						<tr><td colspan="2" class="left"><span id="error_description"><%=resultMessage %></span></td></tr>
			  			<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2" class="center">
				        	<%=Bean.getSubmitButtonAjax("action/coupon.jsp", "button_back", "updateForm2", "div_main") %>
						</td></tr>
						<% } %>
					</table>
				</form>
			<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
				<input type="hidden" name="coupon" value="<%=coupon %>">
			</form>
	    <%
		} else if ("GIVE".equalsIgnoreCase(event)) {
			wpChequeObject cheque = new wpChequeObject(id_telgr, Bean.getChequeSaveFormat(), Bean.loginTerm);
		%>
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_replenish", false) %>: <%=Bean.webposXML.getfieldTransl("operation_confirm", false) %><%=Bean.getHelpButton("pay", "div_action_big") %></h1>
	 					<table class="table_cheque"><tbody>
						<tr><td class="centerb">
						<%= cheque.getChequeAllButtonsShort(Bean.hasStornoMenuPermission()) %></td><td>
						<%=Bean.getSubmitButtonAjax("action/coupon.jsp", "button_back", "updateForm", "div_main") %><br><br>
						</td></tr>
						<%=cheque.getChequeHTML(true) %>
						</tbody></table>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				</form>
		<% }
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
