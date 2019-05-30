<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcCallCenterSettingObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
</head>
<body>

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CALL_CENTER_SETTINGS";
String tagMessage = "_MESSAGES";
String tagMessageFind = "_MESSAGE_FIND";
String tagMessageType = "_MESSAGE_TYPE";
String tagQuestion = "_QUESTIONS";
String tagQuestionFind = "_QUESTION_FIND";
String tagQuestionContactType = "_QUESTION_CONTACT_TYPE";
String tagQuestionType = "_QUESTION_TYPE";
String tagQuestionStatus = "_QUESTION_STATUS";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (id==null || "".equalsIgnoreCase(id)) { id=""; }
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

	Bean.tabsHmSetValue(pageFormName, tab);
	
	bcCallCenterSettingObject setting = new bcCallCenterSettingObject(id);

    boolean isExternalSystem = false;
    if (!(setting.getValue("ID_CC_SETTING") == null || "".equalsIgnoreCase(setting.getValue("ID_CC_SETTING"))) &&
    		"0".equalsIgnoreCase(setting.getValue("ID_CC_SETTING"))) {
    	isExternalSystem = false;
    } else {
    	isExternalSystem = true;
    }

	//Обрабатываем номера страниц
	String l_question_page = Bean.getDecodeParam(parameters.get("question_page"));
	Bean.pageCheck(pageFormName + tagQuestion, l_question_page);
	String l_question_page_beg = Bean.getFirstRowNumber(pageFormName + tagQuestion);
	String l_question_page_end = Bean.getLastRowNumber(pageFormName + tagQuestion);

	String question_find 	= Bean.getDecodeParam(parameters.get("question_find"));
	question_find 	= Bean.checkFindString(pageFormName + tagQuestionFind, question_find, l_question_page);

	String question_contact_type 	= Bean.getDecodeParam(parameters.get("question_contact_type"));
	question_contact_type 	= Bean.checkFindString(pageFormName + tagQuestionContactType, question_contact_type, l_question_page);

	String question_type 	= Bean.getDecodeParam(parameters.get("question_type"));
	question_type 	= Bean.checkFindString(pageFormName + tagQuestionType, question_type, l_question_page);

	String question_status 	= Bean.getDecodeParam(parameters.get("question_status"));
	question_status 	= Bean.checkFindString(pageFormName + tagQuestionStatus, question_status, l_question_page);

	//Обрабатываем номера страниц
	String l_message_page = Bean.getDecodeParam(parameters.get("message_page"));
	Bean.pageCheck(pageFormName + tagMessage, l_message_page);
	String l_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessage);
	String l_message_page_end = Bean.getLastRowNumber(pageFormName + tagMessage);
	
	String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
	message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_message_page);

	String message_type	= Bean.getDecodeParam(parameters.get("message_type"));
	message_type		= Bean.checkFindString(pageFormName + tagMessageType, message_type, l_message_page);

%>
  <% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
<body topmargin="0">
<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_SETTINGS_INFO")) {%>
			    <%= Bean.getMenuButton("ADD", "../crm/call_center/settingupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<% if (isExternalSystem) { %>
			    <%= Bean.getMenuButton("DELETE", "../crm/call_center/settingupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.call_centerXML.getfieldTransl("h_delete_setting", false), setting.getValue("ID_CC_SETTING") + " - " +  setting.getValue("NAME_CC_SETTING")) %>
				<% } %>
			<% } %>

		<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_SETTINGS_QUESTIONS")) {%>
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagQuestion, "../crm/call_center/settingspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_SETTINGS_QUESTIONS")+"&", "question_page") %>

		<% } %>

		<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_SETTINGS_MESSAGES")) {%>
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagMessage, "../crm/call_center/settingspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_SETTINGS_MESSAGES")+"&", "message_page") %>

		<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(setting.getValue("ID_CC_SETTING") + " - " + setting.getValue("NAME_CC_SETTING")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/call_center/settingspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% 
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CALL_CENTER_SETTINGS_INFO")) {
    %> 
	<table <%=Bean.getTableDetailParam() %>> 
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_setting", false) %> </td>
			<td>
				<input type="text" name="id_cc_setting" size="20" value="<%= setting.getValue("ID_CC_SETTING") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% if (isExternalSystem) { %>
			<td colspan="2"><b><%= Bean.call_centerXML.getfieldTransl("h_external_emails_parameters", false) %></b></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_setting_state", false) %></td><td><input type="text" name="name_cc_setting_state" size="70" value="<%= setting.getValue("NAME_CC_SETTING_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<% if (isExternalSystem) { %>
			<td><%= Bean.call_centerXML.getfieldTransl("send_email", false) %></td><td><input type="text" name="send_email_tsl" size="20" value="<%= setting.getValue("send_email_tsl") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_setting", false) %></td><td><input type="text" name="name_cc_setting" size="70" value="<%= setting.getValue("NAME_CC_SETTING") %>" readonly="readonly" class="inputfield-ro"></td>
			<% if (isExternalSystem) { %>
			<td><%= Bean.call_centerXML.getfieldTransl("receiver_emails", false) %></td><td><input type="text" name="receiver_emails" size="60" value="<%= setting.getValue("RECEIVER_EMAILS") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("desc_cc_setting", false) %></td><td><textarea name="desc_cc_setting" cols="66" rows="3" readonly="readonly" class="inputfield-ro"><%= setting.getValue("DESC_CC_SETTING") %></textarea></td>
			<% if (isExternalSystem) { %>
			<td><%= Bean.call_centerXML.getfieldTransl("title_email", false) %></td><td><input type="text" name="title_email" size="60" value="<%= setting.getValue("TITLE_EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.call_centerXML.getfieldTransl("h_tracing_parameters", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.warningXML.getfieldTransl("type_warning_tsl", false) %></td><td><%= setting.getWarningTypeCheckBoxes(true) %></td>
			<td><%= Bean.eventXML.getfieldTransl("desc_event_type", false) %></td><td><%= setting.getSysEventTypeCheckBoxes(true) %></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				setting.getValue(Bean.getCreationDateFieldName()),
				setting.getValue("CREATED_BY"),
				setting.getValue(Bean.getLastUpdateDateFieldName()),
				setting.getValue("LAST_UPDATE_BY")
			) %>
	</table>
<% 	} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_SETTINGS_INFO")) { %>
	<script>
		var formData = new Array (
			new Array ('cd_cc_setting_state', 'varchar2', 1),
			new Array ('name_cc_setting', 'varchar2', 1),
			new Array ('send_email', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}
	</script>

    <form action="../crm/call_center/settingupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= setting.getValue("ID_CC_SETTING") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_setting", false) %> </td>
			<td>
				<input type="text" name="id_cc_setting" size="20" value="<%= setting.getValue("ID_CC_SETTING") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<% if (isExternalSystem) { %>
			<td colspan="2"><b><%= Bean.call_centerXML.getfieldTransl("h_external_emails_parameters", false) %></b></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_setting_state", true) %></td><td><select name="cd_cc_setting_state" class="inputfield"><%= Bean.getCallCenterSettingStateOptions(setting.getValue("CD_CC_SETTING_STATE"), true) %></select></td>
			<% if (isExternalSystem) { %>
			<td><%= Bean.call_centerXML.getfieldTransl("send_email", true) %></td><td><select name="send_email" class="inputfield"><%= Bean.getYesNoLookupOptions(setting.getValue("SEND_EMAIL"), true) %></select></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_setting", true) %></td><td><input type="text" name="name_cc_setting" size="60" value="<%= setting.getValue("NAME_CC_SETTING") %>" class="inputfield"></td>
			<% if (isExternalSystem) { %>
			<td><%= Bean.call_centerXML.getfieldTransl("receiver_emails", false) %></td><td><input type="text" name="receiver_emails" size="60" value="<%= setting.getValue("RECEIVER_EMAILS") %>" class="inputfield"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("desc_cc_setting", false) %></td><td><textarea name="desc_cc_setting" cols="56" rows="3" class="inputfield"><%= setting.getValue("DESC_CC_SETTING") %></textarea></td>
			<% if (isExternalSystem) { %>
			<td><%= Bean.call_centerXML.getfieldTransl("title_email", false) %></td><td><input type="text" name="title_email" size="60" value="<%= setting.getValue("TITLE_EMAIL") %>" class="inputfield"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.call_centerXML.getfieldTransl("h_tracing_parameters", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.warningXML.getfieldTransl("type_warning_tsl", false) %></td><td><%= setting.getWarningTypeCheckBoxes(false) %></td>
			<td><%= Bean.eventXML.getfieldTransl("desc_event_type", false) %></td><td><%= setting.getSysEventTypeCheckBoxes(false) %></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				setting.getValue(Bean.getCreationDateFieldName()),
				setting.getValue("CREATED_BY"),
				setting.getValue(Bean.getLastUpdateDateFieldName()),
				setting.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/settingupdate.jsp") %>
				<%=Bean.getResetButton() %>
			</td>
		</tr>
	</table>
	</form>
	
<%} 

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_SETTINGS_QUESTIONS")) {%>

	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("question_find", question_find, "../crm/call_center/settingspecs.jsp?id=" + id + "&question_page=1") %>
		
		<%=Bean.getSelectOnChangeBeginHTML("question_contact_type", "../crm/call_center/settingspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cc_contact_type", false)) %>
			<%= Bean.getCallCenterContactTypeOptions(question_contact_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		
		<%=Bean.getSelectOnChangeBeginHTML("question_type", "../crm/call_center/settingspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cc_question_type", false)) %>
			<%= Bean.getCallCenterQuestionTypeOptions(question_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		
		<%=Bean.getSelectOnChangeBeginHTML("question_status", "../crm/call_center/settingspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cc_question_status", false)) %>
			<%= Bean.getCallCenterQuestionStatusOptions(question_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  	</tr>
	</table>


	<%=setting.getCallCenterQuestionsHTML(question_find, question_contact_type, question_type, question_status, l_question_page_beg, l_question_page_end) %>
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_SETTINGS_MESSAGES")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("message_find", message_find, "../crm/call_center/settingspecs.jsp?id=" + id + "&message_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("message_type", "../crm/call_center/settingspecs.jsp?id=" + id + "&message_page=1", Bean.messageXML.getfieldTransl("type_message", false)) %>
			<%= Bean.getMessagePatternTypeWitoutTerminals(message_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%=setting.getDSMessagesHTML(message_find, message_type, "", "", l_message_page_beg, l_message_page_end) %>
<% }

%>
</div></div>
</body>


<%@page import="java.util.HashMap"%></html>
