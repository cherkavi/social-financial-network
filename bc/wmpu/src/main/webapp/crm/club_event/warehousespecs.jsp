<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_WAREHOUSE";

String tagDoc = "_DOC";
String tagDocFind = "_DOC_FIND";
String tagDocType = "_DOC_TYPE";
String tagDocState = "_DOC_STATE";

String tagGifts = "_GIFTS_LIST";
String tagGiftFind = "GIFT_FIND";

String tagWinners = "_WINNERS";
String tagWinnerState = "_WINNER_STATE";
String tagWinnerFind = "_WINNER_FIND";

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
	bcLGObject lgGift = new bcLGObject("GIFT", id);

	Bean.currentMenu.setExistFlag("CLUB_EVENT_WAREHOUSE_GIFTS", true);
	
	//Обрабатываем номера страниц
	String l_doc_page = Bean.getDecodeParam(parameters.get("doc_page"));
	Bean.pageCheck(pageFormName + tagDoc, l_doc_page);
	String l_doc_page_beg = Bean.getFirstRowNumber(pageFormName + tagDoc);
	String l_doc_page_end = Bean.getLastRowNumber(pageFormName + tagDoc);

	String doc_find 	= Bean.getDecodeParam(parameters.get("doc_find"));
	doc_find 	= Bean.checkFindString(pageFormName + tagDocFind, doc_find, l_doc_page);

	String doc_type	= Bean.getDecodeParam(parameters.get("doc_type"));
	doc_type		= Bean.checkFindString(pageFormName + tagDocType, doc_type, l_doc_page);

	String doc_state	= Bean.getDecodeParam(parameters.get("doc_state"));
	doc_state		= Bean.checkFindString(pageFormName + tagDocState, doc_state, l_doc_page);

	String l_gift_page = Bean.getDecodeParam(parameters.get("gift_page"));
	Bean.pageCheck(pageFormName + tagGifts, l_gift_page);
	String l_gift_page_beg = Bean.getFirstRowNumber(pageFormName + tagGifts);
	String l_gift_page_end = Bean.getLastRowNumber(pageFormName + tagGifts);

	String gift_find 	= Bean.getDecodeParam(parameters.get("gift_find"));
	gift_find 	= Bean.checkFindString(pageFormName + tagGiftFind, gift_find, l_gift_page);
	
	String l_winners_page = Bean.getDecodeParam(parameters.get("winners_page"));
	Bean.pageCheck(pageFormName + tagWinners, l_winners_page);
	String l_winners_page_beg = Bean.getFirstRowNumber(pageFormName + tagWinners);
	String l_winners_page_end = Bean.getLastRowNumber(pageFormName + tagWinners);

	String winner_find 	= Bean.getDecodeParam(parameters.get("winner_find"));
	winner_find 	= Bean.checkFindString(pageFormName + tagWinnerFind, winner_find, l_winners_page);
	
	String winner_state	= Bean.getDecodeParam(parameters.get("winner_state"));
	winner_state 		= Bean.checkFindString(pageFormName + tagWinnerState, winner_state, l_winners_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_WAREHOUSE_INFO")) {%>
				<%= Bean.getMenuButton("ADD", "../crm/club_event/warehouseupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/club_event/warehouseupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.logisticXML.getfieldTransl("h_delete_warehouse", false), lgGift.getValue("ID_LG_RECORD") + " - " +  lgGift.getValue("OPERATION_NAME")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_DOC")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_WAREHOUSE_DOC")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/club_event/warehousedocupdate.jsp?lg_type=GIFT&type=doc&id="+ id + "&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDoc, "../crm/club_event/warehousespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_WAREHOUSE_DOC") + "&", "doc_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_GIFTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_WAREHOUSE_GIFTS")) { %>
					<%= Bean.getMenuButton("ADD", "../crm/club_event/warehouseupdate.jsp?type=gift&id="+ id + "&action=add&process=no", "", "") %>
					<%= Bean.getMenuButton("ADD_ALL", "../crm/club_event/warehouseupdate.jsp?type=gift&id="+ id + "&action=add_list&process=no", "", "", Bean.logisticXML.getfieldTransl("h_add_gift_select", false)) %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagGifts, "../crm/club_event/warehousespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_WAREHOUSE_GIFTS") + "&", "gift_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_DELIVERY"))	{ %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagWinners, "../crm/club_event/warehousespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_WAREHOUSE_DELIVERY")+"&", "winners_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(lgGift.getValue("ID_LG_RECORD") + " - " + lgGift.getValue("OPERATION_NAME")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club_event/warehousespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_INFO")) {
%>
	
		<script language="JavaScript">
			var formData = new Array (
				new Array ('action_date', 'varchar2', 1)
			);
			</script>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_WAREHOUSE_INFO")) { %>
		  <form action="../crm/club_event/warehouseupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">
		<%} %>

		<table <%=Bean.getTableDetailParam() %>>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_WAREHOUSE_INFO")) {%>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_record", false) %></td><td><input type="text" name="id_lg_record" size="20" value="<%= lgGift.getValue("ID_LG_RECORD") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(lgGift.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(lgGift.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_type", false) %></td><td><input type="text" name="name_lg_type" size="60" value="<%= lgGift.getValue("NAME_LG_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_receiver", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgGift.getValue("ID_JUR_PRS_RECEIVER")) %>
			</td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_receiver", lgGift.getValue("ID_JUR_PRS_RECEIVER"), lgGift.getValue("SNAME_JUR_PRS_RECEIVER"), "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("action_date_frmt", true) %></td><td><%=Bean.getCalendarInputField("action_date", lgGift.getValue("ACTION_DATE_FRMT"), "10") %></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_receiver", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgGift.getValue("ID_SERVICE_PLACE_RECEIVER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_receiver", lgGift.getValue("ID_SERVICE_PLACE_RECEIVER"), "", "'+document.getElementById('id_jur_prs_receiver').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td rowspan="3"><%=  Bean.logisticXML.getfieldTransl("operation_desc", false) %></td><td rowspan="3"><textarea name="operation_desc" cols="60" rows="6" class="inputfield"><%= lgGift.getValue("OPERATION_DESC") %></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_receiver", false) %>
				<%= Bean.getGoToContactPersonLink(lgGift.getValue("ID_CONTACT_PRS_RECEIVER")) %>
			</td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_receiver", lgGift.getValue("ID_CONTACT_PRS_RECEIVER"), "'+document.getElementById('id_jur_prs_receiver').value+'", "'+document.getElementById('id_service_place_receiver').value+'", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%=  Bean.logisticXML.getfieldTransl("desc_receiver", false) %></td><td><textarea name="desc_receiver" cols="50" rows="3" class="inputfield"><%= lgGift.getValue("DESC_RECEIVER") %></textarea></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_sender", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgGift.getValue("ID_JUR_PRS_SENDER")) %>
			</td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_sender", lgGift.getValue("ID_JUR_PRS_SENDER"), lgGift.getValue("SNAME_JUR_PRS_SENDER"), "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td colspan="2" class="top_line"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_statistics", false) %></b></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_sender", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgGift.getValue("ID_SERVICE_PLACE_SENDER")) %>
			</td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_sender", lgGift.getValue("ID_SERVICE_PLACE_SENDER"), "", "'+document.getElementById('id_jur_prs_sender').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("count_gift_all", false) %></td><td><input type="text" name="count_gift_all" size="20" value="<%= lgGift.getValue("COUNT_GIFT_ALL") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_sender", false) %>
				<%= Bean.getGoToContactPersonLink(lgGift.getValue("ID_CONTACT_PRS_SENDER")) %>
			</td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_sender", lgGift.getValue("ID_CONTACT_PRS_SENDER"), "'+document.getElementById('id_jur_prs_sender').value+'", "'+document.getElementById('id_service_place_sender').value+'", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("count_gift_given", false) %></td><td><input type="text" name="count_gift_given" size="20" value="<%= lgGift.getValue("COUNT_GIFT_GIVEN") %>" readonly="readonly" class="inputfield-ro"></td>
			<td rowspan="3"><%= Bean.logisticXML.getfieldTransl("desc_sender", false) %></td><td rowspan="3"><textarea name="desc_sender" cols="50" rows="3" class="inputfield"><%= lgGift.getValue("DESC_SENDER") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("count_gift_remain", false) %></td><td><input type="text" name="count_gift_remain" size="20" value="<%= lgGift.getValue("COUNT_GIFT_REMAIN") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				lgGift.getValue(Bean.getCreationDateFieldName()),
				lgGift.getValue("CREATED_BY"),
				lgGift.getValue(Bean.getLastUpdateDateFieldName()),
				lgGift.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/warehouseupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/warehouses.jsp") %>
			</td>
		</tr>

		<%} else {%>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_record", false) %></td><td><input type="text" name="id_lg_record" size="20" value="<%= lgGift.getValue("ID_LG_RECORD") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(lgGift.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(lgGift.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_type", false) %></td><td><input type="text" name="name_lg_type" size="60" value="<%= lgGift.getValue("NAME_LG_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_receiver", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgGift.getValue("ID_JUR_PRS_RECEIVER")) %>
			</td><td class="top_line"><input type="text" name="sname_jur_prs_receiver" size="50" value="<%= lgGift.getValue("SNAME_JUR_PRS_RECEIVER") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("action_date_frmt", false) %></td><td><input type="text" name="action_date_frmt" size="20" value="<%= lgGift.getValue("ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_receiver", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgGift.getValue("ID_SERVICE_PLACE_RECEIVER")) %>
			</td><td><input type="text" name="name_service_place_receiver" size="50" value="<%= Bean.getServicePlaceName(lgGift.getValue("ID_SERVICE_PLACE_RECEIVER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan="3"><%=  Bean.logisticXML.getfieldTransl("operation_desc", false) %></td><td rowspan="3"><textarea name="operation_desc" cols="60" rows="6" readonly="readonly" class="inputfield-ro"><%= lgGift.getValue("OPERATION_DESC") %></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_receiver", false) %>
				<%= Bean.getGoToContactPersonLink(lgGift.getValue("ID_CONTACT_PRS_RECEIVER")) %>
			</td><td><input type="text" name="name_contact_prs_receiver" size="50" value="<%= Bean.getContactPrsName(lgGift.getValue("ID_CONTACT_PRS_RECEIVER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%=  Bean.logisticXML.getfieldTransl("desc_receiver", false) %></td><td><textarea name="desc_receiver" cols="46" rows="3" readonly="readonly" class="inputfield-ro"><%= lgGift.getValue("DESC_RECEIVER") %></textarea></td>
		</tr>
		<tr>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_sender", false) %>
			<%=Bean.getGoToJurPrsHyperLink(lgGift.getValue("ID_JUR_PRS_SENDER")) %>
			</td><td class="top_line"><input type="text" name="sname_jur_prs_sender" size="50" value="<%= lgGift.getValue("SNAME_JUR_PRS_SENDER") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2" class="top_line"><b><%= Bean.club_actionXML.getfieldTransl("h_gifts_statistics", false) %></b></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_sender", false) %>
				<%= Bean.getGoToJurPrsHyperLink(lgGift.getValue("ID_SERVICE_PLACE_SENDER")) %>
			</td><td><input type="text" name="name_service_place_sender" size="50" value="<%= Bean.getServicePlaceName(lgGift.getValue("ID_SERVICE_PLACE_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("count_gift_all", false) %></td><td><input type="text" name="count_gift_all" size="20" value="<%= lgGift.getValue("COUNT_GIFT_ALL") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_sender", false) %>
				<%= Bean.getGoToContactPersonLink(lgGift.getValue("ID_CONTACT_PRS_SENDER")) %>
			</td><td><input type="text" name="name_contact_prs_sender" size="50" value="<%= Bean.getContactPrsName(lgGift.getValue("ID_CONTACT_PRS_SENDER")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("count_gift_given", false) %></td><td><input type="text" name="count_gift_given" size="20" value="<%= lgGift.getValue("COUNT_GIFT_GIVEN") %>" readonly="readonly" class="inputfield-ro"></td>
			<td rowspan="3"><%= Bean.logisticXML.getfieldTransl("desc_sender", false) %></td><td rowspan="3"><textarea name="desc_sender" cols="46" rows="3" readonly="readonly" class="inputfield-ro"><%= lgGift.getValue("DESC_SENDER") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("count_gift_remain", false) %></td><td><input type="text" name="count_gift_remain" size="20" value="<%= lgGift.getValue("COUNT_GIFT_REMAIN") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				lgGift.getValue(Bean.getCreationDateFieldName()),
				lgGift.getValue("CREATED_BY"),
				lgGift.getValue(Bean.getLastUpdateDateFieldName()),
				lgGift.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/club_event/warehouses.jsp") %>
			</td>
		</tr>
		<%} %>

	</table>
	</form>

	<% if ((Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_WAREHOUSE_INFO"))) { %>
	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("action_date", false) %>
	<%} %>

<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_DOC")) {
%> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("doc_find", doc_find, "../crm/club_event/warehousespecs.jsp?id=" + id + "&doc_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_type", "../crm/club_event/warehousespecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_type", false)) %>
			<%= Bean.getDocTypeOptions(doc_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("doc_state", "../crm/club_event/warehousespecs.jsp?id=" + id + "&doc_page=1", Bean.documentXML.getfieldTransl("name_doc_state", false)) %>
			<%= Bean.getDocStateOptions(doc_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= lgGift.getDocumentsListHTML("CLUB_EVENT_WAREHOUSE_DOC", "../crm/club_event/warehousedocupdate.jsp", doc_find, doc_type, doc_state, l_doc_page_beg, l_doc_page_end) %> 
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_GIFTS")) {
	%> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("gift_find", gift_find, "../crm/club_event/warehousespecs.jsp?id=" + id + "&gift_page=1") %>
		
			<td>&nbsp;</td>
			</tr>
		</table>
		<%= lgGift.getGiftsListHTML(gift_find, l_gift_page_beg, l_gift_page_end) %> 
	<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_DELIVERY")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("winner_find", winner_find, "../crm/club_event/warehousespecs.jsp?id=" + id + "&winners_page=1") %>
	
		<%=Bean.getSelectOnChangeBeginHTML("winner_state", "../crm/club_event/warehousespecs.jsp?id=" + id + "&winners_page=1", Bean.club_actionXML.getfieldTransl("NAME_NAT_PRS_WINNER_STATE", false)) %>
			<%=Bean.getNatPrsGiftStateOptions(winner_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	
	<%= lgGift.getGiftsWinnersHTML(winner_find, winner_state, l_winners_page_beg, l_winners_page_end) %>
<%}

}
		
%>
</div></div>
</body>
</html>
