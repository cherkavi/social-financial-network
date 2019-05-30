<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
</head>
<body>

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CALL_CENTER_FAQ";
String tagQuestions = "_QUESTIONS";
String tagQuestionFind = "_QUESTION_FIND";
String tagQuestionContactType = "_QUESTION_CONTACT_TYPE";
String tagQuestionType = "_QUESTION_TYPE";
String tagQuestionStatus = "_QUESTION_STATUS";

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
	
	bcCallCenterFAQObject faq = new bcCallCenterFAQObject(id);

	//Обрабатываем номера страниц
	String l_question_page = Bean.getDecodeParam(parameters.get("question"));
	Bean.pageCheck(pageFormName + tagQuestions, l_question_page);
	String l_question_page_beg = Bean.getFirstRowNumber(pageFormName + tagQuestions);
	String l_question_page_end = Bean.getLastRowNumber(pageFormName + tagQuestions);
	
	String question_find 	= Bean.getDecodeParam(parameters.get("question_find"));
	question_find 	= Bean.checkFindString(pageFormName + tagQuestionFind, question_find, l_question_page);

	String question_contact_type 	= Bean.getDecodeParam(parameters.get("question_contact_type"));
	question_contact_type 	= Bean.checkFindString(pageFormName + tagQuestionContactType, question_contact_type, l_question_page);

	String question_type 	= Bean.getDecodeParam(parameters.get("question_type"));
	question_type 	= Bean.checkFindString(pageFormName + tagQuestionType, question_type, l_question_page);

	String question_status 	= Bean.getDecodeParam(parameters.get("question_status"));
	question_status 	= Bean.checkFindString(pageFormName + tagQuestionStatus, question_status, l_question_page);
	
%>
  <% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
<body topmargin="0">
<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_FAQ_INFO")) {%>
			    <%= Bean.getMenuButton("ADD", "../crm/call_center/faqupdate.jsp?id=" + faq.getValue("ID_CC_FAQ") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/call_center/faqupdate.jsp?id=" + faq.getValue("ID_CC_FAQ") + "&type=general&action=remove&process=yes", Bean.call_centerXML.getfieldTransl("h_delete_question", false), faq.getValue("ID_CC_FAQ") + " - " +  faq.getValue("TITLE_CC_FAQ")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_FAQ_INCIDENTS")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagQuestions, "../crm/call_center/faqspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_FAQ_INCIDENTS")+"&", "question") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(faq.getValue("ID_CC_FAQ") + " - " + faq.getValue("TITLE_CC_FAQ")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/call_center/faqspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% 
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CALL_CENTER_FAQ_INFO")) {%> 
	<table <%=Bean.getTableDetailParam() %>> 
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_faq", false) %> </td>
			<td>
				<input type="text" name="id_cc_faq" size="20" value="<%= faq.getValue("ID_CC_FAQ") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(faq.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(faq.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_faq", false) %></td><td><input type="text" name="cd_cc_faq" size="20" value="<%= faq.getValue("CD_CC_FAQ") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("exist_flag", false) %></td><td><input type="text" name="exist_flag" size="20" value="<%= faq.getValue("EXIST_FLAG_TSL") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_faq_category", false) %></td><td><input type="text" name="id_cc_faq_category" size="50" value="<%= faq.getValue("NAME_CC_FAQ_CATEGORY") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("title_cc_faq", false) %></td><td  colspan="3"><input type="text" name="title_cc_faq" size="125" value="<%= faq.getValue("TITLE_CC_FAQ") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("question_cc_faq", false) %></td><td  colspan="3"><textarea name="question_cc_faq" cols="120" rows="2" readonly="readonly" class="inputfield-ro"><%= faq.getValue("QUESTION_CC_FAQ") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("answer_cc_faq", false) %></td><td  colspan="3"><textarea name="answer_cc_faq" cols="120" rows="6" readonly="readonly" class="inputfield-ro"><%= faq.getValue("ANSWER_CC_FAQ") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				faq.getValue(Bean.getCreationDateFieldName()),
				faq.getValue("CREATED_BY"),
				faq.getValue(Bean.getLastUpdateDateFieldName()),
				faq.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getGoBackButton("../crm/call_center/faq.jsp") %>
			</td>
		</tr>
	</table>
<% 	} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_FAQ_INFO")) { %>
	<script>
		var formDataFAQ = new Array (
			new Array ('id_cc_faq_category', 'varchar2', 1),
			new Array ('cd_cc_faq', 'varchar2', 1),
			new Array ('exist_flag', 'varchar2', 1),
			new Array ('title_cc_faq', 'varchar2', 1),
			new Array ('question_cc_faq', 'varchar2', 1),
			new Array ('answer_cc_faq', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataFAQ);
		}
	</script>

    <form action="../crm/call_center/faqupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= faq.getValue("ID_CC_FAQ") %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_faq", false) %> </td>
			<td>
				<input type="text" name="id_cc_faq" size="20" value="<%= faq.getValue("ID_CC_FAQ") %>" readonly="readonly" class="inputfield-ro">
			</td>
			<td><%= Bean.clubXML.getfieldTransl("club", false) %>
				<%= Bean.getGoToClubLink(faq.getValue("ID_CLUB")) %>
			</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(faq.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_faq", true) %></td><td><input type="text" name="cd_cc_faq" size="20" value="<%= faq.getValue("CD_CC_FAQ") %>" class="inputfield"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("exist_flag", true) %></td><td><select name="exist_flag" class="inputfield"><%= Bean.getYesNoLookupOptions(faq.getValue("EXIST_FLAG"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_faq_category", true) %></td><td><select name="id_cc_faq_category" class="inputfield"><%= Bean.getCallCenterFAQCategoryOptions(faq.getValue("ID_CC_FAQ_CATEGORY"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("title_cc_faq", true) %></td><td  colspan="3"><input type="text" name="title_cc_faq" size="125" value="<%= faq.getValue("TITLE_CC_FAQ") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("question_cc_faq", true) %></td><td  colspan="3"><textarea name="question_cc_faq" cols="120" rows="2" class="inputfield"><%= faq.getValue("QUESTION_CC_FAQ") %></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("answer_cc_faq", true) %></td><td  colspan="3"><textarea name="answer_cc_faq" cols="120" rows="6" class="inputfield"><%= faq.getValue("ANSWER_CC_FAQ") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				faq.getValue(Bean.getCreationDateFieldName()),
				faq.getValue("CREATED_BY"),
				faq.getValue(Bean.getLastUpdateDateFieldName()),
				faq.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/faqupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/call_center/faq.jsp") %>
			</td>
		</tr>
	</table>
	</form>
	
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_FAQ_INCIDENTS")) {%>


	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("question_find", question_find, "../crm/call_center/faqspecs.jsp?id=" + id + "&question_page=1") %>
		
		<%=Bean.getSelectOnChangeBeginHTML("question_contact_type", "../crm/call_center/faqspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cc_contact_type", false)) %>
			<%= Bean.getCallCenterContactTypeOptions(question_contact_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		
		<%=Bean.getSelectOnChangeBeginHTML("question_type", "../crm/call_center/faqspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cc_question_type", false)) %>
			<%= Bean.getCallCenterQuestionTypeOptions(question_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		
		<%=Bean.getSelectOnChangeBeginHTML("question_status", "../crm/call_center/faqspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cc_question_status", false)) %>
			<%= Bean.getCallCenterQuestionStatusOptions(question_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  	</tr>
	</table>



	<%=faq.getCallCenterQuestionsHTML(question_find, question_contact_type, question_type, question_status, l_question_page_beg, l_question_page_end) %>
<% }
} %>
</div></div>
</body>


<%@page import="bc.objects.bcCallCenterFAQObject"%>
<%@page import="java.util.HashMap"%></html>
