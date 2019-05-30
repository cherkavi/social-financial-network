<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcLGClubCardGivenScheduleObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body topmargin="0">
<div id="div_tabsheet">

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_CLIENTS_OPERATION_SCHEDULE";
String tagServicePlace = "_SERVICE_PLACESES";
String tagServicePlaceFind = "_SERVICE_PLACE_FIND";
String tagPromoter = "_PROMOTER";
String tagPromoterState = "_PROMOTER_STATE";
String tagPromoterFind = "_PROMOTER_FIND";
String tagQuestionnaire = "_QUESTIONNAIRE";
String tagQuestionnaireFind = "_QUESTIONNAIRE_FIND";

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
	bcLGClubCardGivenScheduleObject schedule = new bcLGClubCardGivenScheduleObject(id);

	//Обрабатываем номера страниц
	String l_service_place_page = Bean.getDecodeParam(parameters.get("service_place_page"));
	Bean.pageCheck(pageFormName + tagServicePlace, l_service_place_page);
	String l_service_place_page_beg = Bean.getFirstRowNumber(pageFormName + tagServicePlace);
	String l_service_place_page_end = Bean.getLastRowNumber(pageFormName + tagServicePlace);

	String service_place_find 	= Bean.getDecodeParam(parameters.get("service_place_find"));
	service_place_find 	= Bean.checkFindString(pageFormName + tagServicePlaceFind, service_place_find, l_service_place_page);

	String l_promoter_page = Bean.getDecodeParam(parameters.get("promoter_page"));
	Bean.pageCheck(pageFormName + tagPromoter, l_promoter_page);
	String l_promoter_page_beg = Bean.getFirstRowNumber(pageFormName + tagPromoter);
	String l_promoter_page_end = Bean.getLastRowNumber(pageFormName + tagPromoter);

	String	promoter_state = Bean.getDecodeParam(parameters.get("promoter_state"));
	promoter_state 	= Bean.checkFindString(pageFormName + tagPromoterState, promoter_state, l_promoter_page);

	String promoter_find 	= Bean.getDecodeParam(parameters.get("promoter_find"));
	promoter_find 	= Bean.checkFindString(pageFormName + tagPromoterFind, promoter_find, l_promoter_page);

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
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_SERVICE_PLACES")) {%>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagServicePlace, "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_SERVICE_PLACES") + "&", "service_place_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_PROMOTERS")) {%>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPromoter, "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_PROMOTERS") + "&", "promoter_page") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_QUESTIONNAIRES")) {%>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagQuestionnaire, "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_QUESTIONNAIRES") + "&", "questionnaire_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(schedule.getValue("DATE_CARD_GIVEN_FRMT")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&promoter_state="+promoter_state) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_INFO")) {
		boolean hasEditPermission = false;
		if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_INFO")) {
			hasEditPermission = true;
		}
	%>
		<% if (hasEditPermission) { %>
			
			  <form action="../crm/logistic/clients/operation_scheduleupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onsubmit="return validateForm(formData);">
				<input type="hidden" name="action" value="edit">
		    	<input type="hidden" name="process" value="yes">
		    	<input type="hidden" name="type" value="general">
		    	<input type="hidden" name="id" value="<%= id %>">
		<% } %>
			<table <%=Bean.getTableDetailParam() %>>

			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("id_lg_cc_given_schedule", false) %></td><td><input type="text" name="id_lg_cc_given_schedule" size="20" value="<%=schedule.getValue("ID_LG_CC_GIVEN_SCHEDULE") %>" readonly="readonly" class="inputfield-ro"></td>
				<td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(schedule.getValue("ID_CLUB")) %>
				</td><td class="bottom_line"><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(schedule.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("date_card_given", false) %></td><td><input type="text" name="date_card_given" size="20" value="<%=schedule.getValue("DATE_CARD_GIVEN_DF") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.logisticXML.getfieldTransl("service_places_count", false) %></td><td><input type="text" name="service_places_count" size="10" value="<%= schedule.getValue("SERVICE_PLACES_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td rowspan="3"><%= Bean.logisticXML.getfieldTransl("notes", false) %></td>
				<% if (hasEditPermission) { %>
				<td rowspan="3"><textarea name="notes" cols="56" rows="3" class="inputfield"><%= schedule.getValue("NOTES") %></textarea></td>
				<% } else { %>
				<td rowspan="3"><textarea name="notes" cols="56" rows="3" readonly="readonly" class="inputfield-ro"><%= schedule.getValue("NOTES") %></textarea></td>
				<% } %>
				<td><%= Bean.logisticXML.getfieldTransl("promoters_count", false) %></td><td><input type="text" name="promoters_count" size="10" value="<%= schedule.getValue("PROMOTERS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("sales_cards_count", false) %></td><td><input type="text" name="sales_cards_count" size="10" value="<%= schedule.getValue("SALES_CARDS_COUNT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%=	Bean.getCreationAndMoficationRecordFields(
					schedule.getValue(Bean.getCreationDateFieldName()),
					schedule.getValue("CREATED_BY"),
					schedule.getValue(Bean.getLastUpdateDateFieldName()),
					schedule.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="6" align="center">
				<% if (hasEditPermission) { %>
					<%=Bean.getSubmitButtonAjax("../crm/logistic/clients/operation_scheduleupdate.jsp") %>
					<%=Bean.getResetButton() %>
				<% } %>
					<%=Bean.getGoBackButton("../crm/logistic/clients/operation_schedule.jsp") %>
				</td>
			</tr>
		</table>
		</form>


	<% }
	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_SERVICE_PLACES")) {
	%>  
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("service_place_find", service_place_find, "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&service_place_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table>
		<%= schedule.getServicePlacesHTML(service_place_find, l_service_place_page_beg, l_service_place_page_end) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndEditPermitted("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_PROMOTERS")) {
	%>
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("promoter_find", promoter_find, "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&promoter_find=1") %>
	
			<%=Bean.getSelectOnChangeBeginHTML(
					"",
					"promoter_state", 
					"../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&schedule_page=1&promoter_state='+getElementById('promoter_state').value + '", 
					Bean.logisticXML.getfieldTransl("cd_lg_promoter_state", false),
					"div_main") %>
				<%= Bean.getLogisticPromoterGiveStateOptions(promoter_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			</tr>
		</table> 
		<%= schedule.getScheduleLinesEditHTML(promoter_find, promoter_state, l_promoter_page_beg, l_promoter_page_end) %> 
	<%}	else if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_PROMOTERS")) {
		%> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("promoter_find", promoter_find, "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&promoter_find=1") %>
	
			<%=Bean.getSelectOnChangeBeginHTML(
					"",
					"promoter_state", 
					"../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&schedule_page=1&promoter_state='+getElementById('promoter_state').value + '", 
					Bean.logisticXML.getfieldTransl("cd_lg_promoter_state", false),
					"div_main") %>
				<%= Bean.getLogisticPromoterGiveStateOptions(promoter_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			</tr>
		</table>
		<%= schedule.getScheduleLinesPreviewHTML(promoter_find, promoter_state, l_promoter_page_beg, l_promoter_page_end) %> 
	<%}

	if (Bean.currentMenu.isCurrentTabAndAccessPermitted("LOGISTIC_CLIENTS_OPERATION_SCHEDULE_QUESTIONNAIRES")) {
	%> 
		<table <%= Bean.getTableBottomFilter() %>>
			<tr>
			<%= Bean.getFindHTML("questionnaire_find", questionnaire_find, "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id + "&questionnaire_page=1") %>
	
			<td>&nbsp;</td>
	
			</tr>
		</table> 
		<%= schedule.getQuestionnairesHTML(questionnaire_find, l_questionnaire_page_beg, l_questionnaire_page_end) %> 
	<%}

}
		
%>
</div></div>
</body>

</html>
