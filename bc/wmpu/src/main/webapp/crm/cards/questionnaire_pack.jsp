<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");

String pageFormName = "CARDS_QUESTIONNAIRE_PACK";
String tagPackState = "_PACKSTATE";
String tagPackImportState = "_PACKIMPORTSTATE";
String tagFiltrPack = "_FILTRPACK";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specId"));
String l_page 			= Bean.getDecodeParam(parameters.get("page"));
String filtr_pack 		= Bean.getDecodeParam(parameters.get("filtr_pack"));
String state 			= Bean.getDecodeParam(parameters.get("state"));
String import_state 	= Bean.getDecodeParam(parameters.get("import_state"));

filtr_pack 		= Bean.checkFindString(pageFormName + tagFiltrPack, filtr_pack, l_page);
state 			= Bean.checkFindString(pageFormName + tagPackState, state, l_page);
import_state 	= Bean.checkFindString(pageFormName + tagPackImportState, import_state, l_page);

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
			<%= Bean.getMenuButton("ADD", "../crm/cards/questionnaire_packupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/cards/questionnaire_pack.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/cards/questionnaire_pack.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("filtr_pack", filtr_pack, "../crm/cards/questionnaire_pack.jsp?page=1&") %>
			
			<%=Bean.getSelectOnChangeBeginHTML("state", "../crm/cards/questionnaire_pack.jsp?page=1", Bean.questionnaireXML.getfieldTransl("state_pack", false)) %>
				<%= Bean.getMeaningFromLookupNameOptions("NAT_PRS_INT_PACK_STATE", state, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("import_state", "../crm/cards/questionnaire_pack.jsp?page=1", Bean.questionnaireXML.getfieldTransl("import_state_pack", false)) %>
				<%= Bean.getMeaningFromLookupNameOptions("NAT_PRS_INT_PACK_IMPORT_STATE", import_state, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/cards/questionnaire_packupdate.jsp?type=PACK&action=remove&process=yes&id=",Bean.questionnaireXML.getfieldTransl("h_pack_delete", false),"ID_QUEST_PACK", "DATE_RECEPTION_PACK"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getQuestionnairePackHeadHTML(filtr_pack, state, import_state, l_beg, l_end, print) %>	
<%} %>
</div></div>
</body>
</html>