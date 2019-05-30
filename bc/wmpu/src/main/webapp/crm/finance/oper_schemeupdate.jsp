<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="bc.objects.bcClubRelationshipBKOperationShemeObject"%>
<%@page import="bc.objects.bcClubShortObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>


<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_OPER_SCHEME";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String type		= Bean.getDecodeParam(parameters.get("type")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
    %> 
<body>
	<%	
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add")) {

   			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());

   			%> 
			<%= Bean.getOperationTitle(
					Bean.oper_schemeXML.getfieldTransl("h_operation_scheme_add", false),
					"Y",
					"Y") 
			%>

       <script>
		var formData = new Array (
			new Array ('name_club', 'varchar2', 1),
			new Array ('cd_fn_oper_scheme', 'varchar2', 1),
			new Array ('name_fn_oper_scheme', 'varchar2', 1),
			new Array ('cd_fn_oper_exec_type', 'varchar2', 1),
			new Array ('cd_fn_oper_state', 'varchar2', 1),
			new Array ('is_system_oper', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}

	</script>
	<form action="../crm/finance/oper_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	    <input type="hidden" name="type" value="general">
	    <input type="hidden" name="action" value="add">
	    <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="id" value="<%= id %>">
	
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_scheme", true) %></td> <td><input type="text" name="cd_fn_oper_scheme" size="74" value="" class="inputfield"> </td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
		  	</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("name_fn_oper_scheme", true) %></td> <td><input type="text" name="name_fn_oper_scheme" size="74" value="" class="inputfield"> </td>
			<td><%= Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_exec_type", true) %></td> <td><select name="cd_fn_oper_exec_type" id="cd_fn_oper_exec_type" class="inputfield"><%= Bean.getFNOperExecTypeOptions("MANUAL_BY_OPERATOR", true) %></select></td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.oper_schemeXML.getfieldTransl("desc_fn_oper_scheme", false)%></td><td rowspan="3"><textarea name="desc_fn_oper_scheme" cols="70" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_state", true) %></td> <td><select name="cd_fn_oper_state" id="cd_fn_oper_state" class="inputfield"><%= Bean.getFNOperStateOptions("IN_DEVELOPMENT", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("is_system_oper", true) %></td> <td><select name="is_system_oper" id="is_system_oper" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("note_fn_oper_scheme", false)%></td><td><textarea name="note_fn_oper_scheme" cols="70" rows="3" class="inputfield"></textarea></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/oper_schemeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/oper_scheme.jsp") %>
			</td>
		</tr>
	</table>
	</form>

	        <%
   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   	}

} else if (process.equalsIgnoreCase("yes"))	{
%>
<body>
<%
	
	String
		name_fn_oper_scheme 	= Bean.getDecodeParam(parameters.get("name_fn_oper_scheme")),
		desc_fn_oper_scheme	 	= Bean.getDecodeParam(parameters.get("desc_fn_oper_scheme")),
		note_fn_oper_scheme	 	= Bean.getDecodeParam(parameters.get("note_fn_oper_scheme")),
		cd_fn_oper_exec_type	= Bean.getDecodeParam(parameters.get("cd_fn_oper_exec_type")),
    	id_club		    		= Bean.getDecodeParam(parameters.get("id_club")),
    	cd_fn_oper_scheme    	= Bean.getDecodeParam(parameters.get("cd_fn_oper_scheme")),
    	is_system_oper    		= Bean.getDecodeParam(parameters.get("is_system_oper")),
    	cd_fn_oper_state    	= Bean.getDecodeParam(parameters.get("cd_fn_oper_state"));
    
    if (action.equalsIgnoreCase("add")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_FN_OPER.add_fn_oper_scheme(?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [8];

		pParam[0] = cd_fn_oper_scheme;
		pParam[1] = name_fn_oper_scheme;
		pParam[2] = desc_fn_oper_scheme;
		pParam[3] = note_fn_oper_scheme;
		pParam[4] = cd_fn_oper_exec_type;
		pParam[5] = cd_fn_oper_state;
		pParam[6] = is_system_oper;
		pParam[7] = id_club;
		
		%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/oper_schemespecs.jsp?id=", "../crm/finance/oper_scheme.jsp") %>
		<% 	

    } else if (action.equalsIgnoreCase("remove")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_FN_OPER.delete_fn_oper_scheme(?,?)}";
	
		String[] pParam = new String [1];

		pParam[0] = id;
		
		%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/oper_scheme.jsp", "") %>
		<% 	

    } else if (action.equalsIgnoreCase("edit")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_FN_OPER.update_fn_oper_scheme(?,?,?,?,?,?,?,?)}";

   		String[] pParam = new String [7];

		pParam[0] = id;
   		pParam[1] = cd_fn_oper_scheme;
   		pParam[2] = name_fn_oper_scheme;
   		pParam[3] = desc_fn_oper_scheme;
   		pParam[4] = note_fn_oper_scheme;
   		pParam[5] = cd_fn_oper_state;
   		pParam[6] = is_system_oper;
		
		%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/oper_schemespecs.jsp?id=" + id, "") %>
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
