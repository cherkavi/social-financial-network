<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcTerminalMessageObject"%>
<%@page import="java.util.HashMap"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_TERM";
String tagSend = "_SEND";
String tagSendFind = "_SEND_FIND";
String tagReceiver = "_RECEIVER";
String tagReceiverFind = "_RECEIVER_FIND";
String tagReceiverState = "_RECEIVER_STATE";
String tagReceiverArchive = "_RECEIVER_ARCHIVE";
String tagReceiverSelected = "_RECEIVER_SELECTED";
String tagTask = "_BON_CARD_TASK";
String tagTaskFind = "_BON_CARD_TASK_FIND";
String tagTaskType = "_BON_CARD_TASK_TYPE";
String tagTaskState = "_BON_CARD_TASK_STATE";

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
	bcTerminalMessageObject message = new bcTerminalMessageObject(id);
	
	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_TERM_RECEIVERS", true);
	
	if ("CARD_REQUEST".equalsIgnoreCase(message.getValue("CD_TERM_MESSAGE_TYPE"))) {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_TERM_SEND", false);
		if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_TERM_SEND")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	} else if ("TERMINAL_PARAMETERS".equalsIgnoreCase(message.getValue("CD_TERM_MESSAGE_TYPE"))) {
		Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_TERM_BON_CARD_TASKS", false);
		if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_TERM_BON_CARD_TASKS")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	}
	
	String l_receiver_page = Bean.getDecodeParam(parameters.get("receiver_page"));
	Bean.pageCheck(pageFormName + tagReceiver, l_receiver_page);
	String l_receiver_page_beg = Bean.getFirstRowNumber(pageFormName + tagReceiver);
	String l_receiver_page_end = Bean.getLastRowNumber(pageFormName + tagReceiver);

	String receiver_find 	= Bean.getDecodeParam(parameters.get("receiver_find"));
	receiver_find 	= Bean.checkFindString(pageFormName + tagReceiverFind, receiver_find, l_receiver_page);

	String receiver_state 	= Bean.getDecodeParam(parameters.get("receiver_state"));
	receiver_state 	= Bean.checkFindString(pageFormName + tagReceiverState, receiver_state, l_receiver_page);

	String receiver_archive 	= Bean.getDecodeParam(parameters.get("receiver_archive"));
	receiver_archive 	= Bean.checkFindString(pageFormName + tagReceiverArchive, receiver_archive, l_receiver_page);

	String receiver_selected 	= Bean.getDecodeParam(parameters.get("receiver_selected"));
	receiver_selected 	= Bean.checkFindString(pageFormName + tagReceiverSelected, receiver_selected, l_receiver_page);

	String l_send_page = Bean.getDecodeParam(parameters.get("send_page"));
	Bean.pageCheck(pageFormName + tagSend, l_send_page);
	String l_send_page_beg = Bean.getFirstRowNumber(pageFormName + tagSend);
	String l_send_page_end = Bean.getLastRowNumber(pageFormName + tagSend);

	String send_find 	= Bean.getDecodeParam(parameters.get("send_find"));
	send_find 	= Bean.checkFindString(pageFormName + tagSendFind, send_find, l_send_page);

	String l_task_page = Bean.getDecodeParam(parameters.get("task_page"));
	Bean.pageCheck(pageFormName + tagTask, l_task_page);
	String l_task_page_beg = Bean.getFirstRowNumber(pageFormName + tagTask);
	String l_task_page_end = Bean.getLastRowNumber(pageFormName + tagTask);

	String task_find 	= Bean.getDecodeParam(parameters.get("task_find"));
	task_find 	= Bean.checkFindString(pageFormName + tagTaskFind, task_find, l_task_page);

	String task_type 	= Bean.getDecodeParam(parameters.get("task_type"));
	task_type 	= Bean.checkFindString(pageFormName + tagTaskType, task_type, l_task_page);

	String task_state 	= Bean.getDecodeParam(parameters.get("task_state"));
	task_state 	= Bean.checkFindString(pageFormName + tagTaskState, task_state, l_task_page);
	 
	boolean hasEditPermission = false;
	if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_TERM_INFO")) {
		hasEditPermission = true;
	}
%>

<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_TERM_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/dispatch/messages/term_messagesupdate.jsp?id=" + message.getValue("ID_TERM_MESSAGE") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/dispatch/messages/term_messagesupdate.jsp?id=" + message.getValue("ID_TERM_MESSAGE") + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), message.getValue("ID_TERM_MESSAGE") + " - " + message.getValue("NAME_TERM_MESSAGE_TYPE")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_RECEIVERS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReceiver, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_TERM_RECEIVERS")+"&", "receiver_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_BON_CARD_TASKS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTask, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_TERM_BON_CARD_TASKS")+"&", "task_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_SEND")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSend, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_TERM_SEND")+"&", "send_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(message.getValue("ID_TERM_MESSAGE") + " - " + message.getValue("NAME_TERM_MESSAGE_TYPE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_INFO")) {
	
	if (hasEditPermission) {
%>
		<script>
			var formData = new Array (
				new Array ('text_term_message', 'varchar2', 1),
				new Array ('basis_for_operation', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1),
				new Array ('cd_term_message_state', 'varchar2', 1),
				new Array ('is_archive', 'varchar2', 1)
			);
		</script>
		<%= Bean.getMessageLengthTextAreaInitialScript("text_term_message", "length_term_message") %>
	
	        <form action="../crm/dispatch/messages/term_messagesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id" value="<%= id %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("id_message", false) %></td> <td><input type="text" name="id_term_message" size="20" value="<%= message.getValue("ID_TERM_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
					<td><%= Bean.clubXML.getfieldTransl("club", false) %>
						<%= Bean.getGoToClubLink(message.getValue("ID_CLUB")) %>
					</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(message.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("cd_term_message_type", false) %></td><td><input type="text" name="cd_term_message_type" size="64" value="<%= message.getValue("NAME_TERM_MESSAGE_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.messageXML.getfieldTransl("begin_action_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_action_date", message.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("receivers_count", false) %></td><td><input type="text" name="receivers_count" size="20" value="<%= message.getValue("RECEIVERS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.messageXML.getfieldTransl("end_action_date_frmt", false) %></td> <td><%=Bean.getCalendarInputField("end_action_date", message.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
				</tr>
				<tr>
					<td valign="top"><%= Bean.messageXML.getfieldTransl("cd_term_message_state", true) %></td><td valign="top"><select name="cd_term_message_state" class="inputfield"><%= Bean.getTermMessageStateOptions(message.getValue("CD_TERM_MESSAGE_STATE"), true) %></select></td>
					<td><%= Bean.messageXML.getfieldTransl("repetitions_count", false) %></td><td><input type="text" name="repetitions_count" size="16" value="<%= message.getValue("REPETITIONS_COUNT") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("text_message", true) %></td><td valign="top" rowspan="3"><textarea name="text_term_message" id="text_term_message" cols="60" rows="3" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_term_message") %>><%= message.getValue("TEXT_TERM_MESSAGE") %></textarea></td>
					<td valign="top"><%= Bean.messageXML.getfieldTransl("sendings_quantity", false) %></td><td valign="top"><input type="text" name="sendings_quantity" size="16" value="<%= message.getValue("SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("last_send_date", false) %> </td> <td align="left"><input type="text" name="last_send_date" size="16" value="<%= message.getValue("LAST_SEND_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.smsXML.getfieldTransl("is_archive", true) %> </td> <td align="left"><select name="is_archive" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", message.getValue("IS_ARCHIVE"), true) %></select></td>
				</tr>
				<tr>
					<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td><input type="text" name="length_term_message" id="length_term_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td valign="top"><%= Bean.messageXML.getfieldTransl("basis_for_operation", true) %></td><td valign="top"><textarea name="basis_for_operation" cols="60" rows="3" class="inputfield"><%= message.getValue("BASIS_FOR_OPERATION") %></textarea></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<%=	Bean.getCreationAndMoficationRecordFields(
						message.getValue(Bean.getCreationDateFieldName()),
						message.getValue("CREATED_BY"),
						message.getValue(Bean.getLastUpdateDateFieldName()),
						message.getValue("LAST_UPDATE_BY")
					) %>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/term_messagesupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/term_messages.jsp") %>
					</td>
				</tr>
			</table>
			</form>

	<%= Bean.getCalendarScript("begin_action_date", false) %>
	<%= Bean.getCalendarScript("end_action_date", false) %>

<%	
	} else {
	 %>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_term_message", "length_term_message") %>

	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("id_message", false) %></td> <td><input type="text" name="id_term_message" size="20" value="<%= message.getValue("ID_TERM_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(message.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(message.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("cd_term_message_type", false) %></td><td><input type="text" name="cd_term_message_type" size="64" value="<%= message.getValue("NAME_TERM_MESSAGE_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.messageXML.getfieldTransl("begin_action_date", false) %></td> <td><input type="text" name="begin_action_date" size="16" value="<%= message.getValue("BEGIN_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("receivers_count", false) %></td><td><input type="text" name="receivers_count" size="20" value="<%= message.getValue("RECEIVERS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.messageXML.getfieldTransl("end_action_date", false) %></td> <td><input type="text" name="end_action_date" size="16" value="<%= message.getValue("END_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("cd_term_message_state", false) %></td> <td valign="top"><input type="text" name=cd_term_message_state size="64" value="<%= message.getValue("NAME_TERM_MESSAGE_STATE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.messageXML.getfieldTransl("repetitions_count", false) %></td> <td><input type="text" name="repetitions_count" size="16" value="<%= message.getValue("REPETITIONS_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top" rowspan="3"><textarea name="text_term_message" id="text_term_message" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= message.getValue("TEXT_TERM_MESSAGE") %></textarea></td>
			<td><%= Bean.messageXML.getfieldTransl("sendings_quantity", false) %></td> <td><input type="text" name="sendings_quantity" size="16" value="<%= message.getValue("SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("last_send_date", false) %> </td> <td align="left"><input type="text" name="last_send_date" size="16" value="<%= message.getValue("LAST_SEND_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("is_archive", false) %> </td> <td align="left"><input type="text" name="is_archive_tsl" id="is_archive_tsl" size="16" value="<%= message.getValue("IS_ARCHIVE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_term_message" id="length_term_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("basis_for_operation", false) %></td><td valign="top"><textarea name="basis_for_operation" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= message.getValue("BASIS_FOR_OPERATION") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				message.getValue(Bean.getCreationDateFieldName()),
				message.getValue("CREATED_BY"),
				message.getValue(Bean.getLastUpdateDateFieldName()),
				message.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/dispatch/messages/term_messages.jsp") %>
			</td>
		</tr>
		</table>
		</form>


<%
}
}
	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_RECEIVERS")) { %>
<script type="text/javascript">
 	function CheckCB(Element) {
		myCheck = true;

		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			if (myName.substr(0,6) == 'chb_id'){
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
			
			if (myName.substr(0,6) == Name){
					thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
			}
		}
	}
	CheckCB();
</script>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("receiver_find", receiver_find, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&receiver_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("receiver_state", "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&receiver_page=1", Bean.messageXML.getfieldTransl("cd_term_message_state", false)) %>
			<%= Bean.getTermMessageStateOptions(receiver_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("receiver_archive", "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&receiver_page=1", Bean.smsXML.getfieldTransl("is_archive", false)) %>
			<%=Bean.getSelectOptionHTML(receiver_archive, "", "") %>
			<%=Bean.getSelectOptionHTML(receiver_archive, "Y", Bean.messageXML.getfieldTransl("in_archive", false)) %>
			<%=Bean.getSelectOptionHTML(receiver_archive, "N", Bean.messageXML.getfieldTransl("not_in_archive", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("receiver_selected", "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&receiver_page=1", Bean.commonXML.getfieldTransl("h_chosen", false)) %>
			<%=Bean.getSelectOptionHTML(receiver_selected, "", "") %>
			<%=Bean.getSelectOptionHTML(receiver_selected, "Y", Bean.commonXML.getfieldTransl("h_chosen", false)) %>
			<%=Bean.getSelectOptionHTML(receiver_selected, "N", Bean.commonXML.getfieldTransl("h_not_chosen", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
		<%=message.getTermMessagesReceiverHTML(receiver_selected, receiver_find, receiver_state, receiver_archive, l_receiver_page_beg, l_receiver_page_end) %>
	<%
	}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_BON_CARD_TASKS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("task_find", task_find, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&task_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("task_type", "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&task_page=1", Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)) %>
 			<%= Bean.getClubCardOperationTypeOptions(task_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("task_state", "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&task_page=1", Bean.card_taskXML.getfieldTransl("state_operation", false)) %>
			<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", task_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
		<%=message.getClubCardsTasksHTML(task_find, task_type, task_state, l_task_page_beg, l_task_page_end) %>
	<%
	}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_SEND")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("send_find", send_find, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id + "&send_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
		<%=message.getTermMessagesSendHTML(send_find, l_send_page_beg, l_send_page_end) %>
	<%
	}
} %>

</div></div>
</body>
</html>
