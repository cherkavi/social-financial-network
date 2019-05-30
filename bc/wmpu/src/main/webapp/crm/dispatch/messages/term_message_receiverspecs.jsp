<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcTerminalMessageReceiverObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcTerminalMessageObject"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_TERM";
String tagSend = "_RECEIVER_DET_SEND";
String tagSendFind = "_RECEIVER_DET_SEND_FIND";
String tagTask = "_RECEIVER_DET_BON_CARD_TASK";
String tagTaskFind = "_RECEIVER_DET_BON_CARD_TASK_FIND";
String tagTaskType = "_RECEIVER_DET_BON_CARD_TASK_TYPE";
String tagTaskState = "_RECEIVER_DET_BON_CARD_TASK_STATE";

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
	bcTerminalMessageReceiverObject receiver = new bcTerminalMessageReceiverObject(id);

	bcTerminalMessageObject message = new bcTerminalMessageObject(receiver.getValue("ID_TERM_MESSAGE"));
	
	Bean.currentMenu.setExistFlag("DISPATCH_MESSAGES_TERM_RECEIVERS", false);
	if (Bean.currentMenu.isCurrentTab("DISPATCH_MESSAGES_TERM_RECEIVERS")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}
	
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

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_BON_CARD_TASKS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTask, "../crm/dispatch/messages/term_message_receiverspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_TERM_BON_CARD_TASKS")+"&", "task_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_SEND")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSend, "../crm/dispatch/messages/term_message_receiverspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_TERM_SEND")+"&", "send_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(receiver.getValue("ID_TERM_MESSAGE_RECEIVER") + " - " + receiver.getValue("ID_TERM")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/dispatch/messages/term_message_receiverspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_INFO")) {
	
	
%>
		<% if (hasEditPermission) { %>
		<script>
			var formData = new Array (
				new Array ('is_archive', 'varchar2', 1)
			);
		</script>
	    <form action="../crm/dispatch/messages/term_message_receiverupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id" value="<%= id %>">
		<% } %>
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("id_ds_message_receiver", false) %></td> <td><input type="text" name="id_term_message_receiver" size="20" value="<%= receiver.getValue("ID_TERM_MESSAGE_RECEIVER") %>" readonly="readonly" class="inputfield-ro"> </td>
					<td><%= Bean.messageXML.getfieldTransl("name_term_message_state", false) %></td> <td><input type="text" name="name_term_message_state" size="30" value="<%= receiver.getValue("NAME_TERM_MESSAGE_STATE") %>" readonly="readonly" class="inputfield-ro"> </td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("id_message", false) %>
						<%= Bean.getGoToDispatchTermMessageLink(receiver.getValue("ID_TERM_MESSAGE")) %>
					</td> <td><input type="text" name="id_term_message" size="20" value="<%= receiver.getValue("ID_TERM_MESSAGE") %>" readonly="readonly" class="inputfield-ro"> </td>
					<td><%= Bean.messageXML.getfieldTransl("sendings_quantity", false) %></td> <td><input type="text" name="sendings_quantity" size="20" value="<%= receiver.getValue("SENDINGS_QUANTITY") %>" readonly="readonly" class="inputfield-ro"> </td>
				</tr>
				<tr>
					<td><%= Bean.messageXML.getfieldTransl("id_term", false) %>
						<%= Bean.getGoToTerminalLink(receiver.getValue("ID_TERM")) %>
					</td> <td><input type="text" name="id_term" size="20" value="<%= receiver.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro"> </td>
					<td><%= Bean.messageXML.getfieldTransl("last_send_date", false) %></td> <td><input type="text" name="last_send_date" size="20" value="<%= receiver.getValue("LAST_SEND_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
				</tr>
				<tr>
					<% if (hasEditPermission) { %>
					<td><%= Bean.messageXML.getfieldTransl("repetitions_count", false) %></td> <td><input type="text" name="repetitions_count" size="20" value="<%= receiver.getValue("REPETITIONS_COUNT") %>" class="inputfield"> </td>
					<td><%= Bean.smsXML.getfieldTransl("is_archive", true) %> </td> <td align="left"><select name="is_archive" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", receiver.getValue("IS_ARCHIVE"), true) %></select></td>
					<% } else { %>
					<td><%= Bean.messageXML.getfieldTransl("repetitions_count", false) %></td> <td><input type="text" name="repetitions_count" size="20" value="<%= receiver.getValue("REPETITIONS_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
					<td><%= Bean.messageXML.getfieldTransl("is_archive", false) %></td> <td><input type="text" name="is_archive_tsl" size="20" value="<%= receiver.getValue("IS_ARCHIVE_TSL") %>" readonly="readonly" class="inputfield-ro"> </td>
					<% } %>
				</tr>
				<%=	Bean.getCreationAndMoficationRecordFields(
						receiver.getValue(Bean.getCreationDateFieldName()),
						receiver.getValue("CREATED_BY"),
						receiver.getValue(Bean.getLastUpdateDateFieldName()),
						receiver.getValue("LAST_UPDATE_BY")
					) %>
				<tr>
					<td colspan="6" align="center">
						<% if (hasEditPermission) { %>
						<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/term_message_receiverupdate.jsp") %>
						<% } %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/term_messagesspecs.jsp?id=" + receiver.getValue("ID_TERM_MESSAGE")) %>
					</td>
				</tr>
			</table>
			</form>

<%
}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_BON_CARD_TASKS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("task_find", task_find, "../crm/dispatch/messages/term_message_receiverspecs.jsp?id=" + id + "&task_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("task_type", "../crm/dispatch/messages/term_message_receiverspecs.jsp?id=" + id + "&task_page=1", Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)) %>
 			<%= Bean.getClubCardOperationTypeOptions(task_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("task_state", "../crm/dispatch/messages/term_message_receiverspecs.jsp?id=" + id + "&task_page=1", Bean.card_taskXML.getfieldTransl("state_operation", false)) %>
			<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", task_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
		<%=receiver.getClubCardsTasksHTML(task_find, task_type, task_state, l_task_page_beg, l_task_page_end) %>
	<%
	}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_TERM_SEND")) { %>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("send_find", send_find, "../crm/dispatch/messages/term_message_receiverspecs.jsp?id=" + id + "&send_page=1") %>

		<td>&nbsp;</td>
	  	</tr>
	</table>
		<%=receiver.getTermMessagesSendHTML(send_find, l_send_page_beg, l_send_page_end) %>
	<%
	}
} %>

</div></div>
</body>
</html>
