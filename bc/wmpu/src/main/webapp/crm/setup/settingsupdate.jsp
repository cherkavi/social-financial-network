<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SETUP_SETTINGS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("yes")) {
    
	String  
    	CD_PARAM		= Bean.getDecodeParam(parameters.get("CD_PARAM")),
    	VALUE_PARAM		= Bean.getDecodeParam(parameters.get("VALUE_PARAM"));
    
    if (action.equalsIgnoreCase("edit")) {
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$SYS_PARAM_UI.set_param_value(?,?,?)}";

		String[] pParam = new String [2];

		pParam[0] = CD_PARAM;
		pParam[1] = VALUE_PARAM;
		
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/setup/settingspecs.jsp?id=" + CD_PARAM, "") %>
		<% 	

    } else { %> 
    	<%= Bean.getUnknownActionText(action) %><% 
    }
} else {%> 
	<%= Bean.getUnknownProcessText(process) %>  <%
}

%>


</body>
</html>
