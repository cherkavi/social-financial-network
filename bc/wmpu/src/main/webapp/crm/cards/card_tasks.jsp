<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "CARDS_CARD_TASKS";
String tagType = "_TYPE";
String tagState = "_STATE";
String tagFind = "_FIND";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specid"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String type 		= Bean.getDecodeParam(parameters.get("type"));
String state 		= Bean.getDecodeParam(parameters.get("state"));

find_string = Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
type 		= Bean.checkFindString(pageFormName + tagType, type, l_page);
state 		= Bean.checkFindString(pageFormName + tagState, state, l_page);

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
			    <%= Bean.getMenuButton("ADD", "../crm/cards/card_tasksupdate.jsp?type=general&action=add&process=no", "", "") %>
			    <%=Bean.getMenuButton("POSTING", "../crm/cards/card_tasksupdate.jsp?type=posting&action=run_all&process=yes&id_club="+Bean.getCurrentClubID(), Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_CARD_TASKS_ALL", false), "", Bean.postingXML.getfieldTransl("LAB_RUN_POSTINGS_CARD_TASKS_ALL", false))%>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/cards/card_tasks.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/cards/card_tasks.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/cards/card_tasks.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("type", "../crm/cards/card_tasks.jsp?page=1", Bean.card_taskXML.getfieldTransl("name_card_operation_type", false)) %>
				<%= Bean.getClubCardOperationTypeOptions(type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("state", "../crm/cards/card_tasks.jsp?page=1", Bean.card_taskXML.getfieldTransl("cd_card_oper_state", false)) %>
				<%= Bean.getClubCardOperationStateOptions(state, true) %>
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
<% Bean.header.setDeleteHyperLink("","",""); %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getCardTasksHeaderHTML(type, state, find_string, l_beg, l_end, print) %>
<%  
} %>
</div></div>
</body>
</html>