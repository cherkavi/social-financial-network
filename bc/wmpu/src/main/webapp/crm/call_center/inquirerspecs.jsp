<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcCallCenterInquirerObject"%>
<%@page import="java.util.HashMap"%>
<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
<title></title>
</head>
<body>
<div id="div_tabsheet">

<% 
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());


String pageFormName = "CALL_CENTER_INQUIRER";
String tagQuestions = "_QUESTIONS";
String tagQuestionsFind = "_QUESTIONS_FIND";
String tagQuestionsLine = "_QUESTIONS_LINE";
String tagClients = "_CLIENTS";
String tagClientFind = "_CLIENT_FIND";
String tagQuestionType = "_CLIENT_QUESTION_TYPE";
String tagQuestionStatus = "_CLIENT_QUESTION_STATUS";

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
	
	//Обрабатываем номера страниц
	String l_question_page = Bean.getDecodeParam(parameters.get("question_page"));
	Bean.pageCheck(pageFormName + tagQuestions, l_question_page);
	String l_question_page_beg = Bean.getFirstRowNumber(pageFormName + tagQuestions);
	String l_question_page_end = Bean.getLastRowNumber(pageFormName + tagQuestions);

	String question_find 	= Bean.getDecodeParam(parameters.get("question_find"));
	question_find 	= Bean.checkFindString(pageFormName + tagQuestionsFind, question_find, l_question_page);

	String question_line 	= Bean.getDecodeParam(parameters.get("question_line"));
	question_line 	= Bean.checkFindString(pageFormName + tagQuestionsLine, question_line, l_question_page);

	String l_client_page = Bean.getDecodeParam(parameters.get("client_page"));
	Bean.pageCheck(pageFormName + tagClients, l_client_page);
	String l_client_page_beg = Bean.getFirstRowNumber(pageFormName + tagClients);
	String l_client_page_end = Bean.getLastRowNumber(pageFormName + tagClients);

	String client_find 	= Bean.getDecodeParam(parameters.get("client_find"));
	client_find 	= Bean.checkFindString(pageFormName + tagClientFind, client_find, l_client_page);

	String quest_type 	= Bean.getDecodeParam(parameters.get("quest_type"));
	quest_type 	= Bean.checkFindString(pageFormName + tagQuestionType, quest_type, l_client_page);

	String quest_status	= Bean.getDecodeParam(parameters.get("quest_status"));
	quest_status		= Bean.checkFindString(pageFormName + tagQuestionStatus, quest_status, l_client_page);
	
	bcCallCenterInquirerObject inquirer = new bcCallCenterInquirerObject(id);
	
	
%>
  <% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

		<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_INQUIRER_INFO")) {%>
		    <%= Bean.getMenuButton("ADD", "../crm/call_center/inquirerupdate.jsp?id=" + inquirer.getValue("ID_CC_INQUIRER") + "&type=general&action=add2&process=no", "", "") %>
		    <%= Bean.getMenuButton("DELETE", "../crm/call_center/inquirerupdate.jsp?id=" + inquirer.getValue("ID_CC_INQUIRER") + "&type=general&action=remove&process=yes", Bean.call_centerXML.getfieldTransl("h_delete_inquirer", false), inquirer.getValue("ID_CC_INQUIRER") + " - " +  inquirer.getValue("NAME_CC_INQUIRER")) %>
		<% } %>

		<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_INQUIRER_QUESTIONS")) {%>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_INQUIRER_QUESTIONS")) {%>
			    <%= Bean.getMenuButton("ADD", "../crm/call_center/inquirerupdate.jsp?type=line&id=" + id + "&action=add&process=no", "", "") %>
			<% } %>
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagQuestions, "../crm/call_center/inquirerspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_INQUIRER_QUESTIONS")+"&", "question_page") %>
		<% } %>

		<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_INQUIRER_CLIENTS")) {%>
			<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagClients, "../crm/call_center/inquirerspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_INQUIRER_CLIENTS")+"&", "client_page") %>
		<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(inquirer.getValue("ID_CC_INQUIRER") + " - " + inquirer.getValue("NAME_CC_INQUIRER")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/call_center/inquirerspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% 
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CALL_CENTER_INQUIRER_INFO")) {%> 
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_inquirer", false) %></td><td><input type="text" name="id_cc_inquirer" size="20" value="<%= inquirer.getValue("ID_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(inquirer.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="40" value="<%= Bean.getClubShortName(inquirer.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", true) %></td><td><input type="text" name="name_cc_inquirer" size="40" value="<%= inquirer.getValue("NAME_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_inquirer_state", true) %></td><td><input type="text" name="name_cc_inquirer" size="40" value="<%= inquirer.getValue("NAME_CC_INQUIRER_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>			
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("date_cc_inquirer", true) %></td><td><input type="text" name="name_cc_inquirer" size="10" value="<%= inquirer.getValue("DATE_CC_INQUIRER_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("desc_cc_inquirer", false) %></td><td  colspan="3"><textarea name="desc_cc_inquirer" cols="100" rows="3" readonly="readonly" class="inputfield-ro"><%= inquirer.getValue("DESC_CC_INQUIRER") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				inquirer.getValue(Bean.getCreationDateFieldName()),
				inquirer.getValue("CREATED_BY"),
				inquirer.getValue(Bean.getLastUpdateDateFieldName()),
				inquirer.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/call_center/questions.jsp") %>
			</td>
		</tr>
		
	</table>
<% 	} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_INQUIRER_INFO")) { %>
    <script>
		var formDataInquirer = new Array (
			new Array ('name_cc_inquirer', 'varchar2', 1),
			new Array ('cd_cc_inquirer_state', 'varchar2', 1),
			new Array ('date_cc_inquirer', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataInquirer);
		}
	</script>
    
	<form action="../crm/call_center/inquirerupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= inquirer.getValue("ID_CC_INQUIRER") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_inquirer", false) %></td><td><input type="text" name="id_cc_inquirer" size="20" value="<%= inquirer.getValue("ID_CC_INQUIRER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(inquirer.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="40" value="<%= Bean.getClubShortName(inquirer.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_inquirer", true) %></td><td><input type="text" name="name_cc_inquirer" size="40" value="<%= inquirer.getValue("NAME_CC_INQUIRER") %>" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_inquirer_state", true) %></td><td><select name="cd_cc_inquirer_state" class="inputfield"><%= Bean.getCallCenterInquirerStateOptions(inquirer.getValue("CD_CC_INQUIRER_STATE"), false) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>			
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("date_cc_inquirer", true) %></td><td><%=Bean.getCalendarInputField("date_cc_inquirer", inquirer.getValue("DATE_CC_INQUIRER_FRMT"), "10") %></td>
			<td colspan="2">&nbsp;</td>
		</tr>			
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("desc_cc_inquirer", false) %></td><td  colspan="3"><textarea name="desc_cc_inquirer" cols="100" rows="3" class="inputfield"><%= inquirer.getValue("DESC_CC_INQUIRER") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				inquirer.getValue(Bean.getCreationDateFieldName()),
				inquirer.getValue("CREATED_BY"),
				inquirer.getValue(Bean.getLastUpdateDateFieldName()),
				inquirer.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/inquirerupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/call_center/inquirer.jsp") %>
			</td>
		</tr>
	</table>
	</form>	
	<%= Bean.getCalendarScript("date_cc_inquirer", false) %>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_INQUIRER_QUESTIONS")) {%>
   <table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("question_find", question_find, "../crm/call_center/inquirerspecs.jsp?id=" + id + "&question_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("question_line", "../crm/call_center/inquirerspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cd_cc_inquirer_line_type", false)) %>
			<%= Bean.getCallCenterInquirerLineTypeOptions(question_line, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
   <%=inquirer.getQuestionsHTML(question_find, question_line, l_question_page_beg, l_question_page_end) %>
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_INQUIRER_CLIENTS")) {%>
   <table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("client_find", client_find, "../crm/call_center/inquirerspecs.jsp?id=" + id + "&client_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("quest_type", "../crm/call_center/inquirerspecs.jsp?id=" + id + "&client_page=1", Bean.call_centerXML.getfieldTransl("cc_question_type", false)) %>
			<%= Bean.getCallCenterQuestionTypeOptions(quest_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		<%=Bean.getSelectOnChangeBeginHTML("quest_status", "../crm/call_center/inquirerspecs.jsp?id=" + id + "&client_page=1", Bean.call_centerXML.getfieldTransl("cc_question_status", false)) %>
			<%= Bean.getCallCenterQuestionStatusOptions(quest_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
   <%=inquirer.getClientsHTML(client_find, quest_type, quest_status, l_client_page_beg, l_client_page_end) %>
<% }

} %>
</div></div>
</body>
</html>
