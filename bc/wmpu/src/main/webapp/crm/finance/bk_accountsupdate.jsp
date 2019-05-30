	<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcBKAccountObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_BK_ACCOUNTS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
	%>
	<script>
		var formData = new Array (
			new Array ('name_club', 'varchar2', 1),
			new Array ('segment1', 'varchar2', 1),
			new Array ('name_bk_account', 'varchar2', 1),
			new Array ('internal_name_bk_account', 'varchar2', 1)
		);
	</script>


	<%
	/*  --- Нам нужны параметры клуба --- */
    		
    		
	
		/*  --- Добавити запис --- */
   		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
   		
   			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
   		%>
			<%= Bean.getOperationTitle(
					Bean.bk_accountXML.getfieldTransl("h_bk_accounts_add", false),
					"Y",
					"Y") 
			%>
    <form action="../crm/finance/bk_accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	    <input type="hidden" name="action" value="add">
    	<input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account", true) %> </td><td colspan=6>
			<% //int bkCount = Integer.parseInt(Bean.club_bk_account_segments_count); 
			   int i = 0;
			  for (i=1; i<=Integer.parseInt(club.getValue("BK_ACCOUNT_SEGMENTS_COUNT")); i++) {
			   if (i==1) {
			%>
				<input type="text" name="segment1" size="5" value="" class="inputfield">
			<%} else if (i==2) { %>	
			    <input type="text" name="segment2" size="5" value="" class="inputfield">
			<%} else if (i==3) { %>	
			    <input type="text" name="segment3" size="5" value="" class="inputfield">
			<%} else if (i==4) { %>	
			    <input type="text" name="segment4" size="5" value="" class="inputfield">
			<%} else if (i==5) { %>	
			    <input type="text" name="segment5" size="5" value="" class="inputfield">
			<%} else if (i==6) { %>	
			    <input type="text" name="segment6" size="5" value="" class="inputfield">
			<%} else if (i==7) { %>	
			    <input type="text" name="segment7" size="5" value="" class="inputfield">
			<%} else if (i==8) { %>	
			    <input type="text" name="segment8" size="5" value="" class="inputfield">
			<%} else if (i==9) { %>	
			    <input type="text" name="segment9" size="5" value="" class="inputfield">
			<%} else if (i==10) { %>	
			    <input type="text" name="segment10" size="5" value="" class="inputfield">
			<%} else if (i==11) { %>	
			    <input type="text" name="segment11" size="5" value="" class="inputfield">
			<%} else if (i==12) { %>	
			    <input type="text" name="segment12" size="5" value="" class="inputfield">
			<%} else if (i==13) { %>	
			    <input type="text" name="segment13" size="5" value="" class="inputfield">
			<%} else if (i==14) { %>	
			    <input type="text" name="segment14" size="5" value="" class="inputfield">
			<%} else if (i==15) { %>	
			    <input type="text" name="segment15" size="5" value="" class="inputfield">
			<% } 
			}%>
			</td><td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("id_bk_account_scheme", false) %></td> <td><select name="id_bk_account_scheme_line"  class="inputfield"><option value=""></option><%= Bean.getPostingSettingsOptions("", false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
		  	</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("name_bk_account", true) %></td> <td><input type="text" name="name_bk_account" size="70" value="" class="inputfield"> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("is_group_tsl", false) %></td> <td><select name="is_group" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("internal_name_bk_account", true) %></td><td><input type="text" name="internal_name_bk_account" size="70" value="" class="inputfield"></td>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account_parent", false) %> </td> <td><select name="id_bk_account_parent"  class="inputfield"><%= Bean.getBKAccountsGroupOptions("", true) %></select></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.bk_accountXML.getfieldTransl("desc_bk_account", false) %></td> <td valign=top><textarea name="desc_bk_account" cols="66" rows="4" class="inputfield"></textarea> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("exist_flag", false) %> </td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO","Y", false) %></select></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_accountsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/finance/bk_accounts.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/finance/bk_accountspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>

</form>

	        <%
	    	/*  --- Добавити запис --- */
   	} else if (action.equalsIgnoreCase("copy")) {
    		bcBKAccountObject account = new bcBKAccountObject(id);
    		
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    		
		        %> 
			<%= Bean.getOperationTitle(
					Bean.bk_accountXML.getfieldTransl("h_bk_accounts_add", false),
					"Y",
					"Y") 
			%>
        <form action="../crm/finance/bk_accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account", true) %> </td><td colspan=6>
			<% //int bkCount = Integer.parseInt(Bean.club_bk_account_segments_count);
			
			   int i = 0;
			  for (i=1; i<=Integer.parseInt(club.getValue("BK_ACCOUNT_SEGMENTS_COUNT")); i++) {
			   if (i==1) {
			%>
				<input type="text" name="segment1" size="5" value="<%= account.getValue("SEGMENT1") %>" class="inputfield">
			<%} else if (i==2) { %>	
			    <input type="text" name="segment2" size="5" value="<%= account.getValue("SEGMENT2") %>" class="inputfield">
			<%} else if (i==3) { %>	
			    <input type="text" name="segment3" size="5" value="<%= account.getValue("SEGMENT3") %>" class="inputfield">
			<%} else if (i==4) { %>	
			    <input type="text" name="segment4" size="5" value="<%= account.getValue("SEGMENT4") %>" class="inputfield">
			<%} else if (i==5) { %>	
			    <input type="text" name="segment5" size="5" value="<%= account.getValue("SEGMENT5") %>" class="inputfield">
			<%} else if (i==6) { %>	
			    <input type="text" name="segment6" size="5" value="<%= account.getValue("SEGMENT6") %>" class="inputfield">
			<%} else if (i==7) { %>	
			    <input type="text" name="segment7" size="5" value="<%= account.getValue("SEGMENT7") %>" class="inputfield">
			<%} else if (i==8) { %>	
			    <input type="text" name="segment8" size="5" value="<%= account.getValue("SEGMENT8") %>" class="inputfield">
			<%} else if (i==9) { %>	
			    <input type="text" name="segment9" size="5" value="<%= account.getValue("SEGMENT9") %>" class="inputfield">
			<%} else if (i==10) { %>	
			    <input type="text" name="segment10" size="5" value="<%= account.getValue("SEGMENT10") %>" class="inputfield">
			<%} else if (i==11) { %>	
			    <input type="text" name="segment11" size="5" value="<%= account.getValue("SEGMENT11") %>" class="inputfield">
			<%} else if (i==12) { %>	
			    <input type="text" name="segment12" size="5" value="<%= account.getValue("SEGMENT12") %>" class="inputfield">
			<%} else if (i==13) { %>	
			    <input type="text" name="segment13" size="5" value="<%= account.getValue("SEGMENT13") %>" class="inputfield">
			<%} else if (i==14) { %>	
			    <input type="text" name="segment14" size="5" value="<%= account.getValue("SEGMENT14") %>" class="inputfield">
			<%} else if (i==15) { %>	
			    <input type="text" name="segment15" size="5" value="<%= account.getValue("SEGMENT15") %>" class="inputfield">
			<% } 
			}%>
			</td><td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("id_bk_account_scheme", false) %></td> <td><select name="id_bk_account_scheme_line"  class="inputfield"><option value=""></option><%= Bean.getPostingSettingsOptions(account.getValue("ID_BK_ACCOUNT_SCHEME_LINE"), false) %></select></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
		  	</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", account.getValue("ID_CLUB"), "25") %>
		  	</td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("name_bk_account", true) %></td> <td><input type="text" name="name_bk_account" size="90" value="<%= account.getValue("NAME_BK_ACCOUNT") %>" class="inputfield"> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("is_group_tsl", false) %></td> <td><select name="is_group" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", account.getValue("IS_GROUP"), false) %></select> </td>
		</tr>
		<tr>
			<td><%= Bean.bk_accountXML.getfieldTransl("internal_name_bk_account", true) %></td><td><input type="text" name="internal_name_bk_account" size="90" value="<%= account.getValue("INTERNAL_NAME_BK_ACCOUNT") %>" class="inputfield"></td>
			<td><%= Bean.bk_accountXML.getfieldTransl("cd_bk_account_parent", false) %> </td> <td><select name="id_bk_account_parent"  class="inputfield"><%= Bean.getBKAccountsGroupOptions(account.getValue("ID_BK_ACCOUNT_PARENT"), true) %></select></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.bk_accountXML.getfieldTransl("desc_bk_account", false) %></td> <td valign=top><textarea name="desc_bk_account" cols="90" rows="4" class="inputfield"><%= account.getValue("DESC_BK_ACCOUNT") %></textarea> </td>
			<td><%= Bean.bk_accountXML.getfieldTransl("exist_flag", false) %> </td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", account.getValue("EXIST_FLAG"), false) %></select></td>
		</tr>
	 	<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_accountsupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bk_accountspecs.jsp?id=" + id) %>
			</td>
		</tr>
	</table>
	</form>
		        <%
    	} else if (action.equalsIgnoreCase("calc_rests")) { 

    		bcClubShortObject club = null;
    		bcBKAccountObject account = null;
    		String id_club = "";
    		String sname_club = "";
    		if (!(id==null || "".equalsIgnoreCase(id))) {
    			account = new bcBKAccountObject(id);
    			id_club = account.getValue("ID_CLUB");
    			sname_club = Bean.getClubShortName(account.getValue("ID_CLUB"));
    		} else {
    			club = new bcClubShortObject(Bean.getCurrentClubID());
    			id_club = club.getValue("ID_CLUB");
    			sname_club = club.getValue("SNAME_CLUB");
    		}
    		
    		%>
    			<%= Bean.getOperationTitle(
    					Bean.accountXML.getfieldTransl("h_calc_rests", false),
    					"Y",
    					"N") 
    			%>
    		<form action="../crm/finance/bk_accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
    		    <input type="hidden" name="action" value="calc_rests">
    		    <input type="hidden" name="process" value="yes">
    		<table <%=Bean.getTableDetailParam() %>>
				<% if (!(id==null || "".equalsIgnoreCase(id))) { %>
				<tr>
					<td><%=Bean.bk_accountXML.getfieldTransl("cd_bk_account", false)%> </td>
					<td>
						<input type="hidden" name="id_bk_account" size="20" value="<%=id%>">
						<input type="text" name="cd_bk_account" size="40" value="<%=account.getValue("CD_BK_ACCOUNT")%>" readonly="readonly" class="inputfield-ro">
					</td>
				</tr>
				<% } %>
    			<tr>
    				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
    					<%= Bean.getGoToClubLink(id_club) %>
    			  	</td>
    			  	<td>
    					<%=Bean.getWindowFindClub("club", id_club, sname_club, "25") %>
    			  	</td>
    			</tr>
    			<tr>
    				<td colspan="6" align="center">
    					<%=Bean.getSubmitButtonAjax("../crm/finance/bk_accountsupdate.jsp", "submit", "updateForm") %>
    					<%=Bean.getResetButton() %>
						<% String goBackHyperLink = "";
						   if (!(id==null || "".equalsIgnoreCase(id))) {
							   goBackHyperLink = "../crm/finance/bk_accountspecs.jsp?id=" + id;
						   } else {
							   goBackHyperLink = "../crm/finance/bk_accounts.jsp";
						   }
						%>
    					<%=Bean.getGoBackButton(goBackHyperLink) %>
    				</td>
    			</tr>
    		</table>
    		</form>
	
    	<% 
    	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   	}
	
} else if (process.equalsIgnoreCase("yes")) {
    
	String
   		id_bk_account 				= Bean.getDecodeParam(parameters.get("id_bk_account")),
   		cd_bk_account 				= Bean.getDecodeParam(parameters.get("cd_bk_account")),
   		name_bk_account 			= Bean.getDecodeParam(parameters.get("name_bk_account")),
   		internal_name_bk_account 	= Bean.getDecodeParam(parameters.get("internal_name_bk_account")),
   		desc_bk_account 			= Bean.getDecodeParam(parameters.get("desc_bk_account")),
   		id_bk_account_parent 		= Bean.getDecodeParam(parameters.get("id_bk_account_parent")),
   		is_group 					= Bean.getDecodeParam(parameters.get("is_group")),
   		id_bk_account_scheme_line 	= Bean.getDecodeParam(parameters.get("id_bk_account_scheme_line")),
   		segment1 					= Bean.getDecodeParam(parameters.get("segment1")),
   		segment2 					= Bean.getDecodeParam(parameters.get("segment2")),
   		segment3 					= Bean.getDecodeParam(parameters.get("segment3")),
   		segment4 					= Bean.getDecodeParam(parameters.get("segment4")),
   		segment5 					= Bean.getDecodeParam(parameters.get("segment5")),
   		segment6 					= Bean.getDecodeParam(parameters.get("segment6")),
   		segment7 					= Bean.getDecodeParam(parameters.get("segment7")),
   		segment8 					= Bean.getDecodeParam(parameters.get("segment8")),
   		segment9 					= Bean.getDecodeParam(parameters.get("segment9")),
   		segment10 					= Bean.getDecodeParam(parameters.get("segment10")),
   		segment11 					= Bean.getDecodeParam(parameters.get("segment11")),
   		segment12 					= Bean.getDecodeParam(parameters.get("segment12")),
   		segment13 					= Bean.getDecodeParam(parameters.get("segment13")),
   		segment14 					= Bean.getDecodeParam(parameters.get("segment14")),
   		segment15 					= Bean.getDecodeParam(parameters.get("segment15")),
   		exist_flag 					= Bean.getDecodeParam(parameters.get("exist_flag")),
   		id_club	 					= Bean.getDecodeParam(parameters.get("id_club")),
   	    date_end_period				= Bean.getDecodeParam(parameters.get("date_end_period")),
   	    debug					 	= Bean.getDecodeParam(parameters.get("debug"));
    
	if (action.equalsIgnoreCase("add")) { 
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK.add_bk_account("+
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [23];

		pParam[0] = name_bk_account;
		pParam[1] = desc_bk_account;
		pParam[2] = internal_name_bk_account;
		pParam[3] = id_bk_account_parent;
		pParam[4] = is_group;
		pParam[5] = id_bk_account_scheme_line;
		pParam[6] = segment1;
		pParam[7] = segment2;
		pParam[8] = segment3;
		pParam[9] = segment4;
		pParam[10] = segment5;
		pParam[11] = segment6;
		pParam[12] = segment7;
		pParam[13] = segment8;
		pParam[14] = segment9;
		pParam[15] = segment10;
		pParam[16] = segment11;
		pParam[17] = segment12;
		pParam[18] = segment13;
		pParam[19] = segment14;
		pParam[20] = segment15;
		pParam[21] = exist_flag;
		pParam[22] = id_club;
	
	 	%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/bk_accountspecs.jsp?id=" , "../crm/finance/bk_accounts.jsp") %>
		<% 	

	} else if (action.equalsIgnoreCase("remove")) { 
			
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK.delete_bk_account(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;
	
	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bk_accounts.jsp" , "") %>
		<% 	

	} else if (action.equalsIgnoreCase("removeall")) {
		
		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK.delete_all_bk_accounts(?,?)}";

 		String[] pParam = new String [1];

 		pParam[0] = club.getValue("ID_CLUB");
	 	
	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bk_accounts.jsp" , "") %>
		<% 	

	} else if (action.equalsIgnoreCase("calc_rests")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK.calc_rests(?,?,?)}";

	 	String[] pParam = new String [2];

 		pParam[0] = id_club;
 		pParam[1] = id_bk_account;
	 	
	 	%>
		<% String goBackHyperLink = "";
		   if (!(id_bk_account==null || "".equalsIgnoreCase(id_bk_account))) {
			   goBackHyperLink = "../crm/finance/bk_accountspecs.jsp?id=" + id_bk_account;
		   } else {
			   goBackHyperLink = "../crm/finance/bk_accounts.jsp";
		   }
		%>
    	<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, goBackHyperLink , "") %>
		<% 	

	} else if (action.equalsIgnoreCase("edit")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK.update_bk_account("+
 			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [23];

		pParam[0] = id;
		pParam[1] = name_bk_account;
		pParam[2] = desc_bk_account;
		pParam[3] = internal_name_bk_account;
		pParam[4] = id_bk_account_parent;
		pParam[5] = is_group;
		pParam[6] = id_bk_account_scheme_line;
		pParam[7] = segment1;
		pParam[8] = segment2;
		pParam[9] = segment3;
		pParam[10] = segment4;
		pParam[11] = segment5;
		pParam[12] = segment6;
		pParam[13] = segment7;
		pParam[14] = segment8;
		pParam[15] = segment9;
		pParam[16] = segment10;
		pParam[17] = segment11;
		pParam[18] = segment12;
		pParam[19] = segment13;
		pParam[20] = segment14;
		pParam[21] = segment15;
		pParam[22] = exist_flag;
	
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bk_accountspecs.jsp?id=" + id, "") %>
		<% 	

	} else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
	}
} else { %> 
	<%= Bean.getUnknownProcessText(process) %> <%
}

%>


</body>
</html>
