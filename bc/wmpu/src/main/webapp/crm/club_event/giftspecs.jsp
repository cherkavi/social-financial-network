<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcClubActionObject"%>

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcGiftObject"%>
<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

<title></title>
</head>
<body>
<div id="div_tabsheet">

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_GIFTS";

Bean.setJspPageForTabName(pageFormName);

String tagGifts = "_GIFTS";
String tagGiftFind = "_GIFT_FIND";
String tagWinners = "_WINNERS";
String tagWinnerState = "_WINNER_STATE";
String tagWinnerFind = "_WINNER_FIND";
String tagWarehouse = "_WAREHOUSE";
String tagWarehouseFind = "_WAREHOUSE_FIND";

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {	
	Bean.tabsHmSetValue(pageFormName, tab);
	bcGiftObject gift = new bcGiftObject(id);
	
	//Обрабатываем номера страниц
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
	
	String l_warehouse_page = Bean.getDecodeParam(parameters.get("warehouse_page"));
	Bean.pageCheck(pageFormName + tagWarehouse, l_warehouse_page);
	String l_warehouse_page_beg = Bean.getFirstRowNumber(pageFormName + tagWarehouse);
	String l_warehouse_page_end = Bean.getLastRowNumber(pageFormName + tagWarehouse);

	String warehouse_find 	= Bean.getDecodeParam(parameters.get("warehouse_find"));
	warehouse_find 	= Bean.checkFindString(pageFormName + tagWarehouseFind, warehouse_find, l_warehouse_page);
%>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_GIFTS_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club_event/giftupdate.jsp?id=" + gift.getValue("ID_GIFT") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/club_event/giftupdate.jsp?id=" + gift.getValue("ID_GIFT") + "&type=general&action=remove&process=yes", Bean.buttonXML.getfieldTransl("delete", false), gift.getValue("ID_GIFT") + " - " + gift.getValue("NAME_GIFT")) %>
			<%  } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_GIFTS_WAREHOUSE")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagWarehouse, "../crm/club_event/giftspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_GIFTS_WAREHOUSE")+"&", "warehouse_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_GIFTS_ACTIONS")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagGifts, "../crm/club_event/giftspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_GIFTS_ACTIONS")+"&", "gift_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_GIFTS_WINNERS"))	{ %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagWinners, "../crm/club_event/giftspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_GIFTS_WINNERS")+"&", "winners_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(gift.getValue("ID_GIFT") + " - " + gift.getValue("NAME_GIFT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club_event/giftspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%

if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_EVENT_GIFTS_INFO")) {
%>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_gift", false) %> </td><td><input type="text" name="id_gift" size="30" value="<%= gift.getValue("ID_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(gift.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(gift.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_gift", false) %></td><td><input type="text" name="cd_gift" size="30" value="<%= gift.getValue("CD_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_gift", false) %></td><td><input type="text" name="name_gift" size="70" value="<%= gift.getValue("NAME_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_gift_type", false) %></td> <td><input type="text" name="name_gift_type" size="30" value="<%= gift.getValue("NAME_GIFT_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				gift.getValue(Bean.getCreationDateFieldName()),
				gift.getValue("CREATED_BY"),
				gift.getValue(Bean.getLastUpdateDateFieldName()),
				gift.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club_event/gifts.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<%  } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_GIFTS_INFO")) { %>
	<script>
		var formData = new Array (
			new Array ('cd_gift', 'varchar2', 1),
			new Array ('name_gift', 'varchar2', 1),
			new Array ('cd_gift_type', 'varchar2', 1)
		);
	</script>

    <form action="../crm/club_event/giftupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_gift", false) %> </td><td><input type="text" name="id_gift" size="30" value="<%= gift.getValue("ID_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(gift.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(gift.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
        <tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_gift", true) %> </td><td><input type="text" name="cd_gift" size="30" value="<%= gift.getValue("CD_GIFT") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
        <tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_gift", true) %> </td><td><input type="text" name="name_gift" size="70" value="<%= gift.getValue("name_gift") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_gift_type", true) %></td> <td><select name="cd_gift_type" class="inputfield"><%= Bean.getGiftTypeOptions(gift.getValue("CD_GIFT_TYPE"), false) %></select> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				gift.getValue(Bean.getCreationDateFieldName()),
				gift.getValue("CREATED_BY"),
				gift.getValue(Bean.getLastUpdateDateFieldName()),
				gift.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/giftupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/gifts.jsp") %>
			</td>
		</tr>
	</table>

	</form>   
 <% 
}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_GIFTS_WAREHOUSE")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("warehouse_find", warehouse_find, "../crm/club_event/giftspecs.jsp?id=" + id + "&warehouse_page=1") %>

		<td>&nbsp;</td>
		</tr>
	</table>
	<%= gift.getWarehouseGiftsHTML(warehouse_find, l_warehouse_page_beg, l_warehouse_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_GIFTS_ACTIONS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("gift_find", gift_find, "../crm/club_event/giftspecs.jsp?id=" + id + "&gift_page=1") %>

		<td>&nbsp;</td>
		</tr>
	</table>
	<%= gift.getClubActionHTML(gift_find, l_gift_page_beg, l_gift_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_GIFTS_WINNERS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("winner_find", winner_find, "../crm/club_event/giftspecs.jsp?id=" + id + "&winners_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("winner_state", "../crm/club_event/giftspecs.jsp?id=" + id + "&winners_page=1", Bean.club_actionXML.getfieldTransl("NAME_NAT_PRS_WINNER_STATE", false)) %>
			<%=Bean.getNatPrsGiftStateOptions(winner_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	<%= gift.getWinnersHTML(winner_find, winner_state, l_winners_page_beg, l_winners_page_end) %>
<%}
%>

<%   } %>
</div></div>
</body>
</html>
