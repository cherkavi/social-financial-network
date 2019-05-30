<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>

<%= Bean.getLogOutScript(request) %>


<%@page import="bc.objects.bcNatPrsGiftRequestObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_REQUEST";

String tagDoc = "_DOC";
String tagGifts = "_GIFTS_LIST";
String tagGiftsState = "_GIFTS_STATE";
String tagGiftsFind = "_GIFTS_FIND";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String filtr = Bean.getDecodeParam(parameters.get("filtr"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}


if (id==null || "".equals(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% } else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcNatPrsGiftRequestObject req = new bcNatPrsGiftRequestObject(id);

	//Обрабатываем номера страниц
	String l_gift_page = Bean.getDecodeParam(parameters.get("gift_page"));
	Bean.pageCheck(pageFormName + tagGifts, l_gift_page);
	String l_gift_page_beg = Bean.getFirstRowNumber(pageFormName + tagGifts);
	String l_gift_page_end = Bean.getLastRowNumber(pageFormName + tagGifts);

	String find_gift	= Bean.getDecodeParam(parameters.get("find_gift"));
	find_gift	 		= Bean.checkFindString(pageFormName + tagGiftsFind, find_gift, l_gift_page);

	String gift_state	= Bean.getDecodeParam(parameters.get("gift_state"));
	gift_state	 		= Bean.checkFindString(pageFormName + tagGiftsState, gift_state, l_gift_page);

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_REQUEST_INFO")) {%>
				<%= Bean.getMenuButton("ADD", "../crm/club_event/requestupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/club_event/requestupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.club_actionXML.getfieldTransl("h_delete_nat_prs_gift_request", false), req.getValue("ID_NAT_PRS_GIFT_REQUEST") + " - " +  req.getValue("FULL_NAME")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_REQUEST_GIFTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_REQUEST_GIFTS")) {%>
					<%= Bean.getMenuButton("ADD", "../crm/club_event/requestupdate.jsp?id=" + id + "&type=gift&action=add&process=no", "", "") %>
		    		<%= Bean.getMenuButton("RUN", "../crm/club_event/requestupdate.jsp?id=" + id + "&type=gift&action=disassemble&process=yes", Bean.club_actionXML.getfieldTransl("h_disassemble_nat_prs_gift_request", false), req.getValue("ID_NAT_PRS_GIFT_REQUEST") + " - " +  req.getValue("FULL_NAME")) %>
				<% } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagGifts, "../crm/club_event/requestspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_REQUEST_GIFTS") + "&", "gift_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(req.getValue("ID_NAT_PRS_GIFT_REQUEST") + " - " + req.getValue("FULL_NAME")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club_event/requestspecs.jsp?id=" + id) %>
			</td>
	
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_REQUEST_INFO")) {
%>
	
		<script language="JavaScript">
			var formData = new Array (
				new Array ('cd_nat_prs_gift_request_type', 'varchar2', 1),
				new Array ('cd_nat_prs_gift_request_state', 'varchar2', 1),
				new Array ('date_accept', 'varchar2', 1),
				new Array ('name_nat_prs', 'varchar2', 1)
			);
			</script>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_REQUEST_INFO")) { %>
		  <form action="../crm/club_event/requestupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">
		<%} %>

		<table <%=Bean.getTableDetailParam() %>>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_REQUEST_INFO")) {%>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_nat_prs_gift_request", false) %></td><td><input type="text" name="id_nat_prs_gift_request" size="20" value="<%= req.getValue("ID_NAT_PRS_GIFT_REQUEST") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(req.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(req.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_request_type", true) %></td><td><select name="cd_nat_prs_gift_request_type" class="inputfield"><%=Bean.getNatPrsGiftRequestTypeOptions(req.getValue("CD_NAT_PRS_GIFT_REQUEST_TYPE"), true) %> </select></td>
			<td class="top_line"><%= Bean.club_actionXML.getfieldTransl("name_nat_prs", true) %>
				<%= Bean.getGoToNatPrsLink(req.getValue("ID_NAT_PRS")) %>
			</td>
			<td class="top_line">
				<%=Bean.getWindowFindNatPrs("nat_prs", req.getValue("ID_NAT_PRS"), req.getValue("FULL_NAME"), "35") %>
			</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_request_state", true) %></td><td><select name="cd_nat_prs_gift_request_state" class="inputfield"><%=Bean.getNatPrsGiftRequestStateOptions(req.getValue("CD_NAT_PRS_GIFT_REQUEST_STATE"), true) %> </select></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_full", false) %></td><td><input type="text" name="fact_adr_full" size="50" value="<%= req.getValue("FACT_ADR_FULL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_accept", true) %></td> <td><%=Bean.getCalendarInputField("date_accept", req.getValue("DATE_ACCEPT_FRMT"), "10") %></td>
			<td><%= Bean.natprsXML.getfieldTransl("phone_contact", false) %></td><td><input type="text" name="phone_contact" size="20" value="<%= req.getValue("phone_contact") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_processed", false) %></td> <td><%=Bean.getCalendarInputField("date_processed", req.getValue("DATE_PROCESSED_FRMT"), "10") %></td>
 		    <td class="top_line"><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
				<%= Bean.getGoToClubEventLink(req.getValue("ID_CLUB_EVENT")) %>
			</td>
		  	<td class="top_line">
				<%=Bean.getWindowFindClubAction("club_event", req.getValue("ID_CLUB_EVENT"), req.getValue("NAME_CLUB_EVENT"), "35") %>
	  		</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_reject", false) %></td> <td><%=Bean.getCalendarInputField("date_reject", req.getValue("DATE_REJECT_FRMT"), "10") %></td>
			<td><%= Bean.club_actionXML.getfieldTransl("id_accept_sms_message", false) %>
				<%= Bean.getGoToDispatchSMSLink(req.getValue("ID_ACCEPT_SMS_MESSAGE")) %>
			</td><td><input type="text" name="id_accept_sms_message" size="20" value="<%= req.getValue("ID_ACCEPT_SMS_MESSAGE") %>" class="inputfield"></td>
		</tr>
		<tr>
			<% if ("ERROR".equalsIgnoreCase(req.getValue("CD_NAT_PRS_GIFT_REQUEST_STATE"))) { %>
			<td><%= Bean.club_actionXML.getfieldTransl("error_messages", false) %></td><td><textarea name="error_messages" cols="45" rows="2" readonly="readonly" class="inputfield-ro"><%= req.getValue("ERROR_MESSAGES") %></textarea></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
			<td><%= Bean.club_actionXML.getfieldTransl("text_request", false) %></td><td><textarea name="text_request" cols="46" rows="2" class="inputfield"><%= req.getValue("TEXT_REQUEST") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				req.getValue(Bean.getCreationDateFieldName()),
				req.getValue("CREATED_BY"),
				req.getValue(Bean.getLastUpdateDateFieldName()),
				req.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/requestupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/request.jsp") %>
			</td>
		</tr>

		<%} else {%>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_nat_prs_gift_request", false) %></td><td><input type="text" name="id_nat_prs_gift_request" size="20" value="<%= req.getValue("ID_NAT_PRS_GIFT_REQUEST") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(req.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(req.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_request_type", false) %></td><td><input type="text" name="name_nat_prs_gift_request_type" size="50" value="<%= Bean.getNatPrsGiftRequestTypeName(req.getValue("cd_nat_prs_gift_request_type")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line"><%= Bean.club_actionXML.getfieldTransl("name_nat_prs", false) %>
				<%= Bean.getGoToNatPrsLink(req.getValue("ID_NAT_PRS")) %>
			</td>
			<td class="top_line">
				<input type="text" name="name_nat_prs" size="50" value="<%= req.getValue("FULL_NAME") %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_request_state", false) %></td><td><input type="text" name="name_nat_prs_gift_request_state" size="50" value="<%= Bean.getNatPrsGiftRequestStateName(req.getValue("cd_nat_prs_gift_request_state")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("fact_adr_full", false) %></td><td><input type="text" name="fact_adr_full" size="50" value="<%= req.getValue("FACT_ADR_FULL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_accept", false) %></td> <td><input type="text" name="date_accept" size="20" value="<%= req.getValue("DATE_ACCEPT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.natprsXML.getfieldTransl("phone_contact", false) %></td><td><input type="text" name="phone_contact" size="20" value="<%= req.getValue("phone_contact") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_processed", false) %></td> <td><input type="text" name="date_processed" size="20" value="<%= req.getValue("DATE_PROCESSED_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
 		    <td class="top_line"><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
				<%= Bean.getGoToClubEventLink(req.getValue("ID_CLUB_EVENT")) %>
			</td>
		  	<td class="top_line">
				<input type="text" name="name_club_event" size="49" value="<%= req.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro">
	  		</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_reject", false) %></td> <td><input type="text" name="date_reject" size="20" value="<%= req.getValue("DATE_REJECT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("id_accept_sms_message", false) %>
				<%= Bean.getGoToDispatchSMSLink(req.getValue("ID_ACCEPT_SMS_MESSAGE")) %>
			</td><td><input type="text" name="id_accept_sms_message" size="20" value="<%= req.getValue("ID_ACCEPT_SMS_MESSAGE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<% if ("ERROR".equalsIgnoreCase(req.getValue("CD_NAT_PRS_GIFT_REQUEST_STATE"))) { %>
			<td><%= Bean.club_actionXML.getfieldTransl("error_messages", false) %></td><td><textarea name="error_messages" cols="45" rows="2" readonly="readonly" class="inputfield-ro"><%= req.getValue("ERROR_MESSAGES") %></textarea></td>
			<% } else { %>
			<td colspan="2">&nbsp;</td>
			<% } %>
			<td><%= Bean.club_actionXML.getfieldTransl("text_request", false) %></td><td><textarea name="text_request" cols="45" rows="2" readonly="readonly" class="inputfield-ro"><%= req.getValue("TEXT_REQUEST") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				req.getValue(Bean.getCreationDateFieldName()),
				req.getValue("CREATED_BY"),
				req.getValue(Bean.getLastUpdateDateFieldName()),
				req.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/club_event/request.jsp") %>
			</td>
		</tr>
		<%} %>

	</table>
	</form>

	<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_REQUEST_INFO")) { %>
		<%= Bean.getCalendarScript("date_accept", false) %>
		<%= Bean.getCalendarScript("date_reject", false) %>
		<%= Bean.getCalendarScript("date_processed", false) %>
	<% } %>
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_REQUEST_GIFTS")) {
%> 
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
			<%= Bean.getFindHTML("find_gift", find_gift, "../crm/club_event/requestspecs.jsp?id=" + id + "&gift_page=1&") %>
	
		<%=Bean.getSelectOnChangeBeginHTML("gift_state", "../crm/club_event/requestspecs.jsp?id=" + id + "&gift_page=1", Bean.club_actionXML.getfieldTransl("NAME_NAT_PRS_WINNER_STATE", false)) %>
			<%=Bean.getNatPrsGiftStateOptions(gift_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= req.getGiftsHTML(find_gift, gift_state, l_gift_page_beg, l_gift_page_end) %> 
<%}

}
		
%>
</div></div>
</body>
</html>
