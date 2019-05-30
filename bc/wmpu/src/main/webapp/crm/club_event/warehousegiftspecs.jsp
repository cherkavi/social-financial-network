<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>
<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcLGGiftObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_WAREHOUSE";

String tagWinners = "_GIFT_WINNERS";
String tagWinnerState = "_GIFT_WINNER_STATE";
String tagWinnerFind = "_GIFT_WINNER_FIND";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}

if (id==null || "".equals(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% } else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcLGGiftObject gift = new bcLGGiftObject(id);

	Bean.currentMenu.setExistFlag("CLUB_EVENT_WAREHOUSE_GIFTS", false);
	
	if (Bean.currentMenu.isCurrentTab("CLUB_EVENT_WAREHOUSE_GIFTS")) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}

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
				<%= Bean.getMenuButton("ADD", "../crm/club_event/warehousegiftupdate.jsp?type=general&id=" + id + "&id_lg_record=" + gift.getValue("ID_LG_RECORD") + "&action=add&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/club_event/warehousegiftupdate.jsp?type=general&id=" + id + "&id_lg_record=" + gift.getValue("ID_LG_RECORD") + "&action=remove&process=yes", Bean.club_actionXML.getfieldTransl("h_delete_warehouse", false), "") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_DELIVERY"))	{ %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagWinners, "../crm/club_event/warehousegiftspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_WAREHOUSE_DELIVERY")+"&", "winners_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(gift.getValue("CD_GIFT") + " - " + gift.getValue("NAME_GIFT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club_event/warehousegiftspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_INFO")) {
%>
	
		<script>
			var formData = new Array (
				new Array ('desc_gift', 'varchar2', 1),
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('cost_one_gift', 'varchar2', 1),
				new Array ('count_gifts', 'varchar2', 1)
			);
		</script> 

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_WAREHOUSE_INFO")) { %>
		  <form action="../crm/club_event/warehousegiftupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">
		<%} %>

		<table <%=Bean.getTableDetailParam() %>>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_WAREHOUSE_INFO")) {%>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("id_lg_record", false) %>
					<%= Bean.getGoToClubEventWarehouseLink(gift.getValue("ID_LG_RECORD")) %>
				</td><td><input type="text" name="name_gift" size="70" value="<%=gift.getValue("ID_LG_RECORD") %> - <%=gift.getValue("ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.club_actionXML.getfieldTransl("cd_currency", true) %>
					<%= Bean.getGoToCurrencyLink(gift.getValue("CD_CURRENCY")) %>
				</td> <td><select name="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions(gift.getValue("CD_CURRENCY"), true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("name_gift", false) %>
					<%= Bean.getGoToClubEventGiftLink(gift.getValue("ID_GIFT")) %>
				</td><td><input type="text" name="name_gift" size="70" value="<%=gift.getValue("CD_GIFT") %> - <%=gift.getValue("NAME_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.club_actionXML.getfieldTransl("cost_one_gift", true) %></td><td><input type="text" name="cost_one_gift" size="20" value="<%=gift.getValue("COST_ONE_GIFT_FRMT") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("desc_gift", true) %></td><td><input type="text" name="desc_gift" size="70" value="<%=gift.getValue("DESC_GIFT") %>" class="inputfield"></td>
				<td><%= Bean.club_actionXML.getfieldTransl("count_gifts", true) %></td><td><input type="text" name="count_gifts" size="20" value="<%=gift.getValue("COUNT_GIFT_ALL") %>" class="inputfield"></td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					gift.getValue(Bean.getCreationDateFieldName()),
					gift.getValue("CREATED_BY"),
					gift.getValue(Bean.getLastUpdateDateFieldName()),
					gift.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/warehousegiftupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/warehousespecs.jsp?id=" + gift.getValue("ID_LG_RECORD")) %>
			</td>
		</tr>

		<%} else {%>
		<tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("id_lg_record", false) %>
					<%= Bean.getGoToClubEventWarehouseLink(gift.getValue("ID_LG_RECORD")) %>
				</td><td><input type="text" name="name_gift" size="70" value="<%=gift.getValue("ID_LG_RECORD") %> - <%=gift.getValue("ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.club_actionXML.getfieldTransl("cd_currency", false) %>
					<%= Bean.getGoToCurrencyLink(gift.getValue("CD_CURRENCY")) %>
				</td> <td><input type="text" name="name_currency" size="20" value="<%=gift.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("name_gift", false) %>
					<%= Bean.getGoToClubEventGiftLink(gift.getValue("ID_GIFT")) %>
				</td><td><input type="text" name="name_gift" size="70" value="<%=gift.getValue("CD_GIFT") %> - <%=gift.getValue("NAME_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.club_actionXML.getfieldTransl("cost_one_gift", false) %></td><td><input type="text" name="cost_one_gift" size="20" value="<%=gift.getValue("COST_ONE_GIFT_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("desc_gift", false) %></td><td><input type="text" name="desc_gift" size="70" value="<%=gift.getValue("DESC_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.club_actionXML.getfieldTransl("count_gifts", false) %></td><td><input type="text" name="count_gifts" size="20" value="<%=gift.getValue("COUNT_GIFT_ALL") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					gift.getValue(Bean.getCreationDateFieldName()),
					gift.getValue("CREATED_BY"),
					gift.getValue(Bean.getLastUpdateDateFieldName()),
					gift.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/club_event/warehousespecs.jsp?id=" + gift.getValue("ID_LG_RECORD")) %>
			</td>
		</tr>
		<%} %>

	</table>
	</form>


<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_WAREHOUSE_DELIVERY")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("winner_find", winner_find, "../crm/club_event/warehousegiftspecs.jsp?id=" + id + "&winners_page=1") %>
	
		<%=Bean.getSelectOnChangeBeginHTML("winner_state", "../crm/club_event/warehousegiftspecs.jsp?id=" + id + "&winners_page=1", Bean.club_actionXML.getfieldTransl("NAME_NAT_PRS_WINNER_STATE", false)) %>
			<%=Bean.getNatPrsGiftStateOptions(winner_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	
	<%= gift.getGiftsWinnersHTML(winner_find, winner_state, l_winners_page_beg, l_winners_page_end) %>
<%}

}
		
%>
</div></div>
</body>
</html>
