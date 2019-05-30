<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcCallCenterQuestionObject"%>
<%@page import="bc.objects.bcCallCenterFAQObject"%>
<%@page import="java.util.HashMap"%>
<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcCallCenterCallGroupObject"%>
<%@page import="bc.objects.bcNatPrsObject"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="bc.objects.bcClubCardPurseObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
<title></title>
</head>
<body>
<div id="div_tabsheet">

<% 
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());


String pageFormName = "CALL_CENTER_QUESTIONS";
String tagActivities = "_ACTIVITIES";
String tagActivitiesFind = "_ACTIVITIES_FIND";
String tagMessages = "_MESSAGES";
String tagHistory = "_HISTORY";
String tagHistoryFind = "_HISTORY_FIND";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String tab = Bean.getDecodeParam(parameters.get("tab"));
if (id==null || "".equalsIgnoreCase(id)) { id=""; }
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

if ("".equalsIgnoreCase(id)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} 
else {
	Bean.tabsHmSetValue(pageFormName, tab);	
	
	bcCallCenterQuestionObject question = new bcCallCenterQuestionObject(id);
	
	if (!("CALL_GROUP".equalsIgnoreCase(question.getValue("CD_CC_CONTACT_TYPE")))) {
		Bean.currentMenu.setExistFlag("CALL_CENTER_QUESTIONS_CALL", false);
		if (Bean.currentMenu.isCurrentTab("CALL_CENTER_QUESTIONS_CALL")) {
    		Bean.currentMenu.setFirstCurrentTab();
    		tab = Bean.currentMenu.getCurrentTab();
    		Bean.tabsHmSetValue(pageFormName, tab);
		}
	}
	
	bcCallCenterFAQObject faq = null;
	boolean hasFAQ = false;
	if (!(question.getValue("ID_CC_FAQ")==null || "".equalsIgnoreCase(question.getValue("ID_CC_FAQ")))) {
		hasFAQ = true;
		faq = new bcCallCenterFAQObject(question.getValue("ID_CC_FAQ"));
	}
	
	//Обрабатываем номера страниц
	String l_activity_page = Bean.getDecodeParam(parameters.get("activity_page"));
	Bean.pageCheck(pageFormName + tagActivities, l_activity_page);
	String l_activity_page_beg = Bean.getFirstRowNumber(pageFormName + tagActivities);
	String l_activity_page_end = Bean.getLastRowNumber(pageFormName + tagActivities);

	String activity_find 	= Bean.getDecodeParam(parameters.get("activity_find"));
	activity_find 	= Bean.checkFindString(pageFormName + tagActivitiesFind, activity_find, l_activity_page);
	
	String l_message_page = Bean.getDecodeParam(parameters.get("message_page"));
	Bean.pageCheck(pageFormName + tagMessages, l_message_page);
	String l_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessages);
	String l_message_page_end = Bean.getLastRowNumber(pageFormName + tagMessages);
	
	String l_history_page = Bean.getDecodeParam(parameters.get("history_page"));
	Bean.pageCheck(pageFormName + tagHistory, l_history_page);
	String l_history_page_beg = Bean.getFirstRowNumber(pageFormName + tagHistory);
	String l_history_page_end = Bean.getLastRowNumber(pageFormName + tagHistory);

	String history_find 	= Bean.getDecodeParam(parameters.get("history_find"));
	history_find 	= Bean.checkFindString(pageFormName + tagHistoryFind, history_find, l_history_page);
	
	
	
%>
  <% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_QUESTIONS_INFO")) {%>
		    <%= Bean.getMenuButton("ADD", "../crm/call_center/questionupdate.jsp?id=" + question.getValue("ID_CC_QUESTION") + "&type=general&action=add2&process=no", "", "") %>
		    <%= Bean.getMenuButton("DELETE", "../crm/call_center/questionupdate.jsp?id=" + question.getValue("ID_CC_QUESTION") + "&type=general&action=remove&process=yes", Bean.call_centerXML.getfieldTransl("h_delete_question", false), question.getValue("ID_CC_QUESTION") + " - " +  question.getValue("TITLE")) %>
		<% } %>

		<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_QUESTIONS_ACTIVITIES")) {%>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_QUESTIONS_ACTIVITIES")) {%>
			    <%= Bean.getMenuButton("ADD", "../crm/call_center/questionupdate.jsp?type=activity&id=" + id + "&action=add&process=no", "", "") %>
			<% } %>
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagActivities, "../crm/call_center/questionspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_QUESTIONS_ACTIVITIES")+"&", "activity_page") %>
		<% } %>

		<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_QUESTIONS_HISTORY")) {%>
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagHistory, "../crm/call_center/questionspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_QUESTIONS_HISTORY")+"&", "history_page") %>

		<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(question.getValue("ID_CC_QUESTION") + " - " + question.getValue("TITLE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/call_center/questionspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% 
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CALL_CENTER_QUESTIONS_INFO")) {%> 
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %></td><td><input type="text" name="id_cc_question" size="20" value="<%= question.getValue("ID_CC_QUESTION") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_contact_type", false) %></td><td><input type="text" name="name_cc_contact_type" size="40" value="<%= question.getValue("NAME_CC_CONTACT_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("title", false) %></td><td><input type="text" name="title" size="40" value="<%= question.getValue("title") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_type", false) %></td><td><input type="text" name="name_cc_question_type" size="40" value="<%= question.getValue("NAME_CC_QUESTION_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("due_date", false) %></td><td><input type="text" name="due_date" size="20" value="<%= question.getValue("DUE_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_status", false) %></td><td><input type="text" name="name_cc_question_status" size="40" value="<%= question.getValue("NAME_CC_QUESTION_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("name_nat_prs", false) %>
				<%= Bean.getGoToNatPrsLink(question.getValue("ID_NAT_PRS")) %>
		    </td><td><input type="text" name="name_nat_prs" size="40" value="<%= Bean.getNatPrsName(question.getValue("ID_NAT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_important", false) %></td><td><input type="text" name="name_cc_question_important" size="40" value="<%= question.getValue("NAME_CC_QUESTION_IMPORTANT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<% String cd_card1 = Bean.getClubCardCode(question.getValue("CARD_SERIAL_NUMBER")+"_"+question.getValue("CARD_ID_ISSUER")+"_"+question.getValue("CARD_ID_PAYMENT_SYSTEM")); %>
			<td valign="top"><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						question.getValue("CARD_SERIAL_NUMBER"),
						question.getValue("CARD_ID_ISSUER"),
						question.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td><td><input type="text" name="cd_card1" size="40" value="<%= cd_card1 %>" readonly="readonly" class="inputfield-ro"></td>			
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_urgent", false) %></td><td><input type="text" name="name_cc_question_urgent" size="40" value="<%= question.getValue("NAME_CC_QUESTION_URGENT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("assigned_user", false) %>
				<%= Bean.getGoToCallCenterAdministrationLink(question.getValue("ID_ASSIGNED_USER")) %>
		    </td><td><input type="text" name="assigned_user" size="40" value="<%= question.getValue("NAME_USER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(question.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="40" value="<%= Bean.getClubShortName(question.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("faq", false) %>
				<%= Bean.getGoToCallCenterFAQLink(question.getValue("ID_CC_FAQ")) %>
		    </td><td><input type="text" name="title_cc_faq" size="40" value="<%= Bean.getFAQTitle(question.getValue("ID_CC_FAQ")) %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				question.getValue(Bean.getCreationDateFieldName()),
				question.getValue("CREATED_BY"),
				question.getValue(Bean.getLastUpdateDateFieldName()),
				question.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/call_center/questions.jsp") %>
			</td>
		</tr>
		
	</table>
<% 	} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_QUESTIONS_INFO")) { %>
    <script>
		var formDataQuestion = new Array (
			new Array ('title', 'varchar2', 1),
			new Array ('due_date', 'varchar2', 1),
			new Array ('cc_contact_type', 'varchar2', 1),
			new Array ('cd_cc_question_type', 'varchar2', 1),
			new Array ('cd_cc_question_status', 'varchar2', 1),
			new Array ('cd_cc_question_important', 'varchar2', 1),
			new Array ('cd_cc_question_urgent', 'varchar2', 1),
			new Array ('exist_flag', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataQuestion);
		}
	</script>
    
	<form action="../crm/call_center/questionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= question.getValue("ID_CC_QUESTION") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %></td><td><input type="text" name="id_cc_question" size="20" value="<%= question.getValue("ID_CC_QUESTION") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(question.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(question.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("title", true) %></td><td><input type="text" name="title" size="40" value="<%= question.getValue("title") %>" class="inputfield"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_contact_type", true) %></td><td><select name="cc_contact_type" class="inputfield"><%= Bean.getCallCenterContactTypeOptions(question.getValue("CD_CC_CONTACT_TYPE"), false) %></select></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("due_date", true) %></td><td><%=Bean.getCalendarInputField("due_date", question.getValue("DUE_DATE_FRMT"), "20") %></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_type", true) %></td><td><select name="cd_cc_question_type" class="inputfield"><%= Bean.getCallCenterQuestionTypeOptions(question.getValue("CD_CC_QUESTION_TYPE"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("name_nat_prs", false) %>
				<%= Bean.getGoToNatPrsLink(question.getValue("ID_NAT_PRS")) %>
		    </td>
            <td>
				<%=Bean.getWindowFindCallCenterNatPrs("nat_prs", question.getValue("ID_NAT_PRS"), Bean.getNatPrsName(question.getValue("ID_NAT_PRS")), "35") %>
			</td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_status", true) %></td><td><select name="cd_cc_question_status" class="inputfield"><%= Bean.getCallCenterQuestionStatusOptions(question.getValue("CD_CC_QUESTION_STATUS"), false) %></select></td>
		</tr>
		<tr>
			<% String cd_card1 = Bean.getClubCardCode(question.getValue("CARD_SERIAL_NUMBER")+"_"+question.getValue("CARD_ID_ISSUER")+"_"+question.getValue("CARD_ID_PAYMENT_SYSTEM")); %>
			<td valign="top"><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						question.getValue("CARD_SERIAL_NUMBER"),
						question.getValue("CARD_ID_ISSUER"),
						question.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td> 
			<td>
				<%=Bean.getWindowFindCallCenterClubCard(cd_card1, question.getValue("CARD_SERIAL_NUMBER"), question.getValue("CARD_ID_ISSUER"), question.getValue("CARD_ID_PAYMENT_SYSTEM"), "35") %>
			</td>			
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_important", true) %></td><td><select name="cd_cc_question_important" class="inputfield"><%= Bean.getCallCenterQuestionImportantOptions(question.getValue("CD_CC_QUESTION_IMPORTANT"), false) %></select></td>
		</tr>
		<tr>
		<td><%= Bean.call_centerXML.getfieldTransl("assigned_user", false) %>
				<%= Bean.getGoToCallCenterAdministrationLink(question.getValue("ID_ASSIGNED_USER")) %>
		    </td>
			<td>
				<%=Bean.getWindowFindCallCenterUser("user", question.getValue("ID_ASSIGNED_USER"), Bean.getUserName(question.getValue("ID_ASSIGNED_USER")), "35") %>
			</td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_urgent", true) %></td><td><select name="cd_cc_question_urgent" class="inputfield"><%= Bean.getCallCenterQuestionUrgentOptions(question.getValue("CD_CC_QUESTION_URGENT"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("faq", false) %>
				<%= Bean.getGoToCallCenterFAQLink(question.getValue("ID_CC_FAQ")) %>
		    </td>
			<td>
				<%=Bean.getWindowFindCallCenterFAQ("cc_faq", question.getValue("ID_CC_FAQ"), "35") %>
			</td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				question.getValue(Bean.getCreationDateFieldName()),
				question.getValue("CREATED_BY"),
				question.getValue(Bean.getLastUpdateDateFieldName()),
				question.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/questionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/call_center/questions.jsp") %>
			</td>
		</tr>
	</table>
	</form>	
	<%= Bean.getCalendarScript("due_date", false) %>
<%}
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CALL_CENTER_QUESTIONS_DESCRIPTION")) {%> 
	<table <%=Bean.getTableDetailParam() %>> 
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %></td><td><input type="text" name="id_cc_question" size="20" value="<%= question.getValue("ID_CC_QUESTION") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("description", false) %></td><td><textarea name="description" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= question.getValue("DESCRIPTION") %></textarea></td>
		</tr>
		<% if (hasFAQ) {%>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("question_cc_faq", false) %></td><td><textarea name="question_cc_faq" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= faq.getValue("QUESTION_CC_FAQ") %></textarea></td>
		</tr>
		<% } %>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("note", false) %></td><td><textarea name="note" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= question.getValue("NOTE") %></textarea></td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<%=Bean.getGoBackButton("../crm/call_center/questions.jsp") %>
			</td>
		</tr>
	
	</table>
<% 	} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_QUESTIONS_DESCRIPTION")) { %>
	<script>
		var formData = new Array (
		);
	</script>

    <form action="../crm/call_center/questionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="description">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= question.getValue("ID_CC_QUESTION") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %></td><td><input type="text" name="id_cc_question" size="20" value="<%= question.getValue("ID_CC_QUESTION") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("description", false) %></td><td><textarea name="description" cols="120" rows="3" class="inputfield"><%= question.getValue("DESCRIPTION") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("note", false) %></td><td><textarea name="note" cols="120" rows="3" class="inputfield"><%= question.getValue("NOTE") %></textarea></td>
		</tr>
		<% if (hasFAQ) {%>
		<tr>
			<td>FAQ: <%= Bean.call_centerXML.getfieldTransl("question_cc_faq", false) %></td><td><textarea name="question_cc_faq" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= faq.getValue("QUESTION_CC_FAQ") %></textarea></td>
		</tr>
		<% } %>
		<tr>
			<td colspan="2" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/questionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/call_center/questions.jsp") %>
			</td>
		</tr>

	</table>
	</form>	
<%}


if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CALL_CENTER_QUESTIONS_SOLUTION")) {%> 
	<table <%=Bean.getTableDetailParam() %>> 
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %></td><td><input type="text" name="id_cc_question" size="20" value="<%= question.getValue("ID_CC_QUESTION") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("resolution", false) %></td><td><textarea name="resolution" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= question.getValue("RESOLUTION") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("solution", false) %></td><td><textarea name="solution" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= question.getValue("SOLUTION") %></textarea></td>
		</tr>
		<% if (hasFAQ) {%>
		<tr>
			<td>FAQ: <%= Bean.call_centerXML.getfieldTransl("answer_cc_faq", false) %></td><td><textarea name="answer_cc_faq" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= faq.getValue("ANSWER_CC_FAQ") %></textarea></td>
		</tr>
		<% } %>
		<tr>
			<td colspan="2" align="center">
				<%=Bean.getGoBackButton("../crm/call_center/questions.jsp") %>
			</td>
		</tr>

	</table>
<% 	} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_QUESTIONS_SOLUTION")) { %>
	<script>
		var formData = new Array (
		);
	</script>

  	<form action="../crm/call_center/questionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
    	<input type="hidden" name="type" value="solution">
    	<input type="hidden" name="action" value="edit">
    	<input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= question.getValue("ID_CC_QUESTION") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %></td><td><input type="text" name="id_cc_question" size="20" value="<%= question.getValue("ID_CC_QUESTION") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("resolution", false) %></td><td><textarea name="resolution" cols="120" rows="3" class="inputfield"><%= question.getValue("RESOLUTION") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("solution", false) %></td><td><textarea name="solution" cols="120" rows="3" class="inputfield"><%= question.getValue("SOLUTION") %></textarea></td>
		</tr>
		<% if (hasFAQ) {%>
		<tr>
			<td>FAQ: <%= Bean.call_centerXML.getfieldTransl("answer_cc_faq", false) %></td><td><textarea name="answer_cc_faq" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= faq.getValue("ANSWER_CC_FAQ") %></textarea></td>
		</tr>
		<% } %>
		<tr>
			<td colspan="2" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/questionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/call_center/questions.jsp") %>
			</td>
		</tr>

	</table>
	</form>	
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_QUESTIONS_ACTIVITIES")) {%>
   <table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("activity_find", activity_find, "../crm/call_center/questionspecs.jsp?id=" + id + "&activity_page=1") %>

		</tr>
	</table>
   <%=question.getActivitiesHTML("", l_activity_page_beg, l_activity_page_end) %>
<% }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_QUESTIONS_CALL")) {%>
	
	<%
	boolean hasEditPermission = false;
	int hasTabSheetPermission = Bean.currentMenu.isEditPermited("CALL_CENTER_INQUIRER_CLIENTS");
	if (hasTabSheetPermission > 0) {
		hasEditPermission = true;
	}
			
	bcCallCenterCallGroupObject group = new bcCallCenterCallGroupObject(question.getValue("ID_CC_CALL_GROUP"));
			
	bcNatPrsObject natprs = new bcNatPrsObject(question.getValue("ID_NAT_PRS"));
			
	bcClubCardObject clubcard = new bcClubCardObject(question.getValue("CARD_SERIAL_NUMBER"), question.getValue("CARD_ID_ISSUER"), question.getValue("CARD_ID_PAYMENT_SYSTEM"));
	
	bcClubCardPurseObject superbon = new bcClubCardPurseObject(question.getValue("CARD_SERIAL_NUMBER"), question.getValue("CARD_ID_ISSUER"), question.getValue("CARD_ID_PAYMENT_SYSTEM"), "SUPERBON");
	
	%>

	<% if (hasEditPermission) { %>
	<form action="../crm/call_center/inquirerupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="client">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id %>">
	        <input type="hidden" name="id_cc_question" value="<%=question.getValue("ID_CC_QUESTION")%>">
	<% } %>
	<table <%=Bean.getTableMenuFilter() %>>
		
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_call_group", false) %>
				<%= Bean.getGoToCallCenterCallGroupLink(group.getValue("ID_CC_CALL_GROUP")) %>
			</td><td><input type="text" name="name_cc_call_group" size="40" value="<%= group.getValue("NAME_CC_CALL_GROUP") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("name_nat_prs", false) %>
				<%= Bean.getGoToNatPrsLink(question.getValue("ID_NAT_PRS")) %>
		    </td><td><input type="text" name="name_nat_prs" size="40" value="<%= Bean.getNatPrsName(question.getValue("ID_NAT_PRS")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("date_cc_call_group", false) %></td><td><input type="text" name="name_cc_call_group" size="10" value="<%= group.getValue("DATE_CC_CALL_GROUP_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		  	<td><%= Bean.natprsXML.getfieldTransl("date_of_birth", false) %> </td><td><input type="text" name="date_of_birth" size="10" value="<%= natprs.getValue("DATE_OF_BIRTH_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", false) %>
				<%= Bean.getGoToCallCenterInquirerLink(group.getValue("ID_CC_INQUIRER")) %>
			</td>
			<td>
				<input type="text" name="name_cc_inquirer" size="40" value="<%= group.getValue("NAME_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro">
			</td>
		    <td><%= Bean.natprsXML.getfieldTransl("phone_mobile", false) %></td><td><input type="text" name="phone_mobile" size="30" value="<%= natprs.getValue("PHONE_MOBILE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>			
		<tr>
			<td colspan="2"><b><%= Bean.call_centerXML.getfieldTransl("cg_criterion", false) %></b></td>
			<% String cd_card1 = Bean.getClubCardCode(question.getValue("CARD_SERIAL_NUMBER")+"_"+question.getValue("CARD_ID_ISSUER")+"_"+question.getValue("CARD_ID_PAYMENT_SYSTEM")); %>
			<td valign="top" style="border-top:solid windowtext 1.0pt;"><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %>
				<%= Bean.getGoToClubCardLink(
						question.getValue("CARD_SERIAL_NUMBER"),
						question.getValue("CARD_ID_ISSUER"),
						question.getValue("CARD_ID_PAYMENT_SYSTEM")
					) %>
			</td><td style="border-top:solid windowtext 1.0pt;"><input type="text" name="cd_card1" size="30" value="<%= cd_card1 %>" readonly="readonly" class="inputfield-ro"></td>			
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_begin_purchase_date", false) %></td><td><input type="text" name="cg_crit_begin_purchase_date" size="10" value="<%= group.getValue("CRIT_BEGIN_PURCHASE_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<%  String goToJurPrsHyperLink = Bean.getGoToJurPrsHyperLink(clubcard.getValue("ID_JUR_PRS_WHO_CARD_SOLD"));%>
		  	<td><%= Bean.getClubCardXMLFieldTransl("sname_jur_pr_who_has_sold_card", false) %>
				<%= goToJurPrsHyperLink %></td><td><input type="text" name="id_jur_pr_who_has_sold_card" size="30" value="<%= Bean.getJurPersonShortName(clubcard.getValue("ID_JUR_PRS_WHO_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro">
			</td>
		</tr>			
		<tr>
		    <td><%= Bean.call_centerXML.getfieldTransl("cg_crit_give_for_event", false) %>
				<%= Bean.getGoToClubEventLink(group.getValue("CRIT_GIVE_FOR_EVENT")) %>
			</td>
		  	<td>
				<input type="text" name="crit_give_for_event" size="40" value="<%= Bean.getClubActionName(group.getValue("CRIT_GIVE_FOR_EVENT")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
			<%  String goToJurPrsHyperLink2 = Bean.getGoToJurPrsHyperLink(clubcard.getValue("ID_JUR_PRS_WHERE_CARD_SOLD"));	%>
			  <td><%= Bean.getClubCardXMLFieldTransl("sname_jur_prs_where_card_sold", false) %>
				<%= goToJurPrsHyperLink2 %></td><td><input type="text" name="id_jur_prs_where_card_sold" size="30" value="<%= Bean.getJurPersonShortName(clubcard.getValue("ID_JUR_PRS_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_given_from_date", false) %></td><td><input type="text" name="cg_crit_given_from_date" size="10" value="<%= group.getValue("CRIT_GIVEN_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_given_to_date", false) %>&nbsp;<input type="text" name="cg_crit_given_to_date" size="10" value="<%= group.getValue("CRIT_GIVEN_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			 <td><%= Bean.getClubCardXMLFieldTransl("name_serv_plce_where_card_sold", false) %>
				<%= Bean.getGoToServicePlaceLink(clubcard.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>
			  </td><td><input type="text" id="name_service_place" name="name_service_place" size="30" value="<%= Bean.getServicePlaceName(clubcard.getValue("ID_SERV_PLACE_WHERE_CARD_SOLD")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchases", false) %></td>
				<td>
					<%
					  String p_crit_made_purchase_condition = "";
					  if ("LESS".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_less", false);
					  } else if ("EQUAL".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_equal", false);
					  } else if ("MORE".equalsIgnoreCase(group.getValue("CRIT_MADE_PURCHASE_CONDITION"))) {
						  p_crit_made_purchase_condition = Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_more", false);
					  }
					%>
					<input type="text" name="crit_made_purchase_condition" size="10" value="<%= p_crit_made_purchase_condition %>" readonly="readonly" class="inputfield-ro">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="crit_made_purchase_value" size="5" value="<%= group.getValue("CRIT_MADE_PURCHASE_VALUE") %>" readonly="readonly" class="inputfield-ro">
				</td>
			<td><%= Bean.getClubCardXMLFieldTransl("club_event_given_card", false) %>
				<%= Bean.getGoToClubEventLink(clubcard.getValue("ID_CLUB_EVENT")) %>
			</td>
			<td><input type="text" name="id_club_event" size="35" value="<%= Bean.getClubActionName(clubcard.getValue("ID_CLUB_EVENT")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		  	<td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_from_date", false) %></td><td><input type="text" name="crit_made_purchase_from_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_FROM_DATE_DF") %>" readonly="readonly" class="inputfield-ro">&nbsp;<%= Bean.call_centerXML.getfieldTransl("cg_crit_made_purchase_to_date", false) %>&nbsp;<input type="text" name="crit_made_purchase_to_date" size="10" value="<%= group.getValue("CRIT_MADE_PURCHASE_TO_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.getClubCardXMLFieldTransl("date_card_sale", false) %></td><td><input type="text" name="date_card_sale" size="12" value="<%= clubcard.getValue("DATE_CARD_SALE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
		    <td>&nbsp;&nbsp;&nbsp;&nbsp;<%= Bean.call_centerXML.getfieldTransl("crit_made_purchase_dealer", false) %>
				<%= Bean.getGoToJurPrsHyperLink(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>
			</td>
		  	<td>
				<input type="text" name="crit_made_purchase_dealer" size="40" value="<%= Bean.getJurPersonShortName(group.getValue("CRIT_MADE_PURCHASE_DEALER")) %>" readonly="readonly" class="inputfield-ro">
			</td>	
			<td style="border-top:solid windowtext 1.0pt;"><%= Bean.call_centerXML.getfieldTransl("card_bal_exist", false) %> (<%=clubcard.getValue("SNAME_CURRENCY") %>)</td> <td style="border-top:solid windowtext 1.0pt;"><input type="text" name="card_bal_exist" size="12" value="<%= clubcard.getValue("BAL_EXIST_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_last_date_payment", false) %></td> <td><input type="text" name="card_last_date_payment" size="12" value="<%= clubcard.getValue("DATE_ACC_DF") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_payment_count", false) %></td> <td><input type="text" name="card_payment_count" size="12" value="<%= question.getValue("CARD_PAYMENT_COUNT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("card_payment_sum", false) %></td> <td><input type="text" name="card_payment_sum" size="12" value="<%= question.getValue("CARD_PAYMENT_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.call_centerXML.getfieldTransl("suporbon_sum", false) %></td> <td><input type="text" name="suporbon_sum" size="12" value="<%= superbon.getValue("VALUE_CARD_PURSE_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		
	</table>
	<table <%=Bean.getTableBottomParam() %>><tbody>
		<%
			String pLineStyle = Bean.getUIUserParam("inquirer_line_answer_style");
			if (pLineStyle == null || "".equalsIgnoreCase(pLineStyle)) {
				pLineStyle = "SELECT";
			}
		%>
		<%=question.getInquirerLineHTML(hasEditPermission, pLineStyle, "1", "1000") %>

		<%
		String pCallDate = question.getValue("DUE_DATE_DF");
		if (pCallDate == null || "".equalsIgnoreCase(pCallDate)) {
			pCallDate = Bean.getSysDate();
		}
		%>

	<% if (hasEditPermission) { %>
		<tr>
		  	<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("call_date", true) %></td><td style="background-color: #D3D3D3;"><%=Bean.getCalendarInputField("call_date", pCallDate, "10") %></td>
		</tr>
		<tr>
			<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("cc_question_status", true) %></td><td style="background-color: #D3D3D3;"><select name="cd_cc_question_status" class="inputfield"><%= Bean.getCallCenterQuestionStatusOptions(question.getValue("CD_CC_QUESTION_STATUS"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("note", false) %></td><td colspan="2"><textarea name="question_note" cols="120" rows="3" class="inputfield"><%= question.getValue("NOTE") %></textarea></td>
		</tr>
	<% } else { %>
		<tr>
		  	<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("call_date", false) %></td><td><input type="text" name="call_date" size="10" value="<%= pCallDate %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td colspan="2"><%= Bean.call_centerXML.getfieldTransl("cc_question_status", false) %></td><td><input type="text" name="name_cc_question_status" size="10" value="<%= question.getValue("NAME_CC_QUESTION_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("note", false) %></td><td colspan="2"><textarea name="question_note" cols="120" rows="3" readonly="readonly" class="inputfield-ro"><%= question.getValue("NOTE") %></textarea></td>
		</tr>
	<% } %>
	</tbody></table>
	<table <%=Bean.getTableMenuFilter() %>>
 		<tr>
			<td colspan="4" align="center">
				<% if (hasEditPermission) { %>
				<%=Bean.getSubmitButtonAjax("../crm/call_center/questionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% } %>
			</td>
		</tr>
	</table>
	</form>
	<% if (hasEditPermission) { %>
		<%= Bean.getCalendarScript("call_date", false) %>
	<% } %>
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_QUESTIONS_HISTORY")) {%>
   <table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("history_find", history_find, "../crm/call_center/questionspecs.jsp?id=" + id + "&history_page=1") %>

		</tr>
	</table>
	<%=question.getHistoryHTML("", l_history_page_beg, l_history_page_end) %>
<% }

} %>
</div></div>
</body>
</html>
