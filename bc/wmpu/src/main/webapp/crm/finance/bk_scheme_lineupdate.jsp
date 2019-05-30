	<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcFNBKSchemeLineObject"%><html>

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

String id			= Bean.getDecodeParam(parameters.get("id"));
String id_scheme	= Bean.getDecodeParam(parameters.get("id_scheme"));
String type			= Bean.getDecodeParam(parameters.get("type"));
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("participant"))
	/* вибираємо тип дії (добавити, видалити...)*/
		{
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
		%>
		<script>
			var formData = new Array (
				new Array ('segment1', 'varchar2', 1),
				new Array ('name_bk_account_scheme_line', 'varchar2', 1),
				new Array ('int_nm_bk_account_scheme_line', 'varchar2', 1)
			);
		</script>


		<%
	/*  --- Нам нужны параметры клуба --- */
	bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
	
	/*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
	        %>
<body> 
			<%= Bean.getOperationTitle(
					Bean.bk_schemeXML.getfieldTransl("h_bk_accounts_add", false),
					"Y",
					"Y") 
			%>

        <form action="../crm/finance/bk_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="participant">
			<input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_scheme" value="<%=id_scheme %>">
		<table <%=Bean.getTableDetailParam() %>>

		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("cd_bk_account_scheme_line", true) %> </td><td colspan=6>
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
			<td><%= Bean.bk_schemeXML.getfieldTransl("name_bk_account_scheme_line", true) %></td> <td><input type="text" name="name_bk_account_scheme_line" size="90" value="" class="inputfield"> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("is_group", false) %></td><td><select name="is_group" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.bk_schemeXML.getfieldTransl("int_nm_bk_account_scheme_line", true) %></td><td valign="top"><input type="text" name="int_nm_bk_account_scheme_line" size="90" value="" class="inputfield"></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("id_bk_account_scheme_ln_parent", false) %></td><td><select name="id_bk_account_scheme_ln_parent" class="inputfield"><OPTION value=""></OPTION><%= Bean.getPostingSettingsGroupOptions("", false) %></select></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.bk_schemeXML.getfieldTransl("desc_bk_account_scheme_line", false) %></td> <td valign=top><textarea name="desc_bk_account_scheme_line" cols="90" rows="4" class="inputfield"></textarea> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("exist_flag", false) %> </td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %></select></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_scheme_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
			<% if (action.equalsIgnoreCase("add")) { %>
				<%=Bean.getGoBackButton("../crm/finance/bk_schemespecs.jsp?id=" + id_scheme) %>
			<% } else { %>
				<%=Bean.getGoBackButton("../crm/finance/bk_scheme_linespecs.jsp?id=" + id) %>
			<% } %>
			</td>
		</tr>

	</table>

</form>

	        <%
	    	/*  --- Добавити запис --- */
    	} else if (action.equalsIgnoreCase("copy")) {
    		bcFNBKSchemeLineObject line = new bcFNBKSchemeLineObject(id);
    		
	        %>
<body> 
			<%= Bean.getOperationTitle(
					Bean.bk_schemeXML.getfieldTransl("h_bk_accounts_add", false),
					"Y",
					"Y") 
			%>
        <form action="../crm/finance/bk_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="participant">
			<input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id_scheme" value="<%=id_scheme %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("cd_bk_account_scheme_line", true) %> </td><td colspan=6>
			<% //int bkCount = Integer.parseInt(Bean.club_bk_account_segments_count); 
			
			   int i = 0;
			  for (i=1; i<=Integer.parseInt(club.getValue("BK_ACCOUNT_SEGMENTS_COUNT")); i++) {
			   if (i==1) {
			%>
				<input type="text" name="segment1" size="5" value="<%= line.getValue("SEGMENT1") %>" class="inputfield">
			<%} else if (i==2) { %>	
			    <input type="text" name="segment2" size="5" value="<%= line.getValue("SEGMENT2") %>" class="inputfield">
			<%} else if (i==3) { %>	
			    <input type="text" name="segment3" size="5" value="<%= line.getValue("SEGMENT3") %>" class="inputfield">
			<%} else if (i==4) { %>	
			    <input type="text" name="segment4" size="5" value="<%= line.getValue("SEGMENT4") %>" class="inputfield">
			<%} else if (i==5) { %>	
			    <input type="text" name="segment5" size="5" value="<%= line.getValue("SEGMENT5") %>" class="inputfield">
			<%} else if (i==6) { %>	
			    <input type="text" name="segment6" size="5" value="<%= line.getValue("SEGMENT6") %>" class="inputfield">
			<%} else if (i==7) { %>	
			    <input type="text" name="segment7" size="5" value="<%= line.getValue("SEGMENT7") %>" class="inputfield">
			<%} else if (i==8) { %>	
			    <input type="text" name="segment8" size="5" value="<%= line.getValue("SEGMENT8") %>" class="inputfield">
			<%} else if (i==9) { %>	
			    <input type="text" name="segment9" size="5" value="<%= line.getValue("SEGMENT9") %>" class="inputfield">
			<%} else if (i==10) { %>	
			    <input type="text" name="segment10" size="5" value="<%= line.getValue("SEGMENT10") %>" class="inputfield">
			<%} else if (i==11) { %>	
			    <input type="text" name="segment11" size="5" value="<%= line.getValue("SEGMENT11") %>" class="inputfield">
			<%} else if (i==12) { %>	
			    <input type="text" name="segment12" size="5" value="<%= line.getValue("SEGMENT12") %>" class="inputfield">
			<%} else if (i==13) { %>	
			    <input type="text" name="segment13" size="5" value="<%= line.getValue("SEGMENT13") %>" class="inputfield">
			<%} else if (i==14) { %>	
			    <input type="text" name="segment14" size="5" value="<%= line.getValue("SEGMENT14") %>" class="inputfield">
			<%} else if (i==15) { %>	
			    <input type="text" name="segment15" size="5" value="<%= line.getValue("SEGMENT15") %>" class="inputfield">
			<% } 
			}%>
			</td><td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.bk_schemeXML.getfieldTransl("name_bk_account_scheme_line", true) %></td> <td><input type="text" name="name_bk_account_scheme_line" size="90" value="<%= line.getValue("NAME_BK_ACCOUNT_SCHEME_LINE") %>" class="inputfield"> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("is_group", false) %></td><td><select name="is_group" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("IS_GROUP"), false) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.bk_schemeXML.getfieldTransl("int_nm_bk_account_scheme_line", true) %></td><td valign="top"><input type="text" name="int_nm_bk_account_scheme_line" size="90" value="<%= line.getValue("INT_NM_BK_ACCOUNT_SCHEME_LINE") %>" class="inputfield"></td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("id_bk_account_scheme_ln_parent", false) %></td><td><select name="id_bk_account_scheme_ln_parent" class="inputfield"><OPTION value=""></OPTION><%= Bean.getPostingSettingsGroupOptions(line.getValue("ID_BK_ACCOUNT_SCHEME_LN_PARENT"), false) %></select></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.bk_schemeXML.getfieldTransl("desc_bk_account_scheme_line", false) %></td> <td valign=top><textarea name="desc_bk_account_scheme_line" cols="90" rows="4" class="inputfield"><%= line.getValue("DESC_BK_ACCOUNT_SCHEME_LINE") %></textarea> </td>
			<td><%= Bean.bk_schemeXML.getfieldTransl("exist_flag", true) %> </td><td><select name="exist_flag" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", line.getValue("EXIST_FLAG"), false) %></select></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/bk_scheme_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/bk_scheme_linespecs.jsp?id=" + id) %>
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
    		name_bk_account_scheme_line 	= Bean.getDecodeParam(parameters.get("name_bk_account_scheme_line")),
    		desc_bk_account_scheme_line 	= Bean.getDecodeParam(parameters.get("desc_bk_account_scheme_line")),
    		int_nm_bk_account_scheme_line 	= Bean.getDecodeParam(parameters.get("int_nm_bk_account_scheme_line")),
    		segment1 						= Bean.getDecodeParam(parameters.get("segment1")),
    		segment2 						= Bean.getDecodeParam(parameters.get("segment2")),
    		segment3 						= Bean.getDecodeParam(parameters.get("segment3")),
    		segment4 						= Bean.getDecodeParam(parameters.get("segment4")),
    		segment5 						= Bean.getDecodeParam(parameters.get("segment5")),
    		segment6 						= Bean.getDecodeParam(parameters.get("segment6")),
    		segment7 						= Bean.getDecodeParam(parameters.get("segment7")),
    		segment8 						= Bean.getDecodeParam(parameters.get("segment8")),
    		segment9 						= Bean.getDecodeParam(parameters.get("segment9")),
    		segment10 						= Bean.getDecodeParam(parameters.get("segment10")),
    		segment11 						= Bean.getDecodeParam(parameters.get("segment11")),
    		segment12 						= Bean.getDecodeParam(parameters.get("segment12")),
    		segment13 						= Bean.getDecodeParam(parameters.get("segment13")),
    		segment14 						= Bean.getDecodeParam(parameters.get("segment14")),
    		segment15 						= Bean.getDecodeParam(parameters.get("segment15")),
    		exist_flag 						= Bean.getDecodeParam(parameters.get("exist_flag")),
    		is_group 						= Bean.getDecodeParam(parameters.get("is_group")),
    		id_bk_account_scheme_ln_parent 	= Bean.getDecodeParam(parameters.get("id_bk_account_scheme_ln_parent"));
    

		if (action.equalsIgnoreCase("add")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING_SETTINGS.add_bk_account_scheme_line("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [37];

			pParam[0] = id_scheme;
			pParam[1] = name_bk_account_scheme_line;
			pParam[2] = desc_bk_account_scheme_line;
			pParam[3] = int_nm_bk_account_scheme_line;
			pParam[4] = is_group;
			pParam[5] = id_bk_account_scheme_ln_parent;
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
			pParam[21] = "";
			pParam[22] = "";
			pParam[23] = "";
			pParam[24] = "";
			pParam[25] = "";
			pParam[26] = "";
			pParam[27] = "";
			pParam[28] = "";
			pParam[29] = "";
			pParam[30] = "";
			pParam[31] = "";
			pParam[32] = "";
			pParam[33] = "";
			pParam[34] = "";
			pParam[35] = "";
			pParam[36] = exist_flag;

		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/bk_scheme_linespecs.jsp?id=" , "../crm/finance/bk_schemespecs.jsp?id=" + id_scheme) %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING_SETTINGS.delete_bk_account_scheme_line(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/bk_schemespecs.jsp?id=" + id_scheme , "../crm/finance/bk_scheme_linespecs.jsp?id=" + id) %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
			
	 		bcFNBKSchemeLineObject setting = new bcFNBKSchemeLineObject(id);
	 		
	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING_SETTINGS.update_bk_account_scheme_line("+
	 			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [37];

			pParam[0] = id;
			pParam[1] = name_bk_account_scheme_line;
			pParam[2] = desc_bk_account_scheme_line;
			pParam[3] = int_nm_bk_account_scheme_line;
			pParam[4] = is_group;
			pParam[5] = id_bk_account_scheme_ln_parent;
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
			pParam[21] = setting.getValue("SEGMENT1_PARTICIPANT");
			pParam[22] = setting.getValue("SEGMENT2_PARTICIPANT");
			pParam[23] = setting.getValue("SEGMENT3_PARTICIPANT");
			pParam[24] = setting.getValue("SEGMENT4_PARTICIPANT");
			pParam[25] = setting.getValue("SEGMENT5_PARTICIPANT");
			pParam[26] = setting.getValue("SEGMENT6_PARTICIPANT");
			pParam[27] = setting.getValue("SEGMENT7_PARTICIPANT");
			pParam[28] = setting.getValue("SEGMENT8_PARTICIPANT");
			pParam[29] = setting.getValue("SEGMENT9_PARTICIPANT");
			pParam[30] = setting.getValue("SEGMENT10_PARTICIPANT");
			pParam[31] = setting.getValue("SEGMENT11_PARTICIPANT");
			pParam[32] = setting.getValue("SEGMENT12_PARTICIPANT");
			pParam[33] = setting.getValue("SEGMENT13_PARTICIPANT");
			pParam[34] = setting.getValue("SEGMENT14_PARTICIPANT");
			pParam[35] = setting.getValue("SEGMENT15_PARTICIPANT");
			pParam[36] = exist_flag;
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bk_scheme_linespecs.jsp?id=" + id, "") %>
			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
 	   %> <%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("setting")) {
	if (process.equalsIgnoreCase("yes")) {  
		
		String
			segment 		= Bean.getDecodeParam(parameters.get("segment")),
			cd_participant 	= Bean.getDecodeParam(parameters.get("cd_participant"));
	    
		if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_POSTING_SETTINGS.apply_participant(?,?,?,?)}";

			String[] pParam = new String [3];

			pParam[0] = id;
			pParam[1] = segment;
			pParam[2] = cd_participant;
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/bk_scheme_linespecs.jsp?id=" + id, "") %>
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
