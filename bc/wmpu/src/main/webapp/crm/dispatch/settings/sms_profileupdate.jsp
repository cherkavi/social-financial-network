<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcDsClientProfileObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcClubShortObject"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "DISPATCH_SETTINGS_SMS_PROFILE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id			= Bean.getDecodeParam(parameters.get("id"));
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));

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
		<script>
			var formData = new Array (
					new Array ('name_sms_profile', 'varchar2', 1),
					new Array ('name_club', 'varchar2', 1),
					new Array ('device_serial_number', 'varchar2', 1),
					new Array ('max_send_repeat_count', 'varchar2', 1),
					new Array ('delay_message_msec', 'varchar2', 1),
					new Array ('max_delivery_time_sec', 'varchar2', 1),
					new Array ('check_new_messages_time_msec', 'varchar2', 1),
					new Array ('analyse_old_messages_time_msec', 'varchar2', 1)
			);
		</script>

		<%= Bean.getOperationTitle(
			Bean.messageXML.getfieldTransl("h_add_profile", false),
			"Y",
			"N") 
		%>
        <form action="../crm/dispatch/settings/sms_profileupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">

		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("cd_profile_state", false) %></td><td><select name="cd_profile_state" class="inputfield"><%= Bean.getDSProfileStateOptions("ACTIVE",true) %></select></td>			
	 		    <td class="bottom_line"><%= Bean.clubXML.getfieldTransl("club", true) %>
					<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
				</td>
			  	<td class="bottom_line">
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  		</td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_profile", true) %></td> <td align="left"><input type="text" name="name_sms_profile" size="74" value="" class="inputfield"></td>
				<td><%= Bean.smsXML.getfieldTransl("name_sms_device_type", false) %></td> <td align="left"><input type="text" name="name_sms_device_type" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td rowspan=8 valign="top"><%= Bean.smsXML.getfieldTransl("desc_sms_profile", false) %></td> <td rowspan=8><textarea name="desc_sms_profile" cols="70" rows="7" class="inputfield"></textarea></td>
				<td><%= Bean.smsXML.getfieldTransl("device_serial_number", true) %></td> <td align="left"><input type="text" name="device_serial_number" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("mobile_operator", false) %></td> <td align="left"><input type="text" name="mobile_operator" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("phone_mobile", false) %></td> <td align="left"><input type="text" name="phone_mobile" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td class="top_line"><%= Bean.smsXML.getfieldTransl("max_send_repeat_count", true) %></td> <td align="left" class="top_line"><input type="text" name="max_send_repeat_count" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("delay_message_msec", true) %></td> <td align="left"><input type="text" name="delay_message_msec" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("max_delivery_time_sec", true) %></td> <td align="left"><input type="text" name="max_delivery_time_sec" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("check_new_messages_time_msec", true) %></td> <td align="left"><input type="text" name="check_new_messages_time_msec" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.smsXML.getfieldTransl("analyse_old_messages_time_msec", true) %></td> <td align="left"><input type="text" name="analyse_old_messages_time_msec" size="30" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/dispatch/settings/sms_profileupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<% if (action.equalsIgnoreCase("add")) { %>
						<%=Bean.getGoBackButton("../crm/dispatch/settings/sms_profiles.jsp") %>
					<% } else { %>
						<%=Bean.getGoBackButton("../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id) %>
					<% } %>
				</td>
			</tr>

		</table>

	</form>

	        <%
	    	} else if (action.equalsIgnoreCase("change_profile")) {
	    		
	    		String smsId = "";

	    		String callSQL = "";
	    		Object current_parameter;
	    			Set<String> keySet = parameters.keySet();
	    			Iterator<String> keySetIterator = keySet.iterator();
	    			String key = "";
	    			while(keySetIterator.hasNext()) {
	    				try{
	    					key = (String)keySetIterator.next();
	    					
	    					if(key.contains("chb")){
	    						smsId = smsId + "<input type=\"hidden\" id=\""+key+"\" name=\""+key+"\" value=\"Y\">";
	    					}
	    					
	    				}
	    				catch(Exception ex){
	    					Bean.writeException(
	    							"../crm/dispatch/settings/sms_profileupdate.jsp",
	    							"",
	    							process,
	    							action,
	    							Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.getMessage());
	    				}
	    				 
	    			}		    		
		    		
	    		%> 
	    		<script>
	    			var formData = new Array (
	    					new Array ('id_sms_profile', 'varchar2', 1)
	    			);
	    		</script>

	    		<%= Bean.getOperationTitle(
	    			Bean.smsXML.getfieldTransl("h_change_sms_profile", false),
	    			"Y",
	    			"N") 
	    		%>
	            <form action="../crm/dispatch/settings/sms_profileupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	    		        <input type="hidden" name="id" value="<%=id %>">
	    		        <input type="hidden" name="type" value="general">
	    		        <input type="hidden" name="action" value="change_profile">
	    		        <input type="hidden" name="process" value="yes">
						<%=smsId %>

	    		<table <%=Bean.getTableDetailParam() %>>
	    			<tr>
	    				<td><%= Bean.smsXML.getfieldTransl("name_sms_profile", true) %></td><td><select name="id_new_profile" class="inputfield"><%= Bean.getDispatchClientSMSProfileExcludeSelectedOptions(id,false) %></select></td>			
	    			</tr>
	    			<tr>
	    				<td colspan="6" align="center">
	    					<%=Bean.getSubmitButtonAjax("../crm/dispatch/settings/sms_profileupdate.jsp") %>
	    					<%=Bean.getResetButton() %>
    						<%=Bean.getGoBackButton("../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id) %>
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
		name_sms_profile				= Bean.getDecodeParam(parameters.get("name_sms_profile")),
		desc_sms_profile				= Bean.getDecodeParam(parameters.get("desc_sms_profile")),
		cd_profile_state				= Bean.getDecodeParam(parameters.get("cd_profile_state")),
		name_sms_device_type			= Bean.getDecodeParam(parameters.get("name_sms_device_type")),
		mobile_operator					= Bean.getDecodeParam(parameters.get("mobile_operator")),
		device_serial_number			= Bean.getDecodeParam(parameters.get("device_serial_number")),
		phone_mobile					= Bean.getDecodeParam(parameters.get("phone_mobile")),
		max_send_repeat_count			= Bean.getDecodeParam(parameters.get("max_send_repeat_count")),
		delay_message_msec				= Bean.getDecodeParam(parameters.get("delay_message_msec")),
		max_delivery_time_sec			= Bean.getDecodeParam(parameters.get("max_delivery_time_sec")),
		check_new_messages_time_msec	= Bean.getDecodeParam(parameters.get("check_new_messages_time_msec")),
		analyse_old_messages_time_msec	= Bean.getDecodeParam(parameters.get("analyse_old_messages_time_msec")),
		id_club							= Bean.getDecodeParam(parameters.get("id_club"));
	    
	if (action.equalsIgnoreCase("add")) { 
	    	
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.add_sms_profile("+
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [13];

		pParam[0] = name_sms_profile;
		pParam[1] = desc_sms_profile;
		pParam[2] = cd_profile_state;
		pParam[3] = name_sms_device_type;
		pParam[4] = device_serial_number;
		pParam[5] = mobile_operator;
		pParam[6] = phone_mobile;
		pParam[7] = max_send_repeat_count;
		pParam[8] = delay_message_msec;
		pParam[9] = max_delivery_time_sec;
		pParam[10] = check_new_messages_time_msec;
		pParam[11] = analyse_old_messages_time_msec;
		pParam[12] = id_club;
			
	 	%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/dispatch/settings/sms_profilespecs.jsp?id=" , "../crm/dispatch/settings/sms_profiles.jsp") %>
		<% 	

	} else if (action.equalsIgnoreCase("remove")) { 
	    	
	   	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.delete_sms_profile(?,?)}";

		String[] pParam = new String [11];

		pParam[0] = id;
			
	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/dispatch/settings/sms_profiles.jsp" , "") %>
		<% 	

	} else if (action.equalsIgnoreCase("edit")) { 
			
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.update_sms_profile("+
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [13];

		pParam[0] = id;
		pParam[1] = name_sms_profile;
		pParam[2] = desc_sms_profile;
		pParam[3] = cd_profile_state;
		pParam[4] = name_sms_device_type;
		pParam[5] = device_serial_number;
		pParam[6] = mobile_operator;
		pParam[7] = phone_mobile;
		pParam[8] = max_send_repeat_count;
		pParam[9] = delay_message_msec;
		pParam[10] = max_delivery_time_sec;
		pParam[11] = check_new_messages_time_msec;
		pParam[12] = analyse_old_messages_time_msec;
			
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/settings/sms_profilespecs.jsp?id=" + id, "") %>
		<% 	

	} else if (action.equalsIgnoreCase("execute")) {
		
		String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
		
		ArrayList<String> id_value=new ArrayList<String>();

		String callSQL = "";
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
							"../crm/dispatch/settings/sms_profileupdate.jsp",
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

			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.set_sms_action(?,?,?)}";

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
   	    		"../crm/dispatch/settings/sms_profilespecs.jsp?operation_type=" + operation_type + "&id=" + id, 
   	    		"../crm/dispatch/settings/sms_profilespecs.jsp?operation_type=" + operation_type + "&id=" + id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

	} else if (action.equalsIgnoreCase("change_profile")) {

		String id_new_profile 	= Bean.getDecodeParam(parameters.get("id_new_profile"));

		ArrayList<String> id_value=new ArrayList<String>();

		String callSQL = "";
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
							"../crm/dispatch/settings/sms_profileupdate.jsp",
							"",
							"",
							"",
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
			 callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_SMS.change_sms_profile(?,?,?)}";

			pParam[0] = id_value.get(counter);
			pParam[1] = id_new_profile;
		
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
   	    		"../crm/dispatch/settings/sms_profilespecs.jsp?operation_type=change_profile&id=" + id, 
   	    		"../crm/dispatch/settings/sms_profilespecs.jsp?operation_type=change_profile&id=" + id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 
	} else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
	}
} else {
    %> <%= Bean.getUnknownProcessText(process) %> <%
} %>
</body>
</html>

