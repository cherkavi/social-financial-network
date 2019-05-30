<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcNatPrsObject"%>
<%@page import="bc.objects.bcJurPrsObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcContactsObject"%>
<%@page import="java.util.Date"%><html>

<%= Bean.getLogOutScript(request) %>
 
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

<title></title>
</head>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_NATPERSONS";

String tagCards = "_CARDS";
String tagCardFind = "_CARD_FIND";
String tagCardRoleState = "_CARD_ROLE_STATE";
String tagCardClubMemberStatus = "_CARD_CLUB_MEMBER_STATUS";
String tagBankAccounts = "_BANK_ACCOUNTS";
String tagBankAccountFind = "_BANK_ACCOUNT_FIND";
String tagBankAccountType = "_BANK_ACCOUNT_TYPE";
String tagMessages = "_MESSAGES";
String tagMessageFind = "_MESSAGE_FIND";
String tagMessageType = "_MESSAGE_TYPE";
String tagDoc = "_DOCS";
String tagDocFind = "_DOC_FIND";
String tagDocType = "_DOC_TYPE";
String tagGift = "_GIFT";
String tagGiftFind = "_GIFT_FIND";
String tagGiftState = "_GIFT_STATE";
String tagGiftRequest = "_GIFT_REQUEST";
String tagGiftRequestState = "_GIFT_REQUEST_STATE";
String tagGiftRequestType = "_GIFT_REQUEST_TYPE";
String tagGiftRequestFind = "_GIFT_REQUEST_FIND";

Bean.setJspPageForTabName(pageFormName);

String personid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
String	filtr_type = Bean.getDecodeParam(parameters.get("filtr_type"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if (filtr_type==null || "".equalsIgnoreCase(filtr_type)) { filtr_type=""; }
if (personid==null || "".equalsIgnoreCase(personid)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
}
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcNatPrsObject natprs = new bcNatPrsObject(personid);
	
	//Обрабатываем номера страниц
	String l_card_page = Bean.getDecodeParam(parameters.get("card_page"));
	Bean.pageCheck(pageFormName + tagCards, l_card_page);
	String l_card_page_beg = Bean.getFirstRowNumber(pageFormName + tagCards);
	String l_card_page_end = Bean.getLastRowNumber(pageFormName + tagCards);

	String card_find 	= Bean.getDecodeParam(parameters.get("card_find"));
	card_find 	= Bean.checkFindString(pageFormName + tagCardFind, card_find, l_card_page);

	String card_role_state 	= Bean.getDecodeParam(parameters.get("card_role_state"));
	card_role_state 	= Bean.checkFindString(pageFormName + tagCardRoleState, card_role_state, l_card_page);

	String club_member_status 	= Bean.getDecodeParam(parameters.get("club_member_status"));
	club_member_status 	= Bean.checkFindString(pageFormName + tagCardClubMemberStatus, club_member_status, l_card_page);
	
	String l_bank_acc_page = Bean.getDecodeParam(parameters.get("bank_acc_page"));
	Bean.pageCheck(pageFormName + tagBankAccounts, l_bank_acc_page);
	String l_bank_acc_page_beg = Bean.getFirstRowNumber(pageFormName + tagBankAccounts);
	String l_bank_acc_page_end = Bean.getLastRowNumber(pageFormName + tagBankAccounts);

	String bank_acc_find 	= Bean.getDecodeParam(parameters.get("bank_acc_find"));
	bank_acc_find 	= Bean.checkFindString(pageFormName + tagBankAccountFind, bank_acc_find, l_bank_acc_page);

	String bank_acc_type 	= Bean.getDecodeParam(parameters.get("bank_acc_type"));
	bank_acc_type 	= Bean.checkFindString(pageFormName + tagBankAccountType, bank_acc_type, l_bank_acc_page);
	
	String l_message_page = Bean.getDecodeParam(parameters.get("message_page"));
	Bean.pageCheck(pageFormName + tagMessages, l_message_page);
	String l_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessages);
	String l_message_page_end = Bean.getLastRowNumber(pageFormName + tagMessages);

	String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
	message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_message_page);

	String message_type	= Bean.getDecodeParam(parameters.get("message_type"));
	message_type		= Bean.checkFindString(pageFormName + tagMessageType, message_type, l_message_page);

	String l_doc_page = Bean.getDecodeParam(parameters.get("doc_page"));
	Bean.pageCheck(pageFormName + tagDoc, l_doc_page);
	String l_doc_page_beg = Bean.getFirstRowNumber(pageFormName + tagDoc);
	String l_doc_page_end = Bean.getLastRowNumber(pageFormName + tagDoc);

	String doc_find 	= Bean.getDecodeParam(parameters.get("doc_find"));
	doc_find 	= Bean.checkFindString(pageFormName + tagDocFind, doc_find, l_doc_page);

	String doc_type	= Bean.getDecodeParam(parameters.get("doc_type"));
	doc_type		= Bean.checkFindString(pageFormName + tagDocType, doc_type, l_doc_page);

	String l_gift_page = Bean.getDecodeParam(parameters.get("gift_page"));
	Bean.pageCheck(pageFormName + tagGift, l_gift_page);
	String l_gift_page_beg = Bean.getFirstRowNumber(pageFormName + tagGift);
	String l_gift_page_end = Bean.getLastRowNumber(pageFormName + tagGift);

	String gift_find 	= Bean.getDecodeParam(parameters.get("gift_find"));
	gift_find 	= Bean.checkFindString(pageFormName + tagGiftFind, gift_find, l_gift_page);

	String gift_state	= Bean.getDecodeParam(parameters.get("gift_state"));
	gift_state		= Bean.checkFindString(pageFormName + tagGiftState, gift_state, l_gift_page);

	String l_request_page = Bean.getDecodeParam(parameters.get("request_page"));
	Bean.pageCheck(pageFormName + tagGiftRequest, l_request_page);
	String l_request_page_beg = Bean.getFirstRowNumber(pageFormName + tagGiftRequest);
	String l_request_page_end = Bean.getLastRowNumber(pageFormName + tagGiftRequest);

	String request_find 	= Bean.getDecodeParam(parameters.get("request_find"));
	request_find 	= Bean.checkFindString(pageFormName + tagGiftRequestFind, request_find, l_request_page);
	
	String request_type	= Bean.getDecodeParam(parameters.get("request_type"));
	request_type		= Bean.checkFindString(pageFormName + tagGiftRequestType, request_type, l_request_page);

	String request_state	= Bean.getDecodeParam(parameters.get("request_state"));
	request_state		= Bean.checkFindString(pageFormName + tagGiftRequestState, request_state, l_request_page);
%>
<body>
<div id="div_tabsheet">

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_INFO")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_NATPERSONS_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/clients/natpersonupdate.jsp?id=" + personid + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/clients/natpersonupdate.jsp?id=" + personid + "&type=general&action=remove&process=yes", Bean.natprsXML.getfieldTransl("LAB_DELETE_FLD", false), natprs.getValue("ID_NAT_PRS") + " - " +  natprs.getValue("FULL_NAME")) %>
				<% } %>
				<%= Bean.getReportHyperLink("SR12", "ID_NAT_PRS=" + personid + "&DATE_REPORT=" + Bean.getSysDate()) %>
	
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_CARDS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagCards, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&tab="+Bean.currentMenu.getTabID("CLIENTS_NATPERSONS_CARDS")+"&", "card_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_BANK_ACCOUNTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_NATPERSONS_BANK_ACCOUNTS")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/accountsupdate.jsp?back_type=NAT_PRS&id=" + personid + "&id_nat_prs=" + personid + "&type=general&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBankAccounts, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&tab="+Bean.currentMenu.getTabID("CLIENTS_NATPERSONS_BANK_ACCOUNTS")+"&", "bank_acc_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_MESSAGES")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMessages, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&tab="+Bean.currentMenu.getTabID("CLIENTS_NATPERSONS_MESSAGES")+"&", "message_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_DOC")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_NATPERSONS_DOC")) { %>
				    <%= Bean.getMenuButtonBase("ADD", "../crm/clients/natpersondocupdate.jsp?id=" + personid + "&type=doc&action=add&process=no", "", "", "", "div_data_detail") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDoc, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&tab="+Bean.currentMenu.getTabID("CLIENTS_NATPERSONS_DOC")+"&", "doc_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_GIFTS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagGift, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&tab="+Bean.currentMenu.getTabID("CLIENTS_NATPERSONS_GIFTS")+"&", "gift_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_GIFT_REQUEST")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagGiftRequest, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&tab="+Bean.currentMenu.getTabID("CLIENTS_NATPERSONS_GIFT_REQUEST")+"&", "request_page") %>
			<% } %>
	
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(natprs.getValue("FULL_NAME")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/clients/natpersonspecs.jsp?id=" + personid) %>
			</td>
	</tr>
	</table>

</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLIENTS_NATPERSONS_INFO")) {

%>
	<table <%=Bean.getTableDetailParam() %>> 
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("tax_code", false) %></td><td><input type="text" name="tax_code" size="30" value="<%= natprs.getValue("TAX_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(natprs.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(natprs.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("surname", false) %></td><td><input type="text" name="surname" size="30" value="<%= natprs.getValue("SURNAME") %>" readonly="readonly" class="inputfield-ro"></td>
 			<td rowspan="8"><%= Bean.natprsXML.getfieldTransl("photo_nat_prs", false) %></td><td rowspan="8">
				<div id="div_photo" class="photo_rect">
					<% if (!(natprs.getValue("FULL_PHOTO_FILE_NAME") == null || "".equalsIgnoreCase(natprs.getValue("FULL_PHOTO_FILE_NAME")))) { %>
						<img class="photo_image" id="image-0" height="150" src="../NatPrsPhoto?id_nat_prs=<%=natprs.getValue("ID_NAT_PRS") %>&noCache=<%=(new Date().getTime()) + Math.random() %>">
					<% } else { %>
						<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_not_found", false) %></p>
					<% } %>
				</div>
			</td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("name", false) %> </td><td><input type="text" name="name" size="30" value="<%= natprs.getValue("NAME") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("patronymic", false) %></td><td><input type="text" name="patronymic" size="30" value="<%= natprs.getValue("PATRONYMIC") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("date_of_birth", false) %> </td><td><input type="text" name="date_of_birth" size="10" value="<%= natprs.getValue("DATE_OF_BIRTH_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("sex", false) %></td><td><input type="text" name="sex" size="30" value="<%= natprs.getValue("SEX_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("title_pasport", false) %></b></td>
		</tr>
		<tr>
		    <td><%= Bean.natprsXML.getfieldTransl("pasport_code_country", false) %></td><td><input type="text" name="pasport_code_country" size="30" value="<%= Bean.getCountryName(natprs.getValue("PASPORT_CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_date", false) %> </td><td><input type="text" name="pasport_date" size="10" value="<%= natprs.getValue("PASPORT_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro">
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_series_number", false) %></td><td><input type="text" name="pasport_series" size="5" value="<%= natprs.getValue("PASPORT_SERIES") %>" readonly="readonly" class="inputfield-ro"><input type="text" name="pasport_number" size="20" value="<%= natprs.getValue("PASPORT_NUMBER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td rowspan="2"><%= Bean.natprsXML.getfieldTransl("pasport_text", false) %></td><td rowspan="2"><textarea name="pasport_text" cols="47" rows="2" readonly="readonly" class="inputfield-ro"><%= natprs.getValue("PASPORT_TEXT") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_division_code", false) %></td><td><input type="text" name="pasport_division_code" size="30" value="<%= natprs.getValue("PASPORT_DIVISION_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("h_contact_information", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("phone_work", false) %>
				<% if ("N".equalsIgnoreCase(natprs.getValue("IS_PHONE_WORK_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="phone_work" size="30" value="<%= natprs.getValue("PHONE_WORK") %>" readonly="readonly" class="inputfield-ro">
				<input type="checkbox" name="is_phone_work_valid" size="20" value="Y" DISABLED style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(natprs.getValue("IS_PHONE_WORK_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
			<td><%= Bean.natprsXML.getfieldTransl("phone_mobile", false) %>
				<% if ("N".equalsIgnoreCase(natprs.getValue("IS_PHONE_MOBILE_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="phone_mobile" size="30" value="<%= natprs.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro">
				<input type="checkbox" name="is_phone_mobile_valid" size="20" value="Y" DISABLED style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(natprs.getValue("IS_PHONE_MOBILE_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("phone_home", false) %>
				<% if ("N".equalsIgnoreCase(natprs.getValue("IS_PHONE_HOME_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="phone_home" size="30" value="<%= natprs.getValue("PHONE_HOME") %>" readonly="readonly" class="inputfield-ro">
				<input type="checkbox" name="is_phone_home_valid" size="20" value="Y" DISABLED style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(natprs.getValue("IS_PHONE_HOME_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
			<td><%= Bean.natprsXML.getfieldTransl("email", false) %> 
				<% if ("N".equalsIgnoreCase(natprs.getValue("IS_EMAIL_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="email" size="30" value="<%= natprs.getValue("EMAIL") %>" readonly="readonly" class="inputfield-ro">
				<input type="checkbox" name="is_email_valid" size="20" value="Y" DISABLED style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(natprs.getValue("IS_EMAIL_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("code_country", false) %></td><td><input type="text" name="fact_code_country" size="30" value="<%= Bean.getCountryName(natprs.getValue("FACT_CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="fact_adr_city" size="30" value="<%= natprs.getValue("FACT_ADR_CITY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="fact_adr_zip_code" size="30" value="<%= natprs.getValue("FACT_ADR_ZIP_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="fact_adr_street" size="30" value="<%= natprs.getValue("FACT_ADR_STREET") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_oblast", false) %> </td><td><input type="text" name="fact_adr_oblast" size="30" value="<%= natprs.getValue("FACT_ADR_OBLAST") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_house", false) %>/<%= Bean.natprsXML.getfieldTransl("fact_adr_case", false) %></td>
			<td>
				<input type="text" name="fact_adr_house" size="10" value="<%= natprs.getValue("FACT_ADR_HOUSE") %>" readonly="readonly" class="inputfield-ro">
				/&nbsp;<input type="text" name="fact_adr_case" size="10" value="<%= natprs.getValue("FACT_ADR_CASE") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="fact_adr_district" size="30" value="<%= natprs.getValue("FACT_ADR_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_apartment", false) %></td>
			<td>
				<input type="text" name="fact_adr_apartment" size="10" value="<%= natprs.getValue("FACT_ADR_APARTMENT") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("REG_ADR_FULL", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("code_country", false) %></td><td><input type="text" name="reg_code_country" size="30" value="<%= Bean.getCountryName(natprs.getValue("REG_CODE_COUNTRY")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_city", false) %> </td><td><input type="text" name="reg_adr_city" size="30" value="<%= natprs.getValue("REG_ADR_CITY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_zip_code", false) %> </td><td><input type="text" name="reg_adr_zip_code" size="30" value="<%= natprs.getValue("REG_ADR_ZIP_CODE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_street", false) %></td><td><input type="text" name="reg_adr_street" size="30" value="<%= natprs.getValue("REG_ADR_STREET") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_oblast", false) %></td><td><input type="text" name="reg_adr_oblast" size="30" value="<%= natprs.getValue("REG_ADR_OBLAST") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_house", false) %>/<%= Bean.natprsXML.getfieldTransl("reg_adr_case", false) %></td>
			<td>
				<input type="text" name="reg_adr_house" size="10" value="<%= natprs.getValue("REG_ADR_HOUSE") %>" readonly="readonly" class="inputfield-ro">
				/&nbsp;<input type="text" name="reg_adr_case" size="10" value="<%= natprs.getValue("REG_ADR_CASE") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_district", false) %></td><td><input type="text" name="reg_adr_district" size="30" value="<%= natprs.getValue("REG_ADR_DISTRICT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_apartment", false) %></td>
			<td>
				<input type="text" name="reg_adr_apartment" size="10" value="<%= natprs.getValue("REG_ADR_APARTMENT") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				natprs.getValue("ID_NAT_PRS"),
				natprs.getValue(Bean.getCreationDateFieldName()),
				natprs.getValue("CREATED_BY"),
				natprs.getValue(Bean.getLastUpdateDateFieldName()),
				natprs.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/clients/natpersons.jsp") %>
			</td>
		</tr>
		
	</table>


<% 	} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLIENTS_NATPERSONS_INFO")) { %>
	<script language="JavaScript">
	function copyFactAddress(){
		
		document.getElementById('reg_adr_district').value = document.getElementById('fact_adr_district').value;
		document.getElementById('reg_adr_house').value = document.getElementById('fact_adr_house').value;
		document.getElementById('reg_adr_zip_code').value = document.getElementById('fact_adr_zip_code').value;
		document.getElementById('reg_adr_city').value = document.getElementById('fact_adr_city').value;
		document.getElementById('reg_adr_street').value = document.getElementById('fact_adr_street').value;
		document.getElementById('reg_adr_case').value = document.getElementById('fact_adr_case').value;
		document.getElementById('reg_adr_apartment').value = document.getElementById('fact_adr_apartment').value;

		var regCountries		= document.getElementById('reg_code_country');
		for(var counter=regCountries.childNodes.length-1;counter>=0;counter--){
			regCountries.removeChild(regCountries.childNodes[counter]);
		}

		var factCountries		= document.getElementById('fact_code_country');
		
		for(counter=0;counter<factCountries.childNodes.length;counter++){
			var option_element=document.createElement("option");
			var text_element=document.createTextNode(factCountries.childNodes[counter].text);
			option_element.value=factCountries.childNodes[counter].value;
			if(factCountries.childNodes[counter].selected==true){
				option_element.selected="selected";
			}
			option_element.appendChild(text_element);
			regCountries.appendChild(option_element);
		}

		var regOblast		= document.getElementById('reg_adr_oblast');
		for(var counter=regOblast.childNodes.length-1;counter>=0;counter--){
			regOblast.removeChild(regOblast.childNodes[counter]);
		}

		var factOblast		= document.getElementById('fact_adr_oblast');
		
		for(counter=0;counter<factOblast.childNodes.length;counter++){
			var option_element=document.createElement("option");
			var text_element=document.createTextNode(factOblast.childNodes[counter].text);
			option_element.value=factOblast.childNodes[counter].value;
			if(factOblast.childNodes[counter].selected==true){
				option_element.selected="selected";
			}
			option_element.appendChild(text_element);
			regOblast.appendChild(option_element);
		}
	}
	function setCheckValue(element) {
		if (element.checked) {
			element.value='Y';
		} else {
			element.value='N';
		}
	}
	</script>
	<script>
		var formData = new Array (
			new Array ('surname', 'varchar2', 1),
			new Array ('date_of_birth', 'varchar2', 1),
			new Array ('sex', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}


		function del_photo() {
			var msg='<%=Bean.buttonXML.getfieldTransl("photo_delete", false) %>?';
			var res=window.confirm(msg);
			if (res) {
				img = document.getElementById('image-0');
				img.src = '';
				div_photo = document.getElementById('div_photo');
				div_photo.innerHTML = '<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_load", false) %></p><input type="hidden" name="id_loaded_file" value="-1"><input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">';
				var action = document.getElementById('action');
				action.value = 'del_photo';
				ajaxpage('../crm/clients/natpersonupdate.jsp?' + mySubmitForm('updateForm'),'div_photo');
				action.value = 'edit';
			}
		}
		function load_photo() {
			var action = document.getElementById('action');
			action.value = 'load_photo';
			var form = document.getElementById('updateForm');
			form.enctype="multipart/form-data";
			post_form('../crm/clients/natpersonupdate.jsp','updateForm','div_photo');
			action.value = 'edit';
			form.enctype='';
			//alert('action.value='+action.value);
		}
	</script>

    <form action="../crm/clients/natpersonupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" id="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id_club" value="<%= natprs.getValue("ID_CLUB") %>">
		<input type="hidden" name="id" value="<%= natprs.getValue("ID_NAT_PRS") %>">
		<input type="hidden" name="id_nat_prs" value="<%= natprs.getValue("ID_NAT_PRS") %>">
		<input type="hidden" name="filtr_type" value="<%=filtr_type %>">		
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("tax_code", false) %></td><td><input type="text" name="tax_code" size="30" value="<%= natprs.getValue("TAX_CODE") %>" class="inputfield"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(natprs.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(natprs.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("surname", true) %></td><td><input type="text" name="surname" size="30" value="<%= natprs.getValue("SURNAME") %>" class="inputfield"></td>
			
			<td rowspan="8"><%= Bean.natprsXML.getfieldTransl("photo_nat_prs", false) %></td><td rowspan="8">
				<div id="div_photo" class="photo_rect">
					<% if (!(natprs.getValue("FULL_PHOTO_FILE_NAME") == null || "".equalsIgnoreCase(natprs.getValue("FULL_PHOTO_FILE_NAME")))) { %>
						<div onclick="del_photo()" title="<%=Bean.buttonXML.getfieldTransl("photo_delete", false) %>" class="del" id="delgimage-0"></div>
						<div title="<%=Bean.buttonXML.getfieldTransl("photo_edit", false) %>" class="edt" id="edtgimage-0">
							<input type="file" title="Изменить фото" onchange="load_photo()" capture="camera" accept="image/*" value="" size="50" name="client_photo" class="photo_file">
						</div>
						<img class="photo_image" id="image-0" height="150" src="../NatPrsPhoto?id_nat_prs=<%=natprs.getValue("ID_NAT_PRS") %>&noCache=<%=(new Date().getTime()) + Math.random() %>">
					<% } else { %>
						<p><%=Bean.clubfundXML.getfieldTransl("titl_picture_load", false) %></p>
						<input type="file" class="photo_file" name="client_photo" size="50" value="" accept="image/*" capture="camera" onchange="load_photo()">
					<% } %>
				</div>
			</td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("name", false) %> </td><td><input type="text" name="name" size="30" value="<%= natprs.getValue("NAME") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("patronymic", false) %></td><td><input type="text" name="patronymic" size="30" value="<%= natprs.getValue("PATRONYMIC") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("date_of_birth", true) %></td><td><%=Bean.getCalendarInputField("date_of_birth", natprs.getValue("DATE_OF_BIRTH_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("sex", true) %></td><td><select name="sex" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("MALE_FEMALE", natprs.getValue("SEX"), false) %></select> </td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("title_pasport", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_code_country", false) %></td><td><select name="pasport_code_country" class="inputfield"><%= Bean.getCountryOptions(natprs.getValue("PASPORT_CODE_COUNTRY"), true) %></select></td>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_date", false) %> </td><td><%=Bean.getCalendarInputField("pasport_date", natprs.getValue("PASPORT_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_series_number", false) %></td><td><input type="text" name="pasport_series" size="5" value="<%= natprs.getValue("PASPORT_SERIES") %>" class="inputfield"><input type="text" name="pasport_number" size="20" value="<%= natprs.getValue("PASPORT_NUMBER") %>" class="inputfield"></td>
			<td rowspan="2"><%= Bean.natprsXML.getfieldTransl("pasport_text", false) %></td><td rowspan="2"><textarea name="pasport_text" cols="47" rows="2" class="inputfield"><%= natprs.getValue("PASPORT_TEXT") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("pasport_division_code", false) %></td><td><input type="text" name="pasport_division_code" size="30" value="<%= natprs.getValue("PASPORT_DIVISION_CODE") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("h_contact_information", false) %></b></td>
		</tr>
		<tr>
			<td>
				<%= Bean.natprsXML.getfieldTransl("phone_work", false) %>
				<% if ("N".equalsIgnoreCase(natprs.getValue("IS_PHONE_WORK_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="phone_work" size="30" value="<%= natprs.getValue("PHONE_WORK") %>" class="inputfield">
				<input type="checkbox" name="is_phone_work_valid" size="20" value="Y" style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(natprs.getValue("IS_PHONE_WORK_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
			<td>
				<%= Bean.natprsXML.getfieldTransl("phone_mobile", false) %>
				<% if ("N".equalsIgnoreCase(natprs.getValue("IS_PHONE_MOBILE_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="phone_mobile" size="30" value="<%= natprs.getValue("PHONE_MOBILE") %>" class="inputfield">
				<input type="checkbox" name="is_phone_mobile_valid" size="20" value="Y" style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(natprs.getValue("IS_PHONE_MOBILE_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
		</tr>
		<tr>
			<td>
				<%= Bean.natprsXML.getfieldTransl("phone_home", false) %>
				<% if ("N".equalsIgnoreCase(natprs.getValue("IS_PHONE_HOME_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="phone_home" size="30" value="<%= natprs.getValue("PHONE_HOME") %>" class="inputfield">
				<input type="checkbox" name="is_phone_home_valid" size="20" value="Y" style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(natprs.getValue("IS_PHONE_HOME_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
			<td>
				<%= Bean.natprsXML.getfieldTransl("email", false) %> 
				<% if ("N".equalsIgnoreCase(natprs.getValue("IS_EMAIL_VALID"))) { %>&nbsp;(<blink><font color="red"><b><%= Bean.natprsXML.getfieldTransl("h_contact_invalid", false) %></b></font></blink>)<%} %>
			</td>
			<td>
				<input type="text" name="email" size="30" value="<%= natprs.getValue("EMAIL") %>" class="inputfield">
				<input type="checkbox" name="is_email_valid" size="20" value="Y" style="height: inherit;padding:0;margin-top:4px;" <% if ("Y".equalsIgnoreCase(natprs.getValue("IS_EMAIL_VALID"))) { %>checked<%} %> title="<%= Bean.natprsXML.getfieldTransl("h_contact_checked", false) %>">
			</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("FACT_ADR_FULL", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("code_country", false) %></td><td><select name="fact_code_country" id="fact_code_country" class="inputfield" onchange="dwr_oblast_array('fact_adr_oblast', this.value, document.getElementById('fact_adr_oblast').value, '<%=Bean.getSessionId() %>');"><option value=""></option><%= Bean.getCountryOptions(natprs.getValue("FACT_CODE_COUNTRY"), false) %></select></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_city", false) %></td><td><input type="text" name="fact_adr_city" id="fact_adr_city" size="30" value="<%= natprs.getValue("FACT_ADR_CITY") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_zip_code", false) %></td><td><input type="text" name="fact_adr_zip_code" id="fact_adr_zip_code" size="30" value="<%= natprs.getValue("FACT_ADR_ZIP_CODE") %>" class="inputfield"></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_street", false) %> </td><td><input type="text" name="fact_adr_street" id="fact_adr_street" size="30" value="<%= natprs.getValue("FACT_ADR_STREET") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_oblast", false) %></td><td><select name="fact_adr_oblast" id="fact_adr_oblast" class="inputfield"><option value=""></option><%= Bean.getOblastOptions(natprs.getValue("FACT_CODE_COUNTRY"), natprs.getValue("FACT_ADR_ID_OBLAST"), false) %></select></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_house", false) %>/<%= Bean.natprsXML.getfieldTransl("fact_adr_case", false) %></td>
			<td>
				<input type="text" name="fact_adr_house" id="fact_adr_house" size="10" value="<%= natprs.getValue("FACT_ADR_HOUSE") %>" class="inputfield">
				/&nbsp;<input type="text" name="fact_adr_case" id="fact_adr_case" size="10" value="<%= natprs.getValue("FACT_ADR_CASE") %>" class="inputfield">
			</td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_district", false) %></td><td><input type="text" name="fact_adr_district" id="fact_adr_district" size="30" value="<%= natprs.getValue("FACT_ADR_DISTRICT") %>" class="inputfield"></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_apartment", false) %></td>
			<td>
				<input type="text" name="fact_adr_apartment" id="fact_adr_apartment" size="30" value="<%= natprs.getValue("FACT_ADR_APARTMENT") %>" class="inputfield">
			</td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.natprsXML.getfieldTransl("REG_ADR_FULL", false) %></b>
			&nbsp;&nbsp;<button type="button" class="button" onclick="copyFactAddress(); "><%= Bean.natprsXML.getfieldTransl("button_copy_fact_address", false) %> </button>
			</td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("code_country", false) %></td><td><select name="reg_code_country" id="reg_code_country" class="inputfield" onchange="dwr_oblast_array('reg_adr_oblast', this.value, document.getElementById('reg_adr_oblast').value, '<%=Bean.getSessionId() %>');"><option value=""></option><%= Bean.getCountryOptions(natprs.getValue("REG_CODE_COUNTRY"), false) %></select></td>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_city", false) %> </td><td><input type="text" name="reg_adr_city" id="reg_adr_city" size="30" value="<%= natprs.getValue("REG_ADR_CITY") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_zip_code", false) %> </td><td><input type="text" name="reg_adr_zip_code" id="reg_adr_zip_code" size="30" value="<%= natprs.getValue("REG_ADR_ZIP_CODE") %>" class="inputfield"></td>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_street", false) %></td><td><input type="text" name="reg_adr_street" id="reg_adr_street" size="30" value="<%= natprs.getValue("REG_ADR_STREET") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_oblast", false) %></td><td><select name="reg_adr_oblast" id="reg_adr_oblast" class="inputfield"><option value=""></option><%= Bean.getOblastOptions(natprs.getValue("REG_CODE_COUNTRY"), natprs.getValue("REG_ADR_ID_OBLAST"), false) %></select></td>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_house", false) %>/<%= Bean.natprsXML.getfieldTransl("reg_adr_case", false) %></td>
			<td>
				<input type="text" name="reg_adr_house" id="reg_adr_house" size="10" value="<%= natprs.getValue("REG_ADR_HOUSE") %>" class="inputfield">
				/&nbsp;<input type="text" name="reg_adr_case" id="reg_adr_case" size="10" value="<%= natprs.getValue("REG_ADR_CASE") %>" class="inputfield">
			</td>
		</tr>
		<tr>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_district", false) %></td><td><input type="text" name="reg_adr_district" id="reg_adr_district" size="30" value="<%= natprs.getValue("REG_ADR_DISTRICT") %>" class="inputfield"></td>
			<td><%= Bean.natprsXML.getfieldTransl("reg_adr_apartment", false) %></td>
			<td>
				<input type="text" name="reg_adr_apartment" id="reg_adr_apartment" size="30" value="<%= natprs.getValue("REG_ADR_APARTMENT") %>" class="inputfield">
			</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				natprs.getValue("ID_NAT_PRS"),
				natprs.getValue(Bean.getCreationDateFieldName()),
				natprs.getValue("CREATED_BY"),
				natprs.getValue(Bean.getLastUpdateDateFieldName()),
				natprs.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/clients/natpersonupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/clients/natpersons.jsp") %>
			</td>
		</tr>

	</table>
	</form>	
	
		<%= Bean.getCalendarScript("date_of_birth", false) %>
		<%= Bean.getCalendarScript("pasport_date", false) %>

<%	}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_CARDS")) {%>    
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("card_find", card_find, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&card_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("club_member_status", "../crm/clients/natpersonspecs.jsp?id=" + personid + "&card_page=1", Bean.clubXML.getfieldTransl("club_member_status", false)) %>
			<%= Bean.getClubMemberStatusOptions(club_member_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("card_role_state", "../crm/clients/natpersonspecs.jsp?id=" + personid + "&card_page=1", Bean.clubcardXML.getfieldTransl("name_nat_prs_role_state", false)) %>
			<%= Bean.getNatPrsRoleStateOptions(card_role_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= natprs.getNatPersonCardsHTML(card_find, club_member_status, card_role_state, l_card_page_beg, l_card_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_BANK_ACCOUNTS")) {%>    
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("bank_acc_find", bank_acc_find, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&bank_acc_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("bank_acc_type", "../crm/clients/natpersonspecs.jsp?id=" + personid + "&bank_acc_page=1", Bean.accountXML.getfieldTransl("name_bank_account_type", false)) %>
			<%= Bean.getBankAccountTypeOptions(bank_acc_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table>
	<%= natprs.getBankAccountsHTML(bank_acc_find, bank_acc_type, l_bank_acc_page_beg, l_bank_acc_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_MESSAGES")) {%>    
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("message_find", message_find, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&message_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("message_type", "../crm/clients/natpersonspecs.jsp?id=" + personid + "&message_page=1", Bean.messageXML.getfieldTransl("type_message", false)) %>
			<%= Bean.getMessagePatternTypeWitoutTerminals(message_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= natprs.getNatPersonMessagesHTML(message_find, message_type, l_message_page_beg, l_message_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_DOC")) {
%>   
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("doc_find", doc_find, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&doc_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_type", "../crm/clients/natpersonspecs.jsp?id=" + personid + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_type", false)) %>
			<%= Bean.getNatPrsDocTypeOptions(doc_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= natprs.getDocumentsHTML(doc_find, doc_type, l_doc_page_beg, l_doc_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_GIFTS")) {
%>  
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("gift_find", gift_find, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&gift_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("gift_state", "../crm/clients/natpersonspecs.jsp?id=" + personid + "&gift_page=1", Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_state", false)) %>
			<%= Bean.getNatPrsGiftStateOptions(gift_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= natprs.getGiftsHTML(gift_find, gift_state, l_gift_page_beg, l_gift_page_end) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLIENTS_NATPERSONS_GIFT_REQUEST")) {
%> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("request_find", request_find, "../crm/clients/natpersonspecs.jsp?id=" + personid + "&request_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("request_type", "../crm/clients/natpersonspecs.jsp?id=" + personid + "&request_page=1", Bean.club_actionXML.getfieldTransl("name_nat_prs_gift_request_type", false)) %>
			<%= Bean.getNatPrsGiftRequestTypeOptions(request_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("request_state", "../crm/clients/natpersonspecs.jsp?id=" + personid + "&request_page=1", Bean.club_actionXML.getfieldTransl("nm_nat_prs_gift_request_state", false)) %>
			<%= Bean.getNatPrsGiftRequestStateOptions(request_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	<%= natprs.getGiftsRequestsHTML(request_type, request_state, request_find, l_request_page_beg, l_request_page_end) %>
<%}
%>

<%   } %>

</div></div>
</body>
</html>
