<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_CLIENTS_PROMOTERS";

String tagWorkHistory = "_WORK_HISTORY";
String tagWorkHistoryFind = "_WORK_HISTORY_FIND";
String tagSchedule = "_SCHEDULE";
String tagScheduleFind = "_SCHEDULE_FIND";
String tagScheduleGiveState = "_SCHEDULE_GIVE_STATE";
String tagQuestionnaire = "_QUESTIONNAIRE";
String tagQuestionnaireFind = "_QUESTIONNAIRE_FIND";
String tagSubordinate = "_SUBORDINATE";
String tagSubordinateFind = "_SUBORDINATE_FIND";
String tagSubordinatePost = "_SUBORDINATE_POST";
String tagSubordinateState = "_SUBORDINATE_STATE";
String tagDeliveryPoint = "_DELIVERY_POINT";
String tagDeliveryPointFind = "_DELIVERY_POINT_FIND";
String tagPenalty = "_PENALTY";
String tagPenaltyFind = "_PENALTY_FIND";
String tagPayment = "_PAYMENT";
String tagPaymentFind = "_PAYMENT_FIND";


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
	bcLGPromoterObject promoter = new bcLGPromoterObject(id);
	
	Bean.currentMenu.setExistFlag("LOGISTIC_CLIENTS_PROMOTERS_SOBORDINATES", true);
	Bean.currentMenu.setExistFlag("'LOGISTIC_CLIENTS_PROMOTERS_DELIVERY_POINTS", true);
	
	if (!("SUPERVISOR".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST")) ||
			"CHIEF".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST")) ||
			"SENIOR_TELLER".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST")))) {
		Bean.currentMenu.setExistFlag("LOGISTIC_CLIENTS_PROMOTERS_SOBORDINATES", false);
		if (Bean.currentMenu.isCurrentTab("LOGISTIC_CLIENTS_PROMOTERS_SOBORDINATES")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	}
	
	if (!("SUPERVISOR".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST")))) {
		Bean.currentMenu.setExistFlag("LOGISTIC_CLIENTS_PROMOTERS_DELIVERY_POINTS", false);
		if (Bean.currentMenu.isCurrentTab("LOGISTIC_CLIENTS_PROMOTERS_DELIVERY_POINTS")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	}

	String l_work_page = Bean.getDecodeParam(parameters.get("work_page"));
	Bean.pageCheck(pageFormName + tagWorkHistory, l_work_page);
	String l_work_page_beg = Bean.getFirstRowNumber(pageFormName + tagWorkHistory);
	String l_work_page_end = Bean.getLastRowNumber(pageFormName + tagWorkHistory);

	String work_find 	= Bean.getDecodeParam(parameters.get("work_find"));
	work_find 	= Bean.checkFindString(pageFormName + tagWorkHistoryFind, work_find, l_work_page);

	String l_schedule_page = Bean.getDecodeParam(parameters.get("schedule_page"));
	Bean.pageCheck(pageFormName + tagSchedule, l_schedule_page);
	String l_schedule_page_beg = Bean.getFirstRowNumber(pageFormName + tagSchedule);
	String l_schedule_page_end = Bean.getLastRowNumber(pageFormName + tagSchedule);

	String	schedule_give_state = Bean.getDecodeParam(parameters.get("schedule_give_state"));
	schedule_give_state 	= Bean.checkFindString(pageFormName + tagScheduleGiveState, schedule_give_state, l_schedule_page);

	String schedule_find 	= Bean.getDecodeParam(parameters.get("schedule_find"));
	schedule_find 	= Bean.checkFindString(pageFormName + tagScheduleFind, schedule_find, l_schedule_page);

	String l_questionnaire_page = Bean.getDecodeParam(parameters.get("questionnaire_page"));
	Bean.pageCheck(pageFormName + tagQuestionnaire, l_questionnaire_page);
	String l_questionnaire_page_beg = Bean.getFirstRowNumber(pageFormName + tagQuestionnaire);
	String l_questionnaire_page_end = Bean.getLastRowNumber(pageFormName + tagQuestionnaire);

	String questionnaire_find 	= Bean.getDecodeParam(parameters.get("questionnaire_find"));
	questionnaire_find 	= Bean.checkFindString(pageFormName + tagQuestionnaireFind, questionnaire_find, l_questionnaire_page);

	String l_subordinate_page = Bean.getDecodeParam(parameters.get("subordinate_page"));
	Bean.pageCheck(pageFormName + tagSubordinate, l_subordinate_page);
	String l_subordinate_page_beg = Bean.getFirstRowNumber(pageFormName + tagSubordinate);
	String l_subordinate_page_end = Bean.getLastRowNumber(pageFormName + tagSubordinate);

	String subordinate_find 	= Bean.getDecodeParam(parameters.get("subordinate_find"));
	subordinate_find 	= Bean.checkFindString(pageFormName + tagSubordinateFind, subordinate_find, l_subordinate_page);

	String subordinate_post 	= Bean.getDecodeParam(parameters.get("subordinate_post"));
	subordinate_post 	= Bean.checkFindString(pageFormName + tagSubordinatePost, subordinate_post, l_subordinate_page);

	String subordinate_state 	= Bean.getDecodeParam(parameters.get("subordinate_state"));
	subordinate_state 	= Bean.checkFindString(pageFormName + tagSubordinateState, subordinate_state, l_subordinate_page);

	String l_delivery_point_page = Bean.getDecodeParam(parameters.get("delivery_point_page"));
	Bean.pageCheck(pageFormName + tagDeliveryPoint, l_delivery_point_page);
	String l_delivery_point_page_beg = Bean.getFirstRowNumber(pageFormName + tagDeliveryPoint);
	String l_delivery_point_page_end = Bean.getLastRowNumber(pageFormName + tagDeliveryPoint);

	String delivery_point_find 	= Bean.getDecodeParam(parameters.get("delivery_point_find"));
	delivery_point_find 	= Bean.checkFindString(pageFormName + tagDeliveryPointFind, delivery_point_find, l_delivery_point_page);

	String l_penalty_page = Bean.getDecodeParam(parameters.get("penalty_page"));
	Bean.pageCheck(pageFormName + tagPenalty, l_penalty_page);
	String l_penalty_page_beg = Bean.getFirstRowNumber(pageFormName + tagPenalty);
	String l_penalty_page_end = Bean.getLastRowNumber(pageFormName + tagPenalty);

	String penalty_find 	= Bean.getDecodeParam(parameters.get("penalty_find"));
	penalty_find 	= Bean.checkFindString(pageFormName + tagPenaltyFind, penalty_find, l_penalty_page);

	String l_payment_page = Bean.getDecodeParam(parameters.get("payment_page"));
	Bean.pageCheck(pageFormName + tagPayment, l_payment_page);
	String l_payment_page_beg = Bean.getFirstRowNumber(pageFormName + tagPayment);
	String l_payment_page_end = Bean.getLastRowNumber(pageFormName + tagPayment);

	String payment_find 	= Bean.getDecodeParam(parameters.get("payment_find"));
	payment_find 	= Bean.checkFindString(pageFormName + tagPaymentFind, payment_find, l_payment_page);
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_PROMOTERS_INFO")) {%>
				<%= Bean.getMenuButton("ADD", "../crm/logistic/clients/promoterupdate.jsp?id=" + id + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/logistic/clients/promoterupdate.jsp?id=" + id + "&type=general&action=remove&process=yes", Bean.logisticXML.getfieldTransl("h_action_bon_card_delete", false), promoter.getValue("ID_LG_PROMOTER") + " - " +  promoter.getValue("NAME_LG_PROMOTER")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_WORK_HISTORY")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagWorkHistory, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PROMOTERS_WORK_HISTORY") + "&", "work_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_SCHEDULE")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSchedule, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PROMOTERS_SCHEDULE") + "&", "schedule_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_QUESTIONNAIRES")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagQuestionnaire, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PROMOTERS_QUESTIONNAIRES") + "&", "questionnaire_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_SOBORDINATES")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSubordinate, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PROMOTERS_SOBORDINATES") + "&", "subordinate_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_DELIVERY_POINTS")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagDeliveryPoint, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PROMOTERS_DELIVERY_POINTS") + "&", "delivery_point_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_PAYMENTS")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPayment, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PROMOTERS_PAYMENTS") + "&", "payment_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_PENALTIES")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPenalty, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PROMOTERS_PENALTIES") + "&", "penalty_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(promoter.getValue("ID_LG_PROMOTER") + " - " +  promoter.getValue("NAME_LG_PROMOTER")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&schedule_give_state="+schedule_give_state) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_PROMOTERS_INFO")) {
%>
	
		<script language="JavaScript">
			var formData = new Array (
				new Array ('cd_lg_promoter', 'varchar2', 1),
				new Array ('name_lg_promoter', 'varchar2', 1)
			);
			</script>

		  <form action="../crm/logistic/clients/promoterupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">

		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_promoter", false) %></td><td><input type="text" name="id_lg_promoter" size="20" value="<%=promoter.getValue("ID_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(promoter.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(promoter.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
 		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", true) %></td><td><input type="text" name="name_lg_promoter" size="60" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" class="inputfield"></td>
			<td rowspan="2"><%= Bean.logisticXML.getfieldTransl("desc_lg_promoter", false) %></td><td rowspan="2"><textarea name="desc_lg_promoter" cols="46" rows="2" class="inputfield"><%= promoter.getValue("DESC_LG_PROMOTER") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="20" value="<%=promoter.getValue("PHONE_MOBILE") %>" class="inputfield"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				promoter.getValue(Bean.getCreationDateFieldName()),
				promoter.getValue("CREATED_BY"),
				promoter.getValue(Bean.getLastUpdateDateFieldName()),
				promoter.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/promoterupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/logistic/clients/promoters.jsp") %>
			</td>
		</tr>
		<tr>
			<td class="top_line" colspan="4">
				<b><%= Bean.logisticXML.getfieldTransl("h_promoter_work_param", false) %></b>
			</td>
		</tr>
 		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter", false) %></td><td><input type="text" name="cd_lg_promoter" size="20" value="<%=promoter.getValue("CD_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_state", false) %></td><td><input type="text" name="cd_lg_promoter_state" size="20" value="<%=promoter.getValue("NAME_LG_PROMOTER_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
 		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("jur_prs", false) %>
					<%=Bean.getGoToJurPrsHyperLink(promoter.getValue("ID_JUR_PRS")) %>
			</td><td><input type="text" name="sname_jur_prs" size="50" value="<%=promoter.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("date_begin_work", false) %></td><td><input type="text" name="date_begin_work" size="20" value="<%= promoter.getValue("DATE_BEGIN_WORK_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
 		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("service_place", false) %>
				<%= Bean.getGoToJurPrsHyperLink(promoter.getValue("ID_SERVICE_PLACE")) %>
			</td><td><input type="text" name="name_service_place" size="50" value="<%=promoter.getValue("NAME_SERVICE_PLACE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("date_end_work", false) %></td><td><input type="text" name="date_end_work" size="20" value="<%= promoter.getValue("DATE_END_WORK_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
 		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false) %></td><td><input type="text" name="cd_lg_promoter_post" size="50" value="<%=promoter.getValue("NAME_LG_PROMOTER_POST") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
 		<tr>
			<td>&nbsp;</td>
			<td  colspan="3">
				<% if ("DISMISSED".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_STATE"))) { %>
				<button type="button" class="button" onclick="ajaxpage('<%="../crm/logistic/clients/promoterupdate.jsp?id=" + id + "&type=general&action=accept&process=no" %>', 'div_main')"><%= Bean.logisticXML.getfieldTransl("h_button_work_place_accept", false) %> </button>
				&nbsp;
				<% } %>
				<% if ("ACCEPTED".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_STATE")) ||
						"TRANSFERRED".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_STATE"))) { %>
				<button type="button" class="button" onclick="ajaxpage('<%="../crm/logistic/clients/promoterupdate.jsp?id=" + id + "&type=general&action=transfer&process=no" %>', 'div_main')"><%= Bean.logisticXML.getfieldTransl("h_button_work_place_transfer", false) %> </button>
				&nbsp;
				<button type="button" class="button" onclick="ajaxpage('<%="../crm/logistic/clients/promoterupdate.jsp?id=" + id + "&type=general&action=dismiss&process=no" %>', 'div_main')"><%= Bean.logisticXML.getfieldTransl("h_button_work_place_dismiss", false) %> </button>
				<% } %>
			</td>
		</tr>
		<% if ("PROMOTER".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST")) ||
				"CASHIER".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST")) ||
				"GAS_STATION_OPERATOR".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST")) ||
				"SELLER".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST"))) { %>
		<tr>
			<td class="top_line"  colspan="3">
				<% if ("PROMOTER".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST"))) { %>
				<b><%= Bean.logisticXML.getfieldTransl("h_supervisor_param", false) %></b>
				<% } else { %>
				<b><%= Bean.logisticXML.getfieldTransl("h_manager_param", false) %></b>
				<% } %>
			</td>
			<td class="top_line" align="right">
				<button type="button" class="button" onclick="ajaxpage('<%="../crm/logistic/clients/promoterupdate.jsp?id=" + id + "&id_lg_promoter_work=" + promoter.getValue("ID_LG_PROMOTER_WORK_CURRENT") + "&type=supervisor&action=add&process=no" %>', 'div_main')"><%= Bean.buttonXML.getfieldTransl("button_add", false) %> </button>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<%=promoter.getManagerHistoryHTML("", "1", "50") %>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				&nbsp;
			</td>
		</tr>
		<% } %>
		<% if (!("CHIEF".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST")) ||
				"SENIOR_TELLER".equalsIgnoreCase(promoter.getValue("CD_LG_PROMOTER_POST")))) { %>
		<tr>
			<td class="top_line"  colspan="3">
				<b><%= Bean.logisticXML.getfieldTransl("h_promoter_payment_param", false) %></b>
			</td>
			<td class="top_line" align="right">
				<button type="button" class="button" onclick="ajaxpage('<%="../crm/logistic/clients/promoterupdate.jsp?id=" + id + "&id_lg_promoter_work=" + promoter.getValue("ID_LG_PROMOTER_WORK_CURRENT") + "&type=pay_param&action=add&process=no" %>', 'div_main')"><%= Bean.buttonXML.getfieldTransl("button_add", false) %> </button>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<%=promoter.getPaymentParamHistoryHTML("", "1", "50") %>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				&nbsp;
			</td>
		</tr>
		<% } %>
		<tr>
			<td class="top_line"  colspan="3">
				<b><%= Bean.logisticXML.getfieldTransl("h_promoter_payment_card", false) %></b>
			</td>
			<td class="top_line" align="right">
				<button type="button" class="button" onclick="ajaxpage('<%="../crm/logistic/clients/promoterupdate.jsp?id=" + id + "&type=general&action=change_state&process=no" %>', 'div_main')"><%= Bean.buttonXML.getfieldTransl("button_add", false) %> </button>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<%=promoter.getPaymentCardHistoryHTML("", "1", "50") %>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				&nbsp;
			</td>
		</tr>
	</table>
	</form>


<% } else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("LOGISTIC_CLIENTS_PROMOTERS_INFO")) {
	%>
	
	  <form>
	<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_lg_promoter", false) %></td><td><input type="text" name="id_lg_promoter" size="20" value="<%=promoter.getValue("ID_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(promoter.getValue("ID_CLUB")) %>
			</td><td class="bottom_line"><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(promoter.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("jur_prs", false) %>
					<%=Bean.getGoToJurPrsHyperLink(promoter.getValue("ID_JUR_PRS")) %>
			</td><td><input type="text" name="sname_jur_prs" size="50" value="<%=promoter.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_promoter", false) %></td><td><input type="text" name="name_lg_promoter" size="60" value="<%= promoter.getValue("NAME_LG_PROMOTER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("service_place", false) %>
				<%= Bean.getGoToJurPrsHyperLink(promoter.getValue("ID_SERVICE_PLACE")) %>
			</td><td><input type="text" name="name_service_place" size="50" value="<%=promoter.getValue("NAME_SERVICE_PLACE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.logisticXML.getfieldTransl("desc_lg_promoter", false) %></td><td rowspan="3"><textarea name="desc_lg_promoter" cols="56" rows="3" readonly="readonly" class="inputfield-ro"><%= promoter.getValue("DESC_LG_PROMOTER") %></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false) %></td><td><input type="text" name="cd_lg_promoter_post" size="50" value="<%=promoter.getValue("NAME_LG_PROMOTER_POST") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_promoter_state", false) %></td><td><input type="text" name="cd_lg_promoter_state" size="50" value="<%=promoter.getValue("NAME_LG_PROMOTER_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("date_begin_work", false) %></td><td><input type="text" name="date_begin_work" size="16" value="<%= promoter.getValue("DATE_BEGIN_WORK_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="20" value="<%=promoter.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.logisticXML.getfieldTransl("date_end_work", false) %></td><td><input type="text" name="date_end_work" size="16" value="<%= promoter.getValue("DATE_END_WORK_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				promoter.getValue(Bean.getCreationDateFieldName()),
				promoter.getValue("CREATED_BY"),
				promoter.getValue(Bean.getLastUpdateDateFieldName()),
				promoter.getValue("LAST_UPDATE_BY")
			) %>
	<tr>
		<td colspan="6" align="center">
			<%=Bean.getGoBackButton("../crm/logistic/clients/promoters.jsp") %>
		</td>
	</tr>
</table>
</form>

<% }

	if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_PROMOTERS_SCHEDULE")) { %>  
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("schedule_find", schedule_find, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&schedule_page=1") %>
	
			<%=Bean.getSelectOnChangeBeginHTML(
					"",
					"schedule_give_state", 
					"../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&schedule_page=1&schedule_give_state='+getElementById('schedule_give_state').value + '", 
					Bean.logisticXML.getfieldTransl("cd_lg_promoter_give_state", false),
					"div_main") %>
				<%= Bean.getLogisticPromoterGiveStateOptions(schedule_give_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			</tr>
		</table> 
		<%= promoter.getScheduleEditHTML(schedule_find, schedule_give_state, l_schedule_page_beg, l_schedule_page_end) %> 
	<%}	else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("LOGISTIC_CLIENTS_PROMOTERS_SCHEDULE")) {
		%> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("schedule_find", schedule_find, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&schedule_page=1") %>
	
			<%=Bean.getSelectOnChangeBeginHTML(
					"",
					"schedule_give_state", 
					"../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&schedule_page=1&schedule_give_state='+getElementById('schedule_give_state').value + '", 
					Bean.logisticXML.getfieldTransl("cd_lg_promoter_give_state", false),
					"div_main") %>
				<%= Bean.getLogisticPromoterGiveStateOptions(schedule_give_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			</tr>
		</table>
		<%= promoter.getSchedulePreviewHTML(schedule_find, schedule_give_state, l_schedule_page_beg, l_schedule_page_beg) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_QUESTIONNAIRES")) { %> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("questionnaire_find", questionnaire_find, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&questionnaire_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table> 
		<%= promoter.getQuestionnairesHTML(questionnaire_find, l_questionnaire_page_beg, l_questionnaire_page_end) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_WORK_HISTORY")) { %> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("work_find", work_find, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&work_page=1") %>

		<td>&nbsp;</td>

		</tr>
	</table> 
	<%= promoter.getWorkPlacesHistoryHTML(work_find, l_work_page_beg, l_work_page_end) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_SOBORDINATES")) { %> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("subordinate_find", subordinate_find, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&subordinate_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("subordinate_post", "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&subordinate_page=1", Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false)) %>
				<%= Bean.getLogisticPromoterPostOptions(subordinate_post, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("subordinate_state", "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&subordinate_page=1", Bean.logisticXML.getfieldTransl("cd_lg_promoter_state", false)) %>
				<%= Bean.getSelectOptionHTML(subordinate_state, "", "") %>
				<%= Bean.getSelectOptionHTML(subordinate_state, "WORKS", Bean.logisticXML.getfieldTransl("cd_lg_promoter_state_work", false)) %>
				<%= Bean.getLogisticPromoterStateOptions(subordinate_state, false) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
	</table> 
	<%= promoter.getSubordinatesHistoryHTML(subordinate_find, subordinate_post, subordinate_state, l_subordinate_page_beg, l_subordinate_page_end) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_DELIVERY_POINTS")) { %> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("delivery_point_find", delivery_point_find, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&delivery_point_page=1") %>

		<td>&nbsp;</td>

		</tr>
	</table> 
	<%= promoter.getSupervisorDeliveryPointHistoryHTML(delivery_point_find, l_delivery_point_page_beg, l_delivery_point_page_end) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_PAYMENTS")) { %> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("payment_find", payment_find, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&payment_page=1") %>

		<td>&nbsp;</td>

		</tr>
	</table> 
	<%= promoter.getPromoterPaymentsHTML(payment_find, l_payment_page_beg, l_payment_page_end) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_PROMOTERS_PENALTIES")) { %> 
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("penalty_find", penalty_find, "../crm/logistic/clients/promoterspecs.jsp?id=" + id + "&penalty_page=1") %>

		<td>&nbsp;</td>

		</tr>
	</table> 
	<%= promoter.getPromoterPenaltiesHTML(penalty_find, l_penalty_page_beg, l_penalty_page_end) %> 
	<%}
}
		
%>
</div></div>
</body>

<%@page import="bc.objects.bcLGPromoterObject"%></html>
