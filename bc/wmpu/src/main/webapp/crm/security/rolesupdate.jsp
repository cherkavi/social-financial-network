<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcRoleObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.lists.bcListSystemRole"%>
<%@page import="java.util.ArrayList"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SECURITY_ROLES";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String external_id		= Bean.getDecodeParamPrepare(parameters.get("id")); 
String id_role			= Bean.getDecodeParamPrepare(parameters.get("id_role")); 
String type				= Bean.getDecodeParamPrepare(parameters.get("type")); 
String action			= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process			= Bean.getDecodeParamPrepare(parameters.get("process"));
String back_type		= Bean.getDecodeParamPrepare(parameters.get("back_type"));
String id_jur_prs		= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs")); 

String updateLink = "../crm/security/rolesupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"ROLE":back_type;
System.out.println("back_type="+back_type);
if ("ROLE".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/security/roles.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/security/roles.jsp?";
	} else {
		backLink = "../crm/security/rolespecs.jsp?id="+id_role;
	}
} else if ("PARTNER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
	backLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
}

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
	        %> 
			<script>
				var formData = new Array (
					new Array ('NAME_ROLE', 'varchar2', 1),
					new Array ('name_jur_prs', 'varchar2', 1),
					new Array ('NAME_MODULE_TYPE', 'varchar2', 1)
				);
			</script>

			<% if ("ROLE".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.roleXML.getfieldTransl("LAB_ADD_ROLE", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.roleXML.getfieldTransl("LAB_ADD_ROLE", false),
					"Y",
					"Y") 
			%>
			<% } %>
        <form action="<%=updateLink %>" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="id_role" value="<%=id_role %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">

		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_ROLE", true) %></td><td><input type="text" name="NAME_ROLE" size="70" value=""  class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.roleXML.getfieldTransl("DESC_ROLE", false) %></td> <td colspan="4"><textarea name="DESC_ROLE" cols="67" rows="3" class="inputfield"></textarea></td>
		</tr>	
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("partner", true) %> </td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", "", "", "", "60") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_MODULE_TYPE", true) %></td><td><select name="CD_MODULE_TYPE" class="inputfield"><%=Bean.getSysModuleTypeOptions("", true) %></select></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", ("ROLE".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>

	</table>

</form>

	        <%
		} else if (action.equalsIgnoreCase("select")) {
			String tagRole = "" + back_type + "_SELECT_ROLE";
			String tagRoleFind = "" + back_type + "__SELECT_ROLE_FIND";
			String tagRoleModuleType = "" + back_type + "__SELECT_ROLE_MODULE_TYPE";
			
			String l_role_page = Bean.getDecodeParam(parameters.get("role_page"));
			Bean.pageCheck(pageFormName + tagRole, l_role_page);
			String l_role_page_beg = Bean.getFirstRowNumber(pageFormName + tagRole);
			String l_role_page_end = Bean.getLastRowNumber(pageFormName + tagRole);
			
			String role_find 	= Bean.getDecodeParam(parameters.get("role_find"));
			role_find 	= Bean.checkFindString(pageFormName + tagRoleFind, role_find, l_role_page);

			String role_module_type 	= Bean.getDecodeParam(parameters.get("role_module_type"));
			role_module_type 	= Bean.checkFindString(pageFormName + tagRoleModuleType, role_module_type, l_role_page);

			bcListSystemRole list = new bcListSystemRole();
	    	
	    	String mySelectLink = "../crm/security/rolesupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&id_jur_prs="+id_jur_prs+"&id_role="+id_role+"&action=copy&process=no";
	    	
	    	%>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.roleXML.getfieldTransl("LAB_SELECT_ROLE", false),
					"N",
					"N") 
			%>
			<table <%=Bean.getTableBottomFilter() %>><tbody>
			<tr>
				<%=Bean.getFindHTML("role_find", role_find, "../crm/security/rolesupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&role_page=1&", "div_data_detail") %>
			
				<%=Bean.getSelectOnChangeBeginHTML("", "role_module_type", "../crm/security/rolesupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&role_page=1", Bean.roleXML.getfieldTransl("name_module_type", false), "div_data_detail") %>
					<%= Bean.getReferralShemeTypeOptions(role_module_type, true) %>
				<%=Bean.getSelectOnChangeEndHTML() %>

				<%= Bean.getPagesHTML(pageFormName + tagRole, "../crm/club/roleupdate.jsp?back_type="+back_type+"&type=general&id="+external_id+"&action=select&process=no&", "role_page", "", "div_data_detail") %>
	
			</tr>
			</tbody>
			</table>

	    	<%=list.getSystemRolesHTMLOnlySelect(role_find, role_module_type, mySelectLink, l_role_page_beg, l_role_page_end) %>
			<%
	       
    	} else if (action.equalsIgnoreCase("edit") || action.equalsIgnoreCase("copy")) {
    		
    		bcRoleObject role = new bcRoleObject(id_role); 
    		
	        %> 
			<script>
				var formData = new Array (
					new Array ('NAME_ROLE', 'varchar2', 1),
					new Array ('name_jur_prs', 'varchar2', 1)
				);
			</script>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.roleXML.getfieldTransl(action.equalsIgnoreCase("edit")?"LAB_EDIT_ROLE":"LAB_COPY_ROLE", false),
					"Y",
					"Y") 
			%>
        <form action="../crm/security/rolesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="<%=action %>">
	        <input type="hidden" name="process" value="yes">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="id_role" value="<%=id_role %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
    		<input type="hidden" name="CD_MODULE_TYPE" value="<%=role.getValue("CD_MODULE_TYPE") %>">

		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_ROLE", true) %></td><td><input type="text" name="NAME_ROLE" size="70" value="<%=(action.equalsIgnoreCase("copy"))?"":role.getValue("NAME_ROLE") %>"  class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.roleXML.getfieldTransl("DESC_ROLE", false) %></td> <td colspan="4"><textarea name="DESC_ROLE" cols="67" rows="3" class="inputfield"><%= (action.equalsIgnoreCase("copy"))?"":role.getValue("DESC_ROLE") %></textarea></td>
		</tr>	
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("partner", true) %> </td> 
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", (action.equalsIgnoreCase("copy")?(back_type.equalsIgnoreCase("PARTNER")?external_id:""):role.getValue("ID_JUR_PRS")), (action.equalsIgnoreCase("copy")?"":role.getValue("SNAME_JUR_PRS")), "", "60") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_MODULE_TYPE", false) %></td> <td><input type="text" name="NAME_MODULE_TYPE" size="20" value="<%= role.getValue("NAME_MODULE_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
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
				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", ("ROLE".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>

	</table>

</form>

	        <%
     	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}

	} else if (process.equalsIgnoreCase("yes"))	{
    
		String
    		NAME_ROLE				= Bean.getDecodeParam(parameters.get("NAME_ROLE")),
    		DESC_ROLE				= Bean.getDecodeParam(parameters.get("DESC_ROLE")),
    		NAME_MODULE_TYPE		= Bean.getDecodeParam(parameters.get("NAME_MODULE_TYPE")),
    		CD_MODULE_TYPE			= Bean.getDecodeParam(parameters.get("CD_MODULE_TYPE")),
    		DATE_BEG				= Bean.getDecodeParam(parameters.get("DATE_BEG")),
    		DATE_END				= Bean.getDecodeParam(parameters.get("DATE_END")); 
    
		ArrayList<String> pParam = new ArrayList<String>();	
		
		if (action.equalsIgnoreCase("add")) { 
		
			pParam.add(NAME_ROLE);
			pParam.add(DESC_ROLE);
			pParam.add(id_jur_prs);
			pParam.add(CD_MODULE_TYPE);
			pParam.add("Y");
		
		 	%>
			<%= Bean.executeInsertFunction("PACK$ROLE_UI.add_role", pParam, backLink + "&id_role=" , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("copy")) { 
		
			pParam.add(id_role);
			pParam.add(NAME_ROLE);
			pParam.add(DESC_ROLE);
			pParam.add(id_jur_prs);
			pParam.add(CD_MODULE_TYPE);
			pParam.add("Y");
		
		 	%>
			<%= Bean.executeInsertFunction("PACK$ROLE_UI.copy_role", pParam, backLink + "&id_role=" , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
		
			pParam.add(id_role);
		
	   	 	%>
   			<%= Bean.executeDeleteFunction("PACK$ROLE_UI.delete_role", pParam, generalLink , "") %>
   			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
		
			pParam.add(id_role);
			pParam.add(NAME_ROLE);
			pParam.add(DESC_ROLE);
			pParam.add(CD_MODULE_TYPE);
			pParam.add("Y");
		
		 	%>
			<%= Bean.executeUpdateFunction("PACK$ROLE_UI.update_role", pParam, backLink, "") %>
			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("user")) {
	
	String id_user		= Bean.getDecodeParam(parameters.get("id_user"));
	
    if (process.equalsIgnoreCase("no"))	{
    	if (action.equalsIgnoreCase("add")) {
    		bcRoleObject role = new bcRoleObject(id_role);
	        %> 

			<%= Bean.getOperationTitle(
					Bean.roleXML.getfieldTransl("LAB_ADD_USER", false),
					"Y",
					"N") 
			%>
        <form action="../crm/security/rolesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="user">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=id_role %>">

		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.roleXML.getfieldTransl("NAME_ROLE", true) %></td><td><input type="text" name="NAME_ROLE" size="60" value="<%=role.getValue("NAME_ROLE") %>" readonly="readonly" class="inputfield-ro"></td>
		</tr>
		<tr>
			<td><%= Bean.userXML.getfieldTransl("name_user", false) %></td><td><select name="id_user" class="inputfield"><%=Bean.getRoleFreeUsersListOptions(id_role, false) %></select></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/security/rolesupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/security/rolespecs.jsp?id=" + id_role) %>
			</td>
		</tr>

	</table>

	</form>

	        <%
     	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}

    } else if (process.equalsIgnoreCase("yes"))	{
	
    	if (action.equalsIgnoreCase("add")) { 
    		
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.add_user_role(?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = id_role;
			pParam[1] = id_user;
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/security/rolespecs.jsp?id=" + id_role, "") %>
			<% 	

    	} else if (action.equalsIgnoreCase("remove")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$ROLE_UI.delete_user_role(?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = id_role;
			pParam[1] = id_user;
			
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/security/rolespecs.jsp?id=" + id_role, "") %>
			<% 	

    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}

%>


</body>
</html>
