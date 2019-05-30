<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcCallCenterFAQObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CALL_CENTER_SETTINGS";

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
	<script>
		var formDataFAQ = new Array (
			new Array ('name_cc_setting', 'varchar2', 1),
			new Array ('cd_cc_setting_state', 'varchar2', 1),
			new Array ('send_email', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataFAQ);
		}
	</script> 
		<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_add_setting", false),
				"Y",
				"N") 
		%>
    <form action="../crm/call_center/settingupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_setting_state", true) %></td><td><select name="cd_cc_setting_state" class="inputfield"><%= Bean.getCallCenterSettingStateOptions("ENABLE", true) %></select></td>
			<td><%= Bean.call_centerXML.getfieldTransl("send_email", true) %></td><td><select name="send_email" class="inputfield"><%= Bean.getYesNoLookupOptions("Y", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("name_cc_setting", true) %></td><td><input type="text" name="name_cc_setting" size="60" value="" class="inputfield"></td>
			<td><%= Bean.call_centerXML.getfieldTransl("receiver_emails", false) %></td><td><input type="text" name="receiver_emails" size="60" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("desc_cc_setting", false) %></td><td><textarea name="desc_cc_setting" cols="56" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.call_centerXML.getfieldTransl("title_email", false) %></td><td><input type="text" name="title_email" size="60" value="" class="inputfield"></td>
		</tr>
 		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/settingupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/call_center/setting.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/call_center/settingspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	

</form>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	}

	else if (process.equalsIgnoreCase("yes")){
    	String
    		name_cc_setting 				= Bean.getDecodeParam(parameters.get("name_cc_setting")),
    		desc_cc_setting 				= Bean.getDecodeParam(parameters.get("desc_cc_setting")),
    		cd_cc_setting_state 			= Bean.getDecodeParam(parameters.get("cd_cc_setting_state")),
    		send_email 						= Bean.getDecodeParam(parameters.get("send_email")),
    		receiver_emails 				= Bean.getDecodeParam(parameters.get("receiver_emails")),
    		title_email		 				= Bean.getDecodeParam(parameters.get("title_email"));


		if (action.equalsIgnoreCase("add")) { 

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_setting("+
				"?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(name_cc_setting);
			pParam.add(desc_cc_setting);
			pParam.add(cd_cc_setting_state);
			pParam.add(send_email);
			pParam.add(receiver_emails);
			pParam.add(title_email);
		
			%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/call_center/settingspecs.jsp?id=", "../crm/call_center/setting.jsp") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 

		   	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.delete_setting(?,?)}";

		   	ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/call_center/setting.jsp", "") %>
			<%
		
		} else if (action.equalsIgnoreCase("edit")) { 
		
	    	String callSQL = "";
	   	    String resultInt = "";
	   	    String resultFull = "0";
	   	    String resultMessage = "";
	   	    String resultMessageFull = "";
	   	    String[] results = new String[4];

	    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_setting(" + 
	    		"?,?,?,?,?,?,?,?)}";

	    	ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);
			pParam.add(name_cc_setting);
			pParam.add(desc_cc_setting);
			pParam.add(cd_cc_setting_state);
			pParam.add(send_email);
			pParam.add(receiver_emails);
			pParam.add(title_email);
		
			results = Bean.myCallFunctionParam(callSQL, pParam, results.length);
			resultInt = results[0];
			resultMessage = results[1];
				
			%>
			<%= Bean.showCallSQL(callSQL) %>
			<%
			
			if (!("0".equalsIgnoreCase(resultInt))) {
				resultFull = resultInt;
				resultMessageFull = resultMessageFull + "; " +resultMessage;
			}

			ArrayList<String> event_value=new ArrayList<String>();
			ArrayList<String> prv_event_value=new ArrayList<String>();
			ArrayList<String> warning_value=new ArrayList<String>();
			ArrayList<String> prv_warning_value=new ArrayList<String>();
	
	    	Set<String> keySet = parameters.keySet();
			Iterator<String> keySetIterator = keySet.iterator();
			String key = "";
	    	while(keySetIterator.hasNext()) {
	   			try{
	   				key = (String)keySetIterator.next();
	   				if(key.startsWith("cd_event_type")){
	   					event_value.add(key.substring(14));
	   				}
	   				if(key.startsWith("prv_cd_event_type")){
	   					prv_event_value.add(key.substring(18));
	   				}
	   				if(key.startsWith("cd_warning_type")){
	   					warning_value.add(key.substring(16));
	   				}
	   				if(key.startsWith("prv_cd_warning_type")){
	   					prv_warning_value.add(key.substring(20));
	   				}
	   			}
	   			catch(Exception ex){
	   				Bean.writeException(
	   						"../crm/call_center/settingupdate.jsp",
	   						type,
	   						process,
	   						action,
	   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
	   			}
	   		}
	
	    	if (event_value.size()>0) {
	  	 		 for(int counter=0;counter<event_value.size();counter++){ 
	  	 			 if (!(prv_event_value.contains(event_value.get(counter)))) {
	  	 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + 
			        		".PACK_UI_CALL_CENTER.add_setting_event_type(?,?,?)}";
			        		
			        	ArrayList<String> pParam2 = new ArrayList<String>();

			    		pParam2.add(id);
			    		pParam2.add(event_value.get(counter));
				
						results = Bean.myCallFunctionParam(callSQL, pParam2, results.length);
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
	   	 	if (prv_event_value.size()>0) {
	   	 		for(int counter=0;counter<prv_event_value.size();counter++){ 
				 	if (!(event_value.contains(prv_event_value.get(counter)))) {
				   	 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + 
			        		".PACK_UI_CALL_CENTER.delete_setting_event_type(?,?,?)}";

			        	ArrayList<String> pParam2 = new ArrayList<String>();
			        	
				    	pParam2.add(id);
				    	pParam2.add(prv_event_value.get(counter));
					
				    	results = Bean.myCallFunctionParam(callSQL, pParam2, results.length);
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
	
	   	    if (warning_value.size()>0) {
	  	 		 for(int counter=0;counter<warning_value.size();counter++){ 
	  	 			 if (!(prv_warning_value.contains(warning_value.get(counter)))) {
	  	 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + 
			        		".PACK_UI_CALL_CENTER.add_setting_warning_type(?,?,?)}";

			        	ArrayList<String> pParam2 = new ArrayList<String>();
			        		
					    pParam2.add(id);
					    pParam2.add(warning_value.get(counter));
						
					    results = Bean.myCallFunctionParam(callSQL, pParam2, results.length);
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
	   	 	if (prv_warning_value.size()>0) {
	   	 		for(int counter=0;counter<prv_warning_value.size();counter++){ 
				 	if (!(warning_value.contains(prv_warning_value.get(counter)))) {
				   	 				 
			        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + 
			        		".PACK_UI_CALL_CENTER.delete_setting_warning_type(?,?,?)}";

			        	ArrayList<String> pParam2 = new ArrayList<String>();
			        	
						pParam2.add(id);
						pParam2.add(prv_warning_value.get(counter));
							
						results = Bean.myCallFunctionParam(callSQL, pParam2, results.length);
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
	   	    		"../crm/call_center/settingspecs.jsp?id=" + id, 
	   	    		"../crm/call_center/settingspecs.jsp?id=" + id, 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
