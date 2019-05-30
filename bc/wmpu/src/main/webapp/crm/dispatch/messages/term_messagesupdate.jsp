<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcClubShortObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_MESSAGES_TERM";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
	        
 	        %>
			<%= Bean.getOperationTitle(
					Bean.messageXML.getfieldTransl("h_message_add", false),
					"Y",
					"Y") 
			%>
		<script>
			var formData = new Array (
				new Array ('cd_term_message_type', 'varchar2', 1),
				new Array ('text_term_message', 'varchar2', 1),
				new Array ('basis_for_operation', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('begin_action_date', 'varchar2', 1),
				new Array ('cd_term_message_state', 'varchar2', 1),
				new Array ('is_archive', 'varchar2', 1)
			);
		</script>

		<%= Bean.getMessageLengthTextAreaInitialScript("text_term_message", "length_term_message") %>

	    <form action="../crm/dispatch/messages/term_messagesupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=id %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("cd_term_message_type", true) %> </td> <td><select name="cd_term_message_type" class="inputfield"><%= Bean.getTermMessageTypeOptions("CARD_REQUEST", true) %></select></td>
				<td><%= Bean.clubXML.getfieldTransl("club", true) %>
					<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
				</td> 
				<td>
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"),"35") %>
				</td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("cd_term_message_state", true) %></td><td valign="top"><select name="cd_term_message_state" class="inputfield"><%= Bean.getTermMessageStateOptions("PREPARED", true) %></select></td>
				<td><%= Bean.messageXML.getfieldTransl("begin_action_date", true) %></td> <td><%=Bean.getCalendarInputField("begin_action_date", Bean.getSysDate(), "10") %></td>
			</tr>
			<tr>
				<td valign="top" rowspan="3"><%= Bean.messageXML.getfieldTransl("text_message", true) %></td><td valign="top" rowspan="3"><textarea name="text_term_message" id="text_term_message" cols="60" rows="3" class="inputfield" <%=Bean.getMessageLengthTextAreaEvent("length_term_message") %>></textarea></td>
				<td><%= Bean.messageXML.getfieldTransl("end_action_date", false) %></td> <td><%=Bean.getCalendarInputField("end_action_date", "", "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.messageXML.getfieldTransl("repetitions_count", false) %></td> <td><input type="text" name="repetitions_count" size="20" value="" class="inputfield"> </td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("is_archive", true) %> </td> <td align="left"><select name="is_archive" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", true) %></select></td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("length_message", false) %></td><td valign="top"><input type="text" name="length_term_message" id="length_term_message" size="8" value="" readonly="readonly" class="inputfield-ro"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td valign="top"><%= Bean.messageXML.getfieldTransl("basis_for_operation", true) %></td><td valign="top"><textarea name="basis_for_operation" cols="60" rows="3" class="inputfield"></textarea></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/messages/term_messagesupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if (action.equalsIgnoreCase("add")) { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/term_messages.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>
		</table>
		</form>

	<%= Bean.getCalendarScript("begin_action_date", false) %>
	<%= Bean.getCalendarScript("end_action_date", false) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}

} else if (process.equalsIgnoreCase("yes"))	{
    
	String
		cd_term_message_type	= Bean.getDecodeParam(parameters.get("cd_term_message_type")),
		text_term_message		= Bean.getDecodeParam(parameters.get("text_term_message")),
    	begin_action_date		= Bean.getDecodeParam(parameters.get("begin_action_date")),
    	end_action_date			= Bean.getDecodeParam(parameters.get("end_action_date")),
    	repetitions_count		= Bean.getDecodeParam(parameters.get("repetitions_count")),
    	cd_term_message_state	= Bean.getDecodeParam(parameters.get("cd_term_message_state")),
    	basis_for_operation		= Bean.getDecodeParam(parameters.get("basis_for_operation")),
    	is_archive				= Bean.getDecodeParam(parameters.get("is_archive")),
    	id_club					= Bean.getDecodeParam(parameters.get("id_club"));
    
	if (action.equalsIgnoreCase("add")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.add_term_message("+ 
			"?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [10];

		pParam[0] = cd_term_message_type;
		pParam[1] = text_term_message;
		pParam[2] = begin_action_date;
		pParam[3] = end_action_date;
		pParam[4] = repetitions_count;
		pParam[5] = basis_for_operation;
		pParam[6] = cd_term_message_state;
		pParam[7] = is_archive;
		pParam[8] = id_club;
		pParam[9] = Bean.getDateFormat();
		
	 	%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/dispatch/messages/term_messagesspecs.jsp?id=", "../crm/dispatch/messages/term_messages.jsp") %>
		<% 	

	} else if (action.equalsIgnoreCase("remove")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.delete_term_message(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;

	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/messages/term_messages.jsp", "") %>
		<% 	

	} else if (action.equalsIgnoreCase("set_state")) {
		String 	new_state		= Bean.getDecodeParam(parameters.get("new_state"));
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.set_term_message_state(?,?,?)}";

		String[] pParam = new String [2];

		pParam[0] = id;
		pParam[1] = new_state;

		%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id, "") %>
		<% 		

	} else if (action.equalsIgnoreCase("execute")) {
		
		String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
		String callSQL = "";
		ArrayList<String> id_value=new ArrayList<String>();		

		Object current_parameter;
			Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
	    	while(keySetIterator.hasNext()) {
				try{
					key = (String)keySetIterator.next();
					
					if(key.contains("chb")){
						id_value.add(key.substring(3));
					}
					
				}
				catch(Exception ex){
					Bean.writeException(
							"../crm/dispatch/messages/term_messagesupdate.jsp",
							"",
							process,
							action,
							Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.getMessage());
				}
				 
			}
		
	    String[] results = new String[2];
	    String resultInt = "";
	    String resultFull = "0";
	    String resultMessage = "";
	    String resultMessageFull = "";

		String[] pParam = new String [2];
 
		%>

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		if (!(id_value == null)) {
		 for(int counter=0;counter<id_value.size();counter++){
			 callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.set_term_message_state(?,?,?)}";

	    	pParam[0] = id_value.get(counter);
	    	pParam[1] = operation_type;
		
			results = Bean.myCallFunctionParam(callSQL, pParam, 2);
			resultInt = results[0];
			resultMessage = results[1];
			
			%>
			<%= Bean.showCallSQL(callSQL) %>
			<%
			
			if (!("0".equalsIgnoreCase(resultInt))) {
				resultFull = resultInt;
				resultMessageFull = resultMessageFull + "; " +resultMessage;
			}
		 }
		}
		
		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessageFull, 
   	    		"../crm/dispatch/messages/term_messages.jsp?operation_type=" + operation_type + "&specid=", 
   	    		"../crm/dispatch/messages/term_messages.jsp?operation_type=" + operation_type + "&specid=", 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

	} else if (action.equalsIgnoreCase("set")) {
		
		String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
		String callSQL = "";
		ArrayList<String> id_receiver=new ArrayList<String>();
		ArrayList<String> id_prv_receiver=new ArrayList<String>();
		ArrayList<String> id_term=new ArrayList<String>();

		Object current_parameter;
			Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
	    	while(keySetIterator.hasNext()) {
				try{
					key = (String)keySetIterator.next();
					
					if(key.contains("chb_id_receiver")){
						id_receiver.add(key.substring(16));
					}
					if(key.contains("prv_id_receiver")){
						id_prv_receiver.add(key.substring(16));
					}
					if(key.contains("chb_id_term")){
						id_term.add(key.substring(12));
					}
					
				}
				catch(Exception ex){
					Bean.writeException(
							"../crm/dispatch/messages/term_messagesupdate.jsp",
							"",
							process,
							action,
							Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.getMessage());
				}
				 
			}
		
	    String[] results = new String[2];
	    String resultInt = "";
	    String resultFull = "0";
	    String resultMessage = "";
	    String resultMessageFull = "";

		String[] pParam = new String [4];
 
		%>

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		if (!(id_term == null)) {
		 for(int counter=0;counter<id_term.size();counter++){
			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.set_term_message_receiver(?,?,?,?,?)}";

			pParam[0] = "";
			pParam[1] = id;
			pParam[2] = id_term.get(counter);
			pParam[3] = "Y";
		
			results = Bean.myCallFunctionParam(callSQL, pParam, 2);
			resultInt = results[0];
			resultMessage = results[1];
			
			%>
			<%= Bean.showCallSQL(callSQL) %>
			<%
			
			if (!("0".equalsIgnoreCase(resultInt))) {
				resultFull = resultInt;
				resultMessageFull = resultMessageFull + "; " +resultMessage;
			}
		 }
		}
		if (id_receiver.size()>0) {
	 		 for(int counter=0;counter<id_receiver.size();counter++){
	 			
	 			 if (!(id_prv_receiver.contains(id_receiver.get(counter)))) {
		 			 
		        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.set_term_message_receiver(?,?,?,?,?)}";

		    		pParam[0] = id_receiver.get(counter);
		    		pParam[1] = "";
		    		pParam[2] = "";
		    		pParam[3] = "Y";
			
					results = Bean.myCallFunctionParam(callSQL, pParam, 2);
					resultInt = results[0];
					resultMessage = results[1];
					
					%>
					<%= Bean.showCallSQL(callSQL) %>
					<%
					
					if (!("0".equalsIgnoreCase(resultInt))) {
						resultFull = resultInt;
						resultMessageFull = resultMessageFull + "; " +resultMessage;
					}
				}
			}
		}
		

  	 if (id_prv_receiver.size()>0) {
  	 		for(int counter=0;counter<id_prv_receiver.size();counter++){ 
			 	if (!(id_receiver.contains(id_prv_receiver.get(counter)))) {
			   	 			 
			 		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.set_term_message_receiver(?,?,?,?,?)}";

			    	pParam[0] = id_prv_receiver.get(counter);
			    	pParam[1] = "";
			    	pParam[2] = "";
			    	pParam[3] = "N";
			
					results = Bean.myCallFunctionParam(callSQL, pParam, 2);
					resultInt = results[0];
					resultMessage = results[1];
					
					%>
					<%= Bean.showCallSQL(callSQL) %>
					<%
				
					if (!("0".equalsIgnoreCase(resultInt))) {
						resultFull = resultInt;
						resultMessageFull = resultMessageFull + "; " +resultMessage;
					}
					
			 	}
 	 		 }
  	 		
  	 	}
		
		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessageFull, 
   	    		"../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id, 
   	    		"../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

	} else if (action.equalsIgnoreCase("edit")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.update_term_message(" + 
			"?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [9];

		pParam[0] = id;
		pParam[1] = text_term_message;
		pParam[2] = begin_action_date;
		pParam[3] = end_action_date;
		pParam[4] = repetitions_count;
		pParam[5] = basis_for_operation;
		pParam[6] = cd_term_message_state;
		pParam[7] = is_archive;
		pParam[8] = Bean.getDateFormat();
		
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/term_messagesspecs.jsp?id=" + id, "") %>
		<% 	

	} else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
	}

} else {
    %> <%= Bean.getUnknownProcessText(process) %> <%
}

%>


</body>
</html>
