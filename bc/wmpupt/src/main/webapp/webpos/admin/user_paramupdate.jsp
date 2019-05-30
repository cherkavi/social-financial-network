<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpUserObject"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_USER_PARAM";

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

type 	= Bean.isEmpty(type)?"":type;
action 	= Bean.isEmpty(action)?"":action;
process = Bean.isEmpty(process)?"":process;

if (type.equalsIgnoreCase("param")) {
	if (process.equalsIgnoreCase("yes")) {

		if (action.equalsIgnoreCase("online")) {
			
			String 
				id_theme				= Bean.getDecodeParam(parameters.get("id_theme")),
				cheque_format		    = Bean.getDecodeParam(parameters.get("cheque_format"));
			
			Bean.setCurrentThemeFolder(Bean.isEmpty(id_theme)?Bean.getCurrentThemeFolder():id_theme);
			Bean.setChequeSaveFormat(Bean.isEmpty(cheque_format)?Bean.getChequeSaveFormat():cheque_format);
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add("webpos_theme");
			pParam.add(id_theme);
			
			String[] results = new String[2];
			
			results 						= Bean.executeFunction("fnc_user_set_ui_param", pParam, results.length);
			String resultInt 				= results[0];
	 		String resultMessage 			= results[1];
			
			%>
			<script type="text/javascript">
				window.location = 'main.jsp';
 			</script>
			<%
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("card")) {
	if (process.equalsIgnoreCase("no")) {

		Bean.loginTerm.getTermAdditionFeature();

		String kind1Disable = " disabled=\"disabled\" ";
		String kind2Disable = " disabled=\"disabled\" ";
		String kind3Disable = " disabled=\"disabled\" ";
		int enable1Count = 0;
		if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_PAY_CASH")) {
			if (Bean.isMenuElementEnable("WEBPOS_SERVICE_CARD_ACTIVATION_PAY_CASH")) {
				kind1Disable = "";
				enable1Count ++;
			}
		}
		if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_PAY_BANK_CARD")) {
			if (Bean.isMenuElementEnable("WEBPOS_SERVICE_CARD_ACTIVATION_PAY_BANK_CARD")) {
				kind2Disable = "";
				enable1Count ++;
			}
		}
		if (Bean.hasExecuteMenuPermission("WEBPOS_SERVICE_CARD_ACTIVATION_MAKE_INVOICE")) {
			if (Bean.isMenuElementEnable("WEBPOS_SERVICE_CARD_ACTIVATION_MAKE_INVOICE")) {
				kind3Disable = "";
				enable1Count ++;
			}
		}
		if (enable1Count==1) {
			kind1Disable 		= " CHECKED ";
			kind2Disable 		= " CHECKED ";
			kind3Disable 		= " CHECKED ";
		}
		
		if (action.equalsIgnoreCase("give1")) {

			String id_user = Bean.getDecodeParam(parameters.get("id_user"));
		
			wpUserObject user = new wpUserObject(id_user);
		%>
		<script>
		
			function validateParam(){
				var returnValue = null;
				var formParam = new Array (
					new Array ('cd_user_status', 'varchar2', 1),
					new Array ('id_user_role', 'varchar2', 1),
					new Array ('id_user_term', 'varchar2', 1),
					new Array ('cd_user_access_type', 'varchar2', 1)					
				);
				returnValue = validateFormForID(formParam, 'updateForm');
				return returnValue;
			}
			</script>
		<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="edit">
			        <input type="hidden" name="process" value="no">
			        <input type="hidden" name="action" value="update">
			        <input type="hidden" name="tab" value="3">
			        <input type="hidden" name="id_user" value="<%=user.getValue("ID_USER") %>">
					<table class="action_table" style="font-family: arial; font-size:12px;">
						<tr><td colspan="4" align="center"><span style="color:blue; font-weight: bold; font-family: arial; font-size:16px;"><%=Bean.webposXML.getfieldTransl("title_update_user", false) %></span></td></tr>
						<tr>
							<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.webposXML.getfieldTransl("user_param_name_user", false) %></td> <td colspan="3"><input type="text" name="name_user" size="25" value="<%= user.getValue("NAME_USER") %>" readonly class="inputfield_finish_green inputfield_finish_small_font"> </td>
						</tr>
						<tr>
							<td><%= Bean.webposXML.getfieldTransl("user_param_fio_nat_prs", false) %></td> <td colspan="3"><input type="text" name="fio_nat_prs" size="25" value="<%= user.getValue("FIO_NAT_PRS") %>" readonly class="inputfield_finish_green inputfield_finish_small_font"> </td>
						</tr>
						<tr>
							<td><%= Bean.webposXML.getfieldTransl("user_work_place", false) %></td> <td colspan="3"><input type="text" name="sname_jur_prs" size="25" value="<%= user.getValue("SNAME_JUR_PRS") %>" readonly class="inputfield_finish_green inputfield_finish_small_font"> </td>
						</tr>
						<tr>
							<td><%= Bean.webposXML.getfieldTransl("user_status_full", true) %></td> <td colspan="3"><select name="cd_user_status" id='cd_user_status' class="inputfield"><%=Bean.getUserStatusOptions(user.getValue("CD_USER_STATUS"), true) %></select></td>
						</tr>
						<tr>
							<td><%= Bean.webposXML.getfieldTransl("id_term", true) %></td> <td colspan="3"><select name="id_user_term" id='id_user_term' class="inputfield"><%=Bean.getWebPOSAdminTerminalsOptions(user.getValue("ID_TERM"), true) %></select></td>
						</tr>
						<tr>
							<td><%= Bean.webposXML.getfieldTransl("user_role", true) %></td> <td colspan="3"><select name="id_user_role" id='id_user_role' class="inputfield"><%=Bean.getWebPOSRolesOptions(user.getValue("ID_ROLE"), user.getValue("ID_SERVICE_PLACE_WORK"), true) %></select></td>
						</tr>
						<% String cd_user_access_type = user.getValue("CD_USER_ACCESS_TYPE");%>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("title_access_type", true) %></td>
							<td><%=Bean.getSelectBeginHTML("cd_user_access_type", Bean.webposXML.getfieldTransl("title_access_type", false)) %>
							 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "", "") %>
							 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "CASHIER", Bean.webposXML.getfieldTransl("title_access_type_cashier", false), "style=\"font-weight: bold; color: green;\"") %>
							 	<%=Bean.getSelectOptionHTML(cd_user_access_type, "MANAGER", Bean.webposXML.getfieldTransl("title_access_type_manager", false), "style=\"font-weight: bold; color: blue;\"") %>
							<%=Bean.getSelectOnChangeEndHTML() %></td>
						</tr>
						<% String cdCard1 = user.getValue("CD_CARD1_HIDE"); %>
						<tr>
							<% if (cdCard1== null || "".equalsIgnoreCase(cdCard1)) { %>
							<td><%= Bean.webposXML.getfieldTransl("user_cd_card1", false) %></td> <td colspan="3"><input type="text" name="user_cd_card1" size="25" value="<%= Bean.webposXML.getfieldTransl("user_cd_card1_none", false) %>" readonly class="inputfield_finish_red inputfield_finish_small_font"></td>
							
							<%/*Заглушка!!! Поменять местами с предыдущим кодом*/ } else if (1==2) { %>
							<td><%= Bean.webposXML.getfieldTransl("user_cd_card1", false) %> (<span class="go_to" onclick="ajaxpage('admin/setting.jsp?tab=1', 'div_main')" title="<%=Bean.webposXML.getfieldTransl("title_user_give_card", false) %>"><%=Bean.webposXML.getfieldTransl("title_user_give_card_short", false) %></span>)</td> <td colspan="3"><input type="text" name="user_cd_card1" size="25" value="<%= Bean.webposXML.getfieldTransl("user_cd_card1_none", false) %>" readonly class="inputfield_finish_red inputfield_finish_small_font"></td>
							<% } else { %>
							<td><%= Bean.webposXML.getfieldTransl("user_cd_card1", false) %></td> <td colspan="3"><input type="text" name="user_cd_card1" size="25" value="<%= cdCard1 %>" readonly class="inputfield_finish_blue inputfield_finish_small_font"> </td>
							<% } %>
						</tr>
						<tr>
							<td colspan="4"  align="center">
								<%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_save", "updateForm", "div_main", "validateParam") %>
						        <%=Bean.getSubmitButtonAjax("admin/setting.jsp", "button_back", "updateForm2", "div_main") %>
							</td>
						</tr>
					</table>
				</form>	
				<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="show">
				</form>
		<%} else if (action.equalsIgnoreCase("give2")) {
	%>
		<script>
	function validatePutCard(){
		var returnValue = false;
		var formAll = new Array (
			new Array ('cd_card1', 'card', 1),
			new Array ('client_country', 'varchar2', 1),
			new Array ('club_date_beg', 'varchar2', 1),
			new Array ('new_client_package', 'varchar2', 1),
			new Array ('pay_type', 'varchar2', 1)
		);
		var formPayBankCard = new Array (
			new Array ('bank_trn', 'varchar2', 1)
		);

		try {
			var pay_type_card = document.getElementById('pay_type_BANK_CARD');
			if (pay_type_card.checked) {
				formAll = formAll.concat(formPayBankCard);
			}
		} catch(err) {}
		returnValue = validateFormForID(formAll, 'updateForm');
		return returnValue;
	}
	card_mask2("cd_card1");

	function checkPayType(checkBox){
		//checkBox = document.getElementById(elem);
		element = document.getElementById('bank_trn');
		if (checkBox.id == 'pay_type_BANK_CARD') {
			element.className = 'inputfield';
			element.readOnly = 0;
		} else {
			element.value = '';
			element.className = 'inputfield-ro';
			element.readOnly = 1;
		}
	}
	</script>
			<h1><%=Bean.webposXML.getfieldTransl("title_card_registration", false) %><%=Bean.getHelpButton("put_card", "div_action_big") %></h1>
			<%=Bean.getCardActivationTabSheets("1") %>
			<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
				<input type="hidden" name="type" value="client">
    			<input type="hidden" name="action" value="put_card">
    			<input type="hidden" name="process" value="yes">
				<table class="action_table">
				<tbody>
			  			<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("cd_card1", false) %></td><td><input type="text" name="cd_card1" id="cd_card1" size="25" value="" readonly class="inputfield-ro"></td></tr>
						<tr><td><%= Bean.webposXML.getfieldTransl("client_country", false) %></td><td><input type="text" name="client_country_name" id="client_country_name" size="25" value="<%=Bean.getCountryName(Bean.loginTerm.getValue("ADR_CODE_COUNTRY_SERVICE_PLACE", "TERM")) %>" readonly class="inputfield-ro"></td></tr>
			  			<tr><td><%=Bean.webposXML.getfieldTransl("club_date_beg", true) %></td><td><%=Bean.getCalendarInputField("club_date_beg", Bean.getSysDate(), "10") %></td></tr>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("new_client_package", true) %></td>
							<td>
								<%=Bean.getSelectBeginHTML("new_client_package", Bean.webposXML.getfieldTransl("new_client_package", false)) %>
									<%= Bean.getWEBPosCardPackagesOptions("", Bean.loginTerm.getValue("ID_DEALER", "TERM"), Bean.loginTerm.getValue("ID_CLUB", "TERM"), "", true) %>
								<%=Bean.getSelectEndHTML() %>
							</td>
						</tr>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", true) %></td>
							<td>
								<% if (Bean.isMenuElementVisible("WEBPOS_SERVICE_CARD_ACTIVATION_PAY_CASH")) { %>
								<input id="pay_type_CASH" type="radio" <%=kind1Disable %> value="CASH" name="pay_type" onclick="checkPayType(this)">
								<label class="checbox_label" <%=kind1Disable %> for="pay_type_CASH"><%=Bean.webposXML.getfieldTransl("goods_pay_cash", false) %></label>
								<% } %>
								<% if (Bean.isMenuElementVisible("WEBPOS_SERVICE_CARD_ACTIVATION_PAY_BANK_CARD")) { %>
								<br>
								<input id="pay_type_BANK_CARD" type="radio" <%=kind2Disable %> value="BANK_CARD" name="pay_type" onclick="checkPayType(this)">
								<label class="checbox_label" <%=kind2Disable %> for="pay_type_BANK_CARD"><%=Bean.webposXML.getfieldTransl("goods_pay_card", false) %></label>
								<br>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="bank_trn" id="bank_trn" size="21" value="" placeholder="<%=Bean.webposXML.getfieldTransl("bank_trn", false) %>" readonly class="inputfield-ro" title="<%=Bean.webposXML.getfieldTransl("bank_trn", false) %>" >
								<% } %>
								<% if (Bean.isMenuElementVisible("WEBPOS_SERVICE_CARD_ACTIVATION_MAKE_INVOICE")) { %>
								<br>
								<input id="pay_type_INVOICE" type="radio" <%=kind3Disable %> value="INVOICE" name="pay_type" onclick="checkPayType(this)">
								<label class="checbox_label" <%=kind3Disable %> for="pay_type_INVOICE"><%=Bean.webposXML.getfieldTransl("goods_pay_invoice", false) %></label>
								<% } %>
								<% if (enable1Count == 0) { %>
									<%=Bean.webposXML.getfieldTransl("goods_pay_hasnt_permission", false) %>
								<% } %>
							</td>
						</tr>
						<% if (enable1Count > 0) { %>
						<tr>
							<td colspan="2" align="center">
						        <%=Bean.getSubmitButtonAjax("action/new_client_registration.jsp", "put", "updateForm", "div_action_big", "validatePutCard") %>
						        <%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateForm", "div_main") %>
							</td>
						</tr>
						<% } else { %>
						<tr>
							<td colspan="2" align="center">
						        <br><span style="font-weight: bold; color: red;"><%=Bean.webposXML.getfieldTransl("operation_permission_denied", false) %></span><br>
						        <%=Bean.getSubmitButtonAjax("action/new_client.jsp", "button_back", "updateForm", "div_main") %>
							</td>
						</tr>
						<% } %>
				</tbody>
				</table>
			</form>
		<%= Bean.getCalendarScript("club_date_beg", false) %>
			<% 
			
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("give")) {
			
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
