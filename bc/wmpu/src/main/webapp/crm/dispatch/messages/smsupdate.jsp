<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" import="java.util.*" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>


<%@page import="bc.objects.bcSMSObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %> 
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_SMS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

ArrayList<String> id_value=new ArrayList<String>();

String callSQL = "";

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

%>
<%
if (process.equalsIgnoreCase("no")) {
	if (action.equalsIgnoreCase("add")|| action.equalsIgnoreCase("add2")) { %>
		
		<%= Bean.getOperationTitle(
				Bean.smsXML.getfieldTransl("h_add_sms", false),
				"Y",
				"Y") 
		%>
		<script>
			var formData = new Array (
					new Array ('action', 'varchar2', 1)
			);
		</script>
        <form action="../crm/dispatch/messages/smsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData);">
		        <input type="hidden" name="action" value="adddet">
		        <input type="hidden" name="process" value="no">
		        <input type="hidden" name="id" value="<%=id %>">
		        <input type="hidden" name="action_prev" value="<%=action %>">


		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_message_type", false) %> </td> <td align="left"><input type="text" name="name_sms_message_type" size="35" value="<%=Bean.getSMSTypeName("SEND") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_receiver_dispatch_kind", true) %></td><td><%= Bean.getDSDispatchKindRadio("cd_dispatch_kind","CLIENT") %></td>			
			</tr>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/smsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if (action.equalsIgnoreCase("add")) { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/sms.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/smsspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>

		</table>

		</form>

	<%
	} else if (action.equalsIgnoreCase("adddet")) { 
		String 
			action_prev 				= Bean.getDecodeParam(parameters.get("action_prev")),
			cd_dispatch_kind			= Bean.getDecodeParam(parameters.get("cd_dispatch_kind"));
	%>
	
		<%= Bean.getOperationTitle(
				Bean.smsXML.getfieldTransl("h_add_sms", false),
				"Y",
				"Y") 
		%>
		<script>
			var formData = new Array (
					new Array ('cd_sms_state', 'varchar2', 1),
					new Array ('id_sms_profile', 'varchar2', 1),
					new Array ('text_message', 'varchar2', 1)
			);
		</script>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>

	    <form action="../crm/dispatch/messages/smsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData);">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="cd_dispatch_kind" value="<%=cd_dispatch_kind %>">
	
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_message_type", false) %> </td> <td align="left"><input type="text" name="name_sms_message_type" size="35" value="<%=Bean.getSMSTypeName("SEND") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_receiver_dispatch_kind", false) %> </td> <td align="left"><input type="text" name="name_dispatch_kind" size="35" value="<%=Bean.getDispatchKindName(cd_dispatch_kind) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
				<% if ("CLIENT".equalsIgnoreCase(cd_dispatch_kind)) { %>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("full_name_nat_prs", false) %></td>
	            <td>
					<%=Bean.getWindowFindNatPrs("sender_receiver", "", "35") %>
				</td>
			</tr>
				<% } else if ("PARTNER".equalsIgnoreCase(cd_dispatch_kind)) { %>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("id_contact_prs", false) %></td>
	            <td>
					<%=Bean.getWindowContactPersons("sender_receiver", "", "", "", "35") %>
				</td>
			</tr>
				<% } else if ("SYSTEM".equalsIgnoreCase(cd_dispatch_kind)) { %>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("id_user", false) %></td>
	            <td>
					<%=Bean.getWindowFindUser("sender_receiver", "", "", "35") %>
				</td>
			</tr>
				<% } else if ("TRAINING".equalsIgnoreCase(cd_dispatch_kind)) { %>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("id_tr_person", false) %></td>
	            <td>
					<%=Bean.getWindowFindTrainingPerson("sender_receiver", "", "", "35") %>
				</td>
			</tr>
				<% } %>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("recepient", false) %> </td> <td align="left"><input type="text" name="recepient" size="35" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("begin_action_date", false) %></td><td><%=Bean.getCalendarInputField("begin_action_date", "", "16") %></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("sms_send_time", false) %></td>
				<td>
					<%= Bean.commonXML.getfieldTransl("h_period_from", false) %>&nbsp;
					<select name="begin_send_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS", "", true) %></select>
					<select name="begin_send_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS", "", true) %></select>
					&nbsp;&nbsp;
					<%= Bean.commonXML.getfieldTransl("h_period_to", false) %>&nbsp;
					<select name="end_send_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS", "", true) %></select>
					<select name="end_send_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS", "", true) %></select>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("cd_sms_state", true) %></td><td><select name="cd_sms_state" class="inputfield"><%= Bean.getSMSStateOptions("NEW", false) %></select></td>			
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("sms_profile", true) %></td><td><select name="id_sms_profile" class="inputfield"><%= Bean.getDispatchClientSMSProfileOptions("",false) %></select></td>			
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("text_message", true) %></td> <td  colspan="3"><textarea name="text_message" id="text_message" cols="90" rows="3" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("note_message", false) %></td> <td  colspan="3"><textarea name="note_message" cols="90" rows="3" class="inputfield"></textarea></td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/smsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/dispatch/messages/smsupdate.jsp?id=" + id + "&type=general&process=no&action=" + action_prev) %>
				</td>
			</tr>
	
		</table>
	
		</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", true) %>
	
	<%
	} else if (action.equalsIgnoreCase("repeat")) { 
	
		bcSMSObject sms = new bcSMSObject(id);
		%>
	
		<%= Bean.getOperationTitle(
				Bean.smsXML.getfieldTransl("h_repeat_on_message", false),
				"Y",
				"Y") 
		%>
		<script>
			var formData = new Array (
					new Array ('cd_sms_state', 'varchar2', 1),
					new Array ('id_sms_profile', 'varchar2', 1),
					new Array ('text_message', 'varchar2', 1)
			);
		</script>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_message", "length_message") %>

	    <form action="../crm/dispatch/messages/smsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData);">
		        <input type="hidden" name="action" value="repeat">
		        <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_sms_message_parent" value="<%=id %>">
		        <input type="hidden" name="cd_dispatch_kind" value="<%=sms.getValue("CD_DISPATCH_KIND") %>">
	
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("h_input_message2", false) %></td> <td><textarea name="text_input_message" cols="70" rows="3" readonly="readonly" class="inputfield-ro"><%= sms.getValue("TEXT_MESSAGE") %></textarea></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td class="top_line"><%= Bean.smsXML.getfieldTransl("name_sms_message_type", false) %> </td> <td align="left" class="top_line"><input type="text" name="name_sms_message_type" size="35" value="<%=Bean.getSMSTypeName("SEND") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("cd_ds_receiver_dispatch_kind", false) %> </td> <td align="left"><input type="text" name="name_dispatch_kind" size="35" value="<%= sms.getValue("NAME_DISPATCH_KIND") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
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
				<td><%= Bean.smsXML.getfieldTransl("recepient", false) %> </td> <td align="left"><input type="text" name="recepient" size="35" value="<%= sms.getValue("RECEPIENT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("begin_action_date", false) %></td><td><%=Bean.getCalendarInputField("begin_action_date", "", "16") %></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("sms_send_time", false) %></td>
				<td>
					<%= Bean.commonXML.getfieldTransl("h_period_from", false) %>&nbsp;
					<select name="begin_send_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS", "", true) %></select>
					<select name="begin_send_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS", "", true) %></select>
					&nbsp;&nbsp;
					<%= Bean.commonXML.getfieldTransl("h_period_to", false) %>&nbsp;
					<select name="end_send_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS", "", true) %></select>
					<select name="end_send_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS", "", true) %></select>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("cd_sms_state", true) %></td><td><select name="cd_sms_state" class="inputfield"><%= Bean.getSMSStateOptions("NEW", false) %></select></td>			
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("sms_profile", true) %>
					<%= Bean.getGoToDispatchSMSProfileLink(sms.getValue("ID_SMS_PROFILE")) %>
				</td><td><select name="id_sms_profile" class="inputfield"><%= Bean.getDispatchClientSMSProfileOptions(sms.getValue("ID_SMS_PROFILE"),true) %></select></td>			
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("text_message", true) %></td> <td  colspan="3"><textarea name="text_message" id="text_message" cols="70" rows="3" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_message") %>></textarea></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_message" id="length_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("note_message", false) %></td> <td  colspan="3"><textarea name="note_message" cols="70" rows="3" class="inputfield"></textarea></td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/smsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if (action.equalsIgnoreCase("add")) { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/sms.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/smsspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
	
		</table>
	
		</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", true) %>
	<%
	} else if (action.equalsIgnoreCase("gift_request")) { 
		
		bcSMSObject sms = new bcSMSObject(id);
		%>
	
		<%= Bean.getOperationTitle(
				Bean.club_actionXML.getfieldTransl("h_add_nat_prs_gift_request", false),
				"Y",
				"Y") 
		%>
		<script>
			var formData = new Array (
				new Array ('cd_nat_prs_gift_request_type', 'varchar2', 1),
				new Array ('cd_nat_prs_gift_request_state', 'varchar2', 1),
				new Array ('date_accept', 'varchar2', 1),
				new Array ('name_nat_prs', 'varchar2', 1)
			);
		</script>
	    <form action="../crm/dispatch/messages/smsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData);">
		        <input type="hidden" name="action" value="gift_request">
		        <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_accept_sms_message" value="<%=id %>">
	
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_request_type", true) %></td><td><select name="cd_nat_prs_gift_request_type" class="inputfield"><%=Bean.getNatPrsGiftRequestTypeOptionsExclude("SMS", "", true) %> </select></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_request_state", true) %></td><td><select name="cd_nat_prs_gift_request_state" class="inputfield"><%=Bean.getNatPrsGiftRequestStateOptions("ACCEPT", true) %> </select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_accept", true) %></td> <td><%=Bean.getCalendarInputField("date_accept", Bean.getSysDate(), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_nat_prs", true) %>
				<%= Bean.getGoToNatPrsLink(sms.getValue("ID_NAT_PRS")) %>
			</td><td><%=Bean.getWindowFindNatPrs("nat_prs", sms.getValue("ID_NAT_PRS"), "35") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
 		    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %></td>
		  	<td>
				<%=Bean.getWindowFindClubAction("club_event", "", "", "35") %>
	  		</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_accept_sms_message", false) %></td><td><input type="text" name="id_accept_sms_message" size="20" value="<%= id %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("text_request", false) %></td><td><textarea name="text_request" cols="60" rows="3" class="inputfield"><%= sms.getValue("TEXT_MESSAGE") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
			<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/smsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if (action.equalsIgnoreCase("add")) { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/sms.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/smsspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
	
		</table>
	
		</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_accept", false) %>
	<%
	} else { %> 
    	<%= Bean.getUnknownActionText(action) %><% 
    }
} else if (process.equalsIgnoreCase("yes")) {
	
	String 
		cd_dispatch_kind		= Bean.getDecodeParam(parameters.get("cd_dispatch_kind")),
		id_nat_prs 				= Bean.getDecodeParam(parameters.get("id_nat_prs")),
		id_contact_prs			= Bean.getDecodeParam(parameters.get("id_contact_prs")),
		id_tr_person			= Bean.getDecodeParam(parameters.get("id_tr_person")),
		id_user 				= Bean.getDecodeParam(parameters.get("id_user")),
		id_sender_receiver		= Bean.getDecodeParam(parameters.get("id_sender_receiver")),
		recepient 				= Bean.getDecodeParam(parameters.get("recepient")),
		text_message 			= Bean.getDecodeParam(parameters.get("text_message")),
		note_message 			= Bean.getDecodeParam(parameters.get("note_message")),
		cd_sms_state 			= Bean.getDecodeParam(parameters.get("cd_sms_state")),
		begin_action_date 		= Bean.getDecodeParam(parameters.get("begin_action_date")),
		id_sms_profile 			= Bean.getDecodeParam(parameters.get("id_sms_profile")),
		is_archive	 			= Bean.getDecodeParam(parameters.get("is_archive")),
		begin_send_hour	 		= Bean.getDecodeParam(parameters.get("begin_send_hour")),
		begin_send_min	 		= Bean.getDecodeParam(parameters.get("begin_send_min")),
		end_send_hour	 		= Bean.getDecodeParam(parameters.get("end_send_hour")),
		end_send_min	 		= Bean.getDecodeParam(parameters.get("end_send_min")),
		id_sms_message_parent	= Bean.getDecodeParam(parameters.get("id_sms_message_parent"));
    
	if (action.equalsIgnoreCase("add")) { 
		
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.add_sms("+
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [14];

		pParam[0] = cd_dispatch_kind;
		pParam[1] = id_sender_receiver;
		pParam[2] = recepient;
		pParam[3] = "SEND";
		pParam[4] = text_message;
		pParam[5] = begin_action_date;
		pParam[6] = begin_send_hour;
		pParam[7] = begin_send_min;
		pParam[8] = end_send_hour;
		pParam[9] = end_send_min;
		pParam[10] = cd_sms_state;
		pParam[11] = id_sms_profile;
		pParam[12] = note_message;
		pParam[13] = Bean.getDateFormat();
	
	 	%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/dispatch/messages/smsspecs.jsp?id=" , "../crm/dispatch/messages/sms.jsp") %>
		<% 	

	} else if (action.equalsIgnoreCase("repeat")) { 
		
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.add_sms2("+
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [15];

		pParam[0] = id_sms_message_parent;
		pParam[1] = cd_dispatch_kind;
		pParam[2] = id_sender_receiver;
		pParam[3] = recepient;
		pParam[4] = "SEND";
		pParam[5] = text_message;
		pParam[6] = begin_action_date;
		pParam[7] = begin_send_hour;
		pParam[8] = begin_send_min;
		pParam[9] = end_send_hour;
		pParam[10] = end_send_min;
		pParam[11] = cd_sms_state;
		pParam[12] = id_sms_profile;
		pParam[13] = note_message;
		pParam[14] = Bean.getDateFormat();
	
	 	%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/dispatch/messages/smsspecs.jsp?id=" , "../crm/dispatch/messages/sms.jsp") %>
		<% 	

	} else if (action.equalsIgnoreCase("gift_request")) { 
		String
			id_accept_sms_message			= Bean.getDecodeParam(parameters.get("id_accept_sms_message")),
			cd_nat_prs_gift_request_type	= Bean.getDecodeParam(parameters.get("cd_nat_prs_gift_request_type")),
			cd_nat_prs_gift_request_state	= Bean.getDecodeParam(parameters.get("cd_nat_prs_gift_request_state")),
			date_accept						= Bean.getDecodeParam(parameters.get("date_accept")),
			date_reject						= Bean.getDecodeParam(parameters.get("date_reject")),
			date_processed					= Bean.getDecodeParam(parameters.get("date_processed")),
			id_club_event					= Bean.getDecodeParam(parameters.get("id_club_event")),
			text_request					= Bean.getDecodeParam(parameters.get("text_request"));
		
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.add_nat_prs_gift_request("+
			"?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [8];

		pParam[0] = id_accept_sms_message;
		pParam[1] = cd_nat_prs_gift_request_type;
		pParam[2] = cd_nat_prs_gift_request_state;
		pParam[3] = date_accept;
		pParam[4] = id_nat_prs;
		pParam[5] = id_club_event;
		pParam[6] = text_request;
		pParam[7] = Bean.getDateFormat();
	
	 	%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/dispatch/messages/smsspecs.jsp?id=" + id_accept_sms_message + "&id_gift_request=" , "") %>
		<% 	

	} else if (action.equalsIgnoreCase("remove_request")) { 
		String	id_nat_prs_gift_request			= Bean.getDecodeParam(parameters.get("id_nat_prs_gift_request"));
		
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.delete_nat_prs_gift_request(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id_nat_prs_gift_request;
	
	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/smsspecs.jsp?id=" + id, "") %>
		<% 	

	} else if (action.equalsIgnoreCase("execute")) {

		Object current_parameter;
			Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
			while(keySetIterator.hasNext()) {
				try{
					key = (String)keySetIterator.next();
					
					if(key.contains("chb")){
						id_value.add(key.substring(3));
					}
					
				}
				catch(Exception ex){
					Bean.writeException(
							"../crm/dispatch/messages/smsupdate.jsp",
							"",
							process,
							action,
							Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.getMessage());
				}
				 
			}
		
		String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
		
	    String[] results = new String[2];
	    String resultInt = "";
	    String resultFull = "0";
	    String resultMessage = "";
	    String resultMessageFull = "";

		String[] pParam = new String [2];
 
		%>

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		if (!(id_value == null)) {
		 for(int counter=0;counter<id_value.size();counter++){
			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.set_sms_action(?,?,?)}";

	    	pParam[0] = id_value.get(counter);
	    	pParam[1] = operation_type;
	    	
	    	 /*
			 if ("send".equalsIgnoreCase(operation_type)) {
				 callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.send_sms('" +
		    		id_value.get(counter)+"',?)}";
			 } else if ("delete".equalsIgnoreCase(operation_type)) {
				 callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.delete_sms('" +
		    		id_value.get(counter)+"',?)}";
			 } else if ("cancel".equalsIgnoreCase(operation_type)) {
				 callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.cancel_sms('" +
		    		id_value.get(counter)+"',?)}";
			 } else if ("to_archive".equalsIgnoreCase(operation_type)) {
				 callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.to_archive_sms('" +
		    		id_value.get(counter)+"',?)}";
			 } else if ("from_archive".equalsIgnoreCase(operation_type)) {
				 callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.from_archive_sms('" +
		    		id_value.get(counter)+"',?)}";
			 } else {
				 callSQL = "";
			 }
	    	*/
		
			results = Bean.myCallFunctionParam(callSQL, pParam, 2);
			resultInt = results[0];
			resultMessage = results[1];
			
			%>
			<%= Bean.showCallSQL(callSQL) %>
			<%
			
			if (!("0".equalsIgnoreCase(resultInt))) {
				resultFull = resultInt;
				resultMessageFull = resultMessageFull + "; " +resultMessage;
			}
		 }
		}
		
		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessageFull, 
   	    		"../crm/dispatch/messages/sms.jsp?operation_type=" + operation_type + "&specid=", 
   	    		"../crm/dispatch/messages/sms.jsp?operation_type=" + operation_type + "&specid=", 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

    } else if (action.equalsIgnoreCase("remove")) { 
			
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.delete_sms(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;
	
	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/sms.jsp" , "../crm/dispatch/messages/smsspecs.jsp?id=" + id) %>
		<% 	

    } else if (action.equalsIgnoreCase("edit")) { 
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.update_sms("+
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [15];

		pParam[0] = id;
		pParam[1] = cd_dispatch_kind;
		pParam[2] = id_sender_receiver;
		pParam[3] = recepient;
		pParam[4] = text_message;
		pParam[5] = begin_action_date;
		pParam[6] = begin_send_hour;
		pParam[7] = begin_send_min;
		pParam[8] = end_send_hour;
		pParam[9] = end_send_min;
		pParam[10] = cd_sms_state;
		pParam[11] = id_sms_profile;
		pParam[12] = note_message;
		pParam[13] = is_archive;
		pParam[14] = Bean.getDateFormat();
	
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/smsspecs.jsp?id=" + id , "") %>
		<% 	

    } else if (action.equalsIgnoreCase("edit_short")) { 
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.update_sms_short("+
			"?,?,?,?,?)}";

		String[] pParam = new String [4];

		pParam[0] = id;
		pParam[1] = cd_dispatch_kind;
		pParam[2] = id_sender_receiver;
		pParam[3] = note_message;
	
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/smsspecs.jsp?id=" + id , "") %>
		<% 	

    } else if (action.equalsIgnoreCase("edit_note")) { 
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.update_sms_note(?,?,?)}";

		String[] pParam = new String [2];

		pParam[0] = id;
		pParam[1] = note_message;
	
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/smsspecs.jsp?id=" + id , "") %>
		<% 	

	} else { %> 
    	<%= Bean.getUnknownActionText(action) %><% 
    }
} else {%> 
	<%= Bean.getUnknownProcessText(process) %> <%
}
%>

</body>
</html>
