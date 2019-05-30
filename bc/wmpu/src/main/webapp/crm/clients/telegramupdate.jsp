<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcTerminalSessionObject"%>

<%= Bean.getLogOutScript(request) %>
<html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_TERMSES";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type			= Bean.getDecodeParam(parameters.get("type"));
String id			= Bean.getDecodeParam(parameters.get("id")); 
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

%>
</head>
<body topmargin="0">

<%
if (type.equalsIgnoreCase("posting")) {
	  if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("run")) { 
	       
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_SES_UI.run_telgr_posting(?,?,?)}";

	 	   	String[] pParam = new String [1];
	 		 	    	      				
	 	    pParam[0] = id;
	
			%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/clients/telegramspecs.jsp?id=" + id+ "&id_report=", "") %>
			<%

		} else if (action.equalsIgnoreCase("removeall")) { 
	       
	       	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_SES_UI.delete_telgr_posting(?,?)}";

	 	   	String[] pParam = new String [1];
	 		 	    	      				
	 	    pParam[0] = id;
	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/clients/telegramspecs.jsp?id=" + id, "") %>
			<% 	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %><%
}
%>
</body>

<%@page import="java.util.HashMap"%></html>