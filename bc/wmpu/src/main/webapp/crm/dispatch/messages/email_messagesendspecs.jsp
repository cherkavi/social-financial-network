<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcRoleObject"%>
<%@page import="bc.objects.bcEmailMessageSendObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLEncoder"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_EMAIL";

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
	bcEmailMessageSendObject message_send = new bcEmailMessageSendObject(id);
	
	String entry_type = Bean.getDecodeParam(parameters.get("entry_type"));
	if (entry_type==null || "".equalsIgnoreCase(entry_type)) {
		entry_type = "MESSAGE";
	}
	String sendTabID = Bean.currentMenu.getTabID("DISPATCH_MESSAGES_EMAIL_SEND");

	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_RECEIVERS", false);
	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_SEND", false);
	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_RESEND", false);
	
	if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_EMAIL_RECEIVERS") ||
			Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_EMAIL_SEND") ||
			Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_EMAIL_RESEND")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}
	
	
%>

<body topmargin="0">
<div id="div_tabsheet">
	<% String messageCaption = message_send.getValue("ID_DS_MESSAGE_SEND") + " - " + message_send.getValue("RECEIVER_EMAIL"); 
	%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<td>&nbsp;</td>

	</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(messageCaption) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/dispatch/messages/email_messagesendspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_EMAIL_INFO")) { 
	
	boolean hasEditPermission = Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_EMAIL_INFO");
	
%>
		<% if (hasEditPermission) { %>
	   		<script>
				var formData = new Array (
					new Array ('cd_ds_message_send_state', 'varchar2', 1)
				);
			</script>
		<% } %>
		<%= Bean.getMessageLengthTextAreaInitialScript("text_ds_message", "length_message") %>

	<% if (hasEditPermission) { %>
	<form action="../crm/dispatch/messages/email_messagesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="send">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id_ds_message_sene" value="<%= id %>">
		<input type="hidden" name="id" value="<%= message_send.getValue("ID_DS_MESSAGE") %>">
		<input type="hidden" name="entry_type" id="entry_type" value="<%=entry_type %>">
	<% } %>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_ds_message_send", false) %></td> <td><input type="text" name="id_ds_message_send" size="20" value="<%= message_send.getValue("ID_DS_MESSAGE_SEND") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("name_ds_receiver_dispatch_kind", false) %></td> <td><input type="text" name="name_ds_receiver_dispatch_kind" size="20" value="<%= message_send.getValue("NAME_DS_RECEIVER_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_ds_message", false) %>
				<%= Bean.getGoToDispatchEmailLink(message_send.getValue("ID_DS_MESSAGE")) %>
			</td> <td><input type="text" name="id_ds_message" size="20" value="<%= message_send.getValue("ID_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<% if ("CLIENT".equalsIgnoreCase(message_send.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(message_send.getValue("ID_NAT_PRS_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_nat_prs" size="35" value="<%= Bean.getNatPrsName(message_send.getValue("ID_NAT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else if ("PARTNER".equalsIgnoreCase(message_send.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(message_send.getValue("ID_CONTACT_PRS_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_contact_prs" size="35" value="<%= Bean.getContactPrsName(message_send.getValue("ID_CONTACT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else if ("SYSTEM".equalsIgnoreCase(message_send.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(message_send.getValue("ID_USER_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_user" size="35" value="<%= Bean.getUserName(message_send.getValue("ID_USER_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_ds_message_receiver", false) %>
				<%= Bean.getGoToDispatchEmailReceiverLink(message_send.getValue("ID_DS_MESSAGE_RECEIVER")) %>
			</td> <td><input type="text" name="id_ds_message_receiver" size="20" value="<%= message_send.getValue("ID_DS_MESSAGE_RECEIVER") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("email", false) %></td> <td><input type="text" name="receiver_email" size="35" value="<%= message_send.getValue("RECEIVER_EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.messageXML.getfieldTransl("email_profile", false) %>
				<%= Bean.getGoToDispatchEmailProfileLink(message_send.getValue("ID_EMAIL_PROFILE")) %>
			</td> 
			<td class="top_line">
				<input type="text" name="name_email_profile" size="65" value="<%= Bean.getDSEmailProfileName(message_send.getValue("ID_EMAIL_PROFILE")) %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% if (hasEditPermission) { %>			
			<td><%= Bean.messageXML.getfieldTransl("name_ds_message_send_state", true) %></td><td><select name="cd_ds_message_send_state" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_ACTION_STATE", message_send.getValue("NAME_DS_MESSAGE_SEND_STATE"), true) %></select></td>
			<% } else { %>
			<td><%= Bean.messageXML.getfieldTransl("name_ds_message_send_state", false) %></td> <td><input type="text" name="name_ds_message_send_state" size="35" value="<%= message_send.getValue("NAME_DS_MESSAGE_SEND_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td> <td><input type="text" name="title_ds_message" size="65" value="<%= message_send.getValue("TITLE_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("send_date", false) %></td> <td><input type="text" name="send_date_frmt" size="20" value="<%= message_send.getValue("SEND_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top"  colspan="3"><textarea name="text_ds_message" id="text_ds_message" cols="120" rows="12" readonly="readonly" class="inputfield-ro" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>><%= message_send.getValue("TEXT_DS_MESSAGE") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<% if (!(message_send.getValue("STORED_MESSAGE_FILE_NAME")==null || "".equalsIgnoreCase(message_send.getValue("STORED_MESSAGE_FILE_NAME")))) { %>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("message_file_name", false) %></td>
			<td valign="middle">
					<%=message_send.getValue("MESSAGE_FILE_NAME") %><a href="../FileSender?FILENAME=<%=URLEncoder.encode(message_send.getValue("STORED_MESSAGE_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
						<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
					</a>
					<br>
			</td>
		</tr>
		<% } %>
		<tr>
			<td colspan="6" align="center">
				<% if (hasEditPermission) { %>
					<%=Bean.getSubmitButtonMultiPart("../crm/dispatch/messages/email_messagesupdate.jsp","submit","updateForm") %>
				<% } %>
				<%=Bean.getResetButton() %>
				<% if ("MESSAGE".equalsIgnoreCase(entry_type)) { %> 
					<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messagesspecs.jsp?id=" + message_send.getValue("ID_DS_MESSAGE") + "&tab=" + sendTabID) %>
				<% } else if ("RECEIVER".equalsIgnoreCase(entry_type)) { %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messagereceiverspecs.jsp?id=" + message_send.getValue("ID_DS_MESSAGE_RECEIVER") + "&tab=" + sendTabID) %>
				<% } %>
			</td>
		</tr>
		<tr>
			<td valign="top" class="top_line"><%= Bean.messageXML.getfieldTransl("h_attached_files", false) %></td>
			<td colspan="3" class="top_line">
				<%= message_send.getAttachedFilesHTML() %>&nbsp;
			</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				message_send.getValue(Bean.getCreationDateFieldName()),
				message_send.getValue("CREATED_BY"),
				message_send.getValue(Bean.getLastUpdateDateFieldName()),
				message_send.getValue("LAST_UPDATE_BY")
			) %>

	</table>
	</form> 
	
<% 

} if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("DISPATCH_MESSAGES_EMAIL_INFO")) {%>
 
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_ds_message_receiver", false) %></td> <td><input type="text" name="id_ds_message_receiver" size="20" value="<%= message_send.getValue("ID_DS_MESSAGE_RECEIVER") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("cd_ds_message_send_state", false) %></td> <td><input type="text" name="name_ds_message_send_state" size="10" value="<%= message_send.getValue("name_ds_message_send_state") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_ds_message", false) %>
				<%= Bean.getGoToDispatchEmailLink(message_send.getValue("ID_DS_MESSAGE")) %>
			</td> <td><input type="text" name="id_ds_message" size="20" value="<%= message_send.getValue("ID_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("is_archive", false) %></td> <td><input type="text" name="is_archive" size="10" value="<%= message_send.getValue("IS_ARCHIVE_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("cd_dispatch_kind", false) %></td> <td><input type="text" name="cd_ds_receiver_dispatch_kind" size="20" value="<%= message_send.getValue("NAME_DS_RECEIVER_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("sendings_quantity", false) %></td> <td><input type="text" name="sendings_quantity" size="10" value="<%= message_send.getValue("SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<% if ("CLIENT".equalsIgnoreCase(message_send.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(message_send.getValue("ID_NAT_PRS_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_nat_prs" size="35" value="<%= Bean.getNatPrsName(message_send.getValue("ID_NAT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else if ("PARTNER".equalsIgnoreCase(message_send.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(message_send.getValue("ID_CONTACT_PRS_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_contact_prs" size="35" value="<%= Bean.getContactPrsName(message_send.getValue("ID_CONTACT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else if ("SYSTEM".equalsIgnoreCase(message_send.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(message_send.getValue("ID_USER_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_user" size="35" value="<%= Bean.getUserName(message_send.getValue("ID_USER_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			<td><%= Bean.messageXML.getfieldTransl("error_sendings_quantity", false) %></td> <td><input type="text" name="error_sendings_quantity" size="10" value="<%= message_send.getValue("ERROR_SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("email", false) %></td> <td><input type="text" name="receiver_email" size="35" value="<%= message_send.getValue("RECEIVER_EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.messageXML.getfieldTransl("last_send_date", false) %></td> <td><input type="text" name="last_send_date" size="20" value="<%= message_send.getValue("LAST_SEND_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				message_send.getValue(Bean.getCreationDateFieldName()),
				message_send.getValue("CREATED_BY"),
				message_send.getValue(Bean.getLastUpdateDateFieldName()),
				message_send.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messagesspecs.jsp?id="+message_send.getValue("ID_DS_MESSAGE")) %>
			</td>
		</tr>
	</table>
	</form>
<%		}

} %>

</div></div>
</body>
</html>
