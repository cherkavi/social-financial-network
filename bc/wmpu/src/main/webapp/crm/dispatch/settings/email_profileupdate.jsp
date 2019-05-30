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

String pageFormName = "DISPATCH_SETTINGS_EMAIL_PROFILE";

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
					Bean.emailXML.getfieldTransl("h_add_profile", false),
					"Y",
					"N") 
			%>
        <script>
			var formData = new Array (
				new Array ('name_email_profile', 'varchar2', 1),
				new Array ('cd_profile_state', 'varchar2', 1),
				new Array ('delay_next_letter', 'integer', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('sender_email', 'varchar2', 1),
				new Array ('smtp_server', 'varchar2', 1),
				new Array ('smtp_port', 'integer', 1),
				new Array ('smtp_ssl', 'varchar2', 1),
				new Array ('need_autorization', 'varchar2', 1)
			);
			var formAutorization = new Array (
				new Array ('smtp_user', 'varchar2', 1),
				new Array ('smtp_password', 'varchar2', 1)
			);
			function myValidateForm() {
				var needAutoriz = document.getElementById('need_autorization').value;
				if (needAutoriz == 'Y') {
					return validateForm(formData.concat(formAutorization));
				} else {
					return validateForm(formData);
				}
			}

			function checkAutorization(){
				var needAutoriz = document.getElementById('need_autorization').value;
				if (needAutoriz == 'Y') {
					document.getElementById('span_smtp_user').innerHTML='<%= Bean.emailXML.getfieldTransl("smtp_user", true) %>';
					document.getElementById('span_smtp_password').innerHTML='<%= Bean.emailXML.getfieldTransl("smtp_password", true) %>';
				} else {
					document.getElementById('span_smtp_user').innerHTML='<%= Bean.emailXML.getfieldTransl("smtp_user", false) %>';
					document.getElementById('span_smtp_password').innerHTML='<%= Bean.emailXML.getfieldTransl("smtp_password", false) %>';
				}
			}
			checkAutorization();
		</script>

    	<form action="../crm/dispatch/settings/email_profileupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("name_email_profile", true) %></td> <td align="left"><input type="text" name="name_email_profile" size="70" value="" class="inputfield"></td>
 		    <td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td class="bottom_line">
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td rowspan=4 valign="top"><%= Bean.emailXML.getfieldTransl("desc_email_profile", false) %></td> <td rowspan=4><textarea name="desc_email_profile" cols="67" rows="5" class="inputfield"></textarea></td>
			<td><%= Bean.emailXML.getfieldTransl("sender_email", true) %></td> <td align="left"><input type="text" name="sender_email" size="30" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("smtp_server", true) %></td> <td align="left"><input type="text" name="smtp_server" size="30" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("smtp_port", true) %></td> <td align="left"><input type="text" name="smtp_port" size="30" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("smtp_ssl", true) %></td> <td><select name="smtp_ssl" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("cd_profile_state", true) %></td><td><select name="cd_profile_state" class="inputfield"><%= Bean.getDSProfileStateOptions("ACTIVE",true) %></select></td>			
			<td><%= Bean.emailXML.getfieldTransl("need_autorization", true) %></td> <td><select name="need_autorization" id="need_autorization" class="inputfield" onchange="checkAutorization()"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("delay_next_letter", true) %></td> <td align="left"><input type="text" name="delay_next_letter" size="30" value="" class="inputfield"></td>
			<td><span id="span_smtp_user"><%= Bean.emailXML.getfieldTransl("smtp_user", false) %></span></td> <td align="left"><input type="text" name="smtp_user" size="30" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.emailXML.getfieldTransl("max_error_messages", false) %></td> <td align="left"><input type="text" name="max_error_messages" size="30" value="" class="inputfield"></td>
			<td><span id="span_smtp_password"><%= Bean.emailXML.getfieldTransl("smtp_password", false) %></span></td> <td align="left"><input type="password" name="smtp_password" size="30" value="" class="inputfield"></td>
		</tr>
		<tr>
		</tr>
 		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/dispatch/settings/email_profileupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/dispatch/settings/email_profiles.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/dispatch/settings/email_profilespecs.jsp?id=" + id) %>
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
		name_email_profile 	= Bean.getDecodeParam(parameters.get("name_email_profile")),
		desc_email_profile 	= Bean.getDecodeParam(parameters.get("desc_email_profile")),
		cd_profile_state 	= Bean.getDecodeParam(parameters.get("cd_profile_state")),
		sender_email 		= Bean.getDecodeParam(parameters.get("sender_email")),
		smtp_server 		= Bean.getDecodeParam(parameters.get("smtp_server")),
		smtp_port 			= Bean.getDecodeParam(parameters.get("smtp_port")),
		smtp_ssl 			= Bean.getDecodeParam(parameters.get("smtp_ssl")),
		smtp_user 			= Bean.getDecodeParam(parameters.get("smtp_user")),
		smtp_password 		= Bean.getDecodeParam(parameters.get("smtp_password")),
		need_autorization 	= Bean.getDecodeParam(parameters.get("need_autorization")),
		delay_next_letter 	= Bean.getDecodeParam(parameters.get("delay_next_letter")),
		max_error_messages 	= Bean.getDecodeParam(parameters.get("max_error_messages")),
		id_club			 	= Bean.getDecodeParam(parameters.get("id_club"));

	if (action.equalsIgnoreCase("add")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_EMAIL.add_email_profile(" +
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [13];

		pParam[0] = name_email_profile;
		pParam[1] = desc_email_profile;
		pParam[2] = cd_profile_state;
		pParam[3] = sender_email;
		pParam[4] = smtp_server;
		pParam[5] = smtp_port;
		pParam[6] = smtp_ssl;
		pParam[7] = need_autorization;
		pParam[8] = smtp_user;
		pParam[9] = smtp_password;
		pParam[10] = delay_next_letter;
		pParam[11] = max_error_messages;
		pParam[12] = id_club;
		
	 	%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/dispatch/settings/email_profilespecs.jsp?id=", "../crm/dispatch/settings/email_profiles.jsp") %>
		<% 	

	} else if (action.equalsIgnoreCase("remove")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_EMAIL.delete_email_profile(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;

	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/settings/email_profiles.jsp", "") %>
		<% 	

	} else if (action.equalsIgnoreCase("edit")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_EMAIL.update_email_profile(" + 
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [13];

		pParam[0] = id;
		pParam[1] = name_email_profile;
		pParam[2] = desc_email_profile;
		pParam[3] = cd_profile_state;
		pParam[4] = sender_email;
		pParam[5] = smtp_server;
		pParam[6] = smtp_port;
		pParam[7] = smtp_ssl;
		pParam[8] = need_autorization;
		pParam[9] = smtp_user;
		pParam[10] = smtp_password;
		pParam[11] = delay_next_letter;
		pParam[12] = max_error_messages;
		
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/settings/email_profilespecs.jsp?id=" + id, "") %>
		<% 	

	} else if (action.equalsIgnoreCase("execute")) {
		
		String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
		String callSQL = "";
		ArrayList<String> id_value=new ArrayList<String>();		
		ArrayList<String> recepient_type=new ArrayList<String>();
    	
		Object current_parameter;
	    int _position = 0;
    	String fullValue = "";
    	
	    	Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
    		
			while(keySetIterator.hasNext()) {
				try{
					key = (String)keySetIterator.next();
					
					if(key.contains("chb")){
						fullValue = key.substring(3);
						_position = fullValue.indexOf("_");
						id_value.add(fullValue.substring(0, _position));
						recepient_type.add(fullValue.substring(_position+1));
					}
					
				}
				catch(Exception ex){
					Bean.writeException(
							"../crm/dispatch/settings/email_profileupdate.jsp",
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
 
		%>

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		if (!(id_value == null)) {
		 for(int counter=0;counter<id_value.size();counter++){
			 if ("CLIENT".equalsIgnoreCase(recepient_type.get(counter))) {
				callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_CLIENT.set_message_state(?,?,?,?)}";

				String[] pParam = new String [3];

				pParam[0] = id_value.get(counter);
				pParam[1] = "EMAIL";
				pParam[2] = operation_type;
				
				results = Bean.myCallFunctionParam(callSQL, pParam, 2);
				resultInt = results[0];
				resultMessage = results[1];
				
			 } else if ("PARTNER".equalsIgnoreCase(recepient_type.get(counter))) {
				 callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.set_contact_prs_message_state('" +
		    		id_value.get(counter)+"','"+operation_type+"',?)}";

				String[] pParam = new String [2];

				pParam[0] = id_value.get(counter);
				pParam[1] = operation_type;
					
				results = Bean.myCallFunctionParam(callSQL, pParam, 2);
				resultInt = results[0];
				resultMessage = results[1];
					
			 }
			
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
   	    		"../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&operation_type=" + operation_type + "&specid=", 
   	    		"../crm/dispatch/settings/email_profilespecs.jsp?id=" + id + "&operation_type=" + operation_type + "&specid=", 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
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
