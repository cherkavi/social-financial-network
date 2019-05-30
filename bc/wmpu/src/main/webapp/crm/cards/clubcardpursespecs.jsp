<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%>

<%@page import="bc.objects.bcClubCardPurseObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
</head>

<% /* Получаем скрипты для проверки, изменены ли данные на форме*/ %>
<%= Bean.getCheckScripts() %>

<body topmargin="0"">
<div id="div_tabsheet">

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_CLUBCARDS";

Bean.setJspPageForTabName(pageFormName);

String tagPurseTaskType = "_PURSE_TASK_TYPE";
String tagPurseTaskState = "_PURSE_TASK_STATE";
String tagPurseTaskFind = "_PURSE_TASK_FIND";
String tagPurseTasks = "_PURSE_TASKS";
String tagFinOper = "_PURSE_FIN_OPER";
String tagFinOperState = "_PURSE_FIN_OPER_STATE";
String tagFinOperFind = "_PURSE_FIN_OPER_FIND";
String tagRests = "_PURSE_RESTS";

String id = Bean.getDecodeParam(parameters.get("id"));

String	tab = Bean.getDecodeParam(parameters.get("tab"));
tab = Bean.isEmpty(tab)?Bean.tabsHmGetValue(pageFormName):tab;
if (Bean.isEmpty(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcClubCardPurseObject purse = new bcClubCardPurseObject(id);
	
	String cd_card1 = Bean.getClubCardCode(purse.getValue("CARD_SERIAL_NUMBER") + "_" + purse.getValue("ID_ISSUER") + "_" + purse.getValue("ID_PAYMENT_SYSTEM"));

	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_BONAPPL", false);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_PURSES", false);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_TRANS", false);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_REQUESTS", false);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_HISTORY", false);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_BK_ACCOUNTS", false);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_POSTINGS", false);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_LOGISTIC", false);
	Bean.currentMenu.setExistFlag("CARDS_CLUBCARDS_DISCOUNT", false);
	
	if (!(Bean.currentMenu.isCurrentTab("CARDS_CLUBCARDS_INFO") ||
			Bean.currentMenu.isCurrentTab("CARDS_CLUBCARDS_TASKS") ||
			Bean.currentMenu.isCurrentTab("CARDS_CLUBCARDS_RESTS") ||
			Bean.currentMenu.isCurrentTab("CARDS_CLUBCARDS_FINANCE_OPER"))) {
		Bean.currentMenu.setFirstCurrentTab();
		tab = Bean.currentMenu.getCurrentTab();
		Bean.tabsHmSetValue(pageFormName, tab);
	}
	
	//Обрабатываем номера страниц
	String l_tasks_page = Bean.getDecodeParam(parameters.get("tasks_page"));
	Bean.pageCheck(pageFormName + tagPurseTasks, l_tasks_page);
	String l_tasks_page_beg = Bean.getFirstRowNumber(pageFormName + tagPurseTasks);
	String l_tasks_page_end = Bean.getLastRowNumber(pageFormName + tagPurseTasks);

	String task_type 	= Bean.getDecodeParam(parameters.get("task_type"));
	task_type 	= Bean.checkFindString(pageFormName + tagPurseTaskType, task_type, l_tasks_page);
	
	String task_state	= Bean.getDecodeParam(parameters.get("task_state"));
	task_state 		= Bean.checkFindString(pageFormName + tagPurseTaskState, task_state, l_tasks_page);
	
	String task_find 	= Bean.getDecodeParam(parameters.get("task_find"));
	task_find 	= Bean.checkFindString(pageFormName + tagPurseTaskFind, task_find, l_tasks_page);
	
	String l_fin_oper_page = Bean.getDecodeParam(parameters.get("fin_oper_page"));
	Bean.pageCheck(pageFormName + tagFinOper, l_fin_oper_page);
	String l_fin_oper_page_beg = Bean.getFirstRowNumber(pageFormName + tagFinOper);
	String l_fin_oper_page_end = Bean.getLastRowNumber(pageFormName + tagFinOper);

	String fin_oper_state	= Bean.getDecodeParam(parameters.get("fin_oper_state"));
	fin_oper_state 		= Bean.checkFindString(pageFormName + tagFinOperState, fin_oper_state, l_fin_oper_page);
	
	String fin_oper_find 	= Bean.getDecodeParam(parameters.get("fin_oper_find"));
	fin_oper_find 	= Bean.checkFindString(pageFormName + tagFinOperFind, fin_oper_find, l_fin_oper_page);
	
	String l_rests_page = Bean.getDecodeParam(parameters.get("rests_page"));
	Bean.pageCheck(pageFormName + tagRests, l_rests_page);
	String l_rests_page_beg = Bean.getFirstRowNumber(pageFormName + tagRests);
	String l_rests_page_end = Bean.getLastRowNumber(pageFormName + tagRests);

	
%>

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_TASKS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CLUBCARDS_TASKS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/cards/clubcardpurseupdate.jsp?id="+ id + "&type=tasks&action=add&process=no", "", "") %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagPurseTasks, "../crm/cards/clubcardpursespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_TASKS")+"&", "tasks_page") %>
			<% }%>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_RESTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CARDS_CLUBCARDS_RESTS")) { %>
					<%= Bean.getMenuButton("RUN", "../crm/cards/clubcardpurseupdate.jsp?id=" + id + "&type=rests&action=run&process=yes", Bean.bk_accountXML.getfieldTransl("h_calc_rests", false), "", Bean.bk_accountXML.getfieldTransl("h_calc_rests", false)) %>
				<% } %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagRests, "../crm/cards/clubcardpursespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_RESTS")+"&", "rests_page") %>
			<% }%>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_FINANCE_OPER")) { %>
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagFinOper, "../crm/cards/clubcardpursespecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CARDS_CLUBCARDS_FINANCE_OPER")+"&", "fin_oper_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
        <%= Bean.getDetailCaption(cd_card1 + ": " + purse.getValue("NUMBER_CARD_PURSE") + " - " + purse.getValue("NAME_CARD_PURSE_TYPE")) %>
		<tr>
			<!-- Выводим перечень закладок -->
			<td>
			<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/cards/clubcardpursespecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_INFO")) { 
	boolean hasEditPerm = Bean.currentMenu.isTabSheetEditPermitted("CARDS_CLUBCARDS_INFO");
%>

	<% if (hasEditPerm) { %>
	<script>
		var formData = new Array (
			new Array ('value_card_purse', 'varchar2', 1)
		);

		function myValidateForm() {
			return validateForm(formData);
		}
	</script>
		  <form action="../crm/cards/clubcardpurseupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			<input type="hidden" name="action" value="edit">
	    	<input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="id" value="<%= id %>">

		<%} %>
		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
							purse.getValue("CARD_SERIAL_NUMBER"),
							purse.getValue("ID_ISSUER"),
							purse.getValue("ID_PAYMENT_SYSTEM")
				) %>
				</td> <td><input type="text" name="cd_card1" size="30" value="<%= cd_card1 %>" readonly="readonly" class="inputfield-ro"></td>
			<% if (hasEditPerm) { %>
			<td><%= Bean.getClubCardXMLFieldTransl("value_card_purse", true) %></td> <td><input type="text" name="value_card_purse" size="20" value="<%= purse.getValue("VALUE_CARD_PURSE_FRMT") %>"  class="inputfield"></td>
			<% } else { %>
			<td><%= Bean.getClubCardXMLFieldTransl("value_card_purse", true) %></td> <td><input type="text" name="value_card_purse" size="20" value="<%= purse.getValue("VALUE_CARD_PURSE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<% } %>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("number_card_purse", false) %></td> <td><input type="text" name="number_card_purse" size="30" value="<%= purse.getValue("NUMBER_CARD_PURSE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_currency", false) %></td> <td><input type="text" name="name_currency" size="20" value="<%= purse.getValue("NAME_CURRENCY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("name_card_purse_type", false) %></td> <td><input type="text" name="name_card_purse_type" size="30" value="<%= purse.getValue("NAME_CARD_PURSE_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getIdCreationAndMoficationRecordFields(
				purse.getValue("ID_CARD_PURSE"),
				purse.getValue(Bean.getCreationDateFieldName()),
				purse.getValue("CREATED_BY"),
				purse.getValue(Bean.getLastUpdateDateFieldName()),
				purse.getValue("LAST_UPDATE_BY")
			) %>
		<% if (hasEditPerm) {%>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/clubcardpurseupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/cards/clubcardspecs.jsp?id=" + purse.getValue("CARD_SERIAL_NUMBER") + "&iss=" + purse.getValue("ID_ISSUER") + "&paysys=" + purse.getValue("ID_PAYMENT_SYSTEM")) %>
				</td>
			</tr>
		<% } else { %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getGoBackButton("../crm/cards/clubcardspecs.jsp?id=" + purse.getValue("CARD_SERIAL_NUMBER") + "&iss=" + purse.getValue("ID_ISSUER") + "&paysys=" + purse.getValue("ID_PAYMENT_SYSTEM")) %>
				</td>
			</tr>
		<% } %>
	</table>
	</form>

<%
} 
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_TASKS")) { 
	boolean hasEditPerm = Bean.currentMenu.isTabSheetEditPermitted("CARDS_CLUBCARDS_TASKS");

%> 		
	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("task_find", task_find, "../crm/cards/clubcardpursespecs.jsp?id=" + id + "&tasks_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("task_type", "../crm/cards/clubcardpursespecs.jsp?id=" + id + "&tasks_page=1", Bean.card_taskXML.getfieldTransl("CD_CARD_OPERATION_TYPE", false)) %>
			<%= Bean.getClubCardOperationTypeOptions(task_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

		<%=Bean.getSelectOnChangeBeginHTML("task_state", "../crm/cards/clubcardpursespecs.jsp?id=" + id + "&tasks_page=1", Bean.card_taskXML.getfieldTransl("cd_card_oper_state", false)) %>
			<%= Bean.getClubCardOperationStateOptions(task_state, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
	  	</tr>
	</table>

	<%= purse.getClubCardsTasksHTML(task_find, task_type, task_state, l_tasks_page_beg, l_tasks_page_end) %>
					<%
} 

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_RESTS")) {

    String tagFindRest = "_FIND_PURSE_REST";
    String tagFindRestBegin = "_FIND_PURSE_REST_BEGIN";
    String tagFindRestEnd = "_FIND_PURSE_REST_END";
    
	String find_rest	 	= Bean.getDecodeParam(parameters.get("find_rest"));
	find_rest 				= Bean.checkFindString(pageFormName + tagFindRest, find_rest, "");
	String begin_rest_period	 	= Bean.getDecodeParam(parameters.get("begin_rest_period"));
	begin_rest_period 			= Bean.checkFindString(pageFormName + tagFindRestBegin, begin_rest_period, "");
	String end_rest_period	 	= Bean.getDecodeParam(parameters.get("end_rest_period"));
	end_rest_period 				= Bean.checkFindString(pageFormName + tagFindRestEnd, end_rest_period, "");

%> 
	<form action="../crm/cards/clubcardpursespecs.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="id" value="<%= id %>">
	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<td valign="top"><%= Bean.bk_accountXML.getfieldTransl("h_begin_period", false) %>&nbsp;<%=Bean.getCalendarInputField("begin_rest_period", begin_rest_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_end_period", false) %>&nbsp;<%=Bean.getCalendarInputField("end_rest_period", end_rest_period, "10") %>&nbsp;&nbsp;&nbsp;
			<%= Bean.bk_accountXML.getfieldTransl("h_find_string", false) %>&nbsp;
			<input type="text" name="find_rest" id="find_rest" size="30" value="<%=find_rest %>" class="inputfield" title="<%= Bean.buttonXML.getfieldTransl("find_string", false) %>">&nbsp;
			<%=Bean.getSubmitButtonAjax("../crm/cards/clubcardpursespecs.jsp?id=" + id + "&rest_page=1&", "find", "updateForm") %>&nbsp;
			</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	</form>
	<%= Bean.getCalendarScript("begin_rest_period", false) %>
	<%= Bean.getCalendarScript("end_rest_period", false) %>

	<%=purse.getRestsHTML(begin_rest_period, end_rest_period, find_rest, l_rests_page_beg, l_rests_page_end)%> 
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CARDS_CLUBCARDS_FINANCE_OPER")) { 

	%> 		
		<table <%=Bean.getTableBottomFilter() %>>
		  	<tr>
			<%= Bean.getFindHTML("fin_oper_find", fin_oper_find, "../crm/cards/clubcardpursespecs.jsp?id=" + id + "&fin_oper_page=1") %>

			<%=Bean.getSelectOnChangeBeginHTML("fin_oper_state", "../crm/cards/clubcardpursespecs.jsp?id=" + id + "&fin_oper_page=1", Bean.card_taskXML.getfieldTransl("cd_card_oper_state", false)) %>
				<%= Bean.getClubCardFinanceOperationStateOptions(fin_oper_state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		  	</tr>
		</table>

		<%= purse.getFinanceOperationsHTML(fin_oper_state, fin_oper_find, l_fin_oper_page_beg, l_fin_oper_page_end) %>
<%
}
}
%>
</div></div>
</body>
</html>
