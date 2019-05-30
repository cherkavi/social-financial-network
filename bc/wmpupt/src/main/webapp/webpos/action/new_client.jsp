<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpUserObject"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBPOS_SERVICE_CARD_ACTIVATION";
String tagTransaction = "_TRANSACTION";
String tagTransactionFind = "_FIND_TRANS";
String tagRoleState = "_ROLE_STATE";
String tagDataType = "_DATA_TYPE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String id_term = Bean.getCurrentTerm();
Bean.loginTerm.getTermAdditionFeature();

String cd_card1			= Bean.getDecodeParam(parameters.get("cd_card1"));
cd_card1				= !(cd_card1 == null)?cd_card1.replace(" ", ""):"";

String oper = Bean.getDecodeParam(parameters.get("oper"));

String l_transaction_page = Bean.getDecodeParam(parameters.get("transaction_page"));
Bean.pageCheck(pageFormName + tagTransaction, l_transaction_page);
String l_transaction_page_beg = Bean.getFirstRowNumber(pageFormName + tagTransaction);
String l_transaction_page_end = Bean.getLastRowNumber(pageFormName + tagTransaction);

String transaction_find 	= Bean.getDecodeParam(parameters.get("transaction_find"));
transaction_find 			= Bean.checkFindString(pageFormName + tagTransactionFind, transaction_find, l_transaction_page);

String role_state 	= Bean.getDecodeParam(parameters.get("role_state"));
role_state 			= Bean.checkFindString(pageFormName + tagRoleState, role_state, l_transaction_page);

String data_type 	= Bean.getDecodeParam(parameters.get("data_type"));
if (!Bean.isEmpty(data_type)) {
	Bean.setFormParam(pageFormName+"_DATA_TYPE", data_type, "NOPAY");
}
data_type = Bean.getFormParam(pageFormName+"_DATA_TYPE", "NOPAY");

String phone_mobile = Bean.getDecodeParamPrepare(parameters.get("phone_mobile"));

String
	date_beg		= Bean.getDecodeParam(parameters.get("date_beg")),
	date_end		= Bean.getDecodeParam(parameters.get("date_end")),
	cd_card1_find	= Bean.getDecodeParam(parameters.get("cd_card1_find"));

	if (!(date_beg == null)) {
		Bean.setFormParam(pageFormName+"_DATE_FROM", date_beg, Bean.getSysDate());
		Bean.isQuestionairePeriodSet = true;
	}
	if (!(date_end == null)) {
		Bean.setFormParam(pageFormName+"_DATE_TO", date_end, Bean.getSysDate());
		Bean.isQuestionairePeriodSet = true;
	}
	if (!(cd_card1_find == null)) {
		Bean.setFormParam(pageFormName+"_CARD_FIND", cd_card1_find, "");
		Bean.isQuestionairePeriodSet = true;
	}
	
	String operFrom = Bean.getFormParam(pageFormName+"_DATE_FROM", Bean.getSysDate());
	String operTo = Bean.getFormParam(pageFormName+"_DATE_TO", Bean.getSysDate());
	String titleFromTo = Bean.webposXML.getfieldTransl("title_card_given_from_to", false);
	if (!Bean.isEmpty(titleFromTo)) {
		titleFromTo = titleFromTo.replace("$from$", (Bean.isEmpty(operFrom)?"...":operFrom)).replace("$to$", (Bean.isEmpty(operTo)?"...":operTo));
	}
	String cardFind = Bean.getFormParam(pageFormName+"_CARD_FIND", "");


%>
<body>
	<%=Bean.getWebPosMenuHTML(pageFormName) %>
<%



String savedTab = Bean.tabsHmGetValue(pageFormName);
String inputTab = Bean.getDecodeParam(parameters.get("tab"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab == null || "".equalsIgnoreCase(tab)) {
	if (Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_REGISTRATION")) {
		tab = "1"; // По просьбе Кудинского А. переход всегда на закладку "Выдача карты"
	} else {
		tab = Bean.tabsHmGetValue(pageFormName);
	}
}

boolean tab1HasPermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_REGISTRATION");
boolean tab2HasPermission = Bean.hasReadMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_QUESTIONNAIRE");
boolean tab3HasPermission = Bean.hasWriteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_ACTIVATION");
int tabCount = (tab1HasPermission?1:0)+(tab2HasPermission?1:0)+(tab3HasPermission?1:0);

if (!tab1HasPermission) {
	tab = "1".equalsIgnoreCase(tab)?"2":tab;
}
if (!tab2HasPermission) {
	tab = "2".equalsIgnoreCase(tab)?"3":tab;
}
if (!tab3HasPermission) {
	tab = "3".equalsIgnoreCase(tab)?"4":tab;
}
Bean.tabsHmSetValue(pageFormName, tab);

if (oper == null || "".equalsIgnoreCase(oper)) {
	if ("2".equalsIgnoreCase(tab)) {
		if (Bean.isQuestionairePeriodSet) {
			oper = "show";
		} else {
			oper = "param";
		}
	}
}
%>
<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_WRITE_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
<% } else { %>

<%if (!"4".equalsIgnoreCase(tab)) { %>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td><div id="div_action_big">

				
				<%if ("1".equalsIgnoreCase(tab)) {%>
				<h1><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %><%=Bean.getHelpButton("registration", "div_action_big") %></h1>
				<% } else if ("2".equalsIgnoreCase(tab)) {%>
				<h1><%=Bean.webposXML.getfieldTransl("title_questionnaire", false) %><%=Bean.getHelpButton("questionnaire", "div_action_big") %></h1>
				<% } else if ("3".equalsIgnoreCase(tab)) {%>
				<h1><%=Bean.webposXML.getfieldTransl("title_activation", false) %><%=Bean.getHelpButton("activation", "div_action_big") %></h1>
				<% } %>
				<% if (tabCount > 1) { %>
				<%=Bean.getCardActivationTabSheets(tab) %>
				<% } %>

<% if ("1".equalsIgnoreCase(tab)) { %>
		<%
			String term_code_country = Bean.loginTerm.getValue("ADR_CODE_COUNTRY_SERVICE_PLACE", "TERM");
			if (term_code_country == null) {
				term_code_country = "";
			}
			
			String id_user = Bean.getDecodeParam(parameters.get("id_user")); 
		%>
		<script>
		<% if ("NOPAY".equalsIgnoreCase(data_type)) { %>
		function validatePutCard(){
			var returnValue = null;
			var formParam = new Array (
				new Array ('cd_card1', 'card', 1),
				new Array ('client_country', 'varchar2', 1)		
			);
			returnValue = validateForm(formParam, 'updateForm1');
			return returnValue;
		}
		card_mask2("cd_card1");
		<% } else { %>
		function validatePutCard(){
			var returnValue = null;
			var formParam = new Array (
				new Array ('phone_mobile', 'varchar2', 1)
			);
			returnValue = validateForm(formParam, 'updateForm1');
			return returnValue;
		}
		phone_mask_empty("phone_mobile","<%=term_code_country%>");
		<% } %>
		</script>
		
		<input type="radio" id="data_type_nopay" name="data_type" value="NOPAY" <%if ("NOPAY".equalsIgnoreCase(data_type)) { %>checked<% } %> onclick="ajaxpage('action/new_client.jsp?tab=1&data_type=NOPAY&id_user=<%=id_user %>', 'div_main')"><label class="checbox_label" for="data_type_nopay">Вступительный взнос <font color="red">не оплачен</font></label><br>
		<input type="radio" id="data_type_paid" name="data_type" value="PAID" <%if ("PAID".equalsIgnoreCase(data_type)) { %>checked<% } %> onclick="ajaxpage('action/new_client.jsp?tab=1&data_type=PAID&id_user=<%=id_user %>', 'div_main')"><label class="checbox_label" for="data_type_paid">Вступительный взнос <font color="green">оплачен</font> внешними платежными системами</label><br><br>
		
		<% 	wpUserObject user = null;
			if (!Bean.isEmpty(id_user)) {
				user = new wpUserObject(id_user);
			}
		%>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="client">
					<% if ("NOPAY".equalsIgnoreCase(data_type)) { %>
			        <input type="hidden" name="action" value="check_card">
					<% } else { %>
			        <input type="hidden" name="action" value="check_phone_mobile">
					<% } %>
			        <input type="hidden" name="process" value="yes">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="data_type" value="<%=data_type %>">
					<% if (!"".equalsIgnoreCase(term_code_country)) { %>
					<input type="hidden" name="client_country" value="<%=term_code_country %>">
					<% } %>
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<input type="hidden" name="id_user" value="<%=id_user %>">
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
				  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="30" value="<%=cd_card1 %>"  class="inputfield"></td></tr>
							<% if ("".equalsIgnoreCase(term_code_country)) { %>
							<tr><td><%= Bean.webposXML.getfieldTransl("client_country", true) %></td><td><select name="client_country" id="client_country" class="inputfield"><%= Bean.getCountryOptions("", true) %></select></td></tr>
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
				  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("client_phone_mobile", true) %></td><td><input type="text" name="phone_mobile" id="phone_mobile" size="30" value="<% if (!Bean.isEmpty(id_user)) { %><%= user.getValue("PHONE_MOBILE") %><% } else {%><%=phone_mobile %><%} %>"  class="inputfield"></td></tr>
							<tr><td colspan="2">&nbsp;</td></tr>
							<tr><td colspan="2" class="center">
								<%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "button_further", "updateForm", "div_action_big", "validatePutCard") %> 
							<% if (!Bean.isEmpty(id_user)) { %>
									<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateFormBack", "div_main") %>
							<% } %>
 							</td></tr>
						<% } %>
						<% if ("NOPAY".equalsIgnoreCase(data_type)) { %>
						<tr><td colspan="2" class="left">
						<div id=div_hints>
						<%=Bean.getWEBPosOnlyTestCards() %>
						</div>
						<% } %>
						</td></tr>
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
<% } %>

<% if ("2".equalsIgnoreCase(tab)) { %>
	<% if ("param".equalsIgnoreCase(oper)) { %>
	<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="type" value="operation">
	    <input type="hidden" name="oper" value="show">
		<input type="hidden" name="process" value="yes">
		<input type="hidden" name="div_name" value="div_action_big">
		<input type="hidden" name="transaction_page" value="1">
		<input type="hidden" name="tab" value="2">
		<table class="action_table">
			<tr><td colspan="2"><span style="color:green; font-weight: bold;"><%=Bean.webposXML.getfieldTransl("title_card_given_find", false) %></span></td></tr>
			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("title_card_given_from", false) %></td><td><%=Bean.getCalendarInputField("date_beg", operFrom, "10") %></td></tr>
            <tr><td><%=Bean.webposXML.getfieldTransl("title_card_given_to", false) %></td><td><%=Bean.getCalendarInputField("date_end", operTo, "10") %></td></tr>
			<tr><td><%=Bean.webposXML.getfieldTransl("title_card_given_cd_card1", false) %></td><td><input type="text" name="cd_card1_find" id="cd_card1_find" size="30" value="<%=cardFind %>"  class="inputfield" maxlength="20"></td></tr>
			<tr>
				<td><%=Bean.webposXML.getfieldTransl("nat_prs_role_state", false) %></td>
				<td><%=Bean.getSelectBeginHTML("role_state", Bean.webposXML.getfieldTransl("nat_prs_role_state", false)) %>
				 	<%=Bean.getSelectOptionHTML(role_state, "", "") %>
				 	<%=Bean.getSelectOptionHTML(role_state, "GIVEN", Bean.webposXML.getfieldTransl("nat_prs_role_state_given", false)) %>
				 	<%=Bean.getSelectOptionHTML(role_state, "ACTIVATED", Bean.webposXML.getfieldTransl("nat_prs_role_state_activated", false)) %>
				 	<%=Bean.getSelectOptionHTML(role_state, "QUESTIONED_CHECKED", Bean.webposXML.getfieldTransl("nat_prs_role_state_questioned_checked", false)) %>
				 	<%=Bean.getSelectOptionHTML(role_state, "QUESTIONED_NOT_CHECKED", Bean.webposXML.getfieldTransl("nat_prs_role_state_questioned_not_checked", false)) %>
				 	<%=Bean.getSelectOptionHTML(role_state, "CANCEL", Bean.webposXML.getfieldTransl("nat_prs_role_state_cancel", false)) %>
				 	<%=Bean.getSelectOptionHTML(role_state, "ERROR", Bean.webposXML.getfieldTransl("nat_prs_role_state_error", false)) %>
				<%=Bean.getSelectOnChangeEndHTML() %></td>
			</tr>
			<tr><td colspan="2" class="center">&nbsp;</td></tr>
			<tr><td colspan="2" class="center">
				<%=Bean.getSubmitButtonAjax("action/new_client.jsp", "find", "updateForm1", "div_main") %>
				<% if (Bean.isQuestionairePeriodSet) { %> 
				<%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateFormBack", "div_main") %>
				<% } %>
			</td></tr>
		</table>
	</form>
	<form class="hiddenForm" name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="type" value="operation">
	    <input type="hidden" name="oper" value="show">
		<input type="hidden" name="process" value="yes">
		<input type="hidden" name="div_name" value="div_action_big">
		<input type="hidden" name="tab" value="2">
	</form>

	<%= Bean.getCalendarScript("date_beg", false) %>
	<%= Bean.getCalendarScript("date_end", false) %>

	<% } else if ("show".equalsIgnoreCase(oper)) {
		boolean periodSet = (!Bean.isEmpty(operFrom) || !Bean.isEmpty(operTo) );
		boolean cardSet = (!Bean.isEmpty(cardFind));
		
		StringBuilder html = new StringBuilder();
		html.append(Bean.loginTerm.getCardSaleListHTML(Bean.getLoginUserId(), transaction_find, cardFind, role_state, operFrom, operTo, l_transaction_page_beg, l_transaction_page_end));
	%>
		<table <%=Bean.getTableBottomFilter() %>>
			<tr>
				<td colspan=10>
					<span style="font-size: 12px; color:green;">

						<% if (periodSet || cardSet) { %>
						<span style="font-size: 14px; color:red;font-weight:bold;"><%=Bean.webposXML.getfieldTransl("title_operation_filter_set", false) %></span><br>
						<% } %>
						<% if (periodSet) { %><%=titleFromTo %><% } %>
						<% if (periodSet && cardSet) { %><br><% } %>
						<% if (cardSet) { %>
						<%=Bean.webposXML.getfieldTransl("title_questionnaire_cd_card", false) %>:&nbsp;<%=cardFind %>
						<% } %>
						<% if (!Bean.isEmpty(role_state)) { 
							String role_state_name = "";
							if ("GIVEN".equalsIgnoreCase(role_state)) {
								role_state_name = Bean.webposXML.getfieldTransl("nat_prs_role_state_given", false);
							} else if ("ACTIVATED".equalsIgnoreCase(role_state)) {
								role_state_name = Bean.webposXML.getfieldTransl("nat_prs_role_state_activated", false);
							} else if ("QUESTIONED_CHECKED".equalsIgnoreCase(role_state)) {
								role_state_name = Bean.webposXML.getfieldTransl("nat_prs_role_state_questioned_checked", false);
							} else if ("QUESTIONED_NOT_CHECKED".equalsIgnoreCase(role_state)) {
								role_state_name = Bean.webposXML.getfieldTransl("nat_prs_role_state_questioned_not_checked", false);
							} else if ("CANCEL".equalsIgnoreCase(role_state)) {
								role_state_name = Bean.webposXML.getfieldTransl("nat_prs_role_state_cancel", false);
							} else if ("ERROR".equalsIgnoreCase(role_state)) {
								role_state_name = Bean.webposXML.getfieldTransl("nat_prs_role_state_error", false);
							}
						%>
						<br><%=Bean.webposXML.getfieldTransl("nat_prs_role_state", false) %>:&nbsp;<%=role_state_name %>
						<% } %>
						<% if (!periodSet && !cardSet) { %>
						<font color="red"><%=Bean.webposXML.getfieldTransl("title_questionnaire_find_all_period", false) %></font>
						<% } %>
					</span>
					<span style="float:right;"><button id="change_button" onclick="ajaxpage('action/new_client.jsp?' +  mySubmitForm('updateForm2'),'div_main');" type="button">Изменить</button></span>
				</td>
			</tr>
	  	<tr>
		<%= Bean.getFindHTML("transaction_find", transaction_find, "action/new_client.jsp?tab=2&action=show&transaction_page=1&", "div_main") %>
		
	    <!-- Вывод страниц -->
		<%= Bean.getPagesHTML(pageFormName + tagTransaction, "action/new_client.jsp?tab=2&action=show&", "transaction_page", Bean.loginTerm.getCardSalesCount(), "div_main") %>
	  	</tr>
	</table>
	<%= html.toString() %>

		<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="oper" value="param">
			<input type="hidden" name="tab" value="2">
		</form>
	<% } else { %> 
		<%= Bean.getUnknownActionText(oper) %><% 
	} %>
<% } %>
<% if ("3".equalsIgnoreCase(tab)) { %>
	<script>
	function validatePutCard(){
		var formParam = new Array (
			new Array ('cd_card1', 'card', 1)
		);
		return validateForm(formParam, 'updateForm1');
	}
	card_mask2("cd_card1");
	</script>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="client">
			        <input type="hidden" name="action" value="check_card">
			        <input type="hidden" name="process" value="yes">
					<input type="hidden" name="tab" value="3">
					<input type="hidden" name="id_term" value="<%=id_term %>">
					<table class="action_table">
			  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", true) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="30" value="<%=cd_card1 %>"  class="inputfield"></td></tr>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2" class="center"><%=Bean.getSubmitButtonAjax("action/new_client_activation.jsp", "button_further", "updateForm", "div_action_big", "validatePutCard") %> </td></tr>
						<tr><td colspan="2" class="center">&nbsp;</td></tr>
						<tr><td colspan="2" class="left">
						<div id="div_hints">
							<i><b><%=Bean.webposXML.getfieldTransl("title_note", false) %>
							</b><br><%=Bean.webposXML.getfieldTransl("title_note_new_client", false) %></i>
							<%=Bean.getWEBPosOnlyTestCards() %>
						</div>
						</td></tr>
					</table>
				</form>
<% } %>
				</div>
			</td>
		</tr>
	</table>
<% } else { %>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td>
				<div id="div_action_big">
				<h1><%=Bean.webposXML.getfieldTransl("title_card_issue", false) %><%=Bean.getHelpButton("registration", "div_action_big") %></h1>
					<table class="action_table">
						<tr><td align="center" style="padding: 10px;"><font style="font-size: 22px; color:red; font-weight: bold;"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></font></td></tr>
						<tr><td align="center">Доступ запрещен</td></tr>
						<tr><td align="center">&nbsp;</td></tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
	<% } %>
<% } %>
</body>
</html>