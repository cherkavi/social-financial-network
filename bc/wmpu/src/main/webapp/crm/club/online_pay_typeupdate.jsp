<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcTerminalOnlinePaymentTypeObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_ONLINE_PAY_TYPE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String p_id			= Bean.getDecodeParam(parameters.get("id"));
String p_type		= Bean.getDecodeParam(parameters.get("type"));
String p_action		= Bean.getDecodeParam(parameters.get("action")); 
String p_process	= Bean.getDecodeParam(parameters.get("process"));

if (p_id==null || ("".equalsIgnoreCase(p_id))) p_id="empty";
if (p_type==null || ("".equalsIgnoreCase(p_type))) p_type="empty";
if (p_action==null || ("".equalsIgnoreCase(p_action))) p_action="empty";
if (p_process==null || ("".equalsIgnoreCase(p_process))) p_process="empty";

if (p_type.equalsIgnoreCase("general")) {
	if (p_process.equalsIgnoreCase("no")) {
		if (p_action.equalsIgnoreCase("add") || p_action.equalsIgnoreCase("add2")) { 
		
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
		%>

			<%= Bean.getOperationTitle(
					Bean.clubXML.getfieldTransl("h_add_online_payment_type", false),
					"Y",
					"N") 
			%>
			<script>
				var formClubParam = new Array (
					new Array ('cd_club', 'varchar2', 1),
					new Array ('name_club_online_pay_type', 'varchar2', 1),
					new Array ('exist_club_online_pay_type', 'varchar2', 1),
					new Array ('term_card_req_club_pay_id_def', 'varchar2', 1),
					new Array ('need_calc_pin', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formClubParam);
				}
			</script>

	<form action="../crm/club/online_pay_typeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formClubParam)">
		<input type="hidden" name="type" value="general">
	    <input type="hidden" name="action" value="add">
	    <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_club_online_pay_type", true) %> </td><td><input type="text" name="name_club_online_pay_type" size="60" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td> 
			<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"),"35") %>
			</td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.clubXML.getfieldTransl("desc_club_online_pay_type", false) %></td> <td rowspan="3"><textarea name="desc_club_online_pay_type" cols="57" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.clubXML.getfieldTransl("term_card_req_club_pay_id_def", true) %> </td><td><input type="text" name="term_card_req_club_pay_id_def" size="20" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("need_calc_pin", true) %> </td><td><select name="need_calc_pin" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("exist_club_online_pay_type", true) %> </td><td><select name="exist_club_online_pay_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/online_pay_typeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(p_action)) { %>
					<%=Bean.getGoBackButton("../crm/club/online_pay_type.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/club/online_pay_typespecs.jsp?id=" + p_id) %>
				<% } %>
			</td>
		</tr>
	</table>
	</form>
		<% 	} else { %>
			<%= Bean.getUnknownActionText(p_action) %><% 
		}
	} else if (p_process.equalsIgnoreCase("yes")) {
		String
			id_club					 		= Bean.getDecodeParam(parameters.get("id_club")),
			name_club_online_pay_type 		= Bean.getDecodeParam(parameters.get("name_club_online_pay_type")),
			desc_club_online_pay_type 		= Bean.getDecodeParam(parameters.get("desc_club_online_pay_type")),
			exist_club_online_pay_type 		= Bean.getDecodeParam(parameters.get("exist_club_online_pay_type")),
			term_card_req_club_pay_id_def 	= Bean.getDecodeParam(parameters.get("term_card_req_club_pay_id_def")),
			need_calc_pin 					= Bean.getDecodeParam(parameters.get("need_calc_pin"));

		if (p_action.equalsIgnoreCase("add")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.add_online_payment_type(" +
			"?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [6];
				
		 	pParam[0] = id_club;
			pParam[1] = name_club_online_pay_type;
			pParam[2] = desc_club_online_pay_type;
			pParam[3] = term_card_req_club_pay_id_def;
			pParam[4] = exist_club_online_pay_type;
			pParam[5] = need_calc_pin;
			
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club/online_pay_typespecs.jsp?id=", "../crm/club/online_pay_type.jsp") %>
		
		<% 	} else if (p_action.equalsIgnoreCase("remove")) {
	
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.delete_online_payment_type(?,?)}";

			String[] pParam = new String [1];
					
			pParam[0] = p_id;
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club/online_pay_type.jsp", "") %>
		
		<% 	} else if (p_action.equalsIgnoreCase("update")) {
	
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.update_online_payment_type(" +
	 		"?,?,?,?,?,?,?)}";

			String[] pParam = new String [6];
						
			pParam[0] = p_id;
			pParam[1] = name_club_online_pay_type;
			pParam[2] = desc_club_online_pay_type;
			pParam[3] = term_card_req_club_pay_id_def;
			pParam[4] = exist_club_online_pay_type;
			pParam[5] = need_calc_pin;
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club/online_pay_typespecs.jsp?id=" + p_id, "") %>

		<% 	} else { %>
			<%= Bean.getUnknownActionText(p_action) %><% 
		} 
	} else { %>
		<%= Bean.getUnknownProcessText(p_process) %><% 
	}
} else if (p_type.equalsIgnoreCase("term")) {
	
	String 
		id_term 						= Bean.getDecodeParam(parameters.get("id_term")),
		id_term_online_pay_type 		= Bean.getDecodeParam(parameters.get("id_term_online_pay_type")),
		id_club_online_pay_type 		= Bean.getDecodeParam(parameters.get("id_club_online_pay_type")),
		term_card_req_club_pay_id 		= Bean.getDecodeParam(parameters.get("term_card_req_club_pay_id")),
		exist_term_online_pay_type 		= Bean.getDecodeParam(parameters.get("exist_term_online_pay_type")),
		need_calc_pin 					= Bean.getDecodeParam(parameters.get("need_calc_pin"));

	if (p_process.equalsIgnoreCase("no")) {
		if (p_action.equalsIgnoreCase("add1")) {
    		
			%>
			<script>
				var formData = new Array (
					new Array ('id_club_online_pay_type', 'varchar2', 1),
					new Array ('name_club_online_pay_type', 'varchar2', 1),
					new Array ('exist_club_online_pay_type', 'varchar2', 1),
					new Array ('term_card_req_club_pay_id_def', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitle(
				Bean.clubXML.getfieldTransl("h_add_online_payment_type", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/club/online_pay_typeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="online">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=p_id %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubXML.getfieldTransl("name_club_online_pay_type", true) %> </td><td><input type="text" name="name_club_online_pay_type" size="60" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("term_card_req_club_pay_id", true) %> </td><td><input type="text" name="term_card_req_club_pay_id" size="20" value="" class="inputfield"></td>
		</tr>
	    <tr>
			<td rowspan="3"><%= Bean.clubXML.getfieldTransl("desc_club_online_pay_type", false) %></td> <td rowspan="3"><textarea name="desc_club_online_pay_type" cols="57" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.clubXML.getfieldTransl("need_calc_pin", false) %> </td><td><select name="need_calc_pin" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
		</tr>
	    <tr>
			<td><%= Bean.clubXML.getfieldTransl("exist_club_online_pay_type", true) %> </td><td><select name="exist_club_online_pay_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", true) %></select></td>
		</tr>
	    <tr>
			<td colspan="2">&nbsp;</td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/online_pay_typeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/online_pay_typespecs.jsp?id=" + p_id) %>
			</td>
		</tr>

	</table>
	</form>

	        <%
    	} else if (p_action.equalsIgnoreCase("edit")) {
    		
    		bcTerminalOnlinePaymentTypeObject pay = new bcTerminalOnlinePaymentTypeObject(id_term_online_pay_type);
	        
	        %>
			<script>
				var formData = new Array (
					new Array ('term_card_req_club_pay_id', 'varchar2', 1),
					new Array ('exist_term_online_pay_type', 'varchar2', 1)
				);
			</script>

		<%= Bean.getOperationTitle(
				Bean.clubXML.getfieldTransl("h_update_online_payment_type", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/club/online_pay_typeupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="term">
	        <input type="hidden" name="action" value="edit">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="id" value="<%=p_id %>">
	        <input type="hidden" name="id_term_online_pay_type" value="<%=id_term_online_pay_type %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("id_term", false) %> </td><td><input type="text" name="id_term" size="20" value="<%= pay.getValue("ID_TERM") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("term_card_req_club_pay_id", true) %> </td><td><input type="text" name="term_card_req_club_pay_id" size="20" value="<%= pay.getValue("TERM_CARD_REQ_CLUB_PAY_ID") %>" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.terminalXML.getfieldTransl("name_club_online_pay_type", false) %> </td><td><input type="text" name="name_club_online_pay_type" size="60" value="<%= pay.getValue("NAME_CLUB_ONLINE_PAY_TYPE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.terminalXML.getfieldTransl("need_calc_pin", false) %> </td><td><select name="need_calc_pin" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", pay.getValue("NEED_CALC_PIN"), true) %></select></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.terminalXML.getfieldTransl("exist_term_online_pay_type", true) %> </td><td><select name="exist_term_online_pay_type" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", pay.getValue("EXIST_TERM_ONLINE_PAY_TYPE"), true) %></select></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				pay.getValue(Bean.getCreationDateFieldName()),
				pay.getValue("CREATED_BY"),
				pay.getValue(Bean.getLastUpdateDateFieldName()),
				pay.getValue("LAST_UPDATE_BY")
			) %>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/online_pay_typeupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club/online_pay_typespecs.jsp?id=" + p_id) %>
			</td>
		</tr>

	</table>
	</form>
		 
		<%} else {
    	    %> <%= Bean.getUnknownActionText(p_action) %><%
    	}
	} else if (p_process.equalsIgnoreCase("yes")) {
		if (p_action.equalsIgnoreCase("add1")) { 

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.add_term_onl_payment_type("+
				"?,?,?,?,?, ?,?)}";

		 	String[] pParam = new String [5];
		 		 	    	      				
		 	pParam[0] = id_term;
			pParam[1] = id_club_online_pay_type;
			pParam[2] = term_card_req_club_pay_id;
			pParam[3] = exist_term_online_pay_type;
			pParam[4] = need_calc_pin;
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club/online_pay_typespecs.jsp?id=" + p_id + "&id_term_online_pay_type=", "") %>
			<% 	
	   
		} else if (p_action.equalsIgnoreCase("remove1")) { 
		   
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.delete_term_onl_payment_type(?,?)}";

			String[] pParam = new String [1];
					 	    	      				
			pParam[0] = id_term_online_pay_type;

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club/online_pay_typespecs.jsp?id=" + p_id, "") %>
			<% 	
		     
		} else if (p_action.equalsIgnoreCase("edit")) { 
			
		 	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CLUB_UI.update_term_onl_payment_type(" +
		 		"?,?,?,?, ?)}";

			String[] pParam = new String [4];
			 		 	    	      				
			pParam[0] = id_term_online_pay_type;
			pParam[1] = term_card_req_club_pay_id;
			pParam[2] = exist_term_online_pay_type;
			pParam[3] = need_calc_pin;
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club/online_pay_typespecs.jsp?id=" + p_id, "") %>
			<% 	
		
		} else { %>
			<%= Bean.getUnknownActionText(p_action) %><% 
		}
	} else { %>
		<%= Bean.getUnknownProcessText(p_process) %> <br><% 
	}
} else {%> 
	<%= Bean.getUnknownTypeText(p_type) %><%
}
  
%>
</body>
</html>
