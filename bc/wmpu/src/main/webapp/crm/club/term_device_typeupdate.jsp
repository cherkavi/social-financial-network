<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcTerminalDeviceTypeObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_TERM_DEVICE_TYPE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	
	
	if (process.equalsIgnoreCase("no")) {
		%>
			<script>
				var formData = new Array (
					new Array ('cd_term_type', 'varchar2', 1),
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

		<%
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
			
			%> 
			<%= Bean.getOperationTitle(
					Bean.terminalXML.getfieldTransl("h_add_devict_type", false),
					"Y",
					"N") 
			%>
		<form action="../crm/club/term_device_typeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        	<input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
    	    <input type="hidden" name="process" value="yes">
        	<input type="hidden" name="id" value="<%=id %>">
		
	<table <%=Bean.getTableDetailParam() %>>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("cd_term_type", true) %></td> <td><select name="cd_term_type" class="inputfield" > <%= Bean.getTermTypeOptions("", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("work_with_certificate", true) %></td> <td><select name="work_with_certificate" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("name_device_type", true) %></td> <td><input type="text" name="name_device_type" size="53" value="" class="inputfield"> </td>
			<td><%= Bean.terminalXML.getfieldTransl("exist_flag", true) %></td> <td><select name="exist_flag" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("desc_device_type", false) %></td> <td><textarea name="desc_device_type" cols="50" rows="3" class="inputfield"></textarea></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("terminal_manufacturer", true) %></td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", "", "", "TERMINAL_MANUFACTURER", "40") %>
			</td>
   		</tr>
		<tr>
			<td class="top_line" colspan="4"><b><%=Bean.terminalXML.getfieldTransl("h_term_setting_param", false) %></b></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("term_code_page", true) %></td> <td valign="top"><select name="term_code_page" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("TERMINAL_CODE_PAGE", "", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("ver_telgr", true) %></td> <td valign="top"><select name="ver_telgr" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("VER_TELGR", "", true) %></select></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("crypt_telgr", true) %></td> <td valign="top"><select name="crypt_telgr" class="inputfield" > <%= Bean.getMeaningFromLookupNumHTML("YES_NO", "", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("nincmax", true) %></td> <td valign="top"><input type="text" name="nincmax" size="15" value="" class="inputfield"></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("resp_time", true) %></td> <td valign="top"><input type="text" name="resp_time" size="15" value="" class="inputfield"></td>
			<td><%= Bean.terminalXML.getfieldTransl("connect_ekka", true) %></td> <td><select name="connect_ekka" class="inputfield" title="CONNECT_EKKA"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", "", true) %></select></td>
		</tr>
   		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("vk_enc", true) %></td> <td valign="top"><select name="vk_enc" class="inputfield" > <%= Bean.getMeaningFromLookupNameOptions("VER_KEY", "", true) %></select></td>
			<td><%= Bean.terminalXML.getfieldTransl("tr_limit", true) %></td> <td valign="top"><input type="text" name="tr_limit" size="15" value="" class="inputfield"></td>
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
			<td><%=Bean.getCheckBoxNumber("card_type_active_nsmep", "1", Bean.terminalXML.getfieldTransl("card_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_nsmep", "1", Bean.terminalXML.getfieldTransl("oper_type_active_nsmep", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_discount_club", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_discount_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_cash", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_pay_cash", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_magnetic", "1", Bean.terminalXML.getfieldTransl("card_type_active_magnetic", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cash", "1", Bean.terminalXML.getfieldTransl("oper_type_active_cash", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_bonus_club", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_bonus_club", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_card", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_pay_card", false)) %></td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_chip", "1", Bean.terminalXML.getfieldTransl("card_type_active_chip", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_bon", "1", Bean.terminalXML.getfieldTransl("oper_type_active_bon", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_mov_bon", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_mov_bon", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_emv", "1", Bean.terminalXML.getfieldTransl("card_type_active_emv", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_cheque", "1", Bean.terminalXML.getfieldTransl("oper_type_active_cheque", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_check_param", "0", Bean.terminalXML.getfieldTransl("noprint_cheque_check_param", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.getCheckBoxNumber("card_type_active_barcode", "1", Bean.terminalXML.getfieldTransl("card_type_active_barcode", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("oper_type_active_emv", "1", Bean.terminalXML.getfieldTransl("OPER_TYPE_ACTIVE_EMV", false)) %></td>
			<td><%=Bean.getCheckBoxNumber("noprint_cheque_pay_bon", "0", Bean.terminalXML.getfieldTransl("NOPRINT_CHEQUE_PAY_BON", false)) %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club/term_device_typeupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club/term_device_typespecs.jsp?id=" + id) %>
				</td>
			</tr>
		</table>
	</form>

		<%
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
		String 
			cd_term_type 					= Bean.getDecodeParam(parameters.get("cd_term_type")),
			id_jur_prs	 					= Bean.getDecodeParam(parameters.get("id_jur_prs")),
			name_device_type 				= Bean.getDecodeParam(parameters.get("name_device_type")),
			desc_device_type 				= Bean.getDecodeParam(parameters.get("desc_device_type")),
			exist_flag 						= Bean.getDecodeParam(parameters.get("exist_flag")),
			work_with_certificate 			= Bean.getDecodeParam(parameters.get("work_with_certificate")),
			term_code_page 					= Bean.getDecodeParam(parameters.get("term_code_page")),
			resp_time	 					= Bean.getDecodeParam(parameters.get("resp_time")),
			ver_telgr	 					= Bean.getDecodeParam(parameters.get("ver_telgr")),
			crypt_telgr 					= Bean.getDecodeParam(parameters.get("crypt_telgr")),
			vk_enc  	 					= Bean.getDecodeParam(parameters.get("vk_enc")),
			tr_limit  	 					= Bean.getDecodeParam(parameters.get("tr_limit")),
			nincmax  	 					= Bean.getDecodeParam(parameters.get("nincmax")),
			connect_ekka  	 				= Bean.getDecodeParam(parameters.get("connect_ekka")),
			noprint_cheque_discount_club 	= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_discount_club")),
			noprint_cheque_bonus_club 		= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_bonus_club")),
			noprint_cheque_mov_bon 			= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_mov_bon")),
			noprint_cheque_check_param 		= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_check_param")),
			noprint_cheque_pay_cash 		= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_pay_cash")),
			noprint_cheque_pay_card 		= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_pay_card")),
			noprint_cheque_pay_bon 			= Bean.getDecodeCheckBoxNumber(parameters.get("noprint_cheque_pay_bon")),
			card_type_active_nsmep 			= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_nsmep")),
			card_type_active_magnetic 		= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_magnetic")),
			card_type_active_chip 			= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_chip")),
			card_type_active_emv 			= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_emv")),
			card_type_active_barcode 		= Bean.getDecodeCheckBoxNumber(parameters.get("card_type_active_barcode")),
			oper_type_active_nsmep 			= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_nsmep")),
			oper_type_active_cash 			= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_cash")),
			oper_type_active_bon 			= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_bon")),
			oper_type_active_cheque 		= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_cheque")),
			oper_type_active_emv 			= Bean.getDecodeCheckBoxNumber(parameters.get("oper_type_active_emv"));
	
	    String[] results = new String[2];

		//String resultFull = "0";
	    //String resultMessageFull = "";
	    
		if (action.equalsIgnoreCase("add")) {
			ArrayList<String> pParam = new ArrayList<String>();
				
			pParam.add(id_jur_prs);
			pParam.add(cd_term_type);
			pParam.add(name_device_type);
			pParam.add(desc_device_type);
			pParam.add(exist_flag);
			pParam.add(work_with_certificate);
			pParam.add(term_code_page);
			pParam.add(resp_time);
			pParam.add(ver_telgr);
			pParam.add(crypt_telgr);
			pParam.add(vk_enc);
			pParam.add(tr_limit);
			pParam.add(nincmax);
			pParam.add(connect_ekka);
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
	
			%>
			
			<%= Bean.executeInsertFunction("PACK$TERM_DEVICE_TYPE_UI.add_device_type", pParam, "../crm/club/term_device_typespecs.jsp?id=" , "") %>
   			<% 

		} else if (action.equalsIgnoreCase("remove")) {
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id);
			
		 	%>
			<%= Bean.executeDeleteFunction("PACK$TERM_DEVICE_TYPE_UI.delete_device_type", pParam, "../crm/club/term_device_type.jsp", "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) {
	 		
	 		ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id);
			pParam.add(id_jur_prs);
			//pParam.add(cd_term_type);
			pParam.add(name_device_type);
			pParam.add(desc_device_type);
			pParam.add(exist_flag);
			pParam.add(work_with_certificate);
			pParam.add(term_code_page);
			pParam.add(resp_time);
			pParam.add(ver_telgr);
			pParam.add(crypt_telgr);
			pParam.add(vk_enc);
			pParam.add(tr_limit);
			pParam.add(nincmax);
			pParam.add(connect_ekka);
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

			%>
			<%= Bean.executeUpdateFunction("PACK$TERM_DEVICE_TYPE_UI.update_device_type", pParam, "../crm/club/term_device_typespecs.jsp?id=" + id , "") %>
			
			<% 
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
