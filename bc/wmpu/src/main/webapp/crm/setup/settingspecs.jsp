<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcSystemSettingObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SETUP_SETTINGS";

Bean.setJspPageForTabName(pageFormName);

String settingid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }
if (settingid==null) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcSystemSettingObject setting = new bcSystemSettingObject(settingid);
%>

<body topmargin="0">
<div id="div_tabsheet">
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(setting.getValue("NAME_PARAM")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/setup/settingspecs.jsp?id=" + settingid) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">

<%
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SETUP_SETTINGS_INFO")) {
	boolean hasEditPerm = false;
	if (Bean.currentMenu.isCurrentTabAndEditPermitted("SETUP_SETTINGS_INFO")) {
		hasEditPerm = true;
	}
%>

<%if (hasEditPerm) { %>
		<script>
			var formData = new Array (
				new Array ('VALUE_PARAM', 'varchar2', 1)
			);
		</script>

	  <form action="../crm/setup/settingsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="id" value="<%= settingid %>">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.settingXML.getfieldTransl("CD_PARAM", false) %></td> <td><input type="text" name="CD_PARAM" size="40" value="<%= setting.getValue("CD_PARAM") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.settingXML.getfieldTransl("NAME_PARAM", false) %> </td><td><input type="text" name="NAME_PARAM" size="100" value="<%= setting.getValue("NAME_PARAM") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<% if (setting.getValue("NAME_LOOKUP_TYPE")==null || "".equalsIgnoreCase(setting.getValue("NAME_LOOKUP_TYPE"))) { %>
     		  <td><%= Bean.settingXML.getfieldTransl("VALUE_PARAM", true) %></td> <td><input type="text" name="VALUE_PARAM" size="100" value="<%= setting.getValue("VALUE_PARAM") %>" class="inputfield"> </td>
     		<%} else {%>
     		  <td><%= Bean.settingXML.getfieldTransl("VALUE_PARAM", false) %></td> <td><select name="VALUE_PARAM" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions(setting.getValue("NAME_LOOKUP_TYPE"), setting.getValue("VALUE_PARAM"), false) %></select> </td>
     		<%} %>
		</tr>
		<tr>
 			<td><%= Bean.settingXML.getfieldTransl("DATE_PARAM", false) %></td> <td><input type="text" name="DATE_PARAM" size="16" value="<%= setting.getValue("DATE_PARAM_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/setup/settingsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/setup/settings.jsp") %>
			</td>
		</tr>
	</table>
	<%} else {%>    
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.settingXML.getfieldTransl("CD_PARAM", false) %></td> <td><input type="text" name="CD_PARAM" size="40" value="<%= setting.getValue("CD_PARAM") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td><%= Bean.settingXML.getfieldTransl("NAME_PARAM", false) %> </td><td><input type="text" name="NAME_PARAM" size="100" value="<%= setting.getValue("NAME_PARAM") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
     		<td><%= Bean.settingXML.getfieldTransl("VALUE_PARAM", false) %></td> <td><input type="text" name="VALUE_PARAM" size="100" value="<%= setting.getValue("VALUE_PARAM") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
 			<td><%= Bean.settingXML.getfieldTransl("DATE_PARAM", false) %></td> <td><input type="text" name="DATE_PARAM" size="16" value="<%= setting.getValue("DATE_PARAM_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/setup/settings.jsp") %>
			</td>
		</tr>

	<%} %>	
	</table>
	</form>

<%  }
%>

<%    } %>

</div></div>
</body>
</html>
