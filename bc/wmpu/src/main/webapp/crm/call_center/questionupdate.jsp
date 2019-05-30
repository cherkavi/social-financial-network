<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcCallCenterFAQObject"%>
<%@page import="bc.objects.bcCallCenterQuestionObject"%>
<%@page import="bc.objects.bcCallCenterActivityObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>


<%@page import="java.util.ArrayList"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CALL_CENTER_QUESTIONS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add")|| action.equalsIgnoreCase("add2")) {
    	
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    	%>
		<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_add_question", false),
				"Y",
				"Y") 
		%>
    <script>
		var formDataQuestion = new Array (
			new Array ('title', 'varchar2', 1),
			new Array ('due_date', 'varchar2', 1),
			new Array ('cc_contact_type', 'varchar2', 1),
			new Array ('cd_cc_question_type', 'varchar2', 1),
			new Array ('cd_cc_question_status', 'varchar2', 1),
			new Array ('cd_cc_question_important', 'varchar2', 1),
			new Array ('cd_cc_question_urgent', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('exist_flag', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataQuestion);
		}
	</script>
    
	<form action="../crm/call_center/questionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_question", false) %></td><td><input type="text" name="id_cc_question" size="20" value="" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_contact_type", true) %></td><td><select name="cc_contact_type" class="inputfield"><%= Bean.getCallCenterContactTypeOptions("PHONE", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("title", true) %></td><td><input type="text" name="title" size="40" value="" class="inputfield"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_type", true) %></td><td><select name="cd_cc_question_type" class="inputfield"><%= Bean.getCallCenterQuestionTypeOptions("SYSTEM", false) %></select></td>
		</tr>
		<tr>
		  	<td><%= Bean.call_centerXML.getfieldTransl("due_date", true) %></td><td><%=Bean.getCalendarInputField("due_date", Bean.getSysDate(), "20") %></td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_status", true) %></td><td><select name="cd_cc_question_status" class="inputfield"><%= Bean.getCallCenterQuestionStatusOptions("BEGUN", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.getClubCardXMLFieldTransl("name_nat_prs", false) %></td>
			<td>
				<%=Bean.getWindowFindCallCenterNatPrs("nat_prs", "", "", "35") %>
			</td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_important", true) %></td><td><select name="cd_cc_question_important" class="inputfield"><%= Bean.getCallCenterQuestionImportantOptions("HIGH", false) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.getClubCardXMLFieldTransl("cd_card1", false) %></td> 
			<td>
				<%=Bean.getWindowFindCallCenterClubCard("", "", "", "", "35") %>
			</td>			
			<td><%= Bean.call_centerXML.getfieldTransl("cc_question_urgent", true) %></td><td><select name="cd_cc_question_urgent" class="inputfield"><%= Bean.getCallCenterQuestionUrgentOptions("HIGH", false) %></select></td>
		</tr>
		<tr>
		<td><%= Bean.call_centerXML.getfieldTransl("assigned_user", false) %></td>
			<td>
				<%=Bean.getWindowFindCallCenterUser("user", Bean.loginUser.getValue("ID_USER"), Bean.getCallCenterUserName(Bean.loginUser.getValue("ID_USER")), "35") %>
			</td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
		  	</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
 		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_faq", false) %></td>
			<td>
				<%=Bean.getWindowFindCallCenterFAQ("cc_faq", "", "35") %>
			</td>
			<td><%= Bean.call_centerXML.getfieldTransl("exist_flag", true) %></td><td><select name="exist_flag" class="inputfield"><%= Bean.getYesNoLookupOptions("Y", false) %></select></td>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("description", false) %></td><td colspan="5"><textarea name="description" cols="120" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/questionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/call_center/questions.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/call_center/questionspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	</form>
	<%= Bean.getCalendarScript("due_date", false) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	}

	else if (process.equalsIgnoreCase("yes")){
    	String
    		cd_cc_contact_type 			= Bean.getDecodeParam(parameters.get("cc_contact_type")),
    		due_date 					= Bean.getDecodeParam(parameters.get("due_date")),
    		card_serial_number 			= Bean.getDecodeParam(parameters.get("card_serial_number")),
    		card_id_issuer 				= Bean.getDecodeParam(parameters.get("id_issuer")),
    		card_id_payment_system 		= Bean.getDecodeParam(parameters.get("id_payment_system")),
    		id_nat_prs 					= Bean.getDecodeParam(parameters.get("id_nat_prs")),
    		title 						= Bean.getDecodeParam(parameters.get("title")),
    		description 				= Bean.getDecodeParam(parameters.get("description")),
    		note 						= Bean.getDecodeParam(parameters.get("note")),
    		resolution 					= Bean.getDecodeParam(parameters.get("resolution")),
    		solution 					= Bean.getDecodeParam(parameters.get("solution")),
    		cd_cc_question_type 		= Bean.getDecodeParam(parameters.get("cd_cc_question_type")),
   			cd_cc_question_status 		= Bean.getDecodeParam(parameters.get("cd_cc_question_status")),
    		cd_cc_question_important 	= Bean.getDecodeParam(parameters.get("cd_cc_question_important")),
    		cd_cc_question_urgent 		= Bean.getDecodeParam(parameters.get("cd_cc_question_urgent")),
    		id_assigned_user 			= Bean.getDecodeParam(parameters.get("id_user")),
    		exist_flag 					= Bean.getDecodeParam(parameters.get("exist_flag")),
    		id_cc_faq 					= Bean.getDecodeParam(parameters.get("id_cc_faq")),
    		id_club 					= Bean.getDecodeParam(parameters.get("id_club"));

    	if (action.equalsIgnoreCase("add")) { 
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_question("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(cd_cc_contact_type);
			pParam.add(due_date);
			pParam.add(card_serial_number);
			pParam.add(card_id_issuer);
			pParam.add(card_id_payment_system);
			pParam.add(id_nat_prs);
			pParam.add(title);
			pParam.add(description);
			pParam.add(cd_cc_question_type);
			pParam.add(cd_cc_question_status);
			pParam.add(cd_cc_question_important);
			pParam.add(cd_cc_question_urgent);
			pParam.add(id_assigned_user);
			pParam.add(exist_flag);
			pParam.add(id_cc_faq);
			pParam.add(id_club);
			pParam.add(Bean.getDateFormat());
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/call_center/questionspecs.jsp?id=" , "../crm/call_center/questions.jsp") %>
			<% 	
 		

  		} else if (action.equalsIgnoreCase("remove")) { 
	   
  			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.delete_question(?,?)}";

  			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(cd_cc_contact_type);

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/call_center/questions.jsp" , "") %>
			<% 	
	    } else if (action.equalsIgnoreCase("edit")) { 
	    	
	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_question(" +
	 			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	 		ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);
			pParam.add(cd_cc_contact_type);
			pParam.add(due_date);
			pParam.add(card_serial_number);
			pParam.add(card_id_issuer);
			pParam.add(card_id_payment_system);
			pParam.add(id_nat_prs);
			pParam.add(title);
			pParam.add(cd_cc_question_type);
			pParam.add(cd_cc_question_status);
			pParam.add(cd_cc_question_important);
			pParam.add(cd_cc_question_urgent);
			pParam.add(id_assigned_user);
			pParam.add(exist_flag);
			pParam.add(id_cc_faq);
			pParam.add(Bean.getDateFormat());
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/call_center/questionspecs.jsp?id=" + id, "") %>
			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
	
	
} else if (type.equalsIgnoreCase("description")) {
	if (process.equalsIgnoreCase("yes"))	{
    	if (action.equalsIgnoreCase("edit")) { 
    		String
    	    	description 	= Bean.getDecodeParam(parameters.get("description")),
    	    	note			= Bean.getDecodeParam(parameters.get("note"));
 
   	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_question_description(" + 
   	 			"?,?,?,?)}";

   	 		ArrayList<String> pParam = new ArrayList<String>();
    				
    		pParam.add(id);
    		pParam.add(description);
    		pParam.add(note);
    		
   		 	%>
   			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/call_center/questionspecs.jsp?id=" + id, "") %>
   			<% 	
			
    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("solution")) {
	if (process.equalsIgnoreCase("yes"))	{
    	if (action.equalsIgnoreCase("edit")) { 
    		String
    	    	resolution 	= Bean.getDecodeParam(parameters.get("resolution")),
    	    	solution	= Bean.getDecodeParam(parameters.get("solution"));
    		
   	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_question_resolution('" + 
   	 			"?,?,?,?)}";

   	 		ArrayList<String> pParam = new ArrayList<String>();
   	    				
   	    	pParam.add(id);
   	    	pParam.add(resolution);
   	    	pParam.add(solution);
    		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/call_center/questionspecs.jsp?id=" + id, "") %>
			<% 	
    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("activity")) {
	
	String 
	id_cc_activity 	= Bean.getDecodeParam(parameters.get("id_activity")),
	id_user 		= Bean.getDecodeParam(parameters.get("id_user")),
	begin_date 		= Bean.getDecodeParam(parameters.get("begin_date")),
	end_date 		= Bean.getDecodeParam(parameters.get("end_date")),
	description 	= Bean.getDecodeParam(parameters.get("description"));

	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
	        %> 
	
		<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_add_activity", false),
				"Y",
				"Y") 
		%>
    <script>
		var formDataQuestionActivity = new Array (
			new Array ('name_user', 'varchar2', 1),
			new Array ('id_begin_date', 'varchar2', 1),
			new Array ('id_end_date', 'varchar2', 1),
			new Array ('description', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataQuestionActivity);
		}
	</script>
    
	<form action="../crm/call_center/questionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="activity">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
	<table <%=Bean.getTableDetailParam() %>>
    <tr>
		<td><%= Bean.call_centerXML.getfieldTransl("name_user", true) %></td>
		<td>
			<%=Bean.getWindowFindCallCenterUser("user", "", "", "10") %>
		</td>
			<td><%= Bean.call_centerXML.getfieldTransl("description", true) %></td><td rowspan="3" valign="top"><textarea name="description" cols="70" rows="5" class="inputfield"></textarea></td>
	</tr>
    <tr>
		<td><%= Bean.call_centerXML.getfieldTransl("begin_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_date", Bean.getSysDate(), "15") %></td>
	</tr>
    <tr>
		<td><%= Bean.call_centerXML.getfieldTransl("end_date", true) %></td> <td><%=Bean.getCalendarInputField("end_date", "", "15") %></td>
	</tr>
	<tr>
		<td colspan="6" align="center">
			<%=Bean.getSubmitButtonAjax("../crm/call_center/questionupdate.jsp") %>
			<%=Bean.getResetButton() %>
			<%=Bean.getGoBackButton("../crm/call_center/questionspecs.jsp?id=" + id) %>
		</td>
	</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("begin_date", false) %>
	<%= Bean.getCalendarScript("end_date", false) %>

		<% 
		} else if (action.equalsIgnoreCase("edit")) {
			
			bcCallCenterActivityObject activity = new bcCallCenterActivityObject(id_cc_activity);
			
	        %> 
	
		<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_update_activity", false),
				"Y",
				"Y") 
		%>
    <script>
		var formDataQuestionActivity = new Array (
			new Array ('name_user', 'varchar2', 1),
			new Array ('id_begin_date', 'varchar2', 1),
			new Array ('id_end_date', 'varchar2', 1),
			new Array ('description', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataQuestionActivity);
		}
	</script>
    
	<form action="../crm/call_center/questionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="activity">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id %>">
        <input type="hidden" name="id_activity" value="<%=id_cc_activity %>">
	<table <%=Bean.getTableDetailParam() %>>
	    <tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_user", true) %></td>
			<td>
				<%=Bean.getWindowFindCallCenterUser("user", activity.getValue("ID_USER"), activity.getValue("NAME_USER"), "10") %>
			</td>
			<td><%= Bean.call_centerXML.getfieldTransl("description", true) %></td><td rowspan="3" valign="top"><textarea name="description" cols="70" rows="5" class="inputfield"><%=activity.getValue("DESCRIPTION") %></textarea></td>
		</tr>
	    <tr>
			<td><%= Bean.call_centerXML.getfieldTransl("begin_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_date", activity.getValue("BEGIN_DATE_FRMT"), "15") %></td>
		</tr>
	    <tr>
			<td><%= Bean.call_centerXML.getfieldTransl("end_date", true) %></td> <td><%=Bean.getCalendarInputField("end_date", activity.getValue("END_DATE_FRMT"), "15") %></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				activity.getValue(Bean.getCreationDateFieldName()),
				activity.getValue("CREATED_BY"),
				activity.getValue(Bean.getLastUpdateDateFieldName()),
				activity.getValue("LAST_UPDATE_BY")
			) %>
	<tr>
		<td colspan="6" align="center">
			<%=Bean.getSubmitButtonAjax("../crm/call_center/questionupdate.jsp") %>
			<%=Bean.getResetButton() %>
			<%=Bean.getGoBackButton("../crm/call_center/questionspecs.jsp?id=" + id) %>
		</td>
	</tr>
	</table>
	</form>
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("begin_date", false) %>
	<%= Bean.getCalendarScript("end_date", false) %>

		<%
			
		} else { %> 
		<%= Bean.getUnknownActionText(action) %><%
			
		}
	} else if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("add")) {
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_activity(" + 
				"?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
	   	    				
	   	    pParam.add(id);
	   	    pParam.add(id_user);
	   	    pParam.add(begin_date);
	   	 	pParam.add(end_date);
	   	    pParam.add(description);
	   	    pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/call_center/questionspecs.jsp?id=" + id + "&activity=", "") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) {
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.delete_activity(?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
		   	    				
		   	pParam.add(id_cc_activity);

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/call_center/questionspecs.jsp?id=" + id, "") %>
			<% 	

	 	} else if (action.equalsIgnoreCase("edit")) {
	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_activity(" + 
	 			"?,?,?,?,?,?,?)}";

	 		ArrayList<String> pParam = new ArrayList<String>();
		   	    				
		   pParam.add(id_cc_activity);
		   pParam.add(id_user);
		   pParam.add(begin_date);
		   pParam.add(end_date);
		   pParam.add(description);
		   pParam.add(Bean.getDateFormat());

		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/call_center/questionspecs.jsp?id=" + id, "") %>
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
