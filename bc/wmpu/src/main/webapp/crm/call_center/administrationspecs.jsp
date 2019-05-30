<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcCallCenterUserObject"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body>
<div id="div_tabsheet">

<% 

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CALL_CENTER_ADMINISTRATION";
String tagMessage = "_MESSAGES";
String tagMessageFind = "_MESSAGE_FIND";
String tagMessageType = "_MESSAGE_TYPE";
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
	
	bcCallCenterUserObject cc_user = new bcCallCenterUserObject(id);

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
	
	String l_message_page = Bean.getDecodeParam(parameters.get("message_page"));
	Bean.pageCheck(pageFormName + tagMessage, l_message_page);
	String l_message_page_beg = Bean.getFirstRowNumber(pageFormName + tagMessage);
	String l_message_page_end = Bean.getLastRowNumber(pageFormName + tagMessage);

	String message_find 	= Bean.getDecodeParam(parameters.get("message_find"));
	message_find 	= Bean.checkFindString(pageFormName + tagMessageFind, message_find, l_message_page);

	String message_type	= Bean.getDecodeParam(parameters.get("message_type"));
	message_type		= Bean.checkFindString(pageFormName + tagMessageType, message_type, l_message_page);
	
%>
  <% Bean.currentMenu.setCurrentTab(Bean.currentMenu.getTabSheetName(tab)); %>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_ADMINISTRATION_INFO")) {%>
			    <%= Bean.getMenuButton("ADD", "../crm/call_center/administrationupdate.jsp?id=" + cc_user.getValue("ID_USER") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/call_center/administrationupdate.jsp?id=" + cc_user.getValue("ID_USER") + "&type=general&action=remove&process=yes", Bean.call_centerXML.getfieldTransl("h_delete_user", false), cc_user.getValue("ID_USER") + " - " +  cc_user.getValue("NAME_USER")) %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_ADMINISTRATION_QUESTIONS")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagQuestions, "../crm/call_center/administrationspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_ADMINISTRATION_QUESTIONS")+"&", "question") %>
			<% } %>

			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_ADMINISTRATION_MESSAGES")) {%>
				<!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagMessage, "../crm/call_center/administrationspecs.jsp?id=" + id + "&tab="+Bean.currentMenu.getTabID("CALL_CENTER_ADMINISTRATION_MESSAGES")+"&", "message_page") %>
			<% } %>

		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(cc_user.getValue("ID_USER") + " - " + cc_user.getValue("NAME_USER")) %>
		<tr>
			<td>
			<!-- Выводим перечень закладок -->
			<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/call_center/administrationspecs.jsp?id=" + id) %>
		</td>
	</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<% 
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("CALL_CENTER_ADMINISTRATION_INFO")) {%> 
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("ID_USER", false) %></td><td><input type="text" name="name_user" size="30" value="<%= cc_user.getValue("NAME_USER") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_user_question_type", false) %></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.call_centerXML.getfieldTransl("cc_user_status", false) %></td><td valign=top><input type="text" name="cc_user_status" size="30" value="<%= cc_user.getValue("NAME_CC_USER_STATUS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td>
				<select name="cd_cc_question_type" id="destination" multiple="multiple" size="7" class="inputfield-ro"><%= Bean.getCallCenterUserQuestionTypeOptions(cc_user.getValue("ID_USER"), false) %></select>
			</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				cc_user.getValue(Bean.getCreationDateFieldName()),
				cc_user.getValue("CREATED_BY"),
				cc_user.getValue(Bean.getLastUpdateDateFieldName()),
				cc_user.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
		    <td colspan="4" class="top_line"><b><%=Bean.commonXML.getfieldTransl("h_record_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.commonXML.getfieldTransl("creation_date", false)%></td> <td><input type="text" name="creation_date" size="20" value="<%=cc_user.getValue(Bean.getCreationDateFieldName())%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.commonXML.getfieldTransl("last_update_date",false)%></td> <td><input type="text" name="last_update_date" size="20" value="<%=cc_user.getValue(Bean.getLastUpdateDateFieldName())%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.commonXML.getfieldTransl("created_by", false)%></td> <td><input type="text" name="created_by" size="20" value="<%=Bean.getSystemUserName(cc_user.getValue("CREATED_BY"))%>" readonly="readonly" class="inputfield-ro"> </td>
			<td><%=Bean.commonXML.getfieldTransl("last_update_by", false)%></td> <td><input type="text" name="last_update_by" size="20" value="<%=Bean.getSystemUserName(cc_user.getValue("LAST_UPDATE_BY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="7" align="center">
				<%=Bean.getGoBackButton("../crm/call_center/administration.jsp") %>
			</td>
		</tr>
		
	</table>
<% 	} else if (Bean.currentMenu.isCurrentTabAndEditPermitted("CALL_CENTER_ADMINISTRATION_INFO")) { %>
	<script>
	// Скрипты для пользователей Call-центра
	 var prefix="p_";
	 var suffixCounter=0;
	 function move(source, destination,method){
	  for(var counter=(source.options.length-1);counter>=0;counter--){
	   if(source.options[counter].selected){
	    var element=source.options[counter];
	    source.removeChild(element);
	    destination.appendChild(element);

	    method(element);
	   }
	  }
	 }

	 function removeElementFromForm(element){
		  var formDestination=document.getElementById("p_span");
		  var elem = document.getElementById(prefix + element.value);
		  formDestination.removeChild(document.getElementById(elem.name));
	 }

	 function addElementToForm(element){
	  var elementText=element.text;
	  var hiddenElement=document.createElement("input");
	  hiddenElement.name=prefix+element.getAttribute("value");//prefix+suffixCounter;
	  hiddenElement.id=prefix+element.getAttribute("value");//prefix+suffixCounter;
	  hiddenElement.type="hidden";
	  hiddenElement.value=element.getAttribute("value");
	  formDestination=document.getElementById("p_span");
	  formDestination.appendChild(hiddenElement);
	 }


	 function fromSource(){
	  var source=document.getElementById("source");
	  var destination=document.getElementById("destination");
	  move(source,destination,addElementToForm);
	 }

	 function fromDestination(){
	  var source=document.getElementById("destination");
	  var destination=document.getElementById("source");
	  move(source,destination,removeElementFromForm);
	 }
	 
			
	</script>
	<script>
		var formDataAdministration = new Array (
			new Array ('name_user', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataAdministration);
		}
	</script>
    
	<form action="../crm/call_center/administrationupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="GET" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id" value="<%= cc_user.getValue("ID_USER") %>">
        <input type="hidden" name="id_user" id="id_user" value="<%= cc_user.getValue("ID_USER") %>">
		<span id="p_span"><%= Bean.getCallCenterUserQuestionTypeHidden(cc_user.getValue("ID_USER")) %></span>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("ID_USER", true) %></td>
			<td>
				<%=Bean.getWindowFindUser("user", cc_user.getValue("ID_USER"), cc_user.getValue("NAME_USER"), "10") %>
			</td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_all_question_type", false) %></td>
			<td rowspan="2" align="center">
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("add", false) %>" onclick="fromSource()" class="inputfield2"><br>
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("delete", false) %>" onclick="fromDestination()" class="inputfield2">
			</td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_user_question_type", false) %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.call_centerXML.getfieldTransl("cc_user_status", true) %></td><td valign="top"><select name="cd_cc_user_status" class="inputfield"><%= Bean.getCallCenterUserStatusOptions(cc_user.getValue("CD_CC_USER_STATUS"), false) %></select></td>
			<td width="20">
				<select name="cd_cc_question_type" id="source" multiple="multiple" size="7" class="inputfield"><%= Bean.getCallCenterQuestionTypeUserOptions(cc_user.getValue("ID_USER"), false) %></select>
			</td>
			<td>
				<select name="cd_cc_question_type" id="destination" multiple="multiple" size="7" class="inputfield"><%= Bean.getCallCenterUserQuestionTypeOptions(cc_user.getValue("ID_USER"), false) %></select>
			</td>
		</tr>
		<tr>
		    <td colspan=6 class="top_line"><b><%=Bean.commonXML.getfieldTransl("h_record_param", false)%></b></td>
		</tr>
		<tr>
			<td><%=Bean.commonXML.getfieldTransl("creation_date", false)%></td> <td><input type="text" name="creation_date" size="20" value="<%=cc_user.getValue(Bean.getCreationDateFieldName())%>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2"><%=Bean.commonXML.getfieldTransl("last_update_date",false)%></td> <td  colspan="3"><input type="text" name="last_update_date" size="20" value="<%=cc_user.getValue(Bean.getLastUpdateDateFieldName())%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%=Bean.commonXML.getfieldTransl("created_by", false)%></td> <td><input type="text" name="created_by" size="20" value="<%=Bean.getSystemUserName(cc_user.getValue("CREATED_BY"))%>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2"><%=Bean.commonXML.getfieldTransl("last_update_by", false)%></td> <td  colspan="3"><input type="text" name="last_update_by" size="20" value="<%=Bean.getSystemUserName(cc_user.getValue("LAST_UPDATE_BY"))%>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="7" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/administrationupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/call_center/administration.jsp") %>
			</td>
		</tr>

	</table>	
	</form>
<%}

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_ADMINISTRATION_QUESTIONS")) {%>


	<table <%=Bean.getTableBottomFilter() %>>
	  	<tr>
		<%= Bean.getFindHTML("question_find", question_find, "../crm/call_center/administrationspecs.jsp?id=" + id + "&question_page=1") %>
		
		<%=Bean.getSelectOnChangeBeginHTML("question_contact_type", "../crm/call_center/administrationspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cc_contact_type", false)) %>
			<%= Bean.getCallCenterContactTypeOptions(question_contact_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		
		<%=Bean.getSelectOnChangeBeginHTML("question_type", "../crm/call_center/administrationspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cc_question_type", false)) %>
			<%= Bean.getCallCenterQuestionTypeOptions(question_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		
		<%=Bean.getSelectOnChangeBeginHTML("question_status", "../crm/call_center/administrationspecs.jsp?id=" + id + "&question_page=1", Bean.call_centerXML.getfieldTransl("cc_question_status", false)) %>
			<%= Bean.getCallCenterQuestionStatusOptions(question_status, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>

	  	</tr>
	</table>



	<%=cc_user.getCallCenterQuestionsHTML(question_find, question_contact_type, question_type, question_status, l_question_page_beg, l_question_page_end) %>
<% }

if (Bean.currentMenu.isCurrentTabAndAccessPermitted("CALL_CENTER_ADMINISTRATION_MESSAGES")) {%>
	<table <%= Bean.getTableBottomFilter() %>>
		<tr>
		<%= Bean.getFindHTML("message_find", message_find, "../crm/call_center/administrationspecs.jsp?id=" + id + "&message_page=1") %>

		<%=Bean.getSelectOnChangeBeginHTML("message_type", "../crm/call_center/administrationspecs.jsp?id=" + id + "&message_page=1", Bean.messageXML.getfieldTransl("type_message", false)) %>
			<%= Bean.getMessagePatternTypeWitoutTerminals(message_type, true) %>
		<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
	<%=cc_user.getMessagesHTML(message_find, message_type, l_message_page_beg, l_message_page_end) %>
<% }

} %>
</div></div>
</body>

<%@page import="java.util.HashMap"%></html>
