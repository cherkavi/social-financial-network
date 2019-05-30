<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>

<%@page import="bc.objects.bcLGServicePlaceObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_CLIENTS_DELIVERY_POINTS";

String tagPlan = "_PLAN";
String tagPlanFind = "_PLAN_FIND";
String tagSchedule = "_SCHEDULE";
String tagScheduleFind = "_SCHEDULE_FIND";
String tagScheduleGiveState = "_SCHEDULE_GIVE_STATE";
String tagQuestionnaire = "_QUESTIONNAIRE";
String tagQuestionnaireFind = "_QUESTIONNAIRE_FIND";



Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String id_club = Bean.getDecodeParam(parameters.get("id_club"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName);
}

if (id==null || "".equals(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% } else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcLGServicePlaceObject plan = new bcLGServicePlaceObject(id, id_club);

	String l_plan_page = Bean.getDecodeParam(parameters.get("plan_page"));
	Bean.pageCheck(pageFormName + tagPlan, l_plan_page);
	String l_plan_page_beg = Bean.getFirstRowNumber(pageFormName + tagPlan);
	String l_plan_page_end = Bean.getLastRowNumber(pageFormName + tagPlan);

	String plan_find 	= Bean.getDecodeParam(parameters.get("plan_find"));
	plan_find 	= Bean.checkFindString(pageFormName + tagPlanFind, plan_find, l_plan_page);

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
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_INFO")) {%>
				<%= Bean.getMenuButton("ADD", "../crm/logistic/clients/delivery_pointupdate.jsp?id=" + id + "&id_club=" + id_club + "&type=general&action=add2&process=no", "", "") %>
				<%= Bean.getMenuButton("DELETE", "../crm/logistic/clients/delivery_pointupdate.jsp?id=" + id + "&id_club=" + id_club + "&type=general&action=remove&process=yes", Bean.logisticXML.getfieldTransl("h_action_bon_card_delete", false), plan.getValue("CD_CLUB_SERVICE_PLACE") + " - " +  plan.getValue("NAME_SERVICE_PLACE")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_PLAN_LINES")) {%>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_PLAN_LINES")) {%>
					<%= Bean.getMenuButton("ADD", "../crm/logistic/clients/delivery_pointupdate.jsp?id=" + id + "&id_club=" + id_club + "&type=plan&action=add&process=no", "", "") %>
				<% } %>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPlan, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_PROMOTERS_SCHEDULE") + "&", "plan_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_BON_CARD_GIVE")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagSchedule, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_DELIVERY_POINTS_BON_CARD_GIVE") + "&", "schedule_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_QUESTIONNAIRES")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagQuestionnaire, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_DELIVERY_POINTS_QUESTIONNAIRES") + "&", "questionnaire_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(plan.getValue("CD_CLUB_SERVICE_PLACE") + " - " +  plan.getValue("NAME_SERVICE_PLACE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_INFO")) {
	boolean hasInfoEditPermission = false;
	if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_INFO")) {
		hasInfoEditPermission = true;
	}
%>
	<% if (hasInfoEditPermission) { %>
		<script language="JavaScript">
			var formData = new Array (
				new Array ('cd_club_service_place', 'varchar2', 1)
			);
			</script>

		  <form action="../crm/logistic/clients/delivery_pointupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id" value="<%= id %>">
	    	<input type="hidden" name="id_club" value="<%= id_club %>">
	<% } %>
		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("id_service_place", false) %>
				<%= Bean.getGoToJurPrsHyperLink(plan.getValue("ID_SERVICE_PLACE")) %>
			</td> <td><input type="text" name="id_service_place" size="60" value="<%=plan.getValue("ID_SERVICE_PLACE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(plan.getValue("ID_CLUB")) %>
			</td> <td><input type="text" name="name_club" size="35" value="<%=Bean.getClubShortName(plan.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("NAME_SERVICE_PLACE", false) %></td> <td><input type="text" name="NAME_SERVICE_PLACE" size="60" value="<%=plan.getValue("NAME_SERVICE_PLACE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club_member_status", false) %></td> <td><input type="text" name="name_club_member_status" size="35" value="<%=plan.getValue("NAME_CLUB_MEMBER_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("id_service_place_type", false) %></td> <td><input type="text" name="id_service_place_type" size="60" value="<%=plan.getValue("NAME_SERVICE_PLACE_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_beg", false) %></td> <td><input type="text" name="club_date_beg" size="10" value="<%=plan.getValue("DATE_BEG_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("id_jur_prs", false) %>
				<%=Bean.getGoToJurPrsHyperLink(plan.getValue("ID_JUR_PRS")) %>
			</td> <td><input type="text" name="name_jur_prs" size="60" value="<%=plan.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%= Bean.jurpersonXML.getfieldTransl("club_date_end", false) %></td> <td><input type="text" name="club_date_end" size="10" value="<%=plan.getValue("DATE_END_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.jurpersonXML.getfieldTransl("adr_full", false) %></td> <td  colspan="3"><input type="text" name="adr_full" size="120" value="<%=plan.getValue("ADR_FULL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>								
			<td colspan=8 class="top_line"><b><%= Bean.jurpersonXML.getfieldTransl("h_logistic_param", false) %></b>
			</td>
		</tr>
		<tr>					
			<% if (hasInfoEditPermission) { %>			
			<td><%= Bean.logisticXML.getfieldTransl("cd_club_service_place", true) %></td> <td><input type="text" name="cd_club_service_place" size="20" value="<%=plan.getValue("CD_CLUB_SERVICE_PLACE") %>" class="inputfield"></td>
			<% } else { %>
			<td><%= Bean.logisticXML.getfieldTransl("cd_club_service_place", false) %></td> <td><input type="text" name="cd_club_service_place" size="20" value="<%=plan.getValue("CD_CLUB_SERVICE_PLACE") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				plan.getValue(Bean.getCreationDateFieldName()),
				plan.getValue("CREATED_BY"),
				plan.getValue(Bean.getLastUpdateDateFieldName()),
				plan.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<% if (hasInfoEditPermission) { %>
				<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/delivery_pointupdate.jsp") %>
				<% } %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/logistic/clients/delivery_point.jsp") %>
			</td>
		</tr>
	</table>
	</form>


<% }

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_PLAN_LINES")) { %> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("plan_find", plan_find, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&plan_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table> 
		<%= plan.getPlanLinesHTML(plan_find, l_plan_page_beg, l_plan_page_end) %> 
	<%}
	
	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_BON_CARD_GIVE")) {
		%> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("schedule_find", schedule_find, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&schedule_page=1") %>
	
			<%=Bean.getSelectOnChangeBeginHTML(
					"",
					"schedule_give_state", 
					"../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&schedule_page=1", 
					Bean.logisticXML.getfieldTransl("cd_club_promoter_give_state", false),
					"div_main") %>
				<%= Bean.getLogisticPromoterGiveStateOptions(schedule_give_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			</tr>
		</table>
		<%= plan.getSchedulePreviewHTML(schedule_find, schedule_give_state, l_schedule_page_beg, l_schedule_page_end) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_DELIVERY_POINTS_QUESTIONNAIRES")) { %> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("questionnaire_find", questionnaire_find, "../crm/logistic/clients/delivery_pointspecs.jsp?id=" + id + "&id_club=" + id_club + "&questionnaire_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table> 
		<%= plan.getQuestionnairesHTML(questionnaire_find, l_questionnaire_page_beg, l_questionnaire_page_end) %> 
	<%}

}
		
%>
</div></div>
</body>

<%@page import="bc.objects.bcLGPromoterObject"%></html>
