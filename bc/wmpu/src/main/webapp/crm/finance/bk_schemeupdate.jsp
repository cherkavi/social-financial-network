	<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcFNBKSchemeObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %> 
	<%= Bean.getBottomFrameCSS() %>

</head>


<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_BK_SCHEME";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id"));
String type	= Bean.getDecodeParam(parameters.get("type"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general"))
	/* вибираємо тип дії (добавити, видалити...)*/
		{
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
		%>
		<script>
			var formData = new Array (
				new Array ('state_bk_account_scheme', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1)
			);
		</script>


		<%
	
	/*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {

    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		
    		%>
<body> 
			<%= Bean.getOperationTitle(
					Bean.bk_schemeXML.getfieldTransl("h_bk_accounts_add", false),
					"Y",
					"Y") 
			%>

        <form action="../crm/finance/bk_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
			<input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("state_bk_account_scheme", true) %></td> <td><select name="state_bk_account_scheme"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("BK_OPERATION_SCHEME_STATE", "IN_WORKING", false) %></select></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td rowspan="3"><%=Bean.bk_schemeXML.getfieldTransl("desc_bk_account_scheme", false)%></td><td rowspan="3"><textarea name="desc_bk_account_scheme" cols="60" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("begin_action_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("end_action_date", false) %></td><td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_schemeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/finance/bk_scheme.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/finance/bk_schemespecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>

</form>


		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("begin_action_date", false) %>
		<%= Bean.getCalendarScript("end_action_date", false) %>

	        <%
	    	/*  --- Добавити запис --- */
    	} else if (action.equalsIgnoreCase("copy")) {
    		bcFNBKSchemeObject setting = new bcFNBKSchemeObject(id);
    		
	        %>
<body> 
			<%= Bean.getOperationTitle(
					Bean.bk_schemeXML.getfieldTransl("h_bk_accounts_add", false),
					"Y",
					"Y") 
			%>
        <form action="../crm/finance/bk_schemeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
			<input type="hidden" name="action" value="copy">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_previous" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("state_bk_account_scheme", true) %></td> <td><select name="state_bk_account_scheme"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("BK_OPERATION_SCHEME_STATE", setting.getValue("state_bk_account_scheme"), true) %></select></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(setting.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", setting.getValue("ID_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td rowspan="3"><%=Bean.bk_schemeXML.getfieldTransl("desc_bk_account_scheme", false)%></td><td rowspan="3"><textarea name="desc_bk_account_scheme" cols="60" rows="3" class="inputfield"><%=setting.getValue("DESC_BK_ACCOUNT_SCHEME")%></textarea></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("begin_action_date", true) %></td><td><%=Bean.getCalendarInputField("begin_action_date", setting.getValue("BEGIN_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("end_action_date", false) %></td><td><%=Bean.getCalendarInputField("end_action_date", setting.getValue("END_ACTION_DATE_FRMT"), "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_schemeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bk_schemespecs.jsp?id=" + id) %>
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
			id_previous 				= Bean.getDecodeParam(parameters.get("id_previous")),
			state_bk_account_scheme 	= Bean.getDecodeParam(parameters.get("state_bk_account_scheme")),
			desc_bk_account_scheme 		= Bean.getDecodeParam(parameters.get("desc_bk_account_scheme")),
			begin_action_date			= Bean.getDecodeParam(parameters.get("begin_action_date")),
			end_action_date 			= Bean.getDecodeParam(parameters.get("end_action_date")),
			id_club			 			= Bean.getDecodeParam(parameters.get("id_club"));
    

		if (action.equalsIgnoreCase("add")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING_SETTINGS.add_bk_account_scheme("+
				"?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [6];

			pParam[0] = state_bk_account_scheme;
			pParam[1] = desc_bk_account_scheme;
			pParam[2] = begin_action_date;
			pParam[3] = end_action_date;
			pParam[4] = id_club;
			pParam[5] = Bean.getDateFormat();

		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/bk_schemespecs.jsp?id=" , "../crm/finance/bk_scheme.jsp") %>
			<% 	

		} else if (action.equalsIgnoreCase("copy")) { 
				
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING_SETTINGS.copy_bk_account_scheme("+
				"?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [7];

			pParam[0] = id_previous;
			pParam[1] = state_bk_account_scheme;
			pParam[2] = desc_bk_account_scheme;
			pParam[3] = begin_action_date;
			pParam[4] = end_action_date;
			pParam[5] = id_club;
			pParam[6] = Bean.getDateFormat();

		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/bk_schemespecs.jsp?id=" , "../crm/finance/bk_scheme.jsp") %>
			<% 	

			} else if (action.equalsIgnoreCase("remove")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING_SETTINGS.delete_bk_account_scheme(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bk_scheme.jsp" , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
			
	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING_SETTINGS.update_bk_account_scheme("+
	 			"?,?,?,?,?,?,?)}";

			String[] pParam = new String [6];

			pParam[0] = id;
			pParam[1] = state_bk_account_scheme;
			pParam[2] = desc_bk_account_scheme;
			pParam[3] = begin_action_date;
			pParam[4] = end_action_date;
			pParam[5] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bk_schemespecs.jsp?id=" + id, "") %>
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
