<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcWarningObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
<body>
<div id="div_tabsheet">

<% 
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "ADMIN_WARNING";

Bean.setJspPageForTabName(pageFormName);

String id = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { 
	tab = Bean.tabsHmGetValue(pageFormName); 
}
if (id==null || "".equalsIgnoreCase(id)) {
%>

	<%=Bean.getIDNotFoundMessage() %>

<% }
else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcWarningObject warning = new bcWarningObject(id);
%>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader(Bean.warningXML.getfieldTransl("general", false), "../admin/warnings.jsp") %>
			<%= Bean.getMenuButton("ADD", "../admin/warningsupdate.jsp?id_warning=" + warning.getValue("ID_WARNING") + "&type=general&action=add2&process=no", "", "") %>
			<% if (Bean.loginUser.getValue("ID_USER").equalsIgnoreCase(warning.getValue("CREATED_BY"))) {%>
				<%= Bean.getMenuButton("DELETE", "../admin/warningsupdate.jsp?id_warning=" + warning.getValue("ID_WARNING") + "&type=general&action=remove&process=yes", Bean.warningXML.getfieldTransl("h_delete_warning", false), warning.getValue("ID_WARNING")) %>
			<% } %>
		</tr>
	</table>

	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(warning.getValue("ID_WARNING")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../admin/warningsspecs.jsp?id=" + warning.getValue("ID_WARNING")) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
	
<%
if (tab.equals("1")) {
%>
	<script>
		var formData = new Array (
			new Array ('desc_warning', 'varchar2', 1)
		);
	</script>
	<form action="../admin/warningsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.warningXML.getfieldTransl("type_warning", true) %></td> <td><select name="type_warning" class="inputfield"><%=Bean.getMeaningFromLookupNameOptions("SYS_WARNING_TYPE", warning.getValue("TYPE_WARNING"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.warningXML.getfieldTransl("status_warning", true) %></td> <td><select name="status_warning" class="inputfield"><%=Bean.getWarningStatusOptions(warning.getValue("STATUS_WARNING"), false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.warningXML.getfieldTransl("system_version", false) %></td> <td><input type="text" name="system_version" size="20" value="<%= warning.getValue("SYSTEM_VERSION") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.warningXML.getfieldTransl("name_menu_element", false) %></td> <td><select name="id_menu_element" class="inputfield"><%=Bean.getAllMenuOptions(warning.getValue("ID_MENU_ELEMENT_RELATED"), true) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.warningXML.getfieldTransl("desc_warning", true) %></td><td valign="top"><textarea name="desc_warning" cols="90" rows="8" class="inputfield"><%= warning.getValue("DESC_WARNING") %></textarea></td>			
		</tr>
		<tr>
			<td valign="top"><%= Bean.warningXML.getfieldTransl("realization", false) %></td><td valign="top"><textarea name="realization" cols="90" rows="4" class="inputfield"><%= warning.getValue("REALIZATION") %></textarea></td>			
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				"",
				"1",
				warning.getValue("ID_WARNING"),
				"",
				warning.getValue(Bean.getCreationDateFieldName()),
				"",
				warning.getValue("CREATED_BY"),
				"",
				warning.getValue(Bean.getLastUpdateDateFieldName()),
				"",
				warning.getValue("LAST_UPDATE_BY"),
				""
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../admin/warningsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../admin/warnings.jsp") %>
			</td>
		</tr>
		
	</table>
	</form>


<%  } 
}%>
</div></div>
</body>
</html>
