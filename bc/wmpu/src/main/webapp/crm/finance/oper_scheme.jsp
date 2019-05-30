<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "FINANCE_OPER_SCHEME";
String tagFind = "_FIND";
String tagOperExecType = "_OPER_EXEC_TYPE";
String tagOperState = "_OPER_STATE";

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 			= Bean.getDecodeParamPrepare(parameters.get("specId"));
String find_string 		= Bean.getDecodeParam(parameters.get("find_string"));
String l_page 			= Bean.getDecodeParam(parameters.get("page"));
String oper_exec_type	= Bean.getDecodeParam(parameters.get("oper_exec_type"));
String oper_state		= Bean.getDecodeParam(parameters.get("oper_state"));

find_string 		= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);
oper_exec_type 		= Bean.checkFindString(pageFormName + tagOperExecType, oper_exec_type, l_page);
oper_state	 		= Bean.checkFindString(pageFormName + tagOperState, oper_state, l_page);

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
		    <%= Bean.getMenuButton("ADD", "../crm/finance/oper_schemeupdate.jsp?type=general&action=add&process=no", "", "") %>
			<% } %>
		    <%= Bean.getMenuButton("PRINT", "../crm/finance/oper_scheme.jsp?print=Y", "", "") %>

	    	<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/finance/oper_scheme.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/finance/oper_scheme.jsp?page=1&") %>
	
	        <%=Bean.getSelectOnChangeBeginHTML("oper_exec_type", "../crm/finance/oper_scheme.jsp?page=1", Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_exec_type", false)) %>
				<%=Bean.getFNOperExecTypeOptions(oper_exec_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
	        <%=Bean.getSelectOnChangeBeginHTML("oper_state", "../crm/finance/oper_scheme.jsp?page=1", Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_state", false)) %>
				<%=Bean.getFNOperStateOptions(oper_state, true) %>
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
	<% Bean.header.setDeleteHyperLink("../crm/finance/oper_schemeupdate.jsp?type=general&action=remove&process=yes&id=",Bean.buttonXML.getfieldTransl("delete", false),"ID_BK_OPERATION_SCHEME"); %>
<%} else { %>
	<% Bean.header.setDeleteHyperLink("","",""); %>
<%} %>

<% if (Bean.hasAccessMenuPermission(pageFormName)) {%>
	<%= Bean.header.getOperSchemeHeadHTML(oper_exec_type, oper_state, find_string, l_beg, l_end, print) %>
<%} %>
</div></div>
</body>
</html>