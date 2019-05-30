<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcTerminalDeviceTypeObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

<title></title>
</head>

<% 
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_TERM_DEVICE_TYPE";

Bean.setJspPageForTabName(pageFormName);

String tagTerminals = "_TERMINALS";
String tagTerminalFind = "_TERMINAL_FIND";
String tagTerminalType = "_TERMINAL_TYPE";
String tagTerminalStatus = "_TERMINAL_STATUS";

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null || "".equalsIgnoreCase(id)) { id=""; }
if ("".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcTerminalDeviceTypeObject device = new bcTerminalDeviceTypeObject(id);
	
	//Обрабатываем номера страниц
	
	String l_terminals_page = Bean.getDecodeParam(parameters.get("terminals_page"));
	Bean.pageCheck(pageFormName + tagTerminals, l_terminals_page);
	String l_terminals_page_beg = Bean.getFirstRowNumber(pageFormName + tagTerminals);
	String l_terminals_page_end = Bean.getLastRowNumber(pageFormName + tagTerminals);
	
	String terminal_find 	= Bean.getDecodeParam(parameters.get("terminal_find"));
	terminal_find 	= Bean.checkFindString(pageFormName + tagTerminalFind, terminal_find, l_terminals_page);
	
	String terminal_status	= Bean.getDecodeParam(parameters.get("terminal_status"));
	terminal_status		= Bean.checkFindString(pageFormName + tagTerminalStatus, terminal_status, l_terminals_page);
	
	String terminal_type	= Bean.getDecodeParam(parameters.get("terminal_type"));
	terminal_type		= Bean.checkFindString(pageFormName + tagTerminalType, terminal_type, l_terminals_page);
	

%>
<% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TERM_DEVICE_TYPE_INFO")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_TERM_DEVICE_TYPE_INFO")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club/term_device_typeupdate.jsp?id=" + device.getValue("ID_DEVICE_TYPE") + "&type=general&action=add2&process=no", "", "") %>
				    <%= Bean.getMenuButton("DELETE", "../crm/club/term_device_typeupdate.jsp?id=" + device.getValue("ID_DEVICE_TYPE") + "&type=general&action=remove&process=yes", Bean.terminalXML.getfieldTransl("h_delete_term_device_type", false), device.getValue("ID_DEVICE_TYPE") + " - " + device.getValue("NAME_DEVICE_TYPE")) %>
				<% } %>
			<%  }  %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TERM_DEVICE_TYPE_TERMINALS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTerminals, "../crm/club/term_device_typespecs.jsp?id=" + device.getValue("ID_DEVICE_TYPE") + "&tab="+Bean.currentMenu.getTabID("CLUB_TERM_DEVICE_TYPE_TERM")+"&", "terminals_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(device.getValue("ID_DEVICE_TYPE") + " - " + device.getValue("NAME_DEVICE_TYPE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club/term_device_typespecs.jsp?id=" + id + "&adr=full") %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<%

if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_TERM_DEVICE_TYPE_INFO")) {
%>

	<script language="JavaScript">
		var formData = new Array (
			new Array ('name_device_type', 'varchar2', 1),
			new Array ('name_jur_prs', 'varchar2', 1),
			new Array ('work_with_certificate', 'varchar2', 1),
			new Array ('exist_flag', 'varchar2', 1),
			new Array ('term_code_page', 'varchar2', 1),
			new Array ('ver_telgr', 'varchar2', 1),
			new Array ('resp_time', 'varchar2', 1),
			new Array ('crypt_telgr', 'varchar2', 1),
			new Array ('vk_enc', 'varchar2', 1),
			new Array ('nincmax', 'varchar2', 1),
			new Array ('tr_limit', 'varchar2', 1),
			new Array ('connect_ekka', 'varchar2', 1)
		);
	</script>


    <form action="../crm/club/term_device_typeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=device.getValue("ID_DEVICE_TYPE") %>">
	<table <%=Bean.getTableDetailParam() %>>
	   	<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_type", false) %></td> <td><input type="text" name="name_term_type" size="53" value="<%=device.getValue("NAME_TERM_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("work_with_certificate", true) %></td> <td><select name="work_with_certificate" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("YES_NO", device.getValue("WORK_WITH_CERTIFICATE"), true) %></select></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("name_device_type", true) %></td> <td><input type="text" name="name_device_type" size="53" value="<%=device.getValue("NAME_DEVICE_TYPE") %>" class="inputfield"> </td>
			<td><%= Bean.terminalXML.getfieldTransl("exist_flag", true) %></td> <td><select name="exist_flag" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("YES_NO", device.getValue("EXIST_FLAG"), true) %></select></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("desc_device_type", false) %></td> <td><textarea name="desc_device_type" cols="50" rows="3" class="inputfield"><%=device.getValue("DESC_DEVICE_TYPE") %></textarea></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("terminal_manufacturer", true) %>
				<%=Bean.getGoToJurPrsHyperLink(device.getValue("ID_JUR_PRS_MANUFACTURER")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", device.getValue("ID_JUR_PRS_MANUFACTURER"), device.getValue("SNAME_JUR_PRS"), "TERMINAL_MANUFACTURER", "40") %>
			</td>
   		</tr>
		<tr>
			<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_setting_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("TERM_CODE_PAGE", true) %></td> <td><select name="term_code_page"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("TERMINAL_CODE_PAGE", device.getValue("TERM_CODE_PAGE"), true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("VER_TELGR", true) %></td> <td><select name="ver_telgr" class="inputfield" title="TEL_VERSION"><%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", device.getValue("VER_TELGR"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("CRYPT_TELGR", true) %></td><td><select name="crypt_telgr" class="inputfield" title="CRYPT_TELGR"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", device.getValue("CRYPT_TELGR"), true) %></select></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("nincmax", true) %></td>  <td><input type="text" name="nincmax" size="16" value="<%=device.getValue("NINCMAX") %>" class="inputfield" title="NINCMAX"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("RESP_TIME", true) %></td> <td><input type="text" name="resp_time" size="16" value="<%=device.getValue("RESP_TIME") %>" class="inputfield" title="RESP_TIME"></td>
			<td><%= Bean.terminalXML.getfieldTransl("connect_ekka", true) %></td> <td><select name="connect_ekka" class="inputfield" title="CONNECT_EKKA"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", device.getValue("CONNECT_EKKA"), true) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("VK_ENC", true) %></td> <td valign="top"><select name="vk_enc" class="inputfield" title="VK_ENC"><%= Bean.getMeaningFromLookupNameOptions("VER_KEY", device.getValue("VK_ENC"), true) %></select></td>
			<td valign="top"><%= Bean.terminalXML.getfieldTransl("tr_limit", true) %></td>  <td><input type="text" name="tr_limit" size="16" value="<%=device.getValue("TR_LIMIT") %>" class="inputfield" title="TR_LIMIT"></td>
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
			<td><%=Bean.getCheckBoxNumber("card_type_active_nsmep", device.getValue("CARD_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("card_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_nsmep", device.getValue("OPER_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("oper_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_discount_club", device.getValue("NOPRINT_CHEQUE_DISCOUNT_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_discount_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_cash", device.getValue("NOPRINT_CHEQUE_PAY_CASH"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_cash", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_magnetic", device.getValue("CARD_TYPE_ACTIVE_MAGNETIC"), Bean.terminalXML.getfieldTransl("card_type_active_magnetic", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cash", device.getValue("OPER_TYPE_ACTIVE_CASH"), Bean.terminalXML.getfieldTransl("oper_type_active_cash", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_bonus_club", device.getValue("NOPRINT_CHEQUE_BONUS_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_bonus_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_card", device.getValue("NOPRINT_CHEQUE_PAY_CARD"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_card", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_chip", device.getValue("CARD_TYPE_ACTIVE_CHIP"), Bean.terminalXML.getfieldTransl("card_type_active_chip", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_bon", device.getValue("OPER_TYPE_ACTIVE_BON"), Bean.terminalXML.getfieldTransl("oper_type_active_bon", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_mov_bon", device.getValue("NOPRINT_CHEQUE_MOV_BON"), Bean.terminalXML.getfieldTransl("noprint_cheque_mov_bon", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_emv", device.getValue("CARD_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("card_type_active_emv", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cheque", device.getValue("OPER_TYPE_ACTIVE_CHEQUE"), Bean.terminalXML.getfieldTransl("oper_type_active_cheque", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_check_param", device.getValue("NOPRINT_CHEQUE_CHECK_PARAM"), Bean.terminalXML.getfieldTransl("noprint_cheque_check_param", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_barcode", device.getValue("CARD_TYPE_ACTIVE_BARCODE"), Bean.terminalXML.getfieldTransl("card_type_active_barcode", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_emv", device.getValue("OPER_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("OPER_TYPE_ACTIVE_EMV", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_bon", device.getValue("noprint_cheque_pay_bon"), Bean.terminalXML.getfieldTransl("NOPRINT_CHEQUE_PAY_BON", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				device.getValue("ID_DEVICE_TYPE"),
				device.getValue(Bean.getCreationDateFieldName()),
				device.getValue("CREATED_BY"),
				device.getValue(Bean.getLastUpdateDateFieldName()),
				device.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/term_device_typeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/term_device_type.jsp?") %>
			</td>
		</tr>

	</table>

	</form> 
 <% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_TERM_DEVICE_TYPE_INFO")) { %>
    <form>
 	<table <%=Bean.getTableDetailParam() %>>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_type", false) %></td> <td><input type="text" name="name_term_type" size="53" value="<%=device.getValue("NAME_TERM_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("work_with_certificate", false) %></td> <td><input type="text" name="work_with_certificate" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", device.getValue("WORK_WITH_CERTIFICATE")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("name_device_type", false) %></td> <td><input type="text" name="name_device_type" size="53" value="<%=device.getValue("NAME_DEVICE_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.terminalXML.getfieldTransl("exist_flag", false) %></td> <td><input type="text" name="exist_flag" size="20" value="<%= Bean.getMeaningFoCodeValue("YES_NO", device.getValue("EXIST_FLAG")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("desc_device_type", false) %></td> <td><textarea name="desc_device_type" cols="50" rows="3" readonly="readonly" class="inputfield-ro"><%=device.getValue("DESC_DEVICE_TYPE") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("terminal_manufacturer", false) %></td> <td><input type="text" name="name_jur_prs" size="53" value="<%=device.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_setting_param", false) %></b></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("term_code_page", false) %></td> <td><input type="text" name="ver_telgr" size="15" value="<%= Bean.getMeaningFoCodeValue("TERMINAL_CODE_PAGE", device.getValue("TERM_CODE_PAGE")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("ver_telgr", false) %></td> <td><input type="text" name="ver_telgr" size="15" value="<%= Bean.getMeaningFoCodeValue("VER_TELGR", device.getValue("VER_TELGR")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("crypt_telgr", false) %></td> <td><input type="text" name="crypt_telgr" size="15" value="<%= Bean.getMeaningForNumValue("YES_NO", device.getValue("CRYPT_TELGR")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("nincmax", false) %></td> <td><input type="text" name="nincmax" size="15" value="<%= device.getValue("NINCMAX") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("resp_time", false) %></td> <td><input type="text" name="resp_time" size="15" value="<%=device.getValue("RESP_TIME") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("connect_ekka", false) %></td> <td><input type="text" name="connect_ekka" size="15" value="<%= Bean.getMeaningForNumValue("YES_NO", device.getValue("CONNECT_EKKA")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("vk_enc", false) %></td> <td><input type="text" name="vk_enc" size="15" value="<%= Bean.getMeaningFoCodeValue("VER_KEY", device.getValue("VK_ENC")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("tr_limit", false) %></td> <td><input type="text" name="tr_limit" size="15" value="<%= device.getValue("TR_LIMIT") %>" readonly="readonly" class="inputfield-ro"></td>
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
			<td><%=Bean.getCheckBoxNumberDisabled("card_type_active_nsmep", device.getValue("CARD_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("card_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("oper_type_active_nsmep", device.getValue("OPER_TYPE_ACTIVE_NSMEP"), Bean.terminalXML.getfieldTransl("oper_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("noprint_cheque_discount_club", device.getValue("NOPRINT_CHEQUE_DISCOUNT_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_discount_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("noprint_cheque_pay_cash", device.getValue("NOPRINT_CHEQUE_PAY_CASH"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_cash", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumberDisabled("card_type_active_magnetic", device.getValue("CARD_TYPE_ACTIVE_MAGNETIC"), Bean.terminalXML.getfieldTransl("card_type_active_magnetic", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("oper_type_active_cash", device.getValue("OPER_TYPE_ACTIVE_CASH"), Bean.terminalXML.getfieldTransl("oper_type_active_cash", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("noprint_cheque_bonus_club", device.getValue("NOPRINT_CHEQUE_BONUS_CLUB"), Bean.terminalXML.getfieldTransl("noprint_cheque_bonus_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("noprint_cheque_pay_card", device.getValue("NOPRINT_CHEQUE_PAY_CARD"), Bean.terminalXML.getfieldTransl("noprint_cheque_pay_card", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumberDisabled("card_type_active_chip", device.getValue("CARD_TYPE_ACTIVE_CHIP"), Bean.terminalXML.getfieldTransl("card_type_active_chip", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("oper_type_active_bon", device.getValue("OPER_TYPE_ACTIVE_BON"), Bean.terminalXML.getfieldTransl("oper_type_active_bon", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("noprint_cheque_mov_bon", device.getValue("NOPRINT_CHEQUE_MOV_BON"), Bean.terminalXML.getfieldTransl("noprint_cheque_mov_bon", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumberDisabled("card_type_active_emv", device.getValue("CARD_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("card_type_active_emv", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("oper_type_active_cheque", device.getValue("OPER_TYPE_ACTIVE_CHEQUE"), Bean.terminalXML.getfieldTransl("oper_type_active_cheque", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("noprint_cheque_check_param", device.getValue("NOPRINT_CHEQUE_CHECK_PARAM"), Bean.terminalXML.getfieldTransl("noprint_cheque_check_param", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumberDisabled("card_type_active_barcode", device.getValue("CARD_TYPE_ACTIVE_BARCODE"), Bean.terminalXML.getfieldTransl("card_type_active_barcode", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("oper_type_active_emv", device.getValue("OPER_TYPE_ACTIVE_EMV"), Bean.terminalXML.getfieldTransl("OPER_TYPE_ACTIVE_EMV", false)) %></td>
			<td><%=Bean.getCheckBoxNumberDisabled("noprint_cheque_pay_bon", device.getValue("noprint_cheque_pay_bon"), Bean.terminalXML.getfieldTransl("NOPRINT_CHEQUE_PAY_BON", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				device.getValue("ID_DEVICE_TYPE"),
				device.getValue(Bean.getCreationDateFieldName()),
				device.getValue("CREATED_BY"),
				device.getValue(Bean.getLastUpdateDateFieldName()),
				device.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/club/term_device_type.jsp?") %>
			</td>
		</tr>
	</table>

	</form> 

 <% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_TERM_DEVICE_TYPE_TERMINALS")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%= Bean.getFindHTML("terminal_find", terminal_find, "../crm/club/term_device_typespecs.jsp?id=" + id + "&terminals_page=1&") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("terminal_type", "../crm/club/term_device_typespecs.jsp?id=" + id + "&terminals_page=1", Bean.terminalXML.getfieldTransl("cd_term_type", false)) %>
				<%= Bean.getTermTypeOptions(terminal_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("terminal_status", "../crm/club/term_device_typespecs.jsp?id=" + id + "&terminals_page=1", Bean.terminalXML.getfieldTransl("cd_term_status", false)) %>
				<%= Bean.getTermStatusOptions(terminal_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
		</tbody>
		</table>
	<%= device.getTerminalsHTML(terminal_find, terminal_type, terminal_status, l_terminals_page_beg, l_terminals_page_end) %>
<%}


} %>
</div></div>
</body>
</html>
