<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcRoleObject"%>
<%@page import="bc.objects.bcEmailMessageReceiverObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLEncoder"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_EMAIL";
String tagSend = "_RECEIVER_SEND";
String tagSendFind = "_RECEIVER_SEND_FIND";
String tagSendState = "_RECEIVER_SEND_STATE";

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
	bcEmailMessageReceiverObject receiver = new bcEmailMessageReceiverObject(id);
	
	String receiverTabID = Bean.currentMenu.getTabID("DISPATCH_MESSAGES_EMAIL_RECEIVERS");

	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_RECEIVERS", false);
	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_EMAIL_RESEND", false);
	
	if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_EMAIL_RECEIVERS") ||
			Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_EMAIL_RESEND")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}
	
	String l_send_page = Bean.getDecodeParam(parameters.get("send_page"));
	Bean.pageCheck(pageFormName + tagSend, l_send_page);
	String l_send_page_beg = Bean.getFirstRowNumber(pageFormName + tagSend);
	String l_send_page_end = Bean.getLastRowNumber(pageFormName + tagSend);

	String send_find 	= Bean.getDecodeParam(parameters.get("send_find"));
	send_find 	= Bean.checkFindString(pageFormName + tagSendFind, send_find, l_send_page);

	String send_state 	= Bean.getDecodeParam(parameters.get("send_state"));
	send_state 	= Bean.checkFindString(pageFormName + tagSendState, send_state, l_send_page);

%>

<body topmargin="0">
<div id="div_tabsheet">
	<% String messageCaption = receiver.getValue("ID_DS_MESSAGE_RECEIVER") + " - " + receiver.getValue("RECEIVER_EMAIL"); 
	%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_EMAIL_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/dispatch/messages/email_messagesupdate.jsp?id=" + receiver.getValue("ID_DS_MESSAGE") + "&type=receiver&action=add&process=no", "", "") %>
		    	<%= Bean.getMenuButton("DELETE", "../crm/dispatch/messages/email_messagesupdate.jsp?id=" + receiver.getValue("ID_DS_MESSAGE") + "&id_ds_message_receiver=" + receiver.getValue("ID_DS_MESSAGE_RECEIVER") + "&type=receiver&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), messageCaption) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_EMAIL_SEND")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSend, "../crm/dispatch/messages/email_messagereceiverspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_EMAIL_SEND")+"&", "send_page") %>
			<% } %>

	</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(messageCaption) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/dispatch/messages/email_messagereceiverspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_EMAIL_INFO")) { 
	
%>
	<form action="../crm/dispatch/messages/email_messagesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="receiver">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= id %>">
		<input type="hidden" name="entry_type" value="RECEIVER">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_ds_message_receiver", false) %></td> <td><input type="text" name="id_ds_message_receiver" size="20" value="<%= receiver.getValue("ID_DS_MESSAGE_RECEIVER") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("cd_ds_message_send_state", true) %></td><td valign="top"><select name="cd_ds_message_state" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", receiver.getValue("CD_DS_MESSAGE_STATE"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_ds_message", false) %>
				<%= Bean.getGoToDispatchEmailLink(receiver.getValue("ID_DS_MESSAGE")) %>
			</td> <td><input type="text" name="id_ds_message" size="20" value="<%= receiver.getValue("ID_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("is_archive", true) %> </td> <td align="left"><select name="is_archive" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", receiver.getValue("IS_ARCHIVE"), true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("cd_dispatch_kind", false) %></td> <td><input type="text" name="cd_ds_receiver_dispatch_kind" size="20" value="<%= receiver.getValue("NAME_DS_RECEIVER_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("sendings_quantity", false) %></td> <td><input type="text" name="sendings_quantity" size="10" value="<%= receiver.getValue("SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<% if ("CLIENT".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(receiver.getValue("ID_NAT_PRS_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_nat_prs" size="35" value="<%= Bean.getNatPrsName(receiver.getValue("ID_NAT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else if ("PARTNER".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(receiver.getValue("ID_CONTACT_PRS_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_contact_prs" size="35" value="<%= Bean.getContactPrsName(receiver.getValue("ID_CONTACT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else if ("SYSTEM".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(receiver.getValue("ID_USER_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_user" size="35" value="<%= Bean.getUserName(receiver.getValue("ID_USER_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else { %>
				<td><%= Bean.messageXML.getfieldTransl("email", false) %></td> <td><input type="text" name="receiver_email" size="35" value="<%= receiver.getValue("RECEIVER_EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } %>
			<td><%= Bean.messageXML.getfieldTransl("error_sendings_quantity", false) %></td> <td><input type="text" name="error_sendings_quantity" size="10" value="<%= receiver.getValue("ERROR_SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<% if (!("CLIENT".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND")) || 
					"PARTNER".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND")) ||
					"TRAINING".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND")) ||
					"SYSTEM".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND")))) { %>
			<td colspan="2">&nbsp;</td>
			<% } else { %>
			<td><%= Bean.messageXML.getfieldTransl("email", false) %></td> <td><input type="text" name="receiver_email" size="35" value="<%= receiver.getValue("RECEIVER_EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
			<td><%= Bean.messageXML.getfieldTransl("last_send_date", false) %></td> <td><input type="text" name="last_send_date" size="20" value="<%= receiver.getValue("LAST_SEND_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				receiver.getValue(Bean.getCreationDateFieldName()),
				receiver.getValue("CREATED_BY"),
				receiver.getValue(Bean.getLastUpdateDateFieldName()),
				receiver.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/email_messagesupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messagesspecs.jsp?id="+receiver.getValue("ID_DS_MESSAGE") + "&tab=" + receiverTabID) %>
			</td>
		</tr>

	</table>
	</form> 

<% 

} if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("DISPATCH_MESSAGES_EMAIL_INFO")) {%>
 
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_ds_message_receiver", false) %></td> <td><input type="text" name="id_ds_message_receiver" size="20" value="<%= receiver.getValue("ID_DS_MESSAGE_RECEIVER") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("cd_ds_message_send_state", false) %></td> <td><input type="text" name="name_ds_message_send_state" size="10" value="<%= receiver.getValue("name_ds_message_send_state") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_ds_message", false) %>
				<%= Bean.getGoToDispatchEmailLink(receiver.getValue("ID_DS_MESSAGE")) %>
			</td> <td><input type="text" name="id_ds_message" size="20" value="<%= receiver.getValue("ID_DS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("is_archive", false) %></td> <td><input type="text" name="is_archive" size="10" value="<%= receiver.getValue("IS_ARCHIVE_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("cd_dispatch_kind", false) %></td> <td><input type="text" name="cd_ds_receiver_dispatch_kind" size="20" value="<%= receiver.getValue("NAME_DS_RECEIVER_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("sendings_quantity", false) %></td> <td><input type="text" name="sendings_quantity" size="10" value="<%= receiver.getValue("SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<% if ("CLIENT".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %>
					<%= Bean.getGoToNatPrsLink(receiver.getValue("ID_NAT_PRS_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_nat_prs" size="35" value="<%= Bean.getNatPrsName(receiver.getValue("ID_NAT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else if ("PARTNER".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %>
					<%= Bean.getGoToContactPersonLink(receiver.getValue("ID_CONTACT_PRS_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_contact_prs" size="35" value="<%= Bean.getContactPrsName(receiver.getValue("ID_CONTACT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else if ("SYSTEM".equalsIgnoreCase(receiver.getValue("CD_DS_RECEIVER_DISPATCH_KIND"))) { %>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %>
					<%= Bean.getGoToSystemUserLink(receiver.getValue("ID_USER_RECEIVER")) %>
				</td>
	            <td>
					<input type="text" name="name_user" size="35" value="<%= Bean.getUserName(receiver.getValue("ID_USER_RECEIVER")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } else { %>
				<td colspan="2">&nbsp;</td>
				<% } %>
			<td><%= Bean.messageXML.getfieldTransl("error_sendings_quantity", false) %></td> <td><input type="text" name="error_sendings_quantity" size="10" value="<%= receiver.getValue("ERROR_SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("email", false) %></td> <td><input type="text" name="receiver_email" size="35" value="<%= receiver.getValue("RECEIVER_EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.messageXML.getfieldTransl("last_send_date", false) %></td> <td><input type="text" name="last_send_date" size="20" value="<%= receiver.getValue("LAST_SEND_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				receiver.getValue(Bean.getCreationDateFieldName()),
				receiver.getValue("CREATED_BY"),
				receiver.getValue(Bean.getLastUpdateDateFieldName()),
				receiver.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/dispatch/messages/email_messagesspecs.jsp?id="+receiver.getValue("ID_DS_MESSAGE")) %>
			</td>
		</tr>
	</table>
	</form>

<%		}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_EMAIL_SEND")) {	%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
			<%= Bean.getFindHTML("send_find", send_find, "../crm/dispatch/messages/email_messagereceiverspecs.jsp?id=" + id + "&send_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("send_state", "../crm/dispatch/messages/email_messagereceiverspecs.jsp?id=" + id + "&send_page=1", Bean.messageXML.getfieldTransl("name_ds_message_send_state", false)) %>
				<%= Bean.getMeaningFromLookupNameOptions("SEND_MESSAGE_ACTION_STATE", send_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
	  	</tr>
	</table>
		<%=receiver.getMessageSendHTML(send_find, send_state, l_send_page_beg, l_send_page_end) %>
	<%
	}

} %>

</div></div>
</body>
</html>
