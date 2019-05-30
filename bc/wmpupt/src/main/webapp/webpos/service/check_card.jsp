<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="webpos.wpNatPrsRoleObject"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_CHECK_CARD";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String action	= Bean.getDecodeParam(parameters.get("check_card")); 

action 	= Bean.isEmpty(action)?"get_activation_code":action;


String
	cd_card1		= Bean.getDecodeParam(parameters.get("cd_card1")),
	id_term			= Bean.getCurrentTerm(),
	activation_code	= Bean.getDecodeParam(parameters.get("activation_code")),
	back_type		= Bean.getDecodeParam(parameters.get("back_type"));

if (!Bean.isEmpty(cd_card1)) {
	cd_card1 = cd_card1.replace(" ", "");
} else {
	cd_card1		= Bean.getDecodeParam(parameters.get("from_card"));
	if (!Bean.isEmpty(cd_card1)) {
		cd_card1 = cd_card1.replace(" ", "");
	}
}

String pBackForm = "";
if ("pay".equalsIgnoreCase(back_type)) {
	pBackForm = "action/pay.jsp";
} else if ("replenish".equalsIgnoreCase(back_type)) {
	pBackForm = "action/replenish.jsp";
} else if ("transfer".equalsIgnoreCase(back_type)) {
	pBackForm = "action/transfer.jsp";
} else if ("storno".equalsIgnoreCase(back_type)) {
	pBackForm = "action/storno.jsp";
} else if ("oper".equalsIgnoreCase(back_type)) {
	pBackForm = "report/operation.jsp";
} else {
	pBackForm = "action/pay.jsp";
}

Bean.loginTerm.getFeature();

Bean.readWebPosMenuHTML();

%>

<% if (!Bean.hasTerminalPermission(Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
	<% action = "error_term"; %>
<% } %>
<%
if (action.equalsIgnoreCase("error_term")) {
} else { 
	if (action.equalsIgnoreCase("get_activation_code")) { 
		
		if (cd_card1 == null || "".equalsIgnoreCase(cd_card1)) {%>
		<script>
		function validateData(){
			var formParam = new Array (
				new Array ('cd_card1', 'card', 1)
			);
			return validateFormForID(formParam, 'updateForm3');
		}
		card_mask2("cd_card1");
		</script>
			<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_check_card", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("check_card", "div_action_big") %></h1>
			<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
		        <input type="hidden" name="check_card" value="get_activation_code">
		        <input type="hidden" name="id_term" value="<%=id_term %>">
		        <input type="hidden" name="back_type" value="<%=back_type %>">
		        <input type="hidden" name="from_card" value="<%=cd_card1 %>">
				<table class="action_table">
		  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="20" value="<%=cd_card1 %>"  class="inputfield"></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
					<tr><td colspan="2"><span id="error_description"><%=Bean.webposXML.getfieldTransl("title_card_not_enter", false) %></span></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td colspan="2" class="center">
					        <%=Bean.getSubmitButtonAjax("service/check_card.jsp", "confirm", "updateForm3", "div_action_big", "validateData") %>
					        <%=Bean.getSubmitButtonAjax(pBackForm, "button_back", "updateForm3", "div_main") %>
						</td>
					</tr>
					<tr><td colspan="2" class="left">
					<div id=div_hints><%=Bean.getWEBPosOnlyTestCards() %>
					</div>
					</td></tr>
				</table>
			</form>

			
		<%} else {
			
			if (Bean.isLastCheckCard(cd_card1)) {
				action = "check";
			} else {
	%>
	<script>
	function validateData(){
		var formParam = new Array (
			new Array ('activation_code', 'varchar2', 1)
		);
		return validateFormForID(formParam, 'updateForm3');
	}
	</script>
	<h1><%=Bean.webposXML.getfieldTransl("title_check_card", false) %>: <%=Bean.webposXML.getfieldTransl("oper_confirmation", false) %><%=Bean.getHelpButton("check_card", "div_action_big") %></h1>
	<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
		        <input type="hidden" name="check_card" value="check">
		        <input type="hidden" name="id_term" value="<%=id_term %>">
		        <input type="hidden" name="back_type" value="<%=back_type %>">
		        <input type="hidden" name="from_card" value="<%=cd_card1 %>">
		        <input type="hidden" name="cd_card1" id="cd_card1" value="<%=cd_card1 %>">
				<table class="action_table">
		  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1_hide" id="cd_card1_hide" size="20" value="<%=Bean.hideCdCard1(cd_card1) %>" readonly class="inputfield_finish_blue"></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_activation_code", false) %></td></tr>
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("title_activation_code", true) %></td><td><input class="inputfield" id="activation_code" name="activation_code" placeholder="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER.length() %>"></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td colspan="2" class="center">
					        <%=Bean.getSubmitButtonAjax("service/check_card.jsp", "confirm", "updateForm3", "div_action_big", "validateData") %>
					        <%=Bean.getSubmitButtonAjax(pBackForm, "button_back", "updateForm3", "div_main") %>
						</td>
					</tr>
					<tr><td colspan="2" class="left">
					<div id=div_hints><%=Bean.getWEBPosOnlyTestCards() %>
					</div>
					</td></tr>
				</table>
			</form>
    <%		}
		}
	}
	
	if (action.equalsIgnoreCase("get_activation_code") || action.equalsIgnoreCase("check")) {
		if (action.equalsIgnoreCase("check")) {

			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_term);
			pParam.add(cd_card1);
			pParam.add(activation_code);
			
			String[] results = new String[5];
			
			results 						= Bean.executeFunction("PACK$WEBPOS_UI.check_card", pParam, results.length);
			String resultInt 				= results[0];
			String card_serial_number		= results[1];
			String card_id_issuer			= results[2];
			String card_id_payment_system	= results[3];
			String resultMessage 			= results[4];
	    	
	    	if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) {
	    %>
		<script>
	function validateData(){
		var formParam = new Array (
			new Array ('activation_code', 'varchar2', 1)
		);
		return validateFormForID(formParam, 'updateForm3');
	}
	card_mask2("cd_card1");
	</script>
	<h1 class="error"><%=Bean.webposXML.getfieldTransl("title_check_card", false) %>: <%=Bean.webposXML.getfieldTransl("operation_error", false) %><%=Bean.getHelpButton("check_card", "div_action_big") %></h1>
	<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
		        <input type="hidden" name="check_card" value="check">
		        <input type="hidden" name="id_term" value="<%=id_term %>">
		        <input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
		        <input type="hidden" name="back_type" value="<%=back_type %>">
		        <input type="hidden" name="from_card" value="<%=cd_card1 %>">
				<table class="action_table">
		  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="20" value="<%=cd_card1 %>" readonly class="inputfield-ro"></td></tr>
					<tr><td colspan="2"><%=Bean.webposXML.getfieldTransl("title_need_activation_code", false) %></td></tr>
					<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("title_activation_code", true) %></td><td><input class="inputfield" id="activation_code" name="activation_code" placeholder="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_ACTIVATION_CODE_PLACEHOLDER.length() %>"></td></tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
					<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
					<tr>
						<td colspan="2" class="center">
					        <%=Bean.getSubmitButtonAjax("service/check_card.jsp", "confirm", "updateForm3", "div_action_big", "validateData") %>
					        <%=Bean.getSubmitButtonAjax(pBackForm, "button_back", "updateForm3", "div_main") %>
						</td>
					</tr>
					<tr><td colspan="2" class="left">
					<div id=div_hints><%=Bean.getWEBPosOnlyTestCards() %>
					</div>
					</td></tr>
				</table>
			</form>
	    <%
			} else {
				
				wpClubCardObject card = new wpClubCardObject(card_serial_number, card_id_issuer, card_id_payment_system);
				Bean.setLastCheckCard(cd_card1);
			%>
				<h1 class="confirm"><%=Bean.webposXML.getfieldTransl("title_check_card", false) %>: <%=Bean.webposXML.getfieldTransl("operation_confirm", false) %><%=Bean.getHelpButton("check_card", "div_action_big") %></h1>
				<form name="updateForm3" id="updateForm3" accept-charset="UTF-8" method="POST">
		        	<input type="hidden" name="id_term" value="<%=id_term %>">
		        	<input type="hidden" name="cd_card1" value="<%=cd_card1 %>">
		        	<input type="hidden" name="back_type" value="<%=back_type %>">
	 					<table class="action_table"><tbody>
						<tr>
							<td style="text-align: center; margin: 0 auto;" colspan="2" class="centerb">
								<%= card.getCardParamAllButtons() %>
							    <%=Bean.getSubmitButtonAjax(pBackForm, "button_back", "updateForm3", "div_main") %>
							</td>
						</tr>
						<%=card.getWEBClientCardParamHTML(Bean.loginTerm) %>
						</tbody></table>
				</form>
			<% }
		}
    } else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
	}
}
%>


</body>
</html>
