<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcReglamentObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.connection.Connector"%>
<%@page import="java.sql.Connection"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SECURITY_REGLAMENTS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action"));
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("no")) {
	
} else if (process.equalsIgnoreCase("yes"))	{
    
	if (action.equalsIgnoreCase("remove")) {
    	
		Connector.dropUserConnection(id);
		
	 	%>
		<%=Bean.showCallResult(
   	    		"", 
   	    		"0", 
   	    		"", 
   	    		"../crm/security/admin.jsp", 
   	    		"../crm/security/admin.jsp", 
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
