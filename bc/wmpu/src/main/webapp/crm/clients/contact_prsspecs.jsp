<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcContactsObject"%><html>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head> 

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_CONTACT_PRS";
String tagUser = "_USER";
String tagUserFind = "_USER_FIND";
String tagUserStatus = "_USER_STATUS";
String tagTermUser = "_TERM_USER";
String tagTermUserFind = "_TERMINAL_USER_FIND";
String tagTermUserAccessType = "_TERM_USER_ACCESS_TYPE";
String tagTermUserStatus = "_TERM_USER_STATUS";
String tagMessages = "_MESSAGES";
String tagMessageFind = "_MESSAGE_FIND";
String tagMessageType = "_MESSAGE_TYPE";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab	= Bean.tabsHmGetValue(pageFormName); 
}

if (id==null || "".equalsIgnoreCase(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% }
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcContactsObject contact = new bcContactsObject(id);
	contact.setLanguage(Bean.getLanguage());
	
	boolean isShareholder = !Bean.isEmpty(contact.getValue("CARD_SERIAL_NUMBER"));

	//Обрабатываем номера страниц
	String l_user_page = Bean.getDecodeParam(parameters.get("user_page"));
	Bean.pageCheck(pageFormName + tagUser, l_user_page);
	String l_user_page_beg = Bean.getFirstRowNumber(pageFormName + tagUser);
	String l_user_page_end = Bean.getLastRowNumber(pageFormName + tagUser);

	String user_find 	= Bean.getDecodeParam(parameters.get("user_find"));
	user_find 	= Bean.checkFindString(pageFormName + tagUserFind, user_find, l_user_page);

	String user_status 	= Bean.getDecodeParam(parameters.get("user_status"));
	user_status 	= Bean.checkFindString(pageFormName + tagUserStatus, user_status, l_user_page);

	//Обрабатываем номера страниц
	String l_term_user_page = Bean.getDecodeParam(parameters.get("term_user_page"));
	Bean.pageCheck(pageFormName + tagTermUser, l_term_user_page);
	String l_term_user_page_beg = Bean.getFirstRowNumber(pageFormName + tagTermUser);
	String l_term_user_page_end = Bean.getLastRowNumber(pageFormName + tagTermUser);

	String term_user_find 	= Bean.getDecodeParam(parameters.get("term_user_find"));
	term_user_find 	= Bean.checkFindString(pageFormName + tagTermUserFind, term_user_find, l_term_user_page);

	String term_user_access_type 	= Bean.getDecodeParam(parameters.get("term_user_access_type"));
	term_user_access_type 	= Bean.checkFindString(pageFormName + tagTermUserAccessType, term_user_access_type, l_term_user_page);
	
	String term_user_status 	= Bean.getDecodeParam(parameters.get("term_user_status"));
	term_user_status 	= Bean.checkFindString(pageFormName + tagTermUserStatus, term_user_status, l_term_user_page);

	String l_message_page = Bean.getDecodeParam(parameters.get("message_page"));
	Bean.pageCheck(pageFormName + tagMessages, l_message_page);
	String l_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessages);
	String l_message_page_end = Bean.getLastRowNumber(pageFormName + tagMessages);

	String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
	message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_message_page);

	String message_type	= Bean.getDecodeParam(parameters.get("message_type"));
	message_type		= Bean.checkFindString(pageFormName + tagMessageType, message_type, l_message_page);

%>

<body>
<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_CONTACT_PRS_INFO")) { %>
				<%= Bean.getMenuButton("ADD", "../crm/clients/contact_prsupdate.jsp?id_contact_prs=" + contact.getValue("ID_CONTACT_PRS") + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/clients/contact_prsupdate.jsp?id_contact_prs=" + contact.getValue("ID_CONTACT_PRS") + "&type=general&action=remove&process=yes", Bean.contactXML.getfieldTransl("l_remove_contact", false) + " \\\'" + contact.getValue("NAME_CONTACT_PRS") + "\\\'", "") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CONTACT_PRS_SYSTEM_USER")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_CONTACT_PRS_SYSTEM_USER")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/contact_prsupdate.jsp?id_contact_prs=" + contact.getValue("ID_CONTACT_PRS") + "&type=system_user&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagUser, "../crm/clients/contact_prsspecs.jsp?id=" + contact.getValue("ID_CONTACT_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_CONTACT_PRS_SYSTEM_USER")+"&", "user_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CONTACT_PRS_TERMINAL_USER")) { %>
			    <% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_CONTACT_PRS_TERMINAL_USER")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/terminaluserupdate.jsp?back_type=CONTACT_PRS&id=" + contact.getValue("ID_CONTACT_PRS") + "&id_nat_prs_role=" + contact.getValue("ID_CONTACT_PRS") + "&type=user&action=addlist&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagTermUser, "../crm/clients/contact_prsspecs.jsp?id=" + contact.getValue("ID_CONTACT_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_CONTACT_PRS_TERMINAL_USER")+"&", "term_user_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CONTACT_PRS_MESSAGES")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMessages, "../crm/clients/contact_prsspecs.jsp?id=" + contact.getValue("ID_CONTACT_PRS") + "&tab="+Bean.currentMenu.getTabID("CLIENTS_CONTACT_PRS_MESSAGES")+"&", "message_page") %>
			<% } %>
		</tr>
	</table>

	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(contact.getValue("ID_CONTACT_PRS") + " - " + contact.getValue("NAME_CONTACT_PRS")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/contact_prsspecs.jsp?id=" + contact.getValue("ID_CONTACT_PRS")) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLIENTS_CONTACT_PRS_INFO")) {
%>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
	   <tr>
			<td><%= Bean.contactXML.getfieldTransl("name_contact_prs", false) %></td> <td><input type="text" name="name_contact_prs" size="55" value="<%=contact.getValue("NAME_CONTACT_PRS") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(contact.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(contact.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%=Bean.contactXML.getfieldTransl("contact_prs_place", false)%>
				<%=Bean.getGoToJurPrsHyperLink(contact.getValue("ID_SERVICE_PLACE_WORK")) %>
			</td> <td><input type="text" name="sname_service_place_work" size="55" value="<%=contact.getValue("SNAME_SERVICE_PLACE_WORK")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club_date_beg", false) %></td> <td><input type="text" name="club_date_beg" size="10" value="<%=contact.getValue("CLUB_DATE_BEG_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("name_post", false) %></td> <td><input type="text" name="name_post" size="55" value="<%=contact.getValue("NAME_POST") %>" readonly="readonly" class="inputfield-ro"> </td>
			<% if (isShareholder) { %>
			<td><%= Bean.contactXML.getfieldTransl("is_shareholder", false) %>
					<%= Bean.getGoToNatPrsLink(contact.getValue("ID_NAT_PRS")) %>
				</td> <td><input type="text" name="is_shareholder_tsl" size="30" value="<%=contact.getValue("NAME_CONTACT_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td><%= Bean.contactXML.getfieldTransl("is_shareholder", false) %>
				</td> <td><input type="text" name="is_shareholder_tsl" size="10" value="<%=Bean.getMeaningFoCodeValue("YES_NO", "N") %>" readonly="readonly" class="inputfield-ro"> </td>
			<% } %>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.contactXML.getfieldTransl("desc_contact_prs", false) %></td><td rowspan="3"><textarea name="desc_contact_prs" cols="50" rows="3" readonly="readonly" class="inputfield-ro"><%=contact.getValue("DESC_CONTACT_PRS") %></textarea></td>
			<% if (isShareholder) { %>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						contact.getValue("CARD_SERIAL_NUMBER"),
						contact.getValue("CARD_ID_ISSUER"),
						contact.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td><td><input type="text" name="cd_card1" size="30" value="<%= contact.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>			
		</tr>
	    <tr>
			<% if (isShareholder) { %>
			<td>&nbsp;</td>
			<td><%=Bean.natprsXML.getfieldTransl("title_card", false)%>:&nbsp;
				<%=Bean.getCheckBoxBase("is_corporate_card", contact.getValue("IS_CORPORATE_CARD"), Bean.natprsXML.getfieldTransl("is_corporate_card", false), false, true) %>
				<%=Bean.getCheckBoxBase("is_temporary_card", contact.getValue("IS_TEMPORARY_CARD"), Bean.natprsXML.getfieldTransl("is_temporary_card", false), false, true) %>
			</td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>			
		</tr>
	    <tr><td colspan="2">&nbsp;</td></tr>
	    <tr>
			<td colspan="4" class="top_line"><b><%= Bean.contactXML.getfieldTransl("h_contact_information", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("phone_work", false) %></td> <td><input type="text" name="phone_work" size="30" value="<%=contact.getValue("PHONE_WORK") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.contactXML.getfieldTransl("phone_mobile", false) %></td> <td><input type="text" name="phone_mobile" size="30" value="<%=contact.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("email_work", false) %></td><td><input type="text" name="email_work" size="30" value="<%=contact.getValue("EMAIL_WORK") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
	    </tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				contact.getValue("ID_CONTACT_PRS"),
				contact.getValue(Bean.getCreationDateFieldName()),
				contact.getValue("CREATED_BY"),
				contact.getValue(Bean.getLastUpdateDateFieldName()),
				contact.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/clients/contact_prs.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<%
} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_CONTACT_PRS_INFO")) {
%> 
	<script>
		var formData = new Array (
			new Array ('cd_post', 'varchar2', 1),
			new Array ('name_contact_prs', 'varchar2', 1),
			new Array ('club_date_beg', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}
	</script>

    <form action="../crm/clients/contact_prsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id_contact_prs" value="<%=contact.getValue("ID_CONTACT_PRS")%>">
        <input type="hidden" name="LUD" value="<%=contact.getValue("LUD")%>">
	<table <%=Bean.getTableDetailParam() %>>
	   <tr>
			<td><%= Bean.contactXML.getfieldTransl("name_contact_prs", false) %></td> <td><input type="text" name="name_contact_prs" size="55" value="<%=contact.getValue("NAME_CONTACT_PRS") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(contact.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(contact.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%=Bean.contactXML.getfieldTransl("contact_prs_place", false)%>
				<%=Bean.getGoToJurPrsHyperLink(contact.getValue("ID_SERVICE_PLACE_WORK")) %>
			</td> <td><input type="text" name="sname_service_place_work" size="55" value="<%=contact.getValue("SNAME_SERVICE_PLACE_WORK")%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("club_date_beg", contact.getValue("CLUB_DATE_BEG_DF"), "10") %></td>
			
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("name_post", true) %></td> <td><select name="cd_post" class="inputfield" > <%= Bean.getContactPrsTypeOptions(contact.getValue("CD_POST"), true) %></select></td>
			<% if (isShareholder) { %>
			<td><%= Bean.contactXML.getfieldTransl("is_shareholder", false) %>
					<%= Bean.getGoToNatPrsLink(contact.getValue("ID_NAT_PRS")) %>
				</td> <td><input type="text" name="is_shareholder_tsl" size="30" value="<%=contact.getValue("NAME_CONTACT_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td><%=Bean.clubcardXML.getfieldTransl("title_card_not_issued", false)%></td>
			<td><%=Bean.getSubmitButtonAjax("../crm/cards/clubcardupdate.jsp", "button_give", "updateGiveCard", "div_main") %></td>
			<% } %>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.contactXML.getfieldTransl("desc_contact_prs", false) %></td><td rowspan="3"><textarea name="desc_contact_prs" cols="52" rows="3" class="inputfield"><%=contact.getValue("DESC_NAT_PRS_ROLE") %></textarea></td>
			<% if (isShareholder) { %>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						contact.getValue("CARD_SERIAL_NUMBER"),
						contact.getValue("CARD_ID_ISSUER"),
						contact.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td><td><input type="text" name="cd_card1" size="30" value="<%= contact.getValue("CD_CARD1") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>			
		</tr>
	    <tr>
			<% if (isShareholder) { %>
			<td>&nbsp;</td>
			<td><%=Bean.natprsXML.getfieldTransl("title_card", false)%>:&nbsp;
				<%=Bean.getCheckBoxBase("is_corporate_card", contact.getValue("IS_CORPORATE_CARD"), Bean.natprsXML.getfieldTransl("is_corporate_card", false), false, true) %>
				<%=Bean.getCheckBoxBase("is_temporary_card", contact.getValue("IS_TEMPORARY_CARD"), Bean.natprsXML.getfieldTransl("is_temporary_card", false), false, true) %>
			</td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>			
		</tr>
	    <tr><td colspan="2">&nbsp;</td></tr>
	    <tr>
			<td colspan="4" class="top_line"><b><%= Bean.contactXML.getfieldTransl("h_contact_information", false) %></b></td>
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("phone_work", false) %></td> <td><input type="text" name="phone_work" size="30" value="<%=contact.getValue("PHONE_WORK") %>" class="inputfield"></td>
			<td><%= Bean.contactXML.getfieldTransl("phone_mobile", false) %></td> <td><input type="text" name="phone_mobile" size="30" value="<%=contact.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.contactXML.getfieldTransl("email_work", false) %></td><td><input type="text" name="email_work" size="30" value="<%=contact.getValue("EMAIL_WORK") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				contact.getValue("ID_CONTACT_PRS"),
				contact.getValue(Bean.getCreationDateFieldName()),
				contact.getValue("CREATED_BY"),
				contact.getValue(Bean.getLastUpdateDateFieldName()),
				contact.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/contact_prsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/contact_prs.jsp") %>
			</td>
		</tr>

	</table>
	</form>

	<form action="../crm/cards/clubcardupdate.jsp" name="updateGiveCard" id="updateGiveCard" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="type" value="actions">
	    	<input type="hidden" name="process" value="no">
	    	<input type="hidden" name="id_contact_prs" value="<%=id %>">
	    	<input type="hidden" name="action" value="choisecontact">
	    	<input type="hidden" name="card_action" value="give">
	</form>

<% } 

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CONTACT_PRS_SYSTEM_USER")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("user_find", user_find, "../crm/clients/contact_prsspecs.jsp?id=" + id + "&user_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("user_status", "../crm/clients/contact_prsspecs.jsp?id=" + id + "&user_page=1", Bean.contactXML.getfieldTransl("name_user_status", false)) %>
			<%= Bean.getUserStatusOptions(user_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%=contact.getSystemUsersHTML(user_find, user_status, l_user_page_beg, l_user_page_end) %>
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CONTACT_PRS_TERMINAL_USER")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("term_user_find", term_user_find, "../crm/clients/contact_prsspecs.jsp?id=" + id + "&term_user_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("term_user_access_type", "../crm/clients/contact_prsspecs.jsp?id=" + id + "&term_user_page=1", Bean.contactXML.getfieldTransl("name_term_user_access_type", false)) %>
			<%= Bean.getTermUserAccessTypeOptions(term_user_access_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("term_user_status", "../crm/clients/contact_prsspecs.jsp?id=" + id + "&term_user_page=1", Bean.contactXML.getfieldTransl("name_term_user_status", false)) %>
			<%= Bean.getTermUserStatusOptions(term_user_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%=contact.getTerminalUsersHTML(term_user_find, term_user_access_type, term_user_status, l_term_user_page_beg, l_term_user_page_end) %>
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_CONTACT_PRS_MESSAGES")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("message_find", message_find, "../crm/clients/contact_prsspecs.jsp?id=" + id + "&message_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("message_type", "../crm/clients/contact_prsspecs.jsp?id=" + id + "&message_page=1", Bean.messageXML.getfieldTransl("type_message", false)) %>
			<%= Bean.getMessagePatternTypeWitoutTerminals(message_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%=contact.getMessagesHTML(message_find, message_type, l_message_page_beg, l_message_page_end) %>
<% }
 } %>
</div></div>
</body>
</html>
