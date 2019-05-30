<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcRoleObject"%>
<%@page import="bc.objects.bcPrivilegeObject"%>
<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SECURITY_ROLES";
String tagRolesEdit = "_ROLES_EDIT";
String tagFind = "_ROLES_FIND";
String tagPrivilegeType = "_ROLES_PRIVILEGE_TYPE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id_role	= Bean.getDecodeParam(parameters.get("id_role")); 
String id_menu	= Bean.getDecodeParam(parameters.get("id_menu"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

String	tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab==null || "".equalsIgnoreCase(tab)) { tab = Bean.tabsHmGetValue(pageFormName); }

String l_role_page	= Bean.getDecodeParam(parameters.get("role_page"));
Bean.pageCheck(pageFormName + tagRolesEdit, l_role_page);
String l_role_page_beg = Bean.getFirstRowNumber(pageFormName + tagRolesEdit);
String l_role_page_end = Bean.getLastRowNumber(pageFormName + tagRolesEdit);

String menu_access_type	= Bean.getDecodeParamPrepare(parameters.get("menu_access_type"));
menu_access_type		= Bean.checkFindString(pageFormName + tagPrivilegeType, menu_access_type, l_role_page);

String role_find	= Bean.getDecodeParamPrepare(parameters.get("role_find"));
role_find		= Bean.checkFindString(pageFormName + tagFind, role_find, l_role_page);


if (id_role==null || ("".equalsIgnoreCase(id_role))) id_role="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

bcRoleObject role = new bcRoleObject(id_role);

%>
	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%= Bean.getPageHeader() %>
		</tr>
	</table>
	<table <%=Bean.getTableMenuParam() %>>
		<%= Bean.getDetailCaption(role.getValue("ID_ROLE") + " - " + role.getValue("NAME_ROLE")) %>
		<tr>
			<td>
				<!-- Выводим перечень закладок -->
				<%= Bean.currentMenu.getFormTabScheeds(tab, "../crm/security/rolespecs.jsp?id=" + id_role) %>
			</td>
		</tr>
	</table>
<%
if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{

	%>
	<%
    if (action.equalsIgnoreCase("add")) {
    	
	    %> 

	<script>
		var formData = new Array (
			new Array ('id_menu', 'varchar2', 1),
			new Array ('id_privilege_type', 'varchar2', 1)
		);
	</script>

   <form action="../crm/security/rolesmenuupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">	
		<input type="hidden" name="id_role" value="<%= id_role %>">
	<table <%=Bean.getTableDetailParam() %>>
	<tr>
		<%=Bean.getOperationTitleShort("4", Bean.roleXML.getfieldTransl("LAB_EDIT_ROLE_MENU", false), "Y", "Y") %>
	</tr>
	<tr>
		<td><%= Bean.roleXML.getfieldTransl("NAME_ROLE", false) %> </td><td><input type="text" name="NAME_ROLE" size="40" value="<%= role.getValue("NAME_ROLE") %>" readonly="readonly" class="inputfield-ro"></td>
	</tr>
    <tr>
		<td><%= Bean.roleXML.getfieldTransl("name_menu_element", true) %></td> <td><select name="id_menu" class="inputfield"><%= Bean.getRoleMenuOptions(role.getValue("CD_MODULE_TYPE"), id_role) %></select></td>
	</tr>
    <tr>
		<td><%= Bean.roleXML.getfieldTransl("name_privilege_type", true) %></td> <td><select name="id_privilege_type" class="inputfield"><%= Bean.getPrivilegeTypeOptions("", false) %></select></td>
	</tr>
	<tr>
		<td colspan="6" align="center">
			<%=Bean.getSubmitButtonAjax("../crm/security/rolesmenuupdate.jsp") %>
			<%=Bean.getResetButton() %>
			<%=Bean.getGoBackButton("../crm/security/rolespecs.jsp?id=" + id_role) %>
		</td>
	</tr>

</table>

</form> <br><%
    } else if (action.equalsIgnoreCase("edit")) {
    	
    	bcPrivilegeObject privilege = new bcPrivilegeObject(id_role, id_menu);
		
        %> 

	<script>
		var formData = new Array (
			new Array ('id_privilege_type', 'varchar2', 1)
		);
	</script>

        <form action="../crm/security/rolesmenuupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">	
			<input type="hidden" name="id_role" value="<%= id_role %>">	
			<input type="hidden" name="id_menu" value="<%= id_menu %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<%=Bean.getOperationTitleShort("4", Bean.roleXML.getfieldTransl("LAB_EDIT_ROLE_MENU", false), "Y", "Y") %>
		</tr>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_ROLE", false) %> </td><td><input type="text" name="NAME_ROLE" size="40" value="<%= privilege.getValue("NAME_ROLE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("name_menu_element", false) %> </td><td><input type="text" name="name_menu_element" size="40" value="<%= privilege.getValue("NAME_MENU_ELEMENT") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
	    <tr>
			<td><%= Bean.roleXML.getfieldTransl("name_privilege_type", true) %></td> <td><select name="id_privilege_type" class="inputfield"><%= Bean.getPrivilegeTypeOptions(privilege.getValue("ID_PRIVILEGE_TYPE"), true) %></select></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/security/rolesmenuupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/security/rolespecs.jsp?id=" + id_role) %>
			</td>
		</tr>

	</table>

</form> <br><%
    	} 
    else if (action.equalsIgnoreCase("editall")) {%>
		<%= Bean.getOperationTitleShort(
				"",
				Bean.roleXML.getfieldTransl("LAB_EDIT_ROLE_MENU", false),
				"N",
				"N") 
		%>

    	<table <%=Bean.getTableMenuParam() %>>
		<tr>
			<%=Bean.getFindHTML("role_find", role_find, "../crm/security/rolesmenuupdate.jsp?id_role=" + id_role + "&process=no&action=editall&role_page=1") %>
		
			<td>
				<%=Bean.getSelectOnChangeBeginHTML("menu_access_type", "../crm/security/rolesmenuupdate.jsp?id_role=" + id_role + "&process=no&action=editall&role_page=1&", Bean.roleXML.getfieldTransl("NAME_PRIVILEGE_TYPE", false)) %>
					<%=Bean.getSelectOptionHTML(menu_access_type, "", "") %>
					<%=Bean.getSelectOptionHTML(menu_access_type, "0", Bean.roleXML.getfieldTransl("l_access_denied", false)) %>
					<%= Bean.getPrivilegeTypeOptions(menu_access_type, false) %>
				<%=Bean.getSelectOnChangeEndHTML() %>
			</td>
			<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagRolesEdit, "../crm/security/rolesmenuupdate.jsp?id_role=" + id_role + "&process=no&action=editall&menu_access_type=" + menu_access_type + "&", "role_page") %>
			</tr>
		</table>
		<%=role.getRoleMenuListFullHTML(role_find, menu_access_type, l_role_page_beg, l_role_page_end) %>
	<%
    	} 
    else {
    	    %><%= Bean.getUnknownActionText(action) %><%
    	}

} else if (process.equalsIgnoreCase("yes"))	{
    
	String
    	id_privilege_type	= Bean.getDecodeParam(parameters.get("id_privilege_type"));
    
	if (action.equalsIgnoreCase("add")) { %>
		<%= Bean.form_messageXML.getfieldTransl("processing_insert", false) %>
		<% 
		
		String[] results = new String[2];
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.add_role_privilege(?,?,?,?,?)}";

		String[] pParam = new String [4];

		pParam[0] = id_role;
		pParam[1] = id_menu;
		pParam[2] = id_privilege_type;
		pParam[3] = "Y";
		
		results = Bean.myCallFunctionParam(callSQL, pParam, 2);
 		String resultInt = results[0];
 		String resultMessage = results[1];
 		String fullResult = "0";
 		
 		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
		
 		if (!"0".equalsIgnoreCase(resultInt)) {
 			fullResult = resultMessage;
 		}
 		
		%>
   	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		fullResult, 
   	    		fullResult, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		Bean.form_messageXML.getfieldTransl("add_error", false)) %>
   		<% 

    } else if (action.equalsIgnoreCase("remove")) { %>

		<%= Bean.form_messageXML.getfieldTransl("processing_delete", false) %>
		<%
		String[] results = new String[2];
    	
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.delete_role_privilege(?,?,?)}";

    	String[] pParam = new String [2];

    	pParam[0] = id_role;
    	pParam[1] = id_menu;
		
    	results = Bean.myCallFunctionParam(callSQL, pParam, 2);
 		String resultInt = results[0];
 		String resultMessage = results[1];
 		String fullResult = "0";
 		
 		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
 		
 		if (!"0".equalsIgnoreCase(resultInt)) {
 			fullResult = resultMessage;
 		}
 		
 		%>
   	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		fullResult, 
   	    		fullResult, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		Bean.form_messageXML.getfieldTransl("remove_error", false)) %>
   		<% 

    } else if (action.equalsIgnoreCase("edit")) {  %>
		
		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		
		String[] results = new String[2];
	    
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.update_role_privilege(?,?,?,?,?)}";

	    String[] pParam = new String [4];

	    pParam[0] = id_role;
	    pParam[1] = id_menu;
	    pParam[2] = id_privilege_type;
	    pParam[3] = "Y";
		
		results = Bean.myCallFunctionParam(callSQL, pParam, 2);
 		String resultInt = results[0];
 		String resultMessage = results[1];
 		String fullResult = "0";
 		
 		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
		
 		if (!"0".equalsIgnoreCase(resultInt)) {
 			fullResult = resultMessage;
 		}
 		%>
 		
   	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		fullResult, 
   	    		fullResult, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		"../crm/security/rolespecs.jsp?id=" + id_role, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

    } else if (action.equalsIgnoreCase("editall")) {  %>
		
		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		
		String l_paramCount	= Bean.getDecodeParam(parameters.get("rowcount"));
		String p_id			= Bean.getDecodeParam(parameters.get("roleid"));
		%>
		<!-- l_paramCount=<%=l_paramCount %>, p_id=<%=p_id %> -->
		<%
	
		String[] results = new String[2];
    	String fullResult = "0";
    	String fullResultMessage = "";
    	String menuId = "";
    	String permId = "";
    	String callSQL = "";
		String resultInt = "";
 		String resultMessage = "";

		String[] pParam = new String [4];
	
		int i;
		for (i=1;i<=Integer.parseInt(l_paramCount);i++) {
			menuId = Bean.getDecodeParam(parameters.get("menu"+i));
			permId = Bean.getDecodeParam(parameters.get("perm"+i));
			
			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.set_privilege(?,?,?,?,?)}";

			pParam[0] = p_id;
			pParam[1] = menuId;
			pParam[1] = permId;
			pParam[1] = "Y";

			results = Bean.myCallFunctionParam(callSQL, pParam, 2);
			resultInt = results[0];
			resultMessage = results[1];
			
			%>
			
			<%
			
			if (!("0".equalsIgnoreCase(resultInt))) { 
				fullResult = resultInt;
				fullResultMessage = fullResultMessage + ", " + resultMessage;
			}
			
		}
		
		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.grant_db_priv(?,?)}";

		String[] pParam2 = new String [1];

		pParam2[0] = p_id;
			
		results = Bean.myCallFunctionParam(callSQL, pParam2, 2);
		resultInt = results[0];
		resultMessage = results[1];
			
		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
		if (!("0".equalsIgnoreCase(resultInt))) { 
			fullResult = resultInt;
			fullResultMessage = fullResultMessage + ", " + resultMessage;
		}
		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		fullResult, 
   	    		fullResultMessage, 
   	    		"../crm/security/rolesmenuupdate.jsp?id_role=" + p_id + "&process=no&action=editall", 
   	    		"../crm/security/rolespecs.jsp?id=" + p_id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

    } else { %> 
    	<%= Bean.getUnknownActionText(action) %><% 
    }
} else {
    %> <%= Bean.getUnknownProcessText(process) %> <%
}

%>


</body>
</html>
