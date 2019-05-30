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

String pageFormName = "SETUP_DICTIONARY";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";


String[] id_value = null;

String callSQL = "";
Object current_parameter;
	Set<String> keySet = parameters.keySet();
	Iterator<String> keySetIterator = keySet.iterator();
	String key = "";
	while(keySetIterator.hasNext()) {
		try{
			key = (String)keySetIterator.next();
			if(key.equalsIgnoreCase("ID")){
				id_value=request.getParameterValues(key);
			}
			
		}
		catch(Exception ex){
			Bean.writeException(
					"../crm/setup/dictionaryupdate.jsp",
					"",
					process,
					action,
					Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
		}
		 
	}
	


%>
<%
  if (process.equalsIgnoreCase("yes"))
	{    
    String[] results = new String[2];
    String resultInt = "";
    String resultFull = "0";
    String resultMessage = "";
    String resultMessageFull = "";

    String[] pParam = new String [1];

	if (action.equalsIgnoreCase("delete")) { %>

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<%
		if (!(id_value == null)) {
		 for(int counter=0;counter<id_value.length;counter++){
	    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$SYS_EVENT_UI.delete_event(?,?)}";

			pParam[0] = id_value[counter];
		
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
   	    		"../crm/setup/events.jsp?specid=", 
   	    		"../crm/setup/events.jsp?specid=", 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
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
