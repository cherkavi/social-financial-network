<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcRoleObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<Head>
<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SECURITY_ROLES";

Bean.setJspPageForTabName(pageFormName);

String tagUsers = "_USERS";
String tagUserFind = "_USER_FIND";
String tagUserStatus = "_USER_STATUS";

String tagMenuFind = "_MENU_FIND";
String tagMenuAccessType = "_MENU_ACCESS_TYPE";

String tagReports = "_REPORTS";
String tagIdMenuReports = "_REPORT_ID_MENU";
String tagReportFind = "_REPORT_FIND";
String tagReportModuleType = "_REPORT_MODULE_TYPE";
String tagReportKind = "_REPORT_KIND";
String tagReportHasPermission = "_REPORT_HAS_PERMISSION";

String roleid = Bean.getDecodeParam(parameters.get("id"));
String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }


if (roleid==null || "".equalsIgnoreCase(roleid)) {
%>
	<%=Bean.getIDNotFoundMessage() %>
<% 
} else {
	Bean.tabsHmSetValue(pageFormName, tab);
	bcRoleObject role = new bcRoleObject(roleid); 
	
	//Обрабатываем номера страниц
	String l_users_page = Bean.getDecodeParam(parameters.get("users_page"));
	Bean.pageCheck(pageFormName + tagUsers, l_users_page);
	String l_users_page_beg = Bean.getFirstRowNumber(pageFormName + tagUsers);
	String l_users_page_end = Bean.getLastRowNumber(pageFormName + tagUsers);
	
	String user_find 	= Bean.getDecodeParam(parameters.get("user_find"));
	user_find 	= Bean.checkFindString(pageFormName + tagUserFind, user_find, l_users_page);

	String user_status 	= Bean.getDecodeParam(parameters.get("user_status"));
	user_status 	= Bean.checkFindString(pageFormName + tagUserStatus, user_status, l_users_page);

	
	String menu_find 	= Bean.getDecodeParam(parameters.get("menu_find"));
	menu_find 	= Bean.checkFindString(pageFormName + tagMenuFind, menu_find, "");
	
	String menu_access_type 	= Bean.getDecodeParam(parameters.get("menu_access_type"));
	menu_access_type 	= Bean.checkFindString(pageFormName + tagMenuAccessType, menu_access_type, "");

	String l_report_page = Bean.getDecodeParam(parameters.get("report_page"));
	Bean.pageCheck(pageFormName + tagReports, l_report_page);
	String l_report_page_beg = Bean.getFirstRowNumber(pageFormName + tagReports);
	String l_report_page_end = Bean.getLastRowNumber(pageFormName + tagReports);

	String id_menu_report = Bean.getDecodeParam(parameters.get("id_menu_report"));
	id_menu_report 		= Bean.checkFindString(pageFormName + tagIdMenuReports, id_menu_report, l_report_page);

	String has_permission = Bean.getDecodeParam(parameters.get("has_permission"));
	has_permission 		= Bean.checkFindString(pageFormName + tagReportHasPermission, has_permission, l_report_page);

	String find_report = Bean.getDecodeParam(parameters.get("find_report"));
	find_report 		= Bean.checkFindString(pageFormName + tagReportFind, find_report, l_report_page);

	String module_type = Bean.getDecodeParam(parameters.get("module_type"));
	module_type 		= Bean.checkFindString(pageFormName + tagReportModuleType, module_type, l_report_page);

	String report_kind = Bean.getDecodeParam(parameters.get("report_kind"));
	report_kind 		= Bean.checkFindString(pageFormName + tagReportKind, report_kind, l_report_page);
%>
</head>
<body>
<div id="div_tabsheet">
	

	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>

			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_ROLES_INFO")) { %>
			<td>
			    <%= Bean.getMenuButton("ADD", "../crm/security/rolesupdate.jsp?id_role=" + role.getValue("ID_ROLE") + "&type=general&action=add2&process=no", "", "") %>
			    <%= Bean.getMenuButton("COPY", "../crm/security/rolesupdate.jsp?id_role=" + role.getValue("ID_ROLE") + "&type=general&action=copy&process=no", "", "") %>
			    <%= Bean.getMenuButton("DELETE", "../crm/security/rolesupdate.jsp?id_role=" + role.getValue("ID_ROLE") + "&type=general&action=remove&process=yes", Bean.roleXML.getfieldTransl("LAB_DELETE_ROLE", false), role.getValue("ID_ROLE") + " - " + role.getValue("NAME_ROLE")) %>
			</td>
			<% } %>
			
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_ROLES_USERS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_ROLES_USERS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/security/rolesupdate.jsp?id_role=" + role.getValue("ID_ROLE") + "&type=user&action=add&process=no", "", "") %>
				<% } %>
		
			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagUsers, "../crm/security/rolespecs.jsp?id=" + roleid + "&tab="+Bean.currentMenu.getTabID("SECURITY_ROLES_USERS")+"&", "users_page") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_ROLES_PRIVILEGES")) { %>
			    <%= Bean.getMenuButton("ADD", "../crm/security/rolesmenuupdate.jsp?id_role=" + role.getValue("ID_ROLE") + "&type=general&action=add&process=no", "", "") %>
			    <%= Bean.getMenuButton("EDIT_ALL", "../crm/security/rolesmenuupdate.jsp?id_role=" + role.getValue("ID_ROLE") + "&type=general&action=editall&process=no&role_page=1", "", "") %>
			<% } %>
	
			<% if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_ROLES_REPORTS")) { %>
				<% if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_ROLES_REPORTS")) { %>
				    <%= Bean.getMenuButton("ADD", "../crm/security/rolesreportupdate.jsp?id_role=" + role.getValue("ID_ROLE") + "&type=general&action=add&process=no", "", "") %>
				<% } %>

			    <!-- Вывод страниц -->
				<%= Bean.getPagesHTML(pageFormName + tagReports, "../crm/security/rolespecs.jsp?id=" + roleid + "&tab="+Bean.currentMenu.getTabID("SECURITY_ROLES_REPORTS")+"&", "report_page") %>
			<% } %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(role.getValue("ID_ROLE") + " - " + role.getValue("NAME_ROLE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/security/rolespecs.jsp?id=" + roleid) %>
			</td>
		</tr>
	</table>
</div>
<div id="div_data">
<div id="div_data_detail">
<%
if (Bean.currentMenu.isCurrentTabAndPreviewPermitted("SECURITY_ROLES_INFO")) {
 %>
	<form>
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_ROLE", false) %> </td><td><input type="text" name="textfield11" size="70" value="<%= role.getValue("NAME_ROLE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.roleXML.getfieldTransl("DESC_ROLE", false) %></td> <td colspan="4"><textarea name="field_describe" cols="67" rows="3" readonly="readonly" class="inputfield-ro"><%= role.getValue("DESC_ROLE") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>	
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("partner", false) %>
				<%=Bean.getGoToJurPrsHyperLink(role.getValue("ID_JUR_PRS")) %>
			</td><td><input type="text" name="partner" size="70" value="<%= role.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_MODULE_TYPE", false) %></td> <td><input type="text" name="NAME_MODULE_TYPE" size="20" value="<%= role.getValue("NAME_MODULE_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				"",
				"1",
				role.getValue("ID_ROLE"),
				"",
				role.getValue(Bean.getCreationDateFieldName()),
				"",
				role.getValue("CREATED_BY"),
				"",
				role.getValue(Bean.getLastUpdateDateFieldName()),
				"",
				role.getValue("LAST_UPDATE_BY"),
				""
			) %>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getGoBackButton("../crm/security/roles.jsp") %>
			</td>
		</tr>
	</table>
	</form>

<% } else if (Bean.currentMenu.isCurrentTabAndEditPermitted("SECURITY_ROLES_INFO")) { %>

	<script>
		var formData = new Array (
			new Array ('NAME_ROLE', 'varchar2', 1)
		);
	</script>

    <form action="../crm/security/rolesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
		<input type="hidden" name="id_role" value="<%= roleid %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_ROLE", true) %></td><td><input type="text" name="NAME_ROLE" size="70" value="<%= role.getValue("NAME_ROLE") %>"  class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	    <tr>
			<td><%= Bean.roleXML.getfieldTransl("DESC_ROLE", false) %></td> <td colspan="4"><textarea name="DESC_ROLE" cols="67" rows="3" class="inputfield"><%= role.getValue("DESC_ROLE") %></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>	
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("partner", false) %>
				<%=Bean.getGoToJurPrsHyperLink(role.getValue("ID_JUR_PRS")) %>
			</td><td><input type="text" name="partner" size="70" value="<%= role.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_MODULE_TYPE", false) %></td> <td><input type="text" name="NAME_MODULE_TYPE" size="20" value="<%= role.getValue("NAME_MODULE_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				"",
				"1",
				role.getValue("ID_ROLE"),
				"",
				role.getValue(Bean.getCreationDateFieldName()),
				"",
				role.getValue("CREATED_BY"),
				"",
				role.getValue(Bean.getLastUpdateDateFieldName()),
				"",
				role.getValue("LAST_UPDATE_BY"),
				""
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/security/rolesupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/security/roles.jsp") %>
			</td>
		</tr>

	</table>

</form> 
<%	
}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_ROLES_USERS")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("user_find", user_find, "../crm/security/rolespecs.jsp?id=" + roleid + "&users_page=1") %>
		

			<%=Bean.getSelectOnChangeBeginHTML("user_status", "../crm/security/rolespecs.jsp?id=" + roleid + "&users_page=1", Bean.contactXML.getfieldTransl("name_user_status", false)) %>
				<%= Bean.getUserStatusOptions(user_status, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>
	<%= role.getRoleUsersHTML(user_find, user_status, l_users_page_beg, l_users_page_end) %>
<%}
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_ROLES_PRIVILEGES")) {%>
		<table <%=Bean.getTableBottomFilter() %>><tbody>
		<tr>
			<%=Bean.getFindHTML("menu_find", menu_find, "../crm/security/rolespecs.jsp?id=" + roleid + "&menu_page=1") %>
		
			<%=Bean.getSelectOnChangeBeginHTML("menu_access_type", "../crm/security/rolespecs.jsp?id=" + roleid + "&menu_page=1", Bean.syslogXML.getfieldTransl("row_type", false)) %>
				<%= Bean.getPrivilegeTypeOptions(menu_access_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

		</tr>
		</tbody>
		</table>
	<%= role.getRolesMenuHTML(menu_find, menu_access_type) %>
<% }
if (Bean.currentMenu.isCurrentTabAndAccessPermitted("SECURITY_ROLES_REPORTS")) {%>

<script type="text/javascript">
 	function CheckCB(Element) {
		myCheck = true;

		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			if (myName.substr(0,6) == 'chb_id'){
				myCheck = myCheck && thisCheckBoxes[i].checked;
			}
		}
		if (document.getElementById('mainCheck')) {
			document.getElementById('mainCheck').checked = myCheck;
		}
	}
	function CheckAll(Element,Name) {
		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			
			if (myName.substr(0,6) == Name){
					thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
			}
		}
	}
	CheckCB();
</script>

	<table <%=Bean.getTableBottomFilter() %>>
		<tr>
			<%= Bean.getFindHTML("find_report", find_report, "../crm/security/rolespecs.jsp?id=" + roleid + "&report_page=1&") %>

			<%=Bean.getSelectOnChangeBeginHTML("id_menu_report", "../crm/security/rolespecs.jsp?id=" + roleid + "&report_page=1", "") %>
				<%=Bean.getUserDistinctReportMenuNameOptions(id_menu_report, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("module_type", "../crm/security/rolespecs.jsp?id=" + roleid + "&report_page=1", Bean.reportXML.getfieldTransl("cd_module_type", false)) %>
			 	<%= Bean.getSysModuleTypeOptions(module_type, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
	
			<%=Bean.getSelectOnChangeBeginHTML("report_kind", "../crm/security/rolespecs.jsp?id=" + roleid + "&report_page=1", Bean.reportXML.getfieldTransl("cd_report_kind", false)) %>
			 	<%= Bean.getReportKindOptions(report_kind, true) %>
			<%=Bean.getSelectOnChangeEndHTML() %>

			<% String lElementSelected = Bean.getRoleReportsPermissionCount(roleid); %>
			<%=Bean.getSelectOnChangeBeginHTML("has_permission", "../crm/security/rolespecs.jsp?id=" + roleid + "&report_page=1", Bean.commonXML.getfieldTransl("h_chosen", false) + "(" + lElementSelected + ")") %>
				<%=Bean.getSelectOptionHTML(has_permission, "", "") %>
				<%=Bean.getSelectOptionHTML(has_permission, "Y", Bean.commonXML.getfieldTransl("h_chosen", false)) %>
				<%=Bean.getSelectOptionHTML(has_permission, "N", Bean.commonXML.getfieldTransl("h_not_chosen", false)) %>
			<%=Bean.getSelectOnChangeEndHTML() %>
		</tr>
	</table>

	     
<%= role.getRoleReportsHTML(id_menu_report, has_permission, find_report, module_type, report_kind, l_report_page_beg, l_report_page_end) %>
<% }

} %>

</div></div>
</body>
</html>
