<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcDsSMSProfileObject"%>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_SETTINGS_SMS_PROFILE";

Bean.setJspPageForTabName(pageFormName);

String tagMessages = "_MESSAGES";
String tagTypeSMS = "_TYPE_SMS";
String tagStateMessage = "_STATE_MESSAGE";
String tagFindMessage = "_FIND_MESSAGE";
String tagMessageDispatchKind = "_MESSAGE_DISPATCH_KIND";
String tagMessageInArchive = "_MESSAGE_IN_ARCHIVE";
String tagPattern = "_PATTERN";
String tagPatternFind = "_PATTERN_FIND";
String tagPatternType = "_PATTERN_TYPE";
String tagPatternStatus = "_PATTERN_STATUA";
String tagOperationType = "_OPERATION_TYPE";

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}

if (id==null || "-1".equals(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcDsSMSProfileObject profile = new bcDsSMSProfileObject(id);
	
	//Обрабатываем номера страниц
	String l_messages_page = Bean.getDecodeParam(parameters.get("messages_page"));
	Bean.pageCheck(pageFormName + tagMessages, l_messages_page);
	String l_messages_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessages);
	String l_messages_page_end = Bean.getLastRowNumber(pageFormName + tagMessages);
	
	String cd_type 		= Bean.getDecodeParam(parameters.get("cd_type"));
	cd_type 			= Bean.checkFindString(pageFormName + tagTypeSMS, cd_type, l_messages_page);
	
	String cd_state		= Bean.getDecodeParam(parameters.get("cd_state"));
	cd_state 			= Bean.checkFindString(pageFormName + tagStateMessage, cd_state, l_messages_page);
	
	String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
	find_string 	= Bean.checkFindString(pageFormName + tagFindMessage, find_string, l_messages_page);
	
	String dispatch_kind		= Bean.getDecodeParam(parameters.get("dispatch_kind"));
	dispatch_kind 			= Bean.checkFindString(pageFormName + tagMessageDispatchKind, dispatch_kind, l_messages_page);
	
	String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
	operation_type 			= Bean.checkFindString(pageFormName + tagOperationType, operation_type, l_messages_page);

	String is_archive 	= Bean.getDecodeParam(parameters.get("is_archive"));
	is_archive 			= Bean.checkFindString(pageFormName + tagMessageInArchive, is_archive, l_messages_page);

	String l_pattern_page = Bean.getDecodeParam(parameters.get("pattern_page"));
	Bean.pageCheck(pageFormName + tagPattern, l_pattern_page);
	String l_pattern_page_beg = Bean.getFirstRowNumber(pageFormName + tagPattern);
	String l_pattern_page_end = Bean.getLastRowNumber(pageFormName + tagPattern);

	String pattern_find	= Bean.getDecodeParam(parameters.get("pattern_find"));
	pattern_find 	= Bean.checkFindString(pageFormName + tagPatternFind, pattern_find, l_pattern_page);

	String pattern_type	= Bean.getDecodeParam(parameters.get("pattern_type"));
	pattern_type 	= Bean.checkFindString(pageFormName + tagPatternType, pattern_type, l_pattern_page);

	String pattern_status	= Bean.getDecodeParam(parameters.get("pattern_status"));
	pattern_status 	= Bean.checkFindString(pageFormName + tagPatternStatus, pattern_status, l_pattern_page);
	
	
%>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_SETTINGS_SMS_PROFILES_INFO")) {	%>
			    <%= Bean.getMenuButton("ADD", "../crm/dispatch/settings/sms_profileupdate.jsp?id=" + profile.getValue("ID_SMS_PROFILE") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/dispatch/settings/sms_profileupdate.jsp?id=" + profile.getValue("ID_SMS_PROFILE") + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), profile.getValue("ID_SMS_PROFILE") + " - " + profile.getValue("NAME_SMS_PROFILE")) %>
			<% } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_SETTINGS_SMS_PROFILES_PATTERNS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPattern, "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_SETTINGS_SMS_PROFILES_PATTERNS")+"&", "pattern_page") %>
			<% } %>
		
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_SETTINGS_SMS_PROFILES_MESSAGES")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMessages, "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_SETTINGS_SMS_PROFILES_MESSAGES")+"&", "messages_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(profile.getValue("ID_SMS_PROFILE") + " - " + profile.getValue("NAME_SMS_PROFILE")) %>
		<tr>

			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_SETTINGS_SMS_PROFILES_INFO")) { %>
		
		<script>
			var formData = new Array (
				new Array ('name_sms_profile', 'varchar2', 1),
				new Array ('device_serial_number', 'varchar2', 1),
				new Array ('max_send_repeat_count', 'varchar2', 1),
				new Array ('delay_message_msec', 'varchar2', 1),
				new Array ('max_delivery_time_sec', 'varchar2', 1),
				new Array ('check_new_messages_time_msec', 'varchar2', 1),
				new Array ('analyse_old_messages_time_msec', 'varchar2', 1)
			);

		</script>

	<form action="../crm/dispatch/settings/sms_profileupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
		<input type="hidden" name="action" value="edit">
	   	<input type="hidden" name="process" value="yes">
	   	<input type="hidden" name="id" value="<%= id %>">

	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("id_sms_profile", false) %></td> <td><input type="text" name="id_sms_profile" size="20" value="<%= id %>" readonly="readonly" class="inputfield-ro"> </td>
			<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(profile.getValue("ID_CLUB")) %>
			</td><td class="bottom_line"><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(profile.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("cd_profile_state", false) %></td><td><select name="cd_profile_state" class="inputfield"><%= Bean.getDSProfileStateOptions(profile.getValue("CD_PROFILE_STATE"),true) %></select></td>			
			<td><%= Bean.smsXML.getfieldTransl("name_sms_device_type", false) %></td> <td align="left"><input type="text" name="name_sms_device_type" size="30" value="<%= profile.getValue("NAME_SMS_DEVICE_TYPE") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("name_sms_profile", true) %></td> <td align="left"><input type="text" name="name_sms_profile" size="74" value="<%= profile.getValue("NAME_SMS_PROFILE") %>" class="inputfield"></td>
			<td><%= Bean.smsXML.getfieldTransl("device_serial_number", true) %></td> <td align="left"><input type="text" name="device_serial_number" size="30" value="<%= profile.getValue("DEVICE_SERIAL_NUMBER") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td rowspan=7 valign="top"><%= Bean.smsXML.getfieldTransl("desc_sms_profile", false) %></td> <td rowspan=7><textarea name="desc_sms_profile" cols="70" rows="7" class="inputfield"><%= profile.getValue("DESC_SMS_PROFILE") %></textarea></td>
			<td><%= Bean.smsXML.getfieldTransl("mobile_operator", false) %></td> <td align="left"><input type="text" name="mobile_operator" size="30" value="<%= profile.getValue("MOBILE_OPERATOR") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("phone_mobile", false) %></td> <td align="left"><input type="text" name="phone_mobile" size="30" value="<%= profile.getValue("PHONE_MOBILE") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.smsXML.getfieldTransl("max_send_repeat_count", true) %></td> <td align="left" class="top_line"><input type="text" name="max_send_repeat_count" size="30" value="<%= profile.getValue("MAX_SEND_REPEAT_COUNT") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("delay_message_msec", true) %></td> <td align="left"><input type="text" name="delay_message_msec" size="30" value="<%= profile.getValue("DELAY_MESSAGE_MSEC") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("max_delivery_time_sec", true) %></td> <td align="left"><input type="text" name="max_delivery_time_sec" size="30" value="<%= profile.getValue("MAX_DELIVERY_TIME_SEC") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("check_new_messages_time_msec", true) %></td> <td align="left"><input type="text" name="check_new_messages_time_msec" size="30" value="<%= profile.getValue("CHECK_NEW_MESSAGES_TIME_MSEC") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("analyse_old_messages_time_msec", true) %></td> <td align="left"><input type="text" name="analyse_old_messages_time_msec" size="30" value="<%= profile.getValue("ANALYSE_OLD_MESSAGES_TIME_MSEC") %>" class="inputfield"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				profile.getValue(Bean.getCreationDateFieldName()),
				profile.getValue("CREATED_BY"),
				profile.getValue(Bean.getLastUpdateDateFieldName()),
				profile.getValue("LAST_UPDATE_BY")
			) %>

		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/dispatch/settings/sms_profileupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/dispatch/settings/sms_profiles.jsp") %>
			</td>
		</tr>
		</table>
	</form>

<%} else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("DISPATCH_SETTINGS_SMS_PROFILES_INFO")) { %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("id_sms_profile", false) %></td> <td><input type="text" name="ID_SMS_PROFILE" size="20" value="<%= id %>" readonly="readonly" class="inputfield-ro"> </td>
			<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(profile.getValue("ID_CLUB")) %>
			</td><td class="bottom_line"><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(profile.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("name_profile_state", false) %></td> <td align="left"><input type="text" name="name_profile_state" size="74" value="<%= profile.getValue("NAME_PROFILE_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.smsXML.getfieldTransl("name_sms_device_type", false) %></td> <td align="left"><input type="text" name="name_sms_device_type" size="30" value="<%= profile.getValue("NAME_SMS_DEVICE_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("name_sms_profile", false) %></td> <td align="left"><input type="text" name="name_sms_profile" size="74" value="<%= profile.getValue("NAME_SMS_PROFILE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.smsXML.getfieldTransl("device_serial_number", false) %></td> <td align="left"><input type="text" name="device_serial_number" size="30" value="<%= profile.getValue("DEVICE_SERIAL_NUMBER") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan=7 valign="top"><%= Bean.smsXML.getfieldTransl("desc_sms_profile", false) %></td> <td rowspan=7><textarea name="desc_sms_profile" cols="70" rows="7" readonly="readonly" class="inputfield-ro"><%= profile.getValue("DESC_SMS_PROFILE") %></textarea></td>
			<td><%= Bean.smsXML.getfieldTransl("mobile_operator", false) %></td> <td align="left"><input type="text" name="mobile_operator" size="30" value="<%= profile.getValue("MOBILE_OPERATOR") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("phone_mobile", false) %></td> <td align="left"><input type="text" name="phone_mobile" size="30" value="<%= profile.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.smsXML.getfieldTransl("max_send_repeat_count", false) %></td> <td align="left" class="top_line"><input type="text" name="max_send_repeat_count" size="30" value="<%= profile.getValue("MAX_SEND_REPEAT_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("delay_message_msec", false) %></td> <td align="left"><input type="text" name="delay_message_msec" size="30" value="<%= profile.getValue("DELAY_MESSAGE_MSEC") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("max_delivery_time_sec", false) %></td> <td align="left"><input type="text" name="max_delivery_time_sec" size="30" value="<%= profile.getValue("MAX_DELIVERY_TIME_SEC") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("check_new_messages_time_msec", false) %></td> <td align="left"><input type="text" name="check_new_messages_time_msec" size="30" value="<%= profile.getValue("CHECK_NEW_MESSAGES_TIME_MSEC") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("analyse_old_messages_time_msec", false) %></td> <td align="left"><input type="text" name="analyse_old_messages_time_msec" size="30" value="<%= profile.getValue("ANALYSE_OLD_MESSAGES_TIME_MSEC") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				profile.getValue(Bean.getCreationDateFieldName()),
				profile.getValue("CREATED_BY"),
				profile.getValue(Bean.getLastUpdateDateFieldName()),
				profile.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/dispatch/settings/sms_profiles.jsp") %>
			</td>
		</tr>
	</table>
	</form>
		<%} %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_SETTINGS_SMS_PROFILES_PATTERNS")) { %>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("pattern_find", pattern_find, "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&pattern_page=1&") %>
	
		<%=Bean.getSelectOnChangeBeginHTML("pattern_type", "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&pattern_page=1&", Bean.messageXML.getfieldTransl("name_pattern_type", false)) %>
			<%= Bean.getDSPatternTypeOptions(pattern_type, "50", true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("pattern_status", "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&pattern_page=1&", Bean.messageXML.getfieldTransl("name_pattern_type", false)) %>
			<%= Bean.getDsPatternStatusOptions(pattern_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= profile.getPatternsHTML(pattern_find, pattern_type, pattern_status, l_pattern_page_beg, l_pattern_page_end) %>
<% } %>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_SETTINGS_SMS_PROFILES_MESSAGES")) { %>
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
           	         		if (confirm('<%= Bean.smsXML.getfieldTransl("h_confirm_delete", false) %>'))  {
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
     	         		} else if (operType == 'change_profile') {
        	         		return true;
        	         	}
               			
           			}
           		}
           	}
       		alert('<%= Bean.smsXML.getfieldTransl("h_not_entered_sms", false) %>');
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

   	 	function checkOperation() {
   	 		operation_type = document.getElementById('operation_type');
	 		operation_process = document.getElementById('process');
   	 		operation_action = document.getElementById('action');
   	 		if (operation_type.value == 'change_profile') {
   	 			operation_process.value = 'no';
   	 			operation_action.value = 'change_profile';
   	 		} else {
   	 			operation_process.value = 'yes';
   	 			operation_action.value = 'execute';
   	 		}
   	 		//alert(operation_action.value);
   	 	}
   	 </script>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&messages_page=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("cd_type", "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&messages_page=1", Bean.smsXML.getfieldTransl("name_sms_message_type", false)) %>
				<%= Bean.getSMSTypeOptions(cd_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("dispatch_kind", "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&messages_page=1", Bean.messageXML.getfieldTransl("name_dispatch_kind", false)) %>
				<%= Bean.getDispatchKindOptions(dispatch_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("cd_state", "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&messages_page=1", Bean.smsXML.getfieldTransl("name_sms_state", false)) %>
				<%= Bean.getSMSStateOptions(cd_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("is_archive", "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id + "&messages_page=1", Bean.smsXML.getfieldTransl("is_archive", false)) %>
				<%=Bean.getSelectOptionHTML(is_archive, "", "") %>
				<%=Bean.getSelectOptionHTML(is_archive, "Y", Bean.messageXML.getfieldTransl("in_archive", false)) %>
				<%=Bean.getSelectOptionHTML(is_archive, "N", Bean.messageXML.getfieldTransl("not_in_archive", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= profile.getMessagesHTML(operation_type, find_string, cd_type, cd_state, dispatch_kind, is_archive, l_messages_page_beg, l_messages_page_end) %>
<% } %>

<% } %>
</div></div>
</body>
</html>
