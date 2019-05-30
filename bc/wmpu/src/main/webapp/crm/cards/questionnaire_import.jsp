<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

String pageFormName = "CARDS_QUESTIONNAIRE_IMPORT";
String tagProfile = "_PROFILE";
String tagFiltrQuest = "_FILTRQUEST";
String tagOperationType = "_OPERATION_TYPE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specId"));
String l_page 			= Bean.getDecodeParam(parameters.get("page"));
String id_profile 		= Bean.getDecodeParam(parameters.get("id_profile"));
String filtr_quest 		= Bean.getDecodeParam(parameters.get("filtr_quest"));

id_profile 		= Bean.checkFindString(pageFormName + tagProfile, id_profile, l_page);

filtr_quest 	= Bean.checkFindString(pageFormName + tagFiltrQuest, filtr_quest, l_page);

String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
operation_type 			= Bean.checkFindString(pageFormName + tagOperationType, operation_type, l_page);

//Обрабатываем номера страниц
Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

String print        = Bean.checkPrint(Bean.getDecodeParam(parameters.get("print")));

%>
<head>
	<%=Bean.getJSPHeadHTML(print) %>
</head>
<body>
<div id="div_tabsheet">
<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
			<%= Bean.getMenuButton("ADD", "../crm/cards/questionnaire_importupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/cards/questionnaire_import.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/cards/questionnaire_import.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("filtr_quest", filtr_quest, "../crm/cards/questionnaire_import.jsp?page=1&") %>
			
			<%=Bean.getSelectOnChangeBeginHTML("id_profile", "../crm/cards/questionnaire_import.jsp?page=1", Bean.questionnaireXML.getfieldTransl("import_state_pack", false)) %>
				<%=Bean.getSelectOptionHTML(id_profile, "", "") %>
				<%=Bean.getSelectOptionHTML(id_profile, "NOT_IMPORTED", Bean.questionnaireXML.getfieldTransl("f_not_imported", false)) %>
				<%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("NAT_PRS_INT_QUEST_STATE", id_profile, false) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>
<% } else { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeaderPrint() %>
		</tr>
	</table>
<% } %>
</div>
<div id="div_data">
<div id="div_data_detail">

<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
	<% Bean.header.setDeleteHyperLink("../crm/cards/questionnaire_importupdate.jsp?type=QUEST&action=remove&process=yes&id=",Bean.questionnaireXML.getfieldTransl("h_remove_questionnaire", false),"ID_QUEST_INT"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

     <script type="text/javascript">
     	function CheckSelect(form)  {
     		if (confirm('<%= Bean.questionnaireXML.getfieldTransl("h_confirm_set_discount_card_interchange", false) %> ?'))  {
				return true;
			} else {
				return false;
			}
   	 	}

     	function CheckCB(Element) {
	   	 	myCheck = true;
	
			thisCheckBoxes = document.getElementsByTagName('input');
			for (i = 1; i < thisCheckBoxes.length; i++) { 
				myName = thisCheckBoxes[i].name;
				if (myName.substr(0,3) == 'chb'){
					myCheck = myCheck && thisCheckBoxes[i].checked;
				}
			}
			if (document.getElementById('mainCheck')) {
				document.getElementById('mainCheck').checked = myCheck;
			}
   	 	}

   	 	function CheckAll(Element,Name) {
   	 		thisCheckBoxes = document.getElementsByTagName('input');
   	 		for (i = 1; i < thisCheckBoxes.length; i++) {
   				myName = thisCheckBoxes[i].name;
   				
   				if (myName.substr(0,3) == Name){
 					thisCheckBoxes[i].checked = Element.checked;
   				}
   	 		}
   	 	}
   	 </script>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
    <% Bean.header.setDeleteHyperLink("","",""); %>
	<%= Bean.header.getQuestionnairesHTML(operation_type, filtr_quest, id_profile, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>