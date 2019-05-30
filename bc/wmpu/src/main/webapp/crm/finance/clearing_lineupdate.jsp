<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_CLEARING";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));
String type		= Bean.getDecodeParam(parameters.get("type"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("set_state")) {
 
	if (process.equalsIgnoreCase("yes")) {
    
		String id_clearing					= Bean.getDecodeParam(parameters.get("id_clearing"));
		String need_clearing_line_export	= Bean.getDecodeParam(parameters.get("need_clearing_line_export"));
		
    	if (action.equalsIgnoreCase("update")) { 

    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLEARING.set_need_clearing_line_export(?,?,?,?)}";

		String[] pParam = new String [3];

		pParam[0] = id_clearing;
		pParam[1] = id;
		pParam[2] = need_clearing_line_export;
		
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/clearing_linespecs.jsp?id=" + id, "") %>
		<% 	
       	} else { %> 
       		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {
    	%> <%= Bean.getUnknownProcessText(process) %> <%
 	}

} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
