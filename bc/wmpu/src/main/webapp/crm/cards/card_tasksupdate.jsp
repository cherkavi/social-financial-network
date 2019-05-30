<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcPostingEditObject"%>
<%@page import="bc.objects.bcClubCardOperationObject"%>
<%@page import="bc.objects.bcClubCardPurseObject"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="java.util.ArrayList"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_CARD_TASKS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type					= Bean.getDecodeParamPrepare(parameters.get("type")); 
String action				= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process				= Bean.getDecodeParamPrepare(parameters.get("process"));
String external_id			= Bean.getDecodeParamPrepare(parameters.get("id")); 
String id_card_operation	= Bean.getDecodeParamPrepare(parameters.get("id_card_operation"));
String id_club				= Bean.getDecodeParamPrepare(parameters.get("id_club"));
String card_serial_number	= Bean.getDecodeParamPrepare(parameters.get("card_serial_number"));
String id_issuer			= Bean.getDecodeParamPrepare(parameters.get("id_issuer"));
String id_payment_system	= Bean.getDecodeParamPrepare(parameters.get("id_payment_system"));
String cd_card1				= Bean.getDecodeParamPrepare(parameters.get("cd_card1"));
String id_card_purse		= Bean.getDecodeParamPrepare(parameters.get("id_card_purse"));
String back_type			= Bean.getDecodeParamPrepare(parameters.get("back_type"));

String updateLink = "../crm/cards/card_tasksupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"CARD_TASK":back_type;

if ("CARD_TASK".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/cards/card_tasks.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/cards/card_tasks.jsp?";
	} else {
		backLink = "../crm/cards/card_taskspecs.jsp?id="+id_card_operation;
	}
} else if ("CARD".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/cards/clubcardspecs.jsp?id="+card_serial_number+"&iss="+id_issuer+"&paysys="+id_payment_system;
	backLink = "../crm/cards/clubcardspecs.jsp?id="+card_serial_number+"&iss="+id_issuer+"&paysys="+id_payment_system;
} else if ("PURSE".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/cards/clubcardpursespecs.jsp?id="+id_card_purse;
	backLink = "../crm/cards/clubcardpursespecs.jsp?id="+id_card_purse;
}


if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
	        
	        %>
		<script>
			var formData = new Array (
				new Array ('cd_card1', 'varchar2', 1)
			);
		</script>

			<% if ("CARD_TASK".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.card_taskXML.getfieldTransl("h_task_add", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.card_taskXML.getfieldTransl("h_task_add", false),
					"Y",
					"Y") 
			%>
			<% } %>
        <form action="../crm/cards/card_tasksupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="type" value="general">
			<input type="hidden" name="action" value="add_form">
			<input type="hidden" name="action_prev" value="<%=action %>">
			<input type="hidden" name="process" value="no">
			<input type="hidden" name="id_card_operation" value="<%=id_card_operation %>">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
			<input type="hidden" name="id_card_purse" value="<%= id_card_purse %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.card_taskXML.getfieldTransl("cd_card1", true) %></td> 
					<td>
						<%=Bean.getWindowFindClubCard(cd_card1, card_serial_number, id_issuer, id_payment_system, "30") %>
					</td>			
				</tr>
		 		<tr>
					<%
					  	String pOperationType = Bean.getDecodeParam(parameters.get("cd_card_operation_type"));
						if (Bean.isEmpty(pOperationType)) {
							String pUserOperationType = Bean.getUIUserParam("CARD_OPERATION_TYPE");	
							if (Bean.isEmpty(pUserOperationType)) {
								pOperationType = "BLOCK_CARD";
							} else {
								pOperationType = pUserOperationType;
							}
						}
					%>
					<td><%=Bean.card_taskXML.getfieldTransl("cd_card_operation_type", true)%></td><td><%= Bean.getClubCardOperationTypeRadio("cd_card_operation_type", pOperationType) %></td>
				</tr>
		 		<tr>
					<td colspan="2" align="center">
						<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", "CARD_TASK".equalsIgnoreCase(back_type)?"div_main":"div_data_detail") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton(backLink) %>
					</td>
				</tr>
		
			</table>
		</form>

	        <%
	        /*  --- Видалити запис --- */
    	} else if (action.equalsIgnoreCase("add_form")) {
    	    
    	String 
    		cd_card_operation_type 	= Bean.getDecodeParam(parameters.get("cd_card_operation_type")),
    		action_prev 			= Bean.getDecodeParam(parameters.get("action_prev"));
	        
	        %>

		<script>
			var formData = new Array (
				new Array ('cd_card1', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1),
				new Array ('need_apply', 'varchar2', 1),
				new Array ('basis_for_operation', 'varchar2', 1)
			);
			var formDataCHANGE_PARAM = new Array (
				new Array ('id_card_status', 'varchar2', 1)
			);
			var formDataWRITE_OFF_BON = new Array (
				new Array ('bal_full', 'varchar2', 1)
			);
			var formDataSEND_MESSAGE = new Array (
				new Array ('text_message', 'varchar2', 1)
			);
			var formDataSET_CATEGORIES_ON_PERIOD = new Array (
				new Array ('end_action_date', 'varchar2', 1),
				new Array ('id_card_status', 'varchar2', 1)
			);
			var formDataPurseOperations = new Array (
				new Array ('id_card_purse', 'varchar2', 1),
				new Array ('value_card_purse', 'varchar2', 1)
			);
			<% if ("CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type)) { %>
				formData = formData.concat(formDataCHANGE_PARAM);
			<%} else if ("WRITE_OFF_BON".equalsIgnoreCase(cd_card_operation_type)) {%>
				formData = formData.concat(formDataWRITE_OFF_BON);
			<%} else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) {%>
				formData = formData.concat(formDataSET_CATEGORIES_ON_PERIOD);
			<% } else if ("SEND_MESSAGE".equalsIgnoreCase(cd_card_operation_type)) { %>
				formData = formData.concat(formDataSEND_MESSAGE);
			<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(cd_card_operation_type) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(cd_card_operation_type)) { %>
				formData = formData.concat(formDataPurseOperations);
			<% } %>
	
			function myValidateForm() {
				return validateForm(formData);
			}

			<% if ("CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type) ||
					"SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) { %>
			dwr_get_card_bon_disc_category3('',document.getElementById('id_card_status'),'','','<%=Bean.getSessionId() %>');
			<% } %>
			
		</script>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>

		<body>
			<% if ("CARD_TASK".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.card_taskXML.getfieldTransl("h_task_add", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.card_taskXML.getfieldTransl("h_task_add", false),
					"Y",
					"Y") 
			%>
			<% } %>
        <form action="../crm/cards/card_tasksupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
			<input type="hidden" name="cd_card_operation_type" value="<%= cd_card_operation_type %>">
			<input type="hidden" name="card_serial_number" value="<%= card_serial_number %>">
			<input type="hidden" name="id_issuer" value="<%= id_issuer %>">
			<input type="hidden" name="id_payment_system" value="<%= id_payment_system %>">
			<input type="hidden" name="id_card_operation" value="<%=id_card_operation %>">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
			<input type="hidden" name="id_card_purse" value="<%= id_card_purse %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= Bean.getClubCardOperationTypeName(cd_card_operation_type) %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.card_taskXML.getfieldTransl("cd_card1", false) %></td> 
			<td>
				<input type="text" name="cd_card1" size="40" value="<%= cd_card1 %> " readonly="readonly" class="inputfield-ro">
			</td>			
		</tr>

		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) {	%>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= Bean.getClubCardOperationTypeName(cd_card_operation_type) %>" readonly="readonly" class="inputfield-ro"> </td>
		    <td><%= Bean.card_taskXML.getfieldTransl("begin_period_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<%  String cd_app_card_resp_action = Bean.getClubCardOperationTypeAction(cd_card_operation_type);
				String name_app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION", cd_app_card_resp_action);
			%>
			<td><%= Bean.card_taskXML.getfieldTransl("app_card_resp_action", false) %></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%= cd_app_card_resp_action + " - " + name_app_card_resp_action %>" readonly="readonly" class="inputfield-ro" title = "<%=cd_app_card_resp_action + " - " + name_app_card_resp_action %>"> </td>
		    <td><%= Bean.card_taskXML.getfieldTransl("end_period_date", true) %></td><td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.card_taskXML.getfieldTransl("need_apply", true) %></td> <td><select name="need_apply" class="inputfield"><%= Bean.getYesNoLookupOptions("Y", false) %></select></td>
		</tr>
		<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(cd_card_operation_type) ||
				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(cd_card_operation_type)) {	%>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= Bean.getClubCardOperationTypeName(cd_card_operation_type) %>" readonly="readonly" class="inputfield-ro"> </td>
		    <td><%= Bean.card_taskXML.getfieldTransl("begin_period_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.card_taskXML.getfieldTransl("need_apply", true) %></td> <td><select name="need_apply" class="inputfield"><%= Bean.getYesNoLookupOptions("Y", false) %></select></td>
		</tr>
		<% } else { %>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= Bean.getClubCardOperationTypeName(cd_card_operation_type) %>" readonly="readonly" class="inputfield-ro"> </td>
		    <td><%= Bean.card_taskXML.getfieldTransl("begin_period_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<%  String cd_app_card_resp_action = Bean.getClubCardOperationTypeAction(cd_card_operation_type);
				String name_app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION", cd_app_card_resp_action);
			%>
			<td><%= Bean.card_taskXML.getfieldTransl("app_card_resp_action", false) %></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%= cd_app_card_resp_action + " - " + name_app_card_resp_action %>" readonly="readonly" class="inputfield-ro" title = "<%=cd_app_card_resp_action + " - " + name_app_card_resp_action %>"> </td>
			<td><%= Bean.card_taskXML.getfieldTransl("need_apply", true) %></td> <td><select name="need_apply" class="inputfield"><%= Bean.getYesNoLookupOptions("Y", false) %></select></td>
		</tr>
		<% } %>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("basis_for_operation", true) %></td><td  colspan="3"><textarea name="basis_for_operation" cols="120" rows="2" class="inputfield"></textarea></td>
		</tr>
		
		<% if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", true) %></td> 
			<td>
				<select name="id_card_purse" class="inputfield"><%=Bean.getClubCardPurses(card_serial_number, id_issuer, id_payment_system, "", false)%></select>
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_add", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<% } else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", true) %></td> 
			<td>
				<select name="id_card_purse" class="inputfield"><%=Bean.getClubCardPurses(card_serial_number, id_issuer, id_payment_system, "", false)%></select>
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_write_off", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<% } else if ("ADD_BON".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("h_bal_acc_add", false) %></td> 
			<td><input type="text" name="bal_acc" size="20" value="" class="inputfield"> </td>

		    <td><%= Bean.card_taskXML.getfieldTransl("date_acc", false) %></td><td><%=Bean.getCalendarInputField("date_acc", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("h_bal_cur_add", false) %></td> 
			<td><input type="text" name="bal_cur" size="20" value="" class="inputfield"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>

		<% } else if ("WRITE_OFF_BON".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("h_bal_delete", true) %></td> 
			<td  colspan="3"><input type="text" name="bal_full" size="20" value="" class="inputfield"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>

		<% } else if ("CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_card_status", true) %></td><td><select name="id_card_status" id="id_card_status"  class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId() %>');"><%=Bean.getClubCardStatusOptions("", false) %> </select></td>
		    <td><%= Bean.getClubCardXMLFieldTransl("date_acc", false) %></td><td><%=Bean.getCalendarInputField("date_acc", "", "10") %></tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td class="top_line_gray"><select name="id_bon_category"  id="id_bon_category" class="inputfield"><option value=""></option></select></td>
		    <td><%= Bean.getClubCardXMLFieldTransl("date_mov", false) %></td><td><%=Bean.getCalendarInputField("date_mov", "", "10") %></tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y">
				<label class="checbox_label" for="can_bon_category_reduction"><%=Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) %></label>
			</td>
		    <td><%= Bean.getClubCardXMLFieldTransl("date_onl_next", false) %></td><td><%=Bean.getCalendarInputField("date_onl", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td class="top_line_gray"><select name="id_disc_category"  id="id_disc_category" class="inputfield"><option value=""></option></select></td>
			<td class="top_line_gray"><%= Bean.getClubCardXMLFieldTransl("bal_acc", false) %></td> <td class="top_line_gray"><input type="text" name="bal_acc" size="20" value="" class="inputfield" title="BAL_ACC"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y">
				<label class="checbox_label" for="can_disc_category_reduction"><%=Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) %></label>
			</td>
			<td><%= Bean.getClubCardXMLFieldTransl("bal_cur", false) %></td> <td><input type="text" name="bal_cur" size="20" value="" class="inputfield" title="BAL_CUR"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.getClubCardXMLFieldTransl("bal_bon_per", false) %></td> <td><input type="text" name="bal_bon_per" size="20" value="" class="inputfield" title="BAL_BON_PER"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.getClubCardXMLFieldTransl("bal_disc_per", false) %></td> <td><input type="text" name="bal_disc_per" size="20" value="" class="inputfield" title="BAL_DISC_PER"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_card_status", true) %></td><td><select name="id_card_status" id="id_card_status"  class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId() %>');"><%=Bean.getClubCardStatusOptions("", false) %> </select></td>
		    <td><%= Bean.getClubCardXMLFieldTransl("date_onl_next", false) %></td><td><%=Bean.getCalendarInputField("date_onl", "", "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_bon_category", false)  %></td> <td class="top_line_gray"><select name="id_bon_category"  id="id_bon_category" class="inputfield"><option value=""></option></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y">
				<label class="checbox_label" for="can_bon_category_reduction"><%=Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) %></label>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("id_disc_category", false)  %></td> <td class="top_line_gray"><select name="id_disc_category"  id="id_disc_category" class="inputfield"><option value=""></option></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y">
				<label class="checbox_label" for="can_disc_category_reduction"><%=Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) %></label>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("BLOCK_CARD".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } else if ("SEND_MESSAGE".equalsIgnoreCase(cd_card_operation_type)) { %>
		<tr>
		    <td colspan="4" class="top_line"><b><%= Bean.card_taskXML.getfieldTransl("tag_param", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.card_taskXML.getfieldTransl("text_message", false) %></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% } %>
 		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", "CARD_TASK".equalsIgnoreCase(back_type)?"div_main":"div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/cards/card_tasksupdate.jsp?type=general&action="+action_prev+"&process=no&id_card_operation=" + id_card_operation+"&cd_card_operation_type="+cd_card_operation_type+"&id="+external_id+"&id_club="+id_club+"&card_serial_number="+card_serial_number+"&id_issuer="+id_issuer+"&id_payment_system="+id_payment_system+"&cd_card1="+cd_card1+"&id_card_purse="+id_card_purse+"&back_type="+back_type, "cancel", "CARD_TASK".equalsIgnoreCase(back_type)?"div_main":"div_data_detail") %>
			</td>
		</tr>

	</table>

	</form>
	
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>

		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type)) {	%>
			<%= Bean.getCalendarScript("end_action_date", false) %>
		<% } %>
    	<% if ("ADD_BON".equalsIgnoreCase(cd_card_operation_type) ||
 			   "CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type)) { %>
			<%= Bean.getCalendarScript("date_acc", false) %>
    	<% } %>
    	<% if ("CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type)) { %>
			<%= Bean.getCalendarScript("date_mov", false) %>
      	<% } %>
		
    	<%	if (("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(cd_card_operation_type) || 
    				"CHANGE_PARAM".equalsIgnoreCase(cd_card_operation_type))) { %>
			<%= Bean.getCalendarScript("date_onl", false) %>
    	<% } %>

	        <%

    	
    	} else if (action.equalsIgnoreCase("edit")) {
    	    
    		bcClubCardOperationObject operation = new bcClubCardOperationObject(id_card_operation);

			bcClubCardObject card = null;
			if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
				card = new bcClubCardObject(operation.getValue("CARD_SERIAL_NUMBER"), operation.getValue("CARD_ID_ISSUER"), operation.getValue("CARD_ID_PAYMENT_SYSTEM"));
			}
			bcClubCardPurseObject purse = null;
			if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
					"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
				purse = new bcClubCardPurseObject(operation.getValue("ID_CARD_PURSE"));
			}
    	        
    	        %>

	<script>
		var formData = new Array (
			new Array ('priority', 'varchar2', 1),
			new Array ('cd_card1', 'varchar2', 1),
			new Array ('begin_action_date', 'varchar2', 1),
			new Array ('cd_card_oper_state', 'varchar2', 1),
			new Array ('need_apply', 'varchar2', 1),
			new Array ('basis_for_operation', 'varchar2', 1)
		);
		var formDataCHANGE_PARAM = new Array (
			new Array ('id_card_status', 'varchar2', 1)
		);
		var formDataWRITE_OFF_BON = new Array (
			new Array ('bal_full', 'varchar2', 1)
		);
		var formDataSEND_MESSAGE = new Array (
			new Array ('text_message', 'varchar2', 1)
		);
		var formDataSET_CATEGORIES_ON_PERIOD = new Array (
			new Array ('end_action_date', 'varchar2', 1),
			new Array ('id_card_status', 'varchar2', 1)
		);
		var formDataPurseOperations = new Array (
			new Array ('value_card_purse', 'varchar2', 1)
		);
		<%if ("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataCHANGE_PARAM);
		<%} else if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataWRITE_OFF_BON);
		<%} else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataSET_CATEGORIES_ON_PERIOD);
		<%} else if ("SEND_MESSAGE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {%>
			formData = formData.concat(formDataSEND_MESSAGE);
		<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) { %>
			formData = formData.concat(formDataPurseOperations);
		<%}%>

		function myValidateForm() {
			return validateForm(formData);
		}
	</script>

	<%=Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message")%>

    <form action="../crm/cards/card_tasksupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id_card_operation" value="<%=id_card_operation%>">
		<input type="hidden" name="cd_card_operation_type" value="<%=operation.getValue("CD_CARD_OPERATION_TYPE")%>">
		<input type="hidden" name="card_serial_number" value="<%= operation.getValue("CARD_SERIAL_NUMBER") %>">
		<input type="hidden" name="id_issuer" value="<%= operation.getValue("CARD_ID_ISSUER") %>">
		<input type="hidden" name="id_payment_system" value="<%= operation.getValue("CARD_ID_PAYMENT_SYSTEM") %>">
		<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">
		<input type="hidden" name="id_card_purse" value="<%= id_card_purse %>">
	<table <%=Bean.getTableDetailParam()%>>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("ID_CARD_OPERATION",false)%></td> <td><input type="text" name="id_card_operation" size="20" value="<%=operation.getValue("ID_CARD_OPERATION")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.clubXML.getfieldTransl("club", false)%>
					<%=Bean.getGoToClubLink(operation.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%=Bean.getClubShortName(operation.getValue("ID_CLUB"))%>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						operation.getValue("CARD_SERIAL_NUMBER"),
						operation.getValue("CARD_ID_ISSUER"),
						operation.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td> 
			<td><input type="text" name="cd_card1" size="40" value="<%= operation.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%=Bean.card_taskXML.getfieldTransl("cd_card_oper_state", true)%></td> <td><select name="cd_card_oper_state" class="inputfield"><%=Bean.getClubCardOperationStateOptions(operation.getValue("CD_CARD_OPER_STATE"),true)%></select></td>
		</tr>
		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= operation.getValue("NAME_CARD_OPERATION_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		    <td><%=Bean.card_taskXML.getfieldTransl("begin_period_date",true)%></td><td><%=Bean.getCalendarInputField("begin_action_date", operation.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<%
				String app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION",operation.getValue("APP_CARD_RESP_ACTION"));
			%>
			<td><%=Bean.card_taskXML.getfieldTransl("app_card_resp_action",false)%></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>" readonly="readonly" class="inputfield-ro" title = "<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>"> </td>
		    <td><%=Bean.card_taskXML.getfieldTransl("end_period_date",true)%></td><td><%=Bean.getCalendarInputField("end_action_date", operation.getValue("END_ACTION_DATE_FRMT"), "10") %>
			  <font color="green">&nbsp;(<%=operation.getValue("PERIOD_DURATION")%>&nbsp;<%=Bean.commonXML.getfieldTransl("h_days",false)%>)</font>
		    </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", true)%></td> <td><select name="need_apply" class="inputfield"><%=Bean.getYesNoLookupOptions(operation.getValue("NEED_APPLY"),false)%></select></td>
		</tr>
		<% } else if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) ||
				"WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= operation.getValue("NAME_CARD_OPERATION_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		    <td><%=Bean.card_taskXML.getfieldTransl("begin_action_date",true)%></td><td><%=Bean.getCalendarInputField("begin_action_date", operation.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", true)%></td> <td><select name="need_apply" class="inputfield"><%=Bean.getYesNoLookupOptions(operation.getValue("NEED_APPLY"),false)%></select></td>
		</tr>
		<% } else { %>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false) %></td> <td><input type="text" name="name_card_operation_type" size="40" value="<%= operation.getValue("NAME_CARD_OPERATION_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		    <td><%=Bean.card_taskXML.getfieldTransl("begin_action_date",true)%></td><td><%=Bean.getCalendarInputField("begin_action_date", operation.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<%
				String app_card_resp_action = Bean.getMeaningForNumValue("APP_CARD_RESP_ACTION",operation.getValue("APP_CARD_RESP_ACTION"));
			%>
			<td><%=Bean.card_taskXML.getfieldTransl("app_card_resp_action",false)%></td> <td><input type="text" name="app_card_resp_action" size="40" value="<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>" readonly="readonly" class="inputfield-ro" title = "<%=operation.getValue("APP_CARD_RESP_ACTION") + " - " + app_card_resp_action%>"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("need_apply", true)%></td> <td><select name="need_apply" class="inputfield"><%=Bean.getYesNoLookupOptions(operation.getValue("NEED_APPLY"),false)%></select></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("PRIORITY", false)%></td> <td><input type="text" name="priority" size="20" value="<%=operation.getValue("PRIORITY")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<% } %>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("basis_for_operation",true)%></td><td  colspan="3"><textarea name="basis_for_operation" cols="120" rows="3" class="inputfield"><%=operation.getValue("BASIS_FOR_OPERATION")%></textarea></td>
		</tr>

		<%
			if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", false) %>
				<%= Bean.getGoToClubCardPurseLink(purse.getValue("ID_CARD_PURSE")) %>
			</td> 
			<td>
				<input type="hidden" name="id_card_purse" value="<%= purse.getValue("ID_CARD_PURSE") %>">
				<input type="text" name="name_card_purse" size="40" value="<%= purse.getValue("NUMBER_CARD_PURSE") %> - <%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_add", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="<%=operation.getValue("VALUE_CARD_PURSE_FRMT")%>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>

		<%
			} else if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
				
				double cardPurseValue = 0;
				double operationPurseValue = 0;
				if (!Bean.isEmpty(purse.getValue("VALUE_CARD_PURSE"))) {
					cardPurseValue = Double.parseDouble(purse.getValue("VALUE_CARD_PURSE"))/100;
				}
				if (!Bean.isEmpty(operation.getValue("VALUE_CARD_PURSE"))) {
					operationPurseValue = Double.parseDouble(operation.getValue("VALUE_CARD_PURSE"))/100;
				}

			%>
			
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b>
			<%
			if (cardPurseValue < operationPurseValue) {
				%>
					<b><font color="red">&nbsp;(<%=Bean.card_taskXML.getfieldTransl("h_exception_in_purse_value",false)%>)</font></b>
				<%
					}
			%>
			</td>
		</tr>
		<tr>
			<td><%= Bean.card_taskXML.getfieldTransl("card_purse", false) %>
				<%= Bean.getGoToClubCardPurseLink(purse.getValue("ID_CARD_PURSE")) %>
			</td> 
			<td>
				<input type="hidden" name="id_card_purse" value="<%= purse.getValue("ID_CARD_PURSE") %>">
				<input type="text" name="name_card_purse" size="40" value="<%= purse.getValue("NUMBER_CARD_PURSE") %> - <%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_value_card_purse_write_off", true)%></td> <td><input type="text" name="value_card_purse" size="20" value="<%=operation.getValue("VALUE_CARD_PURSE_FRMT")%>" class="inputfield">
				(<%=Bean.card_taskXML.getfieldTransl("h_has_purse_value",false)%>&nbsp;
				<b><span style="color:green"><%="" + cardPurseValue%></span></b>)
            </td>
			<td colspan="2">&nbsp;</td> 
		</tr>

		<%
			} else if ("ADD_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_acc_add", false)%></td> <td><input type="text" name="bal_acc" size="20" value="<%=operation.getValue("BAL_ACC_FRMT")%>" class="inputfield"></td>
			<td><%=Bean.card_taskXML.getfieldTransl("date_acc", false)%></td><td><%=Bean.getCalendarInputField("date_acc", operation.getValue("DATE_ACC_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_cur_add", false)%></td> 
              <td><input type="text" name="bal_cur" size="20" value="<%=operation.getValue("BAL_CUR_FRMT")%>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td> 
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message",false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>

		<%
			} else if ("WRITE_OFF_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b>
			<%
				double cardBalAcc = 0;
				double cardBalCur = 0;
				double cardBalFull = 0;
				double operationBalFull = 0;

				if (!Bean.isEmpty(card.getValue("BAL_ACC"))) {
					cardBalAcc = Double.parseDouble(card.getValue("BAL_ACC"))/100;
				}
				if (!Bean.isEmpty(card.getValue("BAL_CUR"))) {
					cardBalCur = Double.parseDouble(card.getValue("BAL_CUR"))/100;
				}
				cardBalFull = cardBalAcc + cardBalFull;
				if (!Bean.isEmpty(operation.getValue("BAL_FULL"))) {
					operationBalFull = Double.parseDouble(operation.getValue("BAL_FULL"))/100;
				}

				if (cardBalFull < operationBalFull) {
			%>
				<b><font color="red">&nbsp;(<%=Bean.card_taskXML.getfieldTransl("h_exception_in_bon",false)%>)</font></b>
			<%
				}
			%>
			</td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("h_bal_delete", true)%></td> 
            <td  colspan="3"><input type="text" name="bal_full" size="20" value="<%=operation.getValue("BAL_FULL_FRMT")%>" class="inputfield">
				(<%=Bean.card_taskXML.getfieldTransl("h_has_bon",false)%>&nbsp;
				<b>
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_ACC", false) %>"><%="" + cardBalAcc%></span>&nbsp;+
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_CUR", false) %>"><%="" + cardBalCur%></span>&nbsp;=
					<span style="color:green" title="<%= Bean.getClubCardXMLFieldTransl("BAL_FULL", false) %>"><%="" + cardBalFull%></span>
				</b>)
            </td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message",false)%></td><td  colspan="3"><textarea name="text_message" cols="120" rows="2" class="inputfield"><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<%
			} else if ("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_card_status",true)%></td><td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId()%>');"><%=Bean.getClubCardStatusOptions(operation.getValue("ID_CARD_STATUS"),false)%> </select></td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_acc", false)%></td><td><%=Bean.getCalendarInputField("date_acc", operation.getValue("DATE_ACC_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_bon_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_BON_CATEGORY"),"BON",true) %></select>
			</td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_mov", false)%></td><td><%=Bean.getCalendarInputField("date_mov", operation.getValue("DATE_MOV_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String bonReductionChecked = "";
					String bonReductionLabel = Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_BON_CATEGORY_REDUCTION"))) {
						bonReductionChecked = " CHECKED ";
						bonReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y" <%=bonReductionChecked %>>
				<label class="checbox_label" for="can_bon_category_reduction"><%=bonReductionLabel %></label>
			 		<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_change_param_can_category_reduction", false)) %>
			</td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_onl_next", false)%></td><td><%=Bean.getCalendarInputField("date_onl", operation.getValue("DATE_ONL_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_disc_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_DISC_CATEGORY"),"DISC",true) %></select>
			</td>
			<td class="top_line_gray"><%=Bean.getClubCardXMLFieldTransl("bal_acc", false)%></td> <td class="top_line_gray"><input type="text" name="bal_acc" size="20" value="<%=operation.getValue("BAL_ACC_FRMT")%>" class="inputfield" title="BAL_ACC"> </td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String discReductionChecked = "";
					String discReductionLabel = Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_DISC_CATEGORY_REDUCTION"))) {
						discReductionChecked = " CHECKED ";
						discReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y" <%=discReductionChecked %>>
				<label class="checbox_label" for="can_disc_category_reduction"><%=discReductionLabel %></label>
			 		<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_change_param_can_category_reduction", false)) %>
			</td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_cur", false)%></td> <td><input type="text" name="bal_cur" size="20" value="<%=operation.getValue("BAL_CUR_FRMT")%>" class="inputfield" title="BAL_CUR"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%=Bean.getClubCardXMLFieldTransl("bal_bon_per",false)%></td> <td><input type="text" name="bal_bon_per" size="20" value="<%=operation.getValue("BAL_BON_PER_FRMT")%>" class="inputfield" title="BAL_BON_PER"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td valign="top"><%=Bean.getClubCardXMLFieldTransl("bal_disc_per",false)%></td> <td valign="top"><input type="text" name="bal_disc_per" size="20" value="<%=operation.getValue("BAL_DISC_PER_FRMT")%>" class="inputfield" title="BAL_DISC_PER"> </td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message", false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%
			} else if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_card_status",true)%></td><td><select name="id_card_status" id="id_card_status" class="inputfield" onchange="dwr_get_card_bon_disc_category3('',this,'','','<%=Bean.getSessionId()%>');"><%=Bean.getClubCardStatusOptions(operation.getValue("ID_CARD_STATUS"),false)%> </select></td>
		    <td><%=Bean.getClubCardXMLFieldTransl("date_onl_next", false)%></td><td><%=Bean.getCalendarInputField("date_onl", operation.getValue("DATE_ONL_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_bon_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_bon_category"  id="id_bon_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_BON_CATEGORY"),"BON",true) %></select>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String bonReductionChecked = "";
					String bonReductionLabel = Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_BON_CATEGORY_REDUCTION"))) {
						bonReductionChecked = " CHECKED ";
						bonReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_bon_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_bon_category_reduction" id="can_bon_category_reduction" value="Y" <%=bonReductionChecked %>>
				<label class="checbox_label" for="can_bon_category_reduction"><%=bonReductionLabel %></label>
			 		<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_set_categories_can_category_reduction", false)) %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_disc_category",false)%></td> 
			<td class="top_line_gray">
				<select name="id_disc_category"  id="id_disc_category" class="inputfield"><%=Bean.getClubCardCategoryForStatusOptions(operation.getValue("ID_CARD_STATUS"),operation.getValue("ID_DISC_CATEGORY"),"DISC",true) %></select>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<%
					String discReductionChecked = "";
					String discReductionLabel = Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false);
					if ("Y".equalsIgnoreCase(operation.getValue("CAN_DISC_CATEGORY_REDUCTION"))) {
						discReductionChecked = " CHECKED ";
						discReductionLabel = "<b><font color=\"red\">" + Bean.card_taskXML.getfieldTransl("can_disc_category_reduction",false) + "</font></b>";
					}
				%>
				<input type="checkbox" name="can_disc_category_reduction" id="can_disc_category_reduction" value="Y" <%=discReductionChecked %>>
				<label class="checbox_label" for="can_disc_category_reduction"><%=discReductionLabel %></label>
			 		<%=Bean.getUWndHelp(Bean.card_taskXML.getfieldTransl("help_set_categories_can_category_reduction", false)) %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message", false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%
			} else if ("BLOCK_CARD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", false)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message",false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%
			} else if ("SEND_MESSAGE".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_param", false)%></b></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.card_taskXML.getfieldTransl("text_message", true)%></td><td  colspan="3"><textarea name="text_message" id="text_message" cols="120" rows="2" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message")%>><%=operation.getValue("TEXT_MESSAGE")%></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%=Bean.messageXML.getfieldTransl("length_message", false)%></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%
			}
		%>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.card_taskXML.getfieldTransl("tag_source_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_sequrity_monitor", false)%></td> <td><input type="text" name="id_sequrity_monitor" size="20" value="<%=operation.getValue("ID_SEQURITY_MONITOR")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("id_previous_operation", false)%>
				<%=Bean.getGoToCardTaskLink(operation.getValue("ID_PREVIOUS_OPERATION"))
				%>
			  </td> <td><input type="text" name="id_previous_operation" size="20" value="<%=operation.getValue("ID_PREVIOUS_OPERATION")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_remittance", false)%></td> <td><input type="text" name="id_remittance" size="20" value="<%=operation.getValue("ID_REMITTANCE")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.card_taskXML.getfieldTransl("id_club_event", false)%>
				<%=Bean.getGoToClubEventLink(operation.getValue("ID_CLUB_EVENT"))
				%>
			  </td> <td><input type="text" name="id_quest_int" size="20" value="<%=operation.getValue("ID_CLUB_EVENT")%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.card_taskXML.getfieldTransl("id_quest_int", false)%>
				<%=Bean.getGoToQuestionnaireLink(operation.getValue("ID_QUEST_INT"))
				%>
			  </td> <td><input type="text" name="id_quest_int" size="20" value="<%=operation.getValue("ID_QUEST_INT")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				operation.getValue(Bean.getCreationDateFieldName()),
				operation.getValue("CREATED_BY"),
				operation.getValue(Bean.getLastUpdateDateFieldName()),
				operation.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax(updateLink)%>
				<%=Bean.getResetButton()%>
				<%=Bean.getGoBackButton(backLink)%>
			</td>
		</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
	<% if ("NEW".equalsIgnoreCase(operation.getValue("CD_CARD_OPER_STATE"))) { %>
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		
		<% if ("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE"))) {	%>
			<%= Bean.getCalendarScript("end_action_date", false) %>
		<% } %>

   	<% } %>
   	<% if (("ADD_BON".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) || 
    			"CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")))) { %>
		
		<%= Bean.getCalendarScript("date_acc", false) %>
    <% } %>
    <% if (("CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")))) { %>
		
		<%= Bean.getCalendarScript("date_mov", false) %>
    <%	} %>
    <% if (("SET_CATEGORIES_ON_PERIOD".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")) || 
    				"CHANGE_PARAM".equalsIgnoreCase(operation.getValue("CD_CARD_OPERATION_TYPE")))) { %>

		<%= Bean.getCalendarScript("date_onl", false) %>
   	<% } %>
 

    	        <%
    	} else {
	   	    %> <%= Bean.getUnknownActionText(action) %><%
	   	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
    
		String
	    	cd_card_operation_type 		= Bean.getDecodeParam(parameters.get("cd_card_operation_type")),
	    	begin_action_date 			= Bean.getDecodeParam(parameters.get("begin_action_date")),
	    	end_action_date 			= Bean.getDecodeParam(parameters.get("end_action_date")),
	    	need_apply 					= Bean.getDecodeParam(parameters.get("need_apply")),
	    	basis_for_operation 		= Bean.getDecodeParam(parameters.get("basis_for_operation")),
	    	bal_acc 					= Bean.getDecodeParam(parameters.get("bal_acc")),
	    	bal_cur 					= Bean.getDecodeParam(parameters.get("bal_cur")),
	    	bal_bon_per					= Bean.getDecodeParam(parameters.get("bal_bon_per")),
	    	bal_disc_per				= Bean.getDecodeParam(parameters.get("bal_disc_per")),
	    	bal_full					= Bean.getDecodeParam(parameters.get("bal_full")),
	    	date_acc 					= Bean.getDecodeParam(parameters.get("date_acc")),
	    	date_mov 					= Bean.getDecodeParam(parameters.get("date_mov")),
	    	date_onl 					= Bean.getDecodeParam(parameters.get("date_onl")),
	    	id_card_status 				= Bean.getDecodeParam(parameters.get("id_card_status")),
	    	id_bon_category		 		= Bean.getDecodeParam(parameters.get("id_bon_category")),
	    	id_disc_category 			= Bean.getDecodeParam(parameters.get("id_disc_category")),
	    	text_message 				= Bean.getDecodeParam(parameters.get("text_message")),
	    	cd_card_oper_state			= Bean.getDecodeParam(parameters.get("cd_card_oper_state")),
	    	can_bon_category_reduction	= Bean.getDecodeParam(parameters.get("can_bon_category_reduction")),
	    	can_disc_category_reduction	= Bean.getDecodeParam(parameters.get("can_disc_category_reduction")),
	    	value_card_purse			= Bean.getDecodeParam(parameters.get("value_card_purse"));
	
		ArrayList<String> pParam = new ArrayList<String>();
		
		if (action.equalsIgnoreCase("add")) { 
		
			pParam.add(cd_card_operation_type);
			pParam.add(begin_action_date);
			pParam.add(end_action_date);
			pParam.add(need_apply);
			pParam.add(basis_for_operation);
			pParam.add(card_serial_number);
			pParam.add(id_issuer);
			pParam.add(id_payment_system);
			pParam.add(bal_acc);
			pParam.add(bal_cur);
			pParam.add(bal_bon_per);
			pParam.add(bal_disc_per);
			pParam.add(bal_full);
			pParam.add(date_acc);
			pParam.add(date_mov);
			pParam.add(date_onl);
			pParam.add(id_card_status);
			pParam.add(id_bon_category);
			pParam.add(can_bon_category_reduction);
			pParam.add(id_disc_category);
			pParam.add(can_disc_category_reduction);
			pParam.add(text_message);
			pParam.add("");
			pParam.add("");
			pParam.add("");
			pParam.add("");
			pParam.add(id_card_purse);
			pParam.add(value_card_purse);
			pParam.add("");
			pParam.add(Bean.getDateFormat());
			
		 	%>
			<%= Bean.executeInsertFunction("PACK$CARD_OPER_UI.add_card_operation", pParam, backLink + "&id_card_operation=", "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) { 
			
			pParam.add(id_card_operation);
	
		 	%>
			<%= Bean.executeDeleteFunction("PACK$CARD_OPER_UI.delete_card_operation", pParam, generalLink, "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) { 
			
			pParam.add(id_card_operation);
			pParam.add(cd_card_operation_type);
			pParam.add(begin_action_date);
			pParam.add(end_action_date);
			pParam.add(need_apply);
			pParam.add(basis_for_operation);
			pParam.add(cd_card_oper_state);
			pParam.add(card_serial_number);
			pParam.add(id_issuer);
			pParam.add(id_payment_system);
			pParam.add(bal_acc);
			pParam.add(bal_cur);
			pParam.add(bal_bon_per);
			pParam.add(bal_disc_per);
			pParam.add(bal_full);
			pParam.add(date_acc);
			pParam.add(date_mov);
			pParam.add(date_onl);
			pParam.add(id_card_status);
			pParam.add(id_bon_category);
			pParam.add(can_bon_category_reduction);
			pParam.add(id_disc_category);
			pParam.add(can_disc_category_reduction);
			pParam.add(id_card_purse);
			pParam.add(value_card_purse);
			pParam.add(text_message);
			pParam.add(Bean.getDateFormat());
			
		 	%>
			<%= Bean.executeUpdateFunction("PACK$CARD_OPER_UI.update_card_operation", pParam, backLink, "") %>
			<% 	
			
	    } else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("posting")) {%>
	<script>
		var formAll = new Array();
		var formData = new Array (
			new Array ('operation_date', 'varchar2', 1),
			new Array ('debet_name_bk_account', 'varchar2', 1),
			new Array ('cd_currency', 'varchar2', 1),
			new Array ('credit_name_bk_account', 'varchar2', 1),
			new Array ('entered_amount', 'varchar2', 1),
			new Array ('run_postings_export', 'varchar2', 1),
			new Array ('using_in_clearing', 'varchar2', 1)
		);
		var formDataClearing = new Array (
			new Array ('name_bank_account_debet', 'varchar2', 1),
			new Array ('name_bank_account_credit', 'varchar2', 1)
		);
		var formDataExport = new Array (
			new Array ('cd_bk_doc_type', 'varchar2', 1)
		);
	
		function myValidateForm() {
			var using = document.getElementById('using_in_clearing').value;
			var run_exp = document.getElementById('run_postings_export').value;
	
			formAll = formData;
			if (using == 'Y') {
				formAll = formAll.concat(formDataClearing);
			}
			if (run_exp == 'Y') {
				formAll = formAll.concat(formDataExport);
			}
			return validateForm(formAll);
		}
		
		function checkClearing(){
			var using = document.getElementById('using_in_clearing').value;
			if (using == 'Y') {
				document.getElementById('span_receiver_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("receiver_id_bank_account", true) %>';
				document.getElementById('span_payer_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("payer_id_bank_account", true) %>';
			} else {
				document.getElementById('span_receiver_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("receiver_id_bank_account", false) %>';
				document.getElementById('span_payer_id_bank_account').innerHTML='<%= Bean.postingXML.getfieldTransl("payer_id_bank_account", false) %>';
			}
		}
		
		function checkBKDocType(){
			var run_exp = document.getElementById('run_postings_export').value;
			if (run_exp == 'Y') {
				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", true) %>';
			} else {
				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) %>';
			}
		}
		checkClearing();
		checkBKDocType();
		
		function changeRelationShipParam(id, name){
			document.getElementById('id_club_rel').value = id;
			document.getElementById('name_club_rel').value = name;
			document.getElementById('name_club_rel').className = "inputfield_modified";
		}
	</script>

	<%
	String
		id_posting_detail	= Bean.getDecodeParam(parameters.get("id_posting_detail"));
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) { 
		
			bcPostingEditObject posting = new bcPostingEditObject();
		
	%>

	<%= Bean.getOperationTitle(
			Bean.card_taskXML.getfieldTransl("LAB_ADD_POSTING", false),
			"Y",
			"Y") 
	%>
	
	<form action="../crm/cards/card_tasksupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="posting">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_card_operation" value="<%=id_card_operation %>">
        <input type="hidden" name="id_club" value="<%=id_club %>">
	<table <%=Bean.getTableDetailParam() %>>

		<%=posting.getPostingAddHTML("", Bean.getDateFormatTitle()) %>

		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/card_tasksupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/cards/card_taskspecs.jsp?id=" + id_card_operation) %>
			</td>
		</tr>
	</table>
	</form>	
	<%= Bean.getCalendarScript("operation_date", false) %>
	<%= Bean.getCalendarScript("conversion_date", false) %>

	<% 
	} else if (action.equalsIgnoreCase("edit")) { 
	
		bcPostingEditObject posting = new bcPostingEditObject();
		
	%>

	<%= Bean.getOperationTitle(
			Bean.card_taskXML.getfieldTransl("LAB_EDIT_POSTING", false),
			"Y",
			"Y") 
	%>
		<form action="../crm/cards/card_tasksupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="posting">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_card_operation" value="<%=id_card_operation %>">
	<table <%=Bean.getTableDetailParam() %>>

			<%=posting.getPostingEditHTML(id_posting_detail, Bean.getDateFormatTitle()) %>

		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/card_tasksupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/cards/card_taskspecs.jsp?id=" + id_card_operation) %>
			</td>
		</tr>
	</table>
	</form>	
	<%= Bean.getCalendarScript("operation_date", false) %>
	<%= Bean.getCalendarScript("conversion_date", false) %>
		<%
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
		
	} else if (process.equalsIgnoreCase("yes")) {
		String
			operation_date				= Bean.getDecodeParam(parameters.get("operation_date")),
			debet_id_bk_account			= Bean.getDecodeParam(parameters.get("debet_id_bk_account")),
			cd_currency					= Bean.getDecodeParam(parameters.get("cd_currency")),
			credit_id_bk_account		= Bean.getDecodeParam(parameters.get("credit_id_bk_account")),
			entered_amount				= Bean.getDecodeParam(parameters.get("entered_amount")),
			assignment_posting			= Bean.getDecodeParam(parameters.get("assignment_posting")),
			base_currency				= Bean.getDecodeParam(parameters.get("base_currency")),
			exchange_rate				= Bean.getDecodeParam(parameters.get("exchange_rate")),
			id_bk_operation_scheme_line	= Bean.getDecodeParam(parameters.get("id_bk_operation_scheme_line")),
			conversion_date				= Bean.getDecodeParam(parameters.get("conversion_date")),
			id_club_rel					= Bean.getDecodeParam(parameters.get("id_club_rel")),
			accounted_amount			= Bean.getDecodeParam(parameters.get("accounted_amount")),
			using_in_clearing			= Bean.getDecodeParam(parameters.get("using_in_clearing")),
			run_postings_export			= Bean.getDecodeParam(parameters.get("run_postings_export")),
			cd_bk_doc_type				= Bean.getDecodeParam(parameters.get("cd_bk_doc_type")),
			id_bank_account_debet		= Bean.getDecodeParam(parameters.get("id_bank_account_debet")),
			id_bank_account_credit		= Bean.getDecodeParam(parameters.get("id_bank_account_credit")),
			payment_function			= Bean.getDecodeParam(parameters.get("payment_function"));

		if (action.equalsIgnoreCase("run")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_OPER_UI.run_posting(?,?,?,?)}";

			String[] pParam = new String [2];
							
			pParam[0] = id_club;
			pParam[1] = id_card_operation;
			
	 		%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/cards/card_taskspecs.jsp?id=" + id_card_operation + "&id_report=", "") %>
			<%
			
		} else if (action.equalsIgnoreCase("run_all")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_OPER_UI.run_posting_all(?,?,?)}";

			String[] pParam = new String [1];
								
			pParam[0] = id_club;
			
	 		%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/cards/card_taskspecs.jsp?id=" + id_card_operation + "&id_report=", "") %>
			<%
			
		} else if (action.equalsIgnoreCase("add")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_OPER_UI.add_posting("+
	       		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	    	String[] pParam = new String [21];

	    	pParam[0] = id_card_operation;
	    	pParam[1] = debet_id_bk_account;
	    	pParam[2] = credit_id_bk_account;
	    	pParam[3] = cd_currency;
	    	pParam[4] = operation_date;
	    	pParam[5] = entered_amount;
	    	pParam[6] = base_currency;
	    	pParam[7] = exchange_rate;
	    	pParam[8] = conversion_date;
	    	pParam[9] = accounted_amount;
	    	pParam[10] = assignment_posting;
	    	pParam[11] = id_bk_operation_scheme_line;
	    	pParam[12] = id_club_rel;
	    	pParam[13] = using_in_clearing;
	    	pParam[14] = run_postings_export;
	    	pParam[15] = cd_bk_doc_type;
	    	pParam[16] = id_bank_account_debet;
	    	pParam[17] = id_bank_account_credit;
	    	pParam[18] = payment_function;
	    	pParam[19] = id_club;
	    	pParam[20] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/cards/card_taskspecs.jsp?id=" + id_card_operation + "&id_posting_detail=", "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("edit")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_OPER_UI.update_posting("+
	       		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	    	String[] pParam = new String [20];

	    	pParam[0] = id_posting_detail;
	    	pParam[1] = debet_id_bk_account;
	    	pParam[2] = credit_id_bk_account;
	    	pParam[3] = cd_currency;
	    	pParam[4] = operation_date;
	    	pParam[5] = entered_amount;
	    	pParam[6] = base_currency;
	    	pParam[7] = exchange_rate;
	    	pParam[8] = conversion_date;
	    	pParam[9] = accounted_amount;
	    	pParam[10] = assignment_posting;
	    	pParam[11] = id_bk_operation_scheme_line;
	    	pParam[12] = id_club_rel;
	    	pParam[13] = using_in_clearing;
	    	pParam[14] = run_postings_export;
	    	pParam[15] = cd_bk_doc_type;
	    	pParam[16] = id_bank_account_debet;
	    	pParam[17] = id_bank_account_credit;
	    	pParam[18] = payment_function;
	    	pParam[19] = Bean.getDateFormat();
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/card_taskspecs.jsp?id=" + id_card_operation, "") %>
			<% 	
			
		} else if (action.equalsIgnoreCase("remove")) { 
		       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_OPER_UI.delete_posting(?,?)}";

	    	String[] pParam = new String [1];

	    	pParam[0] = id_posting_detail;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/cards/card_taskspecs.jsp?id=" + id_card_operation, "") %>
			<% 	
			
	    } else if (action.equalsIgnoreCase("removeall")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_OPER_UI.delete_all_task_posting(?,?)}";

		   	String[] pParam = new String [1];

		    pParam[0] = id_card_operation;
			
	 		%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/cards/card_taskspecs.jsp?id=" + id_card_operation, "") %>
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
