<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcRoleObject"%>
<%@page import="bc.objects.bcEmailMessageObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLEncoder"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_EMAIL";
String tagSend = "_SEND";
String tagSendFind = "_SEND_FIND";
String tagSendState = "_SEND_STATE";
String tagReceiver = "_RECEIVER";
String tagReceiverOperationType = "_RECEIVER_OPERATION_TYPE";
String tagReceiverFind = "_RECEIVER_FIND";
String tagReceiverState = "_RECEIVER_STATE";
String tagReceiverArchive = "_RECEIVER_ARCHIVE";
String tagReceiverDispatchKind = "_RECEIVER_DISPATCH_KIND";
String tagResend = "_RESEND";
String tagResendFind = "_RESEND_FIND";
String tagResendState = "_RESEND_STATE";
String tagResendIsArchive = "_RESEND_IS_ARCHIVE";
String tagResendDispatchKind = "_RESEND_DISPATCH_KIND";
String tagResendMessageOperType = "_RESEND_MESSAGE_OPER_TYPE";


Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcEmailMessageObject message = new bcEmailMessageObject(id);
	
	bcEmailMessageObject message_parent = null;
	boolean hasParentMessage = false;
	
	if (!(message.getValue("ID_DS_MESSAGE_PARENT") == null || 
			"".equalsIgnoreCase(message.getValue("ID_DS_MESSAGE_PARENT")))) {
		message_parent = new bcEmailMessageObject(message.getValue("ID_DS_MESSAGE_PARENT"));
		hasParentMessage = true;
	}

	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_RECEIVERS", true);
	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_SEND", true);
	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_RESEND", true);
	
	if ("RECEIVE".equalsIgnoreCase(message.getValue("CD_DS_MESSAGE_OPER_TYPE"))) {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_RECEIVERS", false);
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_SEND", false);
		if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_EMAIL_RECEIVERS") ||
				Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_EMAIL_SEND")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	} else if ("SEND".equalsIgnoreCase(message.getValue("CD_DS_MESSAGE_OPER_TYPE"))) {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_RESEND", false);
		if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_EMAIL_RESEND")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	}

	String l_receiver_page = Bean.getDecodeParam(parameters.get("receiver_page"));
	Bean.pageCheck(pageFormName + tagReceiver, l_receiver_page);
	String l_receiver_page_beg = Bean.getFirstRowNumber(pageFormName + tagReceiver);
	String l_receiver_page_end = Bean.getLastRowNumber(pageFormName + tagReceiver);

	String receiver_operation_type 	= Bean.getDecodeParam(parameters.get("receiver_operation_type"));
	receiver_operation_type 	= Bean.checkFindString(pageFormName + tagReceiverOperationType, receiver_operation_type, l_receiver_page);

	String receiver_find 	= Bean.getDecodeParam(parameters.get("receiver_find"));
	receiver_find 	= Bean.checkFindString(pageFormName + tagReceiverFind, receiver_find, l_receiver_page);

	String receiver_state 	= Bean.getDecodeParam(parameters.get("receiver_state"));
	receiver_state 	= Bean.checkFindString(pageFormName + tagReceiverState, receiver_state, l_receiver_page);

	String receiver_archive 	= Bean.getDecodeParam(parameters.get("receiver_archive"));
	receiver_archive 	= Bean.checkFindString(pageFormName + tagReceiverArchive, receiver_archive, l_receiver_page);

	String receiver_dispatch_kind 	= Bean.getDecodeParam(parameters.get("receiver_dispatch_kind"));
	receiver_dispatch_kind 	= Bean.checkFindString(pageFormName + tagReceiverDispatchKind, receiver_dispatch_kind, l_receiver_page);

	String l_send_page = Bean.getDecodeParam(parameters.get("send_page"));
	Bean.pageCheck(pageFormName + tagSend, l_send_page);
	String l_send_page_beg = Bean.getFirstRowNumber(pageFormName + tagSend);
	String l_send_page_end = Bean.getLastRowNumber(pageFormName + tagSend);

	String send_find 	= Bean.getDecodeParam(parameters.get("send_find"));
	send_find 	= Bean.checkFindString(pageFormName + tagSendFind, send_find, l_send_page);

	String send_state 	= Bean.getDecodeParam(parameters.get("send_state"));
	send_state 	= Bean.checkFindString(pageFormName + tagSendState, send_state, l_send_page);
	 
	String l_resend_page = Bean.getDecodeParam(parameters.get("resend_page"));
	Bean.pageCheck(pageFormName + tagResend, l_resend_page);
	String l_resend_page_beg = Bean.getFirstRowNumber(pageFormName + tagResend);
	String l_resend_page_end = Bean.getLastRowNumber(pageFormName + tagResend);

	String resend_find 	= Bean.getDecodeParam(parameters.get("resend_find"));
	resend_find 	= Bean.checkFindString(pageFormName + tagResendFind, resend_find, l_resend_page);

	String resend_state 	= Bean.getDecodeParam(parameters.get("resend_state"));
	resend_state 	= Bean.checkFindString(pageFormName + tagResendState, resend_state, l_resend_page);

	String resend_archive 	= Bean.getDecodeParam(parameters.get("resend_archive"));
	resend_archive 	= Bean.checkFindString(pageFormName + tagResendIsArchive, resend_archive, l_resend_page);

	String resend_dispatch_kind 	= Bean.getDecodeParam(parameters.get("resend_dispatch_kind"));
	resend_dispatch_kind 	= Bean.checkFindString(pageFormName + tagResendDispatchKind, resend_dispatch_kind, l_resend_page);

	String resend_oper_type 	= Bean.getDecodeParam(parameters.get("resend_oper_type"));
	resend_oper_type 	= Bean.checkFindString(pageFormName + tagResendMessageOperType, resend_oper_type, l_resend_page);

%>

<body topmargin="0">
<div id="div_tabsheet">
	<% String messageCaption = message.getValue("ID_DS_MESSAGE"); 
		if (message.getValue("EMAIL")==null || "".equalsIgnoreCase(message.getValue("EMAIL"))) {
			messageCaption = messageCaption + " - " + message.getValue("NAME_DS_MESSAGE_OPER_TYPE");
		} else {
			messageCaption = messageCaption + " - " + message.getValue("EMAIL");
		}
	%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_EMAIL_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/dispatch/messages/email_messagesupdate.jsp?id=" + message.getValue("ID_DS_MESSAGE") + "&type=general&action=add2&process=no", "", "") %>
		    	<%= Bean.getMenuButton("DELETE", "../crm/dispatch/messages/email_messagesupdate.jsp?id=" + message.getValue("ID_DS_MESSAGE") + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), messageCaption) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_EMAIL_RECEIVERS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_EMAIL_RECEIVERS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/dispatch/messages/email_messagesupdate.jsp?id=" + message.getValue("ID_DS_MESSAGE") + "&type=receiver&action=add&process=no", "", "") %>
			<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReceiver, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_EMAIL_RECEIVERS")+"&", "receiver_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_EMAIL_SEND")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSend, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_EMAIL_SEND")+"&", "send_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_EMAIL_RESEND")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagResend, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_EMAIL_RESEND")+"&", "resend_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(messageCaption) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_EMAIL_INFO")) { 
	
	if ("SEND".equalsIgnoreCase(message.getValue("CD_DS_MESSAGE_OPER_TYPE"))) {
		
%>
		<%= Bean.getMessageLengthTextAreaInitialScript("text_ds_message", "length_message") %>
	
			<script>
			function attachFile(){
				var myFile = document.getElementById('attached_file_name').value;
				if (myFile != "") {
					document.getElementById('attach_file').value = "Y";
				}
				return true;
			}
			</script>
	   		<script>
				var formData = new Array (
					new Array ('basis_for_operation', 'varchar2', 1),
					new Array ('name_email_profile', 'varchar2', 1),
					new Array ('begin_action_date', 'varchar2', 1),
					new Array ('state_operation', 'varchar2', 1),
					new Array ('is_archive', 'varchar2', 1)
				);
			</script>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_ds_message", "length_message") %>

	<form action="../crm/dispatch/messages/email_messagesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= id %>">
		<input type="hidden" name="attach_file" id="attach_file" value="N">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("ID_MESSAGE", false) %></td> <td><input type="text" name="id_ds_message" size="20" value="<%= message.getValue("ID_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(message.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(message.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("name_ds_message_oper_type", false) %></td> <td><input type="text" name="name_ds_message_oper_type" size="20" value="<%= message.getValue("NAME_DS_MESSAGE_OPER_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("state_operation", true) %></td><td valign="top"><select name="cd_ds_message_state" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", message.getValue("CD_DS_MESSAGE_STATE"), true) %></select></td>
		</tr>
		<tr>
			<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("basis_for_operation", true) %></td><td valign="top" rowspan="3"><textarea name="basis_for_operation" cols="60" rows="4" class="inputfield"><%= message.getValue("BASIS_FOR_OPERATION") %></textarea></td>
			<td><%= Bean.messageXML.getfieldTransl("begin_action_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_action_date", message.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("end_action_date", false) %></td> <td><%=Bean.getCalendarInputField("end_action_date", message.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("is_archive", true) %> </td> <td align="left"><select name="is_archive" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", message.getValue("IS_ARCHIVE"), true) %></select></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.messageXML.getfieldTransl("receivers_count", false) %></td> <td class="top_line"><input type="text" name="receivers_count" size="20" value="<%= message.getValue("RECEIVERS_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td class="top_line"><%= Bean.messageXML.getfieldTransl("sendings_quantity", false) %></td> <td class="top_line"><input type="text" name="sendings_quantity" size="10" value="<%= message.getValue("SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("email_profile", true) %>
				<%= Bean.getGoToDispatchEmailProfileLink(message.getValue("ID_EMAIL_PROFILE")) %>
			</td> 
			<td>
				<%=Bean.getWindowEmailProfiles("email_profile", message.getValue("ID_EMAIL_PROFILE"), "50") %>
			</td>			
			<td><%= Bean.messageXML.getfieldTransl("error_sendings_quantity", false) %></td> <td><input type="text" name="error_sendings_quantity" size="10" value="<%= message.getValue("ERROR_SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td> <td><input type="text" name="title_ds_message" size="65" value="<%= message.getValue("TITLE_DS_MESSAGE") %>" class="inputfield"> </td>
			<td><%= Bean.messageXML.getfieldTransl("last_send_date", false) %></td> <td><input type="text" name="event_date" size="20" value="<%= message.getValue("EVENT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top"  colspan="3"><textarea name="text_ds_message" id="text_ds_message" cols="120" rows="12" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>><%= message.getValue("TEXT_DS_MESSAGE") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("message_file_name", false) %></td>
			<td valign="middle">
				<% if (!(message.getValue("STORED_MESSAGE_FILE_NAME")==null || "".equalsIgnoreCase(message.getValue("STORED_MESSAGE_FILE_NAME")))) { %>
					<%=message.getValue("MESSAGE_FILE_NAME") %><a href="../FileSender?FILENAME=<%=URLEncoder.encode(message.getValue("STORED_MESSAGE_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
						<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
					</a>
					<a href="#" onclick="if (window.confirm('<%=Bean.documentXML.getfieldTransl("l_remove_file", false) %> \'<%=message.getValue("MESSAGE_FILE_NAME") %>\'?')) {ajaxpage('../crm/dispatch/messages/email_messagesupdate.jsp?id=<%=id%>&type=email&process=yes&action=remove_file', 'div_main')}" title="<%= Bean.buttonXML.getfieldTransl("button_delete", false) %>">
						<img vspace="0" hspace="0" src="../images/oper/small/delete.gif" align="top">
					</a>
					<br>
				<% } else {%>
					<input type="file" name="message_file_name" size="50" value="<%=message.getValue("MESSAGE_FILE_NAME") %>" class="inputfield">
				<% } %>
			</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonMultiPart("../crm/dispatch/messages/email_messagesupdate.jsp","submit","updateForm","document.getElementById('attach_file').value='N'") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messages.jsp") %>
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("h_attached_files", false) %></td>
			<td>
				<%= message.getAttachedFilesHTML(true) %>
			<input type="file" name="attached_file_name" id="attached_file_name" size="50" value="" class="inputfield" onchange="attachFile();">
				<%=Bean.getSubmitButtonMultiPart("../crm/dispatch/messages/email_messagesupdate.jsp","attach","updateForm") %>
			</td>
		</tr>
		<tr>
			<td valign="top">&nbsp;</td>
		</tr>
		<% if (hasParentMessage) { %>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.smsXML.getfieldTransl("h_input_message", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("ID_MESSAGE", false) %>
					<%= Bean.getGoToDispatchEmailLink(message_parent.getValue("ID_DS_MESSAGE")) %></td>
				<td><input type="text" name="id_ds_message_parent" size="16" value="<%= message_parent.getValue("ID_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_sender_dispatch_kind", false) %> </td> <td align="left"><input type="text" name="cd_ds_sender_dispatch_kind" size="35" value="<%= message_parent.getValue("NAME_DS_SENDER_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td rowspan="3"><%= Bean.messageXML.getfieldTransl("title_message", false) %></td> <td rowspan="3"><textarea name="title_message_parent" cols="70" rows="3" readonly="readonly" class="inputfield-ro"><%= message_parent.getValue("TITLE_DS_MESSAGE") %></textarea></td>
				<% if ("CLIENT".equalsIgnoreCase(message_parent.getValue("CD_DS_SENDER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(message_parent.getValue("ID_NAT_PRS_SENDER")) %>
				</td><td><input type="text" name="id_nat_prs_sender" size="35" value="<%= Bean.getNatPrsName(message_parent.getValue("ID_NAT_PRS_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else if ("PARTNER".equalsIgnoreCase(message_parent.getValue("CD_DS_SENDER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(message_parent.getValue("ID_CONTACT_PRS_SENDER")) %>
				</td><td><input type="text" name="id_contact_prs_sender" size="35" value="<%= Bean.getContactPrsName(message_parent.getValue("ID_CONTACT_PRS_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else if ("SYSTEM".equalsIgnoreCase(message_parent.getValue("CD_DS_SENDER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(message_parent.getValue("ID_USER_SENDER")) %>
				</td><td><input type="text" name="id_user_sender" size="35" value="<%= Bean.getUserName(message_parent.getValue("ID_USER_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("email", false) %> </td> <td align="left"><input type="text" name="input_email" size="35" value="<%= message_parent.getValue("EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("receive_date", false) %> </td> <td align="left"><input type="text" name="event_date_parent" size="35" value="<%= message_parent.getValue("EVENT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
		<% } %>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.smsXML.getfieldTransl("h_source_data", false) %></b></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("id_ds_pattern", false) %>
					<%= Bean.getGoToDispatchMessagePatternLink(message.getValue("ID_DS_PATTERN")) %>
				</td> <td align="left"><input type="text" name="id_ds_pattern" size="20" value="<%= Bean.getDSPatternName(message.getValue("ID_DS_PATTERN")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.call_centerXML.getfieldTransl("id_cc_setting", false) %>
					<%= Bean.getGoToCallCenterSettingLink(message.getValue("ID_CC_SETTING")) %>
				</td> <td align="left"><input type="text" name="id_cc_setting" size="20" value="<%= message.getValue("ID_CC_SETTING") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("id_quest_int", false) %>
					<%= Bean.getGoToQuestionnaireLink(message.getValue("ID_QUEST_INT")) %>
				</td> <td align="left"><input type="text" name="id_quest_int" size="20" value="<%= message.getValue("ID_QUEST_INT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %>
					<%= Bean.getGoToCallCenterQuestionLink(message.getValue("ID_CC_QUESTION")) %>
				</td> <td align="left"><input type="text" name="id_cc_question" size="20" value="<%= message.getValue("ID_CC_QUESTION") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("id_club_event", false) %>
					<%= Bean.getGoToClubEventLink(message.getValue("ID_CLUB_EVENT")) %>
				</td> <td align="left"><input type="text" name="id_club_event" size="20" value="<%= message.getValue("ID_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				message.getValue(Bean.getCreationDateFieldName()),
				message.getValue("CREATED_BY"),
				message.getValue(Bean.getLastUpdateDateFieldName()),
				message.getValue("LAST_UPDATE_BY")
			) %>

	</table>
	</form> 
	
	<%= Bean.getCalendarScript("begin_action_date", false) %>
	<%= Bean.getCalendarScript("end_action_date", false) %>

	<% } else if ("RECEIVE".equalsIgnoreCase(message.getValue("CD_DS_MESSAGE_OPER_TYPE"))) {%>
	<form action="../crm/dispatch/messages/email_messagesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="edit_receive">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= id %>">
		<input type="hidden" name="attach_file" id="attach_file" value="N">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("ID_MESSAGE", false) %></td> <td><input type="text" name="id_ds_message" size="30" value="<%= message.getValue("ID_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(message.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="35" value="<%= Bean.getClubShortName(message.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("name_ds_message_oper_type", false) %></td><td valign="top"><input type="text" name="name_ds_message_oper_type" size="30" value="<%= message.getValue("NAME_DS_MESSAGE_OPER_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("name_ds_sender_dispatch_kind", false) %></td><td valign="top"><input type="text" name="name_ds_sender_dispatch_kind" size="35" value="<%= message.getValue("NAME_DS_SENDER_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("receive_date", false) %></td> <td><input type="text" name="event_date" size="30" value="<%= message.getValue("EVENT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
				<% if ("CLIENT".equalsIgnoreCase(message.getValue("CD_DS_SENDER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(message.getValue("ID_NAT_PRS_SENDER")) %>
				</td>
	            <td>
					<%= Bean.getWindowFindDSMessageSender("", "sender_receiver", message.getValue("ID_NAT_PRS_SENDER"), Bean.getNatPrsName(message.getValue("ID_NAT_PRS_SENDER")), "EMAIL", message.getValue("EMAIL").replace("+", "%2B"), "35") %>
				</td>
				<% } else if ("PARTNER".equalsIgnoreCase(message.getValue("CD_DS_SENDER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(message.getValue("ID_CONTACT_PRS_SENDER")) %>
				</td>
	            <td>
					<%= Bean.getWindowFindDSMessageSender("", "sender_receiver", message.getValue("ID_CONTACT_PRS_SENDER"), Bean.getContactPrsName(message.getValue("ID_CONTACT_PRS_SENDER")), "EMAIL", message.getValue("EMAIL").replace("+", "%2B"), "35") %>
				</td>
				<% } else if ("SYSTEM".equalsIgnoreCase(message.getValue("CD_DS_SENDER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(message.getValue("ID_USER_SENDER")) %>
				</td>
	            <td>
					<%= Bean.getWindowFindDSMessageSender("", "sender_receiver", message.getValue("ID_USER_SENDER"), Bean.getUserName(message.getValue("ID_USER_SENDER")), "EMAIL", message.getValue("EMAIL").replace("+", "%2B"), "35") %>
				</td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("email_profile", false) %>
				<%= Bean.getGoToDispatchEmailProfileLink(message.getValue("ID_EMAIL_PROFILE")) %>
			</td> 
			<td>
				<input type="text" name="name_email_profile" size="65" value="<%= Bean.getDSEmailProfileName(message.getValue("ID_EMAIL_PROFILE")) %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<td><%= Bean.messageXML.getfieldTransl("email", false) %></td> <td><input type="text" name="email" size="35" value="<%= message.getValue("EMAIL") %>"  readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td> <td><input type="text" name="title_ds_message" size="65" value="<%= message.getValue("TITLE_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.smsXML.getfieldTransl("is_archive", false) %> </td> <td align="left"><input type="text" name="is_archive" size="16" value="<%= message.getValue("IS_ARCHIVE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top"  colspan="3"><textarea name="text_ds_message" id="text_ds_message" cols="120" rows="12" readonly="readonly" class="inputfield-ro" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>><%= message.getValue("TEXT_DS_MESSAGE") %></textarea></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/email_messagesupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messages.jsp") %>
			</td>
		</tr>
		<% if (!(message.getValue("STORED_MESSAGE_FILE_NAME")==null || "".equalsIgnoreCase(message.getValue("STORED_MESSAGE_FILE_NAME")))) { %>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("message_file_name", false) %></td>
			<td valign="middle">
				<%=message.getValue("MESSAGE_FILE_NAME") %><a href="../FileSender?FILENAME=<%=URLEncoder.encode(message.getValue("STORED_MESSAGE_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
					<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
				</a>
				<br>
			</td>
		</tr>
		<% } %>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("h_attached_files", false) %></td>
			<td>
				<%= message.getAttachedFilesHTML(false) %>
			</td>
		</tr>
		<tr>
			<td valign="top">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				message.getValue(Bean.getCreationDateFieldName()),
				message.getValue("CREATED_BY"),
				message.getValue(Bean.getLastUpdateDateFieldName()),
				message.getValue("LAST_UPDATE_BY")
			) %>
		</table>
		</form>
<% }

} if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("DISPATCH_MESSAGES_EMAIL_INFO")) {%>
 
		<% if ("SEND".equalsIgnoreCase(message.getValue("CD_DS_MESSAGE_OPER_TYPE"))) {
		%>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_ds_message", "length_message") %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("ID_MESSAGE", false) %></td> <td><input type="text" name="id_ds_message" size="20" value="<%= message.getValue("ID_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(message.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(message.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_pattern", false) %>
				<%= Bean.getGoToDispatchClientPatternLink(message.getValue("ID_CL_PATTERN")) %>
			</td><td><input type="text" name="id_cl_pattern" size="20" value="<%= message.getValue("ID_CL_PATTERN") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
				<%= Bean.getGoToNatPrsLink(message.getValue("ID_NAT_PRS")) %>
			</td><td><input type="text" name="full_name_nat_prs" size="30" value="<%= message.getValue("FULL_NAME_NAT_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top" rowspan="4"><%= Bean.messageXML.getfieldTransl("basis_for_operation", false) %></td><td valign="top" rowspan="4"><textarea name="basis_for_operation" cols="60" rows="5" readonly="readonly" class="inputfield-ro"><%= message.getValue("BASIS_FOR_OPERATION") %></textarea></td>
			<td class="bottom_line"><%= Bean.messageXML.getfieldTransl("email", false) %></td> <td class="bottom_line"><input type="text" name="email" size="30" value="<%= message.getValue("EMAIL") %>"  readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("begin_action_date", false) %></td> <td><input type="text" name="begin_action_date" size="16" value="<%= message.getValue("BEGIN_ACTION_DATE_FRMT") %>"  readonly="readonly" class="inputfield-ro" title="<%=Bean.getDateFormatTitle() %>"></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("end_action_date_frmt", false) %></td> <td><input type="text" name="end_action_date" size="16" value="<%= message.getValue("END_ACTION_DATE_FRMT") %>"  readonly="readonly" class="inputfield-ro" title="<%=Bean.getDateFormatTitle() %>"></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("sendings_quantity", false) %></td> <td><input type="text" name="sendings_quantity" size="16" value="<%= message.getValue("SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("state_operation", false) %></td><td valign="top"><input type="text" name="state_operation" size="30" value="<%= Bean.getMeaningFoCodeValue("SEND_MESSAGE_STATE", message.getValue("STATE_RECORD")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.messageXML.getfieldTransl("error_sendings_quantity", false) %></td> <td><input type="text" name="error_sendings_quantity" size="16" value="<%= message.getValue("ERROR_SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("email_profile", false) %>
				<%= Bean.getGoToDispatchEmailProfileLink(message.getValue("ID_EMAIL_PROFILE")) %>
			</td> 
			<td>
				<input type="text" name="name_email_profile" size="65" value="<%= Bean.getDSEmailProfileName(message.getValue("ID_EMAIL_PROFILE")) %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<td><%= Bean.messageXML.getfieldTransl("event_date", false) %></td> <td><input type="text" name="event_date" size="16" value="<%= message.getValue("EVENT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td> <td><input type="text" name="title_ds_message" size="65" value="<%= message.getValue("TITLE_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.smsXML.getfieldTransl("is_archive", false) %> </td> <td align="left"><input type="text" name="is_archive" size="16" value="<%= message.getValue("IS_ARCHIVE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top"  colspan="3"><textarea name="text_ds_message" id="text_ds_message" cols="120" rows="12" readonly="readonly" class="inputfield-ro" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>><%= message.getValue("TEXT_DS_MESSAGE") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("message_file_name", false) %></td>
			<td valign="middle">
				<% if (!(message.getValue("STORED_MESSAGE_FILE_NAME")==null || "".equalsIgnoreCase(message.getValue("STORED_MESSAGE_FILE_NAME")))) { %>
					<%=message.getValue("MESSAGE_FILE_NAME") %><a href="../FileSender?FILENAME=<%=URLEncoder.encode(message.getValue("STORED_MESSAGE_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
						<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
					</a>
					<br>
				<% } %>
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("h_attached_files", false) %></td>
			<td>
				<%= message.getAttachedFilesHTML(false) %>
			</td>
		</tr>
		<tr>
			<td valign="top">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				message.getValue(Bean.getCreationDateFieldName()),
				message.getValue("CREATED_BY"),
				message.getValue(Bean.getLastUpdateDateFieldName()),
				message.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messages.jsp") %>
			</td>
		</tr>
		</table>
		</form>
	<% } else if ("RECEIVE".equalsIgnoreCase(message.getValue("CD_DS_MESSAGE_OPER_TYPE"))) {%>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("ID_MESSAGE", false) %></td> <td><input type="text" name="id_ds_message" size="20" value="<%= message.getValue("ID_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(message.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(message.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("name_ds_message_oper_type", false) %></td><td valign="top"><input type="text" name="name_ds_message_oper_type" size="30" value="<%= message.getValue("NAME_DS_MESSAGE_OPER_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<% if ("CLIENT".equalsIgnoreCase(message.getValue("CD_DS_SENDER_DISPATCH_KIND"))) { %>
			<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
				<%= Bean.getGoToNatPrsLink(message.getValue("ID_NAT_PRS_SENDER")) %>
			</td><td><input type="text" name="id_nat_prs_sender" size="30" value="<%= Bean.getNatPrsName(message.getValue("ID_NAT_PRS_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else if ("PARTNER".equalsIgnoreCase(message.getValue("CD_DS_SENDER_DISPATCH_KIND"))) { %>
			<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
				<%= Bean.getGoToContactPersonLink(message.getValue("ID_CONTACT_PRS_SENDER")) %>
			</td><td><input type="text" name="id_contact_prs_sender" size="30" value="<%= Bean.getContactPrsName(message.getValue("ID_CONTACT_PRS_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else if ("SYSTEM".equalsIgnoreCase(message.getValue("CD_DS_SENDER_DISPATCH_KIND"))) { %>
			<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
				<%= Bean.getGoToSystemUserLink(message.getValue("ID_USER_SENDER")) %>
			</td><td><input type="text" name="id_user_sender" size="30" value="<%= Bean.getUserName(message.getValue("ID_USER_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("name_ds_sender_dispatch_kind", false) %></td><td valign="top"><input type="text" name="name_ds_sender_dispatch_kind" size="30" value="<%= message.getValue("NAME_DS_SENDER_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.messageXML.getfieldTransl("email", false) %></td> <td><input type="text" name="email" size="30" value="<%= message.getValue("EMAIL") %>"  readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("email_profile", false) %>
				<%= Bean.getGoToDispatchEmailProfileLink(message.getValue("ID_EMAIL_PROFILE")) %>
			</td> 
			<td>
				<input type="text" name="name_email_profile" size="65" value="<%= Bean.getDSEmailProfileName(message.getValue("ID_EMAIL_PROFILE")) %>" readonly="readonly" class="inputfield-ro">
			</td>			
			<td><%= Bean.messageXML.getfieldTransl("receive_date", false) %></td> <td><input type="text" name="event_date" size="30" value="<%= message.getValue("EVENT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td> <td><input type="text" name="title_ds_message" size="65" value="<%= message.getValue("TITLE_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.smsXML.getfieldTransl("is_archive", false) %> </td> <td align="left"><input type="text" name="is_archive" size="16" value="<%= message.getValue("IS_ARCHIVE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top"  colspan="3"><textarea name="text_ds_message" id="text_ds_message" cols="120" rows="12" readonly="readonly" class="inputfield-ro" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>><%= message.getValue("TEXT_DS_MESSAGE") %></textarea></td>
		</tr>
		<% if (!(message.getValue("STORED_MESSAGE_FILE_NAME")==null || "".equalsIgnoreCase(message.getValue("STORED_MESSAGE_FILE_NAME")))) { %>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("message_file_name", false) %></td>
			<td valign="middle">
				<%=message.getValue("MESSAGE_FILE_NAME") %><a href="../FileSender?FILENAME=<%=URLEncoder.encode(message.getValue("STORED_MESSAGE_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
					<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
				</a>
				<br>
			</td>
		</tr>
		<% } %>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("h_attached_files", false) %></td>
			<td>
				<%= message.getAttachedFilesHTML(false) %>
			</td>
		</tr>
		<tr>
			<td valign="top">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				message.getValue(Bean.getCreationDateFieldName()),
				message.getValue("CREATED_BY"),
				message.getValue(Bean.getLastUpdateDateFieldName()),
				message.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messages.jsp") %>
			</td>
		</tr>
		</table>
		</form>
	<% } %>


<%		}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_EMAIL_RECEIVERS")) {
		if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_EMAIL_RECEIVERS")) {
			%>

			     <script type="text/javascript">
			     	function CheckSelect(form)  {
			         	var operType = document.getElementById('operation_type').value;
			         	//alert (operType);
			        	for (i = 0; i < form.elements.length; i++) {
			           		var item = form.elements[i];
			           		if (item.name.substr(0,3) == "chb")  {
			           			if (item.checked)  {
			           	         	if (operType == 'send') {
			           	         		return true;
			           	         	} else if (operType == 'delete') {
			           	         		if (confirm('<%= Bean.messageXML.getfieldTransl("h_confirm_delete", false) %>'))  {
			           						return true;
			           					} else {
			            					return false;
			           					}
			           	         	} else if (operType == 'prepare') {
			           	         		return true;
			           	        	} else if (operType == 'to_archive') {
			        	         		return true;
			           	        	} else if (operType == 'from_archive') {
			        	         		return true;
			           	        	} else if (operType == 'delivered') {
			        	         		return true;
			           	        	} else if (operType == 'error') {
			        	         		return true;
			        	         	}
			               			
			           			}
			           		}
			           	}
			       		alert('<%= Bean.messageXML.getfieldTransl("h_not_entered_messages", false) %>');
			        	return false;
			   	 	}

			     	function CheckCB(Element) {
				   	 	myCheck = true;
				
						thisCheckBoxes = document.getElementsByTagName('input');
						for (i = 1; i < thisCheckBoxes.length; i++) { 
							myName = thisCheckBoxes[i].name;
							if (myName.substr(0,3) == 'chb'){
								myCheck = myCheck && thisCheckBoxes[i].checked;
							}
						}
						if (document.getElementById('mainCheck')) {
							document.getElementById('mainCheck').checked = myCheck;
						}
			   	 	}

			   	 	function CheckAll(Element,Name) {
			   	 		thisCheckBoxes = document.getElementsByTagName('input');
			   	 		for (i = 1; i < thisCheckBoxes.length; i++) {
			   				myName = thisCheckBoxes[i].name;
			   				
			   				if (myName.substr(0,3) == Name){
			 					thisCheckBoxes[i].checked = Element.checked;
			   				}
			   	 		}
			   	 	}
			   	 </script>
			<% } %>
		<table <%=Bean.getTableBottomFilter() %>>
		  	<tr>
			<%= Bean.getFindHTML("receiver_find", receiver_find, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&receiver_page=1") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("receiver_dispatch_kind", "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&receiver_page=1", Bean.messageXML.getfieldTransl("name_ds_sender_dispatch_kind", false)) %>
				<%= Bean.getDispatchKindOptions(receiver_dispatch_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("receiver_state", "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&receiver_page=1", Bean.messageXML.getfieldTransl("cd_term_message_state", false)) %>
				<%= Bean.getTermMessageStateOptions(receiver_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("receiver_archive", "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&receiver_page=1", Bean.smsXML.getfieldTransl("is_archive", false)) %>
				<%=Bean.getSelectOptionHTML(receiver_archive, "", "") %>
				<%=Bean.getSelectOptionHTML(receiver_archive, "Y", Bean.messageXML.getfieldTransl("in_archive", false)) %>
				<%=Bean.getSelectOptionHTML(receiver_archive, "N", Bean.messageXML.getfieldTransl("not_in_archive", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		  	</tr>
		</table>
			<%=message.getMessagesReceiver2HTML(receiver_operation_type, receiver_find, receiver_dispatch_kind, receiver_state, receiver_archive, l_receiver_page_beg, l_receiver_page_end) %>
		<%
		}



	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_EMAIL_SEND")) {	%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
			<%= Bean.getFindHTML("send_find", send_find, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&send_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("send_state", "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&send_page=1", Bean.messageXML.getfieldTransl("name_ds_message_send_state", false)) %>
				<%= Bean.getMeaningFromLookupNameOptions("SEND_MESSAGE_ACTION_STATE", send_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
	  	</tr>
	</table>
		<%=message.getMessageSendHTML(send_find, send_state, l_send_page_beg, l_send_page_end) %>
	<%
	}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_EMAIL_RESEND")) {	%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("resend_find", resend_find, "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&resend_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("resend_oper_type", "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&resend_page=1", Bean.messageXML.getfieldTransl("name_ds_message_oper_type", false)) %>
			<%= Bean.getDispatchMessageOperationTypeOptions(resend_oper_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("resend_dispatch_kind", "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&resend_page=1", Bean.messageXML.getfieldTransl("name_ds_sender_dispatch_kind", false)) %>
			<%= Bean.getDispatchKindOptions(resend_dispatch_kind, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("resend_state", "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&resend_page=1", Bean.smsXML.getfieldTransl("name_sms_state", false)) %>
			<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", resend_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("resend_archive", "../crm/dispatch/messages/email_messagesspecs.jsp?id=" + id + "&resend_page=1", Bean.smsXML.getfieldTransl("is_archive", false)) %>
			<%=Bean.getSelectOptionHTML(resend_archive, "", "") %>
			<%=Bean.getSelectOptionHTML(resend_archive, "Y", Bean.messageXML.getfieldTransl("in_archive", false)) %>
			<%=Bean.getSelectOptionHTML(resend_archive, "N", Bean.messageXML.getfieldTransl("not_in_archive", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
		<%=message.getAnswerMessagesHTML(resend_find, resend_dispatch_kind, resend_oper_type, resend_state, resend_archive, l_resend_page_beg, l_resend_page_end) %>
	<%
	}
} %>

</div></div>
</body>
</html>
