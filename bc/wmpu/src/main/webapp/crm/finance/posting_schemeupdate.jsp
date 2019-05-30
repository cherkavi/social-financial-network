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

String pageFormName = "FINANCE_POSTING_SCHEME";

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
					Bean.posting_schemeXML.getfieldTransl("h_operation_scheme_add", false),
					"Y",
					"Y") 
			%>

       <script>
		var formData = new Array (
			new Array ('name_club', 'varchar2', 1),
			new Array ('state_bk_operation_scheme', 'varchar2', 1),
			new Array ('begin_action_date', 'varchar2', 1),
			new Array ('id_bk_account_scheme', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}

	</script>
	<form action="../crm/finance/posting_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	    <input type="hidden" name="type" value="general">
	    <input type="hidden" name="action" value="add">
	    <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="id" value="<%= id %>">
	
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("state_bk_operation_scheme", true) %></td> <td><select name="state_bk_operation_scheme"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("BK_OPERATION_SCHEME_STATE", "IN_WORKING", false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
		  	</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("id_bk_account_scheme", true) %></td> <td><select name="id_bk_account_scheme"  class="inputfield"><%= Bean.getBKAccountSchemeOptions("", true) %></select></td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("begin_action_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("end_action_date", false) %></td><td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.posting_schemeXML.getfieldTransl("desc_bk_operation_scheme", false)%></td><td  colspan="3"><textarea name="desc_bk_operation_scheme" cols="70" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/posting_schemeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/posting_scheme.jsp") %>
			</td>
		</tr>
	</table>
	</form>

	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("begin_action_date", false) %>
	<%= Bean.getCalendarScript("end_action_date", false) %>

	        <%
   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   	}

} else if (process.equalsIgnoreCase("yes"))	{
%>
<body>
<%
	
	String
    	state_bk_operation_scheme 	= Bean.getDecodeParam(parameters.get("state_bk_operation_scheme")),
		desc_bk_operation_scheme	= Bean.getDecodeParam(parameters.get("desc_bk_operation_scheme")),
		id_bk_account_scheme		= Bean.getDecodeParam(parameters.get("id_bk_account_scheme")),
	    
    	begin_action_date    		= Bean.getDecodeParam(parameters.get("begin_action_date")),
    	end_action_date    			= Bean.getDecodeParam(parameters.get("end_action_date")),
    	id_club		    			= Bean.getDecodeParam(parameters.get("id_club"));
    
    if (action.equalsIgnoreCase("add")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.add_bk_oper_scheme(?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [7];

		pParam[0] = state_bk_operation_scheme;
		pParam[1] = desc_bk_operation_scheme;
		pParam[2] = begin_action_date;
		pParam[3] = end_action_date;
		pParam[4] = id_bk_account_scheme;
		pParam[5] = id_club;
		pParam[6] = Bean.getDateFormat();
		
		%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/posting_schemespecs.jsp?id=", "../crm/finance/posting_scheme.jsp") %>
		<% 	

    } else if (action.equalsIgnoreCase("remove")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.delete_bk_oper_scheme(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;
	
		%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/posting_scheme.jsp", "") %>
		<% 	

    } else if (action.equalsIgnoreCase("edit")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.update_bk_oper_scheme(?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [7];

		pParam[0] = id;
		pParam[1] = state_bk_operation_scheme;
		pParam[2] = desc_bk_operation_scheme;
		pParam[3] = begin_action_date;
		pParam[4] = end_action_date;
		pParam[5] = id_bk_account_scheme;
		pParam[6] = Bean.getDateFormat();
		
		%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/posting_schemespecs.jsp?id=" + id, "") %>
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
