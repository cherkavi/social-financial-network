<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.io.files"%>
<%@page import="bc.AppConst"%>
<%@page import="java.util.HashMap"%><html>
<head>
	<%=Bean.getMetaContent() %>
	<%=Bean.getTopFrameCSS() %>
    <%=Bean.getTableSorterCSS("[0,0],[2,1]") %>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FORM_HELP";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id_menu = Bean.getDecodeParam(parameters.get("id_menu"));
String id_tab = Bean.getDecodeParam(parameters.get("id_tab"));

%>

</head>
<body>
Страница в разработке
<table class="tablesorter">
	<tr><td colspan="2"><%= Bean.buttonXML.getfieldTransl("button_help", false) %></td></tr>
	<tr><td><%= Bean.warningXML.getfieldTransl("id_menu", false) %></td> <td><%=Bean.getMenuName(id_menu) %>&nbsp;</td></tr>
	<tr><td><%= Bean.warningXML.getfieldTransl("id_tab", false) %></td> <td><%=Bean.getMenuName(id_tab) %>&nbsp;</td></tr>
</table>

</body>
</html>