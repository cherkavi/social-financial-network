<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<%

String pageFormName = "SECURITY_ADMIN";
String tagFind = "_FIND";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String specId 		= Bean.getDecodeParamPrepare(parameters.get("specId"));
String l_page 		= Bean.getDecodeParam(parameters.get("page"));
String id_type 		= Bean.getDecodeParam(parameters.get("id_type"));

String find_string 	= Bean.getDecodeParam(parameters.get("find_string"));
find_string 	= Bean.checkFindString(pageFormName + tagFind, find_string, l_page);

Bean.pageCheck(pageFormName, l_page);
String l_beg = Bean.getFirstRowNumber(pageFormName);
String l_end = Bean.getLastRowNumber(pageFormName);

if (id_type==null || "".equalsIgnoreCase(id_type)) {
	id_type = "CONNECTIONS";
}

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

		    <%= Bean.getMenuButton("PRINT", "../crm/security/admin.jsp?print=Y", "", "") %>
	
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName, "../crm/security/admin.jsp?", "page") %>
		</tr>
	</table>
	<table <%= Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_string", find_string, "../crm/security/admin.jsp?page=1&") %>

			<td align="right" width="20">
			  	<select onchange="ajaxpage('../crm/security/admin.jsp?page=1&id_type='+getElementById('id_type').value, 'div_main')" name="id_type" id="id_type" class="inputfield">
					<option value="CONNECTIONS" <% if ("CONNECTIONS".equalsIgnoreCase(id_type)) { %>SELECTED<% } %>><%= Bean.clubXML.getfieldTransl("t_connections", false) %></option>
				</select>
			</td>
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
	<% if ("CONNECTIONS".equalsIgnoreCase(id_type)) { %>
	<%= Bean.header.getAdminConnectionsHeadHTML(find_string, l_beg, l_end, print) %>
	<% } %>
<%} %>
</div></div>
</body>
</html>