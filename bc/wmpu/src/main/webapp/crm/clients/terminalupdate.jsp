<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcTerminalObject"%>
<%@page import="bc.objects.bcTerminalUserObject"%>
<%@page import="bc.objects.bcObject"%>
<%@page import="bc.objects.bcTerminalDeviceTypeObject"%>
<%@page import="bc.objects.bcClubObject"%>
<%@page import="bc.objects.bcTerminalOnlinePaymentTypeObject"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcTerminalMessageReceiverObject"%>
<%@page import="bc.objects.bcTerminalSAMObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_TERMINALS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String p_tab				= Bean.getDecodeParamPrepare(parameters.get("tab"));
String id_term				= Bean.getDecodeParamPrepare(parameters.get("id_term")); 
String p_id_sam				= Bean.getDecodeParamPrepare(parameters.get("id_sam"));
String type					= Bean.getDecodeParamPrepare(parameters.get("type"));
String action				= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process				= Bean.getDecodeParamPrepare(parameters.get("process"));
String p_cd_scheme			= Bean.getDecodeParamPrepare(parameters.get("cd")); 
String p_cd_kind			= Bean.getDecodeParamPrepare(parameters.get("kind"));
String p_status				= Bean.getDecodeParamPrepare(parameters.get("status")); 
String p_param				= Bean.getDecodeParamPrepare(parameters.get("param"));
String back_type			= Bean.getDecodeParamPrepare(parameters.get("back_type"));
String external_id			= Bean.getDecodeParamPrepare(parameters.get("id"));
String id_partner			= Bean.getDecodeParamPrepare(parameters.get("id_partner"));

String updateLink = "../crm/clients/terminalupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"TERMINAL":back_type;
System.out.println("back_type="+back_type);
if ("TERMINAL".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/terminals.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/clients/terminals.jsp?";
	} else {
		backLink = "../crm/clients/terminalspecs.jsp?id="+id_term;
	}
} else if ("PARTNER".equalsIgnoreCase(back_type) || "SERVICE_PLACE".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
	backLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
}

if (type.equalsIgnoreCase("term")) {
	%> 
	<% 

	if (process.equalsIgnoreCase("no"))
  /* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {%>
		
		<script language="JavaScript">
			dwr_get_term_device_type(document.getElementById('cd_term_type'),'id_device_type','<%=Bean.getSessionId() %>');
		</script>    	

		<script>
			var formPhisicalTerm = new Array (
					new Array ('cd_term_type', 'varchar2', 1),
					new Array ('id_device_type', 'varchar2', 1)
			);

			var formOtherTerm = new Array (
					new Array ('cd_term_type', 'varchar2', 1)
			);

			function myValidateForm(){
				var termType = document.getElementById('cd_term_type').value;
				if (termType == 'PHYSICAL') {
					return validateForm(formPhisicalTerm);
				} else {
					return validateForm(formOtherTerm);
				}
			}
			
		</script>
 		
			<% if ("TERMINAL".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.terminalXML.getfieldTransl("h_terminal_add", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.terminalXML.getfieldTransl("h_terminal_add", false),
					"Y",
					"Y") 
			%>
			<% } %>
    <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="term">
        <input type="hidden" name="action" value="addnext">
        <input type="hidden" name="process" value="no">
        <input type="hidden" name="actionprev" value="<%=action %>">
        <input type="hidden" name="id_term_prev" value="<%=id_term %>">
    	<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">
    	<input type="hidden" name="id_partner" value="<%=id_partner %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_type", true) %></td><td><%=Bean.getTermTypeRadio("cd_term_type", "PHYSICAL") %></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", ("TERMINAL".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>
	</table>
	</form>
   	
    	<%
    	} else if (action.equalsIgnoreCase("addnext")) {
    		
    		bcClubObject club = new bcClubObject(Bean.getCurrentClubID());
    		
	        %> 
		<script>
			var formPhisicalTerm = new Array (
					new Array ('id_term', 'varchar2', 1),
					new Array ('id_device_type', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('club_registration_date', 'varchar2', 1),
					new Array ('name_finance_acquirer', 'varchar2', 1),
					new Array ('term_code_page', 'varchar2', 1),
					new Array ('crypt_telgr', 'varchar2', 1),
					new Array ('resp_time', 'varchar2', 1),
					new Array ('vk_enc', 'varchar2', 1),
					new Array ('ver_telgr', 'varchar2', 1),
					new Array ('nincmax', 'varchar2', 1),
					new Array ('connect_ekka', 'varchar2', 1),
					new Array ('tr_limit', 'varchar2', 1),
					new Array ('cd_term_currency', 'varchar2', 1),
					new Array ('sname_term_currency', 'varchar2', 1),
					new Array ('can_oper_different_currency', 'varchar2', 1)
			);

			var formOtherTerm = new Array (
					new Array ('name_club', 'varchar2', 1),
					new Array ('club_registration_date', 'varchar2', 1),
					new Array ('name_service_place', 'varchar2', 1),
					new Array ('date_location', 'varchar2', 1),
					new Array ('name_referral_scheme', 'varchar2', 1),
					new Array ('name_loyality_scheme', 'varchar2', 1),
					new Array ('cd_term_currency', 'varchar2', 1)
			);
			function myValidateForm(){
				var termType = document.getElementById('cd_term_type').value;
				if (termType == 'PHYSICAL') {
					return validateForm(formPhisicalTerm);
				} else {
					return validateForm(formOtherTerm);
				}
			}
			
		</script>
	
		<% if ("TERMINAL".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.terminalXML.getfieldTransl("h_terminal_add", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.terminalXML.getfieldTransl("h_terminal_add", false),
					"Y",
					"Y") 
			%>
			<% } %>
	<%
	String
		cd_term_type			= Bean.getDecodeParam(parameters.get("cd_term_type")),
		id_device_type			= Bean.getDecodeParam(parameters.get("id_device_type")),
		id_club					= Bean.getDecodeParam(parameters.get("id_club")),
		club_registration_date	= Bean.getDecodeParam(parameters.get("club_registration_date")),
		actionprev				= Bean.getDecodeParam(parameters.get("actionprev")),
		id_term_prev			= Bean.getDecodeParam(parameters.get("id_term_prev"));

	boolean isWebPosTerminal = "WEBPOS".equalsIgnoreCase(cd_term_type);
	boolean isPhisicalTerminal = "PHYSICAL".equalsIgnoreCase(cd_term_type);
	
	String 
		term_code_page = "",
		crypt_telgr = "",
		resp_time = "",
		vk_enc = "",
		ver_telgr = "",
		nincmax = "",
		connect_ekka = "",
		tr_limit = "",
		noprint_cheque_discount_club = "0",
		noprint_cheque_bonus_club = "0",
		noprint_cheque_mov_bon = "0",
		noprint_cheque_check_param = "0",
		noprint_cheque_pay_cash = "0",
		noprint_cheque_pay_card = "0",
		noprint_cheque_pay_bon = "0",
		card_type_active_nsmep = "0",
		card_type_active_magnetic = "0",
		card_type_active_chip = "0",
		card_type_active_emv = "0",
		card_type_active_barcode = "0",
		oper_type_active_nsmep = "0",
		oper_type_active_cash = "0",
		oper_type_active_bon = "0",
		oper_type_active_cheque = "0",
		oper_type_active_emv = "0",
		oper_save_cheque = "Y",
		oper_sms_cheque = "Y";
		
	if (!Bean.isEmpty(id_device_type)) {
		bcTerminalDeviceTypeObject device = new bcTerminalDeviceTypeObject(id_device_type);
		
		term_code_page 					= device.getValue("term_code_page");
		crypt_telgr 					= device.getValue("crypt_telgr");
		resp_time 						= device.getValue("resp_time");
		vk_enc 							= device.getValue("vk_enc");
		ver_telgr 						= device.getValue("ver_telgr");
		nincmax 						= device.getValue("nincmax");
		connect_ekka 					= device.getValue("connect_ekka");
		tr_limit 						= device.getValue("tr_limit");
		noprint_cheque_discount_club 	= device.getValue("noprint_cheque_discount_club");
		noprint_cheque_bonus_club 		= device.getValue("noprint_cheque_bonus_club");
		noprint_cheque_mov_bon 			= device.getValue("noprint_cheque_mov_bon");
		noprint_cheque_check_param 		= device.getValue("noprint_cheque_check_param");
		noprint_cheque_pay_cash 		= device.getValue("noprint_cheque_pay_cash");
		noprint_cheque_pay_card 		= device.getValue("noprint_cheque_pay_card");
		noprint_cheque_pay_bon 			= device.getValue("noprint_cheque_pay_bon");
		card_type_active_nsmep 			= device.getValue("card_type_active_nsmep");
		card_type_active_magnetic 		= device.getValue("card_type_active_magnetic");
		card_type_active_chip 			= device.getValue("card_type_active_chip");
		card_type_active_emv 			= device.getValue("card_type_active_emv");
		card_type_active_barcode 		= device.getValue("card_type_active_barcode");
		oper_type_active_nsmep 			= device.getValue("oper_type_active_nsmep");
		oper_type_active_cash 			= device.getValue("oper_type_active_cash");
		oper_type_active_bon 			= device.getValue("oper_type_active_bon");
		oper_type_active_cheque 		= device.getValue("oper_type_active_cheque");
		oper_type_active_emv 			= device.getValue("oper_type_active_emv");
	} else {
		
		term_code_page 					= club.getValue("term_code_page");
		crypt_telgr 					= club.getValue("crypt_telgr");
		resp_time 						= club.getValue("max_resp_time");
		vk_enc 							= club.getValue("def_ver_key");
		ver_telgr 						= club.getValue("def_ver_telgr");
		nincmax 						= club.getValue("def_nincmax");
		connect_ekka 					= "0";
		tr_limit 						= club.getValue("def_tr_limit");
		/*noprint_cheque_discount_club 	= club.getValue("noprint_cheque_discount_club");
		noprint_cheque_bonus_club 		= club.getValue("noprint_cheque_bonus_club");
		noprint_cheque_mov_bon 			= club.getValue("noprint_cheque_mov_bon");
		noprint_cheque_check_param 		= club.getValue("noprint_cheque_check_param");
		noprint_cheque_pay_cash 		= club.getValue("noprint_cheque_pay_cash");
		noprint_cheque_pay_card 		= club.getValue("noprint_cheque_pay_card");
		noprint_cheque_pay_bon 			= club.getValue("noprint_cheque_pay_bon");
		card_type_active_nsmep 			= club.getValue("card_type_active_nsmep");
		card_type_active_magnetic 		= club.getValue("card_type_active_magnetic");
		card_type_active_chip 			= club.getValue("card_type_active_chip");
		card_type_active_emv 			= club.getValue("card_type_active_emv");
		card_type_active_barcode 		= club.getValue("card_type_active_barcode");
		oper_type_active_nsmep 			= club.getValue("oper_type_active_nsmep");
		oper_type_active_cash 			= club.getValue("oper_type_active_cash");
		oper_type_active_bon 			= club.getValue("oper_type_active_bon");
		oper_type_active_cheque 		= club.getValue("oper_type_active_cheque");
		oper_type_active_emv 			= club.getValue("oper_type_active_emv");*/
	}
	%>
    <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="term">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_club" value="<%=id_club %>">
        <input type="hidden" name="cd_term_type" id="cd_term_type" value="<%=cd_term_type %>">
        <input type="hidden" name="id_device_type" value="<%=id_device_type %>">
    	<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">
	<table <%=Bean.getTableDetailParam() %>>
		<% if (isPhisicalTerminal) { %>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("id_term", true) %></td><td><input type="text" name="id_term" size="20" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "20") %>
		  	</td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("term_serial_number", false) %></td><td><input type="text" name="term_serial_number" size="20" value="" class="inputfield"></td>
			<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club_date_beg", true) %></td><td class="bottom_line"><%=Bean.getCalendarInputField("club_registration_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_status", true) %></td> <td><select name="cd_term_status" class="inputfield"><%= Bean.getTermStatusOptions("ACTIVE", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("term_owner", false) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("term_owner", "", "", "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_type", false) %></td><td><input type="text" name="name_term_type" size="20" value="<%=Bean.getTermTypeName(cd_term_type) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><span id="span_finance_acquirer"><%= Bean.terminalXML.getfieldTransl("name_finance_acquirer", true) %></span></td> 
			<td>
				<%=Bean.getWindowFindJurPrs("finance_acquirer", "", "", "FIN_ACQUIRER", "40") %>
			</td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("device_type", isPhisicalTerminal) %></td> <td><select name="id_device_type" class="inputfield"><%= Bean.getTermDeviceOptions(cd_term_type, "", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("id_technical_acquirer", false) %> </td> 
			<td>
				<%=Bean.getWindowFindJurPrs("technical_acquirer", "", "", "TECH_ACQUIRER", "40") %>
			</td>	
		</tr>
		<% } else { %>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_status", true) %></td> <td><select name="cd_term_status" class="inputfield"><%= Bean.getTermStatusOptions("ACTIVE", true) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "20") %>
		  	</td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_type", false) %></td><td><input type="text" name="name_term_type" size="20" value="<%=Bean.getTermTypeName(cd_term_type) %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club_date_beg", true) %></td><td class="bottom_line"><%=Bean.getCalendarInputField("club_registration_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("device_type", isPhisicalTerminal) %></td> <td><select name="id_device_type" class="inputfield"><%= Bean.getTermDeviceOptions(cd_term_type, "", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("term_owner", false) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("term_owner", "", "", "ALL", "40") %>
			</td>			
		</tr>
		<% } %>
		<tr>
			<td><%=  Bean.terminalXML.getfieldTransl("DESCRIPTION", false) %></td><td colspan="5"><textarea name="description" cols="120" rows="3" class="inputfield"></textarea></td>
		</tr>

		<tr>
			<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_location_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("name_service_place", isWebPosTerminal) %></td> 
			<td>
				<%=Bean.getWindowFindServicePlace("service_place", ("SERVICE_PLACE".equalsIgnoreCase(back_type)?external_id:""), "", id_partner, "", "40") %>
			</td>			
			<td><%= Bean.terminalXML.getfieldTransl("referral_scheme", isWebPosTerminal) %></td> 
			<td>
				<%=Bean.getWindowFindReferralScheme("referral_scheme", "", "", id_partner, "40", false) %>
			</td>		
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("date_location", isWebPosTerminal) %></td><td><%=Bean.getCalendarInputField("date_location", "", "15") %></td>
			<td><%= Bean.terminalXML.getfieldTransl("NAME_LOYALITY_SCHEME", isWebPosTerminal) %></td>
			<td>
				<%=Bean.getWindowFindLoyScheme("loyality_scheme", "", id_partner, "40", false) %>
			</td>			
		</tr>
		<tr>
			<% if (isPhisicalTerminal) { %>
			<td><%= Bean.terminalXML.getfieldTransl("cash_desk_number", false) %></td><td><input type="text" name="cash_desk_number" size="20" value="" class="inputfield"></td>
			<td><%= Bean.terminalXML.getfieldTransl("ID_SHEDULE", false) %></td>
			<td>
				<%=Bean.getWindowFindLoySchedule("shedule",  "", "40", false) %>
			</td>			
			<% } else { %>
			<td><%= Bean.terminalXML.getfieldTransl("can_test_mode", false) %></td> <td><select name="can_test_mode" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select> </td>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<% if (isPhisicalTerminal) { %>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.terminalXML.getfieldTransl("autosynchronize_loy_param", false) %></td><td><select name="autosynchronize_loy_param" id="autosynchronize_loy_param" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %></select></td>
		</tr>
		<% } %>

		<% if (isPhisicalTerminal) { %>
		<tr>
			<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_setting_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("term_mon_interval_day", false) %></td><td><input type="text" name="term_mon_interval_day" size="20" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("TERM_CODE_PAGE", true) %></td> <td><select name="term_code_page"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("TERMINAL_CODE_PAGE", term_code_page, true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("VER_TELGR", true) %></td> <td><select name="ver_telgr" class="inputfield" title="TEL_VERSION"><%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", ver_telgr, true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("CRYPT_TELGR", true) %></td><td><select name="crypt_telgr" class="inputfield" title="CRYPT_TELGR"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", crypt_telgr, true) %></select></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("nincmax", true) %></td>  <td><input type="text" name="nincmax" size="16" value="<%=nincmax %>" class="inputfield" title="NINCMAX"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("RESP_TIME", true) %></td> <td><input type="text" name="resp_time" size="16" value="<%=resp_time %>" class="inputfield" title="RESP_TIME"></td>
			<td><%= Bean.terminalXML.getfieldTransl("connect_ekka", true) %></td> <td><select name="connect_ekka" class="inputfield" title="CONNECT_EKKA"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", connect_ekka, true) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("VK_ENC", true) %></td> <td valign="top"><select name="vk_enc" class="inputfield" title="VK_ENC"><%= Bean.getMeaningFromLookupNameOptions("VER_KEY", vk_enc, true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("tr_limit", true) %></td> <td valign="top"><input type="text" name="tr_limit" size="15" value="<%=tr_limit %>" class="inputfield"></td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>

		<tr>
			<td><b><%= Bean.terminalXML.getfieldTransl("h_bon_cards_types", false) %></b></td>
			<td><b><%= Bean.terminalXML.getfieldTransl("h_operations_types", false) %></b></td>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_print_cheque_settings", false) %></b></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_nsmep", card_type_active_nsmep, Bean.terminalXML.getfieldTransl("card_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_nsmep", oper_type_active_nsmep, Bean.terminalXML.getfieldTransl("oper_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_discount_club", noprint_cheque_discount_club, Bean.terminalXML.getfieldTransl("noprint_cheque_discount_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_cash", noprint_cheque_pay_cash, Bean.terminalXML.getfieldTransl("noprint_cheque_pay_cash", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_magnetic", card_type_active_magnetic, Bean.terminalXML.getfieldTransl("card_type_active_magnetic", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cash", oper_type_active_cash, Bean.terminalXML.getfieldTransl("oper_type_active_cash", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_bonus_club", noprint_cheque_bonus_club, Bean.terminalXML.getfieldTransl("noprint_cheque_bonus_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_card", noprint_cheque_pay_card, Bean.terminalXML.getfieldTransl("noprint_cheque_pay_card", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_chip", card_type_active_chip, Bean.terminalXML.getfieldTransl("card_type_active_chip", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_bon", oper_type_active_bon, Bean.terminalXML.getfieldTransl("oper_type_active_bon", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_mov_bon", noprint_cheque_mov_bon, Bean.terminalXML.getfieldTransl("noprint_cheque_mov_bon", false)) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_emv", card_type_active_emv, Bean.terminalXML.getfieldTransl("card_type_active_emv", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cheque", oper_type_active_cheque, Bean.terminalXML.getfieldTransl("oper_type_active_cheque", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_check_param", noprint_cheque_check_param, Bean.terminalXML.getfieldTransl("noprint_cheque_check_param", false)) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_barcode", card_type_active_barcode, Bean.terminalXML.getfieldTransl("card_type_active_barcode", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_emv", oper_type_active_emv, Bean.terminalXML.getfieldTransl("OPER_TYPE_ACTIVE_EMV", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_bon", noprint_cheque_pay_bon, Bean.terminalXML.getfieldTransl("NOPRINT_CHEQUE_PAY_BON", false)) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>

		<tr>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_currencies_param", false) %></b></td>
			<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_points_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_currency", true) %></td> <td><select name="cd_term_currency" class="inputfield"><%= Bean.getCurrencyOptions("", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("can_calc_point", false) %></td> <td><select name="can_calc_point"  class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", "0", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("sname_term_currency", true) %></td> <td><input type="text" name="sname_term_currency" size="16" value="" class="inputfield"></td>
			<td><%= Bean.terminalXML.getfieldTransl("sname_point", false) %></td> <td><input type="text" name="sname_point" size="16" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("can_oper_different_currency", true) %></td> <td><select name="can_oper_different_currency"  class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", "0", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("multiplicator_point", false) %></td> <td><input type="text" name="multiplicator_point" size="16" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4"><b><%= Bean.terminalXML.getfieldTransl("h_need_online_pin", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_bon_pay_pin", false) %></td> <td><select name="need_calc_online_bon_pay_pin"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
			<td colspan="2"><%= Bean.terminalXML.getfieldTransl("need_calc_online_club_pay_pin", true) %></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_storno_pin", false) %></td> <td><select name="need_calc_online_storno_pin"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } else { %>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td class="top_line" colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_currencies_param", false) %></b></td>
			<td class="top_line" colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_print_cheque_settings", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_currency", true) %></td> <td><select name="cd_term_currency" class="inputfield"><%= Bean.getCurrencyOptions("", true) %></select></td>
			<td><%=Bean.getCheckBox("oper_save_cheque", oper_save_cheque, Bean.terminalXML.getfieldTransl("oper_save_cheque", false)) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.getCheckBox("oper_sms_cheque", oper_sms_cheque, Bean.terminalXML.getfieldTransl("oper_sms_cheque", false)) %></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4"><b><%= Bean.terminalXML.getfieldTransl("h_online_confirmations", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_cash_conf_type", false) %></td> <td valign="top"><select name="cd_online_pay_cash_conf_type" class="inputfield"><%= Bean.getTermPayConfirmationWayOptions("NONE", true) %></select></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_rbk_conf_type", false) %></td> <td valign="top"><select name="cd_online_pay_rbk_conf_type" class="inputfield"><%= Bean.getTermPayConfirmationWayOptions("NONE", true) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_card_conf_type", false) %></td> <td valign="top"><select name="cd_online_pay_card_conf_type" class="inputfield"><%= Bean.getTermPayConfirmationWayOptions("NONE", true) %></select></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("has_online_pay_rbk_permission", false) %></td> <td valign="top"><select name="has_online_pay_rbk_permission" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_point_conf_type", false) %></td> <td valign="top"><select name="cd_online_pay_point_conf_type" class="inputfield"><%= Bean.getTermPayConfirmationWayOptions("NONE", true) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<% } %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/terminalupdate.jsp?id_term=" + id_term_prev + "&type=term&action=" + actionprev + "&process=no&id=" + external_id + "&back_type=" + back_type, "cancel", ("TERMINAL".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
			</td>
		</tr>
	</table>
	</form>

		<%= Bean.getCalendarScript("club_registration_date", false) %>
		<%= Bean.getCalendarScript("date_location", false) %>
		
	  <%
	   	} else if (action.equalsIgnoreCase("edit")) { 
	   		bcTerminalObject terminal = new bcTerminalObject(id_term);
	   		terminal.getFeature();
	   		
	   		String cdTermType = terminal.getValue("CD_TERM_TYPE");
	   		boolean isWebPosTerminal = "WEBPOS".equalsIgnoreCase(cdTermType);
	   		boolean isPhisicalTerminal = "PHYSICAL".equalsIgnoreCase(cdTermType);
	   	 %>

			<script>
				var formPhisicalTerm = new Array (
						new Array ('cd_term_status', 'varchar2', 1),
						new Array ('id_device_type', 'varchar2', 1),
						new Array ('name_club', 'varchar2', 1),
						new Array ('club_registration_date', 'varchar2', 1),
						new Array ('name_finance_acquirer', 'varchar2', 1),
						new Array ('need_update_param', 'varchar2', 1),
						new Array ('need_sys_lock', 'varchar2', 1),
						new Array ('term_code_page', 'varchar2', 1),
						new Array ('crypt_telgr', 'varchar2', 1),
						new Array ('resp_time', 'varchar2', 1),
						new Array ('vk_enc', 'varchar2', 1),
						new Array ('ver_telgr', 'varchar2', 1),
						new Array ('nincmax', 'varchar2', 1),
						new Array ('connect_ekka', 'varchar2', 1),
						new Array ('tr_limit', 'varchar2', 1),
						new Array ('cd_term_currency', 'varchar2', 1),
						new Array ('sname_term_currency', 'varchar2', 1),
						new Array ('can_oper_different_currency', 'varchar2', 1)
				);

				var formOtherTerm = new Array (
						new Array ('cd_term_status', 'varchar2', 1),
						new Array ('name_club', 'varchar2', 1),
						new Array ('club_registration_date', 'varchar2', 1),
						new Array ('name_service_place', 'varchar2', 1),
						new Array ('date_location', 'varchar2', 1),
						new Array ('name_referral_scheme', 'varchar2', 1),
						new Array ('can_test_mode', 'varchar2', 1),
						new Array ('cd_term_currency', 'varchar2', 1),
						new Array ('sname_term_currency', 'varchar2', 1)
				);

				function myValidateForm(){
					var termType = document.getElementById('cd_term_type').value;
					if (termType == 'PHYSICAL') {
						return validateForm(formPhisicalTerm);
					} else {
						return validateForm(formOtherTerm);
					}
				}
				function checkTermType(){
					var termType = document.getElementById('cd_term_type').value;
					if (termType == 'PHYSICAL') {
						document.getElementById('span_finance_acquirer').innerHTML='<%= Bean.terminalXML.getfieldTransl("name_finance_acquirer", true) %>';
					} else {
						document.getElementById('span_finance_acquirer').innerHTML='<%= Bean.terminalXML.getfieldTransl("name_finance_acquirer", false) %>';
					}
				}
		   
				function submitDateForm(form){
					form.datetime.value = '';
					var origDate = form.date.value;
					if (origDate != '') {
						var hours = form.hours.value;
						var min = form.min.value;
						var sec = form.sec.value;
						form.datetime.value = origDate+' '+hours+':'+min+':'+sec;
					}			
					form.submit();
				}		
				checkTermType();
			</script>
		
			<%= Bean.getOperationTitleShort(
					"",
					Bean.terminalXML.getfieldTransl("h_terminal_edit", false),
					"Y",
					"Y") 
			%>
	    <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="term">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="cd_term_type" id="cd_term_type" value="<%=cdTermType%>">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("id_term", false) %></td>
				<td>
					<input type="text" name="id_term" size="20" value="<%= terminal.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro">
					<span><%=terminal.getValue("ID_TERM_HEX") %> (HEX)</span>
				</td>
	 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(terminal.getValue("ID_CLUB")) %>
				</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(terminal.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("term_serial_number", false) %></td><td><input type="text" name="term_serial_number" size="20" value="<%=terminal.getValue("TERM_SERIAL_NUMBER") %>" class="inputfield"></td>
				<td><%= Bean.clubXML.getfieldTransl("club_date_beg", true) %></td><td><%=Bean.getCalendarInputField("club_registration_date", terminal.getValue("CLUB_REGISTRATION_DATE_FRMT"), "15") %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("cd_term_status", true) %></td> <td><select name="cd_term_status" class="inputfield"><%= Bean.getTermStatusOptions(terminal.getValue("NAME_TERM_STATUS"), true) %></select></td>
				<td><%= Bean.terminalXML.getfieldTransl("term_owner", false) %>
					<%=Bean.getGoToJurPrsHyperLink(terminal.getValue("ID_TERM_OWNER")) %>
				</td>
				<td>
					<%=Bean.getWindowFindJurPrs("term_owner", terminal.getValue("ID_TERM_OWNER"), terminal.getValue("SNAME_TERM_OWNER"), "ALL", "40") %>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("NAME_TERM_TYPE", false) %></td><td><input type="text" name="NAME_TERM_TYPE" size="30" value="<%= terminal.getValue("NAME_TERM_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
				<% if (isPhisicalTerminal) { %>
				<td><span id="span_finance_acquirer"><%= Bean.terminalXML.getfieldTransl("name_finance_acquirer", false) %></span>
					<%=Bean.getGoToJurPrsHyperLink(terminal.getValue("ID_FINANCE_ACQUIRER")) %>
				</td> 
				<td>
					<%=Bean.getWindowFindJurPrs("finance_acquirer", terminal.getValue("ID_FINANCE_ACQUIRER"), terminal.getValue("SNAME_FINANCE_ACQUIRER"), "FIN_ACQUIRER", "40") %>
				</td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("device_type", isPhisicalTerminal) %></td> <td><select name="id_device_type" class="inputfield"><%= Bean.getTermDeviceOptions(terminal.getValue("CD_TERM_TYPE"), terminal.getValue("ID_DEVICE_TYPE"), true) %></select> </td>
				<% if (isPhisicalTerminal) { %>
				<td><%= Bean.terminalXML.getfieldTransl("id_technical_acquirer", false) %>
					<%=Bean.getGoToJurPrsHyperLink(terminal.getValue("ID_TECHNICAL_ACQUIRER")) %>
				</td> 
				<td>
					<%=Bean.getWindowFindJurPrs("technical_acquirer", terminal.getValue("ID_TECHNICAL_ACQUIRER"), terminal.getValue("SNAME_TECHNICAL_ACQUIRER"), "TECH_ACQUIRER", "40") %>
				</td>			
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			</tr>
			<tr>
				<td><%=  Bean.terminalXML.getfieldTransl("DESCRIPTION", false) %></td><td colspan="3"><textarea name="description" cols="120" rows="3" class="inputfield"><%= terminal.getValue("DESCRIPTION") %></textarea></td>
			</tr>

			<tr>
				<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_location_param", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("name_service_place", isWebPosTerminal) %>
					<%=Bean.getGoToServicePlaceLink(terminal.getValue("ID_SERVICE_PLACE")) %>
				</td> 
				<td>
					<% if (isWebPosTerminal) { %>
					<%=Bean.getWindowFindServicePlace("service_place", terminal.getValue("ID_SERVICE_PLACE"), terminal.getValue("SNAME_SERVICE_PLACE"), terminal.getValue("ID_DEALER"), "", "30") %>
					<% } else { %>
	        		<input type="hidden" name="id_service_place" value="<%=terminal.getValue("ID_SERVICE_PLACE")%>">
					<%=Bean.getInputTextElement("sname_service_place", "", terminal.getValue("SNAME_SERVICE_PLACE"), true, "250") %>
					<% } %>		
				</td>
				<td><%= Bean.terminalXML.getfieldTransl("referral_scheme", isWebPosTerminal) %>
					<%= Bean.getGoToReferralSchemeLink(terminal.getValue("ID_REFERRAL_SCHEME")) %>
				</td> 
				<td>
					<%=Bean.getWindowFindReferralScheme("referral_scheme", terminal.getValue("ID_REFERRAL_SCHEME"), "", terminal.getValue("ID_DEALER"), "40", false) %>
				</td>		
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("date_location", isWebPosTerminal) %></td><td><%=Bean.getCalendarInputField("date_location", terminal.getValue("DATE_LOCATION_DF"), "15") %></td>
				<% if (isWebPosTerminal) { %>
				<td><%= Bean.terminalXML.getfieldTransl("can_test_mode", true) %></td> <td><select name="can_test_mode" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("CAN_TEST_MODE"), true) %></select> </td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			</tr>
			<% if (isPhisicalTerminal) { %>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("cash_desk_number", false) %></td><td><input type="text" name="cash_desk_number" size="20" value="<%=terminal.getValue("CASH_DESK_NUMBER") %>" class="inputfield"></td>
			</tr>
			<% } %>

			<tr>
				<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_setting_param", false) %></b></td>
			</tr>
			<% if (isPhisicalTerminal) { %>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("need_update_param", true) %></td> <td><select name="need_update_param"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_UPDATE_PARAM"), true) %></select></td>
				<td><%= Bean.terminalXML.getfieldTranslDisable( "term_mon_interval_day", false, !isPhisicalTerminal) %></td><td><input type="text" name="term_mon_interval_day" size="15" " class="inputfield" value="<%=terminal.getValue("TERM_MON_INTERVAL_DAY")%>"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("need_sys_lock_name", true) %></td> <td><select name="need_sys_lock" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_SYS_LOCK"), false) %></select></td>
				<td><%= Bean.terminalXML.getfieldTransl("term_mon_rep_next_date", false) %></td><td><%=Bean.getCalendarInputField("term_mon_rep_next_date", terminal.getValue("TERM_MON_REP_NEXT_DATE_DHMF"), "15") %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("TERM_CODE_PAGE", true) %></td> <td><select name="term_code_page"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("TERMINAL_CODE_PAGE", terminal.getValue("TERM_CODE_PAGE"), true) %></select></td>
				<td><%= Bean.terminalXML.getfieldTransl("VER_TELGR", true) %></td> <td><select name="ver_telgr" class="inputfield" title="TEL_VERSION"><%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", terminal.getValue("VER_TELGR"), true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("CRYPT_TELGR", true) %></td><td><select name="crypt_telgr" class="inputfield" title="CRYPT_TELGR"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", terminal.getValue("CRYPT_TELGR"), true) %></select></td>
				<td valign="top"><%= Bean.terminalXML.getfieldTransl("nincmax", true) %></td>  <td><input type="text" name="nincmax" size="16" value="<%=terminal.getValue("NINCMAX") %>" class="inputfield" title="NINCMAX"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("RESP_TIME", true) %></td> <td><input type="text" name="resp_time" size="16" value="<%=terminal.getValue("RESP_TIME") %>" class="inputfield" title="RESP_TIME"></td>
				<td><%= Bean.terminalXML.getfieldTransl("connect_ekka", true) %></td> <td><select name="connect_ekka" class="inputfield" title="CONNECT_EKKA"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", terminal.getValue("connect_ekka"), true) %></select></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.terminalXML.getfieldTransl("VK_ENC", true) %></td> <td valign="top"><select name="vk_enc" class="inputfield" title="VK_ENC"><%= Bean.getMeaningFromLookupNameOptions("VER_KEY", terminal.getValue("VK_ENC"), true) %></select></td>
				<td valign="top"><%= Bean.terminalXML.getfieldTransl("tr_limit", true) %></td>  <td><input type="text" name="tr_limit" size="16" value="<%=terminal.getValue("TR_LIMIT") %>" class="inputfield" title="TR_LIMIT"></td>
			</tr>
			<% } else { %>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("need_update_param", true) %></td> <td><select name="need_update_param"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_UPDATE_PARAM"), true) %></select></td>
			</tr>
			<% } %>

			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
			<% if (isPhisicalTerminal) { %>
			<tr>
				<td><b><%= Bean.terminalXML.getfieldTransl("h_bon_cards_types", false) %></b></td>
				<td><b><%= Bean.terminalXML.getfieldTransl("h_operations_types", false) %></b></td>
				<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_print_cheque_settings", false) %></b></td>
			</tr>
			<tr>
				<td><%=Bean.getCheckBoxNumber("card_type_active_nsmep", terminal.getValue("CARD_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("card_type_active_nsmep", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("oper_type_active_nsmep", terminal.getValue("OPER_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("oper_type_active_nsmep", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("noprint_cheque_discount_club", terminal.getValue("NOPRINT_CHEQUE_DISCOUNT_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_discount_club", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_cash", terminal.getValue("NOPRINT_CHEQUE_PAY_CASH"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_cash", false)) %></td>
			</tr>
			<tr>
				<td><%=Bean.getCheckBoxNumber("card_type_active_magnetic", terminal.getValue("CARD_TYPE_ACTIVE_MAGNETIC"), Bean.terminalXML.getfieldTransl("card_type_active_magnetic", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("oper_type_active_cash", terminal.getValue("OPER_TYPE_ACTIVE_CASH"), Bean.terminalXML.getfieldTransl("oper_type_active_cash", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("noprint_cheque_bonus_club", terminal.getValue("NOPRINT_CHEQUE_BONUS_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_bonus_club", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_card", terminal.getValue("NOPRINT_CHEQUE_PAY_CARD"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_card", false)) %></td>
			</tr>
			<tr>
				<td><%=Bean.getCheckBoxNumber("card_type_active_chip", terminal.getValue("CARD_TYPE_ACTIVE_CHIP"), Bean.terminalXML.getfieldTransl("card_type_active_chip", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("oper_type_active_bon", terminal.getValue("OPER_TYPE_ACTIVE_BON"), Bean.terminalXML.getfieldTransl("oper_type_active_bon", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("noprint_cheque_mov_bon", terminal.getValue("NOPRINT_CHEQUE_MOV_BON"), Bean.terminalXML.getfieldTransl("noprint_cheque_mov_bon", false)) %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><%=Bean.getCheckBoxNumber("card_type_active_emv", terminal.getValue("CARD_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("card_type_active_emv", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("oper_type_active_cheque", terminal.getValue("OPER_TYPE_ACTIVE_CHEQUE"), Bean.terminalXML.getfieldTransl("oper_type_active_cheque", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("noprint_cheque_check_param", terminal.getValue("NOPRINT_CHEQUE_CHECK_PARAM"), Bean.terminalXML.getfieldTransl("noprint_cheque_check_param", false)) %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><%=Bean.getCheckBoxNumber("card_type_active_barcode", terminal.getValue("CARD_TYPE_ACTIVE_BARCODE"), Bean.terminalXML.getfieldTransl("card_type_active_barcode", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("oper_type_active_emv", terminal.getValue("OPER_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("OPER_TYPE_ACTIVE_EMV", false)) %></td>
				<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_bon", terminal.getValue("noprint_cheque_pay_bon"), Bean.terminalXML.getfieldTransl("NOPRINT_CHEQUE_PAY_BON", false)) %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>

			<tr>
				<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_currencies_param", false) %></b></td>
				<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_points_param", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("cd_term_currency", true) %></td> <td><select name="cd_term_currency" class="inputfield"><%= Bean.getCurrencyOptions(terminal.getValue("CD_TERM_CURRENCY"), true) %></select></td>
				<td><%= Bean.terminalXML.getfieldTransl("can_calc_point", false) %></td> <td><select name="can_calc_point"  class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", terminal.getValue("CAN_CALC_POINT"), true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("sname_term_currency", false) %></td> <td><input type="text" name="sname_term_currency" size="16" value="<%=terminal.getValue("SNAME_TERM_CURRENCY") %>" class="inputfield"></td>
				<td><%= Bean.terminalXML.getfieldTransl("sname_point", false) %></td> <td><input type="text" name="sname_point" size="16" value="<%=terminal.getValue("SNAME_POINT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("can_oper_different_currency", true) %></td> <td><select name="can_oper_different_currency"  class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", terminal.getValue("CAN_OPER_DIFFERENT_CURRENCY"), true) %></select></td>
				<td><%= Bean.terminalXML.getfieldTransl("multiplicator_point", false) %></td> <td><input type="text" name="multiplicator_point" size="16" value="<%=terminal.getValue("MULTIPLICATOR_POINT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>

			<tr>
				<td colspan="4"><b><%= Bean.terminalXML.getfieldTransl("h_need_online_pin", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_bon_pay_pin", false) %></td> <td><select name="need_calc_online_bon_pay_pin"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_CALC_ONLINE_BON_PAY_PIN"), true) %></select></td>
				<td colspan="2"><%= Bean.terminalXML.getfieldTransl("need_calc_online_club_pay_pin", true) %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("need_calc_online_storno_pin", false) %></td> <td><select name="need_calc_online_storno_pin"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("NEED_CALC_ONLINE_STORNO_PIN"), true) %></select></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<% } else { %>

			<tr>
				<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_currencies_param", false) %></b></td>
				<td colspan="2"><b><%= Bean.terminalXML.getfieldTransl("h_print_cheque_settings", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("cd_term_currency", true) %></td> <td><select name="cd_term_currency" class="inputfield"><%= Bean.getCurrencyOptions(terminal.getValue("CD_TERM_CURRENCY"), true) %></select></td>
				<td><%=Bean.getCheckBox("oper_save_cheque", terminal.getValue("OPER_SAVE_CHEQUE"), Bean.terminalXML.getfieldTransl("oper_save_cheque", false)) %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("sname_term_currency", false) %></td> <td><input type="text" name="sname_term_currency" size="16" value="<%=terminal.getValue("SNAME_TERM_CURRENCY") %>" class="inputfield"></td>
				<td><%=Bean.getCheckBox("oper_sms_cheque", terminal.getValue("OPER_SMS_CHEQUE"), Bean.terminalXML.getfieldTransl("oper_sms_cheque", false)) %></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="4"><b><%= Bean.terminalXML.getfieldTransl("h_online_confirmations", false) %></b></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_cash_conf_type", false) %></td> <td valign="top"><select name="cd_online_pay_cash_conf_type" class="inputfield"><%= Bean.getTermPayConfirmationWayOptions(terminal.getValue("cd_online_pay_cash_conf_type"), true) %></select></td>
				<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_rbk_conf_type", false) %></td> <td valign="top"><select name="cd_online_pay_rbk_conf_type" class="inputfield"><%= Bean.getTermPayConfirmationWayOptions(terminal.getValue("cd_online_pay_rbk_conf_type"), true) %></select></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_card_conf_type", false) %></td> <td valign="top"><select name="cd_online_pay_card_conf_type" class="inputfield"><%= Bean.getTermPayConfirmationWayOptions(terminal.getValue("cd_online_pay_card_conf_type"), true) %></select></td>
				<td valign="top"><%= Bean.terminalXML.getfieldTransl("has_online_pay_rbk_permission", false) %></td> <td valign="top"><select name="has_online_pay_rbk_permission" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", terminal.getValue("has_online_pay_rbk_permission"), true) %></select></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.terminalXML.getfieldTransl("cd_online_pay_point_conf_type", false) %></td> <td valign="top"><select name="cd_online_pay_point_conf_type" class="inputfield"><%= Bean.getTermPayConfirmationWayOptions(terminal.getValue("cd_online_pay_point_conf_type"), true) %></select></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<% } %>
			<tr>
				<td colspan="4">&nbsp;</td>
			</tr>

			<tr><td colspan="4"><b>Сессионные параметры&nbsp;<span onclick="show_term_addit_param();" id="addit_button">&gt;&gt;</span></b></td></tr>
			<tr id="addit_row1" style="display:none">
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("IS_OPENED_SESSION", false) %></td><td class="gray_background"><select name="IS_OPENED_SESSION" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", terminal.getValue("IS_OPENED_SESSION"), false) %></select></td>
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("ID_LAST_SES", false) %>
					<%= Bean.getGoToTermSessionLink(terminal.getValue("ID_LAST_SES")) %>
				</td><td class="gray_background"><input type="text" name="ID_LAST_SES" size="20" value="<%= terminal.getValue("ID_LAST_SES") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr id="addit_row2" style="display:none">
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("ERROR_MESSAGES_CNT", false) %></td><td class="gray_background"><input type="text" name="ERROR_MESSAGES_CNT" size="8" value="<%=terminal.getValue("ERROR_MESSAGES_CNT")%>" class="inputfield"></td>
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("DATE_LAST_SES", false) %></td><td class="gray_background"><input type="text" name="DATE_LAST_SES" size="20" value="<%= terminal.getValue("DATE_LAST_SES_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr id="addit_row3" style="display:none">
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("DATE_LAST_TELGR", false) %></td><td class="gray_background"><input type="text" name="DATE_LAST_TELGR" size="20" value="<%= terminal.getValue("DATE_LAST_TELGR_FRMT")%>" readonly="readonly" class="inputfield-ro"></td>
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("ID_SAM_LAST_SES", false) %>
					<%= Bean.getGoToSAMLink(terminal.getValue("ID_SAM_LAST_SES")) %>
				</td><td class="gray_background"><input type="text" name="ID_SAM_LAST_SES" size="20" value="<%=terminal.getValue("ID_SAM_LAST_SES") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr id="addit_row4" style="display:none">
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("CREATING_APP_TRAR_DATA_DATE", false) %></td><td class="gray_background"><input type="text" name="CREATING_APP_TRAR_DATA_DATE" size="20" value="<%= terminal.getValue("CREATING_APP_TRAR_DATA_DATE_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("NT_SAM_LAST_SES", false) %></td><td class="gray_background"><input type="text" name="NT_SAM_LAST_SES" size="20" value="<%= terminal.getValue("NT_SAM_LAST_SES")%>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr id="addit_row5" style="display:none">
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_term_col_data", false) %></td><td class="gray_background"><input type="text" name="date_last_term_col_data" size="20" value="<%= terminal.getValue("DATE_LAST_TERM_COL_DATA_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("NT_MSG_B_LAST_SES", false) %></td> <td class="gray_background"><input type="text" name="NT_MSG_B_LAST_SES" size="20" value="<%= terminal.getValue("NT_MSG_B_LAST_SES") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr id="addit_row6" style="display:none">
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_term_card_req", false) %></td><td class="gray_background"><input type="text" name="date_last_term_card_req" size="20" value="<%= terminal.getValue("DATE_LAST_TERM_CARD_REQ_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_payment", false) %></td> <td class="gray_background"><input type="text" name="date_last_rec_payment" size="20" value="<%= terminal.getValue("DATE_LAST_REC_PAYMENT_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr id="addit_row7" style="display:none">
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_mov_bon", false) %></td><td class="gray_background"><input type="text" name="date_last_rec_mov_bon" size="20" value="<%= terminal.getValue("DATE_LAST_REC_MOV_BON_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_chk_card", false) %></td> <td class="gray_background"><input type="text" name="date_last_rec_chk_card" size="20" value="<%= terminal.getValue("DATE_LAST_REC_CHK_CARD_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr id="addit_row8" style="display:none">
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_inval_card", false) %></td><td class="gray_background"><input type="text" name="date_last_rec_inval_card" size="20" value="<%= terminal.getValue("DATE_LAST_REC_INVAL_CARD_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
				<td class="gray_background"><%= Bean.terminalXML.getfieldTransl("date_last_rec_storno_bon", false) %></td> <td class="gray_background"><input type="text" name="date_last_rec_storno_bon" size="20" value="<%= terminal.getValue("DATE_LAST_REC_STORNO_BON_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>

			<%=	Bean.getIdCreationAndMoficationRecordFields(
					terminal.getValue("ID_TERM"),
					terminal.getValue(Bean.getCreationDateFieldName()),
					terminal.getValue("CREATED_BY"),
					terminal.getValue(Bean.getLastUpdateDateFieldName()),
					terminal.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", "div_data_detail") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton(backLink) %>
				</td>
			</tr>
		</table>
		</form> 

			<%= Bean.getCalendarScript("club_registration_date", false) %>
			<%= Bean.getCalendarScript("date_location", false) %>
			<% if (isPhisicalTerminal) { %>
			<%= Bean.getCalendarScript("term_mon_rep_next_date", true) %>
			<% } %>
	<%
    	} else {
    	    %><%= Bean.getUnknownActionText(action) %><%
    	}

	} else if (process.equalsIgnoreCase("yes")) {
    
		String
    		description						= Bean.getDecodeParam(parameters.get("description")),
    		term_serial_number				= Bean.getDecodeParam(parameters.get("term_serial_number")),
    		cd_term_status					= Bean.getDecodeParam(parameters.get("cd_term_status")),
    		id_finance_acquirer				= Bean.getDecodeParam(parameters.get("id_finance_acquirer")),
    		need_sys_lock					= Bean.getDecodeParam(parameters.get("need_sys_lock")),
    		cd_term_type					= Bean.getDecodeParam(parameters.get("cd_term_type")),
    		id_device_type					= Bean.getDecodeParam(parameters.get("id_device_type")),
    		id_technical_acquirer			= Bean.getDecodeParam(parameters.get("id_technical_acquirer")),
    		id_club			 				= Bean.getDecodeParam(parameters.get("id_club")),
    		club_registration_date			= Bean.getDecodeParam(parameters.get("club_registration_date")),
    		id_term_owner					= Bean.getDecodeParam(parameters.get("id_term_owner")),
    		term_code_page					= Bean.getDecodeParam(parameters.get("term_code_page")),
			ver_telgr						= Bean.getDecodeParam(parameters.get("ver_telgr")), 
			vk_enc							= Bean.getDecodeParam(parameters.get("vk_enc")), 
			resp_time						= Bean.getDecodeParam(parameters.get("resp_time")), 
			crypt_telgr						= Bean.getDecodeParam(parameters.get("crypt_telgr")), 
			need_update_param				= Bean.getDecodeParam(parameters.get("need_update_param")), 
			tr_limit						= Bean.getDecodeParam(parameters.get("tr_limit")), 
			nincmax							= Bean.getDecodeParam(parameters.get("nincmax")), 
			connect_ekka					= Bean.getDecodeParam(parameters.get("connect_ekka")), 
			term_mon_interval_day			= Bean.getDecodeParam(parameters.get("term_mon_interval_day")), 
			term_mon_rep_next_date			= Bean.getDecodeParam(parameters.get("term_mon_rep_next_date")), 
			noprint_cheque_discount_club	= Bean.getDecodeParam(parameters.get("noprint_cheque_discount_club")), 
			noprint_cheque_bonus_club		= Bean.getDecodeParam(parameters.get("noprint_cheque_bonus_club")), 
			noprint_cheque_mov_bon			= Bean.getDecodeParam(parameters.get("noprint_cheque_mov_bon")), 
			noprint_cheque_check_param		= Bean.getDecodeParam(parameters.get("noprint_cheque_check_param")), 
			noprint_cheque_pay_cash			= Bean.getDecodeParam(parameters.get("noprint_cheque_pay_cash")), 
			noprint_cheque_pay_card			= Bean.getDecodeParam(parameters.get("noprint_cheque_pay_card")), 
			noprint_cheque_pay_bon			= Bean.getDecodeParam(parameters.get("noprint_cheque_pay_bon")), 
			card_type_active_nsmep			= Bean.getDecodeParam(parameters.get("card_type_active_nsmep")), 
			card_type_active_magnetic		= Bean.getDecodeParam(parameters.get("card_type_active_magnetic")), 
			card_type_active_chip			= Bean.getDecodeParam(parameters.get("card_type_active_chip")), 
			card_type_active_emv			= Bean.getDecodeParam(parameters.get("card_type_active_emv")), 
			card_type_active_barcode		= Bean.getDecodeParam(parameters.get("card_type_active_barcode")), 
			oper_type_active_nsmep			= Bean.getDecodeParam(parameters.get("oper_type_active_nsmep")), 
			oper_type_active_cash			= Bean.getDecodeParam(parameters.get("oper_type_active_cash")), 
			oper_type_active_bon			= Bean.getDecodeParam(parameters.get("oper_type_active_bon")), 
			oper_type_active_cheque			= Bean.getDecodeParam(parameters.get("oper_type_active_cheque")), 
			oper_type_active_emv			= Bean.getDecodeParam(parameters.get("oper_type_active_emv")), 
			cd_term_currency				= Bean.getDecodeParam(parameters.get("cd_term_currency")), 
			sname_term_currency				= Bean.getDecodeParam(parameters.get("sname_term_currency")), 
			can_oper_different_currency		= Bean.getDecodeParam(parameters.get("can_oper_different_currency")), 
			can_calc_point					= Bean.getDecodeParam(parameters.get("can_calc_point")), 
			sname_point						= Bean.getDecodeParam(parameters.get("sname_point")), 
			multiplicator_point				= Bean.getDecodeParam(parameters.get("multiplicator_point")), 
			need_calc_online_bon_pay_pin	= Bean.getDecodeParam(parameters.get("need_calc_online_bon_pay_pin")), 
			need_calc_online_storno_pin		= Bean.getDecodeParam(parameters.get("need_calc_online_storno_pin")), 
			
			oper_save_cheque				= Bean.getDecodeParam(parameters.get("oper_save_cheque")), 
			oper_sms_cheque					= Bean.getDecodeParam(parameters.get("oper_sms_cheque")), 
			cd_online_pay_cash_conf_type	= Bean.getDecodeParam(parameters.get("cd_online_pay_cash_conf_type")), 
			cd_online_pay_card_conf_type	= Bean.getDecodeParam(parameters.get("cd_online_pay_card_conf_type")), 
			cd_online_pay_point_conf_type	= Bean.getDecodeParam(parameters.get("cd_online_pay_point_conf_type")), 
			cd_online_pay_rbk_conf_type		= Bean.getDecodeParam(parameters.get("cd_online_pay_rbk_conf_type")), 
			has_online_pay_rbk_permission	= Bean.getDecodeParam(parameters.get("has_online_pay_rbk_permission")),
			
			id_service_place				= Bean.getDecodeParam(parameters.get("id_service_place")), 
			cash_desk_number				= Bean.getDecodeParam(parameters.get("cash_desk_number")), 
			date_location					= Bean.getDecodeParam(parameters.get("date_location")), 
			id_referral_scheme				= Bean.getDecodeParam(parameters.get("id_referral_scheme")), 
			id_loyality_scheme				= Bean.getDecodeParam(parameters.get("id_loyality_scheme")), 
			id_loyality_shedule				= Bean.getDecodeParam(parameters.get("id_loyality_shedule")), 
			autosynchronize_loyality_param	= Bean.getDecodeParam(parameters.get("autosynchronize_loyality_param")), 
			can_test_mode					= Bean.getDecodeParam(parameters.get("can_test_mode")), 
			is_opened_session				= Bean.getDecodeParam(parameters.get("IS_OPENED_SESSION")), 
			error_messages_cnt				= Bean.getDecodeParam(parameters.get("ERROR_MESSAGES_CNT"));

		ArrayList<String> pParam = new ArrayList<String>();
		
    	if (action.equalsIgnoreCase("add")) {
		
    		pParam.add(id_term);
			pParam.add(term_serial_number);
			pParam.add(id_term_owner);
			pParam.add(id_finance_acquirer);
			pParam.add(id_technical_acquirer);
			pParam.add(cd_term_status);
			pParam.add(cd_term_type);
			pParam.add(id_device_type);
			pParam.add(description);
			pParam.add(id_service_place);
			pParam.add(cash_desk_number);
			pParam.add(date_location);
			pParam.add(id_referral_scheme);
			pParam.add(id_loyality_scheme);
			pParam.add(id_loyality_shedule);
			pParam.add(autosynchronize_loyality_param);
			pParam.add(term_code_page);
			pParam.add(resp_time);
			pParam.add(ver_telgr);
			pParam.add(crypt_telgr);
			pParam.add(vk_enc);
			pParam.add(tr_limit);
			pParam.add(nincmax);
			pParam.add(connect_ekka);
			pParam.add(id_club);
			pParam.add(club_registration_date);
			pParam.add(term_mon_interval_day);
			pParam.add(noprint_cheque_discount_club);
			pParam.add(noprint_cheque_bonus_club);
			pParam.add(noprint_cheque_mov_bon);
			pParam.add(noprint_cheque_check_param);
			pParam.add(noprint_cheque_pay_cash);
			pParam.add(noprint_cheque_pay_card);
			pParam.add(noprint_cheque_pay_bon);
			pParam.add(card_type_active_nsmep);
			pParam.add(card_type_active_magnetic);
			pParam.add(card_type_active_chip);
			pParam.add(card_type_active_emv);
			pParam.add(card_type_active_barcode);
			pParam.add(oper_type_active_nsmep);
			pParam.add(oper_type_active_cash);
			pParam.add(oper_type_active_bon);
			pParam.add(oper_type_active_cheque);
			pParam.add(oper_type_active_emv);
			pParam.add(oper_save_cheque);
			pParam.add(oper_sms_cheque);
			pParam.add(cd_online_pay_cash_conf_type);
			pParam.add(cd_online_pay_card_conf_type);
			pParam.add(cd_online_pay_point_conf_type);
			pParam.add(cd_online_pay_rbk_conf_type);
			pParam.add(has_online_pay_rbk_permission);
			pParam.add(cd_term_currency);
			pParam.add(sname_term_currency);
			pParam.add(can_oper_different_currency);
			pParam.add(can_calc_point);
			pParam.add(sname_point);
			pParam.add(multiplicator_point);
			pParam.add(need_calc_online_bon_pay_pin);
			pParam.add(need_calc_online_storno_pin);
			pParam.add(can_test_mode);
			pParam.add(Bean.getDateFormat());

    	 	%>
    		<%= Bean.executeInsertFunction("PACK$TERM_UI.add_term", pParam, backLink + "&id_term=" , "") %>
    		<% 	

     	} else if (action.equalsIgnoreCase("remove")) {
     		
    		pParam.add(id_term);

    	 	%>
    		<%= Bean.executeDeleteFunction("PACK$TERM_UI.delete_term", pParam, generalLink, "") %>
    		<% 	

     	} else if (action.equalsIgnoreCase("edit")) { 
    
     		pParam.add(id_term);
			pParam.add(term_serial_number);
			pParam.add(id_term_owner);
			pParam.add(id_finance_acquirer);
			pParam.add(id_technical_acquirer);
			pParam.add(cd_term_status);
			pParam.add(id_device_type);
			pParam.add(description);
			pParam.add(id_service_place);
			pParam.add(cash_desk_number);
			pParam.add(date_location);
			pParam.add(id_referral_scheme);
			pParam.add(need_sys_lock);
			pParam.add(term_code_page);
			pParam.add(resp_time);
			pParam.add(ver_telgr);
			pParam.add(crypt_telgr);
			pParam.add(vk_enc);
			pParam.add(tr_limit);
			pParam.add(nincmax);
			pParam.add(connect_ekka);
			pParam.add(need_update_param);
			pParam.add(club_registration_date);
			pParam.add(term_mon_interval_day);
			pParam.add(term_mon_rep_next_date);
			pParam.add(noprint_cheque_discount_club);
			pParam.add(noprint_cheque_bonus_club);
			pParam.add(noprint_cheque_mov_bon);
			pParam.add(noprint_cheque_check_param);
			pParam.add(noprint_cheque_pay_cash);
			pParam.add(noprint_cheque_pay_card);			
			pParam.add(noprint_cheque_pay_bon);
			pParam.add(card_type_active_nsmep);
			pParam.add(card_type_active_magnetic);
			pParam.add(card_type_active_chip);
			pParam.add(card_type_active_emv);
			pParam.add(card_type_active_barcode);
			pParam.add(oper_type_active_nsmep);
			pParam.add(oper_type_active_cash);
			pParam.add(oper_type_active_bon);
			pParam.add(oper_type_active_cheque);
			pParam.add(oper_type_active_emv);
			pParam.add(oper_save_cheque);
			pParam.add(oper_sms_cheque);
			pParam.add(cd_online_pay_cash_conf_type);
			pParam.add(cd_online_pay_card_conf_type);
			pParam.add(cd_online_pay_point_conf_type);
			pParam.add(cd_online_pay_rbk_conf_type);
			pParam.add(has_online_pay_rbk_permission);
			pParam.add(cd_term_currency);
			pParam.add(sname_term_currency);
			pParam.add(can_oper_different_currency);
			pParam.add(can_calc_point);
			pParam.add(sname_point);
			pParam.add(multiplicator_point);
			pParam.add(need_calc_online_bon_pay_pin);
			pParam.add(need_calc_online_storno_pin);
			pParam.add(is_opened_session);
			pParam.add(error_messages_cnt);
			pParam.add(can_test_mode);
			pParam.add(Bean.getDateFormat());
			
			%>
			<%= Bean.executeUpdateFunction("PACK$TERM_UI.update_term", pParam, backLink, "") %>
			
			<%
    	} else { %><%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {%><%= Bean.getUnknownProcessText(process) %><%
  	}
}  else if (type.equalsIgnoreCase("sam")) {
	%> 
	<% 
	  if (process.equalsIgnoreCase("no"))
	  /* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add")) { 
	    		bcTerminalObject terminal = new bcTerminalObject(id_term);
	    		terminal.getFeature();
	    		
	    		String cdTermType = terminal.getValue("CD_TERM_TYPE");
	    		boolean isWebPosTerminal = "WEBPOS".equalsIgnoreCase(cdTermType);
	    		boolean isPhisicalTerminal = "PHYSICAL".equalsIgnoreCase(cdTermType);
	    		
	    		%>
 
				<%= Bean.getOperationTitleShort(
						"",
						Bean.terminalXML.getfieldTransl("h_sam_add", false),
						"Y",
						"Y") 
				%>
		<script>
			var formData = new Array (
				new Array ('id_sam', 'varchar2', 1),
				new Array ('cd_sam_status', 'varchar2', 1),
				<% if (isPhisicalTerminal) { %>
				new Array ('check_mac_icc', 'varchar2', 1),
				new Array ('check_mac_pda', 'varchar2', 1),
				<% }%>
				new Array ('assign_term_date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="sam">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_term" value="<%=id_term %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("id_term", false) %></td><td><input type="text" name="id_term" size="25" value="<%= id_term %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("t_sucurity_module", true) %></td><td><select name="id_sam" class="inputfield"><%= Bean.getSAMNotUsedOptions("", false) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("cd_sam_status", true) %></td><td><select name="cd_sam_status" id="cd_sam_status" class="inputfield"><%= Bean.getSAMStatusOptions("", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("assign_term_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("assign_term_date_beg", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("assign_term_date_end", false) %></td> <td><%=Bean.getCalendarInputField("assign_term_date_end", "", "10") %></td>
			</tr>
			<% if (isPhisicalTerminal) { %>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("check_mac_icc", true) %></td><td><select name="check_mac_icc" id="check_mac_icc" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("check_mac_pda", true) %></td><td><select name="check_mac_pda" id="check_mac_pda" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
			</tr>
			<% }%>
			<tr>
				<td colspan="2"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/clients/terminalspecs.jsp?id=" + id_term) %>
				</td>
			</tr>
		</table>
		</form>

		<%= Bean.getCalendarScript("assign_term_date_beg", false) %>
		<%= Bean.getCalendarScript("assign_term_date_end", false) %>

		        <%
	    	} else if (action.equalsIgnoreCase("edit")) { 
	    		String id_term_sam				= Bean.getDecodeParam(parameters.get("id_term_sam"));
	    		bcTerminalSAMObject termSAM = new bcTerminalSAMObject(id_term_sam);

	    		bcTerminalObject terminal = new bcTerminalObject(termSAM.getValue("ID_TERM"));
	    		terminal.getFeature();
	    		
	    		String cdTermType = terminal.getValue("CD_TERM_TYPE");
	    		boolean isWebPosTerminal = "WEBPOS".equalsIgnoreCase(cdTermType);
	    		boolean isPhisicalTerminal = "PHYSICAL".equalsIgnoreCase(cdTermType);
	    		
	    		%>
 
				<%= Bean.getOperationTitleShort(
						"",
						Bean.terminalXML.getfieldTransl("h_sam_update", false),
						"Y",
						"Y") 
				%>
		<script>
			var formData = new Array (
				new Array ('cd_sam_status', 'varchar2', 1),
				<% if (isPhisicalTerminal) { %>
				new Array ('check_mac_icc', 'varchar2', 1),
				new Array ('check_mac_pda', 'varchar2', 1),
				<% }%>
				new Array ('assign_term_date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="sam">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_term" value="<%=id_term %>">
	        <input type="hidden" name="id_term_sam" value="<%=id_term_sam %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("id_term", false) %></td><td><input type="text" name="id_term" size="25" value="<%= termSAM.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("t_sucurity_module", false) %></td><td><input type="text" name="id_sam" size="25" value="<%= termSAM.getValue("ID_SAM") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("cd_sam_status", true) %></td><td><select name="cd_sam_status" id="cd_sam_status" class="inputfield"><%= Bean.getSAMStatusOptions(termSAM.getValue("CD_SAM_STATUS"), true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("assign_term_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("assign_term_date_beg", termSAM.getValue("ASSIGN_TERM_DATE_BEG_DF"), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("assign_term_date_end", false) %></td> <td><%=Bean.getCalendarInputField("assign_term_date_end", termSAM.getValue("ASSIGN_TERM_DATE_END_DF"), "10") %></td>
			</tr>
			<% if (isPhisicalTerminal) { %>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("check_mac_icc", true) %></td><td><select name="check_mac_icc" id="check_mac_icc" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", termSAM.getValue("CHECK_MAC_ICC"), true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.terminalXML.getfieldTransl("check_mac_pda", true) %></td><td><select name="check_mac_pda" id="check_mac_pda" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", termSAM.getValue("CHECK_MAC_PDA"), true) %></select></td>
			</tr>
			<% }%>
			<tr>
				<td colspan="2"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/clients/terminalspecs.jsp?id=" + id_term) %>
				</td>
			</tr>
		</table>
		</form>

		<%= Bean.getCalendarScript("assign_term_date_beg", false) %>
		<%= Bean.getCalendarScript("assign_term_date_end", false) %>

		        <%
	    	} else {
	    	    %><%= Bean.getUnknownActionText(process) %><%
	    	}
	
	} else if (process.equalsIgnoreCase("yes")) {
	    String
	    	id_sam					= Bean.getDecodeParam(parameters.get("id_sam")),
	    	id_term_sam				= Bean.getDecodeParam(parameters.get("id_term_sam")),
	    	cd_sam_status			= Bean.getDecodeParam(parameters.get("cd_sam_status")),
	    	assign_term_date_beg	= Bean.getDecodeParam(parameters.get("assign_term_date_beg")),
	    	assign_term_date_end	= Bean.getDecodeParam(parameters.get("assign_term_date_end")),
	    	check_mac_icc			= Bean.getDecodeParam(parameters.get("check_mac_icc")),
	    	check_mac_pda			= Bean.getDecodeParam(parameters.get("check_mac_pda"));
	    
	    ArrayList<String> pParam = new ArrayList<String>();
	    
		if (action.equalsIgnoreCase("add")) {
	    	 		 	    	      				
	    	pParam.add(id_term);
	    	pParam.add(id_sam);
	    	pParam.add(cd_sam_status);
	    	pParam.add(assign_term_date_beg);
	    	pParam.add(assign_term_date_end);
	    	pParam.add(check_mac_icc);
	    	pParam.add(check_mac_pda);
	    	pParam.add(Bean.getDateFormat());

    		%>
    		<%= Bean.executeUpdateFunction("PACK$TERM_UI.assign_sam", pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
    		<% 	
	    } else if (action.equalsIgnoreCase("remove")) {
     		 	    	      				
     	    pParam.add(id_term_sam);

		 	%>
			<%= Bean.executeUpdateFunction("PACK$TERM_UI.detach_sam", pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
			<% 	

	    } else if (action.equalsIgnoreCase("edit")) {
	    	 		 	    	      				
	    	pParam.add(id_term_sam);
	    	pParam.add(cd_sam_status);
	    	pParam.add(assign_term_date_beg);
	    	pParam.add(assign_term_date_end);
	    	pParam.add(check_mac_icc);
	    	pParam.add(check_mac_pda);
	    	pParam.add(Bean.getDateFormat());

    		%>
    		<%= Bean.executeUpdateFunction("PACK$TERM_UI.update_term_sam", pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
    		<% 	
	    } else  { %><%= Bean.getUnknownProcessText(process) %><% 
	    }
	} else {
	    %><%= Bean.getUnknownProcessText(process) %><%
	}
} else if (type.equalsIgnoreCase("seans")) {
	%> 
	<body>
	<% 
	
	String 
		l_is_opened_session		= Bean.getDecodeParam(parameters.get("IS_OPENED_SESSION")),
		l_error_messages_cnt	= Bean.getDecodeParam(parameters.get("ERROR_MESSAGES_CNT"));
	
	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_UI.set_session_param(?,?,?,?)}";

 	String[] pParam = new String [3];
 		 	    	      				
 	pParam[0] = id_term;
 	pParam[1] = l_is_opened_session;
 	pParam[2] = l_error_messages_cnt;

 	%>
	<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
	<% 	

} else if (type.equalsIgnoreCase("loy")) {
	%> 
	<body>
	<% 
	System.out.println("111");
	String 
		id_loyality_scheme_next		= Bean.getDecodeParam(parameters.get("id_loyality_scheme_next")),
		id_shedule					= Bean.getDecodeParam(parameters.get("id_shedule")),
		autosynchronize_loy_param	= Bean.getDecodeParam(parameters.get("autosynchronize_loy_param"));
	
	ArrayList<String> pParam = new ArrayList<String>();
			 	    	      			 
 	pParam.add(id_term);
	pParam.add(id_loyality_scheme_next);
	pParam.add(id_shedule);
	pParam.add(autosynchronize_loy_param);

 	%>
	<%= Bean.executeUpdateFunction("PACK$TERM_UI.update_term_loyality", pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
	<% 	
   
} else if (type.equalsIgnoreCase("certificate")) {
	%> 
	<body>
	<% 
	
	String 
		id_next_certificate			= Bean.getDecodeParam(parameters.get("id_next_certificate")),
		need_update_certificate		= Bean.getDecodeParam(parameters.get("need_update_certificate"));
	
	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_UI.set_next_certificate(?,?,?,?)}";

 	String[] pParam = new String [3];
 		 	    	      				
 	pParam[0] = id_term;
	pParam[1] = id_next_certificate;
	pParam[2] = need_update_certificate;

 	%>
	<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
	<% 	

} else if (type.equalsIgnoreCase("message")) {

	String	id_term_message_receiver	= Bean.getDecodeParam(parameters.get("id_term_message_receiver"));

	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) { %> 
			
			<%= Bean.getOperationTitleShort(
					"",
					Bean.messageXML.getfieldTransl("h_message_add", false),
					"Y",
					"Y") 
			%>

			<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>
        <script>
			var formData = new Array (
				new Array ('id_term', 'varchar2', 1),
				new Array ('text_message', 'varchar2', 1),
				new Array ('basis_for_operation', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1),
				new Array ('to_send', 'varchar2', 1)
			);
		</script>

	        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
		        <input type="hidden" name="type" value="message">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id_term" value="<%=id_term %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("text_message", true) %></td><td valign="top" rowspan="3"><textarea name="text_message" id="text_message" cols="60" rows="3" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
				<td><%= Bean.card_taskXML.getfieldTransl("id_term", false) %></td> 
				<td>
					<input type="text" name="id_term" size="16" value="<%=id_term %>" readonly="readonly" class="inputfield-ro">
				</td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("begin_send_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("end_send_date", false) %></td> <td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.messageXML.getfieldTransl("repetitions_count", false) %></td><td><input type="text" name="repetitions_count" size="16" value="1" class="inputfield"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("basis_for_operation", true) %></td><td valign="top"><textarea name="basis_for_operation" cols="60" rows="3" class="inputfield"></textarea></td>
				<td><%= Bean.messageXML.getfieldTransl("to_send", true) %> </td> <td align="left"><select name="to_send" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/clients/terminalspecs.jsp?id=" + id_term) %>
				</td>
			</tr>
		</table>
		</form>
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

        <%
		} else if (action.equalsIgnoreCase("edit")) { 

			bcTerminalMessageReceiverObject message = new bcTerminalMessageReceiverObject(id_term_message_receiver);
			%> 
		<script>
			var formData = new Array (
				new Array ('to_send', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
				<%= Bean.getOperationTitleShort(
						"",
						Bean.messageXML.getfieldTransl("h_message_edit", false),
						"Y",
						"Y") 
				%>
	
				<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>
	
	        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
		        <input type="hidden" name="type" value="message">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id_term" value="<%=id_term %>">
		        <input type="hidden" name="id_term_message_receiver" value="<%=id_term_message_receiver %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top" rowspan="3"><textarea name="text_message" id="text_message" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= message.getValue("TEXT_TERM_MESSAGE") %></textarea></td>
					<td><%= Bean.card_taskXML.getfieldTransl("id_term", false) %></td> 
					<td>
						<input type="text" name="id_term" size="20" value="<%= message.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro">
					</td>
				</tr>
				<tr>
					<td valign="top"><%= Bean.messageXML.getfieldTransl("sendings_quantity", false) %></td><td valign="top"><input type="text" name="sendings_quantity" size="20" value="<%= message.getValue("SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td valign="top"><%= Bean.messageXML.getfieldTransl("last_send_date", false) %></td><td valign="top"><input type="text" name="last_send_date" size="20" value="<%= message.getValue("LAST_SEND_DATE_DHMSF") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td valign="top"><%= Bean.messageXML.getfieldTransl("begin_send_date", false) %></td><td valign="top"><input type="text" name="begin_send_date" size="16" value="<%= message.getValue("BEGIN_ACTION_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.messageXML.getfieldTransl("to_send", true) %> </td> <td align="left"><select name="to_send" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", message.getValue("TO_SEND"), true) %></select></td>
				</tr>
				<tr>
					<td valign="top"><%= Bean.messageXML.getfieldTransl("end_send_date", false) %></td><td valign="top"><input type="text" name="end_send_date" size="16" value="<%= message.getValue("END_ACTION_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<%=	Bean.getIdCreationAndMoficationRecordFields(
						message.getValue("ID_TERM_MESSAGE_RECEIVER"),
						message.getValue(Bean.getCreationDateFieldName()),
						message.getValue("CREATED_BY"),
						message.getValue(Bean.getLastUpdateDateFieldName()),
						message.getValue("LAST_UPDATE_BY")
				) %>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/clients/terminalspecs.jsp?id=" + id_term) %>
					</td>
				</tr>
			</table>
			</form>
				
				<div id="div_oper_caption"><center><b><%=Bean.messageXML.getfieldTransl("h_message_send_info", false) %></b></center></div>
				
				<% 
				String tagMessageAction = "_MESSAGE_ACTION";
				//Обрабатываем номера страниц
				String l_action_page = Bean.getDecodeParam(parameters.get("action_page"));
				if (Bean.isEmpty(l_action_page)) {
					l_action_page = "1";
				}
				Bean.pageCheck(pageFormName + tagMessageAction, l_action_page);
				String l_action_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessageAction);
				String l_action_page_end = Bean.getLastRowNumber(pageFormName + tagMessageAction);
				%>
				<table <%=Bean.getTableBottomFilter() %>>
					<tr>
						<td>&nbsp;</td>
						<%= Bean.getPagesHTML(pageFormName + tagMessageAction, "../crm/clients/terminalupdate.jsp?type=message&action=edit&process=no&id_term=" + id_term + "&id_term_message_receiver=" + id_term_message_receiver + "&", "action_page", "", "div_data_detail") %>
					</tr>
				</table>
				<%=message.getTermMessagesSendHTML("", l_action_page_beg, l_action_page_end) %>
		<%

    	} else {%><%= Bean.getUnknownActionText(action) %><%
    	}
	} else if (process.equalsIgnoreCase("yes")){
	    String
	    	text_message		= Bean.getDecodeParam(parameters.get("text_message")),
	    	begin_action_date	= Bean.getDecodeParam(parameters.get("begin_action_date")),
	    	end_action_date		= Bean.getDecodeParam(parameters.get("end_action_date")),
	    	repetitions_count	= Bean.getDecodeParam(parameters.get("repetitions_count")),
	    	basis_for_operation	= Bean.getDecodeParam(parameters.get("basis_for_operation")),
	    	to_send				= Bean.getDecodeParam(parameters.get("to_send"));

	    ArrayList<String> pParam = new ArrayList<String>();
	    
		if (action.equalsIgnoreCase("add")) {
				
			pParam.add("TERMINAL_PARAMETERS");
			pParam.add(id_term);
			pParam.add(text_message);
			pParam.add(begin_action_date);
			pParam.add(end_action_date);
			pParam.add(repetitions_count);
			pParam.add(basis_for_operation);
			pParam.add("NEW");
			pParam.add("N");
			pParam.add(to_send);
			pParam.add("");
			pParam.add(Bean.getDateFormat());

			%>
			<%= Bean.executeInsertFunction("PACK$TERM_UI.add_term_message", pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term + "&id_message=", "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
		    
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_UI.update_term_message_receiver(" + 
	    		"?,?,?,?,?,?,?,?,?,?)}";

			pParam.add(id_term_message_receiver);
			pParam.add(to_send);

			%>
			<%= Bean.executeUpdateFunction("PACK$TERM_UI.update_term_message_receiver", pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
			<% 	

		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%><%= Bean.getUnknownProcessText(process) %><%
	}
	
}   else if (type.equalsIgnoreCase("current_param")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("edit")) {  
		    String
		    id_param_history	= Bean.getDecodeParam(parameters.get("id_param_history")),
		    resp_time			= Bean.getDecodeParam(parameters.get("resp_time_history")),
		    ver_telgr			= Bean.getDecodeParam(parameters.get("ver_telgr_history")),
		    crypt_telgr			= Bean.getDecodeParam(parameters.get("crypt_telgr_history")),
		    vk_enc				= Bean.getDecodeParam(parameters.get("vk_enc_history"));

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_UI.update_history_param("+ 
				"?,?,?,?,?,?,?)}";

		 	String[] pParam = new String [6];
		 		 	    	      				
		 	pParam[0] = id_term;
			pParam[1] = id_param_history;
			pParam[2] = resp_time;
			pParam[3] = ver_telgr;
			pParam[4] = crypt_telgr;
			pParam[5] = vk_enc;

			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
			<% 	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %><%
	}
} else if (type.equalsIgnoreCase("relationship")) {
	
	String id_club_rel = Bean.getDecodeParam(parameters.get("id_club_rel"));
	if (process.equalsIgnoreCase("no")){
		if (action.equalsIgnoreCase("addneeded")) {

			    	bcClubRelationshipObject rel = new bcClubRelationshipObject("NEEDED", id_club_rel);
		    		String rel_type = rel.getValue("CD_CLUB_REL_TYPE");
		    		
			        %>

				<body>
					<%= Bean.getOperationTitle(
							Bean.relationshipXML.getfieldTransl("h_add_relationship", false),
							"Y",
							"Y") 
					%>

				<script language="JavaScript">
					<%= rel.getClubRelCheckScript(rel_type)%>
				</script>

		        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
			        <input type="hidden" name="type" value="relationship">
			        <input type="hidden" name="action" value="add">
			        <input type="hidden" name="process" value="yes">
			        <input type="hidden" name="id_term" value="<%= id_term %>">
					<input type="hidden" name="cd_club_rel_type" value="<%= rel_type %>">
				<table <%=Bean.getTableDetailParam() %>>
				<%=rel.getClubRelAddHTML(rel.getValue("ID_CLUB"), rel_type, action, "", Bean.getSysDate(), Bean.getDateFormatTitle()) %>
		 		<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/clients/terminalspecs.jsp?rel_kind=NEEDED&id=" + id_term) %>
					</td> 
				</tr>
			</table>

			</form> 
			<%= Bean.getCalendarScript("date_club_rel", false) %>

			        <%
			        /*  --- Видалити запис --- */
		    	} else if (action.equalsIgnoreCase("edit")) {

		    		bcClubRelationshipObject rel = new bcClubRelationshipObject("CREATED", id_club_rel);
		    		String rel_type = rel.getValue("CD_CLUB_REL_TYPE");
		    	    %> 
	<script language="JavaScript">
		<%= rel.getClubRelCheckScript(rel_type)%>
	</script>
		<%= Bean.getOperationTitle(
				Bean.relationshipXML.getfieldTransl("h_edit_relationship", false),
				"Y",
				"Y") 
		%>

    <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="relationship">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_term" value="<%=id_term%>">
	<table <%=Bean.getTableDetailParam() %>>
		<%=rel.getClubRelEditHTML(Bean.getDateFormatTitle()) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/terminalspecs.jsp?rel_kind=CREATED&id=" + id_term) %>
			</td>
		</tr>
	</table>

	</form> 
	<%= Bean.getCalendarScript("date_club_rel", false) %>
		<%

		   	} else {
		   	    %> <%= Bean.getUnknownActionText(action) %><%
		   	}

		} else if (process.equalsIgnoreCase("yes"))	{
		    
			String
				cd_club_rel_type 				= Bean.getDecodeParam(parameters.get("cd_club_rel_type")),
				date_club_rel 					= Bean.getDecodeParam(parameters.get("date_club_rel")),
				desc_club_rel 					= Bean.getDecodeParam(parameters.get("desc_club_rel")),
				id_party1 						= Bean.getDecodeParam(parameters.get("id_party1")),
				id_party1_settlem_accnt 		= Bean.getDecodeParam(parameters.get("id_party1_settlem_accnt")),
				id_party1_club_distrib_accnt 	= Bean.getDecodeParam(parameters.get("id_party1_club_distrib_accnt")),
				id_party1_club_bon_accnt		= Bean.getDecodeParam(parameters.get("id_party1_club_bon_accnt")),
				id_party2						= Bean.getDecodeParam(parameters.get("id_party2")),
				id_party2_settlem_accnt 		= Bean.getDecodeParam(parameters.get("id_party2_settlem_accnt")),
				id_club					 		= Bean.getDecodeParam(parameters.get("id_club"));

			if (action.equalsIgnoreCase("add")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.add_relationship(" +
					"?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			 	String[] pParam = new String [11];
			 		 	    	      				
			 	pParam[0] = cd_club_rel_type;
				pParam[1] = date_club_rel;
				pParam[2] = desc_club_rel;
				pParam[3] = id_party1;
				pParam[4] = id_party1_settlem_accnt;
				pParam[5] = id_party1_club_distrib_accnt;
				pParam[6] = id_party1_club_bon_accnt;
				pParam[7] = id_party2;
				pParam[8] = id_party2_settlem_accnt;
				pParam[9] = id_club;
				pParam[10] = Bean.getDateFormat();
				
			 	%>
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/clients/terminalspecs.jsp?type=CREATED&id=" + id_term + "&id_club_rel=", "../crm/clients/terminalspecs.jsp?id=" + id_term) %>
				<% 	

			} else if (action.equalsIgnoreCase("remove")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.delete_relationship(?,?)}";

				String[] pParam = new String [1];
					 		 	    	      				
				pParam[0] = id_club_rel;

			 	%>
				<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
				<% 	

			} else if (action.equalsIgnoreCase("edit")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.update_relationship(" + 
					"?,?,?,?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [10];
				 		 	    	      				
				pParam[0] = id_club_rel;
				pParam[1] = date_club_rel;
				pParam[2] = desc_club_rel;
				pParam[3] = id_party1;
				pParam[4] = id_party1_settlem_accnt;
				pParam[5] = id_party1_club_distrib_accnt;
				pParam[6] = id_party1_club_bon_accnt;
				pParam[7] = id_party2;
				pParam[8] = id_party2_settlem_accnt;
				pParam[9] = Bean.getDateFormat();
				
			 	%>
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
				<% 	

				} else { %> 
					<%= Bean.getUnknownActionText(action) %><% 
				}
			} else {
		    	%> <%= Bean.getUnknownProcessText(process) %> <%
			}

} else if (type.equalsIgnoreCase("online_type")) {
	
	String 
		id_term_online_pay_type 		= Bean.getDecodeParam(parameters.get("id_term_online_pay_type")),
		id_club_online_pay_type 		= Bean.getDecodeParam(parameters.get("id_club_online_pay_type")),
		term_card_req_club_pay_id 		= Bean.getDecodeParam(parameters.get("term_card_req_club_pay_id")),
		exist_term_online_pay_type 		= Bean.getDecodeParam(parameters.get("exist_term_online_pay_type")),
		need_calc_pin 					= Bean.getDecodeParam(parameters.get("need_calc_pin"));

	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add1")) {
    		
			%>
			<script>
				var formData = new Array (
					new Array ('id_club_online_pay_type', 'varchar2', 1),
					new Array ('name_club_online_pay_type', 'varchar2', 1),
					new Array ('exist_club_online_pay_type', 'varchar2', 1),
					new Array ('term_card_req_club_pay_id_def', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitle(
				Bean.clubXML.getfieldTransl("h_add_online_payment_type", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="online_type">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_term" value="<%=id_term %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_club_online_pay_type", true) %> </td><td><input type="text" name="name_club_online_pay_type" size="60" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("term_card_req_club_pay_id", true) %> </td><td><input type="text" name="term_card_req_club_pay_id" size="20" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubXML.getfieldTransl("desc_club_online_pay_type", false) %></td> <td rowspan="3"><textarea name="desc_club_online_pay_type" cols="57" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.clubXML.getfieldTransl("need_calc_pin", false) %> </td><td><select name="need_calc_pin" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("exist_club_online_pay_type", true) %> </td><td><select name="exist_club_online_pay_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
		</tr>
	    <tr>
			<td colspan="2">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/terminalspecs.jsp?id=" + id_term) %>
			</td>
		</tr>

	</table>
	</form>

	        <%
    	} else if (action.equalsIgnoreCase("edit")) {
    		
    		bcTerminalOnlinePaymentTypeObject pay = new bcTerminalOnlinePaymentTypeObject(id_term_online_pay_type);
	        
	        %>
			<script>
				var formData = new Array (
					new Array ('term_card_req_club_pay_id', 'varchar2', 1),
					new Array ('exist_term_online_pay_type', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitle(
				Bean.clubXML.getfieldTransl("h_update_online_payment_type", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/clients/terminalupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="online_type">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_term" value="<%=id_term %>">
	        <input type="hidden" name="id_term_online_pay_type" value="<%=id_term_online_pay_type %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("id_term", false) %> </td><td><input type="text" name="id_term" size="20" value="<%= pay.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("term_card_req_club_pay_id", true) %> </td><td><input type="text" name="term_card_req_club_pay_id" size="20" value="<%= pay.getValue("TERM_CARD_REQ_CLUB_PAY_ID") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("name_club_online_pay_type", false) %> </td><td><input type="text" name="name_club_online_pay_type" size="60" value="<%= pay.getValue("NAME_CLUB_ONLINE_PAY_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("need_calc_pin", false) %> </td><td><select name="need_calc_pin" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", pay.getValue("NEED_CALC_PIN"), true) %></select></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.terminalXML.getfieldTransl("exist_term_online_pay_type", true) %> </td><td><select name="exist_term_online_pay_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", pay.getValue("EXIST_TERM_ONLINE_PAY_TYPE"), true) %></select></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				pay.getValue(Bean.getCreationDateFieldName()),
				pay.getValue("CREATED_BY"),
				pay.getValue(Bean.getLastUpdateDateFieldName()),
				pay.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/terminalupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/terminalspecs.jsp?id=" + id_term) %>
			</td>
		</tr>

	</table>
	</form>
		 
		<%} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	} else if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("add1")) { 

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.add_term_onl_payment_type("+
				"?,?,?,?,?, ?,?)}";

		 	String[] pParam = new String [5];
		 		 	    	      				
		 	pParam[0] = id_term;
			pParam[1] = id_club_online_pay_type;
			pParam[2] = term_card_req_club_pay_id;
			pParam[3] = exist_term_online_pay_type;
			pParam[4] = need_calc_pin;
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term + "&id_term_online_pay_type=", "") %>
			<% 	
	   
		} else if (action.equalsIgnoreCase("remove1")) { 
		   
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.delete_term_onl_payment_type(?,?)}";

			String[] pParam = new String [1];
					 	    	      				
			pParam[0] = id_term_online_pay_type;

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
			<% 	
		     
		} else if (action.equalsIgnoreCase("edit")) { 
			
		 	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.update_term_onl_payment_type(" +
		 		"?,?,?,?, ?)}";

			String[] pParam = new String [4];
			 		 	    	      				
			pParam[0] = id_term_online_pay_type;
			pParam[1] = term_card_req_club_pay_id;
			pParam[2] = exist_term_online_pay_type;
			pParam[3] = need_calc_pin;
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/terminalspecs.jsp?id=" + id_term, "") %>
			<% 	
		
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %>
		<%= Bean.getUnknownProcessText(process) %> <br><% 
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %><%
}
%>


</body>

<%@page import="bc.objects.bcTerminalMessageObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%></html>
