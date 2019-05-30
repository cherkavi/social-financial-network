<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Enumeration"%>
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

String pageFormName = "DISPATCH_MESSAGES_TERM";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("yes"))	{
    
	String
		repetitions_count	= Bean.getDecodeParam(parameters.get("repetitions_count")),
    	is_archive			= Bean.getDecodeParam(parameters.get("is_archive"));
    
	if (action.equalsIgnoreCase("edit")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_DS_PARTNER.update_term_message_receiver(?,?,?,?)}";

		String[] pParam = new String [4];

		pParam[0] = id;
		pParam[1] = repetitions_count;
		pParam[2] = is_archive;
		
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/dispatch/messages/term_message_receiverspecs.jsp?id=" + id, "") %>
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
