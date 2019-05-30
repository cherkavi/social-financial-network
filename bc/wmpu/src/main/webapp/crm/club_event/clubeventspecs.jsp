<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcClubActionObject"%>

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body>
<div id="div_tabsheet">

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_EVENT";

Bean.setJspPageForTabName(pageFormName);

String tagGifts = "_GIFTS";
String tagGiftsFind = "_GIFTS_FIND";
String tagWinners = "_WINNERS";
String tagWinnerState = "_WINNER_STATE";
String tagWinnerFind = "_WINNER_FIND";
String tagEstimate = "_ESTIMATE";
String tagEstimateFind = "_ESTIMATE_FIND";
String tagEstimateCriterion = "_ESTIMATE_CRITERION";
String tagMessages = "_MESSAGES";
String tagMessageFind = "_MESSAGE_FIND";
String tagMessageType = "_MESSAGE_TYPE";
String tagGivenSchedule = "_GIVEN_SCHEDULE";
String tagGivenScheduleFind = "_GIVEN_SCHEDULE_FIND";
String tagGivenScheduleDay = "_GIVEN_SCHEDULE_DAY";
String tagCardTask = "_CARD_TASK";
String tagCardTaskFind = "_CARD_TASK_FIND";
String tagCardTaskType = "_CARD_TASK_TYPE";
String tagCardTaskState = "_CARD_TASK_STATE";
String tagRequest = "_REQUEST_DET";
String tagRequestType = "_REQUEST_TYPE_DET";
String tagRequestState = "_REQUEST_STATE_DET";
String tagRequestFind = "_REQUEST_FIND_DET";
String tagBonCards = "_BON_CARDS_DET";
String tagBonCardsFind = "_BON_CARDS_FIND";

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (id==null || "".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {	
	Bean.tabsHmSetValue(pageFormName, tab);
	bcClubActionObject action = new bcClubActionObject(id);
	
	//Обрабатываем номера страниц
	String l_gifts_page = Bean.getDecodeParam(parameters.get("gifts_page"));
	Bean.pageCheck(pageFormName + tagGifts, l_gifts_page);
	String l_gifts_page_beg = Bean.getFirstRowNumber(pageFormName + tagGifts);
	String l_gifts_page_end = Bean.getLastRowNumber(pageFormName + tagGifts);
	
	String gifts_find 	= Bean.getDecodeParam(parameters.get("gifts_find"));
	gifts_find 	= Bean.checkFindString(pageFormName + tagGiftsFind, gifts_find, l_gifts_page);
	
	String l_order_column = Bean.getDecodeParam(parameters.get("col"));
	String l_order_type = Bean.getDecodeParam(parameters.get("order"));

	String l_given_schedule_page = Bean.getDecodeParam(parameters.get("given_schedule_page"));
	Bean.pageCheck(pageFormName + tagGivenSchedule, l_given_schedule_page);
	String l_given_schedule_page_beg = Bean.getFirstRowNumber(pageFormName + tagGivenSchedule);
	String l_given_schedule_page_end = Bean.getLastRowNumber(pageFormName + tagGivenSchedule);
	
	String given_schedule_find 	= Bean.getDecodeParam(parameters.get("given_schedule_find"));
	given_schedule_find 	= Bean.checkFindString(pageFormName + tagGivenScheduleFind, given_schedule_find, l_given_schedule_page);
	
	String given_schedule_day 	= Bean.getDecodeParam(parameters.get("given_schedule_day"));
	given_schedule_day 	= Bean.checkFindString(pageFormName + tagGivenScheduleDay, given_schedule_day, l_given_schedule_page);

	String l_winners_page = Bean.getDecodeParam(parameters.get("winners_page"));
	Bean.pageCheck(pageFormName + tagWinners, l_winners_page);
	String l_winners_page_beg = Bean.getFirstRowNumber(pageFormName + tagWinners);
	String l_winners_page_end = Bean.getLastRowNumber(pageFormName + tagWinners);

	String winner_find 	= Bean.getDecodeParam(parameters.get("winner_find"));
	winner_find 	= Bean.checkFindString(pageFormName + tagWinnerFind, winner_find, l_winners_page);
	
	String winner_state	= Bean.getDecodeParam(parameters.get("winner_state"));
	winner_state 		= Bean.checkFindString(pageFormName + tagWinnerState, winner_state, l_winners_page);
	
	String l_estimate_page = Bean.getDecodeParam(parameters.get("estimate_page"));
	Bean.pageCheck(pageFormName + tagEstimate, l_estimate_page);
	String l_estimate_page_beg = Bean.getFirstRowNumber(pageFormName + tagEstimate);
	String l_estimate_page_end = Bean.getLastRowNumber(pageFormName + tagEstimate);

	String estimate_find 	= Bean.getDecodeParam(parameters.get("estimate_find"));
	estimate_find 	= Bean.checkFindString(pageFormName + tagEstimateFind, estimate_find, l_estimate_page);

	String estimate_criterion 	= Bean.getDecodeParam(parameters.get("estimate_criterion"));
	estimate_criterion 	= Bean.checkFindString(pageFormName + tagEstimateCriterion, estimate_criterion, l_estimate_page);

	String l_message_page = Bean.getDecodeParam(parameters.get("message_page"));
	Bean.pageCheck(pageFormName + tagMessages, l_message_page);
	String l_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessages);
	String l_message_page_end = Bean.getLastRowNumber(pageFormName + tagMessages);

	String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
	message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_message_page);

	String message_type	= Bean.getDecodeParam(parameters.get("message_type"));
	message_type		= Bean.checkFindString(pageFormName + tagMessageType, message_type, l_message_page);

	String l_request_page = Bean.getDecodeParam(parameters.get("request_page"));
	Bean.pageCheck(pageFormName + tagRequest, l_request_page);
	String l_request_page_beg = Bean.getFirstRowNumber(pageFormName + tagRequest);
	String l_request_page_end = Bean.getLastRowNumber(pageFormName + tagRequest);

	String request_find 	= Bean.getDecodeParam(parameters.get("request_find"));
	request_find 	= Bean.checkFindString(pageFormName + tagRequestFind, request_find, l_request_page);
	
	String request_type	= Bean.getDecodeParam(parameters.get("request_type"));
	request_type		= Bean.checkFindString(pageFormName + tagRequestType, request_type, l_request_page);

	String request_state	= Bean.getDecodeParam(parameters.get("request_state"));
	request_state		= Bean.checkFindString(pageFormName + tagRequestState, request_state, l_request_page);

	String l_card_task_page = Bean.getDecodeParam(parameters.get("card_task_page"));
	Bean.pageCheck(pageFormName + tagCardTask, l_card_task_page);
	String l_card_task_page_beg = Bean.getFirstRowNumber(pageFormName + tagCardTask);
	String l_card_task_page_end = Bean.getLastRowNumber(pageFormName + tagCardTask);
	
	String card_task_find 	= Bean.getDecodeParam(parameters.get("card_task_find"));
	card_task_find 	= Bean.checkFindString(pageFormName + tagCardTaskFind, card_task_find, l_card_task_page);

	String card_task_type = Bean.getDecodeParam(parameters.get("card_task_type"));
	if (card_task_type==null) {
		card_task_type = Bean.filtersHmGetValue(pageFormName + tagCardTaskType);
	} else if  ("".equalsIgnoreCase(card_task_type)) {
		card_task_type = "";
		Bean.filtersHmSetValue(pageFormName + tagCardTaskType, card_task_type);
	} else {
		Bean.filtersHmSetValue(pageFormName + tagCardTaskType, card_task_type);
	}

	String card_task_state = Bean.getDecodeParam(parameters.get("card_task_state"));
	if (card_task_state==null) {
		card_task_state = Bean.filtersHmGetValue(pageFormName + tagCardTaskState);
	} else if  ("".equalsIgnoreCase(card_task_state)) {
		card_task_state = "";
		Bean.filtersHmSetValue(pageFormName + tagCardTaskState, card_task_state);
	} else {
		Bean.filtersHmSetValue(pageFormName + tagCardTaskState, card_task_state);
	}

	String l_card_page = Bean.getDecodeParam(parameters.get("card_page"));
	Bean.pageCheck(pageFormName + tagBonCards, l_card_page);
	String l_card_page_beg = Bean.getFirstRowNumber(pageFormName + tagBonCards);
	String l_card_page_end = Bean.getLastRowNumber(pageFormName + tagBonCards);

	String card_find 	= Bean.getDecodeParam(parameters.get("card_find"));
	card_find 	= Bean.checkFindString(pageFormName + tagBonCardsFind, card_find, l_card_page);

%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_EVENT_INFO")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/club_event/clubeventupdate.jsp?id=" + action.getValue("ID_CLUB_EVENT") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/club_event/clubeventupdate.jsp?id=" + action.getValue("ID_CLUB_EVENT") + "&type=general&action=remove&process=yes", Bean.club_actionXML.getfieldTransl("h_delete_action", false), action.getValue("ID_CLUB_EVENT") + " - " + action.getValue("NAME_CLUB_EVENT")) %>
			<%  } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_GIFTS")) { %>
				<% 
				if (l_order_column==null || "".equalsIgnoreCase(l_order_column)) {
					l_order_column = "2";
				}
				if (l_order_type==null || "".equalsIgnoreCase(l_order_type)) {
					l_order_type = "asc";
				}
				%>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_EVENT_GIFTS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club_event/clubeventupdate.jsp?id=" + id + "&type=gifts&action=add&process=no", "", "") %>
				    <%= Bean.getMenuButton("ADD_ALL", "../crm/club_event/clubeventupdate.jsp?type=gifts&id="+id+"&action=addall&process=no&gifts_page=1&gifts_find=", "", "") %>
				<% } %>
	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagGifts, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_EVENT_GIFTS")+"&col="+l_order_column+"&order="+l_order_type+"&", "gifts_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_GIVEN_SCHEDULE"))	{ %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_EVENT_GIVEN_SCHEDULE")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club_event/clubeventupdate.jsp?id=" + id + "&type=given_schedule&action=add&process=no", "", "") %>
				<% } %>
	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagGivenSchedule, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_EVENT_GIVEN_SCHEDULE")+"&", "given_schedule_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_REQUESTS"))	{ %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_EVENT_REQUESTS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club_event/clubeventupdate.jsp?id=" + id + "&type=request&action=add&process=no", "", "") %>
				<% } %>
	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRequest, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_EVENT_REQUESTS")+"&", "request_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_WINNERS"))	{ %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_EVENT_WINNERS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club_event/clubeventupdate.jsp?id=" + id + "&type=winner&action=add&process=no", "", "") %>
				<% } %>
				
	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagWinners, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_EVENT_WINNERS")+"&", "winners_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_ESTIMATE")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_EVENT_ESTIMATE")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/club_event/clubeventupdate.jsp?id=" + id + "&type=estimate&action=add&process=no", "", "") %>
				<% } %>
	
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagEstimate, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_EVENT_ESTIMATE")+"&", "estimate_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_MESSAGES")) { %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMessages, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_EVENT_ESTIMATE")+"&", "message_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_BON_CARDS_TASKS")) { %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagCardTask, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_EVENT_BON_CARDS_TASKS")+"&", "card_task_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_BON_CARDS")) { %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagBonCards, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CLUB_EVENT_EVENT_BON_CARDS")+"&", "card_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(action.getValue("ID_CLUB_EVENT") + " - " + action.getValue("NAME_CLUB_EVENT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/club_event/clubeventspecs.jsp?id=" + id) %>
			</td>
	
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%

if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CLUB_EVENT_EVENT_INFO")) {
%>
		<% 
			String event_type_up_categories_checked = "";
			String event_type_put_gifts_checked = "";
			String event_type_add_goods_checked = "";
			if ("UP_CATEGORIES".equalsIgnoreCase(action.getValue("CD_CLUB_EVENT_EVENT_TYPE"))) {
				event_type_up_categories_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_up_categories");
				</script>
				<%
			} else if ("PUT_GIFTS".equalsIgnoreCase(action.getValue("CD_CLUB_EVENT_EVENT_TYPE"))) {
				event_type_put_gifts_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_put_gifts");
				</script>
				<%
			} else if ("ADD_GOODS".equalsIgnoreCase(action.getValue("CD_CLUB_EVENT_EVENT_TYPE"))) {
				event_type_add_goods_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_add_goods");
				</script>
				<%
			}
		%>

	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_club_event", false) %> </td><td><input type="text" name="id_club_event" size="20" value="<%= action.getValue("ID_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(action.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(action.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
        <tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event_type", false) %></td> <td><input type="text" name="name_club_event_type" size="64" value="<%= action.getValue("NAME_CLUB_EVENT_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2"><b><%= Bean.club_actionXML.getfieldTransl("event_types", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %> </td><td><input type="text" name="name_club_event" size="64" value="<%= action.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2"><input type="radio" name="event_type" id="event_type_up_categories" value="UP_CATEGORIES" class="inputfield" disabled <%=event_type_up_categories_checked %>><b><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories", false) %></b></td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.club_actionXML.getfieldTransl("desc_action_club", false) %></td> <td rowspan="3"><textarea name="desc_action_club" cols="60" rows="3" readonly="readonly" class="inputfield-ro"><%= action.getValue("DESC_ACTION_CLUB") %></textarea> </td>
			<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="event_type_up_categories_bon" id="event_type_up_categories_bon" disabled value="Y" <%if ("Y".equalsIgnoreCase(action.getValue("EVENT_TYPE_UP_BON_CATEGORY"))) { %>CHECKED<%} %> class="inputfield"><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories_bon", false) %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="event_type_up_categories_disc" id="event_type_up_categories_disc" disabled value="Y" <%if ("Y".equalsIgnoreCase(action.getValue("EVENT_TYPE_UP_DISC_CATEGORY"))) { %>CHECKED<%} %>  class="inputfield"><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories_disc", false) %></td>
		</tr>
		<tr>
			<td colspan="2"><input type="radio" name="event_type" id="event_type_put_gifts" value="PUT_GIFTS" class="inputfield" disabled <%=event_type_put_gifts_checked %>><b><%= Bean.club_actionXML.getfieldTransl("event_type_put_gifts", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event_state", false) %></td> <td><input type="text" name="name_club_event_state" size="64" value="<%= action.getValue("NAME_CLUB_EVENT_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_put_gifts" id="event_type_put_gifts_none" disabled value="NONE" <%if ("NONE".equalsIgnoreCase(action.getValue("EVENT_TYPE_PUT_GIFT_TYPE"))) { %>CHECKED<%} %> class="inputfield"><i><%= Bean.club_actionXML.getfieldTransl("event_type_put_gifts_none", false) %></i></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_beg", false) %> </td><td><input type="text" name="date_beg" size="20" value="<%= action.getValue("DATE_BEG_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_put_gifts" id="event_type_put_gifts_write_off_bons" disabled value="WRITE_OFF_BONS" <%if ("WRITE_OFF_BONS".equalsIgnoreCase(action.getValue("EVENT_TYPE_PUT_GIFT_TYPE"))) { %>CHECKED<%} %> class="inputfield"><%= Bean.club_actionXML.getfieldTransl("event_type_put_gifts_write_off_bons", false) %></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_end", false) %> </td><td><input type="text" name="date_end" size="20" value="<%= action.getValue("DATE_END_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_put_gifts" id="event_type_put_gifts_write_off_purse" disabled value="WRITE_OFF_GOODS_FROM_PURSE" <%if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(action.getValue("EVENT_TYPE_PUT_GIFT_TYPE"))) { %>CHECKED<%} %> class="inputfield"><%= Bean.club_actionXML.getfieldTransl("event_type_put_gifts_write_off_purse", false) %>&nbsp;<input type="text" name="event_type_put_gifts_purse_type" size="20" value="<%= Bean.getClubCardPurseTypeName(action.getValue("EVENT_TYPE_PUT_GIFT_PURSE_TP")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td colspan="2"><input type="radio" name="event_type" id="event_type_add_goods" value="ADD_GOODS" class="inputfield" disabled <%=event_type_add_goods_checked %>><b><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods", false) %></b></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_add_goods_value" id="event_type_add_goods_bons" disabled value="ADD_BONS" <%if ("ADD_BONS".equalsIgnoreCase(action.getValue("EVENT_TYPE_ADD_GOODS_TYPE"))) { %>CHECKED<%} %> class="inputfield"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods_bons", false) %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_add_goods_value" id="event_type_add_goods_purse" disabled value="ADD_GOODS_TO_PURSE" <%if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(action.getValue("EVENT_TYPE_ADD_GOODS_TYPE"))) { %>CHECKED<%} %> class="inputfield"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods_purse", false) %>&nbsp;<input type="text" name="event_type_add_goods_purse_type" size="20" value="<%= Bean.getClubCardPurseTypeName(action.getValue("EVENT_TYPE_ADD_GOODS_PURSE_TP")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				action.getValue(Bean.getCreationDateFieldName()),
				action.getValue("CREATED_BY"),
				action.getValue(Bean.getLastUpdateDateFieldName()),
				action.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/club_event/clubevent.jsp") %>
			</td>
		</tr>
	</table>
	</form>
<%  } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CLUB_EVENT_EVENT_INFO")) { %>
	<script>
		var formData = new Array (
			new Array ('cd_club_event_type', 'varchar2', 1),
			new Array ('name_club_event', 'varchar2', 1),
			new Array ('cd_club_event_state', 'varchar2', 1),
			new Array ('date_beg', 'varchar2', 1)
		);
		function check_event(elem_id) {
			document.getElementById("event_type_up_categories_bon").disabled=true;
			document.getElementById("event_type_up_categories_disc").disabled=true;
			document.getElementById("event_type_put_gifts_none").disabled=true;
			document.getElementById("event_type_put_gifts_write_off_bons").disabled=true;
			document.getElementById("event_type_put_gifts_write_off_purse").disabled=true;
			document.getElementById("event_type_put_gifts_purse_type").disabled=true;
			document.getElementById("event_type_add_goods_bons").disabled=true;
			document.getElementById("event_type_add_goods_purse").disabled=true;
			document.getElementById("event_type_add_goods_purse_type").disabled=true;
			if (elem_id=="event_type_up_categories") {
				document.getElementById("event_type_up_categories_bon").disabled=false;
				document.getElementById("event_type_up_categories_disc").disabled=false;
			}
			if (elem_id=="event_type_put_gifts") {
				document.getElementById("event_type_put_gifts_none").disabled=false;
				document.getElementById("event_type_put_gifts_write_off_bons").disabled=false;
				document.getElementById("event_type_put_gifts_write_off_purse").disabled=false;
				document.getElementById("event_type_put_gifts_purse_type").disabled=false;
			}
			if (elem_id=="event_type_add_goods") {
				document.getElementById("event_type_add_goods_bons").disabled=false;
				document.getElementById("event_type_add_goods_purse").disabled=false;
				document.getElementById("event_type_add_goods_purse_type").disabled=false;
			}
		}
	</script>
		<% 
			String event_type_up_categories_checked = "";
			String event_type_put_gifts_checked = "";
			String event_type_add_goods_checked = "";
			if ("UP_CATEGORIES".equalsIgnoreCase(action.getValue("CD_CLUB_EVENT_EVENT_TYPE"))) {
				event_type_up_categories_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_up_categories");
				</script>
				<%
			} else if ("PUT_GIFTS".equalsIgnoreCase(action.getValue("CD_CLUB_EVENT_EVENT_TYPE"))) {
				event_type_put_gifts_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_put_gifts");
				</script>
				<%
			} else if ("ADD_GOODS".equalsIgnoreCase(action.getValue("CD_CLUB_EVENT_EVENT_TYPE"))) {
				event_type_add_goods_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_add_goods");
				</script>
				<%
			}
		%>

    <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%= id %>">
        <input type="hidden" name="LUD" value="<%= action.getValue("LUD") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_club_event", false) %> </td><td><input type="text" name="id_club_event" size="20" value="<%= action.getValue("ID_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(action.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(action.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
        <tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event_type", true) %></td> <td><select name="cd_club_event_type"  class="inputfield"><%= Bean.getClubActionTypeOptions(action.getValue("CD_CLUB_EVENT_TYPE"), false) %></select> </td>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event_state", true) %></td> <td><select name="cd_club_event_state"  class="inputfield"><%= Bean.getClubActionStateOptions(action.getValue("CD_CLUB_EVENT_STATE"), false) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", true) %> </td><td><input type="text" name="name_club_event" size="64" value="<%= action.getValue("NAME_CLUB_EVENT") %>" class="inputfield"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("date_beg", true) %> </td><td><%=Bean.getCalendarInputField("date_beg", action.getValue("DATE_BEG_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("desc_action_club", false) %></td> <td><textarea name="desc_action_club" cols="60" rows="3" class="inputfield"><%= action.getValue("DESC_ACTION_CLUB") %></textarea> </td>
			<td><%= Bean.club_actionXML.getfieldTransl("date_end", false) %> </td><td><%=Bean.getCalendarInputField("date_end", action.getValue("DATE_END_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				action.getValue(Bean.getCreationDateFieldName()),
				action.getValue("CREATED_BY"),
				action.getValue(Bean.getLastUpdateDateFieldName()),
				action.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/clubevent.jsp") %>
			</td>
		</tr>
	</table>

	</form>   
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>
 <% 
}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_GIVEN_SCHEDULE")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("given_schedule_find", given_schedule_find, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&given_schedule_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("given_schedule_day", "../crm/club_event/clubeventspecs.jsp?id=" + id + "&given_schedule_page=1", Bean.club_actionXML.getfieldTransl("cd_given_day", true)) %>
				<%=Bean.getMeaningFromLookupNameOrderByNymberValueOptions("DAY_NAME", given_schedule_day, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= action.getClubActionGivenScheduleHTML(given_schedule_find, given_schedule_day, l_given_schedule_page_beg, l_given_schedule_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_GIFTS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("gifts_find", gifts_find, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&gifts_page=1") %>
			<td>&nbsp;</td>
		</tr>
	</table>
	<%= action.getClubActionGiftsHTML(gifts_find, l_gifts_page_beg, l_gifts_page_end, l_order_column, l_order_type) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_WINNERS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("winner_find", winner_find, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&winners_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("winner_state", "../crm/club_event/clubeventspecs.jsp?id=" + id + "&winners_page=1", Bean.club_actionXML.getfieldTransl("NAME_NAT_PRS_winner_state", false)) %>
			<%=Bean.getNatPrsGiftStateOptions(winner_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	<%= action.getClubActionWinnersHTML(winner_find, winner_state, l_winners_page_beg, l_winners_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_REQUESTS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("request_find", request_find, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&request_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("request_type", "../crm/club_event/clubeventspecs.jsp?id=" + id + "&request_page=1", Bean.club_actionXML.getfieldTransl("name_nat_prs_gift_request_type", false)) %>
			<%= Bean.getNatPrsGiftRequestTypeOptions(request_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("request_state", "../crm/club_event/clubeventspecs.jsp?id=" + id + "&request_page=1", Bean.club_actionXML.getfieldTransl("nm_nat_prs_gift_request_state", false)) %>
			<%= Bean.getNatPrsGiftRequestStateOptions(request_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	<%= action.getClubActionRequestsHTML(request_type, request_state, request_find, l_request_page_beg, l_request_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_ESTIMATE")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("estimate_find", estimate_find, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&estimate_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("estimate_criterion", "../crm/club_event/clubeventspecs.jsp?id=" + id + "&estimate_page=1", Bean.club_actionXML.getfieldTransl("id_club_event_estim_crit", true)) %>
				<%=Bean.getEstimateCriterionsOptions(estimate_criterion, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= action.getClubActionEstimateHTML(estimate_find, estimate_criterion, l_estimate_page_beg, l_estimate_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_MESSAGES")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("message_find", message_find, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&message_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("message_type", "../crm/club_event/clubeventspecs.jsp?id=" + id + "&message_page=1", Bean.messageXML.getfieldTransl("type_cl_pattern", false)) %>
			<%= Bean.getMessagePatternTypeWitoutTerminals(message_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%= action.getMessagesHTML(message_find, message_type, l_message_page_beg, l_message_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_BON_CARDS_TASKS")) {%>
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("card_task_find", card_task_find, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&card_task_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("card_task_type", "../crm/club_event/clubeventspecs.jsp?id=" + id + "&card_task_page=1", Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)) %>
			<%= Bean.getClubCardOperationTypeOptions(card_task_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("card_task_state", "../crm/club_event/clubeventspecs.jsp?id=" + id + "&card_task_page=1", Bean.card_taskXML.getfieldTransl("cd_card_oper_state", false)) %>
			<%= Bean.getClubCardOperationStateOptions(card_task_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>
	<%= action.getClubCardsTasksHTML(card_task_find, card_task_type, card_task_state, l_card_task_page_beg, l_card_task_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CLUB_EVENT_EVENT_BON_CARDS")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("card_find", card_find, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&card_page=1") %>
	
			<td>&nbsp;</td>
		</tr>
	</table>
	<%= action.getClubActionBonCardsHTML(card_find, l_card_page_beg, l_card_page_end) %>
<%}


%>

<%   } %>
</div></div>
</body>
</html>
