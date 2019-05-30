<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>

<%

String pageFormName = "LOGISTIC_CLIENTS_PROMOTERS";
String tagFind = "_FIND";
String tagState = "_STATE";
String tagPost = "_POST";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);

String cd_state			= Bean.getDecodeParam(parameters.get("cd_state"));
cd_state 				= Bean.checkFindString3(pageFormName + tagState, cd_state, "WORKS", l_page);

String cd_post			= Bean.getDecodeParam(parameters.get("cd_post"));
cd_post 				= Bean.checkFindString3(pageFormName + tagPost, cd_post, "", l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/logistic/clients/promoterupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/logistic/clients/promoters.jsp?print=Y", "", "") %>
			
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/logistic/clients/promoters.jsp?", "page") %>
		</tr>
	</table>
	
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/logistic/clients/promoters.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("cd_post", "../crm/logistic/clients/promoters.jsp?page=1", Bean.logisticXML.getfieldTransl("cd_lg_promoter_post", false)) %>
				<%= Bean.getLogisticPromoterPostOptions(cd_post, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<%=Bean.getSelectOnChangeBeginHTML("cd_state", "../crm/logistic/clients/promoters.jsp?page=1", Bean.logisticXML.getfieldTransl("cd_lg_promoter_state", false)) %>
				<%= Bean.getSelectOptionHTML(cd_state, "", "") %>
				<%= Bean.getSelectOptionHTML(cd_state, "WORKS", Bean.logisticXML.getfieldTransl("cd_lg_promoter_state_work", false)) %>
				<%= Bean.getLogisticPromoterStateOptions(cd_state, false) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/logistic/clients/promoterupdate.jsp?type=general&action=remove&process=yes&id=",Bean.commonXML.getfieldTransl("delete", false),"ID_LG_PROMOTER"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>
<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= Bean.header.getLogisticPromotersHTML(find_string, cd_post, cd_state, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>