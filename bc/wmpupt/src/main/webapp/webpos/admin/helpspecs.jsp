<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>

<html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBPOS_HELP";

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab == null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
}
Bean.tabsHmSetValue(pageFormName, tab);

String id_menu			= Bean.getDecodeParam(parameters.get("id_menu"));

%>
<head>
</head>

<body>
	<div id="div_action_big">
				<h1><%=Bean.webposXML.getfieldTransl("title_setting_help", false) %>:&nbsp;<%=id_menu %></h1>
				<form name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="type" value="coupon">
			        <input type="hidden" name="action" value="check">
			        <input type="hidden" name="process" value="yes">
			        <input type="hidden" name="id_menu" value="<%=id_menu %>">
					<table class="action_table">
			  			<tr><td>Menu context</td></tr>
					</table>
				</form>
				</div>
</body>
</html>