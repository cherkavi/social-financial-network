<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<% 

String pageFormName = "CLIENTS_CONTACT_PRS";
String tagFind = "_FIND";
String tagType = "_TYPE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 		= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 			= Bean.getDecodeParam(parameters.get("page"));
String cd_type_filtr 	= Bean.getDecodeParam(parameters.get("cd_type_filtr"));

find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
cd_type_filtr 	= Bean.checkFindString(pageFormName + tagType, cd_type_filtr, l_page);

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
<% StringBuilder html = new StringBuilder();
   html.append(Bean.header.getContactPrsHeadHTML(cd_type_filtr, find_string, l_beg, l_end, print));
%>

<% if ("N".equalsIgnoreCase(print)) { %>
	<table <%= Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.hasEditMenuPermission(pageFormName)) { %>
		    <%= Bean.getMenuButton("ADD", "../crm/clients/contact_prsupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/clients/contact_prs.jsp?print=Y", "", "") %>

		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/clients/contact_prs.jsp?", "page", Bean.header.getLastResultSetRowCount()) %>
		</tr>
	</table>
	
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/clients/contact_prs.jsp?page=1&") %>
	
			<%=Bean.getSelectOnChangeBeginHTML("cd_type_filtr", "../crm/clients/contact_prs.jsp?page=1", Bean.contactXML.getfieldTransl("name_post", false)) %>
				<%= Bean.getContactPrsTypeOptions(cd_type_filtr, true) %>
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
<% if (Bean.hasAccessMenuPermission(pageFormName)) { %>
	<%= html.toString() %>
<%} %>
</div></div>
</body>
</html>