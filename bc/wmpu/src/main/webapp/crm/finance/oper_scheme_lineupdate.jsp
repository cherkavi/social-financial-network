<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcFNOperSchemeLineObject"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="bc.objects.bcClubRelationshipBKOperationShemeObject"%>
<%@page import="bc.objects.bcFNOperSchemeObject"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%><html>

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

String id			= Bean.getDecodeParam(parameters.get("id")); 
String id_scheme	= Bean.getDecodeParam(parameters.get("id_scheme"));
String action		= Bean.getDecodeParam(parameters.get("action")); 
String type			= Bean.getDecodeParam(parameters.get("type")); 
String process		= Bean.getDecodeParam(parameters.get("process"));

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
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		bcFNOperSchemeObject scheme = new bcFNOperSchemeObject(id_scheme);
    		
	        %> 
			<%= Bean.getOperationTitle(
					Bean.oper_schemeXML.getfieldTransl("h_postings_settings_add", false),
					"Y",
					"Y") 
			%>

    <script>
		var formData = new Array (
			new Array ('desc_fn_oper_scheme_line', 'varchar2', 1),
			new Array ('order_number', 'varchar2', 1),
			new Array ('cd_fn_oper_type', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}
	</script>

	<form action="../crm/finance/oper_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	    <input type="hidden" name="type" value="general">
	    <input type="hidden" name="action" value="add">
	    <input type="hidden" name="actionprev" value="<%=action %>">
	    <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="id" value="<%= id %>">
	    <input type="hidden" name="id_scheme" value="<%= id_scheme %>">
	
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("cd_fn_oper_type", true) %></td> <td><select name="cd_fn_oper_type" id="cd_fn_oper_type" class="inputfield"><%= Bean.getFNOperTypeOptions("", false) %></select></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.oper_schemeXML.getfieldTransl("order_number", true) %></td> <td><input type="text" name="order_number" size="20" value="" class="inputfield"> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign=top><%= Bean.oper_schemeXML.getfieldTransl("desc_fn_oper_scheme_line", true) %></td><td valign=top><textarea name="desc_fn_oper_scheme_line" cols="74" rows="5" class="inputfield"></textarea></td>
			<td valign=top><%= Bean.oper_schemeXML.getfieldTransl("note_fn_oper_scheme_line", false) %></td><td valign=top><textarea name="note_fn_oper_scheme_line" cols="74" rows="5" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td colspan="10" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/oper_scheme_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
			<% if (action.equalsIgnoreCase("add")) { %>
				<%=Bean.getGoBackButton("../crm/finance/oper_schemespecs.jsp?id=" + id_scheme) %>
			<% } else { %>
                <%=Bean.getGoBackButton("../crm/finance/oper_scheme_linespecs.jsp?id=" + id) %>
			<% } %>
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
		actionprev		 			= Bean.getDecodeParam(parameters.get("actionprev")),
	
		desc_fn_oper_scheme_line 	= Bean.getDecodeParam(parameters.get("desc_fn_oper_scheme_line")),
		note_fn_oper_scheme_line 	= Bean.getDecodeParam(parameters.get("note_fn_oper_scheme_line")),
		order_number 				= Bean.getDecodeParam(parameters.get("order_number")),
		cd_fn_oper_type	 			= Bean.getDecodeParam(parameters.get("cd_fn_oper_type"));
    
%>
<script>
	//window.alert('parameter=<%=parameters.get("amount")%>\n, prepared=<%=Bean.getDecodeParam(parameters.get("amount"))%>');
</script>
<%
    if (action.equalsIgnoreCase("add")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_FN_OPER.add_fn_oper_scheme_line(?,?,?,?,?,?,?)}";

		String[] pParam = new String [5];

		pParam[0] = id_scheme;
		pParam[1] = cd_fn_oper_type;
		pParam[2] = desc_fn_oper_scheme_line;
		pParam[3] = note_fn_oper_scheme_line;
		pParam[4] = order_number;
		
		%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/oper_schemespecs.jsp?id=" + id_scheme + "&id_line=", "../crm/finance/oper_schemespecs.jsp?id=" + id_scheme) %>
		<% 	

    } else if (action.equalsIgnoreCase("remove")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_FN_OPER.delete_fn_oper_scheme_line(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;
	
		%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/oper_schemespecs.jsp?id=" + id_scheme, "../crm/finance/oper_scheme_linespecs.jsp?id=" + id) %>
		<% 	

    } else if (action.equalsIgnoreCase("edit")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_FN_OPER.update_fn_oper_scheme_line(?,?,?,?,?,?)}";

    	String[] pParam = new String [5];

    	pParam[0] = id;
    	pParam[1] = cd_fn_oper_type;
    	pParam[2] = desc_fn_oper_scheme_line;
    	pParam[3] = note_fn_oper_scheme_line;
    	pParam[4] = order_number;
		
		%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/oper_scheme_linespecs.jsp?id=" + id, "") %>
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
