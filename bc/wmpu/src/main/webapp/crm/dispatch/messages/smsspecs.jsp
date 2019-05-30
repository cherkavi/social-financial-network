<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcSMSObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<Head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_SMS";

String tagActions = "_ACTIONS";
String tagSendMessages = "_SEND_MESSAGES";
String tagGiftsRequest = "_GIFTS_REQUEST";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null) { id=""; }
if ("".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcSMSObject sms = new bcSMSObject(id);
	
	bcSMSObject sms_parent = null;
	boolean hasParentMessage = false;
	
	if (!(sms.getValue("ID_SMS_MESSAGE_PARENT") == null || 
			"".equalsIgnoreCase(sms.getValue("ID_SMS_MESSAGE_PARENT")))) {
		sms_parent = new bcSMSObject(sms.getValue("ID_SMS_MESSAGE_PARENT"));
		hasParentMessage = true;
	}
	
	//Обрабатываем номера страниц
	String l_action_page = Bean.getDecodeParam(parameters.get("action_page"));
	Bean.pageCheck(pageFormName + tagActions, l_action_page);
	String l_action_page_beg = Bean.getFirstRowNumber(pageFormName + tagActions);
	String l_action_page_end = Bean.getLastRowNumber(pageFormName + tagActions);
	
	String l_send_page = Bean.getDecodeParam(parameters.get("send_page"));
	Bean.pageCheck(pageFormName + tagSendMessages, l_send_page);
	String l_send_page_beg = Bean.getFirstRowNumber(pageFormName + tagSendMessages);
	String l_send_page_end = Bean.getLastRowNumber(pageFormName + tagSendMessages);
	
	String l_request_page = Bean.getDecodeParam(parameters.get("request_page"));
	Bean.pageCheck(pageFormName + tagGiftsRequest, l_request_page);
	String l_request_page_beg = Bean.getFirstRowNumber(pageFormName + tagGiftsRequest);
	String l_request_page_end = Bean.getLastRowNumber(pageFormName + tagGiftsRequest);
	
	if (!"SEND".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_SMS_ACTION",false);
		if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_SMS_ACTION")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	} else {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_SMS_ACTION",true);
	}

	if (!("RECEIVE".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")) ||
			"CALL_IN".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")))) {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_SMS_SEND_MESSAGES",false);
		if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_SMS_SEND_MESSAGES")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	} else {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_SMS_SEND_MESSAGES",true);
	}

	if (!("RECEIVE".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")) ||
			"CALL_IN".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")))) {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_SMS_GIFTS_REQUEST",false);
		if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_SMS_GIFTS_REQUEST")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	} else {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_SMS_GIFTS_REQUEST",true);
	}

%>
</head>
<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_SMS_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/dispatch/messages/smsupdate.jsp?id=" + sms.getValue("ID_SMS_MESSAGE") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/dispatch/messages/smsupdate.jsp?id=" + sms.getValue("ID_SMS_MESSAGE") + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), sms.getValue("ID_SMS_MESSAGE") + " - " + sms.getValue("NAME_SMS_MESSAGE_TYPE")) %>
			<% } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_SMS_SEND_MESSAGES")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_SMS_SEND_MESSAGES") &&
						("RECEIVE".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")) ||
						"CALL_IN".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")))) {%>
			    	<%= Bean.getMenuButton("REPEAT", "../crm/dispatch/messages/smsupdate.jsp?id=" + sms.getValue("ID_SMS_MESSAGE") + "&type=general&action=repeat&process=no", "", "", Bean.smsXML.getfieldTransl("h_repeat_on_message", false)) %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSendMessages, "../crm/dispatch/messages/smsspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_SMS_SEND_MESSAGES")+"&", "send_page") %>
			<% } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_SMS_ACTION")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagActions, "../crm/dispatch/messages/smsspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_SMS_ACTION")+"&", "action_page") %>
			<% } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_SMS_GIFTS_REQUEST")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_SMS_GIFTS_REQUEST") &&
						("RECEIVE".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")) ||
						"CALL_IN".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) &&
						!(sms.getValue("ID_NAT_PRS")==null || "".equalsIgnoreCase(sms.getValue("ID_NAT_PRS")))) {%>
			    	<%= Bean.getMenuButton("RUN", "../crm/dispatch/messages/smsupdate.jsp?id=" + sms.getValue("ID_SMS_MESSAGE") + "&type=general&action=gift_request&process=no", "", "", Bean.smsXML.getfieldTransl("h_repeat_on_message", false)) %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagGiftsRequest, "../crm/dispatch/messages/smsspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_SMS_GIFTS_REQUEST")+"&", "request_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(sms.getValue("ID_SMS_MESSAGE") + " - " + sms.getValue("NAME_SMS_MESSAGE_TYPE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/dispatch/messages/smsspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_SMS_INFO")) {
	boolean isReceiveMessage = false;
	
	if (!("SEND".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")))) {
		isReceiveMessage = true;
	}
	
	%>

		<script>
			var formData = new Array (
				new Array ('id_sms_profile', 'varchar2', 1),
				new Array ('text_message', 'varchar2', 1),
				new Array ('cd_sms_state', 'varchar2', 1),
				new Array ('is_archive', 'varchar2', 1)
			);
		</script>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>
		<%= Bean.getMessageLengthTextAreaInitialScript("text_message_parent", "length_message_parent") %>

		<form action="../crm/dispatch/messages/smsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<% if (isReceiveMessage) { %>
			<input type="hidden" name="action" value="edit_short">
			<% } else { %>
			<input type="hidden" name="action" value="edit">
			<% } %>
		   	<input type="hidden" name="process" value="yes">
		   	<input type="hidden" name="id" value="<%= id %>">
			<input type="hidden" name="cd_dispatch_kind" id="cd_dispatch_kind" value="<%= sms.getValue("CD_DISPATCH_KIND") %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("id_sms_message", false) %></td> <td><input type="text" name="id_sms_message" size="16" value="<%= id %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(sms.getValue("ID_CLUB")) %>
				</td>
				<td>
					<input type="text" name="name_club" size="35" value="<%= Bean.getClubShortName(sms.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
				</td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_message_type", false) %> </td> <td align="left"><input type="text" name="name_sms_message_type" size="35" value="<%= sms.getValue("NAME_SMS_MESSAGE_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_receiver_dispatch_kind", false) %> </td> <td align="left"><input type="text" name="name_dispatch_kind" size="35" value="<%= sms.getValue("NAME_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<% if (isReceiveMessage) { %>
				<td><%= Bean.smsXML.getfieldTransl("begin_action_date", false) %> </td> <td align="left"><input type="text" name="begin_action_date" size="16" value="<%= sms.getValue("BEGIN_ACTION_DATE_DHMF") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
				<td><%= Bean.smsXML.getfieldTransl("begin_action_date", false) %></td><td><%=Bean.getCalendarInputField("begin_action_date", sms.getValue("BEGIN_ACTION_DATE_DHMF"), "16") %></td>
				<% } %>
				<% if ("CLIENT".equalsIgnoreCase(sms.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(sms.getValue("ID_NAT_PRS")) %>
				</td>
	            <td>
					<%= Bean.getWindowFindDSMessageSender("", "sender_receiver", sms.getValue("ID_NAT_PRS"), Bean.getNatPrsName(sms.getValue("ID_NAT_PRS")), "PHONE_MOBILE", sms.getValue("RECEPIENT").replace("+", "%2B"), "35") %>
				</td>
				<% } else if ("PARTNER".equalsIgnoreCase(sms.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(sms.getValue("ID_CONTACT_PRS")) %>
				</td>
	            <td>
					<%= Bean.getWindowFindDSMessageSender("", "sender_receiver", sms.getValue("ID_CONTACT_PRS"), Bean.getContactPrsName(sms.getValue("ID_CONTACT_PRS")), "PHONE_MOBILE", sms.getValue("RECEPIENT").replace("+", "%2B"), "35") %>
				</td>
				<% } else if ("SYSTEM".equalsIgnoreCase(sms.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(sms.getValue("ID_USER")) %>
				</td>
	            <td>
					<%= Bean.getWindowFindDSMessageSender("", "sender_receiver", sms.getValue("ID_USER"), Bean.getUserName(sms.getValue("ID_USER")), "PHONE_MOBILE", sms.getValue("RECEPIENT").replace("+", "%2B"), "35") %>
				</td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			</tr>
			<tr>
				<% if (isReceiveMessage) { %>
				<td><%= Bean.messageXML.getfieldTransl("sms_send_time", false) %></td>
				<td>
					<%= Bean.commonXML.getfieldTransl("h_period_from", false) %>&nbsp;
					<input type="text" name="begin_send_hour" size="5" value="<%= Bean.getMeaningForNumValue("HOURS",sms.getValue("BEGIN_SEND_HOUR")) %>" readonly="readonly" class="inputfield-ro">
					<input type="text" name="begin_send_min" size="5" value="<%= Bean.getMeaningForNumValue("SECONDS",sms.getValue("BEGIN_SEND_MIN")) %>" readonly="readonly" class="inputfield-ro">
					&nbsp;&nbsp;
					<%= Bean.commonXML.getfieldTransl("h_period_to", false) %>&nbsp;
					<input type="text" name="end_send_hour" size="5" value="<%= Bean.getMeaningForNumValue("HOURS",sms.getValue("END_SEND_HOUR")) %>" readonly="readonly" class="inputfield-ro">
					<input type="text" name="end_send_min" size="5" value="<%= Bean.getMeaningForNumValue("SECONDS",sms.getValue("END_SEND_MIN")) %>" readonly="readonly" class="inputfield-ro">
				</td>			
				<td><%= Bean.smsXML.getfieldTransl("recepient", false) %> </td> <td align="left"><input type="text" name="recepient" size="35" value="<%= sms.getValue("RECEPIENT") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
				<td><%= Bean.messageXML.getfieldTransl("sms_send_time", false) %></td>
				<td>
					<%= Bean.commonXML.getfieldTransl("h_period_from", false) %>&nbsp;
					<select name="begin_send_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS",sms.getValue("BEGIN_SEND_HOUR"), true) %></select>
					<select name="begin_send_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS",sms.getValue("BEGIN_SEND_MIN"), true) %></select>
					&nbsp;&nbsp;
					<%= Bean.commonXML.getfieldTransl("h_period_to", false) %>&nbsp;
					<select name="end_send_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS",sms.getValue("END_SEND_HOUR"), true) %></select>
					<select name="end_send_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS",sms.getValue("END_SEND_MIN"), true) %></select>
				</td>			
				<td><%= Bean.smsXML.getfieldTransl("recepient", false) %> </td> <td align="left"><input type="text" name="recepient" size="35" value="<%= sms.getValue("RECEPIENT") %>" class="inputfield"></td>
				<% } %>
			</tr>
			<tr>
				<% if (isReceiveMessage) { %>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_profile", false) %>
					<%= Bean.getGoToDispatchSMSProfileLink(sms.getValue("ID_SMS_PROFILE")) %>
				</td> <td align="left"><input type="text" name="name_sms_profile" size="72" value="<%= sms.getValue("NAME_SMS_PROFILE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_state", false) %> </td> <td align="left"><input type="text" name="name_sms_state" size="35" value="<%= sms.getValue("NAME_SMS_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
				<td><%= Bean.smsXML.getfieldTransl("sms_profile", true) %>
					<%= Bean.getGoToDispatchSMSProfileLink(sms.getValue("ID_SMS_PROFILE")) %>
				</td><td><select name="id_sms_profile" class="inputfield"><%= Bean.getDispatchClientSMSProfileOptions(sms.getValue("ID_SMS_PROFILE"),true) %></select></td>			
				<td><%= Bean.smsXML.getfieldTransl("cd_sms_state", true) %></td><td><select name="cd_sms_state" class="inputfield"><%= Bean.getSMSStateOptions(sms.getValue("CD_SMS_STATE"), false) %></select></td>			
				<% } %>
			</tr>
			<tr>
				<% if (isReceiveMessage) { %>
				<td rowspan="2"><%= Bean.smsXML.getfieldTransl("text_message", false) %></td> <td rowspan="2"><textarea name="text_message" id="text_message" cols="70" rows="3" readonly="readonly" class="inputfield-ro" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>><%= sms.getValue("TEXT_MESSAGE") %></textarea></td>
				<td><%= Bean.smsXML.getfieldTransl("is_archive", false) %> </td> <td align="left"><input type="text" name="is_archive" size="20" value="<%= sms.getValue("IS_ARCHIVE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
				<td rowspan="2"><%= Bean.smsXML.getfieldTransl("text_message", true) %></td> <td rowspan="2"><textarea name="text_message" id="text_message" cols="70" rows="3" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>><%= sms.getValue("TEXT_MESSAGE") %></textarea></td>
				<td><%= Bean.smsXML.getfieldTransl("is_archive", true) %> </td> <td align="left"><select name="is_archive" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", sms.getValue("IS_ARCHIVE"), true) %></select></td>
				<% } %>
			</tr>
			<tr>
				<%
					String pEventDateCaption = "event_date";
					if ("SEND".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) {
						pEventDateCaption = "last_send_date";
					} else if ("RECEIVE".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) {
						pEventDateCaption = "receive_date";
					} else if ("CALL_IN".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) {
						pEventDateCaption = "call_in_date";
					} else if ("CALL_OUT".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) {
						pEventDateCaption = "call_out_date";
					}
				%>
				<td class="top_line"><%= Bean.smsXML.getfieldTransl(pEventDateCaption, false) %> </td> <td align="left" class="top_line"><input type="text" name="event_date" size="20" value="<%= sms.getValue("EVENT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.smsXML.getfieldTransl("repeat_count", false) %> </td> <td align="left"><input type="text" name="repeat_count" size="20" value="<%= sms.getValue("REPEAT_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td rowspan="3"><%= Bean.smsXML.getfieldTransl("note_message", false) %></td> <td rowspan="3"><textarea name="note_message" cols="70" rows="3" class="inputfield"><%= sms.getValue("NOTE_MESSAGE") %></textarea></td>
				<td><%= Bean.smsXML.getfieldTransl("send_count", false) %> </td> <td align="left"><input type="text" name="send_count" size="20" value="<%= sms.getValue("SEND_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("error_sendings_quantity", false) %> </td> <td align="left"><input type="text" name="error_sendings_quantity" size="20" value="<%= sms.getValue("ERROR_SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("error_message", false) %> </td> <td align="left"><input type="text" name="sms_error_message" size="35" value="<%= sms.getValue("SMS_ERROR_MESSAGE") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/smsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/sms.jsp") %>
				</td>
			</tr>
		<% if (hasParentMessage) { %>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.smsXML.getfieldTransl("h_input_message", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("id_sms_message", false) %>
					<%= Bean.getGoToDispatchSMSLink(sms_parent.getValue("ID_SMS_MESSAGE")) %></td> 
				<td><input type="text" name="id_sms_message_parent" size="16" value="<%= sms_parent.getValue("ID_SMS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_sender_dispatch_kind", false) %> </td> <td align="left"><input type="text" name="cd_ds_sender_dispatch_kind" size="35" value="<%= sms_parent.getValue("NAME_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td rowspan="2"><%= Bean.smsXML.getfieldTransl("text_message", false) %></td> <td rowspan="2"><textarea name="text_message_parent" id="text_message_parent" cols="70" rows="2" readonly="readonly" class="inputfield-ro"><%= sms_parent.getValue("TEXT_MESSAGE") %></textarea></td>
				<% if ("CLIENT".equalsIgnoreCase(sms_parent.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(sms_parent.getValue("ID_NAT_PRS")) %>
				</td><td><input type="text" name="id_nat_prs_sender" size="35" value="<%= Bean.getNatPrsName(sms_parent.getValue("ID_NAT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else if ("PARTNER".equalsIgnoreCase(sms_parent.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(sms_parent.getValue("ID_CONTACT_PRS")) %>
				</td><td><input type="text" name="id_contact_prs_sender" size="35" value="<%= Bean.getContactPrsName(sms_parent.getValue("ID_CONTACT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else if ("SYSTEM".equalsIgnoreCase(sms_parent.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(sms_parent.getValue("ID_USER")) %>
				</td><td><input type="text" name="id_user_sender" size="35" value="<%= Bean.getUserName(sms_parent.getValue("ID_USER")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("recepient", false) %> </td> <td align="left"><input type="text" name="recepient_parent" size="35" value="<%= sms_parent.getValue("RECEPIENT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message_parent" id="length_message_parent" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.smsXML.getfieldTransl("receive_date", false) %> </td> <td align="left"><input type="text" name="event_date_parent" size="35" value="<%= sms_parent.getValue("EVENT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
		<% } %>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.smsXML.getfieldTransl("h_source_data", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("id_ds_pattern", false) %>
					<%= Bean.getGoToDispatchMessagePatternLink(sms.getValue("ID_DS_PATTERN")) %>
				</td> <td align="left"><input type="text" name="id_ds_pattern" size="20" value="<%= Bean.getDSPatternName(sms.getValue("ID_DS_PATTERN")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.call_centerXML.getfieldTransl("id_cc_setting", false) %>
					<%= Bean.getGoToCallCenterSettingLink(sms.getValue("ID_CC_SETTING")) %>
				</td> <td align="left"><input type="text" name="id_cc_setting" size="20" value="<%= sms.getValue("ID_CC_SETTING") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("id_quest_int", false) %>
					<%= Bean.getGoToQuestionnaireLink(sms.getValue("ID_QUEST_INT")) %>
				</td> <td align="left"><input type="text" name="id_quest_int" size="20" value="<%= sms.getValue("ID_QUEST_INT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %>
					<%= Bean.getGoToCallCenterQuestionLink(sms.getValue("ID_CC_QUESTION")) %>
				</td> <td align="left"><input type="text" name="id_cc_question" size="20" value="<%= sms.getValue("ID_CC_QUESTION") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("id_club_event", false) %>
					<%= Bean.getGoToClubEventLink(sms.getValue("ID_CLUB_EVENT")) %>
				</td> <td align="left"><input type="text" name="id_club_event" size="20" value="<%= sms.getValue("ID_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				sms.getValue(Bean.getCreationDateFieldName()),
				sms.getValue("CREATED_BY"),
				sms.getValue(Bean.getLastUpdateDateFieldName()),
				sms.getValue("LAST_UPDATE_BY")
			) %>
	
		</table>
		</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<% if (!isReceiveMessage) { %>
		<%= Bean.getCalendarScript("begin_action_date", true) %>
		<% } %>

<% } 
	if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("DISPATCH_MESSAGES_SMS_INFO")) { %>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>
		<%= Bean.getMessageLengthTextAreaInitialScript("text_message_parent", "length_message_parent") %>

		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("id_sms_message", false) %></td> <td><input type="text" name="id_sms_message" size="16" value="<%= id %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(sms.getValue("ID_CLUB")) %>
				</td>
				<td>
					<input type="text" name="name_club" size="35" value="<%= Bean.getClubShortName(sms.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro">
				</td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_message_type", false) %> </td> <td align="left"><input type="text" name="name_sms_message_type" size="35" value="<%= sms.getValue("NAME_SMS_MESSAGE_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_receiver_dispatch_kind", false) %> </td> <td align="left"><input type="text" name="cd_ds_receiver_dispatch_kind" size="35" value="<%= sms.getValue("NAME_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("begin_action_date", false) %> </td> <td align="left"><input type="text" name="begin_action_date" size="16" value="<%= sms.getValue("BEGIN_ACTION_DATE_DHMF") %>" readonly="readonly" class="inputfield-ro"></td>
				<% if ("CLIENT".equalsIgnoreCase(sms.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(sms.getValue("ID_NAT_PRS")) %>
				</td><td><input type="text" name="id_nat_prs" size="35" value="<%= Bean.getNatPrsName(sms.getValue("ID_NAT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else if ("PARTNER".equalsIgnoreCase(sms.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(sms.getValue("ID_CONTACT_PRS")) %>
				</td><td><input type="text" name="id_contact_prs" size="35" value="<%= Bean.getContactPrsName(sms.getValue("ID_CONTACT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else if ("SYSTEM".equalsIgnoreCase(sms.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(sms.getValue("ID_USER")) %>
				</td><td><input type="text" name="id_user" size="35" value="<%= Bean.getUserName(sms.getValue("ID_USER")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("sms_send_time", false) %></td>
				<td>
					<%= Bean.commonXML.getfieldTransl("h_period_from", false) %>&nbsp;
					<input type="text" name="begin_send_hour" size="5" value="<%= Bean.getMeaningForNumValue("HOURS",sms.getValue("BEGIN_SEND_HOUR")) %>" readonly="readonly" class="inputfield-ro">
					<input type="text" name="begin_send_min" size="5" value="<%= Bean.getMeaningForNumValue("SECONDS",sms.getValue("BEGIN_SEND_MIN")) %>" readonly="readonly" class="inputfield-ro">
					&nbsp;&nbsp;
					<%= Bean.commonXML.getfieldTransl("h_period_to", false) %>&nbsp;
					<input type="text" name="end_send_hour" size="5" value="<%= Bean.getMeaningForNumValue("HOURS",sms.getValue("END_SEND_HOUR")) %>" readonly="readonly" class="inputfield-ro">
					<input type="text" name="end_send_min" size="5" value="<%= Bean.getMeaningForNumValue("SECONDS",sms.getValue("END_SEND_MIN")) %>" readonly="readonly" class="inputfield-ro">
				</td>			
				<td><%= Bean.smsXML.getfieldTransl("recepient", false) %> </td> <td align="left"><input type="text" name="recepient" size="35" value="<%= sms.getValue("RECEPIENT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_profile", false) %>
					<%= Bean.getGoToDispatchSMSProfileLink(sms.getValue("ID_SMS_PROFILE")) %>
				</td> <td align="left"><input type="text" name="name_sms_profile" size="72" value="<%= sms.getValue("NAME_SMS_PROFILE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_state", false) %> </td> <td align="left"><input type="text" name="name_sms_state" size="35" value="<%= sms.getValue("NAME_SMS_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td rowspan="2"><%= Bean.smsXML.getfieldTransl("text_message", false) %></td> <td rowspan="2"><textarea name="text_message" id="text_message" cols="70" rows="3" readonly="readonly" class="inputfield-ro"><%= sms.getValue("TEXT_MESSAGE") %></textarea></td>
				<td><%= Bean.smsXML.getfieldTransl("is_archive", false) %> </td> <td align="left"><input type="text" name="is_archive" size="20" value="<%= sms.getValue("IS_ARCHIVE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<%
					String pEventDateCaption = "event_date";
					if ("SEND".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) {
						pEventDateCaption = "last_send_date";
					} else if ("RECEIVE".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) {
						pEventDateCaption = "receive_date";
					} else if ("CALL_IN".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) {
						pEventDateCaption = "call_in_date";
					} else if ("CALL_OUT".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) {
						pEventDateCaption = "call_out_date";
					}
				%>
				<td class="top_line"><%= Bean.smsXML.getfieldTransl(pEventDateCaption, false) %> </td> <td align="left" class="top_line"><input type="text" name="event_date" size="20" value="<%= sms.getValue("EVENT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.smsXML.getfieldTransl("repeat_count", false) %> </td> <td align="left"><input type="text" name="repeat_count" size="20" value="<%= sms.getValue("REPEAT_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td rowspan="3"><%= Bean.smsXML.getfieldTransl("note_message", false) %></td> <td rowspan="3"><textarea name="note_message" cols="70" rows="3" readonly="readonly" class="inputfield-ro"><%= sms.getValue("NOTE_MESSAGE") %></textarea></td>
				<td><%= Bean.smsXML.getfieldTransl("send_count", false) %> </td> <td align="left"><input type="text" name="send_count" size="20" value="<%= sms.getValue("SEND_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("error_sendings_quantity", false) %> </td> <td align="left"><input type="text" name="error_sendings_quantity" size="20" value="<%= sms.getValue("ERROR_SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("error_message", false) %> </td> <td align="left"><input type="text" name="sms_error_message" size="35" value="<%= sms.getValue("SMS_ERROR_MESSAGE") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/sms.jsp") %>
				</td>
			</tr>
		<% if (hasParentMessage) { %>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.smsXML.getfieldTransl("h_input_message", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("id_sms_message", false) %>
					<%= Bean.getGoToDispatchSMSLink(sms_parent.getValue("ID_SMS_MESSAGE")) %></td> 
				<td><input type="text" name="id_sms_message_parent" size="16" value="<%= sms_parent.getValue("ID_SMS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_sender_dispatch_kind", false) %> </td> <td align="left"><input type="text" name="cd_ds_sender_dispatch_kind" size="35" value="<%= sms_parent.getValue("NAME_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td rowspan="2"><%= Bean.smsXML.getfieldTransl("text_message", false) %></td> <td rowspan="2"><textarea name="text_message_parent" id="text_message_parent" cols="70" rows="2" readonly="readonly" class="inputfield-ro"><%= sms_parent.getValue("TEXT_MESSAGE") %></textarea></td>
				<% if ("CLIENT".equalsIgnoreCase(sms_parent.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(sms_parent.getValue("ID_NAT_PRS")) %>
				</td><td><input type="text" name="id_nat_prs_sender" size="35" value="<%= Bean.getNatPrsName(sms_parent.getValue("ID_NAT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else if ("PARTNER".equalsIgnoreCase(sms_parent.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(sms_parent.getValue("ID_CONTACT_PRS")) %>
				</td><td><input type="text" name="id_contact_prs_sender" size="35" value="<%= Bean.getContactPrsName(sms_parent.getValue("ID_CONTACT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else if ("SYSTEM".equalsIgnoreCase(sms_parent.getValue("CD_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(sms_parent.getValue("ID_USER")) %>
				</td><td><input type="text" name="id_user_sender" size="35" value="<%= Bean.getUserName(sms_parent.getValue("ID_USER")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("recepient", false) %> </td> <td align="left"><input type="text" name="recepient_parent" size="35" value="<%= sms_parent.getValue("RECEPIENT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message_parent" id="length_message_parent" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.smsXML.getfieldTransl("receive_date", false) %> </td> <td align="left"><input type="text" name="event_date_parent" size="35" value="<%= sms_parent.getValue("EVENT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
		<% } %>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.smsXML.getfieldTransl("h_source_data", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("id_ds_pattern", false) %>
					<%= Bean.getGoToDispatchMessagePatternLink(sms.getValue("ID_DS_PATTERN")) %>
				</td> <td align="left"><input type="text" name="id_ds_pattern" size="20" value="<%= Bean.getDSPatternName(sms.getValue("ID_DS_PATTERN")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.call_centerXML.getfieldTransl("id_cc_setting", false) %>
					<%= Bean.getGoToCallCenterSettingLink(sms.getValue("ID_CC_SETTING")) %>
				</td> <td align="left"><input type="text" name="id_cc_setting" size="20" value="<%= sms.getValue("ID_CC_SETTING") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("id_quest_int", false) %>
					<%= Bean.getGoToQuestionnaireLink(sms.getValue("ID_QUEST_INT")) %>
				</td> <td align="left"><input type="text" name="id_quest_int" size="20" value="<%= sms.getValue("ID_QUEST_INT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %>
					<%= Bean.getGoToCallCenterQuestionLink(sms.getValue("ID_CC_QUESTION")) %>
				</td> <td align="left"><input type="text" name="id_cc_question" size="20" value="<%= sms.getValue("ID_CC_QUESTION") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("id_club_event", false) %>
					<%= Bean.getGoToClubEventLink(sms.getValue("ID_CLUB_EVENT")) %>
				</td> <td align="left"><input type="text" name="id_club_event" size="20" value="<%= sms.getValue("ID_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					sms.getValue(Bean.getCreationDateFieldName()),
					sms.getValue("CREATED_BY"),
					sms.getValue(Bean.getLastUpdateDateFieldName()),
					sms.getValue("LAST_UPDATE_BY")
				) %>
	
	
		</table>

<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_SMS_SEND_MESSAGES") &&
		("RECEIVE".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")) ||
				"CALL_IN".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")))) { %>
	<%= sms.getRelatedSMSHTML(l_send_page_beg, l_send_page_end) %>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_SMS_ACTION") &&
		"SEND".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE"))) { %>
	<%= sms.getActionsHTML("", "", l_action_page_beg, l_action_page_end) %>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_SMS_GIFTS_REQUEST") &&
		("RECEIVE".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")) ||
				"CALL_IN".equalsIgnoreCase(sms.getValue("CD_SMS_MESSAGE_TYPE")))) { %>
	<%= sms.getGiftsRequestsHTML(l_request_page_beg, l_request_page_end) %>
<% } %>

<%   } %>
</div></div>
</body>
</html>
