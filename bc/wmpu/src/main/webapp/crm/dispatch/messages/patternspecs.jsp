<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcRoleObject"%>
<%@page import="bc.objects.bcDsMessagePatternObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="bc.reports.bcReports"%>
<%@page import="bc.objects.bcSysLogObject"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_PATTERN";

Bean.setJspPageForTabName(pageFormName);

String tagMessages = "_MESSAGES";
String tagMessageType = "_MESSAGE_TYPE";
String tagIsArchive = "_IS_ARCHIVE";
String tagOperationType = "_OPERATION_TYPE";
String tagMessageFind = "_MESSAGE_FIND";
String tagMessageDispatchKind = "_MESSAGE_DISPATCH_KIND";
String tagMessageInArchive = "_MESSAGE_IN_ARCHIVE";
String tagStateMessage = "_STATE_MESSAGE";
String tagMessageOperationType = "_MESSAGE_OPERATION_TYPE";

String tagFindReport = "_FIND_REPORT";
String tagFindLog = "_FIND_LOG";
String tagPageReport = "_PAGE_REPORT";
String tagPageReportDet = "_page_log_det";
String tagPageReportLog = "_PAGE_REPORT_LOG";
String tagLogRowType = "_LOG_ROW_TYPE";


String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcDsMessagePatternObject pattern = new bcDsMessagePatternObject(id);
	
	boolean hasEditInfoPermission = Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_PATTERN_INFO");

	String l_messages_page = Bean.getDecodeParam(parameters.get("messages_page"));
	Bean.pageCheck(pageFormName + tagMessages, l_messages_page);
	String l_messages_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessages);
	String l_messages_page_end = Bean.getLastRowNumber(pageFormName + tagMessages);

	String cd_state		= Bean.getDecodeParam(parameters.get("cd_state"));
	cd_state 			= Bean.checkFindString(pageFormName + tagStateMessage, cd_state, l_messages_page);

	String message_type	= Bean.getDecodeParam(parameters.get("message_type"));
	message_type		= Bean.checkFindString(pageFormName + tagMessageType, message_type, l_messages_page);

	String dispatch_kind		= Bean.getDecodeParam(parameters.get("dispatch_kind"));
	dispatch_kind 			= Bean.checkFindString(pageFormName + tagMessageDispatchKind, dispatch_kind, l_messages_page);

	String is_archive	= Bean.getDecodeParam(parameters.get("is_archive"));
	is_archive 		= Bean.checkFindString(pageFormName + tagIsArchive, is_archive, l_messages_page);

	String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
	operation_type 			= Bean.checkFindString(pageFormName + tagOperationType, operation_type, l_messages_page);

	String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
	message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_messages_page);

	String message_oper_type	= Bean.getDecodeParam(parameters.get("message_oper_type"));
	message_oper_type			= Bean.checkFindString(pageFormName + tagMessageOperationType, message_oper_type, l_messages_page);

	String find_report	 			= Bean.getDecodeParam(parameters.get("find_report"));
	String find_log		 			= Bean.getDecodeParam(parameters.get("find_log"));
	String l_page_report			= Bean.getDecodeParam(parameters.get("page_report"));
	String id_report				= Bean.getDecodeParam(parameters.get("id_report"));
	String l_page_log_det		= Bean.getDecodeParam(parameters.get("page_log_det"));
	String l_log_row_type			= Bean.getDecodeParam(parameters.get("log_row_type"));

	find_report 		= Bean.checkFindString(pageFormName + tagFindReport, find_report, "");
	find_log	 		= Bean.checkFindString(pageFormName + tagFindLog, find_log, "");
	l_log_row_type 	= Bean.checkFindString(pageFormName + tagLogRowType, l_log_row_type, "");
	
	Bean.pageCheck(pageFormName + tagPageReport, l_page_report);
	String l_page_report_beg = Bean.getFirstRowNumber(pageFormName + tagPageReport);
	String l_page_report_end = Bean.getLastRowNumber(pageFormName + tagPageReport);
	
	Bean.pageCheck(pageFormName + tagPageReportDet, l_page_log_det);
	String l_page_log_det_beg = Bean.getFirstRowNumber(pageFormName + tagPageReportDet);
	String l_page_log_det_end = Bean.getLastRowNumber(pageFormName + tagPageReportDet);
%>

<body topmargin="0">
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_PATTERN_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/dispatch/messages/patternupdate.jsp?id=" + pattern.getValue("ID_DS_PATTERN") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/dispatch/messages/patternupdate.jsp?id=" + pattern.getValue("ID_DS_PATTERN") + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), pattern.getValue("ID_DS_PATTERN") + " - " + pattern.getValue("NAME_DS_PATTERN")) %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_PATTERN_MESSAGES")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_PATTERN_MESSAGES") && 
						"ACTIVE".equalsIgnoreCase(pattern.getValue("CD_PATTERN_STATUS"))) { %>
				    <%= Bean.getMenuButton("RUN_PARAM", "../crm/dispatch/messages/patternupdate.jsp?id=" + pattern.getValue("ID_DS_PATTERN") + "&type=general&action=apply&process=no", Bean.buttonXML.getfieldTransl("button_apply", false), "") %>
				<% } %>
		
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMessages, "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_PATTERN_MESSAGES")+"&", "messages_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_PATTERN_LOG")) {%>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPageReport, "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("DISPATCH_MESSAGES_PATTERN_LOG")+"&", "page_report") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(pattern.getValue("ID_DS_PATTERN") + " - " + pattern.getValue("NAME_DS_PATTERN")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/dispatch/messages/patternspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_PATTERN_INFO")) {
	
	%>
		<% if (hasEditInfoPermission) { %>
		<script>
			var formAll = new Array (
			);
			var formGeneral = new Array (
				new Array ('name_ds_pattern', 'varchar2', 1),
				new Array ('cd_pattern_status', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1),
				new Array ('basis_for_operation', 'varchar2', 1)
			);
			var formCongratulation = new Array (
				new Array ('side_condition', 'varchar2', 1),
				new Array ('day_number', 'varchar2', 1),
				new Array ('month_name', 'varchar2', 1)
			);
			var formSideCondition = new Array (
				new Array ('side_condition', 'varchar2', 1)
			);
			var formSMSRequest = new Array (
				new Array ('cd_ds_sender_dispatch_kind', 'varchar2', 1),
				new Array ('text_request', 'varchar2', 1)
			);

			function myValidateForm(){
				var smsType = '<%= pattern.getValue("CD_PATTERN_TYPE")%>';
				formAll = formGeneral;
				if (smsType == 'CONGRATULATION') {
					formAll = formAll.concat(formCongratulation);
				}
				if (smsType == 'BIRTHDAY') {
					formAll = formAll.concat(formSideCondition);
				}
				if (smsType == 'END_PERION_CARD_VALIDITY') {
					formAll = formAll.concat(formSideCondition);
				}
				if (smsType == 'SMS_REQUEST') {
					formAll = formAll.concat(formSMSRequest);
				}
				//alert(formAll);
				return validateForm(formAll);
			}
		</script>


	    <form action="../crm/dispatch/messages/patternupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
			<input type="hidden" name="id" value="<%= id %>">
		<% } %>
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("id_pattern", false) %></td> <td><input type="text" name="id_ds_pattern" size="20" value="<%= pattern.getValue("ID_DS_PATTERN") %>" readonly="readonly" class="inputfield-ro"> </td>
				<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(pattern.getValue("ID_CLUB")) %>
				</td><td class="bottom_line"><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(pattern.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("name_pattern_type", false) %></td><td><input type="text" name="name_pattern_type" size="65" value="<%= pattern.getValue("NAME_PATTERN_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
				<% if (hasEditInfoPermission) { %>
				<td><%= Bean.smsXML.getfieldTransl("name_pattern_status", true) %></td><td><select name="cd_pattern_status" class="inputfield"><%= Bean.getDsPatternStatusOptions(pattern.getValue("CD_PATTERN_STATUS"), true) %></select></td>			
				<% } else { %>
				<td><%= Bean.smsXML.getfieldTransl("name_pattern_status", false) %></td><td><input type="text" name="name_pattern_status" size="30" value="<%= pattern.getValue("NAME_PATTERN_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } %>
			</tr>
			<tr>
				<% if (hasEditInfoPermission) { %>
				<td><%= Bean.messageXML.getfieldTransl("name_pattern", true) %></td><td><input type="text" name="name_ds_pattern" size="65" value="<%= pattern.getValue("NAME_DS_PATTERN") %>" class="inputfield"></td>
				<td><%= Bean.messageXML.getfieldTransl("begin_action_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_action_date", pattern.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
				<% } else { %>
				<td><%= Bean.messageXML.getfieldTransl("name_pattern", false) %></td><td><input type="text" name="name_ds_pattern" size="65" value="<%= pattern.getValue("NAME_DS_PATTERN") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.messageXML.getfieldTransl("begin_action_date", false) %></td><td><input type="text" name="begin_action_date" size="30" value="<%= pattern.getValue("BEGIN_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } %>
			</tr>
			<tr>
				<% if (hasEditInfoPermission) { %>
				<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("basis_for_operation", true) %></td><td valign="top" rowspan="3"><textarea name="basis_for_operation" cols="60" rows="3" class="inputfield"><%= pattern.getValue("BASIS_FOR_OPERATION") %></textarea></td>
				<td class="bottom_line"><%= Bean.messageXML.getfieldTransl("end_action_date_frmt", false) %></td> <td class="bottom_line"><%=Bean.getCalendarInputField("end_action_date", pattern.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
				<% } else { %>
				<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("basis_for_operation", false) %></td><td valign="top" rowspan="3"><textarea name="basis_for_operation" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= pattern.getValue("BASIS_FOR_OPERATION") %></textarea></td>
				<td class="bottom_line"><%= Bean.messageXML.getfieldTransl("end_action_date_frmt", false) %></td><td class="bottom_line"><input type="text" name="end_action_date" size="30" value="<%= pattern.getValue("END_ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				<% } %>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("can_send_sms", false) %></td><td><input type="text" name="can_send_sms" size="20" value="<%= pattern.getValue("can_send_sms_tsl") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("can_send_email", false) %></td><td><input type="text" name="can_send_email" size="20" value="<%= pattern.getValue("can_send_email_tsl") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.messageXML.getfieldTransl("can_send_office", false) %></td><td><input type="text" name="can_send_office" size="20" value="<%= pattern.getValue("can_send_office_tsl") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>

			<% if ("SMS_REQUEST".equalsIgnoreCase(pattern.getValue("CD_PATTERN_TYPE"))) { %>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.messageXML.getfieldTransl("p_additional_param", false) %></b></td>
			</tr>
			<% if (hasEditInfoPermission) { %>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_sender_dispatch_kind", true) %></td>
				<td>
					<select name="cd_ds_sender_dispatch_kind" class="inputfield"><%= Bean.getDispatchKindOptions(pattern.getValue("CD_DS_SENDER_DISPATCH_KIND"), true) %></select>
				</td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("text_request", true) %></td><td><input type="text" name="text_request" size="20" value="<%=pattern.getValue("TEXT_REQUEST") %>" class="inputfield"></td>			
				<td colspan="2">&nbsp;</td>
			<tr>
			<% } else { %>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_sender_dispatch_kind", false) %></td><td><input type="text" name="name_ds_sender_dispatch_kind" size="20" value="<%= Bean.getDispatchKindName(pattern.getValue("CD_DS_SENDER_DISPATCH_KIND")) %>" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("text_request", true) %></td><td><input type="text" name="text_request" size="20" value="<%=pattern.getValue("TEXT_REQUEST") %>" readonly="readonly" class="inputfield-ro"></td>			
				<td colspan="2">&nbsp;</td>
			<tr>
			<% } %>
			<% } else if ("CONGRATULATION".equalsIgnoreCase(pattern.getValue("CD_PATTERN_TYPE"))) { %>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.messageXML.getfieldTransl("p_additional_param", false) %></b></td>
			</tr>
			<tr>
				<% if (hasEditInfoPermission) { %>
				<td><%= Bean.messageXML.getfieldTransl("side_condition", true) %></td><td><select name="side_condition" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("SMS_PATTERN_SIDE_CONDITION", pattern.getValue("SIDE_CONDITION"), true) %></select></td>
				<% } else { %>
				<td><%= Bean.messageXML.getfieldTransl("side_condition", false) %></td><td><input type="text" name="side_condition" size="20" value="<%= Bean.getMeaningFoCodeValue("SMS_PATTERN_SIDE_CONDITION", pattern.getValue("SIDE_CONDITION")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } %>
			</tr>
			<tr>
				<% if (hasEditInfoPermission) { %>
				<td><%= Bean.messageXML.getfieldTransl("day_in_year", true) %></td>
				<td>
					<select name="day_number" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("DAY_NUMBER", pattern.getValue("DAY_NUMBER"), true) %></select>
					&nbsp;
					<select name="month_name" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("MONTH_NAME", pattern.getValue("MONTH_NAME"), true) %></select>
				</td>
				<% } else { %>
				<td><%= Bean.messageXML.getfieldTransl("day_in_year", false) %></td>
				<td>
					<input type="text" name="day_number" size="20" value="<%= Bean.getMeaningFoCodeValue("DAY_NUMBER", pattern.getValue("DAY_NUMBER")) %>" readonly="readonly" class="inputfield-ro">
					&nbsp;
					<input type="text" name="month_name" size="20" value="<%= Bean.getMeaningFoCodeValue("MONTH_NAME", pattern.getValue("MONTH_NAME")) %>" readonly="readonly" class="inputfield-ro">
				</td>
				<% } %>
			</tr>
			<% } %>
			<% if ("BIRTHDAY".equalsIgnoreCase(pattern.getValue("CD_PATTERN_TYPE")) ||
					"END_PERION_CARD_VALIDITY".equalsIgnoreCase(pattern.getValue("CD_PATTERN_TYPE"))) { %>
			<tr>
				<td colspan="4" class="top_line"><b><%= Bean.messageXML.getfieldTransl("p_additional_param", false) %></b></td>
			</tr>
			<tr>
				<% if (hasEditInfoPermission) { %>
				<td><%= Bean.messageXML.getfieldTransl("side_condition", true) %></td><td><select name="side_condition" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("SMS_PATTERN_SIDE_CONDITION", pattern.getValue("SIDE_CONDITION"), true) %></select></td>
				<% } else { %>
				<td><%= Bean.messageXML.getfieldTransl("side_condition", false) %></td><td><input type="text" name="side_condition" size="20" value="<%= Bean.getMeaningFoCodeValue("SMS_PATTERN_SIDE_CONDITION", pattern.getValue("SIDE_CONDITION")) %>" readonly="readonly" class="inputfield-ro"></td>
				<% } %>
			</tr>
			<% } %>
			<%=	Bean.getCreationAndMoficationRecordFields(
					pattern.getValue(Bean.getCreationDateFieldName()),
					pattern.getValue("CREATED_BY"),
					pattern.getValue(Bean.getLastUpdateDateFieldName()),
					pattern.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="6" align="center">
				<% if (hasEditInfoPermission) { %>
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/patternupdate.jsp") %>
					<%=Bean.getResetButton() %>
				<% } %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/pattern.jsp") %>
				</td>
			</tr>

		</table>
		</form> 
	<% if (hasEditInfoPermission) { %>
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>
	<% } %>

	<%	
	}
%>

<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_PATTERN_SMS")) { %>
		
		<script>
			var formAll = new Array (
			);
			var formCanSMS = new Array (
				new Array ('can_send_sms', 'varchar2', 1)
			);
			var formProfile = new Array (
				new Array ('id_sms_profile', 'varchar2', 1),
				new Array ('text_sms_ua', 'varchar2', 1),
				new Array ('text_sms_ru', 'varchar2', 1)
			);
			
			function checkCanSendSMS(){
				//alert ("dd");
				var canSend = document.getElementById('can_send_sms').value;
				if (canSend == 'Y') {
					document.getElementById('span_sms_profile').innerHTML='<%= Bean.smsXML.getfieldTransl("sms_profile", true) %>';
					document.getElementById('span_text_sms_ua').innerHTML='<%= Bean.smsXML.getfieldTransl("text_sms_ua", true) %>';
					document.getElementById('span_text_sms_ru').innerHTML='<%= Bean.smsXML.getfieldTransl("text_sms_ru", true) %>';
				} else {
					document.getElementById('span_sms_profile').innerHTML='<%= Bean.smsXML.getfieldTransl("sms_profile", false) %>';
					document.getElementById('span_text_sms_ua').innerHTML='<%= Bean.smsXML.getfieldTransl("text_sms_ua", false) %>';
					document.getElementById('span_text_sms_ru').innerHTML='<%= Bean.smsXML.getfieldTransl("text_sms_ru", false) %>';
				}
			}
			checkCanSendSMS();
	
	
			function myValidateForm(){
				var canSend = document.getElementById('can_send_sms').value;
				formAll = formCanSMS;
				if (canSend == 'Y') {
					formAll = formAll.concat(formProfile);
				}
				//alert(formAll);
				return validateForm(formAll);
			}
		</script>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_sms_ua", "length_message_ua") %>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_sms_ru", "length_message_ru") %>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_sms_en", "length_message_en") %>

		<script>
		function CheckAllOblast(pattern) {
   	 		thisCheckBoxes = document.getElementsByTagName('input');
   	 		for (i = 1; i < thisCheckBoxes.length; i++) {
   				myName = thisCheckBoxes[i].id;
   				//alert(myName.substr(0,pattern.length-1));
   				if (myName.substr(0,pattern.length) == pattern){
 					thisCheckBoxes[i].checked = true;
   				}
   	 		}
   	 	}
		</script>

	<form action="../crm/dispatch/messages/patternupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
		<input type="hidden" name="type" value="sms">
	   	<input type="hidden" name="action" value="edit">
	   	<input type="hidden" name="process" value="yes">
	   	<input type="hidden" name="id" value="<%= id %>">

	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("can_send_sms", true) %></td> <td><select name="can_send_sms" id="can_send_sms" class="inputfield" onchange="checkCanSendSMS();"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", pattern.getValue("CAN_SEND_SMS"),true) %></select></td>
			<td><%= Bean.messageXML.getfieldTransl("sms_send_time", false) %></td>
			<td  colspan="3">
				<%= Bean.commonXML.getfieldTransl("h_period_from", false) %>&nbsp;
				<select name="sms_begin_send_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS",pattern.getValue("SMS_BEGIN_SEND_HOUR"), true) %></select>
				<select name="sms_begin_send_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS",pattern.getValue("SMS_BEGIN_SEND_MIN"), true) %></select>
				&nbsp;&nbsp;
				<%= Bean.commonXML.getfieldTransl("h_period_to", false) %>&nbsp;
				<select name="sms_end_send_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS",pattern.getValue("SMS_END_SEND_HOUR"), true) %></select>
				<select name="sms_end_send_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS",pattern.getValue("SMS_END_SEND_MIN"), true) %></select>
			</td>			
		</tr>
		<tr>
			<td><span id="span_sms_profile"><%= Bean.smsXML.getfieldTransl("sms_profile", true) %></span>
				<%= Bean.getGoToDispatchSMSProfileLink(pattern.getValue("ID_SMS_PROFILE")) %>
			</td><td><select name="id_sms_profile" class="inputfield"><%= Bean.getDispatchClientSMSProfileOptions(pattern.getValue("ID_SMS_PROFILE"),true) %></select></td>			
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td> <td><input type="text" name=name_country size="20" value="<%= Bean.getCountryName(pattern.getValue("SMS_CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>			
		</tr>
		<tr>
			<td valign="top" class="top_line"><span id="span_text_sms_ua"><%= Bean.smsXML.getfieldTransl("text_sms_ua", true) %></span></td> 
			<td class="top_line"><textarea name="text_sms_ua" id="text_sms_ua" cols="70" rows="4" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message_ua") %>><%= pattern.getValue("TEXT_SMS_UA") %></textarea></td>
			<%=pattern.getSMSRegionOblastList(pattern.getValue("SMS_CODE_COUNTRY"),  true, "UA") %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td>
			<td valign="top" colspan="5"><input type="text" name="length_message_ua" id="length_message_ua" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top" class="top_line"><span id="span_text_sms_ru"><%= Bean.smsXML.getfieldTransl("text_sms_ru", true) %></span></td> 
			<td class="top_line"><textarea name="text_sms_ru" id="text_sms_ru" cols="70" rows="4" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message_ru") %>><%= pattern.getValue("TEXT_SMS_RU") %></textarea></td>
			<%=pattern.getSMSRegionOblastList(pattern.getValue("SMS_CODE_COUNTRY"), true, "RU") %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top" colspan="5"><input type="text" name="length_message_ru" id="length_message_ru" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top" class="top_line"><%= Bean.smsXML.getfieldTransl("text_sms_en", false) %></td> 
			<td class="top_line"><textarea name="text_sms_en" id="text_sms_en" cols="70" rows="4" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message_en") %>><%= pattern.getValue("TEXT_SMS_EN") %></textarea></td>
			<%=pattern.getSMSRegionOblastList(pattern.getValue("SMS_CODE_COUNTRY"), true, "EN") %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top" colspan="5"><input type="text" name="length_message_en" id="length_message_en" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/patternupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/dispatch/messages/pattern.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<%} else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("DISPATCH_MESSAGES_PATTERN_SMS")) { %>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_sms_ua", "length_message_ua") %>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_sms_ru", "length_message_ru") %>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_sms_en", "length_message_en") %>

	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.messageXML.getfieldTransl("can_send_sms", false) %></td> <td  colspan="3"><input type="text" name="can_send_sms_tsl" size="50" value="<%= pattern.getValue("CAN_SEND_SMS_TSL") %>" readonly="readonly" class="inputfield-ro"></td>			
			<td><%= Bean.messageXML.getfieldTransl("sms_send_time", true) %></td>
			<td  colspan="3">
				<%= Bean.commonXML.getfieldTransl("h_period_from", false) %>&nbsp;
				<input type="text" name="sms_begin_send_hour" size="5" value="<%= Bean.getMeaningForNumValue("HOURS",pattern.getValue("SMS_BEGIN_SEND_HOUR")) %>" readonly="readonly" class="inputfield-ro">
				<input type="text" name="sms_begin_send_min" size="5" value="<%= Bean.getMeaningForNumValue("SECONDS",pattern.getValue("SMS_BEGIN_SEND_MIN")) %>" readonly="readonly" class="inputfield-ro">
				&nbsp;&nbsp;
				<%= Bean.commonXML.getfieldTransl("h_period_to", false) %>&nbsp;
				<input type="text" name="sms_end_send_hour" size="5" value="<%= Bean.getMeaningForNumValue("HOURS",pattern.getValue("SMS_END_SEND_HOUR")) %>" readonly="readonly" class="inputfield-ro">
				<input type="text" name="sms_end_send_min" size="5" value="<%= Bean.getMeaningForNumValue("SECONDS",pattern.getValue("SMS_END_SEND_MIN")) %>" readonly="readonly" class="inputfield-ro">
			</td>			
		</tr>
		<tr>
			<td><%= Bean.smsXML.getfieldTransl("sms_profile", false) %>
				<%= Bean.getGoToDispatchSMSProfileLink(pattern.getValue("ID_SMS_PROFILE")) %>
			</td><td><input type="text" name="name_sms_profile" size="50" value="<%= Bean.getDSSMSProfileName(pattern.getValue("ID_SMS_PROFILE")) %>" readonly="readonly" class="inputfield-ro"></td>			
			<td><%= Bean.jurpersonXML.getfieldTransl("name_country", false) %></td> <td colspan="3"><input type="text" name=name_country size="20" value="<%= Bean.getCountryName(pattern.getValue("SMS_CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top" class="top_line"><%= Bean.smsXML.getfieldTransl("text_sms_ua", false) %></td> 
			<td class="top_line"><textarea name="text_sms_ua" id="text_sms_ua" cols="90" rows="4" readonly="readonly" class="inputfield-ro"><%= pattern.getValue("TEXT_SMS_UA") %></textarea></td>
			<%=pattern.getSMSRegionOblastList(pattern.getValue("SMS_CODE_COUNTRY"), false, "UA") %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td>
			<td valign="top" colspan="5">
				<input type="text" name="length_message_ua" id="length_message_ua" size="8" value="" readonly="readonly" class="inputfield-ro">
				<input type="button">
			</td>
		</tr>
		<tr>
			<td valign="top" class="top_line"><%= Bean.smsXML.getfieldTransl("text_sms_ru", false) %></td> 
			<td class="top_line"><textarea name="text_sms_ru" id="text_sms_ru" cols="90" rows="4" readonly="readonly" class="inputfield-ro"><%= pattern.getValue("TEXT_SMS_RU") %></textarea></td>
			<%=pattern.getSMSRegionOblastList(pattern.getValue("SMS_CODE_COUNTRY"), false, "RU") %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top" colspan="5"><input type="text" name="length_message_ru" id="length_message_ru" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td valign="top" class="top_line"><%= Bean.smsXML.getfieldTransl("text_sms_en", false) %></td> 
			<td class="top_line"><textarea name="text_sms_en" id="text_sms_en" cols="90" rows="4" readonly="readonly" class="inputfield-ro"><%= pattern.getValue("TEXT_SMS_EN") %></textarea></td>
			<%=pattern.getSMSRegionOblastList(pattern.getValue("SMS_CODE_COUNTRY"), false, "EN") %>
		</tr>
		<tr>
			<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top" colspan="5"><input type="text" name="length_message_en" id="length_message_en" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/dispatch/messages/pattern.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<%} %>

<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_PATTERN_EMAIL")) {
	
	boolean hasEditEmailPermission = Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_PATTERN_EMAIL");
	%>
		<script>
			function attachFile(){
				var myFile = document.getElementById('email_attached_file_name').value;
				if (myFile != "") {
					document.getElementById('attach_file').value = "Y";
				}
				return true;
			}
		</script>
	<% if (hasEditEmailPermission) { %>
		<script>
		var formAll = new Array (
		);
		var formCanEmail = new Array (
			new Array ('can_send_email', 'varchar2', 1)
		);
		var formProfile = new Array (
			new Array ('name_email_profile', 'varchar2', 1)
		);
		
		function checkCanSend(){
			var canSend = document.getElementById('can_send_email').value;
			if (canSend == 'Y') {
				document.getElementById('span_name_email_profile').innerHTML='<%= Bean.messageXML.getfieldTransl("email_profile", true) %>';
			} else {
				document.getElementById('span_name_email_profile').innerHTML='<%= Bean.messageXML.getfieldTransl("email_profile", false) %>';
			}
		}
		checkCanSend();


		function myValidateForm(){
			var canSend = document.getElementById('can_send_email').value;
			formAll = formCanEmail;
			if (canSend == 'Y') {
				formAll = formAll.concat(formProfile);
			}
			//alert(formAll);
			return validateForm(formAll);
		}
		</script>
	<% } %>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_email_message", "length_email_message") %>

	    <form action="../crm/dispatch/messages/patternupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="email">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="attach_file" id="attach_file" value="N">
			<input type="hidden" name="id" value="<%= id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("can_send_email", true) %></td> 
				<% if (hasEditEmailPermission) { %>
				<td><select name="can_send_email" id="can_send_email" class="inputfield" onchange="checkCanSend()"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", pattern.getValue("CAN_SEND_EMAIL"),true) %></select></td>			
				<% } else { %>
				<td><input type="text" name="can_send_email" size="10" value="<%= pattern.getValue("CAN_SEND_EMAIL_TSL") %>" readonly="readonly" class="inputfield-ro"></td>			
				<% } %>
			</tr>
			<tr>
				<td><span id="span_name_email_profile"><%= Bean.messageXML.getfieldTransl("email_profile", hasEditEmailPermission) %></span>
					<%= Bean.getGoToDispatchEmailProfileLink(pattern.getValue("ID_EMAIL_PROFILE")) %>
				</td> 
				<% if (hasEditEmailPermission) { %>
				<td  colspan="3">
					<%=Bean.getWindowEmailProfiles("email_profile", pattern.getValue("ID_EMAIL_PROFILE"), "53") %>
				</td>
				<% } else { %>
				<td  colspan="3">
					<input type="text" name="name_email_profile" size="65" value="<%= Bean.getDSEmailProfileName(pattern.getValue("ID_PROFILE")) %>" readonly="readonly" class="inputfield-ro">
				</td>			
				<% } %>
			</tr>
			<tr>
				<% if (hasEditEmailPermission) { %>
				<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td><td><input type="text" name="title_email_message" size="65" value="<%= pattern.getValue("TITLE_EMAIL_MESSAGE") %>" class="inputfield"></td>
				<% } else { %>
				<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td><td><input type="text" name="title_email_message" size="65" value="<%= pattern.getValue("TITLE_EMAIL_MESSAGE") %>" readonly="readonly" class="inputfield-ro"></td>			
				<% } %>
			</tr>
			<tr>
				<% if (hasEditEmailPermission) { %>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top" colspan="3"><textarea name="text_email_message" id="text_email_message" cols="120" rows="10" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_email_message") %>><%= pattern.getValue("TEXT_EMAIL_MESSAGE") %></textarea></td>
				<% } else { %>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top" colspan="3"><textarea name="text_email_message" id="text_email_message" cols="120" rows="10" readonly="readonly" class="inputfield-ro"><%= pattern.getValue("TEXT_EMAIL_MESSAGE") %></textarea></td>
				<% } %>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_email_message" id="length_email_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("message_file_name", false) %></td>
				<% if (hasEditEmailPermission) { %>
				<td valign="middle">
					<% if (!(pattern.getValue("STORED_EMAIL_FILE_NAME")==null || "".equalsIgnoreCase(pattern.getValue("STORED_EMAIL_FILE_NAME")))) { %>
						<%=pattern.getValue("EMAIL_FILE_NAME") %><a href="../FileSender?FILENAME=<%=URLEncoder.encode(pattern.getValue("STORED_EMAIL_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
							<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
						</a>
						<a href="#" onclick="if (window.confirm('<%=Bean.documentXML.getfieldTransl("l_remove_src_doc", false) %>?')) {ajaxpage('../crm/dispatch/messages/patternupdate.jsp?id=<%=id%>&type=email&process=yes&action=remove_file', 'div_main')}" title="<%= Bean.buttonXML.getfieldTransl("button_delete", false) %>">
							<img vspace="0" hspace="0" src="../images/oper/small/delete.gif" align="top">
						</a>
						<br>
					<% } else {%>
						<input type="file" name="email_file_name" size="50" value="<%=pattern.getValue("EMAIL_FILE_NAME") %>" class="inputfield">
					<% } %>
				</td>
				<% } else {%>
					<% if (!(pattern.getValue("STORED_EMAIL_FILE_NAME")==null || "".equalsIgnoreCase(pattern.getValue("STORED_EMAIL_FILE_NAME")))) { %>
					<td valign="middle">
						<%=pattern.getValue("EMAIL_FILE_NAME") %><a href="../FileSender?FILENAME=<%=URLEncoder.encode(pattern.getValue("STORED_EMAIL_FILE_NAME"),"UTF-8")%>" title="<%= Bean.buttonXML.getfieldTransl("open", false) %>" target="_blank">
							<img vspace="0" hspace="0" src="../images/oper/small/open.gif" align="top">
						</a>
					</td>
					<% } %>
				<% } %>
			</tr>
			<tr>
				<td colspan="6" align="center">
				<% if (hasEditEmailPermission) { %>
					<%=Bean.getSubmitButtonMultiPart("../crm/dispatch/messages/patternupdate.jsp", "submit", "updateForm") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/pattern.jsp") %>
				<% } %>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" class="top_line">&nbsp;</td>
			</tr>
			<tr>
				<td valign="top"><b><%= Bean.messageXML.getfieldTransl("h_attached_files", false) %></b></td>
				<td>
				<% if (hasEditEmailPermission) { %>
					<%= pattern.getAttachedFilesHTML("EMAIL", true) %>
					<input type="file" name="email_attached_file_name" id="email_attached_file_name" size="50" value="" class="inputfield" onchange="attachFile();">
						<%=Bean.getSubmitButtonMultiPart("../crm/dispatch/messages/patternupdate.jsp","attach","updateForm") %>
				<% } else { %>
					<%= pattern.getAttachedFilesHTML("EMAIL", false) %>
				<% } %>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
				<% if (!hasEditEmailPermission) { %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/pattern.jsp") %>
				<% } %>
				</td>
			</tr>

		</table>
		</form> 

	<%	
	}
%>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_PATTERN_OFFICE")) {

	boolean hasEditOfficePermission = Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_PATTERN_OFFICE");
%>
		<script>
			function attachFile(){
				var myFile = document.getElementById('office_attached_file_name').value;
				if (myFile != "") {
					document.getElementById('attach_file').value = "Y";
				}
				return true;
			}
		</script>
	<% if (hasEditOfficePermission) { %>
		<script>
		var formAll = new Array (
		);
		var formCanOffice = new Array (
			new Array ('can_send_office', 'varchar2', 1)
		);
		
		function myValidateForm(){
			formAll = formCanOffice;
			return validateForm(formAll);
		}
		</script>
	<% } %>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_office_message", "length_office_message") %>

	    <form action="../crm/dispatch/messages/patternupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="office">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="attach_file" id="attach_file" value="N">
			<input type="hidden" name="id" value="<%= id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("can_send_office", true) %></td> 
				<% if (hasEditOfficePermission) { %>
				<td><select name="can_send_office" id="can_send_office" class="inputfield" onchange="checkCanSend();"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", pattern.getValue("CAN_SEND_OFFICE"),true) %></select></td>			
				<% } else { %>
				<td><input type="text" name="can_send_office" size="10" value="<%= pattern.getValue("CAN_SEND_OFFICE_TSL") %>" readonly="readonly" class="inputfield-ro"></td>			
				<% } %>
			</tr>
			<tr>
				<% if (hasEditOfficePermission) { %>
				<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td><td><input type="text" name="title_office_message" size="65" value="<%= pattern.getValue("TITLE_OFFICE_MESSAGE") %>" class="inputfield"></td>
				<% } else { %>
				<td><%= Bean.messageXML.getfieldTransl("title_message", false) %></td><td><input type="text" name="title_office_message" size="65" value="<%= pattern.getValue("TITLE_OFFICE_MESSAGE") %>" readonly="readonly" class="inputfield-ro"></td>			
				<% } %>
			</tr>
			<tr>
				<% if (hasEditOfficePermission) { %>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top" colspan="3"><textarea name="text_office_message" id="text_office_message" cols="120" rows="10" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_office_message") %>><%= pattern.getValue("TEXT_OFFICE_MESSAGE") %></textarea></td>
				<% } else { %>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("text_message", false) %></td><td valign="top" colspan="3"><textarea name="text_office_message" id="text_office_message" cols="120" rows="10" readonly="readonly" class="inputfield-ro"><%= pattern.getValue("TEXT_OFFICE_MESSAGE") %></textarea></td>
				<% } %>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_office_message" id="length_office_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
				<% if (hasEditOfficePermission) { %>
					<%=Bean.getSubmitButtonMultiPart("../crm/dispatch/messages/patternupdate.jsp", "submit", "updateForm") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/pattern.jsp") %>
				<% } %>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
				<% if (!hasEditOfficePermission) { %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/pattern.jsp") %>
				<% } %>
				</td>
			</tr>

		</table>
		</form> 
<% } %>



<%if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_PATTERN_MESSAGES")) { 
	if (Bean.currentMenu.isCurrentTabAndEditPermitted("DISPATCH_MESSAGES_PATTERN_MESSAGES")) {
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
		<%= Bean.getFindHTML("message_find", message_find, "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&messages_page=1&") %>

		<%=Bean.getSelectOnChangeBeginHTML("message_type", "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&messages_page=1", Bean.smsXML.getfieldTransl("name_sms_message_type", false)) %>
			<%= Bean.getMessagePatternTypeWitoutTerminals(message_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	
		<%=Bean.getSelectOnChangeBeginHTML("message_oper_type", "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&messages_page=1", Bean.messageXML.getfieldTransl("name_ds_message_oper_type", false)) %>
			<%= Bean.getDispatchMessageOperationTypeOptions(message_oper_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("dispatch_kind", "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&messages_page=1", Bean.messageXML.getfieldTransl("name_dispatch_kind", false)) %>
			<%= Bean.getDispatchKindOptions(dispatch_kind, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("cd_state", "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&messages_page=1", Bean.smsXML.getfieldTransl("name_sms_state", false)) %>
			<%= Bean.getSMSStateOptions(cd_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("is_archive", "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&messages_page=1", Bean.smsXML.getfieldTransl("is_archive", false)) %>
			<%=Bean.getSelectOptionHTML(is_archive, "", "") %>
			<%=Bean.getSelectOptionHTML(is_archive, "Y", Bean.messageXML.getfieldTransl("in_archive", false)) %>
			<%=Bean.getSelectOptionHTML(is_archive, "N", Bean.messageXML.getfieldTransl("not_in_archive", false)) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	<%= pattern.getMessagesAppliedHTML(operation_type, message_find, message_type, dispatch_kind, message_oper_type, cd_state, is_archive, l_messages_page_beg, l_messages_page_end) %>

<%}%>

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("DISPATCH_MESSAGES_PATTERN_LOG")) {  
	
	bcReports report = new bcReports(Bean.getReportFormat());

		
	  %>
		<%= report.getReportHeaderHTML("'DISPATCH_CLIENT_MESSAGE_CREATION'", id, find_report, id_report, "", "../crm/dispatch/messages/patternspecs.jsp?page_log_det=1&id=" + id, l_page_report_beg, l_page_report_end, "N") %>

		<% if (!(id_report==null || "".equalsIgnoreCase(id_report))) { %>
			<br>
			<table  <%=Bean.getTableDetailParam() %>>
			<tr>
			<td align="right" width=20>
			  <select onchange="ajaxpage('../crm/dispatch/messages/patternspecs.jsp?id=<%=id %>&id_report=<%=id_report %>&page_log_det=1&log_row_type='+this.value, 'div_main')" name="log_row_type" id="log_row_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("SYS_LOG_ROW_TYPE", l_log_row_type, true) %></select>
			</td>
			<td align="right">
			<%= Bean.getPagesHTML(pageFormName + tagPageReportDet, "../crm/dispatch/messages/patternspecs.jsp?id=" + id + "&id_report="+id_report+"&", "page_log_det") %>
			</td>
			</tr>
			</table>
			<% bcSysLogObject log = new bcSysLogObject(); %>
	
			<%= log.getSysLogReportHTML(id_report, "", l_log_row_type, l_page_log_det_beg, l_page_log_det_end) %>
		<% } %>
	
<% }

} %>

</div></div>
</body>
</html>
