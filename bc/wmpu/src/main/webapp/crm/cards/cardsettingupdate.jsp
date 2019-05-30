<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcCardSettingObject"%>

<%@page import="bc.objects.bcCardSettingCategoryObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_CARDSETTING";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id			= Bean.getDecodeParamPrepare(parameters.get("id"));
String id_club		= Bean.getDecodeParamPrepare(parameters.get("id_club"));
String action		= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process		= Bean.getDecodeParamPrepare(parameters.get("process"));
String type			= Bean.getDecodeParamPrepare(parameters.get("type"));

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("edit")) { 
			String    
				telgr_name_card_status		= Bean.getDecodeParam(parameters.get("telgr_name_card_status")),
				day_next_online				= Bean.getDecodeParam(parameters.get("day_next_online"));
		    
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_SETTING_UI.update_card_status("+
				"?,?,?,?)}";

			String[] pParam = new String [3];
				
			pParam[0] = id;
			pParam[1] = telgr_name_card_status;
			pParam[2] = day_next_online;
				
		 	%>
			<%= Bean.getCallResultCheckParam(
					"",
					"CARDS_CARDSETTINGS_INFO",
					"UPDATE", 
					callSQL, 
					pParam,
					"../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club, 
					"") %>
			<% 	
	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("category")) {
	
	String id_category		= Bean.getDecodeParam(parameters.get("id_category"));
	
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
%> 
		<script>
			var formData = new Array (
				new Array ('id_category', 'varchar2', 1),
				new Array ('club_bon', 'varchar2', 1),
				new Array ('club_disc', 'varchar2', 1),
				new Array ('max_bon_st', 'varchar2', 1),
				new Array ('max_disc_st', 'varchar2', 1),
				new Array ('bonus_transfer_term', 'varchar2', 1),
				new Array ('bonus_calc_term', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>
		<%= Bean.getOperationTitle(
			Bean.cardsettingXML.getfieldTransl("LAB_ADD", false),
			"Y",
			"N") 
		%>
        <form action="../crm/cards/cardsettingupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		    <input type="hidden" name="action" value="add">
		    <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="category">
	    	<input type="hidden" name="id" value="<%= id %>">
	    	<input type="hidden" name="id_club" value="<%= id_club %>">

		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%=Bean.cardsettingXML.getfieldTransl("name_card_status", false)%> </td><td><input type="text" name="name_club" size="25" value="<%= Bean.getCardStatusName(id) %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(id_club) %>
			  	</td>
			  	<td><input type="text" name="name_club" size="25" value="<%= Bean.getClubShortName(id_club) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%=Bean.cardsettingXML.getfieldTransl("name_category", true)%> </td><td><select name="id_category_name" class="inputfield"><%=Bean.getClubCardCategoryOptions("", true)%></select></td>
			</tr>
			<tr>
				<td><%=Bean.cardsettingXML.getfieldTransl("club_bon", true)%></td><td><input type="text" name="club_bon" size="20" value="" class="inputfield"></td>
				<td><%=Bean.cardsettingXML.getfieldTransl("club_disc", true)%></td><td><input type="text" name="club_disc" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%=Bean.cardsettingXML.getfieldTransl("max_bon_st", true)%></td><td><input type="text" name="max_bon_st" size="20" value="" class="inputfield"></td>
				<td><%=Bean.cardsettingXML.getfieldTransl("max_disc_st", true)%></td><td><input type="text" name="max_disc_st" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%=Bean.cardsettingXML.getfieldTransl("bonus_transfer_term", true)%></td><td><input type="text" name="bonus_transfer_term" size="20" value="" class="inputfield"></td>
				<td><%=Bean.cardsettingXML.getfieldTransl("bonus_calc_term", true)%></td><td><input type="text" name="bonus_calc_term" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/cardsettingupdate.jsp") %>
					<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add2")) { %>
					<%=Bean.getGoBackButton("../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club) %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club) %>
				<% } %>
				</td>
			</tr>

		</table>

	</form>

		        <%
	    } else if (action.equalsIgnoreCase("edit")) {
	    	
	    	bcCardSettingCategoryObject category = new bcCardSettingCategoryObject(id_category);
	    	
	    		%> 
	    		<script>
	    			var formData = new Array (
	    				new Array ('club_bon', 'varchar2', 1),
	    				new Array ('club_disc', 'varchar2', 1),
	    				new Array ('max_bon_st', 'varchar2', 1),
	    				new Array ('max_disc_st', 'varchar2', 1),
	    				new Array ('bonus_transfer_term', 'varchar2', 1),
	    				new Array ('bonus_calc_term', 'varchar2', 1)
	    			);
	    			function myValidateForm() {
	    				return validateForm(formData);
	    			}
	    		</script>
	    		<%= Bean.getOperationTitle(
	    			Bean.cardsettingXML.getfieldTransl("LAB_ADD", false),
	    			"Y",
	    			"N") 
	    		%>
	            <form action="../crm/cards/cardsettingupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	    		    <input type="hidden" name="action" value="edit">
	    		    <input type="hidden" name="process" value="yes">
	    	    	<input type="hidden" name="type" value="category">
	    	    	<input type="hidden" name="id" value="<%= id %>">
	    	    	<input type="hidden" name="id_club" value="<%= id_club %>">
	    	    	<input type="hidden" name="id_category" value="<%= id_category %>">

    		<table <%=Bean.getTableDetailParam() %>>
    			<tr>
					<td><%= Bean.cardsettingXML.getfieldTransl("name_card_status", false) %> </td><td><input type="text" name="name_card_status" size="30" value="<%= category.getValue("NAME_CARD_STATUS") %>" readonly="readonly" class="inputfield-ro" title="<%= category.getValue("ID_CARD_STATUS") %>"></td>
					<td><%= Bean.clubXML.getfieldTransl("club", false) %>
						<%= Bean.getGoToClubLink(category.getValue("ID_CLUB")) %>
					</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(category.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
				</tr>
				<tr>
					<td><%= Bean.cardsettingXML.getfieldTransl("name_category", false) %> </td><td><input type="text" name="name_category" size="30" value="<%= category.getValue("NAME_CATEGORY") %>" readonly="readonly" class="inputfield-ro" title="<%= category.getValue("ID_CATEGORY_NAME") %>"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><%= Bean.cardsettingXML.getfieldTransl("club_bon", true) %></td><td><input type="text" name="club_bon" size="20" value="<%= category.getValue("CLUB_BON_FRMT") %>" class="inputfield"></td>
					<td><%= Bean.cardsettingXML.getfieldTransl("club_disc", true) %></td><td><input type="text" name="club_disc" size="20" value="<%= category.getValue("CLUB_DISC_FRMT") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.cardsettingXML.getfieldTransl("max_bon_st", true) %></td><td><input type="text" name="max_bon_st" size="20" value="<%= category.getValue("MAX_BON_ST_FRMT") %>" class="inputfield"></td>
					<td><%= Bean.cardsettingXML.getfieldTransl("max_disc_st", true) %></td><td><input type="text" name="max_disc_st" size="20" value="<%= category.getValue("MAX_DISC_ST_FRMT") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.cardsettingXML.getfieldTransl("bonus_transfer_term", true) %></td><td><input type="text" name="bonus_transfer_term" size="20" value="<%= category.getValue("BONUS_TRANSFER_TERM") %>" class="inputfield"></td>
					<td><%= Bean.cardsettingXML.getfieldTransl("bonus_calc_term", true) %></td><td><input type="text" name="bonus_calc_term" size="20" value="<%= category.getValue("BONUS_CALC_TERM") %>" class="inputfield"></td>
				</tr>

				<%=	Bean.getIdCreationAndMoficationRecordFields(
						category.getValue("ID_CATEGORY"),
						category.getValue(Bean.getCreationDateFieldName()),
						category.getValue("CREATED_BY"),
						category.getValue(Bean.getLastUpdateDateFieldName()),
						category.getValue("LAST_UPDATE_BY")
				) %>
	    			<tr>
	    				<td colspan="6" align="center">
	    					<%=Bean.getSubmitButtonAjax("../crm/cards/cardsettingupdate.jsp") %>
	    					<%=Bean.getResetButton() %>
	    				<% if (action.equalsIgnoreCase("add2")) { %>
	    					<%=Bean.getGoBackButton("../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club) %>
	    				<% } else { %>
	    					<%=Bean.getGoBackButton("../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club) %>
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
		String    
			id_category_name	= Bean.getDecodeParam(parameters.get("id_category_name")),
	    	club_bon			= Bean.getDecodeParam(parameters.get("club_bon")),
		    max_bon_st			= Bean.getDecodeParam(parameters.get("max_bon_st")),
		    bonus_transfer_term	= Bean.getDecodeParam(parameters.get("bonus_transfer_term")),
		    club_disc			= Bean.getDecodeParam(parameters.get("club_disc")),
		    max_disc_st			= Bean.getDecodeParam(parameters.get("max_disc_st")),
		    bonus_calc_term		= Bean.getDecodeParam(parameters.get("bonus_calc_term"));
		    
		if (action.equalsIgnoreCase("add")) { 
		    	
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_SETTING_UI.add_category_param("+
				"?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [9];
					
			pParam[0] = id;
			pParam[1] = id_category_name;
			pParam[2] = club_bon;
			pParam[3] = max_bon_st;
			pParam[4] = bonus_transfer_term;
			pParam[5] = club_disc;
			pParam[6] = max_disc_st;
			pParam[7] = bonus_calc_term;
			pParam[8] = id_club;
			
		 	%>
			<%= Bean.getCallResultCheckParam(
					"",
					"CARDS_CARDSETTINGS_INFO",
					"INSERT", 
					callSQL, 
					pParam,
					"../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club + "&id_category=", 
					"") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) { 
		    	
		   	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_SETTING_UI.delete_category_param(?,?)}";

			String[] pParam = new String [9];
						
			pParam[0] = id_category;
				
		 	%>
			<%= Bean.getCallResultCheckParam(
					"",
					"CARDS_CARDSETTINGS_INFO",
					"DELETE", 
					callSQL, 
					pParam,
					"../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club + "&id_category=", 
					"") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) { 
				
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_SETTING_UI.update_category_param("+
				"?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [7];
						
			pParam[0] = id_category;
			pParam[1] = club_bon;
			pParam[2] = max_bon_st;
			pParam[3] = bonus_transfer_term;
			pParam[4] = club_disc;
			pParam[5] = max_disc_st;
			pParam[6] = bonus_calc_term;
				
		 	%>
			<%= Bean.getCallResultCheckParam(
					"",
					"CARDS_CARDSETTINGS_INFO",
					"UPDATE", 
					callSQL, 
					pParam,
					"../crm/cards/cardsettingspecs.jsp?id=" + id + "&id_club=" + id_club + "&id_category=", 
					"") %>
			<% 	
	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}%>
</body>

<%@page import="java.util.HashMap"%></html>
