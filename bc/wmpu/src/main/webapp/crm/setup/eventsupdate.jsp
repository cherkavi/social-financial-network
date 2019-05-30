<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" import="java.util.*" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SETUP_EVENTS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

%>
<%
if (process.equalsIgnoreCase("no")) {
	if (action.equalsIgnoreCase("delete_pattern")) {
		%>
		<%= Bean.getOperationTitle(
				Bean.eventXML.getfieldTransl("h_delete_event_for_pattern", false),
				"Y",
				"N") 
		%>
        <form action="../crm/setup/eventsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	        <input type="hidden" name="action" value="delete_pattern">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
	        <tr>
				<td><%= Bean.eventXML.getfieldTransl("delete_pattern", false) %></td> <td><input type="text" name="pattern_value" size="100" value="" class="inputfield"></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td><%= Bean.eventXML.getfieldTransl("desc_event_type", false) %></td> <td><select name="cd_event_type"  class="inputfield"><%= Bean.getEventTypeOptions("", true) %></select> </td>
				<td colspan="2">&nbsp;</td>
			</tr>
	 		<tr>
				<td colspan="4" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/setup/eventsupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/setup/events.jsp") %>
				</td>
			</tr>

		</table>

	</form>

	<%
	} else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
	}
} else if (process.equalsIgnoreCase("yes"))	{    
	String callSQL = "";
    String[] results = new String[2];
    String resultInt = "";
    String resultFull = "0";
    String resultMessage = "";
    String resultMessageFull = "";

    if (action.equalsIgnoreCase("delete")) {

		ArrayList<String> id_value=new ArrayList<String>();
	
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
						"../crm/setup/eventsupdate.jsp",
							"",
							process,
							action,
							Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
			}
			 
		}
		 %>
		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		if (!(id_value == null)) {
			
		 String[] pParam = new String [1];
			
		 for(int counter=0;counter<id_value.size();counter++){
	    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$SYS_EVENT_UI.delete_event(?,?)}";
		
	    	pParam[0] = id_value.get(counter);
	    	
			results = Bean.myCallFunctionParam(callSQL, pParam, 2);
			resultInt = results[0];
			resultMessage = results[1];
			
			%>
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
   	    		"../crm/setup/events.jsp", 
   	    		"../crm/setup/events.jsp", 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

    } else if (action.equalsIgnoreCase("delete_pattern")) {
    	String
			pattern_value		= Bean.getDecodeParam(parameters.get("pattern_value")),
			cd_event_type		= Bean.getDecodeParam(parameters.get("cd_event_type"));
    	
    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$SYS_EVENT_UI.delete_event_for_pattern(?,?,?)}";

		String[] pParam = new String [2];

		pParam[0] = pattern_value;
		pParam[1] = cd_event_type;
				
   		%>
   		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/setup/events.jsp", "") %>
   		<% 	
		
	} else { %> 
    	<%= Bean.getUnknownActionText(action) %><% 
    }
} else {%> 
	<%= Bean.getUnknownProcessText(process) %> <%
}
%>

</body>
</html>
