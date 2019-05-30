<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcEmailProfileObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<Head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_SETTINGS_EMAIL_PROFILE";
String tagMessages = "_MESSAGES";
String tagOperationType = "_OPERATION_TYPE";
String tagFindMessage = "_FIND_MESSAGE";
String tagDispatchKind = "_DISPATCH_KIND";
String tagMessageOperationType = "_MESSAGE_OPERATION_TYPE";
String tagStateMessage = "_STATE_MESSAGE";
String tagIsArchive = "_IS_ARCHIVE";
String tagPattern = "_PATTERN";
String tagPatternFind = "_PATTERN_FIND";
String tagPatternType = "_PATTERN_TYPE";
String tagPatternStatus = "_PATTERN_STATUS";


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
	bcEmailProfileObject profile = new bcEmailProfileObject(id);
	
	//Обрабатываем номера страниц
	String l_messages_page = Bean.getDecodeParam(parameters.get("messages_page"));
	Bean.pageCheck(pageFormName + tagMessages, l_messages_page);
	String l_messages_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessages);
	String l_messages_page_end = Bean.getLastRowNumber(pageFormName + tagMessages);
	
	String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
	operation_type 			= Bean.checkFindString(pageFormName + tagOperationType, operation_type, l_messages_page);

	String find_message = Bean.getDecodeParam(parameters.get("find_message"));
	find_message 		= Bean.checkFindString(pageFormName + tagFindMessage, find_message, l_messages_page);
	
	String dispatch_kind		= Bean.getDecodeParam(parameters.get("dispatch_kind"));
	dispatch_kind 			= Bean.checkFindString(pageFormName + tagDispatchKind, dispatch_kind, l_messages_page);

	String message_oper_type	= Bean.getDecodeParam(parameters.get("message_oper_type"));
	message_oper_type			= Bean.checkFindString(pageFormName + tagMessageOperationType, message_oper_type, l_messages_page);

	String message_state	= Bean.getDecodeParam(parameters.get("message_state"));
	message_state			= Bean.checkFindString(pageFormName + tagStateMessage, message_state, l_messages_page);

	String is_archive	= Bean.getDecodeParam(parameters.get("is_archive"));
	is_archive 		= Bean.checkFindString(pageFormName + tagIsArchive, is_archive, l_messages_page);
	
	String l_pattern_page = Bean.getDecodeParam(parameters.get("pattern_page"));
	Bean.pageCheck(pageFormName + tagPattern, l_pattern_page);
	String l_pattern_page_beg = Bean.getFirstRowNumber(pageFormName + tagPattern);
	String l_pattern_page_end = Bean.getLastRowNumber(pageFormName + tagPattern);

	String pattern_find		= Bean.getDecodeParam(parameters.get("pattern_find"));
	pattern_find 			= Bean.checkFindString(pageFormName + tagPatternFind, pattern_find, l_pattern_page);

	String pattern_type	= Bean.getDecodeParam(parameters.get("pattern_type"));
	pattern_type 	= Bean.checkFindString(pageFormName + tagPatternType, pattern_type, l_pattern_page);

	String pattern_status	= Bean.getDecodeParam(parameters.get("pattern_status"));
	pattern_status 	= Bean.checkFindString(pageFormName + tagPatternStatus, pattern_status, l_pattern_page);
	
%>
</head>
<body topmargin="0">
<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_SETTINGS_EMAIL_PROFILE_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/dispatch/settings/email_profileupdate.jsp?id=" + profile.getValue("ID_EMAIL_PROFILE") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/dispatch/settings/email_profileupdate.jsp?id=" + profile.getValue("ID_EMAIL_PROFILE") + "&type=general&action=remove&process=yes", Bean.emailXML.getfieldTransl("h_delete_profile", false), profile.getValue("ID_EMAIL_PROFILE") + " - " + profile.getValue("NAME_EMAIL_PROFILE")) %>
			<% } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_SETTINGS_EMAIL_PROFILE_PATTERNS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPattern, "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_SETTINGS_EMAIL_PROFILE_PATTERNS")+"&", "pattern_page") %>
			<% } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_SETTINGS_EMAIL_PROFILE_MESSAGES")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMessages, "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_SETTINGS_EMAIL_PROFILE_MESSAGES")+"&", "messages_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(profile.getValue("ID_EMAIL_PROFILE") + " - " + profile.getValue("NAME_EMAIL_PROFILE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id) %>
			</td>
		
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_SETTINGS_EMAIL_PROFILE_INFO")) {

 %>

	<script>
		var formData = new Array (
			new Array ('name_email_profile', 'varchar2', 1),
			new Array ('cd_profile_state', 'varchar2', 1),
			new Array ('delay_next_letter', 'integer', 1),
			new Array ('sender_email', 'varchar2', 1),
			new Array ('smtp_server', 'varchar2', 1),
			new Array ('smtp_port', 'integer', 1),
			new Array ('smtp_ssl', 'varchar2', 1),
			new Array ('need_autorization', 'varchar2', 1)
		);
		var formAutorization = new Array (
			new Array ('smtp_user', 'varchar2', 1),
			new Array ('smtp_password', 'varchar2', 1)
		);
		function myValidateForm() {
			var needAutoriz = document.getElementById('need_autorization').value;
			if (needAutoriz == 'Y') {
				return validateForm(formData.concat(formAutorization));
			} else {
				return validateForm(formData);
			}
		}

		function checkAutorization(){
			var needAutoriz = document.getElementById('need_autorization').value;
			if (needAutoriz == 'Y') {
				document.getElementById('span_smtp_user').innerHTML='<%= Bean.emailXML.getfieldTransl("smtp_user", true) %>';
				document.getElementById('span_smtp_password').innerHTML='<%= Bean.emailXML.getfieldTransl("smtp_password", true) %>';
			} else {
				document.getElementById('span_smtp_user').innerHTML='<%= Bean.emailXML.getfieldTransl("smtp_user", false) %>';
				document.getElementById('span_smtp_password').innerHTML='<%= Bean.emailXML.getfieldTransl("smtp_password", false) %>';
			}
		}
		checkAutorization();
	</script>

    <form action="../crm/dispatch/settings/email_profileupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("id_email_profile", false) %></td> <td><input type="text" name="id_email_profile" size="20" value="<%= id %>" readonly="readonly" class="inputfield-ro"> </td>
			<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(profile.getValue("ID_CLUB")) %>
			</td><td class="bottom_line"><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(profile.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("name_email_profile", true) %></td> <td align="left"><input type="text" name="name_email_profile" size="70" value="<%= profile.getValue("NAME_EMAIL_PROFILE") %>" class="inputfield"></td>
			<td><%= Bean.emailXML.getfieldTransl("sender_email", true) %></td> <td align="left"><input type="text" name="sender_email" size="30" value="<%= profile.getValue("SENDER_EMAIL") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td rowspan="3" valign="top"><%= Bean.emailXML.getfieldTransl("desc_email_profile", false) %></td> <td rowspan="3"><textarea name="desc_email_profile" cols="67" rows="4" class="inputfield"><%= profile.getValue("DESC_EMAIL_PROFILE") %></textarea></td>
			<td><%= Bean.emailXML.getfieldTransl("smtp_server", true) %></td> <td align="left"><input type="text" name="smtp_server" size="30" value="<%= profile.getValue("SMTP_SERVER") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("smtp_port", true) %></td> <td align="left"><input type="text" name="smtp_port" size="30" value="<%= profile.getValue("SMTP_PORT") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("smtp_ssl", true) %></td> <td><select name="smtp_ssl" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", profile.getValue("SMTP_SSL"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("cd_profile_state", true) %></td><td><select name="cd_profile_state" class="inputfield"><%= Bean.getDSProfileStateOptions(profile.getValue("CD_PROFILE_STATE"),true) %></select></td>			
			<td><%= Bean.emailXML.getfieldTransl("need_autorization", true) %></td> <td><select name="need_autorization" id="need_autorization" class="inputfield" onchange="checkAutorization()"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", profile.getValue("NEED_AUTORIZATION"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("delay_next_letter", true) %></td> <td align="left"><input type="text" name="delay_next_letter" size="30" value="<%= profile.getValue("DELAY_NEXT_LETTER") %>" class="inputfield"></td>
			<td><span id="span_smtp_user"><%= Bean.emailXML.getfieldTransl("smtp_user", false) %></span></td> <td align="left"><input type="text" name="smtp_user" size="30" value="<%= profile.getValue("SMTP_USER") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("max_error_messages", false) %></td> <td align="left"><input type="text" name="max_error_messages" size="30" value="<%= profile.getValue("MAX_ERROR_MESSAGES") %>" class="inputfield"></td>
			<td><span id="span_smtp_password"><%= Bean.emailXML.getfieldTransl("smtp_password", false) %></span></td> <td align="left"><input type="password" name="smtp_password" size="30" value="<%= profile.getValue("SMTP_PASSWORD") %>" class="inputfield"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				profile.getValue(Bean.getCreationDateFieldName()),
				profile.getValue("CREATED_BY"),
				profile.getValue(Bean.getLastUpdateDateFieldName()),
				profile.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/dispatch/settings/email_profileupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/dispatch/settings/email_profiles.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<%  } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("DISPATCH_SETTINGS_EMAIL_PROFILE_INFO")) {

	 %>
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.emailXML.getfieldTransl("id_email_profile", false) %></td> <td><input type="text" name="id_email_profile" size="20" value="<%= id %>" readonly="readonly" class="inputfield-ro"> </td>
				<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(profile.getValue("ID_CLUB")) %>
				</td><td class="bottom_line"><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(profile.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.emailXML.getfieldTransl("name_email_profile", false) %></td> <td align="left"><input type="text" name="name_email_profile" size="70" value="<%= profile.getValue("NAME_EMAIL_PROFILE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.emailXML.getfieldTransl("sender_email", false) %></td> <td align="left"><input type="text" name="sender_email" size="30" value="<%= profile.getValue("SENDER_EMAIL") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td rowspan="3" valign="top"><%= Bean.emailXML.getfieldTransl("desc_email_profile", false) %></td> <td rowspan="3"><textarea name="desc_email_profile" cols="67" rows="4" readonly="readonly" class="inputfield-ro"><%= profile.getValue("DESC_EMAIL_PROFILE") %></textarea></td>
				<td><%= Bean.emailXML.getfieldTransl("smtp_server", false) %></td> <td align="left"><input type="text" name="smtp_server" size="30" value="<%= profile.getValue("SMTP_SERVER") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.emailXML.getfieldTransl("smtp_port", false) %></td> <td align="left"><input type="text" name="smtp_port" size="30" value="<%= profile.getValue("SMTP_PORT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.emailXML.getfieldTransl("smtp_ssl", false) %></td> <td align="left"><input type="text" name="smtp_ssl" size="30" value="<%= profile.getValue("SMTP_SSL_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("name_profile_state", false) %></td> <td align="left"><input type="text" name="name_profile_state" size="74" value="<%= profile.getValue("NAME_PROFILE_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.emailXML.getfieldTransl("need_autorization", false) %></td> <td align="left"><input type="text" name="need_autorization" size="30" value="<%= profile.getValue("NEED_AUTORIZATION_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.emailXML.getfieldTransl("delay_next_letter", false) %></td> <td align="left"><input type="text" name="delay_next_letter" size="30" value="<%= profile.getValue("DELAY_NEXT_LETTER") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.emailXML.getfieldTransl("smtp_user", false) %></td> <td align="left"><input type="text" name="smtp_user" size="30" value="<%= profile.getValue("SMTP_USER") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.emailXML.getfieldTransl("max_error_messages", false) %></td> <td align="left"><input type="text" name="max_error_messages" size="30" value="<%= profile.getValue("MAX_ERROR_MESSAGES") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.emailXML.getfieldTransl("smtp_password", false) %></td> <td align="left"><input type="password" name="smtp_password" size="30" value="<%= profile.getValue("SMTP_PASSWORD") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					profile.getValue(Bean.getCreationDateFieldName()),
					profile.getValue("CREATED_BY"),
					profile.getValue(Bean.getLastUpdateDateFieldName()),
					profile.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getGoBackButton("../crm/dispatch/settings/email_profiles.jsp") %>
				</td>
			</tr>
		</table>

	<%  }

%>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_SETTINGS_EMAIL_PROFILE_PATTERNS")) { %>

	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("pattern_find", pattern_find, "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&pattern_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("pattern_type", "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&pattern_page=1&", Bean.messageXML.getfieldTransl("name_pattern_type", false)) %>
			<%= Bean.getDSPatternTypeOptions(pattern_type, "50", true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("pattern_status", "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&pattern_page=1&", Bean.messageXML.getfieldTransl("name_pattern_type", false)) %>
			<%= Bean.getDsPatternStatusOptions(pattern_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	<%= profile.getPatternsHTML(pattern_find, pattern_type, pattern_status, l_pattern_page_beg, l_pattern_page_end) %>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_SETTINGS_EMAIL_PROFILE_MESSAGES")) { 
	if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_SETTINGS_EMAIL_PROFILE_MESSAGES")) {
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

	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("find_message", find_message, "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&messages_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("message_oper_type", "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&messages_page=1", Bean.messageXML.getfieldTransl("name_ds_message_oper_type", false)) %>
			<%= Bean.getDispatchMessageOperationTypeOptions(message_oper_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("dispatch_kind", "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&messages_page=1", Bean.messageXML.getfieldTransl("name_ds_sender_dispatch_kind", false)) %>
			<%= Bean.getDispatchKindOptions(dispatch_kind, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("message_state", "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&messages_page=1", Bean.smsXML.getfieldTransl("name_sms_state", false)) %>
			<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SEND_MESSAGE_STATE", message_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("is_archive", "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&messages_page=1", Bean.smsXML.getfieldTransl("is_archive", false)) %>
			<%=Bean.getSelectOptionHTML(is_archive, "", "") %>
			<%=Bean.getSelectOptionHTML(is_archive, "Y", Bean.messageXML.getfieldTransl("in_archive", false)) %>
			<%=Bean.getSelectOptionHTML(is_archive, "N", Bean.messageXML.getfieldTransl("not_in_archive", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>

	<%= profile.getMessagesHTML(operation_type, find_message, dispatch_kind, message_oper_type, message_state, is_archive, l_messages_page_beg, l_messages_page_end) %>
<% } %>


<%   } %>
</div></div>
</body>
</html>
